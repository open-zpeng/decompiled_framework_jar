package android.view;

import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedVectorDrawable;
import dalvik.annotation.optimization.CriticalNative;
import dalvik.annotation.optimization.FastNative;
import libcore.util.NativeAllocationRegistry;
/* loaded from: classes2.dex */
public class RenderNode {
    final long mNativeRenderNode;
    private final View mOwningView;

    private static native void nAddAnimator(long j, long j2);

    private static native long nCreate(String str);

    private static native void nEndAllAnimators(long j);

    @CriticalNative
    private static native float nGetAlpha(long j);

    @CriticalNative
    private static native int nGetAmbientShadowColor(long j);

    @CriticalNative
    private static native float nGetCameraDistance(long j);

    @CriticalNative
    private static native boolean nGetClipToOutline(long j);

    private static native int nGetDebugSize(long j);

    @CriticalNative
    private static native float nGetElevation(long j);

    @CriticalNative
    private static native void nGetInverseTransformMatrix(long j, long j2);

    private static native long nGetNativeFinalizer();

    @CriticalNative
    private static native float nGetPivotX(long j);

    @CriticalNative
    private static native float nGetPivotY(long j);

    @CriticalNative
    private static native float nGetRotation(long j);

    @CriticalNative
    private static native float nGetRotationX(long j);

    @CriticalNative
    private static native float nGetRotationY(long j);

    @CriticalNative
    private static native float nGetScaleX(long j);

    @CriticalNative
    private static native float nGetScaleY(long j);

    @CriticalNative
    private static native int nGetSpotShadowColor(long j);

    @CriticalNative
    private static native void nGetTransformMatrix(long j, long j2);

    @CriticalNative
    private static native float nGetTranslationX(long j);

    @CriticalNative
    private static native float nGetTranslationY(long j);

    @CriticalNative
    private static native float nGetTranslationZ(long j);

    @CriticalNative
    private static native boolean nHasIdentityMatrix(long j);

    @CriticalNative
    private static native boolean nHasOverlappingRendering(long j);

    @CriticalNative
    private static native boolean nHasShadow(long j);

    @CriticalNative
    private static native boolean nIsPivotExplicitlySet(long j);

    @CriticalNative
    private static native boolean nIsValid(long j);

    @CriticalNative
    private static native boolean nOffsetLeftAndRight(long j, int i);

    @CriticalNative
    private static native boolean nOffsetTopAndBottom(long j, int i);

    private static native void nOutput(long j);

    private static native void nRequestPositionUpdates(long j, SurfaceView surfaceView);

    @CriticalNative
    private static native boolean nResetPivot(long j);

    @CriticalNative
    private static native boolean nSetAlpha(long j, float f);

    @CriticalNative
    private static native boolean nSetAmbientShadowColor(long j, int i);

    @CriticalNative
    private static native boolean nSetAnimationMatrix(long j, long j2);

    @CriticalNative
    private static native boolean nSetBottom(long j, int i);

    @CriticalNative
    private static native boolean nSetCameraDistance(long j, float f);

    @CriticalNative
    private static native boolean nSetClipBounds(long j, int i, int i2, int i3, int i4);

    @CriticalNative
    private static native boolean nSetClipBoundsEmpty(long j);

    @CriticalNative
    private static native boolean nSetClipToBounds(long j, boolean z);

    @CriticalNative
    private static native boolean nSetClipToOutline(long j, boolean z);

    @FastNative
    private static native void nSetDisplayList(long j, long j2);

    @CriticalNative
    private static native boolean nSetElevation(long j, float f);

    @CriticalNative
    private static native boolean nSetHasOverlappingRendering(long j, boolean z);

    @CriticalNative
    private static native boolean nSetLayerPaint(long j, long j2);

    @CriticalNative
    private static native boolean nSetLayerType(long j, int i);

    @CriticalNative
    private static native boolean nSetLeft(long j, int i);

    @CriticalNative
    private static native boolean nSetLeftTopRightBottom(long j, int i, int i2, int i3, int i4);

    @CriticalNative
    private static native boolean nSetOutlineConvexPath(long j, long j2, float f);

    @CriticalNative
    private static native boolean nSetOutlineEmpty(long j);

    @CriticalNative
    private static native boolean nSetOutlineNone(long j);

