package android.security.keystore;

import java.security.PublicKey;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class AndroidKeyStorePublicKey extends AndroidKeyStoreKey implements PublicKey {
    private final byte[] mEncoded;

    public AndroidKeyStorePublicKey(String alias, int uid, String algorithm, byte[] x509EncodedForm) {
        super(alias, uid, algorithm);
        this.mEncoded = ArrayUtils.cloneIfNotEmpty(x509EncodedForm);
    }

    @Override // android.security.keystore.AndroidKeyStoreKey, java.security.Key
    public String getFormat() {
        return "X.509";
    }

    @Override // android.security.keystore.AndroidKeyStoreKey, java.security.Key
    public byte[] getEncoded() {
        return ArrayUtils.cloneIfNotEmpty(this.mEncoded);
    }

    @Override // android.security.keystore.AndroidKeyStoreKey
    public int hashCode() {
        int result = super.hashCode();
        return (result * 31) + Arrays.hashCode(this.mEncoded);
    }

    @Override // android.security.keystore.AndroidKeyStoreKey
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (super.equals(obj) && getClass() == obj.getClass()) {
            AndroidKeyStorePublicKey other = (AndroidKeyStorePublicKey) obj;
            return Arrays.equals(this.mEncoded, other.mEncoded);
        }
        return false;
    }
}
