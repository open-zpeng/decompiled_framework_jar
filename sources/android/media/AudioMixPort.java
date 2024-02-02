package android.media;
/* loaded from: classes.dex */
public class AudioMixPort extends AudioPort {
    private final int mIoHandle;

    public private protected AudioMixPort(AudioHandle handle, int ioHandle, int role, String deviceName, int[] samplingRates, int[] channelMasks, int[] channelIndexMasks, int[] formats, AudioGain[] gains) {
        super(handle, role, deviceName, samplingRates, channelMasks, channelIndexMasks, formats, gains);
        this.mIoHandle = ioHandle;
    }

    @Override // android.media.AudioPort
    public synchronized AudioMixPortConfig buildConfig(int samplingRate, int channelMask, int format, AudioGainConfig gain) {
        return new AudioMixPortConfig(this, samplingRate, channelMask, format, gain);
    }

    private protected int ioHandle() {
        return this.mIoHandle;
    }

    @Override // android.media.AudioPort
    public boolean equals(Object o) {
        if (o == null || !(o instanceof AudioMixPort)) {
            return false;
        }
        AudioMixPort other = (AudioMixPort) o;
        if (this.mIoHandle != other.ioHandle()) {
            return false;
        }
        return super.equals(o);
    }
}
