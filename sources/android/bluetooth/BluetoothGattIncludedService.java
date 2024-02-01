package android.bluetooth;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import java.util.UUID;
/* loaded from: classes.dex */
public class BluetoothGattIncludedService implements Parcelable {
    public static final Parcelable.Creator<BluetoothGattIncludedService> CREATOR = new Parcelable.Creator<BluetoothGattIncludedService>() { // from class: android.bluetooth.BluetoothGattIncludedService.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothGattIncludedService createFromParcel(Parcel in) {
            return new BluetoothGattIncludedService(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothGattIncludedService[] newArray(int size) {
            return new BluetoothGattIncludedService[size];
        }
    };
    protected int mInstanceId;
    protected int mServiceType;
    protected UUID mUuid;

    public synchronized BluetoothGattIncludedService(UUID uuid, int instanceId, int serviceType) {
        this.mUuid = uuid;
        this.mInstanceId = instanceId;
        this.mServiceType = serviceType;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(new ParcelUuid(this.mUuid), 0);
        out.writeInt(this.mInstanceId);
        out.writeInt(this.mServiceType);
    }

    private synchronized BluetoothGattIncludedService(Parcel in) {
        this.mUuid = ((ParcelUuid) in.readParcelable(null)).getUuid();
        this.mInstanceId = in.readInt();
        this.mServiceType = in.readInt();
    }

    public synchronized UUID getUuid() {
        return this.mUuid;
    }

    public synchronized int getInstanceId() {
        return this.mInstanceId;
    }

    public synchronized int getType() {
        return this.mServiceType;
    }
}
