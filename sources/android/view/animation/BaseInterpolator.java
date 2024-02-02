package android.view.animation;
/* loaded from: classes2.dex */
public abstract class BaseInterpolator implements Interpolator {
    private int mChangingConfiguration;

    public synchronized int getChangingConfiguration() {
        return this.mChangingConfiguration;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setChangingConfiguration(int changingConfiguration) {
        this.mChangingConfiguration = changingConfiguration;
    }
}
