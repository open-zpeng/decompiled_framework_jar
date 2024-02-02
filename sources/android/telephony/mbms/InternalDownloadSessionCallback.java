package android.telephony.mbms;

import android.os.Binder;
import android.telephony.mbms.IMbmsDownloadSessionCallback;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class InternalDownloadSessionCallback extends IMbmsDownloadSessionCallback.Stub {
    private final MbmsDownloadSessionCallback mAppCallback;
    private final Executor mExecutor;
    private volatile boolean mIsStopped = false;

    public synchronized InternalDownloadSessionCallback(MbmsDownloadSessionCallback appCallback, Executor executor) {
        this.mAppCallback = appCallback;
        this.mExecutor = executor;
    }

    @Override // android.telephony.mbms.IMbmsDownloadSessionCallback
    public synchronized void onError(final int errorCode, final String message) {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalDownloadSessionCallback.1
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalDownloadSessionCallback.this.mAppCallback.onError(errorCode, message);
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
        });
    }

    @Override // android.telephony.mbms.IMbmsDownloadSessionCallback
    public synchronized void onFileServicesUpdated(final List<FileServiceInfo> services) {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalDownloadSessionCallback.2
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalDownloadSessionCallback.this.mAppCallback.onFileServicesUpdated(services);
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
        });
    }

    @Override // android.telephony.mbms.IMbmsDownloadSessionCallback
    public synchronized void onMiddlewareReady() {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalDownloadSessionCallback.3
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalDownloadSessionCallback.this.mAppCallback.onMiddlewareReady();
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
