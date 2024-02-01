package android.view;

import android.content.Context;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver;
import com.xiaopeng.app.xpActivityManager;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: classes2.dex */
public class SurfaceView extends View implements ViewRootImpl.WindowStoppedCallback {
    private static final boolean DEBUG = true;
    private static final String TAG = "SurfaceView";
    private boolean mAttachedToWindow;
    public private protected final ArrayList<SurfaceHolder.Callback> mCallbacks;
    final Configuration mConfiguration;
    SurfaceControl mDeferredDestroySurfaceControl;
    boolean mDrawFinished;
    public protected final ViewTreeObserver.OnPreDrawListener mDrawListener;
    public private protected boolean mDrawingStopped;
    public private protected int mFormat;
    private boolean mGlobalListenersAdded;
    public private protected boolean mHaveFrame;
    public private protected boolean mIsCreating;
    public private protected long mLastLockTime;
    int mLastSurfaceHeight;
    int mLastSurfaceWidth;
    boolean mLastWindowVisibility;
    final int[] mLocation;
    private int mPendingReportDraws;
    private Rect mRTLastReportedPosition;
    public private protected int mRequestedFormat;
    public private protected int mRequestedHeight;
    boolean mRequestedVisible;
    public private protected int mRequestedWidth;
    private int mRoundCornerFlag;
    private int mRoundCornerRadius;
    private int mRoundCornerType;
    private volatile boolean mRtHandlingPositionUpdates;
    private SurfaceControl.Transaction mRtTransaction;
    final Rect mScreenRect;
    private final ViewTreeObserver.OnScrollChangedListener mScrollChangedListener;
    int mSubLayer;
    public private protected final Surface mSurface;
    SurfaceControlWithBackground mSurfaceControl;
    boolean mSurfaceCreated;
    private int mSurfaceFlags;
    public private protected final Rect mSurfaceFrame;
    int mSurfaceHeight;
    public protected final SurfaceHolder mSurfaceHolder;
    public private protected final ReentrantLock mSurfaceLock;
    SurfaceSession mSurfaceSession;
    int mSurfaceWidth;
    final Rect mTmpRect;
    private CompatibilityInfo.Translator mTranslator;
    boolean mViewVisibility;
    boolean mVisible;
    int mWindowSpaceLeft;
    int mWindowSpaceTop;
    boolean mWindowStopped;
    boolean mWindowVisibility;

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
        this.mTmpRect = new Rect();
        this.mConfiguration = new Configuration();
        this.mSubLayer = -2;
        this.mIsCreating = false;
        this.mRtHandlingPositionUpdates = false;
        this.mScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() { // from class: android.view.SurfaceView.1
            @Override // android.view.ViewTreeObserver.OnScrollChangedListener
            public void onScrollChanged() {
                SurfaceView.this.updateSurface();
            }
        };
        this.mDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: android.view.SurfaceView.2
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                SurfaceView.this.mHaveFrame = SurfaceView.this.getWidth() > 0 && SurfaceView.this.getHeight() > 0;
                SurfaceView.this.updateSurface();
                return true;
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
        this.mRTLastReportedPosition = new Rect();
        this.mSurfaceHolder = new AnonymousClass3();
        this.mRoundCornerFlag = -1;
        this.mRoundCornerType = -1;
        this.mRoundCornerRadius = -1;
        this.mRenderNode.requestPositionUpdates(this);
        setWillNotDraw(true);
    }

    public SurfaceHolder getHolder() {
        return this.mSurfaceHolder;
    }

    private synchronized void updateRequestedVisibility() {
        this.mRequestedVisible = this.mViewVisibility && this.mWindowVisibility && !this.mWindowStopped;
    }

    @Override // android.view.ViewRootImpl.WindowStoppedCallback
    public synchronized void windowStopped(boolean stopped) {
        Log.i(TAG, System.identityHashCode(this) + " windowStopped stopped=" + stopped);
        this.mWindowStopped = stopped;
        updateRequestedVisibility();
        updateSurface();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, System.identityHashCode(this) + " onAttachedToWindow");
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
        Log.i(TAG, System.identityHashCode(this) + " onWindowVisibilityChanged() visibility:" + visibility);
        updateRequestedVisibility();
        updateSurface();
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        boolean newRequestedVisible = false;
        this.mViewVisibility = visibility == 0;
        if (this.mWindowVisibility && this.mViewVisibility && !this.mWindowStopped) {
            newRequestedVisible = true;
        }
        if (newRequestedVisible != this.mRequestedVisible) {
            requestLayout();
        }
        this.mRequestedVisible = newRequestedVisible;
        updateSurface();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void performDrawFinished() {
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

    synchronized void notifyDrawFinished() {
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
        Log.i(TAG, System.identityHashCode(this) + " onDetachedFromWindow");
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
        if (this.mSurfaceControl != null) {
            this.mSurfaceControl.destroy();
        }
        this.mSurfaceControl = null;
        this.mHaveFrame = false;
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        if (this.mRequestedWidth >= 0) {
            width = resolveSizeAndState(this.mRequestedWidth, widthMeasureSpec, 0);
        } else {
            width = getDefaultSize(0, widthMeasureSpec);
        }
        if (this.mRequestedHeight >= 0) {
            height = resolveSizeAndState(this.mRequestedHeight, heightMeasureSpec, 0);
        } else {
            height = getDefaultSize(0, heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    public private boolean setFrame(int left, int top, int right, int bottom) {
        boolean result = super.setFrame(left, top, right, bottom);
        updateSurface();
        return result;
    }

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
                int l = this.mLocation[0];
                int t = this.mLocation[1];
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
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        super.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (this.mDrawFinished && !isAboveParent() && (this.mPrivateFlags & 128) == 128) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        super.dispatchDraw(canvas);
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

    private synchronized void updateOpaqueFlag() {
        if (!PixelFormat.formatHasAlpha(this.mRequestedFormat)) {
            this.mSurfaceFlags |= 1024;
        } else {
            this.mSurfaceFlags &= -1025;
        }
    }

    private synchronized Rect getParentSurfaceInsets() {
        ViewRootImpl root = getViewRootImpl();
        if (root == null) {
            return null;
        }
        return root.mWindowAttributes.surfaceInsets;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't wrap try/catch for region: R(33:11|(1:13)|14|(1:16)|17|(1:19)|20|(1:22)(1:317)|23|(1:25)(1:316)|26|(1:315)(1:30)|31|(1:312)(1:35)|36|(1:38)(1:311)|39|(1:310)(1:(1:45)(8:277|(1:309)(1:281)|282|(1:308)(1:286)|287|(4:293|(1:295)|296|(1:298)(2:299|(2:303|304)))(1:290)|291|292))|46|(1:48)(1:276)|49|(1:51)(1:275)|52|(20:53|54|(2:271|272)|56|(3:58|(1:60)(1:267)|61)(2:268|(1:270))|62|(1:64)(1:262)|65|66|67|68|(2:254|255)(1:70)|(1:252)|(1:76)|88|(1:250)(1:91)|92|93|(2:246|247)(1:95)|96)|(15:(2:98|(22:100|101|102|103|104|(3:230|231|(16:233|107|108|109|(11:(4:208|209|210|(6:212|(4:214|215|216|217)|219|220|(1:222)|223))(1:114)|(1:116)|(1:120)|(3:132|133|(18:135|(8:182|183|184|(2:186|187)(1:201)|188|(5:190|191|192|193|194)|196|197)|140|141|(8:148|(3:150|(1:152)(1:162)|153)|123|124|(1:128)|129|130|131)|163|(2:165|166)(1:179)|167|(4:169|170|171|172)|174|175|(0)|123|124|(2:126|128)|129|130|131))|122|123|124|(0)|129|130|131)|226|(0)|(2:118|120)|(0)|122|123|124|(0)|129|130|131))|106|107|108|109|(0)|226|(0)|(0)|(0)|122|123|124|(0)|129|130|131))|108|109|(0)|226|(0)|(0)|(0)|122|123|124|(0)|129|130|131)|245|101|102|103|104|(0)|106|107) */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x0672, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x0673, code lost:
        r18 = r12;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:168:0x03ec  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x0462 A[Catch: all -> 0x046a, TryCatch #8 {all -> 0x046a, blocks: (B:189:0x0462, B:193:0x0473, B:195:0x0481, B:208:0x0527, B:180:0x042e, B:181:0x0441, B:183:0x044d), top: B:309:0x042e }] */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0473 A[Catch: all -> 0x046a, TryCatch #8 {all -> 0x046a, blocks: (B:189:0x0462, B:193:0x0473, B:195:0x0481, B:208:0x0527, B:180:0x042e, B:181:0x0441, B:183:0x044d), top: B:309:0x042e }] */
    /* JADX WARN: Removed duplicated region for block: B:243:0x05eb A[Catch: all -> 0x0629, TryCatch #7 {all -> 0x0629, blocks: (B:243:0x05eb, B:245:0x0607, B:247:0x0610, B:240:0x05d7), top: B:307:0x05d7 }] */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0640 A[Catch: Exception -> 0x069e, TryCatch #14 {Exception -> 0x069e, blocks: (B:257:0x063a, B:259:0x0640, B:261:0x0644, B:265:0x0659, B:267:0x0660, B:269:0x0664, B:270:0x0671, B:283:0x0698, B:284:0x069d), top: B:319:0x02a5 }] */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0660 A[Catch: Exception -> 0x069e, TryCatch #14 {Exception -> 0x069e, blocks: (B:257:0x063a, B:259:0x0640, B:261:0x0644, B:265:0x0659, B:267:0x0660, B:269:0x0664, B:270:0x0671, B:283:0x0698, B:284:0x069d), top: B:319:0x02a5 }] */
    /* JADX WARN: Removed duplicated region for block: B:295:0x039c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:317:0x048a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v128 */
    /* JADX WARN: Type inference failed for: r0v78 */
    /* JADX WARN: Type inference failed for: r0v80 */
    /* JADX WARN: Type inference failed for: r12v20 */
    /* JADX WARN: Type inference failed for: r12v21 */
    /* JADX WARN: Type inference failed for: r12v23 */
    /* JADX WARN: Type inference failed for: r12v27 */
    /* JADX WARN: Type inference failed for: r12v28, types: [boolean] */
    /* JADX WARN: Type inference failed for: r12v29 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void updateSurface() {
        /*
            Method dump skipped, instructions count: 1790
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.SurfaceView.updateSurface():void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onDrawFinished() {
        Log.i(TAG, System.identityHashCode(this) + " finishedDrawing");
        if (this.mDeferredDestroySurfaceControl != null) {
            this.mDeferredDestroySurfaceControl.destroy();
            this.mDeferredDestroySurfaceControl = null;
        }
        runOnUiThread(new Runnable() { // from class: android.view.-$$Lambda$SurfaceView$Cs7TGTdA1lXf9qW8VOJAfEsMjdk
            @Override // java.lang.Runnable
            public final void run() {
                SurfaceView.this.performDrawFinished();
            }
        });
    }

    protected synchronized void applyChildSurfaceTransaction_renderWorker(SurfaceControl.Transaction t, Surface viewRootSurface, long nextViewRootFrameNumber) {
    }

    private synchronized void applySurfaceTransforms(SurfaceControl surface, Rect position, long frameNumber) {
        if (frameNumber > 0) {
            ViewRootImpl viewRoot = getViewRootImpl();
            this.mRtTransaction.deferTransactionUntilSurface(surface, viewRoot.mSurface, frameNumber);
        }
        this.mRtTransaction.setPosition(surface, position.left, position.top);
        this.mRtTransaction.setMatrix(surface, position.width() / this.mSurfaceWidth, 0.0f, 0.0f, position.height() / this.mSurfaceHeight);
    }

    private synchronized void setParentSpaceRectangle(Rect position, long frameNumber) {
        ViewRootImpl viewRoot = getViewRootImpl();
        applySurfaceTransforms(this.mSurfaceControl, position, frameNumber);
        applySurfaceTransforms(this.mSurfaceControl.mBackgroundControl, position, frameNumber);
        applyChildSurfaceTransaction_renderWorker(this.mRtTransaction, viewRoot.mSurface, frameNumber);
        this.mRtTransaction.apply();
    }

    private protected final void updateSurfacePosition_renderWorker(long frameNumber, int left, int top, int right, int bottom) {
        if (this.mSurfaceControl == null) {
            return;
        }
        this.mRtHandlingPositionUpdates = true;
        if (this.mRTLastReportedPosition.left == left && this.mRTLastReportedPosition.top == top && this.mRTLastReportedPosition.right == right && this.mRTLastReportedPosition.bottom == bottom) {
            return;
        }
        try {
            Log.d(TAG, String.format("%d updateSurfacePosition RenderWorker, frameNr = %d, postion = [%d, %d, %d, %d]", Integer.valueOf(System.identityHashCode(this)), Long.valueOf(frameNumber), Integer.valueOf(left), Integer.valueOf(top), Integer.valueOf(right), Integer.valueOf(bottom)));
            this.mRTLastReportedPosition.set(left, top, right, bottom);
            setParentSpaceRectangle(this.mRTLastReportedPosition, frameNumber);
        } catch (Exception ex) {
            Log.e(TAG, "Exception from repositionChild", ex);
        }
    }

    private protected final void surfacePositionLost_uiRtSync(long frameNumber) {
        Log.d(TAG, String.format("%d windowPositionLost, frameNr = %d", Integer.valueOf(System.identityHashCode(this)), Long.valueOf(frameNumber)));
        this.mRTLastReportedPosition.setEmpty();
        if (this.mSurfaceControl != null && this.mRtHandlingPositionUpdates) {
            this.mRtHandlingPositionUpdates = false;
            if (!this.mScreenRect.isEmpty() && !this.mScreenRect.equals(this.mRTLastReportedPosition)) {
                try {
                    Log.d(TAG, String.format("%d updateSurfacePosition, postion = [%d, %d, %d, %d]", Integer.valueOf(System.identityHashCode(this)), Integer.valueOf(this.mScreenRect.left), Integer.valueOf(this.mScreenRect.top), Integer.valueOf(this.mScreenRect.right), Integer.valueOf(this.mScreenRect.bottom)));
                    setParentSpaceRectangle(this.mScreenRect, frameNumber);
                } catch (Exception ex) {
                    Log.e(TAG, "Exception configuring surface", ex);
                }
            }
        }
    }

    private synchronized SurfaceHolder.Callback[] getSurfaceCallbacks() {
        SurfaceHolder.Callback[] callbacks;
        synchronized (this.mCallbacks) {
            callbacks = new SurfaceHolder.Callback[this.mCallbacks.size()];
            this.mCallbacks.toArray(callbacks);
        }
        return callbacks;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void runOnUiThread(Runnable runnable) {
        Handler handler = getHandler();
        if (handler != null && handler.getLooper() != Looper.myLooper()) {
            handler.post(runnable);
        } else {
            runnable.run();
        }
    }

    private protected boolean isFixedSize() {
        return (this.mRequestedWidth == -1 && this.mRequestedHeight == -1) ? false : true;
    }

    private synchronized boolean isAboveParent() {
        return this.mSubLayer >= 0;
    }

    public synchronized void setResizeBackgroundColor(int bgColor) {
        this.mSurfaceControl.setBackgroundColor(bgColor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.view.SurfaceView$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements SurfaceHolder {
        private static final String LOG_TAG = "SurfaceHolder";

        AnonymousClass3() {
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
                SurfaceView.this.mRequestedWidth = width;
                SurfaceView.this.mRequestedHeight = height;
                SurfaceView.this.requestLayout();
            }
        }

        @Override // android.view.SurfaceHolder
        public void setSizeFromLayout() {
            if (SurfaceView.this.mRequestedWidth != -1 || SurfaceView.this.mRequestedHeight != -1) {
                SurfaceView surfaceView = SurfaceView.this;
                SurfaceView.this.mRequestedHeight = -1;
                surfaceView.mRequestedWidth = -1;
                SurfaceView.this.requestLayout();
            }
        }

        @Override // android.view.SurfaceHolder
        public void setFormat(int format) {
            if (format == -1) {
                format = 4;
            }
            SurfaceView.this.mRequestedFormat = format;
            if (SurfaceView.this.mSurfaceControl != null) {
                SurfaceView.this.updateSurface();
            }
        }

        @Override // android.view.SurfaceHolder
        @Deprecated
        public void setType(int type) {
        }

        @Override // android.view.SurfaceHolder
        public void setKeepScreenOn(final boolean screenOn) {
            SurfaceView.this.runOnUiThread(new Runnable() { // from class: android.view.-$$Lambda$SurfaceView$3$XvaZSTTyv1kHN4GtX5NDdmQTRp8
                @Override // java.lang.Runnable
                public final void run() {
                    SurfaceView.this.setKeepScreenOn(screenOn);
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
            SurfaceView.this.mLastLockTime = now;
            SurfaceView.this.mSurfaceLock.unlock();
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class SurfaceControlWithBackground extends SurfaceControl {
        SurfaceControl mBackgroundControl;
        int mHeight;
        private boolean mOpaque;
        public boolean mVisible;
        int mWidth;
        float mX;
        float mY;

        public SurfaceControlWithBackground(String name, boolean opaque, SurfaceControl.Builder b) throws Exception {
            super(b.setName(name).build());
            this.mOpaque = true;
            this.mVisible = false;
            this.mX = 0.0f;
            this.mY = 0.0f;
            this.mWidth = 0;
            this.mHeight = 0;
            this.mBackgroundControl = b.setName("Background for -" + name).setFormat(1024).setColorLayer(true).build();
            this.mOpaque = opaque;
            this.mOpaque = SurfaceViewFactory.isOpaqueBackground(this.mOpaque, SurfaceView.this.mContext, SurfaceView.this.mRoundCornerFlag, SurfaceView.this.mRoundCornerType, SurfaceView.this.mRoundCornerRadius);
            SurfaceControl.openTransaction();
            try {
                try {
                    SurfaceViewFactory.setRoundCornerLocked(this.mBackgroundControl, SurfaceView.this.mRoundCornerFlag, SurfaceView.this.mRoundCornerType, SurfaceView.this.mRoundCornerRadius);
                } catch (Exception e) {
                    Log.d(SurfaceView.TAG, "setRoundCornerLocked trans e=" + e);
                }
                Log.i(SurfaceView.TAG, "SurfaceControlWithBackground create name=" + name + " opaque=" + this.mOpaque + " visible=" + this.mVisible);
            } finally {
                SurfaceControl.closeTransaction();
            }
        }

        @Override // android.view.SurfaceControl
        public synchronized void setAlpha(float alpha) {
            super.setAlpha(alpha);
            this.mBackgroundControl.setAlpha(alpha);
        }

        public synchronized void setLayer(int zorder) {
            super.setLayer(zorder);
            this.mBackgroundControl.setLayer(-3);
        }

        public synchronized void setPosition(final float x, final float y) {
            super.setPosition(x, y);
            boolean delayTransaction = SurfaceViewFactory.delayTransactionBackground(SurfaceView.this.mContext);
            if (delayTransaction && (x > this.mX || y > this.mY)) {
                SurfaceView.this.getHandler().postDelayed(new Runnable() { // from class: android.view.-$$Lambda$SurfaceView$SurfaceControlWithBackground$isQcGhDzABkyUqLCOKwtGj-x1HE
                    @Override // java.lang.Runnable
                    public final void run() {
                        SurfaceView.SurfaceControlWithBackground.lambda$setPosition$0(SurfaceView.SurfaceControlWithBackground.this, x, y);
                    }
                }, SurfaceViewFactory.BACKGROUND_TRANSACTION_DELAY);
            } else {
                this.mBackgroundControl.setPosition(x, y);
            }
            this.mX = x;
            this.mY = y;
        }

        public static /* synthetic */ void lambda$setPosition$0(SurfaceControlWithBackground surfaceControlWithBackground, float x, float y) {
            if (surfaceControlWithBackground.mBackgroundControl != null) {
                surfaceControlWithBackground.mBackgroundControl.setPosition(x, y);
            }
        }

        @Override // android.view.SurfaceControl
        public synchronized void setSize(final int w, final int h) {
            super.setSize(w, h);
            boolean delayTransaction = SurfaceViewFactory.delayTransactionBackground(SurfaceView.this.mContext);
            if (delayTransaction && (w < this.mWidth || h < this.mHeight)) {
                SurfaceView.this.getHandler().postDelayed(new Runnable() { // from class: android.view.-$$Lambda$SurfaceView$SurfaceControlWithBackground$ErDWfnCS0OIMlL-V-egf_inWodA
                    @Override // java.lang.Runnable
                    public final void run() {
                        SurfaceView.SurfaceControlWithBackground.lambda$setSize$1(SurfaceView.SurfaceControlWithBackground.this, w, h);
                    }
                }, SurfaceViewFactory.BACKGROUND_TRANSACTION_DELAY);
            } else {
                this.mBackgroundControl.setSize(w, h);
            }
            this.mWidth = w;
            this.mHeight = h;
        }

        public static /* synthetic */ void lambda$setSize$1(SurfaceControlWithBackground surfaceControlWithBackground, int w, int h) {
            if (surfaceControlWithBackground.mBackgroundControl != null) {
                surfaceControlWithBackground.mBackgroundControl.setSize(w, h);
            }
        }

        @Override // android.view.SurfaceControl
        public synchronized void setWindowCrop(Rect crop) {
            super.setWindowCrop(crop);
            this.mBackgroundControl.setWindowCrop(crop);
        }

        @Override // android.view.SurfaceControl
        public synchronized void setFinalCrop(Rect crop) {
            super.setFinalCrop(crop);
            this.mBackgroundControl.setFinalCrop(crop);
        }

        @Override // android.view.SurfaceControl
        public synchronized void setLayerStack(int layerStack) {
            super.setLayerStack(layerStack);
            this.mBackgroundControl.setLayerStack(layerStack);
        }

        @Override // android.view.SurfaceControl
        public synchronized void setOpaque(boolean isOpaque) {
            super.setOpaque(isOpaque);
            this.mOpaque = isOpaque;
            updateBackgroundVisibility();
        }

        @Override // android.view.SurfaceControl
        public synchronized void setSecure(boolean isSecure) {
            super.setSecure(isSecure);
        }

        @Override // android.view.SurfaceControl
        public synchronized void setMatrix(float dsdx, float dtdx, float dsdy, float dtdy) {
            super.setMatrix(dsdx, dtdx, dsdy, dtdy);
            this.mBackgroundControl.setMatrix(dsdx, dtdx, dsdy, dtdy);
        }

        public synchronized void hide() {
            super.hide();
            this.mVisible = false;
            updateBackgroundVisibility();
        }

        public synchronized void show() {
            super.show();
            this.mVisible = true;
            updateBackgroundVisibility();
        }

        @Override // android.view.SurfaceControl
        public synchronized void destroy() {
            super.destroy();
            boolean delayTransaction = SurfaceViewFactory.delayTransactionBackground(SurfaceView.this.mContext);
            if (delayTransaction) {
                SurfaceView.this.getHandler().postDelayed(new Runnable() { // from class: android.view.-$$Lambda$SurfaceView$SurfaceControlWithBackground$KdkZA1YCPNKuvfUC8zvI_XowR7U
                    @Override // java.lang.Runnable
                    public final void run() {
                        SurfaceView.SurfaceControlWithBackground.lambda$destroy$2(SurfaceView.SurfaceControlWithBackground.this);
                    }
                }, SurfaceViewFactory.BACKGROUND_TRANSACTION_DELAY);
            } else {
                this.mBackgroundControl.destroy();
            }
        }

        public static /* synthetic */ void lambda$destroy$2(SurfaceControlWithBackground surfaceControlWithBackground) {
            if (surfaceControlWithBackground.mBackgroundControl != null) {
                surfaceControlWithBackground.mBackgroundControl.destroy();
            }
        }

        @Override // android.view.SurfaceControl
        public synchronized void release() {
            super.release();
            this.mBackgroundControl.release();
        }

        @Override // android.view.SurfaceControl
        public synchronized void setTransparentRegionHint(Region region) {
            super.setTransparentRegionHint(region);
            this.mBackgroundControl.setTransparentRegionHint(region);
        }

        @Override // android.view.SurfaceControl
        public synchronized void deferTransactionUntil(IBinder handle, long frame) {
            super.deferTransactionUntil(handle, frame);
            this.mBackgroundControl.deferTransactionUntil(handle, frame);
        }

        @Override // android.view.SurfaceControl
        public synchronized void deferTransactionUntil(Surface barrier, long frame) {
            super.deferTransactionUntil(barrier, frame);
            this.mBackgroundControl.deferTransactionUntil(barrier, frame);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setBackgroundColor(int bgColor) {
            float[] colorComponents = {Color.red(bgColor) / 255.0f, Color.green(bgColor) / 255.0f, Color.blue(bgColor) / 255.0f};
            SurfaceControl.openTransaction();
            try {
                SurfaceViewFactory.setRoundCornerLocked(this.mBackgroundControl, SurfaceView.this.mRoundCornerFlag, SurfaceView.this.mRoundCornerType, SurfaceView.this.mRoundCornerRadius);
                this.mBackgroundControl.setColor(colorComponents);
            } finally {
                SurfaceControl.closeTransaction();
            }
        }

        synchronized void updateBackgroundVisibility() {
            SurfaceViewFactory.setRoundCornerLocked(this.mBackgroundControl, SurfaceView.this.mRoundCornerFlag, SurfaceView.this.mRoundCornerType, SurfaceView.this.mRoundCornerRadius);
            if (this.mOpaque && this.mVisible) {
                this.mBackgroundControl.show();
            } else {
                this.mBackgroundControl.hide();
            }
        }
    }

    public void setRoundCorner(int flag, int type, int radius) {
        this.mRoundCornerFlag = flag;
        this.mRoundCornerType = type;
        this.mRoundCornerRadius = radius;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class SurfaceViewFactory {
        public static final long BACKGROUND_TRANSACTION_DELAY = 2000;

        private SurfaceViewFactory() {
        }

        public static void setRoundCornerLocked(SurfaceControl surface, int flag, int type, int radius) {
            if (surface != null && hasRoundCorner(flag, type, radius)) {
                try {
                    surface.setRoundCorner(flag, type, radius);
                } catch (Exception e) {
                    Log.d(SurfaceView.TAG, "setRoundCornerLocked e=" + e);
                }
            }
        }

        public static boolean isOpaqueBackground(boolean opaque, Context context, int flag, int type, int radius) {
            String packageName = context != null ? context.getBasePackageName() : "";
            boolean hasRoundCorner = hasRoundCorner(flag, type, radius);
            boolean systemApplication = xpActivityManager.isSystemApplication(packageName);
            if (hasRoundCorner) {
                return false;
            }
            if (systemApplication) {
                return opaque;
            }
            return true;
        }

        public static boolean delayTransactionBackground(Context context) {
            String packageName = context != null ? context.getBasePackageName() : "";
            boolean systemApplication = xpActivityManager.isSystemApplication(packageName);
            return !systemApplication;
        }

        private static boolean hasRoundCorner(int flag, int type, int radius) {
            return flag >= 0 && type >= 0 && radius >= 0;
        }
    }
}
