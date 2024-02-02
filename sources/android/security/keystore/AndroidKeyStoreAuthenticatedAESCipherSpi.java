package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.KeyStoreException;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import android.security.keymaster.OperationResult;
import android.security.keystore.KeyStoreCryptoOperationChunkedStreamer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import javax.crypto.spec.GCMParameterSpec;
import libcore.util.EmptyArray;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class AndroidKeyStoreAuthenticatedAESCipherSpi extends AndroidKeyStoreCipherSpiBase {
    private static final int BLOCK_SIZE_BYTES = 16;
    private byte[] mIv;
    private boolean mIvHasBeenUsed;
    private final int mKeymasterBlockMode;
    private final int mKeymasterPadding;

    /* loaded from: classes2.dex */
    static abstract class GCM extends AndroidKeyStoreAuthenticatedAESCipherSpi {
        private static final int DEFAULT_TAG_LENGTH_BITS = 128;
        private static final int IV_LENGTH_BYTES = 12;
        private static final int MAX_SUPPORTED_TAG_LENGTH_BITS = 128;
        static final int MIN_SUPPORTED_TAG_LENGTH_BITS = 96;
        private int mTagLengthBits;

        GCM(int keymasterPadding) {
            super(32, keymasterPadding);
            this.mTagLengthBits = 128;
        }

        @Override // android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi, android.security.keystore.AndroidKeyStoreCipherSpiBase
        protected final void resetAll() {
            this.mTagLengthBits = 128;
            super.resetAll();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
        public final void resetWhilePreservingInitState() {
            super.resetWhilePreservingInitState();
        }

        @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
        protected final void initAlgorithmSpecificParameters() throws InvalidKeyException {
            if (!isEncrypting()) {
                throw new InvalidKeyException("IV required when decrypting. Use IvParameterSpec or AlgorithmParameters to provide it.");
            }
        }

        @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
        protected final void initAlgorithmSpecificParameters(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException {
            if (params == null) {
                if (!isEncrypting()) {
                    throw new InvalidAlgorithmParameterException("GCMParameterSpec must be provided when decrypting");
                }
            } else if (!(params instanceof GCMParameterSpec)) {
                throw new InvalidAlgorithmParameterException("Only GCMParameterSpec supported");
            } else {
                GCMParameterSpec spec = (GCMParameterSpec) params;
                byte[] iv = spec.getIV();
                if (iv == null) {
                    throw new InvalidAlgorithmParameterException("Null IV in GCMParameterSpec");
                }
                if (iv.length != 12) {
                    throw new InvalidAlgorithmParameterException("Unsupported IV length: " + iv.length + " bytes. Only 12 bytes long IV supported");
                }
                int tagLengthBits = spec.getTLen();
                if (tagLengthBits < 96 || tagLengthBits > 128 || tagLengthBits % 8 != 0) {
                    throw new InvalidAlgorithmParameterException("Unsupported tag length: " + tagLengthBits + " bits. Supported lengths: 96, 104, 112, 120, 128");
                }
                setIv(iv);
                this.mTagLengthBits = tagLengthBits;
            }
        }

        @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
        protected final void initAlgorithmSpecificParameters(AlgorithmParameters params) throws InvalidAlgorithmParameterException {
            if (params == null) {
                if (!isEncrypting()) {
                    throw new InvalidAlgorithmParameterException("IV required when decrypting. Use GCMParameterSpec or GCM AlgorithmParameters to provide it.");
                }
            } else if (!"GCM".equalsIgnoreCase(params.getAlgorithm())) {
                throw new InvalidAlgorithmParameterException("Unsupported AlgorithmParameters algorithm: " + params.getAlgorithm() + ". Supported: GCM");
            } else {
                try {
                    GCMParameterSpec spec = (GCMParameterSpec) params.getParameterSpec(GCMParameterSpec.class);
                    initAlgorithmSpecificParameters(spec);
                } catch (InvalidParameterSpecException e) {
                    if (!isEncrypting()) {
                        throw new InvalidAlgorithmParameterException("IV and tag length required when decrypting, but not found in parameters: " + params, e);
                    }
                    setIv(null);
                }
            }
        }

        @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase, javax.crypto.CipherSpi
        protected final AlgorithmParameters engineGetParameters() {
            byte[] iv = getIv();
            if (iv != null && iv.length > 0) {
                try {
                    AlgorithmParameters params = AlgorithmParameters.getInstance("GCM");
                    params.init(new GCMParameterSpec(this.mTagLengthBits, iv));
                    return params;
                } catch (NoSuchAlgorithmException e) {
                    throw new ProviderException("Failed to obtain GCM AlgorithmParameters", e);
                } catch (InvalidParameterSpecException e2) {
                    throw new ProviderException("Failed to initialize GCM AlgorithmParameters", e2);
                }
            }
            return null;
        }

        @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
        protected KeyStoreCryptoOperationStreamer createMainDataStreamer(KeyStore keyStore, IBinder operationToken) {
            KeyStoreCryptoOperationStreamer streamer = new KeyStoreCryptoOperationChunkedStreamer(new KeyStoreCryptoOperationChunkedStreamer.MainDataStream(keyStore, operationToken));
            if (isEncrypting()) {
                return streamer;
            }
            return new BufferAllOutputUntilDoFinalStreamer(streamer);
        }

        @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
        protected final KeyStoreCryptoOperationStreamer createAdditionalAuthenticationDataStreamer(KeyStore keyStore, IBinder operationToken) {
            return new KeyStoreCryptoOperationChunkedStreamer(new AdditionalAuthenticationDataStream(keyStore, operationToken));
        }

        @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
        protected final int getAdditionalEntropyAmountForBegin() {
            if (getIv() == null && isEncrypting()) {
                return 12;
            }
            return 0;
        }

        @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
        protected final int getAdditionalEntropyAmountForFinish() {
            return 0;
        }

        @Override // android.security.keystore.AndroidKeyStoreAuthenticatedAESCipherSpi, android.security.keystore.AndroidKeyStoreCipherSpiBase
        protected final void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArgs) {
            super.addAlgorithmSpecificParametersToBegin(keymasterArgs);
            keymasterArgs.addUnsignedInt(KeymasterDefs.KM_TAG_MAC_LENGTH, this.mTagLengthBits);
        }

        protected final int getTagLengthBits() {
            return this.mTagLengthBits;
        }

        /* loaded from: classes2.dex */
        public static final class NoPadding extends GCM {
            @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
            public /* bridge */ /* synthetic */ void finalize() throws Throwable {
                super.finalize();
            }

            public NoPadding() {
                super(1);
            }

            @Override // javax.crypto.CipherSpi
            protected final int engineGetOutputSize(int inputLen) {
                long result;
                int tagLengthBytes = (getTagLengthBits() + 7) / 8;
                if (isEncrypting()) {
                    result = (getConsumedInputSizeBytes() - getProducedOutputSizeBytes()) + inputLen + tagLengthBytes;
                } else {
                    long result2 = getConsumedInputSizeBytes();
                    result = ((result2 - getProducedOutputSizeBytes()) + inputLen) - tagLengthBytes;
                }
                if (result < 0) {
                    return 0;
                }
                if (result > 2147483647L) {
                    return Integer.MAX_VALUE;
                }
                return (int) result;
            }
        }
    }

    AndroidKeyStoreAuthenticatedAESCipherSpi(int keymasterBlockMode, int keymasterPadding) {
        this.mKeymasterBlockMode = keymasterBlockMode;
        this.mKeymasterPadding = keymasterPadding;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    public void resetAll() {
        this.mIv = null;
        this.mIvHasBeenUsed = false;
        super.resetAll();
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    protected final void initKey(int opmode, Key key) throws InvalidKeyException {
        if (!(key instanceof AndroidKeyStoreSecretKey)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unsupported key: ");
            sb.append(key != null ? key.getClass().getName() : "null");
            throw new InvalidKeyException(sb.toString());
        } else if (!"AES".equalsIgnoreCase(key.getAlgorithm())) {
            throw new InvalidKeyException("Unsupported key algorithm: " + key.getAlgorithm() + ". Only AES supported");
        } else {
            setKey((AndroidKeyStoreSecretKey) key);
        }
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    protected void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArgs) {
        if (isEncrypting() && this.mIvHasBeenUsed) {
            throw new IllegalStateException("IV has already been used. Reusing IV in encryption mode violates security best practices.");
        }
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_ALGORITHM, 32);
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_BLOCK_MODE, this.mKeymasterBlockMode);
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterPadding);
        if (this.mIv != null) {
            keymasterArgs.addBytes(KeymasterDefs.KM_TAG_NONCE, this.mIv);
        }
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    protected final void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments keymasterArgs) {
        this.mIvHasBeenUsed = true;
        byte[] returnedIv = keymasterArgs.getBytes(KeymasterDefs.KM_TAG_NONCE, null);
        if (returnedIv != null && returnedIv.length == 0) {
            returnedIv = null;
        }
        if (this.mIv == null) {
            this.mIv = returnedIv;
        } else if (returnedIv != null && !Arrays.equals(returnedIv, this.mIv)) {
            throw new ProviderException("IV in use differs from provided IV");
        }
    }

    @Override // javax.crypto.CipherSpi
    protected final int engineGetBlockSize() {
        return 16;
    }

    @Override // javax.crypto.CipherSpi
    protected final byte[] engineGetIV() {
        return ArrayUtils.cloneIfNotEmpty(this.mIv);
    }

    protected void setIv(byte[] iv) {
        this.mIv = iv;
    }

    protected byte[] getIv() {
        return this.mIv;
    }

    /* loaded from: classes2.dex */
    private static class BufferAllOutputUntilDoFinalStreamer implements KeyStoreCryptoOperationStreamer {
        private ByteArrayOutputStream mBufferedOutput;
        private final KeyStoreCryptoOperationStreamer mDelegate;
        private long mProducedOutputSizeBytes;

        private BufferAllOutputUntilDoFinalStreamer(KeyStoreCryptoOperationStreamer delegate) {
            this.mBufferedOutput = new ByteArrayOutputStream();
            this.mDelegate = delegate;
        }

        @Override // android.security.keystore.KeyStoreCryptoOperationStreamer
        public byte[] update(byte[] input, int inputOffset, int inputLength) throws KeyStoreException {
            byte[] output = this.mDelegate.update(input, inputOffset, inputLength);
            if (output != null) {
                try {
                    this.mBufferedOutput.write(output);
                } catch (IOException e) {
                    throw new ProviderException("Failed to buffer output", e);
                }
            }
            return EmptyArray.BYTE;
        }

        @Override // android.security.keystore.KeyStoreCryptoOperationStreamer
        public byte[] doFinal(byte[] input, int inputOffset, int inputLength, byte[] signature, byte[] additionalEntropy) throws KeyStoreException {
            byte[] output = this.mDelegate.doFinal(input, inputOffset, inputLength, signature, additionalEntropy);
            if (output != null) {
                try {
                    this.mBufferedOutput.write(output);
                } catch (IOException e) {
                    throw new ProviderException("Failed to buffer output", e);
                }
            }
            byte[] result = this.mBufferedOutput.toByteArray();
            this.mBufferedOutput.reset();
            this.mProducedOutputSizeBytes += result.length;
            return result;
        }

        @Override // android.security.keystore.KeyStoreCryptoOperationStreamer
        public long getConsumedInputSizeBytes() {
            return this.mDelegate.getConsumedInputSizeBytes();
        }

        @Override // android.security.keystore.KeyStoreCryptoOperationStreamer
        public long getProducedOutputSizeBytes() {
            return this.mProducedOutputSizeBytes;
        }
    }

    /* loaded from: classes2.dex */
    private static class AdditionalAuthenticationDataStream implements KeyStoreCryptoOperationChunkedStreamer.Stream {
        private final KeyStore mKeyStore;
        private final IBinder mOperationToken;

        private AdditionalAuthenticationDataStream(KeyStore keyStore, IBinder operationToken) {
            this.mKeyStore = keyStore;
            this.mOperationToken = operationToken;
        }

        @Override // android.security.keystore.KeyStoreCryptoOperationChunkedStreamer.Stream
        public OperationResult update(byte[] input) {
            KeymasterArguments keymasterArgs = new KeymasterArguments();
            keymasterArgs.addBytes(KeymasterDefs.KM_TAG_ASSOCIATED_DATA, input);
            OperationResult result = this.mKeyStore.update(this.mOperationToken, keymasterArgs, null);
            if (result.resultCode == 1) {
                return new OperationResult(result.resultCode, result.token, result.operationHandle, input.length, result.output, result.outParams);
            }
            return result;
        }

        @Override // android.security.keystore.KeyStoreCryptoOperationChunkedStreamer.Stream
        public OperationResult finish(byte[] signature, byte[] additionalEntropy) {
            if (additionalEntropy != null && additionalEntropy.length > 0) {
                throw new ProviderException("AAD stream does not support additional entropy");
            }
            return new OperationResult(1, this.mOperationToken, 0L, 0, EmptyArray.BYTE, new KeymasterArguments());
        }
    }
}
