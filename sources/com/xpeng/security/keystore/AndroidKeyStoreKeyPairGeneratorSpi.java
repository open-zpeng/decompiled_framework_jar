package com.xpeng.security.keystore;

import android.security.KeyPairGeneratorSpec;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keymaster.KeymasterDefs;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.SecureKeyImportUnavailableException;
import android.security.keystore.StrongBoxUnavailableException;
import com.android.internal.logging.nano.MetricsProto;
import com.android.org.bouncycastle.asn1.ASN1EncodableVector;
import com.android.org.bouncycastle.asn1.ASN1InputStream;
import com.android.org.bouncycastle.asn1.ASN1Integer;
import com.android.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.android.org.bouncycastle.asn1.DERBitString;
import com.android.org.bouncycastle.asn1.DERInteger;
import com.android.org.bouncycastle.asn1.DERNull;
import com.android.org.bouncycastle.asn1.DERSequence;
import com.android.org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import com.android.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.android.org.bouncycastle.asn1.x509.Certificate;
import com.android.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import com.android.org.bouncycastle.asn1.x509.TBSCertificate;
import com.android.org.bouncycastle.asn1.x509.Time;
import com.android.org.bouncycastle.asn1.x509.V3TBSCertificateGenerator;
import com.android.org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import com.android.org.bouncycastle.jce.X509Principal;
import com.android.org.bouncycastle.jce.provider.X509CertificateObject;
import com.android.org.bouncycastle.x509.X509V3CertificateGenerator;
import com.xpeng.security.Credentials;
import com.xpeng.security.KeyStore;
import com.xpeng.security.keystore.KeyProperties;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import libcore.util.EmptyArray;
/* loaded from: classes3.dex */
public abstract class AndroidKeyStoreKeyPairGeneratorSpi extends KeyPairGeneratorSpi {
    private static final int EC_DEFAULT_KEY_SIZE = 256;
    private static final int RSA_DEFAULT_KEY_SIZE = 2048;
    private static final int RSA_MAX_KEY_SIZE = 8192;
    private static final int RSA_MIN_KEY_SIZE = 512;
    private boolean mEncryptionAtRestRequired;
    private String mEntryAlias;
    private int mEntryUid;
    private String mJcaKeyAlgorithm;
    private int mKeySizeBits;
    private KeyStore mKeyStore;
    private int mKeymasterAlgorithm = -1;
    private int[] mKeymasterBlockModes;
    private int[] mKeymasterDigests;
    private int[] mKeymasterEncryptionPaddings;
    private int[] mKeymasterPurposes;
    private int[] mKeymasterSignaturePaddings;
    private final int mOriginalKeymasterAlgorithm;
    private BigInteger mRSAPublicExponent;
    private SecureRandom mRng;
    private KeyGenParameterSpec mSpec;
    private static final Map<String, Integer> SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE = new HashMap();
    private static final List<String> SUPPORTED_EC_NIST_CURVE_NAMES = new ArrayList();
    private static final List<Integer> SUPPORTED_EC_NIST_CURVE_SIZES = new ArrayList();

    /* loaded from: classes3.dex */
    public static class RSA extends AndroidKeyStoreKeyPairGeneratorSpi {
        public RSA() {
            super(1);
        }
    }

    /* loaded from: classes3.dex */
    public static class EC extends AndroidKeyStoreKeyPairGeneratorSpi {
        public EC() {
            super(3);
        }
    }

    static {
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-224", 224);
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp224r1", 224);
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-256", 256);
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp256r1", 256);
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("prime256v1", 256);
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-384", Integer.valueOf((int) MetricsProto.MetricsEvent.ACTION_SHOW_SETTINGS_SUGGESTION));
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp384r1", Integer.valueOf((int) MetricsProto.MetricsEvent.ACTION_SHOW_SETTINGS_SUGGESTION));
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-521", 521);
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp521r1", 521);
        SUPPORTED_EC_NIST_CURVE_NAMES.addAll(SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.keySet());
        Collections.sort(SUPPORTED_EC_NIST_CURVE_NAMES);
        SUPPORTED_EC_NIST_CURVE_SIZES.addAll(new HashSet(SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.values()));
        Collections.sort(SUPPORTED_EC_NIST_CURVE_SIZES);
    }

