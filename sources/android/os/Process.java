package android.os;

import android.os.StrictMode;
import android.system.Os;
import android.system.OsConstants;
import android.webkit.WebViewZygote;
import dalvik.system.VMRuntime;
/* loaded from: classes2.dex */
public class Process {
    public static final int AUDIOSERVER_UID = 1041;
    private protected static final int BLUETOOTH_UID = 1002;
    public static final int CAMERASERVER_UID = 1047;
    private protected static final int DRM_UID = 1019;
    public static final int FIRST_APPLICATION_CACHE_GID = 20000;
    public static final int FIRST_APPLICATION_UID = 10000;
    public static final int FIRST_ISOLATED_UID = 99000;
    public static final int FIRST_SHARED_APPLICATION_GID = 50000;
    public static final int INCIDENTD_UID = 1067;
    public static final int KEYSTORE_UID = 1017;
    public static final int LAST_APPLICATION_CACHE_GID = 29999;
    public static final int LAST_APPLICATION_UID = 19999;
    public static final int LAST_ISOLATED_UID = 99999;
    public static final int LAST_SHARED_APPLICATION_GID = 59999;
    private static final String LOG_TAG = "Process";
    private protected static final int LOG_UID = 1007;
    public static final int MEDIA_RW_GID = 1023;
    private protected static final int MEDIA_UID = 1013;
    private protected static final int NFC_UID = 1027;
    public static final int NOBODY_UID = 9999;
    public static final int OTA_UPDATE_UID = 1061;
    public static final int PACKAGE_INFO_GID = 1032;
    public static final int PHONE_UID = 1001;
    public static final int PROC_CHAR = 2048;
    private protected static final int PROC_COMBINE = 256;
    private protected static final int PROC_OUT_FLOAT = 16384;
    private protected static final int PROC_OUT_LONG = 8192;
    private protected static final int PROC_OUT_STRING = 4096;
    private protected static final int PROC_PARENS = 512;
    private protected static final int PROC_QUOTES = 1024;
    private protected static final int PROC_SPACE_TERM = 32;
    private protected static final int PROC_TAB_TERM = 9;
    private protected static final int PROC_TERM_MASK = 255;
    private protected static final int PROC_ZERO_TERM = 0;
    private protected static final int ROOT_UID = 0;
    public static final int SCHED_BATCH = 3;
    public static final int SCHED_FIFO = 1;
    public static final int SCHED_IDLE = 5;
    public static final int SCHED_OTHER = 0;
    public static final int SCHED_RESET_ON_FORK = 1073741824;
    public static final int SCHED_RR = 2;
    public static final int SE_UID = 1068;
    public static final int SHARED_RELRO_UID = 1037;
    public static final int SHARED_USER_GID = 9997;
    private protected static final int SHELL_UID = 2000;
    public static final int SIGNAL_KILL = 9;
    public static final int SIGNAL_QUIT = 3;
    public static final int SIGNAL_USR1 = 10;
    public static final int SYSTEM_UID = 1000;
    public static final int THREAD_GROUP_AUDIO_APP = 3;
    public static final int THREAD_GROUP_AUDIO_SYS = 4;
    public static final int THREAD_GROUP_BG_NONINTERACTIVE = 0;
    public static final int THREAD_GROUP_DEFAULT = -1;
    private static final int THREAD_GROUP_FOREGROUND = 1;
    public static final int THREAD_GROUP_RESTRICTED = 7;
    public static final int THREAD_GROUP_RT_APP = 6;
    public static final int THREAD_GROUP_SYSTEM = 2;
    public static final int THREAD_GROUP_TOP_APP = 5;
    public static final int THREAD_PRIORITY_AUDIO = -16;
    public static final int THREAD_PRIORITY_BACKGROUND = 10;
    public static final int THREAD_PRIORITY_DEFAULT = 0;
    public static final int THREAD_PRIORITY_DISPLAY = -4;
    public static final int THREAD_PRIORITY_FOREGROUND = -2;
    public static final int THREAD_PRIORITY_LESS_FAVORABLE = 1;
    public static final int THREAD_PRIORITY_LOWEST = 19;
    public static final int THREAD_PRIORITY_MORE_FAVORABLE = -1;
    public static final int THREAD_PRIORITY_URGENT_AUDIO = -19;
    public static final int THREAD_PRIORITY_URGENT_DISPLAY = -8;
    public static final int THREAD_PRIORITY_VIDEO = -10;
    private protected static final int VPN_UID = 1016;
    public static final int WEBVIEW_ZYGOTE_UID = 1053;
    private protected static final int WIFI_UID = 1010;
    private static long sStartElapsedRealtime;
    private static long sStartUptimeMillis;
    public static final String ZYGOTE_SOCKET = "zygote";
    public static final String SECONDARY_ZYGOTE_SOCKET = "zygote_secondary";
    public static final ZygoteProcess zygoteProcess = new ZygoteProcess(ZYGOTE_SOCKET, SECONDARY_ZYGOTE_SOCKET);

    /* loaded from: classes2.dex */
    public static final class ProcessStartResult {
        public int pid;
        public boolean usingWrapper;
    }

    public static final native long getElapsedCpuTime();

    public static final native int[] getExclusiveCores();

    private protected static final native long getFreeMemory();

