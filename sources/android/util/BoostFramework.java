package android.util;

import android.content.Context;
import android.os.SystemProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
/* loaded from: classes2.dex */
public class BoostFramework {
    public static final int MPCTLV3_COMPACT_MEMORY = 1120043008;
    public static final int MPCTLV3_DROP_CACHES = 1120010240;
    public static final int MPCTLV3_EXTFRAG_THRESHOLD = 1120026624;
    private static final String PERFORMANCE_CLASS = "com.qualcomm.qti.Performance";
    private static final String PERFORMANCE_JAR = "/system/framework/QPerformance.jar";
    private static final String TAG = "BoostFramework";
    public static final int UXE_EVENT_BINDAPP = 2;
    public static final int UXE_EVENT_DISPLAYED_ACT = 3;
    public static final int UXE_EVENT_GAME = 5;
    public static final int UXE_EVENT_KILL = 4;
    public static final int UXE_EVENT_PKG_INSTALL = 8;
    public static final int UXE_EVENT_PKG_UNINSTALL = 7;
    public static final int UXE_EVENT_SUB_LAUNCH = 6;
    public static final int UXE_TRIGGER = 1;
    private static final String UXPERFORMANCE_CLASS = "com.qualcomm.qti.UxPerformance";
    private static final String UXPERFORMANCE_JAR = "/system/framework/UxPerformance.jar";
    public static final int VENDOR_FEEDBACK_LAUNCH_END_POINT = 5634;
    public static final int VENDOR_FEEDBACK_WORKLOAD_TYPE = 5633;
    public static final int VENDOR_HINT_ACTIVITY_BOOST = 4228;
    public static final int VENDOR_HINT_ANIM_BOOST = 4227;
    public static final int VENDOR_HINT_APP_UPDATE = 4242;
    public static final int VENDOR_HINT_DRAG_BOOST = 4231;
    public static final int VENDOR_HINT_FIRST_DRAW = 4162;
    public static final int VENDOR_HINT_FIRST_LAUNCH_BOOST = 4225;
    public static final int VENDOR_HINT_KILL = 4241;
    public static final int VENDOR_HINT_MTP_BOOST = 4230;
    public static final int VENDOR_HINT_PACKAGE_INSTALL_BOOST = 4232;
    public static final int VENDOR_HINT_ROTATION_ANIM_BOOST = 4240;
    public static final int VENDOR_HINT_ROTATION_LATENCY_BOOST = 4233;
    public static final int VENDOR_HINT_SCROLL_BOOST = 4224;
    public static final int VENDOR_HINT_SUBSEQ_LAUNCH_BOOST = 4226;
    public static final int VENDOR_HINT_SUSPEND_PREPARE = 4243;
    public static final int VENDOR_HINT_TAP_EVENT = 4163;
    public static final int VENDOR_HINT_TOUCH_BOOST = 4229;
    private Object mPerf;
    private Object mUxPerf;
    private static boolean sIsLoaded = false;
    private static Class<?> sPerfClass = null;
    private static Method sAcquireFunc = null;
    private static Method sPerfHintFunc = null;
    private static Method sReleaseFunc = null;
    private static Method sReleaseHandlerFunc = null;
    private static Method sFeedbackFunc = null;
    private static int sIopv2 = -1;
    private static Method sIOPStart = null;
    private static Method sIOPStop = null;
    private static Method sUXEngineEvents = null;
    private static Method sUXEngineTrigger = null;
    private static boolean sUxIsLoaded = false;
    private static Class<?> sUxPerfClass = null;
    private static Method sUxIOPStart = null;

    /* loaded from: classes2.dex */
    public class Scroll {
        public static final int HORIZONTAL = 2;
        public static final int PANEL_VIEW = 3;
        public static final int PREFILING = 4;
        public static final int VERTICAL = 1;

        public Scroll() {
        }
    }

    /* loaded from: classes2.dex */
    public class Launch {
        public static final int BOOST_GAME = 4;
        public static final int BOOST_V1 = 1;
        public static final int BOOST_V2 = 2;
        public static final int BOOST_V3 = 3;
        public static final int RESERVED_1 = 5;
        public static final int RESERVED_2 = 6;
        public static final int TYPE_SERVICE_START = 100;
        public static final int TYPE_START_PROC = 101;

        public Launch() {
        }
    }

    /* loaded from: classes2.dex */
    public class Draw {
        public static final int EVENT_TYPE_V1 = 1;

