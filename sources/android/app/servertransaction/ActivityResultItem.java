package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.app.ResultInfo;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Trace;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class ActivityResultItem extends ClientTransactionItem {
    public static final Parcelable.Creator<ActivityResultItem> CREATOR = new Parcelable.Creator<ActivityResultItem>() { // from class: android.app.servertransaction.ActivityResultItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityResultItem createFromParcel(Parcel in) {
            return new ActivityResultItem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityResultItem[] newArray(int size) {
            return new ActivityResultItem[size];
        }
    };
    public protected List<ResultInfo> mResultInfoList;

    @Override // android.app.servertransaction.BaseClientRequest
    public synchronized void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        Trace.traceBegin(64L, "activityDeliverResult");
        client.handleSendResult(token, this.mResultInfoList, "ACTIVITY_RESULT");
        Trace.traceEnd(64L);
    }

    private synchronized ActivityResultItem() {
    }

    public static synchronized ActivityResultItem obtain(List<ResultInfo> resultInfoList) {
        ActivityResultItem instance = (ActivityResultItem) ObjectPool.obtain(ActivityResultItem.class);
        if (instance == null) {
            instance = new ActivityResultItem();
        }
        instance.mResultInfoList = resultInfoList;
        return instance;
    }

    @Override // android.app.servertransaction.ObjectPoolItem
    public synchronized void recycle() {
        this.mResultInfoList = null;
        ObjectPool.recycle(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mResultInfoList, flags);
    }

    private synchronized ActivityResultItem(Parcel in) {
        this.mResultInfoList = in.createTypedArrayList(ResultInfo.CREATOR);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActivityResultItem other = (ActivityResultItem) o;
        return Objects.equals(this.mResultInfoList, other.mResultInfoList);
    }

    public int hashCode() {
        return this.mResultInfoList.hashCode();
    }

    public String toString() {
        return "ActivityResultItem{resultInfoList=" + this.mResultInfoList + "}";
    }
}
