package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.OverScroller;
import android.widget.ScrollView;
import com.android.internal.R;
/* loaded from: classes3.dex */
public class ResolverDrawerLayout extends ViewGroup {
    private static final String TAG = "ResolverDrawerLayout";
    private int mActivePointerId;
    private float mCollapseOffset;
    private int mCollapsibleHeight;
    private int mCollapsibleHeightReserved;
    private boolean mDismissLocked;
    private boolean mDismissOnScrollerFinished;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private boolean mIsDragging;
    private float mLastTouchY;
    private int mMaxCollapsedHeight;
    private int mMaxCollapsedHeightSmall;
    private int mMaxWidth;
    private final float mMinFlingVelocity;
    private OnDismissedListener mOnDismissedListener;
    private boolean mOpenOnClick;
    private boolean mOpenOnLayout;
    private RunOnDismissedListener mRunOnDismissedListener;
    private Drawable mScrollIndicatorDrawable;
    private final OverScroller mScroller;
    private boolean mShowAtTop;
    private boolean mSmallCollapsed;
    private final Rect mTempRect;
    private int mTopOffset;
    private final ViewTreeObserver.OnTouchModeChangeListener mTouchModeChangeListener;
    private final int mTouchSlop;
    private int mUncollapsibleHeight;
    private final VelocityTracker mVelocityTracker;

    /* loaded from: classes3.dex */
    public interface OnDismissedListener {
        void onDismissed();
    }

    public ResolverDrawerLayout(Context context) {
        this(context, null);
    }

    public ResolverDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResolverDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mActivePointerId = -1;
        this.mTempRect = new Rect();
        this.mTouchModeChangeListener = new ViewTreeObserver.OnTouchModeChangeListener() { // from class: com.android.internal.widget.ResolverDrawerLayout.1
            @Override // android.view.ViewTreeObserver.OnTouchModeChangeListener
            public void onTouchModeChanged(boolean isInTouchMode) {
                if (!isInTouchMode && ResolverDrawerLayout.this.hasFocus() && ResolverDrawerLayout.this.isDescendantClipped(ResolverDrawerLayout.this.getFocusedChild())) {
                    ResolverDrawerLayout.this.smoothScrollTo(0, 0.0f);
                }
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ResolverDrawerLayout, defStyleAttr, 0);
        this.mMaxWidth = a.getDimensionPixelSize(0, -1);
        this.mMaxCollapsedHeight = a.getDimensionPixelSize(1, 0);
        this.mMaxCollapsedHeightSmall = a.getDimensionPixelSize(2, this.mMaxCollapsedHeight);
        this.mShowAtTop = a.getBoolean(3, false);
        a.recycle();
        this.mScrollIndicatorDrawable = this.mContext.getDrawable(R.drawable.scroll_indicator_material);
        this.mScroller = new OverScroller(context, AnimationUtils.loadInterpolator(context, 17563653));
        this.mVelocityTracker = VelocityTracker.obtain();
        ViewConfiguration vc = ViewConfiguration.get(context);
        this.mTouchSlop = vc.getScaledTouchSlop();
        this.mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        setImportantForAccessibility(1);
    }

    public void setSmallCollapsed(boolean smallCollapsed) {
        this.mSmallCollapsed = smallCollapsed;
        requestLayout();
    }

    public boolean isSmallCollapsed() {
        return this.mSmallCollapsed;
    }

    public boolean isCollapsed() {
        return this.mCollapseOffset > 0.0f;
    }

    public void setShowAtTop(boolean showOnTop) {
        this.mShowAtTop = showOnTop;
        invalidate();
        requestLayout();
    }

    public boolean getShowAtTop() {
        return this.mShowAtTop;
    }

    public void setCollapsed(boolean collapsed) {
        if (!isLaidOut()) {
            this.mOpenOnLayout = collapsed;
        } else {
            smoothScrollTo(collapsed ? this.mCollapsibleHeight : 0, 0.0f);
        }
    }

