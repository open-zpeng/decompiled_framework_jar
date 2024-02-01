package android.net;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class RouteInfo implements Parcelable {
    public static final Parcelable.Creator<RouteInfo> CREATOR = new Parcelable.Creator<RouteInfo>() { // from class: android.net.RouteInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteInfo createFromParcel(Parcel in) {
            IpPrefix dest = (IpPrefix) in.readParcelable(null);
            InetAddress gateway = null;
            byte[] addr = in.createByteArray();
            try {
                gateway = InetAddress.getByAddress(addr);
            } catch (UnknownHostException e) {
            }
            String iface = in.readString();
            int type = in.readInt();
            return new RouteInfo(dest, gateway, iface, type);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteInfo[] newArray(int size) {
            return new RouteInfo[size];
        }
    };
    public static final int RTN_THROW = 9;
    public static final int RTN_UNICAST = 1;
    public static final int RTN_UNREACHABLE = 7;
    private final IpPrefix mDestination;
    public protected final InetAddress mGateway;
    private final boolean mHasGateway;
    private final String mInterface;
    public protected final boolean mIsHost;
    private final int mType;

    public synchronized RouteInfo(IpPrefix destination, InetAddress gateway, String iface, int type) {
        if (type != 1 && type != 7 && type != 9) {
            throw new IllegalArgumentException("Unknown route type " + type);
        }
        if (destination == null) {
            if (gateway != null) {
                if (gateway instanceof Inet4Address) {
                    destination = new IpPrefix(Inet4Address.ANY, 0);
                } else {
                    destination = new IpPrefix(Inet6Address.ANY, 0);
                }
            } else {
                throw new IllegalArgumentException("Invalid arguments passed in: " + gateway + "," + destination);
            }
        }
        if (gateway == null) {
            if (destination.getAddress() instanceof Inet4Address) {
                gateway = Inet4Address.ANY;
            } else {
                gateway = Inet6Address.ANY;
            }
        }
        this.mHasGateway = true ^ gateway.isAnyLocalAddress();
        if (((destination.getAddress() instanceof Inet4Address) && !(gateway instanceof Inet4Address)) || ((destination.getAddress() instanceof Inet6Address) && !(gateway instanceof Inet6Address))) {
            throw new IllegalArgumentException("address family mismatch in RouteInfo constructor");
        }
        this.mDestination = destination;
        this.mGateway = gateway;
        this.mInterface = iface;
        this.mType = type;
        this.mIsHost = isHost();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RouteInfo(IpPrefix destination, InetAddress gateway, String iface) {
        this(destination, gateway, iface, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RouteInfo(LinkAddress destination, InetAddress gateway, String iface) {
        this(destination == null ? null : new IpPrefix(destination.getAddress(), destination.getPrefixLength()), gateway, iface);
    }

    public synchronized RouteInfo(IpPrefix destination, InetAddress gateway) {
        this(destination, gateway, (String) null);
    }

    private protected RouteInfo(LinkAddress destination, InetAddress gateway) {
        this(destination, gateway, (String) null);
    }

    private protected RouteInfo(InetAddress gateway) {
        this((IpPrefix) null, gateway, (String) null);
    }

    public synchronized RouteInfo(IpPrefix destination) {
        this(destination, (InetAddress) null, (String) null);
    }

    public synchronized RouteInfo(LinkAddress destination) {
        this(destination, (InetAddress) null, (String) null);
    }

    public synchronized RouteInfo(IpPrefix destination, int type) {
        this(destination, null, null, type);
    }

    public static synchronized RouteInfo makeHostRoute(InetAddress host, String iface) {
        return makeHostRoute(host, null, iface);
    }

    public static synchronized RouteInfo makeHostRoute(InetAddress host, InetAddress gateway, String iface) {
        if (host == null) {
            return null;
        }
        if (host instanceof Inet4Address) {
            return new RouteInfo(new IpPrefix(host, 32), gateway, iface);
        }
        return new RouteInfo(new IpPrefix(host, 128), gateway, iface);
    }

    public protected boolean isHost() {
        return ((this.mDestination.getAddress() instanceof Inet4Address) && this.mDestination.getPrefixLength() == 32) || ((this.mDestination.getAddress() instanceof Inet6Address) && this.mDestination.getPrefixLength() == 128);
    }

    public IpPrefix getDestination() {
        return this.mDestination;
    }

    public synchronized LinkAddress getDestinationLinkAddress() {
        return new LinkAddress(this.mDestination.getAddress(), this.mDestination.getPrefixLength());
    }

    public InetAddress getGateway() {
        return this.mGateway;
    }

    public String getInterface() {
        return this.mInterface;
    }

    public synchronized int getType() {
        return this.mType;
    }

    public boolean isDefaultRoute() {
        return this.mType == 1 && this.mDestination.getPrefixLength() == 0;
    }

    public synchronized boolean isIPv4Default() {
        return isDefaultRoute() && (this.mDestination.getAddress() instanceof Inet4Address);
    }

    public synchronized boolean isIPv6Default() {
        return isDefaultRoute() && (this.mDestination.getAddress() instanceof Inet6Address);
    }

    public synchronized boolean isHostRoute() {
        return this.mIsHost;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasGateway() {
        return this.mHasGateway;
    }

    public boolean matches(InetAddress destination) {
        return this.mDestination.contains(destination);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static RouteInfo selectBestRoute(Collection<RouteInfo> routes, InetAddress dest) {
        if (routes == null || dest == null) {
            return null;
        }
        RouteInfo bestRoute = null;
        for (RouteInfo route : routes) {
            if (NetworkUtils.addressTypeMatches(route.mDestination.getAddress(), dest) && (bestRoute == null || bestRoute.mDestination.getPrefixLength() < route.mDestination.getPrefixLength())) {
                if (route.matches(dest)) {
                    bestRoute = route;
                }
            }
        }
        return bestRoute;
    }

    public String toString() {
        String val = this.mDestination != null ? this.mDestination.toString() : "";
        if (this.mType == 7) {
            return val + " unreachable";
        } else if (this.mType == 9) {
            return val + " throw";
        } else {
            String val2 = val + " ->";
            if (this.mGateway != null) {
                val2 = val2 + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mGateway.getHostAddress();
            }
            if (this.mInterface != null) {
                val2 = val2 + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mInterface;
            }
            if (this.mType != 1) {
                return val2 + " unknown type " + this.mType;
            }
            return val2;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RouteInfo) {
            RouteInfo target = (RouteInfo) obj;
            return Objects.equals(this.mDestination, target.getDestination()) && Objects.equals(this.mGateway, target.getGateway()) && Objects.equals(this.mInterface, target.getInterface()) && this.mType == target.getType();
        }
        return false;
    }

    public int hashCode() {
        return (this.mDestination.hashCode() * 41) + (this.mGateway == null ? 0 : this.mGateway.hashCode() * 47) + (this.mInterface != null ? this.mInterface.hashCode() * 67 : 0) + (this.mType * 71);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mDestination, flags);
        byte[] gatewayBytes = this.mGateway == null ? null : this.mGateway.getAddress();
        dest.writeByteArray(gatewayBytes);
        dest.writeString(this.mInterface);
        dest.writeInt(this.mType);
    }
}
