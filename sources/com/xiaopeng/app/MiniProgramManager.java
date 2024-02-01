package com.xiaopeng.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.IActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.view.IWindowManager;
import com.xiaopeng.app.xpActivityManager;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.view.xpWindowManager;

/* loaded from: classes3.dex */
public class MiniProgramManager {
    private static final String MINI_PROGRAM_COMPONENT = FeatureOption.FO_MINI_PROGRAM_PACKAGES;
    private static final String TAG = "MiniProgramManager";

    public static final xpActivityManager.ActivityRecordInfo getMiniProgramRecord(Intent intent, Bundle options) {
        ActivityOptions activityOptions = ActivityOptions.makeBasic();
        activityOptions.setLaunchWindowingMode(5);
        Rect rect = getMiniProgramBounds();
        if (rect != null) {
            int left = SystemProperties.getInt("persist.mini.program.bounds.launch.x", 0);
            int top = SystemProperties.getInt("persist.mini.program.bounds.launch.y", 0);
            int right = rect.width() + left;
            int bottom = rect.height() + top;
            activityOptions.setLaunchBounds(new Rect(left, top, right, bottom));
        }
        if (options != null) {
            options.putAll(activityOptions.toBundle());
        } else {
            options = activityOptions.toBundle();
        }
        intent.addFlags(4096);
        xpActivityManager.ActivityRecordInfo ari = new xpActivityManager.ActivityRecordInfo();
        ari.intent = intent;
        ari.options = options;
        return ari;
    }

    public static final Configuration getMiniProgramConfiguration(Configuration configuration) {
        if (configuration == null) {
            configuration = new Configuration();
        }
        Rect rect = getMiniProgramBounds();
        if (rect != null) {
            int left = SystemProperties.getInt("persist.mini.program.bounds.app.x", 0);
            int top = SystemProperties.getInt("persist.mini.program.bounds.app.y", 0);
            int right = rect.width() + left;
            int bottom = rect.height() + top;
            configuration.windowConfiguration.setWindowingMode(5);
            configuration.windowConfiguration.setBounds(new Rect(left, top, right, bottom));
            configuration.windowConfiguration.setAppBounds(left, top, right, bottom);
            configuration.smallestScreenWidthDp = rect.width();
            configuration.screenWidthDp = rect.width();
            configuration.screenHeightDp = rect.height();
            configuration.orientation = SystemProperties.getInt("persist.mini.program.orientation", 1);
            configuration.densityDpi = SystemProperties.getInt("persist.mini.program.dpi", 160);
            configuration.screenLayout = SystemProperties.getInt("persist.mini.program.layout", 1);
            configuration.compatScreenWidthDp = rect.width();
            configuration.compatScreenHeightDp = rect.height();
            configuration.fontScale = (float) SystemProperties.getLong("persist.mini.program.font", 1L);
        }
        return configuration;
    }

    public static final Rect getMiniProgramBounds() {
        try {
            IWindowManager wm = xpWindowManager.getWindowManager();
            if (wm != null) {
                return wm.getXuiRectByType(12);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static final void finishMiniProgramIfNeed(IActivityManager am) {
        try {
            am.finishMiniProgram();
        } catch (Exception e) {
        }
    }

    public static final void finishMiniProgramByWindowType(IActivityManager am, int windowType) {
        if (windowType != 5 && windowType != 12) {
            finishMiniProgramIfNeed(am);
        }
    }

    public static final boolean isMiniProgramDialog(Activity owner) {
        if (owner != null) {
            return isMiniProgram(owner.getPackageName());
        }
        return false;
    }

    public static final boolean isMiniProgram(String packageName) {
        return isMiniProgram(xpActivityInfo.create(packageName, null));
    }

    public static final boolean isMiniProgram(xpActivityInfo activityInfo) {
        if (activityInfo == null || TextUtils.isEmpty(MINI_PROGRAM_COMPONENT)) {
            return false;
        }
        String packageName = activityInfo.packageName;
        Intent intent = activityInfo.intent;
        if (intent != null) {
            if (TextUtils.equals(MINI_PROGRAM_COMPONENT, intent.getPackage())) {
                return true;
            }
            ComponentName component = intent.getComponent();
            if (TextUtils.equals(MINI_PROGRAM_COMPONENT, component != null ? component.getPackageName() : "")) {
                return true;
            }
        }
        if (!TextUtils.equals(MINI_PROGRAM_COMPONENT, packageName)) {
            return false;
        }
        return true;
    }
}
