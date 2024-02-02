package com.android.internal.os;

import android.os.BatteryStats;
import android.util.ArrayMap;
/* loaded from: classes3.dex */
public class WakelockPowerCalculator extends PowerCalculator {
    private static final boolean DEBUG = false;
    private static final String TAG = "WakelockPowerCalculator";
    private final double mPowerWakelock;
    private long mTotalAppWakelockTimeMs = 0;

    public WakelockPowerCalculator(PowerProfile profile) {
        this.mPowerWakelock = profile.getAveragePower("cpu.idle");
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateApp(BatterySipper app, BatteryStats.Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        ArrayMap<String, ? extends BatteryStats.Uid.Wakelock> wakelockStats = u.getWakelockStats();
        int wakelockStatsCount = wakelockStats.size();
        long wakeLockTimeUs = 0;
        for (int i = 0; i < wakelockStatsCount; i++) {
            BatteryStats.Uid.Wakelock wakelock = wakelockStats.valueAt(i);
            BatteryStats.Timer timer = wakelock.getWakeTime(0);
            if (timer != null) {
                wakeLockTimeUs += timer.getTotalTimeLocked(rawRealtimeUs, statsType);
            }
        }
        app.wakeLockTimeMs = wakeLockTimeUs / 1000;
        this.mTotalAppWakelockTimeMs += app.wakeLockTimeMs;
        app.wakeLockPowerMah = (app.wakeLockTimeMs * this.mPowerWakelock) / 3600000.0d;
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateRemaining(BatterySipper app, BatteryStats stats, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        long wakeTimeMillis = (stats.getBatteryUptime(rawUptimeUs) / 1000) - (this.mTotalAppWakelockTimeMs + (stats.getScreenOnTime(rawRealtimeUs, statsType) / 1000));
        if (wakeTimeMillis > 0) {
            double power = (wakeTimeMillis * this.mPowerWakelock) / 3600000.0d;
            app.wakeLockTimeMs += wakeTimeMillis;
            app.wakeLockPowerMah += power;
        }
    }

    @Override // com.android.internal.os.PowerCalculator
    public void reset() {
        this.mTotalAppWakelockTimeMs = 0L;
    }
}
