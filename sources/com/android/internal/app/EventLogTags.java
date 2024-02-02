package com.android.internal.app;

import android.util.EventLog;
/* loaded from: classes3.dex */
public class EventLogTags {
    public static final int HARMFUL_APP_WARNING_LAUNCH_ANYWAY = 53001;
    public static final int HARMFUL_APP_WARNING_UNINSTALL = 53000;

    private synchronized EventLogTags() {
    }

    public static synchronized void writeHarmfulAppWarningUninstall(String packageName) {
        EventLog.writeEvent((int) HARMFUL_APP_WARNING_UNINSTALL, packageName);
    }

    public static synchronized void writeHarmfulAppWarningLaunchAnyway(String packageName) {
        EventLog.writeEvent((int) HARMFUL_APP_WARNING_LAUNCH_ANYWAY, packageName);
    }
}
