package android.media;

import android.media.IMediaHTTPConnection;
import android.net.NetworkUtils;
import android.net.ProxyInfo;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.SettingsStringUtil;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class MediaHTTPConnection extends IMediaHTTPConnection.Stub {
    private static final int CONNECT_TIMEOUT_MS = 30000;
    private static final int HTTP_TEMP_REDIRECT = 307;
    private static final int MAX_REDIRECTS = 20;
    private static final String TAG = "MediaHTTPConnection";
    private static final boolean VERBOSE = false;
    private long mNativeContext;
    public protected long mCurrentOffset = -1;
    public protected URL mURL = null;
    public protected Map<String, String> mHeaders = null;
    public protected HttpURLConnection mConnection = null;
    public protected long mTotalSize = -1;
    private InputStream mInputStream = null;
    public protected boolean mAllowCrossDomainRedirect = true;
    public protected boolean mAllowCrossProtocolRedirect = true;

    private final native void native_finalize();

    private final native IBinder native_getIMemory();

    private static final native void native_init();

    private final native int native_readAt(long j, int i);

    private final native void native_setup();

    /* JADX INFO: Access modifiers changed from: private */
    public MediaHTTPConnection() {
        CookieHandler cookieHandler = CookieHandler.getDefault();
        if (cookieHandler == null) {
            Log.w(TAG, "MediaHTTPConnection: Unexpected. No CookieHandler found.");
        }
        native_setup();
    }

    private protected IBinder connect(String uri, String headers) {
        try {
            disconnect();
            this.mAllowCrossDomainRedirect = true;
            this.mURL = new URL(uri);
            this.mHeaders = convertHeaderStringToMap(headers);
            return native_getIMemory();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private synchronized boolean parseBoolean(String val) {
        try {
            return Long.parseLong(val) != 0;
        } catch (NumberFormatException e) {
            return "true".equalsIgnoreCase(val) || "yes".equalsIgnoreCase(val);
        }
    }

    private synchronized boolean filterOutInternalHeaders(String key, String val) {
        if ("android-allow-cross-domain-redirect".equalsIgnoreCase(key)) {
            this.mAllowCrossDomainRedirect = parseBoolean(val);
            this.mAllowCrossProtocolRedirect = this.mAllowCrossDomainRedirect;
            return true;
        }
        return false;
    }

    private synchronized Map<String, String> convertHeaderStringToMap(String headers) {
        HashMap<String, String> map = new HashMap<>();
        String[] pairs = headers.split("\r\n");
        for (String pair : pairs) {
            int colonPos = pair.indexOf(SettingsStringUtil.DELIMITER);
            if (colonPos >= 0) {
                String key = pair.substring(0, colonPos);
                String val = pair.substring(colonPos + 1);
                if (!filterOutInternalHeaders(key, val)) {
                    map.put(key, val);
                }
            }
        }
        return map;
    }

    private protected void disconnect() {
        teardownConnection();
        this.mHeaders = null;
        this.mURL = null;
    }

    private synchronized void teardownConnection() {
        if (this.mConnection != null) {
            if (this.mInputStream != null) {
                try {
                    this.mInputStream.close();
                } catch (IOException e) {
                }
                this.mInputStream = null;
            }
            this.mConnection.disconnect();
            this.mConnection = null;
            this.mCurrentOffset = -1L;
        }
    }

    private static final synchronized boolean isLocalHost(URL url) {
        String host;
        if (url == null || (host = url.getHost()) == null) {
            return false;
        }
        if (host.equalsIgnoreCase(ProxyInfo.LOCAL_HOST)) {
            return true;
        }
        return NetworkUtils.numericToInetAddress(host).isLoopbackAddress();
    }

    /* JADX WARN: Code restructure failed: missing block: B:86:0x019c, code lost:
        r17.mURL = r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized void seekTo(long r18) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 461
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaHTTPConnection.seekTo(long):void");
    }

    private protected int readAt(long offset, int size) {
        return native_readAt(offset, size);
    }

    private synchronized int readAt(long offset, byte[] data, int size) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            synchronized (this) {
                if (offset != this.mCurrentOffset) {
                    seekTo(offset);
                }
            }
            int n = this.mInputStream.read(data, 0, size);
            if (n == -1) {
                n = 0;
            }
            this.mCurrentOffset += n;
            return n;
        } catch (NoRouteToHostException e) {
            Log.w(TAG, "readAt " + offset + " / " + size + " => " + e);
            return -1010;
        } catch (ProtocolException e2) {
            Log.w(TAG, "readAt " + offset + " / " + size + " => " + e2);
            return -1010;
        } catch (UnknownServiceException e3) {
            Log.w(TAG, "readAt " + offset + " / " + size + " => " + e3);
            return -1010;
        } catch (IOException e4) {
            return -1;
        } catch (Exception e5) {
            return -1;
        }
    }

    @Override // android.media.IMediaHTTPConnection
    public synchronized long getSize() {
        if (this.mConnection == null) {
            try {
                seekTo(0L);
            } catch (IOException e) {
                return -1L;
            }
        }
        return this.mTotalSize;
    }

    private protected synchronized String getMIMEType() {
        if (this.mConnection == null) {
            try {
                seekTo(0L);
            } catch (IOException e) {
                return "application/octet-stream";
            }
        }
        return this.mConnection.getContentType();
    }

    private protected String getUri() {
        return this.mURL.toString();
    }

    protected void finalize() {
        native_finalize();
    }

    static {
        System.loadLibrary("media_jni");
        native_init();
    }
}
