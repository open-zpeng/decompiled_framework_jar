package android.net;

import java.net.InetAddress;
import libcore.net.InetAddressUtils;

/* loaded from: classes2.dex */
public class InetAddresses {
    private InetAddresses() {
    }

    public static boolean isNumericAddress(String address) {
        return InetAddressUtils.isNumericAddress(address);
    }

    public static InetAddress parseNumericAddress(String address) {
        return InetAddressUtils.parseNumericAddress(address);
    }
}
