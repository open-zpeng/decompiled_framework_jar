package android.view;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes3.dex */
public final class SurfaceSession {
    @UnsupportedAppUsage
    private long mNativeClient = nativeCreate();

    private static native long nativeCreate();

    private static native void nativeDestroy(long j);

    private static native void nativeKill(long j);

    protected void finalize() throws Throwable {
        try {
            if (this.mNativeClient != 0) {
                nativeDestroy(this.mNativeClient);
            }
        } finally {
            super.finalize();
        }
    }

    @UnsupportedAppUsage
    public void kill() {
        nativeKill(this.mNativeClient);
    }
}
