package android.telephony;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.location.LocationManager;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.Build;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.widget.Toast;
import java.util.List;

/* loaded from: classes2.dex */
public final class LocationAccessPolicy {
    private static final boolean DBG = false;
    public static final int MAX_SDK_FOR_ANY_ENFORCEMENT = 10000;
    private static final String TAG = "LocationAccessPolicy";

    /* loaded from: classes2.dex */
    public enum LocationPermissionResult {
        ALLOWED,
        DENIED_SOFT,
        DENIED_HARD
    }

    /* loaded from: classes2.dex */
    public static class LocationPermissionQuery {
        public final String callingPackage;
        public final int callingPid;
        public final int callingUid;
        public final boolean logAsInfo;
        public final String method;
        public final int minSdkVersionForCoarse;
        public final int minSdkVersionForFine;

        private LocationPermissionQuery(String callingPackage, int callingUid, int callingPid, int minSdkVersionForCoarse, int minSdkVersionForFine, boolean logAsInfo, String method) {
            this.callingPackage = callingPackage;
            this.callingUid = callingUid;
            this.callingPid = callingPid;
            this.minSdkVersionForCoarse = minSdkVersionForCoarse;
            this.minSdkVersionForFine = minSdkVersionForFine;
            this.logAsInfo = logAsInfo;
            this.method = method;
        }

        /* loaded from: classes2.dex */
        public static class Builder {
            private String mCallingPackage;
            private int mCallingPid;
            private int mCallingUid;
            private String mMethod;
            private int mMinSdkVersionForCoarse = Integer.MAX_VALUE;
            private int mMinSdkVersionForFine = Integer.MAX_VALUE;
            private boolean mLogAsInfo = false;

            public Builder setCallingPackage(String callingPackage) {
                this.mCallingPackage = callingPackage;
                return this;
            }

            public Builder setCallingUid(int callingUid) {
                this.mCallingUid = callingUid;
                return this;
            }

            public Builder setCallingPid(int callingPid) {
                this.mCallingPid = callingPid;
                return this;
            }

            public Builder setMinSdkVersionForCoarse(int minSdkVersionForCoarse) {
                this.mMinSdkVersionForCoarse = minSdkVersionForCoarse;
                return this;
            }

            public Builder setMinSdkVersionForFine(int minSdkVersionForFine) {
                this.mMinSdkVersionForFine = minSdkVersionForFine;
                return this;
            }

            public Builder setMethod(String method) {
                this.mMethod = method;
                return this;
            }

            public Builder setLogAsInfo(boolean logAsInfo) {
                this.mLogAsInfo = logAsInfo;
                return this;
            }

            public LocationPermissionQuery build() {
                return new LocationPermissionQuery(this.mCallingPackage, this.mCallingUid, this.mCallingPid, this.mMinSdkVersionForCoarse, this.mMinSdkVersionForFine, this.mLogAsInfo, this.mMethod);
            }
        }
    }

    private static void logError(Context context, LocationPermissionQuery query, String errorMsg) {
        if (query.logAsInfo) {
            Log.i(TAG, errorMsg);
            return;
        }
        Log.e(TAG, errorMsg);
        try {
            if (Build.IS_DEBUGGABLE) {
                Toast.makeText(context, errorMsg, 0).show();
            }
        } catch (Throwable th) {
        }
    }

    private static LocationPermissionResult appOpsModeToPermissionResult(int appOpsMode) {
        if (appOpsMode != 0) {
            if (appOpsMode == 2) {
                return LocationPermissionResult.DENIED_HARD;
            }
            return LocationPermissionResult.DENIED_SOFT;
        }
        return LocationPermissionResult.ALLOWED;
    }

