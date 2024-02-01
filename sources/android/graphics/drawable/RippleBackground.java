package android.graphics.drawable;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.FloatProperty;
import android.view.animation.LinearInterpolator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RippleBackground extends RippleComponent {
    private static final TimeInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static final BackgroundProperty OPACITY = new BackgroundProperty("opacity") { // from class: android.graphics.drawable.RippleBackground.1
        @Override // android.util.FloatProperty
        public void setValue(RippleBackground object, float value) {
            object.mOpacity = value;
            object.invalidateSelf();
        }

        @Override // android.util.Property
        public Float get(RippleBackground object) {
            return Float.valueOf(object.mOpacity);
        }
    };
    private static final int OPACITY_DURATION = 80;
    private ObjectAnimator mAnimator;
    private boolean mFocused;
    private boolean mHovered;
    private boolean mIsBounded;
    private float mOpacity;

    public synchronized RippleBackground(RippleDrawable owner, Rect bounds, boolean isBounded) {
        super(owner, bounds);
        this.mOpacity = 0.0f;
        this.mFocused = false;
        this.mHovered = false;
        this.mIsBounded = isBounded;
    }

    public synchronized boolean isVisible() {
        return this.mOpacity > 0.0f;
    }

    public synchronized void draw(Canvas c, Paint p) {
        int origAlpha = p.getAlpha();
        int alpha = Math.min((int) ((origAlpha * this.mOpacity) + 0.5f), 255);
        if (alpha > 0) {
            p.setAlpha(alpha);
            c.drawCircle(0.0f, 0.0f, this.mTargetRadius, p);
            p.setAlpha(origAlpha);
        }
    }

    public synchronized void setState(boolean focused, boolean hovered, boolean pressed) {
        boolean z = false;
        if (!this.mFocused) {
            focused = focused && !pressed;
        }
        if (!this.mHovered) {
            if (hovered && !pressed) {
                z = true;
            }
            hovered = z;
        }
        if (this.mHovered != hovered || this.mFocused != focused) {
            this.mHovered = hovered;
            this.mFocused = focused;
            onStateChanged();
        }
    }

    private synchronized void onStateChanged() {
        float newOpacity = this.mFocused ? 0.6f : this.mHovered ? 0.2f : 0.0f;
        if (this.mAnimator != null) {
            this.mAnimator.cancel();
            this.mAnimator = null;
        }
        this.mAnimator = ObjectAnimator.ofFloat(this, OPACITY, newOpacity);
        this.mAnimator.setDuration(80L);
        this.mAnimator.setInterpolator(LINEAR_INTERPOLATOR);
        this.mAnimator.start();
    }

    public synchronized void jumpToFinal() {
        if (this.mAnimator != null) {
            this.mAnimator.end();
            this.mAnimator = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class BackgroundProperty extends FloatProperty<RippleBackground> {
        public synchronized BackgroundProperty(String name) {
            super(name);
        }
    }
}
