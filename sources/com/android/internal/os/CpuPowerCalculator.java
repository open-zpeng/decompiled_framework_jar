package com.android.internal.os;

import android.os.BatteryStats;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.telephony.PhoneConstants;
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
        double highestDrain;
        int i = statsType;
        app.cpuTimeMs = (u.getUserCpuTimeUs(i) + u.getSystemCpuTimeUs(i)) / 1000;
        int numClusters = this.mProfile.getNumCpuClusters();
        double cpuPowerMaUs = 0.0d;
        int cluster = 0;
        while (cluster < numClusters) {
            int speedsForCluster = this.mProfile.getNumSpeedStepsInCpuCluster(cluster);
            double cpuPowerMaUs2 = cpuPowerMaUs;
            for (int speed = 0; speed < speedsForCluster; speed++) {
                long timeUs = u.getTimeAtCpuSpeed(cluster, speed, i);
                double cpuSpeedStepPower = timeUs * this.mProfile.getAveragePowerForCpuCore(cluster, speed);
                cpuPowerMaUs2 += cpuSpeedStepPower;
            }
            cluster++;
            cpuPowerMaUs = cpuPowerMaUs2;
        }
        double cpuPowerMaUs3 = cpuPowerMaUs + (u.getCpuActiveTime() * 1000 * this.mProfile.getAveragePower("cpu.active"));
        long[] cpuClusterTimes = u.getCpuClusterTimes();
        if (cpuClusterTimes != null) {
            if (cpuClusterTimes.length == numClusters) {
                for (int i2 = 0; i2 < numClusters; i2++) {
                    double power = cpuClusterTimes[i2] * 1000 * this.mProfile.getAveragePowerForCpuCluster(i2);
                    cpuPowerMaUs3 += power;
                }
            } else {
                Log.w(TAG, "UID " + u.getUid() + " CPU cluster # mismatch: Power Profile # " + numClusters + " actual # " + cpuClusterTimes.length);
            }
        }
        app.cpuPowerMah = cpuPowerMaUs3 / 3.6E9d;
        double highestDrain2 = FeatureOption.FO_BOOT_POLICY_CPU;
        app.cpuFgTimeMs = 0L;
        ArrayMap<String, ? extends BatteryStats.Uid.Proc> processStats = u.getProcessStats();
        int processStatsCount = processStats.size();
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 >= processStatsCount) {
                break;
            }
            BatteryStats.Uid.Proc ps = processStats.valueAt(i4);
            String processName = processStats.keyAt(i4);
            int numClusters2 = numClusters;
            long[] cpuClusterTimes2 = cpuClusterTimes;
            app.cpuFgTimeMs += ps.getForegroundTime(i);
            long costValue = ps.getUserTime(i) + ps.getSystemTime(i) + ps.getForegroundTime(i);
            if (app.packageWithHighestDrain == null || app.packageWithHighestDrain.startsWith(PhoneConstants.APN_TYPE_ALL)) {
                highestDrain = costValue;
                app.packageWithHighestDrain = processName;
            } else {
                if (highestDrain2 < costValue && !processName.startsWith(PhoneConstants.APN_TYPE_ALL)) {
                    highestDrain = costValue;
                    app.packageWithHighestDrain = processName;
                }
                i3 = i4 + 1;
                numClusters = numClusters2;
                cpuClusterTimes = cpuClusterTimes2;
                i = statsType;
            }
            highestDrain2 = highestDrain;
            i3 = i4 + 1;
            numClusters = numClusters2;
            cpuClusterTimes = cpuClusterTimes2;
            i = statsType;
        }
        if (app.cpuFgTimeMs > app.cpuTimeMs) {
            app.cpuTimeMs = app.cpuFgTimeMs;
        }
    }
}
