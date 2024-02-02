package android.net.lowpan;
/* loaded from: classes2.dex */
public class JoinFailedAtScanException extends JoinFailedException {
    private protected synchronized JoinFailedAtScanException() {
    }

    private protected synchronized JoinFailedAtScanException(String message) {
        super(message);
    }

    private protected synchronized JoinFailedAtScanException(String message, Throwable cause) {
        super(message, cause);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized JoinFailedAtScanException(Exception cause) {
        super(cause);
    }
}
