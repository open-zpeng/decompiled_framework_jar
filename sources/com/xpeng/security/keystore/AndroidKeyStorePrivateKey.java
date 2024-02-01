package com.xpeng.security.keystore;

import java.security.PrivateKey;

/* loaded from: classes3.dex */
public class AndroidKeyStorePrivateKey extends AndroidKeyStoreKey implements PrivateKey {
    public AndroidKeyStorePrivateKey(String alias, int uid, String algorithm) {
        super(alias, uid, algorithm);
    }
}
