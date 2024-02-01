package android.widget;

import android.content.Context;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.xiaopeng.util.FeatureOption;
/* loaded from: classes3.dex */
public class OverScroller {
    private static final int DEFAULT_DURATION = 250;
    private static final int FLING_MODE = 1;
    private static final int SCROLL_MODE = 0;
    private final boolean mFlywheel;
    public protected Interpolator mInterpolator;
    private int mMode;
    private final SplineOverScroller mScrollerX;
    public protected final SplineOverScroller mScrollerY;

    public OverScroller(Context context) {
        this(context, null);
    }

    public OverScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, true);
    }

    private protected OverScroller(Context context, Interpolator interpolator, boolean flywheel) {
        if (interpolator == null) {
            this.mInterpolator = new Scroller.ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
        this.mFlywheel = flywheel;
        this.mScrollerX = new SplineOverScroller(context);
        this.mScrollerY = new SplineOverScroller(context);
    }

    @Deprecated
    public OverScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY) {
        this(context, interpolator, true);
    }

    @Deprecated
    public OverScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY, boolean flywheel) {
        this(context, interpolator, flywheel);
    }

    public private protected void setInterpolator(Interpolator interpolator) {
        if (interpolator == null) {
            this.mInterpolator = new Scroller.ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
    }

    public final void setFriction(float friction) {
        this.mScrollerX.setFriction(friction);
        this.mScrollerY.setFriction(friction);
    }

    public final boolean isFinished() {
        return this.mScrollerX.mFinished && this.mScrollerY.mFinished;
    }

    public final void forceFinished(boolean finished) {
        this.mScrollerX.mFinished = this.mScrollerY.mFinished = finished;
    }

    public final int getCurrX() {
        return this.mScrollerX.mCurrentPosition;
    }

    public final int getCurrY() {
        return this.mScrollerY.mCurrentPosition;
    }

    public float getCurrVelocity() {
        return (float) Math.hypot(this.mScrollerX.mCurrVelocity, this.mScrollerY.mCurrVelocity);
    }

    public final int getStartX() {
        return this.mScrollerX.mStart;
    }

    public final int getStartY() {
        return this.mScrollerY.mStart;
    }

    public final int getFinalX() {
        return this.mScrollerX.mFinal;
    }

    public final int getFinalY() {
        return this.mScrollerY.mFinal;
    }

    @Deprecated
    public final synchronized int getDuration() {
        return Math.max(this.mScrollerX.mDuration, this.mScrollerY.mDuration);
    }

    @Deprecated
    private protected void extendDuration(int extend) {
        this.mScrollerX.extendDuration(extend);
        this.mScrollerY.extendDuration(extend);
    }

    @Deprecated
    public synchronized void setFinalX(int newX) {
        this.mScrollerX.setFinalPosition(newX);
    }

    @Deprecated
    public synchronized void setFinalY(int newY) {
        this.mScrollerY.setFinalPosition(newY);
    }

    public boolean computeScrollOffset() {
        if (isFinished()) {
            return false;
        }
        switch (this.mMode) {
            case 0:
                long time = AnimationUtils.currentAnimationTimeMillis();
                long elapsedTime = time - this.mScrollerX.mStartTime;
                int duration = this.mScrollerX.mDuration;
                if (elapsedTime < duration) {
                    float q = this.mInterpolator.getInterpolation(((float) elapsedTime) / duration);
                    this.mScrollerX.updateScroll(q);
                    this.mScrollerY.updateScroll(q);
                    return true;
                }
                abortAnimation();
                return true;
            case 1:
                if (!this.mScrollerX.mFinished && !this.mScrollerX.update() && !this.mScrollerX.continueWhenFinished()) {
                    this.mScrollerX.finish();
                }
                if (!this.mScrollerY.mFinished && !this.mScrollerY.update() && !this.mScrollerY.continueWhenFinished()) {
                    this.mScrollerY.finish();
                    return true;
                }
                return true;
            default:
                return true;
        }
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, 250);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mMode = 0;
        this.mScrollerX.startScroll(startX, dx, duration);
        this.mScrollerY.startScroll(startY, dy, duration);
    }

    public boolean springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) {
        this.mMode = 1;
        boolean spingbackX = this.mScrollerX.springback(startX, minX, maxX);
        boolean spingbackY = this.mScrollerY.springback(startY, minY, maxY);
        return spingbackX || spingbackY;
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        int velocityX2 = velocityX;
        int i = velocityY;
        if (this.mFlywheel && !isFinished()) {
            float oldVelocityX = this.mScrollerX.mCurrVelocity;
            float oldVelocityY = this.mScrollerY.mCurrVelocity;
            if (Math.signum(velocityX2) == Math.signum(oldVelocityX) && Math.signum(i) == Math.signum(oldVelocityY)) {
                velocityX2 = (int) (velocityX2 + oldVelocityX);
                i = (int) (i + oldVelocityY);
            }
        }
        int velocityY2 = i;
        this.mMode = 1;
        this.mScrollerX.fling(startX, velocityX2, minX, maxX, overX);
        this.mScrollerY.fling(startY, velocityY2, minY, maxY, overY);
    }

    public void notifyHorizontalEdgeReached(int startX, int finalX, int overX) {
        this.mScrollerX.notifyEdgeReached(startX, finalX, overX);
    }

    public void notifyVerticalEdgeReached(int startY, int finalY, int overY) {
        this.mScrollerY.notifyEdgeReached(startY, finalY, overY);
    }

    public boolean isOverScrolled() {
        return ((this.mScrollerX.mFinished || this.mScrollerX.mState == 0) && (this.mScrollerY.mFinished || this.mScrollerY.mState == 0)) ? false : true;
    }

    public void abortAnimation() {
        this.mScrollerX.finish();
        this.mScrollerY.finish();
    }

    public synchronized int timePassed() {
        long time = AnimationUtils.currentAnimationTimeMillis();
        long startTime = Math.min(this.mScrollerX.mStartTime, this.mScrollerY.mStartTime);
        return (int) (time - startTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isScrollingInDirection(float xvel, float yvel) {
        int dx = this.mScrollerX.mFinal - this.mScrollerX.mStart;
        int dy = this.mScrollerY.mFinal - this.mScrollerY.mStart;
        return !isFinished() && Math.signum(xvel) == Math.signum((float) dx) && Math.signum(yvel) == Math.signum((float) dy);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class SplineOverScroller {
        private static final int BALLISTIC = 2;
        private static final int CUBIC = 1;
        private static final float END_TENSION = 1.0f;
        private static final float GRAVITY = 2000.0f;
        private static final float INFLEXION = 0.35f;
        private static final int NB_SAMPLES = 100;
        private static final float P1 = 0.175f;
        private static final float P2 = 0.35000002f;
        private static final int SPLINE = 0;
        private static final float START_TENSION = 0.5f;
        public protected float mCurrVelocity;
        private int mCurrentPosition;
        private float mDeceleration;
        private int mDuration;
        private int mFinal;
        private int mOver;
        private float mPhysicalCoeff;
        private int mSplineDistance;
        private int mSplineDuration;
        private int mStart;
        private long mStartTime;
        private int mVelocity;
        private static float DECELERATION_RATE = (float) (Math.log(0.78d) / Math.log(0.9d));
        private static final float[] SPLINE_POSITION = new float[101];
        private static final float[] SPLINE_TIME = new float[101];
        private float mFlingFriction = ViewConfiguration.getScrollFriction();
        private int mState = 0;
        private boolean mFinished = true;

        static {
            float f;
            float x;
            float f2;
            float coef;
            float y;
            float coef2;
            float y_min;
            float x_min = 0.0f;
            float y_min2 = 0.0f;
            int i = 0;
            while (i < 100) {
                float alpha = i / 100.0f;
                float x_min2 = x_min;
                float x_min3 = 1.0f;
                while (true) {
                    f = 2.0f;
                    x = ((x_min3 - x_min2) / 2.0f) + x_min2;
                    f2 = 3.0f;
                    coef = 3.0f * x * (1.0f - x);
                    float tx = ((((1.0f - x) * P1) + (x * P2)) * coef) + (x * x * x);
                    if (Math.abs(tx - alpha) < 1.0E-5d) {
                        break;
                    } else if (tx > alpha) {
                        x_min3 = x;
                    } else {
                        x_min2 = x;
                    }
                }
                SPLINE_POSITION[i] = ((((1.0f - x) * 0.5f) + x) * coef) + (x * x * x);
                float y_min3 = y_min2;
                float y_max = 1.0f;
                while (true) {
                    y = ((y_max - y_min3) / f) + y_min3;
                    coef2 = f2 * y * (1.0f - y);
                    float dy = ((((1.0f - y) * 0.5f) + y) * coef2) + (y * y * y);
                    y_min = y_min3;
                    if (Math.abs(dy - alpha) < 1.0E-5d) {
                        break;
                    }
                    if (dy > alpha) {
                        y_max = y;
                        y_min3 = y_min;
                    } else {
                        y_min3 = y;
                    }
                    f = 2.0f;
                    f2 = 3.0f;
                }
                SPLINE_TIME[i] = ((((1.0f - y) * P1) + (P2 * y)) * coef2) + (y * y * y);
                i++;
                x_min = x_min2;
                y_min2 = y_min;
            }
            float[] fArr = SPLINE_POSITION;
            SPLINE_TIME[100] = 1.0f;
            fArr[100] = 1.0f;
        }

        synchronized void setFriction(float friction) {
            this.mFlingFriction = friction;
        }

        synchronized SplineOverScroller(Context context) {
            float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
            this.mPhysicalCoeff = 386.0878f * ppi * 0.84f;
        }

        synchronized void updateScroll(float q) {
            this.mCurrentPosition = this.mStart + Math.round((this.mFinal - this.mStart) * q);
        }

        private static synchronized float getDeceleration(int velocity) {
            if (velocity > 0) {
                return -2000.0f;
            }
            return GRAVITY;
        }

        private synchronized void adjustDuration(int start, int oldFinal, int newFinal) {
            int oldDistance = oldFinal - start;
            int newDistance = newFinal - start;
            float x = Math.abs(newDistance / oldDistance);
            int index = (int) (100.0f * x);
            if (index < 100) {
                float x_inf = index / 100.0f;
                float x_sup = (index + 1) / 100.0f;
                float t_inf = SPLINE_TIME[index];
                float t_sup = SPLINE_TIME[index + 1];
                float timeCoef = (((x - x_inf) / (x_sup - x_inf)) * (t_sup - t_inf)) + t_inf;
                this.mDuration = (int) (this.mDuration * timeCoef);
            }
        }

        synchronized void startScroll(int start, int distance, int duration) {
            this.mFinished = false;
            this.mStart = start;
            this.mCurrentPosition = start;
            this.mFinal = start + distance;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = duration;
            this.mDeceleration = 0.0f;
            this.mVelocity = 0;
        }

        synchronized void finish() {
            this.mCurrentPosition = this.mFinal;
            this.mFinished = true;
        }

        synchronized void setFinalPosition(int position) {
            this.mFinal = position;
            this.mFinished = false;
        }

        synchronized void extendDuration(int extend) {
            long time = AnimationUtils.currentAnimationTimeMillis();
            int elapsedTime = (int) (time - this.mStartTime);
            this.mDuration = elapsedTime + extend;
            this.mFinished = false;
        }

        synchronized boolean springback(int start, int min, int max) {
            this.mFinished = true;
            this.mFinal = start;
            this.mStart = start;
            this.mCurrentPosition = start;
            this.mVelocity = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = 0;
            if (start < min) {
                startSpringback(start, min, 0);
            } else if (start > max) {
                startSpringback(start, max, 0);
            }
            return true ^ this.mFinished;
        }

        private synchronized void startSpringback(int start, int end, int velocity) {
            this.mFinished = false;
            this.mState = 1;
            this.mStart = start;
            this.mCurrentPosition = start;
            this.mFinal = end;
            int delta = start - end;
            this.mDeceleration = getDeceleration(delta);
            this.mVelocity = -delta;
            this.mOver = Math.abs(delta);
            this.mDuration = (int) (1000.0d * Math.sqrt(((-2.0d) * delta) / this.mDeceleration));
        }

        synchronized void fling(int start, int velocity, int min, int max, int over) {
            this.mOver = over;
            this.mFinished = false;
            this.mVelocity = velocity;
            this.mCurrVelocity = velocity;
            this.mSplineDuration = 0;
            this.mDuration = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStart = start;
            this.mCurrentPosition = start;
            if (start > max || start < min) {
                startAfterEdge(start, min, max, velocity);
                return;
            }
            this.mState = 0;
            double totalDistance = FeatureOption.FO_BOOT_POLICY_CPU;
            if (velocity != 0) {
                int splineFlingDuration = getSplineFlingDuration(velocity);
                this.mSplineDuration = splineFlingDuration;
                this.mDuration = splineFlingDuration;
                totalDistance = getSplineFlingDistance(velocity);
            }
            this.mSplineDistance = (int) (Math.signum(velocity) * totalDistance);
            this.mFinal = this.mSplineDistance + start;
            if (this.mFinal < min) {
                adjustDuration(this.mStart, this.mFinal, min);
                this.mFinal = min;
            }
            if (this.mFinal > max) {
                adjustDuration(this.mStart, this.mFinal, max);
                this.mFinal = max;
            }
        }

        private synchronized double getSplineDeceleration(int velocity) {
            return Math.log((0.35f * Math.abs(velocity)) / (this.mFlingFriction * this.mPhysicalCoeff));
        }

        private synchronized double getSplineFlingDistance(int velocity) {
            double l = getSplineDeceleration(velocity);
            double decelMinusOne = DECELERATION_RATE - 1.0d;
            return this.mFlingFriction * this.mPhysicalCoeff * Math.exp((DECELERATION_RATE / decelMinusOne) * l);
        }

        private synchronized int getSplineFlingDuration(int velocity) {
            double l = getSplineDeceleration(velocity);
            double decelMinusOne = DECELERATION_RATE - 1.0d;
            return (int) (1000.0d * Math.exp(l / decelMinusOne));
        }

        private synchronized void fitOnBounceCurve(int start, int end, int velocity) {
            float durationToApex = (-velocity) / this.mDeceleration;
            float velocitySquared = velocity * velocity;
            float distanceToApex = (velocitySquared / 2.0f) / Math.abs(this.mDeceleration);
            float distanceToEdge = Math.abs(end - start);
            float totalDuration = (float) Math.sqrt((2.0d * (distanceToApex + distanceToEdge)) / Math.abs(this.mDeceleration));
            this.mStartTime -= (int) (1000.0f * (totalDuration - durationToApex));
            this.mStart = end;
            this.mCurrentPosition = end;
            this.mVelocity = (int) ((-this.mDeceleration) * totalDuration);
        }

        private synchronized void startBounceAfterEdge(int start, int end, int velocity) {
            this.mDeceleration = getDeceleration(velocity == 0 ? start - end : velocity);
            fitOnBounceCurve(start, end, velocity);
            onEdgeReached();
        }

        private synchronized void startAfterEdge(int start, int min, int max, int velocity) {
            if (start > min && start < max) {
                Log.e("OverScroller", "startAfterEdge called from a valid position");
                this.mFinished = true;
                return;
            }
            boolean positive = start > max;
            int edge = positive ? max : min;
            int overDistance = start - edge;
            boolean keepIncreasing = overDistance * velocity >= 0;
            if (keepIncreasing) {
                startBounceAfterEdge(start, edge, velocity);
                return;
            }
            double totalDistance = getSplineFlingDistance(velocity);
            if (totalDistance > Math.abs(overDistance)) {
                fling(start, velocity, positive ? min : start, positive ? start : max, this.mOver);
            } else {
                startSpringback(start, edge, velocity);
            }
        }

        synchronized void notifyEdgeReached(int start, int end, int over) {
            if (this.mState == 0) {
                this.mOver = over;
                this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                startAfterEdge(start, end, end, (int) this.mCurrVelocity);
            }
        }

        private synchronized void onEdgeReached() {
            float velocitySquared = this.mVelocity * this.mVelocity;
            float distance = velocitySquared / (Math.abs(this.mDeceleration) * 2.0f);
            float sign = Math.signum(this.mVelocity);
            if (distance > this.mOver) {
                this.mDeceleration = ((-sign) * velocitySquared) / (2.0f * this.mOver);
                distance = this.mOver;
            }
            this.mOver = (int) distance;
            this.mState = 2;
            this.mFinal = this.mStart + ((int) (this.mVelocity > 0 ? distance : -distance));
            this.mDuration = -((int) ((1000.0f * this.mVelocity) / this.mDeceleration));
        }

        synchronized boolean continueWhenFinished() {
            switch (this.mState) {
                case 0:
                    if (this.mDuration < this.mSplineDuration) {
                        int i = this.mFinal;
                        this.mStart = i;
                        this.mCurrentPosition = i;
                        this.mVelocity = (int) this.mCurrVelocity;
                        this.mDeceleration = getDeceleration(this.mVelocity);
                        this.mStartTime += this.mDuration;
                        onEdgeReached();
                        break;
                    } else {
                        return false;
                    }
                case 1:
                    return false;
                case 2:
                    this.mStartTime += this.mDuration;
                    startSpringback(this.mFinal, this.mStart, 0);
                    break;
            }
            update();
            return true;
        }

        synchronized boolean update() {
            long time = AnimationUtils.currentAnimationTimeMillis();
            long currentTime = time - this.mStartTime;
            if (currentTime == 0) {
                return this.mDuration > 0;
            } else if (currentTime > this.mDuration) {
                return false;
            } else {
                double distance = FeatureOption.FO_BOOT_POLICY_CPU;
                switch (this.mState) {
                    case 0:
                        float t = ((float) currentTime) / this.mSplineDuration;
                        int index = (int) (100.0f * t);
                        float distanceCoef = 1.0f;
                        float velocityCoef = 0.0f;
                        if (index < 100) {
                            float t_inf = index / 100.0f;
                            float t_sup = (index + 1) / 100.0f;
                            float d_inf = SPLINE_POSITION[index];
                            float d_sup = SPLINE_POSITION[index + 1];
                            velocityCoef = (d_sup - d_inf) / (t_sup - t_inf);
                            distanceCoef = d_inf + ((t - t_inf) * velocityCoef);
                        }
                        distance = this.mSplineDistance * distanceCoef;
                        this.mCurrVelocity = ((this.mSplineDistance * velocityCoef) / this.mSplineDuration) * 1000.0f;
                        break;
                    case 1:
                        float t2 = ((float) currentTime) / this.mDuration;
                        float t22 = t2 * t2;
                        float sign = Math.signum(this.mVelocity);
                        distance = this.mOver * sign * ((3.0f * t22) - ((2.0f * t2) * t22));
                        this.mCurrVelocity = this.mOver * sign * 6.0f * ((-t2) + t22);
                        break;
                    case 2:
                        float t3 = ((float) currentTime) / 1000.0f;
                        this.mCurrVelocity = this.mVelocity + (this.mDeceleration * t3);
                        distance = (this.mVelocity * t3) + (((this.mDeceleration * t3) * t3) / 2.0f);
                        break;
                }
                this.mCurrentPosition = this.mStart + ((int) Math.round(distance));
                return true;
            }
        }
    }
}
