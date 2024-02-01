package com.xiaopeng.view;

import android.app.ActivityThread;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.display.DisplayManagerGlobal;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.IWindowManager;
import com.xiaopeng.app.MiniProgramManager;

/* loaded from: classes3.dex */
public class xpDisplayInfo {
    private static final String TAG = "xpDisplayInfo";

    public static void getMetricsWithSize(DisplayInfo displayInfo, DisplayMetrics outMetrics, CompatibilityInfo compatInfo, Configuration configuration, int width, int height) {
        if (displayInfo == null || outMetrics == null || compatInfo == null) {
            return;
        }
        int i = displayInfo.logicalDensityDpi;
        outMetrics.noncompatDensityDpi = i;
        outMetrics.densityDpi = i;
        float f = displayInfo.logicalDensityDpi * 0.00625f;
        outMetrics.noncompatDensity = f;
        outMetrics.density = f;
        float f2 = outMetrics.density;
        outMetrics.noncompatScaledDensity = f2;
        outMetrics.scaledDensity = f2;
        float f3 = displayInfo.physicalXDpi;
        outMetrics.noncompatXdpi = f3;
        outMetrics.xdpi = f3;
        float f4 = displayInfo.physicalYDpi;
        outMetrics.noncompatYdpi = f4;
        outMetrics.ydpi = f4;
        String packageName = ActivityThread.currentPackageName();
        boolean realMetrics = configuration == null;
        if (realMetrics) {
            boolean isSystem = TextUtils.isEmpty(packageName) || "android".equals(packageName);
            boolean isDefault = displayInfo.displayId == 0;
            boolean sharedEnabled = SharedDisplayManager.enable();
            Rect appBounds = configuration != null ? configuration.windowConfiguration.getAppBounds() : null;
            int width2 = appBounds != null ? appBounds.width() : width;
            int height2 = appBounds != null ? appBounds.height() : height;
            if (isDefault && sharedEnabled && !isSystem) {
                width2 /= 2;
            }
            outMetrics.widthPixels = width2;
            outMetrics.noncompatWidthPixels = width2;
            outMetrics.heightPixels = height2;
            outMetrics.noncompatHeightPixels = height2;
        } else {
            Configuration config = getXuiConfiguration(packageName);
            if (config != null) {
                int i2 = config.densityDpi;
                outMetrics.noncompatDensityDpi = i2;
                outMetrics.densityDpi = i2;
                float f5 = config.densityDpi * 0.00625f;
                outMetrics.noncompatDensity = f5;
                outMetrics.density = f5;
                float f6 = outMetrics.density;
                outMetrics.noncompatScaledDensity = f6;
                outMetrics.scaledDensity = f6;
                int i3 = config.screenWidthDp;
                outMetrics.widthPixels = i3;
                outMetrics.noncompatWidthPixels = i3;
                int i4 = config.screenHeightDp;
                outMetrics.heightPixels = i4;
                outMetrics.noncompatHeightPixels = i4;
            } else {
                Rect appBounds2 = getAppBounds(packageName);
                if (appBounds2 == null) {
                    appBounds2 = configuration != null ? configuration.windowConfiguration.getAppBounds() : null;
                }
                int width3 = appBounds2 != null ? appBounds2.width() : width;
                int height3 = appBounds2 != null ? appBounds2.height() : height;
                outMetrics.widthPixels = width3;
                outMetrics.noncompatWidthPixels = width3;
                outMetrics.heightPixels = height3;
                outMetrics.noncompatHeightPixels = height3;
            }
        }
        if (!compatInfo.equals(CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO)) {
            compatInfo.applyToDisplayMetrics(outMetrics);
        }
    }

    private static Configuration getXuiConfiguration(String packageName) {
        try {
            if (!TextUtils.isEmpty(packageName)) {
                IWindowManager wm = xpWindowManager.getWindowManager();
                return wm.getXuiConfiguration(packageName);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static Rect getAppBounds(String packageName) {
        int window = MiniProgramManager.isMiniProgram(packageName) ? 12 : 1;
        try {
            IWindowManager wm = xpWindowManager.getWindowManager();
            if (wm != null) {
                return wm.getXuiRectByType(window);
            }
            return null;
        } catch (Exception e) {
            Log.d(TAG, "getAppBounds exception:" + e);
            return null;
        }
    }

    public static boolean isIcmDisplay(Display display) {
        return display != null && display.getType() == 6;
    }

    public static boolean isIcmDisplay(int displayId) {
        Display display = null;
        try {
            display = DisplayManagerGlobal.getInstance().getRealDisplay(displayId);
        } catch (Exception e) {
        }
        return isIcmDisplay(display);
    }
}
