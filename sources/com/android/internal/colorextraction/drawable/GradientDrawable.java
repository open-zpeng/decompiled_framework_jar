package com.android.internal.colorextraction.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.graphics.ColorUtils;
/* loaded from: classes3.dex */
public class GradientDrawable extends Drawable {
    private static final float CENTRALIZED_CIRCLE_1 = -2.0f;
    private static final long COLOR_ANIMATION_DURATION = 2000;
    private static final int GRADIENT_RADIUS = 480;
    private static final String TAG = "GradientDrawable";
    private ValueAnimator mColorAnimation;
    private float mDensity;
    private int mMainColor;
    private int mMainColorTo;
    private int mSecondaryColor;
    private int mSecondaryColorTo;
    private int mAlpha = 255;
    private final Splat mSplat = new Splat(0.5f, 1.0f, 480.0f, CENTRALIZED_CIRCLE_1);
    private final Rect mWindowBounds = new Rect();
    private final Paint mPaint = new Paint();

    public GradientDrawable(Context context) {
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.mPaint.setStyle(Paint.Style.FILL);
    }

    public void setColors(ColorExtractor.GradientColors colors) {
        setColors(colors.getMainColor(), colors.getSecondaryColor(), true);
    }

    public void setColors(ColorExtractor.GradientColors colors, boolean animated) {
        setColors(colors.getMainColor(), colors.getSecondaryColor(), animated);
    }

    public void setColors(final int mainColor, final int secondaryColor, boolean animated) {
        if (mainColor == this.mMainColorTo && secondaryColor == this.mSecondaryColorTo) {
            return;
        }
        if (this.mColorAnimation != null && this.mColorAnimation.isRunning()) {
            this.mColorAnimation.cancel();
        }
        this.mMainColorTo = mainColor;
        this.mSecondaryColorTo = mainColor;
        if (animated) {
            final int mainFrom = this.mMainColor;
            final int secFrom = this.mSecondaryColor;
            ValueAnimator anim = ValueAnimator.ofFloat(0.0f, 1.0f);
            anim.setDuration(2000L);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.internal.colorextraction.drawable.-$$Lambda$GradientDrawable$lMoQsZzfSN2bVHgYiK0hm0tzCVE
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    GradientDrawable.lambda$setColors$0(GradientDrawable.this, mainFrom, mainColor, secFrom, secondaryColor, valueAnimator);
                }
            });
            anim.addListener(new AnimatorListenerAdapter() { // from class: com.android.internal.colorextraction.drawable.GradientDrawable.1
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    if (GradientDrawable.this.mColorAnimation == animation) {
                        GradientDrawable.this.mColorAnimation = null;
                    }
                }
            });
            anim.setInterpolator(new DecelerateInterpolator());
            anim.start();
            this.mColorAnimation = anim;
            return;
        }
        this.mMainColor = mainColor;
        this.mSecondaryColor = secondaryColor;
        buildPaints();
        invalidateSelf();
    }

    public static /* synthetic */ void lambda$setColors$0(GradientDrawable gradientDrawable, int mainFrom, int mainColor, int secFrom, int secondaryColor, ValueAnimator animation) {
        float ratio = ((Float) animation.getAnimatedValue()).floatValue();
        gradientDrawable.mMainColor = ColorUtils.blendARGB(mainFrom, mainColor, ratio);
        gradientDrawable.mSecondaryColor = ColorUtils.blendARGB(secFrom, secondaryColor, ratio);
        gradientDrawable.buildPaints();
        gradientDrawable.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        if (alpha != this.mAlpha) {
            this.mAlpha = alpha;
            this.mPaint.setAlpha(this.mAlpha);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mAlpha;
    }

    @Override // android.graphics.drawable.Drawable
    public void setXfermode(Xfermode mode) {
        this.mPaint.setXfermode(mode);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public void setScreenSize(int width, int height) {
        this.mWindowBounds.set(0, 0, width, height);
        setBounds(0, 0, width, height);
        buildPaints();
    }

    private void buildPaints() {
        Rect bounds = this.mWindowBounds;
        if (bounds.width() == 0) {
            return;
        }
        float w = bounds.width();
        float h = bounds.height();
        float x = this.mSplat.x * w;
        float y = this.mSplat.y * h;
        float radius = this.mSplat.radius * this.mDensity;
        RadialGradient radialGradient = new RadialGradient(x, y, radius, this.mSecondaryColor, this.mMainColor, Shader.TileMode.CLAMP);
        this.mPaint.setShader(radialGradient);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = this.mWindowBounds;
        if (bounds.width() == 0) {
            throw new IllegalStateException("You need to call setScreenSize before drawing.");
        }
        float w = bounds.width();
        float h = bounds.height();
        float x = this.mSplat.x * w;
        float y = this.mSplat.y * h;
        float radius = Math.max(w, h);
        canvas.drawRect(x - radius, y - radius, x + radius, y + radius, this.mPaint);
    }

    @VisibleForTesting
    public int getMainColor() {
        return this.mMainColor;
    }

    @VisibleForTesting
    public int getSecondaryColor() {
        return this.mSecondaryColor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class Splat {
        final float colorIndex;
        final float radius;
        final float x;
        final float y;

        Splat(float x, float y, float radius, float colorIndex) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.colorIndex = colorIndex;
        }
    }
}
