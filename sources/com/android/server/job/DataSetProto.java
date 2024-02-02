package com.android.server.job;
/* loaded from: classes3.dex */
public final class DataSetProto {
    private protected static final long ELAPSED_TIME_MS = 1112396529666L;
    private protected static final long MAX_CONCURRENCY = 1120986464261L;
    private protected static final long MAX_FOREGROUND_CONCURRENCY = 1120986464262L;
    private protected static final long PACKAGE_ENTRIES = 2246267895812L;
    private protected static final long PERIOD_MS = 1112396529667L;
    private protected static final long START_CLOCK_TIME_MS = 1112396529665L;

    private protected synchronized DataSetProto() {
    }

    /* loaded from: classes3.dex */
    public final class PackageEntryProto {
        private protected static final long ACTIVE = 1133871366151L;
        private protected static final long ACTIVE_STATE = 1146756268036L;
        private protected static final long ACTIVE_TOP = 1133871366152L;
        private protected static final long ACTIVE_TOP_STATE = 1146756268037L;
        private protected static final long PACKAGE_NAME = 1138166333442L;
        private protected static final long PENDING = 1133871366150L;
        private protected static final long PENDING_STATE = 1146756268035L;
        private protected static final long STOP_REASONS = 2246267895817L;
        private protected static final long UID = 1120986464257L;

        public PackageEntryProto() {
        }

        /* loaded from: classes3.dex */
        public final class State {
            private protected static final long COUNT = 1120986464258L;
            private protected static final long DURATION_MS = 1112396529665L;

            public State() {
            }
        }

        /* loaded from: classes3.dex */
        public final class StopReasonCount {
            private protected static final long COUNT = 1120986464258L;
            private protected static final long REASON = 1159641169921L;

            public StopReasonCount() {
            }
        }
    }
}
