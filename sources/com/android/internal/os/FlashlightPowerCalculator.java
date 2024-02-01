package com.android.internal.os;

import android.os.BatteryStats;
import com.xiaopeng.util.FeatureOption;

/* loaded from: classes3.dex */
public class FlashlightPowerCalculator extends PowerCalculator {
    private final double mFlashlightPowerOnAvg;

    public FlashlightPowerCalculator(PowerProfile profile) {
        this.mFlashlightPowerOnAvg = profile.getAveragePower(PowerProfile.POWER_FLASHLIGHT);
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateApp(BatterySipper app, BatteryStats.Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        BatteryStats.Timer timer = u.getFlashlightTurnedOnTimer();
        if (timer != null) {
            long totalTime = timer.getTotalTimeLocked(rawRealtimeUs, statsType) / 1000;
            app.flashlightTimeMs = totalTime;
            app.flashlightPowerMah = (totalTime * this.mFlashlightPowerOnAvg) / 3600000.0d;
            return;
        }
        app.flashlightTimeMs = 0L;
        app.flashlightPowerMah = FeatureOption.FO_BOOT_POLICY_CPU;
    }
}
