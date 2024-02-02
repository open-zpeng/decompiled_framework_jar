package android.telephony;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import java.util.List;
/* loaded from: classes2.dex */
public final class LocationAccessPolicy {
    private static final String LOG_TAG = LocationAccessPolicy.class.getSimpleName();

    public static synchronized boolean canAccessCellLocation(Context context, String pkgName, int uid, int pid, boolean throwOnDeniedPermission) throws SecurityException {
        Trace.beginSection("TelephonyLohcationCheck");
        boolean z = true;
        if (uid == 1001) {
            Trace.endSection();
            return true;
        }
        try {
            if (throwOnDeniedPermission) {
                context.enforcePermission(Manifest.permission.ACCESS_COARSE_LOCATION, pid, uid, "canAccessCellLocation");
            } else if (context.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, pid, uid) == -1) {
                Trace.endSection();
                return false;
            }
            int opCode = AppOpsManager.permissionToOpCode(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (opCode != -1 && ((AppOpsManager) context.getSystemService(AppOpsManager.class)).noteOpNoThrow(opCode, uid, pkgName) != 0) {
                Trace.endSection();
                return false;
            } else if (!isLocationModeEnabled(context, UserHandle.getUserId(uid))) {
                Trace.endSection();
                return false;
            } else {
                if (!isCurrentProfile(context, uid)) {
                    if (!checkInteractAcrossUsersFull(context)) {
                        z = false;
                    }
                }
                Trace.endSection();
                return z;
            }
        } catch (Throwable th) {
            Trace.endSection();
            throw th;
        }
    }

    private static synchronized boolean isLocationModeEnabled(Context context, int userId) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LocationManager.class);
        if (locationManager == null) {
            Log.w(LOG_TAG, "Couldn't get location manager, denying location access");
            return false;
        }
        return locationManager.isLocationEnabledForUser(UserHandle.of(userId));
    }

    private static synchronized boolean checkInteractAcrossUsersFull(Context context) {
        return context.checkCallingOrSelfPermission(Manifest.permission.INTERACT_ACROSS_USERS_FULL) == 0;
    }

    private static synchronized boolean isCurrentProfile(Context context, int uid) {
        long token = Binder.clearCallingIdentity();
        try {
            int currentUser = ActivityManager.getCurrentUser();
            int callingUserId = UserHandle.getUserId(uid);
            if (callingUserId == currentUser) {
                return true;
            }
            List<UserInfo> userProfiles = ((UserManager) context.getSystemService(UserManager.class)).getProfiles(currentUser);
            for (UserInfo user : userProfiles) {
                if (user.id == callingUserId) {
                    return true;
                }
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }
}