    public void setCollapsibleHeightReserved(int heightPixels) {
        int oldReserved = this.mCollapsibleHeightReserved;
        this.mCollapsibleHeightReserved = heightPixels;
        int dReserved = this.mCollapsibleHeightReserved - oldReserved;
        if (dReserved != 0 && this.mIsDragging) {
            this.mLastTouchY -= dReserved;
        }
        int oldCollapsibleHeight = this.mCollapsibleHeight;
        this.mCollapsibleHeight = Math.max(this.mCollapsibleHeight, getMaxCollapsedHeight());
        if (updateCollapseOffset(oldCollapsibleHeight, !isDragging())) {
            return;
        }
        invalidate();
    }

    public void setDismissLocked(boolean locked) {
        this.mDismissLocked = locked;
    }

    private boolean isMoving() {
        return this.mIsDragging || !this.mScroller.isFinished();
    }

    private boolean isDragging() {
        return this.mIsDragging || getNestedScrollAxes() == 2;
    }

    private boolean updateCollapseOffset(int oldCollapsibleHeight, boolean remainClosed) {
        if (oldCollapsibleHeight == this.mCollapsibleHeight) {
            return false;
        }
        if (getShowAtTop()) {
            this.mCollapseOffset = 0.0f;
            return false;
        }
        if (isLaidOut()) {
            boolean isCollapsedOld = this.mCollapseOffset != 0.0f;
            if (remainClosed && oldCollapsibleHeight < this.mCollapsibleHeight && this.mCollapseOffset == oldCollapsibleHeight) {
                this.mCollapseOffset = this.mCollapsibleHeight;
            } else {
                this.mCollapseOffset = Math.min(this.mCollapseOffset, this.mCollapsibleHeight);
            }
            boolean isCollapsedNew = this.mCollapseOffset != 0.0f;
            if (isCollapsedOld != isCollapsedNew) {
                onCollapsedChanged(isCollapsedNew);
            }
        } else {
            this.mCollapseOffset = this.mOpenOnLayout ? 0.0f : this.mCollapsibleHeight;
        }
        return true;
    }

    private int getMaxCollapsedHeight() {
        return (isSmallCollapsed() ? this.mMaxCollapsedHeightSmall : this.mMaxCollapsedHeight) + this.mCollapsibleHeightReserved;
    }

    public void setOnDismissedListener(OnDismissedListener listener) {
        this.mOnDismissedListener = listener;
    }

