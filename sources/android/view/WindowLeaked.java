package android.view;

import android.annotation.UnsupportedAppUsage;
import android.util.AndroidRuntimeException;

/* compiled from: WindowManagerGlobal.java */
/* loaded from: classes3.dex */
final class WindowLeaked extends AndroidRuntimeException {
    @UnsupportedAppUsage
    public WindowLeaked(String msg) {
        super(msg);
    }
}
