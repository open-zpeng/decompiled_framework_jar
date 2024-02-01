package com.android.internal.os;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.BatteryStats;
import android.util.SparseArray;
import com.xiaopeng.util.FeatureOption;
import java.util.List;
/* loaded from: classes3.dex */
public class SensorPowerCalculator extends PowerCalculator {
    private final double mGpsPower;
    private final List<Sensor> mSensors;

    public SensorPowerCalculator(PowerProfile profile, SensorManager sensorManager, BatteryStats stats, long rawRealtimeUs, int statsType) {
        this.mSensors = sensorManager.getSensorList(-1);
        this.mGpsPower = getAverageGpsPower(profile, stats, rawRealtimeUs, statsType);
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateApp(BatterySipper app, BatteryStats.Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        SparseArray<? extends BatteryStats.Uid.Sensor> sensorStats;
        int NSE;
        SparseArray<? extends BatteryStats.Uid.Sensor> sensorStats2 = u.getSensorStats();
        int NSE2 = sensorStats2.size();
        int ise = 0;
        while (ise < NSE2) {
            BatteryStats.Uid.Sensor sensor = sensorStats2.valueAt(ise);
            int sensorHandle = sensorStats2.keyAt(ise);
            BatteryStats.Timer timer = sensor.getSensorTime();
            long sensorTime = timer.getTotalTimeLocked(rawRealtimeUs, statsType) / 1000;
            if (sensorHandle == -10000) {
                sensorStats = sensorStats2;
                NSE = NSE2;
                app.gpsTimeMs = sensorTime;
                app.gpsPowerMah = (app.gpsTimeMs * this.mGpsPower) / 3600000.0d;
            } else {
                int sensorsCount = this.mSensors.size();
                int i = 0;
                while (true) {
                    if (i >= sensorsCount) {
                        sensorStats = sensorStats2;
                        NSE = NSE2;
                        break;
                    }
                    Sensor s = this.mSensors.get(i);
                    sensorStats = sensorStats2;
                    if (s.getHandle() == sensorHandle) {
                        NSE = NSE2;
                        app.sensorPowerMah += (((float) sensorTime) * s.getPower()) / 3600000.0f;
                        break;
                    }
                    i++;
                    sensorStats2 = sensorStats;
                }
            }
            ise++;
            sensorStats2 = sensorStats;
            NSE2 = NSE;
        }
    }

    private double getAverageGpsPower(PowerProfile profile, BatteryStats stats, long rawRealtimeUs, int statsType) {
        PowerProfile powerProfile = profile;
        double averagePower = powerProfile.getAveragePowerOrDefault("gps.on", -1.0d);
        if (averagePower != -1.0d) {
            return averagePower;
        }
        double averagePower2 = FeatureOption.FO_BOOT_POLICY_CPU;
        long totalTime = 0;
        double totalPower = FeatureOption.FO_BOOT_POLICY_CPU;
        int i = 0;
        while (i < 2) {
            long timePerLevel = stats.getGpsSignalQualityTime(i, rawRealtimeUs, statsType);
            totalTime += timePerLevel;
            totalPower += powerProfile.getAveragePower(PowerProfile.POWER_GPS_SIGNAL_QUALITY_BASED, i) * timePerLevel;
            i++;
            averagePower2 = averagePower2;
            powerProfile = profile;
        }
        return totalTime != 0 ? totalPower / totalTime : averagePower2;
    }
}
