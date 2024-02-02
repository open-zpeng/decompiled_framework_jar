package android.app;

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
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ParceledListSlice;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
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
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.WorkSource;
import android.service.voice.IVoiceInteractionSession;
import android.text.TextUtils;
import android.view.IRecentsAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationDefinition;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.os.IResultReceiver;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.xiaopeng.app.xpDialogInfo;
import java.util.List;
/* loaded from: classes.dex */
public interface IActivityManager extends IInterface {
    synchronized void activityDestroyed(IBinder iBinder) throws RemoteException;

    synchronized void activityIdle(IBinder iBinder, Configuration configuration, boolean z) throws RemoteException;

    synchronized void activityPaused(IBinder iBinder) throws RemoteException;

    synchronized void activityRelaunched(IBinder iBinder) throws RemoteException;

    synchronized void activityResumed(IBinder iBinder) throws RemoteException;

    synchronized void activitySlept(IBinder iBinder) throws RemoteException;

    synchronized void activityStopped(IBinder iBinder, Bundle bundle, PersistableBundle persistableBundle, CharSequence charSequence) throws RemoteException;

    synchronized int addAppTask(IBinder iBinder, Intent intent, ActivityManager.TaskDescription taskDescription, Bitmap bitmap) throws RemoteException;

    synchronized void addInstrumentationResults(IApplicationThread iApplicationThread, Bundle bundle) throws RemoteException;

    synchronized void addPackageDependency(String str) throws RemoteException;

    synchronized void alwaysShowUnsupportedCompileSdkWarning(ComponentName componentName) throws RemoteException;

    synchronized void appNotRespondingViaProvider(IBinder iBinder) throws RemoteException;

    synchronized void attachApplication(IApplicationThread iApplicationThread, long j) throws RemoteException;

    synchronized void backgroundWhitelistUid(int i) throws RemoteException;

    synchronized void backupAgentCreated(String str, IBinder iBinder) throws RemoteException;

