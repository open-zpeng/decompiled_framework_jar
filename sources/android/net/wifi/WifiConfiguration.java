package android.net.wifi;

import android.annotation.SystemApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertisingSetParameters;
import android.net.IpConfiguration;
import android.net.MacAddress;
import android.net.ProxyInfo;
import android.net.StaticIpConfiguration;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.BackupUtils;
import android.util.Log;
import android.util.TimeUtils;
import com.android.internal.content.NativeLibraryHelper;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
/* loaded from: classes2.dex */
public class WifiConfiguration implements Parcelable {
    public static final int AP_BAND_2GHZ = 0;
    public static final int AP_BAND_5GHZ = 1;
    public static final int AP_BAND_ANY = -1;
    private static final int BACKUP_VERSION = 2;
    public static final int HOME_NETWORK_RSSI_BOOST = 5;
    public static final int INVALID_NETWORK_ID = -1;
    public static final int LOCAL_ONLY_NETWORK_ID = -2;
    private static final int MAXIMUM_RANDOM_MAC_GENERATION_RETRY = 3;
    public static final int METERED_OVERRIDE_METERED = 1;
    public static final int METERED_OVERRIDE_NONE = 0;
    public static final int METERED_OVERRIDE_NOT_METERED = 2;
    private static final String TAG = "WifiConfiguration";
    public static final int UNKNOWN_UID = -1;
    public static final int USER_APPROVED = 1;
    public static final int USER_BANNED = 2;
    public static final int USER_PENDING = 3;
    public static final int USER_UNSPECIFIED = 0;
    public static final String bssidVarName = "bssid";
    public static final String hiddenSSIDVarName = "scan_ssid";
    public static final String pmfVarName = "ieee80211w";
    public static final String priorityVarName = "priority";
    public static final String pskVarName = "psk";
    public static final String ssidVarName = "ssid";
    public static final String updateIdentiferVarName = "update_identifier";
    @Deprecated
    public static final String wepTxKeyIdxVarName = "wep_tx_keyidx";
    public String BSSID;
    public String FQDN;
    public String SSID;
    public BitSet allowedAuthAlgorithms;
    public BitSet allowedGroupCiphers;
    public BitSet allowedGroupMgmtCiphers;
    public BitSet allowedKeyManagement;
    public BitSet allowedPairwiseCiphers;
    public BitSet allowedProtocols;
    public BitSet allowedSuiteBCiphers;
    private protected int apBand;
    private protected int apChannel;
    public String creationTime;
    @SystemApi
    public String creatorName;
    @SystemApi
    public int creatorUid;
    private protected String defaultGwMacAddress;
    public String dhcpServer;
    public boolean didSelfAdd;
    public String dppConnector;
    public String dppCsign;
    public String dppNetAccessKey;
    public int dppNetAccessKeyExpiry;
    public int dtimInterval;
    public WifiEnterpriseConfig enterpriseConfig;
    public boolean ephemeral;
    public boolean hiddenSSID;
    public boolean isHomeProviderNetwork;
    public boolean isLegacyPasspointConfig;
    private protected int lastConnectUid;
    public long lastConnected;
    public long lastDisconnected;
    @SystemApi
    public String lastUpdateName;
    @SystemApi
    public int lastUpdateUid;
    public HashMap<String, Integer> linkedConfigurations;
    String mCachedConfigKey;
    public protected IpConfiguration mIpConfiguration;
    private NetworkSelectionStatus mNetworkSelectionStatus;
    private String mPasspointManagementObjectTree;
    private MacAddress mRandomizedMacAddress;
    @SystemApi
    public boolean meteredHint;
    public int meteredOverride;
    public int networkId;
    private protected boolean noInternetAccessExpected;
    @SystemApi
    public int numAssociation;
    private protected int numNoInternetAccessReports;
    @SystemApi
    public int numScorerOverride;
    @SystemApi
    public int numScorerOverrideAndSwitchedNetwork;
    public String peerWifiConfiguration;
    public String preSharedKey;
    @Deprecated
    public int priority;
    public String providerFriendlyName;
    public final RecentFailure recentFailure;
    public boolean requirePMF;
    public long[] roamingConsortiumIds;
    private protected boolean selfAdded;
    private protected boolean shared;
    public int status;
    public String updateIdentifier;
    public String updateTime;
    @SystemApi
    public boolean useExternalScores;
    public int userApproved;
    private protected boolean validatedInternetAccess;
    @Deprecated
    public String[] wepKeys;
    @Deprecated
    public int wepTxKeyIndex;
    @Deprecated
    private protected static final String[] wepKeyVarNames = {"wep_key0", "wep_key1", "wep_key2", "wep_key3"};
    private protected static int INVALID_RSSI = AdvertisingSetParameters.TX_POWER_MIN;
    private protected static final Parcelable.Creator<WifiConfiguration> CREATOR = new Parcelable.Creator<WifiConfiguration>() { // from class: android.net.wifi.WifiConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiConfiguration createFromParcel(Parcel in) {
            WifiConfiguration config = new WifiConfiguration();
            config.networkId = in.readInt();
            config.status = in.readInt();
            config.mNetworkSelectionStatus.readFromParcel(in);
            config.SSID = in.readString();
            config.BSSID = in.readString();
            config.apBand = in.readInt();
            config.apChannel = in.readInt();
            config.FQDN = in.readString();
            config.providerFriendlyName = in.readString();
            config.isHomeProviderNetwork = in.readInt() != 0;
            int numRoamingConsortiumIds = in.readInt();
            config.roamingConsortiumIds = new long[numRoamingConsortiumIds];
            for (int i = 0; i < numRoamingConsortiumIds; i++) {
                config.roamingConsortiumIds[i] = in.readLong();
            }
            config.preSharedKey = in.readString();
            for (int i2 = 0; i2 < config.wepKeys.length; i2++) {
                config.wepKeys[i2] = in.readString();
            }
            int i3 = in.readInt();
            config.wepTxKeyIndex = i3;
            config.priority = in.readInt();
            config.hiddenSSID = in.readInt() != 0;
            config.requirePMF = in.readInt() != 0;
            config.updateIdentifier = in.readString();
            config.allowedKeyManagement = WifiConfiguration.readBitSet(in);
            config.allowedProtocols = WifiConfiguration.readBitSet(in);
            config.allowedAuthAlgorithms = WifiConfiguration.readBitSet(in);
            config.allowedPairwiseCiphers = WifiConfiguration.readBitSet(in);
            config.allowedGroupCiphers = WifiConfiguration.readBitSet(in);
            config.allowedGroupMgmtCiphers = WifiConfiguration.readBitSet(in);
            config.allowedSuiteBCiphers = WifiConfiguration.readBitSet(in);
            config.enterpriseConfig = (WifiEnterpriseConfig) in.readParcelable(null);
            config.setIpConfiguration((IpConfiguration) in.readParcelable(null));
            config.dhcpServer = in.readString();
            config.defaultGwMacAddress = in.readString();
            config.selfAdded = in.readInt() != 0;
            config.didSelfAdd = in.readInt() != 0;
            config.validatedInternetAccess = in.readInt() != 0;
            config.isLegacyPasspointConfig = in.readInt() != 0;
            config.ephemeral = in.readInt() != 0;
            config.meteredHint = in.readInt() != 0;
            config.meteredOverride = in.readInt();
            config.useExternalScores = in.readInt() != 0;
            config.creatorUid = in.readInt();
            config.lastConnectUid = in.readInt();
            config.lastUpdateUid = in.readInt();
            config.creatorName = in.readString();
            config.lastUpdateName = in.readString();
            config.numScorerOverride = in.readInt();
            config.numScorerOverrideAndSwitchedNetwork = in.readInt();
            config.numAssociation = in.readInt();
            config.userApproved = in.readInt();
            config.numNoInternetAccessReports = in.readInt();
            config.noInternetAccessExpected = in.readInt() != 0;
            config.shared = in.readInt() != 0;
            config.mPasspointManagementObjectTree = in.readString();
            config.recentFailure.setAssociationStatus(in.readInt());
            config.mRandomizedMacAddress = (MacAddress) in.readParcelable(null);
            config.dppConnector = in.readString();
            config.dppNetAccessKey = in.readString();
            config.dppNetAccessKeyExpiry = in.readInt();
            config.dppCsign = in.readString();
            return config;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiConfiguration[] newArray(int size) {
            return new WifiConfiguration[size];
        }
    };

