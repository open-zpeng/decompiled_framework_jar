package android.app;

import android.content.ComponentName;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.service.voice.IVoiceInteractionSession;
import android.util.SparseIntArray;
import com.android.internal.app.IVoiceInteractor;
import java.util.List;
/* loaded from: classes.dex */
public abstract class ActivityManagerInternal {
    public static final int APP_TRANSITION_RECENTS_ANIM = 5;
    public static final int APP_TRANSITION_SNAPSHOT = 4;
    public static final int APP_TRANSITION_SPLASH_SCREEN = 1;
    public static final int APP_TRANSITION_TIMEOUT = 3;
    public static final int APP_TRANSITION_WINDOWS_DRAWN = 2;
    public static final String ASSIST_KEY_CONTENT = "content";
    public static final String ASSIST_KEY_DATA = "data";
    public static final String ASSIST_KEY_RECEIVER_EXTRAS = "receiverExtras";
    public static final String ASSIST_KEY_STRUCTURE = "structure";

    /* loaded from: classes.dex */
    public interface ScreenObserver {
        synchronized void onAwakeStateChanged(boolean z);

        synchronized void onKeyguardStateChanged(boolean z);
    }

    /* loaded from: classes.dex */
    public static abstract class SleepToken {
        public abstract synchronized void release();
    }

    public abstract synchronized SleepToken acquireSleepToken(String str, int i);

    public abstract synchronized boolean canStartMoreUsers();

    public abstract synchronized void cancelRecentsAnimation(boolean z);

    public abstract synchronized String checkContentProviderAccess(String str, int i);

    public abstract synchronized void clearSavedANRState();

    public abstract synchronized void enforceCallerIsRecentsOrHasPermission(String str, String str2);

    public abstract synchronized ComponentName getHomeActivityForUser(int i);

    public abstract Intent getHomeIntent();

    public abstract synchronized int getMaxRunningUsers();

    public abstract synchronized List<ProcessMemoryState> getMemoryStateForProcesses();

    public abstract synchronized List<IBinder> getTopVisibleActivities();

    public abstract synchronized int getUidProcessState(int i);

    public abstract synchronized void grantUriPermissionFromIntent(int i, String str, Intent intent, int i2);

    public abstract synchronized boolean hasRunningActivity(int i, String str);

    public abstract synchronized boolean isCallerRecents(int i);

    public abstract synchronized boolean isRecentsComponentHomeActivity(int i);

    public abstract synchronized boolean isRuntimeRestarted();

    public abstract synchronized boolean isSystemReady();

    public abstract synchronized boolean isUidActive(int i);

    public abstract synchronized void killForegroundAppsForUser(int i);

    public abstract synchronized void notifyActiveVoiceInteractionServiceChanged(ComponentName componentName);

    public abstract synchronized void notifyAppTransitionCancelled();

    public abstract synchronized void notifyAppTransitionFinished();

    public abstract synchronized void notifyAppTransitionStarting(SparseIntArray sparseIntArray, long j);

    public abstract void notifyDefaultDisplaySizeChanged();

    public abstract synchronized void notifyDockedStackMinimizedChanged(boolean z);

    public abstract synchronized void notifyKeyguardFlagsChanged(Runnable runnable);

    public abstract synchronized void notifyKeyguardTrustedChanged();

    public abstract synchronized void notifyNetworkPolicyRulesUpdated(int i, long j);

    public abstract synchronized void onLocalVoiceInteractionStarted(IBinder iBinder, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor);

    public abstract synchronized void onUserRemoved(int i);

    public abstract synchronized void onWakefulnessChanged(int i);

    public abstract synchronized void registerScreenObserver(ScreenObserver screenObserver);

    public abstract synchronized void saveANRState(String str);

    public abstract synchronized void setAllowAppSwitches(String str, int i, int i2);

    public abstract synchronized void setDeviceIdleWhitelist(int[] iArr, int[] iArr2);

    public abstract synchronized void setFocusedActivity(IBinder iBinder);

    public abstract synchronized void setHasOverlayUi(int i, boolean z);

    public abstract synchronized void setPendingIntentWhitelistDuration(IIntentSender iIntentSender, IBinder iBinder, long j);

    public abstract synchronized void setRunningRemoteAnimation(int i, boolean z);

    public abstract synchronized void setSwitchingFromSystemUserMessage(String str);

    public abstract synchronized void setSwitchingToSystemUserMessage(String str);

    public abstract synchronized void setVr2dDisplayId(int i);

    public abstract synchronized int startActivitiesAsPackage(String str, int i, Intent[] intentArr, Bundle bundle);

    public abstract synchronized int startActivityAsUser(IApplicationThread iApplicationThread, String str, Intent intent, Bundle bundle, int i);

    public abstract synchronized boolean startIsolatedProcess(String str, String[] strArr, String str2, String str3, int i, Runnable runnable);

    public abstract synchronized void updateDeviceIdleTempWhitelist(int[] iArr, int i, boolean z);

    public abstract synchronized void updatePersistentConfigurationForUser(Configuration configuration, int i);
}
