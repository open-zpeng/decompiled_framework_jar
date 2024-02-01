package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Trace;
/* loaded from: classes.dex */
public class WindowVisibilityItem extends ClientTransactionItem {
    public static final Parcelable.Creator<WindowVisibilityItem> CREATOR = new Parcelable.Creator<WindowVisibilityItem>() { // from class: android.app.servertransaction.WindowVisibilityItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WindowVisibilityItem createFromParcel(Parcel in) {
            return new WindowVisibilityItem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WindowVisibilityItem[] newArray(int size) {
            return new WindowVisibilityItem[size];
        }
    };
    private boolean mShowWindow;

    @Override // android.app.servertransaction.BaseClientRequest
    public synchronized void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        Trace.traceBegin(64L, "activityShowWindow");
        client.handleWindowVisibility(token, this.mShowWindow);
        Trace.traceEnd(64L);
    }

    private synchronized WindowVisibilityItem() {
    }

    public static synchronized WindowVisibilityItem obtain(boolean showWindow) {
        WindowVisibilityItem instance = (WindowVisibilityItem) ObjectPool.obtain(WindowVisibilityItem.class);
        if (instance == null) {
            instance = new WindowVisibilityItem();
        }
        instance.mShowWindow = showWindow;
        return instance;
    }

    @Override // android.app.servertransaction.ObjectPoolItem
    public synchronized void recycle() {
        this.mShowWindow = false;
        ObjectPool.recycle(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.mShowWindow);
    }

    private synchronized WindowVisibilityItem(Parcel in) {
        this.mShowWindow = in.readBoolean();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WindowVisibilityItem other = (WindowVisibilityItem) o;
        if (this.mShowWindow == other.mShowWindow) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 17 + (31 * (this.mShowWindow ? 1 : 0));
    }

    public String toString() {
        return "WindowVisibilityItem{showWindow=" + this.mShowWindow + "}";
    }
}
