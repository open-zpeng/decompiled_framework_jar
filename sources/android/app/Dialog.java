package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.app.WindowDecorActionBar;
import com.android.internal.policy.PhoneWindow;
import com.xiaopeng.app.xpDialogProxy;
import com.xiaopeng.view.xpWindowManager;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class Dialog implements DialogInterface, Window.Callback, KeyEvent.Callback, View.OnCreateContextMenuListener, Window.OnWindowDismissedCallback {
    public protected static final int CANCEL = 68;
    private static final String DIALOG_HIERARCHY_TAG = "android:dialogHierarchy";
    private static final String DIALOG_SHOWING_TAG = "android:dialogShowing";
    private static final int DISMISS = 67;
    private static final int SHOW = 69;
    private static final String TAG = "Dialog";
    private ActionBar mActionBar;
    private ActionMode mActionMode;
    private int mActionModeTypeStarting;
    private String mCancelAndDismissTaken;
    public protected Message mCancelMessage;
    protected boolean mCancelable;
    private boolean mCanceled;
    public private protected final Context mContext;
    private boolean mCreated;
    View mDecor;
    private xpDialogProxy mDialogProxy;
    private final Runnable mDismissAction;
    public protected Message mDismissMessage;
    public protected final Handler mHandler;
    private boolean mHasFocus;
    public protected final Handler mListenersHandler;
    public protected DialogInterface.OnKeyListener mOnKeyListener;
    public protected Activity mOwnerActivity;
    private SearchEvent mSearchEvent;
    public protected Message mShowMessage;
    public protected boolean mShowing;
    public private protected final Window mWindow;
    private final WindowManager mWindowManager;

    public Dialog(Context context) {
        this(context, 0, true);
    }

    public Dialog(Context context, int themeResId) {
        this(context, themeResId, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Dialog(Context context, int themeResId, boolean createContextThemeWrapper) {
        this.mCancelable = true;
        this.mCreated = false;
        this.mShowing = false;
        this.mCanceled = false;
        this.mHasFocus = false;
        this.mHandler = new Handler();
        this.mActionModeTypeStarting = 0;
        this.mDismissAction = new Runnable() { // from class: android.app.-$$Lambda$oslF4K8Uk6v-6nTRoaEpCmfAptE
            @Override // java.lang.Runnable
            public final void run() {
                Dialog.this.dismissDialog();
            }
        };
        if (createContextThemeWrapper) {
            if (themeResId == 0) {
                TypedValue outValue = new TypedValue();
                context.getTheme().resolveAttribute(16843528, outValue, true);
                themeResId = outValue.resourceId;
            }
            this.mContext = new ContextThemeWrapper(context, themeResId);
        } else {
            this.mContext = context;
        }
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Window w = new PhoneWindow(this.mContext);
        w.setType(9);
        w.setAttributes(xpWindowManager.getOverrideLayoutParams(w.getAttributes(), this));
        this.mWindow = w;
        w.setCallback(this);
        w.setOnWindowDismissedCallback(this);
        w.setOnWindowSwipeDismissedCallback(new Window.OnWindowSwipeDismissedCallback() { // from class: android.app.-$$Lambda$Dialog$zXRzrq3I7H1_zmZ8d_W7t2CQN0I
            @Override // android.view.Window.OnWindowSwipeDismissedCallback
            public final void onWindowSwipeDismissed() {
                Dialog.lambda$new$0(Dialog.this);
            }
        });
        w.setWindowManager(this.mWindowManager, null, null);
        w.setGravity(17);
        this.mListenersHandler = new ListenersHandler(this);
        this.mDialogProxy = new xpDialogProxy(this, this.mContext);
    }

    public static /* synthetic */ void lambda$new$0(Dialog dialog) {
        if (dialog.mCancelable) {
            dialog.cancel();
        }
    }

    @Deprecated
    protected synchronized Dialog(Context context, boolean cancelable, Message cancelCallback) {
        this(context);
        this.mCancelable = cancelable;
        updateWindowForCancelable();
        this.mCancelMessage = cancelCallback;
    }

    protected Dialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        this(context);
        this.mCancelable = cancelable;
        updateWindowForCancelable();
        setOnCancelListener(cancelListener);
    }

    public final Context getContext() {
        return this.mContext;
    }

    public ActionBar getActionBar() {
        return this.mActionBar;
    }

    public final void setOwnerActivity(Activity activity) {
        this.mOwnerActivity = activity;
        getWindow().setVolumeControlStream(this.mOwnerActivity.getVolumeControlStream());
    }

    public final Activity getOwnerActivity() {
        return this.mOwnerActivity;
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void create() {
        if (!this.mCreated) {
            dispatchOnCreate(null);
        }
    }

    public void show() {
        boolean abort = this.mDialogProxy != null && this.mDialogProxy.abortDialog(this.mWindow);
        if (abort) {
            return;
        }
        if (this.mShowing) {
            if (this.mDecor != null) {
                if (this.mWindow.hasFeature(8)) {
                    this.mWindow.invalidatePanelMenu(8);
                }
                this.mDecor.setVisibility(0);
            }
            if (this.mDialogProxy != null) {
                this.mDialogProxy.onShow();
                return;
            }
            return;
        }
        this.mCanceled = false;
        if (!this.mCreated) {
            dispatchOnCreate(null);
        } else {
            Configuration config = this.mContext.getResources().getConfiguration();
            this.mWindow.getDecorView().dispatchConfigurationChanged(config);
        }
        onStart();
        this.mDecor = this.mWindow.getDecorView();
        if (this.mActionBar == null && this.mWindow.hasFeature(8)) {
            ApplicationInfo info = this.mContext.getApplicationInfo();
            this.mWindow.setDefaultIcon(info.icon);
            this.mWindow.setDefaultLogo(info.logo);
            this.mActionBar = new WindowDecorActionBar(this);
        }
        WindowManager.LayoutParams l = this.mWindow.getAttributes();
        boolean restoreSoftInputMode = false;
        if ((l.softInputMode & 256) == 0) {
            l.softInputMode |= 256;
            restoreSoftInputMode = true;
        }
        boolean fullscreen = this.mWindow.hasFeature(15);
        if (fullscreen) {
            l.flags = xpWindowManager.getWindowFlags(l.flags, true, true, true, true, true);
            l.systemUiVisibility = xpWindowManager.getSystemUiVisibility(l.systemUiVisibility, true, true, true, true, true);
            l.subtreeSystemUiVisibility = l.systemUiVisibility;
        }
        try {
            if (!this.mDecor.isAttachedToWindow()) {
                this.mWindowManager.addView(this.mDecor, l);
            }
        } catch (Exception e) {
            Log.d(TAG, "show addView e=" + e);
        }
        if (restoreSoftInputMode) {
            l.softInputMode &= -257;
        }
        this.mShowing = true;
        sendShowMessage();
        if (this.mDialogProxy != null) {
            this.mDialogProxy.onShow();
        }
    }

    public void hide() {
        if (this.mDecor != null) {
            this.mDecor.setVisibility(8);
        }
        if (this.mDialogProxy != null) {
            this.mDialogProxy.onHide();
        }
    }

    @Override // android.content.DialogInterface
    public void dismiss() {
        if (Looper.myLooper() == this.mHandler.getLooper()) {
            dismissDialog();
        } else {
            this.mHandler.post(this.mDismissAction);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0059, code lost:
        if (r6.mActionMode == null) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x005e, code lost:
        if (r6.mDialogProxy == null) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0060, code lost:
        r6.mDialogProxy.onHide();
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0065, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:?, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public private protected void dismissDialog() {
        /*
            r6 = this;
            android.view.View r0 = r6.mDecor
            if (r0 == 0) goto L81
            boolean r0 = r6.mShowing
            if (r0 != 0) goto La
            goto L81
        La:
            android.view.Window r0 = r6.mWindow
            boolean r0 = r0.isDestroyed()
            if (r0 == 0) goto L1a
            java.lang.String r0 = "Dialog"
            java.lang.String r1 = "Tried to dismissDialog() but the Dialog's window was already destroyed!"
            android.util.Log.e(r0, r1)
            return
        L1a:
            r0 = 0
            r1 = 0
            android.view.WindowManager r2 = r6.mWindowManager     // Catch: java.lang.Throwable -> L3e java.lang.Exception -> L40
            android.view.View r3 = r6.mDecor     // Catch: java.lang.Throwable -> L3e java.lang.Exception -> L40
            r2.removeViewImmediate(r3)     // Catch: java.lang.Throwable -> L3e java.lang.Exception -> L40
            android.view.ActionMode r2 = r6.mActionMode
            if (r2 == 0) goto L2c
        L27:
            android.view.ActionMode r2 = r6.mActionMode
            r2.finish()
        L2c:
            r6.mDecor = r0
            android.view.Window r0 = r6.mWindow
            r0.closeAllPanels()
            r6.onStop()
            r6.mShowing = r1
            r6.mHasFocus = r1
            r6.sendDismissMessage()
            goto L5c
        L3e:
            r2 = move-exception
            goto L66
        L40:
            r2 = move-exception
            java.lang.String r3 = "Dialog"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L3e
            r4.<init>()     // Catch: java.lang.Throwable -> L3e
            java.lang.String r5 = "dismissDialog e:"
            r4.append(r5)     // Catch: java.lang.Throwable -> L3e
            r4.append(r2)     // Catch: java.lang.Throwable -> L3e
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L3e
            android.util.Log.d(r3, r4)     // Catch: java.lang.Throwable -> L3e
            android.view.ActionMode r2 = r6.mActionMode
            if (r2 == 0) goto L2c
            goto L27
        L5c:
            com.xiaopeng.app.xpDialogProxy r0 = r6.mDialogProxy
            if (r0 == 0) goto L65
            com.xiaopeng.app.xpDialogProxy r0 = r6.mDialogProxy
            r0.onHide()
        L65:
            return
        L66:
            android.view.ActionMode r3 = r6.mActionMode
            if (r3 == 0) goto L6f
            android.view.ActionMode r3 = r6.mActionMode
            r3.finish()
        L6f:
            r6.mDecor = r0
            android.view.Window r0 = r6.mWindow
            r0.closeAllPanels()
            r6.onStop()
            r6.mShowing = r1
            r6.mHasFocus = r1
            r6.sendDismissMessage()
            throw r2
        L81:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Dialog.dismissDialog():void");
    }

    private synchronized void sendDismissMessage() {
        if (this.mDismissMessage != null) {
            Message.obtain(this.mDismissMessage).sendToTarget();
        }
    }

    private synchronized void sendShowMessage() {
        if (this.mShowMessage != null) {
            Message.obtain(this.mShowMessage).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dispatchOnCreate(Bundle savedInstanceState) {
        if (!this.mCreated) {
            onCreate(savedInstanceState);
            this.mCreated = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onStart() {
        if (this.mActionBar != null) {
            this.mActionBar.setShowHideAnimationEnabled(true);
        }
        if (this.mDialogProxy != null) {
            this.mDialogProxy.onStart();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onStop() {
        if (this.mActionBar != null) {
            this.mActionBar.setShowHideAnimationEnabled(false);
        }
        if (this.mDialogProxy != null) {
            this.mDialogProxy.onStop();
        }
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(DIALOG_SHOWING_TAG, this.mShowing);
        if (this.mCreated) {
            bundle.putBundle(DIALOG_HIERARCHY_TAG, this.mWindow.saveHierarchyState());
        }
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Bundle dialogHierarchyState = savedInstanceState.getBundle(DIALOG_HIERARCHY_TAG);
        if (dialogHierarchyState == null) {
            return;
        }
        dispatchOnCreate(savedInstanceState);
        this.mWindow.restoreHierarchyState(dialogHierarchyState);
        if (savedInstanceState.getBoolean(DIALOG_SHOWING_TAG)) {
            show();
        }
    }

    public Window getWindow() {
        return this.mWindow;
    }

    public View getCurrentFocus() {
        if (this.mWindow != null) {
            return this.mWindow.getCurrentFocus();
        }
        return null;
    }

    public <T extends View> T findViewById(int id) {
        return (T) this.mWindow.findViewById(id);
    }

    public final <T extends View> T requireViewById(int id) {
        T view = (T) findViewById(id);
        if (view == null) {
            throw new IllegalArgumentException("ID does not reference a View inside this Dialog");
        }
        return view;
    }

    public void setContentView(int layoutResID) {
        this.mWindow.setContentView(layoutResID);
    }

    public void setContentView(View view) {
        this.mWindow.setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        this.mWindow.setContentView(view, params);
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        this.mWindow.addContentView(view, params);
    }

    public void setTitle(CharSequence title) {
        this.mWindow.setTitle(title);
        this.mWindow.getAttributes().setTitle(title);
    }

    public void setTitle(int titleId) {
        setTitle(this.mContext.getText(titleId));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 || keyCode == 111) {
            event.startTracking();
            return true;
        }
        return false;
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == 4 || keyCode == 111) && event.isTracking() && !event.isCanceled()) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    public void onBackPressed() {
        if (this.mCancelable) {
            cancel();
        }
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mCancelable && this.mShowing && this.mWindow.shouldCloseOnTouch(this.mContext, event)) {
            cancel();
            return true;
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }

    @Override // android.view.Window.Callback
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (this.mDecor != null) {
            this.mWindowManager.updateViewLayout(this.mDecor, params);
        }
    }

    @Override // android.view.Window.Callback
    public void onContentChanged() {
    }

    @Override // android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        this.mHasFocus = hasFocus;
        if (this.mDialogProxy != null) {
            this.mDialogProxy.onWindowFocusChanged(hasFocus);
        }
    }

    @Override // android.view.Window.Callback
    public void onAttachedToWindow() {
        if (this.mDialogProxy != null) {
            this.mDialogProxy.onAttachedToWindow();
        }
    }

    @Override // android.view.Window.Callback
    public void onDetachedFromWindow() {
        if (this.mDialogProxy != null) {
            this.mDialogProxy.onDetachedFromWindow();
        }
    }

    @Override // android.view.Window.OnWindowDismissedCallback
    public synchronized void onWindowDismissed(boolean finishTask, boolean suppressWindowTransition) {
        dismiss();
        if (this.mDialogProxy != null) {
            this.mDialogProxy.onWindowDismissed(finishTask, suppressWindowTransition);
        }
    }

    @Override // android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((this.mOnKeyListener == null || !this.mOnKeyListener.onKey(this, event.getKeyCode(), event)) && !this.mWindow.superDispatchKeyEvent(event)) {
            return event.dispatch(this, this.mDecor != null ? this.mDecor.getKeyDispatcherState() : null, this);
        }
        return true;
    }

    @Override // android.view.Window.Callback
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        if (this.mWindow.superDispatchKeyShortcutEvent(event)) {
            return true;
        }
        return onKeyShortcut(event.getKeyCode(), event);
    }

    @Override // android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.mWindow.superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override // android.view.Window.Callback
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        if (this.mWindow.superDispatchTrackballEvent(ev)) {
            return true;
        }
        return onTrackballEvent(ev);
    }

    @Override // android.view.Window.Callback
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        if (this.mWindow.superDispatchGenericMotionEvent(ev)) {
            return true;
        }
        return onGenericMotionEvent(ev);
    }

    @Override // android.view.Window.Callback
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        event.setClassName(getClass().getName());
        event.setPackageName(this.mContext.getPackageName());
        ViewGroup.LayoutParams params = getWindow().getAttributes();
        boolean isFullScreen = params.width == -1 && params.height == -1;
        event.setFullScreen(isFullScreen);
        return false;
    }

    @Override // android.view.Window.Callback
    public View onCreatePanelView(int featureId) {
        return null;
    }

    @Override // android.view.Window.Callback
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == 0) {
            return onCreateOptionsMenu(menu);
        }
        return false;
    }

    @Override // android.view.Window.Callback
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId != 0 || menu == null) {
            return true;
        }
        return onPrepareOptionsMenu(menu) && menu.hasVisibleItems();
    }

    @Override // android.view.Window.Callback
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == 8) {
            this.mActionBar.dispatchMenuVisibilityChanged(true);
        }
        return true;
    }

    @Override // android.view.Window.Callback
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return false;
    }

    @Override // android.view.Window.Callback
    public void onPanelClosed(int featureId, Menu menu) {
        if (featureId == 8) {
            this.mActionBar.dispatchMenuVisibilityChanged(false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void onOptionsMenuClosed(Menu menu) {
    }

    public void openOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            this.mWindow.openPanel(0, null);
        }
    }

    public void closeOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            this.mWindow.closePanel(0);
        }
    }

    public void invalidateOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            this.mWindow.invalidatePanelMenu(0);
        }
    }

    @Override // android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    }

    public void registerForContextMenu(View view) {
        view.setOnCreateContextMenuListener(this);
    }

    public void unregisterForContextMenu(View view) {
        view.setOnCreateContextMenuListener(null);
    }

    public void openContextMenu(View view) {
        view.showContextMenu();
    }

    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

    public void onContextMenuClosed(Menu menu) {
    }

    @Override // android.view.Window.Callback
    public boolean onSearchRequested(SearchEvent searchEvent) {
        this.mSearchEvent = searchEvent;
        return onSearchRequested();
    }

    @Override // android.view.Window.Callback
    public boolean onSearchRequested() {
        SearchManager searchManager = (SearchManager) this.mContext.getSystemService("search");
        ComponentName appName = getAssociatedActivity();
        if (appName != null && searchManager.getSearchableInfo(appName) != null) {
            searchManager.startSearch(null, false, appName, null, false);
            dismiss();
            return true;
        }
        return false;
    }

    public final SearchEvent getSearchEvent() {
        return this.mSearchEvent;
    }

    @Override // android.view.Window.Callback
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        if (this.mActionBar != null && this.mActionModeTypeStarting == 0) {
            return this.mActionBar.startActionMode(callback);
        }
        return null;
    }

    @Override // android.view.Window.Callback
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        try {
            this.mActionModeTypeStarting = type;
            return onWindowStartingActionMode(callback);
        } finally {
            this.mActionModeTypeStarting = 0;
        }
    }

    @Override // android.view.Window.Callback
    public void onActionModeStarted(ActionMode mode) {
        this.mActionMode = mode;
    }

    @Override // android.view.Window.Callback
    public void onActionModeFinished(ActionMode mode) {
        if (mode == this.mActionMode) {
            this.mActionMode = null;
        }
    }

    private synchronized ComponentName getAssociatedActivity() {
        Activity activity = this.mOwnerActivity;
        Context context = getContext();
        while (true) {
            Context context2 = null;
            if (activity != null || context == null) {
                break;
            } else if (context instanceof Activity) {
                activity = (Activity) context;
            } else {
                if (context instanceof ContextWrapper) {
                    context2 = ((ContextWrapper) context).getBaseContext();
                }
                context = context2;
            }
        }
        if (activity == null) {
            return null;
        }
        return activity.getComponentName();
    }

    public void takeKeyEvents(boolean get) {
        this.mWindow.takeKeyEvents(get);
    }

    public final boolean requestWindowFeature(int featureId) {
        return getWindow().requestFeature(featureId);
    }

    public final void setFeatureDrawableResource(int featureId, int resId) {
        getWindow().setFeatureDrawableResource(featureId, resId);
    }

    public final void setFeatureDrawableUri(int featureId, Uri uri) {
        getWindow().setFeatureDrawableUri(featureId, uri);
    }

    public final void setFeatureDrawable(int featureId, Drawable drawable) {
        getWindow().setFeatureDrawable(featureId, drawable);
    }

    public final void setFeatureDrawableAlpha(int featureId, int alpha) {
        getWindow().setFeatureDrawableAlpha(featureId, alpha);
    }

    public LayoutInflater getLayoutInflater() {
        return getWindow().getLayoutInflater();
    }

    public void setCancelable(boolean flag) {
        this.mCancelable = flag;
        updateWindowForCancelable();
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        if (cancel && !this.mCancelable) {
            this.mCancelable = true;
            updateWindowForCancelable();
        }
        this.mWindow.setCloseOnTouchOutside(cancel);
    }

    @Override // android.content.DialogInterface
    public void cancel() {
        if (!this.mCanceled && this.mCancelMessage != null) {
            this.mCanceled = true;
            Message.obtain(this.mCancelMessage).sendToTarget();
        }
        dismiss();
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        if (this.mCancelAndDismissTaken != null) {
            throw new IllegalStateException("OnCancelListener is already taken by " + this.mCancelAndDismissTaken + " and can not be replaced.");
        } else if (listener != null) {
            this.mCancelMessage = this.mListenersHandler.obtainMessage(68, listener);
        } else {
            this.mCancelMessage = null;
        }
    }

    public void setCancelMessage(Message msg) {
        this.mCancelMessage = msg;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        if (this.mCancelAndDismissTaken != null) {
            throw new IllegalStateException("OnDismissListener is already taken by " + this.mCancelAndDismissTaken + " and can not be replaced.");
        } else if (listener != null) {
            this.mDismissMessage = this.mListenersHandler.obtainMessage(67, listener);
        } else {
            this.mDismissMessage = null;
        }
    }

    public void setOnShowListener(DialogInterface.OnShowListener listener) {
        if (listener != null) {
            this.mShowMessage = this.mListenersHandler.obtainMessage(69, listener);
        } else {
            this.mShowMessage = null;
        }
    }

    public void setDismissMessage(Message msg) {
        this.mDismissMessage = msg;
    }

    public synchronized boolean takeCancelAndDismissListeners(String msg, DialogInterface.OnCancelListener cancel, DialogInterface.OnDismissListener dismiss) {
        if (this.mCancelAndDismissTaken != null) {
            this.mCancelAndDismissTaken = null;
        } else if (this.mCancelMessage != null || this.mDismissMessage != null) {
            return false;
        }
        setOnCancelListener(cancel);
        setOnDismissListener(dismiss);
        this.mCancelAndDismissTaken = msg;
        return true;
    }

    public final void setVolumeControlStream(int streamType) {
        getWindow().setVolumeControlStream(streamType);
    }

    public final int getVolumeControlStream() {
        return getWindow().getVolumeControlStream();
    }

    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        this.mOnKeyListener = onKeyListener;
    }

    /* loaded from: classes.dex */
    private static final class ListenersHandler extends Handler {
        private final WeakReference<DialogInterface> mDialog;

        public synchronized ListenersHandler(Dialog dialog) {
            this.mDialog = new WeakReference<>(dialog);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 67:
                    ((DialogInterface.OnDismissListener) msg.obj).onDismiss(this.mDialog.get());
                    return;
                case 68:
                    ((DialogInterface.OnCancelListener) msg.obj).onCancel(this.mDialog.get());
                    return;
                case 69:
                    ((DialogInterface.OnShowListener) msg.obj).onShow(this.mDialog.get());
                    return;
                default:
                    return;
            }
        }
    }

    private synchronized void updateWindowForCancelable() {
        this.mWindow.setCloseOnSwipeEnabled(this.mCancelable);
    }

    public boolean isCancelable() {
        return this.mCancelable;
    }

    public boolean hasFocus() {
        return this.mHasFocus;
    }

    public int myPid() {
        return Process.myPid();
    }

    public int myUid() {
        return Process.myUid();
    }
}
