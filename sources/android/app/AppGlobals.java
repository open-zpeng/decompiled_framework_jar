package android.app;

import android.content.pm.IPackageManager;
/* loaded from: classes.dex */
public class AppGlobals {
    /* JADX INFO: Access modifiers changed from: private */
    public static Application getInitialApplication() {
        return ActivityThread.currentApplication();
    }

    private protected static String getInitialPackage() {
        return ActivityThread.currentPackageName();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IPackageManager getPackageManager() {
        return ActivityThread.getPackageManager();
    }

    public static synchronized int getIntCoreSetting(String key, int defaultValue) {
        ActivityThread currentActivityThread = ActivityThread.currentActivityThread();
        if (currentActivityThread != null) {
            return currentActivityThread.getIntCoreSetting(key, defaultValue);
        }
        return defaultValue;
    }
}
