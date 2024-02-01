package android.security.keystore;

import java.security.PrivateKey;

/* loaded from: classes2.dex */
public class AndroidKeyStorePrivateKey extends AndroidKeyStoreKey implements PrivateKey {
    public AndroidKeyStorePrivateKey(String alias, int uid, String algorithm) {
        super(alias, uid, algorithm);
    }
}
