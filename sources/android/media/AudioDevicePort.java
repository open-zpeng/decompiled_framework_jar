package android.media;
/* loaded from: classes.dex */
public class AudioDevicePort extends AudioPort {
    private final String mAddress;
    private final int mType;

    public private protected AudioDevicePort(AudioHandle handle, String deviceName, int[] samplingRates, int[] channelMasks, int[] channelIndexMasks, int[] formats, AudioGain[] gains, int type, String address) {
        super(handle, AudioManager.isInputDevice(type) ? 1 : 2, deviceName, samplingRates, channelMasks, channelIndexMasks, formats, gains);
        this.mType = type;
        this.mAddress = address;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int type() {
        return this.mType;
    }

    public synchronized String address() {
        return this.mAddress;
    }

    @Override // android.media.AudioPort
    public synchronized AudioDevicePortConfig buildConfig(int samplingRate, int channelMask, int format, AudioGainConfig gain) {
        return new AudioDevicePortConfig(this, samplingRate, channelMask, format, gain);
    }

    @Override // android.media.AudioPort
    public boolean equals(Object o) {
        if (o == null || !(o instanceof AudioDevicePort)) {
            return false;
        }
        AudioDevicePort other = (AudioDevicePort) o;
        if (this.mType != other.type()) {
            return false;
        }
        if ((this.mAddress == null && other.address() != null) || !this.mAddress.equals(other.address())) {
            return false;
        }
        return super.equals(o);
    }

    @Override // android.media.AudioPort
    public String toString() {
        String type;
        if (this.mRole == 1) {
            type = AudioSystem.getInputDeviceName(this.mType);
        } else {
            type = AudioSystem.getOutputDeviceName(this.mType);
        }
        return "{" + super.toString() + ", mType: " + type + ", mAddress: " + this.mAddress + "}";
    }
}
