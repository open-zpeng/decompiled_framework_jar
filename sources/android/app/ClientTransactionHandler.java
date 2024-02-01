package android.app;

import android.app.ActivityThread;
import android.app.servertransaction.ClientTransaction;
import android.app.servertransaction.PendingTransactionActions;
import android.app.servertransaction.TransactionExecutor;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.MergedConfiguration;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.ReferrerIntent;
import java.util.List;
/* loaded from: classes.dex */
public abstract class ClientTransactionHandler {
    public abstract synchronized ActivityThread.ActivityClientRecord getActivityClient(IBinder iBinder);

    public abstract synchronized LoadedApk getPackageInfoNoCheck(ApplicationInfo applicationInfo, CompatibilityInfo compatibilityInfo);

    abstract synchronized TransactionExecutor getTransactionExecutor();

    public abstract synchronized void handleActivityConfigurationChanged(IBinder iBinder, Configuration configuration, int i);

    public abstract synchronized void handleConfigurationChanged(Configuration configuration);

    public abstract synchronized void handleDestroyActivity(IBinder iBinder, boolean z, int i, boolean z2, String str);

    public abstract synchronized Activity handleLaunchActivity(ActivityThread.ActivityClientRecord activityClientRecord, PendingTransactionActions pendingTransactionActions, Intent intent);

    public abstract synchronized void handleMultiWindowModeChanged(IBinder iBinder, boolean z, Configuration configuration);

    public abstract synchronized void handleNewIntent(IBinder iBinder, List<ReferrerIntent> list, boolean z);

    public abstract void handlePauseActivity(IBinder iBinder, boolean z, boolean z2, int i, int i2, PendingTransactionActions pendingTransactionActions, String str);

    public abstract synchronized void handlePictureInPictureModeChanged(IBinder iBinder, boolean z, Configuration configuration);

    public abstract synchronized void handleRelaunchActivity(ActivityThread.ActivityClientRecord activityClientRecord, PendingTransactionActions pendingTransactionActions);

    public abstract synchronized void handleResumeActivity(IBinder iBinder, boolean z, boolean z2, String str);

    public abstract synchronized void handleSendResult(IBinder iBinder, List<ResultInfo> list, String str);

    public abstract synchronized void handleStartActivity(ActivityThread.ActivityClientRecord activityClientRecord, PendingTransactionActions pendingTransactionActions);

    public abstract synchronized void handleStopActivity(IBinder iBinder, boolean z, int i, PendingTransactionActions pendingTransactionActions, boolean z2, String str);

    public abstract synchronized void handleWindowVisibility(IBinder iBinder, boolean z);

    public abstract synchronized void performRestartActivity(IBinder iBinder, boolean z);

    public abstract synchronized ActivityThread.ActivityClientRecord prepareRelaunchActivity(IBinder iBinder, List<ResultInfo> list, List<ReferrerIntent> list2, int i, MergedConfiguration mergedConfiguration, boolean z);

    public abstract synchronized void reportRelaunch(IBinder iBinder, PendingTransactionActions pendingTransactionActions);

    public abstract synchronized void reportStop(PendingTransactionActions pendingTransactionActions);

    abstract synchronized void sendMessage(int i, Object obj);

    public abstract synchronized void updatePendingConfiguration(Configuration configuration);

    public abstract synchronized void updateProcessState(int i, boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void scheduleTransaction(ClientTransaction transaction) {
        transaction.preExecute(this);
        sendMessage(159, transaction);
    }

    @VisibleForTesting
    public synchronized void executeTransaction(ClientTransaction transaction) {
        transaction.preExecute(this);
        getTransactionExecutor().execute(transaction);
        transaction.recycle();
    }
}
