package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class BluetoothAudioConfig implements Parcelable {
    public static final Parcelable.Creator<BluetoothAudioConfig> CREATOR = new Parcelable.Creator<BluetoothAudioConfig>() { // from class: android.bluetooth.BluetoothAudioConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothAudioConfig createFromParcel(Parcel in) {
            int sampleRate = in.readInt();
            int channelConfig = in.readInt();
            int audioFormat = in.readInt();
            int codecIndex = in.readInt();
            return new BluetoothAudioConfig(sampleRate, channelConfig, audioFormat, codecIndex);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothAudioConfig[] newArray(int size) {
            return new BluetoothAudioConfig[size];
        }
    };
    private final int mAudioFormat;
    private final int mChannelConfig;
    private final int mCodecIndex;
    private final int mSampleRate;

    public BluetoothAudioConfig(int sampleRate, int channelConfig, int audioFormat, int codecIndex) {
        this.mSampleRate = sampleRate;
        this.mChannelConfig = channelConfig;
        this.mAudioFormat = audioFormat;
        this.mCodecIndex = codecIndex;
    }

    public boolean equals(Object o) {
        if (o instanceof BluetoothAudioConfig) {
            BluetoothAudioConfig bac = (BluetoothAudioConfig) o;
            return bac.mSampleRate == this.mSampleRate && bac.mChannelConfig == this.mChannelConfig && bac.mAudioFormat == this.mAudioFormat && bac.mCodecIndex == this.mCodecIndex;
        }
        return false;
    }

    public int hashCode() {
        return this.mSampleRate | (this.mCodecIndex << 20) | (this.mChannelConfig << 24) | (this.mAudioFormat << 28);
    }

    public String toString() {
        return "{mSampleRate:" + this.mSampleRate + ",mChannelConfig:" + this.mChannelConfig + ",mAudioFormat:" + this.mAudioFormat + ",mCodecIndex:" + this.mCodecIndex + "}";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mSampleRate);
        out.writeInt(this.mChannelConfig);
        out.writeInt(this.mAudioFormat);
        out.writeInt(this.mCodecIndex);
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getChannelConfig() {
        return this.mChannelConfig;
    }

    public int getAudioFormat() {
        return this.mAudioFormat;
    }

    public int getCodecIndex() {
        return this.mCodecIndex;
    }
}
