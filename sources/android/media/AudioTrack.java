package android.media;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRouting;
import android.media.AudioTrack;
import android.media.IAudioService;
import android.media.VolumeShaper;
import android.opengl.GLES30;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.NioUtils;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class AudioTrack extends PlayerBase implements AudioRouting, VolumeAutomation {
    private static final int AUDIO_OUTPUT_FLAG_DEEP_BUFFER = 8;
    private static final int AUDIO_OUTPUT_FLAG_FAST = 4;
    public static final int CHANNEL_COUNT_MAX = native_get_FCC_8();
    public static final int ERROR = -1;
    public static final int ERROR_BAD_VALUE = -2;
    public static final int ERROR_DEAD_OBJECT = -6;
    public static final int ERROR_INVALID_OPERATION = -3;
    private static final int ERROR_NATIVESETUP_AUDIOSYSTEM = -16;
    private static final int ERROR_NATIVESETUP_INVALIDCHANNELMASK = -17;
    private static final int ERROR_NATIVESETUP_INVALIDFORMAT = -18;
    private static final int ERROR_NATIVESETUP_INVALIDSTREAMTYPE = -19;
    private static final int ERROR_NATIVESETUP_NATIVEINITFAILED = -20;
    public static final int ERROR_WOULD_BLOCK = -7;
    private static final float GAIN_MAX = 1.0f;
    private static final float GAIN_MIN = 0.0f;
    private static final float HEADER_V2_SIZE_BYTES = 20.0f;
    public static final int MODE_STATIC = 0;
    public static final int MODE_STREAM = 1;
    private static final int NATIVE_EVENT_MARKER = 3;
    private static final int NATIVE_EVENT_MORE_DATA = 0;
    private static final int NATIVE_EVENT_NEW_IAUDIOTRACK = 6;
    private static final int NATIVE_EVENT_NEW_POS = 4;
    private static final int NATIVE_EVENT_STREAM_END = 7;
    public static final int PERFORMANCE_MODE_LOW_LATENCY = 1;
    public static final int PERFORMANCE_MODE_NONE = 0;
    public static final int PERFORMANCE_MODE_POWER_SAVING = 2;
    public static final int PLAYSTATE_PAUSED = 2;
    public static final int PLAYSTATE_PLAYING = 3;
    public static final int PLAYSTATE_STOPPED = 1;
    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_NO_STATIC_DATA = 2;
    public static final int STATE_UNINITIALIZED = 0;
    public static final int SUCCESS = 0;
    private static final int SUPPORTED_OUT_CHANNELS = 7420;
    private static final String TAG = "android.media.AudioTrack";
    public static final int WRITE_BLOCKING = 0;
    public static final int WRITE_NON_BLOCKING = 1;
    private static IAudioService sService;
    private int mAudioFormat;
    private int mAudioType;
    private int mAvSyncBytesRemaining;
    private ByteBuffer mAvSyncHeader;
    private int mChannelConfiguration;
    private int mChannelCount;
    private int mChannelIndexMask;
    private int mChannelMask;
    private int mDataLoadMode;
    private NativePositionEventHandlerDelegate mEventHandlerDelegate;
    private final Looper mInitializationLooper;
    public protected long mJniData;
    private int mNativeBufferSizeInBytes;
    private int mNativeBufferSizeInFrames;
    public private long mNativeTrackInJavaObj;
    private int mOffset;
    private int mPlayState;
    private final Object mPlayStateLock;
    private AudioDeviceInfo mPreferredDevice;
    @GuardedBy("mRoutingChangeListeners")
    private ArrayMap<AudioRouting.OnRoutingChangedListener, NativeRoutingEventHandlerDelegate> mRoutingChangeListeners;
    private int mSampleRate;
    private int mSessionId;
    private int mState;
    private StreamEventCallback mStreamEventCb;
    private final Object mStreamEventCbLock;
    private Executor mStreamEventExec;
    public protected int mStreamType;

    /* loaded from: classes.dex */
    public interface OnPlaybackPositionUpdateListener {
        void onMarkerReached(AudioTrack audioTrack);

        void onPeriodicNotification(AudioTrack audioTrack);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface PerformanceMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface TransferMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface WriteMode {
    }

    private native int native_applyVolumeShaper(VolumeShaper.Configuration configuration, VolumeShaper.Operation operation);

    private final native int native_attachAuxEffect(int i);

    private final native void native_disableDeviceCallback();

    private final native void native_enableDeviceCallback();

    private final native void native_finalize();

    private final native void native_flush();

    private native PersistableBundle native_getMetrics();

    private final native int native_getRoutedDeviceId();

    private native VolumeShaper.State native_getVolumeShaperState(int i);

    private static native int native_get_FCC_8();

    private final native int native_get_buffer_capacity_frames();

    private final native int native_get_buffer_size_frames();

    private final native int native_get_flags();

    private final native int native_get_latency();

    private final native int native_get_marker_pos();

    private static final native int native_get_min_buff_size(int i, int i2, int i3);

    private static final native int native_get_output_sample_rate(int i);

    private final native PlaybackParams native_get_playback_params();

    private final native int native_get_playback_rate();

    private final native int native_get_pos_update_period();

    private final native int native_get_position();

    private final native int native_get_timestamp(long[] jArr);

    private final native int native_get_underrun_count();

    private final native void native_pause();

    private final native int native_reload_static();

    private final native int native_setAuxEffectSendLevel(float f);

    private final native boolean native_setOutputDevice(int i);

    private final native int native_setPresentation(int i, int i2);

    private final native void native_setVolume(float f, float f2);

    private final native int native_set_buffer_size_frames(int i);

    private final native int native_set_loop(int i, int i2, int i3);

    private final native int native_set_marker_pos(int i);

    private final native void native_set_playback_params(PlaybackParams playbackParams);

    private final native int native_set_playback_rate(int i);

    private final native int native_set_pos_update_period(int i);

    private final native int native_set_position(int i);

    private final native int native_setup(Object obj, Object obj2, int[] iArr, int i, int i2, int i3, int i4, int i5, int[] iArr2, long j, boolean z);

    private final native void native_start();

    private final native void native_stop();

    private final native int native_write_byte(byte[] bArr, int i, int i2, int i3, boolean z);

    private final native int native_write_float(float[] fArr, int i, int i2, int i3, boolean z);

    private final native int native_write_native_bytes(Object obj, int i, int i2, int i3, boolean z);

    private final native int native_write_short(short[] sArr, int i, int i2, int i3, boolean z);

    private protected final native void native_release();

    public AudioTrack(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode) throws IllegalArgumentException {
        this(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode, 0);
    }

    public AudioTrack(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode, int sessionId) throws IllegalArgumentException {
        this(new AudioAttributes.Builder().setLegacyStreamType(streamType).build(), new AudioFormat.Builder().setChannelMask(channelConfig).setEncoding(audioFormat).setSampleRate(sampleRateInHz).build(), bufferSizeInBytes, mode, sessionId);
        deprecateStreamTypeForPlayback(streamType, "AudioTrack", "AudioTrack()");
        this.mAudioType = streamType;
    }

    public AudioTrack(AudioAttributes attributes, AudioFormat format, int bufferSizeInBytes, int mode, int sessionId) throws IllegalArgumentException {
        this(attributes, format, bufferSizeInBytes, mode, sessionId, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0167  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized AudioTrack(android.media.AudioAttributes r29, android.media.AudioFormat r30, int r31, int r32, int r33, boolean r34) throws java.lang.IllegalArgumentException {
        /*
            Method dump skipped, instructions count: 395
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioTrack.<init>(android.media.AudioAttributes, android.media.AudioFormat, int, int, int, boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized AudioTrack(long nativeTrackInJavaObj) {
        super(new AudioAttributes.Builder().build(), 1);
        this.mState = 0;
        this.mPlayState = 1;
        this.mPlayStateLock = new Object();
        this.mNativeBufferSizeInBytes = 0;
        this.mNativeBufferSizeInFrames = 0;
        this.mChannelCount = 1;
        this.mChannelMask = 4;
        this.mStreamType = 3;
        this.mDataLoadMode = 1;
        this.mChannelConfiguration = 4;
        this.mChannelIndexMask = 0;
        this.mSessionId = 0;
        this.mAvSyncHeader = null;
        this.mAvSyncBytesRemaining = 0;
        this.mOffset = 0;
        this.mAudioType = -1;
        this.mPreferredDevice = null;
        this.mRoutingChangeListeners = new ArrayMap<>();
        this.mStreamEventCbLock = new Object();
        this.mNativeTrackInJavaObj = 0L;
        this.mJniData = 0L;
        Looper myLooper = Looper.myLooper();
        Looper looper = myLooper;
        this.mInitializationLooper = myLooper == null ? Looper.getMainLooper() : looper;
        if (nativeTrackInJavaObj != 0) {
            baseRegisterPlayer();
            deferred_connect(nativeTrackInJavaObj);
            return;
        }
        this.mState = 0;
    }

    private static IAudioService getService() {
        if (sService != null) {
            return sService;
        }
        IBinder b = ServiceManager.getService("audio");
        sService = IAudioService.Stub.asInterface(b);
        return sService;
    }

    public private protected void deferred_connect(long nativeTrackInJavaObj) {
        if (this.mState != 1) {
            int[] session = {0};
            int[] rates = {0};
            int initResult = native_setup(new WeakReference(this), null, rates, 0, 0, 0, 0, 0, session, nativeTrackInJavaObj, false);
            if (initResult != 0) {
                loge("Error code " + initResult + " when initializing AudioTrack.");
                return;
            }
            this.mSessionId = session[0];
            this.mState = 1;
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private AudioAttributes mAttributes;
        private int mBufferSizeInBytes;
        private AudioFormat mFormat;
        private int mSessionId = 0;
        private int mMode = 1;
        private int mPerformanceMode = 0;
        private boolean mOffload = false;

        public Builder setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
            if (attributes == null) {
                throw new IllegalArgumentException("Illegal null AudioAttributes argument");
            }
            this.mAttributes = attributes;
            return this;
        }

        public Builder setAudioFormat(AudioFormat format) throws IllegalArgumentException {
            if (format == null) {
                throw new IllegalArgumentException("Illegal null AudioFormat argument");
            }
            this.mFormat = format;
            return this;
        }

        public Builder setBufferSizeInBytes(int bufferSizeInBytes) throws IllegalArgumentException {
            if (bufferSizeInBytes <= 0) {
                throw new IllegalArgumentException("Invalid buffer size " + bufferSizeInBytes);
            }
            this.mBufferSizeInBytes = bufferSizeInBytes;
            return this;
        }

        public Builder setTransferMode(int mode) throws IllegalArgumentException {
            switch (mode) {
                case 0:
                case 1:
                    this.mMode = mode;
                    return this;
                default:
                    throw new IllegalArgumentException("Invalid transfer mode " + mode);
            }
        }

        public Builder setSessionId(int sessionId) throws IllegalArgumentException {
            if (sessionId != 0 && sessionId < 1) {
                throw new IllegalArgumentException("Invalid audio session ID " + sessionId);
            }
            this.mSessionId = sessionId;
            return this;
        }

        public Builder setPerformanceMode(int performanceMode) {
            switch (performanceMode) {
                case 0:
                case 1:
                case 2:
                    this.mPerformanceMode = performanceMode;
                    return this;
                default:
                    throw new IllegalArgumentException("Invalid performance mode " + performanceMode);
            }
        }

        public synchronized Builder setOffloadedPlayback(boolean offload) {
            this.mOffload = offload;
            return this;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:10:0x0042, code lost:
            if (android.media.AudioTrack.shouldEnablePowerSaving(r9.mAttributes, r9.mFormat, r9.mBufferSizeInBytes, r9.mMode) == false) goto L10;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public android.media.AudioTrack build() throws java.lang.UnsupportedOperationException {
            /*
                r9 = this;
                android.media.AudioAttributes r0 = r9.mAttributes
                r1 = 1
                if (r0 != 0) goto L14
                android.media.AudioAttributes$Builder r0 = new android.media.AudioAttributes$Builder
                r0.<init>()
                android.media.AudioAttributes$Builder r0 = r0.setUsage(r1)
                android.media.AudioAttributes r0 = r0.build()
                r9.mAttributes = r0
            L14:
                int r0 = r9.mPerformanceMode
                switch(r0) {
                    case 0: goto L36;
                    case 1: goto L1a;
                    case 2: goto L45;
                    default: goto L19;
                }
            L19:
                goto L60
            L1a:
                android.media.AudioAttributes$Builder r0 = new android.media.AudioAttributes$Builder
                android.media.AudioAttributes r2 = r9.mAttributes
                r0.<init>(r2)
                android.media.AudioAttributes r2 = r9.mAttributes
                int r2 = r2.getAllFlags()
                r2 = r2 | 256(0x100, float:3.59E-43)
                r2 = r2 & (-513(0xfffffffffffffdff, float:NaN))
                android.media.AudioAttributes$Builder r0 = r0.replaceFlags(r2)
                android.media.AudioAttributes r0 = r0.build()
                r9.mAttributes = r0
                goto L60
            L36:
                android.media.AudioAttributes r0 = r9.mAttributes
                android.media.AudioFormat r2 = r9.mFormat
                int r3 = r9.mBufferSizeInBytes
                int r4 = r9.mMode
                boolean r0 = android.media.AudioTrack.access$000(r0, r2, r3, r4)
                if (r0 != 0) goto L45
                goto L60
            L45:
                android.media.AudioAttributes$Builder r0 = new android.media.AudioAttributes$Builder
                android.media.AudioAttributes r2 = r9.mAttributes
                r0.<init>(r2)
                android.media.AudioAttributes r2 = r9.mAttributes
                int r2 = r2.getAllFlags()
                r2 = r2 | 512(0x200, float:7.17E-43)
                r2 = r2 & (-257(0xfffffffffffffeff, float:NaN))
                android.media.AudioAttributes$Builder r0 = r0.replaceFlags(r2)
                android.media.AudioAttributes r0 = r0.build()
                r9.mAttributes = r0
            L60:
                android.media.AudioFormat r0 = r9.mFormat
                if (r0 != 0) goto L79
                android.media.AudioFormat$Builder r0 = new android.media.AudioFormat$Builder
                r0.<init>()
                r2 = 12
                android.media.AudioFormat$Builder r0 = r0.setChannelMask(r2)
                android.media.AudioFormat$Builder r0 = r0.setEncoding(r1)
                android.media.AudioFormat r0 = r0.build()
                r9.mFormat = r0
            L79:
                boolean r0 = r9.mOffload
                if (r0 == 0) goto L9e
                android.media.AudioAttributes r0 = r9.mAttributes
                int r0 = r0.getUsage()
                if (r0 != r1) goto L96
                android.media.AudioFormat r0 = r9.mFormat
                boolean r0 = android.media.AudioSystem.isOffloadSupported(r0)
                if (r0 == 0) goto L8e
                goto L9e
            L8e:
                java.lang.UnsupportedOperationException r0 = new java.lang.UnsupportedOperationException
                java.lang.String r1 = "Cannot create AudioTrack, offload format not supported"
                r0.<init>(r1)
                throw r0
            L96:
                java.lang.UnsupportedOperationException r0 = new java.lang.UnsupportedOperationException
                java.lang.String r1 = "Cannot create AudioTrack, offload requires USAGE_MEDIA"
                r0.<init>(r1)
                throw r0
            L9e:
                int r0 = r9.mMode     // Catch: java.lang.IllegalArgumentException -> Ldd
                if (r0 != r1) goto Lbb
                int r0 = r9.mBufferSizeInBytes     // Catch: java.lang.IllegalArgumentException -> Ldd
                if (r0 != 0) goto Lbb
                android.media.AudioFormat r0 = r9.mFormat     // Catch: java.lang.IllegalArgumentException -> Ldd
                int r0 = r0.getChannelCount()     // Catch: java.lang.IllegalArgumentException -> Ldd
                android.media.AudioFormat r1 = r9.mFormat     // Catch: java.lang.IllegalArgumentException -> Ldd
                android.media.AudioFormat r1 = r9.mFormat     // Catch: java.lang.IllegalArgumentException -> Ldd
                int r1 = r1.getEncoding()     // Catch: java.lang.IllegalArgumentException -> Ldd
                int r1 = android.media.AudioFormat.getBytesPerSample(r1)     // Catch: java.lang.IllegalArgumentException -> Ldd
                int r0 = r0 * r1
                r9.mBufferSizeInBytes = r0     // Catch: java.lang.IllegalArgumentException -> Ldd
            Lbb:
                android.media.AudioTrack r0 = new android.media.AudioTrack     // Catch: java.lang.IllegalArgumentException -> Ldd
                android.media.AudioAttributes r2 = r9.mAttributes     // Catch: java.lang.IllegalArgumentException -> Ldd
                android.media.AudioFormat r3 = r9.mFormat     // Catch: java.lang.IllegalArgumentException -> Ldd
                int r4 = r9.mBufferSizeInBytes     // Catch: java.lang.IllegalArgumentException -> Ldd
                int r5 = r9.mMode     // Catch: java.lang.IllegalArgumentException -> Ldd
                int r6 = r9.mSessionId     // Catch: java.lang.IllegalArgumentException -> Ldd
                boolean r7 = r9.mOffload     // Catch: java.lang.IllegalArgumentException -> Ldd
                r8 = 0
                r1 = r0
                r1.<init>(r2, r3, r4, r5, r6, r7)     // Catch: java.lang.IllegalArgumentException -> Ldd
                int r1 = r0.getState()     // Catch: java.lang.IllegalArgumentException -> Ldd
                if (r1 == 0) goto Ld5
                return r0
            Ld5:
                java.lang.UnsupportedOperationException r1 = new java.lang.UnsupportedOperationException     // Catch: java.lang.IllegalArgumentException -> Ldd
                java.lang.String r2 = "Cannot create AudioTrack"
                r1.<init>(r2)     // Catch: java.lang.IllegalArgumentException -> Ldd
                throw r1     // Catch: java.lang.IllegalArgumentException -> Ldd
            Ldd:
                r0 = move-exception
                java.lang.UnsupportedOperationException r1 = new java.lang.UnsupportedOperationException
                java.lang.String r2 = r0.getMessage()
                r1.<init>(r2)
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.AudioTrack.Builder.build():android.media.AudioTrack");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean shouldEnablePowerSaving(AudioAttributes attributes, AudioFormat format, int bufferSizeInBytes, int mode) {
        if ((attributes != null && (attributes.getAllFlags() != 0 || attributes.getUsage() != 1 || (attributes.getContentType() != 0 && attributes.getContentType() != 2 && attributes.getContentType() != 3))) || format == null || format.getSampleRate() == 0 || !AudioFormat.isEncodingLinearPcm(format.getEncoding()) || !AudioFormat.isValidEncoding(format.getEncoding()) || format.getChannelCount() < 1 || mode != 1) {
            return false;
        }
        if (bufferSizeInBytes != 0) {
            long bufferTargetSize = (((100 * format.getChannelCount()) * AudioFormat.getBytesPerSample(format.getEncoding())) * format.getSampleRate()) / 1000;
            if (bufferSizeInBytes < bufferTargetSize) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00b4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized void audioParamCheck(int r5, int r6, int r7, int r8, int r9) {
        /*
            Method dump skipped, instructions count: 224
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioTrack.audioParamCheck(int, int, int, int, int):void");
    }

    private static synchronized boolean isMultichannelConfigSupported(int channelConfig) {
        if ((channelConfig & SUPPORTED_OUT_CHANNELS) != channelConfig) {
            loge("Channel configuration features unsupported channels");
            return false;
        }
        int channelCount = AudioFormat.channelCountFromOutChannelMask(channelConfig);
        if (channelCount > CHANNEL_COUNT_MAX) {
            loge("Channel configuration contains too many channels " + channelCount + ">" + CHANNEL_COUNT_MAX);
            return false;
        } else if ((channelConfig & 12) != 12) {
            loge("Front channels must be present in multichannel configurations");
            return false;
        } else if ((channelConfig & 192) != 0 && (channelConfig & 192) != 192) {
            loge("Rear channels can't be used independently");
            return false;
        } else if ((channelConfig & GLES30.GL_COLOR) != 0 && (channelConfig & GLES30.GL_COLOR) != 6144) {
            loge("Side channels can't be used independently");
            return false;
        } else {
            return true;
        }
    }

    private synchronized void audioBuffSizeCheck(int audioBufferSize) {
        int frameSizeInBytes;
        if (AudioFormat.isEncodingLinearFrames(this.mAudioFormat)) {
            frameSizeInBytes = this.mChannelCount * AudioFormat.getBytesPerSample(this.mAudioFormat);
        } else {
            frameSizeInBytes = 1;
        }
        if (audioBufferSize % frameSizeInBytes != 0 || audioBufferSize < 1) {
            throw new IllegalArgumentException("Invalid audio buffer size.");
        }
        this.mNativeBufferSizeInBytes = audioBufferSize;
        this.mNativeBufferSizeInFrames = audioBufferSize / frameSizeInBytes;
    }

    public void release() {
        try {
            stop();
        } catch (IllegalStateException e) {
        }
        baseRelease();
        native_release();
        this.mState = 0;
    }

    protected void finalize() {
        baseRelease();
        native_finalize();
    }

    public static float getMinVolume() {
        return 0.0f;
    }

    public static float getMaxVolume() {
        return 1.0f;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getPlaybackRate() {
        return native_get_playback_rate();
    }

    public PlaybackParams getPlaybackParams() {
        return native_get_playback_params();
    }

    public int getAudioFormat() {
        return this.mAudioFormat;
    }

    public int getStreamType() {
        return this.mStreamType;
    }

    public int getChannelConfiguration() {
        return this.mChannelConfiguration;
    }

    public AudioFormat getFormat() {
        AudioFormat.Builder builder = new AudioFormat.Builder().setSampleRate(this.mSampleRate).setEncoding(this.mAudioFormat);
        if (this.mChannelConfiguration != 0) {
            builder.setChannelMask(this.mChannelConfiguration);
        }
        if (this.mChannelIndexMask != 0) {
            builder.setChannelIndexMask(this.mChannelIndexMask);
        }
        return builder.build();
    }

    public int getChannelCount() {
        return this.mChannelCount;
    }

    public int getState() {
        return this.mState;
    }

    public int getPlayState() {
        int i;
        synchronized (this.mPlayStateLock) {
            i = this.mPlayState;
        }
        return i;
    }

    public int getBufferSizeInFrames() {
        return native_get_buffer_size_frames();
    }

    public int setBufferSizeInFrames(int bufferSizeInFrames) {
        if (this.mDataLoadMode == 0 || this.mState == 0) {
            return -3;
        }
        if (bufferSizeInFrames < 0) {
            return -2;
        }
        return native_set_buffer_size_frames(bufferSizeInFrames);
    }

    public int getBufferCapacityInFrames() {
        return native_get_buffer_capacity_frames();
    }

    @Deprecated
    protected int getNativeFrameCount() {
        return native_get_buffer_capacity_frames();
    }

    public int getNotificationMarkerPosition() {
        return native_get_marker_pos();
    }

    public int getPositionNotificationPeriod() {
        return native_get_pos_update_period();
    }

    public int getPlaybackHeadPosition() {
        return native_get_position();
    }

    private protected int getLatency() {
        return native_get_latency();
    }

    public int getUnderrunCount() {
        return native_get_underrun_count();
    }

    public int getPerformanceMode() {
        int flags = native_get_flags();
        if ((flags & 4) != 0) {
            return 1;
        }
        if ((flags & 8) != 0) {
            return 2;
        }
        return 0;
    }

    public static int getNativeOutputSampleRate(int streamType) {
        return native_get_output_sample_rate(streamType);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0024  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x002a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int getMinBufferSize(int r4, int r5, int r6) {
        /*
            r0 = 0
            r1 = 12
            r2 = -2
            if (r5 == r1) goto L1c
            switch(r5) {
                case 2: goto L1a;
                case 3: goto L1c;
                case 4: goto L1a;
                default: goto L9;
            }
        L9:
            boolean r1 = isMultichannelConfigSupported(r5)
            if (r1 != 0) goto L15
            java.lang.String r1 = "getMinBufferSize(): Invalid channel configuration."
            loge(r1)
            return r2
        L15:
            int r0 = android.media.AudioFormat.channelCountFromOutChannelMask(r5)
            goto L1e
        L1a:
            r0 = 1
            goto L1e
        L1c:
            r0 = 2
        L1e:
            boolean r1 = android.media.AudioFormat.isPublicEncoding(r6)
            if (r1 != 0) goto L2a
            java.lang.String r1 = "getMinBufferSize(): Invalid audio format."
            loge(r1)
            return r2
        L2a:
            r1 = 4000(0xfa0, float:5.605E-42)
            if (r4 < r1) goto L42
            r1 = 192000(0x2ee00, float:2.6905E-40)
            if (r4 <= r1) goto L34
            goto L42
        L34:
            int r1 = native_get_min_buff_size(r4, r0, r6)
            if (r1 > 0) goto L41
            java.lang.String r2 = "getMinBufferSize(): error querying hardware"
            loge(r2)
            r2 = -1
            return r2
        L41:
            return r1
        L42:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "getMinBufferSize(): "
            r1.append(r3)
            r1.append(r4)
            java.lang.String r3 = " Hz is not a supported sample rate."
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            loge(r1)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioTrack.getMinBufferSize(int, int, int):int");
    }

    public int getAudioSessionId() {
        return this.mSessionId;
    }

    public boolean getTimestamp(AudioTimestamp timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException();
        }
        long[] longArray = new long[2];
        int ret = native_get_timestamp(longArray);
        if (ret != 0) {
            return false;
        }
        timestamp.framePosition = longArray[0];
        timestamp.nanoTime = longArray[1];
        return true;
    }

    public synchronized int getTimestampWithStatus(AudioTimestamp timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException();
        }
        long[] longArray = new long[2];
        int ret = native_get_timestamp(longArray);
        timestamp.framePosition = longArray[0];
        timestamp.nanoTime = longArray[1];
        return ret;
    }

    public PersistableBundle getMetrics() {
        PersistableBundle bundle = native_getMetrics();
        return bundle;
    }

    public void setPlaybackPositionUpdateListener(OnPlaybackPositionUpdateListener listener) {
        setPlaybackPositionUpdateListener(listener, null);
    }

    public void setPlaybackPositionUpdateListener(OnPlaybackPositionUpdateListener listener, Handler handler) {
        if (listener != null) {
            this.mEventHandlerDelegate = new NativePositionEventHandlerDelegate(this, listener, handler);
        } else {
            this.mEventHandlerDelegate = null;
        }
    }

    private static synchronized float clampGainOrLevel(float gainOrLevel) {
        if (Float.isNaN(gainOrLevel)) {
            throw new IllegalArgumentException();
        }
        if (gainOrLevel < 0.0f) {
            return 0.0f;
        }
        if (gainOrLevel > 1.0f) {
            return 1.0f;
        }
        return gainOrLevel;
    }

    @Deprecated
    public int setStereoVolume(float leftGain, float rightGain) {
        if (this.mState == 0) {
            return -3;
        }
        baseSetVolume(leftGain, rightGain);
        return 0;
    }

    @Override // android.media.PlayerBase
    synchronized void playerSetVolume(boolean muting, float leftVolume, float rightVolume) {
        native_setVolume(clampGainOrLevel(muting ? 0.0f : leftVolume), clampGainOrLevel(muting ? 0.0f : rightVolume));
    }

    public int setVolume(float gain) {
        return setStereoVolume(gain, gain);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.media.PlayerBase
    public synchronized int playerApplyVolumeShaper(VolumeShaper.Configuration configuration, VolumeShaper.Operation operation) {
        return native_applyVolumeShaper(configuration, operation);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.media.PlayerBase
    public synchronized VolumeShaper.State playerGetVolumeShaperState(int id) {
        return native_getVolumeShaperState(id);
    }

    @Override // android.media.VolumeAutomation
    public VolumeShaper createVolumeShaper(VolumeShaper.Configuration configuration) {
        return new VolumeShaper(configuration, this);
    }

    public int setPlaybackRate(int sampleRateInHz) {
        if (this.mState != 1) {
            return -3;
        }
        if (sampleRateInHz <= 0) {
            return -2;
        }
        return native_set_playback_rate(sampleRateInHz);
    }

    public void setPlaybackParams(PlaybackParams params) {
        if (params == null) {
            throw new IllegalArgumentException("params is null");
        }
        native_set_playback_params(params);
    }

    public int setNotificationMarkerPosition(int markerInFrames) {
        if (this.mState == 0) {
            return -3;
        }
        return native_set_marker_pos(markerInFrames);
    }

    public int setPositionNotificationPeriod(int periodInFrames) {
        if (this.mState == 0) {
            return -3;
        }
        return native_set_pos_update_period(periodInFrames);
    }

    public int setPlaybackHeadPosition(int positionInFrames) {
        if (this.mDataLoadMode == 1 || this.mState == 0 || getPlayState() == 3) {
            return -3;
        }
        if (positionInFrames < 0 || positionInFrames > this.mNativeBufferSizeInFrames) {
            return -2;
        }
        return native_set_position(positionInFrames);
    }

    public int setLoopPoints(int startInFrames, int endInFrames, int loopCount) {
        if (this.mDataLoadMode == 1 || this.mState == 0 || getPlayState() == 3) {
            return -3;
        }
        if (loopCount != 0 && (startInFrames < 0 || startInFrames >= this.mNativeBufferSizeInFrames || startInFrames >= endInFrames || endInFrames > this.mNativeBufferSizeInFrames)) {
            return -2;
        }
        return native_set_loop(startInFrames, endInFrames, loopCount);
    }

    public int setPresentation(AudioPresentation presentation) {
        if (presentation == null) {
            throw new IllegalArgumentException("audio presentation is null");
        }
        return native_setPresentation(presentation.getPresentationId(), presentation.getProgramId());
    }

    @Deprecated
    protected void setState(int state) {
        this.mState = state;
    }

    private boolean needVitualizer() {
        return this.mAttributes.getUsage() == 1;
    }

    private boolean isTypeSpeech() {
        if (this.mAttributes.getContentType() == 1 && this.mAudioType == 10) {
            Log.d(TAG, "Speech TTS");
            return true;
        }
        return false;
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [android.media.AudioTrack$1] */
    public void play() throws IllegalStateException {
        if (this.mState != 1) {
            throw new IllegalStateException("play() called on uninitialized AudioTrack.");
        }
        IAudioService service = getService();
        if (service != null) {
            try {
                if (needVitualizer()) {
                    service.startAudioCapture(this.mSessionId, this.mAttributes.getUsage());
                }
                if (isTypeSpeech()) {
                    service.startSpeechEffect(this.mSessionId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.w(TAG, "startAudioCapture fft data failed");
            }
        }
        final int delay = getStartDelayMs();
        if (delay == 0) {
            startImpl();
        } else {
            new Thread() { // from class: android.media.AudioTrack.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    AudioTrack.this.baseSetStartDelayMs(0);
                    try {
                        AudioTrack.this.startImpl();
                    } catch (IllegalStateException e3) {
                    }
                }
            }.start();
        }
    }

    public void play(int location, int fadeTime, int id) throws IllegalStateException {
        IAudioService service = getService();
        if (service != null) {
            try {
                service.selectAlarmChannels(location, fadeTime, id);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        play();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startImpl() {
        synchronized (this.mPlayStateLock) {
            baseStartXui();
            native_start();
            this.mPlayState = 3;
        }
    }

    public void stop() throws IllegalStateException {
        if (this.mState != 1) {
            throw new IllegalStateException("stop() called on uninitialized AudioTrack.");
        }
        IAudioService service = getService();
        if (service != null) {
            try {
                if (needVitualizer()) {
                    service.stopAudioCapture(this.mSessionId, this.mAttributes.getUsage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.w(TAG, "stopAudioCapture fft data failed");
            }
        }
        synchronized (this.mPlayStateLock) {
            native_stop();
            baseStop();
            this.mPlayState = 1;
            this.mAvSyncHeader = null;
            this.mAvSyncBytesRemaining = 0;
        }
        if (service != null) {
            try {
                if (isTypeSpeech()) {
                    service.stopSpeechEffect(this.mSessionId);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.w(TAG, "stopAudioCapture fft data failed");
            }
        }
    }

    public void pause() throws IllegalStateException {
        if (this.mState != 1) {
            throw new IllegalStateException("pause() called on uninitialized AudioTrack.");
        }
        IAudioService service = getService();
        if (service != null) {
            try {
                if (needVitualizer()) {
                    service.stopAudioCapture(this.mSessionId, this.mAttributes.getUsage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.w(TAG, "pauseAudioCapture fft data failed");
            }
        }
        synchronized (this.mPlayStateLock) {
            native_pause();
            basePause();
            this.mPlayState = 2;
        }
        if (service != null) {
            try {
                if (isTypeSpeech()) {
                    service.stopSpeechEffect(this.mSessionId);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.w(TAG, "pauseAudioCapture fft data failed");
            }
        }
    }

    public void flush() {
        if (this.mState == 1) {
            native_flush();
            this.mAvSyncHeader = null;
            this.mAvSyncBytesRemaining = 0;
        }
    }

    public int write(byte[] audioData, int offsetInBytes, int sizeInBytes) {
        return write(audioData, offsetInBytes, sizeInBytes, 0);
    }

    public int write(byte[] audioData, int offsetInBytes, int sizeInBytes, int writeMode) {
        if (this.mState == 0 || this.mAudioFormat == 4) {
            return -3;
        }
        if (writeMode != 0 && writeMode != 1) {
            Log.e(TAG, "AudioTrack.write() called with invalid blocking mode");
            return -2;
        } else if (audioData == null || offsetInBytes < 0 || sizeInBytes < 0 || offsetInBytes + sizeInBytes < 0 || offsetInBytes + sizeInBytes > audioData.length) {
            return -2;
        } else {
            int ret = native_write_byte(audioData, offsetInBytes, sizeInBytes, this.mAudioFormat, writeMode == 0);
            if (this.mDataLoadMode == 0 && this.mState == 2 && ret > 0) {
                this.mState = 1;
            }
            return ret;
        }
    }

    public int write(short[] audioData, int offsetInShorts, int sizeInShorts) {
        return write(audioData, offsetInShorts, sizeInShorts, 0);
    }

    public int write(short[] audioData, int offsetInShorts, int sizeInShorts, int writeMode) {
        if (this.mState == 0 || this.mAudioFormat == 4) {
            return -3;
        }
        if (writeMode != 0 && writeMode != 1) {
            Log.e(TAG, "AudioTrack.write() called with invalid blocking mode");
            return -2;
        } else if (audioData == null || offsetInShorts < 0 || sizeInShorts < 0 || offsetInShorts + sizeInShorts < 0 || offsetInShorts + sizeInShorts > audioData.length) {
            return -2;
        } else {
            int ret = native_write_short(audioData, offsetInShorts, sizeInShorts, this.mAudioFormat, writeMode == 0);
            if (this.mDataLoadMode == 0 && this.mState == 2 && ret > 0) {
                this.mState = 1;
            }
            return ret;
        }
    }

    public int write(float[] audioData, int offsetInFloats, int sizeInFloats, int writeMode) {
        if (this.mState == 0) {
            Log.e(TAG, "AudioTrack.write() called in invalid state STATE_UNINITIALIZED");
            return -3;
        } else if (this.mAudioFormat != 4) {
            Log.e(TAG, "AudioTrack.write(float[] ...) requires format ENCODING_PCM_FLOAT");
            return -3;
        } else if (writeMode != 0 && writeMode != 1) {
            Log.e(TAG, "AudioTrack.write() called with invalid blocking mode");
            return -2;
        } else if (audioData == null || offsetInFloats < 0 || sizeInFloats < 0 || offsetInFloats + sizeInFloats < 0 || offsetInFloats + sizeInFloats > audioData.length) {
            Log.e(TAG, "AudioTrack.write() called with invalid array, offset, or size");
            return -2;
        } else {
            int ret = native_write_float(audioData, offsetInFloats, sizeInFloats, this.mAudioFormat, writeMode == 0);
            if (this.mDataLoadMode == 0 && this.mState == 2 && ret > 0) {
                this.mState = 1;
            }
            return ret;
        }
    }

    public int write(ByteBuffer audioData, int sizeInBytes, int writeMode) {
        int ret;
        if (this.mState == 0) {
            Log.e(TAG, "AudioTrack.write() called in invalid state STATE_UNINITIALIZED");
            return -3;
        } else if (writeMode != 0 && writeMode != 1) {
            Log.e(TAG, "AudioTrack.write() called with invalid blocking mode");
            return -2;
        } else if (audioData == null || sizeInBytes < 0 || sizeInBytes > audioData.remaining()) {
            Log.e(TAG, "AudioTrack.write() called with invalid size (" + sizeInBytes + ") value");
            return -2;
        } else {
            if (audioData.isDirect()) {
                ret = native_write_native_bytes(audioData, audioData.position(), sizeInBytes, this.mAudioFormat, writeMode == 0);
            } else {
                ret = native_write_byte(NioUtils.unsafeArray(audioData), audioData.position() + NioUtils.unsafeArrayOffset(audioData), sizeInBytes, this.mAudioFormat, writeMode == 0);
            }
            if (this.mDataLoadMode == 0 && this.mState == 2 && ret > 0) {
                this.mState = 1;
            }
            if (ret > 0) {
                audioData.position(audioData.position() + ret);
            }
            return ret;
        }
    }

    public int write(ByteBuffer audioData, int sizeInBytes, int writeMode, long timestamp) {
        if (this.mState == 0) {
            Log.e(TAG, "AudioTrack.write() called in invalid state STATE_UNINITIALIZED");
            return -3;
        } else if (writeMode != 0 && writeMode != 1) {
            Log.e(TAG, "AudioTrack.write() called with invalid blocking mode");
            return -2;
        } else if (this.mDataLoadMode != 1) {
            Log.e(TAG, "AudioTrack.write() with timestamp called for non-streaming mode track");
            return -3;
        } else if ((this.mAttributes.getFlags() & 16) == 0) {
            Log.d(TAG, "AudioTrack.write() called on a regular AudioTrack. Ignoring pts...");
            return write(audioData, sizeInBytes, writeMode);
        } else if (audioData == null || sizeInBytes < 0 || sizeInBytes > audioData.remaining()) {
            Log.e(TAG, "AudioTrack.write() called with invalid size (" + sizeInBytes + ") value");
            return -2;
        } else {
            if (this.mAvSyncHeader == null) {
                this.mAvSyncHeader = ByteBuffer.allocate(this.mOffset);
                this.mAvSyncHeader.order(ByteOrder.BIG_ENDIAN);
                this.mAvSyncHeader.putInt(1431633922);
            }
            if (this.mAvSyncBytesRemaining == 0) {
                this.mAvSyncHeader.putInt(4, sizeInBytes);
                this.mAvSyncHeader.putLong(8, timestamp);
                this.mAvSyncHeader.putInt(16, this.mOffset);
                this.mAvSyncHeader.position(0);
                this.mAvSyncBytesRemaining = sizeInBytes;
            }
            if (this.mAvSyncHeader.remaining() != 0) {
                int ret = write(this.mAvSyncHeader, this.mAvSyncHeader.remaining(), writeMode);
                if (ret < 0) {
                    Log.e(TAG, "AudioTrack.write() could not write timestamp header!");
                    this.mAvSyncHeader = null;
                    this.mAvSyncBytesRemaining = 0;
                    return ret;
                } else if (this.mAvSyncHeader.remaining() > 0) {
                    Log.v(TAG, "AudioTrack.write() partial timestamp header written.");
                    return 0;
                }
            }
            int sizeToWrite = Math.min(this.mAvSyncBytesRemaining, sizeInBytes);
            int ret2 = write(audioData, sizeToWrite, writeMode);
            if (ret2 < 0) {
                Log.e(TAG, "AudioTrack.write() could not write audio data!");
                this.mAvSyncHeader = null;
                this.mAvSyncBytesRemaining = 0;
                return ret2;
            }
            this.mAvSyncBytesRemaining -= ret2;
            return ret2;
        }
    }

    public int reloadStaticData() {
        if (this.mDataLoadMode == 1 || this.mState != 1) {
            return -3;
        }
        return native_reload_static();
    }

    public int attachAuxEffect(int effectId) {
        if (this.mState == 0) {
            return -3;
        }
        return native_attachAuxEffect(effectId);
    }

    public int setAuxEffectSendLevel(float level) {
        if (this.mState == 0) {
            return -3;
        }
        return baseSetAuxEffectSendLevel(level);
    }

    @Override // android.media.PlayerBase
    synchronized int playerSetAuxEffectSendLevel(boolean muting, float level) {
        int err = native_setAuxEffectSendLevel(clampGainOrLevel(muting ? 0.0f : level));
        return err == 0 ? 0 : -1;
    }

    @Override // android.media.AudioRouting
    public boolean setPreferredDevice(AudioDeviceInfo deviceInfo) {
        if (deviceInfo != null && !deviceInfo.isSink()) {
            return false;
        }
        int preferredDeviceId = deviceInfo != null ? deviceInfo.getId() : 0;
        boolean status = native_setOutputDevice(preferredDeviceId);
        if (status) {
            synchronized (this) {
                this.mPreferredDevice = deviceInfo;
            }
        }
        return status;
    }

    @Override // android.media.AudioRouting
    public AudioDeviceInfo getPreferredDevice() {
        AudioDeviceInfo audioDeviceInfo;
        synchronized (this) {
            audioDeviceInfo = this.mPreferredDevice;
        }
        return audioDeviceInfo;
    }

    @Override // android.media.AudioRouting
    public AudioDeviceInfo getRoutedDevice() {
        int deviceId = native_getRoutedDeviceId();
        if (deviceId == 0) {
            return null;
        }
        AudioDeviceInfo[] devices = AudioManager.getDevicesStatic(2);
        for (int i = 0; i < devices.length; i++) {
            if (devices[i].getId() == deviceId) {
                return devices[i];
            }
        }
        return null;
    }

    @GuardedBy("mRoutingChangeListeners")
    private synchronized void testEnableNativeRoutingCallbacksLocked() {
        if (this.mRoutingChangeListeners.size() == 0) {
            native_enableDeviceCallback();
        }
    }

    @GuardedBy("mRoutingChangeListeners")
    private synchronized void testDisableNativeRoutingCallbacksLocked() {
        if (this.mRoutingChangeListeners.size() == 0) {
            native_disableDeviceCallback();
        }
    }

    @Override // android.media.AudioRouting
    public void addOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener listener, Handler handler) {
        synchronized (this.mRoutingChangeListeners) {
            if (listener != null) {
                try {
                    if (!this.mRoutingChangeListeners.containsKey(listener)) {
                        testEnableNativeRoutingCallbacksLocked();
                        this.mRoutingChangeListeners.put(listener, new NativeRoutingEventHandlerDelegate(this, listener, handler != null ? handler : new Handler(this.mInitializationLooper)));
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    @Override // android.media.AudioRouting
    public void removeOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener listener) {
        synchronized (this.mRoutingChangeListeners) {
            if (this.mRoutingChangeListeners.containsKey(listener)) {
                this.mRoutingChangeListeners.remove(listener);
            }
            testDisableNativeRoutingCallbacksLocked();
        }
    }

    @Deprecated
    /* loaded from: classes.dex */
    public interface OnRoutingChangedListener extends AudioRouting.OnRoutingChangedListener {
        void onRoutingChanged(AudioTrack audioTrack);

        @Override // android.media.AudioRouting.OnRoutingChangedListener
        default void onRoutingChanged(AudioRouting router) {
            if (router instanceof AudioTrack) {
                onRoutingChanged((AudioTrack) router);
            }
        }
    }

    @Deprecated
    public void addOnRoutingChangedListener(OnRoutingChangedListener listener, Handler handler) {
        addOnRoutingChangedListener((AudioRouting.OnRoutingChangedListener) listener, handler);
    }

    @Deprecated
    public void removeOnRoutingChangedListener(OnRoutingChangedListener listener) {
        removeOnRoutingChangedListener((AudioRouting.OnRoutingChangedListener) listener);
    }

    private synchronized void broadcastRoutingChange() {
        AudioManager.resetAudioPortGeneration();
        synchronized (this.mRoutingChangeListeners) {
            for (NativeRoutingEventHandlerDelegate delegate : this.mRoutingChangeListeners.values()) {
                delegate.notifyClient();
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class StreamEventCallback {
        public synchronized void onTearDown(AudioTrack track) {
        }

        public synchronized void onStreamPresentationEnd(AudioTrack track) {
        }

        public synchronized void onStreamDataRequest(AudioTrack track) {
        }
    }

    public synchronized void setStreamEventCallback(Executor executor, StreamEventCallback eventCallback) {
        if (eventCallback == null) {
            throw new IllegalArgumentException("Illegal null StreamEventCallback");
        }
        if (executor == null) {
            throw new IllegalArgumentException("Illegal null Executor for the StreamEventCallback");
        }
        synchronized (this.mStreamEventCbLock) {
            this.mStreamEventExec = executor;
            this.mStreamEventCb = eventCallback;
        }
    }

    public synchronized void removeStreamEventCallback() {
        synchronized (this.mStreamEventCbLock) {
            this.mStreamEventExec = null;
            this.mStreamEventCb = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class NativePositionEventHandlerDelegate {
        private final Handler mHandler;

        NativePositionEventHandlerDelegate(final AudioTrack track, final OnPlaybackPositionUpdateListener listener, Handler handler) {
            Looper looper;
            if (handler == null) {
                looper = AudioTrack.this.mInitializationLooper;
            } else {
                looper = handler.getLooper();
            }
            if (looper != null) {
                this.mHandler = new Handler(looper) { // from class: android.media.AudioTrack.NativePositionEventHandlerDelegate.1
                    @Override // android.os.Handler
                    public void handleMessage(Message msg) {
                        if (track == null) {
                            return;
                        }
                        switch (msg.what) {
                            case 3:
                                if (listener != null) {
                                    listener.onMarkerReached(track);
                                    return;
                                }
                                return;
                            case 4:
                                if (listener != null) {
                                    listener.onPeriodicNotification(track);
                                    return;
                                }
                                return;
                            default:
                                AudioTrack.loge("Unknown native event type: " + msg.what);
                                return;
                        }
                    }
                };
            } else {
                this.mHandler = null;
            }
        }

        synchronized Handler getHandler() {
            return this.mHandler;
        }
    }

    @Override // android.media.PlayerBase
    synchronized void playerStart() {
        play();
    }

    @Override // android.media.PlayerBase
    synchronized void playerPause() {
        pause();
    }

    @Override // android.media.PlayerBase
    synchronized void playerStop() {
        stop();
    }

    public protected static void postEventFromNative(Object audiotrack_ref, int what, int arg1, int arg2, Object obj) {
        Executor exec;
        final StreamEventCallback cb;
        Handler handler;
        final AudioTrack track = (AudioTrack) ((WeakReference) audiotrack_ref).get();
        if (track == null) {
            return;
        }
        if (what == 1000) {
            track.broadcastRoutingChange();
            return;
        }
        if (what == 0 || what == 6 || what == 7) {
            synchronized (track.mStreamEventCbLock) {
                exec = track.mStreamEventExec;
                cb = track.mStreamEventCb;
            }
            if (exec == null || cb == null) {
                return;
            }
            if (what == 0) {
                exec.execute(new Runnable() { // from class: android.media.-$$Lambda$AudioTrack$RYzHLsveZX4qW27TDViuZeb3nTQ
                    @Override // java.lang.Runnable
                    public final void run() {
                        AudioTrack.StreamEventCallback.this.onStreamDataRequest(track);
                    }
                });
                return;
            }
            switch (what) {
                case 6:
                    exec.execute(new Runnable() { // from class: android.media.-$$Lambda$AudioTrack$m_q5GeJNFuHKP4bKA5zNcUJmptg
                        @Override // java.lang.Runnable
                        public final void run() {
                            AudioTrack.StreamEventCallback.this.onTearDown(track);
                        }
                    });
                    return;
                case 7:
                    exec.execute(new Runnable() { // from class: android.media.-$$Lambda$AudioTrack$om39tqtuoUKWEwKYDHE7uiykjxw
                        @Override // java.lang.Runnable
                        public final void run() {
                            AudioTrack.StreamEventCallback.this.onStreamPresentationEnd(track);
                        }
                    });
                    return;
            }
        }
        NativePositionEventHandlerDelegate delegate = track.mEventHandlerDelegate;
        if (delegate != null && (handler = delegate.getHandler()) != null) {
            Message m = handler.obtainMessage(what, arg1, arg2, obj);
            handler.sendMessage(m);
        }
    }

    private static synchronized void logd(String msg) {
        Log.d(TAG, msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void loge(String msg) {
        Log.e(TAG, msg);
    }

    /* loaded from: classes.dex */
    public static final class MetricsConstants {
        public static final String CHANNELMASK = "android.media.audiorecord.channelmask";
        public static final String CONTENTTYPE = "android.media.audiotrack.type";
        public static final String SAMPLERATE = "android.media.audiorecord.samplerate";
        public static final String STREAMTYPE = "android.media.audiotrack.streamtype";
        public static final String USAGE = "android.media.audiotrack.usage";

        private synchronized MetricsConstants() {
        }
    }
}
