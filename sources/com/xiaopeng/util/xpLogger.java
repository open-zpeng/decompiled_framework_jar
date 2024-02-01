package com.xiaopeng.util;

import android.util.Log;
import android.util.Slog;
/* loaded from: classes3.dex */
public class xpLogger {
    private static final boolean DEBUG_D = true;
    private static final boolean DEBUG_E = true;
    private static final boolean DEBUG_I = true;
    private static final boolean DEBUG_V = true;

    public static void log(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void slog(String tag, String msg) {
        Slog.i(tag, msg);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }
}
