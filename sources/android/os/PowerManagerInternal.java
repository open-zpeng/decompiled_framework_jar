package android.os;

import java.util.function.Consumer;
/* loaded from: classes2.dex */
public abstract class PowerManagerInternal {
    public static final int AWAKE_SCREEN_ON = 0;
    public static final int FAKE_SLEEP = 1;
    public static final int SLEEP = 2;
    public static final int WAKEFULNESS_ASLEEP = 0;
    public static final int WAKEFULNESS_AWAKE = 1;
    public static final int WAKEFULNESS_DISPLAY_OFF = 5;
    public static final int WAKEFULNESS_DOZING = 3;
    public static final int WAKEFULNESS_DREAMING = 2;
    public static final int WAKEFULNESS_FAKESLEEP = 4;
    public static final String XP_POWER_STATE = "sys.xiaopeng.power_state";

    /* loaded from: classes2.dex */
    public interface LowPowerModeListener {
        synchronized int getServiceType();

        synchronized void onLowPowerModeChanged(PowerSaveState powerSaveState);
    }

    public abstract synchronized void finishUidChanges();

    public abstract synchronized PowerSaveState getLowPowerState(int i);

    public abstract synchronized void powerHint(int i, int i2);

    public abstract synchronized void registerLowPowerModeObserver(LowPowerModeListener lowPowerModeListener);

    public abstract synchronized boolean setDeviceIdleMode(boolean z);

    public abstract synchronized void setDeviceIdleTempWhitelist(int[] iArr);

    public abstract synchronized void setDeviceIdleWhitelist(int[] iArr);

    public abstract synchronized void setDozeOverrideFromDreamManager(int i, int i2);

    public abstract synchronized void setDrawWakeLockOverrideFromSidekick(boolean z);

    public abstract synchronized boolean setLightDeviceIdleMode(boolean z);

    public abstract synchronized void setMaximumScreenOffTimeoutFromDeviceAdmin(int i, long j);

    public abstract synchronized void setScreenBrightnessOverrideFromWindowManager(int i);

    public abstract synchronized void setUserActivityTimeoutOverrideFromWindowManager(long j);

    public abstract synchronized void setUserInactiveOverrideFromWindowManager();

    public abstract synchronized void startUidChanges();

    public abstract synchronized void uidActive(int i);

    public abstract synchronized void uidGone(int i);

    public abstract synchronized void uidIdle(int i);

    public abstract synchronized void updateUidProcState(int i, int i2);

    public static synchronized String wakefulnessToString(int wakefulness) {
        switch (wakefulness) {
            case 0:
                return "Asleep";
            case 1:
                return "Awake";
            case 2:
                return "Dreaming";
            case 3:
                return "Dozing";
            case 4:
                return "FakeSleep";
            case 5:
                return "DisplayOff";
            default:
                return Integer.toString(wakefulness);
        }
    }

    public static synchronized int wakefulnessToProtoEnum(int wakefulness) {
        switch (wakefulness) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            default:
                return wakefulness;
        }
    }

    public static synchronized boolean isInteractive(int wakefulness) {
        return wakefulness == 1 || wakefulness == 2;
    }

    public static boolean isInAwakeDisplayOff(int wakefulness) {
        return wakefulness == 5;
    }

    public static boolean isInSleep(int wakefulness) {
        return wakefulness == 0 || wakefulness == 3;
    }

    public synchronized void registerLowPowerModeObserver(final int serviceType, final Consumer<PowerSaveState> listener) {
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
