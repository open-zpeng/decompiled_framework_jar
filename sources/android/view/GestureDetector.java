package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.StatsLog;

/* loaded from: classes3.dex */
public class GestureDetector {
    private static final int LONG_PRESS = 2;
    private static final int SHOW_PRESS = 1;
    private static final int TAP = 3;
    private boolean mAlwaysInBiggerTapRegion;
    @UnsupportedAppUsage
    private boolean mAlwaysInTapRegion;
    private OnContextClickListener mContextClickListener;
    private MotionEvent mCurrentDownEvent;
    private MotionEvent mCurrentMotionEvent;
    private boolean mDeferConfirmSingleTap;
    private OnDoubleTapListener mDoubleTapListener;
    private int mDoubleTapSlopSquare;
    private int mDoubleTapTouchSlopSquare;
    private float mDownFocusX;
    private float mDownFocusY;
    private final Handler mHandler;
    private boolean mHasRecordedClassification;
    private boolean mIgnoreNextUpEvent;
    private boolean mInContextClick;
    private boolean mInLongPress;
    private final InputEventConsistencyVerifier mInputEventConsistencyVerifier;
    private boolean mIsDoubleTapping;
    private boolean mIsLongpressEnabled;
    private float mLastFocusX;
    private float mLastFocusY;
    @UnsupportedAppUsage
    private final OnGestureListener mListener;
    private int mMaximumFlingVelocity;
    @UnsupportedAppUsage
    private int mMinimumFlingVelocity;
    private MotionEvent mPreviousUpEvent;
    private boolean mStillDown;
    @UnsupportedAppUsage
    private int mTouchSlopSquare;
    private VelocityTracker mVelocityTracker;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    private static final int DOUBLE_TAP_MIN_TIME = ViewConfiguration.getDoubleTapMinTime();

    /* loaded from: classes3.dex */
    public interface OnContextClickListener {
        boolean onContextClick(MotionEvent motionEvent);
    }

    /* loaded from: classes3.dex */
    public interface OnDoubleTapListener {
        boolean onDoubleTap(MotionEvent motionEvent);

        boolean onDoubleTapEvent(MotionEvent motionEvent);

        boolean onSingleTapConfirmed(MotionEvent motionEvent);
    }

    /* loaded from: classes3.dex */
    public interface OnGestureListener {
        boolean onDown(MotionEvent motionEvent);

        boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onLongPress(MotionEvent motionEvent);

        boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onShowPress(MotionEvent motionEvent);

        boolean onSingleTapUp(MotionEvent motionEvent);
    }

