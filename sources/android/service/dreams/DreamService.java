package android.service.dreams;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.dreams.IDreamManager;
import android.service.dreams.IDreamService;
import android.util.MathUtils;
import android.util.Slog;
import android.view.ActionMode;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.R;
import com.android.internal.policy.PhoneWindow;
import com.android.internal.util.DumpUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class DreamService extends Service implements Window.Callback {
    public static final String DREAM_META_DATA = "android.service.dream";
    public static final String DREAM_SERVICE = "dreams";
    public static final String SERVICE_INTERFACE = "android.service.dreams.DreamService";
    private boolean mCanDoze;
    private boolean mDozing;
    private boolean mFinished;
    private boolean mFullscreen;
    private boolean mInteractive;
    private boolean mStarted;
    private boolean mWaking;
    private Window mWindow;
    private IBinder mWindowToken;
    private boolean mWindowless;
    private final String TAG = DreamService.class.getSimpleName() + "[" + getClass().getSimpleName() + "]";
    private final Handler mHandler = new Handler();
    private boolean mLowProfile = true;
    private boolean mScreenBright = true;
    private int mDozeScreenState = 0;
    private int mDozeScreenBrightness = -1;
    private boolean mDebug = false;
    private final IDreamManager mSandman = IDreamManager.Stub.asInterface(ServiceManager.getService(DREAM_SERVICE));

    public synchronized void setDebug(boolean dbg) {
        this.mDebug = dbg;
    }

    @Override // android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!this.mInteractive) {
            if (this.mDebug) {
                Slog.v(this.TAG, "Waking up on keyEvent");
            }
            wakeUp();
            return true;
        } else if (event.getKeyCode() == 4) {
            if (this.mDebug) {
                Slog.v(this.TAG, "Waking up on back key");
            }
            wakeUp();
            return true;
        } else {
            return this.mWindow.superDispatchKeyEvent(event);
        }
    }

    @Override // android.view.Window.Callback
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        if (!this.mInteractive) {
            if (this.mDebug) {
                Slog.v(this.TAG, "Waking up on keyShortcutEvent");
            }
            wakeUp();
            return true;
        }
        return this.mWindow.superDispatchKeyShortcutEvent(event);
    }

    @Override // android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!this.mInteractive) {
            if (this.mDebug) {
                Slog.v(this.TAG, "Waking up on touchEvent");
            }
            wakeUp();
            return true;
        }
        return this.mWindow.superDispatchTouchEvent(event);
    }

    @Override // android.view.Window.Callback
    public boolean dispatchTrackballEvent(MotionEvent event) {
        if (!this.mInteractive) {
            if (this.mDebug) {
                Slog.v(this.TAG, "Waking up on trackballEvent");
            }
            wakeUp();
            return true;
        }
        return this.mWindow.superDispatchTrackballEvent(event);
    }

    @Override // android.view.Window.Callback
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (!this.mInteractive) {
            if (this.mDebug) {
                Slog.v(this.TAG, "Waking up on genericMotionEvent");
            }
            wakeUp();
            return true;
        }
        return this.mWindow.superDispatchGenericMotionEvent(event);
    }

    @Override // android.view.Window.Callback
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return false;
    }

    @Override // android.view.Window.Callback
    public View onCreatePanelView(int featureId) {
        return null;
    }

    @Override // android.view.Window.Callback
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return false;
    }

    @Override // android.view.Window.Callback
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return false;
    }

    @Override // android.view.Window.Callback
    public boolean onMenuOpened(int featureId, Menu menu) {
        return false;
    }

    @Override // android.view.Window.Callback
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return false;
    }

    @Override // android.view.Window.Callback
    public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
    }

    @Override // android.view.Window.Callback
    public void onContentChanged() {
    }

    @Override // android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
    }

    @Override // android.view.Window.Callback
    public void onAttachedToWindow() {
    }

    @Override // android.view.Window.Callback
    public void onDetachedFromWindow() {
    }

    @Override // android.view.Window.Callback
    public void onPanelClosed(int featureId, Menu menu) {
    }

    @Override // android.view.Window.Callback
    public boolean onSearchRequested(SearchEvent event) {
        return onSearchRequested();
    }

    @Override // android.view.Window.Callback
    public boolean onSearchRequested() {
        return false;
    }

    @Override // android.view.Window.Callback
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override // android.view.Window.Callback
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        return null;
    }

    @Override // android.view.Window.Callback
    public void onActionModeStarted(ActionMode mode) {
    }

    @Override // android.view.Window.Callback
    public void onActionModeFinished(ActionMode mode) {
    }

    public WindowManager getWindowManager() {
        if (this.mWindow != null) {
            return this.mWindow.getWindowManager();
        }
        return null;
    }

    public Window getWindow() {
        return this.mWindow;
    }

    public void setContentView(int layoutResID) {
        getWindow().setContentView(layoutResID);
    }

    public void setContentView(View view) {
        getWindow().setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getWindow().setContentView(view, params);
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getWindow().addContentView(view, params);
    }

    public <T extends View> T findViewById(int id) {
        return (T) getWindow().findViewById(id);
    }

    public final <T extends View> T requireViewById(int id) {
        T view = (T) findViewById(id);
        if (view == null) {
            throw new IllegalArgumentException("ID does not reference a View inside this DreamService");
        }
        return view;
    }

    public void setInteractive(boolean interactive) {
        this.mInteractive = interactive;
    }

    public boolean isInteractive() {
        return this.mInteractive;
    }

    public synchronized void setLowProfile(boolean lowProfile) {
        if (this.mLowProfile != lowProfile) {
            this.mLowProfile = lowProfile;
            applySystemUiVisibilityFlags(this.mLowProfile ? 1 : 0, 1);
        }
    }

    public synchronized boolean isLowProfile() {
        return getSystemUiVisibilityFlagValue(1, this.mLowProfile);
    }

    public void setFullscreen(boolean fullscreen) {
        if (this.mFullscreen != fullscreen) {
            this.mFullscreen = fullscreen;
            applyWindowFlags(this.mFullscreen ? 1024 : 0, 1024);
        }
    }

    public boolean isFullscreen() {
        return this.mFullscreen;
    }

    public void setScreenBright(boolean screenBright) {
        if (this.mScreenBright != screenBright) {
            this.mScreenBright = screenBright;
            applyWindowFlags(this.mScreenBright ? 128 : 0, 128);
        }
    }

    public boolean isScreenBright() {
        return getWindowFlagValue(128, this.mScreenBright);
    }

    private protected void setWindowless(boolean windowless) {
        this.mWindowless = windowless;
    }

    public synchronized boolean isWindowless() {
        return this.mWindowless;
    }

    private protected boolean canDoze() {
        return this.mCanDoze;
    }

    private protected void startDozing() {
        if (this.mCanDoze && !this.mDozing) {
            this.mDozing = true;
            updateDoze();
        }
    }

    private synchronized void updateDoze() {
        if (this.mWindowToken == null) {
            Slog.w(this.TAG, "Updating doze without a window token.");
        } else if (this.mSandman != null && this.mDozing) {
            try {
                this.mSandman.startDozing(this.mWindowToken, this.mDozeScreenState, this.mDozeScreenBrightness);
            } catch (RemoteException e) {
            }
        }
    }

    private protected void stopDozing() {
        if (this.mDozing) {
            this.mDozing = false;
            if (this.mSandman == null) {
                return;
            }
            try {
                this.mSandman.stopDozing(this.mWindowToken);
            } catch (RemoteException e) {
            }
        }
    }

    private protected boolean isDozing() {
        return this.mDozing;
    }

    public synchronized int getDozeScreenState() {
        return this.mDozeScreenState;
    }

    private protected void setDozeScreenState(int state) {
        if (this.mDozeScreenState != state) {
            this.mDozeScreenState = state;
            updateDoze();
        }
    }

    private protected int getDozeScreenBrightness() {
        return this.mDozeScreenBrightness;
    }

    private protected void setDozeScreenBrightness(int brightness) {
        if (brightness != -1) {
            brightness = clampAbsoluteBrightness(brightness);
        }
        if (this.mDozeScreenBrightness != brightness) {
            this.mDozeScreenBrightness = brightness;
            updateDoze();
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        if (this.mDebug) {
            Slog.v(this.TAG, "onCreate()");
        }
        super.onCreate();
    }

    public void onDreamingStarted() {
        if (this.mDebug) {
            Slog.v(this.TAG, "onDreamingStarted()");
        }
    }

    public void onDreamingStopped() {
        if (this.mDebug) {
            Slog.v(this.TAG, "onDreamingStopped()");
        }
    }

    public void onWakeUp() {
        finish();
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        if (this.mDebug) {
            String str = this.TAG;
            Slog.v(str, "onBind() intent = " + intent);
        }
        return new DreamServiceWrapper();
    }

    public final void finish() {
        if (this.mDebug) {
            String str = this.TAG;
            Slog.v(str, "finish(): mFinished=" + this.mFinished);
        }
        if (!this.mFinished) {
            this.mFinished = true;
            if (this.mWindowToken == null) {
                Slog.w(this.TAG, "Finish was called before the dream was attached.");
            } else {
                try {
                    if (this.mSandman != null) {
                        this.mSandman.finishSelf(this.mWindowToken, true);
                    }
                } catch (RemoteException e) {
                }
            }
            stopSelf();
        }
    }

    public final void wakeUp() {
        wakeUp(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void wakeUp(boolean fromSystem) {
        if (this.mDebug) {
            String str = this.TAG;
            Slog.v(str, "wakeUp(): fromSystem=" + fromSystem + ", mWaking=" + this.mWaking + ", mFinished=" + this.mFinished);
        }
        if (!this.mWaking && !this.mFinished) {
            this.mWaking = true;
            onWakeUp();
            if (!fromSystem && !this.mFinished) {
                if (this.mWindowToken == null) {
                    Slog.w(this.TAG, "WakeUp was called before the dream was attached.");
                } else if (this.mSandman == null) {
                } else {
                    try {
                        this.mSandman.finishSelf(this.mWindowToken, false);
                    } catch (RemoteException e) {
                    }
                }
            }
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        if (this.mDebug) {
            Slog.v(this.TAG, "onDestroy()");
        }
        detach();
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final synchronized void detach() {
        if (this.mStarted) {
            if (this.mDebug) {
                Slog.v(this.TAG, "detach(): Calling onDreamingStopped()");
            }
            this.mStarted = false;
            onDreamingStopped();
        }
        if (this.mWindow != null) {
            if (this.mDebug) {
                Slog.v(this.TAG, "detach(): Removing window from window manager");
            }
            this.mWindow.getWindowManager().removeViewImmediate(this.mWindow.getDecorView());
            this.mWindow = null;
        }
        if (this.mWindowToken != null) {
            WindowManagerGlobal.getInstance().closeAll(this.mWindowToken, getClass().getName(), "Dream");
            this.mWindowToken = null;
            this.mCanDoze = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final synchronized void attach(IBinder windowToken, boolean canDoze, final IRemoteCallback started) {
        if (this.mWindowToken != null) {
            Slog.e(this.TAG, "attach() called when already attached with token=" + this.mWindowToken);
        } else if (this.mFinished || this.mWaking) {
            Slog.w(this.TAG, "attach() called after dream already finished");
            if (this.mSandman == null) {
                return;
            }
            try {
                this.mSandman.finishSelf(windowToken, true);
            } catch (RemoteException e) {
            }
        } else {
            this.mWindowToken = windowToken;
            this.mCanDoze = canDoze;
            if (this.mWindowless && !this.mCanDoze) {
                throw new IllegalStateException("Only doze dreams can be windowless");
            }
            if (!this.mWindowless) {
                this.mWindow = new PhoneWindow(this);
                this.mWindow.setCallback(this);
                this.mWindow.requestFeature(1);
                this.mWindow.setBackgroundDrawable(new ColorDrawable(-16777216));
                this.mWindow.setFormat(-1);
                if (this.mDebug) {
                    Slog.v(this.TAG, String.format("Attaching window token: %s to window of type %s", windowToken, Integer.valueOf((int) WindowManager.LayoutParams.TYPE_DREAM)));
                }
                WindowManager.LayoutParams lp = this.mWindow.getAttributes();
                lp.type = WindowManager.LayoutParams.TYPE_DREAM;
                lp.token = windowToken;
                lp.windowAnimations = R.style.Animation_Dream;
                lp.flags |= (this.mScreenBright ? 128 : 0) | 4784385 | (this.mFullscreen ? 1024 : 0);
                this.mWindow.setAttributes(lp);
                this.mWindow.clearFlags(Integer.MIN_VALUE);
                this.mWindow.setWindowManager(null, windowToken, "dream", true);
                applySystemUiVisibilityFlags(this.mLowProfile ? 1 : 0, 1);
                try {
                    getWindowManager().addView(this.mWindow.getDecorView(), this.mWindow.getAttributes());
                } catch (WindowManager.BadTokenException e2) {
                    Slog.i(this.TAG, "attach() called after window token already removed, dream will finish soon");
                    this.mWindow = null;
                    return;
                }
            }
            this.mHandler.post(new Runnable() { // from class: android.service.dreams.DreamService.1
                @Override // java.lang.Runnable
                public void run() {
                    if (DreamService.this.mWindow != null || DreamService.this.mWindowless) {
                        if (DreamService.this.mDebug) {
                            Slog.v(DreamService.this.TAG, "Calling onDreamingStarted()");
                        }
                        DreamService.this.mStarted = true;
                        try {
                            DreamService.this.onDreamingStarted();
                            try {
                                started.sendResult(null);
                            } catch (RemoteException e3) {
                                throw e3.rethrowFromSystemServer();
                            }
                        } catch (Throwable th) {
                            try {
                                started.sendResult(null);
                                throw th;
                            } catch (RemoteException e4) {
                                throw e4.rethrowFromSystemServer();
                            }
                        }
                    }
                }
            });
        }
    }

    private synchronized boolean getWindowFlagValue(int flag, boolean defaultValue) {
        return this.mWindow == null ? defaultValue : (this.mWindow.getAttributes().flags & flag) != 0;
    }

    private synchronized void applyWindowFlags(int flags, int mask) {
        if (this.mWindow != null) {
            WindowManager.LayoutParams lp = this.mWindow.getAttributes();
            lp.flags = applyFlags(lp.flags, flags, mask);
            this.mWindow.setAttributes(lp);
            this.mWindow.getWindowManager().updateViewLayout(this.mWindow.getDecorView(), lp);
        }
    }

    private synchronized boolean getSystemUiVisibilityFlagValue(int flag, boolean defaultValue) {
        View v = this.mWindow == null ? null : this.mWindow.getDecorView();
        return v == null ? defaultValue : (v.getSystemUiVisibility() & flag) != 0;
    }

    private synchronized void applySystemUiVisibilityFlags(int flags, int mask) {
        View v = this.mWindow == null ? null : this.mWindow.getDecorView();
        if (v != null) {
            v.setSystemUiVisibility(applyFlags(v.getSystemUiVisibility(), flags, mask));
        }
    }

    private synchronized int applyFlags(int oldFlags, int flags, int mask) {
        return ((~mask) & oldFlags) | (flags & mask);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Service
    public void dump(final FileDescriptor fd, PrintWriter pw, final String[] args) {
        DumpUtils.dumpAsync(this.mHandler, new DumpUtils.Dump() { // from class: android.service.dreams.DreamService.2
            @Override // com.android.internal.util.DumpUtils.Dump
            public void dump(PrintWriter pw2, String prefix) {
                DreamService.this.dumpOnHandler(fd, pw2, args);
            }
        }, pw, "", 1000L);
    }

    protected synchronized void dumpOnHandler(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.print(this.TAG + ": ");
        if (this.mWindowToken == null) {
            pw.println("stopped");
        } else {
            pw.println("running (token=" + this.mWindowToken + ")");
        }
        pw.println("  window: " + this.mWindow);
        pw.print("  flags:");
        if (isInteractive()) {
            pw.print(" interactive");
        }
        if (isLowProfile()) {
            pw.print(" lowprofile");
        }
        if (isFullscreen()) {
            pw.print(" fullscreen");
        }
        if (isScreenBright()) {
            pw.print(" bright");
        }
        if (isWindowless()) {
            pw.print(" windowless");
        }
        if (isDozing()) {
            pw.print(" dozing");
        } else if (canDoze()) {
            pw.print(" candoze");
        }
        pw.println();
        if (canDoze()) {
            pw.println("  doze screen state: " + Display.stateToString(this.mDozeScreenState));
            pw.println("  doze screen brightness: " + this.mDozeScreenBrightness);
        }
    }

    private static synchronized int clampAbsoluteBrightness(int value) {
        return MathUtils.constrain(value, 0, 255);
    }

    /* loaded from: classes2.dex */
    private final class DreamServiceWrapper extends IDreamService.Stub {
        private DreamServiceWrapper() {
        }

        @Override // android.service.dreams.IDreamService
        public synchronized void attach(final IBinder windowToken, final boolean canDoze, final IRemoteCallback started) {
            DreamService.this.mHandler.post(new Runnable() { // from class: android.service.dreams.DreamService.DreamServiceWrapper.1
                @Override // java.lang.Runnable
                public void run() {
                    DreamService.this.attach(windowToken, canDoze, started);
                }
            });
        }

        @Override // android.service.dreams.IDreamService
        public synchronized void detach() {
            DreamService.this.mHandler.post(new Runnable() { // from class: android.service.dreams.DreamService.DreamServiceWrapper.2
                @Override // java.lang.Runnable
                public void run() {
                    DreamService.this.detach();
                }
            });
        }

        @Override // android.service.dreams.IDreamService
        public synchronized void wakeUp() {
            DreamService.this.mHandler.post(new Runnable() { // from class: android.service.dreams.DreamService.DreamServiceWrapper.3
                @Override // java.lang.Runnable
                public void run() {
                    DreamService.this.wakeUp(true);
                }
            });
        }
    }
}
