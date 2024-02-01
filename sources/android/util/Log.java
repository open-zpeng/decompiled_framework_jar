package android.util;

import android.os.DeadSystemException;
import com.android.internal.os.RuntimeInit;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.LineBreakBufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.UnknownHostException;
/* loaded from: classes2.dex */
public final class Log {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    private static final int DISCARD_LOG = 0;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    private static final boolean LOG_DEBUG = false;
    public static final int LOG_ID_CRASH = 4;
    public static final int LOG_ID_EVENTS = 2;
    public static final int LOG_ID_MAIN = 0;
    public static final int LOG_ID_RADIO = 1;
    public static final int LOG_ID_SYSTEM = 3;
    private static final int LOG_PRIORITY_INIT = -1;
    private static final String LOG_TAG = "Log";
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static Integer sLogPriority = -1;
    private static TerribleFailureHandler sWtfHandler = new TerribleFailureHandler() { // from class: android.util.Log.1
        @Override // android.util.Log.TerribleFailureHandler
        public void onTerribleFailure(String tag, TerribleFailure what, boolean system) {
            RuntimeInit.wtf(tag, what, system);
        }
    };

    /* loaded from: classes2.dex */
    public interface TerribleFailureHandler {
        synchronized void onTerribleFailure(String str, TerribleFailure terribleFailure, boolean z);
    }

    private static native int get_log_priority_native();

    public static native boolean isLoggable(String str, int i);

    private static native int logger_entry_max_payload_native();

    private protected static native int println_native(int i, int i2, String str, String str2);

    static /* synthetic */ int access$000() {
        return logger_entry_max_payload_native();
    }

    private static void initLogLevel() {
        if (sLogPriority.intValue() == -1) {
            synchronized (Log.class) {
                if (sLogPriority.intValue() == -1) {
                    try {
                        sLogPriority = Integer.valueOf(get_log_priority_native());
                    } catch (Exception e) {
                        sLogPriority = 3;
                    }
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class TerribleFailure extends Exception {
        synchronized TerribleFailure(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    private synchronized Log() {
    }

    public static int v(String tag, String msg) {
        return printLog(0, 2, tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return printlns(0, 2, tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        return printLog(0, 3, tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return printlns(0, 3, tag, msg, tr);
    }

    public static int i(String tag, String msg) {
        return printLog(0, 4, tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return printlns(0, 4, tag, msg, tr);
    }

    public static int w(String tag, String msg) {
        return printLog(0, 5, tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return printlns(0, 5, tag, msg, tr);
    }

    public static int w(String tag, Throwable tr) {
        return printlns(0, 5, tag, "", tr);
    }

    public static int e(String tag, String msg) {
        return printLog(0, 6, tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return printlns(0, 6, tag, msg, tr);
    }

    public static int wtf(String tag, String msg) {
        return wtf(0, tag, msg, null, false, false);
    }

    public static synchronized int wtfStack(String tag, String msg) {
        return wtf(0, tag, msg, null, true, false);
    }

    public static int wtf(String tag, Throwable tr) {
        return wtf(0, tag, tr.getMessage(), tr, false, false);
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        return wtf(0, tag, msg, tr, false, false);
    }

    public private protected static int wtf(int logId, String tag, String msg, Throwable tr, boolean localStack, boolean system) {
        TerribleFailure what = new TerribleFailure(msg, tr);
        int bytes = printlns(logId, 6, tag, msg, localStack ? what : tr);
        sWtfHandler.onTerribleFailure(tag, what, system);
        return bytes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void wtfQuiet(int logId, String tag, String msg, boolean system) {
        TerribleFailure what = new TerribleFailure(msg, null);
        sWtfHandler.onTerribleFailure(tag, what, system);
    }

    public static synchronized TerribleFailureHandler setWtfHandler(TerribleFailureHandler handler) {
        if (handler == null) {
            throw new NullPointerException("handler == null");
        }
        TerribleFailureHandler oldHandler = sWtfHandler;
        sWtfHandler = handler;
        return oldHandler;
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        for (Throwable t = tr; t != null; t = t.getCause()) {
            if (t instanceof UnknownHostException) {
                return "";
            }
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new FastPrintWriter((Writer) sw, false, 256);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static int println(int priority, String tag, String msg) {
        return printLog(0, priority, tag, msg);
    }

    public static synchronized int printlns(int bufID, int priority, String tag, String msg, Throwable tr) {
        return printLongLog(bufID, priority, tag, msg, tr);
    }

    public static int printLog(int bufID, int priority, String tag, String msg) {
        if (sLogPriority.intValue() == -1) {
            initLogLevel();
        }
        if (priority < sLogPriority.intValue() || sLogPriority.intValue() == 0) {
            return 0;
        }
        return println_native(bufID, priority, tag, msg);
    }

    private static int printLongLog(int bufID, int priority, String tag, String msg, Throwable tr) {
        if (sLogPriority.intValue() == -1) {
            initLogLevel();
        }
        if (priority < sLogPriority.intValue() || sLogPriority.intValue() == 0) {
            return 0;
        }
        ImmediateLogWriter logWriter = new ImmediateLogWriter(bufID, priority, tag);
        int bufferSize = ((PreloadHolder.LOGGER_ENTRY_MAX_PAYLOAD - 2) - (tag != null ? tag.length() : 0)) - 32;
        LineBreakBufferedWriter lbbw = new LineBreakBufferedWriter(logWriter, Math.max(bufferSize, 100));
        lbbw.println(msg);
        if (tr != null) {
            Throwable t = tr;
            while (true) {
                if (t == null || (t instanceof UnknownHostException)) {
                    break;
                } else if (t instanceof DeadSystemException) {
                    lbbw.println("DeadSystemException: The system died; earlier logs will point to the root cause");
                    break;
                } else {
                    t = t.getCause();
                }
            }
            if (t == null) {
                tr.printStackTrace(lbbw);
            }
        }
        lbbw.flush();
        return logWriter.getWritten();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class PreloadHolder {
        public static final int LOGGER_ENTRY_MAX_PAYLOAD = Log.access$000();

        synchronized PreloadHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class ImmediateLogWriter extends Writer {
        private int bufID;
        private int priority;
        private String tag;
        private int written = 0;

        public synchronized ImmediateLogWriter(int bufID, int priority, String tag) {
            this.bufID = bufID;
            this.priority = priority;
            this.tag = tag;
        }

        public synchronized int getWritten() {
            return this.written;
        }

        @Override // java.io.Writer
        public void write(char[] cbuf, int off, int len) {
            this.written += Log.println_native(this.bufID, this.priority, this.tag, new String(cbuf, off, len));
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }
    }
}