    /* loaded from: classes2.dex */
    public static class KeyMgmt {
        public static final int DPP = 10;
        public static final int FILS_SHA256 = 8;
        public static final int FILS_SHA384 = 9;
        public static final int FT_EAP = 7;
        public static final int FT_PSK = 6;
        public static final int IEEE8021X = 3;
        public static final int NONE = 0;
        public static final int OSEN = 5;
        public static final int OWE = 12;
        public static final int SAE = 11;
        public static final int SUITE_B_192 = 13;
        @SystemApi
        public static final int WPA2_PSK = 4;
        public static final int WPA_EAP = 2;
        public static final int WPA_PSK = 1;
        public static final String[] strings = {"NONE", "WPA_PSK", "WPA_EAP", "IEEE8021X", "WPA2_PSK", "OSEN", "FT_PSK", "FT_EAP", "FILS_SHA256", "FILS_SHA384", "DPP", "SAE", "OWE", "SUITE_B_192"};
        public static final String varName = "key_mgmt";

        private synchronized KeyMgmt() {
        }
    }

    /* loaded from: classes2.dex */
    public static class Protocol {
        public static final int OSEN = 2;
        public static final int RSN = 1;
        @Deprecated
        public static final int WPA = 0;
        public static final String[] strings = {"WPA", "RSN", "OSEN"};
        public static final String varName = "proto";

        private synchronized Protocol() {
        }
    }

    /* loaded from: classes2.dex */
    public static class AuthAlgorithm {
        public static final int LEAP = 2;
        public static final int OPEN = 0;
        @Deprecated
        public static final int SHARED = 1;
        public static final String[] strings = {"OPEN", "SHARED", "LEAP"};
        public static final String varName = "auth_alg";

        private synchronized AuthAlgorithm() {
        }
    }

    /* loaded from: classes2.dex */
    public static class PairwiseCipher {
        public static final int CCMP = 2;
        public static final int GCMP = 3;
        public static final int NONE = 0;
        @Deprecated
        public static final int TKIP = 1;
        public static final String[] strings = {"NONE", "TKIP", "CCMP", "GCMP"};
        public static final String varName = "pairwise";

        private synchronized PairwiseCipher() {
        }
    }

    /* loaded from: classes2.dex */
    public static class GroupCipher {
        public static final int CCMP = 3;
        public static final int GCMP = 5;
        public static final int GTK_NOT_USED = 4;
        public static final int TKIP = 2;
        @Deprecated
        public static final int WEP104 = 1;
        @Deprecated
        public static final int WEP40 = 0;
        public static final String[] strings = {"WEP40", "WEP104", "TKIP", "CCMP", "GTK_NOT_USED", "GCMP"};
        public static final String varName = "group";

        private synchronized GroupCipher() {
        }
    }

    /* loaded from: classes2.dex */
    public static class GroupMgmtCipher {
        public static final int CMAC = 0;
        public static final int GMAC = 1;
        public static final String[] strings = {"CMAC", "GMAC"};
        public static final String varName = "groupMgmt";

        private GroupMgmtCipher() {
        }
    }

    /* loaded from: classes2.dex */
    public static class SuiteBCipher {
        public static final int ECDHE_ECDSA = 0;
        public static final int ECDHE_RSA = 1;
        public static final String[] strings = {"ECDHE_ECDSA", "ECDHE_RSA"};
        public static final String varName = "SuiteB";

        private SuiteBCipher() {
        }
    }

    /* loaded from: classes2.dex */
    public static class Status {
        public static final int CURRENT = 0;
        public static final int DISABLED = 1;
        public static final int ENABLED = 2;
        public static final String[] strings = {Telephony.Carriers.CURRENT, "disabled", "enabled"};

        private synchronized Status() {
        }
    }

    @SystemApi
    public boolean hasNoInternetAccess() {
        return this.numNoInternetAccessReports > 0 && !this.validatedInternetAccess;
    }

    @SystemApi
    public boolean isNoInternetAccessExpected() {
        return this.noInternetAccessExpected;
    }

    @SystemApi
    public boolean isEphemeral() {
        return this.ephemeral;
    }

    public static synchronized boolean isMetered(WifiConfiguration config, WifiInfo info) {
        boolean metered = false;
        if (info != null && info.getMeteredHint()) {
            metered = true;
        }
        if (config != null && config.meteredHint) {
            metered = true;
        }
        if (config != null && config.meteredOverride == 1) {
            metered = true;
        }
        if (config != null && config.meteredOverride == 2) {
            return false;
        }
        return metered;
    }

    public synchronized boolean isOpenNetwork() {
        int cardinality = this.allowedKeyManagement.cardinality();
        boolean hasNoKeyMgmt = cardinality == 0 || (cardinality == 1 && this.allowedKeyManagement.get(0));
        boolean hasNoWepKeys = true;
        if (this.wepKeys != null) {
            int i = 0;
            while (true) {
                if (i >= this.wepKeys.length) {
                    break;
                } else if (this.wepKeys[i] == null) {
                    i++;
                } else {
                    hasNoWepKeys = false;
                    break;
                }
            }
        }
        return hasNoKeyMgmt && hasNoWepKeys;
    }

    public static synchronized boolean isValidMacAddressForRandomization(MacAddress mac) {
        return (mac == null || mac.isMulticastAddress() || !mac.isLocallyAssigned() || MacAddress.fromString(BluetoothAdapter.DEFAULT_MAC_ADDRESS).equals(mac)) ? false : true;
    }

