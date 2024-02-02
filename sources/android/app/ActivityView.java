package android.app;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.hardware.input.InputManager;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.IWindowManager;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import dalvik.system.CloseGuard;
import java.util.List;
/* loaded from: classes.dex */
public class ActivityView extends ViewGroup {
    private static final String DISPLAY_NAME = "ActivityViewVirtualDisplay";
    private static final String TAG = "ActivityView";
    private IActivityManager mActivityManager;
    private StateCallback mActivityViewCallback;
    private final CloseGuard mGuard;
    private IInputForwarder mInputForwarder;
    private final int[] mLocationOnScreen;
    private boolean mOpened;
    private Surface mSurface;
    private final SurfaceCallback mSurfaceCallback;
    private final SurfaceView mSurfaceView;
    private TaskStackListener mTaskStackListener;
    private VirtualDisplay mVirtualDisplay;

    private protected ActivityView(Context context) {
        this(context, null);
    }

    public synchronized ActivityView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public synchronized ActivityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mLocationOnScreen = new int[2];
        this.mGuard = CloseGuard.get();
        this.mActivityManager = ActivityManager.getService();
        this.mSurfaceView = new SurfaceView(context);
        this.mSurfaceCallback = new SurfaceCallback();
        this.mSurfaceView.getHolder().addCallback(this.mSurfaceCallback);
        addView(this.mSurfaceView);
        this.mOpened = true;
        this.mGuard.open("release");
    }

    /* loaded from: classes.dex */
    public static abstract class StateCallback {
        public abstract synchronized void onActivityViewDestroyed(ActivityView activityView);

        public abstract synchronized void onActivityViewReady(ActivityView activityView);

        public synchronized void onTaskMovedToFront(ActivityManager.StackInfo stackInfo) {
        }
    }

    public synchronized void setCallback(StateCallback callback) {
        this.mActivityViewCallback = callback;
        if (this.mVirtualDisplay != null && this.mActivityViewCallback != null) {
            this.mActivityViewCallback.onActivityViewReady(this);
        }
    }

    private protected void startActivity(Intent intent) {
        ActivityOptions options = prepareActivityOptions();
        getContext().startActivity(intent, options.toBundle());
    }

    public synchronized void startActivity(Intent intent, UserHandle user) {
        ActivityOptions options = prepareActivityOptions();
        getContext().startActivityAsUser(intent, options.toBundle(), user);
    }

    private protected void startActivity(PendingIntent pendingIntent) {
        ActivityOptions options = prepareActivityOptions();
        try {
            pendingIntent.send(null, 0, null, null, null, null, options.toBundle());
        } catch (PendingIntent.CanceledException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized ActivityOptions prepareActivityOptions() {
        if (this.mVirtualDisplay == null) {
            throw new IllegalStateException("Trying to start activity before ActivityView is ready.");
        }
        ActivityOptions options = ActivityOptions.makeBasic();
        options.setLaunchDisplayId(this.mVirtualDisplay.getDisplay().getDisplayId());
        return options;
    }

    private protected void release() {
        if (this.mVirtualDisplay == null) {
            throw new IllegalStateException("Trying to release container that is not initialized.");
        }
        performRelease();
    }

    public synchronized void onLocationChanged() {
        updateLocation();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mSurfaceView.layout(0, 0, r - l, b - t);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateLocation() {
        try {
            getLocationOnScreen(this.mLocationOnScreen);
            WindowManagerGlobal.getWindowSession().updateTapExcludeRegion(getWindow(), hashCode(), this.mLocationOnScreen[0], this.mLocationOnScreen[1], getWidth(), getHeight());
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        return injectInputEvent(event) || super.onTouchEvent(event);
    }

    @Override // android.view.View
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (event.isFromSource(2) && injectInputEvent(event)) {
            return true;
        }
        return super.onGenericMotionEvent(event);
    }

    private synchronized boolean injectInputEvent(InputEvent event) {
        if (this.mInputForwarder != null) {
            try {
                return this.mInputForwarder.forwardEvent(event);
            } catch (RemoteException e) {
                e.rethrowAsRuntimeException();
                return false;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SurfaceCallback implements SurfaceHolder.Callback {
        private SurfaceCallback() {
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            ActivityView.this.mSurface = ActivityView.this.mSurfaceView.getHolder().getSurface();
            if (ActivityView.this.mVirtualDisplay == null) {
                ActivityView.this.initVirtualDisplay();
                if (ActivityView.this.mVirtualDisplay != null && ActivityView.this.mActivityViewCallback != null) {
                    ActivityView.this.mActivityViewCallback.onActivityViewReady(ActivityView.this);
                }
            } else {
                ActivityView.this.mVirtualDisplay.setSurface(surfaceHolder.getSurface());
            }
            ActivityView.this.updateLocation();
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            if (ActivityView.this.mVirtualDisplay != null) {
                ActivityView.this.mVirtualDisplay.resize(width, height, ActivityView.this.getBaseDisplayDensity());
            }
            ActivityView.this.updateLocation();
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            ActivityView.this.mSurface.release();
            ActivityView.this.mSurface = null;
            if (ActivityView.this.mVirtualDisplay != null) {
                ActivityView.this.mVirtualDisplay.setSurface(null);
            }
            ActivityView.this.cleanTapExcludeRegion();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void initVirtualDisplay() {
        if (this.mVirtualDisplay != null) {
            throw new IllegalStateException("Trying to initialize for the second time.");
        }
        int width = this.mSurfaceView.getWidth();
        int height = this.mSurfaceView.getHeight();
        DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService(DisplayManager.class);
        this.mVirtualDisplay = displayManager.createVirtualDisplay("ActivityViewVirtualDisplay@" + System.identityHashCode(this), width, height, getBaseDisplayDensity(), this.mSurface, 9);
        if (this.mVirtualDisplay == null) {
            Log.e(TAG, "Failed to initialize ActivityView");
            return;
        }
        int displayId = this.mVirtualDisplay.getDisplay().getDisplayId();
        IWindowManager wm = WindowManagerGlobal.getWindowManagerService();
        try {
            wm.dontOverrideDisplayInfo(displayId);
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
        this.mInputForwarder = InputManager.getInstance().createInputForwarder(displayId);
        this.mTaskStackListener = new TaskStackListenerImpl();
        try {
            this.mActivityManager.registerTaskStackListener(this.mTaskStackListener);
        } catch (RemoteException e2) {
            Log.e(TAG, "Failed to register task stack listener", e2);
        }
    }

    private synchronized void performRelease() {
        boolean displayReleased;
        if (!this.mOpened) {
            return;
        }
        this.mSurfaceView.getHolder().removeCallback(this.mSurfaceCallback);
        if (this.mInputForwarder != null) {
            this.mInputForwarder = null;
        }
        cleanTapExcludeRegion();
        if (this.mTaskStackListener != null) {
            try {
                this.mActivityManager.unregisterTaskStackListener(this.mTaskStackListener);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to unregister task stack listener", e);
            }
            this.mTaskStackListener = null;
        }
        if (this.mVirtualDisplay != null) {
            this.mVirtualDisplay.release();
            this.mVirtualDisplay = null;
            displayReleased = true;
        } else {
            displayReleased = false;
        }
        if (this.mSurface != null) {
            this.mSurface.release();
            this.mSurface = null;
        }
        if (displayReleased && this.mActivityViewCallback != null) {
            this.mActivityViewCallback.onActivityViewDestroyed(this);
        }
        this.mGuard.close();
        this.mOpened = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void cleanTapExcludeRegion() {
        try {
            WindowManagerGlobal.getWindowSession().updateTapExcludeRegion(getWindow(), hashCode(), 0, 0, 0, 0);
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getBaseDisplayDensity() {
        WindowManager wm = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.densityDpi;
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mGuard != null) {
                this.mGuard.warnIfOpen();
                performRelease();
            }
        } finally {
            super.finalize();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class TaskStackListenerImpl extends TaskStackListener {
        private TaskStackListenerImpl() {
        }

        @Override // android.app.TaskStackListener, android.app.ITaskStackListener
        public synchronized void onTaskDescriptionChanged(int taskId, ActivityManager.TaskDescription td) throws RemoteException {
            ActivityManager.StackInfo stackInfo;
            if (ActivityView.this.mVirtualDisplay != null && (stackInfo = getTopMostStackInfo()) != null && taskId == stackInfo.taskIds[stackInfo.taskIds.length - 1]) {
                ActivityView.this.mSurfaceView.setResizeBackgroundColor(td.getBackgroundColor());
            }
        }

        @Override // android.app.ITaskStackListener
        public synchronized void onTaskMovedToFront(int taskId) throws RemoteException {
            ActivityManager.StackInfo stackInfo;
            if (ActivityView.this.mActivityViewCallback != null && (stackInfo = getTopMostStackInfo()) != null && taskId == stackInfo.taskIds[stackInfo.taskIds.length - 1]) {
                ActivityView.this.mActivityViewCallback.onTaskMovedToFront(stackInfo);
            }
        }

        private synchronized ActivityManager.StackInfo getTopMostStackInfo() throws RemoteException {
            int displayId = ActivityView.this.mVirtualDisplay.getDisplay().getDisplayId();
            List<ActivityManager.StackInfo> stackInfoList = ActivityView.this.mActivityManager.getAllStackInfos();
            int stackCount = stackInfoList.size();
            for (int i = 0; i < stackCount; i++) {
                ActivityManager.StackInfo stackInfo = stackInfoList.get(i);
                if (stackInfo.displayId == displayId) {
                    return stackInfo;
                }
            }
            return null;
        }
    }
}
