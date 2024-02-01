package android.net.lowpan;
/* loaded from: classes2.dex */
public class OperationCanceledException extends LowpanException {
    private protected synchronized OperationCanceledException() {
    }

    private protected synchronized OperationCanceledException(String message) {
        super(message);
    }

    private protected synchronized OperationCanceledException(String message, Throwable cause) {
        super(message, cause);
    }

    public private synchronized OperationCanceledException(Exception cause) {
        super(cause);
    }
}
