package android.telephony.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Telephony;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.RILConstants;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes2.dex */
public class ApnSetting implements Parcelable {
    private static final Map<Integer, String> APN_TYPE_INT_MAP;
    private static final Map<String, Integer> APN_TYPE_STRING_MAP = new ArrayMap();
    public static final int AUTH_TYPE_CHAP = 2;
    public static final int AUTH_TYPE_NONE = 0;
    public static final int AUTH_TYPE_PAP = 1;
    public static final int AUTH_TYPE_PAP_OR_CHAP = 3;
    public static final Parcelable.Creator<ApnSetting> CREATOR;
    private static final String LOG_TAG = "ApnSetting";
    public static final int MVNO_TYPE_GID = 2;
    public static final int MVNO_TYPE_ICCID = 3;
    public static final int MVNO_TYPE_IMSI = 1;
    private static final Map<Integer, String> MVNO_TYPE_INT_MAP;
    public static final int MVNO_TYPE_SPN = 0;
    private static final Map<String, Integer> MVNO_TYPE_STRING_MAP;
    private static final int NOT_IN_MAP_INT = -1;
    private static final int NO_PORT_SPECIFIED = -1;
    private static final Map<Integer, String> PROTOCOL_INT_MAP;
    public static final int PROTOCOL_IP = 0;
    public static final int PROTOCOL_IPV4V6 = 2;
    public static final int PROTOCOL_IPV6 = 1;
    public static final int PROTOCOL_PPP = 3;
    private static final Map<String, Integer> PROTOCOL_STRING_MAP;
    private static final int TYPE_ALL_BUT_IA = 767;
    public static final int TYPE_CBS = 128;
    public static final int TYPE_DEFAULT = 17;
    public static final int TYPE_DUN = 8;
    public static final int TYPE_EMERGENCY = 512;
    public static final int TYPE_FOTA = 32;
    public static final int TYPE_HIPRI = 16;
    public static final int TYPE_IA = 256;
    public static final int TYPE_IMS = 64;
    public static final int TYPE_MMS = 2;
    public static final int TYPE_SUPL = 4;
    private static final boolean VDBG = false;
    private final String mApnName;
    private final int mApnTypeBitmask;
    private final int mAuthType;
    private final boolean mCarrierEnabled;
    private final String mEntryName;
    private final int mId;
    private final int mMaxConns;
    private final int mMaxConnsTime;
    private final InetAddress mMmsProxyAddress;
    private final int mMmsProxyPort;
    private final Uri mMmsc;
    private final boolean mModemCognitive;
    private final int mMtu;
    private final String mMvnoMatchData;
    private final int mMvnoType;
    private final int mNetworkTypeBitmask;
    private final String mOperatorNumeric;
    private final String mPassword;
    private boolean mPermanentFailed;
    private final int mProfileId;
    private final int mProtocol;
    private final InetAddress mProxyAddress;
    private final int mProxyPort;
    private final int mRoamingProtocol;
    private final String mUser;
    private final int mWaitTime;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ApnType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface AuthType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface MvnoType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ProtocolType {
    }

