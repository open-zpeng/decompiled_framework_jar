package android.os;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.BugreportManager;
import android.os.IDumpstateListener;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Executor;
import libcore.io.IoUtils;

@SystemApi
/* loaded from: classes2.dex */
public final class BugreportManager {
    private static final String TAG = "BugreportManager";
    private final IDumpstate mBinder;
    private final Context mContext;

    public BugreportManager(Context context, IDumpstate binder) {
        this.mContext = context;
        this.mBinder = binder;
    }

    /* loaded from: classes2.dex */
    public static abstract class BugreportCallback {
        public static final int BUGREPORT_ERROR_ANOTHER_REPORT_IN_PROGRESS = 5;
        public static final int BUGREPORT_ERROR_INVALID_INPUT = 1;
        public static final int BUGREPORT_ERROR_RUNTIME = 2;
        public static final int BUGREPORT_ERROR_USER_CONSENT_TIMED_OUT = 4;
        public static final int BUGREPORT_ERROR_USER_DENIED_CONSENT = 3;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface BugreportErrorCode {
        }

        public void onProgress(float progress) {
        }

        public void onError(int errorCode) {
        }

        public void onFinished() {
        }
    }

    public void startBugreport(ParcelFileDescriptor bugreportFd, ParcelFileDescriptor screenshotFd, BugreportParams params, Executor executor, BugreportCallback callback) {
        try {
            try {
                try {
                    Preconditions.checkNotNull(bugreportFd);
                    Preconditions.checkNotNull(params);
                    Preconditions.checkNotNull(executor);
                    Preconditions.checkNotNull(callback);
                    if (screenshotFd == null) {
                        screenshotFd = ParcelFileDescriptor.open(new File("/dev/null"), 268435456);
                    }
                    DumpstateListener dsListener = new DumpstateListener(executor, callback);
                    this.mBinder.startBugreport(-1, this.mContext.getOpPackageName(), bugreportFd.getFileDescriptor(), screenshotFd.getFileDescriptor(), params.getMode(), dsListener);
                    IoUtils.closeQuietly(bugreportFd);
                } catch (FileNotFoundException e) {
                    Log.wtf(TAG, "Not able to find /dev/null file: ", e);
                    IoUtils.closeQuietly(bugreportFd);
                    if (screenshotFd == null) {
                        return;
                    }
                }
                IoUtils.closeQuietly(screenshotFd);
            } catch (RemoteException e2) {
                throw e2.rethrowFromSystemServer();
            }
        } catch (Throwable th) {
            IoUtils.closeQuietly(bugreportFd);
            if (screenshotFd != null) {
                IoUtils.closeQuietly(screenshotFd);
            }
            throw th;
        }
    }

    public void cancelBugreport() {
        try {
            this.mBinder.cancelBugreport();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class DumpstateListener extends IDumpstateListener.Stub {
        private final BugreportCallback mCallback;
        private final Executor mExecutor;

        DumpstateListener(Executor executor, BugreportCallback callback) {
            this.mExecutor = executor;
            this.mCallback = callback;
        }

        @Override // android.os.IDumpstateListener
        public void onProgress(final int progress) throws RemoteException {
            long identity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.os.-$$Lambda$BugreportManager$DumpstateListener$Vi18nEKTiYzuC_I5Io1XCZxd88w
                    @Override // java.lang.Runnable
                    public final void run() {
                        BugreportManager.DumpstateListener.this.lambda$onProgress$0$BugreportManager$DumpstateListener(progress);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        }

        public /* synthetic */ void lambda$onProgress$0$BugreportManager$DumpstateListener(int progress) {
            this.mCallback.onProgress(progress);
        }

        @Override // android.os.IDumpstateListener
        public void onError(final int errorCode) throws RemoteException {
            long identity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.os.-$$Lambda$BugreportManager$DumpstateListener$srBmWyEMI-89xDivmKB4DtiSQ2A
                    @Override // java.lang.Runnable
                    public final void run() {
                        BugreportManager.DumpstateListener.this.lambda$onError$1$BugreportManager$DumpstateListener(errorCode);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        }

        public /* synthetic */ void lambda$onError$1$BugreportManager$DumpstateListener(int errorCode) {
            this.mCallback.onError(errorCode);
        }

        @Override // android.os.IDumpstateListener
        public void onFinished() throws RemoteException {
            long identity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.os.-$$Lambda$BugreportManager$DumpstateListener$XpZbAM9CYGe3tPOak0Nw-HdFQ8I
                    @Override // java.lang.Runnable
                    public final void run() {
                        BugreportManager.DumpstateListener.this.lambda$onFinished$2$BugreportManager$DumpstateListener();
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        }

        public /* synthetic */ void lambda$onFinished$2$BugreportManager$DumpstateListener() {
            this.mCallback.onFinished();
        }

        @Override // android.os.IDumpstateListener
        public void onProgressUpdated(int progress) throws RemoteException {
        }

        @Override // android.os.IDumpstateListener
        public void onMaxProgressUpdated(int maxProgress) throws RemoteException {
        }

        @Override // android.os.IDumpstateListener
        public void onSectionComplete(String title, int status, int size, int durationMs) throws RemoteException {
        }
    }
}
