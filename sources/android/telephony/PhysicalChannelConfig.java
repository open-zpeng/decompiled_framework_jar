package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes2.dex */
public final class PhysicalChannelConfig implements Parcelable {
    public static final int CONNECTION_PRIMARY_SERVING = 1;
    public static final int CONNECTION_SECONDARY_SERVING = 2;
    public static final int CONNECTION_UNKNOWN = Integer.MAX_VALUE;
    public static final Parcelable.Creator<PhysicalChannelConfig> CREATOR = new Parcelable.Creator<PhysicalChannelConfig>() { // from class: android.telephony.PhysicalChannelConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PhysicalChannelConfig createFromParcel(Parcel in) {
            return new PhysicalChannelConfig(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PhysicalChannelConfig[] newArray(int size) {
            return new PhysicalChannelConfig[size];
        }
    };
    private int mCellBandwidthDownlinkKhz;
    private int mCellConnectionStatus;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ConnectionStatus {
    }

    public synchronized PhysicalChannelConfig(int status, int bandwidth) {
        this.mCellConnectionStatus = status;
        this.mCellBandwidthDownlinkKhz = bandwidth;
    }

    public synchronized PhysicalChannelConfig(Parcel in) {
        this.mCellConnectionStatus = in.readInt();
        this.mCellBandwidthDownlinkKhz = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mCellConnectionStatus);
        dest.writeInt(this.mCellBandwidthDownlinkKhz);
    }

    public synchronized int getCellBandwidthDownlink() {
        return this.mCellBandwidthDownlinkKhz;
    }

    public synchronized int getConnectionStatus() {
        return this.mCellConnectionStatus;
    }

    private synchronized String getConnectionStatusString() {
        int i = this.mCellConnectionStatus;
        if (i != Integer.MAX_VALUE) {
            switch (i) {
                case 1:
                    return "PrimaryServing";
                case 2:
                    return "SecondaryServing";
                default:
                    return "Invalid(" + this.mCellConnectionStatus + ")";
            }
        }
        return "Unknown";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof PhysicalChannelConfig) {
            PhysicalChannelConfig config = (PhysicalChannelConfig) o;
            return this.mCellConnectionStatus == config.mCellConnectionStatus && this.mCellBandwidthDownlinkKhz == config.mCellBandwidthDownlinkKhz;
        }
        return false;
    }

    public int hashCode() {
        return (this.mCellBandwidthDownlinkKhz * 29) + (this.mCellConnectionStatus * 31);
    }

    public String toString() {
        return "{mConnectionStatus=" + getConnectionStatusString() + ",mCellBandwidthDownlinkKhz=" + this.mCellBandwidthDownlinkKhz + "}";
    }
}
