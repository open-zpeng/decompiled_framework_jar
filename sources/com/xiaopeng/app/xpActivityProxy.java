package com.xiaopeng.app;

import android.app.Activity;
import android.app.AppGlobals;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.util.xpLogger;
import com.xiaopeng.view.xpWindowManager;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes3.dex */
public class xpActivityProxy {
    private static final String TAG = "xpActivityProxy";
    private Activity mActivity;
    private Handler mHandler = new Handler();
    private BroadcastReceiver mActivitySizeReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.app.xpActivityProxy.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int value = intent.getIntExtra("fullscreen", 0);
            xpActivityProxy.this.resizeActivity(value == 1);
        }
    };

    public void onResume(Activity activity) {
        this.mActivity = activity;
        handleActivityReceiver(true);
    }

    public void onPause(Activity activity) {
        this.mActivity = activity;
        handleActivityReceiver(false);
    }

    private void handleActivityReceiver(boolean register) {
        try {
            Activity activity = this.mActivity;
            if (activity == null) {
                return;
            }
            if (register) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("com.xiaopeng.intent.action.ACTIVITY_SIZE_CHANGED");
                activity.registerReceiver(this.mActivitySizeReceiver, filter);
            } else {
                activity.unregisterReceiver(this.mActivitySizeReceiver);
            }
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resizeActivity(final boolean fullscreen) {
        xpLogger.i(TAG, "resizeActivity fullscreen=" + fullscreen);
        Activity activity = this.mActivity;
        Window window = activity != null ? activity.getWindow() : null;
        if (activity == null || window == null) {
            return;
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        if (lp.type == 12) {
            return;
        }
        if (lp.type == 6 && window.isFloating()) {
            return;
        }
        xpPackageInfo pi = xpPackageManager.getOverridePackageInfo(activity.getBasePackageName());
        boolean screenAdaption = true;
        if ((pi == null || pi.screenAdaption != 1) && !FeatureOption.FO_ACTIVITY_AUTO_ADAPTION_ENABLED) {
            screenAdaption = false;
        }
        if (!screenAdaption) {
            return;
        }
        updatePackageInfo(fullscreen);
        window.setAttributes(getOverrideLayoutParams(window.getAttributes(), fullscreen));
        setViewSystemUiVisibility(window.getDecorView(), fullscreen);
        if (fullscreen) {
            window.addFlags(512);
        } else {
            window.clearFlags(512);
        }
        this.mHandler.post(new Runnable() { // from class: com.xiaopeng.app.xpActivityProxy.1
            @Override // java.lang.Runnable
            public void run() {
                xpActivityProxy.this.relayoutRootView(fullscreen);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void relayoutRootView(boolean fullscreen) {
        Activity activity = this.mActivity;
        Window window = activity != null ? activity.getWindow() : null;
        if (activity == null || window == null) {
            return;
        }
        ArrayList<ViewRootImpl> list = WindowManagerGlobal.getInstance().getRootViews(activity.getActivityToken());
        Iterator<ViewRootImpl> it = list.iterator();
        while (it.hasNext()) {
            ViewRootImpl root = it.next();
            if (root != null && root.mWindowAttributes != null) {
                View v = root.getView();
                WindowManager.LayoutParams lp = getOverrideLayoutParams(root.mWindowAttributes, fullscreen);
                root.setLayoutParams(lp);
                v.setLayoutParams(lp);
                v.forceLayout();
                v.requestLayout();
                activity.getWindowManager().updateViewLayout(v, lp);
                try {
                    root.relayoutWindow(lp, v.getVisibility());
                } catch (Exception e) {
                    Log.i(TAG, "relayoutRootView e=" + e);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("relayoutRootView v=");
                sb.append(v != null ? v.toString() : "");
                sb.append(" attr=");
                sb.append(lp.toString());
                Log.i(TAG, sb.toString());
            }
        }
    }

    private WindowManager.LayoutParams getOverrideLayoutParams(WindowManager.LayoutParams lp, boolean fullscreen) {
        Rect rect = null;
        if (lp == null) {
            return null;
        }
        try {
            rect = fullscreen ? xpWindowManager.getRealFullscreenRect() : xpWindowManager.getWindowManager().getXuiRectByType(1);
        } catch (Exception e) {
        }
        int windowType = lp.type;
        lp.flags = xpWindowManager.getWindowFlags(lp.flags, fullscreen, fullscreen, fullscreen, fullscreen, false);
        lp.systemUiVisibility = xpWindowManager.getSystemUiVisibility(lp.systemUiVisibility, fullscreen, fullscreen, fullscreen, fullscreen, false);
        lp.subtreeSystemUiVisibility = lp.systemUiVisibility;
        if (windowType != 9) {
            lp.width = rect != null ? rect.width() : lp.width;
            lp.height = rect != null ? rect.height() : lp.height;
        }
        if (fullscreen) {
            lp.xpFlags |= 4;
        } else {
            lp.xpFlags &= -5;
        }
        return lp;
    }

    private void setViewSystemUiVisibility(View view, boolean fullscreen) {
        int systemUiVisibility;
        if (view != null) {
            int systemUiVisibility2 = view.getSystemUiVisibility();
            if (fullscreen) {
                systemUiVisibility = systemUiVisibility2 | 256 | 512 | 1024 | 2 | 4 | 4096;
            } else {
                systemUiVisibility = systemUiVisibility2 & (-257) & (-513) & (-1025) & (-3) & (-5) & (-4097);
            }
            view.setSystemUiVisibility(systemUiVisibility);
        }
    }

    private void updatePackageInfo(boolean fullscreen) {
        try {
            AppGlobals.getPackageManager().updateAppScreenFlag(fullscreen ? 4 : 1);
        } catch (Exception e) {
        }
    }
}
