package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Trace;
import com.android.internal.content.ReferrerIntent;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class NewIntentItem extends ClientTransactionItem {
    public static final Parcelable.Creator<NewIntentItem> CREATOR = new Parcelable.Creator<NewIntentItem>() { // from class: android.app.servertransaction.NewIntentItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NewIntentItem createFromParcel(Parcel in) {
            return new NewIntentItem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NewIntentItem[] newArray(int size) {
            return new NewIntentItem[size];
        }
    };
    public protected List<ReferrerIntent> mIntents;
    private boolean mPause;

    @Override // android.app.servertransaction.BaseClientRequest
    public synchronized void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        Trace.traceBegin(64L, "activityNewIntent");
        client.handleNewIntent(token, this.mIntents, this.mPause);
        Trace.traceEnd(64L);
    }

    private synchronized NewIntentItem() {
    }

    public static synchronized NewIntentItem obtain(List<ReferrerIntent> intents, boolean pause) {
        NewIntentItem instance = (NewIntentItem) ObjectPool.obtain(NewIntentItem.class);
        if (instance == null) {
            instance = new NewIntentItem();
        }
        instance.mIntents = intents;
        instance.mPause = pause;
        return instance;
    }

    @Override // android.app.servertransaction.ObjectPoolItem
    public synchronized void recycle() {
        this.mIntents = null;
        this.mPause = false;
        ObjectPool.recycle(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.mPause);
        dest.writeTypedList(this.mIntents, flags);
    }

    private synchronized NewIntentItem(Parcel in) {
        this.mPause = in.readBoolean();
        this.mIntents = in.createTypedArrayList(ReferrerIntent.CREATOR);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewIntentItem other = (NewIntentItem) o;
        if (this.mPause == other.mPause && Objects.equals(this.mIntents, other.mIntents)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (31 * 17) + (this.mPause ? 1 : 0);
        return (31 * result) + this.mIntents.hashCode();
    }

    public String toString() {
        return "NewIntentItem{pause=" + this.mPause + ",intents=" + this.mIntents + "}";
    }
}
