package android.speech.tts;

import android.speech.tts.TextToSpeechService;
import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class XpSynthesisCallback extends AbstractSynthesisCallback {
    private static final boolean DBG = true;
    private static final int MIN_AUDIO_BUFFER_SIZE = 8192;
    private static final String TAG = "XpSynthesisRequest";
    private final TextToSpeechService.UtteranceProgressDispatcher mDispatcher;
    private volatile boolean mDone;
    private volatile boolean mStarted;
    private final Object mStateLock;
    protected volatile int mStatusCode;

    /* JADX INFO: Access modifiers changed from: package-private */
    public XpSynthesisCallback(TextToSpeechService.UtteranceProgressDispatcher dispatcher, boolean clientIsUsingV2) {
        super(clientIsUsingV2);
        this.mStateLock = new Object();
        this.mStarted = false;
        this.mDone = false;
        this.mDispatcher = dispatcher;
        this.mStatusCode = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.speech.tts.AbstractSynthesisCallback
    public void stop() {
        Log.d(TAG, "stop()");
        synchronized (this.mStateLock) {
            if (this.mDone) {
                Log.w(TAG, "stop() already done");
            } else if (this.mStatusCode == -2) {
                Log.w(TAG, "stop() called twice");
            } else {
                this.mStatusCode = -2;
                this.mDispatcher.dispatchOnStop();
            }
        }
    }

    @Override // android.speech.tts.SynthesisCallback
    public int getMaxBufferSize() {
        return 8192;
    }

    @Override // android.speech.tts.SynthesisCallback
    public boolean hasStarted() {
        boolean z;
        synchronized (this.mStateLock) {
            z = this.mStarted;
        }
        return z;
    }

    @Override // android.speech.tts.SynthesisCallback
    public boolean hasFinished() {
        boolean z;
        synchronized (this.mStateLock) {
            z = this.mDone;
        }
        return z;
    }

    @Override // android.speech.tts.SynthesisCallback
    public int start(int sampleRateInHz, int audioFormat, int channelCount) {
        Log.d(TAG, "start(" + sampleRateInHz + "," + audioFormat + "," + channelCount + ")");
        if (audioFormat != 3 && audioFormat != 2 && audioFormat != 4) {
            Log.w(TAG, "Audio format encoding " + audioFormat + " not supported. Please use one of AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT or AudioFormat.ENCODING_PCM_FLOAT");
        }
        synchronized (this.mStateLock) {
            if (this.mStatusCode == -2) {
                Log.d(TAG, "stop() called before start(), returning.");
                return errorCodeOnStop();
            } else if (this.mStatusCode != 0) {
                Log.d(TAG, "Error was raised");
                return -1;
            } else if (this.mStarted) {
                Log.w(TAG, "Duplicate call to start()");
                return -1;
            } else {
                this.mStarted = true;
                this.mDispatcher.dispatchOnBeginSynthesis(sampleRateInHz, audioFormat, channelCount);
                this.mDispatcher.dispatchOnStart();
                return 0;
            }
        }
    }

    @Override // android.speech.tts.SynthesisCallback
    public int audioAvailable(byte[] buffer, int offset, int length) {
        Log.d(TAG, "audioAvailable(byte[" + buffer.length + "]," + offset + "," + length + ")");
        if (length > getMaxBufferSize() || length <= 0) {
            throw new IllegalArgumentException("buffer is too large or of zero length (" + length + " bytes)");
        }
        synchronized (this.mStateLock) {
            if (this.mStatusCode == -2) {
                Log.w(TAG, "already stop");
                return -1;
            }
            this.mDispatcher.dispatchOnAudioAvailable(buffer);
            return 0;
        }
    }

    @Override // android.speech.tts.SynthesisCallback
    public int done() {
        Log.d(TAG, "done()");
        synchronized (this.mStateLock) {
            if (this.mDone) {
                Log.w(TAG, "Duplicate call to done()");
                return -1;
            } else if (this.mStatusCode == -2) {
                Log.w(TAG, "already stop");
                return -1;
            } else {
                int statusCode = this.mStatusCode;
                this.mDone = true;
                if (statusCode == 0) {
                    this.mDispatcher.dispatchOnSuccess();
                    return 0;
                }
                this.mDispatcher.dispatchOnError(statusCode);
                return 0;
            }
        }
    }

    @Override // android.speech.tts.SynthesisCallback
    public void error() {
        error(-3);
    }

    @Override // android.speech.tts.SynthesisCallback
    public void error(int errorCode) {
        Log.d(TAG, "error " + errorCode);
        synchronized (this.mStateLock) {
            if (this.mDone) {
                return;
            }
            if (this.mStatusCode == -2) {
                Log.w(TAG, "already stop");
            } else {
                this.mStatusCode = errorCode;
            }
        }
    }

    @Override // android.speech.tts.SynthesisCallback
    public void rangeStart(int markerInFrames, int start, int end) {
    }
}
