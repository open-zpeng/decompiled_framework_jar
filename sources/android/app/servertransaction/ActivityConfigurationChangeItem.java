package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Trace;
import java.util.Objects;
/* loaded from: classes.dex */
public class ActivityConfigurationChangeItem extends ClientTransactionItem {
    public static final Parcelable.Creator<ActivityConfigurationChangeItem> CREATOR = new Parcelable.Creator<ActivityConfigurationChangeItem>() { // from class: android.app.servertransaction.ActivityConfigurationChangeItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityConfigurationChangeItem createFromParcel(Parcel in) {
            return new ActivityConfigurationChangeItem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityConfigurationChangeItem[] newArray(int size) {
            return new ActivityConfigurationChangeItem[size];
        }
    };
    private Configuration mConfiguration;

    @Override // android.app.servertransaction.BaseClientRequest
    public synchronized void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        Trace.traceBegin(64L, "activityConfigChanged");
        client.handleActivityConfigurationChanged(token, this.mConfiguration, -1);
        Trace.traceEnd(64L);
    }

    private synchronized ActivityConfigurationChangeItem() {
    }

    public static synchronized ActivityConfigurationChangeItem obtain(Configuration config) {
        ActivityConfigurationChangeItem instance = (ActivityConfigurationChangeItem) ObjectPool.obtain(ActivityConfigurationChangeItem.class);
        if (instance == null) {
            instance = new ActivityConfigurationChangeItem();
        }
        instance.mConfiguration = config;
        return instance;
    }

    @Override // android.app.servertransaction.ObjectPoolItem
    public synchronized void recycle() {
        this.mConfiguration = null;
        ObjectPool.recycle(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(this.mConfiguration, flags);
    }

    private synchronized ActivityConfigurationChangeItem(Parcel in) {
        this.mConfiguration = (Configuration) in.readTypedObject(Configuration.CREATOR);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActivityConfigurationChangeItem other = (ActivityConfigurationChangeItem) o;
        return Objects.equals(this.mConfiguration, other.mConfiguration);
    }

    public int hashCode() {
        return this.mConfiguration.hashCode();
    }

    public String toString() {
        return "ActivityConfigurationChange{config=" + this.mConfiguration + "}";
    }
}
