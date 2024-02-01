package android.graphics;

import android.graphics.PorterDuff;
/* loaded from: classes.dex */
public class PorterDuffColorFilter extends ColorFilter {
    private int mColor;
    private PorterDuff.Mode mMode;

    private static native long native_CreatePorterDuffFilter(int i, int i2);

    public PorterDuffColorFilter(int color, PorterDuff.Mode mode) {
        this.mColor = color;
        this.mMode = mode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getColor() {
        return this.mColor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setColor(int color) {
        if (this.mColor != color) {
            this.mColor = color;
            discardNativeInstance();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PorterDuff.Mode getMode() {
        return this.mMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMode(PorterDuff.Mode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("mode must be non-null");
        }
        this.mMode = mode;
        discardNativeInstance();
    }

    @Override // android.graphics.ColorFilter
    synchronized long createNativeInstance() {
        return native_CreatePorterDuffFilter(this.mColor, this.mMode.nativeInt);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PorterDuffColorFilter other = (PorterDuffColorFilter) object;
        if (this.mColor == other.mColor && this.mMode.nativeInt == other.mMode.nativeInt) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (31 * this.mMode.hashCode()) + this.mColor;
    }
}
