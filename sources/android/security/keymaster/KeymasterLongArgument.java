package android.security.keymaster;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class KeymasterLongArgument extends KeymasterArgument {
    @UnsupportedAppUsage
    public final long value;

    @UnsupportedAppUsage
    public KeymasterLongArgument(int tag, long value) {
        super(tag);
        int tagType = KeymasterDefs.getTagType(tag);
        if (tagType != -1610612736 && tagType != 1342177280) {
            throw new IllegalArgumentException("Bad long tag " + tag);
        }
        this.value = value;
    }

    @UnsupportedAppUsage
    public KeymasterLongArgument(int tag, Parcel in) {
        super(tag);
        this.value = in.readLong();
    }

    @Override // android.security.keymaster.KeymasterArgument
    public void writeValue(Parcel out) {
        out.writeLong(this.value);
    }
}
