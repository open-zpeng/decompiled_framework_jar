package android.net;

import android.util.Log;
import java.io.IOException;
import java.net.InetSocketAddress;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/* loaded from: classes2.dex */
public class PrivateDnsConnectivityChecker {
    private static final int CONNECTION_TIMEOUT_MS = 5000;
    private static final int PRIVATE_DNS_PORT = 853;
    private static final String TAG = "NetworkUtils";

    private PrivateDnsConnectivityChecker() {
    }

    public static boolean canConnectToPrivateDnsServer(String hostname) {
        SocketFactory factory = SSLSocketFactory.getDefault();
        TrafficStats.setThreadStatsTag(TrafficStats.TAG_SYSTEM_APP);
        try {
            SSLSocket socket = (SSLSocket) factory.createSocket();
            socket.setSoTimeout(5000);
            socket.connect(new InetSocketAddress(hostname, 853));
            if (!socket.isConnected()) {
                Log.w(TAG, String.format("Connection to %s failed.", hostname));
                socket.close();
                return false;
            }
            socket.startHandshake();
            Log.w(TAG, String.format("TLS handshake to %s succeeded.", hostname));
            socket.close();
            return true;
        } catch (IOException e) {
            Log.w(TAG, String.format("TLS handshake to %s failed.", hostname), e);
            return false;
        }
    }
}
