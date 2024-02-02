package android.os;
/* loaded from: classes2.dex */
public class AsyncResult {
    private protected Throwable exception;
    private protected Object result;
    private protected Object userObj;

    private protected static AsyncResult forMessage(Message m, Object r, Throwable ex) {
        AsyncResult ret = new AsyncResult(m.obj, r, ex);
        m.obj = ret;
        return ret;
    }

    private protected static AsyncResult forMessage(Message m) {
        AsyncResult ret = new AsyncResult(m.obj, null, null);
        m.obj = ret;
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AsyncResult(Object uo, Object r, Throwable ex) {
        this.userObj = uo;
        this.result = r;
        this.exception = ex;
    }
}
