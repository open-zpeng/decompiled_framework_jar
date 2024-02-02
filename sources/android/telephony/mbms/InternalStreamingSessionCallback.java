package android.telephony.mbms;

import android.os.Binder;
import android.os.RemoteException;
import android.telephony.mbms.IMbmsStreamingSessionCallback;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class InternalStreamingSessionCallback extends IMbmsStreamingSessionCallback.Stub {
    private final MbmsStreamingSessionCallback mAppCallback;
    private final Executor mExecutor;
    private volatile boolean mIsStopped = false;

    public synchronized InternalStreamingSessionCallback(MbmsStreamingSessionCallback appCallback, Executor executor) {
        this.mAppCallback = appCallback;
        this.mExecutor = executor;
    }

    @Override // android.telephony.mbms.IMbmsStreamingSessionCallback
    public synchronized void onError(final int errorCode, final String message) throws RemoteException {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalStreamingSessionCallback.1
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalStreamingSessionCallback.this.mAppCallback.onError(errorCode, message);
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
        });
    }

    @Override // android.telephony.mbms.IMbmsStreamingSessionCallback
    public synchronized void onStreamingServicesUpdated(final List<StreamingServiceInfo> services) throws RemoteException {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalStreamingSessionCallback.2
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalStreamingSessionCallback.this.mAppCallback.onStreamingServicesUpdated(services);
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
        });
    }

    @Override // android.telephony.mbms.IMbmsStreamingSessionCallback
    public synchronized void onMiddlewareReady() throws RemoteException {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalStreamingSessionCallback.3
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalStreamingSessionCallback.this.mAppCallback.onMiddlewareReady();
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
        });
    }

    public synchronized void stop() {
        this.mIsStopped = true;
    }
}