    public synchronized MacAddress getOrCreateRandomizedMacAddress() {
        for (int randomMacGenerationCount = 0; !isValidMacAddressForRandomization(this.mRandomizedMacAddress) && randomMacGenerationCount < 3; randomMacGenerationCount++) {
            this.mRandomizedMacAddress = MacAddress.createRandomUnicastAddress();
        }
        if (!isValidMacAddressForRandomization(this.mRandomizedMacAddress)) {
            this.mRandomizedMacAddress = MacAddress.fromString(BluetoothAdapter.DEFAULT_MAC_ADDRESS);
        }
        return this.mRandomizedMacAddress;
    }

    public synchronized MacAddress getRandomizedMacAddress() {
        return this.mRandomizedMacAddress;
    }

    public synchronized void setRandomizedMacAddress(MacAddress mac) {
        if (mac == null) {
            Log.e(TAG, "setRandomizedMacAddress received null MacAddress.");
        } else {
            this.mRandomizedMacAddress = mac;
        }
    }

    /* loaded from: classes2.dex */
    public static class NetworkSelectionStatus {
        private static final int CONNECT_CHOICE_EXISTS = 1;
        private static final int CONNECT_CHOICE_NOT_EXISTS = -1;
        public static final int DISABLED_ASSOCIATION_REJECTION = 2;
        public static final int DISABLED_AUTHENTICATION_FAILURE = 3;
        public static final int DISABLED_AUTHENTICATION_NO_CREDENTIALS = 9;
        public static final int DISABLED_BAD_LINK = 1;
        public static final int DISABLED_BY_WIFI_MANAGER = 11;
        public static final int DISABLED_BY_WRONG_PASSWORD = 13;
        public static final int DISABLED_DHCP_FAILURE = 4;
        public static final int DISABLED_DNS_FAILURE = 5;
        public static final int DISABLED_DUE_TO_USER_SWITCH = 12;
        public static final int DISABLED_NO_INTERNET_PERMANENT = 10;
        public static final int DISABLED_NO_INTERNET_TEMPORARY = 6;
        public static final int DISABLED_TLS_VERSION_MISMATCH = 8;
        public static final int DISABLED_WPS_START = 7;
        public static final long INVALID_NETWORK_SELECTION_DISABLE_TIMESTAMP = -1;
        public static final int NETWORK_SELECTION_DISABLED_MAX = 14;
        public static final int NETWORK_SELECTION_DISABLED_STARTING_INDEX = 1;
        public static final int NETWORK_SELECTION_ENABLE = 0;
        public static final int NETWORK_SELECTION_ENABLED = 0;
        public static final int NETWORK_SELECTION_PERMANENTLY_DISABLED = 2;
        public static final int NETWORK_SELECTION_STATUS_MAX = 3;
        public static final int NETWORK_SELECTION_TEMPORARY_DISABLED = 1;
        private ScanResult mCandidate;
        private int mCandidateScore;
        private String mConnectChoice;
        private String mNetworkSelectionBSSID;
        private int mNetworkSelectionDisableReason;
        private boolean mNotRecommended;
        private boolean mSeenInLastQualifiedNetworkSelection;
        private int mStatus;
        public static final String[] QUALITY_NETWORK_SELECTION_STATUS = {"NETWORK_SELECTION_ENABLED", "NETWORK_SELECTION_TEMPORARY_DISABLED", "NETWORK_SELECTION_PERMANENTLY_DISABLED"};
        public static final String[] QUALITY_NETWORK_SELECTION_DISABLE_REASON = {"NETWORK_SELECTION_ENABLE", "NETWORK_SELECTION_DISABLED_BAD_LINK", "NETWORK_SELECTION_DISABLED_ASSOCIATION_REJECTION ", "NETWORK_SELECTION_DISABLED_AUTHENTICATION_FAILURE", "NETWORK_SELECTION_DISABLED_DHCP_FAILURE", "NETWORK_SELECTION_DISABLED_DNS_FAILURE", "NETWORK_SELECTION_DISABLED_NO_INTERNET_TEMPORARY", "NETWORK_SELECTION_DISABLED_WPS_START", "NETWORK_SELECTION_DISABLED_TLS_VERSION", "NETWORK_SELECTION_DISABLED_AUTHENTICATION_NO_CREDENTIALS", "NETWORK_SELECTION_DISABLED_NO_INTERNET_PERMANENT", "NETWORK_SELECTION_DISABLED_BY_WIFI_MANAGER", "NETWORK_SELECTION_DISABLED_BY_USER_SWITCH", "NETWORK_SELECTION_DISABLED_BY_WRONG_PASSWORD"};
        private long mTemporarilyDisabledTimestamp = -1;
        private int[] mNetworkSeclectionDisableCounter = new int[14];
        private long mConnectChoiceTimestamp = -1;
        private boolean mHasEverConnected = false;

        public synchronized void setNotRecommended(boolean notRecommended) {
            this.mNotRecommended = notRecommended;
        }

        public synchronized boolean isNotRecommended() {
            return this.mNotRecommended;
        }

        public synchronized void setSeenInLastQualifiedNetworkSelection(boolean seen) {
            this.mSeenInLastQualifiedNetworkSelection = seen;
        }

        public synchronized boolean getSeenInLastQualifiedNetworkSelection() {
            return this.mSeenInLastQualifiedNetworkSelection;
        }

        public synchronized void setCandidate(ScanResult scanCandidate) {
            this.mCandidate = scanCandidate;
        }

        public synchronized ScanResult getCandidate() {
            return this.mCandidate;
        }

        public synchronized void setCandidateScore(int score) {
            this.mCandidateScore = score;
        }

        public synchronized int getCandidateScore() {
            return this.mCandidateScore;
        }

        public synchronized String getConnectChoice() {
            return this.mConnectChoice;
        }

        public synchronized void setConnectChoice(String newConnectChoice) {
            this.mConnectChoice = newConnectChoice;
        }

        public synchronized long getConnectChoiceTimestamp() {
            return this.mConnectChoiceTimestamp;
        }

        public synchronized void setConnectChoiceTimestamp(long timeStamp) {
            this.mConnectChoiceTimestamp = timeStamp;
        }

        public synchronized String getNetworkStatusString() {
            return QUALITY_NETWORK_SELECTION_STATUS[this.mStatus];
        }

        public synchronized void setHasEverConnected(boolean value) {
            this.mHasEverConnected = value;
        }

        public synchronized boolean getHasEverConnected() {
            return this.mHasEverConnected;
        }

        public static synchronized String getNetworkDisableReasonString(int reason) {
            if (reason >= 0 && reason < 14) {
                return QUALITY_NETWORK_SELECTION_DISABLE_REASON[reason];
            }
            return null;
        }

        public synchronized String getNetworkDisableReasonString() {
            return QUALITY_NETWORK_SELECTION_DISABLE_REASON[this.mNetworkSelectionDisableReason];
        }

        public synchronized int getNetworkSelectionStatus() {
            return this.mStatus;
        }

        public synchronized boolean isNetworkEnabled() {
            return this.mStatus == 0;
        }

        public synchronized boolean isNetworkTemporaryDisabled() {
            return this.mStatus == 1;
        }

        public synchronized boolean isNetworkPermanentlyDisabled() {
            return this.mStatus == 2;
        }

