package android.service.voice;

import android.content.Intent;
import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.KeyphraseEnrollmentInfo;
import android.hardware.soundtrigger.KeyphraseMetadata;
import android.hardware.soundtrigger.SoundTrigger;
import android.media.AudioFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.app.IVoiceInteractionManagerService;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
/* loaded from: classes2.dex */
public class AlwaysOnHotwordDetector {
    static final boolean DBG = false;
    public static final int MANAGE_ACTION_ENROLL = 0;
    public static final int MANAGE_ACTION_RE_ENROLL = 1;
    public static final int MANAGE_ACTION_UN_ENROLL = 2;
    private static final int MSG_AVAILABILITY_CHANGED = 1;
    private static final int MSG_DETECTION_ERROR = 3;
    private static final int MSG_DETECTION_PAUSE = 4;
    private static final int MSG_DETECTION_RESUME = 5;
    private static final int MSG_HOTWORD_DETECTED = 2;
    public static final int RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS = 2;
    public static final int RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO = 1;
    public static final int RECOGNITION_FLAG_NONE = 0;
    public static final int RECOGNITION_MODE_USER_IDENTIFICATION = 2;
    public static final int RECOGNITION_MODE_VOICE_TRIGGER = 1;
    public static final int STATE_HARDWARE_UNAVAILABLE = -2;
    private static final int STATE_INVALID = -3;
    public static final int STATE_KEYPHRASE_ENROLLED = 2;
    public static final int STATE_KEYPHRASE_UNENROLLED = 1;
    public static final int STATE_KEYPHRASE_UNSUPPORTED = -1;
    private static final int STATE_NOT_READY = 0;
    private static final int STATUS_ERROR = Integer.MIN_VALUE;
    private static final int STATUS_OK = 0;
    static final String TAG = "AlwaysOnHotwordDetector";
    private final Callback mExternalCallback;
    private final KeyphraseEnrollmentInfo mKeyphraseEnrollmentInfo;
    private final KeyphraseMetadata mKeyphraseMetadata;
    private final Locale mLocale;
    private final IVoiceInteractionManagerService mModelManagementService;
    private final String mText;
    private final IVoiceInteractionService mVoiceInteractionService;
    private final Object mLock = new Object();
    private int mAvailability = 0;
    private final Handler mHandler = new MyHandler();
    private final SoundTriggerListener mInternalCallback = new SoundTriggerListener(this.mHandler);

    /* loaded from: classes2.dex */
    public static abstract class Callback {
        public abstract void onAvailabilityChanged(int i);

        public abstract void onDetected(EventPayload eventPayload);

        public abstract void onError();

        public abstract void onRecognitionPaused();

