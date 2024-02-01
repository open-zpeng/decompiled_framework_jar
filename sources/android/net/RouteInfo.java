package android.net;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SmsManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
    @SystemApi
    public static final int RTN_THROW = 9;
    @SystemApi
    public static final int RTN_UNICAST = 1;
    @SystemApi
    public static final int RTN_UNREACHABLE = 7;
    private final IpPrefix mDestination;
    @UnsupportedAppUsage
    private final InetAddress mGateway;
    private final boolean mHasGateway;
    private final String mInterface;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final boolean mIsHost;
    private final int mType;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface RouteType {
    }

    @SystemApi
    public RouteInfo(IpPrefix destination, InetAddress gateway, String iface, int type) {
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
                throw new IllegalArgumentException("Invalid arguments passed in: " + gateway + SmsManager.REGEX_PREFIX_DELIMITER + destination);
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

    @UnsupportedAppUsage
    public RouteInfo(IpPrefix destination, InetAddress gateway, String iface) {
        this(destination, gateway, iface, 1);
    }

    @UnsupportedAppUsage
    public RouteInfo(LinkAddress destination, InetAddress gateway, String iface) {
        this(destination == null ? null : new IpPrefix(destination.getAddress(), destination.getPrefixLength()), gateway, iface);
    }

    public RouteInfo(IpPrefix destination, InetAddress gateway) {
        this(destination, gateway, (String) null);
    }

    @UnsupportedAppUsage
    public RouteInfo(LinkAddress destination, InetAddress gateway) {
        this(destination, gateway, (String) null);
    }

    @UnsupportedAppUsage
    public RouteInfo(InetAddress gateway) {
        this((IpPrefix) null, gateway, (String) null);
    }

    public RouteInfo(IpPrefix destination) {
        this(destination, (InetAddress) null, (String) null);
    }

    public RouteInfo(LinkAddress destination) {
        this(destination, (InetAddress) null, (String) null);
    }

    public RouteInfo(IpPrefix destination, int type) {
        this(destination, null, null, type);
    }

    public static RouteInfo makeHostRoute(InetAddress host, String iface) {
        return makeHostRoute(host, null, iface);
    }

    public static RouteInfo makeHostRoute(InetAddress host, InetAddress gateway, String iface) {
        if (host == null) {
            return null;
        }
        if (host instanceof Inet4Address) {
            return new RouteInfo(new IpPrefix(host, 32), gateway, iface);
        }
        return new RouteInfo(new IpPrefix(host, 128), gateway, iface);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private boolean isHost() {
        return ((this.mDestination.getAddress() instanceof Inet4Address) && this.mDestination.getPrefixLength() == 32) || ((this.mDestination.getAddress() instanceof Inet6Address) && this.mDestination.getPrefixLength() == 128);
    }

    public IpPrefix getDestination() {
        return this.mDestination;
    }

    public LinkAddress getDestinationLinkAddress() {
        return new LinkAddress(this.mDestination.getAddress(), this.mDestination.getPrefixLength());
    }

    public InetAddress getGateway() {
        return this.mGateway;
    }

    public String getInterface() {
        return this.mInterface;
    }

    @SystemApi
    public int getType() {
        return this.mType;
    }

    public boolean isDefaultRoute() {
        return this.mType == 1 && this.mDestination.getPrefixLength() == 0;
    }

    public boolean isIPv4Default() {
        return isDefaultRoute() && (this.mDestination.getAddress() instanceof Inet4Address);
    }

    public boolean isIPv6Default() {
        return isDefaultRoute() && (this.mDestination.getAddress() instanceof Inet6Address);
    }

    public boolean isHostRoute() {
        return this.mIsHost;
    }

    public boolean hasGateway() {
        return this.mHasGateway;
    }

    public boolean matches(InetAddress destination) {
        return this.mDestination.contains(destination);
    }

    @UnsupportedAppUsage
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
        IpPrefix ipPrefix = this.mDestination;
        String val = ipPrefix != null ? ipPrefix.toString() : "";
        int i = this.mType;
        if (i == 7) {
            return val + " unreachable";
        } else if (i == 9) {
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
        int hashCode = this.mDestination.hashCode() * 41;
        InetAddress inetAddress = this.mGateway;
        int hashCode2 = hashCode + (inetAddress == null ? 0 : inetAddress.hashCode() * 47);
        String str = this.mInterface;
        return hashCode2 + (str != null ? str.hashCode() * 67 : 0) + (this.mType * 71);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mDestination, flags);
        InetAddress inetAddress = this.mGateway;
        byte[] gatewayBytes = inetAddress == null ? null : inetAddress.getAddress();
        dest.writeByteArray(gatewayBytes);
        dest.writeString(this.mInterface);
        dest.writeInt(this.mType);
    }
}
