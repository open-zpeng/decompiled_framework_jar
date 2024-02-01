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

    /* JADX WARN: Code restructure failed: missing block: B:160:0x02fc, code lost:
        if (r23.getMonth() < 0) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0302, code lost:
        if (r23.getMonth() < 12) goto L100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0305, code lost:
        r16 = r21.localeData.shortMonthNames[r23.getMonth()];
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0311, code lost:
        modifyAndAppend(r16, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0316, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0317, code lost:
        r7 = r23.getYear();
        r13 = r23.getYearDay();
        r15 = r23.getWeekDay();
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x0323, code lost:
        r17 = 366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x032b, code lost:
        if (isLeap(r7) == false) goto L159;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x032d, code lost:
        r16 = 366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x0330, code lost:
        r16 = 365;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0332, code lost:
        r9 = (((r13 + 11) - r15) % 7) - 3;
        r8 = r9 - (r16 % 7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x033f, code lost:
        if (r8 >= (-3)) goto L136;
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x0341, code lost:
        r8 = r8 + 7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x0345, code lost:
        if (r13 < (r8 + r16)) goto L138;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x0347, code lost:
        r7 = r7 + 1;
        r6 = 1;
        r14 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x034c, code lost:
        if (r13 < r9) goto L139;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x034e, code lost:
        r14 = 1;
        r6 = ((r13 - r9) / 7) + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x0356, code lost:
        if (r5 != 86) goto L151;
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x0358, code lost:
        r8 = r21.numberFormatter;
        r9 = getFormat(r4, "%02d", "%2d", "%d", "%02d");
        r10 = new java.lang.Object[r14];
        r10[0] = java.lang.Integer.valueOf(r6);
        r8.format(r9, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x036e, code lost:
        if (r5 != 103) goto L155;
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x0370, code lost:
        outputYear(r7, false, r14, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x0374, code lost:
        outputYear(r7, r14, r14, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x0377, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x0378, code lost:
        r7 = r7 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x0380, code lost:
        if (isLeap(r7) == false) goto L141;
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x0383, code lost:
        r17 = 365;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0385, code lost:
        r13 = r13 + r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:?, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:?, code lost:
        return false;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean handleToken(java.nio.CharBuffer r22, libcore.util.ZoneInfo.WallTime r23, libcore.util.ZoneInfo r24) {
        /*
            Method dump skipped, instructions count: 1094
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.format.TimeFormatter.handleToken(java.nio.CharBuffer, libcore.util.ZoneInfo$WallTime, libcore.util.ZoneInfo):boolean");
    }

    private void modifyAndAppend(CharSequence str, int modifier) {
        if (modifier == -1) {
            for (int i = 0; i < str.length(); i++) {
                this.outputBuilder.append(brokenToLower(str.charAt(i)));
            }
        } else if (modifier == 35) {
            for (int i2 = 0; i2 < str.length(); i2++) {
                char c = str.charAt(i2);
                if (brokenIsUpper(c)) {
                    c = brokenToLower(c);
                } else if (brokenIsLower(c)) {
                    c = brokenToUpper(c);
                }
                this.outputBuilder.append(c);
            }
        } else {
            if (modifier == 94) {
                for (int i3 = 0; i3 < str.length(); i3++) {
                    this.outputBuilder.append(brokenToUpper(str.charAt(i3)));
                }
                return;
            }
            this.outputBuilder.append(str);
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
