package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.android.internal.R;
import com.xiaopeng.util.FeatureOption;
/* loaded from: classes3.dex */
public class EdgeEffect {
    private static final float EPSILON = 0.001f;
    private static final float GLOW_ALPHA_START = 0.09f;
    private static final float MAX_ALPHA = 0.15f;
    private static final float MAX_GLOW_SCALE = 2.0f;
    private static final int MAX_VELOCITY = 10000;
    private static final int MIN_VELOCITY = 100;
    private static final int PULL_DECAY_TIME = 2000;
    private static final float PULL_DISTANCE_ALPHA_GLOW_FACTOR = 0.8f;
    private static final float PULL_GLOW_BEGIN = 0.0f;
    private static final int PULL_TIME = 167;
    private static final float RADIUS_FACTOR = 0.6f;
    private static final int RECEDE_TIME = 600;
    private static final int STATE_ABSORB = 2;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PULL = 1;
    private static final int STATE_PULL_DECAY = 4;
    private static final int STATE_RECEDE = 3;
    private static final String TAG = "EdgeEffect";
    private static final int VELOCITY_GLOW_FACTOR = 6;
    private float mBaseGlowScale;
    private float mDuration;
    private float mGlowAlpha;
    private float mGlowAlphaFinish;
    private float mGlowAlphaStart;
    public protected float mGlowScaleY;
    private float mGlowScaleYFinish;
    private float mGlowScaleYStart;
    private final Interpolator mInterpolator;
    private float mPullDistance;
    private float mRadius;
    private long mStartTime;
    private static final double ANGLE = 0.5235987755982988d;
    private static final float SIN = (float) Math.sin(ANGLE);
    private static final float COS = (float) Math.cos(ANGLE);
    private int mState = 0;
    private final Rect mBounds = new Rect();
    public protected final Paint mPaint = new Paint();
    private float mDisplacement = 0.5f;
    private float mTargetDisplacement = 0.5f;

    public EdgeEffect(Context context) {
        this.mPaint.setAntiAlias(true);
        TypedArray a = context.obtainStyledAttributes(R.styleable.EdgeEffect);
        int themeColor = a.getColor(0, -10066330);
        a.recycle();
        this.mPaint.setColor((16777215 & themeColor) | 855638016);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        this.mInterpolator = new DecelerateInterpolator();
    }

    public void setSize(int width, int height) {
        float r = (width * 0.6f) / SIN;
        float y = COS * r;
        float h = r - y;
        float or = (height * 0.6f) / SIN;
        float oy = COS * or;
        float oh = or - oy;
        this.mRadius = r;
        this.mBaseGlowScale = h > 0.0f ? Math.min(oh / h, 1.0f) : 1.0f;
        this.mBounds.set(this.mBounds.left, this.mBounds.top, width, (int) Math.min(height, h));
    }

    public boolean isFinished() {
        return this.mState == 0;
    }

    public void finish() {
        this.mState = 0;
    }

    public void onPull(float deltaDistance) {
        onPull(deltaDistance, 0.5f);
    }

    public void onPull(float deltaDistance, float displacement) {
        long now = AnimationUtils.currentAnimationTimeMillis();
        this.mTargetDisplacement = displacement;
        if (this.mState == 4 && ((float) (now - this.mStartTime)) < this.mDuration) {
            return;
        }
        if (this.mState != 1) {
            this.mGlowScaleY = Math.max(0.0f, this.mGlowScaleY);
        }
        this.mState = 1;
        this.mStartTime = now;
        this.mDuration = 167.0f;
        this.mPullDistance += deltaDistance;
        float absdd = Math.abs(deltaDistance);
        float min = Math.min((float) MAX_ALPHA, this.mGlowAlpha + (PULL_DISTANCE_ALPHA_GLOW_FACTOR * absdd));
        this.mGlowAlphaStart = min;
        this.mGlowAlpha = min;
        if (this.mPullDistance == 0.0f) {
            this.mGlowScaleYStart = 0.0f;
            this.mGlowScaleY = 0.0f;
        } else {
            float scale = (float) (Math.max((double) FeatureOption.FO_BOOT_POLICY_CPU, (1.0d - (1.0d / Math.sqrt(Math.abs(this.mPullDistance) * this.mBounds.height()))) - 0.3d) / 0.7d);
            this.mGlowScaleYStart = scale;
            this.mGlowScaleY = scale;
        }
        this.mGlowAlphaFinish = this.mGlowAlpha;
        this.mGlowScaleYFinish = this.mGlowScaleY;
    }

