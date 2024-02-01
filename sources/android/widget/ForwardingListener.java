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

    public abstract ShowableListMenu getPopup();

    public ForwardingListener(View src) {
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
        Runnable runnable = this.mDisallowIntercept;
        if (runnable != null) {
            this.mSrc.removeCallbacks(runnable);
        }
    }

    protected boolean onForwardingStarted() {
        ShowableListMenu popup = getPopup();
        if (popup != null && !popup.isShowing()) {
            popup.show();
            return true;
        }
        return true;
    }

    protected boolean onForwardingStopped() {
        ShowableListMenu popup = getPopup();
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
            return true;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0017, code lost:
        if (r1 != 3) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean onTouchObserved(android.view.MotionEvent r9) {
        /*
            r8 = this;
            android.view.View r0 = r8.mSrc
            boolean r1 = r0.isEnabled()
            r2 = 0
            if (r1 != 0) goto La
            return r2
        La:
            int r1 = r9.getActionMasked()
            if (r1 == 0) goto L42
            r3 = 1
            if (r1 == r3) goto L3e
            r4 = 2
            if (r1 == r4) goto L1a
            r3 = 3
            if (r1 == r3) goto L3e
            goto L70
        L1a:
            int r4 = r8.mActivePointerId
            int r4 = r9.findPointerIndex(r4)
            if (r4 < 0) goto L70
            float r5 = r9.getX(r4)
            float r6 = r9.getY(r4)
            float r7 = r8.mScaledTouchSlop
            boolean r7 = r0.pointInView(r5, r6, r7)
            if (r7 != 0) goto L3d
            r8.clearCallbacks()
            android.view.ViewParent r2 = r0.getParent()
            r2.requestDisallowInterceptTouchEvent(r3)
            return r3
        L3d:
            goto L70
        L3e:
            r8.clearCallbacks()
            goto L70
        L42:
            int r3 = r9.getPointerId(r2)
            r8.mActivePointerId = r3
            java.lang.Runnable r3 = r8.mDisallowIntercept
            r4 = 0
            if (r3 != 0) goto L54
            android.widget.ForwardingListener$DisallowIntercept r3 = new android.widget.ForwardingListener$DisallowIntercept
            r3.<init>()
            r8.mDisallowIntercept = r3
        L54:
            java.lang.Runnable r3 = r8.mDisallowIntercept
            int r5 = r8.mTapTimeout
            long r5 = (long) r5
            r0.postDelayed(r3, r5)
            java.lang.Runnable r3 = r8.mTriggerLongPress
            if (r3 != 0) goto L67
            android.widget.ForwardingListener$TriggerLongPress r3 = new android.widget.ForwardingListener$TriggerLongPress
            r3.<init>()
            r8.mTriggerLongPress = r3
        L67:
            java.lang.Runnable r3 = r8.mTriggerLongPress
            int r4 = r8.mLongPressTimeout
            long r4 = (long) r4
            r0.postDelayed(r3, r4)
        L70:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ForwardingListener.onTouchObserved(android.view.MotionEvent):boolean");
    }

    private void clearCallbacks() {
        Runnable runnable = this.mTriggerLongPress;
        if (runnable != null) {
            this.mSrc.removeCallbacks(runnable);
        }
        Runnable runnable2 = this.mDisallowIntercept;
        if (runnable2 != null) {
            this.mSrc.removeCallbacks(runnable2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLongPress() {
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

    private boolean onTouchForwarded(MotionEvent srcEvent) {
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
