package com.xpeng.security.keystore;

import javax.crypto.SecretKey;

/* loaded from: classes3.dex */
public class AndroidKeyStoreSecretKey extends AndroidKeyStoreKey implements SecretKey {
    public AndroidKeyStoreSecretKey(String alias, int uid, String algorithm) {
        super(alias, uid, algorithm);
    }
}
