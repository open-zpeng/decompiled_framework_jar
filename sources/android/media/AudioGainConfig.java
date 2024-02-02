package android.media;
/* loaded from: classes.dex */
public class AudioGainConfig {
    public protected final int mChannelMask;
    AudioGain mGain;
    public protected final int mIndex;
    public protected final int mMode;
    public protected final int mRampDurationMs;
    public protected final int[] mValues;

    public private protected AudioGainConfig(int index, AudioGain gain, int mode, int channelMask, int[] values, int rampDurationMs) {
        this.mIndex = index;
        this.mGain = gain;
        this.mMode = mode;
        this.mChannelMask = channelMask;
        this.mValues = values;
        this.mRampDurationMs = rampDurationMs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int index() {
        return this.mIndex;
    }

    public synchronized int mode() {
        return this.mMode;
    }

    public synchronized int channelMask() {
        return this.mChannelMask;
    }

    public synchronized int[] values() {
        return this.mValues;
    }

    public synchronized int rampDurationMs() {
        return this.mRampDurationMs;
    }
}
