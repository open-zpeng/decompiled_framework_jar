package android.telephony.mbms;

import android.os.Binder;
import android.os.RemoteException;
import android.telephony.mbms.IDownloadProgressListener;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class InternalDownloadProgressListener extends IDownloadProgressListener.Stub {
    private final DownloadProgressListener mAppListener;
    private final Executor mExecutor;
    private volatile boolean mIsStopped = false;

    public synchronized InternalDownloadProgressListener(DownloadProgressListener appListener, Executor executor) {
        this.mAppListener = appListener;
        this.mExecutor = executor;
    }

    @Override // android.telephony.mbms.IDownloadProgressListener
    public synchronized void onProgressUpdated(final DownloadRequest request, final FileInfo fileInfo, final int currentDownloadSize, final int fullDownloadSize, final int currentDecodedSize, final int fullDecodedSize) throws RemoteException {
        if (this.mIsStopped) {
            return;
        }
        this.mExecutor.execute(new Runnable() { // from class: android.telephony.mbms.InternalDownloadProgressListener.1
            @Override // java.lang.Runnable
            public void run() {
                long token = Binder.clearCallingIdentity();
                try {
                    InternalDownloadProgressListener.this.mAppListener.onProgressUpdated(request, fileInfo, currentDownloadSize, fullDownloadSize, currentDecodedSize, fullDecodedSize);
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
