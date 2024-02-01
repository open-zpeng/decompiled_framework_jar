package android.net;

import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.os.RoSystemProperties;
import com.android.org.conscrypt.Conscrypt;
import com.android.org.conscrypt.OpenSSLContextImpl;
import com.android.org.conscrypt.OpenSSLSocketImpl;
import com.android.org.conscrypt.SSLClientSessionCache;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
/* loaded from: classes2.dex */
public class SSLCertificateSocketFactory extends SSLSocketFactory {
    public protected static final TrustManager[] INSECURE_TRUST_MANAGER = {new X509TrustManager() { // from class: android.net.SSLCertificateSocketFactory.1
        @Override // javax.net.ssl.X509TrustManager
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }};
    public protected static final String TAG = "SSLCertificateSocketFactory";
    public protected byte[] mAlpnProtocols;
    public protected PrivateKey mChannelIdPrivateKey;
    public protected final int mHandshakeTimeoutMillis;
    public protected SSLSocketFactory mInsecureFactory;
    public protected KeyManager[] mKeyManagers;
    public protected byte[] mNpnProtocols;
    public protected final boolean mSecure;
    public protected SSLSocketFactory mSecureFactory;
    public protected final SSLClientSessionCache mSessionCache;
    public protected TrustManager[] mTrustManagers;

    @Deprecated
    public SSLCertificateSocketFactory(int handshakeTimeoutMillis) {
        this(handshakeTimeoutMillis, null, true);
    }

    public protected SSLCertificateSocketFactory(int handshakeTimeoutMillis, SSLSessionCache cache, boolean secure) {
        this.mInsecureFactory = null;
        this.mSecureFactory = null;
        this.mTrustManagers = null;
        this.mKeyManagers = null;
        this.mNpnProtocols = null;
        this.mAlpnProtocols = null;
        this.mChannelIdPrivateKey = null;
        this.mHandshakeTimeoutMillis = handshakeTimeoutMillis;
        this.mSessionCache = cache != null ? cache.mSessionCache : null;
        this.mSecure = secure;
    }

    public static SocketFactory getDefault(int handshakeTimeoutMillis) {
        return new SSLCertificateSocketFactory(handshakeTimeoutMillis, null, true);
    }

    public static SSLSocketFactory getDefault(int handshakeTimeoutMillis, SSLSessionCache cache) {
        return new SSLCertificateSocketFactory(handshakeTimeoutMillis, cache, true);
    }

    public static SSLSocketFactory getInsecure(int handshakeTimeoutMillis, SSLSessionCache cache) {
        return new SSLCertificateSocketFactory(handshakeTimeoutMillis, cache, false);
    }

    @Deprecated
    private protected static org.apache.http.conn.ssl.SSLSocketFactory getHttpSocketFactory(int handshakeTimeoutMillis, SSLSessionCache cache) {
        return new org.apache.http.conn.ssl.SSLSocketFactory(new SSLCertificateSocketFactory(handshakeTimeoutMillis, cache, true));
    }

