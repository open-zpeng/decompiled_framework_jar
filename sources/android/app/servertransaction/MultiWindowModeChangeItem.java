package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* loaded from: classes.dex */
public class MultiWindowModeChangeItem extends ClientTransactionItem {
    public static final Parcelable.Creator<MultiWindowModeChangeItem> CREATOR = new Parcelable.Creator<MultiWindowModeChangeItem>() { // from class: android.app.servertransaction.MultiWindowModeChangeItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MultiWindowModeChangeItem createFromParcel(Parcel in) {
            return new MultiWindowModeChangeItem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MultiWindowModeChangeItem[] newArray(int size) {
            return new MultiWindowModeChangeItem[size];
        }
    };
    private boolean mIsInMultiWindowMode;
    private Configuration mOverrideConfig;

    @Override // android.app.servertransaction.BaseClientRequest
    public void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        client.handleMultiWindowModeChanged(token, this.mIsInMultiWindowMode, this.mOverrideConfig);
    }

    private MultiWindowModeChangeItem() {
    }

    public static MultiWindowModeChangeItem obtain(boolean isInMultiWindowMode, Configuration overrideConfig) {
        MultiWindowModeChangeItem instance = (MultiWindowModeChangeItem) ObjectPool.obtain(MultiWindowModeChangeItem.class);
        if (instance == null) {
            instance = new MultiWindowModeChangeItem();
        }
        instance.mIsInMultiWindowMode = isInMultiWindowMode;
        instance.mOverrideConfig = overrideConfig;
        return instance;
    }

    @Override // android.app.servertransaction.ObjectPoolItem
    public void recycle() {
        this.mIsInMultiWindowMode = false;
        this.mOverrideConfig = null;
        ObjectPool.recycle(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.mIsInMultiWindowMode);
        dest.writeTypedObject(this.mOverrideConfig, flags);
    }

    private MultiWindowModeChangeItem(Parcel in) {
        this.mIsInMultiWindowMode = in.readBoolean();
        this.mOverrideConfig = (Configuration) in.readTypedObject(Configuration.CREATOR);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultiWindowModeChangeItem other = (MultiWindowModeChangeItem) o;
        if (this.mIsInMultiWindowMode == other.mIsInMultiWindowMode && Objects.equals(this.mOverrideConfig, other.mOverrideConfig)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (17 * 31) + (this.mIsInMultiWindowMode ? 1 : 0);
        return (result * 31) + this.mOverrideConfig.hashCode();
    }

    public String toString() {
        return "MultiWindowModeChangeItem{isInMultiWindowMode=" + this.mIsInMultiWindowMode + ",overrideConfig=" + this.mOverrideConfig + "}";
    }
}
