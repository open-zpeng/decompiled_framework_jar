package android.util;

import android.annotation.UnsupportedAppUsage;
import java.io.Writer;

/* loaded from: classes2.dex */
public class LogWriter extends Writer {
    private final int mBuffer;
    private StringBuilder mBuilder;
    private final int mPriority;
    private final String mTag;

    @UnsupportedAppUsage
    public LogWriter(int priority, String tag) {
        this.mBuilder = new StringBuilder(128);
        this.mPriority = priority;
        this.mTag = tag;
        this.mBuffer = 0;
    }

    public LogWriter(int priority, String tag, int buffer) {
        this.mBuilder = new StringBuilder(128);
        this.mPriority = priority;
        this.mTag = tag;
        this.mBuffer = buffer;
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        flushBuilder();
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
        flushBuilder();
    }

    @Override // java.io.Writer
    public void write(char[] buf, int offset, int count) {
        for (int i = 0; i < count; i++) {
            char c = buf[offset + i];
            if (c == '\n') {
                flushBuilder();
            } else {
                this.mBuilder.append(c);
            }
        }
    }

    private void flushBuilder() {
        if (this.mBuilder.length() > 0) {
            Log.printLog(this.mBuffer, this.mPriority, this.mTag, this.mBuilder.toString());
            StringBuilder sb = this.mBuilder;
            sb.delete(0, sb.length());
        }
    }
}
