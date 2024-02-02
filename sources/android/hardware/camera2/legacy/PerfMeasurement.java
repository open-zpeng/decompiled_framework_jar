package android.hardware.camera2.legacy;

import android.os.SystemClock;
import android.provider.SettingsStringUtil;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
/* loaded from: classes.dex */
class PerfMeasurement {
    private protected static final int DEFAULT_MAX_QUERIES = 3;
    public protected static final long FAILED_TIMING = -2;
    public protected static final long NO_DURATION_YET = -1;
    public protected static final String TAG = "PerfMeasurement";
    public protected ArrayList<Long> mCollectedCpuDurations;
    public protected ArrayList<Long> mCollectedGpuDurations;
    public protected ArrayList<Long> mCollectedTimestamps;
    public protected int mCompletedQueryCount;
    public protected Queue<Long> mCpuDurationsQueue;
    public protected final long mNativeContext;
    public protected long mStartTimeNs;
    public protected Queue<Long> mTimestampQueue;

    public protected static native long nativeCreateContext(int i);

    public protected static native void nativeDeleteContext(long j);

    public private static native long nativeGetNextGlDuration(long j);

    public protected static native boolean nativeQuerySupport();

    public private static native void nativeStartGlTimer(long j);

    public private static native void nativeStopGlTimer(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized PerfMeasurement() {
        this.mCompletedQueryCount = 0;
        this.mCollectedGpuDurations = new ArrayList<>();
        this.mCollectedCpuDurations = new ArrayList<>();
        this.mCollectedTimestamps = new ArrayList<>();
        this.mTimestampQueue = new LinkedList();
        this.mCpuDurationsQueue = new LinkedList();
        this.mNativeContext = nativeCreateContext(3);
    }

    private protected synchronized PerfMeasurement(int maxQueries) {
        this.mCompletedQueryCount = 0;
        this.mCollectedGpuDurations = new ArrayList<>();
        this.mCollectedCpuDurations = new ArrayList<>();
        this.mCollectedTimestamps = new ArrayList<>();
        this.mTimestampQueue = new LinkedList();
        this.mCpuDurationsQueue = new LinkedList();
        if (maxQueries < 1) {
            throw new IllegalArgumentException("maxQueries is less than 1");
        }
        this.mNativeContext = nativeCreateContext(maxQueries);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isGlTimingSupported() {
        return nativeQuerySupport();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dumpPerformanceData(String path) {
        try {
            BufferedWriter dump = new BufferedWriter(new FileWriter(path));
            dump.write("timestamp gpu_duration cpu_duration\n");
            for (int i = 0; i < this.mCollectedGpuDurations.size(); i++) {
                dump.write(String.format("%d %d %d\n", this.mCollectedTimestamps.get(i), this.mCollectedGpuDurations.get(i), this.mCollectedCpuDurations.get(i)));
            }
            this.mCollectedTimestamps.clear();
            this.mCollectedGpuDurations.clear();
            this.mCollectedCpuDurations.clear();
            dump.close();
        } catch (IOException e) {
            Log.e(TAG, "Error writing data dump to " + path + SettingsStringUtil.DELIMITER + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startTimer() {
        nativeStartGlTimer(this.mNativeContext);
        this.mStartTimeNs = SystemClock.elapsedRealtimeNanos();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void stopTimer() {
        long longValue;
        long endTimeNs = SystemClock.elapsedRealtimeNanos();
        this.mCpuDurationsQueue.add(Long.valueOf(endTimeNs - this.mStartTimeNs));
        nativeStopGlTimer(this.mNativeContext);
        long duration = getNextGlDuration();
        if (duration > 0) {
            this.mCollectedGpuDurations.add(Long.valueOf(duration));
            ArrayList<Long> arrayList = this.mCollectedTimestamps;
            if (this.mTimestampQueue.isEmpty()) {
                longValue = -1;
            } else {
                longValue = this.mTimestampQueue.poll().longValue();
            }
            arrayList.add(Long.valueOf(longValue));
            this.mCollectedCpuDurations.add(Long.valueOf(this.mCpuDurationsQueue.isEmpty() ? -1L : this.mCpuDurationsQueue.poll().longValue()));
        }
        if (duration == -2) {
            if (!this.mTimestampQueue.isEmpty()) {
                this.mTimestampQueue.poll();
            }
            if (!this.mCpuDurationsQueue.isEmpty()) {
                this.mCpuDurationsQueue.poll();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void addTimestamp(long timestamp) {
        this.mTimestampQueue.add(Long.valueOf(timestamp));
    }

    public protected synchronized long getNextGlDuration() {
        long duration = nativeGetNextGlDuration(this.mNativeContext);
        if (duration > 0) {
            this.mCompletedQueryCount++;
        }
        return duration;
    }

    private protected synchronized int getCompletedQueryCount() {
        return this.mCompletedQueryCount;
    }

    protected void finalize() {
        nativeDeleteContext(this.mNativeContext);
    }
}
