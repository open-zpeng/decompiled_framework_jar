package android.net.wifi.aware;

import android.net.TransportInfo;
import android.os.Parcel;
import android.os.Parcelable;
import java.net.Inet6Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;

/* loaded from: classes2.dex */
public final class WifiAwareNetworkInfo implements TransportInfo, Parcelable {
    public static final Parcelable.Creator<WifiAwareNetworkInfo> CREATOR = new Parcelable.Creator<WifiAwareNetworkInfo>() { // from class: android.net.wifi.aware.WifiAwareNetworkInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiAwareNetworkInfo createFromParcel(Parcel in) {
            byte[] addr = in.createByteArray();
            String interfaceName = in.readString();
            int port = in.readInt();
            int transportProtocol = in.readInt();
            NetworkInterface ni = null;
            try {
                if (interfaceName != null) {
                    try {
                        ni = NetworkInterface.getByName(interfaceName);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }
                Inet6Address ipv6Addr = Inet6Address.getByAddress((String) null, addr, ni);
                return new WifiAwareNetworkInfo(ipv6Addr, port, transportProtocol);
            } catch (UnknownHostException e2) {
                e2.printStackTrace();
                return null;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiAwareNetworkInfo[] newArray(int size) {
            return new WifiAwareNetworkInfo[size];
        }
    };
    private Inet6Address mIpv6Addr;
    private int mPort;
    private int mTransportProtocol;

    public WifiAwareNetworkInfo(Inet6Address ipv6Addr) {
        this.mPort = 0;
        this.mTransportProtocol = -1;
        this.mIpv6Addr = ipv6Addr;
    }

    public WifiAwareNetworkInfo(Inet6Address ipv6Addr, int port, int transportProtocol) {
        this.mPort = 0;
        this.mTransportProtocol = -1;
        this.mIpv6Addr = ipv6Addr;
        this.mPort = port;
        this.mTransportProtocol = transportProtocol;
    }

    public Inet6Address getPeerIpv6Addr() {
        return this.mIpv6Addr;
    }

    public int getPort() {
        return this.mPort;
    }

    public int getTransportProtocol() {
        return this.mTransportProtocol;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.mIpv6Addr.getAddress());
        NetworkInterface ni = this.mIpv6Addr.getScopedInterface();
        dest.writeString(ni == null ? null : ni.getName());
        dest.writeInt(this.mPort);
        dest.writeInt(this.mTransportProtocol);
    }

    public String toString() {
        return "AwareNetworkInfo: IPv6=" + this.mIpv6Addr + ", port=" + this.mPort + ", transportProtocol=" + this.mTransportProtocol;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WifiAwareNetworkInfo) {
            WifiAwareNetworkInfo lhs = (WifiAwareNetworkInfo) obj;
            return Objects.equals(this.mIpv6Addr, lhs.mIpv6Addr) && this.mPort == lhs.mPort && this.mTransportProtocol == lhs.mTransportProtocol;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mIpv6Addr, Integer.valueOf(this.mPort), Integer.valueOf(this.mTransportProtocol));
    }
}
