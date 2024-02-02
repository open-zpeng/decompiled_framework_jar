package org.chromium.arc;

import android.util.EventLog;
/* loaded from: classes3.dex */
public class EventLogTags {
    private protected static final int ARC_SYSTEM_EVENT = 300000;

    private protected static synchronized void writeArcSystemEvent(String event) {
        EventLog.writeEvent((int) ARC_SYSTEM_EVENT, event);
    }
}
