package com.android.internal.os;

import android.annotation.UnsupportedAppUsage;
import android.os.DropBoxManager;
import android.util.Log;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class AndroidPrintStream extends LoggingPrintStream {
    private final int priority;
    private final String tag;

    @UnsupportedAppUsage
    public AndroidPrintStream(int priority, String tag) {
        if (tag == null) {
            throw new NullPointerException(DropBoxManager.EXTRA_TAG);
        }
        this.priority = priority;
        this.tag = tag;
    }

    @Override // com.android.internal.os.LoggingPrintStream
    protected void log(String line) {
        Log.println(this.priority, this.tag, line);
    }
}
