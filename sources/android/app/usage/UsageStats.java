package android.app.usage;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.SparseIntArray;

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
            stats.mLastTimeVisible = in.readLong();
            stats.mLastTimeForegroundServiceUsed = in.readLong();
            stats.mTotalTimeInForeground = in.readLong();
            stats.mTotalTimeVisible = in.readLong();
            stats.mTotalTimeForegroundServiceUsed = in.readLong();
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
            readSparseIntArray(in, stats.mActivities);
            readBundleToEventMap(in.readBundle(), stats.mForegroundServices);
            return stats;
        }

        private void readSparseIntArray(Parcel in, SparseIntArray arr) {
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                int key = in.readInt();
                int value = in.readInt();
                arr.put(key, value);
            }
        }

        private void readBundleToEventMap(Bundle bundle, ArrayMap<String, Integer> eventMap) {
            if (bundle != null) {
                for (String className : bundle.keySet()) {
                    int event = bundle.getInt(className);
                    eventMap.put(className, Integer.valueOf(event));
                }
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UsageStats[] newArray(int size) {
            return new UsageStats[size];
        }
    };
    public SparseIntArray mActivities;
    public int mAppLaunchCount;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public long mBeginTimeStamp;
    public ArrayMap<String, ArrayMap<String, Integer>> mChooserCounts;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public long mEndTimeStamp;
    public ArrayMap<String, Integer> mForegroundServices;
    @UnsupportedAppUsage
    @Deprecated
    public int mLastEvent;
    public long mLastTimeForegroundServiceUsed;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public long mLastTimeUsed;
    public long mLastTimeVisible;
    @UnsupportedAppUsage
    public int mLaunchCount;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public String mPackageName;
    public long mTotalTimeForegroundServiceUsed;
    @UnsupportedAppUsage
    public long mTotalTimeInForeground;
    public long mTotalTimeVisible;

    public UsageStats() {
        this.mActivities = new SparseIntArray();
        this.mForegroundServices = new ArrayMap<>();
        this.mChooserCounts = new ArrayMap<>();
    }

    public UsageStats(UsageStats stats) {
        this.mActivities = new SparseIntArray();
        this.mForegroundServices = new ArrayMap<>();
        this.mChooserCounts = new ArrayMap<>();
        this.mPackageName = stats.mPackageName;
        this.mBeginTimeStamp = stats.mBeginTimeStamp;
        this.mEndTimeStamp = stats.mEndTimeStamp;
        this.mLastTimeUsed = stats.mLastTimeUsed;
        this.mLastTimeVisible = stats.mLastTimeVisible;
        this.mLastTimeForegroundServiceUsed = stats.mLastTimeForegroundServiceUsed;
        this.mTotalTimeInForeground = stats.mTotalTimeInForeground;
        this.mTotalTimeVisible = stats.mTotalTimeVisible;
        this.mTotalTimeForegroundServiceUsed = stats.mTotalTimeForegroundServiceUsed;
        this.mLaunchCount = stats.mLaunchCount;
        this.mAppLaunchCount = stats.mAppLaunchCount;
        this.mLastEvent = stats.mLastEvent;
        this.mActivities = stats.mActivities;
        this.mForegroundServices = stats.mForegroundServices;
        this.mChooserCounts = stats.mChooserCounts;
    }

    public UsageStats getObfuscatedForInstantApp() {
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

    public long getLastTimeVisible() {
        return this.mLastTimeVisible;
    }

    public long getTotalTimeInForeground() {
        return this.mTotalTimeInForeground;
    }

    public long getTotalTimeVisible() {
        return this.mTotalTimeVisible;
    }

    public long getLastTimeForegroundServiceUsed() {
        return this.mLastTimeForegroundServiceUsed;
    }

    public long getTotalTimeForegroundServiceUsed() {
        return this.mTotalTimeForegroundServiceUsed;
    }

    @SystemApi
    public int getAppLaunchCount() {
        return this.mAppLaunchCount;
    }

    private void mergeEventMap(SparseIntArray left, SparseIntArray right) {
        int size = right.size();
        for (int i = 0; i < size; i++) {
            int instanceId = right.keyAt(i);
            int event = right.valueAt(i);
            int index = left.indexOfKey(instanceId);
            if (index >= 0) {
                left.put(instanceId, Math.max(left.valueAt(index), event));
            } else {
                left.put(instanceId, event);
            }
        }
    }

    private void mergeEventMap(ArrayMap<String, Integer> left, ArrayMap<String, Integer> right) {
        int size = right.size();
        for (int i = 0; i < size; i++) {
            String className = right.keyAt(i);
            Integer event = right.valueAt(i);
            if (left.containsKey(className)) {
                left.put(className, Integer.valueOf(Math.max(left.get(className).intValue(), event.intValue())));
            } else {
                left.put(className, event);
            }
        }
    }

    public void add(UsageStats right) {
        if (!this.mPackageName.equals(right.mPackageName)) {
            throw new IllegalArgumentException("Can't merge UsageStats for package '" + this.mPackageName + "' with UsageStats for package '" + right.mPackageName + "'.");
        }
        if (right.mBeginTimeStamp > this.mBeginTimeStamp) {
            mergeEventMap(this.mActivities, right.mActivities);
            mergeEventMap(this.mForegroundServices, right.mForegroundServices);
            this.mLastTimeUsed = Math.max(this.mLastTimeUsed, right.mLastTimeUsed);
            this.mLastTimeVisible = Math.max(this.mLastTimeVisible, right.mLastTimeVisible);
            this.mLastTimeForegroundServiceUsed = Math.max(this.mLastTimeForegroundServiceUsed, right.mLastTimeForegroundServiceUsed);
        }
        this.mBeginTimeStamp = Math.min(this.mBeginTimeStamp, right.mBeginTimeStamp);
        this.mEndTimeStamp = Math.max(this.mEndTimeStamp, right.mEndTimeStamp);
        this.mTotalTimeInForeground += right.mTotalTimeInForeground;
        this.mTotalTimeVisible += right.mTotalTimeVisible;
        this.mTotalTimeForegroundServiceUsed += right.mTotalTimeForegroundServiceUsed;
        this.mLaunchCount += right.mLaunchCount;
        this.mAppLaunchCount += right.mAppLaunchCount;
        if (this.mChooserCounts == null) {
            this.mChooserCounts = right.mChooserCounts;
            return;
        }
        ArrayMap<String, ArrayMap<String, Integer>> arrayMap = right.mChooserCounts;
        if (arrayMap != null) {
            int chooserCountsSize = arrayMap.size();
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

    private boolean hasForegroundActivity() {
        int size = this.mActivities.size();
        for (int i = 0; i < size; i++) {
            if (this.mActivities.valueAt(i) == 1) {
                return true;
            }
        }
        return false;
    }

    private boolean hasVisibleActivity() {
        int size = this.mActivities.size();
        for (int i = 0; i < size; i++) {
            int type = this.mActivities.valueAt(i);
            if (type == 1 || type == 2) {
                return true;
            }
        }
        return false;
    }

    private boolean anyForegroundServiceStarted() {
        return !this.mForegroundServices.isEmpty();
    }

    private void incrementTimeUsed(long timeStamp) {
        long j = this.mLastTimeUsed;
        if (timeStamp > j) {
            this.mTotalTimeInForeground += timeStamp - j;
            this.mLastTimeUsed = timeStamp;
        }
    }

    private void incrementTimeVisible(long timeStamp) {
        long j = this.mLastTimeVisible;
        if (timeStamp > j) {
            this.mTotalTimeVisible += timeStamp - j;
            this.mLastTimeVisible = timeStamp;
        }
    }

    private void incrementServiceTimeUsed(long timeStamp) {
        long j = this.mLastTimeForegroundServiceUsed;
        if (timeStamp > j) {
            this.mTotalTimeForegroundServiceUsed += timeStamp - j;
            this.mLastTimeForegroundServiceUsed = timeStamp;
        }
    }

    private void updateActivity(String className, long timeStamp, int eventType, int instanceId) {
        if (eventType != 1 && eventType != 2 && eventType != 23 && eventType != 24) {
            return;
        }
        int index = this.mActivities.indexOfKey(instanceId);
        if (index >= 0) {
            int lastEvent = this.mActivities.valueAt(index);
            if (lastEvent == 1) {
                incrementTimeUsed(timeStamp);
                incrementTimeVisible(timeStamp);
            } else if (lastEvent == 2) {
                incrementTimeVisible(timeStamp);
            }
        }
        if (eventType == 1) {
            if (!hasVisibleActivity()) {
                this.mLastTimeUsed = timeStamp;
                this.mLastTimeVisible = timeStamp;
            } else if (!hasForegroundActivity()) {
                this.mLastTimeUsed = timeStamp;
            }
            this.mActivities.put(instanceId, eventType);
        } else if (eventType != 2) {
            if (eventType == 23 || eventType == 24) {
                this.mActivities.delete(instanceId);
            }
        } else {
            if (!hasVisibleActivity()) {
                this.mLastTimeVisible = timeStamp;
            }
            this.mActivities.put(instanceId, eventType);
        }
    }

    private void updateForegroundService(String className, long timeStamp, int eventType) {
        int intValue;
        if (eventType != 20 && eventType != 19) {
            return;
        }
        Integer lastEvent = this.mForegroundServices.get(className);
        if (lastEvent != null && ((intValue = lastEvent.intValue()) == 19 || intValue == 21)) {
            incrementServiceTimeUsed(timeStamp);
        }
        if (eventType != 19) {
            if (eventType == 20) {
                this.mForegroundServices.remove(className);
                return;
            }
            return;
        }
        if (!anyForegroundServiceStarted()) {
            this.mLastTimeForegroundServiceUsed = timeStamp;
        }
        this.mForegroundServices.put(className, Integer.valueOf(eventType));
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void update(java.lang.String r4, long r5, int r7, int r8) {
        /*
            r3 = this;
            r0 = 1
            if (r7 == r0) goto L56
            r1 = 2
            if (r7 == r1) goto L56
            r1 = 3
            if (r7 == r1) goto L43
            switch(r7) {
                case 19: goto L3f;
                case 20: goto L3f;
                case 21: goto L33;
                case 22: goto L29;
                case 23: goto L56;
                case 24: goto L56;
                case 25: goto Ld;
                case 26: goto Ld;
                default: goto Lc;
            }
        Lc:
            goto L5a
        Ld:
            boolean r1 = r3.hasForegroundActivity()
            if (r1 == 0) goto L16
            r3.incrementTimeUsed(r5)
        L16:
            boolean r1 = r3.hasVisibleActivity()
            if (r1 == 0) goto L1f
            r3.incrementTimeVisible(r5)
        L1f:
            boolean r1 = r3.anyForegroundServiceStarted()
            if (r1 == 0) goto L5a
            r3.incrementServiceTimeUsed(r5)
            goto L5a
        L29:
            boolean r1 = r3.anyForegroundServiceStarted()
            if (r1 == 0) goto L5a
            r3.incrementServiceTimeUsed(r5)
            goto L5a
        L33:
            r3.mLastTimeForegroundServiceUsed = r5
            android.util.ArrayMap<java.lang.String, java.lang.Integer> r1 = r3.mForegroundServices
            java.lang.Integer r2 = java.lang.Integer.valueOf(r7)
            r1.put(r4, r2)
            goto L5a
        L3f:
            r3.updateForegroundService(r4, r5, r7)
            goto L5a
        L43:
            boolean r1 = r3.hasForegroundActivity()
            if (r1 == 0) goto L4c
            r3.incrementTimeUsed(r5)
        L4c:
            boolean r1 = r3.hasVisibleActivity()
            if (r1 == 0) goto L5a
            r3.incrementTimeVisible(r5)
            goto L5a
        L56:
            r3.updateActivity(r4, r5, r7, r8)
        L5a:
            r3.mEndTimeStamp = r5
            if (r7 != r0) goto L63
            int r1 = r3.mLaunchCount
            int r1 = r1 + r0
            r3.mLaunchCount = r1
        L63:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.usage.UsageStats.update(java.lang.String, long, int, int):void");
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
        dest.writeLong(this.mLastTimeVisible);
        dest.writeLong(this.mLastTimeForegroundServiceUsed);
        dest.writeLong(this.mTotalTimeInForeground);
        dest.writeLong(this.mTotalTimeVisible);
        dest.writeLong(this.mTotalTimeForegroundServiceUsed);
        dest.writeInt(this.mLaunchCount);
        dest.writeInt(this.mAppLaunchCount);
        dest.writeInt(this.mLastEvent);
        Bundle allCounts = new Bundle();
        ArrayMap<String, ArrayMap<String, Integer>> arrayMap = this.mChooserCounts;
        if (arrayMap != null) {
            int chooserCountSize = arrayMap.size();
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
        writeSparseIntArray(dest, this.mActivities);
        dest.writeBundle(eventMapToBundle(this.mForegroundServices));
    }

    private void writeSparseIntArray(Parcel dest, SparseIntArray arr) {
        int size = arr.size();
        dest.writeInt(size);
        for (int i = 0; i < size; i++) {
            dest.writeInt(arr.keyAt(i));
            dest.writeInt(arr.valueAt(i));
        }
    }

    private Bundle eventMapToBundle(ArrayMap<String, Integer> eventMap) {
        Bundle bundle = new Bundle();
        int size = eventMap.size();
        for (int i = 0; i < size; i++) {
            bundle.putInt(eventMap.keyAt(i), eventMap.valueAt(i).intValue());
        }
        return bundle;
    }
}
