package android.net.lowpan;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.HexDump;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes2.dex */
public class LowpanCredential implements Parcelable {
    private protected static final Parcelable.Creator<LowpanCredential> CREATOR = new Parcelable.Creator<LowpanCredential>() { // from class: android.net.lowpan.LowpanCredential.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LowpanCredential createFromParcel(Parcel in) {
            LowpanCredential credential = new LowpanCredential();
            credential.mMasterKey = in.createByteArray();
            credential.mMasterKeyIndex = in.readInt();
            return credential;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LowpanCredential[] newArray(int size) {
            return new LowpanCredential[size];
        }
    };
    private protected static final int UNSPECIFIED_KEY_INDEX = 0;
    public protected byte[] mMasterKey = null;
    public protected int mMasterKeyIndex = 0;

    public private protected synchronized LowpanCredential() {
    }

    public protected synchronized LowpanCredential(byte[] masterKey, int keyIndex) {
        setMasterKey(masterKey, keyIndex);
    }

    public protected synchronized LowpanCredential(byte[] masterKey) {
        setMasterKey(masterKey);
    }

    private protected static synchronized LowpanCredential createMasterKey(byte[] masterKey) {
        return new LowpanCredential(masterKey);
    }

    private protected static synchronized LowpanCredential createMasterKey(byte[] masterKey, int keyIndex) {
        return new LowpanCredential(masterKey, keyIndex);
    }

    public private protected synchronized void setMasterKey(byte[] masterKey) {
        if (masterKey != null) {
            masterKey = (byte[]) masterKey.clone();
        }
        this.mMasterKey = masterKey;
    }

    public private protected synchronized void setMasterKeyIndex(int keyIndex) {
        this.mMasterKeyIndex = keyIndex;
    }

    public private protected synchronized void setMasterKey(byte[] masterKey, int keyIndex) {
        setMasterKey(masterKey);
        setMasterKeyIndex(keyIndex);
    }

    private protected synchronized byte[] getMasterKey() {
        if (this.mMasterKey != null) {
            return (byte[]) this.mMasterKey.clone();
        }
        return null;
    }

    private protected synchronized int getMasterKeyIndex() {
        return this.mMasterKeyIndex;
    }

    private protected synchronized boolean isMasterKey() {
        return this.mMasterKey != null;
    }

    private protected synchronized String toSensitiveString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<LowpanCredential");
        if (isMasterKey()) {
            sb.append(" MasterKey:");
            sb.append(HexDump.toHexString(this.mMasterKey));
            if (this.mMasterKeyIndex != 0) {
                sb.append(", Index:");
                sb.append(this.mMasterKeyIndex);
            }
        } else {
            sb.append(" empty");
        }
        sb.append(">");
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<LowpanCredential");
        if (isMasterKey()) {
            sb.append(" MasterKey");
            if (this.mMasterKeyIndex != 0) {
                sb.append(", Index:");
                sb.append(this.mMasterKeyIndex);
            }
        } else {
            sb.append(" empty");
        }
        sb.append(">");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof LowpanCredential) {
            LowpanCredential rhs = (LowpanCredential) obj;
            return Arrays.equals(this.mMasterKey, rhs.mMasterKey) && this.mMasterKeyIndex == rhs.mMasterKeyIndex;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(Arrays.hashCode(this.mMasterKey)), Integer.valueOf(this.mMasterKeyIndex));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.mMasterKey);
        dest.writeInt(this.mMasterKeyIndex);
    }
}
