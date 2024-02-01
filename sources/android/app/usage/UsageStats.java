package android.app.usage;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
/* loaded from: classes.dex */
public final class UsageStats implements Parcelable {
    public static final Parcelable.Creator<UsageStats> CREATOR = new Parcelable.Creator<UsageStats>() { // from class: android.app.usage.UsageStats.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UsageStats createFromParcel(Parcel in) {
            UsageStats stats = new UsageStats();
            stats.mPackageName = in.readString();
            stats.mBeginTimeStamp = in.readLong();
            stats.mEndTimeStamp = in.readLong();
            stats.mLastTimeUsed = in.readLong();
            stats.mTotalTimeInForeground = in.readLong();
            stats.mLaunchCount = in.readInt();
            stats.mAppLaunchCount = in.readInt();
            stats.mLastEvent = in.readInt();
            Bundle allCounts = in.readBundle();
            if (allCounts != null) {
                stats.mChooserCounts = new ArrayMap<>();
                for (String action : allCounts.keySet()) {
                    if (!stats.mChooserCounts.containsKey(action)) {
                        ArrayMap<String, Integer> newCounts = new ArrayMap<>();
                        stats.mChooserCounts.put(action, newCounts);
                    }
                    Bundle currentCounts = allCounts.getBundle(action);
                    if (currentCounts != null) {
                        for (String key : currentCounts.keySet()) {
                            int value = currentCounts.getInt(key);
                            if (value > 0) {
                                stats.mChooserCounts.get(action).put(key, Integer.valueOf(value));
                            }
                        }
                    }
                }
            }
            return stats;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UsageStats[] newArray(int size) {
            return new UsageStats[size];
        }
    };
    public int mAppLaunchCount;
    private protected long mBeginTimeStamp;
    public ArrayMap<String, ArrayMap<String, Integer>> mChooserCounts;
    private protected long mEndTimeStamp;
    private protected int mLastEvent;
    private protected long mLastTimeUsed;
    private protected int mLaunchCount;
    private protected String mPackageName;
    private protected long mTotalTimeInForeground;

    public synchronized UsageStats() {
    }

    public UsageStats(UsageStats stats) {
        this.mPackageName = stats.mPackageName;
        this.mBeginTimeStamp = stats.mBeginTimeStamp;
        this.mEndTimeStamp = stats.mEndTimeStamp;
        this.mLastTimeUsed = stats.mLastTimeUsed;
        this.mTotalTimeInForeground = stats.mTotalTimeInForeground;
        this.mLaunchCount = stats.mLaunchCount;
        this.mAppLaunchCount = stats.mAppLaunchCount;
        this.mLastEvent = stats.mLastEvent;
        this.mChooserCounts = stats.mChooserCounts;
    }

    public synchronized UsageStats getObfuscatedForInstantApp() {
        UsageStats ret = new UsageStats(this);
        ret.mPackageName = UsageEvents.INSTANT_APP_PACKAGE_NAME;
        return ret;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public long getFirstTimeStamp() {
        return this.mBeginTimeStamp;
    }

    public long getLastTimeStamp() {
        return this.mEndTimeStamp;
    }

    public long getLastTimeUsed() {
        return this.mLastTimeUsed;
    }

    public long getTotalTimeInForeground() {
        return this.mTotalTimeInForeground;
    }

    @SystemApi
    public int getAppLaunchCount() {
        return this.mAppLaunchCount;
    }

    public void add(UsageStats right) {
        if (!this.mPackageName.equals(right.mPackageName)) {
            throw new IllegalArgumentException("Can't merge UsageStats for package '" + this.mPackageName + "' with UsageStats for package '" + right.mPackageName + "'.");
        }
        if (right.mBeginTimeStamp > this.mBeginTimeStamp) {
            this.mLastEvent = Math.max(this.mLastEvent, right.mLastEvent);
            this.mLastTimeUsed = Math.max(this.mLastTimeUsed, right.mLastTimeUsed);
        }
        this.mBeginTimeStamp = Math.min(this.mBeginTimeStamp, right.mBeginTimeStamp);
        this.mEndTimeStamp = Math.max(this.mEndTimeStamp, right.mEndTimeStamp);
        this.mTotalTimeInForeground += right.mTotalTimeInForeground;
        this.mLaunchCount += right.mLaunchCount;
        this.mAppLaunchCount += right.mAppLaunchCount;
        if (this.mChooserCounts == null) {
            this.mChooserCounts = right.mChooserCounts;
        } else if (right.mChooserCounts != null) {
            int chooserCountsSize = right.mChooserCounts.size();
            for (int i = 0; i < chooserCountsSize; i++) {
                String action = right.mChooserCounts.keyAt(i);
                ArrayMap<String, Integer> counts = right.mChooserCounts.valueAt(i);
                if (!this.mChooserCounts.containsKey(action) || this.mChooserCounts.get(action) == null) {
                    this.mChooserCounts.put(action, counts);
                } else {
                    int annotationSize = counts.size();
                    for (int j = 0; j < annotationSize; j++) {
                        String key = counts.keyAt(j);
                        int rightValue = counts.valueAt(j).intValue();
                        int leftValue = this.mChooserCounts.get(action).getOrDefault(key, 0).intValue();
                        this.mChooserCounts.get(action).put(key, Integer.valueOf(leftValue + rightValue));
                    }
                }
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPackageName);
        dest.writeLong(this.mBeginTimeStamp);
        dest.writeLong(this.mEndTimeStamp);
        dest.writeLong(this.mLastTimeUsed);
        dest.writeLong(this.mTotalTimeInForeground);
        dest.writeInt(this.mLaunchCount);
        dest.writeInt(this.mAppLaunchCount);
        dest.writeInt(this.mLastEvent);
        Bundle allCounts = new Bundle();
        if (this.mChooserCounts != null) {
            int chooserCountSize = this.mChooserCounts.size();
            for (int i = 0; i < chooserCountSize; i++) {
                String action = this.mChooserCounts.keyAt(i);
                ArrayMap<String, Integer> counts = this.mChooserCounts.valueAt(i);
                Bundle currentCounts = new Bundle();
                int annotationSize = counts.size();
                for (int j = 0; j < annotationSize; j++) {
                    currentCounts.putInt(counts.keyAt(j), counts.valueAt(j).intValue());
                }
                allCounts.putBundle(action, currentCounts);
            }
        }
        dest.writeBundle(allCounts);
    }
}
