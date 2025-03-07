package com.android.internal.view.animation;

import android.animation.TimeInterpolator;
import android.util.TimeUtils;
import android.view.Choreographer;

@HasNativeInterpolator
/* loaded from: classes3.dex */
public class FallbackLUTInterpolator implements NativeInterpolatorFactory, TimeInterpolator {
    private static final int MAX_SAMPLE_POINTS = 300;
    private final float[] mLut;
    private TimeInterpolator mSourceInterpolator;

    public FallbackLUTInterpolator(TimeInterpolator interpolator, long duration) {
        this.mSourceInterpolator = interpolator;
        this.mLut = createLUT(interpolator, duration);
    }

    private static float[] createLUT(TimeInterpolator interpolator, long duration) {
        long frameIntervalNanos = Choreographer.getInstance().getFrameIntervalNanos();
        int animIntervalMs = (int) (frameIntervalNanos / TimeUtils.NANOS_PER_MS);
        int numAnimFrames = Math.min(Math.max(2, (int) Math.ceil(duration / animIntervalMs)), 300);
        float[] values = new float[numAnimFrames];
        float lastFrame = numAnimFrames - 1;
        for (int i = 0; i < numAnimFrames; i++) {
            float inValue = i / lastFrame;
            values[i] = interpolator.getInterpolation(inValue);
        }
        return values;
    }

    @Override // com.android.internal.view.animation.NativeInterpolatorFactory
    public long createNativeInterpolator() {
        return NativeInterpolatorFactoryHelper.createLutInterpolator(this.mLut);
    }

    public static long createNativeInterpolator(TimeInterpolator interpolator, long duration) {
        float[] lut = createLUT(interpolator, duration);
        return NativeInterpolatorFactoryHelper.createLutInterpolator(lut);
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float input) {
        return this.mSourceInterpolator.getInterpolation(input);
    }
}
