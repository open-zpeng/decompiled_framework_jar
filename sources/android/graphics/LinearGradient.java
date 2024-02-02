package android.graphics;

import android.graphics.Shader;
/* loaded from: classes.dex */
public class LinearGradient extends Shader {
    private static final int TYPE_COLORS_AND_POSITIONS = 1;
    private static final int TYPE_COLOR_START_AND_COLOR_END = 2;
    public protected int mColor0;
    public protected int mColor1;
    public protected int[] mColors;
    public protected float[] mPositions;
    public protected Shader.TileMode mTileMode;
    private int mType;
    public protected float mX0;
    public protected float mX1;
    public protected float mY0;
    public protected float mY1;

    private native long nativeCreate1(long j, float f, float f2, float f3, float f4, int[] iArr, float[] fArr, int i);

    private native long nativeCreate2(long j, float f, float f2, float f3, float f4, int i, int i2, int i3);

    public LinearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions, Shader.TileMode tile) {
        if (colors.length < 2) {
            throw new IllegalArgumentException("needs >= 2 number of colors");
        }
        if (positions != null && colors.length != positions.length) {
            throw new IllegalArgumentException("color and position arrays must be of equal length");
        }
        this.mType = 1;
        this.mX0 = x0;
        this.mY0 = y0;
        this.mX1 = x1;
        this.mY1 = y1;
        this.mColors = (int[]) colors.clone();
        this.mPositions = positions != null ? (float[]) positions.clone() : null;
        this.mTileMode = tile;
    }

    public LinearGradient(float x0, float y0, float x1, float y1, int color0, int color1, Shader.TileMode tile) {
        this.mType = 2;
        this.mX0 = x0;
        this.mY0 = y0;
        this.mX1 = x1;
        this.mY1 = y1;
        this.mColor0 = color0;
        this.mColor1 = color1;
        this.mColors = null;
        this.mPositions = null;
        this.mTileMode = tile;
    }

    @Override // android.graphics.Shader
    synchronized long createNativeInstance(long nativeMatrix) {
        if (this.mType == 1) {
            return nativeCreate1(nativeMatrix, this.mX0, this.mY0, this.mX1, this.mY1, this.mColors, this.mPositions, this.mTileMode.nativeInt);
        }
        return nativeCreate2(nativeMatrix, this.mX0, this.mY0, this.mX1, this.mY1, this.mColor0, this.mColor1, this.mTileMode.nativeInt);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.Shader
    public synchronized Shader copy() {
        LinearGradient copy;
        if (this.mType == 1) {
            copy = new LinearGradient(this.mX0, this.mY0, this.mX1, this.mY1, (int[]) this.mColors.clone(), this.mPositions != null ? (float[]) this.mPositions.clone() : null, this.mTileMode);
        } else {
            copy = new LinearGradient(this.mX0, this.mY0, this.mX1, this.mY1, this.mColor0, this.mColor1, this.mTileMode);
        }
        copyLocalMatrix(copy);
        return copy;
    }
}
