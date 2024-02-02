package android.net.wifi.aware;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
/* loaded from: classes2.dex */
public final class ConfigRequest implements Parcelable {
    public static final int CLUSTER_ID_MAX = 65535;
    public static final int CLUSTER_ID_MIN = 0;
    public static final Parcelable.Creator<ConfigRequest> CREATOR = new Parcelable.Creator<ConfigRequest>() { // from class: android.net.wifi.aware.ConfigRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConfigRequest[] newArray(int size) {
            return new ConfigRequest[size];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConfigRequest createFromParcel(Parcel in) {
            boolean support5gBand = in.readInt() != 0;
            int masterPreference = in.readInt();
            int clusterLow = in.readInt();
            int clusterHigh = in.readInt();
            int[] discoveryWindowInterval = in.createIntArray();
            return new ConfigRequest(support5gBand, masterPreference, clusterLow, clusterHigh, discoveryWindowInterval);
        }
    };
    public static final int DW_DISABLE = 0;
    public static final int DW_INTERVAL_NOT_INIT = -1;
    public static final int NAN_BAND_24GHZ = 0;
    public static final int NAN_BAND_5GHZ = 1;
    public final int mClusterHigh;
    public final int mClusterLow;
    public final int[] mDiscoveryWindowInterval;
    public final int mMasterPreference;
    public final boolean mSupport5gBand;

    private synchronized ConfigRequest(boolean support5gBand, int masterPreference, int clusterLow, int clusterHigh, int[] discoveryWindowInterval) {
        this.mSupport5gBand = support5gBand;
        this.mMasterPreference = masterPreference;
        this.mClusterLow = clusterLow;
        this.mClusterHigh = clusterHigh;
        this.mDiscoveryWindowInterval = discoveryWindowInterval;
    }

    public String toString() {
        return "ConfigRequest [mSupport5gBand=" + this.mSupport5gBand + ", mMasterPreference=" + this.mMasterPreference + ", mClusterLow=" + this.mClusterLow + ", mClusterHigh=" + this.mClusterHigh + ", mDiscoveryWindowInterval=" + Arrays.toString(this.mDiscoveryWindowInterval) + "]";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSupport5gBand ? 1 : 0);
        dest.writeInt(this.mMasterPreference);
        dest.writeInt(this.mClusterLow);
        dest.writeInt(this.mClusterHigh);
        dest.writeIntArray(this.mDiscoveryWindowInterval);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ConfigRequest) {
            ConfigRequest lhs = (ConfigRequest) o;
            return this.mSupport5gBand == lhs.mSupport5gBand && this.mMasterPreference == lhs.mMasterPreference && this.mClusterLow == lhs.mClusterLow && this.mClusterHigh == lhs.mClusterHigh && Arrays.equals(this.mDiscoveryWindowInterval, lhs.mDiscoveryWindowInterval);
        }
        return false;
    }

    public int hashCode() {
        int result = (31 * 17) + (this.mSupport5gBand ? 1 : 0);
        return (31 * ((31 * ((31 * ((31 * result) + this.mMasterPreference)) + this.mClusterLow)) + this.mClusterHigh)) + Arrays.hashCode(this.mDiscoveryWindowInterval);
    }

    public synchronized void validate() throws IllegalArgumentException {
        if (this.mMasterPreference < 0) {
            throw new IllegalArgumentException("Master Preference specification must be non-negative");
        }
        if (this.mMasterPreference == 1 || this.mMasterPreference == 255 || this.mMasterPreference > 255) {
            throw new IllegalArgumentException("Master Preference specification must not exceed 255 or use 1 or 255 (reserved values)");
        }
        if (this.mClusterLow < 0) {
            throw new IllegalArgumentException("Cluster specification must be non-negative");
        }
        if (this.mClusterLow > 65535) {
            throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
        }
        if (this.mClusterHigh < 0) {
            throw new IllegalArgumentException("Cluster specification must be non-negative");
        }
        if (this.mClusterHigh > 65535) {
            throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
        }
        if (this.mClusterLow > this.mClusterHigh) {
            throw new IllegalArgumentException("Invalid argument combination - must have Cluster Low <= Cluster High");
        }
        if (this.mDiscoveryWindowInterval.length != 2) {
            throw new IllegalArgumentException("Invalid discovery window interval: must have 2 elements (2.4 & 5");
        }
        if (this.mDiscoveryWindowInterval[0] != -1 && (this.mDiscoveryWindowInterval[0] < 1 || this.mDiscoveryWindowInterval[0] > 5)) {
            throw new IllegalArgumentException("Invalid discovery window interval for 2.4GHz: valid is UNSET or [1,5]");
        }
        if (this.mDiscoveryWindowInterval[1] != -1) {
            if (this.mDiscoveryWindowInterval[1] < 0 || this.mDiscoveryWindowInterval[1] > 5) {
                throw new IllegalArgumentException("Invalid discovery window interval for 5GHz: valid is UNSET or [0,5]");
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private boolean mSupport5gBand = false;
        private int mMasterPreference = 0;
        private int mClusterLow = 0;
        private int mClusterHigh = 65535;
        private int[] mDiscoveryWindowInterval = {-1, -1};

        public synchronized Builder setSupport5gBand(boolean support5gBand) {
            this.mSupport5gBand = support5gBand;
            return this;
        }

        public synchronized Builder setMasterPreference(int masterPreference) {
            if (masterPreference < 0) {
                throw new IllegalArgumentException("Master Preference specification must be non-negative");
            }
            if (masterPreference == 1 || masterPreference == 255 || masterPreference > 255) {
                throw new IllegalArgumentException("Master Preference specification must not exceed 255 or use 1 or 255 (reserved values)");
            }
            this.mMasterPreference = masterPreference;
            return this;
        }

        public synchronized Builder setClusterLow(int clusterLow) {
            if (clusterLow < 0) {
                throw new IllegalArgumentException("Cluster specification must be non-negative");
            }
            if (clusterLow > 65535) {
                throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
            }
            this.mClusterLow = clusterLow;
            return this;
        }

        public synchronized Builder setClusterHigh(int clusterHigh) {
            if (clusterHigh < 0) {
                throw new IllegalArgumentException("Cluster specification must be non-negative");
            }
            if (clusterHigh > 65535) {
                throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
            }
            this.mClusterHigh = clusterHigh;
            return this;
        }

        public synchronized Builder setDiscoveryWindowInterval(int band, int interval) {
            if (band == 0 || band == 1) {
                if ((band == 0 && (interval < 1 || interval > 5)) || (band == 1 && (interval < 0 || interval > 5))) {
                    throw new IllegalArgumentException("Invalid interval value: 2.4 GHz [1,5] or 5GHz [0,5]");
                }
                this.mDiscoveryWindowInterval[band] = interval;
                return this;
            }
            throw new IllegalArgumentException("Invalid band value");
        }

        public synchronized ConfigRequest build() {
            if (this.mClusterLow > this.mClusterHigh) {
                throw new IllegalArgumentException("Invalid argument combination - must have Cluster Low <= Cluster High");
            }
            return new ConfigRequest(this.mSupport5gBand, this.mMasterPreference, this.mClusterLow, this.mClusterHigh, this.mDiscoveryWindowInterval);
        }
    }
}
