package android.view;

import android.animation.Animator;
import android.animation.RevealAnimator;
/* loaded from: classes2.dex */
public final class ViewAnimationUtils {
    private synchronized ViewAnimationUtils() {
    }

    public static Animator createCircularReveal(View view, int centerX, int centerY, float startRadius, float endRadius) {
        return new RevealAnimator(view, centerX, centerY, startRadius, endRadius);
    }
}
