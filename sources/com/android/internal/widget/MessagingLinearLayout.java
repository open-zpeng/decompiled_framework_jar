package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import com.android.internal.R;
@RemoteViews.RemoteView
/* loaded from: classes3.dex */
public class MessagingLinearLayout extends ViewGroup {
    private int mMaxDisplayedLines;
    private MessagingLayout mMessagingLayout;
    private int mSpacing;

    public MessagingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mMaxDisplayedLines = Integer.MAX_VALUE;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MessagingLinearLayout, 0, 0);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            if (attr == 0) {
                this.mSpacing = a.getDimensionPixelSize(i, 0);
            }
        }
        a.recycle();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count;
        int targetHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        if (View.MeasureSpec.getMode(heightMeasureSpec) == 0) {
            targetHeight = Integer.MAX_VALUE;
        }
        int targetHeight2 = targetHeight;
        int targetHeight3 = this.mPaddingLeft;
        int measuredWidth = targetHeight3 + this.mPaddingRight;
        int count2 = getChildCount();
        for (int i = 0; i < count2; i++) {
            ((LayoutParams) getChildAt(i).getLayoutParams()).hide = true;
        }
        int i2 = this.mPaddingTop;
        int totalHeight = i2 + this.mPaddingBottom;
        int linesRemaining = this.mMaxDisplayedLines;
        int i3 = count2 - 1;
        int measuredWidth2 = measuredWidth;
        int totalHeight2 = totalHeight;
        boolean first = true;
        int linesRemaining2 = linesRemaining;
        while (true) {
            int i4 = i3;
            if (i4 < 0 || totalHeight2 >= targetHeight2) {
                break;
            }
            if (getChildAt(i4).getVisibility() == 8) {
                count = count2;
            } else {
                View child = getChildAt(i4);
                LayoutParams lp = (LayoutParams) getChildAt(i4).getLayoutParams();
                MessagingChild messagingChild = null;
                int spacing = this.mSpacing;
                if (child instanceof MessagingChild) {
                    messagingChild = (MessagingChild) child;
                    messagingChild.setMaxDisplayedLines(linesRemaining2);
                    spacing += messagingChild.getExtraSpacing();
                }
                MessagingChild messagingChild2 = messagingChild;
                int spacing2 = first ? 0 : spacing;
                count = count2;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, ((totalHeight2 - this.mPaddingTop) - this.mPaddingBottom) + spacing2);
                int childHeight = child.getMeasuredHeight();
                int newHeight = Math.max(totalHeight2, totalHeight2 + childHeight + lp.topMargin + lp.bottomMargin + spacing2);
                int measureType = 0;
                if (messagingChild2 != null) {
                    measureType = messagingChild2.getMeasuredType();
                    linesRemaining2 -= messagingChild2.getConsumedLines();
                }
                boolean isTooSmall = measureType == 2 && !first;
                boolean isShortened = measureType == 1 || (measureType == 2 && first);
                if (newHeight > targetHeight2 || isTooSmall) {
                    break;
                }
                totalHeight2 = newHeight;
                measuredWidth2 = Math.max(measuredWidth2, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin + this.mPaddingLeft + this.mPaddingRight);
                lp.hide = false;
                if (isShortened || linesRemaining2 <= 0) {
                    break;
                }
                first = false;
            }
            i3 = i4 - 1;
            count2 = count;
        }
        setMeasuredDimension(resolveSize(Math.max(getSuggestedMinimumWidth(), measuredWidth2), widthMeasureSpec), Math.max(getSuggestedMinimumHeight(), totalHeight2));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childLeft;
        int paddingLeft;
        int paddingLeft2 = this.mPaddingLeft;
        int width = right - left;
        int childRight = width - this.mPaddingRight;
        int layoutDirection = getLayoutDirection();
        int count = getChildCount();
        int childTop = this.mPaddingTop;
        boolean shown = isShown();
        boolean first = true;
        int childTop2 = childTop;
        int childTop3 = 0;
        while (childTop3 < count) {
            View child = getChildAt(childTop3);
            if (child.getVisibility() == 8) {
                paddingLeft = paddingLeft2;
            } else {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                MessagingChild messagingChild = (MessagingChild) child;
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                if (layoutDirection == 1) {
                    childLeft = (childRight - childWidth) - lp.rightMargin;
                } else {
                    childLeft = paddingLeft2 + lp.leftMargin;
                }
                int childLeft2 = childLeft;
                paddingLeft = paddingLeft2;
                if (lp.hide) {
                    if (shown && lp.visibleBefore) {
                        child.layout(childLeft2, childTop2, childLeft2 + childWidth, lp.lastVisibleHeight + childTop2);
                        messagingChild.hideAnimated();
                    }
                    lp.visibleBefore = false;
                } else {
                    lp.visibleBefore = true;
                    lp.lastVisibleHeight = childHeight;
                    if (!first) {
                        childTop2 += this.mSpacing;
                    }
                    int childTop4 = childTop2 + lp.topMargin;
                    child.layout(childLeft2, childTop4, childLeft2 + childWidth, childTop4 + childHeight);
                    childTop2 = childTop4 + lp.bottomMargin + childHeight;
                    first = false;
                }
            }
            childTop3++;
            paddingLeft2 = paddingLeft;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp.hide) {
            MessagingChild messagingChild = (MessagingChild) child;
            if (!messagingChild.isHidingAnimated()) {
                return true;
            }
        }
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(this.mContext, attrs);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        LayoutParams copy = new LayoutParams(lp.width, lp.height);
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            copy.copyMarginsFrom((ViewGroup.MarginLayoutParams) lp);
        }
        return copy;
    }

    public static boolean isGone(View view) {
        if (view.getVisibility() == 8) {
            return true;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        return (lp instanceof LayoutParams) && ((LayoutParams) lp).hide;
    }

    @RemotableViewMethod
    public void setMaxDisplayedLines(int numberLines) {
        this.mMaxDisplayedLines = numberLines;
    }

    public void setMessagingLayout(MessagingLayout layout) {
        this.mMessagingLayout = layout;
    }

    public MessagingLayout getMessagingLayout() {
        return this.mMessagingLayout;
    }

    /* loaded from: classes3.dex */
    public interface MessagingChild {
        public static final int MEASURED_NORMAL = 0;
        public static final int MEASURED_SHORTENED = 1;
        public static final int MEASURED_TOO_SMALL = 2;

        int getConsumedLines();

        int getMeasuredType();

        void hideAnimated();

        boolean isHidingAnimated();

        void setMaxDisplayedLines(int i);

        default int getExtraSpacing() {
            return 0;
        }
    }

    /* loaded from: classes3.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public boolean hide;
        public int lastVisibleHeight;
        public boolean visibleBefore;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.hide = false;
            this.visibleBefore = false;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.hide = false;
            this.visibleBefore = false;
        }
    }
}
