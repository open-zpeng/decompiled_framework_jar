package android.accessibilityservice;

import android.accessibilityservice.GestureDescription;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.graphics.Region;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.SomeArgs;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
/* loaded from: classes.dex */
public abstract class AccessibilityService extends Service {
    public static final int GESTURE_SWIPE_DOWN = 2;
    public static final int GESTURE_SWIPE_DOWN_AND_LEFT = 15;
    public static final int GESTURE_SWIPE_DOWN_AND_RIGHT = 16;
    public static final int GESTURE_SWIPE_DOWN_AND_UP = 8;
    public static final int GESTURE_SWIPE_LEFT = 3;
    public static final int GESTURE_SWIPE_LEFT_AND_DOWN = 10;
    public static final int GESTURE_SWIPE_LEFT_AND_RIGHT = 5;
    public static final int GESTURE_SWIPE_LEFT_AND_UP = 9;
    public static final int GESTURE_SWIPE_RIGHT = 4;
    public static final int GESTURE_SWIPE_RIGHT_AND_DOWN = 12;
    public static final int GESTURE_SWIPE_RIGHT_AND_LEFT = 6;
    public static final int GESTURE_SWIPE_RIGHT_AND_UP = 11;
    public static final int GESTURE_SWIPE_UP = 1;
    public static final int GESTURE_SWIPE_UP_AND_DOWN = 7;
    public static final int GESTURE_SWIPE_UP_AND_LEFT = 13;
    public static final int GESTURE_SWIPE_UP_AND_RIGHT = 14;
    public static final int GLOBAL_ACTION_BACK = 1;
    public static final int GLOBAL_ACTION_HOME = 2;
    public static final int GLOBAL_ACTION_LOCK_SCREEN = 8;
    public static final int GLOBAL_ACTION_NOTIFICATIONS = 4;
    public static final int GLOBAL_ACTION_POWER_DIALOG = 6;
    public static final int GLOBAL_ACTION_QUICK_SETTINGS = 5;
    public static final int GLOBAL_ACTION_RECENTS = 3;
    public static final int GLOBAL_ACTION_TAKE_SCREENSHOT = 9;
    public static final int GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN = 7;
    private static final String LOG_TAG = "AccessibilityService";
    public static final String SERVICE_INTERFACE = "android.accessibilityservice.AccessibilityService";
    public static final String SERVICE_META_DATA = "android.accessibilityservice";
    public static final int SHOW_MODE_AUTO = 0;
    public static final int SHOW_MODE_HIDDEN = 1;
    private AccessibilityButtonController mAccessibilityButtonController;
    private FingerprintGestureController mFingerprintGestureController;
    private SparseArray<GestureResultCallbackInfo> mGestureStatusCallbackInfos;
    private int mGestureStatusCallbackSequence;
    public protected AccessibilityServiceInfo mInfo;
    private MagnificationController mMagnificationController;
    private SoftKeyboardController mSoftKeyboardController;
    private WindowManager mWindowManager;
    public protected IBinder mWindowToken;
    private int mConnectionId = -1;
    private final Object mLock = new Object();

    /* loaded from: classes.dex */
    public interface Callbacks {
        synchronized void init(int i, IBinder iBinder);

        synchronized void onAccessibilityButtonAvailabilityChanged(boolean z);

        synchronized void onAccessibilityButtonClicked();

        synchronized void onAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        synchronized void onFingerprintCapturingGesturesChanged(boolean z);

        synchronized void onFingerprintGesture(int i);

        synchronized boolean onGesture(int i);

        synchronized void onInterrupt();

        synchronized boolean onKeyEvent(KeyEvent keyEvent);

        synchronized void onMagnificationChanged(Region region, float f, float f2, float f3);

        synchronized void onPerformGestureResult(int i, boolean z);

        synchronized void onServiceConnected();

