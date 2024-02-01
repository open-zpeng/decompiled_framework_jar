package android.service.dreams;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.util.Slog;
import com.android.internal.R;
/* loaded from: classes2.dex */
public final class Sandman {
    private static final ComponentName SOMNAMBULATOR_COMPONENT = new ComponentName("com.android.systemui", "com.android.systemui.Somnambulator");
    private static final String TAG = "Sandman";

    private synchronized Sandman() {
    }

    public static synchronized boolean shouldStartDockApp(Context context, Intent intent) {
        ComponentName name = intent.resolveActivity(context.getPackageManager());
        return (name == null || name.equals(SOMNAMBULATOR_COMPONENT)) ? false : true;
    }

    public static synchronized void startDreamByUserRequest(Context context) {
        startDream(context, false);
    }

    public static synchronized void startDreamWhenDockedIfAppropriate(Context context) {
        if (!isScreenSaverEnabled(context) || !isScreenSaverActivatedOnDock(context)) {
            Slog.i(TAG, "Dreams currently disabled for docks.");
        } else {
            startDream(context, true);
        }
    }

    private static synchronized void startDream(Context context, boolean docked) {
        try {
            IDreamManager dreamManagerService = IDreamManager.Stub.asInterface(ServiceManager.getService(DreamService.DREAM_SERVICE));
            if (dreamManagerService != null && !dreamManagerService.isDreaming()) {
                if (docked) {
                    Slog.i(TAG, "Activating dream while docked.");
                    PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                    powerManager.wakeUp(SystemClock.uptimeMillis(), "android.service.dreams:DREAM");
                } else {
                    Slog.i(TAG, "Activating dream by user request.");
                }
                dreamManagerService.dream();
            }
        } catch (RemoteException ex) {
            Slog.e(TAG, "Could not start dream when docked.", ex);
        }
    }

    private static synchronized boolean isScreenSaverEnabled(Context context) {
        int def = context.getResources().getBoolean(R.bool.config_dreamsEnabledByDefault) ? 1 : 0;
        return Settings.Secure.getIntForUser(context.getContentResolver(), Settings.Secure.SCREENSAVER_ENABLED, def, -2) != 0;
    }

    private static synchronized boolean isScreenSaverActivatedOnDock(Context context) {
        int def = context.getResources().getBoolean(R.bool.config_dreamsActivatedOnDockByDefault) ? 1 : 0;
        return Settings.Secure.getIntForUser(context.getContentResolver(), Settings.Secure.SCREENSAVER_ACTIVATE_ON_DOCK, def, -2) != 0;
    }
}
