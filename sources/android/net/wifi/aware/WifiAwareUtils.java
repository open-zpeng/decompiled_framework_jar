package android.net.wifi.aware;

import android.content.Context;
/* loaded from: classes2.dex */
public class WifiAwareUtils {
    public static synchronized void validateServiceName(byte[] serviceNameData) throws IllegalArgumentException {
        if (serviceNameData == null) {
            throw new IllegalArgumentException("Invalid service name - null");
        }
        if (serviceNameData.length < 1 || serviceNameData.length > 255) {
            throw new IllegalArgumentException("Invalid service name length - must be between 1 and 255 bytes (UTF-8 encoding)");
        }
        for (byte b : serviceNameData) {
            if ((b & 128) == 0 && ((b < 48 || b > 57) && ((b < 97 || b > 122) && ((b < 65 || b > 90) && b != 45 && b != 46)))) {
                throw new IllegalArgumentException("Invalid service name - illegal characters, allowed = (0-9, a-z,A-Z, -, .)");
            }
        }
    }

    public static synchronized boolean validatePassphrase(String passphrase) {
        if (passphrase == null || passphrase.length() < 8 || passphrase.length() > 63) {
            return false;
        }
        return true;
    }

    public static synchronized boolean validatePmk(byte[] pmk) {
        if (pmk == null || pmk.length != 32) {
            return false;
        }
        return true;
    }

    public static synchronized boolean isLegacyVersion(Context context, int minVersion) {
        if (context.getPackageManager().getApplicationInfo(context.getOpPackageName(), 0).targetSdkVersion >= minVersion) {
            return false;
        }
        return true;
    }
}
