package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewHierarchyEncoder;
import android.widget.RemoteViews;
import com.android.internal.R;
import java.util.ArrayList;
@RemoteViews.RemoteView
/* loaded from: classes3.dex */
public class FrameLayout extends ViewGroup {
    private static final int DEFAULT_CHILD_GRAVITY = 8388659;
    @ViewDebug.ExportedProperty(category = "padding")
    public protected int mForegroundPaddingBottom;
    @ViewDebug.ExportedProperty(category = "padding")
    public protected int mForegroundPaddingLeft;
    @ViewDebug.ExportedProperty(category = "padding")
    public protected int mForegroundPaddingRight;
    @ViewDebug.ExportedProperty(category = "padding")
    public protected int mForegroundPaddingTop;
    private final ArrayList<View> mMatchParentChildren;
    @ViewDebug.ExportedProperty(category = "measurement")
    public private protected boolean mMeasureAllChildren;

    public FrameLayout(Context context) {
        super(context);
        this.mMeasureAllChildren = false;
        this.mForegroundPaddingLeft = 0;
        this.mForegroundPaddingTop = 0;
        this.mForegroundPaddingRight = 0;
        this.mForegroundPaddingBottom = 0;
        this.mMatchParentChildren = new ArrayList<>(1);
    }

    public FrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mMeasureAllChildren = false;
        this.mForegroundPaddingLeft = 0;
        this.mForegroundPaddingTop = 0;
        this.mForegroundPaddingRight = 0;
        this.mForegroundPaddingBottom = 0;
        this.mMatchParentChildren = new ArrayList<>(1);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FrameLayout, defStyleAttr, defStyleRes);
        if (a.getBoolean(0, false)) {
            setMeasureAllChildren(true);
        }
        a.recycle();
    }

    @Override // android.view.View
    @RemotableViewMethod
    public void setForegroundGravity(int foregroundGravity) {
        if (getForegroundGravity() != foregroundGravity) {
            super.setForegroundGravity(foregroundGravity);
            Drawable foreground = getForeground();
            if (getForegroundGravity() == 119 && foreground != null) {
                Rect padding = new Rect();
                if (foreground.getPadding(padding)) {
                    this.mForegroundPaddingLeft = padding.left;
                    this.mForegroundPaddingTop = padding.top;
                    this.mForegroundPaddingRight = padding.right;
                    this.mForegroundPaddingBottom = padding.bottom;
                }
            } else {
                this.mForegroundPaddingLeft = 0;
                this.mForegroundPaddingTop = 0;
                this.mForegroundPaddingRight = 0;
                this.mForegroundPaddingBottom = 0;
            }
            requestLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getPaddingLeftWithForeground() {
        return isForegroundInsidePadding() ? Math.max(this.mPaddingLeft, this.mForegroundPaddingLeft) : this.mPaddingLeft + this.mForegroundPaddingLeft;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getPaddingRightWithForeground() {
        return isForegroundInsidePadding() ? Math.max(this.mPaddingRight, this.mForegroundPaddingRight) : this.mPaddingRight + this.mForegroundPaddingRight;
    }

    private synchronized int getPaddingTopWithForeground() {
        return isForegroundInsidePadding() ? Math.max(this.mPaddingTop, this.mForegroundPaddingTop) : this.mPaddingTop + this.mForegroundPaddingTop;
    }

    private synchronized int getPaddingBottomWithForeground() {
        return isForegroundInsidePadding() ? Math.max(this.mPaddingBottom, this.mForegroundPaddingBottom) : this.mPaddingBottom + this.mForegroundPaddingBottom;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int i;
        int count = getChildCount();
        boolean measureMatchParentChildren = (View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && View.MeasureSpec.getMode(heightMeasureSpec) == 1073741824) ? false : true;
        this.mMatchParentChildren.clear();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int maxHeight2 = 0;
        while (true) {
            int i2 = maxHeight2;
            if (i2 >= count) {
                break;
            }
            View child = getChildAt(i2);
            if (this.mMeasureAllChildren || child.getVisibility() != 8) {
                i = i2;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int maxWidth2 = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                int maxHeight3 = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                int childState2 = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren && (lp.width == -1 || lp.height == -1)) {
                    this.mMatchParentChildren.add(child);
                }
                maxWidth = maxWidth2;
                maxHeight = maxHeight3;
                childState = childState2;
            } else {
                i = i2;
            }
            maxHeight2 = i + 1;
        }
        int i3 = -1;
        int childState3 = childState;
        int maxWidth3 = maxWidth + getPaddingLeftWithForeground() + getPaddingRightWithForeground();
        int maxHeight4 = Math.max(maxHeight + getPaddingTopWithForeground() + getPaddingBottomWithForeground(), getSuggestedMinimumHeight());
        int maxWidth4 = Math.max(maxWidth3, getSuggestedMinimumWidth());
        Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight4 = Math.max(maxHeight4, drawable.getMinimumHeight());
            maxWidth4 = Math.max(maxWidth4, drawable.getMinimumWidth());
        }
        setMeasuredDimension(resolveSizeAndState(maxWidth4, widthMeasureSpec, childState3), resolveSizeAndState(maxHeight4, heightMeasureSpec, childState3 << 16));
        int count2 = this.mMatchParentChildren.size();
        if (count2 > 1) {
            int i4 = 0;
            while (i4 < count2) {
                View child2 = this.mMatchParentChildren.get(i4);
                ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) child2.getLayoutParams();
                if (lp2.width == i3) {
                    int width2 = Math.max(0, (((getMeasuredWidth() - getPaddingLeftWithForeground()) - getPaddingRightWithForeground()) - lp2.leftMargin) - lp2.rightMargin);
                    width = View.MeasureSpec.makeMeasureSpec(width2, 1073741824);
                } else {
                    width = getChildMeasureSpec(widthMeasureSpec, getPaddingLeftWithForeground() + getPaddingRightWithForeground() + lp2.leftMargin + lp2.rightMargin, lp2.width);
                }
                if (lp2.height == i3) {
                    int height2 = Math.max(0, (((getMeasuredHeight() - getPaddingTopWithForeground()) - getPaddingBottomWithForeground()) - lp2.topMargin) - lp2.bottomMargin);
                    height = View.MeasureSpec.makeMeasureSpec(height2, 1073741824);
                } else {
                    height = getChildMeasureSpec(heightMeasureSpec, getPaddingTopWithForeground() + getPaddingBottomWithForeground() + lp2.topMargin + lp2.bottomMargin, lp2.height);
                }
                int childHeightMeasureSpec = height;
                child2.measure(width, childHeightMeasureSpec);
                i4++;
                i3 = -1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChildren(left, top, right, bottom, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {
        int count;
        int parentLeft;
        int childLeft;
        int childTop;
        int count2 = getChildCount();
        int parentLeft2 = getPaddingLeftWithForeground();
        int parentRight = (right - left) - getPaddingRightWithForeground();
        int parentTop = getPaddingTopWithForeground();
        int parentBottom = (bottom - top) - getPaddingBottomWithForeground();
        int i = 0;
        while (i < count2) {
            View child = getChildAt(i);
            if (child.getVisibility() == 8) {
                count = count2;
                parentLeft = parentLeft2;
            } else {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int gravity = lp.gravity;
                count = count2;
                if (gravity == -1) {
                    gravity = DEFAULT_CHILD_GRAVITY;
                }
                int absoluteGravity = Gravity.getAbsoluteGravity(gravity, getLayoutDirection());
                int layoutDirection = gravity & 112;
                int i2 = absoluteGravity & 7;
                if (i2 == 1) {
                    childLeft = (((((parentRight - parentLeft2) - width) / 2) + parentLeft2) + lp.leftMargin) - lp.rightMargin;
                } else if (i2 == 5 && !forceLeftGravity) {
                    childLeft = (parentRight - width) - lp.rightMargin;
                } else {
                    childLeft = parentLeft2 + lp.leftMargin;
                }
                int childLeft2 = childLeft;
                if (layoutDirection == 16) {
                    int verticalGravity = parentBottom - parentTop;
                    childTop = ((((verticalGravity - height) / 2) + parentTop) + lp.topMargin) - lp.bottomMargin;
                } else if (layoutDirection == 48) {
                    int verticalGravity2 = lp.topMargin;
                    childTop = parentTop + verticalGravity2;
                } else if (layoutDirection == 80) {
                    int verticalGravity3 = lp.bottomMargin;
                    childTop = (parentBottom - height) - verticalGravity3;
                } else {
                    childTop = lp.topMargin + parentTop;
                }
                int childTop2 = childTop;
                int childTop3 = childLeft2 + width;
                parentLeft = parentLeft2;
                int parentLeft3 = childTop2 + height;
                child.layout(childLeft2, childTop2, childTop3, parentLeft3);
            }
            i++;
            count2 = count;
            parentLeft2 = parentLeft;
        }
    }

    @RemotableViewMethod
    public void setMeasureAllChildren(boolean measureAll) {
        this.mMeasureAllChildren = measureAll;
    }

    @Deprecated
    public boolean getConsiderGoneChildrenWhenMeasuring() {
        return getMeasureAllChildren();
    }

    public boolean getMeasureAllChildren() {
        return this.mMeasureAllChildren;
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (sPreserveMarginParamsInLayoutParamConversion) {
            if (lp instanceof LayoutParams) {
                return new LayoutParams((LayoutParams) lp);
            }
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
            }
        }
        return new LayoutParams(lp);
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return FrameLayout.class.getName();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public synchronized void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("measurement:measureAllChildren", this.mMeasureAllChildren);
        encoder.addProperty("padding:foregroundPaddingLeft", this.mForegroundPaddingLeft);
        encoder.addProperty("padding:foregroundPaddingTop", this.mForegroundPaddingTop);
        encoder.addProperty("padding:foregroundPaddingRight", this.mForegroundPaddingRight);
        encoder.addProperty("padding:foregroundPaddingBottom", this.mForegroundPaddingBottom);
    }

    /* loaded from: classes3.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public static final int UNSPECIFIED_GRAVITY = -1;
        public int gravity;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.gravity = -1;
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FrameLayout_Layout);
            this.gravity = a.getInt(0, -1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = -1;
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = -1;
            this.gravity = gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.gravity = -1;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            this.gravity = -1;
        }

        public LayoutParams(LayoutParams source) {
            super((ViewGroup.MarginLayoutParams) source);
            this.gravity = -1;
            this.gravity = source.gravity;
        }
    }
}
