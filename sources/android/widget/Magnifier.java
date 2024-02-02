package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.DisplayListCanvas;
import android.view.PixelCopy;
import android.view.RenderNode;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceSession;
import android.view.SurfaceView;
import android.view.ThreadedRenderer;
import android.view.View;
import android.view.ViewRootImpl;
import android.widget.Magnifier;
import com.android.internal.R;
import com.android.internal.util.Preconditions;
/* loaded from: classes3.dex */
public final class Magnifier {
    private static final int NONEXISTENT_PREVIOUS_CONFIG_VALUE = -1;
    private static final HandlerThread sPixelCopyHandlerThread = new HandlerThread("magnifier pixel copy result handler");
    private final int mBitmapHeight;
    private final int mBitmapWidth;
    private Callback mCallback;
    private SurfaceInfo mContentCopySurface;
    private SurfaceInfo mParentSurface;
    private final View mView;
    private final int[] mViewCoordinatesInSurface;
    private InternalPopupWindow mWindow;
    private final float mWindowCornerRadius;
    private final float mWindowElevation;
    private final int mWindowHeight;
    private final int mWindowWidth;
    private final float mZoom;
    private final Point mWindowCoords = new Point();
    private final Point mCenterZoomCoords = new Point();
    private final Point mClampedCenterZoomCoords = new Point();
    private final Point mPrevStartCoordsInSurface = new Point(-1, -1);
    private final PointF mPrevPosInView = new PointF(-1.0f, -1.0f);
    private final Rect mPixelCopyRequestRect = new Rect();
    private final Object mLock = new Object();

    /* loaded from: classes3.dex */
    public interface Callback {
        void onOperationComplete();
    }

    static {
        sPixelCopyHandlerThread.start();
    }

    public Magnifier(View view) {
        this.mView = (View) Preconditions.checkNotNull(view);
        Context context = this.mView.getContext();
        this.mWindowWidth = context.getResources().getDimensionPixelSize(R.dimen.magnifier_width);
        this.mWindowHeight = context.getResources().getDimensionPixelSize(R.dimen.magnifier_height);
        this.mWindowElevation = context.getResources().getDimension(R.dimen.magnifier_elevation);
        this.mWindowCornerRadius = getDeviceDefaultDialogCornerRadius();
        this.mZoom = context.getResources().getFloat(R.dimen.magnifier_zoom_scale);
        this.mBitmapWidth = Math.round(this.mWindowWidth / this.mZoom);
        this.mBitmapHeight = Math.round(this.mWindowHeight / this.mZoom);
        this.mViewCoordinatesInSurface = new int[2];
    }

    private synchronized float getDeviceDefaultDialogCornerRadius() {
        Context deviceDefaultContext = new ContextThemeWrapper(this.mView.getContext(), 16974120);
        TypedArray ta = deviceDefaultContext.obtainStyledAttributes(new int[]{16844145});
        float dialogCornerRadius = ta.getDimension(0, 0.0f);
        ta.recycle();
        return dialogCornerRadius;
    }

