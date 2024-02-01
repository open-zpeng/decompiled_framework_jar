package android.content.res;

import android.content.res.Resources;
/* loaded from: classes.dex */
public abstract class ComplexColor {
    private int mChangingConfigurations;

    public abstract synchronized boolean canApplyTheme();

    public abstract synchronized ConstantState<ComplexColor> getConstantState();

    public abstract synchronized int getDefaultColor();

    /* renamed from: obtainForTheme */
    public abstract synchronized ComplexColor mo19obtainForTheme(Resources.Theme theme);

    public synchronized boolean isStateful() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void setBaseChangingConfigurations(int changingConfigurations) {
        this.mChangingConfigurations = changingConfigurations;
    }

    public synchronized int getChangingConfigurations() {
        return this.mChangingConfigurations;
    }
}
