package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import java.util.Map;
/* loaded from: classes2.dex */
public class Crossfade extends Transition {
    public static final int FADE_BEHAVIOR_CROSSFADE = 0;
    public static final int FADE_BEHAVIOR_OUT_IN = 2;
    public static final int FADE_BEHAVIOR_REVEAL = 1;
    private static final String LOG_TAG = "Crossfade";
    private static final String PROPNAME_BITMAP = "android:crossfade:bitmap";
    private static final String PROPNAME_BOUNDS = "android:crossfade:bounds";
    private static final String PROPNAME_DRAWABLE = "android:crossfade:drawable";
    public static final int RESIZE_BEHAVIOR_NONE = 0;
    public static final int RESIZE_BEHAVIOR_SCALE = 1;
    private static RectEvaluator sRectEvaluator = new RectEvaluator();
    private int mFadeBehavior = 1;
    private int mResizeBehavior = 1;

    public synchronized Crossfade setFadeBehavior(int fadeBehavior) {
        if (fadeBehavior >= 0 && fadeBehavior <= 2) {
            this.mFadeBehavior = fadeBehavior;
        }
        return this;
    }

    public synchronized int getFadeBehavior() {
        return this.mFadeBehavior;
    }

    public synchronized Crossfade setResizeBehavior(int resizeBehavior) {
        if (resizeBehavior >= 0 && resizeBehavior <= 1) {
            this.mResizeBehavior = resizeBehavior;
        }
        return this;
    }

    public synchronized int getResizeBehavior() {
        return this.mResizeBehavior;
    }

    @Override // android.transition.Transition
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        ViewOverlay overlay;
        ObjectAnimator anim;
        BitmapDrawable endDrawable;
        if (startValues == null || endValues == null) {
            return null;
        }
        final boolean useParentOverlay = this.mFadeBehavior != 1;
        final View view = endValues.view;
        Map<String, Object> startVals = startValues.values;
        Map<String, Object> endVals = endValues.values;
        Rect startBounds = (Rect) startVals.get(PROPNAME_BOUNDS);
        Rect endBounds = (Rect) endVals.get(PROPNAME_BOUNDS);
        Bitmap startBitmap = (Bitmap) startVals.get(PROPNAME_BITMAP);
        Bitmap endBitmap = (Bitmap) endVals.get(PROPNAME_BITMAP);
        final BitmapDrawable startDrawable = (BitmapDrawable) startVals.get(PROPNAME_DRAWABLE);
        BitmapDrawable endDrawable2 = (BitmapDrawable) endVals.get(PROPNAME_DRAWABLE);
        if (startDrawable == null || endDrawable2 == null || startBitmap.sameAs(endBitmap)) {
            return null;
        }
        ViewOverlay overlay2 = useParentOverlay ? ((ViewGroup) view.getParent()).getOverlay() : view.getOverlay();
        if (this.mFadeBehavior == 1) {
            overlay2.add(endDrawable2);
        }
        overlay2.add(startDrawable);
        if (this.mFadeBehavior == 2) {
            overlay = overlay2;
            anim = ObjectAnimator.ofInt(startDrawable, "alpha", 255, 0, 0);
        } else {
            overlay = overlay2;
            anim = ObjectAnimator.ofInt(startDrawable, "alpha", 0);
        }
        ObjectAnimator anim2 = anim;
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.transition.Crossfade.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                view.invalidate(startDrawable.getBounds());
            }
        });
        ObjectAnimator anim1 = null;
        if (this.mFadeBehavior == 2) {
            ObjectAnimator anim12 = ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 0.0f, 1.0f);
            anim1 = anim12;
            endDrawable = endDrawable2;
        } else if (this.mFadeBehavior == 0) {
            endDrawable = endDrawable2;
            ObjectAnimator anim13 = ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 1.0f);
            anim1 = anim13;
        } else {
            endDrawable = endDrawable2;
        }
        final BitmapDrawable endDrawable3 = endDrawable;
        anim2.addListener(new AnimatorListenerAdapter() { // from class: android.transition.Crossfade.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                ViewOverlay overlay3 = useParentOverlay ? ((ViewGroup) view.getParent()).getOverlay() : view.getOverlay();
                overlay3.remove(startDrawable);
                if (Crossfade.this.mFadeBehavior == 1) {
                    overlay3.remove(endDrawable3);
                }
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.playTogether(anim2);
        if (anim1 != null) {
            set.playTogether(anim1);
        }
        if (this.mResizeBehavior == 1 && !startBounds.equals(endBounds)) {
            Animator anim22 = ObjectAnimator.ofObject(startDrawable, "bounds", sRectEvaluator, startBounds, endBounds);
            set.playTogether(anim22);
            if (this.mResizeBehavior == 1) {
                Animator anim3 = ObjectAnimator.ofObject(endDrawable3, "bounds", sRectEvaluator, startBounds, endBounds);
                set.playTogether(anim3);
            }
        }
        return set;
    }

    private synchronized void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        Rect bounds = new Rect(0, 0, view.getWidth(), view.getHeight());
        if (this.mFadeBehavior != 1) {
            bounds.offset(view.getLeft(), view.getTop());
        }
        transitionValues.values.put(PROPNAME_BOUNDS, bounds);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        if (view instanceof TextureView) {
            bitmap = ((TextureView) view).getBitmap();
        } else {
            Canvas c = new Canvas(bitmap);
            view.draw(c);
        }
        transitionValues.values.put(PROPNAME_BITMAP, bitmap);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        drawable.setBounds(bounds);
        transitionValues.values.put(PROPNAME_DRAWABLE, drawable);
    }

    @Override // android.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // android.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }
}
