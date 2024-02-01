package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* loaded from: classes.dex */
public class PipModeChangeItem extends ClientTransactionItem {
    public static final Parcelable.Creator<PipModeChangeItem> CREATOR = new Parcelable.Creator<PipModeChangeItem>() { // from class: android.app.servertransaction.PipModeChangeItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PipModeChangeItem createFromParcel(Parcel in) {
            return new PipModeChangeItem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PipModeChangeItem[] newArray(int size) {
            return new PipModeChangeItem[size];
        }
    };
    private boolean mIsInPipMode;
    private Configuration mOverrideConfig;

    @Override // android.app.servertransaction.BaseClientRequest
    public void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        client.handlePictureInPictureModeChanged(token, this.mIsInPipMode, this.mOverrideConfig);
    }

    private PipModeChangeItem() {
    }

    public static PipModeChangeItem obtain(boolean isInPipMode, Configuration overrideConfig) {
        PipModeChangeItem instance = (PipModeChangeItem) ObjectPool.obtain(PipModeChangeItem.class);
        if (instance == null) {
            instance = new PipModeChangeItem();
        }
        instance.mIsInPipMode = isInPipMode;
        instance.mOverrideConfig = overrideConfig;
        return instance;
    }

    @Override // android.app.servertransaction.ObjectPoolItem
    public void recycle() {
        this.mIsInPipMode = false;
        this.mOverrideConfig = null;
        ObjectPool.recycle(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.mIsInPipMode);
        dest.writeTypedObject(this.mOverrideConfig, flags);
    }

    private PipModeChangeItem(Parcel in) {
        this.mIsInPipMode = in.readBoolean();
        this.mOverrideConfig = (Configuration) in.readTypedObject(Configuration.CREATOR);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PipModeChangeItem other = (PipModeChangeItem) o;
        if (this.mIsInPipMode == other.mIsInPipMode && Objects.equals(this.mOverrideConfig, other.mOverrideConfig)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (17 * 31) + (this.mIsInPipMode ? 1 : 0);
        return (result * 31) + this.mOverrideConfig.hashCode();
    }

    public String toString() {
        return "PipModeChangeItem{isInPipMode=" + this.mIsInPipMode + ",overrideConfig=" + this.mOverrideConfig + "}";
    }
}