        public Draw() {
        }
    }

    /* loaded from: classes2.dex */
    public class WorkloadType {
        public static final int APP = 1;
        public static final int BROWSER = 3;
        public static final int GAME = 2;
        public static final int NOT_KNOWN = 0;
        public static final int PREPROAPP = 4;

        public WorkloadType() {
        }
    }

    public BoostFramework() {
        this.mPerf = null;
        this.mUxPerf = null;
        initFunctions();
        try {
            if (sPerfClass != null) {
                this.mPerf = sPerfClass.newInstance();
            }
            if (sUxPerfClass != null) {
                this.mUxPerf = sUxPerfClass.newInstance();
            }
        } catch (Exception e) {
            Log.e(TAG, "BoostFramework() : Exception_2 = " + e);
        }
    }

    public BoostFramework(Context context) {
        Constructor cons;
        this.mPerf = null;
        this.mUxPerf = null;
        initFunctions();
        try {
            if (sPerfClass != null && (cons = sPerfClass.getConstructor(Context.class)) != null) {
                this.mPerf = cons.newInstance(context);
            }
            if (sUxPerfClass != null) {
                this.mUxPerf = sUxPerfClass.newInstance();
            }
        } catch (Exception e) {
            Log.e(TAG, "BoostFramework() : Exception_3 = " + e);
        }
    }

    public BoostFramework(boolean isUntrustedDomain) {
        Constructor cons;
        this.mPerf = null;
        this.mUxPerf = null;
        initFunctions();
        try {
            if (sPerfClass != null && (cons = sPerfClass.getConstructor(Boolean.TYPE)) != null) {
                this.mPerf = cons.newInstance(Boolean.valueOf(isUntrustedDomain));
            }
            if (sUxPerfClass != null) {
                this.mUxPerf = sUxPerfClass.newInstance();
            }
        } catch (Exception e) {
            Log.e(TAG, "BoostFramework() : Exception_5 = " + e);
        }
    }

    private void initFunctions() {
        synchronized (BoostFramework.class) {
            if (!sIsLoaded) {
                try {
                    sPerfClass = Class.forName(PERFORMANCE_CLASS);
                    Class[] argClasses = {Integer.TYPE, int[].class};
                    sAcquireFunc = sPerfClass.getMethod("perfLockAcquire", argClasses);
                    Class[] argClasses2 = {Integer.TYPE, String.class, Integer.TYPE, Integer.TYPE};
                    sPerfHintFunc = sPerfClass.getMethod("perfHint", argClasses2);
                    sReleaseFunc = sPerfClass.getMethod("perfLockRelease", new Class[0]);
                    Class[] argClasses3 = {Integer.TYPE};
                    sReleaseHandlerFunc = sPerfClass.getDeclaredMethod("perfLockReleaseHandler", argClasses3);
                    Class[] argClasses4 = {Integer.TYPE, String.class};
                    sFeedbackFunc = sPerfClass.getMethod("perfGetFeedback", argClasses4);
                    Class[] argClasses5 = {Integer.TYPE, String.class, String.class};
                    sIOPStart = sPerfClass.getDeclaredMethod("perfIOPrefetchStart", argClasses5);
                    sIOPStop = sPerfClass.getDeclaredMethod("perfIOPrefetchStop", new Class[0]);
                    try {
                        Class[] argClasses6 = {Integer.TYPE, Integer.TYPE, String.class, Integer.TYPE};
                        sUXEngineEvents = sPerfClass.getDeclaredMethod("perfUXEngine_events", argClasses6);
                        Class[] argClasses7 = {Integer.TYPE};
                        sUXEngineTrigger = sPerfClass.getDeclaredMethod("perfUXEngine_trigger", argClasses7);
                    } catch (Exception e) {
                        Log.i(TAG, "BoostFramework() : Exception_4 = PreferredApps not supported");
                    }
                    sIsLoaded = true;
                } catch (Exception e2) {
                    Log.e(TAG, "BoostFramework() : Exception_1 = " + e2);
                }
                try {
                    sUxPerfClass = Class.forName(UXPERFORMANCE_CLASS);
                    Class[] argUxClasses = {Integer.TYPE, String.class, String.class};
                    sUxIOPStart = sUxPerfClass.getDeclaredMethod("perfIOPrefetchStart", argUxClasses);
                    sUxIsLoaded = true;
                } catch (Exception e3) {
                    Log.e(TAG, "BoostFramework() Ux Perf: Exception = " + e3);
                }
            }
        }
    }

