package android.net.lowpan;
/* loaded from: classes2.dex */
public class WrongStateException extends LowpanException {
    private protected synchronized WrongStateException() {
    }

    private protected synchronized WrongStateException(String message) {
        super(message);
    }

    private protected synchronized WrongStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public private synchronized WrongStateException(Exception cause) {
        super(cause);
    }
}
