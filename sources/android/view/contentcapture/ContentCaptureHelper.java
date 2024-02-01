package android.view.contentcapture;

import android.os.Build;
import android.provider.DeviceConfig;
import android.util.ArraySet;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* loaded from: classes3.dex */
public final class ContentCaptureHelper {
    private static final String TAG = ContentCaptureHelper.class.getSimpleName();
    public static boolean sVerbose = false;
    public static boolean sDebug = true;

    public static String getSanitizedString(CharSequence text) {
        if (text == null) {
            return null;
        }
        return text.length() + "_chars";
    }

    public static int getDefaultLoggingLevel() {
        return Build.IS_DEBUGGABLE ? 1 : 0;
    }

    public static void setLoggingLevel() {
        int defaultLevel = getDefaultLoggingLevel();
        int level = DeviceConfig.getInt("content_capture", ContentCaptureManager.DEVICE_CONFIG_PROPERTY_LOGGING_LEVEL, defaultLevel);
        setLoggingLevel(level);
    }

    public static void setLoggingLevel(int level) {
        String str = TAG;
        Log.i(str, "Setting logging level to " + getLoggingLevelAsString(level));
        sDebug = false;
        sVerbose = false;
        if (level != 0) {
            if (level != 1) {
                if (level == 2) {
                    sVerbose = true;
                } else {
                    String str2 = TAG;
                    Log.w(str2, "setLoggingLevel(): invalud level: " + level);
                    return;
                }
            }
            sDebug = true;
        }
    }

    public static String getLoggingLevelAsString(int level) {
        if (level != 0) {
            if (level != 1) {
                if (level == 2) {
                    return "VERBOSE";
                }
                return "UNKNOWN-" + level;
            }
            return "DEBUG";
        }
        return "OFF";
    }

    public static <T> ArrayList<T> toList(Set<T> set) {
        if (set == null) {
            return null;
        }
        return new ArrayList<>(set);
    }

    public static <T> ArraySet<T> toSet(List<T> list) {
        if (list == null) {
            return null;
        }
        return new ArraySet<>(list);
    }

    private ContentCaptureHelper() {
        throw new UnsupportedOperationException("contains only static methods");
    }
}
