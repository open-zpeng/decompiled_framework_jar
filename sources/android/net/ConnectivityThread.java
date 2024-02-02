package android.net;

import android.os.HandlerThread;
import android.os.Looper;
/* loaded from: classes2.dex */
public final class ConnectivityThread extends HandlerThread {
    static /* synthetic */ ConnectivityThread access$000() {
        return createInstance();
    }

    /* loaded from: classes2.dex */
    private static class Singleton {
        private static final ConnectivityThread INSTANCE = ConnectivityThread.access$000();

        private synchronized Singleton() {
        }
    }

    private synchronized ConnectivityThread() {
        super("ConnectivityThread");
    }

    private static synchronized ConnectivityThread createInstance() {
        ConnectivityThread t = new ConnectivityThread();
        t.start();
        return t;
    }

    public static synchronized ConnectivityThread get() {
        return Singleton.INSTANCE;
    }

    public static synchronized Looper getInstanceLooper() {
        return Singleton.INSTANCE.getLooper();
    }
}
