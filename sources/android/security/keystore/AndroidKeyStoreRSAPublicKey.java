package android.security.keystore;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
/* loaded from: classes2.dex */
public class AndroidKeyStoreRSAPublicKey extends AndroidKeyStorePublicKey implements RSAPublicKey {
    private final BigInteger mModulus;
    private final BigInteger mPublicExponent;

    public synchronized AndroidKeyStoreRSAPublicKey(String alias, int uid, byte[] x509EncodedForm, BigInteger modulus, BigInteger publicExponent) {
        super(alias, uid, "RSA", x509EncodedForm);
        this.mModulus = modulus;
        this.mPublicExponent = publicExponent;
    }

    public synchronized AndroidKeyStoreRSAPublicKey(String alias, int uid, RSAPublicKey info) {
        this(alias, uid, info.getEncoded(), info.getModulus(), info.getPublicExponent());
        if (!"X.509".equalsIgnoreCase(info.getFormat())) {
            throw new IllegalArgumentException("Unsupported key export format: " + info.getFormat());
        }
    }

    @Override // java.security.interfaces.RSAKey
    public BigInteger getModulus() {
        return this.mModulus;
    }

    @Override // java.security.interfaces.RSAPublicKey
    public BigInteger getPublicExponent() {
        return this.mPublicExponent;
    }
}
