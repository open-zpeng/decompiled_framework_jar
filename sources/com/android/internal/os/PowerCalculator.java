package com.android.internal.os;

import android.os.BatteryStats;

/* loaded from: classes3.dex */
public abstract class PowerCalculator {
    public abstract void calculateApp(BatterySipper batterySipper, BatteryStats.Uid uid, long j, long j2, int i);

    public void calculateRemaining(BatterySipper app, BatteryStats stats, long rawRealtimeUs, long rawUptimeUs, int statsType) {
    }

    public void reset() {
    }
}
