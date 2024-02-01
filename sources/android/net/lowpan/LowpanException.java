package android.net.lowpan;

import android.os.ServiceSpecificException;
import android.util.AndroidException;
/* loaded from: classes2.dex */
public class LowpanException extends AndroidException {
    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LowpanException() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LowpanException(String message) {
        super(message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LowpanException(String message, Throwable cause) {
        super(message, cause);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LowpanException(Exception cause) {
        super(cause);
    }

    public private protected static synchronized LowpanException rethrowFromServiceSpecificException(ServiceSpecificException e) throws LowpanException {
        switch (e.errorCode) {
            case 2:
                throw new LowpanRuntimeException(e.getMessage() != null ? e.getMessage() : "Invalid argument", e);
            case 3:
                throw new InterfaceDisabledException(e);
            case 4:
                throw new WrongStateException(e);
            case 5:
            case 6:
            case 8:
            case 9:
            default:
                throw new LowpanRuntimeException(e);
            case 7:
                throw new LowpanRuntimeException(e.getMessage() != null ? e.getMessage() : "NCP problem", e);
            case 10:
                throw new OperationCanceledException(e);
            case 11:
                throw new LowpanException(e.getMessage() != null ? e.getMessage() : "Feature not supported", e);
            case 12:
                throw new JoinFailedException(e);
            case 13:
                throw new JoinFailedAtScanException(e);
            case 14:
                throw new JoinFailedAtAuthException(e);
            case 15:
                throw new NetworkAlreadyExistsException(e);
        }
    }
}
