package android.view;

import android.os.IBinder;

/* loaded from: classes3.dex */
public final class InputApplicationHandle {
    public long dispatchingTimeoutNanos;
    public String name;
    private long ptr;
    public IBinder token;

    private native void nativeDispose();

    public InputApplicationHandle(IBinder token) {
        this.token = token;
    }

    protected void finalize() throws Throwable {
        try {
            nativeDispose();
        } finally {
            super.finalize();
        }
    }
}
