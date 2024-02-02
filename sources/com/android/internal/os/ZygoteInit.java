package com.android.internal.os;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.icu.impl.CacheValue;
import android.icu.text.DecimalFormatSymbols;
import android.icu.util.ULocale;
import android.opengl.EGL14;
import android.os.Build;
import android.os.Environment;
import android.os.IInstalld;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.ZygoteProcess;
import android.provider.SettingsStringUtil;
import android.security.keystore.AndroidKeyStoreProvider;
import android.service.notification.ZenModeConfig;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructCapUserData;
import android.system.StructCapUserHeader;
import android.text.Hyphenator;
import android.util.EventLog;
import android.util.Log;
import android.util.TimingsTraceLog;
import android.webkit.WebViewFactory;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.os.RuntimeInit;
import com.android.internal.os.ZygoteConnection;
import com.android.internal.util.Preconditions;
import com.xpeng.security.keystore.XpengAndroidKeyStoreProvider;
import dalvik.system.VMRuntime;
import dalvik.system.ZygoteHooks;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Provider;
import java.security.Security;
import libcore.io.IoUtils;
/* loaded from: classes3.dex */
public class ZygoteInit {
    private static final String ABI_LIST_ARG = "--abi-list=";
    private static final int LOG_BOOT_PROGRESS_PRELOAD_END = 3030;
    private static final int LOG_BOOT_PROGRESS_PRELOAD_START = 3020;
    private static final String PRELOADED_CLASSES = "/system/etc/preloaded-classes";
    private static final int PRELOAD_GC_THRESHOLD = 50000;
    public static final boolean PRELOAD_RESOURCES = true;
    private static final String PROPERTY_DISABLE_OPENGL_PRELOADING = "ro.zygote.disable_gl_preload";
    private static final String PROPERTY_GFX_DRIVER = "ro.gfx.driver.0";
    private static final int ROOT_GID = 0;
    private static final int ROOT_UID = 0;
    private static final String SOCKET_NAME_ARG = "--socket-name=";
    private static final String TAG = "Zygote";
    private static final int UNPRIVILEGED_GID = 9999;
    private static final int UNPRIVILEGED_UID = 9999;
    private static boolean mBootProfDisable = false;
    private static Resources mResources;
    private static boolean sPreloadComplete;

    private static native void nativePreloadAppProcessHALs();

    private static final native void nativeZygoteInit();

