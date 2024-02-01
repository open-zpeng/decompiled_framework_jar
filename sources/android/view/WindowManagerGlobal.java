package android.view;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.ArraySet;
import android.util.Log;
import android.view.IWindowManager;
import android.view.IWindowSessionCallback;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.util.FastPrintWriter;
import com.xiaopeng.view.xpWindowManager;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public final class WindowManagerGlobal {
    public static final int ADD_APP_EXITING = -4;
    public static final int ADD_BAD_APP_TOKEN = -1;
    public static final int ADD_BAD_SUBWINDOW_TOKEN = -2;
    public static final int ADD_DUPLICATE_ADD = -5;
    public static final int ADD_FLAG_ALWAYS_CONSUME_NAV_BAR = 4;
    public static final int ADD_FLAG_APP_VISIBLE = 2;
    public static final int ADD_FLAG_IN_TOUCH_MODE = 1;
    public static final int ADD_INVALID_DISPLAY = -9;
    public static final int ADD_INVALID_TYPE = -10;
    public static final int ADD_MULTIPLE_SINGLETON = -7;
    public static final int ADD_NOT_APP_TOKEN = -3;
    public static final int ADD_OKAY = 0;
    public static final int ADD_PERMISSION_DENIED = -8;
    public static final int ADD_STARTING_NOT_NEEDED = -6;
    public static final int RELAYOUT_DEFER_SURFACE_DESTROY = 2;
    public static final int RELAYOUT_INSETS_PENDING = 1;
    public static final int RELAYOUT_RES_CONSUME_ALWAYS_NAV_BAR = 64;
    public static final int RELAYOUT_RES_DRAG_RESIZING_DOCKED = 8;
    public static final int RELAYOUT_RES_DRAG_RESIZING_FREEFORM = 16;
    public static final int RELAYOUT_RES_FIRST_TIME = 2;
    public static final int RELAYOUT_RES_IN_TOUCH_MODE = 1;
    public static final int RELAYOUT_RES_SURFACE_CHANGED = 4;
    public static final int RELAYOUT_RES_SURFACE_RESIZED = 32;
    private static final String TAG = "WindowManager";
    public protected static WindowManagerGlobal sDefaultWindowManager;
    public protected static IWindowManager sWindowManagerService;
    public protected static IWindowSession sWindowSession;
    private Runnable mSystemPropertyUpdater;
    public protected final Object mLock = new Object();
    public protected final ArrayList<View> mViews = new ArrayList<>();
    public protected final ArrayList<ViewRootImpl> mRoots = new ArrayList<>();
    public protected final ArrayList<WindowManager.LayoutParams> mParams = new ArrayList<>();
    private final ArraySet<View> mDyingViews = new ArraySet<>();

    private synchronized WindowManagerGlobal() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void initialize() {
        getWindowManagerService();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static WindowManagerGlobal getInstance() {
        WindowManagerGlobal windowManagerGlobal;
        synchronized (WindowManagerGlobal.class) {
            if (sDefaultWindowManager == null) {
                sDefaultWindowManager = new WindowManagerGlobal();
            }
            windowManagerGlobal = sDefaultWindowManager;
        }
        return windowManagerGlobal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IWindowManager getWindowManagerService() {
        IWindowManager iWindowManager;
        synchronized (WindowManagerGlobal.class) {
            if (sWindowManagerService == null) {
                sWindowManagerService = IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
                try {
                    if (sWindowManagerService != null) {
                        ValueAnimator.setDurationScale(sWindowManagerService.getCurrentAnimatorScale());
                    }
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            iWindowManager = sWindowManagerService;
        }
        return iWindowManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IWindowSession getWindowSession() {
        IWindowSession iWindowSession;
        synchronized (WindowManagerGlobal.class) {
            if (sWindowSession == null) {
                try {
                    InputMethodManager imm = InputMethodManager.getInstance();
                    IWindowManager windowManager = getWindowManagerService();
                    sWindowSession = windowManager.openSession(new IWindowSessionCallback.Stub() { // from class: android.view.WindowManagerGlobal.1
                        @Override // android.view.IWindowSessionCallback
                        public void onAnimatorScaleChanged(float scale) {
                            ValueAnimator.setDurationScale(scale);
                        }
                    }, imm.getClient(), imm.getInputContext());
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            iWindowSession = sWindowSession;
        }
        return iWindowSession;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IWindowSession peekWindowSession() {
        IWindowSession iWindowSession;
        synchronized (WindowManagerGlobal.class) {
            iWindowSession = sWindowSession;
        }
        return iWindowSession;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] getViewRootNames() {
        String[] mViewRoots;
        synchronized (this.mLock) {
            int numRoots = this.mRoots.size();
            mViewRoots = new String[numRoots];
            for (int i = 0; i < numRoots; i++) {
                mViewRoots[i] = getWindowName(this.mRoots.get(i));
            }
        }
        return mViewRoots;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<ViewRootImpl> getRootViews(IBinder token) {
        ArrayList<ViewRootImpl> views = new ArrayList<>();
        synchronized (this.mLock) {
            int numRoots = this.mRoots.size();
            for (int i = 0; i < numRoots; i++) {
                WindowManager.LayoutParams params = this.mParams.get(i);
                if (params.token != null) {
                    if (params.token != token) {
                        boolean isChild = false;
                        if (params.type >= 1000 && params.type <= 1999) {
                            int j = 0;
                            while (true) {
                                if (j >= numRoots) {
                                    break;
                                }
                                View viewj = this.mViews.get(j);
                                WindowManager.LayoutParams paramsj = this.mParams.get(j);
                                if (params.token != viewj.getWindowToken() || paramsj.token != token) {
                                    j++;
                                } else {
                                    isChild = true;
                                    break;
                                }
                            }
                        }
                        if (!isChild) {
                        }
                    }
                    views.add(this.mRoots.get(i));
                }
            }
        }
        return views;
    }

    public synchronized View getWindowView(IBinder windowToken) {
        synchronized (this.mLock) {
            int numViews = this.mViews.size();
            for (int i = 0; i < numViews; i++) {
                View view = this.mViews.get(i);
                if (view.getWindowToken() == windowToken) {
                    return view;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View getRootView(String name) {
        synchronized (this.mLock) {
            for (int i = this.mRoots.size() - 1; i >= 0; i--) {
                ViewRootImpl root = this.mRoots.get(i);
                if (name.equals(getWindowName(root))) {
                    return root.getView();
                }
            }
            return null;
        }
    }

    public synchronized void addView(View view, ViewGroup.LayoutParams params, Display display, Window parentWindow) {
        if (view == null) {
            throw new IllegalArgumentException("view must not be null");
        }
        if (display == null) {
            throw new IllegalArgumentException("display must not be null");
        }
        if (!(params instanceof WindowManager.LayoutParams)) {
            throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
        }
        WindowManager.LayoutParams wparams = xpWindowManager.getOverrideLayoutParams((WindowManager.LayoutParams) params, view, sWindowManagerService);
        if (parentWindow != null) {
            parentWindow.adjustLayoutParamsForSubWindow(wparams);
        } else {
            Context context = view.getContext();
            if (context != null && (context.getApplicationInfo().flags & 536870912) != 0) {
                wparams.flags |= 16777216;
            }
        }
        View panelParentView = null;
        synchronized (this.mLock) {
            if (this.mSystemPropertyUpdater == null) {
                this.mSystemPropertyUpdater = new Runnable() { // from class: android.view.WindowManagerGlobal.2
                    @Override // java.lang.Runnable
                    public void run() {
                        synchronized (WindowManagerGlobal.this.mLock) {
                            for (int i = WindowManagerGlobal.this.mRoots.size() - 1; i >= 0; i--) {
                                ((ViewRootImpl) WindowManagerGlobal.this.mRoots.get(i)).loadSystemProperties();
                            }
                        }
                    }
                };
                SystemProperties.addChangeCallback(this.mSystemPropertyUpdater);
            }
            int index = findViewLocked(view, false);
            if (index >= 0) {
                if (this.mDyingViews.contains(view)) {
                    this.mRoots.get(index).doDie();
                } else {
                    throw new IllegalStateException("View " + view + " has already been added to the window manager.");
                }
            }
            if (wparams.type >= 1000 && wparams.type <= 1999) {
                int count = this.mViews.size();
                for (int i = 0; i < count; i++) {
                    if (this.mRoots.get(i).mWindow.asBinder() == wparams.token) {
                        panelParentView = this.mViews.get(i);
                    }
                }
            }
            ViewRootImpl root = new ViewRootImpl(view.getContext(), display);
            view.setLayoutParams(wparams);
            this.mViews.add(view);
            this.mRoots.add(root);
            this.mParams.add(wparams);
            try {
                root.setView(view, wparams, panelParentView);
            } catch (RuntimeException e) {
                if (index >= 0) {
                    removeViewLocked(index, true);
                }
                throw e;
            }
        }
    }

    public synchronized void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        if (view == null) {
            throw new IllegalArgumentException("view must not be null");
        }
        if (!(params instanceof WindowManager.LayoutParams)) {
            throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
        }
        WindowManager.LayoutParams wparams = (WindowManager.LayoutParams) params;
        view.setLayoutParams(wparams);
        synchronized (this.mLock) {
            int index = findViewLocked(view, true);
            ViewRootImpl root = this.mRoots.get(index);
            this.mParams.remove(index);
            this.mParams.add(index, wparams);
            root.setLayoutParams(wparams, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeView(View view, boolean immediate) {
        if (view == null) {
            throw new IllegalArgumentException("view must not be null");
        }
        synchronized (this.mLock) {
            int index = findViewLocked(view, true);
            View curView = this.mRoots.get(index).getView();
            removeViewLocked(index, immediate);
            if (curView != view) {
                throw new IllegalStateException("Calling with view " + view + " but the ViewAncestor is attached to " + curView);
            }
        }
    }

    public synchronized void closeAll(IBinder token, String who, String what) {
        closeAllExceptView(token, null, who, what);
    }

    public synchronized void closeAllExceptView(IBinder token, View view, String who, String what) {
        synchronized (this.mLock) {
            int count = this.mViews.size();
            for (int i = 0; i < count; i++) {
                if ((view == null || this.mViews.get(i) != view) && (token == null || this.mParams.get(i).token == token)) {
                    ViewRootImpl root = this.mRoots.get(i);
                    if (who != null) {
                        WindowLeaked leak = new WindowLeaked(what + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + who + " has leaked window " + root.getView() + " that was originally added here");
                        leak.setStackTrace(root.getLocation().getStackTrace());
                        Log.e(TAG, "", leak);
                    }
                    removeViewLocked(i, false);
                }
            }
        }
    }

    private synchronized void removeViewLocked(int index, boolean immediate) {
        InputMethodManager imm;
        ViewRootImpl root = this.mRoots.get(index);
        View view = root.getView();
        if (view != null && (imm = InputMethodManager.getInstance()) != null) {
            imm.windowDismissed(this.mViews.get(index).getWindowToken());
        }
        boolean deferred = root.die(immediate);
        if (view != null) {
            view.assignParent(null);
            if (deferred) {
                this.mDyingViews.add(view);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void doRemoveView(ViewRootImpl root) {
        synchronized (this.mLock) {
            int index = this.mRoots.indexOf(root);
            if (index >= 0) {
                this.mRoots.remove(index);
                this.mParams.remove(index);
                View view = this.mViews.remove(index);
                this.mDyingViews.remove(view);
            }
        }
        if (ThreadedRenderer.sTrimForeground && ThreadedRenderer.isAvailable()) {
            doTrimForeground();
        }
    }

    private synchronized int findViewLocked(View view, boolean required) {
        int index = this.mViews.indexOf(view);
        if (required && index < 0) {
            throw new IllegalArgumentException("View=" + view + " not attached to window manager");
        }
        return index;
    }

    public static synchronized boolean shouldDestroyEglContext(int trimLevel) {
        if (trimLevel >= 80) {
            return true;
        }
        if (trimLevel >= 60 && !ActivityManager.isHighEndGfx()) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trimMemory(int level) {
        if (ThreadedRenderer.isAvailable()) {
            if (shouldDestroyEglContext(level)) {
                synchronized (this.mLock) {
                    for (int i = this.mRoots.size() - 1; i >= 0; i--) {
                        this.mRoots.get(i).destroyHardwareResources();
                    }
                }
                level = 80;
            }
            ThreadedRenderer.trimMemory(level);
            if (ThreadedRenderer.sTrimForeground) {
                doTrimForeground();
            }
        }
    }

    public static synchronized void trimForeground() {
        if (ThreadedRenderer.sTrimForeground && ThreadedRenderer.isAvailable()) {
            WindowManagerGlobal wm = getInstance();
            wm.doTrimForeground();
        }
    }

    private synchronized void doTrimForeground() {
        boolean hasVisibleWindows = false;
        synchronized (this.mLock) {
            for (int i = this.mRoots.size() - 1; i >= 0; i--) {
                ViewRootImpl root = this.mRoots.get(i);
                if (root.mView != null && root.getHostVisibility() == 0 && root.mAttachInfo.mThreadedRenderer != null) {
                    hasVisibleWindows = true;
                } else {
                    root.destroyHardwareResources();
                }
            }
        }
        if (!hasVisibleWindows) {
            ThreadedRenderer.trimMemory(80);
        }
    }

    public synchronized void dumpGfxInfo(FileDescriptor fd, String[] args) {
        FileOutputStream fout = new FileOutputStream(fd);
        PrintWriter pw = new FastPrintWriter(fout);
        try {
            synchronized (this.mLock) {
                try {
                    int count = this.mViews.size();
                    pw.println("Profile data in ms:");
                    for (int i = 0; i < count; i++) {
                        ViewRootImpl root = this.mRoots.get(i);
                        String name = getWindowName(root);
                        pw.printf("\n\t%s (visibility=%d)", name, Integer.valueOf(root.getHostVisibility()));
                        ThreadedRenderer renderer = root.getView().mAttachInfo.mThreadedRenderer;
                        if (renderer != null) {
                            try {
                                renderer.dumpGfxInfo(pw, fd, args);
                            } catch (Throwable th) {
                                th = th;
                                try {
                                    throw th;
                                } catch (Throwable th2) {
                                    th = th2;
                                    pw.flush();
                                    throw th;
                                }
                            }
                        }
                    }
                    pw.println("\nView hierarchy:\n");
                    int[] info = new int[2];
                    int displayListsSize = 0;
                    int displayListsSize2 = 0;
                    for (int viewsCount = 0; viewsCount < count; viewsCount++) {
                        ViewRootImpl root2 = this.mRoots.get(viewsCount);
                        root2.dumpGfxInfo(info);
                        String name2 = getWindowName(root2);
                        pw.printf("  %s\n  %d views, %.2f kB of display lists", name2, Integer.valueOf(info[0]), Float.valueOf(info[1] / 1024.0f));
                        pw.printf("\n\n", new Object[0]);
                        displayListsSize2 += info[0];
                        displayListsSize += info[1];
                    }
                    pw.printf("\nTotal ViewRootImpl: %d\n", Integer.valueOf(count));
                    pw.printf("Total Views:        %d\n", Integer.valueOf(displayListsSize2));
                    pw.printf("Total DisplayList:  %.2f kB\n\n", Float.valueOf(displayListsSize / 1024.0f));
                    pw.flush();
                } catch (Throwable th3) {
                    th = th3;
                }
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }

    private static synchronized String getWindowName(ViewRootImpl root) {
        return ((Object) root.mWindowAttributes.getTitle()) + "/" + root.getClass().getName() + '@' + Integer.toHexString(root.hashCode());
    }

    public synchronized void setStoppedState(IBinder token, boolean stopped) {
        synchronized (this.mLock) {
            int count = this.mViews.size();
            for (int i = count - 1; i >= 0; i--) {
                if (token == null || this.mParams.get(i).token == token) {
                    ViewRootImpl root = this.mRoots.get(i);
                    root.setWindowStopped(stopped);
                    setStoppedState(root.mAttachInfo.mWindowToken, stopped);
                }
            }
        }
    }

    public synchronized void reportNewConfiguration(Configuration config) {
        synchronized (this.mLock) {
            int count = this.mViews.size();
            Configuration config2 = new Configuration(config);
            for (int i = 0; i < count; i++) {
                ViewRootImpl root = this.mRoots.get(i);
                root.requestUpdateConfiguration(config2);
            }
        }
    }

    public synchronized void changeCanvasOpacity(IBinder token, boolean opaque) {
        if (token == null) {
            return;
        }
        synchronized (this.mLock) {
            for (int i = this.mParams.size() - 1; i >= 0; i--) {
                if (this.mParams.get(i).token == token) {
                    this.mRoots.get(i).changeCanvasOpacity(opaque);
                    return;
                }
            }
        }
    }
}
