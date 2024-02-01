package com.xpeng.security.keystore;

import java.math.BigInteger;
import java.security.interfaces.RSAKey;

/* loaded from: classes3.dex */
public class AndroidKeyStoreRSAPrivateKey extends AndroidKeyStorePrivateKey implements RSAKey {
    private final BigInteger mModulus;

    public AndroidKeyStoreRSAPrivateKey(String alias, int uid, BigInteger modulus) {
        super(alias, uid, "RSA");
        this.mModulus = modulus;
    }

    @Override // java.security.interfaces.RSAKey
    public BigInteger getModulus() {
        return this.mModulus;
    }
}
