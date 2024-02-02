package android.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.ActivityThread;
import android.app.Application;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.IntArray;
import android.util.Log;
import android.util.LongArray;
import android.util.PathParser;
import android.util.Property;
import android.view.Choreographer;
import android.view.DisplayListCanvas;
import android.view.RenderNode;
import android.view.RenderNodeAnimatorSetHelper;
import com.android.internal.R;
import com.android.internal.util.VirtualRefBasePtr;
import dalvik.annotation.optimization.FastNative;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class AnimatedVectorDrawable extends Drawable implements Animatable2 {
    private static final String ANIMATED_VECTOR = "animated-vector";
    private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
    private static final String LOGTAG = "AnimatedVectorDrawable";
    private static final String TARGET = "target";
    public protected AnimatedVectorDrawableState mAnimatedVectorState;
    private ArrayList<Animatable2.AnimationCallback> mAnimationCallbacks;
    private Animator.AnimatorListener mAnimatorListener;
    public protected VectorDrawableAnimator mAnimatorSet;
    private AnimatorSet mAnimatorSetFromXml;
    private final Drawable.Callback mCallback;
    private boolean mMutated;
    private Resources mRes;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface VectorDrawableAnimator {
        synchronized boolean canReverse();

        synchronized void end();

        synchronized void init(AnimatorSet animatorSet);

        synchronized boolean isInfinite();

        synchronized boolean isRunning();

        synchronized boolean isStarted();

        synchronized void onDraw(Canvas canvas);

        synchronized void pause();

        synchronized void removeListener(Animator.AnimatorListener animatorListener);

        synchronized void reset();

        synchronized void resume();

        synchronized void reverse();

        synchronized void setListener(Animator.AnimatorListener animatorListener);

        synchronized void start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nAddAnimator(long j, long j2, long j3, long j4, long j5, int i, int i2);

    private static native long nCreateAnimatorSet();

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native long nCreateGroupPropertyHolder(long j, int i, float f, float f2);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native long nCreatePathColorPropertyHolder(long j, int i, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native long nCreatePathDataPropertyHolder(long j, long j2, long j3);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native long nCreatePathPropertyHolder(long j, int i, float f, float f2);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native long nCreateRootAlphaPropertyHolder(long j, float f, float f2);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nEnd(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nReset(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nReverse(long j, VectorDrawableAnimatorRT vectorDrawableAnimatorRT, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nSetPropertyHolderData(long j, float[] fArr, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nSetPropertyHolderData(long j, int[] iArr, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nSetVectorDrawableTarget(long j, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nStart(long j, VectorDrawableAnimatorRT vectorDrawableAnimatorRT, int i);

    static /* synthetic */ boolean access$400() {
        return shouldIgnoreInvalidAnimation();
    }

    static /* synthetic */ long access$800() {
        return nCreateAnimatorSet();
    }

    public AnimatedVectorDrawable() {
        this(null, null);
    }

    private synchronized AnimatedVectorDrawable(AnimatedVectorDrawableState state, Resources res) {
        this.mAnimatorSetFromXml = null;
        this.mAnimationCallbacks = null;
        this.mAnimatorListener = null;
        this.mCallback = new Drawable.Callback() { // from class: android.graphics.drawable.AnimatedVectorDrawable.1
            @Override // android.graphics.drawable.Drawable.Callback
            public void invalidateDrawable(Drawable who) {
                AnimatedVectorDrawable.this.invalidateSelf();
            }

            @Override // android.graphics.drawable.Drawable.Callback
            public void scheduleDrawable(Drawable who, Runnable what, long when) {
                AnimatedVectorDrawable.this.scheduleSelf(what, when);
            }

            @Override // android.graphics.drawable.Drawable.Callback
            public void unscheduleDrawable(Drawable who, Runnable what) {
                AnimatedVectorDrawable.this.unscheduleSelf(what);
            }
        };
        this.mAnimatedVectorState = new AnimatedVectorDrawableState(state, this.mCallback, res);
        this.mAnimatorSet = new VectorDrawableAnimatorRT(this);
        this.mRes = res;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mAnimatedVectorState = new AnimatedVectorDrawableState(this.mAnimatedVectorState, this.mCallback, this.mRes);
            this.mMutated = true;
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public synchronized void clearMutated() {
        super.clearMutated();
        if (this.mAnimatedVectorState.mVectorDrawable != null) {
            this.mAnimatedVectorState.mVectorDrawable.clearMutated();
        }
        this.mMutated = false;
    }

    private static synchronized boolean shouldIgnoreInvalidAnimation() {
        Application app = ActivityThread.currentApplication();
        if (app == null || app.getApplicationInfo() == null || app.getApplicationInfo().targetSdkVersion < 24) {
            return true;
        }
        return false;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        this.mAnimatedVectorState.mChangingConfigurations = getChangingConfigurations();
        return this.mAnimatedVectorState;
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mAnimatedVectorState.getChangingConfigurations();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (!canvas.isHardwareAccelerated() && (this.mAnimatorSet instanceof VectorDrawableAnimatorRT) && !this.mAnimatorSet.isRunning() && ((VectorDrawableAnimatorRT) this.mAnimatorSet).mPendingAnimationActions.size() > 0) {
            fallbackOntoUI();
        }
        this.mAnimatorSet.onDraw(canvas);
        this.mAnimatedVectorState.mVectorDrawable.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect bounds) {
        this.mAnimatedVectorState.mVectorDrawable.setBounds(bounds);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] state) {
        return this.mAnimatedVectorState.mVectorDrawable.setState(state);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onLevelChange(int level) {
        return this.mAnimatedVectorState.mVectorDrawable.setLevel(level);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return this.mAnimatedVectorState.mVectorDrawable.setLayoutDirection(layoutDirection);
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mAnimatedVectorState.mVectorDrawable.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.mAnimatedVectorState.mVectorDrawable.setAlpha(alpha);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mAnimatedVectorState.mVectorDrawable.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.mAnimatedVectorState.mVectorDrawable.getColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList tint) {
        this.mAnimatedVectorState.mVectorDrawable.setTintList(tint);
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspot(float x, float y) {
        this.mAnimatedVectorState.mVectorDrawable.setHotspot(x, y);
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        this.mAnimatedVectorState.mVectorDrawable.setHotspotBounds(left, top, right, bottom);
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode tintMode) {
        this.mAnimatedVectorState.mVectorDrawable.setTintMode(tintMode);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean visible, boolean restart) {
        if (this.mAnimatorSet.isInfinite() && this.mAnimatorSet.isStarted()) {
            if (visible) {
                this.mAnimatorSet.resume();
            } else {
                this.mAnimatorSet.pause();
            }
        }
        this.mAnimatedVectorState.mVectorDrawable.setVisible(visible, restart);
        return super.setVisible(visible, restart);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return this.mAnimatedVectorState.mVectorDrawable.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        this.mAnimatedVectorState.mVectorDrawable.getOutline(outline);
    }

    private protected Insets getOpticalInsets() {
        return this.mAnimatedVectorState.mVectorDrawable.getOpticalInsets();
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        AnimatedVectorDrawableState state = this.mAnimatedVectorState;
        int eventType = parser.getEventType();
        float pathErrorScale = 1.0f;
        int innerDepth = parser.getDepth() + 1;
        while (true) {
            if (eventType == 1 || (parser.getDepth() < innerDepth && eventType == 3)) {
                break;
            }
            if (eventType == 2) {
                String tagName = parser.getName();
                if (ANIMATED_VECTOR.equals(tagName)) {
                    TypedArray a = obtainAttributes(res, theme, attrs, R.styleable.AnimatedVectorDrawable);
                    int drawableRes = a.getResourceId(0, 0);
                    if (drawableRes != 0) {
                        VectorDrawable vectorDrawable = (VectorDrawable) res.getDrawable(drawableRes, theme).mutate();
                        vectorDrawable.setAllowCaching(false);
                        vectorDrawable.setCallback(this.mCallback);
                        pathErrorScale = vectorDrawable.getPixelSize();
                        if (state.mVectorDrawable != null) {
                            state.mVectorDrawable.setCallback(null);
                        }
                        state.mVectorDrawable = vectorDrawable;
                    }
                    a.recycle();
                } else if (TARGET.equals(tagName)) {
                    TypedArray a2 = obtainAttributes(res, theme, attrs, R.styleable.AnimatedVectorDrawableTarget);
                    String target = a2.getString(0);
                    int animResId = a2.getResourceId(1, 0);
                    if (animResId != 0) {
                        if (theme != null) {
                            Animator animator = AnimatorInflater.loadAnimator(res, theme, animResId, pathErrorScale);
                            updateAnimatorProperty(animator, target, state.mVectorDrawable, state.mShouldIgnoreInvalidAnim);
                            state.addTargetAnimator(target, animator);
                        } else {
                            state.addPendingAnimator(animResId, pathErrorScale, target);
                        }
                    }
                    a2.recycle();
                }
            }
            eventType = parser.next();
        }
        this.mRes = state.mPendingAnims != null ? res : null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void updateAnimatorProperty(Animator animator, String targetName, VectorDrawable vectorDrawable, boolean ignoreInvalidAnim) {
        if (!(animator instanceof ObjectAnimator)) {
            if (animator instanceof AnimatorSet) {
                Iterator<Animator> it = ((AnimatorSet) animator).getChildAnimations().iterator();
                while (it.hasNext()) {
                    Animator anim = it.next();
                    updateAnimatorProperty(anim, targetName, vectorDrawable, ignoreInvalidAnim);
                }
                return;
            }
            return;
        }
        PropertyValuesHolder[] holders = ((ObjectAnimator) animator).getValues();
        for (PropertyValuesHolder pvh : holders) {
            String propertyName = pvh.getPropertyName();
            Object targetNameObj = vectorDrawable.getTargetByName(targetName);
            Property property = null;
            if (targetNameObj instanceof VectorDrawable.VObject) {
                property = ((VectorDrawable.VObject) targetNameObj).getProperty(propertyName);
            } else if (targetNameObj instanceof VectorDrawable.VectorDrawableState) {
                property = ((VectorDrawable.VectorDrawableState) targetNameObj).getProperty(propertyName);
            }
            if (property != null) {
                if (containsSameValueType(pvh, property)) {
                    pvh.setProperty(property);
                } else if (!ignoreInvalidAnim) {
                    throw new RuntimeException("Wrong valueType for Property: " + propertyName + ".  Expected type: " + property.getType().toString() + ". Actual type defined in resources: " + pvh.getValueType().toString());
                }
            }
        }
    }

    private static synchronized boolean containsSameValueType(PropertyValuesHolder holder, Property property) {
        Class type1 = holder.getValueType();
        Class type2 = property.getType();
        return (type1 == Float.TYPE || type1 == Float.class) ? type2 == Float.TYPE || type2 == Float.class : (type1 == Integer.TYPE || type1 == Integer.class) ? type2 == Integer.TYPE || type2 == Integer.class : type1 == type2;
    }

    private protected void forceAnimationOnUI() {
        if (this.mAnimatorSet instanceof VectorDrawableAnimatorRT) {
            VectorDrawableAnimatorRT animator = (VectorDrawableAnimatorRT) this.mAnimatorSet;
            if (animator.isRunning()) {
                throw new UnsupportedOperationException("Cannot force Animated Vector Drawable to run on UI thread when the animation has started on RenderThread.");
            }
            fallbackOntoUI();
        }
    }

    private synchronized void fallbackOntoUI() {
        if (this.mAnimatorSet instanceof VectorDrawableAnimatorRT) {
            VectorDrawableAnimatorRT oldAnim = (VectorDrawableAnimatorRT) this.mAnimatorSet;
            this.mAnimatorSet = new VectorDrawableAnimatorUI(this);
            if (this.mAnimatorSetFromXml != null) {
                this.mAnimatorSet.init(this.mAnimatorSetFromXml);
            }
            if (oldAnim.mListener != null) {
                this.mAnimatorSet.setListener(oldAnim.mListener);
            }
            oldAnim.transferPendingActions(this.mAnimatorSet);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return (this.mAnimatedVectorState != null && this.mAnimatedVectorState.canApplyTheme()) || super.canApplyTheme();
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme t) {
        super.applyTheme(t);
        VectorDrawable vectorDrawable = this.mAnimatedVectorState.mVectorDrawable;
        if (vectorDrawable != null && vectorDrawable.canApplyTheme()) {
            vectorDrawable.applyTheme(t);
        }
        if (t != null) {
            this.mAnimatedVectorState.inflatePendingAnimators(t.getResources(), t);
        }
        if (this.mAnimatedVectorState.mPendingAnims == null) {
            this.mRes = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AnimatedVectorDrawableState extends Drawable.ConstantState {
        ArrayList<Animator> mAnimators;
        int mChangingConfigurations;
        ArrayList<PendingAnimator> mPendingAnims;
        private final boolean mShouldIgnoreInvalidAnim = AnimatedVectorDrawable.access$400();
        ArrayMap<Animator, String> mTargetNameMap;
        VectorDrawable mVectorDrawable;

        public synchronized AnimatedVectorDrawableState(AnimatedVectorDrawableState copy, Drawable.Callback owner, Resources res) {
            if (copy != null) {
                this.mChangingConfigurations = copy.mChangingConfigurations;
                if (copy.mVectorDrawable != null) {
                    Drawable.ConstantState cs = copy.mVectorDrawable.getConstantState();
                    if (res != null) {
                        this.mVectorDrawable = (VectorDrawable) cs.newDrawable(res);
                    } else {
                        this.mVectorDrawable = (VectorDrawable) cs.newDrawable();
                    }
                    this.mVectorDrawable = (VectorDrawable) this.mVectorDrawable.mutate();
                    this.mVectorDrawable.setCallback(owner);
                    this.mVectorDrawable.setLayoutDirection(copy.mVectorDrawable.getLayoutDirection());
                    this.mVectorDrawable.setBounds(copy.mVectorDrawable.getBounds());
                    this.mVectorDrawable.setAllowCaching(false);
                }
                if (copy.mAnimators != null) {
                    this.mAnimators = new ArrayList<>(copy.mAnimators);
                }
                if (copy.mTargetNameMap != null) {
                    this.mTargetNameMap = new ArrayMap<>(copy.mTargetNameMap);
                }
                if (copy.mPendingAnims != null) {
                    this.mPendingAnims = new ArrayList<>(copy.mPendingAnims);
                    return;
                }
                return;
            }
            this.mVectorDrawable = new VectorDrawable();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return (this.mVectorDrawable != null && this.mVectorDrawable.canApplyTheme()) || this.mPendingAnims != null || super.canApplyTheme();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new AnimatedVectorDrawable(this, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources res) {
            return new AnimatedVectorDrawable(this, res);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }

        public synchronized void addPendingAnimator(int resId, float pathErrorScale, String target) {
            if (this.mPendingAnims == null) {
                this.mPendingAnims = new ArrayList<>(1);
            }
            this.mPendingAnims.add(new PendingAnimator(resId, pathErrorScale, target));
        }

        public synchronized void addTargetAnimator(String targetName, Animator animator) {
            if (this.mAnimators == null) {
                this.mAnimators = new ArrayList<>(1);
                this.mTargetNameMap = new ArrayMap<>(1);
            }
            this.mAnimators.add(animator);
            this.mTargetNameMap.put(animator, targetName);
        }

        public synchronized void prepareLocalAnimators(AnimatorSet animatorSet, Resources res) {
            if (this.mPendingAnims != null) {
                if (res != null) {
                    inflatePendingAnimators(res, null);
                } else {
                    Log.e(AnimatedVectorDrawable.LOGTAG, "Failed to load animators. Either the AnimatedVectorDrawable must be created using a Resources object or applyTheme() must be called with a non-null Theme object.");
                }
                this.mPendingAnims = null;
            }
            int count = this.mAnimators == null ? 0 : this.mAnimators.size();
            if (count > 0) {
                Animator firstAnim = prepareLocalAnimator(0);
                AnimatorSet.Builder builder = animatorSet.play(firstAnim);
                for (int i = 1; i < count; i++) {
                    Animator nextAnim = prepareLocalAnimator(i);
                    builder.with(nextAnim);
                }
            }
        }

        private synchronized Animator prepareLocalAnimator(int index) {
            Animator animator = this.mAnimators.get(index);
            Animator localAnimator = animator.mo0clone();
            String targetName = this.mTargetNameMap.get(animator);
            Object target = this.mVectorDrawable.getTargetByName(targetName);
            if (!this.mShouldIgnoreInvalidAnim) {
                if (target == null) {
                    throw new IllegalStateException("Target with the name \"" + targetName + "\" cannot be found in the VectorDrawable to be animated.");
                } else if (!(target instanceof VectorDrawable.VectorDrawableState) && !(target instanceof VectorDrawable.VObject)) {
                    throw new UnsupportedOperationException("Target should be either VGroup, VPath, or ConstantState, " + target.getClass() + " is not supported");
                }
            }
            localAnimator.setTarget(target);
            return localAnimator;
        }

        public synchronized void inflatePendingAnimators(Resources res, Resources.Theme t) {
            ArrayList<PendingAnimator> pendingAnims = this.mPendingAnims;
            if (pendingAnims != null) {
                this.mPendingAnims = null;
                int count = pendingAnims.size();
                for (int i = 0; i < count; i++) {
                    PendingAnimator pendingAnimator = pendingAnims.get(i);
                    Animator animator = pendingAnimator.newInstance(res, t);
                    AnimatedVectorDrawable.updateAnimatorProperty(animator, pendingAnimator.target, this.mVectorDrawable, this.mShouldIgnoreInvalidAnim);
                    addTargetAnimator(pendingAnimator.target, animator);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class PendingAnimator {
            public final int animResId;
            public final float pathErrorScale;
            public final String target;

            public synchronized PendingAnimator(int animResId, float pathErrorScale, String target) {
                this.animResId = animResId;
                this.pathErrorScale = pathErrorScale;
                this.target = target;
            }

            public synchronized Animator newInstance(Resources res, Resources.Theme theme) {
                return AnimatorInflater.loadAnimator(res, theme, this.animResId, this.pathErrorScale);
            }
        }
    }

    @Override // android.graphics.drawable.Animatable
    public boolean isRunning() {
        return this.mAnimatorSet.isRunning();
    }

    public void reset() {
        ensureAnimatorSet();
        this.mAnimatorSet.reset();
    }

    @Override // android.graphics.drawable.Animatable
    public void start() {
        ensureAnimatorSet();
        this.mAnimatorSet.start();
    }

    private synchronized void ensureAnimatorSet() {
        if (this.mAnimatorSetFromXml == null) {
            this.mAnimatorSetFromXml = new AnimatorSet();
            this.mAnimatedVectorState.prepareLocalAnimators(this.mAnimatorSetFromXml, this.mRes);
            this.mAnimatorSet.init(this.mAnimatorSetFromXml);
            this.mRes = null;
        }
    }

    @Override // android.graphics.drawable.Animatable
    public void stop() {
        this.mAnimatorSet.end();
    }

    public synchronized void reverse() {
        ensureAnimatorSet();
        if (!canReverse()) {
            Log.w(LOGTAG, "AnimatedVectorDrawable can't reverse()");
        } else {
            this.mAnimatorSet.reverse();
        }
    }

    public synchronized boolean canReverse() {
        return this.mAnimatorSet.canReverse();
    }

    @Override // android.graphics.drawable.Animatable2
    public void registerAnimationCallback(Animatable2.AnimationCallback callback) {
        if (callback == null) {
            return;
        }
        if (this.mAnimationCallbacks == null) {
            this.mAnimationCallbacks = new ArrayList<>();
        }
        this.mAnimationCallbacks.add(callback);
        if (this.mAnimatorListener == null) {
            this.mAnimatorListener = new AnimatorListenerAdapter() { // from class: android.graphics.drawable.AnimatedVectorDrawable.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                    ArrayList<Animatable2.AnimationCallback> tmpCallbacks = new ArrayList<>(AnimatedVectorDrawable.this.mAnimationCallbacks);
                    int size = tmpCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        tmpCallbacks.get(i).onAnimationStart(AnimatedVectorDrawable.this);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    ArrayList<Animatable2.AnimationCallback> tmpCallbacks = new ArrayList<>(AnimatedVectorDrawable.this.mAnimationCallbacks);
                    int size = tmpCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        tmpCallbacks.get(i).onAnimationEnd(AnimatedVectorDrawable.this);
                    }
                }
            };
        }
        this.mAnimatorSet.setListener(this.mAnimatorListener);
    }

    private synchronized void removeAnimatorSetListener() {
        if (this.mAnimatorListener != null) {
            this.mAnimatorSet.removeListener(this.mAnimatorListener);
            this.mAnimatorListener = null;
        }
    }

    @Override // android.graphics.drawable.Animatable2
    public boolean unregisterAnimationCallback(Animatable2.AnimationCallback callback) {
        if (this.mAnimationCallbacks == null || callback == null) {
            return false;
        }
        boolean removed = this.mAnimationCallbacks.remove(callback);
        if (this.mAnimationCallbacks.size() == 0) {
            removeAnimatorSetListener();
        }
        return removed;
    }

    @Override // android.graphics.drawable.Animatable2
    public void clearAnimationCallbacks() {
        removeAnimatorSetListener();
        if (this.mAnimationCallbacks == null) {
            return;
        }
        this.mAnimationCallbacks.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class VectorDrawableAnimatorUI implements VectorDrawableAnimator {
        private final Drawable mDrawable;
        private AnimatorSet mSet = null;
        private ArrayList<Animator.AnimatorListener> mListenerArray = null;
        private boolean mIsInfinite = false;

        synchronized VectorDrawableAnimatorUI(AnimatedVectorDrawable drawable) {
            this.mDrawable = drawable;
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void init(AnimatorSet set) {
            if (this.mSet != null) {
                throw new UnsupportedOperationException("VectorDrawableAnimator cannot be re-initialized");
            }
            this.mSet = set.mo0clone();
            int i = 0;
            this.mIsInfinite = this.mSet.getTotalDuration() == -1;
            if (this.mListenerArray == null || this.mListenerArray.isEmpty()) {
                return;
            }
            while (true) {
                int i2 = i;
                if (i2 < this.mListenerArray.size()) {
                    this.mSet.addListener(this.mListenerArray.get(i2));
                    i = i2 + 1;
                } else {
                    this.mListenerArray.clear();
                    this.mListenerArray = null;
                    return;
                }
            }
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void start() {
            if (this.mSet == null || this.mSet.isStarted()) {
                return;
            }
            this.mSet.start();
            invalidateOwningView();
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void end() {
            if (this.mSet == null) {
                return;
            }
            this.mSet.end();
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void reset() {
            if (this.mSet == null) {
                return;
            }
            start();
            this.mSet.cancel();
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void reverse() {
            if (this.mSet == null) {
                return;
            }
            this.mSet.reverse();
            invalidateOwningView();
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized boolean canReverse() {
            return this.mSet != null && this.mSet.canReverse();
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void setListener(Animator.AnimatorListener listener) {
            if (this.mSet == null) {
                if (this.mListenerArray == null) {
                    this.mListenerArray = new ArrayList<>();
                }
                this.mListenerArray.add(listener);
                return;
            }
            this.mSet.addListener(listener);
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void removeListener(Animator.AnimatorListener listener) {
            if (this.mSet == null) {
                if (this.mListenerArray == null) {
                    return;
                }
                this.mListenerArray.remove(listener);
                return;
            }
            this.mSet.removeListener(listener);
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void onDraw(Canvas canvas) {
            if (this.mSet != null && this.mSet.isStarted()) {
                invalidateOwningView();
            }
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized boolean isStarted() {
            return this.mSet != null && this.mSet.isStarted();
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized boolean isRunning() {
            return this.mSet != null && this.mSet.isRunning();
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized boolean isInfinite() {
            return this.mIsInfinite;
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void pause() {
            if (this.mSet == null) {
                return;
            }
            this.mSet.pause();
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void resume() {
            if (this.mSet == null) {
                return;
            }
            this.mSet.resume();
        }

        private synchronized void invalidateOwningView() {
            this.mDrawable.invalidateSelf();
        }
    }

    /* loaded from: classes.dex */
    public static class VectorDrawableAnimatorRT implements VectorDrawableAnimator {
        private static final int END_ANIMATION = 4;
        private static final int MAX_SAMPLE_POINTS = 300;
        private static final int RESET_ANIMATION = 3;
        private static final int REVERSE_ANIMATION = 2;
        private static final int START_ANIMATION = 1;
        private final AnimatedVectorDrawable mDrawable;
        private long mSetPtr;
        private final VirtualRefBasePtr mSetRefBasePtr;
        private Animator.AnimatorListener mListener = null;
        private final LongArray mStartDelays = new LongArray();
        private PropertyValuesHolder.PropertyValues mTmpValues = new PropertyValuesHolder.PropertyValues();
        private boolean mContainsSequentialAnimators = false;
        private boolean mStarted = false;
        private boolean mInitialized = false;
        private boolean mIsReversible = false;
        private boolean mIsInfinite = false;
        private WeakReference<RenderNode> mLastSeenTarget = null;
        private int mLastListenerId = 0;
        private final IntArray mPendingAnimationActions = new IntArray();

        synchronized VectorDrawableAnimatorRT(AnimatedVectorDrawable drawable) {
            this.mSetPtr = 0L;
            this.mDrawable = drawable;
            this.mSetPtr = AnimatedVectorDrawable.access$800();
            this.mSetRefBasePtr = new VirtualRefBasePtr(this.mSetPtr);
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void init(AnimatorSet set) {
            if (this.mInitialized) {
                throw new UnsupportedOperationException("VectorDrawableAnimator cannot be re-initialized");
            }
            parseAnimatorSet(set, 0L);
            long vectorDrawableTreePtr = this.mDrawable.mAnimatedVectorState.mVectorDrawable.getNativeTree();
            AnimatedVectorDrawable.nSetVectorDrawableTarget(this.mSetPtr, vectorDrawableTreePtr);
            this.mInitialized = true;
            this.mIsInfinite = set.getTotalDuration() == -1;
            this.mIsReversible = true;
            if (this.mContainsSequentialAnimators) {
                this.mIsReversible = false;
                return;
            }
            for (int i = 0; i < this.mStartDelays.size(); i++) {
                if (this.mStartDelays.get(i) > 0) {
                    this.mIsReversible = false;
                    return;
                }
            }
        }

        private synchronized void parseAnimatorSet(AnimatorSet set, long startTime) {
            ArrayList<Animator> animators = set.getChildAnimations();
            boolean playTogether = set.shouldPlayTogether();
            for (int i = 0; i < animators.size(); i++) {
                Animator animator = animators.get(i);
                if (animator instanceof AnimatorSet) {
                    parseAnimatorSet((AnimatorSet) animator, startTime);
                } else if (animator instanceof ObjectAnimator) {
                    createRTAnimator((ObjectAnimator) animator, startTime);
                }
                if (!playTogether) {
                    startTime += animator.getTotalDuration();
                    this.mContainsSequentialAnimators = true;
                }
            }
        }

        private synchronized void createRTAnimator(ObjectAnimator animator, long startTime) {
            PropertyValuesHolder[] values = animator.getValues();
            Object target = animator.getTarget();
            if (target instanceof VectorDrawable.VGroup) {
                createRTAnimatorForGroup(values, animator, (VectorDrawable.VGroup) target, startTime);
            } else if (target instanceof VectorDrawable.VPath) {
                for (PropertyValuesHolder propertyValuesHolder : values) {
                    propertyValuesHolder.getPropertyValues(this.mTmpValues);
                    if ((this.mTmpValues.endValue instanceof PathParser.PathData) && this.mTmpValues.propertyName.equals("pathData")) {
                        createRTAnimatorForPath(animator, (VectorDrawable.VPath) target, startTime);
                    } else if (!(target instanceof VectorDrawable.VFullPath)) {
                        if (!this.mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                            throw new IllegalArgumentException("ClipPath only supports PathData property");
                        }
                    } else {
                        createRTAnimatorForFullPath(animator, (VectorDrawable.VFullPath) target, startTime);
                    }
                }
            } else if (target instanceof VectorDrawable.VectorDrawableState) {
                createRTAnimatorForRootGroup(values, animator, (VectorDrawable.VectorDrawableState) target, startTime);
            }
        }

        private synchronized void createRTAnimatorForGroup(PropertyValuesHolder[] values, ObjectAnimator animator, VectorDrawable.VGroup target, long startTime) {
            long nativePtr = target.getNativePtr();
            int i = 0;
            while (true) {
                int i2 = i;
                int i3 = values.length;
                if (i2 < i3) {
                    values[i2].getPropertyValues(this.mTmpValues);
                    int propertyId = VectorDrawable.VGroup.getPropertyIndex(this.mTmpValues.propertyName);
                    if ((this.mTmpValues.type == Float.class || this.mTmpValues.type == Float.TYPE) && propertyId >= 0) {
                        long propertyPtr = AnimatedVectorDrawable.nCreateGroupPropertyHolder(nativePtr, propertyId, ((Float) this.mTmpValues.startValue).floatValue(), ((Float) this.mTmpValues.endValue).floatValue());
                        if (this.mTmpValues.dataSource != null) {
                            float[] dataPoints = createFloatDataPoints(this.mTmpValues.dataSource, animator.getDuration());
                            AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints, dataPoints.length);
                        }
                        createNativeChildAnimator(propertyPtr, startTime, animator);
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }

        private synchronized void createRTAnimatorForPath(ObjectAnimator animator, VectorDrawable.VPath target, long startTime) {
            long nativePtr = target.getNativePtr();
            long startPathDataPtr = ((PathParser.PathData) this.mTmpValues.startValue).getNativePtr();
            long endPathDataPtr = ((PathParser.PathData) this.mTmpValues.endValue).getNativePtr();
            long propertyPtr = AnimatedVectorDrawable.nCreatePathDataPropertyHolder(nativePtr, startPathDataPtr, endPathDataPtr);
            createNativeChildAnimator(propertyPtr, startTime, animator);
        }

        private synchronized void createRTAnimatorForFullPath(ObjectAnimator animator, VectorDrawable.VFullPath target, long startTime) {
            long propertyPtr;
            int propertyId = target.getPropertyIndex(this.mTmpValues.propertyName);
            long nativePtr = target.getNativePtr();
            if (this.mTmpValues.type == Float.class || this.mTmpValues.type == Float.TYPE) {
                if (propertyId >= 0) {
                    propertyPtr = AnimatedVectorDrawable.nCreatePathPropertyHolder(nativePtr, propertyId, ((Float) this.mTmpValues.startValue).floatValue(), ((Float) this.mTmpValues.endValue).floatValue());
                    if (this.mTmpValues.dataSource != null) {
                        float[] dataPoints = createFloatDataPoints(this.mTmpValues.dataSource, animator.getDuration());
                        AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints, dataPoints.length);
                    }
                } else if (this.mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                    return;
                } else {
                    throw new IllegalArgumentException("Property: " + this.mTmpValues.propertyName + " is not supported for FullPath");
                }
            } else if (this.mTmpValues.type == Integer.class || this.mTmpValues.type == Integer.TYPE) {
                propertyPtr = AnimatedVectorDrawable.nCreatePathColorPropertyHolder(nativePtr, propertyId, ((Integer) this.mTmpValues.startValue).intValue(), ((Integer) this.mTmpValues.endValue).intValue());
                if (this.mTmpValues.dataSource != null) {
                    int[] dataPoints2 = createIntDataPoints(this.mTmpValues.dataSource, animator.getDuration());
                    AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints2, dataPoints2.length);
                }
            } else if (this.mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                return;
            } else {
                throw new UnsupportedOperationException("Unsupported type: " + this.mTmpValues.type + ". Only float, int or PathData value is supported for Paths.");
            }
            createNativeChildAnimator(propertyPtr, startTime, animator);
        }

        private synchronized void createRTAnimatorForRootGroup(PropertyValuesHolder[] values, ObjectAnimator animator, VectorDrawable.VectorDrawableState target, long startTime) {
            long nativePtr = target.getNativeRenderer();
            if (!animator.getPropertyName().equals("alpha")) {
                if (this.mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
                    return;
                }
                throw new UnsupportedOperationException("Only alpha is supported for root group");
            }
            Float startValue = null;
            Float endValue = null;
            int i = 0;
            while (true) {
                if (i >= values.length) {
                    break;
                }
                values[i].getPropertyValues(this.mTmpValues);
                if (!this.mTmpValues.propertyName.equals("alpha")) {
                    i++;
                } else {
                    startValue = (Float) this.mTmpValues.startValue;
                    endValue = (Float) this.mTmpValues.endValue;
                    break;
                }
            }
            Float startValue2 = startValue;
            Float endValue2 = endValue;
            if (startValue2 != null || endValue2 != null) {
                long propertyPtr = AnimatedVectorDrawable.nCreateRootAlphaPropertyHolder(nativePtr, startValue2.floatValue(), endValue2.floatValue());
                if (this.mTmpValues.dataSource != null) {
                    float[] dataPoints = createFloatDataPoints(this.mTmpValues.dataSource, animator.getDuration());
                    AnimatedVectorDrawable.nSetPropertyHolderData(propertyPtr, dataPoints, dataPoints.length);
                }
                createNativeChildAnimator(propertyPtr, startTime, animator);
            } else if (this.mDrawable.mAnimatedVectorState.mShouldIgnoreInvalidAnim) {
            } else {
                throw new UnsupportedOperationException("No alpha values are specified");
            }
        }

        private static synchronized int getFrameCount(long duration) {
            long frameIntervalNanos = Choreographer.getInstance().getFrameIntervalNanos();
            int animIntervalMs = (int) (frameIntervalNanos / 1000000);
            int numAnimFrames = Math.max(2, (int) Math.ceil(duration / animIntervalMs));
            if (numAnimFrames > 300) {
                Log.w(AnimatedVectorDrawable.LOGTAG, "Duration for the animation is too long :" + duration + ", the animation will subsample the keyframe or path data.");
                return 300;
            }
            return numAnimFrames;
        }

        private static synchronized float[] createFloatDataPoints(PropertyValuesHolder.PropertyValues.DataSource dataSource, long duration) {
            int numAnimFrames = getFrameCount(duration);
            float[] values = new float[numAnimFrames];
            float lastFrame = numAnimFrames - 1;
            for (int i = 0; i < numAnimFrames; i++) {
                float fraction = i / lastFrame;
                values[i] = ((Float) dataSource.getValueAtFraction(fraction)).floatValue();
            }
            return values;
        }

        private static synchronized int[] createIntDataPoints(PropertyValuesHolder.PropertyValues.DataSource dataSource, long duration) {
            int numAnimFrames = getFrameCount(duration);
            int[] values = new int[numAnimFrames];
            float lastFrame = numAnimFrames - 1;
            for (int i = 0; i < numAnimFrames; i++) {
                float fraction = i / lastFrame;
                values[i] = ((Integer) dataSource.getValueAtFraction(fraction)).intValue();
            }
            return values;
        }

        private synchronized void createNativeChildAnimator(long propertyPtr, long extraDelay, ObjectAnimator animator) {
            long duration = animator.getDuration();
            int repeatCount = animator.getRepeatCount();
            TimeInterpolator interpolator = animator.getInterpolator();
            long nativeInterpolator = RenderNodeAnimatorSetHelper.createNativeInterpolator(interpolator, duration);
            long startDelay = ((float) (extraDelay + animator.getStartDelay())) * ValueAnimator.getDurationScale();
            long duration2 = ((float) duration) * ValueAnimator.getDurationScale();
            this.mStartDelays.add(startDelay);
            AnimatedVectorDrawable.nAddAnimator(this.mSetPtr, propertyPtr, nativeInterpolator, startDelay, duration2, repeatCount, animator.getRepeatMode());
        }

        protected synchronized void recordLastSeenTarget(DisplayListCanvas canvas) {
            RenderNode node = RenderNodeAnimatorSetHelper.getTarget(canvas);
            this.mLastSeenTarget = new WeakReference<>(node);
            if ((this.mInitialized || this.mPendingAnimationActions.size() > 0) && useTarget(node)) {
                for (int i = 0; i < this.mPendingAnimationActions.size(); i++) {
                    handlePendingAction(this.mPendingAnimationActions.get(i));
                }
                this.mPendingAnimationActions.clear();
            }
        }

        private synchronized void handlePendingAction(int pendingAnimationAction) {
            if (pendingAnimationAction == 1) {
                startAnimation();
            } else if (pendingAnimationAction == 2) {
                reverseAnimation();
            } else if (pendingAnimationAction == 3) {
                resetAnimation();
            } else if (pendingAnimationAction == 4) {
                endAnimation();
            } else {
                throw new UnsupportedOperationException("Animation action " + pendingAnimationAction + "is not supported");
            }
        }

        private synchronized boolean useLastSeenTarget() {
            if (this.mLastSeenTarget != null) {
                RenderNode target = this.mLastSeenTarget.get();
                return useTarget(target);
            }
            return false;
        }

        private synchronized boolean useTarget(RenderNode target) {
            if (target != null && target.isAttached()) {
                target.registerVectorDrawableAnimator(this);
                return true;
            }
            return false;
        }

        private synchronized void invalidateOwningView() {
            this.mDrawable.invalidateSelf();
        }

        private synchronized void addPendingAction(int pendingAnimationAction) {
            invalidateOwningView();
            this.mPendingAnimationActions.add(pendingAnimationAction);
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void start() {
            if (!this.mInitialized) {
                return;
            }
            if (useLastSeenTarget()) {
                startAnimation();
            } else {
                addPendingAction(1);
            }
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void end() {
            if (!this.mInitialized) {
                return;
            }
            if (useLastSeenTarget()) {
                endAnimation();
            } else {
                addPendingAction(4);
            }
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void reset() {
            if (!this.mInitialized) {
                return;
            }
            if (useLastSeenTarget()) {
                resetAnimation();
            } else {
                addPendingAction(3);
            }
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void reverse() {
            if (!this.mIsReversible || !this.mInitialized) {
                return;
            }
            if (useLastSeenTarget()) {
                reverseAnimation();
            } else {
                addPendingAction(2);
            }
        }

        private synchronized void startAnimation() {
            this.mStarted = true;
            long j = this.mSetPtr;
            int i = this.mLastListenerId + 1;
            this.mLastListenerId = i;
            AnimatedVectorDrawable.nStart(j, this, i);
            invalidateOwningView();
            if (this.mListener != null) {
                this.mListener.onAnimationStart(null);
            }
        }

        private synchronized void endAnimation() {
            AnimatedVectorDrawable.nEnd(this.mSetPtr);
            invalidateOwningView();
        }

        private synchronized void resetAnimation() {
            AnimatedVectorDrawable.nReset(this.mSetPtr);
            invalidateOwningView();
        }

        private synchronized void reverseAnimation() {
            this.mStarted = true;
            long j = this.mSetPtr;
            int i = this.mLastListenerId + 1;
            this.mLastListenerId = i;
            AnimatedVectorDrawable.nReverse(j, this, i);
            invalidateOwningView();
            if (this.mListener != null) {
                this.mListener.onAnimationStart(null);
            }
        }

        public synchronized long getAnimatorNativePtr() {
            return this.mSetPtr;
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized boolean canReverse() {
            return this.mIsReversible;
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized boolean isStarted() {
            return this.mStarted;
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized boolean isRunning() {
            if (!this.mInitialized) {
                return false;
            }
            return this.mStarted;
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void setListener(Animator.AnimatorListener listener) {
            this.mListener = listener;
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void removeListener(Animator.AnimatorListener listener) {
            this.mListener = null;
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void onDraw(Canvas canvas) {
            if (canvas.isHardwareAccelerated()) {
                recordLastSeenTarget((DisplayListCanvas) canvas);
            }
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized boolean isInfinite() {
            return this.mIsInfinite;
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void pause() {
        }

        @Override // android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimator
        public synchronized void resume() {
        }

        private synchronized void onAnimationEnd(int listenerId) {
            if (listenerId != this.mLastListenerId) {
                return;
            }
            this.mStarted = false;
            invalidateOwningView();
            if (this.mListener != null) {
                this.mListener.onAnimationEnd(null);
            }
        }

        public protected static void callOnFinished(VectorDrawableAnimatorRT set, int id) {
            set.onAnimationEnd(id);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void transferPendingActions(VectorDrawableAnimator animatorSet) {
            for (int i = 0; i < this.mPendingAnimationActions.size(); i++) {
                int pendingAction = this.mPendingAnimationActions.get(i);
                if (pendingAction == 1) {
                    animatorSet.start();
                } else if (pendingAction == 4) {
                    animatorSet.end();
                } else if (pendingAction == 2) {
                    animatorSet.reverse();
                } else if (pendingAction == 3) {
                    animatorSet.reset();
                } else {
                    throw new UnsupportedOperationException("Animation action " + pendingAction + "is not supported");
                }
            }
            this.mPendingAnimationActions.clear();
        }
    }
}
