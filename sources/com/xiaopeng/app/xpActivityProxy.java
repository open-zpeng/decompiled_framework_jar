package com.xiaopeng.app;

import android.app.Activity;
import android.app.AppGlobals;
import android.app.WindowConfiguration;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.util.xpLogger;
import com.xiaopeng.view.SharedDisplayManager;
import com.xiaopeng.view.xpWindowManager;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes3.dex */
public class xpActivityProxy {
    private static final String TAG = "xpActivityProxy";
    private Activity mActivity;
    private boolean mHasFocus = false;
    private Handler mHandler = new Handler();
    private BroadcastReceiver mActivityReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.app.xpActivityProxy.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (((action.hashCode() == -288516212 && action.equals("com.xiaopeng.intent.action.ACTIVITY_SIZE_CHANGED")) ? (char) 0 : (char) 65535) == 0) {
                int value = intent.getIntExtra("fullscreen", 0);
                xpActivityProxy.this.resizeActivity(value == 1);
            }
        }
    };

    public void onStart(Activity activity) {
        this.mActivity = activity;
    }

    public void onResume(Activity activity) {
        this.mActivity = activity;
        handleActivityReceiver(true);
    }

    public void onPause(Activity activity) {
        this.mActivity = activity;
        handleActivityReceiver(false);
    }

    public void onTopResumedActivityChanged(Activity activity, boolean isTopResumedActivity) {
        this.mActivity = activity;
        handleTopActivityChanged(activity, isTopResumedActivity);
    }

    public void onWindowFocusChanged(Activity activity, boolean hasFocus) {
        this.mActivity = activity;
        this.mHasFocus = hasFocus;
    }

    public void onAttachedToWindow() {
        setRendererEventListener();
    }

    public void onDetachedFromWindow() {
    }

    public void setRendererEventListener() {
        Activity activity = this.mActivity;
        if (activity == null || activity.getWindow() == null) {
            return;
        }
        View decor = this.mActivity.getWindow().getDecorView();
        ViewRootImpl root = decor != null ? decor.getViewRootImpl() : null;
        if (root == null) {
            return;
        }
        root.setRendererEventListener(new ViewRootImpl.OnRendererEventListener() { // from class: com.xiaopeng.app.-$$Lambda$xpActivityProxy$0roZ2NTxbWUXdnAQDgH90Ru0Wgw
            @Override // android.view.ViewRootImpl.OnRendererEventListener
            public final void onEventChanged(int i, String str) {
                xpActivityProxy.this.lambda$setRendererEventListener$1$xpActivityProxy(i, str);
            }
        });
    }

    public /* synthetic */ void lambda$setRendererEventListener$1$xpActivityProxy(int event, String action) {
        xpLogger.i(TAG, "onRendererEventListener event=" + event + " action=" + action);
        if (((action.hashCode() == -554401882 && action.equals("relaunch")) ? (char) 0 : (char) 65535) == 0) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.xiaopeng.app.-$$Lambda$xpActivityProxy$P3C7iunfjseqVDQQGaVMCMs4yls
                @Override // java.lang.Runnable
                public final void run() {
                    xpActivityProxy.this.lambda$setRendererEventListener$0$xpActivityProxy();
                }
            });
        }
    }

    public /* synthetic */ void lambda$setRendererEventListener$0$xpActivityProxy() {
        Activity activity = this.mActivity;
        if (activity == null || !activity.isResumed()) {
            return;
        }
        this.mActivity.recreate();
    }

    private void handleTopActivityChanged(Activity activity, boolean isTopResumedActivity) {
        if (activity != null && isTopResumedActivity && !this.mHasFocus && SharedDisplayManager.isUnityUI()) {
            try {
                Configuration configuration = activity.getResources().getConfiguration();
                WindowConfiguration windowConfiguration = configuration.windowConfiguration;
                boolean isHome = windowConfiguration.getActivityType() == 2;
                if (isHome) {
                    xpActivityManager.getActivityManager().setFocusedAppNoChecked(activity.getTaskId());
                    xpLogger.i(TAG, "handleTopActivityChanged setFocusedTask");
                }
            } catch (Exception e) {
            }
        }
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
                activity.registerReceiver(this.mActivityReceiver, filter);
            } else {
                activity.unregisterReceiver(this.mActivityReceiver);
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
                Log.i(TAG, "relayoutRootView v=" + v.toString() + " attr=" + lp.toString());
            }
        }
    }

    private WindowManager.LayoutParams getOverrideLayoutParams(WindowManager.LayoutParams lp, boolean fullscreen) {
        if (lp == null) {
            return null;
        }
        Rect rect = null;
        try {
            rect = fullscreen ? xpWindowManager.getDisplayBounds() : xpWindowManager.getWindowManager().getXuiRectByType(1);
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
                systemUiVisibility = systemUiVisibility2 & TrafficStats.TAG_NETWORK_STACK_RANGE_END & (-513) & (-1025) & (-3) & (-5) & (-4097);
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
