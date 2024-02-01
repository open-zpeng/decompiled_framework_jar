package android.security;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class KeystoreArguments implements Parcelable {
    private protected static final Parcelable.Creator<KeystoreArguments> CREATOR = new Parcelable.Creator<KeystoreArguments>() { // from class: android.security.KeystoreArguments.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KeystoreArguments createFromParcel(Parcel in) {
            return new KeystoreArguments(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KeystoreArguments[] newArray(int size) {
            return new KeystoreArguments[size];
        }
    };
    public byte[][] args;

    public synchronized KeystoreArguments() {
        this.args = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public KeystoreArguments(byte[][] args) {
        this.args = args;
    }

    private synchronized KeystoreArguments(Parcel in) {
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        byte[][] bArr;
        if (this.args == null) {
            out.writeInt(0);
            return;
        }
        out.writeInt(this.args.length);
        for (byte[] arg : this.args) {
            out.writeByteArray(arg);
        }
    }

    private synchronized void readFromParcel(Parcel in) {
        int length = in.readInt();
        this.args = new byte[length];
        for (int i = 0; i < length; i++) {
            this.args[i] = in.createByteArray();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
