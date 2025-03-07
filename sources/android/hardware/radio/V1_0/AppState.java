package android.hardware.radio.V1_0;

import com.android.internal.telephony.IccCardConstants;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class AppState {
    public static final int DETECTED = 1;
    public static final int PIN = 2;
    public static final int PUK = 3;
    public static final int READY = 5;
    public static final int SUBSCRIPTION_PERSO = 4;
    public static final int UNKNOWN = 0;

    public static final String toString(int o) {
        if (o == 0) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        if (o == 1) {
            return "DETECTED";
        }
        if (o == 2) {
            return IccCardConstants.INTENT_VALUE_LOCKED_ON_PIN;
        }
        if (o == 3) {
            return IccCardConstants.INTENT_VALUE_LOCKED_ON_PUK;
        }
        if (o == 4) {
            return "SUBSCRIPTION_PERSO";
        }
        if (o == 5) {
            return IccCardConstants.INTENT_VALUE_ICC_READY;
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add(IccCardConstants.INTENT_VALUE_ICC_UNKNOWN);
        if ((o & 1) == 1) {
            list.add("DETECTED");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add(IccCardConstants.INTENT_VALUE_LOCKED_ON_PIN);
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add(IccCardConstants.INTENT_VALUE_LOCKED_ON_PUK);
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("SUBSCRIPTION_PERSO");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add(IccCardConstants.INTENT_VALUE_ICC_READY);
            flipped |= 5;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
