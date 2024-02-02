package com.xiaopeng.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import com.xiaopeng.util.DebugOption;
import com.xiaopeng.util.xpLogger;
import com.xiaopeng.view.xpWindowManager;
/* loaded from: classes3.dex */
public class xpDialogProxy {
    public static final int DIALOG_EVENT_DISMISS_FROM_DEFAULT = 1;
    public static final int DIALOG_EVENT_DISMISS_FROM_RESUMED = 2;
    public static final int DIALOG_EVENT_DISMISS_FROM_TOPPING = 4;
    public static final int DIALOG_EVENT_DISMISS_MASK = Integer.MIN_VALUE;
    public static final int DIALOG_EVENT_DISMISS_ONLY_APPLICATION = 16;
    public static final int DIALOG_EVENT_DISMISS_ONLY_SYSTEM = 8;
    private static final String TAG = "xpDialogProxy";
    private Context mContext;
    private Dialog mDialog;
    private static final boolean DEBUG = DebugOption.DEBUG_DIALOG_PROXY;
    public static final String KEY_DIALOG_EVENT = "key_dialog_event";
    public static final Uri URI_DIALOG_EVENT = Settings.Secure.getUriFor(KEY_DIALOG_EVENT);
    private Handler mHandler = new Handler();
    private ContentObserver mContentObserver = new ContentObserver(this.mHandler) { // from class: com.xiaopeng.app.xpDialogProxy.1
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            if (selfChange) {
                return;
            }
            int event = Settings.Secure.getInt(xpDialogProxy.this.mContext.getContentResolver(), xpDialogProxy.KEY_DIALOG_EVENT, 0);
            xpDialogProxy.this.handleEventChanged(event);
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.app.xpDialogProxy.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                xpDialogProxy.this.handleScreenChanged(true);
            }
        }
    };

    public xpDialogProxy(Dialog dialog, Context context) {
        this.mDialog = dialog;
        this.mContext = context;
    }

    public boolean abortDialog(Window window) {
        int windowType = window != null ? window.getAttributes().type : 0;
        if (!xpDialogManager.isDialogValid(windowType, this.mContext.getBasePackageName())) {
            xpWindowManager.triggerWindowErrorEvent(this.mContext, this.mDialog);
            if (DEBUG) {
                xpLogger.i(TAG, "triggerWindowErrorEvent dialog=" + System.identityHashCode(this.mDialog));
            }
            return true;
        } else if (!xpDialogManager.waitActivityReadyIfNeed(this.mDialog)) {
            return false;
        } else {
            if (DEBUG) {
                xpLogger.i(TAG, "waitActivityReadyIfNeed dialog=" + System.identityHashCode(this.mDialog));
            }
            return true;
        }
    }

    public void onStart() {
        if (DEBUG) {
            xpLogger.i(TAG, "onStart dialog=" + System.identityHashCode(this.mDialog));
        }
        registerObserver();
        registerReceiver();
    }

    public void onStop() {
        if (DEBUG) {
            xpLogger.i(TAG, "onStop dialog=" + System.identityHashCode(this.mDialog));
        }
        unregisterObserver();
        unregisterReceiver();
    }

    public void onShow() {
        if (DEBUG) {
            xpLogger.i(TAG, "onShow dialog=" + System.identityHashCode(this.mDialog));
        }
        xpDialogManager.setDialogRecorder(xpDialogInfo.create(this.mDialog, 1));
    }

    public void onHide() {
        if (DEBUG) {
            xpLogger.i(TAG, "onHide dialog=" + System.identityHashCode(this.mDialog));
        }
        xpDialogManager.setDialogRecorder(xpDialogInfo.create(this.mDialog, 2));
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (DEBUG) {
            xpLogger.i(TAG, "onWindowFocusChanged hasFocus=" + hasFocus + " dialog=" + System.identityHashCode(this.mDialog));
        }
        xpDialogManager.setDialogRecorder(xpDialogInfo.create(this.mDialog, hasFocus ? 5 : 6));
    }

    public void onAttachedToWindow() {
        if (DEBUG) {
            xpLogger.i(TAG, "onAttachedToWindow dialog=" + System.identityHashCode(this.mDialog));
        }
        xpDialogManager.setDialogRecorder(xpDialogInfo.create(this.mDialog, 3));
    }

    public void onDetachedFromWindow() {
        if (DEBUG) {
            xpLogger.i(TAG, "onDetachedFromWindow dialog=" + System.identityHashCode(this.mDialog));
        }
        xpDialogManager.setDialogRecorder(xpDialogInfo.create(this.mDialog, 4));
    }

    public void onWindowDismissed(boolean finishTask, boolean suppressWindowTransition) {
        if (DEBUG) {
            xpLogger.i(TAG, "onWindowDismissed finishTask=" + finishTask + " suppressWindowTransition=" + suppressWindowTransition);
        }
        xpDialogManager.setDialogRecorder(xpDialogInfo.create(this.mDialog, 7));
    }

    private void closeDialog(Dialog dialog) {
        if (dialog != null) {
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes().type == 2011) {
                dialog.hide();
            } else {
                dialog.dismiss();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEventChanged(int value) {
        if (this.mDialog != null && this.mDialog.getWindow() != null) {
            int event = Integer.MAX_VALUE & value;
            Window window = this.mDialog.getWindow();
            Activity activity = xpWindowManager.getDialogOwnerActivity(this.mDialog);
            int type = window.getAttributes().type;
            boolean system = xpWindowManager.isSystemAlertWindowType(type);
            boolean resumed = activity != null && activity.isResumed();
            boolean cancelable = this.mDialog.isCancelable();
            boolean fullscreen = window != null ? window.hasFeature(15) : false;
            boolean closeOutside = window != null ? window.shouldCloseOnTouchOutside() : false;
            if (event == 4) {
                if (xpDialogManager.shouldDismissByPolicy(window.getAttributes().type)) {
                    xpWindowManager.triggerWindowErrorEvent(this.mContext, this.mDialog);
                    closeDialog(this.mDialog);
                }
            } else if (event == 8) {
                if (system) {
                    if (cancelable || closeOutside) {
                        closeDialog(this.mDialog);
                    }
                }
            } else if (event != 16) {
                switch (event) {
                    case 1:
                        if (cancelable || closeOutside) {
                            closeDialog(this.mDialog);
                            return;
                        }
                        return;
                    case 2:
                        if (type != 9) {
                            if (type == 2048) {
                                closeDialog(this.mDialog);
                                return;
                            }
                            return;
                        }
                        if (fullscreen && cancelable && closeOutside && !resumed) {
                            closeDialog(this.mDialog);
                        }
                        xpLogger.i(TAG, "handleEventChanged resumed=" + resumed + " cancelable=" + cancelable + " fullscreen=" + fullscreen + " closeOutside=" + closeOutside);
                        return;
                    default:
                        return;
                }
            } else if (system) {
            } else {
                if (cancelable || closeOutside) {
                    closeDialog(this.mDialog);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScreenChanged(boolean on) {
        xpLogger.i(TAG, "onScreenStateChanged on=" + on);
        if (this.mDialog != null && on) {
            Window window = this.mDialog.getWindow();
            View decor = window != null ? window.getDecorView() : null;
            if (window != null && decor != null && decor.isAttachedToWindow()) {
                window.setAttributes(window.getAttributes());
            }
        }
    }

    private void registerObserver() {
        try {
            this.mContext.getContentResolver().registerContentObserver(URI_DIALOG_EVENT, true, this.mContentObserver);
        } catch (Exception e) {
        }
    }

    private void unregisterObserver() {
        try {
            this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
        } catch (Exception e) {
        }
    }

    private void registerReceiver() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
        } catch (Exception e) {
            Log.d(TAG, "registerReceiver e=" + e);
        }
    }

    private void unregisterReceiver() {
        try {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        } catch (Exception e) {
            Log.d(TAG, "unregisterReceiver e=" + e);
        }
    }
}
