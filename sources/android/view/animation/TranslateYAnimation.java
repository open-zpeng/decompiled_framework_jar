package android.view.animation;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Matrix;

/* loaded from: classes3.dex */
public class TranslateYAnimation extends TranslateAnimation {
    float[] mTmpValues;

    public TranslateYAnimation(float fromYDelta, float toYDelta) {
        super(0.0f, 0.0f, fromYDelta, toYDelta);
        this.mTmpValues = new float[9];
    }

    @UnsupportedAppUsage
    public TranslateYAnimation(int fromYType, float fromYValue, int toYType, float toYValue) {
        super(0, 0.0f, 0, 0.0f, fromYType, fromYValue, toYType, toYValue);
        this.mTmpValues = new float[9];
    }

    @Override // android.view.animation.TranslateAnimation, android.view.animation.Animation
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Matrix m = t.getMatrix();
        m.getValues(this.mTmpValues);
        float dy = this.mFromYDelta + ((this.mToYDelta - this.mFromYDelta) * interpolatedTime);
        t.getMatrix().setTranslate(this.mTmpValues[2], dy);
    }
}
