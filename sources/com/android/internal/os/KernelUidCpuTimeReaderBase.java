package com.android.internal.os;

import android.os.SystemClock;
import com.android.internal.os.KernelUidCpuTimeReaderBase.Callback;
/* loaded from: classes3.dex */
public abstract class KernelUidCpuTimeReaderBase<T extends Callback> {
    protected static final boolean DEBUG = false;
    private static final long DEFAULT_THROTTLE_INTERVAL = 10000;
    private final String TAG = getClass().getSimpleName();
    private long mLastTimeReadMs = Long.MIN_VALUE;
    private long mThrottleInterval = 10000;

    /* loaded from: classes3.dex */
    public interface Callback {
    }

    protected abstract synchronized void readDeltaImpl(T t);

    public synchronized void readDelta(T cb) {
        if (SystemClock.elapsedRealtime() < this.mLastTimeReadMs + this.mThrottleInterval) {
            return;
        }
        readDeltaImpl(cb);
        this.mLastTimeReadMs = SystemClock.elapsedRealtime();
    }

    public synchronized void setThrottleInterval(long throttleInterval) {
        if (throttleInterval >= 0) {
            this.mThrottleInterval = throttleInterval;
        }
    }
}
