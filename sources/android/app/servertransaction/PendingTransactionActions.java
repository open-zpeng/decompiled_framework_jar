package android.app.servertransaction;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;
import android.util.Log;
import android.util.LogWriter;
import com.android.internal.util.IndentingPrintWriter;
/* loaded from: classes.dex */
public class PendingTransactionActions {
    private boolean mCallOnPostCreate;
    private Bundle mOldState;
    private boolean mReportRelaunchToWM;
    private boolean mRestoreInstanceState;
    private StopInfo mStopInfo;

    public synchronized PendingTransactionActions() {
        clear();
    }

    public synchronized void clear() {
        this.mRestoreInstanceState = false;
        this.mCallOnPostCreate = false;
        this.mOldState = null;
        this.mStopInfo = null;
    }

    public synchronized boolean shouldRestoreInstanceState() {
        return this.mRestoreInstanceState;
    }

    public synchronized void setRestoreInstanceState(boolean restoreInstanceState) {
        this.mRestoreInstanceState = restoreInstanceState;
    }

    public synchronized boolean shouldCallOnPostCreate() {
        return this.mCallOnPostCreate;
    }

    public synchronized void setCallOnPostCreate(boolean callOnPostCreate) {
        this.mCallOnPostCreate = callOnPostCreate;
    }

    public synchronized Bundle getOldState() {
        return this.mOldState;
    }

    public synchronized void setOldState(Bundle oldState) {
        this.mOldState = oldState;
    }

    public synchronized StopInfo getStopInfo() {
        return this.mStopInfo;
    }

    public synchronized void setStopInfo(StopInfo stopInfo) {
        this.mStopInfo = stopInfo;
    }

    public synchronized boolean shouldReportRelaunchToWindowManager() {
        return this.mReportRelaunchToWM;
    }

    public synchronized void setReportRelaunchToWindowManager(boolean reportToWm) {
        this.mReportRelaunchToWM = reportToWm;
    }

    /* loaded from: classes.dex */
    public static class StopInfo implements Runnable {
        private static final String TAG = "ActivityStopInfo";
        private ActivityThread.ActivityClientRecord mActivity;
        private CharSequence mDescription;
        private PersistableBundle mPersistentState;
        private Bundle mState;

        public synchronized void setActivity(ActivityThread.ActivityClientRecord activity) {
            this.mActivity = activity;
        }

        public synchronized void setState(Bundle state) {
            this.mState = state;
        }

        public synchronized void setPersistentState(PersistableBundle persistentState) {
            this.mPersistentState = persistentState;
        }

        public synchronized void setDescription(CharSequence description) {
            this.mDescription = description;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ActivityManager.getService().activityStopped(this.mActivity.token, this.mState, this.mPersistentState, this.mDescription);
            } catch (RemoteException ex) {
                LogWriter writer = new LogWriter(5, TAG);
                IndentingPrintWriter pw = new IndentingPrintWriter(writer, "  ");
                pw.println("Bundle stats:");
                Bundle.dumpStats(pw, this.mState);
                pw.println("PersistableBundle stats:");
                Bundle.dumpStats(pw, this.mPersistentState);
                if ((ex instanceof TransactionTooLargeException) && this.mActivity.packageInfo.getTargetSdkVersion() < 24) {
                    Log.e(TAG, "App sent too much data in instance state, so it was ignored", ex);
                    return;
                }
                throw ex.rethrowFromSystemServer();
            }
        }
    }
}
