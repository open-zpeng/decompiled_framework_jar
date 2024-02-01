package android.accessibilityservice;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.util.Preconditions;
/* loaded from: classes.dex */
public final class AccessibilityButtonController {
    private static final String LOG_TAG = "A11yButtonController";
    private ArrayMap<AccessibilityButtonCallback, Handler> mCallbacks;
    private final Object mLock = new Object();
    private final IAccessibilityServiceConnection mServiceConnection;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized AccessibilityButtonController(IAccessibilityServiceConnection serviceConnection) {
        this.mServiceConnection = serviceConnection;
    }

    public boolean isAccessibilityButtonAvailable() {
        try {
            return this.mServiceConnection.isAccessibilityButtonAvailable();
        } catch (RemoteException re) {
            Slog.w(LOG_TAG, "Failed to get accessibility button availability.", re);
            re.rethrowFromSystemServer();
            return false;
        }
    }

    public void registerAccessibilityButtonCallback(AccessibilityButtonCallback callback) {
        registerAccessibilityButtonCallback(callback, new Handler(Looper.getMainLooper()));
    }

    public void registerAccessibilityButtonCallback(AccessibilityButtonCallback callback, Handler handler) {
        Preconditions.checkNotNull(callback);
        Preconditions.checkNotNull(handler);
        synchronized (this.mLock) {
            if (this.mCallbacks == null) {
                this.mCallbacks = new ArrayMap<>();
            }
            this.mCallbacks.put(callback, handler);
        }
    }

    public void unregisterAccessibilityButtonCallback(AccessibilityButtonCallback callback) {
        Preconditions.checkNotNull(callback);
        synchronized (this.mLock) {
            if (this.mCallbacks == null) {
                return;
            }
            int keyIndex = this.mCallbacks.indexOfKey(callback);
            boolean hasKey = keyIndex >= 0;
            if (hasKey) {
                this.mCallbacks.removeAt(keyIndex);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dispatchAccessibilityButtonClicked() {
        synchronized (this.mLock) {
            if (this.mCallbacks != null && !this.mCallbacks.isEmpty()) {
                ArrayMap<AccessibilityButtonCallback, Handler> entries = new ArrayMap<>(this.mCallbacks);
                int count = entries.size();
                for (int i = 0; i < count; i++) {
                    final AccessibilityButtonCallback callback = entries.keyAt(i);
                    Handler handler = entries.valueAt(i);
                    handler.post(new Runnable() { // from class: android.accessibilityservice.-$$Lambda$AccessibilityButtonController$b_UAM9QJWcH4KQOC_odiN0t_boU
                        @Override // java.lang.Runnable
                        public final void run() {
                            callback.onClicked(AccessibilityButtonController.this);
                        }
                    });
                }
                return;
            }
            Slog.w(LOG_TAG, "Received accessibility button click with no callbacks!");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dispatchAccessibilityButtonAvailabilityChanged(final boolean available) {
        synchronized (this.mLock) {
            if (this.mCallbacks != null && !this.mCallbacks.isEmpty()) {
                ArrayMap<AccessibilityButtonCallback, Handler> entries = new ArrayMap<>(this.mCallbacks);
                int count = entries.size();
                for (int i = 0; i < count; i++) {
                    final AccessibilityButtonCallback callback = entries.keyAt(i);
                    Handler handler = entries.valueAt(i);
                    handler.post(new Runnable() { // from class: android.accessibilityservice.-$$Lambda$AccessibilityButtonController$RskKrfcSyUz7I9Sqaziy1P990ZM
                        @Override // java.lang.Runnable
                        public final void run() {
                            callback.onAvailabilityChanged(AccessibilityButtonController.this, available);
                        }
                    });
                }
                return;
            }
            Slog.w(LOG_TAG, "Received accessibility button availability change with no callbacks!");
        }
    }

    /* loaded from: classes.dex */
    public static abstract class AccessibilityButtonCallback {
        public void onClicked(AccessibilityButtonController controller) {
        }

        public void onAvailabilityChanged(AccessibilityButtonController controller, boolean available) {
        }
    }
}
