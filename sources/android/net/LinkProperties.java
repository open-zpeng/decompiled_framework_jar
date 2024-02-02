package android.net;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class LinkProperties implements Parcelable {
    public static final Parcelable.Creator<LinkProperties> CREATOR = new Parcelable.Creator<LinkProperties>() { // from class: android.net.LinkProperties.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LinkProperties createFromParcel(Parcel in) {
            LinkProperties netProp = new LinkProperties();
            String iface = in.readString();
            if (iface != null) {
                netProp.setInterfaceName(iface);
            }
            int addressCount = in.readInt();
            for (int i = 0; i < addressCount; i++) {
                netProp.addLinkAddress((LinkAddress) in.readParcelable(null));
            }
            int addressCount2 = in.readInt();
            for (int i2 = 0; i2 < addressCount2; i2++) {
                try {
                    netProp.addDnsServer(InetAddress.getByAddress(in.createByteArray()));
                } catch (UnknownHostException e) {
                }
            }
            int addressCount3 = in.readInt();
            for (int i3 = 0; i3 < addressCount3; i3++) {
                try {
                    netProp.addValidatedPrivateDnsServer(InetAddress.getByAddress(in.createByteArray()));
                } catch (UnknownHostException e2) {
                }
            }
            netProp.setUsePrivateDns(in.readBoolean());
            netProp.setPrivateDnsServerName(in.readString());
            netProp.setDomains(in.readString());
            netProp.setMtu(in.readInt());
            netProp.setTcpBufferSizes(in.readString());
            int addressCount4 = in.readInt();
            for (int i4 = 0; i4 < addressCount4; i4++) {
                netProp.addRoute((RouteInfo) in.readParcelable(null));
            }
            int i5 = in.readByte();
            if (i5 == 1) {
                netProp.setHttpProxy((ProxyInfo) in.readParcelable(null));
            }
            ArrayList<LinkProperties> stackedLinks = new ArrayList<>();
            in.readList(stackedLinks, LinkProperties.class.getClassLoader());
            Iterator<LinkProperties> it = stackedLinks.iterator();
            while (it.hasNext()) {
                LinkProperties stackedLink = it.next();
                netProp.addStackedLink(stackedLink);
            }
            return netProp;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LinkProperties[] newArray(int size) {
            return new LinkProperties[size];
        }
    };
    private static final int MAX_MTU = 10000;
    private static final int MIN_MTU = 68;
    private static final int MIN_MTU_V6 = 1280;
    private String mDomains;
    private ProxyInfo mHttpProxy;
    public protected String mIfaceName;
    private int mMtu;
    private String mPrivateDnsServerName;
    private String mTcpBufferSizes;
    private boolean mUsePrivateDns;
    private ArrayList<LinkAddress> mLinkAddresses = new ArrayList<>();
    private ArrayList<InetAddress> mDnses = new ArrayList<>();
    private ArrayList<InetAddress> mValidatedPrivateDnses = new ArrayList<>();
    private ArrayList<RouteInfo> mRoutes = new ArrayList<>();
    private Hashtable<String, LinkProperties> mStackedLinks = new Hashtable<>();

    /* loaded from: classes2.dex */
    public enum ProvisioningChange {
        STILL_NOT_PROVISIONED,
        LOST_PROVISIONING,
        GAINED_PROVISIONING,
        STILL_PROVISIONED
    }

    /* loaded from: classes2.dex */
    public static class CompareResult<T> {
        public final List<T> removed = new ArrayList();
        public final List<T> added = new ArrayList();

        public synchronized CompareResult() {
        }

        public synchronized CompareResult(Collection<T> oldItems, Collection<T> newItems) {
            if (oldItems != null) {
                this.removed.addAll(oldItems);
            }
            if (newItems != null) {
                for (T newItem : newItems) {
                    if (!this.removed.remove(newItem)) {
                        this.added.add(newItem);
                    }
                }
            }
        }

        public String toString() {
            String retVal = "removed=[";
            for (T addr : this.removed) {
                retVal = retVal + addr.toString() + ",";
            }
            String retVal2 = retVal + "] added=[";
            for (T addr2 : this.added) {
                retVal2 = retVal2 + addr2.toString() + ",";
            }
            return retVal2 + "]";
        }
    }

    private protected static ProvisioningChange compareProvisioning(LinkProperties before, LinkProperties after) {
        if (before.isProvisioned() && after.isProvisioned()) {
            if ((before.isIPv4Provisioned() && !after.isIPv4Provisioned()) || (before.isIPv6Provisioned() && !after.isIPv6Provisioned())) {
                return ProvisioningChange.LOST_PROVISIONING;
            }
            return ProvisioningChange.STILL_PROVISIONED;
        } else if (before.isProvisioned() && !after.isProvisioned()) {
            return ProvisioningChange.LOST_PROVISIONING;
        } else {
            if (!before.isProvisioned() && after.isProvisioned()) {
                return ProvisioningChange.GAINED_PROVISIONING;
            }
            return ProvisioningChange.STILL_NOT_PROVISIONED;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LinkProperties() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LinkProperties(LinkProperties source) {
        if (source != null) {
            this.mIfaceName = source.getInterfaceName();
            for (LinkAddress l : source.getLinkAddresses()) {
                this.mLinkAddresses.add(l);
            }
            for (InetAddress i : source.getDnsServers()) {
                this.mDnses.add(i);
            }
            for (InetAddress i2 : source.getValidatedPrivateDnsServers()) {
                this.mValidatedPrivateDnses.add(i2);
            }
            this.mUsePrivateDns = source.mUsePrivateDns;
            this.mPrivateDnsServerName = source.mPrivateDnsServerName;
            this.mDomains = source.getDomains();
            for (RouteInfo r : source.getRoutes()) {
                this.mRoutes.add(r);
            }
            this.mHttpProxy = source.getHttpProxy() == null ? null : new ProxyInfo(source.getHttpProxy());
            for (LinkProperties l2 : source.mStackedLinks.values()) {
                addStackedLink(l2);
            }
            setMtu(source.getMtu());
            this.mTcpBufferSizes = source.mTcpBufferSizes;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInterfaceName(String iface) {
        this.mIfaceName = iface;
        ArrayList<RouteInfo> newRoutes = new ArrayList<>(this.mRoutes.size());
        Iterator<RouteInfo> it = this.mRoutes.iterator();
        while (it.hasNext()) {
            RouteInfo route = it.next();
            newRoutes.add(routeWithInterface(route));
        }
        this.mRoutes = newRoutes;
    }

    public String getInterfaceName() {
        return this.mIfaceName;
    }

    private protected List<String> getAllInterfaceNames() {
        List<String> interfaceNames = new ArrayList<>(this.mStackedLinks.size() + 1);
        if (this.mIfaceName != null) {
            interfaceNames.add(new String(this.mIfaceName));
        }
        for (LinkProperties stacked : this.mStackedLinks.values()) {
            interfaceNames.addAll(stacked.getAllInterfaceNames());
        }
        return interfaceNames;
    }

    private protected List<InetAddress> getAddresses() {
        List<InetAddress> addresses = new ArrayList<>();
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            LinkAddress linkAddress = it.next();
            addresses.add(linkAddress.getAddress());
        }
        return Collections.unmodifiableList(addresses);
    }

    private protected List<InetAddress> getAllAddresses() {
        List<InetAddress> addresses = new ArrayList<>();
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            LinkAddress linkAddress = it.next();
            addresses.add(linkAddress.getAddress());
        }
        for (LinkProperties stacked : this.mStackedLinks.values()) {
            addresses.addAll(stacked.getAllAddresses());
        }
        return addresses;
    }

    private synchronized int findLinkAddressIndex(LinkAddress address) {
        for (int i = 0; i < this.mLinkAddresses.size(); i++) {
            if (this.mLinkAddresses.get(i).isSameAddressAs(address)) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean addLinkAddress(LinkAddress address) {
        if (address == null) {
            return false;
        }
        int i = findLinkAddressIndex(address);
        if (i < 0) {
            this.mLinkAddresses.add(address);
            return true;
        } else if (this.mLinkAddresses.get(i).equals(address)) {
            return false;
        } else {
            this.mLinkAddresses.set(i, address);
            return true;
        }
    }

    public synchronized boolean removeLinkAddress(LinkAddress toRemove) {
        int i = findLinkAddressIndex(toRemove);
        if (i >= 0) {
            this.mLinkAddresses.remove(i);
            return true;
        }
        return false;
    }

    public List<LinkAddress> getLinkAddresses() {
        return Collections.unmodifiableList(this.mLinkAddresses);
    }

    private protected List<LinkAddress> getAllLinkAddresses() {
        List<LinkAddress> addresses = new ArrayList<>();
        addresses.addAll(this.mLinkAddresses);
        for (LinkProperties stacked : this.mStackedLinks.values()) {
            addresses.addAll(stacked.getAllLinkAddresses());
        }
        return addresses;
    }

    private protected void setLinkAddresses(Collection<LinkAddress> addresses) {
        this.mLinkAddresses.clear();
        for (LinkAddress address : addresses) {
            addLinkAddress(address);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean addDnsServer(InetAddress dnsServer) {
        if (dnsServer != null && !this.mDnses.contains(dnsServer)) {
            this.mDnses.add(dnsServer);
            return true;
        }
        return false;
    }

    private protected boolean removeDnsServer(InetAddress dnsServer) {
        if (dnsServer != null) {
            return this.mDnses.remove(dnsServer);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDnsServers(Collection<InetAddress> dnsServers) {
        this.mDnses.clear();
        for (InetAddress dnsServer : dnsServers) {
            addDnsServer(dnsServer);
        }
    }

    public List<InetAddress> getDnsServers() {
        return Collections.unmodifiableList(this.mDnses);
    }

    public synchronized void setUsePrivateDns(boolean usePrivateDns) {
        this.mUsePrivateDns = usePrivateDns;
    }

    public boolean isPrivateDnsActive() {
        return this.mUsePrivateDns;
    }

    public synchronized void setPrivateDnsServerName(String privateDnsServerName) {
        this.mPrivateDnsServerName = privateDnsServerName;
    }

    public String getPrivateDnsServerName() {
        return this.mPrivateDnsServerName;
    }

    public synchronized boolean addValidatedPrivateDnsServer(InetAddress dnsServer) {
        if (dnsServer != null && !this.mValidatedPrivateDnses.contains(dnsServer)) {
            this.mValidatedPrivateDnses.add(dnsServer);
            return true;
        }
        return false;
    }

    public synchronized boolean removeValidatedPrivateDnsServer(InetAddress dnsServer) {
        if (dnsServer != null) {
            return this.mValidatedPrivateDnses.remove(dnsServer);
        }
        return false;
    }

    public synchronized void setValidatedPrivateDnsServers(Collection<InetAddress> dnsServers) {
        this.mValidatedPrivateDnses.clear();
        for (InetAddress dnsServer : dnsServers) {
            addValidatedPrivateDnsServer(dnsServer);
        }
    }

    public synchronized List<InetAddress> getValidatedPrivateDnsServers() {
        return Collections.unmodifiableList(this.mValidatedPrivateDnses);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDomains(String domains) {
        this.mDomains = domains;
    }

    public String getDomains() {
        return this.mDomains;
    }

    private protected void setMtu(int mtu) {
        this.mMtu = mtu;
    }

    private protected int getMtu() {
        return this.mMtu;
    }

    private protected void setTcpBufferSizes(String tcpBufferSizes) {
        this.mTcpBufferSizes = tcpBufferSizes;
    }

    private protected String getTcpBufferSizes() {
        return this.mTcpBufferSizes;
    }

    private synchronized RouteInfo routeWithInterface(RouteInfo route) {
        return new RouteInfo(route.getDestination(), route.getGateway(), this.mIfaceName, route.getType());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean addRoute(RouteInfo route) {
        if (route != null) {
            String routeIface = route.getInterface();
            if (routeIface != null && !routeIface.equals(this.mIfaceName)) {
                throw new IllegalArgumentException("Route added with non-matching interface: " + routeIface + " vs. " + this.mIfaceName);
            }
            RouteInfo route2 = routeWithInterface(route);
            if (!this.mRoutes.contains(route2)) {
                this.mRoutes.add(route2);
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeRoute(RouteInfo route) {
        return route != null && Objects.equals(this.mIfaceName, route.getInterface()) && this.mRoutes.remove(route);
    }

    public List<RouteInfo> getRoutes() {
        return Collections.unmodifiableList(this.mRoutes);
    }

    public synchronized void ensureDirectlyConnectedRoutes() {
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            LinkAddress addr = it.next();
            addRoute(new RouteInfo(addr, (InetAddress) null, this.mIfaceName));
        }
    }

    private protected List<RouteInfo> getAllRoutes() {
        List<RouteInfo> routes = new ArrayList<>();
        routes.addAll(this.mRoutes);
        for (LinkProperties stacked : this.mStackedLinks.values()) {
            routes.addAll(stacked.getAllRoutes());
        }
        return routes;
    }

    private protected void setHttpProxy(ProxyInfo proxy) {
        this.mHttpProxy = proxy;
    }

    public ProxyInfo getHttpProxy() {
        return this.mHttpProxy;
    }

    private protected boolean addStackedLink(LinkProperties link) {
        if (link != null && link.getInterfaceName() != null) {
            this.mStackedLinks.put(link.getInterfaceName(), link);
            return true;
        }
        return false;
    }

    public synchronized boolean removeStackedLink(String iface) {
        if (iface == null) {
            return false;
        }
        LinkProperties removed = this.mStackedLinks.remove(iface);
        return removed != null;
    }

    private protected List<LinkProperties> getStackedLinks() {
        if (this.mStackedLinks.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<LinkProperties> stacked = new ArrayList<>();
        for (LinkProperties link : this.mStackedLinks.values()) {
            stacked.add(new LinkProperties(link));
        }
        return Collections.unmodifiableList(stacked);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clear() {
        this.mIfaceName = null;
        this.mLinkAddresses.clear();
        this.mDnses.clear();
        this.mUsePrivateDns = false;
        this.mPrivateDnsServerName = null;
        this.mDomains = null;
        this.mRoutes.clear();
        this.mHttpProxy = null;
        this.mStackedLinks.clear();
        this.mMtu = 0;
        this.mTcpBufferSizes = null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        String ifaceName = this.mIfaceName == null ? "" : "InterfaceName: " + this.mIfaceName + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        String linkAddresses = "LinkAddresses: [";
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            LinkAddress addr = it.next();
            linkAddresses = linkAddresses + addr.toString() + ",";
        }
        String linkAddresses2 = linkAddresses + "] ";
        String dns = "DnsAddresses: [";
        Iterator<InetAddress> it2 = this.mDnses.iterator();
        while (it2.hasNext()) {
            InetAddress addr2 = it2.next();
            dns = dns + addr2.getHostAddress() + ",";
        }
        String dns2 = dns + "] ";
        String usePrivateDns = "UsePrivateDns: " + this.mUsePrivateDns + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        String privateDnsServerName = "PrivateDnsServerName: " + this.mPrivateDnsServerName + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        if (!this.mValidatedPrivateDnses.isEmpty()) {
            String validatedPrivateDns = "ValidatedPrivateDnsAddresses: [";
            Iterator<InetAddress> it3 = this.mValidatedPrivateDnses.iterator();
            while (it3.hasNext()) {
                InetAddress addr3 = it3.next();
                validatedPrivateDns = validatedPrivateDns + addr3.getHostAddress() + ",";
            }
            String str = validatedPrivateDns + "] ";
        }
        String domainName = "Domains: " + this.mDomains;
        String mtu = " MTU: " + this.mMtu;
        String tcpBuffSizes = this.mTcpBufferSizes != null ? " TcpBufferSizes: " + this.mTcpBufferSizes : "";
        String routes = " Routes: [";
        Iterator<RouteInfo> it4 = this.mRoutes.iterator();
        while (it4.hasNext()) {
            RouteInfo route = it4.next();
            routes = routes + route.toString() + ",";
        }
        String routes2 = routes + "] ";
        String proxy = this.mHttpProxy == null ? "" : " HttpProxy: " + this.mHttpProxy.toString() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        String stacked = "";
        if (this.mStackedLinks.values().size() > 0) {
            String stacked2 = " Stacked: [";
            for (LinkProperties link : this.mStackedLinks.values()) {
                stacked2 = stacked2 + " [" + link.toString() + " ],";
            }
            stacked = stacked2 + "] ";
        }
        return "{" + ifaceName + linkAddresses2 + routes2 + dns2 + usePrivateDns + privateDnsServerName + domainName + mtu + tcpBuffSizes + proxy + stacked + "}";
    }

    private protected boolean hasIPv4Address() {
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            LinkAddress address = it.next();
            if (address.getAddress() instanceof Inet4Address) {
                return true;
            }
        }
        return false;
    }

    private synchronized boolean hasIPv4AddressOnInterface(String iface) {
        return (Objects.equals(iface, this.mIfaceName) && hasIPv4Address()) || (iface != null && this.mStackedLinks.containsKey(iface) && this.mStackedLinks.get(iface).hasIPv4Address());
    }

    private protected boolean hasGlobalIPv6Address() {
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            LinkAddress address = it.next();
            if ((address.getAddress() instanceof Inet6Address) && address.isGlobalPreferred()) {
                return true;
            }
        }
        return false;
    }

    private protected boolean hasIPv4DefaultRoute() {
        Iterator<RouteInfo> it = this.mRoutes.iterator();
        while (it.hasNext()) {
            RouteInfo r = it.next();
            if (r.isIPv4Default()) {
                return true;
            }
        }
        return false;
    }

    private protected boolean hasIPv6DefaultRoute() {
        Iterator<RouteInfo> it = this.mRoutes.iterator();
        while (it.hasNext()) {
            RouteInfo r = it.next();
            if (r.isIPv6Default()) {
                return true;
            }
        }
        return false;
    }

    private protected boolean hasIPv4DnsServer() {
        Iterator<InetAddress> it = this.mDnses.iterator();
        while (it.hasNext()) {
            InetAddress ia = it.next();
            if (ia instanceof Inet4Address) {
                return true;
            }
        }
        return false;
    }

    private protected boolean hasIPv6DnsServer() {
        Iterator<InetAddress> it = this.mDnses.iterator();
        while (it.hasNext()) {
            InetAddress ia = it.next();
            if (ia instanceof Inet6Address) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean isIPv4Provisioned() {
        return hasIPv4Address() && hasIPv4DefaultRoute() && hasIPv4DnsServer();
    }

    private protected boolean isIPv6Provisioned() {
        return hasGlobalIPv6Address() && hasIPv6DefaultRoute() && hasIPv6DnsServer();
    }

    private protected boolean isProvisioned() {
        return isIPv4Provisioned() || isIPv6Provisioned();
    }

    private protected boolean isReachable(InetAddress ip) {
        List<RouteInfo> allRoutes = getAllRoutes();
        RouteInfo bestRoute = RouteInfo.selectBestRoute(allRoutes, ip);
        if (bestRoute == null) {
            return false;
        }
        if (ip instanceof Inet4Address) {
            return hasIPv4AddressOnInterface(bestRoute.getInterface());
        }
        if (ip instanceof Inet6Address) {
            return ip.isLinkLocalAddress() ? ((Inet6Address) ip).getScopeId() != 0 : !bestRoute.hasGateway() || hasGlobalIPv6Address();
        }
        return false;
    }

    private protected boolean isIdenticalInterfaceName(LinkProperties target) {
        return TextUtils.equals(getInterfaceName(), target.getInterfaceName());
    }

    private protected boolean isIdenticalAddresses(LinkProperties target) {
        Collection<?> targetAddresses = target.getAddresses();
        Collection<InetAddress> sourceAddresses = getAddresses();
        if (sourceAddresses.size() == targetAddresses.size()) {
            return sourceAddresses.containsAll(targetAddresses);
        }
        return false;
    }

    private protected boolean isIdenticalDnses(LinkProperties target) {
        Collection<InetAddress> targetDnses = target.getDnsServers();
        String targetDomains = target.getDomains();
        if (this.mDomains == null) {
            if (targetDomains != null) {
                return false;
            }
        } else if (!this.mDomains.equals(targetDomains)) {
            return false;
        }
        if (this.mDnses.size() == targetDnses.size()) {
            return this.mDnses.containsAll(targetDnses);
        }
        return false;
    }

    public synchronized boolean isIdenticalPrivateDns(LinkProperties target) {
        return isPrivateDnsActive() == target.isPrivateDnsActive() && TextUtils.equals(getPrivateDnsServerName(), target.getPrivateDnsServerName());
    }

    public synchronized boolean isIdenticalValidatedPrivateDnses(LinkProperties target) {
        Collection<InetAddress> targetDnses = target.getValidatedPrivateDnsServers();
        if (this.mValidatedPrivateDnses.size() == targetDnses.size()) {
            return this.mValidatedPrivateDnses.containsAll(targetDnses);
        }
        return false;
    }

    private protected boolean isIdenticalRoutes(LinkProperties target) {
        Collection<RouteInfo> targetRoutes = target.getRoutes();
        if (this.mRoutes.size() == targetRoutes.size()) {
            return this.mRoutes.containsAll(targetRoutes);
        }
        return false;
    }

    private protected boolean isIdenticalHttpProxy(LinkProperties target) {
        if (getHttpProxy() == null) {
            return target.getHttpProxy() == null;
        }
        return getHttpProxy().equals(target.getHttpProxy());
    }

    private protected boolean isIdenticalStackedLinks(LinkProperties target) {
        if (this.mStackedLinks.keySet().equals(target.mStackedLinks.keySet())) {
            for (LinkProperties stacked : this.mStackedLinks.values()) {
                String iface = stacked.getInterfaceName();
                if (!stacked.equals(target.mStackedLinks.get(iface))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public synchronized boolean isIdenticalMtu(LinkProperties target) {
        return getMtu() == target.getMtu();
    }

    public synchronized boolean isIdenticalTcpBufferSizes(LinkProperties target) {
        return Objects.equals(this.mTcpBufferSizes, target.mTcpBufferSizes);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LinkProperties) {
            LinkProperties target = (LinkProperties) obj;
            return isIdenticalInterfaceName(target) && isIdenticalAddresses(target) && isIdenticalDnses(target) && isIdenticalPrivateDns(target) && isIdenticalValidatedPrivateDnses(target) && isIdenticalRoutes(target) && isIdenticalHttpProxy(target) && isIdenticalStackedLinks(target) && isIdenticalMtu(target) && isIdenticalTcpBufferSizes(target);
        }
        return false;
    }

    public synchronized CompareResult<LinkAddress> compareAddresses(LinkProperties target) {
        return new CompareResult<>(this.mLinkAddresses, target != null ? target.getLinkAddresses() : null);
    }

    public synchronized CompareResult<InetAddress> compareDnses(LinkProperties target) {
        return new CompareResult<>(this.mDnses, target != null ? target.getDnsServers() : null);
    }

    public synchronized CompareResult<InetAddress> compareValidatedPrivateDnses(LinkProperties target) {
        return new CompareResult<>(this.mValidatedPrivateDnses, target != null ? target.getValidatedPrivateDnsServers() : null);
    }

    public synchronized CompareResult<RouteInfo> compareAllRoutes(LinkProperties target) {
        return new CompareResult<>(getAllRoutes(), target != null ? target.getAllRoutes() : null);
    }

    public synchronized CompareResult<String> compareAllInterfaceNames(LinkProperties target) {
        return new CompareResult<>(getAllInterfaceNames(), target != null ? target.getAllInterfaceNames() : null);
    }

    public int hashCode() {
        int hashCode;
        if (this.mIfaceName == null) {
            hashCode = 0;
        } else {
            hashCode = this.mIfaceName.hashCode() + (this.mLinkAddresses.size() * 31) + (this.mDnses.size() * 37) + (this.mValidatedPrivateDnses.size() * 61) + (this.mDomains == null ? 0 : this.mDomains.hashCode()) + (this.mRoutes.size() * 41) + (this.mHttpProxy == null ? 0 : this.mHttpProxy.hashCode()) + (this.mStackedLinks.hashCode() * 47);
        }
        return hashCode + (this.mMtu * 51) + (this.mTcpBufferSizes == null ? 0 : this.mTcpBufferSizes.hashCode()) + (this.mUsePrivateDns ? 57 : 0) + (this.mPrivateDnsServerName != null ? this.mPrivateDnsServerName.hashCode() : 0);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getInterfaceName());
        dest.writeInt(this.mLinkAddresses.size());
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            LinkAddress linkAddress = it.next();
            dest.writeParcelable(linkAddress, flags);
        }
        dest.writeInt(this.mDnses.size());
        Iterator<InetAddress> it2 = this.mDnses.iterator();
        while (it2.hasNext()) {
            InetAddress d = it2.next();
            dest.writeByteArray(d.getAddress());
        }
        dest.writeInt(this.mValidatedPrivateDnses.size());
        Iterator<InetAddress> it3 = this.mValidatedPrivateDnses.iterator();
        while (it3.hasNext()) {
            InetAddress d2 = it3.next();
            dest.writeByteArray(d2.getAddress());
        }
        dest.writeBoolean(this.mUsePrivateDns);
        dest.writeString(this.mPrivateDnsServerName);
        dest.writeString(this.mDomains);
        dest.writeInt(this.mMtu);
        dest.writeString(this.mTcpBufferSizes);
        dest.writeInt(this.mRoutes.size());
        Iterator<RouteInfo> it4 = this.mRoutes.iterator();
        while (it4.hasNext()) {
            RouteInfo route = it4.next();
            dest.writeParcelable(route, flags);
        }
        if (this.mHttpProxy != null) {
            dest.writeByte((byte) 1);
            dest.writeParcelable(this.mHttpProxy, flags);
        } else {
            dest.writeByte((byte) 0);
        }
        ArrayList<LinkProperties> stackedLinks = new ArrayList<>(this.mStackedLinks.values());
        dest.writeList(stackedLinks);
    }

    public static synchronized boolean isValidMtu(int mtu, boolean ipv6) {
        if (ipv6) {
            if (mtu >= 1280 && mtu <= 10000) {
                return true;
            }
            return false;
        } else if (mtu >= 68 && mtu <= 10000) {
            return true;
        } else {
            return false;
        }
    }
}
