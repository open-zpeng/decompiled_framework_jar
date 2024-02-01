package com.android.server.power;
/* loaded from: classes3.dex */
public final class PowerManagerServiceDumpProto {
    private protected static final long ACTIVE_WAKE_LOCKS = 1146756268048L;
    private protected static final long ARE_UIDS_CHANGED = 1133871366188L;
    private protected static final long ARE_UIDS_CHANGING = 1133871366187L;
    private protected static final long BATTERY_LEVEL = 1120986464263L;
    private protected static final long BATTERY_LEVEL_WHEN_DREAM_STARTED = 1120986464264L;
    private protected static final long BATTERY_SAVER_STATE_MACHINE = 1146756268082L;
    private protected static final long CONSTANTS = 1146756268033L;
    private protected static final long DEVICE_IDLE_TEMP_WHITELIST = 2220498092060L;
    private protected static final long DEVICE_IDLE_WHITELIST = 2220498092059L;
    private protected static final long DIRTY = 1120986464258L;
    private protected static final long DOCK_STATE = 1159641169929L;
    private protected static final long IS_BATTERY_LEVEL_LOW = 1133871366168L;
    private protected static final long IS_BOOT_COMPLETED = 1133871366156L;
    private protected static final long IS_DEVICE_IDLE_MODE = 1133871366170L;
    private protected static final long IS_DISPLAY_READY = 1133871366180L;
    private protected static final long IS_HAL_AUTO_INTERACTIVE_MODE_ENABLED = 1133871366159L;
    private protected static final long IS_HAL_AUTO_SUSPEND_MODE_ENABLED = 1133871366158L;
    private protected static final long IS_HOLDING_DISPLAY_SUSPEND_BLOCKER = 1133871366182L;
    private protected static final long IS_HOLDING_WAKE_LOCK_SUSPEND_BLOCKER = 1133871366181L;
    private protected static final long IS_LIGHT_DEVICE_IDLE_MODE = 1133871366169L;
    private protected static final long IS_POWERED = 1133871366149L;
    private protected static final long IS_PROXIMITY_POSITIVE = 1133871366155L;
    private protected static final long IS_REQUEST_WAIT_FOR_NEGATIVE_PROXIMITY = 1133871366165L;
    private protected static final long IS_SANDMAN_SCHEDULED = 1133871366166L;
    private protected static final long IS_SANDMAN_SUMMONED = 1133871366167L;
    private protected static final long IS_SCREEN_BRIGHTNESS_BOOST_IN_PROGRESS = 1133871366179L;
    private protected static final long IS_STAY_ON = 1133871366154L;
    private protected static final long IS_SYSTEM_READY = 1133871366157L;
    private protected static final long IS_WAKEFULNESS_CHANGING = 1133871366148L;
    private protected static final long LAST_INTERACTIVE_POWER_HINT_TIME_MS = 1112396529697L;
    private protected static final long LAST_SCREEN_BRIGHTNESS_BOOST_TIME_MS = 1112396529698L;
    private protected static final long LAST_SLEEP_TIME_MS = 1112396529694L;
    private protected static final long LAST_USER_ACTIVITY_TIME_MS = 1112396529695L;
    private protected static final long LAST_USER_ACTIVITY_TIME_NO_CHANGE_LIGHTS_MS = 1112396529696L;
    private protected static final long LAST_WAKE_TIME_MS = 1112396529693L;
    private protected static final long LOOPER = 1146756268078L;
    private protected static final long NOTIFY_LONG_DISPATCHED_MS = 1112396529682L;
    private protected static final long NOTIFY_LONG_NEXT_CHECK_MS = 1112396529683L;
    private protected static final long NOTIFY_LONG_SCHEDULED_MS = 1112396529681L;
    private protected static final long PLUG_TYPE = 1159641169926L;
    private protected static final long SCREEN_DIM_DURATION_MS = 1120986464298L;
    private protected static final long SCREEN_OFF_TIMEOUT_MS = 1120986464297L;
    private protected static final long SETTINGS_AND_CONFIGURATION = 1146756268071L;
    private protected static final long SLEEP_TIMEOUT_MS = 1172526071848L;
    private protected static final long SUSPEND_BLOCKERS = 2246267895856L;
    private protected static final long UID_STATES = 2246267895853L;
    private protected static final long USER_ACTIVITY = 1146756268052L;
    private protected static final long WAKEFULNESS = 1159641169923L;
    private protected static final long WAKE_LOCKS = 2246267895855L;
    private protected static final long WIRELESS_CHARGER_DETECTOR = 1146756268081L;

    private protected synchronized PowerManagerServiceDumpProto() {
    }

    /* loaded from: classes3.dex */
    public final class ConstantsProto {
        private protected static final long IS_NO_CACHED_WAKE_LOCKS = 1133871366145L;

        public ConstantsProto() {
        }
    }

    /* loaded from: classes3.dex */
    public final class ActiveWakeLocksProto {
        private protected static final long IS_BUTTON_BRIGHT = 1133871366148L;
        private protected static final long IS_CPU = 1133871366145L;
        private protected static final long IS_DOZE = 1133871366151L;
        private protected static final long IS_DRAW = 1133871366152L;
        private protected static final long IS_PROXIMITY_SCREEN_OFF = 1133871366149L;
        private protected static final long IS_SCREEN_BRIGHT = 1133871366146L;
        private protected static final long IS_SCREEN_DIM = 1133871366147L;
        private protected static final long IS_STAY_AWAKE = 1133871366150L;

        public ActiveWakeLocksProto() {
        }
    }

    /* loaded from: classes3.dex */
    public final class UserActivityProto {
        private protected static final long IS_SCREEN_BRIGHT = 1133871366145L;
        private protected static final long IS_SCREEN_DIM = 1133871366146L;
        private protected static final long IS_SCREEN_DREAM = 1133871366147L;

        public UserActivityProto() {
        }
    }

    /* loaded from: classes3.dex */
    public final class UidStateProto {
        private protected static final long IS_ACTIVE = 1133871366147L;
        private protected static final long NUM_WAKE_LOCKS = 1120986464260L;
        private protected static final long PROCESS_STATE = 1159641169925L;
        private protected static final long UID = 1120986464257L;
        private protected static final long UID_STRING = 1138166333442L;

        public UidStateProto() {
        }
    }
}
