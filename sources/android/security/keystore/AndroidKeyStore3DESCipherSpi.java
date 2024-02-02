package android.security.keystore;

import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import javax.crypto.spec.IvParameterSpec;
/* loaded from: classes2.dex */
public class AndroidKeyStore3DESCipherSpi extends AndroidKeyStoreCipherSpiBase {
    private static final int BLOCK_SIZE_BYTES = 8;
    private byte[] mIv;
    private boolean mIvHasBeenUsed;
    private final boolean mIvRequired;
    private final int mKeymasterBlockMode;
    private final int mKeymasterPadding;

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    public /* bridge */ /* synthetic */ void finalize() throws Throwable {
        super.finalize();
    }

    synchronized AndroidKeyStore3DESCipherSpi(int keymasterBlockMode, int keymasterPadding, boolean ivRequired) {
        this.mKeymasterBlockMode = keymasterBlockMode;
        this.mKeymasterPadding = keymasterPadding;
        this.mIvRequired = ivRequired;
    }

    /* loaded from: classes2.dex */
    static abstract class ECB extends AndroidKeyStore3DESCipherSpi {
        protected synchronized ECB(int keymasterPadding) {
            super(1, keymasterPadding, false);
        }

        /* loaded from: classes2.dex */
        public static class NoPadding extends ECB {
            @Override // android.security.keystore.AndroidKeyStore3DESCipherSpi, android.security.keystore.AndroidKeyStoreCipherSpiBase
            public /* bridge */ /* synthetic */ void finalize() throws Throwable {
                super.finalize();
            }

            public synchronized NoPadding() {
                super(1);
            }
        }

        /* loaded from: classes2.dex */
        public static class PKCS7Padding extends ECB {
            @Override // android.security.keystore.AndroidKeyStore3DESCipherSpi, android.security.keystore.AndroidKeyStoreCipherSpiBase
            public /* bridge */ /* synthetic */ void finalize() throws Throwable {
                super.finalize();
            }

            public synchronized PKCS7Padding() {
                super(64);
            }
        }
    }

    /* loaded from: classes2.dex */
    static abstract class CBC extends AndroidKeyStore3DESCipherSpi {
        protected synchronized CBC(int keymasterPadding) {
            super(2, keymasterPadding, true);
        }

        /* loaded from: classes2.dex */
        public static class NoPadding extends CBC {
            @Override // android.security.keystore.AndroidKeyStore3DESCipherSpi, android.security.keystore.AndroidKeyStoreCipherSpiBase
            public /* bridge */ /* synthetic */ void finalize() throws Throwable {
                super.finalize();
            }

            public synchronized NoPadding() {
                super(1);
            }
        }

        /* loaded from: classes2.dex */
        public static class PKCS7Padding extends CBC {
            @Override // android.security.keystore.AndroidKeyStore3DESCipherSpi, android.security.keystore.AndroidKeyStoreCipherSpiBase
            public /* bridge */ /* synthetic */ void finalize() throws Throwable {
                super.finalize();
            }

