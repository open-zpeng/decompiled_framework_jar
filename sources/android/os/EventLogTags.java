package android.os;

import android.util.EventLog;

/* loaded from: classes2.dex */
public class EventLogTags {
    public static final int SERVICE_MANAGER_SLOW = 230001;
    public static final int SERVICE_MANAGER_STATS = 230000;
    public static final int TIME_NTP_TRUSTED = 230002;

    private EventLogTags() {
    }

    public static void writeServiceManagerStats(int callCount, int totalTime, int duration) {
        EventLog.writeEvent((int) SERVICE_MANAGER_STATS, Integer.valueOf(callCount), Integer.valueOf(totalTime), Integer.valueOf(duration));
    }

    public static void writeServiceManagerSlow(int time, String service) {
        EventLog.writeEvent((int) SERVICE_MANAGER_SLOW, Integer.valueOf(time), service);
    }

    public static void writeTimeNtpTrusted(String server, long cachedNtpTime, long elapsedRealtime) {
        EventLog.writeEvent((int) TIME_NTP_TRUSTED, server, Long.valueOf(cachedNtpTime), Long.valueOf(elapsedRealtime));
    }
}
