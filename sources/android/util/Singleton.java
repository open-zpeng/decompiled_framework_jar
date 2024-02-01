package android.util;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes2.dex */
public abstract class Singleton<T> {
    @UnsupportedAppUsage
    private T mInstance;

    protected abstract T create();

    @UnsupportedAppUsage
    public final T get() {
        T t;
        synchronized (this) {
            if (this.mInstance == null) {
                this.mInstance = create();
            }
            t = this.mInstance;
        }
        return t;
    }
}
