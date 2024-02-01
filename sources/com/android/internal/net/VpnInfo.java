package com.android.internal.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import java.util.Arrays;

/* loaded from: classes3.dex */
public class VpnInfo implements Parcelable {
    public static final Parcelable.Creator<VpnInfo> CREATOR = new Parcelable.Creator<VpnInfo>() { // from class: com.android.internal.net.VpnInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VpnInfo createFromParcel(Parcel source) {
            VpnInfo info = new VpnInfo();
            info.ownerUid = source.readInt();
            info.vpnIface = source.readString();
            info.underlyingIfaces = source.readStringArray();
            return info;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VpnInfo[] newArray(int size) {
            return new VpnInfo[size];
        }
    };
    public int ownerUid;
    public String[] underlyingIfaces;
    public String vpnIface;

    public String toString() {
        return "VpnInfo{ownerUid=" + this.ownerUid + ", vpnIface='" + this.vpnIface + DateFormat.QUOTE + ", underlyingIfaces='" + Arrays.toString(this.underlyingIfaces) + DateFormat.QUOTE + '}';
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ownerUid);
        dest.writeString(this.vpnIface);
        dest.writeStringArray(this.underlyingIfaces);
    }
}
