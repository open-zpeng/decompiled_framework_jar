package android.net.wifi.aware;

import android.net.wifi.aware.TlvBufferUtils;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import libcore.util.HexEncoding;
/* loaded from: classes2.dex */
public final class SubscribeConfig implements Parcelable {
    public static final Parcelable.Creator<SubscribeConfig> CREATOR = new Parcelable.Creator<SubscribeConfig>() { // from class: android.net.wifi.aware.SubscribeConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SubscribeConfig[] newArray(int size) {
            return new SubscribeConfig[size];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SubscribeConfig createFromParcel(Parcel in) {
            byte[] serviceName = in.createByteArray();
            byte[] ssi = in.createByteArray();
            byte[] matchFilter = in.createByteArray();
            int subscribeType = in.readInt();
            int ttlSec = in.readInt();
            boolean enableTerminateNotification = in.readInt() != 0;
            int minDistanceMm = in.readInt();
            boolean minDistanceMmSet = in.readInt() != 0;
            int maxDistanceMm = in.readInt();
            boolean maxDistanceMmSet = in.readInt() != 0;
            return new SubscribeConfig(serviceName, ssi, matchFilter, subscribeType, ttlSec, enableTerminateNotification, minDistanceMmSet, minDistanceMm, maxDistanceMmSet, maxDistanceMm);
        }
    };
    public static final int SUBSCRIBE_TYPE_ACTIVE = 1;
    public static final int SUBSCRIBE_TYPE_PASSIVE = 0;
    public final boolean mEnableTerminateNotification;
    public final byte[] mMatchFilter;
    public final int mMaxDistanceMm;
    public final boolean mMaxDistanceMmSet;
    public final int mMinDistanceMm;
    public final boolean mMinDistanceMmSet;
    public final byte[] mServiceName;
    public final byte[] mServiceSpecificInfo;
    public final int mSubscribeType;
    public final int mTtlSec;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface SubscribeTypes {
    }

