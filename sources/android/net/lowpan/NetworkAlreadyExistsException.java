package android.net.lowpan;
/* loaded from: classes2.dex */
public class NetworkAlreadyExistsException extends LowpanException {
    private protected synchronized NetworkAlreadyExistsException() {
    }

    private protected synchronized NetworkAlreadyExistsException(String message) {
        super(message, null);
    }

    private protected synchronized NetworkAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized NetworkAlreadyExistsException(Exception cause) {
        super(cause);
    }
}
