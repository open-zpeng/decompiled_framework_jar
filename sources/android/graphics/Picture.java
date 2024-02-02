package android.graphics;

import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: classes.dex */
public class Picture {
    private static final int WORKING_STREAM_STORAGE = 16384;
    public protected long mNativePicture;
    private PictureCanvas mRecordingCanvas;
    private boolean mRequiresHwAcceleration;

    private static native long nativeBeginRecording(long j, int i, int i2);

    private static native long nativeConstructor(long j);

    private static native long nativeCreateFromStream(InputStream inputStream, byte[] bArr);

    private static native void nativeDestructor(long j);

    private static native void nativeDraw(long j, long j2);

    private static native void nativeEndRecording(long j);

    private static native int nativeGetHeight(long j);

    private static native int nativeGetWidth(long j);

    private static native boolean nativeWriteToStream(long j, OutputStream outputStream, byte[] bArr);

    public Picture() {
        this(nativeConstructor(0L));
    }

    public Picture(Picture src) {
        this(nativeConstructor(src != null ? src.mNativePicture : 0L));
    }

    private synchronized Picture(long nativePicture) {
        if (nativePicture == 0) {
            throw new RuntimeException();
        }
        this.mNativePicture = nativePicture;
    }

    protected void finalize() throws Throwable {
        try {
            nativeDestructor(this.mNativePicture);
            this.mNativePicture = 0L;
        } finally {
            super.finalize();
        }
    }

    public Canvas beginRecording(int width, int height) {
        if (this.mRecordingCanvas != null) {
            throw new IllegalStateException("Picture already recording, must call #endRecording()");
        }
        long ni = nativeBeginRecording(this.mNativePicture, width, height);
        this.mRecordingCanvas = new PictureCanvas(this, ni);
        this.mRequiresHwAcceleration = false;
        return this.mRecordingCanvas;
    }

    public void endRecording() {
        if (this.mRecordingCanvas != null) {
            this.mRequiresHwAcceleration = this.mRecordingCanvas.mHoldsHwBitmap;
            this.mRecordingCanvas = null;
            nativeEndRecording(this.mNativePicture);
        }
    }

    public int getWidth() {
        return nativeGetWidth(this.mNativePicture);
    }

    public int getHeight() {
        return nativeGetHeight(this.mNativePicture);
    }

    public boolean requiresHardwareAcceleration() {
        return this.mRequiresHwAcceleration;
    }

    public void draw(Canvas canvas) {
        if (this.mRecordingCanvas != null) {
            endRecording();
        }
        if (this.mRequiresHwAcceleration && !canvas.isHardwareAccelerated()) {
            canvas.onHwBitmapInSwMode();
        }
        nativeDraw(canvas.getNativeCanvasWrapper(), this.mNativePicture);
    }

    @Deprecated
    public static Picture createFromStream(InputStream stream) {
        return new Picture(nativeCreateFromStream(stream, new byte[16384]));
    }

    @Deprecated
    public void writeToStream(OutputStream stream) {
        if (stream == null) {
            throw new NullPointerException();
        }
        if (!nativeWriteToStream(this.mNativePicture, stream, new byte[16384])) {
            throw new RuntimeException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PictureCanvas extends Canvas {
        boolean mHoldsHwBitmap;
        private final Picture mPicture;

        public synchronized PictureCanvas(Picture pict, long nativeCanvas) {
            super(nativeCanvas);
            this.mPicture = pict;
            this.mDensity = 0;
        }

        @Override // android.graphics.Canvas
        public void setBitmap(Bitmap bitmap) {
            throw new RuntimeException("Cannot call setBitmap on a picture canvas");
        }

        @Override // android.graphics.Canvas
        public void drawPicture(Picture picture) {
            if (this.mPicture == picture) {
                throw new RuntimeException("Cannot draw a picture into its recording canvas");
            }
            super.drawPicture(picture);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.graphics.BaseCanvas
        public synchronized void onHwBitmapInSwMode() {
            this.mHoldsHwBitmap = true;
        }
    }
}
