package android.os;

/* loaded from: classes2.dex */
public abstract class BatteryManagerInternal {
    public static final boolean IS_XP_BATTERY = true;

    public abstract int getBatteryChargeCounter();

    public abstract int getBatteryFullCharge();

    public abstract int getBatteryLevel();

    public abstract boolean getBatteryLevelLow();

    public abstract int getInvalidCharger();

    public abstract int getPlugType();

    public abstract boolean isPowered(int i);
}