    private boolean isDismissable() {
        return (this.mOnDismissedListener == null || this.mDismissLocked) ? false : true;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == 0) {
            this.mVelocityTracker.clear();
        }
        this.mVelocityTracker.addMovement(ev);
        if (action != 6) {
            switch (action) {
                case 0:
                    float x = ev.getX();
                    float y = ev.getY();
                    this.mInitialTouchX = x;
                    this.mLastTouchY = y;
                    this.mInitialTouchY = y;
                    this.mOpenOnClick = isListChildUnderClipped(x, y) && this.mCollapseOffset > 0.0f;
                    break;
                case 1:
                case 3:
                    resetTouch();
                    break;
                case 2:
                    float x2 = ev.getX();
                    float y2 = ev.getY();
                    float dy = y2 - this.mInitialTouchY;
                    if (Math.abs(dy) > this.mTouchSlop && findChildUnder(x2, y2) != null && (getNestedScrollAxes() & 2) == 0) {
                        this.mActivePointerId = ev.getPointerId(0);
                        this.mIsDragging = true;
                        this.mLastTouchY = Math.max(this.mLastTouchY - this.mTouchSlop, Math.min(this.mLastTouchY + dy, this.mLastTouchY + this.mTouchSlop));
                        break;
                    }
                    break;
            }
        } else {
            onSecondaryPointerUp(ev);
        }
        if (this.mIsDragging) {
            abortAnimation();
        }
        return this.mIsDragging || this.mOpenOnClick;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        this.mVelocityTracker.addMovement(ev);
        boolean handled = false;
        boolean z = true;
        switch (action) {
            case 0:
                float x = ev.getX();
                float y = ev.getY();
                this.mInitialTouchX = x;
                this.mLastTouchY = y;
                this.mInitialTouchY = y;
                this.mActivePointerId = ev.getPointerId(0);
                boolean hitView = findChildUnder(this.mInitialTouchX, this.mInitialTouchY) != null;
                handled = isDismissable() || this.mCollapsibleHeight > 0;
                if (!hitView || !handled) {
                    z = false;
                }
                this.mIsDragging = z;
                abortAnimation();
                break;
            case 1:
                boolean wasDragging = this.mIsDragging;
                this.mIsDragging = false;
                if (!wasDragging && findChildUnder(this.mInitialTouchX, this.mInitialTouchY) == null && findChildUnder(ev.getX(), ev.getY()) == null && isDismissable()) {
                    dispatchOnDismissed();
                    resetTouch();
                    return true;
                } else if (this.mOpenOnClick && Math.abs(ev.getX() - this.mInitialTouchX) < this.mTouchSlop && Math.abs(ev.getY() - this.mInitialTouchY) < this.mTouchSlop) {
                    smoothScrollTo(0, 0.0f);
                    return true;
                } else {
                    this.mVelocityTracker.computeCurrentVelocity(1000);
                    float yvel = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                    if (Math.abs(yvel) > this.mMinFlingVelocity) {
                        if (getShowAtTop()) {
                            if (isDismissable() && yvel < 0.0f) {
                                abortAnimation();
                                dismiss();
                            } else {
                                smoothScrollTo(yvel >= 0.0f ? this.mCollapsibleHeight : 0, yvel);
                            }
                        } else if (isDismissable() && yvel > 0.0f && this.mCollapseOffset > this.mCollapsibleHeight) {
                            smoothScrollTo(this.mCollapsibleHeight + this.mUncollapsibleHeight, yvel);
                            this.mDismissOnScrollerFinished = true;
                        } else {
                            smoothScrollTo(yvel >= 0.0f ? this.mCollapsibleHeight : 0, yvel);
                        }
                    } else {
                        smoothScrollTo(this.mCollapseOffset >= ((float) (this.mCollapsibleHeight / 2)) ? this.mCollapsibleHeight : 0, 0.0f);
                    }
                    resetTouch();
                    break;
                }
                break;
            case 2:
                int index = ev.findPointerIndex(this.mActivePointerId);
                if (index < 0) {
                    Log.e(TAG, "Bad pointer id " + this.mActivePointerId + ", resetting");
                    index = 0;
                    this.mActivePointerId = ev.getPointerId(0);
                    this.mInitialTouchX = ev.getX();
                    float y2 = ev.getY();
                    this.mLastTouchY = y2;
                    this.mInitialTouchY = y2;
                }
                float x2 = ev.getX(index);
                float y3 = ev.getY(index);
                if (!this.mIsDragging) {
                    float dy = y3 - this.mInitialTouchY;
                    if (Math.abs(dy) > this.mTouchSlop && findChildUnder(x2, y3) != null) {
                        this.mIsDragging = true;
                        handled = true;
                        this.mLastTouchY = Math.max(this.mLastTouchY - this.mTouchSlop, Math.min(this.mLastTouchY + dy, this.mLastTouchY + this.mTouchSlop));
                    }
                }
                if (this.mIsDragging) {
                    performDrag(y3 - this.mLastTouchY);
                }
                this.mLastTouchY = y3;
                break;
            case 3:
                if (this.mIsDragging) {
                    smoothScrollTo(this.mCollapseOffset >= ((float) (this.mCollapsibleHeight / 2)) ? this.mCollapsibleHeight : 0, 0.0f);
                }
                resetTouch();
                return true;
            case 5:
                int pointerIndex = ev.getActionIndex();
                int pointerId = ev.getPointerId(pointerIndex);
                this.mActivePointerId = pointerId;
                this.mInitialTouchX = ev.getX(pointerIndex);
                float y4 = ev.getY(pointerIndex);
                this.mLastTouchY = y4;
                this.mInitialTouchY = y4;
                break;
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        return handled;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = ev.getActionIndex();
        int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mInitialTouchX = ev.getX(newPointerIndex);
            float y = ev.getY(newPointerIndex);
            this.mLastTouchY = y;
            this.mInitialTouchY = y;
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private void resetTouch() {
        this.mActivePointerId = -1;
        this.mIsDragging = false;
        this.mOpenOnClick = false;
        this.mLastTouchY = 0.0f;
        this.mInitialTouchY = 0.0f;
        this.mInitialTouchX = 0.0f;
        this.mVelocityTracker.clear();
    }

    private void dismiss() {
        this.mRunOnDismissedListener = new RunOnDismissedListener();
        post(this.mRunOnDismissedListener);
    }

    @Override // android.view.View
    public void computeScroll() {
        super.computeScroll();
        if (this.mScroller.computeScrollOffset()) {
            boolean keepGoing = !this.mScroller.isFinished();
            performDrag(this.mScroller.getCurrY() - this.mCollapseOffset);
            if (keepGoing) {
                postInvalidateOnAnimation();
            } else if (this.mDismissOnScrollerFinished && this.mOnDismissedListener != null) {
                dismiss();
            }
        }
    }

    private void abortAnimation() {
        this.mScroller.abortAnimation();
        this.mRunOnDismissedListener = null;
        this.mDismissOnScrollerFinished = false;
    }

    private float performDrag(float dy) {
        if (getShowAtTop()) {
            return 0.0f;
        }
        float newPos = Math.max(0.0f, Math.min(this.mCollapseOffset + dy, this.mCollapsibleHeight + this.mUncollapsibleHeight));
        if (newPos != this.mCollapseOffset) {
            float dy2 = newPos - this.mCollapseOffset;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (!lp.ignoreOffset) {
                    child.offsetTopAndBottom((int) dy2);
                }
            }
            boolean isCollapsedOld = this.mCollapseOffset != 0.0f;
            this.mCollapseOffset = newPos;
            this.mTopOffset = (int) (this.mTopOffset + dy2);
            boolean isCollapsedNew = newPos != 0.0f;
            if (isCollapsedOld != isCollapsedNew) {
                onCollapsedChanged(isCollapsedNew);
            }
            postInvalidateOnAnimation();
            return dy2;
        }
        return 0.0f;
    }

