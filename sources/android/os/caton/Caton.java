package android.os.caton;

/* loaded from: classes2.dex */
public class Caton {
    static final long DEFAULT_COLLECT_INTERVAL = 1000;
    static MonitorMode DEFAULT_MODE = MonitorMode.LOOPER;
    static final long DEFAULT_THRESHOLD_TIME = 3000;
    static final long MAX_COLLECT_INTERVAL = 400;
    static final long MAX_THRESHOLD_TIME = 400;
    static final long MIN_COLLECT_INTERVAL = 200;
    static final long MIN_THRESHOLD_TIME = 200;
    private static volatile Caton sCaton;
    public BlockHandler mBlockHandler;

    /* loaded from: classes2.dex */
    public interface Callback {
        void onBlockOccurs(String[] strArr, boolean z, String str, long... jArr);
    }

    private Caton(long thresholdTimeMillis, long collectIntervalMillis, MonitorMode mode, boolean loggingEnabled, Callback callback) {
        long thresholdTime = Math.min(Math.max(thresholdTimeMillis, 200L), 400L);
        long collectInterval = Math.min(Math.max(collectIntervalMillis, 200L), 400L);
        Config.LOG_ENABLED = loggingEnabled;
        Config.THRESHOLD_TIME = thresholdTime;
        Collector mTraceCollector = new StackTraceCollector(collectInterval);
        this.mBlockHandler = new BlockHandler(mTraceCollector, callback);
    }

    public static void initialize() {
        initialize(new Builder());
    }

    public static void initialize(Builder builder) {
        if (sCaton == null) {
            synchronized (Caton.class) {
                if (sCaton == null) {
                    sCaton = builder.build();
                }
            }
        }
    }

    public static BlockHandler getBlockHandler() {
        if (sCaton == null) {
            return null;
        }
        return sCaton.mBlockHandler;
    }

    public static void setLoggingEnabled(boolean enabled) {
        Config.LOG_ENABLED = enabled;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private Callback mCallback;
        private long mThresholdTime = Caton.DEFAULT_THRESHOLD_TIME;
        private long mCollectInterval = 1000;
        private MonitorMode mMonitorMode = Caton.DEFAULT_MODE;
        private boolean loggingEnabled = true;

        public Builder thresholdTime(long thresholdTimeMillis) {
            this.mThresholdTime = thresholdTimeMillis;
            return this;
        }

        public Builder collectInterval(long collectIntervalMillis) {
            this.mCollectInterval = collectIntervalMillis;
            return this;
        }

        public Builder monitorMode(MonitorMode mode) {
            this.mMonitorMode = mode;
            return this;
        }

        public Builder loggingEnabled(boolean enable) {
            this.loggingEnabled = enable;
            return this;
        }

        public Builder callback(Callback callback) {
            this.mCallback = callback;
            return this;
        }

        Caton build() {
            return new Caton(this.mThresholdTime, this.mCollectInterval, this.mMonitorMode, this.loggingEnabled, this.mCallback);
        }
    }

    /* loaded from: classes2.dex */
    public enum MonitorMode {
        LOOPER(0),
        FRAME(1);
        
        int value;

        MonitorMode(int mode) {
            this.value = mode;
        }
    }
}
