package android.hardware.biometrics;

import android.security.keystore.AndroidKeyStoreProvider;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;
/* loaded from: classes.dex */
public class CryptoObject {
    private final Object mCrypto;

    public synchronized CryptoObject(Signature signature) {
        this.mCrypto = signature;
    }

    public synchronized CryptoObject(Cipher cipher) {
        this.mCrypto = cipher;
    }

    public synchronized CryptoObject(Mac mac) {
        this.mCrypto = mac;
    }

    public synchronized Signature getSignature() {
        if (this.mCrypto instanceof Signature) {
            return (Signature) this.mCrypto;
        }
        return null;
    }

    public synchronized Cipher getCipher() {
        if (this.mCrypto instanceof Cipher) {
            return (Cipher) this.mCrypto;
        }
        return null;
    }

    public synchronized Mac getMac() {
        if (this.mCrypto instanceof Mac) {
            return (Mac) this.mCrypto;
        }
        return null;
    }

    public final synchronized long getOpId() {
        if (this.mCrypto != null) {
            return AndroidKeyStoreProvider.getKeyStoreOperationHandle(this.mCrypto);
        }
        return 0L;
    }
}
