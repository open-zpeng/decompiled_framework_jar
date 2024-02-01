package android.net.wifi;

import android.app.ActivityThread;
import android.net.MacAddress;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/* loaded from: classes2.dex */
public final class WifiNetworkSuggestion implements Parcelable {
    public static final Parcelable.Creator<WifiNetworkSuggestion> CREATOR = new Parcelable.Creator<WifiNetworkSuggestion>() { // from class: android.net.wifi.WifiNetworkSuggestion.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiNetworkSuggestion createFromParcel(Parcel in) {
            return new WifiNetworkSuggestion((WifiConfiguration) in.readParcelable(null), in.readBoolean(), in.readBoolean(), in.readInt(), in.readString());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiNetworkSuggestion[] newArray(int size) {
            return new WifiNetworkSuggestion[size];
        }
    };
    public final boolean isAppInteractionRequired;
    public final boolean isUserInteractionRequired;
    public final String suggestorPackageName;
    public final int suggestorUid;
    public final WifiConfiguration wifiConfiguration;

    /* loaded from: classes2.dex */
    public static final class Builder {
        private static final int UNASSIGNED_PRIORITY = -1;
        private String mSsid = null;
        private MacAddress mBssid = null;
        private boolean mIsEnhancedOpen = false;
        private String mWpa2PskPassphrase = null;
        private String mWpa3SaePassphrase = null;
        private WifiEnterpriseConfig mWpa2EnterpriseConfig = null;
        private WifiEnterpriseConfig mWpa3EnterpriseConfig = null;
        private boolean mIsHiddenSSID = false;
        private boolean mIsAppInteractionRequired = false;
        private boolean mIsUserInteractionRequired = false;
        private boolean mIsMetered = false;
        private int mPriority = -1;

        public Builder setSsid(String ssid) {
            Preconditions.checkNotNull(ssid);
            CharsetEncoder unicodeEncoder = StandardCharsets.UTF_8.newEncoder();
            if (!unicodeEncoder.canEncode(ssid)) {
                throw new IllegalArgumentException("SSID is not a valid unicode string");
            }
            this.mSsid = new String(ssid);
            return this;
        }

        public Builder setBssid(MacAddress bssid) {
            Preconditions.checkNotNull(bssid);
            this.mBssid = MacAddress.fromBytes(bssid.toByteArray());
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

        public Builder setIsAppInteractionRequired(boolean isAppInteractionRequired) {
            this.mIsAppInteractionRequired = isAppInteractionRequired;
            return this;
        }

        public Builder setIsUserInteractionRequired(boolean isUserInteractionRequired) {
            this.mIsUserInteractionRequired = isUserInteractionRequired;
            return this;
        }

        public Builder setPriority(int priority) {
            if (priority < 0) {
                throw new IllegalArgumentException("Invalid priority value " + priority);
            }
            this.mPriority = priority;
            return this;
        }

        public Builder setIsMetered(boolean isMetered) {
            this.mIsMetered = isMetered;
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
            wifiConfiguration.SSID = "\"" + this.mSsid + "\"";
            MacAddress macAddress = this.mBssid;
            if (macAddress != null) {
                wifiConfiguration.BSSID = macAddress.toString();
            }
            setSecurityParamsInWifiConfiguration(wifiConfiguration);
            wifiConfiguration.hiddenSSID = this.mIsHiddenSSID;
            wifiConfiguration.priority = this.mPriority;
            wifiConfiguration.meteredOverride = this.mIsMetered ? 1 : 0;
            return wifiConfiguration;
        }

        private void validateSecurityParams() {
            int numSecurityTypes = 0 + (this.mIsEnhancedOpen ? 1 : 0);
            if (numSecurityTypes + (!TextUtils.isEmpty(this.mWpa2PskPassphrase) ? 1 : 0) + (!TextUtils.isEmpty(this.mWpa3SaePassphrase) ? 1 : 0) + (this.mWpa2EnterpriseConfig != null ? 1 : 0) + (this.mWpa3EnterpriseConfig != null ? 1 : 0) > 1) {
                throw new IllegalStateException("only one of setIsEnhancedOpen, setWpa2Passphrase,setWpa3Passphrase, setWpa2EnterpriseConfig or setWpa3EnterpriseConfig can be invoked for network specifier");
            }
        }

        public WifiNetworkSuggestion build() {
            String str = this.mSsid;
            if (str == null) {
                throw new IllegalStateException("setSsid should be invoked for suggestion");
            }
            if (TextUtils.isEmpty(str)) {
                throw new IllegalStateException("invalid ssid for suggestion");
            }
            MacAddress macAddress = this.mBssid;
            if (macAddress != null && (macAddress.equals(MacAddress.BROADCAST_ADDRESS) || this.mBssid.equals(MacAddress.ALL_ZEROS_ADDRESS))) {
                throw new IllegalStateException("invalid bssid for suggestion");
            }
            validateSecurityParams();
            return new WifiNetworkSuggestion(buildWifiConfiguration(), this.mIsAppInteractionRequired, this.mIsUserInteractionRequired, Process.myUid(), ActivityThread.currentApplication().getApplicationContext().getOpPackageName());
        }
    }

