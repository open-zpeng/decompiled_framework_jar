package android.speech.tts;

import android.speech.tts.TextToSpeechService;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class PlaybackQueueItem implements Runnable {
    private final Object mCallerIdentity;
    private final TextToSpeechService.UtteranceProgressDispatcher mDispatcher;

    @Override // java.lang.Runnable
    public abstract void run();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract synchronized void stop(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized PlaybackQueueItem(TextToSpeechService.UtteranceProgressDispatcher dispatcher, Object callerIdentity) {
        this.mDispatcher = dispatcher;
        this.mCallerIdentity = callerIdentity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Object getCallerIdentity() {
        return this.mCallerIdentity;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized TextToSpeechService.UtteranceProgressDispatcher getDispatcher() {
        return this.mDispatcher;
    }
}
