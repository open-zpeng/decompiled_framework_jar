package android.hardware.radio.V1_0;

import com.android.internal.telephony.IccCardConstants;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class CallPresentation {
    public static final int ALLOWED = 0;
    public static final int PAYPHONE = 3;
    public static final int RESTRICTED = 1;
    public static final int UNKNOWN = 2;

    public static final String toString(int o) {
        if (o == 0) {
            return "ALLOWED";
        }
        if (o == 1) {
            return "RESTRICTED";
        }
        if (o == 2) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        if (o == 3) {
            return "PAYPHONE";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("ALLOWED");
        if ((o & 1) == 1) {
            list.add("RESTRICTED");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add(IccCardConstants.INTENT_VALUE_ICC_UNKNOWN);
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("PAYPHONE");
            flipped |= 3;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
