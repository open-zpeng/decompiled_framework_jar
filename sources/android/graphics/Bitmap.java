package android.graphics;

import android.content.res.ResourcesImpl;
import android.graphics.ColorSpace;
import android.graphics.NinePatch;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;
import android.os.Trace;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DisplayListCanvas;
import android.view.ThreadedRenderer;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import libcore.util.NativeAllocationRegistry;
/* loaded from: classes.dex */
public final class Bitmap implements Parcelable {
    public static final int DENSITY_NONE = 0;
    private static final long NATIVE_ALLOCATION_SIZE = 32;
    private static final String TAG = "Bitmap";
    private static final int WORKING_COMPRESS_STORAGE = 4096;
    public static volatile int sPreloadTracingNumInstantiatedBitmaps;
    public static volatile long sPreloadTracingTotalBitmapsSize;
    private ColorSpace mColorSpace;
    public int mDensity;
    public protected int mHeight;
    public protected final boolean mIsMutable;
    public protected final long mNativePtr;
    public protected byte[] mNinePatchChunk;
    public protected NinePatch.InsetStruct mNinePatchInsets;
    private boolean mRecycled;
    private boolean mRequestPremultiplied;
    public protected int mWidth;
    private static volatile int sDefaultDensity = -1;
    public static final Parcelable.Creator<Bitmap> CREATOR = new Parcelable.Creator<Bitmap>() { // from class: android.graphics.Bitmap.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Bitmap createFromParcel(Parcel p) {
            Bitmap bm = Bitmap.nativeCreateFromParcel(p);
            if (bm == null) {
                throw new RuntimeException("Failed to unparcel Bitmap");
            }
            return bm;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Bitmap[] newArray(int size) {
            return new Bitmap[size];
        }
    };

    private static native boolean nativeCompress(long j, int i, int i2, OutputStream outputStream, byte[] bArr);

    private static native int nativeConfig(long j);

    private static native Bitmap nativeCopy(long j, int i, boolean z);

    private static native Bitmap nativeCopyAshmem(long j);

    private static native Bitmap nativeCopyAshmemConfig(long j, int i);

    private static native void nativeCopyColorSpace(long j, long j2);

    private static native void nativeCopyPixelsFromBuffer(long j, Buffer buffer);

    private static native void nativeCopyPixelsToBuffer(long j, Buffer buffer);

    private static native Bitmap nativeCopyPreserveInternalConfig(long j);

    private static native Bitmap nativeCreate(int[] iArr, int i, int i2, int i3, int i4, int i5, boolean z, float[] fArr, ColorSpace.Rgb.TransferParameters transferParameters);

    /* JADX INFO: Access modifiers changed from: private */
    public static native Bitmap nativeCreateFromParcel(Parcel parcel);

    private static native GraphicBuffer nativeCreateGraphicBufferHandle(long j);

    private static native Bitmap nativeCreateHardwareBitmap(GraphicBuffer graphicBuffer);

    private static native void nativeErase(long j, int i);

    private static native Bitmap nativeExtractAlpha(long j, long j2, int[] iArr);

    private static native int nativeGenerationId(long j);

    private static native int nativeGetAllocationByteCount(long j);

    private static native boolean nativeGetColorSpace(long j, float[] fArr, float[] fArr2);

    private static native long nativeGetNativeFinalizer();

    private static native int nativeGetPixel(long j, int i, int i2);

    private static native void nativeGetPixels(long j, int[] iArr, int i, int i2, int i3, int i4, int i5, int i6);

    private static native boolean nativeHasAlpha(long j);

    private static native boolean nativeHasMipMap(long j);

    private static native boolean nativeIsPremultiplied(long j);

    private static native boolean nativeIsSRGB(long j);

    private static native boolean nativeIsSRGBLinear(long j);

    private static native void nativePrepareToDraw(long j);

    public protected static native void nativeReconfigure(long j, int i, int i2, int i3, boolean z);

    private static native boolean nativeRecycle(long j);

    private static native int nativeRowBytes(long j);

    private static native boolean nativeSameAs(long j, long j2);