    static {
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_ALL, 767);
        APN_TYPE_STRING_MAP.put("default", 17);
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_MMS, 2);
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_SUPL, 4);
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_DUN, 8);
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_HIPRI, 16);
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_FOTA, 32);
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_IMS, 64);
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_CBS, 128);
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_IA, 256);
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_EMERGENCY, 512);
        APN_TYPE_INT_MAP = new ArrayMap();
        APN_TYPE_INT_MAP.put(17, "default");
        APN_TYPE_INT_MAP.put(2, PhoneConstants.APN_TYPE_MMS);
        APN_TYPE_INT_MAP.put(4, PhoneConstants.APN_TYPE_SUPL);
        APN_TYPE_INT_MAP.put(8, PhoneConstants.APN_TYPE_DUN);
        APN_TYPE_INT_MAP.put(16, PhoneConstants.APN_TYPE_HIPRI);
        APN_TYPE_INT_MAP.put(32, PhoneConstants.APN_TYPE_FOTA);
        APN_TYPE_INT_MAP.put(64, PhoneConstants.APN_TYPE_IMS);
        APN_TYPE_INT_MAP.put(128, PhoneConstants.APN_TYPE_CBS);
        APN_TYPE_INT_MAP.put(256, PhoneConstants.APN_TYPE_IA);
        APN_TYPE_INT_MAP.put(512, PhoneConstants.APN_TYPE_EMERGENCY);
        PROTOCOL_STRING_MAP = new ArrayMap();
        PROTOCOL_STRING_MAP.put(RILConstants.SETUP_DATA_PROTOCOL_IP, 0);
        PROTOCOL_STRING_MAP.put(RILConstants.SETUP_DATA_PROTOCOL_IPV6, 1);
        PROTOCOL_STRING_MAP.put(RILConstants.SETUP_DATA_PROTOCOL_IPV4V6, 2);
        PROTOCOL_STRING_MAP.put("PPP", 3);
        PROTOCOL_INT_MAP = new ArrayMap();
        PROTOCOL_INT_MAP.put(0, RILConstants.SETUP_DATA_PROTOCOL_IP);
        PROTOCOL_INT_MAP.put(1, RILConstants.SETUP_DATA_PROTOCOL_IPV6);
        PROTOCOL_INT_MAP.put(2, RILConstants.SETUP_DATA_PROTOCOL_IPV4V6);
        PROTOCOL_INT_MAP.put(3, "PPP");
        MVNO_TYPE_STRING_MAP = new ArrayMap();
        MVNO_TYPE_STRING_MAP.put("spn", 0);
        MVNO_TYPE_STRING_MAP.put("imsi", 1);
        MVNO_TYPE_STRING_MAP.put("gid", 2);
        MVNO_TYPE_STRING_MAP.put("iccid", 3);
        MVNO_TYPE_INT_MAP = new ArrayMap();
        MVNO_TYPE_INT_MAP.put(0, "spn");
        MVNO_TYPE_INT_MAP.put(1, "imsi");
        MVNO_TYPE_INT_MAP.put(2, "gid");
        MVNO_TYPE_INT_MAP.put(3, "iccid");
        CREATOR = new Parcelable.Creator<ApnSetting>() { // from class: android.telephony.data.ApnSetting.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ApnSetting createFromParcel(Parcel in) {
                return ApnSetting.readFromParcel(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ApnSetting[] newArray(int size) {
                return new ApnSetting[size];
            }
        };
    }

    public synchronized int getMtu() {
        return this.mMtu;
    }

    public synchronized int getProfileId() {
        return this.mProfileId;
    }

    public synchronized boolean getModemCognitive() {
        return this.mModemCognitive;
    }

    public synchronized int getMaxConns() {
        return this.mMaxConns;
    }

    public synchronized int getWaitTime() {
        return this.mWaitTime;
    }

    public synchronized int getMaxConnsTime() {
        return this.mMaxConnsTime;
    }

    public synchronized String getMvnoMatchData() {
        return this.mMvnoMatchData;
    }

    public synchronized boolean getPermanentFailed() {
        return this.mPermanentFailed;
    }

    public synchronized void setPermanentFailed(boolean permanentFailed) {
        this.mPermanentFailed = permanentFailed;
    }

    public String getEntryName() {
        return this.mEntryName;
    }

    public String getApnName() {
        return this.mApnName;
    }

    public InetAddress getProxyAddress() {
        return this.mProxyAddress;
    }

    public int getProxyPort() {
        return this.mProxyPort;
    }

    public Uri getMmsc() {
        return this.mMmsc;
    }

    public InetAddress getMmsProxyAddress() {
        return this.mMmsProxyAddress;
    }

    public int getMmsProxyPort() {
        return this.mMmsProxyPort;
    }

    public String getUser() {
        return this.mUser;
    }

    public String getPassword() {
        return this.mPassword;
    }

    public int getAuthType() {
        return this.mAuthType;
    }

    public int getApnTypeBitmask() {
        return this.mApnTypeBitmask;
    }

    public int getId() {
        return this.mId;
    }

    public String getOperatorNumeric() {
        return this.mOperatorNumeric;
    }

    public int getProtocol() {
        return this.mProtocol;
    }

    public int getRoamingProtocol() {
        return this.mRoamingProtocol;
    }

    public boolean isEnabled() {
        return this.mCarrierEnabled;
    }

    public int getNetworkTypeBitmask() {
        return this.mNetworkTypeBitmask;
    }

    public int getMvnoType() {
        return this.mMvnoType;
    }

    private synchronized ApnSetting(Builder builder) {
        this.mPermanentFailed = false;
        this.mEntryName = builder.mEntryName;
        this.mApnName = builder.mApnName;
        this.mProxyAddress = builder.mProxyAddress;
        this.mProxyPort = builder.mProxyPort;
        this.mMmsc = builder.mMmsc;
        this.mMmsProxyAddress = builder.mMmsProxyAddress;
        this.mMmsProxyPort = builder.mMmsProxyPort;
        this.mUser = builder.mUser;
        this.mPassword = builder.mPassword;
        this.mAuthType = builder.mAuthType;
        this.mApnTypeBitmask = builder.mApnTypeBitmask;
        this.mId = builder.mId;
        this.mOperatorNumeric = builder.mOperatorNumeric;
        this.mProtocol = builder.mProtocol;
        this.mRoamingProtocol = builder.mRoamingProtocol;
        this.mMtu = builder.mMtu;
        this.mCarrierEnabled = builder.mCarrierEnabled;
        this.mNetworkTypeBitmask = builder.mNetworkTypeBitmask;
        this.mProfileId = builder.mProfileId;
        this.mModemCognitive = builder.mModemCognitive;
        this.mMaxConns = builder.mMaxConns;
        this.mWaitTime = builder.mWaitTime;
        this.mMaxConnsTime = builder.mMaxConnsTime;
        this.mMvnoType = builder.mMvnoType;
        this.mMvnoMatchData = builder.mMvnoMatchData;
    }

    public static synchronized ApnSetting makeApnSetting(int id, String operatorNumeric, String entryName, String apnName, InetAddress proxy, int port, Uri mmsc, InetAddress mmsProxy, int mmsPort, String user, String password, int authType, int mApnTypeBitmask, int protocol, int roamingProtocol, boolean carrierEnabled, int networkTypeBitmask, int profileId, boolean modemCognitive, int maxConns, int waitTime, int maxConnsTime, int mtu, int mvnoType, String mvnoMatchData) {
        return new Builder().setId(id).setOperatorNumeric(operatorNumeric).setEntryName(entryName).setApnName(apnName).setProxyAddress(proxy).setProxyPort(port).setMmsc(mmsc).setMmsProxyAddress(mmsProxy).setMmsProxyPort(mmsPort).setUser(user).setPassword(password).setAuthType(authType).setApnTypeBitmask(mApnTypeBitmask).setProtocol(protocol).setRoamingProtocol(roamingProtocol).setCarrierEnabled(carrierEnabled).setNetworkTypeBitmask(networkTypeBitmask).setProfileId(profileId).setModemCognitive(modemCognitive).setMaxConns(maxConns).setWaitTime(waitTime).setMaxConnsTime(maxConnsTime).setMtu(mtu).setMvnoType(mvnoType).setMvnoMatchData(mvnoMatchData).build();
    }

    public static synchronized ApnSetting makeApnSetting(Cursor cursor) {
        int apnTypesBitmask = parseTypes(cursor.getString(cursor.getColumnIndexOrThrow("type")));
        int networkTypeBitmask = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.NETWORK_TYPE_BITMASK));
        if (networkTypeBitmask == 0) {
            int bearerBitmask = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.BEARER_BITMASK));
            networkTypeBitmask = ServiceState.convertBearerBitmaskToNetworkTypeBitmask(bearerBitmask);
        }
        return makeApnSetting(cursor.getInt(cursor.getColumnIndexOrThrow("_id")), cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.NUMERIC)), cursor.getString(cursor.getColumnIndexOrThrow("name")), cursor.getString(cursor.getColumnIndexOrThrow("apn")), inetAddressFromString(cursor.getString(cursor.getColumnIndexOrThrow("proxy"))), portFromString(cursor.getString(cursor.getColumnIndexOrThrow("port"))), UriFromString(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.MMSC))), inetAddressFromString(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.MMSPROXY))), portFromString(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.MMSPORT))), cursor.getString(cursor.getColumnIndexOrThrow("user")), cursor.getString(cursor.getColumnIndexOrThrow("password")), cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.AUTH_TYPE)), apnTypesBitmask, nullToNotInMapInt(PROTOCOL_STRING_MAP.get(cursor.getString(cursor.getColumnIndexOrThrow("protocol")))), nullToNotInMapInt(PROTOCOL_STRING_MAP.get(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Carriers.ROAMING_PROTOCOL)))), cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.CARRIER_ENABLED)) == 1, networkTypeBitmask, cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.PROFILE_ID)), cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.MODEM_COGNITIVE)) == 1, cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.MAX_CONNS)), cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.WAIT_TIME)), cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Carriers.MAX_CONNS_TIME)), cursor.getInt(cursor.getColumnIndexOrThrow("mtu")), nullToNotInMapInt(MVNO_TYPE_STRING_MAP.get(cursor.getString(cursor.getColumnIndexOrThrow("mvno_type")))), cursor.getString(cursor.getColumnIndexOrThrow("mvno_match_data")));
    }

    public static synchronized ApnSetting makeApnSetting(ApnSetting apn) {
        return makeApnSetting(apn.mId, apn.mOperatorNumeric, apn.mEntryName, apn.mApnName, apn.mProxyAddress, apn.mProxyPort, apn.mMmsc, apn.mMmsProxyAddress, apn.mMmsProxyPort, apn.mUser, apn.mPassword, apn.mAuthType, apn.mApnTypeBitmask, apn.mProtocol, apn.mRoamingProtocol, apn.mCarrierEnabled, apn.mNetworkTypeBitmask, apn.mProfileId, apn.mModemCognitive, apn.mMaxConns, apn.mWaitTime, apn.mMaxConnsTime, apn.mMtu, apn.mMvnoType, apn.mMvnoMatchData);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ApnSettingV4] ");
        sb.append(this.mEntryName);
        sb.append(", ");
        sb.append(this.mId);
        sb.append(", ");
        sb.append(this.mOperatorNumeric);
        sb.append(", ");
        sb.append(this.mApnName);
        sb.append(", ");
        sb.append(inetAddressToString(this.mProxyAddress));
        sb.append(", ");
        sb.append(UriToString(this.mMmsc));
        sb.append(", ");
        sb.append(inetAddressToString(this.mMmsProxyAddress));
        sb.append(", ");
        sb.append(portToString(this.mMmsProxyPort));
        sb.append(", ");
        sb.append(portToString(this.mProxyPort));
        sb.append(", ");
        sb.append(this.mAuthType);
        sb.append(", ");
        String[] types = deParseTypes(this.mApnTypeBitmask).split(",");
        sb.append(TextUtils.join(" | ", types));
        sb.append(", ");
        sb.append(", ");
        sb.append(this.mProtocol);
        sb.append(", ");
        sb.append(this.mRoamingProtocol);
        sb.append(", ");
        sb.append(this.mCarrierEnabled);
        sb.append(", ");
        sb.append(this.mProfileId);
        sb.append(", ");
        sb.append(this.mModemCognitive);
        sb.append(", ");
        sb.append(this.mMaxConns);
        sb.append(", ");
        sb.append(this.mWaitTime);
        sb.append(", ");
        sb.append(this.mMaxConnsTime);
        sb.append(", ");
        sb.append(this.mMtu);
        sb.append(", ");
        sb.append(this.mMvnoType);
        sb.append(", ");
        sb.append(this.mMvnoMatchData);
        sb.append(", ");
        sb.append(this.mPermanentFailed);
        sb.append(", ");
        sb.append(this.mNetworkTypeBitmask);
        return sb.toString();
    }

    public synchronized boolean hasMvnoParams() {
        return (this.mMvnoType == -1 || TextUtils.isEmpty(this.mMvnoMatchData)) ? false : true;
    }

    public synchronized boolean canHandleType(int type) {
        return this.mCarrierEnabled && (this.mApnTypeBitmask & type) == type;
    }

    private synchronized boolean typeSameAny(ApnSetting first, ApnSetting second) {
        if ((first.mApnTypeBitmask & second.mApnTypeBitmask) != 0) {
            return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        if (o instanceof ApnSetting) {
            ApnSetting other = (ApnSetting) o;
            return this.mEntryName.equals(other.mEntryName) && Objects.equals(Integer.valueOf(this.mId), Integer.valueOf(other.mId)) && Objects.equals(this.mOperatorNumeric, other.mOperatorNumeric) && Objects.equals(this.mApnName, other.mApnName) && Objects.equals(this.mProxyAddress, other.mProxyAddress) && Objects.equals(this.mMmsc, other.mMmsc) && Objects.equals(this.mMmsProxyAddress, other.mMmsProxyAddress) && Objects.equals(Integer.valueOf(this.mMmsProxyPort), Integer.valueOf(other.mMmsProxyPort)) && Objects.equals(Integer.valueOf(this.mProxyPort), Integer.valueOf(other.mProxyPort)) && Objects.equals(this.mUser, other.mUser) && Objects.equals(this.mPassword, other.mPassword) && Objects.equals(Integer.valueOf(this.mAuthType), Integer.valueOf(other.mAuthType)) && Objects.equals(Integer.valueOf(this.mApnTypeBitmask), Integer.valueOf(other.mApnTypeBitmask)) && Objects.equals(Integer.valueOf(this.mProtocol), Integer.valueOf(other.mProtocol)) && Objects.equals(Integer.valueOf(this.mRoamingProtocol), Integer.valueOf(other.mRoamingProtocol)) && Objects.equals(Boolean.valueOf(this.mCarrierEnabled), Boolean.valueOf(other.mCarrierEnabled)) && Objects.equals(Integer.valueOf(this.mProfileId), Integer.valueOf(other.mProfileId)) && Objects.equals(Boolean.valueOf(this.mModemCognitive), Boolean.valueOf(other.mModemCognitive)) && Objects.equals(Integer.valueOf(this.mMaxConns), Integer.valueOf(other.mMaxConns)) && Objects.equals(Integer.valueOf(this.mWaitTime), Integer.valueOf(other.mWaitTime)) && Objects.equals(Integer.valueOf(this.mMaxConnsTime), Integer.valueOf(other.mMaxConnsTime)) && Objects.equals(Integer.valueOf(this.mMtu), Integer.valueOf(other.mMtu)) && Objects.equals(Integer.valueOf(this.mMvnoType), Integer.valueOf(other.mMvnoType)) && Objects.equals(this.mMvnoMatchData, other.mMvnoMatchData) && Objects.equals(Integer.valueOf(this.mNetworkTypeBitmask), Integer.valueOf(other.mNetworkTypeBitmask));
        }
        return false;
    }

    public synchronized boolean equals(Object o, boolean isDataRoaming) {
        if (o instanceof ApnSetting) {
            ApnSetting other = (ApnSetting) o;
            if (this.mEntryName.equals(other.mEntryName) && Objects.equals(this.mOperatorNumeric, other.mOperatorNumeric) && Objects.equals(this.mApnName, other.mApnName) && Objects.equals(this.mProxyAddress, other.mProxyAddress) && Objects.equals(this.mMmsc, other.mMmsc) && Objects.equals(this.mMmsProxyAddress, other.mMmsProxyAddress) && Objects.equals(Integer.valueOf(this.mMmsProxyPort), Integer.valueOf(other.mMmsProxyPort)) && Objects.equals(Integer.valueOf(this.mProxyPort), Integer.valueOf(other.mProxyPort)) && Objects.equals(this.mUser, other.mUser) && Objects.equals(this.mPassword, other.mPassword) && Objects.equals(Integer.valueOf(this.mAuthType), Integer.valueOf(other.mAuthType)) && Objects.equals(Integer.valueOf(this.mApnTypeBitmask), Integer.valueOf(other.mApnTypeBitmask))) {
                if (isDataRoaming || Objects.equals(Integer.valueOf(this.mProtocol), Integer.valueOf(other.mProtocol))) {
                    return (!isDataRoaming || Objects.equals(Integer.valueOf(this.mRoamingProtocol), Integer.valueOf(other.mRoamingProtocol))) && Objects.equals(Boolean.valueOf(this.mCarrierEnabled), Boolean.valueOf(other.mCarrierEnabled)) && Objects.equals(Integer.valueOf(this.mProfileId), Integer.valueOf(other.mProfileId)) && Objects.equals(Boolean.valueOf(this.mModemCognitive), Boolean.valueOf(other.mModemCognitive)) && Objects.equals(Integer.valueOf(this.mMaxConns), Integer.valueOf(other.mMaxConns)) && Objects.equals(Integer.valueOf(this.mWaitTime), Integer.valueOf(other.mWaitTime)) && Objects.equals(Integer.valueOf(this.mMaxConnsTime), Integer.valueOf(other.mMaxConnsTime)) && Objects.equals(Integer.valueOf(this.mMtu), Integer.valueOf(other.mMtu)) && Objects.equals(Integer.valueOf(this.mMvnoType), Integer.valueOf(other.mMvnoType)) && Objects.equals(this.mMvnoMatchData, other.mMvnoMatchData);
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public synchronized boolean similar(ApnSetting other) {
        return !canHandleType(8) && !other.canHandleType(8) && Objects.equals(this.mApnName, other.mApnName) && !typeSameAny(this, other) && xorEquals(this.mProxyAddress, other.mProxyAddress) && xorEqualsPort(this.mProxyPort, other.mProxyPort) && xorEquals(Integer.valueOf(this.mProtocol), Integer.valueOf(other.mProtocol)) && xorEquals(Integer.valueOf(this.mRoamingProtocol), Integer.valueOf(other.mRoamingProtocol)) && Objects.equals(Boolean.valueOf(this.mCarrierEnabled), Boolean.valueOf(other.mCarrierEnabled)) && Objects.equals(Integer.valueOf(this.mProfileId), Integer.valueOf(other.mProfileId)) && Objects.equals(Integer.valueOf(this.mMvnoType), Integer.valueOf(other.mMvnoType)) && Objects.equals(this.mMvnoMatchData, other.mMvnoMatchData) && xorEquals(this.mMmsc, other.mMmsc) && xorEquals(this.mMmsProxyAddress, other.mMmsProxyAddress) && xorEqualsPort(this.mMmsProxyPort, other.mMmsProxyPort) && Objects.equals(Integer.valueOf(this.mNetworkTypeBitmask), Integer.valueOf(other.mNetworkTypeBitmask));
    }

    private synchronized boolean xorEquals(String first, String second) {
        return Objects.equals(first, second) || TextUtils.isEmpty(first) || TextUtils.isEmpty(second);
    }

    private synchronized boolean xorEquals(Object first, Object second) {
        return first == null || second == null || first.equals(second);
    }

    private synchronized boolean xorEqualsPort(int first, int second) {
        return first == -1 || second == -1 || Objects.equals(Integer.valueOf(first), Integer.valueOf(second));
    }

    private synchronized String deParseTypes(int apnTypeBitmask) {
        List<String> types = new ArrayList<>();
        for (Integer type : APN_TYPE_INT_MAP.keySet()) {
            if ((type.intValue() & apnTypeBitmask) == type.intValue()) {
                types.add(APN_TYPE_INT_MAP.get(type));
            }
        }
        return TextUtils.join(",", types);
    }

    private synchronized String nullToEmpty(String stringValue) {
        return stringValue == null ? "" : stringValue;
    }

    public synchronized ContentValues toContentValues() {
        ContentValues apnValue = new ContentValues();
        apnValue.put(Telephony.Carriers.NUMERIC, nullToEmpty(this.mOperatorNumeric));
        apnValue.put("name", nullToEmpty(this.mEntryName));
        apnValue.put("apn", nullToEmpty(this.mApnName));
        apnValue.put("proxy", this.mProxyAddress == null ? "" : inetAddressToString(this.mProxyAddress));
        apnValue.put("port", portToString(this.mProxyPort));
        apnValue.put(Telephony.Carriers.MMSC, this.mMmsc == null ? "" : UriToString(this.mMmsc));
        apnValue.put(Telephony.Carriers.MMSPORT, portToString(this.mMmsProxyPort));
        apnValue.put(Telephony.Carriers.MMSPROXY, this.mMmsProxyAddress == null ? "" : inetAddressToString(this.mMmsProxyAddress));
        apnValue.put("user", nullToEmpty(this.mUser));
        apnValue.put("password", nullToEmpty(this.mPassword));
        apnValue.put(Telephony.Carriers.AUTH_TYPE, Integer.valueOf(this.mAuthType));
        String apnType = deParseTypes(this.mApnTypeBitmask);
        apnValue.put("type", nullToEmpty(apnType));
        apnValue.put("protocol", nullToEmpty(PROTOCOL_INT_MAP.get(Integer.valueOf(this.mProtocol))));
        apnValue.put(Telephony.Carriers.ROAMING_PROTOCOL, nullToEmpty(PROTOCOL_INT_MAP.get(Integer.valueOf(this.mRoamingProtocol))));
        apnValue.put(Telephony.Carriers.CARRIER_ENABLED, Boolean.valueOf(this.mCarrierEnabled));
        apnValue.put("mvno_type", nullToEmpty(MVNO_TYPE_INT_MAP.get(Integer.valueOf(this.mMvnoType))));
        apnValue.put(Telephony.Carriers.NETWORK_TYPE_BITMASK, Integer.valueOf(this.mNetworkTypeBitmask));
        return apnValue;
    }

    public static synchronized int parseTypes(String types) {
        String[] split;
        if (TextUtils.isEmpty(types)) {
            return 767;
        }
        int result = 0;
        for (String str : types.split(",")) {
            Integer type = APN_TYPE_STRING_MAP.get(str);
            if (type != null) {
                result |= type.intValue();
            }
        }
        return result;
    }

    private static synchronized Uri UriFromString(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return null;
        }
        return Uri.parse(uri);
    }

    private static synchronized String UriToString(Uri uri) {
        return uri == null ? "" : uri.toString();
    }

    private static synchronized InetAddress inetAddressFromString(String inetAddress) {
        if (TextUtils.isEmpty(inetAddress)) {
            return null;
        }
        try {
            return InetAddress.getByName(inetAddress);
        } catch (UnknownHostException e) {
            Log.e(LOG_TAG, "Can't parse InetAddress from string: unknown host.");
            return null;
        }
    }

    private static synchronized String inetAddressToString(InetAddress inetAddress) {
        if (inetAddress == null) {
            return null;
        }
        String inetAddressString = inetAddress.toString();
        if (TextUtils.isEmpty(inetAddressString)) {
            return null;
        }
        String hostName = inetAddressString.substring(0, inetAddressString.indexOf("/"));
        String address = inetAddressString.substring(inetAddressString.indexOf("/") + 1);
        if (TextUtils.isEmpty(hostName) && TextUtils.isEmpty(address)) {
            return null;
        }
        return TextUtils.isEmpty(hostName) ? address : hostName;
    }

    private static synchronized int portFromString(String strPort) {
        if (TextUtils.isEmpty(strPort)) {
            return -1;
        }
        try {
            int port = Integer.parseInt(strPort);
            return port;
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Can't parse port from String");
            return -1;
        }
    }

    private static synchronized String portToString(int port) {
        return port == -1 ? "" : Integer.toString(port);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mOperatorNumeric);
        dest.writeString(this.mEntryName);
        dest.writeString(this.mApnName);
        dest.writeValue(this.mProxyAddress);
        dest.writeInt(this.mProxyPort);
        dest.writeValue(this.mMmsc);
        dest.writeValue(this.mMmsProxyAddress);
        dest.writeInt(this.mMmsProxyPort);
        dest.writeString(this.mUser);
        dest.writeString(this.mPassword);
        dest.writeInt(this.mAuthType);
        dest.writeInt(this.mApnTypeBitmask);
        dest.writeInt(this.mProtocol);
        dest.writeInt(this.mRoamingProtocol);
        dest.writeInt(this.mCarrierEnabled ? 1 : 0);
        dest.writeInt(this.mMvnoType);
        dest.writeInt(this.mNetworkTypeBitmask);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized ApnSetting readFromParcel(Parcel in) {
        int id = in.readInt();
        String operatorNumeric = in.readString();
        String entryName = in.readString();
        String apnName = in.readString();
        InetAddress proxy = (InetAddress) in.readValue(InetAddress.class.getClassLoader());
        int port = in.readInt();
        Uri mmsc = (Uri) in.readValue(Uri.class.getClassLoader());
        InetAddress mmsProxy = (InetAddress) in.readValue(InetAddress.class.getClassLoader());
        int mmsPort = in.readInt();
        String user = in.readString();
        String password = in.readString();
        int authType = in.readInt();
        int apnTypesBitmask = in.readInt();
        int protocol = in.readInt();
        int roamingProtocol = in.readInt();
        boolean carrierEnabled = in.readInt() > 0;
        int mvnoType = in.readInt();
        int networkTypeBitmask = in.readInt();
        return makeApnSetting(id, operatorNumeric, entryName, apnName, proxy, port, mmsc, mmsProxy, mmsPort, user, password, authType, apnTypesBitmask, protocol, roamingProtocol, carrierEnabled, networkTypeBitmask, 0, false, 0, 0, 0, 0, mvnoType, null);
    }

    private static synchronized int nullToNotInMapInt(Integer value) {
        if (value == null) {
            return -1;
        }
        return value.intValue();
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private String mApnName;
        private int mApnTypeBitmask;
        private int mAuthType;
        private boolean mCarrierEnabled;
        private String mEntryName;
        private int mId;
        private int mMaxConns;
        private int mMaxConnsTime;
        private InetAddress mMmsProxyAddress;
        private Uri mMmsc;
        private boolean mModemCognitive;
        private int mMtu;
        private String mMvnoMatchData;
        private int mNetworkTypeBitmask;
        private String mOperatorNumeric;
        private String mPassword;
        private int mProfileId;
        private InetAddress mProxyAddress;
        private String mUser;
        private int mWaitTime;
        private int mProxyPort = -1;
        private int mMmsProxyPort = -1;
        private int mProtocol = -1;
        private int mRoamingProtocol = -1;
        private int mMvnoType = -1;

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Builder setId(int id) {
            this.mId = id;
            return this;
        }

        public synchronized Builder setMtu(int mtu) {
            this.mMtu = mtu;
            return this;
        }

        public synchronized Builder setProfileId(int profileId) {
            this.mProfileId = profileId;
            return this;
        }

        public synchronized Builder setModemCognitive(boolean modemCognitive) {
            this.mModemCognitive = modemCognitive;
            return this;
        }

        public synchronized Builder setMaxConns(int maxConns) {
            this.mMaxConns = maxConns;
            return this;
        }

        public synchronized Builder setWaitTime(int waitTime) {
            this.mWaitTime = waitTime;
            return this;
        }

        public synchronized Builder setMaxConnsTime(int maxConnsTime) {
            this.mMaxConnsTime = maxConnsTime;
            return this;
        }

        public synchronized Builder setMvnoMatchData(String mvnoMatchData) {
            this.mMvnoMatchData = mvnoMatchData;
            return this;
        }

        public Builder setEntryName(String entryName) {
            this.mEntryName = entryName;
            return this;
        }

        public Builder setApnName(String apnName) {
            this.mApnName = apnName;
            return this;
        }

        public Builder setProxyAddress(InetAddress proxy) {
            this.mProxyAddress = proxy;
            return this;
        }

        public Builder setProxyPort(int port) {
            this.mProxyPort = port;
            return this;
        }

        public Builder setMmsc(Uri mmsc) {
            this.mMmsc = mmsc;
            return this;
        }

        public Builder setMmsProxyAddress(InetAddress mmsProxy) {
            this.mMmsProxyAddress = mmsProxy;
            return this;
        }

        public Builder setMmsProxyPort(int mmsPort) {
            this.mMmsProxyPort = mmsPort;
            return this;
        }

        public Builder setUser(String user) {
            this.mUser = user;
            return this;
        }

        public Builder setPassword(String password) {
            this.mPassword = password;
            return this;
        }

        public Builder setAuthType(int authType) {
            this.mAuthType = authType;
            return this;
        }

        public Builder setApnTypeBitmask(int apnTypeBitmask) {
            this.mApnTypeBitmask = apnTypeBitmask;
            return this;
        }

        public Builder setOperatorNumeric(String operatorNumeric) {
            this.mOperatorNumeric = operatorNumeric;
            return this;
        }

        public Builder setProtocol(int protocol) {
            this.mProtocol = protocol;
            return this;
        }

        public Builder setRoamingProtocol(int roamingProtocol) {
            this.mRoamingProtocol = roamingProtocol;
            return this;
        }

        public Builder setCarrierEnabled(boolean carrierEnabled) {
            this.mCarrierEnabled = carrierEnabled;
            return this;
        }

        public Builder setNetworkTypeBitmask(int networkTypeBitmask) {
            this.mNetworkTypeBitmask = networkTypeBitmask;
            return this;
        }

        public Builder setMvnoType(int mvnoType) {
            this.mMvnoType = mvnoType;
            return this;
        }

        public ApnSetting build() {
            if ((this.mApnTypeBitmask & 1023) == 0 || TextUtils.isEmpty(this.mApnName) || TextUtils.isEmpty(this.mEntryName)) {
                return null;
            }
            return new ApnSetting(this);
        }
    }
}
