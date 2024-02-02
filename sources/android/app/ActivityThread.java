package android.app;

import android.app.Activity;
import android.app.ActivityThread;
import android.app.IApplicationThread;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.app.backup.BackupAgent;
import android.app.job.JobInfo;
import android.app.servertransaction.ActivityLifecycleItem;
import android.app.servertransaction.ActivityRelaunchItem;
import android.app.servertransaction.ActivityResultItem;
import android.app.servertransaction.ClientTransaction;
import android.app.servertransaction.PendingTransactionActions;
import android.app.servertransaction.TransactionExecutor;
import android.app.servertransaction.TransactionExecutorHelper;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.ContentProvider;
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
import android.graphics.ImageDecoder;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManagerGlobal;
import android.net.ConnectivityManager;
import android.net.IConnectivityManager;
import android.net.Network;
import android.net.Proxy;
import android.net.ProxyInfo;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.GraphicsEnvironment;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.Process;
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
import android.provider.Downloads;
import android.provider.FontsContract;
import android.provider.Settings;
import android.renderscript.RenderScriptCacheDir;
import android.security.NetworkSecurityPolicy;
import android.security.net.config.NetworkSecurityConfigProvider;
import android.service.notification.ZenModeConfig;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
import android.util.MergedConfiguration;
import android.util.Pair;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.util.SparseIntArray;
import android.util.SuperNotCalledException;
import android.util.proto.ProtoOutputStream;
import android.view.Choreographer;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.SurfaceControl;
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
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.org.conscrypt.OpenSSLSocketImpl;
import com.android.org.conscrypt.TrustedCertificateStore;
import com.xiaopeng.app.xpActivityManager;
import com.xiaopeng.app.xpActivityThreadProxy;
import com.xiaopeng.util.DebugOption;
import com.xiaopeng.view.xpWindowManager;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.CloseGuard;
import dalvik.system.VMDebug;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import libcore.io.DropBox;
import libcore.io.EventLogger;
import libcore.io.IoUtils;
import libcore.net.event.NetworkEventDispatcher;
import org.apache.harmony.dalvik.ddmc.DdmVmInternal;
/* loaded from: classes.dex */
public final class ActivityThread extends ClientTransactionHandler {
    private static final int ACTIVITY_THREAD_CHECKIN_VERSION = 4;
    private static final boolean DEBUG_BACKUP = false;
    public static final boolean DEBUG_CONFIGURATION = false;
    public static final boolean DEBUG_MEMORY_TRIM = false;
    static final boolean DEBUG_MESSAGES = false;
    public static final boolean DEBUG_ORDER = false;
    private static final boolean DEBUG_PROVIDER = false;
    private static final boolean DEBUG_RESULTS = false;
    private static final boolean DEBUG_SERVICE = false;
    private static final String HEAP_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s";
    private static final String HEAP_FULL_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s";
    public static final long INVALID_PROC_STATE_SEQ = -1;
    private static final long MIN_TIME_BETWEEN_GCS = 5000;
    private static final String ONE_COUNT_COLUMN = "%21s %8d";
    private static final String ONE_COUNT_COLUMN_HEADER = "%21s %8s";
    private static final String OVERDAW_DIR = "overdraw";
    public static final String PROC_START_SEQ_IDENT = "seq=";
    private static final boolean REPORT_TO_ACTIVITY = true;
    public static final int SERVICE_DONE_EXECUTING_ANON = 0;
    public static final int SERVICE_DONE_EXECUTING_START = 1;
    public static final int SERVICE_DONE_EXECUTING_STOP = 2;
    private static final int SQLITE_MEM_RELEASED_EVENT_LOG_TAG = 75003;
    public static final String TAG = "ActivityThread";
    private static final String TWO_COUNT_COLUMNS = "%21s %8d %21s %8d";
    private static final String VIEWHIERARCHY_DIR = "viewHierarchy";
    private static File mOverdrawDirectory;
    private static File mViewHierarchyDirectory;
    public protected static volatile ActivityThread sCurrentActivityThread;
    public private protected static volatile Handler sMainThreadHandler;
    public private protected static volatile IPackageManager sPackageManager;
    public private protected AppBindData mBoundApplication;
    Configuration mCompatConfiguration;
    public private protected Configuration mConfiguration;
    public private protected int mCurDefaultDisplayDpi;
    public private protected boolean mDensityCompatMode;
    public private protected Application mInitialApplication;
    public private protected Instrumentation mInstrumentation;
    private int mLastSessionId;
    Profiler mProfiler;
    public protected ContextImpl mSystemContext;
    private ContextImpl mSystemUiContext;
    private static final Bitmap.Config THUMBNAIL_FORMAT = Bitmap.Config.RGB_565;
    static final boolean localLOGV = DebugOption.DEBUG_AM_ACTIVITY_THREAD_LOCAL_LOGV;
    public static final boolean DEBUG_BROADCAST = DebugOption.DEBUG_AM_ACTIVITY_THREAD_BROADCAST;
    private static final boolean SHOWOVERDRAW = ThreadedRenderer.OVERDRAW_PROPERTY_SHOW.equals(SystemProperties.get("persist.sys.debug.overdraw"));
    private static final ThreadLocal<Intent> sCurrentBroadcastIntent = new ThreadLocal<>();
    private final Object mNetworkPolicyLock = new Object();
    @GuardedBy("mNetworkPolicyLock")
    private long mNetworkBlockSeq = -1;
    public private protected final ApplicationThread mAppThread = new ApplicationThread();
    public private protected final Looper mLooper = Looper.myLooper();
    public private protected final H mH = new H();
    final Executor mExecutor = new HandlerExecutor(this.mH);
    public private protected final ArrayMap<IBinder, ActivityClientRecord> mActivities = new ArrayMap<>();
    ActivityClientRecord mNewActivities = null;
    public private protected int mNumVisibleActivities = 0;
    ArrayList<WeakReference<AssistStructure>> mLastAssistStructures = new ArrayList<>();
    public private protected final ArrayMap<IBinder, Service> mServices = new ArrayMap<>();
    public private protected final ArrayList<Application> mAllApplications = new ArrayList<>();
    final ArrayMap<String, BackupAgent> mBackupAgents = new ArrayMap<>();
    String mInstrumentationPackageName = null;
    public private protected String mInstrumentationAppDir = null;
    String[] mInstrumentationSplitAppDirs = null;
    String mInstrumentationLibDir = null;
    public private protected String mInstrumentedAppDir = null;
    String[] mInstrumentedSplitAppDirs = null;
    String mInstrumentedLibDir = null;
    boolean mSystemThread = false;
    boolean mJitEnabled = false;
    boolean mSomeActivitiesChanged = false;
    boolean mUpdatingSystemConfig = false;
    boolean mHiddenApiWarningShown = false;
    @GuardedBy("mResourcesManager")
    public private protected final ArrayMap<String, WeakReference<LoadedApk>> mPackages = new ArrayMap<>();
    @GuardedBy("mResourcesManager")
    public private protected final ArrayMap<String, WeakReference<LoadedApk>> mResourcePackages = new ArrayMap<>();
    @GuardedBy("mResourcesManager")
    final ArrayList<ActivityClientRecord> mRelaunchingActivities = new ArrayList<>();
    @GuardedBy("mResourcesManager")
    public private protected Configuration mPendingConfiguration = null;
    private final TransactionExecutor mTransactionExecutor = new TransactionExecutor(this);
    public private protected final ArrayMap<ProviderKey, ProviderClientRecord> mProviderMap = new ArrayMap<>();
    public private protected final ArrayMap<IBinder, ProviderRefCount> mProviderRefCountMap = new ArrayMap<>();
    public private protected final ArrayMap<IBinder, ProviderClientRecord> mLocalProviders = new ArrayMap<>();
    public private protected final ArrayMap<ComponentName, ProviderClientRecord> mLocalProvidersByName = new ArrayMap<>();
    @GuardedBy("mGetProviderLocks")
    final ArrayMap<ProviderKey, Object> mGetProviderLocks = new ArrayMap<>();
    final ArrayMap<Activity, ArrayList<OnActivityPausedListener>> mOnPauseListeners = new ArrayMap<>();
    final GcIdler mGcIdler = new GcIdler();
    final PurgeIdler mPurgeIdler = new PurgeIdler();
    boolean mPurgeIdlerScheduled = false;
    boolean mGcIdlerScheduled = false;
    Bundle mCoreSettings = null;
    private xpActivityThreadProxy mActivityThreadProxy = new xpActivityThreadProxy();
    private Configuration mMainThreadConfig = new Configuration();
    public protected final ResourcesManager mResourcesManager = ResourcesManager.getInstance();

    /* JADX INFO: Access modifiers changed from: private */
    public native void nDumpGraphicsInfo(FileDescriptor fileDescriptor);

    /* JADX INFO: Access modifiers changed from: private */
    public native void nPurgePendingResources();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ProviderKey {
        final String authority;
        final int userId;

        public synchronized ProviderKey(String authority, int userId) {
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
            return (this.authority != null ? this.authority.hashCode() : 0) ^ this.userId;
        }
    }

    /* loaded from: classes.dex */
    public static final class ActivityClientRecord {
        public private protected Activity activity;
        public private protected ActivityInfo activityInfo;
        public private protected CompatibilityInfo compatInfo;
        ViewRootImpl.ActivityConfigCallback configCallback;
        Configuration createdConfig;
        String embeddedID;
        boolean hideForNow;
        int ident;
        public private protected Intent intent;
        public final boolean isForward;
        Activity.NonConfigurationInstances lastNonConfigurationInstances;
        private int mLifecycleState;
        Window mPendingRemoveWindow;
        WindowManager mPendingRemoveWindowManager;
        public private protected boolean mPreserveWindow;
        Configuration newConfig;
        ActivityClientRecord nextIdle;
        Configuration overrideConfig;
        private protected LoadedApk packageInfo;
        Activity parent;
        public private protected boolean paused;
        int pendingConfigChanges;
        List<ReferrerIntent> pendingIntents;
        List<ResultInfo> pendingResults;
        PersistableBundle persistentState;
        ProfilerInfo profilerInfo;
        String referrer;
        boolean startsNotResumed;
        Bundle state;
        public private protected boolean stopped;
        private Configuration tmpConfig;
        private protected IBinder token;
        IVoiceInteractor voiceInteractor;
        Window window;

        @VisibleForTesting
        private protected ActivityClientRecord() {
            this.tmpConfig = new Configuration();
            this.mLifecycleState = 0;
            this.isForward = false;
            init();
        }

        public synchronized ActivityClientRecord(IBinder token, Intent intent, int ident, ActivityInfo info, Configuration overrideConfig, CompatibilityInfo compatInfo, String referrer, IVoiceInteractor voiceInteractor, Bundle state, PersistableBundle persistentState, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, boolean isForward, ProfilerInfo profilerInfo, ClientTransactionHandler client) {
            this.tmpConfig = new Configuration();
            this.mLifecycleState = 0;
            this.token = token;
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
            this.overrideConfig = xpActivityManager.getOverrideConfiguration(overrideConfig);
            this.packageInfo = client.getPackageInfoNoCheck(this.activityInfo.applicationInfo, compatInfo);
            init();
        }

        private synchronized void init() {
            this.parent = null;
            this.embeddedID = null;
            this.paused = false;
            this.stopped = false;
            this.hideForNow = false;
            this.nextIdle = null;
            this.configCallback = new ViewRootImpl.ActivityConfigCallback() { // from class: android.app.-$$Lambda$ActivityThread$ActivityClientRecord$HOrG1qglSjSUHSjKBn2rXtX0gGg
                @Override // android.view.ViewRootImpl.ActivityConfigCallback
                public final void onConfigurationChanged(Configuration configuration, int i) {
                    ActivityThread.ActivityClientRecord.lambda$init$0(ActivityThread.ActivityClientRecord.this, configuration, i);
                }
            };
        }

        public static /* synthetic */ void lambda$init$0(ActivityClientRecord activityClientRecord, Configuration overrideConfig, int newDisplayId) {
            if (activityClientRecord.activity == null) {
                throw new IllegalStateException("Received config update for non-existing activity");
            }
            activityClientRecord.activity.mMainThread.handleActivityConfigurationChanged(activityClientRecord.token, overrideConfig, newDisplayId);
        }

        public synchronized int getLifecycleState() {
            return this.mLifecycleState;
        }

