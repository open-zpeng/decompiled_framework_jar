package com.android.server.am;
/* loaded from: classes3.dex */
public final class ProcessOomProto {
    private protected static final long ACTIVITIES = 1133871366149L;
    private protected static final long ADJ_SOURCE_OBJECT = 1138166333454L;
    private protected static final long ADJ_SOURCE_PROC = 1146756268045L;
    private protected static final long ADJ_TARGET_COMPONENT_NAME = 1146756268043L;
    private protected static final long ADJ_TARGET_OBJECT = 1138166333452L;
    private protected static final long ADJ_TYPE = 1138166333450L;
    private protected static final long DETAIL = 1146756268047L;
    private protected static final long NUM = 1120986464258L;
    private protected static final long OOM_ADJ = 1138166333443L;
    private protected static final long PERSISTENT = 1133871366145L;
    private protected static final long PROC = 1146756268041L;
    private protected static final long SCHED_GROUP = 1159641169924L;
    private protected static final int SCHED_GROUP_BACKGROUND = 0;
    private protected static final int SCHED_GROUP_DEFAULT = 1;
    private protected static final int SCHED_GROUP_TOP_APP = 2;
    private protected static final int SCHED_GROUP_TOP_APP_BOUND = 3;
    private protected static final int SCHED_GROUP_UNKNOWN = -1;
    private protected static final long SERVICES = 1133871366150L;
    private protected static final long STATE = 1159641169927L;
    private protected static final long TRIM_MEMORY_LEVEL = 1120986464264L;

    private protected synchronized ProcessOomProto() {
    }

    /* loaded from: classes3.dex */
    public final class Detail {
        private protected static final long CACHED = 1133871366156L;
        private protected static final long CURRENT_STATE = 1159641169927L;
        private protected static final long CUR_ADJ = 1120986464260L;
        private protected static final long CUR_RAW_ADJ = 1120986464258L;
        private protected static final long EMPTY = 1133871366157L;
        private protected static final long HAS_ABOVE_CLIENT = 1133871366158L;
        private protected static final long LAST_CACHED_PSS = 1138166333451L;
        private protected static final long LAST_PSS = 1138166333449L;
        private protected static final long LAST_SWAP_PSS = 1138166333450L;
        private protected static final long MAX_ADJ = 1120986464257L;
        private protected static final long SERVICE_RUN_TIME = 1146756268047L;
        private protected static final long SET_ADJ = 1120986464261L;
        private protected static final long SET_RAW_ADJ = 1120986464259L;
        private protected static final long SET_STATE = 1159641169928L;

        public Detail() {
        }

        /* loaded from: classes3.dex */
        public final class CpuRunTime {
            private protected static final long OVER_MS = 1112396529665L;
            private protected static final long ULTILIZATION = 1108101562371L;
            private protected static final long USED_MS = 1112396529666L;

            public CpuRunTime() {
            }
        }
    }
}
