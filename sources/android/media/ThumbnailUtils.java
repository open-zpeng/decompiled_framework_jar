package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import com.android.internal.util.ArrayUtils;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.ToIntFunction;
import libcore.io.IoUtils;

/* loaded from: classes2.dex */
public class ThumbnailUtils {
    private static final int OPTIONS_NONE = 0;
    public static final int OPTIONS_RECYCLE_INPUT = 2;
    private static final int OPTIONS_SCALE_UP = 1;
    private static final String TAG = "ThumbnailUtils";
    @UnsupportedAppUsage
    @Deprecated
    public static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;

    private static Size convertKind(int kind) {
        if (kind == 3) {
            return Point.convert(MediaStore.ThumbnailConstants.MICRO_SIZE);
        }
        if (kind == 2) {
            return Point.convert(MediaStore.ThumbnailConstants.FULL_SCREEN_SIZE);
        }
        if (kind == 1) {
            return Point.convert(MediaStore.ThumbnailConstants.MINI_SIZE);
        }
        throw new IllegalArgumentException("Unsupported kind: " + kind);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class Resizer implements ImageDecoder.OnHeaderDecodedListener {
        private final CancellationSignal signal;
        private final Size size;

        public Resizer(Size size, CancellationSignal signal) {
            this.size = size;
            this.signal = signal;
        }

        @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
        public void onHeaderDecoded(ImageDecoder decoder, ImageDecoder.ImageInfo info, ImageDecoder.Source source) {
            CancellationSignal cancellationSignal = this.signal;
            if (cancellationSignal != null) {
                cancellationSignal.throwIfCanceled();
            }
            decoder.setAllocator(1);
            int widthSample = info.getSize().getWidth() / this.size.getWidth();
            int heightSample = info.getSize().getHeight() / this.size.getHeight();
            int sample = Math.max(widthSample, heightSample);
            if (sample > 1) {
                decoder.setTargetSampleSize(sample);
            }
        }
    }

    @Deprecated
    public static Bitmap createAudioThumbnail(String filePath, int kind) {
        try {
            return createAudioThumbnail(new File(filePath), convertKind(kind), null);
        } catch (IOException e) {
            Log.w(TAG, e);
            return null;
        }
    }

    public static Bitmap createAudioThumbnail(File file, Size size, CancellationSignal signal) throws IOException {
        if (signal != null) {
            signal.throwIfCanceled();
        }
        Resizer resizer = new Resizer(size, signal);
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(file.getAbsolutePath());
            byte[] raw = retriever.getEmbeddedPicture();
            if (raw != null) {
                Bitmap decodeBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(raw), resizer);
                $closeResource(null, retriever);
                return decodeBitmap;
            }
            $closeResource(null, retriever);
            if ("unknown".equals(Environment.getExternalStorageState(file))) {
                throw new IOException("No embedded album art found");
            }
            File parent = file.getParentFile();
            File grandParent = parent != null ? parent.getParentFile() : null;
            if (parent != null && parent.getName().equals(Environment.DIRECTORY_DOWNLOADS)) {
                throw new IOException("No thumbnails in Downloads directories");
            }
            if (grandParent != null && "unknown".equals(Environment.getExternalStorageState(grandParent))) {
                throw new IOException("No thumbnails in top-level directories");
            }
            File[] found = ArrayUtils.defeatNullable(file.getParentFile().listFiles(new FilenameFilter() { // from class: android.media.-$$Lambda$ThumbnailUtils$P13h9YbyD69p6ss1gYpoef43_MU
                @Override // java.io.FilenameFilter
                public final boolean accept(File file2, String str) {
                    return ThumbnailUtils.lambda$createAudioThumbnail$0(file2, str);
                }
            }));
            final ToIntFunction<File> score = new ToIntFunction() { // from class: android.media.-$$Lambda$ThumbnailUtils$qOH5vebuTwPi2G92PTa6rgwKGoc
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ThumbnailUtils.lambda$createAudioThumbnail$1((File) obj);
                }
            };
            Comparator<File> bestScore = new Comparator() { // from class: android.media.-$$Lambda$ThumbnailUtils$HhGKNQZck57eO__Paj6KyQm6lCk
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return ThumbnailUtils.lambda$createAudioThumbnail$2(score, (File) obj, (File) obj2);
                }
            };
            File bestFile = (File) Arrays.asList(found).stream().max(bestScore).orElse(null);
            if (bestFile == null) {
                throw new IOException("No album art found");
            }
            if (signal != null) {
                signal.throwIfCanceled();
            }
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(bestFile), resizer);
        } catch (RuntimeException e) {
            throw new IOException("Failed to create thumbnail", e);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$createAudioThumbnail$0(File dir, String name) {
        String lower = name.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".png");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$createAudioThumbnail$1(File f) {
        String lower = f.getName().toLowerCase();
        if (lower.equals("albumart.jpg")) {
            return 4;
        }
        if (lower.startsWith("albumart") && lower.endsWith(".jpg")) {
            return 3;
        }
        if (lower.contains("albumart") && lower.endsWith(".jpg")) {
            return 2;
        }
        return lower.endsWith(".jpg") ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$createAudioThumbnail$2(ToIntFunction score, File a, File b) {
        return score.applyAsInt(a) - score.applyAsInt(b);
    }

    @Deprecated
    public static Bitmap createImageThumbnail(String filePath, int kind) {
        try {
            return createImageThumbnail(new File(filePath), convertKind(kind), null);
        } catch (IOException e) {
            Log.w(TAG, e);
            return null;
        }
    }

    public static Bitmap createImageThumbnail(File file, Size size, CancellationSignal signal) throws IOException {
        ExifInterface exif;
        byte[] raw;
        if (signal != null) {
            signal.throwIfCanceled();
        }
        Resizer resizer = new Resizer(size, signal);
        String mimeType = MediaFile.getMimeTypeForFile(file.getName());
        Bitmap bitmap = null;
        int orientation = 0;
        if (!MediaFile.isExifMimeType(mimeType)) {
            exif = null;
        } else {
            ExifInterface exif2 = new ExifInterface(file);
            int attributeInt = exif2.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            if (attributeInt == 3) {
                orientation = 180;
                exif = exif2;
            } else if (attributeInt == 6) {
                orientation = 90;
                exif = exif2;
            } else if (attributeInt != 8) {
                exif = exif2;
            } else {
                orientation = 270;
                exif = exif2;
            }
        }
        if (mimeType.equals("image/heif") || mimeType.equals("image/heif-sequence") || mimeType.equals("image/heic") || mimeType.equals("image/heic-sequence")) {
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(file.getAbsolutePath());
                bitmap = retriever.getThumbnailImageAtIndex(-1, new MediaMetadataRetriever.BitmapParams(), size.getWidth(), size.getWidth() * size.getHeight());
                $closeResource(null, retriever);
            } catch (RuntimeException e) {
                throw new IOException("Failed to create thumbnail", e);
            }
        }
        if (bitmap == null && exif != null && (raw = exif.getThumbnailBytes()) != null) {
            try {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(raw), resizer);
            } catch (ImageDecoder.DecodeException e2) {
                Log.w(TAG, e2);
            }
        }
        if (signal != null) {
            signal.throwIfCanceled();
        }
        if (bitmap == null) {
            Bitmap bitmap2 = ImageDecoder.decodeBitmap(ImageDecoder.createSource(file), resizer);
            return bitmap2;
        } else if (orientation != 0) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix m = new Matrix();
            m.setRotate(orientation, width / 2, height / 2);
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, m, false);
        } else {
            return bitmap;
        }
    }

    @Deprecated
    public static Bitmap createVideoThumbnail(String filePath, int kind) {
        try {
            return createVideoThumbnail(new File(filePath), convertKind(kind), null);
        } catch (IOException e) {
            Log.w(TAG, e);
            return null;
        }
    }

    public static Bitmap createVideoThumbnail(File file, Size size, CancellationSignal signal) throws IOException {
        if (signal != null) {
            signal.throwIfCanceled();
        }
        Resizer resizer = new Resizer(size, signal);
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(file.getAbsolutePath());
            byte[] raw = mmr.getEmbeddedPicture();
            if (raw != null) {
                Bitmap decodeBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(raw), resizer);
                $closeResource(null, mmr);
                return decodeBitmap;
            }
            int width = Integer.parseInt(mmr.extractMetadata(18));
            int height = Integer.parseInt(mmr.extractMetadata(19));
            long duration = Long.parseLong(mmr.extractMetadata(9));
            if (size.getWidth() <= width || size.getHeight() <= height) {
                Bitmap bitmap = (Bitmap) Objects.requireNonNull(mmr.getScaledFrameAtTime(duration / 2, 2, size.getWidth(), size.getHeight()));
                $closeResource(null, mmr);
                return bitmap;
            }
            Bitmap bitmap2 = (Bitmap) Objects.requireNonNull(mmr.getFrameAtTime(duration / 2, 2));
            $closeResource(null, mmr);
            return bitmap2;
        } catch (RuntimeException e) {
            throw new IOException("Failed to create thumbnail", e);
        }
    }

    public static Bitmap extractThumbnail(Bitmap source, int width, int height) {
        return extractThumbnail(source, width, height, 0);
    }

    public static Bitmap extractThumbnail(Bitmap source, int width, int height, int options) {
        float scale;
        if (source == null) {
            return null;
        }
        if (source.getWidth() < source.getHeight()) {
            scale = width / source.getWidth();
        } else {
            float scale2 = height;
            scale = scale2 / source.getHeight();
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        Bitmap thumbnail = transform(matrix, source, width, height, options | 1);
        return thumbnail;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    @Deprecated
    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        return 1;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    @Deprecated
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        return 1;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    @Deprecated
    private static void closeSilently(ParcelFileDescriptor c) {
        IoUtils.closeQuietly(c);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    @Deprecated
    private static ParcelFileDescriptor makeInputStream(Uri uri, ContentResolver cr) {
        try {
            return cr.openFileDescriptor(uri, "r");
        } catch (IOException e) {
            return null;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    @Deprecated
    private static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, int options) {
        Matrix scaler2;
        Matrix scaler3 = scaler;
        boolean scaleUp = (options & 1) != 0;
        boolean recycle = (options & 2) != 0;
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
            Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b2);
            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect src = new Rect(deltaXHalf, deltaYHalf, Math.min(targetWidth, source.getWidth()) + deltaXHalf, Math.min(targetHeight, source.getHeight()) + deltaYHalf);
            int dstX = (targetWidth - src.width()) / 2;
            int dstY = (targetHeight - src.height()) / 2;
            Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY);
            c.drawBitmap(source, src, dst, (Paint) null);
            if (recycle) {
                source.recycle();
            }
            c.setBitmap(null);
            return b2;
        }
        float bitmapWidthF = source.getWidth();
        float bitmapHeightF = source.getHeight();
        float bitmapAspect = bitmapWidthF / bitmapHeightF;
        float viewAspect = targetWidth / targetHeight;
        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeightF;
            if (scale < 0.9f || scale > 1.0f) {
                scaler3.setScale(scale, scale);
            } else {
                scaler3 = null;
            }
            scaler2 = scaler3;
        } else {
            float scale2 = targetWidth / bitmapWidthF;
            if (scale2 < 0.9f || scale2 > 1.0f) {
                scaler3.setScale(scale2, scale2);
                scaler2 = scaler3;
            } else {
                scaler2 = null;
            }
        }
        Bitmap b1 = scaler2 != null ? Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler2, true) : source;
        if (recycle && b1 != source) {
            source.recycle();
        }
        int dx1 = Math.max(0, b1.getWidth() - targetWidth);
        int dy1 = Math.max(0, b1.getHeight() - targetHeight);
        Bitmap b22 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth, targetHeight);
        if (b22 != b1 && (recycle || b1 != source)) {
            b1.recycle();
        }
        return b22;
    }

    @Deprecated
    /* loaded from: classes2.dex */
    private static class SizedThumbnailBitmap {
        public Bitmap mBitmap;
        public byte[] mThumbnailData;
        public int mThumbnailHeight;
        public int mThumbnailWidth;

        private SizedThumbnailBitmap() {
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    @Deprecated
    private static void createThumbnailFromEXIF(String filePath, int targetSize, int maxPixels, SizedThumbnailBitmap sizedThumbBitmap) {
    }
}
