package com.android.internal.os;

import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.EventLog;
import android.util.SparseIntArray;
import com.android.internal.util.Preconditions;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public class BinderInternal {
    private static final String TAG = "BinderInternal";
    static long sLastGcTime;
    static WeakReference<GcWatcher> sGcWatcher = new WeakReference<>(new GcWatcher());
    static ArrayList<Runnable> sGcWatchers = new ArrayList<>();
    static Runnable[] sTmpWatchers = new Runnable[1];
    static final BinderProxyLimitListenerDelegate sBinderProxyLimitListenerDelegate = new BinderProxyLimitListenerDelegate();

    /* loaded from: classes3.dex */
    public interface BinderProxyLimitListener {
        void onLimitReached(int i);
    }

    public static final native void disableBackgroundScheduling(boolean z);

    public static final native IBinder getContextObject();

    static final native void handleGc();

    public static final native void joinThreadPool();

    public static final native int nGetBinderProxyCount(int i);

    public static final native SparseIntArray nGetBinderProxyPerUidCounts();

    public static final native void nSetBinderProxyCountEnabled(boolean z);

    public static final native void nSetBinderProxyCountWatermarks(int i, int i2);

    public static final native void setMaxThreads(int i);

    /* loaded from: classes3.dex */
    static final class GcWatcher {
        GcWatcher() {
        }

        protected void finalize() throws Throwable {
            BinderInternal.handleGc();
            BinderInternal.sLastGcTime = SystemClock.uptimeMillis();
            synchronized (BinderInternal.sGcWatchers) {
                BinderInternal.sTmpWatchers = (Runnable[]) BinderInternal.sGcWatchers.toArray(BinderInternal.sTmpWatchers);
            }
            for (int i = 0; i < BinderInternal.sTmpWatchers.length; i++) {
                if (BinderInternal.sTmpWatchers[i] != null) {
                    BinderInternal.sTmpWatchers[i].run();
                }
            }
            BinderInternal.sGcWatcher = new WeakReference<>(new GcWatcher());
        }
    }

    public static void addGcWatcher(Runnable watcher) {
        synchronized (sGcWatchers) {
            sGcWatchers.add(watcher);
        }
    }

    public static long getLastGcTime() {
        return sLastGcTime;
    }

    public static void forceGc(String reason) {
        EventLog.writeEvent(2741, reason);
        VMRuntime.getRuntime().requestConcurrentGC();
    }

    static void forceBinderGc() {
        forceGc("Binder");
    }

    public static void binderProxyLimitCallbackFromNative(int uid) {
        sBinderProxyLimitListenerDelegate.notifyClient(uid);
    }

    public static void setBinderProxyCountCallback(BinderProxyLimitListener listener, Handler handler) {
        Preconditions.checkNotNull(handler, "Must provide NonNull Handler to setBinderProxyCountCallback when setting BinderProxyLimitListener");
        sBinderProxyLimitListenerDelegate.setListener(listener, handler);
    }

    public static void clearBinderProxyCountCallback() {
        sBinderProxyLimitListenerDelegate.setListener(null, null);
    }

    /* loaded from: classes3.dex */
    private static class BinderProxyLimitListenerDelegate {
        private BinderProxyLimitListener mBinderProxyLimitListener;
        private Handler mHandler;

        private BinderProxyLimitListenerDelegate() {
        }

        void setListener(BinderProxyLimitListener listener, Handler handler) {
            synchronized (this) {
                this.mBinderProxyLimitListener = listener;
                this.mHandler = handler;
            }
        }

        void notifyClient(final int uid) {
            synchronized (this) {
                if (this.mBinderProxyLimitListener != null) {
                    this.mHandler.post(new Runnable() { // from class: com.android.internal.os.BinderInternal.BinderProxyLimitListenerDelegate.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BinderProxyLimitListenerDelegate.this.mBinderProxyLimitListener.onLimitReached(uid);
                        }
                    });
                }
            }
        }
    }
}
