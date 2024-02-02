package com.android.internal.os;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.StrictMode;
import android.util.IntArray;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.KernelUidCpuTimeReaderBase;
import com.android.internal.util.Preconditions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Consumer;
/* loaded from: classes3.dex */
public class KernelUidCpuFreqTimeReader extends KernelUidCpuTimeReaderBase<Callback> {
    private static final String TAG = KernelUidCpuFreqTimeReader.class.getSimpleName();
    private static final int TOTAL_READ_ERROR_COUNT = 5;
    static final String UID_TIMES_PROC_FILE = "/proc/uid_time_in_state";
    private boolean mAllUidTimesAvailable;
    private long[] mCpuFreqs;
    private int mCpuFreqsCount;
    private long[] mCurTimes;
    private long[] mDeltaTimes;
    private SparseArray<long[]> mLastUidCpuFreqTimeMs;
    private boolean mPerClusterTimesAvailable;
    private final KernelCpuProcReader mProcReader;
    private int mReadErrorCounter;

    /* loaded from: classes3.dex */
    public interface Callback extends KernelUidCpuTimeReaderBase.Callback {
        synchronized void onUidCpuFreqTime(int i, long[] jArr);
    }

    public synchronized KernelUidCpuFreqTimeReader() {
        this.mLastUidCpuFreqTimeMs = new SparseArray<>();
        this.mAllUidTimesAvailable = true;
        this.mProcReader = KernelCpuProcReader.getFreqTimeReaderInstance();
    }

    @VisibleForTesting
    public synchronized KernelUidCpuFreqTimeReader(KernelCpuProcReader procReader) {
        this.mLastUidCpuFreqTimeMs = new SparseArray<>();
        this.mAllUidTimesAvailable = true;
        this.mProcReader = procReader;
    }

    public synchronized boolean perClusterTimesAvailable() {
        return this.mPerClusterTimesAvailable;
    }

    public synchronized boolean allUidTimesAvailable() {
        return this.mAllUidTimesAvailable;
    }

    public synchronized SparseArray<long[]> getAllUidCpuFreqTimeMs() {
        return this.mLastUidCpuFreqTimeMs;
    }

