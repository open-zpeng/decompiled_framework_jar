package android.os;
/* loaded from: classes2.dex */
public class ServiceSpecificException extends RuntimeException {
    private protected final int errorCode;

    /* JADX INFO: Access modifiers changed from: private */
    public ServiceSpecificException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public synchronized ServiceSpecificException(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return super.toString() + " (code " + this.errorCode + ")";
    }
}
