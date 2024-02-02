package com.android.internal.os;

import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.KernelUidCpuTimeReaderBase;
import com.xiaopeng.util.FeatureOption;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Consumer;
/* loaded from: classes3.dex */
public class KernelUidCpuActiveTimeReader extends KernelUidCpuTimeReaderBase<Callback> {
    private static final String TAG = KernelUidCpuActiveTimeReader.class.getSimpleName();
    private int mCores;
    private SparseArray<Double> mLastUidCpuActiveTimeMs;
    private final KernelCpuProcReader mProcReader;

    /* loaded from: classes3.dex */
    public interface Callback extends KernelUidCpuTimeReaderBase.Callback {
        synchronized void onUidCpuActiveTime(int i, long j);
    }

    public synchronized KernelUidCpuActiveTimeReader() {
        this.mLastUidCpuActiveTimeMs = new SparseArray<>();
        this.mProcReader = KernelCpuProcReader.getActiveTimeReaderInstance();
    }

    @VisibleForTesting
    public synchronized KernelUidCpuActiveTimeReader(KernelCpuProcReader procReader) {
        this.mLastUidCpuActiveTimeMs = new SparseArray<>();
        this.mProcReader = procReader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.internal.os.KernelUidCpuTimeReaderBase
    public synchronized void readDeltaImpl(final Callback callback) {
        readImpl(new Consumer() { // from class: com.android.internal.os.-$$Lambda$KernelUidCpuActiveTimeReader$bd1LhtH6p3uJgMUQoWfE2Qs8bRc
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KernelUidCpuActiveTimeReader.lambda$readDeltaImpl$0(KernelUidCpuActiveTimeReader.this, callback, (IntBuffer) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$readDeltaImpl$0(KernelUidCpuActiveTimeReader kernelUidCpuActiveTimeReader, Callback callback, IntBuffer buf) {
        int uid = buf.get();
        double activeTime = kernelUidCpuActiveTimeReader.sumActiveTime(buf);
        if (activeTime > FeatureOption.FO_BOOT_POLICY_CPU) {
            double delta = activeTime - kernelUidCpuActiveTimeReader.mLastUidCpuActiveTimeMs.get(uid, Double.valueOf((double) FeatureOption.FO_BOOT_POLICY_CPU)).doubleValue();
            if (delta > FeatureOption.FO_BOOT_POLICY_CPU) {
                kernelUidCpuActiveTimeReader.mLastUidCpuActiveTimeMs.put(uid, Double.valueOf(activeTime));
                if (callback != null) {
                    callback.onUidCpuActiveTime(uid, (long) delta);
                }
            } else if (delta < FeatureOption.FO_BOOT_POLICY_CPU) {
                String str = TAG;
                Slog.e(str, "Negative delta from active time proc: " + delta);
            }
        }
    }

    public synchronized void readAbsolute(final Callback callback) {
        readImpl(new Consumer() { // from class: com.android.internal.os.-$$Lambda$KernelUidCpuActiveTimeReader$uXm3GBhF7PBpo0hLrva14EQYjPA
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KernelUidCpuActiveTimeReader.lambda$readAbsolute$1(KernelUidCpuActiveTimeReader.this, callback, (IntBuffer) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$readAbsolute$1(KernelUidCpuActiveTimeReader kernelUidCpuActiveTimeReader, Callback callback, IntBuffer buf) {
        int uid = buf.get();
        double activeTime = kernelUidCpuActiveTimeReader.sumActiveTime(buf);
        if (activeTime > FeatureOption.FO_BOOT_POLICY_CPU) {
            callback.onUidCpuActiveTime(uid, (long) activeTime);
        }
    }

    private synchronized double sumActiveTime(IntBuffer buffer) {
        double sum = FeatureOption.FO_BOOT_POLICY_CPU;
        boolean corrupted = false;
        for (int j = 1; j <= this.mCores; j++) {
            int time = buffer.get();
            if (time < 0) {
                String str = TAG;
                Slog.e(str, "Negative time from active time proc: " + time);
                corrupted = true;
            } else {
                sum += (time * 10.0d) / j;
            }
        }
        if (corrupted) {
            return -1.0d;
        }
        return sum;
    }

    private synchronized void readImpl(Consumer<IntBuffer> processUid) {
        synchronized (this.mProcReader) {
            ByteBuffer bytes = this.mProcReader.readBytes();
            if (bytes != null && bytes.remaining() > 4) {
                if ((bytes.remaining() & 3) != 0) {
                    String str = TAG;
                    Slog.wtf(str, "Cannot parse active time proc bytes to int: " + bytes.remaining());
                    return;
                }
                IntBuffer buf = bytes.asIntBuffer();
                int cores = buf.get();
                if (this.mCores != 0 && cores != this.mCores) {
                    String str2 = TAG;
                    Slog.wtf(str2, "Cpu active time wrong # cores: " + cores);
                    return;
                }
                this.mCores = cores;
                if (cores > 0 && buf.remaining() % (cores + 1) == 0) {
                    int numUids = buf.remaining() / (cores + 1);
                    for (int i = 0; i < numUids; i++) {
                        processUid.accept(buf);
                    }
                    return;
                }
                String str3 = TAG;
                Slog.wtf(str3, "Cpu active time format error: " + buf.remaining() + " / " + (cores + 1));
            }
        }
    }

    public synchronized void removeUid(int uid) {
        this.mLastUidCpuActiveTimeMs.delete(uid);
    }

    public synchronized void removeUidsInRange(int startUid, int endUid) {
        this.mLastUidCpuActiveTimeMs.put(startUid, null);
        this.mLastUidCpuActiveTimeMs.put(endUid, null);
        int firstIndex = this.mLastUidCpuActiveTimeMs.indexOfKey(startUid);
        int lastIndex = this.mLastUidCpuActiveTimeMs.indexOfKey(endUid);
        this.mLastUidCpuActiveTimeMs.removeAtRange(firstIndex, (lastIndex - firstIndex) + 1);
    }
}
