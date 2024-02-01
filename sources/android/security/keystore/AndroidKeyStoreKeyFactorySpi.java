package android.security.keystore;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.storage.VolumeInfo;
import android.security.KeyStore;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

/* loaded from: classes2.dex */
public class AndroidKeyStoreKeyFactorySpi extends KeyFactorySpi {
    private final KeyStore mKeyStore = KeyStore.getInstance();

    @Override // java.security.KeyFactorySpi
    protected <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> keySpecClass) throws InvalidKeySpecException {
        if (key == null) {
            throw new InvalidKeySpecException("key == null");
        }
        if (!(key instanceof AndroidKeyStorePrivateKey) && !(key instanceof AndroidKeyStorePublicKey)) {
            throw new InvalidKeySpecException("Unsupported key type: " + key.getClass().getName() + ". This KeyFactory supports only Android Keystore asymmetric keys");
        } else if (keySpecClass == null) {
            throw new InvalidKeySpecException("keySpecClass == null");
        } else {
            if (KeyInfo.class.equals(keySpecClass)) {
                if (!(key instanceof AndroidKeyStorePrivateKey)) {
                    throw new InvalidKeySpecException("Unsupported key type: " + key.getClass().getName() + ". KeyInfo can be obtained only for Android Keystore private keys");
                }
                AndroidKeyStorePrivateKey keystorePrivateKey = (AndroidKeyStorePrivateKey) key;
                String keyAliasInKeystore = keystorePrivateKey.getAlias();
                if (keyAliasInKeystore.startsWith("USRPKEY_")) {
                    String entryAlias = keyAliasInKeystore.substring("USRPKEY_".length());
                    T result = AndroidKeyStoreSecretKeyFactorySpi.getKeyInfo(this.mKeyStore, entryAlias, keyAliasInKeystore, keystorePrivateKey.getUid());
                    return result;
                }
                throw new InvalidKeySpecException("Invalid key alias: " + keyAliasInKeystore);
            } else if (X509EncodedKeySpec.class.equals(keySpecClass)) {
                if (!(key instanceof AndroidKeyStorePublicKey)) {
                    throw new InvalidKeySpecException("Unsupported key type: " + key.getClass().getName() + ". X509EncodedKeySpec can be obtained only for Android Keystore public keys");
                }
                T result2 = new X509EncodedKeySpec(((AndroidKeyStorePublicKey) key).getEncoded());
                return result2;
            } else if (PKCS8EncodedKeySpec.class.equals(keySpecClass)) {
                if (key instanceof AndroidKeyStorePrivateKey) {
                    throw new InvalidKeySpecException("Key material export of Android Keystore private keys is not supported");
                }
                throw new InvalidKeySpecException("Cannot export key material of public key in PKCS#8 format. Only X.509 format (X509EncodedKeySpec) supported for public keys.");
            } else {
                boolean equals = RSAPublicKeySpec.class.equals(keySpecClass);
                String str = VolumeInfo.ID_PRIVATE_INTERNAL;
                if (equals) {
                    if (key instanceof AndroidKeyStoreRSAPublicKey) {
                        AndroidKeyStoreRSAPublicKey rsaKey = (AndroidKeyStoreRSAPublicKey) key;
                        T result3 = new RSAPublicKeySpec(rsaKey.getModulus(), rsaKey.getPublicExponent());
                        return result3;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Obtaining RSAPublicKeySpec not supported for ");
                    sb.append(key.getAlgorithm());
                    sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    if (!(key instanceof AndroidKeyStorePrivateKey)) {
                        str = "public";
                    }
                    sb.append(str);
                    sb.append(" key");
                    throw new InvalidKeySpecException(sb.toString());
                } else if (ECPublicKeySpec.class.equals(keySpecClass)) {
                    if (key instanceof AndroidKeyStoreECPublicKey) {
                        AndroidKeyStoreECPublicKey ecKey = (AndroidKeyStoreECPublicKey) key;
                        T result4 = new ECPublicKeySpec(ecKey.getW(), ecKey.getParams());
                        return result4;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Obtaining ECPublicKeySpec not supported for ");
                    sb2.append(key.getAlgorithm());
                    sb2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    if (!(key instanceof AndroidKeyStorePrivateKey)) {
                        str = "public";
                    }
                    sb2.append(str);
                    sb2.append(" key");
                    throw new InvalidKeySpecException(sb2.toString());
                } else {
                    throw new InvalidKeySpecException("Unsupported key spec: " + keySpecClass.getName());
                }
            }
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PrivateKey engineGeneratePrivate(KeySpec spec) throws InvalidKeySpecException {
        throw new InvalidKeySpecException("To generate a key pair in Android Keystore, use KeyPairGenerator initialized with " + KeyGenParameterSpec.class.getName());
    }

    @Override // java.security.KeyFactorySpi
    protected PublicKey engineGeneratePublic(KeySpec spec) throws InvalidKeySpecException {
        throw new InvalidKeySpecException("To generate a key pair in Android Keystore, use KeyPairGenerator initialized with " + KeyGenParameterSpec.class.getName());
    }

    @Override // java.security.KeyFactorySpi
    protected Key engineTranslateKey(Key key) throws InvalidKeyException {
        if (key == null) {
            throw new InvalidKeyException("key == null");
        }
        if (!(key instanceof AndroidKeyStorePrivateKey) && !(key instanceof AndroidKeyStorePublicKey)) {
            throw new InvalidKeyException("To import a key into Android Keystore, use KeyStore.setEntry");
        }
        return key;
    }
}
