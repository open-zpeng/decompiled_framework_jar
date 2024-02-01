package android.sysprop;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.SmsManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

/* loaded from: classes2.dex */
public final class DisplayProperties {
    private DisplayProperties() {
    }

    private static Boolean tryParseBoolean(String str) {
        char c;
        String lowerCase = str.toLowerCase(Locale.US);
        int hashCode = lowerCase.hashCode();
        if (hashCode == 48) {
            if (lowerCase.equals(WifiEnterpriseConfig.ENGINE_DISABLE)) {
                c = 2;
            }
            c = 65535;
        } else if (hashCode == 49) {
            if (lowerCase.equals(WifiEnterpriseConfig.ENGINE_ENABLE)) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode != 3569038) {
            if (hashCode == 97196323 && lowerCase.equals("false")) {
                c = 3;
            }
            c = 65535;
        } else {
            if (lowerCase.equals("true")) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0 || c == 1) {
            return Boolean.TRUE;
        }
        if (c == 2 || c == 3) {
            return Boolean.FALSE;
        }
        return null;
    }

    private static Integer tryParseInteger(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Long tryParseLong(String str) {
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double tryParseDouble(String str) {
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String tryParseString(String str) {
        if ("".equals(str)) {
            return null;
        }
        return str;
    }

    private static <T extends Enum<T>> T tryParseEnum(Class<T> enumType, String str) {
        try {
            return (T) Enum.valueOf(enumType, str.toUpperCase(Locale.US));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static <T> List<T> tryParseList(Function<String, T> elementParser, String str) {
        String[] split;
        if ("".equals(str)) {
            return new ArrayList();
        }
        List<T> ret = new ArrayList<>();
        for (String element : str.split(SmsManager.REGEX_PREFIX_DELIMITER)) {
            ret.add(elementParser.apply(element));
        }
        return ret;
    }

    private static <T extends Enum<T>> List<T> tryParseEnumList(Class<T> enumType, String str) {
        String[] split;
        if ("".equals(str)) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (String element : str.split(SmsManager.REGEX_PREFIX_DELIMITER)) {
            arrayList.add(tryParseEnum(enumType, element));
        }
        return arrayList;
    }

    private static <T> String formatList(List<T> list) {
        StringJoiner joiner = new StringJoiner(SmsManager.REGEX_PREFIX_DELIMITER);
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T element = it.next();
            joiner.add(element == null ? "" : element.toString());
        }
        return joiner.toString();
    }

    private static <T extends Enum<T>> String formatEnumList(List<T> list, Function<T, String> elementFormatter) {
        StringJoiner joiner = new StringJoiner(SmsManager.REGEX_PREFIX_DELIMITER);
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T element = it.next();
            joiner.add(element == null ? "" : elementFormatter.apply(element));
        }
        return joiner.toString();
    }

    public static Optional<Boolean> debug_force_msaa() {
        String value = SystemProperties.get("debug.egl.force_msaa");
        return Optional.ofNullable(tryParseBoolean(value));
    }

    public static void debug_force_msaa(Boolean value) {
        SystemProperties.set("debug.egl.force_msaa", value == null ? "" : value.toString());
    }

    public static Optional<String> debug_opengl_trace() {
        String value = SystemProperties.get("debug.egl.trace");
        return Optional.ofNullable(tryParseString(value));
    }

    public static void debug_opengl_trace(String value) {
        SystemProperties.set("debug.egl.trace", value == null ? "" : value.toString());
    }

    public static Optional<Boolean> debug_force_rtl() {
        String value = SystemProperties.get(Settings.Global.DEVELOPMENT_FORCE_RTL);
        return Optional.ofNullable(tryParseBoolean(value));
    }

    public static void debug_force_rtl(Boolean value) {
        SystemProperties.set(Settings.Global.DEVELOPMENT_FORCE_RTL, value == null ? "" : value.toString());
    }

    public static Optional<Boolean> debug_layout() {
        String value = SystemProperties.get("debug.layout");
        return Optional.ofNullable(tryParseBoolean(value));
    }

    public static void debug_layout(Boolean value) {
        SystemProperties.set("debug.layout", value == null ? "" : value.toString());
    }
}
