package android.text.format;

import android.annotation.UnsupportedAppUsage;
import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.icu.text.MeasureFormat;
import android.icu.util.Measure;
import android.icu.util.MeasureUnit;
import com.android.internal.R;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import libcore.icu.DateIntervalFormat;
import libcore.icu.LocaleData;
import libcore.icu.RelativeDateTimeFormatter;

/* loaded from: classes2.dex */
public class DateUtils {
    @Deprecated
    public static final String ABBREV_MONTH_FORMAT = "%b";
    public static final String ABBREV_WEEKDAY_FORMAT = "%a";
    public static final long DAY_IN_MILLIS = 86400000;
    @Deprecated
    public static final int FORMAT_12HOUR = 64;
    @Deprecated
    public static final int FORMAT_24HOUR = 128;
    public static final int FORMAT_ABBREV_ALL = 524288;
    public static final int FORMAT_ABBREV_MONTH = 65536;
    public static final int FORMAT_ABBREV_RELATIVE = 262144;
    public static final int FORMAT_ABBREV_TIME = 16384;
    public static final int FORMAT_ABBREV_WEEKDAY = 32768;
    @Deprecated
    public static final int FORMAT_CAP_AMPM = 256;
    @Deprecated
    public static final int FORMAT_CAP_MIDNIGHT = 4096;
    @Deprecated
    public static final int FORMAT_CAP_NOON = 1024;
    @Deprecated
    public static final int FORMAT_CAP_NOON_MIDNIGHT = 5120;
    public static final int FORMAT_NO_MIDNIGHT = 2048;
    public static final int FORMAT_NO_MONTH_DAY = 32;
    public static final int FORMAT_NO_NOON = 512;
    @Deprecated
    public static final int FORMAT_NO_NOON_MIDNIGHT = 2560;
    public static final int FORMAT_NO_YEAR = 8;
    public static final int FORMAT_NUMERIC_DATE = 131072;
    public static final int FORMAT_SHOW_DATE = 16;
    public static final int FORMAT_SHOW_TIME = 1;
    public static final int FORMAT_SHOW_WEEKDAY = 2;
    public static final int FORMAT_SHOW_YEAR = 4;
    @Deprecated
    public static final int FORMAT_UTC = 8192;
    public static final long HOUR_IN_MILLIS = 3600000;
    @Deprecated
    public static final String HOUR_MINUTE_24 = "%H:%M";
    @Deprecated
    public static final int LENGTH_LONG = 10;
    @Deprecated
    public static final int LENGTH_MEDIUM = 20;
    @Deprecated
    public static final int LENGTH_SHORT = 30;
    @Deprecated
    public static final int LENGTH_SHORTER = 40;
    @Deprecated
    public static final int LENGTH_SHORTEST = 50;
    public static final long MINUTE_IN_MILLIS = 60000;
    public static final String MONTH_DAY_FORMAT = "%-d";
    public static final String MONTH_FORMAT = "%B";
    public static final String NUMERIC_MONTH_FORMAT = "%m";
    public static final long SECOND_IN_MILLIS = 1000;
    public static final String WEEKDAY_FORMAT = "%A";
    public static final long WEEK_IN_MILLIS = 604800000;
    public static final String YEAR_FORMAT = "%Y";
    public static final String YEAR_FORMAT_TWO_DIGITS = "%g";
    public static final long YEAR_IN_MILLIS = 31449600000L;
    private static String sElapsedFormatHMMSS;
    private static String sElapsedFormatMMSS;
    private static Configuration sLastConfig;
    private static Time sNowTime;
    private static Time sThenTime;
    private static final Object sLock = new Object();
    @Deprecated
    public static final int[] sameYearTable = null;
    @Deprecated
    public static final int[] sameMonthTable = null;

    @Deprecated
    public static String getDayOfWeekString(int dayOfWeek, int abbrev) {
        String[] names;
        LocaleData d = LocaleData.get(Locale.getDefault());
        if (abbrev == 10) {
            names = d.longWeekdayNames;
        } else if (abbrev == 20) {
            names = d.shortWeekdayNames;
        } else if (abbrev == 30) {
            names = d.shortWeekdayNames;
        } else if (abbrev == 40) {
            names = d.shortWeekdayNames;
        } else if (abbrev == 50) {
            names = d.tinyWeekdayNames;
        } else {
            names = d.shortWeekdayNames;
        }
        return names[dayOfWeek];
    }

