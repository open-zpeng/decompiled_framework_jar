package android.security.keymaster;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class KeymasterIntArgument extends KeymasterArgument {
    @UnsupportedAppUsage
    public final int value;

    @UnsupportedAppUsage
    public KeymasterIntArgument(int tag, int value) {
        super(tag);
        int tagType = KeymasterDefs.getTagType(tag);
        if (tagType != 268435456 && tagType != 536870912 && tagType != 805306368 && tagType != 1073741824) {
            throw new IllegalArgumentException("Bad int tag " + tag);
        }
        this.value = value;
    }

    @UnsupportedAppUsage
    public KeymasterIntArgument(int tag, Parcel in) {
        super(tag);
        this.value = in.readInt();
    }

    @Override // android.security.keymaster.KeymasterArgument
    public void writeValue(Parcel out) {
        out.writeInt(this.value);
    }
}