    private static native void nativeSetHasAlpha(long j, boolean z, boolean z2);

    private static native void nativeSetHasMipMap(long j, boolean z);

    private static native void nativeSetPixel(long j, int i, int i2, int i3);

    private static native void nativeSetPixels(long j, int[] iArr, int i, int i2, int i3, int i4, int i5, int i6);

    private static native void nativeSetPremultiplied(long j, boolean z);

    private static native boolean nativeWriteToParcel(long j, boolean z, int i, Parcel parcel);

    /* JADX INFO: Access modifiers changed from: private */
    public static void setDefaultDensity(int density) {
        sDefaultDensity = density;
    }

    public private protected static int getDefaultDensity() {
        if (sDefaultDensity >= 0) {
            return sDefaultDensity;
        }
        sDefaultDensity = DisplayMetrics.DENSITY_DEVICE;
        return sDefaultDensity;
    }

    public private protected Bitmap(long nativeBitmap, int width, int height, int density, boolean isMutable, boolean requestPremultiplied, byte[] ninePatchChunk, NinePatch.InsetStruct ninePatchInsets) {
        this.mDensity = getDefaultDensity();
        if (nativeBitmap == 0) {
            throw new RuntimeException("internal error: native bitmap is 0");
        }
        this.mWidth = width;
        this.mHeight = height;
        this.mIsMutable = isMutable;
        this.mRequestPremultiplied = requestPremultiplied;
        this.mNinePatchChunk = ninePatchChunk;
        this.mNinePatchInsets = ninePatchInsets;
        if (density >= 0) {
            this.mDensity = density;
        }
        this.mNativePtr = nativeBitmap;
        long nativeSize = 32 + getAllocationByteCount();
        NativeAllocationRegistry registry = new NativeAllocationRegistry(Bitmap.class.getClassLoader(), nativeGetNativeFinalizer(), nativeSize);
        registry.registerNativeAllocation(this, nativeBitmap);
        if (ResourcesImpl.TRACE_FOR_DETAILED_PRELOAD) {
            sPreloadTracingNumInstantiatedBitmaps++;
            sPreloadTracingTotalBitmapsSize += nativeSize;
        }
    }

    public synchronized long getNativeInstance() {
        return this.mNativePtr;
    }

    public private protected void reinit(int width, int height, boolean requestPremultiplied) {
        this.mWidth = width;
        this.mHeight = height;
        this.mRequestPremultiplied = requestPremultiplied;
        this.mColorSpace = null;
    }

