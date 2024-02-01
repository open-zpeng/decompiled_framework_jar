package android.os;

import android.annotation.UnsupportedAppUsage;

/* compiled from: ZygoteProcess.java */
/* loaded from: classes2.dex */
class ZygoteStartFailedEx extends Exception {
    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public ZygoteStartFailedEx(String s) {
        super(s);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public ZygoteStartFailedEx(Throwable cause) {
        super(cause);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ZygoteStartFailedEx(String s, Throwable cause) {
        super(s, cause);
    }
}
