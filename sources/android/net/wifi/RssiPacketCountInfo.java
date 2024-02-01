package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class RssiPacketCountInfo implements Parcelable {
    public static final Parcelable.Creator<RssiPacketCountInfo> CREATOR = new Parcelable.Creator<RssiPacketCountInfo>() { // from class: android.net.wifi.RssiPacketCountInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RssiPacketCountInfo createFromParcel(Parcel in) {
            return new RssiPacketCountInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RssiPacketCountInfo[] newArray(int size) {
            return new RssiPacketCountInfo[size];
        }
    };
    public int rssi;
    public int rxgood;
    public int txbad;
    public int txgood;

    public synchronized RssiPacketCountInfo() {
        this.rxgood = 0;
        this.txbad = 0;
        this.txgood = 0;
        this.rssi = 0;
    }

    private synchronized RssiPacketCountInfo(Parcel in) {
        this.rssi = in.readInt();
        this.txgood = in.readInt();
        this.txbad = in.readInt();
        this.rxgood = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.rssi);
        out.writeInt(this.txgood);
        out.writeInt(this.txbad);
        out.writeInt(this.rxgood);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
