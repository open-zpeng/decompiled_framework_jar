package android.graphics;
/* loaded from: classes.dex */
public class Insets {
    private protected static final Insets NONE = new Insets(0, 0, 0, 0);
    private protected final int bottom;
    private protected final int left;
    private protected final int right;
    private protected final int top;

    private synchronized Insets(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Insets of(int left, int top, int right, int bottom) {
        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
            return NONE;
        }
        return new Insets(left, top, right, bottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Insets of(Rect r) {
        return r == null ? NONE : of(r.left, r.top, r.right, r.bottom);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Insets insets = (Insets) o;
        if (this.bottom == insets.bottom && this.left == insets.left && this.right == insets.right && this.top == insets.top) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.left;
        return (31 * ((31 * ((31 * result) + this.top)) + this.right)) + this.bottom;
    }

    public String toString() {
        return "Insets{left=" + this.left + ", top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + '}';
    }
}
