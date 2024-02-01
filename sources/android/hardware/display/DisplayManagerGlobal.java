package android.hardware.display;

import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.IDisplayManager;
import android.hardware.display.IDisplayManagerCallback;
import android.hardware.display.IVirtualDisplayCallback;
import android.hardware.display.VirtualDisplay;
import android.media.projection.IMediaProjection;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayAdjustments;
import android.view.DisplayInfo;
import android.view.Surface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public final class DisplayManagerGlobal {
    private static final boolean DEBUG = false;
    public static final int EVENT_DISPLAY_ADDED = 1;
    public static final int EVENT_DISPLAY_CHANGED = 2;
    public static final int EVENT_DISPLAY_REMOVED = 3;
    private static final String TAG = "DisplayManager";
    private static final boolean USE_CACHE = false;
    public protected static DisplayManagerGlobal sInstance;
    private DisplayManagerCallback mCallback;
    private int[] mDisplayIdCache;
    public protected final IDisplayManager mDm;
    private int mWifiDisplayScanNestCount;
    private final Object mLock = new Object();
    private final ArrayList<DisplayListenerDelegate> mDisplayListeners = new ArrayList<>();
    private final SparseArray<DisplayInfo> mDisplayInfoCache = new SparseArray<>();

    private synchronized DisplayManagerGlobal(IDisplayManager dm) {
        this.mDm = dm;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static DisplayManagerGlobal getInstance() {
        DisplayManagerGlobal displayManagerGlobal;
        IBinder b;
        synchronized (DisplayManagerGlobal.class) {
            if (sInstance == null && (b = ServiceManager.getService(Context.DISPLAY_SERVICE)) != null) {
                sInstance = new DisplayManagerGlobal(IDisplayManager.Stub.asInterface(b));
            }
            displayManagerGlobal = sInstance;
        }
        return displayManagerGlobal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DisplayInfo getDisplayInfo(int displayId) {
        try {
            synchronized (this.mLock) {
                DisplayInfo info = this.mDm.getDisplayInfo(displayId);
                if (info == null) {
                    return null;
                }
                registerCallbackIfNeededLocked();
                return info;
            }
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getDisplayIds() {
        int[] displayIds;
        try {
            synchronized (this.mLock) {
                displayIds = this.mDm.getDisplayIds();
                registerCallbackIfNeededLocked();
            }
            return displayIds;
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized Display getCompatibleDisplay(int displayId, DisplayAdjustments daj) {
        DisplayInfo displayInfo = getDisplayInfo(displayId);
        if (displayInfo == null) {
            return null;
        }
        return new Display(this, displayId, displayInfo, daj);
    }

    public synchronized Display getCompatibleDisplay(int displayId, Resources resources) {
        DisplayInfo displayInfo = getDisplayInfo(displayId);
        if (displayInfo == null) {
            return null;
        }
        return new Display(this, displayId, displayInfo, resources);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Display getRealDisplay(int displayId) {
        return getCompatibleDisplay(displayId, DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS);
    }

    public synchronized void registerDisplayListener(DisplayManager.DisplayListener listener, Handler handler) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        synchronized (this.mLock) {
            int index = findDisplayListenerLocked(listener);
            if (index < 0) {
                this.mDisplayListeners.add(new DisplayListenerDelegate(listener, handler));
                registerCallbackIfNeededLocked();
            }
        }
    }

    public synchronized void unregisterDisplayListener(DisplayManager.DisplayListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        synchronized (this.mLock) {
            int index = findDisplayListenerLocked(listener);
            if (index >= 0) {
                DisplayListenerDelegate d = this.mDisplayListeners.get(index);
                d.clearEvents();
                this.mDisplayListeners.remove(index);
            }
        }
    }

    private synchronized int findDisplayListenerLocked(DisplayManager.DisplayListener listener) {
        int numListeners = this.mDisplayListeners.size();
        for (int i = 0; i < numListeners; i++) {
            if (this.mDisplayListeners.get(i).mListener == listener) {
                return i;
            }
        }
        return -1;
    }

    private synchronized void registerCallbackIfNeededLocked() {
        if (this.mCallback == null) {
            this.mCallback = new DisplayManagerCallback();
            try {
                this.mDm.registerCallback(this.mCallback);
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleDisplayEvent(int displayId, int event) {
        synchronized (this.mLock) {
            int numListeners = this.mDisplayListeners.size();
            for (int i = 0; i < numListeners; i++) {
                this.mDisplayListeners.get(i).sendDisplayEvent(displayId, event);
            }
        }
    }

    public synchronized void startWifiDisplayScan() {
        synchronized (this.mLock) {
            int i = this.mWifiDisplayScanNestCount;
            this.mWifiDisplayScanNestCount = i + 1;
            if (i == 0) {
                registerCallbackIfNeededLocked();
                try {
                    this.mDm.startWifiDisplayScan();
                } catch (RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            }
        }
    }

    public synchronized void stopWifiDisplayScan() {
        synchronized (this.mLock) {
            int i = this.mWifiDisplayScanNestCount - 1;
            this.mWifiDisplayScanNestCount = i;
            if (i == 0) {
                try {
                    this.mDm.stopWifiDisplayScan();
                } catch (RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            } else if (this.mWifiDisplayScanNestCount < 0) {
                Log.wtf(TAG, "Wifi display scan nest count became negative: " + this.mWifiDisplayScanNestCount);
                this.mWifiDisplayScanNestCount = 0;
            }
        }
    }

    public synchronized void connectWifiDisplay(String deviceAddress) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            this.mDm.connectWifiDisplay(deviceAddress);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void pauseWifiDisplay() {
        try {
            this.mDm.pauseWifiDisplay();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void resumeWifiDisplay() {
        try {
            this.mDm.resumeWifiDisplay();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnectWifiDisplay() {
        try {
            this.mDm.disconnectWifiDisplay();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void renameWifiDisplay(String deviceAddress, String alias) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            this.mDm.renameWifiDisplay(deviceAddress, alias);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void forgetWifiDisplay(String deviceAddress) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            this.mDm.forgetWifiDisplay(deviceAddress);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WifiDisplayStatus getWifiDisplayStatus() {
        try {
            return this.mDm.getWifiDisplayStatus();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void requestColorMode(int displayId, int colorMode) {
        try {
            this.mDm.requestColorMode(displayId, colorMode);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void setSaturationLevel(float level) {
        try {
            this.mDm.setSaturationLevel(level);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized VirtualDisplay createVirtualDisplay(Context context, MediaProjection projection, String name, int width, int height, int densityDpi, Surface surface, int flags, VirtualDisplay.Callback callback, Handler handler, String uniqueId) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name must be non-null and non-empty");
        }
        if (width <= 0 || height <= 0 || densityDpi <= 0) {
            throw new IllegalArgumentException("width, height, and densityDpi must be greater than 0");
        }
        VirtualDisplayCallback callbackWrapper = new VirtualDisplayCallback(callback, handler);
        IMediaProjection projectionToken = projection != null ? projection.getProjection() : null;
        try {
        } catch (RemoteException e) {
            ex = e;
        }
        try {
            int displayId = this.mDm.createVirtualDisplay(callbackWrapper, projectionToken, context.getPackageName(), name, width, height, densityDpi, surface, flags, uniqueId);
            if (displayId < 0) {
                Log.e(TAG, "Could not create virtual display: " + name);
                return null;
            }
            Display display = getRealDisplay(displayId);
            if (display == null) {
                Log.wtf(TAG, "Could not obtain display info for newly created virtual display: " + name);
                try {
                    this.mDm.releaseVirtualDisplay(callbackWrapper);
                    return null;
                } catch (RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            }
            return new VirtualDisplay(this, display, callbackWrapper, surface);
        } catch (RemoteException e2) {
            ex = e2;
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void setVirtualDisplaySurface(IVirtualDisplayCallback token, Surface surface) {
        try {
            this.mDm.setVirtualDisplaySurface(token, surface);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void resizeVirtualDisplay(IVirtualDisplayCallback token, int width, int height, int densityDpi) {
        try {
            this.mDm.resizeVirtualDisplay(token, width, height, densityDpi);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void releaseVirtualDisplay(IVirtualDisplayCallback token) {
        try {
            this.mDm.releaseVirtualDisplay(token);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized Point getStableDisplaySize() {
        try {
            return this.mDm.getStableDisplaySize();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized List<BrightnessChangeEvent> getBrightnessEvents(String callingPackage) {
        try {
            ParceledListSlice<BrightnessChangeEvent> events = this.mDm.getBrightnessEvents(callingPackage);
            if (events == null) {
                return Collections.emptyList();
            }
            return events.getList();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void setBrightnessConfigurationForUser(BrightnessConfiguration c, int userId, String packageName) {
        try {
            this.mDm.setBrightnessConfigurationForUser(c, userId, packageName);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized BrightnessConfiguration getBrightnessConfigurationForUser(int userId) {
        try {
            return this.mDm.getBrightnessConfigurationForUser(userId);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized BrightnessConfiguration getDefaultBrightnessConfiguration() {
        try {
            return this.mDm.getDefaultBrightnessConfiguration();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void setTemporaryBrightness(int brightness) {
        try {
            this.mDm.setTemporaryBrightness(brightness);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void setTemporaryAutoBrightnessAdjustment(float adjustment) {
        try {
            this.mDm.setTemporaryAutoBrightnessAdjustment(adjustment);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized Pair<float[], float[]> getMinimumBrightnessCurve() {
        try {
            Curve curve = this.mDm.getMinimumBrightnessCurve();
            return Pair.create(curve.getX(), curve.getY());
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized List<AmbientBrightnessDayStats> getAmbientBrightnessStats() {
        try {
            ParceledListSlice<AmbientBrightnessDayStats> stats = this.mDm.getAmbientBrightnessStats();
            if (stats == null) {
                return Collections.emptyList();
            }
            return stats.getList();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class DisplayManagerCallback extends IDisplayManagerCallback.Stub {
        private DisplayManagerCallback() {
        }

        @Override // android.hardware.display.IDisplayManagerCallback
        public synchronized void onDisplayEvent(int displayId, int event) {
            DisplayManagerGlobal.this.handleDisplayEvent(displayId, event);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class DisplayListenerDelegate extends Handler {
        public final DisplayManager.DisplayListener mListener;

        public synchronized DisplayListenerDelegate(DisplayManager.DisplayListener listener, Handler handler) {
            super(handler != null ? handler.getLooper() : Looper.myLooper(), null, true);
            this.mListener = listener;
        }

        public synchronized void sendDisplayEvent(int displayId, int event) {
            Message msg = obtainMessage(event, displayId, 0);
            sendMessage(msg);
        }

        public synchronized void clearEvents() {
            removeCallbacksAndMessages(null);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    this.mListener.onDisplayAdded(msg.arg1);
                    return;
                case 2:
                    this.mListener.onDisplayChanged(msg.arg1);
                    return;
                case 3:
                    this.mListener.onDisplayRemoved(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    /* loaded from: classes.dex */
    private static final class VirtualDisplayCallback extends IVirtualDisplayCallback.Stub {
        private VirtualDisplayCallbackDelegate mDelegate;

        public synchronized VirtualDisplayCallback(VirtualDisplay.Callback callback, Handler handler) {
            if (callback != null) {
                this.mDelegate = new VirtualDisplayCallbackDelegate(callback, handler);
            }
        }

        @Override // android.hardware.display.IVirtualDisplayCallback
        public synchronized void onPaused() {
            if (this.mDelegate != null) {
                this.mDelegate.sendEmptyMessage(0);
            }
        }

        @Override // android.hardware.display.IVirtualDisplayCallback
        public synchronized void onResumed() {
            if (this.mDelegate != null) {
                this.mDelegate.sendEmptyMessage(1);
            }
        }

        @Override // android.hardware.display.IVirtualDisplayCallback
        public synchronized void onStopped() {
            if (this.mDelegate != null) {
                this.mDelegate.sendEmptyMessage(2);
            }
        }
    }

    /* loaded from: classes.dex */
    private static final class VirtualDisplayCallbackDelegate extends Handler {
        public static final int MSG_DISPLAY_PAUSED = 0;
        public static final int MSG_DISPLAY_RESUMED = 1;
        public static final int MSG_DISPLAY_STOPPED = 2;
        private final VirtualDisplay.Callback mCallback;

        public synchronized VirtualDisplayCallbackDelegate(VirtualDisplay.Callback callback, Handler handler) {
            super(handler != null ? handler.getLooper() : Looper.myLooper(), null, true);
            this.mCallback = callback;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    this.mCallback.onPaused();
                    return;
                case 1:
                    this.mCallback.onResumed();
                    return;
                case 2:
                    this.mCallback.onStopped();
                    return;
                default:
                    return;
            }
        }
    }
}
