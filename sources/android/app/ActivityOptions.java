package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.ExitTransitionCoordinator;
import android.app.WindowConfiguration;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.GraphicBuffer;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IRemoteCallback;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.transition.Transition;
import android.transition.TransitionListenerAdapter;
import android.transition.TransitionManager;
import android.util.Pair;
import android.util.Slog;
import android.view.AppTransitionAnimationSpec;
import android.view.IAppTransitionAnimationSpecsFuture;
import android.view.RemoteAnimationAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.android.internal.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ActivityOptions {
    public static final int ANIM_CLIP_REVEAL = 11;
    public static final int ANIM_CUSTOM = 1;
    public static final int ANIM_CUSTOM_IN_PLACE = 10;
    public static final int ANIM_DEFAULT = 6;
    public static final int ANIM_LAUNCH_TASK_BEHIND = 7;
    public static final int ANIM_NONE = 0;
    public static final int ANIM_OPEN_CROSS_PROFILE_APPS = 12;
    public static final int ANIM_REMOTE_ANIMATION = 13;
    public static final int ANIM_SCALE_UP = 2;
    public static final int ANIM_SCENE_TRANSITION = 5;
    public static final int ANIM_THUMBNAIL_ASPECT_SCALE_DOWN = 9;
    public static final int ANIM_THUMBNAIL_ASPECT_SCALE_UP = 8;
    public static final int ANIM_THUMBNAIL_SCALE_DOWN = 4;
    public static final int ANIM_THUMBNAIL_SCALE_UP = 3;
    public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
    public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";
    private static final String KEY_ANIMATION_FINISHED_LISTENER = "android:activity.animationFinishedListener";
    public static final String KEY_ANIM_ENTER_RES_ID = "android:activity.animEnterRes";
    public static final String KEY_ANIM_EXIT_RES_ID = "android:activity.animExitRes";
    public static final String KEY_ANIM_HEIGHT = "android:activity.animHeight";
    public static final String KEY_ANIM_IN_PLACE_RES_ID = "android:activity.animInPlaceRes";
    private static final String KEY_ANIM_SPECS = "android:activity.animSpecs";
    public static final String KEY_ANIM_START_LISTENER = "android:activity.animStartListener";
    public static final String KEY_ANIM_START_X = "android:activity.animStartX";
    public static final String KEY_ANIM_START_Y = "android:activity.animStartY";
    public static final String KEY_ANIM_THUMBNAIL = "android:activity.animThumbnail";
    public static final String KEY_ANIM_TYPE = "android:activity.animType";
    public static final String KEY_ANIM_WIDTH = "android:activity.animWidth";
    private static final String KEY_AVOID_MOVE_TO_FRONT = "android.activity.avoidMoveToFront";
    private static final String KEY_DISALLOW_ENTER_PICTURE_IN_PICTURE_WHILE_LAUNCHING = "android:activity.disallowEnterPictureInPictureWhileLaunching";
    private static final String KEY_EXIT_COORDINATOR_INDEX = "android:activity.exitCoordinatorIndex";
    private static final String KEY_FREEZE_RECENT_TASKS_REORDERING = "android.activity.freezeRecentTasksReordering";
    private static final String KEY_INSTANT_APP_VERIFICATION_BUNDLE = "android:instantapps.installerbundle";
    private static final String KEY_LAUNCH_ACTIVITY_TYPE = "android.activity.activityType";
    public static final String KEY_LAUNCH_BOUNDS = "android:activity.launchBounds";
    private static final String KEY_LAUNCH_DISPLAY_ID = "android.activity.launchDisplayId";
    private static final String KEY_LAUNCH_TASK_ID = "android.activity.launchTaskId";
    private static final String KEY_LAUNCH_WINDOWING_MODE = "android.activity.windowingMode";
    private static final String KEY_LOCK_TASK_MODE = "android:activity.lockTaskMode";
    public static final String KEY_PACKAGE_NAME = "android:activity.packageName";
    private static final String KEY_PENDING_INTENT_LAUNCH_FLAGS = "android.activity.pendingIntentLaunchFlags";
    private static final String KEY_REMOTE_ANIMATION_ADAPTER = "android:activity.remoteAnimationAdapter";
    private static final String KEY_RESULT_CODE = "android:activity.resultCode";
    private static final String KEY_RESULT_DATA = "android:activity.resultData";
    private static final String KEY_ROTATION_ANIMATION_HINT = "android:activity.rotationAnimationHint";
    private static final String KEY_SPECS_FUTURE = "android:activity.specsFuture";
    private static final String KEY_SPLIT_SCREEN_CREATE_MODE = "android:activity.splitScreenCreateMode";
    private static final String KEY_TASK_OVERLAY = "android.activity.taskOverlay";
    private static final String KEY_TASK_OVERLAY_CAN_RESUME = "android.activity.taskOverlayCanResume";
    private static final String KEY_TRANSITION_COMPLETE_LISTENER = "android:activity.transitionCompleteListener";
    private static final String KEY_TRANSITION_IS_RETURNING = "android:activity.transitionIsReturning";
    private static final String KEY_TRANSITION_SHARED_ELEMENTS = "android:activity.sharedElementNames";
    private static final String KEY_USAGE_TIME_REPORT = "android:activity.usageTimeReport";
    private static final String TAG = "ActivityOptions";
    private AppTransitionAnimationSpec[] mAnimSpecs;
    private IRemoteCallback mAnimationFinishedListener;
    private IRemoteCallback mAnimationStartedListener;
    private int mAnimationType;
    private Bundle mAppVerificationBundle;
    private boolean mAvoidMoveToFront;
    private int mCustomEnterResId;
    private int mCustomExitResId;
    private int mCustomInPlaceResId;
    private boolean mDisallowEnterPictureInPictureWhileLaunching;
    private int mExitCoordinatorIndex;
    private boolean mFreezeRecentTasksReordering;
    private int mHeight;
    private boolean mIsReturning;
    @WindowConfiguration.ActivityType
    private int mLaunchActivityType;
    private Rect mLaunchBounds;
    private int mLaunchDisplayId;
    private int mLaunchTaskId;
    @WindowConfiguration.WindowingMode
    private int mLaunchWindowingMode;
    private boolean mLockTaskMode;
    private String mPackageName;
    private int mPendingIntentLaunchFlags;
    private RemoteAnimationAdapter mRemoteAnimationAdapter;
    private int mResultCode;
    private Intent mResultData;
    private int mRotationAnimationHint;
    private ArrayList<String> mSharedElementNames;
    private IAppTransitionAnimationSpecsFuture mSpecsFuture;
    private int mSplitScreenCreateMode;
    private int mStartX;
    private int mStartY;
    private boolean mTaskOverlay;
    private boolean mTaskOverlayCanResume;
    private Bitmap mThumbnail;
    private ResultReceiver mTransitionReceiver;
    private PendingIntent mUsageTimeReport;
    private int mWidth;

    /* loaded from: classes.dex */
    public interface OnAnimationFinishedListener {
        void onAnimationFinished();
    }

    /* loaded from: classes.dex */
    public interface OnAnimationStartedListener {
        void onAnimationStarted();
    }

    public static ActivityOptions makeCustomAnimation(Context context, int enterResId, int exitResId) {
        return makeCustomAnimation(context, enterResId, exitResId, null, null);
    }

    @UnsupportedAppUsage
    public static ActivityOptions makeCustomAnimation(Context context, int enterResId, int exitResId, Handler handler, OnAnimationStartedListener listener) {
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = context.getPackageName();
        opts.mAnimationType = 1;
        opts.mCustomEnterResId = enterResId;
        opts.mCustomExitResId = exitResId;
        opts.setOnAnimationStartedListener(handler, listener);
        return opts;
    }

    public static ActivityOptions makeCustomInPlaceAnimation(Context context, int animId) {
        if (animId == 0) {
            throw new RuntimeException("You must specify a valid animation.");
        }
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = context.getPackageName();
        opts.mAnimationType = 10;
        opts.mCustomInPlaceResId = animId;
        return opts;
    }

    private void setOnAnimationStartedListener(final Handler handler, final OnAnimationStartedListener listener) {
        if (listener != null) {
            this.mAnimationStartedListener = new IRemoteCallback.Stub() { // from class: android.app.ActivityOptions.1
                @Override // android.os.IRemoteCallback
                public void sendResult(Bundle data) throws RemoteException {
                    handler.post(new Runnable() { // from class: android.app.ActivityOptions.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            listener.onAnimationStarted();
                        }
                    });
                }
            };
        }
    }

    private void setOnAnimationFinishedListener(final Handler handler, final OnAnimationFinishedListener listener) {
        if (listener != null) {
            this.mAnimationFinishedListener = new IRemoteCallback.Stub() { // from class: android.app.ActivityOptions.2
                @Override // android.os.IRemoteCallback
                public void sendResult(Bundle data) throws RemoteException {
                    handler.post(new Runnable() { // from class: android.app.ActivityOptions.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            listener.onAnimationFinished();
                        }
                    });
                }
            };
        }
    }

    public static ActivityOptions makeScaleUpAnimation(View source, int startX, int startY, int width, int height) {
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = 2;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.mWidth = width;
        opts.mHeight = height;
        return opts;
    }

    public static ActivityOptions makeClipRevealAnimation(View source, int startX, int startY, int width, int height) {
        ActivityOptions opts = new ActivityOptions();
        opts.mAnimationType = 11;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.mWidth = width;
        opts.mHeight = height;
        return opts;
    }

    public static ActivityOptions makeOpenCrossProfileAppsAnimation() {
        ActivityOptions options = new ActivityOptions();
        options.mAnimationType = 12;
        return options;
    }

    public static ActivityOptions makeThumbnailScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY) {
        return makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY, null);
    }

    private static ActivityOptions makeThumbnailScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY, OnAnimationStartedListener listener) {
        return makeThumbnailAnimation(source, thumbnail, startX, startY, listener, true);
    }

    private static ActivityOptions makeThumbnailAnimation(View source, Bitmap thumbnail, int startX, int startY, OnAnimationStartedListener listener, boolean scaleUp) {
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = scaleUp ? 3 : 4;
        opts.mThumbnail = thumbnail;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.setOnAnimationStartedListener(source.getHandler(), listener);
        return opts;
    }

    @UnsupportedAppUsage
    public static ActivityOptions makeMultiThumbFutureAspectScaleAnimation(Context context, Handler handler, IAppTransitionAnimationSpecsFuture specsFuture, OnAnimationStartedListener listener, boolean scaleUp) {
        int i;
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = context.getPackageName();
        if (scaleUp) {
            i = 8;
        } else {
            i = 9;
        }
        opts.mAnimationType = i;
        opts.mSpecsFuture = specsFuture;
        opts.setOnAnimationStartedListener(handler, listener);
        return opts;
    }

    public static ActivityOptions makeThumbnailAspectScaleDownAnimation(View source, Bitmap thumbnail, int startX, int startY, int targetWidth, int targetHeight, Handler handler, OnAnimationStartedListener listener) {
        return makeAspectScaledThumbnailAnimation(source, thumbnail, startX, startY, targetWidth, targetHeight, handler, listener, false);
    }

    private static ActivityOptions makeAspectScaledThumbnailAnimation(View source, Bitmap thumbnail, int startX, int startY, int targetWidth, int targetHeight, Handler handler, OnAnimationStartedListener listener, boolean scaleUp) {
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = scaleUp ? 8 : 9;
        opts.mThumbnail = thumbnail;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.mWidth = targetWidth;
        opts.mHeight = targetHeight;
        opts.setOnAnimationStartedListener(handler, listener);
        return opts;
    }

    public static ActivityOptions makeThumbnailAspectScaleDownAnimation(View source, AppTransitionAnimationSpec[] specs, Handler handler, OnAnimationStartedListener onAnimationStartedListener, OnAnimationFinishedListener onAnimationFinishedListener) {
        ActivityOptions opts = new ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = 9;
        opts.mAnimSpecs = specs;
        opts.setOnAnimationStartedListener(handler, onAnimationStartedListener);
        opts.setOnAnimationFinishedListener(handler, onAnimationFinishedListener);
        return opts;
    }

    public static ActivityOptions makeSceneTransitionAnimation(Activity activity, View sharedElement, String sharedElementName) {
        return makeSceneTransitionAnimation(activity, Pair.create(sharedElement, sharedElementName));
    }

    @SafeVarargs
    public static ActivityOptions makeSceneTransitionAnimation(Activity activity, Pair<View, String>... sharedElements) {
        ActivityOptions opts = new ActivityOptions();
        makeSceneTransitionAnimation(activity, activity.getWindow(), opts, activity.mExitTransitionListener, sharedElements);
        return opts;
    }

    @SafeVarargs
    public static ActivityOptions startSharedElementAnimation(Window window, Pair<View, String>... sharedElements) {
        ActivityOptions opts = new ActivityOptions();
        View decorView = window.getDecorView();
        if (decorView == null) {
            return opts;
        }
        ExitTransitionCoordinator exit = makeSceneTransitionAnimation((Activity) null, window, opts, (SharedElementCallback) null, sharedElements);
        if (exit != null) {
            HideWindowListener listener = new HideWindowListener(window, exit);
            exit.setHideSharedElementsCallback(listener);
            exit.startExit();
        }
        return opts;
    }

    public static void stopSharedElementAnimation(Window window) {
        ExitTransitionCoordinator exit;
        View decorView = window.getDecorView();
        if (decorView != null && (exit = (ExitTransitionCoordinator) decorView.getTag(R.id.cross_task_transition)) != null) {
            exit.cancelPendingTransitions();
            decorView.setTagInternal(R.id.cross_task_transition, null);
            TransitionManager.endTransitions((ViewGroup) decorView);
            exit.resetViews();
            exit.clearState();
            decorView.setVisibility(0);
        }
    }

    static ExitTransitionCoordinator makeSceneTransitionAnimation(Activity activity, Window window, ActivityOptions opts, SharedElementCallback callback, Pair<View, String>[] sharedElements) {
        if (!window.hasFeature(13)) {
            opts.mAnimationType = 6;
            return null;
        }
        opts.mAnimationType = 5;
        ArrayList<String> names = new ArrayList<>();
        ArrayList<View> views = new ArrayList<>();
        if (sharedElements != null) {
            for (Pair<View, String> sharedElement : sharedElements) {
                String sharedElementName = sharedElement.second;
                if (sharedElementName == null) {
                    throw new IllegalArgumentException("Shared element name must not be null");
                }
                names.add(sharedElementName);
                View view = sharedElement.first;
                if (view == null) {
                    throw new IllegalArgumentException("Shared element must not be null");
                }
                views.add(sharedElement.first);
            }
        }
        ExitTransitionCoordinator exit = new ExitTransitionCoordinator(activity, window, callback, names, names, views, false);
        opts.mTransitionReceiver = exit;
        opts.mSharedElementNames = names;
        opts.mIsReturning = activity == null;
        if (activity == null) {
            opts.mExitCoordinatorIndex = -1;
        } else {
            opts.mExitCoordinatorIndex = activity.mActivityTransitionState.addExitTransitionCoordinator(exit);
        }
        return exit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ActivityOptions makeSceneTransitionAnimation(Activity activity, ExitTransitionCoordinator exitCoordinator, ArrayList<String> sharedElementNames, int resultCode, Intent resultData) {
        ActivityOptions opts = new ActivityOptions();
        opts.mAnimationType = 5;
        opts.mSharedElementNames = sharedElementNames;
        opts.mTransitionReceiver = exitCoordinator;
        opts.mIsReturning = true;
        opts.mResultCode = resultCode;
        opts.mResultData = resultData;
        opts.mExitCoordinatorIndex = activity.mActivityTransitionState.addExitTransitionCoordinator(exitCoordinator);
        return opts;
    }

    public static ActivityOptions makeTaskLaunchBehind() {
        ActivityOptions opts = new ActivityOptions();
        opts.mAnimationType = 7;
        return opts;
    }

    public static ActivityOptions makeBasic() {
        ActivityOptions opts = new ActivityOptions();
        return opts;
    }

    @UnsupportedAppUsage
    public static ActivityOptions makeRemoteAnimation(RemoteAnimationAdapter remoteAnimationAdapter) {
        ActivityOptions opts = new ActivityOptions();
        opts.mRemoteAnimationAdapter = remoteAnimationAdapter;
        opts.mAnimationType = 13;
        return opts;
    }

    public boolean getLaunchTaskBehind() {
        return this.mAnimationType == 7;
    }

    private ActivityOptions() {
        this.mAnimationType = 0;
        this.mLaunchDisplayId = -1;
        this.mLaunchWindowingMode = 0;
        this.mLaunchActivityType = 0;
        this.mLaunchTaskId = -1;
        this.mSplitScreenCreateMode = 0;
        this.mLockTaskMode = false;
        this.mRotationAnimationHint = -1;
    }

    public ActivityOptions(Bundle opts) {
        this.mAnimationType = 0;
        this.mLaunchDisplayId = -1;
        this.mLaunchWindowingMode = 0;
        this.mLaunchActivityType = 0;
        this.mLaunchTaskId = -1;
        this.mSplitScreenCreateMode = 0;
        this.mLockTaskMode = false;
        this.mRotationAnimationHint = -1;
        opts.setDefusable(true);
        this.mPackageName = opts.getString(KEY_PACKAGE_NAME);
        try {
            this.mUsageTimeReport = (PendingIntent) opts.getParcelable(KEY_USAGE_TIME_REPORT);
        } catch (RuntimeException e) {
            Slog.w(TAG, e);
        }
        this.mLaunchBounds = (Rect) opts.getParcelable(KEY_LAUNCH_BOUNDS);
        this.mAnimationType = opts.getInt(KEY_ANIM_TYPE);
        switch (this.mAnimationType) {
            case 1:
                this.mCustomEnterResId = opts.getInt(KEY_ANIM_ENTER_RES_ID, 0);
                this.mCustomExitResId = opts.getInt(KEY_ANIM_EXIT_RES_ID, 0);
                this.mAnimationStartedListener = IRemoteCallback.Stub.asInterface(opts.getBinder(KEY_ANIM_START_LISTENER));
                break;
            case 2:
            case 11:
                this.mStartX = opts.getInt(KEY_ANIM_START_X, 0);
                this.mStartY = opts.getInt(KEY_ANIM_START_Y, 0);
                this.mWidth = opts.getInt(KEY_ANIM_WIDTH, 0);
                this.mHeight = opts.getInt(KEY_ANIM_HEIGHT, 0);
                break;
            case 3:
            case 4:
            case 8:
            case 9:
                GraphicBuffer buffer = (GraphicBuffer) opts.getParcelable(KEY_ANIM_THUMBNAIL);
                if (buffer != null) {
                    this.mThumbnail = Bitmap.wrapHardwareBuffer(buffer, (ColorSpace) null);
                }
                this.mStartX = opts.getInt(KEY_ANIM_START_X, 0);
                this.mStartY = opts.getInt(KEY_ANIM_START_Y, 0);
                this.mWidth = opts.getInt(KEY_ANIM_WIDTH, 0);
                this.mHeight = opts.getInt(KEY_ANIM_HEIGHT, 0);
                this.mAnimationStartedListener = IRemoteCallback.Stub.asInterface(opts.getBinder(KEY_ANIM_START_LISTENER));
                break;
            case 5:
                this.mTransitionReceiver = (ResultReceiver) opts.getParcelable(KEY_TRANSITION_COMPLETE_LISTENER);
                this.mIsReturning = opts.getBoolean(KEY_TRANSITION_IS_RETURNING, false);
                this.mSharedElementNames = opts.getStringArrayList(KEY_TRANSITION_SHARED_ELEMENTS);
                this.mResultData = (Intent) opts.getParcelable(KEY_RESULT_DATA);
                this.mResultCode = opts.getInt(KEY_RESULT_CODE);
                this.mExitCoordinatorIndex = opts.getInt(KEY_EXIT_COORDINATOR_INDEX);
                break;
            case 10:
                this.mCustomInPlaceResId = opts.getInt(KEY_ANIM_IN_PLACE_RES_ID, 0);
                break;
        }
        this.mLockTaskMode = opts.getBoolean(KEY_LOCK_TASK_MODE, false);
        this.mLaunchDisplayId = opts.getInt(KEY_LAUNCH_DISPLAY_ID, -1);
        this.mLaunchWindowingMode = opts.getInt(KEY_LAUNCH_WINDOWING_MODE, 0);
        this.mLaunchActivityType = opts.getInt(KEY_LAUNCH_ACTIVITY_TYPE, 0);
        this.mLaunchTaskId = opts.getInt(KEY_LAUNCH_TASK_ID, -1);
        this.mPendingIntentLaunchFlags = opts.getInt(KEY_PENDING_INTENT_LAUNCH_FLAGS, 0);
        this.mTaskOverlay = opts.getBoolean(KEY_TASK_OVERLAY, false);
        this.mTaskOverlayCanResume = opts.getBoolean(KEY_TASK_OVERLAY_CAN_RESUME, false);
        this.mAvoidMoveToFront = opts.getBoolean(KEY_AVOID_MOVE_TO_FRONT, false);
        this.mFreezeRecentTasksReordering = opts.getBoolean(KEY_FREEZE_RECENT_TASKS_REORDERING, false);
        this.mSplitScreenCreateMode = opts.getInt(KEY_SPLIT_SCREEN_CREATE_MODE, 0);
        this.mDisallowEnterPictureInPictureWhileLaunching = opts.getBoolean(KEY_DISALLOW_ENTER_PICTURE_IN_PICTURE_WHILE_LAUNCHING, false);
        if (opts.containsKey(KEY_ANIM_SPECS)) {
            Parcelable[] specs = opts.getParcelableArray(KEY_ANIM_SPECS);
            this.mAnimSpecs = new AppTransitionAnimationSpec[specs.length];
            for (int i = specs.length - 1; i >= 0; i--) {
                this.mAnimSpecs[i] = (AppTransitionAnimationSpec) specs[i];
            }
        }
        if (opts.containsKey(KEY_ANIMATION_FINISHED_LISTENER)) {
            this.mAnimationFinishedListener = IRemoteCallback.Stub.asInterface(opts.getBinder(KEY_ANIMATION_FINISHED_LISTENER));
        }
        this.mRotationAnimationHint = opts.getInt(KEY_ROTATION_ANIMATION_HINT, -1);
        this.mAppVerificationBundle = opts.getBundle(KEY_INSTANT_APP_VERIFICATION_BUNDLE);
        if (opts.containsKey(KEY_SPECS_FUTURE)) {
            this.mSpecsFuture = IAppTransitionAnimationSpecsFuture.Stub.asInterface(opts.getBinder(KEY_SPECS_FUTURE));
        }
        this.mRemoteAnimationAdapter = (RemoteAnimationAdapter) opts.getParcelable(KEY_REMOTE_ANIMATION_ADAPTER);
    }

    public ActivityOptions setLaunchBounds(Rect screenSpacePixelRect) {
        this.mLaunchBounds = screenSpacePixelRect != null ? new Rect(screenSpacePixelRect) : null;
        return this;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public Rect getLaunchBounds() {
        return this.mLaunchBounds;
    }

    public int getAnimationType() {
        return 0;
    }

    public int getCustomEnterResId() {
        return this.mCustomEnterResId;
    }

    public int getCustomExitResId() {
        return this.mCustomExitResId;
    }

    public int getCustomInPlaceResId() {
        return this.mCustomInPlaceResId;
    }

    public GraphicBuffer getThumbnail() {
        Bitmap bitmap = this.mThumbnail;
        if (bitmap != null) {
            return bitmap.createGraphicBufferHandle();
        }
        return null;
    }

    public int getStartX() {
        return this.mStartX;
    }

    public int getStartY() {
        return this.mStartY;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public IRemoteCallback getOnAnimationStartListener() {
        return this.mAnimationStartedListener;
    }

    public IRemoteCallback getAnimationFinishedListener() {
        return this.mAnimationFinishedListener;
    }

    public int getExitCoordinatorKey() {
        return this.mExitCoordinatorIndex;
    }

    public void abort() {
        IRemoteCallback iRemoteCallback = this.mAnimationStartedListener;
        if (iRemoteCallback != null) {
            try {
                iRemoteCallback.sendResult(null);
            } catch (RemoteException e) {
            }
        }
    }

    public boolean isReturning() {
        return this.mIsReturning;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCrossTask() {
        return this.mExitCoordinatorIndex < 0;
    }

    public ArrayList<String> getSharedElementNames() {
        return this.mSharedElementNames;
    }

    public ResultReceiver getResultReceiver() {
        return this.mTransitionReceiver;
    }

    public int getResultCode() {
        return this.mResultCode;
    }

    public Intent getResultData() {
        return this.mResultData;
    }

    public PendingIntent getUsageTimeReport() {
        return this.mUsageTimeReport;
    }

    public AppTransitionAnimationSpec[] getAnimSpecs() {
        return this.mAnimSpecs;
    }

    public IAppTransitionAnimationSpecsFuture getSpecsFuture() {
        return this.mSpecsFuture;
    }

    public RemoteAnimationAdapter getRemoteAnimationAdapter() {
        return this.mRemoteAnimationAdapter;
    }

    public void setRemoteAnimationAdapter(RemoteAnimationAdapter remoteAnimationAdapter) {
        this.mRemoteAnimationAdapter = remoteAnimationAdapter;
    }

    public static ActivityOptions fromBundle(Bundle bOptions) {
        if (bOptions != null) {
            return new ActivityOptions(bOptions);
        }
        return null;
    }

    public static void abort(ActivityOptions options) {
        if (options != null) {
            options.abort();
        }
    }

    public boolean getLockTaskMode() {
        return this.mLockTaskMode;
    }

    public ActivityOptions setLockTaskEnabled(boolean lockTaskMode) {
        this.mLockTaskMode = lockTaskMode;
        return this;
    }

    public int getLaunchDisplayId() {
        return this.mLaunchDisplayId;
    }

    public ActivityOptions setLaunchDisplayId(int launchDisplayId) {
        this.mLaunchDisplayId = launchDisplayId;
        return this;
    }

    public int getLaunchWindowingMode() {
        return this.mLaunchWindowingMode;
    }

    public void setLaunchWindowingMode(int windowingMode) {
        this.mLaunchWindowingMode = windowingMode;
    }

    public int getLaunchActivityType() {
        return this.mLaunchActivityType;
    }

    public void setLaunchActivityType(int activityType) {
        this.mLaunchActivityType = activityType;
    }

    public void setLaunchTaskId(int taskId) {
        this.mLaunchTaskId = taskId;
    }

    public int getLaunchTaskId() {
        return this.mLaunchTaskId;
    }

    public void setPendingIntentLaunchFlags(int flags) {
        this.mPendingIntentLaunchFlags = flags;
    }

    public int getPendingIntentLaunchFlags() {
        return this.mPendingIntentLaunchFlags;
    }

    public void setTaskOverlay(boolean taskOverlay, boolean canResume) {
        this.mTaskOverlay = taskOverlay;
        this.mTaskOverlayCanResume = canResume;
    }

    public boolean getTaskOverlay() {
        return this.mTaskOverlay;
    }

    public boolean canTaskOverlayResume() {
        return this.mTaskOverlayCanResume;
    }

    public void setAvoidMoveToFront() {
        this.mAvoidMoveToFront = true;
    }

    public boolean getAvoidMoveToFront() {
        return this.mAvoidMoveToFront;
    }

    public void setFreezeRecentTasksReordering() {
        this.mFreezeRecentTasksReordering = true;
    }

    public boolean freezeRecentTasksReordering() {
        return this.mFreezeRecentTasksReordering;
    }

    public int getSplitScreenCreateMode() {
        return this.mSplitScreenCreateMode;
    }

    @UnsupportedAppUsage
    public void setSplitScreenCreateMode(int splitScreenCreateMode) {
        this.mSplitScreenCreateMode = splitScreenCreateMode;
    }

    public void setDisallowEnterPictureInPictureWhileLaunching(boolean disallow) {
        this.mDisallowEnterPictureInPictureWhileLaunching = disallow;
    }

    public boolean disallowEnterPictureInPictureWhileLaunching() {
        return this.mDisallowEnterPictureInPictureWhileLaunching;
    }

    public void update(ActivityOptions otherOptions) {
        String str = otherOptions.mPackageName;
        if (str != null) {
            this.mPackageName = str;
        }
        this.mUsageTimeReport = otherOptions.mUsageTimeReport;
        this.mTransitionReceiver = null;
        this.mSharedElementNames = null;
        this.mIsReturning = false;
        this.mResultData = null;
        this.mResultCode = 0;
        this.mExitCoordinatorIndex = 0;
        this.mAnimationType = otherOptions.mAnimationType;
        switch (otherOptions.mAnimationType) {
            case 1:
                this.mCustomEnterResId = otherOptions.mCustomEnterResId;
                this.mCustomExitResId = otherOptions.mCustomExitResId;
                this.mThumbnail = null;
                IRemoteCallback iRemoteCallback = this.mAnimationStartedListener;
                if (iRemoteCallback != null) {
                    try {
                        iRemoteCallback.sendResult(null);
                    } catch (RemoteException e) {
                    }
                }
                this.mAnimationStartedListener = otherOptions.mAnimationStartedListener;
                break;
            case 2:
                this.mStartX = otherOptions.mStartX;
                this.mStartY = otherOptions.mStartY;
                this.mWidth = otherOptions.mWidth;
                this.mHeight = otherOptions.mHeight;
                IRemoteCallback iRemoteCallback2 = this.mAnimationStartedListener;
                if (iRemoteCallback2 != null) {
                    try {
                        iRemoteCallback2.sendResult(null);
                    } catch (RemoteException e2) {
                    }
                }
                this.mAnimationStartedListener = null;
                break;
            case 3:
            case 4:
            case 8:
            case 9:
                this.mThumbnail = otherOptions.mThumbnail;
                this.mStartX = otherOptions.mStartX;
                this.mStartY = otherOptions.mStartY;
                this.mWidth = otherOptions.mWidth;
                this.mHeight = otherOptions.mHeight;
                IRemoteCallback iRemoteCallback3 = this.mAnimationStartedListener;
                if (iRemoteCallback3 != null) {
                    try {
                        iRemoteCallback3.sendResult(null);
                    } catch (RemoteException e3) {
                    }
                }
                this.mAnimationStartedListener = otherOptions.mAnimationStartedListener;
                break;
            case 5:
                this.mTransitionReceiver = otherOptions.mTransitionReceiver;
                this.mSharedElementNames = otherOptions.mSharedElementNames;
                this.mIsReturning = otherOptions.mIsReturning;
                this.mThumbnail = null;
                this.mAnimationStartedListener = null;
                this.mResultData = otherOptions.mResultData;
                this.mResultCode = otherOptions.mResultCode;
                this.mExitCoordinatorIndex = otherOptions.mExitCoordinatorIndex;
                break;
            case 10:
                this.mCustomInPlaceResId = otherOptions.mCustomInPlaceResId;
                break;
        }
        this.mLockTaskMode = otherOptions.mLockTaskMode;
        this.mAnimSpecs = otherOptions.mAnimSpecs;
        this.mAnimationFinishedListener = otherOptions.mAnimationFinishedListener;
        this.mSpecsFuture = otherOptions.mSpecsFuture;
        this.mRemoteAnimationAdapter = otherOptions.mRemoteAnimationAdapter;
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        String str = this.mPackageName;
        if (str != null) {
            b.putString(KEY_PACKAGE_NAME, str);
        }
        Rect rect = this.mLaunchBounds;
        if (rect != null) {
            b.putParcelable(KEY_LAUNCH_BOUNDS, rect);
        }
        b.putInt(KEY_ANIM_TYPE, this.mAnimationType);
        PendingIntent pendingIntent = this.mUsageTimeReport;
        if (pendingIntent != null) {
            b.putParcelable(KEY_USAGE_TIME_REPORT, pendingIntent);
        }
        switch (this.mAnimationType) {
            case 1:
                b.putInt(KEY_ANIM_ENTER_RES_ID, this.mCustomEnterResId);
                b.putInt(KEY_ANIM_EXIT_RES_ID, this.mCustomExitResId);
                IRemoteCallback iRemoteCallback = this.mAnimationStartedListener;
                b.putBinder(KEY_ANIM_START_LISTENER, iRemoteCallback != null ? iRemoteCallback.asBinder() : null);
                break;
            case 2:
            case 11:
                b.putInt(KEY_ANIM_START_X, this.mStartX);
                b.putInt(KEY_ANIM_START_Y, this.mStartY);
                b.putInt(KEY_ANIM_WIDTH, this.mWidth);
                b.putInt(KEY_ANIM_HEIGHT, this.mHeight);
                break;
            case 3:
            case 4:
            case 8:
            case 9:
                Bitmap bitmap = this.mThumbnail;
                if (bitmap != null) {
                    Bitmap hwBitmap = bitmap.copy(Bitmap.Config.HARDWARE, false);
                    if (hwBitmap != null) {
                        b.putParcelable(KEY_ANIM_THUMBNAIL, hwBitmap.createGraphicBufferHandle());
                    } else {
                        Slog.w(TAG, "Failed to copy thumbnail");
                    }
                }
                b.putInt(KEY_ANIM_START_X, this.mStartX);
                b.putInt(KEY_ANIM_START_Y, this.mStartY);
                b.putInt(KEY_ANIM_WIDTH, this.mWidth);
                b.putInt(KEY_ANIM_HEIGHT, this.mHeight);
                IRemoteCallback iRemoteCallback2 = this.mAnimationStartedListener;
                b.putBinder(KEY_ANIM_START_LISTENER, iRemoteCallback2 != null ? iRemoteCallback2.asBinder() : null);
                break;
            case 5:
                ResultReceiver resultReceiver = this.mTransitionReceiver;
                if (resultReceiver != null) {
                    b.putParcelable(KEY_TRANSITION_COMPLETE_LISTENER, resultReceiver);
                }
                b.putBoolean(KEY_TRANSITION_IS_RETURNING, this.mIsReturning);
                b.putStringArrayList(KEY_TRANSITION_SHARED_ELEMENTS, this.mSharedElementNames);
                b.putParcelable(KEY_RESULT_DATA, this.mResultData);
                b.putInt(KEY_RESULT_CODE, this.mResultCode);
                b.putInt(KEY_EXIT_COORDINATOR_INDEX, this.mExitCoordinatorIndex);
                break;
            case 10:
                b.putInt(KEY_ANIM_IN_PLACE_RES_ID, this.mCustomInPlaceResId);
                break;
        }
        boolean z = this.mLockTaskMode;
        if (z) {
            b.putBoolean(KEY_LOCK_TASK_MODE, z);
        }
        int i = this.mLaunchDisplayId;
        if (i != -1) {
            b.putInt(KEY_LAUNCH_DISPLAY_ID, i);
        }
        int i2 = this.mLaunchWindowingMode;
        if (i2 != 0) {
            b.putInt(KEY_LAUNCH_WINDOWING_MODE, i2);
        }
        int i3 = this.mLaunchActivityType;
        if (i3 != 0) {
            b.putInt(KEY_LAUNCH_ACTIVITY_TYPE, i3);
        }
        int i4 = this.mLaunchTaskId;
        if (i4 != -1) {
            b.putInt(KEY_LAUNCH_TASK_ID, i4);
        }
        int i5 = this.mPendingIntentLaunchFlags;
        if (i5 != 0) {
            b.putInt(KEY_PENDING_INTENT_LAUNCH_FLAGS, i5);
        }
        boolean z2 = this.mTaskOverlay;
        if (z2) {
            b.putBoolean(KEY_TASK_OVERLAY, z2);
        }
        boolean z3 = this.mTaskOverlayCanResume;
        if (z3) {
            b.putBoolean(KEY_TASK_OVERLAY_CAN_RESUME, z3);
        }
        boolean z4 = this.mAvoidMoveToFront;
        if (z4) {
            b.putBoolean(KEY_AVOID_MOVE_TO_FRONT, z4);
        }
        boolean z5 = this.mFreezeRecentTasksReordering;
        if (z5) {
            b.putBoolean(KEY_FREEZE_RECENT_TASKS_REORDERING, z5);
        }
        int i6 = this.mSplitScreenCreateMode;
        if (i6 != 0) {
            b.putInt(KEY_SPLIT_SCREEN_CREATE_MODE, i6);
        }
        boolean z6 = this.mDisallowEnterPictureInPictureWhileLaunching;
        if (z6) {
            b.putBoolean(KEY_DISALLOW_ENTER_PICTURE_IN_PICTURE_WHILE_LAUNCHING, z6);
        }
        AppTransitionAnimationSpec[] appTransitionAnimationSpecArr = this.mAnimSpecs;
        if (appTransitionAnimationSpecArr != null) {
            b.putParcelableArray(KEY_ANIM_SPECS, appTransitionAnimationSpecArr);
        }
        IRemoteCallback iRemoteCallback3 = this.mAnimationFinishedListener;
        if (iRemoteCallback3 != null) {
            b.putBinder(KEY_ANIMATION_FINISHED_LISTENER, iRemoteCallback3.asBinder());
        }
        IAppTransitionAnimationSpecsFuture iAppTransitionAnimationSpecsFuture = this.mSpecsFuture;
        if (iAppTransitionAnimationSpecsFuture != null) {
            b.putBinder(KEY_SPECS_FUTURE, iAppTransitionAnimationSpecsFuture.asBinder());
        }
        int i7 = this.mRotationAnimationHint;
        if (i7 != -1) {
            b.putInt(KEY_ROTATION_ANIMATION_HINT, i7);
        }
        Bundle bundle = this.mAppVerificationBundle;
        if (bundle != null) {
            b.putBundle(KEY_INSTANT_APP_VERIFICATION_BUNDLE, bundle);
        }
        RemoteAnimationAdapter remoteAnimationAdapter = this.mRemoteAnimationAdapter;
        if (remoteAnimationAdapter != null) {
            b.putParcelable(KEY_REMOTE_ANIMATION_ADAPTER, remoteAnimationAdapter);
        }
        return b;
    }

    public void requestUsageTimeReport(PendingIntent receiver) {
        this.mUsageTimeReport = receiver;
    }

    public ActivityOptions forTargetActivity() {
        if (this.mAnimationType == 5) {
            ActivityOptions result = new ActivityOptions();
            result.update(this);
            return result;
        }
        return null;
    }

    public int getRotationAnimationHint() {
        return this.mRotationAnimationHint;
    }

    public void setRotationAnimationHint(int hint) {
        this.mRotationAnimationHint = hint;
    }

    public Bundle popAppVerificationBundle() {
        Bundle out = this.mAppVerificationBundle;
        this.mAppVerificationBundle = null;
        return out;
    }

    public ActivityOptions setAppVerificationBundle(Bundle bundle) {
        this.mAppVerificationBundle = bundle;
        return this;
    }

    public String toString() {
        return "ActivityOptions(" + hashCode() + "), mPackageName=" + this.mPackageName + ", mAnimationType=" + this.mAnimationType + ", mStartX=" + this.mStartX + ", mStartY=" + this.mStartY + ", mWidth=" + this.mWidth + ", mHeight=" + this.mHeight;
    }

    /* loaded from: classes.dex */
    private static class HideWindowListener extends TransitionListenerAdapter implements ExitTransitionCoordinator.HideSharedElementsCallback {
        private final ExitTransitionCoordinator mExit;
        private boolean mSharedElementHidden;
        private ArrayList<View> mSharedElements;
        private boolean mTransitionEnded;
        private final boolean mWaitingForTransition;
        private final Window mWindow;

        public HideWindowListener(Window window, ExitTransitionCoordinator exit) {
            this.mWindow = window;
            this.mExit = exit;
            this.mSharedElements = new ArrayList<>(exit.mSharedElements);
            Transition transition = this.mWindow.getExitTransition();
            if (transition != null) {
                transition.addListener(this);
                this.mWaitingForTransition = true;
            } else {
                this.mWaitingForTransition = false;
            }
            View decorView = this.mWindow.getDecorView();
            if (decorView != null) {
                if (decorView.getTag(R.id.cross_task_transition) != null) {
                    throw new IllegalStateException("Cannot start a transition while one is running");
                }
                decorView.setTagInternal(R.id.cross_task_transition, exit);
            }
        }

        @Override // android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
        public void onTransitionEnd(Transition transition) {
            this.mTransitionEnded = true;
            hideWhenDone();
            transition.removeListener(this);
        }

        @Override // android.app.ExitTransitionCoordinator.HideSharedElementsCallback
        public void hideSharedElements() {
            this.mSharedElementHidden = true;
            hideWhenDone();
        }

        private void hideWhenDone() {
            if (this.mSharedElementHidden) {
                if (!this.mWaitingForTransition || this.mTransitionEnded) {
                    this.mExit.resetViews();
                    int numSharedElements = this.mSharedElements.size();
                    for (int i = 0; i < numSharedElements; i++) {
                        View view = this.mSharedElements.get(i);
                        view.requestLayout();
                    }
                    View decorView = this.mWindow.getDecorView();
                    if (decorView != null) {
                        decorView.setTagInternal(R.id.cross_task_transition, null);
                        decorView.setVisibility(8);
                    }
                }
            }
        }
    }
}