        public synchronized void setState(int newLifecycleState) {
            this.mLifecycleState = newLifecycleState;
            switch (this.mLifecycleState) {
                case 1:
                    this.paused = true;
                    this.stopped = true;
                    return;
                case 2:
                    this.paused = true;
                    this.stopped = false;
                    return;
                case 3:
                    this.paused = false;
                    this.stopped = false;
                    return;
                case 4:
                    this.paused = true;
                    this.stopped = false;
                    return;
                case 5:
                    this.paused = true;
                    this.stopped = true;
                    return;
                default:
                    return;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean isPreHoneycomb() {
            return this.activity != null && this.activity.getApplicationInfo().targetSdkVersion < 11;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean isPreP() {
            return this.activity != null && this.activity.getApplicationInfo().targetSdkVersion < 28;
        }

        public synchronized boolean isPersistable() {
            return this.activityInfo.persistableMode == 2;
        }

        public synchronized boolean isVisibleFromServer() {
            return this.activity != null && this.activity.mVisibleFromServer;
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
            ComponentName componentName = this.intent != null ? this.intent.getComponent() : null;
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

        public synchronized String getStateString() {
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
                sb.append(", temporaryPause=");
                sb.append(this.activity.mTemporaryPause);
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
        public private protected final ContentProviderHolder mHolder;
        public private protected final ContentProvider mLocalProvider;
        final String[] mNames;
        public private protected final IContentProvider mProvider;

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
        public private protected CompatibilityInfo compatInfo;
        public private protected ActivityInfo info;
        public private protected Intent intent;

        public synchronized ReceiverData(Intent intent, int resultCode, String resultData, Bundle resultExtras, boolean ordered, boolean sticky, IBinder token, int sendingUser) {
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

        synchronized CreateBackupAgentData() {
        }

        public String toString() {
            return "CreateBackupAgentData{appInfo=" + this.appInfo + " backupAgent=" + this.appInfo.backupAgentName + " mode=" + this.backupMode + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class CreateServiceData {
        public private protected CompatibilityInfo compatInfo;
        public private protected ServiceInfo info;
        public private protected Intent intent;
        public private protected IBinder token;

        public String toString() {
            return "CreateServiceData{token=" + this.token + " className=" + this.info.name + " packageName=" + this.info.packageName + " intent=" + this.intent + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class BindServiceData {
        public private protected Intent intent;
        boolean rebind;
        public private protected IBinder token;

        synchronized BindServiceData() {
        }

        public String toString() {
            return "BindServiceData{token=" + this.token + " intent=" + this.intent + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ServiceArgsData {
        public private protected Intent args;
        int flags;
        int startId;
        boolean taskRemoved;
        public private protected IBinder token;

        synchronized ServiceArgsData() {
        }

        public String toString() {
            return "ServiceArgsData{token=" + this.token + " startId=" + this.startId + " args=" + this.args + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class AppBindData {
        public private protected ApplicationInfo appInfo;
        boolean autofillCompatibilityEnabled;
        String buildSerial;
        public private protected CompatibilityInfo compatInfo;
        Configuration config;
        int debugMode;
        boolean enableBinderTracking;
        public private protected LoadedApk info;
        ProfilerInfo initProfilerInfo;
        public private protected Bundle instrumentationArgs;
        ComponentName instrumentationName;
        IUiAutomationConnection instrumentationUiAutomationConnection;
        IInstrumentationWatcher instrumentationWatcher;
        public private protected boolean persistent;
        public private protected String processName;
        public private protected List<ProviderInfo> providers;
        public private protected boolean restrictedBackupMode;
        boolean trackAllocation;

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

        synchronized Profiler() {
        }

        public synchronized void setProfiler(ProfilerInfo profilerInfo) {
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
            if (this.profileFd != null) {
                try {
                    this.profileFd.close();
                } catch (IOException e2) {
                }
            }
            this.profileFile = profilerInfo.profileFile;
            this.profileFd = fd;
            this.samplingInterval = profilerInfo.samplingInterval;
            this.autoStopProfiler = profilerInfo.autoStopProfiler;
            this.streamingOutput = profilerInfo.streamingOutput;
        }

        public synchronized void startProfiling() {
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

        public synchronized void stopProfiling() {
            if (this.profiling) {
                this.profiling = false;
                Debug.stopMethodTracing();
                if (this.profileFd != null) {
                    try {
                        this.profileFd.close();
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

        synchronized DumpComponentInfo() {
        }
    }

    /* loaded from: classes.dex */
    static final class ContextCleanupInfo {
        ContextImpl context;
        String what;
        String who;

        synchronized ContextCleanupInfo() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class DumpHeapData {
        ParcelFileDescriptor fd;
        public boolean mallocInfo;
        public boolean managed;
        String path;
        public boolean runGc;

        synchronized DumpHeapData() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class UpdateCompatibilityData {
        CompatibilityInfo info;
        String pkg;

        synchronized UpdateCompatibilityData() {
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

        synchronized RequestAssistContextExtras() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ApplicationThread extends IApplicationThread.Stub {
        private static final String DB_INFO_FORMAT = "  %8s %8s %14s %14s  %s";
        private int mLastProcessState;

        private ApplicationThread() {
            this.mLastProcessState = -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void updatePendingConfiguration(Configuration config) {
            synchronized (ActivityThread.this.mResourcesManager) {
                if (ActivityThread.this.mPendingConfiguration == null || ActivityThread.this.mPendingConfiguration.isOtherSeqNewer(config)) {
                    ActivityThread.this.mPendingConfiguration = config;
                }
            }
        }

        @Override // android.app.IApplicationThread
        public final synchronized void scheduleSleeping(IBinder token, boolean sleeping) {
            ActivityThread.this.sendMessage(137, token, sleeping ? 1 : 0);
        }

        @Override // android.app.IApplicationThread
        public final synchronized void scheduleReceiver(Intent intent, ActivityInfo info, CompatibilityInfo compatInfo, int resultCode, String data, Bundle extras, boolean sync, int sendingUser, int processState) {
            updateProcessState(processState, false);
            ReceiverData r = new ReceiverData(intent, resultCode, data, extras, sync, false, ActivityThread.this.mAppThread.asBinder(), sendingUser);
            r.info = info;
            r.compatInfo = compatInfo;
            ActivityThread.this.sendMessage(113, r);
        }

        @Override // android.app.IApplicationThread
        public final synchronized void scheduleCreateBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int backupMode) {
            CreateBackupAgentData d = new CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;
            d.backupMode = backupMode;
            ActivityThread.this.sendMessage(128, d);
        }

        @Override // android.app.IApplicationThread
        public final synchronized void scheduleDestroyBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo) {
            CreateBackupAgentData d = new CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;
            ActivityThread.this.sendMessage(129, d);
        }

        public final synchronized void scheduleCreateService(IBinder token, ServiceInfo info, CompatibilityInfo compatInfo, int processState) {
            updateProcessState(processState, false);
            CreateServiceData s = new CreateServiceData();
            s.token = token;
            s.info = info;
            s.compatInfo = compatInfo;
            ActivityThread.this.sendMessage(114, s);
        }

        public final synchronized void scheduleBindService(IBinder token, Intent intent, boolean rebind, int processState) {
            updateProcessState(processState, false);
            BindServiceData s = new BindServiceData();
            s.token = token;
            s.intent = intent;
            s.rebind = rebind;
            ActivityThread.this.sendMessage(121, s);
        }

        public final synchronized void scheduleUnbindService(IBinder token, Intent intent) {
            BindServiceData s = new BindServiceData();
            s.token = token;
            s.intent = intent;
            ActivityThread.this.sendMessage(122, s);
        }

        @Override // android.app.IApplicationThread
        public final synchronized void scheduleServiceArgs(IBinder token, ParceledListSlice args) {
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

        public final synchronized void scheduleStopService(IBinder token) {
            ActivityThread.this.sendMessage(116, token);
        }

        @Override // android.app.IApplicationThread
        public final synchronized void bindApplication(String processName, ApplicationInfo appInfo, List<ProviderInfo> providers, ComponentName instrumentationName, ProfilerInfo profilerInfo, Bundle instrumentationArgs, IInstrumentationWatcher instrumentationWatcher, IUiAutomationConnection instrumentationUiConnection, int debugMode, boolean enableBinderTracking, boolean trackAllocation, boolean isRestrictedBackupMode, boolean persistent, Configuration config, CompatibilityInfo compatInfo, Map services, Bundle coreSettings, String buildSerial, boolean autofillCompatibilityEnabled) {
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
            data.config = xpActivityManager.getOverrideConfiguration(config, appInfo.packageName);
            data.compatInfo = compatInfo;
            data.initProfilerInfo = profilerInfo;
            data.buildSerial = buildSerial;
            data.autofillCompatibilityEnabled = autofillCompatibilityEnabled;
            ActivityThread.this.sendMessage(110, data);
        }

        @Override // android.app.IApplicationThread
        public final synchronized void runIsolatedEntryPoint(String entryPoint, String[] entryPointArgs) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = entryPoint;
            args.arg2 = entryPointArgs;
            ActivityThread.this.sendMessage(158, args);
        }

        public final synchronized void scheduleExit() {
            ActivityThread.this.sendMessage(111, null);
        }

        public final synchronized void scheduleSuicide() {
            ActivityThread.this.sendMessage(130, null);
        }

        @Override // android.app.IApplicationThread
        public synchronized void scheduleApplicationInfoChanged(ApplicationInfo ai) {
            ActivityThread.this.sendMessage(156, ai);
        }

        public synchronized void updateTimeZone() {
            TimeZone.setDefault(null);
        }

        @Override // android.app.IApplicationThread
        public synchronized void clearDnsCache() {
            InetAddress.clearDnsCache();
            NetworkEventDispatcher.getInstance().onNetworkConfigurationChanged();
        }

        @Override // android.app.IApplicationThread
        public synchronized void setHttpProxy(String host, String port, String exclList, Uri pacFileUrl) {
            ConnectivityManager cm = ConnectivityManager.from(ActivityThread.this.getApplication() != null ? ActivityThread.this.getApplication() : ActivityThread.this.getSystemContext());
            Network network = cm.getBoundNetworkForProcess();
            if (network != null) {
                Proxy.setHttpProxySystemProperty(cm.getDefaultProxy());
            } else {
                Proxy.setHttpProxySystemProperty(host, port, exclList, pacFileUrl);
            }
        }

        public synchronized void processInBackground() {
            ActivityThread.this.mH.removeMessages(120);
            ActivityThread.this.mH.sendMessage(ActivityThread.this.mH.obtainMessage(120));
        }

        @Override // android.app.IApplicationThread
        public synchronized void dumpService(ParcelFileDescriptor pfd, IBinder servicetoken, String[] args) {
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
        public synchronized void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent, int resultCode, String dataStr, Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws RemoteException {
            updateProcessState(processState, false);
            receiver.performReceive(intent, resultCode, dataStr, extras, ordered, sticky, sendingUser);
        }

        public synchronized void scheduleLowMemory() {
            ActivityThread.this.sendMessage(124, null);
        }

        @Override // android.app.IApplicationThread
        public synchronized void profilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) {
            ActivityThread.this.sendMessage(127, profilerInfo, start ? 1 : 0, profileType);
        }

        @Override // android.app.IApplicationThread
        public synchronized void dumpHeap(boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd) {
            DumpHeapData dhd = new DumpHeapData();
            dhd.managed = managed;
            dhd.mallocInfo = mallocInfo;
            dhd.runGc = runGc;
            dhd.path = path;
            dhd.fd = fd;
            ActivityThread.this.sendMessage(135, (Object) dhd, 0, 0, true);
        }

        @Override // android.app.IApplicationThread
        public synchronized void attachAgent(String agent) {
            ActivityThread.this.sendMessage(155, agent);
        }

        @Override // android.app.IApplicationThread
        public synchronized void setSchedulingGroup(int group) {
            try {
                Process.setProcessGroup(Process.myPid(), group);
            } catch (Exception e) {
                Slog.w(ActivityThread.TAG, "Failed setting process group to " + group, e);
            }
        }

        @Override // android.app.IApplicationThread
        public synchronized void dispatchPackageBroadcast(int cmd, String[] packages) {
            ActivityThread.this.sendMessage(133, packages, cmd);
        }

        @Override // android.app.IApplicationThread
        public synchronized void scheduleCrash(String msg) {
            ActivityThread.this.sendMessage(134, msg);
        }

        @Override // android.app.IApplicationThread
        public synchronized void dumpActivity(ParcelFileDescriptor pfd, IBinder activitytoken, String prefix, String[] args) {
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
        public synchronized void dumpProvider(ParcelFileDescriptor pfd, IBinder providertoken, String[] args) {
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
        public synchronized void dumpMemInfo(ParcelFileDescriptor pfd, Debug.MemoryInfo mem, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) {
            FileOutputStream fout = new FileOutputStream(pfd.getFileDescriptor());
            PrintWriter pw = new FastPrintWriter(fout);
            try {
                dumpMemInfo(pw, mem, checkin, dumpFullInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable);
            } finally {
                pw.flush();
                IoUtils.closeQuietly(pfd);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private synchronized void dumpMemInfo(PrintWriter pw, Debug.MemoryInfo memInfo, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable) {
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
            long viewInstanceCount2 = ViewDebug.getViewRootImplCount();
            int globalAssetCount = AssetManager.getGlobalAssetCount();
            int globalAssetManagerCount = AssetManager.getGlobalAssetManagerCount();
            int binderLocalObjectCount = Debug.getBinderLocalObjectCount();
            int globalAssetManagerCount2 = Debug.getBinderProxyObjectCount();
            int binderProxyObjectCount = Debug.getBinderDeathObjectCount();
            long parcelSize = Parcel.getGlobalAllocSize();
            long parcelCount = Parcel.getGlobalAllocCount();
            SQLiteDebug.PagerStats stats = SQLiteDebug.getDatabaseInfo();
            ActivityThread.dumpMemInfoTable(pw, memInfo, checkin, dumpFullInfo, dumpDalvik, dumpSummaryOnly, Process.myPid(), ActivityThread.this.mBoundApplication != null ? ActivityThread.this.mBoundApplication.processName : "unknown", nativeMax, nativeAllocated, nativeFree, dalvikMax, dalvikAllocated, dalvikFree);
            if (!checkin) {
                SQLiteDebug.PagerStats stats2 = stats;
                pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                pw.println(" Objects");
                ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Views:", Long.valueOf(viewInstanceCount), "ViewRootImpl:", Long.valueOf(viewInstanceCount2));
                ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "AppContexts:", Long.valueOf(appContextInstanceCount), "Activities:", Long.valueOf(activityInstanceCount));
                ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Assets:", Integer.valueOf(globalAssetCount), "AssetManagers:", Integer.valueOf(globalAssetManagerCount));
                ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Local Binders:", Integer.valueOf(binderLocalObjectCount), "Proxy Binders:", Integer.valueOf(globalAssetManagerCount2));
                long viewRootInstanceCount = parcelSize / 1024;
                ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Parcel memory:", Long.valueOf(viewRootInstanceCount), "Parcel count:", Long.valueOf(parcelCount));
                ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Death Recipients:", Integer.valueOf(binderProxyObjectCount), "OpenSSL Sockets:", Long.valueOf(openSslSocketCount));
                ActivityThread.printRow(pw, ActivityThread.ONE_COUNT_COLUMN, "WebViews:", Long.valueOf(webviewInstanceCount));
                pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                pw.println(" SQL");
                ActivityThread.printRow(pw, ActivityThread.ONE_COUNT_COLUMN, "MEMORY_USED:", Integer.valueOf(stats2.memoryUsed / 1024));
                ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "PAGECACHE_OVERFLOW:", Integer.valueOf(stats2.pageCacheOverflow / 1024), "MALLOC_SIZE:", Integer.valueOf(stats2.largestMemAlloc / 1024));
                pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                int N = stats2.dbStats.size();
                if (N > 0) {
                    pw.println(" DATABASES");
                    int i = 5;
                    ActivityThread.printRow(pw, DB_INFO_FORMAT, "pgsz", "dbsz", "Lookaside(b)", "cache", "Dbname");
                    int i2 = 0;
                    while (i2 < N) {
                        SQLiteDebug.DbStats dbStats = stats2.dbStats.get(i2);
                        int N2 = N;
                        SQLiteDebug.PagerStats stats3 = stats2;
                        Object[] objArr = new Object[i];
                        objArr[0] = dbStats.pageSize > 0 ? String.valueOf(dbStats.pageSize) : WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                        objArr[1] = dbStats.dbSize > 0 ? String.valueOf(dbStats.dbSize) : WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                        objArr[2] = dbStats.lookaside > 0 ? String.valueOf(dbStats.lookaside) : WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                        objArr[3] = dbStats.cache;
                        objArr[4] = dbStats.dbName;
                        ActivityThread.printRow(pw, DB_INFO_FORMAT, objArr);
                        i2++;
                        N = N2;
                        stats2 = stats3;
                        i = 5;
                    }
                }
                String assetAlloc = AssetManager.getAssetAllocations();
                if (assetAlloc != null) {
                    pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    pw.println(" Asset Allocations");
                    pw.print(assetAlloc);
                }
                if (dumpUnreachable) {
                    i = (!(ActivityThread.this.mBoundApplication == null || (2 & ActivityThread.this.mBoundApplication.appInfo.flags) == 0) || Build.IS_DEBUGGABLE) ? 1 : 0;
                    boolean showContents = i;
                    pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    pw.println(" Unreachable memory");
                    pw.print(Debug.getUnreachableMemory(100, showContents));
                    return;
                }
                return;
            }
            pw.print(viewInstanceCount);
            pw.print(',');
            pw.print(viewInstanceCount2);
            pw.print(',');
            pw.print(appContextInstanceCount);
            pw.print(',');
            pw.print(activityInstanceCount);
            pw.print(',');
            pw.print(globalAssetCount);
            pw.print(',');
            int globalAssetManagerCount3 = globalAssetManagerCount;
            pw.print(globalAssetManagerCount3);
            pw.print(',');
            int binderLocalObjectCount2 = binderLocalObjectCount;
            pw.print(binderLocalObjectCount2);
            pw.print(',');
            pw.print(globalAssetManagerCount2);
            pw.print(',');
            pw.print(binderProxyObjectCount);
            pw.print(',');
            long openSslSocketCount2 = openSslSocketCount;
            pw.print(openSslSocketCount2);
            pw.print(',');
            pw.print(stats.memoryUsed / 1024);
            pw.print(',');
            pw.print(stats.memoryUsed / 1024);
            pw.print(',');
            pw.print(stats.pageCacheOverflow / 1024);
            pw.print(',');
            pw.print(stats.largestMemAlloc / 1024);
            while (true) {
                int i3 = i;
                long openSslSocketCount3 = openSslSocketCount2;
                if (i3 >= stats.dbStats.size()) {
                    pw.println();
                    return;
                }
                SQLiteDebug.DbStats dbStats2 = stats.dbStats.get(i3);
                pw.print(',');
                pw.print(dbStats2.dbName);
                pw.print(',');
                pw.print(dbStats2.pageSize);
                pw.print(',');
                pw.print(dbStats2.dbSize);
                pw.print(',');
                pw.print(dbStats2.lookaside);
                pw.print(',');
                pw.print(dbStats2.cache);
                pw.print(',');
                pw.print(dbStats2.cache);
                i = i3 + 1;
                openSslSocketCount2 = openSslSocketCount3;
                globalAssetManagerCount3 = globalAssetManagerCount3;
                binderLocalObjectCount2 = binderLocalObjectCount2;
            }
        }

        @Override // android.app.IApplicationThread
        public synchronized void dumpMemInfoProto(ParcelFileDescriptor pfd, Debug.MemoryInfo mem, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) {
            ProtoOutputStream proto = new ProtoOutputStream(pfd.getFileDescriptor());
            try {
                dumpMemInfo(proto, mem, dumpFullInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable);
            } finally {
                proto.flush();
                IoUtils.closeQuietly(pfd);
            }
        }

        private synchronized void dumpMemInfo(ProtoOutputStream proto, Debug.MemoryInfo memInfo, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable) {
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
            int globalAssetManagerCount2 = Debug.getBinderProxyObjectCount();
            int binderProxyObjectCount = Debug.getBinderDeathObjectCount();
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
            proto.write(1120986464264L, globalAssetManagerCount2);
            proto.write(1112396529673L, parcelSize / 1024);
            proto.write(1120986464266L, parcelCount);
            proto.write(1120986464267L, binderProxyObjectCount);
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
                activityInstanceCount2 = activityInstanceCount3;
                stats2 = stats2;
                n = n;
                appContextInstanceCount2 = appContextInstanceCount2;
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
        public synchronized void dumpGfxInfo(ParcelFileDescriptor pfd, String[] args) {
            ActivityThread.this.nDumpGraphicsInfo(pfd.getFileDescriptor());
            WindowManagerGlobal.getInstance().dumpGfxInfo(pfd.getFileDescriptor(), args);
            IoUtils.closeQuietly(pfd);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void dumpDatabaseInfo(ParcelFileDescriptor pfd, String[] args) {
            PrintWriter pw = new FastPrintWriter(new FileOutputStream(pfd.getFileDescriptor()));
            PrintWriterPrinter printer = new PrintWriterPrinter(pw);
            SQLiteDebug.dump(printer, args);
            pw.flush();
        }

        @Override // android.app.IApplicationThread
        public synchronized void dumpDbInfo(ParcelFileDescriptor pfd, final String[] args) {
            try {
                if (!ActivityThread.this.mSystemThread) {
                    dumpDatabaseInfo(pfd, args);
                    return;
                }
                final ParcelFileDescriptor dup = pfd.dup();
                IoUtils.closeQuietly(pfd);
                AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() { // from class: android.app.ActivityThread.ApplicationThread.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            ApplicationThread.this.dumpDatabaseInfo(dup, args);
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
        public synchronized void unstableProviderDied(IBinder provider) {
            ActivityThread.this.sendMessage(142, provider);
        }

        @Override // android.app.IApplicationThread
        public synchronized void requestAssistContextExtras(IBinder activityToken, IBinder requestToken, int requestType, int sessionId, int flags) {
            RequestAssistContextExtras cmd = new RequestAssistContextExtras();
            cmd.activityToken = activityToken;
            cmd.requestToken = requestToken;
            cmd.requestType = requestType;
            cmd.sessionId = sessionId;
            cmd.flags = flags;
            ActivityThread.this.sendMessage(143, cmd);
        }

        @Override // android.app.IApplicationThread
        public synchronized void setCoreSettings(Bundle coreSettings) {
            ActivityThread.this.sendMessage(138, coreSettings);
        }

        @Override // android.app.IApplicationThread
        public synchronized void updatePackageCompatibilityInfo(String pkg, CompatibilityInfo info) {
            UpdateCompatibilityData ucd = new UpdateCompatibilityData();
            ucd.pkg = pkg;
            ucd.info = info;
            ActivityThread.this.sendMessage(139, ucd);
        }

        public synchronized void scheduleTrimMemory(int level) {
            Runnable r = PooledLambda.obtainRunnable(new BiConsumer() { // from class: android.app.-$$Lambda$ActivityThread$ApplicationThread$tUGFX7CUhzB4Pg5wFd5yeqOnu38
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((ActivityThread) obj).handleTrimMemory(((Integer) obj2).intValue());
                }
            }, ActivityThread.this, Integer.valueOf(level));
            Choreographer choreographer = Choreographer.getMainThreadInstance();
            if (choreographer != null) {
                choreographer.postCallback(3, r, null);
            } else {
                ActivityThread.this.mH.post(r);
            }
        }

        @Override // android.app.IApplicationThread
        public synchronized void scheduleTranslucentConversionComplete(IBinder token, boolean drawComplete) {
            ActivityThread.this.sendMessage(144, token, drawComplete ? 1 : 0);
        }

        @Override // android.app.IApplicationThread
        public synchronized void scheduleOnNewActivityOptions(IBinder token, Bundle options) {
            ActivityThread.this.sendMessage(146, new Pair(token, ActivityOptions.fromBundle(options)));
        }

        @Override // android.app.IApplicationThread
        public synchronized void setProcessState(int state) {
            updateProcessState(state, true);
        }

        public synchronized void updateProcessState(int processState, boolean fromIpc) {
            if (this.mLastProcessState != processState) {
                this.mLastProcessState = processState;
                int dalvikProcessState = 1;
                if (processState <= 5) {
                    dalvikProcessState = 0;
                }
                VMRuntime.getRuntime().updateProcessState(dalvikProcessState);
            }
        }

        @Override // android.app.IApplicationThread
        public synchronized void setNetworkBlockSeq(long procStateSeq) {
            synchronized (ActivityThread.this.mNetworkPolicyLock) {
                ActivityThread.this.mNetworkBlockSeq = procStateSeq;
            }
        }

        @Override // android.app.IApplicationThread
        public synchronized void scheduleInstallProvider(ProviderInfo provider) {
            ActivityThread.this.sendMessage(145, provider);
        }

        @Override // android.app.IApplicationThread
        public final synchronized void updateTimePrefs(int timeFormatPreference) {
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
        public synchronized void scheduleEnterAnimationComplete(IBinder token) {
            ActivityThread.this.sendMessage(149, token);
        }

        @Override // android.app.IApplicationThread
        public synchronized void notifyCleartextNetwork(byte[] firstPacket) {
            if (StrictMode.vmCleartextNetworkEnabled()) {
                StrictMode.onCleartextNetworkDetected(firstPacket);
            }
        }

        @Override // android.app.IApplicationThread
        public synchronized void startBinderTracking() {
            ActivityThread.this.sendMessage(150, null);
        }

        @Override // android.app.IApplicationThread
        public synchronized void stopBinderTrackingAndDump(ParcelFileDescriptor pfd) {
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
        public synchronized void scheduleLocalVoiceInteractionStarted(IBinder token, IVoiceInteractor voiceInteractor) throws RemoteException {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = token;
            args.arg2 = voiceInteractor;
            ActivityThread.this.sendMessage(154, args);
        }

        @Override // android.app.IApplicationThread
        public synchronized void handleTrustStorageUpdate() {
            NetworkSecurityPolicy.getInstance().handleTrustStorageUpdate();
        }

        @Override // android.app.IApplicationThread
        public synchronized void scheduleTransaction(ClientTransaction transaction) throws RemoteException {
            ActivityThread.this.scheduleTransaction(transaction);
        }
    }

    public void performanceAnalyze(IBinder token) {
        sendMessage(162, token);
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized void updatePendingConfiguration(Configuration config) {
        this.mAppThread.updatePendingConfiguration(config);
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized void updateProcessState(int processState, boolean fromIpc) {
        this.mAppThread.updateProcessState(processState, fromIpc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class H extends Handler {
        public static final int ANALYZE_PERFORMANCE = 162;
        public static final int APPLICATION_INFO_CHANGED = 156;
        public static final int ATTACH_AGENT = 155;
        public static final int BIND_APPLICATION = 110;
        private protected static final int BIND_SERVICE = 121;
        public static final int CLEAN_UP_CONTEXT = 119;
        public static final int CONFIGURATION_CHANGED = 118;
        public static final int CREATE_BACKUP_AGENT = 128;
        private protected static final int CREATE_SERVICE = 114;
        public static final int DESTROY_BACKUP_AGENT = 129;
        public static final int DISPATCH_PACKAGE_BROADCAST = 133;
        public static final int DUMP_ACTIVITY = 136;
        public static final int DUMP_HEAP = 135;
        private protected static final int DUMP_PROVIDER = 141;
        public static final int DUMP_SERVICE = 123;
        public static final int ENABLE_JIT = 132;
        private protected static final int ENTER_ANIMATION_COMPLETE = 149;
        public static final int EXECUTE_TRANSACTION = 159;
        private protected static final int EXIT_APPLICATION = 111;
        private protected static final int GC_WHEN_IDLE = 120;
        private protected static final int INSTALL_PROVIDER = 145;
        public static final int LOCAL_VOICE_INTERACTION_STARTED = 154;
        public static final int LOW_MEMORY = 124;
        public static final int ON_NEW_ACTIVITY_OPTIONS = 146;
        public static final int PROFILER_CONTROL = 127;
        public static final int PURGE_RESOURCES = 161;
        private protected static final int RECEIVER = 113;
        public static final int RELAUNCH_ACTIVITY = 160;
        private protected static final int REMOVE_PROVIDER = 131;
        public static final int REQUEST_ASSIST_CONTEXT_EXTRAS = 143;
        public static final int RUN_ISOLATED_ENTRY_POINT = 158;
        private protected static final int SCHEDULE_CRASH = 134;
        private protected static final int SERVICE_ARGS = 115;
        public static final int SET_CORE_SETTINGS = 138;
        public static final int SLEEPING = 137;
        public static final int START_BINDER_TRACKING = 150;
        public static final int STOP_BINDER_TRACKING_AND_DUMP = 151;
        private protected static final int STOP_SERVICE = 116;
        public static final int SUICIDE = 130;
        public static final int TRANSLUCENT_CONVERSION_COMPLETE = 144;
        private protected static final int UNBIND_SERVICE = 122;
        public static final int UNSTABLE_PROVIDER_DIED = 142;
        public static final int UPDATE_PACKAGE_COMPATIBILITY_INFO = 139;

        H() {
        }

        synchronized String codeToString(int code) {
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
                case 132:
                    ActivityThread.this.ensureJitEnabled();
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
                    ActivityThread.this.mUpdatingSystemConfig = true;
                    try {
                        ActivityThread.this.handleApplicationInfoChanged((ApplicationInfo) msg.obj);
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
                case 162:
                    ActivityThread.this.handlePerformanceAnalyze((IBinder) msg.obj);
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
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean queueIdle() {
            /*
                r8 = this;
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
                android.app.IActivityManager r3 = android.app.ActivityManager.getService()
            L28:
                boolean r5 = android.app.ActivityThread.localLOGV
                if (r5 == 0) goto L57
                java.lang.String r5 = "ActivityThread"
                java.lang.StringBuilder r6 = new java.lang.StringBuilder
                r6.<init>()
                java.lang.String r7 = "Reporting idle of "
                r6.append(r7)
                r6.append(r0)
                java.lang.String r7 = " finished="
                r6.append(r7)
                android.app.Activity r7 = r0.activity
                if (r7 == 0) goto L4c
                android.app.Activity r7 = r0.activity
                boolean r7 = r7.mFinished
                if (r7 == 0) goto L4c
                r7 = 1
                goto L4d
            L4c:
                r7 = r2
            L4d:
                r6.append(r7)
                java.lang.String r6 = r6.toString()
                android.util.Slog.v(r5, r6)
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
                r3.ensureJitEnabled()
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
            ActivityThread.this.nPurgePendingResources();
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
            Trace.traceBegin(64L, "purgePendingResources");
            ActivityThread.this.nPurgePendingResources();
            Trace.traceEnd(64L);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ActivityThread currentActivityThread() {
        return sCurrentActivityThread;
    }

    public static synchronized boolean isSystem() {
        if (sCurrentActivityThread != null) {
            return sCurrentActivityThread.mSystemThread;
        }
        return false;
    }

    public static synchronized String currentOpPackageName() {
        ActivityThread am = currentActivityThread();
        if (am == null || am.getApplication() == null) {
            return null;
        }
        return am.getApplication().getOpPackageName();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String currentPackageName() {
        ActivityThread am = currentActivityThread();
        if (am == null || am.mBoundApplication == null) {
            return null;
        }
        return am.mBoundApplication.appInfo.packageName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String currentProcessName() {
        ActivityThread am = currentActivityThread();
        if (am == null || am.mBoundApplication == null) {
            return null;
        }
        return am.mBoundApplication.processName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Application currentApplication() {
        ActivityThread am = currentActivityThread();
        if (am != null) {
            return am.mInitialApplication;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IPackageManager getPackageManager() {
        if (sPackageManager != null) {
            return sPackageManager;
        }
        IBinder b = ServiceManager.getService("package");
        sPackageManager = IPackageManager.Stub.asInterface(b);
        return sPackageManager;
    }

    synchronized Configuration applyConfigCompatMainThread(int displayDensity, Configuration config, CompatibilityInfo compat) {
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
    public synchronized Resources getTopLevelResources(String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, LoadedApk pkgInfo) {
        return this.mResourcesManager.getResources(null, resDir, splitResDirs, overlayDirs, libDirs, displayId, null, pkgInfo.getCompatibilityInfo(), pkgInfo.getClassLoader());
    }

    public private protected final Handler getHandler() {
        return this.mH;
    }

    private protected final LoadedApk getPackageInfo(String packageName, CompatibilityInfo compatInfo, int flags) {
        return getPackageInfo(packageName, compatInfo, flags, UserHandle.myUserId());
    }

    public final synchronized LoadedApk getPackageInfo(String packageName, CompatibilityInfo compatInfo, int flags, int userId) {
        WeakReference<LoadedApk> ref;
        boolean differentUser = UserHandle.myUserId() != userId;
        synchronized (this.mResourcesManager) {
            try {
                if (differentUser) {
                    ref = null;
                } else if ((flags & 1) != 0) {
                    ref = this.mPackages.get(packageName);
                } else {
                    ref = this.mResourcePackages.get(packageName);
                }
                LoadedApk packageInfo = ref != null ? ref.get() : null;
                if (packageInfo != null && (packageInfo.mResources == null || packageInfo.mResources.getAssets().isUpToDate())) {
                    if (packageInfo.isSecurityViolation() && (flags & 2) == 0) {
                        throw new SecurityException("Requesting code from " + packageName + " to be run in process " + this.mBoundApplication.processName + "/" + this.mBoundApplication.appInfo.uid);
                    }
                    return packageInfo;
                }
                try {
                    ApplicationInfo ai = getPackageManager().getApplicationInfo(packageName, 268436480, userId);
                    if (ai == null) {
                        return null;
                    }
                    return getPackageInfo(ai, compatInfo, flags);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    private protected final LoadedApk getPackageInfoNoCheck(ApplicationInfo ai, CompatibilityInfo compatInfo) {
        return getPackageInfo(ai, compatInfo, null, false, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final LoadedApk peekPackageInfo(String packageName, boolean includeCode) {
        WeakReference<LoadedApk> ref;
        LoadedApk loadedApk;
        synchronized (this.mResourcesManager) {
            try {
                if (includeCode) {
                    ref = this.mPackages.get(packageName);
                } else {
                    ref = this.mResourcePackages.get(packageName);
                }
                loadedApk = ref != null ? ref.get() : null;
            } catch (Throwable th) {
                throw th;
            }
        }
        return loadedApk;
    }

    private synchronized LoadedApk getPackageInfo(ApplicationInfo aInfo, CompatibilityInfo compatInfo, ClassLoader baseLoader, boolean securityViolation, boolean includeCode, boolean registerPackage) {
        WeakReference<LoadedApk> ref;
        LoadedApk packageInfo;
        boolean differentUser = UserHandle.myUserId() != UserHandle.getUserId(aInfo.uid);
        synchronized (this.mResourcesManager) {
            try {
                if (differentUser) {
                    ref = null;
                } else if (includeCode) {
                    ref = this.mPackages.get(aInfo.packageName);
                } else {
                    ref = this.mResourcePackages.get(aInfo.packageName);
                }
                String str = null;
                packageInfo = ref != null ? ref.get() : null;
                if (packageInfo == null || (packageInfo.mResources != null && !packageInfo.mResources.getAssets().isUpToDate())) {
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
                    packageInfo = new LoadedApk(this, aInfo, compatInfo, baseLoader, securityViolation, includeCode && (aInfo.flags & 4) != 0, registerPackage);
                    if (this.mSystemThread && ZenModeConfig.SYSTEM_AUTHORITY.equals(aInfo.packageName)) {
                        packageInfo.installSystemApplicationInfo(aInfo, getSystemContext().mPackageInfo.getClassLoader());
                    }
                    if (!differentUser) {
                        if (includeCode) {
                            this.mPackages.put(aInfo.packageName, new WeakReference<>(packageInfo));
                        } else {
                            this.mResourcePackages.put(aInfo.packageName, new WeakReference<>(packageInfo));
                        }
                    }
                }
            } finally {
            }
        }
        return packageInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ApplicationThread getApplicationThread() {
        return this.mAppThread;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Instrumentation getInstrumentation() {
        return this.mInstrumentation;
    }

    public synchronized boolean isProfiling() {
        return (this.mProfiler == null || this.mProfiler.profileFile == null || this.mProfiler.profileFd != null) ? false : true;
    }

    public synchronized String getProfileFilePath() {
        return this.mProfiler.profileFile;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Looper getLooper() {
        return this.mLooper;
    }

    public synchronized Executor getExecutor() {
        return this.mExecutor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Application getApplication() {
        return this.mInitialApplication;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getProcessName() {
        return this.mBoundApplication.processName;
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    public synchronized ContextImpl getSystemUiContext() {
        if (this.mSystemUiContext == null) {
            this.mSystemUiContext = ContextImpl.createSystemUiContext(getSystemContext());
        }
        return this.mSystemUiContext;
    }

    public synchronized void installSystemApplicationInfo(ApplicationInfo info, ClassLoader classLoader) {
        getSystemContext().installSystemApplicationInfo(info, classLoader);
        getSystemUiContext().installSystemApplicationInfo(info, classLoader);
        this.mProfiler = new Profiler();
    }

    synchronized void ensureJitEnabled() {
        if (!this.mJitEnabled) {
            this.mJitEnabled = true;
            VMRuntime.getRuntime().startJitCompilation();
        }
    }

    public private protected void scheduleGcIdler() {
        if (!this.mGcIdlerScheduled) {
            this.mGcIdlerScheduled = true;
            Looper.myQueue().addIdleHandler(this.mGcIdler);
        }
        this.mH.removeMessages(120);
    }

    synchronized void unscheduleGcIdler() {
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

    synchronized void doGcIfNeeded() {
        this.mGcIdlerScheduled = false;
        long now = SystemClock.uptimeMillis();
        if (BinderInternal.getLastGcTime() + 5000 < now) {
            BinderInternal.forceGc("bg");
        }
    }

    static void printRow(PrintWriter pw, String format, Object... objs) {
        pw.println(String.format(format, objs));
    }

    public static synchronized void dumpMemInfoTable(PrintWriter pw, Debug.MemoryInfo memInfo, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, int pid, String processName, long nativeMax, long nativeAllocated, long nativeFree, long dalvikMax, long dalvikAllocated, long dalvikFree) {
        long j;
        int i;
        int i2;
        int otherPss;
        int otherSwappedOutPss;
        int mySharedClean;
        int otherSharedClean;
        int otherPrivateClean;
        int i3;
        int i4;
        int i5 = 0;
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
            while (true) {
                int i6 = i5;
                if (i6 < 17) {
                    pw.print(Debug.MemoryInfo.getOtherLabel(i6));
                    pw.print(',');
                    pw.print(memInfo.getOtherPss(i6));
                    pw.print(',');
                    pw.print(memInfo.getOtherSwappablePss(i6));
                    pw.print(',');
                    pw.print(memInfo.getOtherSharedDirty(i6));
                    pw.print(',');
                    pw.print(memInfo.getOtherSharedClean(i6));
                    pw.print(',');
                    pw.print(memInfo.getOtherPrivateDirty(i6));
                    pw.print(',');
                    pw.print(memInfo.getOtherPrivateClean(i6));
                    pw.print(',');
                    pw.print(memInfo.getOtherSwappedOut(i6));
                    pw.print(',');
                    if (memInfo.hasSwappedOutPss) {
                        pw.print(memInfo.getOtherSwappedOutPss(i6));
                        pw.print(',');
                    } else {
                        pw.print("N/A,");
                    }
                    i5 = i6 + 1;
                } else {
                    return;
                }
            }
        } else {
            if (!dumpSummaryOnly) {
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
                    if (!memInfo.hasSwappedOutPss) {
                        i3 = memInfo.nativeSwappedOut;
                    } else {
                        i3 = memInfo.nativeSwappedOutPss;
                    }
                    objArr2[7] = Integer.valueOf(i3);
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
                    if (!memInfo.hasSwappedOutPss) {
                        i4 = memInfo.dalvikSwappedOut;
                    } else {
                        i4 = memInfo.dalvikSwappedOutPss;
                    }
                    objArr3[7] = Integer.valueOf(i4);
                    j = dalvikMax;
                    objArr3[8] = Long.valueOf(dalvikMax);
                    objArr3[9] = Long.valueOf(dalvikAllocated);
                    objArr3[10] = Long.valueOf(dalvikFree);
                    printRow(pw, HEAP_FULL_COLUMN, objArr3);
                } else {
                    j = dalvikMax;
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
                    if (memInfo.hasSwappedOutPss) {
                        i = memInfo.nativeSwappedOutPss;
                    } else {
                        i = memInfo.nativeSwappedOut;
                    }
                    objArr5[4] = Integer.valueOf(i);
                    objArr5[5] = Long.valueOf(nativeMax);
                    objArr5[6] = Long.valueOf(nativeAllocated);
                    objArr5[7] = Long.valueOf(nativeFree);
                    printRow(pw, HEAP_COLUMN, objArr5);
                    Object[] objArr6 = new Object[8];
                    objArr6[0] = "Dalvik Heap";
                    objArr6[1] = Integer.valueOf(memInfo.dalvikPss);
                    objArr6[2] = Integer.valueOf(memInfo.dalvikPrivateDirty);
                    objArr6[3] = Integer.valueOf(memInfo.dalvikPrivateClean);
                    if (memInfo.hasSwappedOutPss) {
                        i2 = memInfo.dalvikSwappedOutPss;
                    } else {
                        i2 = memInfo.dalvikSwappedOut;
                    }
                    objArr6[4] = Integer.valueOf(i2);
                    objArr6[5] = Long.valueOf(dalvikMax);
                    objArr6[6] = Long.valueOf(dalvikAllocated);
                    objArr6[7] = Long.valueOf(dalvikFree);
                    printRow(pw, HEAP_COLUMN, objArr6);
                }
                int otherPss2 = memInfo.otherPss;
                int otherSwappablePss = memInfo.otherSwappablePss;
                int otherSharedDirty = memInfo.otherSharedDirty;
                int otherPrivateDirty = memInfo.otherPrivateDirty;
                int otherPss3 = memInfo.otherSharedClean;
                int otherSharedClean2 = memInfo.otherPrivateClean;
                int otherPrivateClean2 = memInfo.otherSwappedOut;
                int otherSwappedOut = otherPrivateClean2;
                int otherSwappedOut2 = memInfo.otherSwappedOutPss;
                int otherSwappedOutPss2 = otherSwappedOut2;
                int otherPrivateDirty2 = otherPrivateDirty;
                int otherSharedClean3 = otherPss3;
                int otherPrivateClean3 = otherSharedClean2;
                int otherPrivateDirty3 = otherSharedDirty;
                int otherSharedDirty2 = otherSwappablePss;
                int otherSwappablePss2 = otherPss2;
                for (int otherSwappedOutPss3 = 0; otherSwappedOutPss3 < 17; otherSwappedOutPss3++) {
                    int myPss = memInfo.getOtherPss(otherSwappedOutPss3);
                    int mySwappablePss = memInfo.getOtherSwappablePss(otherSwappedOutPss3);
                    int mySharedDirty = memInfo.getOtherSharedDirty(otherSwappedOutPss3);
                    int myPrivateDirty = memInfo.getOtherPrivateDirty(otherSwappedOutPss3);
                    int mySharedClean2 = memInfo.getOtherSharedClean(otherSwappedOutPss3);
                    int myPrivateClean = memInfo.getOtherPrivateClean(otherSwappedOutPss3);
                    int mySwappedOut = memInfo.getOtherSwappedOut(otherSwappedOutPss3);
                    int mySwappedOutPss = memInfo.getOtherSwappedOutPss(otherSwappedOutPss3);
                    if (myPss == 0 && mySharedDirty == 0 && myPrivateDirty == 0 && mySharedClean2 == 0 && myPrivateClean == 0) {
                        otherSwappedOutPss = otherSwappedOutPss2;
                        if ((memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut) == 0) {
                            otherSwappedOutPss2 = otherSwappedOutPss;
                        }
                    } else {
                        otherSwappedOutPss = otherSwappedOutPss2;
                    }
                    if (dumpFullInfo) {
                        otherSharedClean = otherSharedClean3;
                        otherPrivateClean = otherPrivateClean3;
                        Object[] objArr7 = new Object[11];
                        objArr7[0] = Debug.MemoryInfo.getOtherLabel(otherSwappedOutPss3);
                        objArr7[1] = Integer.valueOf(myPss);
                        objArr7[2] = Integer.valueOf(mySwappablePss);
                        objArr7[3] = Integer.valueOf(mySharedDirty);
                        objArr7[4] = Integer.valueOf(myPrivateDirty);
                        objArr7[5] = Integer.valueOf(mySharedClean2);
                        objArr7[6] = Integer.valueOf(myPrivateClean);
                        objArr7[7] = Integer.valueOf(memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut);
                        mySharedClean = mySharedClean2;
                        objArr7[8] = "";
                        objArr7[9] = "";
                        objArr7[10] = "";
                        printRow(pw, HEAP_FULL_COLUMN, objArr7);
                    } else {
                        mySharedClean = mySharedClean2;
                        otherSharedClean = otherSharedClean3;
                        otherPrivateClean = otherPrivateClean3;
                        Object[] objArr8 = new Object[8];
                        objArr8[0] = Debug.MemoryInfo.getOtherLabel(otherSwappedOutPss3);
                        objArr8[1] = Integer.valueOf(myPss);
                        objArr8[2] = Integer.valueOf(myPrivateDirty);
                        objArr8[3] = Integer.valueOf(myPrivateClean);
                        objArr8[4] = Integer.valueOf(memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut);
                        objArr8[5] = "";
                        objArr8[6] = "";
                        objArr8[7] = "";
                        printRow(pw, HEAP_COLUMN, objArr8);
                    }
                    otherSwappablePss2 -= myPss;
                    otherSharedDirty2 -= mySwappablePss;
                    otherPrivateDirty3 -= mySharedDirty;
                    otherPrivateDirty2 -= myPrivateDirty;
                    otherSharedClean3 = otherSharedClean - mySharedClean;
                    otherPrivateClean3 = otherPrivateClean - myPrivateClean;
                    otherSwappedOut -= mySwappedOut;
                    otherSwappedOutPss2 = otherSwappedOutPss - mySwappedOutPss;
                }
                int otherSharedClean4 = otherSharedClean3;
                int otherPrivateClean4 = otherPrivateClean3;
                int otherSwappedOutPss4 = otherSwappedOutPss2;
                if (dumpFullInfo) {
                    Object[] objArr9 = new Object[11];
                    objArr9[0] = "Unknown";
                    objArr9[1] = Integer.valueOf(otherSwappablePss2);
                    objArr9[2] = Integer.valueOf(otherSharedDirty2);
                    objArr9[3] = Integer.valueOf(otherPrivateDirty3);
                    objArr9[4] = Integer.valueOf(otherPrivateDirty2);
                    objArr9[5] = Integer.valueOf(otherSharedClean4);
                    objArr9[6] = Integer.valueOf(otherPrivateClean4);
                    objArr9[7] = Integer.valueOf(memInfo.hasSwappedOutPss ? otherSwappedOutPss4 : otherSwappedOut);
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
                    objArr10[8] = Long.valueOf(nativeMax + j);
                    objArr10[9] = Long.valueOf(nativeAllocated + dalvikAllocated);
                    objArr10[10] = Long.valueOf(nativeFree + dalvikFree);
                    printRow(pw, HEAP_FULL_COLUMN, objArr10);
                } else {
                    Object[] objArr11 = new Object[8];
                    objArr11[0] = "Unknown";
                    objArr11[1] = Integer.valueOf(otherSwappablePss2);
                    objArr11[2] = Integer.valueOf(otherPrivateDirty2);
                    objArr11[3] = Integer.valueOf(otherPrivateClean4);
                    objArr11[4] = Integer.valueOf(memInfo.hasSwappedOutPss ? otherSwappedOutPss4 : otherSwappedOut);
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
                    objArr12[5] = Long.valueOf(nativeMax + j);
                    objArr12[6] = Long.valueOf(nativeAllocated + dalvikAllocated);
                    objArr12[7] = Long.valueOf(nativeFree + dalvikFree);
                    printRow(pw, HEAP_COLUMN, objArr12);
                }
                if (dumpDalvik) {
                    pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    pw.println(" Dalvik Details");
                    int i7 = 17;
                    while (true) {
                        int i8 = i7;
                        if (i8 >= 31) {
                            break;
                        }
                        int myPss2 = memInfo.getOtherPss(i8);
                        int mySwappablePss2 = memInfo.getOtherSwappablePss(i8);
                        int mySharedDirty2 = memInfo.getOtherSharedDirty(i8);
                        int myPrivateDirty2 = memInfo.getOtherPrivateDirty(i8);
                        int mySharedClean3 = memInfo.getOtherSharedClean(i8);
                        int myPrivateClean2 = memInfo.getOtherPrivateClean(i8);
                        int mySwappedOut2 = memInfo.getOtherSwappedOut(i8);
                        int mySwappedOutPss2 = memInfo.getOtherSwappedOutPss(i8);
                        if (myPss2 == 0 && mySharedDirty2 == 0 && myPrivateDirty2 == 0 && mySharedClean3 == 0 && myPrivateClean2 == 0) {
                            if ((memInfo.hasSwappedOutPss ? mySwappedOutPss2 : mySwappedOut2) == 0) {
                                otherPss = otherSwappablePss2;
                                i7 = i8 + 1;
                                otherSwappablePss2 = otherPss;
                            }
                        }
                        if (dumpFullInfo) {
                            otherPss = otherSwappablePss2;
                            Object[] objArr13 = new Object[11];
                            objArr13[0] = Debug.MemoryInfo.getOtherLabel(i8);
                            objArr13[1] = Integer.valueOf(myPss2);
                            objArr13[2] = Integer.valueOf(mySwappablePss2);
                            objArr13[3] = Integer.valueOf(mySharedDirty2);
                            objArr13[4] = Integer.valueOf(myPrivateDirty2);
                            objArr13[5] = Integer.valueOf(mySharedClean3);
                            objArr13[6] = Integer.valueOf(myPrivateClean2);
                            objArr13[7] = Integer.valueOf(memInfo.hasSwappedOutPss ? mySwappedOutPss2 : mySwappedOut2);
                            objArr13[8] = "";
                            objArr13[9] = "";
                            objArr13[10] = "";
                            printRow(pw, HEAP_FULL_COLUMN, objArr13);
                        } else {
                            otherPss = otherSwappablePss2;
                            Object[] objArr14 = new Object[8];
                            objArr14[0] = Debug.MemoryInfo.getOtherLabel(i8);
                            objArr14[1] = Integer.valueOf(myPss2);
                            objArr14[2] = Integer.valueOf(myPrivateDirty2);
                            objArr14[3] = Integer.valueOf(myPrivateClean2);
                            objArr14[4] = Integer.valueOf(memInfo.hasSwappedOutPss ? mySwappedOutPss2 : mySwappedOut2);
                            objArr14[5] = "";
                            objArr14[6] = "";
                            objArr14[7] = "";
                            printRow(pw, HEAP_COLUMN, objArr14);
                        }
                        i7 = i8 + 1;
                        otherSwappablePss2 = otherPss;
                    }
                }
            }
            pw.println(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            pw.println(" App Summary");
            printRow(pw, ONE_COUNT_COLUMN_HEADER, "", "Pss(KB)");
            printRow(pw, ONE_COUNT_COLUMN_HEADER, "", "------");
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
    }

    private static synchronized void dumpMemoryInfo(ProtoOutputStream proto, long fieldId, String name, int pss, int cleanPss, int sharedDirty, int privateDirty, int sharedClean, int privateClean, boolean hasSwappedOutPss, int dirtySwap, int dirtySwapPss) {
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

    public static synchronized void dumpMemInfoTable(ProtoOutputStream proto, Debug.MemoryInfo memInfo, boolean dumpDalvik, boolean dumpSummaryOnly, long nativeMax, long nativeAllocated, long nativeFree, long dalvikMax, long dalvikAllocated, long dalvikFree) {
        int i;
        long tToken;
        long dvToken;
        long j;
        int i2;
        long j2;
        long j3;
        Debug.MemoryInfo memoryInfo = memInfo;
        if (!dumpSummaryOnly) {
            long nhToken = proto.start(1146756268035L);
            dumpMemoryInfo(proto, 1146756268033L, "Native Heap", memoryInfo.nativePss, memoryInfo.nativeSwappablePss, memoryInfo.nativeSharedDirty, memoryInfo.nativePrivateDirty, memoryInfo.nativeSharedClean, memoryInfo.nativePrivateClean, memoryInfo.hasSwappedOutPss, memoryInfo.nativeSwappedOut, memoryInfo.nativeSwappedOutPss);
            proto.write(1120986464258L, nativeMax);
            proto.write(1120986464259L, nativeAllocated);
            proto.write(1120986464260L, nativeFree);
            proto.end(nhToken);
            long dvToken2 = proto.start(1146756268036L);
            memoryInfo = memInfo;
            dumpMemoryInfo(proto, 1146756268033L, "Dalvik Heap", memInfo.dalvikPss, memInfo.dalvikSwappablePss, memInfo.dalvikSharedDirty, memInfo.dalvikPrivateDirty, memInfo.dalvikSharedClean, memInfo.dalvikPrivateClean, memInfo.hasSwappedOutPss, memInfo.dalvikSwappedOut, memInfo.dalvikSwappedOutPss);
            long j4 = dalvikMax;
            proto.write(1120986464258L, j4);
            long j5 = dalvikAllocated;
            proto.write(1120986464259L, j5);
            long j6 = dalvikFree;
            proto.write(1120986464260L, j6);
            long dvToken3 = dvToken2;
            proto.end(dvToken3);
            int otherPss = memoryInfo.otherPss;
            int otherSwappablePss = memoryInfo.otherSwappablePss;
            int otherSharedDirty = memoryInfo.otherSharedDirty;
            int otherPrivateDirty = memoryInfo.otherPrivateDirty;
            int otherSharedClean = memoryInfo.otherSharedClean;
            int otherPrivateClean = memoryInfo.otherPrivateClean;
            int otherPss2 = otherPss;
            int otherPss3 = memoryInfo.otherSwappedOut;
            int otherSwappedOut = otherPss3;
            int otherSwappedOut2 = memoryInfo.otherSwappedOutPss;
            int myPss = 0;
            int otherSwappedOutPss = otherSwappedOut2;
            int otherSharedDirty2 = otherSharedDirty;
            int otherPrivateDirty2 = otherPrivateDirty;
            int otherSharedClean2 = otherSharedClean;
            int otherPrivateClean2 = otherPrivateClean;
            while (true) {
                int otherPrivateClean3 = myPss;
                if (otherPrivateClean3 >= 17) {
                    break;
                }
                int myPss2 = memoryInfo.getOtherPss(otherPrivateClean3);
                int mySwappablePss = memoryInfo.getOtherSwappablePss(otherPrivateClean3);
                int mySharedDirty = memoryInfo.getOtherSharedDirty(otherPrivateClean3);
                int myPrivateDirty = memoryInfo.getOtherPrivateDirty(otherPrivateClean3);
                int mySharedClean = memoryInfo.getOtherSharedClean(otherPrivateClean3);
                int myPrivateClean = memoryInfo.getOtherPrivateClean(otherPrivateClean3);
                int mySwappedOut = memoryInfo.getOtherSwappedOut(otherPrivateClean3);
                int mySwappedOutPss = memoryInfo.getOtherSwappedOutPss(otherPrivateClean3);
                if (myPss2 == 0 && mySharedDirty == 0 && myPrivateDirty == 0 && mySharedClean == 0 && myPrivateClean == 0) {
                    if ((memoryInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut) == 0) {
                        dvToken = dvToken3;
                        j = j6;
                        i2 = otherPrivateClean3;
                        j2 = j5;
                        j3 = j4;
                        myPss = i2 + 1;
                        dvToken3 = dvToken;
                        j6 = j;
                        j5 = j2;
                        j4 = j3;
                    }
                }
                dvToken = dvToken3;
                j = j6;
                i2 = otherPrivateClean3;
                j2 = j5;
                j3 = j4;
                dumpMemoryInfo(proto, 2246267895813L, Debug.MemoryInfo.getOtherLabel(otherPrivateClean3), myPss2, mySwappablePss, mySharedDirty, myPrivateDirty, mySharedClean, myPrivateClean, memoryInfo.hasSwappedOutPss, mySwappedOut, mySwappedOutPss);
                otherPss2 -= myPss2;
                otherSwappablePss -= mySwappablePss;
                otherSharedDirty2 -= mySharedDirty;
                otherPrivateDirty2 -= myPrivateDirty;
                otherSharedClean2 -= mySharedClean;
                otherPrivateClean2 -= myPrivateClean;
                otherSwappedOut -= mySwappedOut;
                otherSwappedOutPss -= mySwappedOutPss;
                myPss = i2 + 1;
                dvToken3 = dvToken;
                j6 = j;
                j5 = j2;
                j4 = j3;
            }
            long j7 = j4;
            int myPss3 = 17;
            dumpMemoryInfo(proto, 1146756268038L, "Unknown", otherPss2, otherSwappablePss, otherSharedDirty2, otherPrivateDirty2, otherSharedClean2, otherPrivateClean2, memoryInfo.hasSwappedOutPss, otherSwappedOut, otherSwappedOutPss);
            long tToken2 = proto.start(1146756268039L);
            dumpMemoryInfo(proto, 1146756268033L, "TOTAL", memInfo.getTotalPss(), memInfo.getTotalSwappablePss(), memInfo.getTotalSharedDirty(), memInfo.getTotalPrivateDirty(), memInfo.getTotalSharedClean(), memInfo.getTotalPrivateClean(), memoryInfo.hasSwappedOutPss, memInfo.getTotalSwappedOut(), memInfo.getTotalSwappedOutPss());
            proto.write(1120986464258L, nativeMax + j7);
            proto.write(1120986464259L, nativeAllocated + j5);
            proto.write(1120986464260L, nativeFree + j6);
            long tToken3 = tToken2;
            proto.end(tToken3);
            if (dumpDalvik) {
                while (true) {
                    int i3 = myPss3;
                    if (i3 >= 31) {
                        break;
                    }
                    int myPss4 = memoryInfo.getOtherPss(i3);
                    int mySwappablePss2 = memoryInfo.getOtherSwappablePss(i3);
                    int mySharedDirty2 = memoryInfo.getOtherSharedDirty(i3);
                    int myPrivateDirty2 = memoryInfo.getOtherPrivateDirty(i3);
                    int mySharedClean2 = memoryInfo.getOtherSharedClean(i3);
                    int myPrivateClean2 = memoryInfo.getOtherPrivateClean(i3);
                    int mySwappedOut2 = memoryInfo.getOtherSwappedOut(i3);
                    int mySwappedOutPss2 = memoryInfo.getOtherSwappedOutPss(i3);
                    if (myPss4 == 0 && mySharedDirty2 == 0 && myPrivateDirty2 == 0 && mySharedClean2 == 0 && myPrivateClean2 == 0) {
                        if ((memoryInfo.hasSwappedOutPss ? mySwappedOutPss2 : mySwappedOut2) == 0) {
                            i = i3;
                            tToken = tToken3;
                            myPss3 = i + 1;
                            tToken3 = tToken;
                        }
                    }
                    i = i3;
                    tToken = tToken3;
                    dumpMemoryInfo(proto, 2246267895816L, Debug.MemoryInfo.getOtherLabel(i3), myPss4, mySwappablePss2, mySharedDirty2, myPrivateDirty2, mySharedClean2, myPrivateClean2, memoryInfo.hasSwappedOutPss, mySwappedOut2, mySwappedOutPss2);
                    myPss3 = i + 1;
                    tToken3 = tToken;
                }
            }
        }
        long asToken = proto.start(1146756268041L);
        proto.write(1120986464257L, memInfo.getSummaryJavaHeap());
        proto.write(1120986464258L, memInfo.getSummaryNativeHeap());
        proto.write(1120986464259L, memInfo.getSummaryCode());
        proto.write(1120986464260L, memInfo.getSummaryStack());
        proto.write(1120986464261L, memInfo.getSummaryGraphics());
        proto.write(1120986464262L, memInfo.getSummaryPrivateOther());
        proto.write(1120986464263L, memInfo.getSummarySystem());
        if (memoryInfo.hasSwappedOutPss) {
            proto.write(1120986464264L, memInfo.getSummaryTotalSwapPss());
        } else {
            proto.write(1120986464264L, memInfo.getSummaryTotalSwap());
        }
        proto.end(asToken);
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterOnActivityPausedListener(Activity activity, OnActivityPausedListener listener) {
        synchronized (this.mOnPauseListeners) {
            ArrayList<OnActivityPausedListener> list = this.mOnPauseListeners.get(activity);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    public final synchronized ActivityInfo resolveActivityInfo(Intent intent) {
        ActivityInfo aInfo = intent.resolveActivityInfo(this.mInitialApplication.getPackageManager(), 1024);
        if (aInfo == null) {
            Instrumentation.checkStartActivityResult(-92, intent);
        }
        return aInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Activity startActivityNow(Activity parent, String id, Intent intent, ActivityInfo activityInfo, IBinder token, Bundle state, Activity.NonConfigurationInstances lastNonConfigurationInstances) {
        String name;
        ActivityClientRecord r = new ActivityClientRecord();
        r.token = token;
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

    private protected final Activity getActivity(IBinder token) {
        return this.mActivities.get(token).activity;
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized ActivityClientRecord getActivityClient(IBinder token) {
        return this.mActivities.get(token);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void sendActivityResult(IBinder token, String id, int requestCode, int resultCode, Intent data) {
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
    synchronized TransactionExecutor getTransactionExecutor() {
        return this.mTransactionExecutor;
    }

    @Override // android.app.ClientTransactionHandler
    synchronized void sendMessage(int what, Object obj) {
        sendMessage(what, obj, 0, 0, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendMessage(int what, Object obj, int arg1) {
        sendMessage(what, obj, arg1, 0, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendMessage(int what, Object obj, int arg1, int arg2) {
        sendMessage(what, obj, arg1, arg2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendMessage(int what, Object obj, int arg1, int arg2, boolean async) {
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

    private synchronized void sendMessage(int what, Object obj, int arg1, int arg2, int seq) {
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
    public final synchronized void scheduleContextCleanup(ContextImpl context, String who, String what) {
        ContextCleanupInfo cci = new ContextCleanupInfo();
        cci.context = context;
        cci.who = who;
        cci.what = what;
        sendMessage(119, cci);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:105:0x02a2  */
    /* JADX WARN: Type inference failed for: r1v15, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v38, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v27, types: [android.app.Instrumentation] */
    /* JADX WARN: Type inference failed for: r4v14 */
    /* JADX WARN: Type inference failed for: r4v15 */
    /* JADX WARN: Type inference failed for: r4v18 */
    /* JADX WARN: Type inference failed for: r4v21 */
    /* JADX WARN: Type inference failed for: r4v28 */
    /* JADX WARN: Type inference failed for: r4v30, types: [android.content.Intent] */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Type inference failed for: r4v5, types: [android.app.ActivityThread] */
    /* JADX WARN: Type inference failed for: r4v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized android.app.Activity performLaunchActivity(android.app.ActivityThread.ActivityClientRecord r32, android.content.Intent r33) {
        /*
            Method dump skipped, instructions count: 765
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.performLaunchActivity(android.app.ActivityThread$ActivityClientRecord, android.content.Intent):android.app.Activity");
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized void handleStartActivity(ActivityClientRecord r, PendingTransactionActions pendingActions) {
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

    private synchronized void checkAndBlockForNetworkAccess() {
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

    private synchronized ContextImpl createBaseContextForActivity(ActivityClientRecord r) {
        int[] displayIds;
        try {
            int displayId = ActivityManager.getService().getActivityDisplayId(r.token);
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
    public synchronized Activity handleLaunchActivity(ActivityClientRecord r, PendingTransactionActions pendingActions, Intent customIntent) {
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
        if (!ThreadedRenderer.sRendererDisabled) {
            GraphicsEnvironment.earlyInitEGL();
        }
        WindowManagerGlobal.initialize();
        Activity a = performLaunchActivity(r, customIntent);
        if (a == null) {
            try {
                ActivityManager.getService().finishActivity(r.token, 0, null, 0);
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        } else {
            r.createdConfig = new Configuration(this.mConfiguration);
            reportSizeConfigurations(r);
            if (!r.activity.mFinished && pendingActions != null) {
                pendingActions.setOldState(r.state);
                pendingActions.setRestoreInstanceState(true);
                pendingActions.setCallOnPostCreate(true);
            }
        }
        return a;
    }

    private synchronized void reportSizeConfigurations(ActivityClientRecord r) {
        Configuration[] configurations = r.activity.getResources().getSizeConfigurations();
        if (configurations == null) {
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
        if (r == null) {
            return;
        }
        try {
            if (r.token == null) {
                return;
            }
            ActivityManager.getService().reportSizeConfigurations(r.token, horizontal.copyKeys(), vertical.copyKeys(), smallest.copyKeys());
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
    }

    private synchronized void deliverNewIntents(ActivityClientRecord r, List<ReferrerIntent> intents) {
        int N = intents.size();
        for (int i = 0; i < N; i++) {
            ReferrerIntent intent = intents.get(i);
            intent.setExtrasClassLoader(r.activity.getClassLoader());
            intent.prepareToEnterProcess();
            r.activity.mFragments.noteStateNotSaved();
            this.mInstrumentation.callActivityOnNewIntent(r.activity, intent);
        }
    }

    public private protected void performNewIntents(IBinder token, List<ReferrerIntent> intents, boolean andPause) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r == null) {
            return;
        }
        boolean resumed = !r.paused;
        if (resumed) {
            r.activity.mTemporaryPause = true;
            this.mInstrumentation.callActivityOnPause(r.activity);
        }
        checkAndBlockForNetworkAccess();
        deliverNewIntents(r, intents);
        if (resumed) {
            r.activity.performResume(false, "performNewIntents");
            r.activity.mTemporaryPause = false;
            if (this.mActivityThreadProxy != null) {
                this.mActivityThreadProxy.performResumeActivity(r, this.mH, false, "performNewIntents");
            }
        }
        if (r.paused && andPause) {
            performResumeActivity(token, false, "performNewIntents");
            performPauseActivityIfNeeded(r, "performNewIntents");
        }
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized void handleNewIntent(IBinder token, List<ReferrerIntent> intents, boolean andPause) {
        performNewIntents(token, intents, andPause);
    }

    public synchronized void handleRequestAssistContextExtras(RequestAssistContextExtras cmd) {
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
        Uri referrer = null;
        if (r != null) {
            if (!forAutofill) {
                r.activity.getApplication().dispatchOnProvideAssistData(r.activity, data);
                r.activity.onProvideAssistData(data);
                referrer = r.activity.onProvideReferrer();
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
            }
        }
        Uri referrer2 = referrer;
        if (structure3 == null) {
            structure = new AssistStructure();
        } else {
            structure = structure3;
        }
        structure.setAcquisitionStartTime(startTime);
        structure.setAcquisitionEndTime(SystemClock.uptimeMillis());
        this.mLastAssistStructures.add(new WeakReference<>(structure));
        IActivityManager mgr = ActivityManager.getService();
        try {
            mgr.reportAssistContextExtras(cmd.requestToken, data, structure, content, referrer2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void handleTranslucentConversionComplete(IBinder token, boolean drawComplete) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            r.activity.onTranslucentConversionComplete(drawComplete);
        }
    }

    public synchronized void onNewActivityOptions(IBinder token, ActivityOptions options) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            r.activity.onNewActivityOptions(options);
        }
    }

    public synchronized void handleInstallProvider(ProviderInfo info) {
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            installContentProviders(this.mInitialApplication, Arrays.asList(info));
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleEnterAnimationComplete(IBinder token) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            r.activity.dispatchEnterAnimationComplete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleStartBinderTracking() {
        Binder.enableTracing();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleStopBinderTrackingAndDump(ParcelFileDescriptor fd) {
        try {
            Binder.disableTracing();
            Binder.getTransactionTracker().writeTracesToFile(fd);
        } finally {
            IoUtils.closeQuietly(fd);
            Binder.getTransactionTracker().clearTraces();
        }
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized void handleMultiWindowModeChanged(IBinder token, boolean isInMultiWindowMode, Configuration overrideConfig) {
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
    public synchronized void handlePictureInPictureModeChanged(IBinder token, boolean isInPipMode, Configuration overrideConfig) {
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
    public synchronized void handleLocalVoiceInteractionStarted(IBinder token, IVoiceInteractor interactor) {
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

    private static synchronized boolean attemptAttachAgent(String agent, ClassLoader classLoader) {
        try {
            VMDebug.attachAgent(agent, classLoader);
            return true;
        } catch (IOException e) {
            Slog.e(TAG, "Attaching agent with " + classLoader + " failed: " + agent);
            return false;
        }
    }

    static synchronized void handleAttachAgent(String agent, LoadedApk loadedApk) {
        ClassLoader classLoader = loadedApk != null ? loadedApk.getClassLoader() : null;
        if (!attemptAttachAgent(agent, classLoader) && classLoader != null) {
            attemptAttachAgent(agent, null);
        }
    }

    public static synchronized Intent getIntentBeingBroadcast() {
        return sCurrentBroadcastIntent.get();
    }

    /* JADX INFO: Access modifiers changed from: public */
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
            } catch (Throwable th) {
                sCurrentBroadcastIntent.set(null);
                throw th;
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
    public synchronized void handleCreateBackupAgent(CreateBackupAgentData data) {
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
                BackupAgent agent = this.mBackupAgents.get(packageName);
                if (agent != null) {
                    IBinder binder2 = agent.onBind();
                    binder = binder2;
                } else {
                    try {
                        ClassLoader cl = packageInfo.getClassLoader();
                        BackupAgent agent2 = (BackupAgent) cl.loadClass(classname).newInstance();
                        ContextImpl context = ContextImpl.createAppContext(this, packageInfo);
                        context.setOuterContext(agent2);
                        agent2.attach(context);
                        agent2.onCreate();
                        binder = agent2.onBind();
                        this.mBackupAgents.put(packageName, agent2);
                    } catch (Exception e) {
                        Slog.e(TAG, "Agent threw during creation: " + e);
                        if (data.backupMode != 2 && data.backupMode != 3) {
                            throw e;
                        }
                    }
                }
                try {
                    ActivityManager.getService().backupAgentCreated(packageName, binder);
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
    public synchronized void handleDestroyBackupAgent(CreateBackupAgentData data) {
        LoadedApk packageInfo = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
        String packageName = packageInfo.mPackageName;
        BackupAgent agent = this.mBackupAgents.get(packageName);
        if (agent != null) {
            try {
                agent.onDestroy();
            } catch (Exception e) {
                Slog.w(TAG, "Exception thrown in onDestroy by backup agent of " + data.appInfo);
                e.printStackTrace();
            }
            this.mBackupAgents.remove(packageName);
            return;
        }
        Slog.w(TAG, "Attempt to destroy unknown backup agent " + data);
    }

    /* JADX INFO: Access modifiers changed from: public */
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
    public synchronized void handleBindService(BindServiceData data) {
        Service s = this.mServices.get(data.token);
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
                    ensureJitEnabled();
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
    public synchronized void handleUnbindService(BindServiceData data) {
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
    public synchronized void handleDumpService(DumpComponentInfo info) {
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
    public void handlePerformanceAnalyze(IBinder token) {
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            ActivityClientRecord r = this.mActivities.get(token);
            if (r != null && r.activity != null && r.window != null) {
                r.window = r.activity.getWindow();
                View decor = r.window.getDecorView();
                ViewRootImpl impl = decor.getViewRootImpl();
                if (impl != null) {
                    Bitmap bitmap = takeScreenshotBitmap();
                    String[] nameData = r.activityInfo.name.split("\\.");
                    if (nameData.length > 3) {
                        String fileName = nameData[2] + nameData[nameData.length - 1];
                        float number = impl.countOverDraw();
                        if (number > 3.2d) {
                            takeScreenshot(bitmap, fileName, number);
                        }
                        int depth = impl.getMaxDepth();
                        if (depth > 25) {
                            File depthOut = new File(getDirectory(mViewHierarchyDirectory, VIEWHIERARCHY_DIR) + "/" + fileName + "_" + depth + ".txt");
                            impl.dumpDepth(depthOut);
                        }
                        Log.v(TAG, "pause activity Name = " + r.activityInfo.name + "=overdraw = " + number + "viewHierarchy = " + depth);
                        writeMapToFile(r.activityInfo.name, number, depth);
                    }
                }
            }
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void writeMapToFile(String name, float number, int depth) {
        FileWriter overdrawHierarchy = null;
        try {
            overdrawHierarchy = new FileWriter(getDirectory(mOverdrawDirectory, OVERDAW_DIR) + "/activityDrawResult.txt", true);
            PrintWriter writer = new PrintWriter(overdrawHierarchy);
            String result = name + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + number + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + depth;
            writer.println(result);
            writer.close();
        } catch (IOException e) {
        } catch (Throwable th) {
            IoUtils.closeQuietly(overdrawHierarchy);
            throw th;
        }
        IoUtils.closeQuietly(overdrawHierarchy);
    }

    private static boolean takeScreenshot(Bitmap bitmap, String fileName, float number) {
        String numberString = String.format("%10.2f", Float.valueOf(number));
        File overdrawOut = new File(getDirectory(mOverdrawDirectory, OVERDAW_DIR) + "/" + fileName + "_" + numberString + ".png");
        if (bitmap == null) {
            return false;
        }
        try {
            return bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(overdrawOut));
        } catch (IOException e) {
            return false;
        } finally {
            bitmap.recycle();
        }
    }

    static Bitmap takeScreenshotBitmap() {
        Display display = DisplayManagerGlobal.getInstance().getRealDisplay(0);
        Point displaySize = new Point();
        display.getRealSize(displaySize);
        int displayWidth = displaySize.x;
        int displayHeight = displaySize.y;
        int rotation = display.getRotation();
        Rect crop = new Rect(0, 0, displayWidth, displayHeight);
        Bitmap screenShot = SurfaceControl.screenshot(crop, displayWidth, displayHeight, rotation);
        if (screenShot == null) {
            return null;
        }
        screenShot.setHasAlpha(false);
        return screenShot;
    }

    private static File getDirectory(File directory, String path) {
        if (directory == null) {
            File storage = Environment.getExternalStorageDirectory();
            directory = new File(storage, path);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new RuntimeException("Failed to create a screenshot directory.");
            }
        }
        return directory;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleDumpActivity(DumpComponentInfo info) {
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
    public synchronized void handleDumpProvider(DumpComponentInfo info) {
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
    public synchronized void handleServiceArgs(ServiceArgsData data) {
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
                    ensureJitEnabled();
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
    public synchronized void handleStopService(IBinder token) {
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
    public synchronized ActivityClientRecord performResumeActivity(IBinder token, boolean finalStateRequest, String reason) {
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
        } catch (Exception e2) {
            if (!this.mInstrumentation.onException(r.activity, e2)) {
                throw new RuntimeException("Unable to resume activity " + r.intent.getComponent().toShortString() + ": " + e2.toString(), e2);
            }
        }
        if (this.mActivityThreadProxy != null) {
            this.mActivityThreadProxy.performResumeActivity(r, this.mH, finalStateRequest, reason);
        }
        return r;
    }

    static final synchronized void cleanUpPendingRemoveWindows(ActivityClientRecord r, boolean force) {
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
    public synchronized void handleResumeActivity(IBinder token, boolean finalStateRequest, boolean isForward, String reason) {
        int forwardBit;
        unscheduleGcIdler();
        this.mSomeActivitiesChanged = true;
        ActivityClientRecord r = performResumeActivity(token, finalStateRequest, reason);
        if (r == null) {
            return;
        }
        Activity a = r.activity;
        if (localLOGV) {
            Slog.v(TAG, "Resume " + r + " started activity: " + a.mStartedActivity + ", hideForNow: " + r.hideForNow + ", finished: " + a.mFinished);
        }
        if (!isForward) {
            forwardBit = 0;
        } else {
            forwardBit = 256;
        }
        boolean willBeVisible = !a.mStartedActivity;
        if (!willBeVisible) {
            try {
                willBeVisible = ActivityManager.getService().willActivityBeVisible(a.getActivityToken());
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
            WindowManager.LayoutParams lp = xpWindowManager.getOverrideLayoutParams(r);
            if (lp != null) {
                l.copyFrom(lp);
                r.window.setAttributes(l);
                r.activity.getWindow().setAttributes(l);
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
                r.newConfig = null;
            }
            if (localLOGV) {
                Slog.v(TAG, "Resuming " + r + " with isForward=" + isForward);
            }
            WindowManager.LayoutParams l2 = r.window.getAttributes();
            l2.softInputMode = (l2.softInputMode & (-257)) | forwardBit;
            if (r.activity.mVisibleFromClient) {
                ViewManager wm2 = a.getWindowManager();
                wm2.updateViewLayout(r.window.getDecorView(), l2);
            }
            r.activity.mVisibleFromServer = true;
            this.mNumVisibleActivities++;
            if (r.activity.mVisibleFromClient) {
                r.activity.makeVisible();
            }
        }
        r.nextIdle = this.mNewActivities;
        this.mNewActivities = r;
        if (this.mActivityThreadProxy != null) {
            this.mActivityThreadProxy.handleResumeActivity(r, this.mH, reason);
        }
        if (localLOGV) {
            Slog.v(TAG, "Scheduling idle handler for " + r);
        }
        Looper.myQueue().addIdleHandler(new Idler());
    }

    @Override // android.app.ClientTransactionHandler
    public void handlePauseActivity(IBinder token, boolean finished, boolean userLeaving, int configChanges, int resumingFlags, PendingTransactionActions pendingActions, String reason) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            if (SHOWOVERDRAW) {
                performanceAnalyze(token);
            }
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
        if (this.mActivityThreadProxy != null) {
            this.mActivityThreadProxy.handlePauseActivity(r, finished, userLeaving, configChanges, resumingFlags, reason);
        }
    }

    final synchronized void performUserLeavingActivity(ActivityClientRecord r) {
        this.mInstrumentation.callActivityOnUserLeaving(r.activity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized Bundle performPauseActivity(IBinder token, boolean finished, String reason, PendingTransactionActions pendingActions) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            return performPauseActivity(r, finished, reason, pendingActions);
        }
        return null;
    }

    private synchronized Bundle performPauseActivity(ActivityClientRecord r, boolean finished, String reason, PendingTransactionActions pendingActions) {
        ArrayList<OnActivityPausedListener> listeners;
        if (this.mActivityThreadProxy != null) {
            this.mActivityThreadProxy.performPauseActivity(r, finished, reason);
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

    private synchronized void performPauseActivityIfNeeded(ActivityClientRecord r, String reason) {
        if (r.paused) {
            return;
        }
        if (this.mActivityThreadProxy != null) {
            this.mActivityThreadProxy.performPauseActivityIfNeeded(r, reason);
        }
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

    public private protected final void performStopActivity(IBinder token, boolean saveState, String reason) {
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

        synchronized ProviderRefCount(ContentProviderHolder inHolder, ProviderClientRecord inClient, int sCount, int uCount) {
            this.holder = inHolder;
            this.client = inClient;
            this.stableCount = sCount;
            this.unstableCount = uCount;
        }
    }

    private synchronized void performStopActivityInner(ActivityClientRecord r, PendingTransactionActions.StopInfo info, boolean keepShown, boolean saveState, boolean finalStateRequest, String reason) {
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

    private synchronized void callActivityOnStop(ActivityClientRecord r, boolean saveState, String reason) {
        boolean shouldSaveState = saveState && !r.activity.mFinished && r.state == null && !r.isPreHoneycomb();
        boolean isPreP = r.isPreP();
        if (shouldSaveState && isPreP) {
            callActivityOnSaveInstanceState(r);
        }
        try {
            r.activity.performStop(false, reason);
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
        if (xpActivityManager.isHomeActivity(r)) {
            try {
                IActivityManager am = xpActivityManager.getActivityManager();
                am.setHomeState(r.getActivityInfo().getComponentName(), 5);
            } catch (Exception e3) {
            }
        }
    }

    private synchronized void updateVisibility(ActivityClientRecord r, boolean show) {
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
    public synchronized void handleStopActivity(IBinder token, boolean show, int configChanges, PendingTransactionActions pendingActions, boolean finalStateRequest, String reason) {
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
            l.softInputMode &= -257;
            r.window.setAttributes(l);
        }
        stopInfo.setActivity(r);
        stopInfo.setState(r.state);
        stopInfo.setPersistentState(r.persistentState);
        pendingActions.setStopInfo(stopInfo);
        this.mSomeActivitiesChanged = true;
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized void reportStop(PendingTransactionActions pendingActions) {
        this.mH.post(pendingActions.getStopInfo());
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized void performRestartActivity(IBinder token, boolean start) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r.stopped) {
            r.activity.performRestart(start, "performRestartActivity");
            if (start) {
                r.setState(2);
            }
        }
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized void handleWindowVisibility(IBinder token, boolean show) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r == null) {
            Log.w(TAG, "handleWindowVisibility: no activity for token " + token);
            return;
        }
        if (!show && !r.stopped) {
            performStopActivityInner(r, null, show, false, false, "handleWindowVisibility");
        } else if (show && r.stopped) {
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
    public synchronized void handleSleeping(IBinder token, boolean sleeping) {
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
                ActivityManager.getService().activitySlept(r.token);
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        } else if (r.stopped && r.activity.mVisibleFromServer) {
            r.activity.performRestart(true, "handleSleeping");
            r.setState(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleSetCoreSettings(Bundle coreSettings) {
        synchronized (this.mResourcesManager) {
            this.mCoreSettings = coreSettings;
        }
        onCoreSettingsChange();
    }

    private synchronized void onCoreSettingsChange() {
        boolean debugViewAttributes = this.mCoreSettings.getInt(Settings.Global.DEBUG_VIEW_ATTRIBUTES, 0) != 0;
        if (debugViewAttributes != View.mDebugViewAttributes) {
            View.mDebugViewAttributes = debugViewAttributes;
            relaunchAllActivities();
        }
    }

    private synchronized void relaunchAllActivities() {
        for (Map.Entry<IBinder, ActivityClientRecord> entry : this.mActivities.entrySet()) {
            Activity activity = entry.getValue().activity;
            if (!activity.mFinished) {
                scheduleRelaunchActivity(entry.getKey());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleUpdatePackageCompatibilityInfo(UpdateCompatibilityData data) {
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

    private synchronized void deliverResults(ActivityClientRecord r, List<ResultInfo> results, String reason) {
        int N = results.size();
        for (int i = 0; i < N; i++) {
            ResultInfo ri = results.get(i);
            try {
                if (ri.mData != null) {
                    ri.mData.setExtrasClassLoader(r.activity.getClassLoader());
                    ri.mData.prepareToEnterProcess();
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
    public synchronized void handleSendResult(IBinder token, List<ResultInfo> results, String reason) {
        ActivityClientRecord r = this.mActivities.get(token);
        if (r != null) {
            boolean resumed = !r.paused;
            if (!r.activity.mFinished && r.activity.mDecor != null && r.hideForNow && resumed) {
                updateVisibility(r, true);
            }
            if (resumed) {
                try {
                    r.activity.mCalled = false;
                    r.activity.mTemporaryPause = true;
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
                r.activity.mTemporaryPause = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ActivityClientRecord performDestroyActivity(IBinder token, boolean finishing, int configChanges, boolean getNonConfigInstance, String reason) {
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
        this.mActivities.remove(token);
        StrictMode.decrementExpectedActivityCount(cls);
        return r;
    }

    private static synchronized String safeToComponentShortString(Intent intent) {
        ComponentName component = intent.getComponent();
        return component == null ? "[Unknown]" : component.toShortString();
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized void handleDestroyActivity(IBinder token, boolean finishing, int configChanges, boolean getNonConfigInstance, String reason) {
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
                ActivityManager.getService().activityDestroyed(token);
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
        this.mSomeActivitiesChanged = true;
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized ActivityClientRecord prepareRelaunchActivity(IBinder token, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, int configChanges, MergedConfiguration config, boolean preserveWindow) {
        ActivityClientRecord target = null;
        boolean scheduleRelaunch = false;
        synchronized (this.mResourcesManager) {
            int i = 0;
            while (true) {
                if (i >= this.mRelaunchingActivities.size()) {
                    break;
                }
                ActivityClientRecord r = this.mRelaunchingActivities.get(i);
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
    public synchronized void handleRelaunchActivity(ActivityClientRecord tmp, PendingTransactionActions pendingActions) {
        ActivityClientRecord activityClientRecord;
        int configChanges;
        unscheduleGcIdler();
        this.mSomeActivitiesChanged = true;
        Configuration changedConfig = null;
        synchronized (this.mResourcesManager) {
            try {
                int N = this.mRelaunchingActivities.size();
                activityClientRecord = tmp;
                try {
                    IBinder token = activityClientRecord.token;
                    int i = 0;
                    int configChanges2 = 0;
                    ActivityClientRecord tmp2 = null;
                    while (true) {
                        int i2 = i;
                        if (i2 >= N) {
                            break;
                        }
                        try {
                            ActivityClientRecord r = this.mRelaunchingActivities.get(i2);
                            if (r.token == token) {
                                try {
                                    configChanges = r.pendingConfigChanges | configChanges2;
                                } catch (Throwable th) {
                                    e = th;
                                    while (true) {
                                        try {
                                            break;
                                        } catch (Throwable th2) {
                                            e = th2;
                                        }
                                    }
                                    throw e;
                                }
                                try {
                                    this.mRelaunchingActivities.remove(i2);
                                    i2--;
                                    N--;
                                    tmp2 = r;
                                    configChanges2 = configChanges;
                                } catch (Throwable th3) {
                                    e = th3;
                                    while (true) {
                                        break;
                                        break;
                                    }
                                    throw e;
                                }
                            }
                            i = i2 + 1;
                        } catch (Throwable th4) {
                            e = th4;
                        }
                    }
                    if (tmp2 == null) {
                        return;
                    }
                    if (this.mPendingConfiguration != null) {
                        changedConfig = this.mPendingConfiguration;
                        this.mPendingConfiguration = null;
                    }
                    if (tmp2.createdConfig != null && ((this.mConfiguration == null || (tmp2.createdConfig.isOtherSeqNewer(this.mConfiguration) && this.mConfiguration.diff(tmp2.createdConfig) != 0)) && (changedConfig == null || tmp2.createdConfig.isOtherSeqNewer(changedConfig)))) {
                        changedConfig = tmp2.createdConfig;
                    }
                    Configuration changedConfig2 = changedConfig;
                    if (changedConfig2 != null) {
                        this.mCurDefaultDisplayDpi = changedConfig2.densityDpi;
                        updateDefaultDensity();
                        handleConfigurationChanged(changedConfig2, null);
                    }
                    ActivityClientRecord r2 = this.mActivities.get(tmp2.token);
                    if (r2 == null) {
                        return;
                    }
                    r2.activity.mConfigChangeFlags |= configChanges2;
                    r2.mPreserveWindow = tmp2.mPreserveWindow;
                    r2.activity.mChangingConfigurations = true;
                    try {
                        if (r2.mPreserveWindow) {
                            WindowManagerGlobal.getWindowSession().prepareToReplaceWindows(r2.token, true);
                        }
                        handleRelaunchActivityInner(r2, configChanges2, tmp2.pendingResults, tmp2.pendingIntents, pendingActions, tmp2.startsNotResumed, tmp2.overrideConfig, "handleRelaunchActivity");
                        if (pendingActions != null) {
                            pendingActions.setReportRelaunchToWindowManager(true);
                        }
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                } catch (Throwable th5) {
                    e = th5;
                    while (true) {
                        break;
                        break;
                    }
                    throw e;
                }
            } catch (Throwable th6) {
                e = th6;
                activityClientRecord = tmp;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void scheduleRelaunchActivity(IBinder token) {
        sendMessage(160, token);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleRelaunchActivityLocally(IBinder token) {
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

    private synchronized void handleRelaunchActivityInner(ActivityClientRecord r, int configChanges, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingIntents, PendingTransactionActions pendingActions, boolean startsNotResumed, Configuration overrideConfig, String reason) {
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
    public synchronized void reportRelaunch(IBinder token, PendingTransactionActions pendingActions) {
        try {
            ActivityManager.getService().activityRelaunched(token);
            ActivityClientRecord r = this.mActivities.get(token);
            if (pendingActions.shouldReportRelaunchToWindowManager() && r != null && r.window != null) {
                r.window.reportActivityRelaunched();
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private synchronized void callActivityOnSaveInstanceState(ActivityClientRecord r) {
        r.state = new Bundle();
        r.state.setAllowFds(false);
        if (r.isPersistable()) {
            r.persistentState = new PersistableBundle();
            this.mInstrumentation.callActivityOnSaveInstanceState(r.activity, r.state, r.persistentState);
            return;
        }
        this.mInstrumentation.callActivityOnSaveInstanceState(r.activity, r.state);
    }

    synchronized ArrayList<ComponentCallbacks2> collectComponentCallbacks(boolean allActivities, Configuration newConfig) {
        int i;
        ArrayList<ComponentCallbacks2> callbacks = new ArrayList<>();
        synchronized (this.mResourcesManager) {
            int NAPP = this.mAllApplications.size();
            for (int i2 = 0; i2 < NAPP; i2++) {
                callbacks.add(this.mAllApplications.get(i2));
            }
            int NACT = this.mActivities.size();
            for (int i3 = 0; i3 < NACT; i3++) {
                ActivityClientRecord ar = this.mActivities.valueAt(i3);
                Activity a = ar.activity;
                if (a != null) {
                    Configuration thisConfig = applyConfigCompatMainThread(this.mCurDefaultDisplayDpi, newConfig, ar.packageInfo.getCompatibilityInfo());
                    if (!ar.activity.mFinished && (allActivities || !ar.paused)) {
                        callbacks.add(a);
                    } else if (thisConfig != null) {
                        ar.newConfig = thisConfig;
                    }
                }
            }
            int NSVC = this.mServices.size();
            for (int i4 = 0; i4 < NSVC; i4++) {
                callbacks.add(this.mServices.valueAt(i4));
            }
        }
        synchronized (this.mProviderMap) {
            int NPRV = this.mLocalProviders.size();
            for (i = 0; i < NPRV; i++) {
                callbacks.add(this.mLocalProviders.valueAt(i).mLocalProvider);
            }
        }
        return callbacks;
    }

    private synchronized void performConfigurationChangedForActivity(ActivityClientRecord r, Configuration newBaseConfig) {
        performConfigurationChangedForActivity(r, newBaseConfig, r.activity.getDisplay().getDisplayId(), false);
    }

    private synchronized Configuration performConfigurationChangedForActivity(ActivityClientRecord r, Configuration newBaseConfig, int displayId, boolean movedToDifferentDisplay) {
        r.tmpConfig.setTo(newBaseConfig);
        if (r.overrideConfig != null) {
            r.tmpConfig.updateFrom(r.overrideConfig);
        }
        Configuration reportedConfig = performActivityConfigurationChanged(r.activity, r.tmpConfig, r.overrideConfig, displayId, movedToDifferentDisplay);
        freeTextLayoutCachesIfNeeded(r.activity.mCurrentConfig.diff(r.tmpConfig));
        return reportedConfig;
    }

    private static synchronized Configuration createNewConfigAndUpdateIfNotNull(Configuration base, Configuration override) {
        if (override == null) {
            return base;
        }
        Configuration newConfig = new Configuration(base);
        newConfig.updateFrom(override);
        return newConfig;
    }

    private synchronized void performConfigurationChanged(ComponentCallbacks2 cb, Configuration newConfig) {
        Configuration contextThemeWrapperOverrideConfig = null;
        if (cb instanceof ContextThemeWrapper) {
            ContextThemeWrapper contextThemeWrapper = (ContextThemeWrapper) cb;
            contextThemeWrapperOverrideConfig = contextThemeWrapper.getOverrideConfiguration();
        }
        Configuration configToReport = createNewConfigAndUpdateIfNotNull(newConfig, contextThemeWrapperOverrideConfig);
        cb.onConfigurationChanged(configToReport);
    }

    private synchronized Configuration performActivityConfigurationChanged(Activity activity, Configuration newConfig, Configuration amOverrideConfig, int displayId, boolean movedToDifferentDisplay) {
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

    public final synchronized void applyConfigurationToResources(Configuration config) {
        synchronized (this.mResourcesManager) {
            this.mResourcesManager.applyConfigurationToResourcesLocked(config, null);
        }
    }

    final synchronized Configuration applyCompatConfiguration(int displayDensity) {
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
    public synchronized void handleConfigurationChanged(Configuration config) {
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

    private synchronized void handleConfigurationChanged(Configuration config, CompatibilityInfo compat) {
        String packageName = getApplication().getPackageName();
        Configuration config2 = xpActivityManager.getOverrideConfiguration(config, packageName);
        boolean equivalent = (config2 == null || this.mConfiguration == null || this.mConfiguration.diffPublicOnly(config2) != 0) ? false : true;
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

    synchronized void handleApplicationInfoChanged(ApplicationInfo ai) {
        LoadedApk apk;
        LoadedApk resApk;
        synchronized (this.mResourcesManager) {
            WeakReference<LoadedApk> ref = this.mPackages.get(ai.packageName);
            apk = ref != null ? ref.get() : null;
            WeakReference<LoadedApk> ref2 = this.mResourcePackages.get(ai.packageName);
            resApk = ref2 != null ? ref2.get() : null;
        }
        if (apk != null) {
            ArrayList<String> oldPaths = new ArrayList<>();
            LoadedApk.makePaths(this, apk.getApplicationInfo(), oldPaths);
            apk.updateApplicationInfo(ai, oldPaths);
        }
        if (resApk != null) {
            ArrayList<String> oldPaths2 = new ArrayList<>();
            LoadedApk.makePaths(this, resApk.getApplicationInfo(), oldPaths2);
            resApk.updateApplicationInfo(ai, oldPaths2);
        }
        synchronized (this.mResourcesManager) {
            this.mResourcesManager.applyNewResourceDirsLocked(ai.sourceDir, ai.resourceDirs);
        }
        ApplicationPackageManager.configurationChanged();
        Configuration newConfig = new Configuration();
        newConfig.assetsSeq = (this.mConfiguration != null ? this.mConfiguration.assetsSeq : 0) + 1;
        handleConfigurationChanged(newConfig, null);
        relaunchAllActivities();
    }

    static synchronized void freeTextLayoutCachesIfNeeded(int configDiff) {
        if (configDiff != 0) {
            boolean hasLocaleConfigChange = (configDiff & 4) != 0;
            if (hasLocaleConfigChange) {
                Canvas.freeTextLayoutCaches();
            }
        }
    }

    @Override // android.app.ClientTransactionHandler
    public synchronized void handleActivityConfigurationChanged(IBinder activityToken, Configuration overrideConfig, int displayId) {
        ActivityClientRecord r = this.mActivities.get(activityToken);
        if (r == null || r.activity == null) {
            return;
        }
        boolean movedToDifferentDisplay = (displayId == -1 || displayId == r.activity.getDisplay().getDisplayId()) ? false : true;
        r.overrideConfig = overrideConfig;
        ViewRootImpl viewRoot = r.activity.mDecor != null ? r.activity.mDecor.getViewRootImpl() : null;
        if (movedToDifferentDisplay) {
            Configuration reportedConfig = performConfigurationChangedForActivity(r, this.mCompatConfiguration, displayId, true);
            if (viewRoot != null) {
                viewRoot.onMovedToDisplay(displayId, reportedConfig);
            }
        } else {
            performConfigurationChangedForActivity(r, this.mCompatConfiguration);
        }
        if (viewRoot != null) {
            viewRoot.updateConfiguration(displayId);
        }
        this.mSomeActivitiesChanged = true;
    }

    final synchronized void handleProfilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) {
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

    public synchronized void stopProfiling() {
        if (this.mProfiler != null) {
            this.mProfiler.stopProfiling();
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:11:0x0023 -> B:33:0x0078). Please submit an issue!!! */
    static synchronized void handleDumpHeap(DumpHeapData dhd) {
        if (dhd.runGc) {
            System.gc();
            System.runFinalization();
            System.gc();
        }
        try {
            try {
                if (dhd.managed) {
                    try {
                        Debug.dumpHprofData(dhd.path, dhd.fd.getFileDescriptor());
                        dhd.fd.close();
                    } catch (IOException e) {
                        Slog.w(TAG, "Managed heap dump failed on path " + dhd.path + " -- can the process access this path?");
                        dhd.fd.close();
                    }
                } else if (dhd.mallocInfo) {
                    Debug.dumpNativeMallocInfo(dhd.fd.getFileDescriptor());
                } else {
                    Debug.dumpNativeHeap(dhd.fd.getFileDescriptor());
                }
            } catch (IOException e2) {
                Slog.w(TAG, "Failure closing profile fd", e2);
            }
            try {
                ActivityManager.getService().dumpHeapFinished(dhd.path);
            } catch (RemoteException e3) {
                throw e3.rethrowFromSystemServer();
            }
        } catch (Throwable th) {
            try {
                dhd.fd.close();
            } catch (IOException e4) {
                Slog.w(TAG, "Failure closing profile fd", e4);
            }
            throw th;
        }
    }

    final synchronized void handleDispatchPackageBroadcast(int cmd, String[] packages) {
        boolean hasPkgInfo = false;
        if (cmd != 0) {
            switch (cmd) {
                case 2:
                    break;
                case 3:
                    if (packages != null) {
                        synchronized (this.mResourcesManager) {
                            int i = packages.length - 1;
                            while (true) {
                                int i2 = i;
                                if (i2 >= 0) {
                                    WeakReference<LoadedApk> ref = this.mPackages.get(packages[i2]);
                                    LoadedApk pkgInfo = ref != null ? ref.get() : null;
                                    if (pkgInfo != null) {
                                        hasPkgInfo = true;
                                    } else {
                                        WeakReference<LoadedApk> ref2 = this.mResourcePackages.get(packages[i2]);
                                        pkgInfo = ref2 != null ? ref2.get() : null;
                                        if (pkgInfo != null) {
                                            hasPkgInfo = true;
                                        }
                                    }
                                    if (pkgInfo != null) {
                                        try {
                                            String packageName = packages[i2];
                                            ApplicationInfo aInfo = sPackageManager.getApplicationInfo(packageName, 1024, UserHandle.myUserId());
                                            if (this.mActivities.size() > 0) {
                                                for (ActivityClientRecord ar : this.mActivities.values()) {
                                                    if (ar.activityInfo.applicationInfo.packageName.equals(packageName)) {
                                                        ar.activityInfo.applicationInfo = aInfo;
                                                        ar.packageInfo = pkgInfo;
                                                    }
                                                }
                                            }
                                            ArrayList<String> oldPaths = new ArrayList<>();
                                            LoadedApk.makePaths(this, pkgInfo.getApplicationInfo(), oldPaths);
                                            pkgInfo.updateApplicationInfo(aInfo, oldPaths);
                                        } catch (RemoteException e) {
                                        }
                                    }
                                    i = i2 - 1;
                                }
                            }
                        }
                    }
                    ApplicationPackageManager.handlePackageBroadcast(cmd, packages, hasPkgInfo);
                default:
                    ApplicationPackageManager.handlePackageBroadcast(cmd, packages, hasPkgInfo);
            }
        }
        boolean killApp = cmd == 0;
        if (packages != null) {
            synchronized (this.mResourcesManager) {
                int i3 = packages.length - 1;
                while (true) {
                    int i4 = i3;
                    if (i4 >= 0) {
                        if (!hasPkgInfo) {
                            WeakReference<LoadedApk> ref3 = this.mPackages.get(packages[i4]);
                            if (ref3 != null && ref3.get() != null) {
                                hasPkgInfo = true;
                            } else {
                                WeakReference<LoadedApk> ref4 = this.mResourcePackages.get(packages[i4]);
                                if (ref4 != null && ref4.get() != null) {
                                    hasPkgInfo = true;
                                }
                            }
                        }
                        if (killApp) {
                            this.mPackages.remove(packages[i4]);
                            this.mResourcePackages.remove(packages[i4]);
                        }
                        i3 = i4 - 1;
                    }
                }
            }
        }
        ApplicationPackageManager.handlePackageBroadcast(cmd, packages, hasPkgInfo);
    }

    final synchronized void handleLowMemory() {
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
    public synchronized void handleTrimMemory(int level) {
        Trace.traceBegin(64L, "trimMemory");
        ArrayList<ComponentCallbacks2> callbacks = collectComponentCallbacks(true, null);
        int N = callbacks.size();
        for (int i = 0; i < N; i++) {
            callbacks.get(i).onTrimMemory(level);
        }
        WindowManagerGlobal.getInstance().trimMemory(level);
        Trace.traceEnd(64L);
    }

    private synchronized void setupGraphicsSupport(Context context) {
        Trace.traceBegin(64L, "setupGraphicsSupport");
        if (!ZenModeConfig.SYSTEM_AUTHORITY.equals(context.getPackageName())) {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null) {
                System.setProperty("java.io.tmpdir", cacheDir.getAbsolutePath());
            } else {
                Log.v(TAG, "Unable to initialize \"java.io.tmpdir\" property due to missing cache directory");
            }
            Context deviceContext = context.createDeviceProtectedStorageContext();
            File codeCacheDir = deviceContext.getCodeCacheDir();
            if (codeCacheDir != null) {
                try {
                    int uid = Process.myUid();
                    String[] packages = getPackageManager().getPackagesForUid(uid);
                    if (packages != null) {
                        ThreadedRenderer.setupDiskCache(codeCacheDir);
                        RenderScriptCacheDir.setupDiskCache(codeCacheDir);
                    }
                } catch (RemoteException e) {
                    Trace.traceEnd(64L);
                    throw e.rethrowFromSystemServer();
                }
            } else {
                Log.w(TAG, "Unable to use shader/script cache: missing code-cache directory");
            }
        }
        GraphicsEnvironment.getInstance().setup(context);
        Trace.traceEnd(64L);
    }

    private synchronized void updateDefaultDensity() {
        int densityDpi = this.mCurDefaultDisplayDpi;
        if (!this.mDensityCompatMode && densityDpi != 0 && densityDpi != DisplayMetrics.DENSITY_DEVICE) {
            DisplayMetrics.DENSITY_DEVICE = densityDpi;
            Bitmap.setDefaultDensity(densityDpi);
        }
    }

    private synchronized String getInstrumentationLibrary(ApplicationInfo appInfo, InstrumentationInfo insInfo) {
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

    private synchronized void updateLocaleListFromAppContext(Context context, LocaleList newLocaleList) {
        if (context == null || context.getResources() == null) {
            return;
        }
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

    /* JADX INFO: Access modifiers changed from: public */
    public void handleBindApplication(AppBindData data) {
        InstrumentationInfo ii;
        ApplicationInfo instrApp;
        ContextImpl appContext;
        int preloadedFontsResource;
        VMRuntime.registerSensitiveThread();
        if (data.trackAllocation) {
            DdmVmInternal.enableRecentAllocations(true);
        }
        Process.setStartTimes(SystemClock.elapsedRealtime(), SystemClock.uptimeMillis());
        this.mBoundApplication = data;
        this.mConfiguration = new Configuration(data.config);
        this.mCompatConfiguration = new Configuration(data.config);
        this.mProfiler = new Profiler();
        String agent = null;
        if (data.initProfilerInfo != null) {
            this.mProfiler.profileFile = data.initProfilerInfo.profileFile;
            this.mProfiler.profileFd = data.initProfilerInfo.profileFd;
            this.mProfiler.samplingInterval = data.initProfilerInfo.samplingInterval;
            this.mProfiler.autoStopProfiler = data.initProfilerInfo.autoStopProfiler;
            this.mProfiler.streamingOutput = data.initProfilerInfo.streamingOutput;
            if (data.initProfilerInfo.attachAgentDuringBind) {
                agent = data.initProfilerInfo.agent;
            }
        }
        String agent2 = agent;
        Process.setArgV0(data.processName);
        DdmHandleAppName.setAppName(data.processName, UserHandle.myUserId());
        VMRuntime.setProcessPackageName(data.appInfo.packageName);
        if (this.mProfiler.profileFd != null) {
            this.mProfiler.startProfiling();
        }
        if (data.appInfo.targetSdkVersion <= 12) {
            AsyncTask.setDefaultExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        Message.updateCheckRecycle(data.appInfo.targetSdkVersion);
        ImageDecoder.sApiLevel = data.appInfo.targetSdkVersion;
        TimeZone.setDefault(null);
        LocaleList.setDefault(data.config.getLocales());
        synchronized (this.mResourcesManager) {
            this.mResourcesManager.applyConfigurationToResourcesLocked(data.config, data.compatInfo);
            this.mCurDefaultDisplayDpi = data.config.densityDpi;
            applyCompatConfiguration(this.mCurDefaultDisplayDpi);
        }
        data.info = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
        if (agent2 != null) {
            handleAttachAgent(agent2, data.info);
        }
        if ((data.appInfo.flags & 8192) == 0) {
            this.mDensityCompatMode = true;
            Bitmap.setDefaultDensity(160);
        }
        updateDefaultDensity();
        String use24HourSetting = this.mCoreSettings.getString(Settings.System.TIME_12_24);
        Boolean is24Hr = null;
        if (use24HourSetting != null) {
            is24Hr = "24".equals(use24HourSetting) ? Boolean.TRUE : Boolean.FALSE;
        }
        DateFormat.set24HourTimePref(is24Hr);
        View.mDebugViewAttributes = this.mCoreSettings.getInt(Settings.Global.DEBUG_VIEW_ATTRIBUTES, 0) != 0;
        StrictMode.initThreadDefaults(data.appInfo);
        StrictMode.initVmDefaults(data.appInfo);
        try {
            Field field = Build.class.getDeclaredField("SERIAL");
            field.setAccessible(true);
            field.set(Build.class, data.buildSerial);
        } catch (IllegalAccessException | NoSuchFieldException e) {
        }
        if (data.debugMode != 0) {
            Debug.changeDebugPort(8100);
            if (data.debugMode == 2) {
                Slog.w(TAG, "Application " + data.info.getPackageName() + " is waiting for the debugger on port 8100...");
                IActivityManager mgr = ActivityManager.getService();
                try {
                    mgr.showWaitingForDebugger(this.mAppThread, true);
                    Debug.waitForDebugger();
                    try {
                        mgr.showWaitingForDebugger(this.mAppThread, false);
                    } catch (RemoteException ex) {
                        throw ex.rethrowFromSystemServer();
                    }
                } catch (RemoteException ex2) {
                    throw ex2.rethrowFromSystemServer();
                }
            } else {
                Slog.w(TAG, "Application " + data.info.getPackageName() + " can be debugged on port 8100...");
            }
        }
        boolean isAppDebuggable = (data.appInfo.flags & 2) != 0;
        Trace.setAppTracingAllowed(isAppDebuggable);
        ThreadedRenderer.setDebuggingEnabled(isAppDebuggable || Build.IS_DEBUGGABLE);
        if (isAppDebuggable && data.enableBinderTracking) {
            Binder.enableTracing();
        }
        Trace.traceBegin(64L, "Setup proxies");
        IBinder b = ServiceManager.getService(Context.CONNECTIVITY_SERVICE);
        if (b != null) {
            IConnectivityManager service = IConnectivityManager.Stub.asInterface(b);
            try {
                ProxyInfo proxyInfo = service.getProxyForNetwork(null);
                Proxy.setHttpProxySystemProperty(proxyInfo);
            } catch (RemoteException e2) {
                Trace.traceEnd(64L);
                throw e2.rethrowFromSystemServer();
            }
        }
        Trace.traceEnd(64L);
        if (data.instrumentationName != null) {
            try {
                ii = new ApplicationPackageManager(null, getPackageManager()).getInstrumentationInfo(data.instrumentationName, 0);
                if (!Objects.equals(data.appInfo.primaryCpuAbi, ii.primaryCpuAbi) || !Objects.equals(data.appInfo.secondaryCpuAbi, ii.secondaryCpuAbi)) {
                    Slog.w(TAG, "Package uses different ABI(s) than its instrumentation: package[" + data.appInfo.packageName + "]: " + data.appInfo.primaryCpuAbi + ", " + data.appInfo.secondaryCpuAbi + " instrumentation[" + ii.packageName + "]: " + ii.primaryCpuAbi + ", " + ii.secondaryCpuAbi);
                }
                this.mInstrumentationPackageName = ii.packageName;
                this.mInstrumentationAppDir = ii.sourceDir;
                this.mInstrumentationSplitAppDirs = ii.splitSourceDirs;
                this.mInstrumentationLibDir = getInstrumentationLibrary(data.appInfo, ii);
                this.mInstrumentedAppDir = data.info.getAppDir();
                this.mInstrumentedSplitAppDirs = data.info.getSplitAppDirs();
                this.mInstrumentedLibDir = data.info.getLibDir();
            } catch (PackageManager.NameNotFoundException e3) {
                throw new RuntimeException("Unable to find instrumentation info for: " + data.instrumentationName);
            }
        } else {
            ii = null;
        }
        InstrumentationInfo ii2 = ii;
        ContextImpl appContext2 = ContextImpl.createAppContext(this, data.info);
        updateLocaleListFromAppContext(appContext2, this.mResourcesManager.getConfiguration().getLocales());
        if (Process.isIsolated()) {
            ThreadedRenderer.setIsolatedProcess(true);
        } else {
            int oldMask = StrictMode.allowThreadDiskWritesMask();
            try {
                setupGraphicsSupport(appContext2);
            } finally {
                StrictMode.setThreadPolicyMask(oldMask);
            }
        }
        if (SystemProperties.getBoolean("dalvik.vm.usejitprofiles", false)) {
            BaseDexClassLoader.setReporter(DexLoadReporter.getInstance());
        }
        Trace.traceBegin(64L, "NetworkSecurityConfigProvider.install");
        NetworkSecurityConfigProvider.install(appContext2);
        Trace.traceEnd(64L);
        if (ii2 != null) {
            try {
                instrApp = getPackageManager().getApplicationInfo(ii2.packageName, 0, UserHandle.myUserId());
            } catch (RemoteException e4) {
                instrApp = null;
            }
            if (instrApp == null) {
                instrApp = new ApplicationInfo();
            }
            ApplicationInfo instrApp2 = instrApp;
            ii2.copyTo(instrApp2);
            instrApp2.initForUser(UserHandle.myUserId());
            appContext = appContext2;
            LoadedApk pi = getPackageInfo(instrApp2, data.compatInfo, appContext2.getClassLoader(), false, true, false);
            ContextImpl instrContext = ContextImpl.createAppContext(this, pi);
            try {
                ClassLoader cl = instrContext.getClassLoader();
                this.mInstrumentation = (Instrumentation) cl.loadClass(data.instrumentationName.getClassName()).newInstance();
                ComponentName component = new ComponentName(ii2.packageName, ii2.name);
                this.mInstrumentation.init(this, instrContext, appContext, component, data.instrumentationWatcher, data.instrumentationUiAutomationConnection);
                if (this.mProfiler.profileFile != null && !ii2.handleProfiling && this.mProfiler.profileFd == null) {
                    this.mProfiler.handlingProfiling = true;
                    File file = new File(this.mProfiler.profileFile);
                    file.getParentFile().mkdirs();
                    Debug.startMethodTracing(file.toString(), 8388608);
                }
            } catch (Exception e5) {
                throw new RuntimeException("Unable to instantiate instrumentation " + data.instrumentationName + ": " + e5.toString(), e5);
            }
        } else {
            appContext = appContext2;
            this.mInstrumentation = new Instrumentation();
            this.mInstrumentation.basicInit(this);
        }
        if ((data.appInfo.flags & 1048576) != 0) {
            VMRuntime.getRuntime().clearGrowthLimit();
        } else {
            VMRuntime.getRuntime().clampGrowthLimit();
        }
        StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskWrites();
        StrictMode.ThreadPolicy writesAllowedPolicy = StrictMode.getThreadPolicy();
        try {
            Application app = data.info.makeApplication(data.restrictedBackupMode, null);
            app.setAutofillCompatibilityEnabled(data.autofillCompatibilityEnabled);
            this.mInitialApplication = app;
            if (!data.restrictedBackupMode) {
                try {
                    if (!ArrayUtils.isEmpty(data.providers)) {
                        installContentProviders(app, data.providers);
                        this.mH.sendEmptyMessageDelayed(132, JobInfo.MIN_BACKOFF_MILLIS);
                    }
                } catch (Throwable th) {
                    e = th;
                    if (data.appInfo.targetSdkVersion >= 27 || StrictMode.getThreadPolicy().equals(writesAllowedPolicy)) {
                        StrictMode.setThreadPolicy(savedPolicy);
                    }
                    throw e;
                }
            }
            try {
                try {
                    this.mInstrumentation.onCreate(data.instrumentationArgs);
                    try {
                        this.mInstrumentation.callApplicationOnCreate(app);
                    } catch (Exception e6) {
                        if (!this.mInstrumentation.onException(app, e6)) {
                            throw new RuntimeException("Unable to create application " + app.getClass().getName() + ": " + e6.toString(), e6);
                        }
                    }
                    if (data.appInfo.targetSdkVersion < 27 || StrictMode.getThreadPolicy().equals(writesAllowedPolicy)) {
                        StrictMode.setThreadPolicy(savedPolicy);
                    }
                    FontsContract.setApplicationContextForResources(appContext);
                    if (Process.isIsolated()) {
                        return;
                    }
                    try {
                        ApplicationInfo info = getPackageManager().getApplicationInfo(data.appInfo.packageName, 128, UserHandle.myUserId());
                        if (info.metaData == null || (preloadedFontsResource = info.metaData.getInt(ApplicationInfo.METADATA_PRELOADED_FONTS, 0)) == 0) {
                            return;
                        }
                        data.info.getResources().preloadFonts(preloadedFontsResource);
                    } catch (RemoteException e7) {
                        throw e7.rethrowFromSystemServer();
                    }
                } catch (Throwable th2) {
                    e = th2;
                    if (data.appInfo.targetSdkVersion >= 27) {
                    }
                    StrictMode.setThreadPolicy(savedPolicy);
                    throw e;
                }
            } catch (Exception e8) {
                throw new RuntimeException("Exception thrown in onCreate() of " + data.instrumentationName + ": " + e8.toString(), e8);
            }
        } catch (Throwable th3) {
            e = th3;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void finishInstrumentation(int resultCode, Bundle results) {
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

    public protected void installContentProviders(Context context, List<ProviderInfo> providers) {
        ArrayList<ContentProviderHolder> results = new ArrayList<>();
        for (ProviderInfo cpi : providers) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public final IContentProvider acquireProvider(Context c, String auth, int userId, boolean stable) {
        IContentProvider provider = acquireExistingProvider(c, auth, userId, stable);
        if (provider != null) {
            return provider;
        }
        try {
            try {
                synchronized (getGetProviderLock(auth, userId)) {
                    try {
                        ContentProviderHolder holder = ActivityManager.getService().getContentProvider(getApplicationThread(), auth, userId, stable);
                        if (holder == null) {
                            Slog.e(TAG, "Failed to find provider info for " + auth);
                            return null;
                        }
                        return installProvider(c, holder, holder.info, true, holder.noReleaseNeeded, stable).provider;
                    } catch (Throwable th) {
                        th = th;
                        try {
                            throw th;
                        } catch (RemoteException e) {
                            ex = e;
                            throw ex.rethrowFromSystemServer();
                        }
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (RemoteException e2) {
            ex = e2;
        }
    }

    private synchronized Object getGetProviderLock(String auth, int userId) {
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

    private final synchronized void incProviderRefLocked(ProviderRefCount prc, boolean stable) {
        int unstableDelta = 0;
        if (stable) {
            prc.stableCount++;
            if (prc.stableCount == 1) {
                if (prc.removePending) {
                    prc.removePending = false;
                    this.mH.removeMessages(131, prc);
                    unstableDelta = -1;
                }
                try {
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
                prc.removePending = false;
                this.mH.removeMessages(131, prc);
                return;
            }
            try {
                ActivityManager.getService().refContentProvider(prc.holder.connection, 0, 1);
            } catch (RemoteException e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean releaseProvider(IContentProvider provider, boolean stable) {
        int i = 0;
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
                    return false;
                }
                prc.stableCount--;
                if (prc.stableCount == 0) {
                    lastRef = prc.unstableCount == 0;
                    try {
                        IActivityManager service = ActivityManager.getService();
                        IBinder iBinder = prc.holder.connection;
                        if (lastRef) {
                            i = 1;
                        }
                        service.refContentProvider(iBinder, -1, i);
                    } catch (RemoteException e) {
                    }
                }
            } else if (prc.unstableCount == 0) {
                return false;
            } else {
                prc.unstableCount--;
                if (prc.unstableCount == 0) {
                    lastRef = prc.stableCount == 0;
                    if (!lastRef) {
                        try {
                            ActivityManager.getService().refContentProvider(prc.holder.connection, 0, -1);
                        } catch (RemoteException e2) {
                        }
                    }
                }
            }
            if (lastRef) {
                if (!prc.removePending) {
                    prc.removePending = true;
                    Message msg = this.mH.obtainMessage(131, prc);
                    this.mH.sendMessage(msg);
                } else {
                    Slog.w(TAG, "Duplicate remove pending of provider " + prc.holder.info.name);
                }
            }
            return true;
        }
    }

    final synchronized void completeRemoveProvider(ProviderRefCount prc) {
        synchronized (this.mProviderMap) {
            if (prc.removePending) {
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
                    ActivityManager.getService().removeContentProvider(prc.holder.connection, false);
                } catch (RemoteException e) {
                }
            }
        }
    }

    public private protected final void handleUnstableProviderDied(IBinder provider, boolean fromClient) {
        synchronized (this.mProviderMap) {
            handleUnstableProviderDiedLocked(provider, fromClient);
        }
    }

    final synchronized void handleUnstableProviderDiedLocked(IBinder provider, boolean fromClient) {
        ProviderRefCount prc = this.mProviderRefCountMap.get(provider);
        if (prc != null) {
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
    public final synchronized void appNotRespondingViaProvider(IBinder provider) {
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

    private synchronized ProviderClientRecord installProviderAuthoritiesLocked(IContentProvider provider, ContentProvider localProvider, ContentProviderHolder holder) {
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
                        if (auth.equals("telephony")) {
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

    /* JADX WARN: Removed duplicated region for block: B:30:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x009a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public protected android.app.ContentProviderHolder installProvider(android.content.Context r16, android.app.ContentProviderHolder r17, android.content.pm.ProviderInfo r18, boolean r19, boolean r20, boolean r21) {
        /*
            Method dump skipped, instructions count: 436
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.installProvider(android.content.Context, android.app.ContentProviderHolder, android.content.pm.ProviderInfo, boolean, boolean, boolean):android.app.ContentProviderHolder");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleRunIsolatedEntryPoint(String entryPoint, String[] entryPointArgs) {
        try {
            Method main = Class.forName(entryPoint).getMethod("main", String[].class);
            main.invoke(null, entryPointArgs);
            System.exit(0);
        } catch (ReflectiveOperationException e) {
            throw new AndroidRuntimeException("runIsolatedEntryPoint failed", e);
        }
    }

    public protected void attach(boolean system, long startSeq) {
        sCurrentActivityThread = this;
        this.mSystemThread = system;
        if (!system) {
            ViewRootImpl.addFirstDrawHandler(new Runnable() { // from class: android.app.ActivityThread.1
                @Override // java.lang.Runnable
                public void run() {
                    ActivityThread.this.ensureJitEnabled();
                }
            });
            DdmHandleAppName.setAppName("<pre-initialized>", UserHandle.myUserId());
            RuntimeInit.setApplicationObject(this.mAppThread.asBinder());
            final IActivityManager mgr = ActivityManager.getService();
            try {
                mgr.attachApplication(this.mAppThread, startSeq);
                BinderInternal.addGcWatcher(new Runnable() { // from class: android.app.ActivityThread.2
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
                                mgr.releaseSomeActivities(ActivityThread.this.mAppThread);
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
        DropBox.setReporter(new DropBoxReporter());
        ViewRootImpl.ConfigChangedCallback configChangedCallback = new ViewRootImpl.ConfigChangedCallback() { // from class: android.app.-$$Lambda$ActivityThread$ZXDWm3IBeFmLnFVblhB-IOZCr9o
            @Override // android.view.ViewRootImpl.ConfigChangedCallback
            public final void onConfigurationChanged(Configuration configuration) {
                ActivityThread.lambda$attach$0(ActivityThread.this, configuration);
            }
        };
        ViewRootImpl.addConfigCallback(configChangedCallback);
    }

    public static /* synthetic */ void lambda$attach$0(ActivityThread activityThread, Configuration globalConfig) {
        synchronized (activityThread.mResourcesManager) {
            if (activityThread.mResourcesManager.applyConfigurationToResourcesLocked(globalConfig, null)) {
                activityThread.updateLocaleListFromAppContext(activityThread.mInitialApplication.getApplicationContext(), activityThread.mResourcesManager.getConfiguration().getLocales());
                if (activityThread.mPendingConfiguration == null || activityThread.mPendingConfiguration.isOtherSeqNewer(globalConfig)) {
                    activityThread.mPendingConfiguration = globalConfig;
                    activityThread.sendMessage(118, globalConfig);
                }
            }
        }
    }

    private protected static ActivityThread systemMain() {
        if (!ActivityManager.isHighEndGfx()) {
            ThreadedRenderer.disable(true);
        } else {
            ThreadedRenderer.enableForegroundTrimming();
        }
        ActivityThread thread = new ActivityThread();
        thread.attach(true, 0L);
        return thread;
    }

    private protected final void installSystemProviders(List<ProviderInfo> providers) {
        if (providers != null) {
            installContentProviders(this.mInitialApplication, providers);
        }
    }

    public synchronized int getIntCoreSetting(String key, int defaultValue) {
        synchronized (this.mResourcesManager) {
            if (this.mCoreSettings != null) {
                return this.mCoreSettings.getInt(key, defaultValue);
            }
            return defaultValue;
        }
    }

    /* loaded from: classes.dex */
    private static class EventLoggingReporter implements EventLogger.Reporter {
        private synchronized EventLoggingReporter() {
        }

        public void report(int code, Object... list) {
            EventLog.writeEvent(code, list);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DropBoxReporter implements DropBox.Reporter {
        private DropBoxManager dropBox;

        public DropBoxReporter() {
        }

        public void addData(String tag, byte[] data, int flags) {
            ensureInitialized();
            this.dropBox.addData(tag, data, flags);
        }

        public void addText(String tag, String data) {
            ensureInitialized();
            this.dropBox.addText(tag, data);
        }

        private synchronized void ensureInitialized() {
            if (this.dropBox == null) {
                this.dropBox = (DropBoxManager) ActivityThread.this.getSystemContext().getSystemService(Context.DROPBOX_SERVICE);
            }
        }
    }

    public static synchronized void main(String[] args) {
        Trace.traceBegin(64L, "ActivityThreadMain");
        CloseGuard.setEnabled(false);
        Environment.initForCurrentUser();
        EventLogger.setReporter(new EventLoggingReporter());
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
}
