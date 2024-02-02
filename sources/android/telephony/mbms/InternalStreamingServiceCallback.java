package android.telephony.mbms;

import android.os.Binder;
import android.os.RemoteException;
import android.telephony.mbms.IStreamingServiceCallback;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class InternalStreamingServiceCallback extends IStreamingServiceCallback.Stub {
    private final StreamingServiceCallback mAppCallback;
    private final Executor mExecutor;
    private volatile boolean mIsStopped = false;

    public synchronized InternalStreamingServiceCallback(StreamingServiceCallback appCallback, Executor executor) {
        this.mAppCallback = appCallback;
        this.mExecutor = executor;
    }

    @Override // android.telephony.mbms.IStreamingServiceCallback
    public synchronized void onError(final int errorCode, final String message) throws RemoteException {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalStreamingServiceCallback.1
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalStreamingServiceCallback.this.mAppCallback.onError(errorCode, message);
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
        });
    }

    @Override // android.telephony.mbms.IStreamingServiceCallback
    public synchronized void onStreamStateUpdated(final int state, final int reason) throws RemoteException {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalStreamingServiceCallback.2
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalStreamingServiceCallback.this.mAppCallback.onStreamStateUpdated(state, reason);
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
        });
    }

    @Override // android.telephony.mbms.IStreamingServiceCallback
    public synchronized void onMediaDescriptionUpdated() throws RemoteException {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalStreamingServiceCallback.3
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalStreamingServiceCallback.this.mAppCallback.onMediaDescriptionUpdated();
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
        });
    }

    @Override // android.telephony.mbms.IStreamingServiceCallback
    public synchronized void onBroadcastSignalStrengthUpdated(final int signalStrength) throws RemoteException {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalStreamingServiceCallback.4
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalStreamingServiceCallback.this.mAppCallback.onBroadcastSignalStrengthUpdated(signalStrength);
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
        });
    }

    @Override // android.telephony.mbms.IStreamingServiceCallback
    public synchronized void onStreamMethodUpdated(final int methodType) throws RemoteException {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalStreamingServiceCallback.5
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalStreamingServiceCallback.this.mAppCallback.onStreamMethodUpdated(methodType);
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
