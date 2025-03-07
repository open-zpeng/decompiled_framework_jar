package android.security.keymaster;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class KeymasterBlob implements Parcelable {
    @UnsupportedAppUsage
    public static final Parcelable.Creator<KeymasterBlob> CREATOR = new Parcelable.Creator<KeymasterBlob>() { // from class: android.security.keymaster.KeymasterBlob.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KeymasterBlob createFromParcel(Parcel in) {
            return new KeymasterBlob(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KeymasterBlob[] newArray(int length) {
            return new KeymasterBlob[length];
        }
    };
    public byte[] blob;

    public KeymasterBlob(byte[] blob) {
        this.blob = blob;
    }

    protected KeymasterBlob(Parcel in) {
        this.blob = in.createByteArray();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeByteArray(this.blob);
    }
}
