package android.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.IAudioService;
import android.media.VolumeShaper;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.AndroidRuntimeException;
import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class SoundPool extends PlayerBase {
    private static final boolean DEBUG;
    private static final int SAMPLE_LOADED = 1;
    private static final int STOP_NOTIFY = 100;
    private static final String SYSTEM_SOUND_CONFIG = "persist.audio.system_sound";
    private static final String SYSTEM_SOUND_NOTPLAY = "void.xppolicy.system.notplay";
    private static final String TAG = "SoundPool";
    private static xEventHandler mXHandler;
    private static IAudioService sService;
    private final AudioAttributes mAttributes;
    private EventHandler mEventHandler;
    private boolean mHasAppOpsPlayAudio;
    private final Object mLock;
    private long mNativeContext;
    private OnLoadCompleteListener mOnLoadCompleteListener;

    /* loaded from: classes2.dex */
    public interface OnLoadCompleteListener {
        void onLoadComplete(SoundPool soundPool, int i, int i2);
    }

    private final native int _load(FileDescriptor fileDescriptor, long j, long j2, int i);

    private final native void _mute(boolean z);

    private final native int _play(int i, float f, float f2, int i2, int i3, float f3);

    private final native void _setVolume(int i, float f, float f2);

    private final native void native_release();

    private final native int native_setup(Object obj, int i, Object obj2);

    public final native void autoPause();

    public final native void autoResume();

    public final native void pause(int i);

    public final native void resume(int i);

    public final native void setLoop(int i, int i2);

    public final native void setPriority(int i, int i2);

    public final native void setRate(int i, float f);

    public final native void stop(int i);

    public final native boolean unload(int i);

    static {
        System.loadLibrary("soundpool");
        DEBUG = Log.isLoggable(TAG, 3);
    }

    public SoundPool(int maxStreams, int streamType, int srcQuality) {
        this(maxStreams, new AudioAttributes.Builder().setInternalLegacyStreamType(streamType).build());
        PlayerBase.deprecateStreamTypeForPlayback(streamType, TAG, "SoundPool()");
    }

    private SoundPool(int maxStreams, AudioAttributes attributes) {
        super(attributes, 3);
        if (native_setup(new WeakReference(this), maxStreams, attributes) != 0) {
            throw new RuntimeException("Native setup failed");
        }
        this.mLock = new Object();
        this.mAttributes = attributes;
        baseRegisterPlayer();
    }

    public final void release() {
        baseRelease();
        native_release();
    }

    protected void finalize() {
        release();
    }

    public int load(String path, int priority) {
        int id = 0;
        try {
            File f = new File(path);
            ParcelFileDescriptor fd = ParcelFileDescriptor.open(f, 268435456);
            if (fd == null) {
                return 0;
            }
            id = _load(fd.getFileDescriptor(), 0L, f.length(), priority);
            fd.close();
            return id;
        } catch (IOException e) {
            Log.e(TAG, "error loading " + path);
            return id;
        }
    }

    public int load(Context context, int resId, int priority) {
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(resId);
        int id = 0;
        if (afd != null) {
            id = _load(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength(), priority);
            try {
                afd.close();
            } catch (IOException e) {
            }
        }
        return id;
    }

    public int load(AssetFileDescriptor afd, int priority) {
        if (afd != null) {
            long len = afd.getLength();
            if (len < 0) {
                throw new AndroidRuntimeException("no length for fd");
            }
            return _load(afd.getFileDescriptor(), afd.getStartOffset(), len, priority);
        }
        return 0;
    }

    public int load(FileDescriptor fd, long offset, long length, int priority) {
        return _load(fd, offset, length, priority);
    }

    public final int play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) {
        float leftVolume2;
        float rightVolume2;
        Log.d(TAG, "play() " + this.mAttributes.getUsage());
        boolean mSystemSoundOn = SystemProperties.getBoolean(SYSTEM_SOUND_CONFIG, true);
        boolean mPolicyNotPlay = SystemProperties.getBoolean(SYSTEM_SOUND_NOTPLAY, false);
        if (5 == this.mAttributes.getUsage() && !mSystemSoundOn) {
            Log.d(TAG, "do not play, because mSystemSoundOn is not on!");
            return 0;
        }
        IAudioService service = getService();
        if (service != null) {
            try {
                if (5 == this.mAttributes.getUsage() && service.isKaraokeOn()) {
                    Log.d(TAG, "do not play, because karaoke is on!");
                    return 0;
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        if (mPolicyNotPlay && (5 == this.mAttributes.getUsage() || 13 == this.mAttributes.getUsage())) {
            Log.d(TAG, "do not play, because policy not allowed!");
            return 0;
        }
        baseStart();
        int testvol = SystemProperties.getInt("persist.syssoundpool.test", 0);
        if (testvol == 0) {
            leftVolume2 = leftVolume;
            rightVolume2 = rightVolume;
        } else {
            float leftVolume3 = testvol / 100.0f;
            leftVolume2 = leftVolume3;
            rightVolume2 = testvol / 100.0f;
        }
        int id = _play(soundID, leftVolume2, rightVolume2, priority, loop, rate);
        baseStart_sysPatch(id);
        return id;
    }

    public final int play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate, int location, int fadeTime) {
        IAudioService service = getService();
        baseStartOrigin();
        if (service != null) {
            try {
                service.checkAlarmVolume();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        int ret = _play(soundID, leftVolume, rightVolume, priority, loop, rate);
        Log.d(TAG, "play location:" + location + " fadeTime:" + fadeTime + " usage:" + this.mAttributes.getUsage());
        if (service != null && ret != 0 && this.mAttributes.getUsage() == 4) {
            try {
                if (this.mAttributes.getUsage() == 4) {
                    service.setStreamPosition(4, "xuiAudio", location, -1);
                }
                baseLocationStart(this.mAttributes.getUsage(), location, ret, fadeTime);
            } catch (RemoteException e2) {
                throw e2.rethrowFromSystemServer();
            }
        }
        return ret;
    }

    public final void setVolume(int streamID, float leftVolume, float rightVolume) {
        _setVolume(streamID, leftVolume, rightVolume);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.media.PlayerBase
    public int playerApplyVolumeShaper(VolumeShaper.Configuration configuration, VolumeShaper.Operation operation) {
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.media.PlayerBase
    public VolumeShaper.State playerGetVolumeShaperState(int id) {
        return null;
    }

    @Override // android.media.PlayerBase
    void playerSetVolume(boolean muting, float leftVolume, float rightVolume) {
        _mute(muting);
    }

    @Override // android.media.PlayerBase
    int playerSetAuxEffectSendLevel(boolean muting, float level) {
        return 0;
    }

    @Override // android.media.PlayerBase
    void playerStart() {
    }

    @Override // android.media.PlayerBase
    void playerPause() {
    }

    @Override // android.media.PlayerBase
    void playerStop() {
    }

    public void setVolume(int streamID, float volume) {
        setVolume(streamID, volume, volume);
    }

    public void setOnLoadCompleteListener(OnLoadCompleteListener listener) {
        synchronized (this.mLock) {
            if (listener != null) {
                Looper looper = Looper.myLooper();
                if (looper != null) {
                    this.mEventHandler = new EventHandler(looper);
                } else {
                    Looper looper2 = Looper.getMainLooper();
                    if (looper2 != null) {
                        this.mEventHandler = new EventHandler(looper2);
                    } else {
                        this.mEventHandler = null;
                    }
                }
            } else {
                this.mEventHandler = null;
            }
            this.mOnLoadCompleteListener = listener;
        }
    }

    private static void postEventFromNative(Object ref, int msg, int arg1, int arg2, Object obj) {
        SoundPool soundPool = (SoundPool) ((WeakReference) ref).get();
        Log.d(TAG, "postEventFromNative  msg:" + msg);
        if (soundPool == null) {
            return;
        }
        if (msg == 100) {
            soundPool.baseReleaseUsage(arg1, arg2);
            return;
        }
        EventHandler eventHandler = soundPool.mEventHandler;
        if (eventHandler != null) {
            Message m = eventHandler.obtainMessage(msg, arg1, arg2, obj);
            soundPool.mEventHandler.sendMessage(m);
        }
    }

    /* loaded from: classes2.dex */
    private final class xEventHandler extends Handler {
        public xEventHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                int usage = msg.arg1;
                int id = msg.arg2;
                Log.d(SoundPool.TAG, "get notify!! usage:" + usage + " id:" + id);
                return;
            }
            Log.e(SoundPool.TAG, "Unknown message type " + msg.what);
        }
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

    /* loaded from: classes2.dex */
    private final class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i != 1) {
                if (i != 100) {
                    Log.e(SoundPool.TAG, "Unknown message type " + msg.what);
                    return;
                }
                return;
            }
            if (SoundPool.DEBUG) {
                Log.d(SoundPool.TAG, "Sample " + msg.arg1 + " loaded");
            }
            synchronized (SoundPool.this.mLock) {
                if (SoundPool.this.mOnLoadCompleteListener != null) {
                    SoundPool.this.mOnLoadCompleteListener.onLoadComplete(SoundPool.this, msg.arg1, msg.arg2);
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private AudioAttributes mAudioAttributes;
        private int mMaxStreams = 1;

        public Builder setMaxStreams(int maxStreams) throws IllegalArgumentException {
            if (maxStreams <= 0) {
                throw new IllegalArgumentException("Strictly positive value required for the maximum number of streams");
            }
            this.mMaxStreams = maxStreams;
            return this;
        }

        public Builder setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
            if (attributes == null) {
                throw new IllegalArgumentException("Invalid null AudioAttributes");
            }
            this.mAudioAttributes = attributes;
            return this;
        }

        public SoundPool build() {
            if (this.mAudioAttributes == null) {
                this.mAudioAttributes = new AudioAttributes.Builder().setUsage(1).build();
            }
            return new SoundPool(this.mMaxStreams, this.mAudioAttributes);
        }
    }
}
