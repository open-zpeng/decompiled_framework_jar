package android.telephony;

import android.annotation.SystemApi;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
@SystemApi
/* loaded from: classes2.dex */
public final class TelephonyHistogram implements Parcelable {
    private static final int ABSENT = 0;
    public static final Parcelable.Creator<TelephonyHistogram> CREATOR = new Parcelable.Creator<TelephonyHistogram>() { // from class: android.telephony.TelephonyHistogram.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TelephonyHistogram createFromParcel(Parcel in) {
            return new TelephonyHistogram(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TelephonyHistogram[] newArray(int size) {
            return new TelephonyHistogram[size];
        }
    };
    private static final int PRESENT = 1;
    private static final int RANGE_CALCULATION_COUNT = 10;
    public static final int TELEPHONY_CATEGORY_RIL = 1;
    private int mAverageTimeMs;
    private final int mBucketCount;
    private final int[] mBucketCounters;
    private final int[] mBucketEndPoints;
    private final int mCategory;
    private final int mId;
    private int[] mInitialTimings;
    private int mMaxTimeMs;
    private int mMinTimeMs;
    private int mSampleCount;

    public TelephonyHistogram(int category, int id, int bucketCount) {
        if (bucketCount <= 1) {
            throw new IllegalArgumentException("Invalid number of buckets");
        }
        this.mCategory = category;
        this.mId = id;
        this.mMinTimeMs = Integer.MAX_VALUE;
        this.mMaxTimeMs = 0;
        this.mAverageTimeMs = 0;
        this.mSampleCount = 0;
        this.mInitialTimings = new int[10];
        this.mBucketCount = bucketCount;
        this.mBucketEndPoints = new int[bucketCount - 1];
        this.mBucketCounters = new int[bucketCount];
    }

    public TelephonyHistogram(TelephonyHistogram th) {
        this.mCategory = th.getCategory();
        this.mId = th.getId();
        this.mMinTimeMs = th.getMinTime();
        this.mMaxTimeMs = th.getMaxTime();
        this.mAverageTimeMs = th.getAverageTime();
        this.mSampleCount = th.getSampleCount();
        this.mInitialTimings = th.getInitialTimings();
        this.mBucketCount = th.getBucketCount();
        this.mBucketEndPoints = th.getBucketEndPoints();
        this.mBucketCounters = th.getBucketCounters();
    }

    public int getCategory() {
        return this.mCategory;
    }

    public int getId() {
        return this.mId;
    }

    public int getMinTime() {
        return this.mMinTimeMs;
    }

    public int getMaxTime() {
        return this.mMaxTimeMs;
    }

    public int getAverageTime() {
        return this.mAverageTimeMs;
    }

    public int getSampleCount() {
        return this.mSampleCount;
    }

    private synchronized int[] getInitialTimings() {
        return this.mInitialTimings;
    }

    public int getBucketCount() {
        return this.mBucketCount;
    }

    public int[] getBucketEndPoints() {
        if (this.mSampleCount > 1 && this.mSampleCount < 10) {
            int[] tempEndPoints = new int[this.mBucketCount - 1];
            calculateBucketEndPoints(tempEndPoints);
            return tempEndPoints;
        }
        return getDeepCopyOfArray(this.mBucketEndPoints);
    }

    public int[] getBucketCounters() {
        if (this.mSampleCount > 1 && this.mSampleCount < 10) {
            int[] tempEndPoints = new int[this.mBucketCount - 1];
            int[] tempBucketCounters = new int[this.mBucketCount];
            calculateBucketEndPoints(tempEndPoints);
            for (int j = 0; j < this.mSampleCount; j++) {
                addToBucketCounter(tempEndPoints, tempBucketCounters, this.mInitialTimings[j]);
            }
            return tempBucketCounters;
        }
        return getDeepCopyOfArray(this.mBucketCounters);
    }

    private synchronized int[] getDeepCopyOfArray(int[] array) {
        int[] clone = new int[array.length];
        System.arraycopy(array, 0, clone, 0, array.length);
        return clone;
    }

    private synchronized void addToBucketCounter(int[] bucketEndPoints, int[] bucketCounters, int time) {
        int i = 0;
        while (i < bucketEndPoints.length) {
            if (time > bucketEndPoints[i]) {
                i++;
            } else {
                bucketCounters[i] = bucketCounters[i] + 1;
                return;
            }
        }
        bucketCounters[i] = bucketCounters[i] + 1;
    }

    private synchronized void calculateBucketEndPoints(int[] bucketEndPoints) {
        for (int i = 1; i < this.mBucketCount; i++) {
            int endPt = this.mMinTimeMs + (((this.mMaxTimeMs - this.mMinTimeMs) * i) / this.mBucketCount);
            bucketEndPoints[i - 1] = endPt;
        }
    }

    public void addTimeTaken(int time) {
        int j = 0;
        if (this.mSampleCount == 0 || this.mSampleCount == Integer.MAX_VALUE) {
            if (this.mSampleCount == 0) {
                this.mMinTimeMs = time;
                this.mMaxTimeMs = time;
                this.mAverageTimeMs = time;
            } else {
                this.mInitialTimings = new int[10];
            }
            this.mSampleCount = 1;
            Arrays.fill(this.mInitialTimings, 0);
            this.mInitialTimings[0] = time;
            Arrays.fill(this.mBucketEndPoints, 0);
            Arrays.fill(this.mBucketCounters, 0);
            return;
        }
        if (time < this.mMinTimeMs) {
            this.mMinTimeMs = time;
        }
        if (time > this.mMaxTimeMs) {
            this.mMaxTimeMs = time;
        }
        long totalTime = (this.mAverageTimeMs * this.mSampleCount) + time;
        int i = this.mSampleCount + 1;
        this.mSampleCount = i;
        this.mAverageTimeMs = (int) (totalTime / i);
        if (this.mSampleCount < 10) {
            this.mInitialTimings[this.mSampleCount - 1] = time;
        } else if (this.mSampleCount == 10) {
            this.mInitialTimings[this.mSampleCount - 1] = time;
            calculateBucketEndPoints(this.mBucketEndPoints);
            while (true) {
                int j2 = j;
                if (j2 < 10) {
                    addToBucketCounter(this.mBucketEndPoints, this.mBucketCounters, this.mInitialTimings[j2]);
                    j = j2 + 1;
                } else {
                    this.mInitialTimings = null;
                    return;
                }
            }
        } else {
            addToBucketCounter(this.mBucketEndPoints, this.mBucketCounters, time);
        }
    }

    public String toString() {
        String basic = " Histogram id = " + this.mId + " Time(ms): min = " + this.mMinTimeMs + " max = " + this.mMaxTimeMs + " avg = " + this.mAverageTimeMs + " Count = " + this.mSampleCount;
        if (this.mSampleCount < 10) {
            return basic;
        }
        StringBuffer intervals = new StringBuffer(" Interval Endpoints:");
        for (int i = 0; i < this.mBucketEndPoints.length; i++) {
            intervals.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mBucketEndPoints[i]);
        }
        intervals.append(" Interval counters:");
        for (int i2 = 0; i2 < this.mBucketCounters.length; i2++) {
            intervals.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mBucketCounters[i2]);
        }
        return basic + ((Object) intervals);
    }

    public TelephonyHistogram(Parcel in) {
        this.mCategory = in.readInt();
        this.mId = in.readInt();
        this.mMinTimeMs = in.readInt();
        this.mMaxTimeMs = in.readInt();
        this.mAverageTimeMs = in.readInt();
        this.mSampleCount = in.readInt();
        if (in.readInt() == 1) {
            this.mInitialTimings = new int[10];
            in.readIntArray(this.mInitialTimings);
        }
        this.mBucketCount = in.readInt();
        this.mBucketEndPoints = new int[this.mBucketCount - 1];
        in.readIntArray(this.mBucketEndPoints);
        this.mBucketCounters = new int[this.mBucketCount];
        in.readIntArray(this.mBucketCounters);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mCategory);
        out.writeInt(this.mId);
        out.writeInt(this.mMinTimeMs);
        out.writeInt(this.mMaxTimeMs);
        out.writeInt(this.mAverageTimeMs);
        out.writeInt(this.mSampleCount);
        if (this.mInitialTimings == null) {
            out.writeInt(0);
        } else {
            out.writeInt(1);
            out.writeIntArray(this.mInitialTimings);
        }
        out.writeInt(this.mBucketCount);
        out.writeIntArray(this.mBucketEndPoints);
        out.writeIntArray(this.mBucketCounters);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
