package com.android.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.android.internal.R;
@RemoteViews.RemoteView
/* loaded from: classes3.dex */
public class MediaNotificationView extends FrameLayout {
    private View mActions;
    private View mHeader;
    private int mImagePushIn;
    private View mMainColumn;
    private final int mNotificationContentImageMarginEnd;
    private final int mNotificationContentMarginEnd;
    private ImageView mRightIcon;

    public MediaNotificationView(Context context) {
        this(context, null);
    }

    public MediaNotificationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaNotificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean hasIcon = this.mRightIcon.getVisibility() != 8;
        if (!hasIcon) {
            resetHeaderIndention();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = View.MeasureSpec.getMode(widthMeasureSpec);
        boolean reMeasure = false;
        this.mImagePushIn = 0;
        if (hasIcon && mode != 0) {
            int size = View.MeasureSpec.getSize(widthMeasureSpec);
            int size2 = size - this.mActions.getMeasuredWidth();
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) this.mRightIcon.getLayoutParams();
            int imageEndMargin = layoutParams.getMarginEnd();
            int size3 = size2 - imageEndMargin;
            int fullHeight = getMeasuredHeight();
            if (size3 > fullHeight) {
                size3 = fullHeight;
            } else if (size3 < fullHeight) {
                size3 = Math.max(0, size3);
                this.mImagePushIn = fullHeight - size3;
            }
            if (layoutParams.width != fullHeight || layoutParams.height != fullHeight) {
                layoutParams.width = fullHeight;
                layoutParams.height = fullHeight;
                this.mRightIcon.setLayoutParams(layoutParams);
                reMeasure = true;
            }
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) this.mMainColumn.getLayoutParams();
            int marginEnd = size3 + imageEndMargin + this.mNotificationContentMarginEnd;
            if (marginEnd != params.getMarginEnd()) {
                params.setMarginEnd(marginEnd);
                this.mMainColumn.setLayoutParams(params);
                reMeasure = true;
            }
            int headerMarginEnd = size3 + imageEndMargin;
            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) this.mHeader.getLayoutParams();
            if (params2.getMarginEnd() != headerMarginEnd) {
                params2.setMarginEnd(headerMarginEnd);
                this.mHeader.setLayoutParams(params2);
                reMeasure = true;
            }
            if (this.mHeader.getPaddingEnd() != this.mNotificationContentImageMarginEnd) {
                this.mHeader.setPaddingRelative(this.mHeader.getPaddingStart(), this.mHeader.getPaddingTop(), this.mNotificationContentImageMarginEnd, this.mHeader.getPaddingBottom());
                reMeasure = true;
            }
        }
        if (reMeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mImagePushIn > 0) {
            this.mRightIcon.layout(this.mRightIcon.getLeft() + this.mImagePushIn, this.mRightIcon.getTop(), this.mRightIcon.getRight() + this.mImagePushIn, this.mRightIcon.getBottom());
        }
    }

    private void resetHeaderIndention() {
        if (this.mHeader.getPaddingEnd() != this.mNotificationContentMarginEnd) {
            this.mHeader.setPaddingRelative(this.mHeader.getPaddingStart(), this.mHeader.getPaddingTop(), this.mNotificationContentMarginEnd, this.mHeader.getPaddingBottom());
        }
        ViewGroup.MarginLayoutParams headerParams = (ViewGroup.MarginLayoutParams) this.mHeader.getLayoutParams();
        headerParams.setMarginEnd(0);
        if (headerParams.getMarginEnd() != 0) {
            headerParams.setMarginEnd(0);
            this.mHeader.setLayoutParams(headerParams);
        }
    }

    public MediaNotificationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mNotificationContentMarginEnd = context.getResources().getDimensionPixelSize(R.dimen.notification_content_margin_end);
        this.mNotificationContentImageMarginEnd = context.getResources().getDimensionPixelSize(R.dimen.notification_content_image_margin_end);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mRightIcon = (ImageView) findViewById(16909334);
        this.mActions = findViewById(16909147);
        this.mHeader = findViewById(16909212);
        this.mMainColumn = findViewById(R.id.notification_main_column);
    }
}
