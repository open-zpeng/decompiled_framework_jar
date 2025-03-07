package android.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityTransitionCoordinator;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.transition.Transition;
import android.transition.TransitionListenerAdapter;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.android.internal.view.OneShotPreDrawListener;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ExitTransitionCoordinator extends ActivityTransitionCoordinator {
    private static final long MAX_WAIT_MS = 1000;
    private static final String TAG = "ExitTransitionCoordinator";
    private Activity mActivity;
    private ObjectAnimator mBackgroundAnimator;
    private boolean mExitNotified;
    private Bundle mExitSharedElementBundle;
    private Handler mHandler;
    private HideSharedElementsCallback mHideSharedElementsCallback;
    private boolean mIsBackgroundReady;
    private boolean mIsCanceled;
    private boolean mIsExitStarted;
    private boolean mIsHidden;
    private Bundle mSharedElementBundle;
    private boolean mSharedElementNotified;
    private boolean mSharedElementsHidden;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface HideSharedElementsCallback {
        void hideSharedElements();
    }

    public ExitTransitionCoordinator(Activity activity, Window window, SharedElementCallback listener, ArrayList<String> names, ArrayList<String> accepted, ArrayList<View> mapped, boolean isReturning) {
        super(window, names, listener, isReturning);
        viewsReady(mapSharedElements(accepted, mapped));
        stripOffscreenViews();
        this.mIsBackgroundReady = !isReturning;
        this.mActivity = activity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHideSharedElementsCallback(HideSharedElementsCallback callback) {
        this.mHideSharedElementsCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.ResultReceiver
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == 100) {
            stopCancel();
            this.mResultReceiver = (ResultReceiver) resultData.getParcelable("android:remoteReceiver");
            if (this.mIsCanceled) {
                this.mResultReceiver.send(106, null);
                this.mResultReceiver = null;
                return;
            }
            notifyComplete();
        } else if (resultCode == 101) {
            stopCancel();
            if (!this.mIsCanceled) {
                hideSharedElements();
            }
        } else {
            switch (resultCode) {
                case 105:
                    this.mHandler.removeMessages(106);
                    startExit();
                    return;
                case 106:
                    this.mIsCanceled = true;
                    finish();
                    return;
                case 107:
                    this.mExitSharedElementBundle = resultData;
                    sharedElementExitBack();
                    return;
                default:
                    return;
            }
        }
    }

    private void stopCancel() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(106);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void delayCancel() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.sendEmptyMessageDelayed(106, 1000L);
        }
    }

    public void resetViews() {
        ViewGroup decorView = getDecor();
        if (decorView != null) {
            TransitionManager.endTransitions(decorView);
        }
        if (this.mTransitioningViews != null) {
            showViews(this.mTransitioningViews, true);
            setTransitioningViewsVisiblity(0, true);
        }
        showViews(this.mSharedElements, true);
        this.mIsHidden = true;
        if (!this.mIsReturning && decorView != null) {
            decorView.suppressLayout(false);
        }
        moveSharedElementsFromOverlay();
        clearState();
    }

    private void sharedElementExitBack() {
        Bundle bundle;
        final ViewGroup decorView = getDecor();
        if (decorView != null) {
            decorView.suppressLayout(true);
        }
        if (decorView != null && (bundle = this.mExitSharedElementBundle) != null && !bundle.isEmpty() && !this.mSharedElements.isEmpty() && getSharedElementTransition() != null) {
            startTransition(new Runnable() { // from class: android.app.ExitTransitionCoordinator.1
                @Override // java.lang.Runnable
                public void run() {
                    ExitTransitionCoordinator.this.startSharedElementExit(decorView);
                }
            });
        } else {
            sharedElementTransitionComplete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSharedElementExit(ViewGroup decorView) {
        Transition transition = getSharedElementExitTransition();
        transition.addListener(new TransitionListenerAdapter() { // from class: android.app.ExitTransitionCoordinator.2
            @Override // android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
            public void onTransitionEnd(Transition transition2) {
                transition2.removeListener(this);
                if (ExitTransitionCoordinator.this.isViewsTransitionComplete()) {
                    ExitTransitionCoordinator.this.delayCancel();
                }
            }
        });
        final ArrayList<View> sharedElementSnapshots = createSnapshots(this.mExitSharedElementBundle, this.mSharedElementNames);
        OneShotPreDrawListener.add(decorView, new Runnable() { // from class: android.app.-$$Lambda$ExitTransitionCoordinator$QSAvMs76ZWnO0eiLyXWkcGxkRIY
            @Override // java.lang.Runnable
            public final void run() {
                ExitTransitionCoordinator.this.lambda$startSharedElementExit$0$ExitTransitionCoordinator(sharedElementSnapshots);
            }
        });
        lambda$scheduleGhostVisibilityChange$1$ActivityTransitionCoordinator(4);
        scheduleGhostVisibilityChange(4);
        if (this.mListener != null) {
            this.mListener.onSharedElementEnd(this.mSharedElementNames, this.mSharedElements, sharedElementSnapshots);
        }
        TransitionManager.beginDelayedTransition(decorView, transition);
        scheduleGhostVisibilityChange(0);
        lambda$scheduleGhostVisibilityChange$1$ActivityTransitionCoordinator(0);
        decorView.invalidate();
    }

    public /* synthetic */ void lambda$startSharedElementExit$0$ExitTransitionCoordinator(ArrayList sharedElementSnapshots) {
        setSharedElementState(this.mExitSharedElementBundle, sharedElementSnapshots);
    }

    private void hideSharedElements() {
        moveSharedElementsFromOverlay();
        HideSharedElementsCallback hideSharedElementsCallback = this.mHideSharedElementsCallback;
        if (hideSharedElementsCallback != null) {
            hideSharedElementsCallback.hideSharedElements();
        }
        if (!this.mIsHidden) {
            hideViews(this.mSharedElements);
        }
        this.mSharedElementsHidden = true;
        finishIfNecessary();
    }

    public void startExit() {
        if (!this.mIsExitStarted) {
            backgroundAnimatorComplete();
            this.mIsExitStarted = true;
            pauseInput();
            ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.suppressLayout(true);
            }
            moveSharedElementsToOverlay();
            startTransition(new Runnable() { // from class: android.app.ExitTransitionCoordinator.3
                @Override // java.lang.Runnable
                public void run() {
                    if (ExitTransitionCoordinator.this.mActivity != null) {
                        ExitTransitionCoordinator.this.beginTransitions();
                    } else {
                        ExitTransitionCoordinator.this.startExitTransition();
                    }
                }
            });
        }
    }

    public void startExit(int resultCode, Intent data) {
        if (!this.mIsExitStarted) {
            boolean targetsM = true;
            this.mIsExitStarted = true;
            pauseInput();
            ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.suppressLayout(true);
            }
            this.mHandler = new Handler() { // from class: android.app.ExitTransitionCoordinator.4
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    ExitTransitionCoordinator.this.mIsCanceled = true;
                    ExitTransitionCoordinator.this.finish();
                }
            };
            delayCancel();
            moveSharedElementsToOverlay();
            if (decorView != null && decorView.getBackground() == null) {
                getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            if (decorView != null && decorView.getContext().getApplicationInfo().targetSdkVersion < 23) {
                targetsM = false;
            }
            ArrayList<String> sharedElementNames = targetsM ? this.mSharedElementNames : this.mAllSharedElementNames;
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this.mActivity, this, sharedElementNames, resultCode, data);
            this.mActivity.convertToTranslucent(new Activity.TranslucentConversionListener() { // from class: android.app.ExitTransitionCoordinator.5
                @Override // android.app.Activity.TranslucentConversionListener
                public void onTranslucentConversionComplete(boolean drawComplete) {
                    if (!ExitTransitionCoordinator.this.mIsCanceled) {
                        ExitTransitionCoordinator.this.fadeOutBackground();
                    }
                }
            }, options);
            startTransition(new Runnable() { // from class: android.app.ExitTransitionCoordinator.6
                @Override // java.lang.Runnable
                public void run() {
                    ExitTransitionCoordinator.this.startExitTransition();
                }
            });
        }
    }

    public void stop() {
        Activity activity;
        if (this.mIsReturning && (activity = this.mActivity) != null) {
            activity.convertToTranslucent(null, null);
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startExitTransition() {
        Transition transition = getExitTransition();
        ViewGroup decorView = getDecor();
        if (transition != null && decorView != null && this.mTransitioningViews != null) {
            setTransitioningViewsVisiblity(0, false);
            TransitionManager.beginDelayedTransition(decorView, transition);
            setTransitioningViewsVisiblity(4, false);
            decorView.invalidate();
            return;
        }
        transitionStarted();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fadeOutBackground() {
        Drawable background;
        if (this.mBackgroundAnimator == null) {
            ViewGroup decor = getDecor();
            if (decor != null && (background = decor.getBackground()) != null) {
                Drawable background2 = background.mutate();
                getWindow().setBackgroundDrawable(background2);
                this.mBackgroundAnimator = ObjectAnimator.ofInt(background2, "alpha", 0);
                this.mBackgroundAnimator.addListener(new AnimatorListenerAdapter() { // from class: android.app.ExitTransitionCoordinator.7
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animation) {
                        ExitTransitionCoordinator.this.mBackgroundAnimator = null;
                        if (!ExitTransitionCoordinator.this.mIsCanceled) {
                            ExitTransitionCoordinator.this.mIsBackgroundReady = true;
                            ExitTransitionCoordinator.this.notifyComplete();
                        }
                        ExitTransitionCoordinator.this.backgroundAnimatorComplete();
                    }
                });
                this.mBackgroundAnimator.setDuration(getFadeDuration());
                this.mBackgroundAnimator.start();
                return;
            }
            backgroundAnimatorComplete();
            this.mIsBackgroundReady = true;
        }
    }

    private Transition getExitTransition() {
        Transition viewsTransition = null;
        if (this.mTransitioningViews != null && !this.mTransitioningViews.isEmpty()) {
            viewsTransition = configureTransition(getViewsTransition(), true);
            removeExcludedViews(viewsTransition, this.mTransitioningViews);
            if (this.mTransitioningViews.isEmpty()) {
                viewsTransition = null;
            }
        }
        if (viewsTransition == null) {
            viewsTransitionComplete();
        } else {
            final ArrayList<View> transitioningViews = this.mTransitioningViews;
            viewsTransition.addListener(new ActivityTransitionCoordinator.ContinueTransitionListener() { // from class: android.app.ExitTransitionCoordinator.8
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // android.app.ActivityTransitionCoordinator.ContinueTransitionListener, android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
                public void onTransitionEnd(Transition transition) {
                    ArrayList<View> arrayList;
                    ExitTransitionCoordinator.this.viewsTransitionComplete();
                    if (ExitTransitionCoordinator.this.mIsHidden && (arrayList = transitioningViews) != null) {
                        ExitTransitionCoordinator.this.showViews(arrayList, true);
                        ExitTransitionCoordinator.this.setTransitioningViewsVisiblity(0, true);
                    }
                    if (ExitTransitionCoordinator.this.mSharedElementBundle != null) {
                        ExitTransitionCoordinator.this.delayCancel();
                    }
                    super.onTransitionEnd(transition);
                }
            });
        }
        return viewsTransition;
    }

    private Transition getSharedElementExitTransition() {
        Transition sharedElementTransition = null;
        if (!this.mSharedElements.isEmpty()) {
            sharedElementTransition = configureTransition(getSharedElementTransition(), false);
        }
        if (sharedElementTransition == null) {
            sharedElementTransitionComplete();
        } else {
            sharedElementTransition.addListener(new ActivityTransitionCoordinator.ContinueTransitionListener() { // from class: android.app.ExitTransitionCoordinator.9
                @Override // android.app.ActivityTransitionCoordinator.ContinueTransitionListener, android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
                public void onTransitionEnd(Transition transition) {
                    ExitTransitionCoordinator.this.sharedElementTransitionComplete();
                    if (ExitTransitionCoordinator.this.mIsHidden) {
                        ExitTransitionCoordinator exitTransitionCoordinator = ExitTransitionCoordinator.this;
                        exitTransitionCoordinator.showViews(exitTransitionCoordinator.mSharedElements, true);
                    }
                    super.onTransitionEnd(transition);
                }
            });
            this.mSharedElements.get(0).invalidate();
        }
        return sharedElementTransition;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void beginTransitions() {
        Transition sharedElementTransition = getSharedElementExitTransition();
        Transition viewsTransition = getExitTransition();
        Transition transition = mergeTransitions(sharedElementTransition, viewsTransition);
        ViewGroup decorView = getDecor();
        if (transition != null && decorView != null) {
            lambda$scheduleGhostVisibilityChange$1$ActivityTransitionCoordinator(4);
            scheduleGhostVisibilityChange(4);
            if (viewsTransition != null) {
                setTransitioningViewsVisiblity(0, false);
            }
            TransitionManager.beginDelayedTransition(decorView, transition);
            scheduleGhostVisibilityChange(0);
            lambda$scheduleGhostVisibilityChange$1$ActivityTransitionCoordinator(0);
            if (viewsTransition != null) {
                setTransitioningViewsVisiblity(4, false);
            }
            decorView.invalidate();
            return;
        }
        transitionStarted();
    }

    protected boolean isReadyToNotify() {
        return (this.mSharedElementBundle == null || this.mResultReceiver == null || !this.mIsBackgroundReady) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.ActivityTransitionCoordinator
    public void sharedElementTransitionComplete() {
        this.mSharedElementBundle = this.mExitSharedElementBundle == null ? captureSharedElementState() : captureExitSharedElementsState();
        super.sharedElementTransitionComplete();
    }

    private Bundle captureExitSharedElementsState() {
        Bundle bundle = new Bundle();
        RectF bounds = new RectF();
        Matrix matrix = new Matrix();
        for (int i = 0; i < this.mSharedElements.size(); i++) {
            String name = this.mSharedElementNames.get(i);
            Bundle sharedElementState = this.mExitSharedElementBundle.getBundle(name);
            if (sharedElementState != null) {
                bundle.putBundle(name, sharedElementState);
            } else {
                View view = this.mSharedElements.get(i);
                captureSharedElementState(view, name, bundle, matrix, bounds);
            }
        }
        return bundle;
    }

    @Override // android.app.ActivityTransitionCoordinator
    protected void onTransitionsComplete() {
        notifyComplete();
    }

    protected void notifyComplete() {
        if (isReadyToNotify()) {
            if (!this.mSharedElementNotified) {
                this.mSharedElementNotified = true;
                delayCancel();
                if (!this.mActivity.isTopOfTask()) {
                    this.mResultReceiver.send(108, null);
                }
                if (this.mListener == null) {
                    this.mResultReceiver.send(103, this.mSharedElementBundle);
                    notifyExitComplete();
                    return;
                }
                final ResultReceiver resultReceiver = this.mResultReceiver;
                final Bundle sharedElementBundle = this.mSharedElementBundle;
                this.mListener.onSharedElementsArrived(this.mSharedElementNames, this.mSharedElements, new SharedElementCallback.OnSharedElementsReadyListener() { // from class: android.app.ExitTransitionCoordinator.10
                    @Override // android.app.SharedElementCallback.OnSharedElementsReadyListener
                    public void onSharedElementsReady() {
                        resultReceiver.send(103, sharedElementBundle);
                        ExitTransitionCoordinator.this.notifyExitComplete();
                    }
                });
                return;
            }
            notifyExitComplete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyExitComplete() {
        if (!this.mExitNotified && isViewsTransitionComplete()) {
            this.mExitNotified = true;
            this.mResultReceiver.send(104, null);
            this.mResultReceiver = null;
            ViewGroup decorView = getDecor();
            if (!this.mIsReturning && decorView != null) {
                decorView.suppressLayout(false);
            }
            finishIfNecessary();
        }
    }

    private void finishIfNecessary() {
        if (this.mIsReturning && this.mExitNotified && this.mActivity != null && (this.mSharedElements.isEmpty() || this.mSharedElementsHidden)) {
            finish();
        }
        if (!this.mIsReturning && this.mExitNotified) {
            this.mActivity = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finish() {
        stopCancel();
        Activity activity = this.mActivity;
        if (activity != null) {
            activity.mActivityTransitionState.clear();
            this.mActivity.finish();
            this.mActivity.overridePendingTransition(0, 0);
            this.mActivity = null;
        }
        clearState();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.ActivityTransitionCoordinator
    public void clearState() {
        this.mHandler = null;
        this.mSharedElementBundle = null;
        ObjectAnimator objectAnimator = this.mBackgroundAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.mBackgroundAnimator = null;
        }
        this.mExitSharedElementBundle = null;
        super.clearState();
    }

    @Override // android.app.ActivityTransitionCoordinator
    protected boolean moveSharedElementWithParent() {
        return !this.mIsReturning;
    }

    @Override // android.app.ActivityTransitionCoordinator
    protected Transition getViewsTransition() {
        if (this.mIsReturning) {
            return getWindow().getReturnTransition();
        }
        return getWindow().getExitTransition();
    }

    protected Transition getSharedElementTransition() {
        if (this.mIsReturning) {
            return getWindow().getSharedElementReturnTransition();
        }
        return getWindow().getSharedElementExitTransition();
    }
}
