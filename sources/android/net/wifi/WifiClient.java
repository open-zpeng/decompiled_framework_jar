package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class WifiClient implements Parcelable {
    public static final Parcelable.Creator<WifiClient> CREATOR = new Parcelable.Creator<WifiClient>() { // from class: android.net.wifi.WifiClient.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiClient createFromParcel(Parcel source) {
            return new WifiClient(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiClient[] newArray(int size) {
            return new WifiClient[size];
        }
    };
    public String mHostName;
    public String mIpAddr;
    public String mMacAddr;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mMacAddr);
        dest.writeString(this.mIpAddr);
        dest.writeString(this.mHostName);
    }

    public WifiClient(String mac, String ip, String name) {
        this.mMacAddr = mac;
        this.mIpAddr = ip;
        this.mHostName = name;
    }

    private WifiClient(Parcel source) {
        readFromParcel(source);
    }

    public void readFromParcel(Parcel source) {
        this.mMacAddr = source.readString();
        this.mIpAddr = source.readString();
        this.mHostName = source.readString();
    }

    public String toString() {
        return "[" + this.mHostName + ", " + this.mMacAddr + ", " + this.mIpAddr + "]";
    }
}
