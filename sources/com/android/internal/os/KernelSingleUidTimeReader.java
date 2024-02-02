package com.android.internal.os;

import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: classes3.dex */
public class KernelSingleUidTimeReader {
    @VisibleForTesting
    public static final int TOTAL_READ_ERROR_COUNT = 5;
    private final boolean DBG;
    private final String PROC_FILE_DIR;
    private final String PROC_FILE_NAME;
    private final String TAG;
    @GuardedBy("this")
    private final int mCpuFreqsCount;
    @GuardedBy("this")
    private boolean mCpuFreqsCountVerified;
    @GuardedBy("this")
    private boolean mHasStaleData;
    private final Injector mInjector;
    @GuardedBy("this")
    private SparseArray<long[]> mLastUidCpuTimeMs;
    @GuardedBy("this")
    private int mReadErrorCounter;
    @GuardedBy("this")
    private boolean mSingleUidCpuTimesAvailable;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized KernelSingleUidTimeReader(int cpuFreqsCount) {
        this(cpuFreqsCount, new Injector());
    }

    public synchronized KernelSingleUidTimeReader(int cpuFreqsCount, Injector injector) {
        this.TAG = KernelUidCpuFreqTimeReader.class.getName();
        this.DBG = false;
        this.PROC_FILE_DIR = "/proc/uid/";
        this.PROC_FILE_NAME = "/time_in_state";
        this.mLastUidCpuTimeMs = new SparseArray<>();
        this.mSingleUidCpuTimesAvailable = true;
        this.mInjector = injector;
        this.mCpuFreqsCount = cpuFreqsCount;
        if (this.mCpuFreqsCount == 0) {
            this.mSingleUidCpuTimesAvailable = false;
        }
    }

    public synchronized boolean singleUidCpuTimesAvailable() {
        return this.mSingleUidCpuTimesAvailable;
    }

    public synchronized long[] readDeltaMs(int uid) {
        if (this.mSingleUidCpuTimesAvailable) {
            String procFile = "/proc/uid/" + uid + "/time_in_state";
            try {
                byte[] data = this.mInjector.readData(procFile);
                if (!this.mCpuFreqsCountVerified) {
                    verifyCpuFreqsCount(data.length, procFile);
                }
                ByteBuffer buffer = ByteBuffer.wrap(data);
                buffer.order(ByteOrder.nativeOrder());
                long[] cpuTimesMs = readCpuTimesFromByteBuffer(buffer);
                return computeDelta(uid, cpuTimesMs);
            } catch (Exception e) {
                int i = this.mReadErrorCounter + 1;
                this.mReadErrorCounter = i;
                if (i >= 5) {
                    this.mSingleUidCpuTimesAvailable = false;
                }
                return null;
            }
        }
        return null;
    }

    private synchronized void verifyCpuFreqsCount(int numBytes, String procFile) {
        int actualCount = numBytes / 8;
        if (this.mCpuFreqsCount != actualCount) {
            this.mSingleUidCpuTimesAvailable = false;
            throw new IllegalStateException("Freq count didn't match,count from /proc/uid_time_in_state=" + this.mCpuFreqsCount + ", butcount from " + procFile + "=" + actualCount);
        }
        this.mCpuFreqsCountVerified = true;
    }

    private synchronized long[] readCpuTimesFromByteBuffer(ByteBuffer buffer) {
        long[] cpuTimesMs = new long[this.mCpuFreqsCount];
        for (int i = 0; i < this.mCpuFreqsCount; i++) {
            cpuTimesMs[i] = buffer.getLong() * 10;
        }
        return cpuTimesMs;
    }

    public synchronized long[] computeDelta(int uid, long[] latestCpuTimesMs) {
        if (this.mSingleUidCpuTimesAvailable) {
            long[] lastCpuTimesMs = this.mLastUidCpuTimeMs.get(uid);
            long[] deltaTimesMs = getDeltaLocked(lastCpuTimesMs, latestCpuTimesMs);
            if (deltaTimesMs == null) {
                return null;
            }
            boolean hasNonZero = false;
            int i = deltaTimesMs.length - 1;
            while (true) {
                if (i < 0) {
                    break;
                } else if (deltaTimesMs[i] <= 0) {
                    i--;
                } else {
                    hasNonZero = true;
                    break;
                }
            }
            if (hasNonZero) {
                this.mLastUidCpuTimeMs.put(uid, latestCpuTimesMs);
                return deltaTimesMs;
            }
            return null;
        }
        return null;
    }

    @GuardedBy("this")
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public synchronized long[] getDeltaLocked(long[] lastCpuTimesMs, long[] latestCpuTimesMs) {
        int i = latestCpuTimesMs.length;
        while (true) {
            i--;
            if (i >= 0) {
                if (latestCpuTimesMs[i] < 0) {
                    return null;
                }
            } else if (lastCpuTimesMs == null) {
                return latestCpuTimesMs;
            } else {
                long[] deltaTimesMs = new long[latestCpuTimesMs.length];
                for (int i2 = latestCpuTimesMs.length - 1; i2 >= 0; i2--) {
                    deltaTimesMs[i2] = latestCpuTimesMs[i2] - lastCpuTimesMs[i2];
                    if (deltaTimesMs[i2] < 0) {
                        return null;
                    }
                }
                return deltaTimesMs;
            }
        }
    }

    public synchronized void markDataAsStale(boolean hasStaleData) {
        this.mHasStaleData = hasStaleData;
    }

    public synchronized boolean hasStaleData() {
        return this.mHasStaleData;
    }

    public synchronized void setAllUidsCpuTimesMs(SparseArray<long[]> allUidsCpuTimesMs) {
        this.mLastUidCpuTimeMs.clear();
        for (int i = allUidsCpuTimesMs.size() - 1; i >= 0; i--) {
            long[] cpuTimesMs = allUidsCpuTimesMs.valueAt(i);
            if (cpuTimesMs != null) {
                this.mLastUidCpuTimeMs.put(allUidsCpuTimesMs.keyAt(i), (long[]) cpuTimesMs.clone());
            }
        }
    }

    public synchronized void removeUid(int uid) {
        this.mLastUidCpuTimeMs.delete(uid);
    }

    public synchronized void removeUidsInRange(int startUid, int endUid) {
        if (endUid < startUid) {
            return;
        }
        synchronized (this) {
            this.mLastUidCpuTimeMs.put(startUid, null);
            this.mLastUidCpuTimeMs.put(endUid, null);
            int startIdx = this.mLastUidCpuTimeMs.indexOfKey(startUid);
            int endIdx = this.mLastUidCpuTimeMs.indexOfKey(endUid);
            this.mLastUidCpuTimeMs.removeAtRange(startIdx, (endIdx - startIdx) + 1);
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public static class Injector {
        public synchronized byte[] readData(String procFile) throws IOException {
            return Files.readAllBytes(Paths.get(procFile, new String[0]));
        }
    }

    @VisibleForTesting
    public synchronized SparseArray<long[]> getLastUidCpuTimeMs() {
        return this.mLastUidCpuTimeMs;
    }

    @VisibleForTesting
    public synchronized void setSingleUidCpuTimesAvailable(boolean singleUidCpuTimesAvailable) {
        this.mSingleUidCpuTimesAvailable = singleUidCpuTimesAvailable;
    }
}
