package com.android.internal.widget;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ReceiverCallNotAllowedException;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import com.android.internal.R;
import com.android.internal.widget.SwipeDismissLayout;
/* loaded from: classes3.dex */
public class SwipeDismissLayout extends FrameLayout {
    private static final float MAX_DIST_THRESHOLD = 0.33f;
    private static final float MIN_DIST_THRESHOLD = 0.1f;
    private static final String TAG = "SwipeDismissLayout";
    private int mActiveTouchId;
    private boolean mActivityTranslucencyConverted;
    private boolean mBlockGesture;
    private boolean mDiscardIntercept;
    private final DismissAnimator mDismissAnimator;
    private boolean mDismissable;
    private boolean mDismissed;
    private OnDismissedListener mDismissedListener;
    private float mDownX;
    private float mDownY;
    private boolean mIsWindowNativelyTranslucent;
    private float mLastX;
    private int mMinFlingVelocity;
    private OnSwipeProgressChangedListener mProgressListener;
    private IntentFilter mScreenOffFilter;
    private BroadcastReceiver mScreenOffReceiver;
    private int mSlop;
    private boolean mSwiping;
    private VelocityTracker mVelocityTracker;

    /* loaded from: classes3.dex */
    public interface OnDismissedListener {
        void onDismissed(SwipeDismissLayout swipeDismissLayout);
    }

    /* loaded from: classes3.dex */
    public interface OnSwipeProgressChangedListener {
        void onSwipeCancelled(SwipeDismissLayout swipeDismissLayout);

        void onSwipeProgressChanged(SwipeDismissLayout swipeDismissLayout, float f, float f2);
    }

    public SwipeDismissLayout(Context context) {
        super(context);
        this.mBlockGesture = false;
        this.mActivityTranslucencyConverted = false;
        this.mDismissAnimator = new DismissAnimator();
        this.mScreenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        this.mDismissable = true;
        init(context);
    }

