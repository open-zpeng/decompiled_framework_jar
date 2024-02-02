package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.proto.ProtoOutputStream;
import com.android.okhttp.internalandroidapi.Dns;
import com.android.okhttp.internalandroidapi.HttpURLConnectionFactory;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import libcore.io.IoUtils;
/* loaded from: classes2.dex */
public class Network implements Parcelable {
    public static final Parcelable.Creator<Network> CREATOR;
    private static final long HANDLE_MAGIC = 3405697037L;
    private static final int HANDLE_MAGIC_SIZE = 32;
    private static final boolean httpKeepAlive = Boolean.parseBoolean(System.getProperty("http.keepAlive", "true"));
    private static final long httpKeepAliveDurationMs;
    private static final int httpMaxConnections;
    private volatile HttpURLConnectionFactory mUrlConnectionFactory;
    private protected final int netId;
    private volatile NetworkBoundSocketFactory mNetworkBoundSocketFactory = null;
    private final Object mLock = new Object();
    private boolean mPrivateDnsBypass = false;

    static {
        httpMaxConnections = httpKeepAlive ? Integer.parseInt(System.getProperty("http.maxConnections", "5")) : 0;
        httpKeepAliveDurationMs = Long.parseLong(System.getProperty("http.keepAliveDuration", "300000"));
        CREATOR = new Parcelable.Creator<Network>() { // from class: android.net.Network.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Network createFromParcel(Parcel in) {
                int netId = in.readInt();
                return new Network(netId);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Network[] newArray(int size) {
                return new Network[size];
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Network(int netId) {
        this.netId = netId;
    }

    public synchronized Network(Network that) {
        this.netId = that.netId;
    }

    public InetAddress[] getAllByName(String host) throws UnknownHostException {
        return InetAddress.getAllByNameOnNet(host, getNetIdForResolv());
    }

    public InetAddress getByName(String host) throws UnknownHostException {
        return InetAddress.getByNameOnNet(host, getNetIdForResolv());
    }

    public synchronized void setPrivateDnsBypass(boolean bypass) {
        this.mPrivateDnsBypass = bypass;
    }

    public synchronized int getNetIdForResolv() {
        if (this.mPrivateDnsBypass) {
            return (int) (2147483648L | this.netId);
        }
        return this.netId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class NetworkBoundSocketFactory extends SocketFactory {
        private final int mNetId;

        public NetworkBoundSocketFactory(int netId) {
            this.mNetId = netId;
        }

        private synchronized Socket connectToHost(String host, int port, SocketAddress localAddress) throws IOException {
            InetAddress[] hostAddresses = Network.this.getAllByName(host);
            for (int i = 0; i < hostAddresses.length; i++) {
                try {
                    Socket socket = createSocket();
                    if (localAddress != null) {
                        socket.bind(localAddress);
                    }
                    socket.connect(new InetSocketAddress(hostAddresses[i], port));
                    if (0 != 0) {
                        IoUtils.closeQuietly(socket);
                    }
                    return socket;
                } catch (IOException e) {
                    if (i == hostAddresses.length - 1) {
                        throw e;
                    }
                }
            }
            throw new UnknownHostException(host);
        }

        @Override // javax.net.SocketFactory
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
            return connectToHost(host, port, new InetSocketAddress(localHost, localPort));
        }

        @Override // javax.net.SocketFactory
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            Socket socket = createSocket();
            boolean failed = true;
            try {
                socket.bind(new InetSocketAddress(localAddress, localPort));
                socket.connect(new InetSocketAddress(address, port));
                failed = false;
                return socket;
            } finally {
                if (failed) {
                    IoUtils.closeQuietly(socket);
                }
            }
        }

        @Override // javax.net.SocketFactory
        public Socket createSocket(InetAddress host, int port) throws IOException {
            Socket socket = createSocket();
            boolean failed = true;
            try {
                socket.connect(new InetSocketAddress(host, port));
                failed = false;
                return socket;
            } finally {
                if (failed) {
                    IoUtils.closeQuietly(socket);
                }
            }
        }

        @Override // javax.net.SocketFactory
        public Socket createSocket(String host, int port) throws IOException {
            return connectToHost(host, port, null);
        }

        @Override // javax.net.SocketFactory
        public Socket createSocket() throws IOException {
            Socket socket = new Socket();
            boolean failed = true;
            try {
                Network.this.bindSocket(socket);
                failed = false;
                return socket;
            } finally {
                if (failed) {
                    IoUtils.closeQuietly(socket);
                }
            }
        }
    }

    public SocketFactory getSocketFactory() {
        if (this.mNetworkBoundSocketFactory == null) {
            synchronized (this.mLock) {
                if (this.mNetworkBoundSocketFactory == null) {
                    this.mNetworkBoundSocketFactory = new NetworkBoundSocketFactory(this.netId);
                }
            }
        }
        return this.mNetworkBoundSocketFactory;
    }

    private synchronized void maybeInitUrlConnectionFactory() {
        synchronized (this.mLock) {
            if (this.mUrlConnectionFactory == null) {
                Dns dnsLookup = new Dns() { // from class: android.net.-$$Lambda$Network$KD6DxaMRJIcajhj36TU1K7lJnHQ
                    public final List lookup(String str) {
                        List asList;
                        asList = Arrays.asList(Network.this.getAllByName(str));
                        return asList;
                    }
                };
                HttpURLConnectionFactory urlConnectionFactory = new HttpURLConnectionFactory();
                urlConnectionFactory.setDns(dnsLookup);
                urlConnectionFactory.setNewConnectionPool(httpMaxConnections, httpKeepAliveDurationMs, TimeUnit.MILLISECONDS);
                this.mUrlConnectionFactory = urlConnectionFactory;
            }
        }
    }

    public URLConnection openConnection(URL url) throws IOException {
        java.net.Proxy proxy;
        ConnectivityManager cm = ConnectivityManager.getInstanceOrNull();
        if (cm == null) {
            throw new IOException("No ConnectivityManager yet constructed, please construct one");
        }
        ProxyInfo proxyInfo = cm.getProxyForNetwork(this);
        if (proxyInfo != null) {
            proxy = proxyInfo.makeProxy();
        } else {
            proxy = java.net.Proxy.NO_PROXY;
        }
        return openConnection(url, proxy);
    }

    public URLConnection openConnection(URL url, java.net.Proxy proxy) throws IOException {
        if (proxy == null) {
            throw new IllegalArgumentException("proxy is null");
        }
        maybeInitUrlConnectionFactory();
        SocketFactory socketFactory = getSocketFactory();
        return this.mUrlConnectionFactory.openConnection(url, socketFactory, proxy);
    }

    public void bindSocket(DatagramSocket socket) throws IOException {
        socket.getReuseAddress();
        bindSocket(socket.getFileDescriptor$());
    }

    public void bindSocket(Socket socket) throws IOException {
        socket.getReuseAddress();
        bindSocket(socket.getFileDescriptor$());
    }

    public void bindSocket(FileDescriptor fd) throws IOException {
        InetAddress inetPeer;
        try {
            SocketAddress peer = Os.getpeername(fd);
            inetPeer = ((InetSocketAddress) peer).getAddress();
        } catch (ErrnoException e) {
            if (e.errno != OsConstants.ENOTCONN) {
                throw e.rethrowAsSocketException();
            }
        } catch (ClassCastException e2) {
            throw new SocketException("Only AF_INET/AF_INET6 sockets supported");
        }
        if (!inetPeer.isAnyLocalAddress()) {
            throw new SocketException("Socket is connected");
        }
        int err = NetworkUtils.bindSocketToNetwork(fd.getInt$(), this.netId);
        if (err != 0) {
            throw new ErrnoException("Binding socket to network " + this.netId, -err).rethrowAsSocketException();
        }
    }

    public static Network fromNetworkHandle(long networkHandle) {
        if (networkHandle == 0) {
            throw new IllegalArgumentException("Network.fromNetworkHandle refusing to instantiate NETID_UNSET Network.");
        }
        if ((4294967295L & networkHandle) != HANDLE_MAGIC || networkHandle < 0) {
            throw new IllegalArgumentException("Value passed to fromNetworkHandle() is not a network handle.");
        }
        return new Network((int) (networkHandle >> 32));
    }

    public long getNetworkHandle() {
        if (this.netId == 0) {
            return 0L;
        }
        return (this.netId << 32) | HANDLE_MAGIC;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.netId);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Network) {
            Network other = (Network) obj;
            return this.netId == other.netId;
        }
        return false;
    }

    public int hashCode() {
        return this.netId * 11;
    }

    public String toString() {
        return Integer.toString(this.netId);
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.netId);
        proto.end(token);
    }
}