    public void onRelease() {
        this.mPullDistance = 0.0f;
        if (this.mState != 1 && this.mState != 4) {
            return;
        }
        this.mState = 3;
        this.mGlowAlphaStart = this.mGlowAlpha;
        this.mGlowScaleYStart = this.mGlowScaleY;
        this.mGlowAlphaFinish = 0.0f;
        this.mGlowScaleYFinish = 0.0f;
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mDuration = 600.0f;
    }

    public void onAbsorb(int velocity) {
        this.mState = 2;
        int velocity2 = Math.min(Math.max(100, Math.abs(velocity)), 10000);
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mDuration = (velocity2 * 0.02f) + MAX_ALPHA;
        this.mGlowAlphaStart = GLOW_ALPHA_START;
        this.mGlowScaleYStart = Math.max(this.mGlowScaleY, 0.0f);
        this.mGlowScaleYFinish = Math.min(0.025f + ((((velocity2 / 100) * velocity2) * 1.5E-4f) / MAX_GLOW_SCALE), 1.0f);
        this.mGlowAlphaFinish = Math.max(this.mGlowAlphaStart, Math.min(velocity2 * 6 * 1.0E-5f, (float) MAX_ALPHA));
        this.mTargetDisplacement = 0.5f;
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
    }

    public int getColor() {
        return this.mPaint.getColor();
    }

    public boolean draw(Canvas canvas) {
        update();
        int count = canvas.save();
        float centerX = this.mBounds.centerX();
        float centerY = this.mBounds.height() - this.mRadius;
        canvas.scale(1.0f, Math.min(this.mGlowScaleY, 1.0f) * this.mBaseGlowScale, centerX, 0.0f);
        float displacement = Math.max(0.0f, Math.min(this.mDisplacement, 1.0f)) - 0.5f;
        float translateX = (this.mBounds.width() * displacement) / MAX_GLOW_SCALE;
        canvas.clipRect(this.mBounds);
        canvas.translate(translateX, 0.0f);
        this.mPaint.setAlpha((int) (255.0f * this.mGlowAlpha));
        canvas.drawCircle(centerX, centerY, this.mRadius, this.mPaint);
        canvas.restoreToCount(count);
        boolean oneLastFrame = false;
        if (this.mState == 3 && this.mGlowScaleY == 0.0f) {
            this.mState = 0;
            oneLastFrame = true;
        }
        return this.mState != 0 || oneLastFrame;
    }

    public int getMaxHeight() {
        return (int) ((this.mBounds.height() * MAX_GLOW_SCALE) + 0.5f);
    }

    private synchronized void update() {
        long time = AnimationUtils.currentAnimationTimeMillis();
        float t = Math.min(((float) (time - this.mStartTime)) / this.mDuration, 1.0f);
        float interp = this.mInterpolator.getInterpolation(t);
        this.mGlowAlpha = this.mGlowAlphaStart + ((this.mGlowAlphaFinish - this.mGlowAlphaStart) * interp);
        this.mGlowScaleY = this.mGlowScaleYStart + ((this.mGlowScaleYFinish - this.mGlowScaleYStart) * interp);
        this.mDisplacement = (this.mDisplacement + this.mTargetDisplacement) / MAX_GLOW_SCALE;
        if (t >= 0.999f) {
            switch (this.mState) {
                case 1:
                    this.mState = 4;
                    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDuration = 2000.0f;
                    this.mGlowAlphaStart = this.mGlowAlpha;
                    this.mGlowScaleYStart = this.mGlowScaleY;
                    this.mGlowAlphaFinish = 0.0f;
                    this.mGlowScaleYFinish = 0.0f;
                    return;
                case 2:
                    this.mState = 3;
                    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDuration = 600.0f;
                    this.mGlowAlphaStart = this.mGlowAlpha;
                    this.mGlowScaleYStart = this.mGlowScaleY;
                    this.mGlowAlphaFinish = 0.0f;
                    this.mGlowScaleYFinish = 0.0f;
                    return;
                case 3:
                    this.mState = 0;
                    return;
                case 4:
                    this.mState = 3;
                    return;
                default:
                    return;
            }
        }
    }
}
