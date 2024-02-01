package android.app;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.android.internal.statusbar.IStatusBarService;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public class StatusBarManager {
    public static final int CAMERA_LAUNCH_SOURCE_LIFT_TRIGGER = 2;
    public static final int CAMERA_LAUNCH_SOURCE_POWER_DOUBLE_TAP = 1;
    public static final int CAMERA_LAUNCH_SOURCE_WIGGLE = 0;
    public static final int DISABLE2_GLOBAL_ACTIONS = 8;
    public static final int DISABLE2_MASK = 31;
    public static final int DISABLE2_NONE = 0;
    public static final int DISABLE2_NOTIFICATION_SHADE = 4;
    public static final int DISABLE2_QUICK_SETTINGS = 1;
    public static final int DISABLE2_ROTATE_SUGGESTIONS = 16;
    public static final int DISABLE2_SYSTEM_ICONS = 2;
    public static final int DISABLE_BACK = 4194304;
    public static final int DISABLE_CLOCK = 8388608;
    private protected static final int DISABLE_EXPAND = 65536;
    public static final int DISABLE_HOME = 2097152;
    public static final int DISABLE_MASK = 67043328;
    @Deprecated
    public static final int DISABLE_NAVIGATION = 18874368;
    private protected static final int DISABLE_NONE = 0;
    public static final int DISABLE_NOTIFICATION_ALERTS = 262144;
    public static final int DISABLE_NOTIFICATION_ICONS = 131072;
    @Deprecated
    private protected static final int DISABLE_NOTIFICATION_TICKER = 524288;
    public static final int DISABLE_RECENT = 16777216;
    public static final int DISABLE_SEARCH = 33554432;
    public static final int DISABLE_SYSTEM_INFO = 1048576;
    public static final int NAVIGATION_HINT_BACK_ALT = 1;
    public static final int NAVIGATION_HINT_IME_SHOWN = 2;
    public static final int WINDOW_NAVIGATION_BAR = 2;
    public static final int WINDOW_STATE_HIDDEN = 2;
    public static final int WINDOW_STATE_HIDING = 1;
    public static final int WINDOW_STATE_SHOWING = 0;
    public static final int WINDOW_STATUS_BAR = 1;
    public protected Context mContext;
    private IStatusBarService mService;
    public protected IBinder mToken = new Binder();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Disable2Flags {
    }

    public private protected StatusBarManager(Context context) {
        this.mContext = context;
    }

    public protected synchronized IStatusBarService getService() {
        if (this.mService == null) {
            this.mService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
            if (this.mService == null) {
                Slog.w("StatusBarManager", "warning: no STATUS_BAR_SERVICE");
            }
        }
        return this.mService;
    }

    private protected void disable(int what) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.disable(what, this.mToken, this.mContext.getPackageName());
            }
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized void disable2(int what) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.disable2(what, this.mToken, this.mContext.getPackageName());
            }
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private protected void expandNotificationsPanel() {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.expandNotificationsPanel();
            }
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private protected void collapsePanels() {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.collapsePanels();
            }
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private protected void expandSettingsPanel() {
        expandSettingsPanel(null);
    }

    private protected void expandSettingsPanel(String subPanel) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.expandSettingsPanel(subPanel);
            }
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private protected void setIcon(String slot, int iconId, int iconLevel, String contentDescription) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.setIcon(slot, this.mContext.getPackageName(), iconId, iconLevel, contentDescription);
            }
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private protected void removeIcon(String slot) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.removeIcon(slot);
            }
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private protected void setIconVisibility(String slot, boolean visible) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.setIconVisibility(slot, visible);
            }
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public static synchronized String windowStateToString(int state) {
        return state == 1 ? "WINDOW_STATE_HIDING" : state == 2 ? "WINDOW_STATE_HIDDEN" : state == 0 ? "WINDOW_STATE_SHOWING" : "WINDOW_STATE_UNKNOWN";
    }
}
