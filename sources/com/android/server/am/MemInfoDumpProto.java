package com.android.server.am;
/* loaded from: classes3.dex */
public final class MemInfoDumpProto {
    private protected static final long APP_PROCESSES = 2246267895812L;
    private protected static final long CACHED_KERNEL_KB = 1112396529675L;
    private protected static final long CACHED_PSS_KB = 1112396529674L;
    private protected static final long ELAPSED_REALTIME_MS = 1112396529666L;
    private protected static final long FREE_KB = 1112396529676L;
    private protected static final long IS_HIGH_END_GFX = 1133871366172L;
    private protected static final long IS_LOW_RAM_DEVICE = 1133871366171L;
    private protected static final long KSM_SHARED_KB = 1112396529684L;
    private protected static final long KSM_SHARING_KB = 1112396529683L;
    private protected static final long KSM_UNSHARED_KB = 1112396529685L;
    private protected static final long KSM_VOLATILE_KB = 1112396529686L;
    private protected static final long LOST_RAM_KB = 1112396529679L;
    private protected static final long NATIVE_PROCESSES = 2246267895811L;
    private protected static final long OOM_KB = 1112396529689L;
    private protected static final long RESTORE_LIMIT_KB = 1112396529690L;
    private protected static final long STATUS = 1159641169929L;
    private protected static final long TOTAL_PSS_BY_CATEGORY = 2246267895815L;
    private protected static final long TOTAL_PSS_BY_OOM_ADJUSTMENT = 2246267895814L;
    private protected static final long TOTAL_PSS_BY_PROCESS = 2246267895813L;
    private protected static final long TOTAL_RAM_KB = 1112396529672L;
    private protected static final long TOTAL_ZRAM_KB = 1112396529680L;
    private protected static final long TOTAL_ZRAM_SWAP_KB = 1112396529682L;
    private protected static final long TUNING_LARGE_MB = 1120986464280L;
    private protected static final long TUNING_MB = 1120986464279L;
    private protected static final long UPTIME_DURATION_MS = 1112396529665L;
    private protected static final long USED_KERNEL_KB = 1112396529678L;
    private protected static final long USED_PSS_KB = 1112396529677L;
    private protected static final long ZRAM_PHYSICAL_USED_IN_SWAP_KB = 1112396529681L;

    private protected synchronized MemInfoDumpProto() {
    }

    /* loaded from: classes3.dex */
    public final class ProcessMemory {
        private protected static final long APP_SUMMARY = 1146756268041L;
        private protected static final long DALVIK_DETAILS = 2246267895816L;
        private protected static final long DALVIK_HEAP = 1146756268036L;
        private protected static final long NATIVE_HEAP = 1146756268035L;
        private protected static final long OTHER_HEAPS = 2246267895813L;
        private protected static final long PID = 1120986464257L;
        private protected static final long PROCESS_NAME = 1138166333442L;
        private protected static final long TOTAL_HEAP = 1146756268039L;
        private protected static final long UNKNOWN_HEAP = 1146756268038L;

        public ProcessMemory() {
        }

        /* loaded from: classes3.dex */
        public final class MemoryInfo {
            private protected static final long CLEAN_PSS_KB = 1120986464259L;
            private protected static final long DIRTY_SWAP_KB = 1120986464264L;
            private protected static final long DIRTY_SWAP_PSS_KB = 1120986464265L;
            private protected static final long NAME = 1138166333441L;
            private protected static final long PRIVATE_CLEAN_KB = 1120986464263L;
            private protected static final long PRIVATE_DIRTY_KB = 1120986464261L;
            private protected static final long SHARED_CLEAN_KB = 1120986464262L;
            private protected static final long SHARED_DIRTY_KB = 1120986464260L;
            private protected static final long TOTAL_PSS_KB = 1120986464258L;

            public MemoryInfo() {
            }
        }