    public int perfLockAcquire(int duration, int... list) {
        Log.i(TAG, "perfLockAcquire");
        try {
            if (sAcquireFunc == null) {
                return -1;
            }
            Object retVal = sAcquireFunc.invoke(this.mPerf, Integer.valueOf(duration), list);
            int ret = ((Integer) retVal).intValue();
            return ret;
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e);
            return -1;
        }
    }

    public int perfLockRelease() {
        Log.i(TAG, "perfLockRelease");
        try {
            if (sReleaseFunc == null) {
                return -1;
            }
            Object retVal = sReleaseFunc.invoke(this.mPerf, new Object[0]);
            int ret = ((Integer) retVal).intValue();
            return ret;
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e);
            return -1;
        }
    }

    public int perfLockReleaseHandler(int handle) {
        Log.i(TAG, "perfLockReleaseHandler");
        try {
            if (sReleaseHandlerFunc == null) {
                return -1;
            }
            Object retVal = sReleaseHandlerFunc.invoke(this.mPerf, Integer.valueOf(handle));
            int ret = ((Integer) retVal).intValue();
            return ret;
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e);
            return -1;
        }
    }

    public int perfHint(int hint, String userDataStr) {
        Log.i(TAG, "perfHint 01");
        return perfHint(hint, userDataStr, -1, -1);
    }

    public int perfHint(int hint, String userDataStr, int userData) {
        Log.i(TAG, "perfHint 02");
        return perfHint(hint, userDataStr, userData, -1);
    }

    public int perfHint(int hint, String userDataStr, int userData1, int userData2) {
        try {
            Log.i(TAG, "perfHint 03");
            if (sPerfHintFunc == null) {
                return -1;
            }
            Object retVal = sPerfHintFunc.invoke(this.mPerf, Integer.valueOf(hint), userDataStr, Integer.valueOf(userData1), Integer.valueOf(userData2));
            int ret = ((Integer) retVal).intValue();
            return ret;
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e);
            return -1;
        }
    }

    public int perfGetFeedback(int req, String userDataStr) {
        try {
            if (sFeedbackFunc == null) {
                return -1;
            }
            Object retVal = sFeedbackFunc.invoke(this.mPerf, Integer.valueOf(req), userDataStr);
            int ret = ((Integer) retVal).intValue();
            return ret;
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e);
            return -1;
        }
    }

    public int perfIOPrefetchStart(int pid, String pkgName, String codePath) {
        int ret = -1;
        try {
            Object retVal = sIOPStart.invoke(this.mPerf, Integer.valueOf(pid), pkgName, codePath);
            ret = ((Integer) retVal).intValue();
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e);
        }
        try {
            Object retVal2 = sUxIOPStart.invoke(this.mUxPerf, Integer.valueOf(pid), pkgName, codePath);
            int ret2 = ((Integer) retVal2).intValue();
            return ret2;
        } catch (Exception e2) {
            Log.e(TAG, "Ux Perf Exception " + e2);
            return ret;
        }
    }

    public int perfIOPrefetchStop() {
        try {
            Object retVal = sIOPStop.invoke(this.mPerf, new Object[0]);
            int ret = ((Integer) retVal).intValue();
            return ret;
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e);
            return -1;
        }
    }

    public int perfUXEngine_events(int opcode, int pid, String pkgName, int lat) {
        if (sIopv2 == -1) {
            sIopv2 = SystemProperties.getInt("vendor.iop.enable_uxe", 0);
        }
        try {
            if (sIopv2 != 0 && sUXEngineEvents != null) {
                Object retVal = sUXEngineEvents.invoke(this.mPerf, Integer.valueOf(opcode), Integer.valueOf(pid), pkgName, Integer.valueOf(lat));
                int ret = ((Integer) retVal).intValue();
                return ret;
            }
            return -1;
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e);
            return -1;
        }
    }

    public String perfUXEngine_trigger(int opcode) {
        if (sIopv2 == -1) {
            sIopv2 = SystemProperties.getInt("vendor.iop.enable_uxe", 0);
        }
        try {
            if (sIopv2 != 0 && sUXEngineTrigger != null) {
                Object retVal = sUXEngineTrigger.invoke(this.mPerf, Integer.valueOf(opcode));
                String ret = (String) retVal;
                return ret;
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e);
            return null;
        }
    }
}
