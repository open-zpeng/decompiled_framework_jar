package android.speech.tts;

import android.app.ActivityManager;
import android.app.Service;
import android.app.slice.Slice;
import android.bluetooth.BluetoothHeadsetClient;
import android.bluetooth.BluetoothHeadsetClientCall;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.speech.tts.ITextToSpeechService;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: classes2.dex */
public abstract class TextToSpeechService extends Service {
    private static final boolean DBG = true;
    private static final int DEFAULT_STREAM = 10;
    private static final String TAG = "TextToSpeechService";
    private static final String VOL_DOWN_ON_NAVI = "decrease_volume_navigating";
    private AudioFocusHelper mAudioFocusHelper;
    private AudioManager mAudioManager;
    private AudioPlaybackHandler mAudioPlaybackHandler;
    private CallbackMap mCallbacks;
    private Context mContext;
    private TtsEngines mEngineHelper;
    private HandlerThread mHandlerThread;
    private String mPackageName;
    private SynthHandler mSynthHandler;
    private VolDownOnNavi mVolDownOnNavi;
    private final BroadcastReceiver mReceiver = new CallStateReceiver();
    private volatile boolean mIsVolDownOnNavi = true;
    private final Object mVoicesInfoLock = new Object();
    private volatile boolean mIsCallStateActive = false;
    private final ITextToSpeechService.Stub mBinder = new ITextToSpeechService.Stub() { // from class: android.speech.tts.TextToSpeechService.1
        @Override // android.speech.tts.ITextToSpeechService
        public int speak(IBinder caller, CharSequence text, int queueMode, Bundle params, String utteranceId) {
            String str;
            if (!checkNonNull(caller, text, params)) {
                return -1;
            }
            int priority = params.getInt("priority", 1);
            String name = TextToSpeechService.this.getPackageNameByPid(Binder.getCallingPid());
            params.putString(Slice.SUBTYPE_SOURCE, name);
            StringBuilder sb = new StringBuilder();
            sb.append("speak ");
            sb.append(TextToSpeechService.this.getPriorityName(priority));
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb.append(TextToSpeechService.this.getQueueModeName(queueMode));
            sb.append(" by ");
            sb.append(name);
            sb.append(" text ");
            if (text.length() > 40) {
                str = ((Object) text.subSequence(0, 40)) + "...";
            } else {
                str = text;
            }
            sb.append((Object) str);
            sb.append(" id ");
            sb.append(utteranceId);
            Log.i(TextToSpeechService.TAG, sb.toString());
            float volume = params.getFloat("volume", 1.0f);
            if (volume < 0.0f) {
                volume = 0.0f;
            } else if (volume > 1.0f) {
                volume = 1.0f;
            }
            params.putFloat("volume", volume);
            SpeechItem item = new SynthesisSpeechItem(caller, Binder.getCallingUid(), Binder.getCallingPid(), priority, params, utteranceId, text);
            return TextToSpeechService.this.mSynthHandler.enqueueSpeechItem(queueMode, item);
        }

        @Override // android.speech.tts.ITextToSpeechService
        public int synthesizeToFileDescriptor(IBinder caller, CharSequence text, ParcelFileDescriptor fileDescriptor, Bundle params, String utteranceId) {
            return !checkNonNull(caller, text, fileDescriptor, params) ? -1 : -1;
        }

        @Override // android.speech.tts.ITextToSpeechService
        public int playAudio(IBinder caller, Uri audioUri, int queueMode, Bundle params, String utteranceId) {
            return !checkNonNull(caller, audioUri, params) ? -1 : -1;
        }

        @Override // android.speech.tts.ITextToSpeechService
        public int playSilence(IBinder caller, long duration, int queueMode, String utteranceId) {
            return !checkNonNull(caller) ? -1 : -1;
        }

        @Override // android.speech.tts.ITextToSpeechService
        public boolean isSpeaking() {
            return TextToSpeechService.this.mSynthHandler.isSpeaking() || TextToSpeechService.this.mAudioPlaybackHandler.isSpeaking();
        }

        @Override // android.speech.tts.ITextToSpeechService
        public int stop(final IBinder caller, final String utteranceId) {
            if (checkNonNull(caller)) {
                String name = TextToSpeechService.this.getPackageNameByPid(Binder.getCallingPid());
                Log.d(TextToSpeechService.TAG, "stop " + utteranceId + " by " + name);
                TextToSpeechService.this.mSynthHandler.post(new Runnable() { // from class: android.speech.tts.TextToSpeechService.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        TextToSpeechService.this.mSynthHandler.stopForApp(caller, utteranceId);
                    }
                });
                return 0;
            }
            return -1;
        }

        @Override // android.speech.tts.ITextToSpeechService
        public String[] getLanguage() {
            return TextToSpeechService.this.onGetLanguage();
        }

        @Override // android.speech.tts.ITextToSpeechService
        public String[] getClientDefaultLanguage() {
            return TextToSpeechService.this.getSettingsLocale();
        }

        @Override // android.speech.tts.ITextToSpeechService
        public int isLanguageAvailable(String lang, String country, String variant) {
            if (!checkNonNull(lang)) {
                return -1;
            }
            return TextToSpeechService.this.onIsLanguageAvailable(lang, country, variant);
        }

        @Override // android.speech.tts.ITextToSpeechService
        public String[] getFeaturesForLanguage(String lang, String country, String variant) {
            Set<String> features = TextToSpeechService.this.onGetFeaturesForLanguage(lang, country, variant);
            if (features != null) {
                String[] featuresArray = new String[features.size()];
                features.toArray(featuresArray);
                return featuresArray;
            }
            return new String[0];
        }

        @Override // android.speech.tts.ITextToSpeechService
        public int loadLanguage(IBinder caller, String lang, String country, String variant) {
            if (!checkNonNull(lang)) {
                return -1;
            }
            int retVal = TextToSpeechService.this.onIsLanguageAvailable(lang, country, variant);
            return retVal;
        }

        @Override // android.speech.tts.ITextToSpeechService
        public List<Voice> getVoices() {
            return TextToSpeechService.this.onGetVoices();
        }

        @Override // android.speech.tts.ITextToSpeechService
        public int loadVoice(IBinder caller, String voiceName) {
            if (!checkNonNull(voiceName)) {
                return -1;
            }
            int retVal = TextToSpeechService.this.onIsValidVoiceName(voiceName);
            return retVal;
        }

        @Override // android.speech.tts.ITextToSpeechService
        public String getDefaultVoiceNameFor(String lang, String country, String variant) {
            if (checkNonNull(lang)) {
                int retVal = TextToSpeechService.this.onIsLanguageAvailable(lang, country, variant);
                if (retVal == 0 || retVal == 1 || retVal == 2) {
                    return TextToSpeechService.this.onGetDefaultVoiceNameFor(lang, country, variant);
                }
                return null;
            }
            return null;
        }

        @Override // android.speech.tts.ITextToSpeechService
        public void setCallback(IBinder caller, ITextToSpeechCallback cb) {
            if (checkNonNull(caller)) {
                String name = TextToSpeechService.this.getPackageNameByPid(Binder.getCallingPid());
                TextToSpeechService.this.mCallbacks.setCallback(caller, cb, name);
            }
        }

