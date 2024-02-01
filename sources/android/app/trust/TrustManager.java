package android.app.trust;

import android.app.trust.ITrustListener;
import android.app.trust.ITrustManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.ArrayMap;
/* loaded from: classes.dex */
public class TrustManager {
    private static final String DATA_FLAGS = "initiatedByUser";
    private static final String DATA_MESSAGE = "message";
    private static final int MSG_TRUST_CHANGED = 1;
    private static final int MSG_TRUST_ERROR = 3;
    private static final int MSG_TRUST_MANAGED_CHANGED = 2;
    private static final String TAG = "TrustManager";
    private final ITrustManager mService;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: android.app.trust.TrustManager.2
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int flags = msg.peekData() != null ? msg.peekData().getInt(TrustManager.DATA_FLAGS) : 0;
                    ((TrustListener) msg.obj).onTrustChanged(msg.arg1 != 0, msg.arg2, flags);
                    return;
                case 2:
                    ((TrustListener) msg.obj).onTrustManagedChanged(msg.arg1 != 0, msg.arg2);
                    return;
                case 3:
                    CharSequence message = msg.peekData().getCharSequence("message");
                    ((TrustListener) msg.obj).onTrustError(message);
                    return;
                default:
                    return;
            }
        }
    };
    private final ArrayMap<TrustListener, ITrustListener> mTrustListeners = new ArrayMap<>();

    /* loaded from: classes.dex */
    public interface TrustListener {
        synchronized void onTrustChanged(boolean z, int i, int i2);

        synchronized void onTrustError(CharSequence charSequence);

        synchronized void onTrustManagedChanged(boolean z, int i);
    }

    public synchronized TrustManager(IBinder b) {
        this.mService = ITrustManager.Stub.asInterface(b);
    }

    public synchronized void setDeviceLockedForUser(int userId, boolean locked) {
        try {
            this.mService.setDeviceLockedForUser(userId, locked);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportUnlockAttempt(boolean successful, int userId) {
        try {
            this.mService.reportUnlockAttempt(successful, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void reportUnlockLockout(int timeoutMs, int userId) {
        try {
            this.mService.reportUnlockLockout(timeoutMs, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void reportEnabledTrustAgentsChanged(int userId) {
        try {
            this.mService.reportEnabledTrustAgentsChanged(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void reportKeyguardShowingChanged() {
        try {
            this.mService.reportKeyguardShowingChanged();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void registerTrustListener(final TrustListener trustListener) {
        try {
            ITrustListener.Stub iTrustListener = new ITrustListener.Stub() { // from class: android.app.trust.TrustManager.1
                @Override // android.app.trust.ITrustListener
                public void onTrustChanged(boolean enabled, int userId, int flags) {
                    Message m = TrustManager.this.mHandler.obtainMessage(1, enabled ? 1 : 0, userId, trustListener);
                    if (flags != 0) {
                        m.getData().putInt(TrustManager.DATA_FLAGS, flags);
                    }
                    m.sendToTarget();
                }

                @Override // android.app.trust.ITrustListener
                public void onTrustManagedChanged(boolean managed, int userId) {
                    TrustManager.this.mHandler.obtainMessage(2, managed ? 1 : 0, userId, trustListener).sendToTarget();
                }

                @Override // android.app.trust.ITrustListener
                public void onTrustError(CharSequence message) {
                    Message m = TrustManager.this.mHandler.obtainMessage(3);
                    m.getData().putCharSequence("message", message);
                    m.sendToTarget();
                }
            };
            this.mService.registerTrustListener(iTrustListener);
            this.mTrustListeners.put(trustListener, iTrustListener);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void unregisterTrustListener(TrustListener trustListener) {
        ITrustListener iTrustListener = this.mTrustListeners.remove(trustListener);
        if (iTrustListener != null) {
            try {
                this.mService.unregisterTrustListener(iTrustListener);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public synchronized boolean isTrustUsuallyManaged(int userId) {
        try {
            return this.mService.isTrustUsuallyManaged(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void unlockedByFingerprintForUser(int userId) {
        try {
            this.mService.unlockedByFingerprintForUser(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void clearAllFingerprints() {
        try {
            this.mService.clearAllFingerprints();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
