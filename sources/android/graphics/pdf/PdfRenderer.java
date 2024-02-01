package android.graphics.pdf;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import libcore.io.IoUtils;
/* loaded from: classes.dex */
public final class PdfRenderer implements AutoCloseable {
    static final Object sPdfiumLock = new Object();
    public protected Page mCurrentPage;
    private ParcelFileDescriptor mInput;
    private long mNativeDocument;
    private final int mPageCount;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final Point mTempPoint = new Point();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface RenderMode {
    }

    private static native void nativeClose(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeClosePage(long j);

    private static native long nativeCreate(int i, long j);

    private static native int nativeGetPageCount(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenPageAndGetSize(long j, int i, Point point);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeRenderPage(long j, long j2, Bitmap bitmap, int i, int i2, int i3, int i4, long j3, int i5);

    private static native boolean nativeScaleForPrinting(long j);

    public PdfRenderer(ParcelFileDescriptor input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input cannot be null");
        }
        try {
            Os.lseek(input.getFileDescriptor(), 0L, OsConstants.SEEK_SET);
            long size = Os.fstat(input.getFileDescriptor()).st_size;
            this.mInput = input;
            synchronized (sPdfiumLock) {
                this.mNativeDocument = nativeCreate(this.mInput.getFd(), size);
                this.mPageCount = nativeGetPageCount(this.mNativeDocument);
            }
            this.mCloseGuard.open("close");
        } catch (ErrnoException e) {
            throw new IllegalArgumentException("file descriptor not seekable");
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        throwIfClosed();
        throwIfPageOpened();
        doClose();
    }

    public int getPageCount() {
        throwIfClosed();
        return this.mPageCount;
    }

    public boolean shouldScaleForPrinting() {
        boolean nativeScaleForPrinting;
        throwIfClosed();
        synchronized (sPdfiumLock) {
            nativeScaleForPrinting = nativeScaleForPrinting(this.mNativeDocument);
        }
        return nativeScaleForPrinting;
    }

    public Page openPage(int index) {
        throwIfClosed();
        throwIfPageOpened();
        throwIfPageNotInDocument(index);
        this.mCurrentPage = new Page(index);
        return this.mCurrentPage;
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            doClose();
        } finally {
            super.finalize();
        }
    }

    public protected void doClose() {
        if (this.mCurrentPage != null) {
            this.mCurrentPage.close();
            this.mCurrentPage = null;
        }
        if (this.mNativeDocument != 0) {
            synchronized (sPdfiumLock) {
                nativeClose(this.mNativeDocument);
            }
            this.mNativeDocument = 0L;
        }
        if (this.mInput != null) {
            IoUtils.closeQuietly(this.mInput);
            this.mInput = null;
        }
        this.mCloseGuard.close();
    }

    private synchronized void throwIfClosed() {
        if (this.mInput == null) {
            throw new IllegalStateException("Already closed");
        }
    }

    private synchronized void throwIfPageOpened() {
        if (this.mCurrentPage != null) {
            throw new IllegalStateException("Current page not closed");
        }
    }

    private synchronized void throwIfPageNotInDocument(int pageIndex) {
        if (pageIndex < 0 || pageIndex >= this.mPageCount) {
            throw new IllegalArgumentException("Invalid page index");
        }
    }

    /* loaded from: classes.dex */
    public final class Page implements AutoCloseable {
        public static final int RENDER_MODE_FOR_DISPLAY = 1;
        public static final int RENDER_MODE_FOR_PRINT = 2;
        private final CloseGuard mCloseGuard;
        private final int mHeight;
        private final int mIndex;
        private long mNativePage;
        private final int mWidth;

        private Page(int index) {
            this.mCloseGuard = CloseGuard.get();
            Point size = PdfRenderer.this.mTempPoint;
            synchronized (PdfRenderer.sPdfiumLock) {
                this.mNativePage = PdfRenderer.nativeOpenPageAndGetSize(PdfRenderer.this.mNativeDocument, index, size);
            }
            this.mIndex = index;
            this.mWidth = size.x;
            this.mHeight = size.y;
            this.mCloseGuard.open("close");
        }

        public int getIndex() {
            return this.mIndex;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public void render(Bitmap destination, Rect destClip, Matrix transform, int renderMode) {
            Matrix transform2;
            if (this.mNativePage == 0) {
                throw new NullPointerException();
            }
            Bitmap destination2 = (Bitmap) Preconditions.checkNotNull(destination, "bitmap null");
            if (destination2.getConfig() != Bitmap.Config.ARGB_8888) {
                throw new IllegalArgumentException("Unsupported pixel format");
            }
            if (destClip != null && (destClip.left < 0 || destClip.top < 0 || destClip.right > destination2.getWidth() || destClip.bottom > destination2.getHeight())) {
                throw new IllegalArgumentException("destBounds not in destination");
            }
            if (transform != null && !transform.isAffine()) {
                throw new IllegalArgumentException("transform not affine");
            }
            if (renderMode != 2 && renderMode != 1) {
                throw new IllegalArgumentException("Unsupported render mode");
            }
            if (renderMode == 2 && renderMode == 1) {
                throw new IllegalArgumentException("Only single render mode supported");
            }
            int contentLeft = destClip != null ? destClip.left : 0;
            int contentTop = destClip != null ? destClip.top : 0;
            int contentRight = destClip != null ? destClip.right : destination2.getWidth();
            int contentBottom = destClip != null ? destClip.bottom : destination2.getHeight();
            if (transform == null) {
                int clipWidth = contentRight - contentLeft;
                int clipHeight = contentBottom - contentTop;
                Matrix transform3 = new Matrix();
                transform3.postScale(clipWidth / getWidth(), clipHeight / getHeight());
                transform3.postTranslate(contentLeft, contentTop);
                transform2 = transform3;
            } else {
                transform2 = transform;
            }
            long transformPtr = transform2.native_instance;
            synchronized (PdfRenderer.sPdfiumLock) {
                try {
                    try {
                        PdfRenderer.nativeRenderPage(PdfRenderer.this.mNativeDocument, this.mNativePage, destination2, contentLeft, contentTop, contentRight, contentBottom, transformPtr, renderMode);
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            throwIfClosed();
            doClose();
        }

        protected void finalize() throws Throwable {
            try {
                if (this.mCloseGuard != null) {
                    this.mCloseGuard.warnIfOpen();
                }
                doClose();
            } finally {
                super.finalize();
            }
        }

        private synchronized void doClose() {
            if (this.mNativePage != 0) {
                synchronized (PdfRenderer.sPdfiumLock) {
                    PdfRenderer.nativeClosePage(this.mNativePage);
                }
                this.mNativePage = 0L;
            }
            this.mCloseGuard.close();
            PdfRenderer.this.mCurrentPage = null;
        }

        private synchronized void throwIfClosed() {
            if (this.mNativePage == 0) {
                throw new IllegalStateException("Already closed");
            }
        }
    }
}
