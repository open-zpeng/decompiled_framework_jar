package android.security.keystore;

import java.math.BigInteger;
import java.security.interfaces.RSAKey;
/* loaded from: classes2.dex */
public class AndroidKeyStoreRSAPrivateKey extends AndroidKeyStorePrivateKey implements RSAKey {
    private final BigInteger mModulus;

    public synchronized AndroidKeyStoreRSAPrivateKey(String alias, int uid, BigInteger modulus) {
        super(alias, uid, "RSA");
        this.mModulus = modulus;
    }

    @Override // java.security.interfaces.RSAKey
    public BigInteger getModulus() {
        return this.mModulus;
    }
}
