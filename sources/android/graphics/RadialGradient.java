package android.graphics;

import android.graphics.Shader;
/* loaded from: classes.dex */
public class RadialGradient extends Shader {
    private static final int TYPE_COLORS_AND_POSITIONS = 1;
    private static final int TYPE_COLOR_CENTER_AND_COLOR_EDGE = 2;
    public protected int mCenterColor;
    public protected int[] mColors;
    public protected int mEdgeColor;
    public protected float[] mPositions;
    public protected float mRadius;
    public protected Shader.TileMode mTileMode;
    private int mType;
    public protected float mX;
    public protected float mY;

    private static native long nativeCreate1(long j, float f, float f2, float f3, int[] iArr, float[] fArr, int i);

    private static native long nativeCreate2(long j, float f, float f2, float f3, int i, int i2, int i3);

    public RadialGradient(float centerX, float centerY, float radius, int[] colors, float[] stops, Shader.TileMode tileMode) {
        if (radius <= 0.0f) {
            throw new IllegalArgumentException("radius must be > 0");
        }
        if (colors.length < 2) {
            throw new IllegalArgumentException("needs >= 2 number of colors");
        }
        if (stops != null && colors.length != stops.length) {
            throw new IllegalArgumentException("color and position arrays must be of equal length");
        }
        this.mType = 1;
        this.mX = centerX;
        this.mY = centerY;
        this.mRadius = radius;
        this.mColors = (int[]) colors.clone();
        this.mPositions = stops != null ? (float[]) stops.clone() : null;
        this.mTileMode = tileMode;
    }

    public RadialGradient(float centerX, float centerY, float radius, int centerColor, int edgeColor, Shader.TileMode tileMode) {
        if (radius <= 0.0f) {
            throw new IllegalArgumentException("radius must be > 0");
        }
        this.mType = 2;
        this.mX = centerX;
        this.mY = centerY;
        this.mRadius = radius;
        this.mCenterColor = centerColor;
        this.mEdgeColor = edgeColor;
        this.mTileMode = tileMode;
    }

    @Override // android.graphics.Shader
    synchronized long createNativeInstance(long nativeMatrix) {
        if (this.mType == 1) {
            return nativeCreate1(nativeMatrix, this.mX, this.mY, this.mRadius, this.mColors, this.mPositions, this.mTileMode.nativeInt);
        }
        return nativeCreate2(nativeMatrix, this.mX, this.mY, this.mRadius, this.mCenterColor, this.mEdgeColor, this.mTileMode.nativeInt);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.Shader
    public synchronized Shader copy() {
        RadialGradient copy;
        if (this.mType == 1) {
            copy = new RadialGradient(this.mX, this.mY, this.mRadius, (int[]) this.mColors.clone(), this.mPositions != null ? (float[]) this.mPositions.clone() : null, this.mTileMode);
        } else {
            copy = new RadialGradient(this.mX, this.mY, this.mRadius, this.mCenterColor, this.mEdgeColor, this.mTileMode);
        }
        copyLocalMatrix(copy);
        return copy;
    }
}
