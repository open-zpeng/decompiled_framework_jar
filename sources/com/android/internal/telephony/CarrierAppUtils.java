package com.android.internal.telephony;

import android.content.ContentResolver;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.server.SystemConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public final class CarrierAppUtils {
    private static final boolean DEBUG = false;
    private static final String TAG = "CarrierAppUtils";

    private CarrierAppUtils() {
    }

    public static synchronized void disableCarrierAppsUntilPrivileged(String callingPackage, IPackageManager packageManager, TelephonyManager telephonyManager, ContentResolver contentResolver, int userId) {
        synchronized (CarrierAppUtils.class) {
            SystemConfig config = SystemConfig.getInstance();
            ArraySet<String> systemCarrierAppsDisabledUntilUsed = config.getDisabledUntilUsedPreinstalledCarrierApps();
            ArrayMap<String, List<String>> systemCarrierAssociatedAppsDisabledUntilUsed = config.getDisabledUntilUsedPreinstalledCarrierAssociatedApps();
            disableCarrierAppsUntilPrivileged(callingPackage, packageManager, telephonyManager, contentResolver, userId, systemCarrierAppsDisabledUntilUsed, systemCarrierAssociatedAppsDisabledUntilUsed);
        }
    }

    public static synchronized void disableCarrierAppsUntilPrivileged(String callingPackage, IPackageManager packageManager, ContentResolver contentResolver, int userId) {
        synchronized (CarrierAppUtils.class) {
            SystemConfig config = SystemConfig.getInstance();
            ArraySet<String> systemCarrierAppsDisabledUntilUsed = config.getDisabledUntilUsedPreinstalledCarrierApps();
            ArrayMap<String, List<String>> systemCarrierAssociatedAppsDisabledUntilUsed = config.getDisabledUntilUsedPreinstalledCarrierAssociatedApps();
            disableCarrierAppsUntilPrivileged(callingPackage, packageManager, null, contentResolver, userId, systemCarrierAppsDisabledUntilUsed, systemCarrierAssociatedAppsDisabledUntilUsed);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x007d A[Catch: RemoteException -> 0x0067, TRY_ENTER, TryCatch #2 {RemoteException -> 0x0067, blocks: (B:18:0x0059, B:20:0x005f, B:30:0x007d, B:31:0x0081, B:33:0x0087, B:42:0x00b5, B:44:0x00bc), top: B:112:0x0059 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a7 A[Catch: RemoteException -> 0x0235, TRY_ENTER, TRY_LEAVE, TryCatch #1 {RemoteException -> 0x0235, blocks: (B:12:0x0037, B:13:0x003b, B:15:0x0041, B:27:0x0070, B:37:0x00a7, B:40:0x00b1, B:50:0x00d2), top: B:110:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x018d A[Catch: RemoteException -> 0x0233, TRY_LEAVE, TryCatch #0 {RemoteException -> 0x0233, blocks: (B:55:0x0119, B:56:0x011d, B:58:0x0123, B:60:0x012e, B:62:0x0133, B:67:0x0141, B:71:0x0185, B:52:0x0107, B:72:0x018d, B:75:0x019f, B:77:0x01a3, B:79:0x01a9, B:83:0x01cf, B:84:0x01d3, B:86:0x01d9, B:88:0x01e3, B:90:0x01e9, B:95:0x021c, B:96:0x0220, B:98:0x0226), top: B:108:0x0119 }] */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void disableCarrierAppsUntilPrivileged(java.lang.String r29, android.content.pm.IPackageManager r30, android.telephony.TelephonyManager r31, android.content.ContentResolver r32, int r33, android.util.ArraySet<java.lang.String> r34, android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> r35) {
        /*
            Method dump skipped, instructions count: 578
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.CarrierAppUtils.disableCarrierAppsUntilPrivileged(java.lang.String, android.content.pm.IPackageManager, android.telephony.TelephonyManager, android.content.ContentResolver, int, android.util.ArraySet, android.util.ArrayMap):void");
    }

    public static List<ApplicationInfo> getDefaultCarrierApps(IPackageManager packageManager, TelephonyManager telephonyManager, int userId) {
        List<ApplicationInfo> candidates = getDefaultCarrierAppCandidates(packageManager, userId);
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        for (int i = candidates.size() - 1; i >= 0; i--) {
            ApplicationInfo ai = candidates.get(i);
            String packageName = ai.packageName;
            boolean hasPrivileges = telephonyManager.checkCarrierPrivilegesForPackageAnyPhone(packageName) == 1;
            if (!hasPrivileges) {
                candidates.remove(i);
            }
        }
        return candidates;
    }

    public static List<ApplicationInfo> getDefaultCarrierAppCandidates(IPackageManager packageManager, int userId) {
        ArraySet<String> systemCarrierAppsDisabledUntilUsed = SystemConfig.getInstance().getDisabledUntilUsedPreinstalledCarrierApps();
        return getDefaultCarrierAppCandidatesHelper(packageManager, userId, systemCarrierAppsDisabledUntilUsed);
    }

    private static List<ApplicationInfo> getDefaultCarrierAppCandidatesHelper(IPackageManager packageManager, int userId, ArraySet<String> systemCarrierAppsDisabledUntilUsed) {
        int size;
        if (systemCarrierAppsDisabledUntilUsed == null || (size = systemCarrierAppsDisabledUntilUsed.size()) == 0) {
            return null;
        }
        List<ApplicationInfo> apps = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String packageName = systemCarrierAppsDisabledUntilUsed.valueAt(i);
            ApplicationInfo ai = getApplicationInfoIfSystemApp(packageManager, userId, packageName);
            if (ai != null) {
                apps.add(ai);
            }
        }
        return apps;
    }

    private static Map<String, List<ApplicationInfo>> getDefaultCarrierAssociatedAppsHelper(IPackageManager packageManager, int userId, ArrayMap<String, List<String>> systemCarrierAssociatedAppsDisabledUntilUsed) {
        int size = systemCarrierAssociatedAppsDisabledUntilUsed.size();
        Map<String, List<ApplicationInfo>> associatedApps = new ArrayMap<>(size);
        for (int i = 0; i < size; i++) {
            String carrierAppPackage = systemCarrierAssociatedAppsDisabledUntilUsed.keyAt(i);
            List<String> associatedAppPackages = systemCarrierAssociatedAppsDisabledUntilUsed.valueAt(i);
            for (int j = 0; j < associatedAppPackages.size(); j++) {
                ApplicationInfo ai = getApplicationInfoIfSystemApp(packageManager, userId, associatedAppPackages.get(j));
                if (ai != null && !ai.isUpdatedSystemApp()) {
                    List<ApplicationInfo> appList = associatedApps.get(carrierAppPackage);
                    if (appList == null) {
                        appList = new ArrayList<>();
                        associatedApps.put(carrierAppPackage, appList);
                    }
                    appList.add(ai);
                }
            }
        }
        return associatedApps;
    }

    private static ApplicationInfo getApplicationInfoIfSystemApp(IPackageManager packageManager, int userId, String packageName) {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 536903680, userId);
            if (ai != null) {
                if (ai.isSystemApp()) {
                    return ai;
                }
                return null;
            }
            return null;
        } catch (RemoteException e) {
            Slog.w(TAG, "Could not reach PackageManager", e);
            return null;
        }
    }
}
