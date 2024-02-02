package android.security.keystore;
/* loaded from: classes2.dex */
public class KeyAttestationException extends Exception {
    public synchronized KeyAttestationException(String detailMessage) {
        super(detailMessage);
    }

    public synchronized KeyAttestationException(String message, Throwable cause) {
        super(message, cause);
    }
}
