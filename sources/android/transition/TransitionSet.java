package android.transition;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.transition.Transition;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class TransitionSet extends Transition {
    public static final int ORDERING_SEQUENTIAL = 1;
    public static final int ORDERING_TOGETHER = 0;
    int mCurrentListeners;
    private boolean mPlayTogether;
    boolean mStarted;
    ArrayList<Transition> mTransitions;

    public TransitionSet() {
        this.mTransitions = new ArrayList<>();
        this.mPlayTogether = true;
        this.mStarted = false;
    }

    public TransitionSet(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTransitions = new ArrayList<>();
        this.mPlayTogether = true;
        this.mStarted = false;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TransitionSet);
        int ordering = a.getInt(0, 0);
        setOrdering(ordering);
        a.recycle();
    }

    public TransitionSet setOrdering(int ordering) {
        switch (ordering) {
            case 0:
                this.mPlayTogether = true;
                break;
            case 1:
                this.mPlayTogether = false;
                break;
            default:
                throw new AndroidRuntimeException("Invalid parameter for TransitionSet ordering: " + ordering);
        }
        return this;
    }

    public int getOrdering() {
        return !this.mPlayTogether ? 1 : 0;
    }

    public TransitionSet addTransition(Transition transition) {
        if (transition != null) {
            this.mTransitions.add(transition);
            transition.mParent = this;
            if (this.mDuration >= 0) {
                transition.setDuration(this.mDuration);
            }
        }
        return this;
    }

    public int getTransitionCount() {
        return this.mTransitions.size();
    }

    public Transition getTransitionAt(int index) {
        if (index < 0 || index >= this.mTransitions.size()) {
            return null;
        }
        return this.mTransitions.get(index);
    }

    @Override // android.transition.Transition
    public TransitionSet setDuration(long duration) {
        super.setDuration(duration);
        if (this.mDuration >= 0 && this.mTransitions != null) {
            int numTransitions = this.mTransitions.size();
            for (int i = 0; i < numTransitions; i++) {
                this.mTransitions.get(i).setDuration(duration);
            }
        }
        return this;
    }

    @Override // android.transition.Transition
    public TransitionSet setStartDelay(long startDelay) {
        return (TransitionSet) super.setStartDelay(startDelay);
    }

    @Override // android.transition.Transition
    public TransitionSet setInterpolator(TimeInterpolator interpolator) {
        return (TransitionSet) super.setInterpolator(interpolator);
    }

    @Override // android.transition.Transition
    public TransitionSet addTarget(View target) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).addTarget(target);
        }
        return (TransitionSet) super.addTarget(target);
    }

    @Override // android.transition.Transition
    public TransitionSet addTarget(int targetId) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).addTarget(targetId);
        }
        return (TransitionSet) super.addTarget(targetId);
    }

    @Override // android.transition.Transition
    public TransitionSet addTarget(String targetName) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).addTarget(targetName);
        }
        return (TransitionSet) super.addTarget(targetName);
    }

    @Override // android.transition.Transition
    public TransitionSet addTarget(Class targetType) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).addTarget(targetType);
        }
        return (TransitionSet) super.addTarget(targetType);
    }

    @Override // android.transition.Transition
    public TransitionSet addListener(Transition.TransitionListener listener) {
        return (TransitionSet) super.addListener(listener);
    }

    @Override // android.transition.Transition
    public TransitionSet removeTarget(int targetId) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).removeTarget(targetId);
        }
        return (TransitionSet) super.removeTarget(targetId);
    }

    @Override // android.transition.Transition
    public TransitionSet removeTarget(View target) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).removeTarget(target);
        }
        return (TransitionSet) super.removeTarget(target);
    }

    @Override // android.transition.Transition
    public TransitionSet removeTarget(Class target) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).removeTarget(target);
        }
        return (TransitionSet) super.removeTarget(target);
    }

    @Override // android.transition.Transition
    public TransitionSet removeTarget(String target) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).removeTarget(target);
        }
        return (TransitionSet) super.removeTarget(target);
    }

    @Override // android.transition.Transition
    public Transition excludeTarget(View target, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(target, exclude);
        }
        return super.excludeTarget(target, exclude);
    }

    @Override // android.transition.Transition
    public Transition excludeTarget(String targetName, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(targetName, exclude);
        }
        return super.excludeTarget(targetName, exclude);
    }

    @Override // android.transition.Transition
    public Transition excludeTarget(int targetId, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(targetId, exclude);
        }
        return super.excludeTarget(targetId, exclude);
    }

    @Override // android.transition.Transition
    public Transition excludeTarget(Class type, boolean exclude) {
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).excludeTarget(type, exclude);
        }
        return super.excludeTarget(type, exclude);
    }

    @Override // android.transition.Transition
    public TransitionSet removeListener(Transition.TransitionListener listener) {
        return (TransitionSet) super.removeListener(listener);
    }

    @Override // android.transition.Transition
    public void setPathMotion(PathMotion pathMotion) {
        super.setPathMotion(pathMotion);
        for (int i = 0; i < this.mTransitions.size(); i++) {
            this.mTransitions.get(i).setPathMotion(pathMotion);
        }
    }

    public TransitionSet removeTransition(Transition transition) {
        this.mTransitions.remove(transition);
        transition.mParent = null;
        return this;
    }

    private synchronized void setupStartEndListeners() {
        TransitionSetListener listener = new TransitionSetListener(this);
        Iterator<Transition> it = this.mTransitions.iterator();
        while (it.hasNext()) {
            Transition childTransition = it.next();
            childTransition.addListener(listener);
        }
        this.mCurrentListeners = this.mTransitions.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class TransitionSetListener extends TransitionListenerAdapter {
        TransitionSet mTransitionSet;

        synchronized TransitionSetListener(TransitionSet transitionSet) {
            this.mTransitionSet = transitionSet;
        }

        @Override // android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
        public void onTransitionStart(Transition transition) {
            if (!this.mTransitionSet.mStarted) {
                this.mTransitionSet.start();
                this.mTransitionSet.mStarted = true;
            }
        }

        @Override // android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
        public void onTransitionEnd(Transition transition) {
            TransitionSet transitionSet = this.mTransitionSet;
            transitionSet.mCurrentListeners--;
            if (this.mTransitionSet.mCurrentListeners == 0) {
                this.mTransitionSet.mStarted = false;
                this.mTransitionSet.end();
            }
            transition.removeListener(this);
        }
    }

    @Override // android.transition.Transition
    protected synchronized void createAnimators(ViewGroup sceneRoot, TransitionValuesMaps startValues, TransitionValuesMaps endValues, ArrayList<TransitionValues> startValuesList, ArrayList<TransitionValues> endValuesList) {
        long startDelay = getStartDelay();
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            Transition childTransition = this.mTransitions.get(i);
            if (startDelay > 0 && (this.mPlayTogether || i == 0)) {
                long childStartDelay = childTransition.getStartDelay();
                if (childStartDelay > 0) {
                    childTransition.setStartDelay(startDelay + childStartDelay);
                } else {
                    childTransition.setStartDelay(startDelay);
                }
            }
            childTransition.createAnimators(sceneRoot, startValues, endValues, startValuesList, endValuesList);
        }
    }

    @Override // android.transition.Transition
    protected synchronized void runAnimators() {
        if (this.mTransitions.isEmpty()) {
            start();
            end();
            return;
        }
        setupStartEndListeners();
        int numTransitions = this.mTransitions.size();
        int i = 0;
        if (!this.mPlayTogether) {
            for (int i2 = 1; i2 < numTransitions; i2++) {
                Transition previousTransition = this.mTransitions.get(i2 - 1);
                final Transition nextTransition = this.mTransitions.get(i2);
                previousTransition.addListener(new TransitionListenerAdapter() { // from class: android.transition.TransitionSet.1
                    @Override // android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
                    public void onTransitionEnd(Transition transition) {
                        nextTransition.runAnimators();
                        transition.removeListener(this);
                    }
                });
            }
            Transition firstTransition = this.mTransitions.get(0);
            if (firstTransition != null) {
                firstTransition.runAnimators();
                return;
            }
            return;
        }
        while (true) {
            int i3 = i;
            if (i3 < numTransitions) {
                this.mTransitions.get(i3).runAnimators();
                i = i3 + 1;
            } else {
                return;
            }
        }
    }

    @Override // android.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        if (isValidTarget(transitionValues.view)) {
            Iterator<Transition> it = this.mTransitions.iterator();
            while (it.hasNext()) {
                Transition childTransition = it.next();
                if (childTransition.isValidTarget(transitionValues.view)) {
                    childTransition.captureStartValues(transitionValues);
                    transitionValues.targetedTransitions.add(childTransition);
                }
            }
        }
    }

    @Override // android.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        if (isValidTarget(transitionValues.view)) {
            Iterator<Transition> it = this.mTransitions.iterator();
            while (it.hasNext()) {
                Transition childTransition = it.next();
                if (childTransition.isValidTarget(transitionValues.view)) {
                    childTransition.captureEndValues(transitionValues);
                    transitionValues.targetedTransitions.add(childTransition);
                }
            }
        }
    }

    @Override // android.transition.Transition
    synchronized void capturePropagationValues(TransitionValues transitionValues) {
        super.capturePropagationValues(transitionValues);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).capturePropagationValues(transitionValues);
        }
    }

    @Override // android.transition.Transition
    public synchronized void pause(View sceneRoot) {
        super.pause(sceneRoot);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).pause(sceneRoot);
        }
    }

    @Override // android.transition.Transition
    public synchronized void resume(View sceneRoot) {
        super.resume(sceneRoot);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).resume(sceneRoot);
        }
    }

    protected synchronized void cancel() {
        super.cancel();
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).cancel();
        }
    }

    @Override // android.transition.Transition
    synchronized void forceToEnd(ViewGroup sceneRoot) {
        super.forceToEnd(sceneRoot);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).forceToEnd(sceneRoot);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.transition.Transition
    public synchronized TransitionSet setSceneRoot(ViewGroup sceneRoot) {
        super.setSceneRoot(sceneRoot);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).setSceneRoot(sceneRoot);
        }
        return this;
    }

    @Override // android.transition.Transition
    synchronized void setCanRemoveViews(boolean canRemoveViews) {
        super.setCanRemoveViews(canRemoveViews);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).setCanRemoveViews(canRemoveViews);
        }
    }

    @Override // android.transition.Transition
    public void setPropagation(TransitionPropagation propagation) {
        super.setPropagation(propagation);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).setPropagation(propagation);
        }
    }

    @Override // android.transition.Transition
    public void setEpicenterCallback(Transition.EpicenterCallback epicenterCallback) {
        super.setEpicenterCallback(epicenterCallback);
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            this.mTransitions.get(i).setEpicenterCallback(epicenterCallback);
        }
    }

    @Override // android.transition.Transition
    synchronized String toString(String indent) {
        String result = super.toString(indent);
        for (int i = 0; i < this.mTransitions.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            sb.append("\n");
            sb.append(this.mTransitions.get(i).toString(indent + "  "));
            result = sb.toString();
        }
        return result;
    }

    @Override // android.transition.Transition
    /* renamed from: clone */
    public TransitionSet mo52clone() {
        TransitionSet clone = (TransitionSet) super.mo52clone();
        clone.mTransitions = new ArrayList<>();
        int numTransitions = this.mTransitions.size();
        for (int i = 0; i < numTransitions; i++) {
            clone.addTransition(this.mTransitions.get(i).mo52clone());
        }
        return clone;
    }
}
