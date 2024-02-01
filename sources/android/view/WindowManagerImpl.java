package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.os.IResultReceiver;
import com.xiaopeng.app.xpDialogInfo;
import com.xiaopeng.view.ISharedDisplayListener;
import java.util.List;

/* loaded from: classes3.dex */
public final class WindowManagerImpl implements WindowManager {
    private final Context mContext;
    private IBinder mDefaultToken;
    @UnsupportedAppUsage
    private final WindowManagerGlobal mGlobal;
    private final Window mParentWindow;

    public WindowManagerImpl(Context context) {
        this(context, null);
    }

    private WindowManagerImpl(Context context, Window parentWindow) {
        this.mGlobal = WindowManagerGlobal.getInstance();
        this.mContext = context;
        this.mParentWindow = parentWindow;
    }

    public WindowManagerImpl createLocalWindowManager(Window parentWindow) {
        return new WindowManagerImpl(this.mContext, parentWindow);
    }

    public WindowManagerImpl createPresentationWindowManager(Context displayContext) {
        return new WindowManagerImpl(displayContext, this.mParentWindow);
    }

    public void setDefaultToken(IBinder token) {
        this.mDefaultToken = token;
    }

    @Override // android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams params) {
        applyDefaultToken(params);
        this.mGlobal.addView(view, params, this.mContext.getDisplay(), this.mParentWindow);
    }

    @Override // android.view.ViewManager
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        applyDefaultToken(params);
        this.mGlobal.updateViewLayout(view, params);
    }

    private void applyDefaultToken(ViewGroup.LayoutParams params) {
        if (this.mDefaultToken != null && this.mParentWindow == null) {
            if (!(params instanceof WindowManager.LayoutParams)) {
                throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
            }
            WindowManager.LayoutParams wparams = (WindowManager.LayoutParams) params;
            if (wparams.token == null) {
                wparams.token = this.mDefaultToken;
            }
        }
    }

    @Override // android.view.ViewManager
    public void removeView(View view) {
        this.mGlobal.removeView(view, false);
    }

    @Override // android.view.WindowManager
    public void removeViewImmediate(View view) {
        this.mGlobal.removeView(view, true);
    }

    @Override // android.view.WindowManager
    public void requestAppKeyboardShortcuts(final WindowManager.KeyboardShortcutsReceiver receiver, int deviceId) {
        IResultReceiver resultReceiver = new IResultReceiver.Stub() { // from class: android.view.WindowManagerImpl.1
            @Override // com.android.internal.os.IResultReceiver
            public void send(int resultCode, Bundle resultData) throws RemoteException {
                List<KeyboardShortcutGroup> result = resultData.getParcelableArrayList(WindowManager.PARCEL_KEY_SHORTCUTS_ARRAY);
                receiver.onKeyboardShortcutsReceived(result);
            }
        };
        try {
            WindowManagerGlobal.getWindowManagerService().requestAppKeyboardShortcuts(resultReceiver, deviceId);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public Display getDefaultDisplay() {
        return this.mContext.getDisplay();
    }

    @Override // android.view.WindowManager
    public Region getCurrentImeTouchRegion() {
        try {
            return WindowManagerGlobal.getWindowManagerService().getCurrentImeTouchRegion();
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override // android.view.WindowManager
    public void setShouldShowWithInsecureKeyguard(int displayId, boolean shouldShow) {
        try {
            WindowManagerGlobal.getWindowManagerService().setShouldShowWithInsecureKeyguard(displayId, shouldShow);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public void setShouldShowSystemDecors(int displayId, boolean shouldShow) {
        try {
            WindowManagerGlobal.getWindowManagerService().setShouldShowSystemDecors(displayId, shouldShow);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public boolean shouldShowSystemDecors(int displayId) {
        try {
            return WindowManagerGlobal.getWindowManagerService().shouldShowSystemDecors(displayId);
        } catch (RemoteException e) {
            return false;
        }
    }

    @Override // android.view.WindowManager
    public void setShouldShowIme(int displayId, boolean shouldShow) {
        try {
            WindowManagerGlobal.getWindowManagerService().setShouldShowIme(displayId, shouldShow);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public boolean shouldShowIme(int displayId) {
        try {
            return WindowManagerGlobal.getWindowManagerService().shouldShowIme(displayId);
        } catch (RemoteException e) {
            return false;
        }
    }

    @Override // android.view.WindowManager
    public int getScreenId(String packageName) {
        try {
            return WindowManagerGlobal.getWindowManagerService().getScreenId(packageName);
        } catch (RemoteException e) {
            return -1;
        }
    }

    @Override // android.view.WindowManager
    public void setScreenId(String packageName, int screenId) {
        try {
            WindowManagerGlobal.getWindowManagerService().setScreenId(packageName, screenId);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public int getSharedId(String packageName) {
        try {
            return WindowManagerGlobal.getWindowManagerService().getSharedId(packageName);
        } catch (RemoteException e) {
            return -1;
        }
    }

    @Override // android.view.WindowManager
    public void setSharedId(String packageName, int sharedId) {
        try {
            WindowManagerGlobal.getWindowManagerService().setSharedId(packageName, sharedId);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public List<String> getSharedPackages() {
        try {
            return WindowManagerGlobal.getWindowManagerService().getSharedPackages();
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override // android.view.WindowManager
    public List<String> getFilterPackages(int sharedId) {
        try {
            return WindowManagerGlobal.getWindowManagerService().getFilterPackages(sharedId);
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override // android.view.WindowManager
    public void setSharedEvent(int event) {
        setSharedEvent(event, -1, null);
    }

    @Override // android.view.WindowManager
    public void setSharedEvent(int event, int sharedId) {
        setSharedEvent(event, sharedId, null);
    }

    @Override // android.view.WindowManager
    public void setSharedEvent(int event, int sharedId, String extras) {
        try {
            WindowManagerGlobal.getWindowManagerService().setSharedEvent(event, sharedId, extras);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public String getTopWindow() {
        try {
            return WindowManagerGlobal.getWindowManagerService().getTopWindow();
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override // android.view.WindowManager
    public Rect getActivityBounds(String packageName, boolean fullscreen) {
        try {
            return WindowManagerGlobal.getWindowManagerService().getActivityBounds(packageName, fullscreen);
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override // android.view.WindowManager
    public String getTopActivity(int type, int id) {
        try {
            return WindowManagerGlobal.getWindowManagerService().getTopActivity(type, id);
        } catch (RemoteException e) {
            return "";
        }
    }

    @Override // android.view.WindowManager
    public boolean isSharedScreenEnabled(int screenId) {
        try {
            return WindowManagerGlobal.getWindowManagerService().isSharedScreenEnabled(screenId);
        } catch (RemoteException e) {
            return false;
        }
    }

    @Override // android.view.WindowManager
    public boolean isSharedPackageEnabled(String packageName) {
        try {
            return WindowManagerGlobal.getWindowManagerService().isSharedPackageEnabled(packageName);
        } catch (RemoteException e) {
            return false;
        }
    }

    @Override // android.view.WindowManager
    public void setSharedScreenPolicy(int screenId, int policy) {
        try {
            WindowManagerGlobal.getWindowManagerService().setSharedScreenPolicy(screenId, policy);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public void setSharedPackagePolicy(String packageName, int policy) {
        try {
            WindowManagerGlobal.getWindowManagerService().setSharedPackagePolicy(packageName, policy);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public xpDialogInfo getTopDialog(Bundle extras) {
        try {
            return WindowManagerGlobal.getWindowManagerService().getTopDialog(extras);
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override // android.view.WindowManager
    public boolean dismissDialog(Bundle extras) {
        try {
            return WindowManagerGlobal.getWindowManagerService().dismissDialog(extras);
        } catch (RemoteException e) {
            return false;
        }
    }

    @Override // android.view.WindowManager
    public int getAppPolicy(Bundle extras) {
        try {
            return WindowManagerGlobal.getWindowManagerService().getAppPolicy(extras);
        } catch (RemoteException e) {
            return 0;
        }
    }

    @Override // android.view.WindowManager
    public void setModeEvent(int sharedId, int mode, String extra) {
        try {
            WindowManagerGlobal.getWindowManagerService().setModeEvent(sharedId, mode, extra);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public void setPackageSettings(String packageName, Bundle extras) {
        try {
            WindowManagerGlobal.getWindowManagerService().setPackageSettings(packageName, extras);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public void registerSharedListener(ISharedDisplayListener listener) {
        try {
            WindowManagerGlobal.getWindowManagerService().registerSharedListener(listener);
        } catch (RemoteException e) {
        }
    }

    @Override // android.view.WindowManager
    public void unregisterSharedListener(ISharedDisplayListener listener) {
        try {
            WindowManagerGlobal.getWindowManagerService().unregisterSharedListener(listener);
        } catch (RemoteException e) {
        }
    }
}