    protected AndroidKeyStoreKeyPairGeneratorSpi(int keymasterAlgorithm) {
        this.mOriginalKeymasterAlgorithm = keymasterAlgorithm;
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(int keysize, SecureRandom random) {
        throw new IllegalArgumentException(KeyGenParameterSpec.class.getName() + " or " + KeyPairGeneratorSpec.class.getName() + " required to initialize this KeyPairGenerator");
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
        KeyGenParameterSpec.Builder specBuilder;
        KeyGenParameterSpec spec;
        int[] iArr;
        resetAll();
        try {
            if (params == null) {
                throw new InvalidAlgorithmParameterException("Must supply params of type " + KeyGenParameterSpec.class.getName() + " or " + KeyPairGeneratorSpec.class.getName());
            }
            boolean encryptionAtRestRequired = false;
            try {
                int keymasterAlgorithm = this.mOriginalKeymasterAlgorithm;
                if (params instanceof KeyGenParameterSpec) {
                    spec = (KeyGenParameterSpec) params;
                } else if (!(params instanceof KeyPairGeneratorSpec)) {
                    throw new InvalidAlgorithmParameterException("Unsupported params class: " + params.getClass().getName() + ". Supported: " + KeyGenParameterSpec.class.getName() + ", " + KeyPairGeneratorSpec.class.getName());
                } else {
                    KeyPairGeneratorSpec legacySpec = (KeyPairGeneratorSpec) params;
                    try {
                        String specKeyAlgorithm = legacySpec.getKeyType();
                        if (specKeyAlgorithm != null) {
                            try {
                                keymasterAlgorithm = KeyProperties.KeyAlgorithm.toKeymasterAsymmetricKeyAlgorithm(specKeyAlgorithm);
                            } catch (IllegalArgumentException e) {
                                throw new InvalidAlgorithmParameterException("Invalid key type in parameters", e);
                            }
                        }
                        if (keymasterAlgorithm == 1) {
                            specBuilder = new KeyGenParameterSpec.Builder(legacySpec.getKeystoreAlias(), 15);
                            specBuilder.setDigests("NONE", "MD5", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512");
                            specBuilder.setEncryptionPaddings("NoPadding", "PKCS1Padding", "OAEPPadding");
                            specBuilder.setSignaturePaddings("PKCS1", "PSS");
                            specBuilder.setRandomizedEncryptionRequired(false);
                        } else if (keymasterAlgorithm == 3) {
                            specBuilder = new KeyGenParameterSpec.Builder(legacySpec.getKeystoreAlias(), 12);
                            specBuilder.setDigests("NONE", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512");
                        } else {
                            throw new ProviderException("Unsupported algorithm: " + this.mKeymasterAlgorithm);
                        }
                        if (legacySpec.getKeySize() != -1) {
                            specBuilder.setKeySize(legacySpec.getKeySize());
                        }
                        if (legacySpec.getAlgorithmParameterSpec() != null) {
                            specBuilder.setAlgorithmParameterSpec(legacySpec.getAlgorithmParameterSpec());
                        }
                        specBuilder.setCertificateSubject(legacySpec.getSubjectDN());
                        specBuilder.setCertificateSerialNumber(legacySpec.getSerialNumber());
                        specBuilder.setCertificateNotBefore(legacySpec.getStartDate());
                        specBuilder.setCertificateNotAfter(legacySpec.getEndDate());
                        encryptionAtRestRequired = legacySpec.isEncryptionRequired();
                        specBuilder.setUserAuthenticationRequired(false);
                        KeyGenParameterSpec spec2 = specBuilder.build();
                        spec = spec2;
                    } catch (IllegalArgumentException | NullPointerException e2) {
                        throw new InvalidAlgorithmParameterException(e2);
                    }
                }
                this.mEntryAlias = spec.getKeystoreAlias();
                this.mEntryUid = spec.getUid();
                this.mSpec = spec;
                this.mKeymasterAlgorithm = keymasterAlgorithm;
                this.mEncryptionAtRestRequired = encryptionAtRestRequired;
                this.mKeySizeBits = spec.getKeySize();
                initAlgorithmSpecificParameters();
                if (this.mKeySizeBits == -1) {
                    this.mKeySizeBits = getDefaultKeySize(keymasterAlgorithm);
                }
                checkValidKeySize(keymasterAlgorithm, this.mKeySizeBits);
                if (spec.getKeystoreAlias() == null) {
                    throw new InvalidAlgorithmParameterException("KeyStore entry alias not provided");
                }
                try {
                    String jcaKeyAlgorithm = KeyProperties.KeyAlgorithm.fromKeymasterAsymmetricKeyAlgorithm(keymasterAlgorithm);
                    this.mKeymasterPurposes = KeyProperties.Purpose.allToKeymaster(spec.getPurposes());
                    this.mKeymasterBlockModes = KeyProperties.BlockMode.allToKeymaster(spec.getBlockModes());
                    this.mKeymasterEncryptionPaddings = KeyProperties.EncryptionPadding.allToKeymaster(spec.getEncryptionPaddings());
                    if ((1 & spec.getPurposes()) != 0 && spec.isRandomizedEncryptionRequired()) {
                        for (int keymasterPadding : this.mKeymasterEncryptionPaddings) {
                            if (!KeymasterUtils.isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto(keymasterPadding)) {
                                throw new InvalidAlgorithmParameterException("Randomized encryption (IND-CPA) required but may be violated by padding scheme: " + KeyProperties.EncryptionPadding.fromKeymaster(keymasterPadding) + ". See " + KeyGenParameterSpec.class.getName() + " documentation.");
                            }
                        }
                    }
                    this.mKeymasterSignaturePaddings = KeyProperties.SignaturePadding.allToKeymaster(spec.getSignaturePaddings());
                    if (spec.isDigestsSpecified()) {
                        this.mKeymasterDigests = KeyProperties.Digest.allToKeymaster(spec.getDigests());
                    } else {
                        this.mKeymasterDigests = EmptyArray.INT;
                    }
                    KeymasterUtils.addUserAuthArgs(new KeymasterArguments(), this.mSpec);
                    this.mJcaKeyAlgorithm = jcaKeyAlgorithm;
                    this.mRng = random;
                    this.mKeyStore = KeyStore.getInstance();
                    if (1 == 0) {
                        resetAll();
                    }
                } catch (IllegalArgumentException | IllegalStateException e3) {
                    throw new InvalidAlgorithmParameterException(e3);
                }
            } catch (Throwable th) {
                th = th;
                if (0 == 0) {
                    resetAll();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void resetAll() {
        this.mEntryAlias = null;
        this.mEntryUid = -1;
        this.mJcaKeyAlgorithm = null;
        this.mKeymasterAlgorithm = -1;
        this.mKeymasterPurposes = null;
        this.mKeymasterBlockModes = null;
        this.mKeymasterEncryptionPaddings = null;
        this.mKeymasterSignaturePaddings = null;
        this.mKeymasterDigests = null;
        this.mKeySizeBits = 0;
        this.mSpec = null;
        this.mRSAPublicExponent = null;
        this.mEncryptionAtRestRequired = false;
        this.mRng = null;
        this.mKeyStore = null;
    }

    private void initAlgorithmSpecificParameters() throws InvalidAlgorithmParameterException {
        AlgorithmParameterSpec algSpecificSpec = this.mSpec.getAlgorithmParameterSpec();
        int i = this.mKeymasterAlgorithm;
        if (i != 1) {
            if (i == 3) {
                if (algSpecificSpec instanceof ECGenParameterSpec) {
                    ECGenParameterSpec ecSpec = (ECGenParameterSpec) algSpecificSpec;
                    String curveName = ecSpec.getName();
                    Integer ecSpecKeySizeBits = SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.get(curveName.toLowerCase(Locale.US));
                    if (ecSpecKeySizeBits == null) {
                        throw new InvalidAlgorithmParameterException("Unsupported EC curve name: " + curveName + ". Supported: " + SUPPORTED_EC_NIST_CURVE_NAMES);
                    } else if (this.mKeySizeBits == -1) {
                        this.mKeySizeBits = ecSpecKeySizeBits.intValue();
                        return;
                    } else if (this.mKeySizeBits != ecSpecKeySizeBits.intValue()) {
                        throw new InvalidAlgorithmParameterException("EC key size must match  between " + this.mSpec + " and " + algSpecificSpec + ": " + this.mKeySizeBits + " vs " + ecSpecKeySizeBits);
                    } else {
                        return;
                    }
                } else if (algSpecificSpec != null) {
                    throw new InvalidAlgorithmParameterException("EC may only use ECGenParameterSpec");
                } else {
                    return;
                }
            }
            throw new ProviderException("Unsupported algorithm: " + this.mKeymasterAlgorithm);
        }
        BigInteger publicExponent = null;
        if (algSpecificSpec instanceof RSAKeyGenParameterSpec) {
            RSAKeyGenParameterSpec rsaSpec = (RSAKeyGenParameterSpec) algSpecificSpec;
            if (this.mKeySizeBits == -1) {
                this.mKeySizeBits = rsaSpec.getKeysize();
            } else if (this.mKeySizeBits != rsaSpec.getKeysize()) {
                throw new InvalidAlgorithmParameterException("RSA key size must match  between " + this.mSpec + " and " + algSpecificSpec + ": " + this.mKeySizeBits + " vs " + rsaSpec.getKeysize());
            }
            publicExponent = rsaSpec.getPublicExponent();
        } else if (algSpecificSpec != null) {
            throw new InvalidAlgorithmParameterException("RSA may only use RSAKeyGenParameterSpec");
        }
        if (publicExponent == null) {
            publicExponent = RSAKeyGenParameterSpec.F4;
        }
        if (publicExponent.compareTo(BigInteger.ZERO) < 1) {
            throw new InvalidAlgorithmParameterException("RSA public exponent must be positive: " + publicExponent);
        } else if (publicExponent.compareTo(KeymasterArguments.UINT64_MAX_VALUE) > 0) {
            throw new InvalidAlgorithmParameterException("Unsupported RSA public exponent: " + publicExponent + ". Maximum supported value: " + KeymasterArguments.UINT64_MAX_VALUE);
        } else {
            this.mRSAPublicExponent = publicExponent;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.security.KeyPairGeneratorSpi
    public KeyPair generateKeyPair() {
        if (this.mKeyStore == null || this.mSpec == null) {
            throw new IllegalStateException("Not initialized");
        }
        boolean z = this.mEncryptionAtRestRequired;
        if (((z ? 1 : 0) & 1) != 0 && this.mKeyStore.state() != KeyStore.State.UNLOCKED) {
            throw new IllegalStateException("Encryption at rest using secure lock screen credential requested for key pair, but the user has not yet entered the credential");
        }
        int flags = z;
        if (this.mSpec.isStrongBoxBacked()) {
            flags = (z ? 1 : 0) | 16;
        }
        byte[] additionalEntropy = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(this.mRng, (this.mKeySizeBits + 7) / 8);
        Credentials.deleteAllTypesForAlias(this.mKeyStore, this.mEntryAlias, this.mEntryUid);
        String privateKeyAlias = "USRPKEY_" + this.mEntryAlias;
        boolean success = false;
        try {
            try {
                generateKeystoreKeyPair(privateKeyAlias, constructKeyGenerationArguments(), additionalEntropy, flags);
                KeyPair keyPair = loadKeystoreKeyPair(privateKeyAlias);
                storeCertificateChain(flags, createCertificateChain(privateKeyAlias, keyPair));
                boolean success2 = true;
                return keyPair;
            } catch (ProviderException e) {
                if ((this.mSpec.getPurposes() & 32) != 0) {
                    throw new SecureKeyImportUnavailableException(e);
                }
                throw e;
            }
        } finally {
            if (!success) {
                Credentials.deleteAllTypesForAlias(this.mKeyStore, this.mEntryAlias, this.mEntryUid);
            }
        }
    }

    private Iterable<byte[]> createCertificateChain(String privateKeyAlias, KeyPair keyPair) throws ProviderException {
        byte[] challenge = this.mSpec.getAttestationChallenge();
        if (challenge != null) {
            KeymasterArguments args = new KeymasterArguments();
            args.addBytes(KeymasterDefs.KM_TAG_ATTESTATION_CHALLENGE, challenge);
            return getAttestationChain(privateKeyAlias, keyPair, args);
        }
        return Collections.singleton(generateSelfSignedCertificateBytes(keyPair));
    }

    private void generateKeystoreKeyPair(String privateKeyAlias, KeymasterArguments args, byte[] additionalEntropy, int flags) throws ProviderException {
        KeyCharacteristics resultingKeyCharacteristics = new KeyCharacteristics();
        int errorCode = this.mKeyStore.generateKey(privateKeyAlias, args, additionalEntropy, this.mEntryUid, flags, resultingKeyCharacteristics);
        if (errorCode != 1) {
            if (errorCode == -68) {
                throw new StrongBoxUnavailableException("Failed to generate key pair");
            }
            throw new ProviderException("Failed to generate key pair", KeyStore.getKeyStoreException(errorCode));
        }
    }

    private KeyPair loadKeystoreKeyPair(String privateKeyAlias) throws ProviderException {
        try {
            KeyPair result = XpengAndroidKeyStoreProvider.loadAndroidKeyStoreKeyPairFromKeystore(this.mKeyStore, privateKeyAlias, this.mEntryUid);
            if (!this.mJcaKeyAlgorithm.equalsIgnoreCase(result.getPrivate().getAlgorithm())) {
                throw new ProviderException("Generated key pair algorithm does not match requested algorithm: " + result.getPrivate().getAlgorithm() + " vs " + this.mJcaKeyAlgorithm);
            }
            return result;
        } catch (UnrecoverableKeyException e) {
            throw new ProviderException("Failed to load generated key pair from keystore", e);
        }
    }

    private KeymasterArguments constructKeyGenerationArguments() {
        KeymasterArguments args = new KeymasterArguments();
        args.addUnsignedInt(KeymasterDefs.KM_TAG_KEY_SIZE, this.mKeySizeBits);
        args.addEnum(KeymasterDefs.KM_TAG_ALGORITHM, this.mKeymasterAlgorithm);
        args.addEnums(KeymasterDefs.KM_TAG_PURPOSE, this.mKeymasterPurposes);
        args.addEnums(KeymasterDefs.KM_TAG_BLOCK_MODE, this.mKeymasterBlockModes);
        args.addEnums(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterEncryptionPaddings);
        args.addEnums(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterSignaturePaddings);
        args.addEnums(KeymasterDefs.KM_TAG_DIGEST, this.mKeymasterDigests);
        KeymasterUtils.addUserAuthArgs(args, this.mSpec);
        args.addDateIfNotNull(KeymasterDefs.KM_TAG_ACTIVE_DATETIME, this.mSpec.getKeyValidityStart());
        args.addDateIfNotNull(KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, this.mSpec.getKeyValidityForOriginationEnd());
        args.addDateIfNotNull(KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, this.mSpec.getKeyValidityForConsumptionEnd());
        addAlgorithmSpecificParameters(args);
        if (this.mSpec.isUniqueIdIncluded()) {
            args.addBoolean(KeymasterDefs.KM_TAG_INCLUDE_UNIQUE_ID);
        }
        return args;
    }

    private void storeCertificateChain(int flags, Iterable<byte[]> iterable) throws ProviderException {
        Iterator<byte[]> iter = iterable.iterator();
        storeCertificate("USRCERT_", iter.next(), flags, "Failed to store certificate");
        if (!iter.hasNext()) {
            return;
        }
        ByteArrayOutputStream certificateConcatenationStream = new ByteArrayOutputStream();
        while (iter.hasNext()) {
            byte[] data = iter.next();
            certificateConcatenationStream.write(data, 0, data.length);
        }
        storeCertificate("CACERT_", certificateConcatenationStream.toByteArray(), flags, "Failed to store attestation CA certificate");
    }

    private void storeCertificate(String prefix, byte[] certificateBytes, int flags, String failureMessage) throws ProviderException {
        KeyStore keyStore = this.mKeyStore;
        int insertErrorCode = keyStore.insert(prefix + this.mEntryAlias, certificateBytes, this.mEntryUid, flags);
        if (insertErrorCode != 1) {
            throw new ProviderException(failureMessage, KeyStore.getKeyStoreException(insertErrorCode));
        }
    }

    private byte[] generateSelfSignedCertificateBytes(KeyPair keyPair) throws ProviderException {
        try {
            return generateSelfSignedCertificate(keyPair.getPrivate(), keyPair.getPublic()).getEncoded();
        } catch (IOException | CertificateParsingException e) {
            throw new ProviderException("Failed to generate self-signed certificate", e);
        } catch (CertificateEncodingException e2) {
            throw new ProviderException("Failed to obtain encoded form of self-signed certificate", e2);
        }
    }

    private Iterable<byte[]> getAttestationChain(String privateKeyAlias, KeyPair keyPair, KeymasterArguments args) throws ProviderException {
        KeymasterCertificateChain outChain = new KeymasterCertificateChain();
        int errorCode = this.mKeyStore.attestKey(privateKeyAlias, args, outChain);
        if (errorCode != 1) {
            throw new ProviderException("Failed to generate attestation certificate chain", KeyStore.getKeyStoreException(errorCode));
        }
        Collection<byte[]> chain = outChain.getCertificates();
        if (chain.size() < 2) {
            throw new ProviderException("Attestation certificate chain contained " + chain.size() + " entries. At least two are required.");
        }
        return chain;
    }

    private void addAlgorithmSpecificParameters(KeymasterArguments keymasterArgs) {
        int i = this.mKeymasterAlgorithm;
        if (i == 1) {
            keymasterArgs.addUnsignedLong(KeymasterDefs.KM_TAG_RSA_PUBLIC_EXPONENT, this.mRSAPublicExponent);
        } else if (i != 3) {
            throw new ProviderException("Unsupported algorithm: " + this.mKeymasterAlgorithm);
        }
    }

    private X509Certificate generateSelfSignedCertificate(PrivateKey privateKey, PublicKey publicKey) throws CertificateParsingException, IOException {
        String signatureAlgorithm = getCertificateSignatureAlgorithm(this.mKeymasterAlgorithm, this.mKeySizeBits, this.mSpec);
        if (signatureAlgorithm == null) {
            return generateSelfSignedCertificateWithFakeSignature(publicKey);
        }
        try {
            return generateSelfSignedCertificateWithValidSignature(privateKey, publicKey, signatureAlgorithm);
        } catch (Exception e) {
            return generateSelfSignedCertificateWithFakeSignature(publicKey);
        }
    }

    private X509Certificate generateSelfSignedCertificateWithValidSignature(PrivateKey privateKey, PublicKey publicKey, String signatureAlgorithm) throws Exception {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        certGen.setPublicKey(publicKey);
        certGen.setSerialNumber(this.mSpec.getCertificateSerialNumber());
        certGen.setSubjectDN(this.mSpec.getCertificateSubject());
        certGen.setIssuerDN(this.mSpec.getCertificateSubject());
        certGen.setNotBefore(this.mSpec.getCertificateNotBefore());
        certGen.setNotAfter(this.mSpec.getCertificateNotAfter());
        certGen.setSignatureAlgorithm(signatureAlgorithm);
        return certGen.generate(privateKey);
    }

    private X509Certificate generateSelfSignedCertificateWithFakeSignature(PublicKey publicKey) throws IOException, CertificateParsingException {
        byte[] signature;
        AlgorithmIdentifier sigAlgId;
        V3TBSCertificateGenerator tbsGenerator = new V3TBSCertificateGenerator();
        int i = this.mKeymasterAlgorithm;
        if (i == 1) {
            ASN1ObjectIdentifier sigAlgOid = PKCSObjectIdentifiers.sha256WithRSAEncryption;
            AlgorithmIdentifier sigAlgId2 = new AlgorithmIdentifier(sigAlgOid, DERNull.INSTANCE);
            signature = new byte[1];
            sigAlgId = sigAlgId2;
        } else if (i == 3) {
            ASN1ObjectIdentifier sigAlgOid2 = X9ObjectIdentifiers.ecdsa_with_SHA256;
            sigAlgId = new AlgorithmIdentifier(sigAlgOid2);
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new DERInteger(0L));
            v.add(new DERInteger(0L));
            signature = new DERSequence().getEncoded();
        } else {
            throw new ProviderException("Unsupported key algorithm: " + this.mKeymasterAlgorithm);
        }
        byte[] signature2 = signature;
        ASN1InputStream publicKeyInfoIn = new ASN1InputStream(publicKey.getEncoded());
        try {
            tbsGenerator.setSubjectPublicKeyInfo(SubjectPublicKeyInfo.getInstance(publicKeyInfoIn.readObject()));
            publicKeyInfoIn.close();
            tbsGenerator.setSerialNumber(new ASN1Integer(this.mSpec.getCertificateSerialNumber()));
            X509Principal subject = new X509Principal(this.mSpec.getCertificateSubject().getEncoded());
            tbsGenerator.setSubject(subject);
            tbsGenerator.setIssuer(subject);
            tbsGenerator.setStartDate(new Time(this.mSpec.getCertificateNotBefore()));
            tbsGenerator.setEndDate(new Time(this.mSpec.getCertificateNotAfter()));
            tbsGenerator.setSignature(sigAlgId);
            TBSCertificate tbsCertificate = tbsGenerator.generateTBSCertificate();
            ASN1EncodableVector result = new ASN1EncodableVector();
            result.add(tbsCertificate);
            result.add(sigAlgId);
            result.add(new DERBitString(signature2));
            return new X509CertificateObject(Certificate.getInstance(new DERSequence(result)));
        } finally {
        }
    }

    private static int getDefaultKeySize(int keymasterAlgorithm) {
        if (keymasterAlgorithm != 1) {
            if (keymasterAlgorithm == 3) {
                return 256;
            }
            throw new ProviderException("Unsupported algorithm: " + keymasterAlgorithm);
        }
        return 2048;
    }

    private static void checkValidKeySize(int keymasterAlgorithm, int keySize) throws InvalidAlgorithmParameterException {
        if (keymasterAlgorithm == 1) {
            if (keySize < 512 || keySize > 8192) {
                throw new InvalidAlgorithmParameterException("RSA key size must be >= 512 and <= 8192");
            }
        } else if (keymasterAlgorithm == 3) {
            if (!SUPPORTED_EC_NIST_CURVE_SIZES.contains(Integer.valueOf(keySize))) {
                throw new InvalidAlgorithmParameterException("Unsupported EC key size: " + keySize + " bits. Supported: " + SUPPORTED_EC_NIST_CURVE_SIZES);
            }
        } else {
            throw new ProviderException("Unsupported algorithm: " + keymasterAlgorithm);
        }
    }

    private static String getCertificateSignatureAlgorithm(int keymasterAlgorithm, int keySizeBits, KeyGenParameterSpec spec) {
        if ((spec.getPurposes() & 4) == 0 || spec.isUserAuthenticationRequired() || !spec.isDigestsSpecified()) {
            return null;
        }
        if (keymasterAlgorithm == 1) {
            boolean pkcs1SignaturePaddingSupported = com.android.internal.util.ArrayUtils.contains(KeyProperties.SignaturePadding.allToKeymaster(spec.getSignaturePaddings()), 5);
            if (pkcs1SignaturePaddingSupported) {
                Set<Integer> availableKeymasterDigests = getAvailableKeymasterSignatureDigests(spec.getDigests(), XpengAndroidKeyStoreBCWorkaroundProvider.getSupportedEcdsaSignatureDigests());
                int maxDigestOutputSizeBits = keySizeBits - 240;
                int bestKeymasterDigest = -1;
                int bestDigestOutputSizeBits = -1;
                for (Integer num : availableKeymasterDigests) {
                    int keymasterDigest = num.intValue();
                    int outputSizeBits = KeymasterUtils.getDigestOutputSizeBits(keymasterDigest);
                    if (outputSizeBits <= maxDigestOutputSizeBits) {
                        if (bestKeymasterDigest == -1) {
                            bestKeymasterDigest = keymasterDigest;
                            bestDigestOutputSizeBits = outputSizeBits;
                        } else if (outputSizeBits > bestDigestOutputSizeBits) {
                            bestKeymasterDigest = keymasterDigest;
                            bestDigestOutputSizeBits = outputSizeBits;
                        }
                    }
                }
                if (bestKeymasterDigest == -1) {
                    return null;
                }
                return KeyProperties.Digest.fromKeymasterToSignatureAlgorithmDigest(bestKeymasterDigest) + "WithRSA";
            }
            return null;
        } else if (keymasterAlgorithm == 3) {
            Set<Integer> availableKeymasterDigests2 = getAvailableKeymasterSignatureDigests(spec.getDigests(), XpengAndroidKeyStoreBCWorkaroundProvider.getSupportedEcdsaSignatureDigests());
            int bestKeymasterDigest2 = -1;
            int bestDigestOutputSizeBits2 = -1;
            Iterator<Integer> it = availableKeymasterDigests2.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                int keymasterDigest2 = it.next().intValue();
                int outputSizeBits2 = KeymasterUtils.getDigestOutputSizeBits(keymasterDigest2);
                if (outputSizeBits2 == keySizeBits) {
                    bestKeymasterDigest2 = keymasterDigest2;
                    break;
                } else if (bestKeymasterDigest2 == -1) {
                    bestKeymasterDigest2 = keymasterDigest2;
                    bestDigestOutputSizeBits2 = outputSizeBits2;
                } else if (bestDigestOutputSizeBits2 < keySizeBits) {
                    if (outputSizeBits2 > bestDigestOutputSizeBits2) {
                        bestKeymasterDigest2 = keymasterDigest2;
                        bestDigestOutputSizeBits2 = outputSizeBits2;
                    }
                } else if (outputSizeBits2 < bestDigestOutputSizeBits2 && outputSizeBits2 >= keySizeBits) {
                    bestKeymasterDigest2 = keymasterDigest2;
                    bestDigestOutputSizeBits2 = outputSizeBits2;
                }
            }
            if (bestKeymasterDigest2 == -1) {
                return null;
            }
            return KeyProperties.Digest.fromKeymasterToSignatureAlgorithmDigest(bestKeymasterDigest2) + "WithECDSA";
        } else {
            throw new ProviderException("Unsupported algorithm: " + keymasterAlgorithm);
        }
    }

    private static Set<Integer> getAvailableKeymasterSignatureDigests(String[] authorizedKeyDigests, String[] supportedSignatureDigests) {
        int[] allToKeymaster;
        int[] allToKeymaster2;
        Set<Integer> authorizedKeymasterKeyDigests = new HashSet<>();
        for (int keymasterDigest : KeyProperties.Digest.allToKeymaster(authorizedKeyDigests)) {
            authorizedKeymasterKeyDigests.add(Integer.valueOf(keymasterDigest));
        }
        Set<Integer> supportedKeymasterSignatureDigests = new HashSet<>();
        for (int keymasterDigest2 : KeyProperties.Digest.allToKeymaster(supportedSignatureDigests)) {
            supportedKeymasterSignatureDigests.add(Integer.valueOf(keymasterDigest2));
        }
        Set<Integer> result = new HashSet<>(supportedKeymasterSignatureDigests);
        result.retainAll(authorizedKeymasterKeyDigests);
        return result;
    }
}
