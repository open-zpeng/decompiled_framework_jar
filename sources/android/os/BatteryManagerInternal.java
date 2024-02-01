package android.os;
/* loaded from: classes2.dex */
public abstract class BatteryManagerInternal {
    public static final boolean IS_XP_BATTERY = true;

    public abstract synchronized int getBatteryChargeCounter();

    public abstract synchronized int getBatteryFullCharge();

    public abstract synchronized int getBatteryLevel();

    public abstract synchronized boolean getBatteryLevelLow();

    public abstract synchronized int getInvalidCharger();

    public abstract synchronized int getPlugType();

    public abstract synchronized boolean isPowered(int i);
}
