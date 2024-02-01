package com.android.internal.os;

import android.os.BatteryStats;
import android.util.LongSparseArray;
import com.xiaopeng.util.FeatureOption;

/* loaded from: classes3.dex */
public class MemoryPowerCalculator extends PowerCalculator {
    private static final boolean DEBUG = false;
    public static final String TAG = "MemoryPowerCalculator";
    private final double[] powerAverages;

    public MemoryPowerCalculator(PowerProfile profile) {
        int numBuckets = profile.getNumElements(PowerProfile.POWER_MEMORY);
        this.powerAverages = new double[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            this.powerAverages[i] = profile.getAveragePower(PowerProfile.POWER_MEMORY, i);
            double d = this.powerAverages[i];
        }
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateApp(BatterySipper app, BatteryStats.Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateRemaining(BatterySipper app, BatteryStats stats, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        double totalMah = FeatureOption.FO_BOOT_POLICY_CPU;
        long totalTimeMs = 0;
        LongSparseArray<? extends BatteryStats.Timer> timers = stats.getKernelMemoryStats();
        for (int i = 0; i < timers.size(); i++) {
            double[] dArr = this.powerAverages;
            if (i >= dArr.length) {
                break;
            }
            double mAatRail = dArr[(int) timers.keyAt(i)];
            long timeMs = timers.valueAt(i).getTotalTimeLocked(rawRealtimeUs, statsType);
            double mAm = (timeMs * mAatRail) / 60000.0d;
            totalMah += mAm / 60.0d;
            totalTimeMs += timeMs;
        }
        app.usagePowerMah = totalMah;
        app.usageTimeMs = totalTimeMs;
    }
}
