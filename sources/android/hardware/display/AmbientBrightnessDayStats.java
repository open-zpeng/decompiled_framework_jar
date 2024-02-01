package android.hardware.display;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.Preconditions;
import java.time.LocalDate;
import java.util.Arrays;
@SystemApi
/* loaded from: classes.dex */
public final class AmbientBrightnessDayStats implements Parcelable {
    public static final Parcelable.Creator<AmbientBrightnessDayStats> CREATOR = new Parcelable.Creator<AmbientBrightnessDayStats>() { // from class: android.hardware.display.AmbientBrightnessDayStats.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AmbientBrightnessDayStats createFromParcel(Parcel source) {
            return new AmbientBrightnessDayStats(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AmbientBrightnessDayStats[] newArray(int size) {
            return new AmbientBrightnessDayStats[size];
        }
    };
    private final float[] mBucketBoundaries;
    private final LocalDate mLocalDate;
    private final float[] mStats;

    public synchronized AmbientBrightnessDayStats(LocalDate localDate, float[] bucketBoundaries) {
        this(localDate, bucketBoundaries, null);
    }

    public synchronized AmbientBrightnessDayStats(LocalDate localDate, float[] bucketBoundaries, float[] stats) {
        Preconditions.checkNotNull(localDate);
        Preconditions.checkNotNull(bucketBoundaries);
        Preconditions.checkArrayElementsInRange(bucketBoundaries, 0.0f, Float.MAX_VALUE, "bucketBoundaries");
        if (bucketBoundaries.length < 1) {
            throw new IllegalArgumentException("Bucket boundaries must contain at least 1 value");
        }
        checkSorted(bucketBoundaries);
        if (stats == null) {
            stats = new float[bucketBoundaries.length];
        } else {
            Preconditions.checkArrayElementsInRange(stats, 0.0f, Float.MAX_VALUE, Context.STATS_MANAGER);
            if (bucketBoundaries.length != stats.length) {
                throw new IllegalArgumentException("Bucket boundaries and stats must be of same size.");
            }
        }
        this.mLocalDate = localDate;
        this.mBucketBoundaries = bucketBoundaries;
        this.mStats = stats;
    }

    public LocalDate getLocalDate() {
        return this.mLocalDate;
    }

    public float[] getStats() {
        return this.mStats;
    }

    public float[] getBucketBoundaries() {
        return this.mBucketBoundaries;
    }

    private synchronized AmbientBrightnessDayStats(Parcel source) {
        this.mLocalDate = LocalDate.parse(source.readString());
        this.mBucketBoundaries = source.createFloatArray();
        this.mStats = source.createFloatArray();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AmbientBrightnessDayStats other = (AmbientBrightnessDayStats) obj;
        if (this.mLocalDate.equals(other.mLocalDate) && Arrays.equals(this.mBucketBoundaries, other.mBucketBoundaries) && Arrays.equals(this.mStats, other.mStats)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (1 * 31) + this.mLocalDate.hashCode();
        return (((result * 31) + Arrays.hashCode(this.mBucketBoundaries)) * 31) + Arrays.hashCode(this.mStats);
    }

    public String toString() {
        StringBuilder bucketBoundariesString = new StringBuilder();
        StringBuilder statsString = new StringBuilder();
        for (int i = 0; i < this.mBucketBoundaries.length; i++) {
            if (i != 0) {
                bucketBoundariesString.append(", ");
                statsString.append(", ");
            }
            bucketBoundariesString.append(this.mBucketBoundaries[i]);
            statsString.append(this.mStats[i]);
        }
        return this.mLocalDate + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + "{" + ((CharSequence) bucketBoundariesString) + "} {" + ((CharSequence) statsString) + "}";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mLocalDate.toString());
        dest.writeFloatArray(this.mBucketBoundaries);
        dest.writeFloatArray(this.mStats);
    }

    public synchronized void log(float ambientBrightness, float durationSec) {
        int bucketIndex = getBucketIndex(ambientBrightness);
        if (bucketIndex >= 0) {
            float[] fArr = this.mStats;
            fArr[bucketIndex] = fArr[bucketIndex] + durationSec;
        }
    }

    private synchronized int getBucketIndex(float ambientBrightness) {
        if (ambientBrightness < this.mBucketBoundaries[0]) {
            return -1;
        }
        int low = 0;
        int high = this.mBucketBoundaries.length - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (this.mBucketBoundaries[mid] <= ambientBrightness && ambientBrightness < this.mBucketBoundaries[mid + 1]) {
                return mid;
            }
            if (this.mBucketBoundaries[mid] < ambientBrightness) {
                low = mid + 1;
            } else if (this.mBucketBoundaries[mid] > ambientBrightness) {
                int high2 = mid - 1;
                high = high2;
            }
        }
        return low;
    }

    private static synchronized void checkSorted(float[] values) {
        if (values.length <= 1) {
            return;
        }
        float prevValue = values[0];
        float prevValue2 = prevValue;
        for (int i = 1; i < values.length; i++) {
            Preconditions.checkState(prevValue2 < values[i]);
            prevValue2 = values[i];
        }
    }
}
