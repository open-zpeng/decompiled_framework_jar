package android.media.audiopolicy;

import android.Manifest;
import android.annotation.SystemApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusInfo;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.IAudioService;
import android.media.audiopolicy.IAudioPolicyCallback;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@SystemApi
/* loaded from: classes.dex */
public class AudioPolicy {
    private static final boolean DEBUG = false;
    public static final int FOCUS_POLICY_DUCKING_DEFAULT = 0;
    @SystemApi
    public static final int FOCUS_POLICY_DUCKING_IN_APP = 0;
    @SystemApi
    public static final int FOCUS_POLICY_DUCKING_IN_POLICY = 1;
    private static final int MSG_FOCUS_ABANDON = 5;
    private static final int MSG_FOCUS_GRANT = 1;
    private static final int MSG_FOCUS_LOSS = 2;
    private static final int MSG_FOCUS_REQUEST = 4;
    private static final int MSG_MIX_STATE_UPDATE = 3;
    private static final int MSG_POLICY_STATUS_CHANGE = 0;
    private static final int MSG_VOL_ADJUST = 6;
    @SystemApi
    public static final int POLICY_STATUS_REGISTERED = 2;
    @SystemApi
    public static final int POLICY_STATUS_UNREGISTERED = 1;
    private static final String TAG = "AudioPolicy";
    private static IAudioService sService;
    private AudioPolicyConfig mConfig;
    private Context mContext;
    private final EventHandler mEventHandler;
    private AudioPolicyFocusListener mFocusListener;
    private boolean mIsFocusPolicy;
    private final Object mLock;
    private final IAudioPolicyCallback mPolicyCb;
    private String mRegistrationId;
    private int mStatus;
    private AudioPolicyStatusListener mStatusListener;
    private final AudioPolicyVolumeCallback mVolCb;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface PolicyStatus {
    }

    public synchronized AudioPolicyConfig getConfig() {
        return this.mConfig;
    }

    public synchronized boolean hasFocusListener() {
        return this.mFocusListener != null;
    }

    public synchronized boolean isFocusPolicy() {
        return this.mIsFocusPolicy;
    }

    public synchronized boolean isVolumeController() {
        return this.mVolCb != null;
    }

