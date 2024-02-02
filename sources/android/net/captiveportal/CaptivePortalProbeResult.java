package android.net.captiveportal;
/* loaded from: classes2.dex */
public final class CaptivePortalProbeResult {
    private protected static final int FAILED_CODE = 599;
    private protected static final int PORTAL_CODE = 302;
    private protected static final int SUCCESS_CODE = 204;
    private protected final String detectUrl;
    public protected final int mHttpResponseCode;
    private protected final CaptivePortalProbeSpec probeSpec;
    private protected final String redirectUrl;
    private protected static final CaptivePortalProbeResult FAILED = new CaptivePortalProbeResult(599);
    private protected static final CaptivePortalProbeResult SUCCESS = new CaptivePortalProbeResult(204);

    private protected synchronized CaptivePortalProbeResult(int httpResponseCode) {
        this(httpResponseCode, null, null);
    }

    private protected synchronized CaptivePortalProbeResult(int httpResponseCode, String redirectUrl, String detectUrl) {
        this(httpResponseCode, redirectUrl, detectUrl, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized CaptivePortalProbeResult(int httpResponseCode, String redirectUrl, String detectUrl, CaptivePortalProbeSpec probeSpec) {
        this.mHttpResponseCode = httpResponseCode;
        this.redirectUrl = redirectUrl;
        this.detectUrl = detectUrl;
        this.probeSpec = probeSpec;
    }

    private protected synchronized boolean isSuccessful() {
        return this.mHttpResponseCode == 204;
    }

    private protected synchronized boolean isPortal() {
        return !isSuccessful() && this.mHttpResponseCode >= 200 && this.mHttpResponseCode <= 399;
    }

    private protected synchronized boolean isFailed() {
        return (isSuccessful() || isPortal()) ? false : true;
    }
}
