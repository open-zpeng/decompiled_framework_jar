package android.view;

import android.animation.TimeInterpolator;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.android.internal.view.animation.FallbackLUTInterpolator;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ViewPropertyAnimatorRT {
    private static final Interpolator sLinearInterpolator = new LinearInterpolator();
    private RenderNodeAnimator[] mAnimators = new RenderNodeAnimator[12];
    private final View mView;

    synchronized ViewPropertyAnimatorRT(View view) {
        this.mView = view;
    }

    public synchronized boolean startAnimation(ViewPropertyAnimator parent) {
        cancelAnimators(parent.mPendingAnimations);
        if (!canHandleAnimator(parent)) {
            return false;
        }
        doStartAnimation(parent);
        return true;
    }

    public synchronized void cancelAll() {
        for (int i = 0; i < this.mAnimators.length; i++) {
            if (this.mAnimators[i] != null) {
                this.mAnimators[i].cancel();
                this.mAnimators[i] = null;
            }
        }
    }

    private synchronized void doStartAnimation(ViewPropertyAnimator parent) {
        int size = parent.mPendingAnimations.size();
        long startDelay = parent.getStartDelay();
        long duration = parent.getDuration();
        TimeInterpolator interpolator = parent.getInterpolator();
        if (interpolator == null) {
            interpolator = sLinearInterpolator;
        }
        if (!RenderNodeAnimator.isNativeInterpolator(interpolator)) {
            interpolator = new FallbackLUTInterpolator(interpolator, duration);
        }
        for (int i = 0; i < size; i++) {
            ViewPropertyAnimator.NameValuesHolder holder = parent.mPendingAnimations.get(i);
            int property = RenderNodeAnimator.mapViewPropertyToRenderProperty(holder.mNameConstant);
            float finalValue = holder.mFromValue + holder.mDeltaValue;
            RenderNodeAnimator animator = new RenderNodeAnimator(property, finalValue);
            animator.setStartDelay(startDelay);
            animator.setDuration(duration);
            animator.setInterpolator(interpolator);
            animator.setTarget(this.mView);
            animator.start();
            this.mAnimators[property] = animator;
        }
        parent.mPendingAnimations.clear();
    }

    private synchronized boolean canHandleAnimator(ViewPropertyAnimator parent) {
        return parent.getUpdateListener() == null && parent.getListener() == null && this.mView.isHardwareAccelerated() && !parent.hasActions();
    }

    private synchronized void cancelAnimators(ArrayList<ViewPropertyAnimator.NameValuesHolder> mPendingAnimations) {
        int size = mPendingAnimations.size();
        for (int i = 0; i < size; i++) {
            ViewPropertyAnimator.NameValuesHolder holder = mPendingAnimations.get(i);
            int property = RenderNodeAnimator.mapViewPropertyToRenderProperty(holder.mNameConstant);
            if (this.mAnimators[property] != null) {
                this.mAnimators[property].cancel();
                this.mAnimators[property] = null;
            }
        }
    }
}
