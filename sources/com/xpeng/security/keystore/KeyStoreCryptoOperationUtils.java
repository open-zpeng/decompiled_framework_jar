package com.xpeng.security.keystore;

import android.security.keystore.UserNotAuthenticatedException;
import com.xpeng.security.KeyStore;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import libcore.util.EmptyArray;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public abstract class KeyStoreCryptoOperationUtils {
    private static volatile SecureRandom sRng;

    private KeyStoreCryptoOperationUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InvalidKeyException getInvalidKeyExceptionForInit(KeyStore keyStore, AndroidKeyStoreKey key, int beginOpResultCode) {
        if (beginOpResultCode == 1) {
            return null;
        }
        InvalidKeyException e = keyStore.getInvalidKeyException(key.getAlias(), key.getUid(), beginOpResultCode);
        if (beginOpResultCode == 15 && (e instanceof UserNotAuthenticatedException)) {
            return null;
        }
        return e;
    }

    public static GeneralSecurityException getExceptionForCipherInit(KeyStore keyStore, AndroidKeyStoreKey key, int beginOpResultCode) {
        if (beginOpResultCode == 1) {
            return null;
        }
        if (beginOpResultCode != -55) {
            if (beginOpResultCode == -52) {
                return new InvalidAlgorithmParameterException("Invalid IV");
            }
            return getInvalidKeyExceptionForInit(keyStore, key, beginOpResultCode);
        }
        return new InvalidAlgorithmParameterException("Caller-provided IV not permitted");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getRandomBytesToMixIntoKeystoreRng(SecureRandom rng, int sizeBytes) {
        if (sizeBytes <= 0) {
            return EmptyArray.BYTE;
        }
        if (rng == null) {
            rng = getRng();
        }
        byte[] result = new byte[sizeBytes];
        rng.nextBytes(result);
        return result;
    }

    private static SecureRandom getRng() {
        if (sRng == null) {
            sRng = new SecureRandom();
        }
        return sRng;
    }
}
