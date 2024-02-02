package android.os;

import android.app.IAlarmManager;
import android.util.Slog;
import dalvik.annotation.optimization.CriticalNative;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.ZoneOffset;
/* loaded from: classes2.dex */
public final class SystemClock {
    private static final String TAG = "SystemClock";

    /* JADX INFO: Access modifiers changed from: private */
    @CriticalNative
    public static native long currentThreadTimeMicro();

    @CriticalNative
    public static native long currentThreadTimeMillis();

    @CriticalNative
    private protected static native long currentTimeMicro();

    @CriticalNative
    public static native long elapsedRealtime();

    @CriticalNative
    public static native long elapsedRealtimeNanos();

    @CriticalNative
    public static native long uptimeMillis();

    public static void sleep(long ms) {
        long start = uptimeMillis();
        long duration = ms;
        boolean interrupted = false;
        do {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                interrupted = true;
            }
            duration = (start + ms) - uptimeMillis();
        } while (duration > 0);
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    public static boolean setCurrentTimeMillis(long millis) {
        IAlarmManager mgr = IAlarmManager.Stub.asInterface(ServiceManager.getService("alarm"));
        if (mgr == null) {
            return false;
        }
        String gpsTime = SystemProperties.get("sys.gps.sync.time", "0");
        if (!"0".equals(gpsTime) && Math.abs(millis - (Long.valueOf(gpsTime).longValue() * 1000)) <= 600000) {
            Slog.e(TAG, "Ignoring NTP update due to recent GPS time");
            return true;
        }
        try {
            return mgr.setTime(millis);
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to set RTC", e);
            return false;
        } catch (SecurityException e2) {
            Slog.e(TAG, "Unable to set RTC", e2);
            return false;
        }
    }

    @Deprecated
    private protected static Clock uptimeMillisClock() {
        return uptimeClock();
    }

    private protected static Clock uptimeClock() {
        return new SimpleClock(ZoneOffset.UTC) { // from class: android.os.SystemClock.1
            @Override // android.os.SimpleClock, java.time.Clock
            public long millis() {
                return SystemClock.uptimeMillis();
            }
        };
    }

    private protected static Clock elapsedRealtimeClock() {
        return new SimpleClock(ZoneOffset.UTC) { // from class: android.os.SystemClock.2
            @Override // android.os.SimpleClock, java.time.Clock
            public long millis() {
                return SystemClock.elapsedRealtime();
            }
        };
    }

    public static synchronized long currentNetworkTimeMillis() {
        IAlarmManager mgr = IAlarmManager.Stub.asInterface(ServiceManager.getService("alarm"));
        if (mgr != null) {
            try {
                return mgr.currentNetworkTimeMillis();
            } catch (ParcelableException e) {
                e.maybeRethrow(DateTimeException.class);
                throw new RuntimeException(e);
            } catch (RemoteException e2) {
                throw e2.rethrowFromSystemServer();
            }
        }
        throw new RuntimeException(new DeadSystemException());
    }

    public static synchronized Clock currentNetworkTimeClock() {
        return new SimpleClock(ZoneOffset.UTC) { // from class: android.os.SystemClock.3
            @Override // android.os.SimpleClock, java.time.Clock
            public long millis() {
                return SystemClock.currentNetworkTimeMillis();
            }
        };
    }
}
