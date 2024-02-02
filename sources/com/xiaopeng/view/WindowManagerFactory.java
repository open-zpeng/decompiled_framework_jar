package com.xiaopeng.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.view.IWindowManager;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import com.xiaopeng.app.xpDialogInfo;
import java.util.List;
/* loaded from: classes3.dex */
public abstract class WindowManagerFactory {
    public static final int EVENT_DIALOG_DISMISS = 102;
    public static final int EVENT_IN_TRANSACT = 1001;
    public static final int EVENT_OUT_TRANSACT = 1002;
    public static final int EVENT_POLICY_CHANGED = 120;
    public static final int EVENT_SHARED_CHANGED = 4;
    public static final int EVENT_SHARED_DESKTOP = 3;
    public static final int EVENT_SHARED_GESTURE = 5;
    public static final int EVENT_SHARED_METHODS = 8;
    public static final int EVENT_SHARED_SLIDING = 0;
    public static final int EVENT_SHARED_STARTED = 1;
    public static final int EVENT_SHARED_STOPPED = 2;
    public static final int EVENT_SHARED_TOUCHED = 7;
    public static final int EVENT_SHARED_VIRTUAL = 6;
    public static final int EVENT_WAKEUP_ACQUIRE = 200;
    public static final int EVENT_WAKEUP_RELEASE = 201;
    public static final int EVENT_WINDOW_CHANGED = 100;
    public static final int EVENT_WINDOW_DISMISS = 101;
    public static final int FIRST_SYSTEM_WINDOW = 2000;
    public static final int ID_SCREEN_PRIMARY = 0;
    public static final int ID_SCREEN_SECONDARY = 1;
    public static final int ID_SHARED_PRIMARY = 0;
    public static final int ID_SHARED_SECONDARY = 1;
    public static final int ID_SHARED_UNKNOWN = -1;
    public static final int POLICY_BLOCKED = 2;
    public static final int POLICY_DISABLE = 0;
    public static final int POLICY_ENABLED = 1;
    public static final int POLICY_FAIL = 1;
    public static final int POLICY_PASS = 0;
    public static final int POLICY_UNKNOWN = -1;
    public static final int TYPE_AI_ASSISTANT = 2040;
    public static final int TYPE_APPLICATION_WINDOW_OVERLAY = 2044;
    public static final int TYPE_AVATAR = 2051;
    public static final int TYPE_DEVICE_ID = -1;
    public static final int TYPE_INFOFLOW = 2039;
    public static final int TYPE_INFORMATION_BAR = 2046;
    public static final int TYPE_LIFECYCLE_DIALOG = 2048;
    public static final int TYPE_OSD = 2043;
    public static final int TYPE_QUICK_PANEL = 2053;
    public static final int TYPE_SCREEN_ID = 0;
    public static final int TYPE_SECONDARY_NAVIGATION = 2060;
    public static final int TYPE_SECONDARY_SCREENSHOT = 2061;
    public static final int TYPE_SHARED_ID = 1;
    public static final int TYPE_SPECTRUM = 2041;
    public static final int TYPE_VUI = 2049;
    public static final int TYPE_VUI_OVERLAY = 2052;
    public static final int TYPE_WARNING_DIALOG = 2047;
    public static final int TYPE_WATER_MARK = 2045;
    public static final int TYPE_WINDOW_OVERLAY = 2042;
    public static final int TYPE_XUI_WALLPAPER = 2050;
    protected IWindowManager mWindowManager = WindowManagerGlobal.getWindowManagerService();

    public abstract boolean dismissDialog(Bundle bundle);

    public abstract Rect getActivityBounds(String str, boolean z);

    public abstract int getAppPolicy(Bundle bundle);

    public abstract int getAppTaskId(IBinder iBinder);

    public abstract Rect getDisplayBounds();

    public abstract List<String> getFilterPackages(int i);

    public abstract int getImmPosition();

    public abstract int getScreenId(String str);

    public abstract int getSharedId(String str);

    public abstract List<String> getSharedPackages();

    public abstract String getTopActivity(int i, int i2);

    public abstract xpDialogInfo getTopDialog(Bundle bundle);

    public abstract String getTopWindow();

    public abstract WindowFrameModel getWindowFrame(WindowManager.LayoutParams layoutParams);

    public abstract Configuration getXuiConfiguration(String str);

    public abstract int getXuiLayer(int i);

    public abstract WindowManager.LayoutParams getXuiLayoutParams(WindowManager.LayoutParams layoutParams);

    public abstract Rect getXuiRectByType(int i);

    public abstract int[] getXuiRoundCorner(int i);

    public abstract int getXuiStyle();

    public abstract int getXuiSubLayer(int i);

    public abstract void handleSwipeEvent(int i, String str);

    public abstract boolean isImeLayerExist();

    public abstract boolean isSharedPackageEnabled(String str);

    public abstract boolean isSharedScreenEnabled(int i);

    public abstract void registerSharedListener(ISharedDisplayListener iSharedDisplayListener);

    public abstract Bitmap screenshot(int i, Bundle bundle);

    public abstract void setFocusedAppNoChecked(IBinder iBinder, boolean z);

    public abstract void setModeEvent(int i, int i2, String str);

    public abstract void setPackageSettings(String str, Bundle bundle);

    public abstract void setScreenId(String str, int i);

    public abstract void setSharedEvent(int i, int i2, String str);

    public abstract void setSharedId(String str, int i);

    public abstract void setSharedPackagePolicy(String str, int i);

    public abstract void setSharedScreenPolicy(int i, int i2);

    public abstract void unregisterSharedListener(ISharedDisplayListener iSharedDisplayListener);

    public static WindowManagerFactory create(Context context) {
        return new WindowManagerFactoryImpl(context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WindowManagerFactory(Context context) {
    }

    public static boolean isPrimaryId(int sharedId) {
        return true;
    }
}
