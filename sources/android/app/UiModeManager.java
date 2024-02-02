package android.app;

import android.app.IUiModeManager;
import android.content.Context;
import android.location.Location;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.view.SurfaceView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public class UiModeManager {
    public static final int DISABLE_CAR_MODE_GO_HOME = 1;
    public static final int ENABLE_CAR_MODE_ALLOW_SLEEP = 2;
    public static final int ENABLE_CAR_MODE_GO_CAR_HOME = 1;
    public static final int MODE_NIGHT_AUTO = 0;
    public static final int MODE_NIGHT_NO = 1;
    public static final int MODE_NIGHT_YES = 2;
    private static final String TAG = "UiModeManager";
    private IUiModeManager mService = IUiModeManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.UI_MODE_SERVICE));
    public static String ACTION_ENTER_CAR_MODE = "android.app.action.ENTER_CAR_MODE";
    public static String ACTION_EXIT_CAR_MODE = "android.app.action.EXIT_CAR_MODE";
    public static String ACTION_ENTER_DESK_MODE = "android.app.action.ENTER_DESK_MODE";
    public static String ACTION_EXIT_DESK_MODE = "android.app.action.EXIT_DESK_MODE";
    private static final String PROP_THEME_INTERVAL = "persist.sys.theme.interval";
    public static final long THEME_ANIMATION_INTERVAL = SystemProperties.getLong(PROP_THEME_INTERVAL, 500);
    public static final long THEME_TIMEOUT_DELAY = THEME_ANIMATION_INTERVAL + SurfaceView.SurfaceViewFactory.BACKGROUND_TRANSACTION_DELAY;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface NightMode {
    }

    public void enableCarMode(int flags) {
        if (this.mService != null) {
            try {
                this.mService.enableCarMode(flags);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void disableCarMode(int flags) {
        if (this.mService != null) {
            try {
                this.mService.disableCarMode(flags);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public int getCurrentModeType() {
        if (this.mService != null) {
            try {
                return this.mService.getCurrentModeType();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return 1;
    }

    public void setNightMode(int mode) {
        if (this.mService != null) {
            try {
                this.mService.setNightMode(mode);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public int getNightMode() {
        if (this.mService != null) {
            try {
                return this.mService.getNightMode();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return -1;
    }

    public boolean isUiModeLocked() {
        if (this.mService != null) {
            try {
                return this.mService.isUiModeLocked();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return true;
    }

    public boolean isNightModeLocked() {
        if (this.mService != null) {
            try {
                return this.mService.isNightModeLocked();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return true;
    }

    public int getThemeMode() {
        if (this.mService != null) {
            try {
                return this.mService.getThemeMode();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return -1;
    }

    public int getDayNightMode() {
        if (this.mService != null) {
            try {
                return this.mService.getDayNightMode();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return -1;
    }

    public int getDayNightAutoMode() {
        if (this.mService != null) {
            try {
                return this.mService.getDayNightAutoMode();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return -1;
    }

    public void applyThemeMode(int themeMode) {
        if (this.mService != null) {
            try {
                this.mService.applyThemeMode(themeMode);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void applyDayNightMode(int daynightMode) {
        if (this.mService != null) {
            try {
                this.mService.applyDayNightMode(daynightMode);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public boolean isThemeWorking() {
        if (this.mService != null) {
            try {
                return this.mService.isThemeWorking();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return false;
    }

    public long[] getTwilightState(Location location, long timeMillis) {
        if (this.mService != null) {
            try {
                return this.mService.getTwilightState(location, timeMillis);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return new long[]{0, 0};
    }
}
