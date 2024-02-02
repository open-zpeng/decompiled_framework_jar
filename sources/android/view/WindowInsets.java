package android.view;

import android.graphics.Rect;
import com.android.internal.util.Preconditions;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class WindowInsets {
    private boolean mAlwaysConsumeNavBar;
    private DisplayCutout mDisplayCutout;
    private boolean mDisplayCutoutConsumed;
    private boolean mIsRound;
    private Rect mStableInsets;
    private boolean mStableInsetsConsumed;
    private Rect mSystemWindowInsets;
    private boolean mSystemWindowInsetsConsumed;
    private Rect mTempRect;
    private Rect mWindowDecorInsets;
    private boolean mWindowDecorInsetsConsumed;
    private static final Rect EMPTY_RECT = new Rect(0, 0, 0, 0);
    private protected static final WindowInsets CONSUMED = new WindowInsets(null, null, null, false, false, null);

    public synchronized WindowInsets(Rect systemWindowInsets, Rect windowDecorInsets, Rect stableInsets, boolean isRound, boolean alwaysConsumeNavBar, DisplayCutout displayCutout) {
        boolean z;
        boolean z2;
        boolean z3;
        this.mSystemWindowInsetsConsumed = false;
        this.mWindowDecorInsetsConsumed = false;
        this.mStableInsetsConsumed = false;
        this.mDisplayCutoutConsumed = false;
        if (systemWindowInsets != null) {
            z = false;
        } else {
            z = true;
        }
        this.mSystemWindowInsetsConsumed = z;
        this.mSystemWindowInsets = this.mSystemWindowInsetsConsumed ? EMPTY_RECT : new Rect(systemWindowInsets);
        if (windowDecorInsets != null) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.mWindowDecorInsetsConsumed = z2;
        this.mWindowDecorInsets = this.mWindowDecorInsetsConsumed ? EMPTY_RECT : new Rect(windowDecorInsets);
        if (stableInsets != null) {
            z3 = false;
        } else {
            z3 = true;
        }
        this.mStableInsetsConsumed = z3;
        this.mStableInsets = this.mStableInsetsConsumed ? EMPTY_RECT : new Rect(stableInsets);
        this.mIsRound = isRound;
        this.mAlwaysConsumeNavBar = alwaysConsumeNavBar;
        this.mDisplayCutoutConsumed = displayCutout == null;
        this.mDisplayCutout = (this.mDisplayCutoutConsumed || displayCutout.isEmpty()) ? null : displayCutout;
    }

    public WindowInsets(WindowInsets src) {
        this.mSystemWindowInsetsConsumed = false;
        this.mWindowDecorInsetsConsumed = false;
        this.mStableInsetsConsumed = false;
        this.mDisplayCutoutConsumed = false;
        this.mSystemWindowInsets = src.mSystemWindowInsets;
        this.mWindowDecorInsets = src.mWindowDecorInsets;
        this.mStableInsets = src.mStableInsets;
        this.mSystemWindowInsetsConsumed = src.mSystemWindowInsetsConsumed;
        this.mWindowDecorInsetsConsumed = src.mWindowDecorInsetsConsumed;
        this.mStableInsetsConsumed = src.mStableInsetsConsumed;
        this.mIsRound = src.mIsRound;
        this.mAlwaysConsumeNavBar = src.mAlwaysConsumeNavBar;
        this.mDisplayCutout = src.mDisplayCutout;
        this.mDisplayCutoutConsumed = src.mDisplayCutoutConsumed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WindowInsets(Rect systemWindowInsets) {
        this(systemWindowInsets, null, null, false, false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rect getSystemWindowInsets() {
        if (this.mTempRect == null) {
            this.mTempRect = new Rect();
        }
        if (this.mSystemWindowInsets != null) {
            this.mTempRect.set(this.mSystemWindowInsets);
        } else {
            this.mTempRect.setEmpty();
        }
        return this.mTempRect;
    }

    public int getSystemWindowInsetLeft() {
        return this.mSystemWindowInsets.left;
    }

    public int getSystemWindowInsetTop() {
        return this.mSystemWindowInsets.top;
    }

    public int getSystemWindowInsetRight() {
        return this.mSystemWindowInsets.right;
    }

    public int getSystemWindowInsetBottom() {
        return this.mSystemWindowInsets.bottom;
    }

    public synchronized int getWindowDecorInsetLeft() {
        return this.mWindowDecorInsets.left;
    }

    public synchronized int getWindowDecorInsetTop() {
        return this.mWindowDecorInsets.top;
    }

    public synchronized int getWindowDecorInsetRight() {
        return this.mWindowDecorInsets.right;
    }

    public synchronized int getWindowDecorInsetBottom() {
        return this.mWindowDecorInsets.bottom;
    }

    public boolean hasSystemWindowInsets() {
        return (this.mSystemWindowInsets.left == 0 && this.mSystemWindowInsets.top == 0 && this.mSystemWindowInsets.right == 0 && this.mSystemWindowInsets.bottom == 0) ? false : true;
    }

    public synchronized boolean hasWindowDecorInsets() {
        return (this.mWindowDecorInsets.left == 0 && this.mWindowDecorInsets.top == 0 && this.mWindowDecorInsets.right == 0 && this.mWindowDecorInsets.bottom == 0) ? false : true;
    }

    public boolean hasInsets() {
        return hasSystemWindowInsets() || hasWindowDecorInsets() || hasStableInsets() || this.mDisplayCutout != null;
    }

    public DisplayCutout getDisplayCutout() {
        return this.mDisplayCutout;
    }

    public WindowInsets consumeDisplayCutout() {
        WindowInsets result = new WindowInsets(this);
        result.mDisplayCutout = null;
        result.mDisplayCutoutConsumed = true;
        return result;
    }

    public boolean isConsumed() {
        return this.mSystemWindowInsetsConsumed && this.mWindowDecorInsetsConsumed && this.mStableInsetsConsumed && this.mDisplayCutoutConsumed;
    }

    public boolean isRound() {
        return this.mIsRound;
    }

    public WindowInsets consumeSystemWindowInsets() {
        WindowInsets result = new WindowInsets(this);
        result.mSystemWindowInsets = EMPTY_RECT;
        result.mSystemWindowInsetsConsumed = true;
        return result;
    }

    public synchronized WindowInsets consumeSystemWindowInsets(boolean left, boolean top, boolean right, boolean bottom) {
        int i;
        int i2;
        int i3;
        if (left || top || right || bottom) {
            WindowInsets result = new WindowInsets(this);
            if (!left) {
                i = this.mSystemWindowInsets.left;
            } else {
                i = 0;
            }
            if (!top) {
                i2 = this.mSystemWindowInsets.top;
            } else {
                i2 = 0;
            }
            if (!right) {
                i3 = this.mSystemWindowInsets.right;
            } else {
                i3 = 0;
            }
            result.mSystemWindowInsets = new Rect(i, i2, i3, bottom ? 0 : this.mSystemWindowInsets.bottom);
            return result;
        }
        return this;
    }

    public WindowInsets replaceSystemWindowInsets(int left, int top, int right, int bottom) {
        WindowInsets result = new WindowInsets(this);
        result.mSystemWindowInsets = new Rect(left, top, right, bottom);
        return result;
    }

    public WindowInsets replaceSystemWindowInsets(Rect systemWindowInsets) {
        WindowInsets result = new WindowInsets(this);
        result.mSystemWindowInsets = new Rect(systemWindowInsets);
        return result;
    }

    public synchronized WindowInsets consumeWindowDecorInsets() {
        WindowInsets result = new WindowInsets(this);
        result.mWindowDecorInsets.set(0, 0, 0, 0);
        result.mWindowDecorInsetsConsumed = true;
        return result;
    }

    public synchronized WindowInsets consumeWindowDecorInsets(boolean left, boolean top, boolean right, boolean bottom) {
        int i;
        int i2;
        int i3;
        if (left || top || right || bottom) {
            WindowInsets result = new WindowInsets(this);
            if (!left) {
                i = this.mWindowDecorInsets.left;
            } else {
                i = 0;
            }
            if (!top) {
                i2 = this.mWindowDecorInsets.top;
            } else {
                i2 = 0;
            }
            if (!right) {
                i3 = this.mWindowDecorInsets.right;
            } else {
                i3 = 0;
            }
            result.mWindowDecorInsets = new Rect(i, i2, i3, bottom ? 0 : this.mWindowDecorInsets.bottom);
            return result;
        }
        return this;
    }

    public synchronized WindowInsets replaceWindowDecorInsets(int left, int top, int right, int bottom) {
        WindowInsets result = new WindowInsets(this);
        result.mWindowDecorInsets = new Rect(left, top, right, bottom);
        return result;
    }

    public int getStableInsetTop() {
        return this.mStableInsets.top;
    }

    public int getStableInsetLeft() {
        return this.mStableInsets.left;
    }

    public int getStableInsetRight() {
        return this.mStableInsets.right;
    }

    public int getStableInsetBottom() {
        return this.mStableInsets.bottom;
    }

    public boolean hasStableInsets() {
        return (this.mStableInsets.top == 0 && this.mStableInsets.left == 0 && this.mStableInsets.right == 0 && this.mStableInsets.bottom == 0) ? false : true;
    }

    public WindowInsets consumeStableInsets() {
        WindowInsets result = new WindowInsets(this);
        result.mStableInsets = EMPTY_RECT;
        result.mStableInsetsConsumed = true;
        return result;
    }

    public synchronized boolean shouldAlwaysConsumeNavBar() {
        return this.mAlwaysConsumeNavBar;
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("WindowInsets{systemWindowInsets=");
        sb.append(this.mSystemWindowInsets);
        sb.append(" windowDecorInsets=");
        sb.append(this.mWindowDecorInsets);
        sb.append(" stableInsets=");
        sb.append(this.mStableInsets);
        if (this.mDisplayCutout != null) {
            str = " cutout=" + this.mDisplayCutout;
        } else {
            str = "";
        }
        sb.append(str);
        sb.append(isRound() ? " round" : "");
        sb.append("}");
        return sb.toString();
    }

    public synchronized WindowInsets inset(Rect r) {
        return inset(r.left, r.top, r.right, r.bottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WindowInsets inset(int left, int top, int right, int bottom) {
        Preconditions.checkArgumentNonnegative(left);
        Preconditions.checkArgumentNonnegative(top);
        Preconditions.checkArgumentNonnegative(right);
        Preconditions.checkArgumentNonnegative(bottom);
        WindowInsets result = new WindowInsets(this);
        if (!result.mSystemWindowInsetsConsumed) {
            result.mSystemWindowInsets = insetInsets(result.mSystemWindowInsets, left, top, right, bottom);
        }
        if (!result.mWindowDecorInsetsConsumed) {
            result.mWindowDecorInsets = insetInsets(result.mWindowDecorInsets, left, top, right, bottom);
        }
        if (!result.mStableInsetsConsumed) {
            result.mStableInsets = insetInsets(result.mStableInsets, left, top, right, bottom);
        }
        if (this.mDisplayCutout != null) {
            result.mDisplayCutout = result.mDisplayCutout.inset(left, top, right, bottom);
            if (result.mDisplayCutout.isEmpty()) {
                result.mDisplayCutout = null;
            }
        }
        return result;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof WindowInsets)) {
            return false;
        }
        WindowInsets that = (WindowInsets) o;
        if (this.mIsRound == that.mIsRound && this.mAlwaysConsumeNavBar == that.mAlwaysConsumeNavBar && this.mSystemWindowInsetsConsumed == that.mSystemWindowInsetsConsumed && this.mWindowDecorInsetsConsumed == that.mWindowDecorInsetsConsumed && this.mStableInsetsConsumed == that.mStableInsetsConsumed && this.mDisplayCutoutConsumed == that.mDisplayCutoutConsumed && Objects.equals(this.mSystemWindowInsets, that.mSystemWindowInsets) && Objects.equals(this.mWindowDecorInsets, that.mWindowDecorInsets) && Objects.equals(this.mStableInsets, that.mStableInsets) && Objects.equals(this.mDisplayCutout, that.mDisplayCutout)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mSystemWindowInsets, this.mWindowDecorInsets, this.mStableInsets, Boolean.valueOf(this.mIsRound), this.mDisplayCutout, Boolean.valueOf(this.mAlwaysConsumeNavBar), Boolean.valueOf(this.mSystemWindowInsetsConsumed), Boolean.valueOf(this.mWindowDecorInsetsConsumed), Boolean.valueOf(this.mStableInsetsConsumed), Boolean.valueOf(this.mDisplayCutoutConsumed));
    }

    private static synchronized Rect insetInsets(Rect insets, int left, int top, int right, int bottom) {
        int newLeft = Math.max(0, insets.left - left);
        int newTop = Math.max(0, insets.top - top);
        int newRight = Math.max(0, insets.right - right);
        int newBottom = Math.max(0, insets.bottom - bottom);
        if (newLeft == left && newTop == top && newRight == right && newBottom == bottom) {
            return insets;
        }
        return new Rect(newLeft, newTop, newRight, newBottom);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isSystemWindowInsetsConsumed() {
        return this.mSystemWindowInsetsConsumed;
    }
}
