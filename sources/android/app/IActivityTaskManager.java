package android.app;

import android.app.ActivityManager;
import android.app.IActivityController;
import android.app.IApplicationThread;
import android.app.IAssistDataReceiver;
import android.app.IRequestFinishCallback;
import android.app.ITaskStackListener;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.ParceledListSlice;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.service.voice.IVoiceInteractionSession;
import android.text.TextUtils;
import android.view.IRecentsAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationDefinition;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.policy.IKeyguardDismissCallback;
import java.util.List;

/* loaded from: classes.dex */
public interface IActivityTaskManager extends IInterface {
    void activityDestroyed(IBinder iBinder) throws RemoteException;

    void activityIdle(IBinder iBinder, Configuration configuration, boolean z) throws RemoteException;

    void activityPaused(IBinder iBinder) throws RemoteException;

    void activityRelaunched(IBinder iBinder) throws RemoteException;

    void activityResumed(IBinder iBinder) throws RemoteException;

    void activitySlept(IBinder iBinder) throws RemoteException;

    void activityStopped(IBinder iBinder, Bundle bundle, PersistableBundle persistableBundle, CharSequence charSequence) throws RemoteException;

    void activityTopResumedStateLost() throws RemoteException;

    int addAppTask(IBinder iBinder, Intent intent, ActivityManager.TaskDescription taskDescription, Bitmap bitmap) throws RemoteException;

    void alwaysShowUnsupportedCompileSdkWarning(ComponentName componentName) throws RemoteException;

    void cancelRecentsAnimation(boolean z) throws RemoteException;

    void cancelTaskWindowTransition(int i) throws RemoteException;

    void clearLaunchParamsForPackages(List<String> list) throws RemoteException;

    boolean convertFromTranslucent(IBinder iBinder) throws RemoteException;

    boolean convertToTranslucent(IBinder iBinder, Bundle bundle) throws RemoteException;

    void dismissKeyguard(IBinder iBinder, IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) throws RemoteException;

    void dismissPip(boolean z, int i) throws RemoteException;

    void dismissSplitScreenMode(boolean z) throws RemoteException;

    boolean enterPictureInPictureMode(IBinder iBinder, PictureInPictureParams pictureInPictureParams) throws RemoteException;

    boolean finishActivity(IBinder iBinder, int i, Intent intent, int i2) throws RemoteException;

    boolean finishActivityAffinity(IBinder iBinder) throws RemoteException;

    void finishPackageActivity(String str) throws RemoteException;

    void finishSubActivity(IBinder iBinder, String str, int i) throws RemoteException;

    void finishTaskActivity(int i) throws RemoteException;

    void finishVoiceTask(IVoiceInteractionSession iVoiceInteractionSession) throws RemoteException;

    ComponentName getActivityClassForToken(IBinder iBinder) throws RemoteException;

    int getActivityDisplayId(IBinder iBinder) throws RemoteException;

    Bundle getActivityOptions(IBinder iBinder) throws RemoteException;

    List<ActivityManager.StackInfo> getAllStackInfos() throws RemoteException;

    Point getAppTaskThumbnailSize() throws RemoteException;

    List<IBinder> getAppTasks(String str) throws RemoteException;

    Bundle getAssistContextExtras(int i) throws RemoteException;

    ComponentName getCallingActivity(IBinder iBinder) throws RemoteException;

    String getCallingPackage(IBinder iBinder) throws RemoteException;

    ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException;

    List<ActivityManager.RunningTaskInfo> getFilteredTasks(int i, int i2, int i3) throws RemoteException;

    ActivityManager.StackInfo getFocusedStackInfo() throws RemoteException;

    int getFrontActivityScreenCompatMode() throws RemoteException;

    int getLastResumedActivityUserId() throws RemoteException;

    String getLaunchedFromPackage(IBinder iBinder) throws RemoteException;

    int getLaunchedFromUid(IBinder iBinder) throws RemoteException;

    int getLockTaskModeState() throws RemoteException;

    int getMaxNumPictureInPictureActions(IBinder iBinder) throws RemoteException;

    boolean getPackageAskScreenCompat(String str) throws RemoteException;

    String getPackageForToken(IBinder iBinder) throws RemoteException;

    int getPackageScreenCompatMode(String str) throws RemoteException;

    ParceledListSlice getRecentTasks(int i, int i2, int i3) throws RemoteException;

    int getRequestedOrientation(IBinder iBinder) throws RemoteException;

    ActivityManager.StackInfo getStackInfo(int i, int i2) throws RemoteException;

    Rect getTaskBounds(int i) throws RemoteException;

    ActivityManager.TaskDescription getTaskDescription(int i) throws RemoteException;

    Bitmap getTaskDescriptionIcon(String str, int i) throws RemoteException;

    int getTaskForActivity(IBinder iBinder, boolean z) throws RemoteException;

    ActivityManager.TaskSnapshot getTaskSnapshot(int i, boolean z) throws RemoteException;

    List<ActivityManager.RunningTaskInfo> getTasks(int i) throws RemoteException;

    IBinder getUriPermissionOwnerForActivity(IBinder iBinder) throws RemoteException;

    boolean isActivityStartAllowedOnDisplay(int i, Intent intent, String str, int i2) throws RemoteException;

    boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException;

    boolean isImmersive(IBinder iBinder) throws RemoteException;

    boolean isInLockTaskMode() throws RemoteException;

    boolean isInMultiWindowMode(IBinder iBinder) throws RemoteException;

    boolean isInPictureInPictureMode(IBinder iBinder) throws RemoteException;

    boolean isRootVoiceInteraction(IBinder iBinder) throws RemoteException;

    boolean isTopActivityImmersive() throws RemoteException;

    boolean isTopOfTask(IBinder iBinder) throws RemoteException;

    void keyguardGoingAway(int i) throws RemoteException;

    boolean launchAssistIntent(Intent intent, int i, String str, int i2, Bundle bundle) throws RemoteException;

    boolean moveActivityTaskToBack(IBinder iBinder, boolean z) throws RemoteException;

    void moveStackToDisplay(int i, int i2) throws RemoteException;

    void moveTaskToFront(IApplicationThread iApplicationThread, String str, int i, int i2, Bundle bundle) throws RemoteException;

    void moveTaskToStack(int i, int i2, boolean z) throws RemoteException;

    void moveTasksToFullscreenStack(int i, boolean z) throws RemoteException;

    boolean moveTopActivityToPinnedStack(int i, Rect rect) throws RemoteException;

    boolean navigateUpTo(IBinder iBinder, Intent intent, int i, Intent intent2) throws RemoteException;

    void notifyActivityDrawn(IBinder iBinder) throws RemoteException;

    void notifyEnterAnimationComplete(IBinder iBinder) throws RemoteException;

    void notifyLaunchTaskBehindComplete(IBinder iBinder) throws RemoteException;

    void notifyPinnedStackAnimationEnded() throws RemoteException;

    void notifyPinnedStackAnimationStarted() throws RemoteException;

    void offsetPinnedStackBounds(int i, Rect rect, int i2, int i3, int i4) throws RemoteException;

    void onBackPressedOnTaskRoot(IBinder iBinder, IRequestFinishCallback iRequestFinishCallback) throws RemoteException;

    void overridePendingTransition(IBinder iBinder, String str, int i, int i2) throws RemoteException;

    void performActivityChanged(Bundle bundle) throws RemoteException;

    void performTopActivityChanged(Bundle bundle) throws RemoteException;

    void positionTaskInStack(int i, int i2, int i3) throws RemoteException;

