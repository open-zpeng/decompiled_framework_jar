package com.android.internal.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
/* loaded from: classes3.dex */
public class AlertDialogLayout extends LinearLayout {
    public AlertDialogLayout(Context context) {
        super(context);
    }

    public AlertDialogLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlertDialogLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AlertDialogLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!tryOnMeasure(widthMeasureSpec, heightMeasureSpec)) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private boolean tryOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childHeightSpec;
        int count = getChildCount();
        View middlePanel = null;
        View buttonPanel = null;
        View topPanel = null;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                int id = child.getId();
                if (id == 16908859) {
                    buttonPanel = child;
                } else if (id == 16908906 || id == 16908913) {
                    if (middlePanel != null) {
                        return false;
                    }
                    middlePanel = child;
                } else if (id != 16909523) {
                    return false;
                } else {
                    topPanel = child;
                }
            }
        }
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int childState = 0;
        int usedHeight = getPaddingTop() + getPaddingBottom();
        if (topPanel != null) {
            topPanel.measure(widthMeasureSpec, 0);
            usedHeight += topPanel.getMeasuredHeight();
            childState = combineMeasuredStates(0, topPanel.getMeasuredState());
        }
        int buttonHeight = 0;
        int buttonWantsHeight = 0;
        if (buttonPanel != null) {
            buttonPanel.measure(widthMeasureSpec, 0);
            buttonHeight = resolveMinimumHeight(buttonPanel);
            buttonWantsHeight = buttonPanel.getMeasuredHeight() - buttonHeight;
            usedHeight += buttonHeight;
            childState = combineMeasuredStates(childState, buttonPanel.getMeasuredState());
        }
        int middleHeight = 0;
        if (middlePanel != null) {
            if (heightMode == 0) {
                childHeightSpec = 0;
            } else {
                childHeightSpec = View.MeasureSpec.makeMeasureSpec(Math.max(0, heightSize - usedHeight), heightMode);
            }
            middlePanel.measure(widthMeasureSpec, childHeightSpec);
            middleHeight = middlePanel.getMeasuredHeight();
            usedHeight += middleHeight;
            childState = combineMeasuredStates(childState, middlePanel.getMeasuredState());
        }
        int remainingHeight = heightSize - usedHeight;
        if (buttonPanel != null) {
            int usedHeight2 = usedHeight - buttonHeight;
            int heightToGive = Math.min(remainingHeight, buttonWantsHeight);
            if (heightToGive > 0) {
                remainingHeight -= heightToGive;
                buttonHeight += heightToGive;
            }
            buttonPanel.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(buttonHeight, 1073741824));
            usedHeight = usedHeight2 + buttonPanel.getMeasuredHeight();
            childState = combineMeasuredStates(childState, buttonPanel.getMeasuredState());
            remainingHeight = remainingHeight;
        }
        if (middlePanel != null && remainingHeight > 0) {
            int heightToGive2 = remainingHeight;
            middlePanel.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(middleHeight + heightToGive2, heightMode));
            usedHeight = (usedHeight - middleHeight) + middlePanel.getMeasuredHeight();
            childState = combineMeasuredStates(childState, middlePanel.getMeasuredState());
            remainingHeight -= heightToGive2;
        }
        int maxWidth = 0;
        int maxWidth2 = 0;
        while (maxWidth2 < count) {
            int remainingHeight2 = remainingHeight;
            View child2 = getChildAt(maxWidth2);
            View buttonPanel2 = buttonPanel;
            View middlePanel2 = middlePanel;
            if (child2.getVisibility() != 8) {
                maxWidth = Math.max(maxWidth, child2.getMeasuredWidth());
            }
            maxWidth2++;
            remainingHeight = remainingHeight2;
            buttonPanel = buttonPanel2;
            middlePanel = middlePanel2;
        }
        int widthSizeAndState = resolveSizeAndState(maxWidth + getPaddingLeft() + getPaddingRight(), widthMeasureSpec, childState);
        int heightSizeAndState = resolveSizeAndState(usedHeight, heightMeasureSpec, 0);
        setMeasuredDimension(widthSizeAndState, heightSizeAndState);
        if (widthMode != 1073741824) {
            forceUniformWidth(count, heightMeasureSpec);
            return true;
        }
        return true;
    }

    private void forceUniformWidth(int count, int heightMeasureSpec) {
        int uniformMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                if (lp.width == -1) {
                    int oldHeight = lp.height;
                    lp.height = child.getMeasuredHeight();
                    measureChildWithMargins(child, uniformMeasureSpec, 0, heightMeasureSpec, 0);
                    lp.height = oldHeight;
                }
            }
        }
    }

    private int resolveMinimumHeight(View v) {
        int minHeight = v.getMinimumHeight();
        if (minHeight > 0) {
            return minHeight;
        }
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            if (vg.getChildCount() == 1) {
                return resolveMinimumHeight(vg.getChildAt(0));
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childTop;
        int i;
        Drawable dividerDrawable;
        int majorGravity;
        int count;
        int childLeft;
        AlertDialogLayout alertDialogLayout = this;
        int paddingLeft = alertDialogLayout.mPaddingLeft;
        int width = right - left;
        int childRight = width - alertDialogLayout.mPaddingRight;
        int childSpace = (width - paddingLeft) - alertDialogLayout.mPaddingRight;
        int totalLength = getMeasuredHeight();
        int count2 = getChildCount();
        int gravity = getGravity();
        int majorGravity2 = gravity & 112;
        int minorGravity = gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (majorGravity2 == 16) {
            int childTop2 = alertDialogLayout.mPaddingTop;
            childTop = childTop2 + (((bottom - top) - totalLength) / 2);
        } else if (majorGravity2 == 80) {
            childTop = ((alertDialogLayout.mPaddingTop + bottom) - top) - totalLength;
        } else {
            childTop = alertDialogLayout.mPaddingTop;
        }
        Drawable dividerDrawable2 = getDividerDrawable();
        int i2 = 0;
        int dividerHeight = dividerDrawable2 == null ? 0 : dividerDrawable2.getIntrinsicHeight();
        while (i2 < count2) {
            View child = alertDialogLayout.getChildAt(i2);
            if (child != null) {
                dividerDrawable = dividerDrawable2;
                majorGravity = majorGravity2;
                if (child.getVisibility() != 8) {
                    int childWidth = child.getMeasuredWidth();
                    int childHeight = child.getMeasuredHeight();
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                    int layoutGravity = lp.gravity;
                    if (layoutGravity < 0) {
                        layoutGravity = minorGravity;
                    }
                    int layoutDirection = getLayoutDirection();
                    int absoluteGravity = Gravity.getAbsoluteGravity(layoutGravity, layoutDirection);
                    int i3 = absoluteGravity & 7;
                    if (i3 == 1) {
                        int childLeft2 = childSpace - childWidth;
                        childLeft = (((childLeft2 / 2) + paddingLeft) + lp.leftMargin) - lp.rightMargin;
                    } else if (i3 == 5) {
                        childLeft = (childRight - childWidth) - lp.rightMargin;
                    } else {
                        childLeft = lp.leftMargin + paddingLeft;
                    }
                    if (alertDialogLayout.hasDividerBeforeChildAt(i2)) {
                        childTop += dividerHeight;
                    }
                    int childTop3 = childTop + lp.topMargin;
                    i = i2;
                    count = count2;
                    alertDialogLayout.setChildFrame(child, childLeft, childTop3, childWidth, childHeight);
                    childTop = childTop3 + childHeight + lp.bottomMargin;
                } else {
                    i = i2;
                    count = count2;
                }
            } else {
                i = i2;
                dividerDrawable = dividerDrawable2;
                majorGravity = majorGravity2;
                count = count2;
            }
            i2 = i + 1;
            dividerDrawable2 = dividerDrawable;
            majorGravity2 = majorGravity;
            count2 = count;
            alertDialogLayout = this;
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }
}
