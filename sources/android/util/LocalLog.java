package android.util;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
/* loaded from: classes2.dex */
public final class LocalLog {
    private final Deque<String> mLog;
    private final int mMaxLines;

    private protected LocalLog(int maxLines) {
        this.mMaxLines = Math.max(0, maxLines);
        this.mLog = new ArrayDeque(this.mMaxLines);
    }

    private protected void log(String msg) {
        if (this.mMaxLines <= 0) {
            return;
        }
        append(String.format("%s - %s", LocalDateTime.now(), msg));
    }

    private synchronized void append(String logLine) {
        while (this.mLog.size() >= this.mMaxLines) {
            this.mLog.remove();
        }
        this.mLog.add(logLine);
    }

    private protected synchronized void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        for (String str : this.mLog) {
            pw.println(str);
        }
    }

    public synchronized void reverseDump(FileDescriptor fd, PrintWriter pw, String[] args) {
        Iterator<String> itr = this.mLog.descendingIterator();
        while (itr.hasNext()) {
            pw.println(itr.next());
        }
    }

    /* loaded from: classes2.dex */
    public static class ReadOnlyLocalLog {
        private final LocalLog mLog;

        synchronized ReadOnlyLocalLog(LocalLog log) {
            this.mLog = log;
        }

        private protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
            this.mLog.dump(fd, pw, args);
        }

        public synchronized void reverseDump(FileDescriptor fd, PrintWriter pw, String[] args) {
            this.mLog.reverseDump(fd, pw, args);
        }
    }

    private protected ReadOnlyLocalLog readOnlyLocalLog() {
        return new ReadOnlyLocalLog(this);
    }
}
