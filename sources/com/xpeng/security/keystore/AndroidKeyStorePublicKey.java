package com.xpeng.security.keystore;

import java.security.PublicKey;
import java.util.Arrays;
/* loaded from: classes3.dex */
public class AndroidKeyStorePublicKey extends AndroidKeyStoreKey implements PublicKey {
    private final byte[] mEncoded;

    public AndroidKeyStorePublicKey(String alias, int uid, String algorithm, byte[] x509EncodedForm) {
        super(alias, uid, algorithm);
        this.mEncoded = ArrayUtils.cloneIfNotEmpty(x509EncodedForm);
    }

    @Override // com.xpeng.security.keystore.AndroidKeyStoreKey, java.security.Key
    public String getFormat() {
        return "X.509";
    }

    @Override // com.xpeng.security.keystore.AndroidKeyStoreKey, java.security.Key
    public byte[] getEncoded() {
        return ArrayUtils.cloneIfNotEmpty(this.mEncoded);
    }

    @Override // com.xpeng.security.keystore.AndroidKeyStoreKey
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + Arrays.hashCode(this.mEncoded);
    }

    @Override // com.xpeng.security.keystore.AndroidKeyStoreKey
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
