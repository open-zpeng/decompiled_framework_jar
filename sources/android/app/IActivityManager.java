package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.app.IActivityController;
import android.app.IApplicationThread;
import android.app.IAssistDataReceiver;
import android.app.IInstrumentationWatcher;
import android.app.IProcessObserver;
import android.app.IServiceConnection;
import android.app.IStopUserCallback;
import android.app.ITaskStackListener;
import android.app.IUiAutomationConnection;
import android.app.IUidObserver;
import android.app.IUserSwitchObserver;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ParceledListSlice;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IProgressListener;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.WorkSource;
import android.text.TextUtils;
import android.view.IRecentsAnimationRunner;
import com.android.internal.os.IResultReceiver;
import com.xiaopeng.app.xpDialogInfo;
import java.util.List;

/* loaded from: classes.dex */
public interface IActivityManager extends IInterface {
    void addInstrumentationResults(IApplicationThread iApplicationThread, Bundle bundle) throws RemoteException;

    void addPackageDependency(String str) throws RemoteException;

    void appNotRespondingViaProvider(IBinder iBinder) throws RemoteException;

    void attachApplication(IApplicationThread iApplicationThread, long j) throws RemoteException;

    void backgroundWhitelistUid(int i) throws RemoteException;

    void backupAgentCreated(String str, IBinder iBinder, int i) throws RemoteException;

    boolean bindBackupAgent(String str, int i, int i2) throws RemoteException;

    int bindIsolatedService(IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String str, IServiceConnection iServiceConnection, int i, String str2, String str3, int i2) throws RemoteException;

    @UnsupportedAppUsage
    int bindService(IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String str, IServiceConnection iServiceConnection, int i, String str2, int i2) throws RemoteException;

    void bootAnimationComplete() throws RemoteException;

    @UnsupportedAppUsage
    int broadcastIntent(IApplicationThread iApplicationThread, Intent intent, String str, IIntentReceiver iIntentReceiver, int i, String str2, Bundle bundle, String[] strArr, int i2, Bundle bundle2, boolean z, boolean z2, int i3) throws RemoteException;

    void cancelIntentSender(IIntentSender iIntentSender) throws RemoteException;

