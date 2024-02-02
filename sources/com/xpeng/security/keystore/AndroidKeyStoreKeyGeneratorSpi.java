package com.xpeng.security.keystore;

import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import com.xpeng.security.Credentials;
import com.xpeng.security.KeyStore;
import java.security.InvalidAlgorithmParameterException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import libcore.util.EmptyArray;
/* loaded from: classes3.dex */
public abstract class AndroidKeyStoreKeyGeneratorSpi extends KeyGeneratorSpi {
    private final int mDefaultKeySizeBits;
    protected int mKeySizeBits;
    private final KeyStore mKeyStore;
    private final int mKeymasterAlgorithm;
    private int[] mKeymasterBlockModes;
    private final int mKeymasterDigest;
    private int[] mKeymasterDigests;
    private int[] mKeymasterPaddings;
    private int[] mKeymasterPurposes;
    private SecureRandom mRng;
    private KeyGenParameterSpec mSpec;

    /* loaded from: classes3.dex */
    public static class AES extends AndroidKeyStoreKeyGeneratorSpi {
        public AES() {
            super(32, 128);
        }

        @Override // com.xpeng.security.keystore.AndroidKeyStoreKeyGeneratorSpi, javax.crypto.KeyGeneratorSpi
        protected void engineInit(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
            super.engineInit(params, random);
            if (this.mKeySizeBits != 128 && this.mKeySizeBits != 192 && this.mKeySizeBits != 256) {
                throw new InvalidAlgorithmParameterException("Unsupported key size: " + this.mKeySizeBits + ". Supported: 128, 192, 256.");
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class DESede extends AndroidKeyStoreKeyGeneratorSpi {
        public DESede() {
            super(33, 168);
        }
    }

    /* loaded from: classes3.dex */
    protected static abstract class HmacBase extends AndroidKeyStoreKeyGeneratorSpi {
        protected HmacBase(int keymasterDigest) {
            super(128, keymasterDigest, KeymasterUtils.getDigestOutputSizeBits(keymasterDigest));
        }
    }

    /* loaded from: classes3.dex */
    public static class HmacSHA1 extends HmacBase {
        public HmacSHA1() {
            super(2);
        }
    }

    /* loaded from: classes3.dex */
    public static class HmacSHA224 extends HmacBase {
        public HmacSHA224() {
            super(3);
        }
    }

    /* loaded from: classes3.dex */
    public static class HmacSHA256 extends HmacBase {
        public HmacSHA256() {
            super(4);
        }
    }

    /* loaded from: classes3.dex */
    public static class HmacSHA384 extends HmacBase {
        public HmacSHA384() {
            super(5);
        }
    }

    /* loaded from: classes3.dex */
    public static class HmacSHA512 extends HmacBase {
        public HmacSHA512() {
            super(6);
        }
    }

    protected AndroidKeyStoreKeyGeneratorSpi(int keymasterAlgorithm, int defaultKeySizeBits) {
        this(keymasterAlgorithm, -1, defaultKeySizeBits);
    }

    protected AndroidKeyStoreKeyGeneratorSpi(int keymasterAlgorithm, int keymasterDigest, int defaultKeySizeBits) {
        this.mKeyStore = KeyStore.getInstance();
        this.mKeymasterAlgorithm = keymasterAlgorithm;
        this.mKeymasterDigest = keymasterDigest;
        this.mDefaultKeySizeBits = defaultKeySizeBits;
        if (this.mDefaultKeySizeBits <= 0) {
            throw new IllegalArgumentException("Default key size must be positive");
        }
        if (this.mKeymasterAlgorithm == 128 && this.mKeymasterDigest == -1) {
            throw new IllegalArgumentException("Digest algorithm must be specified for HMAC key");
        }
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(SecureRandom random) {
        throw new UnsupportedOperationException("Cannot initialize without a " + KeyGenParameterSpec.class.getName() + " parameter");
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(int keySize, SecureRandom random) {
        throw new UnsupportedOperationException("Cannot initialize without a " + KeyGenParameterSpec.class.getName() + " parameter");
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
        int[] iArr;
        resetAll();
        if (params != null) {
            try {
                if (params instanceof KeyGenParameterSpec) {
                    KeyGenParameterSpec spec = (KeyGenParameterSpec) params;
                    if (spec.getKeystoreAlias() == null) {
                        throw new InvalidAlgorithmParameterException("KeyStore entry alias not provided");
                    }
                    this.mRng = random;
                    this.mSpec = spec;
                    this.mKeySizeBits = spec.getKeySize() != -1 ? spec.getKeySize() : this.mDefaultKeySizeBits;
                    if (this.mKeySizeBits <= 0) {
                        throw new InvalidAlgorithmParameterException("Key size must be positive: " + this.mKeySizeBits);
                    } else if (this.mKeySizeBits % 8 != 0) {
                        throw new InvalidAlgorithmParameterException("Key size must be a multiple of 8: " + this.mKeySizeBits);
                    } else {
                        try {
                            this.mKeymasterPurposes = KeyProperties.Purpose.allToKeymaster(spec.getPurposes());
                            this.mKeymasterPaddings = KeyProperties.EncryptionPadding.allToKeymaster(spec.getEncryptionPaddings());
                            if (spec.getSignaturePaddings().length > 0) {
                                throw new InvalidAlgorithmParameterException("Signature paddings not supported for symmetric key algorithms");
                            }
                            this.mKeymasterBlockModes = KeyProperties.BlockMode.allToKeymaster(spec.getBlockModes());
                            if ((spec.getPurposes() & 1) != 0 && spec.isRandomizedEncryptionRequired()) {
                                for (int keymasterBlockMode : this.mKeymasterBlockModes) {
                                    if (!KeymasterUtils.isKeymasterBlockModeIndCpaCompatibleWithSymmetricCrypto(keymasterBlockMode)) {
                                        throw new InvalidAlgorithmParameterException("Randomized encryption (IND-CPA) required but may be violated by block mode: " + KeyProperties.BlockMode.fromKeymaster(keymasterBlockMode) + ". See " + KeyGenParameterSpec.class.getName() + " documentation.");
                                    }
                                }
                            }
                            if (this.mKeymasterAlgorithm == 128) {
                                if (this.mKeySizeBits < 64) {
                                    throw new InvalidAlgorithmParameterException("HMAC key size must be at least 64 bits.");
                                }
                                this.mKeymasterDigests = new int[]{this.mKeymasterDigest};
                                if (spec.isDigestsSpecified()) {
                                    int[] keymasterDigestsFromSpec = KeyProperties.Digest.allToKeymaster(spec.getDigests());
                                    if (keymasterDigestsFromSpec.length != 1 || keymasterDigestsFromSpec[0] != this.mKeymasterDigest) {
                                        throw new InvalidAlgorithmParameterException("Unsupported digests specification: " + Arrays.asList(spec.getDigests()) + ". Only " + KeyProperties.Digest.fromKeymaster(this.mKeymasterDigest) + " supported for this HMAC key algorithm");
                                    }
                                }
                            } else if (spec.isDigestsSpecified()) {
                                this.mKeymasterDigests = KeyProperties.Digest.allToKeymaster(spec.getDigests());
                            } else {
                                this.mKeymasterDigests = EmptyArray.INT;
                            }
                            KeymasterUtils.addUserAuthArgs(new KeymasterArguments(), spec);
                            if (1 == 0) {
                                resetAll();
                                return;
                            }
                            return;
                        } catch (IllegalArgumentException | IllegalStateException e) {
                            throw new InvalidAlgorithmParameterException(e);
                        }
                    }
                }
            } catch (Throwable th) {
                if (0 == 0) {
                    resetAll();
                }
                throw th;
            }
        }
        throw new InvalidAlgorithmParameterException("Cannot initialize without a " + KeyGenParameterSpec.class.getName() + " parameter");
    }

    private void resetAll() {
        this.mSpec = null;
        this.mRng = null;
        this.mKeySizeBits = -1;
        this.mKeymasterPurposes = null;
        this.mKeymasterPaddings = null;
        this.mKeymasterBlockModes = null;
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected SecretKey engineGenerateKey() {
        KeyGenParameterSpec spec = this.mSpec;
        if (spec != null) {
            KeymasterArguments args = new KeymasterArguments();
            args.addUnsignedInt(KeymasterDefs.KM_TAG_KEY_SIZE, this.mKeySizeBits);
            args.addEnum(KeymasterDefs.KM_TAG_ALGORITHM, this.mKeymasterAlgorithm);
            args.addEnums(KeymasterDefs.KM_TAG_PURPOSE, this.mKeymasterPurposes);
            args.addEnums(KeymasterDefs.KM_TAG_BLOCK_MODE, this.mKeymasterBlockModes);
            args.addEnums(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterPaddings);
            args.addEnums(KeymasterDefs.KM_TAG_DIGEST, this.mKeymasterDigests);
            KeymasterUtils.addUserAuthArgs(args, spec);
            KeymasterUtils.addMinMacLengthAuthorizationIfNecessary(args, this.mKeymasterAlgorithm, this.mKeymasterBlockModes, this.mKeymasterDigests);
            args.addDateIfNotNull(KeymasterDefs.KM_TAG_ACTIVE_DATETIME, spec.getKeyValidityStart());
            args.addDateIfNotNull(KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, spec.getKeyValidityForOriginationEnd());
            args.addDateIfNotNull(KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, spec.getKeyValidityForConsumptionEnd());
            if ((spec.getPurposes() & 1) != 0 && !spec.isRandomizedEncryptionRequired()) {
                args.addBoolean(KeymasterDefs.KM_TAG_CALLER_NONCE);
            }
            byte[] additionalEntropy = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(this.mRng, (this.mKeySizeBits + 7) / 8);
            String keyAliasInKeystore = "USRPKEY_" + spec.getKeystoreAlias();
            KeyCharacteristics resultingKeyCharacteristics = new KeyCharacteristics();
            boolean success = false;
            try {
                Credentials.deleteAllTypesForAlias(this.mKeyStore, spec.getKeystoreAlias(), spec.getUid());
                int errorCode = this.mKeyStore.generateKey(keyAliasInKeystore, args, additionalEntropy, spec.getUid(), 0, resultingKeyCharacteristics);
                if (errorCode == 1) {
                    try {
                        String keyAlgorithmJCA = KeyProperties.KeyAlgorithm.fromKeymasterSecretKeyAlgorithm(this.mKeymasterAlgorithm, this.mKeymasterDigest);
                        SecretKey result = new AndroidKeyStoreSecretKey(keyAliasInKeystore, spec.getUid(), keyAlgorithmJCA);
                        success = true;
                        return result;
                    } catch (IllegalArgumentException e) {
                        throw new ProviderException("Failed to obtain JCA secret key algorithm name", e);
                    }
                }
                throw new ProviderException("Keystore operation failed", KeyStore.getKeyStoreException(errorCode));
            } finally {
                if (!success) {
                    Credentials.deleteAllTypesForAlias(this.mKeyStore, spec.getKeystoreAlias(), spec.getUid());
                }
            }
        }
        throw new IllegalStateException("Not initialized");
    }
}
