package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.net.Inet4Address;
import java.util.Objects;
/* loaded from: classes2.dex */
public class DhcpResults extends StaticIpConfiguration {
    public static final Parcelable.Creator<DhcpResults> CREATOR = new Parcelable.Creator<DhcpResults>() { // from class: android.net.DhcpResults.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DhcpResults createFromParcel(Parcel in) {
            DhcpResults dhcpResults = new DhcpResults();
            DhcpResults.readFromParcel(dhcpResults, in);
            return dhcpResults;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DhcpResults[] newArray(int size) {
            return new DhcpResults[size];
        }
    };
    private static final String TAG = "DhcpResults";
    private protected int leaseDuration;
    private protected int mtu;
    private protected Inet4Address serverAddress;
    private protected String vendorInfo;

    private protected DhcpResults() {
    }

    private protected DhcpResults(StaticIpConfiguration source) {
        super(source);
    }

    private protected DhcpResults(DhcpResults source) {
        super(source);
        if (source != null) {
            this.serverAddress = source.serverAddress;
            this.vendorInfo = source.vendorInfo;
            this.leaseDuration = source.leaseDuration;
            this.mtu = source.mtu;
        }
    }

    public synchronized boolean hasMeteredHint() {
        if (this.vendorInfo != null) {
            return this.vendorInfo.contains("ANDROID_METERED");
        }
        return false;
    }

    @Override // android.net.StaticIpConfiguration
    public synchronized void clear() {
        super.clear();
        this.vendorInfo = null;
        this.leaseDuration = 0;
        this.mtu = 0;
    }

    @Override // android.net.StaticIpConfiguration
    public String toString() {
        StringBuffer str = new StringBuffer(super.toString());
        str.append(" DHCP server ");
        str.append(this.serverAddress);
        str.append(" Vendor info ");
        str.append(this.vendorInfo);
        str.append(" lease ");
        str.append(this.leaseDuration);
        str.append(" seconds");
        if (this.mtu != 0) {
            str.append(" MTU ");
            str.append(this.mtu);
        }
        return str.toString();
    }

    @Override // android.net.StaticIpConfiguration
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DhcpResults) {
            DhcpResults target = (DhcpResults) obj;
            return super.equals((StaticIpConfiguration) obj) && Objects.equals(this.serverAddress, target.serverAddress) && Objects.equals(this.vendorInfo, target.vendorInfo) && this.leaseDuration == target.leaseDuration && this.mtu == target.mtu;
        }
        return false;
    }

    @Override // android.net.StaticIpConfiguration, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.leaseDuration);
        dest.writeInt(this.mtu);
        NetworkUtils.parcelInetAddress(dest, this.serverAddress, flags);
        dest.writeString(this.vendorInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void readFromParcel(DhcpResults dhcpResults, Parcel in) {
        StaticIpConfiguration.readFromParcel(dhcpResults, in);
        dhcpResults.leaseDuration = in.readInt();
        dhcpResults.mtu = in.readInt();
        dhcpResults.serverAddress = (Inet4Address) NetworkUtils.unparcelInetAddress(in);
        dhcpResults.vendorInfo = in.readString();
    }

    public synchronized boolean setIpAddress(String addrString, int prefixLength) {
        try {
            Inet4Address addr = (Inet4Address) NetworkUtils.numericToInetAddress(addrString);
            this.ipAddress = new LinkAddress(addr, prefixLength);
            return false;
        } catch (ClassCastException | IllegalArgumentException e) {
            Log.e(TAG, "setIpAddress failed with addrString " + addrString + "/" + prefixLength);
            return true;
        }
    }

    public synchronized boolean setGateway(String addrString) {
        try {
            this.gateway = NetworkUtils.numericToInetAddress(addrString);
            return false;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "setGateway failed with addrString " + addrString);
            return true;
        }
    }

    public synchronized boolean addDns(String addrString) {
        if (!TextUtils.isEmpty(addrString)) {
            try {
                this.dnsServers.add(NetworkUtils.numericToInetAddress(addrString));
                return false;
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "addDns failed with addrString " + addrString);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean setServerAddress(String addrString) {
        try {
            this.serverAddress = (Inet4Address) NetworkUtils.numericToInetAddress(addrString);
            return false;
        } catch (ClassCastException | IllegalArgumentException e) {
            Log.e(TAG, "setServerAddress failed with addrString " + addrString);
            return true;
        }
    }

    public synchronized void setLeaseDuration(int duration) {
        this.leaseDuration = duration;
    }

    public synchronized void setVendorInfo(String info) {
        this.vendorInfo = info;
    }

    public synchronized void setDomains(String newDomains) {
        this.domains = newDomains;
    }
}