    private void onCollapsedChanged(boolean isCollapsed) {
        notifyViewAccessibilityStateChangedIfNeeded(0);
        if (this.mScrollIndicatorDrawable != null) {
            setWillNotDraw(!isCollapsed);
        }
    }

    void dispatchOnDismissed() {
        if (this.mOnDismissedListener != null) {
            this.mOnDismissedListener.onDismissed();
        }
        if (this.mRunOnDismissedListener != null) {
            removeCallbacks(this.mRunOnDismissedListener);
            this.mRunOnDismissedListener = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void smoothScrollTo(int yOffset, float velocity) {
        int duration;
        abortAnimation();
        int sy = (int) this.mCollapseOffset;
        int dy = yOffset - sy;
        if (dy == 0) {
            return;
        }
        int height = getHeight();
        int halfHeight = height / 2;
        float distanceRatio = Math.min(1.0f, (Math.abs(dy) * 1.0f) / height);
        float distance = halfHeight + (halfHeight * distanceInfluenceForSnapDuration(distanceRatio));
        float velocity2 = Math.abs(velocity);
        if (velocity2 > 0.0f) {
            duration = 4 * Math.round(1000.0f * Math.abs(distance / velocity2));
        } else {
            float pageDelta = Math.abs(dy) / height;
            duration = (int) ((1.0f + pageDelta) * 100.0f);
        }
        this.mScroller.startScroll(0, sy, 0, dy, Math.min(duration, 300));
        postInvalidateOnAnimation();
    }

    private float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((float) ((f - 0.5f) * 0.4712389167638204d));
    }

    private View findChildUnder(float x, float y) {
        return findChildUnder(this, x, y);
    }

    private static View findChildUnder(ViewGroup parent, float x, float y) {
        int childCount = parent.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View child = parent.getChildAt(i);
            if (isChildUnder(child, x, y)) {
                return child;
            }
        }
        return null;
    }

    private View findListChildUnder(float x, float y) {
        View v = findChildUnder(x, y);
        while (v != null) {
            x -= v.getX();
            y -= v.getY();
            if (v instanceof AbsListView) {
                return findChildUnder((ViewGroup) v, x, y);
            }
            v = v instanceof ViewGroup ? findChildUnder((ViewGroup) v, x, y) : null;
        }
        return v;
    }