    /* loaded from: classes3.dex */
    public static class SimpleOnGestureListener implements OnGestureListener, OnDoubleTapListener, OnContextClickListener {
        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent e) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent e) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override // android.view.GestureDetector.OnContextClickListener
        public boolean onContextClick(MotionEvent e) {
            return false;
        }
    }

    /* loaded from: classes3.dex */
    private class GestureHandler extends Handler {
        GestureHandler() {
        }

        GestureHandler(Handler handler) {
            super(handler.getLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                GestureDetector.this.mListener.onShowPress(GestureDetector.this.mCurrentDownEvent);
            } else if (i == 2) {
                GestureDetector.this.recordGestureClassification(msg.arg1);
                GestureDetector.this.dispatchLongPress();
            } else if (i == 3) {
                if (GestureDetector.this.mDoubleTapListener != null) {
                    if (!GestureDetector.this.mStillDown) {
                        GestureDetector.this.recordGestureClassification(1);
                        GestureDetector.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetector.this.mCurrentDownEvent);
                        return;
                    }
                    GestureDetector.this.mDeferConfirmSingleTap = true;
                }
            } else {
                throw new RuntimeException("Unknown message " + msg);
            }
        }
    }

    @Deprecated
    public GestureDetector(OnGestureListener listener, Handler handler) {
        this(null, listener, handler);
    }

    @Deprecated
    public GestureDetector(OnGestureListener listener) {
        this(null, listener, null);
    }

    public GestureDetector(Context context, OnGestureListener listener) {
        this(context, listener, null);
    }

    public GestureDetector(Context context, OnGestureListener listener, Handler handler) {
        this.mInputEventConsistencyVerifier = InputEventConsistencyVerifier.isInstrumentationEnabled() ? new InputEventConsistencyVerifier(this, 0) : null;
        if (handler != null) {
            this.mHandler = new GestureHandler(handler);
        } else {
            this.mHandler = new GestureHandler();
        }
        this.mListener = listener;
        if (listener instanceof OnDoubleTapListener) {
            setOnDoubleTapListener((OnDoubleTapListener) listener);
        }
        if (listener instanceof OnContextClickListener) {
            setContextClickListener((OnContextClickListener) listener);
        }
        init(context);
    }

    public GestureDetector(Context context, OnGestureListener listener, Handler handler, boolean unused) {
        this(context, listener, handler);
    }

    private void init(Context context) {
        int touchSlop;
        int touchSlop2;
        int doubleTapTouchSlop;
        if (this.mListener == null) {
            throw new NullPointerException("OnGestureListener must not be null");
        }
        this.mIsLongpressEnabled = true;
        if (context == null) {
            touchSlop = ViewConfiguration.getTouchSlop();
            touchSlop2 = touchSlop;
            doubleTapTouchSlop = ViewConfiguration.getDoubleTapSlop();
            this.mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            this.mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
        } else {
            ViewConfiguration configuration = ViewConfiguration.get(context);
            int touchSlop3 = configuration.getScaledTouchSlop();
            int doubleTapTouchSlop2 = configuration.getScaledDoubleTapTouchSlop();
            int doubleTapSlop = configuration.getScaledDoubleTapSlop();
            this.mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
            this.mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
            touchSlop = touchSlop3;
            touchSlop2 = doubleTapTouchSlop2;
            doubleTapTouchSlop = doubleTapSlop;
        }
        int doubleTapSlop2 = touchSlop * touchSlop;
        this.mTouchSlopSquare = doubleTapSlop2;
        this.mDoubleTapTouchSlopSquare = touchSlop2 * touchSlop2;
        this.mDoubleTapSlopSquare = doubleTapTouchSlop * doubleTapTouchSlop;
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.mDoubleTapListener = onDoubleTapListener;
    }

    public void setContextClickListener(OnContextClickListener onContextClickListener) {
        this.mContextClickListener = onContextClickListener;
    }

    public void setIsLongpressEnabled(boolean isLongpressEnabled) {
        this.mIsLongpressEnabled = isLongpressEnabled;
    }

    public boolean isLongpressEnabled() {
        return this.mIsLongpressEnabled;
    }

    /* JADX WARN: Removed duplicated region for block: B:158:0x03df  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x03fa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r40) {
        /*
            Method dump skipped, instructions count: 1081
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.GestureDetector.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean onGenericMotionEvent(MotionEvent ev) {
        InputEventConsistencyVerifier inputEventConsistencyVerifier = this.mInputEventConsistencyVerifier;
        if (inputEventConsistencyVerifier != null) {
            inputEventConsistencyVerifier.onGenericMotionEvent(ev, 0);
        }
        int actionButton = ev.getActionButton();
        int actionMasked = ev.getActionMasked();
        if (actionMasked == 11) {
            if (this.mContextClickListener != null && !this.mInContextClick && !this.mInLongPress && ((actionButton == 32 || actionButton == 2) && this.mContextClickListener.onContextClick(ev))) {
                this.mInContextClick = true;
                this.mHandler.removeMessages(2);
                this.mHandler.removeMessages(3);
                return true;
            }
        } else if (actionMasked == 12 && this.mInContextClick && (actionButton == 32 || actionButton == 2)) {
            this.mInContextClick = false;
            this.mIgnoreNextUpEvent = true;
        }
        return false;
    }

    private void cancel() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        this.mIsDoubleTapping = false;
        this.mStillDown = false;
        this.mAlwaysInTapRegion = false;
        this.mAlwaysInBiggerTapRegion = false;
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = false;
        this.mInContextClick = false;
        this.mIgnoreNextUpEvent = false;
    }

    private void cancelTaps() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        this.mIsDoubleTapping = false;
        this.mAlwaysInTapRegion = false;
        this.mAlwaysInBiggerTapRegion = false;
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = false;
        this.mInContextClick = false;
        this.mIgnoreNextUpEvent = false;
    }

    private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp, MotionEvent secondDown) {
        if (this.mAlwaysInBiggerTapRegion) {
            long deltaTime = secondDown.getEventTime() - firstUp.getEventTime();
            if (deltaTime > DOUBLE_TAP_TIMEOUT || deltaTime < DOUBLE_TAP_MIN_TIME) {
                return false;
            }
            int deltaX = ((int) firstDown.getX()) - ((int) secondDown.getX());
            int deltaY = ((int) firstDown.getY()) - ((int) secondDown.getY());
            boolean isGeneratedGesture = (firstDown.getFlags() & 8) != 0;
            int slopSquare = isGeneratedGesture ? 0 : this.mDoubleTapSlopSquare;
            return (deltaX * deltaX) + (deltaY * deltaY) < slopSquare;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchLongPress() {
        this.mHandler.removeMessages(3);
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = true;
        this.mListener.onLongPress(this.mCurrentDownEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordGestureClassification(int classification) {
        if (this.mHasRecordedClassification || classification == 0) {
            return;
        }
        if (this.mCurrentDownEvent == null || this.mCurrentMotionEvent == null) {
            this.mHasRecordedClassification = true;
            return;
        }
        StatsLog.write(177, getClass().getName(), classification, (int) (SystemClock.uptimeMillis() - this.mCurrentMotionEvent.getDownTime()), (float) Math.hypot(this.mCurrentMotionEvent.getRawX() - this.mCurrentDownEvent.getRawX(), this.mCurrentMotionEvent.getRawY() - this.mCurrentDownEvent.getRawY()));
        this.mHasRecordedClassification = true;
    }
}