    public static final native int getGidForName(String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static final native int[] getPids(String str, int[] iArr);

    private protected static final native int[] getPidsForCommands(String[] strArr);

    public static final native int getProcessGroup(int i) throws IllegalArgumentException, SecurityException;

    private protected static final native long getPss(int i);

    public static final native int getThreadPriority(int i) throws IllegalArgumentException;

    public static final native int getThreadScheduler(int i) throws IllegalArgumentException;

    private protected static final native long getTotalMemory();

    public static final native int getUidForName(String str);

    public static final native int killProcessGroup(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static final native boolean parseProcLine(byte[] bArr, int i, int i2, int[] iArr, String[] strArr, long[] jArr, float[] fArr);

    /* JADX INFO: Access modifiers changed from: private */
    public static final native boolean readProcFile(String str, int[] iArr, String[] strArr, long[] jArr, float[] fArr);

    private protected static final native void readProcLines(String str, String[] strArr, long[] jArr);

    public static final native void removeAllProcessGroups();

    public static final native void sendSignal(int i, int i2);

    private protected static final native void sendSignalQuiet(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static final native void setArgV0(String str);

    public static final native void setCanSelfBackground(boolean z);

    public static final native int setGid(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static final native void setProcessGroup(int i, int i2) throws IllegalArgumentException, SecurityException;

    public static final native boolean setSwappiness(int i, boolean z);

    public static final native void setThreadGroup(int i, int i2) throws IllegalArgumentException, SecurityException;

    public static final native void setThreadGroupAndCpuset(int i, int i2) throws IllegalArgumentException, SecurityException;

    public static final native void setThreadPriority(int i) throws IllegalArgumentException, SecurityException;

    public static final native void setThreadPriority(int i, int i2) throws IllegalArgumentException, SecurityException;

    public static final native void setThreadScheduler(int i, int i2, int i3) throws IllegalArgumentException;

    public static final native int setUid(int i);

    public static final synchronized ProcessStartResult start(String processClass, String niceName, int uid, int gid, int[] gids, int runtimeFlags, int mountExternal, int targetSdkVersion, String seInfo, String abi, String instructionSet, String appDataDir, String invokeWith, String[] zygoteArgs) {
        return zygoteProcess.start(processClass, niceName, uid, gid, gids, runtimeFlags, mountExternal, targetSdkVersion, seInfo, abi, instructionSet, appDataDir, invokeWith, zygoteArgs);
    }

    public static final synchronized ProcessStartResult startWebView(String processClass, String niceName, int uid, int gid, int[] gids, int runtimeFlags, int mountExternal, int targetSdkVersion, String seInfo, String abi, String instructionSet, String appDataDir, String invokeWith, String[] zygoteArgs) {
        return WebViewZygote.getProcess().start(processClass, niceName, uid, gid, gids, runtimeFlags, mountExternal, targetSdkVersion, seInfo, abi, instructionSet, appDataDir, invokeWith, zygoteArgs);
    }

    public static final long getStartElapsedRealtime() {
        return sStartElapsedRealtime;
    }

    public static final long getStartUptimeMillis() {
        return sStartUptimeMillis;
    }

    public static final synchronized void setStartTimes(long elapsedRealtime, long uptimeMillis) {
        sStartElapsedRealtime = elapsedRealtime;
        sStartUptimeMillis = uptimeMillis;
    }

    public static final boolean is64Bit() {
        return VMRuntime.getRuntime().is64Bit();
    }

    public static final int myPid() {
        return Os.getpid();
    }

    private protected static final int myPpid() {
        return Os.getppid();
    }

    public static final int myTid() {
        return Os.gettid();
    }

    public static final int myUid() {
        return Os.getuid();
    }

    public static UserHandle myUserHandle() {
        return UserHandle.of(UserHandle.getUserId(myUid()));
    }

    public static synchronized boolean isCoreUid(int uid) {
        return UserHandle.isCore(uid);
    }

    public static boolean isApplicationUid(int uid) {
        return UserHandle.isApp(uid);
    }

    public static final boolean isIsolated() {
        return isIsolated(myUid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean isIsolated(int uid) {
        int uid2 = UserHandle.getAppId(uid);
        return uid2 >= 99000 && uid2 <= 99999;
    }

    private protected static final int getUidForPid(int pid) {
        String[] procStatusLabels = {"Uid:"};
        long[] procStatusValues = {-1};
        readProcLines("/proc/" + pid + "/status", procStatusLabels, procStatusValues);
        return (int) procStatusValues[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int getParentPid(int pid) {
        String[] procStatusLabels = {"PPid:"};
        long[] procStatusValues = {-1};
        readProcLines("/proc/" + pid + "/status", procStatusLabels, procStatusValues);
        return (int) procStatusValues[0];
    }

    public static final synchronized int getThreadGroupLeader(int tid) {
        String[] procStatusLabels = {"Tgid:"};
        long[] procStatusValues = {-1};
        readProcLines("/proc/" + tid + "/status", procStatusLabels, procStatusValues);
        return (int) procStatusValues[0];
    }

    @Deprecated
    public static final boolean supportsProcesses() {
        return true;
    }

    public static final void killProcess(int pid) {
        sendSignal(pid, 9);
    }

    public static final synchronized void killProcessQuiet(int pid) {
        sendSignalQuiet(pid, 9);
    }

    public static final synchronized boolean isThreadInProcess(int tid, int pid) {
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskReads();
        try {
            if (Os.access("/proc/" + tid + "/task/" + pid, OsConstants.F_OK)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }
}
