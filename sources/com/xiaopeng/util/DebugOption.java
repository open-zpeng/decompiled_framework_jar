package com.xiaopeng.util;

import android.os.SystemProperties;

/* loaded from: classes3.dex */
public class DebugOption {
    public static final boolean DEBUG_WM = SystemProperties.getBoolean("persist.sys.xp.debug.wm", false);
    public static final boolean DEBUG_AM = SystemProperties.getBoolean("persist.sys.xp.debug.am", false);
    public static final boolean DEBUG_ATM = SystemProperties.getBoolean("persist.sys.xp.debug.atm", false);
    public static final boolean DEBUG_INPUT = SystemProperties.getBoolean("persist.sys.xp.debug.input", false);
    public static final boolean DEBUG_APP_INPUT = SystemProperties.getBoolean("persist.sys.xp.debug.app.input", true);
    public static final boolean DEBUG_CONFIGURATION = SystemProperties.getBoolean("persist.sys.xp.debug.configuration", false);
    public static final boolean DEBUG_DIALOG_PROXY = SystemProperties.getBoolean("persist.sys.xp.debug.dialog.proxy", false);
    public static final boolean DEBUG_AM_BROADCAST = SystemProperties.getBoolean("persist.sys.xp.debug.am.broadcast", false);
    public static final boolean DEBUG_AM_ACTIVITY_THREAD_BROADCAST = SystemProperties.getBoolean("persist.sys.xp.debug.am.at.broadcast", false);
    public static final boolean DEBUG_AM_ACTIVITY_THREAD_LOCAL_LOGV = SystemProperties.getBoolean("persist.sys.xp.debug.am.at.local_logv", false);
    public static final boolean DEBUG_VIEW_ROOT = SystemProperties.getBoolean("persist.sys.xp.debug.view.root", false);
}
