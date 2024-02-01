package android.security.keystore;

import javax.crypto.SecretKey;

/* loaded from: classes2.dex */
public class AndroidKeyStoreSecretKey extends AndroidKeyStoreKey implements SecretKey {
    public AndroidKeyStoreSecretKey(String alias, int uid, String algorithm) {
        super(alias, uid, algorithm);
    }
}
