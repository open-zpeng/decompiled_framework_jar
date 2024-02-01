package android.security.keystore;

import java.security.ProviderException;
/* loaded from: classes2.dex */
public class KeyStoreConnectException extends ProviderException {
    public synchronized KeyStoreConnectException() {
        super("Failed to communicate with keystore service");
    }
}