    public synchronized SubscribeConfig(byte[] serviceName, byte[] serviceSpecificInfo, byte[] matchFilter, int subscribeType, int ttlSec, boolean enableTerminateNotification, boolean minDistanceMmSet, int minDistanceMm, boolean maxDistanceMmSet, int maxDistanceMm) {
        this.mServiceName = serviceName;
        this.mServiceSpecificInfo = serviceSpecificInfo;
        this.mMatchFilter = matchFilter;
        this.mSubscribeType = subscribeType;
        this.mTtlSec = ttlSec;
        this.mEnableTerminateNotification = enableTerminateNotification;
        this.mMinDistanceMm = minDistanceMm;
        this.mMinDistanceMmSet = minDistanceMmSet;
        this.mMaxDistanceMm = maxDistanceMm;
        this.mMaxDistanceMmSet = maxDistanceMmSet;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SubscribeConfig [mServiceName='");
        sb.append(this.mServiceName == null ? "<null>" : String.valueOf(HexEncoding.encode(this.mServiceName)));
        sb.append(", mServiceName.length=");
        sb.append(this.mServiceName == null ? 0 : this.mServiceName.length);
        sb.append(", mServiceSpecificInfo='");
        sb.append(this.mServiceSpecificInfo == null ? "<null>" : String.valueOf(HexEncoding.encode(this.mServiceSpecificInfo)));
        sb.append(", mServiceSpecificInfo.length=");
        sb.append(this.mServiceSpecificInfo == null ? 0 : this.mServiceSpecificInfo.length);
        sb.append(", mMatchFilter=");
        sb.append(new TlvBufferUtils.TlvIterable(0, 1, this.mMatchFilter).toString());
        sb.append(", mMatchFilter.length=");
        sb.append(this.mMatchFilter != null ? this.mMatchFilter.length : 0);
        sb.append(", mSubscribeType=");
        sb.append(this.mSubscribeType);
        sb.append(", mTtlSec=");
        sb.append(this.mTtlSec);
        sb.append(", mEnableTerminateNotification=");
        sb.append(this.mEnableTerminateNotification);
        sb.append(", mMinDistanceMm=");
        sb.append(this.mMinDistanceMm);
        sb.append(", mMinDistanceMmSet=");
        sb.append(this.mMinDistanceMmSet);
        sb.append(", mMaxDistanceMm=");
        sb.append(this.mMaxDistanceMm);
        sb.append(", mMaxDistanceMmSet=");
        sb.append(this.mMaxDistanceMmSet);
        sb.append("]");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.mServiceName);
        dest.writeByteArray(this.mServiceSpecificInfo);
        dest.writeByteArray(this.mMatchFilter);
        dest.writeInt(this.mSubscribeType);
        dest.writeInt(this.mTtlSec);
        dest.writeInt(this.mEnableTerminateNotification ? 1 : 0);
        dest.writeInt(this.mMinDistanceMm);
        dest.writeInt(this.mMinDistanceMmSet ? 1 : 0);
        dest.writeInt(this.mMaxDistanceMm);
        dest.writeInt(this.mMaxDistanceMmSet ? 1 : 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof SubscribeConfig) {
            SubscribeConfig lhs = (SubscribeConfig) o;
            if (Arrays.equals(this.mServiceName, lhs.mServiceName) && Arrays.equals(this.mServiceSpecificInfo, lhs.mServiceSpecificInfo) && Arrays.equals(this.mMatchFilter, lhs.mMatchFilter) && this.mSubscribeType == lhs.mSubscribeType && this.mTtlSec == lhs.mTtlSec && this.mEnableTerminateNotification == lhs.mEnableTerminateNotification && this.mMinDistanceMmSet == lhs.mMinDistanceMmSet && this.mMaxDistanceMmSet == lhs.mMaxDistanceMmSet) {
                if (!this.mMinDistanceMmSet || this.mMinDistanceMm == lhs.mMinDistanceMm) {
                    return !this.mMaxDistanceMmSet || this.mMaxDistanceMm == lhs.mMaxDistanceMm;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = Objects.hash(this.mServiceName, this.mServiceSpecificInfo, this.mMatchFilter, Integer.valueOf(this.mSubscribeType), Integer.valueOf(this.mTtlSec), Boolean.valueOf(this.mEnableTerminateNotification), Boolean.valueOf(this.mMinDistanceMmSet), Boolean.valueOf(this.mMaxDistanceMmSet));
        if (this.mMinDistanceMmSet) {
            result = Objects.hash(Integer.valueOf(result), Integer.valueOf(this.mMinDistanceMm));
        }
        return this.mMaxDistanceMmSet ? Objects.hash(Integer.valueOf(result), Integer.valueOf(this.mMaxDistanceMm)) : result;
    }

    public synchronized void assertValid(Characteristics characteristics, boolean rttSupported) throws IllegalArgumentException {
        WifiAwareUtils.validateServiceName(this.mServiceName);
        if (!TlvBufferUtils.isValid(this.mMatchFilter, 0, 1)) {
            throw new IllegalArgumentException("Invalid matchFilter configuration - LV fields do not match up to length");
        }
        if (this.mSubscribeType < 0 || this.mSubscribeType > 1) {
            throw new IllegalArgumentException("Invalid subscribeType - " + this.mSubscribeType);
        } else if (this.mTtlSec < 0) {
            throw new IllegalArgumentException("Invalid ttlSec - must be non-negative");
        } else {
            if (characteristics != null) {
                int maxServiceNameLength = characteristics.getMaxServiceNameLength();
                if (maxServiceNameLength != 0 && this.mServiceName.length > maxServiceNameLength) {
                    throw new IllegalArgumentException("Service name longer than supported by device characteristics");
                }
                int maxServiceSpecificInfoLength = characteristics.getMaxServiceSpecificInfoLength();
                if (maxServiceSpecificInfoLength != 0 && this.mServiceSpecificInfo != null && this.mServiceSpecificInfo.length > maxServiceSpecificInfoLength) {
                    throw new IllegalArgumentException("Service specific info longer than supported by device characteristics");
                }
                int maxMatchFilterLength = characteristics.getMaxMatchFilterLength();
                if (maxMatchFilterLength != 0 && this.mMatchFilter != null && this.mMatchFilter.length > maxMatchFilterLength) {
                    throw new IllegalArgumentException("Match filter longer than supported by device characteristics");
                }
            }
            if (this.mMinDistanceMmSet && this.mMinDistanceMm < 0) {
                throw new IllegalArgumentException("Minimum distance must be non-negative");
            }
            if (this.mMaxDistanceMmSet && this.mMaxDistanceMm < 0) {
                throw new IllegalArgumentException("Maximum distance must be non-negative");
            }
            if (this.mMinDistanceMmSet && this.mMaxDistanceMmSet && this.mMaxDistanceMm <= this.mMinDistanceMm) {
                throw new IllegalArgumentException("Maximum distance must be greater than minimum distance");
            }
            if (rttSupported) {
                return;
            }
            if (this.mMinDistanceMmSet || this.mMaxDistanceMmSet) {
                throw new IllegalArgumentException("Ranging is not supported");
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private byte[] mMatchFilter;
        private int mMaxDistanceMm;
        private int mMinDistanceMm;
        private byte[] mServiceName;
        private byte[] mServiceSpecificInfo;
        private int mSubscribeType = 0;
        private int mTtlSec = 0;
        private boolean mEnableTerminateNotification = true;
        private boolean mMinDistanceMmSet = false;
        private boolean mMaxDistanceMmSet = false;

        public Builder setServiceName(String serviceName) {
            if (serviceName == null) {
                throw new IllegalArgumentException("Invalid service name - must be non-null");
            }
            this.mServiceName = serviceName.getBytes(StandardCharsets.UTF_8);
            return this;
        }

        public Builder setServiceSpecificInfo(byte[] serviceSpecificInfo) {
            this.mServiceSpecificInfo = serviceSpecificInfo;
            return this;
        }

        public Builder setMatchFilter(List<byte[]> matchFilter) {
            this.mMatchFilter = new TlvBufferUtils.TlvConstructor(0, 1).allocateAndPut(matchFilter).getArray();
            return this;
        }

        public Builder setSubscribeType(int subscribeType) {
            if (subscribeType < 0 || subscribeType > 1) {
                throw new IllegalArgumentException("Invalid subscribeType - " + subscribeType);
            }
            this.mSubscribeType = subscribeType;
            return this;
        }

        public Builder setTtlSec(int ttlSec) {
            if (ttlSec < 0) {
                throw new IllegalArgumentException("Invalid ttlSec - must be non-negative");
            }
            this.mTtlSec = ttlSec;
            return this;
        }

        public Builder setTerminateNotificationEnabled(boolean enable) {
            this.mEnableTerminateNotification = enable;
            return this;
        }

        public Builder setMinDistanceMm(int minDistanceMm) {
            this.mMinDistanceMm = minDistanceMm;
            this.mMinDistanceMmSet = true;
            return this;
        }

        public Builder setMaxDistanceMm(int maxDistanceMm) {
            this.mMaxDistanceMm = maxDistanceMm;
            this.mMaxDistanceMmSet = true;
            return this;
        }

        public SubscribeConfig build() {
            return new SubscribeConfig(this.mServiceName, this.mServiceSpecificInfo, this.mMatchFilter, this.mSubscribeType, this.mTtlSec, this.mEnableTerminateNotification, this.mMinDistanceMmSet, this.mMinDistanceMm, this.mMaxDistanceMmSet, this.mMaxDistanceMm);
        }
    }
}
