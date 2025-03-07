package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class SdpSapsRecord implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: android.bluetooth.SdpSapsRecord.1
        @Override // android.os.Parcelable.Creator
        public SdpSapsRecord createFromParcel(Parcel in) {
            return new SdpSapsRecord(in);
        }

        @Override // android.os.Parcelable.Creator
        public SdpRecord[] newArray(int size) {
            return new SdpRecord[size];
        }
    };
    private final int mProfileVersion;
    private final int mRfcommChannelNumber;
    private final String mServiceName;

    public SdpSapsRecord(int rfcommChannelNumber, int profileVersion, String serviceName) {
        this.mRfcommChannelNumber = rfcommChannelNumber;
        this.mProfileVersion = profileVersion;
        this.mServiceName = serviceName;
    }

    public SdpSapsRecord(Parcel in) {
        this.mRfcommChannelNumber = in.readInt();
        this.mProfileVersion = in.readInt();
        this.mServiceName = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getRfcommCannelNumber() {
        return this.mRfcommChannelNumber;
    }

    public int getProfileVersion() {
        return this.mProfileVersion;
    }

    public String getServiceName() {
        return this.mServiceName;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRfcommChannelNumber);
        dest.writeInt(this.mProfileVersion);
        dest.writeString(this.mServiceName);
    }

    public String toString() {
        String ret = "Bluetooth MAS SDP Record:\n";
        if (this.mRfcommChannelNumber != -1) {
            ret = "Bluetooth MAS SDP Record:\nRFCOMM Chan Number: " + this.mRfcommChannelNumber + "\n";
        }
        if (this.mServiceName != null) {
            ret = ret + "Service Name: " + this.mServiceName + "\n";
        }
        if (this.mProfileVersion != -1) {
            return ret + "Profile version: " + this.mProfileVersion + "\n";
        }
        return ret;
    }
}
