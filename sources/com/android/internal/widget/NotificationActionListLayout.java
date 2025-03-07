package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.Comparator;

@RemoteViews.RemoteView
/* loaded from: classes3.dex */
public class NotificationActionListLayout extends LinearLayout {
    public static final Comparator<Pair<Integer, TextView>> MEASURE_ORDER_COMPARATOR = new Comparator() { // from class: com.android.internal.widget.-$$Lambda$NotificationActionListLayout$uFZFEmIEBpI3kn6c3tNvvgmMSv8
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            int compareTo;
            compareTo = ((Integer) ((Pair) obj).first).compareTo((Integer) ((Pair) obj2).first);
            return compareTo;
        }
    };
    private int mDefaultPaddingBottom;
    private int mDefaultPaddingTop;
    private int mEmphasizedHeight;
    private boolean mEmphasizedMode;
    private final int mGravity;
    private ArrayList<View> mMeasureOrderOther;
    private ArrayList<Pair<Integer, TextView>> mMeasureOrderTextViews;
    private int mRegularHeight;
    private int mTotalWidth;

    public NotificationActionListLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotificationActionListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NotificationActionListLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTotalWidth = 0;
        this.mMeasureOrderTextViews = new ArrayList<>();
        this.mMeasureOrderOther = new ArrayList<>();
        int[] attrIds = {16842927};
        TypedArray ta = context.obtainStyledAttributes(attrs, attrIds, defStyleAttr, defStyleRes);
        this.mGravity = ta.getInt(0, 0);
        ta.recycle();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        boolean needRebuild;
        View c;
        int usedWidthForChild;
        int i2;
        if (this.mEmphasizedMode) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int N = getChildCount();
        int i3 = 0;
        int textViews = 0;
        int otherViews = 0;
        int notGoneChildren = 0;
        while (true) {
            i = 8;
            if (i3 >= N) {
                break;
            }
            View c2 = getChildAt(i3);
            if (c2 instanceof TextView) {
                textViews++;
            } else {
                otherViews++;
            }
            if (c2.getVisibility() != 8) {
                notGoneChildren++;
            }
            i3++;
        }
        boolean needRebuild2 = false;
        needRebuild2 = (textViews == this.mMeasureOrderTextViews.size() && otherViews == this.mMeasureOrderOther.size()) ? true : true;
        if (needRebuild2) {
            needRebuild = needRebuild2;
        } else {
            int size = this.mMeasureOrderTextViews.size();
            for (int i4 = 0; i4 < size; i4++) {
                Pair<Integer, TextView> pair = this.mMeasureOrderTextViews.get(i4);
                if (pair.first.intValue() != pair.second.getText().length()) {
                    needRebuild2 = true;
                }
            }
            needRebuild = needRebuild2;
        }
        if (needRebuild) {
            rebuildMeasureOrder(textViews, otherViews);
        }
        boolean constrained = View.MeasureSpec.getMode(widthMeasureSpec) != 0;
        int innerWidth = (View.MeasureSpec.getSize(widthMeasureSpec) - this.mPaddingLeft) - this.mPaddingRight;
        int otherSize = this.mMeasureOrderOther.size();
        int usedWidth = 0;
        int measuredChildren = 0;
        int i5 = 0;
        while (i5 < N) {
            if (i5 < otherSize) {
                c = this.mMeasureOrderOther.get(i5);
            } else {
                c = this.mMeasureOrderTextViews.get(i5 - otherSize).second;
            }
            if (c.getVisibility() == i) {
                i2 = i5;
            } else {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) c.getLayoutParams();
                int usedWidthForChild2 = usedWidth;
                if (!constrained) {
                    usedWidthForChild = usedWidthForChild2;
                } else {
                    int availableWidth = innerWidth - usedWidth;
                    int maxWidthForChild = availableWidth / (notGoneChildren - measuredChildren);
                    int usedWidthForChild3 = innerWidth - maxWidthForChild;
                    usedWidthForChild = usedWidthForChild3;
                }
                i2 = i5;
                measureChildWithMargins(c, widthMeasureSpec, usedWidthForChild, heightMeasureSpec, 0);
                usedWidth += c.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
                measuredChildren++;
            }
            i5 = i2 + 1;
            i = 8;
        }
        this.mTotalWidth = usedWidth + this.mPaddingRight + this.mPaddingLeft;
        setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec), resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    private void rebuildMeasureOrder(int capacityText, int capacityOther) {
        clearMeasureOrder();
        this.mMeasureOrderTextViews.ensureCapacity(capacityText);
        this.mMeasureOrderOther.ensureCapacity(capacityOther);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View c = getChildAt(i);
            if ((c instanceof TextView) && ((TextView) c).getText().length() > 0) {
                this.mMeasureOrderTextViews.add(Pair.create(Integer.valueOf(((TextView) c).getText().length()), (TextView) c));
            } else {
                this.mMeasureOrderOther.add(c);
            }
        }
        this.mMeasureOrderTextViews.sort(MEASURE_ORDER_COMPARATOR);
    }

    private void clearMeasureOrder() {
        this.mMeasureOrderOther.clear();
        this.mMeasureOrderTextViews.clear();
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        clearMeasureOrder();
        if (child.getBackground() instanceof RippleDrawable) {
            ((RippleDrawable) child.getBackground()).setForceSoftware(true);
        }
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        clearMeasureOrder();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childLeft;
        boolean isLayoutRtl;
        int paddingTop;
        NotificationActionListLayout notificationActionListLayout = this;
        if (notificationActionListLayout.mEmphasizedMode) {
            super.onLayout(changed, left, top, right, bottom);
            return;
        }
        boolean isLayoutRtl2 = isLayoutRtl();
        int paddingTop2 = notificationActionListLayout.mPaddingTop;
        boolean centerAligned = (notificationActionListLayout.mGravity & 1) != 0;
        if (centerAligned) {
            childLeft = ((notificationActionListLayout.mPaddingLeft + left) + ((right - left) / 2)) - (notificationActionListLayout.mTotalWidth / 2);
        } else {
            childLeft = notificationActionListLayout.mPaddingLeft;
            int absoluteGravity = Gravity.getAbsoluteGravity(Gravity.START, getLayoutDirection());
            if (absoluteGravity == 5) {
                childLeft += (right - left) - notificationActionListLayout.mTotalWidth;
            }
        }
        int absoluteGravity2 = bottom - top;
        int innerHeight = (absoluteGravity2 - paddingTop2) - notificationActionListLayout.mPaddingBottom;
        int count = getChildCount();
        int start = 0;
        int dir = 1;
        if (isLayoutRtl2) {
            start = count - 1;
            dir = -1;
        }
        int i = 0;
        while (i < count) {
            int childIndex = (dir * i) + start;
            View child = notificationActionListLayout.getChildAt(childIndex);
            if (child.getVisibility() == 8) {
                isLayoutRtl = isLayoutRtl2;
                paddingTop = paddingTop2;
            } else {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                int childTop = ((paddingTop2 + ((innerHeight - childHeight) / 2)) + lp.topMargin) - lp.bottomMargin;
                isLayoutRtl = isLayoutRtl2;
                int childLeft2 = childLeft + lp.leftMargin;
                paddingTop = paddingTop2;
                child.layout(childLeft2, childTop, childLeft2 + childWidth, childTop + childHeight);
                childLeft = childLeft2 + lp.rightMargin + childWidth;
            }
            i++;
            notificationActionListLayout = this;
            isLayoutRtl2 = isLayoutRtl;
            paddingTop2 = paddingTop;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mDefaultPaddingBottom = getPaddingBottom();
        this.mDefaultPaddingTop = getPaddingTop();
        updateHeights();
    }

    private void updateHeights() {
        int paddingTop = getResources().getDimensionPixelSize(R.dimen.notification_content_margin);
        int paddingBottom = getResources().getDimensionPixelSize(R.dimen.notification_content_margin_end);
        this.mEmphasizedHeight = paddingBottom + paddingTop + getResources().getDimensionPixelSize(R.dimen.notification_action_emphasized_height);
        this.mRegularHeight = getResources().getDimensionPixelSize(R.dimen.notification_action_list_height);
    }

    @RemotableViewMethod
    public void setEmphasizedMode(boolean emphasizedMode) {
        int height;
        this.mEmphasizedMode = emphasizedMode;
        if (emphasizedMode) {
            int paddingTop = getResources().getDimensionPixelSize(R.dimen.notification_content_margin);
            int paddingBottom = getResources().getDimensionPixelSize(R.dimen.notification_content_margin_end);
            height = this.mEmphasizedHeight;
            int buttonPaddingInternal = getResources().getDimensionPixelSize(R.dimen.button_inset_vertical_material);
            setPaddingRelative(getPaddingStart(), paddingTop - buttonPaddingInternal, getPaddingEnd(), paddingBottom - buttonPaddingInternal);
        } else {
            setPaddingRelative(getPaddingStart(), this.mDefaultPaddingTop, getPaddingEnd(), this.mDefaultPaddingBottom);
            height = this.mRegularHeight;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }

    public int getExtraMeasureHeight() {
        if (this.mEmphasizedMode) {
            return this.mEmphasizedHeight - this.mRegularHeight;
        }
        return 0;
    }
}
