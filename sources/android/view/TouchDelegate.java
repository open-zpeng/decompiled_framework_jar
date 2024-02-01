package android.view;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.ArrayMap;
import android.view.accessibility.AccessibilityNodeInfo;

/* loaded from: classes3.dex */
public class TouchDelegate {
    public static final int ABOVE = 1;
    public static final int BELOW = 2;
    public static final int TO_LEFT = 4;
    public static final int TO_RIGHT = 8;
    private Rect mBounds;
    @UnsupportedAppUsage
    private boolean mDelegateTargeted;
    private View mDelegateView;
    private int mSlop;
    private Rect mSlopBounds;
    private AccessibilityNodeInfo.TouchDelegateInfo mTouchDelegateInfo;

    public TouchDelegate(Rect bounds, View delegateView) {
        this.mBounds = bounds;
        this.mSlop = ViewConfiguration.get(delegateView.getContext()).getScaledTouchSlop();
        this.mSlopBounds = new Rect(bounds);
        Rect rect = this.mSlopBounds;
        int i = this.mSlop;
        rect.inset(-i, -i);
        this.mDelegateView = delegateView;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0020, code lost:
        if (r5 != 6) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r9) {
        /*
            r8 = this;
            float r0 = r9.getX()
            int r0 = (int) r0
            float r1 = r9.getY()
            int r1 = (int) r1
            r2 = 0
            r3 = 1
            r4 = 0
            int r5 = r9.getActionMasked()
            r6 = 2
            if (r5 == 0) goto L37
            r7 = 1
            if (r5 == r7) goto L29
            if (r5 == r6) goto L29
            r7 = 3
            if (r5 == r7) goto L23
            r7 = 5
            if (r5 == r7) goto L29
            r7 = 6
            if (r5 == r7) goto L29
            goto L42
        L23:
            boolean r2 = r8.mDelegateTargeted
            r5 = 0
            r8.mDelegateTargeted = r5
            goto L42
        L29:
            boolean r2 = r8.mDelegateTargeted
            if (r2 == 0) goto L42
            android.graphics.Rect r5 = r8.mSlopBounds
            boolean r7 = r5.contains(r0, r1)
            if (r7 != 0) goto L36
            r3 = 0
        L36:
            goto L42
        L37:
            android.graphics.Rect r5 = r8.mBounds
            boolean r5 = r5.contains(r0, r1)
            r8.mDelegateTargeted = r5
            boolean r2 = r8.mDelegateTargeted
        L42:
            if (r2 == 0) goto L6d
            if (r3 == 0) goto L5a
            android.view.View r5 = r8.mDelegateView
            int r5 = r5.getWidth()
            int r5 = r5 / r6
            float r5 = (float) r5
            android.view.View r7 = r8.mDelegateView
            int r7 = r7.getHeight()
            int r7 = r7 / r6
            float r6 = (float) r7
            r9.setLocation(r5, r6)
            goto L67
        L5a:
            int r5 = r8.mSlop
            int r6 = r5 * 2
            int r6 = -r6
            float r6 = (float) r6
            int r7 = r5 * 2
            int r7 = -r7
            float r7 = (float) r7
            r9.setLocation(r6, r7)
        L67:
            android.view.View r5 = r8.mDelegateView
            boolean r4 = r5.dispatchTouchEvent(r9)
        L6d:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.TouchDelegate.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean onTouchExplorationHoverEvent(MotionEvent event) {
        if (this.mBounds == null) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        boolean hit = true;
        boolean isInbound = this.mBounds.contains(x, y);
        int actionMasked = event.getActionMasked();
        if (actionMasked != 7) {
            if (actionMasked == 9) {
                this.mDelegateTargeted = isInbound;
            } else if (actionMasked == 10) {
                this.mDelegateTargeted = true;
            }
        } else if (isInbound) {
            this.mDelegateTargeted = true;
        } else if (this.mDelegateTargeted && !this.mSlopBounds.contains(x, y)) {
            hit = false;
        }
        if (!this.mDelegateTargeted) {
            return false;
        }
        if (hit) {
            event.setLocation(this.mDelegateView.getWidth() / 2, this.mDelegateView.getHeight() / 2);
        } else {
            this.mDelegateTargeted = false;
        }
        boolean handled = this.mDelegateView.dispatchHoverEvent(event);
        return handled;
    }

    public AccessibilityNodeInfo.TouchDelegateInfo getTouchDelegateInfo() {
        if (this.mTouchDelegateInfo == null) {
            ArrayMap<Region, View> targetMap = new ArrayMap<>(1);
            Rect bounds = this.mBounds;
            if (bounds == null) {
                bounds = new Rect();
            }
            targetMap.put(new Region(bounds), this.mDelegateView);
            this.mTouchDelegateInfo = new AccessibilityNodeInfo.TouchDelegateInfo(targetMap);
        }
        return this.mTouchDelegateInfo;
    }
}
