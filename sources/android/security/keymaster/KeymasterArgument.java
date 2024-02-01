package android.security.keymaster;

import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class KeymasterArgument implements Parcelable {
    public static final Parcelable.Creator<KeymasterArgument> CREATOR = new Parcelable.Creator<KeymasterArgument>() { // from class: android.security.keymaster.KeymasterArgument.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KeymasterArgument createFromParcel(Parcel in) {
            int pos = in.dataPosition();
            int tag = in.readInt();
            int tagType = KeymasterDefs.getTagType(tag);
            if (tagType != Integer.MIN_VALUE && tagType != -1879048192) {
                if (tagType != -1610612736) {
                    if (tagType == 268435456 || tagType == 536870912 || tagType == 805306368 || tagType == 1073741824) {
                        return new KeymasterIntArgument(tag, in);
                    }
                    if (tagType != 1342177280) {
                        if (tagType != 1610612736) {
                            if (tagType == 1879048192) {
                                return new KeymasterBooleanArgument(tag, in);
                            }
                            throw new ParcelFormatException("Bad tag: " + tag + " at " + pos);
                        }
                        return new KeymasterDateArgument(tag, in);
                    }
                }
                return new KeymasterLongArgument(tag, in);
            }
            return new KeymasterBlobArgument(tag, in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KeymasterArgument[] newArray(int size) {
            return new KeymasterArgument[size];
        }
    };
    public final int tag;

    public abstract synchronized void writeValue(Parcel parcel);

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized KeymasterArgument(int tag) {
        this.tag = tag;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.tag);
        writeValue(out);
    }
}
