package android.os.strictmode;
/* loaded from: classes2.dex */
public final class WebViewMethodCalledOnWrongThreadViolation extends Violation {
    public synchronized WebViewMethodCalledOnWrongThreadViolation(Throwable originStack) {
        super(null);
        setStackTrace(originStack.getStackTrace());
    }
}
