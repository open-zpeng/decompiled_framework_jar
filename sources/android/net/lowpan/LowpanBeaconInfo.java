package android.net.lowpan;

import android.net.lowpan.LowpanIdentity;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.HexDump;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;
/* loaded from: classes2.dex */
public class LowpanBeaconInfo implements Parcelable {
    private protected static final Parcelable.Creator<LowpanBeaconInfo> CREATOR = new Parcelable.Creator<LowpanBeaconInfo>() { // from class: android.net.lowpan.LowpanBeaconInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LowpanBeaconInfo createFromParcel(Parcel in) {
            Builder builder = new Builder();
            builder.setLowpanIdentity(LowpanIdentity.CREATOR.createFromParcel(in));
            builder.setRssi(in.readInt());
            builder.setLqi(in.readInt());
            builder.setBeaconAddress(in.createByteArray());
            for (int i = in.readInt(); i > 0; i--) {
                builder.setFlag(in.readInt());
            }
            return builder.build();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LowpanBeaconInfo[] newArray(int size) {
            return new LowpanBeaconInfo[size];
        }
    };
    private protected static final int FLAG_CAN_ASSIST = 1;
    private protected static final int UNKNOWN_LQI = 0;
    private protected static final int UNKNOWN_RSSI = Integer.MAX_VALUE;
    public protected byte[] mBeaconAddress;
    public protected final TreeSet<Integer> mFlags;
    public protected LowpanIdentity mIdentity;
    public protected int mLqi;
    public protected int mRssi;

    /* loaded from: classes2.dex */
    public static class Builder {
        public private protected final LowpanIdentity.Builder mIdentityBuilder = new LowpanIdentity.Builder();
        public private protected final LowpanBeaconInfo mBeaconInfo = new LowpanBeaconInfo();

        private protected synchronized Builder() {
        }

        private protected synchronized Builder setLowpanIdentity(LowpanIdentity x) {
            this.mIdentityBuilder.setLowpanIdentity(x);
            return this;
        }

        private protected synchronized Builder setName(String x) {
            this.mIdentityBuilder.setName(x);
            return this;
        }

        private protected synchronized Builder setXpanid(byte[] x) {
            this.mIdentityBuilder.setXpanid(x);
            return this;
        }

        private protected synchronized Builder setPanid(int x) {
            this.mIdentityBuilder.setPanid(x);
            return this;
        }

        private protected synchronized Builder setChannel(int x) {
            this.mIdentityBuilder.setChannel(x);
            return this;
        }

        private protected synchronized Builder setType(String x) {
            this.mIdentityBuilder.setType(x);
            return this;
        }

        private protected synchronized Builder setRssi(int x) {
            this.mBeaconInfo.mRssi = x;
            return this;
        }

        private protected synchronized Builder setLqi(int x) {
            this.mBeaconInfo.mLqi = x;
            return this;
        }

        private protected synchronized Builder setBeaconAddress(byte[] x) {
            this.mBeaconInfo.mBeaconAddress = x != null ? (byte[]) x.clone() : null;
            return this;
        }

        private protected synchronized Builder setFlag(int x) {
            this.mBeaconInfo.mFlags.add(Integer.valueOf(x));
            return this;
        }

        private protected synchronized Builder setFlags(Collection<Integer> x) {
            this.mBeaconInfo.mFlags.addAll(x);
            return this;
        }

        private protected synchronized LowpanBeaconInfo build() {
            this.mBeaconInfo.mIdentity = this.mIdentityBuilder.build();
            if (this.mBeaconInfo.mBeaconAddress == null) {
                this.mBeaconInfo.mBeaconAddress = new byte[0];
            }
            return this.mBeaconInfo;
        }
    }

    public protected synchronized LowpanBeaconInfo() {
        this.mRssi = Integer.MAX_VALUE;
        this.mLqi = 0;
        this.mBeaconAddress = null;
        this.mFlags = new TreeSet<>();
    }

    private protected synchronized LowpanIdentity getLowpanIdentity() {
        return this.mIdentity;
    }

    private protected synchronized int getRssi() {
        return this.mRssi;
    }

    private protected synchronized int getLqi() {
        return this.mLqi;
    }

    private protected synchronized byte[] getBeaconAddress() {
        return (byte[]) this.mBeaconAddress.clone();
    }

    private protected synchronized Collection<Integer> getFlags() {
        return (Collection) this.mFlags.clone();
    }

    private protected synchronized boolean isFlagSet(int flag) {
        return this.mFlags.contains(Integer.valueOf(flag));
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.mIdentity.toString());
        if (this.mRssi != Integer.MAX_VALUE) {
            sb.append(", RSSI:");
            sb.append(this.mRssi);
            sb.append("dBm");
        }
        if (this.mLqi != 0) {
            sb.append(", LQI:");
            sb.append(this.mLqi);
        }
        if (this.mBeaconAddress.length > 0) {
            sb.append(", BeaconAddress:");
            sb.append(HexDump.toHexString(this.mBeaconAddress));
        }
        Iterator<Integer> it = this.mFlags.iterator();
        while (it.hasNext()) {
            Integer flag = it.next();
            if (flag.intValue() == 1) {
                sb.append(", CAN_ASSIST");
            } else {
                sb.append(", FLAG_");
                sb.append(Integer.toHexString(flag.intValue()));
            }
        }
        return sb.toString();
    }

    public int hashCode() {
        return Objects.hash(this.mIdentity, Integer.valueOf(this.mRssi), Integer.valueOf(this.mLqi), Integer.valueOf(Arrays.hashCode(this.mBeaconAddress)), this.mFlags);
    }

    public boolean equals(Object obj) {
        if (obj instanceof LowpanBeaconInfo) {
            LowpanBeaconInfo rhs = (LowpanBeaconInfo) obj;
            return this.mIdentity.equals(rhs.mIdentity) && Arrays.equals(this.mBeaconAddress, rhs.mBeaconAddress) && this.mRssi == rhs.mRssi && this.mLqi == rhs.mLqi && this.mFlags.equals(rhs.mFlags);
        }
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        this.mIdentity.writeToParcel(dest, flags);
        dest.writeInt(this.mRssi);
        dest.writeInt(this.mLqi);
        dest.writeByteArray(this.mBeaconAddress);
        dest.writeInt(this.mFlags.size());
        Iterator<Integer> it = this.mFlags.iterator();
        while (it.hasNext()) {
            Integer val = it.next();
            dest.writeInt(val.intValue());
        }
    }
}