        synchronized void onSoftKeyboardShowModeChanged(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SoftKeyboardShowMode {
    }

    public abstract void onAccessibilityEvent(AccessibilityEvent accessibilityEvent);

    public abstract void onInterrupt();

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchServiceConnected() {
        if (this.mMagnificationController != null) {
            this.mMagnificationController.onServiceConnected();
        }
        if (this.mSoftKeyboardController != null) {
            this.mSoftKeyboardController.onServiceConnected();
        }
        onServiceConnected();
    }

    protected void onServiceConnected() {
    }

    protected boolean onGesture(int gestureId) {
        return false;
    }

    protected boolean onKeyEvent(KeyEvent event) {
        return false;
    }

    public List<AccessibilityWindowInfo> getWindows() {
        return AccessibilityInteractionClient.getInstance().getWindows(this.mConnectionId);
    }

    public AccessibilityNodeInfo getRootInActiveWindow() {
        return AccessibilityInteractionClient.getInstance().getRootInActiveWindow(this.mConnectionId);
    }

    public final void disableSelf() {
        AccessibilityInteractionClient.getInstance();
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mConnectionId);
        if (connection != null) {
            try {
                connection.disableSelf();
            } catch (RemoteException re) {
                throw new RuntimeException(re);
            }
        }
    }

    public final MagnificationController getMagnificationController() {
        MagnificationController magnificationController;
        synchronized (this.mLock) {
            if (this.mMagnificationController == null) {
                this.mMagnificationController = new MagnificationController(this, this.mLock);
            }
            magnificationController = this.mMagnificationController;
        }
        return magnificationController;
    }

    public final FingerprintGestureController getFingerprintGestureController() {
        if (this.mFingerprintGestureController == null) {
            AccessibilityInteractionClient.getInstance();
            this.mFingerprintGestureController = new FingerprintGestureController(AccessibilityInteractionClient.getConnection(this.mConnectionId));
        }
        return this.mFingerprintGestureController;
    }

    public final boolean dispatchGesture(GestureDescription gesture, GestureResultCallback callback, Handler handler) {
        AccessibilityInteractionClient.getInstance();
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mConnectionId);
        if (connection == null) {
            return false;
        }
        List<GestureDescription.GestureStep> steps = GestureDescription.MotionEventGenerator.getGestureStepsFromGestureDescription(gesture, 100);
        try {
            synchronized (this.mLock) {
                this.mGestureStatusCallbackSequence++;
                if (callback != null) {
                    if (this.mGestureStatusCallbackInfos == null) {
                        this.mGestureStatusCallbackInfos = new SparseArray<>();
                    }
                    GestureResultCallbackInfo callbackInfo = new GestureResultCallbackInfo(gesture, callback, handler);
                    this.mGestureStatusCallbackInfos.put(this.mGestureStatusCallbackSequence, callbackInfo);
                }
                connection.sendGesture(this.mGestureStatusCallbackSequence, new ParceledListSlice(steps));
            }
            return true;
        } catch (RemoteException re) {
            throw new RuntimeException(re);
        }
    }

