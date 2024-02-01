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
public class CycleInterpolator extends BaseInterpolator implements NativeInterpolatorFactory {
    private float mCycles;

    public CycleInterpolator(float cycles) {
        this.mCycles = cycles;
    }

    public CycleInterpolator(Context context, AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    public CycleInterpolator(Resources resources, Resources.Theme theme, AttributeSet attrs) {
        TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.CycleInterpolator, 0, 0);
        } else {
            a = resources.obtainAttributes(attrs, R.styleable.CycleInterpolator);
        }
        this.mCycles = a.getFloat(0, 1.0f);
        setChangingConfiguration(a.getChangingConfigurations());
        a.recycle();
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float input) {
        return (float) Math.sin(this.mCycles * 2.0f * 3.141592653589793d * input);
    }

    @Override // com.android.internal.view.animation.NativeInterpolatorFactory
    public long createNativeInterpolator() {
        return NativeInterpolatorFactoryHelper.createCycleInterpolator(this.mCycles);
    }
}
