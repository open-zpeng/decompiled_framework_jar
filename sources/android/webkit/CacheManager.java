package android.webkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
@Deprecated
/* loaded from: classes2.dex */
public final class CacheManager {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    @Deprecated
    /* loaded from: classes2.dex */
    public static class CacheResult {
        public private protected long contentLength;
        public private protected String contentdisposition;
        public private protected String crossDomain;
        public private protected String encoding;
        public private protected String etag;
        public private protected long expires;
        public private protected String expiresString;
        public private protected int httpStatusCode;
        public private protected InputStream inStream;
        public private protected String lastModified;
        public private protected String localPath;
        public private protected String location;
        public private protected String mimeType;
        public private protected File outFile;
        public private protected OutputStream outStream;

        private protected CacheResult() {
        }

        private protected int getHttpStatusCode() {
            return this.httpStatusCode;
        }

        private protected long getContentLength() {
            return this.contentLength;
        }

        private protected String getLocalPath() {
            return this.localPath;
        }

        private protected long getExpires() {
            return this.expires;
        }

        private protected String getExpiresString() {
            return this.expiresString;
        }

        private protected String getLastModified() {
            return this.lastModified;
        }

        private protected String getETag() {
            return this.etag;
        }

        private protected String getMimeType() {
            return this.mimeType;
        }

        private protected String getLocation() {
            return this.location;
        }

        private protected String getEncoding() {
            return this.encoding;
        }

        private protected String getContentDisposition() {
            return this.contentdisposition;
        }

        private protected InputStream getInputStream() {
            return this.inStream;
        }

        private protected OutputStream getOutputStream() {
            return this.outStream;
        }

        private protected void setInputStream(InputStream stream) {
            this.inStream = stream;
        }

        private protected void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public synchronized void setContentLength(long contentLength) {
            this.contentLength = contentLength;
        }
    }

    @Deprecated
    private protected static File getCacheFileBaseDir() {
        return null;
    }

    @Deprecated
    private protected static boolean cacheDisabled() {
        return false;
    }

    @Deprecated
    private protected static boolean startCacheTransaction() {
        return false;
    }

    @Deprecated
    private protected static boolean endCacheTransaction() {
        return false;
    }

    @Deprecated
    private protected static CacheResult getCacheFile(String url, Map<String, String> headers) {
        return null;
    }

    @Deprecated
    private protected static void saveCacheFile(String url, CacheResult cacheResult) {
        saveCacheFile(url, 0L, cacheResult);
    }

    public private protected static void saveCacheFile(String url, long postIdentifier, CacheResult cacheRet) {
        try {
            cacheRet.outStream.close();
        } catch (IOException e) {
        }
    }
}
