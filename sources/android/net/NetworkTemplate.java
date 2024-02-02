package android.net;

import android.net.wifi.WifiInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.BackupUtils;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes2.dex */
public class NetworkTemplate implements Parcelable {
    private static final int BACKUP_VERSION = 1;
    public static final int MATCH_BLUETOOTH = 8;
    public static final int MATCH_ETHERNET = 5;
    public static final int MATCH_MOBILE = 1;
    public static final int MATCH_MOBILE_WILDCARD = 6;
    public static final int MATCH_PROXY = 9;
    public static final int MATCH_WIFI = 4;
    public static final int MATCH_WIFI_WILDCARD = 7;
    private static final String TAG = "NetworkTemplate";
    private final int mDefaultNetwork;
    private final int mMatchRule;
    private final String[] mMatchSubscriberIds;
    private final int mMetered;
    private final String mNetworkId;
    private final int mRoaming;
    private final String mSubscriberId;
    private static boolean sForceAllNetworkTypes = false;
    private protected static final Parcelable.Creator<NetworkTemplate> CREATOR = new Parcelable.Creator<NetworkTemplate>() { // from class: android.net.NetworkTemplate.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkTemplate createFromParcel(Parcel in) {
            return new NetworkTemplate(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkTemplate[] newArray(int size) {
            return new NetworkTemplate[size];
        }
    };

    private static synchronized boolean isKnownMatchRule(int rule) {
        if (rule != 1) {
            switch (rule) {
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public static synchronized void forceAllNetworkTypes() {
        sForceAllNetworkTypes = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static NetworkTemplate buildTemplateMobileAll(String subscriberId) {
        return new NetworkTemplate(1, subscriberId, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static NetworkTemplate buildTemplateMobileWildcard() {
        return new NetworkTemplate(6, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static NetworkTemplate buildTemplateWifiWildcard() {
        return new NetworkTemplate(7, null, null);
    }

    @Deprecated
    private protected static NetworkTemplate buildTemplateWifi() {
        return buildTemplateWifiWildcard();
    }

    public static synchronized NetworkTemplate buildTemplateWifi(String networkId) {
        return new NetworkTemplate(4, null, networkId);
    }

    private protected static NetworkTemplate buildTemplateEthernet() {
        return new NetworkTemplate(5, null, null);
    }

    public static synchronized NetworkTemplate buildTemplateBluetooth() {
        return new NetworkTemplate(8, null, null);
    }

    public static synchronized NetworkTemplate buildTemplateProxy() {
        return new NetworkTemplate(9, null, null);
    }

    private protected NetworkTemplate(int matchRule, String subscriberId, String networkId) {
        this(matchRule, subscriberId, new String[]{subscriberId}, networkId);
    }

    public synchronized NetworkTemplate(int matchRule, String subscriberId, String[] matchSubscriberIds, String networkId) {
        this(matchRule, subscriberId, matchSubscriberIds, networkId, -1, -1, -1);
    }

    public synchronized NetworkTemplate(int matchRule, String subscriberId, String[] matchSubscriberIds, String networkId, int metered, int roaming, int defaultNetwork) {
        this.mMatchRule = matchRule;
        this.mSubscriberId = subscriberId;
        this.mMatchSubscriberIds = matchSubscriberIds;
        this.mNetworkId = networkId;
        this.mMetered = metered;
        this.mRoaming = roaming;
        this.mDefaultNetwork = defaultNetwork;
        if (!isKnownMatchRule(matchRule)) {
            Log.e(TAG, "Unknown network template rule " + matchRule + " will not match any identity.");
        }
    }

    private synchronized NetworkTemplate(Parcel in) {
        this.mMatchRule = in.readInt();
        this.mSubscriberId = in.readString();
        this.mMatchSubscriberIds = in.createStringArray();
        this.mNetworkId = in.readString();
        this.mMetered = in.readInt();
        this.mRoaming = in.readInt();
        this.mDefaultNetwork = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mMatchRule);
        dest.writeString(this.mSubscriberId);
        dest.writeStringArray(this.mMatchSubscriberIds);
        dest.writeString(this.mNetworkId);
        dest.writeInt(this.mMetered);
        dest.writeInt(this.mRoaming);
        dest.writeInt(this.mDefaultNetwork);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("NetworkTemplate: ");
        builder.append("matchRule=");
        builder.append(getMatchRuleName(this.mMatchRule));
        if (this.mSubscriberId != null) {
            builder.append(", subscriberId=");
            builder.append(NetworkIdentity.scrubSubscriberId(this.mSubscriberId));
        }
        if (this.mMatchSubscriberIds != null) {
            builder.append(", matchSubscriberIds=");
            builder.append(Arrays.toString(NetworkIdentity.scrubSubscriberId(this.mMatchSubscriberIds)));
        }
        if (this.mNetworkId != null) {
            builder.append(", networkId=");
            builder.append(this.mNetworkId);
        }
        if (this.mMetered != -1) {
            builder.append(", metered=");
            builder.append(NetworkStats.meteredToString(this.mMetered));
        }
        if (this.mRoaming != -1) {
            builder.append(", roaming=");
            builder.append(NetworkStats.roamingToString(this.mRoaming));
        }
        if (this.mDefaultNetwork != -1) {
            builder.append(", defaultNetwork=");
            builder.append(NetworkStats.defaultNetworkToString(this.mDefaultNetwork));
        }
        return builder.toString();
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mMatchRule), this.mSubscriberId, this.mNetworkId, Integer.valueOf(this.mMetered), Integer.valueOf(this.mRoaming), Integer.valueOf(this.mDefaultNetwork));
    }

    public boolean equals(Object obj) {
        if (obj instanceof NetworkTemplate) {
            NetworkTemplate other = (NetworkTemplate) obj;
            return this.mMatchRule == other.mMatchRule && Objects.equals(this.mSubscriberId, other.mSubscriberId) && Objects.equals(this.mNetworkId, other.mNetworkId) && this.mMetered == other.mMetered && this.mRoaming == other.mRoaming && this.mDefaultNetwork == other.mDefaultNetwork;
        }
        return false;
    }

    public synchronized boolean isMatchRuleMobile() {
        int i = this.mMatchRule;
        return i == 1 || i == 6;
    }

    public synchronized boolean isPersistable() {
        switch (this.mMatchRule) {
            case 6:
            case 7:
                return false;
            default:
                return true;
        }
    }

    private protected int getMatchRule() {
        return this.mMatchRule;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSubscriberId() {
        return this.mSubscriberId;
    }

    public synchronized String getNetworkId() {
        return this.mNetworkId;
    }

    public synchronized boolean matches(NetworkIdentity ident) {
        if (matchesMetered(ident) && matchesRoaming(ident) && matchesDefaultNetwork(ident)) {
            int i = this.mMatchRule;
            if (i == 1) {
                return matchesMobile(ident);
            }
            switch (i) {
                case 4:
                    return matchesWifi(ident);
                case 5:
                    return matchesEthernet(ident);
                case 6:
                    return matchesMobileWildcard(ident);
                case 7:
                    return matchesWifiWildcard(ident);
                case 8:
                    return matchesBluetooth(ident);
                case 9:
                    return matchesProxy(ident);
                default:
                    return false;
            }
        }
        return false;
    }

    private synchronized boolean matchesMetered(NetworkIdentity ident) {
        if (this.mMetered != -1) {
            if (this.mMetered == 1 && ident.mMetered) {
                return true;
            }
            return this.mMetered == 0 && !ident.mMetered;
        }
        return true;
    }

    private synchronized boolean matchesRoaming(NetworkIdentity ident) {
        if (this.mRoaming != -1) {
            if (this.mRoaming == 1 && ident.mRoaming) {
                return true;
            }
            return this.mRoaming == 0 && !ident.mRoaming;
        }
        return true;
    }

    private synchronized boolean matchesDefaultNetwork(NetworkIdentity ident) {
        if (this.mDefaultNetwork != -1) {
            if (this.mDefaultNetwork == 1 && ident.mDefaultNetwork) {
                return true;
            }
            return this.mDefaultNetwork == 0 && !ident.mDefaultNetwork;
        }
        return true;
    }

    public synchronized boolean matchesSubscriberId(String subscriberId) {
        return ArrayUtils.contains(this.mMatchSubscriberIds, subscriberId);
    }

    private synchronized boolean matchesMobile(NetworkIdentity ident) {
        if (ident.mType == 6) {
            return true;
        }
        return (sForceAllNetworkTypes || (ident.mType == 0 && ident.mMetered)) && !ArrayUtils.isEmpty(this.mMatchSubscriberIds) && ArrayUtils.contains(this.mMatchSubscriberIds, ident.mSubscriberId);
    }

    private synchronized boolean matchesWifi(NetworkIdentity ident) {
        if (ident.mType == 1) {
            return Objects.equals(WifiInfo.removeDoubleQuotes(this.mNetworkId), WifiInfo.removeDoubleQuotes(ident.mNetworkId));
        }
        return false;
    }

    private synchronized boolean matchesEthernet(NetworkIdentity ident) {
        if (ident.mType == 9) {
            return true;
        }
        return false;
    }

    private synchronized boolean matchesMobileWildcard(NetworkIdentity ident) {
        if (ident.mType == 6 || sForceAllNetworkTypes) {
            return true;
        }
        return ident.mType == 0 && ident.mMetered;
    }

    private synchronized boolean matchesWifiWildcard(NetworkIdentity ident) {
        int i = ident.mType;
        return i == 1 || i == 13;
    }

    private synchronized boolean matchesBluetooth(NetworkIdentity ident) {
        if (ident.mType == 7) {
            return true;
        }
        return false;
    }

    private synchronized boolean matchesProxy(NetworkIdentity ident) {
        return ident.mType == 16;
    }

    private static synchronized String getMatchRuleName(int matchRule) {
        if (matchRule == 1) {
            return "MOBILE";
        }
        switch (matchRule) {
            case 4:
                return "WIFI";
            case 5:
                return "ETHERNET";
            case 6:
                return "MOBILE_WILDCARD";
            case 7:
                return "WIFI_WILDCARD";
            case 8:
                return "BLUETOOTH";
            case 9:
                return "PROXY";
            default:
                return "UNKNOWN(" + matchRule + ")";
        }
    }

    private protected static NetworkTemplate normalize(NetworkTemplate template, String[] merged) {
        if (template.isMatchRuleMobile() && ArrayUtils.contains(merged, template.mSubscriberId)) {
            return new NetworkTemplate(template.mMatchRule, merged[0], merged, template.mNetworkId);
        }
        return template;
    }

    public synchronized byte[] getBytesForBackup() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeInt(1);
        out.writeInt(this.mMatchRule);
        BackupUtils.writeString(out, this.mSubscriberId);
        BackupUtils.writeString(out, this.mNetworkId);
        return baos.toByteArray();
    }

    public static synchronized NetworkTemplate getNetworkTemplateFromBackup(DataInputStream in) throws IOException, BackupUtils.BadVersionException {
        int version = in.readInt();
        if (version < 1 || version > 1) {
            throw new BackupUtils.BadVersionException("Unknown Backup Serialization Version");
        }
        int matchRule = in.readInt();
        String subscriberId = BackupUtils.readString(in);
        String networkId = BackupUtils.readString(in);
        if (!isKnownMatchRule(matchRule)) {
            throw new BackupUtils.BadVersionException("Restored network template contains unknown match rule " + matchRule);
        }
        return new NetworkTemplate(matchRule, subscriberId, networkId);
    }
}
