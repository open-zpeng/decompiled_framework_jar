package android.graphics;

import libcore.util.NativeAllocationRegistry;
/* loaded from: classes.dex */
public class Shader {
    private Runnable mCleaner;
    private Matrix mLocalMatrix;
    private long mNativeInstance;

    private static native long nativeGetFinalizer();

    static /* synthetic */ long access$000() {
        return nativeGetFinalizer();
    }

    /* loaded from: classes.dex */
    private static class NoImagePreloadHolder {
        public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(Shader.class.getClassLoader(), Shader.access$000(), 50);

        private synchronized NoImagePreloadHolder() {
        }
    }

    /* loaded from: classes.dex */
    public enum TileMode {
        CLAMP(0),
        REPEAT(1),
        MIRROR(2);
        
        public private protected final int nativeInt;

        TileMode(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }

    public boolean getLocalMatrix(Matrix localM) {
        if (this.mLocalMatrix != null) {
            localM.set(this.mLocalMatrix);
            return true;
        }
        return false;
    }

    public void setLocalMatrix(Matrix localM) {
        if (localM == null || localM.isIdentity()) {
            if (this.mLocalMatrix != null) {
                this.mLocalMatrix = null;
                discardNativeInstance();
            }
        } else if (this.mLocalMatrix == null) {
            this.mLocalMatrix = new Matrix(localM);
            discardNativeInstance();
        } else if (!this.mLocalMatrix.equals(localM)) {
            this.mLocalMatrix.set(localM);
            discardNativeInstance();
        }
    }

    synchronized long createNativeInstance(long nativeMatrix) {
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized void discardNativeInstance() {
        if (this.mNativeInstance != 0) {
            this.mCleaner.run();
            this.mCleaner = null;
            this.mNativeInstance = 0L;
        }
    }

    protected synchronized void verifyNativeInstance() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized Shader copy() {
        Shader copy = new Shader();
        copyLocalMatrix(copy);
        return copy;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void copyLocalMatrix(Shader dest) {
        dest.mLocalMatrix.set(this.mLocalMatrix);
    }

    public final synchronized long getNativeInstance() {
        long j;
        verifyNativeInstance();
        if (this.mNativeInstance == 0) {
            if (this.mLocalMatrix == null) {
                j = 0;
            } else {
                j = this.mLocalMatrix.native_instance;
            }
            this.mNativeInstance = createNativeInstance(j);
            if (this.mNativeInstance != 0) {
                this.mCleaner = NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, this.mNativeInstance);
            }
        }
        return this.mNativeInstance;
    }
}
