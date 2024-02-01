package com.android.internal.telephony;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.SettingsStringUtil;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.util.StatsLog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.ITelephony;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/* loaded from: classes3.dex */
public final class TelephonyPermissions {
    private static final boolean DBG = false;
    private static final String LOG_TAG = "TelephonyPermissions";
    private static final String PROPERTY_DEVICE_IDENTIFIER_ACCESS_RESTRICTIONS_DISABLED = "device_identifier_access_restrictions_disabled";
    private static final Supplier<ITelephony> TELEPHONY_SUPPLIER = new Supplier() { // from class: com.android.internal.telephony.-$$Lambda$TelephonyPermissions$LxEEC4irBSbjD1lSC4EeVLgFY9I
        @Override // java.util.function.Supplier
        public final Object get() {
            ITelephony asInterface;
            asInterface = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
            return asInterface;
        }
    };
    private static final Map<String, Set<String>> sReportedDeviceIDPackages = new HashMap();

    private TelephonyPermissions() {
    }

    public static boolean checkCallingOrSelfReadPhoneState(Context context, int subId, String callingPackage, String message) {
        return checkReadPhoneState(context, subId, Binder.getCallingPid(), Binder.getCallingUid(), callingPackage, message);
    }

    public static boolean checkCallingOrSelfReadPhoneStateNoThrow(Context context, int subId, String callingPackage, String message) {
        try {
            return checkCallingOrSelfReadPhoneState(context, subId, callingPackage, message);
        } catch (SecurityException e) {
            return false;
        }
    }

    public static boolean checkReadPhoneState(Context context, int subId, int pid, int uid, String callingPackage, String message) {
        return checkReadPhoneState(context, TELEPHONY_SUPPLIER, subId, pid, uid, callingPackage, message);
    }

    public static boolean checkCarrierPrivilegeForSubId(int subId) {
        return SubscriptionManager.isValidSubscriptionId(subId) && getCarrierPrivilegeStatus(TELEPHONY_SUPPLIER, subId, Binder.getCallingUid()) == 1;
    }

    @VisibleForTesting
    public static boolean checkReadPhoneState(Context context, Supplier<ITelephony> telephonySupplier, int subId, int pid, int uid, String callingPackage, String message) {
        try {
            context.enforcePermission(Manifest.permission.READ_PRIVILEGED_PHONE_STATE, pid, uid, message);
            return true;
        } catch (SecurityException e) {
            try {
                context.enforcePermission(Manifest.permission.READ_PHONE_STATE, pid, uid, message);
                AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                return appOps.noteOp(51, uid, callingPackage) == 0;
            } catch (SecurityException phoneStateException) {
                if (SubscriptionManager.isValidSubscriptionId(subId)) {
                    enforceCarrierPrivilege(telephonySupplier, subId, uid, message);
                    return true;
                }
                throw phoneStateException;
            }
        }
    }

    public static boolean checkReadPhoneStateOnAnyActiveSub(Context context, int pid, int uid, String callingPackage, String message) {
        return checkReadPhoneStateOnAnyActiveSub(context, TELEPHONY_SUPPLIER, pid, uid, callingPackage, message);
    }

    @VisibleForTesting
    public static boolean checkReadPhoneStateOnAnyActiveSub(Context context, Supplier<ITelephony> telephonySupplier, int pid, int uid, String callingPackage, String message) {
        try {
            context.enforcePermission(Manifest.permission.READ_PRIVILEGED_PHONE_STATE, pid, uid, message);
            return true;
        } catch (SecurityException e) {
            try {
                context.enforcePermission(Manifest.permission.READ_PHONE_STATE, pid, uid, message);
                AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                return appOps.noteOp(51, uid, callingPackage) == 0;
            } catch (SecurityException e2) {
                return checkCarrierPrivilegeForAnySubId(context, telephonySupplier, uid);
            }
        }
    }

    public static boolean checkCallingOrSelfReadDeviceIdentifiers(Context context, String callingPackage, String message) {
        return checkCallingOrSelfReadDeviceIdentifiers(context, -1, callingPackage, message);
    }

    public static boolean checkCallingOrSelfReadDeviceIdentifiers(Context context, int subId, String callingPackage, String message) {
        return checkPrivilegedReadPermissionOrCarrierPrivilegePermission(context, subId, callingPackage, message, true);
    }

