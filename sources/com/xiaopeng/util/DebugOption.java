package com.xiaopeng.util;

import android.os.SystemProperties;
/* loaded from: classes3.dex */
public class DebugOption {
    public static final boolean DEBUG_WM = SystemProperties.getBoolean("persist.sys.xp.debug.wm", false);
    public static final boolean DEBUG_PM = SystemProperties.getBoolean("persist.sys.xp.debug.pm", false);
    public static final boolean DEBUG_AM = SystemProperties.getBoolean("persist.sys.xp.debug.am", false);
    public static final boolean DEBUG_DIALOG_PROXY = SystemProperties.getBoolean("persist.sys.xp.debug.dialog.proxy", false);
    public static final boolean DEBUG_AM_SERVICE = SystemProperties.getBoolean("persist.sys.xp.debug.am.service", false);
    public static final boolean DEBUG_AM_BROADCAST = SystemProperties.getBoolean("persist.sys.xp.debug.am.broadcast", false);
    public static final boolean DEBUG_AM_ACTIVITY_THREAD_BROADCAST = SystemProperties.getBoolean("persist.sys.xp.debug.am.at.broadcast", false);
    public static final boolean DEBUG_AM_ACTIVITY_THREAD_LOCAL_LOGV = SystemProperties.getBoolean("persist.sys.xp.debug.am.at.local_logv", false);
    public static final boolean DEBUG_INFLATER = SystemProperties.getBoolean("persist.sys.xp.debug.Inflater", false);
    public static final boolean DEBUG_WAKE_DRAW = SystemProperties.getBoolean("persist.sys.xp.debug_wake_draw", false);
}
