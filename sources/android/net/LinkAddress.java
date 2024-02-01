package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.system.OsConstants;
import android.util.Pair;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.UnknownHostException;
/* loaded from: classes2.dex */
public class LinkAddress implements Parcelable {
    public static final Parcelable.Creator<LinkAddress> CREATOR = new Parcelable.Creator<LinkAddress>() { // from class: android.net.LinkAddress.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LinkAddress createFromParcel(Parcel in) {
            InetAddress address = null;
            try {
                address = InetAddress.getByAddress(in.createByteArray());
            } catch (UnknownHostException e) {
            }
            int prefixLength = in.readInt();
            int flags = in.readInt();
            int scope = in.readInt();
            return new LinkAddress(address, prefixLength, flags, scope);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LinkAddress[] newArray(int size) {
            return new LinkAddress[size];
        }
    };
    public protected InetAddress address;
    private int flags;
    public protected int prefixLength;
    private int scope;

    private static synchronized int scopeForUnicastAddress(InetAddress addr) {
        if (addr.isAnyLocalAddress()) {
            return OsConstants.RT_SCOPE_HOST;
        }
        if (addr.isLoopbackAddress() || addr.isLinkLocalAddress()) {
            return OsConstants.RT_SCOPE_LINK;
        }
        if (!(addr instanceof Inet4Address) && addr.isSiteLocalAddress()) {
            return OsConstants.RT_SCOPE_SITE;
        }
        return OsConstants.RT_SCOPE_UNIVERSE;
    }

    private synchronized boolean isIPv6ULA() {
        if (isIPv6()) {
            byte[] bytes = this.address.getAddress();
            return (bytes[0] & (-2)) == -4;
        }
        return false;
    }

    private protected boolean isIPv6() {
        return this.address instanceof Inet6Address;
    }

    public synchronized boolean isIPv4() {
        return this.address instanceof Inet4Address;
    }

    private synchronized void init(InetAddress address, int prefixLength, int flags, int scope) {
        if (address == null || address.isMulticastAddress() || prefixLength < 0 || (((address instanceof Inet4Address) && prefixLength > 32) || prefixLength > 128)) {
            throw new IllegalArgumentException("Bad LinkAddress params " + address + "/" + prefixLength);
        }
        this.address = address;
        this.prefixLength = prefixLength;
        this.flags = flags;
        this.scope = scope;
    }

    public synchronized LinkAddress(InetAddress address, int prefixLength, int flags, int scope) {
        init(address, prefixLength, flags, scope);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LinkAddress(InetAddress address, int prefixLength) {
        this(address, prefixLength, 0, 0);
        this.scope = scopeForUnicastAddress(address);
    }

    public synchronized LinkAddress(InterfaceAddress interfaceAddress) {
        this(interfaceAddress.getAddress(), interfaceAddress.getNetworkPrefixLength());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LinkAddress(String address) {
        this(address, 0, 0);
        this.scope = scopeForUnicastAddress(this.address);
    }

    public synchronized LinkAddress(String address, int flags, int scope) {
        Pair<InetAddress, Integer> ipAndMask = NetworkUtils.parseIpAndMask(address);
        init(ipAndMask.first, ipAndMask.second.intValue(), flags, scope);
    }

    public String toString() {
        return this.address.getHostAddress() + "/" + this.prefixLength;
    }

    public boolean equals(Object obj) {
        if (obj instanceof LinkAddress) {
            LinkAddress linkAddress = (LinkAddress) obj;
            return this.address.equals(linkAddress.address) && this.prefixLength == linkAddress.prefixLength && this.flags == linkAddress.flags && this.scope == linkAddress.scope;
        }
        return false;
    }

    public int hashCode() {
        return this.address.hashCode() + (11 * this.prefixLength) + (19 * this.flags) + (43 * this.scope);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSameAddressAs(LinkAddress other) {
        return this.address.equals(other.address) && this.prefixLength == other.prefixLength;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public int getPrefixLength() {
        return this.prefixLength;
    }

    private protected int getNetworkPrefixLength() {
        return getPrefixLength();
    }

    public int getFlags() {
        return this.flags;
    }

    public int getScope() {
        return this.scope;
    }

    public synchronized boolean isGlobalPreferred() {
        return this.scope == OsConstants.RT_SCOPE_UNIVERSE && !isIPv6ULA() && ((long) (this.flags & (OsConstants.IFA_F_DADFAILED | OsConstants.IFA_F_DEPRECATED))) == 0 && (((long) (this.flags & OsConstants.IFA_F_TENTATIVE)) == 0 || ((long) (this.flags & OsConstants.IFA_F_OPTIMISTIC)) != 0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.address.getAddress());
        dest.writeInt(this.prefixLength);
        dest.writeInt(this.flags);
        dest.writeInt(this.scope);
    }
}
