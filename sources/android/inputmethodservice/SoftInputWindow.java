package android.inputmethodservice;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public class SoftInputWindow extends Dialog {
    private static final boolean DEBUG = false;
    private static final String TAG = "SoftInputWindow";
    final boolean mAutomotiveHideNavBarForKeyboard;
    private final Rect mBounds;
    final Callback mCallback;
    final KeyEvent.DispatcherState mDispatcherState;
    final int mGravity;
    final KeyEvent.Callback mKeyEventCallback;
    final String mName;
    final boolean mTakesFocus;
    private int mWindowState;
    final int mWindowType;

    /* loaded from: classes.dex */
    public interface Callback {
        void onBackPressed();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    private @interface SoftInputWindowState {
        public static final int DESTROYED = 4;
        public static final int REJECTED_AT_LEAST_ONCE = 3;
        public static final int SHOWN_AT_LEAST_ONCE = 2;
        public static final int TOKEN_PENDING = 0;
        public static final int TOKEN_SET = 1;
    }

    public void setToken(IBinder token) {
        int i = this.mWindowState;
        if (i == 0) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.token = token;
            getWindow().setAttributes(lp);
            updateWindowState(1);
        } else if (i == 1 || i == 2 || i == 3) {
            throw new IllegalStateException("setToken can be called only once");
        } else {
            if (i == 4) {
                Log.i(TAG, "Ignoring setToken() because window is already destroyed.");
                return;
            }
            throw new IllegalStateException("Unexpected state=" + this.mWindowState);
        }
    }

    public SoftInputWindow(Context context, String name, int theme, Callback callback, KeyEvent.Callback keyEventCallback, KeyEvent.DispatcherState dispatcherState, int windowType, int gravity, boolean takesFocus) {
        super(context, theme);
        this.mBounds = new Rect();
        this.mWindowState = 0;
        this.mName = name;
        this.mCallback = callback;
        this.mKeyEventCallback = keyEventCallback;
        this.mDispatcherState = dispatcherState;
        this.mWindowType = windowType;
        this.mGravity = gravity;
        this.mTakesFocus = takesFocus;
        this.mAutomotiveHideNavBarForKeyboard = context.getResources().getBoolean(R.bool.config_automotiveHideNavBarForKeyboard);
        initDockWindow();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        this.mDispatcherState.reset();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getWindow().getDecorView().getHitRect(this.mBounds);
        if (ev.isWithinBoundsNoHistory(this.mBounds.left, this.mBounds.top, this.mBounds.right - 1, this.mBounds.bottom - 1)) {
            return super.dispatchTouchEvent(ev);
        }
        MotionEvent temp = ev.clampNoHistory(this.mBounds.left, this.mBounds.top, this.mBounds.right - 1, this.mBounds.bottom - 1);
        boolean handled = super.dispatchTouchEvent(temp);
        temp.recycle();
        return handled;
    }

    public void setGravity(int gravity) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = gravity;
        updateWidthHeight(lp);
        getWindow().setAttributes(lp);
    }

    public int getGravity() {
        return getWindow().getAttributes().gravity;
    }

    private void updateWidthHeight(WindowManager.LayoutParams lp) {
        if (lp.gravity == 48 || lp.gravity == 80) {
            lp.width = -1;
            lp.height = -2;
            return;
        }
        lp.width = -2;
        lp.height = -1;
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        KeyEvent.Callback callback = this.mKeyEventCallback;
        if (callback != null && callback.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        KeyEvent.Callback callback = this.mKeyEventCallback;
        if (callback != null && callback.onKeyLongPress(keyCode, event)) {
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        KeyEvent.Callback callback = this.mKeyEventCallback;
        if (callback != null && callback.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        KeyEvent.Callback callback = this.mKeyEventCallback;
        if (callback != null && callback.onKeyMultiple(keyCode, count, event)) {
            return true;
        }
        return super.onKeyMultiple(keyCode, count, event);
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void initDockWindow() {
        int windowSetFlags;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.type = this.mWindowType;
        lp.setTitle(this.mName);
        lp.gravity = this.mGravity;
        updateWidthHeight(lp);
        getWindow().setAttributes(lp);
        int windowModFlags = 266;
        if (!this.mTakesFocus) {
            windowSetFlags = 256 | 8;
        } else {
            windowSetFlags = 256 | 32;
            windowModFlags = 266 | 32;
        }
        if (isAutomotive() && this.mAutomotiveHideNavBarForKeyboard) {
            windowSetFlags |= 512;
            windowModFlags |= 512;
        }
        getWindow().setFlags(windowSetFlags, windowModFlags);
    }

    @Override // android.app.Dialog
    public final void show() {
        int i = this.mWindowState;
        if (i == 0) {
            throw new IllegalStateException("Window token is not set yet.");
        }
        if (i == 1 || i == 2) {
            try {
                super.show();
                updateWindowState(2);
            } catch (WindowManager.BadTokenException e) {
                Log.i(TAG, "Probably the IME window token is already invalidated. show() does nothing.");
                updateWindowState(3);
            }
        } else if (i == 3) {
            Log.i(TAG, "Not trying to call show() because it was already rejected once.");
        } else if (i == 4) {
            Log.i(TAG, "Ignoring show() because the window is already destroyed.");
        } else {
            throw new IllegalStateException("Unexpected state=" + this.mWindowState);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void dismissForDestroyIfNecessary() {
        int i = this.mWindowState;
        if (i == 0 || i == 1) {
            updateWindowState(4);
        } else if (i == 2) {
            try {
                getWindow().setWindowAnimations(0);
                dismiss();
            } catch (WindowManager.BadTokenException e) {
                Log.i(TAG, "Probably the IME window token is already invalidated. No need to dismiss it.");
            }
            updateWindowState(4);
        } else if (i == 3) {
            Log.i(TAG, "Not trying to dismiss the window because it is most likely unnecessary.");
            updateWindowState(4);
        } else if (i == 4) {
            throw new IllegalStateException("dismissForDestroyIfNecessary can be called only once");
        } else {
            throw new IllegalStateException("Unexpected state=" + this.mWindowState);
        }
    }

    private void updateWindowState(int newState) {
        this.mWindowState = newState;
    }

    private boolean isAutomotive() {
        return getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE);
    }

    private static String stateToString(int state) {
        if (state != 0) {
            if (state != 1) {
                if (state != 2) {
                    if (state != 3) {
                        if (state == 4) {
                            return "DESTROYED";
                        }
                        throw new IllegalStateException("Unknown state=" + state);
                    }
                    return "REJECTED_AT_LEAST_ONCE";
                }
                return "SHOWN_AT_LEAST_ONCE";
            }
            return "TOKEN_SET";
        }
        return "TOKEN_PENDING";
    }
}
