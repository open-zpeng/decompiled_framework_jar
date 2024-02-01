package android.media;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRouting;
import android.media.IAudioEventListener;
import android.media.IAudioService;
import android.media.audiopolicy.AudioMix;
import android.media.audiopolicy.AudioPolicy;
import android.media.projection.MediaProjection;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Telephony;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.xiaopeng.audio.xuiKaraoke.IKaraoke;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public class AudioRecord implements AudioRouting, MicrophoneDirection, AudioRecordingMonitor, AudioRecordingMonitorClient {
    private static final int AUDIORECORD_ERROR_SETUP_INVALIDCHANNELMASK = -17;
    private static final int AUDIORECORD_ERROR_SETUP_INVALIDFORMAT = -18;
    private static final int AUDIORECORD_ERROR_SETUP_INVALIDSOURCE = -19;
    private static final int AUDIORECORD_ERROR_SETUP_NATIVEINITFAILED = -20;
    private static final int AUDIORECORD_ERROR_SETUP_ZEROFRAMECOUNT = -16;
    public static final int ERROR = -1;
    public static final int ERROR_BAD_VALUE = -2;
    public static final int ERROR_DEAD_OBJECT = -6;
    public static final int ERROR_INVALID_OPERATION = -3;
    private static final String EXTRA_PUREMIC_VOICEKEY = "xiaopeng.keyAction.loostone.extra";
    private static final int NATIVE_EVENT_MARKER = 2;
    private static final int NATIVE_EVENT_NEW_POS = 3;
    public static final int READ_BLOCKING = 0;
    public static final int READ_NON_BLOCKING = 1;
    private static final String RECORDSTATE_ACTIOIN_PKG = "xiaopeng.record.using.PKGNAME";
    private static final String RECORDSTATE_ACTIOIN_STATUS = "xiaopeng.record.using.STATUS";
    public static final int RECORDSTATE_RECORDING = 3;
    public static final int RECORDSTATE_STOPPED = 1;
    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_UNINITIALIZED = 0;
    public static final String SUBMIX_FIXED_VOLUME = "fixedVolume";
    public static final int SUCCESS = 0;
    private static final String TAG = "android.media.AudioRecord";
    private static AudioRecord mInstance;
    private static WeakReference<AudioRecord> mInstanceRef;
    private final String PUREMIC_VOICEKEY_ACTIOIN;
    private boolean PuremicKeyActionOn;
    private final String RECORDSTATE_ACTIOIN;
    private final int RECORD_PATH_MICSDK;
    private final int RECORD_PATH_NOMICSDK;
    private final int RECORD_PATH_NORMAL;
    private String VOICE_INPUT_PROP;
    private String curAppPackageName;
    @UnsupportedAppUsage
    private AudioAttributes mAudioAttributes;
    private AudioPolicy mAudioCapturePolicy;
    private int mAudioFormat;
    private int mBufferSize;
    private int mChannelCount;
    private int mChannelIndexMask;
    private int mChannelMask;
    private NativeEventHandler mEventHandler;
    private final IBinder mICallBack;
    @UnsupportedAppUsage
    private Looper mInitializationLooper;
    private boolean mIsSubmixFullVolume;
    private AudioEventListenerToService mListenerToService;
    private int mNativeBufferSizeInBytes;
    @UnsupportedAppUsage
    private long mNativeCallbackCookie;
    @UnsupportedAppUsage
    private long mNativeDeviceCallback;
    @UnsupportedAppUsage
    private long mNativeRecorderInJavaObj;
    private OnRecordPositionUpdateListener mPositionListener;
    private final Object mPositionListenerLock;
    private AudioDeviceInfo mPreferredDevice;
    private IKaraoke mProxy;
    private int mRecPath;
    private int mRecordSource;
    AudioRecordingMonitorImpl mRecordingInfoImpl;
    private int mRecordingState;
    private final Object mRecordingStateLock;
    @GuardedBy({"mRoutingChangeListeners"})
    private ArrayMap<AudioRouting.OnRoutingChangedListener, NativeRoutingEventHandlerDelegate> mRoutingChangeListeners;
    private int mSampleRate;
    private int mSessionId;
    private int mState;
    private static final boolean supportMicKey = SystemProperties.getBoolean("persist.audio.record.mickey.support", false);
    private static boolean RecordStarted = false;

    /* loaded from: classes2.dex */
    public interface OnRecordPositionUpdateListener {
        void onMarkerReached(AudioRecord audioRecord);

        void onPeriodicNotification(AudioRecord audioRecord);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ReadMode {
    }

    private final native void native_disableDeviceCallback();

    private final native void native_enableDeviceCallback();

    private final native void native_finalize();

    private native PersistableBundle native_getMetrics();

    private native int native_getPortId();

    private final native int native_getRoutedDeviceId();

    private final native int native_get_active_microphones(ArrayList<MicrophoneInfo> arrayList);

    private final native int native_get_buffer_size_in_frames();

    private final native int native_get_marker_pos();

    private static final native int native_get_min_buff_size(int i, int i2, int i3);

    private final native int native_get_pos_update_period();

    private final native int native_get_timestamp(AudioTimestamp audioTimestamp, int i);

    private final native int native_read_in_byte_array(byte[] bArr, int i, int i2, boolean z);

    private final native int native_read_in_direct_buffer(Object obj, int i, boolean z);

    private final native int native_read_in_float_array(float[] fArr, int i, int i2, boolean z);

    private final native int native_read_in_short_array(short[] sArr, int i, int i2, boolean z);

    private final native boolean native_setInputDevice(int i);

    private final native int native_set_marker_pos(int i);

    private final native int native_set_pos_update_period(int i);

    private native int native_set_preferred_microphone_direction(int i);

    private native int native_set_preferred_microphone_field_dimension(float f);

    /* JADX INFO: Access modifiers changed from: private */
    @UnsupportedAppUsage
    public final native int native_setup(Object obj, Object obj2, int[] iArr, int i, int i2, int i3, int i4, int[] iArr2, String str, long j);

    /* JADX INFO: Access modifiers changed from: private */
    public final native int native_start(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public final native void native_stop();

    @UnsupportedAppUsage
    public final native void native_release();

    public AudioRecord(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes) throws IllegalArgumentException {
        this(new AudioAttributes.Builder().setInternalCapturePreset(audioSource).build(), new AudioFormat.Builder().setChannelMask(getChannelMaskFromLegacyConfig(channelConfig, true)).setEncoding(audioFormat).setSampleRate(sampleRateInHz).build(), bufferSizeInBytes, 0);
    }

    @SystemApi
    public AudioRecord(AudioAttributes attributes, AudioFormat format, int bufferSizeInBytes, int sessionId) throws IllegalArgumentException {
        int rate;
        int encoding;
        int i;
        this.mState = 0;
        this.mRecordingState = 1;
        this.mRecordingStateLock = new Object();
        this.mPositionListener = null;
        this.mPositionListenerLock = new Object();
        this.mEventHandler = null;
        this.mInitializationLooper = null;
        this.mNativeBufferSizeInBytes = 0;
        this.mSessionId = 0;
        this.mIsSubmixFullVolume = false;
        this.RECORDSTATE_ACTIOIN = "xiaopeng.record.using.Action";
        this.PUREMIC_VOICEKEY_ACTIOIN = "xiaopeng.keyAction.loostone_Voice";
        this.RECORD_PATH_NORMAL = 0;
        this.RECORD_PATH_MICSDK = 1;
        this.RECORD_PATH_NOMICSDK = 2;
        this.mRecPath = -1;
        this.curAppPackageName = null;
        this.VOICE_INPUT_PROP = "xpeng.voice.assist.input";
        this.PuremicKeyActionOn = false;
        this.mProxy = null;
        this.mListenerToService = null;
        this.mICallBack = new Binder();
        this.mRoutingChangeListeners = new ArrayMap<>();
        this.mPreferredDevice = null;
        this.mRecordingInfoImpl = new AudioRecordingMonitorImpl(this);
        this.mRecordingState = 1;
        Log.d(TAG, "AudioRecord currentOpPackageName " + ActivityThread.currentOpPackageName() + " supportMicKey " + supportMicKey);
        if ("com.xiaopeng.carspeechservice".equals(ActivityThread.currentOpPackageName()) && supportMicKey) {
            Log.d(TAG, "AudioRecord  com.xiaopeng.carspeechservice: REGISTER!!!");
            mInstance = this;
            BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: android.media.AudioRecord.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    int i2;
                    if (intent == null || !AudioRecord.supportMicKey) {
                        return;
                    }
                    String action = intent.getAction();
                    if ("xiaopeng.keyAction.loostone_Voice".equals(action)) {
                        String extra = (String) intent.getExtra(AudioRecord.EXTRA_PUREMIC_VOICEKEY, "stop");
                        boolean z = true;
                        int[] sampleRate = {AudioRecord.this.mSampleRate};
                        int[] session = {AudioRecord.this.mSessionId};
                        Log.d(AudioRecord.TAG, "AudioRecordKaraoke PuremicKeyActionOn " + AudioRecord.this.PuremicKeyActionOn + " extra " + extra);
                        if ((extra.equals(Telephony.BaseMmsColumns.START) && !AudioRecord.this.PuremicKeyActionOn) || (extra.equals("stop") && AudioRecord.this.PuremicKeyActionOn)) {
                            synchronized (AudioRecord.this.mRecordingStateLock) {
                                AudioRecord.this.native_stop();
                                Log.d(AudioRecord.TAG, "PUREMIC_VOICEKEY_ACTIOIN START");
                                AudioAttributes audioAttributes = AudioRecord.this.mAudioAttributes;
                                if (!AudioRecord.this.PuremicKeyActionOn) {
                                    i2 = 1;
                                } else {
                                    i2 = 6;
                                }
                                audioAttributes.setCapturePreset(i2);
                                int initResult = AudioRecord.this.native_setup(AudioRecord.mInstanceRef, AudioRecord.this.mAudioAttributes, sampleRate, AudioRecord.this.mChannelMask, AudioRecord.this.mChannelIndexMask, AudioRecord.this.mAudioFormat, AudioRecord.this.mNativeBufferSizeInBytes, session, ActivityThread.currentOpPackageName(), 0L);
                                Log.d(AudioRecord.TAG, "native_setup initResult:" + initResult + " sampleRate:" + sampleRate + " mChannelMask:" + AudioRecord.this.mChannelMask + " mChannelIndexMask:" + AudioRecord.this.mChannelIndexMask + " mAudioFormat:" + AudioRecord.this.mAudioFormat + " mNativeBufferSizeInBytes:" + AudioRecord.this.mNativeBufferSizeInBytes + " session:" + session + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + ActivityThread.currentOpPackageName() + " mAudioAttributes:" + AudioRecord.this.mAudioAttributes);
                                if (initResult != 0) {
                                    AudioRecord.loge("Error code " + initResult + " when initializing native AudioRecord object.");
                                }
                                AudioRecord.this.native_start(0, 0);
                                AudioRecord audioRecord = AudioRecord.this;
                                if (AudioRecord.this.PuremicKeyActionOn) {
                                    z = false;
                                }
                                audioRecord.PuremicKeyActionOn = z;
                            }
                        }
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("xiaopeng.keyAction.loostone_Voice");
            ActivityThread.currentApplication().getApplicationContext().registerReceiver(mReceiver, intentFilter);
        }
        Log.d(TAG, "AudioRecord() " + attributes);
        String recordconfig = SystemProperties.get("persist.audio.record.path", WifiEnterpriseConfig.ENGINE_DISABLE);
        Log.d(TAG, "AudioRecord source:" + attributes.getCapturePreset() + " recordconfig:" + recordconfig);
        if (recordconfig.equals("micsdk") && attributes.getCapturePreset() == 6) {
            attributes.setCapturePreset(1);
        }
        if (getRecordPath(attributes.getCapturePreset()) == 1) {
            AudioRecord_Mic(format, bufferSizeInBytes, sessionId);
            i = 1;
        } else if (format == null) {
            throw new IllegalArgumentException("Illegal null AudioFormat");
        } else {
            Looper myLooper = Looper.myLooper();
            this.mInitializationLooper = myLooper;
            if (myLooper == null) {
                this.mInitializationLooper = Looper.getMainLooper();
            }
            if (attributes.getCapturePreset() == 8) {
                AudioAttributes.Builder filteredAttr = new AudioAttributes.Builder();
                for (String tag : attributes.getTags()) {
                    if (tag.equalsIgnoreCase(SUBMIX_FIXED_VOLUME)) {
                        this.mIsSubmixFullVolume = true;
                        Log.v(TAG, "Will record from REMOTE_SUBMIX at full fixed volume");
                    } else {
                        filteredAttr.addTag(tag);
                    }
                }
                filteredAttr.setInternalCapturePreset(attributes.getCapturePreset());
                this.mAudioAttributes = filteredAttr.build();
            } else {
                this.mAudioAttributes = attributes;
            }
            int rate2 = format.getSampleRate();
            if (rate2 != 0) {
                rate = rate2;
            } else {
                rate = 0;
            }
            if ((format.getPropertySetMask() & 1) == 0) {
                encoding = 1;
            } else {
                int encoding2 = format.getEncoding();
                encoding = encoding2;
            }
            int encoding3 = attributes.getCapturePreset();
            audioParamCheck(encoding3, rate, encoding);
            if ((format.getPropertySetMask() & 8) != 0) {
                this.mChannelIndexMask = format.getChannelIndexMask();
                this.mChannelCount = format.getChannelCount();
            }
            if ((format.getPropertySetMask() & 4) != 0) {
                this.mChannelMask = getChannelMaskFromLegacyConfig(format.getChannelMask(), false);
                this.mChannelCount = format.getChannelCount();
            } else if (this.mChannelIndexMask == 0) {
                this.mChannelMask = getChannelMaskFromLegacyConfig(1, false);
                this.mChannelCount = AudioFormat.channelCountFromInChannelMask(this.mChannelMask);
            }
            audioBuffSizeCheck(bufferSizeInBytes);
            int[] sampleRate = {this.mSampleRate};
            int[] session = {sessionId};
            i = 1;
            int initResult = native_setup(new WeakReference(this), this.mAudioAttributes, sampleRate, this.mChannelMask, this.mChannelIndexMask, this.mAudioFormat, this.mNativeBufferSizeInBytes, session, getCurrentOpPackageName(), 0L);
            if (initResult == 0) {
                this.mSampleRate = sampleRate[0];
                this.mSessionId = session[0];
            } else {
                loge("Error code " + initResult + " when initializing native AudioRecord object.");
                return;
            }
        }
        this.mState = i;
    }

    private String getCurrentOpPackageName() {
        String opPackageName = ActivityThread.currentOpPackageName();
        if (opPackageName != null) {
            return opPackageName;
        }
        return "uid:" + Binder.getCallingUid();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioRecord(long nativeRecordInJavaObj) {
        this.mState = 0;
        this.mRecordingState = 1;
        this.mRecordingStateLock = new Object();
        this.mPositionListener = null;
        this.mPositionListenerLock = new Object();
        this.mEventHandler = null;
        this.mInitializationLooper = null;
        this.mNativeBufferSizeInBytes = 0;
        this.mSessionId = 0;
        this.mIsSubmixFullVolume = false;
        this.RECORDSTATE_ACTIOIN = "xiaopeng.record.using.Action";
        this.PUREMIC_VOICEKEY_ACTIOIN = "xiaopeng.keyAction.loostone_Voice";
        this.RECORD_PATH_NORMAL = 0;
        this.RECORD_PATH_MICSDK = 1;
        this.RECORD_PATH_NOMICSDK = 2;
        this.mRecPath = -1;
        this.curAppPackageName = null;
        this.VOICE_INPUT_PROP = "xpeng.voice.assist.input";
        this.PuremicKeyActionOn = false;
        this.mProxy = null;
        this.mListenerToService = null;
        this.mICallBack = new Binder();
        this.mRoutingChangeListeners = new ArrayMap<>();
        this.mPreferredDevice = null;
        this.mRecordingInfoImpl = new AudioRecordingMonitorImpl(this);
        Log.d(TAG, "AudioRecord init3");
        this.mNativeRecorderInJavaObj = 0L;
        this.mNativeCallbackCookie = 0L;
        this.mNativeDeviceCallback = 0L;
        if (nativeRecordInJavaObj != 0) {
            deferred_connect(nativeRecordInJavaObj);
        } else {
            this.mState = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterAudioPolicyOnRelease(AudioPolicy audioPolicy) {
        this.mAudioCapturePolicy = audioPolicy;
    }

    void deferred_connect(long nativeRecordInJavaObj) {
        if (this.mState != 1) {
            int[] session = {0};
            int[] rates = {0};
            int initResult = native_setup(new WeakReference(this), null, rates, 0, 0, 0, 0, session, ActivityThread.currentOpPackageName(), nativeRecordInJavaObj);
            if (initResult == 0) {
                this.mSessionId = session[0];
                this.mState = 1;
                return;
            }
            loge("Error code " + initResult + " when initializing native AudioRecord object.");
        }
    }

    private int getRecordPath() {
        if (this.mRecPath == -1) {
            this.mRecPath = getRecordPath_l();
        }
        return this.mRecPath;
    }

    private int getRecordPath(int audiosource) {
        String pkg = ActivityThread.currentApplication().getApplicationContext().getPackageName();
        this.mRecPath = getRecordPath_l();
        if (this.mRecPath == 0) {
            if (audiosource == 1) {
                String voicepath = SystemProperties.get(this.VOICE_INPUT_PROP, "");
                if (voicepath.equals("")) {
                    this.mRecPath = 0;
                } else {
                    this.mRecPath = 1;
                }
                SystemProperties.set(this.VOICE_INPUT_PROP, "");
            } else {
                this.mRecPath = 0;
            }
        }
        Log.e(TAG, "getRecordPath(source:" + audiosource + ") " + this.mRecPath + " [" + pkg + "]");
        return this.mRecPath;
    }

    private int getRecordPath_l() {
        String recordconfig = SystemProperties.get("persist.audio.record.path", WifiEnterpriseConfig.ENGINE_DISABLE);
        Log.e(TAG, "getRecordPath_l 0(" + recordconfig + ")");
        return 0;
    }

    private void AudioRecord_Mic(AudioFormat format, int bufferSizeInBytes, int sessionId) {
        int rate;
        Log.d(TAG, "AudioRecord_Mic format:" + format + " bufferSizeInBytes:" + bufferSizeInBytes + " sessionId:" + sessionId);
        if ((format.getPropertySetMask() & 2) != 0) {
            rate = format.getSampleRate();
        } else {
            rate = AudioSystem.getPrimaryOutputSamplingRate();
            if (rate <= 0) {
                rate = 44100;
            }
        }
        this.mSampleRate = rate;
        this.mChannelCount = AudioFormat.channelCountFromInChannelMask(format.getChannelMask());
        this.mChannelMask = getChannelMaskFromLegacyConfig(format.getChannelMask(), false);
        this.mSessionId = sessionId;
        this.mBufferSize = bufferSizeInBytes;
        this.curAppPackageName = ActivityThread.currentApplication().getPackageName();
        try {
            IBinder b = ServiceManager.getService("audio");
            this.mProxy = IAudioService.Stub.asInterface(b).getXMicService();
            Log.d(TAG, "AudioRecord_Mic " + format + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + bufferSizeInBytes + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mProxy);
            if (this.mProxy != null) {
                this.mProxy.XMS_Create(this.curAppPackageName, 0, "", null);
            }
        } catch (RemoteException e) {
            Log.w(TAG, "Server Error");
        }
    }

    private void release_Mic() {
        Log.d(TAG, "release_Mic()");
        try {
            if (this.mProxy != null) {
                this.mProxy.XMS_MicDestroy(this.curAppPackageName);
            }
        } catch (RemoteException e) {
            Log.w(TAG, "remote error");
        }
    }

    private void startRecording_Mic() {
        IKaraoke iKaraoke = this.mProxy;
        if (iKaraoke == null) {
            Log.w(TAG, "startRecording_Mic mPorxy==null");
            return;
        }
        try {
            iKaraoke.XMS_Resume(this.curAppPackageName);
            int micSize = this.mProxy.XMS_MicGetMinBuf(this.curAppPackageName, this.mSampleRate, this.mChannelCount);
            Log.d(TAG, "startRecording_Mic: " + this.mSampleRate + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mChannelCount + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + micSize + " mBufferSize:" + this.mBufferSize);
            this.mProxy.XMS_MicCreate(this.curAppPackageName, this.mSampleRate, this.mChannelCount, this.mBufferSize);
        } catch (RemoteException e) {
            Log.w(TAG, "remote error");
        }
        synchronized (this.mRecordingStateLock) {
            this.mRecordingState = 3;
        }
    }

    private void stopRecording_Mic() {
        IKaraoke iKaraoke = this.mProxy;
        if (iKaraoke == null) {
            Log.w(TAG, "stopRecording_Mic mPorxy==null");
            return;
        }
        try {
            iKaraoke.XMS_Pause(this.curAppPackageName);
        } catch (RemoteException e) {
            Log.w(TAG, "remote error");
        }
        synchronized (this.mRecordingStateLock) {
            this.mRecordingState = 1;
        }
    }

    private int read_Mic(byte[] audioData, int size) {
        try {
            ret = this.mProxy != null ? this.mProxy.XMS_MicRead(this.curAppPackageName, audioData, size) : 0;
            Log.d(TAG, "read_Mic " + size + "  " + ret);
        } catch (RemoteException e) {
            Log.w(TAG, "remote error");
        }
        return ret;
    }

    private int read_Mic(short[] audioData, int size) {
        return 0;
    }

    private void sendRecordStateBroadcast(boolean start, String pkgName) {
        Log.i(TAG, "sendRecordStateBroadcast " + start + "" + RecordStarted + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + pkgName);
        if (RecordStarted == start) {
            return;
        }
        IBinder b = ServiceManager.getService("audio");
        IAudioService mService = IAudioService.Stub.asInterface(b);
        try {
            if (this.mListenerToService == null) {
                this.mListenerToService = new AudioEventListenerToService(this);
            }
            if (start) {
                mService.registerCallback(pkgName, this.mListenerToService);
            } else {
                mService.unregisterCallback(pkgName, this.mListenerToService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent RecordStateBroadcast = new Intent("xiaopeng.record.using.Action");
        RecordStateBroadcast.putExtra(RECORDSTATE_ACTIOIN_STATUS, start ? Telephony.BaseMmsColumns.START : "stop");
        RecordStateBroadcast.putExtra(RECORDSTATE_ACTIOIN_PKG, pkgName);
        ActivityThread.currentApplication().getApplicationContext().sendBroadcast(RecordStateBroadcast);
        RecordStarted = start;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class AudioEventListenerToService extends IAudioEventListener.Stub {
        private final WeakReference<AudioRecord> mManager;

        public AudioEventListenerToService(AudioRecord manager) {
            this.mManager = new WeakReference<>(manager);
        }

        @Override // android.media.IAudioEventListener
        public void onError(int errorCode, int operation) {
        }

        @Override // android.media.IAudioEventListener
        public void AudioEventChangeCallBack(int event, int value) {
        }
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private static final String ERROR_MESSAGE_SOURCE_MISMATCH = "Cannot both set audio source and set playback capture config";
        private AudioAttributes mAttributes;
        private AudioPlaybackCaptureConfiguration mAudioPlaybackCaptureConfiguration;
        private int mBufferSizeInBytes;
        private AudioFormat mFormat;
        private int mSessionId = 0;

        public Builder setAudioSource(int source) throws IllegalArgumentException {
            Preconditions.checkState(this.mAudioPlaybackCaptureConfiguration == null, ERROR_MESSAGE_SOURCE_MISMATCH);
            if (source < 0 || source > MediaRecorder.getAudioSourceMax()) {
                throw new IllegalArgumentException("Invalid audio source " + source);
            }
            this.mAttributes = new AudioAttributes.Builder().setInternalCapturePreset(source).build();
            return this;
        }

        @SystemApi
        public Builder setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
            if (attributes == null) {
                throw new IllegalArgumentException("Illegal null AudioAttributes argument");
            }
            if (attributes.getCapturePreset() == -1) {
                throw new IllegalArgumentException("No valid capture preset in AudioAttributes argument");
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

        public Builder setAudioPlaybackCaptureConfig(AudioPlaybackCaptureConfiguration config) {
            Preconditions.checkNotNull(config, "Illegal null AudioPlaybackCaptureConfiguration argument");
            Preconditions.checkState(this.mAttributes == null, ERROR_MESSAGE_SOURCE_MISMATCH);
            this.mAudioPlaybackCaptureConfiguration = config;
            return this;
        }

        @SystemApi
        public Builder setSessionId(int sessionId) throws IllegalArgumentException {
            if (sessionId < 0) {
                throw new IllegalArgumentException("Invalid session ID " + sessionId);
            }
            this.mSessionId = sessionId;
            return this;
        }

        private AudioRecord buildAudioPlaybackCaptureRecord() {
            AudioMix audioMix = this.mAudioPlaybackCaptureConfiguration.createAudioMix(this.mFormat);
            MediaProjection projection = this.mAudioPlaybackCaptureConfiguration.getMediaProjection();
            AudioPolicy audioPolicy = new AudioPolicy.Builder(null).setMediaProjection(projection).addMix(audioMix).build();
            int error = AudioManager.registerAudioPolicyStatic(audioPolicy);
            if (error != 0) {
                throw new UnsupportedOperationException("Error: could not register audio policy");
            }
            AudioRecord record = audioPolicy.createAudioRecordSink(audioMix);
            if (record != null) {
                record.unregisterAudioPolicyOnRelease(audioPolicy);
                return record;
            }
            throw new UnsupportedOperationException("Cannot create AudioRecord");
        }

        public AudioRecord build() throws UnsupportedOperationException {
            Log.d(AudioRecord.TAG, "AudioRecord init4");
            if (this.mAudioPlaybackCaptureConfiguration != null) {
                return buildAudioPlaybackCaptureRecord();
            }
            AudioFormat audioFormat = this.mFormat;
            if (audioFormat == null) {
                this.mFormat = new AudioFormat.Builder().setEncoding(2).setChannelMask(16).build();
            } else {
                if (audioFormat.getEncoding() == 0) {
                    this.mFormat = new AudioFormat.Builder(this.mFormat).setEncoding(2).build();
                }
                if (this.mFormat.getChannelMask() == 0 && this.mFormat.getChannelIndexMask() == 0) {
                    this.mFormat = new AudioFormat.Builder(this.mFormat).setChannelMask(16).build();
                }
            }
            if (this.mAttributes == null) {
                this.mAttributes = new AudioAttributes.Builder().setInternalCapturePreset(0).build();
            }
            try {
                if (this.mBufferSizeInBytes == 0) {
                    int channelCount = this.mFormat.getChannelCount();
                    AudioFormat audioFormat2 = this.mFormat;
                    this.mBufferSizeInBytes = channelCount * AudioFormat.getBytesPerSample(this.mFormat.getEncoding());
                }
                AudioRecord record = new AudioRecord(this.mAttributes, this.mFormat, this.mBufferSizeInBytes, this.mSessionId);
                if (record.getState() == 0) {
                    throw new UnsupportedOperationException("Cannot create AudioRecord");
                }
                return record;
            } catch (IllegalArgumentException e) {
                throw new UnsupportedOperationException(e.getMessage());
            }
        }
    }

    private static int getChannelMaskFromLegacyConfig(int inChannelConfig, boolean allowLegacyConfig) {
        int mask;
        if (inChannelConfig != 1 && inChannelConfig != 2) {
            if (inChannelConfig == 3 || inChannelConfig == 12) {
                mask = 12;
            } else if (inChannelConfig != 16) {
                if (inChannelConfig == 48) {
                    mask = inChannelConfig;
                } else if (inChannelConfig == 60 || inChannelConfig == 124 || inChannelConfig == 252 || inChannelConfig == 1020 || inChannelConfig == 4092 || inChannelConfig == 16380 || inChannelConfig == 65532) {
                    mask = inChannelConfig;
                } else {
                    throw new IllegalArgumentException("Unsupported channel configuration.");
                }
            }
            if (allowLegacyConfig && (inChannelConfig == 2 || inChannelConfig == 3)) {
                throw new IllegalArgumentException("Unsupported deprecated configuration.");
            }
            return mask;
        }
        mask = 16;
        if (allowLegacyConfig) {
        }
        return mask;
    }

    private void audioParamCheck(int audioSource, int sampleRateInHz, int audioFormat) throws IllegalArgumentException {
        if (audioSource < 0 || (audioSource > MediaRecorder.getAudioSourceMax() && audioSource != 1998 && audioSource != 1997 && audioSource != 1999)) {
            throw new IllegalArgumentException("Invalid audio source " + audioSource);
        }
        this.mRecordSource = audioSource;
        if ((sampleRateInHz < 4000 || sampleRateInHz > 192000) && sampleRateInHz != 0) {
            throw new IllegalArgumentException(sampleRateInHz + "Hz is not a supported sample rate.");
        }
        this.mSampleRate = sampleRateInHz;
        if (audioFormat == 1) {
            this.mAudioFormat = 2;
        } else if (audioFormat == 2 || audioFormat == 3 || audioFormat == 4) {
            this.mAudioFormat = audioFormat;
        } else {
            throw new IllegalArgumentException("Unsupported sample encoding " + audioFormat + ". Should be ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, or ENCODING_PCM_FLOAT.");
        }
    }

    private void audioBuffSizeCheck(int audioBufferSize) throws IllegalArgumentException {
        int frameSizeInBytes = this.mChannelCount * AudioFormat.getBytesPerSample(this.mAudioFormat);
        if (audioBufferSize % frameSizeInBytes != 0 || audioBufferSize < 1) {
            throw new IllegalArgumentException("Invalid audio buffer size " + audioBufferSize + " (frame size " + frameSizeInBytes + ")");
        }
        this.mNativeBufferSizeInBytes = audioBufferSize;
    }

    public void release() {
        if (getRecordPath() == 1) {
            release_Mic();
        } else {
            try {
                stop();
            } catch (IllegalStateException e) {
            }
            AudioPolicy audioPolicy = this.mAudioCapturePolicy;
            if (audioPolicy != null) {
                AudioManager.unregisterAudioPolicyAsyncStatic(audioPolicy);
                this.mAudioCapturePolicy = null;
            }
            native_release();
        }
        this.mState = 0;
    }

    protected void finalize() {
        if (getRecordPath() == 1) {
            release_Mic();
        } else {
            release();
        }
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getAudioSource() {
        return this.mRecordSource;
    }

    public int getAudioFormat() {
        return this.mAudioFormat;
    }

    public int getChannelConfiguration() {
        return this.mChannelMask;
    }

    public AudioFormat getFormat() {
        AudioFormat.Builder builder = new AudioFormat.Builder().setSampleRate(this.mSampleRate).setEncoding(this.mAudioFormat);
        int i = this.mChannelMask;
        if (i != 0) {
            builder.setChannelMask(i);
        }
        int i2 = this.mChannelIndexMask;
        if (i2 != 0) {
            builder.setChannelIndexMask(i2);
        }
        return builder.build();
    }

    public int getChannelCount() {
        return this.mChannelCount;
    }

    public int getState() {
        return this.mState;
    }

    public int getRecordingState() {
        int i;
        synchronized (this.mRecordingStateLock) {
            i = this.mRecordingState;
        }
        return i;
    }

    public int getBufferSizeInFrames() {
        return native_get_buffer_size_in_frames();
    }

    public int getNotificationMarkerPosition() {
        return native_get_marker_pos();
    }

    public int getPositionNotificationPeriod() {
        return native_get_pos_update_period();
    }

    public int getTimestamp(AudioTimestamp outTimestamp, int timebase) {
        if (outTimestamp == null || (timebase != 1 && timebase != 0)) {
            throw new IllegalArgumentException();
        }
        return native_get_timestamp(outTimestamp, timebase);
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0082 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0083  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int getMinBufferSize(int r6, int r7, int r8) {
        /*
            r0 = 0
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "getMinBufferSize  "
            r1.append(r2)
            r1.append(r6)
            java.lang.String r2 = " "
            r1.append(r2)
            r1.append(r7)
            r1.append(r2)
            r1.append(r8)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "android.media.AudioRecord"
            android.util.Log.d(r2, r1)
            r1 = 1
            r3 = -2
            if (r7 == r1) goto L66
            r1 = 2
            if (r7 == r1) goto L66
            r1 = 3
            if (r7 == r1) goto L64
            r1 = 12
            if (r7 == r1) goto L64
            r1 = 16
            if (r7 == r1) goto L66
            r1 = 48
            if (r7 == r1) goto L64
            r1 = 124(0x7c, float:1.74E-43)
            if (r7 == r1) goto L62
            r1 = 1020(0x3fc, float:1.43E-42)
            if (r7 == r1) goto L5f
            r1 = 4092(0xffc, float:5.734E-42)
            if (r7 == r1) goto L5c
            r1 = 16380(0x3ffc, float:2.2953E-41)
            if (r7 == r1) goto L59
            r1 = 65532(0xfffc, float:9.183E-41)
            if (r7 == r1) goto L56
            java.lang.String r1 = "getMinBufferSize(): Invalid channel configuration."
            loge(r1)
            return r3
        L56:
            r0 = 14
            goto L68
        L59:
            r0 = 12
            goto L68
        L5c:
            r0 = 10
            goto L68
        L5f:
            r0 = 8
            goto L68
        L62:
            r0 = 5
            goto L68
        L64:
            r0 = 2
            goto L68
        L66:
            r0 = 1
        L68:
            int r1 = native_get_min_buff_size(r6, r0, r8)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "getMinBufferSize  native_get_min_buff_size size:"
            r4.append(r5)
            r4.append(r1)
            java.lang.String r4 = r4.toString()
            android.util.Log.d(r2, r4)
            if (r1 != 0) goto L83
            return r3
        L83:
            r2 = -1
            if (r1 != r2) goto L87
            return r2
        L87:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioRecord.getMinBufferSize(int, int, int):int");
    }

    public int getAudioSessionId() {
        return this.mSessionId;
    }

    public void startRecording() throws IllegalStateException {
        if (this.mState != 1) {
            throw new IllegalStateException("startRecording() called on an uninitialized AudioRecord.");
        }
        Log.d(TAG, "startRecording() " + ActivityThread.currentApplication().getApplicationContext().getPackageName());
        sendRecordStateBroadcast(true, ActivityThread.currentApplication().getApplicationContext().getPackageName());
        if (getRecordPath() == 1) {
            startRecording_Mic();
            return;
        }
        synchronized (this.mRecordingStateLock) {
            if (native_start(0, 0) == 0) {
                handleFullVolumeRec(true);
                this.mRecordingState = 3;
            }
        }
    }

    public void startRecording(MediaSyncEvent syncEvent) throws IllegalStateException {
        if (this.mState != 1) {
            throw new IllegalStateException("startRecording() called on an uninitialized AudioRecord.");
        }
        Log.d(TAG, "startRecording(syncEvent) " + ActivityThread.currentApplication().getApplicationContext().getPackageName());
        sendRecordStateBroadcast(true, ActivityThread.currentApplication().getApplicationContext().getPackageName());
        if (getRecordPath() == 1) {
            startRecording_Mic();
            return;
        }
        synchronized (this.mRecordingStateLock) {
            if (native_start(syncEvent.getType(), syncEvent.getAudioSessionId()) == 0) {
                handleFullVolumeRec(true);
                this.mRecordingState = 3;
            }
        }
    }

    public void stop() throws IllegalStateException {
        if (this.mState != 1) {
            throw new IllegalStateException("stop() called on an uninitialized AudioRecord.");
        }
        Log.d(TAG, "stop()");
        sendRecordStateBroadcast(false, ActivityThread.currentApplication().getApplicationContext().getPackageName());
        if (getRecordPath() == 1) {
            stopRecording_Mic();
            return;
        }
        synchronized (this.mRecordingStateLock) {
            handleFullVolumeRec(false);
            native_stop();
            this.mRecordingState = 1;
        }
    }

    private void handleFullVolumeRec(boolean starting) {
        if (!this.mIsSubmixFullVolume) {
            return;
        }
        IBinder b = ServiceManager.getService("audio");
        IAudioService ias = IAudioService.Stub.asInterface(b);
        try {
            ias.forceRemoteSubmixFullVolume(starting, this.mICallBack);
        } catch (RemoteException e) {
            Log.e(TAG, "Error talking to AudioService when handling full submix volume", e);
        }
    }

    public int read(byte[] audioData, int offsetInBytes, int sizeInBytes) {
        return read(audioData, offsetInBytes, sizeInBytes, 0);
    }

    public int read(byte[] audioData, int offsetInBytes, int sizeInBytes, int readMode) {
        if (this.mState != 1 || this.mAudioFormat == 4) {
            return -3;
        }
        if (getRecordPath() == 1) {
            return read_Mic(audioData, sizeInBytes);
        }
        if (readMode != 0 && readMode != 1) {
            Log.e(TAG, "AudioRecord.read() called with invalid blocking mode");
            return -2;
        } else if (audioData == null || offsetInBytes < 0 || sizeInBytes < 0 || offsetInBytes + sizeInBytes < 0 || offsetInBytes + sizeInBytes > audioData.length) {
            return -2;
        } else {
            return native_read_in_byte_array(audioData, offsetInBytes, sizeInBytes, readMode == 0);
        }
    }

    public int read(short[] audioData, int offsetInShorts, int sizeInShorts) {
        return read(audioData, offsetInShorts, sizeInShorts, 0);
    }

    public int read(short[] audioData, int offsetInShorts, int sizeInShorts, int readMode) {
        if (this.mState != 1 || this.mAudioFormat == 4) {
            return -3;
        }
        if (getRecordPath() == 1) {
            return read_Mic(audioData, sizeInShorts);
        }
        if (readMode != 0 && readMode != 1) {
            Log.e(TAG, "AudioRecord.read() called with invalid blocking mode");
            return -2;
        } else if (audioData == null || offsetInShorts < 0 || sizeInShorts < 0 || offsetInShorts + sizeInShorts < 0 || offsetInShorts + sizeInShorts > audioData.length) {
            return -2;
        } else {
            return native_read_in_short_array(audioData, offsetInShorts, sizeInShorts, readMode == 0);
        }
    }

    public int read(float[] audioData, int offsetInFloats, int sizeInFloats, int readMode) {
        if (this.mState == 0) {
            Log.e(TAG, "AudioRecord.read() called in invalid state STATE_UNINITIALIZED");
            return -3;
        } else if (this.mAudioFormat != 4) {
            Log.e(TAG, "AudioRecord.read(float[] ...) requires format ENCODING_PCM_FLOAT");
            return -3;
        } else {
            if (readMode != 0 && readMode != 1) {
                Log.e(TAG, "AudioRecord.read() called with invalid blocking mode");
                return -2;
            } else if (audioData == null || offsetInFloats < 0 || sizeInFloats < 0 || offsetInFloats + sizeInFloats < 0 || offsetInFloats + sizeInFloats > audioData.length) {
                return -2;
            } else {
                return native_read_in_float_array(audioData, offsetInFloats, sizeInFloats, readMode == 0);
            }
        }
    }

    public int read(ByteBuffer audioBuffer, int sizeInBytes) {
        return read(audioBuffer, sizeInBytes, 0);
    }

    public int read(ByteBuffer audioBuffer, int sizeInBytes, int readMode) {
        if (this.mState != 1) {
            return -3;
        }
        if (getRecordPath() == 1) {
            byte[] audioData = new byte[sizeInBytes];
            int ret = read_Mic(audioData, sizeInBytes);
            audioBuffer.put(audioData, 0, ret);
            return ret;
        } else if (readMode != 0 && readMode != 1) {
            Log.e(TAG, "AudioRecord.read() called with invalid blocking mode");
            return -2;
        } else if (audioBuffer == null || sizeInBytes < 0) {
            return -2;
        } else {
            return native_read_in_direct_buffer(audioBuffer, sizeInBytes, readMode == 0);
        }
    }

    public PersistableBundle getMetrics() {
        PersistableBundle bundle = native_getMetrics();
        return bundle;
    }

    public void setRecordPositionUpdateListener(OnRecordPositionUpdateListener listener) {
        setRecordPositionUpdateListener(listener, null);
    }

    public void setRecordPositionUpdateListener(OnRecordPositionUpdateListener listener, Handler handler) {
        synchronized (this.mPositionListenerLock) {
            this.mPositionListener = listener;
            if (listener != null) {
                if (handler != null) {
                    this.mEventHandler = new NativeEventHandler(this, handler.getLooper());
                } else {
                    this.mEventHandler = new NativeEventHandler(this, this.mInitializationLooper);
                }
            } else {
                this.mEventHandler = null;
            }
        }
    }

    public int setNotificationMarkerPosition(int markerInFrames) {
        if (this.mState == 0) {
            return -3;
        }
        return native_set_marker_pos(markerInFrames);
    }

    @Override // android.media.AudioRouting
    public AudioDeviceInfo getRoutedDevice() {
        int deviceId = native_getRoutedDeviceId();
        if (deviceId == 0) {
            return null;
        }
        AudioDeviceInfo[] devices = AudioManager.getDevicesStatic(1);
        for (int i = 0; i < devices.length; i++) {
            if (devices[i].getId() == deviceId) {
                return devices[i];
            }
        }
        return null;
    }

    @GuardedBy({"mRoutingChangeListeners"})
    private void testEnableNativeRoutingCallbacksLocked() {
        if (this.mRoutingChangeListeners.size() == 0) {
            native_enableDeviceCallback();
        }
    }

    @GuardedBy({"mRoutingChangeListeners"})
    private void testDisableNativeRoutingCallbacksLocked() {
        if (this.mRoutingChangeListeners.size() == 0) {
            native_disableDeviceCallback();
        }
    }

    @Override // android.media.AudioRouting
    public void addOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener listener, Handler handler) {
        synchronized (this.mRoutingChangeListeners) {
            if (listener != null) {
                if (!this.mRoutingChangeListeners.containsKey(listener)) {
                    testEnableNativeRoutingCallbacksLocked();
                    this.mRoutingChangeListeners.put(listener, new NativeRoutingEventHandlerDelegate(this, listener, handler != null ? handler : new Handler(this.mInitializationLooper)));
                }
            }
        }
    }

    @Override // android.media.AudioRouting
    public void removeOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener listener) {
        synchronized (this.mRoutingChangeListeners) {
            if (this.mRoutingChangeListeners.containsKey(listener)) {
                this.mRoutingChangeListeners.remove(listener);
                testDisableNativeRoutingCallbacksLocked();
            }
        }
    }

    @Deprecated
    /* loaded from: classes2.dex */
    public interface OnRoutingChangedListener extends AudioRouting.OnRoutingChangedListener {
        void onRoutingChanged(AudioRecord audioRecord);

        @Override // android.media.AudioRouting.OnRoutingChangedListener
        default void onRoutingChanged(AudioRouting router) {
            if (router instanceof AudioRecord) {
                onRoutingChanged((AudioRecord) router);
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

    private void broadcastRoutingChange() {
        AudioManager.resetAudioPortGeneration();
        synchronized (this.mRoutingChangeListeners) {
            for (NativeRoutingEventHandlerDelegate delegate : this.mRoutingChangeListeners.values()) {
                delegate.notifyClient();
            }
        }
    }

    public int setPositionNotificationPeriod(int periodInFrames) {
        if (this.mState == 0) {
            return -3;
        }
        return native_set_pos_update_period(periodInFrames);
    }

    @Override // android.media.AudioRouting
    public boolean setPreferredDevice(AudioDeviceInfo deviceInfo) {
        if (deviceInfo != null && !deviceInfo.isSource()) {
            return false;
        }
        int preferredDeviceId = deviceInfo != null ? deviceInfo.getId() : 0;
        boolean status = native_setInputDevice(preferredDeviceId);
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

    public List<MicrophoneInfo> getActiveMicrophones() throws IOException {
        AudioDeviceInfo device;
        ArrayList<MicrophoneInfo> activeMicrophones = new ArrayList<>();
        int status = native_get_active_microphones(activeMicrophones);
        if (status != 0) {
            if (status != -3) {
                Log.e(TAG, "getActiveMicrophones failed:" + status);
            }
            Log.i(TAG, "getActiveMicrophones failed, fallback on routed device info");
        }
        AudioManager.setPortIdForMicrophones(activeMicrophones);
        if (activeMicrophones.size() == 0 && (device = getRoutedDevice()) != null) {
            MicrophoneInfo microphone = AudioManager.microphoneInfoFromAudioDeviceInfo(device);
            ArrayList<Pair<Integer, Integer>> channelMapping = new ArrayList<>();
            for (int i = 0; i < this.mChannelCount; i++) {
                channelMapping.add(new Pair<>(Integer.valueOf(i), 1));
            }
            microphone.setChannelMapping(channelMapping);
            activeMicrophones.add(microphone);
        }
        return activeMicrophones;
    }

    @Override // android.media.AudioRecordingMonitor
    public void registerAudioRecordingCallback(Executor executor, AudioManager.AudioRecordingCallback cb) {
        this.mRecordingInfoImpl.registerAudioRecordingCallback(executor, cb);
    }

    @Override // android.media.AudioRecordingMonitor
    public void unregisterAudioRecordingCallback(AudioManager.AudioRecordingCallback cb) {
        this.mRecordingInfoImpl.unregisterAudioRecordingCallback(cb);
    }

    @Override // android.media.AudioRecordingMonitor
    public AudioRecordingConfiguration getActiveRecordingConfiguration() {
        return this.mRecordingInfoImpl.getActiveRecordingConfiguration();
    }

    @Override // android.media.AudioRecordingMonitorClient
    public int getPortId() {
        return native_getPortId();
    }

    @Override // android.media.MicrophoneDirection
    public boolean setPreferredMicrophoneDirection(int direction) {
        return native_set_preferred_microphone_direction(direction) == 0;
    }

    @Override // android.media.MicrophoneDirection
    public boolean setPreferredMicrophoneFieldDimension(float zoom) {
        Preconditions.checkArgument(zoom >= -1.0f && zoom <= 1.0f, "Argument must fall between -1 & 1 (inclusive)");
        return native_set_preferred_microphone_field_dimension(zoom) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class NativeEventHandler extends Handler {
        private final AudioRecord mAudioRecord;

        NativeEventHandler(AudioRecord recorder, Looper looper) {
            super(looper);
            this.mAudioRecord = recorder;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            OnRecordPositionUpdateListener listener;
            synchronized (AudioRecord.this.mPositionListenerLock) {
                listener = this.mAudioRecord.mPositionListener;
            }
            int i = msg.what;
            if (i == 2) {
                if (listener != null) {
                    listener.onMarkerReached(this.mAudioRecord);
                }
            } else if (i == 3) {
                if (listener != null) {
                    listener.onPeriodicNotification(this.mAudioRecord);
                }
            } else {
                AudioRecord.loge("Unknown native event type: " + msg.what);
            }
        }
    }

    @UnsupportedAppUsage
    private static void postEventFromNative(Object audiorecord_ref, int what, int arg1, int arg2, Object obj) {
        AudioRecord recorder = (AudioRecord) ((WeakReference) audiorecord_ref).get();
        if (recorder == null) {
            return;
        }
        if (what == 1000) {
            recorder.broadcastRoutingChange();
            return;
        }
        NativeEventHandler nativeEventHandler = recorder.mEventHandler;
        if (nativeEventHandler != null) {
            Message m = nativeEventHandler.obtainMessage(what, arg1, arg2, obj);
            recorder.mEventHandler.sendMessage(m);
        }
    }

    private static void logd(String msg) {
        Log.d(TAG, msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String msg) {
        Log.e(TAG, msg);
    }

    /* loaded from: classes2.dex */
    public static final class MetricsConstants {
        public static final String ATTRIBUTES = "android.media.audiorecord.attributes";
        public static final String CHANNELS = "android.media.audiorecord.channels";
        public static final String CHANNEL_MASK = "android.media.audiorecord.channelMask";
        public static final String DURATION_MS = "android.media.audiorecord.durationMs";
        public static final String ENCODING = "android.media.audiorecord.encoding";
        public static final String FRAME_COUNT = "android.media.audiorecord.frameCount";
        @Deprecated
        public static final String LATENCY = "android.media.audiorecord.latency";
        private static final String MM_PREFIX = "android.media.audiorecord.";
        public static final String PORT_ID = "android.media.audiorecord.portId";
        public static final String SAMPLERATE = "android.media.audiorecord.samplerate";
        public static final String SOURCE = "android.media.audiorecord.source";
        public static final String START_COUNT = "android.media.audiorecord.startCount";

        private MetricsConstants() {
        }
    }
}
