package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.ColorSpace;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Size;
import android.util.TypedValue;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import libcore.io.IoUtils;

/* loaded from: classes.dex */
public final class ImageDecoder implements AutoCloseable {
    public static final int ALLOCATOR_DEFAULT = 0;
    public static final int ALLOCATOR_HARDWARE = 3;
    public static final int ALLOCATOR_SHARED_MEMORY = 2;
    public static final int ALLOCATOR_SOFTWARE = 1;
    @Deprecated
    public static final int ERROR_SOURCE_ERROR = 3;
    @Deprecated
    public static final int ERROR_SOURCE_EXCEPTION = 1;
    @Deprecated
    public static final int ERROR_SOURCE_INCOMPLETE = 2;
    public static final int MEMORY_POLICY_DEFAULT = 1;
    public static final int MEMORY_POLICY_LOW_RAM = 0;
    public static int sApiLevel;
    private final boolean mAnimated;
    private AssetFileDescriptor mAssetFd;
    private Rect mCropRect;
    private int mDesiredHeight;
    private int mDesiredWidth;
    private final int mHeight;
    private InputStream mInputStream;
    private final boolean mIsNinePatch;
    private long mNativePtr;
    private OnPartialImageListener mOnPartialImageListener;
    private Rect mOutPaddingRect;
    private boolean mOwnsInputStream;
    private PostProcessor mPostProcessor;
    private Source mSource;
    private byte[] mTempStorage;
    private final int mWidth;
    private int mAllocator = 0;
    private boolean mUnpremultipliedRequired = false;
    private boolean mMutable = false;
    private boolean mConserveMemory = false;
    private boolean mDecodeAsAlphaMask = false;
    private ColorSpace mDesiredColorSpace = null;
    private final AtomicBoolean mClosed = new AtomicBoolean();
    private final CloseGuard mCloseGuard = CloseGuard.get();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Allocator {
    }

    @Deprecated
    /* loaded from: classes.dex */
    public static class IncompleteException extends IOException {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface MemoryPolicy {
    }

    /* loaded from: classes.dex */
    public interface OnHeaderDecodedListener {
        void onHeaderDecoded(ImageDecoder imageDecoder, ImageInfo imageInfo, Source source);
    }

    /* loaded from: classes.dex */
    public interface OnPartialImageListener {
        boolean onPartialImage(DecodeException decodeException);
    }

    private static native void nClose(long j);

    private static native ImageDecoder nCreate(long j, Source source) throws IOException;

    private static native ImageDecoder nCreate(FileDescriptor fileDescriptor, Source source) throws IOException;

    private static native ImageDecoder nCreate(InputStream inputStream, byte[] bArr, Source source) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native ImageDecoder nCreate(ByteBuffer byteBuffer, int i, int i2, Source source) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native ImageDecoder nCreate(byte[] bArr, int i, int i2, Source source) throws IOException;

    private static native Bitmap nDecodeBitmap(long j, ImageDecoder imageDecoder, boolean z, int i, int i2, Rect rect, boolean z2, int i3, boolean z3, boolean z4, boolean z5, long j2, boolean z6) throws IOException;

    private static native ColorSpace nGetColorSpace(long j);

    private static native String nGetMimeType(long j);

    private static native void nGetPadding(long j, Rect rect);

    private static native Size nGetSampledSize(long j, int i);

    /* loaded from: classes.dex */
    public static abstract class Source {
        abstract ImageDecoder createImageDecoder() throws IOException;

        private Source() {
        }

        Resources getResources() {
            return null;
        }

        int getDensity() {
            return 0;
        }