    private static LocationPermissionResult checkAppLocationPermissionHelper(Context context, LocationPermissionQuery query, String permissionToCheck) {
        String locationTypeForLog = Manifest.permission.ACCESS_FINE_LOCATION.equals(permissionToCheck) ? "fine" : "coarse";
        boolean hasManifestPermission = checkManifestPermission(context, query.callingPid, query.callingUid, permissionToCheck);
        if (hasManifestPermission) {
            int appOpMode = ((AppOpsManager) context.getSystemService(AppOpsManager.class)).noteOpNoThrow(AppOpsManager.permissionToOpCode(permissionToCheck), query.callingUid, query.callingPackage);
            if (appOpMode == 0) {
                return LocationPermissionResult.ALLOWED;
            }
            Log.i(TAG, query.callingPackage + " is aware of " + locationTypeForLog + " but the app-ops permission is specifically denied.");
            return appOpsModeToPermissionResult(appOpMode);
        }
        int minSdkVersion = Manifest.permission.ACCESS_FINE_LOCATION.equals(permissionToCheck) ? query.minSdkVersionForFine : query.minSdkVersionForCoarse;
        if (minSdkVersion > 10000) {
            String errorMsg = "Allowing " + query.callingPackage + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + locationTypeForLog + " because we're not enforcing API " + minSdkVersion + " yet. Please fix this app because it will break in the future. Called from " + query.method;
            logError(context, query, errorMsg);
            return null;
        }
        String errorMsg2 = query.callingPackage;
        if (!isAppAtLeastSdkVersion(context, errorMsg2, minSdkVersion)) {
            String errorMsg3 = "Allowing " + query.callingPackage + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + locationTypeForLog + " because it doesn't target API " + minSdkVersion + " yet. Please fix this app. Called from " + query.method;
            logError(context, query, errorMsg3);
            return null;
        }
        return LocationPermissionResult.DENIED_HARD;
    }

    public static LocationPermissionResult checkLocationPermission(Context context, LocationPermissionQuery query) {
        LocationPermissionResult resultForCoarse;
        LocationPermissionResult resultForFine;
        if (query.callingUid == 1001 || query.callingUid == 1000 || query.callingUid == 0) {
            return LocationPermissionResult.ALLOWED;
        }
        if (!checkSystemLocationAccess(context, query.callingUid, query.callingPid)) {
            return LocationPermissionResult.DENIED_SOFT;
        }
        if (query.minSdkVersionForFine < Integer.MAX_VALUE && (resultForFine = checkAppLocationPermissionHelper(context, query, Manifest.permission.ACCESS_FINE_LOCATION)) != null) {
            return resultForFine;
        }
        if (query.minSdkVersionForCoarse < Integer.MAX_VALUE && (resultForCoarse = checkAppLocationPermissionHelper(context, query, Manifest.permission.ACCESS_COARSE_LOCATION)) != null) {
            return resultForCoarse;
        }
        return LocationPermissionResult.ALLOWED;
    }

    private static boolean checkManifestPermission(Context context, int pid, int uid, String permissionToCheck) {
        return context.checkPermission(permissionToCheck, pid, uid) == 0;
    }

    private static boolean checkSystemLocationAccess(Context context, int uid, int pid) {
        if (isLocationModeEnabled(context, UserHandle.getUserId(uid))) {
            return isCurrentProfile(context, uid) || checkInteractAcrossUsersFull(context, pid, uid);
        }
        return false;
    }

    private static boolean isLocationModeEnabled(Context context, int userId) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LocationManager.class);
        if (locationManager == null) {
            Log.w(TAG, "Couldn't get location manager, denying location access");
            return false;
        }
        return locationManager.isLocationEnabledForUser(UserHandle.of(userId));
    }

    private static boolean checkInteractAcrossUsersFull(Context context, int pid, int uid) {
        return checkManifestPermission(context, pid, uid, Manifest.permission.INTERACT_ACROSS_USERS_FULL);
    }

    private static boolean isCurrentProfile(Context context, int uid) {
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

    private static boolean isAppAtLeastSdkVersion(Context context, String pkgName, int sdkVersion) {
        if (context.getPackageManager().getApplicationInfo(pkgName, 0).targetSdkVersion < sdkVersion) {
            return false;
        }
        return true;
    }
}
