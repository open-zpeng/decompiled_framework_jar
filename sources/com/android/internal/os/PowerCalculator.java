package com.android.internal.os;

import android.os.BatteryStats;
/* loaded from: classes3.dex */
public abstract class PowerCalculator {
    public abstract synchronized void calculateApp(BatterySipper batterySipper, BatteryStats.Uid uid, long j, long j2, int i);

    public synchronized void calculateRemaining(BatterySipper app, BatteryStats stats, long rawRealtimeUs, long rawUptimeUs, int statsType) {
    }

    public synchronized void reset() {
    }
}
