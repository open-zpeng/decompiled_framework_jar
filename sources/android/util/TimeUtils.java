package android.util;

import android.os.SystemClock;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import libcore.util.TimeZoneFinder;
import libcore.util.ZoneInfoDB;
/* loaded from: classes2.dex */
public class TimeUtils {
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    public static final long NANOS_PER_MS = 1000000;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static SimpleDateFormat sLoggingFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Object sFormatSync = new Object();
    private static char[] sFormatStr = new char[29];
    private static char[] sTmpFormatStr = new char[29];

    public static TimeZone getTimeZone(int offset, boolean dst, long when, String country) {
        android.icu.util.TimeZone icuTimeZone = getIcuTimeZone(offset, dst, when, country);
        if (icuTimeZone != null) {
            return TimeZone.getTimeZone(icuTimeZone.getID());
        }
        return null;
    }

    private static synchronized android.icu.util.TimeZone getIcuTimeZone(int offset, boolean dst, long when, String country) {
        if (country == null) {
            return null;
        }
        android.icu.util.TimeZone bias = android.icu.util.TimeZone.getDefault();
        return TimeZoneFinder.getInstance().lookupTimeZoneByCountryAndOffset(country, offset, dst, when, bias);
    }

    public static String getTimeZoneDatabaseVersion() {
        return ZoneInfoDB.getInstance().getVersion();
    }

    private static synchronized int accumField(int amt, int suffix, boolean always, int zeropad) {
        int num = 0;
        if (amt > 999) {
            while (amt != 0) {
                num++;
                amt /= 10;
            }
            return num + suffix;
        } else if (amt > 99 || (always && zeropad >= 3)) {
            return 3 + suffix;
        } else {
            if (amt > 9 || (always && zeropad >= 2)) {
                return 2 + suffix;
            }
            if (!always && amt <= 0) {
                return 0;
            }
            return 1 + suffix;
        }
    }

    private static synchronized int printFieldLocked(char[] formatStr, int amt, char suffix, int pos, boolean always, int zeropad) {
        if (always || amt > 0) {
            if (amt > 999) {
                int tmp = 0;
                while (amt != 0 && tmp < sTmpFormatStr.length) {
                    sTmpFormatStr[tmp] = (char) ((amt % 10) + 48);
                    tmp++;
                    amt /= 10;
                }
                for (int tmp2 = tmp - 1; tmp2 >= 0; tmp2--) {
                    formatStr[pos] = sTmpFormatStr[tmp2];
                    pos++;
                }
            } else {
                if ((always && zeropad >= 3) || amt > 99) {
                    int dig = amt / 100;
                    formatStr[pos] = (char) (dig + 48);
                    pos++;
                    amt -= dig * 100;
                }
                if ((always && zeropad >= 2) || amt > 9 || pos != pos) {
                    int dig2 = amt / 10;
                    formatStr[pos] = (char) (dig2 + 48);
                    pos++;
                    amt -= dig2 * 10;
                }
                formatStr[pos] = (char) (amt + 48);
                pos++;
            }
            formatStr[pos] = suffix;
            return pos + 1;
        }
        return pos;
    }

    /* JADX WARN: Code restructure failed: missing block: B:76:0x0135, code lost:
        if (r9 != r7) goto L70;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static synchronized int formatDurationLocked(long r27, int r29) {
        /*
            Method dump skipped, instructions count: 336
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.TimeUtils.formatDurationLocked(long, int):int");
    }

    public static synchronized void formatDuration(long duration, StringBuilder builder) {
        synchronized (sFormatSync) {
            int len = formatDurationLocked(duration, 0);
            builder.append(sFormatStr, 0, len);
        }
    }

    public static synchronized void formatDuration(long duration, StringBuilder builder, int fieldLen) {
        synchronized (sFormatSync) {
            int len = formatDurationLocked(duration, fieldLen);
            builder.append(sFormatStr, 0, len);
        }
    }

    private protected static void formatDuration(long duration, PrintWriter pw, int fieldLen) {
        synchronized (sFormatSync) {
            int len = formatDurationLocked(duration, fieldLen);
            pw.print(new String(sFormatStr, 0, len));
        }
    }

    public static synchronized String formatDuration(long duration) {
        String str;
        synchronized (sFormatSync) {
            int len = formatDurationLocked(duration, 0);
            str = new String(sFormatStr, 0, len);
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void formatDuration(long duration, PrintWriter pw) {
        formatDuration(duration, pw, 0);
    }

    public static synchronized void formatDuration(long time, long now, PrintWriter pw) {
        if (time == 0) {
            pw.print("--");
        } else {
            formatDuration(time - now, pw, 0);
        }
    }

    public static synchronized String formatUptime(long time) {
        long diff = time - SystemClock.uptimeMillis();
        if (diff > 0) {
            return time + " (in " + diff + " ms)";
        } else if (diff < 0) {
            return time + " (" + (-diff) + " ms ago)";
        } else {
            return time + " (now)";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String logTimeOfDay(long millis) {
        Calendar c = Calendar.getInstance();
        if (millis >= 0) {
            c.setTimeInMillis(millis);
            return String.format("%tm-%td %tH:%tM:%tS.%tL", c, c, c, c, c, c);
        }
        return Long.toString(millis);
    }

    public static synchronized String formatForLogging(long millis) {
        if (millis <= 0) {
            return "unknown";
        }
        return sLoggingFormat.format(new Date(millis));
    }
}
