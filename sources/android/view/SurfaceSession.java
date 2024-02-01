package android.view;
/* loaded from: classes2.dex */
public final class SurfaceSession {
    public protected long mNativeClient;

    private static native long nativeCreate();

    private static native long nativeCreateScoped(long j);

    private static native void nativeDestroy(long j);

    private static native void nativeKill(long j);

    private protected SurfaceSession() {
        this.mNativeClient = nativeCreate();
    }

    public synchronized SurfaceSession(Surface root) {
        this.mNativeClient = nativeCreateScoped(root.mNativeObject);
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mNativeClient != 0) {
                nativeDestroy(this.mNativeClient);
            }
        } finally {
            super.finalize();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void kill() {
        nativeKill(this.mNativeClient);
    }
}
