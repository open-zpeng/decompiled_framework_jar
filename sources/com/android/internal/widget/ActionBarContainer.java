package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.internal.R;
/* loaded from: classes3.dex */
public class ActionBarContainer extends FrameLayout {
    private View mActionBarView;
    private View mActionContextView;
    private Drawable mBackground;
    private int mHeight;
    private boolean mIsSplit;
    private boolean mIsStacked;
    private boolean mIsTransitioning;
    private Drawable mSplitBackground;
    private Drawable mStackedBackground;
    private View mTabContainer;

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    public ActionBarContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackground(new ActionBarBackgroundDrawable());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        this.mBackground = a.getDrawable(2);
        this.mStackedBackground = a.getDrawable(18);
        this.mHeight = a.getDimensionPixelSize(4, -1);
        boolean z = true;
        if (getId() == 16909430) {
            this.mIsSplit = true;
            this.mSplitBackground = a.getDrawable(19);
        }
        a.recycle();
        if (!this.mIsSplit ? this.mBackground != null || this.mStackedBackground != null : this.mSplitBackground != null) {
            z = false;
        }
        setWillNotDraw(z);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = findViewById(R.id.action_bar);
        this.mActionContextView = findViewById(R.id.action_context_bar);
    }

    public void setPrimaryBackground(Drawable bg) {
        if (this.mBackground != null) {
            this.mBackground.setCallback(null);
            unscheduleDrawable(this.mBackground);
        }
        this.mBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            if (this.mActionBarView != null) {
                this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            }
        }
        boolean z = false;
        if (!this.mIsSplit ? !(this.mBackground != null || this.mStackedBackground != null) : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setStackedBackground(Drawable bg) {
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setCallback(null);
            unscheduleDrawable(this.mStackedBackground);
        }
        this.mStackedBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            if (this.mIsStacked && this.mStackedBackground != null) {
                this.mStackedBackground.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
            }
        }
        boolean z = false;
        if (!this.mIsSplit ? !(this.mBackground != null || this.mStackedBackground != null) : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setSplitBackground(Drawable bg) {
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setCallback(null);
            unscheduleDrawable(this.mSplitBackground);
        }
        this.mSplitBackground = bg;
        boolean z = false;
        if (bg != null) {
            bg.setCallback(this);
            if (this.mIsSplit && this.mSplitBackground != null) {
                this.mSplitBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
        }
        if (!this.mIsSplit ? !(this.mBackground != null || this.mStackedBackground != null) : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
        invalidate();
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        boolean isVisible = visibility == 0;
        if (this.mBackground != null) {
            this.mBackground.setVisible(isVisible, false);
        }
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setVisible(isVisible, false);
        }
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setVisible(isVisible, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public boolean verifyDrawable(Drawable who) {
        return (who == this.mBackground && !this.mIsSplit) || (who == this.mStackedBackground && this.mIsStacked) || ((who == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(who));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] state = getDrawableState();
        boolean changed = false;
        Drawable background = this.mBackground;
        if (background != null && background.isStateful()) {
            changed = false | background.setState(state);
        }
        Drawable stackedBackground = this.mStackedBackground;
        if (stackedBackground != null && stackedBackground.isStateful()) {
            changed |= stackedBackground.setState(state);
        }
        Drawable splitBackground = this.mSplitBackground;
        if (splitBackground != null && splitBackground.isStateful()) {
            changed |= splitBackground.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mBackground != null) {
            this.mBackground.jumpToCurrentState();
        }
        if (this.mStackedBackground != null) {
            this.mStackedBackground.jumpToCurrentState();
        }
        if (this.mSplitBackground != null) {
            this.mSplitBackground.jumpToCurrentState();
        }
    }

    @Override // android.view.View
    public void onResolveDrawables(int layoutDirection) {
        super.onResolveDrawables(layoutDirection);
        if (this.mBackground != null) {
            this.mBackground.setLayoutDirection(layoutDirection);
        }
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setLayoutDirection(layoutDirection);
        }
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setLayoutDirection(layoutDirection);
        }
    }

    public void setTransitioning(boolean isTransitioning) {
        this.mIsTransitioning = isTransitioning;
        setDescendantFocusability(isTransitioning ? 393216 : 262144);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.mIsTransitioning || super.onInterceptTouchEvent(ev);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent ev) {
        super.onHoverEvent(ev);
        return true;
    }

    public void setTabContainer(ScrollingTabContainerView tabView) {
        if (this.mTabContainer != null) {
            removeView(this.mTabContainer);
        }
        this.mTabContainer = tabView;
        if (tabView != null) {
            addView(tabView);
            ViewGroup.LayoutParams lp = tabView.getLayoutParams();
            lp.width = -1;
            lp.height = -2;
            tabView.setAllowCollapse(false);
        }
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View child, ActionMode.Callback callback, int type) {
        if (type != 0) {
            return super.startActionModeForChild(child, callback, type);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isCollapsed(View view) {
        return view == null || view.getVisibility() == 8 || view.getMeasuredHeight() == 0;
    }

    private int getMeasuredHeightWithMargins(View view) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        return view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeightWithMargins;
        if (this.mActionBarView == null && View.MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE && this.mHeight >= 0) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.min(this.mHeight, View.MeasureSpec.getSize(heightMeasureSpec)), Integer.MIN_VALUE);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mActionBarView != null && this.mTabContainer != null && this.mTabContainer.getVisibility() != 8) {
            int childCount = getChildCount();
            int nonTabMaxHeight = 0;
            for (int nonTabMaxHeight2 = 0; nonTabMaxHeight2 < childCount; nonTabMaxHeight2++) {
                View child = getChildAt(nonTabMaxHeight2);
                if (child != this.mTabContainer) {
                    if (isCollapsed(child)) {
                        measuredHeightWithMargins = 0;
                    } else {
                        measuredHeightWithMargins = getMeasuredHeightWithMargins(child);
                    }
                    nonTabMaxHeight = Math.max(nonTabMaxHeight, measuredHeightWithMargins);
                }
            }
            int mode = View.MeasureSpec.getMode(heightMeasureSpec);
            int maxHeight = mode == Integer.MIN_VALUE ? View.MeasureSpec.getSize(heightMeasureSpec) : Integer.MAX_VALUE;
            setMeasuredDimension(getMeasuredWidth(), Math.min(getMeasuredHeightWithMargins(this.mTabContainer) + nonTabMaxHeight, maxHeight));
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View tabContainer = this.mTabContainer;
        boolean hasTabs = (tabContainer == null || tabContainer.getVisibility() == 8) ? false : true;
        if (tabContainer != null && tabContainer.getVisibility() != 8) {
            int containerHeight = getMeasuredHeight();
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tabContainer.getLayoutParams();
            int tabHeight = tabContainer.getMeasuredHeight();
            tabContainer.layout(l, (containerHeight - tabHeight) - lp.bottomMargin, r, containerHeight - lp.bottomMargin);
        }
        boolean needsInvalidate = false;
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                this.mSplitBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                needsInvalidate = true;
            }
        } else {
            if (this.mBackground != null) {
                if (this.mActionBarView.getVisibility() == 0) {
                    this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
                } else if (this.mActionContextView != null && this.mActionContextView.getVisibility() == 0) {
                    this.mBackground.setBounds(this.mActionContextView.getLeft(), this.mActionContextView.getTop(), this.mActionContextView.getRight(), this.mActionContextView.getBottom());
                } else {
                    this.mBackground.setBounds(0, 0, 0, 0);
                }
                needsInvalidate = true;
            }
            this.mIsStacked = hasTabs;
            if (hasTabs && this.mStackedBackground != null) {
                this.mStackedBackground.setBounds(tabContainer.getLeft(), tabContainer.getTop(), tabContainer.getRight(), tabContainer.getBottom());
                needsInvalidate = true;
            }
        }
        if (needsInvalidate) {
            invalidate();
        }
    }

    /* loaded from: classes3.dex */
    private class ActionBarBackgroundDrawable extends Drawable {
        private ActionBarBackgroundDrawable() {
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (ActionBarContainer.this.mIsSplit) {
                if (ActionBarContainer.this.mSplitBackground != null) {
                    ActionBarContainer.this.mSplitBackground.draw(canvas);
                    return;
                }
                return;
            }
            if (ActionBarContainer.this.mBackground != null) {
                ActionBarContainer.this.mBackground.draw(canvas);
            }
            if (ActionBarContainer.this.mStackedBackground != null && ActionBarContainer.this.mIsStacked) {
                ActionBarContainer.this.mStackedBackground.draw(canvas);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void getOutline(Outline outline) {
            if (ActionBarContainer.this.mIsSplit) {
                if (ActionBarContainer.this.mSplitBackground != null) {
                    ActionBarContainer.this.mSplitBackground.getOutline(outline);
                }
            } else if (ActionBarContainer.this.mBackground != null) {
                ActionBarContainer.this.mBackground.getOutline(outline);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            if (ActionBarContainer.this.mIsSplit) {
                if (ActionBarContainer.this.mSplitBackground != null && ActionBarContainer.this.mSplitBackground.getOpacity() == -1) {
                    return -1;
                }
            } else if ((!ActionBarContainer.this.mIsStacked || (ActionBarContainer.this.mStackedBackground != null && ActionBarContainer.this.mStackedBackground.getOpacity() == -1)) && !ActionBarContainer.isCollapsed(ActionBarContainer.this.mActionBarView) && ActionBarContainer.this.mBackground != null && ActionBarContainer.this.mBackground.getOpacity() == -1) {
                return -1;
            }
            return 0;
        }
    }
}
