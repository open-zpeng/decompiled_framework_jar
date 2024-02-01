package android.os;

import android.util.AndroidException;
/* loaded from: classes2.dex */
public class RemoteException extends AndroidException {
    public RemoteException() {
    }

    public RemoteException(String message) {
        super(message);
    }

    public synchronized RemoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public synchronized RuntimeException rethrowAsRuntimeException() {
        throw new RuntimeException(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RuntimeException rethrowFromSystemServer() {
        if (this instanceof DeadObjectException) {
            throw new RuntimeException(new DeadSystemException());
        }
        throw new RuntimeException(this);
    }
}
