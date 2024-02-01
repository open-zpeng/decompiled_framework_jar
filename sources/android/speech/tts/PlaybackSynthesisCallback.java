package android.speech.tts;

import android.speech.tts.TextToSpeechService;
import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class PlaybackSynthesisCallback extends AbstractSynthesisCallback {
    private static final boolean DBG = true;
    private static final int MIN_AUDIO_BUFFER_SIZE = 8192;
    private static final String TAG = "PlaybackSynthesisRequest";
    private final TextToSpeechService.AudioOutputParams mAudioParams;
    private final AudioPlaybackHandler mAudioTrackHandler;
    private final Object mCallerIdentity;
    private final TextToSpeechService.UtteranceProgressDispatcher mDispatcher;
    private volatile boolean mDone;
    private SynthesisPlaybackQueueItem mItem;
    private final AbstractEventLogger mLogger;
    private final Object mStateLock;
    protected volatile int mStatusCode;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PlaybackSynthesisCallback(TextToSpeechService.AudioOutputParams audioParams, AudioPlaybackHandler audioTrackHandler, TextToSpeechService.UtteranceProgressDispatcher dispatcher, Object callerIdentity, AbstractEventLogger logger, boolean clientIsUsingV2) {
        super(clientIsUsingV2);
        this.mStateLock = new Object();
        this.mItem = null;
        this.mDone = false;
        this.mAudioParams = audioParams;
        this.mAudioTrackHandler = audioTrackHandler;
        this.mDispatcher = dispatcher;
        this.mCallerIdentity = callerIdentity;
        this.mLogger = logger;
        this.mStatusCode = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.speech.tts.AbstractSynthesisCallback
    public void stop() {
        Log.d(TAG, "stop()");
        synchronized (this.mStateLock) {
            if (this.mDone) {
                return;
            }
            if (this.mStatusCode == -2) {
                Log.w(TAG, "stop() called twice");
                return;
            }
            SynthesisPlaybackQueueItem item = this.mItem;
            this.mStatusCode = -2;
            if (item != null) {
                item.stop(-2);
                return;
            }
            this.mLogger.onCompleted(-2);
            this.mDispatcher.dispatchOnStop();
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
            z = this.mItem != null;
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
        this.mDispatcher.dispatchOnBeginSynthesis(sampleRateInHz, audioFormat, channelCount);
        int channelConfig = BlockingAudioTrack.getChannelConfig(channelCount);
        synchronized (this.mStateLock) {
            try {
                if (channelConfig == 0) {
                    Log.e(TAG, "Unsupported number of channels :" + channelCount);
                    this.mStatusCode = -5;
                    return -1;
                } else if (this.mStatusCode == -2) {
                    Log.d(TAG, "stop() called before start(), returning.");
                    return errorCodeOnStop();
                } else if (this.mStatusCode != 0) {
                    Log.d(TAG, "Error was raised " + this.mStatusCode);
                    return -1;
                } else if (this.mItem != null) {
                    Log.e(TAG, "Start called twice");
                    return -1;
                } else {
                    SynthesisPlaybackQueueItem item = new SynthesisPlaybackQueueItem(this.mAudioParams, sampleRateInHz, audioFormat, channelCount, this.mDispatcher, this.mCallerIdentity, this.mLogger);
                    this.mAudioTrackHandler.enqueue(item);
                    this.mItem = item;
                    return 0;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // android.speech.tts.SynthesisCallback
    public int audioAvailable(byte[] buffer, int offset, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("buffer is too large or of zero length (" + length + " bytes)");
        }
        synchronized (this.mStateLock) {
            if (this.mItem == null) {
                Log.e(TAG, "item is still null");
                this.mStatusCode = -5;
                return -1;
            } else if (this.mStatusCode == -2) {
                Log.d(TAG, "playback already stopped");
                return errorCodeOnStop();
            } else if (this.mStatusCode != 0) {
                Log.d(TAG, "Error was raised " + this.mStatusCode);
                return -1;
            } else {
                SynthesisPlaybackQueueItem item = this.mItem;
                byte[] bufferCopy = new byte[length];
                System.arraycopy(buffer, offset, bufferCopy, 0, length);
                try {
                    item.put(bufferCopy);
                    this.mLogger.onEngineDataReceived();
                    return 0;
                } catch (InterruptedException ie) {
                    synchronized (this.mStateLock) {
                        Log.e(TAG, "item put interrupted", ie);
                        this.mStatusCode = -5;
                        return -1;
                    }
                }
            }
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
                Log.d(TAG, "Request has been aborted.");
                return errorCodeOnStop();
            } else {
                this.mDone = true;
                if (this.mItem == null) {
                    Log.w(TAG, "done() was called before start() call");
                    if (this.mStatusCode == 0) {
                        this.mDispatcher.dispatchOnSuccess();
                    } else {
                        this.mDispatcher.dispatchOnError(this.mStatusCode);
                    }
                    this.mLogger.onEngineComplete();
                    return -1;
                }
                SynthesisPlaybackQueueItem item = this.mItem;
                int statusCode = this.mStatusCode;
                if (statusCode == 0) {
                    item.done();
                } else {
                    item.stop(statusCode);
                }
                this.mLogger.onEngineComplete();
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
        Log.d(TAG, "error() [will call stop]");
        synchronized (this.mStateLock) {
            if (this.mDone) {
                return;
            }
            this.mStatusCode = errorCode;
        }
    }

    @Override // android.speech.tts.SynthesisCallback
    public void rangeStart(int markerInFrames, int start, int end) {
        if (this.mItem == null) {
            Log.e(TAG, "mItem is null");
        } else {
            this.mItem.rangeStart(markerInFrames, start, end);
        }
    }
}
