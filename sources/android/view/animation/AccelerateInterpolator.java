package android.view.animation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.android.internal.R;
import com.android.internal.view.animation.HasNativeInterpolator;
import com.android.internal.view.animation.NativeInterpolatorFactory;
import com.android.internal.view.animation.NativeInterpolatorFactoryHelper;

@HasNativeInterpolator
/* loaded from: classes3.dex */
public class AccelerateInterpolator extends BaseInterpolator implements NativeInterpolatorFactory {
    private final double mDoubleFactor;
    private final float mFactor;

    public AccelerateInterpolator() {
        this.mFactor = 1.0f;
        this.mDoubleFactor = 2.0d;
    }

    public AccelerateInterpolator(float factor) {
        this.mFactor = factor;
        this.mDoubleFactor = this.mFactor * 2.0f;
    }

    public AccelerateInterpolator(Context context, AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    public AccelerateInterpolator(Resources res, Resources.Theme theme, AttributeSet attrs) {
        TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.AccelerateInterpolator, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.AccelerateInterpolator);
        }
        this.mFactor = a.getFloat(0, 1.0f);
        this.mDoubleFactor = this.mFactor * 2.0f;
        setChangingConfiguration(a.getChangingConfigurations());
        a.recycle();
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float input) {
        if (this.mFactor == 1.0f) {
            return input * input;
        }
        return (float) Math.pow(input, this.mDoubleFactor);
    }

    @Override // com.android.internal.view.animation.NativeInterpolatorFactory
    public long createNativeInterpolator() {
        return NativeInterpolatorFactoryHelper.createAccelerateInterpolator(this.mFactor);
    }
}
