package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BluetoothCodecConfig implements Parcelable {
    private protected static final int BITS_PER_SAMPLE_16 = 1;
    private protected static final int BITS_PER_SAMPLE_24 = 2;
    private protected static final int BITS_PER_SAMPLE_32 = 4;
    private protected static final int BITS_PER_SAMPLE_NONE = 0;
    private protected static final int CHANNEL_MODE_MONO = 1;
    private protected static final int CHANNEL_MODE_NONE = 0;
    private protected static final int CHANNEL_MODE_STEREO = 2;
    private protected static final int CODEC_PRIORITY_DEFAULT = 0;
    private protected static final int CODEC_PRIORITY_DISABLED = -1;
    private protected static final int CODEC_PRIORITY_HIGHEST = 1000000;
    public static final Parcelable.Creator<BluetoothCodecConfig> CREATOR = new Parcelable.Creator<BluetoothCodecConfig>() { // from class: android.bluetooth.BluetoothCodecConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothCodecConfig createFromParcel(Parcel in) {
            int codecType = in.readInt();
            int codecPriority = in.readInt();
            int sampleRate = in.readInt();
            int bitsPerSample = in.readInt();
            int channelMode = in.readInt();
            long codecSpecific1 = in.readLong();
            long codecSpecific2 = in.readLong();
            long codecSpecific3 = in.readLong();
            long codecSpecific4 = in.readLong();
            return new BluetoothCodecConfig(codecType, codecPriority, sampleRate, bitsPerSample, channelMode, codecSpecific1, codecSpecific2, codecSpecific3, codecSpecific4);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothCodecConfig[] newArray(int size) {
            return new BluetoothCodecConfig[size];
        }
    };
    private protected static final int SAMPLE_RATE_176400 = 16;
    private protected static final int SAMPLE_RATE_192000 = 32;
    private protected static final int SAMPLE_RATE_44100 = 1;
    private protected static final int SAMPLE_RATE_48000 = 2;
    private protected static final int SAMPLE_RATE_88200 = 4;
    private protected static final int SAMPLE_RATE_96000 = 8;
    private protected static final int SAMPLE_RATE_NONE = 0;
    private protected static final int SOURCE_CODEC_TYPE_AAC = 1;
    private protected static final int SOURCE_CODEC_TYPE_APTX = 2;
    private protected static final int SOURCE_CODEC_TYPE_APTX_HD = 3;
    private protected static final int SOURCE_CODEC_TYPE_INVALID = 1000000;
    private protected static final int SOURCE_CODEC_TYPE_LDAC = 4;
    private protected static final int SOURCE_CODEC_TYPE_MAX = 5;
    private protected static final int SOURCE_CODEC_TYPE_SBC = 0;
    private final int mBitsPerSample;
    private final int mChannelMode;
    private int mCodecPriority;
    private final long mCodecSpecific1;
    private final long mCodecSpecific2;
    private final long mCodecSpecific3;
    private final long mCodecSpecific4;
    private final int mCodecType;
    private final int mSampleRate;

    private protected BluetoothCodecConfig(int codecType, int codecPriority, int sampleRate, int bitsPerSample, int channelMode, long codecSpecific1, long codecSpecific2, long codecSpecific3, long codecSpecific4) {
        this.mCodecType = codecType;
        this.mCodecPriority = codecPriority;
        this.mSampleRate = sampleRate;
        this.mBitsPerSample = bitsPerSample;
        this.mChannelMode = channelMode;
        this.mCodecSpecific1 = codecSpecific1;
        this.mCodecSpecific2 = codecSpecific2;
        this.mCodecSpecific3 = codecSpecific3;
        this.mCodecSpecific4 = codecSpecific4;
    }

    public boolean equals(Object o) {
        if (o instanceof BluetoothCodecConfig) {
            BluetoothCodecConfig other = (BluetoothCodecConfig) o;
            return other.mCodecType == this.mCodecType && other.mCodecPriority == this.mCodecPriority && other.mSampleRate == this.mSampleRate && other.mBitsPerSample == this.mBitsPerSample && other.mChannelMode == this.mChannelMode && other.mCodecSpecific1 == this.mCodecSpecific1 && other.mCodecSpecific2 == this.mCodecSpecific2 && other.mCodecSpecific3 == this.mCodecSpecific3 && other.mCodecSpecific4 == this.mCodecSpecific4;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mCodecType), Integer.valueOf(this.mCodecPriority), Integer.valueOf(this.mSampleRate), Integer.valueOf(this.mBitsPerSample), Integer.valueOf(this.mChannelMode), Long.valueOf(this.mCodecSpecific1), Long.valueOf(this.mCodecSpecific2), Long.valueOf(this.mCodecSpecific3), Long.valueOf(this.mCodecSpecific4));
    }

    public synchronized boolean isValid() {
        return (this.mSampleRate == 0 || this.mBitsPerSample == 0 || this.mChannelMode == 0) ? false : true;
    }

    private static synchronized String appendCapabilityToString(String prevStr, String capStr) {
        if (prevStr == null) {
            return capStr;
        }
        return prevStr + "|" + capStr;
    }

    public String toString() {
        String sampleRateStr = this.mSampleRate == 0 ? appendCapabilityToString(null, "NONE") : null;
        if ((this.mSampleRate & 1) != 0) {
            sampleRateStr = appendCapabilityToString(sampleRateStr, "44100");
        }
        if ((this.mSampleRate & 2) != 0) {
            sampleRateStr = appendCapabilityToString(sampleRateStr, "48000");
        }
        if ((this.mSampleRate & 4) != 0) {
            sampleRateStr = appendCapabilityToString(sampleRateStr, "88200");
        }
        if ((this.mSampleRate & 8) != 0) {
            sampleRateStr = appendCapabilityToString(sampleRateStr, "96000");
        }
        if ((this.mSampleRate & 16) != 0) {
            sampleRateStr = appendCapabilityToString(sampleRateStr, "176400");
        }
        if ((this.mSampleRate & 32) != 0) {
            sampleRateStr = appendCapabilityToString(sampleRateStr, "192000");
        }
        String bitsPerSampleStr = this.mBitsPerSample == 0 ? appendCapabilityToString(null, "NONE") : null;
        if ((this.mBitsPerSample & 1) != 0) {
            bitsPerSampleStr = appendCapabilityToString(bitsPerSampleStr, "16");
        }
        if ((this.mBitsPerSample & 2) != 0) {
            bitsPerSampleStr = appendCapabilityToString(bitsPerSampleStr, "24");
        }
        if ((this.mBitsPerSample & 4) != 0) {
            bitsPerSampleStr = appendCapabilityToString(bitsPerSampleStr, "32");
        }
        String channelModeStr = this.mChannelMode == 0 ? appendCapabilityToString(null, "NONE") : null;
        if ((this.mChannelMode & 1) != 0) {
            channelModeStr = appendCapabilityToString(channelModeStr, "MONO");
        }
        if ((this.mChannelMode & 2) != 0) {
            channelModeStr = appendCapabilityToString(channelModeStr, "STEREO");
        }
        return "{codecName:" + getCodecName() + ",mCodecType:" + this.mCodecType + ",mCodecPriority:" + this.mCodecPriority + ",mSampleRate:" + String.format("0x%x", Integer.valueOf(this.mSampleRate)) + "(" + sampleRateStr + "),mBitsPerSample:" + String.format("0x%x", Integer.valueOf(this.mBitsPerSample)) + "(" + bitsPerSampleStr + "),mChannelMode:" + String.format("0x%x", Integer.valueOf(this.mChannelMode)) + "(" + channelModeStr + "),mCodecSpecific1:" + this.mCodecSpecific1 + ",mCodecSpecific2:" + this.mCodecSpecific2 + ",mCodecSpecific3:" + this.mCodecSpecific3 + ",mCodecSpecific4:" + this.mCodecSpecific4 + "}";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mCodecType);
        out.writeInt(this.mCodecPriority);
        out.writeInt(this.mSampleRate);
        out.writeInt(this.mBitsPerSample);
        out.writeInt(this.mChannelMode);
        out.writeLong(this.mCodecSpecific1);
        out.writeLong(this.mCodecSpecific2);
        out.writeLong(this.mCodecSpecific3);
        out.writeLong(this.mCodecSpecific4);
    }

    public synchronized String getCodecName() {
        int i = this.mCodecType;
        if (i != 1000000) {
            switch (i) {
                case 0:
                    return "SBC";
                case 1:
                    return "AAC";
                case 2:
                    return "aptX";
                case 3:
                    return "aptX HD";
                case 4:
                    return "LDAC";
                default:
                    return "UNKNOWN CODEC(" + this.mCodecType + ")";
            }
        }
        return "INVALID CODEC";
    }

    private protected int getCodecType() {
        return this.mCodecType;
    }

    public synchronized boolean isMandatoryCodec() {
        return this.mCodecType == 0;
    }

    private protected int getCodecPriority() {
        return this.mCodecPriority;
    }

    private protected void setCodecPriority(int codecPriority) {
        this.mCodecPriority = codecPriority;
    }

    private protected int getSampleRate() {
        return this.mSampleRate;
    }

    private protected int getBitsPerSample() {
        return this.mBitsPerSample;
    }

    private protected int getChannelMode() {
        return this.mChannelMode;
    }

    private protected long getCodecSpecific1() {
        return this.mCodecSpecific1;
    }

    private protected long getCodecSpecific2() {
        return this.mCodecSpecific2;
    }

    private protected long getCodecSpecific3() {
        return this.mCodecSpecific3;
    }

    private protected long getCodecSpecific4() {
        return this.mCodecSpecific4;
    }

    public synchronized boolean sameAudioFeedingParameters(BluetoothCodecConfig other) {
        return other != null && other.mSampleRate == this.mSampleRate && other.mBitsPerSample == this.mBitsPerSample && other.mChannelMode == this.mChannelMode;
    }
}
