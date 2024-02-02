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

    /* JADX WARN: Removed duplicated region for block: B:28:0x0069 A[Catch: RemoteException -> 0x0055, TRY_ENTER, TryCatch #2 {RemoteException -> 0x0055, blocks: (B:18:0x004d, B:28:0x0069, B:29:0x006d, B:31:0x0073, B:39:0x009c, B:41:0x00a0), top: B:105:0x004d }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0091 A[Catch: RemoteException -> 0x0208, TRY_ENTER, TryCatch #1 {RemoteException -> 0x0208, blocks: (B:12:0x0037, B:13:0x003b, B:15:0x0041, B:25:0x005d, B:35:0x0091, B:37:0x0098, B:46:0x00ad), top: B:103:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0158 A[Catch: RemoteException -> 0x01f3, TryCatch #0 {RemoteException -> 0x01f3, blocks: (B:51:0x00ed, B:52:0x00f1, B:54:0x00f7, B:56:0x0102, B:58:0x0106, B:62:0x0112, B:64:0x0150, B:48:0x00e1, B:65:0x0158, B:67:0x0164, B:69:0x0168, B:71:0x016f, B:74:0x0195, B:75:0x0199, B:77:0x019f, B:79:0x01a9, B:81:0x01b0, B:88:0x01ed, B:91:0x01f5, B:93:0x01fb), top: B:101:0x00ed }] */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void disableCarrierAppsUntilPrivileged(java.lang.String r23, android.content.pm.IPackageManager r24, android.telephony.TelephonyManager r25, android.content.ContentResolver r26, int r27, android.util.ArraySet<java.lang.String> r28, android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> r29) {
        /*
            Method dump skipped, instructions count: 536
            To view this dump add '--comments-level debug' option
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
