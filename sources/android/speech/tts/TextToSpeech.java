package android.speech.tts;

import android.accounts.AccountManager;
import android.annotation.UnsupportedAppUsage;
import android.app.slice.Slice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.provider.Telephony;
import android.speech.tts.ITextToSpeechCallback;
import android.speech.tts.ITextToSpeechService;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.content.NativeLibraryHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class TextToSpeech {
    public static final String ACTION_TTS_ENGINE_CHANGED = "android.speech.tts.TTS_ENGINE_CHANGED";
    public static final String ACTION_TTS_QUEUE_PROCESSING_COMPLETED = "android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED";
    public static final int AUDIO_FOCUS_AUTO = 1;
    public static final int AUDIO_FOCUS_DISABLE = 0;
    private static final boolean DBG = true;
    public static final int ERROR = -1;
    public static final int ERROR_DELAY_TIMEOUT = -11;
    public static final int ERROR_INVALID_REQUEST = -8;
    public static final int ERROR_NETWORK = -6;
    public static final int ERROR_NETWORK_TIMEOUT = -7;
    public static final int ERROR_NOT_INSTALLED_YET = -9;
    public static final int ERROR_OUTPUT = -5;
    public static final int ERROR_REJECT_BY_SOLO_MODE = -10;
    public static final int ERROR_SERVICE = -4;
    public static final int ERROR_SYNTHESIS = -3;
    public static final int LANG_AVAILABLE = 0;
    public static final int LANG_COUNTRY_AVAILABLE = 1;
    public static final int LANG_COUNTRY_VAR_AVAILABLE = 2;
    public static final int LANG_MISSING_DATA = -1;
    public static final int LANG_NOT_SUPPORTED = -2;
    private static final int MSG_ENGINE_RECONNECT = 8;
    private static final int MSG_ON_AUDIO_AVAILABLE = 5;
    private static final int MSG_ON_BEGIN_SYNTHESIS = 4;
    private static final int MSG_ON_ERROR = 2;
    private static final int MSG_ON_RANGE_START = 6;
    private static final int MSG_ON_START = 3;
    private static final int MSG_ON_STATE_CHANGED = 7;
    private static final int MSG_ON_STOP = 0;
    private static final int MSG_ON_SUCCESS = 1;
    public static final int PRIORITY_DEFAULT = 1;
    public static final int PRIORITY_IMPORTANT = 2;
    public static final int PRIORITY_INSTANT = 4;
    public static final int PRIORITY_MAX = 4;
    public static final int PRIORITY_MIN = 1;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_URGENT = 3;
    public static final int QUEUE_ADD = 1;
    static final int QUEUE_DESTROY = 2;
    public static final int QUEUE_FLUSH = 0;
    public static final int STOPPED = -2;
    public static final int SUCCESS = 0;
    private static final String TAG = "TextToSpeech";
    public static final int TTS_CHANNEL_BLUETOOTH = 2;
    public static final int TTS_CHANNEL_HEADREST = 1;
    public static final int TTS_CHANNEL_MAIN = 0;
    public static final int TTS_MODE_AUTO = 0;
    public static final int TTS_MODE_CLOUD = 2;
    public static final int TTS_MODE_LOCAL = 1;
    public static final int TTS_STATE_ERROR = -1;
    public static final int TTS_STATE_START = 1;
    public static final int TTS_STATE_STOP = 0;
    public static final String TTS_STYLE_AFFECTIONATE = "affectionate";
    public static final String TTS_STYLE_ANGRY = "angry";
    public static final String TTS_STYLE_ASSISTANT = "assistant";
    public static final String TTS_STYLE_CALM = "calm";
    public static final String TTS_STYLE_CHAT = "chat";
    public static final String TTS_STYLE_CHEERFUL = "cheerful";
    public static final String TTS_STYLE_CUSTOMERSERVICE = "customerservice";
    public static final String TTS_STYLE_DEFAULT = "default";
    public static final String TTS_STYLE_DISGRUNTLED = "disgruntled";
    public static final String TTS_STYLE_FEARFUL = "fearful";
    public static final String TTS_STYLE_GENTLE = "gentle";
    public static final String TTS_STYLE_LYRICAL = "lyrical";
    public static final String TTS_STYLE_NEWSCAST = "newscast";
    public static final String TTS_STYLE_SAD = "sad";
    public static final String TTS_STYLE_SERIOUS = "serious";
    private CallbackHandler mCallbackHandler;
    private HandlerThread mCallbackThread;
    @UnsupportedAppUsage
    private Connection mConnectingServiceConnection;
    private final Context mContext;
    @UnsupportedAppUsage
    private volatile String mCurrentEngine;
    private ArrayList<String> mCurrentUidList;
    private final Map<String, Uri> mEarcons;
    private final TtsEngines mEnginesHelper;
    @UnsupportedAppUsage
    private OnInitListener mInitListener;
    private volatile OnInitListenerExt mInitListenerExt;
    private volatile boolean mIsInited;
    private volatile boolean mIsShutDown;
    private final Bundle mParams;
    private final BroadcastReceiver mReceiver;
    private String mRequestedEngine;
    private Connection mServiceConnection;
    private final Object mStartLock;
    private volatile OnStateChangedListener mStateChangedListener;
    private volatile TtsStateListener mTtsStateListener;
    private final boolean mUseFallback;
    private volatile UtteranceProgressListener mUtteranceProgressListener;
    private final Map<CharSequence, Uri> mUtterances;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public interface Action<R> {
        R run(ITextToSpeechService iTextToSpeechService) throws RemoteException;
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Error {
    }

    /* loaded from: classes2.dex */
    public interface OnInitListener {
        void onInit(int i);
    }

    /* loaded from: classes2.dex */
    public interface OnInitListenerExt {
        void onInit(int i, String str);
    }

    /* loaded from: classes2.dex */
    public interface OnStateChangedListener {
        void onStateChanged(String str, int i);
    }

    @Deprecated
    /* loaded from: classes2.dex */
    public interface OnUtteranceCompletedListener {
        void onUtteranceCompleted(String str);
    }

    /* loaded from: classes2.dex */
    public interface TtsStateListener {
        void onStateChanged(String str);
    }

    /* loaded from: classes2.dex */
    public class Engine {
        public static final String ACTION_CHECK_TTS_DATA = "android.speech.tts.engine.CHECK_TTS_DATA";
        public static final String ACTION_GET_SAMPLE_TEXT = "android.speech.tts.engine.GET_SAMPLE_TEXT";
        public static final String ACTION_INSTALL_TTS_DATA = "android.speech.tts.engine.INSTALL_TTS_DATA";
        public static final String ACTION_TTS_DATA_INSTALLED = "android.speech.tts.engine.TTS_DATA_INSTALLED";
        @Deprecated
        public static final int CHECK_VOICE_DATA_BAD_DATA = -1;
        public static final int CHECK_VOICE_DATA_FAIL = 0;
        @Deprecated
        public static final int CHECK_VOICE_DATA_MISSING_DATA = -2;
        @Deprecated
        public static final int CHECK_VOICE_DATA_MISSING_VOLUME = -3;
        public static final int CHECK_VOICE_DATA_PASS = 1;
        @Deprecated
        public static final String DEFAULT_ENGINE = "com.svox.pico";
        public static final float DEFAULT_PAN = 0.0f;
        public static final int DEFAULT_PITCH = 100;
        public static final int DEFAULT_RATE = 100;
        public static final int DEFAULT_STREAM = 3;
        public static final float DEFAULT_VOLUME = 1.0f;
        public static final String EXTRA_AVAILABLE_VOICES = "availableVoices";
        @Deprecated
        public static final String EXTRA_CHECK_VOICE_DATA_FOR = "checkVoiceDataFor";
        public static final String EXTRA_SAMPLE_TEXT = "sampleText";
        @Deprecated
        public static final String EXTRA_TTS_DATA_INSTALLED = "dataInstalled";
        public static final String EXTRA_UNAVAILABLE_VOICES = "unavailableVoices";
        @Deprecated
        public static final String EXTRA_VOICE_DATA_FILES = "dataFiles";
        @Deprecated
        public static final String EXTRA_VOICE_DATA_FILES_INFO = "dataFilesInfo";
        @Deprecated
        public static final String EXTRA_VOICE_DATA_ROOT_DIRECTORY = "dataRoot";
        public static final String INTENT_ACTION_TTS_SERVICE = "android.intent.action.TTS_SERVICE";
        @Deprecated
        public static final String KEY_FEATURE_EMBEDDED_SYNTHESIS = "embeddedTts";
        public static final String KEY_FEATURE_NETWORK_RETRIES_COUNT = "networkRetriesCount";
        @Deprecated
        public static final String KEY_FEATURE_NETWORK_SYNTHESIS = "networkTts";
        public static final String KEY_FEATURE_NETWORK_TIMEOUT_MS = "networkTimeoutMs";
        public static final String KEY_FEATURE_NOT_INSTALLED = "notInstalled";
        public static final String KEY_PARAM_AUDIOFOCUS = "audioFocus";
        public static final String KEY_PARAM_AUDIO_ATTRIBUTES = "audioAttributes";
        public static final String KEY_PARAM_COUNTRY = "country";
        public static final String KEY_PARAM_DEFAULT_RATE = "defaultRate";
        public static final String KEY_PARAM_DELAY_TOLERANCE = "delayTolerance";
        public static final String KEY_PARAM_ENGINE = "engine";
        public static final String KEY_PARAM_LANGUAGE = "language";
        public static final String KEY_PARAM_PAN = "pan";
        public static final String KEY_PARAM_PITCH = "pitch";
        public static final String KEY_PARAM_PRIORITY = "priority";
        public static final String KEY_PARAM_RATE = "rate";
        public static final String KEY_PARAM_SESSION_ID = "sessionId";
        public static final String KEY_PARAM_STREAM = "streamType";
        public static final String KEY_PARAM_TTS_DEFAULT_MODE = "ttsDefaultMode";
        public static final String KEY_PARAM_TTS_DEFAULT_STYLE = "defaultStyle";
        public static final String KEY_PARAM_TTS_DEFAULT_STYLE_DEGREE = "defaultStyleDegree";
        public static final String KEY_PARAM_TTS_MODE = "ttsMode";
        public static final String KEY_PARAM_TTS_ONLINE_FORMAT = "onLineFormat";
        public static final String KEY_PARAM_TTS_STYLE = "style";
        public static final String KEY_PARAM_TTS_STYLE_DEGREE = "styleDegree";
        public static final String KEY_PARAM_UTTERANCE_ID = "utteranceId";
        public static final String KEY_PARAM_VARIANT = "variant";
        public static final String KEY_PARAM_VOICE_NAME = "voiceName";
        public static final String KEY_PARAM_VOICE_POSITION = "voicePosition";
        public static final String KEY_PARAM_VOLUME = "volume";
        public static final String SERVICE_META_DATA = "android.speech.tts";
        public static final int USE_DEFAULTS = 0;

        public Engine() {
        }
    }

    public TextToSpeech(Context context, OnInitListener listener) {
        this(context, listener, null);
    }

    public TextToSpeech(Context context, OnInitListener listener, String engine) {
        this(context, listener, engine, null, true);
    }

    public TextToSpeech(Context context, OnInitListener listener, String engine, String packageName, boolean useFallback) {
        this.mInitListenerExt = null;
        this.mStateChangedListener = null;
        this.mTtsStateListener = null;
        this.mStartLock = new Object();
        this.mParams = new Bundle();
        this.mCurrentEngine = null;
        this.mReceiver = new TTSEngineChangedReceiver();
        this.mIsShutDown = false;
        this.mIsInited = false;
        this.mContext = context;
        this.mInitListener = listener;
        this.mRequestedEngine = engine;
        this.mUseFallback = useFallback;
        this.mEarcons = new HashMap();
        this.mUtterances = new HashMap();
        this.mUtteranceProgressListener = null;
        this.mCurrentUidList = new ArrayList<>();
        this.mCallbackThread = new HandlerThread("ttsHandler");
        this.mCallbackThread.start();
        this.mCallbackHandler = new CallbackHandler(this.mCallbackThread.getLooper());
        this.mEnginesHelper = new TtsEngines(this.mContext);
        IntentFilter intentFilter = new IntentFilter(ACTION_TTS_ENGINE_CHANGED);
        this.mContext.registerReceiver(this.mReceiver, intentFilter, null, this.mCallbackHandler);
        synchronized (this.mStartLock) {
            initTts();
        }
    }

    private <R> R runActionNoReconnect(Action<R> action, R errorResult, String method, boolean onlyEstablishedConnection) {
        return (R) runAction(action, errorResult, method, false, onlyEstablishedConnection);
    }

    private <R> R runAction(Action<R> action, R errorResult, String method) {
        return (R) runAction(action, errorResult, method, true, true);
    }

    private <R> R runAction(Action<R> action, R errorResult, String method, boolean reconnect, boolean onlyEstablishedConnection) {
        synchronized (this.mStartLock) {
            if (this.mServiceConnection == null) {
                Log.w(TAG, method + " failed: not bound to TTS engine");
                return errorResult;
            }
            return (R) this.mServiceConnection.runAction(action, errorResult, method, reconnect, onlyEstablishedConnection);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int initTts() {
        Log.d(TAG, "initTts");
        synchronized (this.mStartLock) {
            if (this.mRequestedEngine != null) {
                if (this.mEnginesHelper.isEngineInstalled(this.mRequestedEngine)) {
                    if (connectToEngine(this.mRequestedEngine)) {
                        this.mCurrentEngine = this.mRequestedEngine;
                        return 0;
                    } else if (!this.mUseFallback) {
                        this.mCurrentEngine = null;
                        dispatchOnInit(-1);
                        return -1;
                    }
                } else if (!this.mUseFallback) {
                    Log.i(TAG, "Requested engine not installed: " + this.mRequestedEngine);
                    this.mCurrentEngine = null;
                    dispatchOnInit(-1);
                    return -1;
                }
            }
            String defaultEngine = getDefaultEngine();
            if (defaultEngine != null && !defaultEngine.equals(this.mRequestedEngine) && connectToEngine(defaultEngine)) {
                this.mCurrentEngine = defaultEngine;
                return 0;
            }
            String highestRanked = this.mEnginesHelper.getHighestRankedEngineName();
            if (highestRanked != null && !highestRanked.equals(this.mRequestedEngine) && !highestRanked.equals(defaultEngine) && connectToEngine(highestRanked)) {
                this.mCurrentEngine = highestRanked;
                return 0;
            }
            this.mCurrentEngine = null;
            dispatchOnInit(-1);
            return -1;
        }
    }

    private boolean connectToEngine(String engine) {
        Log.d(TAG, "connectToEngine " + engine);
        Connection connection = new Connection();
        Intent intent = new Intent(Engine.INTENT_ACTION_TTS_SERVICE);
        intent.setPackage(engine);
        boolean bound = this.mContext.bindService(intent, connection, 1);
        if (!bound) {
            Log.e(TAG, "Failed to bind to " + engine);
            return false;
        }
        Log.i(TAG, "Sucessfully bound to " + engine);
        this.mConnectingServiceConnection = connection;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchOnInit(int result) {
        synchronized (this.mStartLock) {
            if (this.mInitListener != null) {
                this.mInitListener.onInit(result);
                this.mInitListener = null;
            } else if (this.mInitListenerExt != null) {
                this.mInitListenerExt.onInit(result, this.mCurrentEngine);
            }
            if (result == 0) {
                this.mIsInited = true;
                if (isReConnectEngineNeeded()) {
                    this.mIsInited = false;
                    this.mCallbackHandler.sendEmptyMessage(8);
                } else {
                    if (this.mStateChangedListener != null) {
                        setOnStateChangedListener(this.mStateChangedListener);
                        Message msg = this.mCallbackHandler.obtainMessage(7, "{\"priority\":0,\"source\":\"\"}");
                        this.mCallbackHandler.sendMessage(msg);
                    }
                    if (this.mTtsStateListener != null) {
                        setTtsStateListener(this.mTtsStateListener);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IBinder getCallerIdentity() {
        return this.mServiceConnection.getCallerIdentity();
    }

    private void clearCallback() {
        this.mCallbackHandler.removeCallbacksAndMessages(null);
        this.mContext.unregisterReceiver(this.mReceiver);
        this.mCallbackThread.quit();
    }

    public void shutdown() {
        synchronized (this.mStartLock) {
            if (this.mIsShutDown) {
                return;
            }
            this.mIsShutDown = true;
            if (this.mConnectingServiceConnection != null) {
                this.mContext.unbindService(this.mConnectingServiceConnection);
                this.mConnectingServiceConnection = null;
            } else {
                runActionNoReconnect(new Action<Void>() { // from class: android.speech.tts.TextToSpeech.1
                    @Override // android.speech.tts.TextToSpeech.Action
                    public Void run(ITextToSpeechService service) throws RemoteException {
                        service.setCallback(TextToSpeech.this.getCallerIdentity(), null);
                        service.stop(TextToSpeech.this.getCallerIdentity(), null);
                        TextToSpeech.this.mServiceConnection.disconnect();
                        TextToSpeech.this.mServiceConnection = null;
                        TextToSpeech.this.mCurrentEngine = null;
                        return null;
                    }
                }, null, "shutdown", false);
            }
            clearCallback();
        }
    }

    public int addSpeech(String text, String packagename, int resourceId) {
        synchronized (this.mStartLock) {
            this.mUtterances.put(text, makeResourceUri(packagename, resourceId));
        }
        return 0;
    }

    public int addSpeech(CharSequence text, String packagename, int resourceId) {
        synchronized (this.mStartLock) {
            this.mUtterances.put(text, makeResourceUri(packagename, resourceId));
        }
        return 0;
    }

    public int addSpeech(String text, String filename) {
        synchronized (this.mStartLock) {
            this.mUtterances.put(text, Uri.parse(filename));
        }
        return 0;
    }

    public int addSpeech(CharSequence text, File file) {
        synchronized (this.mStartLock) {
            this.mUtterances.put(text, Uri.fromFile(file));
        }
        return 0;
    }

    public int addEarcon(String earcon, String packagename, int resourceId) {
        synchronized (this.mStartLock) {
            this.mEarcons.put(earcon, makeResourceUri(packagename, resourceId));
        }
        return 0;
    }

    @Deprecated
    public int addEarcon(String earcon, String filename) {
        synchronized (this.mStartLock) {
            this.mEarcons.put(earcon, Uri.parse(filename));
        }
        return 0;
    }

    public int addEarcon(String earcon, File file) {
        synchronized (this.mStartLock) {
            this.mEarcons.put(earcon, Uri.fromFile(file));
        }
        return 0;
    }

    private Uri makeResourceUri(String packageName, int resourceId) {
        return new Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE).encodedAuthority(packageName).appendEncodedPath(String.valueOf(resourceId)).build();
    }

    public int speak(final CharSequence text, final int queueMode, final Bundle params, final String utteranceId) {
        int ret = ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                Uri utteranceUri = (Uri) TextToSpeech.this.mUtterances.get(text);
                if (utteranceUri != null) {
                    return Integer.valueOf(service.playAudio(TextToSpeech.this.getCallerIdentity(), utteranceUri, queueMode, TextToSpeech.this.getParams(params), utteranceId));
                }
                Log.d(TextToSpeech.TAG, "speak " + utteranceId);
                TextToSpeech.this.mCurrentUidList.add(utteranceId);
                return Integer.valueOf(service.speak(TextToSpeech.this.getCallerIdentity(), text, queueMode, TextToSpeech.this.getParams(params), utteranceId));
            }
        }, -1, "speak")).intValue();
        if (ret == -1) {
            Bundle bundle = new Bundle();
            bundle.putString(Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
            bundle.putInt("errorCode", -4);
            Message msg = this.mCallbackHandler.obtainMessage(2, bundle);
            this.mCallbackHandler.sendMessage(msg);
        }
        Log.d(TAG, "speak " + utteranceId + " leave");
        return ret;
    }

    @Deprecated
    public int speak(String text, int queueMode, HashMap<String, String> params) {
        return speak(text, queueMode, convertParamsHashMaptoBundle(params), params == null ? null : params.get(Engine.KEY_PARAM_UTTERANCE_ID));
    }

    public int playEarcon(final String earcon, final int queueMode, final Bundle params, final String utteranceId) {
        return ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                Uri earconUri = (Uri) TextToSpeech.this.mEarcons.get(earcon);
                if (earconUri == null) {
                    return -1;
                }
                return Integer.valueOf(service.playAudio(TextToSpeech.this.getCallerIdentity(), earconUri, queueMode, TextToSpeech.this.getParams(params), utteranceId));
            }
        }, -1, "playEarcon")).intValue();
    }

    @Deprecated
    public int playEarcon(String earcon, int queueMode, HashMap<String, String> params) {
        return playEarcon(earcon, queueMode, convertParamsHashMaptoBundle(params), params == null ? null : params.get(Engine.KEY_PARAM_UTTERANCE_ID));
    }

    public int playSilentUtterance(final long durationInMs, final int queueMode, final String utteranceId) {
        return ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                return Integer.valueOf(service.playSilence(TextToSpeech.this.getCallerIdentity(), durationInMs, queueMode, utteranceId));
            }
        }, -1, "playSilentUtterance")).intValue();
    }

    @Deprecated
    public int playSilence(long durationInMs, int queueMode, HashMap<String, String> params) {
        return playSilentUtterance(durationInMs, queueMode, params == null ? null : params.get(Engine.KEY_PARAM_UTTERANCE_ID));
    }

    @Deprecated
    public Set<String> getFeatures(final Locale locale) {
        return (Set) runAction(new Action<Set<String>>() { // from class: android.speech.tts.TextToSpeech.5
            @Override // android.speech.tts.TextToSpeech.Action
            public Set<String> run(ITextToSpeechService service) throws RemoteException {
                try {
                    String[] features = service.getFeaturesForLanguage(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
                    if (features == null) {
                        return null;
                    }
                    Set<String> featureSet = new HashSet<>();
                    Collections.addAll(featureSet, features);
                    return featureSet;
                } catch (MissingResourceException e) {
                    Log.w(TextToSpeech.TAG, "Couldn't retrieve 3 letter ISO 639-2/T language and/or ISO 3166 country code for locale: " + locale, e);
                    return null;
                }
            }
        }, null, "getFeatures");
    }

    public boolean isSpeaking() {
        return ((Boolean) runAction(new Action() { // from class: android.speech.tts.-$$Lambda$TextToSpeech$64hiI_Hj5JipJ42i8bJqYHod8XU
            @Override // android.speech.tts.TextToSpeech.Action
            public final Object run(ITextToSpeechService iTextToSpeechService) {
                Boolean valueOf;
                valueOf = Boolean.valueOf(iTextToSpeechService.isSpeaking());
                return valueOf;
            }
        }, false, "isSpeaking")).booleanValue();
    }

    public boolean isSpeaking(final int channel) {
        return ((Boolean) runAction(new Action() { // from class: android.speech.tts.-$$Lambda$TextToSpeech$GH6B5ul8ZLMx2LRm4pRICJOOcu8
            @Override // android.speech.tts.TextToSpeech.Action
            public final Object run(ITextToSpeechService iTextToSpeechService) {
                Boolean valueOf;
                valueOf = Boolean.valueOf(iTextToSpeechService.isSpeakingByChannel(channel));
                return valueOf;
            }
        }, false, "isSpeakingByChannel")).booleanValue();
    }

    public int stop() {
        return stop(null);
    }

    public int stop(final String utteranceId) {
        return ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                return Integer.valueOf(service.stop(TextToSpeech.this.getCallerIdentity(), utteranceId));
            }
        }, -1, "stop")).intValue();
    }

    public int stopByChannel(final int[] channelList) {
        return ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                return Integer.valueOf(service.stopByChannel(TextToSpeech.this.getCallerIdentity(), channelList));
            }
        }, -1, "stopByChannel")).intValue();
    }

    public int setSpeechRate(float speechRate) {
        int intRate;
        if (speechRate > 0.0f && (intRate = (int) (100.0f * speechRate)) > 0) {
            synchronized (this.mStartLock) {
                this.mParams.putInt(Engine.KEY_PARAM_RATE, intRate);
            }
            return 0;
        }
        return -1;
    }

    public int setPitch(float pitch) {
        int intPitch;
        if (pitch > 0.0f && (intPitch = (int) (100.0f * pitch)) > 0) {
            synchronized (this.mStartLock) {
                this.mParams.putInt(Engine.KEY_PARAM_PITCH, intPitch);
            }
            return 0;
        }
        return -1;
    }

    public int setAudioAttributes(AudioAttributes audioAttributes) {
        if (audioAttributes != null) {
            synchronized (this.mStartLock) {
                this.mParams.putParcelable(Engine.KEY_PARAM_AUDIO_ATTRIBUTES, audioAttributes);
            }
            return 0;
        }
        return -1;
    }

    @UnsupportedAppUsage
    public String getCurrentEngine() {
        return this.mCurrentEngine;
    }

    @Deprecated
    public Locale getDefaultLanguage() {
        return (Locale) runAction(new Action<Locale>() { // from class: android.speech.tts.TextToSpeech.8
            @Override // android.speech.tts.TextToSpeech.Action
            public Locale run(ITextToSpeechService service) throws RemoteException {
                String[] defaultLanguage = service.getClientDefaultLanguage();
                return new Locale(defaultLanguage[0], defaultLanguage[1], defaultLanguage[2]);
            }
        }, null, "getDefaultLanguage");
    }

    public int setLanguage(final Locale loc) {
        return ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                Locale locale = loc;
                if (locale == null) {
                    return -2;
                }
                try {
                    String language = locale.getISO3Language();
                    try {
                        String country = loc.getISO3Country();
                        String variant = loc.getVariant();
                        int result = service.isLanguageAvailable(language, country, variant);
                        if (result >= 0) {
                            String voiceName = service.getDefaultVoiceNameFor(language, country, variant);
                            if (!TextUtils.isEmpty(voiceName)) {
                                if (service.loadVoice(TextToSpeech.this.getCallerIdentity(), voiceName) != -1) {
                                    Voice voice = TextToSpeech.this.getVoice(service, voiceName);
                                    if (voice == null) {
                                        Log.w(TextToSpeech.TAG, "getDefaultVoiceNameFor returned " + voiceName + " for locale " + language + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + country + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + variant + " but getVoice returns null");
                                        return -2;
                                    }
                                    String voiceLanguage = "";
                                    try {
                                        voiceLanguage = voice.getLocale().getISO3Language();
                                    } catch (MissingResourceException e) {
                                        Log.w(TextToSpeech.TAG, "Couldn't retrieve ISO 639-2/T language code for locale: " + voice.getLocale(), e);
                                    }
                                    String voiceCountry = "";
                                    try {
                                        voiceCountry = voice.getLocale().getISO3Country();
                                    } catch (MissingResourceException e2) {
                                        Log.w(TextToSpeech.TAG, "Couldn't retrieve ISO 3166 country code for locale: " + voice.getLocale(), e2);
                                    }
                                    TextToSpeech.this.mParams.putString(Engine.KEY_PARAM_VOICE_NAME, voiceName);
                                    TextToSpeech.this.mParams.putString("language", voiceLanguage);
                                    TextToSpeech.this.mParams.putString(Engine.KEY_PARAM_COUNTRY, voiceCountry);
                                    TextToSpeech.this.mParams.putString(Engine.KEY_PARAM_VARIANT, voice.getLocale().getVariant());
                                } else {
                                    Log.w(TextToSpeech.TAG, "The service claimed " + language + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + country + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + variant + " was available with voice name " + voiceName + " but loadVoice returned ERROR");
                                    return -2;
                                }
                            } else {
                                Log.w(TextToSpeech.TAG, "Couldn't find the default voice for " + language + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + country + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + variant);
                                return -2;
                            }
                        }
                        return Integer.valueOf(result);
                    } catch (MissingResourceException e3) {
                        Log.w(TextToSpeech.TAG, "Couldn't retrieve ISO 3166 country code for locale: " + loc, e3);
                        return -2;
                    }
                } catch (MissingResourceException e4) {
                    Log.w(TextToSpeech.TAG, "Couldn't retrieve ISO 639-2/T language code for locale: " + loc, e4);
                    return -2;
                }
            }
        }, -2, "setLanguage")).intValue();
    }

    @Deprecated
    public Locale getLanguage() {
        return (Locale) runAction(new Action<Locale>() { // from class: android.speech.tts.TextToSpeech.10
            @Override // android.speech.tts.TextToSpeech.Action
            public Locale run(ITextToSpeechService service) {
                String lang = TextToSpeech.this.mParams.getString("language", "");
                String country = TextToSpeech.this.mParams.getString(Engine.KEY_PARAM_COUNTRY, "");
                String variant = TextToSpeech.this.mParams.getString(Engine.KEY_PARAM_VARIANT, "");
                return new Locale(lang, country, variant);
            }
        }, null, "getLanguage");
    }

    public Set<Locale> getAvailableLanguages() {
        return (Set) runAction(new Action<Set<Locale>>() { // from class: android.speech.tts.TextToSpeech.11
            @Override // android.speech.tts.TextToSpeech.Action
            public Set<Locale> run(ITextToSpeechService service) throws RemoteException {
                List<Voice> voices = service.getVoices();
                if (voices == null) {
                    return new HashSet();
                }
                HashSet<Locale> locales = new HashSet<>();
                for (Voice voice : voices) {
                    locales.add(voice.getLocale());
                }
                return locales;
            }
        }, null, "getAvailableLanguages");
    }

    public Set<Voice> getVoices() {
        return (Set) runAction(new Action<Set<Voice>>() { // from class: android.speech.tts.TextToSpeech.12
            @Override // android.speech.tts.TextToSpeech.Action
            public Set<Voice> run(ITextToSpeechService service) throws RemoteException {
                List<Voice> voices = service.getVoices();
                return voices != null ? new HashSet(voices) : new HashSet();
            }
        }, null, "getVoices");
    }

    public int setVoice(final Voice voice) {
        return ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                int result = service.loadVoice(TextToSpeech.this.getCallerIdentity(), voice.getName());
                if (result == 0) {
                    TextToSpeech.this.mParams.putString(Engine.KEY_PARAM_VOICE_NAME, voice.getName());
                    String language = "";
                    try {
                        language = voice.getLocale().getISO3Language();
                    } catch (MissingResourceException e) {
                        Log.w(TextToSpeech.TAG, "Couldn't retrieve ISO 639-2/T language code for locale: " + voice.getLocale(), e);
                    }
                    String country = "";
                    try {
                        country = voice.getLocale().getISO3Country();
                    } catch (MissingResourceException e2) {
                        Log.w(TextToSpeech.TAG, "Couldn't retrieve ISO 3166 country code for locale: " + voice.getLocale(), e2);
                    }
                    TextToSpeech.this.mParams.putString("language", language);
                    TextToSpeech.this.mParams.putString(Engine.KEY_PARAM_COUNTRY, country);
                    TextToSpeech.this.mParams.putString(Engine.KEY_PARAM_VARIANT, voice.getLocale().getVariant());
                }
                return Integer.valueOf(result);
            }
        }, -2, "setVoice")).intValue();
    }

    public Voice getVoice() {
        return (Voice) runAction(new Action<Voice>() { // from class: android.speech.tts.TextToSpeech.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Voice run(ITextToSpeechService service) throws RemoteException {
                String voiceName = TextToSpeech.this.mParams.getString(Engine.KEY_PARAM_VOICE_NAME, "");
                if (!TextUtils.isEmpty(voiceName)) {
                    return TextToSpeech.this.getVoice(service, voiceName);
                }
                return null;
            }
        }, null, "getVoice");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Voice getVoice(ITextToSpeechService service, String voiceName) throws RemoteException {
        List<Voice> voices = service.getVoices();
        if (voices == null) {
            Log.w(TAG, "getVoices returned null");
            return null;
        }
        for (Voice voice : voices) {
            if (voice.getName().equals(voiceName)) {
                return voice;
            }
        }
        Log.w(TAG, "Could not find voice " + voiceName + " in voice list");
        return null;
    }

    public Voice getDefaultVoice() {
        return (Voice) runAction(new Action<Voice>() { // from class: android.speech.tts.TextToSpeech.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Voice run(ITextToSpeechService service) throws RemoteException {
                List<Voice> voices;
                String[] defaultLanguage = service.getClientDefaultLanguage();
                if (defaultLanguage == null || defaultLanguage.length == 0) {
                    Log.e(TextToSpeech.TAG, "service.getClientDefaultLanguage() returned empty array");
                    return null;
                }
                String language = defaultLanguage[0];
                String country = defaultLanguage.length > 1 ? defaultLanguage[1] : "";
                String variant = defaultLanguage.length > 2 ? defaultLanguage[2] : "";
                int result = service.isLanguageAvailable(language, country, variant);
                if (result < 0) {
                    return null;
                }
                String voiceName = service.getDefaultVoiceNameFor(language, country, variant);
                if (TextUtils.isEmpty(voiceName) || (voices = service.getVoices()) == null) {
                    return null;
                }
                for (Voice voice : voices) {
                    if (voice.getName().equals(voiceName)) {
                        return voice;
                    }
                }
                return null;
            }
        }, null, "getDefaultVoice");
    }

    public int isLanguageAvailable(final Locale loc) {
        return ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                try {
                    String language = loc.getISO3Language();
                    try {
                        String country = loc.getISO3Country();
                        return Integer.valueOf(service.isLanguageAvailable(language, country, loc.getVariant()));
                    } catch (MissingResourceException e) {
                        Log.w(TextToSpeech.TAG, "Couldn't retrieve ISO 3166 country code for locale: " + loc, e);
                        return -2;
                    }
                } catch (MissingResourceException e2) {
                    Log.w(TextToSpeech.TAG, "Couldn't retrieve ISO 639-2/T language code for locale: " + loc, e2);
                    return -2;
                }
            }
        }, -2, "isLanguageAvailable")).intValue();
    }

    public int synthesizeToFile(final CharSequence text, final Bundle params, final File file, final String utteranceId) {
        return ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.17
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                try {
                    if (file.exists() && !file.canWrite()) {
                        Log.e(TextToSpeech.TAG, "Can't write to " + file);
                        return -1;
                    }
                    ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, 738197504);
                    int returnValue = service.synthesizeToFileDescriptor(TextToSpeech.this.getCallerIdentity(), text, fileDescriptor, TextToSpeech.this.getParams(params), utteranceId);
                    fileDescriptor.close();
                    return Integer.valueOf(returnValue);
                } catch (FileNotFoundException e) {
                    Log.e(TextToSpeech.TAG, "Opening file " + file + " failed", e);
                    return -1;
                } catch (IOException e2) {
                    Log.e(TextToSpeech.TAG, "Closing file " + file + " failed", e2);
                    return -1;
                }
            }
        }, -1, "synthesizeToFile")).intValue();
    }

    @Deprecated
    public int synthesizeToFile(String text, HashMap<String, String> params, String filename) {
        return synthesizeToFile(text, convertParamsHashMaptoBundle(params), new File(filename), params.get(Engine.KEY_PARAM_UTTERANCE_ID));
    }

    private Bundle convertParamsHashMaptoBundle(HashMap<String, String> params) {
        if (params != null && !params.isEmpty()) {
            Bundle bundle = new Bundle();
            copyIntParam(bundle, params, Engine.KEY_PARAM_STREAM);
            copyIntParam(bundle, params, Engine.KEY_PARAM_SESSION_ID);
            copyStringParam(bundle, params, Engine.KEY_PARAM_UTTERANCE_ID);
            copyFloatParam(bundle, params, "volume");
            copyFloatParam(bundle, params, Engine.KEY_PARAM_PAN);
            copyStringParam(bundle, params, Engine.KEY_FEATURE_NETWORK_SYNTHESIS);
            copyStringParam(bundle, params, Engine.KEY_FEATURE_EMBEDDED_SYNTHESIS);
            copyIntParam(bundle, params, Engine.KEY_FEATURE_NETWORK_TIMEOUT_MS);
            copyIntParam(bundle, params, Engine.KEY_FEATURE_NETWORK_RETRIES_COUNT);
            if (!TextUtils.isEmpty(this.mCurrentEngine)) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String key = entry.getKey();
                    if (key != null && key.startsWith(this.mCurrentEngine)) {
                        bundle.putString(key, entry.getValue());
                    }
                }
            }
            return bundle;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle getParams(Bundle params) {
        if (params != null && !params.isEmpty()) {
            Bundle bundle = new Bundle(this.mParams);
            bundle.putAll(params);
            verifyIntegerBundleParam(bundle, Engine.KEY_PARAM_STREAM);
            verifyIntegerBundleParam(bundle, Engine.KEY_PARAM_SESSION_ID);
            verifyStringBundleParam(bundle, Engine.KEY_PARAM_UTTERANCE_ID);
            verifyFloatBundleParam(bundle, "volume");
            verifyFloatBundleParam(bundle, Engine.KEY_PARAM_PAN);
            verifyBooleanBundleParam(bundle, Engine.KEY_FEATURE_NETWORK_SYNTHESIS);
            verifyBooleanBundleParam(bundle, Engine.KEY_FEATURE_EMBEDDED_SYNTHESIS);
            verifyIntegerBundleParam(bundle, Engine.KEY_FEATURE_NETWORK_TIMEOUT_MS);
            verifyIntegerBundleParam(bundle, Engine.KEY_FEATURE_NETWORK_RETRIES_COUNT);
            return bundle;
        }
        return this.mParams;
    }

    private static boolean verifyIntegerBundleParam(Bundle bundle, String key) {
        if (bundle.containsKey(key) && !(bundle.get(key) instanceof Integer) && !(bundle.get(key) instanceof Long)) {
            bundle.remove(key);
            Log.w(TAG, "Synthesis request paramter " + key + " containst value  with invalid type. Should be an Integer or a Long");
            return false;
        }
        return true;
    }

    private static boolean verifyStringBundleParam(Bundle bundle, String key) {
        if (bundle.containsKey(key) && !(bundle.get(key) instanceof String)) {
            bundle.remove(key);
            Log.w(TAG, "Synthesis request paramter " + key + " containst value  with invalid type. Should be a String");
            return false;
        }
        return true;
    }

    private static boolean verifyBooleanBundleParam(Bundle bundle, String key) {
        if (bundle.containsKey(key) && !(bundle.get(key) instanceof Boolean) && !(bundle.get(key) instanceof String)) {
            bundle.remove(key);
            Log.w(TAG, "Synthesis request paramter " + key + " containst value  with invalid type. Should be a Boolean or String");
            return false;
        }
        return true;
    }

    private static boolean verifyFloatBundleParam(Bundle bundle, String key) {
        if (bundle.containsKey(key) && !(bundle.get(key) instanceof Float) && !(bundle.get(key) instanceof Double)) {
            bundle.remove(key);
            Log.w(TAG, "Synthesis request paramter " + key + " containst value  with invalid type. Should be a Float or a Double");
            return false;
        }
        return true;
    }

    private void copyStringParam(Bundle bundle, HashMap<String, String> params, String key) {
        String value = params.get(key);
        if (value != null) {
            bundle.putString(key, value);
        }
    }

    private void copyIntParam(Bundle bundle, HashMap<String, String> params, String key) {
        String valueString = params.get(key);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                int value = Integer.parseInt(valueString);
                bundle.putInt(key, value);
            } catch (NumberFormatException e) {
            }
        }
    }

    private void copyFloatParam(Bundle bundle, HashMap<String, String> params, String key) {
        String valueString = params.get(key);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                float value = Float.parseFloat(valueString);
                bundle.putFloat(key, value);
            } catch (NumberFormatException e) {
            }
        }
    }

    @Deprecated
    public int setOnUtteranceCompletedListener(OnUtteranceCompletedListener listener) {
        this.mUtteranceProgressListener = UtteranceProgressListener.from(listener);
        return 0;
    }

    public int setOnUtteranceProgressListener(UtteranceProgressListener listener) {
        this.mUtteranceProgressListener = listener;
        return 0;
    }

    public int setOnStateChangedListener(OnStateChangedListener listener) {
        this.mStateChangedListener = listener;
        return ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.18
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                service.setListenTtsStateChanged(TextToSpeech.this.getCallerIdentity());
                return 0;
            }
        }, -1, "setListenOnStateChanged")).intValue();
    }

    public int setTtsStateListener(TtsStateListener listener) {
        this.mTtsStateListener = listener;
        return ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.19
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                service.setListenTtsStateChanged(TextToSpeech.this.getCallerIdentity());
                return 0;
            }
        }, -1, "setTtsStateListener")).intValue();
    }

    public void setOnInitExt(OnInitListenerExt listener) {
        this.mInitListenerExt = listener;
    }

    @Deprecated
    public int setEngineByPackageName(String enginePackageName) {
        this.mRequestedEngine = enginePackageName;
        return initTts();
    }

    public String getDefaultEngine() {
        return this.mEnginesHelper.getDefaultEngine();
    }

    public void setDefaultEngine(String engine) {
        this.mEnginesHelper.setDefaultEngine(engine);
    }

    @Deprecated
    public boolean areDefaultsEnforced() {
        return false;
    }

    public List<EngineInfo> getEngines() {
        return this.mEnginesHelper.getEngines();
    }

    public int setSoloMode(boolean on) {
        return setSoloMode(0, on);
    }

    public int setSoloMode(final int channel, final boolean on) {
        return ((Integer) runAction(new Action<Integer>() { // from class: android.speech.tts.TextToSpeech.20
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.speech.tts.TextToSpeech.Action
            public Integer run(ITextToSpeechService service) throws RemoteException {
                return Integer.valueOf(service.setSoloMode(TextToSpeech.this.getCallerIdentity(), channel, on));
            }
        }, -1, "setSoloMode")).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class Connection implements ServiceConnection {
        private final ITextToSpeechCallback.Stub mCallback;
        private boolean mEstablished;
        private SetupConnectionAsyncTask mOnSetupConnectionAsyncTask;
        private ITextToSpeechService mService;

        private Connection() {
            this.mCallback = new ITextToSpeechCallback.Stub() { // from class: android.speech.tts.TextToSpeech.Connection.1
                @Override // android.speech.tts.ITextToSpeechCallback
                public void onStop(Bundle params, String utteranceId, boolean isStarted) throws RemoteException {
                    params.putString(Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
                    params.putBoolean("isStarted", isStarted);
                    Message msg = TextToSpeech.this.mCallbackHandler.obtainMessage(0, params);
                    TextToSpeech.this.mCallbackHandler.sendMessage(msg);
                }

                @Override // android.speech.tts.ITextToSpeechCallback
                public void onSuccess(Bundle params, String utteranceId) {
                    params.putString(Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
                    Message msg = TextToSpeech.this.mCallbackHandler.obtainMessage(1, params);
                    TextToSpeech.this.mCallbackHandler.sendMessage(msg);
                }

                @Override // android.speech.tts.ITextToSpeechCallback
                public void onError(Bundle params, String utteranceId, int errorCode) {
                    params.putString(Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
                    params.putInt("errorCode", errorCode);
                    Message msg = TextToSpeech.this.mCallbackHandler.obtainMessage(2, params);
                    TextToSpeech.this.mCallbackHandler.sendMessage(msg);
                }

                @Override // android.speech.tts.ITextToSpeechCallback
                public void onStart(Bundle params, String utteranceId) {
                    params.putString(Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
                    Message msg = TextToSpeech.this.mCallbackHandler.obtainMessage(3, params);
                    TextToSpeech.this.mCallbackHandler.sendMessage(msg);
                }

                @Override // android.speech.tts.ITextToSpeechCallback
                public void onBeginSynthesis(String utteranceId, int sampleRateInHz, int audioFormat, int channelCount) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
                    bundle.putInt("sampleRateInHz", sampleRateInHz);
                    bundle.putInt("audioFormat", audioFormat);
                    bundle.putInt("channelCount", channelCount);
                    Message msg = TextToSpeech.this.mCallbackHandler.obtainMessage(4, bundle);
                    TextToSpeech.this.mCallbackHandler.sendMessage(msg);
                }

                @Override // android.speech.tts.ITextToSpeechCallback
                public void onAudioAvailable(String utteranceId, byte[] audio) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
                    bundle.putByteArray("audio", audio);
                    Message msg = TextToSpeech.this.mCallbackHandler.obtainMessage(5, bundle);
                    TextToSpeech.this.mCallbackHandler.sendMessage(msg);
                }

                @Override // android.speech.tts.ITextToSpeechCallback
                public void onRangeStart(String utteranceId, int start, int end, int frame) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
                    bundle.putInt(Telephony.BaseMmsColumns.START, start);
                    bundle.putInt("end", end);
                    bundle.putInt("frame", frame);
                    Message msg = TextToSpeech.this.mCallbackHandler.obtainMessage(6, bundle);
                    TextToSpeech.this.mCallbackHandler.sendMessage(msg);
                }

                @Override // android.speech.tts.ITextToSpeechCallback
                public void onStateChanged(String state) {
                    Message msg = TextToSpeech.this.mCallbackHandler.obtainMessage(7, state);
                    TextToSpeech.this.mCallbackHandler.sendMessage(msg);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public class SetupConnectionAsyncTask extends AsyncTask<Void, Void, Integer> {
            private final ComponentName mName;

            public SetupConnectionAsyncTask(ComponentName name) {
                this.mName = name;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Integer doInBackground(Void... params) {
                synchronized (TextToSpeech.this.mStartLock) {
                    if (!isCancelled()) {
                        try {
                            Connection.this.mService.setCallback(Connection.this.getCallerIdentity(), Connection.this.mCallback);
                            if (TextToSpeech.this.mParams.getString("language") == null) {
                                String[] defaultLanguage = Connection.this.mService.getClientDefaultLanguage();
                                TextToSpeech.this.mParams.putString("language", defaultLanguage[0]);
                                TextToSpeech.this.mParams.putString(Engine.KEY_PARAM_COUNTRY, defaultLanguage[1]);
                                TextToSpeech.this.mParams.putString(Engine.KEY_PARAM_VARIANT, defaultLanguage[2]);
                                String defaultVoiceName = Connection.this.mService.getDefaultVoiceNameFor(defaultLanguage[0], defaultLanguage[1], defaultLanguage[2]);
                                TextToSpeech.this.mParams.putString(Engine.KEY_PARAM_VOICE_NAME, defaultVoiceName);
                            }
                            Log.i(TextToSpeech.TAG, "Set up connection to " + this.mName);
                            return 0;
                        } catch (RemoteException e) {
                            Log.e(TextToSpeech.TAG, "Error connecting to service, setCallback() failed");
                            return -1;
                        }
                    }
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Integer result) {
                synchronized (TextToSpeech.this.mStartLock) {
                    if (Connection.this.mOnSetupConnectionAsyncTask == this) {
                        Connection.this.mOnSetupConnectionAsyncTask = null;
                    }
                    Connection.this.mEstablished = true;
                    TextToSpeech.this.dispatchOnInit(result.intValue());
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (TextToSpeech.this.mStartLock) {
                TextToSpeech.this.mConnectingServiceConnection = null;
                Log.i(TextToSpeech.TAG, "Connected to " + name);
                if (this.mOnSetupConnectionAsyncTask != null) {
                    this.mOnSetupConnectionAsyncTask.cancel(false);
                }
                this.mService = ITextToSpeechService.Stub.asInterface(service);
                TextToSpeech.this.mServiceConnection = this;
                this.mEstablished = false;
                this.mOnSetupConnectionAsyncTask = new SetupConnectionAsyncTask(name);
                this.mOnSetupConnectionAsyncTask.execute(new Void[0]);
                if (!TextToSpeech.this.mCurrentUidList.isEmpty()) {
                    Iterator it = TextToSpeech.this.mCurrentUidList.iterator();
                    while (it.hasNext()) {
                        String uid = (String) it.next();
                        Bundle params = new Bundle();
                        params.putString(Engine.KEY_PARAM_UTTERANCE_ID, uid);
                        params.putInt("errorCode", -4);
                        params.putBoolean("notRemove", true);
                        Message msg = TextToSpeech.this.mCallbackHandler.obtainMessage(2, params);
                        TextToSpeech.this.mCallbackHandler.sendMessage(msg);
                    }
                    TextToSpeech.this.mCurrentUidList.clear();
                }
            }
        }

        public IBinder getCallerIdentity() {
            return this.mCallback;
        }

        private boolean clearServiceConnection() {
            boolean result;
            synchronized (TextToSpeech.this.mStartLock) {
                result = false;
                if (this.mOnSetupConnectionAsyncTask != null) {
                    result = this.mOnSetupConnectionAsyncTask.cancel(false);
                    this.mOnSetupConnectionAsyncTask = null;
                }
                this.mService = null;
                if (TextToSpeech.this.mServiceConnection == this) {
                    TextToSpeech.this.mServiceConnection = null;
                }
            }
            return result;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            Log.v(TextToSpeech.TAG, "Asked to disconnect from " + name);
            if (clearServiceConnection()) {
                TextToSpeech.this.dispatchOnInit(-1);
            }
        }

        public void disconnect() {
            TextToSpeech.this.mContext.unbindService(this);
            clearServiceConnection();
        }

        public boolean isEstablished() {
            return this.mService != null && this.mEstablished;
        }

        public <R> R runAction(Action<R> action, R errorResult, String method, boolean reconnect, boolean onlyEstablishedConnection) {
            synchronized (TextToSpeech.this.mStartLock) {
                try {
                    try {
                        if (this.mService == null) {
                            Log.w(TextToSpeech.TAG, method + " failed: not connected to TTS engine");
                            return errorResult;
                        } else if (onlyEstablishedConnection && !isEstablished()) {
                            Log.w(TextToSpeech.TAG, method + " failed: TTS engine connection not fully set up");
                            return errorResult;
                        } else {
                            return action.run(this.mService);
                        }
                    } catch (RemoteException ex) {
                        Log.e(TextToSpeech.TAG, method + " failed", ex);
                        if (reconnect) {
                            disconnect();
                            TextToSpeech.this.initTts();
                        }
                        return errorResult;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class EngineInfo {
        public int icon;
        public String label;
        public String name;
        public int priority;
        public boolean system;

        public String toString() {
            return "EngineInfo{name=" + this.name + "}";
        }
    }

    public static int getMaxSpeechInputLength() {
        return 4000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class CallbackHandler extends Handler {
        public CallbackHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle params = (Bundle) msg.obj;
                    String utteranceId = params.getString(Engine.KEY_PARAM_UTTERANCE_ID);
                    boolean isStarted = params.getBoolean("isStarted");
                    Log.i(TextToSpeech.TAG, "onStop " + utteranceId + " isStarted " + isStarted);
                    try {
                        synchronized (TextToSpeech.this.mStartLock) {
                            TextToSpeech.this.mCurrentUidList.remove(utteranceId);
                        }
                    } catch (Exception e) {
                        Log.e(TextToSpeech.TAG, "remove uid fail", e);
                    }
                    UtteranceProgressListener listener = TextToSpeech.this.mUtteranceProgressListener;
                    if (listener != null) {
                        params.putBoolean("interrupted", isStarted);
                        listener.onStop(params);
                        return;
                    }
                    return;
                case 1:
                    Bundle params2 = (Bundle) msg.obj;
                    String utteranceId2 = params2.getString(Engine.KEY_PARAM_UTTERANCE_ID);
                    Log.i(TextToSpeech.TAG, "onDone " + utteranceId2);
                    try {
                        synchronized (TextToSpeech.this.mStartLock) {
                            TextToSpeech.this.mCurrentUidList.remove(utteranceId2);
                        }
                    } catch (Exception e2) {
                        Log.e(TextToSpeech.TAG, "remove uid fail", e2);
                    }
                    UtteranceProgressListener listener2 = TextToSpeech.this.mUtteranceProgressListener;
                    if (listener2 != null) {
                        listener2.onDone(params2);
                        return;
                    }
                    return;
                case 2:
                    Bundle params3 = (Bundle) msg.obj;
                    String utteranceId3 = params3.getString(Engine.KEY_PARAM_UTTERANCE_ID);
                    int errorCode = params3.getInt("errorCode");
                    Log.e(TextToSpeech.TAG, "onError " + utteranceId3 + " errorCode " + errorCode);
                    if (params3.getBoolean("notRemove", false)) {
                        try {
                            synchronized (TextToSpeech.this.mStartLock) {
                                TextToSpeech.this.mCurrentUidList.remove(utteranceId3);
                            }
                        } catch (Exception e3) {
                            Log.e(TextToSpeech.TAG, "remove uid fail", e3);
                        }
                    }
                    UtteranceProgressListener listener3 = TextToSpeech.this.mUtteranceProgressListener;
                    if (listener3 != null) {
                        listener3.onError(params3);
                        return;
                    }
                    return;
                case 3:
                    Bundle params4 = (Bundle) msg.obj;
                    String utteranceId4 = params4.getString(Engine.KEY_PARAM_UTTERANCE_ID);
                    Log.i(TextToSpeech.TAG, "onStart " + utteranceId4);
                    UtteranceProgressListener listener4 = TextToSpeech.this.mUtteranceProgressListener;
                    if (listener4 != null) {
                        listener4.onStart(params4);
                        return;
                    }
                    return;
                case 4:
                    Bundle bundle = (Bundle) msg.obj;
                    String utteranceId5 = bundle.getString(Engine.KEY_PARAM_UTTERANCE_ID);
                    int sampleRateInHz = bundle.getInt("sampleRateInHz");
                    int audioFormat = bundle.getInt("audioFormat");
                    int channelCount = bundle.getInt("channelCount");
                    UtteranceProgressListener listener5 = TextToSpeech.this.mUtteranceProgressListener;
                    if (listener5 != null) {
                        listener5.onBeginSynthesis(utteranceId5, sampleRateInHz, audioFormat, channelCount);
                        return;
                    }
                    return;
                case 5:
                    Bundle bundle2 = (Bundle) msg.obj;
                    String utteranceId6 = bundle2.getString(Engine.KEY_PARAM_UTTERANCE_ID);
                    byte[] audio = bundle2.getByteArray("audio");
                    UtteranceProgressListener listener6 = TextToSpeech.this.mUtteranceProgressListener;
                    if (listener6 != null) {
                        listener6.onAudioAvailable(utteranceId6, audio);
                        return;
                    }
                    return;
                case 6:
                    Bundle bundle3 = (Bundle) msg.obj;
                    String utteranceId7 = bundle3.getString(Engine.KEY_PARAM_UTTERANCE_ID);
                    int start = bundle3.getInt(Telephony.BaseMmsColumns.START);
                    int end = bundle3.getInt("end");
                    int frame = bundle3.getInt("frame");
                    UtteranceProgressListener listener7 = TextToSpeech.this.mUtteranceProgressListener;
                    if (listener7 != null) {
                        listener7.onRangeStart(utteranceId7, start, end, frame);
                        return;
                    }
                    return;
                case 7:
                    String info = (String) msg.obj;
                    Log.d(TextToSpeech.TAG, "onStateChanged " + info);
                    try {
                        JSONObject stateObject = new JSONObject(info);
                        try {
                            if (TextToSpeech.this.mStateChangedListener != null) {
                                String type = stateObject.optString("type");
                                String source = stateObject.optString(Slice.SUBTYPE_SOURCE);
                                int state = stateObject.optInt("priority");
                                if (type.equals("")) {
                                    TextToSpeech.this.mStateChangedListener.onStateChanged(source, state);
                                } else if (!type.equals(AccountManager.USER_DATA_EXTRA_UPDATE)) {
                                    if (type.equals("end")) {
                                        state = 0;
                                    } else if (type.equals("error")) {
                                        state = -1;
                                    }
                                    TextToSpeech.this.mStateChangedListener.onStateChanged(source, state);
                                }
                            }
                        } catch (Exception e4) {
                            Log.e(TextToSpeech.TAG, "error occur when onStateChanged callback");
                        }
                        try {
                            if (TextToSpeech.this.mTtsStateListener != null) {
                                TextToSpeech.this.mTtsStateListener.onStateChanged(info);
                                return;
                            }
                            return;
                        } catch (Exception e5) {
                            Log.e(TextToSpeech.TAG, "error occur when onTtsStateChanged callback");
                            return;
                        }
                    } catch (Exception e6) {
                        Log.e(TextToSpeech.TAG, "state format error", e6);
                        return;
                    }
                case 8:
                    Log.d(TextToSpeech.TAG, "engine reconnect");
                    TextToSpeech.this.reConnectNewEngine();
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reConnectNewEngine() {
        synchronized (this.mStartLock) {
            if (this.mIsShutDown) {
                return;
            }
            try {
                if (this.mServiceConnection != null) {
                    this.mServiceConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e(TAG, "Reconnect: disconnect fail", e);
            }
            initTts();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isReConnectEngineNeeded() {
        if (this.mCurrentEngine == null) {
            Log.v(TAG, "client did not connect any engine yet");
            return true;
        }
        String str = this.mRequestedEngine;
        if (str != null && this.mEnginesHelper.isEngineInstalled(str)) {
            if (this.mCurrentEngine.equals(this.mRequestedEngine)) {
                Log.v(TAG, "requested engine was already connected");
                return false;
            }
            Log.v(TAG, "connect to requested engine");
            return true;
        }
        String defaultEngine = this.mEnginesHelper.getDefaultEngine();
        if (defaultEngine != null) {
            if (this.mCurrentEngine.equals(defaultEngine)) {
                Log.v(TAG, "default engine already connected");
                return false;
            }
            Log.v(TAG, "connect to new default engine " + defaultEngine);
            return true;
        }
        Log.e(TAG, "no engine can be used");
        return false;
    }

    /* loaded from: classes2.dex */
    private class TTSEngineChangedReceiver extends BroadcastReceiver {
        private TTSEngineChangedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v(TextToSpeech.TAG, "onReceive action " + action);
            synchronized (TextToSpeech.this.mStartLock) {
                if (TextToSpeech.this.mIsShutDown) {
                    return;
                }
                if (action.equals(TextToSpeech.ACTION_TTS_ENGINE_CHANGED) && TextToSpeech.this.mIsInited && TextToSpeech.this.isReConnectEngineNeeded()) {
                    TextToSpeech.this.mIsInited = false;
                    TextToSpeech.this.mCallbackHandler.sendEmptyMessage(8);
                }
            }
        }
    }
}