        final int computeDstDensity() {
            Resources res = getResources();
            if (res == null) {
                return Bitmap.getDefaultDensity();
            }
            return res.getDisplayMetrics().densityDpi;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ByteArraySource extends Source {
        private final byte[] mData;
        private final int mLength;
        private final int mOffset;

        ByteArraySource(byte[] data, int offset, int length) {
            super();
            this.mData = data;
            this.mOffset = offset;
            this.mLength = length;
        }

        @Override // android.graphics.ImageDecoder.Source
        public ImageDecoder createImageDecoder() throws IOException {
            return ImageDecoder.nCreate(this.mData, this.mOffset, this.mLength, this);
        }
    }

    /* loaded from: classes.dex */
    private static class ByteBufferSource extends Source {
        private final ByteBuffer mBuffer;

        ByteBufferSource(ByteBuffer buffer) {
            super();
            this.mBuffer = buffer;
        }

        @Override // android.graphics.ImageDecoder.Source
        public ImageDecoder createImageDecoder() throws IOException {
            if (!this.mBuffer.isDirect() && this.mBuffer.hasArray()) {
                int offset = this.mBuffer.arrayOffset() + this.mBuffer.position();
                int length = this.mBuffer.limit() - this.mBuffer.position();
                return ImageDecoder.nCreate(this.mBuffer.array(), offset, length, this);
            }
            ByteBuffer buffer = this.mBuffer.slice();
            return ImageDecoder.nCreate(buffer, buffer.position(), buffer.limit(), this);
        }
    }

    /* loaded from: classes.dex */
    private static class ContentResolverSource extends Source {
        private final ContentResolver mResolver;
        private final Resources mResources;
        private final Uri mUri;

        ContentResolverSource(ContentResolver resolver, Uri uri, Resources res) {
            super();
            this.mResolver = resolver;
            this.mUri = uri;
            this.mResources = res;
        }

        @Override // android.graphics.ImageDecoder.Source
        Resources getResources() {
            return this.mResources;
        }

        @Override // android.graphics.ImageDecoder.Source
        public ImageDecoder createImageDecoder() throws IOException {
            AssetFileDescriptor assetFd;
            try {
                if (this.mUri.getScheme() == "content") {
                    assetFd = this.mResolver.openTypedAssetFileDescriptor(this.mUri, "image/*", null);
                } else {
                    assetFd = this.mResolver.openAssetFileDescriptor(this.mUri, "r");
                }
                return ImageDecoder.createFromAssetFileDescriptor(assetFd, this);
            } catch (FileNotFoundException e) {
                InputStream is = this.mResolver.openInputStream(this.mUri);
                if (is != null) {
                    return ImageDecoder.createFromStream(is, true, this);
                }
                throw new FileNotFoundException(this.mUri.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ImageDecoder createFromFile(File file, Source source) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        FileDescriptor fd = stream.getFD();
        try {
            Os.lseek(fd, 0L, OsConstants.SEEK_CUR);
            ImageDecoder decoder = null;
            try {
                decoder = nCreate(fd, source);
                return decoder;
            } finally {
                if (decoder == null) {
                    IoUtils.closeQuietly(stream);
                } else {
                    decoder.mInputStream = stream;
                    decoder.mOwnsInputStream = true;
                }
            }
        } catch (ErrnoException e) {
            return createFromStream(stream, true, source);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ImageDecoder createFromStream(InputStream is, boolean closeInputStream, Source source) throws IOException {
        byte[] storage = new byte[16384];
        ImageDecoder decoder = null;
        try {
            decoder = nCreate(is, storage, source);
            return decoder;
        } finally {
            if (decoder == null) {
                if (closeInputStream) {
                    IoUtils.closeQuietly(is);
                }
            } else {
                decoder.mInputStream = is;
                decoder.mOwnsInputStream = closeInputStream;
                decoder.mTempStorage = storage;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ImageDecoder createFromAssetFileDescriptor(AssetFileDescriptor assetFd, Source source) throws IOException {
        FileDescriptor fd = assetFd.getFileDescriptor();
        long offset = assetFd.getStartOffset();
        ImageDecoder decoder = null;
        try {
            try {
                Os.lseek(fd, offset, OsConstants.SEEK_SET);
                decoder = nCreate(fd, source);
            } catch (ErrnoException e) {
                decoder = createFromStream(new FileInputStream(fd), true, source);
            }
            if (decoder == null) {
                IoUtils.closeQuietly(assetFd);
            } else {
                decoder.mAssetFd = assetFd;
            }
            return decoder;
        } catch (Throwable th) {
            if (decoder == null) {
                IoUtils.closeQuietly(assetFd);
            } else {
                decoder.mAssetFd = assetFd;
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class InputStreamSource extends Source {
        final int mInputDensity;
        InputStream mInputStream;
        final Resources mResources;

        InputStreamSource(Resources res, InputStream is, int inputDensity) {
            super();
            if (is == null) {
                throw new IllegalArgumentException("The InputStream cannot be null");
            }
            this.mResources = res;
            this.mInputStream = is;
            this.mInputDensity = inputDensity;
        }

        @Override // android.graphics.ImageDecoder.Source
        public Resources getResources() {
            return this.mResources;
        }

        @Override // android.graphics.ImageDecoder.Source
        public int getDensity() {
            return this.mInputDensity;
        }

        @Override // android.graphics.ImageDecoder.Source
        public ImageDecoder createImageDecoder() throws IOException {
            ImageDecoder createFromStream;
            synchronized (this) {
                if (this.mInputStream == null) {
                    throw new IOException("Cannot reuse InputStreamSource");
                }
                InputStream is = this.mInputStream;
                this.mInputStream = null;
                createFromStream = ImageDecoder.createFromStream(is, false, this);
            }
            return createFromStream;
        }
    }

    /* loaded from: classes.dex */
    public static class AssetInputStreamSource extends Source {
        private AssetManager.AssetInputStream mAssetInputStream;
        private final int mDensity;
        private final Resources mResources;

        public AssetInputStreamSource(AssetManager.AssetInputStream ais, Resources res, TypedValue value) {
            super();
            this.mAssetInputStream = ais;
            this.mResources = res;
            if (value.density == 0) {
                this.mDensity = 160;
            } else if (value.density != 65535) {
                this.mDensity = value.density;
            } else {
                this.mDensity = 0;
            }
        }

        @Override // android.graphics.ImageDecoder.Source
        public Resources getResources() {
            return this.mResources;
        }

        @Override // android.graphics.ImageDecoder.Source
        public int getDensity() {
            return this.mDensity;
        }

        @Override // android.graphics.ImageDecoder.Source
        public ImageDecoder createImageDecoder() throws IOException {
            ImageDecoder createFromAsset;
            synchronized (this) {
                if (this.mAssetInputStream == null) {
                    throw new IOException("Cannot reuse AssetInputStreamSource");
                }
                AssetManager.AssetInputStream ais = this.mAssetInputStream;
                this.mAssetInputStream = null;
                createFromAsset = ImageDecoder.createFromAsset(ais, this);
            }
            return createFromAsset;
        }
    }

    /* loaded from: classes.dex */
    private static class ResourceSource extends Source {
        private Object mLock;
        int mResDensity;
        final int mResId;
        final Resources mResources;

        ResourceSource(Resources res, int resId) {
            super();
            this.mLock = new Object();
            this.mResources = res;
            this.mResId = resId;
            this.mResDensity = 0;
        }

        @Override // android.graphics.ImageDecoder.Source
        public Resources getResources() {
            return this.mResources;
        }

        @Override // android.graphics.ImageDecoder.Source
        public int getDensity() {
            int i;
            synchronized (this.mLock) {
                i = this.mResDensity;
            }
            return i;
        }

        @Override // android.graphics.ImageDecoder.Source
        public ImageDecoder createImageDecoder() throws IOException {
            TypedValue value = new TypedValue();
            InputStream is = this.mResources.openRawResource(this.mResId, value);
            synchronized (this.mLock) {
                if (value.density == 0) {
                    this.mResDensity = 160;
                } else if (value.density != 65535) {
                    this.mResDensity = value.density;
                }
            }
            return ImageDecoder.createFromAsset((AssetManager.AssetInputStream) is, this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ImageDecoder createFromAsset(AssetManager.AssetInputStream ais, Source source) throws IOException {
        ImageDecoder decoder = null;
        try {
            long asset = ais.getNativeAsset();
            decoder = nCreate(asset, source);
            return decoder;
        } finally {
            if (decoder == null) {
                IoUtils.closeQuietly(ais);
            } else {
                decoder.mInputStream = ais;
                decoder.mOwnsInputStream = true;
            }
        }
    }

    /* loaded from: classes.dex */
    private static class AssetSource extends Source {
        private final AssetManager mAssets;
        private final String mFileName;

        AssetSource(AssetManager assets, String fileName) {
            super();
            this.mAssets = assets;
            this.mFileName = fileName;
        }

        @Override // android.graphics.ImageDecoder.Source
        public ImageDecoder createImageDecoder() throws IOException {
            InputStream is = this.mAssets.open(this.mFileName);
            return ImageDecoder.createFromAsset((AssetManager.AssetInputStream) is, this);
        }
    }

    /* loaded from: classes.dex */
    private static class FileSource extends Source {
        private final File mFile;

        FileSource(File file) {
            super();
            this.mFile = file;
        }

        @Override // android.graphics.ImageDecoder.Source
        public ImageDecoder createImageDecoder() throws IOException {
            return ImageDecoder.createFromFile(this.mFile, this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CallableSource extends Source {
        private final Callable<AssetFileDescriptor> mCallable;

        CallableSource(Callable<AssetFileDescriptor> callable) {
            super();
            this.mCallable = callable;
        }

        @Override // android.graphics.ImageDecoder.Source
        public ImageDecoder createImageDecoder() throws IOException {
            try {
                AssetFileDescriptor assetFd = this.mCallable.call();
                return ImageDecoder.createFromAssetFileDescriptor(assetFd, this);
            } catch (Exception e) {
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                throw new IOException(e);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class ImageInfo {
        private ImageDecoder mDecoder;
        private final Size mSize;

        private ImageInfo(ImageDecoder decoder) {
            this.mSize = new Size(decoder.mWidth, decoder.mHeight);
            this.mDecoder = decoder;
        }

        public Size getSize() {
            return this.mSize;
        }

        public String getMimeType() {
            return this.mDecoder.getMimeType();
        }

        public boolean isAnimated() {
            return this.mDecoder.mAnimated;
        }

        public ColorSpace getColorSpace() {
            return this.mDecoder.getColorSpace();
        }
    }

    /* loaded from: classes.dex */
    public static final class DecodeException extends IOException {
        public static final int SOURCE_EXCEPTION = 1;
        public static final int SOURCE_INCOMPLETE = 2;
        public static final int SOURCE_MALFORMED_DATA = 3;
        final int mError;
        final Source mSource;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes.dex */
        public @interface Error {
        }

        DecodeException(int error, Throwable cause, Source source) {
            super(errorMessage(error, cause), cause);
            this.mError = error;
            this.mSource = source;
        }

        DecodeException(int error, String msg, Throwable cause, Source source) {
            super(msg + errorMessage(error, cause), cause);
            this.mError = error;
            this.mSource = source;
        }

        public int getError() {
            return this.mError;
        }

        public Source getSource() {
            return this.mSource;
        }

        private static String errorMessage(int error, Throwable cause) {
            if (error == 1) {
                return "Exception in input: " + cause;
            } else if (error != 2) {
                if (error == 3) {
                    return "Input contained an error.";
                }
                return "";
            } else {
                return "Input was incomplete.";
            }
        }
    }

    private ImageDecoder(long nativePtr, int width, int height, boolean animated, boolean isNinePatch) {
        this.mNativePtr = nativePtr;
        this.mWidth = width;
        this.mHeight = height;
        this.mDesiredWidth = width;
        this.mDesiredHeight = height;
        this.mAnimated = animated;
        this.mIsNinePatch = isNinePatch;
        this.mCloseGuard.open("close");
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            this.mInputStream = null;
            this.mAssetFd = null;
            close();
        } finally {
            super.finalize();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static boolean isMimeTypeSupported(String mimeType) {
        char c;
        Objects.requireNonNull(mimeType);
        String lowerCase = mimeType.toLowerCase(Locale.US);
        switch (lowerCase.hashCode()) {
            case -1875291391:
                if (lowerCase.equals("image/x-fuji-raf")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case -1635437028:
                if (lowerCase.equals("image/x-samsung-srw")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case -1594371159:
                if (lowerCase.equals("image/x-sony-arw")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -1487464693:
                if (lowerCase.equals("image/heic")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1487464690:
                if (lowerCase.equals("image/heif")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1487394660:
                if (lowerCase.equals("image/jpeg")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1487018032:
                if (lowerCase.equals("image/webp")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1423313290:
                if (lowerCase.equals("image/x-adobe-dng")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -985160897:
                if (lowerCase.equals("image/x-panasonic-rw2")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case -879272239:
                if (lowerCase.equals("image/bmp")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -879267568:
                if (lowerCase.equals("image/gif")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -879258763:
                if (lowerCase.equals("image/png")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -332763809:
                if (lowerCase.equals("image/x-pentax-pef")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case 741270252:
                if (lowerCase.equals("image/vnd.wap.wbmp")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1146342924:
                if (lowerCase.equals("image/x-ico")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1378106698:
                if (lowerCase.equals("image/x-olympus-orf")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 2099152104:
                if (lowerCase.equals("image/x-nikon-nef")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 2099152524:
                if (lowerCase.equals("image/x-nikon-nrw")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 2111234748:
                if (lowerCase.equals("image/x-canon-cr2")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case '\b':
            case '\t':
            case '\n':
            case 11:
            case '\f':
            case '\r':
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                return true;
            default:
                return false;
        }
    }

    public static Source createSource(Resources res, int resId) {
        return new ResourceSource(res, resId);
    }

    public static Source createSource(ContentResolver cr, Uri uri) {
        return new ContentResolverSource(cr, uri, null);
    }

    public static Source createSource(ContentResolver cr, Uri uri, Resources res) {
        return new ContentResolverSource(cr, uri, res);
    }

    public static Source createSource(AssetManager assets, String fileName) {
        return new AssetSource(assets, fileName);
    }

    public static Source createSource(byte[] data, int offset, int length) throws ArrayIndexOutOfBoundsException {
        if (data == null) {
            throw new NullPointerException("null byte[] in createSource!");
        }
        if (offset < 0 || length < 0 || offset >= data.length || offset + length > data.length) {
            throw new ArrayIndexOutOfBoundsException("invalid offset/length!");
        }
        return new ByteArraySource(data, offset, length);
    }

    public static Source createSource(byte[] data) {
        return createSource(data, 0, data.length);
    }

    public static Source createSource(ByteBuffer buffer) {
        return new ByteBufferSource(buffer);
    }

    public static Source createSource(Resources res, InputStream is) {
        return new InputStreamSource(res, is, Bitmap.getDefaultDensity());
    }

    public static Source createSource(Resources res, InputStream is, int density) {
        return new InputStreamSource(res, is, density);
    }

    public static Source createSource(File file) {
        return new FileSource(file);
    }

    public static Source createSource(Callable<AssetFileDescriptor> callable) {
        return new CallableSource(callable);
    }

    public Size getSampledSize(int sampleSize) {
        if (sampleSize <= 0) {
            throw new IllegalArgumentException("sampleSize must be positive! provided " + sampleSize);
        }
        long j = this.mNativePtr;
        if (j == 0) {
            throw new IllegalStateException("ImageDecoder is closed!");
        }
        return nGetSampledSize(j, sampleSize);
    }

    @Deprecated
    public ImageDecoder setResize(int width, int height) {
        setTargetSize(width, height);
        return this;
    }

    public void setTargetSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive! provided (" + width + ", " + height + ")");
        }
        this.mDesiredWidth = width;
        this.mDesiredHeight = height;
    }

    @Deprecated
    public ImageDecoder setResize(int sampleSize) {
        setTargetSampleSize(sampleSize);
        return this;
    }

    private int getTargetDimension(int original, int sampleSize, int computed) {
        if (sampleSize >= original) {
            return 1;
        }
        int target = original / sampleSize;
        if (computed == target) {
            return computed;
        }
        int reverse = computed * sampleSize;
        if (Math.abs(reverse - original) < sampleSize) {
            return computed;
        }
        return target;
    }

    public void setTargetSampleSize(int sampleSize) {
        Size size = getSampledSize(sampleSize);
        int targetWidth = getTargetDimension(this.mWidth, sampleSize, size.getWidth());
        int targetHeight = getTargetDimension(this.mHeight, sampleSize, size.getHeight());
        setTargetSize(targetWidth, targetHeight);
    }

    private boolean requestedResize() {
        return (this.mWidth == this.mDesiredWidth && this.mHeight == this.mDesiredHeight) ? false : true;
    }

    public void setAllocator(int allocator) {
        if (allocator < 0 || allocator > 3) {
            throw new IllegalArgumentException("invalid allocator " + allocator);
        }
        this.mAllocator = allocator;
    }

    public int getAllocator() {
        return this.mAllocator;
    }

    public void setUnpremultipliedRequired(boolean unpremultipliedRequired) {
        this.mUnpremultipliedRequired = unpremultipliedRequired;
    }

    @Deprecated
    public ImageDecoder setRequireUnpremultiplied(boolean unpremultipliedRequired) {
        setUnpremultipliedRequired(unpremultipliedRequired);
        return this;
    }

    public boolean isUnpremultipliedRequired() {
        return this.mUnpremultipliedRequired;
    }

    @Deprecated
    public boolean getRequireUnpremultiplied() {
        return isUnpremultipliedRequired();
    }

    public void setPostProcessor(PostProcessor postProcessor) {
        this.mPostProcessor = postProcessor;
    }

    public PostProcessor getPostProcessor() {
        return this.mPostProcessor;
    }

    public void setOnPartialImageListener(OnPartialImageListener listener) {
        this.mOnPartialImageListener = listener;
    }

    public OnPartialImageListener getOnPartialImageListener() {
        return this.mOnPartialImageListener;
    }

    public void setCrop(Rect subset) {
        this.mCropRect = subset;
    }

    public Rect getCrop() {
        return this.mCropRect;
    }

    public void setOutPaddingRect(Rect outPadding) {
        this.mOutPaddingRect = outPadding;
    }

    public void setMutableRequired(boolean mutable) {
        this.mMutable = mutable;
    }

    @Deprecated
    public ImageDecoder setMutable(boolean mutable) {
        setMutableRequired(mutable);
        return this;
    }

    public boolean isMutableRequired() {
        return this.mMutable;
    }

    @Deprecated
    public boolean getMutable() {
        return isMutableRequired();
    }

    public void setMemorySizePolicy(int policy) {
        this.mConserveMemory = policy == 0;
    }

    public int getMemorySizePolicy() {
        return !this.mConserveMemory ? 1 : 0;
    }

    @Deprecated
    public void setConserveMemory(boolean conserveMemory) {
        this.mConserveMemory = conserveMemory;
    }

    @Deprecated
    public boolean getConserveMemory() {
        return this.mConserveMemory;
    }

    public void setDecodeAsAlphaMaskEnabled(boolean enabled) {
        this.mDecodeAsAlphaMask = enabled;
    }

    @Deprecated
    public ImageDecoder setDecodeAsAlphaMask(boolean enabled) {
        setDecodeAsAlphaMaskEnabled(enabled);
        return this;
    }

    @Deprecated
    public ImageDecoder setAsAlphaMask(boolean asAlphaMask) {
        setDecodeAsAlphaMask(asAlphaMask);
        return this;
    }

    public boolean isDecodeAsAlphaMaskEnabled() {
        return this.mDecodeAsAlphaMask;
    }

    @Deprecated
    public boolean getDecodeAsAlphaMask() {
        return this.mDecodeAsAlphaMask;
    }

    @Deprecated
    public boolean getAsAlphaMask() {
        return getDecodeAsAlphaMask();
    }

    public void setTargetColorSpace(ColorSpace colorSpace) {
        this.mDesiredColorSpace = colorSpace;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mCloseGuard.close();
        if (!this.mClosed.compareAndSet(false, true)) {
            return;
        }
        nClose(this.mNativePtr);
        this.mNativePtr = 0L;
        if (this.mOwnsInputStream) {
            IoUtils.closeQuietly(this.mInputStream);
        }
        IoUtils.closeQuietly(this.mAssetFd);
        this.mInputStream = null;
        this.mAssetFd = null;
        this.mTempStorage = null;
    }

    private void checkState(boolean animated) {
        if (this.mNativePtr == 0) {
            throw new IllegalStateException("Cannot use closed ImageDecoder!");
        }
        checkSubset(this.mDesiredWidth, this.mDesiredHeight, this.mCropRect);
        if (!animated && this.mAllocator == 3) {
            if (this.mMutable) {
                throw new IllegalStateException("Cannot make mutable HARDWARE Bitmap!");
            }
            if (this.mDecodeAsAlphaMask) {
                throw new IllegalStateException("Cannot make HARDWARE Alpha mask Bitmap!");
            }
        }
        if (this.mPostProcessor != null && this.mUnpremultipliedRequired) {
            throw new IllegalStateException("Cannot draw to unpremultiplied pixels!");
        }
    }

    private static void checkSubset(int width, int height, Rect r) {
        if (r == null) {
            return;
        }
        if (r.left < 0 || r.top < 0 || r.right > width || r.bottom > height) {
            throw new IllegalStateException("Subset " + r + " not contained by scaled image bounds: (" + width + " x " + height + ")");
        }
    }

    private boolean checkForExtended() {
        ColorSpace colorSpace = this.mDesiredColorSpace;
        if (colorSpace == null) {
            return false;
        }
        return colorSpace == ColorSpace.get(ColorSpace.Named.EXTENDED_SRGB) || this.mDesiredColorSpace == ColorSpace.get(ColorSpace.Named.LINEAR_EXTENDED_SRGB);
    }

    private long getColorSpacePtr() {
        ColorSpace colorSpace = this.mDesiredColorSpace;
        if (colorSpace == null) {
            return 0L;
        }
        return colorSpace.getNativeInstance();
    }

    private Bitmap decodeBitmapInternal() throws IOException {
        checkState(false);
        return nDecodeBitmap(this.mNativePtr, this, this.mPostProcessor != null, this.mDesiredWidth, this.mDesiredHeight, this.mCropRect, this.mMutable, this.mAllocator, this.mUnpremultipliedRequired, this.mConserveMemory, this.mDecodeAsAlphaMask, getColorSpacePtr(), checkForExtended());
    }

    private void callHeaderDecoded(OnHeaderDecodedListener listener, Source src) {
        if (listener != null) {
            ImageInfo info = new ImageInfo();
            try {
                listener.onHeaderDecoded(this, info, src);
            } finally {
                info.mDecoder = null;
            }
        }
    }

    public static Drawable decodeDrawable(Source src, OnHeaderDecodedListener listener) throws IOException {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null! Use decodeDrawable(Source) to not have a listener");
        }
        return decodeDrawableImpl(src, listener);
    }

    private static Drawable decodeDrawableImpl(Source src, OnHeaderDecodedListener listener) throws IOException {
        ImageDecoder decoder = src.createImageDecoder();
        try {
            decoder.mSource = src;
            decoder.callHeaderDecoded(listener, src);
            if (decoder.mUnpremultipliedRequired) {
                throw new IllegalStateException("Cannot decode a Drawable with unpremultiplied pixels!");
            }
            if (decoder.mMutable) {
                throw new IllegalStateException("Cannot decode a mutable Drawable!");
            }
            int srcDensity = decoder.computeDensity(src);
            if (decoder.mAnimated) {
                ImageDecoder postProcessPtr = decoder.mPostProcessor == null ? null : decoder;
                decoder.checkState(true);
                Drawable d = new AnimatedImageDrawable(decoder.mNativePtr, postProcessPtr, decoder.mDesiredWidth, decoder.mDesiredHeight, decoder.getColorSpacePtr(), decoder.checkForExtended(), srcDensity, src.computeDstDensity(), decoder.mCropRect, decoder.mInputStream, decoder.mAssetFd);
                decoder.mInputStream = null;
                decoder.mAssetFd = null;
                $closeResource(null, decoder);
                return d;
            }
            Bitmap bm = decoder.decodeBitmapInternal();
            bm.setDensity(srcDensity);
            Resources res = src.getResources();
            byte[] np = bm.getNinePatchChunk();
            if (np == null || !NinePatch.isNinePatchChunk(np)) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(res, bm);
                $closeResource(null, decoder);
                return bitmapDrawable;
            }
            Rect opticalInsets = new Rect();
            bm.getOpticalInsets(opticalInsets);
            Rect padding = decoder.mOutPaddingRect;
            Rect padding2 = padding == null ? new Rect() : padding;
            nGetPadding(decoder.mNativePtr, padding2);
            NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(res, bm, np, padding2, opticalInsets, null);
            $closeResource(null, decoder);
            return ninePatchDrawable;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (decoder != null) {
                    $closeResource(th, decoder);
                }
                throw th2;
            }
        }
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    public static Drawable decodeDrawable(Source src) throws IOException {
        return decodeDrawableImpl(src, null);
    }

    public static Bitmap decodeBitmap(Source src, OnHeaderDecodedListener listener) throws IOException {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null! Use decodeBitmap(Source) to not have a listener");
        }
        return decodeBitmapImpl(src, listener);
    }

    private static Bitmap decodeBitmapImpl(Source src, OnHeaderDecodedListener listener) throws IOException {
        byte[] np;
        ImageDecoder decoder = src.createImageDecoder();
        try {
            decoder.mSource = src;
            decoder.callHeaderDecoded(listener, src);
            int srcDensity = decoder.computeDensity(src);
            Bitmap bm = decoder.decodeBitmapInternal();
            bm.setDensity(srcDensity);
            Rect padding = decoder.mOutPaddingRect;
            if (padding != null && (np = bm.getNinePatchChunk()) != null && NinePatch.isNinePatchChunk(np)) {
                nGetPadding(decoder.mNativePtr, padding);
            }
            $closeResource(null, decoder);
            return bm;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (decoder != null) {
                    $closeResource(th, decoder);
                }
                throw th2;
            }
        }
    }

    private int computeDensity(Source src) {
        if (requestedResize()) {
            return 0;
        }
        int srcDensity = src.getDensity();
        if (srcDensity == 0) {
            return srcDensity;
        }
        if (this.mIsNinePatch && this.mPostProcessor == null) {
            return srcDensity;
        }
        Resources res = src.getResources();
        if (res != null && res.getDisplayMetrics().noncompatDensityDpi == srcDensity) {
            return srcDensity;
        }
        int dstDensity = src.computeDstDensity();
        if (srcDensity == dstDensity) {
            return srcDensity;
        }
        if (srcDensity < dstDensity && sApiLevel >= 28) {
            return srcDensity;
        }
        float scale = dstDensity / srcDensity;
        int scaledWidth = Math.max((int) ((this.mWidth * scale) + 0.5f), 1);
        int scaledHeight = Math.max((int) ((this.mHeight * scale) + 0.5f), 1);
        setTargetSize(scaledWidth, scaledHeight);
        return dstDensity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getMimeType() {
        return nGetMimeType(this.mNativePtr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ColorSpace getColorSpace() {
        return nGetColorSpace(this.mNativePtr);
    }

    public static Bitmap decodeBitmap(Source src) throws IOException {
        return decodeBitmapImpl(src, null);
    }

    @UnsupportedAppUsage
    private int postProcessAndRelease(Canvas canvas) {
        try {
            return this.mPostProcessor.onPostProcess(canvas);
        } finally {
            canvas.release();
        }
    }

    private void onPartialImage(int error, Throwable cause) throws DecodeException {
        DecodeException exception = new DecodeException(error, cause, this.mSource);
        OnPartialImageListener onPartialImageListener = this.mOnPartialImageListener;
        if (onPartialImageListener == null || !onPartialImageListener.onPartialImage(exception)) {
            throw exception;
        }
    }
}