    @Deprecated
    public static String getAMPMString(int ampm) {
        return LocaleData.get(Locale.getDefault()).amPm[ampm + 0];
    }

    @Deprecated
    public static String getMonthString(int month, int abbrev) {
        String[] names;
        LocaleData d = LocaleData.get(Locale.getDefault());
        if (abbrev == 10) {
            names = d.longMonthNames;
        } else if (abbrev == 20) {
            names = d.shortMonthNames;
        } else if (abbrev == 30) {
            names = d.shortMonthNames;
        } else if (abbrev == 40) {
            names = d.shortMonthNames;
        } else if (abbrev == 50) {
            names = d.tinyMonthNames;
        } else {
            names = d.shortMonthNames;
        }
        return names[month];
    }

    public static CharSequence getRelativeTimeSpanString(long startTime) {
        return getRelativeTimeSpanString(startTime, System.currentTimeMillis(), 60000L);
    }

    public static CharSequence getRelativeTimeSpanString(long time, long now, long minResolution) {
        return getRelativeTimeSpanString(time, now, minResolution, 65556);
    }

    public static CharSequence getRelativeTimeSpanString(long time, long now, long minResolution, int flags) {
        return RelativeDateTimeFormatter.getRelativeTimeSpanString(Locale.getDefault(), TimeZone.getDefault(), time, now, minResolution, flags);
    }

    public static CharSequence getRelativeDateTimeString(Context c, long time, long minResolution, long transitionResolution, int flags) {
        int flags2 = flags;
        if ((flags2 & 193) == 1) {
            flags2 |= DateFormat.is24HourFormat(c) ? 128 : 64;
        }
        return RelativeDateTimeFormatter.getRelativeDateTimeString(Locale.getDefault(), TimeZone.getDefault(), time, System.currentTimeMillis(), minResolution, transitionResolution, flags2);
    }

    private static void initFormatStrings() {
        synchronized (sLock) {
            initFormatStringsLocked();
        }
    }

    private static void initFormatStringsLocked() {
        Resources r = Resources.getSystem();
        Configuration cfg = r.getConfiguration();
        Configuration configuration = sLastConfig;
        if (configuration == null || !configuration.equals(cfg)) {
            sLastConfig = cfg;
            sElapsedFormatMMSS = r.getString(R.string.elapsed_time_short_format_mm_ss);
            sElapsedFormatHMMSS = r.getString(R.string.elapsed_time_short_format_h_mm_ss);
        }
    }

    @UnsupportedAppUsage
    public static CharSequence formatDuration(long millis) {
        return formatDuration(millis, 10);
    }

    @UnsupportedAppUsage
    public static CharSequence formatDuration(long millis, int abbrev) {
        MeasureFormat.FormatWidth width;
        if (abbrev == 10) {
            width = MeasureFormat.FormatWidth.WIDE;
        } else if (abbrev == 20 || abbrev == 30 || abbrev == 40) {
            width = MeasureFormat.FormatWidth.SHORT;
        } else if (abbrev == 50) {
            width = MeasureFormat.FormatWidth.NARROW;
        } else {
            width = MeasureFormat.FormatWidth.WIDE;
        }
        MeasureFormat formatter = MeasureFormat.getInstance(Locale.getDefault(), width);
        if (millis >= 3600000) {
            int hours = (int) ((AlarmManager.INTERVAL_HALF_HOUR + millis) / 3600000);
            return formatter.format(new Measure(Integer.valueOf(hours), MeasureUnit.HOUR));
        } else if (millis >= 60000) {
            int minutes = (int) ((JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS + millis) / 60000);
            return formatter.format(new Measure(Integer.valueOf(minutes), MeasureUnit.MINUTE));
        } else {
            int seconds = (int) ((500 + millis) / 1000);
            return formatter.format(new Measure(Integer.valueOf(seconds), MeasureUnit.SECOND));
        }
    }

    public static String formatElapsedTime(long elapsedSeconds) {
        return formatElapsedTime(null, elapsedSeconds);
    }