    public SwipeDismissLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mBlockGesture = false;
        this.mActivityTranslucencyConverted = false;
        this.mDismissAnimator = new DismissAnimator();
        this.mScreenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        this.mDismissable = true;
        init(context);
    }

    public SwipeDismissLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mBlockGesture = false;
        this.mActivityTranslucencyConverted = false;
        this.mDismissAnimator = new DismissAnimator();
        this.mScreenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        this.mDismissable = true;
        init(context);
    }

    private void init(Context context) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        this.mSlop = vc.getScaledTouchSlop();
        this.mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        TypedArray a = context.getTheme().obtainStyledAttributes(R.styleable.Theme);
        this.mIsWindowNativelyTranslucent = a.getBoolean(5, false);
        a.recycle();
    }

    public void setOnDismissedListener(OnDismissedListener listener) {
        this.mDismissedListener = listener;
    }

    public void setOnSwipeProgressChangedListener(OnSwipeProgressChangedListener listener) {
        this.mProgressListener = listener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            this.mScreenOffReceiver = new AnonymousClass1();
            getContext().registerReceiver(this.mScreenOffReceiver, this.mScreenOffFilter);
        } catch (ReceiverCallNotAllowedException e) {
            this.mScreenOffReceiver = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.internal.widget.SwipeDismissLayout$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            SwipeDismissLayout.this.post(new Runnable() { // from class: com.android.internal.widget.-$$Lambda$SwipeDismissLayout$1$NDXsqpv65OVP2OutTHt-hDxJywg
                @Override // java.lang.Runnable
                public final void run() {
                    SwipeDismissLayout.AnonymousClass1.lambda$onReceive$0(SwipeDismissLayout.AnonymousClass1.this);
                }
            });
        }

        public static /* synthetic */ void lambda$onReceive$0(AnonymousClass1 anonymousClass1) {
            if (SwipeDismissLayout.this.mDismissed) {
                SwipeDismissLayout.this.dismiss();
            } else {
                SwipeDismissLayout.this.cancel();
            }
            SwipeDismissLayout.this.resetMembers();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        if (this.mScreenOffReceiver != null) {
            getContext().unregisterReceiver(this.mScreenOffReceiver);
            this.mScreenOffReceiver = null;
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        checkGesture(ev);
        if (this.mBlockGesture) {
            return true;
        }
        if (!this.mDismissable) {
            return super.onInterceptTouchEvent(ev);
        }
        ev.offsetLocation(ev.getRawX() - ev.getX(), 0.0f);
        switch (ev.getActionMasked()) {
            case 0:
                resetMembers();
                this.mDownX = ev.getRawX();
                this.mDownY = ev.getRawY();
                this.mActiveTouchId = ev.getPointerId(0);
                this.mVelocityTracker = VelocityTracker.obtain("int1");
                this.mVelocityTracker.addMovement(ev);
                break;
            case 1:
            case 3:
                resetMembers();
                break;
            case 2:
                if (this.mVelocityTracker != null && !this.mDiscardIntercept) {
                    int pointerIndex = ev.findPointerIndex(this.mActiveTouchId);
                    if (pointerIndex == -1) {
                        Log.e(TAG, "Invalid pointer index: ignoring.");
                        this.mDiscardIntercept = true;
                        break;
                    } else {
                        float dx = ev.getRawX() - this.mDownX;
                        float x = ev.getX(pointerIndex);
                        float y = ev.getY(pointerIndex);
                        if (dx != 0.0f && canScroll(this, false, dx, x, y)) {
                            this.mDiscardIntercept = true;
                            break;
                        } else {
                            updateSwiping(ev);
                            break;
                        }
                    }
                }
                break;
            case 5:
                this.mActiveTouchId = ev.getPointerId(ev.getActionIndex());
                break;
            case 6:
                int actionIndex = ev.getActionIndex();
                int pointerId = ev.getPointerId(actionIndex);
                if (pointerId == this.mActiveTouchId) {
                    int newActionIndex = actionIndex == 0 ? 1 : 0;
                    this.mActiveTouchId = ev.getPointerId(newActionIndex);
                    break;
                }
                break;
        }
        return !this.mDiscardIntercept && this.mSwiping;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        checkGesture(ev);
        if (this.mBlockGesture) {
            return true;
        }
        if (this.mVelocityTracker == null || !this.mDismissable) {
            return super.onTouchEvent(ev);
        }
        ev.offsetLocation(ev.getRawX() - ev.getX(), 0.0f);
        switch (ev.getActionMasked()) {
            case 1:
                updateDismiss(ev);
                if (this.mDismissed) {
                    this.mDismissAnimator.animateDismissal(ev.getRawX() - this.mDownX);
                } else if (this.mSwiping && this.mLastX != -2.1474836E9f) {
                    this.mDismissAnimator.animateRecovery(ev.getRawX() - this.mDownX);
                }
                resetMembers();
                break;
            case 2:
                this.mVelocityTracker.addMovement(ev);
                this.mLastX = ev.getRawX();
                updateSwiping(ev);
                if (this.mSwiping) {
                    setProgress(ev.getRawX() - this.mDownX);
                    break;
                }
                break;
            case 3:
                cancel();
                resetMembers();
                break;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setProgress(float deltaX) {
        if (this.mProgressListener != null && deltaX >= 0.0f) {
            this.mProgressListener.onSwipeProgressChanged(this, progressToAlpha(deltaX / getWidth()), deltaX);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismiss() {
        if (this.mDismissedListener != null) {
            this.mDismissedListener.onDismissed(this);
        }
    }

    protected void cancel() {
        Activity activity;
        if (!this.mIsWindowNativelyTranslucent && (activity = findActivity()) != null && this.mActivityTranslucencyConverted) {
            activity.convertFromTranslucent();
            this.mActivityTranslucencyConverted = false;
        }
        if (this.mProgressListener != null) {
            this.mProgressListener.onSwipeCancelled(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetMembers() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
        }
        this.mVelocityTracker = null;
        this.mDownX = 0.0f;
        this.mLastX = -2.1474836E9f;
        this.mDownY = 0.0f;
        this.mSwiping = false;
        this.mDismissed = false;
        this.mDiscardIntercept = false;
    }

    private void updateSwiping(MotionEvent ev) {
        Activity activity;
        boolean oldSwiping = this.mSwiping;
        if (!this.mSwiping) {
            float deltaX = ev.getRawX() - this.mDownX;
            float deltaY = ev.getRawY() - this.mDownY;
            boolean z = false;
            if ((deltaX * deltaX) + (deltaY * deltaY) > this.mSlop * this.mSlop) {
                if (deltaX > this.mSlop * 2 && Math.abs(deltaY) < Math.abs(deltaX)) {
                    z = true;
                }
                this.mSwiping = z;
            } else {
                this.mSwiping = false;
            }
        }
        if (this.mSwiping && !oldSwiping && !this.mIsWindowNativelyTranslucent && (activity = findActivity()) != null) {
            this.mActivityTranslucencyConverted = activity.convertToTranslucent(null, null);
        }
    }

    private void updateDismiss(MotionEvent ev) {
        float deltaX = ev.getRawX() - this.mDownX;
        this.mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = this.mVelocityTracker.getXVelocity();
        if (this.mLastX == -2.1474836E9f) {
            xVelocity = deltaX / ((float) ((ev.getEventTime() - ev.getDownTime()) / 1000));
        }
        if (!this.mDismissed) {
            float distanceThreshold = getWidth() * Math.max(Math.min((((-0.23000002f) * xVelocity) / this.mMinFlingVelocity) + MAX_DIST_THRESHOLD, (float) MAX_DIST_THRESHOLD), (float) MIN_DIST_THRESHOLD);
            if ((deltaX > distanceThreshold && ev.getRawX() >= this.mLastX) || xVelocity >= this.mMinFlingVelocity) {
                this.mDismissed = true;
            }
        }
        if (this.mDismissed && this.mSwiping && xVelocity < (-this.mMinFlingVelocity)) {
            this.mDismissed = false;
        }
    }

    protected boolean canScroll(View v, boolean checkV, float dx, float x, float y) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            int count = group.getChildCount();
            for (int i = count - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom() && canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
                    return true;
                }
            }
        }
        return checkV && v.canScrollHorizontally((int) (-dx));
    }

    public void setDismissable(boolean dismissable) {
        if (!dismissable && this.mDismissable) {
            cancel();
            resetMembers();
        }
        this.mDismissable = dismissable;
    }

    private void checkGesture(MotionEvent ev) {
        if (ev.getActionMasked() == 0) {
            this.mBlockGesture = this.mDismissAnimator.isAnimating();
        }
    }

    private float progressToAlpha(float progress) {
        return 1.0f - ((progress * progress) * progress);
    }

    private Activity findActivity() {
        for (Context context = getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class DismissAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        private final TimeInterpolator DISMISS_INTERPOLATOR = new DecelerateInterpolator(1.5f);
        private final long DISMISS_DURATION = 250;
        private final ValueAnimator mDismissAnimator = new ValueAnimator();
        private boolean mWasCanceled = false;
        private boolean mDismissOnComplete = false;

        DismissAnimator() {
            this.mDismissAnimator.addUpdateListener(this);
            this.mDismissAnimator.addListener(this);
        }

        void animateDismissal(float currentTranslation) {
            animate(currentTranslation / SwipeDismissLayout.this.getWidth(), 1.0f, 250L, this.DISMISS_INTERPOLATOR, true);
        }

        void animateRecovery(float currentTranslation) {
            animate(currentTranslation / SwipeDismissLayout.this.getWidth(), 0.0f, 250L, this.DISMISS_INTERPOLATOR, false);
        }

        boolean isAnimating() {
            return this.mDismissAnimator.isStarted();
        }

        private void animate(float from, float to, long duration, TimeInterpolator interpolator, boolean dismissOnComplete) {
            this.mDismissAnimator.cancel();
            this.mDismissOnComplete = dismissOnComplete;
            this.mDismissAnimator.setFloatValues(from, to);
            this.mDismissAnimator.setDuration(duration);
            this.mDismissAnimator.setInterpolator(interpolator);
            this.mDismissAnimator.start();
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = ((Float) animation.getAnimatedValue()).floatValue();
            SwipeDismissLayout.this.setProgress(SwipeDismissLayout.this.getWidth() * value);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animation) {
            this.mWasCanceled = false;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animation) {
            this.mWasCanceled = true;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animation) {
            if (!this.mWasCanceled) {
                if (this.mDismissOnComplete) {
                    SwipeDismissLayout.this.dismiss();
                } else {
                    SwipeDismissLayout.this.cancel();
                }
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
