package com.android.server.am;
/* loaded from: classes3.dex */
public final class ActivityManagerServiceDumpProcessesProto {
    private protected static final long ACTIVE_INSTRUMENTATIONS = 2246267895811L;
    private protected static final long ACTIVE_UIDS = 2246267895812L;
    private protected static final long ADJ_SEQ = 1120986464305L;
    private protected static final long ALLOW_LOWER_MEM_LEVEL = 1133871366199L;
    private protected static final long ALWAYS_FINISH_ACTIVITIES = 1133871366180L;
    private protected static final long APP_ERRORS = 1146756268045L;
    private protected static final long BOOTED = 1133871366185L;
    private protected static final long BOOTING = 1133871366187L;
    private protected static final long BOOT_ANIMATION_COMPLETE = 1133871366189L;
    private protected static final long CALL_FINISH_BOOTING = 1133871366188L;
    private protected static final long CONFIG_WILL_CHANGE = 1133871366165L;
    private protected static final long CONTROLLER = 1146756268069L;
    private protected static final long CURRENT_TRACKER = 1146756268063L;
    private protected static final long DEBUG = 1146756268062L;
    private protected static final long DEVICE_IDLE_TEMP_WHITELIST = 2220498092057L;
    private protected static final long DEVICE_IDLE_WHITELIST = 2220498092056L;
    private protected static final long FACTORY_TEST = 1120986464298L;
    private protected static final long GC_PROCS = 2246267895820L;
    private protected static final long GLOBAL_CONFIGURATION = 1146756268051L;
    private protected static final long GOING_TO_SLEEP = 1146756268079L;
    private protected static final long HEAVY_WEIGHT_PROC = 1146756268050L;
    private protected static final long HOME_PROC = 1146756268047L;
    private protected static final long IMPORTANT_PROCS = 2246267895816L;
    private protected static final long ISOLATED_PROCS = 2246267895810L;
    private protected static final long LAST_IDLE_TIME = 1146756268090L;
    private protected static final long LAST_MEMORY_LEVEL = 1120986464312L;
    private protected static final long LAST_NUM_PROCESSES = 1120986464313L;
    private protected static final long LAST_POWER_CHECK_UPTIME_MS = 1112396529710L;
    private protected static final long LAUNCHING_ACTIVITY = 1146756268080L;
    private protected static final long LOW_RAM_SINCE_LAST_IDLE_MS = 1112396529723L;
    private protected static final long LRU_PROCS = 1146756268038L;
    private protected static final long LRU_SEQ = 1120986464306L;
    private protected static final long MEM_WATCH_PROCESSES = 1146756268064L;
    private protected static final long NATIVE_DEBUGGING_APP = 1138166333475L;
    private protected static final long NEW_NUM_SERVICE_PROCS = 1120986464310L;
    private protected static final long NUM_CACHED_HIDDEN_PROCS = 1120986464308L;
    private protected static final long NUM_NON_CACHED_PROCS = 1120986464307L;
    private protected static final long NUM_SERVICE_PROCS = 1120986464309L;
    private protected static final long ON_HOLD_PROCS = 2246267895819L;
    private protected static final long PENDING_TEMP_WHITELIST = 2246267895834L;
    private protected static final long PERSISTENT_STARTING_PROCS = 2246267895817L;
    private protected static final long PIDS_SELF_LOCKED = 2246267895815L;
    private protected static final long PREVIOUS_PROC = 1146756268048L;
    private protected static final long PREVIOUS_PROC_VISIBLE_TIME_MS = 1112396529681L;
    private protected static final long PROCESSES_READY = 1133871366183L;
    private protected static final long PROCS = 2246267895809L;
    private protected static final long PROFILE = 1146756268066L;
    private protected static final long REMOVED_PROCS = 2246267895818L;
    private protected static final long RUNNING_VOICE = 1146756268060L;
    private protected static final long SCREEN_COMPAT_PACKAGES = 2246267895830L;
    private protected static final long SLEEP_STATUS = 1146756268059L;
    private protected static final long SYSTEM_READY = 1133871366184L;
    private protected static final long TOTAL_PERSISTENT_PROCS = 1120986464294L;
    private protected static final long TRACK_ALLOCATION_APP = 1138166333473L;
    private protected static final long UID_OBSERVERS = 2246267895831L;
    private protected static final long USER_CONTROLLER = 1146756268046L;
    private protected static final long VALIDATE_UIDS = 2246267895813L;
    private protected static final long VR_CONTROLLER = 1146756268061L;