    public synchronized long[] readFreqs(PowerProfile powerProfile) {
        Preconditions.checkNotNull(powerProfile);
        if (this.mCpuFreqs != null) {
            return this.mCpuFreqs;
        }
        if (this.mAllUidTimesAvailable) {
            int oldMask = StrictMode.allowThreadDiskReadsMask();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(UID_TIMES_PROC_FILE));
                try {
                    long[] readFreqs = readFreqs(reader, powerProfile);
                    reader.close();
                    return readFreqs;
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        if (th != null) {
                            try {
                                reader.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                        } else {
                            reader.close();
                        }
                        throw th2;
                    }
                }
            } catch (IOException e) {
                int i = this.mReadErrorCounter + 1;
                this.mReadErrorCounter = i;
                if (i >= 5) {
                    this.mAllUidTimesAvailable = false;
                }
                String str = TAG;
                Slog.e(str, "Failed to read /proc/uid_time_in_state: " + e);
                return null;
            } finally {
                StrictMode.setThreadPolicyMask(oldMask);
            }
        }
        return null;
    }

    @VisibleForTesting
    public synchronized long[] readFreqs(BufferedReader reader, PowerProfile powerProfile) throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }
        String[] freqStr = line.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        this.mCpuFreqsCount = freqStr.length - 1;
        this.mCpuFreqs = new long[this.mCpuFreqsCount];
        this.mCurTimes = new long[this.mCpuFreqsCount];
        this.mDeltaTimes = new long[this.mCpuFreqsCount];
        for (int i = 0; i < this.mCpuFreqsCount; i++) {
            this.mCpuFreqs[i] = Long.parseLong(freqStr[i + 1], 10);
        }
        IntArray numClusterFreqs = extractClusterInfoFromProcFileFreqs();
        int numClusters = powerProfile.getNumCpuClusters();
        if (numClusterFreqs.size() == numClusters) {
            this.mPerClusterTimesAvailable = true;
            int i2 = 0;
            while (true) {
                if (i2 < numClusters) {
                    if (numClusterFreqs.get(i2) == powerProfile.getNumSpeedStepsInCpuCluster(i2)) {
                        i2++;
                    } else {
                        this.mPerClusterTimesAvailable = false;
                        break;
                    }
                } else {
                    break;
                }
            }
        } else {
            this.mPerClusterTimesAvailable = false;
        }
        Slog.i(TAG, "mPerClusterTimesAvailable=" + this.mPerClusterTimesAvailable);
        return this.mCpuFreqs;
    }

    @Override // com.android.internal.os.KernelUidCpuTimeReaderBase
    @VisibleForTesting
    public synchronized void readDeltaImpl(final Callback callback) {
        if (this.mCpuFreqs == null) {
            return;
        }
        readImpl(new Consumer() { // from class: com.android.internal.os.-$$Lambda$KernelUidCpuFreqTimeReader$_LfRKir9FA4B4VL15YGHagRZaR8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KernelUidCpuFreqTimeReader.lambda$readDeltaImpl$0(KernelUidCpuFreqTimeReader.this, callback, (IntBuffer) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$readDeltaImpl$0(KernelUidCpuFreqTimeReader kernelUidCpuFreqTimeReader, Callback callback, IntBuffer buf) {
        int uid = buf.get();
        long[] lastTimes = kernelUidCpuFreqTimeReader.mLastUidCpuFreqTimeMs.get(uid);
        if (lastTimes == null) {
            lastTimes = new long[kernelUidCpuFreqTimeReader.mCpuFreqsCount];
            kernelUidCpuFreqTimeReader.mLastUidCpuFreqTimeMs.put(uid, lastTimes);
        }
        if (!kernelUidCpuFreqTimeReader.getFreqTimeForUid(buf, kernelUidCpuFreqTimeReader.mCurTimes)) {
            return;
        }
        boolean valid = true;
        boolean valid2 = false;
        for (int i = 0; i < kernelUidCpuFreqTimeReader.mCpuFreqsCount; i++) {
            kernelUidCpuFreqTimeReader.mDeltaTimes[i] = kernelUidCpuFreqTimeReader.mCurTimes[i] - lastTimes[i];
            if (kernelUidCpuFreqTimeReader.mDeltaTimes[i] < 0) {
                Slog.e(TAG, "Negative delta from freq time proc: " + kernelUidCpuFreqTimeReader.mDeltaTimes[i]);
                valid = false;
            }
            valid2 |= kernelUidCpuFreqTimeReader.mDeltaTimes[i] > 0;
        }
        if (valid2 && valid) {
            System.arraycopy(kernelUidCpuFreqTimeReader.mCurTimes, 0, lastTimes, 0, kernelUidCpuFreqTimeReader.mCpuFreqsCount);
            if (callback != null) {
                callback.onUidCpuFreqTime(uid, kernelUidCpuFreqTimeReader.mDeltaTimes);
            }
        }
    }

    public synchronized void readAbsolute(final Callback callback) {
        readImpl(new Consumer() { // from class: com.android.internal.os.-$$Lambda$KernelUidCpuFreqTimeReader$s7iJKg0yjXXtqM4hsU8GS_gavIY
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KernelUidCpuFreqTimeReader.lambda$readAbsolute$1(KernelUidCpuFreqTimeReader.this, callback, (IntBuffer) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$readAbsolute$1(KernelUidCpuFreqTimeReader kernelUidCpuFreqTimeReader, Callback callback, IntBuffer buf) {
        int uid = buf.get();
        if (kernelUidCpuFreqTimeReader.getFreqTimeForUid(buf, kernelUidCpuFreqTimeReader.mCurTimes)) {
            callback.onUidCpuFreqTime(uid, kernelUidCpuFreqTimeReader.mCurTimes);
        }
    }

    private synchronized boolean getFreqTimeForUid(IntBuffer buffer, long[] freqTime) {
        boolean valid = true;
        for (int i = 0; i < this.mCpuFreqsCount; i++) {
            freqTime[i] = buffer.get() * 10;
            if (freqTime[i] < 0) {
                String str = TAG;
                Slog.e(str, "Negative time from freq time proc: " + freqTime[i]);
                valid = false;
            }
        }
        return valid;
    }

    private synchronized void readImpl(Consumer<IntBuffer> processUid) {
        synchronized (this.mProcReader) {
            ByteBuffer bytes = this.mProcReader.readBytes();
            if (bytes != null && bytes.remaining() > 4) {
                if ((bytes.remaining() & 3) != 0) {
                    String str = TAG;
                    Slog.wtf(str, "Cannot parse freq time proc bytes to int: " + bytes.remaining());
                    return;
                }
                IntBuffer buf = bytes.asIntBuffer();
                int freqs = buf.get();
                if (freqs != this.mCpuFreqsCount) {
                    String str2 = TAG;
                    Slog.wtf(str2, "Cpu freqs expect " + this.mCpuFreqsCount + " , got " + freqs);
                } else if (buf.remaining() % (freqs + 1) != 0) {
                    String str3 = TAG;
                    Slog.wtf(str3, "Freq time format error: " + buf.remaining() + " / " + (freqs + 1));
                } else {
                    int numUids = buf.remaining() / (freqs + 1);
                    for (int i = 0; i < numUids; i++) {
                        processUid.accept(buf);
                    }
                }
            }
        }
    }

    public synchronized void removeUid(int uid) {
        this.mLastUidCpuFreqTimeMs.delete(uid);
    }

    public synchronized void removeUidsInRange(int startUid, int endUid) {
        this.mLastUidCpuFreqTimeMs.put(startUid, null);
        this.mLastUidCpuFreqTimeMs.put(endUid, null);
        int firstIndex = this.mLastUidCpuFreqTimeMs.indexOfKey(startUid);
        int lastIndex = this.mLastUidCpuFreqTimeMs.indexOfKey(endUid);
        this.mLastUidCpuFreqTimeMs.removeAtRange(firstIndex, (lastIndex - firstIndex) + 1);
    }

    private synchronized IntArray extractClusterInfoFromProcFileFreqs() {
        IntArray numClusterFreqs = new IntArray();
        int freqsFound = 0;
        for (int i = 0; i < this.mCpuFreqsCount; i++) {
            freqsFound++;
            if (i + 1 == this.mCpuFreqsCount || this.mCpuFreqs[i + 1] <= this.mCpuFreqs[i]) {
                numClusterFreqs.add(freqsFound);
                freqsFound = 0;
            }
        }
        return numClusterFreqs;
    }
}
