package android.telephony.mbms;

import android.os.Binder;
import android.os.RemoteException;
import android.telephony.mbms.IDownloadStatusListener;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class InternalDownloadStatusListener extends IDownloadStatusListener.Stub {
    private final DownloadStatusListener mAppListener;
    private final Executor mExecutor;
    private volatile boolean mIsStopped = false;

    public synchronized InternalDownloadStatusListener(DownloadStatusListener appCallback, Executor executor) {
        this.mAppListener = appCallback;
        this.mExecutor = executor;
    }

    @Override // android.telephony.mbms.IDownloadStatusListener
    public synchronized void onStatusUpdated(final DownloadRequest request, final FileInfo fileInfo, final int status) throws RemoteException {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalDownloadStatusListener.1
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalDownloadStatusListener.this.mAppListener.onStatusUpdated(request, fileInfo, status);
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
