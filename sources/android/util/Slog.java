package android.util;
/* loaded from: classes2.dex */
public final class Slog {
    private synchronized Slog() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int v(String tag, String msg) {
        return Log.printLog(3, 2, tag, msg);
    }

    public static synchronized int v(String tag, String msg, Throwable tr) {
        return Log.printLog(3, 2, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int d(String tag, String msg) {
        return Log.printLog(3, 3, tag, msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int d(String tag, String msg, Throwable tr) {
        return Log.printLog(3, 3, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int i(String tag, String msg) {
        return Log.printLog(3, 4, tag, msg);
    }

    public static synchronized int i(String tag, String msg, Throwable tr) {
        return Log.printLog(3, 4, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int w(String tag, String msg) {
        return Log.printLog(3, 5, tag, msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int w(String tag, String msg, Throwable tr) {
        return Log.printLog(3, 5, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static synchronized int w(String tag, Throwable tr) {
        return Log.printLog(3, 5, tag, Log.getStackTraceString(tr));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int e(String tag, String msg) {
        return Log.printLog(3, 6, tag, msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int e(String tag, String msg, Throwable tr) {
        return Log.printLog(3, 6, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int wtf(String tag, String msg) {
        return Log.wtf(3, tag, msg, null, false, true);
    }

    public static synchronized void wtfQuiet(String tag, String msg) {
        Log.wtfQuiet(3, tag, msg, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int wtfStack(String tag, String msg) {
        return Log.wtf(3, tag, msg, null, true, true);
    }

    public static synchronized int wtf(String tag, Throwable tr) {
        return Log.wtf(3, tag, tr.getMessage(), tr, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int wtf(String tag, String msg, Throwable tr) {
        return Log.wtf(3, tag, msg, tr, false, true);
    }

    private protected static int println(int priority, String tag, String msg) {
        return Log.printLog(3, priority, tag, msg);
    }
}
