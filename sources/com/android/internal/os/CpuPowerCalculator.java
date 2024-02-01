package com.android.internal.os;

import android.os.BatteryStats;
import android.util.ArrayMap;
import android.util.Log;
import com.xiaopeng.util.FeatureOption;

/* loaded from: classes3.dex */
public class CpuPowerCalculator extends PowerCalculator {
    private static final boolean DEBUG = false;
    private static final long MICROSEC_IN_HR = 3600000000L;
    private static final String TAG = "CpuPowerCalculator";
    private final PowerProfile mProfile;

    public CpuPowerCalculator(PowerProfile profile) {
        this.mProfile = profile;
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateApp(BatterySipper app, BatteryStats.Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        double cpuPowerMaUs;
        app.cpuTimeMs = (u.getUserCpuTimeUs(statsType) + u.getSystemCpuTimeUs(statsType)) / 1000;
        int numClusters = this.mProfile.getNumCpuClusters();
        double cpuPowerMaUs2 = FeatureOption.FO_BOOT_POLICY_CPU;
        for (int cluster = 0; cluster < numClusters; cluster++) {
            int speedsForCluster = this.mProfile.getNumSpeedStepsInCpuCluster(cluster);
            for (int speed = 0; speed < speedsForCluster; speed++) {
                long timeUs = u.getTimeAtCpuSpeed(cluster, speed, statsType);
                double cpuSpeedStepPower = timeUs * this.mProfile.getAveragePowerForCpuCore(cluster, speed);
                cpuPowerMaUs2 += cpuSpeedStepPower;
            }
        }
        double cpuPowerMaUs3 = cpuPowerMaUs2 + (u.getCpuActiveTime() * 1000 * this.mProfile.getAveragePower(PowerProfile.POWER_CPU_ACTIVE));
        long[] cpuClusterTimes = u.getCpuClusterTimes();
        if (cpuClusterTimes != null) {
            if (cpuClusterTimes.length == numClusters) {
                for (int i = 0; i < numClusters; i++) {
                    double power = cpuClusterTimes[i] * 1000 * this.mProfile.getAveragePowerForCpuCluster(i);
                    cpuPowerMaUs3 += power;
                }
            } else {
                Log.w(TAG, "UID " + u.getUid() + " CPU cluster # mismatch: Power Profile # " + numClusters + " actual # " + cpuClusterTimes.length);
            }
        }
        app.cpuPowerMah = cpuPowerMaUs3 / 3.6E9d;
        double highestDrain = FeatureOption.FO_BOOT_POLICY_CPU;
        app.cpuFgTimeMs = 0L;
        ArrayMap<String, ? extends BatteryStats.Uid.Proc> processStats = u.getProcessStats();
        int processStatsCount = processStats.size();
        int i2 = 0;
        while (i2 < processStatsCount) {
            BatteryStats.Uid.Proc ps = processStats.valueAt(i2);
            String processName = processStats.keyAt(i2);
            int numClusters2 = numClusters;
            long[] cpuClusterTimes2 = cpuClusterTimes;
            app.cpuFgTimeMs += ps.getForegroundTime(statsType);
            long costValue = ps.getUserTime(statsType) + ps.getSystemTime(statsType) + ps.getForegroundTime(statsType);
            if (app.packageWithHighestDrain == null) {
                cpuPowerMaUs = cpuPowerMaUs3;
            } else if (app.packageWithHighestDrain.startsWith("*")) {
                cpuPowerMaUs = cpuPowerMaUs3;
            } else {
                cpuPowerMaUs = cpuPowerMaUs3;
                double cpuPowerMaUs4 = costValue;
                if (highestDrain < cpuPowerMaUs4 && !processName.startsWith("*")) {
                    highestDrain = costValue;
                    app.packageWithHighestDrain = processName;
                }
                i2++;
                numClusters = numClusters2;
                cpuClusterTimes = cpuClusterTimes2;
                cpuPowerMaUs3 = cpuPowerMaUs;
            }
            highestDrain = costValue;
            app.packageWithHighestDrain = processName;
            i2++;
            numClusters = numClusters2;
            cpuClusterTimes = cpuClusterTimes2;
            cpuPowerMaUs3 = cpuPowerMaUs;
        }
        if (app.cpuFgTimeMs > app.cpuTimeMs) {
            app.cpuTimeMs = app.cpuFgTimeMs;
        }
    }
}
