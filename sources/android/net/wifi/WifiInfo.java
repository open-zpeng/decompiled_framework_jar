package android.net.wifi;

import android.net.NetworkInfo;
import android.net.NetworkUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.xiaopeng.util.FeatureOption;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.EnumMap;
import java.util.Locale;
/* loaded from: classes2.dex */
public class WifiInfo implements Parcelable {
    private protected static final Parcelable.Creator<WifiInfo> CREATOR;
    private protected static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";
    public static final String FREQUENCY_UNITS = "MHz";
    private protected static final int INVALID_RSSI = -127;
    public static final String LINK_SPEED_UNITS = "Mbps";
    public static final int MAX_RSSI = 200;
    public static final int MIN_RSSI = -126;
    private static final String TAG = "WifiInfo";
    private static final EnumMap<SupplicantState, NetworkInfo.DetailedState> stateMap = new EnumMap<>(SupplicantState.class);
    public protected String mBSSID;
    private boolean mEphemeral;
    private int mFrequency;
    public protected InetAddress mIpAddress;
    private int mLinkSpeed;
    public protected String mMacAddress;
    private boolean mMeteredHint;
    private int mNetworkId;
    private int mRssi;
    private SupplicantState mSupplicantState;
    public protected WifiSsid mWifiSsid;
    public long rxSuccess;
    public double rxSuccessRate;
    private protected int score;
    public long txBad;
    public double txBadRate;
    public long txRetries;
    public double txRetriesRate;
    public long txSuccess;
    public double txSuccessRate;

