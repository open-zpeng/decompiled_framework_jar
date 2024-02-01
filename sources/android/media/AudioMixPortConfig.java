package android.media;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes2.dex */
public class AudioMixPortConfig extends AudioPortConfig {
    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public AudioMixPortConfig(AudioMixPort mixPort, int samplingRate, int channelMask, int format, AudioGainConfig gain) {
        super(mixPort, samplingRate, channelMask, format, gain);
    }

    @Override // android.media.AudioPortConfig
    public AudioMixPort port() {
        return (AudioMixPort) this.mPort;
    }
}