    public int getDensity() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getDensity() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return this.mDensity;
    }

    public void setDensity(int density) {
        this.mDensity = density;
    }

    public void reconfigure(int width, int height, Config config) {
        checkRecycled("Can't call reconfigure() on a recycled bitmap");
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be > 0");
        }
        if (!isMutable()) {
            throw new IllegalStateException("only mutable bitmaps may be reconfigured");
        }
        nativeReconfigure(this.mNativePtr, width, height, config.nativeInt, this.mRequestPremultiplied);
        this.mWidth = width;
        this.mHeight = height;
        this.mColorSpace = null;
    }

    public void setWidth(int width) {
        reconfigure(width, getHeight(), getConfig());
    }

    public void setHeight(int height) {
        reconfigure(getWidth(), height, getConfig());
    }

    public void setConfig(Config config) {
        reconfigure(getWidth(), getHeight(), config);
    }

    private protected void setNinePatchChunk(byte[] chunk) {
        this.mNinePatchChunk = chunk;
    }

    public void recycle() {
        if (!this.mRecycled && this.mNativePtr != 0) {
            if (nativeRecycle(this.mNativePtr)) {
                this.mNinePatchChunk = null;
            }
            this.mRecycled = true;
        }
    }

    public final boolean isRecycled() {
        return this.mRecycled;
    }

    public int getGenerationId() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getGenerationId() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeGenerationId(this.mNativePtr);
    }

    private synchronized void checkRecycled(String errorMessage) {
        if (this.mRecycled) {
            throw new IllegalStateException(errorMessage);
        }
    }

    private synchronized void checkHardware(String errorMessage) {
        if (getConfig() == Config.HARDWARE) {
            throw new IllegalStateException(errorMessage);
        }
    }

    private static synchronized void checkXYSign(int x, int y) {
        if (x < 0) {
            throw new IllegalArgumentException("x must be >= 0");
        }
        if (y < 0) {
            throw new IllegalArgumentException("y must be >= 0");
        }
    }

    private static synchronized void checkWidthHeight(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("width must be > 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("height must be > 0");
        }
    }

    /* loaded from: classes.dex */
    public enum Config {
        ALPHA_8(1),
        RGB_565(3),
        ARGB_4444(4),
        ARGB_8888(5),
        RGBA_F16(6),
        HARDWARE(7);
        
        public private protected final int nativeInt;
        private static Config[] sConfigs = {null, ALPHA_8, null, RGB_565, ARGB_4444, ARGB_8888, RGBA_F16, HARDWARE};

        Config(int ni) {
            this.nativeInt = ni;
        }

        public private protected static Config nativeToConfig(int ni) {
            return sConfigs[ni];
        }
    }

    public void copyPixelsToBuffer(Buffer dst) {
        int shift;
        checkHardware("unable to copyPixelsToBuffer, pixel access is not supported on Config#HARDWARE bitmaps");
        int elements = dst.remaining();
        if (dst instanceof ByteBuffer) {
            shift = 0;
        } else if (dst instanceof ShortBuffer) {
            shift = 1;
        } else if (dst instanceof IntBuffer) {
            shift = 2;
        } else {
            throw new RuntimeException("unsupported Buffer subclass");
        }
        long bufferSize = elements << shift;
        long pixelSize = getByteCount();
        if (bufferSize < pixelSize) {
            throw new RuntimeException("Buffer not large enough for pixels");
        }
        nativeCopyPixelsToBuffer(this.mNativePtr, dst);
        int position = dst.position();
        dst.position((int) (position + (pixelSize >> shift)));
    }

    public void copyPixelsFromBuffer(Buffer src) {
        int shift;
        checkRecycled("copyPixelsFromBuffer called on recycled bitmap");
        checkHardware("unable to copyPixelsFromBuffer, Config#HARDWARE bitmaps are immutable");
        int elements = src.remaining();
        if (src instanceof ByteBuffer) {
            shift = 0;
        } else if (src instanceof ShortBuffer) {
            shift = 1;
        } else if (src instanceof IntBuffer) {
            shift = 2;
        } else {
            throw new RuntimeException("unsupported Buffer subclass");
        }
        long bufferBytes = elements << shift;
        long bitmapBytes = getByteCount();
        if (bufferBytes < bitmapBytes) {
            throw new RuntimeException("Buffer not large enough for pixels");
        }
        nativeCopyPixelsFromBuffer(this.mNativePtr, src);
        int position = src.position();
        src.position((int) (position + (bitmapBytes >> shift)));
    }

    private synchronized void noteHardwareBitmapSlowCall() {
        if (getConfig() == Config.HARDWARE) {
            StrictMode.noteSlowCall("Warning: attempt to read pixels from hardware bitmap, which is very slow operation");
        }
    }

    public Bitmap copy(Config config, boolean isMutable) {
        checkRecycled("Can't copy a recycled bitmap");
        if (config == Config.HARDWARE && isMutable) {
            throw new IllegalArgumentException("Hardware bitmaps are always immutable");
        }
        noteHardwareBitmapSlowCall();
        Bitmap b = nativeCopy(this.mNativePtr, config.nativeInt, isMutable);
        if (b != null) {
            b.setPremultiplied(this.mRequestPremultiplied);
            b.mDensity = this.mDensity;
        }
        return b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap createAshmemBitmap() {
        checkRecycled("Can't copy a recycled bitmap");
        noteHardwareBitmapSlowCall();
        Bitmap b = nativeCopyAshmem(this.mNativePtr);
        if (b != null) {
            b.setPremultiplied(this.mRequestPremultiplied);
            b.mDensity = this.mDensity;
        }
        return b;
    }

    private protected Bitmap createAshmemBitmap(Config config) {
        checkRecycled("Can't copy a recycled bitmap");
        noteHardwareBitmapSlowCall();
        Bitmap b = nativeCopyAshmemConfig(this.mNativePtr, config.nativeInt);
        if (b != null) {
            b.setPremultiplied(this.mRequestPremultiplied);
            b.mDensity = this.mDensity;
        }
        return b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap createHardwareBitmap(GraphicBuffer graphicBuffer) {
        return nativeCreateHardwareBitmap(graphicBuffer);
    }

    public static Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        Matrix m = new Matrix();
        int width = src.getWidth();
        int height = src.getHeight();
        if (width != dstWidth || height != dstHeight) {
            float sx = dstWidth / width;
            float sy = dstHeight / height;
            m.setScale(sx, sy);
        }
        return createBitmap(src, 0, 0, width, height, m, filter);
    }

    public static Bitmap createBitmap(Bitmap src) {
        return createBitmap(src, 0, 0, src.getWidth(), src.getHeight());
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height) {
        return createBitmap(source, x, y, width, height, (Matrix) null, false);
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
        Bitmap bitmap;
        Paint paint;
        Bitmap source2 = source;
        checkXYSign(x, y);
        checkWidthHeight(width, height);
        if (x + width > source.getWidth()) {
            throw new IllegalArgumentException("x + width must be <= bitmap.width()");
        }
        if (y + height > source.getHeight()) {
            throw new IllegalArgumentException("y + height must be <= bitmap.height()");
        }
        if (!source.isMutable() && x == 0 && y == 0 && width == source.getWidth() && height == source.getHeight() && (m == null || m.isIdentity())) {
            return source2;
        }
        boolean isHardware = source.getConfig() == Config.HARDWARE;
        if (isHardware) {
            source.noteHardwareBitmapSlowCall();
            source2 = nativeCopyPreserveInternalConfig(source2.mNativePtr);
        }
        int neww = width;
        Rect srcR = new Rect(x, y, x + width, y + height);
        RectF dstR = new RectF(0.0f, 0.0f, width, height);
        RectF deviceR = new RectF();
        Config newConfig = Config.ARGB_8888;
        Config config = source2.getConfig();
        if (config != null) {
            switch (config) {
                case RGB_565:
                    newConfig = Config.RGB_565;
                    break;
                case ALPHA_8:
                    newConfig = Config.ALPHA_8;
                    break;
                case RGBA_F16:
                    newConfig = Config.RGBA_F16;
                    break;
                default:
                    newConfig = Config.ARGB_8888;
                    break;
            }
        }
        if (m == null || m.isIdentity()) {
            bitmap = createBitmap(neww, height, newConfig, source2.hasAlpha());
            paint = null;
        } else {
            boolean transformed = !m.rectStaysRect();
            m.mapRect(deviceR, dstR);
            neww = Math.round(deviceR.width());
            int newh = Math.round(deviceR.height());
            Config transformedConfig = newConfig;
            if (transformed && transformedConfig != Config.ARGB_8888 && transformedConfig != Config.RGBA_F16) {
                transformedConfig = Config.ARGB_8888;
            }
            bitmap = createBitmap(neww, newh, transformedConfig, transformed || source2.hasAlpha());
            paint = new Paint();
            paint.setFilterBitmap(filter);
            if (transformed) {
                paint.setAntiAlias(true);
            }
        }
        nativeCopyColorSpace(source2.mNativePtr, bitmap.mNativePtr);
        bitmap.mDensity = source2.mDensity;
        bitmap.setHasAlpha(source2.hasAlpha());
        bitmap.setPremultiplied(source2.mRequestPremultiplied);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(-deviceR.left, -deviceR.top);
        canvas.concat(m);
        canvas.drawBitmap(source2, srcR, dstR, paint);
        canvas.setBitmap(null);
        if (isHardware) {
            return bitmap.copy(Config.HARDWARE, false);
        }
        return bitmap;
    }

    public static Bitmap createBitmap(int width, int height, Config config) {
        return createBitmap(width, height, config, true);
    }

    public static Bitmap createBitmap(DisplayMetrics display, int width, int height, Config config) {
        return createBitmap(display, width, height, config, true);
    }

    public static Bitmap createBitmap(int width, int height, Config config, boolean hasAlpha) {
        return createBitmap((DisplayMetrics) null, width, height, config, hasAlpha);
    }

    public static Bitmap createBitmap(int width, int height, Config config, boolean hasAlpha, ColorSpace colorSpace) {
        return createBitmap((DisplayMetrics) null, width, height, config, hasAlpha, colorSpace);
    }

    public static Bitmap createBitmap(DisplayMetrics display, int width, int height, Config config, boolean hasAlpha) {
        return createBitmap(display, width, height, config, hasAlpha, ColorSpace.get(ColorSpace.Named.SRGB));
    }

    public static Bitmap createBitmap(DisplayMetrics display, int width, int height, Config config, boolean hasAlpha, ColorSpace colorSpace) {
        Bitmap bm;
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be > 0");
        }
        if (config != Config.HARDWARE) {
            if (colorSpace != null) {
                if (config != Config.ARGB_8888 || colorSpace == ColorSpace.get(ColorSpace.Named.SRGB)) {
                    bm = nativeCreate(null, 0, width, width, height, config.nativeInt, true, null, null);
                } else if (!(colorSpace instanceof ColorSpace.Rgb)) {
                    throw new IllegalArgumentException("colorSpace must be an RGB color space");
                } else {
                    ColorSpace.Rgb rgb = (ColorSpace.Rgb) colorSpace;
                    ColorSpace.Rgb.TransferParameters parameters = rgb.getTransferParameters();
                    if (parameters == null) {
                        throw new IllegalArgumentException("colorSpace must use an ICC parametric transfer function");
                    }
                    ColorSpace.Rgb d50 = (ColorSpace.Rgb) ColorSpace.adapt(rgb, ColorSpace.ILLUMINANT_D50);
                    bm = nativeCreate(null, 0, width, width, height, config.nativeInt, true, d50.getTransform(), parameters);
                }
                if (display != null) {
                    bm.mDensity = display.densityDpi;
                }
                bm.setHasAlpha(hasAlpha);
                if ((config == Config.ARGB_8888 || config == Config.RGBA_F16) && !hasAlpha) {
                    nativeErase(bm.mNativePtr, -16777216);
                }
                return bm;
            }
            throw new IllegalArgumentException("can't create bitmap without a color space");
        }
        throw new IllegalArgumentException("can't create mutable bitmap with Config.HARDWARE");
    }

    public static Bitmap createBitmap(int[] colors, int offset, int stride, int width, int height, Config config) {
        return createBitmap((DisplayMetrics) null, colors, offset, stride, width, height, config);
    }

    public static Bitmap createBitmap(DisplayMetrics display, int[] colors, int offset, int stride, int width, int height, Config config) {
        checkWidthHeight(width, height);
        if (Math.abs(stride) >= width) {
            int lastScanline = offset + ((height - 1) * stride);
            int length = colors.length;
            if (offset < 0 || offset + width > length || lastScanline < 0 || lastScanline + width > length) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (width > 0 && height > 0) {
                Bitmap bm = nativeCreate(colors, offset, stride, width, height, config.nativeInt, false, null, null);
                if (display != null) {
                    bm.mDensity = display.densityDpi;
                }
                return bm;
            }
            throw new IllegalArgumentException("width and height must be > 0");
        }
        throw new IllegalArgumentException("abs(stride) must be >= width");
    }

    public static Bitmap createBitmap(int[] colors, int width, int height, Config config) {
        return createBitmap((DisplayMetrics) null, colors, 0, width, width, height, config);
    }

    public static Bitmap createBitmap(DisplayMetrics display, int[] colors, int width, int height, Config config) {
        return createBitmap(display, colors, 0, width, width, height, config);
    }

    public static Bitmap createBitmap(Picture source) {
        return createBitmap(source, source.getWidth(), source.getHeight(), Config.HARDWARE);
    }

    public static Bitmap createBitmap(Picture source, int width, int height, Config config) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width & height must be > 0");
        }
        if (config == null) {
            throw new IllegalArgumentException("Config must not be null");
        }
        source.endRecording();
        if (source.requiresHardwareAcceleration() && config != Config.HARDWARE) {
            StrictMode.noteSlowCall("GPU readback");
        }
        if (config == Config.HARDWARE || source.requiresHardwareAcceleration()) {
            android.view.RenderNode node = android.view.RenderNode.create("BitmapTemporary", null);
            node.setLeftTopRightBottom(0, 0, width, height);
            node.setClipToBounds(false);
            DisplayListCanvas canvas = node.start(width, height);
            if (source.getWidth() != width || source.getHeight() != height) {
                canvas.scale(width / source.getWidth(), height / source.getHeight());
            }
            canvas.drawPicture(source);
            node.end(canvas);
            Bitmap bitmap = ThreadedRenderer.createHardwareBitmap(node, width, height);
            if (config != Config.HARDWARE) {
                return bitmap.copy(config, false);
            }
            return bitmap;
        }
        Bitmap bitmap2 = createBitmap(width, height, config);
        Canvas canvas2 = new Canvas(bitmap2);
        if (source.getWidth() != width || source.getHeight() != height) {
            canvas2.scale(width / source.getWidth(), height / source.getHeight());
        }
        canvas2.drawPicture(source);
        canvas2.setBitmap(null);
        bitmap2.makeImmutable();
        return bitmap2;
    }

    public byte[] getNinePatchChunk() {
        return this.mNinePatchChunk;
    }

    public synchronized void getOpticalInsets(Rect outInsets) {
        if (this.mNinePatchInsets == null) {
            outInsets.setEmpty();
        } else {
            outInsets.set(this.mNinePatchInsets.opticalRect);
        }
    }

    public synchronized NinePatch.InsetStruct getNinePatchInsets() {
        return this.mNinePatchInsets;
    }

    /* loaded from: classes.dex */
    public enum CompressFormat {
        JPEG(0),
        PNG(1),
        WEBP(2);
        
        final int nativeInt;

        CompressFormat(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }

    public boolean compress(CompressFormat format, int quality, OutputStream stream) {
        checkRecycled("Can't compress a recycled bitmap");
        if (stream == null) {
            throw new NullPointerException();
        }
        if (quality < 0 || quality > 100) {
            throw new IllegalArgumentException("quality must be 0..100");
        }
        StrictMode.noteSlowCall("Compression of a bitmap is slow");
        Trace.traceBegin(8192L, "Bitmap.compress");
        boolean result = nativeCompress(this.mNativePtr, format.nativeInt, quality, stream, new byte[4096]);
        Trace.traceEnd(8192L);
        return result;
    }

    public final boolean isMutable() {
        return this.mIsMutable;
    }

    public final synchronized void makeImmutable() {
    }

    public final boolean isPremultiplied() {
        if (this.mRecycled) {
            Log.w(TAG, "Called isPremultiplied() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeIsPremultiplied(this.mNativePtr);
    }

    public final void setPremultiplied(boolean premultiplied) {
        checkRecycled("setPremultiplied called on a recycled bitmap");
        this.mRequestPremultiplied = premultiplied;
        nativeSetPremultiplied(this.mNativePtr, premultiplied);
    }

    public final int getWidth() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getWidth() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return this.mWidth;
    }

    public final int getHeight() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getHeight() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return this.mHeight;
    }

    public int getScaledWidth(Canvas canvas) {
        return scaleFromDensity(getWidth(), this.mDensity, canvas.mDensity);
    }

    public int getScaledHeight(Canvas canvas) {
        return scaleFromDensity(getHeight(), this.mDensity, canvas.mDensity);
    }

    public int getScaledWidth(DisplayMetrics metrics) {
        return scaleFromDensity(getWidth(), this.mDensity, metrics.densityDpi);
    }

    public int getScaledHeight(DisplayMetrics metrics) {
        return scaleFromDensity(getHeight(), this.mDensity, metrics.densityDpi);
    }

    public int getScaledWidth(int targetDensity) {
        return scaleFromDensity(getWidth(), this.mDensity, targetDensity);
    }

    public int getScaledHeight(int targetDensity) {
        return scaleFromDensity(getHeight(), this.mDensity, targetDensity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int scaleFromDensity(int size, int sdensity, int tdensity) {
        if (sdensity == 0 || tdensity == 0 || sdensity == tdensity) {
            return size;
        }
        return ((size * tdensity) + (sdensity >> 1)) / sdensity;
    }

    public final int getRowBytes() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getRowBytes() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeRowBytes(this.mNativePtr);
    }

    public final int getByteCount() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getByteCount() on a recycle()'d bitmap! This is undefined behavior!");
            return 0;
        }
        return getRowBytes() * getHeight();
    }

    public final int getAllocationByteCount() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getAllocationByteCount() on a recycle()'d bitmap! This is undefined behavior!");
            return 0;
        }
        return nativeGetAllocationByteCount(this.mNativePtr);
    }

    public final Config getConfig() {
        if (this.mRecycled) {
            Log.w(TAG, "Called getConfig() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return Config.nativeToConfig(nativeConfig(this.mNativePtr));
    }

    public final boolean hasAlpha() {
        if (this.mRecycled) {
            Log.w(TAG, "Called hasAlpha() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeHasAlpha(this.mNativePtr);
    }

    public void setHasAlpha(boolean hasAlpha) {
        checkRecycled("setHasAlpha called on a recycled bitmap");
        nativeSetHasAlpha(this.mNativePtr, hasAlpha, this.mRequestPremultiplied);
    }

    public final boolean hasMipMap() {
        if (this.mRecycled) {
            Log.w(TAG, "Called hasMipMap() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return nativeHasMipMap(this.mNativePtr);
    }

    public final void setHasMipMap(boolean hasMipMap) {
        checkRecycled("setHasMipMap called on a recycled bitmap");
        nativeSetHasMipMap(this.mNativePtr, hasMipMap);
    }

    public final ColorSpace getColorSpace() {
        Bitmap bitmap;
        if (getConfig() == Config.RGBA_F16) {
            this.mColorSpace = null;
            return ColorSpace.get(ColorSpace.Named.LINEAR_EXTENDED_SRGB);
        }
        if (this.mColorSpace == null) {
            if (nativeIsSRGB(this.mNativePtr)) {
                this.mColorSpace = ColorSpace.get(ColorSpace.Named.SRGB);
            } else if (getConfig() == Config.HARDWARE && nativeIsSRGBLinear(this.mNativePtr)) {
                this.mColorSpace = ColorSpace.get(ColorSpace.Named.LINEAR_EXTENDED_SRGB);
            } else {
                float[] xyz = new float[9];
                float[] params = new float[7];
                boolean hasColorSpace = nativeGetColorSpace(this.mNativePtr, xyz, params);
                if (hasColorSpace) {
                    ColorSpace.Rgb.TransferParameters parameters = new ColorSpace.Rgb.TransferParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6]);
                    ColorSpace cs = ColorSpace.match(xyz, parameters);
                    if (cs != null) {
                        bitmap = this;
                        bitmap.mColorSpace = cs;
                    } else {
                        bitmap = this;
                        bitmap.mColorSpace = new ColorSpace.Rgb("Unknown", xyz, parameters);
                    }
                    return bitmap.mColorSpace;
                }
            }
        }
        bitmap = this;
        return bitmap.mColorSpace;
    }

    public void eraseColor(int c) {
        checkRecycled("Can't erase a recycled bitmap");
        if (!isMutable()) {
            throw new IllegalStateException("cannot erase immutable bitmaps");
        }
        nativeErase(this.mNativePtr, c);
    }

    public int getPixel(int x, int y) {
        checkRecycled("Can't call getPixel() on a recycled bitmap");
        checkHardware("unable to getPixel(), pixel access is not supported on Config#HARDWARE bitmaps");
        checkPixelAccess(x, y);
        return nativeGetPixel(this.mNativePtr, x, y);
    }

    public void getPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        checkRecycled("Can't call getPixels() on a recycled bitmap");
        checkHardware("unable to getPixels(), pixel access is not supported on Config#HARDWARE bitmaps");
        if (width == 0 || height == 0) {
            return;
        }
        checkPixelsAccess(x, y, width, height, offset, stride, pixels);
        nativeGetPixels(this.mNativePtr, pixels, offset, stride, x, y, width, height);
    }

    private synchronized void checkPixelAccess(int x, int y) {
        checkXYSign(x, y);
        if (x >= getWidth()) {
            throw new IllegalArgumentException("x must be < bitmap.width()");
        }
        if (y >= getHeight()) {
            throw new IllegalArgumentException("y must be < bitmap.height()");
        }
    }

    private synchronized void checkPixelsAccess(int x, int y, int width, int height, int offset, int stride, int[] pixels) {
        checkXYSign(x, y);
        if (width < 0) {
            throw new IllegalArgumentException("width must be >= 0");
        }
        if (height < 0) {
            throw new IllegalArgumentException("height must be >= 0");
        }
        if (x + width > getWidth()) {
            throw new IllegalArgumentException("x + width must be <= bitmap.width()");
        }
        if (y + height > getHeight()) {
            throw new IllegalArgumentException("y + height must be <= bitmap.height()");
        }
        if (Math.abs(stride) < width) {
            throw new IllegalArgumentException("abs(stride) must be >= width");
        }
        int lastScanline = ((height - 1) * stride) + offset;
        int length = pixels.length;
        if (offset < 0 || offset + width > length || lastScanline < 0 || lastScanline + width > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void setPixel(int x, int y, int color) {
        checkRecycled("Can't call setPixel() on a recycled bitmap");
        if (!isMutable()) {
            throw new IllegalStateException();
        }
        checkPixelAccess(x, y);
        nativeSetPixel(this.mNativePtr, x, y, color);
    }

    public void setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        checkRecycled("Can't call setPixels() on a recycled bitmap");
        if (!isMutable()) {
            throw new IllegalStateException();
        }
        if (width == 0 || height == 0) {
            return;
        }
        checkPixelsAccess(x, y, width, height, offset, stride, pixels);
        nativeSetPixels(this.mNativePtr, pixels, offset, stride, x, y, width, height);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        checkRecycled("Can't parcel a recycled bitmap");
        noteHardwareBitmapSlowCall();
        if (!nativeWriteToParcel(this.mNativePtr, this.mIsMutable, this.mDensity, p)) {
            throw new RuntimeException("native writeToParcel failed");
        }
    }

    public Bitmap extractAlpha() {
        return extractAlpha(null, null);
    }

    public Bitmap extractAlpha(Paint paint, int[] offsetXY) {
        checkRecycled("Can't extractAlpha on a recycled bitmap");
        long nativePaint = paint != null ? paint.getNativeInstance() : 0L;
        noteHardwareBitmapSlowCall();
        Bitmap bm = nativeExtractAlpha(this.mNativePtr, nativePaint, offsetXY);
        if (bm == null) {
            throw new RuntimeException("Failed to extractAlpha on Bitmap");
        }
        bm.mDensity = this.mDensity;
        return bm;
    }

    public boolean sameAs(Bitmap other) {
        checkRecycled("Can't call sameAs on a recycled bitmap!");
        noteHardwareBitmapSlowCall();
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        other.noteHardwareBitmapSlowCall();
        if (other.isRecycled()) {
            throw new IllegalArgumentException("Can't compare to a recycled bitmap!");
        }
        return nativeSameAs(this.mNativePtr, other.mNativePtr);
    }

    public void prepareToDraw() {
        checkRecycled("Can't prepareToDraw on a recycled bitmap!");
        nativePrepareToDraw(this.mNativePtr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public GraphicBuffer createGraphicBufferHandle() {
        return nativeCreateGraphicBufferHandle(this.mNativePtr);
    }
}
