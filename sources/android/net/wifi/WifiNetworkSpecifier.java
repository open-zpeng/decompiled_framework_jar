package android.net.wifi;

import android.app.ActivityThread;
import android.net.MacAddress;
import android.net.MatchAllNetworkSpecifier;
import android.net.NetworkSpecifier;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PatternMatcher;
import android.os.Process;
import android.text.TextUtils;
import android.util.Pair;
import com.android.internal.util.Preconditions;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/* loaded from: classes2.dex */
public final class WifiNetworkSpecifier extends NetworkSpecifier implements Parcelable {
    public static final Parcelable.Creator<WifiNetworkSpecifier> CREATOR = new Parcelable.Creator<WifiNetworkSpecifier>() { // from class: android.net.wifi.WifiNetworkSpecifier.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiNetworkSpecifier createFromParcel(Parcel in) {
            PatternMatcher ssidPatternMatcher = (PatternMatcher) in.readParcelable(null);
            MacAddress baseAddress = (MacAddress) in.readParcelable(null);
            MacAddress mask = (MacAddress) in.readParcelable(null);
            Pair<MacAddress, MacAddress> bssidPatternMatcher = Pair.create(baseAddress, mask);
            WifiConfiguration wifiConfiguration = (WifiConfiguration) in.readParcelable(null);
            int requestorUid = in.readInt();
            String requestorPackageName = in.readString();
            return new WifiNetworkSpecifier(ssidPatternMatcher, bssidPatternMatcher, wifiConfiguration, requestorUid, requestorPackageName);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiNetworkSpecifier[] newArray(int size) {
            return new WifiNetworkSpecifier[size];
        }
    };
    public final Pair<MacAddress, MacAddress> bssidPatternMatcher;
    public final String requestorPackageName;
    public final int requestorUid;
    public final PatternMatcher ssidPatternMatcher;
    public final WifiConfiguration wifiConfiguration;

    /* loaded from: classes2.dex */
    public static final class Builder {
        private static final String MATCH_ALL_SSID_PATTERN_PATH = ".*";
        private static final String MATCH_EMPTY_SSID_PATTERN_PATH = "";
        private static final Pair<MacAddress, MacAddress> MATCH_NO_BSSID_PATTERN1 = new Pair<>(MacAddress.BROADCAST_ADDRESS, MacAddress.BROADCAST_ADDRESS);
        private static final Pair<MacAddress, MacAddress> MATCH_NO_BSSID_PATTERN2 = new Pair<>(MacAddress.ALL_ZEROS_ADDRESS, MacAddress.BROADCAST_ADDRESS);
        private static final Pair<MacAddress, MacAddress> MATCH_ALL_BSSID_PATTERN = new Pair<>(MacAddress.ALL_ZEROS_ADDRESS, MacAddress.ALL_ZEROS_ADDRESS);
        private static final MacAddress MATCH_EXACT_BSSID_PATTERN_MASK = MacAddress.BROADCAST_ADDRESS;
        private PatternMatcher mSsidPatternMatcher = null;
        private Pair<MacAddress, MacAddress> mBssidPatternMatcher = null;
        private boolean mIsEnhancedOpen = false;
        private String mWpa2PskPassphrase = null;
        private String mWpa3SaePassphrase = null;
        private WifiEnterpriseConfig mWpa2EnterpriseConfig = null;
        private WifiEnterpriseConfig mWpa3EnterpriseConfig = null;
        private boolean mIsHiddenSSID = false;

        public Builder setSsidPattern(PatternMatcher ssidPattern) {
            Preconditions.checkNotNull(ssidPattern);
            this.mSsidPatternMatcher = ssidPattern;
            return this;
        }

        public Builder setSsid(String ssid) {
            Preconditions.checkNotNull(ssid);
            CharsetEncoder unicodeEncoder = StandardCharsets.UTF_8.newEncoder();
            if (!unicodeEncoder.canEncode(ssid)) {
                throw new IllegalArgumentException("SSID is not a valid unicode string");
            }
            this.mSsidPatternMatcher = new PatternMatcher(ssid, 0);
            return this;
        }

        public Builder setBssidPattern(MacAddress baseAddress, MacAddress mask) {
            Preconditions.checkNotNull(baseAddress, mask);
            this.mBssidPatternMatcher = Pair.create(baseAddress, mask);
            return this;
        }

        public Builder setBssid(MacAddress bssid) {
            Preconditions.checkNotNull(bssid);
            this.mBssidPatternMatcher = Pair.create(bssid, MATCH_EXACT_BSSID_PATTERN_MASK);
            return this;
        }

        public Builder setIsEnhancedOpen(boolean isEnhancedOpen) {
            this.mIsEnhancedOpen = isEnhancedOpen;
            return this;
        }

