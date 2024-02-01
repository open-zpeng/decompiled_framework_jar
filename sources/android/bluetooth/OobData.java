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
    private byte[] mC192;
    private byte[] mC256;
    private byte[] mLeBluetoothDeviceAddress;
    private byte[] mLeSecureConnectionsConfirmation;
    private byte[] mLeSecureConnectionsRandom;
    private byte[] mR192;
    private byte[] mR256;
    private byte[] mSecurityManagerTk;

    public byte[] getLeBluetoothDeviceAddress() {
        return this.mLeBluetoothDeviceAddress;
    }

    public void setLeBluetoothDeviceAddress(byte[] leBluetoothDeviceAddress) {
        this.mLeBluetoothDeviceAddress = leBluetoothDeviceAddress;
    }

    public byte[] getSecurityManagerTk() {
        return this.mSecurityManagerTk;
    }

    public void setSecurityManagerTk(byte[] securityManagerTk) {
        this.mSecurityManagerTk = securityManagerTk;
    }

    public byte[] getLeSecureConnectionsConfirmation() {
        return this.mLeSecureConnectionsConfirmation;
    }

    public void setLeSecureConnectionsConfirmation(byte[] leSecureConnectionsConfirmation) {
        this.mLeSecureConnectionsConfirmation = leSecureConnectionsConfirmation;
    }

    public byte[] getLeSecureConnectionsRandom() {
        return this.mLeSecureConnectionsRandom;
    }

    public void setLeSecureConnectionsRandom(byte[] leSecureConnectionsRandom) {
        this.mLeSecureConnectionsRandom = leSecureConnectionsRandom;
    }

    public byte[] getC192() {
        return this.mC192;
    }

    public void setC192(byte[] c192) {
        this.mC192 = c192;
    }

    public byte[] getR192() {
        return this.mR192;
    }

    public void setR192(byte[] r192) {
        this.mR192 = r192;
    }

    public byte[] getC256() {
        return this.mC256;
    }

    public void setC256(byte[] c256) {
        this.mC256 = c256;
    }

    public byte[] getR256() {
        return this.mR256;
    }

    public void setR256(byte[] r256) {
        this.mR256 = r256;
    }

    public OobData() {
    }

    private OobData(Parcel in) {
        this.mLeBluetoothDeviceAddress = in.createByteArray();
        this.mSecurityManagerTk = in.createByteArray();
        this.mLeSecureConnectionsConfirmation = in.createByteArray();
        this.mLeSecureConnectionsRandom = in.createByteArray();
        this.mC192 = in.createByteArray();
        this.mR192 = in.createByteArray();
        this.mC256 = in.createByteArray();
        this.mR256 = in.createByteArray();
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
        out.writeByteArray(this.mC192);
        out.writeByteArray(this.mR192);
        out.writeByteArray(this.mC256);
        out.writeByteArray(this.mR256);
    }
}
