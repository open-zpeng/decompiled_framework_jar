package android.view;

import android.annotation.UnsupportedAppUsage;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import dalvik.annotation.optimization.FastNative;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public abstract class DisplayEventReceiver {
    public static final int CONFIG_CHANGED_EVENT_DISPATCH = 1;
    public static final int CONFIG_CHANGED_EVENT_SUPPRESS = 0;
    private static final String TAG = "DisplayEventReceiver";
    public static final int VSYNC_SOURCE_APP = 0;
    public static final int VSYNC_SOURCE_SURFACE_FLINGER = 1;
    private final CloseGuard mCloseGuard;
    private MessageQueue mMessageQueue;
    @UnsupportedAppUsage
    private long mReceiverPtr;

    private static native void nativeDispose(long j);

    private static native long nativeInit(WeakReference<DisplayEventReceiver> weakReference, MessageQueue messageQueue, int i, int i2);

    @FastNative
    private static native void nativeScheduleVsync(long j);

    @UnsupportedAppUsage
    public DisplayEventReceiver(Looper looper) {
        this(looper, 0, 0);
    }

    public DisplayEventReceiver(Looper looper, int vsyncSource, int configChanged) {
        this.mCloseGuard = CloseGuard.get();
        if (looper == null) {
            throw new IllegalArgumentException("looper must not be null");
        }
        this.mMessageQueue = looper.getQueue();
        this.mReceiverPtr = nativeInit(new WeakReference(this), this.mMessageQueue, vsyncSource, configChanged);
        this.mCloseGuard.open("dispose");
    }

    protected void finalize() throws Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    public void dispose() {
        dispose(false);
    }

    private void dispose(boolean finalized) {
        CloseGuard closeGuard = this.mCloseGuard;
        if (closeGuard != null) {
            if (finalized) {
                closeGuard.warnIfOpen();
            }
            this.mCloseGuard.close();
        }
        long j = this.mReceiverPtr;
        if (j != 0) {
            nativeDispose(j);
            this.mReceiverPtr = 0L;
        }
        this.mMessageQueue = null;
    }

    @UnsupportedAppUsage
    public void onVsync(long timestampNanos, long physicalDisplayId, int frame) {
    }

    @UnsupportedAppUsage
    public void onHotplug(long timestampNanos, long physicalDisplayId, boolean connected) {
    }

    public void onConfigChanged(long timestampNanos, long physicalDisplayId, int configId) {
    }

    @UnsupportedAppUsage
    public void scheduleVsync() {
        long j = this.mReceiverPtr;
        if (j == 0) {
            Log.w(TAG, "Attempted to schedule a vertical sync pulse but the display event receiver has already been disposed.");
        } else {
            nativeScheduleVsync(j);
        }
    }

    @UnsupportedAppUsage
    private void dispatchVsync(long timestampNanos, long physicalDisplayId, int frame) {
        onVsync(timestampNanos, physicalDisplayId, frame);
    }

    @UnsupportedAppUsage
    private void dispatchHotplug(long timestampNanos, long physicalDisplayId, boolean connected) {
        onHotplug(timestampNanos, physicalDisplayId, connected);
    }

    private void dispatchConfigChanged(long timestampNanos, long physicalDisplayId, int configId) {
        onConfigChanged(timestampNanos, physicalDisplayId, configId);
    }
}
