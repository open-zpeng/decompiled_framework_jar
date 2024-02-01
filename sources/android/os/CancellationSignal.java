package android.os;

import android.os.ICancellationSignal;
/* loaded from: classes2.dex */
public final class CancellationSignal {
    public protected boolean mCancelInProgress;
    public protected boolean mIsCanceled;
    public protected OnCancelListener mOnCancelListener;
    public protected ICancellationSignal mRemote;

    /* loaded from: classes2.dex */
    public interface OnCancelListener {
        void onCancel();
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this) {
            z = this.mIsCanceled;
        }
        return z;
    }

    public void throwIfCanceled() {
        if (isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    public void cancel() {
        synchronized (this) {
            if (this.mIsCanceled) {
                return;
            }
            this.mIsCanceled = true;
            this.mCancelInProgress = true;
            OnCancelListener listener = this.mOnCancelListener;
            ICancellationSignal remote = this.mRemote;
            if (listener != null) {
                try {
                    listener.onCancel();
                } catch (Throwable th) {
                    synchronized (this) {
                        this.mCancelInProgress = false;
                        notifyAll();
                        throw th;
                    }
                }
            }
            if (remote != null) {
                try {
                    remote.cancel();
                } catch (RemoteException e) {
                }
            }
            synchronized (this) {
                this.mCancelInProgress = false;
                notifyAll();
            }
        }
    }

    public void setOnCancelListener(OnCancelListener listener) {
        synchronized (this) {
            waitForCancelFinishedLocked();
            if (this.mOnCancelListener == listener) {
                return;
            }
            this.mOnCancelListener = listener;
            if (this.mIsCanceled && listener != null) {
                listener.onCancel();
            }
        }
    }

    public synchronized void setRemote(ICancellationSignal remote) {
        waitForCancelFinishedLocked();
        if (this.mRemote == remote) {
            return;
        }
        this.mRemote = remote;
        if (this.mIsCanceled && remote != null) {
            try {
                remote.cancel();
            } catch (RemoteException e) {
            }
        }
    }

    public protected void waitForCancelFinishedLocked() {
        while (this.mCancelInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public static synchronized ICancellationSignal createTransport() {
        return new Transport();
    }

    public static synchronized CancellationSignal fromTransport(ICancellationSignal transport) {
        if (transport instanceof Transport) {
            return ((Transport) transport).mCancellationSignal;
        }
        return null;
    }

    /* loaded from: classes2.dex */
    private static final class Transport extends ICancellationSignal.Stub {
        final CancellationSignal mCancellationSignal;

        private synchronized Transport() {
            this.mCancellationSignal = new CancellationSignal();
        }

        @Override // android.os.ICancellationSignal
        public synchronized void cancel() throws RemoteException {
            this.mCancellationSignal.cancel();
        }
    }
}
