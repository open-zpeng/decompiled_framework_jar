package android.os;

import java.lang.ref.WeakReference;
/* loaded from: classes2.dex */
public class Registrant {
    WeakReference refH;
    Object userObj;
    int what;

    /* JADX INFO: Access modifiers changed from: private */
    public Registrant(Handler h, int what, Object obj) {
        this.refH = new WeakReference(h);
        this.what = what;
        this.userObj = obj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clear() {
        this.refH = null;
        this.userObj = null;
    }

    private protected void notifyRegistrant() {
        internalNotifyRegistrant(null, null);
    }

    private protected void notifyResult(Object result) {
        internalNotifyRegistrant(result, null);
    }

    public synchronized void notifyException(Throwable exception) {
        internalNotifyRegistrant(null, exception);
    }

    private protected void notifyRegistrant(AsyncResult ar) {
        internalNotifyRegistrant(ar.result, ar.exception);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void internalNotifyRegistrant(Object result, Throwable exception) {
        Handler h = getHandler();
        if (h == null) {
            clear();
            return;
        }
        Message msg = Message.obtain();
        msg.what = this.what;
        msg.obj = new AsyncResult(this.userObj, result, exception);
        h.sendMessage(msg);
    }

    private protected Message messageForRegistrant() {
        Handler h = getHandler();
        if (h == null) {
            clear();
            return null;
        }
        Message msg = h.obtainMessage();
        msg.what = this.what;
        msg.obj = this.userObj;
        return msg;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.refH == null) {
            return null;
        }
        return (Handler) this.refH.get();
    }
}
