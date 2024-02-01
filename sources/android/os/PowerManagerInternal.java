package android.os;

import android.os.PowerManager;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public abstract class PowerManagerInternal {
    public static final int ASLEEP = 2;
    public static final int AWAKE = 0;
    public static final String DP_ON = "dp_on";
    public static final int FAKE_SLEEP = 1;
    public static final String HMI_STATE_FILE = "/sys/xpeng/gpio_indicator/hmi_gpio";
    public static final boolean IS_E38;
    public static final boolean IS_HAS_PASSENGER;
    public static final boolean IS_SILENT_BOOT = true;
    public static final boolean IS_XP_POWER = true;
    public static final String SCREEN_IDLE_CHANGE_ACTION = "com.xiaopeng.broadcast.ACTION_SCREEN_IDLE_CHANGE";
    public static final String SCREEN_STATUS_CHANGE_ACTION = "com.xiaopeng.broadcast.ACTION_SCREEN_STATUS_CHANGE";
    public static final int SILENCE_INITIAL = 0;
    public static final int SILENCE_OFF_BLACK = 1;
    public static final int SILENCE_OTA_BLACK = 4;
    public static final int SILENCE_SLEEP_BLACK = 2;
    public static final int SILENCE_STATUS_OFF = 2;
    public static final int SILENCE_STATUS_ON = 1;
    public static final String SILENT_OFF = "silence_off";
    public static final String SILENT_ON = "silence_on";
    public static final String SLEEP_STATE_FILE = "/sys/xpeng/gpio_indicator/sleep_gpio";
    public static final int WAKEFULNESS_ASLEEP = 0;
    public static final int WAKEFULNESS_AWAKE = 1;
    public static final int WAKEFULNESS_DOZING = 3;
    public static final int WAKEFULNESS_DREAMING = 2;
    public static final String WAKEUP_STATE_FILE = "/sys/xpeng/gpio_indicator/lcd_gpio";
    public static final String XP_CLUSTER_STATUS = "/sys/xpeng/cluster/cluster_status";
    public static final String XP_ICM_BRIGHTNESS_FILE = "/sys/class/backlight/panel1-backlight/brightness";
    public static final String XP_IVI_BRIGHTNESS_FILE = "/sys/class/backlight/panel0-backlight/brightness";
    public static final String XP_IVI_PASSENGER_BRIGHTNESS_FILE = "/sys/class/backlight/panel2-backlight/brightness";
    public static final String XP_IVI_STATUS = "/sys/xpeng/ivi/ivi_status";
    public static final String XP_PASSENGER_STATUS = "/sys/xpeng/passenger/passenger_status";
    public static final String XP_POWER_STATE = "sys.xiaopeng.power_state";

    /* loaded from: classes2.dex */
    public interface LowPowerModeListener {
        int getServiceType();

        void onLowPowerModeChanged(PowerSaveState powerSaveState);
    }

    public abstract void finishUidChanges();

    public abstract PowerManager.WakeData getLastWakeup();

    public abstract PowerSaveState getLowPowerState(int i);

    public abstract boolean isScreenOn(String str);

    public abstract void powerHint(int i, int i2);

    public abstract void registerLowPowerModeObserver(LowPowerModeListener lowPowerModeListener);

    public abstract boolean setDeviceIdleMode(boolean z);

    public abstract void setDeviceIdleTempWhitelist(int[] iArr);

    public abstract void setDeviceIdleWhitelist(int[] iArr);

    public abstract void setDozeOverrideFromDreamManager(int i, int i2);

    public abstract void setDrawWakeLockOverrideFromSidekick(boolean z);

    public abstract boolean setLightDeviceIdleMode(boolean z);

    public abstract void setMaximumScreenOffTimeoutFromDeviceAdmin(int i, long j);

    public abstract void setScreenBrightnessOverrideFromWindowManager(int i);

    public abstract void setUserActivityTimeoutOverrideFromWindowManager(long j);

    public abstract void setUserInactiveOverrideFromWindowManager();

    public abstract void startUidChanges();

    public abstract void uidActive(int i);

    public abstract void uidGone(int i);

    public abstract void uidIdle(int i);

    public abstract void updateUidProcState(int i, int i2);

    public abstract void userSetBackLightOn(String str, int i);

    public abstract boolean wasDeviceIdleFor(long j);

    public abstract void writeDisplaySilentStatus(String str, String str2);

    static {
        IS_HAS_PASSENGER = SystemProperties.getInt("ro.boot.xp_product_major", -1) == 1;
        IS_E38 = SystemProperties.getInt("ro.boot.xp_product_major", -1) == 1;
    }

    public static String wakefulnessToString(int wakefulness) {
        if (wakefulness != 0) {
            if (wakefulness != 1) {
                if (wakefulness != 2) {
                    if (wakefulness == 3) {
                        return "Dozing";
                    }
                    return Integer.toString(wakefulness);
                }
                return "Dreaming";
            }
            return "Awake";
        }
        return "Asleep";
    }

    public static String powerStateToString(int powerState) {
        if (powerState != 0) {
            if (powerState != 1) {
                if (powerState == 2) {
                    return "Asleep";
                }
                return Integer.toString(powerState);
            }
            return "FakeSleep";
        }
        return "Awake";
    }

    public static int wakefulnessToProtoEnum(int wakefulness) {
        if (wakefulness != 0) {
            if (wakefulness != 1) {
                if (wakefulness != 2) {
                    if (wakefulness != 3) {
                        return wakefulness;
                    }
                    return 3;
                }
                return 2;
            }
            return 1;
        }
        return 0;
    }

    public static boolean isInteractive(int wakefulness) {
        return wakefulness == 1 || wakefulness == 2;
    }

    public void registerLowPowerModeObserver(final int serviceType, final Consumer<PowerSaveState> listener) {
        registerLowPowerModeObserver(new LowPowerModeListener() { // from class: android.os.PowerManagerInternal.1
            @Override // android.os.PowerManagerInternal.LowPowerModeListener
            public int getServiceType() {
                return serviceType;
            }

            @Override // android.os.PowerManagerInternal.LowPowerModeListener
            public void onLowPowerModeChanged(PowerSaveState state) {
                listener.accept(state);
            }
        });
    }
}
