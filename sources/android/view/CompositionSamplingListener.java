package android.view;

import android.graphics.Rect;
import android.os.IBinder;
import com.android.internal.util.Preconditions;
import java.util.concurrent.Executor;

/* loaded from: classes3.dex */
public abstract class CompositionSamplingListener {
    private final Executor mExecutor;
    private long mNativeListener = nativeCreate(this);

    private static native long nativeCreate(CompositionSamplingListener compositionSamplingListener);

    private static native void nativeDestroy(long j);

    private static native void nativeRegister(long j, IBinder iBinder, int i, int i2, int i3, int i4);

    private static native void nativeUnregister(long j);

    public abstract void onSampleCollected(float f);

    public CompositionSamplingListener(Executor executor) {
        this.mExecutor = executor;
    }

    public void destroy() {
        if (this.mNativeListener == 0) {
            return;
        }
        unregister(this);
        nativeDestroy(this.mNativeListener);
        this.mNativeListener = 0L;
    }

    protected void finalize() throws Throwable {
        try {
            destroy();
        } finally {
            super.finalize();
        }
    }

    public static void register(CompositionSamplingListener listener, int displayId, IBinder stopLayer, Rect samplingArea) {
        if (listener.mNativeListener == 0) {
            return;
        }
        Preconditions.checkArgument(displayId == 0, "default display only for now");
        nativeRegister(listener.mNativeListener, stopLayer, samplingArea.left, samplingArea.top, samplingArea.right, samplingArea.bottom);
    }

    public static void unregister(CompositionSamplingListener listener) {
        long j = listener.mNativeListener;
        if (j == 0) {
            return;
        }
        nativeUnregister(j);
    }

    private static void dispatchOnSampleCollected(final CompositionSamplingListener listener, final float medianLuma) {
        listener.mExecutor.execute(new Runnable() { // from class: android.view.-$$Lambda$CompositionSamplingListener$hrbPutjnKRv7VkkiY9eg32N6QA8
            @Override // java.lang.Runnable
            public final void run() {
                CompositionSamplingListener.this.onSampleCollected(medianLuma);
            }
        });
    }
}