    void registerRemoteAnimationForNextActivityStart(String str, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException;

    void registerRemoteAnimations(IBinder iBinder, RemoteAnimationDefinition remoteAnimationDefinition) throws RemoteException;

    void registerRemoteAnimationsForDisplay(int i, RemoteAnimationDefinition remoteAnimationDefinition) throws RemoteException;

    void registerTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    boolean releaseActivityInstance(IBinder iBinder) throws RemoteException;

    void releaseSomeActivities(IApplicationThread iApplicationThread) throws RemoteException;

    void removeAllVisibleRecentTasks() throws RemoteException;

    void removeStack(int i) throws RemoteException;

    void removeStacksInWindowingModes(int[] iArr) throws RemoteException;

    void removeStacksWithActivityTypes(int[] iArr) throws RemoteException;

    boolean removeTask(int i) throws RemoteException;

    void reportActivityFullyDrawn(IBinder iBinder, boolean z) throws RemoteException;

    void reportAssistContextExtras(IBinder iBinder, Bundle bundle, AssistStructure assistStructure, AssistContent assistContent, Uri uri) throws RemoteException;

    void reportSizeConfigurations(IBinder iBinder, int[] iArr, int[] iArr2, int[] iArr3) throws RemoteException;

    boolean requestAssistContextExtras(int i, IAssistDataReceiver iAssistDataReceiver, Bundle bundle, IBinder iBinder, boolean z, boolean z2) throws RemoteException;

    boolean requestAutofillData(IAssistDataReceiver iAssistDataReceiver, Bundle bundle, IBinder iBinder, int i) throws RemoteException;

    IBinder requestStartActivityPermissionToken(IBinder iBinder) throws RemoteException;

    void resizeDockedStack(Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5) throws RemoteException;

    void resizePinnedStack(Rect rect, Rect rect2) throws RemoteException;

    void resizeStack(int i, Rect rect, boolean z, boolean z2, boolean z3, int i2) throws RemoteException;

    void resizeTask(int i, Rect rect, int i2) throws RemoteException;

    void restartActivityProcessIfVisible(IBinder iBinder) throws RemoteException;

    void resumeAppSwitches() throws RemoteException;

    void setActivityController(IActivityController iActivityController, boolean z) throws RemoteException;

    void setDisablePreviewScreenshots(IBinder iBinder, boolean z) throws RemoteException;

    void setDisplayToSingleTaskInstance(int i) throws RemoteException;

    void setFocusedStack(int i) throws RemoteException;

    void setFocusedTask(int i) throws RemoteException;

    void setFrontActivityScreenCompatMode(int i) throws RemoteException;

    void setImmersive(IBinder iBinder, boolean z) throws RemoteException;

    void setInheritShowWhenLocked(IBinder iBinder, boolean z) throws RemoteException;

    void setLockScreenShown(boolean z, boolean z2) throws RemoteException;

    void setPackageAskScreenCompat(String str, boolean z) throws RemoteException;

    void setPackageScreenCompatMode(String str, int i) throws RemoteException;

    void setPersistentVrThread(int i) throws RemoteException;

    void setPictureInPictureParams(IBinder iBinder, PictureInPictureParams pictureInPictureParams) throws RemoteException;

    void setRequestedOrientation(IBinder iBinder, int i) throws RemoteException;

    void setShowWhenLocked(IBinder iBinder, boolean z) throws RemoteException;

    void setSplitScreenResizing(boolean z) throws RemoteException;

    void setTaskDescription(IBinder iBinder, ActivityManager.TaskDescription taskDescription) throws RemoteException;

    void setTaskResizeable(int i, int i2) throws RemoteException;

    void setTaskWindowingMode(int i, int i2, boolean z) throws RemoteException;

    boolean setTaskWindowingModeSplitScreenPrimary(int i, int i2, boolean z, boolean z2, Rect rect, boolean z3) throws RemoteException;

    void setTurnScreenOn(IBinder iBinder, boolean z) throws RemoteException;

    void setVoiceKeepAwake(IVoiceInteractionSession iVoiceInteractionSession, boolean z) throws RemoteException;

    int setVrMode(IBinder iBinder, boolean z, ComponentName componentName) throws RemoteException;

    void setVrThread(int i) throws RemoteException;

    boolean shouldUpRecreateTask(IBinder iBinder, String str) throws RemoteException;

    boolean showAssistFromActivity(IBinder iBinder, Bundle bundle) throws RemoteException;

    void showLockTaskEscapeMessage(IBinder iBinder) throws RemoteException;

    int startActivities(IApplicationThread iApplicationThread, String str, Intent[] intentArr, String[] strArr, IBinder iBinder, Bundle bundle, int i) throws RemoteException;

    int startActivity(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException;

    WaitResult startActivityAndWait(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    int startActivityAsCaller(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, IBinder iBinder2, boolean z, int i3) throws RemoteException;

    int startActivityAsUser(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    int startActivityFromRecents(int i, Bundle bundle) throws RemoteException;

    int startActivityIntentSender(IApplicationThread iApplicationThread, IIntentSender iIntentSender, IBinder iBinder, Intent intent, String str, IBinder iBinder2, String str2, int i, int i2, int i3, Bundle bundle) throws RemoteException;

    int startActivityWithConfig(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, Configuration configuration, Bundle bundle, int i3) throws RemoteException;

    int startAssistantActivity(String str, int i, int i2, Intent intent, String str2, Bundle bundle, int i3) throws RemoteException;

    void startInPlaceAnimationOnFrontMostApplication(Bundle bundle) throws RemoteException;

    void startLocalVoiceInteraction(IBinder iBinder, Bundle bundle) throws RemoteException;

    void startLockTaskModeByToken(IBinder iBinder) throws RemoteException;

    boolean startNextMatchingActivity(IBinder iBinder, Intent intent, Bundle bundle) throws RemoteException;

    void startRecentsActivity(Intent intent, IAssistDataReceiver iAssistDataReceiver, IRecentsAnimationRunner iRecentsAnimationRunner) throws RemoteException;

    void startSystemLockTaskMode(int i) throws RemoteException;

    int startVoiceActivity(String str, int i, int i2, Intent intent, String str2, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, int i3, ProfilerInfo profilerInfo, Bundle bundle, int i4) throws RemoteException;

    void stopAppSwitches() throws RemoteException;

    void stopLocalVoiceInteraction(IBinder iBinder) throws RemoteException;

    void stopLockTaskModeByToken(IBinder iBinder) throws RemoteException;

    void stopSystemLockTaskMode() throws RemoteException;

    boolean supportsLocalVoiceInteraction() throws RemoteException;

    void suppressResizeConfigChanges(boolean z) throws RemoteException;

    void toggleFreeformWindowingMode(IBinder iBinder) throws RemoteException;

    void unhandledBack() throws RemoteException;

    void unregisterTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    boolean updateConfiguration(Configuration configuration) throws RemoteException;

    boolean updateDisplayOverrideConfiguration(Configuration configuration, int i) throws RemoteException;

    void updateLockTaskFeatures(int i, int i2) throws RemoteException;

    void updateLockTaskPackages(int i, String[] strArr) throws RemoteException;

    boolean willActivityBeVisible(IBinder iBinder) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IActivityTaskManager {
        @Override // android.app.IActivityTaskManager
        public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public int startActivities(IApplicationThread caller, String callingPackage, Intent[] intents, String[] resolvedTypes, IBinder resultTo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public int startActivityAsUser(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public boolean startNextMatchingActivity(IBinder callingActivity, Intent intent, Bundle options) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public int startActivityIntentSender(IApplicationThread caller, IIntentSender target, IBinder whitelistToken, Intent fillInIntent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flagsMask, int flagsValues, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public WaitResult startActivityAndWait(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public int startActivityWithConfig(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, Configuration newConfig, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public int startVoiceActivity(String callingPackage, int callingPid, int callingUid, Intent intent, String resolvedType, IVoiceInteractionSession session, IVoiceInteractor interactor, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public int startAssistantActivity(String callingPackage, int callingPid, int callingUid, Intent intent, String resolvedType, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public void startRecentsActivity(Intent intent, IAssistDataReceiver assistDataReceiver, IRecentsAnimationRunner recentsAnimationRunner) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public int startActivityAsCaller(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, IBinder permissionToken, boolean ignoreTargetSecurity, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public boolean isActivityStartAllowedOnDisplay(int displayId, Intent intent, String resolvedType, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void unhandledBack() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean finishActivity(IBinder token, int code, Intent data, int finishTask) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean finishActivityAffinity(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void activityIdle(IBinder token, Configuration config, boolean stopProfiling) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void activityResumed(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void activityTopResumedStateLost() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void activityPaused(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void activityStopped(IBinder token, Bundle state, PersistableBundle persistentState, CharSequence description) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void activityDestroyed(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void activityRelaunched(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void activitySlept(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int getFrontActivityScreenCompatMode() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public void setFrontActivityScreenCompatMode(int mode) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public String getCallingPackage(IBinder token) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public ComponentName getCallingActivity(IBinder token) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void setFocusedTask(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean removeTask(int taskId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void removeAllVisibleRecentTasks() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public List<ActivityManager.RunningTaskInfo> getFilteredTasks(int maxNum, int ignoreActivityType, int ignoreWindowingMode) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public boolean shouldUpRecreateTask(IBinder token, String destAffinity) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean navigateUpTo(IBinder token, Intent target, int resultCode, Intent resultData) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void moveTaskToFront(IApplicationThread app, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public void finishSubActivity(IBinder token, String resultWho, int requestCode) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public ParceledListSlice getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public boolean willActivityBeVisible(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int getRequestedOrientation(IBinder token) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public boolean convertFromTranslucent(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean convertToTranslucent(IBinder token, Bundle options) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void notifyActivityDrawn(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void reportActivityFullyDrawn(IBinder token, boolean restoredFromBundle) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int getActivityDisplayId(IBinder activityToken) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public boolean isImmersive(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void setImmersive(IBinder token, boolean immersive) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean isTopActivityImmersive() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public ActivityManager.TaskDescription getTaskDescription(int taskId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void overridePendingTransition(IBinder token, String packageName, int enterAnim, int exitAnim) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void reportAssistContextExtras(IBinder token, Bundle extras, AssistStructure structure, AssistContent content, Uri referrer) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setFocusedStack(int stackId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public ActivityManager.StackInfo getFocusedStackInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public Rect getTaskBounds(int taskId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void cancelRecentsAnimation(boolean restoreHomeStackPosition) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void startLockTaskModeByToken(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void stopLockTaskModeByToken(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean isInLockTaskMode() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public int getLockTaskModeState() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public void setTaskDescription(IBinder token, ActivityManager.TaskDescription values) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public Bundle getActivityOptions(IBinder token) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public List<IBinder> getAppTasks(String callingPackage) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void startSystemLockTaskMode(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void stopSystemLockTaskMode() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void finishVoiceTask(IVoiceInteractionSession session) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean isTopOfTask(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void notifyLaunchTaskBehindComplete(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void notifyEnterAnimationComplete(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int addAppTask(IBinder activityToken, Intent intent, ActivityManager.TaskDescription description, Bitmap thumbnail) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public Point getAppTaskThumbnailSize() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public boolean releaseActivityInstance(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public IBinder requestStartActivityPermissionToken(IBinder delegatorToken) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void releaseSomeActivities(IApplicationThread app) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public Bitmap getTaskDescriptionIcon(String filename, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void startInPlaceAnimationOnFrontMostApplication(Bundle opts) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void toggleFreeformWindowingMode(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void moveStackToDisplay(int stackId, int displayId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void removeStack(int stackId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setTaskWindowingMode(int taskId, int windowingMode, boolean toTop) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void moveTaskToStack(int taskId, int stackId, boolean toTop) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void resizeStack(int stackId, Rect bounds, boolean allowResizeInDockedMode, boolean preserveWindows, boolean animate, int animationDuration) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean setTaskWindowingModeSplitScreenPrimary(int taskId, int createMode, boolean toTop, boolean animate, Rect initialBounds, boolean showRecents) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void offsetPinnedStackBounds(int stackId, Rect compareBounds, int xOffset, int yOffset, int animationDuration) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void removeStacksInWindowingModes(int[] windowingModes) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void removeStacksWithActivityTypes(int[] activityTypes) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public List<ActivityManager.StackInfo> getAllStackInfos() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public ActivityManager.StackInfo getStackInfo(int windowingMode, int activityType) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void setLockScreenShown(boolean showingKeyguard, boolean showingAod) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public Bundle getAssistContextExtras(int requestType) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public boolean launchAssistIntent(Intent intent, int requestType, String hint, int userHandle, Bundle args) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean requestAssistContextExtras(int requestType, IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, boolean focused, boolean newSessionId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean requestAutofillData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean showAssistFromActivity(IBinder token, Bundle args) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean isRootVoiceInteraction(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void showLockTaskEscapeMessage(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void keyguardGoingAway(int flags) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public ComponentName getActivityClassForToken(IBinder token) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public String getPackageForToken(IBinder token) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void positionTaskInStack(int taskId, int stackId, int position) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void reportSizeConfigurations(IBinder token, int[] horizontalSizeConfiguration, int[] verticalSizeConfigurations, int[] smallestWidthConfigurations) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void dismissSplitScreenMode(boolean toTop) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void dismissPip(boolean animate, int animationDuration) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void moveTasksToFullscreenStack(int fromStackId, boolean onTop) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean moveTopActivityToPinnedStack(int stackId, Rect bounds) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean isInMultiWindowMode(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean isInPictureInPictureMode(IBinder token) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public boolean enterPictureInPictureMode(IBinder token, PictureInPictureParams params) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void setPictureInPictureParams(IBinder token, PictureInPictureParams params) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int getMaxNumPictureInPictureActions(IBinder token) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public IBinder getUriPermissionOwnerForActivity(IBinder activityToken) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void resizeDockedStack(Rect dockedBounds, Rect tempDockedTaskBounds, Rect tempDockedTaskInsetBounds, Rect tempOtherTaskBounds, Rect tempOtherTaskInsetBounds) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setSplitScreenResizing(boolean resizing) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int setVrMode(IBinder token, boolean enabled, ComponentName packageName) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public void startLocalVoiceInteraction(IBinder token, Bundle options) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void stopLocalVoiceInteraction(IBinder token) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean supportsLocalVoiceInteraction() throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void notifyPinnedStackAnimationStarted() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void notifyPinnedStackAnimationEnded() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void resizePinnedStack(Rect pinnedBounds, Rect tempPinnedTaskBounds) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean updateDisplayOverrideConfiguration(Configuration values, int displayId) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void dismissKeyguard(IBinder token, IKeyguardDismissCallback callback, CharSequence message) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void cancelTaskWindowTransition(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public ActivityManager.TaskSnapshot getTaskSnapshot(int taskId, boolean reducedResolution) throws RemoteException {
            return null;
        }

        @Override // android.app.IActivityTaskManager
        public void setDisablePreviewScreenshots(IBinder token, boolean disable) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int getLastResumedActivityUserId() throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public boolean updateConfiguration(Configuration values) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void updateLockTaskFeatures(int userId, int flags) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setShowWhenLocked(IBinder token, boolean showWhenLocked) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setInheritShowWhenLocked(IBinder token, boolean setInheritShownWhenLocked) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setTurnScreenOn(IBinder token, boolean turnScreenOn) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void registerRemoteAnimations(IBinder token, RemoteAnimationDefinition definition) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void registerRemoteAnimationForNextActivityStart(String packageName, RemoteAnimationAdapter adapter) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void registerRemoteAnimationsForDisplay(int displayId, RemoteAnimationDefinition definition) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void alwaysShowUnsupportedCompileSdkWarning(ComponentName activity) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setVrThread(int tid) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setPersistentVrThread(int tid) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void stopAppSwitches() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void resumeAppSwitches() throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setVoiceKeepAwake(IVoiceInteractionSession session, boolean keepAwake) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public int getPackageScreenCompatMode(String packageName) throws RemoteException {
            return 0;
        }

        @Override // android.app.IActivityTaskManager
        public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public boolean getPackageAskScreenCompat(String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.IActivityTaskManager
        public void setPackageAskScreenCompat(String packageName, boolean ask) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void clearLaunchParamsForPackages(List<String> packageNames) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void setDisplayToSingleTaskInstance(int displayId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void restartActivityProcessIfVisible(IBinder activityToken) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void onBackPressedOnTaskRoot(IBinder activityToken, IRequestFinishCallback callback) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void finishTaskActivity(int taskId) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void finishPackageActivity(String packageName) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void performActivityChanged(Bundle extras) throws RemoteException {
        }

        @Override // android.app.IActivityTaskManager
        public void performTopActivityChanged(Bundle extras) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IActivityTaskManager {
        private static final String DESCRIPTOR = "android.app.IActivityTaskManager";
        static final int TRANSACTION_activityDestroyed = 22;
        static final int TRANSACTION_activityIdle = 17;
        static final int TRANSACTION_activityPaused = 20;
        static final int TRANSACTION_activityRelaunched = 23;
        static final int TRANSACTION_activityResumed = 18;
        static final int TRANSACTION_activitySlept = 24;
        static final int TRANSACTION_activityStopped = 21;
        static final int TRANSACTION_activityTopResumedStateLost = 19;
        static final int TRANSACTION_addAppTask = 75;
        static final int TRANSACTION_alwaysShowUnsupportedCompileSdkWarning = 147;
        static final int TRANSACTION_cancelRecentsAnimation = 60;
        static final int TRANSACTION_cancelTaskWindowTransition = 135;
        static final int TRANSACTION_clearLaunchParamsForPackages = 158;
        static final int TRANSACTION_convertFromTranslucent = 43;
        static final int TRANSACTION_convertToTranslucent = 44;
        static final int TRANSACTION_dismissKeyguard = 134;
        static final int TRANSACTION_dismissPip = 113;
        static final int TRANSACTION_dismissSplitScreenMode = 112;
        static final int TRANSACTION_enterPictureInPictureMode = 119;
        static final int TRANSACTION_finishActivity = 15;
        static final int TRANSACTION_finishActivityAffinity = 16;
        static final int TRANSACTION_finishPackageActivity = 163;
        static final int TRANSACTION_finishSubActivity = 38;
        static final int TRANSACTION_finishTaskActivity = 162;
        static final int TRANSACTION_finishVoiceTask = 71;
        static final int TRANSACTION_getActivityClassForToken = 108;
        static final int TRANSACTION_getActivityDisplayId = 47;
        static final int TRANSACTION_getActivityOptions = 67;
        static final int TRANSACTION_getAllStackInfos = 96;
        static final int TRANSACTION_getAppTaskThumbnailSize = 76;
        static final int TRANSACTION_getAppTasks = 68;
        static final int TRANSACTION_getAssistContextExtras = 99;
        static final int TRANSACTION_getCallingActivity = 28;
        static final int TRANSACTION_getCallingPackage = 27;
        static final int TRANSACTION_getDeviceConfigurationInfo = 131;
        static final int TRANSACTION_getFilteredTasks = 33;
        static final int TRANSACTION_getFocusedStackInfo = 58;
        static final int TRANSACTION_getFrontActivityScreenCompatMode = 25;
        static final int TRANSACTION_getLastResumedActivityUserId = 138;
        static final int TRANSACTION_getLaunchedFromPackage = 55;
        static final int TRANSACTION_getLaunchedFromUid = 54;
        static final int TRANSACTION_getLockTaskModeState = 65;
        static final int TRANSACTION_getMaxNumPictureInPictureActions = 121;
        static final int TRANSACTION_getPackageAskScreenCompat = 156;
        static final int TRANSACTION_getPackageForToken = 109;
        static final int TRANSACTION_getPackageScreenCompatMode = 154;
        static final int TRANSACTION_getRecentTasks = 39;
        static final int TRANSACTION_getRequestedOrientation = 42;
        static final int TRANSACTION_getStackInfo = 97;
        static final int TRANSACTION_getTaskBounds = 59;
        static final int TRANSACTION_getTaskDescription = 52;
        static final int TRANSACTION_getTaskDescriptionIcon = 80;
        static final int TRANSACTION_getTaskForActivity = 37;
        static final int TRANSACTION_getTaskSnapshot = 136;
        static final int TRANSACTION_getTasks = 32;
        static final int TRANSACTION_getUriPermissionOwnerForActivity = 122;
        static final int TRANSACTION_isActivityStartAllowedOnDisplay = 13;
        static final int TRANSACTION_isAssistDataAllowedOnCurrentActivity = 103;
        static final int TRANSACTION_isImmersive = 48;
        static final int TRANSACTION_isInLockTaskMode = 64;
        static final int TRANSACTION_isInMultiWindowMode = 117;
        static final int TRANSACTION_isInPictureInPictureMode = 118;
        static final int TRANSACTION_isRootVoiceInteraction = 105;
        static final int TRANSACTION_isTopActivityImmersive = 50;
        static final int TRANSACTION_isTopOfTask = 72;
        static final int TRANSACTION_keyguardGoingAway = 107;
        static final int TRANSACTION_launchAssistIntent = 100;
        static final int TRANSACTION_moveActivityTaskToBack = 51;
        static final int TRANSACTION_moveStackToDisplay = 87;
        static final int TRANSACTION_moveTaskToFront = 36;
        static final int TRANSACTION_moveTaskToStack = 90;
        static final int TRANSACTION_moveTasksToFullscreenStack = 115;
        static final int TRANSACTION_moveTopActivityToPinnedStack = 116;
        static final int TRANSACTION_navigateUpTo = 35;
        static final int TRANSACTION_notifyActivityDrawn = 45;
        static final int TRANSACTION_notifyEnterAnimationComplete = 74;
        static final int TRANSACTION_notifyLaunchTaskBehindComplete = 73;
        static final int TRANSACTION_notifyPinnedStackAnimationEnded = 130;
        static final int TRANSACTION_notifyPinnedStackAnimationStarted = 129;
        static final int TRANSACTION_offsetPinnedStackBounds = 93;
        static final int TRANSACTION_onBackPressedOnTaskRoot = 161;
        static final int TRANSACTION_overridePendingTransition = 53;
        static final int TRANSACTION_performActivityChanged = 164;
        static final int TRANSACTION_performTopActivityChanged = 165;
        static final int TRANSACTION_positionTaskInStack = 110;
        static final int TRANSACTION_registerRemoteAnimationForNextActivityStart = 145;
        static final int TRANSACTION_registerRemoteAnimations = 144;
        static final int TRANSACTION_registerRemoteAnimationsForDisplay = 146;
        static final int TRANSACTION_registerTaskStackListener = 82;
        static final int TRANSACTION_releaseActivityInstance = 77;
        static final int TRANSACTION_releaseSomeActivities = 79;
        static final int TRANSACTION_removeAllVisibleRecentTasks = 31;
        static final int TRANSACTION_removeStack = 88;
        static final int TRANSACTION_removeStacksInWindowingModes = 94;
        static final int TRANSACTION_removeStacksWithActivityTypes = 95;
        static final int TRANSACTION_removeTask = 30;
        static final int TRANSACTION_reportActivityFullyDrawn = 46;
        static final int TRANSACTION_reportAssistContextExtras = 56;
        static final int TRANSACTION_reportSizeConfigurations = 111;
        static final int TRANSACTION_requestAssistContextExtras = 101;
        static final int TRANSACTION_requestAutofillData = 102;
        static final int TRANSACTION_requestStartActivityPermissionToken = 78;
        static final int TRANSACTION_resizeDockedStack = 123;
        static final int TRANSACTION_resizePinnedStack = 132;
        static final int TRANSACTION_resizeStack = 91;
        static final int TRANSACTION_resizeTask = 86;
        static final int TRANSACTION_restartActivityProcessIfVisible = 160;
        static final int TRANSACTION_resumeAppSwitches = 151;
        static final int TRANSACTION_setActivityController = 152;
        static final int TRANSACTION_setDisablePreviewScreenshots = 137;
        static final int TRANSACTION_setDisplayToSingleTaskInstance = 159;
        static final int TRANSACTION_setFocusedStack = 57;
        static final int TRANSACTION_setFocusedTask = 29;
        static final int TRANSACTION_setFrontActivityScreenCompatMode = 26;
        static final int TRANSACTION_setImmersive = 49;
        static final int TRANSACTION_setInheritShowWhenLocked = 142;
        static final int TRANSACTION_setLockScreenShown = 98;
        static final int TRANSACTION_setPackageAskScreenCompat = 157;
        static final int TRANSACTION_setPackageScreenCompatMode = 155;
        static final int TRANSACTION_setPersistentVrThread = 149;
        static final int TRANSACTION_setPictureInPictureParams = 120;
        static final int TRANSACTION_setRequestedOrientation = 41;
        static final int TRANSACTION_setShowWhenLocked = 141;
        static final int TRANSACTION_setSplitScreenResizing = 124;
        static final int TRANSACTION_setTaskDescription = 66;
        static final int TRANSACTION_setTaskResizeable = 84;
        static final int TRANSACTION_setTaskWindowingMode = 89;
        static final int TRANSACTION_setTaskWindowingModeSplitScreenPrimary = 92;
        static final int TRANSACTION_setTurnScreenOn = 143;
        static final int TRANSACTION_setVoiceKeepAwake = 153;
        static final int TRANSACTION_setVrMode = 125;
        static final int TRANSACTION_setVrThread = 148;
        static final int TRANSACTION_shouldUpRecreateTask = 34;
        static final int TRANSACTION_showAssistFromActivity = 104;
        static final int TRANSACTION_showLockTaskEscapeMessage = 106;
        static final int TRANSACTION_startActivities = 2;
        static final int TRANSACTION_startActivity = 1;
        static final int TRANSACTION_startActivityAndWait = 6;
        static final int TRANSACTION_startActivityAsCaller = 12;
        static final int TRANSACTION_startActivityAsUser = 3;
        static final int TRANSACTION_startActivityFromRecents = 11;
        static final int TRANSACTION_startActivityIntentSender = 5;
        static final int TRANSACTION_startActivityWithConfig = 7;
        static final int TRANSACTION_startAssistantActivity = 9;
        static final int TRANSACTION_startInPlaceAnimationOnFrontMostApplication = 81;
        static final int TRANSACTION_startLocalVoiceInteraction = 126;
        static final int TRANSACTION_startLockTaskModeByToken = 61;
        static final int TRANSACTION_startNextMatchingActivity = 4;
        static final int TRANSACTION_startRecentsActivity = 10;
        static final int TRANSACTION_startSystemLockTaskMode = 69;
        static final int TRANSACTION_startVoiceActivity = 8;
        static final int TRANSACTION_stopAppSwitches = 150;
        static final int TRANSACTION_stopLocalVoiceInteraction = 127;
        static final int TRANSACTION_stopLockTaskModeByToken = 62;
        static final int TRANSACTION_stopSystemLockTaskMode = 70;
        static final int TRANSACTION_supportsLocalVoiceInteraction = 128;
        static final int TRANSACTION_suppressResizeConfigChanges = 114;
        static final int TRANSACTION_toggleFreeformWindowingMode = 85;
        static final int TRANSACTION_unhandledBack = 14;
        static final int TRANSACTION_unregisterTaskStackListener = 83;
        static final int TRANSACTION_updateConfiguration = 139;
        static final int TRANSACTION_updateDisplayOverrideConfiguration = 133;
        static final int TRANSACTION_updateLockTaskFeatures = 140;
        static final int TRANSACTION_updateLockTaskPackages = 63;
        static final int TRANSACTION_willActivityBeVisible = 40;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IActivityTaskManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IActivityTaskManager)) {
                return (IActivityTaskManager) iin;
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
                    return "startActivity";
                case 2:
                    return "startActivities";
                case 3:
                    return "startActivityAsUser";
                case 4:
                    return "startNextMatchingActivity";
                case 5:
                    return "startActivityIntentSender";
                case 6:
                    return "startActivityAndWait";
                case 7:
                    return "startActivityWithConfig";
                case 8:
                    return "startVoiceActivity";
                case 9:
                    return "startAssistantActivity";
                case 10:
                    return "startRecentsActivity";
                case 11:
                    return "startActivityFromRecents";
                case 12:
                    return "startActivityAsCaller";
                case 13:
                    return "isActivityStartAllowedOnDisplay";
                case 14:
                    return "unhandledBack";
                case 15:
                    return "finishActivity";
                case 16:
                    return "finishActivityAffinity";
                case 17:
                    return "activityIdle";
                case 18:
                    return "activityResumed";
                case 19:
                    return "activityTopResumedStateLost";
                case 20:
                    return "activityPaused";
                case 21:
                    return "activityStopped";
                case 22:
                    return "activityDestroyed";
                case 23:
                    return "activityRelaunched";
                case 24:
                    return "activitySlept";
                case 25:
                    return "getFrontActivityScreenCompatMode";
                case 26:
                    return "setFrontActivityScreenCompatMode";
                case 27:
                    return "getCallingPackage";
                case 28:
                    return "getCallingActivity";
                case 29:
                    return "setFocusedTask";
                case 30:
                    return "removeTask";
                case 31:
                    return "removeAllVisibleRecentTasks";
                case 32:
                    return "getTasks";
                case 33:
                    return "getFilteredTasks";
                case 34:
                    return "shouldUpRecreateTask";
                case 35:
                    return "navigateUpTo";
                case 36:
                    return "moveTaskToFront";
                case 37:
                    return "getTaskForActivity";
                case 38:
                    return "finishSubActivity";
                case 39:
                    return "getRecentTasks";
                case 40:
                    return "willActivityBeVisible";
                case 41:
                    return "setRequestedOrientation";
                case 42:
                    return "getRequestedOrientation";
                case 43:
                    return "convertFromTranslucent";
                case 44:
                    return "convertToTranslucent";
                case 45:
                    return "notifyActivityDrawn";
                case 46:
                    return "reportActivityFullyDrawn";
                case 47:
                    return "getActivityDisplayId";
                case 48:
                    return "isImmersive";
                case 49:
                    return "setImmersive";
                case 50:
                    return "isTopActivityImmersive";
                case 51:
                    return "moveActivityTaskToBack";
                case 52:
                    return "getTaskDescription";
                case 53:
                    return "overridePendingTransition";
                case 54:
                    return "getLaunchedFromUid";
                case 55:
                    return "getLaunchedFromPackage";
                case 56:
                    return "reportAssistContextExtras";
                case 57:
                    return "setFocusedStack";
                case 58:
                    return "getFocusedStackInfo";
                case 59:
                    return "getTaskBounds";
                case 60:
                    return "cancelRecentsAnimation";
                case 61:
                    return "startLockTaskModeByToken";
                case 62:
                    return "stopLockTaskModeByToken";
                case 63:
                    return "updateLockTaskPackages";
                case 64:
                    return "isInLockTaskMode";
                case 65:
                    return "getLockTaskModeState";
                case 66:
                    return "setTaskDescription";
                case 67:
                    return "getActivityOptions";
                case 68:
                    return "getAppTasks";
                case 69:
                    return "startSystemLockTaskMode";
                case 70:
                    return "stopSystemLockTaskMode";
                case 71:
                    return "finishVoiceTask";
                case 72:
                    return "isTopOfTask";
                case 73:
                    return "notifyLaunchTaskBehindComplete";
                case 74:
                    return "notifyEnterAnimationComplete";
                case 75:
                    return "addAppTask";
                case 76:
                    return "getAppTaskThumbnailSize";
                case 77:
                    return "releaseActivityInstance";
                case 78:
                    return "requestStartActivityPermissionToken";
                case 79:
                    return "releaseSomeActivities";
                case 80:
                    return "getTaskDescriptionIcon";
                case 81:
                    return "startInPlaceAnimationOnFrontMostApplication";
                case 82:
                    return "registerTaskStackListener";
                case 83:
                    return "unregisterTaskStackListener";
                case 84:
                    return "setTaskResizeable";
                case 85:
                    return "toggleFreeformWindowingMode";
                case 86:
                    return "resizeTask";
                case 87:
                    return "moveStackToDisplay";
                case 88:
                    return "removeStack";
                case 89:
                    return "setTaskWindowingMode";
                case 90:
                    return "moveTaskToStack";
                case 91:
                    return "resizeStack";
                case 92:
                    return "setTaskWindowingModeSplitScreenPrimary";
                case 93:
                    return "offsetPinnedStackBounds";
                case 94:
                    return "removeStacksInWindowingModes";
                case 95:
                    return "removeStacksWithActivityTypes";
                case 96:
                    return "getAllStackInfos";
                case 97:
                    return "getStackInfo";
                case 98:
                    return "setLockScreenShown";
                case 99:
                    return "getAssistContextExtras";
                case 100:
                    return "launchAssistIntent";
                case 101:
                    return "requestAssistContextExtras";
                case 102:
                    return "requestAutofillData";
                case 103:
                    return "isAssistDataAllowedOnCurrentActivity";
                case 104:
                    return "showAssistFromActivity";
                case 105:
                    return "isRootVoiceInteraction";
                case 106:
                    return "showLockTaskEscapeMessage";
                case 107:
                    return "keyguardGoingAway";
                case 108:
                    return "getActivityClassForToken";
                case 109:
                    return "getPackageForToken";
                case 110:
                    return "positionTaskInStack";
                case 111:
                    return "reportSizeConfigurations";
                case 112:
                    return "dismissSplitScreenMode";
                case 113:
                    return "dismissPip";
                case 114:
                    return "suppressResizeConfigChanges";
                case 115:
                    return "moveTasksToFullscreenStack";
                case 116:
                    return "moveTopActivityToPinnedStack";
                case 117:
                    return "isInMultiWindowMode";
                case 118:
                    return "isInPictureInPictureMode";
                case 119:
                    return "enterPictureInPictureMode";
                case 120:
                    return "setPictureInPictureParams";
                case 121:
                    return "getMaxNumPictureInPictureActions";
                case 122:
                    return "getUriPermissionOwnerForActivity";
                case 123:
                    return "resizeDockedStack";
                case 124:
                    return "setSplitScreenResizing";
                case 125:
                    return "setVrMode";
                case 126:
                    return "startLocalVoiceInteraction";
                case 127:
                    return "stopLocalVoiceInteraction";
                case 128:
                    return "supportsLocalVoiceInteraction";
                case 129:
                    return "notifyPinnedStackAnimationStarted";
                case 130:
                    return "notifyPinnedStackAnimationEnded";
                case 131:
                    return "getDeviceConfigurationInfo";
                case 132:
                    return "resizePinnedStack";
                case 133:
                    return "updateDisplayOverrideConfiguration";
                case 134:
                    return "dismissKeyguard";
                case 135:
                    return "cancelTaskWindowTransition";
                case 136:
                    return "getTaskSnapshot";
                case 137:
                    return "setDisablePreviewScreenshots";
                case 138:
                    return "getLastResumedActivityUserId";
                case 139:
                    return "updateConfiguration";
                case 140:
                    return "updateLockTaskFeatures";
                case 141:
                    return "setShowWhenLocked";
                case 142:
                    return "setInheritShowWhenLocked";
                case 143:
                    return "setTurnScreenOn";
                case 144:
                    return "registerRemoteAnimations";
                case 145:
                    return "registerRemoteAnimationForNextActivityStart";
                case 146:
                    return "registerRemoteAnimationsForDisplay";
                case 147:
                    return "alwaysShowUnsupportedCompileSdkWarning";
                case 148:
                    return "setVrThread";
                case 149:
                    return "setPersistentVrThread";
                case 150:
                    return "stopAppSwitches";
                case 151:
                    return "resumeAppSwitches";
                case 152:
                    return "setActivityController";
                case 153:
                    return "setVoiceKeepAwake";
                case 154:
                    return "getPackageScreenCompatMode";
                case 155:
                    return "setPackageScreenCompatMode";
                case 156:
                    return "getPackageAskScreenCompat";
                case 157:
                    return "setPackageAskScreenCompat";
                case 158:
                    return "clearLaunchParamsForPackages";
                case 159:
                    return "setDisplayToSingleTaskInstance";
                case 160:
                    return "restartActivityProcessIfVisible";
                case 161:
                    return "onBackPressedOnTaskRoot";
                case 162:
                    return "finishTaskActivity";
                case 163:
                    return "finishPackageActivity";
                case 164:
                    return "performActivityChanged";
                case 165:
                    return "performTopActivityChanged";
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
            Intent _arg2;
            ProfilerInfo _arg8;
            Bundle _arg9;
            Bundle _arg5;
            Intent _arg22;
            ProfilerInfo _arg82;
            Bundle _arg92;
            Intent _arg1;
            Bundle _arg23;
            Intent _arg3;
            Bundle _arg10;
            Intent _arg24;
            ProfilerInfo _arg83;
            Bundle _arg93;
            Intent _arg25;
            Configuration _arg84;
            Bundle _arg94;
            Intent _arg32;
            ProfilerInfo _arg85;
            Bundle _arg95;
            Intent _arg33;
            Bundle _arg52;
            Intent _arg0;
            Bundle _arg12;
            Intent _arg26;
            ProfilerInfo _arg86;
            Bundle _arg96;
            boolean _arg11;
            Intent _arg13;
            Intent _arg27;
            Configuration _arg14;
            Bundle _arg15;
            PersistableBundle _arg28;
            CharSequence _arg34;
            Intent _arg16;
            Intent _arg35;
            Bundle _arg4;
            Bundle _arg17;
            Bundle _arg18;
            AssistStructure _arg29;
            AssistContent _arg36;
            Uri _arg42;
            ActivityManager.TaskDescription _arg19;
            Intent _arg110;
            ActivityManager.TaskDescription _arg210;
            Bitmap _arg37;
            Bundle _arg02;
            Rect _arg111;
            Rect _arg112;
            Rect _arg43;
            Rect _arg113;
            Intent _arg03;
            Bundle _arg44;
            Bundle _arg211;
            Bundle _arg114;
            Bundle _arg115;
            Rect _arg116;
            PictureInPictureParams _arg117;
            PictureInPictureParams _arg118;
            Rect _arg04;
            Rect _arg119;
            Rect _arg212;
            Rect _arg38;
            Rect _arg45;
            ComponentName _arg213;
            Bundle _arg120;
            Rect _arg05;
            Rect _arg121;
            Configuration _arg06;
            CharSequence _arg214;
            Configuration _arg07;
            RemoteAnimationDefinition _arg122;
            RemoteAnimationAdapter _arg123;
            RemoteAnimationDefinition _arg124;
            ComponentName _arg08;
            Bundle _arg09;
            Bundle _arg010;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg011 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg125 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    String _arg39 = data.readString();
                    IBinder _arg46 = data.readStrongBinder();
                    String _arg53 = data.readString();
                    int _arg6 = data.readInt();
                    int _arg7 = data.readInt();
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
                    int _result = startActivity(_arg011, _arg125, _arg2, _arg39, _arg46, _arg53, _arg6, _arg7, _arg8, _arg9);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg012 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg126 = data.readString();
                    Intent[] _arg215 = (Intent[]) data.createTypedArray(Intent.CREATOR);
                    String[] _arg310 = data.createStringArray();
                    IBinder _arg47 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg5 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    int _arg62 = data.readInt();
                    int _result2 = startActivities(_arg012, _arg126, _arg215, _arg310, _arg47, _arg5, _arg62);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg013 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg127 = data.readString();
                    if (data.readInt() != 0) {
                        _arg22 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    String _arg311 = data.readString();
                    IBinder _arg48 = data.readStrongBinder();
                    String _arg54 = data.readString();
                    int _arg63 = data.readInt();
                    int _arg72 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg82 = ProfilerInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg82 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg92 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg92 = null;
                    }
                    int _arg102 = data.readInt();
                    int _result3 = startActivityAsUser(_arg013, _arg127, _arg22, _arg311, _arg48, _arg54, _arg63, _arg72, _arg82, _arg92, _arg102);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg014 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg23 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg23 = null;
                    }
                    boolean startNextMatchingActivity = startNextMatchingActivity(_arg014, _arg1, _arg23);
                    reply.writeNoException();
                    reply.writeInt(startNextMatchingActivity ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg015 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    IIntentSender _arg128 = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    IBinder _arg216 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg3 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    String _arg49 = data.readString();
                    IBinder _arg55 = data.readStrongBinder();
                    String _arg64 = data.readString();
                    int _arg73 = data.readInt();
                    int _arg87 = data.readInt();
                    int _arg97 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg10 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg10 = null;
                    }
                    int _result4 = startActivityIntentSender(_arg015, _arg128, _arg216, _arg3, _arg49, _arg55, _arg64, _arg73, _arg87, _arg97, _arg10);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg016 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg129 = data.readString();
                    if (data.readInt() != 0) {
                        _arg24 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg24 = null;
                    }
                    String _arg312 = data.readString();
                    IBinder _arg410 = data.readStrongBinder();
                    String _arg56 = data.readString();
                    int _arg65 = data.readInt();
                    int _arg74 = data.readInt();
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
                    int _arg103 = data.readInt();
                    WaitResult _result5 = startActivityAndWait(_arg016, _arg129, _arg24, _arg312, _arg410, _arg56, _arg65, _arg74, _arg83, _arg93, _arg103);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg017 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg130 = data.readString();
                    if (data.readInt() != 0) {
                        _arg25 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg25 = null;
                    }
                    String _arg313 = data.readString();
                    IBinder _arg411 = data.readStrongBinder();
                    String _arg57 = data.readString();
                    int _arg66 = data.readInt();
                    int _arg75 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg84 = Configuration.CREATOR.createFromParcel(data);
                    } else {
                        _arg84 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg94 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg94 = null;
                    }
                    int _arg104 = data.readInt();
                    int _result6 = startActivityWithConfig(_arg017, _arg130, _arg25, _arg313, _arg411, _arg57, _arg66, _arg75, _arg84, _arg94, _arg104);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    int _arg131 = data.readInt();
                    int _arg217 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg32 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    String _arg412 = data.readString();
                    IVoiceInteractionSession _arg58 = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                    IVoiceInteractor _arg67 = IVoiceInteractor.Stub.asInterface(data.readStrongBinder());
                    int _arg76 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg85 = ProfilerInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg85 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg95 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg95 = null;
                    }
                    int _arg105 = data.readInt();
                    int _result7 = startVoiceActivity(_arg018, _arg131, _arg217, _arg32, _arg412, _arg58, _arg67, _arg76, _arg85, _arg95, _arg105);
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    int _arg132 = data.readInt();
                    int _arg218 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg33 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg33 = null;
                    }
                    String _arg413 = data.readString();
                    if (data.readInt() != 0) {
                        _arg52 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg52 = null;
                    }
                    int _arg68 = data.readInt();
                    int _result8 = startAssistantActivity(_arg019, _arg132, _arg218, _arg33, _arg413, _arg52, _arg68);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    IAssistDataReceiver _arg133 = IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
                    IRecentsAnimationRunner _arg219 = IRecentsAnimationRunner.Stub.asInterface(data.readStrongBinder());
                    startRecentsActivity(_arg0, _arg133, _arg219);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    int _result9 = startActivityFromRecents(_arg020, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg021 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg134 = data.readString();
                    if (data.readInt() != 0) {
                        _arg26 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg26 = null;
                    }
                    String _arg314 = data.readString();
                    IBinder _arg414 = data.readStrongBinder();
                    String _arg59 = data.readString();
                    int _arg69 = data.readInt();
                    int _arg77 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg86 = ProfilerInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg86 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg96 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg96 = null;
                    }
                    IBinder _arg106 = data.readStrongBinder();
                    _arg11 = data.readInt() != 0;
                    int _arg1210 = data.readInt();
                    int _result10 = startActivityAsCaller(_arg021, _arg134, _arg26, _arg314, _arg414, _arg59, _arg69, _arg77, _arg86, _arg96, _arg106, _arg11, _arg1210);
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    String _arg220 = data.readString();
                    int _arg315 = data.readInt();
                    boolean isActivityStartAllowedOnDisplay = isActivityStartAllowedOnDisplay(_arg022, _arg13, _arg220, _arg315);
                    reply.writeNoException();
                    reply.writeInt(isActivityStartAllowedOnDisplay ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    unhandledBack();
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg023 = data.readStrongBinder();
                    int _arg135 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg27 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg27 = null;
                    }
                    int _arg316 = data.readInt();
                    boolean finishActivity = finishActivity(_arg023, _arg135, _arg27, _arg316);
                    reply.writeNoException();
                    reply.writeInt(finishActivity ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg024 = data.readStrongBinder();
                    boolean finishActivityAffinity = finishActivityAffinity(_arg024);
                    reply.writeNoException();
                    reply.writeInt(finishActivityAffinity ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg025 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg14 = Configuration.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    _arg11 = data.readInt() != 0;
                    boolean _arg221 = _arg11;
                    activityIdle(_arg025, _arg14, _arg221);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg026 = data.readStrongBinder();
                    activityResumed(_arg026);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    activityTopResumedStateLost();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg027 = data.readStrongBinder();
                    activityPaused(_arg027);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg028 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg15 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg28 = PersistableBundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg28 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg34 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg34 = null;
                    }
                    activityStopped(_arg028, _arg15, _arg28, _arg34);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg029 = data.readStrongBinder();
                    activityDestroyed(_arg029);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg030 = data.readStrongBinder();
                    activityRelaunched(_arg030);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg031 = data.readStrongBinder();
                    activitySlept(_arg031);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _result11 = getFrontActivityScreenCompatMode();
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg032 = data.readInt();
                    setFrontActivityScreenCompatMode(_arg032);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg033 = data.readStrongBinder();
                    String _result12 = getCallingPackage(_arg033);
                    reply.writeNoException();
                    reply.writeString(_result12);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg034 = data.readStrongBinder();
                    ComponentName _result13 = getCallingActivity(_arg034);
                    reply.writeNoException();
                    if (_result13 != null) {
                        reply.writeInt(1);
                        _result13.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg035 = data.readInt();
                    setFocusedTask(_arg035);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    boolean removeTask = removeTask(_arg036);
                    reply.writeNoException();
                    reply.writeInt(removeTask ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    removeAllVisibleRecentTasks();
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg037 = data.readInt();
                    List<ActivityManager.RunningTaskInfo> _result14 = getTasks(_arg037);
                    reply.writeNoException();
                    reply.writeTypedList(_result14);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg038 = data.readInt();
                    int _arg136 = data.readInt();
                    int _arg222 = data.readInt();
                    List<ActivityManager.RunningTaskInfo> _result15 = getFilteredTasks(_arg038, _arg136, _arg222);
                    reply.writeNoException();
                    reply.writeTypedList(_result15);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg039 = data.readStrongBinder();
                    String _arg137 = data.readString();
                    boolean shouldUpRecreateTask = shouldUpRecreateTask(_arg039, _arg137);
                    reply.writeNoException();
                    reply.writeInt(shouldUpRecreateTask ? 1 : 0);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg040 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg16 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg16 = null;
                    }
                    int _arg223 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg35 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg35 = null;
                    }
                    boolean navigateUpTo = navigateUpTo(_arg040, _arg16, _arg223, _arg35);
                    reply.writeNoException();
                    reply.writeInt(navigateUpTo ? 1 : 0);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg041 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg138 = data.readString();
                    int _arg224 = data.readInt();
                    int _arg317 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg4 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    moveTaskToFront(_arg041, _arg138, _arg224, _arg317, _arg4);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg042 = data.readStrongBinder();
                    _arg11 = data.readInt() != 0;
                    boolean _arg139 = _arg11;
                    int _result16 = getTaskForActivity(_arg042, _arg139);
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg043 = data.readStrongBinder();
                    String _arg140 = data.readString();
                    int _arg225 = data.readInt();
                    finishSubActivity(_arg043, _arg140, _arg225);
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg044 = data.readInt();
                    int _arg141 = data.readInt();
                    int _arg226 = data.readInt();
                    ParceledListSlice _result17 = getRecentTasks(_arg044, _arg141, _arg226);
                    reply.writeNoException();
                    if (_result17 != null) {
                        reply.writeInt(1);
                        _result17.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg045 = data.readStrongBinder();
                    boolean willActivityBeVisible = willActivityBeVisible(_arg045);
                    reply.writeNoException();
                    reply.writeInt(willActivityBeVisible ? 1 : 0);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg046 = data.readStrongBinder();
                    int _arg142 = data.readInt();
                    setRequestedOrientation(_arg046, _arg142);
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg047 = data.readStrongBinder();
                    int _result18 = getRequestedOrientation(_arg047);
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg048 = data.readStrongBinder();
                    boolean convertFromTranslucent = convertFromTranslucent(_arg048);
                    reply.writeNoException();
                    reply.writeInt(convertFromTranslucent ? 1 : 0);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg049 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg17 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg17 = null;
                    }
                    boolean convertToTranslucent = convertToTranslucent(_arg049, _arg17);
                    reply.writeNoException();
                    reply.writeInt(convertToTranslucent ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg050 = data.readStrongBinder();
                    notifyActivityDrawn(_arg050);
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg051 = data.readStrongBinder();
                    _arg11 = data.readInt() != 0;
                    boolean _arg143 = _arg11;
                    reportActivityFullyDrawn(_arg051, _arg143);
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg052 = data.readStrongBinder();
                    int _result19 = getActivityDisplayId(_arg052);
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg053 = data.readStrongBinder();
                    boolean isImmersive = isImmersive(_arg053);
                    reply.writeNoException();
                    reply.writeInt(isImmersive ? 1 : 0);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg054 = data.readStrongBinder();
                    _arg11 = data.readInt() != 0;
                    boolean _arg144 = _arg11;
                    setImmersive(_arg054, _arg144);
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isTopActivityImmersive = isTopActivityImmersive();
                    reply.writeNoException();
                    reply.writeInt(isTopActivityImmersive ? 1 : 0);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg055 = data.readStrongBinder();
                    _arg11 = data.readInt() != 0;
                    boolean _arg145 = _arg11;
                    boolean moveActivityTaskToBack = moveActivityTaskToBack(_arg055, _arg145);
                    reply.writeNoException();
                    reply.writeInt(moveActivityTaskToBack ? 1 : 0);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg056 = data.readInt();
                    ActivityManager.TaskDescription _result20 = getTaskDescription(_arg056);
                    reply.writeNoException();
                    if (_result20 != null) {
                        reply.writeInt(1);
                        _result20.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg057 = data.readStrongBinder();
                    String _arg146 = data.readString();
                    int _arg227 = data.readInt();
                    int _arg318 = data.readInt();
                    overridePendingTransition(_arg057, _arg146, _arg227, _arg318);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg058 = data.readStrongBinder();
                    int _result21 = getLaunchedFromUid(_arg058);
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg059 = data.readStrongBinder();
                    String _result22 = getLaunchedFromPackage(_arg059);
                    reply.writeNoException();
                    reply.writeString(_result22);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg060 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg18 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg18 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg29 = AssistStructure.CREATOR.createFromParcel(data);
                    } else {
                        _arg29 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg36 = AssistContent.CREATOR.createFromParcel(data);
                    } else {
                        _arg36 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg42 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }
                    reportAssistContextExtras(_arg060, _arg18, _arg29, _arg36, _arg42);
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg061 = data.readInt();
                    setFocusedStack(_arg061);
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    ActivityManager.StackInfo _result23 = getFocusedStackInfo();
                    reply.writeNoException();
                    if (_result23 != null) {
                        reply.writeInt(1);
                        _result23.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg062 = data.readInt();
                    Rect _result24 = getTaskBounds(_arg062);
                    reply.writeNoException();
                    if (_result24 != null) {
                        reply.writeInt(1);
                        _result24.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    _arg11 = data.readInt() != 0;
                    boolean _arg063 = _arg11;
                    cancelRecentsAnimation(_arg063);
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg064 = data.readStrongBinder();
                    startLockTaskModeByToken(_arg064);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg065 = data.readStrongBinder();
                    stopLockTaskModeByToken(_arg065);
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg066 = data.readInt();
                    String[] _arg147 = data.createStringArray();
                    updateLockTaskPackages(_arg066, _arg147);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isInLockTaskMode = isInLockTaskMode();
                    reply.writeNoException();
                    reply.writeInt(isInLockTaskMode ? 1 : 0);
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    int _result25 = getLockTaskModeState();
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg067 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg19 = ActivityManager.TaskDescription.CREATOR.createFromParcel(data);
                    } else {
                        _arg19 = null;
                    }
                    setTaskDescription(_arg067, _arg19);
                    reply.writeNoException();
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg068 = data.readStrongBinder();
                    Bundle _result26 = getActivityOptions(_arg068);
                    reply.writeNoException();
                    if (_result26 != null) {
                        reply.writeInt(1);
                        _result26.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg069 = data.readString();
                    List<IBinder> _result27 = getAppTasks(_arg069);
                    reply.writeNoException();
                    reply.writeBinderList(_result27);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg070 = data.readInt();
                    startSystemLockTaskMode(_arg070);
                    reply.writeNoException();
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    stopSystemLockTaskMode();
                    reply.writeNoException();
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractionSession _arg071 = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                    finishVoiceTask(_arg071);
                    reply.writeNoException();
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg072 = data.readStrongBinder();
                    boolean isTopOfTask = isTopOfTask(_arg072);
                    reply.writeNoException();
                    reply.writeInt(isTopOfTask ? 1 : 0);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg073 = data.readStrongBinder();
                    notifyLaunchTaskBehindComplete(_arg073);
                    reply.writeNoException();
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg074 = data.readStrongBinder();
                    notifyEnterAnimationComplete(_arg074);
                    reply.writeNoException();
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg075 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg110 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg110 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg210 = ActivityManager.TaskDescription.CREATOR.createFromParcel(data);
                    } else {
                        _arg210 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg37 = Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg37 = null;
                    }
                    int _result28 = addAppTask(_arg075, _arg110, _arg210, _arg37);
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    Point _result29 = getAppTaskThumbnailSize();
                    reply.writeNoException();
                    if (_result29 != null) {
                        reply.writeInt(1);
                        _result29.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg076 = data.readStrongBinder();
                    boolean releaseActivityInstance = releaseActivityInstance(_arg076);
                    reply.writeNoException();
                    reply.writeInt(releaseActivityInstance ? 1 : 0);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg077 = data.readStrongBinder();
                    IBinder _result30 = requestStartActivityPermissionToken(_arg077);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result30);
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg078 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    releaseSomeActivities(_arg078);
                    reply.writeNoException();
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg079 = data.readString();
                    int _arg148 = data.readInt();
                    Bitmap _result31 = getTaskDescriptionIcon(_arg079, _arg148);
                    reply.writeNoException();
                    if (_result31 != null) {
                        reply.writeInt(1);
                        _result31.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    startInPlaceAnimationOnFrontMostApplication(_arg02);
                    reply.writeNoException();
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    ITaskStackListener _arg080 = ITaskStackListener.Stub.asInterface(data.readStrongBinder());
                    registerTaskStackListener(_arg080);
                    reply.writeNoException();
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    ITaskStackListener _arg081 = ITaskStackListener.Stub.asInterface(data.readStrongBinder());
                    unregisterTaskStackListener(_arg081);
                    reply.writeNoException();
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg082 = data.readInt();
                    int _arg149 = data.readInt();
                    setTaskResizeable(_arg082, _arg149);
                    reply.writeNoException();
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg083 = data.readStrongBinder();
                    toggleFreeformWindowingMode(_arg083);
                    reply.writeNoException();
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg084 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg111 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg111 = null;
                    }
                    int _arg228 = data.readInt();
                    resizeTask(_arg084, _arg111, _arg228);
                    reply.writeNoException();
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg085 = data.readInt();
                    int _arg150 = data.readInt();
                    moveStackToDisplay(_arg085, _arg150);
                    reply.writeNoException();
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg086 = data.readInt();
                    removeStack(_arg086);
                    reply.writeNoException();
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg087 = data.readInt();
                    int _arg151 = data.readInt();
                    _arg11 = data.readInt() != 0;
                    boolean _arg229 = _arg11;
                    setTaskWindowingMode(_arg087, _arg151, _arg229);
                    reply.writeNoException();
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg088 = data.readInt();
                    int _arg152 = data.readInt();
                    _arg11 = data.readInt() != 0;
                    boolean _arg230 = _arg11;
                    moveTaskToStack(_arg088, _arg152, _arg230);
                    reply.writeNoException();
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg089 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg112 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg112 = null;
                    }
                    boolean _arg231 = data.readInt() != 0;
                    boolean _arg319 = data.readInt() != 0;
                    boolean _arg415 = data.readInt() != 0;
                    int _arg510 = data.readInt();
                    resizeStack(_arg089, _arg112, _arg231, _arg319, _arg415, _arg510);
                    reply.writeNoException();
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg090 = data.readInt();
                    int _arg153 = data.readInt();
                    boolean _arg232 = data.readInt() != 0;
                    boolean _arg320 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        _arg43 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg43 = null;
                    }
                    boolean _arg511 = data.readInt() != 0;
                    boolean taskWindowingModeSplitScreenPrimary = setTaskWindowingModeSplitScreenPrimary(_arg090, _arg153, _arg232, _arg320, _arg43, _arg511);
                    reply.writeNoException();
                    reply.writeInt(taskWindowingModeSplitScreenPrimary ? 1 : 0);
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg091 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg113 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg113 = null;
                    }
                    int _arg233 = data.readInt();
                    int _arg321 = data.readInt();
                    int _arg416 = data.readInt();
                    offsetPinnedStackBounds(_arg091, _arg113, _arg233, _arg321, _arg416);
                    reply.writeNoException();
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg092 = data.createIntArray();
                    removeStacksInWindowingModes(_arg092);
                    reply.writeNoException();
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg093 = data.createIntArray();
                    removeStacksWithActivityTypes(_arg093);
                    reply.writeNoException();
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    List<ActivityManager.StackInfo> _result32 = getAllStackInfos();
                    reply.writeNoException();
                    reply.writeTypedList(_result32);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg094 = data.readInt();
                    int _arg154 = data.readInt();
                    ActivityManager.StackInfo _result33 = getStackInfo(_arg094, _arg154);
                    reply.writeNoException();
                    if (_result33 != null) {
                        reply.writeInt(1);
                        _result33.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg095 = data.readInt() != 0;
                    _arg11 = data.readInt() != 0;
                    boolean _arg155 = _arg11;
                    setLockScreenShown(_arg095, _arg155);
                    reply.writeNoException();
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg096 = data.readInt();
                    Bundle _result34 = getAssistContextExtras(_arg096);
                    reply.writeNoException();
                    if (_result34 != null) {
                        reply.writeInt(1);
                        _result34.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    int _arg156 = data.readInt();
                    String _arg234 = data.readString();
                    int _arg322 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg44 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg44 = null;
                    }
                    boolean launchAssistIntent = launchAssistIntent(_arg03, _arg156, _arg234, _arg322, _arg44);
                    reply.writeNoException();
                    reply.writeInt(launchAssistIntent ? 1 : 0);
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg097 = data.readInt();
                    IAssistDataReceiver _arg157 = IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg211 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg211 = null;
                    }
                    IBinder _arg323 = data.readStrongBinder();
                    boolean _arg417 = data.readInt() != 0;
                    boolean _arg512 = data.readInt() != 0;
                    boolean requestAssistContextExtras = requestAssistContextExtras(_arg097, _arg157, _arg211, _arg323, _arg417, _arg512);
                    reply.writeNoException();
                    reply.writeInt(requestAssistContextExtras ? 1 : 0);
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    IAssistDataReceiver _arg098 = IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg114 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg114 = null;
                    }
                    IBinder _arg235 = data.readStrongBinder();
                    int _arg324 = data.readInt();
                    boolean requestAutofillData = requestAutofillData(_arg098, _arg114, _arg235, _arg324);
                    reply.writeNoException();
                    reply.writeInt(requestAutofillData ? 1 : 0);
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAssistDataAllowedOnCurrentActivity = isAssistDataAllowedOnCurrentActivity();
                    reply.writeNoException();
                    reply.writeInt(isAssistDataAllowedOnCurrentActivity ? 1 : 0);
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg099 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg115 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg115 = null;
                    }
                    boolean showAssistFromActivity = showAssistFromActivity(_arg099, _arg115);
                    reply.writeNoException();
                    reply.writeInt(showAssistFromActivity ? 1 : 0);
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0100 = data.readStrongBinder();
                    boolean isRootVoiceInteraction = isRootVoiceInteraction(_arg0100);
                    reply.writeNoException();
                    reply.writeInt(isRootVoiceInteraction ? 1 : 0);
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0101 = data.readStrongBinder();
                    showLockTaskEscapeMessage(_arg0101);
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0102 = data.readInt();
                    keyguardGoingAway(_arg0102);
                    reply.writeNoException();
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0103 = data.readStrongBinder();
                    ComponentName _result35 = getActivityClassForToken(_arg0103);
                    reply.writeNoException();
                    if (_result35 != null) {
                        reply.writeInt(1);
                        _result35.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0104 = data.readStrongBinder();
                    String _result36 = getPackageForToken(_arg0104);
                    reply.writeNoException();
                    reply.writeString(_result36);
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0105 = data.readInt();
                    int _arg158 = data.readInt();
                    int _arg236 = data.readInt();
                    positionTaskInStack(_arg0105, _arg158, _arg236);
                    reply.writeNoException();
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0106 = data.readStrongBinder();
                    int[] _arg159 = data.createIntArray();
                    int[] _arg237 = data.createIntArray();
                    int[] _arg325 = data.createIntArray();
                    reportSizeConfigurations(_arg0106, _arg159, _arg237, _arg325);
                    reply.writeNoException();
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    _arg11 = data.readInt() != 0;
                    boolean _arg0107 = _arg11;
                    dismissSplitScreenMode(_arg0107);
                    reply.writeNoException();
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    _arg11 = data.readInt() != 0;
                    boolean _arg0108 = _arg11;
                    int _arg160 = data.readInt();
                    dismissPip(_arg0108, _arg160);
                    reply.writeNoException();
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    _arg11 = data.readInt() != 0;
                    boolean _arg0109 = _arg11;
                    suppressResizeConfigChanges(_arg0109);
                    reply.writeNoException();
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0110 = data.readInt();
                    _arg11 = data.readInt() != 0;
                    boolean _arg161 = _arg11;
                    moveTasksToFullscreenStack(_arg0110, _arg161);
                    reply.writeNoException();
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0111 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg116 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg116 = null;
                    }
                    boolean moveTopActivityToPinnedStack = moveTopActivityToPinnedStack(_arg0111, _arg116);
                    reply.writeNoException();
                    reply.writeInt(moveTopActivityToPinnedStack ? 1 : 0);
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0112 = data.readStrongBinder();
                    boolean isInMultiWindowMode = isInMultiWindowMode(_arg0112);
                    reply.writeNoException();
                    reply.writeInt(isInMultiWindowMode ? 1 : 0);
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0113 = data.readStrongBinder();
                    boolean isInPictureInPictureMode = isInPictureInPictureMode(_arg0113);
                    reply.writeNoException();
                    reply.writeInt(isInPictureInPictureMode ? 1 : 0);
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0114 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg117 = PictureInPictureParams.CREATOR.createFromParcel(data);
                    } else {
                        _arg117 = null;
                    }
                    boolean enterPictureInPictureMode = enterPictureInPictureMode(_arg0114, _arg117);
                    reply.writeNoException();
                    reply.writeInt(enterPictureInPictureMode ? 1 : 0);
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0115 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg118 = PictureInPictureParams.CREATOR.createFromParcel(data);
                    } else {
                        _arg118 = null;
                    }
                    setPictureInPictureParams(_arg0115, _arg118);
                    reply.writeNoException();
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0116 = data.readStrongBinder();
                    int _result37 = getMaxNumPictureInPictureActions(_arg0116);
                    reply.writeNoException();
                    reply.writeInt(_result37);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0117 = data.readStrongBinder();
                    IBinder _result38 = getUriPermissionOwnerForActivity(_arg0117);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result38);
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg119 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg119 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg212 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg212 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg38 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg38 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg45 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg45 = null;
                    }
                    resizeDockedStack(_arg04, _arg119, _arg212, _arg38, _arg45);
                    reply.writeNoException();
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    _arg11 = data.readInt() != 0;
                    boolean _arg0118 = _arg11;
                    setSplitScreenResizing(_arg0118);
                    reply.writeNoException();
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0119 = data.readStrongBinder();
                    _arg11 = data.readInt() != 0;
                    boolean _arg162 = _arg11;
                    if (data.readInt() != 0) {
                        _arg213 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg213 = null;
                    }
                    int _result39 = setVrMode(_arg0119, _arg162, _arg213);
                    reply.writeNoException();
                    reply.writeInt(_result39);
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0120 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg120 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg120 = null;
                    }
                    startLocalVoiceInteraction(_arg0120, _arg120);
                    reply.writeNoException();
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0121 = data.readStrongBinder();
                    stopLocalVoiceInteraction(_arg0121);
                    reply.writeNoException();
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    boolean supportsLocalVoiceInteraction = supportsLocalVoiceInteraction();
                    reply.writeNoException();
                    reply.writeInt(supportsLocalVoiceInteraction ? 1 : 0);
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    notifyPinnedStackAnimationStarted();
                    reply.writeNoException();
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    notifyPinnedStackAnimationEnded();
                    reply.writeNoException();
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    ConfigurationInfo _result40 = getDeviceConfigurationInfo();
                    reply.writeNoException();
                    if (_result40 != null) {
                        reply.writeInt(1);
                        _result40.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg121 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg121 = null;
                    }
                    resizePinnedStack(_arg05, _arg121);
                    reply.writeNoException();
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = Configuration.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    int _arg163 = data.readInt();
                    boolean updateDisplayOverrideConfiguration = updateDisplayOverrideConfiguration(_arg06, _arg163);
                    reply.writeNoException();
                    reply.writeInt(updateDisplayOverrideConfiguration ? 1 : 0);
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0122 = data.readStrongBinder();
                    IKeyguardDismissCallback _arg164 = IKeyguardDismissCallback.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg214 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg214 = null;
                    }
                    dismissKeyguard(_arg0122, _arg164, _arg214);
                    reply.writeNoException();
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0123 = data.readInt();
                    cancelTaskWindowTransition(_arg0123);
                    reply.writeNoException();
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0124 = data.readInt();
                    boolean _arg165 = data.readInt() != 0;
                    ActivityManager.TaskSnapshot _result41 = getTaskSnapshot(_arg0124, _arg165);
                    reply.writeNoException();
                    if (_result41 != null) {
                        reply.writeInt(1);
                        _result41.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0125 = data.readStrongBinder();
                    _arg11 = data.readInt() != 0;
                    boolean _arg166 = _arg11;
                    setDisablePreviewScreenshots(_arg0125, _arg166);
                    reply.writeNoException();
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    int _result42 = getLastResumedActivityUserId();
                    reply.writeNoException();
                    reply.writeInt(_result42);
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = Configuration.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    boolean updateConfiguration = updateConfiguration(_arg07);
                    reply.writeNoException();
                    reply.writeInt(updateConfiguration ? 1 : 0);
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0126 = data.readInt();
                    int _arg167 = data.readInt();
                    updateLockTaskFeatures(_arg0126, _arg167);
                    reply.writeNoException();
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0127 = data.readStrongBinder();
                    _arg11 = data.readInt() != 0;
                    boolean _arg168 = _arg11;
                    setShowWhenLocked(_arg0127, _arg168);
                    reply.writeNoException();
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0128 = data.readStrongBinder();
                    _arg11 = data.readInt() != 0;
                    boolean _arg169 = _arg11;
                    setInheritShowWhenLocked(_arg0128, _arg169);
                    reply.writeNoException();
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0129 = data.readStrongBinder();
                    _arg11 = data.readInt() != 0;
                    boolean _arg170 = _arg11;
                    setTurnScreenOn(_arg0129, _arg170);
                    reply.writeNoException();
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0130 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg122 = RemoteAnimationDefinition.CREATOR.createFromParcel(data);
                    } else {
                        _arg122 = null;
                    }
                    registerRemoteAnimations(_arg0130, _arg122);
                    reply.writeNoException();
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0131 = data.readString();
                    if (data.readInt() != 0) {
                        _arg123 = RemoteAnimationAdapter.CREATOR.createFromParcel(data);
                    } else {
                        _arg123 = null;
                    }
                    registerRemoteAnimationForNextActivityStart(_arg0131, _arg123);
                    reply.writeNoException();
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0132 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg124 = RemoteAnimationDefinition.CREATOR.createFromParcel(data);
                    } else {
                        _arg124 = null;
                    }
                    registerRemoteAnimationsForDisplay(_arg0132, _arg124);
                    reply.writeNoException();
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    alwaysShowUnsupportedCompileSdkWarning(_arg08);
                    reply.writeNoException();
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0133 = data.readInt();
                    setVrThread(_arg0133);
                    reply.writeNoException();
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0134 = data.readInt();
                    setPersistentVrThread(_arg0134);
                    reply.writeNoException();
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    stopAppSwitches();
                    reply.writeNoException();
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    resumeAppSwitches();
                    reply.writeNoException();
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    IActivityController _arg0135 = IActivityController.Stub.asInterface(data.readStrongBinder());
                    _arg11 = data.readInt() != 0;
                    boolean _arg171 = _arg11;
                    setActivityController(_arg0135, _arg171);
                    reply.writeNoException();
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractionSession _arg0136 = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                    _arg11 = data.readInt() != 0;
                    boolean _arg172 = _arg11;
                    setVoiceKeepAwake(_arg0136, _arg172);
                    reply.writeNoException();
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0137 = data.readString();
                    int _result43 = getPackageScreenCompatMode(_arg0137);
                    reply.writeNoException();
                    reply.writeInt(_result43);
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0138 = data.readString();
                    int _arg173 = data.readInt();
                    setPackageScreenCompatMode(_arg0138, _arg173);
                    reply.writeNoException();
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0139 = data.readString();
                    boolean packageAskScreenCompat = getPackageAskScreenCompat(_arg0139);
                    reply.writeNoException();
                    reply.writeInt(packageAskScreenCompat ? 1 : 0);
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0140 = data.readString();
                    _arg11 = data.readInt() != 0;
                    boolean _arg174 = _arg11;
                    setPackageAskScreenCompat(_arg0140, _arg174);
                    reply.writeNoException();
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg0141 = data.createStringArrayList();
                    clearLaunchParamsForPackages(_arg0141);
                    reply.writeNoException();
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0142 = data.readInt();
                    setDisplayToSingleTaskInstance(_arg0142);
                    reply.writeNoException();
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0143 = data.readStrongBinder();
                    restartActivityProcessIfVisible(_arg0143);
                    reply.writeNoException();
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0144 = data.readStrongBinder();
                    IRequestFinishCallback _arg175 = IRequestFinishCallback.Stub.asInterface(data.readStrongBinder());
                    onBackPressedOnTaskRoot(_arg0144, _arg175);
                    reply.writeNoException();
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0145 = data.readInt();
                    finishTaskActivity(_arg0145);
                    reply.writeNoException();
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0146 = data.readString();
                    finishPackageActivity(_arg0146);
                    reply.writeNoException();
                    return true;
                case 164:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg09 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg09 = null;
                    }
                    performActivityChanged(_arg09);
                    reply.writeNoException();
                    return true;
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg010 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg010 = null;
                    }
                    performTopActivityChanged(_arg010);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IActivityTaskManager {
            public static IActivityTaskManager sDefaultImpl;
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

            @Override // android.app.IActivityTaskManager
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
                    boolean _status = this.mRemote.transact(1, _data, _reply2, 0);
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

            @Override // android.app.IActivityTaskManager
            public int startActivities(IApplicationThread caller, String callingPackage, Intent[] intents, String[] resolvedTypes, IBinder resultTo, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(callingPackage);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeTypedArray(intents, 0);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeStringArray(resolvedTypes);
                    try {
                        _data.writeStrongBinder(resultTo);
                        if (options != null) {
                            _data.writeInt(1);
                            options.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(userId);
                        boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            int startActivities = Stub.getDefaultImpl().startActivities(caller, callingPackage, intents, resolvedTypes, resultTo, options, userId);
                            _reply.recycle();
                            _data.recycle();
                            return startActivities;
                        }
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
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityTaskManager
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
                    _status = this.mRemote.transact(3, _data2, _reply2, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean startNextMatchingActivity(IBinder callingActivity, Intent intent, Bundle options) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startNextMatchingActivity(callingActivity, intent, options);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int startActivityIntentSender(IApplicationThread caller, IIntentSender target, IBinder whitelistToken, Intent fillInIntent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flagsMask, int flagsValues, Bundle options) throws RemoteException {
                Parcel _reply;
                IBinder asBinder;
                boolean _status;
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
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    _data.writeStrongBinder(whitelistToken);
                    if (fillInIntent != null) {
                        _data.writeInt(1);
                        fillInIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeStrongBinder(resultTo);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    _data.writeInt(flagsMask);
                    _data.writeInt(flagsValues);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _status = this.mRemote.transact(5, _data, _reply2, 0);
                } catch (Throwable th2) {
                    th = th2;
                    _reply = _reply2;
                }
                try {
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int startActivityIntentSender = Stub.getDefaultImpl().startActivityIntentSender(caller, target, whitelistToken, fillInIntent, resolvedType, resultTo, resultWho, requestCode, flagsMask, flagsValues, options);
                        _reply2.recycle();
                        _data.recycle();
                        return startActivityIntentSender;
                    }
                    _reply2.readException();
                    int _result = _reply2.readInt();
                    _reply2.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityTaskManager
            public WaitResult startActivityAndWait(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                IBinder asBinder;
                WaitResult _result;
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
                    boolean _status = this.mRemote.transact(6, _data2, _reply2, 0);
                    if (!_status) {
                        try {
                            if (Stub.getDefaultImpl() != null) {
                                _data = _data2;
                                try {
                                    WaitResult startActivityAndWait = Stub.getDefaultImpl().startActivityAndWait(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options, userId);
                                    _reply2.recycle();
                                    _data.recycle();
                                    return startActivityAndWait;
                                } catch (Throwable th2) {
                                    th = th2;
                                    _reply = _reply2;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _data = _data2;
                            _reply = _reply2;
                        }
                    }
                    _data = _data2;
                    try {
                        _reply2.readException();
                        if (_reply2.readInt() != 0) {
                            _reply = _reply2;
                            try {
                                _result = WaitResult.CREATOR.createFromParcel(_reply);
                            } catch (Throwable th4) {
                                th = th4;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } else {
                            _reply = _reply2;
                            _result = null;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th5) {
                        th = th5;
                        _reply = _reply2;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    _reply = _reply2;
                    _data = _data2;
                }
            }

            @Override // android.app.IActivityTaskManager
            public int startActivityWithConfig(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, Configuration newConfig, Bundle options, int userId) throws RemoteException {
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
                    _data2.writeInt(startFlags);
                    if (newConfig != null) {
                        _data2.writeInt(1);
                        newConfig.writeToParcel(_data2, 0);
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
                    _status = this.mRemote.transact(7, _data2, _reply2, 0);
                } catch (Throwable th2) {
                    th = th2;
                    _reply = _reply2;
                    _data = _data2;
                }
                try {
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int startActivityWithConfig = Stub.getDefaultImpl().startActivityWithConfig(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, newConfig, options, userId);
                        _reply2.recycle();
                        _data2.recycle();
                        return startActivityWithConfig;
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

            @Override // android.app.IActivityTaskManager
            public int startVoiceActivity(String callingPackage, int callingPid, int callingUid, Intent intent, String resolvedType, IVoiceInteractionSession session, IVoiceInteractor interactor, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Parcel _reply;
                Parcel _data;
                boolean _status;
                Parcel _data2 = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                try {
                    _data2.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data2.writeString(callingPackage);
                    _data2.writeInt(callingPid);
                    _data2.writeInt(callingUid);
                    if (intent != null) {
                        try {
                            _data2.writeInt(1);
                            intent.writeToParcel(_data2, 0);
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply2;
                            _data = _data2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeString(resolvedType);
                    _data2.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data2.writeStrongBinder(interactor != null ? interactor.asBinder() : null);
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
                    _status = this.mRemote.transact(8, _data2, _reply2, 0);
                } catch (Throwable th2) {
                    th = th2;
                    _reply = _reply2;
                    _data = _data2;
                }
                try {
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int startVoiceActivity = Stub.getDefaultImpl().startVoiceActivity(callingPackage, callingPid, callingUid, intent, resolvedType, session, interactor, flags, profilerInfo, options, userId);
                        _reply2.recycle();
                        _data2.recycle();
                        return startVoiceActivity;
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

            @Override // android.app.IActivityTaskManager
            public int startAssistantActivity(String callingPackage, int callingPid, int callingUid, Intent intent, String resolvedType, Bundle options, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(callingPackage);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(callingPid);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
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
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int startAssistantActivity = Stub.getDefaultImpl().startAssistantActivity(callingPackage, callingPid, callingUid, intent, resolvedType, options, userId);
                        _reply.recycle();
                        _data.recycle();
                        return startAssistantActivity;
                    }
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
            }

            @Override // android.app.IActivityTaskManager
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
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
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
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public int startActivityAsCaller(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, IBinder permissionToken, boolean ignoreTargetSecurity, int userId) throws RemoteException {
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
                    int i = 1;
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
                    _data2.writeStrongBinder(permissionToken);
                    if (!ignoreTargetSecurity) {
                        i = 0;
                    }
                    _data2.writeInt(i);
                    _data2.writeInt(userId);
                    _status = this.mRemote.transact(12, _data2, _reply2, 0);
                } catch (Throwable th2) {
                    th = th2;
                    _reply = _reply2;
                    _data = _data2;
                }
                try {
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int startActivityAsCaller = Stub.getDefaultImpl().startActivityAsCaller(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options, permissionToken, ignoreTargetSecurity, userId);
                        _reply2.recycle();
                        _data2.recycle();
                        return startActivityAsCaller;
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

            @Override // android.app.IActivityTaskManager
            public boolean isActivityStartAllowedOnDisplay(int displayId, Intent intent, String resolvedType, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isActivityStartAllowedOnDisplay(displayId, intent, resolvedType, userId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void unhandledBack() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
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
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean finishActivityAffinity(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().finishActivityAffinity(token);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void activityIdle(IBinder token, Configuration config, boolean stopProfiling) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().activityIdle(token, config, stopProfiling);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void activityResumed(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().activityResumed(token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void activityTopResumedStateLost() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().activityTopResumedStateLost();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void activityPaused(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().activityPaused(token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void activityStopped(IBinder token, Bundle state, PersistableBundle persistentState, CharSequence description) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().activityStopped(token, state, persistentState, description);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void activityDestroyed(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(22, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().activityDestroyed(token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void activityRelaunched(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().activityRelaunched(token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void activitySlept(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(24, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().activitySlept(token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int getFrontActivityScreenCompatMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFrontActivityScreenCompatMode();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setFrontActivityScreenCompatMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setFrontActivityScreenCompatMode(mode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public String getCallingPackage(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCallingPackage(token);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public ComponentName getCallingActivity(IBinder token) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCallingActivity(token);
                    }
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

            @Override // android.app.IActivityTaskManager
            public void setFocusedTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setFocusedTask(taskId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean removeTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void removeAllVisibleRecentTasks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeAllVisibleRecentTasks();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public List<ActivityManager.RunningTaskInfo> getFilteredTasks(int maxNum, int ignoreActivityType, int ignoreWindowingMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(ignoreActivityType);
                    _data.writeInt(ignoreWindowingMode);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean shouldUpRecreateTask(IBinder token, String destAffinity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(destAffinity);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().shouldUpRecreateTask(token, destAffinity);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean navigateUpTo(IBinder token, Intent target, int resultCode, Intent resultData) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().navigateUpTo(token, target, resultCode, resultData);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void moveTaskToFront(IApplicationThread app, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app != null ? app.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeInt(task);
                    _data.writeInt(flags);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moveTaskToFront(app, callingPackage, task, flags, options);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(onlyRoot ? 1 : 0);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void finishSubActivity(IBinder token, String resultWho, int requestCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishSubActivity(token, resultWho, requestCode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public ParceledListSlice getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean willActivityBeVisible(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().willActivityBeVisible(token);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(requestedOrientation);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public int getRequestedOrientation(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRequestedOrientation(token);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean convertFromTranslucent(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().convertFromTranslucent(token);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean convertToTranslucent(IBinder token, Bundle options) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().convertToTranslucent(token, options);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void notifyActivityDrawn(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyActivityDrawn(token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void reportActivityFullyDrawn(IBinder token, boolean restoredFromBundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(restoredFromBundle ? 1 : 0);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportActivityFullyDrawn(token, restoredFromBundle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int getActivityDisplayId(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActivityDisplayId(activityToken);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean isImmersive(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isImmersive(token);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setImmersive(IBinder token, boolean immersive) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(immersive ? 1 : 0);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setImmersive(token, immersive);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean isTopActivityImmersive() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(nonRoot ? 1 : 0);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public ActivityManager.TaskDescription getTaskDescription(int taskId) throws RemoteException {
                ActivityManager.TaskDescription _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTaskDescription(taskId);
                    }
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

            @Override // android.app.IActivityTaskManager
            public void overridePendingTransition(IBinder token, String packageName, int enterAnim, int exitAnim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(packageName);
                    _data.writeInt(enterAnim);
                    _data.writeInt(exitAnim);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().overridePendingTransition(token, packageName, enterAnim, exitAnim);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void reportAssistContextExtras(IBinder token, Bundle extras, AssistStructure structure, AssistContent content, Uri referrer) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportAssistContextExtras(token, extras, structure, content, referrer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setFocusedStack(int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public ActivityManager.StackInfo getFocusedStackInfo() throws RemoteException {
                ActivityManager.StackInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public Rect getTaskBounds(int taskId) throws RemoteException {
                Rect _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void cancelRecentsAnimation(boolean restoreHomeStackPosition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(restoreHomeStackPosition ? 1 : 0);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void startLockTaskModeByToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startLockTaskModeByToken(token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void stopLockTaskModeByToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopLockTaskModeByToken(token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packages);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean isInLockTaskMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public int getLockTaskModeState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void setTaskDescription(IBinder token, ActivityManager.TaskDescription values) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTaskDescription(token, values);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public Bundle getActivityOptions(IBinder token) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActivityOptions(token);
                    }
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

            @Override // android.app.IActivityTaskManager
            public List<IBinder> getAppTasks(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAppTasks(callingPackage);
                    }
                    _reply.readException();
                    List<IBinder> _result = _reply.createBinderArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void startSystemLockTaskMode(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void stopSystemLockTaskMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopSystemLockTaskMode();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void finishVoiceTask(IVoiceInteractionSession session) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishVoiceTask(session);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean isTopOfTask(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void notifyLaunchTaskBehindComplete(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyLaunchTaskBehindComplete(token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void notifyEnterAnimationComplete(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyEnterAnimationComplete(token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int addAppTask(IBinder activityToken, Intent intent, ActivityManager.TaskDescription description, Bitmap thumbnail) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addAppTask(activityToken, intent, description, thumbnail);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public Point getAppTaskThumbnailSize() throws RemoteException {
                Point _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAppTaskThumbnailSize();
                    }
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

            @Override // android.app.IActivityTaskManager
            public boolean releaseActivityInstance(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().releaseActivityInstance(token);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public IBinder requestStartActivityPermissionToken(IBinder delegatorToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(delegatorToken);
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().requestStartActivityPermissionToken(delegatorToken);
                    }
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void releaseSomeActivities(IApplicationThread app) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app != null ? app.asBinder() : null);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().releaseSomeActivities(app);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public Bitmap getTaskDescriptionIcon(String filename, int userId) throws RemoteException {
                Bitmap _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(filename);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTaskDescriptionIcon(filename, userId);
                    }
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

            @Override // android.app.IActivityTaskManager
            public void startInPlaceAnimationOnFrontMostApplication(Bundle opts) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startInPlaceAnimationOnFrontMostApplication(opts);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(resizeableMode);
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void toggleFreeformWindowingMode(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().toggleFreeformWindowingMode(token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
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
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void moveStackToDisplay(int stackId, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    _data.writeInt(displayId);
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moveStackToDisplay(stackId, displayId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void removeStack(int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void setTaskWindowingMode(int taskId, int windowingMode, boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(windowingMode);
                    _data.writeInt(toTop ? 1 : 0);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTaskWindowingMode(taskId, windowingMode, toTop);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void moveTaskToStack(int taskId, int stackId, boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    _data.writeInt(toTop ? 1 : 0);
                    boolean _status = this.mRemote.transact(90, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
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
                    boolean _status = this.mRemote.transact(91, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean setTaskWindowingModeSplitScreenPrimary(int taskId, int createMode, boolean toTop, boolean animate, Rect initialBounds, boolean showRecents) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(taskId);
                    try {
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
                        try {
                            boolean _status = this.mRemote.transact(92, _data, _reply, 0);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                boolean taskWindowingModeSplitScreenPrimary = Stub.getDefaultImpl().setTaskWindowingModeSplitScreenPrimary(taskId, createMode, toTop, animate, initialBounds, showRecents);
                                _reply.recycle();
                                _data.recycle();
                                return taskWindowingModeSplitScreenPrimary;
                            }
                            _reply.readException();
                            boolean _result = _reply.readInt() != 0;
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
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityTaskManager
            public void offsetPinnedStackBounds(int stackId, Rect compareBounds, int xOffset, int yOffset, int animationDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    if (compareBounds != null) {
                        _data.writeInt(1);
                        compareBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(xOffset);
                    _data.writeInt(yOffset);
                    _data.writeInt(animationDuration);
                    boolean _status = this.mRemote.transact(93, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().offsetPinnedStackBounds(stackId, compareBounds, xOffset, yOffset, animationDuration);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void removeStacksInWindowingModes(int[] windowingModes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(windowingModes);
                    boolean _status = this.mRemote.transact(94, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeStacksInWindowingModes(windowingModes);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void removeStacksWithActivityTypes(int[] activityTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(activityTypes);
                    boolean _status = this.mRemote.transact(95, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeStacksWithActivityTypes(activityTypes);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public List<ActivityManager.StackInfo> getAllStackInfos() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(96, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public ActivityManager.StackInfo getStackInfo(int windowingMode, int activityType) throws RemoteException {
                ActivityManager.StackInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    _data.writeInt(activityType);
                    boolean _status = this.mRemote.transact(97, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStackInfo(windowingMode, activityType);
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

            @Override // android.app.IActivityTaskManager
            public void setLockScreenShown(boolean showingKeyguard, boolean showingAod) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    _data.writeInt(showingKeyguard ? 1 : 0);
                    if (!showingAod) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(98, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLockScreenShown(showingKeyguard, showingAod);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public Bundle getAssistContextExtras(int requestType) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestType);
                    boolean _status = this.mRemote.transact(99, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAssistContextExtras(requestType);
                    }
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

            @Override // android.app.IActivityTaskManager
            public boolean launchAssistIntent(Intent intent, int requestType, String hint, int userHandle, Bundle args) throws RemoteException {
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
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(requestType);
                    try {
                        _data.writeString(hint);
                        try {
                            _data.writeInt(userHandle);
                            if (args != null) {
                                _data.writeInt(1);
                                args.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
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
                        boolean _status = this.mRemote.transact(100, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            boolean launchAssistIntent = Stub.getDefaultImpl().launchAssistIntent(intent, requestType, hint, userHandle, args);
                            _reply.recycle();
                            _data.recycle();
                            return launchAssistIntent;
                        }
                        _reply.readException();
                        boolean _result = _reply.readInt() != 0;
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
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean requestAssistContextExtras(int requestType, IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, boolean focused, boolean newSessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(requestType);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    if (receiverExtras != null) {
                        _data.writeInt(1);
                        receiverExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeStrongBinder(activityToken);
                        _data.writeInt(focused ? 1 : 0);
                        _data.writeInt(newSessionId ? 1 : 0);
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
                    boolean _status = this.mRemote.transact(101, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean requestAssistContextExtras = Stub.getDefaultImpl().requestAssistContextExtras(requestType, receiver, receiverExtras, activityToken, focused, newSessionId);
                        _reply.recycle();
                        _data.recycle();
                        return requestAssistContextExtras;
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean requestAutofillData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(102, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().requestAutofillData(receiver, receiverExtras, activityToken, flags);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(103, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAssistDataAllowedOnCurrentActivity();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean showAssistFromActivity(IBinder token, Bundle args) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(104, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().showAssistFromActivity(token, args);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean isRootVoiceInteraction(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(105, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isRootVoiceInteraction(token);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void showLockTaskEscapeMessage(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(106, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().showLockTaskEscapeMessage(token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void keyguardGoingAway(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(107, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().keyguardGoingAway(flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public ComponentName getActivityClassForToken(IBinder token) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(108, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActivityClassForToken(token);
                    }
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

            @Override // android.app.IActivityTaskManager
            public String getPackageForToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(109, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageForToken(token);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void positionTaskInStack(int taskId, int stackId, int position) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    _data.writeInt(position);
                    boolean _status = this.mRemote.transact(110, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void reportSizeConfigurations(IBinder token, int[] horizontalSizeConfiguration, int[] verticalSizeConfigurations, int[] smallestWidthConfigurations) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeIntArray(horizontalSizeConfiguration);
                    _data.writeIntArray(verticalSizeConfigurations);
                    _data.writeIntArray(smallestWidthConfigurations);
                    boolean _status = this.mRemote.transact(111, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportSizeConfigurations(token, horizontalSizeConfiguration, verticalSizeConfigurations, smallestWidthConfigurations);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void dismissSplitScreenMode(boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(toTop ? 1 : 0);
                    boolean _status = this.mRemote.transact(112, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dismissSplitScreenMode(toTop);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void dismissPip(boolean animate, int animationDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(animate ? 1 : 0);
                    _data.writeInt(animationDuration);
                    boolean _status = this.mRemote.transact(113, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dismissPip(animate, animationDuration);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(suppress ? 1 : 0);
                    boolean _status = this.mRemote.transact(114, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void moveTasksToFullscreenStack(int fromStackId, boolean onTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fromStackId);
                    _data.writeInt(onTop ? 1 : 0);
                    boolean _status = this.mRemote.transact(115, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moveTasksToFullscreenStack(fromStackId, onTop);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
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
                    boolean _status = this.mRemote.transact(116, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean isInMultiWindowMode(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(117, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isInMultiWindowMode(token);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean isInPictureInPictureMode(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(118, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isInPictureInPictureMode(token);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean enterPictureInPictureMode(IBinder token, PictureInPictureParams params) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(119, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enterPictureInPictureMode(token, params);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setPictureInPictureParams(IBinder token, PictureInPictureParams params) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(120, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPictureInPictureParams(token, params);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int getMaxNumPictureInPictureActions(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(121, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMaxNumPictureInPictureActions(token);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public IBinder getUriPermissionOwnerForActivity(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    boolean _status = this.mRemote.transact(122, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUriPermissionOwnerForActivity(activityToken);
                    }
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
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
                    boolean _status = this.mRemote.transact(123, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void setSplitScreenResizing(boolean resizing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(resizing ? 1 : 0);
                    boolean _status = this.mRemote.transact(124, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSplitScreenResizing(resizing);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int setVrMode(IBinder token, boolean enabled, ComponentName packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(125, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setVrMode(token, enabled, packageName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void startLocalVoiceInteraction(IBinder token, Bundle options) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(126, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startLocalVoiceInteraction(token, options);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void stopLocalVoiceInteraction(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(127, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopLocalVoiceInteraction(token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean supportsLocalVoiceInteraction() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(128, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supportsLocalVoiceInteraction();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void notifyPinnedStackAnimationStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(129, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyPinnedStackAnimationStarted();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void notifyPinnedStackAnimationEnded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(130, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyPinnedStackAnimationEnded();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException {
                ConfigurationInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(131, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceConfigurationInfo();
                    }
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

            @Override // android.app.IActivityTaskManager
            public void resizePinnedStack(Rect pinnedBounds, Rect tempPinnedTaskBounds) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(132, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resizePinnedStack(pinnedBounds, tempPinnedTaskBounds);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public boolean updateDisplayOverrideConfiguration(Configuration values, int displayId) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(133, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateDisplayOverrideConfiguration(values, displayId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void dismissKeyguard(IBinder token, IKeyguardDismissCallback callback, CharSequence message) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(134, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dismissKeyguard(token, callback, message);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void cancelTaskWindowTransition(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(135, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public ActivityManager.TaskSnapshot getTaskSnapshot(int taskId, boolean reducedResolution) throws RemoteException {
                ActivityManager.TaskSnapshot _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(reducedResolution ? 1 : 0);
                    boolean _status = this.mRemote.transact(136, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void setDisablePreviewScreenshots(IBinder token, boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(disable ? 1 : 0);
                    boolean _status = this.mRemote.transact(137, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDisablePreviewScreenshots(token, disable);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int getLastResumedActivityUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(138, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastResumedActivityUserId();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
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
                    boolean _status = this.mRemote.transact(139, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void updateLockTaskFeatures(int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(140, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateLockTaskFeatures(userId, flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setShowWhenLocked(IBinder token, boolean showWhenLocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(showWhenLocked ? 1 : 0);
                    boolean _status = this.mRemote.transact(141, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setShowWhenLocked(token, showWhenLocked);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setInheritShowWhenLocked(IBinder token, boolean setInheritShownWhenLocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(setInheritShownWhenLocked ? 1 : 0);
                    boolean _status = this.mRemote.transact(142, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setInheritShowWhenLocked(token, setInheritShownWhenLocked);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setTurnScreenOn(IBinder token, boolean turnScreenOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(turnScreenOn ? 1 : 0);
                    boolean _status = this.mRemote.transact(143, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTurnScreenOn(token, turnScreenOn);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void registerRemoteAnimations(IBinder token, RemoteAnimationDefinition definition) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(144, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerRemoteAnimations(token, definition);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void registerRemoteAnimationForNextActivityStart(String packageName, RemoteAnimationAdapter adapter) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(145, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerRemoteAnimationForNextActivityStart(packageName, adapter);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void registerRemoteAnimationsForDisplay(int displayId, RemoteAnimationDefinition definition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (definition != null) {
                        _data.writeInt(1);
                        definition.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(146, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerRemoteAnimationsForDisplay(displayId, definition);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void alwaysShowUnsupportedCompileSdkWarning(ComponentName activity) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(147, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().alwaysShowUnsupportedCompileSdkWarning(activity);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setVrThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    boolean _status = this.mRemote.transact(148, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVrThread(tid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setPersistentVrThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    boolean _status = this.mRemote.transact(149, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void stopAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(150, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void resumeAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(151, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    _data.writeInt(imAMonkey ? 1 : 0);
                    boolean _status = this.mRemote.transact(152, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public void setVoiceKeepAwake(IVoiceInteractionSession session, boolean keepAwake) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(keepAwake ? 1 : 0);
                    boolean _status = this.mRemote.transact(153, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoiceKeepAwake(session, keepAwake);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public int getPackageScreenCompatMode(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(154, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageScreenCompatMode(packageName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(155, _data, _reply, 0);
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

            @Override // android.app.IActivityTaskManager
            public boolean getPackageAskScreenCompat(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(156, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageAskScreenCompat(packageName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setPackageAskScreenCompat(String packageName, boolean ask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(ask ? 1 : 0);
                    boolean _status = this.mRemote.transact(157, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPackageAskScreenCompat(packageName, ask);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void clearLaunchParamsForPackages(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    boolean _status = this.mRemote.transact(158, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearLaunchParamsForPackages(packageNames);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void setDisplayToSingleTaskInstance(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean _status = this.mRemote.transact(159, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDisplayToSingleTaskInstance(displayId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void restartActivityProcessIfVisible(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    boolean _status = this.mRemote.transact(160, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().restartActivityProcessIfVisible(activityToken);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void onBackPressedOnTaskRoot(IBinder activityToken, IRequestFinishCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(161, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBackPressedOnTaskRoot(activityToken, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void finishTaskActivity(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(162, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishTaskActivity(taskId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void finishPackageActivity(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(163, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishPackageActivity(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void performActivityChanged(Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(164, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().performActivityChanged(extras);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IActivityTaskManager
            public void performTopActivityChanged(Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(165, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().performTopActivityChanged(extras);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IActivityTaskManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IActivityTaskManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
