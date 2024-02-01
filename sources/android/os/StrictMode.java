package android.os;

import android.animation.ValueAnimator;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.INetworkManagementService;
import android.os.MessageQueue;
import android.os.Parcelable;
import android.os.StrictMode;
import android.os.storage.IStorageManager;
import android.os.strictmode.CleartextNetworkViolation;
import android.os.strictmode.ContentUriWithoutPermissionViolation;
import android.os.strictmode.CredentialProtectedWhileLockedViolation;
import android.os.strictmode.CustomViolation;
import android.os.strictmode.DiskReadViolation;
import android.os.strictmode.DiskWriteViolation;
import android.os.strictmode.ExplicitGcViolation;
import android.os.strictmode.FileUriExposedViolation;
import android.os.strictmode.ImplicitDirectBootViolation;
import android.os.strictmode.InstanceCountViolation;
import android.os.strictmode.IntentReceiverLeakedViolation;
import android.os.strictmode.LeakedClosableViolation;
import android.os.strictmode.NetworkViolation;
import android.os.strictmode.NonSdkApiUsedViolation;
import android.os.strictmode.ResourceMismatchViolation;
import android.os.strictmode.ServiceConnectionLeakedViolation;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.os.strictmode.UnbufferedIoViolation;
import android.os.strictmode.UntaggedSocketViolation;
import android.os.strictmode.Violation;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Printer;
import android.util.Singleton;
import android.view.IWindowManager;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.RuntimeInit;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.HexDump;
import dalvik.system.BlockGuard;
import dalvik.system.CloseGuard;
import dalvik.system.VMDebug;
import dalvik.system.VMRuntime;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public final class StrictMode {
    private static final String CLEARTEXT_PROPERTY = "persist.sys.strictmode.clear";
    private static final int DETECT_THREAD_ALL = 65535;
    private static final int DETECT_THREAD_CUSTOM = 8;
    private static final int DETECT_THREAD_DISK_READ = 2;
    private static final int DETECT_THREAD_DISK_WRITE = 1;
    private static final int DETECT_THREAD_EXPLICIT_GC = 64;
    private static final int DETECT_THREAD_NETWORK = 4;
    private static final int DETECT_THREAD_RESOURCE_MISMATCH = 16;
    private static final int DETECT_THREAD_UNBUFFERED_IO = 32;
    private static final int DETECT_VM_ACTIVITY_LEAKS = 4;
    private static final int DETECT_VM_ALL = 65535;
    private static final int DETECT_VM_CLEARTEXT_NETWORK = 64;
    private static final int DETECT_VM_CLOSABLE_LEAKS = 2;
    private static final int DETECT_VM_CONTENT_URI_WITHOUT_PERMISSION = 128;
    private static final int DETECT_VM_CREDENTIAL_PROTECTED_WHILE_LOCKED = 2048;
    private static final int DETECT_VM_CURSOR_LEAKS = 1;
    private static final int DETECT_VM_FILE_URI_EXPOSURE = 32;
    private static final int DETECT_VM_IMPLICIT_DIRECT_BOOT = 1024;
    private static final int DETECT_VM_INSTANCE_LEAKS = 8;
    private static final int DETECT_VM_NON_SDK_API_USAGE = 512;
    private static final int DETECT_VM_REGISTRATION_LEAKS = 16;
    private static final int DETECT_VM_UNTAGGED_SOCKET = 256;
    private static final boolean DISABLE = false;
    public static final String DISABLE_PROPERTY = "persist.sys.strictmode.disable";
    private static final int MAX_OFFENSES_PER_LOOP = 10;
    private static final int MAX_SPAN_TAGS = 20;
    private static final long MIN_DIALOG_INTERVAL_MS = 30000;
    private static final long MIN_LOG_INTERVAL_MS = 1000;
    private static final long MIN_VM_INTERVAL_MS = 1000;
    public static final int NETWORK_POLICY_ACCEPT = 0;
    public static final int NETWORK_POLICY_LOG = 1;
    public static final int NETWORK_POLICY_REJECT = 2;
    public static final int PENALTY_ALL = -65536;
    public static final int PENALTY_DEATH = 268435456;
    public static final int PENALTY_DEATH_ON_CLEARTEXT_NETWORK = 16777216;
    public static final int PENALTY_DEATH_ON_FILE_URI_EXPOSURE = 8388608;
    public static final int PENALTY_DEATH_ON_NETWORK = 33554432;
    public static final int PENALTY_DIALOG = 536870912;
    public static final int PENALTY_DROPBOX = 67108864;
    public static final int PENALTY_FLASH = 134217728;
    public static final int PENALTY_GATHER = Integer.MIN_VALUE;
    public static final int PENALTY_LOG = 1073741824;
    public static final String VISUAL_PROPERTY = "persist.sys.strictmode.visual";
    private static final String TAG = "StrictMode";
    private static final boolean LOG_V = Log.isLoggable(TAG, 2);
    private static final HashMap<Class, Integer> EMPTY_CLASS_LIMIT_MAP = new HashMap<>();
    private static volatile VmPolicy sVmPolicy = VmPolicy.LAX;
    private static final ViolationLogger LOGCAT_LOGGER = new ViolationLogger() { // from class: android.os.-$$Lambda$StrictMode$1yH8AK0bTwVwZOb9x8HoiSBdzr0
        @Override // android.os.StrictMode.ViolationLogger
        public final void log(StrictMode.ViolationInfo violationInfo) {
            StrictMode.lambda$static$0(violationInfo);
        }
    };
    private static volatile ViolationLogger sLogger = LOGCAT_LOGGER;
    private static final ThreadLocal<OnThreadViolationListener> sThreadViolationListener = new ThreadLocal<>();
    private static final ThreadLocal<Executor> sThreadViolationExecutor = new ThreadLocal<>();
    private static final AtomicInteger sDropboxCallsInFlight = new AtomicInteger(0);
    private static final Consumer<String> sNonSdkApiUsageConsumer = new Consumer() { // from class: android.os.-$$Lambda$StrictMode$lu9ekkHJ2HMz0jd3F8K8MnhenxQ
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            StrictMode.onVmPolicyViolation(new NonSdkApiUsedViolation((String) obj));
        }
    };
    private static final ThreadLocal<ArrayList<ViolationInfo>> gatheredViolations = new ThreadLocal<ArrayList<ViolationInfo>>() { // from class: android.os.StrictMode.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public ArrayList<ViolationInfo> initialValue() {
            return null;
        }
    };
    @UnsupportedAppUsage
    private static final ThreadLocal<ArrayList<ViolationInfo>> violationsBeingTimed = new ThreadLocal<ArrayList<ViolationInfo>>() { // from class: android.os.StrictMode.2
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public ArrayList<ViolationInfo> initialValue() {
            return new ArrayList<>();
        }
    };
    private static final ThreadLocal<Handler> THREAD_HANDLER = new ThreadLocal<Handler>() { // from class: android.os.StrictMode.3
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Handler initialValue() {
            return new Handler();
        }
    };
    private static final ThreadLocal<AndroidBlockGuardPolicy> THREAD_ANDROID_POLICY = new ThreadLocal<AndroidBlockGuardPolicy>() { // from class: android.os.StrictMode.4
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public AndroidBlockGuardPolicy initialValue() {
            return new AndroidBlockGuardPolicy(0);
        }
    };
    private static final BlockGuard.VmPolicy VM_ANDROID_POLICY = new BlockGuard.VmPolicy() { // from class: android.os.StrictMode.5
        public void onPathAccess(String path) {
            if (path == null) {
                return;
            }
            if (path.startsWith("/data/user/") || path.startsWith("/data/media/") || path.startsWith("/data/system_ce/") || path.startsWith("/data/misc_ce/") || path.startsWith("/data/vendor_ce/") || path.startsWith("/storage/emulated/")) {
                int second = path.indexOf(47, 1);
                int third = path.indexOf(47, second + 1);
                int fourth = path.indexOf(47, third + 1);
                if (fourth == -1) {
                    return;
                }
                try {
                    int userId = Integer.parseInt(path.substring(third + 1, fourth));
                    StrictMode.onCredentialProtectedPathAccess(path, userId);
                } catch (NumberFormatException e) {
                }
            } else if (path.startsWith("/data/data/")) {
                StrictMode.onCredentialProtectedPathAccess(path, 0);
            }
        }
    };
    private static long sLastInstanceCountCheckMillis = 0;
    private static boolean sIsIdlerRegistered = false;
    private static final MessageQueue.IdleHandler sProcessIdleHandler = new MessageQueue.IdleHandler() { // from class: android.os.StrictMode.6
        @Override // android.os.MessageQueue.IdleHandler
        public boolean queueIdle() {
            long now = SystemClock.uptimeMillis();
            if (now - StrictMode.sLastInstanceCountCheckMillis > 30000) {
                long unused = StrictMode.sLastInstanceCountCheckMillis = now;
                StrictMode.conditionallyCheckInstanceCounts();
                return true;
            }
            return true;
        }
    };
    private static volatile boolean sUserKeyUnlocked = false;
    @UnsupportedAppUsage
    private static final HashMap<Integer, Long> sLastVmViolationTime = new HashMap<>();
    private static final Span NO_OP_SPAN = new Span() { // from class: android.os.StrictMode.7
        @Override // android.os.StrictMode.Span
        public void finish() {
        }
    };
    private static final ThreadLocal<ThreadSpanState> sThisThreadSpanState = new ThreadLocal<ThreadSpanState>() { // from class: android.os.StrictMode.8
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public ThreadSpanState initialValue() {
            return new ThreadSpanState();
        }
    };
    @UnsupportedAppUsage
    private static Singleton<IWindowManager> sWindowManager = new Singleton<IWindowManager>() { // from class: android.os.StrictMode.9
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.util.Singleton
        public IWindowManager create() {
            return IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
        }
    };
    @GuardedBy({"StrictMode.class"})
    private static final HashMap<Class, Integer> sExpectedActivityInstanceCount = new HashMap<>();

    /* loaded from: classes2.dex */
    public interface OnThreadViolationListener {
        void onThreadViolation(Violation violation);
    }

    /* loaded from: classes2.dex */
    public interface OnVmViolationListener {
        void onVmViolation(Violation violation);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ThreadPolicyMask {
    }

    /* loaded from: classes2.dex */
    public interface ViolationLogger {
        void log(ViolationInfo violationInfo);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface VmPolicyMask {
    }

    static /* synthetic */ boolean access$400() {
        return tooManyViolationsThisLoop();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$static$0(ViolationInfo info) {
        String msg;
        if (info.durationMillis != -1) {
            msg = "StrictMode policy violation; ~duration=" + info.durationMillis + " ms:";
        } else {
            msg = "StrictMode policy violation:";
        }
        Log.d(TAG, msg + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + info.getStackTrace());
    }

    public static void setViolationLogger(ViolationLogger listener) {
        if (listener == null) {
            listener = LOGCAT_LOGGER;
        }
        sLogger = listener;
    }

    private StrictMode() {
    }

    /* loaded from: classes2.dex */
    public static final class ThreadPolicy {
        public static final ThreadPolicy LAX = new ThreadPolicy(0, null, null);
        final Executor mCallbackExecutor;
        final OnThreadViolationListener mListener;
        @UnsupportedAppUsage
        final int mask;

        private ThreadPolicy(int mask, OnThreadViolationListener listener, Executor executor) {
            this.mask = mask;
            this.mListener = listener;
            this.mCallbackExecutor = executor;
        }

        public String toString() {
            return "[StrictMode.ThreadPolicy; mask=" + this.mask + "]";
        }

        /* loaded from: classes2.dex */
        public static final class Builder {
            private Executor mExecutor;
            private OnThreadViolationListener mListener;
            private int mMask;

            public Builder() {
                this.mMask = 0;
                this.mMask = 0;
            }

            public Builder(ThreadPolicy policy) {
                this.mMask = 0;
                this.mMask = policy.mask;
                this.mListener = policy.mListener;
                this.mExecutor = policy.mCallbackExecutor;
            }

            public Builder detectAll() {
                detectDiskReads();
                detectDiskWrites();
                detectNetwork();
                int targetSdk = VMRuntime.getRuntime().getTargetSdkVersion();
                if (targetSdk >= 11) {
                    detectCustomSlowCalls();
                }
                if (targetSdk >= 23) {
                    detectResourceMismatches();
                }
                if (targetSdk >= 26) {
                    detectUnbufferedIo();
                }
                return this;
            }

            public Builder permitAll() {
                return disable(65535);
            }

            public Builder detectNetwork() {
                return enable(4);
            }

            public Builder permitNetwork() {
                return disable(4);
            }

            public Builder detectDiskReads() {
                return enable(2);
            }

            public Builder permitDiskReads() {
                return disable(2);
            }

            public Builder detectCustomSlowCalls() {
                return enable(8);
            }

            public Builder permitCustomSlowCalls() {
                return disable(8);
            }

            public Builder permitResourceMismatches() {
                return disable(16);
            }

            public Builder detectUnbufferedIo() {
                return enable(32);
            }

            public Builder permitUnbufferedIo() {
                return disable(32);
            }

            public Builder detectResourceMismatches() {
                return enable(16);
            }

            public Builder detectDiskWrites() {
                return enable(1);
            }

            public Builder permitDiskWrites() {
                return disable(1);
            }

            public Builder detectExplicitGc() {
                return enable(64);
            }

            public Builder permitExplicitGc() {
                return disable(64);
            }

            public Builder penaltyDialog() {
                return enable(536870912);
            }

            public Builder penaltyDeath() {
                return enable(268435456);
            }

            public Builder penaltyDeathOnNetwork() {
                return enable(33554432);
            }

            public Builder penaltyFlashScreen() {
                return enable(134217728);
            }

            public Builder penaltyLog() {
                return enable(1073741824);
            }

            public Builder penaltyDropBox() {
                return enable(67108864);
            }

            public Builder penaltyListener(Executor executor, OnThreadViolationListener listener) {
                if (executor == null) {
                    throw new NullPointerException("executor must not be null");
                }
                this.mListener = listener;
                this.mExecutor = executor;
                return this;
            }

            public Builder penaltyListener(OnThreadViolationListener listener, Executor executor) {
                return penaltyListener(executor, listener);
            }

            private Builder enable(int mask) {
                this.mMask |= mask;
                return this;
            }

            private Builder disable(int mask) {
                this.mMask &= ~mask;
                return this;
            }

            public ThreadPolicy build() {
                int i;
                if (this.mListener == null && (i = this.mMask) != 0 && (i & 1946157056) == 0) {
                    penaltyLog();
                }
                return new ThreadPolicy(this.mMask, this.mListener, this.mExecutor);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class VmPolicy {
        public static final VmPolicy LAX = new VmPolicy(0, StrictMode.EMPTY_CLASS_LIMIT_MAP, null, null);
        final HashMap<Class, Integer> classInstanceLimit;
        final Executor mCallbackExecutor;
        final OnVmViolationListener mListener;
        @UnsupportedAppUsage
        final int mask;

        private VmPolicy(int mask, HashMap<Class, Integer> classInstanceLimit, OnVmViolationListener listener, Executor executor) {
            if (classInstanceLimit == null) {
                throw new NullPointerException("classInstanceLimit == null");
            }
            this.mask = mask;
            this.classInstanceLimit = classInstanceLimit;
            this.mListener = listener;
            this.mCallbackExecutor = executor;
        }

        public String toString() {
            return "[StrictMode.VmPolicy; mask=" + this.mask + "]";
        }

        /* loaded from: classes2.dex */
        public static final class Builder {
            private HashMap<Class, Integer> mClassInstanceLimit;
            private boolean mClassInstanceLimitNeedCow;
            private Executor mExecutor;
            private OnVmViolationListener mListener;
            @UnsupportedAppUsage
            private int mMask;

            public Builder() {
                this.mClassInstanceLimitNeedCow = false;
                this.mMask = 0;
            }

            public Builder(VmPolicy base) {
                this.mClassInstanceLimitNeedCow = false;
                this.mMask = base.mask;
                this.mClassInstanceLimitNeedCow = true;
                this.mClassInstanceLimit = base.classInstanceLimit;
                this.mListener = base.mListener;
                this.mExecutor = base.mCallbackExecutor;
            }

            public Builder setClassInstanceLimit(Class klass, int instanceLimit) {
                if (klass == null) {
                    throw new NullPointerException("klass == null");
                }
                if (this.mClassInstanceLimitNeedCow) {
                    if (this.mClassInstanceLimit.containsKey(klass) && this.mClassInstanceLimit.get(klass).intValue() == instanceLimit) {
                        return this;
                    }
                    this.mClassInstanceLimitNeedCow = false;
                    this.mClassInstanceLimit = (HashMap) this.mClassInstanceLimit.clone();
                } else if (this.mClassInstanceLimit == null) {
                    this.mClassInstanceLimit = new HashMap<>();
                }
                this.mMask |= 8;
                this.mClassInstanceLimit.put(klass, Integer.valueOf(instanceLimit));
                return this;
            }

            public Builder detectActivityLeaks() {
                return enable(4);
            }

            public Builder permitActivityLeaks() {
                return disable(4);
            }

            public Builder detectNonSdkApiUsage() {
                return enable(512);
            }

            public Builder permitNonSdkApiUsage() {
                return disable(512);
            }

            public Builder detectAll() {
                detectLeakedSqlLiteObjects();
                int targetSdk = VMRuntime.getRuntime().getTargetSdkVersion();
                if (targetSdk >= 11) {
                    detectActivityLeaks();
                    detectLeakedClosableObjects();
                }
                if (targetSdk >= 16) {
                    detectLeakedRegistrationObjects();
                }
                if (targetSdk >= 18) {
                    detectFileUriExposure();
                }
                if (targetSdk >= 23 && SystemProperties.getBoolean(StrictMode.CLEARTEXT_PROPERTY, false)) {
                    detectCleartextNetwork();
                }
                if (targetSdk >= 26) {
                    detectContentUriWithoutPermission();
                    detectUntaggedSockets();
                }
                if (targetSdk >= 29) {
                    detectCredentialProtectedWhileLocked();
                }
                return this;
            }

            public Builder detectLeakedSqlLiteObjects() {
                return enable(1);
            }

            public Builder detectLeakedClosableObjects() {
                return enable(2);
            }

            public Builder detectLeakedRegistrationObjects() {
                return enable(16);
            }

            public Builder detectFileUriExposure() {
                return enable(32);
            }

            public Builder detectCleartextNetwork() {
                return enable(64);
            }

            public Builder detectContentUriWithoutPermission() {
                return enable(128);
            }

            public Builder detectUntaggedSockets() {
                return enable(256);
            }

            public Builder permitUntaggedSockets() {
                return disable(256);
            }

            public Builder detectImplicitDirectBoot() {
                return enable(1024);
            }

            public Builder permitImplicitDirectBoot() {
                return disable(1024);
            }

            public Builder detectCredentialProtectedWhileLocked() {
                return enable(2048);
            }

            public Builder permitCredentialProtectedWhileLocked() {
                return disable(2048);
            }

            public Builder penaltyDeath() {
                return enable(268435456);
            }

            public Builder penaltyDeathOnCleartextNetwork() {
                return enable(16777216);
            }

            public Builder penaltyDeathOnFileUriExposure() {
                return enable(8388608);
            }

            public Builder penaltyLog() {
                return enable(1073741824);
            }

            public Builder penaltyDropBox() {
                return enable(67108864);
            }

            public Builder penaltyListener(Executor executor, OnVmViolationListener listener) {
                if (executor == null) {
                    throw new NullPointerException("executor must not be null");
                }
                this.mListener = listener;
                this.mExecutor = executor;
                return this;
            }

            public Builder penaltyListener(OnVmViolationListener listener, Executor executor) {
                return penaltyListener(executor, listener);
            }

            private Builder enable(int mask) {
                this.mMask |= mask;
                return this;
            }

            Builder disable(int mask) {
                this.mMask &= ~mask;
                return this;
            }

            public VmPolicy build() {
                int i;
                if (this.mListener == null && (i = this.mMask) != 0 && (i & 1946157056) == 0) {
                    penaltyLog();
                }
                int i2 = this.mMask;
                HashMap<Class, Integer> hashMap = this.mClassInstanceLimit;
                if (hashMap == null) {
                    hashMap = StrictMode.EMPTY_CLASS_LIMIT_MAP;
                }
                return new VmPolicy(i2, hashMap, this.mListener, this.mExecutor);
            }
        }
    }

    public static void setThreadPolicy(ThreadPolicy policy) {
        setThreadPolicyMask(policy.mask);
        sThreadViolationListener.set(policy.mListener);
        sThreadViolationExecutor.set(policy.mCallbackExecutor);
    }

    public static void setThreadPolicyMask(int threadPolicyMask) {
        setBlockGuardPolicy(threadPolicyMask);
        Binder.setThreadStrictModePolicy(threadPolicyMask);
    }

    private static void setBlockGuardPolicy(int threadPolicyMask) {
        AndroidBlockGuardPolicy androidPolicy;
        if (threadPolicyMask == 0) {
            BlockGuard.setThreadPolicy(BlockGuard.LAX_POLICY);
            return;
        }
        BlockGuard.Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            androidPolicy = (AndroidBlockGuardPolicy) policy;
        } else {
            androidPolicy = THREAD_ANDROID_POLICY.get();
            BlockGuard.setThreadPolicy(androidPolicy);
        }
        androidPolicy.setThreadPolicyMask(threadPolicyMask);
    }

    private static void setBlockGuardVmPolicy(int vmPolicyMask) {
        if ((vmPolicyMask & 2048) != 0) {
            BlockGuard.setVmPolicy(VM_ANDROID_POLICY);
        } else {
            BlockGuard.setVmPolicy(BlockGuard.LAX_VM_POLICY);
        }
    }

    private static void setCloseGuardEnabled(boolean enabled) {
        if (!(CloseGuard.getReporter() instanceof AndroidCloseGuardReporter)) {
            CloseGuard.setReporter(new AndroidCloseGuardReporter());
        }
        CloseGuard.setEnabled(enabled);
    }

    @UnsupportedAppUsage
    public static int getThreadPolicyMask() {
        BlockGuard.Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            return ((AndroidBlockGuardPolicy) policy).getThreadPolicyMask();
        }
        return 0;
    }

    public static ThreadPolicy getThreadPolicy() {
        return new ThreadPolicy(getThreadPolicyMask(), sThreadViolationListener.get(), sThreadViolationExecutor.get());
    }

    public static ThreadPolicy allowThreadDiskWrites() {
        return new ThreadPolicy(allowThreadDiskWritesMask(), sThreadViolationListener.get(), sThreadViolationExecutor.get());
    }

    public static int allowThreadDiskWritesMask() {
        int oldPolicyMask = getThreadPolicyMask();
        int newPolicyMask = oldPolicyMask & (-4);
        if (newPolicyMask != oldPolicyMask) {
            setThreadPolicyMask(newPolicyMask);
        }
        return oldPolicyMask;
    }

    public static ThreadPolicy allowThreadDiskReads() {
        return new ThreadPolicy(allowThreadDiskReadsMask(), sThreadViolationListener.get(), sThreadViolationExecutor.get());
    }

    public static int allowThreadDiskReadsMask() {
        int oldPolicyMask = getThreadPolicyMask();
        int newPolicyMask = oldPolicyMask & (-3);
        if (newPolicyMask != oldPolicyMask) {
            setThreadPolicyMask(newPolicyMask);
        }
        return oldPolicyMask;
    }

    public static ThreadPolicy allowThreadViolations() {
        ThreadPolicy oldPolicy = getThreadPolicy();
        setThreadPolicyMask(0);
        return oldPolicy;
    }

    public static VmPolicy allowVmViolations() {
        VmPolicy oldPolicy = getVmPolicy();
        sVmPolicy = VmPolicy.LAX;
        return oldPolicy;
    }

    public static boolean isBundledSystemApp(ApplicationInfo ai) {
        if (ai == null || ai.packageName == null) {
            return true;
        }
        if (!ai.isSystemApp() || ai.packageName.equals("com.android.vending") || ai.packageName.equals("com.android.chrome") || ai.packageName.equals(TelephonyManager.PHONE_PROCESS_NAME)) {
            return false;
        }
        return ai.packageName.equals("android") || ai.packageName.startsWith("android.") || ai.packageName.startsWith("com.android.");
    }

    public static void initThreadDefaults(ApplicationInfo ai) {
        ThreadPolicy.Builder builder = new ThreadPolicy.Builder();
        int targetSdkVersion = ai != null ? ai.targetSdkVersion : 10000;
        if (targetSdkVersion >= 11) {
            builder.detectNetwork();
            builder.penaltyDeathOnNetwork();
        }
        if (!Build.IS_USER && !SystemProperties.getBoolean(DISABLE_PROPERTY, false)) {
            if (Build.IS_USERDEBUG) {
                if (isBundledSystemApp(ai)) {
                    builder.detectAll();
                    builder.penaltyDropBox();
                    if (SystemProperties.getBoolean(VISUAL_PROPERTY, false)) {
                        builder.penaltyFlashScreen();
                    }
                }
            } else if (Build.IS_ENG && isBundledSystemApp(ai)) {
                builder.detectAll();
                builder.penaltyDropBox();
                builder.penaltyLog();
                builder.penaltyFlashScreen();
            }
        }
        setThreadPolicy(builder.build());
    }

    public static void initVmDefaults(ApplicationInfo ai) {
        VmPolicy.Builder builder = new VmPolicy.Builder();
        int targetSdkVersion = ai != null ? ai.targetSdkVersion : 10000;
        if (targetSdkVersion >= 24) {
            builder.detectFileUriExposure();
            builder.penaltyDeathOnFileUriExposure();
        }
        if (!Build.IS_USER && !SystemProperties.getBoolean(DISABLE_PROPERTY, false)) {
            if (Build.IS_USERDEBUG) {
                if (isBundledSystemApp(ai)) {
                    builder.detectAll();
                    builder.permitActivityLeaks();
                    builder.penaltyDropBox();
                }
            } else if (Build.IS_ENG && isBundledSystemApp(ai)) {
                builder.detectAll();
                builder.penaltyDropBox();
                builder.penaltyLog();
            }
        }
        setVmPolicy(builder.build());
    }

    @UnsupportedAppUsage
    public static void enableDeathOnFileUriExposure() {
        sVmPolicy = new VmPolicy(8388608 | sVmPolicy.mask | 32, sVmPolicy.classInstanceLimit, sVmPolicy.mListener, sVmPolicy.mCallbackExecutor);
    }

    @UnsupportedAppUsage
    public static void disableDeathOnFileUriExposure() {
        sVmPolicy = new VmPolicy((-8388641) & sVmPolicy.mask, sVmPolicy.classInstanceLimit, sVmPolicy.mListener, sVmPolicy.mCallbackExecutor);
    }

    private static boolean tooManyViolationsThisLoop() {
        return violationsBeingTimed.get().size() >= 10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class AndroidBlockGuardPolicy implements BlockGuard.Policy {
        private ArrayMap<Integer, Long> mLastViolationTime;
        private int mThreadPolicyMask;

        public AndroidBlockGuardPolicy(int threadPolicyMask) {
            this.mThreadPolicyMask = threadPolicyMask;
        }

        public String toString() {
            return "AndroidBlockGuardPolicy; mPolicyMask=" + this.mThreadPolicyMask;
        }

        public int getPolicyMask() {
            return this.mThreadPolicyMask;
        }

        public void onWriteToDisk() {
            if ((this.mThreadPolicyMask & 1) == 0 || StrictMode.access$400()) {
                return;
            }
            startHandlingViolationException(new DiskWriteViolation());
        }

        void onCustomSlowCall(String name) {
            if ((this.mThreadPolicyMask & 8) == 0 || StrictMode.access$400()) {
                return;
            }
            startHandlingViolationException(new CustomViolation(name));
        }

        void onResourceMismatch(Object tag) {
            if ((this.mThreadPolicyMask & 16) == 0 || StrictMode.access$400()) {
                return;
            }
            startHandlingViolationException(new ResourceMismatchViolation(tag));
        }

        public void onUnbufferedIO() {
            if ((this.mThreadPolicyMask & 32) == 0 || StrictMode.access$400()) {
                return;
            }
            startHandlingViolationException(new UnbufferedIoViolation());
        }

        public void onReadFromDisk() {
            if ((this.mThreadPolicyMask & 2) == 0 || StrictMode.access$400()) {
                return;
            }
            startHandlingViolationException(new DiskReadViolation());
        }

        public void onNetwork() {
            int i = this.mThreadPolicyMask;
            if ((i & 4) == 0) {
                return;
            }
            if ((i & 33554432) != 0) {
                throw new NetworkOnMainThreadException();
            }
            if (StrictMode.access$400()) {
                return;
            }
            startHandlingViolationException(new NetworkViolation());
        }

        public void onExplicitGc() {
            if ((this.mThreadPolicyMask & 64) == 0 || StrictMode.access$400()) {
                return;
            }
            startHandlingViolationException(new ExplicitGcViolation());
        }

        public int getThreadPolicyMask() {
            return this.mThreadPolicyMask;
        }

        public void setThreadPolicyMask(int threadPolicyMask) {
            this.mThreadPolicyMask = threadPolicyMask;
        }

        void startHandlingViolationException(Violation e) {
            int penaltyMask = this.mThreadPolicyMask & (-65536);
            ViolationInfo info = new ViolationInfo(e, penaltyMask);
            info.violationUptimeMillis = SystemClock.uptimeMillis();
            handleViolationWithTimingAttempt(info);
        }

        void handleViolationWithTimingAttempt(ViolationInfo info) {
            Looper looper = Looper.myLooper();
            if (looper != null && info.mPenaltyMask != 268435456) {
                final ArrayList<ViolationInfo> records = (ArrayList) StrictMode.violationsBeingTimed.get();
                if (records.size() >= 10) {
                    return;
                }
                records.add(info);
                if (records.size() > 1) {
                    return;
                }
                final IWindowManager windowManager = info.penaltyEnabled(134217728) ? (IWindowManager) StrictMode.sWindowManager.get() : null;
                if (windowManager != null) {
                    try {
                        windowManager.showStrictModeViolation(true);
                    } catch (RemoteException e) {
                    }
                }
                ((Handler) StrictMode.THREAD_HANDLER.get()).postAtFrontOfQueue(new Runnable() { // from class: android.os.-$$Lambda$StrictMode$AndroidBlockGuardPolicy$9nBulCQKaMajrWr41SB7f7YRT1I
                    @Override // java.lang.Runnable
                    public final void run() {
                        StrictMode.AndroidBlockGuardPolicy.this.lambda$handleViolationWithTimingAttempt$0$StrictMode$AndroidBlockGuardPolicy(windowManager, records);
                    }
                });
                return;
            }
            info.durationMillis = -1;
            onThreadPolicyViolation(info);
        }

        public /* synthetic */ void lambda$handleViolationWithTimingAttempt$0$StrictMode$AndroidBlockGuardPolicy(IWindowManager windowManager, ArrayList records) {
            long loopFinishTime = SystemClock.uptimeMillis();
            if (windowManager != null) {
                try {
                    windowManager.showStrictModeViolation(false);
                } catch (RemoteException e) {
                }
            }
            for (int n = 0; n < records.size(); n++) {
                ViolationInfo v = (ViolationInfo) records.get(n);
                v.violationNumThisLoop = n + 1;
                v.durationMillis = (int) (loopFinishTime - v.violationUptimeMillis);
                onThreadPolicyViolation(v);
            }
            records.clear();
        }

        void onThreadPolicyViolation(ViolationInfo info) {
            int penaltyMask;
            if (StrictMode.LOG_V) {
                Log.d(StrictMode.TAG, "onThreadPolicyViolation; penalty=" + info.mPenaltyMask);
            }
            if (info.penaltyEnabled(Integer.MIN_VALUE)) {
                ArrayList<ViolationInfo> violations = (ArrayList) StrictMode.gatheredViolations.get();
                if (violations == null) {
                    violations = new ArrayList<>(1);
                    StrictMode.gatheredViolations.set(violations);
                }
                Iterator<ViolationInfo> it = violations.iterator();
                while (it.hasNext()) {
                    ViolationInfo previous = it.next();
                    if (info.getStackTrace().equals(previous.getStackTrace())) {
                        return;
                    }
                }
                violations.add(info);
                return;
            }
            Integer crashFingerprint = Integer.valueOf(info.hashCode());
            long lastViolationTime = 0;
            ArrayMap<Integer, Long> arrayMap = this.mLastViolationTime;
            if (arrayMap == null) {
                this.mLastViolationTime = new ArrayMap<>(1);
            } else {
                Long vtime = arrayMap.get(crashFingerprint);
                if (vtime != null) {
                    lastViolationTime = vtime.longValue();
                }
            }
            long now = SystemClock.uptimeMillis();
            this.mLastViolationTime.put(crashFingerprint, Long.valueOf(now));
            long timeSinceLastViolationMillis = lastViolationTime == 0 ? Long.MAX_VALUE : now - lastViolationTime;
            if (info.penaltyEnabled(1073741824) && timeSinceLastViolationMillis > 1000) {
                StrictMode.sLogger.log(info);
            }
            final Violation violation = info.mViolation;
            int penaltyMask2 = 0;
            if (info.penaltyEnabled(536870912) && timeSinceLastViolationMillis > 30000) {
                penaltyMask2 = 0 | 536870912;
            }
            if (info.penaltyEnabled(67108864) && lastViolationTime == 0) {
                penaltyMask = penaltyMask2 | 67108864;
            } else {
                penaltyMask = penaltyMask2;
            }
            if (penaltyMask != 0) {
                boolean justDropBox = info.mPenaltyMask == 67108864;
                if (justDropBox) {
                    StrictMode.dropboxViolationAsync(penaltyMask, info);
                } else {
                    StrictMode.handleApplicationStrictModeViolation(penaltyMask, info);
                }
            }
            if (!info.penaltyEnabled(268435456)) {
                final OnThreadViolationListener listener = (OnThreadViolationListener) StrictMode.sThreadViolationListener.get();
                Executor executor = (Executor) StrictMode.sThreadViolationExecutor.get();
                if (listener != null && executor != null) {
                    try {
                        executor.execute(new Runnable() { // from class: android.os.-$$Lambda$StrictMode$AndroidBlockGuardPolicy$FxZGA9KtfTewqdcxlUwvIe5Nx9I
                            @Override // java.lang.Runnable
                            public final void run() {
                                StrictMode.AndroidBlockGuardPolicy.lambda$onThreadPolicyViolation$1(StrictMode.OnThreadViolationListener.this, violation);
                            }
                        });
                        return;
                    } catch (RejectedExecutionException e) {
                        Log.e(StrictMode.TAG, "ThreadPolicy penaltyCallback failed", e);
                        return;
                    }
                }
                return;
            }
            throw new RuntimeException("StrictMode ThreadPolicy violation", violation);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onThreadPolicyViolation$1(OnThreadViolationListener listener, Violation violation) {
            ThreadPolicy oldPolicy = StrictMode.allowThreadViolations();
            try {
                listener.onThreadViolation(violation);
            } finally {
                StrictMode.setThreadPolicy(oldPolicy);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dropboxViolationAsync(final int penaltyMask, final ViolationInfo info) {
        int outstanding = sDropboxCallsInFlight.incrementAndGet();
        if (outstanding > 20) {
            sDropboxCallsInFlight.decrementAndGet();
            return;
        }
        if (LOG_V) {
            Log.d(TAG, "Dropboxing async; in-flight=" + outstanding);
        }
        BackgroundThread.getHandler().post(new Runnable() { // from class: android.os.-$$Lambda$StrictMode$yZJXPvy2veRNA-xL_SWdXzX_OLg
            @Override // java.lang.Runnable
            public final void run() {
                StrictMode.lambda$dropboxViolationAsync$2(penaltyMask, info);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$dropboxViolationAsync$2(int penaltyMask, ViolationInfo info) {
        handleApplicationStrictModeViolation(penaltyMask, info);
        int outstandingInner = sDropboxCallsInFlight.decrementAndGet();
        if (LOG_V) {
            Log.d(TAG, "Dropbox complete; in-flight=" + outstandingInner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleApplicationStrictModeViolation(int penaltyMask, ViolationInfo info) {
        int oldMask = getThreadPolicyMask();
        try {
            try {
                setThreadPolicyMask(0);
                IActivityManager am = ActivityManager.getService();
                if (am == null) {
                    Log.w(TAG, "No activity manager; failed to Dropbox violation.");
                } else {
                    am.handleApplicationStrictModeViolation(RuntimeInit.getApplicationObject(), penaltyMask, info);
                }
            } catch (RemoteException e) {
                if (!(e instanceof DeadObjectException)) {
                    Log.e(TAG, "RemoteException handling StrictMode violation", e);
                }
            }
        } finally {
            setThreadPolicyMask(oldMask);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class AndroidCloseGuardReporter implements CloseGuard.Reporter {
        private AndroidCloseGuardReporter() {
        }

        public void report(String message, Throwable allocationSite) {
            StrictMode.onVmPolicyViolation(new LeakedClosableViolation(message, allocationSite));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasGatheredViolations() {
        return gatheredViolations.get() != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void clearGatheredViolations() {
        gatheredViolations.set(null);
    }

    public static void conditionallyCheckInstanceCounts() {
        VmPolicy policy = getVmPolicy();
        int policySize = policy.classInstanceLimit.size();
        if (policySize == 0) {
            return;
        }
        System.gc();
        System.runFinalization();
        System.gc();
        Class[] classes = (Class[]) policy.classInstanceLimit.keySet().toArray(new Class[policySize]);
        long[] instanceCounts = VMDebug.countInstancesOfClasses(classes, false);
        for (int i = 0; i < classes.length; i++) {
            Class klass = classes[i];
            int limit = policy.classInstanceLimit.get(klass).intValue();
            long instances = instanceCounts[i];
            if (instances > limit) {
                onVmPolicyViolation(new InstanceCountViolation(klass, instances, limit));
            }
        }
    }

    public static void setVmPolicy(VmPolicy policy) {
        synchronized (StrictMode.class) {
            sVmPolicy = policy;
            setCloseGuardEnabled(vmClosableObjectLeaksEnabled());
            Looper looper = Looper.getMainLooper();
            if (looper != null) {
                MessageQueue mq = looper.mQueue;
                if (policy.classInstanceLimit.size() != 0 && (sVmPolicy.mask & (-65536)) != 0) {
                    if (!sIsIdlerRegistered) {
                        mq.addIdleHandler(sProcessIdleHandler);
                        sIsIdlerRegistered = true;
                    }
                }
                mq.removeIdleHandler(sProcessIdleHandler);
                sIsIdlerRegistered = false;
            }
            int networkPolicy = 0;
            if ((sVmPolicy.mask & 64) != 0) {
                if ((sVmPolicy.mask & 268435456) == 0 && (sVmPolicy.mask & 16777216) == 0) {
                    networkPolicy = 1;
                }
                networkPolicy = 2;
            }
            INetworkManagementService netd = INetworkManagementService.Stub.asInterface(ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE));
            if (netd != null) {
                try {
                    netd.setUidCleartextNetworkPolicy(Process.myUid(), networkPolicy);
                } catch (RemoteException e) {
                }
            } else if (networkPolicy != 0) {
                Log.w(TAG, "Dropping requested network policy due to missing service!");
            }
            if ((sVmPolicy.mask & 512) != 0) {
                VMRuntime.setNonSdkApiUsageConsumer(sNonSdkApiUsageConsumer);
                VMRuntime.setDedupeHiddenApiWarnings(false);
            } else {
                VMRuntime.setNonSdkApiUsageConsumer((Consumer) null);
                VMRuntime.setDedupeHiddenApiWarnings(true);
            }
            setBlockGuardVmPolicy(sVmPolicy.mask);
        }
    }

    public static VmPolicy getVmPolicy() {
        VmPolicy vmPolicy;
        synchronized (StrictMode.class) {
            vmPolicy = sVmPolicy;
        }
        return vmPolicy;
    }

    public static void enableDefaults() {
        setThreadPolicy(new ThreadPolicy.Builder().detectAll().penaltyLog().build());
        setVmPolicy(new VmPolicy.Builder().detectAll().penaltyLog().build());
    }

    public static boolean vmSqliteObjectLeaksEnabled() {
        return (sVmPolicy.mask & 1) != 0;
    }

    public static boolean vmClosableObjectLeaksEnabled() {
        return (sVmPolicy.mask & 2) != 0;
    }

    public static boolean vmRegistrationLeaksEnabled() {
        return (sVmPolicy.mask & 16) != 0;
    }

    public static boolean vmFileUriExposureEnabled() {
        return (sVmPolicy.mask & 32) != 0;
    }

    public static boolean vmCleartextNetworkEnabled() {
        return (sVmPolicy.mask & 64) != 0;
    }

    public static boolean vmContentUriWithoutPermissionEnabled() {
        return (sVmPolicy.mask & 128) != 0;
    }

    public static boolean vmUntaggedSocketEnabled() {
        return (sVmPolicy.mask & 256) != 0;
    }

    public static boolean vmImplicitDirectBootEnabled() {
        return (sVmPolicy.mask & 1024) != 0;
    }

    public static boolean vmCredentialProtectedWhileLockedEnabled() {
        return (sVmPolicy.mask & 2048) != 0;
    }

    public static void onSqliteObjectLeaked(String message, Throwable originStack) {
        onVmPolicyViolation(new SqliteObjectLeakedViolation(message, originStack));
    }

    @UnsupportedAppUsage
    public static void onWebViewMethodCalledOnWrongThread(Throwable originStack) {
        onVmPolicyViolation(new WebViewMethodCalledOnWrongThreadViolation(originStack));
    }

    public static void onIntentReceiverLeaked(Throwable originStack) {
        onVmPolicyViolation(new IntentReceiverLeakedViolation(originStack));
    }

    public static void onServiceConnectionLeaked(Throwable originStack) {
        onVmPolicyViolation(new ServiceConnectionLeakedViolation(originStack));
    }

    public static void onFileUriExposed(Uri uri, String location) {
        String message = uri + " exposed beyond app through " + location;
        if ((sVmPolicy.mask & 8388608) != 0) {
            throw new FileUriExposedException(message);
        }
        onVmPolicyViolation(new FileUriExposedViolation(message));
    }

    public static void onContentUriWithoutPermission(Uri uri, String location) {
        onVmPolicyViolation(new ContentUriWithoutPermissionViolation(uri, location));
    }

    public static void onCleartextNetworkDetected(byte[] firstPacket) {
        byte[] rawAddr = null;
        if (firstPacket != null) {
            if (firstPacket.length >= 20 && (firstPacket[0] & 240) == 64) {
                rawAddr = new byte[4];
                System.arraycopy(firstPacket, 16, rawAddr, 0, 4);
            } else if (firstPacket.length >= 40 && (firstPacket[0] & 240) == 96) {
                rawAddr = new byte[16];
                System.arraycopy(firstPacket, 24, rawAddr, 0, 16);
            }
        }
        int uid = Process.myUid();
        String msg = "Detected cleartext network traffic from UID " + uid;
        if (rawAddr != null) {
            try {
                msg = msg + " to " + InetAddress.getByAddress(rawAddr);
            } catch (UnknownHostException e) {
            }
        }
        String msg2 = msg + HexDump.dumpHexString(firstPacket).trim() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        boolean forceDeath = (sVmPolicy.mask & 16777216) != 0;
        onVmPolicyViolation(new CleartextNetworkViolation(msg2), forceDeath);
    }

    public static void onUntaggedSocket() {
        onVmPolicyViolation(new UntaggedSocketViolation());
    }

    public static void onImplicitDirectBoot() {
        onVmPolicyViolation(new ImplicitDirectBootViolation());
    }

    private static boolean isUserKeyUnlocked(int userId) {
        IStorageManager storage = IStorageManager.Stub.asInterface(ServiceManager.getService("mount"));
        if (storage != null) {
            try {
                return storage.isUserKeyUnlocked(userId);
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onCredentialProtectedPathAccess(String path, int userId) {
        if (userId == UserHandle.myUserId()) {
            if (sUserKeyUnlocked) {
                return;
            }
            if (isUserKeyUnlocked(userId)) {
                sUserKeyUnlocked = true;
                return;
            }
        } else if (isUserKeyUnlocked(userId)) {
            return;
        }
        onVmPolicyViolation(new CredentialProtectedWhileLockedViolation("Accessed credential protected path " + path + " while user " + userId + " was locked"));
    }

    public static void onVmPolicyViolation(Violation originStack) {
        onVmPolicyViolation(originStack, false);
    }

    public static void onVmPolicyViolation(final Violation violation, boolean forceDeath) {
        boolean penaltyDropbox = (sVmPolicy.mask & 67108864) != 0;
        boolean penaltyDeath = (sVmPolicy.mask & 268435456) != 0 || forceDeath;
        boolean penaltyLog = (sVmPolicy.mask & 1073741824) != 0;
        int penaltyMask = (-65536) & sVmPolicy.mask;
        ViolationInfo info = new ViolationInfo(violation, penaltyMask);
        info.numAnimationsRunning = 0;
        info.tags = null;
        info.broadcastIntentAction = null;
        Integer fingerprint = Integer.valueOf(info.hashCode());
        long now = SystemClock.uptimeMillis();
        long timeSinceLastViolationMillis = Long.MAX_VALUE;
        synchronized (sLastVmViolationTime) {
            if (sLastVmViolationTime.containsKey(fingerprint)) {
                long lastViolationTime = sLastVmViolationTime.get(fingerprint).longValue();
                timeSinceLastViolationMillis = now - lastViolationTime;
            }
            if (timeSinceLastViolationMillis > 1000) {
                sLastVmViolationTime.put(fingerprint, Long.valueOf(now));
            }
        }
        if (timeSinceLastViolationMillis <= 1000) {
            return;
        }
        if (penaltyLog && sLogger != null && timeSinceLastViolationMillis > 1000) {
            sLogger.log(info);
        }
        if (penaltyDropbox) {
            if (penaltyDeath) {
                handleApplicationStrictModeViolation(67108864, info);
            } else {
                dropboxViolationAsync(67108864, info);
            }
        }
        if (penaltyDeath) {
            System.err.println("StrictMode VmPolicy violation with POLICY_DEATH; shutting down.");
            Process.killProcess(Process.myPid());
            System.exit(10);
        }
        if (sVmPolicy.mListener != null && sVmPolicy.mCallbackExecutor != null) {
            final OnVmViolationListener listener = sVmPolicy.mListener;
            try {
                sVmPolicy.mCallbackExecutor.execute(new Runnable() { // from class: android.os.-$$Lambda$StrictMode$UFC_nI1x6u8ZwMQmA7bmj9NHZz4
                    @Override // java.lang.Runnable
                    public final void run() {
                        StrictMode.lambda$onVmPolicyViolation$3(StrictMode.OnVmViolationListener.this, violation);
                    }
                });
            } catch (RejectedExecutionException e) {
                Log.e(TAG, "VmPolicy penaltyCallback failed", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onVmPolicyViolation$3(OnVmViolationListener listener, Violation violation) {
        VmPolicy oldPolicy = allowVmViolations();
        try {
            listener.onVmViolation(violation);
        } finally {
            setVmPolicy(oldPolicy);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void writeGatheredViolationsToParcel(Parcel p) {
        ArrayList<ViolationInfo> violations = gatheredViolations.get();
        if (violations == null) {
            p.writeInt(0);
        } else {
            int size = Math.min(violations.size(), 3);
            p.writeInt(size);
            for (int i = 0; i < size; i++) {
                violations.get(i).writeToParcel(p, 0);
            }
        }
        gatheredViolations.set(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void readAndHandleBinderCallViolations(Parcel p) {
        Throwable localCallSite = new Throwable();
        int policyMask = getThreadPolicyMask();
        boolean currentlyGathering = (Integer.MIN_VALUE & policyMask) != 0;
        int size = p.readInt();
        for (int i = 0; i < size; i++) {
            ViolationInfo info = new ViolationInfo(p, !currentlyGathering);
            info.addLocalStack(localCallSite);
            BlockGuard.Policy policy = BlockGuard.getThreadPolicy();
            if (policy instanceof AndroidBlockGuardPolicy) {
                ((AndroidBlockGuardPolicy) policy).handleViolationWithTimingAttempt(info);
            }
        }
    }

    @UnsupportedAppUsage
    private static void onBinderStrictModePolicyChange(int newPolicy) {
        setBlockGuardPolicy(newPolicy);
    }

    /* loaded from: classes2.dex */
    public static class Span {
        private final ThreadSpanState mContainerState;
        private long mCreateMillis;
        private String mName;
        private Span mNext;
        private Span mPrev;

        Span(ThreadSpanState threadState) {
            this.mContainerState = threadState;
        }

        protected Span() {
            this.mContainerState = null;
        }

        @UnsupportedAppUsage
        public void finish() {
            ThreadSpanState state = this.mContainerState;
            synchronized (state) {
                if (this.mName == null) {
                    return;
                }
                if (this.mPrev != null) {
                    this.mPrev.mNext = this.mNext;
                }
                if (this.mNext != null) {
                    this.mNext.mPrev = this.mPrev;
                }
                if (state.mActiveHead == this) {
                    state.mActiveHead = this.mNext;
                }
                state.mActiveSize--;
                if (StrictMode.LOG_V) {
                    Log.d(StrictMode.TAG, "Span finished=" + this.mName + "; size=" + state.mActiveSize);
                }
                this.mCreateMillis = -1L;
                this.mName = null;
                this.mPrev = null;
                this.mNext = null;
                if (state.mFreeListSize < 5) {
                    this.mNext = state.mFreeListHead;
                    state.mFreeListHead = this;
                    state.mFreeListSize++;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class ThreadSpanState {
        public Span mActiveHead;
        public int mActiveSize;
        public Span mFreeListHead;
        public int mFreeListSize;

        private ThreadSpanState() {
        }
    }

    @UnsupportedAppUsage
    public static Span enterCriticalSpan(String name) {
        Span span;
        if (Build.IS_USER) {
            return NO_OP_SPAN;
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name must be non-null and non-empty");
        }
        ThreadSpanState state = sThisThreadSpanState.get();
        synchronized (state) {
            if (state.mFreeListHead != null) {
                span = state.mFreeListHead;
                state.mFreeListHead = span.mNext;
                state.mFreeListSize--;
            } else {
                span = new Span(state);
            }
            span.mName = name;
            span.mCreateMillis = SystemClock.uptimeMillis();
            span.mNext = state.mActiveHead;
            span.mPrev = null;
            state.mActiveHead = span;
            state.mActiveSize++;
            if (span.mNext != null) {
                span.mNext.mPrev = span;
            }
            if (LOG_V) {
                Log.d(TAG, "Span enter=" + name + "; size=" + state.mActiveSize);
            }
        }
        return span;
    }

    public static void noteSlowCall(String name) {
        BlockGuard.Policy policy = BlockGuard.getThreadPolicy();
        if (!(policy instanceof AndroidBlockGuardPolicy)) {
            return;
        }
        ((AndroidBlockGuardPolicy) policy).onCustomSlowCall(name);
    }

    public static void noteResourceMismatch(Object tag) {
        BlockGuard.Policy policy = BlockGuard.getThreadPolicy();
        if (!(policy instanceof AndroidBlockGuardPolicy)) {
            return;
        }
        ((AndroidBlockGuardPolicy) policy).onResourceMismatch(tag);
    }

    public static void noteUnbufferedIO() {
        BlockGuard.Policy policy = BlockGuard.getThreadPolicy();
        if (!(policy instanceof AndroidBlockGuardPolicy)) {
            return;
        }
        policy.onUnbufferedIO();
    }

    public static void noteDiskRead() {
        BlockGuard.Policy policy = BlockGuard.getThreadPolicy();
        if (!(policy instanceof AndroidBlockGuardPolicy)) {
            return;
        }
        policy.onReadFromDisk();
    }

    public static void noteDiskWrite() {
        BlockGuard.Policy policy = BlockGuard.getThreadPolicy();
        if (!(policy instanceof AndroidBlockGuardPolicy)) {
            return;
        }
        policy.onWriteToDisk();
    }

    public static Object trackActivity(Object instance) {
        return new InstanceTracker(instance);
    }

    @UnsupportedAppUsage
    public static void incrementExpectedActivityCount(Class klass) {
        if (klass == null) {
            return;
        }
        synchronized (StrictMode.class) {
            if ((sVmPolicy.mask & 4) == 0) {
                return;
            }
            Integer expected = sExpectedActivityInstanceCount.get(klass);
            int i = 1;
            if (expected != null) {
                i = 1 + expected.intValue();
            }
            Integer newExpected = Integer.valueOf(i);
            sExpectedActivityInstanceCount.put(klass, newExpected);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x002c A[Catch: all -> 0x0060, TryCatch #0 {, blocks: (B:6:0x0006, B:8:0x000e, B:10:0x0010, B:12:0x001b, B:15:0x0022, B:18:0x002c, B:20:0x003b, B:21:0x003d, B:19:0x0032), top: B:32:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0032 A[Catch: all -> 0x0060, TryCatch #0 {, blocks: (B:6:0x0006, B:8:0x000e, B:10:0x0010, B:12:0x001b, B:15:0x0022, B:18:0x002c, B:20:0x003b, B:21:0x003d, B:19:0x0032), top: B:32:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0044 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0045  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void decrementExpectedActivityCount(java.lang.Class r6) {
        /*
            if (r6 != 0) goto L3
            return
        L3:
            java.lang.Class<android.os.StrictMode> r0 = android.os.StrictMode.class
            monitor-enter(r0)
            android.os.StrictMode$VmPolicy r1 = android.os.StrictMode.sVmPolicy     // Catch: java.lang.Throwable -> L60
            int r1 = r1.mask     // Catch: java.lang.Throwable -> L60
            r1 = r1 & 4
            if (r1 != 0) goto L10
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L60
            return
        L10:
            java.util.HashMap<java.lang.Class, java.lang.Integer> r1 = android.os.StrictMode.sExpectedActivityInstanceCount     // Catch: java.lang.Throwable -> L60
            java.lang.Object r1 = r1.get(r6)     // Catch: java.lang.Throwable -> L60
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch: java.lang.Throwable -> L60
            r2 = 0
            if (r1 == 0) goto L29
            int r3 = r1.intValue()     // Catch: java.lang.Throwable -> L60
            if (r3 != 0) goto L22
            goto L29
        L22:
            int r3 = r1.intValue()     // Catch: java.lang.Throwable -> L60
            int r3 = r3 + (-1)
            goto L2a
        L29:
            r3 = r2
        L2a:
            if (r3 != 0) goto L32
            java.util.HashMap<java.lang.Class, java.lang.Integer> r4 = android.os.StrictMode.sExpectedActivityInstanceCount     // Catch: java.lang.Throwable -> L60
            r4.remove(r6)     // Catch: java.lang.Throwable -> L60
            goto L3b
        L32:
            java.util.HashMap<java.lang.Class, java.lang.Integer> r4 = android.os.StrictMode.sExpectedActivityInstanceCount     // Catch: java.lang.Throwable -> L60
            java.lang.Integer r5 = java.lang.Integer.valueOf(r3)     // Catch: java.lang.Throwable -> L60
            r4.put(r6, r5)     // Catch: java.lang.Throwable -> L60
        L3b:
            int r3 = r3 + 1
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L60
            int r0 = android.os.StrictMode.InstanceTracker.getInstanceCount(r6)
            if (r0 > r3) goto L45
            return
        L45:
            java.lang.System.gc()
            java.lang.System.runFinalization()
            java.lang.System.gc()
            long r1 = dalvik.system.VMDebug.countInstancesOfClass(r6, r2)
            long r4 = (long) r3
            int r4 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r4 <= 0) goto L5f
            android.os.strictmode.InstanceCountViolation r4 = new android.os.strictmode.InstanceCountViolation
            r4.<init>(r6, r1, r3)
            onVmPolicyViolation(r4)
        L5f:
            return
        L60:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L60
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.StrictMode.decrementExpectedActivityCount(java.lang.Class):void");
    }

    /* loaded from: classes2.dex */
    public static final class ViolationInfo implements Parcelable {
        public static final Parcelable.Creator<ViolationInfo> CREATOR = new Parcelable.Creator<ViolationInfo>() { // from class: android.os.StrictMode.ViolationInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ViolationInfo createFromParcel(Parcel in) {
                return new ViolationInfo(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ViolationInfo[] newArray(int size) {
                return new ViolationInfo[size];
            }
        };
        public String broadcastIntentAction;
        public int durationMillis;
        private final Deque<StackTraceElement[]> mBinderStack;
        private final int mPenaltyMask;
        private String mStackTrace;
        private final Violation mViolation;
        public int numAnimationsRunning;
        public long numInstances;
        public String[] tags;
        public int violationNumThisLoop;
        public long violationUptimeMillis;

        ViolationInfo(Violation tr, int penaltyMask) {
            this.mBinderStack = new ArrayDeque();
            this.durationMillis = -1;
            this.numAnimationsRunning = 0;
            this.numInstances = -1L;
            this.mViolation = tr;
            this.mPenaltyMask = penaltyMask;
            this.violationUptimeMillis = SystemClock.uptimeMillis();
            this.numAnimationsRunning = ValueAnimator.getCurrentAnimationsCount();
            Intent broadcastIntent = ActivityThread.getIntentBeingBroadcast();
            if (broadcastIntent != null) {
                this.broadcastIntentAction = broadcastIntent.getAction();
            }
            ThreadSpanState state = (ThreadSpanState) StrictMode.sThisThreadSpanState.get();
            if (tr instanceof InstanceCountViolation) {
                this.numInstances = ((InstanceCountViolation) tr).getNumberOfInstances();
            }
            synchronized (state) {
                int spanActiveCount = state.mActiveSize;
                spanActiveCount = spanActiveCount > 20 ? 20 : spanActiveCount;
                if (spanActiveCount != 0) {
                    this.tags = new String[spanActiveCount];
                    int index = 0;
                    for (Span iter = state.mActiveHead; iter != null && index < spanActiveCount; iter = iter.mNext) {
                        this.tags[index] = iter.mName;
                        index++;
                    }
                }
            }
        }

        public String getStackTrace() {
            if (this.mStackTrace == null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new FastPrintWriter((Writer) sw, false, 256);
                this.mViolation.printStackTrace(pw);
                for (StackTraceElement[] traces : this.mBinderStack) {
                    pw.append("# via Binder call with stack:\n");
                    for (StackTraceElement traceElement : traces) {
                        pw.append("\tat ");
                        pw.append(traceElement.toString());
                        pw.append('\n');
                    }
                }
                pw.flush();
                pw.close();
                this.mStackTrace = sw.toString();
            }
            return this.mStackTrace;
        }

        public Class<? extends Violation> getViolationClass() {
            return this.mViolation.getClass();
        }

        public String getViolationDetails() {
            return this.mViolation.getMessage();
        }

        boolean penaltyEnabled(int p) {
            return (this.mPenaltyMask & p) != 0;
        }

        void addLocalStack(Throwable t) {
            this.mBinderStack.addFirst(t.getStackTrace());
        }

        public int hashCode() {
            int result = 17;
            Violation violation = this.mViolation;
            if (violation != null) {
                result = (17 * 37) + violation.hashCode();
            }
            if (this.numAnimationsRunning != 0) {
                result *= 37;
            }
            String str = this.broadcastIntentAction;
            if (str != null) {
                result = (result * 37) + str.hashCode();
            }
            String[] strArr = this.tags;
            if (strArr != null) {
                for (String tag : strArr) {
                    result = (result * 37) + tag.hashCode();
                }
            }
            return result;
        }

        public ViolationInfo(Parcel in) {
            this(in, false);
        }

        public ViolationInfo(Parcel in, boolean unsetGatheringBit) {
            this.mBinderStack = new ArrayDeque();
            this.durationMillis = -1;
            this.numAnimationsRunning = 0;
            this.numInstances = -1L;
            this.mViolation = (Violation) in.readSerializable();
            int binderStackSize = in.readInt();
            for (int i = 0; i < binderStackSize; i++) {
                StackTraceElement[] traceElements = new StackTraceElement[in.readInt()];
                for (int j = 0; j < traceElements.length; j++) {
                    StackTraceElement element = new StackTraceElement(in.readString(), in.readString(), in.readString(), in.readInt());
                    traceElements[j] = element;
                }
                this.mBinderStack.add(traceElements);
            }
            int rawPenaltyMask = in.readInt();
            if (unsetGatheringBit) {
                this.mPenaltyMask = Integer.MAX_VALUE & rawPenaltyMask;
            } else {
                this.mPenaltyMask = rawPenaltyMask;
            }
            this.durationMillis = in.readInt();
            this.violationNumThisLoop = in.readInt();
            this.numAnimationsRunning = in.readInt();
            this.violationUptimeMillis = in.readLong();
            this.numInstances = in.readLong();
            this.broadcastIntentAction = in.readString();
            this.tags = in.readStringArray();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeSerializable(this.mViolation);
            dest.writeInt(this.mBinderStack.size());
            for (StackTraceElement[] traceElements : this.mBinderStack) {
                dest.writeInt(traceElements.length);
                for (StackTraceElement element : traceElements) {
                    dest.writeString(element.getClassName());
                    dest.writeString(element.getMethodName());
                    dest.writeString(element.getFileName());
                    dest.writeInt(element.getLineNumber());
                }
            }
            int start = dest.dataPosition();
            dest.writeInt(this.mPenaltyMask);
            dest.writeInt(this.durationMillis);
            dest.writeInt(this.violationNumThisLoop);
            dest.writeInt(this.numAnimationsRunning);
            dest.writeLong(this.violationUptimeMillis);
            dest.writeLong(this.numInstances);
            dest.writeString(this.broadcastIntentAction);
            dest.writeStringArray(this.tags);
            int dataPosition = dest.dataPosition() - start;
        }

        public void dump(Printer pw, String prefix) {
            pw.println(prefix + "stackTrace: " + getStackTrace());
            pw.println(prefix + "penalty: " + this.mPenaltyMask);
            if (this.durationMillis != -1) {
                pw.println(prefix + "durationMillis: " + this.durationMillis);
            }
            if (this.numInstances != -1) {
                pw.println(prefix + "numInstances: " + this.numInstances);
            }
            if (this.violationNumThisLoop != 0) {
                pw.println(prefix + "violationNumThisLoop: " + this.violationNumThisLoop);
            }
            if (this.numAnimationsRunning != 0) {
                pw.println(prefix + "numAnimationsRunning: " + this.numAnimationsRunning);
            }
            pw.println(prefix + "violationUptimeMillis: " + this.violationUptimeMillis);
            if (this.broadcastIntentAction != null) {
                pw.println(prefix + "broadcastIntentAction: " + this.broadcastIntentAction);
            }
            String[] strArr = this.tags;
            if (strArr != null) {
                int index = 0;
                int length = strArr.length;
                int i = 0;
                while (i < length) {
                    String tag = strArr[i];
                    pw.println(prefix + "tag[" + index + "]: " + tag);
                    i++;
                    index++;
                }
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceTracker {
        private static final HashMap<Class<?>, Integer> sInstanceCounts = new HashMap<>();
        private final Class<?> mKlass;

        public InstanceTracker(Object instance) {
            this.mKlass = instance.getClass();
            synchronized (sInstanceCounts) {
                Integer value = sInstanceCounts.get(this.mKlass);
                int newValue = value != null ? 1 + value.intValue() : 1;
                sInstanceCounts.put(this.mKlass, Integer.valueOf(newValue));
            }
        }

        protected void finalize() throws Throwable {
            try {
                synchronized (sInstanceCounts) {
                    Integer value = sInstanceCounts.get(this.mKlass);
                    if (value != null) {
                        int newValue = value.intValue() - 1;
                        if (newValue > 0) {
                            sInstanceCounts.put(this.mKlass, Integer.valueOf(newValue));
                        } else {
                            sInstanceCounts.remove(this.mKlass);
                        }
                    }
                }
            } finally {
                super.finalize();
            }
        }

        public static int getInstanceCount(Class<?> klass) {
            int intValue;
            synchronized (sInstanceCounts) {
                Integer value = sInstanceCounts.get(klass);
                intValue = value != null ? value.intValue() : 0;
            }
            return intValue;
        }
    }
}
