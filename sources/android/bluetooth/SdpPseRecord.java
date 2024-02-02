package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class SdpPseRecord implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: android.bluetooth.SdpPseRecord.1
        @Override // android.os.Parcelable.Creator
        public SdpPseRecord createFromParcel(Parcel in) {
            return new SdpPseRecord(in);
        }

        @Override // android.os.Parcelable.Creator
        public SdpPseRecord[] newArray(int size) {
            return new SdpPseRecord[size];
        }
    };
    private final int mL2capPsm;
    private final int mProfileVersion;
    private final int mRfcommChannelNumber;
    private final String mServiceName;
    private final int mSupportedFeatures;
    private final int mSupportedRepositories;

    public synchronized SdpPseRecord(int l2capPsm, int rfcommChannelNumber, int profileVersion, int supportedFeatures, int supportedRepositories, String serviceName) {
        this.mL2capPsm = l2capPsm;
        this.mRfcommChannelNumber = rfcommChannelNumber;
        this.mProfileVersion = profileVersion;
        this.mSupportedFeatures = supportedFeatures;
        this.mSupportedRepositories = supportedRepositories;
        this.mServiceName = serviceName;
    }

    public synchronized SdpPseRecord(Parcel in) {
        this.mRfcommChannelNumber = in.readInt();
        this.mL2capPsm = in.readInt();
        this.mProfileVersion = in.readInt();
        this.mSupportedFeatures = in.readInt();
        this.mSupportedRepositories = in.readInt();
        this.mServiceName = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public synchronized int getL2capPsm() {
        return this.mL2capPsm;
    }

    public synchronized int getRfcommChannelNumber() {
        return this.mRfcommChannelNumber;
    }

    public synchronized int getSupportedFeatures() {
        return this.mSupportedFeatures;
    }

    public synchronized String getServiceName() {
        return this.mServiceName;
    }

    public synchronized int getProfileVersion() {
        return this.mProfileVersion;
    }

    public synchronized int getSupportedRepositories() {
        return this.mSupportedRepositories;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRfcommChannelNumber);
        dest.writeInt(this.mL2capPsm);
        dest.writeInt(this.mProfileVersion);
        dest.writeInt(this.mSupportedFeatures);
        dest.writeInt(this.mSupportedRepositories);
        dest.writeString(this.mServiceName);
    }

    public String toString() {
        String ret = "Bluetooth MNS SDP Record:\n";
        if (this.mRfcommChannelNumber != -1) {
            ret = "Bluetooth MNS SDP Record:\nRFCOMM Chan Number: " + this.mRfcommChannelNumber + "\n";
        }
        if (this.mL2capPsm != -1) {
            ret = ret + "L2CAP PSM: " + this.mL2capPsm + "\n";
        }
        if (this.mProfileVersion != -1) {
            ret = ret + "profile version: " + this.mProfileVersion + "\n";
        }
        if (this.mServiceName != null) {
            ret = ret + "Service Name: " + this.mServiceName + "\n";
        }
        if (this.mSupportedFeatures != -1) {
            ret = ret + "Supported features: " + this.mSupportedFeatures + "\n";
        }
        if (this.mSupportedRepositories != -1) {
            return ret + "Supported repositories: " + this.mSupportedRepositories + "\n";
        }
        return ret;
    }
}
