package android.service.procstats;
/* loaded from: classes2.dex */
public final class ProcessStatsProto {
    private protected static final long KILL = 1146756268035L;
    private protected static final long PROCESS = 1138166333441L;
    private protected static final long STATES = 2246267895813L;
    private protected static final long UID = 1120986464258L;

    private protected synchronized ProcessStatsProto() {
    }

    /* loaded from: classes2.dex */
    public final class Kill {
        private protected static final long CACHED = 1120986464258L;
        private protected static final long CACHED_PSS = 1146756268035L;
        private protected static final long CPU = 1120986464257L;

        public Kill() {
        }
    }

    /* loaded from: classes2.dex */
    public final class State {
        private protected static final int BACKUP = 5;
        private protected static final int CACHED_ACTIVITY = 12;
        private protected static final int CACHED_ACTIVITY_CLIENT = 13;
        private protected static final int CACHED_EMPTY = 14;
        private protected static final int CRITICAL = 4;
        private protected static final long DURATION_MS = 1112396529668L;
        private protected static final int HEAVY_WEIGHT = 9;
        private protected static final int HOME = 10;
        private protected static final int IMPORTANT_BACKGROUND = 4;
        private protected static final int IMPORTANT_FOREGROUND = 3;
        private protected static final int LAST_ACTIVITY = 11;
        private protected static final int LOW = 3;
        private protected static final long MEMORY_STATE = 1159641169922L;
        private protected static final int MEMORY_UNKNOWN = 0;
        private protected static final int MODERATE = 2;
        private protected static final int NORMAL = 1;
        private protected static final int OFF = 1;
        private protected static final int ON = 2;
        private protected static final int PERSISTENT = 1;
        private protected static final long PROCESS_STATE = 1159641169923L;
        private protected static final int PROCESS_UNKNOWN = 0;
        private protected static final long PSS = 1146756268038L;
        private protected static final int RECEIVER = 8;
        private protected static final long RSS = 1146756268040L;
        private protected static final long SAMPLE_SIZE = 1120986464261L;
        private protected static final long SCREEN_STATE = 1159641169921L;
        private protected static final int SCREEN_UNKNOWN = 0;
        private protected static final int SERVICE = 6;
        private protected static final int SERVICE_RESTARTING = 7;
        private protected static final int TOP = 2;
        private protected static final long USS = 1146756268039L;

        public State() {
        }
    }
}
