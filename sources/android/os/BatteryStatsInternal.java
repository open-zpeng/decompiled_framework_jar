package android.os;
/* loaded from: classes2.dex */
public abstract class BatteryStatsInternal {
    public abstract synchronized String[] getMobileIfaces();

    public abstract synchronized String[] getWifiIfaces();

    public abstract synchronized void noteJobsDeferred(int i, int i2, long j);
}
