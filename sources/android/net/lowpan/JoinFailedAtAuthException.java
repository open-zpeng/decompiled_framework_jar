package android.net.lowpan;
/* loaded from: classes2.dex */
public class JoinFailedAtAuthException extends JoinFailedException {
    private protected synchronized JoinFailedAtAuthException() {
    }

    private protected synchronized JoinFailedAtAuthException(String message) {
        super(message);
    }

    private protected synchronized JoinFailedAtAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized JoinFailedAtAuthException(Exception cause) {
        super(cause);
    }
}
