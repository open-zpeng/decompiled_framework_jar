package com.xpeng.security.keystore;

import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import java.security.InvalidKeyException;
/* loaded from: classes3.dex */
abstract class AndroidKeyStoreRSASignatureSpi extends AndroidKeyStoreSignatureSpiBase {
    private final int mKeymasterDigest;
    private final int mKeymasterPadding;

    /* loaded from: classes3.dex */
    static abstract class PKCS1Padding extends AndroidKeyStoreRSASignatureSpi {
        PKCS1Padding(int keymasterDigest) {
            super(keymasterDigest, 5);
        }

        @Override // com.xpeng.security.keystore.AndroidKeyStoreSignatureSpiBase
        protected final int getAdditionalEntropyAmountForSign() {
            return 0;
        }
    }

    /* loaded from: classes3.dex */
    public static final class NONEWithPKCS1Padding extends PKCS1Padding {
        public NONEWithPKCS1Padding() {
            super(0);
        }
    }

    /* loaded from: classes3.dex */
    public static final class MD5WithPKCS1Padding extends PKCS1Padding {
        public MD5WithPKCS1Padding() {
            super(1);
        }
    }

    /* loaded from: classes3.dex */
    public static final class SHA1WithPKCS1Padding extends PKCS1Padding {
        public SHA1WithPKCS1Padding() {
            super(2);
        }
    }

    /* loaded from: classes3.dex */
    public static final class SHA224WithPKCS1Padding extends PKCS1Padding {
        public SHA224WithPKCS1Padding() {
            super(3);
        }
    }

    /* loaded from: classes3.dex */
    public static final class SHA256WithPKCS1Padding extends PKCS1Padding {
        public SHA256WithPKCS1Padding() {
            super(4);
        }
    }

    /* loaded from: classes3.dex */
    public static final class SHA384WithPKCS1Padding extends PKCS1Padding {
        public SHA384WithPKCS1Padding() {
            super(5);
        }
    }

    /* loaded from: classes3.dex */
    public static final class SHA512WithPKCS1Padding extends PKCS1Padding {
        public SHA512WithPKCS1Padding() {
            super(6);
        }
    }

    /* loaded from: classes3.dex */
    static abstract class PSSPadding extends AndroidKeyStoreRSASignatureSpi {
        private static final int SALT_LENGTH_BYTES = 20;

        PSSPadding(int keymasterDigest) {
            super(keymasterDigest, 3);
        }

        @Override // com.xpeng.security.keystore.AndroidKeyStoreSignatureSpiBase
        protected final int getAdditionalEntropyAmountForSign() {
            return 20;
        }
    }

    /* loaded from: classes3.dex */
    public static final class SHA1WithPSSPadding extends PSSPadding {
        public SHA1WithPSSPadding() {
            super(2);
        }
    }

    /* loaded from: classes3.dex */
    public static final class SHA224WithPSSPadding extends PSSPadding {
        public SHA224WithPSSPadding() {
            super(3);
        }
    }

    /* loaded from: classes3.dex */
    public static final class SHA256WithPSSPadding extends PSSPadding {
        public SHA256WithPSSPadding() {
            super(4);
        }
    }

    /* loaded from: classes3.dex */
    public static final class SHA384WithPSSPadding extends PSSPadding {
        public SHA384WithPSSPadding() {
            super(5);
        }
    }

    /* loaded from: classes3.dex */
    public static final class SHA512WithPSSPadding extends PSSPadding {
        public SHA512WithPSSPadding() {
            super(6);
        }
    }

    AndroidKeyStoreRSASignatureSpi(int keymasterDigest, int keymasterPadding) {
        this.mKeymasterDigest = keymasterDigest;
        this.mKeymasterPadding = keymasterPadding;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xpeng.security.keystore.AndroidKeyStoreSignatureSpiBase
    public final void initKey(AndroidKeyStoreKey key) throws InvalidKeyException {
        if (!"RSA".equalsIgnoreCase(key.getAlgorithm())) {
            throw new InvalidKeyException("Unsupported key algorithm: " + key.getAlgorithm() + ". OnlyRSA supported");
        }
        super.initKey(key);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xpeng.security.keystore.AndroidKeyStoreSignatureSpiBase
    public final void resetAll() {
        super.resetAll();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.xpeng.security.keystore.AndroidKeyStoreSignatureSpiBase
    public final void resetWhilePreservingInitState() {
        super.resetWhilePreservingInitState();
    }

    @Override // com.xpeng.security.keystore.AndroidKeyStoreSignatureSpiBase
    protected final void addAlgorithmSpecificParametersToBegin(KeymasterArguments keymasterArgs) {
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_ALGORITHM, 1);
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_DIGEST, this.mKeymasterDigest);
        keymasterArgs.addEnum(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterPadding);
    }
}
