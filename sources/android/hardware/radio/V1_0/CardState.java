package android.hardware.radio.V1_0;

import com.android.internal.telephony.IccCardConstants;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class CardState {
    public static final int ABSENT = 0;
    public static final int ERROR = 2;
    public static final int PRESENT = 1;
    public static final int RESTRICTED = 3;

    public static final String toString(int o) {
        if (o == 0) {
            return IccCardConstants.INTENT_VALUE_ICC_ABSENT;
        }
        if (o == 1) {
            return IccCardConstants.INTENT_VALUE_ICC_PRESENT;
        }
        if (o == 2) {
            return "ERROR";
        }
        if (o == 3) {
            return "RESTRICTED";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add(IccCardConstants.INTENT_VALUE_ICC_ABSENT);
        if ((o & 1) == 1) {
            list.add(IccCardConstants.INTENT_VALUE_ICC_PRESENT);
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("ERROR");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("RESTRICTED");
            flipped |= 3;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
