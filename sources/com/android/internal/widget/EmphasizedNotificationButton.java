package com.android.internal.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.widget.Button;
import android.widget.RemoteViews;
import com.android.internal.R;

@RemoteViews.RemoteView
/* loaded from: classes3.dex */
public class EmphasizedNotificationButton extends Button {
    private final RippleDrawable mRipple;
    private final int mStrokeColor;
    private final int mStrokeWidth;

    public EmphasizedNotificationButton(Context context) {
        this(context, null);
    }

    public EmphasizedNotificationButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmphasizedNotificationButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EmphasizedNotificationButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        DrawableWrapper background = (DrawableWrapper) getBackground().mutate();
        this.mRipple = (RippleDrawable) background.getDrawable();
        this.mStrokeWidth = getResources().getDimensionPixelSize(R.dimen.emphasized_button_stroke_width);
        this.mStrokeColor = getContext().getColor(R.color.material_grey_300);
        this.mRipple.mutate();
    }

    @RemotableViewMethod
    public void setRippleColor(ColorStateList color) {
        this.mRipple.setColor(color);
        invalidate();
    }

    @RemotableViewMethod
    public void setButtonBackground(ColorStateList color) {
        GradientDrawable inner = (GradientDrawable) this.mRipple.getDrawable(0);
        inner.setColor(color);
        invalidate();
    }

    @RemotableViewMethod
    public void setHasStroke(boolean hasStroke) {
        GradientDrawable inner = (GradientDrawable) this.mRipple.getDrawable(0);
        inner.setStroke(hasStroke ? this.mStrokeWidth : 0, this.mStrokeColor);
        invalidate();
    }
}
