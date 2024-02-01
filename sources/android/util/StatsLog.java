package android.util;

import android.content.Context;
import android.os.IStatsManager;
import android.os.RemoteException;
import android.os.ServiceManager;
/* loaded from: classes2.dex */
public final class StatsLog extends StatsLogInternal {
    private static final boolean DEBUG = false;
    private static final String TAG = "StatsLog";
    private static IStatsManager sService;

    private synchronized StatsLog() {
    }

    public static boolean logStart(int label) {
        synchronized (StatsLog.class) {
            try {
                try {
                    IStatsManager service = getIStatsManagerLocked();
                    if (service == null) {
                        return false;
                    }
                    service.sendAppBreadcrumbAtom(label, 3);
                    return true;
                } catch (RemoteException e) {
                    sService = null;
                    return false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static boolean logStop(int label) {
        synchronized (StatsLog.class) {
            try {
                try {
                    IStatsManager service = getIStatsManagerLocked();
                    if (service == null) {
                        return false;
                    }
                    service.sendAppBreadcrumbAtom(label, 2);
                    return true;
                } catch (RemoteException e) {
                    sService = null;
                    return false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static boolean logEvent(int label) {
        synchronized (StatsLog.class) {
            try {
                try {
                    IStatsManager service = getIStatsManagerLocked();
                    if (service == null) {
                        return false;
                    }
                    service.sendAppBreadcrumbAtom(label, 1);
                    return true;
                } catch (RemoteException e) {
                    sService = null;
                    return false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static synchronized IStatsManager getIStatsManagerLocked() throws RemoteException {
        if (sService != null) {
            return sService;
        }
        sService = IStatsManager.Stub.asInterface(ServiceManager.getService(Context.STATS_MANAGER));
        return sService;
    }
}