    private static void addBootEvent(String bootevent) {
        if (mBootProfDisable) {
            return;
        }
        FileOutputStream fos = null;
        try {
            try {
                try {
                    fos = new FileOutputStream("/proc/bootprof");
                    fos.write(bootevent.getBytes());
                    fos.flush();
                    fos.close();
                } catch (Throwable th) {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            Log.e("BOOTPROF", "Failure close /proc/bootprof entry", e);
                        }
                    }
                    throw th;
                }
            } catch (FileNotFoundException e2) {
                Log.e("BOOTPROF", "Failure open /proc/bootprof, not found!", e2);
                if (fos == null) {
                    return;
                }
                fos.close();
            } catch (IOException e3) {
                Log.e("BOOTPROF", "Failure open /proc/bootprof entry", e3);
                if (fos == null) {
                    return;
                }
                fos.close();
            }
        } catch (IOException e4) {
            Log.e("BOOTPROF", "Failure close /proc/bootprof entry", e4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void preload(TimingsTraceLog bootTimingsTraceLog) {
        Log.d(TAG, "begin preload");
        bootTimingsTraceLog.traceBegin("BeginIcuCachePinning");
        beginIcuCachePinning();
        bootTimingsTraceLog.traceEnd();
        bootTimingsTraceLog.traceBegin("PreloadClasses");
        preloadClasses();
        bootTimingsTraceLog.traceEnd();
        bootTimingsTraceLog.traceBegin("PreloadResources");
        preloadResources();
        bootTimingsTraceLog.traceEnd();
        Trace.traceBegin(16384L, "PreloadAppProcessHALs");
        nativePreloadAppProcessHALs();
        Trace.traceEnd(16384L);
        Trace.traceBegin(16384L, "PreloadOpenGL");
        preloadOpenGL();
        Trace.traceEnd(16384L);
        preloadSharedLibraries();
        preloadTextResources();
        WebViewFactory.prepareWebViewInZygote();
        endIcuCachePinning();
        warmUpJcaProviders();
        Log.d(TAG, "end preload");
        sPreloadComplete = true;
    }

    public static void lazyPreload() {
        Preconditions.checkState(!sPreloadComplete);
        Log.i(TAG, "Lazily preloading resources.");
        preload(new TimingsTraceLog("ZygoteInitTiming_lazy", 16384L));
    }

    private static void beginIcuCachePinning() {
        Log.i(TAG, "Installing ICU cache reference pinning...");
        CacheValue.setStrength(CacheValue.Strength.STRONG);
        Log.i(TAG, "Preloading ICU data...");
        ULocale[] localesToPin = {ULocale.ROOT, ULocale.US, ULocale.getDefault()};
        for (ULocale uLocale : localesToPin) {
            new DecimalFormatSymbols(uLocale);
        }
    }

    private static void endIcuCachePinning() {
        CacheValue.setStrength(CacheValue.Strength.SOFT);
        Log.i(TAG, "Uninstalled ICU cache reference pinning...");
    }

    private static void preloadSharedLibraries() {
        Log.i(TAG, "Preloading shared libraries...");
        System.loadLibrary(ZenModeConfig.SYSTEM_AUTHORITY);
        System.loadLibrary("compiler_rt");
        System.loadLibrary("jnigraphics");
    }

    private static void preloadOpenGL() {
        String driverPackageName = SystemProperties.get(PROPERTY_GFX_DRIVER);
        if (!SystemProperties.getBoolean(PROPERTY_DISABLE_OPENGL_PRELOADING, false)) {
            if (driverPackageName == null || driverPackageName.isEmpty()) {
                EGL14.eglGetDisplay(0);
            }
        }
    }

    private static void preloadTextResources() {
        Hyphenator.init();
        TextView.preloadFontCache();
    }

    private static void warmUpJcaProviders() {
        Provider[] providers;
        long startTime = SystemClock.uptimeMillis();
        Trace.traceBegin(16384L, "Starting installation of AndroidKeyStoreProvider");
        AndroidKeyStoreProvider.install();
        Log.i(TAG, "Installed AndroidKeyStoreProvider in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
        Trace.traceEnd(16384L);
        long startTime2 = SystemClock.uptimeMillis();
        Trace.traceBegin(16384L, "Starting installation of XpengAndroidKeyStoreProvider");
        XpengAndroidKeyStoreProvider.install();
        Log.i(TAG, "Installed XpengAndroidKeyStoreProvider in " + (SystemClock.uptimeMillis() - startTime2) + "ms.");
        Trace.traceEnd(16384L);
        long startTime3 = SystemClock.uptimeMillis();
        Trace.traceBegin(16384L, "Starting warm up of JCA providers");
        for (Provider p : Security.getProviders()) {
            p.warmUpServiceProvision();
        }
        Log.i(TAG, "Warmed up JCA providers in " + (SystemClock.uptimeMillis() - startTime3) + "ms.");
        Trace.traceEnd(16384L);
    }

    private static void preloadClasses() {
        StringBuilder sb;
        VMRuntime runtime = VMRuntime.getRuntime();
        try {
            InputStream is = new FileInputStream(PRELOADED_CLASSES);
            Log.i(TAG, "Preloading classes...");
            long startTime = SystemClock.uptimeMillis();
            int reuid = Os.getuid();
            int regid = Os.getgid();
            boolean droppedPriviliges = false;
            if (reuid == 0 && regid == 0) {
                try {
                    Os.setregid(0, Process.NOBODY_UID);
                    Os.setreuid(0, Process.NOBODY_UID);
                    droppedPriviliges = true;
                } catch (ErrnoException ex) {
                    throw new RuntimeException("Failed to drop root", ex);
                }
            }
            float defaultUtilization = runtime.getTargetHeapUtilization();
            runtime.setTargetHeapUtilization(0.8f);
            int count = 0;
            long j = 16384;
            try {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is), 256);
                    while (true) {
                        BufferedReader br2 = br;
                        String line = br2.readLine();
                        if (line == null) {
                            break;
                        }
                        String line2 = line.trim();
                        if (!line2.startsWith("#") && !line2.equals("")) {
                            Trace.traceBegin(j, line2);
                            try {
                                Class.forName(line2, true, null);
                                count++;
                            } catch (ClassNotFoundException e) {
                                Log.w(TAG, "Class not found for preloading: " + line2);
                            } catch (UnsatisfiedLinkError e2) {
                                Log.w(TAG, "Problem preloading " + line2 + ": " + e2);
                            } catch (Throwable t) {
                                Log.e(TAG, "Error preloading " + line2 + ".", t);
                                if (t instanceof Error) {
                                    throw ((Error) t);
                                }
                                if (!(t instanceof RuntimeException)) {
                                    throw new RuntimeException(t);
                                }
                                throw ((RuntimeException) t);
                            }
                            Trace.traceEnd(16384L);
                        }
                        br = br2;
                        j = 16384;
                    }
                    Log.i(TAG, "...preloaded " + count + " classes in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
                    IoUtils.closeQuietly(is);
                    runtime.setTargetHeapUtilization(defaultUtilization);
                    Trace.traceBegin(16384L, "PreloadDexCaches");
                    runtime.preloadDexCaches();
                    Trace.traceEnd(16384L);
                    if (droppedPriviliges) {
                        try {
                            Os.setreuid(0, 0);
                            Os.setregid(0, 0);
                        } catch (ErrnoException ex2) {
                            throw new RuntimeException("Failed to restore root", ex2);
                        }
                    }
                    sb = new StringBuilder();
                } catch (IOException e3) {
                    Log.e(TAG, "Error reading /system/etc/preloaded-classes.", e3);
                    IoUtils.closeQuietly(is);
                    runtime.setTargetHeapUtilization(defaultUtilization);
                    Trace.traceBegin(16384L, "PreloadDexCaches");
                    runtime.preloadDexCaches();
                    Trace.traceEnd(16384L);
                    if (droppedPriviliges) {
                        try {
                            Os.setreuid(0, 0);
                            Os.setregid(0, 0);
                        } catch (ErrnoException ex3) {
                            throw new RuntimeException("Failed to restore root", ex3);
                        }
                    }
                    sb = new StringBuilder();
                }
                sb.append("ZygoteInit: Preload ");
                sb.append(count);
                sb.append(" classes in ");
                sb.append(SystemClock.uptimeMillis() - startTime);
                sb.append("ms");
                addBootEvent(sb.toString());
            } catch (Throwable th) {
                IoUtils.closeQuietly(is);
                runtime.setTargetHeapUtilization(defaultUtilization);
                Trace.traceBegin(16384L, "PreloadDexCaches");
                runtime.preloadDexCaches();
                Trace.traceEnd(16384L);
                if (droppedPriviliges) {
                    try {
                        Os.setreuid(0, 0);
                        Os.setregid(0, 0);
                    } catch (ErrnoException ex4) {
                        throw new RuntimeException("Failed to restore root", ex4);
                    }
                }
                addBootEvent("ZygoteInit: Preload 0 classes in " + (SystemClock.uptimeMillis() - startTime) + "ms");
                throw th;
            }
        } catch (FileNotFoundException e4) {
            Log.e(TAG, "Couldn't find /system/etc/preloaded-classes.");
        }
    }

    private static void preloadResources() {
        VMRuntime.getRuntime();
        try {
            mResources = Resources.getSystem();
            mResources.startPreloading();
            Log.i(TAG, "Preloading resources...");
            long startTime = SystemClock.uptimeMillis();
            TypedArray ar = mResources.obtainTypedArray(R.array.preloaded_drawables);
            int N = preloadDrawables(ar);
            ar.recycle();
            Log.i(TAG, "...preloaded " + N + " resources in " + (SystemClock.uptimeMillis() - startTime) + "ms.");
            addBootEvent("ZygoteInit: preload " + N + " resources in " + (SystemClock.uptimeMillis() - startTime) + "ms");
            long startTime2 = SystemClock.uptimeMillis();
            TypedArray ar2 = mResources.obtainTypedArray(R.array.preloaded_color_state_lists);
            int N2 = preloadColorStateLists(ar2);
            ar2.recycle();
            Log.i(TAG, "...preloaded " + N2 + " resources in " + (SystemClock.uptimeMillis() - startTime2) + "ms.");
            addBootEvent("ZygoteInit: preload " + N2 + " resources in " + (SystemClock.uptimeMillis() - startTime2) + "ms");
            if (mResources.getBoolean(R.bool.config_freeformWindowManagement)) {
                long startTime3 = SystemClock.uptimeMillis();
                TypedArray ar3 = mResources.obtainTypedArray(R.array.preloaded_freeform_multi_window_drawables);
                int N3 = preloadDrawables(ar3);
                ar3.recycle();
                Log.i(TAG, "...preloaded " + N3 + " resource in " + (SystemClock.uptimeMillis() - startTime3) + "ms.");
                addBootEvent("ZygoteInit: preload " + N3 + " resources in " + (SystemClock.uptimeMillis() - startTime3) + "ms");
            }
            mResources.finishPreloading();
        } catch (RuntimeException e) {
            Log.w(TAG, "Failure preloading resources", e);
        }
    }

    private static int preloadColorStateLists(TypedArray ar) {
        int N = ar.length();
        for (int i = 0; i < N; i++) {
            int id = ar.getResourceId(i, 0);
            if (id != 0 && mResources.getColorStateList(id, null) == null) {
                throw new IllegalArgumentException("Unable to find preloaded color resource #0x" + Integer.toHexString(id) + " (" + ar.getString(i) + ")");
            }
        }
        return N;
    }

    private static int preloadDrawables(TypedArray ar) {
        int N = ar.length();
        for (int i = 0; i < N; i++) {
            int id = ar.getResourceId(i, 0);
            if (id != 0 && mResources.getDrawable(id, null) == null) {
                throw new IllegalArgumentException("Unable to find preloaded drawable resource #0x" + Integer.toHexString(id) + " (" + ar.getString(i) + ")");
            }
        }
        return N;
    }

    static void gcAndFinalize() {
        VMRuntime runtime = VMRuntime.getRuntime();
        System.gc();
        runtime.runFinalizationSync();
        System.gc();
    }

    private static Runnable handleSystemServerProcess(ZygoteConnection.Arguments parsedArgs) {
        Os.umask(OsConstants.S_IRWXG | OsConstants.S_IRWXO);
        if (parsedArgs.niceName != null) {
            Process.setArgV0(parsedArgs.niceName);
        }
        String systemServerClasspath = Os.getenv("SYSTEMSERVERCLASSPATH");
        if (systemServerClasspath != null) {
            performSystemServerDexOpt(systemServerClasspath);
            boolean profileSystemServer = SystemProperties.getBoolean("dalvik.vm.profilesystemserver", false);
            if (profileSystemServer && (Build.IS_USERDEBUG || Build.IS_ENG)) {
                try {
                    prepareSystemServerProfile(systemServerClasspath);
                } catch (Exception e) {
                    Log.wtf(TAG, "Failed to set up system server profile", e);
                }
            }
        }
        if (parsedArgs.invokeWith != null) {
            String[] args = parsedArgs.remainingArgs;
            if (systemServerClasspath != null) {
                String[] amendedArgs = new String[args.length + 2];
                amendedArgs[0] = "-cp";
                amendedArgs[1] = systemServerClasspath;
                System.arraycopy(args, 0, amendedArgs, 2, args.length);
                args = amendedArgs;
            }
            WrapperInit.execApplication(parsedArgs.invokeWith, parsedArgs.niceName, parsedArgs.targetSdkVersion, VMRuntime.getCurrentInstructionSet(), null, args);
            throw new IllegalStateException("Unexpected return from WrapperInit.execApplication");
        }
        ClassLoader cl = null;
        if (systemServerClasspath != null) {
            cl = createPathClassLoader(systemServerClasspath, parsedArgs.targetSdkVersion);
            Thread.currentThread().setContextClassLoader(cl);
        }
        return zygoteInit(parsedArgs.targetSdkVersion, parsedArgs.remainingArgs, cl);
    }

    private static void prepareSystemServerProfile(String systemServerClasspath) throws RemoteException {
        if (systemServerClasspath.isEmpty()) {
            return;
        }
        String[] codePaths = systemServerClasspath.split(SettingsStringUtil.DELIMITER);
        IInstalld installd = IInstalld.Stub.asInterface(ServiceManager.getService("installd"));
        installd.prepareAppProfile(ZenModeConfig.SYSTEM_AUTHORITY, 0, UserHandle.getAppId(1000), "primary.prof", codePaths[0], null);
        File profileDir = Environment.getDataProfilesDePackageDirectory(0, ZenModeConfig.SYSTEM_AUTHORITY);
        String profilePath = new File(profileDir, "primary.prof").getAbsolutePath();
        VMRuntime.registerAppInfo(profilePath, codePaths);
    }

    public static void setApiBlacklistExemptions(String[] exemptions) {
        VMRuntime.getRuntime().setHiddenApiExemptions(exemptions);
    }

    public static void setHiddenApiAccessLogSampleRate(int percent) {
        VMRuntime.getRuntime().setHiddenApiAccessLogSamplingRate(percent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ClassLoader createPathClassLoader(String classPath, int targetSdkVersion) {
        String libraryPath = System.getProperty("java.library.path");
        return ClassLoaderFactory.createClassLoader(classPath, libraryPath, libraryPath, ClassLoader.getSystemClassLoader(), targetSdkVersion, true, null);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:18:0x009e
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    private static void performSystemServerDexOpt(java.lang.String r36) {
        /*
            Method dump skipped, instructions count: 247
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteInit.performSystemServerDexOpt(java.lang.String):void");
    }

    private static String getSystemServerClassLoaderContext(String classPath) {
        if (classPath == null) {
            return "PCL[]";
        }
        return "PCL[" + classPath + "]";
    }

    private static String encodeSystemServerClassPath(String classPath, String newElement) {
        if (classPath == null || classPath.isEmpty()) {
            return newElement;
        }
        return classPath + SettingsStringUtil.DELIMITER + newElement;
    }

    private static Runnable forkSystemServer(String abiList, String socketName, ZygoteServer zygoteServer) {
        ZygoteConnection.Arguments parsedArgs;
        long capabilities = posixCapabilitiesAsBits(OsConstants.CAP_IPC_LOCK, OsConstants.CAP_KILL, OsConstants.CAP_NET_ADMIN, OsConstants.CAP_NET_BIND_SERVICE, OsConstants.CAP_NET_BROADCAST, OsConstants.CAP_NET_RAW, OsConstants.CAP_SYS_MODULE, OsConstants.CAP_SYS_NICE, OsConstants.CAP_SYS_PTRACE, OsConstants.CAP_SYS_TIME, OsConstants.CAP_SYS_TTY_CONFIG, OsConstants.CAP_WAKE_ALARM, OsConstants.CAP_BLOCK_SUSPEND);
        StructCapUserHeader header = new StructCapUserHeader(OsConstants._LINUX_CAPABILITY_VERSION_3, 0);
        try {
            StructCapUserData[] data = Os.capget(header);
            long capabilities2 = ((data[1].effective << 32) | data[0].effective) & capabilities;
            String[] args = {"--setuid=1000", "--setgid=1000", "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,1021,1023,1024,1032,1065,3001,3002,3003,3006,3007,3009,3010", "--capabilities=" + capabilities2 + "," + capabilities2, "--nice-name=system_server", "--runtime-args", "--target-sdk-version=10000", "com.android.server.SystemServer"};
            try {
                parsedArgs = new ZygoteConnection.Arguments(args);
                ZygoteConnection.applyDebuggerSystemProperty(parsedArgs);
                ZygoteConnection.applyInvokeWithSystemProperty(parsedArgs);
                boolean profileSystemServer = SystemProperties.getBoolean("dalvik.vm.profilesystemserver", false);
                if (profileSystemServer) {
                    try {
                        parsedArgs.runtimeFlags |= 16384;
                    } catch (IllegalArgumentException e) {
                        ex = e;
                        throw new RuntimeException(ex);
                    }
                }
            } catch (IllegalArgumentException e2) {
                ex = e2;
            }
            try {
                int pid = Zygote.forkSystemServer(parsedArgs.uid, parsedArgs.gid, parsedArgs.gids, parsedArgs.runtimeFlags, null, parsedArgs.permittedCapabilities, parsedArgs.effectiveCapabilities);
                if (pid == 0) {
                    if (hasSecondZygote(abiList)) {
                        waitForSecondaryZygote(socketName);
                    }
                    zygoteServer.closeServerSocket();
                    return handleSystemServerProcess(parsedArgs);
                }
                return null;
            } catch (IllegalArgumentException e3) {
                ex = e3;
                throw new RuntimeException(ex);
            }
        } catch (ErrnoException ex) {
            throw new RuntimeException("Failed to capget()", ex);
        }
    }

    private static long posixCapabilitiesAsBits(int... capabilities) {
        long result = 0;
        for (int capability : capabilities) {
            if (capability < 0 || capability > OsConstants.CAP_LAST_CAP) {
                throw new IllegalArgumentException(String.valueOf(capability));
            }
            result |= 1 << capability;
        }
        return result;
    }

    public static void main(String[] argv) {
        Runnable r;
        ZygoteServer zygoteServer = new ZygoteServer();
        ZygoteHooks.startZygoteNoThreadCreation();
        try {
            Os.setpgid(0, 0);
            try {
                if (!"1".equals(SystemProperties.get("sys.boot_completed"))) {
                    MetricsLogger.histogram(null, "boot_zygote_init", (int) SystemClock.elapsedRealtime());
                }
                mBootProfDisable = "1".equals(SystemProperties.get("ro.bootprof.disable", "0"));
                String bootTimeTag = Process.is64Bit() ? "Zygote64Timing" : "Zygote32Timing";
                TimingsTraceLog bootTimingsTraceLog = new TimingsTraceLog(bootTimeTag, 16384L);
                bootTimingsTraceLog.traceBegin("ZygoteInit");
                addBootEvent("ZygoteInit: start");
                RuntimeInit.enableDdms();
                boolean startSystemServer = false;
                String socketName = Process.ZYGOTE_SOCKET;
                String abiList = null;
                boolean enableLazyPreload = false;
                for (int i = 1; i < argv.length; i++) {
                    if ("start-system-server".equals(argv[i])) {
                        startSystemServer = true;
                    } else if ("--enable-lazy-preload".equals(argv[i])) {
                        enableLazyPreload = true;
                    } else if (argv[i].startsWith(ABI_LIST_ARG)) {
                        abiList = argv[i].substring(ABI_LIST_ARG.length());
                    } else if (!argv[i].startsWith(SOCKET_NAME_ARG)) {
                        throw new RuntimeException("Unknown command line argument: " + argv[i]);
                    } else {
                        socketName = argv[i].substring(SOCKET_NAME_ARG.length());
                    }
                }
                if (abiList == null) {
                    throw new RuntimeException("No ABI list supplied.");
                }
                zygoteServer.registerServerSocketFromEnv(socketName);
                if (enableLazyPreload) {
                    Zygote.resetNicePriority();
                } else {
                    bootTimingsTraceLog.traceBegin("ZygotePreload");
                    EventLog.writeEvent((int) LOG_BOOT_PROGRESS_PRELOAD_START, SystemClock.uptimeMillis());
                    addBootEvent("ZygoteInit: boot_progress_preload_start");
                    preload(bootTimingsTraceLog);
                    EventLog.writeEvent((int) LOG_BOOT_PROGRESS_PRELOAD_END, SystemClock.uptimeMillis());
                    addBootEvent("ZygoteInit: boot_progress_preload_end");
                    bootTimingsTraceLog.traceEnd();
                }
                bootTimingsTraceLog.traceBegin("PostZygoteInitGC");
                gcAndFinalize();
                bootTimingsTraceLog.traceEnd();
                addBootEvent("ZygoteInit: End");
                bootTimingsTraceLog.traceEnd();
                Trace.setTracingEnabled(false, 0);
                Zygote.nativeSecurityInit();
                Zygote.nativeUnmountStorageOnInit();
                ZygoteHooks.stopZygoteNoThreadCreation();
                if (startSystemServer && (r = forkSystemServer(abiList, socketName, zygoteServer)) != null) {
                    r.run();
                    return;
                }
                Log.i(TAG, "Accepting command socket connections");
                Runnable caller = zygoteServer.runSelectLoop(abiList);
                if (caller != null) {
                    caller.run();
                }
            } catch (Throwable ex) {
                try {
                    Log.e(TAG, "System zygote died with exception", ex);
                    throw ex;
                } finally {
                    zygoteServer.closeServerSocket();
                }
            }
        } catch (ErrnoException ex2) {
            throw new RuntimeException("Failed to setpgid(0,0)", ex2);
        }
    }

    private static boolean hasSecondZygote(String abiList) {
        return !SystemProperties.get("ro.product.cpu.abilist").equals(abiList);
    }

    private static void waitForSecondaryZygote(String socketName) {
        String otherZygoteName = Process.ZYGOTE_SOCKET.equals(socketName) ? Process.SECONDARY_ZYGOTE_SOCKET : Process.ZYGOTE_SOCKET;
        ZygoteProcess.waitForConnectionToZygote(otherZygoteName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isPreloadComplete() {
        return sPreloadComplete;
    }

    private ZygoteInit() {
    }

    public static final Runnable zygoteInit(int targetSdkVersion, String[] argv, ClassLoader classLoader) {
        Trace.traceBegin(64L, "ZygoteInit");
        RuntimeInit.redirectLogStreams();
        RuntimeInit.commonInit();
        nativeZygoteInit();
        return RuntimeInit.applicationInit(targetSdkVersion, argv, classLoader);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Runnable childZygoteInit(int targetSdkVersion, String[] argv, ClassLoader classLoader) {
        RuntimeInit.Arguments args = new RuntimeInit.Arguments(argv);
        return RuntimeInit.findStaticMain(args.startClass, args.startArgs, classLoader);
    }
}
