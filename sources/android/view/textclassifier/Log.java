package android.view.textclassifier;

import android.util.Slog;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class Log {
    private static final boolean ENABLE_FULL_LOGGING = false;

    private Log() {
    }

    public static void d(String tag, String msg) {
        Slog.d(tag, msg);
    }

    public static void w(String tag, String msg) {
        Slog.w(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        String trString = tr != null ? tr.getClass().getSimpleName() : "??";
        Slog.d(tag, String.format("%s (%s)", msg, trString));
    }
}
