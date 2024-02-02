package com.android.internal.os;

import android.os.BatteryStats;
import com.xiaopeng.util.FeatureOption;
/* loaded from: classes3.dex */
public class WifiPowerEstimator extends PowerCalculator {
    private static final boolean DEBUG = false;
    private static final String TAG = "WifiPowerEstimator";
    private long mTotalAppWifiRunningTimeMs = 0;
    private final double mWifiPowerBatchScan;
    private final double mWifiPowerOn;
    private final double mWifiPowerPerPacket;
    private final double mWifiPowerScan;

    public WifiPowerEstimator(PowerProfile profile) {
        this.mWifiPowerPerPacket = getWifiPowerPerPacket(profile);
        this.mWifiPowerOn = profile.getAveragePower("wifi.on");
        this.mWifiPowerScan = profile.getAveragePower("wifi.scan");
        this.mWifiPowerBatchScan = profile.getAveragePower(PowerProfile.POWER_WIFI_BATCHED_SCAN);
    }

    private static double getWifiPowerPerPacket(PowerProfile profile) {
        double WIFI_POWER = profile.getAveragePower("wifi.active") / 3600.0d;
        return WIFI_POWER / 61.03515625d;
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateApp(BatterySipper app, BatteryStats.Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        BatteryStats.Uid uid = u;
        long j = rawRealtimeUs;
        int i = statsType;
        app.wifiRxPackets = uid.getNetworkActivityPackets(2, i);
        app.wifiTxPackets = uid.getNetworkActivityPackets(3, i);
        app.wifiRxBytes = uid.getNetworkActivityBytes(2, i);
        app.wifiTxBytes = uid.getNetworkActivityBytes(3, i);
        double wifiPacketPower = (app.wifiRxPackets + app.wifiTxPackets) * this.mWifiPowerPerPacket;
        app.wifiRunningTimeMs = uid.getWifiRunningTime(j, i) / 1000;
        this.mTotalAppWifiRunningTimeMs += app.wifiRunningTimeMs;
        double wifiLockPower = (app.wifiRunningTimeMs * this.mWifiPowerOn) / 3600000.0d;
        long wifiScanTimeMs = uid.getWifiScanTime(j, i) / 1000;
        double wifiScanPower = (wifiScanTimeMs * this.mWifiPowerScan) / 3600000.0d;
        double wifiBatchScanPower = FeatureOption.FO_BOOT_POLICY_CPU;
        int bin = 0;
        while (true) {
            int bin2 = bin;
            if (bin2 < 5) {
                long batchScanTimeMs = uid.getWifiBatchedScanTime(bin2, j, i) / 1000;
                double batchScanPower = (batchScanTimeMs * this.mWifiPowerBatchScan) / 3600000.0d;
                wifiBatchScanPower += batchScanPower;
                bin = bin2 + 1;
                uid = u;
                j = rawRealtimeUs;
                i = statsType;
            } else {
                app.wifiPowerMah = wifiPacketPower + wifiLockPower + wifiScanPower + wifiBatchScanPower;
                return;
            }
        }
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateRemaining(BatterySipper app, BatteryStats stats, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        long totalRunningTimeMs = stats.getGlobalWifiRunningTime(rawRealtimeUs, statsType) / 1000;
        double powerDrain = ((totalRunningTimeMs - this.mTotalAppWifiRunningTimeMs) * this.mWifiPowerOn) / 3600000.0d;
        app.wifiRunningTimeMs = totalRunningTimeMs;
        app.wifiPowerMah = Math.max((double) FeatureOption.FO_BOOT_POLICY_CPU, powerDrain);
    }

    @Override // com.android.internal.os.PowerCalculator
    public void reset() {
        this.mTotalAppWifiRunningTimeMs = 0L;
    }
}