    private synchronized AudioPolicy(AudioPolicyConfig config, Context context, Looper looper, AudioPolicyFocusListener fl, AudioPolicyStatusListener sl, boolean isFocusPolicy, AudioPolicyVolumeCallback vc) {
        this.mLock = new Object();
        this.mPolicyCb = new IAudioPolicyCallback.Stub() { // from class: android.media.audiopolicy.AudioPolicy.1
            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public void notifyAudioFocusGrant(AudioFocusInfo afi, int requestResult) {
                AudioPolicy.this.sendMsg(1, afi, requestResult);
            }

            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public void notifyAudioFocusLoss(AudioFocusInfo afi, boolean wasNotified) {
                AudioPolicy.this.sendMsg(2, afi, wasNotified ? 1 : 0);
            }

            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public void notifyAudioFocusRequest(AudioFocusInfo afi, int requestResult) {
                AudioPolicy.this.sendMsg(4, afi, requestResult);
            }

            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public void notifyAudioFocusAbandon(AudioFocusInfo afi) {
                AudioPolicy.this.sendMsg(5, afi, 0);
            }

            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public void notifyMixStateUpdate(String regId, int state) {
                Iterator<AudioMix> it = AudioPolicy.this.mConfig.getMixes().iterator();
                while (it.hasNext()) {
                    AudioMix mix = it.next();
                    if (mix.getRegistration().equals(regId)) {
                        mix.mMixState = state;
                        AudioPolicy.this.sendMsg(3, mix, 0);
                    }
                }
            }

            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public void notifyVolumeAdjust(int adjustment) {
                AudioPolicy.this.sendMsg(6, null, adjustment);
            }
        };
        this.mConfig = config;
        this.mStatus = 1;
        this.mContext = context;
        looper = looper == null ? Looper.getMainLooper() : looper;
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            this.mEventHandler = null;
            Log.e(TAG, "No event handler due to looper without a thread");
        }
        this.mFocusListener = fl;
        this.mStatusListener = sl;
        this.mIsFocusPolicy = isFocusPolicy;
        this.mVolCb = vc;
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static class Builder {
        private Context mContext;
        private AudioPolicyFocusListener mFocusListener;
        private Looper mLooper;
        private AudioPolicyStatusListener mStatusListener;
        private AudioPolicyVolumeCallback mVolCb;
        private boolean mIsFocusPolicy = false;
        private ArrayList<AudioMix> mMixes = new ArrayList<>();

        @SystemApi
        public Builder(Context context) {
            this.mContext = context;
        }

        @SystemApi
        public Builder addMix(AudioMix mix) throws IllegalArgumentException {
            if (mix == null) {
                throw new IllegalArgumentException("Illegal null AudioMix argument");
            }
            this.mMixes.add(mix);
            return this;
        }

        @SystemApi
        public Builder setLooper(Looper looper) throws IllegalArgumentException {
            if (looper == null) {
                throw new IllegalArgumentException("Illegal null Looper argument");
            }
            this.mLooper = looper;
            return this;
        }

        @SystemApi
        public void setAudioPolicyFocusListener(AudioPolicyFocusListener l) {
            this.mFocusListener = l;
        }

        @SystemApi
        public Builder setIsAudioFocusPolicy(boolean isFocusPolicy) {
            this.mIsFocusPolicy = isFocusPolicy;
            return this;
        }

        @SystemApi
        public void setAudioPolicyStatusListener(AudioPolicyStatusListener l) {
            this.mStatusListener = l;
        }

        @SystemApi
        public Builder setAudioPolicyVolumeCallback(AudioPolicyVolumeCallback vc) {
            if (vc == null) {
                throw new IllegalArgumentException("Invalid null volume callback");
            }
            this.mVolCb = vc;
            return this;
        }

        @SystemApi
        public AudioPolicy build() {
            if (this.mStatusListener != null) {
                Iterator<AudioMix> it = this.mMixes.iterator();
                while (it.hasNext()) {
                    AudioMix mix = it.next();
                    mix.mCallbackFlags |= 1;
                }
            }
            if (this.mIsFocusPolicy && this.mFocusListener == null) {
                throw new IllegalStateException("Cannot be a focus policy without an AudioPolicyFocusListener");
            }
            return new AudioPolicy(new AudioPolicyConfig(this.mMixes), this.mContext, this.mLooper, this.mFocusListener, this.mStatusListener, this.mIsFocusPolicy, this.mVolCb);
        }
    }

