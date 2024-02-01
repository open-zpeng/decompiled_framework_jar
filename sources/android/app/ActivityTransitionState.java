package android.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.transition.Transition;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.android.internal.view.OneShotPreDrawListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ActivityTransitionState {
    private static final String ENTERING_SHARED_ELEMENTS = "android:enteringSharedElements";
    private static final String EXITING_MAPPED_FROM = "android:exitingMappedFrom";
    private static final String EXITING_MAPPED_TO = "android:exitingMappedTo";
    private ExitTransitionCoordinator mCalledExitCoordinator;
    private ActivityOptions mEnterActivityOptions;
    private EnterTransitionCoordinator mEnterTransitionCoordinator;
    private ArrayList<String> mEnteringNames;
    private SparseArray<WeakReference<ExitTransitionCoordinator>> mExitTransitionCoordinators;
    private int mExitTransitionCoordinatorsKey = 1;
    private ArrayList<String> mExitingFrom;
    private ArrayList<String> mExitingTo;
    private ArrayList<View> mExitingToView;
    private boolean mHasExited;
    private boolean mIsEnterPostponed;
    private boolean mIsEnterTriggered;
    private ExitTransitionCoordinator mReturnExitCoordinator;

    public synchronized int addExitTransitionCoordinator(ExitTransitionCoordinator exitTransitionCoordinator) {
        if (this.mExitTransitionCoordinators == null) {
            this.mExitTransitionCoordinators = new SparseArray<>();
        }
        WeakReference<ExitTransitionCoordinator> ref = new WeakReference<>(exitTransitionCoordinator);
        for (int i = this.mExitTransitionCoordinators.size() - 1; i >= 0; i--) {
            WeakReference<ExitTransitionCoordinator> oldRef = this.mExitTransitionCoordinators.valueAt(i);
            if (oldRef.get() == null) {
                this.mExitTransitionCoordinators.removeAt(i);
            }
        }
        int i2 = this.mExitTransitionCoordinatorsKey;
        this.mExitTransitionCoordinatorsKey = i2 + 1;
        this.mExitTransitionCoordinators.append(i2, ref);
        return i2;
    }

    public synchronized void readState(Bundle bundle) {
        if (bundle != null) {
            if (this.mEnterTransitionCoordinator == null || this.mEnterTransitionCoordinator.isReturning()) {
                this.mEnteringNames = bundle.getStringArrayList(ENTERING_SHARED_ELEMENTS);
            }
            if (this.mEnterTransitionCoordinator == null) {
                this.mExitingFrom = bundle.getStringArrayList(EXITING_MAPPED_FROM);
                this.mExitingTo = bundle.getStringArrayList(EXITING_MAPPED_TO);
            }
        }
    }

    public synchronized void saveState(Bundle bundle) {
        if (this.mEnteringNames != null) {
            bundle.putStringArrayList(ENTERING_SHARED_ELEMENTS, this.mEnteringNames);
        }
        if (this.mExitingFrom != null) {
            bundle.putStringArrayList(EXITING_MAPPED_FROM, this.mExitingFrom);
            bundle.putStringArrayList(EXITING_MAPPED_TO, this.mExitingTo);
        }
    }

    public synchronized void setEnterActivityOptions(Activity activity, ActivityOptions options) {
        Window window = activity.getWindow();
        if (window == null) {
            return;
        }
        window.getDecorView();
        if (window.hasFeature(13) && options != null && this.mEnterActivityOptions == null && this.mEnterTransitionCoordinator == null && options.getAnimationType() == 5) {
            this.mEnterActivityOptions = options;
            this.mIsEnterTriggered = false;
            if (this.mEnterActivityOptions.isReturning()) {
                restoreExitedViews();
                int result = this.mEnterActivityOptions.getResultCode();
                if (result != 0) {
                    Intent intent = this.mEnterActivityOptions.getResultData();
                    if (intent != null) {
                        intent.setExtrasClassLoader(activity.getClassLoader());
                    }
                    activity.onActivityReenter(result, intent);
                }
            }
        }
    }

    public synchronized void enterReady(Activity activity) {
        if (this.mEnterActivityOptions == null || this.mIsEnterTriggered) {
            return;
        }
        this.mIsEnterTriggered = true;
        this.mHasExited = false;
        ArrayList<String> sharedElementNames = this.mEnterActivityOptions.getSharedElementNames();
        ResultReceiver resultReceiver = this.mEnterActivityOptions.getResultReceiver();
        if (this.mEnterActivityOptions.isReturning()) {
            restoreExitedViews();
            activity.getWindow().getDecorView().setVisibility(0);
        }
        this.mEnterTransitionCoordinator = new EnterTransitionCoordinator(activity, resultReceiver, sharedElementNames, this.mEnterActivityOptions.isReturning(), this.mEnterActivityOptions.isCrossTask());
        if (this.mEnterActivityOptions.isCrossTask()) {
            this.mExitingFrom = new ArrayList<>(this.mEnterActivityOptions.getSharedElementNames());
            this.mExitingTo = new ArrayList<>(this.mEnterActivityOptions.getSharedElementNames());
        }
        if (!this.mIsEnterPostponed) {
            startEnter();
        }
    }

    public synchronized void postponeEnterTransition() {
        this.mIsEnterPostponed = true;
    }

    public synchronized void startPostponedEnterTransition() {
        if (this.mIsEnterPostponed) {
            this.mIsEnterPostponed = false;
            if (this.mEnterTransitionCoordinator != null) {
                startEnter();
            }
        }
    }

    private synchronized void startEnter() {
        if (this.mEnterTransitionCoordinator.isReturning()) {
            if (this.mExitingToView != null) {
                this.mEnterTransitionCoordinator.viewInstancesReady(this.mExitingFrom, this.mExitingTo, this.mExitingToView);
            } else {
                this.mEnterTransitionCoordinator.namedViewsReady(this.mExitingFrom, this.mExitingTo);
            }
        } else {
            this.mEnterTransitionCoordinator.namedViewsReady(null, null);
            this.mEnteringNames = this.mEnterTransitionCoordinator.getAllSharedElementNames();
        }
        this.mExitingFrom = null;
        this.mExitingTo = null;
        this.mExitingToView = null;
        this.mEnterActivityOptions = null;
    }

    public synchronized void onStop() {
        restoreExitedViews();
        if (this.mEnterTransitionCoordinator != null) {
            this.mEnterTransitionCoordinator.stop();
            this.mEnterTransitionCoordinator = null;
        }
        if (this.mReturnExitCoordinator != null) {
            this.mReturnExitCoordinator.stop();
            this.mReturnExitCoordinator = null;
        }
    }

    public synchronized void onResume(Activity activity, boolean isTopOfTask) {
        if (isTopOfTask || this.mEnterTransitionCoordinator == null) {
            restoreExitedViews();
            restoreReenteringViews();
            return;
        }
        activity.mHandler.postDelayed(new Runnable() { // from class: android.app.ActivityTransitionState.1
            @Override // java.lang.Runnable
            public void run() {
                if (ActivityTransitionState.this.mEnterTransitionCoordinator == null || ActivityTransitionState.this.mEnterTransitionCoordinator.isWaitingForRemoteExit()) {
                    ActivityTransitionState.this.restoreExitedViews();
                    ActivityTransitionState.this.restoreReenteringViews();
                }
            }
        }, 1000L);
    }

    public synchronized void clear() {
        this.mEnteringNames = null;
        this.mExitingFrom = null;
        this.mExitingTo = null;
        this.mExitingToView = null;
        this.mCalledExitCoordinator = null;
        this.mEnterTransitionCoordinator = null;
        this.mEnterActivityOptions = null;
        this.mExitTransitionCoordinators = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void restoreExitedViews() {
        if (this.mCalledExitCoordinator != null) {
            this.mCalledExitCoordinator.resetViews();
            this.mCalledExitCoordinator = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void restoreReenteringViews() {
        if (this.mEnterTransitionCoordinator != null && this.mEnterTransitionCoordinator.isReturning() && !this.mEnterTransitionCoordinator.isCrossTask()) {
            this.mEnterTransitionCoordinator.forceViewsToAppear();
            this.mExitingFrom = null;
            this.mExitingTo = null;
            this.mExitingToView = null;
        }
    }

    public synchronized boolean startExitBackTransition(final Activity activity) {
        if (this.mEnteringNames == null || this.mCalledExitCoordinator != null) {
            return false;
        }
        if (!this.mHasExited) {
            this.mHasExited = true;
            Transition enterViewsTransition = null;
            ViewGroup decor = null;
            boolean delayExitBack = false;
            if (this.mEnterTransitionCoordinator != null) {
                enterViewsTransition = this.mEnterTransitionCoordinator.getEnterViewsTransition();
                decor = this.mEnterTransitionCoordinator.getDecor();
                delayExitBack = this.mEnterTransitionCoordinator.cancelEnter();
                this.mEnterTransitionCoordinator = null;
                if (enterViewsTransition != null && decor != null) {
                    enterViewsTransition.pause(decor);
                }
            }
            this.mReturnExitCoordinator = new ExitTransitionCoordinator(activity, activity.getWindow(), activity.mEnterTransitionListener, this.mEnteringNames, null, null, true);
            if (enterViewsTransition != null && decor != null) {
                enterViewsTransition.resume(decor);
            }
            if (delayExitBack && decor != null) {
                OneShotPreDrawListener.add(decor, new Runnable() { // from class: android.app.-$$Lambda$ActivityTransitionState$yioLR6wQWjZ9DcWK5bibElIbsXc
                    @Override // java.lang.Runnable
                    public final void run() {
                        ActivityTransitionState.lambda$startExitBackTransition$0(ActivityTransitionState.this, activity);
                    }
                });
            } else {
                this.mReturnExitCoordinator.startExit(activity.mResultCode, activity.mResultData);
            }
        }
        return true;
    }

    public static /* synthetic */ void lambda$startExitBackTransition$0(ActivityTransitionState activityTransitionState, Activity activity) {
        if (activityTransitionState.mReturnExitCoordinator != null) {
            activityTransitionState.mReturnExitCoordinator.startExit(activity.mResultCode, activity.mResultData);
        }
    }

    public synchronized boolean isTransitionRunning() {
        if (this.mEnterTransitionCoordinator == null || !this.mEnterTransitionCoordinator.isTransitionRunning()) {
            if (this.mCalledExitCoordinator == null || !this.mCalledExitCoordinator.isTransitionRunning()) {
                return this.mReturnExitCoordinator != null && this.mReturnExitCoordinator.isTransitionRunning();
            }
            return true;
        }
        return true;
    }

    public synchronized void startExitOutTransition(Activity activity, Bundle options) {
        this.mEnterTransitionCoordinator = null;
        if (!activity.getWindow().hasFeature(13) || this.mExitTransitionCoordinators == null) {
            return;
        }
        ActivityOptions activityOptions = new ActivityOptions(options);
        if (activityOptions.getAnimationType() == 5) {
            int key = activityOptions.getExitCoordinatorKey();
            int index = this.mExitTransitionCoordinators.indexOfKey(key);
            if (index >= 0) {
                this.mCalledExitCoordinator = this.mExitTransitionCoordinators.valueAt(index).get();
                this.mExitTransitionCoordinators.removeAt(index);
                if (this.mCalledExitCoordinator != null) {
                    this.mExitingFrom = this.mCalledExitCoordinator.getAcceptedNames();
                    this.mExitingTo = this.mCalledExitCoordinator.getMappedNames();
                    this.mExitingToView = this.mCalledExitCoordinator.copyMappedViews();
                    this.mCalledExitCoordinator.startExit();
                }
            }
        }
    }
}