    public static String formatElapsedTime(StringBuilder recycle, long elapsedSeconds) {
        long elapsedSeconds2;
        long hours = 0;
        long minutes = 0;
        if (elapsedSeconds < 3600) {
            elapsedSeconds2 = elapsedSeconds;
        } else {
            hours = elapsedSeconds / 3600;
            elapsedSeconds2 = elapsedSeconds - (3600 * hours);
        }
        if (elapsedSeconds2 >= 60) {
            minutes = elapsedSeconds2 / 60;
            elapsedSeconds2 -= 60 * minutes;
        }
        long seconds = elapsedSeconds2;
        StringBuilder sb = recycle;
        if (sb == null) {
            sb = new StringBuilder(8);
        } else {
            sb.setLength(0);
        }
        java.util.Formatter f = new java.util.Formatter(sb, Locale.getDefault());
        initFormatStrings();
        return hours > 0 ? f.format(sElapsedFormatHMMSS, Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)).toString() : f.format(sElapsedFormatMMSS, Long.valueOf(minutes), Long.valueOf(seconds)).toString();
    }

    public static final CharSequence formatSameDayTime(long then, long now, int dateStyle, int timeStyle) {
        java.text.DateFormat f;
        Calendar thenCal = new GregorianCalendar();
        thenCal.setTimeInMillis(then);
        Date thenDate = thenCal.getTime();
        Calendar nowCal = new GregorianCalendar();
        nowCal.setTimeInMillis(now);
        if (thenCal.get(1) == nowCal.get(1) && thenCal.get(2) == nowCal.get(2) && thenCal.get(5) == nowCal.get(5)) {
            f = java.text.DateFormat.getTimeInstance(timeStyle);
        } else {
            f = java.text.DateFormat.getDateInstance(dateStyle);
        }
        return f.format(thenDate);
    }

    public static boolean isToday(long when) {
        Time time = new Time();
        time.set(when);
        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;
        time.set(System.currentTimeMillis());
        return thenYear == time.year && thenMonth == time.month && thenMonthDay == time.monthDay;
    }

    public static String formatDateRange(Context context, long startMillis, long endMillis, int flags) {
        java.util.Formatter f = new java.util.Formatter(new StringBuilder(50), Locale.getDefault());
        return formatDateRange(context, f, startMillis, endMillis, flags).toString();
    }

    public static java.util.Formatter formatDateRange(Context context, java.util.Formatter formatter, long startMillis, long endMillis, int flags) {
        return formatDateRange(context, formatter, startMillis, endMillis, flags, null);
    }

    public static java.util.Formatter formatDateRange(Context context, java.util.Formatter formatter, long startMillis, long endMillis, int flags, String timeZone) {
        if ((flags & 193) == 1) {
            flags |= DateFormat.is24HourFormat(context) ? 128 : 64;
        }
        String range = DateIntervalFormat.formatDateRange(startMillis, endMillis, flags, timeZone);
        try {
            formatter.out().append(range);
            return formatter;
        } catch (IOException impossible) {
            throw new AssertionError(impossible);
        }
    }

    public static String formatDateTime(Context context, long millis, int flags) {
        return formatDateRange(context, millis, millis, flags);
    }

    public static CharSequence getRelativeTimeSpanString(Context c, long millis, boolean withPreposition) {
        String result;
        int flags;
        long now = System.currentTimeMillis();
        long span = Math.abs(now - millis);
        synchronized (DateUtils.class) {
            if (sNowTime == null) {
                sNowTime = new Time();
            }
            if (sThenTime == null) {
                sThenTime = new Time();
            }
            sNowTime.set(now);
            sThenTime.set(millis);
            if (span < 86400000 && sNowTime.weekDay == sThenTime.weekDay) {
                result = formatDateRange(c, millis, millis, 1);
                flags = R.string.preposition_for_time;
            } else if (sNowTime.year != sThenTime.year) {
                result = formatDateRange(c, millis, millis, 131092);
                flags = R.string.preposition_for_date;
            } else {
                result = formatDateRange(c, millis, millis, 65552);
                flags = R.string.preposition_for_date;
            }
            if (withPreposition) {
                Resources res = c.getResources();
                result = res.getString(flags, result);
            }
        }
        return result;
    }

    public static CharSequence getRelativeTimeSpanString(Context c, long millis) {
        return getRelativeTimeSpanString(c, millis, false);
    }
}
