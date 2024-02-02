package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class OobData implements Parcelable {
    public static final Parcelable.Creator<OobData> CREATOR = new Parcelable.Creator<OobData>() { // from class: android.bluetooth.OobData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OobData createFromParcel(Parcel in) {
            return new OobData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OobData[] newArray(int size) {
            return new OobData[size];
        }
    };
    private byte[] mLeBluetoothDeviceAddress;
    private byte[] mLeSecureConnectionsConfirmation;
    private byte[] mLeSecureConnectionsRandom;
    private byte[] mSecurityManagerTk;

    public synchronized byte[] getLeBluetoothDeviceAddress() {
        return this.mLeBluetoothDeviceAddress;
    }

    public synchronized void setLeBluetoothDeviceAddress(byte[] leBluetoothDeviceAddress) {
        this.mLeBluetoothDeviceAddress = leBluetoothDeviceAddress;
    }

    public synchronized byte[] getSecurityManagerTk() {
        return this.mSecurityManagerTk;
    }

    public synchronized void setSecurityManagerTk(byte[] securityManagerTk) {
        this.mSecurityManagerTk = securityManagerTk;
    }

    public synchronized byte[] getLeSecureConnectionsConfirmation() {
        return this.mLeSecureConnectionsConfirmation;
    }

    public synchronized void setLeSecureConnectionsConfirmation(byte[] leSecureConnectionsConfirmation) {
        this.mLeSecureConnectionsConfirmation = leSecureConnectionsConfirmation;
    }

    public synchronized byte[] getLeSecureConnectionsRandom() {
        return this.mLeSecureConnectionsRandom;
    }

    public synchronized void setLeSecureConnectionsRandom(byte[] leSecureConnectionsRandom) {
        this.mLeSecureConnectionsRandom = leSecureConnectionsRandom;
    }

    public synchronized OobData() {
    }

    private synchronized OobData(Parcel in) {
        this.mLeBluetoothDeviceAddress = in.createByteArray();
        this.mSecurityManagerTk = in.createByteArray();
        this.mLeSecureConnectionsConfirmation = in.createByteArray();
        this.mLeSecureConnectionsRandom = in.createByteArray();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeByteArray(this.mLeBluetoothDeviceAddress);
        out.writeByteArray(this.mSecurityManagerTk);
        out.writeByteArray(this.mLeSecureConnectionsConfirmation);
        out.writeByteArray(this.mLeSecureConnectionsRandom);
    }
}
