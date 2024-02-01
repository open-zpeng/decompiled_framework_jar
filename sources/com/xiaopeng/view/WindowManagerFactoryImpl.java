package com.xiaopeng.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.view.WindowManager;
import com.xiaopeng.app.xpDialogInfo;
import java.util.List;
/* loaded from: classes3.dex */
public class WindowManagerFactoryImpl extends WindowManagerFactory {
    public WindowManagerFactoryImpl(Context context) {
        super(context);
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public Configuration getXuiConfiguration(String packageName) {
        try {
            return this.mWindowManager.getXuiConfiguration(packageName);
        } catch (Exception e) {
            return null;
        }
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public WindowManager.LayoutParams getXuiLayoutParams(WindowManager.LayoutParams attrs) {
        try {
            return this.mWindowManager.getXuiLayoutParams(attrs);
        } catch (Exception e) {
            return null;
        }
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public boolean isImeLayerExist() {
        try {
            return this.mWindowManager.isImeLayerExist();
        } catch (Exception e) {
            return false;
        }
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public Rect getXuiRectByType(int type) {
        try {
            return this.mWindowManager.getXuiRectByType(type);
        } catch (Exception e) {
            return new Rect();
        }
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public int getXuiStyle() {
        try {
            return this.mWindowManager.getXuiStyle();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public int getImmPosition() {
        try {
            return this.mWindowManager.getImmPosition();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public int getXuiLayer(int type) {
        try {
            return this.mWindowManager.getXuiLayer(type);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public int getXuiSubLayer(int type) {
        try {
            return this.mWindowManager.getXuiSubLayer(type);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public int[] getXuiRoundCorner(int type) {
        try {
            return this.mWindowManager.getXuiRoundCorner(type);
        } catch (Exception e) {
            return new int[0];
        }
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public void setFocusedAppNoChecked(IBinder token, boolean moveFocusNow) {
        try {
            this.mWindowManager.setFocusedAppNoChecked(token, moveFocusNow);
        } catch (Exception e) {
        }
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public int getAppTaskId(IBinder token) {
        try {
            return this.mWindowManager.getAppTaskId(token);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public Rect getDisplayBounds() {
        return null;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public WindowFrameModel getWindowFrame(WindowManager.LayoutParams lp) {
        return null;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public int getScreenId(String packageName) {
        return 0;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public void setScreenId(String packageName, int screenId) {
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public int getSharedId(String packageName) {
        return 0;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public void setSharedId(String packageName, int sharedId) {
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public List<String> getSharedPackages() {
        return null;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public List<String> getFilterPackages(int sharedId) {
        return null;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public void setSharedEvent(int event, int sharedId, String extras) {
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public void registerSharedListener(ISharedDisplayListener listener) {
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public void unregisterSharedListener(ISharedDisplayListener listener) {
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public Rect getActivityBounds(String packageName, boolean fullscreen) {
        return null;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public String getTopActivity(int type, int id) {
        return null;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public String getTopWindow() {
        return null;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public void handleSwipeEvent(int direction, String property) {
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public boolean isSharedScreenEnabled(int screenId) {
        return false;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public boolean isSharedPackageEnabled(String packageName) {
        return false;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public void setSharedScreenPolicy(int screenId, int policy) {
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public void setSharedPackagePolicy(String packageName, int policy) {
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public xpDialogInfo getTopDialog(Bundle extras) {
        return null;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public boolean dismissDialog(Bundle extras) {
        return false;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public void setModeEvent(int sharedId, int mode, String extra) {
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public void setPackageSettings(String packageName, Bundle extras) {
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public int getAppPolicy(Bundle extras) {
        return 0;
    }

    @Override // com.xiaopeng.view.WindowManagerFactory
    public Bitmap screenshot(int screenId, Bundle extras) {
        try {
            return this.mWindowManager.screenshot(screenId, extras);
        } catch (Exception e) {
            return null;
        }
    }
}
