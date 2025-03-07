package android.app.servertransaction;

import android.app.ActivityTaskManager;
import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.Trace;

/* loaded from: classes.dex */
public class TopResumedActivityChangeItem extends ClientTransactionItem {
    public static final Parcelable.Creator<TopResumedActivityChangeItem> CREATOR = new Parcelable.Creator<TopResumedActivityChangeItem>() { // from class: android.app.servertransaction.TopResumedActivityChangeItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TopResumedActivityChangeItem createFromParcel(Parcel in) {
            return new TopResumedActivityChangeItem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TopResumedActivityChangeItem[] newArray(int size) {
            return new TopResumedActivityChangeItem[size];
        }
    };
    private boolean mOnTop;

    @Override // android.app.servertransaction.BaseClientRequest
    public void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        Trace.traceBegin(64L, "topResumedActivityChangeItem");
        client.handleTopResumedActivityChanged(token, this.mOnTop, "topResumedActivityChangeItem");
        Trace.traceEnd(64L);
    }

    @Override // android.app.servertransaction.BaseClientRequest
    public void postExecute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        if (this.mOnTop) {
            return;
        }
        try {
            ActivityTaskManager.getService().activityTopResumedStateLost();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private TopResumedActivityChangeItem() {
    }

    public static TopResumedActivityChangeItem obtain(boolean onTop) {
        TopResumedActivityChangeItem instance = (TopResumedActivityChangeItem) ObjectPool.obtain(TopResumedActivityChangeItem.class);
        if (instance == null) {
            instance = new TopResumedActivityChangeItem();
        }
        instance.mOnTop = onTop;
        return instance;
    }

    @Override // android.app.servertransaction.ObjectPoolItem
    public void recycle() {
        this.mOnTop = false;
        ObjectPool.recycle(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.mOnTop);
    }

    private TopResumedActivityChangeItem(Parcel in) {
        this.mOnTop = in.readBoolean();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TopResumedActivityChangeItem other = (TopResumedActivityChangeItem) o;
        if (this.mOnTop == other.mOnTop) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (17 * 31) + (this.mOnTop ? 1 : 0);
        return result;
    }

    public String toString() {
        return "TopResumedActivityChangeItem{onTop=" + this.mOnTop + "}";
    }
}
