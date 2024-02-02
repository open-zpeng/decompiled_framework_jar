package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
@Deprecated
/* loaded from: classes2.dex */
public class WpsInfo implements Parcelable {
    @Deprecated
    public static final Parcelable.Creator<WpsInfo> CREATOR = new Parcelable.Creator<WpsInfo>() { // from class: android.net.wifi.WpsInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        @Deprecated
        public WpsInfo createFromParcel(Parcel in) {
            WpsInfo config = new WpsInfo();
            config.setup = in.readInt();
            config.BSSID = in.readString();
            config.pin = in.readString();
            return config;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        @Deprecated
        public WpsInfo[] newArray(int size) {
            return new WpsInfo[size];
        }
    };
    @Deprecated
    public static final int DISPLAY = 1;
    @Deprecated
    public static final int INVALID = 4;
    @Deprecated
    public static final int KEYPAD = 2;
    @Deprecated
    public static final int LABEL = 3;
    @Deprecated
    public static final int PBC = 0;
    @Deprecated
    public String BSSID;
    @Deprecated
    public String pin;
    @Deprecated
    public int setup;

    @Deprecated
    public WpsInfo() {
        this.setup = 4;
        this.BSSID = null;
        this.pin = null;
    }

    @Deprecated
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(" setup: ");
        sbuf.append(this.setup);
        sbuf.append('\n');
        sbuf.append(" BSSID: ");
        sbuf.append(this.BSSID);
        sbuf.append('\n');
        sbuf.append(" pin: ");
        sbuf.append(this.pin);
        sbuf.append('\n');
        return sbuf.toString();
    }

    @Override // android.os.Parcelable
    @Deprecated
    public int describeContents() {
        return 0;
    }

    @Deprecated
    public WpsInfo(WpsInfo source) {
        if (source != null) {
            this.setup = source.setup;
            this.BSSID = source.BSSID;
            this.pin = source.pin;
        }
    }

    @Override // android.os.Parcelable
    @Deprecated
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.setup);
        dest.writeString(this.BSSID);
        dest.writeString(this.pin);
    }
}
