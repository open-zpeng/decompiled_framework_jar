package android.hardware.sidekick;
/* loaded from: classes.dex */
public abstract class SidekickInternal {
    private protected abstract synchronized void endDisplayControl();

    private protected abstract synchronized boolean reset();

    private protected abstract synchronized boolean startDisplayControl(int i);

    private protected synchronized SidekickInternal() {
    }
}
