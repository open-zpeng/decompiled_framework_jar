package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/* loaded from: classes2.dex */
public class ScanResult implements Parcelable {
    public static final int CHANNEL_WIDTH_160MHZ = 3;
    public static final int CHANNEL_WIDTH_20MHZ = 0;
    public static final int CHANNEL_WIDTH_40MHZ = 1;
    public static final int CHANNEL_WIDTH_80MHZ = 2;
    public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;
    public static final int CIPHER_CCMP = 3;
    public static final int CIPHER_GCMP = 4;
    public static final int CIPHER_NONE = 0;
    public static final int CIPHER_NO_GROUP_ADDRESSED = 1;
    public static final int CIPHER_TKIP = 2;
    private protected static final Parcelable.Creator<ScanResult> CREATOR = new Parcelable.Creator<ScanResult>() { // from class: android.net.wifi.ScanResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ScanResult createFromParcel(Parcel in) {
            WifiSsid wifiSsid = null;
            if (in.readInt() == 1) {
                WifiSsid wifiSsid2 = WifiSsid.CREATOR.createFromParcel(in);
                wifiSsid = wifiSsid2;
            }
            ScanResult sr = new ScanResult(wifiSsid, in.readString(), in.readString(), in.readLong(), in.readInt(), in.readString(), in.readInt(), in.readInt(), in.readLong(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), false);
            sr.seen = in.readLong();
            int i = 0;
            sr.untrusted = in.readInt() != 0;
            sr.numUsage = in.readInt();
            sr.venueName = in.readString();
            sr.operatorFriendlyName = in.readString();
            sr.flags = in.readLong();
            int n = in.readInt();
            if (n != 0) {
                sr.informationElements = new InformationElement[n];
                for (int i2 = 0; i2 < n; i2++) {
                    sr.informationElements[i2] = new InformationElement();
                    sr.informationElements[i2].id = in.readInt();
                    int len = in.readInt();
                    sr.informationElements[i2].bytes = new byte[len];
                    in.readByteArray(sr.informationElements[i2].bytes);
                }
            }
            int n2 = in.readInt();
            if (n2 != 0) {
                sr.anqpLines = new ArrayList();
                for (int i3 = 0; i3 < n2; i3++) {
                    sr.anqpLines.add(in.readString());
                }
            }
            int n3 = in.readInt();
            if (n3 != 0) {
                sr.anqpElements = new AnqpInformationElement[n3];
                for (int i4 = 0; i4 < n3; i4++) {
                    int vendorId = in.readInt();
                    int elementId = in.readInt();
                    int len2 = in.readInt();
                    byte[] payload = new byte[len2];
                    in.readByteArray(payload);
                    sr.anqpElements[i4] = new AnqpInformationElement(vendorId, elementId, payload);
                }
            }
            int i5 = in.readInt();
            sr.isCarrierAp = i5 != 0;
            sr.carrierApEapType = in.readInt();
            sr.carrierName = in.readString();
            int n4 = in.readInt();
            if (n4 != 0) {
                sr.radioChainInfos = new RadioChainInfo[n4];
                while (true) {
                    int i6 = i;
                    if (i6 >= n4) {
                        break;
                    }
                    sr.radioChainInfos[i6] = new RadioChainInfo();
                    sr.radioChainInfos[i6].id = in.readInt();
                    sr.radioChainInfos[i6].level = in.readInt();
                    i = i6 + 1;
                }
            }
            return sr;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ScanResult[] newArray(int size) {
            return new ScanResult[size];
        }
    };
    public static final long FLAG_80211mc_RESPONDER = 2;
    public static final long FLAG_PASSPOINT_NETWORK = 1;
    public static final int KEY_MGMT_DPP = 10;
    public static final int KEY_MGMT_EAP = 2;
    public static final int KEY_MGMT_EAP_SHA256 = 6;
    public static final int KEY_MGMT_EAP_SUITE_B_192 = 13;
    public static final int KEY_MGMT_FT_EAP = 4;
    public static final int KEY_MGMT_FT_PSK = 3;
    public static final int KEY_MGMT_NONE = 0;
    public static final int KEY_MGMT_OSEN = 7;
    public static final int KEY_MGMT_OWE = 12;
    public static final int KEY_MGMT_PSK = 1;
    public static final int KEY_MGMT_PSK_SHA256 = 5;
    public static final int KEY_MGMT_SAE = 11;
    public static final int PROTOCOL_NONE = 0;
    public static final int PROTOCOL_OSEN = 3;
    public static final int PROTOCOL_WPA = 1;
    public static final int PROTOCOL_WPA2 = 2;
    public static final int UNSPECIFIED = -1;
    public String BSSID;
    public String SSID;
    private protected int anqpDomainId;
    public AnqpInformationElement[] anqpElements;
    private protected List<String> anqpLines;
    public String capabilities;
    public int carrierApEapType;
    public String carrierName;
    public int centerFreq0;
    public int centerFreq1;
    public int channelWidth;
    private protected int distanceCm;
    private protected int distanceSdCm;
    private protected long flags;
    public int frequency;
    private protected long hessid;
    private protected InformationElement[] informationElements;
    private protected boolean is80211McRTTResponder;
    public boolean isCarrierAp;
    public int level;
    private protected int numUsage;
    public CharSequence operatorFriendlyName;
    public RadioChainInfo[] radioChainInfos;
    private protected long seen;
    public long timestamp;
    @SystemApi
    private protected boolean untrusted;
    public CharSequence venueName;
    private protected WifiSsid wifiSsid;

