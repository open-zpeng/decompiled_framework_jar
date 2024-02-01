package android.media;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes2.dex */
public class AudioDevicePortConfig extends AudioPortConfig {
    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public AudioDevicePortConfig(AudioDevicePort devicePort, int samplingRate, int channelMask, int format, AudioGainConfig gain) {
        super(devicePort, samplingRate, channelMask, format, gain);
    }

    AudioDevicePortConfig(AudioDevicePortConfig config) {
        this(config.port(), config.samplingRate(), config.channelMask(), config.format(), config.gain());
    }

    @Override // android.media.AudioPortConfig
    public AudioDevicePort port() {
        return (AudioDevicePort) this.mPort;
    }
}