    public WifiNetworkSuggestion() {
        this.wifiConfiguration = null;
        this.isAppInteractionRequired = false;
        this.isUserInteractionRequired = false;
        this.suggestorUid = -1;
        this.suggestorPackageName = null;
    }

    public WifiNetworkSuggestion(WifiConfiguration wifiConfiguration, boolean isAppInteractionRequired, boolean isUserInteractionRequired, int suggestorUid, String suggestorPackageName) {
        Preconditions.checkNotNull(wifiConfiguration);
        Preconditions.checkNotNull(suggestorPackageName);
        this.wifiConfiguration = wifiConfiguration;
        this.isAppInteractionRequired = isAppInteractionRequired;
        this.isUserInteractionRequired = isUserInteractionRequired;
        this.suggestorUid = suggestorUid;
        this.suggestorPackageName = suggestorPackageName;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.wifiConfiguration, flags);
        dest.writeBoolean(this.isAppInteractionRequired);
        dest.writeBoolean(this.isUserInteractionRequired);
        dest.writeInt(this.suggestorUid);
        dest.writeString(this.suggestorPackageName);
    }

    public int hashCode() {
        return Objects.hash(this.wifiConfiguration.SSID, this.wifiConfiguration.BSSID, this.wifiConfiguration.allowedKeyManagement, Integer.valueOf(this.suggestorUid), this.suggestorPackageName);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WifiNetworkSuggestion) {
            WifiNetworkSuggestion lhs = (WifiNetworkSuggestion) obj;
            return Objects.equals(this.wifiConfiguration.SSID, lhs.wifiConfiguration.SSID) && Objects.equals(this.wifiConfiguration.BSSID, lhs.wifiConfiguration.BSSID) && Objects.equals(this.wifiConfiguration.allowedKeyManagement, lhs.wifiConfiguration.allowedKeyManagement) && this.suggestorUid == lhs.suggestorUid && TextUtils.equals(this.suggestorPackageName, lhs.suggestorPackageName);
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("WifiNetworkSuggestion [");
        sb.append(", SSID=");
        sb.append(this.wifiConfiguration.SSID);
        sb.append(", BSSID=");
        sb.append(this.wifiConfiguration.BSSID);
        sb.append(", isAppInteractionRequired=");
        sb.append(this.isAppInteractionRequired);
        sb.append(", isUserInteractionRequired=");
        sb.append(this.isUserInteractionRequired);
        sb.append(", suggestorUid=");
        sb.append(this.suggestorUid);
        sb.append(", suggestorPackageName=");
        sb.append(this.suggestorPackageName);
        StringBuilder sb2 = sb.append("]");
        return sb2.toString();
    }
}
