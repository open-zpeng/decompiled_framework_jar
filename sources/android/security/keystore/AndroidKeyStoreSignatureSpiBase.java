package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.KeyStoreException;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.OperationResult;
import android.security.keystore.KeyStoreCryptoOperationChunkedStreamer;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import libcore.util.EmptyArray;

/* loaded from: classes2.dex */
abstract class AndroidKeyStoreSignatureSpiBase extends SignatureSpi implements KeyStoreCryptoOperation {
    private Exception mCachedException;
    private AndroidKeyStoreKey mKey;
    private final KeyStore mKeyStore = KeyStore.getInstance();
    private KeyStoreCryptoOperationStreamer mMessageStreamer;
    private long mOperationHandle;
    private IBinder mOperationToken;
    private boolean mSigning;

    protected abstract void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArguments);

    protected abstract int getAdditionalEntropyAmountForSign();

    @Override // java.security.SignatureSpi
    protected final void engineInitSign(PrivateKey key) throws InvalidKeyException {
        engineInitSign(key, null);
    }

    @Override // java.security.SignatureSpi
    protected final void engineInitSign(PrivateKey privateKey, SecureRandom random) throws InvalidKeyException {
        resetAll();
        try {
            if (privateKey == null) {
                throw new InvalidKeyException("Unsupported key: null");
            }
            if (privateKey instanceof AndroidKeyStorePrivateKey) {
                AndroidKeyStoreKey keystoreKey = (AndroidKeyStoreKey) privateKey;
                this.mSigning = true;
                initKey(keystoreKey);
                this.appRandom = random;
                ensureKeystoreOperationInitialized();
                if (1 == 0) {
                    resetAll();
                    return;
                }
                return;
            }
            throw new InvalidKeyException("Unsupported private key type: " + privateKey);
        } catch (Throwable th) {
            if (0 == 0) {
                resetAll();
            }
            throw th;
        }
    }

    @Override // java.security.SignatureSpi
    protected final void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        resetAll();
        try {
            if (publicKey == null) {
                throw new InvalidKeyException("Unsupported key: null");
            }
            if (publicKey instanceof AndroidKeyStorePublicKey) {
                AndroidKeyStoreKey keystoreKey = (AndroidKeyStorePublicKey) publicKey;
                this.mSigning = false;
                initKey(keystoreKey);
                this.appRandom = null;
                ensureKeystoreOperationInitialized();
                if (1 == 0) {
                    resetAll();
                    return;
                }
                return;
            }
            throw new InvalidKeyException("Unsupported public key type: " + publicKey);
        } catch (Throwable th) {
            if (0 == 0) {
                resetAll();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initKey(AndroidKeyStoreKey key) throws InvalidKeyException {
        this.mKey = key;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void resetAll() {
        IBinder operationToken = this.mOperationToken;
        if (operationToken != null) {
            this.mOperationToken = null;
            this.mKeyStore.abort(operationToken);
        }
        this.mSigning = false;
        this.mKey = null;
        this.appRandom = null;
        this.mOperationToken = null;
        this.mOperationHandle = 0L;
        this.mMessageStreamer = null;
        this.mCachedException = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void resetWhilePreservingInitState() {
        IBinder operationToken = this.mOperationToken;
        if (operationToken != null) {
            this.mOperationToken = null;
            this.mKeyStore.abort(operationToken);
        }
        this.mOperationHandle = 0L;
        this.mMessageStreamer = null;
        this.mCachedException = null;
    }

    private void ensureKeystoreOperationInitialized() throws InvalidKeyException {
        if (this.mMessageStreamer != null || this.mCachedException != null) {
            return;
        }
        if (this.mKey == null) {
            throw new IllegalStateException("Not initialized");
        }
        KeymasterArguments keymasterInputArgs = new KeymasterArguments();
        addAlgorithmSpecificParametersToBegin(keymasterInputArgs);
        OperationResult opResult = this.mKeyStore.begin(this.mKey.getAlias(), this.mSigning ? 2 : 3, true, keymasterInputArgs, null, this.mKey.getUid());
        if (opResult == null) {
            throw new KeyStoreConnectException();
        }
        this.mOperationToken = opResult.token;
        this.mOperationHandle = opResult.operationHandle;
        InvalidKeyException e = KeyStoreCryptoOperationUtils.getInvalidKeyExceptionForInit(this.mKeyStore, this.mKey, opResult.resultCode);
        if (e != null) {
            throw e;
        }
        if (this.mOperationToken == null) {
            throw new ProviderException("Keystore returned null operation token");
        }
        if (this.mOperationHandle == 0) {
            throw new ProviderException("Keystore returned invalid operation handle");
        }
        this.mMessageStreamer = createMainDataStreamer(this.mKeyStore, opResult.token);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public KeyStoreCryptoOperationStreamer createMainDataStreamer(KeyStore keyStore, IBinder operationToken) {
        return new KeyStoreCryptoOperationChunkedStreamer(new KeyStoreCryptoOperationChunkedStreamer.MainDataStream(keyStore, operationToken));
    }

    @Override // android.security.keystore.KeyStoreCryptoOperation
    public final long getOperationHandle() {
        return this.mOperationHandle;
    }

    @Override // java.security.SignatureSpi
    protected final void engineUpdate(byte[] b, int off, int len) throws SignatureException {
        Exception exc = this.mCachedException;
        if (exc != null) {
            throw new SignatureException(exc);
        }
        try {
            ensureKeystoreOperationInitialized();
            if (len == 0) {
                return;
            }
            try {
                byte[] output = this.mMessageStreamer.update(b, off, len);
                if (output.length != 0) {
                    throw new ProviderException("Update operation unexpectedly produced output: " + output.length + " bytes");
                }
            } catch (KeyStoreException e) {
                throw new SignatureException(e);
            }
        } catch (InvalidKeyException e2) {
            throw new SignatureException(e2);
        }
    }

    @Override // java.security.SignatureSpi
    protected final void engineUpdate(byte b) throws SignatureException {
        engineUpdate(new byte[]{b}, 0, 1);
    }

    @Override // java.security.SignatureSpi
    protected final void engineUpdate(ByteBuffer input) {
        byte[] b;
        int off;
        int len = input.remaining();
        if (input.hasArray()) {
            b = input.array();
            off = input.arrayOffset() + input.position();
            input.position(input.limit());
        } else {
            b = new byte[len];
            off = 0;
            input.get(b);
        }
        try {
            engineUpdate(b, off, len);
        } catch (SignatureException e) {
            this.mCachedException = e;
        }
    }

    @Override // java.security.SignatureSpi
    protected final int engineSign(byte[] out, int outOffset, int outLen) throws SignatureException {
        return super.engineSign(out, outOffset, outLen);
    }

    @Override // java.security.SignatureSpi
    protected final byte[] engineSign() throws SignatureException {
        Exception exc = this.mCachedException;
        if (exc != null) {
            throw new SignatureException(exc);
        }
        try {
            ensureKeystoreOperationInitialized();
            byte[] additionalEntropy = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(this.appRandom, getAdditionalEntropyAmountForSign());
            byte[] signature = this.mMessageStreamer.doFinal(EmptyArray.BYTE, 0, 0, null, additionalEntropy);
            resetWhilePreservingInitState();
            return signature;
        } catch (KeyStoreException | InvalidKeyException e) {
            throw new SignatureException(e);
        }
    }

    @Override // java.security.SignatureSpi
    protected final boolean engineVerify(byte[] signature) throws SignatureException {
        boolean verified;
        byte[] output;
        Exception exc = this.mCachedException;
        if (exc != null) {
            throw new SignatureException(exc);
        }
        try {
            ensureKeystoreOperationInitialized();
            try {
                output = this.mMessageStreamer.doFinal(EmptyArray.BYTE, 0, 0, signature, null);
            } catch (KeyStoreException e) {
                if (e.getErrorCode() == -30) {
                    verified = false;
                } else {
                    throw new SignatureException(e);
                }
            }
            if (output.length != 0) {
                throw new ProviderException("Signature verification unexpected produced output: " + output.length + " bytes");
            }
            verified = true;
            resetWhilePreservingInitState();
            return verified;
        } catch (InvalidKeyException e2) {
            throw new SignatureException(e2);
        }
    }

    @Override // java.security.SignatureSpi
    protected final boolean engineVerify(byte[] sigBytes, int offset, int len) throws SignatureException {
        return engineVerify(ArrayUtils.subarray(sigBytes, offset, len));
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected final Object engineGetParameter(String param) throws InvalidParameterException {
        throw new InvalidParameterException();
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected final void engineSetParameter(String param, Object value) throws InvalidParameterException {
        throw new InvalidParameterException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final KeyStore getKeyStore() {
        return this.mKeyStore;
    }

    protected final boolean isSigning() {
        return this.mSigning;
    }
}