        @Override // android.speech.tts.ITextToSpeechService
        public int setSoloMode(final IBinder caller, final boolean on) {
            if (checkNonNull(caller)) {
                String name = TextToSpeechService.this.getPackageNameByPid(Binder.getCallingPid());
                Log.d(TextToSpeechService.TAG, "setSoloMode " + on + " by " + name);
                TextToSpeechService.this.mSynthHandler.post(new Runnable() { // from class: android.speech.tts.TextToSpeechService.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        TextToSpeechService.this.mSynthHandler.setSoloMode(caller, on);
                    }
                });
                return 0;
            }
            return -1;
        }

        @Override // android.speech.tts.ITextToSpeechService
        public int setListenOnStateChanged(IBinder caller) {
            if (checkNonNull(caller)) {
                String name = TextToSpeechService.this.getPackageNameByPid(Binder.getCallingPid());
                Log.d(TextToSpeechService.TAG, "setListenOnStateChanged by " + name);
                TextToSpeechService.this.mCallbacks.setListenOnStateChanged(caller);
                return 0;
            }
            return -1;
        }

        private String intern(String in) {
            return in.intern();
        }

        private boolean checkNonNull(Object... args) {
            for (Object o : args) {
                if (o == null) {
                    return false;
                }
            }
            return true;
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface UtteranceProgressDispatcher {
        synchronized void dispatchOnAudioAvailable(byte[] bArr);

        synchronized void dispatchOnBeginSynthesis(int i, int i2, int i3);

        synchronized void dispatchOnError(int i);

        synchronized void dispatchOnRangeStart(int i, int i2, int i3);

        synchronized void dispatchOnStart();

        synchronized void dispatchOnStop();

        synchronized void dispatchOnSuccess();
    }

    protected abstract String[] onGetLanguage();

    protected abstract int onIsLanguageAvailable(String str, String str2, String str3);

    protected abstract int onLoadLanguage(String str, String str2, String str3);

    protected abstract void onStop();

    protected abstract void onSynthesizeText(SynthesisRequest synthesisRequest, SynthesisCallback synthesisCallback);

    @Override // android.app.Service
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();
        this.mContext = this;
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.mAudioFocusHelper = new AudioFocusHelper(this.mContext);
        this.mHandlerThread = new HandlerThread("SynthHandler");
        this.mHandlerThread.start();
        this.mSynthHandler = new SynthHandler(this.mHandlerThread.getLooper());
        this.mAudioPlaybackHandler = new AudioPlaybackHandler();
        this.mAudioPlaybackHandler.start();
        this.mEngineHelper = new TtsEngines(this);
        this.mCallbacks = new CallbackMap();
        this.mPackageName = getApplicationInfo().packageName;
        String[] defaultLocale = getSettingsLocale();
        onLoadLanguage(defaultLocale[0], defaultLocale[1], defaultLocale[2]);
        IntentFilter intentFilter = new IntentFilter(BluetoothHeadsetClient.ACTION_CALL_CHANGED);
        intentFilter.addAction(BluetoothHeadsetClient.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(this.mReceiver, intentFilter);
        this.mVolDownOnNavi = new VolDownOnNavi(this.mSynthHandler);
        ContentResolver contentResolver = getContentResolver();
        this.mIsVolDownOnNavi = Settings.System.getInt(contentResolver, VOL_DOWN_ON_NAVI, 1) == 1;
        contentResolver.registerContentObserver(Settings.System.getUriFor(VOL_DOWN_ON_NAVI), true, this.mVolDownOnNavi);
    }

    @Override // android.app.Service
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        this.mSynthHandler.quit();
        this.mHandlerThread.quit();
        this.mAudioPlaybackHandler.quit();
        this.mCallbacks.kill();
        unregisterReceiver(this.mReceiver);
        ContentResolver contentResolver = getContentResolver();
        contentResolver.unregisterContentObserver(this.mVolDownOnNavi);
        super.onDestroy();
    }

    protected Set<String> onGetFeaturesForLanguage(String lang, String country, String variant) {
        return new HashSet();
    }

    private synchronized int getExpectedLanguageAvailableStatus(Locale locale) {
        if (!locale.getVariant().isEmpty()) {
            return 2;
        }
        if (locale.getCountry().isEmpty()) {
            return 0;
        }
        return 1;
    }

    public List<Voice> onGetVoices() {
        TextToSpeechService textToSpeechService = this;
        ArrayList<Voice> voices = new ArrayList<>();
        Locale[] availableLocales = Locale.getAvailableLocales();
        int length = availableLocales.length;
        int i = 0;
        while (i < length) {
            Locale locale = availableLocales[i];
            int expectedStatus = textToSpeechService.getExpectedLanguageAvailableStatus(locale);
            try {
                int localeStatus = textToSpeechService.onIsLanguageAvailable(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
                if (localeStatus == expectedStatus) {
                    Set<String> features = textToSpeechService.onGetFeaturesForLanguage(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
                    String voiceName = textToSpeechService.onGetDefaultVoiceNameFor(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
                    voices.add(new Voice(voiceName, locale, 300, 300, false, features));
                }
            } catch (MissingResourceException e) {
            }
            i++;
            textToSpeechService = this;
        }
        return voices;
    }

    public String onGetDefaultVoiceNameFor(String lang, String country, String variant) {
        Locale iso3Locale;
        int localeStatus = onIsLanguageAvailable(lang, country, variant);
        switch (localeStatus) {
            case 0:
                iso3Locale = new Locale(lang);
                break;
            case 1:
                iso3Locale = new Locale(lang, country);
                break;
            case 2:
                iso3Locale = new Locale(lang, country, variant);
                break;
            default:
                return null;
        }
        Locale properLocale = TtsEngines.normalizeTTSLocale(iso3Locale);
        String voiceName = properLocale.toLanguageTag();
        if (onIsValidVoiceName(voiceName) != 0) {
            return null;
        }
        return voiceName;
    }

    public int onLoadVoice(String voiceName) {
        Locale locale = Locale.forLanguageTag(voiceName);
        if (locale == null) {
            return -1;
        }
        int expectedStatus = getExpectedLanguageAvailableStatus(locale);
        try {
            int localeStatus = onIsLanguageAvailable(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
            if (localeStatus != expectedStatus) {
                return -1;
            }
            onLoadLanguage(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
            return 0;
        } catch (MissingResourceException e) {
            return -1;
        }
    }

    public int onIsValidVoiceName(String voiceName) {
        Locale locale = Locale.forLanguageTag(voiceName);
        if (locale == null) {
            return -1;
        }
        int expectedStatus = getExpectedLanguageAvailableStatus(locale);
        try {
            int localeStatus = onIsLanguageAvailable(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
            if (localeStatus != expectedStatus) {
                return -1;
            }
            return 0;
        } catch (MissingResourceException e) {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getDefaultSpeechRate() {
        return getSecureSettingInt(Settings.Secure.TTS_DEFAULT_RATE, 100);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getDefaultPitch() {
        return getSecureSettingInt(Settings.Secure.TTS_DEFAULT_PITCH, 100);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String[] getSettingsLocale() {
        Locale locale = this.mEngineHelper.getLocalePrefForEngine(this.mPackageName);
        return TtsEngines.toOldLocaleStringFormat(locale);
    }

    private synchronized int getSecureSettingInt(String name, int defaultValue) {
        return Settings.Secure.getInt(getContentResolver(), name, defaultValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class SynthHandler extends Handler {
        private SpeechItem mCurrentSpeechItem;
        private final Condition mLinkCondition;
        private final Lock mLinkLock;
        private volatile Object mSoloCaller;
        private LinkedList<SpeechItem> mSpeechItemLink;
        private SynthThread mSynthThread;

        public SynthHandler(Looper looper) {
            super(looper);
            this.mLinkLock = new ReentrantLock();
            this.mLinkCondition = this.mLinkLock.newCondition();
            this.mCurrentSpeechItem = null;
            this.mSoloCaller = null;
            this.mSpeechItemLink = new LinkedList<>();
            this.mSynthThread = new SynthThread();
            this.mSynthThread.start();
        }

        private synchronized SpeechItem getCurrentSpeechItem() {
            return this.mCurrentSpeechItem;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setCurrentSpeechItem(SpeechItem speechItem) {
            this.mCurrentSpeechItem = speechItem;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized SpeechItem removeCurrentSpeechItem() {
            SpeechItem current;
            current = this.mCurrentSpeechItem;
            this.mCurrentSpeechItem = null;
            return current;
        }

        private synchronized SpeechItem maybeRemoveCurrentSpeechItem(Object callerIdentity) {
            if (this.mCurrentSpeechItem == null || this.mCurrentSpeechItem.getCallerIdentity() != callerIdentity) {
                return null;
            }
            SpeechItem current = this.mCurrentSpeechItem;
            this.mCurrentSpeechItem = null;
            return current;
        }

        private synchronized SpeechItem maybeRemoveCurrentSpeechItem(Object callerIdentity, String utteranceId) {
            if (this.mCurrentSpeechItem != null && this.mCurrentSpeechItem.getCallerIdentity() == callerIdentity) {
                if (utteranceId == null) {
                    SpeechItem current = this.mCurrentSpeechItem;
                    this.mCurrentSpeechItem = null;
                    return current;
                }
                SpeechItem current2 = this.mCurrentSpeechItem;
                if (current2 instanceof UtteranceSpeechItem) {
                    UtteranceSpeechItem utterenceItem = (UtteranceSpeechItem) this.mCurrentSpeechItem;
                    if (utterenceItem.getUtteranceId().equals(utteranceId)) {
                        SpeechItem current3 = this.mCurrentSpeechItem;
                        this.mCurrentSpeechItem = null;
                        return current3;
                    }
                }
            }
            return null;
        }

        private synchronized SpeechItem removeCurrentItemByPriority(int priority) {
            if (this.mCurrentSpeechItem == null || priority <= this.mCurrentSpeechItem.getPriority()) {
                return null;
            }
            SpeechItem current = this.mCurrentSpeechItem;
            this.mCurrentSpeechItem = null;
            return current;
        }

        private synchronized SpeechItem removeCurrentItemBySolo() {
            if (this.mCurrentSpeechItem == null || this.mCurrentSpeechItem.getPriority() == 4 || this.mCurrentSpeechItem.getCallerIdentity() == this.mSoloCaller) {
                return null;
            }
            SpeechItem current = this.mCurrentSpeechItem;
            this.mCurrentSpeechItem = null;
            return current;
        }

        public synchronized boolean isSpeaking() {
            return getCurrentSpeechItem() != null;
        }

        public synchronized void quit() {
            this.mSynthThread.quit();
            SpeechItem current = removeCurrentSpeechItem();
            if (current != null) {
                Log.d(TextToSpeechService.TAG, "stop current item due to quit");
                current.stop();
            }
            removeCallbacksAndMessages(null);
        }

        public synchronized int enqueueSpeechItem(final int queueMode, final SpeechItem speechItem) {
            UtteranceProgressDispatcher utterenceProgress = null;
            if (speechItem instanceof UtteranceProgressDispatcher) {
                utterenceProgress = (UtteranceProgressDispatcher) speechItem;
            }
            if (!speechItem.isValid()) {
                if (utterenceProgress != null) {
                    utterenceProgress.dispatchOnError(-8);
                }
                Log.e(TextToSpeechService.TAG, "item is not valid");
                return 0;
            }
            post(new Runnable() { // from class: android.speech.tts.TextToSpeechService.SynthHandler.1
                @Override // java.lang.Runnable
                public void run() {
                    SynthHandler.this.handleEnqueueSpeechItem(queueMode, speechItem);
                }
            });
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleEnqueueSpeechItem(int queueMode, SpeechItem speechItem) {
            SpeechItem current;
            if (queueMode == 0) {
                stopForApp(speechItem.getCallerIdentity(), null);
            } else if (queueMode == 2) {
                stopAll();
            }
            int priority = speechItem.getPriority();
            try {
                try {
                    this.mLinkLock.lock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (this.mSoloCaller != null && speechItem.getCallerIdentity() != this.mSoloCaller && priority != 4) {
                    Log.d(TextToSpeechService.TAG, "item rejected by solo mode");
                    if (speechItem instanceof UtteranceProgressDispatcher) {
                        UtteranceProgressDispatcher utterenceProgress = (UtteranceProgressDispatcher) speechItem;
                        utterenceProgress.dispatchOnError(-10);
                    }
                    return;
                }
                if (this.mSpeechItemLink.size() != 0) {
                    switch (priority) {
                        case 1:
                            Log.d(TextToSpeechService.TAG, "add the item to the end of the queue");
                            this.mSpeechItemLink.add(speechItem);
                            break;
                        case 2:
                        case 3:
                        case 4:
                            ListIterator<SpeechItem> iter = this.mSpeechItemLink.listIterator();
                            while (true) {
                                if (iter.hasNext()) {
                                    SpeechItem item = iter.next();
                                    if (item.getPriority() < priority) {
                                        Log.d(TextToSpeechService.TAG, "add the item to the middle of the queue");
                                        iter.previous();
                                    }
                                }
                            }
                            if (!iter.hasNext()) {
                                Log.d(TextToSpeechService.TAG, "add the item to the end of the queue");
                            }
                            iter.add(speechItem);
                            break;
                    }
                } else {
                    Log.d(TextToSpeechService.TAG, "add the item to empty queue");
                    this.mSpeechItemLink.add(speechItem);
                    this.mLinkCondition.signal();
                }
                if (priority >= 3 && (current = removeCurrentItemByPriority(priority)) != null) {
                    try {
                        Log.d(TextToSpeechService.TAG, "stop current item due to urgent add");
                        current.stop();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            } finally {
                this.mLinkLock.unlock();
            }
        }

        public int stopForApp(Object callerIdentity, String utteranceId) {
            if (callerIdentity == null) {
                return -1;
            }
            Log.d(TextToSpeechService.TAG, "stopForApp " + utteranceId);
            try {
                try {
                    this.mLinkLock.lock();
                    ListIterator<SpeechItem> iter = this.mSpeechItemLink.listIterator();
                    while (iter.hasNext()) {
                        SpeechItem item = iter.next();
                        if (item.getCallerIdentity() == callerIdentity) {
                            if (utteranceId == null) {
                                if (item instanceof UtteranceProgressDispatcher) {
                                    UtteranceProgressDispatcher dispatcher = (UtteranceProgressDispatcher) item;
                                    dispatcher.dispatchOnStop();
                                }
                                iter.remove();
                            } else if (item instanceof UtteranceSpeechItem) {
                                UtteranceSpeechItem utterenceItem = (UtteranceSpeechItem) item;
                                if (utterenceItem.getUtteranceId().equals(utteranceId)) {
                                    utterenceItem.dispatchOnStop();
                                    iter.remove();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.mLinkLock.unlock();
                SpeechItem current = maybeRemoveCurrentSpeechItem(callerIdentity, utteranceId);
                if (current != null) {
                    Log.d(TextToSpeechService.TAG, "stop current item due to app stop");
                    current.stop();
                    return 0;
                }
                return 0;
            } catch (Throwable th) {
                this.mLinkLock.unlock();
                throw th;
            }
        }

        public synchronized int stopAll() {
            try {
                try {
                    this.mLinkLock.lock();
                    ListIterator<SpeechItem> iter = this.mSpeechItemLink.listIterator();
                    while (iter.hasNext()) {
                        SpeechItem item = iter.next();
                        if (item.getPriority() < 4) {
                            if (item instanceof UtteranceProgressDispatcher) {
                                UtteranceProgressDispatcher dispatcher = (UtteranceProgressDispatcher) item;
                                dispatcher.dispatchOnStop();
                            }
                            iter.remove();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.mLinkLock.unlock();
                SpeechItem current = removeCurrentItemByPriority(4);
                if (current != null) {
                    Log.d(TextToSpeechService.TAG, "stop current item due to destory");
                    current.stop();
                    return 0;
                }
                return 0;
            } catch (Throwable th) {
                this.mLinkLock.unlock();
                throw th;
            }
        }

        public int setSoloMode(Object caller, boolean on) {
            int ret = -1;
            try {
                try {
                    this.mLinkLock.lock();
                    if (this.mSoloCaller == null) {
                        if (on) {
                            this.mSoloCaller = caller;
                            ListIterator<SpeechItem> iter = this.mSpeechItemLink.listIterator();
                            while (iter.hasNext()) {
                                SpeechItem item = iter.next();
                                if (item.getPriority() != 4 && item.getCallerIdentity() != caller) {
                                    if (item instanceof UtteranceProgressDispatcher) {
                                        UtteranceProgressDispatcher dispatcher = (UtteranceProgressDispatcher) item;
                                        dispatcher.dispatchOnStop();
                                    }
                                    iter.remove();
                                }
                            }
                            SpeechItem current = removeCurrentItemBySolo();
                            this.mLinkLock.unlock();
                            if (current != null) {
                                try {
                                    Log.d(TextToSpeechService.TAG, "stop current item due to solo");
                                    current.stop();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            this.mLinkLock.lock();
                            ret = 0;
                        }
                    } else if (caller == this.mSoloCaller) {
                        if (!on) {
                            this.mSoloCaller = null;
                        }
                        ret = 0;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                return ret;
            } finally {
                this.mLinkLock.unlock();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public class SynthThread extends Thread {
            private boolean mFirstIdle = true;
            private volatile boolean mIsWorking;

            public SynthThread() {
                setName("SynthThread");
                this.mIsWorking = true;
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                while (this.mIsWorking) {
                    SpeechItem item = null;
                    try {
                        try {
                            SynthHandler.this.mLinkLock.lock();
                            if (SynthHandler.this.mSpeechItemLink.size() == 0) {
                                Log.d(TextToSpeechService.TAG, "List empty, wait for new item");
                                if (!this.mFirstIdle) {
                                    TextToSpeechService.this.mAudioFocusHelper.abandonAudioFocus();
                                    broadcastTtsQueueProcessingCompleted();
                                } else {
                                    this.mFirstIdle = false;
                                }
                                SynthHandler.this.mLinkCondition.await();
                            }
                            item = (SpeechItem) SynthHandler.this.mSpeechItemLink.pollFirst();
                        } catch (Exception e) {
                            Log.e(TextToSpeechService.TAG, "get item fail", e);
                        }
                        if (item != null) {
                            Log.d(TextToSpeechService.TAG, "got a new item priority " + item.getPriority());
                            SynthHandler.this.setCurrentSpeechItem(item);
                            Log.d(TextToSpeechService.TAG, "play item start");
                            try {
                                item.play();
                            } catch (Exception e2) {
                                Log.e(TextToSpeechService.TAG, "something wrong when playing");
                                e2.printStackTrace();
                            }
                            SynthHandler.this.removeCurrentSpeechItem();
                            Log.d(TextToSpeechService.TAG, "play item end");
                        }
                    } finally {
                        SynthHandler.this.mLinkLock.unlock();
                    }
                }
                TextToSpeechService.this.mAudioFocusHelper.abandonAudioFocus();
            }

            public void quit() {
                this.mIsWorking = false;
                SynthHandler.this.mLinkLock.lock();
                try {
                    SynthHandler.this.mLinkCondition.signal();
                } finally {
                    SynthHandler.this.mLinkLock.unlock();
                }
            }

            private void broadcastTtsQueueProcessingCompleted() {
                Intent i = new Intent(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
                TextToSpeechService.this.sendBroadcast(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class AudioOutputParams {
        public final AudioAttributes mAudioAttributes;
        public final float mPan;
        public final int mSessionId;
        public final float mVolume;

        synchronized AudioOutputParams() {
            this.mSessionId = 0;
            this.mVolume = 1.0f;
            this.mPan = 0.0f;
            this.mAudioAttributes = null;
        }

        synchronized AudioOutputParams(int sessionId, float volume, float pan, AudioAttributes audioAttributes) {
            this.mSessionId = sessionId;
            this.mVolume = volume;
            this.mPan = pan;
            this.mAudioAttributes = audioAttributes;
        }

        static synchronized AudioOutputParams createFromParamsBundle(Bundle paramsBundle, boolean isSpeech) {
            int i;
            if (paramsBundle == null) {
                return new AudioOutputParams();
            }
            AudioAttributes audioAttributes = (AudioAttributes) paramsBundle.getParcelable(TextToSpeech.Engine.KEY_PARAM_AUDIO_ATTRIBUTES);
            if (audioAttributes == null) {
                int streamType = paramsBundle.getInt(TextToSpeech.Engine.KEY_PARAM_STREAM, 10);
                AudioAttributes.Builder legacyStreamType = new AudioAttributes.Builder().setLegacyStreamType(streamType);
                if (isSpeech) {
                    i = 1;
                } else {
                    i = 4;
                }
                audioAttributes = legacyStreamType.setContentType(i).build();
            }
            return new AudioOutputParams(paramsBundle.getInt(TextToSpeech.Engine.KEY_PARAM_SESSION_ID, 0), paramsBundle.getFloat("volume", 1.0f), paramsBundle.getFloat(TextToSpeech.Engine.KEY_PARAM_PAN, 0.0f), audioAttributes);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public abstract class SpeechItem {
        private final Object mCallerIdentity;
        private final int mCallerPid;
        private final int mCallerUid;
        private final int mPriority;
        private volatile boolean mStarted;
        private volatile boolean mStopped;

        public abstract synchronized boolean isValid();

        protected abstract synchronized void playImpl();

        protected abstract synchronized void stopImpl();

        public SpeechItem(Object caller, int callerUid, int callerPid) {
            this.mStarted = false;
            this.mStopped = false;
            this.mCallerIdentity = caller;
            this.mCallerUid = callerUid;
            this.mCallerPid = callerPid;
            this.mPriority = 1;
        }

        public SpeechItem(Object caller, int callerUid, int callerPid, int priority) {
            this.mStarted = false;
            this.mStopped = false;
            this.mCallerIdentity = caller;
            this.mCallerUid = callerUid;
            this.mCallerPid = callerPid;
            this.mPriority = priority;
        }

        public synchronized Object getCallerIdentity() {
            return this.mCallerIdentity;
        }

        public synchronized int getCallerUid() {
            return this.mCallerUid;
        }

        public synchronized int getCallerPid() {
            return this.mCallerPid;
        }

        public int getPriority() {
            return this.mPriority;
        }

        public synchronized void play() {
            if (this.mStarted) {
                throw new IllegalStateException("play() called twice");
            }
            this.mStarted = true;
            playImpl();
        }

        public synchronized void stop() {
            if (this.mStopped) {
                throw new IllegalStateException("stop() called twice");
            }
            this.mStopped = true;
            stopImpl();
        }

        protected synchronized boolean isStopped() {
            return this.mStopped;
        }

        protected synchronized boolean isStarted() {
            return this.mStarted;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public abstract class UtteranceSpeechItem extends SpeechItem implements UtteranceProgressDispatcher {
        public final Condition mItemCondition;
        public final Lock mItemLock;

        public abstract synchronized String getUtteranceId();

        public UtteranceSpeechItem(Object caller, int callerUid, int callerPid) {
            super(caller, callerUid, callerPid);
            this.mItemLock = new ReentrantLock(true);
            this.mItemCondition = this.mItemLock.newCondition();
        }

        public UtteranceSpeechItem(Object caller, int callerUid, int callerPid, int priority) {
            super(caller, callerUid, callerPid, priority);
            this.mItemLock = new ReentrantLock(true);
            this.mItemCondition = this.mItemLock.newCondition();
        }

        @Override // android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher
        public synchronized void dispatchOnSuccess() {
            String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                TextToSpeechService.this.mCallbacks.dispatchOnSuccess(getCallerIdentity(), utteranceId);
            }
            if (TextToSpeechService.this.isUseXpNewStruct()) {
                this.mItemLock.lock();
                try {
                    this.mItemCondition.signal();
                } finally {
                    this.mItemLock.unlock();
                }
            }
        }

        @Override // android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher
        public synchronized void dispatchOnStop() {
            String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                TextToSpeechService.this.mCallbacks.dispatchOnStop(getCallerIdentity(), utteranceId, isStarted());
            }
            if (TextToSpeechService.this.isUseXpNewStruct()) {
                this.mItemLock.lock();
                try {
                    this.mItemCondition.signal();
                } finally {
                    this.mItemLock.unlock();
                }
            }
        }

        @Override // android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher
        public synchronized void dispatchOnStart() {
            String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                TextToSpeechService.this.mCallbacks.dispatchOnStart(getCallerIdentity(), utteranceId, getPriority());
            }
        }

        @Override // android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher
        public synchronized void dispatchOnError(int errorCode) {
            String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                TextToSpeechService.this.mCallbacks.dispatchOnError(getCallerIdentity(), utteranceId, errorCode);
            }
            if (TextToSpeechService.this.isUseXpNewStruct()) {
                this.mItemLock.lock();
                try {
                    this.mItemCondition.signal();
                } finally {
                    this.mItemLock.unlock();
                }
            }
        }

        @Override // android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher
        public synchronized void dispatchOnBeginSynthesis(int sampleRateInHz, int audioFormat, int channelCount) {
            String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                TextToSpeechService.this.mCallbacks.dispatchOnBeginSynthesis(getCallerIdentity(), utteranceId, sampleRateInHz, audioFormat, channelCount);
            }
        }

        @Override // android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher
        public synchronized void dispatchOnAudioAvailable(byte[] audio) {
            String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                TextToSpeechService.this.mCallbacks.dispatchOnAudioAvailable(getCallerIdentity(), utteranceId, audio);
            }
        }

        @Override // android.speech.tts.TextToSpeechService.UtteranceProgressDispatcher
        public synchronized void dispatchOnRangeStart(int start, int end, int frame) {
            String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                TextToSpeechService.this.mCallbacks.dispatchOnRangeStart(getCallerIdentity(), utteranceId, start, end, frame);
            }
        }

        synchronized String getStringParam(Bundle params, String key, String defaultValue) {
            return params == null ? defaultValue : params.getString(key, defaultValue);
        }

        synchronized int getIntParam(Bundle params, String key, int defaultValue) {
            return params == null ? defaultValue : params.getInt(key, defaultValue);
        }

        synchronized float getFloatParam(Bundle params, String key, float defaultValue) {
            return params == null ? defaultValue : params.getFloat(key, defaultValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public abstract class UtteranceSpeechItemWithParams extends UtteranceSpeechItem {
        protected final long mDeadline;
        protected final Bundle mParams;
        protected final String mUtteranceId;

        UtteranceSpeechItemWithParams(TextToSpeechService textToSpeechService, Object callerIdentity, int callerUid, int callerPid, Bundle params, String utteranceId) {
            this(callerIdentity, callerUid, callerPid, 1, params, utteranceId);
        }

        UtteranceSpeechItemWithParams(Object callerIdentity, int callerUid, int callerPid, int priority, Bundle params, String utteranceId) {
            super(callerIdentity, callerUid, callerPid, priority);
            this.mParams = params;
            this.mUtteranceId = utteranceId;
            long delayTolerance = getIntParam(this.mParams, TextToSpeech.Engine.KEY_PARAM_DELAY_TOLERANCE, 0);
            if (delayTolerance != 0) {
                this.mDeadline = SystemClock.elapsedRealtime() + delayTolerance;
            } else {
                this.mDeadline = 0L;
            }
        }

        synchronized boolean hasLanguage() {
            return !TextUtils.isEmpty(getStringParam(this.mParams, "language", null));
        }

        synchronized int getSpeechRate() {
            return getIntParam(this.mParams, TextToSpeech.Engine.KEY_PARAM_RATE, TextToSpeechService.this.getDefaultSpeechRate());
        }

        synchronized int getPitch() {
            return getIntParam(this.mParams, TextToSpeech.Engine.KEY_PARAM_PITCH, TextToSpeechService.this.getDefaultPitch());
        }

        @Override // android.speech.tts.TextToSpeechService.UtteranceSpeechItem
        public synchronized String getUtteranceId() {
            return this.mUtteranceId;
        }

        synchronized AudioOutputParams getAudioParams() {
            return AudioOutputParams.createFromParamsBundle(this.mParams, true);
        }

        long getDeadline() {
            return this.mDeadline;
        }
    }

    /* loaded from: classes2.dex */
    class SynthesisSpeechItem extends UtteranceSpeechItemWithParams {
        private final int mCallerUid;
        private final String[] mDefaultLocale;
        private final EventLogger mEventLogger;
        private AbstractSynthesisCallback mSynthesisCallback;
        private final SynthesisRequest mSynthesisRequest;
        private final CharSequence mText;

        public SynthesisSpeechItem(Object callerIdentity, int callerUid, int callerPid, Bundle params, String utteranceId, CharSequence text) {
            super(TextToSpeechService.this, callerIdentity, callerUid, callerPid, params, utteranceId);
            this.mText = text;
            this.mCallerUid = callerUid;
            this.mSynthesisRequest = new SynthesisRequest(this.mText, this.mParams);
            this.mDefaultLocale = TextToSpeechService.this.getSettingsLocale();
            setRequestParams(this.mSynthesisRequest);
            this.mEventLogger = new EventLogger(this.mSynthesisRequest, callerUid, callerPid, TextToSpeechService.this.mPackageName);
        }

        public SynthesisSpeechItem(Object callerIdentity, int callerUid, int callerPid, int priority, Bundle params, String utteranceId, CharSequence text) {
            super(callerIdentity, callerUid, callerPid, priority, params, utteranceId);
            this.mText = text;
            this.mCallerUid = callerUid;
            this.mSynthesisRequest = new SynthesisRequest(this.mText, this.mParams);
            this.mDefaultLocale = TextToSpeechService.this.getSettingsLocale();
            setRequestParams(this.mSynthesisRequest);
            this.mEventLogger = new EventLogger(this.mSynthesisRequest, callerUid, callerPid, TextToSpeechService.this.mPackageName);
        }

        public synchronized CharSequence getText() {
            return this.mText;
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        public synchronized boolean isValid() {
            if (this.mText == null) {
                Log.e(TextToSpeechService.TAG, "null synthesis text");
                return false;
            } else if (this.mText.length() >= TextToSpeech.getMaxSpeechInputLength()) {
                Log.w(TextToSpeechService.TAG, "Text too long: " + this.mText.length() + " chars");
                return false;
            } else if (getPriority() < 1 || getPriority() > 4) {
                Log.e(TextToSpeechService.TAG, "priority out of range");
                return false;
            } else {
                String txt = this.mText.toString();
                if (txt.replaceAll("[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "").replaceAll("\\s*", "").trim().equals("")) {
                    Log.e(TextToSpeechService.TAG, "text have no meaning");
                    return false;
                }
                return true;
            }
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        protected synchronized void playImpl() {
            long deadline = getDeadline();
            if (deadline != 0) {
                long currentTime = SystemClock.elapsedRealtime();
                if (currentTime > deadline) {
                    dispatchOnError(-11);
                    return;
                }
            }
            if (TextToSpeechService.this.isUseXpNewStruct()) {
                playImplXpNewStruct();
            } else {
                playImplOriginal();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:32:0x00b1, code lost:
            r11.this$0.mAudioManager.setVoicePosition(0, 0);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void playImplXpNewStruct() {
            /*
                Method dump skipped, instructions count: 263
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.speech.tts.TextToSpeechService.SynthesisSpeechItem.playImplXpNewStruct():void");
        }

        private void playImplOriginal() {
            this.mEventLogger.onRequestProcessingStart();
            synchronized (this) {
                if (isStopped()) {
                    return;
                }
                if (TextToSpeechService.this.isUseServicePlayback()) {
                    Log.d(TextToSpeechService.TAG, "use service playback");
                    this.mSynthesisCallback = createSynthesisCallback();
                } else {
                    Log.d(TextToSpeechService.TAG, "use engine playback");
                    this.mSynthesisCallback = new XpSynthesisCallback(this, false);
                }
                AbstractSynthesisCallback synthesisCallback = this.mSynthesisCallback;
                Bundle params = this.mSynthesisRequest.getParams();
                int focusMode = params.getInt(TextToSpeech.Engine.KEY_PARAM_AUDIOFOCUS, 1);
                int streamType = params.getInt(TextToSpeech.Engine.KEY_PARAM_STREAM, 10);
                boolean isFocusGain = true;
                if (focusMode == 1) {
                    isFocusGain = TextToSpeechService.this.mAudioFocusHelper.requestAudioFocus(streamType);
                }
                int voicePositionMode = params.getInt(TextToSpeech.Engine.KEY_PARAM_VOICE_POSITION, 1);
                boolean isNeedSetPosition = false;
                if (streamType == 10 && voicePositionMode == 1) {
                    isNeedSetPosition = true;
                }
                if (isFocusGain || getPriority() == 4) {
                    if (isNeedSetPosition) {
                        TextToSpeechService.this.mAudioManager.setVoicePosition(0, 0);
                    }
                    TextToSpeechService.this.onSynthesizeText(this.mSynthesisRequest, synthesisCallback);
                    if (isNeedSetPosition) {
                        TextToSpeechService.this.mAudioManager.setVoicePosition(-1, 0);
                    }
                    if (TextToSpeechService.this.isUseServicePlayback() && synthesisCallback.hasStarted() && !synthesisCallback.hasFinished()) {
                        synthesisCallback.done();
                        return;
                    }
                    return;
                }
                Log.w(TextToSpeechService.TAG, "speak fail: request audio focus fail");
                dispatchOnError(-4);
            }
        }

        protected synchronized AbstractSynthesisCallback createSynthesisCallback() {
            return new PlaybackSynthesisCallback(getAudioParams(), TextToSpeechService.this.mAudioPlaybackHandler, this, getCallerIdentity(), this.mEventLogger, false);
        }

        private synchronized void setRequestParams(SynthesisRequest request) {
            String voiceName = getVoiceName();
            request.setLanguage(getLanguage(), getCountry(), getVariant());
            if (!TextUtils.isEmpty(voiceName)) {
                request.setVoiceName(getVoiceName());
            }
            request.setSpeechRate(getSpeechRate());
            request.setCallerUid(this.mCallerUid);
            request.setPitch(getPitch());
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        protected synchronized void stopImpl() {
            if (TextToSpeechService.this.isUseXpNewStruct()) {
                stopImplXpNewStruct();
            } else {
                stopImplOriginal();
            }
        }

        private void stopImplXpNewStruct() {
            this.mItemLock.lock();
            try {
                AbstractSynthesisCallback synthesisCallback = this.mSynthesisCallback;
                if (synthesisCallback != null) {
                    synthesisCallback.stop();
                    TextToSpeechService.this.onStop();
                } else {
                    dispatchOnStop();
                }
            } catch (Exception e) {
                Log.e(TextToSpeechService.TAG, "stopImpl exception", e);
            } finally {
                this.mItemLock.unlock();
            }
        }

        private void stopImplOriginal() {
            AbstractSynthesisCallback synthesisCallback;
            synchronized (this) {
                synthesisCallback = this.mSynthesisCallback;
            }
            if (synthesisCallback != null) {
                synthesisCallback.stop();
                TextToSpeechService.this.onStop();
                return;
            }
            dispatchOnStop();
        }

        private synchronized String getCountry() {
            return !hasLanguage() ? this.mDefaultLocale[1] : getStringParam(this.mParams, TextToSpeech.Engine.KEY_PARAM_COUNTRY, "");
        }

        private synchronized String getVariant() {
            return !hasLanguage() ? this.mDefaultLocale[2] : getStringParam(this.mParams, TextToSpeech.Engine.KEY_PARAM_VARIANT, "");
        }

        public synchronized String getLanguage() {
            return getStringParam(this.mParams, "language", this.mDefaultLocale[0]);
        }

        public synchronized String getVoiceName() {
            return getStringParam(this.mParams, TextToSpeech.Engine.KEY_PARAM_VOICE_NAME, "");
        }
    }

    /* loaded from: classes2.dex */
    private class SynthesisToFileOutputStreamSpeechItem extends SynthesisSpeechItem {
        private final FileOutputStream mFileOutputStream;

        public SynthesisToFileOutputStreamSpeechItem(Object callerIdentity, int callerUid, int callerPid, Bundle params, String utteranceId, CharSequence text, FileOutputStream fileOutputStream) {
            super(callerIdentity, callerUid, callerPid, params, utteranceId, text);
            this.mFileOutputStream = fileOutputStream;
        }

        @Override // android.speech.tts.TextToSpeechService.SynthesisSpeechItem
        protected synchronized AbstractSynthesisCallback createSynthesisCallback() {
            return new FileSynthesisCallback(this.mFileOutputStream.getChannel(), this, false);
        }

        @Override // android.speech.tts.TextToSpeechService.SynthesisSpeechItem, android.speech.tts.TextToSpeechService.SpeechItem
        protected synchronized void playImpl() {
            dispatchOnStart();
            super.playImpl();
            try {
                this.mFileOutputStream.close();
            } catch (IOException e) {
                Log.w(TextToSpeechService.TAG, "Failed to close output file", e);
            }
        }
    }

    /* loaded from: classes2.dex */
    private class AudioSpeechItem extends UtteranceSpeechItemWithParams {
        private final AudioPlaybackQueueItem mItem;

        public AudioSpeechItem(Object callerIdentity, int callerUid, int callerPid, Bundle params, String utteranceId, Uri uri) {
            super(TextToSpeechService.this, callerIdentity, callerUid, callerPid, params, utteranceId);
            this.mItem = new AudioPlaybackQueueItem(this, getCallerIdentity(), TextToSpeechService.this, uri, getAudioParams());
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        public synchronized boolean isValid() {
            return true;
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        protected synchronized void playImpl() {
            TextToSpeechService.this.mAudioPlaybackHandler.enqueue(this.mItem);
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        protected synchronized void stopImpl() {
        }

        @Override // android.speech.tts.TextToSpeechService.UtteranceSpeechItemWithParams, android.speech.tts.TextToSpeechService.UtteranceSpeechItem
        public synchronized String getUtteranceId() {
            return getStringParam(this.mParams, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, null);
        }

        @Override // android.speech.tts.TextToSpeechService.UtteranceSpeechItemWithParams
        synchronized AudioOutputParams getAudioParams() {
            return AudioOutputParams.createFromParamsBundle(this.mParams, false);
        }
    }

    /* loaded from: classes2.dex */
    private class SilenceSpeechItem extends UtteranceSpeechItem {
        private final long mDuration;
        private final String mUtteranceId;

        public SilenceSpeechItem(Object callerIdentity, int callerUid, int callerPid, String utteranceId, long duration) {
            super(callerIdentity, callerUid, callerPid);
            this.mUtteranceId = utteranceId;
            this.mDuration = duration;
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        public synchronized boolean isValid() {
            return true;
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        protected synchronized void playImpl() {
            TextToSpeechService.this.mAudioPlaybackHandler.enqueue(new SilencePlaybackQueueItem(this, getCallerIdentity(), this.mDuration));
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        protected synchronized void stopImpl() {
        }

        @Override // android.speech.tts.TextToSpeechService.UtteranceSpeechItem
        public synchronized String getUtteranceId() {
            return this.mUtteranceId;
        }
    }

    /* loaded from: classes2.dex */
    private class LoadLanguageItem extends SpeechItem {
        private final String mCountry;
        private final String mLanguage;
        private final String mVariant;

        public LoadLanguageItem(Object callerIdentity, int callerUid, int callerPid, String language, String country, String variant) {
            super(callerIdentity, callerUid, callerPid);
            this.mLanguage = language;
            this.mCountry = country;
            this.mVariant = variant;
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        public synchronized boolean isValid() {
            return true;
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        protected synchronized void playImpl() {
            TextToSpeechService.this.onLoadLanguage(this.mLanguage, this.mCountry, this.mVariant);
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        protected synchronized void stopImpl() {
        }
    }

    /* loaded from: classes2.dex */
    private class LoadVoiceItem extends SpeechItem {
        private final String mVoiceName;

        public LoadVoiceItem(Object callerIdentity, int callerUid, int callerPid, String voiceName) {
            super(callerIdentity, callerUid, callerPid);
            this.mVoiceName = voiceName;
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        public synchronized boolean isValid() {
            return true;
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        protected synchronized void playImpl() {
            TextToSpeechService.this.onLoadVoice(this.mVoiceName);
        }

        @Override // android.speech.tts.TextToSpeechService.SpeechItem
        protected synchronized void stopImpl() {
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (TextToSpeech.Engine.INTENT_ACTION_TTS_SERVICE.equals(intent.getAction())) {
            return this.mBinder;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getQueueModeName(int queueMode) {
        if (queueMode == 1) {
            return "QUEUE_ADD";
        }
        if (queueMode == 0) {
            return "QUEUE_FLUSH";
        }
        if (queueMode == 2) {
            return "QUEUE_DESTROY";
        }
        return "QUEUE_UNKNOWN";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPriorityName(int priority) {
        if (priority == 1) {
            return "PRIORITY_NORMAL";
        }
        if (priority == 2) {
            return "PRIORITY_IMPORTANT";
        }
        if (priority == 3) {
            return "PRIORITY_URGENT";
        }
        if (priority == 4) {
            return "PRIORITY_INSTANT";
        }
        return "PRIORITY_UNKNOWN";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPackageNameByPid(int pid) {
        ActivityManager am = (ActivityManager) this.mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            if (pid == appProcess.pid) {
                String packageName = appProcess.processName;
                return packageName;
            }
        }
        return "unknown";
    }

    public boolean isUseServicePlayback() {
        return true;
    }

    public boolean isUseXpNewStruct() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class CallbackMap extends RemoteCallbackList<ITextToSpeechCallback> {
        private final HashMap<IBinder, ITextToSpeechCallback> mCallerToCallback;
        private final HashMap<IBinder, String> mCallerToName;
        private ArrayList<IBinder> mListenerOnStateChanged;

        private CallbackMap() {
            this.mCallerToCallback = new HashMap<>();
            this.mCallerToName = new HashMap<>();
            this.mListenerOnStateChanged = new ArrayList<>();
        }

        public void setCallback(IBinder caller, ITextToSpeechCallback cb, String name) {
            ITextToSpeechCallback old;
            synchronized (this.mCallerToCallback) {
                try {
                    if (cb != null) {
                        register(cb, caller);
                        old = this.mCallerToCallback.put(caller, cb);
                        this.mCallerToName.put(caller, name);
                    } else {
                        old = this.mCallerToCallback.remove(caller);
                        this.mCallerToName.remove(caller);
                    }
                    if (old != null && old != cb) {
                        unregister(old);
                    }
                } finally {
                }
            }
            synchronized (this.mListenerOnStateChanged) {
                if (cb == null) {
                    try {
                        if (this.mListenerOnStateChanged.contains(caller)) {
                            this.mListenerOnStateChanged.remove(caller);
                        }
                    } finally {
                    }
                }
            }
        }

        public void setListenOnStateChanged(IBinder caller) {
            synchronized (this.mListenerOnStateChanged) {
                if (!this.mListenerOnStateChanged.contains(caller)) {
                    this.mListenerOnStateChanged.add(caller);
                }
            }
        }

        public void notifyOnStateChanged(IBinder caller, int state) {
            ITextToSpeechCallback cb;
            String name;
            synchronized (this.mListenerOnStateChanged) {
                Iterator<IBinder> it = this.mListenerOnStateChanged.iterator();
                while (it.hasNext()) {
                    IBinder listener = it.next();
                    synchronized (this.mCallerToCallback) {
                        cb = this.mCallerToCallback.get(listener);
                        name = this.mCallerToName.get(caller);
                    }
                    if (cb == null) {
                        break;
                    }
                    if (name == null) {
                        name = "";
                    }
                    try {
                        cb.onStateChanged(name, state);
                    } catch (RemoteException e) {
                        Log.e(TextToSpeechService.TAG, "Callback onStateChanged failed: " + e);
                    }
                }
            }
        }

        public synchronized void dispatchOnStop(Object callerIdentity, String utteranceId, boolean started) {
            if (started) {
                notifyOnStateChanged((IBinder) callerIdentity, 0);
            }
            ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null) {
                return;
            }
            try {
                cb.onStop(utteranceId, started);
            } catch (RemoteException e) {
                Log.e(TextToSpeechService.TAG, "Callback onStop failed: " + e);
            }
        }

        public synchronized void dispatchOnSuccess(Object callerIdentity, String utteranceId) {
            notifyOnStateChanged((IBinder) callerIdentity, 0);
            ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null) {
                return;
            }
            try {
                cb.onSuccess(utteranceId);
            } catch (RemoteException e) {
                Log.e(TextToSpeechService.TAG, "Callback onDone failed: " + e);
            }
        }

        public void dispatchOnStart(Object callerIdentity, String utteranceId, int priority) {
            notifyOnStateChanged((IBinder) callerIdentity, priority);
            ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null) {
                return;
            }
            try {
                cb.onStart(utteranceId);
            } catch (RemoteException e) {
                Log.e(TextToSpeechService.TAG, "Callback onStart failed: " + e);
            }
        }

        public synchronized void dispatchOnError(Object callerIdentity, String utteranceId, int errorCode) {
            notifyOnStateChanged((IBinder) callerIdentity, -1);
            ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null) {
                return;
            }
            try {
                cb.onError(utteranceId, errorCode);
            } catch (RemoteException e) {
                Log.e(TextToSpeechService.TAG, "Callback onError failed: " + e);
            }
        }

        public synchronized void dispatchOnBeginSynthesis(Object callerIdentity, String utteranceId, int sampleRateInHz, int audioFormat, int channelCount) {
            ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null) {
                return;
            }
            try {
                cb.onBeginSynthesis(utteranceId, sampleRateInHz, audioFormat, channelCount);
            } catch (RemoteException e) {
                Log.e(TextToSpeechService.TAG, "Callback dispatchOnBeginSynthesis(String, int, int, int) failed: " + e);
            }
        }

        public synchronized void dispatchOnAudioAvailable(Object callerIdentity, String utteranceId, byte[] buffer) {
            ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null) {
                return;
            }
            try {
                cb.onAudioAvailable(utteranceId, buffer);
            } catch (RemoteException e) {
                Log.e(TextToSpeechService.TAG, "Callback dispatchOnAudioAvailable(String, byte[]) failed: " + e);
            }
        }

        public synchronized void dispatchOnRangeStart(Object callerIdentity, String utteranceId, int start, int end, int frame) {
            ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb == null) {
                return;
            }
            try {
                cb.onRangeStart(utteranceId, start, end, frame);
            } catch (RemoteException e) {
                Log.e(TextToSpeechService.TAG, "Callback dispatchOnRangeStart(String, int, int, int) failed: " + e);
            }
        }

        @Override // android.os.RemoteCallbackList
        public synchronized void onCallbackDied(ITextToSpeechCallback callback, Object cookie) {
            IBinder caller = (IBinder) cookie;
            synchronized (this.mCallerToCallback) {
                this.mCallerToCallback.remove(caller);
            }
            synchronized (this.mListenerOnStateChanged) {
                if (this.mListenerOnStateChanged.contains(caller)) {
                    this.mListenerOnStateChanged.remove(caller);
                }
            }
            TextToSpeechService.this.mSynthHandler.setSoloMode(caller, false);
        }

        @Override // android.os.RemoteCallbackList
        public void kill() {
            synchronized (this.mCallerToCallback) {
                this.mCallerToCallback.clear();
                super.kill();
            }
        }

        private synchronized ITextToSpeechCallback getCallbackFor(Object caller) {
            ITextToSpeechCallback cb;
            IBinder asBinder = (IBinder) caller;
            synchronized (this.mCallerToCallback) {
                cb = this.mCallerToCallback.get(asBinder);
            }
            return cb;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
        private AudioManager mAudioManager;
        private volatile boolean mIsRequested = false;

        public AudioFocusHelper(Context ct) {
            this.mAudioManager = (AudioManager) ct.getSystemService("audio");
        }

        @Override // android.media.AudioManager.OnAudioFocusChangeListener
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case -3:
                case -2:
                case -1:
                    Log.d(TextToSpeechService.TAG, "audio focus loss " + focusChange);
                    return;
                case 0:
                default:
                    return;
                case 1:
                case 2:
                    Log.d(TextToSpeechService.TAG, "onAudioFocusChange audio focus gained " + focusChange);
                    return;
            }
        }

        public boolean requestAudioFocus(int streamType) {
            if (this.mIsRequested) {
                return true;
            }
            int ret = this.mAudioManager.requestAudioFocus(this, streamType, 3);
            if (ret == 0) {
                return false;
            }
            this.mIsRequested = true;
            return true;
        }

        public void abandonAudioFocus() {
            if (this.mIsRequested) {
                this.mIsRequested = false;
                this.mAudioManager.abandonAudioFocus(this);
            }
        }
    }

    /* loaded from: classes2.dex */
    private class VolDownOnNavi extends ContentObserver {
        public VolDownOnNavi(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            boolean isEnable = Settings.System.getInt(TextToSpeechService.this.mContext.getContentResolver(), TextToSpeechService.VOL_DOWN_ON_NAVI, 1) == 1;
            if (isEnable != TextToSpeechService.this.mIsVolDownOnNavi) {
                Log.i(TextToSpeechService.TAG, "volume down on navi tts " + isEnable);
                TextToSpeechService.this.mIsVolDownOnNavi = isEnable;
            }
        }
    }

    /* loaded from: classes2.dex */
    private class CallStateReceiver extends BroadcastReceiver {
        private CallStateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v(TextToSpeechService.TAG, "onReceive action " + action);
            if (!action.equals(BluetoothHeadsetClient.ACTION_CALL_CHANGED)) {
                if (action.equals(BluetoothHeadsetClient.ACTION_CONNECTION_STATE_CHANGED)) {
                    int newState = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
                    int oldState = intent.getIntExtra(BluetoothProfile.EXTRA_PREVIOUS_STATE, -1);
                    Log.i(TextToSpeechService.TAG, "onReceive bt connection state " + oldState + " -> " + newState);
                    if (newState == 0) {
                        TextToSpeechService.this.mIsCallStateActive = false;
                        return;
                    }
                    return;
                }
                return;
            }
            BluetoothHeadsetClientCall callState = (BluetoothHeadsetClientCall) intent.getExtra(BluetoothHeadsetClient.EXTRA_CALL, null);
            if (callState != null) {
                int state = callState.getState();
                Log.i(TextToSpeechService.TAG, "onReceive call state " + state);
                if (state == 0) {
                    TextToSpeechService.this.mIsCallStateActive = true;
                } else {
                    TextToSpeechService.this.mIsCallStateActive = false;
                }
                if (state != 0) {
                    switch (state) {
                        case 2:
                        case 3:
                        case 4:
                            break;
                        default:
                            return;
                    }
                }
                TextToSpeechService.this.mSynthHandler.post(new Runnable() { // from class: android.speech.tts.TextToSpeechService.CallStateReceiver.1
                    @Override // java.lang.Runnable
                    public void run() {
                        TextToSpeechService.this.mSynthHandler.stopAll();
                    }
                });
            }
        }
    }
}