        public synchronized void setNetworkSelectionStatus(int status) {
            if (status >= 0 && status < 3) {
                this.mStatus = status;
            }
        }

        public synchronized int getNetworkSelectionDisableReason() {
            return this.mNetworkSelectionDisableReason;
        }

        public synchronized void setNetworkSelectionDisableReason(int reason) {
            if (reason >= 0 && reason < 14) {
                this.mNetworkSelectionDisableReason = reason;
                return;
            }
            throw new IllegalArgumentException("Illegal reason value: " + reason);
        }

        public synchronized boolean isDisabledByReason(int reason) {
            return this.mNetworkSelectionDisableReason == reason;
        }

        public synchronized void setDisableTime(long timeStamp) {
            this.mTemporarilyDisabledTimestamp = timeStamp;
        }

        public synchronized long getDisableTime() {
            return this.mTemporarilyDisabledTimestamp;
        }

        public synchronized int getDisableReasonCounter(int reason) {
            if (reason >= 0 && reason < 14) {
                return this.mNetworkSeclectionDisableCounter[reason];
            }
            throw new IllegalArgumentException("Illegal reason value: " + reason);
        }

        public synchronized void setDisableReasonCounter(int reason, int value) {
            if (reason >= 0 && reason < 14) {
                this.mNetworkSeclectionDisableCounter[reason] = value;
                return;
            }
            throw new IllegalArgumentException("Illegal reason value: " + reason);
        }

        public synchronized void incrementDisableReasonCounter(int reason) {
            if (reason >= 0 && reason < 14) {
                int[] iArr = this.mNetworkSeclectionDisableCounter;
                iArr[reason] = iArr[reason] + 1;
                return;
            }
            throw new IllegalArgumentException("Illegal reason value: " + reason);
        }

        public synchronized void clearDisableReasonCounter(int reason) {
            if (reason >= 0 && reason < 14) {
                this.mNetworkSeclectionDisableCounter[reason] = 0;
                return;
            }
            throw new IllegalArgumentException("Illegal reason value: " + reason);
        }

        public synchronized void clearDisableReasonCounter() {
            Arrays.fill(this.mNetworkSeclectionDisableCounter, 0);
        }

        public synchronized String getNetworkSelectionBSSID() {
            return this.mNetworkSelectionBSSID;
        }

        public synchronized void setNetworkSelectionBSSID(String bssid) {
            this.mNetworkSelectionBSSID = bssid;
        }

        public synchronized void copy(NetworkSelectionStatus source) {
            this.mStatus = source.mStatus;
            this.mNetworkSelectionDisableReason = source.mNetworkSelectionDisableReason;
            for (int index = 0; index < 14; index++) {
                this.mNetworkSeclectionDisableCounter[index] = source.mNetworkSeclectionDisableCounter[index];
            }
            this.mTemporarilyDisabledTimestamp = source.mTemporarilyDisabledTimestamp;
            this.mNetworkSelectionBSSID = source.mNetworkSelectionBSSID;
            setSeenInLastQualifiedNetworkSelection(source.getSeenInLastQualifiedNetworkSelection());
            setCandidate(source.getCandidate());
            setCandidateScore(source.getCandidateScore());
            setConnectChoice(source.getConnectChoice());
            setConnectChoiceTimestamp(source.getConnectChoiceTimestamp());
            setHasEverConnected(source.getHasEverConnected());
            setNotRecommended(source.isNotRecommended());
        }

        public synchronized void writeToParcel(Parcel dest) {
            dest.writeInt(getNetworkSelectionStatus());
            dest.writeInt(getNetworkSelectionDisableReason());
            for (int index = 0; index < 14; index++) {
                dest.writeInt(getDisableReasonCounter(index));
            }
            dest.writeLong(getDisableTime());
            dest.writeString(getNetworkSelectionBSSID());
            if (getConnectChoice() != null) {
                dest.writeInt(1);
                dest.writeString(getConnectChoice());
                dest.writeLong(getConnectChoiceTimestamp());
            } else {
                dest.writeInt(-1);
            }
            dest.writeInt(getHasEverConnected() ? 1 : 0);
            dest.writeInt(isNotRecommended() ? 1 : 0);
        }

        public synchronized void readFromParcel(Parcel in) {
            setNetworkSelectionStatus(in.readInt());
            setNetworkSelectionDisableReason(in.readInt());
            for (int index = 0; index < 14; index++) {
                setDisableReasonCounter(index, in.readInt());
            }
            setDisableTime(in.readLong());
            setNetworkSelectionBSSID(in.readString());
            if (in.readInt() == 1) {
                setConnectChoice(in.readString());
                setConnectChoiceTimestamp(in.readLong());
            } else {
                setConnectChoice(null);
                setConnectChoiceTimestamp(-1L);
            }
            setHasEverConnected(in.readInt() != 0);
            setNotRecommended(in.readInt() != 0);
        }
    }

    /* loaded from: classes2.dex */
    public static class RecentFailure {
        public static final int NONE = 0;
        public static final int STATUS_AP_UNABLE_TO_HANDLE_NEW_STA = 17;
        private int mAssociationStatus = 0;

        public synchronized void setAssociationStatus(int status) {
            this.mAssociationStatus = status;
        }

        public synchronized void clear() {
            this.mAssociationStatus = 0;
        }

        public synchronized int getAssociationStatus() {
            return this.mAssociationStatus;
        }
    }

    public synchronized NetworkSelectionStatus getNetworkSelectionStatus() {
        return this.mNetworkSelectionStatus;
    }

    public synchronized void setNetworkSelectionStatus(NetworkSelectionStatus status) {
        this.mNetworkSelectionStatus = status;
    }

    public WifiConfiguration() {
        this.apBand = 0;
        this.apChannel = 0;
        this.dtimInterval = 0;
        this.isLegacyPasspointConfig = false;
        this.userApproved = 0;
        this.meteredOverride = 0;
        this.mNetworkSelectionStatus = new NetworkSelectionStatus();
        this.recentFailure = new RecentFailure();
        this.networkId = -1;
        this.SSID = null;
        this.BSSID = null;
        this.FQDN = null;
        this.roamingConsortiumIds = new long[0];
        this.priority = 0;
        this.hiddenSSID = false;
        this.allowedKeyManagement = new BitSet();
        this.allowedProtocols = new BitSet();
        this.allowedAuthAlgorithms = new BitSet();
        this.allowedPairwiseCiphers = new BitSet();
        this.allowedGroupCiphers = new BitSet();
        this.allowedGroupMgmtCiphers = new BitSet();
        this.allowedSuiteBCiphers = new BitSet();
        this.wepKeys = new String[4];
        for (int i = 0; i < this.wepKeys.length; i++) {
            this.wepKeys[i] = null;
        }
        this.enterpriseConfig = new WifiEnterpriseConfig();
        this.selfAdded = false;
        this.didSelfAdd = false;
        this.ephemeral = false;
        this.meteredHint = false;
        this.meteredOverride = 0;
        this.useExternalScores = false;
        this.validatedInternetAccess = false;
        this.mIpConfiguration = new IpConfiguration();
        this.lastUpdateUid = -1;
        this.creatorUid = -1;
        this.shared = true;
        this.dtimInterval = 0;
        this.mRandomizedMacAddress = MacAddress.fromString(BluetoothAdapter.DEFAULT_MAC_ADDRESS);
        this.dppConnector = null;
        this.dppNetAccessKey = null;
        this.dppNetAccessKeyExpiry = -1;
        this.dppCsign = null;
    }

