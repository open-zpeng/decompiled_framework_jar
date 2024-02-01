package com.xiaopeng.app;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.app.IActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.service.quicksettings.TileService;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.util.xpLogger;
import com.xiaopeng.view.xpWindowManager;
import java.util.List;
/* loaded from: classes3.dex */
public class xpActivityManager {
    private static final boolean DEBUG = false;
    private static final String TAG = "xpActivityManager";

    /* loaded from: classes3.dex */
    public static final class ActivityRecordInfo {
        public Intent intent;
        public Bundle options;
    }

    public static ActivityRecordInfo getOverrideActivityRecord(Intent intent, Bundle options) {
        if (intent != null) {
            boolean isFreeformActivity = ActivityInfoManager.isFreeformActivity(xpActivityInfo.create(intent));
            if (isFreeformActivity) {
                return MiniProgramManager.getMiniProgramRecord(intent, options);
            }
            return null;
        }
        return null;
    }

    public static final Configuration getOverrideConfiguration(Configuration configuration) {
        int windowingMode = configuration.windowConfiguration.getWindowingMode();
        if (windowingMode == 5) {
            return MiniProgramManager.getMiniProgramConfiguration(configuration);
        }
        return configuration;
    }

    public static final Configuration getOverrideConfiguration(Configuration configuration, String packageName) {
        boolean isMiniProgram = MiniProgramManager.isMiniProgramPackage(packageName);
        if (isMiniProgram) {
            return MiniProgramManager.getMiniProgramConfiguration(configuration);
        }
        return configuration;
    }

    public static final boolean shouldVisibleWhenPaused(int pausingWindow, int resumingFlags) {
        if (pausingWindow != 5) {
            return false;
        }
        boolean isPanel = (resumingFlags & 256) == 256;
        boolean isDialog = (resumingFlags & 128) == 128;
        boolean isFreeform = (resumingFlags & 512) == 512;
        return isPanel || isDialog || isFreeform;
    }

    public static final int getActivityWindowType(ActivityThread.ActivityClientRecord r) {
        if (r == null || r.getWindow() == null) {
            return 0;
        }
        Window window = r.getWindow();
        WindowManager.LayoutParams lp = window != null ? window.getAttributes() : null;
        if (lp != null) {
            return lp.type;
        }
        return 0;
    }

    public static final boolean isHomeActivity(ActivityThread.ActivityClientRecord r) {
        return 5 == getActivityWindowType(r);
    }

    public static boolean isSystemApplication(String packageName) {
        return ActivityInfoManager.isSystemApplication(packageName);
    }

    public static final void finishMiniProgramIfNeed(ActivityThread.ActivityClientRecord r, Handler handler) {
        final IActivityManager am = getActivityManager();
        final Window window = r != null ? r.getWindow() : null;
        if (am != null && r != null && window != null && handler != null) {
            Runnable runnable = new Runnable() { // from class: com.xiaopeng.app.xpActivityManager.1
                @Override // java.lang.Runnable
                public void run() {
                    MiniProgramManager.finishMiniProgramByWindowType(IActivityManager.this, window.getAttributes().type);
                }
            };
            handler.post(runnable);
        }
    }

    public static final void handleActivityChanged(ActivityThread.ActivityClientRecord r) {
        IActivityManager am = getActivityManager();
        if (am != null && r != null) {
            ActivityInfo info = r.getActivityInfo();
            Window window = r.getWindow();
            Intent intent = r.getIntent();
            if (info != null && window != null && intent != null) {
                WindowManager.LayoutParams lp = xpWindowManager.getOverrideLayoutParams(window.getAttributes());
                boolean dimEnabled = (lp.flags & 2) == 2;
                int systemUiVisibility = lp.subtreeSystemUiVisibility | lp.systemUiVisibility;
                ComponentName component = info.targetActivity != null ? ComponentName.createRelative(info.packageName, info.targetActivity) : info.getComponentName();
                String data = intent.getDataString();
                int windowType = lp.type;
                int flags = ActivityInfoManager.getActivityFlags(intent);
                boolean fullscreen = xpWindowManager.isFullscreen(systemUiVisibility, lp.flags, lp.xpFlags);
                float dimAmount = dimEnabled ? lp.dimAmount : 0.0f;
                Bundle bundle = new Bundle();
                bundle.putString("data", data);
                bundle.putInt(Context.WINDOW_SERVICE, windowType);
                bundle.putInt("flags", flags);
                bundle.putString(CardEmulation.EXTRA_SERVICE_COMPONENT, component.flattenToString());
                bundle.putBoolean("fullscreen", fullscreen);
                bundle.putFloat("dimAmount", dimAmount);
                bundle.putBinder(TileService.EXTRA_TOKEN, r.token);
                try {
                    am.handleActivityChanged(bundle);
                } catch (Exception e) {
                }
            }
        }
    }

