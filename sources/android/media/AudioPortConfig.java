package android.media;
/* loaded from: classes.dex */
public class AudioPortConfig {
    static final int CHANNEL_MASK = 2;
    static final int FORMAT = 4;
    static final int GAIN = 8;
    static final int SAMPLE_RATE = 1;
    public protected final int mChannelMask;
    public private protected int mConfigMask = 0;
    public protected final int mFormat;
    public protected final AudioGainConfig mGain;
    public private protected final AudioPort mPort;
    public protected final int mSamplingRate;

    public private protected AudioPortConfig(AudioPort port, int samplingRate, int channelMask, int format, AudioGainConfig gain) {
        this.mPort = port;
        this.mSamplingRate = samplingRate;
        this.mChannelMask = channelMask;
        this.mFormat = format;
        this.mGain = gain;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AudioPort port() {
        return this.mPort;
    }

    public synchronized int samplingRate() {
        return this.mSamplingRate;
    }

    public synchronized int channelMask() {
        return this.mChannelMask;
    }

    public synchronized int format() {
        return this.mFormat;
    }

    public synchronized AudioGainConfig gain() {
        return this.mGain;
    }

    public String toString() {
        return "{mPort:" + this.mPort + ", mSamplingRate:" + this.mSamplingRate + ", mChannelMask: " + this.mChannelMask + ", mFormat:" + this.mFormat + ", mGain:" + this.mGain + "}";
    }
}
