package android.net;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.net.IConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.system.OsConstants;
import com.android.internal.net.VpnConfig;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class VpnService extends Service {
    public static final String SERVICE_INTERFACE = "android.net.VpnService";
    public static final String SERVICE_META_DATA_SUPPORTS_ALWAYS_ON = "android.net.VpnService.SUPPORTS_ALWAYS_ON";

    static /* synthetic */ IConnectivityManager access$200() {
        return getService();
    }

    private static synchronized IConnectivityManager getService() {
        return IConnectivityManager.Stub.asInterface(ServiceManager.getService(Context.CONNECTIVITY_SERVICE));
    }

    public static Intent prepare(Context context) {
        try {
            if (getService().prepareVpn(context.getPackageName(), null, context.getUserId())) {
                return null;
            }
        } catch (RemoteException e) {
        }
        return VpnConfig.getIntentForConfirmation();
    }

    @SystemApi
    public static void prepareAndAuthorize(Context context) {
        IConnectivityManager cm = getService();
        String packageName = context.getPackageName();
        try {
            int userId = context.getUserId();
            if (!cm.prepareVpn(packageName, null, userId)) {
                cm.prepareVpn(null, packageName, userId);
            }
            cm.setVpnPackageAuthorization(packageName, userId, true);
        } catch (RemoteException e) {
        }
    }

    public boolean protect(int socket) {
        return NetworkUtils.protectFromVpn(socket);
    }

    public boolean protect(Socket socket) {
        return protect(socket.getFileDescriptor$().getInt$());
    }

    public boolean protect(DatagramSocket socket) {
        return protect(socket.getFileDescriptor$().getInt$());
    }

    public synchronized boolean addAddress(InetAddress address, int prefixLength) {
        check(address, prefixLength);
        try {
            return getService().addVpnAddress(address.getHostAddress(), prefixLength);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public synchronized boolean removeAddress(InetAddress address, int prefixLength) {
        check(address, prefixLength);
        try {
            return getService().removeVpnAddress(address.getHostAddress(), prefixLength);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean setUnderlyingNetworks(Network[] networks) {
        try {
            return getService().setUnderlyingNetworksForVpn(networks);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (intent == null || !"android.net.VpnService".equals(intent.getAction())) {
            return null;
        }
        return new Callback();
    }

    public void onRevoke() {
        stopSelf();
    }

    /* loaded from: classes2.dex */
    private class Callback extends Binder {
        private Callback() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
            if (code == 16777215) {
                VpnService.this.onRevoke();
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void check(InetAddress address, int prefixLength) {
        if (address.isLoopbackAddress()) {
            throw new IllegalArgumentException("Bad address");
        }
        if (address instanceof Inet4Address) {
            if (prefixLength < 0 || prefixLength > 32) {
                throw new IllegalArgumentException("Bad prefixLength");
            }
        } else if (address instanceof Inet6Address) {
            if (prefixLength < 0 || prefixLength > 128) {
                throw new IllegalArgumentException("Bad prefixLength");
            }
        } else {
            throw new IllegalArgumentException("Unsupported family");
        }
    }

    /* loaded from: classes2.dex */
    public class Builder {
        private final VpnConfig mConfig = new VpnConfig();
        public protected final List<LinkAddress> mAddresses = new ArrayList();
        public protected final List<RouteInfo> mRoutes = new ArrayList();

        public Builder() {
            this.mConfig.user = VpnService.this.getClass().getName();
        }

        public Builder setSession(String session) {
            this.mConfig.session = session;
            return this;
        }

        public Builder setConfigureIntent(PendingIntent intent) {
            this.mConfig.configureIntent = intent;
            return this;
        }

        public Builder setMtu(int mtu) {
            if (mtu <= 0) {
                throw new IllegalArgumentException("Bad mtu");
            }
            this.mConfig.mtu = mtu;
            return this;
        }

        public Builder addAddress(InetAddress address, int prefixLength) {
            VpnService.check(address, prefixLength);
            if (address.isAnyLocalAddress()) {
                throw new IllegalArgumentException("Bad address");
            }
            this.mAddresses.add(new LinkAddress(address, prefixLength));
            this.mConfig.updateAllowedFamilies(address);
            return this;
        }

        public Builder addAddress(String address, int prefixLength) {
            return addAddress(InetAddress.parseNumericAddress(address), prefixLength);
        }

        public Builder addRoute(InetAddress address, int prefixLength) {
            VpnService.check(address, prefixLength);
            int offset = prefixLength / 8;
            byte[] bytes = address.getAddress();
            if (offset < bytes.length) {
                bytes[offset] = (byte) (bytes[offset] << (prefixLength % 8));
                while (offset < bytes.length) {
                    if (bytes[offset] == 0) {
                        offset++;
                    } else {
                        throw new IllegalArgumentException("Bad address");
                    }
                }
            }
            this.mRoutes.add(new RouteInfo(new IpPrefix(address, prefixLength), (InetAddress) null));
            this.mConfig.updateAllowedFamilies(address);
            return this;
        }

        public Builder addRoute(String address, int prefixLength) {
            return addRoute(InetAddress.parseNumericAddress(address), prefixLength);
        }

        public Builder addDnsServer(InetAddress address) {
            if (address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                throw new IllegalArgumentException("Bad address");
            }
            if (this.mConfig.dnsServers == null) {
                this.mConfig.dnsServers = new ArrayList();
            }
            this.mConfig.dnsServers.add(address.getHostAddress());
            return this;
        }

        public Builder addDnsServer(String address) {
            return addDnsServer(InetAddress.parseNumericAddress(address));
        }

        public Builder addSearchDomain(String domain) {
            if (this.mConfig.searchDomains == null) {
                this.mConfig.searchDomains = new ArrayList();
            }
            this.mConfig.searchDomains.add(domain);
            return this;
        }

        public Builder allowFamily(int family) {
            if (family == OsConstants.AF_INET) {
                this.mConfig.allowIPv4 = true;
            } else if (family == OsConstants.AF_INET6) {
                this.mConfig.allowIPv6 = true;
            } else {
                throw new IllegalArgumentException(family + " is neither " + OsConstants.AF_INET + " nor " + OsConstants.AF_INET6);
            }
            return this;
        }

        private synchronized void verifyApp(String packageName) throws PackageManager.NameNotFoundException {
            IPackageManager pm = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
            try {
                pm.getApplicationInfo(packageName, 0, UserHandle.getCallingUserId());
            } catch (RemoteException e) {
                throw new IllegalStateException(e);
            }
        }

        public Builder addAllowedApplication(String packageName) throws PackageManager.NameNotFoundException {
            if (this.mConfig.disallowedApplications != null) {
                throw new UnsupportedOperationException("addDisallowedApplication already called");
            }
            verifyApp(packageName);
            if (this.mConfig.allowedApplications == null) {
                this.mConfig.allowedApplications = new ArrayList();
            }
            this.mConfig.allowedApplications.add(packageName);
            return this;
        }

        public Builder addDisallowedApplication(String packageName) throws PackageManager.NameNotFoundException {
            if (this.mConfig.allowedApplications != null) {
                throw new UnsupportedOperationException("addAllowedApplication already called");
            }
            verifyApp(packageName);
            if (this.mConfig.disallowedApplications == null) {
                this.mConfig.disallowedApplications = new ArrayList();
            }
            this.mConfig.disallowedApplications.add(packageName);
            return this;
        }

        public Builder allowBypass() {
            this.mConfig.allowBypass = true;
            return this;
        }

        public Builder setBlocking(boolean blocking) {
            this.mConfig.blocking = blocking;
            return this;
        }

        public Builder setUnderlyingNetworks(Network[] networks) {
            this.mConfig.underlyingNetworks = networks != null ? (Network[]) networks.clone() : null;
            return this;
        }

        public ParcelFileDescriptor establish() {
            this.mConfig.addresses = this.mAddresses;
            this.mConfig.routes = this.mRoutes;
            try {
                return VpnService.access$200().establishVpn(this.mConfig);
            } catch (RemoteException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
