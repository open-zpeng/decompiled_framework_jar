package android.net.lowpan;
/* loaded from: classes2.dex */
public class InterfaceDisabledException extends LowpanException {
    private protected synchronized InterfaceDisabledException() {
    }

    private protected synchronized InterfaceDisabledException(String message) {
        super(message);
    }

    private protected synchronized InterfaceDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

    public private synchronized InterfaceDisabledException(Exception cause) {
        super(cause);
    }
}
