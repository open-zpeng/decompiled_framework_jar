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
/* loaded from: classes2.dex */
public class OvershootInterpolator extends BaseInterpolator implements NativeInterpolatorFactory {
    private final float mTension;

    public OvershootInterpolator() {
        this.mTension = 2.0f;
    }

    public OvershootInterpolator(float tension) {
        this.mTension = tension;
    }

    public OvershootInterpolator(Context context, AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    public synchronized OvershootInterpolator(Resources res, Resources.Theme theme, AttributeSet attrs) {
        TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.OvershootInterpolator, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.OvershootInterpolator);
        }
        this.mTension = a.getFloat(0, 2.0f);
        setChangingConfiguration(a.getChangingConfigurations());
        a.recycle();
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float t) {
        float t2 = t - 1.0f;
        return (t2 * t2 * (((this.mTension + 1.0f) * t2) + this.mTension)) + 1.0f;
    }

    public synchronized long createNativeInterpolator() {
        return NativeInterpolatorFactoryHelper.createOvershootInterpolator(this.mTension);
    }
}
