package com.android.internal.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ViewAnimator;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public class DialogViewAnimator extends ViewAnimator {
    private final ArrayList<View> mMatchParentChildren;

    public DialogViewAnimator(Context context) {
        super(context);
        this.mMatchParentChildren = new ArrayList<>(1);
    }

    public DialogViewAnimator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mMatchParentChildren = new ArrayList<>(1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int childWidthMeasureSpec;
        int childHeightMeasureSpec;
        int i2;
        boolean measureMatchParentChildren = (View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && View.MeasureSpec.getMode(heightMeasureSpec) == 1073741824) ? false : true;
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int childState2 = 0;
        while (true) {
            int i3 = childState2;
            i = -1;
            if (i3 >= count) {
                break;
            }
            View child = getChildAt(i3);
            if (!getMeasureAllChildren() && child.getVisibility() == 8) {
                i2 = i3;
            } else {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                boolean matchWidth = lp.width == -1;
                boolean matchHeight = lp.height == -1;
                if (measureMatchParentChildren && (matchWidth || matchHeight)) {
                    this.mMatchParentChildren.add(child);
                }
                i2 = i3;
                int childState3 = childState;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                int state = 0;
                if (measureMatchParentChildren && !matchWidth) {
                    maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                    state = 0 | (child.getMeasuredWidthAndState() & (-16777216));
                }
                if (measureMatchParentChildren && !matchHeight) {
                    maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                    state |= (child.getMeasuredHeightAndState() >> 16) & (-256);
                }
                childState = combineMeasuredStates(childState3, state);
            }
            childState2 = i2 + 1;
        }
        int childState4 = childState;
        int maxWidth2 = maxWidth + getPaddingLeft() + getPaddingRight();
        int maxHeight2 = Math.max(maxHeight + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight());
        int maxWidth3 = Math.max(maxWidth2, getSuggestedMinimumWidth());
        Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight2 = Math.max(maxHeight2, drawable.getMinimumHeight());
            maxWidth3 = Math.max(maxWidth3, drawable.getMinimumWidth());
        }
        setMeasuredDimension(resolveSizeAndState(maxWidth3, widthMeasureSpec, childState4), resolveSizeAndState(maxHeight2, heightMeasureSpec, childState4 << 16));
        int matchCount = this.mMatchParentChildren.size();
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < matchCount) {
                View child2 = this.mMatchParentChildren.get(i5);
                ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) child2.getLayoutParams();
                if (lp2.width == i) {
                    childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec((((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight()) - lp2.leftMargin) - lp2.rightMargin, 1073741824);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight() + lp2.leftMargin + lp2.rightMargin, lp2.width);
                }
                if (lp2.height == i) {
                    childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec((((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom()) - lp2.topMargin) - lp2.bottomMargin, 1073741824);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp2.topMargin + lp2.bottomMargin, lp2.height);
                }
                child2.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                i4 = i5 + 1;
                i = -1;
            } else {
                this.mMatchParentChildren.clear();
                return;
            }
        }
    }
}
