package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.net.wifi.ScanResult;
import android.net.wifi.aware.PeerHandle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
@SystemApi
/* loaded from: classes2.dex */
public final class ResponderConfig implements Parcelable {
    private static final int AWARE_BAND_2_DISCOVERY_CHANNEL = 2437;
    public static final int CHANNEL_WIDTH_160MHZ = 3;
    public static final int CHANNEL_WIDTH_20MHZ = 0;
    public static final int CHANNEL_WIDTH_40MHZ = 1;
    public static final int CHANNEL_WIDTH_80MHZ = 2;
    public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;
    public static final Parcelable.Creator<ResponderConfig> CREATOR = new Parcelable.Creator<ResponderConfig>() { // from class: android.net.wifi.rtt.ResponderConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ResponderConfig[] newArray(int size) {
            return new ResponderConfig[size];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ResponderConfig createFromParcel(Parcel in) {
            boolean macAddressPresent = in.readBoolean();
            MacAddress macAddress = null;
            if (macAddressPresent) {
                MacAddress macAddress2 = MacAddress.CREATOR.createFromParcel(in);
                macAddress = macAddress2;
            }
            boolean peerHandlePresent = in.readBoolean();
            PeerHandle peerHandle = null;
            if (peerHandlePresent) {
                peerHandle = new PeerHandle(in.readInt());
            }
            PeerHandle peerHandle2 = peerHandle;
            int responderType = in.readInt();
            boolean supports80211mc = in.readInt() == 1;
            int channelWidth = in.readInt();
            int frequency = in.readInt();
            int centerFreq0 = in.readInt();
            int centerFreq1 = in.readInt();
            int preamble = in.readInt();
            if (peerHandle2 == null) {
                return new ResponderConfig(macAddress, responderType, supports80211mc, channelWidth, frequency, centerFreq0, centerFreq1, preamble);
            }
            return new ResponderConfig(peerHandle2, responderType, supports80211mc, channelWidth, frequency, centerFreq0, centerFreq1, preamble);
        }
    };
    public static final int PREAMBLE_HT = 1;
    public static final int PREAMBLE_LEGACY = 0;
    public static final int PREAMBLE_VHT = 2;
    public static final int RESPONDER_AP = 0;
    public static final int RESPONDER_AWARE = 4;
    public static final int RESPONDER_P2P_CLIENT = 3;
    public static final int RESPONDER_P2P_GO = 2;
    public static final int RESPONDER_STA = 1;
    private static final String TAG = "ResponderConfig";
    public final int centerFreq0;
    public final int centerFreq1;
    public final int channelWidth;
    public final int frequency;
    public final MacAddress macAddress;
    public final PeerHandle peerHandle;
    public final int preamble;
    public final int responderType;
    public final boolean supports80211mc;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ChannelWidth {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface PreambleType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ResponderType {
    }

    public ResponderConfig(MacAddress macAddress, int responderType, boolean supports80211mc, int channelWidth, int frequency, int centerFreq0, int centerFreq1, int preamble) {
        if (macAddress == null) {
            throw new IllegalArgumentException("Invalid ResponderConfig - must specify a MAC address");
        }
        this.macAddress = macAddress;
        this.peerHandle = null;
        this.responderType = responderType;
        this.supports80211mc = supports80211mc;
        this.channelWidth = channelWidth;
        this.frequency = frequency;
        this.centerFreq0 = centerFreq0;
        this.centerFreq1 = centerFreq1;
        this.preamble = preamble;
    }

    public ResponderConfig(PeerHandle peerHandle, int responderType, boolean supports80211mc, int channelWidth, int frequency, int centerFreq0, int centerFreq1, int preamble) {
        this.macAddress = null;
        this.peerHandle = peerHandle;
        this.responderType = responderType;
        this.supports80211mc = supports80211mc;
        this.channelWidth = channelWidth;
        this.frequency = frequency;
        this.centerFreq0 = centerFreq0;
        this.centerFreq1 = centerFreq1;
        this.preamble = preamble;
    }

    public synchronized ResponderConfig(MacAddress macAddress, PeerHandle peerHandle, int responderType, boolean supports80211mc, int channelWidth, int frequency, int centerFreq0, int centerFreq1, int preamble) {
        this.macAddress = macAddress;
        this.peerHandle = peerHandle;
        this.responderType = responderType;
        this.supports80211mc = supports80211mc;
        this.channelWidth = channelWidth;
        this.frequency = frequency;
        this.centerFreq0 = centerFreq0;
        this.centerFreq1 = centerFreq1;
        this.preamble = preamble;
    }

    public static ResponderConfig fromScanResult(ScanResult scanResult) {
        int i;
        ScanResult.InformationElement[] informationElementArr;
        int preamble;
        MacAddress macAddress = MacAddress.fromString(scanResult.BSSID);
        boolean supports80211mc = scanResult.is80211mcResponder();
        int channelWidth = translateScanResultChannelWidth(scanResult.channelWidth);
        int frequency = scanResult.frequency;
        int centerFreq0 = scanResult.centerFreq0;
        int centerFreq1 = scanResult.centerFreq1;
        if (scanResult.informationElements != null && scanResult.informationElements.length != 0) {
            boolean vhtCapabilitiesPresent = false;
            boolean htCapabilitiesPresent = false;
            for (ScanResult.InformationElement ie : scanResult.informationElements) {
                if (ie.id == 45) {
                    htCapabilitiesPresent = true;
                } else if (ie.id == 191) {
                    vhtCapabilitiesPresent = true;
                }
            }
            if (vhtCapabilitiesPresent) {
                preamble = 2;
            } else {
                preamble = htCapabilitiesPresent ? 1 : 0;
            }
            i = preamble;
        } else {
            Log.e(TAG, "Scan Results do not contain IEs - using backup method to select preamble");
            if (channelWidth == 2 || channelWidth == 3) {
                i = 2;
            } else {
                i = 1;
            }
        }
        int preamble2 = i;
        return new ResponderConfig(macAddress, 0, supports80211mc, channelWidth, frequency, centerFreq0, centerFreq1, preamble2);
    }

    public static ResponderConfig fromWifiAwarePeerMacAddressWithDefaults(MacAddress macAddress) {
        return new ResponderConfig(macAddress, 4, true, 0, (int) AWARE_BAND_2_DISCOVERY_CHANNEL, 0, 0, 1);
    }

    public static ResponderConfig fromWifiAwarePeerHandleWithDefaults(PeerHandle peerHandle) {
        return new ResponderConfig(peerHandle, 4, true, 0, (int) AWARE_BAND_2_DISCOVERY_CHANNEL, 0, 0, 1);
    }

    public synchronized boolean isValid(boolean awareSupported) {
        if (!(this.macAddress == null && this.peerHandle == null) && (this.macAddress == null || this.peerHandle == null)) {
            return awareSupported || this.responderType != 4;
        }
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (this.macAddress == null) {
            dest.writeBoolean(false);
        } else {
            dest.writeBoolean(true);
            this.macAddress.writeToParcel(dest, flags);
        }
        if (this.peerHandle == null) {
            dest.writeBoolean(false);
        } else {
            dest.writeBoolean(true);
            dest.writeInt(this.peerHandle.peerId);
        }
        dest.writeInt(this.responderType);
        dest.writeInt(this.supports80211mc ? 1 : 0);
        dest.writeInt(this.channelWidth);
        dest.writeInt(this.frequency);
        dest.writeInt(this.centerFreq0);
        dest.writeInt(this.centerFreq1);
        dest.writeInt(this.preamble);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ResponderConfig) {
            ResponderConfig lhs = (ResponderConfig) o;
            return Objects.equals(this.macAddress, lhs.macAddress) && Objects.equals(this.peerHandle, lhs.peerHandle) && this.responderType == lhs.responderType && this.supports80211mc == lhs.supports80211mc && this.channelWidth == lhs.channelWidth && this.frequency == lhs.frequency && this.centerFreq0 == lhs.centerFreq0 && this.centerFreq1 == lhs.centerFreq1 && this.preamble == lhs.preamble;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.macAddress, this.peerHandle, Integer.valueOf(this.responderType), Boolean.valueOf(this.supports80211mc), Integer.valueOf(this.channelWidth), Integer.valueOf(this.frequency), Integer.valueOf(this.centerFreq0), Integer.valueOf(this.centerFreq1), Integer.valueOf(this.preamble));
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("ResponderConfig: macAddress=");
        stringBuffer.append(this.macAddress);
        stringBuffer.append(", peerHandle=");
        stringBuffer.append(this.peerHandle == null ? "<null>" : Integer.valueOf(this.peerHandle.peerId));
        stringBuffer.append(", responderType=");
        stringBuffer.append(this.responderType);
        stringBuffer.append(", supports80211mc=");
        stringBuffer.append(this.supports80211mc);
        stringBuffer.append(", channelWidth=");
        stringBuffer.append(this.channelWidth);
        stringBuffer.append(", frequency=");
        stringBuffer.append(this.frequency);
        stringBuffer.append(", centerFreq0=");
        stringBuffer.append(this.centerFreq0);
        stringBuffer.append(", centerFreq1=");
        stringBuffer.append(this.centerFreq1);
        stringBuffer.append(", preamble=");
        stringBuffer.append(this.preamble);
        return stringBuffer.toString();
    }

    static synchronized int translateScanResultChannelWidth(int scanResultChannelWidth) {
        switch (scanResultChannelWidth) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                throw new IllegalArgumentException("translateScanResultChannelWidth: bad " + scanResultChannelWidth);
        }
    }
}