    @CriticalNative
    private static native boolean nSetOutlineRoundRect(long j, int i, int i2, int i3, int i4, float f, float f2);

    @CriticalNative
    private static native boolean nSetPivotX(long j, float f);

    @CriticalNative
    private static native boolean nSetPivotY(long j, float f);

    @CriticalNative
    private static native boolean nSetProjectBackwards(long j, boolean z);

    @CriticalNative
    private static native boolean nSetProjectionReceiver(long j, boolean z);

    @CriticalNative
    private static native boolean nSetRevealClip(long j, boolean z, float f, float f2, float f3);

    @CriticalNative
    private static native boolean nSetRight(long j, int i);

    @CriticalNative
    private static native boolean nSetRotation(long j, float f);

    @CriticalNative
    private static native boolean nSetRotationX(long j, float f);

    @CriticalNative
    private static native boolean nSetRotationY(long j, float f);

    @CriticalNative
    private static native boolean nSetScaleX(long j, float f);

    @CriticalNative
    private static native boolean nSetScaleY(long j, float f);

    @CriticalNative
    private static native boolean nSetSpotShadowColor(long j, int i);

    @CriticalNative
    private static native boolean nSetStaticMatrix(long j, long j2);

    @CriticalNative
    private static native boolean nSetTop(long j, int i);

    @CriticalNative
    private static native boolean nSetTranslationX(long j, float f);

    @CriticalNative
    private static native boolean nSetTranslationY(long j, float f);

    @CriticalNative
    private static native boolean nSetTranslationZ(long j, float f);

    static /* synthetic */ long access$000() {
        return nGetNativeFinalizer();
    }

    /* loaded from: classes2.dex */
    private static class NoImagePreloadHolder {
        public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(RenderNode.class.getClassLoader(), RenderNode.access$000(), 1024);

        private synchronized NoImagePreloadHolder() {
        }
    }

