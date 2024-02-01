package android.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
@Deprecated
/* loaded from: classes3.dex */
public class SlidingDrawer extends ViewGroup {
    private static final int ANIMATION_FRAME_DURATION = 16;
    private static final int COLLAPSED_FULL_CLOSED = -10002;
    private static final int EXPANDED_FULL_OPEN = -10001;
    private static final float MAXIMUM_ACCELERATION = 2000.0f;
    private static final float MAXIMUM_MAJOR_VELOCITY = 200.0f;
    private static final float MAXIMUM_MINOR_VELOCITY = 150.0f;
    private static final float MAXIMUM_TAP_VELOCITY = 100.0f;
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;
    private static final int TAP_THRESHOLD = 6;
    private static final int VELOCITY_UNITS = 1000;
    private boolean mAllowSingleTap;
    private boolean mAnimateOnClick;
    private float mAnimatedAcceleration;
    private float mAnimatedVelocity;
    private boolean mAnimating;
    private long mAnimationLastTime;
    private float mAnimationPosition;
    private int mBottomOffset;
    private View mContent;
    private final int mContentId;
    private long mCurrentAnimationTime;
    private boolean mExpanded;
    private final Rect mFrame;
    private View mHandle;
    private int mHandleHeight;
    private final int mHandleId;
    private int mHandleWidth;
    private final Rect mInvalidate;
    private boolean mLocked;
    private final int mMaximumAcceleration;
    private final int mMaximumMajorVelocity;
    private final int mMaximumMinorVelocity;
    private final int mMaximumTapVelocity;
    private OnDrawerCloseListener mOnDrawerCloseListener;
    private OnDrawerOpenListener mOnDrawerOpenListener;
    private OnDrawerScrollListener mOnDrawerScrollListener;
    private final Runnable mSlidingRunnable;
    private final int mTapThreshold;
    public protected int mTopOffset;
    public protected int mTouchDelta;
    public protected boolean mTracking;
    public protected VelocityTracker mVelocityTracker;
    private final int mVelocityUnits;
    private boolean mVertical;

    /* loaded from: classes3.dex */
    public interface OnDrawerCloseListener {
        void onDrawerClosed();
    }

    /* loaded from: classes3.dex */
    public interface OnDrawerOpenListener {
        void onDrawerOpened();
    }

    /* loaded from: classes3.dex */
    public interface OnDrawerScrollListener {
        void onScrollEnded();

        void onScrollStarted();
    }

