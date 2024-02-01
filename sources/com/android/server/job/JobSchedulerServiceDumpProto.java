package com.android.server.job;
/* loaded from: classes3.dex */
public final class JobSchedulerServiceDumpProto {
    private protected static final long ACTIVE_JOBS = 2246267895818L;
    private protected static final long BACKING_UP_UIDS = 2220498092038L;
    private protected static final long CONTROLLERS = 2246267895812L;
    private protected static final long CURRENT_HEARTBEAT = 1120986464270L;
    private protected static final long HISTORY = 1146756268039L;
    private protected static final long IN_PAROLE = 1133871366162L;
    private protected static final long IS_READY_TO_ROCK = 1133871366155L;
    private protected static final long LAST_HEARTBEAT_TIME_MILLIS = 1112396529680L;
    private protected static final long MAX_ACTIVE_JOBS = 1120986464269L;
    private protected static final long NEXT_HEARTBEAT = 2220498092047L;
    private protected static final long NEXT_HEARTBEAT_TIME_MILLIS = 1112396529681L;
    private protected static final long PACKAGE_TRACKER = 1146756268040L;
    private protected static final long PENDING_JOBS = 2246267895817L;
    private protected static final long PRIORITY_OVERRIDES = 2246267895813L;
    private protected static final long REGISTERED_JOBS = 2246267895811L;
    private protected static final long REPORTED_ACTIVE = 1133871366156L;
    private protected static final long SETTINGS = 1146756268033L;
    private protected static final long STARTED_USERS = 2220498092034L;

    private protected synchronized JobSchedulerServiceDumpProto() {
    }

    /* loaded from: classes3.dex */
    public final class RegisteredJob {
        private protected static final long DUMP = 1146756268034L;
        private protected static final long INFO = 1146756268033L;
        private protected static final long IS_COMPONENT_PRESENT = 1133871366152L;
        private protected static final long IS_JOB_CURRENTLY_ACTIVE = 1133871366150L;
        private protected static final long IS_JOB_PENDING = 1133871366149L;
        private protected static final long IS_JOB_READY = 1133871366147L;
        private protected static final long IS_UID_BACKING_UP = 1133871366151L;
        private protected static final long IS_USER_STARTED = 1133871366148L;
        private protected static final long LAST_RUN_HEARTBEAT = 1112396529673L;

        public RegisteredJob() {
        }
    }

    /* loaded from: classes3.dex */
    public final class PriorityOverride {
        private protected static final long OVERRIDE_VALUE = 1172526071810L;
        private protected static final long UID = 1120986464257L;

        public PriorityOverride() {
        }
    }

    /* loaded from: classes3.dex */
    public final class PendingJob {
        private protected static final long DUMP = 1146756268034L;
        private protected static final long ENQUEUED_DURATION_MS = 1112396529668L;
        private protected static final long EVALUATED_PRIORITY = 1172526071811L;
        private protected static final long INFO = 1146756268033L;

        public PendingJob() {
        }
    }

    /* loaded from: classes3.dex */
    public final class ActiveJob {
        private protected static final long INACTIVE = 1146756268033L;
        private protected static final long RUNNING = 1146756268034L;

        public ActiveJob() {
        }

        /* loaded from: classes3.dex */
        public final class InactiveJob {
            private protected static final long STOPPED_REASON = 1138166333442L;
            private protected static final long TIME_SINCE_STOPPED_MS = 1112396529665L;

            public InactiveJob() {
            }
        }

        /* loaded from: classes3.dex */
        public final class RunningJob {
            private protected static final long DUMP = 1146756268036L;
            private protected static final long EVALUATED_PRIORITY = 1172526071813L;
            private protected static final long INFO = 1146756268033L;
            private protected static final long PENDING_DURATION_MS = 1112396529671L;
            private protected static final long RUNNING_DURATION_MS = 1112396529666L;
            private protected static final long TIME_SINCE_MADE_ACTIVE_MS = 1112396529670L;
            private protected static final long TIME_UNTIL_TIMEOUT_MS = 1112396529667L;

            public RunningJob() {
            }
        }
    }
}