    private protected synchronized ActivityManagerServiceDumpProcessesProto() {
    }

    /* loaded from: classes3.dex */
    public final class LruProcesses {
        private protected static final long LIST = 2246267895812L;
        private protected static final long NON_ACT_AT = 1120986464258L;
        private protected static final long NON_SVC_AT = 1120986464259L;
        private protected static final long SIZE = 1120986464257L;

        public LruProcesses() {
        }
    }

    /* loaded from: classes3.dex */
    public final class ScreenCompatPackage {
        private protected static final long MODE = 1120986464258L;
        private protected static final long PACKAGE = 1138166333441L;

        public ScreenCompatPackage() {
        }
    }

    /* loaded from: classes3.dex */
    public final class UidObserverRegistrationProto {
        private protected static final long CUT_POINT = 1120986464260L;
        private protected static final long FLAGS = 2259152797699L;
        private protected static final long LAST_PROC_STATES = 2246267895813L;
        private protected static final long PACKAGE = 1138166333442L;
        private protected static final long UID = 1120986464257L;

        public UidObserverRegistrationProto() {
        }

        /* loaded from: classes3.dex */
        public final class ProcState {
            private protected static final long STATE = 1120986464258L;
            private protected static final long UID = 1120986464257L;

            public ProcState() {
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class PendingTempWhitelist {
        private protected static final long DURATION_MS = 1112396529666L;
        private protected static final long TAG = 1138166333443L;
        private protected static final long TARGET_UID = 1120986464257L;

        public PendingTempWhitelist() {
        }
    }

    /* loaded from: classes3.dex */
    public final class SleepStatus {
        private protected static final long SHUTTING_DOWN = 1133871366148L;
        private protected static final long SLEEPING = 1133871366147L;
        private protected static final long SLEEP_TOKENS = 2237677961218L;
        private protected static final long TEST_PSS_MODE = 1133871366149L;
        private protected static final long WAKEFULNESS = 1159641169921L;

        public SleepStatus() {
        }
    }

    /* loaded from: classes3.dex */
    public final class Voice {
        private protected static final long SESSION = 1138166333441L;
        private protected static final long WAKELOCK = 1146756268034L;

        public Voice() {
        }
    }

    /* loaded from: classes3.dex */
    public final class DebugApp {
        private protected static final long DEBUG_APP = 1138166333441L;
        private protected static final long DEBUG_TRANSIENT = 1133871366147L;
        private protected static final long ORIG_DEBUG_APP = 1138166333442L;
        private protected static final long ORIG_WAIT_FOR_DEBUGGER = 1133871366148L;

        public DebugApp() {
        }
    }

    /* loaded from: classes3.dex */
    public final class MemWatchProcess {
        private protected static final long DUMP = 1146756268034L;
        private protected static final long PROCS = 2246267895809L;

        public MemWatchProcess() {
        }

        /* loaded from: classes3.dex */
        public final class Process {
            private protected static final long MEM_STATS = 2246267895810L;
            private protected static final long NAME = 1138166333441L;

            public Process() {
            }

            /* loaded from: classes3.dex */
            public final class MemStats {
                private protected static final long REPORT_TO = 1138166333443L;
                private protected static final long SIZE = 1138166333442L;
                private protected static final long UID = 1120986464257L;

                public MemStats() {
                }
            }
        }

        /* loaded from: classes3.dex */
        public final class Dump {
            private protected static final long FILE = 1138166333442L;
            private protected static final long PID = 1120986464259L;
            private protected static final long PROC_NAME = 1138166333441L;
            private protected static final long UID = 1120986464260L;

            public Dump() {
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class Profile {
        private protected static final long APP_NAME = 1138166333441L;
        private protected static final long INFO = 1146756268035L;
        private protected static final long PROC = 1146756268034L;
        private protected static final long TYPE = 1120986464260L;

        public Profile() {
        }
    }

    /* loaded from: classes3.dex */
    public final class Controller {
        private protected static final long CONTROLLER = 1138166333441L;
        private protected static final long IS_A_MONKEY = 1133871366146L;

        public Controller() {
        }
    }
}