        public Builder setWpa2Passphrase(String passphrase) {
            Preconditions.checkNotNull(passphrase);
            CharsetEncoder asciiEncoder = StandardCharsets.US_ASCII.newEncoder();
            if (!asciiEncoder.canEncode(passphrase)) {
                throw new IllegalArgumentException("passphrase not ASCII encodable");
            }
            this.mWpa2PskPassphrase = passphrase;
            return this;
        }

        public Builder setWpa3Passphrase(String passphrase) {
            Preconditions.checkNotNull(passphrase);
            CharsetEncoder asciiEncoder = StandardCharsets.US_ASCII.newEncoder();
            if (!asciiEncoder.canEncode(passphrase)) {
                throw new IllegalArgumentException("passphrase not ASCII encodable");
            }
            this.mWpa3SaePassphrase = passphrase;
            return this;
        }

        public Builder setWpa2EnterpriseConfig(WifiEnterpriseConfig enterpriseConfig) {
            Preconditions.checkNotNull(enterpriseConfig);
            this.mWpa2EnterpriseConfig = new WifiEnterpriseConfig(enterpriseConfig);
            return this;
        }

        public Builder setWpa3EnterpriseConfig(WifiEnterpriseConfig enterpriseConfig) {
            Preconditions.checkNotNull(enterpriseConfig);
            this.mWpa3EnterpriseConfig = new WifiEnterpriseConfig(enterpriseConfig);
            return this;
        }

        public Builder setIsHiddenSsid(boolean isHiddenSsid) {
            this.mIsHiddenSSID = isHiddenSsid;
            return this;
        }

        private void setSecurityParamsInWifiConfiguration(WifiConfiguration configuration) {
            if (!TextUtils.isEmpty(this.mWpa2PskPassphrase)) {
                configuration.setSecurityParams(2);
                configuration.preSharedKey = "\"" + this.mWpa2PskPassphrase + "\"";
            } else if (!TextUtils.isEmpty(this.mWpa3SaePassphrase)) {
                configuration.setSecurityParams(4);
                configuration.preSharedKey = "\"" + this.mWpa3SaePassphrase + "\"";
            } else if (this.mWpa2EnterpriseConfig != null) {
                configuration.setSecurityParams(3);
                configuration.enterpriseConfig = this.mWpa2EnterpriseConfig;
            } else if (this.mWpa3EnterpriseConfig != null) {
                configuration.setSecurityParams(5);
                configuration.enterpriseConfig = this.mWpa3EnterpriseConfig;
            } else if (this.mIsEnhancedOpen) {
                configuration.setSecurityParams(6);
            } else {
                configuration.setSecurityParams(0);
            }
        }

        private WifiConfiguration buildWifiConfiguration() {
            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            if (this.mSsidPatternMatcher.getType() == 0) {
                wifiConfiguration.SSID = "\"" + this.mSsidPatternMatcher.getPath() + "\"";
            }
            if (this.mBssidPatternMatcher.second == MATCH_EXACT_BSSID_PATTERN_MASK) {
                wifiConfiguration.BSSID = this.mBssidPatternMatcher.first.toString();
            }
            setSecurityParamsInWifiConfiguration(wifiConfiguration);
            wifiConfiguration.hiddenSSID = this.mIsHiddenSSID;
            return wifiConfiguration;
        }

        private boolean hasSetAnyPattern() {
            return (this.mSsidPatternMatcher == null && this.mBssidPatternMatcher == null) ? false : true;
        }

        private void setMatchAnyPatternIfUnset() {
            if (this.mSsidPatternMatcher == null) {
                this.mSsidPatternMatcher = new PatternMatcher(MATCH_ALL_SSID_PATTERN_PATH, 2);
            }
            if (this.mBssidPatternMatcher == null) {
                this.mBssidPatternMatcher = MATCH_ALL_BSSID_PATTERN;
            }
        }

        private boolean hasSetMatchNonePattern() {
            return (this.mSsidPatternMatcher.getType() != 1 && this.mSsidPatternMatcher.getPath().equals("")) || this.mBssidPatternMatcher.equals(MATCH_NO_BSSID_PATTERN1) || this.mBssidPatternMatcher.equals(MATCH_NO_BSSID_PATTERN2);
        }

        private boolean hasSetMatchAllPattern() {
            if (this.mSsidPatternMatcher.match("") && this.mBssidPatternMatcher.equals(MATCH_ALL_BSSID_PATTERN)) {
                return true;
            }
            return false;
        }

        private void validateSecurityParams() {
            int numSecurityTypes = 0 + (this.mIsEnhancedOpen ? 1 : 0);
            if (numSecurityTypes + (!TextUtils.isEmpty(this.mWpa2PskPassphrase) ? 1 : 0) + (!TextUtils.isEmpty(this.mWpa3SaePassphrase) ? 1 : 0) + (this.mWpa2EnterpriseConfig != null ? 1 : 0) + (this.mWpa3EnterpriseConfig != null ? 1 : 0) > 1) {
                throw new IllegalStateException("only one of setIsEnhancedOpen, setWpa2Passphrase,setWpa3Passphrase, setWpa2EnterpriseConfig or setWpa3EnterpriseConfig can be invoked for network specifier");
            }
        }

