package android.media;
/* loaded from: classes.dex */
public class AudioPort {
    public static final int ROLE_NONE = 0;
    public static final int ROLE_SINK = 2;
    public static final int ROLE_SOURCE = 1;
    private static final String TAG = "AudioPort";
    public static final int TYPE_DEVICE = 1;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_SESSION = 3;
    public static final int TYPE_SUBMIX = 2;
    public protected AudioPortConfig mActiveConfig;
    private final int[] mChannelIndexMasks;
    private final int[] mChannelMasks;
    private final int[] mFormats;
    public protected final AudioGain[] mGains;
    public private protected AudioHandle mHandle;
    private final String mName;
    public private final int mRole;
    private final int[] mSamplingRates;

    public private protected AudioPort(AudioHandle handle, int role, String name, int[] samplingRates, int[] channelMasks, int[] channelIndexMasks, int[] formats, AudioGain[] gains) {
        this.mHandle = handle;
        this.mRole = role;
        this.mName = name;
        this.mSamplingRates = samplingRates;
        this.mChannelMasks = channelMasks;
        this.mChannelIndexMasks = channelIndexMasks;
        this.mFormats = formats;
        this.mGains = gains;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized AudioHandle handle() {
        return this.mHandle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int id() {
        return this.mHandle.id();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int role() {
        return this.mRole;
    }

    public synchronized String name() {
        return this.mName;
    }

    public synchronized int[] samplingRates() {
        return this.mSamplingRates;
    }

    public synchronized int[] channelMasks() {
        return this.mChannelMasks;
    }

    public synchronized int[] channelIndexMasks() {
        return this.mChannelIndexMasks;
    }

    public synchronized int[] formats() {
        return this.mFormats;
    }

    public synchronized AudioGain[] gains() {
        return this.mGains;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized AudioGain gain(int index) {
        if (index < 0 || index >= this.mGains.length) {
            return null;
        }
        return this.mGains[index];
    }

    public synchronized AudioPortConfig buildConfig(int samplingRate, int channelMask, int format, AudioGainConfig gain) {
        return new AudioPortConfig(this, samplingRate, channelMask, format, gain);
    }

    public synchronized AudioPortConfig activeConfig() {
        return this.mActiveConfig;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof AudioPort)) {
            return false;
        }
        AudioPort ap = (AudioPort) o;
        return this.mHandle.equals(ap.handle());
    }

    public int hashCode() {
        return this.mHandle.hashCode();
    }

    public String toString() {
        String role = Integer.toString(this.mRole);
        switch (this.mRole) {
            case 0:
                role = "NONE";
                break;
            case 1:
                role = "SOURCE";
                break;
            case 2:
                role = "SINK";
                break;
        }
        return "{mHandle: " + this.mHandle + ", mRole: " + role + "}";
    }
}
