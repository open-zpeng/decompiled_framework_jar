package android.security.keystore;

import java.security.KeyStore;

/* loaded from: classes2.dex */
class AndroidKeyStoreLoadStoreParameter implements KeyStore.LoadStoreParameter {
    private final int mUid;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AndroidKeyStoreLoadStoreParameter(int uid) {
        this.mUid = uid;
    }

    @Override // java.security.KeyStore.LoadStoreParameter
    public KeyStore.ProtectionParameter getProtectionParameter() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getUid() {
        return this.mUid;
    }
}
