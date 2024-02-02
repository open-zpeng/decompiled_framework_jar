package com.android.internal.logging;

import android.util.EventLog;
/* loaded from: classes3.dex */
public class EventLogTags {
    public static final int COMMIT_SYS_CONFIG_FILE = 525000;
    public static final int SYSUI_ACTION = 524288;
    public static final int SYSUI_COUNT = 524290;
    public static final int SYSUI_HISTOGRAM = 524291;
    public static final int SYSUI_LATENCY = 36070;
    public static final int SYSUI_MULTI_ACTION = 524292;
    public static final int SYSUI_VIEW_VISIBILITY = 524287;

    private synchronized EventLogTags() {
    }

    public static synchronized void writeSysuiViewVisibility(int category, int visible) {
        EventLog.writeEvent((int) SYSUI_VIEW_VISIBILITY, Integer.valueOf(category), Integer.valueOf(visible));
    }

    public static synchronized void writeSysuiAction(int category, String pkg) {
        EventLog.writeEvent(524288, Integer.valueOf(category), pkg);
    }

    public static synchronized void writeSysuiMultiAction(Object[] content) {
        EventLog.writeEvent(524292, content);
    }

    public static synchronized void writeSysuiCount(String name, int increment) {
        EventLog.writeEvent(524290, name, Integer.valueOf(increment));
    }

    public static synchronized void writeSysuiHistogram(String name, int bucket) {
        EventLog.writeEvent(524291, name, Integer.valueOf(bucket));
    }

    public static synchronized void writeSysuiLatency(int action, int latency) {
        EventLog.writeEvent(36070, Integer.valueOf(action), Integer.valueOf(latency));
    }

    public static synchronized void writeCommitSysConfigFile(String name, long time) {
        EventLog.writeEvent((int) COMMIT_SYS_CONFIG_FILE, name, Long.valueOf(time));
    }
}
