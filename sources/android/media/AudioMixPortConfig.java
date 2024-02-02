package android.media;
/* loaded from: classes.dex */
public class AudioMixPortConfig extends AudioPortConfig {
    public private protected AudioMixPortConfig(AudioMixPort mixPort, int samplingRate, int channelMask, int format, AudioGainConfig gain) {
        super(mixPort, samplingRate, channelMask, format, gain);
    }

    /* renamed from: port */
    public synchronized AudioMixPort m43port() {
        return (AudioMixPort) this.mPort;
    }
}
