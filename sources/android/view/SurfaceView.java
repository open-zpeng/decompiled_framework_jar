package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.HardwareRenderer;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RenderNode;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes3.dex */
public class SurfaceView extends View implements ViewRootImpl.WindowStoppedCallback {
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_POSITION = true;
    private static final String TAG = "SurfaceView";
    private boolean mAttachedToWindow;
    SurfaceControl mBackgroundControl;
    @UnsupportedAppUsage
    final ArrayList<SurfaceHolder.Callback> mCallbacks;
    float mCornerRadius;
    SurfaceControl mDeferredDestroySurfaceControl;
    boolean mDrawFinished;
    @UnsupportedAppUsage
    private final ViewTreeObserver.OnPreDrawListener mDrawListener;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    boolean mDrawingStopped;
    @UnsupportedAppUsage
    int mFormat;
    private boolean mGlobalListenersAdded;
    @UnsupportedAppUsage
    boolean mHaveFrame;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    boolean mIsCreating;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    long mLastLockTime;
    int mLastSurfaceHeight;
    int mLastSurfaceWidth;
    boolean mLastWindowVisibility;
    final int[] mLocation;
    private int mPendingReportDraws;
    private RenderNode.PositionUpdateListener mPositionListener;
    private Rect mRTLastReportedPosition;
    @UnsupportedAppUsage
    int mRequestedFormat;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    int mRequestedHeight;
    boolean mRequestedVisible;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    int mRequestedWidth;
    Paint mRoundedViewportPaint;
    private volatile boolean mRtHandlingPositionUpdates;
    private SurfaceControl.Transaction mRtTransaction;
    final Rect mScreenRect;
    private final ViewTreeObserver.OnScrollChangedListener mScrollChangedListener;
    int mSubLayer;
    @UnsupportedAppUsage
    final Surface mSurface;
    float mSurfaceAlpha;
    SurfaceControl mSurfaceControl;
    final Object mSurfaceControlLock;
    boolean mSurfaceCreated;
    private int mSurfaceFlags;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    final Rect mSurfaceFrame;
    int mSurfaceHeight;
    @UnsupportedAppUsage
    private final SurfaceHolder mSurfaceHolder;
    @UnsupportedAppUsage
    final ReentrantLock mSurfaceLock;
    SurfaceSession mSurfaceSession;
    int mSurfaceWidth;
    final Rect mTmpRect;
    private SurfaceControl.Transaction mTmpTransaction;
    boolean mUseAlpha;
    boolean mViewVisibility;
    boolean mVisible;
    int mWindowSpaceLeft;
    int mWindowSpaceTop;
    boolean mWindowStopped;
    boolean mWindowVisibility;

    public /* synthetic */ boolean lambda$new$0$SurfaceView() {
        this.mHaveFrame = getWidth() > 0 && getHeight() > 0;
        updateSurface();
        return true;
    }

    public SurfaceView(Context context) {
        this(context, null);
    }