    public boolean isPasspoint() {
        return (TextUtils.isEmpty(this.FQDN) || TextUtils.isEmpty(this.providerFriendlyName) || this.enterpriseConfig == null || this.enterpriseConfig.getEapMethod() == -1) ? false : true;
    }

    public synchronized boolean isLinked(WifiConfiguration config) {
        if (config != null && config.linkedConfigurations != null && this.linkedConfigurations != null && config.linkedConfigurations.get(configKey()) != null && this.linkedConfigurations.get(config.configKey()) != null) {
            return true;
        }
        return false;
    }

    private protected boolean isEnterprise() {
        return ((!this.allowedKeyManagement.get(2) && !this.allowedKeyManagement.get(3)) || this.enterpriseConfig == null || this.enterpriseConfig.getEapMethod() == -1) ? false : true;
    }

    public String toString() {
        StringBuilder sbuf = new StringBuilder();
        if (this.status == 0) {
            sbuf.append("* ");
        } else if (this.status == 1) {
            sbuf.append("- DSBLE ");
        }
        sbuf.append("ID: ");
        sbuf.append(this.networkId);
        sbuf.append(" SSID: ");
        sbuf.append(this.SSID);
        sbuf.append(" PROVIDER-NAME: ");
        sbuf.append(this.providerFriendlyName);
        sbuf.append(" BSSID: ");
        sbuf.append(this.BSSID);
        sbuf.append(" FQDN: ");
        sbuf.append(this.FQDN);
        sbuf.append(" PRIO: ");
        sbuf.append(this.priority);
        sbuf.append(" HIDDEN: ");
        sbuf.append(this.hiddenSSID);
        sbuf.append('\n');
        sbuf.append(" NetworkSelectionStatus ");
        sbuf.append(this.mNetworkSelectionStatus.getNetworkStatusString() + "\n");
        int sbc = 0;
        if (this.mNetworkSelectionStatus.getNetworkSelectionDisableReason() > 0) {
            sbuf.append(" mNetworkSelectionDisableReason ");
            sbuf.append(this.mNetworkSelectionStatus.getNetworkDisableReasonString() + "\n");
            NetworkSelectionStatus networkSelectionStatus = this.mNetworkSelectionStatus;
            int index = 0;
            while (true) {
                NetworkSelectionStatus networkSelectionStatus2 = this.mNetworkSelectionStatus;
                if (index >= 14) {
                    break;
                }
                if (this.mNetworkSelectionStatus.getDisableReasonCounter(index) != 0) {
                    sbuf.append(NetworkSelectionStatus.getNetworkDisableReasonString(index) + " counter:" + this.mNetworkSelectionStatus.getDisableReasonCounter(index) + "\n");
                }
                index++;
            }
        }
        if (this.mNetworkSelectionStatus.getConnectChoice() != null) {
            sbuf.append(" connect choice: ");
            sbuf.append(this.mNetworkSelectionStatus.getConnectChoice());
            sbuf.append(" connect choice set time: ");
            sbuf.append(TimeUtils.logTimeOfDay(this.mNetworkSelectionStatus.getConnectChoiceTimestamp()));
        }
        sbuf.append(" hasEverConnected: ");
        sbuf.append(this.mNetworkSelectionStatus.getHasEverConnected());
        sbuf.append("\n");
        if (this.numAssociation > 0) {
            sbuf.append(" numAssociation ");
            sbuf.append(this.numAssociation);
            sbuf.append("\n");
        }
        if (this.numNoInternetAccessReports > 0) {
            sbuf.append(" numNoInternetAccessReports ");
            sbuf.append(this.numNoInternetAccessReports);
            sbuf.append("\n");
        }
        if (this.updateTime != null) {
            sbuf.append(" update ");
            sbuf.append(this.updateTime);
            sbuf.append("\n");
        }
        if (this.creationTime != null) {
            sbuf.append(" creation ");
            sbuf.append(this.creationTime);
            sbuf.append("\n");
        }
        if (this.didSelfAdd) {
            sbuf.append(" didSelfAdd");
        }
        if (this.selfAdded) {
            sbuf.append(" selfAdded");
        }
        if (this.validatedInternetAccess) {
            sbuf.append(" validatedInternetAccess");
        }
        if (this.ephemeral) {
            sbuf.append(" ephemeral");
        }
        if (this.meteredHint) {
            sbuf.append(" meteredHint");
        }
        if (this.useExternalScores) {
            sbuf.append(" useExternalScores");
        }
        if (this.didSelfAdd || this.selfAdded || this.validatedInternetAccess || this.ephemeral || this.meteredHint || this.useExternalScores) {
            sbuf.append("\n");
        }
        if (this.meteredOverride != 0) {
            sbuf.append(" meteredOverride ");
            sbuf.append(this.meteredOverride);
            sbuf.append("\n");
        }
        sbuf.append(" KeyMgmt:");
        for (int k = 0; k < this.allowedKeyManagement.size(); k++) {
            if (this.allowedKeyManagement.get(k)) {
                sbuf.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (k < KeyMgmt.strings.length) {
                    sbuf.append(KeyMgmt.strings[k]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        sbuf.append(" Protocols:");
        for (int p = 0; p < this.allowedProtocols.size(); p++) {
            if (this.allowedProtocols.get(p)) {
                sbuf.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (p < Protocol.strings.length) {
                    sbuf.append(Protocol.strings[p]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        sbuf.append('\n');
        sbuf.append(" AuthAlgorithms:");
        for (int a = 0; a < this.allowedAuthAlgorithms.size(); a++) {
            if (this.allowedAuthAlgorithms.get(a)) {
                sbuf.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (a < AuthAlgorithm.strings.length) {
                    sbuf.append(AuthAlgorithm.strings[a]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        sbuf.append('\n');
        sbuf.append(" PairwiseCiphers:");
        for (int pc = 0; pc < this.allowedPairwiseCiphers.size(); pc++) {
            if (this.allowedPairwiseCiphers.get(pc)) {
                sbuf.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (pc < PairwiseCipher.strings.length) {
                    sbuf.append(PairwiseCipher.strings[pc]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        sbuf.append('\n');
        sbuf.append(" GroupCiphers:");
        for (int gc = 0; gc < this.allowedGroupCiphers.size(); gc++) {
            if (this.allowedGroupCiphers.get(gc)) {
                sbuf.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (gc < GroupCipher.strings.length) {
                    sbuf.append(GroupCipher.strings[gc]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        sbuf.append('\n');
        sbuf.append(" GroupMgmtCiphers:");
        for (int gmc = 0; gmc < this.allowedGroupMgmtCiphers.size(); gmc++) {
            if (this.allowedGroupMgmtCiphers.get(gmc)) {
                sbuf.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (gmc < GroupMgmtCipher.strings.length) {
                    sbuf.append(GroupMgmtCipher.strings[gmc]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        sbuf.append('\n');
        sbuf.append(" SuiteBCiphers:");
        while (true) {
            int sbc2 = sbc;
            if (sbc2 >= this.allowedSuiteBCiphers.size()) {
                break;
            }
            if (this.allowedSuiteBCiphers.get(sbc2)) {
                sbuf.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (sbc2 < SuiteBCipher.strings.length) {
                    sbuf.append(SuiteBCipher.strings[sbc2]);
                } else {
                    sbuf.append("??");
                }
            }
            sbc = sbc2 + 1;
        }
        sbuf.append('\n');
        sbuf.append(" PSK: ");
        if (this.preSharedKey != null) {
            sbuf.append('*');
        }
        sbuf.append("\nEnterprise config:\n");
        sbuf.append(this.enterpriseConfig);
        sbuf.append("\nDPP config:\n");
        if (this.dppConnector != null) {
            sbuf.append(" Dpp Connector: *\n");
        }
        if (this.dppNetAccessKey != null) {
            sbuf.append(" Dpp NetAccessKey: *\n");
        }
        if (this.dppCsign != null) {
            sbuf.append(" Dpp Csign: *\n");
        }
        sbuf.append("IP config:\n");
        sbuf.append(this.mIpConfiguration.toString());
        if (this.mNetworkSelectionStatus.getNetworkSelectionBSSID() != null) {
            sbuf.append(" networkSelectionBSSID=" + this.mNetworkSelectionStatus.getNetworkSelectionBSSID());
        }
        long now_ms = SystemClock.elapsedRealtime();
        if (this.mNetworkSelectionStatus.getDisableTime() != -1) {
            sbuf.append('\n');
            long diff = now_ms - this.mNetworkSelectionStatus.getDisableTime();
            if (diff <= 0) {
                sbuf.append(" blackListed since <incorrect>");
            } else {
                sbuf.append(" blackListed: ");
                sbuf.append(Long.toString(diff / 1000));
                sbuf.append("sec ");
            }
        }
        if (this.creatorUid != 0) {
            sbuf.append(" cuid=" + this.creatorUid);
        }
        if (this.creatorName != null) {
            sbuf.append(" cname=" + this.creatorName);
        }
        if (this.lastUpdateUid != 0) {
            sbuf.append(" luid=" + this.lastUpdateUid);
        }
        if (this.lastUpdateName != null) {
            sbuf.append(" lname=" + this.lastUpdateName);
        }
        sbuf.append(" lcuid=" + this.lastConnectUid);
        sbuf.append(" userApproved=" + userApprovedAsString(this.userApproved));
        sbuf.append(" noInternetAccessExpected=" + this.noInternetAccessExpected);
        sbuf.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        if (this.lastConnected != 0) {
            sbuf.append('\n');
            sbuf.append("lastConnected: ");
            sbuf.append(TimeUtils.logTimeOfDay(this.lastConnected));
            sbuf.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        sbuf.append('\n');
        if (this.linkedConfigurations != null) {
            for (String key : this.linkedConfigurations.keySet()) {
                sbuf.append(" linked: ");
                sbuf.append(key);
                sbuf.append('\n');
            }
        }
        sbuf.append("recentFailure: ");
        sbuf.append("Association Rejection code: ");
        sbuf.append(this.recentFailure.getAssociationStatus());
        sbuf.append("\n");
        return sbuf.toString();
    }

    private protected String getPrintableSsid() {
        if (this.SSID == null) {
            return "";
        }
        int length = this.SSID.length();
        if (length > 2 && this.SSID.charAt(0) == '\"' && this.SSID.charAt(length - 1) == '\"') {
            return this.SSID.substring(1, length - 1);
        }
        if (length > 3 && this.SSID.charAt(0) == 'P' && this.SSID.charAt(1) == '\"' && this.SSID.charAt(length - 1) == '\"') {
            WifiSsid wifiSsid = WifiSsid.createFromAsciiEncoded(this.SSID.substring(2, length - 1));
            return wifiSsid.toString();
        }
        return this.SSID;
    }

    public static synchronized String userApprovedAsString(int userApproved) {
        switch (userApproved) {
            case 0:
                return "USER_UNSPECIFIED";
            case 1:
                return "USER_APPROVED";
            case 2:
                return "USER_BANNED";
            default:
                return "INVALID";
        }
    }

    public synchronized String getKeyIdForCredentials(WifiConfiguration current) {
        WifiEnterpriseConfig wifiEnterpriseConfig = null;
        String keyMgmt = null;
        try {
            if (TextUtils.isEmpty(this.SSID)) {
                this.SSID = current.SSID;
            }
            if (this.allowedKeyManagement.cardinality() == 0) {
                this.allowedKeyManagement = current.allowedKeyManagement;
            }
            if (this.allowedKeyManagement.get(2)) {
                keyMgmt = KeyMgmt.strings[2];
            }
            if (this.allowedKeyManagement.get(5)) {
                keyMgmt = KeyMgmt.strings[5];
            }
            if (this.allowedKeyManagement.get(3)) {
                keyMgmt = keyMgmt + KeyMgmt.strings[3];
            }
            if (TextUtils.isEmpty(keyMgmt)) {
                throw new IllegalStateException("Not an EAP network");
            }
            StringBuilder sb = new StringBuilder();
            sb.append(trimStringForKeyId(this.SSID));
            sb.append("_");
            sb.append(keyMgmt);
            sb.append("_");
            WifiEnterpriseConfig wifiEnterpriseConfig2 = this.enterpriseConfig;
            if (current != null) {
                wifiEnterpriseConfig = current.enterpriseConfig;
            }
            sb.append(trimStringForKeyId(wifiEnterpriseConfig2.getKeyId(wifiEnterpriseConfig)));
            return sb.toString();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Invalid config details");
        }
    }

    private synchronized String trimStringForKeyId(String string) {
        return string.replace("\"", "").replace(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized BitSet readBitSet(Parcel src) {
        int cardinality = src.readInt();
        BitSet set = new BitSet();
        for (int i = 0; i < cardinality; i++) {
            set.set(src.readInt());
        }
        return set;
    }

    private static synchronized void writeBitSet(Parcel dest, BitSet set) {
        int nextSetBit = -1;
        dest.writeInt(set.cardinality());
        while (true) {
            int nextSetBit2 = set.nextSetBit(nextSetBit + 1);
            nextSetBit = nextSetBit2;
            if (nextSetBit2 != -1) {
                dest.writeInt(nextSetBit);
            } else {
                return;
            }
        }
    }

    private protected int getAuthType() {
        if (this.allowedKeyManagement.cardinality() > 1) {
            throw new IllegalStateException("More than one auth type set");
        }
        if (this.allowedKeyManagement.get(1)) {
            return 1;
        }
        if (this.allowedKeyManagement.get(4)) {
            return 4;
        }
        if (this.allowedKeyManagement.get(2)) {
            return 2;
        }
        if (this.allowedKeyManagement.get(3)) {
            return 3;
        }
        if (this.allowedKeyManagement.get(10)) {
            return 10;
        }
        if (this.allowedKeyManagement.get(11)) {
            return 11;
        }
        if (this.allowedKeyManagement.get(12)) {
            return 12;
        }
        return this.allowedKeyManagement.get(13) ? 13 : 0;
    }

    public synchronized String configKey(boolean allowCached) {
        String key;
        if (allowCached && this.mCachedConfigKey != null) {
            String key2 = this.mCachedConfigKey;
            return key2;
        }
        String key3 = this.providerFriendlyName;
        if (key3 != null) {
            String key4 = this.FQDN + KeyMgmt.strings[2];
            if (!this.shared) {
                return key4 + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + Integer.toString(UserHandle.getUserId(this.creatorUid));
            }
            return key4;
        }
        if (this.allowedKeyManagement.get(1)) {
            key = this.SSID + KeyMgmt.strings[1];
        } else if (this.allowedKeyManagement.get(2) || this.allowedKeyManagement.get(3)) {
            key = this.SSID + KeyMgmt.strings[2];
        } else if (this.wepKeys[0] != null) {
            key = this.SSID + "WEP";
        } else if (this.allowedKeyManagement.get(10)) {
            key = this.SSID + KeyMgmt.strings[10];
        } else if (this.allowedKeyManagement.get(12)) {
            key = this.SSID + KeyMgmt.strings[12];
        } else if (this.allowedKeyManagement.get(11)) {
            key = this.SSID + KeyMgmt.strings[11];
        } else if (this.allowedKeyManagement.get(13)) {
            key = this.SSID + KeyMgmt.strings[13];
        } else {
            key = this.SSID + KeyMgmt.strings[0];
        }
        if (!this.shared) {
            key = key + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + Integer.toString(UserHandle.getUserId(this.creatorUid));
        }
        this.mCachedConfigKey = key;
        return key;
    }

    public synchronized String configKey() {
        return configKey(false);
    }

    private protected IpConfiguration getIpConfiguration() {
        return this.mIpConfiguration;
    }

    private protected void setIpConfiguration(IpConfiguration ipConfiguration) {
        if (ipConfiguration == null) {
            ipConfiguration = new IpConfiguration();
        }
        this.mIpConfiguration = ipConfiguration;
    }

    private protected StaticIpConfiguration getStaticIpConfiguration() {
        return this.mIpConfiguration.getStaticIpConfiguration();
    }

    private protected void setStaticIpConfiguration(StaticIpConfiguration staticIpConfiguration) {
        this.mIpConfiguration.setStaticIpConfiguration(staticIpConfiguration);
    }

    private protected IpConfiguration.IpAssignment getIpAssignment() {
        return this.mIpConfiguration.ipAssignment;
    }

    private protected void setIpAssignment(IpConfiguration.IpAssignment ipAssignment) {
        this.mIpConfiguration.ipAssignment = ipAssignment;
    }

    private protected IpConfiguration.ProxySettings getProxySettings() {
        return this.mIpConfiguration.proxySettings;
    }

    private protected void setProxySettings(IpConfiguration.ProxySettings proxySettings) {
        this.mIpConfiguration.proxySettings = proxySettings;
    }

    public ProxyInfo getHttpProxy() {
        if (this.mIpConfiguration.proxySettings == IpConfiguration.ProxySettings.NONE) {
            return null;
        }
        return new ProxyInfo(this.mIpConfiguration.httpProxy);
    }

    public void setHttpProxy(ProxyInfo httpProxy) {
        IpConfiguration.ProxySettings proxySettingCopy;
        ProxyInfo httpProxyCopy;
        if (httpProxy == null) {
            this.mIpConfiguration.setProxySettings(IpConfiguration.ProxySettings.NONE);
            this.mIpConfiguration.setHttpProxy(null);
            return;
        }
        if (!Uri.EMPTY.equals(httpProxy.getPacFileUrl())) {
            proxySettingCopy = IpConfiguration.ProxySettings.PAC;
            httpProxyCopy = new ProxyInfo(httpProxy.getPacFileUrl(), httpProxy.getPort());
        } else {
            proxySettingCopy = IpConfiguration.ProxySettings.STATIC;
            httpProxyCopy = new ProxyInfo(httpProxy.getHost(), httpProxy.getPort(), httpProxy.getExclusionListAsString());
        }
        if (!httpProxyCopy.isValid()) {
            throw new IllegalArgumentException("Invalid ProxyInfo: " + httpProxyCopy.toString());
        }
        this.mIpConfiguration.setProxySettings(proxySettingCopy);
        this.mIpConfiguration.setHttpProxy(httpProxyCopy);
    }

    private protected void setProxy(IpConfiguration.ProxySettings settings, ProxyInfo proxy) {
        this.mIpConfiguration.proxySettings = settings;
        this.mIpConfiguration.httpProxy = proxy;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public synchronized void setPasspointManagementObjectTree(String passpointManagementObjectTree) {
        this.mPasspointManagementObjectTree = passpointManagementObjectTree;
    }

    public synchronized String getMoTree() {
        return this.mPasspointManagementObjectTree;
    }

    private protected WifiConfiguration(WifiConfiguration source) {
        this.apBand = 0;
        this.apChannel = 0;
        this.dtimInterval = 0;
        this.isLegacyPasspointConfig = false;
        this.userApproved = 0;
        this.meteredOverride = 0;
        this.mNetworkSelectionStatus = new NetworkSelectionStatus();
        this.recentFailure = new RecentFailure();
        if (source != null) {
            this.networkId = source.networkId;
            this.status = source.status;
            this.SSID = source.SSID;
            this.BSSID = source.BSSID;
            this.FQDN = source.FQDN;
            this.roamingConsortiumIds = (long[]) source.roamingConsortiumIds.clone();
            this.providerFriendlyName = source.providerFriendlyName;
            this.isHomeProviderNetwork = source.isHomeProviderNetwork;
            this.preSharedKey = source.preSharedKey;
            this.mNetworkSelectionStatus.copy(source.getNetworkSelectionStatus());
            this.apBand = source.apBand;
            this.apChannel = source.apChannel;
            this.wepKeys = new String[4];
            for (int i = 0; i < this.wepKeys.length; i++) {
                this.wepKeys[i] = source.wepKeys[i];
            }
            int i2 = source.wepTxKeyIndex;
            this.wepTxKeyIndex = i2;
            this.priority = source.priority;
            this.hiddenSSID = source.hiddenSSID;
            this.allowedKeyManagement = (BitSet) source.allowedKeyManagement.clone();
            this.allowedProtocols = (BitSet) source.allowedProtocols.clone();
            this.allowedAuthAlgorithms = (BitSet) source.allowedAuthAlgorithms.clone();
            this.allowedPairwiseCiphers = (BitSet) source.allowedPairwiseCiphers.clone();
            this.allowedGroupCiphers = (BitSet) source.allowedGroupCiphers.clone();
            this.allowedGroupMgmtCiphers = (BitSet) source.allowedGroupMgmtCiphers.clone();
            this.allowedSuiteBCiphers = (BitSet) source.allowedSuiteBCiphers.clone();
            this.enterpriseConfig = new WifiEnterpriseConfig(source.enterpriseConfig);
            this.defaultGwMacAddress = source.defaultGwMacAddress;
            this.mIpConfiguration = new IpConfiguration(source.mIpConfiguration);
            if (source.linkedConfigurations != null && source.linkedConfigurations.size() > 0) {
                this.linkedConfigurations = new HashMap<>();
                this.linkedConfigurations.putAll(source.linkedConfigurations);
            }
            this.mCachedConfigKey = null;
            this.selfAdded = source.selfAdded;
            this.validatedInternetAccess = source.validatedInternetAccess;
            this.isLegacyPasspointConfig = source.isLegacyPasspointConfig;
            this.ephemeral = source.ephemeral;
            this.meteredHint = source.meteredHint;
            this.meteredOverride = source.meteredOverride;
            this.useExternalScores = source.useExternalScores;
            this.didSelfAdd = source.didSelfAdd;
            this.lastConnectUid = source.lastConnectUid;
            this.lastUpdateUid = source.lastUpdateUid;
            this.creatorUid = source.creatorUid;
            this.creatorName = source.creatorName;
            this.lastUpdateName = source.lastUpdateName;
            this.peerWifiConfiguration = source.peerWifiConfiguration;
            this.lastConnected = source.lastConnected;
            this.lastDisconnected = source.lastDisconnected;
            this.numScorerOverride = source.numScorerOverride;
            this.numScorerOverrideAndSwitchedNetwork = source.numScorerOverrideAndSwitchedNetwork;
            this.numAssociation = source.numAssociation;
            this.userApproved = source.userApproved;
            this.numNoInternetAccessReports = source.numNoInternetAccessReports;
            this.noInternetAccessExpected = source.noInternetAccessExpected;
            this.creationTime = source.creationTime;
            this.updateTime = source.updateTime;
            this.shared = source.shared;
            this.recentFailure.setAssociationStatus(source.recentFailure.getAssociationStatus());
            this.mRandomizedMacAddress = source.mRandomizedMacAddress;
            this.dppConnector = source.dppConnector;
            this.dppNetAccessKey = source.dppNetAccessKey;
            this.dppNetAccessKeyExpiry = source.dppNetAccessKeyExpiry;
            this.dppCsign = source.dppCsign;
            this.requirePMF = source.requirePMF;
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        long[] jArr;
        String[] strArr;
        dest.writeInt(this.networkId);
        dest.writeInt(this.status);
        this.mNetworkSelectionStatus.writeToParcel(dest);
        dest.writeString(this.SSID);
        dest.writeString(this.BSSID);
        dest.writeInt(this.apBand);
        dest.writeInt(this.apChannel);
        dest.writeString(this.FQDN);
        dest.writeString(this.providerFriendlyName);
        dest.writeInt(this.isHomeProviderNetwork ? 1 : 0);
        dest.writeInt(this.roamingConsortiumIds.length);
        for (long roamingConsortiumId : this.roamingConsortiumIds) {
            dest.writeLong(roamingConsortiumId);
        }
        dest.writeString(this.preSharedKey);
        for (String wepKey : this.wepKeys) {
            dest.writeString(wepKey);
        }
        dest.writeInt(this.wepTxKeyIndex);
        dest.writeInt(this.priority);
        dest.writeInt(this.hiddenSSID ? 1 : 0);
        dest.writeInt(this.requirePMF ? 1 : 0);
        dest.writeString(this.updateIdentifier);
        writeBitSet(dest, this.allowedKeyManagement);
        writeBitSet(dest, this.allowedProtocols);
        writeBitSet(dest, this.allowedAuthAlgorithms);
        writeBitSet(dest, this.allowedPairwiseCiphers);
        writeBitSet(dest, this.allowedGroupCiphers);
        writeBitSet(dest, this.allowedGroupMgmtCiphers);
        writeBitSet(dest, this.allowedSuiteBCiphers);
        dest.writeParcelable(this.enterpriseConfig, flags);
        dest.writeParcelable(this.mIpConfiguration, flags);
        dest.writeString(this.dhcpServer);
        dest.writeString(this.defaultGwMacAddress);
        dest.writeInt(this.selfAdded ? 1 : 0);
        dest.writeInt(this.didSelfAdd ? 1 : 0);
        dest.writeInt(this.validatedInternetAccess ? 1 : 0);
        dest.writeInt(this.isLegacyPasspointConfig ? 1 : 0);
        dest.writeInt(this.ephemeral ? 1 : 0);
        dest.writeInt(this.meteredHint ? 1 : 0);
        dest.writeInt(this.meteredOverride);
        dest.writeInt(this.useExternalScores ? 1 : 0);
        dest.writeInt(this.creatorUid);
        dest.writeInt(this.lastConnectUid);
        dest.writeInt(this.lastUpdateUid);
        dest.writeString(this.creatorName);
        dest.writeString(this.lastUpdateName);
        dest.writeInt(this.numScorerOverride);
        dest.writeInt(this.numScorerOverrideAndSwitchedNetwork);
        dest.writeInt(this.numAssociation);
        dest.writeInt(this.userApproved);
        dest.writeInt(this.numNoInternetAccessReports);
        dest.writeInt(this.noInternetAccessExpected ? 1 : 0);
        dest.writeInt(this.shared ? 1 : 0);
        dest.writeString(this.mPasspointManagementObjectTree);
        dest.writeInt(this.recentFailure.getAssociationStatus());
        dest.writeParcelable(this.mRandomizedMacAddress, flags);
        dest.writeString(this.dppConnector);
        dest.writeString(this.dppNetAccessKey);
        dest.writeInt(this.dppNetAccessKeyExpiry);
        dest.writeString(this.dppCsign);
    }

    public synchronized byte[] getBytesForBackup() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeInt(2);
        BackupUtils.writeString(out, this.SSID);
        out.writeInt(this.apBand);
        out.writeInt(this.apChannel);
        BackupUtils.writeString(out, this.preSharedKey);
        out.writeInt(getAuthType());
        return baos.toByteArray();
    }

    public static synchronized WifiConfiguration getWifiConfigFromBackup(DataInputStream in) throws IOException, BackupUtils.BadVersionException {
        WifiConfiguration config = new WifiConfiguration();
        int version = in.readInt();
        if (version < 1 || version > 2) {
            throw new BackupUtils.BadVersionException("Unknown Backup Serialization Version");
        }
        if (version == 1) {
            return null;
        }
        config.SSID = BackupUtils.readString(in);
        config.apBand = in.readInt();
        config.apChannel = in.readInt();
        config.preSharedKey = BackupUtils.readString(in);
        config.allowedKeyManagement.set(in.readInt());
        return config;
    }
}
