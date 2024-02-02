package android.media;
/* loaded from: classes.dex */
public class AudioDevicePortConfig extends AudioPortConfig {
    public private protected AudioDevicePortConfig(AudioDevicePort devicePort, int samplingRate, int channelMask, int format, AudioGainConfig gain) {
        super(devicePort, samplingRate, channelMask, format, gain);
    }

    synchronized AudioDevicePortConfig(AudioDevicePortConfig config) {
        this(config.m42port(), config.samplingRate(), config.channelMask(), config.format(), config.gain());
    }

    /* renamed from: port */
    public synchronized AudioDevicePort m42port() {
        return (AudioDevicePort) this.mPort;
    }
}
