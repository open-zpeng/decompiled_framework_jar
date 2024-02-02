package android.net.lowpan;

import android.net.IpPrefix;
import android.net.lowpan.ILowpanInterfaceListener;
import android.net.lowpan.LowpanCommissioningSession;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public class LowpanCommissioningSession {
    public protected final LowpanBeaconInfo mBeaconInfo;
    public protected final ILowpanInterface mBinder;
    public protected Handler mHandler;
    public protected final Looper mLooper;
    public protected final ILowpanInterfaceListener mInternalCallback = new InternalCallback();
    public protected Callback mCallback = null;
    public protected volatile boolean mIsClosed = false;

    /* loaded from: classes2.dex */
    public static abstract class Callback {
        private protected synchronized Callback() {
        }

        private protected synchronized void onReceiveFromCommissioner(byte[] packet) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void onClosed() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class InternalCallback extends ILowpanInterfaceListener.Stub {
        private InternalCallback() {
        }

        private protected synchronized void onStateChanged(String value) {
            if (!LowpanCommissioningSession.this.mIsClosed) {
                char c = 65535;
                int hashCode = value.hashCode();
                if (hashCode != -1548612125) {
                    if (hashCode == 97204770 && value.equals("fault")) {
                        c = 1;
                    }
                } else if (value.equals("offline")) {
                    c = 0;
                }
                switch (c) {
                    case 0:
                    case 1:
                        synchronized (LowpanCommissioningSession.this) {
                            LowpanCommissioningSession.this.lockedCleanup();
                        }
                        return;
                    default:
                        return;
                }
            }
        }

        private protected synchronized void onReceiveFromCommissioner(final byte[] packet) {
            LowpanCommissioningSession.this.mHandler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanCommissioningSession$InternalCallback$TrrmDykqIWeXNdgrXO7t2-rqCTo
                @Override // java.lang.Runnable
                public final void run() {
                    LowpanCommissioningSession.InternalCallback.lambda$onReceiveFromCommissioner$0(LowpanCommissioningSession.InternalCallback.this, packet);
                }
            });
        }

        public static /* synthetic */ void lambda$onReceiveFromCommissioner$0(InternalCallback internalCallback, byte[] packet) {
            synchronized (LowpanCommissioningSession.this) {
                if (!LowpanCommissioningSession.this.mIsClosed && LowpanCommissioningSession.this.mCallback != null) {
                    LowpanCommissioningSession.this.mCallback.onReceiveFromCommissioner(packet);
                }
            }
        }

        private protected synchronized void onEnabledChanged(boolean value) {
        }

        private protected synchronized void onConnectedChanged(boolean value) {
        }

        private protected synchronized void onUpChanged(boolean value) {
        }

        private protected synchronized void onRoleChanged(String value) {
        }

        private protected synchronized void onLowpanIdentityChanged(LowpanIdentity value) {
        }

        private protected synchronized void onLinkNetworkAdded(IpPrefix value) {
        }

        private protected synchronized void onLinkNetworkRemoved(IpPrefix value) {
        }

        private protected synchronized void onLinkAddressAdded(String value) {
        }

        private protected synchronized void onLinkAddressRemoved(String value) {
        }
    }

    public private protected synchronized LowpanCommissioningSession(ILowpanInterface binder, LowpanBeaconInfo beaconInfo, Looper looper) {
        this.mBinder = binder;
        this.mBeaconInfo = beaconInfo;
        this.mLooper = looper;
        if (this.mLooper != null) {
            this.mHandler = new Handler(this.mLooper);
        } else {
            this.mHandler = new Handler();
        }
        try {
            this.mBinder.addListener(this.mInternalCallback);
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized void lockedCleanup() {
        if (!this.mIsClosed) {
            try {
                this.mBinder.removeListener(this.mInternalCallback);
            } catch (DeadObjectException e) {
            } catch (RemoteException x) {
                throw x.rethrowAsRuntimeException();
            }
            if (this.mCallback != null) {
                this.mHandler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanCommissioningSession$jqpl-iUq-e7YuWqkG33P8PNe7Ag
                    @Override // java.lang.Runnable
                    public final void run() {
                        LowpanCommissioningSession.this.mCallback.onClosed();
                    }
                });
            }
        }
        this.mCallback = null;
        this.mIsClosed = true;
    }

    private protected synchronized LowpanBeaconInfo getBeaconInfo() {
        return this.mBeaconInfo;
    }

    private protected synchronized void sendToCommissioner(byte[] packet) {
        if (!this.mIsClosed) {
            try {
                this.mBinder.sendToCommissioner(packet);
            } catch (DeadObjectException e) {
            } catch (RemoteException x) {
                throw x.rethrowAsRuntimeException();
            }
        }
    }

    private protected synchronized void setCallback(Callback cb, Handler handler) {
        if (!this.mIsClosed) {
            if (handler != null) {
                this.mHandler = handler;
            } else if (this.mLooper != null) {
                this.mHandler = new Handler(this.mLooper);
            } else {
                this.mHandler = new Handler();
            }
            this.mCallback = cb;
        }
    }

    private protected synchronized void close() {
        if (!this.mIsClosed) {
            try {
                this.mBinder.closeCommissioningSession();
                lockedCleanup();
            } catch (DeadObjectException e) {
            } catch (RemoteException x) {
                throw x.rethrowAsRuntimeException();
            }
        }
    }
}
