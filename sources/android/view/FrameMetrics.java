package android.view;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes2.dex */
public final class FrameMetrics {
    public static final int ANIMATION_DURATION = 2;
    public static final int COMMAND_ISSUE_DURATION = 6;
    public static final int DRAW_DURATION = 4;
    private static final int[] DURATIONS = {1, 5, 5, 6, 6, 7, 7, 8, 8, 9, 10, 11, 11, 12, 12, 13, 1, 13};
    public static final int FIRST_DRAW_FRAME = 9;
    private static final int FRAME_INFO_FLAG_FIRST_DRAW = 1;
    public static final int INPUT_HANDLING_DURATION = 1;
    public static final int INTENDED_VSYNC_TIMESTAMP = 10;
    public static final int LAYOUT_MEASURE_DURATION = 3;
    public static final int SWAP_BUFFERS_DURATION = 7;
    public static final int SYNC_DURATION = 5;
    public static final int TOTAL_DURATION = 8;
    public static final int UNKNOWN_DELAY_DURATION = 0;
    public static final int VSYNC_TIMESTAMP = 11;
    public private protected final long[] mTimingData;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    private @interface Index {
        public static final int ANIMATION_START = 6;
        public static final int DRAW_START = 8;
        public static final int FLAGS = 0;
        public static final int FRAME_COMPLETED = 13;
        public static final int FRAME_STATS_COUNT = 16;
        public static final int HANDLE_INPUT_START = 5;
        public static final int INTENDED_VSYNC = 1;
        public static final int ISSUE_DRAW_COMMANDS_START = 11;
        public static final int NEWEST_INPUT_EVENT = 4;
        public static final int OLDEST_INPUT_EVENT = 3;
        public static final int PERFORM_TRAVERSALS_START = 7;
        public static final int SWAP_BUFFERS = 12;
        public static final int SYNC_QUEUED = 9;
        public static final int SYNC_START = 10;
        public static final int VSYNC = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Metric {
    }

    public FrameMetrics(FrameMetrics other) {
        this.mTimingData = new long[16];
        System.arraycopy(other.mTimingData, 0, this.mTimingData, 0, this.mTimingData.length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized FrameMetrics() {
        this.mTimingData = new long[16];
    }

    public long getMetric(int id) {
        if (id < 0 || id > 11 || this.mTimingData == null) {
            return -1L;
        }
        if (id == 9) {
            return (this.mTimingData[0] & 1) != 0 ? 1L : 0L;
        } else if (id == 10) {
            return this.mTimingData[1];
        } else {
            if (id == 11) {
                return this.mTimingData[2];
            }
            int durationsIdx = 2 * id;
            return this.mTimingData[DURATIONS[durationsIdx + 1]] - this.mTimingData[DURATIONS[durationsIdx]];
        }
    }
}