    public static final String getProcessName(Context context, int pid) {
        if (context != null && pid > 0) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps == null) {
                return null;
            }
            for (ActivityManager.RunningAppProcessInfo info : runningApps) {
                if (info != null && info.pid == pid) {
                    return info.processName;
                }
            }
        }
        return null;
    }

    public static boolean isComponentValid(ComponentName component) {
        return (component == null || TextUtils.isEmpty(component.getPackageName()) || TextUtils.isEmpty(component.getClassName())) ? false : true;
    }

    public static boolean isComponentEqual(ComponentName a, ComponentName b) {
        return isComponentValid(a) && isComponentValid(b) && TextUtils.equals(a.getPackageName(), b.getPackageName()) && TextUtils.equals(a.getClassName(), b.getClassName());
    }

    public static boolean hasPermission(String permission, String packageName) {
        try {
            IPackageManager pm = xpPackageManager.getPackageManager();
            return pm.checkPermission(permission, packageName, UserHandle.myUserId()) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDirectBootAware(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            if (ai == null || !ai.isSystemApp()) {
                return false;
            }
            return ai.isDirectBootAware();
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isHome(Context context, ComponentName component) {
        return ActivityInfoManager.isHome(context, xpActivityInfo.create(component));
    }

    public static void startHome(Context context) {
        xpLogger.i(TAG, "startHome");
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(270532608);
            context.startActivityAsUser(intent, UserHandle.CURRENT_OR_SELF);
        } catch (Exception e) {
            xpLogger.i(TAG, "startHome e=" + e);
        }
    }

    public static void zoomTopActivity(Context context) {
        if (!FeatureOption.FO_ACTIVITY_ZOOM_ENABLED || context == null) {
            return;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            String topActivity = tasks.get(0).topActivity.getPackageName();
            try {
                xpPackageInfo pi = AppGlobals.getPackageManager().getXpPackageInfo(topActivity);
                if (pi != null && pi.screenAdaption == 1) {
                    Intent intent = new Intent("com.xiaopeng.intent.action.ACTIVITY_SIZE_CHANGED");
                    intent.addFlags(16777216);
                    intent.putExtra("fullscreen", 1 ^ (xpPackageInfo.isFullscreen(pi) ? 1 : 0));
                    context.sendBroadcast(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void stopTopActivity(Context context) {
        ActivityManager am;
        List<ActivityManager.RunningTaskInfo> tasks;
        if (FeatureOption.FO_PACKAGE_FORCE_STOP_ENABLED && context != null && (tasks = (am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1)) != null && !tasks.isEmpty()) {
            String topActivity = tasks.get(0).topActivity.getPackageName();
            try {
                xpPackageInfo pi = AppGlobals.getPackageManager().getXpPackageInfo(topActivity);
                if (pi != null) {
                    am.forceStopPackage(topActivity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean shouldGrantUsbPermission(String packageName) {
        xpPackageInfo info = xpPackageManager.getOverridePackageInfo(packageName);
        if (info == null || (info.permissionGrant & 2) != 2) {
            return false;
        }
        return true;
    }

    public static IActivityManager getActivityManager() {
        IBinder b = ServiceManager.getService(Context.ACTIVITY_SERVICE);
        return IActivityManager.Stub.asInterface(b);
    }
}
