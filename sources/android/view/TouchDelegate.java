package android.view;

import android.graphics.Rect;
/* loaded from: classes2.dex */
public class TouchDelegate {
    public static final int ABOVE = 1;
    public static final int BELOW = 2;
    public static final int TO_LEFT = 4;
    public static final int TO_RIGHT = 8;
    private Rect mBounds;
    public protected boolean mDelegateTargeted;
    private View mDelegateView;
    private int mSlop;
    private Rect mSlopBounds;

    public TouchDelegate(Rect bounds, View delegateView) {
        this.mBounds = bounds;
        this.mSlop = ViewConfiguration.get(delegateView.getContext()).getScaledTouchSlop();
        this.mSlopBounds = new Rect(bounds);
        this.mSlopBounds.inset(-this.mSlop, -this.mSlop);
        this.mDelegateView = delegateView;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        boolean sendToDelegate = false;
        boolean hit = true;
        switch (event.getActionMasked()) {
            case 0:
                this.mDelegateTargeted = this.mBounds.contains(x, y);
                sendToDelegate = this.mDelegateTargeted;
                break;
            case 1:
            case 2:
            case 5:
            case 6:
                sendToDelegate = this.mDelegateTargeted;
                if (sendToDelegate) {
                    Rect slopBounds = this.mSlopBounds;
                    if (!slopBounds.contains(x, y)) {
                        hit = false;
                        break;
                    }
                }
                break;
            case 3:
                sendToDelegate = this.mDelegateTargeted;
                this.mDelegateTargeted = false;
                break;
        }
        if (!sendToDelegate) {
            return false;
        }
        View delegateView = this.mDelegateView;
        if (hit) {
            event.setLocation(delegateView.getWidth() / 2, delegateView.getHeight() / 2);
        } else {
            int slop = this.mSlop;
            event.setLocation(-(slop * 2), -(slop * 2));
        }
        boolean handled = delegateView.dispatchTouchEvent(event);
        return handled;
    }
}