    synchronized void onPerformGestureResult(int sequence, final boolean completedSuccessfully) {
        final GestureResultCallbackInfo callbackInfo;
        if (this.mGestureStatusCallbackInfos == null) {
            return;
        }
        synchronized (this.mLock) {
            callbackInfo = this.mGestureStatusCallbackInfos.get(sequence);
        }
        if (callbackInfo != null && callbackInfo.gestureDescription != null && callbackInfo.callback != null) {
            if (callbackInfo.handler != null) {
                callbackInfo.handler.post(new Runnable() { // from class: android.accessibilityservice.AccessibilityService.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (completedSuccessfully) {
                            callbackInfo.callback.onCompleted(callbackInfo.gestureDescription);
                        } else {
                            callbackInfo.callback.onCancelled(callbackInfo.gestureDescription);
                        }
                    }
                });
            } else if (completedSuccessfully) {
                callbackInfo.callback.onCompleted(callbackInfo.gestureDescription);
            } else {
                callbackInfo.callback.onCancelled(callbackInfo.gestureDescription);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onMagnificationChanged(Region region, float scale, float centerX, float centerY) {
        if (this.mMagnificationController != null) {
            this.mMagnificationController.dispatchMagnificationChanged(region, scale, centerX, centerY);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onFingerprintCapturingGesturesChanged(boolean active) {
        getFingerprintGestureController().onGestureDetectionActiveChanged(active);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onFingerprintGesture(int gesture) {
        getFingerprintGestureController().onGesture(gesture);
    }

    /* loaded from: classes.dex */
    public static final class MagnificationController {
        private ArrayMap<OnMagnificationChangedListener, Handler> mListeners;
        private final Object mLock;
        private final AccessibilityService mService;

        /* loaded from: classes.dex */
        public interface OnMagnificationChangedListener {
            void onMagnificationChanged(MagnificationController magnificationController, Region region, float f, float f2, float f3);
        }

        synchronized MagnificationController(AccessibilityService service, Object lock) {
            this.mService = service;
            this.mLock = lock;
        }

        synchronized void onServiceConnected() {
            synchronized (this.mLock) {
                if (this.mListeners != null && !this.mListeners.isEmpty()) {
                    setMagnificationCallbackEnabled(true);
                }
            }
        }

        public void addListener(OnMagnificationChangedListener listener) {
            addListener(listener, null);
        }

        public void addListener(OnMagnificationChangedListener listener, Handler handler) {
            synchronized (this.mLock) {
                if (this.mListeners == null) {
                    this.mListeners = new ArrayMap<>();
                }
                boolean shouldEnableCallback = this.mListeners.isEmpty();
                this.mListeners.put(listener, handler);
                if (shouldEnableCallback) {
                    setMagnificationCallbackEnabled(true);
                }
            }
        }

        public boolean removeListener(OnMagnificationChangedListener listener) {
            boolean hasKey;
            if (this.mListeners == null) {
                return false;
            }
            synchronized (this.mLock) {
                int keyIndex = this.mListeners.indexOfKey(listener);
                hasKey = keyIndex >= 0;
                if (hasKey) {
                    this.mListeners.removeAt(keyIndex);
                }
                if (hasKey && this.mListeners.isEmpty()) {
                    setMagnificationCallbackEnabled(false);
                }
            }
            return hasKey;
        }

        private synchronized void setMagnificationCallbackEnabled(boolean enabled) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    connection.setMagnificationCallbackEnabled(enabled);
                } catch (RemoteException re) {
                    throw new RuntimeException(re);
                }
            }
        }

        synchronized void dispatchMagnificationChanged(final Region region, final float scale, final float centerX, final float centerY) {
            synchronized (this.mLock) {
                if (this.mListeners != null && !this.mListeners.isEmpty()) {
                    ArrayMap<OnMagnificationChangedListener, Handler> entries = new ArrayMap<>(this.mListeners);
                    int count = entries.size();
                    int i = 0;
                    while (true) {
                        int count2 = count;
                        if (i < count2) {
                            final OnMagnificationChangedListener listener = entries.keyAt(i);
                            Handler handler = entries.valueAt(i);
                            if (handler != null) {
                                handler.post(new Runnable() { // from class: android.accessibilityservice.AccessibilityService.MagnificationController.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        listener.onMagnificationChanged(MagnificationController.this, region, scale, centerX, centerY);
                                    }
                                });
                            } else {
                                listener.onMagnificationChanged(this, region, scale, centerX, centerY);
                            }
                            i++;
                            count = count2;
                        } else {
                            return;
                        }
                    }
                }
                Slog.d(AccessibilityService.LOG_TAG, "Received magnification changed callback with no listeners registered!");
                setMagnificationCallbackEnabled(false);
            }
        }

        public float getScale() {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    return connection.getMagnificationScale();
                } catch (RemoteException re) {
                    Log.w(AccessibilityService.LOG_TAG, "Failed to obtain scale", re);
                    re.rethrowFromSystemServer();
                    return 1.0f;
                }
            }
            return 1.0f;
        }

        public float getCenterX() {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    return connection.getMagnificationCenterX();
                } catch (RemoteException re) {
                    Log.w(AccessibilityService.LOG_TAG, "Failed to obtain center X", re);
                    re.rethrowFromSystemServer();
                    return 0.0f;
                }
            }
            return 0.0f;
        }

        public float getCenterY() {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    return connection.getMagnificationCenterY();
                } catch (RemoteException re) {
                    Log.w(AccessibilityService.LOG_TAG, "Failed to obtain center Y", re);
                    re.rethrowFromSystemServer();
                    return 0.0f;
                }
            }
            return 0.0f;
        }

        public Region getMagnificationRegion() {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    return connection.getMagnificationRegion();
                } catch (RemoteException re) {
                    Log.w(AccessibilityService.LOG_TAG, "Failed to obtain magnified region", re);
                    re.rethrowFromSystemServer();
                }
            }
            return Region.obtain();
        }

        public boolean reset(boolean animate) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    return connection.resetMagnification(animate);
                } catch (RemoteException re) {
                    Log.w(AccessibilityService.LOG_TAG, "Failed to reset", re);
                    re.rethrowFromSystemServer();
                    return false;
                }
            }
            return false;
        }

        public boolean setScale(float scale, boolean animate) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    return connection.setMagnificationScaleAndCenter(scale, Float.NaN, Float.NaN, animate);
                } catch (RemoteException re) {
                    Log.w(AccessibilityService.LOG_TAG, "Failed to set scale", re);
                    re.rethrowFromSystemServer();
                    return false;
                }
            }
            return false;
        }

        public boolean setCenter(float centerX, float centerY, boolean animate) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    return connection.setMagnificationScaleAndCenter(Float.NaN, centerX, centerY, animate);
                } catch (RemoteException re) {
                    Log.w(AccessibilityService.LOG_TAG, "Failed to set center", re);
                    re.rethrowFromSystemServer();
                    return false;
                }
            }
            return false;
        }
    }

    public final SoftKeyboardController getSoftKeyboardController() {
        SoftKeyboardController softKeyboardController;
        synchronized (this.mLock) {
            if (this.mSoftKeyboardController == null) {
                this.mSoftKeyboardController = new SoftKeyboardController(this, this.mLock);
            }
            softKeyboardController = this.mSoftKeyboardController;
        }
        return softKeyboardController;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onSoftKeyboardShowModeChanged(int showMode) {
        if (this.mSoftKeyboardController != null) {
            this.mSoftKeyboardController.dispatchSoftKeyboardShowModeChanged(showMode);
        }
    }

    /* loaded from: classes.dex */
    public static final class SoftKeyboardController {
        private ArrayMap<OnShowModeChangedListener, Handler> mListeners;
        private final Object mLock;
        private final AccessibilityService mService;

        /* loaded from: classes.dex */
        public interface OnShowModeChangedListener {
            void onShowModeChanged(SoftKeyboardController softKeyboardController, int i);
        }

        synchronized SoftKeyboardController(AccessibilityService service, Object lock) {
            this.mService = service;
            this.mLock = lock;
        }

        synchronized void onServiceConnected() {
            synchronized (this.mLock) {
                if (this.mListeners != null && !this.mListeners.isEmpty()) {
                    setSoftKeyboardCallbackEnabled(true);
                }
            }
        }

        public void addOnShowModeChangedListener(OnShowModeChangedListener listener) {
            addOnShowModeChangedListener(listener, null);
        }

        public void addOnShowModeChangedListener(OnShowModeChangedListener listener, Handler handler) {
            synchronized (this.mLock) {
                if (this.mListeners == null) {
                    this.mListeners = new ArrayMap<>();
                }
                boolean shouldEnableCallback = this.mListeners.isEmpty();
                this.mListeners.put(listener, handler);
                if (shouldEnableCallback) {
                    setSoftKeyboardCallbackEnabled(true);
                }
            }
        }

        public boolean removeOnShowModeChangedListener(OnShowModeChangedListener listener) {
            boolean hasKey;
            if (this.mListeners == null) {
                return false;
            }
            synchronized (this.mLock) {
                int keyIndex = this.mListeners.indexOfKey(listener);
                hasKey = keyIndex >= 0;
                if (hasKey) {
                    this.mListeners.removeAt(keyIndex);
                }
                if (hasKey && this.mListeners.isEmpty()) {
                    setSoftKeyboardCallbackEnabled(false);
                }
            }
            return hasKey;
        }

        private synchronized void setSoftKeyboardCallbackEnabled(boolean enabled) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    connection.setSoftKeyboardCallbackEnabled(enabled);
                } catch (RemoteException re) {
                    throw new RuntimeException(re);
                }
            }
        }

        synchronized void dispatchSoftKeyboardShowModeChanged(final int showMode) {
            synchronized (this.mLock) {
                if (this.mListeners != null && !this.mListeners.isEmpty()) {
                    ArrayMap<OnShowModeChangedListener, Handler> entries = new ArrayMap<>(this.mListeners);
                    int count = entries.size();
                    for (int i = 0; i < count; i++) {
                        final OnShowModeChangedListener listener = entries.keyAt(i);
                        Handler handler = entries.valueAt(i);
                        if (handler != null) {
                            handler.post(new Runnable() { // from class: android.accessibilityservice.AccessibilityService.SoftKeyboardController.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    listener.onShowModeChanged(SoftKeyboardController.this, showMode);
                                }
                            });
                        } else {
                            listener.onShowModeChanged(this, showMode);
                        }
                    }
                    return;
                }
                Slog.w(AccessibilityService.LOG_TAG, "Received soft keyboard show mode changed callback with no listeners registered!");
                setSoftKeyboardCallbackEnabled(false);
            }
        }

        public int getShowMode() {
            try {
                return Settings.Secure.getInt(this.mService.getContentResolver(), Settings.Secure.ACCESSIBILITY_SOFT_KEYBOARD_MODE);
            } catch (Settings.SettingNotFoundException e) {
                Log.v(AccessibilityService.LOG_TAG, "Failed to obtain the soft keyboard mode", e);
                return 0;
            }
        }

        public boolean setShowMode(int showMode) {
            AccessibilityInteractionClient.getInstance();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mService.mConnectionId);
            if (connection != null) {
                try {
                    return connection.setSoftKeyboardShowMode(showMode);
                } catch (RemoteException re) {
                    Log.w(AccessibilityService.LOG_TAG, "Failed to set soft keyboard behavior", re);
                    re.rethrowFromSystemServer();
                    return false;
                }
            }
            return false;
        }
    }

    public final AccessibilityButtonController getAccessibilityButtonController() {
        AccessibilityButtonController accessibilityButtonController;
        synchronized (this.mLock) {
            if (this.mAccessibilityButtonController == null) {
                AccessibilityInteractionClient.getInstance();
                this.mAccessibilityButtonController = new AccessibilityButtonController(AccessibilityInteractionClient.getConnection(this.mConnectionId));
            }
            accessibilityButtonController = this.mAccessibilityButtonController;
        }
        return accessibilityButtonController;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onAccessibilityButtonClicked() {
        getAccessibilityButtonController().dispatchAccessibilityButtonClicked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onAccessibilityButtonAvailabilityChanged(boolean available) {
        getAccessibilityButtonController().dispatchAccessibilityButtonAvailabilityChanged(available);
    }

    public final boolean performGlobalAction(int action) {
        AccessibilityInteractionClient.getInstance();
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mConnectionId);
        if (connection != null) {
            try {
                return connection.performGlobalAction(action);
            } catch (RemoteException re) {
                Log.w(LOG_TAG, "Error while calling performGlobalAction", re);
                re.rethrowFromSystemServer();
                return false;
            }
        }
        return false;
    }

    public AccessibilityNodeInfo findFocus(int focus) {
        return AccessibilityInteractionClient.getInstance().findFocus(this.mConnectionId, -2, AccessibilityNodeInfo.ROOT_NODE_ID, focus);
    }

    public final AccessibilityServiceInfo getServiceInfo() {
        AccessibilityInteractionClient.getInstance();
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mConnectionId);
        if (connection != null) {
            try {
                return connection.getServiceInfo();
            } catch (RemoteException re) {
                Log.w(LOG_TAG, "Error while getting AccessibilityServiceInfo", re);
                re.rethrowFromSystemServer();
                return null;
            }
        }
        return null;
    }

    public final void setServiceInfo(AccessibilityServiceInfo info) {
        this.mInfo = info;
        sendServiceInfo();
    }

    private synchronized void sendServiceInfo() {
        AccessibilityInteractionClient.getInstance();
        IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getConnection(this.mConnectionId);
        if (this.mInfo != null && connection != null) {
            try {
                connection.setServiceInfo(this.mInfo);
                this.mInfo = null;
                AccessibilityInteractionClient.getInstance().clearCache();
            } catch (RemoteException re) {
                Log.w(LOG_TAG, "Error while setting AccessibilityServiceInfo", re);
                re.rethrowFromSystemServer();
            }
        }
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Object getSystemService(String name) {
        if (getBaseContext() == null) {
            throw new IllegalStateException("System services not available to Activities before onCreate()");
        }
        if (Context.WINDOW_SERVICE.equals(name)) {
            if (this.mWindowManager == null) {
                this.mWindowManager = (WindowManager) getBaseContext().getSystemService(name);
            }
            return this.mWindowManager;
        }
        return super.getSystemService(name);
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return new IAccessibilityServiceClientWrapper(this, getMainLooper(), new Callbacks() { // from class: android.accessibilityservice.AccessibilityService.2
            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public void onServiceConnected() {
                AccessibilityService.this.dispatchServiceConnected();
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public void onInterrupt() {
                AccessibilityService.this.onInterrupt();
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public void onAccessibilityEvent(AccessibilityEvent event) {
                AccessibilityService.this.onAccessibilityEvent(event);
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public void init(int connectionId, IBinder windowToken) {
                AccessibilityService.this.mConnectionId = connectionId;
                AccessibilityService.this.mWindowToken = windowToken;
                WindowManagerImpl wm = (WindowManagerImpl) AccessibilityService.this.getSystemService(Context.WINDOW_SERVICE);
                wm.setDefaultToken(windowToken);
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public boolean onGesture(int gestureId) {
                return AccessibilityService.this.onGesture(gestureId);
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public boolean onKeyEvent(KeyEvent event) {
                return AccessibilityService.this.onKeyEvent(event);
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public void onMagnificationChanged(Region region, float scale, float centerX, float centerY) {
                AccessibilityService.this.onMagnificationChanged(region, scale, centerX, centerY);
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public void onSoftKeyboardShowModeChanged(int showMode) {
                AccessibilityService.this.onSoftKeyboardShowModeChanged(showMode);
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public void onPerformGestureResult(int sequence, boolean completedSuccessfully) {
                AccessibilityService.this.onPerformGestureResult(sequence, completedSuccessfully);
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public void onFingerprintCapturingGesturesChanged(boolean active) {
                AccessibilityService.this.onFingerprintCapturingGesturesChanged(active);
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public void onFingerprintGesture(int gesture) {
                AccessibilityService.this.onFingerprintGesture(gesture);
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public void onAccessibilityButtonClicked() {
                AccessibilityService.this.onAccessibilityButtonClicked();
            }

            @Override // android.accessibilityservice.AccessibilityService.Callbacks
            public void onAccessibilityButtonAvailabilityChanged(boolean available) {
                AccessibilityService.this.onAccessibilityButtonAvailabilityChanged(available);
            }
        });
    }

    /* loaded from: classes.dex */
    public static class IAccessibilityServiceClientWrapper extends IAccessibilityServiceClient.Stub implements HandlerCaller.Callback {
        private static final int DO_ACCESSIBILITY_BUTTON_AVAILABILITY_CHANGED = 13;
        private static final int DO_ACCESSIBILITY_BUTTON_CLICKED = 12;
        private static final int DO_CLEAR_ACCESSIBILITY_CACHE = 5;
        private static final int DO_GESTURE_COMPLETE = 9;
        private static final int DO_INIT = 1;
        private static final int DO_ON_ACCESSIBILITY_EVENT = 3;
        private static final int DO_ON_FINGERPRINT_ACTIVE_CHANGED = 10;
        private static final int DO_ON_FINGERPRINT_GESTURE = 11;
        private static final int DO_ON_GESTURE = 4;
        private static final int DO_ON_INTERRUPT = 2;
        private static final int DO_ON_KEY_EVENT = 6;
        private static final int DO_ON_MAGNIFICATION_CHANGED = 7;
        private static final int DO_ON_SOFT_KEYBOARD_SHOW_MODE_CHANGED = 8;
        private final Callbacks mCallback;
        private final HandlerCaller mCaller;
        private int mConnectionId = -1;

        public synchronized IAccessibilityServiceClientWrapper(Context context, Looper looper, Callbacks callback) {
            this.mCallback = callback;
            this.mCaller = new HandlerCaller(context, looper, this, true);
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void init(IAccessibilityServiceConnection connection, int connectionId, IBinder windowToken) {
            Message message = this.mCaller.obtainMessageIOO(1, connectionId, connection, windowToken);
            this.mCaller.sendMessage(message);
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void onInterrupt() {
            Message message = this.mCaller.obtainMessage(2);
            this.mCaller.sendMessage(message);
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void onAccessibilityEvent(AccessibilityEvent event, boolean serviceWantsEvent) {
            Message message = this.mCaller.obtainMessageBO(3, serviceWantsEvent, event);
            this.mCaller.sendMessage(message);
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void onGesture(int gestureId) {
            Message message = this.mCaller.obtainMessageI(4, gestureId);
            this.mCaller.sendMessage(message);
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void clearAccessibilityCache() {
            Message message = this.mCaller.obtainMessage(5);
            this.mCaller.sendMessage(message);
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void onKeyEvent(KeyEvent event, int sequence) {
            Message message = this.mCaller.obtainMessageIO(6, sequence, event);
            this.mCaller.sendMessage(message);
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void onMagnificationChanged(Region region, float scale, float centerX, float centerY) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = region;
            args.arg2 = Float.valueOf(scale);
            args.arg3 = Float.valueOf(centerX);
            args.arg4 = Float.valueOf(centerY);
            Message message = this.mCaller.obtainMessageO(7, args);
            this.mCaller.sendMessage(message);
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void onSoftKeyboardShowModeChanged(int showMode) {
            Message message = this.mCaller.obtainMessageI(8, showMode);
            this.mCaller.sendMessage(message);
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void onPerformGestureResult(int sequence, boolean successfully) {
            Message message = this.mCaller.obtainMessageII(9, sequence, successfully ? 1 : 0);
            this.mCaller.sendMessage(message);
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void onFingerprintCapturingGesturesChanged(boolean active) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageI(10, active ? 1 : 0));
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void onFingerprintGesture(int gesture) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageI(11, gesture));
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void onAccessibilityButtonClicked() {
            Message message = this.mCaller.obtainMessage(12);
            this.mCaller.sendMessage(message);
        }

        @Override // android.accessibilityservice.IAccessibilityServiceClient
        public synchronized void onAccessibilityButtonAvailabilityChanged(boolean available) {
            Message message = this.mCaller.obtainMessageI(13, available ? 1 : 0);
            this.mCaller.sendMessage(message);
        }

        @Override // com.android.internal.os.HandlerCaller.Callback
        public synchronized void executeMessage(Message message) {
            boolean serviceWantsEvent;
            switch (message.what) {
                case 1:
                    this.mConnectionId = message.arg1;
                    SomeArgs args = (SomeArgs) message.obj;
                    IAccessibilityServiceConnection connection = (IAccessibilityServiceConnection) args.arg1;
                    IBinder windowToken = (IBinder) args.arg2;
                    args.recycle();
                    if (connection != null) {
                        AccessibilityInteractionClient.getInstance();
                        AccessibilityInteractionClient.addConnection(this.mConnectionId, connection);
                        this.mCallback.init(this.mConnectionId, windowToken);
                        this.mCallback.onServiceConnected();
                        return;
                    }
                    AccessibilityInteractionClient.getInstance();
                    AccessibilityInteractionClient.removeConnection(this.mConnectionId);
                    this.mConnectionId = -1;
                    AccessibilityInteractionClient.getInstance().clearCache();
                    this.mCallback.init(-1, null);
                    return;
                case 2:
                    if (this.mConnectionId != -1) {
                        this.mCallback.onInterrupt();
                        return;
                    }
                    return;
                case 3:
                    AccessibilityEvent event = (AccessibilityEvent) message.obj;
                    serviceWantsEvent = message.arg1 != 0;
                    if (event != null) {
                        AccessibilityInteractionClient.getInstance().onAccessibilityEvent(event);
                        if (serviceWantsEvent && this.mConnectionId != -1) {
                            this.mCallback.onAccessibilityEvent(event);
                        }
                        try {
                            event.recycle();
                            return;
                        } catch (IllegalStateException e) {
                            return;
                        }
                    }
                    return;
                case 4:
                    if (this.mConnectionId != -1) {
                        int gestureId = message.arg1;
                        this.mCallback.onGesture(gestureId);
                        return;
                    }
                    return;
                case 5:
                    AccessibilityInteractionClient.getInstance().clearCache();
                    return;
                case 6:
                    KeyEvent event2 = (KeyEvent) message.obj;
                    try {
                        AccessibilityInteractionClient.getInstance();
                        IAccessibilityServiceConnection connection2 = AccessibilityInteractionClient.getConnection(this.mConnectionId);
                        if (connection2 != null) {
                            boolean result = this.mCallback.onKeyEvent(event2);
                            int sequence = message.arg1;
                            try {
                                connection2.setOnKeyEventResult(result, sequence);
                            } catch (RemoteException e2) {
                            }
                        }
                        try {
                            event2.recycle();
                            return;
                        } catch (IllegalStateException e3) {
                            return;
                        }
                    } catch (Throwable th) {
                        try {
                            event2.recycle();
                        } catch (IllegalStateException e4) {
                        }
                        throw th;
                    }
                case 7:
                    if (this.mConnectionId != -1) {
                        SomeArgs args2 = (SomeArgs) message.obj;
                        Region region = (Region) args2.arg1;
                        float scale = ((Float) args2.arg2).floatValue();
                        float centerX = ((Float) args2.arg3).floatValue();
                        float centerY = ((Float) args2.arg4).floatValue();
                        this.mCallback.onMagnificationChanged(region, scale, centerX, centerY);
                        return;
                    }
                    return;
                case 8:
                    if (this.mConnectionId != -1) {
                        int showMode = message.arg1;
                        this.mCallback.onSoftKeyboardShowModeChanged(showMode);
                        return;
                    }
                    return;
                case 9:
                    if (this.mConnectionId != -1) {
                        serviceWantsEvent = message.arg2 == 1;
                        boolean successfully = serviceWantsEvent;
                        this.mCallback.onPerformGestureResult(message.arg1, successfully);
                        return;
                    }
                    return;
                case 10:
                    if (this.mConnectionId != -1) {
                        Callbacks callbacks = this.mCallback;
                        serviceWantsEvent = message.arg1 == 1;
                        callbacks.onFingerprintCapturingGesturesChanged(serviceWantsEvent);
                        return;
                    }
                    return;
                case 11:
                    if (this.mConnectionId != -1) {
                        this.mCallback.onFingerprintGesture(message.arg1);
                        return;
                    }
                    return;
                case 12:
                    if (this.mConnectionId != -1) {
                        this.mCallback.onAccessibilityButtonClicked();
                        return;
                    }
                    return;
                case 13:
                    if (this.mConnectionId != -1) {
                        serviceWantsEvent = message.arg1 != 0;
                        boolean available = serviceWantsEvent;
                        this.mCallback.onAccessibilityButtonAvailabilityChanged(available);
                        return;
                    }
                    return;
                default:
                    Log.w(AccessibilityService.LOG_TAG, "Unknown message type " + message.what);
                    return;
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class GestureResultCallback {
        public void onCompleted(GestureDescription gestureDescription) {
        }

        public void onCancelled(GestureDescription gestureDescription) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class GestureResultCallbackInfo {
        GestureResultCallback callback;
        GestureDescription gestureDescription;
        Handler handler;

        synchronized GestureResultCallbackInfo(GestureDescription gestureDescription, GestureResultCallback callback, Handler handler) {
            this.gestureDescription = gestureDescription;
            this.callback = callback;
            this.handler = handler;
        }
    }
}