    synchronized boolean bindBackupAgent(String str, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int bindService(IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String str, IServiceConnection iServiceConnection, int i, String str2, int i2) throws RemoteException;

    synchronized void bootAnimationComplete() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int broadcastIntent(IApplicationThread iApplicationThread, Intent intent, String str, IIntentReceiver iIntentReceiver, int i, String str2, Bundle bundle, String[] strArr, int i2, Bundle bundle2, boolean z, boolean z2, int i3) throws RemoteException;

    synchronized void cancelIntentSender(IIntentSender iIntentSender) throws RemoteException;

    private protected void cancelRecentsAnimation(boolean z) throws RemoteException;

    private protected void cancelTaskWindowTransition(int i) throws RemoteException;

    synchronized int checkGrantUriPermission(int i, String str, Uri uri, int i2, int i3) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int checkPermission(String str, int i, int i2) throws RemoteException;

    synchronized int checkPermissionWithToken(String str, int i, int i2, IBinder iBinder) throws RemoteException;

    synchronized int checkUriPermission(Uri uri, int i, int i2, int i3, int i4, IBinder iBinder) throws RemoteException;

    synchronized boolean clearApplicationUserData(String str, boolean z, IPackageDataObserver iPackageDataObserver, int i) throws RemoteException;

    synchronized void clearGrantedUriPermissions(String str, int i) throws RemoteException;

    synchronized void clearPendingBackup() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void closeSystemDialogs(String str) throws RemoteException;

    synchronized boolean convertFromTranslucent(IBinder iBinder) throws RemoteException;

    synchronized boolean convertToTranslucent(IBinder iBinder, Bundle bundle) throws RemoteException;

    synchronized void crashApplication(int i, int i2, String str, int i3, String str2) throws RemoteException;

    synchronized int createStackOnDisplay(int i) throws RemoteException;

    void dismissDialog(int i) throws RemoteException;

    synchronized void dismissKeyguard(IBinder iBinder, IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) throws RemoteException;

    synchronized void dismissPip(boolean z, int i) throws RemoteException;

    synchronized void dismissSplitScreenMode(boolean z) throws RemoteException;

    synchronized boolean dumpHeap(String str, int i, boolean z, boolean z2, boolean z3, String str2, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    synchronized void dumpHeapFinished(String str) throws RemoteException;

    synchronized boolean enterPictureInPictureMode(IBinder iBinder, PictureInPictureParams pictureInPictureParams) throws RemoteException;

    private protected void enterSafeMode() throws RemoteException;

    synchronized void exitFreeformMode(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean finishActivity(IBinder iBinder, int i, Intent intent, int i2) throws RemoteException;

    synchronized boolean finishActivityAffinity(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void finishHeavyWeightApp() throws RemoteException;

    synchronized void finishInstrumentation(IApplicationThread iApplicationThread, int i, Bundle bundle) throws RemoteException;

    void finishMiniProgram() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void finishReceiver(IBinder iBinder, int i, String str, Bundle bundle, boolean z, int i2) throws RemoteException;

    synchronized void finishSubActivity(IBinder iBinder, String str, int i) throws RemoteException;

    synchronized void finishVoiceTask(IVoiceInteractionSession iVoiceInteractionSession) throws RemoteException;

    void forceGrantFolderPermission(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void forceStopPackage(String str, int i) throws RemoteException;

    synchronized ComponentName getActivityClassForToken(IBinder iBinder) throws RemoteException;

    synchronized int getActivityDisplayId(IBinder iBinder) throws RemoteException;

    synchronized Bundle getActivityOptions(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    List<ActivityManager.StackInfo> getAllStackInfos() throws RemoteException;

    synchronized Point getAppTaskThumbnailSize() throws RemoteException;

    synchronized List<IBinder> getAppTasks(String str) throws RemoteException;

    synchronized Bundle getAssistContextExtras(int i) throws RemoteException;

    synchronized ComponentName getCallingActivity(IBinder iBinder) throws RemoteException;

    synchronized String getCallingPackage(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    Configuration getConfiguration() throws RemoteException;

    synchronized ContentProviderHolder getContentProvider(IApplicationThread iApplicationThread, String str, int i, boolean z) throws RemoteException;

    synchronized ContentProviderHolder getContentProviderExternal(String str, int i, IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    UserInfo getCurrentUser() throws RemoteException;

    synchronized ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException;

    List<xpDialogInfo> getDialogRecorder(boolean z) throws RemoteException;

    private protected List<ActivityManager.RunningTaskInfo> getFilteredTasks(int i, int i2, int i3) throws RemoteException;

    synchronized ActivityManager.StackInfo getFocusedStackInfo() throws RemoteException;

    synchronized int getFrontActivityScreenCompatMode() throws RemoteException;

    synchronized ParceledListSlice getGrantedUriPermissions(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    Intent getIntentForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    IIntentSender getIntentSender(int i, String str, IBinder iBinder, String str2, int i2, Intent[] intentArr, String[] strArr, int i3, Bundle bundle, int i4) throws RemoteException;

    synchronized int getLastResumedActivityUserId() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String getLaunchedFromPackage(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getLaunchedFromUid(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getLockTaskModeState() throws RemoteException;

    synchronized int getMaxNumPictureInPictureActions(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void getMemoryInfo(ActivityManager.MemoryInfo memoryInfo) throws RemoteException;

    synchronized int getMemoryTrimLevel() throws RemoteException;

    synchronized void getMyMemoryState(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) throws RemoteException;

    String getOption(String str, String str2) throws RemoteException;

    synchronized boolean getPackageAskScreenCompat(String str) throws RemoteException;

    synchronized String getPackageForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    synchronized String getPackageForToken(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getPackageProcessState(String str, String str2) throws RemoteException;

    synchronized int getPackageScreenCompatMode(String str) throws RemoteException;

    synchronized ParceledListSlice getPersistedUriPermissions(String str, boolean z) throws RemoteException;

    private protected int getProcessLimit() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    Debug.MemoryInfo[] getProcessMemoryInfo(int[] iArr) throws RemoteException;

    private protected long[] getProcessPss(int[] iArr) throws RemoteException;

    synchronized List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String getProviderMimeType(Uri uri, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ParceledListSlice getRecentTasks(int i, int i2, int i3) throws RemoteException;

    synchronized int getRequestedOrientation(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException;

    synchronized List<ApplicationInfo> getRunningExternalApplications() throws RemoteException;

    synchronized PendingIntent getRunningServiceControlPanel(ComponentName componentName) throws RemoteException;

    synchronized int[] getRunningUserIds() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    List<ActivityManager.RunningServiceInfo> getServices(int i, int i2) throws RemoteException;

    List<String> getSpeechObserver() throws RemoteException;

    synchronized ActivityManager.StackInfo getStackInfo(int i, int i2) throws RemoteException;

    synchronized String getTagForIntentSender(IIntentSender iIntentSender, String str) throws RemoteException;

    private protected Rect getTaskBounds(int i) throws RemoteException;

    synchronized ActivityManager.TaskDescription getTaskDescription(int i) throws RemoteException;

    synchronized Bitmap getTaskDescriptionIcon(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getTaskForActivity(IBinder iBinder, boolean z) throws RemoteException;

    private protected ActivityManager.TaskSnapshot getTaskSnapshot(int i, boolean z) throws RemoteException;

    synchronized List<ActivityManager.RunningTaskInfo> getTasks(int i) throws RemoteException;

    synchronized int getUidForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    synchronized int getUidProcessState(int i, String str) throws RemoteException;

    synchronized IBinder getUriPermissionOwnerForActivity(IBinder iBinder) throws RemoteException;

    double[] getUsageInfo() throws RemoteException;

    synchronized void grantUriPermission(IApplicationThread iApplicationThread, String str, Uri uri, int i, int i2) throws RemoteException;

    synchronized void grantUriPermissionFromOwner(IBinder iBinder, int i, String str, Uri uri, int i2, int i3, int i4) throws RemoteException;

    void handleActivityChanged(Bundle bundle) throws RemoteException;

    synchronized void handleApplicationCrash(IBinder iBinder, ApplicationErrorReport.ParcelableCrashInfo parcelableCrashInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void handleApplicationStrictModeViolation(IBinder iBinder, int i, StrictMode.ViolationInfo violationInfo) throws RemoteException;

    synchronized boolean handleApplicationWtf(IBinder iBinder, String str, boolean z, ApplicationErrorReport.ParcelableCrashInfo parcelableCrashInfo) throws RemoteException;

    synchronized int handleIncomingUser(int i, int i2, int i3, boolean z, boolean z2, String str, String str2) throws RemoteException;

    private protected void hang(IBinder iBinder, boolean z) throws RemoteException;

    synchronized long inputDispatchingTimedOut(int i, boolean z, String str) throws RemoteException;

    synchronized boolean isAppForeground(int i) throws RemoteException;

    synchronized boolean isAppStartModeDisabled(int i, String str) throws RemoteException;

    synchronized boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException;

    synchronized boolean isBackgroundRestricted(String str) throws RemoteException;

    synchronized boolean isImmersive(IBinder iBinder) throws RemoteException;

    private protected boolean isInLockTaskMode() throws RemoteException;

    synchronized boolean isInMultiWindowMode(IBinder iBinder) throws RemoteException;

    synchronized boolean isInPictureInPictureMode(IBinder iBinder) throws RemoteException;

    synchronized boolean isIntentSenderAForegroundService(IIntentSender iIntentSender) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isIntentSenderAnActivity(IIntentSender iIntentSender) throws RemoteException;

    synchronized boolean isIntentSenderTargetedToPackage(IIntentSender iIntentSender) throws RemoteException;

    synchronized boolean isRootVoiceInteraction(IBinder iBinder) throws RemoteException;

    boolean isTopActivityFullscreen() throws RemoteException;

    synchronized boolean isTopActivityImmersive() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isTopOfTask(IBinder iBinder) throws RemoteException;

    synchronized boolean isUidActive(int i, String str) throws RemoteException;

    synchronized boolean isUserAMonkey() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isUserRunning(int i, int i2) throws RemoteException;

    synchronized boolean isVrModePackageEnabled(ComponentName componentName) throws RemoteException;

    synchronized void keyguardGoingAway(int i) throws RemoteException;

    private protected void killAllBackgroundProcesses() throws RemoteException;

    synchronized void killApplication(String str, int i, int i2, String str2) throws RemoteException;

    private protected void killApplicationProcess(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void killBackgroundProcesses(String str, int i) throws RemoteException;

    synchronized void killPackageDependents(String str, int i) throws RemoteException;

    synchronized boolean killPids(int[] iArr, String str, boolean z) throws RemoteException;

    synchronized boolean killProcessesBelowForeground(String str) throws RemoteException;

    synchronized void killUid(int i, int i2, String str) throws RemoteException;

    synchronized boolean launchAssistIntent(Intent intent, int i, String str, int i2, Bundle bundle) throws RemoteException;

    synchronized void makePackageIdle(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean moveActivityTaskToBack(IBinder iBinder, boolean z) throws RemoteException;

    synchronized void moveStackToDisplay(int i, int i2) throws RemoteException;

    synchronized void moveTaskBackwards(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void moveTaskToFront(int i, int i2, Bundle bundle) throws RemoteException;

    private protected void moveTaskToStack(int i, int i2, boolean z) throws RemoteException;

    synchronized void moveTasksToFullscreenStack(int i, boolean z) throws RemoteException;

    private protected boolean moveTopActivityToPinnedStack(int i, Rect rect) throws RemoteException;

    synchronized boolean navigateUpTo(IBinder iBinder, Intent intent, int i, Intent intent2) throws RemoteException;

    synchronized IBinder newUriPermissionOwner(String str) throws RemoteException;

    synchronized void noteAlarmFinish(IIntentSender iIntentSender, WorkSource workSource, int i, String str) throws RemoteException;

    synchronized void noteAlarmStart(IIntentSender iIntentSender, WorkSource workSource, int i, String str) throws RemoteException;

    synchronized void noteWakeupAlarm(IIntentSender iIntentSender, WorkSource workSource, int i, String str, String str2) throws RemoteException;

    synchronized void notifyActivityDrawn(IBinder iBinder) throws RemoteException;

    synchronized void notifyCleartextNetwork(int i, byte[] bArr) throws RemoteException;

    synchronized void notifyEnterAnimationComplete(IBinder iBinder) throws RemoteException;

    synchronized void notifyLaunchTaskBehindComplete(IBinder iBinder) throws RemoteException;

    synchronized void notifyLockedProfile(int i) throws RemoteException;

    synchronized void notifyPinnedStackAnimationEnded() throws RemoteException;

    synchronized void notifyPinnedStackAnimationStarted() throws RemoteException;

    synchronized ParcelFileDescriptor openContentUri(String str) throws RemoteException;

    synchronized void overridePendingTransition(IBinder iBinder, String str, int i, int i2) throws RemoteException;

    synchronized IBinder peekService(Intent intent, String str, String str2) throws RemoteException;

    synchronized void performIdleMaintenance() throws RemoteException;

    private protected void positionTaskInStack(int i, int i2, int i3) throws RemoteException;

    private protected boolean profileControl(String str, int i, boolean z, ProfilerInfo profilerInfo, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void publishContentProviders(IApplicationThread iApplicationThread, List<ContentProviderHolder> list) throws RemoteException;

    synchronized void publishService(IBinder iBinder, Intent intent, IBinder iBinder2) throws RemoteException;

    synchronized boolean refContentProvider(IBinder iBinder, int i, int i2) throws RemoteException;

    synchronized void registerIntentSenderCancelListener(IIntentSender iIntentSender, IResultReceiver iResultReceiver) throws RemoteException;

    private protected void registerProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    Intent registerReceiver(IApplicationThread iApplicationThread, String str, IIntentReceiver iIntentReceiver, IntentFilter intentFilter, String str2, int i, int i2) throws RemoteException;

    synchronized void registerRemoteAnimationForNextActivityStart(String str, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException;

    synchronized void registerRemoteAnimations(IBinder iBinder, RemoteAnimationDefinition remoteAnimationDefinition) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void registerTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    synchronized void registerUidObserver(IUidObserver iUidObserver, int i, int i2, String str) throws RemoteException;

    private protected void registerUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver, String str) throws RemoteException;

    synchronized boolean releaseActivityInstance(IBinder iBinder) throws RemoteException;

    synchronized void releasePersistableUriPermission(Uri uri, int i, String str, int i2) throws RemoteException;

    synchronized void releaseSomeActivities(IApplicationThread iApplicationThread) throws RemoteException;

    synchronized void removeContentProvider(IBinder iBinder, boolean z) throws RemoteException;

    private protected void removeContentProviderExternal(String str, IBinder iBinder) throws RemoteException;

    private protected void removeStack(int i) throws RemoteException;

    synchronized void removeStacksInWindowingModes(int[] iArr) throws RemoteException;

    synchronized void removeStacksWithActivityTypes(int[] iArr) throws RemoteException;

    private protected boolean removeTask(int i) throws RemoteException;

    synchronized void reportActivityFullyDrawn(IBinder iBinder, boolean z) throws RemoteException;

    synchronized void reportAssistContextExtras(IBinder iBinder, Bundle bundle, AssistStructure assistStructure, AssistContent assistContent, Uri uri) throws RemoteException;

    synchronized void reportSizeConfigurations(IBinder iBinder, int[] iArr, int[] iArr2, int[] iArr3) throws RemoteException;

    synchronized boolean requestAssistContextExtras(int i, IAssistDataReceiver iAssistDataReceiver, Bundle bundle, IBinder iBinder, boolean z, boolean z2) throws RemoteException;

    synchronized boolean requestAutofillData(IAssistDataReceiver iAssistDataReceiver, Bundle bundle, IBinder iBinder, int i) throws RemoteException;

    private protected void requestBugReport(int i) throws RemoteException;

    synchronized void requestTelephonyBugReport(String str, String str2) throws RemoteException;

    synchronized void requestWifiBugReport(String str, String str2) throws RemoteException;

    private protected void resizeDockedStack(Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5) throws RemoteException;

    synchronized void resizePinnedStack(Rect rect, Rect rect2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void resizeStack(int i, Rect rect, boolean z, boolean z2, boolean z3, int i2) throws RemoteException;

    private protected void resizeTask(int i, Rect rect, int i2) throws RemoteException;

    private protected void restart() throws RemoteException;

    synchronized int restartUserInBackground(int i) throws RemoteException;

    private protected void resumeAppSwitches() throws RemoteException;

    synchronized void revokeUriPermission(IApplicationThread iApplicationThread, String str, Uri uri, int i, int i2) throws RemoteException;

    synchronized void revokeUriPermissionFromOwner(IBinder iBinder, Uri uri, int i, int i2) throws RemoteException;

    synchronized void scheduleApplicationInfoChanged(List<String> list, int i) throws RemoteException;

    private protected void sendIdleJobTrigger() throws RemoteException;

    synchronized int sendIntentSender(IIntentSender iIntentSender, IBinder iBinder, int i, Intent intent, String str, IIntentReceiver iIntentReceiver, String str2, Bundle bundle) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void serviceDoneExecuting(IBinder iBinder, int i, int i2, int i3) throws RemoteException;

    private protected void setActivityController(IActivityController iActivityController, boolean z) throws RemoteException;

    synchronized void setAgentApp(String str, String str2) throws RemoteException;

    private protected void setAlwaysFinish(boolean z) throws RemoteException;

    private protected void setDebugApp(String str, boolean z, boolean z2) throws RemoteException;

    void setDialogRecorder(xpDialogInfo xpdialoginfo) throws RemoteException;

    synchronized void setDisablePreviewScreenshots(IBinder iBinder, boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setDumpHeapDebugLimit(String str, int i, long j, String str2) throws RemoteException;

    void setFocusedAppNoChecked(int i) throws RemoteException;

    synchronized void setFocusedStack(int i) throws RemoteException;

    synchronized void setFocusedTask(int i) throws RemoteException;

    synchronized void setFrontActivityScreenCompatMode(int i) throws RemoteException;

    synchronized void setHasTopUi(boolean z) throws RemoteException;

    void setHomeState(ComponentName componentName, int i) throws RemoteException;

    synchronized void setImmersive(IBinder iBinder, boolean z) throws RemoteException;

    synchronized void setLockScreenShown(boolean z, boolean z2, int i) throws RemoteException;

    synchronized void setPackageAskScreenCompat(String str, boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setPackageScreenCompatMode(String str, int i) throws RemoteException;

    synchronized void setPersistentVrThread(int i) throws RemoteException;

    synchronized void setPictureInPictureParams(IBinder iBinder, PictureInPictureParams pictureInPictureParams) throws RemoteException;

    private protected void setProcessImportant(IBinder iBinder, int i, boolean z, String str) throws RemoteException;

    private protected void setProcessLimit(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean setProcessMemoryTrimLevel(String str, int i, int i2) throws RemoteException;

    synchronized void setRenderThread(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setRequestedOrientation(IBinder iBinder, int i) throws RemoteException;

    synchronized void setServiceForeground(ComponentName componentName, IBinder iBinder, int i, Notification notification, int i2) throws RemoteException;

    synchronized void setShowWhenLocked(IBinder iBinder, boolean z) throws RemoteException;

    synchronized void setSplitScreenResizing(boolean z) throws RemoteException;

    synchronized void setTaskDescription(IBinder iBinder, ActivityManager.TaskDescription taskDescription) throws RemoteException;

    private protected void setTaskResizeable(int i, int i2) throws RemoteException;

    synchronized void setTaskWindowingMode(int i, int i2, boolean z) throws RemoteException;

    synchronized boolean setTaskWindowingModeSplitScreenPrimary(int i, int i2, boolean z, boolean z2, Rect rect, boolean z3) throws RemoteException;

    synchronized void setTurnScreenOn(IBinder iBinder, boolean z) throws RemoteException;

    synchronized void setUserIsMonkey(boolean z) throws RemoteException;

    synchronized void setVoiceKeepAwake(IVoiceInteractionSession iVoiceInteractionSession, boolean z) throws RemoteException;

    synchronized int setVrMode(IBinder iBinder, boolean z, ComponentName componentName) throws RemoteException;

    synchronized void setVrThread(int i) throws RemoteException;

    synchronized boolean shouldUpRecreateTask(IBinder iBinder, String str) throws RemoteException;

    synchronized boolean showAssistFromActivity(IBinder iBinder, Bundle bundle) throws RemoteException;

    synchronized void showBootMessage(CharSequence charSequence, boolean z) throws RemoteException;

    synchronized void showLockTaskEscapeMessage(IBinder iBinder) throws RemoteException;

    synchronized void showWaitingForDebugger(IApplicationThread iApplicationThread, boolean z) throws RemoteException;

    private protected boolean shutdown(int i) throws RemoteException;

    synchronized void signalPersistentProcesses(int i) throws RemoteException;

    synchronized int startActivities(IApplicationThread iApplicationThread, String str, Intent[] intentArr, String[] strArr, IBinder iBinder, Bundle bundle, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int startActivity(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException;

    synchronized WaitResult startActivityAndWait(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    synchronized int startActivityAsCaller(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, boolean z, int i3) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int startActivityAsUser(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    private protected int startActivityFromRecents(int i, Bundle bundle) throws RemoteException;

    synchronized int startActivityIntentSender(IApplicationThread iApplicationThread, IIntentSender iIntentSender, IBinder iBinder, Intent intent, String str, IBinder iBinder2, String str2, int i, int i2, int i3, Bundle bundle) throws RemoteException;

    synchronized int startActivityWithConfig(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, Configuration configuration, Bundle bundle, int i3) throws RemoteException;

    synchronized int startAssistantActivity(String str, int i, int i2, Intent intent, String str2, Bundle bundle, int i3) throws RemoteException;

    private protected boolean startBinderTracking() throws RemoteException;

    synchronized void startConfirmDeviceCredentialIntent(Intent intent, Bundle bundle) throws RemoteException;

    synchronized void startInPlaceAnimationOnFrontMostApplication(Bundle bundle) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean startInstrumentation(ComponentName componentName, String str, int i, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, int i2, String str2) throws RemoteException;

    synchronized void startLocalVoiceInteraction(IBinder iBinder, Bundle bundle) throws RemoteException;

    synchronized void startLockTaskModeByToken(IBinder iBinder) throws RemoteException;

    synchronized boolean startNextMatchingActivity(IBinder iBinder, Intent intent, Bundle bundle) throws RemoteException;

    private protected void startRecentsActivity(Intent intent, IAssistDataReceiver iAssistDataReceiver, IRecentsAnimationRunner iRecentsAnimationRunner) throws RemoteException;

    synchronized ComponentName startService(IApplicationThread iApplicationThread, Intent intent, String str, boolean z, String str2, int i) throws RemoteException;

    private protected void startSystemLockTaskMode(int i) throws RemoteException;

    private protected boolean startUserInBackground(int i) throws RemoteException;

    synchronized boolean startUserInBackgroundWithListener(int i, IProgressListener iProgressListener) throws RemoteException;

    synchronized int startVoiceActivity(String str, int i, int i2, Intent intent, String str2, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, int i3, ProfilerInfo profilerInfo, Bundle bundle, int i4) throws RemoteException;

    int startXpApp(String str, Intent intent) throws RemoteException;

    private protected void stopAppSwitches() throws RemoteException;

    private protected boolean stopBinderTrackingAndDump(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    synchronized void stopLocalVoiceInteraction(IBinder iBinder) throws RemoteException;

    synchronized void stopLockTaskModeByToken(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int stopService(IApplicationThread iApplicationThread, Intent intent, String str, int i) throws RemoteException;

    synchronized boolean stopServiceToken(ComponentName componentName, IBinder iBinder, int i) throws RemoteException;

    synchronized void stopSystemLockTaskMode() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int stopUser(int i, boolean z, IStopUserCallback iStopUserCallback) throws RemoteException;

    synchronized boolean supportsLocalVoiceInteraction() throws RemoteException;

    private protected void suppressResizeConfigChanges(boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean switchUser(int i) throws RemoteException;

    synchronized void takePersistableUriPermission(Uri uri, int i, String str, int i2) throws RemoteException;

    private protected void unbindBackupAgent(ApplicationInfo applicationInfo) throws RemoteException;

    synchronized void unbindFinished(IBinder iBinder, Intent intent, boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean unbindService(IServiceConnection iServiceConnection) throws RemoteException;

    synchronized void unbroadcastIntent(IApplicationThread iApplicationThread, Intent intent, int i) throws RemoteException;

    private protected void unhandledBack() throws RemoteException;

    private protected boolean unlockUser(int i, byte[] bArr, byte[] bArr2, IProgressListener iProgressListener) throws RemoteException;

    synchronized void unregisterIntentSenderCancelListener(IIntentSender iIntentSender, IResultReceiver iResultReceiver) throws RemoteException;

    private protected void unregisterProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void unregisterReceiver(IIntentReceiver iIntentReceiver) throws RemoteException;

    synchronized void unregisterTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    synchronized void unregisterUidObserver(IUidObserver iUidObserver) throws RemoteException;

    synchronized void unregisterUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void unstableProviderDied(IBinder iBinder) throws RemoteException;

    private protected boolean updateConfiguration(Configuration configuration) throws RemoteException;

    synchronized void updateDeviceOwner(String str) throws RemoteException;

    synchronized boolean updateDisplayOverrideConfiguration(Configuration configuration, int i) throws RemoteException;

    synchronized void updateLockTaskFeatures(int i, int i2) throws RemoteException;

    synchronized void updateLockTaskPackages(int i, String[] strArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void updatePersistentConfiguration(Configuration configuration) throws RemoteException;

    synchronized void waitForNetworkStateUpdate(long j) throws RemoteException;

    synchronized boolean willActivityBeVisible(IBinder iBinder) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IActivityManager {
        private static final String DESCRIPTOR = "android.app.IActivityManager";
        static final int TRANSACTION_activityDestroyed = 59;
        static final int TRANSACTION_activityIdle = 16;
        static final int TRANSACTION_activityPaused = 17;
        static final int TRANSACTION_activityRelaunched = 259;
        static final int TRANSACTION_activityResumed = 36;
        static final int TRANSACTION_activitySlept = 122;
        static final int TRANSACTION_activityStopped = 18;
        static final int TRANSACTION_addAppTask = 209;
        static final int TRANSACTION_addInstrumentationResults = 41;
        static final int TRANSACTION_addPackageDependency = 94;
        static final int TRANSACTION_alwaysShowUnsupportedCompileSdkWarning = 306;
        static final int TRANSACTION_appNotRespondingViaProvider = 185;
        static final int TRANSACTION_attachApplication = 15;
        static final int TRANSACTION_backgroundWhitelistUid = 299;
        static final int TRANSACTION_backupAgentCreated = 90;
        static final int TRANSACTION_bindBackupAgent = 89;
        static final int TRANSACTION_bindService = 33;
        static final int TRANSACTION_bootAnimationComplete = 213;
        static final int TRANSACTION_broadcastIntent = 12;
        static final int TRANSACTION_cancelIntentSender = 61;
        static final int TRANSACTION_cancelRecentsAnimation = 198;
        static final int TRANSACTION_cancelTaskWindowTransition = 292;
        static final int TRANSACTION_checkGrantUriPermission = 118;
        static final int TRANSACTION_checkPermission = 50;
        static final int TRANSACTION_checkPermissionWithToken = 217;
        static final int TRANSACTION_checkUriPermission = 51;
        static final int TRANSACTION_clearApplicationUserData = 77;
        static final int TRANSACTION_clearGrantedUriPermissions = 265;
        static final int TRANSACTION_clearPendingBackup = 161;
        static final int TRANSACTION_closeSystemDialogs = 96;
        static final int TRANSACTION_convertFromTranslucent = 176;
        static final int TRANSACTION_convertToTranslucent = 177;
        static final int TRANSACTION_crashApplication = 113;
        static final int TRANSACTION_createStackOnDisplay = 221;
        static final int TRANSACTION_dismissDialog = 316;
        static final int TRANSACTION_dismissKeyguard = 290;
        static final int TRANSACTION_dismissPip = 247;
        static final int TRANSACTION_dismissSplitScreenMode = 246;
        static final int TRANSACTION_dumpHeap = 119;
        static final int TRANSACTION_dumpHeapFinished = 227;
        static final int TRANSACTION_enterPictureInPictureMode = 256;
        static final int TRANSACTION_enterSafeMode = 65;
        static final int TRANSACTION_exitFreeformMode = 243;
        static final int TRANSACTION_finishActivity = 9;
        static final int TRANSACTION_finishActivityAffinity = 147;
        static final int TRANSACTION_finishHeavyWeightApp = 108;
        static final int TRANSACTION_finishInstrumentation = 42;
        static final int TRANSACTION_finishMiniProgram = 307;
        static final int TRANSACTION_finishReceiver = 14;
        static final int TRANSACTION_finishSubActivity = 29;
        static final int TRANSACTION_finishVoiceTask = 204;
        static final int TRANSACTION_forceGrantFolderPermission = 311;
        static final int TRANSACTION_forceStopPackage = 78;
        static final int TRANSACTION_getActivityClassForToken = 46;
        static final int TRANSACTION_getActivityDisplayId = 187;
        static final int TRANSACTION_getActivityOptions = 200;
        static final int TRANSACTION_getAllStackInfos = 172;
        static final int TRANSACTION_getAppTaskThumbnailSize = 210;
        static final int TRANSACTION_getAppTasks = 201;
        static final int TRANSACTION_getAssistContextExtras = 163;
        static final int TRANSACTION_getCallingActivity = 20;
        static final int TRANSACTION_getCallingPackage = 19;
        static final int TRANSACTION_getConfiguration = 43;
        static final int TRANSACTION_getContentProvider = 26;
        static final int TRANSACTION_getContentProviderExternal = 139;
        static final int TRANSACTION_getCurrentUser = 143;
        static final int TRANSACTION_getDeviceConfigurationInfo = 83;
        static final int TRANSACTION_getDialogRecorder = 314;
        static final int TRANSACTION_getFilteredTasks = 22;
        static final int TRANSACTION_getFocusedStackInfo = 174;
        static final int TRANSACTION_getFrontActivityScreenCompatMode = 123;
        static final int TRANSACTION_getGrantedUriPermissions = 264;
        static final int TRANSACTION_getIntentForIntentSender = 162;
        static final int TRANSACTION_getIntentSender = 60;
        static final int TRANSACTION_getLastResumedActivityUserId = 298;
        static final int TRANSACTION_getLaunchedFromPackage = 165;
        static final int TRANSACTION_getLaunchedFromUid = 148;
        static final int TRANSACTION_getLockTaskModeState = 225;
        static final int TRANSACTION_getMaxNumPictureInPictureActions = 258;
        static final int TRANSACTION_getMemoryInfo = 75;
        static final int TRANSACTION_getMemoryTrimLevel = 276;
        static final int TRANSACTION_getMyMemoryState = 141;
        static final int TRANSACTION_getOption = 312;
        static final int TRANSACTION_getPackageAskScreenCompat = 127;
        static final int TRANSACTION_getPackageForIntentSender = 62;
        static final int TRANSACTION_getPackageForToken = 47;
        static final int TRANSACTION_getPackageProcessState = 232;
        static final int TRANSACTION_getPackageScreenCompatMode = 125;
        static final int TRANSACTION_getPersistedUriPermissions = 184;
        static final int TRANSACTION_getProcessLimit = 49;
        static final int TRANSACTION_getProcessMemoryInfo = 97;
        static final int TRANSACTION_getProcessPss = 136;
        static final int TRANSACTION_getProcessesInErrorState = 76;
        static final int TRANSACTION_getProviderMimeType = 114;
        static final int TRANSACTION_getRecentTasks = 57;
        static final int TRANSACTION_getRequestedOrientation = 70;
        static final int TRANSACTION_getRunningAppProcesses = 82;
        static final int TRANSACTION_getRunningExternalApplications = 107;
        static final int TRANSACTION_getRunningServiceControlPanel = 30;
        static final int TRANSACTION_getRunningUserIds = 156;
        static final int TRANSACTION_getServices = 80;
        static final int TRANSACTION_getSpeechObserver = 318;
        static final int TRANSACTION_getStackInfo = 175;
        static final int TRANSACTION_getTagForIntentSender = 189;
        static final int TRANSACTION_getTaskBounds = 186;
        static final int TRANSACTION_getTaskDescription = 81;
        static final int TRANSACTION_getTaskDescriptionIcon = 214;
        static final int TRANSACTION_getTaskForActivity = 25;
        static final int TRANSACTION_getTaskSnapshot = 293;
        static final int TRANSACTION_getTasks = 21;
        static final int TRANSACTION_getUidForIntentSender = 92;
        static final int TRANSACTION_getUidProcessState = 236;
        static final int TRANSACTION_getUriPermissionOwnerForActivity = 260;
        static final int TRANSACTION_getUsageInfo = 313;
        static final int TRANSACTION_grantUriPermission = 52;
        static final int TRANSACTION_grantUriPermissionFromOwner = 116;
        static final int TRANSACTION_handleActivityChanged = 309;
        static final int TRANSACTION_handleApplicationCrash = 5;
        static final int TRANSACTION_handleApplicationStrictModeViolation = 109;
        static final int TRANSACTION_handleApplicationWtf = 101;
        static final int TRANSACTION_handleIncomingUser = 93;
        static final int TRANSACTION_hang = 168;
        static final int TRANSACTION_inputDispatchingTimedOut = 160;
        static final int TRANSACTION_isAppForeground = 266;
        static final int TRANSACTION_isAppStartModeDisabled = 251;
        static final int TRANSACTION_isAssistDataAllowedOnCurrentActivity = 237;
        static final int TRANSACTION_isBackgroundRestricted = 283;
        static final int TRANSACTION_isImmersive = 110;
        static final int TRANSACTION_isInLockTaskMode = 193;
        static final int TRANSACTION_isInMultiWindowMode = 253;
        static final int TRANSACTION_isInPictureInPictureMode = 254;
        static final int TRANSACTION_isIntentSenderAForegroundService = 151;
        static final int TRANSACTION_isIntentSenderAnActivity = 150;
        static final int TRANSACTION_isIntentSenderTargetedToPackage = 134;
        static final int TRANSACTION_isRootVoiceInteraction = 239;
        static final int TRANSACTION_isTopActivityFullscreen = 310;
        static final int TRANSACTION_isTopActivityImmersive = 112;
        static final int TRANSACTION_isTopOfTask = 205;
        static final int TRANSACTION_isUidActive = 4;
        static final int TRANSACTION_isUserAMonkey = 103;
        static final int TRANSACTION_isUserRunning = 121;
        static final int TRANSACTION_isVrModePackageEnabled = 278;
        static final int TRANSACTION_keyguardGoingAway = 235;
        static final int TRANSACTION_killAllBackgroundProcesses = 138;
        static final int TRANSACTION_killApplication = 95;
        static final int TRANSACTION_killApplicationProcess = 98;
        static final int TRANSACTION_killBackgroundProcesses = 102;
        static final int TRANSACTION_killPackageDependents = 255;
        static final int TRANSACTION_killPids = 79;
        static final int TRANSACTION_killProcessesBelowForeground = 142;
        static final int TRANSACTION_killUid = 166;
        static final int TRANSACTION_launchAssistIntent = 215;
        static final int TRANSACTION_makePackageIdle = 275;
        static final int TRANSACTION_moveActivityTaskToBack = 74;
        static final int TRANSACTION_moveStackToDisplay = 288;
        static final int TRANSACTION_moveTaskBackwards = 24;
        static final int TRANSACTION_moveTaskToFront = 23;
        static final int TRANSACTION_moveTaskToStack = 170;
        static final int TRANSACTION_moveTasksToFullscreenStack = 249;
        static final int TRANSACTION_moveTopActivityToPinnedStack = 250;
        static final int TRANSACTION_navigateUpTo = 145;
        static final int TRANSACTION_newUriPermissionOwner = 115;
        static final int TRANSACTION_noteAlarmFinish = 231;
        static final int TRANSACTION_noteAlarmStart = 230;
        static final int TRANSACTION_noteWakeupAlarm = 67;
        static final int TRANSACTION_notifyActivityDrawn = 178;
        static final int TRANSACTION_notifyCleartextNetwork = 220;
        static final int TRANSACTION_notifyEnterAnimationComplete = 207;
        static final int TRANSACTION_notifyLaunchTaskBehindComplete = 206;
        static final int TRANSACTION_notifyLockedProfile = 279;
        static final int TRANSACTION_notifyPinnedStackAnimationEnded = 271;
        static final int TRANSACTION_notifyPinnedStackAnimationStarted = 270;
        static final int TRANSACTION_openContentUri = 1;
        static final int TRANSACTION_overridePendingTransition = 100;
        static final int TRANSACTION_peekService = 84;
        static final int TRANSACTION_performIdleMaintenance = 181;
        static final int TRANSACTION_positionTaskInStack = 242;
        static final int TRANSACTION_profileControl = 85;
        static final int TRANSACTION_publishContentProviders = 27;
        static final int TRANSACTION_publishService = 35;
        static final int TRANSACTION_refContentProvider = 28;
        static final int TRANSACTION_registerIntentSenderCancelListener = 63;
        static final int TRANSACTION_registerProcessObserver = 132;
        static final int TRANSACTION_registerReceiver = 10;
        static final int TRANSACTION_registerRemoteAnimationForNextActivityStart = 305;
        static final int TRANSACTION_registerRemoteAnimations = 304;
        static final int TRANSACTION_registerTaskStackListener = 218;
        static final int TRANSACTION_registerUidObserver = 2;
        static final int TRANSACTION_registerUserSwitchObserver = 154;
        static final int TRANSACTION_releaseActivityInstance = 211;
        static final int TRANSACTION_releasePersistableUriPermission = 183;
        static final int TRANSACTION_releaseSomeActivities = 212;
        static final int TRANSACTION_removeContentProvider = 68;
        static final int TRANSACTION_removeContentProviderExternal = 140;
        static final int TRANSACTION_removeStack = 272;
        static final int TRANSACTION_removeStacksInWindowingModes = 273;
        static final int TRANSACTION_removeStacksWithActivityTypes = 274;
        static final int TRANSACTION_removeTask = 131;
        static final int TRANSACTION_reportActivityFullyDrawn = 179;
        static final int TRANSACTION_reportAssistContextExtras = 164;
        static final int TRANSACTION_reportSizeConfigurations = 244;
        static final int TRANSACTION_requestAssistContextExtras = 223;
        static final int TRANSACTION_requestAutofillData = 289;
        static final int TRANSACTION_requestBugReport = 157;
        static final int TRANSACTION_requestTelephonyBugReport = 158;
        static final int TRANSACTION_requestWifiBugReport = 159;
        static final int TRANSACTION_resizeDockedStack = 261;
        static final int TRANSACTION_resizePinnedStack = 277;
        static final int TRANSACTION_resizeStack = 171;
        static final int TRANSACTION_resizeTask = 224;
        static final int TRANSACTION_restart = 180;
        static final int TRANSACTION_restartUserInBackground = 291;
        static final int TRANSACTION_resumeAppSwitches = 88;
        static final int TRANSACTION_revokeUriPermission = 53;
        static final int TRANSACTION_revokeUriPermissionFromOwner = 117;
        static final int TRANSACTION_scheduleApplicationInfoChanged = 294;
        static final int TRANSACTION_sendIdleJobTrigger = 281;
        static final int TRANSACTION_sendIntentSender = 282;
        static final int TRANSACTION_serviceDoneExecuting = 58;
        static final int TRANSACTION_setActivityController = 54;
        static final int TRANSACTION_setAgentApp = 38;
        static final int TRANSACTION_setAlwaysFinish = 39;
        static final int TRANSACTION_setDebugApp = 37;
        static final int TRANSACTION_setDialogRecorder = 315;
        static final int TRANSACTION_setDisablePreviewScreenshots = 297;
        static final int TRANSACTION_setDumpHeapDebugLimit = 226;
        static final int TRANSACTION_setFocusedAppNoChecked = 308;
        static final int TRANSACTION_setFocusedStack = 173;
        static final int TRANSACTION_setFocusedTask = 130;
        static final int TRANSACTION_setFrontActivityScreenCompatMode = 124;
        static final int TRANSACTION_setHasTopUi = 286;
        static final int TRANSACTION_setHomeState = 317;
        static final int TRANSACTION_setImmersive = 111;
        static final int TRANSACTION_setLockScreenShown = 146;
        static final int TRANSACTION_setPackageAskScreenCompat = 128;
        static final int TRANSACTION_setPackageScreenCompatMode = 126;
        static final int TRANSACTION_setPersistentVrThread = 295;
        static final int TRANSACTION_setPictureInPictureParams = 257;
        static final int TRANSACTION_setProcessImportant = 72;
        static final int TRANSACTION_setProcessLimit = 48;
        static final int TRANSACTION_setProcessMemoryTrimLevel = 188;
        static final int TRANSACTION_setRenderThread = 285;
        static final int TRANSACTION_setRequestedOrientation = 69;
        static final int TRANSACTION_setServiceForeground = 73;
        static final int TRANSACTION_setShowWhenLocked = 301;
        static final int TRANSACTION_setSplitScreenResizing = 262;
        static final int TRANSACTION_setTaskDescription = 194;
        static final int TRANSACTION_setTaskResizeable = 222;
        static final int TRANSACTION_setTaskWindowingMode = 169;
        static final int TRANSACTION_setTaskWindowingModeSplitScreenPrimary = 245;
        static final int TRANSACTION_setTurnScreenOn = 302;
        static final int TRANSACTION_setUserIsMonkey = 167;
        static final int TRANSACTION_setVoiceKeepAwake = 228;
        static final int TRANSACTION_setVrMode = 263;
        static final int TRANSACTION_setVrThread = 284;
        static final int TRANSACTION_shouldUpRecreateTask = 144;
        static final int TRANSACTION_showAssistFromActivity = 238;
        static final int TRANSACTION_showBootMessage = 137;
        static final int TRANSACTION_showLockTaskEscapeMessage = 233;
        static final int TRANSACTION_showWaitingForDebugger = 55;
        static final int TRANSACTION_shutdown = 86;
        static final int TRANSACTION_signalPersistentProcesses = 56;
        static final int TRANSACTION_startActivities = 120;
        static final int TRANSACTION_startActivity = 6;
        static final int TRANSACTION_startActivityAndWait = 104;
        static final int TRANSACTION_startActivityAsCaller = 208;
        static final int TRANSACTION_startActivityAsUser = 152;
        static final int TRANSACTION_startActivityFromRecents = 199;
        static final int TRANSACTION_startActivityIntentSender = 99;
        static final int TRANSACTION_startActivityWithConfig = 106;
        static final int TRANSACTION_startAssistantActivity = 196;
        static final int TRANSACTION_startBinderTracking = 240;
        static final int TRANSACTION_startConfirmDeviceCredentialIntent = 280;
        static final int TRANSACTION_startInPlaceAnimationOnFrontMostApplication = 216;
        static final int TRANSACTION_startInstrumentation = 40;
        static final int TRANSACTION_startLocalVoiceInteraction = 267;
        static final int TRANSACTION_startLockTaskModeByToken = 191;
        static final int TRANSACTION_startNextMatchingActivity = 66;
        static final int TRANSACTION_startRecentsActivity = 197;
        static final int TRANSACTION_startService = 31;
        static final int TRANSACTION_startSystemLockTaskMode = 202;
        static final int TRANSACTION_startUserInBackground = 190;
        static final int TRANSACTION_startUserInBackgroundWithListener = 303;
        static final int TRANSACTION_startVoiceActivity = 195;
        static final int TRANSACTION_startXpApp = 7;
        static final int TRANSACTION_stopAppSwitches = 87;
        static final int TRANSACTION_stopBinderTrackingAndDump = 241;
        static final int TRANSACTION_stopLocalVoiceInteraction = 268;
        static final int TRANSACTION_stopLockTaskModeByToken = 192;
        static final int TRANSACTION_stopService = 32;
        static final int TRANSACTION_stopServiceToken = 45;
        static final int TRANSACTION_stopSystemLockTaskMode = 203;
        static final int TRANSACTION_stopUser = 153;
        static final int TRANSACTION_supportsLocalVoiceInteraction = 269;
        static final int TRANSACTION_suppressResizeConfigChanges = 248;
        static final int TRANSACTION_switchUser = 129;
        static final int TRANSACTION_takePersistableUriPermission = 182;
        static final int TRANSACTION_unbindBackupAgent = 91;
        static final int TRANSACTION_unbindFinished = 71;
        static final int TRANSACTION_unbindService = 34;
        static final int TRANSACTION_unbroadcastIntent = 13;
        static final int TRANSACTION_unhandledBack = 8;
        static final int TRANSACTION_unlockUser = 252;
        static final int TRANSACTION_unregisterIntentSenderCancelListener = 64;
        static final int TRANSACTION_unregisterProcessObserver = 133;
        static final int TRANSACTION_unregisterReceiver = 11;
        static final int TRANSACTION_unregisterTaskStackListener = 219;
        static final int TRANSACTION_unregisterUidObserver = 3;
        static final int TRANSACTION_unregisterUserSwitchObserver = 155;
        static final int TRANSACTION_unstableProviderDied = 149;
        static final int TRANSACTION_updateConfiguration = 44;
        static final int TRANSACTION_updateDeviceOwner = 234;
        static final int TRANSACTION_updateDisplayOverrideConfiguration = 287;
        static final int TRANSACTION_updateLockTaskFeatures = 300;
        static final int TRANSACTION_updateLockTaskPackages = 229;
        static final int TRANSACTION_updatePersistentConfiguration = 135;
        static final int TRANSACTION_waitForNetworkStateUpdate = 296;
        static final int TRANSACTION_willActivityBeVisible = 105;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
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

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg1;
            Bundle _arg12;
            PersistableBundle _arg2;
            Intent _arg13;
            Intent _arg14;
            Rect _arg0;
            Intent _arg02;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    ParcelFileDescriptor _result = openContentUri(_arg03);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IUidObserver _arg04 = IUidObserver.Stub.asInterface(data.readStrongBinder());
                    int _arg15 = data.readInt();
                    int _arg22 = data.readInt();
                    String _arg3 = data.readString();
                    registerUidObserver(_arg04, _arg15, _arg22, _arg3);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IUidObserver _arg05 = IUidObserver.Stub.asInterface(data.readStrongBinder());
                    unregisterUidObserver(_arg05);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    boolean isUidActive = isUidActive(_arg06, data.readString());
                    reply.writeNoException();
                    reply.writeInt(isUidActive ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg07 = data.readStrongBinder();
                    handleApplicationCrash(_arg07, data.readInt() != 0 ? ApplicationErrorReport.ParcelableCrashInfo.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 6:
                    return onTransact$startActivity$(data, reply);
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    int _result2 = startXpApp(_arg08, data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    unhandledBack();
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg09 = data.readStrongBinder();
                    int _arg16 = data.readInt();
                    Intent _arg23 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    int _arg32 = data.readInt();
                    boolean finishActivity = finishActivity(_arg09, _arg16, _arg23, _arg32);
                    reply.writeNoException();
                    reply.writeInt(finishActivity ? 1 : 0);
                    return true;
                case 10:
                    return onTransact$registerReceiver$(data, reply);
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentReceiver _arg010 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                    unregisterReceiver(_arg010);
                    reply.writeNoException();
                    return true;
                case 12:
                    return onTransact$broadcastIntent$(data, reply);
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg011 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    Intent _arg17 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    int _arg24 = data.readInt();
                    unbroadcastIntent(_arg011, _arg17, _arg24);
                    reply.writeNoException();
                    return true;
                case 14:
                    return onTransact$finishReceiver$(data, reply);
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg012 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    attachApplication(_arg012, data.readLong());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg013 = data.readStrongBinder();
                    Configuration _arg18 = data.readInt() != 0 ? Configuration.CREATOR.createFromParcel(data) : null;
                    _arg1 = data.readInt() != 0;
                    activityIdle(_arg013, _arg18, _arg1);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg014 = data.readStrongBinder();
                    activityPaused(_arg014);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg015 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg12 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = PersistableBundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    CharSequence _arg33 = data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null;
                    activityStopped(_arg015, _arg12, _arg2, _arg33);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg016 = data.readStrongBinder();
                    String _result3 = getCallingPackage(_arg016);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg017 = data.readStrongBinder();
                    ComponentName _result4 = getCallingActivity(_arg017);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg018 = data.readInt();
                    List<ActivityManager.RunningTaskInfo> _result5 = getTasks(_arg018);
                    reply.writeNoException();
                    reply.writeTypedList(_result5);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg019 = data.readInt();
                    int _arg19 = data.readInt();
                    int _arg25 = data.readInt();
                    List<ActivityManager.RunningTaskInfo> _result6 = getFilteredTasks(_arg019, _arg19, _arg25);
                    reply.writeNoException();
                    reply.writeTypedList(_result6);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    int _arg110 = data.readInt();
                    Bundle _arg26 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    moveTaskToFront(_arg020, _arg110, _arg26);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg021 = data.readInt();
                    moveTaskBackwards(_arg021);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg022 = data.readStrongBinder();
                    _arg1 = data.readInt() != 0;
                    int _result7 = getTaskForActivity(_arg022, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg023 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg111 = data.readString();
                    int _arg27 = data.readInt();
                    boolean _arg34 = data.readInt() != 0;
                    ContentProviderHolder _result8 = getContentProvider(_arg023, _arg111, _arg27, _arg34);
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg024 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    publishContentProviders(_arg024, data.createTypedArrayList(ContentProviderHolder.CREATOR));
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg025 = data.readStrongBinder();
                    int _arg112 = data.readInt();
                    int _arg28 = data.readInt();
                    boolean refContentProvider = refContentProvider(_arg025, _arg112, _arg28);
                    reply.writeNoException();
                    reply.writeInt(refContentProvider ? 1 : 0);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg026 = data.readStrongBinder();
                    String _arg113 = data.readString();
                    int _arg29 = data.readInt();
                    finishSubActivity(_arg026, _arg113, _arg29);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg027 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    PendingIntent _result9 = getRunningServiceControlPanel(_arg027);
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 31:
                    return onTransact$startService$(data, reply);
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg028 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    Intent _arg114 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg210 = data.readString();
                    int _arg35 = data.readInt();
                    int _result10 = stopService(_arg028, _arg114, _arg210, _arg35);
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 33:
                    return onTransact$bindService$(data, reply);
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    IServiceConnection _arg029 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
                    boolean unbindService = unbindService(_arg029);
                    reply.writeNoException();
                    reply.writeInt(unbindService ? 1 : 0);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg030 = data.readStrongBinder();
                    Intent _arg115 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    IBinder _arg211 = data.readStrongBinder();
                    publishService(_arg030, _arg115, _arg211);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg031 = data.readStrongBinder();
                    activityResumed(_arg031);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    boolean _arg116 = data.readInt() != 0;
                    _arg1 = data.readInt() != 0;
                    setDebugApp(_arg032, _arg116, _arg1);
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg033 = data.readString();
                    setAgentApp(_arg033, data.readString());
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg034 = _arg1;
                    setAlwaysFinish(_arg034);
                    reply.writeNoException();
                    return true;
                case 40:
                    return onTransact$startInstrumentation$(data, reply);
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg035 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    addInstrumentationResults(_arg035, data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg036 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    int _arg117 = data.readInt();
                    Bundle _arg212 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    finishInstrumentation(_arg036, _arg117, _arg212);
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    Configuration _result11 = getConfiguration();
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    Configuration _arg037 = data.readInt() != 0 ? Configuration.CREATOR.createFromParcel(data) : null;
                    boolean updateConfiguration = updateConfiguration(_arg037);
                    reply.writeNoException();
                    reply.writeInt(updateConfiguration ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg038 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    IBinder _arg118 = data.readStrongBinder();
                    int _arg213 = data.readInt();
                    boolean stopServiceToken = stopServiceToken(_arg038, _arg118, _arg213);
                    reply.writeNoException();
                    reply.writeInt(stopServiceToken ? 1 : 0);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg039 = data.readStrongBinder();
                    ComponentName _result12 = getActivityClassForToken(_arg039);
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg040 = data.readStrongBinder();
                    String _result13 = getPackageForToken(_arg040);
                    reply.writeNoException();
                    reply.writeString(_result13);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg041 = data.readInt();
                    setProcessLimit(_arg041);
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    int _result14 = getProcessLimit();
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg042 = data.readString();
                    int _arg119 = data.readInt();
                    int _arg214 = data.readInt();
                    int _result15 = checkPermission(_arg042, _arg119, _arg214);
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                case 51:
                    return onTransact$checkUriPermission$(data, reply);
                case 52:
                    return onTransact$grantUriPermission$(data, reply);
                case 53:
                    return onTransact$revokeUriPermission$(data, reply);
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    IActivityController _arg043 = IActivityController.Stub.asInterface(data.readStrongBinder());
                    _arg1 = data.readInt() != 0;
                    setActivityController(_arg043, _arg1);
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg044 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    _arg1 = data.readInt() != 0;
                    showWaitingForDebugger(_arg044, _arg1);
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg045 = data.readInt();
                    signalPersistentProcesses(_arg045);
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg046 = data.readInt();
                    int _arg120 = data.readInt();
                    int _arg215 = data.readInt();
                    ParceledListSlice _result16 = getRecentTasks(_arg046, _arg120, _arg215);
                    reply.writeNoException();
                    if (_result16 != null) {
                        reply.writeInt(1);
                        _result16.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg047 = data.readStrongBinder();
                    int _arg121 = data.readInt();
                    int _arg216 = data.readInt();
                    int _arg36 = data.readInt();
                    serviceDoneExecuting(_arg047, _arg121, _arg216, _arg36);
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg048 = data.readStrongBinder();
                    activityDestroyed(_arg048);
                    return true;
                case 60:
                    return onTransact$getIntentSender$(data, reply);
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg049 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    cancelIntentSender(_arg049);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg050 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    String _result17 = getPackageForIntentSender(_arg050);
                    reply.writeNoException();
                    reply.writeString(_result17);
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg051 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    registerIntentSenderCancelListener(_arg051, IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg052 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    unregisterIntentSenderCancelListener(_arg052, IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    enterSafeMode();
                    reply.writeNoException();
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg053 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg13 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    Bundle _arg217 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    boolean startNextMatchingActivity = startNextMatchingActivity(_arg053, _arg13, _arg217);
                    reply.writeNoException();
                    reply.writeInt(startNextMatchingActivity ? 1 : 0);
                    return true;
                case 67:
                    return onTransact$noteWakeupAlarm$(data, reply);
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg054 = data.readStrongBinder();
                    _arg1 = data.readInt() != 0;
                    removeContentProvider(_arg054, _arg1);
                    reply.writeNoException();
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg055 = data.readStrongBinder();
                    setRequestedOrientation(_arg055, data.readInt());
                    reply.writeNoException();
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg056 = data.readStrongBinder();
                    int _result18 = getRequestedOrientation(_arg056);
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg057 = data.readStrongBinder();
                    Intent _arg122 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    _arg1 = data.readInt() != 0;
                    unbindFinished(_arg057, _arg122, _arg1);
                    reply.writeNoException();
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg058 = data.readStrongBinder();
                    int _arg123 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    String _arg37 = data.readString();
                    setProcessImportant(_arg058, _arg123, _arg1, _arg37);
                    reply.writeNoException();
                    return true;
                case 73:
                    return onTransact$setServiceForeground$(data, reply);
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg059 = data.readStrongBinder();
                    _arg1 = data.readInt() != 0;
                    boolean moveActivityTaskToBack = moveActivityTaskToBack(_arg059, _arg1);
                    reply.writeNoException();
                    reply.writeInt(moveActivityTaskToBack ? 1 : 0);
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    ActivityManager.MemoryInfo _arg060 = new ActivityManager.MemoryInfo();
                    getMemoryInfo(_arg060);
                    reply.writeNoException();
                    reply.writeInt(1);
                    _arg060.writeToParcel(reply, 1);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    List<ActivityManager.ProcessErrorStateInfo> _result19 = getProcessesInErrorState();
                    reply.writeNoException();
                    reply.writeTypedList(_result19);
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg061 = data.readString();
                    _arg1 = data.readInt() != 0;
                    IPackageDataObserver _arg218 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    int _arg38 = data.readInt();
                    boolean clearApplicationUserData = clearApplicationUserData(_arg061, _arg1, _arg218, _arg38);
                    reply.writeNoException();
                    reply.writeInt(clearApplicationUserData ? 1 : 0);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg062 = data.readString();
                    forceStopPackage(_arg062, data.readInt());
                    reply.writeNoException();
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg063 = data.createIntArray();
                    String _arg124 = data.readString();
                    _arg1 = data.readInt() != 0;
                    boolean killPids = killPids(_arg063, _arg124, _arg1);
                    reply.writeNoException();
                    reply.writeInt(killPids ? 1 : 0);
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg064 = data.readInt();
                    List<ActivityManager.RunningServiceInfo> _result20 = getServices(_arg064, data.readInt());
                    reply.writeNoException();
                    reply.writeTypedList(_result20);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg065 = data.readInt();
                    ActivityManager.TaskDescription _result21 = getTaskDescription(_arg065);
                    reply.writeNoException();
                    if (_result21 != null) {
                        reply.writeInt(1);
                        _result21.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    List<ActivityManager.RunningAppProcessInfo> _result22 = getRunningAppProcesses();
                    reply.writeNoException();
                    reply.writeTypedList(_result22);
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    ConfigurationInfo _result23 = getDeviceConfigurationInfo();
                    reply.writeNoException();
                    if (_result23 != null) {
                        reply.writeInt(1);
                        _result23.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg066 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg125 = data.readString();
                    String _arg219 = data.readString();
                    IBinder _result24 = peekService(_arg066, _arg125, _arg219);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result24);
                    return true;
                case 85:
                    return onTransact$profileControl$(data, reply);
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg067 = data.readInt();
                    boolean shutdown = shutdown(_arg067);
                    reply.writeNoException();
                    reply.writeInt(shutdown ? 1 : 0);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    stopAppSwitches();
                    reply.writeNoException();
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    resumeAppSwitches();
                    reply.writeNoException();
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg068 = data.readString();
                    int _arg126 = data.readInt();
                    int _arg220 = data.readInt();
                    boolean bindBackupAgent = bindBackupAgent(_arg068, _arg126, _arg220);
                    reply.writeNoException();
                    reply.writeInt(bindBackupAgent ? 1 : 0);
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg069 = data.readString();
                    backupAgentCreated(_arg069, data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    ApplicationInfo _arg070 = data.readInt() != 0 ? ApplicationInfo.CREATOR.createFromParcel(data) : null;
                    unbindBackupAgent(_arg070);
                    reply.writeNoException();
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg071 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    int _result25 = getUidForIntentSender(_arg071);
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case 93:
                    return onTransact$handleIncomingUser$(data, reply);
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg072 = data.readString();
                    addPackageDependency(_arg072);
                    reply.writeNoException();
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg073 = data.readString();
                    int _arg127 = data.readInt();
                    int _arg221 = data.readInt();
                    String _arg39 = data.readString();
                    killApplication(_arg073, _arg127, _arg221, _arg39);
                    reply.writeNoException();
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg074 = data.readString();
                    closeSystemDialogs(_arg074);
                    reply.writeNoException();
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg075 = data.createIntArray();
                    Debug.MemoryInfo[] _result26 = getProcessMemoryInfo(_arg075);
                    reply.writeNoException();
                    reply.writeTypedArray(_result26, 1);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg076 = data.readString();
                    killApplicationProcess(_arg076, data.readInt());
                    reply.writeNoException();
                    return true;
                case 99:
                    return onTransact$startActivityIntentSender$(data, reply);
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg077 = data.readStrongBinder();
                    String _arg128 = data.readString();
                    int _arg222 = data.readInt();
                    int _arg310 = data.readInt();
                    overridePendingTransition(_arg077, _arg128, _arg222, _arg310);
                    reply.writeNoException();
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg078 = data.readStrongBinder();
                    String _arg129 = data.readString();
                    _arg1 = data.readInt() != 0;
                    ApplicationErrorReport.ParcelableCrashInfo _arg311 = data.readInt() != 0 ? ApplicationErrorReport.ParcelableCrashInfo.CREATOR.createFromParcel(data) : null;
                    boolean handleApplicationWtf = handleApplicationWtf(_arg078, _arg129, _arg1, _arg311);
                    reply.writeNoException();
                    reply.writeInt(handleApplicationWtf ? 1 : 0);
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg079 = data.readString();
                    killBackgroundProcesses(_arg079, data.readInt());
                    reply.writeNoException();
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isUserAMonkey = isUserAMonkey();
                    reply.writeNoException();
                    reply.writeInt(isUserAMonkey ? 1 : 0);
                    return true;
                case 104:
                    return onTransact$startActivityAndWait$(data, reply);
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg080 = data.readStrongBinder();
                    boolean willActivityBeVisible = willActivityBeVisible(_arg080);
                    reply.writeNoException();
                    reply.writeInt(willActivityBeVisible ? 1 : 0);
                    return true;
                case 106:
                    return onTransact$startActivityWithConfig$(data, reply);
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    List<ApplicationInfo> _result27 = getRunningExternalApplications();
                    reply.writeNoException();
                    reply.writeTypedList(_result27);
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    finishHeavyWeightApp();
                    reply.writeNoException();
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg081 = data.readStrongBinder();
                    int _arg130 = data.readInt();
                    StrictMode.ViolationInfo _arg223 = data.readInt() != 0 ? StrictMode.ViolationInfo.CREATOR.createFromParcel(data) : null;
                    handleApplicationStrictModeViolation(_arg081, _arg130, _arg223);
                    reply.writeNoException();
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg082 = data.readStrongBinder();
                    boolean isImmersive = isImmersive(_arg082);
                    reply.writeNoException();
                    reply.writeInt(isImmersive ? 1 : 0);
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg083 = data.readStrongBinder();
                    _arg1 = data.readInt() != 0;
                    setImmersive(_arg083, _arg1);
                    reply.writeNoException();
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isTopActivityImmersive = isTopActivityImmersive();
                    reply.writeNoException();
                    reply.writeInt(isTopActivityImmersive ? 1 : 0);
                    return true;
                case 113:
                    return onTransact$crashApplication$(data, reply);
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    Uri _arg084 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    String _result28 = getProviderMimeType(_arg084, data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result28);
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg085 = data.readString();
                    IBinder _result29 = newUriPermissionOwner(_arg085);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result29);
                    return true;
                case 116:
                    return onTransact$grantUriPermissionFromOwner$(data, reply);
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg086 = data.readStrongBinder();
                    Uri _arg131 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    int _arg224 = data.readInt();
                    int _arg312 = data.readInt();
                    revokeUriPermissionFromOwner(_arg086, _arg131, _arg224, _arg312);
                    reply.writeNoException();
                    return true;
                case 118:
                    return onTransact$checkGrantUriPermission$(data, reply);
                case 119:
                    return onTransact$dumpHeap$(data, reply);
                case 120:
                    return onTransact$startActivities$(data, reply);
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg087 = data.readInt();
                    boolean isUserRunning = isUserRunning(_arg087, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(isUserRunning ? 1 : 0);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg088 = data.readStrongBinder();
                    activitySlept(_arg088);
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    int _result30 = getFrontActivityScreenCompatMode();
                    reply.writeNoException();
                    reply.writeInt(_result30);
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg089 = data.readInt();
                    setFrontActivityScreenCompatMode(_arg089);
                    reply.writeNoException();
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg090 = data.readString();
                    int _result31 = getPackageScreenCompatMode(_arg090);
                    reply.writeNoException();
                    reply.writeInt(_result31);
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg091 = data.readString();
                    setPackageScreenCompatMode(_arg091, data.readInt());
                    reply.writeNoException();
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg092 = data.readString();
                    boolean packageAskScreenCompat = getPackageAskScreenCompat(_arg092);
                    reply.writeNoException();
                    reply.writeInt(packageAskScreenCompat ? 1 : 0);
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg093 = data.readString();
                    _arg1 = data.readInt() != 0;
                    setPackageAskScreenCompat(_arg093, _arg1);
                    reply.writeNoException();
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg094 = data.readInt();
                    boolean switchUser = switchUser(_arg094);
                    reply.writeNoException();
                    reply.writeInt(switchUser ? 1 : 0);
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg095 = data.readInt();
                    setFocusedTask(_arg095);
                    reply.writeNoException();
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg096 = data.readInt();
                    boolean removeTask = removeTask(_arg096);
                    reply.writeNoException();
                    reply.writeInt(removeTask ? 1 : 0);
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    IProcessObserver _arg097 = IProcessObserver.Stub.asInterface(data.readStrongBinder());
                    registerProcessObserver(_arg097);
                    reply.writeNoException();
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    IProcessObserver _arg098 = IProcessObserver.Stub.asInterface(data.readStrongBinder());
                    unregisterProcessObserver(_arg098);
                    reply.writeNoException();
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg099 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    boolean isIntentSenderTargetedToPackage = isIntentSenderTargetedToPackage(_arg099);
                    reply.writeNoException();
                    reply.writeInt(isIntentSenderTargetedToPackage ? 1 : 0);
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    Configuration _arg0100 = data.readInt() != 0 ? Configuration.CREATOR.createFromParcel(data) : null;
                    updatePersistentConfiguration(_arg0100);
                    reply.writeNoException();
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg0101 = data.createIntArray();
                    long[] _result32 = getProcessPss(_arg0101);
                    reply.writeNoException();
                    reply.writeLongArray(_result32);
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    CharSequence _arg0102 = data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null;
                    _arg1 = data.readInt() != 0;
                    showBootMessage(_arg0102, _arg1);
                    reply.writeNoException();
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    killAllBackgroundProcesses();
                    reply.writeNoException();
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0103 = data.readString();
                    int _arg132 = data.readInt();
                    IBinder _arg225 = data.readStrongBinder();
                    ContentProviderHolder _result33 = getContentProviderExternal(_arg0103, _arg132, _arg225);
                    reply.writeNoException();
                    if (_result33 != null) {
                        reply.writeInt(1);
                        _result33.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0104 = data.readString();
                    removeContentProviderExternal(_arg0104, data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    ActivityManager.RunningAppProcessInfo _arg0105 = new ActivityManager.RunningAppProcessInfo();
                    getMyMemoryState(_arg0105);
                    reply.writeNoException();
                    reply.writeInt(1);
                    _arg0105.writeToParcel(reply, 1);
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0106 = data.readString();
                    boolean killProcessesBelowForeground = killProcessesBelowForeground(_arg0106);
                    reply.writeNoException();
                    reply.writeInt(killProcessesBelowForeground ? 1 : 0);
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    UserInfo _result34 = getCurrentUser();
                    reply.writeNoException();
                    if (_result34 != null) {
                        reply.writeInt(1);
                        _result34.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0107 = data.readStrongBinder();
                    boolean shouldUpRecreateTask = shouldUpRecreateTask(_arg0107, data.readString());
                    reply.writeNoException();
                    reply.writeInt(shouldUpRecreateTask ? 1 : 0);
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0108 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg14 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    int _arg226 = data.readInt();
                    Intent _arg313 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    boolean navigateUpTo = navigateUpTo(_arg0108, _arg14, _arg226, _arg313);
                    reply.writeNoException();
                    reply.writeInt(navigateUpTo ? 1 : 0);
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg0109 = data.readInt() != 0;
                    _arg1 = data.readInt() != 0;
                    int _arg227 = data.readInt();
                    setLockScreenShown(_arg0109, _arg1, _arg227);
                    reply.writeNoException();
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0110 = data.readStrongBinder();
                    boolean finishActivityAffinity = finishActivityAffinity(_arg0110);
                    reply.writeNoException();
                    reply.writeInt(finishActivityAffinity ? 1 : 0);
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0111 = data.readStrongBinder();
                    int _result35 = getLaunchedFromUid(_arg0111);
                    reply.writeNoException();
                    reply.writeInt(_result35);
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0112 = data.readStrongBinder();
                    unstableProviderDied(_arg0112);
                    reply.writeNoException();
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0113 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    boolean isIntentSenderAnActivity = isIntentSenderAnActivity(_arg0113);
                    reply.writeNoException();
                    reply.writeInt(isIntentSenderAnActivity ? 1 : 0);
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0114 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    boolean isIntentSenderAForegroundService = isIntentSenderAForegroundService(_arg0114);
                    reply.writeNoException();
                    reply.writeInt(isIntentSenderAForegroundService ? 1 : 0);
                    return true;
                case 152:
                    return onTransact$startActivityAsUser$(data, reply);
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0115 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    IStopUserCallback _arg228 = IStopUserCallback.Stub.asInterface(data.readStrongBinder());
                    int _result36 = stopUser(_arg0115, _arg1, _arg228);
                    reply.writeNoException();
                    reply.writeInt(_result36);
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    IUserSwitchObserver _arg0116 = IUserSwitchObserver.Stub.asInterface(data.readStrongBinder());
                    registerUserSwitchObserver(_arg0116, data.readString());
                    reply.writeNoException();
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    IUserSwitchObserver _arg0117 = IUserSwitchObserver.Stub.asInterface(data.readStrongBinder());
                    unregisterUserSwitchObserver(_arg0117);
                    reply.writeNoException();
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result37 = getRunningUserIds();
                    reply.writeNoException();
                    reply.writeIntArray(_result37);
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0118 = data.readInt();
                    requestBugReport(_arg0118);
                    reply.writeNoException();
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0119 = data.readString();
                    requestTelephonyBugReport(_arg0119, data.readString());
                    reply.writeNoException();
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0120 = data.readString();
                    requestWifiBugReport(_arg0120, data.readString());
                    reply.writeNoException();
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0121 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    String _arg229 = data.readString();
                    long _result38 = inputDispatchingTimedOut(_arg0121, _arg1, _arg229);
                    reply.writeNoException();
                    reply.writeLong(_result38);
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    clearPendingBackup();
                    reply.writeNoException();
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0122 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    Intent _result39 = getIntentForIntentSender(_arg0122);
                    reply.writeNoException();
                    if (_result39 != null) {
                        reply.writeInt(1);
                        _result39.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0123 = data.readInt();
                    Bundle _result40 = getAssistContextExtras(_arg0123);
                    reply.writeNoException();
                    if (_result40 != null) {
                        reply.writeInt(1);
                        _result40.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 164:
                    return onTransact$reportAssistContextExtras$(data, reply);
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0124 = data.readStrongBinder();
                    String _result41 = getLaunchedFromPackage(_arg0124);
                    reply.writeNoException();
                    reply.writeString(_result41);
                    return true;
                case 166:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0125 = data.readInt();
                    int _arg133 = data.readInt();
                    String _arg230 = data.readString();
                    killUid(_arg0125, _arg133, _arg230);
                    reply.writeNoException();
                    return true;
                case 167:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0126 = _arg1;
                    setUserIsMonkey(_arg0126);
                    reply.writeNoException();
                    return true;
                case 168:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0127 = data.readStrongBinder();
                    _arg1 = data.readInt() != 0;
                    hang(_arg0127, _arg1);
                    reply.writeNoException();
                    return true;
                case 169:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0128 = data.readInt();
                    int _arg134 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setTaskWindowingMode(_arg0128, _arg134, _arg1);
                    reply.writeNoException();
                    return true;
                case 170:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0129 = data.readInt();
                    int _arg135 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    moveTaskToStack(_arg0129, _arg135, _arg1);
                    reply.writeNoException();
                    return true;
                case 171:
                    return onTransact$resizeStack$(data, reply);
                case 172:
                    data.enforceInterface(DESCRIPTOR);
                    List<ActivityManager.StackInfo> _result42 = getAllStackInfos();
                    reply.writeNoException();
                    reply.writeTypedList(_result42);
                    return true;
                case 173:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0130 = data.readInt();
                    setFocusedStack(_arg0130);
                    reply.writeNoException();
                    return true;
                case 174:
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
                case 175:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0131 = data.readInt();
                    ActivityManager.StackInfo _result44 = getStackInfo(_arg0131, data.readInt());
                    reply.writeNoException();
                    if (_result44 != null) {
                        reply.writeInt(1);
                        _result44.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 176:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0132 = data.readStrongBinder();
                    boolean convertFromTranslucent = convertFromTranslucent(_arg0132);
                    reply.writeNoException();
                    reply.writeInt(convertFromTranslucent ? 1 : 0);
                    return true;
                case 177:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0133 = data.readStrongBinder();
                    boolean convertToTranslucent = convertToTranslucent(_arg0133, data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeInt(convertToTranslucent ? 1 : 0);
                    return true;
                case 178:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0134 = data.readStrongBinder();
                    notifyActivityDrawn(_arg0134);
                    reply.writeNoException();
                    return true;
                case 179:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0135 = data.readStrongBinder();
                    _arg1 = data.readInt() != 0;
                    reportActivityFullyDrawn(_arg0135, _arg1);
                    reply.writeNoException();
                    return true;
                case 180:
                    data.enforceInterface(DESCRIPTOR);
                    restart();
                    reply.writeNoException();
                    return true;
                case 181:
                    data.enforceInterface(DESCRIPTOR);
                    performIdleMaintenance();
                    reply.writeNoException();
                    return true;
                case 182:
                    data.enforceInterface(DESCRIPTOR);
                    Uri _arg0136 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    int _arg136 = data.readInt();
                    String _arg231 = data.readString();
                    int _arg314 = data.readInt();
                    takePersistableUriPermission(_arg0136, _arg136, _arg231, _arg314);
                    reply.writeNoException();
                    return true;
                case 183:
                    return onTransact$releasePersistableUriPermission$(data, reply);
                case 184:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0137 = data.readString();
                    ParceledListSlice _result45 = getPersistedUriPermissions(_arg0137, data.readInt() != 0);
                    reply.writeNoException();
                    if (_result45 != null) {
                        reply.writeInt(1);
                        _result45.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 185:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0138 = data.readStrongBinder();
                    appNotRespondingViaProvider(_arg0138);
                    reply.writeNoException();
                    return true;
                case 186:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0139 = data.readInt();
                    Rect _result46 = getTaskBounds(_arg0139);
                    reply.writeNoException();
                    if (_result46 != null) {
                        reply.writeInt(1);
                        _result46.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 187:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0140 = data.readStrongBinder();
                    int _result47 = getActivityDisplayId(_arg0140);
                    reply.writeNoException();
                    reply.writeInt(_result47);
                    return true;
                case 188:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0141 = data.readString();
                    int _arg137 = data.readInt();
                    int _arg232 = data.readInt();
                    boolean processMemoryTrimLevel = setProcessMemoryTrimLevel(_arg0141, _arg137, _arg232);
                    reply.writeNoException();
                    reply.writeInt(processMemoryTrimLevel ? 1 : 0);
                    return true;
                case 189:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentSender _arg0142 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    String _result48 = getTagForIntentSender(_arg0142, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result48);
                    return true;
                case 190:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0143 = data.readInt();
                    boolean startUserInBackground = startUserInBackground(_arg0143);
                    reply.writeNoException();
                    reply.writeInt(startUserInBackground ? 1 : 0);
                    return true;
                case 191:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0144 = data.readStrongBinder();
                    startLockTaskModeByToken(_arg0144);
                    reply.writeNoException();
                    return true;
                case 192:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0145 = data.readStrongBinder();
                    stopLockTaskModeByToken(_arg0145);
                    reply.writeNoException();
                    return true;
                case 193:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isInLockTaskMode = isInLockTaskMode();
                    reply.writeNoException();
                    reply.writeInt(isInLockTaskMode ? 1 : 0);
                    return true;
                case 194:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0146 = data.readStrongBinder();
                    setTaskDescription(_arg0146, data.readInt() != 0 ? ActivityManager.TaskDescription.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 195:
                    return onTransact$startVoiceActivity$(data, reply);
                case 196:
                    return onTransact$startAssistantActivity$(data, reply);
                case 197:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg0147 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    IAssistDataReceiver _arg138 = IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
                    IRecentsAnimationRunner _arg233 = IRecentsAnimationRunner.Stub.asInterface(data.readStrongBinder());
                    startRecentsActivity(_arg0147, _arg138, _arg233);
                    reply.writeNoException();
                    return true;
                case 198:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0148 = _arg1;
                    cancelRecentsAnimation(_arg0148);
                    reply.writeNoException();
                    return true;
                case 199:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0149 = data.readInt();
                    int _result49 = startActivityFromRecents(_arg0149, data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeInt(_result49);
                    return true;
                case 200:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0150 = data.readStrongBinder();
                    Bundle _result50 = getActivityOptions(_arg0150);
                    reply.writeNoException();
                    if (_result50 != null) {
                        reply.writeInt(1);
                        _result50.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 201:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0151 = data.readString();
                    List<IBinder> _result51 = getAppTasks(_arg0151);
                    reply.writeNoException();
                    reply.writeBinderList(_result51);
                    return true;
                case 202:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0152 = data.readInt();
                    startSystemLockTaskMode(_arg0152);
                    reply.writeNoException();
                    return true;
                case 203:
                    data.enforceInterface(DESCRIPTOR);
                    stopSystemLockTaskMode();
                    reply.writeNoException();
                    return true;
                case 204:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractionSession _arg0153 = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                    finishVoiceTask(_arg0153);
                    reply.writeNoException();
                    return true;
                case 205:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0154 = data.readStrongBinder();
                    boolean isTopOfTask = isTopOfTask(_arg0154);
                    reply.writeNoException();
                    reply.writeInt(isTopOfTask ? 1 : 0);
                    return true;
                case 206:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0155 = data.readStrongBinder();
                    notifyLaunchTaskBehindComplete(_arg0155);
                    reply.writeNoException();
                    return true;
                case 207:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0156 = data.readStrongBinder();
                    notifyEnterAnimationComplete(_arg0156);
                    reply.writeNoException();
                    return true;
                case 208:
                    return onTransact$startActivityAsCaller$(data, reply);
                case 209:
                    return onTransact$addAppTask$(data, reply);
                case 210:
                    data.enforceInterface(DESCRIPTOR);
                    Point _result52 = getAppTaskThumbnailSize();
                    reply.writeNoException();
                    if (_result52 != null) {
                        reply.writeInt(1);
                        _result52.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 211:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0157 = data.readStrongBinder();
                    boolean releaseActivityInstance = releaseActivityInstance(_arg0157);
                    reply.writeNoException();
                    reply.writeInt(releaseActivityInstance ? 1 : 0);
                    return true;
                case 212:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg0158 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    releaseSomeActivities(_arg0158);
                    reply.writeNoException();
                    return true;
                case 213:
                    data.enforceInterface(DESCRIPTOR);
                    bootAnimationComplete();
                    reply.writeNoException();
                    return true;
                case 214:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0159 = data.readString();
                    Bitmap _result53 = getTaskDescriptionIcon(_arg0159, data.readInt());
                    reply.writeNoException();
                    if (_result53 != null) {
                        reply.writeInt(1);
                        _result53.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 215:
                    return onTransact$launchAssistIntent$(data, reply);
                case 216:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _arg0160 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    startInPlaceAnimationOnFrontMostApplication(_arg0160);
                    reply.writeNoException();
                    return true;
                case 217:
                    return onTransact$checkPermissionWithToken$(data, reply);
                case 218:
                    data.enforceInterface(DESCRIPTOR);
                    ITaskStackListener _arg0161 = ITaskStackListener.Stub.asInterface(data.readStrongBinder());
                    registerTaskStackListener(_arg0161);
                    reply.writeNoException();
                    return true;
                case 219:
                    data.enforceInterface(DESCRIPTOR);
                    ITaskStackListener _arg0162 = ITaskStackListener.Stub.asInterface(data.readStrongBinder());
                    unregisterTaskStackListener(_arg0162);
                    reply.writeNoException();
                    return true;
                case 220:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0163 = data.readInt();
                    notifyCleartextNetwork(_arg0163, data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 221:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0164 = data.readInt();
                    int _result54 = createStackOnDisplay(_arg0164);
                    reply.writeNoException();
                    reply.writeInt(_result54);
                    return true;
                case 222:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0165 = data.readInt();
                    setTaskResizeable(_arg0165, data.readInt());
                    reply.writeNoException();
                    return true;
                case 223:
                    return onTransact$requestAssistContextExtras$(data, reply);
                case 224:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0166 = data.readInt();
                    Rect _arg139 = data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null;
                    int _arg234 = data.readInt();
                    resizeTask(_arg0166, _arg139, _arg234);
                    reply.writeNoException();
                    return true;
                case 225:
                    data.enforceInterface(DESCRIPTOR);
                    int _result55 = getLockTaskModeState();
                    reply.writeNoException();
                    reply.writeInt(_result55);
                    return true;
                case 226:
                    return onTransact$setDumpHeapDebugLimit$(data, reply);
                case 227:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0167 = data.readString();
                    dumpHeapFinished(_arg0167);
                    reply.writeNoException();
                    return true;
                case 228:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractionSession _arg0168 = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                    _arg1 = data.readInt() != 0;
                    setVoiceKeepAwake(_arg0168, _arg1);
                    reply.writeNoException();
                    return true;
                case 229:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0169 = data.readInt();
                    updateLockTaskPackages(_arg0169, data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 230:
                    return onTransact$noteAlarmStart$(data, reply);
                case 231:
                    return onTransact$noteAlarmFinish$(data, reply);
                case 232:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0170 = data.readString();
                    int _result56 = getPackageProcessState(_arg0170, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result56);
                    return true;
                case 233:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0171 = data.readStrongBinder();
                    showLockTaskEscapeMessage(_arg0171);
                    return true;
                case 234:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0172 = data.readString();
                    updateDeviceOwner(_arg0172);
                    reply.writeNoException();
                    return true;
                case 235:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0173 = data.readInt();
                    keyguardGoingAway(_arg0173);
                    reply.writeNoException();
                    return true;
                case 236:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0174 = data.readInt();
                    int _result57 = getUidProcessState(_arg0174, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result57);
                    return true;
                case 237:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAssistDataAllowedOnCurrentActivity = isAssistDataAllowedOnCurrentActivity();
                    reply.writeNoException();
                    reply.writeInt(isAssistDataAllowedOnCurrentActivity ? 1 : 0);
                    return true;
                case 238:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0175 = data.readStrongBinder();
                    boolean showAssistFromActivity = showAssistFromActivity(_arg0175, data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeInt(showAssistFromActivity ? 1 : 0);
                    return true;
                case 239:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0176 = data.readStrongBinder();
                    boolean isRootVoiceInteraction = isRootVoiceInteraction(_arg0176);
                    reply.writeNoException();
                    reply.writeInt(isRootVoiceInteraction ? 1 : 0);
                    return true;
                case 240:
                    data.enforceInterface(DESCRIPTOR);
                    boolean startBinderTracking = startBinderTracking();
                    reply.writeNoException();
                    reply.writeInt(startBinderTracking ? 1 : 0);
                    return true;
                case 241:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _arg0177 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    boolean stopBinderTrackingAndDump = stopBinderTrackingAndDump(_arg0177);
                    reply.writeNoException();
                    reply.writeInt(stopBinderTrackingAndDump ? 1 : 0);
                    return true;
                case 242:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0178 = data.readInt();
                    int _arg140 = data.readInt();
                    int _arg235 = data.readInt();
                    positionTaskInStack(_arg0178, _arg140, _arg235);
                    reply.writeNoException();
                    return true;
                case 243:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0179 = data.readStrongBinder();
                    exitFreeformMode(_arg0179);
                    reply.writeNoException();
                    return true;
                case 244:
                    return onTransact$reportSizeConfigurations$(data, reply);
                case 245:
                    boolean _arg0180 = onTransact$setTaskWindowingModeSplitScreenPrimary$(data, reply);
                    return _arg0180;
                case 246:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0181 = _arg1;
                    dismissSplitScreenMode(_arg0181);
                    reply.writeNoException();
                    return true;
                case 247:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0182 = _arg1;
                    dismissPip(_arg0182, data.readInt());
                    reply.writeNoException();
                    return true;
                case 248:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0183 = _arg1;
                    suppressResizeConfigChanges(_arg0183);
                    reply.writeNoException();
                    return true;
                case 249:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0184 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    moveTasksToFullscreenStack(_arg0184, _arg1);
                    reply.writeNoException();
                    return true;
                case 250:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0185 = data.readInt();
                    boolean moveTopActivityToPinnedStack = moveTopActivityToPinnedStack(_arg0185, data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeInt(moveTopActivityToPinnedStack ? 1 : 0);
                    return true;
                case 251:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0186 = data.readInt();
                    boolean isAppStartModeDisabled = isAppStartModeDisabled(_arg0186, data.readString());
                    reply.writeNoException();
                    reply.writeInt(isAppStartModeDisabled ? 1 : 0);
                    return true;
                case 252:
                    return onTransact$unlockUser$(data, reply);
                case 253:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0187 = data.readStrongBinder();
                    boolean isInMultiWindowMode = isInMultiWindowMode(_arg0187);
                    reply.writeNoException();
                    reply.writeInt(isInMultiWindowMode ? 1 : 0);
                    return true;
                case 254:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0188 = data.readStrongBinder();
                    boolean isInPictureInPictureMode = isInPictureInPictureMode(_arg0188);
                    reply.writeNoException();
                    reply.writeInt(isInPictureInPictureMode ? 1 : 0);
                    return true;
                case 255:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0189 = data.readString();
                    killPackageDependents(_arg0189, data.readInt());
                    reply.writeNoException();
                    return true;
                case 256:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0190 = data.readStrongBinder();
                    boolean enterPictureInPictureMode = enterPictureInPictureMode(_arg0190, data.readInt() != 0 ? PictureInPictureParams.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeInt(enterPictureInPictureMode ? 1 : 0);
                    return true;
                case 257:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0191 = data.readStrongBinder();
                    setPictureInPictureParams(_arg0191, data.readInt() != 0 ? PictureInPictureParams.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 258:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0192 = data.readStrongBinder();
                    int _result58 = getMaxNumPictureInPictureActions(_arg0192);
                    reply.writeNoException();
                    reply.writeInt(_result58);
                    return true;
                case 259:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0193 = data.readStrongBinder();
                    activityRelaunched(_arg0193);
                    reply.writeNoException();
                    return true;
                case 260:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0194 = data.readStrongBinder();
                    IBinder _result59 = getUriPermissionOwnerForActivity(_arg0194);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result59);
                    return true;
                case 261:
                    boolean _arg0195 = onTransact$resizeDockedStack$(data, reply);
                    return _arg0195;
                case 262:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0196 = _arg1;
                    setSplitScreenResizing(_arg0196);
                    reply.writeNoException();
                    return true;
                case 263:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0197 = data.readStrongBinder();
                    _arg1 = data.readInt() != 0;
                    ComponentName _arg236 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _result60 = setVrMode(_arg0197, _arg1, _arg236);
                    reply.writeNoException();
                    reply.writeInt(_result60);
                    return true;
                case 264:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0198 = data.readString();
                    ParceledListSlice _result61 = getGrantedUriPermissions(_arg0198, data.readInt());
                    reply.writeNoException();
                    if (_result61 != null) {
                        reply.writeInt(1);
                        _result61.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 265:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0199 = data.readString();
                    clearGrantedUriPermissions(_arg0199, data.readInt());
                    reply.writeNoException();
                    return true;
                case 266:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0200 = data.readInt();
                    boolean isAppForeground = isAppForeground(_arg0200);
                    reply.writeNoException();
                    reply.writeInt(isAppForeground ? 1 : 0);
                    return true;
                case 267:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0201 = data.readStrongBinder();
                    startLocalVoiceInteraction(_arg0201, data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 268:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0202 = data.readStrongBinder();
                    stopLocalVoiceInteraction(_arg0202);
                    reply.writeNoException();
                    return true;
                case 269:
                    data.enforceInterface(DESCRIPTOR);
                    boolean supportsLocalVoiceInteraction = supportsLocalVoiceInteraction();
                    reply.writeNoException();
                    reply.writeInt(supportsLocalVoiceInteraction ? 1 : 0);
                    return true;
                case 270:
                    data.enforceInterface(DESCRIPTOR);
                    notifyPinnedStackAnimationStarted();
                    reply.writeNoException();
                    return true;
                case 271:
                    data.enforceInterface(DESCRIPTOR);
                    notifyPinnedStackAnimationEnded();
                    reply.writeNoException();
                    return true;
                case 272:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0203 = data.readInt();
                    removeStack(_arg0203);
                    reply.writeNoException();
                    return true;
                case 273:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg0204 = data.createIntArray();
                    removeStacksInWindowingModes(_arg0204);
                    reply.writeNoException();
                    return true;
                case 274:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg0205 = data.createIntArray();
                    removeStacksWithActivityTypes(_arg0205);
                    reply.writeNoException();
                    return true;
                case 275:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0206 = data.readString();
                    makePackageIdle(_arg0206, data.readInt());
                    reply.writeNoException();
                    return true;
                case 276:
                    data.enforceInterface(DESCRIPTOR);
                    int _result62 = getMemoryTrimLevel();
                    reply.writeNoException();
                    reply.writeInt(_result62);
                    return true;
                case 277:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    resizePinnedStack(_arg0, data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 278:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0207 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isVrModePackageEnabled = isVrModePackageEnabled(_arg0207);
                    reply.writeNoException();
                    reply.writeInt(isVrModePackageEnabled ? 1 : 0);
                    return true;
                case 279:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0208 = data.readInt();
                    notifyLockedProfile(_arg0208);
                    reply.writeNoException();
                    return true;
                case 280:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    startConfirmDeviceCredentialIntent(_arg02, data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 281:
                    data.enforceInterface(DESCRIPTOR);
                    sendIdleJobTrigger();
                    reply.writeNoException();
                    return true;
                case 282:
                    return onTransact$sendIntentSender$(data, reply);
                case 283:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0209 = data.readString();
                    boolean isBackgroundRestricted = isBackgroundRestricted(_arg0209);
                    reply.writeNoException();
                    reply.writeInt(isBackgroundRestricted ? 1 : 0);
                    return true;
                case 284:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0210 = data.readInt();
                    setVrThread(_arg0210);
                    reply.writeNoException();
                    return true;
                case 285:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0211 = data.readInt();
                    setRenderThread(_arg0211);
                    reply.writeNoException();
                    return true;
                case 286:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0212 = _arg1;
                    setHasTopUi(_arg0212);
                    reply.writeNoException();
                    return true;
                case 287:
                    data.enforceInterface(DESCRIPTOR);
                    Configuration _arg0213 = data.readInt() != 0 ? Configuration.CREATOR.createFromParcel(data) : null;
                    boolean updateDisplayOverrideConfiguration = updateDisplayOverrideConfiguration(_arg0213, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(updateDisplayOverrideConfiguration ? 1 : 0);
                    return true;
                case 288:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0214 = data.readInt();
                    moveStackToDisplay(_arg0214, data.readInt());
                    reply.writeNoException();
                    return true;
                case 289:
                    return onTransact$requestAutofillData$(data, reply);
                case 290:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0215 = data.readStrongBinder();
                    IKeyguardDismissCallback _arg141 = IKeyguardDismissCallback.Stub.asInterface(data.readStrongBinder());
                    CharSequence _arg237 = data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null;
                    dismissKeyguard(_arg0215, _arg141, _arg237);
                    reply.writeNoException();
                    return true;
                case 291:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0216 = data.readInt();
                    int _result63 = restartUserInBackground(_arg0216);
                    reply.writeNoException();
                    reply.writeInt(_result63);
                    return true;
                case 292:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0217 = data.readInt();
                    cancelTaskWindowTransition(_arg0217);
                    reply.writeNoException();
                    return true;
                case 293:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0218 = data.readInt();
                    ActivityManager.TaskSnapshot _result64 = getTaskSnapshot(_arg0218, data.readInt() != 0);
                    reply.writeNoException();
                    if (_result64 != null) {
                        reply.writeInt(1);
                        _result64.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 294:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg0219 = data.createStringArrayList();
                    scheduleApplicationInfoChanged(_arg0219, data.readInt());
                    reply.writeNoException();
                    return true;
                case 295:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0220 = data.readInt();
                    setPersistentVrThread(_arg0220);
                    reply.writeNoException();
                    return true;
                case 296:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg0221 = data.readLong();
                    waitForNetworkStateUpdate(_arg0221);
                    reply.writeNoException();
                    return true;
                case 297:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0222 = data.readStrongBinder();
                    _arg1 = data.readInt() != 0;
                    setDisablePreviewScreenshots(_arg0222, _arg1);
                    reply.writeNoException();
                    return true;
                case 298:
                    data.enforceInterface(DESCRIPTOR);
                    int _result65 = getLastResumedActivityUserId();
                    reply.writeNoException();
                    reply.writeInt(_result65);
                    return true;
                case 299:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0223 = data.readInt();
                    backgroundWhitelistUid(_arg0223);
                    reply.writeNoException();
                    return true;
                case 300:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0224 = data.readInt();
                    updateLockTaskFeatures(_arg0224, data.readInt());
                    reply.writeNoException();
                    return true;
                case 301:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0225 = data.readStrongBinder();
                    _arg1 = data.readInt() != 0;
                    setShowWhenLocked(_arg0225, _arg1);
                    reply.writeNoException();
                    return true;
                case 302:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0226 = data.readStrongBinder();
                    _arg1 = data.readInt() != 0;
                    setTurnScreenOn(_arg0226, _arg1);
                    reply.writeNoException();
                    return true;
                case 303:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0227 = data.readInt();
                    boolean startUserInBackgroundWithListener = startUserInBackgroundWithListener(_arg0227, IProgressListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(startUserInBackgroundWithListener ? 1 : 0);
                    return true;
                case 304:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0228 = data.readStrongBinder();
                    RemoteAnimationDefinition _arg142 = data.readInt() != 0 ? RemoteAnimationDefinition.CREATOR.createFromParcel(data) : null;
                    registerRemoteAnimations(_arg0228, _arg142);
                    reply.writeNoException();
                    return true;
                case 305:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0229 = data.readString();
                    RemoteAnimationAdapter _arg143 = data.readInt() != 0 ? RemoteAnimationAdapter.CREATOR.createFromParcel(data) : null;
                    registerRemoteAnimationForNextActivityStart(_arg0229, _arg143);
                    reply.writeNoException();
                    return true;
                case 306:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0230 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    alwaysShowUnsupportedCompileSdkWarning(_arg0230);
                    reply.writeNoException();
                    return true;
                case 307:
                    data.enforceInterface(DESCRIPTOR);
                    finishMiniProgram();
                    reply.writeNoException();
                    return true;
                case 308:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0231 = data.readInt();
                    setFocusedAppNoChecked(_arg0231);
                    reply.writeNoException();
                    return true;
                case 309:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _arg0232 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    handleActivityChanged(_arg0232);
                    reply.writeNoException();
                    return true;
                case 310:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isTopActivityFullscreen = isTopActivityFullscreen();
                    reply.writeNoException();
                    reply.writeInt(isTopActivityFullscreen ? 1 : 0);
                    return true;
                case 311:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0233 = data.readString();
                    forceGrantFolderPermission(_arg0233);
                    reply.writeNoException();
                    return true;
                case 312:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0234 = data.readString();
                    String _result66 = getOption(_arg0234, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result66);
                    return true;
                case 313:
                    data.enforceInterface(DESCRIPTOR);
                    double[] _result67 = getUsageInfo();
                    reply.writeNoException();
                    reply.writeDoubleArray(_result67);
                    return true;
                case 314:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0235 = _arg1;
                    List<xpDialogInfo> _result68 = getDialogRecorder(_arg0235);
                    reply.writeNoException();
                    reply.writeTypedList(_result68);
                    return true;
                case 315:
                    data.enforceInterface(DESCRIPTOR);
                    xpDialogInfo _arg0236 = data.readInt() != 0 ? xpDialogInfo.CREATOR.createFromParcel(data) : null;
                    setDialogRecorder(_arg0236);
                    reply.writeNoException();
                    return true;
                case 316:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0237 = data.readInt();
                    dismissDialog(_arg0237);
                    reply.writeNoException();
                    return true;
                case 317:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0238 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    setHomeState(_arg0238, data.readInt());
                    reply.writeNoException();
                    return true;
                case 318:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result69 = getSpeechObserver();
                    reply.writeNoException();
                    reply.writeStringList(_result69);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IActivityManager {
            public protected IBinder mRemote;

            synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.IActivityManager
            public synchronized ParcelFileDescriptor openContentUri(String uriString) throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uriString);
                    this.mRemote.transact(1, _data, _reply, 0);
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
            public synchronized void registerUidObserver(IUidObserver observer, int which, int cutpoint, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(which);
                    _data.writeInt(cutpoint);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void unregisterUidObserver(IUidObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isUidActive(int uid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void handleApplicationCrash(IBinder app, ApplicationErrorReport.ParcelableCrashInfo crashInfo) throws RemoteException {
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
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
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
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
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
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void unhandledBack() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean finishActivity(IBinder token, int code, Intent data, int finishTask) throws RemoteException {
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
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized Intent registerReceiver(IApplicationThread caller, String callerPackage, IIntentReceiver receiver, IntentFilter filter, String requiredPermission, int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Intent _result = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(callerPackage);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(requiredPermission);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Intent.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void unregisterReceiver(IIntentReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int broadcastIntent(IApplicationThread caller, Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode, String resultData, Bundle map, String[] requiredPermissions, int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
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
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(resolvedType);
                    _data.writeStrongBinder(resultTo != null ? resultTo.asBinder() : null);
                    try {
                        _data.writeInt(resultCode);
                        try {
                            _data.writeString(resultData);
                            if (map != null) {
                                _data.writeInt(1);
                                map.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeStringArray(requiredPermissions);
                                try {
                                    _data.writeInt(appOp);
                                    if (options != null) {
                                        _data.writeInt(1);
                                        options.writeToParcel(_data, 0);
                                    } else {
                                        _data.writeInt(0);
                                    }
                                    try {
                                        _data.writeInt(serialized ? 1 : 0);
                                        try {
                                            _data.writeInt(sticky ? 1 : 0);
                                            try {
                                                _data.writeInt(userId);
                                                this.mRemote.transact(12, _data, _reply, 0);
                                                _reply.readException();
                                                int _result = _reply.readInt();
                                                _reply.recycle();
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
                                } catch (Throwable th5) {
                                    th = th5;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
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
                    } catch (Throwable th8) {
                        th = th8;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th9) {
                    th = th9;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void unbroadcastIntent(IApplicationThread caller, Intent intent, int userId) throws RemoteException {
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
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void finishReceiver(IBinder who, int resultCode, String resultData, Bundle map, boolean abortBroadcast, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(who);
                    _data.writeInt(resultCode);
                    _data.writeString(resultData);
                    if (map != null) {
                        _data.writeInt(1);
                        map.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(abortBroadcast ? 1 : 0);
                    _data.writeInt(flags);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void attachApplication(IApplicationThread app, long startSeq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app != null ? app.asBinder() : null);
                    _data.writeLong(startSeq);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void activityIdle(IBinder token, Configuration config, boolean stopProfiling) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(stopProfiling ? 1 : 0);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void activityPaused(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void activityStopped(IBinder token, Bundle state, PersistableBundle persistentState, CharSequence description) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (persistentState != null) {
                        _data.writeInt(1);
                        persistentState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (description != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(description, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized String getCallingPackage(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized ComponentName getCallingActivity(IBinder token) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.createFromParcel(_reply);
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
            public synchronized List<ActivityManager.RunningTaskInfo> getTasks(int maxNum) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    List<ActivityManager.RunningTaskInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningTaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized List<ActivityManager.RunningTaskInfo> getFilteredTasks(int maxNum, int ignoreActivityType, int ignoreWindowingMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(ignoreActivityType);
                    _data.writeInt(ignoreWindowingMode);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    List<ActivityManager.RunningTaskInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningTaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void moveTaskToFront(int task, int flags, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(task);
                    _data.writeInt(flags);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void moveTaskBackwards(int task) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(task);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(onlyRoot ? 1 : 0);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized ContentProviderHolder getContentProvider(IApplicationThread caller, String name, int userId, boolean stable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ContentProviderHolder _result = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeInt(stable ? 1 : 0);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ContentProviderHolder.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void publishContentProviders(IApplicationThread caller, List<ContentProviderHolder> providers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeTypedList(providers);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean refContentProvider(IBinder connection, int stableDelta, int unstableDelta) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    _data.writeInt(stableDelta);
                    _data.writeInt(unstableDelta);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void finishSubActivity(IBinder token, String resultWho, int requestCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized PendingIntent getRunningServiceControlPanel(ComponentName service) throws RemoteException {
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
                    this.mRemote.transact(30, _data, _reply, 0);
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
            public synchronized ComponentName startService(IApplicationThread caller, Intent service, String resolvedType, boolean requireForeground, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName _result = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(requireForeground ? 1 : 0);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int stopService(IApplicationThread caller, Intent service, String resolvedType, int userId) throws RemoteException {
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
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int bindService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeStrongBinder(token);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean unbindService(IServiceConnection connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void publishService(IBinder token, Intent intent, IBinder service) throws RemoteException {
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
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void activityResumed(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setDebugApp(String packageName, boolean waitForDebugger, boolean persistent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(waitForDebugger ? 1 : 0);
                    _data.writeInt(persistent ? 1 : 0);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setAgentApp(String packageName, String agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(agent);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setAlwaysFinish(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean startInstrumentation(ComponentName className, String profileFile, int flags, Bundle arguments, IInstrumentationWatcher watcher, IUiAutomationConnection connection, int userId, String abiOverride) throws RemoteException {
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
                    _data.writeString(profileFile);
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
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void addInstrumentationResults(IApplicationThread target, Bundle results) throws RemoteException {
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
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void finishInstrumentation(IApplicationThread target, int resultCode, Bundle results) throws RemoteException {
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
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected Configuration getConfiguration() throws RemoteException {
                Configuration _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
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

            public synchronized boolean updateConfiguration(Configuration values) throws RemoteException {
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
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean stopServiceToken(ComponentName className, IBinder token, int startId) throws RemoteException {
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
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized ComponentName getActivityClassForToken(IBinder token) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.createFromParcel(_reply);
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
            public synchronized String getPackageForToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setProcessLimit(int max) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(max);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected int getProcessLimit() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int checkPermission(String permission, int pid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int checkUriPermission(Uri uri, int pid, int uid, int mode, int userId, IBinder callerToken) throws RemoteException {
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
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(callerToken);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void grantUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
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
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void revokeUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
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
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    _data.writeInt(imAMonkey ? 1 : 0);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void showWaitingForDebugger(IApplicationThread who, boolean waiting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(who != null ? who.asBinder() : null);
                    _data.writeInt(waiting ? 1 : 0);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void signalPersistentProcesses(int signal) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(signal);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized ParceledListSlice getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(57, _data, _reply, 0);
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

            public synchronized void serviceDoneExecuting(IBinder token, int type, int startId, int res) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(type);
                    _data.writeInt(startId);
                    _data.writeInt(res);
                    this.mRemote.transact(58, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void activityDestroyed(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(59, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized IIntentSender getIntentSender(int type, String packageName, IBinder token, String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes, int flags, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    IIntentSender _result = IIntentSender.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void cancelIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized String getPackageForIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void registerIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void unregisterIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void enterSafeMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean startNextMatchingActivity(IBinder callingActivity, Intent intent, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingActivity);
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
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void noteWakeupAlarm(IIntentSender sender, WorkSource workSource, int sourceUid, String sourcePkg, String tag) throws RemoteException {
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
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void removeContentProvider(IBinder connection, boolean stable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    _data.writeInt(stable ? 1 : 0);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(requestedOrientation);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int getRequestedOrientation(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void unbindFinished(IBinder token, Intent service, boolean doRebind) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(doRebind ? 1 : 0);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setProcessImportant(IBinder token, int pid, boolean isForeground, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(pid);
                    _data.writeInt(isForeground ? 1 : 0);
                    _data.writeString(reason);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setServiceForeground(ComponentName className, IBinder token, int id, Notification notification, int flags) throws RemoteException {
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
                    _data.writeInt(id);
                    if (notification != null) {
                        _data.writeInt(1);
                        notification.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(nonRoot ? 1 : 0);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void getMemoryInfo(ActivityManager.MemoryInfo outInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(75, _data, _reply, 0);
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
            public synchronized List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                    List<ActivityManager.ProcessErrorStateInfo> _result = _reply.createTypedArrayList(ActivityManager.ProcessErrorStateInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean clearApplicationUserData(String packageName, boolean keepState, IPackageDataObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(keepState ? 1 : 0);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void forceStopPackage(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean killPids(int[] pids, String reason, boolean secure) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    _data.writeString(reason);
                    _data.writeInt(secure ? 1 : 0);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized List<ActivityManager.RunningServiceInfo> getServices(int maxNum, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    List<ActivityManager.RunningServiceInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningServiceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized ActivityManager.TaskDescription getTaskDescription(int taskId) throws RemoteException {
                ActivityManager.TaskDescription _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ActivityManager.TaskDescription.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                    List<ActivityManager.RunningAppProcessInfo> _result = _reply.createTypedArrayList(ActivityManager.RunningAppProcessInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException {
                ConfigurationInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ConfigurationInfo.CREATOR.createFromParcel(_reply);
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
            public synchronized IBinder peekService(Intent service, String resolvedType, String callingPackage) throws RemoteException {
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
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean profileControl(String process, int userId, boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(process);
                    _data.writeInt(userId);
                    _data.writeInt(start ? 1 : 0);
                    if (profilerInfo != null) {
                        _data.writeInt(1);
                        profilerInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(profileType);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean shutdown(int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeout);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void stopAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void resumeAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean bindBackupAgent(String packageName, int backupRestoreMode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(backupRestoreMode);
                    _data.writeInt(userId);
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void backupAgentCreated(String packageName, IBinder agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(agent);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void unbindBackupAgent(ApplicationInfo appInfo) throws RemoteException {
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
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int getUidForIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, String name, String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callingPid);
                    _data.writeInt(callingUid);
                    _data.writeInt(userId);
                    _data.writeInt(allowAll ? 1 : 0);
                    _data.writeInt(requireFull ? 1 : 0);
                    _data.writeString(name);
                    _data.writeString(callerPackage);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void addPackageDependency(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void killApplication(String pkg, int appId, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void closeSystemDialogs(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                    Debug.MemoryInfo[] _result = (Debug.MemoryInfo[]) _reply.createTypedArray(Debug.MemoryInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void killApplicationProcess(String processName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int startActivityIntentSender(IApplicationThread caller, IIntentSender target, IBinder whitelistToken, Intent fillInIntent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flagsMask, int flagsValues, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    try {
                        _data.writeStrongBinder(whitelistToken);
                        if (fillInIntent != null) {
                            _data.writeInt(1);
                            fillInIntent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(resolvedType);
                            try {
                                _data.writeStrongBinder(resultTo);
                            } catch (Throwable th) {
                                th = th;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(resultWho);
                        try {
                            _data.writeInt(requestCode);
                            try {
                                _data.writeInt(flagsMask);
                                try {
                                    _data.writeInt(flagsValues);
                                    if (options != null) {
                                        _data.writeInt(1);
                                        options.writeToParcel(_data, 0);
                                    } else {
                                        _data.writeInt(0);
                                    }
                                } catch (Throwable th4) {
                                    th = th4;
                                }
                            } catch (Throwable th5) {
                                th = th5;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
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
                } catch (Throwable th8) {
                    th = th8;
                }
                try {
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th9) {
                    th = th9;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void overridePendingTransition(IBinder token, String packageName, int enterAnim, int exitAnim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(packageName);
                    _data.writeInt(enterAnim);
                    _data.writeInt(exitAnim);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean handleApplicationWtf(IBinder app, String tag, boolean system, ApplicationErrorReport.ParcelableCrashInfo crashInfo) throws RemoteException {
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
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void killBackgroundProcesses(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isUserAMonkey() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized WaitResult startActivityAndWait(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                WaitResult _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callingPackage);
                        if (intent != null) {
                            _data.writeInt(1);
                            intent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(resolvedType);
                            try {
                                _data.writeStrongBinder(resultTo);
                            } catch (Throwable th) {
                                th = th;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(resultWho);
                        try {
                            _data.writeInt(requestCode);
                            try {
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
                                try {
                                    _data.writeInt(userId);
                                } catch (Throwable th4) {
                                    th = th4;
                                }
                            } catch (Throwable th5) {
                                th = th5;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
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
                } catch (Throwable th8) {
                    th = th8;
                }
                try {
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        WaitResult _result2 = WaitResult.CREATOR.createFromParcel(_reply);
                        _result = _result2;
                    } else {
                        _result = null;
                    }
                    WaitResult _result3 = _result;
                    _reply.recycle();
                    _data.recycle();
                    return _result3;
                } catch (Throwable th9) {
                    th = th9;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean willActivityBeVisible(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int startActivityWithConfig(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, Configuration newConfig, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callingPackage);
                        if (intent != null) {
                            _data.writeInt(1);
                            intent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(resolvedType);
                            try {
                                _data.writeStrongBinder(resultTo);
                            } catch (Throwable th) {
                                th = th;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(resultWho);
                        try {
                            _data.writeInt(requestCode);
                            try {
                                _data.writeInt(startFlags);
                                if (newConfig != null) {
                                    _data.writeInt(1);
                                    newConfig.writeToParcel(_data, 0);
                                } else {
                                    _data.writeInt(0);
                                }
                                if (options != null) {
                                    _data.writeInt(1);
                                    options.writeToParcel(_data, 0);
                                } else {
                                    _data.writeInt(0);
                                }
                                try {
                                    _data.writeInt(userId);
                                    try {
                                        this.mRemote.transact(106, _data, _reply, 0);
                                        _reply.readException();
                                        int _result = _reply.readInt();
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
                    } catch (Throwable th8) {
                        th = th8;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th9) {
                    th = th9;
                }
            }

            @Override // android.app.IActivityManager
            public synchronized List<ApplicationInfo> getRunningExternalApplications() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                    List<ApplicationInfo> _result = _reply.createTypedArrayList(ApplicationInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void finishHeavyWeightApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void handleApplicationStrictModeViolation(IBinder app, int violationMask, StrictMode.ViolationInfo crashInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app);
                    _data.writeInt(violationMask);
                    if (crashInfo != null) {
                        _data.writeInt(1);
                        crashInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isImmersive(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setImmersive(IBinder token, boolean immersive) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(immersive ? 1 : 0);
                    this.mRemote.transact(111, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isTopActivityImmersive() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(112, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void crashApplication(int uid, int initialPid, String packageName, int userId, String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(initialPid);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(message);
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getProviderMimeType(Uri uri, int userId) throws RemoteException {
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
                    this.mRemote.transact(114, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized IBinder newUriPermissionOwner(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void grantUriPermissionFromOwner(IBinder owner, int fromUid, String targetPkg, Uri uri, int mode, int sourceUserId, int targetUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(owner);
                    _data.writeInt(fromUid);
                    _data.writeString(targetPkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    _data.writeInt(sourceUserId);
                    _data.writeInt(targetUserId);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void revokeUriPermissionFromOwner(IBinder owner, Uri uri, int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(owner);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int checkGrantUriPermission(int callingUid, String targetPkg, Uri uri, int modeFlags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callingUid);
                    _data.writeString(targetPkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(modeFlags);
                    _data.writeInt(userId);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean dumpHeap(String process, int userId, boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(process);
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
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int startActivities(IApplicationThread caller, String callingPackage, Intent[] intents, String[] resolvedTypes, IBinder resultTo, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeTypedArray(intents, 0);
                    _data.writeStringArray(resolvedTypes);
                    _data.writeStrongBinder(resultTo);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(120, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isUserRunning(int userid, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeInt(flags);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void activitySlept(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(122, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int getFrontActivityScreenCompatMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setFrontActivityScreenCompatMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int getPackageScreenCompatMode(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(mode);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean getPackageAskScreenCompat(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setPackageAskScreenCompat(String packageName, boolean ask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(ask ? 1 : 0);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean switchUser(int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setFocusedTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean removeTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void registerProcessObserver(IProcessObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void unregisterProcessObserver(IProcessObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isIntentSenderTargetedToPackage(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected void updatePersistentConfiguration(Configuration values) throws RemoteException {
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
                    this.mRemote.transact(135, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected long[] getProcessPss(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    this.mRemote.transact(136, _data, _reply, 0);
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void showBootMessage(CharSequence msg, boolean always) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (msg != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(msg, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(always ? 1 : 0);
                    this.mRemote.transact(137, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void killAllBackgroundProcesses() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(138, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token) throws RemoteException {
                ContentProviderHolder _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(139, _data, _reply, 0);
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

            public synchronized void removeContentProviderExternal(String name, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void getMyMemoryState(ActivityManager.RunningAppProcessInfo outInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(141, _data, _reply, 0);
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
            public synchronized boolean killProcessesBelowForeground(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    this.mRemote.transact(142, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized UserInfo getCurrentUser() throws RemoteException {
                UserInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(143, _data, _reply, 0);
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
            public synchronized boolean shouldUpRecreateTask(IBinder token, String destAffinity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(destAffinity);
                    this.mRemote.transact(144, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean navigateUpTo(IBinder token, Intent target, int resultCode, Intent resultData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (target != null) {
                        _data.writeInt(1);
                        target.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(resultCode);
                    if (resultData != null) {
                        _data.writeInt(1);
                        resultData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(145, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setLockScreenShown(boolean showingKeyguard, boolean showingAod, int secondaryDisplayShowing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(showingKeyguard ? 1 : 0);
                    _data.writeInt(showingAod ? 1 : 0);
                    _data.writeInt(secondaryDisplayShowing);
                    this.mRemote.transact(146, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean finishActivityAffinity(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(147, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(148, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void unstableProviderDied(IBinder connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    this.mRemote.transact(149, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isIntentSenderAnActivity(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    this.mRemote.transact(150, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isIntentSenderAForegroundService(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    this.mRemote.transact(151, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int startActivityAsUser(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callingPackage);
                        if (intent != null) {
                            _data.writeInt(1);
                            intent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(resolvedType);
                            try {
                                _data.writeStrongBinder(resultTo);
                            } catch (Throwable th) {
                                th = th;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(resultWho);
                        try {
                            _data.writeInt(requestCode);
                            try {
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
                                try {
                                    _data.writeInt(userId);
                                    try {
                                        this.mRemote.transact(152, _data, _reply, 0);
                                        _reply.readException();
                                        int _result = _reply.readInt();
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
                    } catch (Throwable th8) {
                        th = th8;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th9) {
                    th = th9;
                }
            }

            public synchronized int stopUser(int userid, boolean force, IStopUserCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeInt(force ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(153, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void registerUserSwitchObserver(IUserSwitchObserver observer, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeString(name);
                    this.mRemote.transact(154, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void unregisterUserSwitchObserver(IUserSwitchObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(155, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int[] getRunningUserIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(156, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void requestBugReport(int bugreportType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bugreportType);
                    this.mRemote.transact(157, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void requestTelephonyBugReport(String shareTitle, String shareDescription) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(shareTitle);
                    _data.writeString(shareDescription);
                    this.mRemote.transact(158, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void requestWifiBugReport(String shareTitle, String shareDescription) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(shareTitle);
                    _data.writeString(shareDescription);
                    this.mRemote.transact(159, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized long inputDispatchingTimedOut(int pid, boolean aboveSystem, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(aboveSystem ? 1 : 0);
                    _data.writeString(reason);
                    this.mRemote.transact(160, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void clearPendingBackup() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(161, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized Intent getIntentForIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Intent _result = null;
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    this.mRemote.transact(162, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Intent.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized Bundle getAssistContextExtras(int requestType) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestType);
                    this.mRemote.transact(163, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.createFromParcel(_reply);
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
            public synchronized void reportAssistContextExtras(IBinder token, Bundle extras, AssistStructure structure, AssistContent content, Uri referrer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (structure != null) {
                        _data.writeInt(1);
                        structure.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (content != null) {
                        _data.writeInt(1);
                        content.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (referrer != null) {
                        _data.writeInt(1);
                        referrer.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(164, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(165, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void killUid(int appId, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    this.mRemote.transact(166, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setUserIsMonkey(boolean monkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(monkey ? 1 : 0);
                    this.mRemote.transact(167, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void hang(IBinder who, boolean allowRestart) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(who);
                    _data.writeInt(allowRestart ? 1 : 0);
                    this.mRemote.transact(168, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setTaskWindowingMode(int taskId, int windowingMode, boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(windowingMode);
                    _data.writeInt(toTop ? 1 : 0);
                    this.mRemote.transact(169, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void moveTaskToStack(int taskId, int stackId, boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    _data.writeInt(toTop ? 1 : 0);
                    this.mRemote.transact(170, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void resizeStack(int stackId, Rect bounds, boolean allowResizeInDockedMode, boolean preserveWindows, boolean animate, int animationDuration) throws RemoteException {
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
                    _data.writeInt(allowResizeInDockedMode ? 1 : 0);
                    _data.writeInt(preserveWindows ? 1 : 0);
                    _data.writeInt(animate ? 1 : 0);
                    _data.writeInt(animationDuration);
                    this.mRemote.transact(171, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized List<ActivityManager.StackInfo> getAllStackInfos() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(172, _data, _reply, 0);
                    _reply.readException();
                    List<ActivityManager.StackInfo> _result = _reply.createTypedArrayList(ActivityManager.StackInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setFocusedStack(int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    this.mRemote.transact(173, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized ActivityManager.StackInfo getFocusedStackInfo() throws RemoteException {
                ActivityManager.StackInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(174, _data, _reply, 0);
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
            public synchronized ActivityManager.StackInfo getStackInfo(int windowingMode, int activityType) throws RemoteException {
                ActivityManager.StackInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    _data.writeInt(activityType);
                    this.mRemote.transact(175, _data, _reply, 0);
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
            public synchronized boolean convertFromTranslucent(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(176, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean convertToTranslucent(IBinder token, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(177, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void notifyActivityDrawn(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(178, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void reportActivityFullyDrawn(IBinder token, boolean restoredFromBundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(restoredFromBundle ? 1 : 0);
                    this.mRemote.transact(179, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void restart() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(180, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void performIdleMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(181, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void takePersistableUriPermission(Uri uri, int modeFlags, String toPackage, int userId) throws RemoteException {
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
                    _data.writeInt(modeFlags);
                    _data.writeString(toPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(182, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void releasePersistableUriPermission(Uri uri, int modeFlags, String toPackage, int userId) throws RemoteException {
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
                    _data.writeInt(modeFlags);
                    _data.writeString(toPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(183, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized ParceledListSlice getPersistedUriPermissions(String packageName, boolean incoming) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(incoming ? 1 : 0);
                    this.mRemote.transact(184, _data, _reply, 0);
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
            public synchronized void appNotRespondingViaProvider(IBinder connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    this.mRemote.transact(185, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized Rect getTaskBounds(int taskId) throws RemoteException {
                Rect _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(186, _data, _reply, 0);
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
            public synchronized int getActivityDisplayId(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(187, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean setProcessMemoryTrimLevel(String process, int uid, int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(process);
                    _data.writeInt(uid);
                    _data.writeInt(level);
                    this.mRemote.transact(188, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized String getTagForIntentSender(IIntentSender sender, String prefix) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    _data.writeString(prefix);
                    this.mRemote.transact(189, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean startUserInBackground(int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    this.mRemote.transact(190, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void startLockTaskModeByToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(191, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void stopLockTaskModeByToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(192, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isInLockTaskMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(193, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setTaskDescription(IBinder token, ActivityManager.TaskDescription values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (values != null) {
                        _data.writeInt(1);
                        values.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(194, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int startVoiceActivity(String callingPackage, int callingPid, int callingUid, Intent intent, String resolvedType, IVoiceInteractionSession session, IVoiceInteractor interactor, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(callingPackage);
                    try {
                        _data.writeInt(callingPid);
                        try {
                            _data.writeInt(callingUid);
                            if (intent != null) {
                                _data.writeInt(1);
                                intent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeString(resolvedType);
                                _data.writeStrongBinder(session != null ? session.asBinder() : null);
                                _data.writeStrongBinder(interactor != null ? interactor.asBinder() : null);
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
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
                    try {
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
                        try {
                            _data.writeInt(userId);
                            try {
                                this.mRemote.transact(195, _data, _reply, 0);
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
                    } catch (Throwable th7) {
                        th = th7;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int startAssistantActivity(String callingPackage, int callingPid, int callingUid, Intent intent, String resolvedType, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(callingPid);
                    _data.writeInt(callingUid);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(196, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void startRecentsActivity(Intent intent, IAssistDataReceiver assistDataReceiver, IRecentsAnimationRunner recentsAnimationRunner) throws RemoteException {
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
                    this.mRemote.transact(197, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void cancelRecentsAnimation(boolean restoreHomeStackPosition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(restoreHomeStackPosition ? 1 : 0);
                    this.mRemote.transact(198, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
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
                    this.mRemote.transact(199, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized Bundle getActivityOptions(IBinder token) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(200, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.createFromParcel(_reply);
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
            public synchronized List<IBinder> getAppTasks(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(201, _data, _reply, 0);
                    _reply.readException();
                    List<IBinder> _result = _reply.createBinderArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void startSystemLockTaskMode(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(202, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void stopSystemLockTaskMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(203, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void finishVoiceTask(IVoiceInteractionSession session) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    this.mRemote.transact(204, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isTopOfTask(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(205, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void notifyLaunchTaskBehindComplete(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(206, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void notifyEnterAnimationComplete(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(207, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int startActivityAsCaller(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, boolean ignoreTargetSecurity, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callingPackage);
                        if (intent != null) {
                            _data.writeInt(1);
                            intent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeString(resolvedType);
                        } catch (Throwable th) {
                            th = th;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    _data.writeStrongBinder(resultTo);
                    try {
                        _data.writeString(resultWho);
                        try {
                            _data.writeInt(requestCode);
                            try {
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
                            } catch (Throwable th4) {
                                th = th4;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(ignoreTargetSecurity ? 1 : 0);
                        try {
                            _data.writeInt(userId);
                            try {
                                this.mRemote.transact(208, _data, _reply, 0);
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            } catch (Throwable th7) {
                                th = th7;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th8) {
                            th = th8;
                        }
                    } catch (Throwable th9) {
                        th = th9;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th10) {
                    th = th10;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int addAppTask(IBinder activityToken, Intent intent, ActivityManager.TaskDescription description, Bitmap thumbnail) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (description != null) {
                        _data.writeInt(1);
                        description.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (thumbnail != null) {
                        _data.writeInt(1);
                        thumbnail.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(209, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized Point getAppTaskThumbnailSize() throws RemoteException {
                Point _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(210, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Point.CREATOR.createFromParcel(_reply);
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
            public synchronized boolean releaseActivityInstance(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(211, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void releaseSomeActivities(IApplicationThread app) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app != null ? app.asBinder() : null);
                    this.mRemote.transact(212, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void bootAnimationComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(213, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized Bitmap getTaskDescriptionIcon(String filename, int userId) throws RemoteException {
                Bitmap _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(filename);
                    _data.writeInt(userId);
                    this.mRemote.transact(214, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bitmap.CREATOR.createFromParcel(_reply);
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
            public synchronized boolean launchAssistIntent(Intent intent, int requestType, String hint, int userHandle, Bundle args) throws RemoteException {
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
                    _data.writeInt(requestType);
                    _data.writeString(hint);
                    _data.writeInt(userHandle);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(215, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void startInPlaceAnimationOnFrontMostApplication(Bundle opts) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (opts != null) {
                        _data.writeInt(1);
                        opts.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(216, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int checkPermissionWithToken(String permission, int pid, int uid, IBinder callerToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(callerToken);
                    this.mRemote.transact(217, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(218, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(219, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void notifyCleartextNetwork(int uid, byte[] firstPacket) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeByteArray(firstPacket);
                    this.mRemote.transact(220, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int createStackOnDisplay(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(221, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(resizeableMode);
                    this.mRemote.transact(222, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean requestAssistContextExtras(int requestType, IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, boolean focused, boolean newSessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestType);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    if (receiverExtras != null) {
                        _data.writeInt(1);
                        receiverExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(activityToken);
                    _data.writeInt(focused ? 1 : 0);
                    _data.writeInt(newSessionId ? 1 : 0);
                    this.mRemote.transact(223, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
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
                    this.mRemote.transact(224, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getLockTaskModeState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(225, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setDumpHeapDebugLimit(String processName, int uid, long maxMemSize, String reportPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    _data.writeLong(maxMemSize);
                    _data.writeString(reportPackage);
                    this.mRemote.transact(226, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void dumpHeapFinished(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    this.mRemote.transact(227, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setVoiceKeepAwake(IVoiceInteractionSession session, boolean keepAwake) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(keepAwake ? 1 : 0);
                    this.mRemote.transact(228, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packages);
                    this.mRemote.transact(229, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void noteAlarmStart(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
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
                    this.mRemote.transact(230, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void noteAlarmFinish(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
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
                    this.mRemote.transact(231, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getPackageProcessState(String packageName, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(232, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void showLockTaskEscapeMessage(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(233, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void updateDeviceOwner(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(234, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void keyguardGoingAway(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    this.mRemote.transact(235, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int getUidProcessState(int uid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(236, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(237, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean showAssistFromActivity(IBinder token, Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(238, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isRootVoiceInteraction(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(239, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean startBinderTracking() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(240, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
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
                    this.mRemote.transact(241, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void positionTaskInStack(int taskId, int stackId, int position) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    _data.writeInt(position);
                    this.mRemote.transact(242, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void exitFreeformMode(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(243, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void reportSizeConfigurations(IBinder token, int[] horizontalSizeConfiguration, int[] verticalSizeConfigurations, int[] smallestWidthConfigurations) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeIntArray(horizontalSizeConfiguration);
                    _data.writeIntArray(verticalSizeConfigurations);
                    _data.writeIntArray(smallestWidthConfigurations);
                    this.mRemote.transact(244, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean setTaskWindowingModeSplitScreenPrimary(int taskId, int createMode, boolean toTop, boolean animate, Rect initialBounds, boolean showRecents) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(createMode);
                    _data.writeInt(toTop ? 1 : 0);
                    _data.writeInt(animate ? 1 : 0);
                    if (initialBounds != null) {
                        _data.writeInt(1);
                        initialBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(showRecents ? 1 : 0);
                    this.mRemote.transact(245, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void dismissSplitScreenMode(boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(toTop ? 1 : 0);
                    this.mRemote.transact(246, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void dismissPip(boolean animate, int animationDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(animate ? 1 : 0);
                    _data.writeInt(animationDuration);
                    this.mRemote.transact(247, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(suppress ? 1 : 0);
                    this.mRemote.transact(248, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void moveTasksToFullscreenStack(int fromStackId, boolean onTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fromStackId);
                    _data.writeInt(onTop ? 1 : 0);
                    this.mRemote.transact(249, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean moveTopActivityToPinnedStack(int stackId, Rect bounds) throws RemoteException {
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
                    this.mRemote.transact(250, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isAppStartModeDisabled(int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    this.mRemote.transact(251, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean unlockUser(int userid, byte[] token, byte[] secret, IProgressListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeByteArray(token);
                    _data.writeByteArray(secret);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(252, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isInMultiWindowMode(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(253, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isInPictureInPictureMode(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(254, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void killPackageDependents(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(255, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean enterPictureInPictureMode(IBinder token, PictureInPictureParams params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(256, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setPictureInPictureParams(IBinder token, PictureInPictureParams params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(257, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int getMaxNumPictureInPictureActions(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(258, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void activityRelaunched(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(259, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized IBinder getUriPermissionOwnerForActivity(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(260, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void resizeDockedStack(Rect dockedBounds, Rect tempDockedTaskBounds, Rect tempDockedTaskInsetBounds, Rect tempOtherTaskBounds, Rect tempOtherTaskInsetBounds) throws RemoteException {
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
                    this.mRemote.transact(261, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setSplitScreenResizing(boolean resizing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(resizing ? 1 : 0);
                    this.mRemote.transact(262, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int setVrMode(IBinder token, boolean enabled, ComponentName packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(enabled ? 1 : 0);
                    if (packageName != null) {
                        _data.writeInt(1);
                        packageName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(263, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized ParceledListSlice getGrantedUriPermissions(String packageName, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(264, _data, _reply, 0);
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
            public synchronized void clearGrantedUriPermissions(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(265, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected boolean isAppForeground(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(266, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void startLocalVoiceInteraction(IBinder token, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(267, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void stopLocalVoiceInteraction(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(268, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean supportsLocalVoiceInteraction() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(269, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void notifyPinnedStackAnimationStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(270, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void notifyPinnedStackAnimationEnded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(271, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void removeStack(int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    this.mRemote.transact(272, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void removeStacksInWindowingModes(int[] windowingModes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(windowingModes);
                    this.mRemote.transact(273, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void removeStacksWithActivityTypes(int[] activityTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(activityTypes);
                    this.mRemote.transact(274, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void makePackageIdle(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(275, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int getMemoryTrimLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(276, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void resizePinnedStack(Rect pinnedBounds, Rect tempPinnedTaskBounds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pinnedBounds != null) {
                        _data.writeInt(1);
                        pinnedBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (tempPinnedTaskBounds != null) {
                        _data.writeInt(1);
                        tempPinnedTaskBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(277, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isVrModePackageEnabled(ComponentName packageName) throws RemoteException {
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
                    this.mRemote.transact(278, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void notifyLockedProfile(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(279, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void startConfirmDeviceCredentialIntent(Intent intent, Bundle options) throws RemoteException {
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
                    this.mRemote.transact(280, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void sendIdleJobTrigger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(281, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int sendIntentSender(IIntentSender target, IBinder whitelistToken, int code, Intent intent, String resolvedType, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    _data.writeStrongBinder(whitelistToken);
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
                    this.mRemote.transact(282, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean isBackgroundRestricted(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(283, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setVrThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    this.mRemote.transact(284, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setRenderThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    this.mRemote.transact(285, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setHasTopUi(boolean hasTopUi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hasTopUi ? 1 : 0);
                    this.mRemote.transact(286, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean updateDisplayOverrideConfiguration(Configuration values, int displayId) throws RemoteException {
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
                    _data.writeInt(displayId);
                    this.mRemote.transact(287, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void moveStackToDisplay(int stackId, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    _data.writeInt(displayId);
                    this.mRemote.transact(288, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean requestAutofillData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    if (receiverExtras != null) {
                        _data.writeInt(1);
                        receiverExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(activityToken);
                    _data.writeInt(flags);
                    this.mRemote.transact(289, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void dismissKeyguard(IBinder token, IKeyguardDismissCallback callback, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(290, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int restartUserInBackground(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(291, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void cancelTaskWindowTransition(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(292, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized ActivityManager.TaskSnapshot getTaskSnapshot(int taskId, boolean reducedResolution) throws RemoteException {
                ActivityManager.TaskSnapshot _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(reducedResolution ? 1 : 0);
                    this.mRemote.transact(293, _data, _reply, 0);
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
            public synchronized void scheduleApplicationInfoChanged(List<String> packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    _data.writeInt(userId);
                    this.mRemote.transact(294, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setPersistentVrThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    this.mRemote.transact(295, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void waitForNetworkStateUpdate(long procStateSeq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(procStateSeq);
                    this.mRemote.transact(296, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setDisablePreviewScreenshots(IBinder token, boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(disable ? 1 : 0);
                    this.mRemote.transact(297, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized int getLastResumedActivityUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(298, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void backgroundWhitelistUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(299, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void updateLockTaskFeatures(int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    this.mRemote.transact(300, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setShowWhenLocked(IBinder token, boolean showWhenLocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(showWhenLocked ? 1 : 0);
                    this.mRemote.transact(301, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void setTurnScreenOn(IBinder token, boolean turnScreenOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(turnScreenOn ? 1 : 0);
                    this.mRemote.transact(302, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized boolean startUserInBackgroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeStrongBinder(unlockProgressListener != null ? unlockProgressListener.asBinder() : null);
                    this.mRemote.transact(303, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void registerRemoteAnimations(IBinder token, RemoteAnimationDefinition definition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (definition != null) {
                        _data.writeInt(1);
                        definition.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(304, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void registerRemoteAnimationForNextActivityStart(String packageName, RemoteAnimationAdapter adapter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (adapter != null) {
                        _data.writeInt(1);
                        adapter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(305, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityManager
            public synchronized void alwaysShowUnsupportedCompileSdkWarning(ComponentName activity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(306, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(307, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(308, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(309, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(310, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
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
                    this.mRemote.transact(311, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(312, _data, _reply, 0);
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
                    this.mRemote.transact(313, _data, _reply, 0);
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
                    this.mRemote.transact(314, _data, _reply, 0);
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
                    this.mRemote.transact(315, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(316, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(317, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(318, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        private synchronized boolean onTransact$startActivity$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg2;
            ProfilerInfo _arg8;
            Bundle _arg9;
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            String _arg1 = data.readString();
            if (data.readInt() != 0) {
                Intent _arg22 = Intent.CREATOR.createFromParcel(data);
                _arg2 = _arg22;
            } else {
                _arg2 = null;
            }
            String _arg3 = data.readString();
            IBinder _arg4 = data.readStrongBinder();
            String _arg5 = data.readString();
            int _arg6 = data.readInt();
            int _arg7 = data.readInt();
            if (data.readInt() != 0) {
                ProfilerInfo _arg82 = ProfilerInfo.CREATOR.createFromParcel(data);
                _arg8 = _arg82;
            } else {
                _arg8 = null;
            }
            if (data.readInt() != 0) {
                Bundle _arg92 = Bundle.CREATOR.createFromParcel(data);
                _arg9 = _arg92;
            } else {
                _arg9 = null;
            }
            int _result = startActivity(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$registerReceiver$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            String _arg1 = data.readString();
            IIntentReceiver _arg2 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
            IntentFilter _arg3 = data.readInt() != 0 ? IntentFilter.CREATOR.createFromParcel(data) : null;
            String _arg4 = data.readString();
            int _arg5 = data.readInt();
            int _arg6 = data.readInt();
            Intent _result = registerReceiver(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
            reply.writeNoException();
            if (_result == null) {
                reply.writeInt(0);
            } else {
                reply.writeInt(1);
                _result.writeToParcel(reply, 1);
            }
            return true;
        }

        private synchronized boolean onTransact$broadcastIntent$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg1;
            Bundle _arg6;
            Bundle _arg9;
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            if (data.readInt() != 0) {
                Intent _arg12 = Intent.CREATOR.createFromParcel(data);
                _arg1 = _arg12;
            } else {
                _arg1 = null;
            }
            String _arg2 = data.readString();
            IIntentReceiver _arg3 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
            int _arg4 = data.readInt();
            String _arg5 = data.readString();
            if (data.readInt() != 0) {
                Bundle _arg62 = Bundle.CREATOR.createFromParcel(data);
                _arg6 = _arg62;
            } else {
                _arg6 = null;
            }
            String[] _arg7 = data.createStringArray();
            int _arg8 = data.readInt();
            if (data.readInt() != 0) {
                Bundle _arg92 = Bundle.CREATOR.createFromParcel(data);
                _arg9 = _arg92;
            } else {
                _arg9 = null;
            }
            boolean _arg10 = data.readInt() != 0;
            boolean _arg11 = data.readInt() != 0;
            int _arg122 = data.readInt();
            int _result = broadcastIntent(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10, _arg11, _arg122);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$finishReceiver$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            IBinder _arg0 = data.readStrongBinder();
            int _arg1 = data.readInt();
            String _arg2 = data.readString();
            Bundle _arg3 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
            boolean _arg4 = data.readInt() != 0;
            int _arg5 = data.readInt();
            finishReceiver(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            return true;
        }

        private synchronized boolean onTransact$startService$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            Intent _arg1 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
            String _arg2 = data.readString();
            boolean _arg3 = data.readInt() != 0;
            String _arg4 = data.readString();
            int _arg5 = data.readInt();
            ComponentName _result = startService(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            if (_result != null) {
                reply.writeInt(1);
                _result.writeToParcel(reply, 1);
            } else {
                reply.writeInt(0);
            }
            return true;
        }

        private synchronized boolean onTransact$bindService$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            IBinder _arg1 = data.readStrongBinder();
            Intent _arg2 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
            String _arg3 = data.readString();
            IServiceConnection _arg4 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
            int _arg5 = data.readInt();
            String _arg6 = data.readString();
            int _arg7 = data.readInt();
            int _result = bindService(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$startInstrumentation$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                ComponentName _arg02 = ComponentName.CREATOR.createFromParcel(data);
                _arg0 = _arg02;
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            int _arg2 = data.readInt();
            Bundle _arg3 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
            IInstrumentationWatcher _arg4 = IInstrumentationWatcher.Stub.asInterface(data.readStrongBinder());
            IUiAutomationConnection _arg5 = IUiAutomationConnection.Stub.asInterface(data.readStrongBinder());
            int _arg6 = data.readInt();
            String _arg7 = data.readString();
            boolean startInstrumentation = startInstrumentation(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
            reply.writeNoException();
            reply.writeInt(startInstrumentation ? 1 : 0);
            return true;
        }

        private synchronized boolean onTransact$checkUriPermission$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            Uri _arg0 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
            int _arg1 = data.readInt();
            int _arg2 = data.readInt();
            int _arg3 = data.readInt();
            int _arg4 = data.readInt();
            IBinder _arg5 = data.readStrongBinder();
            int _result = checkUriPermission(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private boolean onTransact$grantUriPermission$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            String _arg1 = data.readString();
            Uri _arg2 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
            int _arg3 = data.readInt();
            int _arg4 = data.readInt();
            grantUriPermission(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$revokeUriPermission$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            String _arg1 = data.readString();
            Uri _arg2 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
            int _arg3 = data.readInt();
            int _arg4 = data.readInt();
            revokeUriPermission(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            return true;
        }

        private synchronized boolean onTransact$getIntentSender$(Parcel data, Parcel reply) throws RemoteException {
            Bundle _arg8;
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            String _arg1 = data.readString();
            IBinder _arg2 = data.readStrongBinder();
            String _arg3 = data.readString();
            int _arg4 = data.readInt();
            Intent[] _arg5 = (Intent[]) data.createTypedArray(Intent.CREATOR);
            String[] _arg6 = data.createStringArray();
            int _arg7 = data.readInt();
            if (data.readInt() != 0) {
                Bundle _arg82 = Bundle.CREATOR.createFromParcel(data);
                _arg8 = _arg82;
            } else {
                _arg8 = null;
            }
            int _arg9 = data.readInt();
            IIntentSender _result = getIntentSender(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9);
            reply.writeNoException();
            reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
            return true;
        }

        private boolean onTransact$noteWakeupAlarm$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            IIntentSender _arg0 = IIntentSender.Stub.asInterface(data.readStrongBinder());
            WorkSource _arg1 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
            int _arg2 = data.readInt();
            String _arg3 = data.readString();
            String _arg4 = data.readString();
            noteWakeupAlarm(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setServiceForeground$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                ComponentName _arg02 = ComponentName.CREATOR.createFromParcel(data);
                _arg0 = _arg02;
            } else {
                _arg0 = null;
            }
            IBinder _arg1 = data.readStrongBinder();
            int _arg2 = data.readInt();
            Notification _arg3 = data.readInt() != 0 ? Notification.CREATOR.createFromParcel(data) : null;
            int _arg4 = data.readInt();
            setServiceForeground(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            return true;
        }

        private synchronized boolean onTransact$profileControl$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            int _arg1 = data.readInt();
            boolean _arg2 = data.readInt() != 0;
            ProfilerInfo _arg3 = data.readInt() != 0 ? ProfilerInfo.CREATOR.createFromParcel(data) : null;
            int _arg4 = data.readInt();
            boolean profileControl = profileControl(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            reply.writeInt(profileControl ? 1 : 0);
            return true;
        }

        private synchronized boolean onTransact$handleIncomingUser$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            int _arg1 = data.readInt();
            int _arg2 = data.readInt();
            boolean _arg3 = data.readInt() != 0;
            boolean _arg4 = data.readInt() != 0;
            String _arg5 = data.readString();
            String _arg6 = data.readString();
            int _result = handleIncomingUser(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$startActivityIntentSender$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg3;
            Bundle _arg10;
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            IIntentSender _arg1 = IIntentSender.Stub.asInterface(data.readStrongBinder());
            IBinder _arg2 = data.readStrongBinder();
            if (data.readInt() != 0) {
                Intent _arg32 = Intent.CREATOR.createFromParcel(data);
                _arg3 = _arg32;
            } else {
                _arg3 = null;
            }
            String _arg4 = data.readString();
            IBinder _arg5 = data.readStrongBinder();
            String _arg6 = data.readString();
            int _arg7 = data.readInt();
            int _arg8 = data.readInt();
            int _arg9 = data.readInt();
            if (data.readInt() != 0) {
                Bundle _arg102 = Bundle.CREATOR.createFromParcel(data);
                _arg10 = _arg102;
            } else {
                _arg10 = null;
            }
            int _result = startActivityIntentSender(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$startActivityAndWait$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg2;
            ProfilerInfo _arg8;
            Bundle _arg9;
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            String _arg1 = data.readString();
            if (data.readInt() != 0) {
                Intent _arg22 = Intent.CREATOR.createFromParcel(data);
                _arg2 = _arg22;
            } else {
                _arg2 = null;
            }
            String _arg3 = data.readString();
            IBinder _arg4 = data.readStrongBinder();
            String _arg5 = data.readString();
            int _arg6 = data.readInt();
            int _arg7 = data.readInt();
            if (data.readInt() != 0) {
                ProfilerInfo _arg82 = ProfilerInfo.CREATOR.createFromParcel(data);
                _arg8 = _arg82;
            } else {
                _arg8 = null;
            }
            if (data.readInt() != 0) {
                Bundle _arg92 = Bundle.CREATOR.createFromParcel(data);
                _arg9 = _arg92;
            } else {
                _arg9 = null;
            }
            int _arg10 = data.readInt();
            WaitResult _result = startActivityAndWait(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10);
            reply.writeNoException();
            if (_result == null) {
                reply.writeInt(0);
            } else {
                reply.writeInt(1);
                _result.writeToParcel(reply, 1);
            }
            return true;
        }

        private synchronized boolean onTransact$startActivityWithConfig$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg2;
            Configuration _arg8;
            Bundle _arg9;
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            String _arg1 = data.readString();
            if (data.readInt() != 0) {
                Intent _arg22 = Intent.CREATOR.createFromParcel(data);
                _arg2 = _arg22;
            } else {
                _arg2 = null;
            }
            String _arg3 = data.readString();
            IBinder _arg4 = data.readStrongBinder();
            String _arg5 = data.readString();
            int _arg6 = data.readInt();
            int _arg7 = data.readInt();
            if (data.readInt() != 0) {
                Configuration _arg82 = Configuration.CREATOR.createFromParcel(data);
                _arg8 = _arg82;
            } else {
                _arg8 = null;
            }
            if (data.readInt() != 0) {
                Bundle _arg92 = Bundle.CREATOR.createFromParcel(data);
                _arg9 = _arg92;
            } else {
                _arg9 = null;
            }
            int _arg10 = data.readInt();
            int _result = startActivityWithConfig(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$crashApplication$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            int _arg1 = data.readInt();
            String _arg2 = data.readString();
            int _arg3 = data.readInt();
            String _arg4 = data.readString();
            crashApplication(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            return true;
        }

        private synchronized boolean onTransact$grantUriPermissionFromOwner$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            IBinder _arg0 = data.readStrongBinder();
            int _arg1 = data.readInt();
            String _arg2 = data.readString();
            Uri _arg3 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
            int _arg4 = data.readInt();
            int _arg5 = data.readInt();
            int _arg6 = data.readInt();
            grantUriPermissionFromOwner(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
            reply.writeNoException();
            return true;
        }

        private synchronized boolean onTransact$checkGrantUriPermission$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            String _arg1 = data.readString();
            Uri _arg2 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
            int _arg3 = data.readInt();
            int _arg4 = data.readInt();
            int _result = checkGrantUriPermission(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$dumpHeap$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            int _arg1 = data.readInt();
            boolean _arg2 = data.readInt() != 0;
            boolean _arg3 = data.readInt() != 0;
            boolean _arg4 = data.readInt() != 0;
            String _arg5 = data.readString();
            ParcelFileDescriptor _arg6 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
            boolean dumpHeap = dumpHeap(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
            reply.writeNoException();
            reply.writeInt(dumpHeap ? 1 : 0);
            return true;
        }

        private synchronized boolean onTransact$startActivities$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            String _arg1 = data.readString();
            Intent[] _arg2 = (Intent[]) data.createTypedArray(Intent.CREATOR);
            String[] _arg3 = data.createStringArray();
            IBinder _arg4 = data.readStrongBinder();
            Bundle _arg5 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
            int _arg6 = data.readInt();
            int _result = startActivities(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$startActivityAsUser$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg2;
            ProfilerInfo _arg8;
            Bundle _arg9;
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            String _arg1 = data.readString();
            if (data.readInt() != 0) {
                Intent _arg22 = Intent.CREATOR.createFromParcel(data);
                _arg2 = _arg22;
            } else {
                _arg2 = null;
            }
            String _arg3 = data.readString();
            IBinder _arg4 = data.readStrongBinder();
            String _arg5 = data.readString();
            int _arg6 = data.readInt();
            int _arg7 = data.readInt();
            if (data.readInt() != 0) {
                ProfilerInfo _arg82 = ProfilerInfo.CREATOR.createFromParcel(data);
                _arg8 = _arg82;
            } else {
                _arg8 = null;
            }
            if (data.readInt() != 0) {
                Bundle _arg92 = Bundle.CREATOR.createFromParcel(data);
                _arg9 = _arg92;
            } else {
                _arg9 = null;
            }
            int _arg10 = data.readInt();
            int _result = startActivityAsUser(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$reportAssistContextExtras$(Parcel data, Parcel reply) throws RemoteException {
            Bundle _arg1;
            AssistStructure _arg2;
            AssistContent _arg3;
            Uri _arg4;
            data.enforceInterface(DESCRIPTOR);
            IBinder _arg0 = data.readStrongBinder();
            if (data.readInt() != 0) {
                Bundle _arg12 = Bundle.CREATOR.createFromParcel(data);
                _arg1 = _arg12;
            } else {
                _arg1 = null;
            }
            if (data.readInt() != 0) {
                AssistStructure _arg22 = AssistStructure.CREATOR.createFromParcel(data);
                _arg2 = _arg22;
            } else {
                _arg2 = null;
            }
            if (data.readInt() != 0) {
                AssistContent _arg32 = AssistContent.CREATOR.createFromParcel(data);
                _arg3 = _arg32;
            } else {
                _arg3 = null;
            }
            if (data.readInt() != 0) {
                Uri _arg42 = Uri.CREATOR.createFromParcel(data);
                _arg4 = _arg42;
            } else {
                _arg4 = null;
            }
            reportAssistContextExtras(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            return true;
        }

        private synchronized boolean onTransact$resizeStack$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            Rect _arg1 = data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null;
            boolean _arg2 = data.readInt() != 0;
            boolean _arg3 = data.readInt() != 0;
            boolean _arg4 = data.readInt() != 0;
            int _arg5 = data.readInt();
            resizeStack(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$releasePersistableUriPermission$(Parcel data, Parcel reply) throws RemoteException {
            Uri _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = Uri.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            int _arg1 = data.readInt();
            String _arg2 = data.readString();
            int _arg3 = data.readInt();
            releasePersistableUriPermission(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        private synchronized boolean onTransact$startVoiceActivity$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg3;
            ProfilerInfo _arg8;
            Bundle _arg9;
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            int _arg1 = data.readInt();
            int _arg2 = data.readInt();
            if (data.readInt() != 0) {
                Intent _arg32 = Intent.CREATOR.createFromParcel(data);
                _arg3 = _arg32;
            } else {
                _arg3 = null;
            }
            String _arg4 = data.readString();
            IVoiceInteractionSession _arg5 = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
            IVoiceInteractor _arg6 = IVoiceInteractor.Stub.asInterface(data.readStrongBinder());
            int _arg7 = data.readInt();
            if (data.readInt() != 0) {
                ProfilerInfo _arg82 = ProfilerInfo.CREATOR.createFromParcel(data);
                _arg8 = _arg82;
            } else {
                _arg8 = null;
            }
            if (data.readInt() != 0) {
                Bundle _arg92 = Bundle.CREATOR.createFromParcel(data);
                _arg9 = _arg92;
            } else {
                _arg9 = null;
            }
            int _arg10 = data.readInt();
            int _result = startVoiceActivity(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$startAssistantActivity$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg3;
            Bundle _arg5;
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            int _arg1 = data.readInt();
            int _arg2 = data.readInt();
            if (data.readInt() != 0) {
                Intent _arg32 = Intent.CREATOR.createFromParcel(data);
                _arg3 = _arg32;
            } else {
                _arg3 = null;
            }
            String _arg4 = data.readString();
            if (data.readInt() != 0) {
                Bundle _arg52 = Bundle.CREATOR.createFromParcel(data);
                _arg5 = _arg52;
            } else {
                _arg5 = null;
            }
            int _arg6 = data.readInt();
            int _result = startAssistantActivity(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$startActivityAsCaller$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg2;
            ProfilerInfo _arg8;
            Bundle _arg9;
            data.enforceInterface(DESCRIPTOR);
            IApplicationThread _arg0 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            String _arg1 = data.readString();
            if (data.readInt() != 0) {
                Intent _arg22 = Intent.CREATOR.createFromParcel(data);
                _arg2 = _arg22;
            } else {
                _arg2 = null;
            }
            String _arg3 = data.readString();
            IBinder _arg4 = data.readStrongBinder();
            String _arg5 = data.readString();
            int _arg6 = data.readInt();
            int _arg7 = data.readInt();
            if (data.readInt() != 0) {
                ProfilerInfo _arg82 = ProfilerInfo.CREATOR.createFromParcel(data);
                _arg8 = _arg82;
            } else {
                _arg8 = null;
            }
            if (data.readInt() != 0) {
                Bundle _arg92 = Bundle.CREATOR.createFromParcel(data);
                _arg9 = _arg92;
            } else {
                _arg9 = null;
            }
            boolean _arg10 = data.readInt() != 0;
            int _arg11 = data.readInt();
            int _result = startActivityAsCaller(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10, _arg11);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private boolean onTransact$addAppTask$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg1;
            ActivityManager.TaskDescription _arg2;
            data.enforceInterface(DESCRIPTOR);
            IBinder _arg0 = data.readStrongBinder();
            if (data.readInt() != 0) {
                _arg1 = Intent.CREATOR.createFromParcel(data);
            } else {
                _arg1 = null;
            }
            if (data.readInt() != 0) {
                _arg2 = ActivityManager.TaskDescription.CREATOR.createFromParcel(data);
            } else {
                _arg2 = null;
            }
            Bitmap _arg3 = data.readInt() != 0 ? Bitmap.CREATOR.createFromParcel(data) : null;
            int _result = addAppTask(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$launchAssistIntent$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                Intent _arg02 = Intent.CREATOR.createFromParcel(data);
                _arg0 = _arg02;
            } else {
                _arg0 = null;
            }
            int _arg1 = data.readInt();
            String _arg2 = data.readString();
            int _arg3 = data.readInt();
            Bundle _arg4 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
            boolean launchAssistIntent = launchAssistIntent(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            reply.writeInt(launchAssistIntent ? 1 : 0);
            return true;
        }

        private boolean onTransact$checkPermissionWithToken$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            int _arg1 = data.readInt();
            int _arg2 = data.readInt();
            IBinder _arg3 = data.readStrongBinder();
            int _result = checkPermissionWithToken(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private synchronized boolean onTransact$requestAssistContextExtras$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            IAssistDataReceiver _arg1 = IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
            Bundle _arg2 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
            IBinder _arg3 = data.readStrongBinder();
            boolean _arg4 = data.readInt() != 0;
            boolean _arg5 = data.readInt() != 0;
            boolean requestAssistContextExtras = requestAssistContextExtras(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            reply.writeInt(requestAssistContextExtras ? 1 : 0);
            return true;
        }

        private boolean onTransact$setDumpHeapDebugLimit$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            int _arg1 = data.readInt();
            long _arg2 = data.readLong();
            String _arg3 = data.readString();
            setDumpHeapDebugLimit(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$noteAlarmStart$(Parcel data, Parcel reply) throws RemoteException {
            WorkSource _arg1;
            data.enforceInterface(DESCRIPTOR);
            IIntentSender _arg0 = IIntentSender.Stub.asInterface(data.readStrongBinder());
            if (data.readInt() != 0) {
                _arg1 = WorkSource.CREATOR.createFromParcel(data);
            } else {
                _arg1 = null;
            }
            int _arg2 = data.readInt();
            String _arg3 = data.readString();
            noteAlarmStart(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$noteAlarmFinish$(Parcel data, Parcel reply) throws RemoteException {
            WorkSource _arg1;
            data.enforceInterface(DESCRIPTOR);
            IIntentSender _arg0 = IIntentSender.Stub.asInterface(data.readStrongBinder());
            if (data.readInt() != 0) {
                _arg1 = WorkSource.CREATOR.createFromParcel(data);
            } else {
                _arg1 = null;
            }
            int _arg2 = data.readInt();
            String _arg3 = data.readString();
            noteAlarmFinish(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$reportSizeConfigurations$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            IBinder _arg0 = data.readStrongBinder();
            int[] _arg1 = data.createIntArray();
            int[] _arg2 = data.createIntArray();
            int[] _arg3 = data.createIntArray();
            reportSizeConfigurations(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        private synchronized boolean onTransact$setTaskWindowingModeSplitScreenPrimary$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            int _arg1 = data.readInt();
            boolean _arg2 = data.readInt() != 0;
            boolean _arg3 = data.readInt() != 0;
            Rect _arg4 = data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null;
            boolean _arg5 = data.readInt() != 0;
            boolean taskWindowingModeSplitScreenPrimary = setTaskWindowingModeSplitScreenPrimary(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            reply.writeInt(taskWindowingModeSplitScreenPrimary ? 1 : 0);
            return true;
        }

        private boolean onTransact$unlockUser$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            byte[] _arg1 = data.createByteArray();
            byte[] _arg2 = data.createByteArray();
            IProgressListener _arg3 = IProgressListener.Stub.asInterface(data.readStrongBinder());
            boolean unlockUser = unlockUser(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            reply.writeInt(unlockUser ? 1 : 0);
            return true;
        }

        private synchronized boolean onTransact$resizeDockedStack$(Parcel data, Parcel reply) throws RemoteException {
            Rect _arg0;
            Rect _arg1;
            Rect _arg2;
            Rect _arg3;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                Rect _arg02 = Rect.CREATOR.createFromParcel(data);
                _arg0 = _arg02;
            } else {
                _arg0 = null;
            }
            if (data.readInt() != 0) {
                Rect _arg12 = Rect.CREATOR.createFromParcel(data);
                _arg1 = _arg12;
            } else {
                _arg1 = null;
            }
            if (data.readInt() != 0) {
                Rect _arg22 = Rect.CREATOR.createFromParcel(data);
                _arg2 = _arg22;
            } else {
                _arg2 = null;
            }
            if (data.readInt() != 0) {
                Rect _arg32 = Rect.CREATOR.createFromParcel(data);
                _arg3 = _arg32;
            } else {
                _arg3 = null;
            }
            Rect _arg4 = data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null;
            resizeDockedStack(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            return true;
        }

        private synchronized boolean onTransact$sendIntentSender$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg3;
            Bundle _arg7;
            data.enforceInterface(DESCRIPTOR);
            IIntentSender _arg0 = IIntentSender.Stub.asInterface(data.readStrongBinder());
            IBinder _arg1 = data.readStrongBinder();
            int _arg2 = data.readInt();
            if (data.readInt() != 0) {
                Intent _arg32 = Intent.CREATOR.createFromParcel(data);
                _arg3 = _arg32;
            } else {
                _arg3 = null;
            }
            String _arg4 = data.readString();
            IIntentReceiver _arg5 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
            String _arg6 = data.readString();
            if (data.readInt() != 0) {
                Bundle _arg72 = Bundle.CREATOR.createFromParcel(data);
                _arg7 = _arg72;
            } else {
                _arg7 = null;
            }
            int _result = sendIntentSender(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private boolean onTransact$requestAutofillData$(Parcel data, Parcel reply) throws RemoteException {
            Bundle _arg1;
            data.enforceInterface(DESCRIPTOR);
            IAssistDataReceiver _arg0 = IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
            if (data.readInt() != 0) {
                _arg1 = Bundle.CREATOR.createFromParcel(data);
            } else {
                _arg1 = null;
            }
            IBinder _arg2 = data.readStrongBinder();
            int _arg3 = data.readInt();
            boolean requestAutofillData = requestAutofillData(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            reply.writeInt(requestAutofillData ? 1 : 0);
            return true;
        }
    }
}
