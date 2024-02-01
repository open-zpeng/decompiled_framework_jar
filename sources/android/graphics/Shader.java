package android.graphics;

import android.annotation.UnsupportedAppUsage;
import libcore.util.NativeAllocationRegistry;

/* loaded from: classes.dex */
public class Shader {
    private Runnable mCleaner;
    private final ColorSpace mColorSpace;
    private Matrix mLocalMatrix;
    private long mNativeInstance;

    private static native long nativeGetFinalizer();

    static /* synthetic */ long access$000() {
        return nativeGetFinalizer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class NoImagePreloadHolder {
        public static final NativeAllocationRegistry sRegistry = NativeAllocationRegistry.createMalloced(Shader.class.getClassLoader(), Shader.access$000());

        private NoImagePreloadHolder() {
        }
    }

    @Deprecated
    public Shader() {
        this.mColorSpace = null;
    }

    public Shader(ColorSpace colorSpace) {
        this.mColorSpace = colorSpace;
        if (colorSpace == null) {
            throw new IllegalArgumentException("Use Shader() to create a Shader with no ColorSpace");
        }
        this.mColorSpace.getNativeInstance();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ColorSpace colorSpace() {
        return this.mColorSpace;
    }

    /* loaded from: classes.dex */
    public enum TileMode {
        CLAMP(0),
        REPEAT(1),
        MIRROR(2);
        
        @UnsupportedAppUsage
        final int nativeInt;

        TileMode(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }

    public boolean getLocalMatrix(Matrix localM) {
        Matrix matrix = this.mLocalMatrix;
        if (matrix != null) {
            localM.set(matrix);
            return true;
        }
        return false;
    }

    public void setLocalMatrix(Matrix localM) {
        if (localM == null || localM.isIdentity()) {
            if (this.mLocalMatrix != null) {
                this.mLocalMatrix = null;
                discardNativeInstance();
                return;
            }
            return;
        }
        Matrix matrix = this.mLocalMatrix;
        if (matrix == null) {
            this.mLocalMatrix = new Matrix(localM);
            discardNativeInstance();
        } else if (!matrix.equals(localM)) {
            this.mLocalMatrix.set(localM);
            discardNativeInstance();
        }
    }

    long createNativeInstance(long nativeMatrix) {
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void discardNativeInstance() {
        if (this.mNativeInstance != 0) {
            this.mCleaner.run();
            this.mCleaner = null;
            this.mNativeInstance = 0L;
        }
    }

    protected void verifyNativeInstance() {
    }

    public final long getNativeInstance() {
        verifyNativeInstance();
        if (this.mNativeInstance == 0) {
            Matrix matrix = this.mLocalMatrix;
            this.mNativeInstance = createNativeInstance(matrix == null ? 0L : matrix.native_instance);
            if (this.mNativeInstance != 0) {
                this.mCleaner = NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, this.mNativeInstance);
            }
        }
        return this.mNativeInstance;
    }

    public static long[] convertColors(int[] colors) {
        if (colors.length < 2) {
            throw new IllegalArgumentException("needs >= 2 number of colors");
        }
        long[] colorLongs = new long[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colorLongs[i] = Color.pack(colors[i]);
        }
        return colorLongs;
    }

    public static ColorSpace detectColorSpace(long[] colors) {
        if (colors.length < 2) {
            throw new IllegalArgumentException("needs >= 2 number of colors");
        }
        ColorSpace colorSpace = Color.colorSpace(colors[0]);
        for (int i = 1; i < colors.length; i++) {
            if (Color.colorSpace(colors[i]) != colorSpace) {
                throw new IllegalArgumentException("All colors must be in the same ColorSpace!");
            }
        }
        return colorSpace;
    }
}
