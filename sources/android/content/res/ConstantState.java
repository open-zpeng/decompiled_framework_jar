package android.content.res;

import android.content.res.Resources;
/* loaded from: classes.dex */
public abstract class ConstantState<T> {
    public abstract synchronized int getChangingConfigurations();

    public abstract synchronized T newInstance();

    public synchronized T newInstance(Resources res) {
        return newInstance();
    }

    public synchronized T newInstance(Resources res, Resources.Theme theme) {
        return newInstance(res);
    }
}
