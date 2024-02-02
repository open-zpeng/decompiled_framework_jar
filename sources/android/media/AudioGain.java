package android.media;
/* loaded from: classes.dex */
public class AudioGain {
    public static final int MODE_CHANNELS = 2;
    public static final int MODE_JOINT = 1;
    public static final int MODE_RAMP = 4;
    private final int mChannelMask;
    private final int mDefaultValue;
    private final int mIndex;
    private final int mMaxValue;
    private final int mMinValue;
    private final int mMode;
    private final int mRampDurationMaxMs;
    private final int mRampDurationMinMs;
    private final int mStepValue;

    public private protected AudioGain(int index, int mode, int channelMask, int minValue, int maxValue, int defaultValue, int stepValue, int rampDurationMinMs, int rampDurationMaxMs) {
        this.mIndex = index;
        this.mMode = mode;
        this.mChannelMask = channelMask;
        this.mMinValue = minValue;
        this.mMaxValue = maxValue;
        this.mDefaultValue = defaultValue;
        this.mStepValue = stepValue;
        this.mRampDurationMinMs = rampDurationMinMs;
        this.mRampDurationMaxMs = rampDurationMaxMs;
    }

    public synchronized int mode() {
        return this.mMode;
    }

    public synchronized int channelMask() {
        return this.mChannelMask;
    }

    public synchronized int minValue() {
        return this.mMinValue;
    }

    public synchronized int maxValue() {
        return this.mMaxValue;
    }

    public synchronized int defaultValue() {
        return this.mDefaultValue;
    }

    public synchronized int stepValue() {
        return this.mStepValue;
    }

    public synchronized int rampDurationMinMs() {
        return this.mRampDurationMinMs;
    }

    public synchronized int rampDurationMaxMs() {
        return this.mRampDurationMaxMs;
    }

    public synchronized AudioGainConfig buildConfig(int mode, int channelMask, int[] values, int rampDurationMs) {
        return new AudioGainConfig(this.mIndex, this, mode, channelMask, values, rampDurationMs);
    }
}