    public static boolean checkCallingOrSelfReadSubscriberIdentifiers(Context context, int subId, String callingPackage, String message) {
        return checkPrivilegedReadPermissionOrCarrierPrivilegePermission(context, subId, callingPackage, message, false);
    }

    private static boolean checkPrivilegedReadPermissionOrCarrierPrivilegePermission(Context context, int subId, String callingPackage, String message, boolean allowCarrierPrivilegeOnAnySub) {
        int uid = Binder.getCallingUid();
        int pid = Binder.getCallingPid();
        int appId = UserHandle.getAppId(uid);
        if (appId == 1000 || appId == 0 || context.checkPermission(Manifest.permission.READ_PRIVILEGED_PHONE_STATE, pid, uid) == 0 || checkCarrierPrivilegeForSubId(subId)) {
            return true;
        }
        if (allowCarrierPrivilegeOnAnySub && checkCarrierPrivilegeForAnySubId(context, TELEPHONY_SUPPLIER, uid)) {
            return true;
        }
        if (callingPackage != null) {
            long token = Binder.clearCallingIdentity();
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                if (appOpsManager.noteOpNoThrow(AppOpsManager.OPSTR_READ_DEVICE_IDENTIFIERS, uid, callingPackage) == 0) {
                    return true;
                }
                Binder.restoreCallingIdentity(token);
                DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                if (devicePolicyManager != null && devicePolicyManager.checkDeviceIdentifierAccess(callingPackage, pid, uid)) {
                    return true;
                }
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
        return reportAccessDeniedToReadIdentifiers(context, subId, pid, uid, callingPackage, message);
    }

    private static boolean reportAccessDeniedToReadIdentifiers(Context context, int subId, int pid, int uid, String callingPackage, String message) {
        Set invokedMethods;
        boolean isPreinstalled = false;
        boolean isPrivApp = false;
        ApplicationInfo callingPackageInfo = null;
        try {
            callingPackageInfo = context.getPackageManager().getApplicationInfoAsUser(callingPackage, 0, UserHandle.getUserId(uid));
            if (callingPackageInfo != null && callingPackageInfo.isSystemApp()) {
                isPreinstalled = true;
                if (callingPackageInfo.isPrivilegedApp()) {
                    isPrivApp = true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "Exception caught obtaining package info for package " + callingPackage, e);
        }
        boolean packageReported = sReportedDeviceIDPackages.containsKey(callingPackage);
        if (!packageReported || !sReportedDeviceIDPackages.get(callingPackage).contains(message)) {
            if (!packageReported) {
                invokedMethods = new HashSet();
                sReportedDeviceIDPackages.put(callingPackage, invokedMethods);
            } else {
                invokedMethods = sReportedDeviceIDPackages.get(callingPackage);
            }
            invokedMethods.add(message);
            StatsLog.write(172, callingPackage, message, isPreinstalled, isPrivApp);
        }
        Log.w(LOG_TAG, "reportAccessDeniedToReadIdentifiers:" + callingPackage + SettingsStringUtil.DELIMITER + message + ":isPreinstalled=" + isPreinstalled + ":isPrivApp=" + isPrivApp);
        if (callingPackageInfo != null && callingPackageInfo.targetSdkVersion < 29 && (context.checkPermission(Manifest.permission.READ_PHONE_STATE, pid, uid) == 0 || checkCarrierPrivilegeForSubId(subId))) {
            return false;
        }
        throw new SecurityException(message + ": The user " + uid + " does not meet the requirements to access device identifiers.");
    }

    public static boolean checkReadCallLog(Context context, int subId, int pid, int uid, String callingPackage) {
        return checkReadCallLog(context, TELEPHONY_SUPPLIER, subId, pid, uid, callingPackage);
    }

    @VisibleForTesting
    public static boolean checkReadCallLog(Context context, Supplier<ITelephony> telephonySupplier, int subId, int pid, int uid, String callingPackage) {
        if (context.checkPermission(Manifest.permission.READ_CALL_LOG, pid, uid) != 0) {
            if (SubscriptionManager.isValidSubscriptionId(subId)) {
                enforceCarrierPrivilege(telephonySupplier, subId, uid, "readCallLog");
                return true;
            }
            return false;
        }
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        return appOps.noteOp(6, uid, callingPackage) == 0;
    }

    public static boolean checkCallingOrSelfReadPhoneNumber(Context context, int subId, String callingPackage, String message) {
        return checkReadPhoneNumber(context, TELEPHONY_SUPPLIER, subId, Binder.getCallingPid(), Binder.getCallingUid(), callingPackage, message);
    }

    @VisibleForTesting
    public static boolean checkReadPhoneNumber(Context context, Supplier<ITelephony> telephonySupplier, int subId, int pid, int uid, String callingPackage, String message) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (appOps.noteOp(15, uid, callingPackage) == 0) {
            return true;
        }
        try {
            return checkReadPhoneState(context, telephonySupplier, subId, pid, uid, callingPackage, message);
        } catch (SecurityException e) {
            try {
                context.enforcePermission(Manifest.permission.READ_SMS, pid, uid, message);
                int opCode = AppOpsManager.permissionToOpCode(Manifest.permission.READ_SMS);
                if (opCode != -1) {
                    return appOps.noteOp(opCode, uid, callingPackage) == 0;
                }
                return true;
            } catch (SecurityException e2) {
                try {
                    context.enforcePermission(Manifest.permission.READ_PHONE_NUMBERS, pid, uid, message);
                    int opCode2 = AppOpsManager.permissionToOpCode(Manifest.permission.READ_PHONE_NUMBERS);
                    if (opCode2 != -1) {
                        return appOps.noteOp(opCode2, uid, callingPackage) == 0;
                    }
                    return true;
                } catch (SecurityException e3) {
                    throw new SecurityException(message + ": Neither user " + uid + " nor current process has " + Manifest.permission.READ_PHONE_STATE + ", " + Manifest.permission.READ_SMS + ", or " + Manifest.permission.READ_PHONE_NUMBERS);
                }
            }
        }
    }

    public static void enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(Context context, int subId, String message) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.MODIFY_PHONE_STATE) == 0) {
            return;
        }
        enforceCallingOrSelfCarrierPrivilege(subId, message);
    }

    public static void enforeceCallingOrSelfReadPhoneStatePermissionOrCarrierPrivilege(Context context, int subId, String message) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == 0) {
            return;
        }
        enforceCallingOrSelfCarrierPrivilege(subId, message);
    }

    public static void enforeceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(Context context, int subId, String message) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.READ_PRIVILEGED_PHONE_STATE) == 0) {
            return;
        }
        enforceCallingOrSelfCarrierPrivilege(subId, message);
    }

    public static void enforceCallingOrSelfCarrierPrivilege(int subId, String message) {
        enforceCarrierPrivilege(subId, Binder.getCallingUid(), message);
    }

    private static void enforceCarrierPrivilege(int subId, int uid, String message) {
        enforceCarrierPrivilege(TELEPHONY_SUPPLIER, subId, uid, message);
    }

    private static void enforceCarrierPrivilege(Supplier<ITelephony> telephonySupplier, int subId, int uid, String message) {
        if (getCarrierPrivilegeStatus(telephonySupplier, subId, uid) != 1) {
            throw new SecurityException(message);
        }
    }

    private static boolean checkCarrierPrivilegeForAnySubId(Context context, Supplier<ITelephony> telephonySupplier, int uid) {
        SubscriptionManager sm = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        int[] activeSubIds = sm.getActiveSubscriptionIdList(false);
        for (int activeSubId : activeSubIds) {
            if (getCarrierPrivilegeStatus(telephonySupplier, activeSubId, uid) == 1) {
                return true;
            }
        }
        return false;
    }

    private static int getCarrierPrivilegeStatus(Supplier<ITelephony> telephonySupplier, int subId, int uid) {
        ITelephony telephony = telephonySupplier.get();
        if (telephony != null) {
            try {
                return telephony.getCarrierPrivilegeStatusForUid(subId, uid);
            } catch (RemoteException e) {
            }
        }
        Rlog.e(LOG_TAG, "Phone process is down, cannot check carrier privileges");
        return 0;
    }

    public static void enforceShellOnly(int callingUid, String message) {
        if (callingUid == 2000 || callingUid == 0) {
            return;
        }
        throw new SecurityException(message + ": Only shell user can call it");
    }
}
