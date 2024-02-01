package android.graphics;
/* loaded from: classes.dex */
public class SweepGradient extends Shader {
    private static final int TYPE_COLORS_AND_POSITIONS = 1;
    private static final int TYPE_COLOR_START_AND_COLOR_END = 2;
    public protected int mColor0;
    public protected int mColor1;
    public protected int[] mColors;
    public protected float mCx;
    public protected float mCy;
    public protected float[] mPositions;
    private int mType;

    private static native long nativeCreate1(long j, float f, float f2, int[] iArr, float[] fArr);

    private static native long nativeCreate2(long j, float f, float f2, int i, int i2);

    public SweepGradient(float cx, float cy, int[] colors, float[] positions) {
        if (colors.length < 2) {
            throw new IllegalArgumentException("needs >= 2 number of colors");
        }
        if (positions != null && colors.length != positions.length) {
            throw new IllegalArgumentException("color and position arrays must be of equal length");
        }
        this.mType = 1;
        this.mCx = cx;
        this.mCy = cy;
        this.mColors = (int[]) colors.clone();
        this.mPositions = positions != null ? (float[]) positions.clone() : null;
    }

    public SweepGradient(float cx, float cy, int color0, int color1) {
        this.mType = 2;
        this.mCx = cx;
        this.mCy = cy;
        this.mColor0 = color0;
        this.mColor1 = color1;
        this.mColors = null;
        this.mPositions = null;
    }

    @Override // android.graphics.Shader
    synchronized long createNativeInstance(long nativeMatrix) {
        if (this.mType == 1) {
            return nativeCreate1(nativeMatrix, this.mCx, this.mCy, this.mColors, this.mPositions);
        }
        return nativeCreate2(nativeMatrix, this.mCx, this.mCy, this.mColor0, this.mColor1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.Shader
    public synchronized Shader copy() {
        SweepGradient copy;
        if (this.mType == 1) {
            copy = new SweepGradient(this.mCx, this.mCy, (int[]) this.mColors.clone(), this.mPositions != null ? (float[]) this.mPositions.clone() : null);
        } else {
            copy = new SweepGradient(this.mCx, this.mCy, this.mColor0, this.mColor1);
        }
        copyLocalMatrix(copy);
        return copy;
    }
}
