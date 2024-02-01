package android.media;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes2.dex */
public class AudioGainConfig {
    @UnsupportedAppUsage
    private final int mChannelMask;
    AudioGain mGain;
    @UnsupportedAppUsage
    private final int mIndex;
    @UnsupportedAppUsage
    private final int mMode;
    @UnsupportedAppUsage
    private final int mRampDurationMs;
    @UnsupportedAppUsage
    private final int[] mValues;

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public AudioGainConfig(int index, AudioGain gain, int mode, int channelMask, int[] values, int rampDurationMs) {
        this.mIndex = index;
        this.mGain = gain;
        this.mMode = mode;
        this.mChannelMask = channelMask;
        this.mValues = values;
        this.mRampDurationMs = rampDurationMs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int index() {
        return this.mIndex;
    }

    public int mode() {
        return this.mMode;
    }

    public int channelMask() {
        return this.mChannelMask;
    }

    public int[] values() {
        return this.mValues;
    }

    public int rampDurationMs() {
        return this.mRampDurationMs;
    }
}
