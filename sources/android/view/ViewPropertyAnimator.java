package android.view;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
/* loaded from: classes2.dex */
public class ViewPropertyAnimator {
    static final int ALPHA = 2048;
    static final int NONE = 0;
    static final int ROTATION = 32;
    static final int ROTATION_X = 64;
    static final int ROTATION_Y = 128;
    static final int SCALE_X = 8;
    static final int SCALE_Y = 16;
    private static final int TRANSFORM_MASK = 2047;
    static final int TRANSLATION_X = 1;
    static final int TRANSLATION_Y = 2;
    static final int TRANSLATION_Z = 4;
    static final int X = 256;
    static final int Y = 512;
    static final int Z = 1024;
    private HashMap<Animator, Runnable> mAnimatorCleanupMap;
    private HashMap<Animator, Runnable> mAnimatorOnEndMap;
    private HashMap<Animator, Runnable> mAnimatorOnStartMap;
    private HashMap<Animator, Runnable> mAnimatorSetupMap;
    private long mDuration;
    private TimeInterpolator mInterpolator;
    private Runnable mPendingCleanupAction;
    private Runnable mPendingOnEndAction;
    private Runnable mPendingOnStartAction;
    private Runnable mPendingSetupAction;
    public protected ViewPropertyAnimatorRT mRTBackend;
    private ValueAnimator mTempValueAnimator;
    final View mView;
    private boolean mDurationSet = false;
    private long mStartDelay = 0;
    private boolean mStartDelaySet = false;
    private boolean mInterpolatorSet = false;
    private Animator.AnimatorListener mListener = null;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = null;
    private AnimatorEventListener mAnimatorEventListener = new AnimatorEventListener();
    ArrayList<NameValuesHolder> mPendingAnimations = new ArrayList<>();
    private Runnable mAnimationStarter = new Runnable() { // from class: android.view.ViewPropertyAnimator.1
        @Override // java.lang.Runnable
        public void run() {
            ViewPropertyAnimator.this.startAnimation();
        }
    };
    private HashMap<Animator, PropertyBundle> mAnimatorMap = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class PropertyBundle {
        ArrayList<NameValuesHolder> mNameValuesHolder;
        int mPropertyMask;

        synchronized PropertyBundle(int propertyMask, ArrayList<NameValuesHolder> nameValuesHolder) {
            this.mPropertyMask = propertyMask;
            this.mNameValuesHolder = nameValuesHolder;
        }

        synchronized boolean cancel(int propertyConstant) {
            if ((this.mPropertyMask & propertyConstant) != 0 && this.mNameValuesHolder != null) {
                int count = this.mNameValuesHolder.size();
                for (int i = 0; i < count; i++) {
                    NameValuesHolder nameValuesHolder = this.mNameValuesHolder.get(i);
                    if (nameValuesHolder.mNameConstant == propertyConstant) {
                        this.mNameValuesHolder.remove(i);
                        this.mPropertyMask &= ~propertyConstant;
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class NameValuesHolder {
        float mDeltaValue;
        float mFromValue;
        int mNameConstant;

        synchronized NameValuesHolder(int nameConstant, float fromValue, float deltaValue) {
            this.mNameConstant = nameConstant;
            this.mFromValue = fromValue;
            this.mDeltaValue = deltaValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ViewPropertyAnimator(View view) {
        this.mView = view;
        view.ensureTransformationInfo();
    }

    public ViewPropertyAnimator setDuration(long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " + duration);
        }
        this.mDurationSet = true;
        this.mDuration = duration;
        return this;
    }

    public long getDuration() {
        if (this.mDurationSet) {
            return this.mDuration;
        }
        if (this.mTempValueAnimator == null) {
            this.mTempValueAnimator = new ValueAnimator();
        }
        return this.mTempValueAnimator.getDuration();
    }

    public long getStartDelay() {
        if (this.mStartDelaySet) {
            return this.mStartDelay;
        }
        return 0L;
    }

    public ViewPropertyAnimator setStartDelay(long startDelay) {
        if (startDelay < 0) {
            throw new IllegalArgumentException("Animators cannot have negative start delay: " + startDelay);
        }
        this.mStartDelaySet = true;
        this.mStartDelay = startDelay;
        return this;
    }

    public ViewPropertyAnimator setInterpolator(TimeInterpolator interpolator) {
        this.mInterpolatorSet = true;
        this.mInterpolator = interpolator;
        return this;
    }

    public TimeInterpolator getInterpolator() {
        if (this.mInterpolatorSet) {
            return this.mInterpolator;
        }
        if (this.mTempValueAnimator == null) {
            this.mTempValueAnimator = new ValueAnimator();
        }
        return this.mTempValueAnimator.getInterpolator();
    }

    public ViewPropertyAnimator setListener(Animator.AnimatorListener listener) {
        this.mListener = listener;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Animator.AnimatorListener getListener() {
        return this.mListener;
    }

    public ViewPropertyAnimator setUpdateListener(ValueAnimator.AnimatorUpdateListener listener) {
        this.mUpdateListener = listener;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ValueAnimator.AnimatorUpdateListener getUpdateListener() {
        return this.mUpdateListener;
    }

    public void start() {
        this.mView.removeCallbacks(this.mAnimationStarter);
        startAnimation();
    }

    public void cancel() {
        if (this.mAnimatorMap.size() > 0) {
            HashMap<Animator, PropertyBundle> mAnimatorMapCopy = (HashMap) this.mAnimatorMap.clone();
            Set<Animator> animatorSet = mAnimatorMapCopy.keySet();
            for (Animator runningAnim : animatorSet) {
                runningAnim.cancel();
            }
        }
        this.mPendingAnimations.clear();
        this.mPendingSetupAction = null;
        this.mPendingCleanupAction = null;
        this.mPendingOnStartAction = null;
        this.mPendingOnEndAction = null;
        this.mView.removeCallbacks(this.mAnimationStarter);
        if (this.mRTBackend != null) {
            this.mRTBackend.cancelAll();
        }
    }

    public ViewPropertyAnimator x(float value) {
        animateProperty(256, value);
        return this;
    }

    public ViewPropertyAnimator xBy(float value) {
        animatePropertyBy(256, value);
        return this;
    }

    public ViewPropertyAnimator y(float value) {
        animateProperty(512, value);
        return this;
    }

    public ViewPropertyAnimator yBy(float value) {
        animatePropertyBy(512, value);
        return this;
    }

    public ViewPropertyAnimator z(float value) {
        animateProperty(1024, value);
        return this;
    }

    public ViewPropertyAnimator zBy(float value) {
        animatePropertyBy(1024, value);
        return this;
    }

    public ViewPropertyAnimator rotation(float value) {
        animateProperty(32, value);
        return this;
    }

    public ViewPropertyAnimator rotationBy(float value) {
        animatePropertyBy(32, value);
        return this;
    }

    public ViewPropertyAnimator rotationX(float value) {
        animateProperty(64, value);
        return this;
    }

    public ViewPropertyAnimator rotationXBy(float value) {
        animatePropertyBy(64, value);
        return this;
    }

    public ViewPropertyAnimator rotationY(float value) {
        animateProperty(128, value);
        return this;
    }

    public ViewPropertyAnimator rotationYBy(float value) {
        animatePropertyBy(128, value);
        return this;
    }

    public ViewPropertyAnimator translationX(float value) {
        animateProperty(1, value);
        return this;
    }

    public ViewPropertyAnimator translationXBy(float value) {
        animatePropertyBy(1, value);
        return this;
    }

    public ViewPropertyAnimator translationY(float value) {
        animateProperty(2, value);
        return this;
    }

    public ViewPropertyAnimator translationYBy(float value) {
        animatePropertyBy(2, value);
        return this;
    }

    public ViewPropertyAnimator translationZ(float value) {
        animateProperty(4, value);
        return this;
    }

    public ViewPropertyAnimator translationZBy(float value) {
        animatePropertyBy(4, value);
        return this;
    }

    public ViewPropertyAnimator scaleX(float value) {
        animateProperty(8, value);
        return this;
    }

    public ViewPropertyAnimator scaleXBy(float value) {
        animatePropertyBy(8, value);
        return this;
    }

    public ViewPropertyAnimator scaleY(float value) {
        animateProperty(16, value);
        return this;
    }

    public ViewPropertyAnimator scaleYBy(float value) {
        animatePropertyBy(16, value);
        return this;
    }

    public ViewPropertyAnimator alpha(float value) {
        animateProperty(2048, value);
        return this;
    }

    public ViewPropertyAnimator alphaBy(float value) {
        animatePropertyBy(2048, value);
        return this;
    }

    public ViewPropertyAnimator withLayer() {
        this.mPendingSetupAction = new Runnable() { // from class: android.view.ViewPropertyAnimator.2
            @Override // java.lang.Runnable
            public void run() {
                ViewPropertyAnimator.this.mView.setLayerType(2, null);
                if (ViewPropertyAnimator.this.mView.isAttachedToWindow()) {
                    ViewPropertyAnimator.this.mView.buildLayer();
                }
            }
        };
        final int currentLayerType = this.mView.getLayerType();
        this.mPendingCleanupAction = new Runnable() { // from class: android.view.ViewPropertyAnimator.3
            @Override // java.lang.Runnable
            public void run() {
                ViewPropertyAnimator.this.mView.setLayerType(currentLayerType, null);
            }
        };
        if (this.mAnimatorSetupMap == null) {
            this.mAnimatorSetupMap = new HashMap<>();
        }
        if (this.mAnimatorCleanupMap == null) {
            this.mAnimatorCleanupMap = new HashMap<>();
        }
        return this;
    }

    public ViewPropertyAnimator withStartAction(Runnable runnable) {
        this.mPendingOnStartAction = runnable;
        if (runnable != null && this.mAnimatorOnStartMap == null) {
            this.mAnimatorOnStartMap = new HashMap<>();
        }
        return this;
    }

    public ViewPropertyAnimator withEndAction(Runnable runnable) {
        this.mPendingOnEndAction = runnable;
        if (runnable != null && this.mAnimatorOnEndMap == null) {
            this.mAnimatorOnEndMap = new HashMap<>();
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean hasActions() {
        return (this.mPendingSetupAction == null && this.mPendingCleanupAction == null && this.mPendingOnStartAction == null && this.mPendingOnEndAction == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startAnimation() {
        if (this.mRTBackend != null && this.mRTBackend.startAnimation(this)) {
            return;
        }
        this.mView.setHasTransientState(true);
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f);
        ArrayList<NameValuesHolder> nameValueList = (ArrayList) this.mPendingAnimations.clone();
        this.mPendingAnimations.clear();
        int propertyMask = 0;
        int propertyCount = nameValueList.size();
        for (int i = 0; i < propertyCount; i++) {
            NameValuesHolder nameValuesHolder = nameValueList.get(i);
            propertyMask |= nameValuesHolder.mNameConstant;
        }
        this.mAnimatorMap.put(animator, new PropertyBundle(propertyMask, nameValueList));
        if (this.mPendingSetupAction != null) {
            this.mAnimatorSetupMap.put(animator, this.mPendingSetupAction);
            this.mPendingSetupAction = null;
        }
        if (this.mPendingCleanupAction != null) {
            this.mAnimatorCleanupMap.put(animator, this.mPendingCleanupAction);
            this.mPendingCleanupAction = null;
        }
        if (this.mPendingOnStartAction != null) {
            this.mAnimatorOnStartMap.put(animator, this.mPendingOnStartAction);
            this.mPendingOnStartAction = null;
        }
        if (this.mPendingOnEndAction != null) {
            this.mAnimatorOnEndMap.put(animator, this.mPendingOnEndAction);
            this.mPendingOnEndAction = null;
        }
        animator.addUpdateListener(this.mAnimatorEventListener);
        animator.addListener(this.mAnimatorEventListener);
        if (this.mStartDelaySet) {
            animator.setStartDelay(this.mStartDelay);
        }
        if (this.mDurationSet) {
            animator.setDuration(this.mDuration);
        }
        if (this.mInterpolatorSet) {
            animator.setInterpolator(this.mInterpolator);
        }
        animator.start();
    }

    private synchronized void animateProperty(int constantName, float toValue) {
        float fromValue = getValue(constantName);
        float deltaValue = toValue - fromValue;
        animatePropertyBy(constantName, fromValue, deltaValue);
    }

    private synchronized void animatePropertyBy(int constantName, float byValue) {
        float fromValue = getValue(constantName);
        animatePropertyBy(constantName, fromValue, byValue);
    }

    private synchronized void animatePropertyBy(int constantName, float startValue, float byValue) {
        if (this.mAnimatorMap.size() > 0) {
            Animator animatorToCancel = null;
            Set<Animator> animatorSet = this.mAnimatorMap.keySet();
            Iterator<Animator> it = animatorSet.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Animator runningAnim = it.next();
                PropertyBundle bundle = this.mAnimatorMap.get(runningAnim);
                if (bundle.cancel(constantName) && bundle.mPropertyMask == 0) {
                    animatorToCancel = runningAnim;
                    break;
                }
            }
            if (animatorToCancel != null) {
                animatorToCancel.cancel();
            }
        }
        NameValuesHolder nameValuePair = new NameValuesHolder(constantName, startValue, byValue);
        this.mPendingAnimations.add(nameValuePair);
        this.mView.removeCallbacks(this.mAnimationStarter);
        this.mView.postOnAnimation(this.mAnimationStarter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setValue(int propertyConstant, float value) {
        View.TransformationInfo info = this.mView.mTransformationInfo;
        RenderNode renderNode = this.mView.mRenderNode;
        switch (propertyConstant) {
            case 1:
                renderNode.setTranslationX(value);
                return;
            case 2:
                renderNode.setTranslationY(value);
                return;
            case 4:
                renderNode.setTranslationZ(value);
                return;
            case 8:
                renderNode.setScaleX(value);
                return;
            case 16:
                renderNode.setScaleY(value);
                return;
            case 32:
                renderNode.setRotation(value);
                return;
            case 64:
                renderNode.setRotationX(value);
                return;
            case 128:
                renderNode.setRotationY(value);
                return;
            case 256:
                renderNode.setTranslationX(value - this.mView.mLeft);
                return;
            case 512:
                renderNode.setTranslationY(value - this.mView.mTop);
                return;
            case 1024:
                renderNode.setTranslationZ(value - renderNode.getElevation());
                return;
            case 2048:
                info.mAlpha = value;
                renderNode.setAlpha(value);
                return;
            default:
                return;
        }
    }

    private synchronized float getValue(int propertyConstant) {
        RenderNode node = this.mView.mRenderNode;
        switch (propertyConstant) {
            case 1:
                return node.getTranslationX();
            case 2:
                return node.getTranslationY();
            case 4:
                return node.getTranslationZ();
            case 8:
                return node.getScaleX();
            case 16:
                return node.getScaleY();
            case 32:
                return node.getRotation();
            case 64:
                return node.getRotationX();
            case 128:
                return node.getRotationY();
            case 256:
                return this.mView.mLeft + node.getTranslationX();
            case 512:
                return this.mView.mTop + node.getTranslationY();
            case 1024:
                return node.getElevation() + node.getTranslationZ();
            case 2048:
                return this.mView.mTransformationInfo.mAlpha;
            default:
                return 0.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class AnimatorEventListener implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
        private AnimatorEventListener() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animation) {
            if (ViewPropertyAnimator.this.mAnimatorSetupMap != null) {
                Runnable r = (Runnable) ViewPropertyAnimator.this.mAnimatorSetupMap.get(animation);
                if (r != null) {
                    r.run();
                }
                ViewPropertyAnimator.this.mAnimatorSetupMap.remove(animation);
            }
            if (ViewPropertyAnimator.this.mAnimatorOnStartMap != null) {
                Runnable r2 = (Runnable) ViewPropertyAnimator.this.mAnimatorOnStartMap.get(animation);
                if (r2 != null) {
                    r2.run();
                }
                ViewPropertyAnimator.this.mAnimatorOnStartMap.remove(animation);
            }
            if (ViewPropertyAnimator.this.mListener != null) {
                ViewPropertyAnimator.this.mListener.onAnimationStart(animation);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animation) {
            if (ViewPropertyAnimator.this.mListener != null) {
                ViewPropertyAnimator.this.mListener.onAnimationCancel(animation);
            }
            if (ViewPropertyAnimator.this.mAnimatorOnEndMap != null) {
                ViewPropertyAnimator.this.mAnimatorOnEndMap.remove(animation);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animation) {
            if (ViewPropertyAnimator.this.mListener != null) {
                ViewPropertyAnimator.this.mListener.onAnimationRepeat(animation);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animation) {
            ViewPropertyAnimator.this.mView.setHasTransientState(false);
            if (ViewPropertyAnimator.this.mAnimatorCleanupMap != null) {
                Runnable r = (Runnable) ViewPropertyAnimator.this.mAnimatorCleanupMap.get(animation);
                if (r != null) {
                    r.run();
                }
                ViewPropertyAnimator.this.mAnimatorCleanupMap.remove(animation);
            }
            if (ViewPropertyAnimator.this.mListener != null) {
                ViewPropertyAnimator.this.mListener.onAnimationEnd(animation);
            }
            if (ViewPropertyAnimator.this.mAnimatorOnEndMap != null) {
                Runnable r2 = (Runnable) ViewPropertyAnimator.this.mAnimatorOnEndMap.get(animation);
                if (r2 != null) {
                    r2.run();
                }
                ViewPropertyAnimator.this.mAnimatorOnEndMap.remove(animation);
            }
            ViewPropertyAnimator.this.mAnimatorMap.remove(animation);
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator animation) {
            PropertyBundle propertyBundle = (PropertyBundle) ViewPropertyAnimator.this.mAnimatorMap.get(animation);
            if (propertyBundle == null) {
                return;
            }
            boolean hardwareAccelerated = ViewPropertyAnimator.this.mView.isHardwareAccelerated();
            boolean alphaHandled = false;
            if (!hardwareAccelerated) {
                ViewPropertyAnimator.this.mView.invalidateParentCaches();
            }
            float fraction = animation.getAnimatedFraction();
            int propertyMask = propertyBundle.mPropertyMask;
            if ((propertyMask & 2047) != 0) {
                ViewPropertyAnimator.this.mView.invalidateViewProperty(hardwareAccelerated, false);
            }
            ArrayList<NameValuesHolder> valueList = propertyBundle.mNameValuesHolder;
            if (valueList != null) {
                int count = valueList.size();
                boolean alphaHandled2 = false;
                for (int i = 0; i < count; i++) {
                    NameValuesHolder values = valueList.get(i);
                    float value = values.mFromValue + (values.mDeltaValue * fraction);
                    if (values.mNameConstant != 2048) {
                        ViewPropertyAnimator.this.setValue(values.mNameConstant, value);
                    } else {
                        alphaHandled2 = ViewPropertyAnimator.this.mView.setAlphaNoInvalidation(value);
                    }
                }
                alphaHandled = alphaHandled2;
            }
            if ((propertyMask & 2047) != 0 && !hardwareAccelerated) {
                ViewPropertyAnimator.this.mView.mPrivateFlags |= 32;
            }
            if (alphaHandled) {
                ViewPropertyAnimator.this.mView.invalidate(true);
            } else {
                ViewPropertyAnimator.this.mView.invalidateViewProperty(false, false);
            }
            if (ViewPropertyAnimator.this.mUpdateListener != null) {
                ViewPropertyAnimator.this.mUpdateListener.onAnimationUpdate(animation);
            }
        }
    }
}
