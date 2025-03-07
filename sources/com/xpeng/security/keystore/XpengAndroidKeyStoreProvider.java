package com.xpeng.security.keystore;

import android.annotation.UnsupportedAppUsage;
import android.os.SystemProperties;
import android.security.keymaster.ExportResult;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterDefs;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import com.xpeng.security.KeyStore;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.Mac;

/* loaded from: classes3.dex */
public class XpengAndroidKeyStoreProvider extends Provider {
    private static final String DESEDE_SYSTEM_PROPERTY = "ro.hardware.keystore_desede";
    private static final String PACKAGE_NAME = "com.xpeng.security.keystore";
    public static final String PROVIDER_NAME = "XpengAndroidKeyStore";

    public XpengAndroidKeyStoreProvider() {
        super(PROVIDER_NAME, 1.0d, "Xpeng Android KeyStore security provider");
        boolean supports3DES = "true".equals(SystemProperties.get(DESEDE_SYSTEM_PROPERTY));
        put("KeyStore.XpengAndroidKeyStore", "com.xpeng.security.keystore.AndroidKeyStoreSpi");
        put("KeyPairGenerator.EC", "com.xpeng.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi$EC");
        put("KeyPairGenerator.RSA", "com.xpeng.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi$RSA");
        putKeyFactoryImpl("EC");
        putKeyFactoryImpl("RSA");
        put("KeyGenerator.AES", "com.xpeng.security.keystore.AndroidKeyStoreKeyGeneratorSpi$AES");
        put("KeyGenerator.HmacSHA1", "com.xpeng.security.keystore.AndroidKeyStoreKeyGeneratorSpi$HmacSHA1");
        put("KeyGenerator.HmacSHA224", "com.xpeng.security.keystore.AndroidKeyStoreKeyGeneratorSpi$HmacSHA224");
        put("KeyGenerator.HmacSHA256", "com.xpeng.security.keystore.AndroidKeyStoreKeyGeneratorSpi$HmacSHA256");
        put("KeyGenerator.HmacSHA384", "com.xpeng.security.keystore.AndroidKeyStoreKeyGeneratorSpi$HmacSHA384");
        put("KeyGenerator.HmacSHA512", "com.xpeng.security.keystore.AndroidKeyStoreKeyGeneratorSpi$HmacSHA512");
        if (supports3DES) {
            put("KeyGenerator.DESede", "com.xpeng.security.keystore.AndroidKeyStoreKeyGeneratorSpi$DESede");
        }
        putSecretKeyFactoryImpl("AES");
        if (supports3DES) {
            putSecretKeyFactoryImpl("DESede");
        }
        putSecretKeyFactoryImpl("HmacSHA1");
        putSecretKeyFactoryImpl("HmacSHA224");
        putSecretKeyFactoryImpl("HmacSHA256");
        putSecretKeyFactoryImpl("HmacSHA384");
        putSecretKeyFactoryImpl("HmacSHA512");
    }

    public static void install() {
        Provider[] providers = Security.getProviders();
        int bcProviderIndex = -1;
        int i = 0;
        while (true) {
            if (i >= providers.length) {
                break;
            }
            Provider provider = providers[i];
            if (!"BC".equals(provider.getName())) {
                i++;
            } else {
                bcProviderIndex = i;
                break;
            }
        }
        Security.addProvider(new XpengAndroidKeyStoreProvider());
        Provider workaroundProvider = new XpengAndroidKeyStoreBCWorkaroundProvider();
        if (bcProviderIndex != -1) {
            Security.insertProviderAt(workaroundProvider, bcProviderIndex + 1);
        } else {
            Security.addProvider(workaroundProvider);
        }
    }

    private void putSecretKeyFactoryImpl(String algorithm) {
        put("SecretKeyFactory." + algorithm, "com.xpeng.security.keystore.AndroidKeyStoreSecretKeyFactorySpi");
    }

    private void putKeyFactoryImpl(String algorithm) {
        put("KeyFactory." + algorithm, "com.xpeng.security.keystore.AndroidKeyStoreKeyFactorySpi");
    }

    @UnsupportedAppUsage
    public static long getKeyStoreOperationHandle(Object cryptoPrimitive) {
        Object spi;
        if (cryptoPrimitive == null) {
            throw new NullPointerException();
        }
        if (cryptoPrimitive instanceof Signature) {
            spi = ((Signature) cryptoPrimitive).getCurrentSpi();
        } else if (cryptoPrimitive instanceof Mac) {
            spi = ((Mac) cryptoPrimitive).getCurrentSpi();
        } else if (cryptoPrimitive instanceof Cipher) {
            spi = ((Cipher) cryptoPrimitive).getCurrentSpi();
        } else {
            throw new IllegalArgumentException("Unsupported crypto primitive: " + cryptoPrimitive + ". Supported: Signature, Mac, Cipher");
        }
        if (spi == null) {
            throw new IllegalStateException("Crypto primitive not initialized");
        }
        if (!(spi instanceof KeyStoreCryptoOperation)) {
            throw new IllegalArgumentException("Crypto primitive not backed by AndroidKeyStore provider: " + cryptoPrimitive + ", spi: " + spi);
        }
        return ((KeyStoreCryptoOperation) spi).getOperationHandle();
    }

