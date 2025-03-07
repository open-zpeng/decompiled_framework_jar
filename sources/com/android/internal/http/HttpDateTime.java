package com.android.internal.http;

import android.annotation.UnsupportedAppUsage;
import android.telephony.PreciseDisconnectCause;
import android.text.format.Time;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes3.dex */
public final class HttpDateTime {
    private static final String HTTP_DATE_RFC_REGEXP = "([0-9]{1,2})[- ]([A-Za-z]{3,9})[- ]([0-9]{2,4})[ ]([0-9]{1,2}:[0-9][0-9]:[0-9][0-9])";
    private static final Pattern HTTP_DATE_RFC_PATTERN = Pattern.compile(HTTP_DATE_RFC_REGEXP);
    private static final String HTTP_DATE_ANSIC_REGEXP = "[ ]([A-Za-z]{3,9})[ ]+([0-9]{1,2})[ ]([0-9]{1,2}:[0-9][0-9]:[0-9][0-9])[ ]([0-9]{2,4})";
    private static final Pattern HTTP_DATE_ANSIC_PATTERN = Pattern.compile(HTTP_DATE_ANSIC_REGEXP);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class TimeOfDay {
        int hour;
        int minute;
        int second;

        TimeOfDay(int h, int m, int s) {
            this.hour = h;
            this.minute = m;
            this.second = s;
        }
    }

    @UnsupportedAppUsage
    public static long parse(String timeString) throws IllegalArgumentException {
        int month;
        int date;
        int year;
        TimeOfDay timeOfDay;
        Matcher rfcMatcher = HTTP_DATE_RFC_PATTERN.matcher(timeString);
        if (rfcMatcher.find()) {
            date = getDate(rfcMatcher.group(1));
            month = getMonth(rfcMatcher.group(2));
            year = getYear(rfcMatcher.group(3));
            timeOfDay = getTime(rfcMatcher.group(4));
        } else {
            Matcher ansicMatcher = HTTP_DATE_ANSIC_PATTERN.matcher(timeString);
            if (ansicMatcher.find()) {
                month = getMonth(ansicMatcher.group(1));
                date = getDate(ansicMatcher.group(2));
                TimeOfDay timeOfDay2 = getTime(ansicMatcher.group(3));
                year = getYear(ansicMatcher.group(4));
                timeOfDay = timeOfDay2;
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (year >= 2038) {
            year = 2038;
            month = 0;
            date = 1;
        }
        Time time = new Time(Time.TIMEZONE_UTC);
        time.set(timeOfDay.second, timeOfDay.minute, timeOfDay.hour, date, month, year);
        return time.toMillis(false);
    }

    private static int getDate(String dateString) {
        if (dateString.length() == 2) {
            return ((dateString.charAt(0) - '0') * 10) + (dateString.charAt(1) - '0');
        }
        return dateString.charAt(0) - '0';
    }

    private static int getMonth(String monthString) {
        int hash = ((Character.toLowerCase(monthString.charAt(0)) + Character.toLowerCase(monthString.charAt(1))) + Character.toLowerCase(monthString.charAt(2))) - 291;
        if (hash != 9) {
            if (hash != 10) {
                if (hash != 22) {
                    if (hash != 26) {
                        if (hash != 29) {
                            if (hash != 32) {
                                if (hash != 40) {
                                    if (hash != 42) {
                                        if (hash != 48) {
                                            switch (hash) {
                                                case 35:
                                                    return 9;
                                                case 36:
                                                    return 4;
                                                case 37:
                                                    return 8;
                                                default:
                                                    throw new IllegalArgumentException();
                                            }
                                        }
                                        return 10;
                                    }
                                    return 5;
                                }
                                return 6;
                            }
                            return 3;
                        }
                        return 2;
                    }
                    return 7;
                }
                return 0;
            }
            return 1;
        }
        return 11;
    }

    private static int getYear(String yearString) {
        if (yearString.length() == 2) {
            int year = ((yearString.charAt(0) - '0') * 10) + (yearString.charAt(1) - '0');
            if (year >= 70) {
                return year + PreciseDisconnectCause.ECBM_NOT_SUPPORTED;
            }
            return year + 2000;
        }
        int year2 = yearString.length();
        if (year2 == 3) {
            int year3 = ((yearString.charAt(0) - '0') * 100) + ((yearString.charAt(1) - '0') * 10) + (yearString.charAt(2) - '0');
            return year3 + PreciseDisconnectCause.ECBM_NOT_SUPPORTED;
        }
        int year4 = yearString.length();
        if (year4 == 4) {
            return ((yearString.charAt(0) - '0') * 1000) + ((yearString.charAt(1) - '0') * 100) + ((yearString.charAt(2) - '0') * 10) + (yearString.charAt(3) - '0');
        }
        return 1970;
    }

    private static TimeOfDay getTime(String timeString) {
        int i;
        int i2;
        int i3;
        int i4 = 0 + 1;
        int hour = timeString.charAt(0) - 48;
        if (timeString.charAt(i4) != ':') {
            hour = (hour * 10) + (timeString.charAt(i4) - 48);
            i4++;
        }
        int i5 = i4 + 1 + 1;
        int minute = ((timeString.charAt(i) - 48) * 10) + (timeString.charAt(i5) - 48);
        int i6 = i5 + 1 + 1 + 1 + 1;
        int second = ((timeString.charAt(i2) - 48) * 10) + (timeString.charAt(i3) - 48);
        return new TimeOfDay(hour, minute, second);
    }
}