    /* loaded from: classes2.dex */
    public static class RadioChainInfo {
        public int id;
        public int level;

        public String toString() {
            return "RadioChainInfo: id=" + this.id + ", level=" + this.level;
        }

        public boolean equals(Object otherObj) {
            if (this == otherObj) {
                return true;
            }
            if (otherObj instanceof RadioChainInfo) {
                RadioChainInfo other = (RadioChainInfo) otherObj;
                return this.id == other.id && this.level == other.level;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.id), Integer.valueOf(this.level));
        }
    }

    public synchronized void setFlag(long flag) {
        this.flags |= flag;
    }

    public synchronized void clearFlag(long flag) {
        this.flags &= ~flag;
    }

    public boolean is80211mcResponder() {
        return (this.flags & 2) != 0;
    }

    public boolean isPasspointNetwork() {
        return (this.flags & 1) != 0;
    }

    public synchronized boolean is24GHz() {
        return is24GHz(this.frequency);
    }

    public static synchronized boolean is24GHz(int freq) {
        return freq > 2400 && freq < 2500;
    }

    public synchronized boolean is5GHz() {
        return is5GHz(this.frequency);
    }

    public static synchronized boolean is5GHz(int freq) {
        return freq > 4900 && freq < 5900;
    }

    /* loaded from: classes2.dex */
    public static class InformationElement {
        private protected static final int EID_BSS_LOAD = 11;
        private protected static final int EID_ERP = 42;
        private protected static final int EID_EXTENDED_CAPS = 127;
        private protected static final int EID_EXTENDED_SUPPORTED_RATES = 50;
        public static final int EID_HT_CAPABILITIES = 45;
        private protected static final int EID_HT_OPERATION = 61;
        private protected static final int EID_INTERWORKING = 107;
        private protected static final int EID_ROAMING_CONSORTIUM = 111;
        private protected static final int EID_RSN = 48;
        private protected static final int EID_SSID = 0;
        private protected static final int EID_SUPPORTED_RATES = 1;
        private protected static final int EID_TIM = 5;
        public static final int EID_VHT_CAPABILITIES = 191;
        private protected static final int EID_VHT_OPERATION = 192;
        private protected static final int EID_VSA = 221;
        private protected byte[] bytes;
        private protected int id;

        public synchronized InformationElement() {
        }

        public synchronized InformationElement(InformationElement rhs) {
            this.id = rhs.id;
            this.bytes = (byte[]) rhs.bytes.clone();
        }
    }

    public synchronized ScanResult(WifiSsid wifiSsid, String BSSID, long hessid, int anqpDomainId, byte[] osuProviders, String caps, int level, int frequency, long tsf) {
        this.wifiSsid = wifiSsid;
        this.SSID = wifiSsid != null ? wifiSsid.toString() : "<unknown ssid>";
        this.BSSID = BSSID;
        this.hessid = hessid;
        this.anqpDomainId = anqpDomainId;
        if (osuProviders != null) {
            this.anqpElements = new AnqpInformationElement[1];
            this.anqpElements[0] = new AnqpInformationElement(AnqpInformationElement.HOTSPOT20_VENDOR_ID, 8, osuProviders);
        }
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = -1;
        this.distanceSdCm = -1;
        this.channelWidth = -1;
        this.centerFreq0 = -1;
        this.centerFreq1 = -1;
        this.flags = 0L;
        this.isCarrierAp = false;
        this.carrierApEapType = -1;
        this.carrierName = null;
        this.radioChainInfos = null;
    }

    public synchronized ScanResult(WifiSsid wifiSsid, String BSSID, String caps, int level, int frequency, long tsf, int distCm, int distSdCm) {
        this.wifiSsid = wifiSsid;
        this.SSID = wifiSsid != null ? wifiSsid.toString() : "<unknown ssid>";
        this.BSSID = BSSID;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = distCm;
        this.distanceSdCm = distSdCm;
        this.channelWidth = -1;
        this.centerFreq0 = -1;
        this.centerFreq1 = -1;
        this.flags = 0L;
        this.isCarrierAp = false;
        this.carrierApEapType = -1;
        this.carrierName = null;
        this.radioChainInfos = null;
    }

    public synchronized ScanResult(String Ssid, String BSSID, long hessid, int anqpDomainId, String caps, int level, int frequency, long tsf, int distCm, int distSdCm, int channelWidth, int centerFreq0, int centerFreq1, boolean is80211McRTTResponder) {
        this.SSID = Ssid;
        this.BSSID = BSSID;
        this.hessid = hessid;
        this.anqpDomainId = anqpDomainId;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = distCm;
        this.distanceSdCm = distSdCm;
        this.channelWidth = channelWidth;
        this.centerFreq0 = centerFreq0;
        this.centerFreq1 = centerFreq1;
        if (is80211McRTTResponder) {
            this.flags = 2L;
        } else {
            this.flags = 0L;
        }
        this.isCarrierAp = false;
        this.carrierApEapType = -1;
        this.carrierName = null;
        this.radioChainInfos = null;
    }

    public synchronized ScanResult(WifiSsid wifiSsid, String Ssid, String BSSID, long hessid, int anqpDomainId, String caps, int level, int frequency, long tsf, int distCm, int distSdCm, int channelWidth, int centerFreq0, int centerFreq1, boolean is80211McRTTResponder) {
        this(Ssid, BSSID, hessid, anqpDomainId, caps, level, frequency, tsf, distCm, distSdCm, channelWidth, centerFreq0, centerFreq1, is80211McRTTResponder);
        this.wifiSsid = wifiSsid;
    }

    public synchronized ScanResult(ScanResult source) {
        if (source != null) {
            this.wifiSsid = source.wifiSsid;
            this.SSID = source.SSID;
            this.BSSID = source.BSSID;
            this.hessid = source.hessid;
            this.anqpDomainId = source.anqpDomainId;
            this.informationElements = source.informationElements;
            this.anqpElements = source.anqpElements;
            this.capabilities = source.capabilities;
            this.level = source.level;
            this.frequency = source.frequency;
            this.channelWidth = source.channelWidth;
            this.centerFreq0 = source.centerFreq0;
            this.centerFreq1 = source.centerFreq1;
            this.timestamp = source.timestamp;
            this.distanceCm = source.distanceCm;
            this.distanceSdCm = source.distanceSdCm;
            this.seen = source.seen;
            this.untrusted = source.untrusted;
            this.numUsage = source.numUsage;
            this.venueName = source.venueName;
            this.operatorFriendlyName = source.operatorFriendlyName;
            this.flags = source.flags;
            this.isCarrierAp = source.isCarrierAp;
            this.carrierApEapType = source.carrierApEapType;
            this.carrierName = source.carrierName;
            this.radioChainInfos = source.radioChainInfos;
        }
    }

    public synchronized ScanResult() {
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SSID: ");
        sb.append(this.wifiSsid == null ? "<unknown ssid>" : this.wifiSsid);
        sb.append(", BSSID: ");
        sb.append(this.BSSID == null ? "<none>" : this.BSSID);
        sb.append(", capabilities: ");
        sb.append(this.capabilities == null ? "<none>" : this.capabilities);
        sb.append(", level: ");
        sb.append(this.level);
        sb.append(", frequency: ");
        sb.append(this.frequency);
        sb.append(", timestamp: ");
        sb.append(this.timestamp);
        sb.append(", distance: ");
        sb.append(this.distanceCm != -1 ? Integer.valueOf(this.distanceCm) : "?");
        sb.append("(cm)");
        sb.append(", distanceSd: ");
        sb.append(this.distanceSdCm != -1 ? Integer.valueOf(this.distanceSdCm) : "?");
        sb.append("(cm)");
        sb.append(", passpoint: ");
        sb.append((this.flags & 1) != 0 ? "yes" : "no");
        sb.append(", ChannelBandwidth: ");
        sb.append(this.channelWidth);
        sb.append(", centerFreq0: ");
        sb.append(this.centerFreq0);
        sb.append(", centerFreq1: ");
        sb.append(this.centerFreq1);
        sb.append(", 80211mcResponder: ");
        sb.append((this.flags & 2) != 0 ? "is supported" : "is not supported");
        sb.append(", Carrier AP: ");
        sb.append(this.isCarrierAp ? "yes" : "no");
        sb.append(", Carrier AP EAP Type: ");
        sb.append(this.carrierApEapType);
        sb.append(", Carrier name: ");
        sb.append(this.carrierName);
        sb.append(", Radio Chain Infos: ");
        sb.append(Arrays.toString(this.radioChainInfos));
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        AnqpInformationElement[] anqpInformationElementArr;
        int i = 0;
        if (this.wifiSsid != null) {
            dest.writeInt(1);
            this.wifiSsid.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        dest.writeString(this.SSID);
        dest.writeString(this.BSSID);
        dest.writeLong(this.hessid);
        dest.writeInt(this.anqpDomainId);
        dest.writeString(this.capabilities);
        dest.writeInt(this.level);
        dest.writeInt(this.frequency);
        dest.writeLong(this.timestamp);
        dest.writeInt(this.distanceCm);
        dest.writeInt(this.distanceSdCm);
        dest.writeInt(this.channelWidth);
        dest.writeInt(this.centerFreq0);
        dest.writeInt(this.centerFreq1);
        dest.writeLong(this.seen);
        dest.writeInt(this.untrusted ? 1 : 0);
        dest.writeInt(this.numUsage);
        dest.writeString(this.venueName != null ? this.venueName.toString() : "");
        dest.writeString(this.operatorFriendlyName != null ? this.operatorFriendlyName.toString() : "");
        dest.writeLong(this.flags);
        if (this.informationElements != null) {
            dest.writeInt(this.informationElements.length);
            for (int i2 = 0; i2 < this.informationElements.length; i2++) {
                dest.writeInt(this.informationElements[i2].id);
                dest.writeInt(this.informationElements[i2].bytes.length);
                dest.writeByteArray(this.informationElements[i2].bytes);
            }
        } else {
            dest.writeInt(0);
        }
        if (this.anqpLines != null) {
            dest.writeInt(this.anqpLines.size());
            for (int i3 = 0; i3 < this.anqpLines.size(); i3++) {
                dest.writeString(this.anqpLines.get(i3));
            }
        } else {
            dest.writeInt(0);
        }
        if (this.anqpElements != null) {
            dest.writeInt(this.anqpElements.length);
            for (AnqpInformationElement element : this.anqpElements) {
                dest.writeInt(element.getVendorId());
                dest.writeInt(element.getElementId());
                dest.writeInt(element.getPayload().length);
                dest.writeByteArray(element.getPayload());
            }
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.isCarrierAp ? 1 : 0);
        dest.writeInt(this.carrierApEapType);
        dest.writeString(this.carrierName);
        if (this.radioChainInfos != null) {
            dest.writeInt(this.radioChainInfos.length);
            while (true) {
                int i4 = i;
                if (i4 < this.radioChainInfos.length) {
                    dest.writeInt(this.radioChainInfos[i4].id);
                    dest.writeInt(this.radioChainInfos[i4].level);
                    i = i4 + 1;
                } else {
                    return;
                }
            }
        } else {
            dest.writeInt(0);
        }
    }
}
