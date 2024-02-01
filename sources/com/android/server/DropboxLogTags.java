package com.android.server;

import android.util.EventLog;
/* loaded from: classes3.dex */
public class DropboxLogTags {
    private protected static final int DROPBOX_FILE_COPY = 81002;

    private protected static synchronized void writeDropboxFileCopy(String filename, int size, String tag) {
        EventLog.writeEvent((int) DROPBOX_FILE_COPY, filename, Integer.valueOf(size), tag);
    }
}