        /* loaded from: classes3.dex */
        public final class HeapInfo {
            private protected static final long HEAP_ALLOC_KB = 1120986464259L;
            private protected static final long HEAP_FREE_KB = 1120986464260L;
            private protected static final long HEAP_SIZE_KB = 1120986464258L;
            private protected static final long MEM_INFO = 1146756268033L;

            public HeapInfo() {
            }
        }

        /* loaded from: classes3.dex */
        public final class AppSummary {
            private protected static final long CODE_PSS_KB = 1120986464259L;
            private protected static final long GRAPHICS_PSS_KB = 1120986464261L;
            private protected static final long JAVA_HEAP_PSS_KB = 1120986464257L;
            private protected static final long NATIVE_HEAP_PSS_KB = 1120986464258L;
            private protected static final long PRIVATE_OTHER_PSS_KB = 1120986464262L;
            private protected static final long STACK_PSS_KB = 1120986464260L;
            private protected static final long SYSTEM_PSS_KB = 1120986464263L;
            private protected static final long TOTAL_SWAP_KB = 1120986464265L;
            private protected static final long TOTAL_SWAP_PSS = 1120986464264L;

            public AppSummary() {
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class AppData {
        private protected static final long ASSET_ALLOCATIONS = 1138166333444L;
        private protected static final long OBJECTS = 1146756268034L;
        private protected static final long PROCESS_MEMORY = 1146756268033L;
        private protected static final long SQL = 1146756268035L;
        private protected static final long UNREACHABLE_MEMORY = 1138166333445L;

        public AppData() {
        }

        /* loaded from: classes3.dex */
        public final class ObjectStats {
            private protected static final long ACTIVITY_INSTANCE_COUNT = 1120986464260L;
            private protected static final long APP_CONTEXT_INSTANCE_COUNT = 1120986464259L;
            private protected static final long BINDER_OBJECT_DEATH_COUNT = 1120986464267L;
            private protected static final long GLOBAL_ASSET_COUNT = 1120986464261L;
            private protected static final long GLOBAL_ASSET_MANAGER_COUNT = 1120986464262L;
            private protected static final long LOCAL_BINDER_OBJECT_COUNT = 1120986464263L;
            private protected static final long OPEN_SSL_SOCKET_COUNT = 1120986464268L;
            private protected static final long PARCEL_COUNT = 1120986464266L;
            private protected static final long PARCEL_MEMORY_KB = 1112396529673L;
            private protected static final long PROXY_BINDER_OBJECT_COUNT = 1120986464264L;
            private protected static final long VIEW_INSTANCE_COUNT = 1120986464257L;
            private protected static final long VIEW_ROOT_INSTANCE_COUNT = 1120986464258L;
            private protected static final long WEBVIEW_INSTANCE_COUNT = 1120986464269L;

            public ObjectStats() {
            }
        }

        /* loaded from: classes3.dex */
        public final class SqlStats {
            private protected static final long DATABASES = 2246267895812L;
            private protected static final long MALLOC_SIZE_KB = 1120986464259L;
            private protected static final long MEMORY_USED_KB = 1120986464257L;
            private protected static final long PAGECACHE_OVERFLOW_KB = 1120986464258L;

            public SqlStats() {
            }

            /* loaded from: classes3.dex */
            public final class Database {
                private protected static final long CACHE = 1138166333445L;
                private protected static final long DB_SIZE = 1120986464259L;
                private protected static final long LOOKASIDE_B = 1120986464260L;
                private protected static final long NAME = 1138166333441L;
                private protected static final long PAGE_SIZE = 1120986464258L;

                public Database() {
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public final class MemItem {
        private protected static final long HAS_ACTIVITIES = 1133871366149L;
        private protected static final long ID = 1120986464259L;
        private protected static final long IS_PROC = 1133871366148L;
        private protected static final long LABEL = 1138166333442L;
        private protected static final long PSS_KB = 1112396529670L;
        private protected static final long SUB_ITEMS = 2246267895816L;
        private protected static final long SWAP_PSS_KB = 1112396529671L;
        private protected static final long TAG = 1138166333441L;

        public MemItem() {
        }
    }
}
