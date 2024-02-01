package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Trace;
import java.util.Objects;

/* loaded from: classes.dex */
public class MoveToDisplayItem extends ClientTransactionItem {
    public static final Parcelable.Creator<MoveToDisplayItem> CREATOR = new Parcelable.Creator<MoveToDisplayItem>() { // from class: android.app.servertransaction.MoveToDisplayItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MoveToDisplayItem createFromParcel(Parcel in) {
            return new MoveToDisplayItem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MoveToDisplayItem[] newArray(int size) {
            return new MoveToDisplayItem[size];
        }
    };
    private Configuration mConfiguration;
    private int mTargetDisplayId;

    @Override // android.app.servertransaction.BaseClientRequest
    public void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        Trace.traceBegin(64L, "activityMovedToDisplay");
        client.handleActivityConfigurationChanged(token, this.mConfiguration, this.mTargetDisplayId);
        Trace.traceEnd(64L);
    }

    private MoveToDisplayItem() {
    }

    public static MoveToDisplayItem obtain(int targetDisplayId, Configuration configuration) {
        MoveToDisplayItem instance = (MoveToDisplayItem) ObjectPool.obtain(MoveToDisplayItem.class);
        if (instance == null) {
            instance = new MoveToDisplayItem();
        }
        instance.mTargetDisplayId = targetDisplayId;
        instance.mConfiguration = configuration;
        return instance;
    }

    @Override // android.app.servertransaction.ObjectPoolItem
    public void recycle() {
        this.mTargetDisplayId = 0;
        this.mConfiguration = null;
        ObjectPool.recycle(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mTargetDisplayId);
        dest.writeTypedObject(this.mConfiguration, flags);
    }

    private MoveToDisplayItem(Parcel in) {
        this.mTargetDisplayId = in.readInt();
        this.mConfiguration = (Configuration) in.readTypedObject(Configuration.CREATOR);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoveToDisplayItem other = (MoveToDisplayItem) o;
        if (this.mTargetDisplayId == other.mTargetDisplayId && Objects.equals(this.mConfiguration, other.mConfiguration)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (17 * 31) + this.mTargetDisplayId;
        return (result * 31) + this.mConfiguration.hashCode();
    }

    public String toString() {
        return "MoveToDisplayItem{targetDisplayId=" + this.mTargetDisplayId + ",configuration=" + this.mConfiguration + "}";
    }
}