        public WifiNetworkSpecifier build() {
            if (!hasSetAnyPattern()) {
                throw new IllegalStateException("one of setSsidPattern/setSsid/setBssidPattern/setBssid should be invoked for specifier");
            }
            setMatchAnyPatternIfUnset();
            if (hasSetMatchNonePattern()) {
                throw new IllegalStateException("cannot set match-none pattern for specifier");
            }
            if (hasSetMatchAllPattern()) {
                throw new IllegalStateException("cannot set match-all pattern for specifier");
            }
            if (this.mIsHiddenSSID && this.mSsidPatternMatcher.getType() != 0) {
                throw new IllegalStateException("setSsid should also be invoked when setIsHiddenSsid is invoked for network specifier");
            }
            validateSecurityParams();
            return new WifiNetworkSpecifier(this.mSsidPatternMatcher, this.mBssidPatternMatcher, buildWifiConfiguration(), Process.myUid(), ActivityThread.currentApplication().getApplicationContext().getOpPackageName());
        }
    }

    public WifiNetworkSpecifier() throws IllegalAccessException {
        throw new IllegalAccessException("Use the builder to create an instance");
    }

    public WifiNetworkSpecifier(PatternMatcher ssidPatternMatcher, Pair<MacAddress, MacAddress> bssidPatternMatcher, WifiConfiguration wifiConfiguration, int requestorUid, String requestorPackageName) {
        Preconditions.checkNotNull(ssidPatternMatcher);
        Preconditions.checkNotNull(bssidPatternMatcher);
        Preconditions.checkNotNull(wifiConfiguration);
        Preconditions.checkNotNull(requestorPackageName);
        this.ssidPatternMatcher = ssidPatternMatcher;
        this.bssidPatternMatcher = bssidPatternMatcher;
        this.wifiConfiguration = wifiConfiguration;
        this.requestorUid = requestorUid;
        this.requestorPackageName = requestorPackageName;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.ssidPatternMatcher, flags);
        dest.writeParcelable(this.bssidPatternMatcher.first, flags);
        dest.writeParcelable(this.bssidPatternMatcher.second, flags);
        dest.writeParcelable(this.wifiConfiguration, flags);
        dest.writeInt(this.requestorUid);
        dest.writeString(this.requestorPackageName);
    }

    public int hashCode() {
        return Objects.hash(this.ssidPatternMatcher.getPath(), Integer.valueOf(this.ssidPatternMatcher.getType()), this.bssidPatternMatcher, this.wifiConfiguration.allowedKeyManagement, Integer.valueOf(this.requestorUid), this.requestorPackageName);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WifiNetworkSpecifier) {
            WifiNetworkSpecifier lhs = (WifiNetworkSpecifier) obj;
            return Objects.equals(this.ssidPatternMatcher.getPath(), lhs.ssidPatternMatcher.getPath()) && Objects.equals(Integer.valueOf(this.ssidPatternMatcher.getType()), Integer.valueOf(lhs.ssidPatternMatcher.getType())) && Objects.equals(this.bssidPatternMatcher, lhs.bssidPatternMatcher) && Objects.equals(this.wifiConfiguration.allowedKeyManagement, lhs.wifiConfiguration.allowedKeyManagement) && this.requestorUid == lhs.requestorUid && TextUtils.equals(this.requestorPackageName, lhs.requestorPackageName);
        }
        return false;
    }

    public String toString() {
        return "WifiNetworkSpecifier [, SSID Match pattern=" + this.ssidPatternMatcher + ", BSSID Match pattern=" + this.bssidPatternMatcher + ", SSID=" + this.wifiConfiguration.SSID + ", BSSID=" + this.wifiConfiguration.BSSID + ", requestorUid=" + this.requestorUid + ", requestorPackageName=" + this.requestorPackageName + "]";
    }

    @Override // android.net.NetworkSpecifier
    public boolean satisfiedBy(NetworkSpecifier other) {
        if (this == other || other == null || (other instanceof MatchAllNetworkSpecifier)) {
            return true;
        }
        if (other instanceof WifiNetworkAgentSpecifier) {
            return ((WifiNetworkAgentSpecifier) other).satisfiesNetworkSpecifier(this);
        }
        return equals(other);
    }

    @Override // android.net.NetworkSpecifier
    public void assertValidFromUid(int requestorUid) {
        if (this.requestorUid != requestorUid) {
            throw new SecurityException("mismatched UIDs");
        }
    }
}
