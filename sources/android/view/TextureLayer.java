package android.view;

import android.graphics.Bitmap;
import android.graphics.HardwareRenderer;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import com.android.internal.util.VirtualRefBasePtr;

/* loaded from: classes3.dex */
public final class TextureLayer {
    private VirtualRefBasePtr mFinalizer;
    private HardwareRenderer mRenderer;

    private static native boolean nPrepare(long j, int i, int i2, boolean z);

    private static native void nSetLayerPaint(long j, long j2);

    private static native void nSetSurfaceTexture(long j, SurfaceTexture surfaceTexture);

    private static native void nSetTransform(long j, long j2);

    private static native void nUpdateSurfaceTexture(long j);

    private TextureLayer(HardwareRenderer renderer, long deferredUpdater) {
        if (renderer == null || deferredUpdater == 0) {
            throw new IllegalArgumentException("Either hardware renderer: " + renderer + " or deferredUpdater: " + deferredUpdater + " is invalid");
        }
        this.mRenderer = renderer;
        this.mFinalizer = new VirtualRefBasePtr(deferredUpdater);
    }

    public void setLayerPaint(Paint paint) {
        nSetLayerPaint(this.mFinalizer.get(), paint != null ? paint.getNativeInstance() : 0L);
        this.mRenderer.pushLayerUpdate(this);
    }

    public boolean isValid() {
        VirtualRefBasePtr virtualRefBasePtr = this.mFinalizer;
        return (virtualRefBasePtr == null || virtualRefBasePtr.get() == 0) ? false : true;
    }

    public void destroy() {
        if (!isValid()) {
            return;
        }
        this.mRenderer.onLayerDestroyed(this);
        this.mRenderer = null;
        this.mFinalizer.release();
        this.mFinalizer = null;
    }

    public long getDeferredLayerUpdater() {
        return this.mFinalizer.get();
    }

    public boolean copyInto(Bitmap bitmap) {
        return this.mRenderer.copyLayerInto(this, bitmap);
    }

    public boolean prepare(int width, int height, boolean isOpaque) {
        return nPrepare(this.mFinalizer.get(), width, height, isOpaque);
    }

    public void setTransform(Matrix matrix) {
        nSetTransform(this.mFinalizer.get(), matrix.native_instance);
        this.mRenderer.pushLayerUpdate(this);
    }

    public void detachSurfaceTexture() {
        this.mRenderer.detachSurfaceTexture(this.mFinalizer.get());
    }

    public long getLayerHandle() {
        return this.mFinalizer.get();
    }

    public void setSurfaceTexture(SurfaceTexture surface) {
        nSetSurfaceTexture(this.mFinalizer.get(), surface);
        this.mRenderer.pushLayerUpdate(this);
    }

    public void updateSurfaceTexture() {
        nUpdateSurfaceTexture(this.mFinalizer.get());
        this.mRenderer.pushLayerUpdate(this);
    }

    public static TextureLayer adoptTextureLayer(HardwareRenderer renderer, long layer) {
        return new TextureLayer(renderer, layer);
    }
}
