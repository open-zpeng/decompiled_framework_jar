package android.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import com.android.internal.view.menu.ShowableListMenu;
/* loaded from: classes3.dex */
public abstract class ForwardingListener implements View.OnTouchListener, View.OnAttachStateChangeListener {
    private int mActivePointerId;
    private Runnable mDisallowIntercept;
    private boolean mForwarding;
    private final int mLongPressTimeout;
    private final float mScaledTouchSlop;
    private final View mSrc;
    private final int mTapTimeout;
    private Runnable mTriggerLongPress;

    public abstract synchronized ShowableListMenu getPopup();

    public synchronized ForwardingListener(View src) {
        this.mSrc = src;
        src.setLongClickable(true);
        src.addOnAttachStateChangeListener(this);
        this.mScaledTouchSlop = ViewConfiguration.get(src.getContext()).getScaledTouchSlop();
        this.mTapTimeout = ViewConfiguration.getTapTimeout();
        this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        boolean forwarding;
        boolean wasForwarding = this.mForwarding;
        if (!wasForwarding) {
            forwarding = onTouchObserved(event) && onForwardingStarted();
            if (forwarding) {
                long now = SystemClock.uptimeMillis();
                MotionEvent e = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
                this.mSrc.onTouchEvent(e);
                e.recycle();
            }
        } else {
            forwarding = onTouchForwarded(event) || !onForwardingStopped();
        }
        this.mForwarding = forwarding;
        return forwarding || wasForwarding;
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View v) {
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View v) {
        this.mForwarding = false;
        this.mActivePointerId = -1;
        if (this.mDisallowIntercept != null) {
            this.mSrc.removeCallbacks(this.mDisallowIntercept);
        }
    }

    protected synchronized boolean onForwardingStarted() {
        ShowableListMenu popup = getPopup();
        if (popup != null && !popup.isShowing()) {
            popup.show();
            return true;
        }
        return true;
    }

    protected synchronized boolean onForwardingStopped() {
        ShowableListMenu popup = getPopup();
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
            return true;
        }
        return true;
    }

    private synchronized boolean onTouchObserved(MotionEvent srcEvent) {
        View src = this.mSrc;
        if (src.isEnabled()) {
            int actionMasked = srcEvent.getActionMasked();
            switch (actionMasked) {
                case 0:
                    this.mActivePointerId = srcEvent.getPointerId(0);
                    if (this.mDisallowIntercept == null) {
                        this.mDisallowIntercept = new DisallowIntercept();
                    }
                    src.postDelayed(this.mDisallowIntercept, this.mTapTimeout);
                    if (this.mTriggerLongPress == null) {
                        this.mTriggerLongPress = new TriggerLongPress();
                    }
                    src.postDelayed(this.mTriggerLongPress, this.mLongPressTimeout);
                    break;
                case 1:
                case 3:
                    clearCallbacks();
                    break;
                case 2:
                    int activePointerIndex = srcEvent.findPointerIndex(this.mActivePointerId);
                    if (activePointerIndex >= 0) {
                        float x = srcEvent.getX(activePointerIndex);
                        float y = srcEvent.getY(activePointerIndex);
                        if (!src.pointInView(x, y, this.mScaledTouchSlop)) {
                            clearCallbacks();
                            src.getParent().requestDisallowInterceptTouchEvent(true);
                            return true;
                        }
                    }
                    break;
            }
            return false;
        }
        return false;
    }

    private synchronized void clearCallbacks() {
        if (this.mTriggerLongPress != null) {
            this.mSrc.removeCallbacks(this.mTriggerLongPress);
        }
        if (this.mDisallowIntercept != null) {
            this.mSrc.removeCallbacks(this.mDisallowIntercept);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onLongPress() {
        clearCallbacks();
        View src = this.mSrc;
        if (!src.isEnabled() || src.isLongClickable() || !onForwardingStarted()) {
            return;
        }
        src.getParent().requestDisallowInterceptTouchEvent(true);
        long now = SystemClock.uptimeMillis();
        MotionEvent e = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
        src.onTouchEvent(e);
        e.recycle();
        this.mForwarding = true;
    }

    private synchronized boolean onTouchForwarded(MotionEvent srcEvent) {
        DropDownListView dst;
        boolean keepForwarding;
        View src = this.mSrc;
        ShowableListMenu popup = getPopup();
        if (popup == null || !popup.isShowing() || (dst = (DropDownListView) popup.getListView()) == null || !dst.isShown()) {
            return false;
        }
        MotionEvent dstEvent = MotionEvent.obtainNoHistory(srcEvent);
        src.toGlobalMotionEvent(dstEvent);
        dst.toLocalMotionEvent(dstEvent);
        boolean handled = dst.onForwardedEvent(dstEvent, this.mActivePointerId);
        dstEvent.recycle();
        int action = srcEvent.getActionMasked();
        if (action == 1 || action == 3) {
            keepForwarding = false;
        } else {
            keepForwarding = true;
        }
        if (!handled || !keepForwarding) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class DisallowIntercept implements Runnable {
        private DisallowIntercept() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewParent parent = ForwardingListener.this.mSrc.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class TriggerLongPress implements Runnable {
        private TriggerLongPress() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ForwardingListener.this.onLongPress();
        }
    }
}
