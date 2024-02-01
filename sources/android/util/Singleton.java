package android.util;
/* loaded from: classes2.dex */
public abstract class Singleton<T> {
    public protected T mInstance;

    protected abstract synchronized T create();

    /* JADX INFO: Access modifiers changed from: private */
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
