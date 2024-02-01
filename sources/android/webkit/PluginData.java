package android.webkit;

import java.io.InputStream;
import java.util.Map;
@Deprecated
/* loaded from: classes2.dex */
public final class PluginData {
    private long mContentLength;
    private Map<String, String[]> mHeaders;
    private int mStatusCode;
    private InputStream mStream;

    @Deprecated
    private protected PluginData(InputStream stream, long length, Map<String, String[]> headers, int code) {
        this.mStream = stream;
        this.mContentLength = length;
        this.mHeaders = headers;
        this.mStatusCode = code;
    }

    @Deprecated
    private protected InputStream getInputStream() {
        return this.mStream;
    }

    @Deprecated
    private protected long getContentLength() {
        return this.mContentLength;
    }

    @Deprecated
    private protected Map<String, String[]> getHeaders() {
        return this.mHeaders;
    }

    @Deprecated
    private protected int getStatusCode() {
        return this.mStatusCode;
    }
}
