package com.android.internal.os;

import android.os.BatteryStats;
import com.xiaopeng.util.FeatureOption;

/* loaded from: classes3.dex */
public class WifiPowerCalculator extends PowerCalculator {
    private static final boolean DEBUG = false;
    private static final String TAG = "WifiPowerCalculator";
    private final double mIdleCurrentMa;
    private final double mRxCurrentMa;
    private double mTotalAppPowerDrain = FeatureOption.FO_BOOT_POLICY_CPU;
    private long mTotalAppRunningTime = 0;
    private final double mTxCurrentMa;

    public WifiPowerCalculator(PowerProfile profile) {
        this.mIdleCurrentMa = profile.getAveragePower(PowerProfile.POWER_WIFI_CONTROLLER_IDLE);
        this.mTxCurrentMa = profile.getAveragePower(PowerProfile.POWER_WIFI_CONTROLLER_TX);
        this.mRxCurrentMa = profile.getAveragePower(PowerProfile.POWER_WIFI_CONTROLLER_RX);
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateApp(BatterySipper app, BatteryStats.Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        BatteryStats.ControllerActivityCounter counter = u.getWifiControllerActivity();
        if (counter == null) {
            return;
        }
        long idleTime = counter.getIdleTimeCounter().getCountLocked(statsType);
        long txTime = counter.getTxTimeCounters()[0].getCountLocked(statsType);
        long rxTime = counter.getRxTimeCounter().getCountLocked(statsType);
        app.wifiRunningTimeMs = idleTime + rxTime + txTime;
        this.mTotalAppRunningTime += app.wifiRunningTimeMs;
        app.wifiPowerMah = (((idleTime * this.mIdleCurrentMa) + (txTime * this.mTxCurrentMa)) + (rxTime * this.mRxCurrentMa)) / 3600000.0d;
        this.mTotalAppPowerDrain += app.wifiPowerMah;
        app.wifiRxPackets = u.getNetworkActivityPackets(2, statsType);
        app.wifiTxPackets = u.getNetworkActivityPackets(3, statsType);
        app.wifiRxBytes = u.getNetworkActivityBytes(2, statsType);
        app.wifiTxBytes = u.getNetworkActivityBytes(3, statsType);
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateRemaining(BatterySipper app, BatteryStats stats, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        BatteryStats.ControllerActivityCounter counter = stats.getWifiControllerActivity();
        long idleTimeMs = counter.getIdleTimeCounter().getCountLocked(statsType);
        long txTimeMs = counter.getTxTimeCounters()[0].getCountLocked(statsType);
        long rxTimeMs = counter.getRxTimeCounter().getCountLocked(statsType);
        app.wifiRunningTimeMs = Math.max(0L, ((idleTimeMs + rxTimeMs) + txTimeMs) - this.mTotalAppRunningTime);
        double powerDrainMah = counter.getPowerCounter().getCountLocked(statsType) / 3600000.0d;
        if (powerDrainMah == FeatureOption.FO_BOOT_POLICY_CPU) {
            powerDrainMah = (((idleTimeMs * this.mIdleCurrentMa) + (txTimeMs * this.mTxCurrentMa)) + (rxTimeMs * this.mRxCurrentMa)) / 3600000.0d;
        }
        app.wifiPowerMah = Math.max((double) FeatureOption.FO_BOOT_POLICY_CPU, powerDrainMah - this.mTotalAppPowerDrain);
    }

    @Override // com.android.internal.os.PowerCalculator
    public void reset() {
        this.mTotalAppPowerDrain = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mTotalAppRunningTime = 0L;
    }
}