    @UnsupportedAppUsage
    void cancelRecentsAnimation(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void cancelTaskWindowTransition(int i) throws RemoteException;

    @UnsupportedAppUsage
    int checkPermission(String str, int i, int i2) throws RemoteException;

    int checkPermissionWithToken(String str, int i, int i2, IBinder iBinder) throws RemoteException;

    int checkUriPermission(Uri uri, int i, int i2, int i3, int i4, IBinder iBinder) throws RemoteException;

    boolean clearApplicationUserData(String str, boolean z, IPackageDataObserver iPackageDataObserver, int i) throws RemoteException;

    @UnsupportedAppUsage
    void closeSystemDialogs(String str) throws RemoteException;

    void crashApplication(int i, int i2, String str, int i3, String str2, boolean z) throws RemoteException;

    void dismissDialog(int i) throws RemoteException;

    boolean dumpHeap(String str, int i, boolean z, boolean z2, boolean z3, String str2, ParcelFileDescriptor parcelFileDescriptor, RemoteCallback remoteCallback) throws RemoteException;

    void dumpHeapFinished(String str) throws RemoteException;

    void enterSafeMode() throws RemoteException;

    @UnsupportedAppUsage
    boolean finishActivity(IBinder iBinder, int i, Intent intent, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void finishHeavyWeightApp() throws RemoteException;

    void finishInstrumentation(IApplicationThread iApplicationThread, int i, Bundle bundle) throws RemoteException;

    void finishMiniProgram() throws RemoteException;

    void finishReceiver(IBinder iBinder, int i, String str, Bundle bundle, boolean z, int i2) throws RemoteException;

    void forceGrantFolderPermission(String str) throws RemoteException;

    @UnsupportedAppUsage
    void forceStopPackage(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    List<ActivityManager.StackInfo> getAllStackInfos() throws RemoteException;

    @UnsupportedAppUsage
    Configuration getConfiguration() throws RemoteException;

    ContentProviderHolder getContentProvider(IApplicationThread iApplicationThread, String str, String str2, int i, boolean z) throws RemoteException;

    ContentProviderHolder getContentProviderExternal(String str, int i, IBinder iBinder, String str2) throws RemoteException;

    @UnsupportedAppUsage
    UserInfo getCurrentUser() throws RemoteException;

    List<xpDialogInfo> getDialogRecorder(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    List<ActivityManager.RunningTaskInfo> getFilteredTasks(int i, int i2, int i3) throws RemoteException;

    ActivityManager.StackInfo getFocusedStackInfo() throws RemoteException;

    int getForegroundServiceType(ComponentName componentName, IBinder iBinder) throws RemoteException;

    @UnsupportedAppUsage
    Intent getIntentForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    @UnsupportedAppUsage
    IIntentSender getIntentSender(int i, String str, IBinder iBinder, String str2, int i2, Intent[] intentArr, String[] strArr, int i3, Bundle bundle, int i4) throws RemoteException;

    @UnsupportedAppUsage
    String getLaunchedFromPackage(IBinder iBinder) throws RemoteException;

    @UnsupportedAppUsage
    int getLaunchedFromUid(IBinder iBinder) throws RemoteException;

    ParcelFileDescriptor getLifeMonitor() throws RemoteException;

    @UnsupportedAppUsage
    int getLockTaskModeState() throws RemoteException;

    @UnsupportedAppUsage
    void getMemoryInfo(ActivityManager.MemoryInfo memoryInfo) throws RemoteException;

    int getMemoryTrimLevel() throws RemoteException;

    void getMyMemoryState(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) throws RemoteException;

    String getOption(String str, String str2) throws RemoteException;

    String getPackageForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    @UnsupportedAppUsage
    int getPackageProcessState(String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    int getProcessLimit() throws RemoteException;

    @UnsupportedAppUsage
    Debug.MemoryInfo[] getProcessMemoryInfo(int[] iArr) throws RemoteException;

    @UnsupportedAppUsage
    long[] getProcessPss(int[] iArr) throws RemoteException;

    List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException;

    @UnsupportedAppUsage
    String getProviderMimeType(Uri uri, int i) throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice getRecentTasks(int i, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException;

    List<ApplicationInfo> getRunningExternalApplications() throws RemoteException;

    PendingIntent getRunningServiceControlPanel(ComponentName componentName) throws RemoteException;

    int[] getRunningUserIds() throws RemoteException;

    @UnsupportedAppUsage
    List<ActivityManager.RunningServiceInfo> getServices(int i, int i2) throws RemoteException;

    List<String> getSpeechObserver() throws RemoteException;

    String getTagForIntentSender(IIntentSender iIntentSender, String str) throws RemoteException;

    @UnsupportedAppUsage
    Rect getTaskBounds(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getTaskForActivity(IBinder iBinder, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    ActivityManager.TaskSnapshot getTaskSnapshot(int i, boolean z) throws RemoteException;

    List<ActivityManager.RunningTaskInfo> getTasks(int i) throws RemoteException;

    int getUidForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    int getUidProcessState(int i, String str) throws RemoteException;

    double[] getUsageInfo() throws RemoteException;

    void grantUriPermission(IApplicationThread iApplicationThread, String str, Uri uri, int i, int i2) throws RemoteException;

    void handleActivityChanged(Bundle bundle) throws RemoteException;

    void handleApplicationCrash(IBinder iBinder, ApplicationErrorReport.ParcelableCrashInfo parcelableCrashInfo) throws RemoteException;

    @UnsupportedAppUsage
    void handleApplicationStrictModeViolation(IBinder iBinder, int i, StrictMode.ViolationInfo violationInfo) throws RemoteException;

    boolean handleApplicationWtf(IBinder iBinder, String str, boolean z, ApplicationErrorReport.ParcelableCrashInfo parcelableCrashInfo) throws RemoteException;

    int handleIncomingUser(int i, int i2, int i3, boolean z, boolean z2, String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void hang(IBinder iBinder, boolean z) throws RemoteException;

    boolean isAppStartModeDisabled(int i, String str) throws RemoteException;

    boolean isBackgroundRestricted(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean isInLockTaskMode() throws RemoteException;

    boolean isIntentSenderABroadcast(IIntentSender iIntentSender) throws RemoteException;

    boolean isIntentSenderAForegroundService(IIntentSender iIntentSender) throws RemoteException;

    @UnsupportedAppUsage
    boolean isIntentSenderAnActivity(IIntentSender iIntentSender) throws RemoteException;

    boolean isIntentSenderTargetedToPackage(IIntentSender iIntentSender) throws RemoteException;

    boolean isTopActivityFullscreen() throws RemoteException;

    boolean isTopActivityImmersive() throws RemoteException;

    @UnsupportedAppUsage
    boolean isTopOfTask(IBinder iBinder) throws RemoteException;

    boolean isUidActive(int i, String str) throws RemoteException;

    boolean isUserAMonkey() throws RemoteException;

    @UnsupportedAppUsage
    boolean isUserRunning(int i, int i2) throws RemoteException;

    boolean isVrModePackageEnabled(ComponentName componentName) throws RemoteException;

    @UnsupportedAppUsage
    void killAllBackgroundProcesses() throws RemoteException;

    void killApplication(String str, int i, int i2, String str2) throws RemoteException;

    void killApplicationProcess(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    void killBackgroundProcesses(String str, int i) throws RemoteException;

    void killPackageDependents(String str, int i) throws RemoteException;

    boolean killPids(int[] iArr, String str, boolean z) throws RemoteException;

    boolean killProcessesBelowForeground(String str) throws RemoteException;

    void killUid(int i, int i2, String str) throws RemoteException;

    void makePackageIdle(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean moveActivityTaskToBack(IBinder iBinder, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void moveTaskToFront(IApplicationThread iApplicationThread, String str, int i, int i2, Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    void moveTaskToStack(int i, int i2, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    boolean moveTopActivityToPinnedStack(int i, Rect rect) throws RemoteException;

    void noteAlarmFinish(IIntentSender iIntentSender, WorkSource workSource, int i, String str) throws RemoteException;

    void noteAlarmStart(IIntentSender iIntentSender, WorkSource workSource, int i, String str) throws RemoteException;

    void noteWakeupAlarm(IIntentSender iIntentSender, WorkSource workSource, int i, String str, String str2) throws RemoteException;

    void notifyCleartextNetwork(int i, byte[] bArr) throws RemoteException;

    void notifyInstallPersistentPackage(String str) throws RemoteException;

    void notifyLockedProfile(int i) throws RemoteException;

    ParcelFileDescriptor openContentUri(String str) throws RemoteException;

    IBinder peekService(Intent intent, String str, String str2) throws RemoteException;

    void performIdleMaintenance() throws RemoteException;

    @UnsupportedAppUsage
    void positionTaskInStack(int i, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    boolean profileControl(String str, int i, boolean z, ProfilerInfo profilerInfo, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void publishContentProviders(IApplicationThread iApplicationThread, List<ContentProviderHolder> list) throws RemoteException;

    void publishService(IBinder iBinder, Intent intent, IBinder iBinder2) throws RemoteException;

    boolean refContentProvider(IBinder iBinder, int i, int i2) throws RemoteException;

    void registerIntentSenderCancelListener(IIntentSender iIntentSender, IResultReceiver iResultReceiver) throws RemoteException;

    @UnsupportedAppUsage
    void registerProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    @UnsupportedAppUsage
    Intent registerReceiver(IApplicationThread iApplicationThread, String str, IIntentReceiver iIntentReceiver, IntentFilter intentFilter, String str2, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void registerTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    void registerUidObserver(IUidObserver iUidObserver, int i, int i2, String str) throws RemoteException;

    @UnsupportedAppUsage
    void registerUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver, String str) throws RemoteException;

    void removeContentProvider(IBinder iBinder, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void removeContentProviderExternal(String str, IBinder iBinder) throws RemoteException;

    void removeContentProviderExternalAsUser(String str, IBinder iBinder, int i) throws RemoteException;

    @UnsupportedAppUsage
    void removeStack(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean removeTask(int i) throws RemoteException;

    @UnsupportedAppUsage
    void requestBugReport(int i) throws RemoteException;

    void requestSystemServerHeapDump() throws RemoteException;

    void requestTelephonyBugReport(String str, String str2) throws RemoteException;

    void requestWifiBugReport(String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void resizeDockedStack(Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5) throws RemoteException;

    @UnsupportedAppUsage
    void resizeStack(int i, Rect rect, boolean z, boolean z2, boolean z3, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void resizeTask(int i, Rect rect, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void restart() throws RemoteException;

    int restartUserInBackground(int i) throws RemoteException;

    @UnsupportedAppUsage
    void resumeAppSwitches() throws RemoteException;

    void revokeUriPermission(IApplicationThread iApplicationThread, String str, Uri uri, int i, int i2) throws RemoteException;

    void scheduleApplicationInfoChanged(List<String> list, int i) throws RemoteException;

    @UnsupportedAppUsage
    void sendIdleJobTrigger() throws RemoteException;

    int sendIntentSender(IIntentSender iIntentSender, IBinder iBinder, int i, Intent intent, String str, IIntentReceiver iIntentReceiver, String str2, Bundle bundle) throws RemoteException;

    void serviceDoneExecuting(IBinder iBinder, int i, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    void setActivityController(IActivityController iActivityController, boolean z) throws RemoteException;

    void setAgentApp(String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void setAlwaysFinish(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setDebugApp(String str, boolean z, boolean z2) throws RemoteException;

    void setDialogRecorder(xpDialogInfo xpdialoginfo) throws RemoteException;

    @UnsupportedAppUsage
    void setDumpHeapDebugLimit(String str, int i, long j, String str2) throws RemoteException;

    void setFocusedAppNoChecked(int i) throws RemoteException;

    void setFocusedStack(int i) throws RemoteException;

    void setHasTopUi(boolean z) throws RemoteException;

    void setHomeState(ComponentName componentName, int i) throws RemoteException;

    @UnsupportedAppUsage
    void setPackageScreenCompatMode(String str, int i) throws RemoteException;

    void setPersistentVrThread(int i) throws RemoteException;

    @UnsupportedAppUsage
    void setProcessImportant(IBinder iBinder, int i, boolean z, String str) throws RemoteException;

    @UnsupportedAppUsage
    void setProcessLimit(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean setProcessMemoryTrimLevel(String str, int i, int i2) throws RemoteException;

    void setRenderThread(int i) throws RemoteException;

    @UnsupportedAppUsage
    void setRequestedOrientation(IBinder iBinder, int i) throws RemoteException;

    void setServiceForeground(ComponentName componentName, IBinder iBinder, int i, Notification notification, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    void setTaskResizeable(int i, int i2) throws RemoteException;

    void setUserIsMonkey(boolean z) throws RemoteException;

    void showBootMessage(CharSequence charSequence, boolean z) throws RemoteException;

    void showWaitingForDebugger(IApplicationThread iApplicationThread, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    boolean shutdown(int i) throws RemoteException;

    void signalPersistentProcesses(int i) throws RemoteException;

    @UnsupportedAppUsage
    int startActivity(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    int startActivityAsUser(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    @UnsupportedAppUsage
    int startActivityFromRecents(int i, Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    boolean startBinderTracking() throws RemoteException;

    void startConfirmDeviceCredentialIntent(Intent intent, Bundle bundle) throws RemoteException;

    void startDelegateShellPermissionIdentity(int i, String[] strArr) throws RemoteException;

    @UnsupportedAppUsage
    boolean startInstrumentation(ComponentName componentName, String str, int i, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, int i2, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void startRecentsActivity(Intent intent, IAssistDataReceiver iAssistDataReceiver, IRecentsAnimationRunner iRecentsAnimationRunner) throws RemoteException;

    ComponentName startService(IApplicationThread iApplicationThread, Intent intent, String str, boolean z, String str2, int i) throws RemoteException;

    @UnsupportedAppUsage
    void startSystemLockTaskMode(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean startUserInBackground(int i) throws RemoteException;

    boolean startUserInBackgroundWithListener(int i, IProgressListener iProgressListener) throws RemoteException;

    boolean startUserInForegroundWithListener(int i, IProgressListener iProgressListener) throws RemoteException;

    int startXpApp(String str, Intent intent) throws RemoteException;

    @UnsupportedAppUsage
    void stopAppSwitches() throws RemoteException;

    @UnsupportedAppUsage
    boolean stopBinderTrackingAndDump(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void stopDelegateShellPermissionIdentity() throws RemoteException;

    @UnsupportedAppUsage
    int stopService(IApplicationThread iApplicationThread, Intent intent, String str, int i) throws RemoteException;

    boolean stopServiceToken(ComponentName componentName, IBinder iBinder, int i) throws RemoteException;

    @UnsupportedAppUsage
    int stopUser(int i, boolean z, IStopUserCallback iStopUserCallback) throws RemoteException;

    @UnsupportedAppUsage
    void suppressResizeConfigChanges(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    boolean switchUser(int i) throws RemoteException;

    void unbindBackupAgent(ApplicationInfo applicationInfo) throws RemoteException;

    void unbindFinished(IBinder iBinder, Intent intent, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    boolean unbindService(IServiceConnection iServiceConnection) throws RemoteException;

    void unbroadcastIntent(IApplicationThread iApplicationThread, Intent intent, int i) throws RemoteException;

    @UnsupportedAppUsage
    void unhandledBack() throws RemoteException;

    @UnsupportedAppUsage
    boolean unlockUser(int i, byte[] bArr, byte[] bArr2, IProgressListener iProgressListener) throws RemoteException;

    void unregisterIntentSenderCancelListener(IIntentSender iIntentSender, IResultReceiver iResultReceiver) throws RemoteException;

    @UnsupportedAppUsage
    void unregisterProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    @UnsupportedAppUsage
    void unregisterReceiver(IIntentReceiver iIntentReceiver) throws RemoteException;

    void unregisterTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    void unregisterUidObserver(IUidObserver iUidObserver) throws RemoteException;

    void unregisterUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver) throws RemoteException;

    @UnsupportedAppUsage
    void unstableProviderDied(IBinder iBinder) throws RemoteException;

    @UnsupportedAppUsage
    boolean updateConfiguration(Configuration configuration) throws RemoteException;

    void updateDeviceOwner(String str) throws RemoteException;

    void updateLockTaskPackages(int i, String[] strArr) throws RemoteException;

    @UnsupportedAppUsage
    void updatePersistentConfiguration(Configuration configuration) throws RemoteException;

    void updateServiceGroup(IServiceConnection iServiceConnection, int i, int i2) throws RemoteException;

    void waitForNetworkStateUpdate(long j) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IActivityManager {
        @Override // android.app.IActivityManager
        public ParcelFileDescriptor openContentUri(String uriString) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void registerUidObserver(IUidObserver observer, int which, int cutpoint, String callingPackage) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unregisterUidObserver(IUidObserver observer) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isUidActive(int uid, String callingPackage) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public int getUidProcessState(int uid, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void handleApplicationCrash(IBinder app, ApplicationErrorReport.ParcelableCrashInfo crashInfo) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int startXpApp(String pkgName, Intent intent) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void unhandledBack() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean finishActivity(IBinder token, int code, Intent data, int finishTask) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public Intent registerReceiver(IApplicationThread caller, String callerPackage, IIntentReceiver receiver, IntentFilter filter, String requiredPermission, int userId, int flags) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void unregisterReceiver(IIntentReceiver receiver) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int broadcastIntent(IApplicationThread caller, Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode, String resultData, Bundle map, String[] requiredPermissions, int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void unbroadcastIntent(IApplicationThread caller, Intent intent, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void finishReceiver(IBinder who, int resultCode, String resultData, Bundle map, boolean abortBroadcast, int flags) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void attachApplication(IApplicationThread app, long startSeq) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public List<ActivityManager.RunningTaskInfo> getFilteredTasks(int maxNum, int ignoreActivityType, int ignoreWindowingMode) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void moveTaskToFront(IApplicationThread caller, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public ContentProviderHolder getContentProvider(IApplicationThread caller, String callingPackage, String name, int userId, boolean stable) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void publishContentProviders(IApplicationThread caller, List<ContentProviderHolder> providers) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean refContentProvider(IBinder connection, int stableDelta, int unstableDelta) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public PendingIntent getRunningServiceControlPanel(ComponentName service) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public ComponentName startService(IApplicationThread caller, Intent service, String resolvedType, boolean requireForeground, String callingPackage, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public int stopService(IApplicationThread caller, Intent service, String resolvedType, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int bindService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String callingPackage, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int bindIsolatedService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String instanceName, String callingPackage, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void updateServiceGroup(IServiceConnection connection, int group, int importance) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean unbindService(IServiceConnection connection) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void publishService(IBinder token, Intent intent, IBinder service) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setDebugApp(String packageName, boolean waitForDebugger, boolean persistent) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setAgentApp(String packageName, String agent) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setAlwaysFinish(boolean enabled) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean startInstrumentation(ComponentName className, String profileFile, int flags, Bundle arguments, IInstrumentationWatcher watcher, IUiAutomationConnection connection, int userId, String abiOverride) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void addInstrumentationResults(IApplicationThread target, Bundle results) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void finishInstrumentation(IApplicationThread target, int resultCode, Bundle results) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public Configuration getConfiguration() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean updateConfiguration(Configuration values) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean stopServiceToken(ComponentName className, IBinder token, int startId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void setProcessLimit(int max) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getProcessLimit() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int checkPermission(String permission, int pid, int uid) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int checkUriPermission(Uri uri, int pid, int uid, int mode, int userId, IBinder callerToken) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void grantUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void revokeUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void showWaitingForDebugger(IApplicationThread who, boolean waiting) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void signalPersistentProcesses(int signal) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public ParceledListSlice getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void serviceDoneExecuting(IBinder token, int type, int startId, int res) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public IIntentSender getIntentSender(int type, String packageName, IBinder token, String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes, int flags, Bundle options, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void cancelIntentSender(IIntentSender sender) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public String getPackageForIntentSender(IIntentSender sender) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void registerIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unregisterIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void enterSafeMode() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void noteWakeupAlarm(IIntentSender sender, WorkSource workSource, int sourceUid, String sourcePkg, String tag) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void removeContentProvider(IBinder connection, boolean stable) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unbindFinished(IBinder token, Intent service, boolean doRebind) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setProcessImportant(IBinder token, int pid, boolean isForeground, String reason) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setServiceForeground(ComponentName className, IBinder token, int id, Notification notification, int flags, int foregroundServiceType) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getForegroundServiceType(ComponentName className, IBinder token) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void getMemoryInfo(ActivityManager.MemoryInfo outInfo) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean clearApplicationUserData(String packageName, boolean keepState, IPackageDataObserver observer, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void forceStopPackage(String packageName, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean killPids(int[] pids, String reason, boolean secure) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public List<ActivityManager.RunningServiceInfo> getServices(int maxNum, int flags) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public IBinder peekService(Intent service, String resolvedType, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean profileControl(String process, int userId, boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean shutdown(int timeout) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void stopAppSwitches() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void resumeAppSwitches() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean bindBackupAgent(String packageName, int backupRestoreMode, int targetUserId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void backupAgentCreated(String packageName, IBinder agent, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unbindBackupAgent(ApplicationInfo appInfo) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getUidForIntentSender(IIntentSender sender) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, String name, String callerPackage) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void addPackageDependency(String packageName) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void killApplication(String pkg, int appId, int userId, String reason) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void closeSystemDialogs(String reason) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void killApplicationProcess(String processName, int uid) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean handleApplicationWtf(IBinder app, String tag, boolean system, ApplicationErrorReport.ParcelableCrashInfo crashInfo) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void killBackgroundProcesses(String packageName, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isUserAMonkey() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public List<ApplicationInfo> getRunningExternalApplications() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void finishHeavyWeightApp() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void handleApplicationStrictModeViolation(IBinder app, int penaltyMask, StrictMode.ViolationInfo crashInfo) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isTopActivityImmersive() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void crashApplication(int uid, int initialPid, String packageName, int userId, String message, boolean force) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public String getProviderMimeType(Uri uri, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean dumpHeap(String process, int userId, boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean isUserRunning(int userid, int flags) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean switchUser(int userid) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean removeTask(int taskId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void registerProcessObserver(IProcessObserver observer) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unregisterProcessObserver(IProcessObserver observer) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isIntentSenderTargetedToPackage(IIntentSender sender) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void updatePersistentConfiguration(Configuration values) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public long[] getProcessPss(int[] pids) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void showBootMessage(CharSequence msg, boolean always) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void killAllBackgroundProcesses() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token, String tag) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void removeContentProviderExternal(String name, IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void removeContentProviderExternalAsUser(String name, IBinder token, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void getMyMemoryState(ActivityManager.RunningAppProcessInfo outInfo) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean killProcessesBelowForeground(String reason) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public UserInfo getCurrentUser() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void unstableProviderDied(IBinder connection) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isIntentSenderAnActivity(IIntentSender sender) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean isIntentSenderAForegroundService(IIntentSender sender) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean isIntentSenderABroadcast(IIntentSender sender) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public int startActivityAsUser(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public int stopUser(int userid, boolean force, IStopUserCallback callback) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void registerUserSwitchObserver(IUserSwitchObserver observer, String name) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unregisterUserSwitchObserver(IUserSwitchObserver observer) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int[] getRunningUserIds() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void requestSystemServerHeapDump() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void requestBugReport(int bugreportType) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void requestTelephonyBugReport(String shareTitle, String shareDescription) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void requestWifiBugReport(String shareTitle, String shareDescription) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public Intent getIntentForIntentSender(IIntentSender sender) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void killUid(int appId, int userId, String reason) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setUserIsMonkey(boolean monkey) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void hang(IBinder who, boolean allowRestart) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public List<ActivityManager.StackInfo> getAllStackInfos() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void moveTaskToStack(int taskId, int stackId, boolean toTop) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void resizeStack(int stackId, Rect bounds, boolean allowResizeInDockedMode, boolean preserveWindows, boolean animate, int animationDuration) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setFocusedStack(int stackId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public ActivityManager.StackInfo getFocusedStackInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void restart() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void performIdleMaintenance() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void appNotRespondingViaProvider(IBinder connection) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public Rect getTaskBounds(int taskId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean setProcessMemoryTrimLevel(String process, int uid, int level) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public String getTagForIntentSender(IIntentSender sender, String prefix) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean startUserInBackground(int userid) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean isInLockTaskMode() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void startRecentsActivity(Intent intent, IAssistDataReceiver assistDataReceiver, IRecentsAnimationRunner recentsAnimationRunner) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void cancelRecentsAnimation(boolean restoreHomeStackPosition) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void startSystemLockTaskMode(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isTopOfTask(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void bootAnimationComplete() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int checkPermissionWithToken(String permission, int pid, int uid, IBinder callerToken) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void notifyCleartextNetwork(int uid, byte[] firstPacket) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getLockTaskModeState() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void setDumpHeapDebugLimit(String processName, int uid, long maxMemSize, String reportPackage) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void dumpHeapFinished(String path) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void noteAlarmStart(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void noteAlarmFinish(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getPackageProcessState(String packageName, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void updateDeviceOwner(String packageName) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean startBinderTracking() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void positionTaskInStack(int taskId, int stackId, int position) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean moveTopActivityToPinnedStack(int stackId, Rect bounds) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean isAppStartModeDisabled(int uid, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public boolean unlockUser(int userid, byte[] token, byte[] secret, IProgressListener listener) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void killPackageDependents(String packageName, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void resizeDockedStack(Rect dockedBounds, Rect tempDockedTaskBounds, Rect tempDockedTaskInsetBounds, Rect tempOtherTaskBounds, Rect tempOtherTaskInsetBounds) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void removeStack(int stackId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void makePackageIdle(String packageName, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int getMemoryTrimLevel() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public boolean isVrModePackageEnabled(ComponentName packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void notifyLockedProfile(int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void startConfirmDeviceCredentialIntent(Intent intent, Bundle options) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void sendIdleJobTrigger() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int sendIntentSender(IIntentSender target, IBinder whitelistToken, int code, Intent intent, String resolvedType, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public boolean isBackgroundRestricted(String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void setRenderThread(int tid) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setHasTopUi(boolean hasTopUi) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public int restartUserInBackground(int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityManager
        public void cancelTaskWindowTransition(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public ActivityManager.TaskSnapshot getTaskSnapshot(int taskId, boolean reducedResolution) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void scheduleApplicationInfoChanged(List<String> packageNames, int userId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setPersistentVrThread(int tid) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void waitForNetworkStateUpdate(long procStateSeq) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void backgroundWhitelistUid(int uid) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean startUserInBackgroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void startDelegateShellPermissionIdentity(int uid, String[] permissions) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void stopDelegateShellPermissionIdentity() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public ParcelFileDescriptor getLifeMonitor() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public boolean startUserInForegroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void finishMiniProgram() throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setFocusedAppNoChecked(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void handleActivityChanged(Bundle bundle) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public boolean isTopActivityFullscreen() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityManager
        public void forceGrantFolderPermission(String path) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public String getOption(String key, String defaultValue) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public double[] getUsageInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public List<xpDialogInfo> getDialogRecorder(boolean topOnly) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void setDialogRecorder(xpDialogInfo info) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void dismissDialog(int type) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public void setHomeState(ComponentName component, int state) throws RemoteException {
        }

        @Override // android.app.IActivityManager
        public List<String> getSpeechObserver() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityManager
        public void notifyInstallPersistentPackage(String packageName) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IActivityManager {
        private static final String DESCRIPTOR = "android.app.IActivityManager";
        static final int TRANSACTION_addInstrumentationResults = 36;
        static final int TRANSACTION_addPackageDependency = 83;
        static final int TRANSACTION_appNotRespondingViaProvider = 141;
        static final int TRANSACTION_attachApplication = 16;
        static final int TRANSACTION_backgroundWhitelistUid = 193;
        static final int TRANSACTION_backupAgentCreated = 79;
        static final int TRANSACTION_bindBackupAgent = 78;
        static final int TRANSACTION_bindIsolatedService = 28;
        static final int TRANSACTION_bindService = 27;
        static final int TRANSACTION_bootAnimationComplete = 152;
        static final int TRANSACTION_broadcastIntent = 13;
        static final int TRANSACTION_cancelIntentSender = 53;
        static final int TRANSACTION_cancelRecentsAnimation = 148;
        static final int TRANSACTION_cancelTaskWindowTransition = 188;
        static final int TRANSACTION_checkPermission = 43;
        static final int TRANSACTION_checkPermissionWithToken = 153;
        static final int TRANSACTION_checkUriPermission = 44;
        static final int TRANSACTION_clearApplicationUserData = 68;
        static final int TRANSACTION_closeSystemDialogs = 85;
        static final int TRANSACTION_crashApplication = 95;
        static final int TRANSACTION_dismissDialog = 208;
        static final int TRANSACTION_dumpHeap = 97;
        static final int TRANSACTION_dumpHeapFinished = 161;
        static final int TRANSACTION_enterSafeMode = 57;
        static final int TRANSACTION_finishActivity = 10;
        static final int TRANSACTION_finishHeavyWeightApp = 92;
        static final int TRANSACTION_finishInstrumentation = 37;
        static final int TRANSACTION_finishMiniProgram = 199;
        static final int TRANSACTION_finishReceiver = 15;
        static final int TRANSACTION_forceGrantFolderPermission = 203;
        static final int TRANSACTION_forceStopPackage = 69;
        static final int TRANSACTION_getAllStackInfos = 134;
        static final int TRANSACTION_getConfiguration = 38;
        static final int TRANSACTION_getContentProvider = 21;
        static final int TRANSACTION_getContentProviderExternal = 109;
        static final int TRANSACTION_getCurrentUser = 114;
        static final int TRANSACTION_getDialogRecorder = 206;
        static final int TRANSACTION_getFilteredTasks = 18;
        static final int TRANSACTION_getFocusedStackInfo = 138;
        static final int TRANSACTION_getForegroundServiceType = 64;
        static final int TRANSACTION_getIntentForIntentSender = 129;
        static final int TRANSACTION_getIntentSender = 52;
        static final int TRANSACTION_getLaunchedFromPackage = 130;
        static final int TRANSACTION_getLaunchedFromUid = 115;
        static final int TRANSACTION_getLifeMonitor = 197;
        static final int TRANSACTION_getLockTaskModeState = 159;
        static final int TRANSACTION_getMemoryInfo = 66;
        static final int TRANSACTION_getMemoryTrimLevel = 178;
        static final int TRANSACTION_getMyMemoryState = 112;
        static final int TRANSACTION_getOption = 204;
        static final int TRANSACTION_getPackageForIntentSender = 54;
        static final int TRANSACTION_getPackageProcessState = 165;
        static final int TRANSACTION_getProcessLimit = 42;
        static final int TRANSACTION_getProcessMemoryInfo = 86;
        static final int TRANSACTION_getProcessPss = 106;
        static final int TRANSACTION_getProcessesInErrorState = 67;
        static final int TRANSACTION_getProviderMimeType = 96;
        static final int TRANSACTION_getRecentTasks = 50;
        static final int TRANSACTION_getRunningAppProcesses = 72;
        static final int TRANSACTION_getRunningExternalApplications = 91;
        static final int TRANSACTION_getRunningServiceControlPanel = 24;
        static final int TRANSACTION_getRunningUserIds = 124;
        static final int TRANSACTION_getServices = 71;
        static final int TRANSACTION_getSpeechObserver = 210;
        static final int TRANSACTION_getTagForIntentSender = 144;
        static final int TRANSACTION_getTaskBounds = 142;
        static final int TRANSACTION_getTaskForActivity = 20;
        static final int TRANSACTION_getTaskSnapshot = 189;
        static final int TRANSACTION_getTasks = 17;
        static final int TRANSACTION_getUidForIntentSender = 81;
        static final int TRANSACTION_getUidProcessState = 5;
        static final int TRANSACTION_getUsageInfo = 205;
        static final int TRANSACTION_grantUriPermission = 45;
        static final int TRANSACTION_handleActivityChanged = 201;
        static final int TRANSACTION_handleApplicationCrash = 6;
        static final int TRANSACTION_handleApplicationStrictModeViolation = 93;
        static final int TRANSACTION_handleApplicationWtf = 88;
        static final int TRANSACTION_handleIncomingUser = 82;
        static final int TRANSACTION_hang = 133;
        static final int TRANSACTION_isAppStartModeDisabled = 172;
        static final int TRANSACTION_isBackgroundRestricted = 184;
        static final int TRANSACTION_isInLockTaskMode = 146;
        static final int TRANSACTION_isIntentSenderABroadcast = 119;
        static final int TRANSACTION_isIntentSenderAForegroundService = 118;
        static final int TRANSACTION_isIntentSenderAnActivity = 117;
        static final int TRANSACTION_isIntentSenderTargetedToPackage = 104;
        static final int TRANSACTION_isTopActivityFullscreen = 202;
        static final int TRANSACTION_isTopActivityImmersive = 94;
        static final int TRANSACTION_isTopOfTask = 151;
        static final int TRANSACTION_isUidActive = 4;
        static final int TRANSACTION_isUserAMonkey = 90;
        static final int TRANSACTION_isUserRunning = 98;
        static final int TRANSACTION_isVrModePackageEnabled = 179;
        static final int TRANSACTION_killAllBackgroundProcesses = 108;
        static final int TRANSACTION_killApplication = 84;
        static final int TRANSACTION_killApplicationProcess = 87;
        static final int TRANSACTION_killBackgroundProcesses = 89;
        static final int TRANSACTION_killPackageDependents = 174;
        static final int TRANSACTION_killPids = 70;
        static final int TRANSACTION_killProcessesBelowForeground = 113;
        static final int TRANSACTION_killUid = 131;
        static final int TRANSACTION_makePackageIdle = 177;
        static final int TRANSACTION_moveActivityTaskToBack = 65;
        static final int TRANSACTION_moveTaskToFront = 19;
        static final int TRANSACTION_moveTaskToStack = 135;
        static final int TRANSACTION_moveTopActivityToPinnedStack = 171;
        static final int TRANSACTION_noteAlarmFinish = 164;
        static final int TRANSACTION_noteAlarmStart = 163;
        static final int TRANSACTION_noteWakeupAlarm = 58;
        static final int TRANSACTION_notifyCleartextNetwork = 156;
        static final int TRANSACTION_notifyInstallPersistentPackage = 211;
        static final int TRANSACTION_notifyLockedProfile = 180;
        static final int TRANSACTION_openContentUri = 1;
        static final int TRANSACTION_peekService = 73;
        static final int TRANSACTION_performIdleMaintenance = 140;
        static final int TRANSACTION_positionTaskInStack = 169;
        static final int TRANSACTION_profileControl = 74;
        static final int TRANSACTION_publishContentProviders = 22;
        static final int TRANSACTION_publishService = 31;
        static final int TRANSACTION_refContentProvider = 23;
        static final int TRANSACTION_registerIntentSenderCancelListener = 55;
        static final int TRANSACTION_registerProcessObserver = 102;
        static final int TRANSACTION_registerReceiver = 11;
        static final int TRANSACTION_registerTaskStackListener = 154;
        static final int TRANSACTION_registerUidObserver = 2;
        static final int TRANSACTION_registerUserSwitchObserver = 122;
        static final int TRANSACTION_removeContentProvider = 59;
        static final int TRANSACTION_removeContentProviderExternal = 110;
        static final int TRANSACTION_removeContentProviderExternalAsUser = 111;
        static final int TRANSACTION_removeStack = 176;
        static final int TRANSACTION_removeTask = 101;
        static final int TRANSACTION_requestBugReport = 126;
        static final int TRANSACTION_requestSystemServerHeapDump = 125;
        static final int TRANSACTION_requestTelephonyBugReport = 127;
        static final int TRANSACTION_requestWifiBugReport = 128;
        static final int TRANSACTION_resizeDockedStack = 175;
        static final int TRANSACTION_resizeStack = 136;
        static final int TRANSACTION_resizeTask = 158;
        static final int TRANSACTION_restart = 139;
        static final int TRANSACTION_restartUserInBackground = 187;
        static final int TRANSACTION_resumeAppSwitches = 77;
        static final int TRANSACTION_revokeUriPermission = 46;
        static final int TRANSACTION_scheduleApplicationInfoChanged = 190;
        static final int TRANSACTION_sendIdleJobTrigger = 182;
        static final int TRANSACTION_sendIntentSender = 183;
        static final int TRANSACTION_serviceDoneExecuting = 51;
        static final int TRANSACTION_setActivityController = 47;
        static final int TRANSACTION_setAgentApp = 33;
        static final int TRANSACTION_setAlwaysFinish = 34;
        static final int TRANSACTION_setDebugApp = 32;
        static final int TRANSACTION_setDialogRecorder = 207;
        static final int TRANSACTION_setDumpHeapDebugLimit = 160;
        static final int TRANSACTION_setFocusedAppNoChecked = 200;
        static final int TRANSACTION_setFocusedStack = 137;
        static final int TRANSACTION_setHasTopUi = 186;
        static final int TRANSACTION_setHomeState = 209;
        static final int TRANSACTION_setPackageScreenCompatMode = 99;
        static final int TRANSACTION_setPersistentVrThread = 191;
        static final int TRANSACTION_setProcessImportant = 62;
        static final int TRANSACTION_setProcessLimit = 41;
        static final int TRANSACTION_setProcessMemoryTrimLevel = 143;
        static final int TRANSACTION_setRenderThread = 185;
        static final int TRANSACTION_setRequestedOrientation = 60;
        static final int TRANSACTION_setServiceForeground = 63;
        static final int TRANSACTION_setTaskResizeable = 157;
        static final int TRANSACTION_setUserIsMonkey = 132;
        static final int TRANSACTION_showBootMessage = 107;
        static final int TRANSACTION_showWaitingForDebugger = 48;
        static final int TRANSACTION_shutdown = 75;
        static final int TRANSACTION_signalPersistentProcesses = 49;
        static final int TRANSACTION_startActivity = 7;
        static final int TRANSACTION_startActivityAsUser = 120;
        static final int TRANSACTION_startActivityFromRecents = 149;
        static final int TRANSACTION_startBinderTracking = 167;
        static final int TRANSACTION_startConfirmDeviceCredentialIntent = 181;
        static final int TRANSACTION_startDelegateShellPermissionIdentity = 195;
        static final int TRANSACTION_startInstrumentation = 35;
        static final int TRANSACTION_startRecentsActivity = 147;
        static final int TRANSACTION_startService = 25;
        static final int TRANSACTION_startSystemLockTaskMode = 150;
        static final int TRANSACTION_startUserInBackground = 145;
        static final int TRANSACTION_startUserInBackgroundWithListener = 194;
        static final int TRANSACTION_startUserInForegroundWithListener = 198;
        static final int TRANSACTION_startXpApp = 8;
        static final int TRANSACTION_stopAppSwitches = 76;
        static final int TRANSACTION_stopBinderTrackingAndDump = 168;
        static final int TRANSACTION_stopDelegateShellPermissionIdentity = 196;
        static final int TRANSACTION_stopService = 26;
        static final int TRANSACTION_stopServiceToken = 40;
        static final int TRANSACTION_stopUser = 121;
        static final int TRANSACTION_suppressResizeConfigChanges = 170;
        static final int TRANSACTION_switchUser = 100;
        static final int TRANSACTION_unbindBackupAgent = 80;
        static final int TRANSACTION_unbindFinished = 61;
        static final int TRANSACTION_unbindService = 30;
        static final int TRANSACTION_unbroadcastIntent = 14;
        static final int TRANSACTION_unhandledBack = 9;
        static final int TRANSACTION_unlockUser = 173;
        static final int TRANSACTION_unregisterIntentSenderCancelListener = 56;
        static final int TRANSACTION_unregisterProcessObserver = 103;
        static final int TRANSACTION_unregisterReceiver = 12;
        static final int TRANSACTION_unregisterTaskStackListener = 155;
        static final int TRANSACTION_unregisterUidObserver = 3;
        static final int TRANSACTION_unregisterUserSwitchObserver = 123;
        static final int TRANSACTION_unstableProviderDied = 116;
        static final int TRANSACTION_updateConfiguration = 39;
        static final int TRANSACTION_updateDeviceOwner = 166;
        static final int TRANSACTION_updateLockTaskPackages = 162;
        static final int TRANSACTION_updatePersistentConfiguration = 105;
        static final int TRANSACTION_updateServiceGroup = 29;
        static final int TRANSACTION_waitForNetworkStateUpdate = 192;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IActivityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IActivityManager)) {
                return (IActivityManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "openContentUri";
                case 2:
                    return "registerUidObserver";
                case 3:
                    return "unregisterUidObserver";
                case 4:
                    return "isUidActive";
                case 5:
                    return "getUidProcessState";
                case 6:
                    return "handleApplicationCrash";
                case 7:
                    return "startActivity";
                case 8:
                    return "startXpApp";
                case 9:
                    return "unhandledBack";
                case 10:
                    return "finishActivity";
                case 11:
                    return "registerReceiver";
                case 12:
                    return "unregisterReceiver";
                case 13:
                    return "broadcastIntent";
                case 14:
                    return "unbroadcastIntent";
                case 15:
                    return "finishReceiver";
                case 16:
                    return "attachApplication";
                case 17:
                    return "getTasks";
                case 18:
                    return "getFilteredTasks";
                case 19:
                    return "moveTaskToFront";
                case 20:
                    return "getTaskForActivity";
                case 21:
                    return "getContentProvider";
                case 22:
                    return "publishContentProviders";
                case 23:
                    return "refContentProvider";
                case 24:
                    return "getRunningServiceControlPanel";
                case 25:
                    return "startService";
                case 26:
                    return "stopService";
                case 27:
                    return "bindService";
                case 28:
                    return "bindIsolatedService";
                case 29:
                    return "updateServiceGroup";
                case 30:
                    return "unbindService";
                case 31:
                    return "publishService";
                case 32:
                    return "setDebugApp";
                case 33:
                    return "setAgentApp";
                case 34:
                    return "setAlwaysFinish";
                case 35:
                    return "startInstrumentation";
                case 36:
                    return "addInstrumentationResults";
                case 37:
                    return "finishInstrumentation";
                case 38:
                    return "getConfiguration";
                case 39:
                    return "updateConfiguration";
                case 40:
                    return "stopServiceToken";
                case 41:
                    return "setProcessLimit";
                case 42:
                    return "getProcessLimit";
                case 43:
                    return "checkPermission";
                case 44:
                    return "checkUriPermission";
                case 45:
                    return "grantUriPermission";
                case 46:
                    return "revokeUriPermission";
                case 47:
                    return "setActivityController";
                case 48:
                    return "showWaitingForDebugger";
                case 49:
                    return "signalPersistentProcesses";
                case 50:
                    return "getRecentTasks";
                case 51:
                    return "serviceDoneExecuting";
                case 52:
                    return "getIntentSender";
                case 53:
                    return "cancelIntentSender";
                case 54:
                    return "getPackageForIntentSender";
                case 55:
                    return "registerIntentSenderCancelListener";
                case 56:
                    return "unregisterIntentSenderCancelListener";
                case 57:
                    return "enterSafeMode";
                case 58:
                    return "noteWakeupAlarm";
                case 59:
                    return "removeContentProvider";
                case 60:
                    return "setRequestedOrientation";
                case 61:
                    return "unbindFinished";
                case 62:
                    return "setProcessImportant";
                case 63:
                    return "setServiceForeground";
                case 64:
                    return "getForegroundServiceType";
                case 65:
                    return "moveActivityTaskToBack";
                case 66:
                    return "getMemoryInfo";
                case 67:
                    return "getProcessesInErrorState";
                case 68:
                    return "clearApplicationUserData";
                case 69:
                    return "forceStopPackage";
                case 70:
                    return "killPids";
                case 71:
                    return "getServices";
                case 72:
                    return "getRunningAppProcesses";
                case 73:
                    return "peekService";
                case 74:
                    return "profileControl";
                case 75:
                    return "shutdown";
                case 76:
                    return "stopAppSwitches";
                case 77:
                    return "resumeAppSwitches";
                case 78:
                    return "bindBackupAgent";
                case 79:
                    return "backupAgentCreated";
                case 80:
                    return "unbindBackupAgent";
                case 81:
                    return "getUidForIntentSender";
                case 82:
                    return "handleIncomingUser";
                case 83:
                    return "addPackageDependency";
                case 84:
                    return "killApplication";
                case 85:
                    return "closeSystemDialogs";
                case 86:
                    return "getProcessMemoryInfo";
                case 87:
                    return "killApplicationProcess";
                case 88:
                    return "handleApplicationWtf";
                case 89:
                    return "killBackgroundProcesses";
                case 90:
                    return "isUserAMonkey";
                case 91:
                    return "getRunningExternalApplications";
                case 92:
                    return "finishHeavyWeightApp";
                case 93:
                    return "handleApplicationStrictModeViolation";
                case 94:
                    return "isTopActivityImmersive";
                case 95:
                    return "crashApplication";
                case 96:
                    return "getProviderMimeType";
                case 97:
                    return "dumpHeap";
                case 98:
                    return "isUserRunning";
                case 99:
                    return "setPackageScreenCompatMode";
                case 100:
                    return "switchUser";
                case 101:
                    return "removeTask";
                case 102:
                    return "registerProcessObserver";
                case 103:
                    return "unregisterProcessObserver";
                case 104:
                    return "isIntentSenderTargetedToPackage";
                case 105:
                    return "updatePersistentConfiguration";
                case 106:
                    return "getProcessPss";
                case 107:
                    return "showBootMessage";
                case 108:
                    return "killAllBackgroundProcesses";
                case 109:
                    return "getContentProviderExternal";
                case 110:
                    return "removeContentProviderExternal";
                case 111:
                    return "removeContentProviderExternalAsUser";
                case 112:
                    return "getMyMemoryState";
                case 113:
                    return "killProcessesBelowForeground";
                case 114:
                    return "getCurrentUser";
                case 115:
                    return "getLaunchedFromUid";
                case 116:
                    return "unstableProviderDied";
                case 117:
                    return "isIntentSenderAnActivity";
                case 118:
                    return "isIntentSenderAForegroundService";
                case 119:
                    return "isIntentSenderABroadcast";
                case 120:
                    return "startActivityAsUser";
                case 121:
                    return "stopUser";
                case 122:
                    return "registerUserSwitchObserver";
                case 123:
                    return "unregisterUserSwitchObserver";
                case 124:
                    return "getRunningUserIds";
                case 125:
                    return "requestSystemServerHeapDump";
                case 126:
                    return "requestBugReport";
                case 127:
                    return "requestTelephonyBugReport";
                case 128:
                    return "requestWifiBugReport";
                case 129:
                    return "getIntentForIntentSender";
                case 130:
                    return "getLaunchedFromPackage";
                case 131:
                    return "killUid";
                case 132:
                    return "setUserIsMonkey";
                case 133:
                    return "hang";
                case 134:
                    return "getAllStackInfos";
                case 135:
                    return "moveTaskToStack";
                case 136:
                    return "resizeStack";
                case 137:
                    return "setFocusedStack";
                case 138:
                    return "getFocusedStackInfo";
                case 139:
                    return "restart";
                case 140:
                    return "performIdleMaintenance";
                case 141:
                    return "appNotRespondingViaProvider";
                case 142:
                    return "getTaskBounds";
                case 143:
                    return "setProcessMemoryTrimLevel";
                case 144:
                    return "getTagForIntentSender";
                case 145:
                    return "startUserInBackground";
                case 146:
                    return "isInLockTaskMode";
                case 147:
                    return "startRecentsActivity";
                case 148:
                    return "cancelRecentsAnimation";
                case 149:
                    return "startActivityFromRecents";
                case 150:
                    return "startSystemLockTaskMode";
                case 151:
                    return "isTopOfTask";
                case 152:
                    return "bootAnimationComplete";
                case 153:
                    return "checkPermissionWithToken";
                case 154:
                    return "registerTaskStackListener";
                case 155:
                    return "unregisterTaskStackListener";
                case 156:
                    return "notifyCleartextNetwork";
                case 157:
                    return "setTaskResizeable";
                case 158:
                    return "resizeTask";
                case 159:
                    return "getLockTaskModeState";
                case 160:
                    return "setDumpHeapDebugLimit";
                case 161:
                    return "dumpHeapFinished";
                case 162:
                    return "updateLockTaskPackages";
                case 163:
                    return "noteAlarmStart";
                case 164:
                    return "noteAlarmFinish";
                case 165:
                    return "getPackageProcessState";
                case 166:
                    return "updateDeviceOwner";
                case 167:
                    return "startBinderTracking";
                case 168:
                    return "stopBinderTrackingAndDump";
                case 169:
                    return "positionTaskInStack";
                case 170:
                    return "suppressResizeConfigChanges";
                case 171:
                    return "moveTopActivityToPinnedStack";
                case 172:
                    return "isAppStartModeDisabled";
                case 173:
                    return "unlockUser";
                case 174:
                    return "killPackageDependents";
                case 175:
                    return "resizeDockedStack";
                case 176:
                    return "removeStack";
                case 177:
                    return "makePackageIdle";
                case 178:
                    return "getMemoryTrimLevel";
                case 179:
                    return "isVrModePackageEnabled";
                case 180:
                    return "notifyLockedProfile";
                case 181:
                    return "startConfirmDeviceCredentialIntent";
                case 182:
                    return "sendIdleJobTrigger";
                case 183:
                    return "sendIntentSender";
                case 184:
                    return "isBackgroundRestricted";
                case 185:
                    return "setRenderThread";
                case 186:
                    return "setHasTopUi";
                case 187:
                    return "restartUserInBackground";
                case 188:
                    return "cancelTaskWindowTransition";
                case 189:
                    return "getTaskSnapshot";
                case 190:
                    return "scheduleApplicationInfoChanged";
                case 191:
                    return "setPersistentVrThread";
                case 192:
                    return "waitForNetworkStateUpdate";
                case 193:
                    return "backgroundWhitelistUid";
                case 194:
                    return "startUserInBackgroundWithListener";
                case 195:
                    return "startDelegateShellPermissionIdentity";
                case 196:
                    return "stopDelegateShellPermissionIdentity";
                case 197:
                    return "getLifeMonitor";
                case 198:
                    return "startUserInForegroundWithListener";
                case 199:
                    return "finishMiniProgram";
                case 200:
                    return "setFocusedAppNoChecked";
                case 201:
                    return "handleActivityChanged";
                case 202:
                    return "isTopActivityFullscreen";
                case 203:
                    return "forceGrantFolderPermission";
                case 204:
                    return "getOption";
                case 205:
                    return "getUsageInfo";
                case 206:
                    return "getDialogRecorder";
                case 207:
                    return "setDialogRecorder";
                case 208:
                    return "dismissDialog";
                case 209:
                    return "setHomeState";
                case 210:
                    return "getSpeechObserver";
                case 211:
                    return "notifyInstallPersistentPackage";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ApplicationErrorReport.ParcelableCrashInfo _arg1;
            Intent _arg2;
            ProfilerInfo _arg8;
            Bundle _arg9;
            Intent _arg12;
            Intent _arg22;
            IntentFilter _arg3;
            Intent _arg13;
            Bundle _arg6;
            Bundle _arg92;
            Intent _arg14;
            Bundle _arg32;
            Bundle _arg4;
            ComponentName _arg0;
            Intent _arg15;
            Intent _arg16;
            Intent _arg23;
            Intent _arg24;
            Intent _arg17;
            ComponentName _arg02;
            Bundle _arg33;
            Bundle _arg18;
            Bundle _arg25;
            Configuration _arg03;
            ComponentName _arg04;
            Uri _arg05;
            Uri _arg26;
            Uri _arg27;
            Bundle _arg82;
            WorkSource _arg19;
            Intent _arg110;
            ComponentName _arg06;
            Notification _arg34;
            ComponentName _arg07;
            Intent _arg08;
            ProfilerInfo _arg35;
            ApplicationInfo _arg09;
            ApplicationErrorReport.ParcelableCrashInfo _arg36;
            StrictMode.ViolationInfo _arg28;
            Uri _arg010;
            ParcelFileDescriptor _arg62;
            RemoteCallback _arg7;
            Configuration _arg011;
            CharSequence _arg012;
            Intent _arg29;
            ProfilerInfo _arg83;
            Bundle _arg93;
            Rect _arg111;
            Intent _arg013;
            Bundle _arg112;
            Rect _arg113;
            WorkSource _arg114;
            WorkSource _arg115;
            ParcelFileDescriptor _arg014;
            Rect _arg116;
            Rect _arg015;
            Rect _arg117;
            Rect _arg210;
            Rect _arg37;
            Rect _arg42;
            ComponentName _arg016;
            Intent _arg017;
            Bundle _arg118;
            Intent _arg38;
            Bundle _arg72;
            Bundle _arg018;
            xpDialogInfo _arg019;
            ComponentName _arg020;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    ParcelFileDescriptor _result = openContentUri(_arg021);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IUidObserver _arg022 = IUidObserver.Stub.asInterface(data.readStrongBinder());
                    int _arg119 = data.readInt();
                    int _arg211 = data.readInt();
                    String _arg39 = data.readString();
                    registerUidObserver(_arg022, _arg119, _arg211, _arg39);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IUidObserver _arg023 = IUidObserver.Stub.asInterface(data.readStrongBinder());
                    unregisterUidObserver(_arg023);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    String _arg120 = data.readString();
                    boolean isUidActive = isUidActive(_arg024, _arg120);
                    reply.writeNoException();
                    reply.writeInt(isUidActive ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    String _arg121 = data.readString();
                    int _result2 = getUidProcessState(_arg025, _arg121);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg026 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = ApplicationErrorReport.ParcelableCrashInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    handleApplicationCrash(_arg026, _arg1);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg027 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg122 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    String _arg310 = data.readString();
                    IBinder _arg43 = data.readStrongBinder();
                    String _arg5 = data.readString();
                    int _arg63 = data.readInt();
                    int _arg73 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg8 = ProfilerInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg8 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg9 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg9 = null;
                    }
                    int _result3 = startActivity(_arg027, _arg122, _arg2, _arg310, _arg43, _arg5, _arg63, _arg73, _arg8, _arg9);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg028 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    int _result4 = startXpApp(_arg028, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    unhandledBack();
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg029 = data.readStrongBinder();
                    int _arg123 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg22 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    int _arg311 = data.readInt();
                    boolean finishActivity = finishActivity(_arg029, _arg123, _arg22, _arg311);
                    reply.writeNoException();
                    reply.writeInt(finishActivity ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg030 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg124 = data.readString();
                    IIntentReceiver _arg212 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg3 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    String _arg44 = data.readString();
                    int _arg52 = data.readInt();
                    int _arg64 = data.readInt();
                    Intent _result5 = registerReceiver(_arg030, _arg124, _arg212, _arg3, _arg44, _arg52, _arg64);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentReceiver _arg031 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                    unregisterReceiver(_arg031);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg032 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg13 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    String _arg213 = data.readString();
                    IIntentReceiver _arg312 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                    int _arg45 = data.readInt();
                    String _arg53 = data.readString();
                    if (data.readInt() != 0) {
                        _arg6 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    String[] _arg74 = data.createStringArray();
                    int _arg84 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg92 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg92 = null;
                    }
                    boolean _arg10 = data.readInt() != 0;
                    boolean _arg11 = data.readInt() != 0;
                    int _arg125 = data.readInt();
                    int _result6 = broadcastIntent(_arg032, _arg13, _arg213, _arg312, _arg45, _arg53, _arg6, _arg74, _arg84, _arg92, _arg10, _arg11, _arg125);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg033 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg14 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    int _arg214 = data.readInt();
                    unbroadcastIntent(_arg033, _arg14, _arg214);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg034 = data.readStrongBinder();
                    int _arg126 = data.readInt();
                    String _arg215 = data.readString();
                    if (data.readInt() != 0) {
                        _arg32 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    boolean _arg46 = data.readInt() != 0;
                    int _arg54 = data.readInt();
                    finishReceiver(_arg034, _arg126, _arg215, _arg32, _arg46, _arg54);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg035 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    long _arg127 = data.readLong();
                    attachApplication(_arg035, _arg127);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    List<ActivityManager.RunningTaskInfo> _result7 = getTasks(_arg036);
                    reply.writeNoException();
                    reply.writeTypedList(_result7);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg037 = data.readInt();
                    int _arg128 = data.readInt();
                    int _arg216 = data.readInt();
                    List<ActivityManager.RunningTaskInfo> _result8 = getFilteredTasks(_arg037, _arg128, _arg216);
                    reply.writeNoException();
                    reply.writeTypedList(_result8);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg038 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg129 = data.readString();
                    int _arg217 = data.readInt();
                    int _arg313 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg4 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    moveTaskToFront(_arg038, _arg129, _arg217, _arg313, _arg4);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg039 = data.readStrongBinder();
                    boolean _arg130 = data.readInt() != 0;
                    int _result9 = getTaskForActivity(_arg039, _arg130);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg040 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg131 = data.readString();
                    String _arg218 = data.readString();
                    int _arg314 = data.readInt();
                    boolean _arg47 = data.readInt() != 0;
                    ContentProviderHolder _result10 = getContentProvider(_arg040, _arg131, _arg218, _arg314, _arg47);
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg041 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    List<ContentProviderHolder> _arg132 = data.createTypedArrayList(ContentProviderHolder.CREATOR);
                    publishContentProviders(_arg041, _arg132);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg042 = data.readStrongBinder();
                    int _arg133 = data.readInt();
                    int _arg219 = data.readInt();
                    boolean refContentProvider = refContentProvider(_arg042, _arg133, _arg219);
                    reply.writeNoException();
                    reply.writeInt(refContentProvider ? 1 : 0);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    PendingIntent _result11 = getRunningServiceControlPanel(_arg0);
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg043 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg15 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    String _arg220 = data.readString();
                    boolean _arg315 = data.readInt() != 0;
                    String _arg48 = data.readString();
                    int _arg55 = data.readInt();
                    ComponentName _result12 = startService(_arg043, _arg15, _arg220, _arg315, _arg48, _arg55);
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg044 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg16 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg16 = null;
                    }
                    String _arg221 = data.readString();
                    int _arg316 = data.readInt();
                    int _result13 = stopService(_arg044, _arg16, _arg221, _arg316);
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg045 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    IBinder _arg134 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg23 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg23 = null;
                    }
                    String _arg317 = data.readString();
                    IServiceConnection _arg49 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
                    int _arg56 = data.readInt();
                    String _arg65 = data.readString();
                    int _arg75 = data.readInt();
                    int _result14 = bindService(_arg045, _arg134, _arg23, _arg317, _arg49, _arg56, _arg65, _arg75);
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg046 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    IBinder _arg135 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg24 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg24 = null;
                    }
                    String _arg318 = data.readString();
                    IServiceConnection _arg410 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
                    int _arg57 = data.readInt();
                    String _arg66 = data.readString();
                    String _arg76 = data.readString();
                    int _arg85 = data.readInt();
                    int _result15 = bindIsolatedService(_arg046, _arg135, _arg24, _arg318, _arg410, _arg57, _arg66, _arg76, _arg85);
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    IServiceConnection _arg047 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
                    int _arg136 = data.readInt();
                    int _arg222 = data.readInt();
                    updateServiceGroup(_arg047, _arg136, _arg222);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    IServiceConnection _arg048 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
                    boolean unbindService = unbindService(_arg048);
                    reply.writeNoException();
                    reply.writeInt(unbindService ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg049 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg17 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg17 = null;
                    }
                    IBinder _arg223 = data.readStrongBinder();
                    publishService(_arg049, _arg17, _arg223);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg050 = data.readString();
                    boolean _arg137 = data.readInt() != 0;
                    boolean _arg224 = data.readInt() != 0;
                    setDebugApp(_arg050, _arg137, _arg224);
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg051 = data.readString();
                    String _arg138 = data.readString();
                    setAgentApp(_arg051, _arg138);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg052 = data.readInt() != 0;
                    setAlwaysFinish(_arg052);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    String _arg139 = data.readString();
                    int _arg225 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg33 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg33 = null;
                    }
                    IInstrumentationWatcher _arg411 = IInstrumentationWatcher.Stub.asInterface(data.readStrongBinder());
                    IUiAutomationConnection _arg58 = IUiAutomationConnection.Stub.asInterface(data.readStrongBinder());
                    int _arg67 = data.readInt();
                    String _arg77 = data.readString();
                    boolean startInstrumentation = startInstrumentation(_arg02, _arg139, _arg225, _arg33, _arg411, _arg58, _arg67, _arg77);
                    reply.writeNoException();
                    reply.writeInt(startInstrumentation ? 1 : 0);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg053 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg18 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg18 = null;
                    }
                    addInstrumentationResults(_arg053, _arg18);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg054 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    int _arg140 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg25 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg25 = null;
                    }
                    finishInstrumentation(_arg054, _arg140, _arg25);
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    Configuration _result16 = getConfiguration();
                    reply.writeNoException();
                    if (_result16 != null) {
                        reply.writeInt(1);
                        _result16.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = Configuration.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    boolean updateConfiguration = updateConfiguration(_arg03);
                    reply.writeNoException();
                    reply.writeInt(updateConfiguration ? 1 : 0);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    IBinder _arg141 = data.readStrongBinder();
                    int _arg226 = data.readInt();
                    boolean stopServiceToken = stopServiceToken(_arg04, _arg141, _arg226);
                    reply.writeNoException();
                    reply.writeInt(stopServiceToken ? 1 : 0);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg055 = data.readInt();
                    setProcessLimit(_arg055);
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    int _result17 = getProcessLimit();
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg056 = data.readString();
                    int _arg142 = data.readInt();
                    int _arg227 = data.readInt();
                    int _result18 = checkPermission(_arg056, _arg142, _arg227);
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    int _arg143 = data.readInt();
                    int _arg228 = data.readInt();
                    int _arg319 = data.readInt();
                    int _arg412 = data.readInt();
                    IBinder _arg59 = data.readStrongBinder();
                    int _result19 = checkUriPermission(_arg05, _arg143, _arg228, _arg319, _arg412, _arg59);
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg057 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg144 = data.readString();
                    if (data.readInt() != 0) {
                        _arg26 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg26 = null;
                    }
                    int _arg320 = data.readInt();
                    int _arg413 = data.readInt();
                    grantUriPermission(_arg057, _arg144, _arg26, _arg320, _arg413);
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg058 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg145 = data.readString();
                    if (data.readInt() != 0) {
                        _arg27 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg27 = null;
                    }
                    int _arg321 = data.readInt();
                    int _arg414 = data.readInt();
                    revokeUriPermission(_arg058, _arg145, _arg27, _arg321, _arg414);
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    IActivityController _arg059 = IActivityController.Stub.asInterface(data.readStrongBinder());
                    boolean _arg146 = data.readInt() != 0;
                    setActivityController(_arg059, _arg146);
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg060 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    boolean _arg147 = data.readInt() != 0;
                    showWaitingForDebugger(_arg060, _arg147);
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg061 = data.readInt();
                    signalPersistentProcesses(_arg061);
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg062 = data.readInt();
                    int _arg148 = data.readInt();
                    int _arg229 = data.readInt();
                    ParceledListSlice _result20 = getRecentTasks(_arg062, _arg148, _arg229);
                    reply.writeNoException();
                    if (_result20 != null) {
                        reply.writeInt(1);
                        _result20.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg063 = data.readStrongBinder();
                    int _arg149 = data.readInt();
                    int _arg230 = data.readInt();
                    int _arg322 = data.readInt();
                    serviceDoneExecuting(_arg063, _arg149, _arg230, _arg322);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg064 = data.readInt();
                    String _arg150 = data.readString();
                    IBinder _arg231 = data.readStrongBinder();
                    String _arg323 = data.readString();
                    int _arg415 = data.readInt();
                    Intent[] _arg510 = (Intent[]) data.createTypedArray(Intent.CREATOR);
                    String[] _arg68 = data.createStringArray();
                    int _arg78 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg82 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg82 = null;
                    }
                    int _arg94 = data.readInt();
                    IIntentSender _result21 = getIntentSender(_arg064, _arg150, _arg231, _arg323, _arg415, _arg510, _arg68, _arg78, _arg82, _arg94);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result21 != null ? _result21.asBinder() : null);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg065 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    cancelIntentSender(_arg065);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg066 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    String _result22 = getPackageForIntentSender(_arg066);
                    reply.writeNoException();
                    reply.writeString(_result22);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg067 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    IResultReceiver _arg151 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                    registerIntentSenderCancelListener(_arg067, _arg151);
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg068 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    IResultReceiver _arg152 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                    unregisterIntentSenderCancelListener(_arg068, _arg152);
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    enterSafeMode();
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg069 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg19 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg19 = null;
                    }
                    int _arg232 = data.readInt();
                    String _arg324 = data.readString();
                    String _arg416 = data.readString();
                    noteWakeupAlarm(_arg069, _arg19, _arg232, _arg324, _arg416);
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg070 = data.readStrongBinder();
                    boolean _arg153 = data.readInt() != 0;
                    removeContentProvider(_arg070, _arg153);
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg071 = data.readStrongBinder();
                    int _arg154 = data.readInt();
                    setRequestedOrientation(_arg071, _arg154);
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg072 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg110 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg110 = null;
                    }
                    boolean _arg233 = data.readInt() != 0;
                    unbindFinished(_arg072, _arg110, _arg233);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg073 = data.readStrongBinder();
                    int _arg155 = data.readInt();
                    boolean _arg234 = data.readInt() != 0;
                    String _arg325 = data.readString();
                    setProcessImportant(_arg073, _arg155, _arg234, _arg325);
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    IBinder _arg156 = data.readStrongBinder();
                    int _arg235 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg34 = Notification.CREATOR.createFromParcel(data);
                    } else {
                        _arg34 = null;
                    }
                    int _arg417 = data.readInt();
                    int _arg511 = data.readInt();
                    setServiceForeground(_arg06, _arg156, _arg235, _arg34, _arg417, _arg511);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    IBinder _arg157 = data.readStrongBinder();
                    int _result23 = getForegroundServiceType(_arg07, _arg157);
                    reply.writeNoException();
                    reply.writeInt(_result23);
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg074 = data.readStrongBinder();
                    boolean _arg158 = data.readInt() != 0;
                    boolean moveActivityTaskToBack = moveActivityTaskToBack(_arg074, _arg158);
                    reply.writeNoException();
                    reply.writeInt(moveActivityTaskToBack ? 1 : 0);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    ActivityManager.MemoryInfo _arg075 = new ActivityManager.MemoryInfo();
                    getMemoryInfo(_arg075);
                    reply.writeNoException();
                    reply.writeInt(1);
                    _arg075.writeToParcel(reply, 1);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    List<ActivityManager.ProcessErrorStateInfo> _result24 = getProcessesInErrorState();
                    reply.writeNoException();
                    reply.writeTypedList(_result24);
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg076 = data.readString();
                    boolean _arg159 = data.readInt() != 0;
                    IPackageDataObserver _arg236 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    int _arg326 = data.readInt();
                    boolean clearApplicationUserData = clearApplicationUserData(_arg076, _arg159, _arg236, _arg326);
                    reply.writeNoException();
                    reply.writeInt(clearApplicationUserData ? 1 : 0);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg077 = data.readString();
                    int _arg160 = data.readInt();
                    forceStopPackage(_arg077, _arg160);
                    reply.writeNoException();
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg078 = data.createIntArray();
                    String _arg161 = data.readString();
                    boolean _arg237 = data.readInt() != 0;
                    boolean killPids = killPids(_arg078, _arg161, _arg237);
                    reply.writeNoException();
                    reply.writeInt(killPids ? 1 : 0);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg079 = data.readInt();
                    int _arg162 = data.readInt();
                    List<ActivityManager.RunningServiceInfo> _result25 = getServices(_arg079, _arg162);
                    reply.writeNoException();
                    reply.writeTypedList(_result25);
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    List<ActivityManager.RunningAppProcessInfo> _result26 = getRunningAppProcesses();
                    reply.writeNoException();
                    reply.writeTypedList(_result26);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    String _arg163 = data.readString();
                    String _arg238 = data.readString();
                    IBinder _result27 = peekService(_arg08, _arg163, _arg238);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result27);
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg080 = data.readString();
                    int _arg164 = data.readInt();
                    boolean _arg239 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        _arg35 = ProfilerInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg35 = null;
                    }
                    int _arg418 = data.readInt();
                    boolean profileControl = profileControl(_arg080, _arg164, _arg239, _arg35, _arg418);
                    reply.writeNoException();
                    reply.writeInt(profileControl ? 1 : 0);
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg081 = data.readInt();
                    boolean shutdown = shutdown(_arg081);
                    reply.writeNoException();
                    reply.writeInt(shutdown ? 1 : 0);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    stopAppSwitches();
                    reply.writeNoException();
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    resumeAppSwitches();
                    reply.writeNoException();
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg082 = data.readString();
                    int _arg165 = data.readInt();
                    int _arg240 = data.readInt();
                    boolean bindBackupAgent = bindBackupAgent(_arg082, _arg165, _arg240);
                    reply.writeNoException();
                    reply.writeInt(bindBackupAgent ? 1 : 0);
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg083 = data.readString();
                    IBinder _arg166 = data.readStrongBinder();
                    int _arg241 = data.readInt();
                    backupAgentCreated(_arg083, _arg166, _arg241);
                    reply.writeNoException();
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg09 = ApplicationInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg09 = null;
                    }
                    unbindBackupAgent(_arg09);
                    reply.writeNoException();
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg084 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    int _result28 = getUidForIntentSender(_arg084);
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg085 = data.readInt();
                    int _arg167 = data.readInt();
                    int _arg242 = data.readInt();
                    boolean _arg327 = data.readInt() != 0;
                    boolean _arg419 = data.readInt() != 0;
                    String _arg512 = data.readString();
                    String _arg69 = data.readString();
                    int _result29 = handleIncomingUser(_arg085, _arg167, _arg242, _arg327, _arg419, _arg512, _arg69);
                    reply.writeNoException();
                    reply.writeInt(_result29);
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg086 = data.readString();
                    addPackageDependency(_arg086);
                    reply.writeNoException();
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg087 = data.readString();
                    int _arg168 = data.readInt();
                    int _arg243 = data.readInt();
                    String _arg328 = data.readString();
                    killApplication(_arg087, _arg168, _arg243, _arg328);
                    reply.writeNoException();
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg088 = data.readString();
                    closeSystemDialogs(_arg088);
                    reply.writeNoException();
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg089 = data.createIntArray();
                    Debug.MemoryInfo[] _result30 = getProcessMemoryInfo(_arg089);
                    reply.writeNoException();
                    reply.writeTypedArray(_result30, 1);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg090 = data.readString();
                    int _arg169 = data.readInt();
                    killApplicationProcess(_arg090, _arg169);
                    reply.writeNoException();
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg091 = data.readStrongBinder();
                    String _arg170 = data.readString();
                    boolean _arg244 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        _arg36 = ApplicationErrorReport.ParcelableCrashInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg36 = null;
                    }
                    boolean handleApplicationWtf = handleApplicationWtf(_arg091, _arg170, _arg244, _arg36);
                    reply.writeNoException();
                    reply.writeInt(handleApplicationWtf ? 1 : 0);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg092 = data.readString();
                    int _arg171 = data.readInt();
                    killBackgroundProcesses(_arg092, _arg171);
                    reply.writeNoException();
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isUserAMonkey = isUserAMonkey();
                    reply.writeNoException();
                    reply.writeInt(isUserAMonkey ? 1 : 0);
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    List<ApplicationInfo> _result31 = getRunningExternalApplications();
                    reply.writeNoException();
                    reply.writeTypedList(_result31);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    finishHeavyWeightApp();
                    reply.writeNoException();
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg093 = data.readStrongBinder();
                    int _arg172 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg28 = StrictMode.ViolationInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg28 = null;
                    }
                    handleApplicationStrictModeViolation(_arg093, _arg172, _arg28);
                    reply.writeNoException();
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isTopActivityImmersive = isTopActivityImmersive();
                    reply.writeNoException();
                    reply.writeInt(isTopActivityImmersive ? 1 : 0);
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg094 = data.readInt();
                    int _arg173 = data.readInt();
                    String _arg245 = data.readString();
                    int _arg329 = data.readInt();
                    String _arg420 = data.readString();
                    boolean _arg513 = data.readInt() != 0;
                    crashApplication(_arg094, _arg173, _arg245, _arg329, _arg420, _arg513);
                    reply.writeNoException();
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg010 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg010 = null;
                    }
                    int _arg174 = data.readInt();
                    String _result32 = getProviderMimeType(_arg010, _arg174);
                    reply.writeNoException();
                    reply.writeString(_result32);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg095 = data.readString();
                    int _arg175 = data.readInt();
                    boolean _arg246 = data.readInt() != 0;
                    boolean _arg330 = data.readInt() != 0;
                    boolean _arg421 = data.readInt() != 0;
                    String _arg514 = data.readString();
                    if (data.readInt() != 0) {
                        _arg62 = ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg62 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg7 = RemoteCallback.CREATOR.createFromParcel(data);
                    } else {
                        _arg7 = null;
                    }
                    boolean dumpHeap = dumpHeap(_arg095, _arg175, _arg246, _arg330, _arg421, _arg514, _arg62, _arg7);
                    reply.writeNoException();
                    reply.writeInt(dumpHeap ? 1 : 0);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg096 = data.readInt();
                    int _arg176 = data.readInt();
                    boolean isUserRunning = isUserRunning(_arg096, _arg176);
                    reply.writeNoException();
                    reply.writeInt(isUserRunning ? 1 : 0);
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg097 = data.readString();
                    int _arg177 = data.readInt();
                    setPackageScreenCompatMode(_arg097, _arg177);
                    reply.writeNoException();
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg098 = data.readInt();
                    boolean switchUser = switchUser(_arg098);
                    reply.writeNoException();
                    reply.writeInt(switchUser ? 1 : 0);
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg099 = data.readInt();
                    boolean removeTask = removeTask(_arg099);
                    reply.writeNoException();
                    reply.writeInt(removeTask ? 1 : 0);
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    IProcessObserver _arg0100 = IProcessObserver.Stub.asInterface(data.readStrongBinder());
                    registerProcessObserver(_arg0100);
                    reply.writeNoException();
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    IProcessObserver _arg0101 = IProcessObserver.Stub.asInterface(data.readStrongBinder());
                    unregisterProcessObserver(_arg0101);
                    reply.writeNoException();
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0102 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    boolean isIntentSenderTargetedToPackage = isIntentSenderTargetedToPackage(_arg0102);
                    reply.writeNoException();
                    reply.writeInt(isIntentSenderTargetedToPackage ? 1 : 0);
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg011 = Configuration.CREATOR.createFromParcel(data);
                    } else {
                        _arg011 = null;
                    }
                    updatePersistentConfiguration(_arg011);
                    reply.writeNoException();
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg0103 = data.createIntArray();
                    long[] _result33 = getProcessPss(_arg0103);
                    reply.writeNoException();
                    reply.writeLongArray(_result33);
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg012 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg012 = null;
                    }
                    boolean _arg178 = data.readInt() != 0;
                    showBootMessage(_arg012, _arg178);
                    reply.writeNoException();
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    killAllBackgroundProcesses();
                    reply.writeNoException();
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0104 = data.readString();
                    int _arg179 = data.readInt();
                    IBinder _arg247 = data.readStrongBinder();
                    String _arg331 = data.readString();
                    ContentProviderHolder _result34 = getContentProviderExternal(_arg0104, _arg179, _arg247, _arg331);
                    reply.writeNoException();
                    if (_result34 != null) {
                        reply.writeInt(1);
                        _result34.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0105 = data.readString();
                    IBinder _arg180 = data.readStrongBinder();
                    removeContentProviderExternal(_arg0105, _arg180);
                    reply.writeNoException();
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0106 = data.readString();
                    IBinder _arg181 = data.readStrongBinder();
                    int _arg248 = data.readInt();
                    removeContentProviderExternalAsUser(_arg0106, _arg181, _arg248);
                    reply.writeNoException();
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    ActivityManager.RunningAppProcessInfo _arg0107 = new ActivityManager.RunningAppProcessInfo();
                    getMyMemoryState(_arg0107);
                    reply.writeNoException();
                    reply.writeInt(1);
                    _arg0107.writeToParcel(reply, 1);
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0108 = data.readString();
                    boolean killProcessesBelowForeground = killProcessesBelowForeground(_arg0108);
                    reply.writeNoException();
                    reply.writeInt(killProcessesBelowForeground ? 1 : 0);
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    UserInfo _result35 = getCurrentUser();
                    reply.writeNoException();
                    if (_result35 != null) {
                        reply.writeInt(1);
                        _result35.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0109 = data.readStrongBinder();
                    int _result36 = getLaunchedFromUid(_arg0109);
                    reply.writeNoException();
                    reply.writeInt(_result36);
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0110 = data.readStrongBinder();
                    unstableProviderDied(_arg0110);
                    reply.writeNoException();
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0111 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    boolean isIntentSenderAnActivity = isIntentSenderAnActivity(_arg0111);
                    reply.writeNoException();
                    reply.writeInt(isIntentSenderAnActivity ? 1 : 0);
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0112 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    boolean isIntentSenderAForegroundService = isIntentSenderAForegroundService(_arg0112);
                    reply.writeNoException();
                    reply.writeInt(isIntentSenderAForegroundService ? 1 : 0);
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0113 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    boolean isIntentSenderABroadcast = isIntentSenderABroadcast(_arg0113);
                    reply.writeNoException();
                    reply.writeInt(isIntentSenderABroadcast ? 1 : 0);
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg0114 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg182 = data.readString();
                    if (data.readInt() != 0) {
                        _arg29 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg29 = null;
                    }
                    String _arg332 = data.readString();
                    IBinder _arg422 = data.readStrongBinder();
                    String _arg515 = data.readString();
                    int _arg610 = data.readInt();
                    int _arg79 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg83 = ProfilerInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg83 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg93 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg93 = null;
                    }
                    int _arg102 = data.readInt();
                    int _result37 = startActivityAsUser(_arg0114, _arg182, _arg29, _arg332, _arg422, _arg515, _arg610, _arg79, _arg83, _arg93, _arg102);
                    reply.writeNoException();
                    reply.writeInt(_result37);
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0115 = data.readInt();
                    boolean _arg183 = data.readInt() != 0;
                    IStopUserCallback _arg249 = IStopUserCallback.Stub.asInterface(data.readStrongBinder());
                    int _result38 = stopUser(_arg0115, _arg183, _arg249);
                    reply.writeNoException();
                    reply.writeInt(_result38);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    IUserSwitchObserver _arg0116 = IUserSwitchObserver.Stub.asInterface(data.readStrongBinder());
                    String _arg184 = data.readString();
                    registerUserSwitchObserver(_arg0116, _arg184);
                    reply.writeNoException();
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    IUserSwitchObserver _arg0117 = IUserSwitchObserver.Stub.asInterface(data.readStrongBinder());
                    unregisterUserSwitchObserver(_arg0117);
                    reply.writeNoException();
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result39 = getRunningUserIds();
                    reply.writeNoException();
                    reply.writeIntArray(_result39);
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    requestSystemServerHeapDump();
                    reply.writeNoException();
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0118 = data.readInt();
                    requestBugReport(_arg0118);
                    reply.writeNoException();
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0119 = data.readString();
                    String _arg185 = data.readString();
                    requestTelephonyBugReport(_arg0119, _arg185);
                    reply.writeNoException();
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0120 = data.readString();
                    String _arg186 = data.readString();
                    requestWifiBugReport(_arg0120, _arg186);
                    reply.writeNoException();
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0121 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    Intent _result40 = getIntentForIntentSender(_arg0121);
                    reply.writeNoException();
                    if (_result40 != null) {
                        reply.writeInt(1);
                        _result40.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0122 = data.readStrongBinder();
                    String _result41 = getLaunchedFromPackage(_arg0122);
                    reply.writeNoException();
                    reply.writeString(_result41);
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0123 = data.readInt();
                    int _arg187 = data.readInt();
                    String _arg250 = data.readString();
                    killUid(_arg0123, _arg187, _arg250);
                    reply.writeNoException();
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg0124 = data.readInt() != 0;
                    setUserIsMonkey(_arg0124);
                    reply.writeNoException();
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0125 = data.readStrongBinder();
                    boolean _arg188 = data.readInt() != 0;
                    hang(_arg0125, _arg188);
                    reply.writeNoException();
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    List<ActivityManager.StackInfo> _result42 = getAllStackInfos();
                    reply.writeNoException();
                    reply.writeTypedList(_result42);
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0126 = data.readInt();
                    int _arg189 = data.readInt();
                    boolean _arg251 = data.readInt() != 0;
                    moveTaskToStack(_arg0126, _arg189, _arg251);
                    reply.writeNoException();
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0127 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg111 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg111 = null;
                    }
                    boolean _arg252 = data.readInt() != 0;
                    boolean _arg333 = data.readInt() != 0;
                    boolean _arg423 = data.readInt() != 0;
                    int _arg516 = data.readInt();
                    resizeStack(_arg0127, _arg111, _arg252, _arg333, _arg423, _arg516);
                    reply.writeNoException();
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0128 = data.readInt();
                    setFocusedStack(_arg0128);
                    reply.writeNoException();
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    ActivityManager.StackInfo _result43 = getFocusedStackInfo();
                    reply.writeNoException();
                    if (_result43 != null) {
                        reply.writeInt(1);
                        _result43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    restart();
                    reply.writeNoException();
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    performIdleMaintenance();
                    reply.writeNoException();
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0129 = data.readStrongBinder();
                    appNotRespondingViaProvider(_arg0129);
                    reply.writeNoException();
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0130 = data.readInt();
                    Rect _result44 = getTaskBounds(_arg0130);
                    reply.writeNoException();
                    if (_result44 != null) {
                        reply.writeInt(1);
                        _result44.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0131 = data.readString();
                    int _arg190 = data.readInt();
                    int _arg253 = data.readInt();
                    boolean processMemoryTrimLevel = setProcessMemoryTrimLevel(_arg0131, _arg190, _arg253);
                    reply.writeNoException();
                    reply.writeInt(processMemoryTrimLevel ? 1 : 0);
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0132 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    String _arg191 = data.readString();
                    String _result45 = getTagForIntentSender(_arg0132, _arg191);
                    reply.writeNoException();
                    reply.writeString(_result45);
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0133 = data.readInt();
                    boolean startUserInBackground = startUserInBackground(_arg0133);
                    reply.writeNoException();
                    reply.writeInt(startUserInBackground ? 1 : 0);
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isInLockTaskMode = isInLockTaskMode();
                    reply.writeNoException();
                    reply.writeInt(isInLockTaskMode ? 1 : 0);
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg013 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg013 = null;
                    }
                    IAssistDataReceiver _arg192 = IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
                    IRecentsAnimationRunner _arg254 = IRecentsAnimationRunner.Stub.asInterface(data.readStrongBinder());
                    startRecentsActivity(_arg013, _arg192, _arg254);
                    reply.writeNoException();
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg0134 = data.readInt() != 0;
                    cancelRecentsAnimation(_arg0134);
                    reply.writeNoException();
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0135 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg112 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg112 = null;
                    }
                    int _result46 = startActivityFromRecents(_arg0135, _arg112);
                    reply.writeNoException();
                    reply.writeInt(_result46);
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0136 = data.readInt();
                    startSystemLockTaskMode(_arg0136);
                    reply.writeNoException();
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0137 = data.readStrongBinder();
                    boolean isTopOfTask = isTopOfTask(_arg0137);
                    reply.writeNoException();
                    reply.writeInt(isTopOfTask ? 1 : 0);
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    bootAnimationComplete();
                    reply.writeNoException();
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0138 = data.readString();
                    int _arg193 = data.readInt();
                    int _arg255 = data.readInt();
                    IBinder _arg334 = data.readStrongBinder();
                    int _result47 = checkPermissionWithToken(_arg0138, _arg193, _arg255, _arg334);
                    reply.writeNoException();
                    reply.writeInt(_result47);
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    ITaskStackListener _arg0139 = ITaskStackListener.Stub.asInterface(data.readStrongBinder());
                    registerTaskStackListener(_arg0139);
                    reply.writeNoException();
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    ITaskStackListener _arg0140 = ITaskStackListener.Stub.asInterface(data.readStrongBinder());
                    unregisterTaskStackListener(_arg0140);
                    reply.writeNoException();
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0141 = data.readInt();
                    byte[] _arg194 = data.createByteArray();
                    notifyCleartextNetwork(_arg0141, _arg194);
                    reply.writeNoException();
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0142 = data.readInt();
                    int _arg195 = data.readInt();
                    setTaskResizeable(_arg0142, _arg195);
                    reply.writeNoException();
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0143 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg113 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg113 = null;
                    }
                    int _arg256 = data.readInt();
                    resizeTask(_arg0143, _arg113, _arg256);
                    reply.writeNoException();
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    int _result48 = getLockTaskModeState();
                    reply.writeNoException();
                    reply.writeInt(_result48);
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0144 = data.readString();
                    int _arg196 = data.readInt();
                    long _arg257 = data.readLong();
                    String _arg335 = data.readString();
                    setDumpHeapDebugLimit(_arg0144, _arg196, _arg257, _arg335);
                    reply.writeNoException();
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0145 = data.readString();
                    dumpHeapFinished(_arg0145);
                    reply.writeNoException();
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0146 = data.readInt();
                    String[] _arg197 = data.createStringArray();
                    updateLockTaskPackages(_arg0146, _arg197);
                    reply.writeNoException();
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0147 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg114 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg114 = null;
                    }
                    int _arg258 = data.readInt();
                    String _arg336 = data.readString();
                    noteAlarmStart(_arg0147, _arg114, _arg258, _arg336);
                    reply.writeNoException();
                    return true;
                case 164:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0148 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg115 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg115 = null;
                    }
                    int _arg259 = data.readInt();
                    String _arg337 = data.readString();
                    noteAlarmFinish(_arg0148, _arg115, _arg259, _arg337);
                    reply.writeNoException();
                    return true;
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0149 = data.readString();
                    String _arg198 = data.readString();
                    int _result49 = getPackageProcessState(_arg0149, _arg198);
                    reply.writeNoException();
                    reply.writeInt(_result49);
                    return true;
                case 166:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0150 = data.readString();
                    updateDeviceOwner(_arg0150);
                    reply.writeNoException();
                    return true;
                case 167:
                    data.enforceInterface(DESCRIPTOR);
                    boolean startBinderTracking = startBinderTracking();
                    reply.writeNoException();
                    reply.writeInt(startBinderTracking ? 1 : 0);
                    return true;
                case 168:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg014 = ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg014 = null;
                    }
                    boolean stopBinderTrackingAndDump = stopBinderTrackingAndDump(_arg014);
                    reply.writeNoException();
                    reply.writeInt(stopBinderTrackingAndDump ? 1 : 0);
                    return true;
                case 169:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0151 = data.readInt();
                    int _arg199 = data.readInt();
                    int _arg260 = data.readInt();
                    positionTaskInStack(_arg0151, _arg199, _arg260);
                    reply.writeNoException();
                    return true;
                case 170:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg0152 = data.readInt() != 0;
                    suppressResizeConfigChanges(_arg0152);
                    reply.writeNoException();
                    return true;
                case 171:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0153 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg116 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg116 = null;
                    }
                    boolean moveTopActivityToPinnedStack = moveTopActivityToPinnedStack(_arg0153, _arg116);
                    reply.writeNoException();
                    reply.writeInt(moveTopActivityToPinnedStack ? 1 : 0);
                    return true;
                case 172:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0154 = data.readInt();
                    String _arg1100 = data.readString();
                    boolean isAppStartModeDisabled = isAppStartModeDisabled(_arg0154, _arg1100);
                    reply.writeNoException();
                    reply.writeInt(isAppStartModeDisabled ? 1 : 0);
                    return true;
                case 173:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0155 = data.readInt();
                    byte[] _arg1101 = data.createByteArray();
                    byte[] _arg261 = data.createByteArray();
                    IProgressListener _arg338 = IProgressListener.Stub.asInterface(data.readStrongBinder());
                    boolean unlockUser = unlockUser(_arg0155, _arg1101, _arg261, _arg338);
                    reply.writeNoException();
                    reply.writeInt(unlockUser ? 1 : 0);
                    return true;
                case 174:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0156 = data.readString();
                    int _arg1102 = data.readInt();
                    killPackageDependents(_arg0156, _arg1102);
                    reply.writeNoException();
                    return true;
                case 175:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg015 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg015 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg117 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg117 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg210 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg210 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg37 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg37 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg42 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }
                    resizeDockedStack(_arg015, _arg117, _arg210, _arg37, _arg42);
                    reply.writeNoException();
                    return true;
                case 176:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0157 = data.readInt();
                    removeStack(_arg0157);
                    reply.writeNoException();
                    return true;
                case 177:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0158 = data.readString();
                    int _arg1103 = data.readInt();
                    makePackageIdle(_arg0158, _arg1103);
                    reply.writeNoException();
                    return true;
                case 178:
                    data.enforceInterface(DESCRIPTOR);
                    int _result50 = getMemoryTrimLevel();
                    reply.writeNoException();
                    reply.writeInt(_result50);
                    return true;
                case 179:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg016 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg016 = null;
                    }
                    boolean isVrModePackageEnabled = isVrModePackageEnabled(_arg016);
                    reply.writeNoException();
                    reply.writeInt(isVrModePackageEnabled ? 1 : 0);
                    return true;
                case 180:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0159 = data.readInt();
                    notifyLockedProfile(_arg0159);
                    reply.writeNoException();
                    return true;
                case 181:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg017 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg017 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg118 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg118 = null;
                    }
                    startConfirmDeviceCredentialIntent(_arg017, _arg118);
                    reply.writeNoException();
                    return true;
                case 182:
                    data.enforceInterface(DESCRIPTOR);
                    sendIdleJobTrigger();
                    reply.writeNoException();
                    return true;
                case 183:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0160 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    IBinder _arg1104 = data.readStrongBinder();
                    int _arg262 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg38 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg38 = null;
                    }
                    String _arg424 = data.readString();
                    IIntentReceiver _arg517 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                    String _arg611 = data.readString();
                    if (data.readInt() != 0) {
                        _arg72 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg72 = null;
                    }
                    int _result51 = sendIntentSender(_arg0160, _arg1104, _arg262, _arg38, _arg424, _arg517, _arg611, _arg72);
                    reply.writeNoException();
                    reply.writeInt(_result51);
                    return true;
                case 184:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0161 = data.readString();
                    boolean isBackgroundRestricted = isBackgroundRestricted(_arg0161);
                    reply.writeNoException();
                    reply.writeInt(isBackgroundRestricted ? 1 : 0);
                    return true;
                case 185:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0162 = data.readInt();
                    setRenderThread(_arg0162);
                    reply.writeNoException();
                    return true;
                case 186:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg0163 = data.readInt() != 0;
                    setHasTopUi(_arg0163);
                    reply.writeNoException();
                    return true;
                case 187:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0164 = data.readInt();
                    int _result52 = restartUserInBackground(_arg0164);
                    reply.writeNoException();
                    reply.writeInt(_result52);
                    return true;
                case 188:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0165 = data.readInt();
                    cancelTaskWindowTransition(_arg0165);
                    reply.writeNoException();
                    return true;
                case 189:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0166 = data.readInt();
                    boolean _arg1105 = data.readInt() != 0;
                    ActivityManager.TaskSnapshot _result53 = getTaskSnapshot(_arg0166, _arg1105);
                    reply.writeNoException();
                    if (_result53 != null) {
                        reply.writeInt(1);
                        _result53.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 190:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg0167 = data.createStringArrayList();
                    int _arg1106 = data.readInt();
                    scheduleApplicationInfoChanged(_arg0167, _arg1106);
                    reply.writeNoException();
                    return true;
                case 191:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0168 = data.readInt();
                    setPersistentVrThread(_arg0168);
                    reply.writeNoException();
                    return true;
                case 192:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg0169 = data.readLong();
                    waitForNetworkStateUpdate(_arg0169);
                    reply.writeNoException();
                    return true;
                case 193:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0170 = data.readInt();
                    backgroundWhitelistUid(_arg0170);
                    reply.writeNoException();
                    return true;
                case 194:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0171 = data.readInt();
                    IProgressListener _arg1107 = IProgressListener.Stub.asInterface(data.readStrongBinder());
                    boolean startUserInBackgroundWithListener = startUserInBackgroundWithListener(_arg0171, _arg1107);
                    reply.writeNoException();
                    reply.writeInt(startUserInBackgroundWithListener ? 1 : 0);
                    return true;
                case 195:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0172 = data.readInt();
                    String[] _arg1108 = data.createStringArray();
                    startDelegateShellPermissionIdentity(_arg0172, _arg1108);
                    reply.writeNoException();
                    return true;
                case 196:
                    data.enforceInterface(DESCRIPTOR);
                    stopDelegateShellPermissionIdentity();
                    reply.writeNoException();
                    return true;
                case 197:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _result54 = getLifeMonitor();
                    reply.writeNoException();
                    if (_result54 != null) {
                        reply.writeInt(1);
                        _result54.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 198:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0173 = data.readInt();
                    IProgressListener _arg1109 = IProgressListener.Stub.asInterface(data.readStrongBinder());
                    boolean startUserInForegroundWithListener = startUserInForegroundWithListener(_arg0173, _arg1109);
                    reply.writeNoException();
                    reply.writeInt(startUserInForegroundWithListener ? 1 : 0);
                    return true;
                case 199:
                    data.enforceInterface(DESCRIPTOR);
                    finishMiniProgram();
                    reply.writeNoException();
                    return true;
                case 200:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0174 = data.readInt();
                    setFocusedAppNoChecked(_arg0174);
                    reply.writeNoException();
                    return true;
                case 201:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg018 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg018 = null;
                    }
                    handleActivityChanged(_arg018);
                    reply.writeNoException();
                    return true;
                case 202:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isTopActivityFullscreen = isTopActivityFullscreen();
                    reply.writeNoException();
                    reply.writeInt(isTopActivityFullscreen ? 1 : 0);
                    return true;
                case 203:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0175 = data.readString();
                    forceGrantFolderPermission(_arg0175);
                    reply.writeNoException();
                    return true;
                case 204:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0176 = data.readString();
                    String _arg1110 = data.readString();
                    String _result55 = getOption(_arg0176, _arg1110);
                    reply.writeNoException();
                    reply.writeString(_result55);
                    return true;
                case 205:
                    data.enforceInterface(DESCRIPTOR);
                    double[] _result56 = getUsageInfo();
                    reply.writeNoException();
                    reply.writeDoubleArray(_result56);
                    return true;
                case 206:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg0177 = data.readInt() != 0;
                    List<xpDialogInfo> _result57 = getDialogRecorder(_arg0177);
                    reply.writeNoException();
                    reply.writeTypedList(_result57);
                    return true;
                case 207:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg019 = xpDialogInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg019 = null;
                    }
                    setDialogRecorder(_arg019);
                    reply.writeNoException();
                    return true;
                case 208:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0178 = data.readInt();
                    dismissDialog(_arg0178);
                    reply.writeNoException();
                    return true;
                case 209:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg020 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg020 = null;
                    }
                    int _arg1111 = data.readInt();
                    setHomeState(_arg020, _arg1111);
                    reply.writeNoException();
                    return true;
                case 210:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result58 = getSpeechObserver();
                    reply.writeNoException();
                    reply.writeStringList(_result58);
                    return true;
                case 211:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0179 = data.readString();
                    notifyInstallPersistentPackage(_arg0179);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IActivityManager {
            public static IActivityManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.IActivityManager
            public ParcelFileDescriptor openContentUri(String uriString) throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uriString);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().openContentUri(uriString);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void registerUidObserver(IUidObserver observer, int which, int cutpoint, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(which);
                    _data.writeInt(cutpoint);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerUidObserver(observer, which, cutpoint, callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterUidObserver(IUidObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterUidObserver(observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isUidActive(int uid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUidActive(uid, callingPackage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getUidProcessState(int uid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUidProcessState(uid, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void handleApplicationCrash(IBinder app, ApplicationErrorReport.ParcelableCrashInfo crashInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app);
                    if (crashInfo != null) {
                        _data.writeInt(1);
                        crashInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleApplicationCrash(app, crashInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
                Parcel _reply;
                IBinder asBinder;
                Parcel _data = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data.writeStrongBinder(asBinder);
                    _data.writeString(callingPackage);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeStrongBinder(resultTo);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    _data.writeInt(flags);
                    if (profilerInfo != null) {
                        _data.writeInt(1);
                        profilerInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(7, _data, _reply2, 0);
                    try {
                        if (!_status && Stub.getDefaultImpl() != null) {
                            int startActivity = Stub.getDefaultImpl().startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
                            _reply2.recycle();
                            _data.recycle();
                            return startActivity;
                        }
                        _reply2.readException();
                        int _result = _reply2.readInt();
                        _reply2.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _reply = _reply2;
                }
            }

            @Override // android.app.IActivityManager
            public int startXpApp(String pkgName, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startXpApp(pkgName, intent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unhandledBack() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unhandledBack();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean finishActivity(IBinder token, int code, Intent data, int finishTask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(code);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(finishTask);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().finishActivity(token, code, data, finishTask);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public Intent registerReceiver(IApplicationThread caller, String callerPackage, IIntentReceiver receiver, IntentFilter filter, String requiredPermission, int userId, int flags) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(callerPackage);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(requiredPermission);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(userId);
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Intent registerReceiver = Stub.getDefaultImpl().registerReceiver(caller, callerPackage, receiver, filter, requiredPermission, userId, flags);
                        _reply.recycle();
                        _data.recycle();
                        return registerReceiver;
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterReceiver(IIntentReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterReceiver(receiver);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int broadcastIntent(IApplicationThread caller, Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode, String resultData, Bundle map, String[] requiredPermissions, int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                boolean _status;
                Parcel _data2 = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                try {
                    _data2.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply2;
                            _data = _data2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data2.writeStrongBinder(asBinder);
                    int i = 1;
                    if (intent != null) {
                        _data2.writeInt(1);
                        intent.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeString(resolvedType);
                    _data2.writeStrongBinder(resultTo != null ? resultTo.asBinder() : null);
                    _data2.writeInt(resultCode);
                    _data2.writeString(resultData);
                    if (map != null) {
                        _data2.writeInt(1);
                        map.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeStringArray(requiredPermissions);
                    _data2.writeInt(appOp);
                    if (options != null) {
                        _data2.writeInt(1);
                        options.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeInt(serialized ? 1 : 0);
                    if (!sticky) {
                        i = 0;
                    }
                    _data2.writeInt(i);
                    _data2.writeInt(userId);
                    _status = this.mRemote.transact(13, _data2, _reply2, 0);
                } catch (Throwable th2) {
                    th = th2;
                    _reply = _reply2;
                    _data = _data2;
                }
                try {
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int broadcastIntent = Stub.getDefaultImpl().broadcastIntent(caller, intent, resolvedType, resultTo, resultCode, resultData, map, requiredPermissions, appOp, options, serialized, sticky, userId);
                        _reply2.recycle();
                        _data2.recycle();
                        return broadcastIntent;
                    }
                    _reply2.readException();
                    int _result = _reply2.readInt();
                    _reply2.recycle();
                    _data2.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public void unbroadcastIntent(IApplicationThread caller, Intent intent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unbroadcastIntent(caller, intent, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void finishReceiver(IBinder who, int resultCode, String resultData, Bundle map, boolean abortBroadcast, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(who);
                } catch (Throwable th2) {
                    th = th2;
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(resultCode);
                } catch (Throwable th3) {
                    th = th3;
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(resultData);
                    if (map != null) {
                        _data.writeInt(1);
                        map.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(abortBroadcast ? 1 : 0);
                    try {
                        _data.writeInt(flags);
                    } catch (Throwable th4) {
                        th = th4;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    _data.recycle();
                    throw th;
                }
                try {
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishReceiver(who, resultCode, resultData, map, abortBroadcast, flags);
                        _data.recycle();
                        return;
                    }
                    _data.recycle();
                } catch (Throwable th6) {
                    th = th6;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public void attachApplication(IApplicationThread app, long startSeq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app != null ? app.asBinder() : null);
                    _data.writeLong(startSeq);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().attachApplication(app, startSeq);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTasks(maxNum);
                    }
                    _reply.readException();
                    List<ActivityManager.RunningTaskInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningTaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ActivityManager.RunningTaskInfo> getFilteredTasks(int maxNum, int ignoreActivityType, int ignoreWindowingMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(ignoreActivityType);
                    _data.writeInt(ignoreWindowingMode);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFilteredTasks(maxNum, ignoreActivityType, ignoreWindowingMode);
                    }
                    _reply.readException();
                    List<ActivityManager.RunningTaskInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningTaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void moveTaskToFront(IApplicationThread caller, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeInt(task);
                    _data.writeInt(flags);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moveTaskToFront(caller, callingPackage, task, flags, options);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(onlyRoot ? 1 : 0);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTaskForActivity(token, onlyRoot);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ContentProviderHolder getContentProvider(IApplicationThread caller, String callingPackage, String name, int userId, boolean stable) throws RemoteException {
                ContentProviderHolder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeInt(stable ? 1 : 0);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getContentProvider(caller, callingPackage, name, userId, stable);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ContentProviderHolder.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void publishContentProviders(IApplicationThread caller, List<ContentProviderHolder> providers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeTypedList(providers);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().publishContentProviders(caller, providers);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean refContentProvider(IBinder connection, int stableDelta, int unstableDelta) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    _data.writeInt(stableDelta);
                    _data.writeInt(unstableDelta);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().refContentProvider(connection, stableDelta, unstableDelta);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public PendingIntent getRunningServiceControlPanel(ComponentName service) throws RemoteException {
                PendingIntent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRunningServiceControlPanel(service);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PendingIntent.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ComponentName startService(IApplicationThread caller, Intent service, String resolvedType, boolean requireForeground, String callingPackage, int userId) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    int i = 1;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(resolvedType);
                        if (!requireForeground) {
                            i = 0;
                        }
                        _data.writeInt(i);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPackage);
                        try {
                            _data.writeInt(userId);
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            ComponentName startService = Stub.getDefaultImpl().startService(caller, service, resolvedType, requireForeground, callingPackage, userId);
                            _reply.recycle();
                            _data.recycle();
                            return startService;
                        }
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            _result = ComponentName.CREATOR.createFromParcel(_reply);
                        } else {
                            _result = null;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }

            @Override // android.app.IActivityManager
            public int stopService(IApplicationThread caller, Intent service, String resolvedType, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopService(caller, service, resolvedType, userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int bindService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(token);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(resolvedType);
                        _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(flags);
                        _data.writeString(callingPackage);
                        _data.writeInt(userId);
                        boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            int bindService = Stub.getDefaultImpl().bindService(caller, token, service, resolvedType, connection, flags, callingPackage, userId);
                            _reply.recycle();
                            _data.recycle();
                            return bindService;
                        }
                        _reply.readException();
                        int _result = _reply.readInt();
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public int bindIsolatedService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String instanceName, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(token);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(resolvedType);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeString(instanceName);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int bindIsolatedService = Stub.getDefaultImpl().bindIsolatedService(caller, token, service, resolvedType, connection, flags, instanceName, callingPackage, userId);
                        _reply.recycle();
                        _data.recycle();
                        return bindIsolatedService;
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public void updateServiceGroup(IServiceConnection connection, int group, int importance) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    _data.writeInt(group);
                    _data.writeInt(importance);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateServiceGroup(connection, group, importance);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean unbindService(IServiceConnection connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unbindService(connection);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void publishService(IBinder token, Intent intent, IBinder service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(service);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().publishService(token, intent, service);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setDebugApp(String packageName, boolean waitForDebugger, boolean persistent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int i = 1;
                    _data.writeInt(waitForDebugger ? 1 : 0);
                    if (!persistent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDebugApp(packageName, waitForDebugger, persistent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setAgentApp(String packageName, String agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(agent);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAgentApp(packageName, agent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setAlwaysFinish(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAlwaysFinish(enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean startInstrumentation(ComponentName className, String profileFile, int flags, Bundle arguments, IInstrumentationWatcher watcher, IUiAutomationConnection connection, int userId, String abiOverride) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(profileFile);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(flags);
                    if (arguments != null) {
                        _data.writeInt(1);
                        arguments.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    _data.writeInt(userId);
                    _data.writeString(abiOverride);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean startInstrumentation = Stub.getDefaultImpl().startInstrumentation(className, profileFile, flags, arguments, watcher, connection, userId, abiOverride);
                        _reply.recycle();
                        _data.recycle();
                        return startInstrumentation;
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public void addInstrumentationResults(IApplicationThread target, Bundle results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    if (results != null) {
                        _data.writeInt(1);
                        results.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addInstrumentationResults(target, results);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void finishInstrumentation(IApplicationThread target, int resultCode, Bundle results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    _data.writeInt(resultCode);
                    if (results != null) {
                        _data.writeInt(1);
                        results.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishInstrumentation(target, resultCode, results);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public Configuration getConfiguration() throws RemoteException {
                Configuration _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getConfiguration();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Configuration.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean updateConfiguration(Configuration values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (values != null) {
                        _data.writeInt(1);
                        values.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateConfiguration(values);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean stopServiceToken(ComponentName className, IBinder token, int startId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(token);
                    _data.writeInt(startId);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopServiceToken(className, token, startId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setProcessLimit(int max) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(max);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setProcessLimit(max);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getProcessLimit() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProcessLimit();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int checkPermission(String permission, int pid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkPermission(permission, pid, uid);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int checkUriPermission(Uri uri, int pid, int uid, int mode, int userId, IBinder callerToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(pid);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(uid);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(mode);
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(userId);
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeStrongBinder(callerToken);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int checkUriPermission = Stub.getDefaultImpl().checkUriPermission(uri, pid, uid, mode, userId, callerToken);
                        _reply.recycle();
                        _data.recycle();
                        return checkUriPermission;
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th6) {
                    th = th6;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public void grantUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(targetPkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().grantUriPermission(caller, targetPkg, uri, mode, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void revokeUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(targetPkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().revokeUriPermission(caller, targetPkg, uri, mode, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    _data.writeInt(imAMonkey ? 1 : 0);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setActivityController(watcher, imAMonkey);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void showWaitingForDebugger(IApplicationThread who, boolean waiting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(who != null ? who.asBinder() : null);
                    _data.writeInt(waiting ? 1 : 0);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().showWaitingForDebugger(who, waiting);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void signalPersistentProcesses(int signal) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(signal);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().signalPersistentProcesses(signal);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ParceledListSlice getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRecentTasks(maxNum, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void serviceDoneExecuting(IBinder token, int type, int startId, int res) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(type);
                    _data.writeInt(startId);
                    _data.writeInt(res);
                    boolean _status = this.mRemote.transact(51, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().serviceDoneExecuting(token, type, startId, res);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public IIntentSender getIntentSender(int type, String packageName, IBinder token, String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes, int flags, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(type);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(token);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    _data.writeTypedArray(intents, 0);
                    _data.writeStringArray(resolvedTypes);
                    _data.writeInt(flags);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        IIntentSender intentSender = Stub.getDefaultImpl().getIntentSender(type, packageName, token, resultWho, requestCode, intents, resolvedTypes, flags, options, userId);
                        _reply.recycle();
                        _data.recycle();
                        return intentSender;
                    }
                    _reply.readException();
                    IIntentSender _result = IIntentSender.Stub.asInterface(_reply.readStrongBinder());
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public void cancelIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelIntentSender(sender);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public String getPackageForIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageForIntentSender(sender);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void registerIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerIntentSenderCancelListener(sender, receiver);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterIntentSenderCancelListener(sender, receiver);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void enterSafeMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enterSafeMode();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void noteWakeupAlarm(IIntentSender sender, WorkSource workSource, int sourceUid, String sourcePkg, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sourceUid);
                    _data.writeString(sourcePkg);
                    _data.writeString(tag);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWakeupAlarm(sender, workSource, sourceUid, sourcePkg, tag);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void removeContentProvider(IBinder connection, boolean stable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    _data.writeInt(stable ? 1 : 0);
                    boolean _status = this.mRemote.transact(59, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeContentProvider(connection, stable);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(requestedOrientation);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRequestedOrientation(token, requestedOrientation);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unbindFinished(IBinder token, Intent service, boolean doRebind) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    int i = 1;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!doRebind) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unbindFinished(token, service, doRebind);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setProcessImportant(IBinder token, int pid, boolean isForeground, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(pid);
                    _data.writeInt(isForeground ? 1 : 0);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setProcessImportant(token, pid, isForeground, reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setServiceForeground(ComponentName className, IBinder token, int id, Notification notification, int flags, int foregroundServiceType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(token);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(id);
                    if (notification != null) {
                        _data.writeInt(1);
                        notification.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(flags);
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(foregroundServiceType);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setServiceForeground(className, token, id, notification, flags, foregroundServiceType);
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public int getForegroundServiceType(ComponentName className, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getForegroundServiceType(className, token);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(nonRoot ? 1 : 0);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().moveActivityTaskToBack(token, nonRoot);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void getMemoryInfo(ActivityManager.MemoryInfo outInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getMemoryInfo(outInfo);
                        return;
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        outInfo.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProcessesInErrorState();
                    }
                    _reply.readException();
                    List<ActivityManager.ProcessErrorStateInfo> _result = _reply.createTypedArrayList(ActivityManager.ProcessErrorStateInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean clearApplicationUserData(String packageName, boolean keepState, IPackageDataObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(keepState ? 1 : 0);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().clearApplicationUserData(packageName, keepState, observer, userId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void forceStopPackage(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceStopPackage(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean killPids(int[] pids, String reason, boolean secure) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    _data.writeString(reason);
                    _data.writeInt(secure ? 1 : 0);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().killPids(pids, reason, secure);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ActivityManager.RunningServiceInfo> getServices(int maxNum, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getServices(maxNum, flags);
                    }
                    _reply.readException();
                    List<ActivityManager.RunningServiceInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningServiceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRunningAppProcesses();
                    }
                    _reply.readException();
                    List<ActivityManager.RunningAppProcessInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningAppProcessInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public IBinder peekService(Intent service, String resolvedType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().peekService(service, resolvedType, callingPackage);
                    }
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean profileControl(String process, int userId, boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(process);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(userId);
                    _data.writeInt(start ? 1 : 0);
                    if (profilerInfo != null) {
                        _data.writeInt(1);
                        profilerInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(profileType);
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean profileControl = Stub.getDefaultImpl().profileControl(process, userId, start, profilerInfo, profileType);
                        _reply.recycle();
                        _data.recycle();
                        return profileControl;
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public boolean shutdown(int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeout);
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().shutdown(timeout);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void stopAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopAppSwitches();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void resumeAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resumeAppSwitches();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean bindBackupAgent(String packageName, int backupRestoreMode, int targetUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(backupRestoreMode);
                    _data.writeInt(targetUserId);
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().bindBackupAgent(packageName, backupRestoreMode, targetUserId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void backupAgentCreated(String packageName, IBinder agent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(agent);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().backupAgentCreated(packageName, agent, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unbindBackupAgent(ApplicationInfo appInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appInfo != null) {
                        _data.writeInt(1);
                        appInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unbindBackupAgent(appInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getUidForIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUidForIntentSender(sender);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, String name, String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(callingPid);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(callingUid);
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        int i = 1;
                        _data.writeInt(allowAll ? 1 : 0);
                        if (!requireFull) {
                            i = 0;
                        }
                        _data.writeInt(i);
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(name);
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callerPackage);
                        boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            int handleIncomingUser = Stub.getDefaultImpl().handleIncomingUser(callingPid, callingUid, userId, allowAll, requireFull, name, callerPackage);
                            _reply.recycle();
                            _data.recycle();
                            return handleIncomingUser;
                        }
                        _reply.readException();
                        int _result = _reply.readInt();
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                }
            }

            @Override // android.app.IActivityManager
            public void addPackageDependency(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addPackageDependency(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killApplication(String pkg, int appId, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killApplication(pkg, appId, userId, reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void closeSystemDialogs(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().closeSystemDialogs(reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProcessMemoryInfo(pids);
                    }
                    _reply.readException();
                    Debug.MemoryInfo[] _result = (Debug.MemoryInfo[]) _reply.createTypedArray(Debug.MemoryInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killApplicationProcess(String processName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killApplicationProcess(processName, uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean handleApplicationWtf(IBinder app, String tag, boolean system, ApplicationErrorReport.ParcelableCrashInfo crashInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app);
                    _data.writeString(tag);
                    _data.writeInt(system ? 1 : 0);
                    if (crashInfo != null) {
                        _data.writeInt(1);
                        crashInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().handleApplicationWtf(app, tag, system, crashInfo);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killBackgroundProcesses(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killBackgroundProcesses(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isUserAMonkey() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(90, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUserAMonkey();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ApplicationInfo> getRunningExternalApplications() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(91, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRunningExternalApplications();
                    }
                    _reply.readException();
                    List<ApplicationInfo> _result = _reply.createTypedArrayList(ApplicationInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void finishHeavyWeightApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(92, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishHeavyWeightApp();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void handleApplicationStrictModeViolation(IBinder app, int penaltyMask, StrictMode.ViolationInfo crashInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app);
                    _data.writeInt(penaltyMask);
                    if (crashInfo != null) {
                        _data.writeInt(1);
                        crashInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(93, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleApplicationStrictModeViolation(app, penaltyMask, crashInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isTopActivityImmersive() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(94, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isTopActivityImmersive();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void crashApplication(int uid, int initialPid, String packageName, int userId, String message, boolean force) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(uid);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(initialPid);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(packageName);
                    try {
                        _data.writeInt(userId);
                        try {
                            _data.writeString(message);
                            _data.writeInt(force ? 1 : 0);
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        boolean _status = this.mRemote.transact(95, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().crashApplication(uid, initialPid, packageName, userId, message, force);
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public String getProviderMimeType(Uri uri, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(96, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProviderMimeType(uri, userId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean dumpHeap(String process, int userId, boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(process);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(userId);
                    _data.writeInt(managed ? 1 : 0);
                    _data.writeInt(mallocInfo ? 1 : 0);
                    _data.writeInt(runGc ? 1 : 0);
                    _data.writeString(path);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (finishCallback != null) {
                        _data.writeInt(1);
                        finishCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(97, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean dumpHeap = Stub.getDefaultImpl().dumpHeap(process, userId, managed, mallocInfo, runGc, path, fd, finishCallback);
                        _reply.recycle();
                        _data.recycle();
                        return dumpHeap;
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public boolean isUserRunning(int userid, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(98, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUserRunning(userid, flags);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(99, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPackageScreenCompatMode(packageName, mode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean switchUser(int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    boolean _status = this.mRemote.transact(100, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().switchUser(userid);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean removeTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(101, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeTask(taskId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void registerProcessObserver(IProcessObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(102, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerProcessObserver(observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterProcessObserver(IProcessObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(103, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterProcessObserver(observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isIntentSenderTargetedToPackage(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean _status = this.mRemote.transact(104, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isIntentSenderTargetedToPackage(sender);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void updatePersistentConfiguration(Configuration values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (values != null) {
                        _data.writeInt(1);
                        values.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(105, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updatePersistentConfiguration(values);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public long[] getProcessPss(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    boolean _status = this.mRemote.transact(106, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProcessPss(pids);
                    }
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void showBootMessage(CharSequence msg, boolean always) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (msg != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(msg, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!always) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(107, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().showBootMessage(msg, always);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killAllBackgroundProcesses() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(108, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killAllBackgroundProcesses();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token, String tag) throws RemoteException {
                ContentProviderHolder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    _data.writeString(tag);
                    boolean _status = this.mRemote.transact(109, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getContentProviderExternal(name, userId, token, tag);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ContentProviderHolder.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void removeContentProviderExternal(String name, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(110, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeContentProviderExternal(name, token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void removeContentProviderExternalAsUser(String name, IBinder token, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeStrongBinder(token);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(111, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeContentProviderExternalAsUser(name, token, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void getMyMemoryState(ActivityManager.RunningAppProcessInfo outInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(112, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getMyMemoryState(outInfo);
                        return;
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        outInfo.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean killProcessesBelowForeground(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(113, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().killProcessesBelowForeground(reason);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public UserInfo getCurrentUser() throws RemoteException {
                UserInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(114, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentUser();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = UserInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    boolean _status = this.mRemote.transact(115, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLaunchedFromUid(activityToken);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unstableProviderDied(IBinder connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    boolean _status = this.mRemote.transact(116, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unstableProviderDied(connection);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isIntentSenderAnActivity(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean _status = this.mRemote.transact(117, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isIntentSenderAnActivity(sender);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isIntentSenderAForegroundService(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean _status = this.mRemote.transact(118, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isIntentSenderAForegroundService(sender);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isIntentSenderABroadcast(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean _status = this.mRemote.transact(119, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isIntentSenderABroadcast(sender);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int startActivityAsUser(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                boolean _status;
                Parcel _data2 = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                try {
                    _data2.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply2;
                            _data = _data2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data2.writeStrongBinder(asBinder);
                    _data2.writeString(callingPackage);
                    if (intent != null) {
                        _data2.writeInt(1);
                        intent.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeString(resolvedType);
                    _data2.writeStrongBinder(resultTo);
                    _data2.writeString(resultWho);
                    _data2.writeInt(requestCode);
                    _data2.writeInt(flags);
                    if (profilerInfo != null) {
                        _data2.writeInt(1);
                        profilerInfo.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    if (options != null) {
                        _data2.writeInt(1);
                        options.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeInt(userId);
                    _status = this.mRemote.transact(120, _data2, _reply2, 0);
                } catch (Throwable th2) {
                    th = th2;
                    _reply = _reply2;
                    _data = _data2;
                }
                try {
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int startActivityAsUser = Stub.getDefaultImpl().startActivityAsUser(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options, userId);
                        _reply2.recycle();
                        _data2.recycle();
                        return startActivityAsUser;
                    }
                    _reply2.readException();
                    int _result = _reply2.readInt();
                    _reply2.recycle();
                    _data2.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public int stopUser(int userid, boolean force, IStopUserCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeInt(force ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(121, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopUser(userid, force, callback);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void registerUserSwitchObserver(IUserSwitchObserver observer, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(122, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerUserSwitchObserver(observer, name);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterUserSwitchObserver(IUserSwitchObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(123, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterUserSwitchObserver(observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int[] getRunningUserIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(124, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRunningUserIds();
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestSystemServerHeapDump() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(125, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestSystemServerHeapDump();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestBugReport(int bugreportType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bugreportType);
                    boolean _status = this.mRemote.transact(126, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestBugReport(bugreportType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestTelephonyBugReport(String shareTitle, String shareDescription) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(shareTitle);
                    _data.writeString(shareDescription);
                    boolean _status = this.mRemote.transact(127, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestTelephonyBugReport(shareTitle, shareDescription);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void requestWifiBugReport(String shareTitle, String shareDescription) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(shareTitle);
                    _data.writeString(shareDescription);
                    boolean _status = this.mRemote.transact(128, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestWifiBugReport(shareTitle, shareDescription);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public Intent getIntentForIntentSender(IIntentSender sender) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean _status = this.mRemote.transact(129, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIntentForIntentSender(sender);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    boolean _status = this.mRemote.transact(130, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLaunchedFromPackage(activityToken);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killUid(int appId, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(131, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killUid(appId, userId, reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setUserIsMonkey(boolean monkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(monkey ? 1 : 0);
                    boolean _status = this.mRemote.transact(132, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserIsMonkey(monkey);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void hang(IBinder who, boolean allowRestart) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(who);
                    _data.writeInt(allowRestart ? 1 : 0);
                    boolean _status = this.mRemote.transact(133, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().hang(who, allowRestart);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<ActivityManager.StackInfo> getAllStackInfos() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(134, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllStackInfos();
                    }
                    _reply.readException();
                    List<ActivityManager.StackInfo> _result = _reply.createTypedArrayList(ActivityManager.StackInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void moveTaskToStack(int taskId, int stackId, boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    _data.writeInt(toTop ? 1 : 0);
                    boolean _status = this.mRemote.transact(135, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moveTaskToStack(taskId, stackId, toTop);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void resizeStack(int stackId, Rect bounds, boolean allowResizeInDockedMode, boolean preserveWindows, boolean animate, int animationDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(stackId);
                    int i = 1;
                    if (bounds != null) {
                        _data.writeInt(1);
                        bounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(allowResizeInDockedMode ? 1 : 0);
                    _data.writeInt(preserveWindows ? 1 : 0);
                    if (!animate) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    try {
                        _data.writeInt(animationDuration);
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    boolean _status = this.mRemote.transact(136, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resizeStack(stackId, bounds, allowResizeInDockedMode, preserveWindows, animate, animationDuration);
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public void setFocusedStack(int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    boolean _status = this.mRemote.transact(137, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setFocusedStack(stackId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ActivityManager.StackInfo getFocusedStackInfo() throws RemoteException {
                ActivityManager.StackInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(138, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFocusedStackInfo();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ActivityManager.StackInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void restart() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(139, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().restart();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void performIdleMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(140, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().performIdleMaintenance();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void appNotRespondingViaProvider(IBinder connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    boolean _status = this.mRemote.transact(141, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().appNotRespondingViaProvider(connection);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public Rect getTaskBounds(int taskId) throws RemoteException {
                Rect _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(142, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTaskBounds(taskId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Rect.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean setProcessMemoryTrimLevel(String process, int uid, int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(process);
                    _data.writeInt(uid);
                    _data.writeInt(level);
                    boolean _status = this.mRemote.transact(143, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setProcessMemoryTrimLevel(process, uid, level);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public String getTagForIntentSender(IIntentSender sender, String prefix) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    _data.writeString(prefix);
                    boolean _status = this.mRemote.transact(144, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTagForIntentSender(sender, prefix);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean startUserInBackground(int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    boolean _status = this.mRemote.transact(145, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startUserInBackground(userid);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isInLockTaskMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(146, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isInLockTaskMode();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void startRecentsActivity(Intent intent, IAssistDataReceiver assistDataReceiver, IRecentsAnimationRunner recentsAnimationRunner) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(assistDataReceiver != null ? assistDataReceiver.asBinder() : null);
                    _data.writeStrongBinder(recentsAnimationRunner != null ? recentsAnimationRunner.asBinder() : null);
                    boolean _status = this.mRemote.transact(147, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startRecentsActivity(intent, assistDataReceiver, recentsAnimationRunner);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void cancelRecentsAnimation(boolean restoreHomeStackPosition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(restoreHomeStackPosition ? 1 : 0);
                    boolean _status = this.mRemote.transact(148, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelRecentsAnimation(restoreHomeStackPosition);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(149, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startActivityFromRecents(taskId, options);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void startSystemLockTaskMode(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(150, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startSystemLockTaskMode(taskId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isTopOfTask(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(151, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isTopOfTask(token);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void bootAnimationComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(152, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().bootAnimationComplete();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int checkPermissionWithToken(String permission, int pid, int uid, IBinder callerToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(callerToken);
                    boolean _status = this.mRemote.transact(153, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkPermissionWithToken(permission, pid, uid, callerToken);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(154, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerTaskStackListener(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(155, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterTaskStackListener(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void notifyCleartextNetwork(int uid, byte[] firstPacket) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeByteArray(firstPacket);
                    boolean _status = this.mRemote.transact(156, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCleartextNetwork(uid, firstPacket);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(resizeableMode);
                    boolean _status = this.mRemote.transact(157, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTaskResizeable(taskId, resizeableMode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (bounds != null) {
                        _data.writeInt(1);
                        bounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(resizeMode);
                    boolean _status = this.mRemote.transact(158, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resizeTask(taskId, bounds, resizeMode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getLockTaskModeState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(159, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLockTaskModeState();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setDumpHeapDebugLimit(String processName, int uid, long maxMemSize, String reportPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    _data.writeLong(maxMemSize);
                    _data.writeString(reportPackage);
                    boolean _status = this.mRemote.transact(160, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDumpHeapDebugLimit(processName, uid, maxMemSize, reportPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void dumpHeapFinished(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    boolean _status = this.mRemote.transact(161, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dumpHeapFinished(path);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packages);
                    boolean _status = this.mRemote.transact(162, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateLockTaskPackages(userId, packages);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void noteAlarmStart(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sourceUid);
                    _data.writeString(tag);
                    boolean _status = this.mRemote.transact(163, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteAlarmStart(sender, workSource, sourceUid, tag);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void noteAlarmFinish(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sourceUid);
                    _data.writeString(tag);
                    boolean _status = this.mRemote.transact(164, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteAlarmFinish(sender, workSource, sourceUid, tag);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getPackageProcessState(String packageName, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(165, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageProcessState(packageName, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void updateDeviceOwner(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(166, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateDeviceOwner(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean startBinderTracking() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(167, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startBinderTracking();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(168, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopBinderTrackingAndDump(fd);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void positionTaskInStack(int taskId, int stackId, int position) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    _data.writeInt(position);
                    boolean _status = this.mRemote.transact(169, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().positionTaskInStack(taskId, stackId, position);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(suppress ? 1 : 0);
                    boolean _status = this.mRemote.transact(170, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().suppressResizeConfigChanges(suppress);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean moveTopActivityToPinnedStack(int stackId, Rect bounds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    if (bounds != null) {
                        _data.writeInt(1);
                        bounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(171, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().moveTopActivityToPinnedStack(stackId, bounds);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isAppStartModeDisabled(int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(172, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAppStartModeDisabled(uid, packageName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean unlockUser(int userid, byte[] token, byte[] secret, IProgressListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeByteArray(token);
                    _data.writeByteArray(secret);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(173, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unlockUser(userid, token, secret, listener);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void killPackageDependents(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(174, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().killPackageDependents(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void resizeDockedStack(Rect dockedBounds, Rect tempDockedTaskBounds, Rect tempDockedTaskInsetBounds, Rect tempOtherTaskBounds, Rect tempOtherTaskInsetBounds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (dockedBounds != null) {
                        _data.writeInt(1);
                        dockedBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (tempDockedTaskBounds != null) {
                        _data.writeInt(1);
                        tempDockedTaskBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (tempDockedTaskInsetBounds != null) {
                        _data.writeInt(1);
                        tempDockedTaskInsetBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (tempOtherTaskBounds != null) {
                        _data.writeInt(1);
                        tempOtherTaskBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (tempOtherTaskInsetBounds != null) {
                        _data.writeInt(1);
                        tempOtherTaskInsetBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(175, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resizeDockedStack(dockedBounds, tempDockedTaskBounds, tempDockedTaskInsetBounds, tempOtherTaskBounds, tempOtherTaskInsetBounds);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void removeStack(int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    boolean _status = this.mRemote.transact(176, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeStack(stackId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void makePackageIdle(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(177, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().makePackageIdle(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int getMemoryTrimLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(178, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMemoryTrimLevel();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isVrModePackageEnabled(ComponentName packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (packageName != null) {
                        _data.writeInt(1);
                        packageName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(179, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isVrModePackageEnabled(packageName);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void notifyLockedProfile(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(180, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyLockedProfile(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void startConfirmDeviceCredentialIntent(Intent intent, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(181, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startConfirmDeviceCredentialIntent(intent, options);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void sendIdleJobTrigger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(182, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendIdleJobTrigger();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int sendIntentSender(IIntentSender target, IBinder whitelistToken, int code, Intent intent, String resolvedType, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(whitelistToken);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(code);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeStrongBinder(finishedReceiver != null ? finishedReceiver.asBinder() : null);
                    _data.writeString(requiredPermission);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(183, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int sendIntentSender = Stub.getDefaultImpl().sendIntentSender(target, whitelistToken, code, intent, resolvedType, finishedReceiver, requiredPermission, options);
                        _reply.recycle();
                        _data.recycle();
                        return sendIntentSender;
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public boolean isBackgroundRestricted(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(184, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBackgroundRestricted(packageName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setRenderThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    boolean _status = this.mRemote.transact(185, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRenderThread(tid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setHasTopUi(boolean hasTopUi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hasTopUi ? 1 : 0);
                    boolean _status = this.mRemote.transact(186, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setHasTopUi(hasTopUi);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public int restartUserInBackground(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(187, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().restartUserInBackground(userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void cancelTaskWindowTransition(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(188, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelTaskWindowTransition(taskId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ActivityManager.TaskSnapshot getTaskSnapshot(int taskId, boolean reducedResolution) throws RemoteException {
                ActivityManager.TaskSnapshot _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(reducedResolution ? 1 : 0);
                    boolean _status = this.mRemote.transact(189, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTaskSnapshot(taskId, reducedResolution);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ActivityManager.TaskSnapshot.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void scheduleApplicationInfoChanged(List<String> packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(190, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleApplicationInfoChanged(packageNames, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setPersistentVrThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    boolean _status = this.mRemote.transact(191, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPersistentVrThread(tid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void waitForNetworkStateUpdate(long procStateSeq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(procStateSeq);
                    boolean _status = this.mRemote.transact(192, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().waitForNetworkStateUpdate(procStateSeq);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void backgroundWhitelistUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(193, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().backgroundWhitelistUid(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean startUserInBackgroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeStrongBinder(unlockProgressListener != null ? unlockProgressListener.asBinder() : null);
                    boolean _status = this.mRemote.transact(194, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startUserInBackgroundWithListener(userid, unlockProgressListener);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void startDelegateShellPermissionIdentity(int uid, String[] permissions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeStringArray(permissions);
                    boolean _status = this.mRemote.transact(195, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startDelegateShellPermissionIdentity(uid, permissions);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void stopDelegateShellPermissionIdentity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(196, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopDelegateShellPermissionIdentity();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public ParcelFileDescriptor getLifeMonitor() throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(197, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLifeMonitor();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean startUserInForegroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeStrongBinder(unlockProgressListener != null ? unlockProgressListener.asBinder() : null);
                    boolean _status = this.mRemote.transact(198, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startUserInForegroundWithListener(userid, unlockProgressListener);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void finishMiniProgram() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(199, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishMiniProgram();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setFocusedAppNoChecked(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(200, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setFocusedAppNoChecked(taskId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void handleActivityChanged(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(201, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleActivityChanged(bundle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public boolean isTopActivityFullscreen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(202, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isTopActivityFullscreen();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void forceGrantFolderPermission(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    boolean _status = this.mRemote.transact(203, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceGrantFolderPermission(path);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public String getOption(String key, String defaultValue) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(defaultValue);
                    boolean _status = this.mRemote.transact(204, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOption(key, defaultValue);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public double[] getUsageInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(205, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUsageInfo();
                    }
                    _reply.readException();
                    double[] _result = _reply.createDoubleArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<xpDialogInfo> getDialogRecorder(boolean topOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(topOnly ? 1 : 0);
                    boolean _status = this.mRemote.transact(206, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDialogRecorder(topOnly);
                    }
                    _reply.readException();
                    List<xpDialogInfo> _result = _reply.createTypedArrayList(xpDialogInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setDialogRecorder(xpDialogInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(207, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDialogRecorder(info);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void dismissDialog(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(208, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dismissDialog(type);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void setHomeState(ComponentName component, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(209, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setHomeState(component, state);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public List<String> getSpeechObserver() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(210, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSpeechObserver();
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public void notifyInstallPersistentPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(211, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyInstallPersistentPackage(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IActivityManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IActivityManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
