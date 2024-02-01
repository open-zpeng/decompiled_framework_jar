package android.os;

import java.util.ArrayList;
/* loaded from: classes2.dex */
public class RegistrantList {
    ArrayList registrants = new ArrayList();

    private protected RegistrantList() {
    }

    private protected synchronized void add(Handler h, int what, Object obj) {
        add(new Registrant(h, what, obj));
    }

    private protected synchronized void addUnique(Handler h, int what, Object obj) {
        remove(h);
        add(new Registrant(h, what, obj));
    }

    private protected synchronized void add(Registrant r) {
        removeCleared();
        this.registrants.add(r);
    }

    private protected synchronized void removeCleared() {
        for (int i = this.registrants.size() - 1; i >= 0; i--) {
            Registrant r = (Registrant) this.registrants.get(i);
            if (r.refH == null) {
                this.registrants.remove(i);
            }
        }
    }

    private protected synchronized int size() {
        return this.registrants.size();
    }

    private protected synchronized Object get(int index) {
        return this.registrants.get(index);
    }

    private synchronized void internalNotifyRegistrants(Object result, Throwable exception) {
        int s = this.registrants.size();
        for (int i = 0; i < s; i++) {
            Registrant r = (Registrant) this.registrants.get(i);
            r.internalNotifyRegistrant(result, exception);
        }
    }

    private protected void notifyRegistrants() {
        internalNotifyRegistrants(null, null);
    }

    public synchronized void notifyException(Throwable exception) {
        internalNotifyRegistrants(null, exception);
    }

    private protected void notifyResult(Object result) {
        internalNotifyRegistrants(result, null);
    }

    private protected void notifyRegistrants(AsyncResult ar) {
        internalNotifyRegistrants(ar.result, ar.exception);
    }

    private protected synchronized void remove(Handler h) {
        int s = this.registrants.size();
        for (int i = 0; i < s; i++) {
            Registrant r = (Registrant) this.registrants.get(i);
            Handler rh = r.getHandler();
            if (rh == null || rh == h) {
                r.clear();
            }
        }
        removeCleared();
    }
}
