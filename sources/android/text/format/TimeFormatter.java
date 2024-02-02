package android.text.format;

import android.content.res.Resources;
import com.android.internal.R;
import java.nio.CharBuffer;
import java.util.Locale;
import libcore.icu.LocaleData;
import libcore.util.ZoneInfo;
/* loaded from: classes2.dex */
class TimeFormatter {
    private static final int DAYSPERLYEAR = 366;
    private static final int DAYSPERNYEAR = 365;
    private static final int DAYSPERWEEK = 7;
    private static final int FORCE_LOWER_CASE = -1;
    private static final int HOURSPERDAY = 24;
    private static final int MINSPERHOUR = 60;
    private static final int MONSPERYEAR = 12;
    private static final int SECSPERMIN = 60;
    private static String sDateOnlyFormat;
    private static String sDateTimeFormat;
    private static Locale sLocale;
    private static LocaleData sLocaleData;
    private static String sTimeOnlyFormat;
    private final String dateOnlyFormat;
    private final String dateTimeFormat;
    private final LocaleData localeData;
    private java.util.Formatter numberFormatter;
    private StringBuilder outputBuilder;
    private final String timeOnlyFormat;

    public TimeFormatter() {
        synchronized (TimeFormatter.class) {
            Locale locale = Locale.getDefault();
            if (sLocale == null || !locale.equals(sLocale)) {
                sLocale = locale;
                sLocaleData = LocaleData.get(locale);
                Resources r = Resources.getSystem();
                sTimeOnlyFormat = r.getString(R.string.time_of_day);
                sDateOnlyFormat = r.getString(R.string.month_day_year);
                sDateTimeFormat = r.getString(R.string.date_and_time);
            }
            this.dateTimeFormat = sDateTimeFormat;
            this.timeOnlyFormat = sTimeOnlyFormat;
            this.dateOnlyFormat = sDateOnlyFormat;
            this.localeData = sLocaleData;
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.StringBuilder, java.util.Formatter] */
    public String format(String pattern, ZoneInfo.WallTime wallTime, ZoneInfo zoneInfo) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            this.outputBuilder = stringBuilder;
            this.numberFormatter = new java.util.Formatter(stringBuilder, Locale.US);
            formatInternal(pattern, wallTime, zoneInfo);
            String result = stringBuilder.toString();
            if (this.localeData.zeroDigit != '0') {
                result = localizeDigits(result);
            }
            return result;
        } finally {
            this.outputBuilder = null;
            this.numberFormatter = null;
        }
    }

    private String localizeDigits(String s) {
        int length = s.length();
        int offsetToLocalizedDigits = this.localeData.zeroDigit - '0';
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char ch = s.charAt(i);
            if (ch >= '0' && ch <= '9') {
                ch = (char) (ch + offsetToLocalizedDigits);
            }
            result.append(ch);
        }
        return result.toString();
    }

    private void formatInternal(String pattern, ZoneInfo.WallTime wallTime, ZoneInfo zoneInfo) {
        CharBuffer formatBuffer = CharBuffer.wrap(pattern);
        while (formatBuffer.remaining() > 0) {
            boolean outputCurrentChar = true;
            char currentChar = formatBuffer.get(formatBuffer.position());
            if (currentChar == '%') {
                outputCurrentChar = handleToken(formatBuffer, wallTime, zoneInfo);
            }
            if (outputCurrentChar) {
                this.outputBuilder.append(formatBuffer.get(formatBuffer.position()));
            }
            formatBuffer.position(formatBuffer.position() + 1);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:133:0x0325, code lost:
        r9 = r21.getYear();
        r10 = r21.getYearDay();
        r11 = r21.getWeekDay();
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x0335, code lost:
        if (isLeap(r9) == false) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0337, code lost:
        r12 = 366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x033a, code lost:
        r12 = 365;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x033c, code lost:
        r15 = (((r10 + 11) - r11) % 7) - 3;
        r13 = r15 - (r12 % 7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x0347, code lost:
        if (r13 >= (-3)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0349, code lost:
        r13 = r13 + 7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x034c, code lost:
        if (r10 < (r13 + r12)) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x034e, code lost:
        r9 = r9 + 1;
        r8 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0352, code lost:
        if (r10 < r15) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x0354, code lost:
        r8 = 1 + ((r10 - r15) / 7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x035d, code lost:
        if (r6 != 86) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x035f, code lost:
        r19.numberFormatter.format(getFormat(r5, "%02d", "%2d", "%d", "%02d"), java.lang.Integer.valueOf(r8));
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x037a, code lost:
        r14 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x037d, code lost:
        if (r6 != 103) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x037f, code lost:
        outputYear(r9, r14, true, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x0383, code lost:
        outputYear(r9, true, true, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0386, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x0387, code lost:
        r9 = r9 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x038d, code lost:
        if (isLeap(r9) == false) goto L112;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x038f, code lost:
        r17 = 366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x0392, code lost:
        r17 = 365;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x0394, code lost:
        r10 = r10 + r17;
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:250:?, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:251:?, code lost:
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x01e8, code lost:
        if (r21.getMonth() < 0) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x01ee, code lost:
        if (r21.getMonth() < 12) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x01f1, code lost:
        r7 = r19.localeData.shortMonthNames[r21.getMonth()];
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01fc, code lost:
        r7 = "?";
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x01fe, code lost:
        modifyAndAppend(r7, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0201, code lost:
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean handleToken(java.nio.CharBuffer r20, libcore.util.ZoneInfo.WallTime r21, libcore.util.ZoneInfo r22) {
        /*
            Method dump skipped, instructions count: 1186
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.format.TimeFormatter.handleToken(java.nio.CharBuffer, libcore.util.ZoneInfo$WallTime, libcore.util.ZoneInfo):boolean");
    }

    private void modifyAndAppend(CharSequence str, int modifier) {
        int i = 0;
        if (modifier == -1) {
            while (true) {
                int i2 = i;
                if (i2 < str.length()) {
                    this.outputBuilder.append(brokenToLower(str.charAt(i2)));
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else if (modifier == 35) {
            while (true) {
                int i3 = i;
                if (i3 < str.length()) {
                    char c = str.charAt(i3);
                    if (brokenIsUpper(c)) {
                        c = brokenToLower(c);
                    } else if (brokenIsLower(c)) {
                        c = brokenToUpper(c);
                    }
                    this.outputBuilder.append(c);
                    i = i3 + 1;
                } else {
                    return;
                }
            }
        } else if (modifier != 94) {
            this.outputBuilder.append(str);
        } else {
            while (true) {
                int i4 = i;
                if (i4 < str.length()) {
                    this.outputBuilder.append(brokenToUpper(str.charAt(i4)));
                    i = i4 + 1;
                } else {
                    return;
                }
            }
        }
    }

    private void outputYear(int value, boolean outputTop, boolean outputBottom, int modifier) {
        int trail = value % 100;
        int lead = (value / 100) + (trail / 100);
        int trail2 = trail % 100;
        if (trail2 < 0 && lead > 0) {
            trail2 += 100;
            lead--;
        } else if (lead < 0 && trail2 > 0) {
            trail2 -= 100;
            lead++;
        }
        if (outputTop) {
            if (lead == 0 && trail2 < 0) {
                this.outputBuilder.append("-0");
            } else {
                this.numberFormatter.format(getFormat(modifier, "%02d", "%2d", "%d", "%02d"), Integer.valueOf(lead));
            }
        }
        if (outputBottom) {
            int n = trail2 < 0 ? -trail2 : trail2;
            this.numberFormatter.format(getFormat(modifier, "%02d", "%2d", "%d", "%02d"), Integer.valueOf(n));
        }
    }

    private static String getFormat(int modifier, String normal, String underscore, String dash, String zero) {
        if (modifier != 45) {
            if (modifier != 48) {
                if (modifier == 95) {
                    return underscore;
                }
                return normal;
            }
            return zero;
        }
        return dash;
    }

    private static boolean isLeap(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    private static boolean brokenIsUpper(char toCheck) {
        return toCheck >= 'A' && toCheck <= 'Z';
    }

    private static boolean brokenIsLower(char toCheck) {
        return toCheck >= 'a' && toCheck <= 'z';
    }

    private static char brokenToLower(char input) {
        if (input >= 'A' && input <= 'Z') {
            return (char) ((input - 'A') + 97);
        }
        return input;
    }

    private static char brokenToUpper(char input) {
        if (input >= 'a' && input <= 'z') {
            return (char) ((input - 'a') + 65);
        }
        return input;
    }
}
