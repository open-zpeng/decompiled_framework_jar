package android.opengl;
/* loaded from: classes2.dex */
public abstract class EGLObjectHandle {
    private final long mHandle;

    @Deprecated
    protected EGLObjectHandle(int handle) {
        this.mHandle = handle;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EGLObjectHandle(long handle) {
        this.mHandle = handle;
    }

    @Deprecated
    public int getHandle() {
        if ((this.mHandle & 4294967295L) != this.mHandle) {
            throw new UnsupportedOperationException();
        }
        return (int) this.mHandle;
    }

    public long getNativeHandle() {
        return this.mHandle;
    }

    public int hashCode() {
        int result = (31 * 17) + ((int) (this.mHandle ^ (this.mHandle >>> 32)));
        return result;
    }
}
