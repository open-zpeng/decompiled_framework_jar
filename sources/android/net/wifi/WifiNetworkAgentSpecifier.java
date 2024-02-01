package android.net.wifi;

import android.net.MacAddress;
import android.net.MatchAllNetworkSpecifier;
import android.net.NetworkSpecifier;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import java.util.Objects;

/* loaded from: classes2.dex */
public final class WifiNetworkAgentSpecifier extends NetworkSpecifier implements Parcelable {
    public static final Parcelable.Creator<WifiNetworkAgentSpecifier> CREATOR = new Parcelable.Creator<WifiNetworkAgentSpecifier>() { // from class: android.net.wifi.WifiNetworkAgentSpecifier.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiNetworkAgentSpecifier createFromParcel(Parcel in) {
            WifiConfiguration wifiConfiguration = (WifiConfiguration) in.readParcelable(null);
            int originalRequestorUid = in.readInt();
            String originalRequestorPackageName = in.readString();
            return new WifiNetworkAgentSpecifier(wifiConfiguration, originalRequestorUid, originalRequestorPackageName);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiNetworkAgentSpecifier[] newArray(int size) {
            return new WifiNetworkAgentSpecifier[size];
        }
    };
    private final String mOriginalRequestorPackageName;
    private final int mOriginalRequestorUid;
    private final WifiConfiguration mWifiConfiguration;

    public WifiNetworkAgentSpecifier(WifiConfiguration wifiConfiguration, int originalRequestorUid, String originalRequestorPackageName) {
        Preconditions.checkNotNull(wifiConfiguration);
        this.mWifiConfiguration = wifiConfiguration;
        this.mOriginalRequestorUid = originalRequestorUid;
        this.mOriginalRequestorPackageName = originalRequestorPackageName;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mWifiConfiguration, flags);
        dest.writeInt(this.mOriginalRequestorUid);
        dest.writeString(this.mOriginalRequestorPackageName);
    }

    @Override // android.net.NetworkSpecifier
    public boolean satisfiedBy(NetworkSpecifier other) {
        if (this == other || other == null || (other instanceof MatchAllNetworkSpecifier)) {
            return true;
        }
        if (other instanceof WifiNetworkSpecifier) {
            return satisfiesNetworkSpecifier((WifiNetworkSpecifier) other);
        }
        return equals(other);
    }

    public boolean satisfiesNetworkSpecifier(WifiNetworkSpecifier ns) {
        Preconditions.checkNotNull(ns);
        Preconditions.checkNotNull(ns.ssidPatternMatcher);
        Preconditions.checkNotNull(ns.bssidPatternMatcher);
        Preconditions.checkNotNull(ns.wifiConfiguration.allowedKeyManagement);
        Preconditions.checkNotNull(this.mWifiConfiguration.SSID);
        Preconditions.checkNotNull(this.mWifiConfiguration.BSSID);
        Preconditions.checkNotNull(this.mWifiConfiguration.allowedKeyManagement);
        String ssidWithQuotes = this.mWifiConfiguration.SSID;
        Preconditions.checkState(ssidWithQuotes.startsWith("\"") && ssidWithQuotes.endsWith("\""));
        String ssidWithoutQuotes = ssidWithQuotes.substring(1, ssidWithQuotes.length() - 1);
        if (ns.ssidPatternMatcher.match(ssidWithoutQuotes)) {
            MacAddress bssid = MacAddress.fromString(this.mWifiConfiguration.BSSID);
            MacAddress matchBaseAddress = ns.bssidPatternMatcher.first;
            MacAddress matchMask = ns.bssidPatternMatcher.second;
            return bssid.matches(matchBaseAddress, matchMask) && ns.wifiConfiguration.allowedKeyManagement.equals(this.mWifiConfiguration.allowedKeyManagement) && ns.requestorUid == this.mOriginalRequestorUid && TextUtils.equals(ns.requestorPackageName, this.mOriginalRequestorPackageName);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mWifiConfiguration.SSID, this.mWifiConfiguration.BSSID, this.mWifiConfiguration.allowedKeyManagement, Integer.valueOf(this.mOriginalRequestorUid), this.mOriginalRequestorPackageName);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WifiNetworkAgentSpecifier) {
            WifiNetworkAgentSpecifier lhs = (WifiNetworkAgentSpecifier) obj;
            return Objects.equals(this.mWifiConfiguration.SSID, lhs.mWifiConfiguration.SSID) && Objects.equals(this.mWifiConfiguration.BSSID, lhs.mWifiConfiguration.BSSID) && Objects.equals(this.mWifiConfiguration.allowedKeyManagement, lhs.mWifiConfiguration.allowedKeyManagement) && this.mOriginalRequestorUid == lhs.mOriginalRequestorUid && TextUtils.equals(this.mOriginalRequestorPackageName, lhs.mOriginalRequestorPackageName);
        }
        return false;
    }

    public String toString() {
        return "WifiNetworkAgentSpecifier [WifiConfiguration=, SSID=" + this.mWifiConfiguration.SSID + ", BSSID=" + this.mWifiConfiguration.BSSID + ", mOriginalRequestorUid=" + this.mOriginalRequestorUid + ", mOriginalRequestorPackageName=" + this.mOriginalRequestorPackageName + "]";
    }

    @Override // android.net.NetworkSpecifier
    public void assertValidFromUid(int requestorUid) {
        throw new IllegalStateException("WifiNetworkAgentSpecifier should never be used for requests.");
    }

    @Override // android.net.NetworkSpecifier
    public NetworkSpecifier redact() {
        return null;
    }
}
