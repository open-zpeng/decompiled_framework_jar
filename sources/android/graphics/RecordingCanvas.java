package android.graphics;

import android.util.Pools;
import android.view.DisplayListCanvas;
import android.view.TextureLayer;
import dalvik.annotation.optimization.CriticalNative;
import dalvik.annotation.optimization.FastNative;

/* loaded from: classes.dex */
public final class RecordingCanvas extends DisplayListCanvas {
    public static final int MAX_BITMAP_SIZE = 104857600;
    private static final int POOL_LIMIT = 25;
    private static final Pools.SynchronizedPool<RecordingCanvas> sPool = new Pools.SynchronizedPool<>(25);
    private int mHeight;
    public RenderNode mNode;
    private int mWidth;

    @FastNative
    private static native void nCallDrawGLFunction(long j, long j2, Runnable runnable);

    @CriticalNative
    private static native long nCreateDisplayListCanvas(long j, int i, int i2);

    @CriticalNative
    private static native void nDrawCircle(long j, long j2, long j3, long j4, long j5);

    @CriticalNative
    private static native void nDrawRenderNode(long j, long j2);

    @CriticalNative
    private static native void nDrawRoundRect(long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8);

    @CriticalNative
    private static native void nDrawTextureLayer(long j, long j2);

    @CriticalNative
    private static native void nDrawWebViewFunctor(long j, int i);

    @CriticalNative
    private static native long nFinishRecording(long j);

    @CriticalNative
    private static native int nGetMaximumTextureHeight();

    @CriticalNative
    private static native int nGetMaximumTextureWidth();

    @CriticalNative
    private static native void nInsertReorderBarrier(long j, boolean z);

    @CriticalNative
    private static native void nResetDisplayListCanvas(long j, long j2, int i, int i2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RecordingCanvas obtain(RenderNode node, int width, int height) {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null");
        }
        RecordingCanvas canvas = sPool.acquire();
        if (canvas == null) {
            canvas = new RecordingCanvas(node, width, height);
        } else {
            nResetDisplayListCanvas(canvas.mNativeCanvasWrapper, node.mNativeRenderNode, width, height);
        }
        canvas.mNode = node;
        canvas.mWidth = width;
        canvas.mHeight = height;
        return canvas;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recycle() {
        this.mNode = null;
        sPool.release(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long finishRecording() {
        return nFinishRecording(this.mNativeCanvasWrapper);
    }

    @Override // android.graphics.Canvas
    public boolean isRecordingFor(Object o) {
        return o == this.mNode;
    }

    protected RecordingCanvas(RenderNode node, int width, int height) {
        super(nCreateDisplayListCanvas(node.mNativeRenderNode, width, height));
        this.mDensity = 0;
    }

    @Override // android.graphics.Canvas
    public void setDensity(int density) {
    }

    @Override // android.graphics.Canvas, android.graphics.BaseCanvas
    public boolean isHardwareAccelerated() {
        return true;
    }

    @Override // android.graphics.Canvas
    public void setBitmap(Bitmap bitmap) {
        throw new UnsupportedOperationException();
    }

    @Override // android.graphics.Canvas
    public boolean isOpaque() {
        return false;
    }

    @Override // android.graphics.Canvas
    public int getWidth() {
        return this.mWidth;
    }

    @Override // android.graphics.Canvas
    public int getHeight() {
        return this.mHeight;
    }

    @Override // android.graphics.Canvas
    public int getMaximumBitmapWidth() {
        return nGetMaximumTextureWidth();
    }

    @Override // android.graphics.Canvas
    public int getMaximumBitmapHeight() {
        return nGetMaximumTextureHeight();
    }

    @Override // android.graphics.Canvas
    public void enableZ() {
        nInsertReorderBarrier(this.mNativeCanvasWrapper, true);
    }

    @Override // android.graphics.Canvas
    public void disableZ() {
        nInsertReorderBarrier(this.mNativeCanvasWrapper, false);
    }

    @Deprecated
    public void callDrawGLFunction2(long drawGLFunction) {
        nCallDrawGLFunction(this.mNativeCanvasWrapper, drawGLFunction, null);
    }

    @Deprecated
    public void drawGLFunctor2(long drawGLFunctor, Runnable releasedCallback) {
        nCallDrawGLFunction(this.mNativeCanvasWrapper, drawGLFunctor, releasedCallback);
    }

    public void drawWebViewFunctor(int functor) {
        nDrawWebViewFunctor(this.mNativeCanvasWrapper, functor);
    }

    @Override // android.graphics.Canvas
    public void drawRenderNode(RenderNode renderNode) {
        nDrawRenderNode(this.mNativeCanvasWrapper, renderNode.mNativeRenderNode);
    }

    public void drawTextureLayer(TextureLayer layer) {
        nDrawTextureLayer(this.mNativeCanvasWrapper, layer.getLayerHandle());
    }

    @Override // android.view.DisplayListCanvas
    public void drawCircle(CanvasProperty<Float> cx, CanvasProperty<Float> cy, CanvasProperty<Float> radius, CanvasProperty<Paint> paint) {
        nDrawCircle(this.mNativeCanvasWrapper, cx.getNativeContainer(), cy.getNativeContainer(), radius.getNativeContainer(), paint.getNativeContainer());
    }

    @Override // android.view.DisplayListCanvas
    public void drawRoundRect(CanvasProperty<Float> left, CanvasProperty<Float> top, CanvasProperty<Float> right, CanvasProperty<Float> bottom, CanvasProperty<Float> rx, CanvasProperty<Float> ry, CanvasProperty<Paint> paint) {
        nDrawRoundRect(this.mNativeCanvasWrapper, left.getNativeContainer(), top.getNativeContainer(), right.getNativeContainer(), bottom.getNativeContainer(), rx.getNativeContainer(), ry.getNativeContainer(), paint.getNativeContainer());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.BaseCanvas
    public void throwIfCannotDraw(Bitmap bitmap) {
        super.throwIfCannotDraw(bitmap);
        int bitmapSize = bitmap.getByteCount();
        if (bitmapSize > 104857600) {
            throw new RuntimeException("Canvas: trying to draw too large(" + bitmapSize + "bytes) bitmap.");
        }
    }
}