            public synchronized PKCS7Padding() {
                super(64);
            }
        }
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    protected synchronized void initKey(int i, Key key) throws InvalidKeyException {
        if (!(key instanceof AndroidKeyStoreSecretKey)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unsupported key: ");
            sb.append(key != null ? key.getClass().getName() : "null");
            throw new InvalidKeyException(sb.toString());
        } else if (!"DESede".equalsIgnoreCase(key.getAlgorithm())) {
            throw new InvalidKeyException("Unsupported key algorithm: " + key.getAlgorithm() + ". Only DESede supported");
        } else {
            setKey((AndroidKeyStoreSecretKey) key);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetBlockSize() {
        return 8;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetOutputSize(int inputLen) {
        return inputLen + 24;
    }

    @Override // javax.crypto.CipherSpi
    protected final byte[] engineGetIV() {
        return ArrayUtils.cloneIfNotEmpty(this.mIv);
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase, javax.crypto.CipherSpi
    protected AlgorithmParameters engineGetParameters() {
        if (this.mIvRequired && this.mIv != null && this.mIv.length > 0) {
            try {
                AlgorithmParameters params = AlgorithmParameters.getInstance("DESede");
                params.init(new IvParameterSpec(this.mIv));
                return params;
            } catch (NoSuchAlgorithmException e) {
                throw new ProviderException("Failed to obtain 3DES AlgorithmParameters", e);
            } catch (InvalidParameterSpecException e2) {
                throw new ProviderException("Failed to initialize 3DES AlgorithmParameters with an IV", e2);
            }
        }
        return null;
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    protected synchronized void initAlgorithmSpecificParameters() throws InvalidKeyException {
        if (this.mIvRequired && !isEncrypting()) {
            throw new InvalidKeyException("IV required when decrypting. Use IvParameterSpec or AlgorithmParameters to provide it.");
        }
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    protected synchronized void initAlgorithmSpecificParameters(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException {
        if (!this.mIvRequired) {
            if (params != null) {
                throw new InvalidAlgorithmParameterException("Unsupported parameters: " + params);
            }
        } else if (params == null) {
            if (!isEncrypting()) {
                throw new InvalidAlgorithmParameterException("IvParameterSpec must be provided when decrypting");
            }
        } else if (!(params instanceof IvParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Only IvParameterSpec supported");
        } else {
            this.mIv = ((IvParameterSpec) params).getIV();
            if (this.mIv == null) {
                throw new InvalidAlgorithmParameterException("Null IV in IvParameterSpec");
            }
        }
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    protected synchronized void initAlgorithmSpecificParameters(AlgorithmParameters params) throws InvalidAlgorithmParameterException {
        if (!this.mIvRequired) {
            if (params != null) {
                throw new InvalidAlgorithmParameterException("Unsupported parameters: " + params);
            }
        } else if (params == null) {
            if (!isEncrypting()) {
                throw new InvalidAlgorithmParameterException("IV required when decrypting. Use IvParameterSpec or AlgorithmParameters to provide it.");
            }
        } else if (!"DESede".equalsIgnoreCase(params.getAlgorithm())) {
            throw new InvalidAlgorithmParameterException("Unsupported AlgorithmParameters algorithm: " + params.getAlgorithm() + ". Supported: DESede");
        } else {
            try {
                IvParameterSpec ivSpec = (IvParameterSpec) params.getParameterSpec(IvParameterSpec.class);
                this.mIv = ivSpec.getIV();
                if (this.mIv == null) {
                    throw new InvalidAlgorithmParameterException("Null IV in AlgorithmParameters");
                }
            } catch (InvalidParameterSpecException e) {
                if (!isEncrypting()) {
                    throw new InvalidAlgorithmParameterException("IV required when decrypting, but not found in parameters: " + params, e);
                }
                this.mIv = null;
            }
        }
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    protected final synchronized int getAdditionalEntropyAmountForBegin() {
        if (this.mIvRequired && this.mIv == null && isEncrypting()) {
            return 8;
        }
        return 0;
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    protected synchronized int getAdditionalEntropyAmountForFinish() {
        return 0;
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    protected synchronized void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArgs) {
        if (isEncrypting() && this.mIvRequired && this.mIvHasBeenUsed) {
            throw new IllegalStateException("IV has already been used. Reusing IV in encryption mode violates security best practices.");
        }
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_ALGORITHM, 33);
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_BLOCK_MODE, this.mKeymasterBlockMode);
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterPadding);
        if (this.mIvRequired && this.mIv != null) {
            keymasterArgs.addBytes(KeymasterDefs.KM_TAG_NONCE, this.mIv);
        }
    }

    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    protected synchronized void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments keymasterArgs) {
        this.mIvHasBeenUsed = true;
        byte[] returnedIv = keymasterArgs.getBytes(KeymasterDefs.KM_TAG_NONCE, null);
        if (returnedIv != null && returnedIv.length == 0) {
            returnedIv = null;
        }
        if (this.mIvRequired) {
            if (this.mIv == null) {
                this.mIv = returnedIv;
            } else if (returnedIv != null && !Arrays.equals(returnedIv, this.mIv)) {
                throw new ProviderException("IV in use differs from provided IV");
            }
        } else if (returnedIv != null) {
            throw new ProviderException("IV in use despite IV not being used by this transformation");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.security.keystore.AndroidKeyStoreCipherSpiBase
    public final synchronized void resetAll() {
        this.mIv = null;
        this.mIvHasBeenUsed = false;
        super.resetAll();
    }
}
