package com.android.internal.telephony;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.ITelephony;
import java.util.function.Supplier;
/* loaded from: classes3.dex */
public final class TelephonyPermissions {
    private static final boolean DBG = false;
    private static final String LOG_TAG = "TelephonyPermissions";
    private static final Supplier<ITelephony> TELEPHONY_SUPPLIER = new Supplier() { // from class: com.android.internal.telephony.-$$Lambda$TelephonyPermissions$LxEEC4irBSbjD1lSC4EeVLgFY9I
        @Override // java.util.function.Supplier
        public final Object get() {
            ITelephony asInterface;
            asInterface = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
            return asInterface;
        }
    };

    private TelephonyPermissions() {
    }

    public static boolean checkCallingOrSelfReadPhoneState(Context context, int subId, String callingPackage, String message) {
        return checkReadPhoneState(context, subId, Binder.getCallingPid(), Binder.getCallingUid(), callingPackage, message);
    }

    public static boolean checkReadPhoneState(Context context, int subId, int pid, int uid, String callingPackage, String message) {
        return checkReadPhoneState(context, TELEPHONY_SUPPLIER, subId, pid, uid, callingPackage, message);
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
}