    private boolean isListChildUnderClipped(float x, float y) {
        View listChild = findListChildUnder(x, y);
        return listChild != null && isDescendantClipped(listChild);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDescendantClipped(View child) {
        View v;
        this.mTempRect.set(0, 0, child.getWidth(), child.getHeight());
        offsetDescendantRectToMyCoords(child, this.mTempRect);
        if (child.getParent() == this) {
            v = child;
        } else {
            v = child;
            ViewParent p = child.getParent();
            while (p != this) {
                v = (View) p;
                p = v.getParent();
            }
        }
        int clipEdge = getHeight() - getPaddingBottom();
        int childCount = getChildCount();
        for (int i = indexOfChild(v) + 1; i < childCount; i++) {
            View nextChild = getChildAt(i);
            if (nextChild.getVisibility() != 8) {
                clipEdge = Math.min(clipEdge, nextChild.getTop());
            }
        }
        return this.mTempRect.bottom > clipEdge;
    }

    private static boolean isChildUnder(View child, float x, float y) {
        float left = child.getX();
        float top = child.getY();
        float right = child.getWidth() + left;
        float bottom = child.getHeight() + top;
        return x >= left && y >= top && x < right && y < bottom;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (!isInTouchMode() && isDescendantClipped(focused)) {
            smoothScrollTo(0, 0.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnTouchModeChangeListener(this.mTouchModeChangeListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnTouchModeChangeListener(this.mTouchModeChangeListener);
        abortAnimation();
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & 2) != 0;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        if (this.mScroller.isFinished()) {
            smoothScrollTo(this.mCollapseOffset < ((float) (this.mCollapsibleHeight / 2)) ? 0 : this.mCollapsibleHeight, 0.0f);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed < 0) {
            performDrag(-dyUnconsumed);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {
            consumed[1] = (int) (-performDrag(-dy));
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getShowAtTop() || velocityY <= this.mMinFlingVelocity || this.mCollapseOffset == 0.0f) {
            return false;
        }
        smoothScrollTo(0, velocityY);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (consumed || Math.abs(velocityY) <= this.mMinFlingVelocity) {
            return false;
        }
        if (getShowAtTop()) {
            if (isDismissable() && velocityY > 0.0f) {
                abortAnimation();
                dismiss();
            } else {
                smoothScrollTo(velocityY < 0.0f ? this.mCollapsibleHeight : 0, velocityY);
            }
        } else if (isDismissable() && velocityY < 0.0f && this.mCollapseOffset > this.mCollapsibleHeight) {
            smoothScrollTo(this.mCollapsibleHeight + this.mUncollapsibleHeight, velocityY);
            this.mDismissOnScrollerFinished = true;
        } else {
            smoothScrollTo(velocityY <= 0.0f ? this.mCollapsibleHeight : 0, velocityY);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        if (super.onNestedPrePerformAccessibilityAction(target, action, args)) {
            return true;
        }
        if (action != 4096 || this.mCollapseOffset == 0.0f) {
            return false;
        }
        smoothScrollTo(0, 0.0f);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return ScrollView.class.getName();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (isEnabled() && this.mCollapseOffset != 0.0f) {
            info.addAction(4096);
            info.setScrollable(true);
        }
        info.removeAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS);
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (action == AccessibilityNodeInfo.AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS.getId()) {
            return false;
        }
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        if (action != 4096 || this.mCollapseOffset == 0.0f) {
            return false;
        }
        smoothScrollTo(0, 0.0f);
        return true;
    }

    @Override // android.view.View
    public void onDrawForeground(Canvas canvas) {
        if (this.mScrollIndicatorDrawable != null) {
            this.mScrollIndicatorDrawable.draw(canvas);
        }
        super.onDrawForeground(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int i2;
        int i3;
        int widthSize;
        int i4;
        int sourceWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthSize2 = sourceWidth;
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (this.mMaxWidth >= 0) {
            widthSize2 = Math.min(widthSize2, this.mMaxWidth);
        }
        int widthSize3 = widthSize2;
        int widthSpec = View.MeasureSpec.makeMeasureSpec(widthSize3, 1073741824);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(heightSize, 1073741824);
        int widthPadding = getPaddingLeft() + getPaddingRight();
        int heightUsed = getPaddingTop() + getPaddingBottom();
        int childCount = getChildCount();
        int heightUsed2 = heightUsed;
        int heightUsed3 = 0;
        while (true) {
            int i5 = heightUsed3;
            i = 8;
            if (i5 >= childCount) {
                break;
            }
            View child = getChildAt(i5);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (!lp.alwaysShow || child.getVisibility() == 8) {
                i4 = i5;
            } else {
                i4 = i5;
                measureChildWithMargins(child, widthSpec, widthPadding, heightSpec, heightUsed2);
                heightUsed2 += child.getMeasuredHeight();
            }
            heightUsed3 = i4 + 1;
        }
        int alwaysShowHeight = heightUsed2;
        int i6 = 0;
        while (true) {
            int i7 = i6;
            if (i7 >= childCount) {
                break;
            }
            View child2 = getChildAt(i7);
            LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
            if (lp2.alwaysShow || child2.getVisibility() == i) {
                i2 = i;
                i3 = i7;
                widthSize = widthSize3;
            } else {
                widthSize = widthSize3;
                i2 = i;
                i3 = i7;
                measureChildWithMargins(child2, widthSpec, widthPadding, heightSpec, heightUsed2);
                heightUsed2 += child2.getMeasuredHeight();
            }
            i6 = i3 + 1;
            i = i2;
            widthSize3 = widthSize;
        }
        int oldCollapsibleHeight = this.mCollapsibleHeight;
        this.mCollapsibleHeight = Math.max(0, (heightUsed2 - alwaysShowHeight) - getMaxCollapsedHeight());
        this.mUncollapsibleHeight = heightUsed2 - this.mCollapsibleHeight;
        updateCollapseOffset(oldCollapsibleHeight, !isDragging());
        if (getShowAtTop()) {
            this.mTopOffset = 0;
        } else {
            this.mTopOffset = Math.max(0, heightSize - heightUsed2) + ((int) this.mCollapseOffset);
        }
        setMeasuredDimension(sourceWidth, heightSize);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int width;
        int width2 = getWidth();
        View indicatorHost = null;
        int ypos = this.mTopOffset;
        int leftEdge = getPaddingLeft();
        int rightEdge = width2 - getPaddingRight();
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.hasNestedScrollIndicator) {
                indicatorHost = child;
            }
            if (child.getVisibility() == 8) {
                width = width2;
            } else {
                int top = lp.topMargin + ypos;
                if (lp.ignoreOffset) {
                    top = (int) (top - this.mCollapseOffset);
                }
                int bottom = child.getMeasuredHeight() + top;
                int childWidth = child.getMeasuredWidth();
                int widthAvailable = rightEdge - leftEdge;
                int left = ((widthAvailable - childWidth) / 2) + leftEdge;
                int right = left + childWidth;
                child.layout(left, top, right, bottom);
                width = width2;
                int ypos2 = bottom + lp.bottomMargin;
                ypos = ypos2;
            }
            i++;
            width2 = width;
        }
        if (this.mScrollIndicatorDrawable != null) {
            if (indicatorHost != null) {
                int left2 = indicatorHost.getLeft();
                int right2 = indicatorHost.getRight();
                int bottom2 = indicatorHost.getTop();
                this.mScrollIndicatorDrawable.setBounds(left2, bottom2 - this.mScrollIndicatorDrawable.getIntrinsicHeight(), right2, bottom2);
                setWillNotDraw(true ^ isCollapsed());
                return;
            }
            this.mScrollIndicatorDrawable = null;
            setWillNotDraw(true);
        }
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        if (p instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) p);
        }
        if (p instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) p);
        }
        return new LayoutParams(p);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.open = this.mCollapsibleHeight > 0 && this.mCollapseOffset == 0.0f;
        return ss;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mOpenOnLayout = ss.open;
    }

    /* loaded from: classes3.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public boolean alwaysShow;
        public boolean hasNestedScrollIndicator;
        public boolean ignoreOffset;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ResolverDrawerLayout_LayoutParams);
            this.alwaysShow = a.getBoolean(1, false);
            this.ignoreOffset = a.getBoolean(3, false);
            this.hasNestedScrollIndicator = a.getBoolean(2, false);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(LayoutParams source) {
            super((ViewGroup.MarginLayoutParams) source);
            this.alwaysShow = source.alwaysShow;
            this.ignoreOffset = source.ignoreOffset;
            this.hasNestedScrollIndicator = source.hasNestedScrollIndicator;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.android.internal.widget.ResolverDrawerLayout.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean open;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.open = in.readInt() != 0;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.open ? 1 : 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class RunOnDismissedListener implements Runnable {
        private RunOnDismissedListener() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ResolverDrawerLayout.this.dispatchOnDismissed();
        }
    }
}
