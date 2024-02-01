package com.android.internal.os;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Trace;
/* loaded from: classes3.dex */
public final class BackgroundThreadxp extends HandlerThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 30000;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 10000;
    private static Handler sHandler;
    private static BackgroundThreadxp sInstance;

    private BackgroundThreadxp() {
        super("android.xp.bg", 10);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new BackgroundThreadxp();
            sInstance.start();
            Looper looper = sInstance.getLooper();
            looper.setTraceTag(Trace.TRACE_TAG_SYSTEM_SERVER);
            looper.setSlowLogThresholdMs(10000L, 30000L);
            sHandler = new Handler(sInstance.getLooper());
        }
    }

    public static BackgroundThreadxp get() {
        BackgroundThreadxp backgroundThreadxp;
        synchronized (BackgroundThreadxp.class) {
            ensureThreadLocked();
            backgroundThreadxp = sInstance;
        }
        return backgroundThreadxp;
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (BackgroundThreadxp.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }
}
