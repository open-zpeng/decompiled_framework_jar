package android.app.role;

import android.Manifest;
import android.annotation.SystemApi;
import android.app.Service;
import android.app.role.IRoleController;
import android.app.role.RoleManager;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteCallback;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.function.BiConsumer;

@SystemApi
/* loaded from: classes.dex */
public abstract class RoleControllerService extends Service {
    public static final String SERVICE_INTERFACE = "android.app.role.RoleControllerService";
    private Handler mWorkerHandler;
    private HandlerThread mWorkerThread;

    public abstract boolean onAddRoleHolder(String str, String str2, @RoleManager.ManageHoldersFlags int i);

    public abstract boolean onClearRoleHolders(String str, @RoleManager.ManageHoldersFlags int i);

    public abstract boolean onGrantDefaultRoles();

    public abstract boolean onIsApplicationQualifiedForRole(String str, String str2);

    public abstract boolean onIsRoleVisible(String str);

    public abstract boolean onRemoveRoleHolder(String str, String str2, @RoleManager.ManageHoldersFlags int i);

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mWorkerThread = new HandlerThread(RoleControllerService.class.getSimpleName());
        this.mWorkerThread.start();
        this.mWorkerHandler = new Handler(this.mWorkerThread.getLooper());
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.mWorkerThread.quitSafely();
    }

    /* renamed from: android.app.role.RoleControllerService$1  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass1 extends IRoleController.Stub {
        AnonymousClass1() {
        }

        @Override // android.app.role.IRoleController
        public void grantDefaultRoles(RemoteCallback callback) {
            enforceCallerSystemUid("grantDefaultRoles");
            Preconditions.checkNotNull(callback, "callback cannot be null");
            RoleControllerService.this.mWorkerHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: android.app.role.-$$Lambda$RoleControllerService$1$-fmj7uDKaG3BoLl6bhtrA675gRI
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((RoleControllerService) obj).grantDefaultRoles((RemoteCallback) obj2);
                }
            }, RoleControllerService.this, callback));
        }

        @Override // android.app.role.IRoleController
        public void onAddRoleHolder(String roleName, String packageName, int flags, RemoteCallback callback) {
            enforceCallerSystemUid("onAddRoleHolder");
            Preconditions.checkStringNotEmpty(roleName, "roleName cannot be null or empty");
            Preconditions.checkStringNotEmpty(packageName, "packageName cannot be null or empty");
            Preconditions.checkNotNull(callback, "callback cannot be null");
            RoleControllerService.this.mWorkerHandler.sendMessage(PooledLambda.obtainMessage(new QuintConsumer() { // from class: android.app.role.-$$Lambda$RoleControllerService$1$UVI1sAWAcBnt3Enqn2IT-Lirwtk
                @Override // com.android.internal.util.function.QuintConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                    ((RoleControllerService) obj).onAddRoleHolder((String) obj2, (String) obj3, ((Integer) obj4).intValue(), (RemoteCallback) obj5);
                }
            }, RoleControllerService.this, roleName, packageName, Integer.valueOf(flags), callback));
        }

        @Override // android.app.role.IRoleController
        public void onRemoveRoleHolder(String roleName, String packageName, int flags, RemoteCallback callback) {
            enforceCallerSystemUid("onRemoveRoleHolder");
            Preconditions.checkStringNotEmpty(roleName, "roleName cannot be null or empty");
            Preconditions.checkStringNotEmpty(packageName, "packageName cannot be null or empty");
            Preconditions.checkNotNull(callback, "callback cannot be null");
            RoleControllerService.this.mWorkerHandler.sendMessage(PooledLambda.obtainMessage(new QuintConsumer() { // from class: android.app.role.-$$Lambda$RoleControllerService$1$PB6H1df6VvLzUJ3hhB_75mN3u7s
                @Override // com.android.internal.util.function.QuintConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                    ((RoleControllerService) obj).onRemoveRoleHolder((String) obj2, (String) obj3, ((Integer) obj4).intValue(), (RemoteCallback) obj5);
                }
            }, RoleControllerService.this, roleName, packageName, Integer.valueOf(flags), callback));
        }

        @Override // android.app.role.IRoleController
        public void onClearRoleHolders(String roleName, int flags, RemoteCallback callback) {
            enforceCallerSystemUid("onClearRoleHolders");
            Preconditions.checkStringNotEmpty(roleName, "roleName cannot be null or empty");
            Preconditions.checkNotNull(callback, "callback cannot be null");
            RoleControllerService.this.mWorkerHandler.sendMessage(PooledLambda.obtainMessage(new QuadConsumer() { // from class: android.app.role.-$$Lambda$RoleControllerService$1$dBm1t_MGyEA9yMAxoOUMOhYVmPo
                @Override // com.android.internal.util.function.QuadConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                    ((RoleControllerService) obj).onClearRoleHolders((String) obj2, ((Integer) obj3).intValue(), (RemoteCallback) obj4);
                }
            }, RoleControllerService.this, roleName, Integer.valueOf(flags), callback));
        }

        private void enforceCallerSystemUid(String methodName) {
            if (Binder.getCallingUid() != 1000) {
                throw new SecurityException("Only the system process can call " + methodName + "()");
            }
        }

        @Override // android.app.role.IRoleController
        public void isApplicationQualifiedForRole(String roleName, String packageName, RemoteCallback callback) {
            RoleControllerService.this.enforceCallingPermission(Manifest.permission.MANAGE_ROLE_HOLDERS, null);
            Preconditions.checkStringNotEmpty(roleName, "roleName cannot be null or empty");
            Preconditions.checkStringNotEmpty(packageName, "packageName cannot be null or empty");
            Preconditions.checkNotNull(callback, "callback cannot be null");
            boolean qualified = RoleControllerService.this.onIsApplicationQualifiedForRole(roleName, packageName);
            callback.sendResult(qualified ? Bundle.EMPTY : null);
        }

        @Override // android.app.role.IRoleController
        public void isRoleVisible(String roleName, RemoteCallback callback) {
            RoleControllerService.this.enforceCallingPermission(Manifest.permission.MANAGE_ROLE_HOLDERS, null);
            Preconditions.checkStringNotEmpty(roleName, "roleName cannot be null or empty");
            Preconditions.checkNotNull(callback, "callback cannot be null");
            boolean visible = RoleControllerService.this.onIsRoleVisible(roleName);
            callback.sendResult(visible ? Bundle.EMPTY : null);
        }
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return new AnonymousClass1();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantDefaultRoles(RemoteCallback callback) {
        boolean successful = onGrantDefaultRoles();
        callback.sendResult(successful ? Bundle.EMPTY : null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAddRoleHolder(String roleName, String packageName, @RoleManager.ManageHoldersFlags int flags, RemoteCallback callback) {
        boolean successful = onAddRoleHolder(roleName, packageName, flags);
        callback.sendResult(successful ? Bundle.EMPTY : null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRemoveRoleHolder(String roleName, String packageName, @RoleManager.ManageHoldersFlags int flags, RemoteCallback callback) {
        boolean successful = onRemoveRoleHolder(roleName, packageName, flags);
        callback.sendResult(successful ? Bundle.EMPTY : null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onClearRoleHolders(String roleName, @RoleManager.ManageHoldersFlags int flags, RemoteCallback callback) {
        boolean successful = onClearRoleHolders(roleName, flags);
        callback.sendResult(successful ? Bundle.EMPTY : null);
    }
}
