package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.Activity;
import android.app.ActivityThread;
import android.app.IApplicationThread;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.app.backup.BackupAgent;
import android.app.backup.FullBackup;
import android.app.servertransaction.ActivityLifecycleItem;
import android.app.servertransaction.ActivityRelaunchItem;
import android.app.servertransaction.ActivityResultItem;
import android.app.servertransaction.ClientTransaction;
import android.app.servertransaction.ClientTransactionItem;
import android.app.servertransaction.PendingTransactionActions;
import android.app.servertransaction.TransactionExecutor;
import android.app.servertransaction.TransactionExecutorHelper;
import android.app.slice.Slice;
import android.content.AutofillOptions;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.ContentCaptureOptions;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IContentProvider;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDebug;
import android.ddm.DdmHandleAppName;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.HardwareRenderer;
import android.hardware.display.DisplayManagerGlobal;
import android.net.ConnectivityManager;
import android.net.Proxy;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Debug;
import android.os.Environment;
import android.os.FileUtils;
import android.os.GraphicsEnvironment;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.BlockedNumberContract;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.DeviceConfig;
import android.provider.Downloads;
import android.provider.Settings;
import android.renderscript.RenderScriptCacheDir;
import android.security.NetworkSecurityPolicy;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
import android.util.MergedConfiguration;
import android.util.Pair;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SuperNotCalledException;
import android.util.proto.ProtoOutputStream;
import android.view.Choreographer;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.ThreadedRenderer;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewManager;
import android.view.ViewRootImpl;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.webkit.WebView;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.content.ReferrerIntent;
import com.android.internal.os.BinderInternal;
import com.android.internal.os.RuntimeInit;
import com.android.internal.os.SomeArgs;
import com.android.internal.policy.PhoneWindow;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.HexConsumer;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.org.conscrypt.OpenSSLSocketImpl;
import com.android.org.conscrypt.TrustedCertificateStore;
import com.xiaopeng.app.xpActivityManager;
import com.xiaopeng.app.xpActivityThreadProxy;
import com.xiaopeng.util.DebugOption;
import com.xiaopeng.view.SharedDisplayManager;
import com.xiaopeng.view.xpWindowManager;
import dalvik.system.CloseGuard;
import dalvik.system.VMDebug;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import libcore.io.ForwardingOs;
import libcore.io.IoUtils;
import libcore.io.Os;
import libcore.net.event.NetworkEventDispatcher;

/* loaded from: classes.dex */
public final class ActivityThread extends ClientTransactionHandler {
    private static final int ACTIVITY_THREAD_CHECKIN_VERSION = 4;
    private static final long CONTENT_PROVIDER_RETAIN_TIME = 1000;
    private static final boolean DEBUG_BACKUP = false;
    public static final boolean DEBUG_MEMORY_TRIM = false;
    static final boolean DEBUG_MESSAGES = false;
    public static final boolean DEBUG_ORDER;
    private static final boolean DEBUG_PROVIDER;
    private static final boolean DEBUG_SERVICE;
    private static final String HEAP_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s";
    private static final String HEAP_FULL_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s";
    public static final long INVALID_PROC_STATE_SEQ = -1;
    private static final long MIN_TIME_BETWEEN_GCS = 5000;
    private static final String ONE_COUNT_COLUMN = "%21s %8d";
    private static final String ONE_COUNT_COLUMN_HEADER = "%21s %8s";
    private static final long PENDING_TOP_PROCESS_STATE_TIMEOUT = 1000;
    public static final String PROC_START_SEQ_IDENT = "seq=";
    private static final boolean REPORT_TO_ACTIVITY = true;
    public static final int SERVICE_DONE_EXECUTING_ANON = 0;
    public static final int SERVICE_DONE_EXECUTING_START = 1;
    public static final int SERVICE_DONE_EXECUTING_STOP = 2;
    private static final int SQLITE_MEM_RELEASED_EVENT_LOG_TAG = 75003;
    public static final String TAG = "ActivityThread";
    private static final String TWO_COUNT_COLUMNS = "%21s %8d %21s %8d";
    private static final int VM_PROCESS_STATE_JANK_IMPERCEPTIBLE = 1;
    private static final int VM_PROCESS_STATE_JANK_PERCEPTIBLE = 0;
    @UnsupportedAppUsage
    private static volatile ActivityThread sCurrentActivityThread;
    private static final ThreadLocal<Intent> sCurrentBroadcastIntent;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    static volatile Handler sMainThreadHandler;
    @UnsupportedAppUsage
    static volatile IPackageManager sPackageManager;
    @UnsupportedAppUsage
    AppBindData mBoundApplication;
    Configuration mCompatConfiguration;
    @UnsupportedAppUsage
    Configuration mConfiguration;
    @UnsupportedAppUsage
    int mCurDefaultDisplayDpi;
    @UnsupportedAppUsage
    boolean mDensityCompatMode;
    @UnsupportedAppUsage
    Application mInitialApplication;
    @UnsupportedAppUsage
    Instrumentation mInstrumentation;
    private int mLastSessionId;
    Profiler mProfiler;
    @GuardedBy({"this"})
    private Map<SafeCancellationTransport, CancellationSignal> mRemoteCancellations;
    @UnsupportedAppUsage
    private ContextImpl mSystemContext;
    private ContextImpl mSystemUiContext;
    private static final Bitmap.Config THUMBNAIL_FORMAT = Bitmap.Config.RGB_565;
    static final boolean localLOGV = DebugOption.DEBUG_AM_ACTIVITY_THREAD_LOCAL_LOGV;
    public static final boolean DEBUG_BROADCAST = DebugOption.DEBUG_AM_ACTIVITY_THREAD_BROADCAST;
    private static final boolean DEBUG_RESULTS = localLOGV;
    public static final boolean DEBUG_CONFIGURATION = DebugOption.DEBUG_CONFIGURATION;
    private final Object mNetworkPolicyLock = new Object();
    @GuardedBy({"mNetworkPolicyLock"})
    private long mNetworkBlockSeq = -1;
    @UnsupportedAppUsage
    final ApplicationThread mAppThread = new ApplicationThread();
    @UnsupportedAppUsage
    final Looper mLooper = Looper.myLooper();
    @UnsupportedAppUsage
    final H mH = new H();
    final Executor mExecutor = new HandlerExecutor(this.mH);
    @UnsupportedAppUsage
    final ArrayMap<IBinder, ActivityClientRecord> mActivities = new ArrayMap<>();
    final Map<IBinder, ClientTransactionItem> mActivitiesToBeDestroyed = Collections.synchronizedMap(new ArrayMap());
    ActivityClientRecord mNewActivities = null;
    @UnsupportedAppUsage
    int mNumVisibleActivities = 0;
    private final AtomicInteger mNumLaunchingActivities = new AtomicInteger();
    @GuardedBy({"mAppThread"})
    private int mLastProcessState = -1;
    @GuardedBy({"mAppThread"})
    private int mPendingProcessState = -1;
    ArrayList<WeakReference<AssistStructure>> mLastAssistStructures = new ArrayList<>();
    @UnsupportedAppUsage
    final ArrayMap<IBinder, Service> mServices = new ArrayMap<>();
    @UnsupportedAppUsage
    final ArrayList<Application> mAllApplications = new ArrayList<>();
    private final SparseArray<ArrayMap<String, BackupAgent>> mBackupAgentsByUser = new SparseArray<>();
    String mInstrumentationPackageName = null;
    @UnsupportedAppUsage
    String mInstrumentationAppDir = null;
    String[] mInstrumentationSplitAppDirs = null;
    String mInstrumentationLibDir = null;
    @UnsupportedAppUsage
    String mInstrumentedAppDir = null;
    String[] mInstrumentedSplitAppDirs = null;
    String mInstrumentedLibDir = null;
    boolean mSystemThread = false;
    boolean mSomeActivitiesChanged = false;
    boolean mUpdatingSystemConfig = false;
    boolean mHiddenApiWarningShown = false;
    @UnsupportedAppUsage
    @GuardedBy({"mResourcesManager"})
    final ArrayMap<String, WeakReference<LoadedApk>> mPackages = new ArrayMap<>();
    @UnsupportedAppUsage
    @GuardedBy({"mResourcesManager"})
    final ArrayMap<String, WeakReference<LoadedApk>> mResourcePackages = new ArrayMap<>();
    @GuardedBy({"mResourcesManager"})
    final ArrayList<ActivityClientRecord> mRelaunchingActivities = new ArrayList<>();
    @UnsupportedAppUsage
    @GuardedBy({"mResourcesManager"})
    Configuration mPendingConfiguration = null;
    private final TransactionExecutor mTransactionExecutor = new TransactionExecutor(this);
    @UnsupportedAppUsage
    final ArrayMap<ProviderKey, ProviderClientRecord> mProviderMap = new ArrayMap<>();
    @UnsupportedAppUsage
    final ArrayMap<IBinder, ProviderRefCount> mProviderRefCountMap = new ArrayMap<>();
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    final ArrayMap<IBinder, ProviderClientRecord> mLocalProviders = new ArrayMap<>();
    @UnsupportedAppUsage
    final ArrayMap<ComponentName, ProviderClientRecord> mLocalProvidersByName = new ArrayMap<>();
    @GuardedBy({"mGetProviderLocks"})
    final ArrayMap<ProviderKey, Object> mGetProviderLocks = new ArrayMap<>();
    final ArrayMap<Activity, ArrayList<OnActivityPausedListener>> mOnPauseListeners = new ArrayMap<>();
    final GcIdler mGcIdler = new GcIdler();
    final PurgeIdler mPurgeIdler = new PurgeIdler();
    boolean mPurgeIdlerScheduled = false;
    boolean mGcIdlerScheduled = false;
    Bundle mCoreSettings = null;
    private xpActivityThreadProxy mActivityThreadProxy = new xpActivityThreadProxy();
    private Configuration mMainThreadConfig = new Configuration();
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final ResourcesManager mResourcesManager = ResourcesManager.getInstance();

    /* JADX INFO: Access modifiers changed from: private */
    public native void nDumpGraphicsInfo(FileDescriptor fileDescriptor);

    private native void nInitZygoteChildHeapProfiling();

    private native void nPurgePendingResources();

