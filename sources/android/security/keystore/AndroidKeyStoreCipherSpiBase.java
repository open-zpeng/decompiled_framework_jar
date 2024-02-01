package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.KeyStoreException;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.OperationResult;
import android.security.keystore.KeyStoreCryptoOperationChunkedStreamer;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import libcore.util.EmptyArray;

/* loaded from: classes2.dex */
abstract class AndroidKeyStoreCipherSpiBase extends CipherSpi implements KeyStoreCryptoOperation {
    private KeyStoreCryptoOperationStreamer mAdditionalAuthenticationDataStreamer;
    private boolean mAdditionalAuthenticationDataStreamerClosed;
    private Exception mCachedException;
    private boolean mEncrypting;
    private AndroidKeyStoreKey mKey;
    private KeyStoreCryptoOperationStreamer mMainDataStreamer;
    private long mOperationHandle;
    private IBinder mOperationToken;
    private SecureRandom mRng;
    private int mKeymasterPurposeOverride = -1;
    private final KeyStore mKeyStore = KeyStore.getInstance();

    protected abstract void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArguments);

    @Override // javax.crypto.CipherSpi
    protected abstract AlgorithmParameters engineGetParameters();

    protected abstract int getAdditionalEntropyAmountForBegin();

    protected abstract int getAdditionalEntropyAmountForFinish();

    protected abstract void initAlgorithmSpecificParameters() throws InvalidKeyException;

    protected abstract void initAlgorithmSpecificParameters(AlgorithmParameters algorithmParameters) throws InvalidAlgorithmParameterException;

    protected abstract void initAlgorithmSpecificParameters(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException;

    protected abstract void initKey(int i, Key key) throws InvalidKeyException;

    protected abstract void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments keymasterArguments);

    @Override // javax.crypto.CipherSpi
    protected final void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
        resetAll();
        boolean success = false;
        try {
            init(opmode, key, random);
            initAlgorithmSpecificParameters();
            try {
                ensureKeystoreOperationInitialized();
                success = true;
            } catch (InvalidAlgorithmParameterException e) {
                throw new InvalidKeyException(e);
            }
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    @Override // javax.crypto.CipherSpi
    protected final void engineInit(int opmode, Key key, AlgorithmParameters params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        resetAll();
        boolean success = false;
        try {
            init(opmode, key, random);
            initAlgorithmSpecificParameters(params);
            ensureKeystoreOperationInitialized();
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    @Override // javax.crypto.CipherSpi
    protected final void engineInit(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        resetAll();
        boolean success = false;
        try {
            init(opmode, key, random);
            initAlgorithmSpecificParameters(params);
            ensureKeystoreOperationInitialized();
            success = true;
        } finally {
            if (!success) {
                resetAll();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0035  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void init(int r4, java.security.Key r5, java.security.SecureRandom r6) throws java.security.InvalidKeyException {
        /*
            r3 = this;
            r0 = 1
            if (r4 == r0) goto L28
            r1 = 2
            if (r4 == r1) goto L24
            r1 = 3
            if (r4 == r1) goto L28
            r0 = 4
            if (r4 != r0) goto Ld
            goto L24
        Ld:
            java.security.InvalidParameterException r0 = new java.security.InvalidParameterException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unsupported opmode: "
            r1.append(r2)
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L24:
            r0 = 0
            r3.mEncrypting = r0
            goto L2b
        L28:
            r3.mEncrypting = r0
        L2b:
            r3.initKey(r4, r5)
            android.security.keystore.AndroidKeyStoreKey r0 = r3.mKey
            if (r0 == 0) goto L35
            r3.mRng = r6
            return
        L35:
            java.security.ProviderException r0 = new java.security.ProviderException
            java.lang.String r1 = "initKey did not initialize the key"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.security.keystore.AndroidKeyStoreCipherSpiBase.init(int, java.security.Key, java.security.SecureRandom):void");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void resetAll() {
        IBinder operationToken = this.mOperationToken;
        if (operationToken != null) {
            this.mKeyStore.abort(operationToken);
        }
        this.mEncrypting = false;
        this.mKeymasterPurposeOverride = -1;
        this.mKey = null;
        this.mRng = null;
        this.mOperationToken = null;
        this.mOperationHandle = 0L;
        this.mMainDataStreamer = null;
        this.mAdditionalAuthenticationDataStreamer = null;
        this.mAdditionalAuthenticationDataStreamerClosed = false;
        this.mCachedException = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void resetWhilePreservingInitState() {
        IBinder operationToken = this.mOperationToken;
        if (operationToken != null) {
            this.mKeyStore.abort(operationToken);
        }
        this.mOperationToken = null;
        this.mOperationHandle = 0L;
        this.mMainDataStreamer = null;
        this.mAdditionalAuthenticationDataStreamer = null;
        this.mAdditionalAuthenticationDataStreamerClosed = false;
        this.mCachedException = null;
    }

    private void ensureKeystoreOperationInitialized() throws InvalidKeyException, InvalidAlgorithmParameterException {
        int purpose;
        if (this.mMainDataStreamer != null || this.mCachedException != null) {
            return;
        }
        if (this.mKey == null) {
            throw new IllegalStateException("Not initialized");
        }
        KeymasterArguments keymasterInputArgs = new KeymasterArguments();
        addAlgorithmSpecificParametersToBegin(keymasterInputArgs);
        byte[] additionalEntropy = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(this.mRng, getAdditionalEntropyAmountForBegin());
        if (this.mKeymasterPurposeOverride != -1) {
            purpose = this.mKeymasterPurposeOverride;
        } else {
            purpose = this.mEncrypting ? 0 : 1;
        }
        OperationResult opResult = this.mKeyStore.begin(this.mKey.getAlias(), purpose, true, keymasterInputArgs, additionalEntropy, this.mKey.getUid());
        if (opResult == null) {
            throw new KeyStoreConnectException();
        }
        this.mOperationToken = opResult.token;
        this.mOperationHandle = opResult.operationHandle;
        GeneralSecurityException e = KeyStoreCryptoOperationUtils.getExceptionForCipherInit(this.mKeyStore, this.mKey, opResult.resultCode);
        if (e != null) {
            if (e instanceof InvalidKeyException) {
                throw ((InvalidKeyException) e);
            }
            if (e instanceof InvalidAlgorithmParameterException) {
                throw ((InvalidAlgorithmParameterException) e);
            }
            throw new ProviderException("Unexpected exception type", e);
        } else if (this.mOperationToken == null) {
            throw new ProviderException("Keystore returned null operation token");
        } else {
            if (this.mOperationHandle == 0) {
                throw new ProviderException("Keystore returned invalid operation handle");
            }
            loadAlgorithmSpecificParametersFromBeginResult(opResult.outParams);
            this.mMainDataStreamer = createMainDataStreamer(this.mKeyStore, opResult.token);
            this.mAdditionalAuthenticationDataStreamer = createAdditionalAuthenticationDataStreamer(this.mKeyStore, opResult.token);
            this.mAdditionalAuthenticationDataStreamerClosed = false;
        }
    }

    protected KeyStoreCryptoOperationStreamer createMainDataStreamer(KeyStore keyStore, IBinder operationToken) {
        return new KeyStoreCryptoOperationChunkedStreamer(new KeyStoreCryptoOperationChunkedStreamer.MainDataStream(keyStore, operationToken));
    }

    protected KeyStoreCryptoOperationStreamer createAdditionalAuthenticationDataStreamer(KeyStore keyStore, IBinder operationToken) {
        return null;
    }

    @Override // javax.crypto.CipherSpi
    protected final byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
        if (this.mCachedException != null) {
            return null;
        }
        try {
            ensureKeystoreOperationInitialized();
            if (inputLen == 0) {
                return null;
            }
            try {
                flushAAD();
                byte[] output = this.mMainDataStreamer.update(input, inputOffset, inputLen);
                if (output.length == 0) {
                    return null;
                }
                return output;
            } catch (KeyStoreException e) {
                this.mCachedException = e;
                return null;
            }
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e2) {
            this.mCachedException = e2;
            return null;
        }
    }

    private void flushAAD() throws KeyStoreException {
        KeyStoreCryptoOperationStreamer keyStoreCryptoOperationStreamer = this.mAdditionalAuthenticationDataStreamer;
        if (keyStoreCryptoOperationStreamer != null && !this.mAdditionalAuthenticationDataStreamerClosed) {
            try {
                byte[] output = keyStoreCryptoOperationStreamer.doFinal(EmptyArray.BYTE, 0, 0, null, null);
                if (output != null && output.length > 0) {
                    throw new ProviderException("AAD update unexpectedly returned data: " + output.length + " bytes");
                }
            } finally {
                this.mAdditionalAuthenticationDataStreamerClosed = true;
            }
        }
    }

    @Override // javax.crypto.CipherSpi
    protected final int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException {
        byte[] outputCopy = engineUpdate(input, inputOffset, inputLen);
        if (outputCopy == null) {
            return 0;
        }
        int outputAvailable = output.length - outputOffset;
        if (outputCopy.length <= outputAvailable) {
            System.arraycopy(outputCopy, 0, output, outputOffset, outputCopy.length);
            return outputCopy.length;
        }
        throw new ShortBufferException("Output buffer too short. Produced: " + outputCopy.length + ", available: " + outputAvailable);
    }

    @Override // javax.crypto.CipherSpi
    protected final int engineUpdate(ByteBuffer input, ByteBuffer output) throws ShortBufferException {
        byte[] inputArray;
        if (input == null) {
            throw new NullPointerException("input == null");
        }
        if (output == null) {
            throw new NullPointerException("output == null");
        }
        int inputSize = input.remaining();
        if (input.hasArray()) {
            inputArray = engineUpdate(input.array(), input.arrayOffset() + input.position(), inputSize);
            input.position(input.position() + inputSize);
        } else {
            byte[] outputArray = new byte[inputSize];
            input.get(outputArray);
            inputArray = engineUpdate(outputArray, 0, inputSize);
        }
        int outputSize = inputArray != null ? inputArray.length : 0;
        if (outputSize > 0) {
            int outputBufferAvailable = output.remaining();
            try {
                output.put(inputArray);
            } catch (BufferOverflowException e) {
                throw new ShortBufferException("Output buffer too small. Produced: " + outputSize + ", available: " + outputBufferAvailable);
            }
        }
        return outputSize;
    }

    @Override // javax.crypto.CipherSpi
    protected final void engineUpdateAAD(byte[] input, int inputOffset, int inputLen) {
        if (this.mCachedException != null) {
            return;
        }
        try {
            ensureKeystoreOperationInitialized();
            if (this.mAdditionalAuthenticationDataStreamerClosed) {
                throw new IllegalStateException("AAD can only be provided before Cipher.update is invoked");
            }
            KeyStoreCryptoOperationStreamer keyStoreCryptoOperationStreamer = this.mAdditionalAuthenticationDataStreamer;
            if (keyStoreCryptoOperationStreamer == null) {
                throw new IllegalStateException("This cipher does not support AAD");
            }
            try {
                byte[] output = keyStoreCryptoOperationStreamer.update(input, inputOffset, inputLen);
                if (output != null && output.length > 0) {
                    throw new ProviderException("AAD update unexpectedly produced output: " + output.length + " bytes");
                }
            } catch (KeyStoreException e) {
                this.mCachedException = e;
            }
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e2) {
            this.mCachedException = e2;
        }
    }

    @Override // javax.crypto.CipherSpi
    protected final void engineUpdateAAD(ByteBuffer src) {
        byte[] input;
        int inputOffset;
        int inputLen;
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        if (!src.hasRemaining()) {
            return;
        }
        if (src.hasArray()) {
            input = src.array();
            inputOffset = src.arrayOffset() + src.position();
            inputLen = src.remaining();
            src.position(src.limit());
        } else {
            input = new byte[src.remaining()];
            inputOffset = 0;
            inputLen = input.length;
            src.get(input);
        }
        engineUpdateAAD(input, inputOffset, inputLen);
    }

    @Override // javax.crypto.CipherSpi
    protected final byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
        if (this.mCachedException != null) {
            throw ((IllegalBlockSizeException) new IllegalBlockSizeException().initCause(this.mCachedException));
        }
        try {
            ensureKeystoreOperationInitialized();
            try {
                flushAAD();
                byte[] additionalEntropy = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(this.mRng, getAdditionalEntropyAmountForFinish());
                byte[] output = this.mMainDataStreamer.doFinal(input, inputOffset, inputLen, null, additionalEntropy);
                resetWhilePreservingInitState();
                return output;
            } catch (KeyStoreException e) {
                int errorCode = e.getErrorCode();
                if (errorCode != -38) {
                    if (errorCode != -30) {
                        if (errorCode == -21) {
                            throw ((IllegalBlockSizeException) new IllegalBlockSizeException().initCause(e));
                        }
                        throw ((IllegalBlockSizeException) new IllegalBlockSizeException().initCause(e));
                    }
                    throw ((AEADBadTagException) new AEADBadTagException().initCause(e));
                }
                throw ((BadPaddingException) new BadPaddingException().initCause(e));
            }
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e2) {
            throw ((IllegalBlockSizeException) new IllegalBlockSizeException().initCause(e2));
        }
    }

    @Override // javax.crypto.CipherSpi
    protected final int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        byte[] outputCopy = engineDoFinal(input, inputOffset, inputLen);
        if (outputCopy == null) {
            return 0;
        }
        int outputAvailable = output.length - outputOffset;
        if (outputCopy.length <= outputAvailable) {
            System.arraycopy(outputCopy, 0, output, outputOffset, outputCopy.length);
            return outputCopy.length;
        }
        throw new ShortBufferException("Output buffer too short. Produced: " + outputCopy.length + ", available: " + outputAvailable);
    }

    @Override // javax.crypto.CipherSpi
    protected final int engineDoFinal(ByteBuffer input, ByteBuffer output) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        byte[] inputArray;
        if (input == null) {
            throw new NullPointerException("input == null");
        }
        if (output == null) {
            throw new NullPointerException("output == null");
        }
        int inputSize = input.remaining();
        if (input.hasArray()) {
            inputArray = engineDoFinal(input.array(), input.arrayOffset() + input.position(), inputSize);
            input.position(input.position() + inputSize);
        } else {
            byte[] outputArray = new byte[inputSize];
            input.get(outputArray);
            inputArray = engineDoFinal(outputArray, 0, inputSize);
        }
        int outputSize = inputArray != null ? inputArray.length : 0;
        if (outputSize > 0) {
            int outputBufferAvailable = output.remaining();
            try {
                output.put(inputArray);
            } catch (BufferOverflowException e) {
                throw new ShortBufferException("Output buffer too small. Produced: " + outputSize + ", available: " + outputBufferAvailable);
            }
        }
        return outputSize;
    }

    @Override // javax.crypto.CipherSpi
    protected final byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        if (this.mKey == null) {
            throw new IllegalStateException("Not initilized");
        }
        if (!isEncrypting()) {
            throw new IllegalStateException("Cipher must be initialized in Cipher.WRAP_MODE to wrap keys");
        }
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        byte[] encoded = null;
        if (key instanceof SecretKey) {
            if ("RAW".equalsIgnoreCase(key.getFormat())) {
                encoded = key.getEncoded();
            }
            if (encoded == null) {
                try {
                    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(key.getAlgorithm());
                    SecretKeySpec spec = (SecretKeySpec) keyFactory.getKeySpec((SecretKey) key, SecretKeySpec.class);
                    encoded = spec.getEncoded();
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    throw new InvalidKeyException("Failed to wrap key because it does not export its key material", e);
                }
            }
        } else if (key instanceof PrivateKey) {
            if ("PKCS8".equalsIgnoreCase(key.getFormat())) {
                encoded = key.getEncoded();
            }
            if (encoded == null) {
                try {
                    KeyFactory keyFactory2 = KeyFactory.getInstance(key.getAlgorithm());
                    PKCS8EncodedKeySpec spec2 = (PKCS8EncodedKeySpec) keyFactory2.getKeySpec(key, PKCS8EncodedKeySpec.class);
                    encoded = spec2.getEncoded();
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e2) {
                    throw new InvalidKeyException("Failed to wrap key because it does not export its key material", e2);
                }
            }
        } else if (key instanceof PublicKey) {
            if ("X.509".equalsIgnoreCase(key.getFormat())) {
                encoded = key.getEncoded();
            }
            if (encoded == null) {
                try {
                    KeyFactory keyFactory3 = KeyFactory.getInstance(key.getAlgorithm());
                    X509EncodedKeySpec spec3 = (X509EncodedKeySpec) keyFactory3.getKeySpec(key, X509EncodedKeySpec.class);
                    encoded = spec3.getEncoded();
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e3) {
                    throw new InvalidKeyException("Failed to wrap key because it does not export its key material", e3);
                }
            }
        } else {
            throw new InvalidKeyException("Unsupported key type: " + key.getClass().getName());
        }
        if (encoded == null) {
            throw new InvalidKeyException("Failed to wrap key because it does not export its key material");
        }
        try {
            return engineDoFinal(encoded, 0, encoded.length);
        } catch (BadPaddingException e4) {
            throw ((IllegalBlockSizeException) new IllegalBlockSizeException().initCause(e4));
        }
    }

    @Override // javax.crypto.CipherSpi
    protected final Key engineUnwrap(byte[] wrappedKey, String wrappedKeyAlgorithm, int wrappedKeyType) throws InvalidKeyException, NoSuchAlgorithmException {
        if (this.mKey == null) {
            throw new IllegalStateException("Not initilized");
        }
        if (isEncrypting()) {
            throw new IllegalStateException("Cipher must be initialized in Cipher.WRAP_MODE to wrap keys");
        }
        if (wrappedKey == null) {
            throw new NullPointerException("wrappedKey == null");
        }
        try {
            byte[] encoded = engineDoFinal(wrappedKey, 0, wrappedKey.length);
            if (wrappedKeyType == 1) {
                KeyFactory keyFactory = KeyFactory.getInstance(wrappedKeyAlgorithm);
                try {
                    return keyFactory.generatePublic(new X509EncodedKeySpec(encoded));
                } catch (InvalidKeySpecException e) {
                    throw new InvalidKeyException("Failed to create public key from its X.509 encoded form", e);
                }
            } else if (wrappedKeyType == 2) {
                KeyFactory keyFactory2 = KeyFactory.getInstance(wrappedKeyAlgorithm);
                try {
                    return keyFactory2.generatePrivate(new PKCS8EncodedKeySpec(encoded));
                } catch (InvalidKeySpecException e2) {
                    throw new InvalidKeyException("Failed to create private key from its PKCS#8 encoded form", e2);
                }
            } else if (wrappedKeyType == 3) {
                return new SecretKeySpec(encoded, wrappedKeyAlgorithm);
            } else {
                throw new InvalidParameterException("Unsupported wrappedKeyType: " + wrappedKeyType);
            }
        } catch (BadPaddingException | IllegalBlockSizeException e3) {
            throw new InvalidKeyException("Failed to unwrap key", e3);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected final void engineSetMode(String mode) throws NoSuchAlgorithmException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.crypto.CipherSpi
    protected final void engineSetPadding(String arg0) throws NoSuchPaddingException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.crypto.CipherSpi
    protected final int engineGetKeySize(Key key) throws InvalidKeyException {
        throw new UnsupportedOperationException();
    }

    public void finalize() throws Throwable {
        try {
            IBinder operationToken = this.mOperationToken;
            if (operationToken != null) {
                this.mKeyStore.abort(operationToken);
            }
        } finally {
            super.finalize();
        }
    }

    @Override // android.security.keystore.KeyStoreCryptoOperation
    public final long getOperationHandle() {
        return this.mOperationHandle;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setKey(AndroidKeyStoreKey key) {
        this.mKey = key;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setKeymasterPurposeOverride(int keymasterPurpose) {
        this.mKeymasterPurposeOverride = keymasterPurpose;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int getKeymasterPurposeOverride() {
        return this.mKeymasterPurposeOverride;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean isEncrypting() {
        return this.mEncrypting;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final KeyStore getKeyStore() {
        return this.mKeyStore;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final long getConsumedInputSizeBytes() {
        KeyStoreCryptoOperationStreamer keyStoreCryptoOperationStreamer = this.mMainDataStreamer;
        if (keyStoreCryptoOperationStreamer == null) {
            throw new IllegalStateException("Not initialized");
        }
        return keyStoreCryptoOperationStreamer.getConsumedInputSizeBytes();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final long getProducedOutputSizeBytes() {
        KeyStoreCryptoOperationStreamer keyStoreCryptoOperationStreamer = this.mMainDataStreamer;
        if (keyStoreCryptoOperationStreamer == null) {
            throw new IllegalStateException("Not initialized");
        }
        return keyStoreCryptoOperationStreamer.getProducedOutputSizeBytes();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String opmodeToString(int opmode) {
        if (opmode != 1) {
            if (opmode != 2) {
                if (opmode != 3) {
                    if (opmode == 4) {
                        return "UNWRAP_MODE";
                    }
                    return String.valueOf(opmode);
                }
                return "WRAP_MODE";
            }
            return "DECRYPT_MODE";
        }
        return "ENCRYPT_MODE";
    }
}
