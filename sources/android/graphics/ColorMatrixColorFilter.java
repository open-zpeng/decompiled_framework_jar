package android.graphics;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes.dex */
public class ColorMatrixColorFilter extends ColorFilter {
    @UnsupportedAppUsage
    private final ColorMatrix mMatrix = new ColorMatrix();

    private static native long nativeColorMatrixFilter(float[] fArr);

    public ColorMatrixColorFilter(ColorMatrix matrix) {
        this.mMatrix.set(matrix);
    }

    public ColorMatrixColorFilter(float[] array) {
        if (array.length < 20) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.mMatrix.set(array);
    }

    public void getColorMatrix(ColorMatrix colorMatrix) {
        colorMatrix.set(this.mMatrix);
    }

    @UnsupportedAppUsage
    public void setColorMatrix(ColorMatrix matrix) {
        discardNativeInstance();
        if (matrix == null) {
            this.mMatrix.reset();
        } else {
            this.mMatrix.set(matrix);
        }
    }

    @UnsupportedAppUsage
    public void setColorMatrixArray(float[] array) {
        discardNativeInstance();
        if (array == null) {
            this.mMatrix.reset();
        } else if (array.length < 20) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            this.mMatrix.set(array);
        }
    }

    @Override // android.graphics.ColorFilter
    long createNativeInstance() {
        return nativeColorMatrixFilter(this.mMatrix.getArray());
    }
}
