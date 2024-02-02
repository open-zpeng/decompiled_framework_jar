package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
/* loaded from: classes2.dex */
public final class ImsiEncryptionInfo implements Parcelable {
    public static final Parcelable.Creator<ImsiEncryptionInfo> CREATOR = new Parcelable.Creator<ImsiEncryptionInfo>() { // from class: android.telephony.ImsiEncryptionInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ImsiEncryptionInfo createFromParcel(Parcel in) {
            return new ImsiEncryptionInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ImsiEncryptionInfo[] newArray(int size) {
            return new ImsiEncryptionInfo[size];
        }
    };
    private static final String LOG_TAG = "ImsiEncryptionInfo";
    private final Date expirationTime;
    private final String keyIdentifier;
    private final int keyType;
    private final String mcc;
    private final String mnc;
    private final PublicKey publicKey;

    public synchronized ImsiEncryptionInfo(String mcc, String mnc, int keyType, String keyIdentifier, byte[] key, Date expirationTime) {
        this(mcc, mnc, keyType, keyIdentifier, makeKeyObject(key), expirationTime);
    }

    public synchronized ImsiEncryptionInfo(String mcc, String mnc, int keyType, String keyIdentifier, PublicKey publicKey, Date expirationTime) {
        this.mcc = mcc;
        this.mnc = mnc;
        this.keyType = keyType;
        this.publicKey = publicKey;
        this.keyIdentifier = keyIdentifier;
        this.expirationTime = expirationTime;
    }

    public synchronized ImsiEncryptionInfo(Parcel in) {
        int length = in.readInt();
        byte[] b = new byte[length];
        in.readByteArray(b);
        this.publicKey = makeKeyObject(b);
        this.mcc = in.readString();
        this.mnc = in.readString();
        this.keyIdentifier = in.readString();
        this.keyType = in.readInt();
        this.expirationTime = new Date(in.readLong());
    }

    public synchronized String getMnc() {
        return this.mnc;
    }

    public synchronized String getMcc() {
        return this.mcc;
    }

    public synchronized String getKeyIdentifier() {
        return this.keyIdentifier;
    }

    public synchronized int getKeyType() {
        return this.keyType;
    }

    public synchronized PublicKey getPublicKey() {
        return this.publicKey;
    }

    public synchronized Date getExpirationTime() {
        return this.expirationTime;
    }

    private static synchronized PublicKey makeKeyObject(byte[] publicKeyBytes) {
        try {
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Log.e(LOG_TAG, "Error makeKeyObject: unable to convert into PublicKey", ex);
            throw new IllegalArgumentException();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        byte[] b = this.publicKey.getEncoded();
        dest.writeInt(b.length);
        dest.writeByteArray(b);
        dest.writeString(this.mcc);
        dest.writeString(this.mnc);
        dest.writeString(this.keyIdentifier);
        dest.writeInt(this.keyType);
        dest.writeLong(this.expirationTime.getTime());
    }

    public String toString() {
        return "[ImsiEncryptionInfo mcc=" + this.mcc + "mnc=" + this.mnc + "publicKey=" + this.publicKey + ", keyIdentifier=" + this.keyIdentifier + ", keyType=" + this.keyType + ", expirationTime=" + this.expirationTime + "]";
    }
}
