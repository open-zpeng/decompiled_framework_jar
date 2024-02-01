package android.view;

import android.util.Pools;
/* loaded from: classes2.dex */
public final class VelocityTracker {
    private static final int ACTIVE_POINTER_ID = -1;
    private static final Pools.SynchronizedPool<VelocityTracker> sPool = new Pools.SynchronizedPool<>(2);
    private long mPtr;
    private final String mStrategy;

    private static native void nativeAddMovement(long j, MotionEvent motionEvent);

    private static native void nativeClear(long j);

    private static native void nativeComputeCurrentVelocity(long j, int i, float f);

    private static native void nativeDispose(long j);

    private static native boolean nativeGetEstimator(long j, int i, Estimator estimator);

    private static native float nativeGetXVelocity(long j, int i);

    private static native float nativeGetYVelocity(long j, int i);

    private static native long nativeInitialize(String str);

    public static VelocityTracker obtain() {
        VelocityTracker instance = sPool.acquire();
        return instance != null ? instance : new VelocityTracker(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static VelocityTracker obtain(String strategy) {
        if (strategy == null) {
            return obtain();
        }
        return new VelocityTracker(strategy);
    }

    public void recycle() {
        if (this.mStrategy == null) {
            clear();
            sPool.release(this);
        }
    }

    private synchronized VelocityTracker(String strategy) {
        this.mPtr = nativeInitialize(strategy);
        this.mStrategy = strategy;
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mPtr != 0) {
                nativeDispose(this.mPtr);
                this.mPtr = 0L;
            }
        } finally {
            super.finalize();
        }
    }

    public void clear() {
        nativeClear(this.mPtr);
    }

    public void addMovement(MotionEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("event must not be null");
        }
        nativeAddMovement(this.mPtr, event);
    }

    public void computeCurrentVelocity(int units) {
        nativeComputeCurrentVelocity(this.mPtr, units, Float.MAX_VALUE);
    }

    public void computeCurrentVelocity(int units, float maxVelocity) {
        nativeComputeCurrentVelocity(this.mPtr, units, maxVelocity);
    }

    public float getXVelocity() {
        return nativeGetXVelocity(this.mPtr, -1);
    }

    public float getYVelocity() {
        return nativeGetYVelocity(this.mPtr, -1);
    }

    public float getXVelocity(int id) {
        return nativeGetXVelocity(this.mPtr, id);
    }

    public float getYVelocity(int id) {
        return nativeGetYVelocity(this.mPtr, id);
    }

    public synchronized boolean getEstimator(int id, Estimator outEstimator) {
        if (outEstimator == null) {
            throw new IllegalArgumentException("outEstimator must not be null");
        }
        return nativeGetEstimator(this.mPtr, id, outEstimator);
    }

    /* loaded from: classes2.dex */
    public static final class Estimator {
        private static final int MAX_DEGREE = 4;
        private protected float confidence;
        private protected int degree;
        private protected final float[] xCoeff = new float[5];
        private protected final float[] yCoeff = new float[5];

        public synchronized float estimateX(float time) {
            return estimate(time, this.xCoeff);
        }

        public synchronized float estimateY(float time) {
            return estimate(time, this.yCoeff);
        }

        public synchronized float getXCoeff(int index) {
            if (index <= this.degree) {
                return this.xCoeff[index];
            }
            return 0.0f;
        }

        public synchronized float getYCoeff(int index) {
            if (index <= this.degree) {
                return this.yCoeff[index];
            }
            return 0.0f;
        }

        private synchronized float estimate(float time, float[] c) {
            float a = 0.0f;
            float scale = 1.0f;
            for (int i = 0; i <= this.degree; i++) {
                a += c[i] * scale;
                scale *= time;
            }
            return a;
        }
    }
}
