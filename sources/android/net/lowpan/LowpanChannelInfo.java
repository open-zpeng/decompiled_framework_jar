package android.net.lowpan;

import android.net.wifi.WifiInfo;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes2.dex */
public class LowpanChannelInfo implements Parcelable {
    private protected static final Parcelable.Creator<LowpanChannelInfo> CREATOR = new Parcelable.Creator<LowpanChannelInfo>() { // from class: android.net.lowpan.LowpanChannelInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LowpanChannelInfo createFromParcel(Parcel in) {
            LowpanChannelInfo info = new LowpanChannelInfo();
            info.mIndex = in.readInt();
            info.mName = in.readString();
            info.mSpectrumCenterFrequency = in.readFloat();
            info.mSpectrumBandwidth = in.readFloat();
            info.mMaxTransmitPower = in.readInt();
            info.mIsMaskedByRegulatoryDomain = in.readBoolean();
            return info;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LowpanChannelInfo[] newArray(int size) {
            return new LowpanChannelInfo[size];
        }
    };
    private protected static final float UNKNOWN_BANDWIDTH = 0.0f;
    private protected static final float UNKNOWN_FREQUENCY = 0.0f;
    private protected static final int UNKNOWN_POWER = Integer.MAX_VALUE;
    public protected int mIndex;
    public protected boolean mIsMaskedByRegulatoryDomain;
    public protected int mMaxTransmitPower;
    public protected String mName;
    public protected float mSpectrumBandwidth;
    public protected float mSpectrumCenterFrequency;

    private protected static synchronized LowpanChannelInfo getChannelInfoForIeee802154Page0(int index) {
        LowpanChannelInfo info = new LowpanChannelInfo();
        if (index < 0) {
            info = null;
        } else if (index == 0) {
            info.mSpectrumCenterFrequency = 8.683E8f;
            info.mSpectrumBandwidth = 600000.0f;
        } else if (index < 11) {
            info.mSpectrumCenterFrequency = 9.04E8f + (2000000.0f * index);
            info.mSpectrumBandwidth = 0.0f;
        } else if (index < 26) {
            info.mSpectrumCenterFrequency = 2.3499999E9f + (5000000.0f * index);
            info.mSpectrumBandwidth = 2000000.0f;
        } else {
            info = null;
        }
        info.mName = Integer.toString(index);
        return info;
    }

    public protected synchronized LowpanChannelInfo() {
        this.mIndex = 0;
        this.mName = null;
        this.mSpectrumCenterFrequency = 0.0f;
        this.mSpectrumBandwidth = 0.0f;
        this.mMaxTransmitPower = Integer.MAX_VALUE;
        this.mIsMaskedByRegulatoryDomain = false;
    }

    public protected synchronized LowpanChannelInfo(int index, String name, float cf, float bw) {
        this.mIndex = 0;
        this.mName = null;
        this.mSpectrumCenterFrequency = 0.0f;
        this.mSpectrumBandwidth = 0.0f;
        this.mMaxTransmitPower = Integer.MAX_VALUE;
        this.mIsMaskedByRegulatoryDomain = false;
        this.mIndex = index;
        this.mName = name;
        this.mSpectrumCenterFrequency = cf;
        this.mSpectrumBandwidth = bw;
    }

    private protected synchronized String getName() {
        return this.mName;
    }

    private protected synchronized int getIndex() {
        return this.mIndex;
    }

    private protected synchronized int getMaxTransmitPower() {
        return this.mMaxTransmitPower;
    }

    private protected synchronized boolean isMaskedByRegulatoryDomain() {
        return this.mIsMaskedByRegulatoryDomain;
    }

    private protected synchronized float getSpectrumCenterFrequency() {
        return this.mSpectrumCenterFrequency;
    }

    private protected synchronized float getSpectrumBandwidth() {
        return this.mSpectrumBandwidth;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Channel ");
        sb.append(this.mIndex);
        if (this.mName != null && !this.mName.equals(Integer.toString(this.mIndex))) {
            sb.append(" (");
            sb.append(this.mName);
            sb.append(")");
        }
        if (this.mSpectrumCenterFrequency > 0.0f) {
            if (this.mSpectrumCenterFrequency > 1.0E9f) {
                sb.append(", SpectrumCenterFrequency: ");
                sb.append(this.mSpectrumCenterFrequency / 1.0E9f);
                sb.append("GHz");
            } else if (this.mSpectrumCenterFrequency > 1000000.0f) {
                sb.append(", SpectrumCenterFrequency: ");
                sb.append(this.mSpectrumCenterFrequency / 1000000.0f);
                sb.append(WifiInfo.FREQUENCY_UNITS);
            } else {
                sb.append(", SpectrumCenterFrequency: ");
                sb.append(this.mSpectrumCenterFrequency / 1000.0f);
                sb.append("kHz");
            }
        }
        if (this.mSpectrumBandwidth > 0.0f) {
            if (this.mSpectrumBandwidth > 1.0E9f) {
                sb.append(", SpectrumBandwidth: ");
                sb.append(this.mSpectrumBandwidth / 1.0E9f);
                sb.append("GHz");
            } else if (this.mSpectrumBandwidth > 1000000.0f) {
                sb.append(", SpectrumBandwidth: ");
                sb.append(this.mSpectrumBandwidth / 1000000.0f);
                sb.append(WifiInfo.FREQUENCY_UNITS);
            } else {
                sb.append(", SpectrumBandwidth: ");
                sb.append(this.mSpectrumBandwidth / 1000.0f);
                sb.append("kHz");
            }
        }
        if (this.mMaxTransmitPower != Integer.MAX_VALUE) {
            sb.append(", MaxTransmitPower: ");
            sb.append(this.mMaxTransmitPower);
            sb.append("dBm");
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof LowpanChannelInfo) {
            LowpanChannelInfo rhs = (LowpanChannelInfo) obj;
            return Objects.equals(this.mName, rhs.mName) && this.mIndex == rhs.mIndex && this.mIsMaskedByRegulatoryDomain == rhs.mIsMaskedByRegulatoryDomain && this.mSpectrumCenterFrequency == rhs.mSpectrumCenterFrequency && this.mSpectrumBandwidth == rhs.mSpectrumBandwidth && this.mMaxTransmitPower == rhs.mMaxTransmitPower;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mName, Integer.valueOf(this.mIndex), Boolean.valueOf(this.mIsMaskedByRegulatoryDomain), Float.valueOf(this.mSpectrumCenterFrequency), Float.valueOf(this.mSpectrumBandwidth), Integer.valueOf(this.mMaxTransmitPower));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mIndex);
        dest.writeString(this.mName);
        dest.writeFloat(this.mSpectrumCenterFrequency);
        dest.writeFloat(this.mSpectrumBandwidth);
        dest.writeInt(this.mMaxTransmitPower);
        dest.writeBoolean(this.mIsMaskedByRegulatoryDomain);
    }
}
