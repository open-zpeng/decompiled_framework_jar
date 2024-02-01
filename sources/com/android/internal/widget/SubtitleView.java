package com.android.internal.widget;

import android.R;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.CaptioningManager;
/* loaded from: classes3.dex */
public class SubtitleView extends View {
    private static final int COLOR_BEVEL_DARK = Integer.MIN_VALUE;
    private static final int COLOR_BEVEL_LIGHT = -2130706433;
    private static final float INNER_PADDING_RATIO = 0.125f;
    private Layout.Alignment mAlignment;
    private int mBackgroundColor;
    private final float mCornerRadius;
    private int mEdgeColor;
    private int mEdgeType;
    private int mForegroundColor;
    private boolean mHasMeasurements;
    private int mInnerPaddingX;
    private int mLastMeasuredWidth;
    private StaticLayout mLayout;
    private final RectF mLineBounds;
    private final float mOutlineWidth;
    private Paint mPaint;
    private final float mShadowOffsetX;
    private final float mShadowOffsetY;
    private final float mShadowRadius;
    private float mSpacingAdd;
    private float mSpacingMult;
    private final SpannableStringBuilder mText;
    private TextPaint mTextPaint;

    public synchronized SubtitleView(Context context) {
        this(context, null);
    }

    public synchronized SubtitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public synchronized SubtitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public synchronized SubtitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
        this.mLineBounds = new RectF();
        this.mText = new SpannableStringBuilder();
        this.mSpacingMult = 1.0f;
        this.mSpacingAdd = 0.0f;
        this.mInnerPaddingX = 0;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextView, defStyleAttr, defStyleRes);
        CharSequence text = "";
        int textSize = 15;
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == 0) {
                textSize = a.getDimensionPixelSize(attr, textSize);
            } else if (attr == 18) {
                text = a.getText(attr);
            } else {
                switch (attr) {
                    case 53:
                        this.mSpacingAdd = a.getDimensionPixelSize(attr, (int) this.mSpacingAdd);
                        continue;
                    case 54:
                        this.mSpacingMult = a.getFloat(attr, this.mSpacingMult);
                        continue;
                }
            }
        }
        Resources res = getContext().getResources();
        this.mCornerRadius = res.getDimensionPixelSize(com.android.internal.R.dimen.subtitle_corner_radius);
        this.mOutlineWidth = res.getDimensionPixelSize(com.android.internal.R.dimen.subtitle_outline_width);
        this.mShadowRadius = res.getDimensionPixelSize(com.android.internal.R.dimen.subtitle_shadow_radius);
        this.mShadowOffsetX = res.getDimensionPixelSize(com.android.internal.R.dimen.subtitle_shadow_offset);
        this.mShadowOffsetY = this.mShadowOffsetX;
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setSubpixelText(true);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        setText(text);
        setTextSize(textSize);
    }

    public synchronized void setText(int resId) {
        CharSequence text = getContext().getText(resId);
        setText(text);
    }

    public synchronized void setText(CharSequence text) {
        this.mText.clear();
        this.mText.append(text);
        this.mHasMeasurements = false;
        requestLayout();
        invalidate();
    }

    public synchronized void setForegroundColor(int color) {
        this.mForegroundColor = color;
        invalidate();
    }

    @Override // android.view.View
    public void setBackgroundColor(int color) {
        this.mBackgroundColor = color;
        invalidate();
    }

    public synchronized void setEdgeType(int edgeType) {
        this.mEdgeType = edgeType;
        invalidate();
    }

    public synchronized void setEdgeColor(int color) {
        this.mEdgeColor = color;
        invalidate();
    }

    public synchronized void setTextSize(float size) {
        if (this.mTextPaint.getTextSize() != size) {
            this.mTextPaint.setTextSize(size);
            this.mInnerPaddingX = (int) ((INNER_PADDING_RATIO * size) + 0.5f);
            this.mHasMeasurements = false;
            requestLayout();
            invalidate();
        }
    }

    public synchronized void setTypeface(Typeface typeface) {
        if (this.mTextPaint.getTypeface() != typeface) {
            this.mTextPaint.setTypeface(typeface);
            this.mHasMeasurements = false;
            requestLayout();
            invalidate();
        }
    }

    public synchronized void setAlignment(Layout.Alignment textAlignment) {
        if (this.mAlignment != textAlignment) {
            this.mAlignment = textAlignment;
            this.mHasMeasurements = false;
            requestLayout();
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = View.MeasureSpec.getSize(widthMeasureSpec);
        if (computeMeasurements(widthSpec)) {
            StaticLayout layout = this.mLayout;
            int paddingX = this.mPaddingLeft + this.mPaddingRight + (this.mInnerPaddingX * 2);
            int width = layout.getWidth() + paddingX;
            int height = layout.getHeight() + this.mPaddingTop + this.mPaddingBottom;
            setMeasuredDimension(width, height);
            return;
        }
        setMeasuredDimension(16777216, 16777216);
    }

    @Override // android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        computeMeasurements(width);
    }

    private synchronized boolean computeMeasurements(int maxWidth) {
        if (this.mHasMeasurements && maxWidth == this.mLastMeasuredWidth) {
            return true;
        }
        int paddingX = this.mPaddingLeft + this.mPaddingRight + (this.mInnerPaddingX * 2);
        int maxWidth2 = maxWidth - paddingX;
        if (maxWidth2 <= 0) {
            return false;
        }
        this.mHasMeasurements = true;
        this.mLastMeasuredWidth = maxWidth2;
        this.mLayout = StaticLayout.Builder.obtain(this.mText, 0, this.mText.length(), this.mTextPaint, maxWidth2).setAlignment(this.mAlignment).setLineSpacing(this.mSpacingAdd, this.mSpacingMult).setUseLineSpacingFromFallbacks(true).build();
        return true;
    }

    public synchronized void setStyle(int styleId) {
        CaptioningManager.CaptionStyle style;
        Context context = this.mContext;
        ContentResolver cr = context.getContentResolver();
        if (styleId == -1) {
            style = CaptioningManager.CaptionStyle.getCustomStyle(cr);
        } else {
            style = CaptioningManager.CaptionStyle.PRESETS[styleId];
        }
        CaptioningManager.CaptionStyle defStyle = CaptioningManager.CaptionStyle.DEFAULT;
        this.mForegroundColor = style.hasForegroundColor() ? style.foregroundColor : defStyle.foregroundColor;
        this.mBackgroundColor = style.hasBackgroundColor() ? style.backgroundColor : defStyle.backgroundColor;
        this.mEdgeType = style.hasEdgeType() ? style.edgeType : defStyle.edgeType;
        this.mEdgeColor = style.hasEdgeColor() ? style.edgeColor : defStyle.edgeColor;
        this.mHasMeasurements = false;
        Typeface typeface = style.getTypeface();
        setTypeface(typeface);
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00de A[LOOP:2: B:37:0x00dc->B:38:0x00de, LOOP_END] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onDraw(android.graphics.Canvas r18) {
        /*
            Method dump skipped, instructions count: 237
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.SubtitleView.onDraw(android.graphics.Canvas):void");
    }
}
