package android.transition;

import android.annotation.UnsupportedAppUsage;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class TransitionManager {
    private static String LOG_TAG = "TransitionManager";
    private static Transition sDefaultTransition = new AutoTransition();
    private static final String[] EMPTY_STRINGS = new String[0];
    @UnsupportedAppUsage
    private static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> sRunningTransitions = new ThreadLocal<>();
    @UnsupportedAppUsage
    private static ArrayList<ViewGroup> sPendingTransitions = new ArrayList<>();
    ArrayMap<Scene, Transition> mSceneTransitions = new ArrayMap<>();
    ArrayMap<Scene, ArrayMap<Scene, Transition>> mScenePairTransitions = new ArrayMap<>();

    static /* synthetic */ ArrayMap access$100() {
        return getRunningTransitions();
    }

    public void setDefaultTransition(Transition transition) {
        sDefaultTransition = transition;
    }

    public static Transition getDefaultTransition() {
        return sDefaultTransition;
    }

    public void setTransition(Scene scene, Transition transition) {
        this.mSceneTransitions.put(scene, transition);
    }

    public void setTransition(Scene fromScene, Scene toScene, Transition transition) {
        ArrayMap<Scene, Transition> sceneTransitionMap = this.mScenePairTransitions.get(toScene);
        if (sceneTransitionMap == null) {
            sceneTransitionMap = new ArrayMap<>();
            this.mScenePairTransitions.put(toScene, sceneTransitionMap);
        }
        sceneTransitionMap.put(fromScene, transition);
    }

    public Transition getTransition(Scene scene) {
        Scene currScene;
        ArrayMap<Scene, Transition> sceneTransitionMap;
        ViewGroup sceneRoot = scene.getSceneRoot();
        if (sceneRoot != null && (currScene = Scene.getCurrentScene(sceneRoot)) != null && (sceneTransitionMap = this.mScenePairTransitions.get(scene)) != null) {
            Transition transition = sceneTransitionMap.get(currScene);
            if (transition != null) {
                return transition;
            }
        }
        Transition transition2 = this.mSceneTransitions.get(scene);
        return transition2 != null ? transition2 : sDefaultTransition;
    }

    private static void changeScene(Scene scene, Transition transition) {
        ViewGroup sceneRoot = scene.getSceneRoot();
        if (!sPendingTransitions.contains(sceneRoot)) {
            Scene oldScene = Scene.getCurrentScene(sceneRoot);
            if (transition == null) {
                if (oldScene != null) {
                    oldScene.exit();
                }
                scene.enter();
                return;
            }
            sPendingTransitions.add(sceneRoot);
            Transition transitionClone = transition.mo33clone();
            transitionClone.setSceneRoot(sceneRoot);
            if (oldScene != null && oldScene.isCreatedFromLayoutResource()) {
                transitionClone.setCanRemoveViews(true);
            }
            sceneChangeSetup(sceneRoot, transitionClone);
            scene.enter();
            sceneChangeRunTransition(sceneRoot, transitionClone);
        }
    }

    @UnsupportedAppUsage
    private static ArrayMap<ViewGroup, ArrayList<Transition>> getRunningTransitions() {
        ArrayMap<ViewGroup, ArrayList<Transition>> transitions;
        WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>> runningTransitions = sRunningTransitions.get();
        if (runningTransitions != null && (transitions = runningTransitions.get()) != null) {
            return transitions;
        }
        ArrayMap<ViewGroup, ArrayList<Transition>> transitions2 = new ArrayMap<>();
        sRunningTransitions.set(new WeakReference<>(transitions2));
        return transitions2;
    }

    private static void sceneChangeRunTransition(ViewGroup sceneRoot, Transition transition) {
        if (transition != null && sceneRoot != null) {
            MultiListener listener = new MultiListener(transition, sceneRoot);
            sceneRoot.addOnAttachStateChangeListener(listener);
            sceneRoot.getViewTreeObserver().addOnPreDrawListener(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class MultiListener implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {
        ViewGroup mSceneRoot;
        Transition mTransition;
        final ViewTreeObserver mViewTreeObserver;

        MultiListener(Transition transition, ViewGroup sceneRoot) {
            this.mTransition = transition;
            this.mSceneRoot = sceneRoot;
            this.mViewTreeObserver = this.mSceneRoot.getViewTreeObserver();
        }

        private void removeListeners() {
            if (this.mViewTreeObserver.isAlive()) {
                this.mViewTreeObserver.removeOnPreDrawListener(this);
            } else {
                this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            }
            this.mSceneRoot.removeOnAttachStateChangeListener(this);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View v) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View v) {
            removeListeners();
            TransitionManager.sPendingTransitions.remove(this.mSceneRoot);
            ArrayList<Transition> runningTransitions = (ArrayList) TransitionManager.access$100().get(this.mSceneRoot);
            if (runningTransitions != null && runningTransitions.size() > 0) {
                Iterator<Transition> it = runningTransitions.iterator();
                while (it.hasNext()) {
                    Transition runningTransition = it.next();
                    runningTransition.resume(this.mSceneRoot);
                }
            }
            this.mTransition.clearValues(true);
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            removeListeners();
            if (TransitionManager.sPendingTransitions.remove(this.mSceneRoot)) {
                final ArrayMap<ViewGroup, ArrayList<Transition>> runningTransitions = TransitionManager.access$100();
                ArrayList<Transition> currentTransitions = runningTransitions.get(this.mSceneRoot);
                ArrayList<Transition> previousRunningTransitions = null;
                if (currentTransitions == null) {
                    currentTransitions = new ArrayList<>();
                    runningTransitions.put(this.mSceneRoot, currentTransitions);
                } else if (currentTransitions.size() > 0) {
                    previousRunningTransitions = new ArrayList<>(currentTransitions);
                }
                currentTransitions.add(this.mTransition);
                this.mTransition.addListener(new TransitionListenerAdapter() { // from class: android.transition.TransitionManager.MultiListener.1
                    @Override // android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
                    public void onTransitionEnd(Transition transition) {
                        ArrayList<Transition> currentTransitions2 = (ArrayList) runningTransitions.get(MultiListener.this.mSceneRoot);
                        currentTransitions2.remove(transition);
                        transition.removeListener(this);
                    }
                });
                this.mTransition.captureValues(this.mSceneRoot, false);
                if (previousRunningTransitions != null) {
                    Iterator<Transition> it = previousRunningTransitions.iterator();
                    while (it.hasNext()) {
                        Transition runningTransition = it.next();
                        runningTransition.resume(this.mSceneRoot);
                    }
                }
                this.mTransition.playTransition(this.mSceneRoot);
                return true;
            }
            return true;
        }
    }

    private static void sceneChangeSetup(ViewGroup sceneRoot, Transition transition) {
        ArrayList<Transition> runningTransitions = getRunningTransitions().get(sceneRoot);
        if (runningTransitions != null && runningTransitions.size() > 0) {
            Iterator<Transition> it = runningTransitions.iterator();
            while (it.hasNext()) {
                Transition runningTransition = it.next();
                runningTransition.pause(sceneRoot);
            }
        }
        if (transition != null) {
            transition.captureValues(sceneRoot, true);
        }
        Scene previousScene = Scene.getCurrentScene(sceneRoot);
        if (previousScene != null) {
            previousScene.exit();
        }
    }

    public void transitionTo(Scene scene) {
        changeScene(scene, getTransition(scene));
    }

    public static void go(Scene scene) {
        changeScene(scene, sDefaultTransition);
    }

    public static void go(Scene scene, Transition transition) {
        changeScene(scene, transition);
    }

    public static void beginDelayedTransition(ViewGroup sceneRoot) {
        beginDelayedTransition(sceneRoot, null);
    }

    public static void beginDelayedTransition(ViewGroup sceneRoot, Transition transition) {
        if (!sPendingTransitions.contains(sceneRoot) && sceneRoot.isLaidOut()) {
            sPendingTransitions.add(sceneRoot);
            if (transition == null) {
                transition = sDefaultTransition;
            }
            Transition transitionClone = transition.mo33clone();
            sceneChangeSetup(sceneRoot, transitionClone);
            Scene.setCurrentScene(sceneRoot, null);
            sceneChangeRunTransition(sceneRoot, transitionClone);
        }
    }

    public static void endTransitions(ViewGroup sceneRoot) {
        sPendingTransitions.remove(sceneRoot);
        ArrayList<Transition> runningTransitions = getRunningTransitions().get(sceneRoot);
        if (runningTransitions != null && !runningTransitions.isEmpty()) {
            ArrayList<Transition> copy = new ArrayList<>(runningTransitions);
            for (int i = copy.size() - 1; i >= 0; i--) {
                Transition transition = copy.get(i);
                transition.forceToEnd(sceneRoot);
            }
        }
    }
}
