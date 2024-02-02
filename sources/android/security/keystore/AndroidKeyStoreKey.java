package android.security.keystore;

import java.security.Key;
/* loaded from: classes2.dex */
public class AndroidKeyStoreKey implements Key {
    private final String mAlgorithm;
    private final String mAlias;
    private final int mUid;

    public synchronized AndroidKeyStoreKey(String alias, int uid, String algorithm) {
        this.mAlias = alias;
        this.mUid = uid;
        this.mAlgorithm = algorithm;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String getAlias() {
        return this.mAlias;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getUid() {
        return this.mUid;
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        return this.mAlgorithm;
    }

    @Override // java.security.Key
    public String getFormat() {
        return null;
    }

    @Override // java.security.Key
    public byte[] getEncoded() {
        return null;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.mAlgorithm == null ? 0 : this.mAlgorithm.hashCode());
        return (31 * ((31 * result) + (this.mAlias != null ? this.mAlias.hashCode() : 0))) + this.mUid;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AndroidKeyStoreKey other = (AndroidKeyStoreKey) obj;
        if (this.mAlgorithm == null) {
            if (other.mAlgorithm != null) {
                return false;
            }
        } else if (!this.mAlgorithm.equals(other.mAlgorithm)) {
            return false;
        }
        if (this.mAlias == null) {
            if (other.mAlias != null) {
                return false;
            }
        } else if (!this.mAlias.equals(other.mAlias)) {
            return false;
        }
        if (this.mUid == other.mUid) {
            return true;
        }
        return false;
    }
}