    @SystemApi
    public int attachMixes(List<AudioMix> mixes) {
        int status;
        if (mixes == null) {
            throw new IllegalArgumentException("Illegal null list of AudioMix");
        }
        synchronized (this.mLock) {
            if (this.mStatus != 2) {
                throw new IllegalStateException("Cannot alter unregistered AudioPolicy");
            }
            ArrayList<AudioMix> zeMixes = new ArrayList<>(mixes.size());
            for (AudioMix mix : mixes) {
                if (mix == null) {
                    throw new IllegalArgumentException("Illegal null AudioMix in attachMixes");
                }
                zeMixes.add(mix);
            }
            AudioPolicyConfig cfg = new AudioPolicyConfig(zeMixes);
            IAudioService service = getService();
            try {
                status = service.addMixForPolicy(cfg, cb());
                if (status == 0) {
                    this.mConfig.add(zeMixes);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in attachMixes", e);
                return -1;
            }
        }
        return status;
    }

    @SystemApi
    public int detachMixes(List<AudioMix> mixes) {
        int status;
        if (mixes == null) {
            throw new IllegalArgumentException("Illegal null list of AudioMix");
        }
        synchronized (this.mLock) {
            if (this.mStatus != 2) {
                throw new IllegalStateException("Cannot alter unregistered AudioPolicy");
            }
            ArrayList<AudioMix> zeMixes = new ArrayList<>(mixes.size());
            for (AudioMix mix : mixes) {
                if (mix == null) {
                    throw new IllegalArgumentException("Illegal null AudioMix in detachMixes");
                }
                zeMixes.add(mix);
            }
            AudioPolicyConfig cfg = new AudioPolicyConfig(zeMixes);
            IAudioService service = getService();
            try {
                status = service.removeMixForPolicy(cfg, cb());
                if (status == 0) {
                    this.mConfig.remove(zeMixes);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in detachMixes", e);
                return -1;
            }
        }
        return status;
    }

    public void setRegistration(String regId) {
        synchronized (this.mLock) {
            this.mRegistrationId = regId;
            this.mConfig.setRegistration(regId);
            if (regId != null) {
                this.mStatus = 2;
            } else {
                this.mStatus = 1;
            }
        }
        sendMsg(0);
    }

    private synchronized boolean policyReadyToUse() {
        synchronized (this.mLock) {
            if (this.mStatus != 2) {
                Log.e(TAG, "Cannot use unregistered AudioPolicy");
                return false;
            } else if (this.mContext == null) {
                Log.e(TAG, "Cannot use AudioPolicy without context");
                return false;
            } else if (this.mRegistrationId == null) {
                Log.e(TAG, "Cannot use unregistered AudioPolicy");
                return false;
            } else if (this.mContext.checkCallingOrSelfPermission(Manifest.permission.MODIFY_AUDIO_ROUTING) != 0) {
                Slog.w(TAG, "Cannot use AudioPolicy for pid " + Binder.getCallingPid() + " / uid " + Binder.getCallingUid() + ", needs MODIFY_AUDIO_ROUTING");
                return false;
            } else {
                return true;
            }
        }
    }

    private synchronized void checkMixReadyToUse(AudioMix mix, boolean forTrack) throws IllegalArgumentException {
        if (mix == null) {
            String msg = forTrack ? "Invalid null AudioMix for AudioTrack creation" : "Invalid null AudioMix for AudioRecord creation";
            throw new IllegalArgumentException(msg);
        } else if (!this.mConfig.mMixes.contains(mix)) {
            throw new IllegalArgumentException("Invalid mix: not part of this policy");
        } else {
            if ((mix.getRouteFlags() & 2) != 2) {
                throw new IllegalArgumentException("Invalid AudioMix: not defined for loop back");
            }
            if (forTrack && mix.getMixType() != 1) {
                throw new IllegalArgumentException("Invalid AudioMix: not defined for being a recording source");
            }
            if (!forTrack && mix.getMixType() != 0) {
                throw new IllegalArgumentException("Invalid AudioMix: not defined for capturing playback");
            }
        }
    }

    @SystemApi
    public int getFocusDuckingBehavior() {
        return this.mConfig.mDuckingPolicy;
    }

    @SystemApi
    public int setFocusDuckingBehavior(int behavior) throws IllegalArgumentException, IllegalStateException {
        int status;
        if (behavior != 0 && behavior != 1) {
            throw new IllegalArgumentException("Invalid ducking behavior " + behavior);
        }
        synchronized (this.mLock) {
            if (this.mStatus != 2) {
                throw new IllegalStateException("Cannot change ducking behavior for unregistered policy");
            }
            if (behavior == 1 && this.mFocusListener == null) {
                throw new IllegalStateException("Cannot handle ducking without an audio focus listener");
            }
            IAudioService service = getService();
            try {
                status = service.setFocusPropertiesForPolicy(behavior, cb());
                if (status == 0) {
                    this.mConfig.mDuckingPolicy = behavior;
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in setFocusPropertiesForPolicy for behavior", e);
                return -1;
            }
        }
        return status;
    }

    @SystemApi
    public AudioRecord createAudioRecordSink(AudioMix mix) throws IllegalArgumentException {
        if (!policyReadyToUse()) {
            Log.e(TAG, "Cannot create AudioRecord sink for AudioMix");
            return null;
        }
        checkMixReadyToUse(mix, false);
        AudioFormat mixFormat = new AudioFormat.Builder(mix.getFormat()).setChannelMask(AudioFormat.inChannelMaskFromOutChannelMask(mix.getFormat().getChannelMask())).build();
        AudioRecord ar = new AudioRecord(new AudioAttributes.Builder().setInternalCapturePreset(8).addTag(addressForTag(mix)).addTag(AudioRecord.SUBMIX_FIXED_VOLUME).build(), mixFormat, AudioRecord.getMinBufferSize(mix.getFormat().getSampleRate(), 12, mix.getFormat().getEncoding()), 0);
        return ar;
    }

    @SystemApi
    public AudioTrack createAudioTrackSource(AudioMix mix) throws IllegalArgumentException {
        if (!policyReadyToUse()) {
            Log.e(TAG, "Cannot create AudioTrack source for AudioMix");
            return null;
        }
        checkMixReadyToUse(mix, true);
        AudioTrack at = new AudioTrack(new AudioAttributes.Builder().setUsage(15).addTag(addressForTag(mix)).build(), mix.getFormat(), AudioTrack.getMinBufferSize(mix.getFormat().getSampleRate(), mix.getFormat().getChannelMask(), mix.getFormat().getEncoding()), 1, 0);
        return at;
    }

    @SystemApi
    public int getStatus() {
        return this.mStatus;
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static abstract class AudioPolicyStatusListener {
        public void onStatusChange() {
        }

        public void onMixStateUpdate(AudioMix mix) {
        }
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static abstract class AudioPolicyFocusListener {
        public void onAudioFocusGrant(AudioFocusInfo afi, int requestResult) {
        }

        public void onAudioFocusLoss(AudioFocusInfo afi, boolean wasNotified) {
        }

        public void onAudioFocusRequest(AudioFocusInfo afi, int requestResult) {
        }

        public void onAudioFocusAbandon(AudioFocusInfo afi) {
        }
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static abstract class AudioPolicyVolumeCallback {
        public void onVolumeAdjustment(int adjustment) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onPolicyStatusChange() {
        synchronized (this.mLock) {
            if (this.mStatusListener == null) {
                return;
            }
            AudioPolicyStatusListener l = this.mStatusListener;
            l.onStatusChange();
        }
    }

    public synchronized IAudioPolicyCallback cb() {
        return this.mPolicyCb;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class EventHandler extends Handler {
        public EventHandler(AudioPolicy ap, Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    AudioPolicy.this.onPolicyStatusChange();
                    return;
                case 1:
                    if (AudioPolicy.this.mFocusListener != null) {
                        AudioPolicy.this.mFocusListener.onAudioFocusGrant((AudioFocusInfo) msg.obj, msg.arg1);
                        return;
                    }
                    return;
                case 2:
                    if (AudioPolicy.this.mFocusListener != null) {
                        AudioPolicy.this.mFocusListener.onAudioFocusLoss((AudioFocusInfo) msg.obj, msg.arg1 != 0);
                        return;
                    }
                    return;
                case 3:
                    if (AudioPolicy.this.mStatusListener != null) {
                        AudioPolicy.this.mStatusListener.onMixStateUpdate((AudioMix) msg.obj);
                        return;
                    }
                    return;
                case 4:
                    if (AudioPolicy.this.mFocusListener != null) {
                        AudioPolicy.this.mFocusListener.onAudioFocusRequest((AudioFocusInfo) msg.obj, msg.arg1);
                        return;
                    } else {
                        Log.e(AudioPolicy.TAG, "Invalid null focus listener for focus request event");
                        return;
                    }
                case 5:
                    if (AudioPolicy.this.mFocusListener != null) {
                        AudioPolicy.this.mFocusListener.onAudioFocusAbandon((AudioFocusInfo) msg.obj);
                        return;
                    } else {
                        Log.e(AudioPolicy.TAG, "Invalid null focus listener for focus abandon event");
                        return;
                    }
                case 6:
                    if (AudioPolicy.this.mVolCb != null) {
                        AudioPolicy.this.mVolCb.onVolumeAdjustment(msg.arg1);
                        return;
                    } else {
                        Log.e(AudioPolicy.TAG, "Invalid null volume event");
                        return;
                    }
                default:
                    Log.e(AudioPolicy.TAG, "Unknown event " + msg.what);
                    return;
            }
        }
    }

    private static synchronized String addressForTag(AudioMix mix) {
        return "addr=" + mix.getRegistration();
    }

    private synchronized void sendMsg(int msg) {
        if (this.mEventHandler != null) {
            this.mEventHandler.sendEmptyMessage(msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendMsg(int msg, Object obj, int i) {
        if (this.mEventHandler != null) {
            this.mEventHandler.sendMessage(this.mEventHandler.obtainMessage(msg, i, 0, obj));
        }
    }

    private static synchronized IAudioService getService() {
        if (sService != null) {
            return sService;
        }
        IBinder b = ServiceManager.getService("audio");
        sService = IAudioService.Stub.asInterface(b);
        return sService;
    }

    public String toLogFriendlyString() {
        String textDump = new String("android.media.audiopolicy.AudioPolicy:\n");
        return textDump + "config=" + this.mConfig.toLogFriendlyString();
    }
}
