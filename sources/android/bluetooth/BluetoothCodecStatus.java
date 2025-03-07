package android.bluetooth;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes.dex */
public final class BluetoothCodecStatus implements Parcelable {
    public static final Parcelable.Creator<BluetoothCodecStatus> CREATOR = new Parcelable.Creator<BluetoothCodecStatus>() { // from class: android.bluetooth.BluetoothCodecStatus.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothCodecStatus createFromParcel(Parcel in) {
            BluetoothCodecConfig codecConfig = (BluetoothCodecConfig) in.readTypedObject(BluetoothCodecConfig.CREATOR);
            BluetoothCodecConfig[] codecsLocalCapabilities = (BluetoothCodecConfig[]) in.createTypedArray(BluetoothCodecConfig.CREATOR);
            BluetoothCodecConfig[] codecsSelectableCapabilities = (BluetoothCodecConfig[]) in.createTypedArray(BluetoothCodecConfig.CREATOR);
            return new BluetoothCodecStatus(codecConfig, codecsLocalCapabilities, codecsSelectableCapabilities);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothCodecStatus[] newArray(int size) {
            return new BluetoothCodecStatus[size];
        }
    };
    @UnsupportedAppUsage
    public static final String EXTRA_CODEC_STATUS = "android.bluetooth.codec.extra.CODEC_STATUS";
    private final BluetoothCodecConfig mCodecConfig;
    private final BluetoothCodecConfig[] mCodecsLocalCapabilities;
    private final BluetoothCodecConfig[] mCodecsSelectableCapabilities;

    public BluetoothCodecStatus(BluetoothCodecConfig codecConfig, BluetoothCodecConfig[] codecsLocalCapabilities, BluetoothCodecConfig[] codecsSelectableCapabilities) {
        this.mCodecConfig = codecConfig;
        this.mCodecsLocalCapabilities = codecsLocalCapabilities;
        this.mCodecsSelectableCapabilities = codecsSelectableCapabilities;
    }

    public boolean equals(Object o) {
        if (o instanceof BluetoothCodecStatus) {
            BluetoothCodecStatus other = (BluetoothCodecStatus) o;
            return Objects.equals(other.mCodecConfig, this.mCodecConfig) && sameCapabilities(other.mCodecsLocalCapabilities, this.mCodecsLocalCapabilities) && sameCapabilities(other.mCodecsSelectableCapabilities, this.mCodecsSelectableCapabilities);
        }
        return false;
    }

    public static boolean sameCapabilities(BluetoothCodecConfig[] c1, BluetoothCodecConfig[] c2) {
        if (c1 == null) {
            return c2 == null;
        } else if (c2 == null || c1.length != c2.length) {
            return false;
        } else {
            return Arrays.asList(c1).containsAll(Arrays.asList(c2));
        }
    }

    public boolean isCodecConfigSelectable(BluetoothCodecConfig codecConfig) {
        BluetoothCodecConfig[] bluetoothCodecConfigArr;
        if (codecConfig == null || !codecConfig.hasSingleSampleRate() || !codecConfig.hasSingleBitsPerSample() || !codecConfig.hasSingleChannelMode()) {
            return false;
        }
        for (BluetoothCodecConfig selectableConfig : this.mCodecsSelectableCapabilities) {
            if (codecConfig.getCodecType() == selectableConfig.getCodecType()) {
                int sampleRate = codecConfig.getSampleRate();
                if ((selectableConfig.getSampleRate() & sampleRate) != 0 || sampleRate == 0) {
                    int bitsPerSample = codecConfig.getBitsPerSample();
                    if ((selectableConfig.getBitsPerSample() & bitsPerSample) != 0 || bitsPerSample == 0) {
                        int channelMode = codecConfig.getChannelMode();
                        if ((selectableConfig.getChannelMode() & channelMode) != 0 || channelMode == 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int hashCode() {
        BluetoothCodecConfig[] bluetoothCodecConfigArr = this.mCodecsLocalCapabilities;
        return Objects.hash(this.mCodecConfig, bluetoothCodecConfigArr, bluetoothCodecConfigArr);
    }

    public String toString() {
        return "{mCodecConfig:" + this.mCodecConfig + ",mCodecsLocalCapabilities:" + Arrays.toString(this.mCodecsLocalCapabilities) + ",mCodecsSelectableCapabilities:" + Arrays.toString(this.mCodecsSelectableCapabilities) + "}";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedObject(this.mCodecConfig, 0);
        out.writeTypedArray(this.mCodecsLocalCapabilities, 0);
        out.writeTypedArray(this.mCodecsSelectableCapabilities, 0);
    }

    @UnsupportedAppUsage
    public BluetoothCodecConfig getCodecConfig() {
        return this.mCodecConfig;
    }

    @UnsupportedAppUsage
    public BluetoothCodecConfig[] getCodecsLocalCapabilities() {
        return this.mCodecsLocalCapabilities;
    }

    @UnsupportedAppUsage
    public BluetoothCodecConfig[] getCodecsSelectableCapabilities() {
        return this.mCodecsSelectableCapabilities;
    }
}
