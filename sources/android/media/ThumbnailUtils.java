package android.media;

import android.app.backup.FullBackup;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaFile;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
/* loaded from: classes.dex */
public class ThumbnailUtils {
    private static final int MAX_NUM_PIXELS_MICRO_THUMBNAIL = 19200;
    private static final int MAX_NUM_PIXELS_THUMBNAIL = 196608;
    private static final int OPTIONS_NONE = 0;
    public static final int OPTIONS_RECYCLE_INPUT = 2;
    private static final int OPTIONS_SCALE_UP = 1;
    private static final String TAG = "ThumbnailUtils";
    private protected static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;
    public static final int TARGET_SIZE_MINI_THUMBNAIL = 320;
    private static final int UNCONSTRAINED = -1;

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:38:0x0082 -> B:74:0x00df). Please submit an issue!!! */
    public static Bitmap createImageThumbnail(String filePath, int kind) {
        FileDescriptor fd;
        BitmapFactory.Options options;
        boolean wantMini = kind == 1;
        int targetSize = wantMini ? 320 : 96;
        int maxPixels = wantMini ? 196608 : MAX_NUM_PIXELS_MICRO_THUMBNAIL;
        SizedThumbnailBitmap sizedThumbnailBitmap = new SizedThumbnailBitmap();
        Bitmap bitmap = null;
        MediaFile.MediaFileType fileType = MediaFile.getFileType(filePath);
        if (fileType != null) {
            if (fileType.fileType == 31 || MediaFile.isRawImageFileType(fileType.fileType)) {
                createThumbnailFromEXIF(filePath, targetSize, maxPixels, sizedThumbnailBitmap);
                bitmap = sizedThumbnailBitmap.mBitmap;
            } else if (fileType.fileType == 37) {
                bitmap = createThumbnailFromMetadataRetriever(filePath, targetSize, maxPixels);
            }
        }
        if (bitmap == null) {
            FileInputStream stream = null;
            try {
                try {
                    try {
                        try {
                            stream = new FileInputStream(filePath);
                            fd = stream.getFD();
                            options = new BitmapFactory.Options();
                            options.inSampleSize = 1;
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFileDescriptor(fd, null, options);
                        } catch (OutOfMemoryError oom) {
                            Log.e(TAG, "Unable to decode file " + filePath + ". OutOfMemoryError.", oom);
                            if (stream != null) {
                                stream.close();
                            }
                        }
                    } catch (IOException ex) {
                        Log.e(TAG, "", ex);
                        if (stream != null) {
                            stream.close();
                        }
                    }
                } catch (IOException ex2) {
                    Log.e(TAG, "", ex2);
                }
                if (!options.mCancel && options.outWidth != -1 && options.outHeight != -1) {
                    options.inSampleSize = computeSampleSize(options, targetSize, maxPixels);
                    options.inJustDecodeBounds = false;
                    options.inDither = false;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
                    stream.close();
                }
                try {
                    stream.close();
                } catch (IOException ex3) {
                    Log.e(TAG, "", ex3);
                }
                return null;
            } catch (Throwable th) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ex4) {
                        Log.e(TAG, "", ex4);
                    }
                }
                throw th;
            }
        }
        return kind == 3 ? extractThumbnail(bitmap, 96, 96, 2) : bitmap;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:7:0x0014 -> B:19:0x0027). Please submit an issue!!! */
    public static Bitmap createVideoThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            try {
                try {
                    retriever.setDataSource(filePath);
                    bitmap = retriever.getFrameAtTime(-1L);
                    retriever.release();
                } catch (RuntimeException e) {
                    retriever.release();
                }
            } catch (IllegalArgumentException e2) {
                retriever.release();
            } catch (Throwable th) {
                try {
                    retriever.release();
                } catch (RuntimeException e3) {
                }
                throw th;
            }
        } catch (RuntimeException e4) {
        }
        if (bitmap == null) {
            return null;
        }
        if (kind != 1) {
            return kind == 3 ? extractThumbnail(bitmap, 96, 96, 2) : bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int max = Math.max(width, height);
        if (max > 512) {
            float scale = 512.0f / max;
            int w = Math.round(width * scale);
            int h = Math.round(height * scale);
            return Bitmap.createScaledBitmap(bitmap, w, h, true);
        }
        return bitmap;
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
        Bitmap thumbnail = transform(matrix, source, width, height, 1 | options);
        return thumbnail;
    }

    public protected static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        if (initialSize > 8) {
            int roundedSize = 8 * ((initialSize + 7) / 8);
            return roundedSize;
        }
        int roundedSize2 = 1;
        while (roundedSize2 < initialSize) {
            roundedSize2 <<= 1;
        }
        return roundedSize2;
    }

    public protected static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = maxNumOfPixels == -1 ? 1 : (int) Math.ceil(Math.sqrt((w * h) / maxNumOfPixels));
        int upperBound = minSideLength == -1 ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if (maxNumOfPixels == -1 && minSideLength == -1) {
            return 1;
        }
        if (minSideLength == -1) {
            return lowerBound;
        }
        return upperBound;
    }

    private static synchronized Bitmap makeBitmap(int minSideLength, int maxNumOfPixels, Uri uri, ContentResolver cr, ParcelFileDescriptor pfd, BitmapFactory.Options options) {
        if (pfd == null) {
            try {
                pfd = makeInputStream(uri, cr);
            } catch (OutOfMemoryError ex) {
                Log.e(TAG, "Got oom exception ", ex);
                return null;
            } finally {
                closeSilently(pfd);
            }
        }
        if (pfd == null) {
            return null;
        }
        if (options == null) {
            options = new BitmapFactory.Options();
        }
        FileDescriptor fd = pfd.getFileDescriptor();
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        if (!options.mCancel && options.outWidth != -1 && options.outHeight != -1) {
            options.inSampleSize = computeSampleSize(options, minSideLength, maxNumOfPixels);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap b = BitmapFactory.decodeFileDescriptor(fd, null, options);
            return b;
        }
        return null;
    }

    public protected static void closeSilently(ParcelFileDescriptor c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Throwable th) {
        }
    }

    public protected static ParcelFileDescriptor makeInputStream(Uri uri, ContentResolver cr) {
        try {
            return cr.openFileDescriptor(uri, FullBackup.ROOT_TREE_TOKEN);
        } catch (IOException e) {
            return null;
        }
    }

    public protected static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, int options) {
        Matrix scaler2 = scaler;
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
                scaler2.setScale(scale, scale);
            } else {
                scaler2 = null;
            }
        } else {
            float scale2 = targetWidth / bitmapWidthF;
            if (scale2 < 0.9f || scale2 > 1.0f) {
                scaler2.setScale(scale2, scale2);
            } else {
                scaler2 = null;
            }
        }
        Matrix scaler3 = scaler2;
        Bitmap b1 = scaler3 != null ? Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler3, true) : source;
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SizedThumbnailBitmap {
        public Bitmap mBitmap;
        public byte[] mThumbnailData;
        public int mThumbnailHeight;
        public int mThumbnailWidth;

        private synchronized SizedThumbnailBitmap() {
        }
    }

    public protected static void createThumbnailFromEXIF(String filePath, int targetSize, int maxPixels, SizedThumbnailBitmap sizedThumbBitmap) {
        if (filePath == null) {
            return;
        }
        byte[] thumbData = null;
        try {
            ExifInterface exif = new ExifInterface(filePath);
            thumbData = exif.getThumbnail();
        } catch (IOException ex) {
            Log.w(TAG, ex);
        }
        BitmapFactory.Options fullOptions = new BitmapFactory.Options();
        BitmapFactory.Options exifOptions = new BitmapFactory.Options();
        int exifThumbWidth = 0;
        if (thumbData != null) {
            exifOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(thumbData, 0, thumbData.length, exifOptions);
            exifOptions.inSampleSize = computeSampleSize(exifOptions, targetSize, maxPixels);
            exifThumbWidth = exifOptions.outWidth / exifOptions.inSampleSize;
        }
        fullOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, fullOptions);
        fullOptions.inSampleSize = computeSampleSize(fullOptions, targetSize, maxPixels);
        int fullThumbWidth = fullOptions.outWidth / fullOptions.inSampleSize;
        if (thumbData != null && exifThumbWidth >= fullThumbWidth) {
            int width = exifOptions.outWidth;
            int height = exifOptions.outHeight;
            exifOptions.inJustDecodeBounds = false;
            sizedThumbBitmap.mBitmap = BitmapFactory.decodeByteArray(thumbData, 0, thumbData.length, exifOptions);
            if (sizedThumbBitmap.mBitmap != null) {
                sizedThumbBitmap.mThumbnailData = thumbData;
                sizedThumbBitmap.mThumbnailWidth = width;
                sizedThumbBitmap.mThumbnailHeight = height;
                return;
            }
            return;
        }
        fullOptions.inJustDecodeBounds = false;
        sizedThumbBitmap.mBitmap = BitmapFactory.decodeFile(filePath, fullOptions);
    }

    private static synchronized Bitmap createThumbnailFromMetadataRetriever(String filePath, int targetSize, int maxPixels) {
        if (filePath == null) {
            return null;
        }
        Bitmap thumbnail = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            MediaMetadataRetriever.BitmapParams params = new MediaMetadataRetriever.BitmapParams();
            params.setPreferredConfig(Bitmap.Config.ARGB_8888);
            thumbnail = retriever.getThumbnailImageAtIndex(-1, params, targetSize, maxPixels);
        } catch (RuntimeException e) {
        } catch (Throwable th) {
            retriever.release();
            throw th;
        }
        retriever.release();
        return thumbnail;
    }
}