    static {
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.DISCONNECTED, (SupplicantState) NetworkInfo.DetailedState.DISCONNECTED);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.INTERFACE_DISABLED, (SupplicantState) NetworkInfo.DetailedState.DISCONNECTED);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.INACTIVE, (SupplicantState) NetworkInfo.DetailedState.IDLE);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.SCANNING, (SupplicantState) NetworkInfo.DetailedState.SCANNING);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.AUTHENTICATING, (SupplicantState) NetworkInfo.DetailedState.CONNECTING);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.ASSOCIATING, (SupplicantState) NetworkInfo.DetailedState.CONNECTING);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.ASSOCIATED, (SupplicantState) NetworkInfo.DetailedState.CONNECTING);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.FOUR_WAY_HANDSHAKE, (SupplicantState) NetworkInfo.DetailedState.AUTHENTICATING);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.GROUP_HANDSHAKE, (SupplicantState) NetworkInfo.DetailedState.AUTHENTICATING);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.COMPLETED, (SupplicantState) NetworkInfo.DetailedState.OBTAINING_IPADDR);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.DORMANT, (SupplicantState) NetworkInfo.DetailedState.DISCONNECTED);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.UNINITIALIZED, (SupplicantState) NetworkInfo.DetailedState.IDLE);
        stateMap.put((EnumMap<SupplicantState, NetworkInfo.DetailedState>) SupplicantState.INVALID, (SupplicantState) NetworkInfo.DetailedState.FAILED);
        CREATOR = new Parcelable.Creator<WifiInfo>() { // from class: android.net.wifi.WifiInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public WifiInfo createFromParcel(Parcel in) {
                WifiInfo info = new WifiInfo();
                info.setNetworkId(in.readInt());
                info.setRssi(in.readInt());
                info.setLinkSpeed(in.readInt());
                info.setFrequency(in.readInt());
                if (in.readByte() == 1) {
                    try {
                        info.setInetAddress(InetAddress.getByAddress(in.createByteArray()));
                    } catch (UnknownHostException e) {
                    }
                }
                if (in.readInt() == 1) {
                    info.mWifiSsid = WifiSsid.CREATOR.createFromParcel(in);
                }
                info.mBSSID = in.readString();
                info.mMacAddress = in.readString();
                info.mMeteredHint = in.readInt() != 0;
                info.mEphemeral = in.readInt() != 0;
                info.score = in.readInt();
                info.txSuccessRate = in.readDouble();
                info.txRetriesRate = in.readDouble();
                info.txBadRate = in.readDouble();
                info.rxSuccessRate = in.readDouble();
                info.mSupplicantState = SupplicantState.CREATOR.createFromParcel(in);
                return info;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public WifiInfo[] newArray(int size) {
                return new WifiInfo[size];
            }
        };
    }

    private protected WifiInfo() {
        this.mMacAddress = "02:00:00:00:00:00";
        this.mWifiSsid = null;
        this.mBSSID = null;
        this.mNetworkId = -1;
        this.mSupplicantState = SupplicantState.UNINITIALIZED;
        this.mRssi = -127;
        this.mLinkSpeed = -1;
        this.mFrequency = -1;
    }

    public synchronized void reset() {
        setInetAddress(null);
        setBSSID(null);
        setSSID(null);
        setNetworkId(-1);
        setRssi(-127);
        setLinkSpeed(-1);
        setFrequency(-1);
        setMeteredHint(false);
        setEphemeral(false);
        this.txBad = 0L;
        this.txSuccess = 0L;
        this.rxSuccess = 0L;
        this.txRetries = 0L;
        this.txBadRate = FeatureOption.FO_BOOT_POLICY_CPU;
        this.txSuccessRate = FeatureOption.FO_BOOT_POLICY_CPU;
        this.rxSuccessRate = FeatureOption.FO_BOOT_POLICY_CPU;
        this.txRetriesRate = FeatureOption.FO_BOOT_POLICY_CPU;
        this.score = 0;
    }

    public synchronized WifiInfo(WifiInfo source) {
        this.mMacAddress = "02:00:00:00:00:00";
        if (source != null) {
            this.mSupplicantState = source.mSupplicantState;
            this.mBSSID = source.mBSSID;
            this.mWifiSsid = source.mWifiSsid;
            this.mNetworkId = source.mNetworkId;
            this.mRssi = source.mRssi;
            this.mLinkSpeed = source.mLinkSpeed;
            this.mFrequency = source.mFrequency;
            this.mIpAddress = source.mIpAddress;
            this.mMacAddress = source.mMacAddress;
            this.mMeteredHint = source.mMeteredHint;
            this.mEphemeral = source.mEphemeral;
            this.txBad = source.txBad;
            this.txRetries = source.txRetries;
            this.txSuccess = source.txSuccess;
            this.rxSuccess = source.rxSuccess;
            this.txBadRate = source.txBadRate;
            this.txRetriesRate = source.txRetriesRate;
            this.txSuccessRate = source.txSuccessRate;
            this.rxSuccessRate = source.rxSuccessRate;
            this.score = source.score;
        }
    }

    public synchronized void setSSID(WifiSsid wifiSsid) {
        this.mWifiSsid = wifiSsid;
    }

    public String getSSID() {
        if (this.mWifiSsid != null) {
            String unicode = this.mWifiSsid.toString();
            if (!TextUtils.isEmpty(unicode)) {
                return "\"" + unicode + "\"";
            }
            String hex = this.mWifiSsid.getHexString();
            return hex != null ? hex : "<unknown ssid>";
        }
        return "<unknown ssid>";
    }

    private protected WifiSsid getWifiSsid() {
        return this.mWifiSsid;
    }

    private protected void setBSSID(String BSSID) {
        this.mBSSID = BSSID;
    }

    public String getBSSID() {
        return this.mBSSID;
    }

    public int getRssi() {
        return this.mRssi;
    }

    private protected void setRssi(int rssi) {
        if (rssi < -127) {
            rssi = -127;
        }
        if (rssi > 200) {
            rssi = 200;
        }
        this.mRssi = rssi;
    }

    public int getLinkSpeed() {
        return this.mLinkSpeed;
    }

    private protected void setLinkSpeed(int linkSpeed) {
        this.mLinkSpeed = linkSpeed;
    }

    public int getFrequency() {
        return this.mFrequency;
    }

    public synchronized void setFrequency(int frequency) {
        this.mFrequency = frequency;
    }

    public synchronized boolean is24GHz() {
        return ScanResult.is24GHz(this.mFrequency);
    }

    private protected boolean is5GHz() {
        return ScanResult.is5GHz(this.mFrequency);
    }

    private protected void setMacAddress(String macAddress) {
        this.mMacAddress = macAddress;
    }

    public String getMacAddress() {
        return this.mMacAddress;
    }

    public synchronized boolean hasRealMacAddress() {
        return (this.mMacAddress == null || "02:00:00:00:00:00".equals(this.mMacAddress)) ? false : true;
    }

    public synchronized void setMeteredHint(boolean meteredHint) {
        this.mMeteredHint = meteredHint;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getMeteredHint() {
        return this.mMeteredHint;
    }

    public synchronized void setEphemeral(boolean ephemeral) {
        this.mEphemeral = ephemeral;
    }

    private protected boolean isEphemeral() {
        return this.mEphemeral;
    }

    private protected void setNetworkId(int id) {
        this.mNetworkId = id;
    }

    public int getNetworkId() {
        return this.mNetworkId;
    }

    public SupplicantState getSupplicantState() {
        return this.mSupplicantState;
    }

    private protected void setSupplicantState(SupplicantState state) {
        this.mSupplicantState = state;
    }

    public synchronized void setInetAddress(InetAddress address) {
        this.mIpAddress = address;
    }

    public int getIpAddress() {
        if (!(this.mIpAddress instanceof Inet4Address)) {
            return 0;
        }
        int result = NetworkUtils.inetAddressToInt((Inet4Address) this.mIpAddress);
        return result;
    }

    public boolean getHiddenSSID() {
        if (this.mWifiSsid == null) {
            return false;
        }
        return this.mWifiSsid.isHidden();
    }

    public static NetworkInfo.DetailedState getDetailedStateOf(SupplicantState suppState) {
        return stateMap.get(suppState);
    }

    public private protected void setSupplicantState(String stateName) {
        this.mSupplicantState = valueOf(stateName);
    }

    static synchronized SupplicantState valueOf(String stateName) {
        if ("4WAY_HANDSHAKE".equalsIgnoreCase(stateName)) {
            return SupplicantState.FOUR_WAY_HANDSHAKE;
        }
        try {
            return SupplicantState.valueOf(stateName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return SupplicantState.INVALID;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String removeDoubleQuotes(String string) {
        if (string == null) {
            return null;
        }
        int length = string.length();
        if (length > 1 && string.charAt(0) == '\"' && string.charAt(length - 1) == '\"') {
            return string.substring(1, length - 1);
        }
        return string;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SSID: ");
        sb.append(this.mWifiSsid == null ? "<unknown ssid>" : this.mWifiSsid);
        sb.append(", BSSID: ");
        sb.append(this.mBSSID == null ? "<none>" : this.mBSSID);
        sb.append(", MAC: ");
        sb.append(this.mMacAddress == null ? "<none>" : this.mMacAddress);
        sb.append(", Supplicant state: ");
        sb.append(this.mSupplicantState == null ? "<none>" : this.mSupplicantState);
        sb.append(", RSSI: ");
        sb.append(this.mRssi);
        sb.append(", Link speed: ");
        sb.append(this.mLinkSpeed);
        sb.append(LINK_SPEED_UNITS);
        sb.append(", Frequency: ");
        sb.append(this.mFrequency);
        sb.append(FREQUENCY_UNITS);
        sb.append(", Net ID: ");
        sb.append(this.mNetworkId);
        sb.append(", Metered hint: ");
        sb.append(this.mMeteredHint);
        sb.append(", score: ");
        sb.append(Integer.toString(this.score));
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mNetworkId);
        dest.writeInt(this.mRssi);
        dest.writeInt(this.mLinkSpeed);
        dest.writeInt(this.mFrequency);
        if (this.mIpAddress != null) {
            dest.writeByte((byte) 1);
            dest.writeByteArray(this.mIpAddress.getAddress());
        } else {
            dest.writeByte((byte) 0);
        }
        if (this.mWifiSsid != null) {
            dest.writeInt(1);
            this.mWifiSsid.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        dest.writeString(this.mBSSID);
        dest.writeString(this.mMacAddress);
        dest.writeInt(this.mMeteredHint ? 1 : 0);
        dest.writeInt(this.mEphemeral ? 1 : 0);
        dest.writeInt(this.score);
        dest.writeDouble(this.txSuccessRate);
        dest.writeDouble(this.txRetriesRate);
        dest.writeDouble(this.txBadRate);
        dest.writeDouble(this.rxSuccessRate);
        this.mSupplicantState.writeToParcel(dest, flags);
    }
}
