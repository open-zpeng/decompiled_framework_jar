package com.android.server.job;
/* loaded from: classes3.dex */
public final class JobPackageHistoryProto {
    private protected static final long HISTORY_EVENT = 2246267895809L;
    private protected static final int START_JOB = 1;
    private protected static final int START_PERIODIC_JOB = 3;
    private protected static final int STOP_JOB = 2;
    private protected static final int STOP_PERIODIC_JOB = 4;
    private protected static final int UNKNOWN = 0;

    private protected synchronized JobPackageHistoryProto() {
    }

    /* loaded from: classes3.dex */
    public final class HistoryEvent {
        private protected static final long EVENT = 1159641169921L;
        private protected static final long JOB_ID = 1120986464260L;
        private protected static final long STOP_REASON = 1159641169926L;
        private protected static final long TAG = 1138166333445L;
        private protected static final long TIME_SINCE_EVENT_MS = 1112396529666L;
        private protected static final long UID = 1120986464259L;

        public HistoryEvent() {
        }
    }
}
