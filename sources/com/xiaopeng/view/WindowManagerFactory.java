package com.xiaopeng.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import com.xiaopeng.app.xpDialogInfo;
import java.util.List;

/* loaded from: classes3.dex */
public class WindowManagerFactory {
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
    public static final int TYPE_DEVICE_ID = -1;
    public static final int TYPE_SCREEN_ID = 0;
    public static final int TYPE_SHARED_ID = 1;
    private WindowManager mWindowManager;

    public static WindowManagerFactory create(Context context) {
        return new WindowManagerFactory(context);
    }

    private WindowManagerFactory(Context context) {
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public static boolean isPrimaryId(int sharedId) {
        return WindowManager.isPrimaryId(sharedId);
    }

    public static int findScreenId(int sharedId) {
        return WindowManager.findScreenId(sharedId);
    }

    public static int findScreenId(int index, MotionEvent event) {
        return WindowManager.findScreenId(index, event);
    }

    public int getScreenId(String packageName) {
        return this.mWindowManager.getScreenId(packageName);
    }

    public void setScreenId(String packageName, int screenId) {
        this.mWindowManager.setScreenId(packageName, screenId);
    }

    public int getSharedId(String packageName) {
        return this.mWindowManager.getSharedId(packageName);
    }

    public void setSharedId(String packageName, int sharedId) {
        this.mWindowManager.setSharedId(packageName, sharedId);
    }

    public List<String> getSharedPackages() {
        return this.mWindowManager.getSharedPackages();
    }

    public List<String> getFilterPackages(int sharedId) {
        return this.mWindowManager.getFilterPackages(sharedId);
    }

    public void setSharedEvent(int event) {
        this.mWindowManager.setSharedEvent(event);
    }

    public void setSharedEvent(int event, int sharedId) {
        this.mWindowManager.setSharedEvent(event, sharedId);
    }

    public void setSharedEvent(int event, int sharedId, String extras) {
        this.mWindowManager.setSharedEvent(event, sharedId, extras);
    }

    public Rect getActivityBounds(String packageName, boolean fullscreen) {
        return this.mWindowManager.getActivityBounds(packageName, fullscreen);
    }

    public String getTopActivity(int type, int id) {
        return this.mWindowManager.getTopActivity(type, id);
    }

    public String getTopWindow() {
        return this.mWindowManager.getTopWindow();
    }

    public void setModeEvent(int sharedId, int mode, String extra) {
        this.mWindowManager.setModeEvent(sharedId, mode, extra);
    }

    public void setPackageSettings(String packageName, Bundle extras) {
        this.mWindowManager.setPackageSettings(packageName, extras);
    }

    public boolean isSharedScreenEnabled(int screenId) {
        return this.mWindowManager.isSharedScreenEnabled(screenId);
    }

    public boolean isSharedPackageEnabled(String packageName) {
        return this.mWindowManager.isSharedPackageEnabled(packageName);
    }

    public void setSharedScreenPolicy(int screenId, int policy) {
        this.mWindowManager.setSharedScreenPolicy(screenId, policy);
    }

    public void setSharedPackagePolicy(String packageName, int policy) {
        this.mWindowManager.setSharedPackagePolicy(packageName, policy);
    }

    public xpDialogInfo getTopDialog(Bundle extras) {
        return this.mWindowManager.getTopDialog(extras);
    }

    public boolean dismissDialog(Bundle extras) {
        return this.mWindowManager.dismissDialog(extras);
    }

    public int getAppPolicy(Bundle extras) {
        return this.mWindowManager.getAppPolicy(extras);
    }

    public void registerSharedListener(ISharedDisplayListener listener) {
        this.mWindowManager.registerSharedListener(listener);
    }

    public void unregisterSharedListener(ISharedDisplayListener listener) {
        this.mWindowManager.unregisterSharedListener(listener);
    }
}