    static {
        boolean z = localLOGV;
        DEBUG_SERVICE = z;
        DEBUG_PROVIDER = z;
        DEBUG_ORDER = z;
        sCurrentBroadcastIntent = new ThreadLocal<>();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ProviderKey {
        final String authority;
        final int userId;

        public ProviderKey(String authority, int userId) {
            this.authority = authority;
            this.userId = userId;
        }

        public boolean equals(Object o) {
            if (o instanceof ProviderKey) {
                ProviderKey other = (ProviderKey) o;
                return Objects.equals(this.authority, other.authority) && this.userId == other.userId;
            }
            return false;
        }

        public int hashCode() {
            String str = this.authority;
            return (str != null ? str.hashCode() : 0) ^ this.userId;
        }
    }

    /* loaded from: classes.dex */
    public static final class ActivityClientRecord {
        @UnsupportedAppUsage
        Activity activity;
        @UnsupportedAppUsage
        ActivityInfo activityInfo;
        public IBinder assistToken;
        @UnsupportedAppUsage
        CompatibilityInfo compatInfo;
        ViewRootImpl.ActivityConfigCallback configCallback;
        Configuration createdConfig;
        String embeddedID;
        boolean hideForNow;
        int ident;
        @UnsupportedAppUsage
        Intent intent;
        public final boolean isForward;
        boolean isTopResumedActivity;
        Activity.NonConfigurationInstances lastNonConfigurationInstances;
        boolean lastReportedTopResumedState;
        private int mLifecycleState;
        @GuardedBy({"this"})
        private Configuration mPendingOverrideConfig;
        Window mPendingRemoveWindow;
        WindowManager mPendingRemoveWindowManager;
        @UnsupportedAppUsage
        boolean mPreserveWindow;
        Configuration newConfig;
        ActivityClientRecord nextIdle;
        Configuration overrideConfig;
        @UnsupportedAppUsage
        public LoadedApk packageInfo;
        Activity parent;
        @UnsupportedAppUsage
        boolean paused;
        int pendingConfigChanges;
        List<ReferrerIntent> pendingIntents;
        List<ResultInfo> pendingResults;
        PersistableBundle persistentState;
        ProfilerInfo profilerInfo;
        String referrer;
        boolean startsNotResumed;
        Bundle state;
        @UnsupportedAppUsage
        boolean stopped;
        private Configuration tmpConfig;
        @UnsupportedAppUsage
        public IBinder token;
        IVoiceInteractor voiceInteractor;
        Window window;

        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        @VisibleForTesting
        public ActivityClientRecord() {
            this.tmpConfig = new Configuration();
            this.mLifecycleState = 0;
            this.isForward = false;
            init();
        }

        public ActivityClientRecord(IBinder token, Intent intent, int ident, ActivityInfo info, Configuration overrideConfig, CompatibilityInfo compatInfo, String referrer, IVoiceInteractor voiceInteractor, Bundle state, PersistableBundle persistentState, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, boolean isForward, ProfilerInfo profilerInfo, ClientTransactionHandler client, IBinder assistToken) {
            this.tmpConfig = new Configuration();
            this.mLifecycleState = 0;
            this.token = token;
            this.assistToken = assistToken;
            this.ident = ident;
            this.intent = intent;
            this.referrer = referrer;
            this.voiceInteractor = voiceInteractor;
            this.activityInfo = info;
            this.compatInfo = compatInfo;
            this.state = state;
            this.persistentState = persistentState;
            this.pendingResults = pendingResults;
            this.pendingIntents = pendingNewIntents;
            this.isForward = isForward;
            this.profilerInfo = profilerInfo;
            this.overrideConfig = xpActivityManager.getOverrideConfiguration(overrideConfig, null);
            this.packageInfo = client.getPackageInfoNoCheck(this.activityInfo.applicationInfo, compatInfo);
            init();
        }

        private void init() {
            this.parent = null;
            this.embeddedID = null;
            this.paused = false;
            this.stopped = false;
            this.hideForNow = false;
            this.nextIdle = null;
            this.configCallback = new ViewRootImpl.ActivityConfigCallback() { // from class: android.app.-$$Lambda$ActivityThread$ActivityClientRecord$HOrG1qglSjSUHSjKBn2rXtX0gGg
                @Override // android.view.ViewRootImpl.ActivityConfigCallback
                public final void onConfigurationChanged(Configuration configuration, int i) {
                    ActivityThread.ActivityClientRecord.this.lambda$init$0$ActivityThread$ActivityClientRecord(configuration, i);
                }
            };
        }

        public /* synthetic */ void lambda$init$0$ActivityThread$ActivityClientRecord(Configuration overrideConfig, int newDisplayId) {
            Activity activity = this.activity;
            if (activity == null) {
                throw new IllegalStateException("Received config update for non-existing activity");
            }
            activity.mMainThread.handleActivityConfigurationChanged(this.token, overrideConfig, newDisplayId);
        }

        public int getLifecycleState() {
            return this.mLifecycleState;
        }

        public void setState(int newLifecycleState) {
            this.mLifecycleState = newLifecycleState;
            int i = this.mLifecycleState;
            if (i == 1) {
                this.paused = true;
                this.stopped = true;
            } else if (i == 2) {
                this.paused = true;
                this.stopped = false;
            } else if (i == 3) {
                this.paused = false;
                this.stopped = false;
            } else if (i == 4) {
                this.paused = true;
                this.stopped = false;
            } else if (i == 5) {
                this.paused = true;
                this.stopped = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isPreHoneycomb() {
            Activity activity = this.activity;
            return activity != null && activity.getApplicationInfo().targetSdkVersion < 11;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isPreP() {
            Activity activity = this.activity;
            return activity != null && activity.getApplicationInfo().targetSdkVersion < 28;
        }

        public boolean isPersistable() {
            return this.activityInfo.persistableMode == 2;
        }

        public boolean isVisibleFromServer() {
            Activity activity = this.activity;
            return activity != null && activity.mVisibleFromServer;
        }

        public Intent getIntent() {
            return this.intent;
        }

        public Window getWindow() {
            return this.window;
        }

        public ActivityInfo getActivityInfo() {
            return this.activityInfo;
        }

        public Activity getActivity() {
            return this.activity;
        }

        public String toString() {
            Intent intent = this.intent;
            ComponentName componentName = intent != null ? intent.getComponent() : null;
            StringBuilder sb = new StringBuilder();
            sb.append("ActivityRecord{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" token=");
            sb.append(this.token);
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb.append(componentName == null ? "no component name" : componentName.toShortString());
            sb.append("}");
            return sb.toString();
        }

        public String getStateString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ActivityClientRecord{");
            sb.append("paused=");
            sb.append(this.paused);
            sb.append(", stopped=");
            sb.append(this.stopped);
            sb.append(", hideForNow=");
            sb.append(this.hideForNow);
            sb.append(", startsNotResumed=");
            sb.append(this.startsNotResumed);
            sb.append(", isForward=");
            sb.append(this.isForward);
            sb.append(", pendingConfigChanges=");
            sb.append(this.pendingConfigChanges);
            sb.append(", preserveWindow=");
            sb.append(this.mPreserveWindow);
            if (this.activity != null) {
                sb.append(", Activity{");
                sb.append("resumed=");
                sb.append(this.activity.mResumed);
                sb.append(", stopped=");
                sb.append(this.activity.mStopped);
                sb.append(", finished=");
                sb.append(this.activity.isFinishing());
                sb.append(", destroyed=");
                sb.append(this.activity.isDestroyed());
                sb.append(", startedActivity=");
                sb.append(this.activity.mStartedActivity);
                sb.append(", changingConfigurations=");
                sb.append(this.activity.mChangingConfigurations);
                sb.append("}");
            }
            sb.append("}");
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class ProviderClientRecord {
        @UnsupportedAppUsage
        final ContentProviderHolder mHolder;
        @UnsupportedAppUsage
        final ContentProvider mLocalProvider;
        final String[] mNames;
        @UnsupportedAppUsage
        final IContentProvider mProvider;

        ProviderClientRecord(String[] names, IContentProvider provider, ContentProvider localProvider, ContentProviderHolder holder) {
            this.mNames = names;
            this.mProvider = provider;
            this.mLocalProvider = localProvider;
            this.mHolder = holder;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ReceiverData extends BroadcastReceiver.PendingResult {
        @UnsupportedAppUsage
        CompatibilityInfo compatInfo;
        @UnsupportedAppUsage
        ActivityInfo info;
        @UnsupportedAppUsage
        Intent intent;

        public ReceiverData(Intent intent, int resultCode, String resultData, Bundle resultExtras, boolean ordered, boolean sticky, IBinder token, int sendingUser) {
            super(resultCode, resultData, resultExtras, 0, ordered, sticky, token, sendingUser, intent.getFlags());
            this.intent = intent;
        }

        public String toString() {
            return "ReceiverData{intent=" + this.intent + " packageName=" + this.info.packageName + " resultCode=" + getResultCode() + " resultData=" + getResultData() + " resultExtras=" + getResultExtras(false) + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class CreateBackupAgentData {
        ApplicationInfo appInfo;
        int backupMode;
        CompatibilityInfo compatInfo;
        int userId;

        CreateBackupAgentData() {
        }

        public String toString() {
            return "CreateBackupAgentData{appInfo=" + this.appInfo + " backupAgent=" + this.appInfo.backupAgentName + " mode=" + this.backupMode + " userId=" + this.userId + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class CreateServiceData {
        @UnsupportedAppUsage
        CompatibilityInfo compatInfo;
        @UnsupportedAppUsage
        ServiceInfo info;
        @UnsupportedAppUsage
        Intent intent;
        @UnsupportedAppUsage
        IBinder token;

        CreateServiceData() {
        }

        public String toString() {
            return "CreateServiceData{token=" + this.token + " className=" + this.info.name + " packageName=" + this.info.packageName + " intent=" + this.intent + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class BindServiceData {
        @UnsupportedAppUsage
        Intent intent;
        boolean rebind;
        @UnsupportedAppUsage
        IBinder token;

        BindServiceData() {
        }

        public String toString() {
            return "BindServiceData{token=" + this.token + " intent=" + this.intent + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ServiceArgsData {
        @UnsupportedAppUsage
        Intent args;
        int flags;
        int startId;
        boolean taskRemoved;
        @UnsupportedAppUsage
        IBinder token;

        ServiceArgsData() {
        }

        public String toString() {
            return "ServiceArgsData{token=" + this.token + " startId=" + this.startId + " args=" + this.args + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class AppBindData {
        @UnsupportedAppUsage
        ApplicationInfo appInfo;
        AutofillOptions autofillOptions;
        String buildSerial;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        CompatibilityInfo compatInfo;
        Configuration config;
        ContentCaptureOptions contentCaptureOptions;
        int debugMode;
        boolean enableBinderTracking;
        @UnsupportedAppUsage
        LoadedApk info;
        ProfilerInfo initProfilerInfo;
        @UnsupportedAppUsage
        Bundle instrumentationArgs;
        ComponentName instrumentationName;
        IUiAutomationConnection instrumentationUiAutomationConnection;
        IInstrumentationWatcher instrumentationWatcher;
        @UnsupportedAppUsage
        boolean persistent;
        @UnsupportedAppUsage
        String processName;
        @UnsupportedAppUsage
        List<ProviderInfo> providers;
        @UnsupportedAppUsage
        boolean restrictedBackupMode;
        boolean trackAllocation;

        AppBindData() {
        }

        public String toString() {
            return "AppBindData{appInfo=" + this.appInfo + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class Profiler {
        boolean autoStopProfiler;
        boolean handlingProfiling;
        ParcelFileDescriptor profileFd;
        String profileFile;
        boolean profiling;
        int samplingInterval;
        boolean streamingOutput;

        Profiler() {
        }

        public void setProfiler(ProfilerInfo profilerInfo) {
            ParcelFileDescriptor fd = profilerInfo.profileFd;
            if (this.profiling) {
                if (fd != null) {
                    try {
                        fd.close();
                        return;
                    } catch (IOException e) {
                        return;
                    }
                }
                return;
            }
            ParcelFileDescriptor parcelFileDescriptor = this.profileFd;
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e2) {
                }
            }
            this.profileFile = profilerInfo.profileFile;
            this.profileFd = fd;
            this.samplingInterval = profilerInfo.samplingInterval;
            this.autoStopProfiler = profilerInfo.autoStopProfiler;
            this.streamingOutput = profilerInfo.streamingOutput;
        }

        public void startProfiling() {
            if (this.profileFd == null || this.profiling) {
                return;
            }
            try {
                int bufferSize = SystemProperties.getInt("debug.traceview-buffer-size-mb", 8);
                VMDebug.startMethodTracing(this.profileFile, this.profileFd.getFileDescriptor(), bufferSize * 1024 * 1024, 0, this.samplingInterval != 0, this.samplingInterval, this.streamingOutput);
                this.profiling = true;
            } catch (RuntimeException e) {
                Slog.w(ActivityThread.TAG, "Profiling failed on path " + this.profileFile, e);
                try {
                    this.profileFd.close();
                    this.profileFd = null;
                } catch (IOException e2) {
                    Slog.w(ActivityThread.TAG, "Failure closing profile fd", e2);
                }
            }
        }

        public void stopProfiling() {
            if (this.profiling) {
                this.profiling = false;
                Debug.stopMethodTracing();
                ParcelFileDescriptor parcelFileDescriptor = this.profileFd;
                if (parcelFileDescriptor != null) {
                    try {
                        parcelFileDescriptor.close();
                    } catch (IOException e) {
                    }
                }
                this.profileFd = null;
                this.profileFile = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class DumpComponentInfo {
        String[] args;
        ParcelFileDescriptor fd;
        String prefix;
        IBinder token;

        DumpComponentInfo() {
        }
    }

    /* loaded from: classes.dex */
    static final class ContextCleanupInfo {
        ContextImpl context;
        String what;
        String who;

        ContextCleanupInfo() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class DumpHeapData {
        ParcelFileDescriptor fd;
        RemoteCallback finishCallback;
        public boolean mallocInfo;
        public boolean managed;
        String path;
        public boolean runGc;

        DumpHeapData() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class UpdateCompatibilityData {
        CompatibilityInfo info;
        String pkg;

        UpdateCompatibilityData() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class RequestAssistContextExtras {
        IBinder activityToken;
        int flags;
        IBinder requestToken;
        int requestType;
        int sessionId;

        RequestAssistContextExtras() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ApplicationThread extends IApplicationThread.Stub {
        private static final String DB_INFO_FORMAT = "  %8s %8s %14s %14s  %s";

        private ApplicationThread() {
        }

        @Override // android.app.IApplicationThread
        public final void scheduleSleeping(IBinder token, boolean sleeping) {
            ActivityThread.this.sendMessage(137, token, sleeping ? 1 : 0);
        }

        @Override // android.app.IApplicationThread
        public final void scheduleReceiver(Intent intent, ActivityInfo info, CompatibilityInfo compatInfo, int resultCode, String data, Bundle extras, boolean sync, int sendingUser, int processState) {
            ActivityThread.this.updateProcessState(processState, false);
            ReceiverData r = new ReceiverData(intent, resultCode, data, extras, sync, false, ActivityThread.this.mAppThread.asBinder(), sendingUser);
            r.info = info;
            r.compatInfo = compatInfo;
            ActivityThread.this.sendMessage(113, r);
        }

        @Override // android.app.IApplicationThread
        public final void scheduleCreateBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int backupMode, int userId) {
            CreateBackupAgentData d = new CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;
            d.backupMode = backupMode;
            d.userId = userId;
            ActivityThread.this.sendMessage(128, d);
        }

        @Override // android.app.IApplicationThread
        public final void scheduleDestroyBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int userId) {
            CreateBackupAgentData d = new CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;
            d.userId = userId;
            ActivityThread.this.sendMessage(129, d);
        }

        @Override // android.app.IApplicationThread
        public final void scheduleCreateService(IBinder token, ServiceInfo info, CompatibilityInfo compatInfo, int processState) {
            ActivityThread.this.updateProcessState(processState, false);
            CreateServiceData s = new CreateServiceData();
            s.token = token;
            s.info = info;
            s.compatInfo = compatInfo;
            ActivityThread.this.sendMessage(114, s);
        }

        @Override // android.app.IApplicationThread
        public final void scheduleBindService(IBinder token, Intent intent, boolean rebind, int processState) {
            ActivityThread.this.updateProcessState(processState, false);
            BindServiceData s = new BindServiceData();
            s.token = token;
            s.intent = intent;
            s.rebind = rebind;
            if (ActivityThread.DEBUG_SERVICE) {
                Slog.v(ActivityThread.TAG, "scheduleBindService token=" + token + " intent=" + intent + " uid=" + Binder.getCallingUid() + " pid=" + Binder.getCallingPid());
            }
            ActivityThread.this.sendMessage(121, s);
        }

        @Override // android.app.IApplicationThread
        public final void scheduleUnbindService(IBinder token, Intent intent) {
            BindServiceData s = new BindServiceData();
            s.token = token;
            s.intent = intent;
            ActivityThread.this.sendMessage(122, s);
        }

        @Override // android.app.IApplicationThread
        public final void scheduleServiceArgs(IBinder token, ParceledListSlice args) {
            List<ServiceStartArgs> list = args.getList();
            for (int i = 0; i < list.size(); i++) {
                ServiceStartArgs ssa = list.get(i);
                ServiceArgsData s = new ServiceArgsData();
                s.token = token;
                s.taskRemoved = ssa.taskRemoved;
                s.startId = ssa.startId;
                s.flags = ssa.flags;
                s.args = ssa.args;
                ActivityThread.this.sendMessage(115, s);
            }
        }

        @Override // android.app.IApplicationThread
        public final void scheduleStopService(IBinder token) {
            ActivityThread.this.sendMessage(116, token);
        }

        @Override // android.app.IApplicationThread
        public final void bindApplication(String processName, ApplicationInfo appInfo, List<ProviderInfo> providers, ComponentName instrumentationName, ProfilerInfo profilerInfo, Bundle instrumentationArgs, IInstrumentationWatcher instrumentationWatcher, IUiAutomationConnection instrumentationUiConnection, int debugMode, boolean enableBinderTracking, boolean trackAllocation, boolean isRestrictedBackupMode, boolean persistent, Configuration config, CompatibilityInfo compatInfo, Map services, Bundle coreSettings, String buildSerial, AutofillOptions autofillOptions, ContentCaptureOptions contentCaptureOptions) {
            if (services != null) {
                ServiceManager.initServiceCache(services);
            }
            setCoreSettings(coreSettings);
            AppBindData data = new AppBindData();
            data.processName = processName;
            data.appInfo = appInfo;
            data.providers = providers;
            data.instrumentationName = instrumentationName;
            data.instrumentationArgs = instrumentationArgs;
            data.instrumentationWatcher = instrumentationWatcher;
            data.instrumentationUiAutomationConnection = instrumentationUiConnection;
            data.debugMode = debugMode;
            data.enableBinderTracking = enableBinderTracking;
            data.trackAllocation = trackAllocation;
            data.restrictedBackupMode = isRestrictedBackupMode;
            data.persistent = persistent;
            data.config = config;
            data.config = xpActivityManager.getOverrideConfiguration(config, null);
            data.compatInfo = compatInfo;
            data.initProfilerInfo = profilerInfo;
            data.buildSerial = buildSerial;
            data.autofillOptions = autofillOptions;
            data.contentCaptureOptions = contentCaptureOptions;
            ActivityThread.this.sendMessage(110, data);
        }

        @Override // android.app.IApplicationThread
        public final void runIsolatedEntryPoint(String entryPoint, String[] entryPointArgs) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = entryPoint;
            args.arg2 = entryPointArgs;
            ActivityThread.this.sendMessage(158, args);
        }

        @Override // android.app.IApplicationThread
        public final void scheduleExit() {
            ActivityThread.this.sendMessage(111, null);
        }

        @Override // android.app.IApplicationThread
        public final void scheduleSuicide() {
            ActivityThread.this.sendMessage(130, null);
        }

        @Override // android.app.IApplicationThread
        public void scheduleApplicationInfoChanged(ApplicationInfo ai) {
            ActivityThread.this.mH.removeMessages(156, ai);
            ActivityThread.this.sendMessage(156, ai);
        }

        @Override // android.app.IApplicationThread
        public void updateTimeZone() {
            TimeZone.setDefault(null);
        }

        @Override // android.app.IApplicationThread
        public void clearDnsCache() {
            InetAddress.clearDnsCache();
            NetworkEventDispatcher.getInstance().onNetworkConfigurationChanged();
        }

        @Override // android.app.IApplicationThread
        public void updateHttpProxy() {
            ActivityThread.updateHttpProxy(ActivityThread.this.getApplication() != null ? ActivityThread.this.getApplication() : ActivityThread.this.getSystemContext());
        }

        @Override // android.app.IApplicationThread
        public void processInBackground() {
            ActivityThread.this.mH.removeMessages(120);
            ActivityThread.this.mH.sendMessage(ActivityThread.this.mH.obtainMessage(120));
        }

        @Override // android.app.IApplicationThread
        public void dumpService(ParcelFileDescriptor pfd, IBinder servicetoken, String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                try {
                    data.fd = pfd.dup();
                    data.token = servicetoken;
                    data.args = args;
                    ActivityThread.this.sendMessage(123, (Object) data, 0, 0, true);
                } catch (IOException e) {
                    Slog.w(ActivityThread.TAG, "dumpService failed", e);
                }
            } finally {
                IoUtils.closeQuietly(pfd);
            }
        }

        @Override // android.app.IApplicationThread
        public void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent, int resultCode, String dataStr, Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws RemoteException {
            ActivityThread.this.updateProcessState(processState, false);
            receiver.performReceive(intent, resultCode, dataStr, extras, ordered, sticky, sendingUser);
        }

        @Override // android.app.IApplicationThread
        public void scheduleLowMemory() {
            ActivityThread.this.sendMessage(124, null);
        }

        @Override // android.app.IApplicationThread
        public void profilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) {
            ActivityThread.this.sendMessage(127, profilerInfo, start ? 1 : 0, profileType);
        }

        @Override // android.app.IApplicationThread
        public void dumpHeap(boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) {
            DumpHeapData dhd = new DumpHeapData();
            dhd.managed = managed;
            dhd.mallocInfo = mallocInfo;
            dhd.runGc = runGc;
            dhd.path = path;
            try {
                dhd.fd = fd.dup();
                dhd.finishCallback = finishCallback;
                ActivityThread.this.sendMessage(135, (Object) dhd, 0, 0, true);
            } catch (IOException e) {
                Slog.e(ActivityThread.TAG, "Failed to duplicate heap dump file descriptor", e);
            }
        }

        @Override // android.app.IApplicationThread
        public void attachAgent(String agent) {
            ActivityThread.this.sendMessage(155, agent);
        }

        @Override // android.app.IApplicationThread
        public void setSchedulingGroup(int group) {
            try {
                Process.setProcessGroup(Process.myPid(), group);
            } catch (Exception e) {
                Slog.w(ActivityThread.TAG, "Failed setting process group to " + group, e);
            }
        }

        @Override // android.app.IApplicationThread
        public void dispatchPackageBroadcast(int cmd, String[] packages) {
            ActivityThread.this.sendMessage(133, packages, cmd);
        }

        @Override // android.app.IApplicationThread
        public void scheduleCrash(String msg) {
            ActivityThread.this.sendMessage(134, msg);
        }

        @Override // android.app.IApplicationThread
        public void dumpActivity(ParcelFileDescriptor pfd, IBinder activitytoken, String prefix, String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                try {
                    data.fd = pfd.dup();
                    data.token = activitytoken;
                    data.prefix = prefix;
                    data.args = args;
                    ActivityThread.this.sendMessage(136, (Object) data, 0, 0, true);
                } catch (IOException e) {
                    Slog.w(ActivityThread.TAG, "dumpActivity failed", e);
                }
            } finally {
                IoUtils.closeQuietly(pfd);
            }
        }

        @Override // android.app.IApplicationThread
        public void dumpProvider(ParcelFileDescriptor pfd, IBinder providertoken, String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                try {
                    data.fd = pfd.dup();
                    data.token = providertoken;
                    data.args = args;
                    ActivityThread.this.sendMessage(141, (Object) data, 0, 0, true);
                } catch (IOException e) {
                    Slog.w(ActivityThread.TAG, "dumpProvider failed", e);
                }
            } finally {
                IoUtils.closeQuietly(pfd);
            }
        }

        @Override // android.app.IApplicationThread
        public void dumpMemInfo(ParcelFileDescriptor pfd, Debug.MemoryInfo mem, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) {
            FileOutputStream fout = new FileOutputStream(pfd.getFileDescriptor());
            PrintWriter pw = new FastPrintWriter(fout);
            try {
                dumpMemInfo(pw, mem, checkin, dumpFullInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable);
            } finally {
                pw.flush();
                IoUtils.closeQuietly(pfd);
            }
        }

        private void dumpMemInfo(PrintWriter pw, Debug.MemoryInfo memInfo, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable) {
            String str;
            int i;
            String str2;
            long nativeMax = Debug.getNativeHeapSize() / 1024;
            long nativeAllocated = Debug.getNativeHeapAllocatedSize() / 1024;
            long nativeFree = Debug.getNativeHeapFreeSize() / 1024;
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long dalvikMax = runtime.totalMemory() / 1024;
            long dalvikFree = runtime.freeMemory() / 1024;
            long dalvikAllocated = dalvikMax - dalvikFree;
            Class[] classesToCount = {ContextImpl.class, Activity.class, WebView.class, OpenSSLSocketImpl.class};
            long[] instanceCounts = VMDebug.countInstancesOfClasses(classesToCount, true);
            long appContextInstanceCount = instanceCounts[0];
            long activityInstanceCount = instanceCounts[1];
            long webviewInstanceCount = instanceCounts[2];
            long openSslSocketCount = instanceCounts[3];
            long viewInstanceCount = ViewDebug.getViewInstanceCount();
            long viewRootInstanceCount = ViewDebug.getViewRootImplCount();
            int globalAssetCount = AssetManager.getGlobalAssetCount();
            int globalAssetManagerCount = AssetManager.getGlobalAssetManagerCount();
            int binderLocalObjectCount = Debug.getBinderLocalObjectCount();
            int binderProxyObjectCount = Debug.getBinderProxyObjectCount();
            int binderDeathObjectCount = Debug.getBinderDeathObjectCount();
            long parcelSize = Parcel.getGlobalAllocSize();
            long parcelCount = Parcel.getGlobalAllocCount();
            SQLiteDebug.PagerStats stats = SQLiteDebug.getDatabaseInfo();
            boolean z = true;
            ActivityThread.dumpMemInfoTable(pw, memInfo, checkin, dumpFullInfo, dumpDalvik, dumpSummaryOnly, Process.myPid(), ActivityThread.this.mBoundApplication != null ? ActivityThread.this.mBoundApplication.processName : "unknown", nativeMax, nativeAllocated, nativeFree, dalvikMax, dalvikAllocated, dalvikFree);
            if (checkin) {
                pw.print(viewInstanceCount);
                pw.print(',');
                long viewRootInstanceCount2 = viewRootInstanceCount;
                pw.print(viewRootInstanceCount2);
                pw.print(',');
                pw.print(appContextInstanceCount);
                pw.print(',');
                pw.print(activityInstanceCount);
                pw.print(',');
                pw.print(globalAssetCount);
                pw.print(',');
                pw.print(globalAssetManagerCount);
                pw.print(',');
                pw.print(binderLocalObjectCount);
                pw.print(',');
                pw.print(binderProxyObjectCount);
                pw.print(',');
                pw.print(binderDeathObjectCount);
                pw.print(',');
                pw.print(openSslSocketCount);
                pw.print(',');
                SQLiteDebug.PagerStats stats2 = stats;
                pw.print(stats2.memoryUsed / 1024);
                pw.print(',');
                pw.print(stats2.memoryUsed / 1024);
                pw.print(',');
                pw.print(stats2.pageCacheOverflow / 1024);
                pw.print(',');
                pw.print(stats2.largestMemAlloc / 1024);
                int i2 = 0;
                while (i2 < stats2.dbStats.size()) {
                    SQLiteDebug.DbStats dbStats = stats2.dbStats.get(i2);
                    pw.print(',');
                    pw.print(dbStats.dbName);
                    pw.print(',');
                    pw.print(dbStats.pageSize);
                    pw.print(',');
                    pw.print(dbStats.dbSize);
                    pw.print(',');
                    pw.print(dbStats.lookaside);
                    pw.print(',');
                    pw.print(dbStats.cache);
                    pw.print(',');
                    pw.print(dbStats.cache);
                    i2++;
                    viewRootInstanceCount2 = viewRootInstanceCount2;
                    stats2 = stats2;
                }
                pw.println();
                return;
            }
            String str3 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            pw.println(" Objects");
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Views:", Long.valueOf(viewInstanceCount), "ViewRootImpl:", Long.valueOf(viewRootInstanceCount));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "AppContexts:", Long.valueOf(appContextInstanceCount), "Activities:", Long.valueOf(activityInstanceCount));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Assets:", Integer.valueOf(globalAssetCount), "AssetManagers:", Integer.valueOf(globalAssetManagerCount));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Local Binders:", Integer.valueOf(binderLocalObjectCount), "Proxy Binders:", Integer.valueOf(binderProxyObjectCount));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Parcel memory:", Long.valueOf(parcelSize / 1024), "Parcel count:", Long.valueOf(parcelCount));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Death Recipients:", Integer.valueOf(binderDeathObjectCount), "OpenSSL Sockets:", Long.valueOf(openSslSocketCount));
            ActivityThread.printRow(pw, ActivityThread.ONE_COUNT_COLUMN, "WebViews:", Long.valueOf(webviewInstanceCount));
            pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            pw.println(" SQL");
            ActivityThread.printRow(pw, ActivityThread.ONE_COUNT_COLUMN, "MEMORY_USED:", Integer.valueOf(stats.memoryUsed / 1024));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "PAGECACHE_OVERFLOW:", Integer.valueOf(stats.pageCacheOverflow / 1024), "MALLOC_SIZE:", Integer.valueOf(stats.largestMemAlloc / 1024));
            pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            int N = stats.dbStats.size();
            if (N > 0) {
                pw.println(" DATABASES");
                int i3 = 5;
                ActivityThread.printRow(pw, DB_INFO_FORMAT, "pgsz", "dbsz", "Lookaside(b)", "cache", "Dbname");
                int i4 = 0;
                while (i4 < N) {
                    SQLiteDebug.DbStats dbStats2 = stats.dbStats.get(i4);
                    int N2 = N;
                    Object[] objArr = new Object[i3];
                    String str4 = str3;
                    objArr[0] = dbStats2.pageSize > 0 ? String.valueOf(dbStats2.pageSize) : str4;
                    objArr[1] = dbStats2.dbSize > 0 ? String.valueOf(dbStats2.dbSize) : str4;
                    objArr[2] = dbStats2.lookaside > 0 ? String.valueOf(dbStats2.lookaside) : str4;
                    objArr[3] = dbStats2.cache;
                    objArr[4] = dbStats2.dbName;
                    ActivityThread.printRow(pw, DB_INFO_FORMAT, objArr);
                    i4++;
                    N = N2;
                    str3 = str4;
                    i3 = 5;
                }
                str = str3;
                i = 2;
            } else {
                str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                i = 2;
            }
            String assetAlloc = AssetManager.getAssetAllocations();
            if (assetAlloc == null) {
                str2 = str;
            } else {
                str2 = str;
                pw.println(str2);
                pw.println(" Asset Allocations");
                pw.print(assetAlloc);
            }
            if (dumpUnreachable) {
                int i5 = i;
                if ((ActivityThread.this.mBoundApplication == null || (i5 & ActivityThread.this.mBoundApplication.appInfo.flags) == 0) && !Build.IS_DEBUGGABLE) {
                    z = false;
                }
                boolean showContents = z;
                pw.println(str2);
                pw.println(" Unreachable memory");
                pw.print(Debug.getUnreachableMemory(100, showContents));
            }
        }

        @Override // android.app.IApplicationThread
        public void dumpMemInfoProto(ParcelFileDescriptor pfd, Debug.MemoryInfo mem, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) {
            ProtoOutputStream proto = new ProtoOutputStream(pfd.getFileDescriptor());
            try {
                dumpMemInfo(proto, mem, dumpFullInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable);
            } finally {
                proto.flush();
                IoUtils.closeQuietly(pfd);
            }
        }

        private void dumpMemInfo(ProtoOutputStream proto, Debug.MemoryInfo memInfo, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable) {
            long nativeMax = Debug.getNativeHeapSize() / 1024;
            long nativeAllocated = Debug.getNativeHeapAllocatedSize() / 1024;
            long nativeFree = Debug.getNativeHeapFreeSize() / 1024;
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long dalvikMax = runtime.totalMemory() / 1024;
            long dalvikFree = runtime.freeMemory() / 1024;
            long dalvikAllocated = dalvikMax - dalvikFree;
            boolean z = false;
            Class[] classesToCount = {ContextImpl.class, Activity.class, WebView.class, OpenSSLSocketImpl.class};
            long[] instanceCounts = VMDebug.countInstancesOfClasses(classesToCount, true);
            long appContextInstanceCount = instanceCounts[0];
            long activityInstanceCount = instanceCounts[1];
            long webviewInstanceCount = instanceCounts[2];
            long openSslSocketCount = instanceCounts[3];
            long viewInstanceCount = ViewDebug.getViewInstanceCount();
            long viewRootInstanceCount = ViewDebug.getViewRootImplCount();
            int globalAssetCount = AssetManager.getGlobalAssetCount();
            int globalAssetManagerCount = AssetManager.getGlobalAssetManagerCount();
            int binderLocalObjectCount = Debug.getBinderLocalObjectCount();
            int binderProxyObjectCount = Debug.getBinderProxyObjectCount();
            int binderDeathObjectCount = Debug.getBinderDeathObjectCount();
            long parcelSize = Parcel.getGlobalAllocSize();
            long parcelCount = Parcel.getGlobalAllocCount();
            SQLiteDebug.PagerStats stats = SQLiteDebug.getDatabaseInfo();
            long mToken = proto.start(1146756268033L);
            proto.write(1120986464257L, Process.myPid());
            proto.write(1138166333442L, ActivityThread.this.mBoundApplication != null ? ActivityThread.this.mBoundApplication.processName : "unknown");
            ActivityThread.dumpMemInfoTable(proto, memInfo, dumpDalvik, dumpSummaryOnly, nativeMax, nativeAllocated, nativeFree, dalvikMax, dalvikAllocated, dalvikFree);
            proto.end(mToken);
            long oToken = proto.start(1146756268034L);
            proto.write(1120986464257L, viewInstanceCount);
            proto.write(1120986464258L, viewRootInstanceCount);
            long appContextInstanceCount2 = appContextInstanceCount;
            proto.write(1120986464259L, appContextInstanceCount2);
            long activityInstanceCount2 = activityInstanceCount;
            proto.write(1120986464260L, activityInstanceCount2);
            proto.write(1120986464261L, globalAssetCount);
            proto.write(1120986464262L, globalAssetManagerCount);
            proto.write(1120986464263L, binderLocalObjectCount);
            proto.write(1120986464264L, binderProxyObjectCount);
            proto.write(1112396529673L, parcelSize / 1024);
            proto.write(1120986464266L, parcelCount);
            proto.write(1120986464267L, binderDeathObjectCount);
            proto.write(1120986464268L, openSslSocketCount);
            proto.write(1120986464269L, webviewInstanceCount);
            proto.end(oToken);
            long sToken = proto.start(1146756268035L);
            SQLiteDebug.PagerStats stats2 = stats;
            proto.write(1120986464257L, stats2.memoryUsed / 1024);
            proto.write(1120986464258L, stats2.pageCacheOverflow / 1024);
            proto.write(1120986464259L, stats2.largestMemAlloc / 1024);
            int n = stats2.dbStats.size();
            int i = 0;
            while (true) {
                long activityInstanceCount3 = activityInstanceCount2;
                if (i >= n) {
                    break;
                }
                SQLiteDebug.DbStats dbStats = stats2.dbStats.get(i);
                long dToken = proto.start(2246267895812L);
                proto.write(1138166333441L, dbStats.dbName);
                proto.write(1120986464258L, dbStats.pageSize);
                proto.write(1120986464259L, dbStats.dbSize);
                proto.write(1120986464260L, dbStats.lookaside);
                proto.write(1138166333445L, dbStats.cache);
                proto.end(dToken);
                i++;
                n = n;
                activityInstanceCount2 = activityInstanceCount3;
                appContextInstanceCount2 = appContextInstanceCount2;
                stats2 = stats2;
            }
            proto.end(sToken);
            String assetAlloc = AssetManager.getAssetAllocations();
            if (assetAlloc != null) {
                proto.write(1138166333444L, assetAlloc);
            }
            if (dumpUnreachable) {
                int flags = ActivityThread.this.mBoundApplication == null ? 0 : ActivityThread.this.mBoundApplication.appInfo.flags;
                if ((flags & 2) != 0 || Build.IS_DEBUGGABLE) {
                    z = true;
                }
                boolean showContents = z;
                proto.write(1138166333445L, Debug.getUnreachableMemory(100, showContents));
            }
        }

        @Override // android.app.IApplicationThread
        public void dumpGfxInfo(ParcelFileDescriptor pfd, String[] args) {
            ActivityThread.this.nDumpGraphicsInfo(pfd.getFileDescriptor());
            WindowManagerGlobal.getInstance().dumpGfxInfo(pfd.getFileDescriptor(), args);
            IoUtils.closeQuietly(pfd);
        }

        private File getDatabasesDir(Context context) {
            return context.getDatabasePath(FullBackup.APK_TREE_TOKEN).getParentFile();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dumpDatabaseInfo(ParcelFileDescriptor pfd, String[] args, boolean isSystem) {
            PrintWriter pw = new FastPrintWriter(new FileOutputStream(pfd.getFileDescriptor()));
            PrintWriterPrinter printer = new PrintWriterPrinter(pw);
            SQLiteDebug.dump(printer, args, isSystem);
            pw.flush();
        }

        @Override // android.app.IApplicationThread
        public void dumpDbInfo(ParcelFileDescriptor pfd, final String[] args) {
            try {
                if (!ActivityThread.this.mSystemThread) {
                    dumpDatabaseInfo(pfd, args, false);
                    return;
                }
                final ParcelFileDescriptor dup = pfd.dup();
                IoUtils.closeQuietly(pfd);
                AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() { // from class: android.app.ActivityThread.ApplicationThread.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            ApplicationThread.this.dumpDatabaseInfo(dup, args, true);
                        } finally {
                            IoUtils.closeQuietly(dup);
                        }
                    }
                });
            } catch (IOException e) {
                Log.w(ActivityThread.TAG, "Could not dup FD " + pfd.getFileDescriptor().getInt$());
            } finally {
                IoUtils.closeQuietly(pfd);
            }
        }

        @Override // android.app.IApplicationThread
        public void unstableProviderDied(IBinder provider) {
            ActivityThread.this.sendMessage(142, provider);
        }

        @Override // android.app.IApplicationThread
        public void requestAssistContextExtras(IBinder activityToken, IBinder requestToken, int requestType, int sessionId, int flags) {
            RequestAssistContextExtras cmd = new RequestAssistContextExtras();
            cmd.activityToken = activityToken;
            cmd.requestToken = requestToken;
            cmd.requestType = requestType;
            cmd.sessionId = sessionId;
            cmd.flags = flags;
            ActivityThread.this.sendMessage(143, cmd);
        }

        @Override // android.app.IApplicationThread
        public void setCoreSettings(Bundle coreSettings) {
            ActivityThread.this.sendMessage(138, coreSettings);
        }

        @Override // android.app.IApplicationThread
        public void updatePackageCompatibilityInfo(String pkg, CompatibilityInfo info) {
            UpdateCompatibilityData ucd = new UpdateCompatibilityData();
            ucd.pkg = pkg;
            ucd.info = info;
            ActivityThread.this.sendMessage(139, ucd);
        }

        @Override // android.app.IApplicationThread
        public void scheduleTrimMemory(int level) {
            Runnable r = PooledLambda.obtainRunnable(new BiConsumer() { // from class: android.app.-$$Lambda$ActivityThread$ApplicationThread$tUGFX7CUhzB4Pg5wFd5yeqOnu38
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((ActivityThread) obj).handleTrimMemory(((Integer) obj2).intValue());
                }
            }, ActivityThread.this, Integer.valueOf(level)).recycleOnUse();
            Choreographer choreographer = Choreographer.getMainThreadInstance();
            if (choreographer != null) {
                choreographer.postCallback(4, r, null);
            } else {
                ActivityThread.this.mH.post(r);
            }
        }

        @Override // android.app.IApplicationThread
        public void scheduleTranslucentConversionComplete(IBinder token, boolean drawComplete) {
            ActivityThread.this.sendMessage(144, token, drawComplete ? 1 : 0);
        }

        @Override // android.app.IApplicationThread
        public void scheduleOnNewActivityOptions(IBinder token, Bundle options) {
            ActivityThread.this.sendMessage(146, new Pair(token, ActivityOptions.fromBundle(options)));
        }

        @Override // android.app.IApplicationThread
        public void setProcessState(int state) {
            ActivityThread.this.updateProcessState(state, true);
        }

        @Override // android.app.IApplicationThread
        public void setNetworkBlockSeq(long procStateSeq) {
            synchronized (ActivityThread.this.mNetworkPolicyLock) {
                ActivityThread.this.mNetworkBlockSeq = procStateSeq;
            }
        }

        @Override // android.app.IApplicationThread
        public void scheduleInstallProvider(ProviderInfo provider) {
            ActivityThread.this.sendMessage(145, provider);
        }

        @Override // android.app.IApplicationThread
        public final void updateTimePrefs(int timeFormatPreference) {
            Boolean timeFormatPreferenceBool;
            if (timeFormatPreference == 0) {
                timeFormatPreferenceBool = Boolean.FALSE;
            } else if (timeFormatPreference == 1) {
                timeFormatPreferenceBool = Boolean.TRUE;
            } else {
                timeFormatPreferenceBool = null;
            }
            DateFormat.set24HourTimePref(timeFormatPreferenceBool);
        }

        @Override // android.app.IApplicationThread
        public void scheduleEnterAnimationComplete(IBinder token) {
            ActivityThread.this.sendMessage(149, token);
        }

        @Override // android.app.IApplicationThread
        public void notifyCleartextNetwork(byte[] firstPacket) {
            if (StrictMode.vmCleartextNetworkEnabled()) {
                StrictMode.onCleartextNetworkDetected(firstPacket);
            }
        }

        @Override // android.app.IApplicationThread
        public void startBinderTracking() {
            ActivityThread.this.sendMessage(150, null);
        }

        @Override // android.app.IApplicationThread
        public void stopBinderTrackingAndDump(ParcelFileDescriptor pfd) {
            try {
                ActivityThread.this.sendMessage(151, pfd.dup());
            } catch (IOException e) {
            } catch (Throwable th) {
                IoUtils.closeQuietly(pfd);
                throw th;
            }
            IoUtils.closeQuietly(pfd);
        }

        @Override // android.app.IApplicationThread
        public void scheduleLocalVoiceInteractionStarted(IBinder token, IVoiceInteractor voiceInteractor) throws RemoteException {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = token;
            args.arg2 = voiceInteractor;
            ActivityThread.this.sendMessage(154, args);
        }

        @Override // android.app.IApplicationThread
        public void handleTrustStorageUpdate() {
            NetworkSecurityPolicy.getInstance().handleTrustStorageUpdate();
        }

        @Override // android.app.IApplicationThread
        public void scheduleTransaction(ClientTransaction transaction) throws RemoteException {
            ActivityThread.this.scheduleTransaction(transaction);
        }

        @Override // android.app.IApplicationThread
        public void requestDirectActions(IBinder activityToken, IVoiceInteractor interactor, RemoteCallback cancellationCallback, RemoteCallback callback) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            if (cancellationCallback != null) {
                ICancellationSignal transport = ActivityThread.this.createSafeCancellationTransport(cancellationSignal);
                Bundle cancellationResult = new Bundle();
                cancellationResult.putBinder(VoiceInteractor.KEY_CANCELLATION_SIGNAL, transport.asBinder());
                cancellationCallback.sendResult(cancellationResult);
            }
            ActivityThread.this.mH.sendMessage(PooledLambda.obtainMessage(new QuintConsumer() { // from class: android.app.-$$Lambda$ActivityThread$ApplicationThread$uR_ee-5oPoxu4U_by7wU55jwtdU
                @Override // com.android.internal.util.function.QuintConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                    ((ActivityThread) obj).handleRequestDirectActions((IBinder) obj2, (IVoiceInteractor) obj3, (CancellationSignal) obj4, (RemoteCallback) obj5);
                }
            }, ActivityThread.this, activityToken, interactor, cancellationSignal, callback));
        }

        @Override // android.app.IApplicationThread
        public void performDirectAction(IBinder activityToken, String actionId, Bundle arguments, RemoteCallback cancellationCallback, RemoteCallback resultCallback) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            if (cancellationCallback != null) {
                ICancellationSignal transport = ActivityThread.this.createSafeCancellationTransport(cancellationSignal);
                Bundle cancellationResult = new Bundle();
                cancellationResult.putBinder(VoiceInteractor.KEY_CANCELLATION_SIGNAL, transport.asBinder());
                cancellationCallback.sendResult(cancellationResult);
            }
            ActivityThread.this.mH.sendMessage(PooledLambda.obtainMessage(new HexConsumer() { // from class: android.app.-$$Lambda$ActivityThread$ApplicationThread$nBC_BR7B9W6ftKAxur3BC53SJYc
                @Override // com.android.internal.util.function.HexConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
                    ((ActivityThread) obj).handlePerformDirectAction((IBinder) obj2, (String) obj3, (Bundle) obj4, (CancellationSignal) obj5, (RemoteCallback) obj6);
                }
            }, ActivityThread.this, activityToken, actionId, arguments, cancellationSignal, resultCallback));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SafeCancellationTransport createSafeCancellationTransport(CancellationSignal cancellationSignal) {
        SafeCancellationTransport transport;
        synchronized (this) {
            if (this.mRemoteCancellations == null) {
                this.mRemoteCancellations = new ArrayMap();
            }
            transport = new SafeCancellationTransport(this, cancellationSignal);
            this.mRemoteCancellations.put(transport, cancellationSignal);
        }
        return transport;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CancellationSignal removeSafeCancellationTransport(SafeCancellationTransport transport) {
        CancellationSignal cancellation;
        synchronized (this) {
            cancellation = this.mRemoteCancellations.remove(transport);
            if (this.mRemoteCancellations.isEmpty()) {
                this.mRemoteCancellations = null;
            }
        }
        return cancellation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SafeCancellationTransport extends ICancellationSignal.Stub {
        private final WeakReference<ActivityThread> mWeakActivityThread;

        SafeCancellationTransport(ActivityThread activityThread, CancellationSignal cancellation) {
            this.mWeakActivityThread = new WeakReference<>(activityThread);
        }

        @Override // android.os.ICancellationSignal
        public void cancel() {
            CancellationSignal cancellation;
            ActivityThread activityThread = this.mWeakActivityThread.get();
            if (activityThread != null && (cancellation = activityThread.removeSafeCancellationTransport(this)) != null) {
                cancellation.cancel();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class H extends Handler {
        public static final int APPLICATION_INFO_CHANGED = 156;
        public static final int ATTACH_AGENT = 155;
        public static final int BIND_APPLICATION = 110;
        @UnsupportedAppUsage
        public static final int BIND_SERVICE = 121;
        public static final int CLEAN_UP_CONTEXT = 119;
        public static final int CONFIGURATION_CHANGED = 118;
        public static final int CREATE_BACKUP_AGENT = 128;
        @UnsupportedAppUsage
        public static final int CREATE_SERVICE = 114;
        public static final int DESTROY_BACKUP_AGENT = 129;
        public static final int DISPATCH_PACKAGE_BROADCAST = 133;
        public static final int DUMP_ACTIVITY = 136;
        public static final int DUMP_HEAP = 135;
        @UnsupportedAppUsage
        public static final int DUMP_PROVIDER = 141;
        public static final int DUMP_SERVICE = 123;
        @UnsupportedAppUsage
        public static final int ENTER_ANIMATION_COMPLETE = 149;
        public static final int EXECUTE_TRANSACTION = 159;
        @UnsupportedAppUsage
        public static final int EXIT_APPLICATION = 111;
        @UnsupportedAppUsage
        public static final int GC_WHEN_IDLE = 120;
        @UnsupportedAppUsage
        public static final int INSTALL_PROVIDER = 145;
        public static final int LOCAL_VOICE_INTERACTION_STARTED = 154;
        public static final int LOW_MEMORY = 124;
        public static final int ON_NEW_ACTIVITY_OPTIONS = 146;
        public static final int PROFILER_CONTROL = 127;
        public static final int PURGE_RESOURCES = 161;
        @UnsupportedAppUsage
        public static final int RECEIVER = 113;
        public static final int RELAUNCH_ACTIVITY = 160;
        @UnsupportedAppUsage
        public static final int REMOVE_PROVIDER = 131;
        public static final int REQUEST_ASSIST_CONTEXT_EXTRAS = 143;
        public static final int RUN_ISOLATED_ENTRY_POINT = 158;
        @UnsupportedAppUsage
        public static final int SCHEDULE_CRASH = 134;
        @UnsupportedAppUsage
        public static final int SERVICE_ARGS = 115;
        public static final int SET_CORE_SETTINGS = 138;
        public static final int SLEEPING = 137;
        public static final int START_BINDER_TRACKING = 150;
        public static final int STOP_BINDER_TRACKING_AND_DUMP = 151;
        @UnsupportedAppUsage
        public static final int STOP_SERVICE = 116;
        public static final int SUICIDE = 130;
        public static final int TRANSLUCENT_CONVERSION_COMPLETE = 144;
        @UnsupportedAppUsage
        public static final int UNBIND_SERVICE = 122;
        public static final int UNSTABLE_PROVIDER_DIED = 142;
        public static final int UPDATE_PACKAGE_COMPATIBILITY_INFO = 139;

        H() {
        }

        String codeToString(int code) {
            return Integer.toString(code);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 110:
                    Trace.traceBegin(64L, "bindApplication");
                    AppBindData data = (AppBindData) msg.obj;
                    ActivityThread.this.handleBindApplication(data);
                    Trace.traceEnd(64L);
                    break;
                case 111:
                    if (ActivityThread.this.mInitialApplication != null) {
                        ActivityThread.this.mInitialApplication.onTerminate();
                    }
                    Looper.myLooper().quit();
                    break;
                case 113:
                    Trace.traceBegin(64L, "broadcastReceiveComp");
                    ActivityThread.this.handleReceiver((ReceiverData) msg.obj);
                    Trace.traceEnd(64L);
                    break;
                case 114:
                    Trace.traceBegin(64L, "serviceCreate: " + String.valueOf(msg.obj));
                    ActivityThread.this.handleCreateService((CreateServiceData) msg.obj);
                    Trace.traceEnd(64L);
                    break;
                case 115:
                    Trace.traceBegin(64L, "serviceStart: " + String.valueOf(msg.obj));
                    ActivityThread.this.handleServiceArgs((ServiceArgsData) msg.obj);
                    Trace.traceEnd(64L);
                    break;
                case 116:
                    Trace.traceBegin(64L, "serviceStop");
                    ActivityThread.this.handleStopService((IBinder) msg.obj);
                    ActivityThread.this.schedulePurgeIdler();
                    Trace.traceEnd(64L);
                    break;
                case 118:
                    ActivityThread.this.handleConfigurationChanged((Configuration) msg.obj);
                    break;
                case 119:
                    ContextCleanupInfo cci = (ContextCleanupInfo) msg.obj;
                    cci.context.performFinalCleanup(cci.who, cci.what);
                    break;
                case 120:
                    ActivityThread.this.scheduleGcIdler();
                    break;
                case 121:
                    Trace.traceBegin(64L, "serviceBind");
                    ActivityThread.this.handleBindService((BindServiceData) msg.obj);
                    Trace.traceEnd(64L);
                    break;
                case 122:
                    Trace.traceBegin(64L, "serviceUnbind");
                    ActivityThread.this.handleUnbindService((BindServiceData) msg.obj);
                    ActivityThread.this.schedulePurgeIdler();
                    Trace.traceEnd(64L);
                    break;
                case 123:
                    ActivityThread.this.handleDumpService((DumpComponentInfo) msg.obj);
                    break;
                case 124:
                    Trace.traceBegin(64L, "lowMemory");
                    ActivityThread.this.handleLowMemory();
                    Trace.traceEnd(64L);
                    break;
                case 127:
                    ActivityThread.this.handleProfilerControl(msg.arg1 != 0, (ProfilerInfo) msg.obj, msg.arg2);
                    break;
                case 128:
                    Trace.traceBegin(64L, "backupCreateAgent");
                    ActivityThread.this.handleCreateBackupAgent((CreateBackupAgentData) msg.obj);
                    Trace.traceEnd(64L);
                    break;
                case 129:
                    Trace.traceBegin(64L, "backupDestroyAgent");
                    ActivityThread.this.handleDestroyBackupAgent((CreateBackupAgentData) msg.obj);
                    Trace.traceEnd(64L);
                    break;
                case 130:
                    Process.killProcess(Process.myPid());
                    break;
                case 131:
                    Trace.traceBegin(64L, "providerRemove");
                    ActivityThread.this.completeRemoveProvider((ProviderRefCount) msg.obj);
                    Trace.traceEnd(64L);
                    break;
                case 133:
                    Trace.traceBegin(64L, "broadcastPackage");
                    ActivityThread.this.handleDispatchPackageBroadcast(msg.arg1, (String[]) msg.obj);
                    Trace.traceEnd(64L);
                    break;
                case 134:
                    throw new RemoteServiceException((String) msg.obj);
                case 135:
                    ActivityThread.handleDumpHeap((DumpHeapData) msg.obj);
                    break;
                case 136:
                    ActivityThread.this.handleDumpActivity((DumpComponentInfo) msg.obj);
                    break;
                case 137:
                    Trace.traceBegin(64L, "sleeping");
                    ActivityThread.this.handleSleeping((IBinder) msg.obj, msg.arg1 != 0);
                    Trace.traceEnd(64L);
                    break;
                case 138:
                    Trace.traceBegin(64L, "setCoreSettings");
                    ActivityThread.this.handleSetCoreSettings((Bundle) msg.obj);
                    Trace.traceEnd(64L);
                    break;
                case 139:
                    ActivityThread.this.handleUpdatePackageCompatibilityInfo((UpdateCompatibilityData) msg.obj);
                    break;
                case 141:
                    ActivityThread.this.handleDumpProvider((DumpComponentInfo) msg.obj);
                    break;
                case 142:
                    ActivityThread.this.handleUnstableProviderDied((IBinder) msg.obj, false);
                    break;
                case 143:
                    ActivityThread.this.handleRequestAssistContextExtras((RequestAssistContextExtras) msg.obj);
                    break;
                case 144:
                    ActivityThread.this.handleTranslucentConversionComplete((IBinder) msg.obj, msg.arg1 == 1);
                    break;
                case 145:
                    ActivityThread.this.handleInstallProvider((ProviderInfo) msg.obj);
                    break;
                case 146:
                    Pair<IBinder, ActivityOptions> pair = (Pair) msg.obj;
                    ActivityThread.this.onNewActivityOptions((IBinder) pair.first, (ActivityOptions) pair.second);
                    break;
                case 149:
                    ActivityThread.this.handleEnterAnimationComplete((IBinder) msg.obj);
                    break;
                case 150:
                    ActivityThread.this.handleStartBinderTracking();
                    break;
                case 151:
                    ActivityThread.this.handleStopBinderTrackingAndDump((ParcelFileDescriptor) msg.obj);
                    break;
                case 154:
                    ActivityThread.this.handleLocalVoiceInteractionStarted((IBinder) ((SomeArgs) msg.obj).arg1, (IVoiceInteractor) ((SomeArgs) msg.obj).arg2);
                    break;
                case 155:
                    Application app = ActivityThread.this.getApplication();
                    ActivityThread.handleAttachAgent((String) msg.obj, app != null ? app.mLoadedApk : null);
                    break;
                case 156:
                    ActivityThread activityThread = ActivityThread.this;
                    activityThread.mUpdatingSystemConfig = true;
                    try {
                        activityThread.handleApplicationInfoChanged((ApplicationInfo) msg.obj);
                        break;
                    } finally {
                        ActivityThread.this.mUpdatingSystemConfig = false;
                    }
                case 158:
                    ActivityThread.this.handleRunIsolatedEntryPoint((String) ((SomeArgs) msg.obj).arg1, (String[]) ((SomeArgs) msg.obj).arg2);
                    break;
                case 159:
                    ClientTransaction transaction = (ClientTransaction) msg.obj;
                    ActivityThread.this.mTransactionExecutor.execute(transaction);
                    if (ActivityThread.isSystem()) {
                        transaction.recycle();
                        break;
                    }
                    break;
                case 160:
                    ActivityThread.this.handleRelaunchActivityLocally((IBinder) msg.obj);
                    break;
                case 161:
                    ActivityThread.this.schedulePurgeIdler();
                    break;
            }
            Object obj = msg.obj;
            if (obj instanceof SomeArgs) {
                ((SomeArgs) obj).recycle();
            }
        }
    }

    /* loaded from: classes.dex */
    private class Idler implements MessageQueue.IdleHandler {
        private Idler() {
        }

        /* JADX WARN: Removed duplicated region for block: B:33:0x007a  */
        @Override // android.os.MessageQueue.IdleHandler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final boolean queueIdle() {
            /*
                r7 = this;
                android.app.ActivityThread r0 = android.app.ActivityThread.this
                android.app.ActivityThread$ActivityClientRecord r0 = r0.mNewActivities
                r1 = 0
                android.app.ActivityThread r2 = android.app.ActivityThread.this
                android.app.ActivityThread$AppBindData r2 = r2.mBoundApplication
                if (r2 == 0) goto L1c
                android.app.ActivityThread r2 = android.app.ActivityThread.this
                android.app.ActivityThread$Profiler r2 = r2.mProfiler
                android.os.ParcelFileDescriptor r2 = r2.profileFd
                if (r2 == 0) goto L1c
                android.app.ActivityThread r2 = android.app.ActivityThread.this
                android.app.ActivityThread$Profiler r2 = r2.mProfiler
                boolean r2 = r2.autoStopProfiler
                if (r2 == 0) goto L1c
                r1 = 1
            L1c:
                r2 = 0
                if (r0 == 0) goto L78
                android.app.ActivityThread r3 = android.app.ActivityThread.this
                r4 = 0
                r3.mNewActivities = r4
                android.app.IActivityTaskManager r3 = android.app.ActivityTaskManager.getService()
            L28:
                boolean r5 = android.app.ActivityThread.localLOGV
                if (r5 == 0) goto L57
                java.lang.StringBuilder r5 = new java.lang.StringBuilder
                r5.<init>()
                java.lang.String r6 = "Reporting idle of "
                r5.append(r6)
                r5.append(r0)
                java.lang.String r6 = " finished="
                r5.append(r6)
                android.app.Activity r6 = r0.activity
                if (r6 == 0) goto L4a
                android.app.Activity r6 = r0.activity
                boolean r6 = r6.mFinished
                if (r6 == 0) goto L4a
                r6 = 1
                goto L4b
            L4a:
                r6 = r2
            L4b:
                r5.append(r6)
                java.lang.String r5 = r5.toString()
                java.lang.String r6 = "ActivityThread"
                android.util.Slog.v(r6, r5)
            L57:
                android.app.Activity r5 = r0.activity
                if (r5 == 0) goto L71
                android.app.Activity r5 = r0.activity
                boolean r5 = r5.mFinished
                if (r5 != 0) goto L71
                android.os.IBinder r5 = r0.token     // Catch: android.os.RemoteException -> L6b
                android.content.res.Configuration r6 = r0.createdConfig     // Catch: android.os.RemoteException -> L6b
                r3.activityIdle(r5, r6, r1)     // Catch: android.os.RemoteException -> L6b
                r0.createdConfig = r4     // Catch: android.os.RemoteException -> L6b
                goto L71
            L6b:
                r2 = move-exception
                java.lang.RuntimeException r4 = r2.rethrowFromSystemServer()
                throw r4
            L71:
                r5 = r0
                android.app.ActivityThread$ActivityClientRecord r0 = r0.nextIdle
                r5.nextIdle = r4
                if (r0 != 0) goto L28
            L78:
                if (r1 == 0) goto L81
                android.app.ActivityThread r3 = android.app.ActivityThread.this
                android.app.ActivityThread$Profiler r3 = r3.mProfiler
                r3.stopProfiling()
            L81:
                android.app.ActivityThread r3 = android.app.ActivityThread.this
                android.app.ActivityThread.access$3600(r3)
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.Idler.queueIdle():boolean");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class GcIdler implements MessageQueue.IdleHandler {
        GcIdler() {
        }

        @Override // android.os.MessageQueue.IdleHandler
        public final boolean queueIdle() {
            ActivityThread.this.doGcIfNeeded();
            ActivityThread.this.purgePendingResources();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class PurgeIdler implements MessageQueue.IdleHandler {
        PurgeIdler() {
        }

        @Override // android.os.MessageQueue.IdleHandler
        public boolean queueIdle() {
            ActivityThread.this.purgePendingResources();
            return false;
        }
    }

    @UnsupportedAppUsage
    public static ActivityThread currentActivityThread() {
        return sCurrentActivityThread;
    }

    public static boolean isSystem() {
        if (sCurrentActivityThread != null) {
            return sCurrentActivityThread.mSystemThread;
        }
        return false;
    }

    public static String currentOpPackageName() {
        ActivityThread am = currentActivityThread();
        if (am == null || am.getApplication() == null) {
            return null;
        }
        return am.getApplication().getOpPackageName();
    }

    @UnsupportedAppUsage
    public static String currentPackageName() {
        AppBindData appBindData;
        ActivityThread am = currentActivityThread();
        if (am == null || (appBindData = am.mBoundApplication) == null) {
            return null;
        }
        return appBindData.appInfo.packageName;
    }

    @UnsupportedAppUsage
    public static String currentProcessName() {
        AppBindData appBindData;
        ActivityThread am = currentActivityThread();
        if (am == null || (appBindData = am.mBoundApplication) == null) {
            return null;
        }
        return appBindData.processName;
    }

    @UnsupportedAppUsage
    public static Application currentApplication() {
        ActivityThread am = currentActivityThread();
        if (am != null) {
            return am.mInitialApplication;
        }
        return null;
    }

    @UnsupportedAppUsage
    public static IPackageManager getPackageManager() {
        if (sPackageManager != null) {
            return sPackageManager;
        }
        IBinder b = ServiceManager.getService("package");
        sPackageManager = IPackageManager.Stub.asInterface(b);
        return sPackageManager;
    }

    Configuration applyConfigCompatMainThread(int displayDensity, Configuration config, CompatibilityInfo compat) {
        if (config == null) {
            return null;
        }
        if (!compat.supportsScreen()) {
            this.mMainThreadConfig.setTo(config);
            Configuration config2 = this.mMainThreadConfig;
            compat.applyToConfiguration(displayDensity, config2);
            return config2;
        }
        return config;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Resources getTopLevelResources(String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, LoadedApk pkgInfo) {
        return this.mResourcesManager.getResources(null, resDir, splitResDirs, overlayDirs, libDirs, displayId, null, pkgInfo.getCompatibilityInfo(), pkgInfo.getClassLoader());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public final Handler getHandler() {
        return this.mH;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final LoadedApk getPackageInfo(String packageName, CompatibilityInfo compatInfo, int flags) {
        return getPackageInfo(packageName, compatInfo, flags, UserHandle.myUserId());
    }

    public final LoadedApk getPackageInfo(String packageName, CompatibilityInfo compatInfo, int flags, int userId) {
        WeakReference<LoadedApk> ref;
        boolean differentUser = UserHandle.myUserId() != userId;
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(packageName, 268436480, userId < 0 ? UserHandle.myUserId() : userId);
            synchronized (this.mResourcesManager) {
                if (differentUser) {
                    ref = null;
                } else if ((flags & 1) != 0) {
                    ref = this.mPackages.get(packageName);
                } else {
                    ref = this.mResourcePackages.get(packageName);
                }
                LoadedApk packageInfo = ref != null ? ref.get() : null;
                if (ai != null && packageInfo != null) {
                    if (!isLoadedApkResourceDirsUpToDate(packageInfo, ai)) {
                        packageInfo.updateApplicationInfo(ai, null);
                    }
                    if (packageInfo.isSecurityViolation() && (flags & 2) == 0) {
                        throw new SecurityException("Requesting code from " + packageName + " to be run in process " + this.mBoundApplication.processName + "/" + this.mBoundApplication.appInfo.uid);
                    }
                    return packageInfo;
                } else if (ai == null) {
                    return null;
                } else {
                    return getPackageInfo(ai, compatInfo, flags);
                }
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public final LoadedApk getPackageInfo(ApplicationInfo ai, CompatibilityInfo compatInfo, int flags) {
        boolean includeCode = (flags & 1) != 0;
        boolean securityViolation = includeCode && ai.uid != 0 && ai.uid != 1000 && (this.mBoundApplication == null || !UserHandle.isSameApp(ai.uid, this.mBoundApplication.appInfo.uid));
        boolean registerPackage = includeCode && (1073741824 & flags) != 0;
        if ((flags & 3) == 1 && securityViolation) {
            String msg = "Requesting code from " + ai.packageName + " (with uid " + ai.uid + ")";
            if (this.mBoundApplication != null) {
                msg = msg + " to be run in process " + this.mBoundApplication.processName + " (with uid " + this.mBoundApplication.appInfo.uid + ")";
            }
            throw new SecurityException(msg);
        }
        return getPackageInfo(ai, compatInfo, null, securityViolation, includeCode, registerPackage);
    }

    @Override // android.app.ClientTransactionHandler
    @UnsupportedAppUsage
    public final LoadedApk getPackageInfoNoCheck(ApplicationInfo ai, CompatibilityInfo compatInfo) {
        return getPackageInfo(ai, compatInfo, null, false, true, false);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final LoadedApk peekPackageInfo(String packageName, boolean includeCode) {
        WeakReference<LoadedApk> ref;
        LoadedApk loadedApk;
        synchronized (this.mResourcesManager) {
            if (includeCode) {
                ref = this.mPackages.get(packageName);
            } else {
                ref = this.mResourcePackages.get(packageName);
            }
            loadedApk = ref != null ? ref.get() : null;
        }
        return loadedApk;
    }

    private LoadedApk getPackageInfo(ApplicationInfo aInfo, CompatibilityInfo compatInfo, ClassLoader baseLoader, boolean securityViolation, boolean includeCode, boolean registerPackage) {
        WeakReference<LoadedApk> ref;
        boolean differentUser = UserHandle.myUserId() != UserHandle.getUserId(aInfo.uid);
        synchronized (this.mResourcesManager) {
            if (differentUser) {
                ref = null;
            } else if (includeCode) {
                ref = this.mPackages.get(aInfo.packageName);
            } else {
                ref = this.mResourcePackages.get(aInfo.packageName);
            }
            String str = null;
            LoadedApk packageInfo = ref != null ? ref.get() : null;
            if (packageInfo != null) {
                if (!isLoadedApkResourceDirsUpToDate(packageInfo, aInfo)) {
                    packageInfo.updateApplicationInfo(aInfo, null);
                }
                return packageInfo;
            }
            if (localLOGV) {
                StringBuilder sb = new StringBuilder();
                sb.append(includeCode ? "Loading code package " : "Loading resource-only package ");
                sb.append(aInfo.packageName);
                sb.append(" (in ");
                if (this.mBoundApplication != null) {
                    str = this.mBoundApplication.processName;
                }
                sb.append(str);
                sb.append(")");
                Slog.v(TAG, sb.toString());
            }
            LoadedApk packageInfo2 = new LoadedApk(this, aInfo, compatInfo, baseLoader, securityViolation, includeCode && (aInfo.flags & 4) != 0, registerPackage);
            if (this.mSystemThread && "android".equals(aInfo.packageName)) {
                packageInfo2.installSystemApplicationInfo(aInfo, getSystemContext().mPackageInfo.getClassLoader());
            }
            if (!differentUser) {
                if (includeCode) {
                    this.mPackages.put(aInfo.packageName, new WeakReference<>(packageInfo2));
                } else {
                    this.mResourcePackages.put(aInfo.packageName, new WeakReference<>(packageInfo2));
                }
            }
            return packageInfo2;
        }
    }

    private static boolean isLoadedApkResourceDirsUpToDate(LoadedApk loadedApk, ApplicationInfo appInfo) {
        Resources packageResources = loadedApk.mResources;
        String[] overlayDirs = ArrayUtils.defeatNullable(loadedApk.getOverlayDirs());
        String[] resourceDirs = ArrayUtils.defeatNullable(appInfo.resourceDirs);
        return (packageResources == null || packageResources.getAssets().isUpToDate()) && overlayDirs.length == resourceDirs.length && ArrayUtils.containsAll(overlayDirs, resourceDirs);
    }

    @UnsupportedAppUsage
    ActivityThread() {
    }

    @UnsupportedAppUsage
    public ApplicationThread getApplicationThread() {
        return this.mAppThread;
    }

    @UnsupportedAppUsage
    public Instrumentation getInstrumentation() {
        return this.mInstrumentation;
    }

    public boolean isProfiling() {
        Profiler profiler = this.mProfiler;
        return (profiler == null || profiler.profileFile == null || this.mProfiler.profileFd != null) ? false : true;
    }

    public String getProfileFilePath() {
        return this.mProfiler.profileFile;
    }

    @UnsupportedAppUsage
    public Looper getLooper() {
        return this.mLooper;
    }

    public Executor getExecutor() {
        return this.mExecutor;
    }

    @UnsupportedAppUsage
    public Application getApplication() {
        return this.mInitialApplication;
    }

    @UnsupportedAppUsage
    public String getProcessName() {
        return this.mBoundApplication.processName;
    }

    @UnsupportedAppUsage
    public ContextImpl getSystemContext() {
        ContextImpl contextImpl;
        synchronized (this) {
            if (this.mSystemContext == null) {
                this.mSystemContext = ContextImpl.createSystemContext(this);
            }
            contextImpl = this.mSystemContext;
        }
        return contextImpl;
    }

    public ContextImpl getSystemUiContext() {
        ContextImpl contextImpl;
        synchronized (this) {
            if (this.mSystemUiContext == null) {
                this.mSystemUiContext = ContextImpl.createSystemUiContext(getSystemContext());
            }
            contextImpl = this.mSystemUiContext;
        }
        return contextImpl;
    }

    public ContextImpl createSystemUiContext(int displayId) {
        return ContextImpl.createSystemUiContext(getSystemUiContext(), displayId);
    }

    public void installSystemApplicationInfo(ApplicationInfo info, ClassLoader classLoader) {
        synchronized (this) {
            getSystemContext().installSystemApplicationInfo(info, classLoader);
            getSystemUiContext().installSystemApplicationInfo(info, classLoader);
            this.mProfiler = new Profiler();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public void scheduleGcIdler() {
        if (!this.mGcIdlerScheduled) {
            this.mGcIdlerScheduled = true;
            Looper.myQueue().addIdleHandler(this.mGcIdler);
        }
        this.mH.removeMessages(120);
    }

    void unscheduleGcIdler() {
        if (this.mGcIdlerScheduled) {
            this.mGcIdlerScheduled = false;
            Looper.myQueue().removeIdleHandler(this.mGcIdler);
        }
        this.mH.removeMessages(120);
    }

    void schedulePurgeIdler() {
        if (!this.mPurgeIdlerScheduled) {
            this.mPurgeIdlerScheduled = true;
            Looper.myQueue().addIdleHandler(this.mPurgeIdler);
        }
        this.mH.removeMessages(161);
    }

    void unschedulePurgeIdler() {
        if (this.mPurgeIdlerScheduled) {
            this.mPurgeIdlerScheduled = false;
            Looper.myQueue().removeIdleHandler(this.mPurgeIdler);
        }
        this.mH.removeMessages(161);
    }

    void doGcIfNeeded() {
        doGcIfNeeded("bg");
    }

    void doGcIfNeeded(String reason) {
        this.mGcIdlerScheduled = false;
        long now = SystemClock.uptimeMillis();
        if (BinderInternal.getLastGcTime() + 5000 < now) {
            BinderInternal.forceGc(reason);
        }
    }

    static void printRow(PrintWriter pw, String format, Object... objs) {
        pw.println(String.format(format, objs));
    }

    public static void dumpMemInfoTable(PrintWriter pw, Debug.MemoryInfo memInfo, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, int pid, String processName, long nativeMax, long nativeAllocated, long nativeFree, long dalvikMax, long dalvikAllocated, long dalvikFree) {
        String str;
        char c;
        if (checkin) {
            pw.print(4);
            pw.print(',');
            pw.print(pid);
            pw.print(',');
            pw.print(processName);
            pw.print(',');
            pw.print(nativeMax);
            pw.print(',');
            pw.print(dalvikMax);
            pw.print(',');
            pw.print("N/A,");
            pw.print(nativeMax + dalvikMax);
            pw.print(',');
            pw.print(nativeAllocated);
            pw.print(',');
            pw.print(dalvikAllocated);
            pw.print(',');
            pw.print("N/A,");
            pw.print(nativeAllocated + dalvikAllocated);
            pw.print(',');
            pw.print(nativeFree);
            pw.print(',');
            pw.print(dalvikFree);
            pw.print(',');
            pw.print("N/A,");
            pw.print(nativeFree + dalvikFree);
            pw.print(',');
            pw.print(memInfo.nativePss);
            pw.print(',');
            pw.print(memInfo.dalvikPss);
            pw.print(',');
            pw.print(memInfo.otherPss);
            pw.print(',');
            pw.print(memInfo.getTotalPss());
            pw.print(',');
            pw.print(memInfo.nativeSwappablePss);
            pw.print(',');
            pw.print(memInfo.dalvikSwappablePss);
            pw.print(',');
            pw.print(memInfo.otherSwappablePss);
            pw.print(',');
            pw.print(memInfo.getTotalSwappablePss());
            pw.print(',');
            pw.print(memInfo.nativeSharedDirty);
            pw.print(',');
            pw.print(memInfo.dalvikSharedDirty);
            pw.print(',');
            pw.print(memInfo.otherSharedDirty);
            pw.print(',');
            pw.print(memInfo.getTotalSharedDirty());
            pw.print(',');
            pw.print(memInfo.nativeSharedClean);
            pw.print(',');
            pw.print(memInfo.dalvikSharedClean);
            pw.print(',');
            pw.print(memInfo.otherSharedClean);
            pw.print(',');
            pw.print(memInfo.getTotalSharedClean());
            pw.print(',');
            pw.print(memInfo.nativePrivateDirty);
            pw.print(',');
            pw.print(memInfo.dalvikPrivateDirty);
            pw.print(',');
            pw.print(memInfo.otherPrivateDirty);
            pw.print(',');
            pw.print(memInfo.getTotalPrivateDirty());
            pw.print(',');
            pw.print(memInfo.nativePrivateClean);
            pw.print(',');
            pw.print(memInfo.dalvikPrivateClean);
            pw.print(',');
            pw.print(memInfo.otherPrivateClean);
            pw.print(',');
            pw.print(memInfo.getTotalPrivateClean());
            pw.print(',');
            pw.print(memInfo.nativeSwappedOut);
            pw.print(',');
            pw.print(memInfo.dalvikSwappedOut);
            pw.print(',');
            pw.print(memInfo.otherSwappedOut);
            pw.print(',');
            pw.print(memInfo.getTotalSwappedOut());
            pw.print(',');
            if (memInfo.hasSwappedOutPss) {
                pw.print(memInfo.nativeSwappedOutPss);
                pw.print(',');
                pw.print(memInfo.dalvikSwappedOutPss);
                pw.print(',');
                pw.print(memInfo.otherSwappedOutPss);
                pw.print(',');
                pw.print(memInfo.getTotalSwappedOutPss());
                pw.print(',');
            } else {
                pw.print("N/A,");
                pw.print("N/A,");
                pw.print("N/A,");
                pw.print("N/A,");
            }
            for (int i = 0; i < 17; i++) {
                pw.print(Debug.MemoryInfo.getOtherLabel(i));
                pw.print(',');
                pw.print(memInfo.getOtherPss(i));
                pw.print(',');
                pw.print(memInfo.getOtherSwappablePss(i));
                pw.print(',');
                pw.print(memInfo.getOtherSharedDirty(i));
                pw.print(',');
                pw.print(memInfo.getOtherSharedClean(i));
                pw.print(',');
                pw.print(memInfo.getOtherPrivateDirty(i));
                pw.print(',');
                pw.print(memInfo.getOtherPrivateClean(i));
                pw.print(',');
                pw.print(memInfo.getOtherSwappedOut(i));
                pw.print(',');
                if (memInfo.hasSwappedOutPss) {
                    pw.print(memInfo.getOtherSwappedOutPss(i));
                    pw.print(',');
                } else {
                    pw.print("N/A,");
                }
            }
            return;
        }
        String str2 = "------";
        if (dumpSummaryOnly) {
            str = "------";
        } else {
            if (dumpFullInfo) {
                Object[] objArr = new Object[11];
                objArr[0] = "";
                objArr[1] = "Pss";
                objArr[2] = "Pss";
                objArr[3] = "Shared";
                objArr[4] = "Private";
                objArr[5] = "Shared";
                objArr[6] = "Private";
                objArr[7] = memInfo.hasSwappedOutPss ? "SwapPss" : "Swap";
                objArr[8] = "Heap";
                objArr[9] = "Heap";
                objArr[10] = "Heap";
                printRow(pw, HEAP_FULL_COLUMN, objArr);
                printRow(pw, HEAP_FULL_COLUMN, "", "Total", "Clean", "Dirty", "Dirty", "Clean", "Clean", "Dirty", "Size", "Alloc", "Free");
                printRow(pw, HEAP_FULL_COLUMN, "", "------", "------", "------", "------", "------", "------", "------", "------", "------", "------");
                Object[] objArr2 = new Object[11];
                objArr2[0] = "Native Heap";
                objArr2[1] = Integer.valueOf(memInfo.nativePss);
                objArr2[2] = Integer.valueOf(memInfo.nativeSwappablePss);
                objArr2[3] = Integer.valueOf(memInfo.nativeSharedDirty);
                objArr2[4] = Integer.valueOf(memInfo.nativePrivateDirty);
                objArr2[5] = Integer.valueOf(memInfo.nativeSharedClean);
                objArr2[6] = Integer.valueOf(memInfo.nativePrivateClean);
                objArr2[7] = Integer.valueOf(memInfo.hasSwappedOutPss ? memInfo.nativeSwappedOutPss : memInfo.nativeSwappedOut);
                objArr2[8] = Long.valueOf(nativeMax);
                objArr2[9] = Long.valueOf(nativeAllocated);
                objArr2[10] = Long.valueOf(nativeFree);
                printRow(pw, HEAP_FULL_COLUMN, objArr2);
                Object[] objArr3 = new Object[11];
                objArr3[0] = "Dalvik Heap";
                objArr3[1] = Integer.valueOf(memInfo.dalvikPss);
                objArr3[2] = Integer.valueOf(memInfo.dalvikSwappablePss);
                objArr3[3] = Integer.valueOf(memInfo.dalvikSharedDirty);
                objArr3[4] = Integer.valueOf(memInfo.dalvikPrivateDirty);
                objArr3[5] = Integer.valueOf(memInfo.dalvikSharedClean);
                objArr3[6] = Integer.valueOf(memInfo.dalvikPrivateClean);
                objArr3[7] = Integer.valueOf(memInfo.hasSwappedOutPss ? memInfo.dalvikSwappedOutPss : memInfo.dalvikSwappedOut);
                objArr3[8] = Long.valueOf(dalvikMax);
                objArr3[9] = Long.valueOf(dalvikAllocated);
                objArr3[10] = Long.valueOf(dalvikFree);
                printRow(pw, HEAP_FULL_COLUMN, objArr3);
            } else {
                Object[] objArr4 = new Object[8];
                objArr4[0] = "";
                objArr4[1] = "Pss";
                objArr4[2] = "Private";
                objArr4[3] = "Private";
                objArr4[4] = memInfo.hasSwappedOutPss ? "SwapPss" : "Swap";
                objArr4[5] = "Heap";
                objArr4[6] = "Heap";
                objArr4[7] = "Heap";
                printRow(pw, HEAP_COLUMN, objArr4);
                printRow(pw, HEAP_COLUMN, "", "Total", "Dirty", "Clean", "Dirty", "Size", "Alloc", "Free");
                printRow(pw, HEAP_COLUMN, "", "------", "------", "------", "------", "------", "------", "------", "------");
                Object[] objArr5 = new Object[8];
                objArr5[0] = "Native Heap";
                objArr5[1] = Integer.valueOf(memInfo.nativePss);
                objArr5[2] = Integer.valueOf(memInfo.nativePrivateDirty);
                objArr5[3] = Integer.valueOf(memInfo.nativePrivateClean);
                objArr5[4] = Integer.valueOf(memInfo.hasSwappedOutPss ? memInfo.nativeSwappedOutPss : memInfo.nativeSwappedOut);
                objArr5[5] = Long.valueOf(nativeMax);
                objArr5[6] = Long.valueOf(nativeAllocated);
                objArr5[7] = Long.valueOf(nativeFree);
                printRow(pw, HEAP_COLUMN, objArr5);
                Object[] objArr6 = new Object[8];
                objArr6[0] = "Dalvik Heap";
                objArr6[1] = Integer.valueOf(memInfo.dalvikPss);
                objArr6[2] = Integer.valueOf(memInfo.dalvikPrivateDirty);
                objArr6[3] = Integer.valueOf(memInfo.dalvikPrivateClean);
                objArr6[4] = Integer.valueOf(memInfo.hasSwappedOutPss ? memInfo.dalvikSwappedOutPss : memInfo.dalvikSwappedOut);
                objArr6[5] = Long.valueOf(dalvikMax);
                objArr6[6] = Long.valueOf(dalvikAllocated);
                objArr6[7] = Long.valueOf(dalvikFree);
                printRow(pw, HEAP_COLUMN, objArr6);
            }
            int otherPss = memInfo.otherPss;
            int otherSwappablePss = memInfo.otherSwappablePss;
            int otherSharedDirty = memInfo.otherSharedDirty;
            int otherPrivateDirty = memInfo.otherPrivateDirty;
            int otherPss2 = otherPss;
            int otherPss3 = memInfo.otherSharedClean;
            int otherSharedClean = otherPss3;
            int otherSharedClean2 = memInfo.otherPrivateClean;
            int otherPrivateClean = otherSharedClean2;
            int otherPrivateClean2 = memInfo.otherSwappedOut;
            int otherSwappedOut = otherPrivateClean2;
            int otherSwappedOut2 = memInfo.otherSwappedOutPss;
            int otherSwappedOutPss = otherSwappedOut2;
            int otherSwappedOutPss2 = 0;
            while (true) {
                str = str2;
                if (otherSwappedOutPss2 >= 17) {
                    break;
                }
                int myPss = memInfo.getOtherPss(otherSwappedOutPss2);
                int mySwappablePss = memInfo.getOtherSwappablePss(otherSwappedOutPss2);
                int mySharedDirty = memInfo.getOtherSharedDirty(otherSwappedOutPss2);
                int myPrivateDirty = memInfo.getOtherPrivateDirty(otherSwappedOutPss2);
                int mySharedClean = memInfo.getOtherSharedClean(otherSwappedOutPss2);
                int myPrivateClean = memInfo.getOtherPrivateClean(otherSwappedOutPss2);
                int mySwappedOut = memInfo.getOtherSwappedOut(otherSwappedOutPss2);
                int mySwappedOutPss = memInfo.getOtherSwappedOutPss(otherSwappedOutPss2);
                if (myPss == 0 && mySharedDirty == 0 && myPrivateDirty == 0 && mySharedClean == 0 && myPrivateClean == 0) {
                    if ((memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut) == 0) {
                        otherSwappedOutPss2++;
                        str2 = str;
                    }
                }
                if (dumpFullInfo) {
                    Object[] objArr7 = new Object[11];
                    objArr7[0] = Debug.MemoryInfo.getOtherLabel(otherSwappedOutPss2);
                    objArr7[1] = Integer.valueOf(myPss);
                    objArr7[2] = Integer.valueOf(mySwappablePss);
                    objArr7[3] = Integer.valueOf(mySharedDirty);
                    objArr7[4] = Integer.valueOf(myPrivateDirty);
                    objArr7[5] = Integer.valueOf(mySharedClean);
                    objArr7[6] = Integer.valueOf(myPrivateClean);
                    objArr7[7] = Integer.valueOf(memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut);
                    objArr7[8] = "";
                    objArr7[9] = "";
                    objArr7[10] = "";
                    printRow(pw, HEAP_FULL_COLUMN, objArr7);
                } else {
                    Object[] objArr8 = new Object[8];
                    objArr8[0] = Debug.MemoryInfo.getOtherLabel(otherSwappedOutPss2);
                    objArr8[1] = Integer.valueOf(myPss);
                    objArr8[2] = Integer.valueOf(myPrivateDirty);
                    objArr8[3] = Integer.valueOf(myPrivateClean);
                    objArr8[4] = Integer.valueOf(memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut);
                    objArr8[5] = "";
                    objArr8[6] = "";
                    objArr8[7] = "";
                    printRow(pw, HEAP_COLUMN, objArr8);
                }
                otherPss2 -= myPss;
                otherSwappablePss -= mySwappablePss;
                otherSharedDirty -= mySharedDirty;
                otherPrivateDirty -= myPrivateDirty;
                otherSharedClean -= mySharedClean;
                otherPrivateClean -= myPrivateClean;
                otherSwappedOut -= mySwappedOut;
                otherSwappedOutPss -= mySwappedOutPss;
                otherSwappedOutPss2++;
                str2 = str;
            }
            if (dumpFullInfo) {
                Object[] objArr9 = new Object[11];
                objArr9[0] = "Unknown";
                objArr9[1] = Integer.valueOf(otherPss2);
                objArr9[2] = Integer.valueOf(otherSwappablePss);
                objArr9[3] = Integer.valueOf(otherSharedDirty);
                objArr9[4] = Integer.valueOf(otherPrivateDirty);
                objArr9[5] = Integer.valueOf(otherSharedClean);
                objArr9[6] = Integer.valueOf(otherPrivateClean);
                objArr9[7] = Integer.valueOf(memInfo.hasSwappedOutPss ? otherSwappedOutPss : otherSwappedOut);
                objArr9[8] = "";
                objArr9[9] = "";
                objArr9[10] = "";
                printRow(pw, HEAP_FULL_COLUMN, objArr9);
                Object[] objArr10 = new Object[11];
                objArr10[0] = "TOTAL";
                objArr10[1] = Integer.valueOf(memInfo.getTotalPss());
                objArr10[2] = Integer.valueOf(memInfo.getTotalSwappablePss());
                objArr10[3] = Integer.valueOf(memInfo.getTotalSharedDirty());
                objArr10[4] = Integer.valueOf(memInfo.getTotalPrivateDirty());
                objArr10[5] = Integer.valueOf(memInfo.getTotalSharedClean());
                objArr10[6] = Integer.valueOf(memInfo.getTotalPrivateClean());
                objArr10[7] = Integer.valueOf(memInfo.hasSwappedOutPss ? memInfo.getTotalSwappedOutPss() : memInfo.getTotalSwappedOut());
                objArr10[8] = Long.valueOf(nativeMax + dalvikMax);
                objArr10[9] = Long.valueOf(nativeAllocated + dalvikAllocated);
                objArr10[10] = Long.valueOf(nativeFree + dalvikFree);
                printRow(pw, HEAP_FULL_COLUMN, objArr10);
            } else {
                Object[] objArr11 = new Object[8];
                objArr11[0] = "Unknown";
                objArr11[1] = Integer.valueOf(otherPss2);
                objArr11[2] = Integer.valueOf(otherPrivateDirty);
                objArr11[3] = Integer.valueOf(otherPrivateClean);
                objArr11[4] = Integer.valueOf(memInfo.hasSwappedOutPss ? otherSwappedOutPss : otherSwappedOut);
                objArr11[5] = "";
                objArr11[6] = "";
                objArr11[7] = "";
                printRow(pw, HEAP_COLUMN, objArr11);
                Object[] objArr12 = new Object[8];
                objArr12[0] = "TOTAL";
                objArr12[1] = Integer.valueOf(memInfo.getTotalPss());
                objArr12[2] = Integer.valueOf(memInfo.getTotalPrivateDirty());
                objArr12[3] = Integer.valueOf(memInfo.getTotalPrivateClean());
                objArr12[4] = Integer.valueOf(memInfo.hasSwappedOutPss ? memInfo.getTotalSwappedOutPss() : memInfo.getTotalSwappedOut());
                objArr12[5] = Long.valueOf(nativeMax + dalvikMax);
                objArr12[6] = Long.valueOf(nativeAllocated + dalvikAllocated);
                objArr12[7] = Long.valueOf(nativeFree + dalvikFree);
                printRow(pw, HEAP_COLUMN, objArr12);
            }
            if (dumpDalvik) {
                pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                pw.println(" Dalvik Details");
                for (int i2 = 17; i2 < 31; i2++) {
                    int myPss2 = memInfo.getOtherPss(i2);
                    int mySwappablePss2 = memInfo.getOtherSwappablePss(i2);
                    int mySharedDirty2 = memInfo.getOtherSharedDirty(i2);
                    int myPrivateDirty2 = memInfo.getOtherPrivateDirty(i2);
                    int mySharedClean2 = memInfo.getOtherSharedClean(i2);
                    int myPrivateClean2 = memInfo.getOtherPrivateClean(i2);
                    int mySwappedOut2 = memInfo.getOtherSwappedOut(i2);
                    int mySwappedOutPss2 = memInfo.getOtherSwappedOutPss(i2);
                    if (myPss2 == 0 && mySharedDirty2 == 0 && myPrivateDirty2 == 0 && mySharedClean2 == 0 && myPrivateClean2 == 0) {
                        if ((memInfo.hasSwappedOutPss ? mySwappedOutPss2 : mySwappedOut2) == 0) {
                            c = '\t';
                        }
                    }
                    if (dumpFullInfo) {
                        Object[] objArr13 = new Object[11];
                        objArr13[0] = Debug.MemoryInfo.getOtherLabel(i2);
                        objArr13[1] = Integer.valueOf(myPss2);
                        objArr13[2] = Integer.valueOf(mySwappablePss2);
                        objArr13[3] = Integer.valueOf(mySharedDirty2);
                        objArr13[4] = Integer.valueOf(myPrivateDirty2);
                        objArr13[5] = Integer.valueOf(mySharedClean2);
                        objArr13[6] = Integer.valueOf(myPrivateClean2);
                        objArr13[7] = Integer.valueOf(memInfo.hasSwappedOutPss ? mySwappedOutPss2 : mySwappedOut2);
                        objArr13[8] = "";
                        c = '\t';
                        objArr13[9] = "";
                        objArr13[10] = "";
                        printRow(pw, HEAP_FULL_COLUMN, objArr13);
                    } else {
                        c = '\t';
                        Object[] objArr14 = new Object[8];
                        objArr14[0] = Debug.MemoryInfo.getOtherLabel(i2);
                        objArr14[1] = Integer.valueOf(myPss2);
                        objArr14[2] = Integer.valueOf(myPrivateDirty2);
                        objArr14[3] = Integer.valueOf(myPrivateClean2);
                        objArr14[4] = Integer.valueOf(memInfo.hasSwappedOutPss ? mySwappedOutPss2 : mySwappedOut2);
                        objArr14[5] = "";
                        objArr14[6] = "";
                        objArr14[7] = "";
                        printRow(pw, HEAP_COLUMN, objArr14);
                    }
                }
            }
        }
        pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        pw.println(" App Summary");
        printRow(pw, ONE_COUNT_COLUMN_HEADER, "", "Pss(KB)");
        printRow(pw, ONE_COUNT_COLUMN_HEADER, "", str);
        printRow(pw, ONE_COUNT_COLUMN, "Java Heap:", Integer.valueOf(memInfo.getSummaryJavaHeap()));
        printRow(pw, ONE_COUNT_COLUMN, "Native Heap:", Integer.valueOf(memInfo.getSummaryNativeHeap()));
        printRow(pw, ONE_COUNT_COLUMN, "Code:", Integer.valueOf(memInfo.getSummaryCode()));
        printRow(pw, ONE_COUNT_COLUMN, "Stack:", Integer.valueOf(memInfo.getSummaryStack()));
        printRow(pw, ONE_COUNT_COLUMN, "Graphics:", Integer.valueOf(memInfo.getSummaryGraphics()));
        printRow(pw, ONE_COUNT_COLUMN, "Private Other:", Integer.valueOf(memInfo.getSummaryPrivateOther()));
        printRow(pw, ONE_COUNT_COLUMN, "System:", Integer.valueOf(memInfo.getSummarySystem()));
        pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        if (memInfo.hasSwappedOutPss) {
            printRow(pw, TWO_COUNT_COLUMNS, "TOTAL:", Integer.valueOf(memInfo.getSummaryTotalPss()), "TOTAL SWAP PSS:", Integer.valueOf(memInfo.getSummaryTotalSwapPss()));
        } else {
            printRow(pw, TWO_COUNT_COLUMNS, "TOTAL:", Integer.valueOf(memInfo.getSummaryTotalPss()), "TOTAL SWAP (KB):", Integer.valueOf(memInfo.getSummaryTotalSwap()));
        }
    }

    private static void dumpMemoryInfo(ProtoOutputStream proto, long fieldId, String name, int pss, int cleanPss, int sharedDirty, int privateDirty, int sharedClean, int privateClean, boolean hasSwappedOutPss, int dirtySwap, int dirtySwapPss) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, name);
        proto.write(1120986464258L, pss);
        proto.write(1120986464259L, cleanPss);
        proto.write(1120986464260L, sharedDirty);
        proto.write(1120986464261L, privateDirty);
        proto.write(1120986464262L, sharedClean);
        proto.write(1120986464263L, privateClean);
        if (hasSwappedOutPss) {
            proto.write(1120986464265L, dirtySwapPss);
        } else {
            proto.write(1120986464264L, dirtySwap);
        }
        proto.end(token);
    }

    public static void dumpMemInfoTable(ProtoOutputStream proto, Debug.MemoryInfo memInfo, boolean dumpDalvik, boolean dumpSummaryOnly, long nativeMax, long nativeAllocated, long nativeFree, long dalvikMax, long dalvikAllocated, long dalvikFree) {
        ProtoOutputStream protoOutputStream;
        Debug.MemoryInfo memoryInfo;
        long tToken;
        long dvToken;
        int i;
        if (!dumpSummaryOnly) {
            long nhToken = proto.start(1146756268035L);
            dumpMemoryInfo(proto, 1146756268033L, "Native Heap", memInfo.nativePss, memInfo.nativeSwappablePss, memInfo.nativeSharedDirty, memInfo.nativePrivateDirty, memInfo.nativeSharedClean, memInfo.nativePrivateClean, memInfo.hasSwappedOutPss, memInfo.nativeSwappedOut, memInfo.nativeSwappedOutPss);
            protoOutputStream = proto;
            protoOutputStream.write(1120986464258L, nativeMax);
            protoOutputStream.write(1120986464259L, nativeAllocated);
            protoOutputStream.write(1120986464260L, nativeFree);
            protoOutputStream.end(nhToken);
            long dvToken2 = protoOutputStream.start(1146756268036L);
            dumpMemoryInfo(proto, 1146756268033L, "Dalvik Heap", memInfo.dalvikPss, memInfo.dalvikSwappablePss, memInfo.dalvikSharedDirty, memInfo.dalvikPrivateDirty, memInfo.dalvikSharedClean, memInfo.dalvikPrivateClean, memInfo.hasSwappedOutPss, memInfo.dalvikSwappedOut, memInfo.dalvikSwappedOutPss);
            protoOutputStream.write(1120986464258L, dalvikMax);
            protoOutputStream.write(1120986464259L, dalvikAllocated);
            protoOutputStream.write(1120986464260L, dalvikFree);
            protoOutputStream.end(dvToken2);
            Debug.MemoryInfo memoryInfo2 = memInfo;
            int otherPss = memoryInfo2.otherPss;
            int otherSwappablePss = memoryInfo2.otherSwappablePss;
            int otherSharedDirty = memoryInfo2.otherSharedDirty;
            int otherPrivateDirty = memoryInfo2.otherPrivateDirty;
            int otherSharedClean = memoryInfo2.otherSharedClean;
            int otherPrivateClean = memoryInfo2.otherPrivateClean;
            int otherSwappedOut = memoryInfo2.otherSwappedOut;
            int otherSwappedOutPss = memoryInfo2.otherSwappedOutPss;
            int otherSwappedOut2 = otherSwappedOut;
            int otherSwappedOutPss2 = otherSwappedOutPss;
            int otherSwappablePss2 = otherSwappablePss;
            int otherSharedDirty2 = otherSharedDirty;
            int otherPrivateDirty2 = otherPrivateDirty;
            int otherSharedClean2 = otherSharedClean;
            int otherPrivateClean2 = otherPrivateClean;
            int otherPrivateClean3 = 0;
            int otherPss2 = otherPss;
            while (otherPrivateClean3 < 17) {
                int myPss = memoryInfo2.getOtherPss(otherPrivateClean3);
                int mySwappablePss = memoryInfo2.getOtherSwappablePss(otherPrivateClean3);
                int mySharedDirty = memoryInfo2.getOtherSharedDirty(otherPrivateClean3);
                int myPrivateDirty = memoryInfo2.getOtherPrivateDirty(otherPrivateClean3);
                int mySharedClean = memoryInfo2.getOtherSharedClean(otherPrivateClean3);
                int myPrivateClean = memoryInfo2.getOtherPrivateClean(otherPrivateClean3);
                int mySwappedOut = memoryInfo2.getOtherSwappedOut(otherPrivateClean3);
                int mySwappedOutPss = memoryInfo2.getOtherSwappedOutPss(otherPrivateClean3);
                if (myPss == 0 && mySharedDirty == 0 && myPrivateDirty == 0 && mySharedClean == 0 && myPrivateClean == 0) {
                    if ((memoryInfo2.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut) == 0) {
                        i = otherPrivateClean3;
                        dvToken = dvToken2;
                        otherPrivateClean3 = i + 1;
                        memoryInfo2 = memInfo;
                        dvToken2 = dvToken;
                    }
                }
                dvToken = dvToken2;
                i = otherPrivateClean3;
                dumpMemoryInfo(proto, 2246267895813L, Debug.MemoryInfo.getOtherLabel(otherPrivateClean3), myPss, mySwappablePss, mySharedDirty, myPrivateDirty, mySharedClean, myPrivateClean, memoryInfo2.hasSwappedOutPss, mySwappedOut, mySwappedOutPss);
                otherPss2 -= myPss;
                otherSwappablePss2 -= mySwappablePss;
                otherSharedDirty2 -= mySharedDirty;
                otherPrivateDirty2 -= myPrivateDirty;
                otherSharedClean2 -= mySharedClean;
                otherPrivateClean2 -= myPrivateClean;
                otherSwappedOut2 -= mySwappedOut;
                otherSwappedOutPss2 -= mySwappedOutPss;
                otherPrivateClean3 = i + 1;
                memoryInfo2 = memInfo;
                dvToken2 = dvToken;
            }
            memoryInfo = memInfo;
            dumpMemoryInfo(proto, 1146756268038L, "Unknown", otherPss2, otherSwappablePss2, otherSharedDirty2, otherPrivateDirty2, otherSharedClean2, otherPrivateClean2, memInfo.hasSwappedOutPss, otherSwappedOut2, otherSwappedOutPss2);
            long tToken2 = protoOutputStream.start(1146756268039L);
            dumpMemoryInfo(proto, 1146756268033L, "TOTAL", memInfo.getTotalPss(), memInfo.getTotalSwappablePss(), memInfo.getTotalSharedDirty(), memInfo.getTotalPrivateDirty(), memInfo.getTotalSharedClean(), memInfo.getTotalPrivateClean(), memoryInfo.hasSwappedOutPss, memInfo.getTotalSwappedOut(), memInfo.getTotalSwappedOutPss());
            protoOutputStream.write(1120986464258L, nativeMax + dalvikMax);
            protoOutputStream.write(1120986464259L, nativeAllocated + dalvikAllocated);
            protoOutputStream.write(1120986464260L, nativeFree + dalvikFree);
            long tToken3 = tToken2;
            protoOutputStream.end(tToken3);
            if (dumpDalvik) {
                int i2 = 17;
                while (i2 < 31) {
                    int myPss2 = memoryInfo.getOtherPss(i2);
                    int mySwappablePss2 = memoryInfo.getOtherSwappablePss(i2);
                    int mySharedDirty2 = memoryInfo.getOtherSharedDirty(i2);
                    int myPrivateDirty2 = memoryInfo.getOtherPrivateDirty(i2);
                    int mySharedClean2 = memoryInfo.getOtherSharedClean(i2);
                    int myPrivateClean2 = memoryInfo.getOtherPrivateClean(i2);
                    int mySwappedOut2 = memoryInfo.getOtherSwappedOut(i2);
                    int mySwappedOutPss2 = memoryInfo.getOtherSwappedOutPss(i2);
                    if (myPss2 == 0 && mySharedDirty2 == 0 && myPrivateDirty2 == 0 && mySharedClean2 == 0 && myPrivateClean2 == 0) {
                        if ((memoryInfo.hasSwappedOutPss ? mySwappedOutPss2 : mySwappedOut2) == 0) {
                            tToken = tToken3;
                            i2++;
                            tToken3 = tToken;
                        }
                    }
                    tToken = tToken3;
                    dumpMemoryInfo(proto, 2246267895816L, Debug.MemoryInfo.getOtherLabel(i2), myPss2, mySwappablePss2, mySharedDirty2, myPrivateDirty2, mySharedClean2, myPrivateClean2, memoryInfo.hasSwappedOutPss, mySwappedOut2, mySwappedOutPss2);
                    i2++;
                    tToken3 = tToken;
                }
            }
        } else {
            protoOutputStream = proto;
            memoryInfo = memInfo;
        }
        long asToken = protoOutputStream.start(1146756268041L);
        protoOutputStream.write(1120986464257L, memInfo.getSummaryJavaHeap());
        protoOutputStream.write(1120986464258L, memInfo.getSummaryNativeHeap());
        protoOutputStream.write(1120986464259L, memInfo.getSummaryCode());
        protoOutputStream.write(1120986464260L, memInfo.getSummaryStack());
        protoOutputStream.write(1120986464261L, memInfo.getSummaryGraphics());
        protoOutputStream.write(1120986464262L, memInfo.getSummaryPrivateOther());
        protoOutputStream.write(1120986464263L, memInfo.getSummarySystem());
        if (memoryInfo.hasSwappedOutPss) {
            protoOutputStream.write(1120986464264L, memInfo.getSummaryTotalSwapPss());
        } else {
            protoOutputStream.write(1120986464264L, memInfo.getSummaryTotalSwap());
        }
        protoOutputStream.end(asToken);
    }

    @UnsupportedAppUsage
    public void registerOnActivityPausedListener(Activity activity, OnActivityPausedListener listener) {
        synchronized (this.mOnPauseListeners) {
            ArrayList<OnActivityPausedListener> list = this.mOnPauseListeners.get(activity);
            if (list == null) {
                list = new ArrayList<>();
                this.mOnPauseListeners.put(activity, list);
            }
            list.add(listener);
        }
    }

    @UnsupportedAppUsage
    public void unregisterOnActivityPausedListener(Activity activity, OnActivityPausedListener listener) {
        synchronized (this.mOnPauseListeners) {
            ArrayList<OnActivityPausedListener> list = this.mOnPauseListeners.get(activity);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    public final ActivityInfo resolveActivityInfo(Intent intent) {
        ActivityInfo aInfo = intent.resolveActivityInfo(this.mInitialApplication.getPackageManager(), 1024);
        if (aInfo == null) {
            Instrumentation.checkStartActivityResult(-92, intent);
        }
        return aInfo;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final Activity startActivityNow(Activity parent, String id, Intent intent, ActivityInfo activityInfo, IBinder token, Bundle state, Activity.NonConfigurationInstances lastNonConfigurationInstances, IBinder assistToken) {
        String name;
        ActivityClientRecord r = new ActivityClientRecord();
        r.token = token;
        r.assistToken = assistToken;
        r.ident = 0;
        r.intent = intent;
        r.state = state;
        r.parent = parent;
        r.embeddedID = id;
        r.activityInfo = activityInfo;
        r.lastNonConfigurationInstances = lastNonConfigurationInstances;
        if (localLOGV) {
            ComponentName compname = intent.getComponent();
            if (compname != null) {
                name = compname.toShortString();
            } else {
                name = "(Intent " + intent + ").getComponent() returned null";
            }
            Slog.v(TAG, "Performing launch: action=" + intent.getAction() + ", comp=" + name + ", token=" + token);
        }
        return performLaunchActivity(r, null);
    }

    @Override // android.app.ClientTransactionHandler
    @UnsupportedAppUsage
    public final Activity getActivity(IBinder token) {
        ActivityClientRecord activityRecord = this.mActivities.get(token);
        if (activityRecord != null) {
            return activityRecord.activity;
        }
        return null;
    }

    @Override // android.app.ClientTransactionHandler
    public ActivityClientRecord getActivityClient(IBinder token) {
        return this.mActivities.get(token);
    }

    @Override // android.app.ClientTransactionHandler
    public void updatePendingConfiguration(Configuration config) {
        synchronized (this.mResourcesManager) {
            if (this.mPendingConfiguration == null || this.mPendingConfiguration.isOtherSeqNewer(config)) {
                this.mPendingConfiguration = config;
            }
        }
    }

    @Override // android.app.ClientTransactionHandler
    public void updateProcessState(int processState, boolean fromIpc) {
        synchronized (this.mAppThread) {
            if (this.mLastProcessState == processState) {
                return;
            }
            this.mLastProcessState = processState;
            if (processState == 2 && this.mNumLaunchingActivities.get() > 0) {
                this.mPendingProcessState = processState;
                this.mH.postDelayed(new Runnable() { // from class: android.app.-$$Lambda$ActivityThread$A4ykhsPb8qV3ffTqpQDklHSMDJ0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ActivityThread.this.applyPendingProcessState();
                    }
                }, 1000L);
            } else {
                this.mPendingProcessState = -1;
                updateVmProcessState(processState);
            }
            if (localLOGV) {
                StringBuilder sb = new StringBuilder();
                sb.append("******************* PROCESS STATE CHANGED TO: ");
                sb.append(processState);
                sb.append(fromIpc ? " (from ipc" : "");
                Slog.i(TAG, sb.toString());
            }
        }
    }

    private void updateVmProcessState(int processState) {
        int state;
        if (processState <= 7) {
            state = 0;
        } else {
            state = 1;
        }
        VMRuntime.getRuntime().updateProcessState(state);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyPendingProcessState() {
        synchronized (this.mAppThread) {
            if (this.mPendingProcessState == -1) {
                return;
            }
            int pendingState = this.mPendingProcessState;
            this.mPendingProcessState = -1;
            if (pendingState == this.mLastProcessState) {
                updateVmProcessState(pendingState);
            }
        }
    }

    @Override // android.app.ClientTransactionHandler
    public void countLaunchingActivities(int num) {
        this.mNumLaunchingActivities.getAndAdd(num);
    }

    @UnsupportedAppUsage
    public final void sendActivityResult(IBinder token, String id, int requestCode, int resultCode, Intent data) {
        if (DEBUG_RESULTS) {
            Slog.v(TAG, "sendActivityResult: id=" + id + " req=" + requestCode + " res=" + resultCode + " data=" + data);
        }
        ArrayList<ResultInfo> list = new ArrayList<>();
        list.add(new ResultInfo(id, requestCode, resultCode, data));
        ClientTransaction clientTransaction = ClientTransaction.obtain(this.mAppThread, token);
        clientTransaction.addCallback(ActivityResultItem.obtain(list));
        try {
            this.mAppThread.scheduleTransaction(clientTransaction);
        } catch (RemoteException e) {
        }
    }

    @Override // android.app.ClientTransactionHandler
    TransactionExecutor getTransactionExecutor() {
        return this.mTransactionExecutor;
    }

    @Override // android.app.ClientTransactionHandler
    void sendMessage(int what, Object obj) {
        sendMessage(what, obj, 0, 0, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(int what, Object obj, int arg1) {
        sendMessage(what, obj, arg1, 0, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(int what, Object obj, int arg1, int arg2) {
        sendMessage(what, obj, arg1, arg2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(int what, Object obj, int arg1, int arg2, boolean async) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        if (async) {
            msg.setAsynchronous(true);
        }
        this.mH.sendMessage(msg);
    }

    private void sendMessage(int what, Object obj, int arg1, int arg2, int seq) {
        Message msg = Message.obtain();
        msg.what = what;
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = obj;
        args.argi1 = arg1;
        args.argi2 = arg2;
        args.argi3 = seq;
        msg.obj = args;
        this.mH.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void scheduleContextCleanup(ContextImpl context, String who, String what) {
        ContextCleanupInfo cci = new ContextCleanupInfo();
        cci.context = context;
        cci.who = who;
        cci.what = what;
        sendMessage(119, cci);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:124:0x02e9  */
    /* JADX WARN: Type inference failed for: r4v13 */
    /* JADX WARN: Type inference failed for: r4v14 */
    /* JADX WARN: Type inference failed for: r4v17 */
    /* JADX WARN: Type inference failed for: r4v20 */
    /* JADX WARN: Type inference failed for: r4v27 */
    /* JADX WARN: Type inference failed for: r4v29, types: [android.content.Intent] */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private android.app.Activity performLaunchActivity(android.app.ActivityThread.ActivityClientRecord r31, android.content.Intent r32) {
        /*
            Method dump skipped, instructions count: 831
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.performLaunchActivity(android.app.ActivityThread$ActivityClientRecord, android.content.Intent):android.app.Activity");
    }

    @Override // android.app.ClientTransactionHandler
    public void handleStartActivity(ActivityClientRecord r, PendingTransactionActions pendingActions) {
        Activity activity = r.activity;
        if (r.activity == null) {
            return;
        }
        if (!r.stopped) {
            throw new IllegalStateException("Can't start activity that is not stopped.");
        }
        if (r.activity.mFinished) {
            return;
        }
        activity.performStart("handleStartActivity");
        r.setState(2);
        if (pendingActions == null) {
            return;
        }
        if (pendingActions.shouldRestoreInstanceState()) {
            if (r.isPersistable()) {
                if (r.state != null || r.persistentState != null) {
                    this.mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state, r.persistentState);
                }
            } else if (r.state != null) {
                this.mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state);
            }
        }
        if (pendingActions.shouldCallOnPostCreate()) {
            activity.mCalled = false;
            if (r.isPersistable()) {
                this.mInstrumentation.callActivityOnPostCreate(activity, r.state, r.persistentState);
            } else {
                this.mInstrumentation.callActivityOnPostCreate(activity, r.state);
            }
            if (!activity.mCalled) {
                throw new SuperNotCalledException("Activity " + r.intent.getComponent().toShortString() + " did not call through to super.onPostCreate()");
            }
        }
    }

    private void checkAndBlockForNetworkAccess() {
        synchronized (this.mNetworkPolicyLock) {
            if (this.mNetworkBlockSeq != -1) {
                try {
                    ActivityManager.getService().waitForNetworkStateUpdate(this.mNetworkBlockSeq);
                    this.mNetworkBlockSeq = -1L;
                } catch (RemoteException e) {
                }
            }
        }
    }

    private ContextImpl createBaseContextForActivity(ActivityClientRecord r) {
        int[] displayIds;
        try {
            int displayId = ActivityTaskManager.getService().getActivityDisplayId(r.token);
            ContextImpl appContext = ContextImpl.createActivityContext(this, r.packageInfo, r.activityInfo, r.token, displayId, r.overrideConfig);
            DisplayManagerGlobal dm = DisplayManagerGlobal.getInstance();
            String pkgName = SystemProperties.get("debug.second-display.pkg");
            if (pkgName != null && !pkgName.isEmpty() && r.packageInfo.mPackageName.contains(pkgName)) {
                for (int id : dm.getDisplayIds()) {
                    if (id != 0) {
                        Display display = dm.getCompatibleDisplay(id, appContext.getResources());
                        return (ContextImpl) appContext.createDisplayContext(display);
                    }
                }
                return appContext;
            }
            return appContext;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.app.ClientTransactionHandler
    public Activity handleLaunchActivity(ActivityClientRecord r, PendingTransactionActions pendingActions, Intent customIntent) {
        unscheduleGcIdler();
        this.mSomeActivitiesChanged = true;
        if (r.profilerInfo != null) {
            this.mProfiler.setProfiler(r.profilerInfo);
            this.mProfiler.startProfiling();
        }
        handleConfigurationChanged(null, null);
        if (localLOGV) {
            Slog.v(TAG, "Handling launch of " + r);
        }
        if (!ThreadedRenderer.sRendererDisabled && (r.activityInfo.flags & 512) != 0) {
            HardwareRenderer.preload();
        }
        WindowManagerGlobal.initialize();
        GraphicsEnvironment.hintActivityLaunch();
        Activity a = performLaunchActivity(r, customIntent);
        if (a != null) {
            r.createdConfig = new Configuration(this.mConfiguration);
            reportSizeConfigurations(r);
            if (!r.activity.mFinished && pendingActions != null) {
                pendingActions.setOldState(r.state);
                pendingActions.setRestoreInstanceState(true);
                pendingActions.setCallOnPostCreate(true);
            }
        } else {
            try {
                Slog.v(TAG, "handleLaunchActivity finishActivity r = " + r);
                ActivityTaskManager.getService().finishActivity(r.token, 0, null, 0);
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
        return a;
    }

    private void reportSizeConfigurations(ActivityClientRecord r) {
        Configuration[] configurations;
        if (this.mActivitiesToBeDestroyed.containsKey(r.token) || (configurations = r.activity.getResources().getSizeConfigurations()) == null) {
            return;
        }
        SparseIntArray horizontal = new SparseIntArray();
        SparseIntArray vertical = new SparseIntArray();
        SparseIntArray smallest = new SparseIntArray();
        for (int i = configurations.length - 1; i >= 0; i--) {
            Configuration config = configurations[i];
            if (config.screenHeightDp != 0) {
                vertical.put(config.screenHeightDp, 0);
            }
            if (config.screenWidthDp != 0) {
                horizontal.put(config.screenWidthDp, 0);
            }
            if (config.smallestScreenWidthDp != 0) {
                smallest.put(config.smallestScreenWidthDp, 0);
            }
        }
        try {
            if (r.token == null) {
                return;
            }
            ActivityTaskManager.getService().reportSizeConfigurations(r.token, horizontal.copyKeys(), vertical.copyKeys(), smallest.copyKeys());
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
    }

    private void deliverNewIntents(ActivityClientRecord r, List<ReferrerIntent> intents) {
        int N = intents.size();
        for (int i = 0; i < N; i++) {
            ReferrerIntent intent = intents.get(i);
            intent.setExtrasClassLoader(r.activity.getClassLoader());
            intent.prepareToEnterProcess();
            r.activity.mFragments.noteStateNotSaved();
            this.mInstrumentation.callActivityOnNewIntent(r.activity, intent);
        }
    }

    @Override // android.app.ClientTransactionHandler
    public void handleNewIntent(IBinder token, List<ReferrerIntent> intents) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r == null) {
            return;
        }
        checkAndBlockForNetworkAccess();
        deliverNewIntents(r, intents);
    }

    public void handleRequestAssistContextExtras(RequestAssistContextExtras cmd) {
        Uri referrer;
        AssistStructure structure;
        boolean notSecure = false;
        boolean forAutofill = cmd.requestType == 2;
        if (this.mLastSessionId != cmd.sessionId) {
            this.mLastSessionId = cmd.sessionId;
            for (int i = this.mLastAssistStructures.size() - 1; i >= 0; i--) {
                AssistStructure structure2 = this.mLastAssistStructures.get(i).get();
                if (structure2 != null) {
                    structure2.clearSendChannel();
                }
                this.mLastAssistStructures.remove(i);
            }
        }
        Bundle data = new Bundle();
        AssistStructure structure3 = null;
        AssistContent content = forAutofill ? null : new AssistContent();
        long startTime = SystemClock.uptimeMillis();
        ActivityClientRecord r = this.mActivities.get(cmd.activityToken);
        Uri referrer2 = null;
        if (r == null) {
            referrer = null;
        } else {
            if (!forAutofill) {
                r.activity.getApplication().dispatchOnProvideAssistData(r.activity, data);
                r.activity.onProvideAssistData(data);
                referrer2 = r.activity.onProvideReferrer();
            }
            if (cmd.requestType == 1 || forAutofill) {
                structure3 = new AssistStructure(r.activity, forAutofill, cmd.flags);
                Intent activityIntent = r.activity.getIntent();
                notSecure = (r.window == null || (r.window.getAttributes().flags & 8192) == 0) ? true : true;
                if (activityIntent != null && notSecure) {
                    if (!forAutofill) {
                        Intent intent = new Intent(activityIntent);
                        intent.setFlags(intent.getFlags() & (-67));
                        intent.removeUnsafeExtras();
                        content.setDefaultIntent(intent);
                    }
                } else if (!forAutofill) {
                    content.setDefaultIntent(new Intent());
                }
                if (!forAutofill) {
                    r.activity.onProvideAssistContent(content);
                }
                referrer = referrer2;
            } else {
                referrer = referrer2;
            }
        }
        if (structure3 != null) {
            structure = structure3;
        } else {
            structure = new AssistStructure();
        }
        structure.setAcquisitionStartTime(startTime);
        structure.setAcquisitionEndTime(SystemClock.uptimeMillis());
        this.mLastAssistStructures.add(new WeakReference<>(structure));
        IActivityTaskManager mgr = ActivityTaskManager.getService();
        try {
        } catch (RemoteException e) {
            e = e;
        }
        try {
            mgr.reportAssistContextExtras(cmd.requestToken, data, structure, content, referrer);
        } catch (RemoteException e2) {
            e = e2;
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRequestDirectActions(IBinder activityToken, IVoiceInteractor interactor, CancellationSignal cancellationSignal, final RemoteCallback callback) {
        final ActivityClientRecord r = this.mActivities.get(activityToken);
        if (r == null) {
            Log.w(TAG, "requestDirectActions(): no activity for " + activityToken);
            callback.sendResult(null);
            return;
        }
        int lifecycleState = r.getLifecycleState();
        if (lifecycleState < 2 || lifecycleState >= 5) {
            Log.w(TAG, "requestDirectActions(" + r + "): wrong lifecycle: " + lifecycleState);
            callback.sendResult(null);
            return;
        }
        if (r.activity.mVoiceInteractor == null || r.activity.mVoiceInteractor.mInteractor.asBinder() != interactor.asBinder()) {
            if (r.activity.mVoiceInteractor != null) {
                r.activity.mVoiceInteractor.destroy();
            }
            r.activity.mVoiceInteractor = new VoiceInteractor(interactor, r.activity, r.activity, Looper.myLooper());
        }
        r.activity.onGetDirectActions(cancellationSignal, new Consumer() { // from class: android.app.-$$Lambda$ActivityThread$FmvGY8exyv0L0oqZrnunpl8OFI8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ActivityThread.lambda$handleRequestDirectActions$0(ActivityThread.ActivityClientRecord.this, callback, (List) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$handleRequestDirectActions$0(ActivityClientRecord r, RemoteCallback callback, List actions) {
        Preconditions.checkNotNull(actions);
        Preconditions.checkCollectionElementsNotNull(actions, Slice.HINT_ACTIONS);
        if (!actions.isEmpty()) {
            int actionCount = actions.size();
            for (int i = 0; i < actionCount; i++) {
                DirectAction action = (DirectAction) actions.get(i);
                action.setSource(r.activity.getTaskId(), r.activity.getAssistToken());
            }
            Bundle result = new Bundle();
            result.putParcelable(DirectAction.KEY_ACTIONS_LIST, new ParceledListSlice(actions));
            callback.sendResult(result);
            return;
        }
        callback.sendResult(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePerformDirectAction(IBinder activityToken, String actionId, Bundle arguments, CancellationSignal cancellationSignal, final RemoteCallback resultCallback) {
        ActivityClientRecord r = this.mActivities.get(activityToken);
        if (r != null) {
            int lifecycleState = r.getLifecycleState();
            if (lifecycleState < 2 || lifecycleState >= 5) {
                resultCallback.sendResult(null);
                return;
            }
            Bundle nonNullArguments = arguments != null ? arguments : Bundle.EMPTY;
            Activity activity = r.activity;
            Objects.requireNonNull(resultCallback);
            activity.onPerformDirectAction(actionId, nonNullArguments, cancellationSignal, new Consumer() { // from class: android.app.-$$Lambda$ZsFzoG2loyqNOR2cNbo-thrNK5c
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    RemoteCallback.this.sendResult((Bundle) obj);
                }
            });
            return;
        }
        resultCallback.sendResult(null);
    }

    public void handleTranslucentConversionComplete(IBinder token, boolean drawComplete) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            r.activity.onTranslucentConversionComplete(drawComplete);
        }
    }

    public void onNewActivityOptions(IBinder token, ActivityOptions options) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            r.activity.onNewActivityOptions(options);
        }
    }

    public void handleInstallProvider(ProviderInfo info) {
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            installContentProviders(this.mInitialApplication, Arrays.asList(info));
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEnterAnimationComplete(IBinder token) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            r.activity.dispatchEnterAnimationComplete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStartBinderTracking() {
        Binder.enableTracing();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStopBinderTrackingAndDump(ParcelFileDescriptor fd) {
        try {
            Binder.disableTracing();
            Binder.getTransactionTracker().writeTracesToFile(fd);
        } finally {
            IoUtils.closeQuietly(fd);
            Binder.getTransactionTracker().clearTraces();
        }
    }

    @Override // android.app.ClientTransactionHandler
    public void handleMultiWindowModeChanged(IBinder token, boolean isInMultiWindowMode, Configuration overrideConfig) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            Configuration newConfig = new Configuration(this.mConfiguration);
            if (overrideConfig != null) {
                newConfig.updateFrom(overrideConfig);
            }
            r.activity.dispatchMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        }
    }

    @Override // android.app.ClientTransactionHandler
    public void handlePictureInPictureModeChanged(IBinder token, boolean isInPipMode, Configuration overrideConfig) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            Configuration newConfig = new Configuration(this.mConfiguration);
            if (overrideConfig != null) {
                newConfig.updateFrom(overrideConfig);
            }
            r.activity.dispatchPictureInPictureModeChanged(isInPipMode, newConfig);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLocalVoiceInteractionStarted(IBinder token, IVoiceInteractor interactor) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            r.voiceInteractor = interactor;
            r.activity.setVoiceInteractor(interactor);
            if (interactor == null) {
                r.activity.onLocalVoiceInteractionStopped();
            } else {
                r.activity.onLocalVoiceInteractionStarted();
            }
        }
    }

    private static boolean attemptAttachAgent(String agent, ClassLoader classLoader) {
        try {
            VMDebug.attachAgent(agent, classLoader);
            return true;
        } catch (IOException e) {
            Slog.e(TAG, "Attaching agent with " + classLoader + " failed: " + agent);
            return false;
        }
    }

    static void handleAttachAgent(String agent, LoadedApk loadedApk) {
        ClassLoader classLoader = loadedApk != null ? loadedApk.getClassLoader() : null;
        if (!attemptAttachAgent(agent, classLoader) && classLoader != null) {
            attemptAttachAgent(agent, null);
        }
    }

    public static Intent getIntentBeingBroadcast() {
        return sCurrentBroadcastIntent.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void handleReceiver(ReceiverData data) {
        unscheduleGcIdler();
        String component = data.intent.getComponent().getClassName();
        LoadedApk packageInfo = getPackageInfoNoCheck(data.info.applicationInfo, data.compatInfo);
        IActivityManager mgr = ActivityManager.getService();
        try {
            Application app = packageInfo.makeApplication(false, this.mInstrumentation);
            ContextImpl context = (ContextImpl) app.getBaseContext();
            if (data.info.splitName != null) {
                context = (ContextImpl) context.createContextForSplit(data.info.splitName);
            }
            ClassLoader cl = context.getClassLoader();
            data.intent.setExtrasClassLoader(cl);
            data.intent.prepareToEnterProcess();
            data.setExtrasClassLoader(cl);
            BroadcastReceiver receiver = packageInfo.getAppFactory().instantiateReceiver(cl, data.info.name, data.intent);
            try {
                try {
                    if (localLOGV) {
                        Slog.v(TAG, "Performing receive of " + data.intent + ": app=" + app + ", appName=" + app.getPackageName() + ", pkg=" + packageInfo.getPackageName() + ", comp=" + data.intent.getComponent().toShortString() + ", dir=" + packageInfo.getAppDir());
                    }
                    sCurrentBroadcastIntent.set(data.intent);
                    receiver.setPendingResult(data);
                    receiver.onReceive(context.getReceiverRestrictedContext(), data.intent);
                } catch (Throwable th) {
                    sCurrentBroadcastIntent.set(null);
                    throw th;
                }
            } catch (Exception e) {
                if (DEBUG_BROADCAST) {
                    Slog.i(TAG, "Finishing failed broadcast to " + data.intent.getComponent());
                }
                data.sendFinished(mgr);
                if (!this.mInstrumentation.onException(receiver, e)) {
                    throw new RuntimeException("Unable to start receiver " + component + ": " + e.toString(), e);
                }
            }
            sCurrentBroadcastIntent.set(null);
            if (receiver.getPendingResult() != null) {
                data.finish();
            }
        } catch (Exception e2) {
            if (DEBUG_BROADCAST) {
                Slog.i(TAG, "Finishing failed broadcast to " + data.intent.getComponent());
            }
            data.sendFinished(mgr);
            throw new RuntimeException("Unable to instantiate receiver " + component + ": " + e2.toString(), e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCreateBackupAgent(CreateBackupAgentData data) {
        try {
            PackageInfo requestedPackage = getPackageManager().getPackageInfo(data.appInfo.packageName, 0, UserHandle.myUserId());
            if (requestedPackage.applicationInfo.uid != Process.myUid()) {
                Slog.w(TAG, "Asked to instantiate non-matching package " + data.appInfo.packageName);
                return;
            }
            unscheduleGcIdler();
            LoadedApk packageInfo = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
            String packageName = packageInfo.mPackageName;
            if (packageName == null) {
                Slog.d(TAG, "Asked to create backup agent for nonexistent package");
                return;
            }
            String classname = data.appInfo.backupAgentName;
            if (classname == null && (data.backupMode == 1 || data.backupMode == 3)) {
                classname = "android.app.backup.FullBackupAgent";
            }
            IBinder binder = null;
            try {
                ArrayMap<String, BackupAgent> backupAgents = getBackupAgentsForUser(data.userId);
                BackupAgent agent = backupAgents.get(packageName);
                if (agent != null) {
                    binder = agent.onBind();
                } else {
                    try {
                        ClassLoader cl = packageInfo.getClassLoader();
                        BackupAgent agent2 = (BackupAgent) cl.loadClass(classname).newInstance();
                        ContextImpl context = ContextImpl.createAppContext(this, packageInfo);
                        context.setOuterContext(agent2);
                        agent2.attach(context);
                        agent2.onCreate(UserHandle.of(data.userId));
                        binder = agent2.onBind();
                        backupAgents.put(packageName, agent2);
                    } catch (Exception e) {
                        Slog.e(TAG, "Agent threw during creation: " + e);
                        if (data.backupMode != 2 && data.backupMode != 3) {
                            throw e;
                        }
                    }
                }
                try {
                    ActivityManager.getService().backupAgentCreated(packageName, binder, data.userId);
                } catch (RemoteException e2) {
                    throw e2.rethrowFromSystemServer();
                }
            } catch (Exception e3) {
                throw new RuntimeException("Unable to create BackupAgent " + classname + ": " + e3.toString(), e3);
            }
        } catch (RemoteException e4) {
            throw e4.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDestroyBackupAgent(CreateBackupAgentData data) {
        LoadedApk packageInfo = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
        String packageName = packageInfo.mPackageName;
        ArrayMap<String, BackupAgent> backupAgents = getBackupAgentsForUser(data.userId);
        BackupAgent agent = backupAgents.get(packageName);
        if (agent != null) {
            try {
                agent.onDestroy();
            } catch (Exception e) {
                Slog.w(TAG, "Exception thrown in onDestroy by backup agent of " + data.appInfo);
                e.printStackTrace();
            }
            backupAgents.remove(packageName);
            return;
        }
        Slog.w(TAG, "Attempt to destroy unknown backup agent " + data);
    }

    private ArrayMap<String, BackupAgent> getBackupAgentsForUser(int userId) {
        ArrayMap<String, BackupAgent> backupAgents = this.mBackupAgentsByUser.get(userId);
        if (backupAgents == null) {
            ArrayMap<String, BackupAgent> backupAgents2 = new ArrayMap<>();
            this.mBackupAgentsByUser.put(userId, backupAgents2);
            return backupAgents2;
        }
        return backupAgents;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @UnsupportedAppUsage
    public void handleCreateService(CreateServiceData data) {
        unscheduleGcIdler();
        LoadedApk packageInfo = getPackageInfoNoCheck(data.info.applicationInfo, data.compatInfo);
        Service service = null;
        try {
            ClassLoader cl = packageInfo.getClassLoader();
            service = packageInfo.getAppFactory().instantiateService(cl, data.info.name, data.intent);
        } catch (Exception e) {
            if (!this.mInstrumentation.onException(null, e)) {
                throw new RuntimeException("Unable to instantiate service " + data.info.name + ": " + e.toString(), e);
            }
        }
        try {
            if (localLOGV) {
                Slog.v(TAG, "Creating service " + data.info.name);
            }
            ContextImpl context = ContextImpl.createAppContext(this, packageInfo);
            context.setOuterContext(service);
            Application app = packageInfo.makeApplication(false, this.mInstrumentation);
            service.attach(context, this, data.info.name, data.token, app, ActivityManager.getService());
            service.onCreate();
            this.mServices.put(data.token, service);
            try {
                ActivityManager.getService().serviceDoneExecuting(data.token, 0, 0, 0);
            } catch (RemoteException e2) {
                throw e2.rethrowFromSystemServer();
            }
        } catch (Exception e3) {
            if (!this.mInstrumentation.onException(service, e3)) {
                throw new RuntimeException("Unable to create service " + data.info.name + ": " + e3.toString(), e3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBindService(BindServiceData data) {
        Service s = this.mServices.get(data.token);
        if (DEBUG_SERVICE) {
            Slog.v(TAG, "handleBindService s=" + s + " rebind=" + data.rebind);
        }
        if (s != null) {
            try {
                data.intent.setExtrasClassLoader(s.getClassLoader());
                data.intent.prepareToEnterProcess();
                try {
                    if (!data.rebind) {
                        IBinder binder = s.onBind(data.intent);
                        ActivityManager.getService().publishService(data.token, data.intent, binder);
                    } else {
                        s.onRebind(data.intent);
                        ActivityManager.getService().serviceDoneExecuting(data.token, 0, 0, 0);
                    }
                } catch (RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            } catch (Exception e) {
                if (!this.mInstrumentation.onException(s, e)) {
                    throw new RuntimeException("Unable to bind to service " + s + " with " + data.intent + ": " + e.toString(), e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUnbindService(BindServiceData data) {
        Service s = this.mServices.get(data.token);
        if (s != null) {
            try {
                data.intent.setExtrasClassLoader(s.getClassLoader());
                data.intent.prepareToEnterProcess();
                boolean doRebind = s.onUnbind(data.intent);
                try {
                    if (doRebind) {
                        ActivityManager.getService().unbindFinished(data.token, data.intent, doRebind);
                    } else {
                        ActivityManager.getService().serviceDoneExecuting(data.token, 0, 0, 0);
                    }
                } catch (RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            } catch (Exception e) {
                if (!this.mInstrumentation.onException(s, e)) {
                    throw new RuntimeException("Unable to unbind to service " + s + " with " + data.intent + ": " + e.toString(), e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDumpService(DumpComponentInfo info) {
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            Service s = this.mServices.get(info.token);
            if (s != null) {
                PrintWriter pw = new FastPrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                s.dump(info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
        } finally {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDumpActivity(DumpComponentInfo info) {
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            ActivityClientRecord r = this.mActivities.get(info.token);
            if (r != null && r.activity != null) {
                PrintWriter pw = new FastPrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                r.activity.dump(info.prefix, info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
        } finally {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDumpProvider(DumpComponentInfo info) {
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            ProviderClientRecord r = this.mLocalProviders.get(info.token);
            if (r != null && r.mLocalProvider != null) {
                PrintWriter pw = new FastPrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                r.mLocalProvider.dump(info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
        } finally {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleServiceArgs(ServiceArgsData data) {
        int res;
        Service s = this.mServices.get(data.token);
        if (s != null) {
            try {
                if (data.args != null) {
                    data.args.setExtrasClassLoader(s.getClassLoader());
                    data.args.prepareToEnterProcess();
                }
                if (!data.taskRemoved) {
                    res = s.onStartCommand(data.args, data.flags, data.startId);
                } else {
                    s.onTaskRemoved(data.args);
                    res = 1000;
                }
                QueuedWork.waitToFinish();
                try {
                    ActivityManager.getService().serviceDoneExecuting(data.token, 1, data.startId, res);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Exception e2) {
                if (!this.mInstrumentation.onException(s, e2)) {
                    throw new RuntimeException("Unable to start service " + s + " with " + data.args + ": " + e2.toString(), e2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStopService(IBinder token) {
        Service s = this.mServices.remove(token);
        if (s != null) {
            try {
                if (localLOGV) {
                    Slog.v(TAG, "Destroying service " + s);
                }
                s.onDestroy();
                s.detachAndCleanUp();
                Context context = s.getBaseContext();
                if (context instanceof ContextImpl) {
                    String who = s.getClassName();
                    ((ContextImpl) context).scheduleFinalCleanup(who, "Service");
                }
                QueuedWork.waitToFinish();
                try {
                    ActivityManager.getService().serviceDoneExecuting(token, 2, 0, 0);
                    return;
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Exception e2) {
                if (!this.mInstrumentation.onException(s, e2)) {
                    throw new RuntimeException("Unable to stop service " + s + ": " + e2.toString(), e2);
                }
                Slog.i(TAG, "handleStopService: exception for " + token, e2);
                return;
            }
        }
        Slog.i(TAG, "handleStopService: token=" + token + " not found.");
    }

    @VisibleForTesting
    public ActivityClientRecord performResumeActivity(IBinder token, boolean finalStateRequest, String reason) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (localLOGV) {
            Slog.v(TAG, "Performing resume of " + r + " finished=" + r.activity.mFinished);
        }
        if (r == null || r.activity.mFinished) {
            return null;
        }
        if (r.getLifecycleState() == 3) {
            if (!finalStateRequest) {
                RuntimeException e = new IllegalStateException("Trying to resume activity which is already resumed");
                Slog.e(TAG, e.getMessage(), e);
                Slog.e(TAG, r.getStateString());
            }
            return null;
        }
        if (finalStateRequest) {
            r.hideForNow = false;
            r.activity.mStartedActivity = false;
        }
        try {
            r.activity.onStateNotSaved();
            r.activity.mFragments.noteStateNotSaved();
            checkAndBlockForNetworkAccess();
            if (r.pendingIntents != null) {
                deliverNewIntents(r, r.pendingIntents);
                r.pendingIntents = null;
            }
            if (r.pendingResults != null) {
                deliverResults(r, r.pendingResults, reason);
                r.pendingResults = null;
            }
            r.activity.performResume(r.startsNotResumed, reason);
            r.state = null;
            r.persistentState = null;
            r.setState(3);
            reportTopResumedActivityChanged(r, r.isTopResumedActivity, "topWhenResuming");
        } catch (Exception e2) {
            if (!this.mInstrumentation.onException(r.activity, e2)) {
                throw new RuntimeException("Unable to resume activity " + r.intent.getComponent().toShortString() + ": " + e2.toString(), e2);
            }
        }
        xpActivityThreadProxy xpactivitythreadproxy = this.mActivityThreadProxy;
        if (xpactivitythreadproxy != null) {
            xpactivitythreadproxy.performResumeActivity(r, this.mH, finalStateRequest, reason);
        }
        return r;
    }

    static final void cleanUpPendingRemoveWindows(ActivityClientRecord r, boolean force) {
        if (r.mPreserveWindow && !force) {
            return;
        }
        if (r.mPendingRemoveWindow != null) {
            r.mPendingRemoveWindowManager.removeViewImmediate(r.mPendingRemoveWindow.getDecorView());
            IBinder wtoken = r.mPendingRemoveWindow.getDecorView().getWindowToken();
            if (wtoken != null) {
                WindowManagerGlobal.getInstance().closeAll(wtoken, r.activity.getClass().getName(), "Activity");
            }
        }
        r.mPendingRemoveWindow = null;
        r.mPendingRemoveWindowManager = null;
    }

    @Override // android.app.ClientTransactionHandler
    public void handleResumeActivity(IBinder token, boolean finalStateRequest, boolean isForward, String reason) {
        unscheduleGcIdler();
        this.mSomeActivitiesChanged = true;
        ActivityClientRecord r = performResumeActivity(token, finalStateRequest, reason);
        if (r == null || this.mActivitiesToBeDestroyed.containsKey(token)) {
            return;
        }
        Activity a = r.activity;
        if (localLOGV) {
            Slog.v(TAG, "Resume " + r + " started activity: " + a.mStartedActivity + ", hideForNow: " + r.hideForNow + ", finished: " + a.mFinished);
        }
        int forwardBit = isForward ? 256 : 0;
        boolean willBeVisible = !a.mStartedActivity;
        if (!willBeVisible) {
            try {
                willBeVisible = ActivityTaskManager.getService().willActivityBeVisible(a.getActivityToken());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        if (r.window == null && !a.mFinished && willBeVisible) {
            r.window = r.activity.getWindow();
            View decor = r.window.getDecorView();
            decor.setVisibility(4);
            ViewManager wm = a.getWindowManager();
            WindowManager.LayoutParams l = r.window.getAttributes();
            a.mDecor = decor;
            l.type = 1;
            int displayId = a.getDisplayId();
            WindowManager.LayoutParams lp = xpWindowManager.getOverrideLayoutParams(r);
            if (lp != null) {
                if (displayId > 0) {
                    lp.displayId = displayId;
                }
                if (r.window instanceof PhoneWindow) {
                    PhoneWindow window = (PhoneWindow) r.window;
                    if (window.isFloating()) {
                        lp.xpFlags |= 64;
                    }
                    if (window.isTranslucent()) {
                        lp.xpFlags |= 128;
                    }
                }
                l.copyFrom(lp);
                r.window.setAttributes(l);
                r.activity.getWindow().setAttributes(l);
                r.overrideConfig = xpActivityManager.getOverrideConfiguration(r.overrideConfig, l);
            }
            l.softInputMode |= forwardBit;
            if (r.mPreserveWindow) {
                a.mWindowAdded = true;
                r.mPreserveWindow = false;
                ViewRootImpl impl = decor.getViewRootImpl();
                if (impl != null) {
                    impl.notifyChildRebuilt();
                }
            }
            if (a.mVisibleFromClient) {
                if (!a.mWindowAdded) {
                    a.mWindowAdded = true;
                    wm.addView(decor, l);
                } else {
                    a.onWindowAttributesChanged(l);
                }
            }
        } else if (!willBeVisible) {
            if (localLOGV) {
                Slog.v(TAG, "Launch " + r + " mStartedActivity set");
            }
            r.hideForNow = true;
        }
        cleanUpPendingRemoveWindows(r, false);
        if (!r.activity.mFinished && willBeVisible && r.activity.mDecor != null && !r.hideForNow) {
            if (r.newConfig != null) {
                performConfigurationChangedForActivity(r, r.newConfig);
                if (DEBUG_CONFIGURATION) {
                    Slog.v(TAG, "Resuming activity " + r.activityInfo.name + " with newConfig " + r.activity.mCurrentConfig);
                }
                r.newConfig = null;
            }
            if (localLOGV) {
                Slog.v(TAG, "Resuming " + r + " with isForward=" + isForward);
            }
            WindowManager.LayoutParams l2 = r.window.getAttributes();
            if ((l2.softInputMode & 256) != forwardBit) {
                l2.softInputMode = (l2.softInputMode & TrafficStats.TAG_NETWORK_STACK_RANGE_END) | forwardBit;
                if (r.activity.mVisibleFromClient) {
                    ViewManager wm2 = a.getWindowManager();
                    wm2.updateViewLayout(r.window.getDecorView(), l2);
                }
            }
            r.activity.mVisibleFromServer = true;
            this.mNumVisibleActivities++;
            if (r.activity.mVisibleFromClient) {
                r.activity.makeVisible();
            }
        }
        r.nextIdle = this.mNewActivities;
        this.mNewActivities = r;
        xpActivityThreadProxy xpactivitythreadproxy = this.mActivityThreadProxy;
        if (xpactivitythreadproxy != null) {
            xpactivitythreadproxy.handleResumeActivity(r, this.mH, reason);
        }
        if (localLOGV) {
            Slog.v(TAG, "Scheduling idle handler for " + r);
        }
        Looper.myQueue().addIdleHandler(new Idler());
    }

    @Override // android.app.ClientTransactionHandler
    public void handleTopResumedActivityChanged(IBinder token, boolean onTop, String reason) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r == null || r.activity == null) {
            Slog.w(TAG, "Not found target activity to report position change for token: " + token);
            return;
        }
        if (DEBUG_ORDER) {
            Slog.d(TAG, "Received position change to top: " + onTop + " for activity: " + r);
        }
        if (r.isTopResumedActivity == onTop) {
            throw new IllegalStateException("Activity top position already set to onTop=" + onTop);
        }
        r.isTopResumedActivity = onTop;
        if (r.getLifecycleState() == 3) {
            reportTopResumedActivityChanged(r, onTop, "topStateChangedWhenResumed");
        } else if (DEBUG_ORDER) {
            Slog.d(TAG, "Won't deliver top position change in state=" + r.getLifecycleState());
        }
    }

    private void reportTopResumedActivityChanged(ActivityClientRecord r, boolean onTop, String reason) {
        if (r.lastReportedTopResumedState != onTop) {
            r.lastReportedTopResumedState = onTop;
            r.activity.performTopResumedActivityChanged(onTop, reason);
            xpActivityThreadProxy xpactivitythreadproxy = this.mActivityThreadProxy;
            if (xpactivitythreadproxy == null || !onTop) {
                return;
            }
            xpactivitythreadproxy.performTopResumedActivityChanged(r);
        }
    }

    @Override // android.app.ClientTransactionHandler
    public void handlePauseActivity(IBinder token, boolean finished, boolean userLeaving, int configChanges, int resumingFlags, PendingTransactionActions pendingActions, String reason) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            if (userLeaving) {
                performUserLeavingActivity(r);
            }
            r.activity.mConfigChangeFlags |= configChanges;
            performPauseActivity(r, finished, reason, pendingActions);
            if (r.isPreHoneycomb()) {
                QueuedWork.waitToFinish();
            }
            this.mSomeActivitiesChanged = true;
        }
        xpActivityThreadProxy xpactivitythreadproxy = this.mActivityThreadProxy;
        if (xpactivitythreadproxy != null) {
            xpactivitythreadproxy.handlePauseActivity(r, finished, userLeaving, configChanges, resumingFlags, reason);
        }
    }

    final void performUserLeavingActivity(ActivityClientRecord r) {
        this.mInstrumentation.callActivityOnUserLeaving(r.activity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Bundle performPauseActivity(IBinder token, boolean finished, String reason, PendingTransactionActions pendingActions) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            return performPauseActivity(r, finished, reason, pendingActions);
        }
        return null;
    }

    private Bundle performPauseActivity(ActivityClientRecord r, boolean finished, String reason, PendingTransactionActions pendingActions) {
        ArrayList<OnActivityPausedListener> listeners;
        xpActivityThreadProxy xpactivitythreadproxy = this.mActivityThreadProxy;
        if (xpactivitythreadproxy != null) {
            xpactivitythreadproxy.performPauseActivity(r, finished, reason);
        }
        if (r.paused) {
            if (r.activity.mFinished) {
                return null;
            }
            RuntimeException e = new RuntimeException("Performing pause of activity that is not resumed: " + r.intent.getComponent().toShortString());
            Slog.e(TAG, e.getMessage(), e);
        }
        boolean shouldSaveState = true;
        if (finished) {
            r.activity.mFinished = true;
        }
        shouldSaveState = (r.activity.mFinished || !r.isPreHoneycomb()) ? false : false;
        if (shouldSaveState) {
            callActivityOnSaveInstanceState(r);
        }
        performPauseActivityIfNeeded(r, reason);
        synchronized (this.mOnPauseListeners) {
            listeners = this.mOnPauseListeners.remove(r.activity);
        }
        int size = listeners != null ? listeners.size() : 0;
        for (int i = 0; i < size; i++) {
            listeners.get(i).onPaused(r.activity);
        }
        Bundle oldState = pendingActions != null ? pendingActions.getOldState() : null;
        if (oldState != null && r.isPreHoneycomb()) {
            r.state = oldState;
        }
        if (shouldSaveState) {
            return r.state;
        }
        return null;
    }

    private void performPauseActivityIfNeeded(ActivityClientRecord r, String reason) {
        if (r.paused) {
            return;
        }
        xpActivityThreadProxy xpactivitythreadproxy = this.mActivityThreadProxy;
        if (xpactivitythreadproxy != null) {
            xpactivitythreadproxy.performPauseActivityIfNeeded(r, reason);
        }
        reportTopResumedActivityChanged(r, false, "pausing");
        try {
            r.activity.mCalled = false;
            this.mInstrumentation.callActivityOnPause(r.activity);
        } catch (SuperNotCalledException e) {
            throw e;
        } catch (Exception e2) {
            if (!this.mInstrumentation.onException(r.activity, e2)) {
                throw new RuntimeException("Unable to pause activity " + safeToComponentShortString(r.intent) + ": " + e2.toString(), e2);
            }
        }
        if (!r.activity.mCalled) {
            throw new SuperNotCalledException("Activity " + safeToComponentShortString(r.intent) + " did not call through to super.onPause()");
        }
        r.setState(4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public final void performStopActivity(IBinder token, boolean saveState, String reason) {
        ActivityClientRecord r = this.mActivities.get(token);
        performStopActivityInner(r, null, false, saveState, false, reason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ProviderRefCount {
        public final ProviderClientRecord client;
        public final ContentProviderHolder holder;
        public boolean removePending;
        public int stableCount;
        public int unstableCount;

        ProviderRefCount(ContentProviderHolder inHolder, ProviderClientRecord inClient, int sCount, int uCount) {
            this.holder = inHolder;
            this.client = inClient;
            this.stableCount = sCount;
            this.unstableCount = uCount;
        }
    }

    private void performStopActivityInner(ActivityClientRecord r, PendingTransactionActions.StopInfo info, boolean keepShown, boolean saveState, boolean finalStateRequest, String reason) {
        if (localLOGV) {
            Slog.v(TAG, "Performing stop of " + r);
        }
        if (r != null) {
            if (!keepShown && r.stopped) {
                if (r.activity.mFinished) {
                    return;
                }
                if (!finalStateRequest) {
                    RuntimeException e = new RuntimeException("Performing stop of activity that is already stopped: " + r.intent.getComponent().toShortString());
                    Slog.e(TAG, e.getMessage(), e);
                    Slog.e(TAG, r.getStateString());
                }
            }
            performPauseActivityIfNeeded(r, reason);
            if (info != null) {
                try {
                    info.setDescription(r.activity.onCreateDescription());
                } catch (Exception e2) {
                    if (!this.mInstrumentation.onException(r.activity, e2)) {
                        throw new RuntimeException("Unable to save state of activity " + r.intent.getComponent().toShortString() + ": " + e2.toString(), e2);
                    }
                }
            }
            if (!keepShown) {
                callActivityOnStop(r, saveState, reason);
            }
        }
    }

    private void callActivityOnStop(ActivityClientRecord r, boolean saveState, String reason) {
        boolean shouldSaveState = saveState && !r.activity.mFinished && r.state == null && !r.isPreHoneycomb();
        boolean isPreP = r.isPreP();
        if (shouldSaveState && isPreP) {
            callActivityOnSaveInstanceState(r);
        }
        try {
            r.activity.performStop(r.mPreserveWindow, reason);
        } catch (SuperNotCalledException e) {
            throw e;
        } catch (Exception e2) {
            if (!this.mInstrumentation.onException(r.activity, e2)) {
                throw new RuntimeException("Unable to stop activity " + r.intent.getComponent().toShortString() + ": " + e2.toString(), e2);
            }
        }
        r.setState(5);
        if (shouldSaveState && !isPreP) {
            callActivityOnSaveInstanceState(r);
        }
    }

    private void updateVisibility(ActivityClientRecord r, boolean show) {
        View v = r.activity.mDecor;
        if (v != null) {
            if (show) {
                if (!r.activity.mVisibleFromServer) {
                    r.activity.mVisibleFromServer = true;
                    this.mNumVisibleActivities++;
                    if (r.activity.mVisibleFromClient) {
                        r.activity.makeVisible();
                    }
                }
                if (r.newConfig != null) {
                    performConfigurationChangedForActivity(r, r.newConfig);
                    if (DEBUG_CONFIGURATION) {
                        Slog.v(TAG, "Updating activity vis " + r.activityInfo.name + " with new config " + r.activity.mCurrentConfig);
                    }
                    r.newConfig = null;
                }
            } else if (r.activity.mVisibleFromServer) {
                r.activity.mVisibleFromServer = false;
                this.mNumVisibleActivities--;
                v.setVisibility(4);
            }
        }
    }

    @Override // android.app.ClientTransactionHandler
    public void handleStopActivity(IBinder token, boolean show, int configChanges, PendingTransactionActions pendingActions, boolean finalStateRequest, String reason) {
        ActivityClientRecord r = this.mActivities.get(token);
        r.activity.mConfigChangeFlags |= configChanges;
        boolean show2 = xpWindowManager.keepActivityShown(r, show);
        PendingTransactionActions.StopInfo stopInfo = new PendingTransactionActions.StopInfo();
        performStopActivityInner(r, stopInfo, show2, true, finalStateRequest, reason);
        if (localLOGV) {
            Slog.v(TAG, "Finishing stop of " + r + ": show=" + show2 + " win=" + r.window);
        }
        updateVisibility(r, show2);
        if (!r.isPreHoneycomb()) {
            QueuedWork.waitToFinish();
        }
        if (r.window != null) {
            WindowManager.LayoutParams l = r.window.getAttributes();
            l.softInputMode &= TrafficStats.TAG_NETWORK_STACK_RANGE_END;
            r.window.setAttributes(l);
        }
        stopInfo.setActivity(r);
        stopInfo.setState(r.state);
        stopInfo.setPersistentState(r.persistentState);
        pendingActions.setStopInfo(stopInfo);
        this.mSomeActivitiesChanged = true;
    }

    @Override // android.app.ClientTransactionHandler
    public void reportStop(PendingTransactionActions pendingActions) {
        this.mH.post(pendingActions.getStopInfo());
    }

    @Override // android.app.ClientTransactionHandler
    public void performRestartActivity(IBinder token, boolean start) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r.stopped) {
            r.activity.performRestart(start, "performRestartActivity");
            if (start) {
                r.setState(2);
            }
        }
    }

    @Override // android.app.ClientTransactionHandler
    public void handleWindowVisibility(IBinder token, boolean show) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r == null) {
            Log.w(TAG, "handleWindowVisibility: no activity for token " + token);
            return;
        }
        if (!show && !r.stopped) {
            performStopActivityInner(r, null, show, false, false, "handleWindowVisibility");
        } else if (show && r.getLifecycleState() == 5) {
            unscheduleGcIdler();
            r.activity.performRestart(true, "handleWindowVisibility");
            r.setState(2);
        }
        if (r.activity.mDecor != null) {
            updateVisibility(r, show);
        }
        this.mSomeActivitiesChanged = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSleeping(IBinder token, boolean sleeping) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r == null) {
            Log.w(TAG, "handleSleeping: no activity for token " + token);
        } else if (sleeping) {
            if (!r.stopped && !r.isPreHoneycomb()) {
                callActivityOnStop(r, true, "sleeping");
            }
            if (!r.isPreHoneycomb()) {
                QueuedWork.waitToFinish();
            }
            try {
                ActivityTaskManager.getService().activitySlept(r.token);
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        } else if (r.stopped && r.activity.mVisibleFromServer) {
            r.activity.performRestart(true, "handleSleeping");
            r.setState(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetCoreSettings(Bundle coreSettings) {
        synchronized (this.mResourcesManager) {
            this.mCoreSettings = coreSettings;
        }
        onCoreSettingsChange();
    }

    private void onCoreSettingsChange() {
        if (updateDebugViewAttributeState()) {
            relaunchAllActivities(false);
        }
    }

    private boolean updateDebugViewAttributeState() {
        boolean previousState = View.sDebugViewAttributes;
        String str = "";
        View.sDebugViewAttributesApplicationPackage = this.mCoreSettings.getString(Settings.Global.DEBUG_VIEW_ATTRIBUTES_APPLICATION_PACKAGE, "");
        AppBindData appBindData = this.mBoundApplication;
        if (appBindData != null && appBindData.appInfo != null) {
            str = this.mBoundApplication.appInfo.packageName;
        }
        String currentPackage = str;
        View.sDebugViewAttributes = this.mCoreSettings.getInt(Settings.Global.DEBUG_VIEW_ATTRIBUTES, 0) != 0 || View.sDebugViewAttributesApplicationPackage.equals(currentPackage);
        return previousState != View.sDebugViewAttributes;
    }

    private void relaunchAllActivities(boolean preserveWindows) {
        for (Map.Entry<IBinder, ActivityClientRecord> entry : this.mActivities.entrySet()) {
            ActivityClientRecord r = entry.getValue();
            if (!r.activity.mFinished) {
                if (preserveWindows && r.window != null) {
                    r.mPreserveWindow = true;
                }
                scheduleRelaunchActivity(entry.getKey());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdatePackageCompatibilityInfo(UpdateCompatibilityData data) {
        LoadedApk apk = peekPackageInfo(data.pkg, false);
        if (apk != null) {
            apk.setCompatibilityInfo(data.info);
        }
        LoadedApk apk2 = peekPackageInfo(data.pkg, true);
        if (apk2 != null) {
            apk2.setCompatibilityInfo(data.info);
        }
        handleConfigurationChanged(this.mConfiguration, data.info);
        WindowManagerGlobal.getInstance().reportNewConfiguration(this.mConfiguration);
    }

    private void deliverResults(ActivityClientRecord r, List<ResultInfo> results, String reason) {
        int N = results.size();
        for (int i = 0; i < N; i++) {
            ResultInfo ri = results.get(i);
            try {
                if (ri.mData != null) {
                    ri.mData.setExtrasClassLoader(r.activity.getClassLoader());
                    ri.mData.prepareToEnterProcess();
                }
                if (DEBUG_RESULTS) {
                    Slog.v(TAG, "Delivering result to activity " + r + " : " + ri);
                }
                r.activity.dispatchActivityResult(ri.mResultWho, ri.mRequestCode, ri.mResultCode, ri.mData, reason);
            } catch (Exception e) {
                if (!this.mInstrumentation.onException(r.activity, e)) {
                    throw new RuntimeException("Failure delivering result " + ri + " to activity " + r.intent.getComponent().toShortString() + ": " + e.toString(), e);
                }
            }
        }
    }

    @Override // android.app.ClientTransactionHandler
    public void handleSendResult(IBinder token, List<ResultInfo> results, String reason) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (DEBUG_RESULTS) {
            Slog.v(TAG, "Handling send result to " + r);
        }
        if (r != null) {
            boolean resumed = !r.paused;
            if (!r.activity.mFinished && r.activity.mDecor != null && r.hideForNow && resumed) {
                updateVisibility(r, true);
            }
            if (resumed) {
                try {
                    r.activity.mCalled = false;
                    this.mInstrumentation.callActivityOnPause(r.activity);
                    if (!r.activity.mCalled) {
                        throw new SuperNotCalledException("Activity " + r.intent.getComponent().toShortString() + " did not call through to super.onPause()");
                    }
                } catch (SuperNotCalledException e) {
                    throw e;
                } catch (Exception e2) {
                    if (!this.mInstrumentation.onException(r.activity, e2)) {
                        throw new RuntimeException("Unable to pause activity " + r.intent.getComponent().toShortString() + ": " + e2.toString(), e2);
                    }
                }
            }
            checkAndBlockForNetworkAccess();
            deliverResults(r, results, reason);
            if (resumed) {
                r.activity.performResume(false, reason);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityClientRecord performDestroyActivity(IBinder token, boolean finishing, int configChanges, boolean getNonConfigInstance, String reason) {
        ActivityClientRecord r = this.mActivities.get(token);
        Class<?> cls = null;
        if (localLOGV) {
            Slog.v(TAG, "Performing finish of " + r);
        }
        if (r != null) {
            cls = r.activity.getClass();
            r.activity.mConfigChangeFlags |= configChanges;
            if (finishing) {
                r.activity.mFinished = true;
            }
            performPauseActivityIfNeeded(r, "destroy");
            if (!r.stopped) {
                callActivityOnStop(r, false, "destroy");
            }
            if (getNonConfigInstance) {
                try {
                    r.lastNonConfigurationInstances = r.activity.retainNonConfigurationInstances();
                } catch (Exception e) {
                    if (!this.mInstrumentation.onException(r.activity, e)) {
                        throw new RuntimeException("Unable to retain activity " + r.intent.getComponent().toShortString() + ": " + e.toString(), e);
                    }
                }
            }
            try {
                r.activity.mCalled = false;
                this.mInstrumentation.callActivityOnDestroy(r.activity);
            } catch (SuperNotCalledException e2) {
                throw e2;
            } catch (Exception e3) {
                if (!this.mInstrumentation.onException(r.activity, e3)) {
                    throw new RuntimeException("Unable to destroy activity " + safeToComponentShortString(r.intent) + ": " + e3.toString(), e3);
                }
            }
            if (!r.activity.mCalled) {
                throw new SuperNotCalledException("Activity " + safeToComponentShortString(r.intent) + " did not call through to super.onDestroy()");
            }
            if (r.window != null) {
                r.window.closeAllPanels();
            }
            r.setState(6);
        }
        schedulePurgeIdler();
        synchronized (this.mResourcesManager) {
            this.mActivities.remove(token);
        }
        StrictMode.decrementExpectedActivityCount(cls);
        return r;
    }

    private static String safeToComponentShortString(Intent intent) {
        ComponentName component = intent.getComponent();
        return component == null ? "[Unknown]" : component.toShortString();
    }

    @Override // android.app.ClientTransactionHandler
    public Map<IBinder, ClientTransactionItem> getActivitiesToBeDestroyed() {
        return this.mActivitiesToBeDestroyed;
    }

    @Override // android.app.ClientTransactionHandler
    public void handleDestroyActivity(IBinder token, boolean finishing, int configChanges, boolean getNonConfigInstance, String reason) {
        ActivityClientRecord r = performDestroyActivity(token, finishing, configChanges, getNonConfigInstance, reason);
        if (r != null) {
            cleanUpPendingRemoveWindows(r, finishing);
            WindowManager wm = r.activity.getWindowManager();
            View v = r.activity.mDecor;
            if (v != null) {
                if (r.activity.mVisibleFromServer) {
                    this.mNumVisibleActivities--;
                }
                IBinder wtoken = v.getWindowToken();
                if (r.activity.mWindowAdded) {
                    if (r.mPreserveWindow) {
                        r.mPendingRemoveWindow = r.window;
                        r.mPendingRemoveWindowManager = wm;
                        r.window.clearContentView();
                    } else {
                        wm.removeViewImmediate(v);
                    }
                }
                if (wtoken != null && r.mPendingRemoveWindow == null) {
                    WindowManagerGlobal.getInstance().closeAll(wtoken, r.activity.getClass().getName(), "Activity");
                } else if (r.mPendingRemoveWindow != null) {
                    WindowManagerGlobal.getInstance().closeAllExceptView(token, v, r.activity.getClass().getName(), "Activity");
                }
                r.activity.mDecor = null;
            }
            if (r.mPendingRemoveWindow == null) {
                WindowManagerGlobal.getInstance().closeAll(token, r.activity.getClass().getName(), "Activity");
            }
            Context c = r.activity.getBaseContext();
            if (c instanceof ContextImpl) {
                ((ContextImpl) c).scheduleFinalCleanup(r.activity.getClass().getName(), "Activity");
            }
        }
        if (finishing) {
            try {
                ActivityTaskManager.getService().activityDestroyed(token);
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
        this.mSomeActivitiesChanged = true;
    }

    @Override // android.app.ClientTransactionHandler
    public ActivityClientRecord prepareRelaunchActivity(IBinder token, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, int configChanges, MergedConfiguration config, boolean preserveWindow) {
        ActivityClientRecord target = null;
        boolean scheduleRelaunch = false;
        synchronized (this.mResourcesManager) {
            int i = 0;
            while (true) {
                if (i >= this.mRelaunchingActivities.size()) {
                    break;
                }
                ActivityClientRecord r = this.mRelaunchingActivities.get(i);
                if (DEBUG_ORDER) {
                    Slog.d(TAG, "requestRelaunchActivity: " + this + ", trying: " + r);
                }
                if (r.token != token) {
                    i++;
                } else {
                    target = r;
                    if (pendingResults != null) {
                        if (r.pendingResults != null) {
                            r.pendingResults.addAll(pendingResults);
                        } else {
                            r.pendingResults = pendingResults;
                        }
                    }
                    if (pendingNewIntents != null) {
                        if (r.pendingIntents != null) {
                            r.pendingIntents.addAll(pendingNewIntents);
                        } else {
                            r.pendingIntents = pendingNewIntents;
                        }
                    }
                }
            }
            if (target == null) {
                if (DEBUG_ORDER) {
                    Slog.d(TAG, "requestRelaunchActivity: target is null");
                }
                target = new ActivityClientRecord();
                target.token = token;
                target.pendingResults = pendingResults;
                target.pendingIntents = pendingNewIntents;
                target.mPreserveWindow = preserveWindow;
                this.mRelaunchingActivities.add(target);
                scheduleRelaunch = true;
            }
            target.createdConfig = config.getGlobalConfiguration();
            target.overrideConfig = config.getOverrideConfiguration();
            target.pendingConfigChanges |= configChanges;
        }
        if (scheduleRelaunch) {
            return target;
        }
        return null;
    }

    @Override // android.app.ClientTransactionHandler
    public void handleRelaunchActivity(ActivityClientRecord tmp, PendingTransactionActions pendingActions) {
        ActivityClientRecord activityClientRecord;
        int N;
        Configuration changedConfig;
        unscheduleGcIdler();
        this.mSomeActivitiesChanged = true;
        Configuration changedConfig2 = null;
        synchronized (this.mResourcesManager) {
            try {
                N = this.mRelaunchingActivities.size();
                activityClientRecord = tmp;
            } catch (Throwable th) {
                e = th;
                activityClientRecord = tmp;
            }
            try {
                IBinder token = activityClientRecord.token;
                int i = 0;
                int configChanges = 0;
                ActivityClientRecord tmp2 = null;
                while (i < N) {
                    try {
                        ActivityClientRecord r = this.mRelaunchingActivities.get(i);
                        if (r.token == token) {
                            try {
                                int configChanges2 = r.pendingConfigChanges | configChanges;
                                try {
                                    this.mRelaunchingActivities.remove(i);
                                    i--;
                                    N--;
                                    tmp2 = r;
                                    configChanges = configChanges2;
                                } catch (Throwable th2) {
                                    e = th2;
                                    while (true) {
                                        try {
                                            break;
                                        } catch (Throwable th3) {
                                            e = th3;
                                        }
                                    }
                                    throw e;
                                }
                            } catch (Throwable th4) {
                                e = th4;
                            }
                        }
                        i++;
                    } catch (Throwable th5) {
                        e = th5;
                    }
                }
                if (tmp2 == null) {
                    if (DEBUG_CONFIGURATION) {
                        Slog.v(TAG, "Abort, activity not relaunching!");
                    }
                    return;
                }
                if (DEBUG_CONFIGURATION) {
                    Slog.v(TAG, "Relaunching activity " + tmp2.token + " with configChanges=0x" + Integer.toHexString(configChanges));
                }
                if (this.mPendingConfiguration != null) {
                    changedConfig2 = this.mPendingConfiguration;
                    this.mPendingConfiguration = null;
                }
                if (tmp2.createdConfig == null || ((this.mConfiguration != null && (!tmp2.createdConfig.isOtherSeqNewer(this.mConfiguration) || this.mConfiguration.diff(tmp2.createdConfig) == 0)) || !(changedConfig2 == null || tmp2.createdConfig.isOtherSeqNewer(changedConfig2)))) {
                    changedConfig = changedConfig2;
                } else {
                    Configuration changedConfig3 = tmp2.createdConfig;
                    changedConfig = changedConfig3;
                }
                if (DEBUG_CONFIGURATION) {
                    Slog.v(TAG, "Relaunching activity " + tmp2.token + ": changedConfig=" + changedConfig);
                }
                if (changedConfig != null) {
                    this.mCurDefaultDisplayDpi = changedConfig.densityDpi;
                    updateDefaultDensity();
                    handleConfigurationChanged(changedConfig, null);
                }
                ActivityClientRecord r2 = this.mActivities.get(tmp2.token);
                if (DEBUG_CONFIGURATION) {
                    Slog.v(TAG, "Handling relaunch of " + r2);
                }
                if (r2 == null) {
                    return;
                }
                r2.activity.mConfigChangeFlags |= configChanges;
                r2.mPreserveWindow = tmp2.mPreserveWindow;
                r2.activity.mChangingConfigurations = true;
                try {
                    if (r2.mPreserveWindow) {
                        WindowManagerGlobal.getWindowSession().prepareToReplaceWindows(r2.token, true);
                    }
                    handleRelaunchActivityInner(r2, configChanges, tmp2.pendingResults, tmp2.pendingIntents, pendingActions, tmp2.startsNotResumed, tmp2.overrideConfig, "handleRelaunchActivity");
                    if (pendingActions != null) {
                        pendingActions.setReportRelaunchToWindowManager(true);
                    }
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th6) {
                e = th6;
                while (true) {
                    break;
                    break;
                }
                throw e;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleRelaunchActivity(IBinder token) {
        this.mH.removeMessages(160, token);
        sendMessage(160, token);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRelaunchActivityLocally(IBinder token) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r == null) {
            Log.w(TAG, "Activity to relaunch no longer exists");
            return;
        }
        int prevState = r.getLifecycleState();
        if (prevState < 3 || prevState > 5) {
            Log.w(TAG, "Activity state must be in [ON_RESUME..ON_STOP] in order to be relaunched,current state is " + prevState);
            return;
        }
        MergedConfiguration mergedConfiguration = new MergedConfiguration(r.createdConfig != null ? r.createdConfig : this.mConfiguration, r.overrideConfig);
        ActivityRelaunchItem activityRelaunchItem = ActivityRelaunchItem.obtain(null, null, 0, mergedConfiguration, r.mPreserveWindow);
        ActivityLifecycleItem lifecycleRequest = TransactionExecutorHelper.getLifecycleRequestForCurrentState(r);
        ClientTransaction transaction = ClientTransaction.obtain(this.mAppThread, r.token);
        transaction.addCallback(activityRelaunchItem);
        transaction.setLifecycleStateRequest(lifecycleRequest);
        executeTransaction(transaction);
    }

    private void handleRelaunchActivityInner(ActivityClientRecord r, int configChanges, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingIntents, PendingTransactionActions pendingActions, boolean startsNotResumed, Configuration overrideConfig, String reason) {
        Intent customIntent = r.activity.mIntent;
        if (!r.paused) {
            performPauseActivity(r, false, reason, (PendingTransactionActions) null);
        }
        if (!r.stopped) {
            callActivityOnStop(r, true, reason);
        }
        handleDestroyActivity(r.token, false, configChanges, true, reason);
        r.activity = null;
        r.window = null;
        r.hideForNow = false;
        r.nextIdle = null;
        if (pendingResults != null) {
            if (r.pendingResults == null) {
                r.pendingResults = pendingResults;
            } else {
                r.pendingResults.addAll(pendingResults);
            }
        }
        if (pendingIntents != null) {
            if (r.pendingIntents == null) {
                r.pendingIntents = pendingIntents;
            } else {
                r.pendingIntents.addAll(pendingIntents);
            }
        }
        r.startsNotResumed = startsNotResumed;
        r.overrideConfig = overrideConfig;
        handleLaunchActivity(r, pendingActions, customIntent);
    }

    @Override // android.app.ClientTransactionHandler
    public void reportRelaunch(IBinder token, PendingTransactionActions pendingActions) {
        try {
            ActivityTaskManager.getService().activityRelaunched(token);
            ActivityClientRecord r = this.mActivities.get(token);
            if (pendingActions.shouldReportRelaunchToWindowManager() && r != null && r.window != null) {
                r.window.reportActivityRelaunched();
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void callActivityOnSaveInstanceState(ActivityClientRecord r) {
        r.state = new Bundle();
        r.state.setAllowFds(false);
        if (r.isPersistable()) {
            r.persistentState = new PersistableBundle();
            this.mInstrumentation.callActivityOnSaveInstanceState(r.activity, r.state, r.persistentState);
            return;
        }
        this.mInstrumentation.callActivityOnSaveInstanceState(r.activity, r.state);
    }

    ArrayList<ComponentCallbacks2> collectComponentCallbacks(boolean allActivities, Configuration newConfig) {
        ArrayList<ComponentCallbacks2> callbacks = new ArrayList<>();
        synchronized (this.mResourcesManager) {
            int NAPP = this.mAllApplications.size();
            for (int i = 0; i < NAPP; i++) {
                callbacks.add(this.mAllApplications.get(i));
            }
            int NACT = this.mActivities.size();
            for (int i2 = 0; i2 < NACT; i2++) {
                ActivityClientRecord ar = this.mActivities.valueAt(i2);
                Activity a = ar.activity;
                if (a != null) {
                    Configuration thisConfig = applyConfigCompatMainThread(this.mCurDefaultDisplayDpi, newConfig, ar.packageInfo.getCompatibilityInfo());
                    if (!ar.activity.mFinished && (allActivities || !ar.paused)) {
                        callbacks.add(a);
                    } else if (thisConfig != null) {
                        if (DEBUG_CONFIGURATION) {
                            Slog.v(TAG, "Setting activity " + ar.activityInfo.name + " newConfig=" + thisConfig);
                        }
                        ar.newConfig = thisConfig;
                    }
                }
            }
            int NSVC = this.mServices.size();
            for (int i3 = 0; i3 < NSVC; i3++) {
                callbacks.add(this.mServices.valueAt(i3));
            }
        }
        synchronized (this.mProviderMap) {
            int NPRV = this.mLocalProviders.size();
            for (int i4 = 0; i4 < NPRV; i4++) {
                callbacks.add(this.mLocalProviders.valueAt(i4).mLocalProvider);
            }
        }
        return callbacks;
    }

    private void performConfigurationChangedForActivity(ActivityClientRecord r, Configuration newBaseConfig) {
        performConfigurationChangedForActivity(r, newBaseConfig, r.activity.getDisplayId(), false);
    }

    private Configuration performConfigurationChangedForActivity(ActivityClientRecord r, Configuration newBaseConfig, int displayId, boolean movedToDifferentDisplay) {
        r.tmpConfig.setTo(newBaseConfig);
        if (r.overrideConfig != null) {
            r.tmpConfig.updateFrom(r.overrideConfig);
        }
        Configuration reportedConfig = performActivityConfigurationChanged(r.activity, r.tmpConfig, r.overrideConfig, displayId, movedToDifferentDisplay);
        freeTextLayoutCachesIfNeeded(r.activity.mCurrentConfig.diff(r.tmpConfig));
        return reportedConfig;
    }

    private static Configuration createNewConfigAndUpdateIfNotNull(Configuration base, Configuration override) {
        if (override == null) {
            return base;
        }
        Configuration newConfig = new Configuration(base);
        newConfig.updateFrom(override);
        return newConfig;
    }

    private void performConfigurationChanged(ComponentCallbacks2 cb, Configuration newConfig) {
        Configuration contextThemeWrapperOverrideConfig = null;
        if (cb instanceof ContextThemeWrapper) {
            ContextThemeWrapper contextThemeWrapper = (ContextThemeWrapper) cb;
            contextThemeWrapperOverrideConfig = contextThemeWrapper.getOverrideConfiguration();
        }
        Configuration configToReport = createNewConfigAndUpdateIfNotNull(newConfig, contextThemeWrapperOverrideConfig);
        cb.onConfigurationChanged(configToReport);
    }

    private Configuration performActivityConfigurationChanged(Activity activity, Configuration newConfig, Configuration amOverrideConfig, int displayId, boolean movedToDifferentDisplay) {
        if (activity == null) {
            throw new IllegalArgumentException("No activity provided.");
        }
        IBinder activityToken = activity.getActivityToken();
        if (activityToken == null) {
            throw new IllegalArgumentException("Activity token not set. Is the activity attached?");
        }
        boolean shouldChangeConfig = false;
        if (activity.mCurrentConfig == null) {
            shouldChangeConfig = true;
        } else {
            int diff = activity.mCurrentConfig.diffPublicOnly(newConfig);
            if ((diff != 0 || !this.mResourcesManager.isSameResourcesOverrideConfig(activityToken, amOverrideConfig)) && (!this.mUpdatingSystemConfig || ((~activity.mActivityInfo.getRealConfigChanged()) & diff) == 0)) {
                shouldChangeConfig = true;
            }
            if (!shouldChangeConfig) {
                int privateFlags = activity.getIntent().getPrivateFlags();
                boolean isSecondaryHome = (privateFlags & 268435456) == 268435456;
                Slog.v(TAG, "Not notify Activity Configuration isSecondaryHome " + isSecondaryHome + " diff " + Integer.toHexString(diff) + " realChange " + Integer.toHexString(activity.mActivityInfo.getRealConfigChanged()));
                if (isSecondaryHome) {
                    shouldChangeConfig = true;
                }
            }
        }
        if (!shouldChangeConfig && !movedToDifferentDisplay) {
            return null;
        }
        Configuration contextThemeWrapperOverrideConfig = activity.getOverrideConfiguration();
        if (contextThemeWrapperOverrideConfig != null) {
            contextThemeWrapperOverrideConfig.uiMode &= 15;
        }
        Configuration finalOverrideConfig = createNewConfigAndUpdateIfNotNull(amOverrideConfig, contextThemeWrapperOverrideConfig);
        this.mResourcesManager.updateResourcesForActivity(activityToken, finalOverrideConfig, displayId, movedToDifferentDisplay);
        activity.mConfigChangeFlags = 0;
        activity.mCurrentConfig = new Configuration(newConfig);
        Configuration configToReport = createNewConfigAndUpdateIfNotNull(newConfig, contextThemeWrapperOverrideConfig);
        if (movedToDifferentDisplay) {
            activity.dispatchMovedToDisplay(displayId, configToReport);
        }
        if (shouldChangeConfig) {
            activity.mCalled = false;
            activity.onConfigurationChanged(configToReport);
            if (!activity.mCalled) {
                throw new SuperNotCalledException("Activity " + activity.getLocalClassName() + " did not call through to super.onConfigurationChanged()");
            }
        }
        return configToReport;
    }

    public final void applyConfigurationToResources(Configuration config) {
        synchronized (this.mResourcesManager) {
            this.mResourcesManager.applyConfigurationToResourcesLocked(config, null);
        }
    }

    final Configuration applyCompatConfiguration(int displayDensity) {
        Configuration config = this.mConfiguration;
        if (this.mCompatConfiguration == null) {
            this.mCompatConfiguration = new Configuration();
        }
        this.mCompatConfiguration.setTo(this.mConfiguration);
        if (this.mResourcesManager.applyCompatConfigurationLocked(displayDensity, this.mCompatConfiguration)) {
            Configuration config2 = this.mCompatConfiguration;
            return config2;
        }
        return config;
    }

    @Override // android.app.ClientTransactionHandler
    public void handleConfigurationChanged(Configuration config) {
        Trace.traceBegin(64L, "configChanged");
        this.mCurDefaultDisplayDpi = config.densityDpi;
        this.mUpdatingSystemConfig = true;
        try {
            handleConfigurationChanged(config, null);
            this.mUpdatingSystemConfig = false;
            Trace.traceEnd(64L);
        } catch (Throwable th) {
            this.mUpdatingSystemConfig = false;
            throw th;
        }
    }

    private void handleConfigurationChanged(Configuration config, CompatibilityInfo compat) {
        getApplication().getPackageName();
        Configuration config2 = xpActivityManager.getOverrideConfiguration(config, null);
        Resources.Theme systemTheme = getSystemContext().getTheme();
        Resources.Theme systemUiTheme = getSystemUiContext().getTheme();
        synchronized (this.mResourcesManager) {
            if (this.mPendingConfiguration != null) {
                if (!this.mPendingConfiguration.isOtherSeqNewer(config2)) {
                    config2 = this.mPendingConfiguration;
                    this.mCurDefaultDisplayDpi = config2.densityDpi;
                    updateDefaultDensity();
                }
                this.mPendingConfiguration = null;
            }
            if (config2 == null) {
                return;
            }
            boolean equivalent = this.mConfiguration != null && this.mConfiguration.diffPublicOnly(config2) == 0;
            if (DEBUG_CONFIGURATION) {
                Slog.v(TAG, "Handle configuration changed: " + config2);
            }
            this.mResourcesManager.applyConfigurationToResourcesLocked(config2, compat);
            updateLocaleListFromAppContext(this.mInitialApplication.getApplicationContext(), this.mResourcesManager.getConfiguration().getLocales());
            if (this.mConfiguration == null) {
                this.mConfiguration = new Configuration();
            }
            if (this.mConfiguration.isOtherSeqNewer(config2) || compat != null) {
                int configDiff = this.mConfiguration.updateFrom(config2);
                Configuration config3 = applyCompatConfiguration(this.mCurDefaultDisplayDpi);
                if ((systemTheme.getChangingConfigurations() & configDiff) != 0) {
                    systemTheme.rebase();
                }
                if ((systemUiTheme.getChangingConfigurations() & configDiff) != 0) {
                    systemUiTheme.rebase();
                }
                ArrayList<ComponentCallbacks2> callbacks = collectComponentCallbacks(false, config3);
                freeTextLayoutCachesIfNeeded(configDiff);
                if (callbacks != null) {
                    int N = callbacks.size();
                    for (int i = 0; i < N; i++) {
                        ComponentCallbacks2 cb = callbacks.get(i);
                        if (cb instanceof Activity) {
                            Activity a = (Activity) cb;
                            performConfigurationChangedForActivity(this.mActivities.get(a.getActivityToken()), config3);
                        } else if (!equivalent) {
                            performConfigurationChanged(cb, config3);
                        }
                    }
                }
            }
        }
    }

    public void handleSystemApplicationInfoChanged(ApplicationInfo ai) {
        Preconditions.checkState(this.mSystemThread, "Must only be called in the system process");
        handleApplicationInfoChanged(ai);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void handleApplicationInfoChanged(ApplicationInfo ai) {
        LoadedApk apk;
        LoadedApk resApk;
        synchronized (this.mResourcesManager) {
            WeakReference<LoadedApk> ref = this.mPackages.get(ai.packageName);
            apk = ref != null ? ref.get() : null;
            WeakReference<LoadedApk> ref2 = this.mResourcePackages.get(ai.packageName);
            resApk = ref2 != null ? ref2.get() : null;
        }
        String[] oldResDirs = new String[2];
        if (apk != null) {
            oldResDirs[0] = apk.getResDir();
            ArrayList<String> oldPaths = new ArrayList<>();
            LoadedApk.makePaths(this, apk.getApplicationInfo(), oldPaths);
            apk.updateApplicationInfo(ai, oldPaths);
        }
        if (resApk != null) {
            oldResDirs[1] = resApk.getResDir();
            ArrayList<String> oldPaths2 = new ArrayList<>();
            LoadedApk.makePaths(this, resApk.getApplicationInfo(), oldPaths2);
            resApk.updateApplicationInfo(ai, oldPaths2);
        }
        synchronized (this.mResourcesManager) {
            this.mResourcesManager.applyNewResourceDirsLocked(ai, oldResDirs);
        }
        ApplicationPackageManager.configurationChanged();
        Configuration newConfig = new Configuration();
        Configuration configuration = this.mConfiguration;
        newConfig.assetsSeq = (configuration != null ? configuration.assetsSeq : 0) + 1;
        handleConfigurationChanged(newConfig, null);
        relaunchAllActivities(true);
    }

    static void freeTextLayoutCachesIfNeeded(int configDiff) {
        if (configDiff != 0) {
            boolean hasLocaleConfigChange = (configDiff & 4) != 0;
            if (hasLocaleConfigChange) {
                Canvas.freeTextLayoutCaches();
                if (DEBUG_CONFIGURATION) {
                    Slog.v(TAG, "Cleared TextLayout Caches");
                }
            }
        }
    }

    @Override // android.app.ClientTransactionHandler
    public void updatePendingActivityConfiguration(IBinder activityToken, Configuration overrideConfig) {
        ActivityClientRecord r;
        synchronized (this.mResourcesManager) {
            r = this.mActivities.get(activityToken);
        }
        if (r == null) {
            if (DEBUG_CONFIGURATION) {
                Slog.w(TAG, "Not found target activity to update its pending config.");
                return;
            }
            return;
        }
        synchronized (r) {
            r.mPendingOverrideConfig = overrideConfig;
        }
    }

    @Override // android.app.ClientTransactionHandler
    public void handleActivityConfigurationChanged(IBinder activityToken, Configuration overrideConfig, int displayId) {
        ViewRootImpl viewRoot;
        ActivityClientRecord r = this.mActivities.get(activityToken);
        if (r == null || r.activity == null) {
            if (DEBUG_CONFIGURATION) {
                Slog.w(TAG, "Not found target activity to report to: " + r);
                return;
            }
            return;
        }
        boolean movedToDifferentDisplay = (displayId == -1 || displayId == r.activity.getDisplayId()) ? false : true;
        synchronized (r) {
            if (r.mPendingOverrideConfig != null && !r.mPendingOverrideConfig.isOtherSeqNewer(overrideConfig)) {
                overrideConfig = r.mPendingOverrideConfig;
            }
            viewRoot = null;
            r.mPendingOverrideConfig = null;
        }
        if (r.overrideConfig != null && !r.overrideConfig.isOtherSeqNewer(overrideConfig) && !movedToDifferentDisplay) {
            if (DEBUG_CONFIGURATION) {
                Slog.v(TAG, "Activity already handled newer configuration so drop this transaction. overrideConfig=" + overrideConfig + " r.overrideConfig=" + r.overrideConfig);
            }
        } else if (SharedDisplayManager.skipConfigurationChanged(r.overrideConfig, overrideConfig)) {
        } else {
            r.overrideConfig = overrideConfig;
            if (r.activity.mDecor != null) {
                viewRoot = r.activity.mDecor.getViewRootImpl();
            }
            if (movedToDifferentDisplay) {
                if (DEBUG_CONFIGURATION) {
                    Slog.v(TAG, "Handle activity moved to display, activity:" + r.activityInfo.name + ", displayId=" + displayId + ", config=" + overrideConfig);
                }
                Configuration reportedConfig = performConfigurationChangedForActivity(r, this.mCompatConfiguration, displayId, true);
                if (viewRoot != null) {
                    viewRoot.onMovedToDisplay(displayId, reportedConfig);
                }
            } else {
                if (DEBUG_CONFIGURATION) {
                    Slog.v(TAG, "Handle activity config changed: " + r.activityInfo.name + ", config=" + overrideConfig);
                }
                performConfigurationChangedForActivity(r, this.mCompatConfiguration);
            }
            if (viewRoot != null) {
                viewRoot.updateConfiguration(displayId);
            }
            this.mSomeActivitiesChanged = true;
        }
    }

    final void handleProfilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) {
        try {
            if (start) {
                try {
                    this.mProfiler.setProfiler(profilerInfo);
                    this.mProfiler.startProfiling();
                } catch (RuntimeException e) {
                    Slog.w(TAG, "Profiling failed on path " + profilerInfo.profileFile + " -- can the process access this path?");
                }
                return;
            }
            this.mProfiler.stopProfiling();
        } finally {
            profilerInfo.closeFd();
        }
    }

    public void stopProfiling() {
        Profiler profiler = this.mProfiler;
        if (profiler != null) {
            profiler.stopProfiling();
        }
    }

    static void handleDumpHeap(DumpHeapData dhd) {
        if (dhd.runGc) {
            System.gc();
            System.runFinalization();
            System.gc();
        }
        try {
            ParcelFileDescriptor fd = dhd.fd;
            if (dhd.managed) {
                Debug.dumpHprofData(dhd.path, fd.getFileDescriptor());
            } else if (dhd.mallocInfo) {
                Debug.dumpNativeMallocInfo(fd.getFileDescriptor());
            } else {
                Debug.dumpNativeHeap(fd.getFileDescriptor());
            }
            if (fd != null) {
                fd.close();
            }
        } catch (IOException e) {
            if (!dhd.managed) {
                Slog.w(TAG, "Failed to dump heap", e);
            } else {
                Slog.w(TAG, "Managed heap dump failed on path " + dhd.path + " -- can the process access this path?", e);
            }
        } catch (RuntimeException e2) {
            Slog.wtf(TAG, "Heap dumper threw a runtime exception", e2);
        }
        try {
            ActivityManager.getService().dumpHeapFinished(dhd.path);
            if (dhd.finishCallback != null) {
                dhd.finishCallback.sendResult(null);
            }
        } catch (RemoteException e3) {
            throw e3.rethrowFromSystemServer();
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:60:0x00e8 -> B:61:0x00e9). Please submit an issue!!! */
    final void handleDispatchPackageBroadcast(int cmd, String[] packages) {
        boolean hasPkgInfo;
        boolean hasPkgInfo2 = false;
        int i = 1;
        if (cmd == 0 || cmd == 2) {
            boolean killApp = cmd == 0;
            if (packages != null) {
                synchronized (this.mResourcesManager) {
                    for (int i2 = packages.length - 1; i2 >= 0; i2--) {
                        if (!hasPkgInfo2) {
                            WeakReference<LoadedApk> ref = this.mPackages.get(packages[i2]);
                            if (ref != null && ref.get() != null) {
                                hasPkgInfo2 = true;
                            } else {
                                WeakReference<LoadedApk> ref2 = this.mResourcePackages.get(packages[i2]);
                                if (ref2 != null && ref2.get() != null) {
                                    hasPkgInfo2 = true;
                                }
                            }
                        }
                        if (killApp) {
                            this.mPackages.remove(packages[i2]);
                            this.mResourcePackages.remove(packages[i2]);
                        }
                    }
                }
            }
        } else if (cmd == 3 && packages != null) {
            List<String> packagesHandled = new ArrayList<>();
            synchronized (this.mResourcesManager) {
                try {
                    boolean hasPkgInfo3 = false;
                    int i3 = packages.length - 1;
                    while (i3 >= 0) {
                        try {
                            String packageName = packages[i3];
                            WeakReference<LoadedApk> ref3 = this.mPackages.get(packageName);
                            LoadedApk pkgInfo = ref3 != null ? ref3.get() : null;
                            if (pkgInfo != null) {
                                hasPkgInfo = true;
                            } else {
                                WeakReference<LoadedApk> ref4 = this.mResourcePackages.get(packageName);
                                pkgInfo = ref4 != null ? ref4.get() : null;
                                if (pkgInfo == null) {
                                    hasPkgInfo = hasPkgInfo3;
                                } else {
                                    hasPkgInfo = true;
                                }
                            }
                            if (pkgInfo != null) {
                                try {
                                    packagesHandled.add(packageName);
                                    try {
                                        ApplicationInfo aInfo = sPackageManager.getApplicationInfo(packageName, 1024, UserHandle.myUserId());
                                        if (this.mActivities.size() > 0) {
                                            for (ActivityClientRecord ar : this.mActivities.values()) {
                                                if (ar.activityInfo.applicationInfo.packageName.equals(packageName)) {
                                                    ar.activityInfo.applicationInfo = aInfo;
                                                    ar.packageInfo = pkgInfo;
                                                }
                                            }
                                        }
                                        String[] oldResDirs = new String[i];
                                        oldResDirs[0] = pkgInfo.getResDir();
                                        ArrayList<String> oldPaths = new ArrayList<>();
                                        LoadedApk.makePaths(this, pkgInfo.getApplicationInfo(), oldPaths);
                                        pkgInfo.updateApplicationInfo(aInfo, oldPaths);
                                        synchronized (this.mResourcesManager) {
                                            this.mResourcesManager.applyNewResourceDirsLocked(aInfo, oldResDirs);
                                        }
                                    } catch (RemoteException e) {
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            }
                            i3--;
                            hasPkgInfo3 = hasPkgInfo;
                            i = 1;
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    }
                    try {
                        getPackageManager().notifyPackagesReplacedReceived((String[]) packagesHandled.toArray(new String[0]));
                    } catch (RemoteException e2) {
                    }
                    hasPkgInfo2 = hasPkgInfo3;
                } catch (Throwable th3) {
                    th = th3;
                }
            }
        }
        ApplicationPackageManager.handlePackageBroadcast(cmd, packages, hasPkgInfo2);
    }

    final void handleLowMemory() {
        ArrayList<ComponentCallbacks2> callbacks = collectComponentCallbacks(true, null);
        int N = callbacks.size();
        for (int i = 0; i < N; i++) {
            callbacks.get(i).onLowMemory();
        }
        int i2 = Process.myUid();
        if (i2 != 1000) {
            int sqliteReleased = SQLiteDatabase.releaseMemory();
            EventLog.writeEvent((int) SQLITE_MEM_RELEASED_EVENT_LOG_TAG, sqliteReleased);
        }
        Canvas.freeCaches();
        Canvas.freeTextLayoutCaches();
        BinderInternal.forceGc("mem");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTrimMemory(int level) {
        Trace.traceBegin(64L, "trimMemory");
        ArrayList<ComponentCallbacks2> callbacks = collectComponentCallbacks(true, null);
        int N = callbacks.size();
        for (int i = 0; i < N; i++) {
            callbacks.get(i).onTrimMemory(level);
        }
        WindowManagerGlobal.getInstance().trimMemory(level);
        Trace.traceEnd(64L);
        if (SystemProperties.getInt("debug.am.run_gc_trim_level", Integer.MAX_VALUE) <= level) {
            unscheduleGcIdler();
            doGcIfNeeded("tm");
        }
        if (SystemProperties.getInt("debug.am.run_mallopt_trim_level", Integer.MAX_VALUE) <= level) {
            unschedulePurgeIdler();
            purgePendingResources();
        }
    }

    private void setupGraphicsSupport(Context context) {
        Trace.traceBegin(64L, "setupGraphicsSupport");
        if (!"android".equals(context.getPackageName())) {
            File cacheDir = context.getCacheDir();
            if (cacheDir == null) {
                Log.v(TAG, "Unable to initialize \"java.io.tmpdir\" property due to missing cache directory");
            } else {
                System.setProperty("java.io.tmpdir", cacheDir.getAbsolutePath());
            }
            Context deviceContext = context.createDeviceProtectedStorageContext();
            File codeCacheDir = deviceContext.getCodeCacheDir();
            if (codeCacheDir == null) {
                Log.w(TAG, "Unable to use shader/script cache: missing code-cache directory");
            } else {
                try {
                    int uid = Process.myUid();
                    String[] packages = getPackageManager().getPackagesForUid(uid);
                    if (packages != null) {
                        HardwareRenderer.setupDiskCache(codeCacheDir);
                        RenderScriptCacheDir.setupDiskCache(codeCacheDir);
                    }
                } catch (RemoteException e) {
                    Trace.traceEnd(64L);
                    throw e.rethrowFromSystemServer();
                }
            }
        }
        GraphicsEnvironment.getInstance().setup(context, this.mCoreSettings);
        Trace.traceEnd(64L);
    }

    private void updateDefaultDensity() {
        int densityDpi = this.mCurDefaultDisplayDpi;
        if (!this.mDensityCompatMode && densityDpi != 0 && densityDpi != DisplayMetrics.DENSITY_DEVICE) {
            DisplayMetrics.DENSITY_DEVICE = densityDpi;
            Bitmap.setDefaultDensity(densityDpi);
        }
    }

    private String getInstrumentationLibrary(ApplicationInfo appInfo, InstrumentationInfo insInfo) {
        if (appInfo.primaryCpuAbi != null && appInfo.secondaryCpuAbi != null && appInfo.secondaryCpuAbi.equals(insInfo.secondaryCpuAbi)) {
            String secondaryIsa = VMRuntime.getInstructionSet(appInfo.secondaryCpuAbi);
            String secondaryDexCodeIsa = SystemProperties.get("ro.dalvik.vm.isa." + secondaryIsa);
            String secondaryIsa2 = secondaryDexCodeIsa.isEmpty() ? secondaryIsa : secondaryDexCodeIsa;
            String runtimeIsa = VMRuntime.getRuntime().vmInstructionSet();
            if (runtimeIsa.equals(secondaryIsa2)) {
                return insInfo.secondaryNativeLibraryDir;
            }
        }
        return insInfo.nativeLibraryDir;
    }

    private void updateLocaleListFromAppContext(Context context, LocaleList newLocaleList) {
        Locale bestLocale = context.getResources().getConfiguration().getLocales().get(0);
        int newLocaleListSize = newLocaleList.size();
        for (int i = 0; i < newLocaleListSize; i++) {
            if (bestLocale.equals(newLocaleList.get(i))) {
                LocaleList.setDefault(newLocaleList, i);
                return;
            }
        }
        LocaleList.setDefault(new LocaleList(bestLocale, newLocaleList));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:16:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x00e2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00c1  */
    @android.annotation.UnsupportedAppUsage
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void handleBindApplication(android.app.ActivityThread.AppBindData r26) {
        /*
            Method dump skipped, instructions count: 1299
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.handleBindApplication(android.app.ActivityThread$AppBindData):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void finishInstrumentation(int resultCode, Bundle results) {
        IActivityManager am = ActivityManager.getService();
        if (this.mProfiler.profileFile != null && this.mProfiler.handlingProfiling && this.mProfiler.profileFd == null) {
            Debug.stopMethodTracing();
        }
        try {
            am.finishInstrumentation(this.mAppThread, resultCode, results);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    private void installContentProviders(Context context, List<ProviderInfo> providers) {
        ArrayList<ContentProviderHolder> results = new ArrayList<>();
        for (ProviderInfo cpi : providers) {
            if (DEBUG_PROVIDER) {
                StringBuilder buf = new StringBuilder(128);
                buf.append("Pub ");
                buf.append(cpi.authority);
                buf.append(": ");
                buf.append(cpi.name);
                Log.i(TAG, buf.toString());
            }
            ContentProviderHolder cph = installProvider(context, null, cpi, false, true, true);
            if (cph != null) {
                cph.noReleaseNeeded = true;
                results.add(cph);
            }
        }
        try {
            ActivityManager.getService().publishContentProviders(getApplicationThread(), results);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public final IContentProvider acquireProvider(Context c, String auth, int userId, boolean stable) {
        ContentProviderHolder holder;
        IContentProvider provider = acquireExistingProvider(c, auth, userId, stable);
        if (provider != null) {
            return provider;
        }
        try {
            synchronized (getGetProviderLock(auth, userId)) {
                holder = ActivityManager.getService().getContentProvider(getApplicationThread(), c.getOpPackageName(), auth, userId, stable);
            }
            if (holder == null) {
                Slog.e(TAG, "Failed to find provider info for " + auth);
                return null;
            }
            return installProvider(c, holder, holder.info, true, holder.noReleaseNeeded, stable).provider;
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private Object getGetProviderLock(String auth, int userId) {
        Object lock;
        ProviderKey key = new ProviderKey(auth, userId);
        synchronized (this.mGetProviderLocks) {
            lock = this.mGetProviderLocks.get(key);
            if (lock == null) {
                lock = key;
                this.mGetProviderLocks.put(key, lock);
            }
        }
        return lock;
    }

    private final void incProviderRefLocked(ProviderRefCount prc, boolean stable) {
        int unstableDelta;
        if (stable) {
            prc.stableCount++;
            if (prc.stableCount == 1) {
                if (prc.removePending) {
                    unstableDelta = -1;
                    if (DEBUG_PROVIDER) {
                        Slog.v(TAG, "incProviderRef: stable snatched provider from the jaws of death");
                    }
                    prc.removePending = false;
                    this.mH.removeMessages(131, prc);
                } else {
                    unstableDelta = 0;
                }
                try {
                    if (DEBUG_PROVIDER) {
                        Slog.v(TAG, "incProviderRef Now stable - " + prc.holder.info.name + ": unstableDelta=" + unstableDelta);
                    }
                    ActivityManager.getService().refContentProvider(prc.holder.connection, 1, unstableDelta);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            return;
        }
        prc.unstableCount++;
        if (prc.unstableCount == 1) {
            if (prc.removePending) {
                if (DEBUG_PROVIDER) {
                    Slog.v(TAG, "incProviderRef: unstable snatched provider from the jaws of death");
                }
                prc.removePending = false;
                this.mH.removeMessages(131, prc);
                return;
            }
            try {
                if (DEBUG_PROVIDER) {
                    Slog.v(TAG, "incProviderRef: Now unstable - " + prc.holder.info.name);
                }
                ActivityManager.getService().refContentProvider(prc.holder.connection, 0, 1);
            } catch (RemoteException e2) {
            }
        }
    }

    @UnsupportedAppUsage
    public final IContentProvider acquireExistingProvider(Context c, String auth, int userId, boolean stable) {
        synchronized (this.mProviderMap) {
            ProviderKey key = new ProviderKey(auth, userId);
            ProviderClientRecord pr = this.mProviderMap.get(key);
            if (pr == null) {
                return null;
            }
            IContentProvider provider = pr.mProvider;
            IBinder jBinder = provider.asBinder();
            if (!jBinder.isBinderAlive()) {
                Log.i(TAG, "Acquiring provider " + auth + " for user " + userId + ": existing object's process dead");
                handleUnstableProviderDiedLocked(jBinder, true);
                return null;
            }
            ProviderRefCount prc = this.mProviderRefCountMap.get(jBinder);
            if (prc != null) {
                incProviderRefLocked(prc, stable);
            }
            return provider;
        }
    }

    @UnsupportedAppUsage
    public final boolean releaseProvider(IContentProvider provider, boolean stable) {
        if (provider == null) {
            return false;
        }
        IBinder jBinder = provider.asBinder();
        synchronized (this.mProviderMap) {
            ProviderRefCount prc = this.mProviderRefCountMap.get(jBinder);
            if (prc == null) {
                return false;
            }
            boolean lastRef = false;
            if (stable) {
                if (prc.stableCount == 0) {
                    if (DEBUG_PROVIDER) {
                        Slog.v(TAG, "releaseProvider: stable ref count already 0, how?");
                    }
                    return false;
                }
                prc.stableCount--;
                if (prc.stableCount == 0) {
                    lastRef = prc.unstableCount == 0;
                    try {
                        if (DEBUG_PROVIDER) {
                            Slog.v(TAG, "releaseProvider: No longer stable w/lastRef=" + lastRef + " - " + prc.holder.info.name);
                        }
                        ActivityManager.getService().refContentProvider(prc.holder.connection, -1, lastRef ? 1 : 0);
                    } catch (RemoteException e) {
                    }
                }
            } else if (prc.unstableCount == 0) {
                if (DEBUG_PROVIDER) {
                    Slog.v(TAG, "releaseProvider: unstable ref count already 0, how?");
                }
                return false;
            } else {
                prc.unstableCount--;
                if (prc.unstableCount == 0) {
                    lastRef = prc.stableCount == 0;
                    if (!lastRef) {
                        try {
                            if (DEBUG_PROVIDER) {
                                Slog.v(TAG, "releaseProvider: No longer unstable - " + prc.holder.info.name);
                            }
                            ActivityManager.getService().refContentProvider(prc.holder.connection, 0, -1);
                        } catch (RemoteException e2) {
                        }
                    }
                }
            }
            if (lastRef) {
                if (!prc.removePending) {
                    if (DEBUG_PROVIDER) {
                        Slog.v(TAG, "releaseProvider: Enqueueing pending removal - " + prc.holder.info.name);
                    }
                    prc.removePending = true;
                    Message msg = this.mH.obtainMessage(131, prc);
                    this.mH.sendMessageDelayed(msg, 1000L);
                } else {
                    Slog.w(TAG, "Duplicate remove pending of provider " + prc.holder.info.name);
                }
            }
            return true;
        }
    }

    final void completeRemoveProvider(ProviderRefCount prc) {
        synchronized (this.mProviderMap) {
            if (!prc.removePending) {
                if (DEBUG_PROVIDER) {
                    Slog.v(TAG, "completeRemoveProvider: lost the race, provider still in use");
                }
                return;
            }
            prc.removePending = false;
            IBinder jBinder = prc.holder.provider.asBinder();
            ProviderRefCount existingPrc = this.mProviderRefCountMap.get(jBinder);
            if (existingPrc == prc) {
                this.mProviderRefCountMap.remove(jBinder);
            }
            for (int i = this.mProviderMap.size() - 1; i >= 0; i--) {
                ProviderClientRecord pr = this.mProviderMap.valueAt(i);
                IBinder myBinder = pr.mProvider.asBinder();
                if (myBinder == jBinder) {
                    this.mProviderMap.removeAt(i);
                }
            }
            try {
                if (DEBUG_PROVIDER) {
                    Slog.v(TAG, "removeProvider: Invoking ActivityManagerService.removeContentProvider(" + prc.holder.info.name + ")");
                }
                ActivityManager.getService().removeContentProvider(prc.holder.connection, false);
            } catch (RemoteException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public final void handleUnstableProviderDied(IBinder provider, boolean fromClient) {
        synchronized (this.mProviderMap) {
            handleUnstableProviderDiedLocked(provider, fromClient);
        }
    }

    final void handleUnstableProviderDiedLocked(IBinder provider, boolean fromClient) {
        ProviderRefCount prc = this.mProviderRefCountMap.get(provider);
        if (prc != null) {
            if (DEBUG_PROVIDER) {
                Slog.v(TAG, "Cleaning up dead provider " + provider + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + prc.holder.info.name);
            }
            this.mProviderRefCountMap.remove(provider);
            for (int i = this.mProviderMap.size() - 1; i >= 0; i--) {
                ProviderClientRecord pr = this.mProviderMap.valueAt(i);
                if (pr != null && pr.mProvider.asBinder() == provider) {
                    Slog.i(TAG, "Removing dead content provider:" + pr.mProvider.toString());
                    this.mProviderMap.removeAt(i);
                }
            }
            if (fromClient) {
                try {
                    ActivityManager.getService().unstableProviderDied(prc.holder.connection);
                } catch (RemoteException e) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void appNotRespondingViaProvider(IBinder provider) {
        synchronized (this.mProviderMap) {
            ProviderRefCount prc = this.mProviderRefCountMap.get(provider);
            if (prc != null) {
                try {
                    ActivityManager.getService().appNotRespondingViaProvider(prc.holder.connection);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    private ProviderClientRecord installProviderAuthoritiesLocked(IContentProvider provider, ContentProvider localProvider, ContentProviderHolder holder) {
        String[] auths = holder.info.authority.split(";");
        int userId = UserHandle.getUserId(holder.info.applicationInfo.uid);
        if (provider != null) {
            for (String auth : auths) {
                char c = 65535;
                switch (auth.hashCode()) {
                    case -845193793:
                        if (auth.equals(ContactsContract.AUTHORITY)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -456066902:
                        if (auth.equals(CalendarContract.AUTHORITY)) {
                            c = 4;
                            break;
                        }
                        break;
                    case -172298781:
                        if (auth.equals(CallLog.AUTHORITY)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 63943420:
                        if (auth.equals(CallLog.SHADOW_AUTHORITY)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 783201304:
                        if (auth.equals(DeviceConfig.NAMESPACE_TELEPHONY)) {
                            c = 6;
                            break;
                        }
                        break;
                    case 1312704747:
                        if (auth.equals(Downloads.Impl.AUTHORITY)) {
                            c = 5;
                            break;
                        }
                        break;
                    case 1995645513:
                        if (auth.equals(BlockedNumberContract.AUTHORITY)) {
                            c = 3;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        Binder.allowBlocking(provider.asBinder());
                        break;
                }
            }
        }
        ProviderClientRecord pcr = new ProviderClientRecord(auths, provider, localProvider, holder);
        for (String auth2 : auths) {
            ProviderKey key = new ProviderKey(auth2, userId);
            ProviderClientRecord existing = this.mProviderMap.get(key);
            if (existing != null) {
                Slog.w(TAG, "Content provider " + pcr.mHolder.info.name + " already published as " + auth2);
            } else {
                this.mProviderMap.put(key, pcr);
            }
        }
        return pcr;
    }

    @UnsupportedAppUsage
    private ContentProviderHolder installProvider(Context context, ContentProviderHolder holder, ProviderInfo info, boolean noisy, boolean noReleaseNeeded, boolean stable) {
        IContentProvider provider;
        ContentProvider localProvider;
        ProviderRefCount providerRefCount;
        ContentProviderHolder retHolder;
        if (holder == null || holder.provider == null) {
            if (DEBUG_PROVIDER || noisy) {
                Slog.d(TAG, "Loading provider " + info.authority + ": " + info.name);
            }
            Context c = null;
            ApplicationInfo ai = info.applicationInfo;
            if (context.getPackageName().equals(ai.packageName)) {
                c = context;
            } else {
                Application application = this.mInitialApplication;
                if (application == null || !application.getPackageName().equals(ai.packageName)) {
                    try {
                        try {
                            c = context.createPackageContext(ai.packageName, 1);
                        } catch (PackageManager.NameNotFoundException e) {
                        }
                    } catch (PackageManager.NameNotFoundException e2) {
                    }
                } else {
                    c = this.mInitialApplication;
                }
            }
            if (c == null) {
                Slog.w(TAG, "Unable to get context for package " + ai.packageName + " while loading content provider " + info.name);
                return null;
            }
            if (info.splitName != null) {
                try {
                    c = c.createContextForSplit(info.splitName);
                } catch (PackageManager.NameNotFoundException e3) {
                    throw new RuntimeException(e3);
                }
            }
            try {
                ClassLoader cl = c.getClassLoader();
                LoadedApk packageInfo = peekPackageInfo(ai.packageName, true);
                if (packageInfo == null) {
                    packageInfo = getSystemContext().mPackageInfo;
                }
                ContentProvider localProvider2 = packageInfo.getAppFactory().instantiateProvider(cl, info.name);
                provider = localProvider2.getIContentProvider();
                if (provider == null) {
                    Slog.e(TAG, "Failed to instantiate class " + info.name + " from sourceDir " + info.applicationInfo.sourceDir);
                    return null;
                }
                if (DEBUG_PROVIDER) {
                    Slog.v(TAG, "Instantiating local provider " + info.name);
                }
                localProvider2.attachInfo(c, info);
                localProvider = localProvider2;
            } catch (Exception e4) {
                if (this.mInstrumentation.onException(null, e4)) {
                    return null;
                }
                throw new RuntimeException("Unable to get provider " + info.name + ": " + e4.toString(), e4);
            }
        } else {
            IContentProvider provider2 = holder.provider;
            if (DEBUG_PROVIDER) {
                Slog.v(TAG, "Installing external provider " + info.authority + ": " + info.name);
            }
            provider = provider2;
            localProvider = null;
        }
        synchronized (this.mProviderMap) {
            try {
                try {
                    if (DEBUG_PROVIDER) {
                        Slog.v(TAG, "Checking to add " + provider + " / " + info.name);
                    }
                    IBinder jBinder = provider.asBinder();
                    if (localProvider != null) {
                        ComponentName cname = new ComponentName(info.packageName, info.name);
                        ProviderClientRecord pr = this.mLocalProvidersByName.get(cname);
                        if (pr != null) {
                            if (DEBUG_PROVIDER) {
                                Slog.v(TAG, "installProvider: lost the race, using existing local provider");
                            }
                            IContentProvider iContentProvider = pr.mProvider;
                        } else {
                            ContentProviderHolder holder2 = new ContentProviderHolder(info);
                            holder2.provider = provider;
                            holder2.noReleaseNeeded = true;
                            pr = installProviderAuthoritiesLocked(provider, localProvider, holder2);
                            this.mLocalProviders.put(jBinder, pr);
                            this.mLocalProvidersByName.put(cname, pr);
                        }
                        retHolder = pr.mHolder;
                    } else {
                        ProviderRefCount prc = this.mProviderRefCountMap.get(jBinder);
                        if (prc != null) {
                            if (DEBUG_PROVIDER) {
                                Slog.v(TAG, "installProvider: lost the race, updating ref count");
                            }
                            if (!noReleaseNeeded) {
                                incProviderRefLocked(prc, stable);
                                try {
                                    ActivityManager.getService().removeContentProvider(holder.connection, stable);
                                } catch (RemoteException e5) {
                                }
                            }
                        } else {
                            ProviderClientRecord client = installProviderAuthoritiesLocked(provider, localProvider, holder);
                            if (noReleaseNeeded) {
                                prc = new ProviderRefCount(holder, client, 1000, 1000);
                            } else {
                                if (stable) {
                                    providerRefCount = new ProviderRefCount(holder, client, 1, 0);
                                } else {
                                    providerRefCount = new ProviderRefCount(holder, client, 0, 1);
                                }
                                prc = providerRefCount;
                            }
                            this.mProviderRefCountMap.put(jBinder, prc);
                        }
                        retHolder = prc.holder;
                    }
                    return retHolder;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRunIsolatedEntryPoint(String entryPoint, String[] entryPointArgs) {
        try {
            Method main = Class.forName(entryPoint).getMethod("main", String[].class);
            main.invoke(null, entryPointArgs);
            System.exit(0);
        } catch (ReflectiveOperationException e) {
            throw new AndroidRuntimeException("runIsolatedEntryPoint failed", e);
        }
    }

    @UnsupportedAppUsage
    private void attach(boolean system, long startSeq) {
        sCurrentActivityThread = this;
        this.mSystemThread = system;
        if (!system) {
            DdmHandleAppName.setAppName("<pre-initialized>", UserHandle.myUserId());
            RuntimeInit.setApplicationObject(this.mAppThread.asBinder());
            IActivityManager mgr = ActivityManager.getService();
            try {
                mgr.attachApplication(this.mAppThread, startSeq);
                BinderInternal.addGcWatcher(new Runnable() { // from class: android.app.ActivityThread.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!ActivityThread.this.mSomeActivitiesChanged) {
                            return;
                        }
                        Runtime runtime = Runtime.getRuntime();
                        long dalvikMax = runtime.maxMemory();
                        long dalvikUsed = runtime.totalMemory() - runtime.freeMemory();
                        if (dalvikUsed > (3 * dalvikMax) / 4) {
                            ActivityThread.this.mSomeActivitiesChanged = false;
                            try {
                                ActivityTaskManager.getService().releaseSomeActivities(ActivityThread.this.mAppThread);
                            } catch (RemoteException e) {
                                throw e.rethrowFromSystemServer();
                            }
                        }
                    }
                });
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        } else {
            DdmHandleAppName.setAppName("system_process", UserHandle.myUserId());
            try {
                this.mInstrumentation = new Instrumentation();
                this.mInstrumentation.basicInit(this);
                ContextImpl context = ContextImpl.createAppContext(this, getSystemContext().mPackageInfo);
                this.mInitialApplication = context.mPackageInfo.makeApplication(true, null);
                this.mInitialApplication.onCreate();
            } catch (Exception e) {
                throw new RuntimeException("Unable to instantiate Application():" + e.toString(), e);
            }
        }
        ViewRootImpl.ConfigChangedCallback configChangedCallback = new ViewRootImpl.ConfigChangedCallback() { // from class: android.app.-$$Lambda$ActivityThread$Wg40iAoNYFxps_KmrqtgptTB054
            @Override // android.view.ViewRootImpl.ConfigChangedCallback
            public final void onConfigurationChanged(Configuration configuration) {
                ActivityThread.this.lambda$attach$1$ActivityThread(configuration);
            }
        };
        ViewRootImpl.addConfigCallback(configChangedCallback);
    }

    public /* synthetic */ void lambda$attach$1$ActivityThread(Configuration globalConfig) {
        synchronized (this.mResourcesManager) {
            if (this.mResourcesManager.applyConfigurationToResourcesLocked(globalConfig, null)) {
                updateLocaleListFromAppContext(this.mInitialApplication.getApplicationContext(), this.mResourcesManager.getConfiguration().getLocales());
                if (this.mPendingConfiguration == null || this.mPendingConfiguration.isOtherSeqNewer(globalConfig)) {
                    this.mPendingConfiguration = globalConfig;
                    sendMessage(118, globalConfig);
                }
            }
        }
    }

    @UnsupportedAppUsage
    public static ActivityThread systemMain() {
        if (!ActivityManager.isHighEndGfx()) {
            ThreadedRenderer.disable(true);
        } else {
            ThreadedRenderer.enableForegroundTrimming();
        }
        ActivityThread thread = new ActivityThread();
        thread.attach(true, 0L);
        return thread;
    }

    public static void updateHttpProxy(Context context) {
        ConnectivityManager cm = ConnectivityManager.from(context);
        Proxy.setHttpProxySystemProperty(cm.getDefaultProxy());
    }

    @UnsupportedAppUsage
    public final void installSystemProviders(List<ProviderInfo> providers) {
        if (providers != null) {
            installContentProviders(this.mInitialApplication, providers);
        }
    }

    public int getIntCoreSetting(String key, int defaultValue) {
        synchronized (this.mResourcesManager) {
            if (this.mCoreSettings != null) {
                return this.mCoreSettings.getInt(key, defaultValue);
            }
            return defaultValue;
        }
    }

    /* loaded from: classes.dex */
    private static class AndroidOs extends ForwardingOs {
        public static void install() {
            Os def;
            if (ContentResolver.DEPRECATE_DATA_COLUMNS) {
                do {
                    def = Os.getDefault();
                } while (!Os.compareAndSetDefault(def, new AndroidOs(def)));
            }
        }

        private AndroidOs(Os os) {
            super(os);
        }

        private FileDescriptor openDeprecatedDataPath(String path, int mode) throws ErrnoException {
            Uri uri = ContentResolver.translateDeprecatedDataPath(path);
            Log.v(ActivityThread.TAG, "Redirecting " + path + " to " + uri);
            ContentResolver cr = ActivityThread.currentActivityThread().getApplication().getContentResolver();
            try {
                FileDescriptor fd = new FileDescriptor();
                fd.setInt$(cr.openFileDescriptor(uri, FileUtils.translateModePosixToString(mode)).detachFd());
                return fd;
            } catch (FileNotFoundException e) {
                throw new ErrnoException(e.getMessage(), OsConstants.ENOENT);
            } catch (SecurityException e2) {
                throw new ErrnoException(e2.getMessage(), OsConstants.EACCES);
            }
        }

        private void deleteDeprecatedDataPath(String path) throws ErrnoException {
            Uri uri = ContentResolver.translateDeprecatedDataPath(path);
            Log.v(ActivityThread.TAG, "Redirecting " + path + " to " + uri);
            ContentResolver cr = ActivityThread.currentActivityThread().getApplication().getContentResolver();
            try {
                if (cr.delete(uri, null, null) == 0) {
                    throw new FileNotFoundException();
                }
            } catch (FileNotFoundException e) {
                throw new ErrnoException(e.getMessage(), OsConstants.ENOENT);
            } catch (SecurityException e2) {
                throw new ErrnoException(e2.getMessage(), OsConstants.EACCES);
            }
        }

        public boolean access(String path, int mode) throws ErrnoException {
            if (path != null && path.startsWith(ContentResolver.DEPRECATE_DATA_PREFIX)) {
                IoUtils.closeQuietly(openDeprecatedDataPath(path, FileUtils.translateModeAccessToPosix(mode)));
                return true;
            }
            return super.access(path, mode);
        }

        public FileDescriptor open(String path, int flags, int mode) throws ErrnoException {
            if (path != null && path.startsWith(ContentResolver.DEPRECATE_DATA_PREFIX)) {
                return openDeprecatedDataPath(path, mode);
            }
            return super.open(path, flags, mode);
        }

        public StructStat stat(String path) throws ErrnoException {
            if (path != null && path.startsWith(ContentResolver.DEPRECATE_DATA_PREFIX)) {
                FileDescriptor fd = openDeprecatedDataPath(path, OsConstants.O_RDONLY);
                try {
                    return android.system.Os.fstat(fd);
                } finally {
                    IoUtils.closeQuietly(fd);
                }
            }
            return super.stat(path);
        }

        public void unlink(String path) throws ErrnoException {
            if (path != null && path.startsWith(ContentResolver.DEPRECATE_DATA_PREFIX)) {
                deleteDeprecatedDataPath(path);
            } else {
                super.unlink(path);
            }
        }

        public void remove(String path) throws ErrnoException {
            if (path != null && path.startsWith(ContentResolver.DEPRECATE_DATA_PREFIX)) {
                deleteDeprecatedDataPath(path);
            } else {
                super.remove(path);
            }
        }

        public void rename(String oldPath, String newPath) throws ErrnoException {
            try {
                super.rename(oldPath, newPath);
            } catch (ErrnoException e) {
                if (e.errno == OsConstants.EXDEV) {
                    Log.v(ActivityThread.TAG, "Recovering failed rename " + oldPath + " to " + newPath);
                    try {
                        Files.move(new File(oldPath).toPath(), new File(newPath).toPath(), new CopyOption[0]);
                        return;
                    } catch (IOException e2) {
                        throw e;
                    }
                }
                throw e;
            }
        }
    }

    public static void main(String[] args) {
        Trace.traceBegin(64L, "ActivityThreadMain");
        AndroidOs.install();
        CloseGuard.setEnabled(false);
        Environment.initForCurrentUser();
        File configDir = Environment.getUserConfigDirectory(UserHandle.myUserId());
        TrustedCertificateStore.setDefaultUserDirectory(configDir);
        Process.setArgV0("<pre-initialized>");
        Looper.prepareMainLooper();
        long startSeq = 0;
        if (args != null) {
            for (int i = args.length - 1; i >= 0; i--) {
                if (args[i] != null && args[i].startsWith(PROC_START_SEQ_IDENT)) {
                    startSeq = Long.parseLong(args[i].substring(PROC_START_SEQ_IDENT.length()));
                }
            }
        }
        ActivityThread thread = new ActivityThread();
        thread.attach(false, startSeq);
        if (sMainThreadHandler == null) {
            sMainThreadHandler = thread.getHandler();
        }
        Trace.traceEnd(64L);
        Looper.loop();
        throw new RuntimeException("Main thread loop unexpectedly exited");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void purgePendingResources() {
        Trace.traceBegin(64L, "purgePendingResources");
        nPurgePendingResources();
        Trace.traceEnd(64L);
    }
}