        public abstract void onRecognitionResumed();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    private @interface ManageActions {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface RecognitionFlags {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface RecognitionModes {
    }

    /* loaded from: classes2.dex */
    public static class EventPayload {
        private final AudioFormat mAudioFormat;
        private final boolean mCaptureAvailable;
        private final int mCaptureSession;
        private final byte[] mData;
        private final boolean mTriggerAvailable;

        private synchronized EventPayload(boolean triggerAvailable, boolean captureAvailable, AudioFormat audioFormat, int captureSession, byte[] data) {
            this.mTriggerAvailable = triggerAvailable;
            this.mCaptureAvailable = captureAvailable;
            this.mCaptureSession = captureSession;
            this.mAudioFormat = audioFormat;
            this.mData = data;
        }

        public AudioFormat getCaptureAudioFormat() {
            return this.mAudioFormat;
        }

        public byte[] getTriggerAudio() {
            if (this.mTriggerAvailable) {
                return this.mData;
            }
            return null;
        }

        private protected Integer getCaptureSession() {
            if (this.mCaptureAvailable) {
                return Integer.valueOf(this.mCaptureSession);
            }
            return null;
        }
    }

    public synchronized AlwaysOnHotwordDetector(String text, Locale locale, Callback callback, KeyphraseEnrollmentInfo keyphraseEnrollmentInfo, IVoiceInteractionService voiceInteractionService, IVoiceInteractionManagerService modelManagementService) {
        this.mText = text;
        this.mLocale = locale;
        this.mKeyphraseEnrollmentInfo = keyphraseEnrollmentInfo;
        this.mKeyphraseMetadata = this.mKeyphraseEnrollmentInfo.getKeyphraseMetadata(text, locale);
        this.mExternalCallback = callback;
        this.mVoiceInteractionService = voiceInteractionService;
        this.mModelManagementService = modelManagementService;
        new RefreshAvailabiltyTask().execute(new Void[0]);
    }

    public int getSupportedRecognitionModes() {
        int supportedRecognitionModesLocked;
        synchronized (this.mLock) {
            supportedRecognitionModesLocked = getSupportedRecognitionModesLocked();
        }
        return supportedRecognitionModesLocked;
    }

    private synchronized int getSupportedRecognitionModesLocked() {
        if (this.mAvailability == -3) {
            throw new IllegalStateException("getSupportedRecognitionModes called on an invalid detector");
        }
        if (this.mAvailability != 2 && this.mAvailability != 1) {
            throw new UnsupportedOperationException("Getting supported recognition modes for the keyphrase is not supported");
        }
        return this.mKeyphraseMetadata.recognitionModeFlags;
    }

    public boolean startRecognition(int recognitionFlags) {
        boolean z;
        synchronized (this.mLock) {
            if (this.mAvailability == -3) {
                throw new IllegalStateException("startRecognition called on an invalid detector");
            }
            if (this.mAvailability != 2) {
                throw new UnsupportedOperationException("Recognition for the given keyphrase is not supported");
            }
            z = startRecognitionLocked(recognitionFlags) == 0;
        }
        return z;
    }

    public boolean stopRecognition() {
        boolean z;
        synchronized (this.mLock) {
            if (this.mAvailability == -3) {
                throw new IllegalStateException("stopRecognition called on an invalid detector");
            }
            if (this.mAvailability != 2) {
                throw new UnsupportedOperationException("Recognition for the given keyphrase is not supported");
            }
            z = stopRecognitionLocked() == 0;
        }
        return z;
    }

    public Intent createEnrollIntent() {
        Intent manageIntentLocked;
        synchronized (this.mLock) {
            manageIntentLocked = getManageIntentLocked(0);
        }
        return manageIntentLocked;
    }

    public Intent createUnEnrollIntent() {
        Intent manageIntentLocked;
        synchronized (this.mLock) {
            manageIntentLocked = getManageIntentLocked(2);
        }
        return manageIntentLocked;
    }

    public Intent createReEnrollIntent() {
        Intent manageIntentLocked;
        synchronized (this.mLock) {
            manageIntentLocked = getManageIntentLocked(1);
        }
        return manageIntentLocked;
    }

    private synchronized Intent getManageIntentLocked(int action) {
        if (this.mAvailability == -3) {
            throw new IllegalStateException("getManageIntent called on an invalid detector");
        }
        if (this.mAvailability != 2 && this.mAvailability != 1) {
            throw new UnsupportedOperationException("Managing the given keyphrase is not supported");
        }
        return this.mKeyphraseEnrollmentInfo.getManageKeyphraseIntent(action, this.mText, this.mLocale);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void invalidate() {
        synchronized (this.mLock) {
            this.mAvailability = -3;
            notifyStateChangedLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onSoundModelsChanged() {
        synchronized (this.mLock) {
            if (this.mAvailability != -3 && this.mAvailability != -2 && this.mAvailability != -1) {
                stopRecognitionLocked();
                new RefreshAvailabiltyTask().execute(new Void[0]);
                return;
            }
            Slog.w(TAG, "Received onSoundModelsChanged for an unsupported keyphrase/config");
        }
    }

    private synchronized int startRecognitionLocked(int recognitionFlags) {
        SoundTrigger.KeyphraseRecognitionExtra[] recognitionExtra = {new SoundTrigger.KeyphraseRecognitionExtra(this.mKeyphraseMetadata.id, this.mKeyphraseMetadata.recognitionModeFlags, 0, new SoundTrigger.ConfidenceLevel[0])};
        boolean captureTriggerAudio = (recognitionFlags & 1) != 0;
        boolean allowMultipleTriggers = (recognitionFlags & 2) != 0;
        int code = Integer.MIN_VALUE;
        try {
            code = this.mModelManagementService.startRecognition(this.mVoiceInteractionService, this.mKeyphraseMetadata.id, this.mLocale.toLanguageTag(), this.mInternalCallback, new SoundTrigger.RecognitionConfig(captureTriggerAudio, allowMultipleTriggers, recognitionExtra, null));
        } catch (RemoteException e) {
            Slog.w(TAG, "RemoteException in startRecognition!", e);
        }
        if (code != 0) {
            Slog.w(TAG, "startRecognition() failed with error code " + code);
        }
        return code;
    }

    private synchronized int stopRecognitionLocked() {
        int code = Integer.MIN_VALUE;
        try {
            code = this.mModelManagementService.stopRecognition(this.mVoiceInteractionService, this.mKeyphraseMetadata.id, this.mInternalCallback);
        } catch (RemoteException e) {
            Slog.w(TAG, "RemoteException in stopRecognition!", e);
        }
        if (code != 0) {
            Slog.w(TAG, "stopRecognition() failed with error code " + code);
        }
        return code;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void notifyStateChangedLocked() {
        Message message = Message.obtain(this.mHandler, 1);
        message.arg1 = this.mAvailability;
        message.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class SoundTriggerListener extends IRecognitionStatusCallback.Stub {
        private final Handler mHandler;

        public synchronized SoundTriggerListener(Handler handler) {
            this.mHandler = handler;
        }

        @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
        public synchronized void onKeyphraseDetected(SoundTrigger.KeyphraseRecognitionEvent event) {
            Slog.i(AlwaysOnHotwordDetector.TAG, "onDetected");
            Message.obtain(this.mHandler, 2, new EventPayload(event.triggerInData, event.captureAvailable, event.captureFormat, event.captureSession, event.data)).sendToTarget();
        }

        @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
        public synchronized void onGenericSoundTriggerDetected(SoundTrigger.GenericRecognitionEvent event) {
            Slog.w(AlwaysOnHotwordDetector.TAG, "Generic sound trigger event detected at AOHD: " + event);
        }

        @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
        public synchronized void onError(int status) {
            Slog.i(AlwaysOnHotwordDetector.TAG, "onError: " + status);
            this.mHandler.sendEmptyMessage(3);
        }

        @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
        public synchronized void onRecognitionPaused() {
            Slog.i(AlwaysOnHotwordDetector.TAG, "onRecognitionPaused");
            this.mHandler.sendEmptyMessage(4);
        }

        @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
        public synchronized void onRecognitionResumed() {
            Slog.i(AlwaysOnHotwordDetector.TAG, "onRecognitionResumed");
            this.mHandler.sendEmptyMessage(5);
        }
    }

    /* loaded from: classes2.dex */
    class MyHandler extends Handler {
        MyHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            synchronized (AlwaysOnHotwordDetector.this.mLock) {
                if (AlwaysOnHotwordDetector.this.mAvailability != -3) {
                    switch (msg.what) {
                        case 1:
                            AlwaysOnHotwordDetector.this.mExternalCallback.onAvailabilityChanged(msg.arg1);
                            return;
                        case 2:
                            AlwaysOnHotwordDetector.this.mExternalCallback.onDetected((EventPayload) msg.obj);
                            return;
                        case 3:
                            AlwaysOnHotwordDetector.this.mExternalCallback.onError();
                            return;
                        case 4:
                            AlwaysOnHotwordDetector.this.mExternalCallback.onRecognitionPaused();
                            return;
                        case 5:
                            AlwaysOnHotwordDetector.this.mExternalCallback.onRecognitionResumed();
                            return;
                        default:
                            super.handleMessage(msg);
                            return;
                    }
                }
                Slog.w(AlwaysOnHotwordDetector.TAG, "Received message: " + msg.what + " for an invalid detector");
            }
        }
    }

    /* loaded from: classes2.dex */
    class RefreshAvailabiltyTask extends AsyncTask<Void, Void, Void> {
        RefreshAvailabiltyTask() {
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            int availability = internalGetInitialAvailability();
            if (availability == 0 || availability == 1 || availability == 2) {
                boolean enrolled = internalGetIsEnrolled(AlwaysOnHotwordDetector.this.mKeyphraseMetadata.id, AlwaysOnHotwordDetector.this.mLocale);
                if (!enrolled) {
                    availability = 1;
                } else {
                    availability = 2;
                }
            }
            synchronized (AlwaysOnHotwordDetector.this.mLock) {
                AlwaysOnHotwordDetector.this.mAvailability = availability;
                AlwaysOnHotwordDetector.this.notifyStateChangedLocked();
            }
            return null;
        }

        private synchronized int internalGetInitialAvailability() {
            synchronized (AlwaysOnHotwordDetector.this.mLock) {
                if (AlwaysOnHotwordDetector.this.mAvailability == -3) {
                    return -3;
                }
                SoundTrigger.ModuleProperties dspModuleProperties = null;
                try {
                    dspModuleProperties = AlwaysOnHotwordDetector.this.mModelManagementService.getDspModuleProperties(AlwaysOnHotwordDetector.this.mVoiceInteractionService);
                } catch (RemoteException e) {
                    Slog.w(AlwaysOnHotwordDetector.TAG, "RemoteException in getDspProperties!", e);
                }
                if (dspModuleProperties != null) {
                    if (AlwaysOnHotwordDetector.this.mKeyphraseMetadata == null) {
                        return -1;
                    }
                    return 0;
                }
                return -2;
            }
        }

        private synchronized boolean internalGetIsEnrolled(int keyphraseId, Locale locale) {
            try {
                return AlwaysOnHotwordDetector.this.mModelManagementService.isEnrolledForKeyphrase(AlwaysOnHotwordDetector.this.mVoiceInteractionService, keyphraseId, locale.toLanguageTag());
            } catch (RemoteException e) {
                Slog.w(AlwaysOnHotwordDetector.TAG, "RemoteException in listRegisteredKeyphraseSoundModels!", e);
                return false;
            }
        }
    }

    public synchronized void dump(String prefix, PrintWriter pw) {
        synchronized (this.mLock) {
            pw.print(prefix);
            pw.print("Text=");
            pw.println(this.mText);
            pw.print(prefix);
            pw.print("Locale=");
            pw.println(this.mLocale);
            pw.print(prefix);
            pw.print("Availability=");
            pw.println(this.mAvailability);
            pw.print(prefix);
            pw.print("KeyphraseMetadata=");
            pw.println(this.mKeyphraseMetadata);
            pw.print(prefix);
            pw.print("EnrollmentInfo=");
            pw.println(this.mKeyphraseEnrollmentInfo);
        }
    }
}
