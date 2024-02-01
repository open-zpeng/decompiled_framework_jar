package android.util.apk;
/* loaded from: classes2.dex */
public class SignatureNotFoundException extends Exception {
    public protected static final long serialVersionUID = 1;

    public synchronized SignatureNotFoundException(String message) {
        super(message);
    }

    public synchronized SignatureNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
