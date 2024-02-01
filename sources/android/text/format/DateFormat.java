package android.text.format;

import android.content.Context;
import android.provider.Settings;
import android.telephony.NetworkScanRequest;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import com.android.internal.content.NativeLibraryHelper;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import libcore.icu.ICU;
import libcore.icu.LocaleData;
/* loaded from: classes2.dex */
public class DateFormat {
    @Deprecated
    private protected static final char AM_PM = 'a';
    @Deprecated
    private protected static final char CAPITAL_AM_PM = 'A';
    @Deprecated
    private protected static final char DATE = 'd';
    @Deprecated
    private protected static final char DAY = 'E';
    @Deprecated
    private protected static final char HOUR = 'h';
    @Deprecated
    private protected static final char HOUR_OF_DAY = 'k';
    @Deprecated
    private protected static final char MINUTE = 'm';
    @Deprecated
    private protected static final char MONTH = 'M';
    @Deprecated
    private protected static final char QUOTE = '\'';
    @Deprecated
    private protected static final char SECONDS = 's';
    @Deprecated
    private protected static final char STANDALONE_MONTH = 'L';
    @Deprecated
    private protected static final char TIME_ZONE = 'z';
    @Deprecated
    private protected static final char YEAR = 'y';
    private static boolean sIs24Hour;
    private static Locale sIs24HourLocale;
    private static final Object sLocaleLock = new Object();

