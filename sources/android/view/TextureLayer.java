package android.view;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import com.android.internal.util.VirtualRefBasePtr;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class TextureLayer {
    private VirtualRefBasePtr mFinalizer;
    private ThreadedRenderer mRenderer;

    private static native boolean nPrepare(long j, int i, int i2, boolean z);

    private static native void nSetLayerPaint(long j, long j2);

    private static native void nSetSurfaceTexture(long j, SurfaceTexture surfaceTexture);

    private static native void nSetTransform(long j, long j2);

    private static native void nUpdateSurfaceTexture(long j);

    private synchronized TextureLayer(ThreadedRenderer renderer, long deferredUpdater) {
        if (renderer == null || deferredUpdater == 0) {
            throw new IllegalArgumentException("Either hardware renderer: " + renderer + " or deferredUpdater: " + deferredUpdater + " is invalid");
        }
        this.mRenderer = renderer;
        this.mFinalizer = new VirtualRefBasePtr(deferredUpdater);
    }

    public synchronized void setLayerPaint(Paint paint) {
        nSetLayerPaint(this.mFinalizer.get(), paint != null ? paint.getNativeInstance() : 0L);
        this.mRenderer.pushLayerUpdate(this);
    }

    public synchronized boolean isValid() {
        return (this.mFinalizer == null || this.mFinalizer.get() == 0) ? false : true;
    }

    public synchronized void destroy() {
        if (!isValid()) {
            return;
        }
        this.mRenderer.onLayerDestroyed(this);
        this.mRenderer = null;
        this.mFinalizer.release();
        this.mFinalizer = null;
    }

    public synchronized long getDeferredLayerUpdater() {
        return this.mFinalizer.get();
    }

    public synchronized boolean copyInto(Bitmap bitmap) {
        return this.mRenderer.copyLayerInto(this, bitmap);
    }

    public synchronized boolean prepare(int width, int height, boolean isOpaque) {
        return nPrepare(this.mFinalizer.get(), width, height, isOpaque);
    }

    public synchronized void setTransform(Matrix matrix) {
        nSetTransform(this.mFinalizer.get(), matrix.native_instance);
        this.mRenderer.pushLayerUpdate(this);
    }

    public synchronized void detachSurfaceTexture() {
        this.mRenderer.detachSurfaceTexture(this.mFinalizer.get());
    }

    public synchronized long getLayerHandle() {
        return this.mFinalizer.get();
    }

    public synchronized void setSurfaceTexture(SurfaceTexture surface) {
        nSetSurfaceTexture(this.mFinalizer.get(), surface);
        this.mRenderer.pushLayerUpdate(this);
    }

    public synchronized void updateSurfaceTexture() {
        nUpdateSurfaceTexture(this.mFinalizer.get());
        this.mRenderer.pushLayerUpdate(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized TextureLayer adoptTextureLayer(ThreadedRenderer renderer, long layer) {
        return new TextureLayer(renderer, layer);
    }
}
