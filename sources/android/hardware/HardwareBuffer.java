package android.hardware;

import android.annotation.UnsupportedAppUsage;
import android.graphics.GraphicBuffer;
import android.os.Parcel;
import android.os.Parcelable;
import dalvik.annotation.optimization.FastNative;
import dalvik.system.CloseGuard;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import libcore.util.NativeAllocationRegistry;

/* loaded from: classes.dex */
public final class HardwareBuffer implements Parcelable, AutoCloseable {
    public static final int BLOB = 33;
    public static final Parcelable.Creator<HardwareBuffer> CREATOR = new Parcelable.Creator<HardwareBuffer>() { // from class: android.hardware.HardwareBuffer.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HardwareBuffer createFromParcel(Parcel in) {
            long nativeObject = HardwareBuffer.nReadHardwareBufferFromParcel(in);
            if (nativeObject != 0) {
                return new HardwareBuffer(nativeObject);
            }
            return null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HardwareBuffer[] newArray(int size) {
            return new HardwareBuffer[size];
        }
    };
    public static final int DS_24UI8 = 50;
    public static final int DS_FP32UI8 = 52;
    public static final int D_16 = 48;
    public static final int D_24 = 49;
    public static final int D_FP32 = 51;
    private static final long NATIVE_HARDWARE_BUFFER_SIZE = 232;
    public static final int RGBA_1010102 = 43;
    public static final int RGBA_8888 = 1;
    public static final int RGBA_FP16 = 22;
    public static final int RGBX_8888 = 2;
    public static final int RGB_565 = 4;
    public static final int RGB_888 = 3;
    public static final int S_UI8 = 53;
    public static final long USAGE_CPU_READ_OFTEN = 3;
    public static final long USAGE_CPU_READ_RARELY = 2;
    public static final long USAGE_CPU_WRITE_OFTEN = 48;
    public static final long USAGE_CPU_WRITE_RARELY = 32;
    public static final long USAGE_GPU_COLOR_OUTPUT = 512;
    public static final long USAGE_GPU_CUBE_MAP = 33554432;
    public static final long USAGE_GPU_DATA_BUFFER = 16777216;
    public static final long USAGE_GPU_MIPMAP_COMPLETE = 67108864;
    public static final long USAGE_GPU_SAMPLED_IMAGE = 256;
    public static final long USAGE_PROTECTED_CONTENT = 16384;
    public static final long USAGE_SENSOR_DIRECT_DATA = 8388608;
    public static final long USAGE_VIDEO_ENCODE = 65536;
    private Runnable mCleaner;
    private final CloseGuard mCloseGuard;
    @UnsupportedAppUsage
    private long mNativeObject;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Format {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Usage {
    }

    private static native long nCreateFromGraphicBuffer(GraphicBuffer graphicBuffer);

    private static native long nCreateHardwareBuffer(int i, int i2, int i3, int i4, long j);

    @FastNative
    private static native int nGetFormat(long j);

    @FastNative
    private static native int nGetHeight(long j);

    @FastNative
    private static native int nGetLayers(long j);

    private static native long nGetNativeFinalizer();

    @FastNative
    private static native long nGetUsage(long j);

    @FastNative
    private static native int nGetWidth(long j);

    private static native boolean nIsSupported(int i, int i2, int i3, int i4, long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nReadHardwareBufferFromParcel(Parcel parcel);

    private static native void nWriteHardwareBufferToParcel(long j, Parcel parcel);

    public static HardwareBuffer create(int width, int height, int format, int layers, long usage) {
        if (!isSupportedFormat(format)) {
            throw new IllegalArgumentException("Invalid pixel format " + format);
        } else if (width <= 0) {
            throw new IllegalArgumentException("Invalid width " + width);
        } else if (height <= 0) {
            throw new IllegalArgumentException("Invalid height " + height);
        } else if (layers <= 0) {
            throw new IllegalArgumentException("Invalid layer count " + layers);
        } else if (format == 33 && height != 1) {
            throw new IllegalArgumentException("Height must be 1 when using the BLOB format");
        } else {
            long nativeObject = nCreateHardwareBuffer(width, height, format, layers, usage);
            if (nativeObject == 0) {
                throw new IllegalArgumentException("Unable to create a HardwareBuffer, either the dimensions passed were too large, too many image layers were requested, or an invalid set of usage flags or invalid format was passed");
            }
            return new HardwareBuffer(nativeObject);
        }
    }

    public static boolean isSupported(int width, int height, int format, int layers, long usage) {
        if (!isSupportedFormat(format)) {
            throw new IllegalArgumentException("Invalid pixel format " + format);
        } else if (width <= 0) {
            throw new IllegalArgumentException("Invalid width " + width);
        } else if (height <= 0) {
            throw new IllegalArgumentException("Invalid height " + height);
        } else if (layers <= 0) {
            throw new IllegalArgumentException("Invalid layer count " + layers);
        } else if (format == 33 && height != 1) {
            throw new IllegalArgumentException("Height must be 1 when using the BLOB format");
        } else {
            return nIsSupported(width, height, format, layers, usage);
        }
    }

    public static HardwareBuffer createFromGraphicBuffer(GraphicBuffer graphicBuffer) {
        long nativeObject = nCreateFromGraphicBuffer(graphicBuffer);
        return new HardwareBuffer(nativeObject);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private HardwareBuffer(long nativeObject) {
        this.mCloseGuard = CloseGuard.get();
        this.mNativeObject = nativeObject;
        ClassLoader loader = HardwareBuffer.class.getClassLoader();
        NativeAllocationRegistry registry = new NativeAllocationRegistry(loader, nGetNativeFinalizer(), (long) NATIVE_HARDWARE_BUFFER_SIZE);
        this.mCleaner = registry.registerNativeAllocation(this, this.mNativeObject);
        this.mCloseGuard.open("close");
    }

    protected void finalize() throws Throwable {
        try {
            this.mCloseGuard.warnIfOpen();
            close();
        } finally {
            super.finalize();
        }
    }

    public int getWidth() {
        if (isClosed()) {
            throw new IllegalStateException("This HardwareBuffer has been closed and its width cannot be obtained.");
        }
        return nGetWidth(this.mNativeObject);
    }

    public int getHeight() {
        if (isClosed()) {
            throw new IllegalStateException("This HardwareBuffer has been closed and its height cannot be obtained.");
        }
        return nGetHeight(this.mNativeObject);
    }

    public int getFormat() {
        if (isClosed()) {
            throw new IllegalStateException("This HardwareBuffer has been closed and its format cannot be obtained.");
        }
        return nGetFormat(this.mNativeObject);
    }

    public int getLayers() {
        if (isClosed()) {
            throw new IllegalStateException("This HardwareBuffer has been closed and its layer count cannot be obtained.");
        }
        return nGetLayers(this.mNativeObject);
    }

    public long getUsage() {
        if (isClosed()) {
            throw new IllegalStateException("This HardwareBuffer has been closed and its usage cannot be obtained.");
        }
        return nGetUsage(this.mNativeObject);
    }

    @Deprecated
    public void destroy() {
        close();
    }

    @Deprecated
    public boolean isDestroyed() {
        return isClosed();
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        if (!isClosed()) {
            this.mCloseGuard.close();
            this.mNativeObject = 0L;
            this.mCleaner.run();
            this.mCleaner = null;
        }
    }

    public boolean isClosed() {
        return this.mNativeObject == 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 1;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (isClosed()) {
            throw new IllegalStateException("This HardwareBuffer has been closed and cannot be written to a parcel.");
        }
        nWriteHardwareBufferToParcel(this.mNativeObject, dest);
    }

    private static boolean isSupportedFormat(int format) {
        if (format != 1 && format != 2 && format != 3 && format != 4 && format != 22 && format != 33 && format != 43) {
            switch (format) {
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