    public static boolean is24HourFormat(Context context) {
        return is24HourFormat(context, context.getUserId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean is24HourFormat(Context context, int userHandle) {
        String value = Settings.System.getStringForUser(context.getContentResolver(), Settings.System.TIME_12_24, userHandle);
        if (value != null) {
            return value.equals("24");
        }
        return is24HourLocale(context.getResources().getConfiguration().locale);
    }

    public static synchronized boolean is24HourLocale(Locale locale) {
        boolean is24Hour;
        synchronized (sLocaleLock) {
            if (sIs24HourLocale != null && sIs24HourLocale.equals(locale)) {
                return sIs24Hour;
            }
            java.text.DateFormat natural = java.text.DateFormat.getTimeInstance(1, locale);
            if (natural instanceof SimpleDateFormat) {
                SimpleDateFormat sdf = (SimpleDateFormat) natural;
                String pattern = sdf.toPattern();
                is24Hour = hasDesignator(pattern, 'H');
            } else {
                is24Hour = false;
            }
            boolean is24Hour2 = is24Hour;
            synchronized (sLocaleLock) {
                sIs24HourLocale = locale;
                sIs24Hour = is24Hour2;
            }
            return is24Hour2;
        }
    }

    public static String getBestDateTimePattern(Locale locale, String skeleton) {
        return ICU.getBestDateTimePattern(skeleton, locale);
    }

    public static java.text.DateFormat getTimeFormat(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return new SimpleDateFormat(getTimeFormatString(context), locale);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getTimeFormatString(Context context) {
        return getTimeFormatString(context, context.getUserId());
    }

    private protected static String getTimeFormatString(Context context, int userHandle) {
        LocaleData d = LocaleData.get(context.getResources().getConfiguration().locale);
        return is24HourFormat(context, userHandle) ? d.timeFormat_Hm : d.timeFormat_hm;
    }

    public static java.text.DateFormat getDateFormat(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return java.text.DateFormat.getDateInstance(3, locale);
    }

    public static java.text.DateFormat getLongDateFormat(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return java.text.DateFormat.getDateInstance(1, locale);
    }

    public static java.text.DateFormat getMediumDateFormat(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return java.text.DateFormat.getDateInstance(2, locale);
    }

    public static char[] getDateFormatOrder(Context context) {
        return ICU.getDateFormatOrder(getDateFormatString(context));
    }

    private static synchronized String getDateFormatString(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(3, locale);
        if (df instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) df).toPattern();
        }
        throw new AssertionError("!(df instanceof SimpleDateFormat)");
    }

    public static CharSequence format(CharSequence inFormat, long inTimeInMillis) {
        return format(inFormat, new Date(inTimeInMillis));
    }

    public static CharSequence format(CharSequence inFormat, Date inDate) {
        Calendar c = new GregorianCalendar();
        c.setTime(inDate);
        return format(inFormat, c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasSeconds(CharSequence inFormat) {
        return hasDesignator(inFormat, 's');
    }

    private protected static boolean hasDesignator(CharSequence inFormat, char designator) {
        if (inFormat == null) {
            return false;
        }
        int length = inFormat.length();
        boolean insideQuote = false;
        for (int i = 0; i < length; i++) {
            char c = inFormat.charAt(i);
            if (c == '\'') {
                insideQuote = insideQuote ? false : true;
            } else if (!insideQuote && c == designator) {
                return true;
            }
        }
        return false;
    }

    public static CharSequence format(CharSequence inFormat, Calendar inDate) {
        int count;
        String replacement;
        SpannableStringBuilder s = new SpannableStringBuilder(inFormat);
        LocaleData localeData = LocaleData.get(Locale.getDefault());
        int len = inFormat.length();
        int len2 = len;
        for (int len3 = 0; len3 < len2; len3 += count) {
            count = 1;
            int c = s.charAt(len3);
            if (c == 39) {
                count = appendQuotedText(s, len3);
                len2 = s.length();
            } else {
                while (len3 + count < len2 && s.charAt(len3 + count) == c) {
                    count++;
                }
                switch (c) {
                    case 65:
                    case 97:
                        replacement = localeData.amPm[inDate.get(9) - 0];
                        break;
                    case 69:
                    case 99:
                        replacement = getDayOfWeekString(localeData, inDate.get(7), count, c);
                        break;
                    case 72:
                    case 107:
                        replacement = zeroPad(inDate.get(11), count);
                        break;
                    case 75:
                    case 104:
                        int hour = inDate.get(10);
                        if (c == 104 && hour == 0) {
                            hour = 12;
                        }
                        replacement = zeroPad(hour, count);
                        break;
                    case 76:
                    case 77:
                        replacement = getMonthString(localeData, inDate.get(2), count, c);
                        break;
                    case 100:
                        replacement = zeroPad(inDate.get(5), count);
                        break;
                    case 109:
                        replacement = zeroPad(inDate.get(12), count);
                        break;
                    case 115:
                        replacement = zeroPad(inDate.get(13), count);
                        break;
                    case 121:
                        replacement = getYearString(inDate.get(1), count);
                        break;
                    case 122:
                        replacement = getTimeZoneString(inDate, count);
                        break;
                    default:
                        replacement = null;
                        break;
                }
                if (replacement != null) {
                    s.replace(len3, len3 + count, (CharSequence) replacement);
                    count = replacement.length();
                    len2 = s.length();
                }
            }
        }
        if (inFormat instanceof Spanned) {
            return new SpannedString(s);
        }
        return s.toString();
    }

    private static synchronized String getDayOfWeekString(LocaleData ld, int day, int count, int kind) {
        boolean standalone = kind == 99;
        return count == 5 ? standalone ? ld.tinyStandAloneWeekdayNames[day] : ld.tinyWeekdayNames[day] : count == 4 ? standalone ? ld.longStandAloneWeekdayNames[day] : ld.longWeekdayNames[day] : standalone ? ld.shortStandAloneWeekdayNames[day] : ld.shortWeekdayNames[day];
    }

    private static synchronized String getMonthString(LocaleData ld, int month, int count, int kind) {
        boolean standalone = kind == 76;
        if (count == 5) {
            return standalone ? ld.tinyStandAloneMonthNames[month] : ld.tinyMonthNames[month];
        } else if (count == 4) {
            return standalone ? ld.longStandAloneMonthNames[month] : ld.longMonthNames[month];
        } else if (count == 3) {
            return standalone ? ld.shortStandAloneMonthNames[month] : ld.shortMonthNames[month];
        } else {
            return zeroPad(month + 1, count);
        }
    }

    private static synchronized String getTimeZoneString(Calendar inDate, int count) {
        TimeZone tz = inDate.getTimeZone();
        if (count < 2) {
            return formatZoneOffset(inDate.get(16) + inDate.get(15), count);
        }
        boolean dst = inDate.get(16) != 0;
        return tz.getDisplayName(dst, 0);
    }

    private static synchronized String formatZoneOffset(int offset, int count) {
        int offset2 = offset / 1000;
        StringBuilder tb = new StringBuilder();
        if (offset2 < 0) {
            tb.insert(0, NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
            offset2 = -offset2;
        } else {
            tb.insert(0, "+");
        }
        int hours = offset2 / NetworkScanRequest.MAX_SEARCH_MAX_SEC;
        int minutes = (offset2 % NetworkScanRequest.MAX_SEARCH_MAX_SEC) / 60;
        tb.append(zeroPad(hours, 2));
        tb.append(zeroPad(minutes, 2));
        return tb.toString();
    }

    private static synchronized String getYearString(int year, int count) {
        return count <= 2 ? zeroPad(year % 100, 2) : String.format(Locale.getDefault(), "%d", Integer.valueOf(year));
    }

    public static synchronized int appendQuotedText(SpannableStringBuilder formatString, int index) {
        int length = formatString.length();
        if (index + 1 < length && formatString.charAt(index + 1) == '\'') {
            formatString.delete(index, index + 1);
            return 1;
        }
        int count = 0;
        formatString.delete(index, index + 1);
        int length2 = length - 1;
        while (index < length2) {
            char c = formatString.charAt(index);
            if (c == '\'') {
                if (index + 1 < length2 && formatString.charAt(index + 1) == '\'') {
                    formatString.delete(index, index + 1);
                    length2--;
                    count++;
                    index++;
                } else {
                    formatString.delete(index, index + 1);
                    break;
                }
            } else {
                index++;
                count++;
            }
        }
        return count;
    }

    private static synchronized String zeroPad(int inValue, int inMinDigits) {
        Locale locale = Locale.getDefault();
        return String.format(locale, "%0" + inMinDigits + "d", Integer.valueOf(inValue));
    }
}
