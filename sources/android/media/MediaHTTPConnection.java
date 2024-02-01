package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.media.IMediaHTTPConnection;
import android.net.NetworkUtils;
import android.net.ProxyInfo;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.SettingsStringUtil;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
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
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class MediaHTTPConnection extends IMediaHTTPConnection.Stub {
    private static final int CONNECT_TIMEOUT_MS = 30000;
    private static final int HTTP_TEMP_REDIRECT = 307;
    private static final int MAX_REDIRECTS = 20;
    private static final String TAG = "MediaHTTPConnection";
    private static final boolean VERBOSE = false;
    private long mNativeContext;
    @UnsupportedAppUsage
    @GuardedBy({"this"})
    private long mCurrentOffset = -1;
    @UnsupportedAppUsage
    @GuardedBy({"this"})
    private URL mURL = null;
    @UnsupportedAppUsage
    @GuardedBy({"this"})
    private Map<String, String> mHeaders = null;
    @UnsupportedAppUsage
    private volatile HttpURLConnection mConnection = null;
    @UnsupportedAppUsage
    @GuardedBy({"this"})
    private long mTotalSize = -1;
    @GuardedBy({"this"})
    private InputStream mInputStream = null;
    @UnsupportedAppUsage
    @GuardedBy({"this"})
    private boolean mAllowCrossDomainRedirect = true;
    @UnsupportedAppUsage
    @GuardedBy({"this"})
    private boolean mAllowCrossProtocolRedirect = true;
    private final AtomicInteger mNumDisconnectingThreads = new AtomicInteger(0);

    private final native void native_finalize();

    private final native IBinder native_getIMemory();

    private static final native void native_init();

    private final native int native_readAt(long j, int i);

    private final native void native_setup();

    @UnsupportedAppUsage
    public MediaHTTPConnection() {
        CookieHandler cookieHandler = CookieHandler.getDefault();
        if (cookieHandler == null) {
            Log.w(TAG, "MediaHTTPConnection: Unexpected. No CookieHandler found.");
        }
        native_setup();
    }

    @Override // android.media.IMediaHTTPConnection
    @UnsupportedAppUsage
    public synchronized IBinder connect(String uri, String headers) {
        try {
            disconnect();
        } catch (MalformedURLException e) {
        }
        try {
            this.mAllowCrossDomainRedirect = true;
            this.mURL = new URL(uri);
            this.mHeaders = convertHeaderStringToMap(headers);
            return native_getIMemory();
        } catch (MalformedURLException e2) {
            return null;
        }
    }

    private static boolean parseBoolean(String val) {
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
        HashMap<String, String> map;
        map = new HashMap<>();
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

    @Override // android.media.IMediaHTTPConnection
    @UnsupportedAppUsage
    public void disconnect() {
        this.mNumDisconnectingThreads.incrementAndGet();
        try {
            HttpURLConnection connectionToDisconnect = this.mConnection;
            if (connectionToDisconnect != null) {
                connectionToDisconnect.disconnect();
            }
            synchronized (this) {
                teardownConnection();
                this.mHeaders = null;
                this.mURL = null;
            }
        } finally {
            this.mNumDisconnectingThreads.decrementAndGet();
        }
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

    private static final boolean isLocalHost(URL url) {
        String host;
        if (url == null || (host = url.getHost()) == null) {
            return false;
        }
        if (host.equalsIgnoreCase(ProxyInfo.LOCAL_HOST)) {
            return true;
        }
        return NetworkUtils.numericToInetAddress(host).isLoopbackAddress();
    }

    /* JADX WARN: Code restructure failed: missing block: B:94:0x01b0, code lost:
        r16.mURL = r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private synchronized void seekTo(long r17) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 499
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaHTTPConnection.seekTo(long):void");
    }

    @Override // android.media.IMediaHTTPConnection
    @UnsupportedAppUsage
    public synchronized int readAt(long offset, int size) {
        return native_readAt(offset, size);
    }

    private synchronized int readAt(long offset, byte[] data, int size) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            try {
                try {
                    if (offset != this.mCurrentOffset) {
                        try {
                            seekTo(offset);
                        } catch (UnknownServiceException e) {
                            e = e;
                            Log.w(TAG, "readAt " + offset + " / " + size + " => " + e);
                            return MediaPlayer.MEDIA_ERROR_UNSUPPORTED;
                        } catch (IOException e2) {
                            return -1;
                        }
                    }
                    int n = this.mInputStream.read(data, 0, size);
                    if (n == -1) {
                        n = 0;
                    }
                    this.mCurrentOffset += n;
                    return n;
                } catch (NoRouteToHostException e3) {
                    Log.w(TAG, "readAt " + offset + " / " + size + " => " + e3);
                    return MediaPlayer.MEDIA_ERROR_UNSUPPORTED;
                } catch (Exception e4) {
                    return -1;
                }
            } catch (UnknownServiceException e5) {
                e = e5;
            } catch (IOException e6) {
            }
        } catch (ProtocolException e7) {
            Log.w(TAG, "readAt " + offset + " / " + size + " => " + e7);
            return MediaPlayer.MEDIA_ERROR_UNSUPPORTED;
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

    @Override // android.media.IMediaHTTPConnection
    @UnsupportedAppUsage
    public synchronized String getMIMEType() {
        if (this.mConnection == null) {
            try {
                seekTo(0L);
            } catch (IOException e) {
                return ContentResolver.MIME_TYPE_DEFAULT;
            }
        }
        return this.mConnection.getContentType();
    }

    @Override // android.media.IMediaHTTPConnection
    @UnsupportedAppUsage
    public synchronized String getUri() {
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