    private protected static void verifyHostname(Socket socket, String hostname) throws IOException {
        if (!(socket instanceof SSLSocket)) {
            throw new IllegalArgumentException("Attempt to verify non-SSL socket");
        }
        if (!isSslCheckRelaxed()) {
            SSLSocket ssl = (SSLSocket) socket;
            ssl.startHandshake();
            SSLSession session = ssl.getSession();
            if (session == null) {
                throw new SSLException("Cannot verify SSL socket without session");
            }
            if (!HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session)) {
                throw new SSLPeerUnverifiedException("Cannot verify hostname: " + hostname);
            }
        }
    }

    public protected SSLSocketFactory makeSocketFactory(KeyManager[] keyManagers, TrustManager[] trustManagers) {
        try {
            OpenSSLContextImpl sslContext = Conscrypt.newPreferredSSLContextSpi();
            sslContext.engineInit(keyManagers, trustManagers, (SecureRandom) null);
            sslContext.engineGetClientSessionContext().setPersistentCache(this.mSessionCache);
            return sslContext.engineGetSocketFactory();
        } catch (KeyManagementException e) {
            Log.wtf(TAG, e);
            return (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
    }

    public protected static boolean isSslCheckRelaxed() {
        return RoSystemProperties.DEBUGGABLE && SystemProperties.getBoolean("socket.relaxsslcheck", false);
    }

    public protected synchronized SSLSocketFactory getDelegate() {
        if (this.mSecure && !isSslCheckRelaxed()) {
            if (this.mSecureFactory == null) {
                this.mSecureFactory = makeSocketFactory(this.mKeyManagers, this.mTrustManagers);
            }
            return this.mSecureFactory;
        }
        if (this.mInsecureFactory == null) {
            if (this.mSecure) {
                Log.w(TAG, "*** BYPASSING SSL SECURITY CHECKS (socket.relaxsslcheck=yes) ***");
            } else {
                Log.w(TAG, "Bypassing SSL security checks at caller's request");
            }
            this.mInsecureFactory = makeSocketFactory(this.mKeyManagers, INSECURE_TRUST_MANAGER);
        }
        return this.mInsecureFactory;
    }

    public void setTrustManagers(TrustManager[] trustManager) {
        this.mTrustManagers = trustManager;
        this.mSecureFactory = null;
    }

    public void setNpnProtocols(byte[][] npnProtocols) {
        this.mNpnProtocols = toLengthPrefixedList(npnProtocols);
    }

    private protected void setAlpnProtocols(byte[][] protocols) {
        this.mAlpnProtocols = toLengthPrefixedList(protocols);
    }

    static byte[] toLengthPrefixedList(byte[]... items) {
        if (items.length == 0) {
            throw new IllegalArgumentException("items.length == 0");
        }
        int totalLength = 0;
        for (byte[] s : items) {
            if (s.length == 0 || s.length > 255) {
                throw new IllegalArgumentException("s.length == 0 || s.length > 255: " + s.length);
            }
            totalLength += 1 + s.length;
        }
        byte[] result = new byte[totalLength];
        int length = items.length;
        int pos = 0;
        int pos2 = 0;
        while (pos2 < length) {
            byte[] s2 = items[pos2];
            int pos3 = pos + 1;
            result[pos] = (byte) s2.length;
            int pos4 = s2.length;
            int pos5 = pos3;
            int pos6 = 0;
            while (pos6 < pos4) {
                byte b = s2[pos6];
                result[pos5] = b;
                pos6++;
                pos5++;
            }
            pos2++;
            pos = pos5;
        }
        return result;
    }

    public byte[] getNpnSelectedProtocol(Socket socket) {
        return castToOpenSSLSocket(socket).getNpnSelectedProtocol();
    }

    private protected byte[] getAlpnSelectedProtocol(Socket socket) {
        return castToOpenSSLSocket(socket).getAlpnSelectedProtocol();
    }

    public void setKeyManagers(KeyManager[] keyManagers) {
        this.mKeyManagers = keyManagers;
        this.mSecureFactory = null;
        this.mInsecureFactory = null;
    }

    private protected void setChannelIdPrivateKey(PrivateKey privateKey) {
        this.mChannelIdPrivateKey = privateKey;
    }

    public void setUseSessionTickets(Socket socket, boolean useSessionTickets) {
        castToOpenSSLSocket(socket).setUseSessionTickets(useSessionTickets);
    }

    public void setHostname(Socket socket, String hostName) {
        castToOpenSSLSocket(socket).setHostname(hostName);
    }

    private protected void setSoWriteTimeout(Socket socket, int writeTimeoutMilliseconds) throws SocketException {
        castToOpenSSLSocket(socket).setSoWriteTimeout(writeTimeoutMilliseconds);
    }

    public protected static OpenSSLSocketImpl castToOpenSSLSocket(Socket socket) {
        if (!(socket instanceof OpenSSLSocketImpl)) {
            throw new IllegalArgumentException("Socket not created by this factory: " + socket);
        }
        return (OpenSSLSocketImpl) socket;
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public Socket createSocket(Socket k, String host, int port, boolean close) throws IOException {
        OpenSSLSocketImpl s = getDelegate().createSocket(k, host, port, close);
        s.setNpnProtocols(this.mNpnProtocols);
        s.setAlpnProtocols(this.mAlpnProtocols);
        s.setHandshakeTimeout(this.mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(this.mChannelIdPrivateKey);
        if (this.mSecure) {
            verifyHostname(s, host);
        }
        return s;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket() throws IOException {
        OpenSSLSocketImpl s = getDelegate().createSocket();
        s.setNpnProtocols(this.mNpnProtocols);
        s.setAlpnProtocols(this.mAlpnProtocols);
        s.setHandshakeTimeout(this.mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(this.mChannelIdPrivateKey);
        return s;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress addr, int port, InetAddress localAddr, int localPort) throws IOException {
        OpenSSLSocketImpl s = getDelegate().createSocket(addr, port, localAddr, localPort);
        s.setNpnProtocols(this.mNpnProtocols);
        s.setAlpnProtocols(this.mAlpnProtocols);
        s.setHandshakeTimeout(this.mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(this.mChannelIdPrivateKey);
        return s;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress addr, int port) throws IOException {
        OpenSSLSocketImpl s = getDelegate().createSocket(addr, port);
        s.setNpnProtocols(this.mNpnProtocols);
        s.setAlpnProtocols(this.mAlpnProtocols);
        s.setHandshakeTimeout(this.mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(this.mChannelIdPrivateKey);
        return s;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String host, int port, InetAddress localAddr, int localPort) throws IOException {
        OpenSSLSocketImpl s = getDelegate().createSocket(host, port, localAddr, localPort);
        s.setNpnProtocols(this.mNpnProtocols);
        s.setAlpnProtocols(this.mAlpnProtocols);
        s.setHandshakeTimeout(this.mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(this.mChannelIdPrivateKey);
        if (this.mSecure) {
            verifyHostname(s, host);
        }
        return s;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String host, int port) throws IOException {
        OpenSSLSocketImpl s = getDelegate().createSocket(host, port);
        s.setNpnProtocols(this.mNpnProtocols);
        s.setAlpnProtocols(this.mAlpnProtocols);
        s.setHandshakeTimeout(this.mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(this.mChannelIdPrivateKey);
        if (this.mSecure) {
            verifyHostname(s, host);
        }
        return s;
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getDefaultCipherSuites() {
        return getDelegate().getDefaultCipherSuites();
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getSupportedCipherSuites() {
        return getDelegate().getSupportedCipherSuites();
    }
}
