package android.media.soundtrigger;

import android.annotation.SystemApi;
import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.SoundTrigger;
import android.media.AudioFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.app.ISoundTriggerService;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;
@SystemApi
/* loaded from: classes.dex */
public final class SoundTriggerDetector {
    private static final boolean DBG = false;
    private static final int MSG_AVAILABILITY_CHANGED = 1;
    private static final int MSG_DETECTION_ERROR = 3;
    private static final int MSG_DETECTION_PAUSE = 4;
    private static final int MSG_DETECTION_RESUME = 5;
    private static final int MSG_SOUND_TRIGGER_DETECTED = 2;
    public static final int RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS = 2;
    public static final int RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO = 1;
    public static final int RECOGNITION_FLAG_NONE = 0;
    private static final String TAG = "SoundTriggerDetector";
    private final Callback mCallback;
    private final Handler mHandler;
    private final Object mLock = new Object();
    private final RecognitionCallback mRecognitionCallback;
    private final UUID mSoundModelId;
    private final ISoundTriggerService mSoundTriggerService;

    /* loaded from: classes.dex */
    public static abstract class Callback {
        public abstract void onAvailabilityChanged(int i);

        public abstract void onDetected(EventPayload eventPayload);

        public abstract void onError();

        public abstract void onRecognitionPaused();

        public abstract void onRecognitionResumed();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface RecognitionFlags {
    }

    /* loaded from: classes.dex */
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

        private protected byte[] getData() {
            if (!this.mTriggerAvailable) {
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized SoundTriggerDetector(ISoundTriggerService soundTriggerService, UUID soundModelId, Callback callback, Handler handler) {
        this.mSoundTriggerService = soundTriggerService;
        this.mSoundModelId = soundModelId;
        this.mCallback = callback;
        if (handler == null) {
            this.mHandler = new MyHandler();
        } else {
            this.mHandler = new MyHandler(handler.getLooper());
        }
        this.mRecognitionCallback = new RecognitionCallback();
    }

    public boolean startRecognition(int recognitionFlags) {
        boolean captureTriggerAudio = (recognitionFlags & 1) != 0;
        boolean allowMultipleTriggers = (recognitionFlags & 2) != 0;
        try {
            int status = this.mSoundTriggerService.startRecognition(new ParcelUuid(this.mSoundModelId), this.mRecognitionCallback, new SoundTrigger.RecognitionConfig(captureTriggerAudio, allowMultipleTriggers, null, null));
            return status == 0;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean stopRecognition() {
        try {
            int status = this.mSoundTriggerService.stopRecognition(new ParcelUuid(this.mSoundModelId), this.mRecognitionCallback);
            return status == 0;
        } catch (RemoteException e) {
            return false;
        }
    }

    public synchronized void dump(String prefix, PrintWriter pw) {
        synchronized (this.mLock) {
        }
    }

    /* loaded from: classes.dex */
    private class RecognitionCallback extends IRecognitionStatusCallback.Stub {
        private RecognitionCallback() {
        }

        @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
        public synchronized void onGenericSoundTriggerDetected(SoundTrigger.GenericRecognitionEvent event) {
            Slog.d(SoundTriggerDetector.TAG, "onGenericSoundTriggerDetected()" + event);
            Message.obtain(SoundTriggerDetector.this.mHandler, 2, new EventPayload(event.triggerInData, event.captureAvailable, event.captureFormat, event.captureSession, event.data)).sendToTarget();
        }

        @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
        public synchronized void onKeyphraseDetected(SoundTrigger.KeyphraseRecognitionEvent event) {
            Slog.e(SoundTriggerDetector.TAG, "Ignoring onKeyphraseDetected() called for " + event);
        }

        @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
        public synchronized void onError(int status) {
            Slog.d(SoundTriggerDetector.TAG, "onError()" + status);
            SoundTriggerDetector.this.mHandler.sendEmptyMessage(3);
        }

        @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
        public synchronized void onRecognitionPaused() {
            Slog.d(SoundTriggerDetector.TAG, "onRecognitionPaused()");
            SoundTriggerDetector.this.mHandler.sendEmptyMessage(4);
        }

        @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
        public synchronized void onRecognitionResumed() {
            Slog.d(SoundTriggerDetector.TAG, "onRecognitionResumed()");
            SoundTriggerDetector.this.mHandler.sendEmptyMessage(5);
        }
    }

    /* loaded from: classes.dex */
    private class MyHandler extends Handler {
        MyHandler() {
        }

        MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (SoundTriggerDetector.this.mCallback != null) {
                switch (msg.what) {
                    case 2:
                        SoundTriggerDetector.this.mCallback.onDetected((EventPayload) msg.obj);
                        return;
                    case 3:
                        SoundTriggerDetector.this.mCallback.onError();
                        return;
                    case 4:
                        SoundTriggerDetector.this.mCallback.onRecognitionPaused();
                        return;
                    case 5:
                        SoundTriggerDetector.this.mCallback.onRecognitionResumed();
                        return;
                    default:
                        super.handleMessage(msg);
                        return;
                }
            }
            Slog.w(SoundTriggerDetector.TAG, "Received message: " + msg.what + " for NULL callback.");
        }
    }
}
