package android.net.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.NetworkCapabilities;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import com.android.internal.R;

/* loaded from: classes2.dex */
public final class KeepaliveUtils {
    public static final String TAG = "KeepaliveUtils";

    /* loaded from: classes2.dex */
    public static class KeepaliveDeviceConfigurationException extends AndroidRuntimeException {
        public KeepaliveDeviceConfigurationException(String msg) {
            super(msg);
        }
    }

    public static int[] getSupportedKeepalives(Context context) {
        String[] res = null;
        try {
            res = context.getResources().getStringArray(R.array.config_networkSupportedKeepaliveCount);
        } catch (Resources.NotFoundException e) {
        }
        if (res == null) {
            throw new KeepaliveDeviceConfigurationException("invalid resource");
        }
        int[] ret = new int[8];
        for (String row : res) {
            if (TextUtils.isEmpty(row)) {
                throw new KeepaliveDeviceConfigurationException("Empty string");
            }
            String[] arr = row.split(SmsManager.REGEX_PREFIX_DELIMITER);
            if (arr.length != 2) {
                throw new KeepaliveDeviceConfigurationException("Invalid parameter length");
            }
            try {
                int transport = Integer.parseInt(arr[0]);
                int supported = Integer.parseInt(arr[1]);
                if (!NetworkCapabilities.isValidTransport(transport)) {
                    throw new KeepaliveDeviceConfigurationException("Invalid transport " + transport);
                } else if (supported < 0) {
                    throw new KeepaliveDeviceConfigurationException("Invalid supported count " + supported + " for " + NetworkCapabilities.transportNameOf(transport));
                } else {
                    ret[transport] = supported;
                }
            } catch (NumberFormatException e2) {
                throw new KeepaliveDeviceConfigurationException("Invalid number format");
            }
        }
        return ret;
    }

    public static int getSupportedKeepalivesForNetworkCapabilities(int[] supportedKeepalives, NetworkCapabilities nc) {
        int[] transports = nc.getTransportTypes();
        if (transports.length == 0) {
            return 0;
        }
        int supportedCount = supportedKeepalives[transports[0]];
        for (int transport : transports) {
            if (supportedCount > supportedKeepalives[transport]) {
                supportedCount = supportedKeepalives[transport];
            }
        }
        return supportedCount;
    }
}