    public SlidingDrawer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SlidingDrawer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mFrame = new Rect();
        this.mInvalidate = new Rect();
        this.mSlidingRunnable = new Runnable() { // from class: android.widget.SlidingDrawer.1
            @Override // java.lang.Runnable
            public void run() {
                SlidingDrawer.this.doAnimation();
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingDrawer, defStyleAttr, defStyleRes);
        int orientation = a.getInt(0, 1);
        this.mVertical = orientation == 1;
        this.mBottomOffset = (int) a.getDimension(1, 0.0f);
        this.mTopOffset = (int) a.getDimension(2, 0.0f);
        this.mAllowSingleTap = a.getBoolean(3, true);
        this.mAnimateOnClick = a.getBoolean(6, true);
        int handleId = a.getResourceId(4, 0);
        if (handleId != 0) {
            int contentId = a.getResourceId(5, 0);
            if (contentId == 0) {
                throw new IllegalArgumentException("The content attribute is required and must refer to a valid child.");
            }
            if (handleId == contentId) {
                throw new IllegalArgumentException("The content and handle attributes must refer to different children.");
            }
            this.mHandleId = handleId;
            this.mContentId = contentId;
            float density = getResources().getDisplayMetrics().density;
            this.mTapThreshold = (int) ((6.0f * density) + 0.5f);
            this.mMaximumTapVelocity = (int) ((100.0f * density) + 0.5f);
            this.mMaximumMinorVelocity = (int) ((MAXIMUM_MINOR_VELOCITY * density) + 0.5f);
            this.mMaximumMajorVelocity = (int) ((MAXIMUM_MAJOR_VELOCITY * density) + 0.5f);
            this.mMaximumAcceleration = (int) ((MAXIMUM_ACCELERATION * density) + 0.5f);
            this.mVelocityUnits = (int) ((1000.0f * density) + 0.5f);
            a.recycle();
            setAlwaysDrawnWithCacheEnabled(false);
            return;
        }
        throw new IllegalArgumentException("The handle attribute is required and must refer to a valid child.");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        this.mHandle = findViewById(this.mHandleId);
        if (this.mHandle == null) {
            throw new IllegalArgumentException("The handle attribute is must refer to an existing child.");
        }
        this.mHandle.setOnClickListener(new DrawerToggler());
        this.mContent = findViewById(this.mContentId);
        if (this.mContent == null) {
            throw new IllegalArgumentException("The content attribute is must refer to an existing child.");
        }
        this.mContent.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == 0 || heightSpecMode == 0) {
            throw new RuntimeException("SlidingDrawer cannot have UNSPECIFIED dimensions");
        }
        View handle = this.mHandle;
        measureChild(handle, widthMeasureSpec, heightMeasureSpec);
        if (this.mVertical) {
            int height = (heightSpecSize - handle.getMeasuredHeight()) - this.mTopOffset;
            this.mContent.measure(View.MeasureSpec.makeMeasureSpec(widthSpecSize, 1073741824), View.MeasureSpec.makeMeasureSpec(height, 1073741824));
        } else {
            int width = (widthSpecSize - handle.getMeasuredWidth()) - this.mTopOffset;
            this.mContent.measure(View.MeasureSpec.makeMeasureSpec(width, 1073741824), View.MeasureSpec.makeMeasureSpec(heightSpecSize, 1073741824));
        }
        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        float left;
        long drawingTime = getDrawingTime();
        View handle = this.mHandle;
        boolean isVertical = this.mVertical;
        drawChild(canvas, handle, drawingTime);
        if (this.mTracking || this.mAnimating) {
            Bitmap cache = this.mContent.getDrawingCache();
            if (cache != null) {
                if (isVertical) {
                    canvas.drawBitmap(cache, 0.0f, handle.getBottom(), (Paint) null);
                    return;
                } else {
                    canvas.drawBitmap(cache, handle.getRight(), 0.0f, (Paint) null);
                    return;
                }
            }
            canvas.save();
            if (!isVertical) {
                left = handle.getLeft() - this.mTopOffset;
            } else {
                left = 0.0f;
            }
            canvas.translate(left, isVertical ? handle.getTop() - this.mTopOffset : 0.0f);
            drawChild(canvas, this.mContent, drawingTime);
            canvas.restore();
        } else if (this.mExpanded) {
            drawChild(canvas, this.mContent, drawingTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft;
        int childTop;
        if (this.mTracking) {
            return;
        }
        int width = r - l;
        int height = b - t;
        View handle = this.mHandle;
        int childWidth = handle.getMeasuredWidth();
        int childHeight = handle.getMeasuredHeight();
        View content = this.mContent;
        if (this.mVertical) {
            childLeft = (width - childWidth) / 2;
            childTop = this.mExpanded ? this.mTopOffset : (height - childHeight) + this.mBottomOffset;
            content.layout(0, this.mTopOffset + childHeight, content.getMeasuredWidth(), this.mTopOffset + childHeight + content.getMeasuredHeight());
        } else {
            childLeft = this.mExpanded ? this.mTopOffset : (width - childWidth) + this.mBottomOffset;
            childTop = (height - childHeight) / 2;
            content.layout(this.mTopOffset + childWidth, 0, this.mTopOffset + childWidth + content.getMeasuredWidth(), content.getMeasuredHeight());
        }
        handle.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        this.mHandleHeight = handle.getHeight();
        this.mHandleWidth = handle.getWidth();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.mLocked) {
            return false;
        }
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        Rect frame = this.mFrame;
        View handle = this.mHandle;
        handle.getHitRect(frame);
        if (this.mTracking || frame.contains((int) x, (int) y)) {
            if (action == 0) {
                this.mTracking = true;
                handle.setPressed(true);
                prepareContent();
                if (this.mOnDrawerScrollListener != null) {
                    this.mOnDrawerScrollListener.onScrollStarted();
                }
                if (this.mVertical) {
                    int top = this.mHandle.getTop();
                    this.mTouchDelta = ((int) y) - top;
                    prepareTracking(top);
                } else {
                    int left = this.mHandle.getLeft();
                    this.mTouchDelta = ((int) x) - left;
                    prepareTracking(left);
                }
                this.mVelocityTracker.addMovement(event);
            }
            return true;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:61:0x00d4, code lost:
        if (r13.mAllowSingleTap == false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00d6, code lost:
        playSoundEffect(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00db, code lost:
        if (r13.mExpanded == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00dd, code lost:
        if (r6 == false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00df, code lost:
        r11 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00e1, code lost:
        r11 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00e2, code lost:
        animateClose(r11, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00e6, code lost:
        if (r6 == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00e8, code lost:
        r11 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00ea, code lost:
        r11 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x00eb, code lost:
        animateOpen(r11, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x00ef, code lost:
        if (r6 == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x00f1, code lost:
        r11 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x00f3, code lost:
        r11 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x00f4, code lost:
        performFling(r11, r7, false, true);
     */
    /* JADX WARN: Removed duplicated region for block: B:77:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00fc  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r14) {
        /*
            Method dump skipped, instructions count: 294
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.SlidingDrawer.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private synchronized void animateClose(int position, boolean notifyScrollListener) {
        prepareTracking(position);
        performFling(position, this.mMaximumAcceleration, true, notifyScrollListener);
    }

    private synchronized void animateOpen(int position, boolean notifyScrollListener) {
        prepareTracking(position);
        performFling(position, -this.mMaximumAcceleration, true, notifyScrollListener);
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0063, code lost:
        if (r8 > (-r6.mMaximumMajorVelocity)) goto L34;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized void performFling(int r7, float r8, boolean r9, boolean r10) {
        /*
            r6 = this;
            float r0 = (float) r7
            r6.mAnimationPosition = r0
            r6.mAnimatedVelocity = r8
            boolean r0 = r6.mExpanded
            r1 = 0
            if (r0 == 0) goto L43
            if (r9 != 0) goto L37
            int r0 = r6.mMaximumMajorVelocity
            float r0 = (float) r0
            int r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r0 > 0) goto L37
            int r0 = r6.mTopOffset
            boolean r2 = r6.mVertical
            if (r2 == 0) goto L1c
            int r2 = r6.mHandleHeight
            goto L1e
        L1c:
            int r2 = r6.mHandleWidth
        L1e:
            int r0 = r0 + r2
            if (r7 <= r0) goto L2a
            int r0 = r6.mMaximumMajorVelocity
            int r0 = -r0
            float r0 = (float) r0
            int r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r0 <= 0) goto L2a
            goto L37
        L2a:
            int r0 = r6.mMaximumAcceleration
            int r0 = -r0
            float r0 = (float) r0
            r6.mAnimatedAcceleration = r0
            int r0 = (r8 > r1 ? 1 : (r8 == r1 ? 0 : -1))
            if (r0 <= 0) goto L7d
            r6.mAnimatedVelocity = r1
            goto L7d
        L37:
            int r0 = r6.mMaximumAcceleration
            float r0 = (float) r0
            r6.mAnimatedAcceleration = r0
            int r0 = (r8 > r1 ? 1 : (r8 == r1 ? 0 : -1))
            if (r0 >= 0) goto L7d
            r6.mAnimatedVelocity = r1
            goto L7d
        L43:
            if (r9 != 0) goto L71
            int r0 = r6.mMaximumMajorVelocity
            float r0 = (float) r0
            int r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r0 > 0) goto L65
            boolean r0 = r6.mVertical
            if (r0 == 0) goto L55
            int r0 = r6.getHeight()
            goto L59
        L55:
            int r0 = r6.getWidth()
        L59:
            int r0 = r0 / 2
            if (r7 <= r0) goto L71
            int r0 = r6.mMaximumMajorVelocity
            int r0 = -r0
            float r0 = (float) r0
            int r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r0 <= 0) goto L71
        L65:
            int r0 = r6.mMaximumAcceleration
            float r0 = (float) r0
            r6.mAnimatedAcceleration = r0
            int r0 = (r8 > r1 ? 1 : (r8 == r1 ? 0 : -1))
            if (r0 >= 0) goto L7d
            r6.mAnimatedVelocity = r1
            goto L7d
        L71:
            int r0 = r6.mMaximumAcceleration
            int r0 = -r0
            float r0 = (float) r0
            r6.mAnimatedAcceleration = r0
            int r0 = (r8 > r1 ? 1 : (r8 == r1 ? 0 : -1))
            if (r0 <= 0) goto L7d
            r6.mAnimatedVelocity = r1
        L7d:
            long r0 = android.os.SystemClock.uptimeMillis()
            r6.mAnimationLastTime = r0
            r2 = 16
            long r4 = r0 + r2
            r6.mCurrentAnimationTime = r4
            r4 = 1
            r6.mAnimating = r4
            java.lang.Runnable r4 = r6.mSlidingRunnable
            r6.removeCallbacks(r4)
            java.lang.Runnable r4 = r6.mSlidingRunnable
            r6.postDelayed(r4, r2)
            r6.stopTracking(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.SlidingDrawer.performFling(int, float, boolean, boolean):void");
    }

    public protected void prepareTracking(int position) {
        int width;
        int i;
        this.mTracking = true;
        this.mVelocityTracker = VelocityTracker.obtain();
        boolean opening = !this.mExpanded;
        if (opening) {
            this.mAnimatedAcceleration = this.mMaximumAcceleration;
            this.mAnimatedVelocity = this.mMaximumMajorVelocity;
            int i2 = this.mBottomOffset;
            if (this.mVertical) {
                width = getHeight();
                i = this.mHandleHeight;
            } else {
                width = getWidth();
                i = this.mHandleWidth;
            }
            this.mAnimationPosition = i2 + (width - i);
            moveHandle((int) this.mAnimationPosition);
            this.mAnimating = true;
            removeCallbacks(this.mSlidingRunnable);
            long now = SystemClock.uptimeMillis();
            this.mAnimationLastTime = now;
            this.mCurrentAnimationTime = 16 + now;
            this.mAnimating = true;
            return;
        }
        if (this.mAnimating) {
            this.mAnimating = false;
            removeCallbacks(this.mSlidingRunnable);
        }
        moveHandle(position);
    }

    private synchronized void moveHandle(int position) {
        View handle = this.mHandle;
        if (this.mVertical) {
            if (position == EXPANDED_FULL_OPEN) {
                handle.offsetTopAndBottom(this.mTopOffset - handle.getTop());
                invalidate();
            } else if (position == COLLAPSED_FULL_CLOSED) {
                handle.offsetTopAndBottom((((this.mBottomOffset + this.mBottom) - this.mTop) - this.mHandleHeight) - handle.getTop());
                invalidate();
            } else {
                int top = handle.getTop();
                int deltaY = position - top;
                if (position < this.mTopOffset) {
                    deltaY = this.mTopOffset - top;
                } else if (deltaY > (((this.mBottomOffset + this.mBottom) - this.mTop) - this.mHandleHeight) - top) {
                    deltaY = (((this.mBottomOffset + this.mBottom) - this.mTop) - this.mHandleHeight) - top;
                }
                handle.offsetTopAndBottom(deltaY);
                Rect frame = this.mFrame;
                Rect region = this.mInvalidate;
                handle.getHitRect(frame);
                region.set(frame);
                region.union(frame.left, frame.top - deltaY, frame.right, frame.bottom - deltaY);
                region.union(0, frame.bottom - deltaY, getWidth(), (frame.bottom - deltaY) + this.mContent.getHeight());
                invalidate(region);
            }
        } else if (position == EXPANDED_FULL_OPEN) {
            handle.offsetLeftAndRight(this.mTopOffset - handle.getLeft());
            invalidate();
        } else if (position == COLLAPSED_FULL_CLOSED) {
            handle.offsetLeftAndRight((((this.mBottomOffset + this.mRight) - this.mLeft) - this.mHandleWidth) - handle.getLeft());
            invalidate();
        } else {
            int left = handle.getLeft();
            int deltaX = position - left;
            if (position < this.mTopOffset) {
                deltaX = this.mTopOffset - left;
            } else if (deltaX > (((this.mBottomOffset + this.mRight) - this.mLeft) - this.mHandleWidth) - left) {
                deltaX = (((this.mBottomOffset + this.mRight) - this.mLeft) - this.mHandleWidth) - left;
            }
            handle.offsetLeftAndRight(deltaX);
            Rect frame2 = this.mFrame;
            Rect region2 = this.mInvalidate;
            handle.getHitRect(frame2);
            region2.set(frame2);
            region2.union(frame2.left - deltaX, frame2.top, frame2.right - deltaX, frame2.bottom);
            region2.union(frame2.right - deltaX, 0, (frame2.right - deltaX) + this.mContent.getWidth(), getHeight());
            invalidate(region2);
        }
    }

    public protected void prepareContent() {
        if (this.mAnimating) {
            return;
        }
        View content = this.mContent;
        if (content.isLayoutRequested()) {
            if (this.mVertical) {
                int childHeight = this.mHandleHeight;
                int height = ((this.mBottom - this.mTop) - childHeight) - this.mTopOffset;
                content.measure(View.MeasureSpec.makeMeasureSpec(this.mRight - this.mLeft, 1073741824), View.MeasureSpec.makeMeasureSpec(height, 1073741824));
                content.layout(0, this.mTopOffset + childHeight, content.getMeasuredWidth(), this.mTopOffset + childHeight + content.getMeasuredHeight());
            } else {
                int childWidth = this.mHandle.getWidth();
                int width = ((this.mRight - this.mLeft) - childWidth) - this.mTopOffset;
                content.measure(View.MeasureSpec.makeMeasureSpec(width, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mBottom - this.mTop, 1073741824));
                content.layout(this.mTopOffset + childWidth, 0, this.mTopOffset + childWidth + content.getMeasuredWidth(), content.getMeasuredHeight());
            }
        }
        content.getViewTreeObserver().dispatchOnPreDraw();
        if (!content.isHardwareAccelerated()) {
            content.buildDrawingCache();
        }
        content.setVisibility(8);
    }

    private synchronized void stopTracking(boolean notifyScrollListener) {
        this.mHandle.setPressed(false);
        this.mTracking = false;
        if (notifyScrollListener && this.mOnDrawerScrollListener != null) {
            this.mOnDrawerScrollListener.onScrollEnded();
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void doAnimation() {
        if (this.mAnimating) {
            incrementAnimation();
            if (this.mAnimationPosition >= (this.mBottomOffset + (this.mVertical ? getHeight() : getWidth())) - 1) {
                this.mAnimating = false;
                closeDrawer();
            } else if (this.mAnimationPosition < this.mTopOffset) {
                this.mAnimating = false;
                openDrawer();
            } else {
                moveHandle((int) this.mAnimationPosition);
                this.mCurrentAnimationTime += 16;
                postDelayed(this.mSlidingRunnable, 16L);
            }
        }
    }

    private synchronized void incrementAnimation() {
        long now = SystemClock.uptimeMillis();
        float t = ((float) (now - this.mAnimationLastTime)) / 1000.0f;
        float position = this.mAnimationPosition;
        float v = this.mAnimatedVelocity;
        float a = this.mAnimatedAcceleration;
        this.mAnimationPosition = (v * t) + position + (0.5f * a * t * t);
        this.mAnimatedVelocity = (a * t) + v;
        this.mAnimationLastTime = now;
    }

    public void toggle() {
        if (!this.mExpanded) {
            openDrawer();
        } else {
            closeDrawer();
        }
        invalidate();
        requestLayout();
    }

    public void animateToggle() {
        if (!this.mExpanded) {
            animateOpen();
        } else {
            animateClose();
        }
    }

    public void open() {
        openDrawer();
        invalidate();
        requestLayout();
        sendAccessibilityEvent(32);
    }

    public void close() {
        closeDrawer();
        invalidate();
        requestLayout();
    }

    public void animateClose() {
        prepareContent();
        OnDrawerScrollListener scrollListener = this.mOnDrawerScrollListener;
        if (scrollListener != null) {
            scrollListener.onScrollStarted();
        }
        animateClose(this.mVertical ? this.mHandle.getTop() : this.mHandle.getLeft(), false);
        if (scrollListener != null) {
            scrollListener.onScrollEnded();
        }
    }

    public void animateOpen() {
        prepareContent();
        OnDrawerScrollListener scrollListener = this.mOnDrawerScrollListener;
        if (scrollListener != null) {
            scrollListener.onScrollStarted();
        }
        animateOpen(this.mVertical ? this.mHandle.getTop() : this.mHandle.getLeft(), false);
        sendAccessibilityEvent(32);
        if (scrollListener != null) {
            scrollListener.onScrollEnded();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return SlidingDrawer.class.getName();
    }

    private synchronized void closeDrawer() {
        moveHandle(COLLAPSED_FULL_CLOSED);
        this.mContent.setVisibility(8);
        this.mContent.destroyDrawingCache();
        if (!this.mExpanded) {
            return;
        }
        this.mExpanded = false;
        if (this.mOnDrawerCloseListener != null) {
            this.mOnDrawerCloseListener.onDrawerClosed();
        }
    }

    private synchronized void openDrawer() {
        moveHandle(EXPANDED_FULL_OPEN);
        this.mContent.setVisibility(0);
        if (this.mExpanded) {
            return;
        }
        this.mExpanded = true;
        if (this.mOnDrawerOpenListener != null) {
            this.mOnDrawerOpenListener.onDrawerOpened();
        }
    }

    public void setOnDrawerOpenListener(OnDrawerOpenListener onDrawerOpenListener) {
        this.mOnDrawerOpenListener = onDrawerOpenListener;
    }

    public void setOnDrawerCloseListener(OnDrawerCloseListener onDrawerCloseListener) {
        this.mOnDrawerCloseListener = onDrawerCloseListener;
    }

    public void setOnDrawerScrollListener(OnDrawerScrollListener onDrawerScrollListener) {
        this.mOnDrawerScrollListener = onDrawerScrollListener;
    }

    public View getHandle() {
        return this.mHandle;
    }

    public View getContent() {
        return this.mContent;
    }

    public void unlock() {
        this.mLocked = false;
    }

    public void lock() {
        this.mLocked = true;
    }

    public boolean isOpened() {
        return this.mExpanded;
    }

    public boolean isMoving() {
        return this.mTracking || this.mAnimating;
    }

    /* loaded from: classes3.dex */
    private class DrawerToggler implements View.OnClickListener {
        private DrawerToggler() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (!SlidingDrawer.this.mLocked) {
                if (SlidingDrawer.this.mAnimateOnClick) {
                    SlidingDrawer.this.animateToggle();
                } else {
                    SlidingDrawer.this.toggle();
                }
            }
        }
    }
}
