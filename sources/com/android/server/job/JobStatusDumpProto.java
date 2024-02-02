package com.android.server.job;
/* loaded from: classes3.dex */
public final class JobStatusDumpProto {
    private protected static final int ACTIVE = 0;
    private protected static final long CALLING_UID = 1120986464257L;
    private protected static final long CHANGED_AUTHORITIES = 2237677961228L;
    private protected static final long CHANGED_URIS = 2237677961229L;
    private protected static final int CONSTRAINT_BATTERY_NOT_LOW = 2;
    private protected static final int CONSTRAINT_CHARGING = 1;
    private protected static final int CONSTRAINT_CONNECTIVITY = 7;
    private protected static final int CONSTRAINT_CONTENT_TRIGGER = 8;
    private protected static final int CONSTRAINT_DEADLINE = 5;
    private protected static final int CONSTRAINT_DEVICE_NOT_DOZING = 9;
    private protected static final int CONSTRAINT_IDLE = 6;
    private protected static final int CONSTRAINT_STORAGE_NOT_LOW = 3;
    private protected static final int CONSTRAINT_TIMING_DELAY = 4;
    private protected static final long ENQUEUE_DURATION_MS = 1112396529682L;
    private protected static final long EXECUTING_WORK = 2246267895824L;
    private protected static final int FREQUENT = 2;
    private protected static final long INTERNAL_FLAGS = 1112396529688L;
    private protected static final long IS_DOZE_WHITELISTED = 1133871366154L;
    private protected static final long JOB_INFO = 1146756268038L;
    private protected static final long LAST_FAILED_RUN_TIME = 1112396529687L;
    private protected static final long LAST_SUCCESSFUL_RUN_TIME = 1112396529686L;
    private protected static final long NETWORK = 1146756268046L;
    private protected static final int NEVER = 4;
    private protected static final long NUM_FAILURES = 1120986464277L;
    private protected static final long PENDING_WORK = 2246267895823L;
    private protected static final int RARE = 3;
    private protected static final long REQUIRED_CONSTRAINTS = 2259152797703L;
    private protected static final long SATISFIED_CONSTRAINTS = 2259152797704L;
    private protected static final long SOURCE_PACKAGE_NAME = 1138166333445L;
    private protected static final long SOURCE_UID = 1120986464259L;
    private protected static final long SOURCE_USER_ID = 1120986464260L;
    private protected static final long STANDBY_BUCKET = 1159641169937L;
    private protected static final long TAG = 1138166333442L;
    private protected static final long TIME_UNTIL_EARLIEST_RUNTIME_MS = 1176821039123L;
    private protected static final long TIME_UNTIL_LATEST_RUNTIME_MS = 1176821039124L;
    private protected static final int TRACKING_BATTERY = 0;
    private protected static final int TRACKING_CONNECTIVITY = 1;
    private protected static final int TRACKING_CONTENT = 2;
    private protected static final long TRACKING_CONTROLLERS = 2259152797707L;
    private protected static final int TRACKING_IDLE = 3;
    private protected static final int TRACKING_STORAGE = 4;
    private protected static final int TRACKING_TIME = 5;
    private protected static final long UNSATISFIED_CONSTRAINTS = 2259152797705L;
    private protected static final int WORKING_SET = 1;

    private protected synchronized JobStatusDumpProto() {
    }

    /* loaded from: classes3.dex */
    public final class JobInfo {
        private protected static final long BACKOFF_POLICY = 1146756268054L;
        private protected static final long CLIP_DATA = 1146756268048L;
        private protected static final long EXTRAS = 1146756268046L;
        private protected static final long FLAGS = 1120986464263L;
        private protected static final long GRANTED_URI_PERMISSIONS = 1146756268049L;
        private protected static final long HAS_EARLY_CONSTRAINT = 1133871366167L;
        private protected static final long HAS_LATE_CONSTRAINT = 1133871366168L;
        private protected static final long IS_PERIODIC = 1133871366146L;
        private protected static final long IS_PERSISTED = 1133871366149L;
        private protected static final long MAX_EXECUTION_DELAY_MS = 1112396529685L;
        private protected static final long MIN_LATENCY_MS = 1112396529684L;
        private protected static final long PERIOD_FLEX_MS = 1112396529668L;
        private protected static final long PERIOD_INTERVAL_MS = 1112396529667L;
        private protected static final long PRIORITY = 1172526071814L;
        private protected static final long REQUIRED_NETWORK = 1146756268050L;
        private protected static final long REQUIRES_BATTERY_NOT_LOW = 1133871366153L;
        private protected static final long REQUIRES_CHARGING = 1133871366152L;
        private protected static final long REQUIRES_DEVICE_IDLE = 1133871366154L;
        private protected static final long SERVICE = 1146756268033L;
        private protected static final long TOTAL_NETWORK_BYTES = 1112396529683L;
        private protected static final long TRANSIENT_EXTRAS = 1146756268047L;
        private protected static final long TRIGGER_CONTENT_MAX_DELAY_MS = 1112396529677L;
        private protected static final long TRIGGER_CONTENT_UPDATE_DELAY_MS = 1112396529676L;
        private protected static final long TRIGGER_CONTENT_URIS = 2246267895819L;

        public JobInfo() {
        }

        /* loaded from: classes3.dex */
        public final class TriggerContentUri {
            private protected static final long FLAGS = 1120986464257L;
            private protected static final long URI = 1138166333442L;

            public TriggerContentUri() {
            }
        }

        /* loaded from: classes3.dex */
        public final class Backoff {
            private protected static final int BACKOFF_POLICY_EXPONENTIAL = 1;
            private protected static final int BACKOFF_POLICY_LINEAR = 0;
            private protected static final long INITIAL_BACKOFF_MS = 1112396529666L;
            private protected static final long POLICY = 1159641169921L;

            public Backoff() {
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class JobWorkItem {
        private protected static final long DELIVERY_COUNT = 1120986464258L;
        private protected static final long INTENT = 1146756268035L;
        private protected static final long URI_GRANTS = 1146756268036L;
        private protected static final long WORK_ID = 1120986464257L;

        public JobWorkItem() {
        }
    }
}