    public void show(float xPosInView, float yPosInView) {
        float yPosInView2;
        float xPosInView2 = Math.max(0.0f, Math.min(xPosInView, this.mView.getWidth()));
        float yPosInView3 = Math.max(0.0f, Math.min(yPosInView, this.mView.getHeight()));
        obtainSurfaces();
        obtainContentCoordinates(xPosInView2, yPosInView3);
        obtainWindowCoordinates();
        int startX = this.mClampedCenterZoomCoords.x - (this.mBitmapWidth / 2);
        int startY = this.mClampedCenterZoomCoords.y - (this.mBitmapHeight / 2);
        if (xPosInView2 != this.mPrevPosInView.x || yPosInView3 != this.mPrevPosInView.y) {
            if (this.mWindow == null) {
                synchronized (this.mLock) {
                    try {
                        try {
                            yPosInView2 = yPosInView3;
                            this.mWindow = new InternalPopupWindow(this.mView.getContext(), this.mView.getDisplay(), this.mParentSurface.mSurface, this.mWindowWidth, this.mWindowHeight, this.mWindowElevation, this.mWindowCornerRadius, Handler.getMain(), this.mLock, this.mCallback);
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            } else {
                yPosInView2 = yPosInView3;
            }
            performPixelCopy(startX, startY, true);
            this.mPrevPosInView.x = xPosInView2;
            this.mPrevPosInView.y = yPosInView2;
        }
    }

    public void dismiss() {
        if (this.mWindow != null) {
            synchronized (this.mLock) {
                this.mWindow.destroy();
                this.mWindow = null;
            }
            this.mPrevPosInView.x = -1.0f;
            this.mPrevPosInView.y = -1.0f;
            this.mPrevStartCoordsInSurface.x = -1;
            this.mPrevStartCoordsInSurface.y = -1;
        }
    }

    public void update() {
        if (this.mWindow != null) {
            obtainSurfaces();
            performPixelCopy(this.mPrevStartCoordsInSurface.x, this.mPrevStartCoordsInSurface.y, false);
        }
    }

    public int getWidth() {
        return this.mWindowWidth;
    }

    public int getHeight() {
        return this.mWindowHeight;
    }

    public float getZoom() {
        return this.mZoom;
    }

    public synchronized Point getWindowCoords() {
        if (this.mWindow == null) {
            return null;
        }
        Rect surfaceInsets = this.mView.getViewRootImpl().mWindowAttributes.surfaceInsets;
        return new Point(this.mWindow.mLastDrawContentPositionX - surfaceInsets.left, this.mWindow.mLastDrawContentPositionY - surfaceInsets.top);
    }

    private synchronized void obtainSurfaces() {
        SurfaceHolder surfaceHolder;
        Surface surfaceViewSurface;
        ViewRootImpl viewRootImpl;
        Surface mainWindowSurface;
        SurfaceInfo validMainWindowSurface = SurfaceInfo.NULL;
        if (this.mView.getViewRootImpl() != null && (mainWindowSurface = (viewRootImpl = this.mView.getViewRootImpl()).mSurface) != null && mainWindowSurface.isValid()) {
            Rect surfaceInsets = viewRootImpl.mWindowAttributes.surfaceInsets;
            int surfaceWidth = viewRootImpl.getWidth() + surfaceInsets.left + surfaceInsets.right;
            int surfaceHeight = viewRootImpl.getHeight() + surfaceInsets.top + surfaceInsets.bottom;
            validMainWindowSurface = new SurfaceInfo(mainWindowSurface, surfaceWidth, surfaceHeight, true);
        }
        SurfaceInfo validSurfaceViewSurface = SurfaceInfo.NULL;
        if ((this.mView instanceof SurfaceView) && (surfaceViewSurface = (surfaceHolder = ((SurfaceView) this.mView).getHolder()).getSurface()) != null && surfaceViewSurface.isValid()) {
            Rect surfaceFrame = surfaceHolder.getSurfaceFrame();
            validSurfaceViewSurface = new SurfaceInfo(surfaceViewSurface, surfaceFrame.right, surfaceFrame.bottom, false);
        }
        this.mParentSurface = validMainWindowSurface != SurfaceInfo.NULL ? validMainWindowSurface : validSurfaceViewSurface;
        this.mContentCopySurface = this.mView instanceof SurfaceView ? validSurfaceViewSurface : validMainWindowSurface;
    }

    private synchronized void obtainContentCoordinates(float xPosInView, float yPosInView) {
        float posX;
        float posY;
        this.mView.getLocationInSurface(this.mViewCoordinatesInSurface);
        if (this.mView instanceof SurfaceView) {
            posX = xPosInView;
            posY = yPosInView;
        } else {
            posX = this.mViewCoordinatesInSurface[0] + xPosInView;
            posY = this.mViewCoordinatesInSurface[1] + yPosInView;
        }
        this.mCenterZoomCoords.x = Math.round(posX);
        this.mCenterZoomCoords.y = Math.round(posY);
        Rect viewVisibleRegion = new Rect();
        this.mView.getGlobalVisibleRect(viewVisibleRegion);
        if (this.mView.getViewRootImpl() != null) {
            Rect surfaceInsets = this.mView.getViewRootImpl().mWindowAttributes.surfaceInsets;
            viewVisibleRegion.offset(surfaceInsets.left, surfaceInsets.top);
        }
        if (this.mView instanceof SurfaceView) {
            viewVisibleRegion.offset(-this.mViewCoordinatesInSurface[0], -this.mViewCoordinatesInSurface[1]);
        }
        this.mClampedCenterZoomCoords.x = Math.max(viewVisibleRegion.left + (this.mBitmapWidth / 2), Math.min(this.mCenterZoomCoords.x, viewVisibleRegion.right - (this.mBitmapWidth / 2)));
        this.mClampedCenterZoomCoords.y = this.mCenterZoomCoords.y;
    }

    private synchronized void obtainWindowCoordinates() {
        int verticalOffset = this.mView.getContext().getResources().getDimensionPixelSize(R.dimen.magnifier_offset);
        this.mWindowCoords.x = this.mCenterZoomCoords.x - (this.mWindowWidth / 2);
        this.mWindowCoords.y = (this.mCenterZoomCoords.y - (this.mWindowHeight / 2)) - verticalOffset;
        if (this.mParentSurface != this.mContentCopySurface) {
            this.mWindowCoords.x += this.mViewCoordinatesInSurface[0];
            this.mWindowCoords.y += this.mViewCoordinatesInSurface[1];
        }
    }

    private synchronized void performPixelCopy(int startXInSurface, int startYInSurface, final boolean updateWindowPosition) {
        Rect systemInsets;
        if (this.mContentCopySurface.mSurface == null || !this.mContentCopySurface.mSurface.isValid()) {
            return;
        }
        int clampedStartXInSurface = Math.max(0, Math.min(startXInSurface, this.mContentCopySurface.mWidth - this.mBitmapWidth));
        int clampedStartYInSurface = Math.max(0, Math.min(startYInSurface, this.mContentCopySurface.mHeight - this.mBitmapHeight));
        if (this.mParentSurface.mIsMainWindowSurface) {
            Rect systemInsets2 = this.mView.getRootWindowInsets().getSystemWindowInsets();
            systemInsets = new Rect(systemInsets2.left, systemInsets2.top, this.mParentSurface.mWidth - systemInsets2.right, this.mParentSurface.mHeight - systemInsets2.bottom);
        } else {
            systemInsets = new Rect(0, 0, this.mParentSurface.mWidth, this.mParentSurface.mHeight);
        }
        Rect windowBounds = systemInsets;
        final int windowCoordsX = Math.max(windowBounds.left, Math.min(windowBounds.right - this.mWindowWidth, this.mWindowCoords.x));
        final int windowCoordsY = Math.max(windowBounds.top, Math.min(windowBounds.bottom - this.mWindowHeight, this.mWindowCoords.y));
        this.mPixelCopyRequestRect.set(clampedStartXInSurface, clampedStartYInSurface, this.mBitmapWidth + clampedStartXInSurface, this.mBitmapHeight + clampedStartYInSurface);
        final InternalPopupWindow currentWindowInstance = this.mWindow;
        final Bitmap bitmap = Bitmap.createBitmap(this.mBitmapWidth, this.mBitmapHeight, Bitmap.Config.ARGB_8888);
        PixelCopy.request(this.mContentCopySurface.mSurface, this.mPixelCopyRequestRect, bitmap, new PixelCopy.OnPixelCopyFinishedListener() { // from class: android.widget.-$$Lambda$Magnifier$1ctRJdojBZQzahoS7og5wm1FKM4
            @Override // android.view.PixelCopy.OnPixelCopyFinishedListener
            public final void onPixelCopyFinished(int i) {
                Magnifier.lambda$performPixelCopy$0(Magnifier.this, currentWindowInstance, updateWindowPosition, windowCoordsX, windowCoordsY, bitmap, i);
            }
        }, sPixelCopyHandlerThread.getThreadHandler());
        this.mPrevStartCoordsInSurface.x = startXInSurface;
        this.mPrevStartCoordsInSurface.y = startYInSurface;
    }

    public static /* synthetic */ void lambda$performPixelCopy$0(Magnifier magnifier, InternalPopupWindow currentWindowInstance, boolean updateWindowPosition, int windowCoordsX, int windowCoordsY, Bitmap bitmap, int result) {
        synchronized (magnifier.mLock) {
            if (magnifier.mWindow != currentWindowInstance) {
                return;
            }
            if (updateWindowPosition) {
                magnifier.mWindow.setContentPositionForNextDraw(windowCoordsX, windowCoordsY);
            }
            magnifier.mWindow.updateContent(bitmap);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class SurfaceInfo {
        public static final SurfaceInfo NULL = new SurfaceInfo(null, 0, 0, false);
        private int mHeight;
        private boolean mIsMainWindowSurface;
        private Surface mSurface;
        private int mWidth;

        synchronized SurfaceInfo(Surface surface, int width, int height, boolean isMainWindowSurface) {
            this.mSurface = surface;
            this.mWidth = width;
            this.mHeight = height;
            this.mIsMainWindowSurface = isMainWindowSurface;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class InternalPopupWindow {
        private static final int CONTENT_BITMAP_ALPHA = 242;
        private static final int SURFACE_Z = 5;
        private Bitmap mBitmap;
        private final RenderNode mBitmapRenderNode;
        private Callback mCallback;
        private final int mContentHeight;
        private final int mContentWidth;
        private final Display mDisplay;
        private boolean mFrameDrawScheduled;
        private final Handler mHandler;
        private int mLastDrawContentPositionX;
        private int mLastDrawContentPositionY;
        private final Object mLock;
        private final Runnable mMagnifierUpdater;
        private final int mOffsetX;
        private final int mOffsetY;
        private boolean mPendingWindowPositionUpdate;
        private final ThreadedRenderer.SimpleRenderer mRenderer;
        private final SurfaceControl mSurfaceControl;
        private final int mSurfaceHeight;
        private final SurfaceSession mSurfaceSession;
        private final int mSurfaceWidth;
        private int mWindowPositionX;
        private int mWindowPositionY;
        private boolean mFirstDraw = true;
        private final Object mDestroyLock = new Object();
        private final Surface mSurface = new Surface();

        synchronized InternalPopupWindow(Context context, Display display, Surface parentSurface, int width, int height, float elevation, float cornerRadius, Handler handler, Object lock, Callback callback) {
            this.mDisplay = display;
            this.mLock = lock;
            this.mCallback = callback;
            this.mContentWidth = width;
            this.mContentHeight = height;
            this.mOffsetX = (int) (width * 0.1f);
            this.mOffsetY = (int) (0.1f * height);
            this.mSurfaceWidth = this.mContentWidth + (this.mOffsetX * 2);
            this.mSurfaceHeight = this.mContentHeight + (2 * this.mOffsetY);
            this.mSurfaceSession = new SurfaceSession(parentSurface);
            this.mSurfaceControl = new SurfaceControl.Builder(this.mSurfaceSession).setFormat(-3).setSize(this.mSurfaceWidth, this.mSurfaceHeight).setName("magnifier surface").setFlags(4).build();
            this.mSurface.copyFrom(this.mSurfaceControl);
            this.mRenderer = new ThreadedRenderer.SimpleRenderer(context, "magnifier renderer", this.mSurface);
            this.mBitmapRenderNode = createRenderNodeForBitmap("magnifier content", elevation, cornerRadius);
            DisplayListCanvas canvas = this.mRenderer.getRootNode().start(width, height);
            try {
                canvas.insertReorderBarrier();
                canvas.drawRenderNode(this.mBitmapRenderNode);
                canvas.insertInorderBarrier();
                this.mRenderer.getRootNode().end(canvas);
                this.mHandler = handler;
                this.mMagnifierUpdater = new Runnable() { // from class: android.widget.-$$Lambda$Magnifier$InternalPopupWindow$t9Cn2sIi2LBUhAVikvRPKKoAwIU
                    @Override // java.lang.Runnable
                    public final void run() {
                        Magnifier.InternalPopupWindow.this.doDraw();
                    }
                };
                this.mFrameDrawScheduled = false;
            } catch (Throwable th) {
                this.mRenderer.getRootNode().end(canvas);
                throw th;
            }
        }

        private synchronized RenderNode createRenderNodeForBitmap(String name, float elevation, float cornerRadius) {
            RenderNode bitmapRenderNode = RenderNode.create(name, null);
            bitmapRenderNode.setLeftTopRightBottom(this.mOffsetX, this.mOffsetY, this.mOffsetX + this.mContentWidth, this.mOffsetY + this.mContentHeight);
            bitmapRenderNode.setElevation(elevation);
            Outline outline = new Outline();
            outline.setRoundRect(0, 0, this.mContentWidth, this.mContentHeight, cornerRadius);
            outline.setAlpha(1.0f);
            bitmapRenderNode.setOutline(outline);
            bitmapRenderNode.setClipToOutline(true);
            DisplayListCanvas canvas = bitmapRenderNode.start(this.mContentWidth, this.mContentHeight);
            try {
                canvas.drawColor(Color.GREEN);
                return bitmapRenderNode;
            } finally {
                bitmapRenderNode.end(canvas);
            }
        }

        public synchronized void setContentPositionForNextDraw(int contentX, int contentY) {
            this.mWindowPositionX = contentX - this.mOffsetX;
            this.mWindowPositionY = contentY - this.mOffsetY;
            this.mPendingWindowPositionUpdate = true;
            requestUpdate();
        }

        public synchronized void updateContent(Bitmap bitmap) {
            if (this.mBitmap != null) {
                this.mBitmap.recycle();
            }
            this.mBitmap = bitmap;
            requestUpdate();
        }

        private synchronized void requestUpdate() {
            if (this.mFrameDrawScheduled) {
                return;
            }
            Message request = Message.obtain(this.mHandler, this.mMagnifierUpdater);
            request.setAsynchronous(true);
            request.sendToTarget();
            this.mFrameDrawScheduled = true;
        }

        public synchronized void destroy() {
            synchronized (this.mDestroyLock) {
                this.mSurface.destroy();
            }
            synchronized (this.mLock) {
                this.mRenderer.destroy();
                this.mSurfaceControl.destroy();
                this.mSurfaceSession.kill();
                this.mBitmapRenderNode.destroy();
                this.mHandler.removeCallbacks(this.mMagnifierUpdater);
                if (this.mBitmap != null) {
                    this.mBitmap.recycle();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0089  */
        /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public synchronized void doDraw() {
            /*
                r12 = this;
                java.lang.Object r0 = r12.mLock
                monitor-enter(r0)
                android.view.Surface r1 = r12.mSurface     // Catch: java.lang.Throwable -> L96
                boolean r1 = r1.isValid()     // Catch: java.lang.Throwable -> L96
                if (r1 != 0) goto Ld
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L96
                return
            Ld:
                android.view.RenderNode r1 = r12.mBitmapRenderNode     // Catch: java.lang.Throwable -> L96
                int r2 = r12.mContentWidth     // Catch: java.lang.Throwable -> L96
                int r3 = r12.mContentHeight     // Catch: java.lang.Throwable -> L96
                android.view.DisplayListCanvas r1 = r1.start(r2, r3)     // Catch: java.lang.Throwable -> L96
                r2 = -1
                r1.drawColor(r2)     // Catch: java.lang.Throwable -> L8f
                android.graphics.Rect r2 = new android.graphics.Rect     // Catch: java.lang.Throwable -> L8f
                android.graphics.Bitmap r3 = r12.mBitmap     // Catch: java.lang.Throwable -> L8f
                int r3 = r3.getWidth()     // Catch: java.lang.Throwable -> L8f
                android.graphics.Bitmap r4 = r12.mBitmap     // Catch: java.lang.Throwable -> L8f
                int r4 = r4.getHeight()     // Catch: java.lang.Throwable -> L8f
                r5 = 0
                r2.<init>(r5, r5, r3, r4)     // Catch: java.lang.Throwable -> L8f
                android.graphics.Rect r3 = new android.graphics.Rect     // Catch: java.lang.Throwable -> L8f
                int r4 = r12.mContentWidth     // Catch: java.lang.Throwable -> L8f
                int r6 = r12.mContentHeight     // Catch: java.lang.Throwable -> L8f
                r3.<init>(r5, r5, r4, r6)     // Catch: java.lang.Throwable -> L8f
                android.graphics.Paint r4 = new android.graphics.Paint     // Catch: java.lang.Throwable -> L8f
                r4.<init>()     // Catch: java.lang.Throwable -> L8f
                r6 = 1
                r4.setFilterBitmap(r6)     // Catch: java.lang.Throwable -> L8f
                r6 = 242(0xf2, float:3.39E-43)
                r4.setAlpha(r6)     // Catch: java.lang.Throwable -> L8f
                android.graphics.Bitmap r6 = r12.mBitmap     // Catch: java.lang.Throwable -> L8f
                r1.drawBitmap(r6, r2, r3, r4)     // Catch: java.lang.Throwable -> L8f
                android.view.RenderNode r2 = r12.mBitmapRenderNode     // Catch: java.lang.Throwable -> L96
                r2.end(r1)     // Catch: java.lang.Throwable -> L96
                boolean r2 = r12.mPendingWindowPositionUpdate     // Catch: java.lang.Throwable -> L96
                if (r2 != 0) goto L5a
                boolean r2 = r12.mFirstDraw     // Catch: java.lang.Throwable -> L96
                if (r2 == 0) goto L58
                goto L5a
            L58:
                r2 = 0
                goto L6e
            L5a:
                boolean r11 = r12.mFirstDraw     // Catch: java.lang.Throwable -> L96
                r12.mFirstDraw = r5     // Catch: java.lang.Throwable -> L96
                boolean r10 = r12.mPendingWindowPositionUpdate     // Catch: java.lang.Throwable -> L96
                r12.mPendingWindowPositionUpdate = r5     // Catch: java.lang.Throwable -> L96
                int r8 = r12.mWindowPositionX     // Catch: java.lang.Throwable -> L96
                int r9 = r12.mWindowPositionY     // Catch: java.lang.Throwable -> L96
                android.widget.-$$Lambda$Magnifier$InternalPopupWindow$vZThyvjDQhg2J1GAeOWCNqy2iiw r2 = new android.widget.-$$Lambda$Magnifier$InternalPopupWindow$vZThyvjDQhg2J1GAeOWCNqy2iiw     // Catch: java.lang.Throwable -> L96
                r6 = r2
                r7 = r12
                r6.<init>()     // Catch: java.lang.Throwable -> L96
            L6e:
                int r3 = r12.mWindowPositionX     // Catch: java.lang.Throwable -> L96
                int r4 = r12.mOffsetX     // Catch: java.lang.Throwable -> L96
                int r3 = r3 + r4
                r12.mLastDrawContentPositionX = r3     // Catch: java.lang.Throwable -> L96
                int r3 = r12.mWindowPositionY     // Catch: java.lang.Throwable -> L96
                int r4 = r12.mOffsetY     // Catch: java.lang.Throwable -> L96
                int r3 = r3 + r4
                r12.mLastDrawContentPositionY = r3     // Catch: java.lang.Throwable -> L96
                r12.mFrameDrawScheduled = r5     // Catch: java.lang.Throwable -> L96
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L96
                android.view.ThreadedRenderer$SimpleRenderer r0 = r12.mRenderer
                r0.draw(r2)
                android.widget.Magnifier$Callback r0 = r12.mCallback
                if (r0 == 0) goto L8e
                android.widget.Magnifier$Callback r0 = r12.mCallback
                r0.onOperationComplete()
            L8e:
                return
            L8f:
                r2 = move-exception
                android.view.RenderNode r3 = r12.mBitmapRenderNode     // Catch: java.lang.Throwable -> L96
                r3.end(r1)     // Catch: java.lang.Throwable -> L96
                throw r2     // Catch: java.lang.Throwable -> L96
            L96:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L96
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Magnifier.InternalPopupWindow.doDraw():void");
        }

        public static /* synthetic */ void lambda$doDraw$0(InternalPopupWindow internalPopupWindow, int pendingX, int pendingY, boolean updateWindowPosition, boolean firstDraw, long frame) {
            synchronized (internalPopupWindow.mDestroyLock) {
                if (internalPopupWindow.mSurface.isValid()) {
                    synchronized (internalPopupWindow.mLock) {
                        internalPopupWindow.mRenderer.setLightCenter(internalPopupWindow.mDisplay, pendingX, pendingY);
                        SurfaceControl.openTransaction();
                        internalPopupWindow.mSurfaceControl.deferTransactionUntil(internalPopupWindow.mSurface, frame);
                        if (updateWindowPosition) {
                            internalPopupWindow.mSurfaceControl.setPosition(pendingX, pendingY);
                        }
                        if (firstDraw) {
                            internalPopupWindow.mSurfaceControl.setLayer(5);
                            internalPopupWindow.mSurfaceControl.show();
                        }
                        SurfaceControl.closeTransaction();
                    }
                }
            }
        }
    }

    public void setOnOperationCompleteCallback(Callback callback) {
        this.mCallback = callback;
        if (this.mWindow != null) {
            this.mWindow.mCallback = callback;
        }
    }

    public Bitmap getContent() {
        Bitmap createScaledBitmap;
        if (this.mWindow != null) {
            synchronized (this.mWindow.mLock) {
                createScaledBitmap = Bitmap.createScaledBitmap(this.mWindow.mBitmap, this.mWindowWidth, this.mWindowHeight, true);
            }
            return createScaledBitmap;
        }
        return null;
    }

    public Rect getWindowPositionOnScreen() {
        int[] viewLocationOnScreen = new int[2];
        this.mView.getLocationOnScreen(viewLocationOnScreen);
        int[] viewLocationInSurface = new int[2];
        this.mView.getLocationInSurface(viewLocationInSurface);
        int left = (this.mWindowCoords.x + viewLocationOnScreen[0]) - viewLocationInSurface[0];
        int top = (this.mWindowCoords.y + viewLocationOnScreen[1]) - viewLocationInSurface[1];
        return new Rect(left, top, this.mWindowWidth + left, this.mWindowHeight + top);
    }

    public static PointF getMagnifierDefaultSize() {
        Resources resources = Resources.getSystem();
        float density = resources.getDisplayMetrics().density;
        PointF size = new PointF();
        size.x = resources.getDimension(R.dimen.magnifier_width) / density;
        size.y = resources.getDimension(R.dimen.magnifier_height) / density;
        return size;
    }
}