    public SurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCallbacks = new ArrayList<>();
        this.mLocation = new int[2];
        this.mSurfaceLock = new ReentrantLock();
        this.mSurface = new Surface();
        this.mDrawingStopped = true;
        this.mDrawFinished = false;
        this.mScreenRect = new Rect();
        this.mSurfaceControlLock = new Object();
        this.mTmpRect = new Rect();
        this.mSubLayer = -2;
        this.mIsCreating = false;
        this.mRtHandlingPositionUpdates = false;
        this.mScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() { // from class: android.view.-$$Lambda$PYGleuqIeCxjTD1pJqqx1opFv1g
            @Override // android.view.ViewTreeObserver.OnScrollChangedListener
            public final void onScrollChanged() {
                SurfaceView.this.updateSurface();
            }
        };
        this.mDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: android.view.-$$Lambda$SurfaceView$w68OV7dB_zKVNsA-r0IrAUtyWas
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public final boolean onPreDraw() {
                return SurfaceView.this.lambda$new$0$SurfaceView();
            }
        };
        this.mRequestedVisible = false;
        this.mWindowVisibility = false;
        this.mLastWindowVisibility = false;
        this.mViewVisibility = false;
        this.mWindowStopped = false;
        this.mRequestedWidth = -1;
        this.mRequestedHeight = -1;
        this.mRequestedFormat = 4;
        this.mUseAlpha = false;
        this.mSurfaceAlpha = 1.0f;
        this.mHaveFrame = false;
        this.mSurfaceCreated = false;
        this.mLastLockTime = 0L;
        this.mVisible = false;
        this.mWindowSpaceLeft = -1;
        this.mWindowSpaceTop = -1;
        this.mSurfaceWidth = -1;
        this.mSurfaceHeight = -1;
        this.mFormat = -1;
        this.mSurfaceFrame = new Rect();
        this.mLastSurfaceWidth = -1;
        this.mLastSurfaceHeight = -1;
        this.mSurfaceFlags = 4;
        this.mRtTransaction = new SurfaceControl.Transaction();
        this.mTmpTransaction = new SurfaceControl.Transaction();
        this.mRTLastReportedPosition = new Rect();
        this.mPositionListener = new RenderNode.PositionUpdateListener() { // from class: android.view.SurfaceView.1
            @Override // android.graphics.RenderNode.PositionUpdateListener
            public void positionChanged(long frameNumber, int left, int top, int right, int bottom) {
                if (SurfaceView.this.mSurfaceControl != null) {
                    SurfaceView.this.mRtHandlingPositionUpdates = true;
                    if (SurfaceView.this.mRTLastReportedPosition.left != left || SurfaceView.this.mRTLastReportedPosition.top != top || SurfaceView.this.mRTLastReportedPosition.right != right || SurfaceView.this.mRTLastReportedPosition.bottom != bottom) {
                        try {
                            Log.d(SurfaceView.TAG, String.format("%d updateSurfacePosition RenderWorker, frameNr = %d, position = [%d, %d, %d, %d]", Integer.valueOf(System.identityHashCode(this)), Long.valueOf(frameNumber), Integer.valueOf(left), Integer.valueOf(top), Integer.valueOf(right), Integer.valueOf(bottom)));
                            SurfaceView.this.mRTLastReportedPosition.set(left, top, right, bottom);
                            SurfaceView.this.setParentSpaceRectangle(SurfaceView.this.mRTLastReportedPosition, frameNumber);
                        } catch (Exception ex) {
                            Log.e(SurfaceView.TAG, "Exception from repositionChild", ex);
                        }
                    }
                }
            }

            @Override // android.graphics.RenderNode.PositionUpdateListener
            public void positionLost(long frameNumber) {
                Log.d(SurfaceView.TAG, String.format("%d windowPositionLost, frameNr = %d", Integer.valueOf(System.identityHashCode(this)), Long.valueOf(frameNumber)));
                SurfaceView.this.mRTLastReportedPosition.setEmpty();
                if (SurfaceView.this.mSurfaceControl == null) {
                    return;
                }
                if (frameNumber > 0) {
                    ViewRootImpl viewRoot = SurfaceView.this.getViewRootImpl();
                    SurfaceView.this.mRtTransaction.deferTransactionUntilSurface(SurfaceView.this.mSurfaceControl, viewRoot.mSurface, frameNumber);
                }
                SurfaceView.this.mRtTransaction.hide(SurfaceView.this.mSurfaceControl);
                SurfaceView.this.mRtTransaction.apply();
            }
        };
        this.mSurfaceHolder = new AnonymousClass2();
        this.mRenderNode.addPositionUpdateListener(this.mPositionListener);
        setWillNotDraw(true);
    }

    public SurfaceHolder getHolder() {
        return this.mSurfaceHolder;
    }

    private void updateRequestedVisibility() {
        this.mRequestedVisible = this.mViewVisibility && this.mWindowVisibility && !this.mWindowStopped;
    }

    @Override // android.view.ViewRootImpl.WindowStoppedCallback
    public void windowStopped(boolean stopped) {
        this.mWindowStopped = stopped;
        updateRequestedVisibility();
        updateSurface();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewRootImpl().addWindowStoppedCallback(this);
        this.mWindowStopped = false;
        this.mViewVisibility = getVisibility() == 0;
        updateRequestedVisibility();
        this.mAttachedToWindow = true;
        this.mParent.requestTransparentRegion(this);
        if (!this.mGlobalListenersAdded) {
            ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnScrollChangedListener(this.mScrollChangedListener);
            observer.addOnPreDrawListener(this.mDrawListener);
            this.mGlobalListenersAdded = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mWindowVisibility = visibility == 0;
        updateRequestedVisibility();
        updateSurface();
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        boolean newRequestedVisible = true;
        this.mViewVisibility = visibility == 0;
        if (!this.mWindowVisibility || !this.mViewVisibility || this.mWindowStopped) {
            newRequestedVisible = false;
        }
        if (newRequestedVisible != this.mRequestedVisible) {
            requestLayout();
        }
        this.mRequestedVisible = newRequestedVisible;
        updateSurface();
    }

    public void setUseAlpha() {
        if (!this.mUseAlpha) {
            this.mUseAlpha = true;
            updateSurfaceAlpha();
        }
    }

    @Override // android.view.View
    public void setAlpha(float alpha) {
        Log.d(TAG, System.identityHashCode(this) + " setAlpha: mUseAlpha = " + this.mUseAlpha + " alpha=" + alpha);
        super.setAlpha(alpha);
        updateSurfaceAlpha();
    }

    private float getFixedAlpha() {
        float alpha = getAlpha();
        if (!this.mUseAlpha || (this.mSubLayer <= 0 && alpha != 0.0f)) {
            return 1.0f;
        }
        return alpha;
    }

    private void updateSurfaceAlpha() {
        if (!this.mUseAlpha) {
            Log.d(TAG, System.identityHashCode(this) + " updateSurfaceAlpha: setUseAlpha() is not called, ignored.");
            return;
        }
        float viewAlpha = getAlpha();
        if (this.mSubLayer < 0 && 0.0f < viewAlpha && viewAlpha < 1.0f) {
            Log.w(TAG, System.identityHashCode(this) + " updateSurfaceAlpha: translucent color is not supported for a surface placed z-below.");
        }
        if (!this.mHaveFrame) {
            Log.d(TAG, System.identityHashCode(this) + " updateSurfaceAlpha: has no surface.");
            return;
        }
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot == null) {
            Log.d(TAG, System.identityHashCode(this) + " updateSurfaceAlpha: ViewRootImpl not available.");
        } else if (this.mSurfaceControl == null) {
            Log.d(TAG, System.identityHashCode(this) + "updateSurfaceAlpha: surface is not yet created, or already released.");
        } else {
            final Surface parent = viewRoot.mSurface;
            if (parent == null || !parent.isValid()) {
                Log.d(TAG, System.identityHashCode(this) + " updateSurfaceAlpha: ViewRootImpl has no valid surface");
                return;
            }
            final float alpha = getFixedAlpha();
            if (alpha != this.mSurfaceAlpha) {
                if (isHardwareAccelerated()) {
                    viewRoot.registerRtFrameCallback(new HardwareRenderer.FrameDrawingCallback() { // from class: android.view.-$$Lambda$SurfaceView$gQ7nwl8ux5A5myBds296J6rgF14
                        @Override // android.graphics.HardwareRenderer.FrameDrawingCallback
                        public final void onFrameDraw(long j) {
                            SurfaceView.this.lambda$updateSurfaceAlpha$1$SurfaceView(parent, alpha, j);
                        }
                    });
                    damageInParent();
                } else {
                    Log.d(TAG, System.identityHashCode(this) + " updateSurfaceAlpha: set alpha=" + alpha);
                    SurfaceControl.openTransaction();
                    try {
                        this.mSurfaceControl.setAlpha(alpha);
                    } finally {
                        SurfaceControl.closeTransaction();
                    }
                }
                this.mSurfaceAlpha = alpha;
            }
        }
    }

    public /* synthetic */ void lambda$updateSurfaceAlpha$1$SurfaceView(Surface parent, float alpha, long frame) {
        try {
            SurfaceControl.Transaction t = new SurfaceControl.Transaction();
            synchronized (this.mSurfaceControlLock) {
                if (!parent.isValid()) {
                    Log.d(TAG, System.identityHashCode(this) + " updateSurfaceAlpha RT: ViewRootImpl has no valid surface");
                } else if (this.mSurfaceControl == null) {
                    Log.d(TAG, System.identityHashCode(this) + "updateSurfaceAlpha RT: mSurfaceControl has already released");
                } else {
                    Log.d(TAG, System.identityHashCode(this) + " updateSurfaceAlpha RT: set alpha=" + alpha);
                    t.setAlpha(this.mSurfaceControl, alpha);
                    t.deferTransactionUntilSurface(this.mSurfaceControl, parent, frame);
                    t.apply();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, System.identityHashCode(this) + "updateSurfaceAlpha RT: Exception during surface transaction", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performDrawFinished() {
        if (this.mPendingReportDraws > 0) {
            this.mDrawFinished = true;
            if (this.mAttachedToWindow) {
                notifyDrawFinished();
                invalidate();
                return;
            }
            return;
        }
        Log.e(TAG, System.identityHashCode(this) + "finished drawing but no pending report draw (extra call to draw completion runnable?)");
    }

    void notifyDrawFinished() {
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot != null) {
            viewRoot.pendingDrawFinished();
        }
        this.mPendingReportDraws--;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDetachedFromWindow() {
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot != null) {
            viewRoot.removeWindowStoppedCallback(this);
        }
        this.mAttachedToWindow = false;
        if (this.mGlobalListenersAdded) {
            ViewTreeObserver observer = getViewTreeObserver();
            observer.removeOnScrollChangedListener(this.mScrollChangedListener);
            observer.removeOnPreDrawListener(this.mDrawListener);
            this.mGlobalListenersAdded = false;
        }
        while (this.mPendingReportDraws > 0) {
            notifyDrawFinished();
        }
        this.mRequestedVisible = false;
        updateSurface();
        releaseSurfaces();
        this.mHaveFrame = false;
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int i = this.mRequestedWidth;
        if (i >= 0) {
            width = resolveSizeAndState(i, widthMeasureSpec, 0);
        } else {
            width = getDefaultSize(0, widthMeasureSpec);
        }
        int i2 = this.mRequestedHeight;
        if (i2 >= 0) {
            height = resolveSizeAndState(i2, heightMeasureSpec, 0);
        } else {
            height = getDefaultSize(0, heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    @UnsupportedAppUsage
    public boolean setFrame(int left, int top, int right, int bottom) {
        boolean result = super.setFrame(left, top, right, bottom);
        updateSurface();
        return result;
    }

    @Override // android.view.View
    public boolean gatherTransparentRegion(Region region) {
        if (isAboveParent() || !this.mDrawFinished) {
            return super.gatherTransparentRegion(region);
        }
        boolean opaque = true;
        if ((this.mPrivateFlags & 128) == 0) {
            opaque = super.gatherTransparentRegion(region);
        } else if (region != null) {
            int w = getWidth();
            int h = getHeight();
            if (w > 0 && h > 0) {
                getLocationInWindow(this.mLocation);
                int[] iArr = this.mLocation;
                int l = iArr[0];
                int t = iArr[1];
                region.op(l, t, l + w, t + h, Region.Op.UNION);
            }
        }
        if (PixelFormat.formatHasAlpha(this.mRequestedFormat)) {
            return false;
        }
        return opaque;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (this.mDrawFinished && !isAboveParent() && (this.mPrivateFlags & 128) == 0) {
            clearSurfaceViewPort(canvas);
        }
        super.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (this.mDrawFinished && !isAboveParent() && (this.mPrivateFlags & 128) == 128) {
            clearSurfaceViewPort(canvas);
        }
        super.dispatchDraw(canvas);
    }

    private void clearSurfaceViewPort(Canvas canvas) {
        if (this.mCornerRadius > 0.0f) {
            canvas.getClipBounds(this.mTmpRect);
            float f = this.mCornerRadius;
            canvas.drawRoundRect(this.mTmpRect.left, this.mTmpRect.top, this.mTmpRect.right, this.mTmpRect.bottom, f, f, this.mRoundedViewportPaint);
            return;
        }
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    public void setCornerRadius(float cornerRadius) {
        this.mCornerRadius = cornerRadius;
        if (this.mCornerRadius > 0.0f && this.mRoundedViewportPaint == null) {
            this.mRoundedViewportPaint = new Paint(1);
            this.mRoundedViewportPaint.setBlendMode(BlendMode.CLEAR);
            this.mRoundedViewportPaint.setColor(0);
        }
        invalidate();
    }

    public void setZOrderMediaOverlay(boolean isMediaOverlay) {
        this.mSubLayer = isMediaOverlay ? -1 : -2;
    }

    public void setZOrderOnTop(boolean onTop) {
        if (onTop) {
            this.mSubLayer = 1;
        } else {
            this.mSubLayer = -2;
        }
    }

    public void setSecure(boolean isSecure) {
        if (isSecure) {
            this.mSurfaceFlags |= 128;
        } else {
            this.mSurfaceFlags &= -129;
        }
    }

    private void updateOpaqueFlag() {
        if (!PixelFormat.formatHasAlpha(this.mRequestedFormat)) {
            this.mSurfaceFlags |= 1024;
        } else {
            this.mSurfaceFlags &= -1025;
        }
    }

    private void updateBackgroundVisibilityInTransaction(SurfaceControl viewRoot) {
        SurfaceControl surfaceControl = this.mBackgroundControl;
        if (surfaceControl == null) {
            return;
        }
        if (this.mSubLayer < 0 && (this.mSurfaceFlags & 1024) != 0) {
            surfaceControl.show();
            this.mBackgroundControl.setRelativeLayer(viewRoot, Integer.MIN_VALUE);
            return;
        }
        this.mBackgroundControl.hide();
    }

    private void releaseSurfaces() {
        synchronized (this.mSurfaceControlLock) {
            if (this.mSurfaceControl != null) {
                this.mTmpTransaction.remove(this.mSurfaceControl);
                this.mSurfaceControl = null;
            }
            if (this.mBackgroundControl != null) {
                this.mTmpTransaction.remove(this.mBackgroundControl);
                this.mBackgroundControl = null;
            }
            this.mTmpTransaction.apply();
        }
        this.mSurfaceAlpha = 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't wrap try/catch for region: R(14:83|84|86|87|(2:312|313)(1:89)|90|(5:303|304|305|306|307)(1:92)|(5:(30:(2:95|(31:97|98|(2:101|102)|113|114|(1:280)|117|(2:278|279)(1:119)|120|(2:122|(21:124|125|126|127|128|(3:259|260|(15:262|131|132|133|(1:(4:238|239|240|(6:242|243|(2:245|246)|247|248|(1:250))(1:252))(1:138))(1:255)|(1:140)|(1:144)|(3:156|157|(10:159|(8:210|211|212|(1:214)(1:231)|215|(5:217|218|219|220|221)|226|227)|(10:184|185|186|187|188|(2:200|201)(1:190)|191|(4:193|194|195|196)|198|199)(1:169)|(3:171|(1:173)(1:183)|174)|147|148|(1:152)|153|154|155))|146|147|148|(2:150|152)|153|154|155))|130|131|132|133|(0)(0)|(0)|(2:142|144)|(0)|146|147|148|(0)|153|154|155))|276|125|126|127|128|(0)|130|131|132|133|(0)(0)|(0)|(0)|(0)|146|147|148|(0)|153|154|155))|113|114|(0)|280|117|(0)(0)|120|(0)|276|125|126|127|128|(0)|130|131|132|133|(0)(0)|(0)|(0)|(0)|146|147|148|(0)|153|154|155)|295|296|98|(2:101|102))|289|290|291|292|293|294) */
    /* JADX WARN: Can't wrap try/catch for region: R(17:(14:(2:95|(31:97|98|(2:101|102)|113|114|(1:280)|117|(2:278|279)(1:119)|120|(2:122|(21:124|125|126|127|128|(3:259|260|(15:262|131|132|133|(1:(4:238|239|240|(6:242|243|(2:245|246)|247|248|(1:250))(1:252))(1:138))(1:255)|(1:140)|(1:144)|(3:156|157|(10:159|(8:210|211|212|(1:214)(1:231)|215|(5:217|218|219|220|221)|226|227)|(10:184|185|186|187|188|(2:200|201)(1:190)|191|(4:193|194|195|196)|198|199)(1:169)|(3:171|(1:173)(1:183)|174)|147|148|(1:152)|153|154|155))|146|147|148|(2:150|152)|153|154|155))|130|131|132|133|(0)(0)|(0)|(2:142|144)|(0)|146|147|148|(0)|153|154|155))|276|125|126|127|128|(0)|130|131|132|133|(0)(0)|(0)|(0)|(0)|146|147|148|(0)|153|154|155))|132|133|(0)(0)|(0)|(0)|(0)|146|147|148|(0)|153|154|155)|113|114|(0)|280|117|(0)(0)|120|(0)|276|125|126|127|128|(0)|130|131) */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x06d0, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x06d1, code lost:
        r5 = r19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x06f9, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x0705, code lost:
        r0 = th;
     */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0477 A[Catch: all -> 0x06e5, TRY_ENTER, TryCatch #1 {all -> 0x06e5, blocks: (B:181:0x044a, B:185:0x0454, B:192:0x048f, B:191:0x0477), top: B:361:0x044a }] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x049b A[Catch: all -> 0x046c, TRY_ENTER, TRY_LEAVE, TryCatch #3 {all -> 0x046c, blocks: (B:187:0x045f, B:194:0x049b), top: B:365:0x045f }] */
    /* JADX WARN: Removed duplicated region for block: B:211:0x04c9  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x053b  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0541 A[Catch: all -> 0x0549, TryCatch #23 {all -> 0x0549, blocks: (B:231:0x0541, B:235:0x0554, B:237:0x0562, B:250:0x059e, B:222:0x0505, B:223:0x051b, B:225:0x0525), top: B:405:0x0505 }] */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0554 A[Catch: all -> 0x0549, TryCatch #23 {all -> 0x0549, blocks: (B:231:0x0541, B:235:0x0554, B:237:0x0562, B:250:0x059e, B:222:0x0505, B:223:0x051b, B:225:0x0525), top: B:405:0x0505 }] */
    /* JADX WARN: Removed duplicated region for block: B:307:0x06a4 A[Catch: Exception -> 0x0739, TryCatch #29 {Exception -> 0x0739, blocks: (B:340:0x0733, B:341:0x0738, B:305:0x069e, B:307:0x06a4, B:309:0x06a8, B:313:0x06bb, B:315:0x06c2, B:317:0x06c6, B:319:0x06cf), top: B:363:0x0368 }] */
    /* JADX WARN: Removed duplicated region for block: B:315:0x06c2 A[Catch: Exception -> 0x0739, TryCatch #29 {Exception -> 0x0739, blocks: (B:340:0x0733, B:341:0x0738, B:305:0x069e, B:307:0x06a4, B:309:0x06a8, B:313:0x06bb, B:315:0x06c2, B:317:0x06c6, B:319:0x06cf), top: B:363:0x0368 }] */
    /* JADX WARN: Removed duplicated region for block: B:365:0x045f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:373:0x04af A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:409:0x056b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void updateSurface() {
        /*
            Method dump skipped, instructions count: 2000
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.SurfaceView.updateSurface():void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDrawFinished() {
        Log.i(TAG, System.identityHashCode(this) + " finishedDrawing");
        SurfaceControl surfaceControl = this.mDeferredDestroySurfaceControl;
        if (surfaceControl != null) {
            this.mTmpTransaction.remove(surfaceControl).apply();
            this.mDeferredDestroySurfaceControl = null;
        }
        runOnUiThread(new Runnable() { // from class: android.view.-$$Lambda$SurfaceView$TWz4D2u33ZlAmRtgKzbqqDue3iM
            @Override // java.lang.Runnable
            public final void run() {
                SurfaceView.this.performDrawFinished();
            }
        });
    }

    protected void applyChildSurfaceTransaction_renderWorker(SurfaceControl.Transaction t, Surface viewRootSurface, long nextViewRootFrameNumber) {
    }

    private void applySurfaceTransforms(SurfaceControl surface, Rect position, long frameNumber) {
        if (frameNumber > 0) {
            ViewRootImpl viewRoot = getViewRootImpl();
            this.mRtTransaction.deferTransactionUntilSurface(surface, viewRoot.mSurface, frameNumber);
        }
        this.mRtTransaction.setPosition(surface, position.left, position.top);
        this.mRtTransaction.setMatrix(surface, position.width() / this.mSurfaceWidth, 0.0f, 0.0f, position.height() / this.mSurfaceHeight);
        if (this.mViewVisibility) {
            this.mRtTransaction.show(surface);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setParentSpaceRectangle(Rect position, long frameNumber) {
        ViewRootImpl viewRoot = getViewRootImpl();
        applySurfaceTransforms(this.mSurfaceControl, position, frameNumber);
        applyChildSurfaceTransaction_renderWorker(this.mRtTransaction, viewRoot.mSurface, frameNumber);
        this.mRtTransaction.apply();
    }

    private SurfaceHolder.Callback[] getSurfaceCallbacks() {
        SurfaceHolder.Callback[] callbacks;
        synchronized (this.mCallbacks) {
            callbacks = new SurfaceHolder.Callback[this.mCallbacks.size()];
            this.mCallbacks.toArray(callbacks);
        }
        return callbacks;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runOnUiThread(Runnable runnable) {
        Handler handler = getHandler();
        if (handler != null && handler.getLooper() != Looper.myLooper()) {
            handler.post(runnable);
        } else {
            runnable.run();
        }
    }

    @UnsupportedAppUsage
    public boolean isFixedSize() {
        return (this.mRequestedWidth == -1 && this.mRequestedHeight == -1) ? false : true;
    }

    private boolean isAboveParent() {
        return this.mSubLayer >= 0;
    }

    public void setResizeBackgroundColor(int bgColor) {
        if (this.mBackgroundControl == null) {
            return;
        }
        float[] colorComponents = {Color.red(bgColor) / 255.0f, Color.green(bgColor) / 255.0f, Color.blue(bgColor) / 255.0f};
        SurfaceControl.openTransaction();
        try {
            this.mBackgroundControl.setColor(colorComponents);
        } finally {
            SurfaceControl.closeTransaction();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.view.SurfaceView$2  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements SurfaceHolder {
        private static final String LOG_TAG = "SurfaceHolder";

        AnonymousClass2() {
        }

        @Override // android.view.SurfaceHolder
        public boolean isCreating() {
            return SurfaceView.this.mIsCreating;
        }

        @Override // android.view.SurfaceHolder
        public void addCallback(SurfaceHolder.Callback callback) {
            synchronized (SurfaceView.this.mCallbacks) {
                if (!SurfaceView.this.mCallbacks.contains(callback)) {
                    SurfaceView.this.mCallbacks.add(callback);
                }
            }
        }

        @Override // android.view.SurfaceHolder
        public void removeCallback(SurfaceHolder.Callback callback) {
            synchronized (SurfaceView.this.mCallbacks) {
                SurfaceView.this.mCallbacks.remove(callback);
            }
        }

        @Override // android.view.SurfaceHolder
        public void setFixedSize(int width, int height) {
            if (SurfaceView.this.mRequestedWidth != width || SurfaceView.this.mRequestedHeight != height) {
                SurfaceView surfaceView = SurfaceView.this;
                surfaceView.mRequestedWidth = width;
                surfaceView.mRequestedHeight = height;
                surfaceView.requestLayout();
            }
        }

        @Override // android.view.SurfaceHolder
        public void setSizeFromLayout() {
            if (SurfaceView.this.mRequestedWidth != -1 || SurfaceView.this.mRequestedHeight != -1) {
                SurfaceView surfaceView = SurfaceView.this;
                surfaceView.mRequestedHeight = -1;
                surfaceView.mRequestedWidth = -1;
                surfaceView.requestLayout();
            }
        }

        @Override // android.view.SurfaceHolder
        public void setFormat(int format) {
            if (format == -1) {
                format = 4;
            }
            SurfaceView surfaceView = SurfaceView.this;
            surfaceView.mRequestedFormat = format;
            if (surfaceView.mSurfaceControl != null) {
                SurfaceView.this.updateSurface();
            }
        }

        @Override // android.view.SurfaceHolder
        @Deprecated
        public void setType(int type) {
        }

        public /* synthetic */ void lambda$setKeepScreenOn$0$SurfaceView$2(boolean screenOn) {
            SurfaceView.this.setKeepScreenOn(screenOn);
        }

        @Override // android.view.SurfaceHolder
        public void setKeepScreenOn(final boolean screenOn) {
            SurfaceView.this.runOnUiThread(new Runnable() { // from class: android.view.-$$Lambda$SurfaceView$2$yPLKBEhjLeg2pTjLhVjBxCxl3rE
                @Override // java.lang.Runnable
                public final void run() {
                    SurfaceView.AnonymousClass2.this.lambda$setKeepScreenOn$0$SurfaceView$2(screenOn);
                }
            });
        }

        @Override // android.view.SurfaceHolder
        public Canvas lockCanvas() {
            return internalLockCanvas(null, false);
        }

        @Override // android.view.SurfaceHolder
        public Canvas lockCanvas(Rect inOutDirty) {
            return internalLockCanvas(inOutDirty, false);
        }

        @Override // android.view.SurfaceHolder
        public Canvas lockHardwareCanvas() {
            return internalLockCanvas(null, true);
        }

        private Canvas internalLockCanvas(Rect dirty, boolean hardware) {
            SurfaceView.this.mSurfaceLock.lock();
            Log.i(SurfaceView.TAG, System.identityHashCode(this) + " Locking canvas... stopped=" + SurfaceView.this.mDrawingStopped + ", surfaceControl=" + SurfaceView.this.mSurfaceControl);
            Canvas c = null;
            if (!SurfaceView.this.mDrawingStopped && SurfaceView.this.mSurfaceControl != null) {
                try {
                    if (hardware) {
                        c = SurfaceView.this.mSurface.lockHardwareCanvas();
                    } else {
                        c = SurfaceView.this.mSurface.lockCanvas(dirty);
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Exception locking surface", e);
                }
            }
            Log.i(SurfaceView.TAG, System.identityHashCode(this) + " Returned canvas: " + c);
            if (c != null) {
                SurfaceView.this.mLastLockTime = SystemClock.uptimeMillis();
                return c;
            }
            long now = SystemClock.uptimeMillis();
            long nextTime = SurfaceView.this.mLastLockTime + 100;
            if (nextTime > now) {
                try {
                    Thread.sleep(nextTime - now);
                } catch (InterruptedException e2) {
                }
                now = SystemClock.uptimeMillis();
            }
            SurfaceView surfaceView = SurfaceView.this;
            surfaceView.mLastLockTime = now;
            surfaceView.mSurfaceLock.unlock();
            return null;
        }

        @Override // android.view.SurfaceHolder
        public void unlockCanvasAndPost(Canvas canvas) {
            SurfaceView.this.mSurface.unlockCanvasAndPost(canvas);
            SurfaceView.this.mSurfaceLock.unlock();
        }

        @Override // android.view.SurfaceHolder
        public Surface getSurface() {
            return SurfaceView.this.mSurface;
        }

        @Override // android.view.SurfaceHolder
        public Rect getSurfaceFrame() {
            return SurfaceView.this.mSurfaceFrame;
        }
    }

    public SurfaceControl getSurfaceControl() {
        return this.mSurfaceControl;
    }
}
