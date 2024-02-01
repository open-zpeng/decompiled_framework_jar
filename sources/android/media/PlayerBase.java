package android.media;

import android.app.ActivityThread;
import android.media.AudioAttributes;
import android.media.IAudioService;
import android.media.IPlayer;
import android.media.VolumeShaper;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.IAppOpsCallback;
import com.android.internal.app.IAppOpsService;
import java.lang.ref.WeakReference;
import java.util.Objects;

/* loaded from: classes2.dex */
public abstract class PlayerBase {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_APP_OPS = false;
    private static final String TAG = "PlayerBase";
    private static final boolean USE_AUDIOFLINGER_MUTING_FOR_OP = true;
    private static final boolean newPolicyOpen = AudioManager.newPolicyOpen;
    private static IAudioService sService;
    private IAppOpsService mAppOps;
    private IAppOpsCallback mAppOpsCallback;
    protected AudioAttributes mAttributes;
    private final int mImplType;
    @GuardedBy({"mLock"})
    private int mState;
    protected float mLeftVolume = 1.0f;
    protected float mRightVolume = 1.0f;
    protected float mAuxEffectSendLevel = 0.0f;
    private final Object mLock = new Object();
    @GuardedBy({"mLock"})
    private boolean mHasAppOpsPlayAudio = true;
    private int mPlayerIId = -1;
    @GuardedBy({"mLock"})
    private int mStartDelayMs = 0;
    @GuardedBy({"mLock"})
    private float mPanMultiplierL = 1.0f;
    @GuardedBy({"mLock"})
    private float mPanMultiplierR = 1.0f;
    @GuardedBy({"mLock"})
    private float mVolMultiplier = 1.0f;
    private HandlerThread mHandlerThread = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int playerApplyVolumeShaper(VolumeShaper.Configuration configuration, VolumeShaper.Operation operation);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract VolumeShaper.State playerGetVolumeShaperState(int i);

    abstract void playerPause();

    abstract int playerSetAuxEffectSendLevel(boolean z, float f);

    abstract void playerSetVolume(boolean z, float f, float f2);

    abstract void playerStart();

    abstract void playerStop();

