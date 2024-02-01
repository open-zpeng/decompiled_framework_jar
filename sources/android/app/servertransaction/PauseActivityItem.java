package android.app.servertransaction;

import android.app.ActivityManager;
import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.Trace;
/* loaded from: classes.dex */
public class PauseActivityItem extends ActivityLifecycleItem {
    public static final Parcelable.Creator<PauseActivityItem> CREATOR = new Parcelable.Creator<PauseActivityItem>() { // from class: android.app.servertransaction.PauseActivityItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PauseActivityItem createFromParcel(Parcel in) {
            return new PauseActivityItem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PauseActivityItem[] newArray(int size) {
            return new PauseActivityItem[size];
        }
    };
    private static final String TAG = "PauseActivityItem";
    private int mConfigChanges;
    private boolean mDontReport;
    private boolean mFinished;
    private int mResumingFlags;
    private boolean mUserLeaving;

    @Override // android.app.servertransaction.BaseClientRequest
    public synchronized void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        Trace.traceBegin(64L, "activityPause");
        client.handlePauseActivity(token, this.mFinished, this.mUserLeaving, this.mConfigChanges, this.mResumingFlags, pendingActions, "PAUSE_ACTIVITY_ITEM");
        Trace.traceEnd(64L);
    }

    @Override // android.app.servertransaction.ActivityLifecycleItem
    public synchronized int getTargetState() {
        return 4;
    }

    @Override // android.app.servertransaction.BaseClientRequest
    public synchronized void postExecute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        if (this.mDontReport) {
            return;
        }
        try {
            ActivityManager.getService().activityPaused(token);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private synchronized PauseActivityItem() {
    }

    public static synchronized PauseActivityItem obtain(boolean finished, boolean userLeaving, int configChanges, boolean dontReport) {
        PauseActivityItem instance = (PauseActivityItem) ObjectPool.obtain(PauseActivityItem.class);
        if (instance == null) {
            instance = new PauseActivityItem();
        }
        instance.mFinished = finished;
        instance.mUserLeaving = userLeaving;
        instance.mConfigChanges = configChanges;
        instance.mDontReport = dontReport;
        return instance;
    }

    public static PauseActivityItem obtain(boolean finished, boolean userLeaving, int configChanges, boolean dontReport, int resumingFlags) {
        PauseActivityItem instance = (PauseActivityItem) ObjectPool.obtain(PauseActivityItem.class);
        if (instance == null) {
            instance = new PauseActivityItem();
        }
        instance.mFinished = finished;
        instance.mUserLeaving = userLeaving;
        instance.mConfigChanges = configChanges;
        instance.mDontReport = dontReport;
        instance.mResumingFlags = resumingFlags;
        return instance;
    }

    public static synchronized PauseActivityItem obtain() {
        PauseActivityItem instance = (PauseActivityItem) ObjectPool.obtain(PauseActivityItem.class);
        if (instance == null) {
            instance = new PauseActivityItem();
        }
        instance.mFinished = false;
        instance.mUserLeaving = false;
        instance.mConfigChanges = 0;
        instance.mDontReport = true;
        instance.mResumingFlags = 0;
        return instance;
    }

    @Override // android.app.servertransaction.ActivityLifecycleItem, android.app.servertransaction.ObjectPoolItem
    public synchronized void recycle() {
        super.recycle();
        this.mFinished = false;
        this.mUserLeaving = false;
        this.mConfigChanges = 0;
        this.mDontReport = false;
        this.mResumingFlags = 0;
        ObjectPool.recycle(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.mFinished);
        dest.writeBoolean(this.mUserLeaving);
        dest.writeInt(this.mConfigChanges);
        dest.writeBoolean(this.mDontReport);
        dest.writeInt(this.mResumingFlags);
    }

    private synchronized PauseActivityItem(Parcel in) {
        this.mFinished = in.readBoolean();
        this.mUserLeaving = in.readBoolean();
        this.mConfigChanges = in.readInt();
        this.mDontReport = in.readBoolean();
        this.mResumingFlags = in.readInt();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PauseActivityItem other = (PauseActivityItem) o;
        if (this.mFinished == other.mFinished && this.mUserLeaving == other.mUserLeaving && this.mConfigChanges == other.mConfigChanges && this.mDontReport == other.mDontReport) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (31 * 17) + (this.mFinished ? 1 : 0);
        return (31 * ((31 * ((31 * result) + (this.mUserLeaving ? 1 : 0))) + this.mConfigChanges)) + (this.mDontReport ? 1 : 0);
    }

    public String toString() {
        return "PauseActivityItem{finished=" + this.mFinished + ",userLeaving=" + this.mUserLeaving + ",configChanges=" + this.mConfigChanges + ",dontReport=" + this.mDontReport + "}";
    }
}
