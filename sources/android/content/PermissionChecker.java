package android.content;

import android.app.AppOpsManager;
import android.os.Binder;
import android.os.Process;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public final class PermissionChecker {
    public static final int PERMISSION_DENIED = -1;
    public static final int PERMISSION_DENIED_APP_OP = -2;
    public static final int PERMISSION_GRANTED = 0;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface PermissionResult {
    }

    private synchronized PermissionChecker() {
    }

    public static synchronized int checkPermission(Context context, String permission, int pid, int uid, String packageName) {
        if (context.checkPermission(permission, pid, uid) == -1) {
            return -1;
        }
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        String op = AppOpsManager.permissionToOp(permission);
        if (op == null) {
            return 0;
        }
        if (packageName == null) {
            String[] packageNames = context.getPackageManager().getPackagesForUid(uid);
            if (packageNames == null || packageNames.length <= 0) {
                return -1;
            }
            packageName = packageNames[0];
        }
        if (appOpsManager.noteProxyOpNoThrow(op, packageName) == 0) {
            return 0;
        }
        return -2;
    }

    public static synchronized int checkSelfPermission(Context context, String permission) {
        return checkPermission(context, permission, Process.myPid(), Process.myUid(), context.getPackageName());
    }

    public static synchronized int checkCallingPermission(Context context, String permission, String packageName) {
        if (Binder.getCallingPid() == Process.myPid()) {
            return -1;
        }
        return checkPermission(context, permission, Binder.getCallingPid(), Binder.getCallingUid(), packageName);
    }

    public static synchronized int checkCallingOrSelfPermission(Context context, String permission) {
        String packageName = Binder.getCallingPid() == Process.myPid() ? context.getPackageName() : null;
        return checkPermission(context, permission, Binder.getCallingPid(), Binder.getCallingUid(), packageName);
    }
}