    /* JADX INFO: Access modifiers changed from: package-private */
    public PlayerBase(AudioAttributes attr, int implType) {
        if (attr == null) {
            throw new IllegalArgumentException("Illegal null AudioAttributes");
        }
        this.mAttributes = attr;
        this.mImplType = implType;
        this.mState = 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void baseRegisterPlayer() {
        try {
            this.mPlayerIId = getService().trackPlayer(new PlayerIdCard(this.mImplType, this.mAttributes, new IPlayerWrapper(this)));
        } catch (RemoteException e) {
            Log.e(TAG, "Error talking to audio service, player will not be tracked", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void baseUpdateAudioAttributes(AudioAttributes attr) {
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

    private void updateState(int state) {
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
    public void baseStart() {
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
        updateState(2);
        synchronized (this.mLock) {
            if (isRestricted_sync()) {
                playerSetVolume(true, 0.0f, 0.0f);
            }
        }
    }

    void baseApplyUsage() {
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
    public void baseLocationStart(int usage, int location, int id, int fadetime) {
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(usage);
        attrBuilder.build();
        IAudioService service = getService();
        if (service != null) {
            try {
                Log.d(TAG, "baseLocationStart() piid=" + this.mPlayerIId + " usage:" + usage + " id:" + id);
                service.selectAlarmChannels(location, fadetime, this.mPlayerIId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void baseReleaseUsage(int usage, int id) {
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
    public void baseSetStartDelayMs(int delayMs) {
        synchronized (this.mLock) {
            this.mStartDelayMs = Math.max(delayMs, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getStartDelayMs() {
        int i;
        synchronized (this.mLock) {
            i = this.mStartDelayMs;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void basePause() {
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
    public void baseStop() {
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

    void baseSetPan(float pan) {
        float p = Math.min(Math.max(-1.0f, pan), 1.0f);
        synchronized (this.mLock) {
            if (p >= 0.0f) {
                this.mPanMultiplierL = 1.0f - p;
                this.mPanMultiplierR = 1.0f;
            } else {
                this.mPanMultiplierL = 1.0f;
                this.mPanMultiplierR = 1.0f + p;
            }
        }
        updatePlayerVolume();
    }

    private void updatePlayerVolume() {
        float finalLeftVol;
        float finalRightVol;
        boolean isRestricted;
        synchronized (this.mLock) {
            finalLeftVol = this.mVolMultiplier * this.mLeftVolume * this.mPanMultiplierL;
            finalRightVol = this.mVolMultiplier * this.mRightVolume * this.mPanMultiplierR;
            isRestricted = isRestricted_sync();
        }
        playerSetVolume(isRestricted, finalLeftVol, finalRightVol);
    }

    void setVolumeMultiplier(float vol) {
        synchronized (this.mLock) {
            this.mVolMultiplier = vol;
        }
        updatePlayerVolume();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void baseSetVolume(float leftVolume, float rightVolume) {
        synchronized (this.mLock) {
            this.mLeftVolume = leftVolume;
            this.mRightVolume = rightVolume;
        }
        updatePlayerVolume();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int baseSetAuxEffectSendLevel(float level) {
        synchronized (this.mLock) {
            this.mAuxEffectSendLevel = level;
            if (isRestricted_sync()) {
                return 0;
            }
            return playerSetAuxEffectSendLevel(false, level);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void baseRelease() {
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
    public void updateAppOpsPlayAudio() {
        synchronized (this.mLock) {
            updateAppOpsPlayAudio_sync(false);
        }
    }

    void updateAppOpsPlayAudio_sync(boolean attributesChanged) {
    }

    boolean isRestricted_sync() {
        return false;
    }

    private static IAudioService getService() {
        IAudioService iAudioService = sService;
        if (iAudioService != null) {
            return iAudioService;
        }
        IBinder b = ServiceManager.getService("audio");
        sService = IAudioService.Stub.asInterface(b);
        return sService;
    }

    public void setStartDelayMs(int delayMs) {
        baseSetStartDelayMs(delayMs);
    }

    /* loaded from: classes2.dex */
    private static class IAppOpsCallbackWrapper extends IAppOpsCallback.Stub {
        private final WeakReference<PlayerBase> mWeakPB;

        public IAppOpsCallbackWrapper(PlayerBase pb) {
            this.mWeakPB = new WeakReference<>(pb);
        }

        @Override // com.android.internal.app.IAppOpsCallback
        public void opChanged(int op, int uid, String packageName) {
            PlayerBase pb;
            if (op == 28 && (pb = this.mWeakPB.get()) != null) {
                pb.updateAppOpsPlayAudio();
            }
        }
    }

    /* loaded from: classes2.dex */
    private static class IPlayerWrapper extends IPlayer.Stub {
        private final WeakReference<PlayerBase> mWeakPB;

        public IPlayerWrapper(PlayerBase pb) {
            this.mWeakPB = new WeakReference<>(pb);
        }

        @Override // android.media.IPlayer
        public void start() {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.playerStart();
            }
        }

        @Override // android.media.IPlayer
        public void pause() {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.playerPause();
            }
        }

        @Override // android.media.IPlayer
        public void stop() {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.playerStop();
            }
        }

        @Override // android.media.IPlayer
        public void setVolume(float vol) {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.setVolumeMultiplier(vol);
            }
        }

        @Override // android.media.IPlayer
        public void setPan(float pan) {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.baseSetPan(pan);
            }
        }

        @Override // android.media.IPlayer
        public void setStartDelayMs(int delayMs) {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.baseSetStartDelayMs(delayMs);
            }
        }

        @Override // android.media.IPlayer
        public void applyVolumeShaper(VolumeShaper.Configuration configuration, VolumeShaper.Operation operation) {
            PlayerBase pb = this.mWeakPB.get();
            if (pb != null) {
                pb.playerApplyVolumeShaper(configuration, operation);
            }
        }
    }

    /* loaded from: classes2.dex */
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

        PlayerIdCard(int type, AudioAttributes attr, IPlayer iplayer) {
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
            IPlayer iPlayer = this.mIPlayer;
            dest.writeStrongBinder(iPlayer == null ? null : iPlayer.asBinder());
        }

        private PlayerIdCard(Parcel in) {
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

    public static void deprecateStreamTypeForPlayback(int streamType, String className, String opName) throws IllegalArgumentException {
        Log.w(className, "Use of stream types is deprecated for operations other than volume control");
        Log.w(className, "See the documentation of " + opName + " for what to use instead with android.media.AudioAttributes to qualify your playback use case");
    }
}
