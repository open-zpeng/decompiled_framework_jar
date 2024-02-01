package android.service.trust;

import android.Manifest;
import android.annotation.SystemApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.service.trust.ITrustAgentService;
import android.util.Log;
import android.util.Slog;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
@SystemApi
/* loaded from: classes2.dex */
public class TrustAgentService extends Service {
    private static final boolean DEBUG = false;
    private static final String EXTRA_TOKEN = "token";
    private static final String EXTRA_TOKEN_HANDLE = "token_handle";
    private static final String EXTRA_TOKEN_REMOVED_RESULT = "token_removed_result";
    private static final String EXTRA_TOKEN_STATE = "token_state";
    private static final String EXTRA_USER_HANDLE = "user_handle";
    public static final int FLAG_GRANT_TRUST_DISMISS_KEYGUARD = 2;
    public static final int FLAG_GRANT_TRUST_INITIATED_BY_USER = 1;
    private static final int MSG_CONFIGURE = 2;
    private static final int MSG_DEVICE_LOCKED = 4;
    private static final int MSG_DEVICE_UNLOCKED = 5;
    private static final int MSG_ESCROW_TOKEN_ADDED = 7;
    private static final int MSG_ESCROW_TOKEN_REMOVED = 9;
    private static final int MSG_ESCROW_TOKEN_STATE_RECEIVED = 8;
    private static final int MSG_TRUST_TIMEOUT = 3;
    private static final int MSG_UNLOCK_ATTEMPT = 1;
    private static final int MSG_UNLOCK_LOCKOUT = 6;
    public static final String SERVICE_INTERFACE = "android.service.trust.TrustAgentService";
    public static final int TOKEN_STATE_ACTIVE = 1;
    public static final int TOKEN_STATE_INACTIVE = 0;
    public static final String TRUST_AGENT_META_DATA = "android.service.trust.trustagent";
    private ITrustAgentServiceCallback mCallback;
    private boolean mManagingTrust;
    private Runnable mPendingGrantTrustTask;
    private final String TAG = TrustAgentService.class.getSimpleName() + "[" + getClass().getSimpleName() + "]";
    private final Object mLock = new Object();
    private Handler mHandler = new Handler() { // from class: android.service.trust.TrustAgentService.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    TrustAgentService.this.onUnlockAttempt(msg.arg1 != 0);
                    return;
                case 2:
                    ConfigurationData data = (ConfigurationData) msg.obj;
                    boolean result = TrustAgentService.this.onConfigure(data.options);
                    if (data.token != null) {
                        try {
                            synchronized (TrustAgentService.this.mLock) {
                                TrustAgentService.this.mCallback.onConfigureCompleted(result, data.token);
                            }
                            return;
                        } catch (RemoteException e) {
                            TrustAgentService.this.onError("calling onSetTrustAgentFeaturesEnabledCompleted()");
                            return;
                        }
                    }
                    return;
                case 3:
                    TrustAgentService.this.onTrustTimeout();
                    return;
                case 4:
                    TrustAgentService.this.onDeviceLocked();
                    return;
                case 5:
                    TrustAgentService.this.onDeviceUnlocked();
                    return;
                case 6:
                    TrustAgentService.this.onDeviceUnlockLockout(msg.arg1);
                    return;
                case 7:
                    Bundle data2 = msg.getData();
                    byte[] token = data2.getByteArray("token");
                    long handle = data2.getLong(TrustAgentService.EXTRA_TOKEN_HANDLE);
                    UserHandle user = (UserHandle) data2.getParcelable(TrustAgentService.EXTRA_USER_HANDLE);
                    TrustAgentService.this.onEscrowTokenAdded(token, handle, user);
                    return;
                case 8:
                    Bundle data3 = msg.getData();
                    long handle2 = data3.getLong(TrustAgentService.EXTRA_TOKEN_HANDLE);
                    int tokenState = data3.getInt(TrustAgentService.EXTRA_TOKEN_STATE, 0);
                    TrustAgentService.this.onEscrowTokenStateReceived(handle2, tokenState);
                    return;
                case 9:
                    Bundle data4 = msg.getData();
                    long handle3 = data4.getLong(TrustAgentService.EXTRA_TOKEN_HANDLE);
                    boolean success = data4.getBoolean(TrustAgentService.EXTRA_TOKEN_REMOVED_RESULT);
                    TrustAgentService.this.onEscrowTokenRemoved(handle3, success);
                    return;
                default:
                    return;
            }
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface GrantTrustFlags {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface TokenState {
    }

    /* loaded from: classes2.dex */
    private static final class ConfigurationData {
        final List<PersistableBundle> options;
        final IBinder token;

        synchronized ConfigurationData(List<PersistableBundle> opts, IBinder t) {
            this.options = opts;
            this.token = t;
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        ComponentName component = new ComponentName(this, getClass());
        try {
            ServiceInfo serviceInfo = getPackageManager().getServiceInfo(component, 0);
            if (!Manifest.permission.BIND_TRUST_AGENT.equals(serviceInfo.permission)) {
                throw new IllegalStateException(component.flattenToShortString() + " is not declared with the permission \"" + Manifest.permission.BIND_TRUST_AGENT + "\"");
            }
        } catch (PackageManager.NameNotFoundException e) {
            String str = this.TAG;
            Log.e(str, "Can't get ServiceInfo for " + component.toShortString());
        }
    }

    public void onUnlockAttempt(boolean successful) {
    }

    public void onTrustTimeout() {
    }

    public void onDeviceLocked() {
    }

    public void onDeviceUnlocked() {
    }

    public void onDeviceUnlockLockout(long timeoutMs) {
    }

    public void onEscrowTokenAdded(byte[] token, long handle, UserHandle user) {
    }

    public void onEscrowTokenStateReceived(long handle, int tokenState) {
    }

    public void onEscrowTokenRemoved(long handle, boolean successful) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onError(String msg) {
        String str = this.TAG;
        Slog.v(str, "Remote exception while " + msg);
    }

    public boolean onConfigure(List<PersistableBundle> options) {
        return false;
    }

    @Deprecated
    public final void grantTrust(CharSequence message, long durationMs, boolean initiatedByUser) {
        grantTrust(message, durationMs, initiatedByUser ? 1 : 0);
    }

    public final void grantTrust(final CharSequence message, final long durationMs, final int flags) {
        synchronized (this.mLock) {
            if (!this.mManagingTrust) {
                throw new IllegalStateException("Cannot grant trust if agent is not managing trust. Call setManagingTrust(true) first.");
            }
            if (this.mCallback != null) {
                try {
                    this.mCallback.grantTrust(message.toString(), durationMs, flags);
                } catch (RemoteException e) {
                    onError("calling enableTrust()");
                }
            } else {
                this.mPendingGrantTrustTask = new Runnable() { // from class: android.service.trust.TrustAgentService.2
                    @Override // java.lang.Runnable
                    public void run() {
                        TrustAgentService.this.grantTrust(message, durationMs, flags);
                    }
                };
            }
        }
    }

    public final void revokeTrust() {
        synchronized (this.mLock) {
            if (this.mPendingGrantTrustTask != null) {
                this.mPendingGrantTrustTask = null;
            }
            if (this.mCallback != null) {
                try {
                    this.mCallback.revokeTrust();
                } catch (RemoteException e) {
                    onError("calling revokeTrust()");
                }
            }
        }
    }

    public final void setManagingTrust(boolean managingTrust) {
        synchronized (this.mLock) {
            if (this.mManagingTrust != managingTrust) {
                this.mManagingTrust = managingTrust;
                if (this.mCallback != null) {
                    try {
                        this.mCallback.setManagingTrust(managingTrust);
                    } catch (RemoteException e) {
                        onError("calling setManagingTrust()");
                    }
                }
            }
        }
    }

    public final void addEscrowToken(byte[] token, UserHandle user) {
        synchronized (this.mLock) {
            if (this.mCallback == null) {
                Slog.w(this.TAG, "Cannot add escrow token if the agent is not connecting to framework");
                throw new IllegalStateException("Trust agent is not connected");
            } else {
                try {
                    this.mCallback.addEscrowToken(token, user.getIdentifier());
                } catch (RemoteException e) {
                    onError("calling addEscrowToken");
                }
            }
        }
    }

    public final void isEscrowTokenActive(long handle, UserHandle user) {
        synchronized (this.mLock) {
            if (this.mCallback == null) {
                Slog.w(this.TAG, "Cannot add escrow token if the agent is not connecting to framework");
                throw new IllegalStateException("Trust agent is not connected");
            } else {
                try {
                    this.mCallback.isEscrowTokenActive(handle, user.getIdentifier());
                } catch (RemoteException e) {
                    onError("calling isEscrowTokenActive");
                }
            }
        }
    }

    public final void removeEscrowToken(long handle, UserHandle user) {
        synchronized (this.mLock) {
            if (this.mCallback == null) {
                Slog.w(this.TAG, "Cannot add escrow token if the agent is not connecting to framework");
                throw new IllegalStateException("Trust agent is not connected");
            } else {
                try {
                    this.mCallback.removeEscrowToken(handle, user.getIdentifier());
                } catch (RemoteException e) {
                    onError("callling removeEscrowToken");
                }
            }
        }
    }

    public final void unlockUserWithToken(long handle, byte[] token, UserHandle user) {
        UserManager um = (UserManager) getSystemService("user");
        if (um.isUserUnlocked(user)) {
            Slog.i(this.TAG, "User already unlocked");
            return;
        }
        synchronized (this.mLock) {
            if (this.mCallback == null) {
                Slog.w(this.TAG, "Cannot add escrow token if the agent is not connecting to framework");
                throw new IllegalStateException("Trust agent is not connected");
            } else {
                try {
                    this.mCallback.unlockUserWithToken(handle, token, user.getIdentifier());
                } catch (RemoteException e) {
                    onError("calling unlockUserWithToken");
                }
            }
        }
    }

    public final void showKeyguardErrorMessage(CharSequence message) {
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null");
        }
        synchronized (this.mLock) {
            if (this.mCallback == null) {
                Slog.w(this.TAG, "Cannot show message because service is not connected to framework.");
                throw new IllegalStateException("Trust agent is not connected");
            } else {
                try {
                    this.mCallback.showKeyguardErrorMessage(message);
                } catch (RemoteException e) {
                    onError("calling showKeyguardErrorMessage");
                }
            }
        }
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return new TrustAgentServiceWrapper();
    }

    /* loaded from: classes2.dex */
    private final class TrustAgentServiceWrapper extends ITrustAgentService.Stub {
        private TrustAgentServiceWrapper() {
        }

        @Override // android.service.trust.ITrustAgentService
        public synchronized void onUnlockAttempt(boolean successful) {
            TrustAgentService.this.mHandler.obtainMessage(1, successful ? 1 : 0, 0).sendToTarget();
        }

        @Override // android.service.trust.ITrustAgentService
        public synchronized void onUnlockLockout(int timeoutMs) {
            TrustAgentService.this.mHandler.obtainMessage(6, timeoutMs, 0).sendToTarget();
        }

        @Override // android.service.trust.ITrustAgentService
        public synchronized void onTrustTimeout() {
            TrustAgentService.this.mHandler.sendEmptyMessage(3);
        }

        @Override // android.service.trust.ITrustAgentService
        public synchronized void onConfigure(List<PersistableBundle> args, IBinder token) {
            TrustAgentService.this.mHandler.obtainMessage(2, new ConfigurationData(args, token)).sendToTarget();
        }

        @Override // android.service.trust.ITrustAgentService
        public synchronized void onDeviceLocked() throws RemoteException {
            TrustAgentService.this.mHandler.obtainMessage(4).sendToTarget();
        }

        @Override // android.service.trust.ITrustAgentService
        public synchronized void onDeviceUnlocked() throws RemoteException {
            TrustAgentService.this.mHandler.obtainMessage(5).sendToTarget();
        }

        @Override // android.service.trust.ITrustAgentService
        public synchronized void setCallback(ITrustAgentServiceCallback callback) {
            synchronized (TrustAgentService.this.mLock) {
                TrustAgentService.this.mCallback = callback;
                if (TrustAgentService.this.mManagingTrust) {
                    try {
                        TrustAgentService.this.mCallback.setManagingTrust(TrustAgentService.this.mManagingTrust);
                    } catch (RemoteException e) {
                        TrustAgentService.this.onError("calling setManagingTrust()");
                    }
                }
                if (TrustAgentService.this.mPendingGrantTrustTask != null) {
                    TrustAgentService.this.mPendingGrantTrustTask.run();
                    TrustAgentService.this.mPendingGrantTrustTask = null;
                }
            }
        }

        @Override // android.service.trust.ITrustAgentService
        public synchronized void onEscrowTokenAdded(byte[] token, long handle, UserHandle user) {
            Message msg = TrustAgentService.this.mHandler.obtainMessage(7);
            msg.getData().putByteArray("token", token);
            msg.getData().putLong(TrustAgentService.EXTRA_TOKEN_HANDLE, handle);
            msg.getData().putParcelable(TrustAgentService.EXTRA_USER_HANDLE, user);
            msg.sendToTarget();
        }

        @Override // android.service.trust.ITrustAgentService
        public synchronized void onTokenStateReceived(long handle, int tokenState) {
            Message msg = TrustAgentService.this.mHandler.obtainMessage(8);
            msg.getData().putLong(TrustAgentService.EXTRA_TOKEN_HANDLE, handle);
            msg.getData().putInt(TrustAgentService.EXTRA_TOKEN_STATE, tokenState);
            msg.sendToTarget();
        }

        @Override // android.service.trust.ITrustAgentService
        public synchronized void onEscrowTokenRemoved(long handle, boolean successful) {
            Message msg = TrustAgentService.this.mHandler.obtainMessage(9);
            msg.getData().putLong(TrustAgentService.EXTRA_TOKEN_HANDLE, handle);
            msg.getData().putBoolean(TrustAgentService.EXTRA_TOKEN_REMOVED_RESULT, successful);
            msg.sendToTarget();
        }
    }
}
