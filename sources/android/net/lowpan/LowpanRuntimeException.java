package android.net.lowpan;

import android.util.AndroidRuntimeException;
/* loaded from: classes2.dex */
public class LowpanRuntimeException extends AndroidRuntimeException {
    private protected synchronized LowpanRuntimeException() {
    }

    private protected synchronized LowpanRuntimeException(String message) {
        super(message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LowpanRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LowpanRuntimeException(Exception cause) {
        super(cause);
    }
}
