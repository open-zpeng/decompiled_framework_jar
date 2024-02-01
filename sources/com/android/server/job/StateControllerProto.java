package com.android.server.job;
/* loaded from: classes3.dex */
public final class StateControllerProto {
    private protected static final long BACKGROUND = 1146756268033L;
    private protected static final long BATTERY = 1146756268034L;
    private protected static final long CONNECTIVITY = 1146756268035L;
    private protected static final long CONTENT_OBSERVER = 1146756268036L;
    private protected static final long DEVICE_IDLE = 1146756268037L;
    private protected static final long IDLE = 1146756268038L;
    private protected static final long STORAGE = 1146756268039L;
    private protected static final long TIME = 1146756268040L;

    private protected synchronized StateControllerProto() {
    }

    /* loaded from: classes3.dex */
    public final class BackgroundJobsController {
        private protected static final long FORCE_APP_STANDBY_TRACKER = 1146756268033L;
        private protected static final long TRACKED_JOBS = 2246267895810L;

        public BackgroundJobsController() {
        }

        /* loaded from: classes3.dex */
        public final class TrackedJob {
            private protected static final long ARE_CONSTRAINTS_SATISFIED = 1133871366151L;
            private protected static final long CAN_RUN_ANY_IN_BACKGROUND = 1133871366150L;
            private protected static final long INFO = 1146756268033L;
            private protected static final long IS_IN_FOREGROUND = 1133871366148L;
            private protected static final long IS_WHITELISTED = 1133871366149L;
            private protected static final long SOURCE_PACKAGE_NAME = 1138166333443L;
            private protected static final long SOURCE_UID = 1120986464258L;

            public TrackedJob() {
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class BatteryController {
        private protected static final long IS_BATTERY_NOT_LOW = 1133871366146L;
        private protected static final long IS_MONITORING = 1133871366147L;
        private protected static final long IS_ON_STABLE_POWER = 1133871366145L;
        private protected static final long LAST_BROADCAST_SEQUENCE_NUMBER = 1120986464260L;
        private protected static final long TRACKED_JOBS = 2246267895813L;

        public BatteryController() {
        }

        /* loaded from: classes3.dex */
        public final class TrackedJob {
            private protected static final long INFO = 1146756268033L;
            private protected static final long SOURCE_UID = 1120986464258L;

            public TrackedJob() {
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class ConnectivityController {
        private protected static final long IS_CONNECTED = 1133871366145L;
        private protected static final long TRACKED_JOBS = 2246267895810L;

        public ConnectivityController() {
        }

        /* loaded from: classes3.dex */
        public final class TrackedJob {
            private protected static final long INFO = 1146756268033L;
            private protected static final long REQUIRED_NETWORK = 1146756268035L;
            private protected static final long SOURCE_UID = 1120986464258L;

            public TrackedJob() {
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class ContentObserverController {
        private protected static final long OBSERVERS = 2246267895810L;
        private protected static final long TRACKED_JOBS = 2246267895809L;

        public ContentObserverController() {
        }

        /* loaded from: classes3.dex */
        public final class TrackedJob {
            private protected static final long INFO = 1146756268033L;
            private protected static final long SOURCE_UID = 1120986464258L;

            public TrackedJob() {
            }
        }

        /* loaded from: classes3.dex */
        public final class Observer {
            private protected static final long TRIGGERS = 2246267895810L;
            private protected static final long USER_ID = 1120986464257L;

            public Observer() {
            }

            /* loaded from: classes3.dex */
            public final class TriggerContentData {
                private protected static final long FLAGS = 1120986464258L;
                private protected static final long JOBS = 2246267895811L;
                private protected static final long URI = 1138166333441L;

                public TriggerContentData() {
                }

                /* loaded from: classes3.dex */
                public final class JobInstance {
                    private protected static final long CHANGED_AUTHORITIES = 2237677961221L;
                    private protected static final long CHANGED_URIS = 2237677961222L;
                    private protected static final long INFO = 1146756268033L;
                    private protected static final long SOURCE_UID = 1120986464258L;
                    private protected static final long TRIGGER_CONTENT_MAX_DELAY_MS = 1112396529668L;
                    private protected static final long TRIGGER_CONTENT_UPDATE_DELAY_MS = 1112396529667L;

                    public JobInstance() {
                    }
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class DeviceIdleJobsController {
        private protected static final long IS_DEVICE_IDLE_MODE = 1133871366145L;
        private protected static final long TRACKED_JOBS = 2246267895810L;

        public DeviceIdleJobsController() {
        }

        /* loaded from: classes3.dex */
        public final class TrackedJob {
            private protected static final long ARE_CONSTRAINTS_SATISFIED = 1133871366148L;
            private protected static final long INFO = 1146756268033L;
            private protected static final long IS_ALLOWED_IN_DOZE = 1133871366150L;
            private protected static final long IS_DOZE_WHITELISTED = 1133871366149L;
            private protected static final long SOURCE_PACKAGE_NAME = 1138166333443L;
            private protected static final long SOURCE_UID = 1120986464258L;

            public TrackedJob() {
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class IdleController {
        private protected static final long IS_IDLE = 1133871366145L;
        private protected static final long TRACKED_JOBS = 2246267895810L;

        public IdleController() {
        }

        /* loaded from: classes3.dex */
        public final class TrackedJob {
            private protected static final long INFO = 1146756268033L;
            private protected static final long SOURCE_UID = 1120986464258L;

            public TrackedJob() {
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class StorageController {
        private protected static final long IS_STORAGE_NOT_LOW = 1133871366145L;
        private protected static final long LAST_BROADCAST_SEQUENCE_NUMBER = 1120986464258L;
        private protected static final long TRACKED_JOBS = 2246267895811L;

        public StorageController() {
        }

        /* loaded from: classes3.dex */
        public final class TrackedJob {
            private protected static final long INFO = 1146756268033L;
            private protected static final long SOURCE_UID = 1120986464258L;

            public TrackedJob() {
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class TimeController {
        private protected static final long NOW_ELAPSED_REALTIME = 1112396529665L;
        private protected static final long TIME_UNTIL_NEXT_DEADLINE_ALARM_MS = 1112396529667L;
        private protected static final long TIME_UNTIL_NEXT_DELAY_ALARM_MS = 1112396529666L;
        private protected static final long TRACKED_JOBS = 2246267895812L;

        public TimeController() {
        }

        /* loaded from: classes3.dex */
        public final class TrackedJob {
            private protected static final long DELAY_TIME_REMAINING_MS = 1112396529668L;
            private protected static final long HAS_DEADLINE_CONSTRAINT = 1133871366149L;
            private protected static final long HAS_TIMING_DELAY_CONSTRAINT = 1133871366147L;
            private protected static final long INFO = 1146756268033L;
            private protected static final long SOURCE_UID = 1120986464258L;
            private protected static final long TIME_REMAINING_UNTIL_DEADLINE_MS = 1112396529670L;

            public TrackedJob() {
            }
        }
    }
}
