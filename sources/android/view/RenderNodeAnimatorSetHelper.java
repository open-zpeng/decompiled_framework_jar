package android.view;

import android.animation.TimeInterpolator;
import com.android.internal.view.animation.FallbackLUTInterpolator;
import com.android.internal.view.animation.NativeInterpolatorFactory;
import com.android.internal.view.animation.NativeInterpolatorFactoryHelper;
/* loaded from: classes2.dex */
public class RenderNodeAnimatorSetHelper {
    public static synchronized RenderNode getTarget(DisplayListCanvas recordingCanvas) {
        return recordingCanvas.mNode;
    }

    public static synchronized long createNativeInterpolator(TimeInterpolator interpolator, long duration) {
        if (interpolator == null) {
            return NativeInterpolatorFactoryHelper.createLinearInterpolator();
        }
        if (RenderNodeAnimator.isNativeInterpolator(interpolator)) {
            return ((NativeInterpolatorFactory) interpolator).createNativeInterpolator();
        }
        return FallbackLUTInterpolator.createNativeInterpolator(interpolator, duration);
    }
}
