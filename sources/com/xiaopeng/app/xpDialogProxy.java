package com.xiaopeng.app;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import com.xiaopeng.util.DebugOption;
import com.xiaopeng.util.xpLogger;
import com.xiaopeng.view.SharedDisplayManager;
import com.xiaopeng.view.xpWindowManager;

/* loaded from: classes3.dex */
public class xpDialogProxy {
    private static final boolean DEBUG = DebugOption.DEBUG_DIALOG_PROXY;
    public static final String EXTRA_FROM_REASON = "fromReason";
    public static final String EXTRA_ONLY_APPLICATION = "applicationOnly";
    public static final String EXTRA_ONLY_SYSTEM = "systemOnly";
    public static final String EXTRA_ONLY_TOP = "topOnly";
    public static final String FROM_DEFAULT = "fromDefault";
    public static final String FROM_RESUMED = "fromResumed";
    public static final String FROM_TOPPING = "fromTopping";
    private static final String TAG = "xpDialogProxy";
    private Context mContext;
    private Dialog mDialog;
    private final String ACTION_DISMISS_DIALOG = "com.xiaopeng.intent.action.DISMISS_DIALOG";
    private Handler mHandler = new Handler();
    private boolean mReceiverRegistered = false;
    private boolean mObserverRegistered = false;
    private int mScreenId = -1;
    private ContentObserver mContentObserver = new ContentObserver(this.mHandler) { // from class: com.xiaopeng.app.xpDialogProxy.1
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.app.xpDialogProxy.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            char c = 65535;
            int hashCode = action.hashCode();
            if (hashCode != -1997022345) {
                if (hashCode == -1454123155 && action.equals(Intent.ACTION_SCREEN_ON)) {
                    c = 0;
                }
            } else if (action.equals("com.xiaopeng.intent.action.DISMISS_DIALOG")) {
                c = 1;
            }
            if (c == 0) {
                xpDialogProxy.this.handleScreenChanged(true);
            } else if (c == 1) {
                xpDialogProxy.this.handleDismissDialog(intent);
            }
        }
    };

    public xpDialogProxy(Dialog dialog, Context context) {
        this.mDialog = dialog;
        this.mContext = context;
    }

    public boolean abortDialog(Window window) {
        int windowType = window != null ? window.getAttributes().type : 0;
        Context context = this.mContext;
        if (!xpDialogManager.isDialogValid(context, this.mDialog, windowType, context.getBasePackageName())) {
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

    public int getSharedId() {
        int screenId = this.mScreenId;
        if (screenId != 0) {
            return screenId != 1 ? -1 : 1;
        }
        return 0;
    }

    public int getScreenId() {
        int screenId = this.mScreenId;
        if (screenId < 0 && SharedDisplayManager.enable()) {
            try {
                String packageName = this.mContext.getPackageName();
                screenId = xpWindowManager.getWindowManager().getScreenId(packageName);
            } catch (Exception e) {
            }
        }
        if (screenId < 0) {
            return 0;
        }
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.mScreenId = screenId;
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
    public void handleScreenChanged(boolean on) {
        xpLogger.i(TAG, "onScreenStateChanged on=" + on);
        Dialog dialog = this.mDialog;
        if (dialog != null && on) {
            Window window = dialog.getWindow();
            View decor = window != null ? window.getDecorView() : null;
            if (window != null && decor != null && decor.isAttachedToWindow()) {
                window.setAttributes(window.getAttributes());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0179  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0186 A[Catch: Exception -> 0x018c, TRY_LEAVE, TryCatch #0 {Exception -> 0x018c, blocks: (B:12:0x0018, B:14:0x001e, B:15:0x0024, B:17:0x0034, B:22:0x003e, B:24:0x006b, B:28:0x0074, B:36:0x0087, B:111:0x0186, B:58:0x00c7, B:60:0x00cd, B:62:0x00e0, B:68:0x00f6, B:70:0x00fe, B:75:0x0109, B:80:0x0117, B:84:0x014a, B:86:0x0150, B:94:0x0160, B:99:0x016b, B:43:0x00a2, B:46:0x00aa, B:49:0x00b4), top: B:117:0x0018 }] */
    /* JADX WARN: Removed duplicated region for block: B:120:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x006b A[Catch: Exception -> 0x018c, TryCatch #0 {Exception -> 0x018c, blocks: (B:12:0x0018, B:14:0x001e, B:15:0x0024, B:17:0x0034, B:22:0x003e, B:24:0x006b, B:28:0x0074, B:36:0x0087, B:111:0x0186, B:58:0x00c7, B:60:0x00cd, B:62:0x00e0, B:68:0x00f6, B:70:0x00fe, B:75:0x0109, B:80:0x0117, B:84:0x014a, B:86:0x0150, B:94:0x0160, B:99:0x016b, B:43:0x00a2, B:46:0x00aa, B:49:0x00b4), top: B:117:0x0018 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0073 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0074 A[Catch: Exception -> 0x018c, TryCatch #0 {Exception -> 0x018c, blocks: (B:12:0x0018, B:14:0x001e, B:15:0x0024, B:17:0x0034, B:22:0x003e, B:24:0x006b, B:28:0x0074, B:36:0x0087, B:111:0x0186, B:58:0x00c7, B:60:0x00cd, B:62:0x00e0, B:68:0x00f6, B:70:0x00fe, B:75:0x0109, B:80:0x0117, B:84:0x014a, B:86:0x0150, B:94:0x0160, B:99:0x016b, B:43:0x00a2, B:46:0x00aa, B:49:0x00b4), top: B:117:0x0018 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0086 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0087 A[Catch: Exception -> 0x018c, TryCatch #0 {Exception -> 0x018c, blocks: (B:12:0x0018, B:14:0x001e, B:15:0x0024, B:17:0x0034, B:22:0x003e, B:24:0x006b, B:28:0x0074, B:36:0x0087, B:111:0x0186, B:58:0x00c7, B:60:0x00cd, B:62:0x00e0, B:68:0x00f6, B:70:0x00fe, B:75:0x0109, B:80:0x0117, B:84:0x014a, B:86:0x0150, B:94:0x0160, B:99:0x016b, B:43:0x00a2, B:46:0x00aa, B:49:0x00b4), top: B:117:0x0018 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0146  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void handleDismissDialog(android.content.Intent r25) {
        /*
            Method dump skipped, instructions count: 400
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.app.xpDialogProxy.handleDismissDialog(android.content.Intent):void");
    }

    private void registerObserver() {
    }

    private void unregisterObserver() {
    }

    private void registerReceiver() {
        try {
            if (this.mReceiverRegistered) {
                return;
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.xiaopeng.intent.action.DISMISS_DIALOG");
            filter.addAction(Intent.ACTION_SCREEN_ON);
            this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
            this.mReceiverRegistered = true;
        } catch (Exception e) {
            Log.d(TAG, "registerReceiver e=" + e);
        }
    }

    private void unregisterReceiver() {
        try {
            if (this.mReceiverRegistered) {
                this.mContext.unregisterReceiver(this.mBroadcastReceiver);
                this.mReceiverRegistered = false;
            }
        } catch (Exception e) {
            Log.d(TAG, "unregisterReceiver e=" + e);
        }
    }
}
