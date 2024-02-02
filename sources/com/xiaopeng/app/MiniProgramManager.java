package com.xiaopeng.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.IActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.view.IWindowManager;
import com.xiaopeng.app.xpActivityManager;
import com.xiaopeng.view.xpWindowManager;
/* loaded from: classes3.dex */
public class MiniProgramManager {
    public static final String[] MINI_PROGRAM_COMPONENTS = ActivityInfoManager.FREEFORM_PACKAGES;
    private static final String TAG = "MiniProgramManager";

    public static final xpActivityManager.ActivityRecordInfo getMiniProgramRecord(Intent intent, Bundle options) {
        ActivityOptions activityOptions = ActivityOptions.makeBasic();
        activityOptions.setLaunchWindowingMode(5);
        Rect rect = getMiniProgramWindowRect();
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
        Rect rect = getMiniProgramWindowRect();
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

    public static final Rect getMiniProgramWindowRect() {
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
        String[] strArr;
        if (owner != null) {
            try {
                String packageName = owner.getPackageName();
                for (String component : MINI_PROGRAM_COMPONENTS) {
                    if (component.startsWith(packageName)) {
                        return true;
                    }
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    public static final boolean isMiniProgramPackage(String packageName) {
        String[] strArr;
        if (!TextUtils.isEmpty(packageName)) {
            for (String item : MINI_PROGRAM_COMPONENTS) {
                if (item.startsWith(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
