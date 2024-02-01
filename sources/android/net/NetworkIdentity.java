package android.net;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import java.util.Objects;
/* loaded from: classes2.dex */
public class NetworkIdentity implements Comparable<NetworkIdentity> {
    @Deprecated
    public static final boolean COMBINE_SUBTYPE_ENABLED = true;
    public static final int SUBTYPE_COMBINED = -1;
    private static final String TAG = "NetworkIdentity";
    final boolean mDefaultNetwork;
    final boolean mMetered;
    final String mNetworkId;
    final boolean mRoaming;
    final int mSubType = -1;
    final String mSubscriberId;
    final int mType;

    public synchronized NetworkIdentity(int type, int subType, String subscriberId, String networkId, boolean roaming, boolean metered, boolean defaultNetwork) {
        this.mType = type;
        this.mSubscriberId = subscriberId;
        this.mNetworkId = networkId;
        this.mRoaming = roaming;
        this.mMetered = metered;
        this.mDefaultNetwork = defaultNetwork;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mType), Integer.valueOf(this.mSubType), this.mSubscriberId, this.mNetworkId, Boolean.valueOf(this.mRoaming), Boolean.valueOf(this.mMetered), Boolean.valueOf(this.mDefaultNetwork));
    }

    public boolean equals(Object obj) {
        if (obj instanceof NetworkIdentity) {
            NetworkIdentity ident = (NetworkIdentity) obj;
            return this.mType == ident.mType && this.mSubType == ident.mSubType && this.mRoaming == ident.mRoaming && Objects.equals(this.mSubscriberId, ident.mSubscriberId) && Objects.equals(this.mNetworkId, ident.mNetworkId) && this.mMetered == ident.mMetered && this.mDefaultNetwork == ident.mDefaultNetwork;
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        builder.append("type=");
        builder.append(ConnectivityManager.getNetworkTypeName(this.mType));
        builder.append(", subType=");
        builder.append("COMBINED");
        if (this.mSubscriberId != null) {
            builder.append(", subscriberId=");
            builder.append(scrubSubscriberId(this.mSubscriberId));
        }
        if (this.mNetworkId != null) {
            builder.append(", networkId=");
            builder.append(this.mNetworkId);
        }
        if (this.mRoaming) {
            builder.append(", ROAMING");
        }
        builder.append(", metered=");
        builder.append(this.mMetered);
        builder.append(", defaultNetwork=");
        builder.append(this.mDefaultNetwork);
        builder.append("}");
        return builder.toString();
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long tag) {
        long start = proto.start(tag);
        proto.write(1120986464257L, this.mType);
        if (this.mSubscriberId != null) {
            proto.write(1138166333442L, scrubSubscriberId(this.mSubscriberId));
        }
        proto.write(1138166333443L, this.mNetworkId);
        proto.write(1133871366148L, this.mRoaming);
        proto.write(1133871366149L, this.mMetered);
        proto.write(1133871366150L, this.mDefaultNetwork);
        proto.end(start);
    }

    public synchronized int getType() {
        return this.mType;
    }

    public synchronized int getSubType() {
        return this.mSubType;
    }

    public synchronized String getSubscriberId() {
        return this.mSubscriberId;
    }

    public synchronized String getNetworkId() {
        return this.mNetworkId;
    }

    public synchronized boolean getRoaming() {
        return this.mRoaming;
    }

    public synchronized boolean getMetered() {
        return this.mMetered;
    }

    public synchronized boolean getDefaultNetwork() {
        return this.mDefaultNetwork;
    }

    public static synchronized String scrubSubscriberId(String subscriberId) {
        if (Build.IS_ENG) {
            return subscriberId;
        }
        if (subscriberId != null) {
            return subscriberId.substring(0, Math.min(6, subscriberId.length())) + "...";
        }
        return "null";
    }

    public static synchronized String[] scrubSubscriberId(String[] subscriberId) {
        if (subscriberId == null) {
            return null;
        }
        String[] res = new String[subscriberId.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = scrubSubscriberId(subscriberId[i]);
        }
        return res;
    }

    public static synchronized NetworkIdentity buildNetworkIdentity(Context context, NetworkState state, boolean defaultNetwork) {
        int type = state.networkInfo.getType();
        int subType = state.networkInfo.getSubtype();
        String subscriberId = null;
        String networkId = null;
        boolean roaming = !state.networkCapabilities.hasCapability(18);
        boolean metered = !state.networkCapabilities.hasCapability(11);
        if (ConnectivityManager.isNetworkTypeMobile(type)) {
            if (state.subscriberId == null && state.networkInfo.getState() != NetworkInfo.State.DISCONNECTED && state.networkInfo.getState() != NetworkInfo.State.UNKNOWN) {
                Slog.w(TAG, "Active mobile network without subscriber! ni = " + state.networkInfo);
            }
            subscriberId = state.subscriberId;
        } else if (type == 1) {
            if (state.networkId != null) {
                networkId = state.networkId;
            } else {
                WifiManager wifi = (WifiManager) context.getSystemService("wifi");
                WifiInfo info = wifi.getConnectionInfo();
                networkId = info != null ? info.getSSID() : null;
            }
        }
        return new NetworkIdentity(type, subType, subscriberId, networkId, roaming, metered, defaultNetwork);
    }

    @Override // java.lang.Comparable
    public synchronized int compareTo(NetworkIdentity another) {
        int res = Integer.compare(this.mType, another.mType);
        if (res == 0) {
            res = Integer.compare(this.mSubType, another.mSubType);
        }
        if (res == 0 && this.mSubscriberId != null && another.mSubscriberId != null) {
            res = this.mSubscriberId.compareTo(another.mSubscriberId);
        }
        if (res == 0 && this.mNetworkId != null && another.mNetworkId != null) {
            res = this.mNetworkId.compareTo(another.mNetworkId);
        }
        if (res == 0) {
            res = Boolean.compare(this.mRoaming, another.mRoaming);
        }
        if (res == 0) {
            res = Boolean.compare(this.mMetered, another.mMetered);
        }
        if (res == 0) {
            return Boolean.compare(this.mDefaultNetwork, another.mDefaultNetwork);
        }
        return res;
    }
}
