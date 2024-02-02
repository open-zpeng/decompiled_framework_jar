package android.speech.tts;
/* loaded from: classes2.dex */
abstract class AbstractSynthesisCallback implements SynthesisCallback {
    protected final boolean mClientIsUsingV2;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract synchronized void stop();

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized AbstractSynthesisCallback(boolean clientIsUsingV2) {
        this.mClientIsUsingV2 = clientIsUsingV2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int errorCodeOnStop() {
        return this.mClientIsUsingV2 ? -2 : -1;
    }
}
