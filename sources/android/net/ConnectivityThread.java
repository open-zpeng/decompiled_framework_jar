package android.net;

import android.os.HandlerThread;
import android.os.Looper;

/* loaded from: classes2.dex */
public final class ConnectivityThread extends HandlerThread {
    static /* synthetic */ ConnectivityThread access$000() {
        return createInstance();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class Singleton {
        private static final ConnectivityThread INSTANCE = ConnectivityThread.access$000();

        private Singleton() {
        }
    }

    private ConnectivityThread() {
        super("ConnectivityThread");
    }

    private static ConnectivityThread createInstance() {
        ConnectivityThread t = new ConnectivityThread();
        t.start();
        return t;
    }

    public static ConnectivityThread get() {
        return Singleton.INSTANCE;
    }

    public static Looper getInstanceLooper() {
        return Singleton.INSTANCE.getLooper();
    }
}
