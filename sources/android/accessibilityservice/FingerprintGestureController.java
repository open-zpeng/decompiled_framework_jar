package android.accessibilityservice;

import android.accessibilityservice.FingerprintGestureController;
import android.os.Handler;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
/* loaded from: classes.dex */
public final class FingerprintGestureController {
    public static final int FINGERPRINT_GESTURE_SWIPE_DOWN = 8;
    public static final int FINGERPRINT_GESTURE_SWIPE_LEFT = 2;
    public static final int FINGERPRINT_GESTURE_SWIPE_RIGHT = 1;
    public static final int FINGERPRINT_GESTURE_SWIPE_UP = 4;
    private static final String LOG_TAG = "FingerprintGestureController";
    private final IAccessibilityServiceConnection mAccessibilityServiceConnection;
    private final Object mLock = new Object();
    private final ArrayMap<FingerprintGestureCallback, Handler> mCallbackHandlerMap = new ArrayMap<>(1);

    @VisibleForTesting
    public synchronized FingerprintGestureController(IAccessibilityServiceConnection connection) {
        this.mAccessibilityServiceConnection = connection;
    }

    public boolean isGestureDetectionAvailable() {
        try {
            return this.mAccessibilityServiceConnection.isFingerprintGestureDetectionAvailable();
        } catch (RemoteException re) {
            Log.w(LOG_TAG, "Failed to check if fingerprint gestures are active", re);
            re.rethrowFromSystemServer();
            return false;
        }
    }

    public void registerFingerprintGestureCallback(FingerprintGestureCallback callback, Handler handler) {
        synchronized (this.mLock) {
            this.mCallbackHandlerMap.put(callback, handler);
        }
    }

    public void unregisterFingerprintGestureCallback(FingerprintGestureCallback callback) {
        synchronized (this.mLock) {
            this.mCallbackHandlerMap.remove(callback);
        }
    }

    public synchronized void onGestureDetectionActiveChanged(final boolean active) {
        ArrayMap<FingerprintGestureCallback, Handler> handlerMap;
        synchronized (this.mLock) {
            handlerMap = new ArrayMap<>(this.mCallbackHandlerMap);
        }
        int numListeners = handlerMap.size();
        for (int i = 0; i < numListeners; i++) {
            final FingerprintGestureCallback callback = handlerMap.keyAt(i);
            Handler handler = handlerMap.valueAt(i);
            if (handler != null) {
                handler.post(new Runnable() { // from class: android.accessibilityservice.-$$Lambda$FingerprintGestureController$M-ZApqp96G6ZF2WdWrGDJ8Qsfck
                    @Override // java.lang.Runnable
                    public final void run() {
                        FingerprintGestureController.FingerprintGestureCallback.this.onGestureDetectionAvailabilityChanged(active);
                    }
                });
            } else {
                callback.onGestureDetectionAvailabilityChanged(active);
            }
        }
    }

    public synchronized void onGesture(final int gesture) {
        ArrayMap<FingerprintGestureCallback, Handler> handlerMap;
        synchronized (this.mLock) {
            handlerMap = new ArrayMap<>(this.mCallbackHandlerMap);
        }
        int numListeners = handlerMap.size();
        for (int i = 0; i < numListeners; i++) {
            final FingerprintGestureCallback callback = handlerMap.keyAt(i);
            Handler handler = handlerMap.valueAt(i);
            if (handler != null) {
                handler.post(new Runnable() { // from class: android.accessibilityservice.-$$Lambda$FingerprintGestureController$BQjrQQom4K3C98FNiI0fi7SvHfY
                    @Override // java.lang.Runnable
                    public final void run() {
                        FingerprintGestureController.FingerprintGestureCallback.this.onGestureDetected(gesture);
                    }
                });
            } else {
                callback.onGestureDetected(gesture);
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class FingerprintGestureCallback {
        public void onGestureDetectionAvailabilityChanged(boolean available) {
        }

        public void onGestureDetected(int gesture) {
        }
    }
}
