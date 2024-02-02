package android.speech.tts;

import android.os.SystemClock;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class AbstractEventLogger {
    protected final int mCallerPid;
    protected final int mCallerUid;
    protected final String mServiceApp;
    protected long mPlaybackStartTime = -1;
    private volatile long mRequestProcessingStartTime = -1;
    private volatile long mEngineStartTime = -1;
    private volatile long mEngineCompleteTime = -1;
    private boolean mLogWritten = false;
    protected final long mReceivedTime = SystemClock.elapsedRealtime();

    protected abstract synchronized void logFailure(int i);

    protected abstract synchronized void logSuccess(long j, long j2, long j3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized AbstractEventLogger(int callerUid, int callerPid, String serviceApp) {
        this.mCallerUid = callerUid;
        this.mCallerPid = callerPid;
        this.mServiceApp = serviceApp;
    }

    public synchronized void onRequestProcessingStart() {
        this.mRequestProcessingStartTime = SystemClock.elapsedRealtime();
    }

    public synchronized void onEngineDataReceived() {
        if (this.mEngineStartTime == -1) {
            this.mEngineStartTime = SystemClock.elapsedRealtime();
        }
    }

    public synchronized void onEngineComplete() {
        this.mEngineCompleteTime = SystemClock.elapsedRealtime();
    }

    public synchronized void onAudioDataWritten() {
        if (this.mPlaybackStartTime == -1) {
            this.mPlaybackStartTime = SystemClock.elapsedRealtime();
        }
    }

    public synchronized void onCompleted(int statusCode) {
        if (this.mLogWritten) {
            return;
        }
        this.mLogWritten = true;
        SystemClock.elapsedRealtime();
        if (statusCode != 0 || this.mPlaybackStartTime == -1 || this.mEngineCompleteTime == -1) {
            logFailure(statusCode);
            return;
        }
        long audioLatency = this.mPlaybackStartTime - this.mReceivedTime;
        long engineLatency = this.mEngineStartTime - this.mRequestProcessingStartTime;
        long engineTotal = this.mEngineCompleteTime - this.mRequestProcessingStartTime;
        logSuccess(audioLatency, engineLatency, engineTotal);
    }
}