    public static AndroidKeyStorePublicKey getAndroidKeyStorePublicKey(String alias, int uid, String keyAlgorithm, byte[] x509EncodedForm) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(x509EncodedForm));
            if ("EC".equalsIgnoreCase(keyAlgorithm)) {
                return new AndroidKeyStoreECPublicKey(alias, uid, (ECPublicKey) publicKey);
            }
            if ("RSA".equalsIgnoreCase(keyAlgorithm)) {
                return new AndroidKeyStoreRSAPublicKey(alias, uid, (RSAPublicKey) publicKey);
            }
            throw new ProviderException("Unsupported Android Keystore public key algorithm: " + keyAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new ProviderException("Failed to obtain " + keyAlgorithm + " KeyFactory", e);
        } catch (InvalidKeySpecException e2) {
            throw new ProviderException("Invalid X.509 encoding of public key", e2);
        }
    }

    private static AndroidKeyStorePrivateKey getAndroidKeyStorePrivateKey(AndroidKeyStorePublicKey publicKey) {
        String keyAlgorithm = publicKey.getAlgorithm();
        if ("EC".equalsIgnoreCase(keyAlgorithm)) {
            return new AndroidKeyStoreECPrivateKey(publicKey.getAlias(), publicKey.getUid(), ((ECKey) publicKey).getParams());
        }
        if ("RSA".equalsIgnoreCase(keyAlgorithm)) {
            return new AndroidKeyStoreRSAPrivateKey(publicKey.getAlias(), publicKey.getUid(), ((RSAKey) publicKey).getModulus());
        }
        throw new ProviderException("Unsupported Android Keystore public key algorithm: " + keyAlgorithm);
    }

    private static KeyCharacteristics getKeyCharacteristics(KeyStore keyStore, String alias, int uid) throws UnrecoverableKeyException, KeyPermanentlyInvalidatedException {
        KeyCharacteristics keyCharacteristics = new KeyCharacteristics();
        int errorCode = keyStore.getKeyCharacteristics(alias, null, null, uid, keyCharacteristics);
        if (errorCode == 17) {
            throw new KeyPermanentlyInvalidatedException("User changed or deleted their auth credentials", KeyStore.getKeyStoreException(errorCode));
        }
        if (errorCode != 1) {
            throw ((UnrecoverableKeyException) new UnrecoverableKeyException("Failed to obtain information about key").initCause(KeyStore.getKeyStoreException(errorCode)));
        }
        return keyCharacteristics;
    }

    private static AndroidKeyStorePublicKey loadAndroidKeyStorePublicKeyFromKeystore(KeyStore keyStore, String privateKeyAlias, int uid, KeyCharacteristics keyCharacteristics) throws UnrecoverableKeyException {
        ExportResult exportResult = keyStore.exportKey(privateKeyAlias, 0, null, null, uid);
        if (exportResult.resultCode != 1) {
            throw ((UnrecoverableKeyException) new UnrecoverableKeyException("Failed to obtain X.509 form of public key").initCause(KeyStore.getKeyStoreException(exportResult.resultCode)));
        }
        byte[] x509EncodedPublicKey = exportResult.exportData;
        Integer keymasterAlgorithm = keyCharacteristics.getEnum(KeymasterDefs.KM_TAG_ALGORITHM);
        if (keymasterAlgorithm == null) {
            throw new UnrecoverableKeyException("Key algorithm unknown");
        }
        try {
            String jcaKeyAlgorithm = KeyProperties.KeyAlgorithm.fromKeymasterAsymmetricKeyAlgorithm(keymasterAlgorithm.intValue());
            return getAndroidKeyStorePublicKey(privateKeyAlias, uid, jcaKeyAlgorithm, x509EncodedPublicKey);
        } catch (IllegalArgumentException e) {
            throw ((UnrecoverableKeyException) new UnrecoverableKeyException("Failed to load private key").initCause(e));
        }
    }

    public static AndroidKeyStorePublicKey loadAndroidKeyStorePublicKeyFromKeystore(KeyStore keyStore, String privateKeyAlias, int uid) throws UnrecoverableKeyException, KeyPermanentlyInvalidatedException {
        return loadAndroidKeyStorePublicKeyFromKeystore(keyStore, privateKeyAlias, uid, getKeyCharacteristics(keyStore, privateKeyAlias, uid));
    }

    private static KeyPair loadAndroidKeyStoreKeyPairFromKeystore(KeyStore keyStore, String privateKeyAlias, int uid, KeyCharacteristics keyCharacteristics) throws UnrecoverableKeyException {
        AndroidKeyStorePublicKey publicKey = loadAndroidKeyStorePublicKeyFromKeystore(keyStore, privateKeyAlias, uid, keyCharacteristics);
        AndroidKeyStorePrivateKey privateKey = getAndroidKeyStorePrivateKey(publicKey);
        return new KeyPair(publicKey, privateKey);
    }

    public static KeyPair loadAndroidKeyStoreKeyPairFromKeystore(KeyStore keyStore, String privateKeyAlias, int uid) throws UnrecoverableKeyException, KeyPermanentlyInvalidatedException {
        return loadAndroidKeyStoreKeyPairFromKeystore(keyStore, privateKeyAlias, uid, getKeyCharacteristics(keyStore, privateKeyAlias, uid));
    }

    private static AndroidKeyStorePrivateKey loadAndroidKeyStorePrivateKeyFromKeystore(KeyStore keyStore, String privateKeyAlias, int uid, KeyCharacteristics keyCharacteristics) throws UnrecoverableKeyException {
        KeyPair keyPair = loadAndroidKeyStoreKeyPairFromKeystore(keyStore, privateKeyAlias, uid, keyCharacteristics);
        return (AndroidKeyStorePrivateKey) keyPair.getPrivate();
    }

    public static AndroidKeyStorePrivateKey loadAndroidKeyStorePrivateKeyFromKeystore(KeyStore keyStore, String privateKeyAlias, int uid) throws UnrecoverableKeyException, KeyPermanentlyInvalidatedException {
        return loadAndroidKeyStorePrivateKeyFromKeystore(keyStore, privateKeyAlias, uid, getKeyCharacteristics(keyStore, privateKeyAlias, uid));
    }

    private static AndroidKeyStoreSecretKey loadAndroidKeyStoreSecretKeyFromKeystore(String secretKeyAlias, int uid, KeyCharacteristics keyCharacteristics) throws UnrecoverableKeyException {
        int keymasterDigest;
        Integer keymasterAlgorithm = keyCharacteristics.getEnum(KeymasterDefs.KM_TAG_ALGORITHM);
        if (keymasterAlgorithm == null) {
            throw new UnrecoverableKeyException("Key algorithm unknown");
        }
        List<Integer> keymasterDigests = keyCharacteristics.getEnums(KeymasterDefs.KM_TAG_DIGEST);
        if (keymasterDigests.isEmpty()) {
            keymasterDigest = -1;
        } else {
            keymasterDigest = keymasterDigests.get(0).intValue();
        }
        try {
            String keyAlgorithmString = KeyProperties.KeyAlgorithm.fromKeymasterSecretKeyAlgorithm(keymasterAlgorithm.intValue(), keymasterDigest);
            return new AndroidKeyStoreSecretKey(secretKeyAlias, uid, keyAlgorithmString);
        } catch (IllegalArgumentException e) {
            throw ((UnrecoverableKeyException) new UnrecoverableKeyException("Unsupported secret key type").initCause(e));
        }
    }

    public static AndroidKeyStoreKey loadAndroidKeyStoreKeyFromKeystore(KeyStore keyStore, String userKeyAlias, int uid) throws UnrecoverableKeyException, KeyPermanentlyInvalidatedException {
        KeyCharacteristics keyCharacteristics = getKeyCharacteristics(keyStore, userKeyAlias, uid);
        Integer keymasterAlgorithm = keyCharacteristics.getEnum(KeymasterDefs.KM_TAG_ALGORITHM);
        if (keymasterAlgorithm == null) {
            throw new UnrecoverableKeyException("Key algorithm unknown");
        }
        if (keymasterAlgorithm.intValue() == 128 || keymasterAlgorithm.intValue() == 32 || keymasterAlgorithm.intValue() == 33) {
            return loadAndroidKeyStoreSecretKeyFromKeystore(userKeyAlias, uid, keyCharacteristics);
        }
        if (keymasterAlgorithm.intValue() == 1 || keymasterAlgorithm.intValue() == 3) {
            return loadAndroidKeyStorePrivateKeyFromKeystore(keyStore, userKeyAlias, uid, keyCharacteristics);
        }
        throw new UnrecoverableKeyException("Key algorithm unknown");
    }

    public static java.security.KeyStore getKeyStoreForUid(int uid) throws KeyStoreException, NoSuchProviderException {
        java.security.KeyStore result = java.security.KeyStore.getInstance("AndroidKeyStore", PROVIDER_NAME);
        try {
            result.load(new AndroidKeyStoreLoadStoreParameter(uid));
            return result;
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new KeyStoreException("Failed to load AndroidKeyStore KeyStore for UID " + uid, e);
        }
    }
}
