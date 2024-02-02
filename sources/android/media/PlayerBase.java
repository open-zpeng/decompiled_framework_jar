package android.media;

import android.app.ActivityThread;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.IAudioService;
import android.media.IPlayer;
import android.media.VolumeShaper;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.IAppOpsCallback;
import com.android.internal.app.IAppOpsService;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class PlayerBase {
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_APP_OPS = false;
    private static final String TAG = "PlayerBase";
    private static final boolean newPolicyOpen = AudioManager.newPolicyOpen;
    private static IAudioService sService;
    private IAppOpsService mAppOps;
    private IAppOpsCallback mAppOpsCallback;
    protected AudioAttributes mAttributes;
    private final int mImplType;
    @GuardedBy("mLock")
    private int mState;
    protected float mLeftVolume = 1.0f;
    protected float mRightVolume = 1.0f;
    protected float mAuxEffectSendLevel = 0.0f;
    private final Object mLock = new Object();
    @GuardedBy("mLock")
    private boolean mHasAppOpsPlayAudio = true;
    private int mPlayerIId = 0;
    @GuardedBy("mLock")
    private int mStartDelayMs = 0;
    @GuardedBy("mLock")
    private float mPanMultiplierL = 1.0f;
    @GuardedBy("mLock")
    private float mPanMultiplierR = 1.0f;
    private HandlerThread mHandlerThread = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract synchronized int playerApplyVolumeShaper(VolumeShaper.Configuration configuration, VolumeShaper.Operation operation);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract synchronized VolumeShaper.State playerGetVolumeShaperState(int i);

    abstract synchronized void playerPause();

    abstract synchronized int playerSetAuxEffectSendLevel(boolean z, float f);

    abstract synchronized void playerSetVolume(boolean z, float f, float f2);

    abstract synchronized void playerStart();

    abstract synchronized void playerStop();

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized PlayerBase(AudioAttributes attr, int implType) {
        if (attr == null) {
            throw new IllegalArgumentException("Illegal null AudioAttributes");
        }
        this.mAttributes = attr;
        this.mImplType = implType;
        this.mState = 1;
        Log.d(TAG, "PlayerBase " + this.mAttributes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void baseRegisterPlayer() {
        int newPiid = -1;
        IBinder b = ServiceManager.getService(Context.APP_OPS_SERVICE);
        this.mAppOps = IAppOpsService.Stub.asInterface(b);
        updateAppOpsPlayAudio();
        this.mAppOpsCallback = new IAppOpsCallbackWrapper(this);
        try {
            this.mAppOps.startWatchingMode(28, ActivityThread.currentPackageName(), this.mAppOpsCallback);
        } catch (RemoteException e) {
            Log.e(TAG, "Error registering appOps callback", e);
            this.mHasAppOpsPlayAudio = false;
        }
        try {
            newPiid = getService().trackPlayer(new PlayerIdCard(this.mImplType, this.mAttributes, new IPlayerWrapper(this)));
        } catch (RemoteException e2) {
            Log.e(TAG, "Error talking to audio service, player will not be tracked", e2);
        }
        this.mPlayerIId = newPiid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void baseUpdateAudioAttributes(AudioAttributes attr) {
        if (attr == null) {
            throw new IllegalArgumentException("Illegal null AudioAttributes");
        }
        try {
            getService().playerAttributes(this.mPlayerIId, attr);
        } catch (RemoteException e) {
            Log.e(TAG, "Error talking to audio service, STARTED state will not be tracked", e);
        }
        synchronized (this.mLock) {
            boolean attributesChanged = this.mAttributes != attr;
            this.mAttributes = attr;
            updateAppOpsPlayAudio_sync(attributesChanged);
        }
    }

    private synchronized void updateState(int state) {
        int piid;
        synchronized (this.mLock) {
            this.mState = state;
            piid = this.mPlayerIId;
        }
        try {
            getService().playerEvent(piid, state);
        } catch (RemoteException e) {
            Log.e(TAG, "Error talking to audio service, " + AudioPlaybackConfiguration.toLogFriendlyPlayerState(state) + " state will not be tracked for piid=" + piid, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void baseStart() {
        Log.v(TAG, "baseStart() piid=" + this.mPlayerIId);
        updateState(2);
        synchronized (this.mLock) {
            IAudioService service = getService();
            if (service != null) {
                try {
                    service.audioThreadProcess(1, this.mAttributes.getUsage(), AudioAttributes.toXpStreamType(this.mAttributes), this.mPlayerIId, ActivityThread.currentPackageName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isRestricted_sync()) {
                playerSetVolume(true, 0.0f, 0.0f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void baseStart_sysPatch(int id) {
        synchronized (this.mLock) {
            IAudioService service = getService();
            if (service != null) {
                try {
                    service.audioThreadProcess(2, this.mAttributes.getUsage(), AudioAttributes.toXpStreamType(this.mAttributes), id, ActivityThread.currentPackageName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void baseStartXui() {
        Log.v(TAG, "baseStartXui() piid=" + this.mPlayerIId + " usage:" + this.mAttributes.getUsage());
        IAudioService service = getService();
        if (service != null) {
            try {
                service.audioThreadProcess(3, this.mAttributes.getUsage(), AudioAttributes.toXpStreamType(this.mAttributes), this.mPlayerIId, ActivityThread.currentPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateState(2);
        synchronized (this.mLock) {
            if (isRestricted_sync()) {
                playerSetVolume(true, 0.0f, 0.0f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void baseStartOrigin() {
        Log.v(TAG, "baseStartOrigin() piid=" + this.mPlayerIId + " usage:" + this.mAttributes.getUsage());
        updateState(2);
        synchronized (this.mLock) {
            if (isRestricted_sync()) {
                playerSetVolume(true, 0.0f, 0.0f);
            }
        }
    }

    void baseApplyUsage() {
        Log.v(TAG, "baseApplyUsage() piid=" + this.mPlayerIId);
        IAudioService service = getService();
        if (service != null) {
            try {
                service.audioThreadProcess(4, this.mAttributes.getUsage(), AudioAttributes.toXpStreamType(this.mAttributes), this.mPlayerIId, ActivityThread.currentPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void baseReleaseUsage(int usage, int id) {
        Log.v(TAG, "baseReleaseUsage() piid=" + this.mPlayerIId + " usage:" + usage);
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(usage);
        attrBuilder.build();
        IAudioService service = getService();
        if (service != null) {
            try {
                service.audioThreadProcess(5, this.mAttributes.getUsage(), AudioAttributes.toXpStreamType(this.mAttributes), newPolicyOpen ? this.mPlayerIId : id, ActivityThread.currentPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void baseSetStartDelayMs(int delayMs) {
        synchronized (this.mLock) {
            this.mStartDelayMs = Math.max(delayMs, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized int getStartDelayMs() {
        int i;
        synchronized (this.mLock) {
            i = this.mStartDelayMs;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void basePause() {
        Log.v(TAG, "basePause() piid=" + this.mPlayerIId);
        IAudioService service = getService();
        if (service != null) {
            try {
                service.audioThreadProcess(6, this.mAttributes.getUsage(), AudioAttributes.toXpStreamType(this.mAttributes), this.mPlayerIId, ActivityThread.currentPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateState(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void baseStop() {
        Log.v(TAG, "baseStop() piid=" + this.mPlayerIId);
        IAudioService service = getService();
        if (service != null) {
            try {
                service.audioThreadProcess(7, this.mAttributes.getUsage(), AudioAttributes.toXpStreamType(this.mAttributes), this.mPlayerIId, ActivityThread.currentPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateState(4);
    }

    synchronized void baseSetPan(float pan) {
        float p = Math.min(Math.max(-1.0f, pan), 1.0f);
        synchronized (this.mLock) {
            try {
                if (p >= 0.0f) {
                    this.mPanMultiplierL = 1.0f - p;
                    this.mPanMultiplierR = 1.0f;
                } else {
                    this.mPanMultiplierL = 1.0f;
                    this.mPanMultiplierR = 1.0f + p;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        baseSetVolume(this.mLeftVolume, this.mRightVolume);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void baseSetVolume(float leftVolume, float rightVolume) {
        boolean isRestricted;
        synchronized (this.mLock) {
            this.mLeftVolume = leftVolume;
            this.mRightVolume = rightVolume;
            isRestricted = isRestricted_sync();
        }
        playerSetVolume(isRestricted, this.mPanMultiplierL * leftVolume, this.mPanMultiplierR * rightVolume);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int baseSetAuxEffectSendLevel(float level) {
        synchronized (this.mLock) {
            this.mAuxEffectSendLevel = level;
            if (isRestricted_sync()) {
                return 0;
            }
            return playerSetAuxEffectSendLevel(false, level);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void baseRelease() {
        Log.v(TAG, "baseRelease() piid=" + this.mPlayerIId + " state=" + this.mState);
        boolean releasePlayer = false;
        IAudioService service = getService();
        if (service != null) {
            try {
                service.audioThreadProcess(8, this.mAttributes.getUsage(), AudioAttributes.toXpStreamType(this.mAttributes), this.mPlayerIId, ActivityThread.currentPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.mHandlerThread != null) {
            Log.d(TAG, "mHandlerThread.quit()");
            this.mHandlerThread.quit();
            this.mHandlerThread = null;
        }
        synchronized (this.mLock) {
            if (this.mState != 0) {
                releasePlayer = true;
                this.mState = 0;
            }
        }
        if (releasePlayer) {
            try {
                getService().releasePlayer(this.mPlayerIId);
            } catch (RemoteException e2) {
                Log.e(TAG, "Error talking to audio service, the player will still be tracked", e2);
            }
        }
        try {
            if (this.mAppOps != null) {
                this.mAppOps.stopWatchingMode(this.mAppOpsCallback);
            }
        } catch (Exception e3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateAppOpsPlayAudio() {
        synchronized (this.mLock) {
            updateAppOpsPlayAudio_sync(false);
        }
    }

    synchronized void updateAppOpsPlayAudio_sync(boolean attributesChanged) {
        boolean oldHasAppOpsPlayAudio = this.mHasAppOpsPlayAudio;
        int mode = 1;
        try {
            if (this.mAppOps != null) {
                mode = this.mAppOps.checkAudioOperation(28, this.mAttributes.getUsage(), Process.myUid(), ActivityThread.currentPackageName());
            }
            this.mHasAppOpsPlayAudio = mode == 0;
        } catch (RemoteException e) {
            this.mHasAppOpsPlayAudio = false;
        }
        try {
            if (oldHasAppOpsPlayAudio != this.mHasAppOpsPlayAudio || attributesChanged) {
                getService().playerHasOpPlayAudio(this.mPlayerIId, this.mHasAppOpsPlayAudio);
                if (isRestricted_sync()) {
                    playerSetVolume(true, 0.0f, 0.0f);
                    playerSetAuxEffectSendLevel(true, 0.0f);
                    return;
                }
                playerSetVolume(false, this.mLeftVolume * this.mPanMultiplierL, this.mRightVolume * this.mPanMultiplierR);
                playerSetAuxEffectSendLevel(false, this.mAuxEffectSendLevel);
            }
        } catch (Exception e2) {
        }
    }

    synchronized boolean isRestricted_sync() {
        if (!this.mHasAppOpsPlayAudio && (this.mAttributes.getAllFlags() & 64) == 0) {
            if ((this.mAttributes.getAllFlags() & 1) != 0 && this.mAttributes.getUsage() == 13) {
                boolean cameraSoundForced = false;
                try {
                    cameraSoundForced = getService().isCameraSoundForced();
                } catch (RemoteException e) {
                    Log.e(TAG, "Cannot access AudioService in isRestricted_sync()");
                } catch (NullPointerException e2) {
                    Log.e(TAG, "Null AudioService in isRestricted_sync()");
                }
                if (cameraSoundForced) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static synchronized IAudioService getService() {
        if (sService != null) {
            return sService;
        }
        IBinder b = ServiceManager.getService("audio");
        sService = IAudioService.Stub.asInterface(b);
        return sService;
    }

    public synchronized void setStartDelayMs(int delayMs) {
        baseSetStartDelayMs(delayMs);
    }

    /* loaded from: classes.dex */
    private static class IAppOpsCallbackWrapper extends IAppOpsCallback.Stub {
        private final WeakReference<PlayerBase> mWeakPB;

        public synchronized IAppOpsCallbackWrapper(PlayerBase pb) {
            this.mWeakPB = new WeakReference<>(pb);
        }

        @Override // com.android.internal.app.IAppOpsCallback
        public synchronized void opChanged(int op, int uid, String packageName) {
            PlayerBase pb;
            if (op == 28 && (pb = this.mWeakPB.get()) != null) {
                pb.updateAppOpsPlayAudio();
            }
        }
    }

    /* loaded from: classes.dex */
    private static class IPlayerWrapper extends IPlayer.Stub {
        private final WeakReference<PlayerBase> mWeakPB;

        public synchronized IPlayerWrapper(PlayerBase pb) {
            this.mWeakPB = new WeakReference<>(pb);
        }

        @Override // android.media.IPlayer
        public synchronized void start() {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.playerStart();
            }
        }

        @Override // android.media.IPlayer
        public synchronized void pause() {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.playerPause();
            }
        }

        @Override // android.media.IPlayer
        public synchronized void stop() {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.playerStop();
            }
        }

        @Override // android.media.IPlayer
        public synchronized void setVolume(float vol) {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.baseSetVolume(vol, vol);
            }
        }

        @Override // android.media.IPlayer
        public synchronized void setPan(float pan) {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.baseSetPan(pan);
            }
        }

        @Override // android.media.IPlayer
        public synchronized void setStartDelayMs(int delayMs) {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.baseSetStartDelayMs(delayMs);
            }
        }

        @Override // android.media.IPlayer
        public synchronized void applyVolumeShaper(VolumeShaper.Configuration configuration, VolumeShaper.Operation operation) {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.playerApplyVolumeShaper(configuration, operation);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class PlayerIdCard implements Parcelable {
        public static final int AUDIO_ATTRIBUTES_DEFINED = 1;
        public static final int AUDIO_ATTRIBUTES_NONE = 0;
        public static final Parcelable.Creator<PlayerIdCard> CREATOR = new Parcelable.Creator<PlayerIdCard>() { // from class: android.media.PlayerBase.PlayerIdCard.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public PlayerIdCard createFromParcel(Parcel p) {
                return new PlayerIdCard(p);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public PlayerIdCard[] newArray(int size) {
                return new PlayerIdCard[size];
            }
        };
        public final AudioAttributes mAttributes;
        public final IPlayer mIPlayer;
        public final int mPlayerType;

        synchronized PlayerIdCard(int type, AudioAttributes attr, IPlayer iplayer) {
            this.mPlayerType = type;
            this.mAttributes = attr;
            this.mIPlayer = iplayer;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mPlayerType));
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mPlayerType);
            this.mAttributes.writeToParcel(dest, 0);
            dest.writeStrongBinder(this.mIPlayer == null ? null : this.mIPlayer.asBinder());
        }

        private synchronized PlayerIdCard(Parcel in) {
            this.mPlayerType = in.readInt();
            this.mAttributes = AudioAttributes.CREATOR.createFromParcel(in);
            IBinder b = in.readStrongBinder();
            this.mIPlayer = b == null ? null : IPlayer.Stub.asInterface(b);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof PlayerIdCard)) {
                return false;
            }
            PlayerIdCard that = (PlayerIdCard) o;
            if (this.mPlayerType == that.mPlayerType && this.mAttributes.equals(that.mAttributes)) {
                return true;
            }
            return false;
        }
    }

    public static synchronized void deprecateStreamTypeForPlayback(int streamType, String className, String opName) throws IllegalArgumentException {
        Log.w(className, "Use of stream types is deprecated for operations other than volume control");
        Log.w(className, "See the documentation of " + opName + " for what to use instead with android.media.AudioAttributes to qualify your playback use case");
    }
}
