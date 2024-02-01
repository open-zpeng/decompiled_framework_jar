package com.xiaopeng.view;

import android.app.ActivityOptions;
import android.app.WindowConfiguration;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.view.IWindowManager;
import android.view.MotionEvent;
import android.view.WindowManager;
import com.xiaopeng.app.ActivityInfoManager;
import com.xiaopeng.app.xpActivityInfo;
import com.xiaopeng.app.xpActivityManager;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.util.xpLogger;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class SharedDisplayManager {
    public static final int FLAG_PACKAGE_NO_LIMIT = 1024;
    public static final int FLAG_PACKAGE_NO_SLIDE = 512;
    public static final int ID_SCREEN_PRIMARY = 0;
    public static final int ID_SCREEN_SECONDARY = 1;
    public static final int ID_SHARED_PRIMARY = 0;
    public static final int ID_SHARED_SECONDARY = 1;
    public static final int ID_SHARED_UNKNOWN = -1;
    public static final int POLICY_BLOCKED = 2;
    public static final int POLICY_DISABLE = 0;
    public static final int POLICY_ENABLED = 1;
    public static final int POLICY_UNKNOWN = -1;
    public static final String PROP_TASK_RESIZING = "xui.sys.shared.display.task.resizing";
    public static final boolean SHARED_DISPLAY_ENABLED;
    private static final String TAG = "SharedWindowManager";
    public static final int TYPE_SCREEN_ID = 0;
    public static final int TYPE_SHARED_ID = 1;
    public static final boolean WINDOW_DECOR_CAPTION_ENABLE = false;

    static {
        SHARED_DISPLAY_ENABLED = SystemProperties.getInt("persist.sys.xp.shared_display.enable", 0) == 1;
    }

    public static boolean enable() {
        return SHARED_DISPLAY_ENABLED;
    }

    public static boolean packageEnabled(String packageName) {
        try {
            return xpWindowManager.getWindowManager().isSharedPackageEnabled(packageName);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean preventFreeformOverlapStable() {
        return !enable();
    }

    public static boolean preventActivityStarted(String packageName) {
        JSONArray array;
        int length;
        if (enable()) {
            boolean system = ActivityInfoManager.isSystemApplication(packageName);
            if (system) {
                return false;
            }
            try {
                try {
                    int sharedId = xpWindowManager.getWindowManager().getSharedId(packageName);
                    int screenId = findScreenId(sharedId);
                    String top = xpWindowManager.getWindowManager().getTopWindow();
                    if (!TextUtils.isEmpty(top) && (length = (array = new JSONArray(top)).length()) > 0) {
                        for (int i = 0; i < length; i++) {
                            JSONObject object = array.getJSONObject(i);
                            if (object != null) {
                                int mode = object.has("mode") ? object.getInt("mode") : -1;
                                int _sharedId = object.has("id") ? object.getInt("id") : -1;
                                int _screenId = findScreenId(_sharedId);
                                if (_sharedId != -1) {
                                    if ((_sharedId == 0 || _sharedId == 1) && mode >= 0 && _screenId == screenId) {
                                        return true;
                                    }
                                } else if (mode >= 0) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }
            } catch (Exception e2) {
                return false;
            }
        }
        return false;
    }

    public static boolean sharedValid(int sharedId) {
        if (enable()) {
            return sharedId == 0 || sharedId == 1;
        }
        return false;
    }

    public static boolean screenValid(int screenId) {
        if (screenId == 0 || screenId == 1) {
            return true;
        }
        return false;
    }

    public static int generateNextId(int type, int id) {
        if (type != 0) {
            if (type != 1) {
                return -1;
            }
            if (id != 0) {
                if (id != 1) {
                    return -1;
                }
                return 0;
            }
            return 1;
        } else if (id != 0) {
            if (id != 1) {
                return -1;
            }
            return 0;
        } else {
            return 1;
        }
    }

    public static boolean isPrimaryId(int sharedId) {
        return sharedId == -1 || sharedId == 0 || sharedId != 1;
    }

    public static boolean isSharedApplication(xpActivityInfo ai) {
        if (ai != null) {
            int sharedId = getOverrideSharedId(SharedParams.create(ai.packageName, ai.intent, null));
            return sharedValid(sharedId);
        }
        return false;
    }

    public static boolean hasWindowDecorCaption(WindowConfiguration configuration, WindowManager.LayoutParams lp) {
        return false;
    }

    public static int getLaunchSharedId(Context context, ActivityInfo activity, Intent intent, ApplicationInfo caller) {
        if (!enable() || context == null || activity == null || intent == null || caller == null) {
            return -1;
        }
        String packageName = activity.packageName;
        ComponentName component = activity.getComponentName();
        if (TextUtils.isEmpty(packageName) || component == null) {
            return -1;
        }
        boolean packageEnabled = packageEnabled(packageName);
        if (packageEnabled) {
            int sharedId = -1;
            try {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                int requestId = intent.getSharedId();
                int callingId = wm.getSharedId(caller.packageName);
                if (sharedValid(requestId)) {
                    sharedId = requestId;
                } else {
                    int callingScreenId = findScreenId(callingId);
                    if (callingScreenId == 0) {
                        sharedId = 0;
                    } else if (callingScreenId == 1) {
                        sharedId = 1;
                    }
                }
                if (!sharedValid(sharedId)) {
                    return 0;
                }
                return sharedId;
            } catch (Exception e) {
                return sharedId;
            }
        }
        return -1;
    }

    public static int getOverrideSharedId(SharedParams sp) {
        if (enable() && sp != null) {
            int sharedId = SharedParams.getSharedIdFromLayoutParams(sp);
            if (sharedValid(sharedId)) {
                return sharedId;
            }
            boolean packageEnabled = packageEnabled(sp.packageName);
            if (packageEnabled) {
                int sharedId2 = SharedParams.getSharedIdFromIntent(sp);
                if (sharedValid(sharedId2)) {
                    return sharedId2;
                }
                int sharedId3 = SharedParams.getSharedIdFromWindowManager(sp);
                if (sharedValid(sharedId3)) {
                    return sharedId3;
                }
                return -1;
            }
            return -1;
        }
        return -1;
    }

    public static WindowManager.LayoutParams createLayoutParams(String packageName, int sharedId, int activityFlags) {
        if (sharedValid(sharedId)) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.type = sharedValid(sharedId) ? 10 : 1;
            lp.sharedId = sharedId;
            lp.packageName = packageName;
            lp.intentFlags = activityFlags;
            return lp;
        }
        return null;
    }

    public static final xpActivityManager.ActivityRecordInfo getSharedWindowRecord(xpActivityInfo ai, Bundle options) {
        WindowFrameModel model;
        if (ai == null || ai.intent == null) {
            return null;
        }
        int sharedId = getOverrideSharedId(SharedParams.create(ai.packageName, ai.intent, null));
        if (!sharedValid(sharedId)) {
            return null;
        }
        ActivityOptions activityOptions = ActivityOptions.makeBasic();
        activityOptions.setLaunchWindowingMode(5);
        int activityFlags = ActivityInfoManager.getActivityFlags(ai.intent);
        WindowManager.LayoutParams lp = createLayoutParams(ai.packageName, sharedId, activityFlags);
        if (lp != null && (model = getWindowFrame(lp)) != null && model.contentBounds != null) {
            Rect bounds = new Rect(model.contentBounds);
            xpLogger.i(TAG, "getSharedWindowRecord bounds=" + bounds);
            activityOptions.setLaunchBounds(bounds);
        }
        if (options != null) {
            options.putAll(activityOptions.toBundle());
        } else {
            options = activityOptions.toBundle();
        }
        xpActivityManager.ActivityRecordInfo ari = new xpActivityManager.ActivityRecordInfo();
        ari.intent = ai.intent;
        ari.options = options;
        return ari;
    }

    public static final Configuration getSharedConfiguration(Configuration configuration, WindowManager.LayoutParams lp) {
        if (configuration != null) {
            Rect bounds = null;
            WindowFrameModel model = getWindowFrame(lp);
            if (model != null && model.contentBounds != null) {
                bounds = new Rect(model.contentBounds);
            }
            if (bounds != null) {
                configuration.windowConfiguration.setWindowingMode(5);
                configuration.windowConfiguration.setBounds(bounds);
                configuration.windowConfiguration.setAppBounds(bounds);
                configuration.smallestScreenWidthDp = bounds.width();
                configuration.screenWidthDp = bounds.width();
                configuration.screenHeightDp = bounds.height();
                configuration.orientation = SystemProperties.getInt("persist.shared.window.orientation", 1);
                configuration.densityDpi = SystemProperties.getInt("persist.shared.window.dpi", 160);
                configuration.screenLayout = SystemProperties.getInt("persist.shared.window.layout", 1);
                configuration.compatScreenWidthDp = bounds.width();
                configuration.compatScreenHeightDp = bounds.height();
                configuration.fontScale = (float) SystemProperties.getLong("persist.shared.window.font", 1L);
            }
        }
        return configuration;
    }

    public static WindowFrameModel getWindowFrame(WindowManager.LayoutParams lp) {
        try {
            IWindowManager wm = xpWindowManager.getWindowManager();
            return wm.getWindowFrame(lp);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isUnityUI() {
        return FeatureOption.FO_PROJECT_UI_TYPE == 2;
    }

    public static void enableScreenIfNeed(Context context, int screenId) {
        if (context != null && screenId == 1) {
            try {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                boolean isScreenOn = pm.isScreenOn(PowerManager.DEVICE_PASSENGER);
                if (!isScreenOn) {
                    pm.setXpScreenOn(PowerManager.DEVICE_PASSENGER, SystemClock.uptimeMillis());
                }
            } catch (Exception e) {
            }
        }
    }

    public static boolean skipConfigurationChanged(Configuration config, Configuration newConfig) {
        if (!enable() || config == null || newConfig == null) {
            return false;
        }
        int changes = config.diff(newConfig);
        boolean resizing = WifiEnterpriseConfig.ENGINE_ENABLE.equals(SystemProperties.get(PROP_TASK_RESIZING, WifiEnterpriseConfig.ENGINE_DISABLE));
        if (!resizing || changes <= 0) {
            return false;
        }
        if ((changes & 536870912) != 536870912 && (changes & 1024) != 1024 && (changes & 2048) != 2048 && (changes & 4096) != 4096 && (changes & 256) != 256) {
            return false;
        }
        return true;
    }

    public static int findScreenId(int sharedId) {
        return (sharedId == -1 || sharedId == 0 || sharedId != 1) ? 0 : 1;
    }

    public static int findScreenId(MotionEvent event) {
        return findScreenId(0, event);
    }

    public static int findScreenId(int index, MotionEvent event) {
        if (enable() && event != null) {
            int count = event.getPointerCount();
            if (index < 0 || index >= count) {
                return 0;
            }
            float x = event.getRawX(index);
            Rect bounds = xpWindowManager.getDisplayBounds();
            if (bounds != null && !bounds.isEmpty() && x >= bounds.width() / 2) {
                return 1;
            }
        }
        return 0;
    }

    public static int findScreenId(Rect bounds, int logicalWidth, int logicalHeight) {
        if (enable() && bounds != null) {
            Rect primary = new Rect(0, 0, logicalWidth / 2, logicalHeight);
            Rect secondary = new Rect(logicalWidth / 2, 0, logicalWidth, logicalHeight);
            if (!primary.contains(bounds) && secondary.contains(bounds)) {
                return 1;
            }
        }
        return 0;
    }

    public static int findSharedId(Rect bounds, int windowingMode) {
        if (windowingMode == 5 && bounds != null && !bounds.isEmpty()) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.type = 10;
            lp.sharedId = 0;
            WindowFrameModel model = getWindowFrame(lp);
            Rect primary = model != null ? model.displayBounds : null;
            if (primary != null && !primary.isEmpty() && primary.contains(bounds)) {
                return 0;
            }
            lp.sharedId = 1;
            WindowFrameModel model2 = getWindowFrame(lp);
            Rect secondary = model2 != null ? model2.displayBounds : null;
            if (secondary != null && !secondary.isEmpty() && secondary.contains(bounds)) {
                return 1;
            }
            return -1;
        }
        return -1;
    }

    public static boolean isDefaultDisplay(int displayId) {
        return displayId == 0 || displayId == -1;
    }

    public static boolean isPhysicalActivity(int privateFlags) {
        return (privateFlags & 32) == 32;
    }

    public static boolean hasDynamicScreenFlags(WindowManager.LayoutParams lp) {
        if (lp == null) {
            return false;
        }
        boolean hasPrimaryFlag = (lp.xpFlags & 16) == 16;
        boolean hasSecondaryFlag = (lp.xpFlags & 32) == 32;
        return hasPrimaryFlag || hasSecondaryFlag;
    }

    public static boolean isFactoryMode() {
        return SystemProperties.getBoolean("persist.sys.xiaopeng.factory_mode", false);
    }

    /* loaded from: classes3.dex */
    public static final class SharedParams {
        public Intent intent;
        public WindowManager.LayoutParams lp;
        public String packageName;

        public static SharedParams create(String packageName, Intent intent, WindowManager.LayoutParams lp) {
            SharedParams sp = new SharedParams();
            sp.packageName = packageName;
            sp.intent = intent;
            sp.lp = lp;
            return sp;
        }

        public static SharedParams create(String packageName, Intent intent) {
            return create(packageName, intent, null);
        }

        public static SharedParams create(String packageName, WindowManager.LayoutParams lp) {
            return create(packageName, null, lp);
        }

        public static int getSharedIdFromIntent(SharedParams sp) {
            Intent intent;
            if (sp == null || (intent = sp.intent) == null) {
                return -1;
            }
            try {
                int sharedId = intent.getSharedId();
                return sharedId;
            } catch (Exception e) {
                return -1;
            }
        }

        public static int getSharedIdFromLayoutParams(SharedParams sp) {
            WindowManager.LayoutParams layoutParams;
            if (sp == null || (layoutParams = sp.lp) == null || layoutParams.xpFlags <= 0) {
                return -1;
            }
            boolean primary = (sp.lp.xpFlags & 16) == 16;
            boolean secondary = (sp.lp.xpFlags & 32) == 32;
            if (primary) {
                return 0;
            }
            if (!secondary) {
                return -1;
            }
            return 1;
        }

        public static int getSharedIdFromWindowManager(SharedParams sp) {
            if (sp == null || TextUtils.isEmpty(sp.packageName)) {
                return -1;
            }
            try {
                IWindowManager wm = xpWindowManager.getWindowManager();
                if (wm != null) {
                    int sharedId = wm.getSharedId(sp.packageName);
                    return sharedId;
                }
                return -1;
            } catch (Exception e) {
                return -1;
            }
        }
    }
}
