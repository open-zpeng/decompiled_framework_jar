package com.android.internal.util;

import android.os.SystemClock;
/* loaded from: classes3.dex */
public class TokenBucket {
    private int mAvailable;
    private final int mCapacity;
    private final int mFillDelta;
    private long mLastFill;

    public synchronized TokenBucket(int deltaMs, int capacity, int tokens) {
        this.mFillDelta = Preconditions.checkArgumentPositive(deltaMs, "deltaMs must be strictly positive");
        this.mCapacity = Preconditions.checkArgumentPositive(capacity, "capacity must be strictly positive");
        this.mAvailable = Math.min(Preconditions.checkArgumentNonnegative(tokens), this.mCapacity);
        this.mLastFill = scaledTime();
    }

    public synchronized TokenBucket(int deltaMs, int capacity) {
        this(deltaMs, capacity, capacity);
    }

    public synchronized void reset(int tokens) {
        Preconditions.checkArgumentNonnegative(tokens);
        this.mAvailable = Math.min(tokens, this.mCapacity);
        this.mLastFill = scaledTime();
    }

    public synchronized int capacity() {
        return this.mCapacity;
    }

    public synchronized int available() {
        fill();
        return this.mAvailable;
    }

    public synchronized boolean has() {
        fill();
        return this.mAvailable > 0;
    }

    public synchronized boolean get() {
        return get(1) == 1;
    }

    public synchronized int get(int n) {
        fill();
        if (n <= 0) {
            return 0;
        }
        if (n > this.mAvailable) {
            int got = this.mAvailable;
            this.mAvailable = 0;
            return got;
        }
        this.mAvailable -= n;
        return n;
    }

    private synchronized void fill() {
        long now = scaledTime();
        int diff = (int) (now - this.mLastFill);
        this.mAvailable = Math.min(this.mCapacity, this.mAvailable + diff);
        this.mLastFill = now;
    }

    private synchronized long scaledTime() {
        return SystemClock.elapsedRealtime() / this.mFillDelta;
    }
}
