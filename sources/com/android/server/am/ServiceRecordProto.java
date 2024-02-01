package com.android.server.am;
/* loaded from: classes3.dex */
public final class ServiceRecordProto {
    private protected static final long APP = 1146756268041L;
    private protected static final long APPINFO = 1146756268040L;
    private protected static final long BINDINGS = 2246267895833L;
    private protected static final long CONNECTIONS = 2246267895834L;
    private protected static final long CRASH = 1146756268054L;
    private protected static final long CREATED_FROM_FG = 1133871366162L;
    private protected static final long CREATE_REAL_TIME = 1146756268046L;
    private protected static final long DELAYED = 1133871366156L;
    private protected static final long DELIVERED_STARTS = 2246267895831L;
    private protected static final long DESTORY_TIME = 1146756268053L;
    private protected static final long EXECUTE = 1146756268052L;
    private protected static final long FOREGROUND = 1146756268045L;
    private protected static final long INTENT = 1146756268036L;
    private protected static final long ISOLATED_PROC = 1146756268042L;
    private protected static final long IS_RUNNING = 1133871366146L;
    private protected static final long LAST_ACTIVITY_TIME = 1146756268048L;
    private protected static final long PACKAGE_NAME = 1138166333445L;
    private protected static final long PENDING_STARTS = 2246267895832L;
    private protected static final long PERMISSION = 1138166333447L;
    private protected static final long PID = 1120986464259L;
    private protected static final long PROCESS_NAME = 1138166333446L;
    private protected static final long RESTART_TIME = 1146756268049L;
    private protected static final long SHORT_NAME = 1138166333441L;
    private protected static final long START = 1146756268051L;
    private protected static final long STARTING_BG_TIMEOUT = 1146756268047L;
    private protected static final long WHITELIST_MANAGER = 1133871366155L;

    private protected synchronized ServiceRecordProto() {
    }

    /* loaded from: classes3.dex */
    public final class AppInfo {
        private protected static final long BASE_DIR = 1138166333441L;
        private protected static final long DATA_DIR = 1138166333443L;
        private protected static final long RES_DIR = 1138166333442L;

        public AppInfo() {
        }
    }

    /* loaded from: classes3.dex */
    public final class Foreground {
        private protected static final long ID = 1120986464257L;
        private protected static final long NOTIFICATION = 1146756268034L;

        public Foreground() {
        }
    }

    /* loaded from: classes3.dex */
    public final class Start {
        private protected static final long CALL_START = 1133871366148L;
        private protected static final long DELAYED_STOP = 1133871366146L;
        private protected static final long LAST_START_ID = 1120986464261L;
        private protected static final long START_REQUESTED = 1133871366145L;
        private protected static final long STOP_IF_KILLED = 1133871366147L;

        public Start() {
        }
    }

    /* loaded from: classes3.dex */
    public final class ExecuteNesting {
        private protected static final long EXECUTE_FG = 1133871366146L;
        private protected static final long EXECUTE_NESTING = 1120986464257L;
        private protected static final long EXECUTING_START = 1146756268035L;

        public ExecuteNesting() {
        }
    }

    /* loaded from: classes3.dex */
    public final class Crash {
        private protected static final long CRASH_COUNT = 1120986464260L;
        private protected static final long NEXT_RESTART_TIME = 1146756268035L;
        private protected static final long RESTART_COUNT = 1120986464257L;
        private protected static final long RESTART_DELAY = 1146756268034L;

        public Crash() {
        }
    }

    /* loaded from: classes3.dex */
    public final class StartItem {
        private protected static final long DELIVERY_COUNT = 1120986464259L;
        private protected static final long DONE_EXECUTING_COUNT = 1120986464260L;
        private protected static final long DURATION = 1146756268034L;
        private protected static final long ID = 1120986464257L;
        private protected static final long INTENT = 1146756268037L;
        private protected static final long NEEDED_GRANTS = 1146756268038L;
        private protected static final long URI_PERMISSIONS = 1146756268039L;

        public StartItem() {
        }
    }
}