    private synchronized RenderNode(String name, View owningView) {
        this.mNativeRenderNode = nCreate(name);
        NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, this.mNativeRenderNode);
        this.mOwningView = owningView;
    }

    private synchronized RenderNode(long nativePtr) {
        this.mNativeRenderNode = nativePtr;
        NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, this.mNativeRenderNode);
        this.mOwningView = null;
    }

    public synchronized void destroy() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static RenderNode create(String name, View owningView) {
        return new RenderNode(name, owningView);
    }

    public static synchronized RenderNode adopt(long nativePtr) {
        return new RenderNode(nativePtr);
    }

    public synchronized void requestPositionUpdates(SurfaceView view) {
        nRequestPositionUpdates(this.mNativeRenderNode, view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DisplayListCanvas start(int width, int height) {
        return DisplayListCanvas.obtain(this, width, height);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void end(DisplayListCanvas canvas) {
        long displayList = canvas.finishRecording();
        nSetDisplayList(this.mNativeRenderNode, displayList);
        canvas.recycle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void discardDisplayList() {
        nSetDisplayList(this.mNativeRenderNode, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValid() {
        return nIsValid(this.mNativeRenderNode);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized long getNativeDisplayList() {
        if (!isValid()) {
            throw new IllegalStateException("The display list is not valid.");
        }
        return this.mNativeRenderNode;
    }

    public synchronized boolean hasIdentityMatrix() {
        return nHasIdentityMatrix(this.mNativeRenderNode);
    }

    public synchronized void getMatrix(Matrix outMatrix) {
        nGetTransformMatrix(this.mNativeRenderNode, outMatrix.native_instance);
    }

    public synchronized void getInverseMatrix(Matrix outMatrix) {
        nGetInverseTransformMatrix(this.mNativeRenderNode, outMatrix.native_instance);
    }

    public synchronized boolean setLayerType(int layerType) {
        return nSetLayerType(this.mNativeRenderNode, layerType);
    }

    public synchronized boolean setLayerPaint(Paint paint) {
        return nSetLayerPaint(this.mNativeRenderNode, paint != null ? paint.getNativeInstance() : 0L);
    }

    public synchronized boolean setClipBounds(Rect rect) {
        if (rect == null) {
            return nSetClipBoundsEmpty(this.mNativeRenderNode);
        }
        return nSetClipBounds(this.mNativeRenderNode, rect.left, rect.top, rect.right, rect.bottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setClipToBounds(boolean clipToBounds) {
        return nSetClipToBounds(this.mNativeRenderNode, clipToBounds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setProjectBackwards(boolean shouldProject) {
        return nSetProjectBackwards(this.mNativeRenderNode, shouldProject);
    }

    public synchronized boolean setProjectionReceiver(boolean shouldRecieve) {
        return nSetProjectionReceiver(this.mNativeRenderNode, shouldRecieve);
    }

    public synchronized boolean setOutline(Outline outline) {
        if (outline == null) {
            return nSetOutlineNone(this.mNativeRenderNode);
        }
        switch (outline.mMode) {
            case 0:
                return nSetOutlineEmpty(this.mNativeRenderNode);
            case 1:
                return nSetOutlineRoundRect(this.mNativeRenderNode, outline.mRect.left, outline.mRect.top, outline.mRect.right, outline.mRect.bottom, outline.mRadius, outline.mAlpha);
            case 2:
                return nSetOutlineConvexPath(this.mNativeRenderNode, outline.mPath.mNativePath, outline.mAlpha);
            default:
                throw new IllegalArgumentException("Unrecognized outline?");
        }
    }

    public synchronized boolean hasShadow() {
        return nHasShadow(this.mNativeRenderNode);
    }

    public synchronized boolean setSpotShadowColor(int color) {
        return nSetSpotShadowColor(this.mNativeRenderNode, color);
    }

    public synchronized boolean setAmbientShadowColor(int color) {
        return nSetAmbientShadowColor(this.mNativeRenderNode, color);
    }

    public synchronized int getSpotShadowColor() {
        return nGetSpotShadowColor(this.mNativeRenderNode);
    }

    public synchronized int getAmbientShadowColor() {
        return nGetAmbientShadowColor(this.mNativeRenderNode);
    }

    public synchronized boolean setClipToOutline(boolean clipToOutline) {
        return nSetClipToOutline(this.mNativeRenderNode, clipToOutline);
    }

    public synchronized boolean getClipToOutline() {
        return nGetClipToOutline(this.mNativeRenderNode);
    }

    public synchronized boolean setRevealClip(boolean shouldClip, float x, float y, float radius) {
        return nSetRevealClip(this.mNativeRenderNode, shouldClip, x, y, radius);
    }

    public synchronized boolean setStaticMatrix(Matrix matrix) {
        return nSetStaticMatrix(this.mNativeRenderNode, matrix.native_instance);
    }

    public synchronized boolean setAnimationMatrix(Matrix matrix) {
        return nSetAnimationMatrix(this.mNativeRenderNode, matrix != null ? matrix.native_instance : 0L);
    }

    public synchronized boolean setAlpha(float alpha) {
        return nSetAlpha(this.mNativeRenderNode, alpha);
    }

    public synchronized float getAlpha() {
        return nGetAlpha(this.mNativeRenderNode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setHasOverlappingRendering(boolean hasOverlappingRendering) {
        return nSetHasOverlappingRendering(this.mNativeRenderNode, hasOverlappingRendering);
    }

    public synchronized boolean hasOverlappingRendering() {
        return nHasOverlappingRendering(this.mNativeRenderNode);
    }

    public synchronized boolean setElevation(float lift) {
        return nSetElevation(this.mNativeRenderNode, lift);
    }

    public synchronized float getElevation() {
        return nGetElevation(this.mNativeRenderNode);
    }

    public synchronized boolean setTranslationX(float translationX) {
        return nSetTranslationX(this.mNativeRenderNode, translationX);
    }

    public synchronized float getTranslationX() {
        return nGetTranslationX(this.mNativeRenderNode);
    }

    public synchronized boolean setTranslationY(float translationY) {
        return nSetTranslationY(this.mNativeRenderNode, translationY);
    }

    public synchronized float getTranslationY() {
        return nGetTranslationY(this.mNativeRenderNode);
    }

    public synchronized boolean setTranslationZ(float translationZ) {
        return nSetTranslationZ(this.mNativeRenderNode, translationZ);
    }

    public synchronized float getTranslationZ() {
        return nGetTranslationZ(this.mNativeRenderNode);
    }

    public synchronized boolean setRotation(float rotation) {
        return nSetRotation(this.mNativeRenderNode, rotation);
    }

    public synchronized float getRotation() {
        return nGetRotation(this.mNativeRenderNode);
    }

    public synchronized boolean setRotationX(float rotationX) {
        return nSetRotationX(this.mNativeRenderNode, rotationX);
    }

    public synchronized float getRotationX() {
        return nGetRotationX(this.mNativeRenderNode);
    }

    public synchronized boolean setRotationY(float rotationY) {
        return nSetRotationY(this.mNativeRenderNode, rotationY);
    }

    public synchronized float getRotationY() {
        return nGetRotationY(this.mNativeRenderNode);
    }

    public synchronized boolean setScaleX(float scaleX) {
        return nSetScaleX(this.mNativeRenderNode, scaleX);
    }

    public synchronized float getScaleX() {
        return nGetScaleX(this.mNativeRenderNode);
    }

    public synchronized boolean setScaleY(float scaleY) {
        return nSetScaleY(this.mNativeRenderNode, scaleY);
    }

    public synchronized float getScaleY() {
        return nGetScaleY(this.mNativeRenderNode);
    }

    public synchronized boolean setPivotX(float pivotX) {
        return nSetPivotX(this.mNativeRenderNode, pivotX);
    }

    public synchronized float getPivotX() {
        return nGetPivotX(this.mNativeRenderNode);
    }

    public synchronized boolean setPivotY(float pivotY) {
        return nSetPivotY(this.mNativeRenderNode, pivotY);
    }

    public synchronized float getPivotY() {
        return nGetPivotY(this.mNativeRenderNode);
    }

    public synchronized boolean isPivotExplicitlySet() {
        return nIsPivotExplicitlySet(this.mNativeRenderNode);
    }

    public synchronized boolean resetPivot() {
        return nResetPivot(this.mNativeRenderNode);
    }

    public synchronized boolean setCameraDistance(float distance) {
        return nSetCameraDistance(this.mNativeRenderNode, distance);
    }

    public synchronized float getCameraDistance() {
        return nGetCameraDistance(this.mNativeRenderNode);
    }

    public synchronized boolean setLeft(int left) {
        return nSetLeft(this.mNativeRenderNode, left);
    }

    public synchronized boolean setTop(int top) {
        return nSetTop(this.mNativeRenderNode, top);
    }

    public synchronized boolean setRight(int right) {
        return nSetRight(this.mNativeRenderNode, right);
    }

    public synchronized boolean setBottom(int bottom) {
        return nSetBottom(this.mNativeRenderNode, bottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setLeftTopRightBottom(int left, int top, int right, int bottom) {
        return nSetLeftTopRightBottom(this.mNativeRenderNode, left, top, right, bottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean offsetLeftAndRight(int offset) {
        return nOffsetLeftAndRight(this.mNativeRenderNode, offset);
    }

    public synchronized boolean offsetTopAndBottom(int offset) {
        return nOffsetTopAndBottom(this.mNativeRenderNode, offset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void output() {
        nOutput(this.mNativeRenderNode);
    }

    public synchronized int getDebugSize() {
        return nGetDebugSize(this.mNativeRenderNode);
    }

    public synchronized void addAnimator(RenderNodeAnimator animator) {
        if (this.mOwningView == null || this.mOwningView.mAttachInfo == null) {
            throw new IllegalStateException("Cannot start this animator on a detached view!");
        }
        nAddAnimator(this.mNativeRenderNode, animator.getNativeAnimator());
        this.mOwningView.mAttachInfo.mViewRootImpl.registerAnimatingRenderNode(this);
    }

    public synchronized boolean isAttached() {
        return (this.mOwningView == null || this.mOwningView.mAttachInfo == null) ? false : true;
    }

    public synchronized void registerVectorDrawableAnimator(AnimatedVectorDrawable.VectorDrawableAnimatorRT animatorSet) {
        if (this.mOwningView == null || this.mOwningView.mAttachInfo == null) {
            throw new IllegalStateException("Cannot start this animator on a detached view!");
        }
        this.mOwningView.mAttachInfo.mViewRootImpl.registerVectorDrawableAnimator(animatorSet);
    }

    public synchronized void endAllAnimators() {
        nEndAllAnimators(this.mNativeRenderNode);
    }
}
