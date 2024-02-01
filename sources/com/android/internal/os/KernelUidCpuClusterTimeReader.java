package com.android.internal.os;

import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.KernelUidCpuTimeReaderBase;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Consumer;
/* loaded from: classes3.dex */
public class KernelUidCpuClusterTimeReader extends KernelUidCpuTimeReaderBase<Callback> {
    private static final String TAG = KernelUidCpuClusterTimeReader.class.getSimpleName();
    private double[] mCurTime;
    private long[] mCurTimeRounded;
    private long[] mDeltaTime;
    private SparseArray<double[]> mLastUidPolicyTimeMs;
    private int mNumClusters;
    private int mNumCores;
    private int[] mNumCoresOnCluster;
    private final KernelCpuProcReader mProcReader;

    /* loaded from: classes3.dex */
    public interface Callback extends KernelUidCpuTimeReaderBase.Callback {
        synchronized void onUidCpuPolicyTime(int i, long[] jArr);
    }

    public synchronized KernelUidCpuClusterTimeReader() {
        this.mLastUidPolicyTimeMs = new SparseArray<>();
        this.mNumClusters = -1;
        this.mProcReader = KernelCpuProcReader.getClusterTimeReaderInstance();
    }

    @VisibleForTesting
    public synchronized KernelUidCpuClusterTimeReader(KernelCpuProcReader procReader) {
        this.mLastUidPolicyTimeMs = new SparseArray<>();
        this.mNumClusters = -1;
        this.mProcReader = procReader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.internal.os.KernelUidCpuTimeReaderBase
    public synchronized void readDeltaImpl(final Callback cb) {
        readImpl(new Consumer() { // from class: com.android.internal.os.-$$Lambda$KernelUidCpuClusterTimeReader$j4vHMa0qvl5KRBiWr-LkFJbasC8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KernelUidCpuClusterTimeReader.lambda$readDeltaImpl$0(KernelUidCpuClusterTimeReader.this, cb, (IntBuffer) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$readDeltaImpl$0(KernelUidCpuClusterTimeReader kernelUidCpuClusterTimeReader, Callback cb, IntBuffer buf) {
        int uid = buf.get();
        double[] lastTimes = kernelUidCpuClusterTimeReader.mLastUidPolicyTimeMs.get(uid);
        if (lastTimes == null) {
            lastTimes = new double[kernelUidCpuClusterTimeReader.mNumClusters];
            kernelUidCpuClusterTimeReader.mLastUidPolicyTimeMs.put(uid, lastTimes);
        }
        if (!kernelUidCpuClusterTimeReader.sumClusterTime(buf, kernelUidCpuClusterTimeReader.mCurTime)) {
            return;
        }
        boolean notify = false;
        boolean valid = true;
        for (int i = 0; i < kernelUidCpuClusterTimeReader.mNumClusters; i++) {
            kernelUidCpuClusterTimeReader.mDeltaTime[i] = (long) (kernelUidCpuClusterTimeReader.mCurTime[i] - lastTimes[i]);
            if (kernelUidCpuClusterTimeReader.mDeltaTime[i] < 0) {
                Slog.e(TAG, "Negative delta from cluster time proc: " + kernelUidCpuClusterTimeReader.mDeltaTime[i]);
                valid = false;
            }
            notify |= kernelUidCpuClusterTimeReader.mDeltaTime[i] > 0;
        }
        if (notify && valid) {
            System.arraycopy(kernelUidCpuClusterTimeReader.mCurTime, 0, lastTimes, 0, kernelUidCpuClusterTimeReader.mNumClusters);
            if (cb != null) {
                cb.onUidCpuPolicyTime(uid, kernelUidCpuClusterTimeReader.mDeltaTime);
            }
        }
    }

    public synchronized void readAbsolute(final Callback callback) {
        readImpl(new Consumer() { // from class: com.android.internal.os.-$$Lambda$KernelUidCpuClusterTimeReader$SvNbuRWT162Eb4ur1GVE0r4GiDo
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KernelUidCpuClusterTimeReader.lambda$readAbsolute$1(KernelUidCpuClusterTimeReader.this, callback, (IntBuffer) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$readAbsolute$1(KernelUidCpuClusterTimeReader kernelUidCpuClusterTimeReader, Callback callback, IntBuffer buf) {
        int uid = buf.get();
        if (kernelUidCpuClusterTimeReader.sumClusterTime(buf, kernelUidCpuClusterTimeReader.mCurTime)) {
            for (int i = 0; i < kernelUidCpuClusterTimeReader.mNumClusters; i++) {
                kernelUidCpuClusterTimeReader.mCurTimeRounded[i] = (long) kernelUidCpuClusterTimeReader.mCurTime[i];
            }
            callback.onUidCpuPolicyTime(uid, kernelUidCpuClusterTimeReader.mCurTimeRounded);
        }
    }

    private synchronized boolean sumClusterTime(IntBuffer buffer, double[] clusterTime) {
        boolean valid = true;
        for (int i = 0; i < this.mNumClusters; i++) {
            clusterTime[i] = 0.0d;
            for (int j = 1; j <= this.mNumCoresOnCluster[i]; j++) {
                int time = buffer.get();
                if (time < 0) {
                    String str = TAG;
                    Slog.e(str, "Negative time from cluster time proc: " + time);
                    valid = false;
                }
                clusterTime[i] = clusterTime[i] + ((time * 10.0d) / j);
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
                    Slog.wtf(str, "Cannot parse cluster time proc bytes to int: " + bytes.remaining());
                    return;
                }
                IntBuffer buf = bytes.asIntBuffer();
                int numClusters = buf.get();
                if (numClusters <= 0) {
                    String str2 = TAG;
                    Slog.wtf(str2, "Cluster time format error: " + numClusters);
                    return;
                }
                if (this.mNumClusters == -1) {
                    this.mNumClusters = numClusters;
                }
                if (buf.remaining() < numClusters) {
                    String str3 = TAG;
                    Slog.wtf(str3, "Too few data left in the buffer: " + buf.remaining());
                    return;
                }
                if (this.mNumCores <= 0) {
                    if (!readCoreInfo(buf, numClusters)) {
                        return;
                    }
                } else {
                    buf.position(buf.position() + numClusters);
                }
                if (buf.remaining() % (this.mNumCores + 1) != 0) {
                    String str4 = TAG;
                    Slog.wtf(str4, "Cluster time format error: " + buf.remaining() + " / " + (this.mNumCores + 1));
                    return;
                }
                int numUids = buf.remaining() / (this.mNumCores + 1);
                for (int i = 0; i < numUids; i++) {
                    processUid.accept(buf);
                }
            }
        }
    }

    private synchronized boolean readCoreInfo(IntBuffer buf, int numClusters) {
        int[] numCoresOnCluster = new int[numClusters];
        int numCores = 0;
        for (int numCores2 = 0; numCores2 < numClusters; numCores2++) {
            numCoresOnCluster[numCores2] = buf.get();
            numCores += numCoresOnCluster[numCores2];
        }
        if (numCores <= 0) {
            Slog.e(TAG, "Invalid # cores from cluster time proc file: " + numCores);
            return false;
        }
        this.mNumCores = numCores;
        this.mNumCoresOnCluster = numCoresOnCluster;
        this.mCurTime = new double[numClusters];
        this.mDeltaTime = new long[numClusters];
        this.mCurTimeRounded = new long[numClusters];
        return true;
    }

    public synchronized void removeUid(int uid) {
        this.mLastUidPolicyTimeMs.delete(uid);
    }

    public synchronized void removeUidsInRange(int startUid, int endUid) {
        this.mLastUidPolicyTimeMs.put(startUid, null);
        this.mLastUidPolicyTimeMs.put(endUid, null);
        int firstIndex = this.mLastUidPolicyTimeMs.indexOfKey(startUid);
        int lastIndex = this.mLastUidPolicyTimeMs.indexOfKey(endUid);
        this.mLastUidPolicyTimeMs.removeAtRange(firstIndex, (lastIndex - firstIndex) + 1);
    }
}
