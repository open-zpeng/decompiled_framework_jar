package android.service.notification;

import android.service.notification.ZenModeConfig;
import android.util.ArraySet;
import android.util.Log;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
/* loaded from: classes2.dex */
public class ScheduleCalendar {
    public static final boolean DEBUG = Log.isLoggable("ConditionProviders", 3);
    public static final String TAG = "ScheduleCalendar";
    private ZenModeConfig.ScheduleInfo mSchedule;
    private final ArraySet<Integer> mDays = new ArraySet<>();
    private final Calendar mCalendar = Calendar.getInstance();

    public String toString() {
        return "ScheduleCalendar[mDays=" + this.mDays + ", mSchedule=" + this.mSchedule + "]";
    }

    public synchronized boolean exitAtAlarm() {
        return this.mSchedule.exitAtAlarm;
    }

    public synchronized void setSchedule(ZenModeConfig.ScheduleInfo schedule) {
        if (Objects.equals(this.mSchedule, schedule)) {
            return;
        }
        this.mSchedule = schedule;
        updateDays();
    }

    public synchronized void maybeSetNextAlarm(long now, long nextAlarm) {
        if (this.mSchedule != null && this.mSchedule.exitAtAlarm) {
            if (nextAlarm == 0) {
                this.mSchedule.nextAlarm = 0L;
            }
            if (nextAlarm > now) {
                if (this.mSchedule.nextAlarm == 0 || this.mSchedule.nextAlarm < now) {
                    this.mSchedule.nextAlarm = nextAlarm;
                    return;
                }
                this.mSchedule.nextAlarm = Math.min(this.mSchedule.nextAlarm, nextAlarm);
            } else if (this.mSchedule.nextAlarm < now) {
                if (DEBUG) {
                    Log.d(TAG, "All alarms are in the past " + this.mSchedule.nextAlarm);
                }
                this.mSchedule.nextAlarm = 0L;
            }
        }
    }

    public synchronized void setTimeZone(TimeZone tz) {
        this.mCalendar.setTimeZone(tz);
    }

    public synchronized long getNextChangeTime(long now) {
        if (this.mSchedule == null) {
            return 0L;
        }
        long nextStart = getNextTime(now, this.mSchedule.startHour, this.mSchedule.startMinute);
        long nextEnd = getNextTime(now, this.mSchedule.endHour, this.mSchedule.endMinute);
        long nextScheduleTime = Math.min(nextStart, nextEnd);
        return nextScheduleTime;
    }

    private synchronized long getNextTime(long now, int hr, int min) {
        long time = getTime(now, hr, min);
        return time <= now ? addDays(time, 1) : time;
    }

    private synchronized long getTime(long millis, int hour, int min) {
        this.mCalendar.setTimeInMillis(millis);
        this.mCalendar.set(11, hour);
        this.mCalendar.set(12, min);
        this.mCalendar.set(13, 0);
        this.mCalendar.set(14, 0);
        return this.mCalendar.getTimeInMillis();
    }

    public synchronized boolean isInSchedule(long time) {
        if (this.mSchedule == null || this.mDays.size() == 0) {
            return false;
        }
        long start = getTime(time, this.mSchedule.startHour, this.mSchedule.startMinute);
        long end = getTime(time, this.mSchedule.endHour, this.mSchedule.endMinute);
        if (end <= start) {
            end = addDays(end, 1);
        }
        long end2 = end;
        return isInSchedule(-1, time, start, end2) || isInSchedule(0, time, start, end2);
    }

    public synchronized boolean isAlarmInSchedule(long alarm, long now) {
        if (this.mSchedule == null || this.mDays.size() == 0) {
            return false;
        }
        long start = getTime(alarm, this.mSchedule.startHour, this.mSchedule.startMinute);
        long end = getTime(alarm, this.mSchedule.endHour, this.mSchedule.endMinute);
        if (end <= start) {
            end = addDays(end, 1);
        }
        long end2 = end;
        return (isInSchedule(-1, alarm, start, end2) && isInSchedule(-1, now, start, end2)) || (isInSchedule(0, alarm, start, end2) && isInSchedule(0, now, start, end2));
    }

    public synchronized boolean shouldExitForAlarm(long time) {
        return this.mSchedule != null && this.mSchedule.exitAtAlarm && this.mSchedule.nextAlarm != 0 && time >= this.mSchedule.nextAlarm && isAlarmInSchedule(this.mSchedule.nextAlarm, time);
    }

    private synchronized boolean isInSchedule(int daysOffset, long time, long start, long end) {
        int day = ((((getDayOfWeek(time) - 1) + (daysOffset % 7)) + 7) % 7) + 1;
        return this.mDays.contains(Integer.valueOf(day)) && time >= addDays(start, daysOffset) && time < addDays(end, daysOffset);
    }

    private synchronized int getDayOfWeek(long time) {
        this.mCalendar.setTimeInMillis(time);
        return this.mCalendar.get(7);
    }

    private synchronized void updateDays() {
        this.mDays.clear();
        if (this.mSchedule != null && this.mSchedule.days != null) {
            for (int i = 0; i < this.mSchedule.days.length; i++) {
                this.mDays.add(Integer.valueOf(this.mSchedule.days[i]));
            }
        }
    }

    private synchronized long addDays(long time, int days) {
        this.mCalendar.setTimeInMillis(time);
        this.mCalendar.add(5, days);
        return this.mCalendar.getTimeInMillis();
    }
}
