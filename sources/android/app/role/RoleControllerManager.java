package android.app.role;

import android.app.ActivityThread;
import android.app.role.IRoleController;
import android.app.role.RoleControllerManager;
import android.app.role.RoleManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService;
import com.android.internal.infra.AbstractRemoteService;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class RoleControllerManager {
    private static volatile ComponentName sRemoteServiceComponentName;
    private final RemoteService mRemoteService;
    private static final String LOG_TAG = RoleControllerManager.class.getSimpleName();
    private static final Object sRemoteServicesLock = new Object();
    @GuardedBy({"sRemoteServicesLock"})
    private static final SparseArray<RemoteService> sRemoteServices = new SparseArray<>();

    public static void initializeRemoteServiceComponentName(Context context) {
        sRemoteServiceComponentName = getRemoteServiceComponentName(context);
    }

    public static RoleControllerManager createWithInitializedRemoteServiceComponentName(Handler handler, Context context) {
        return new RoleControllerManager(sRemoteServiceComponentName, handler, context);
    }

    private RoleControllerManager(ComponentName remoteServiceComponentName, Handler handler, Context context) {
        synchronized (sRemoteServicesLock) {
            int userId = context.getUserId();
            RemoteService remoteService = sRemoteServices.get(userId);
            if (remoteService == null) {
                remoteService = new RemoteService(ActivityThread.currentApplication(), remoteServiceComponentName, handler, userId);
                sRemoteServices.put(userId, remoteService);
            }
            this.mRemoteService = remoteService;
        }
    }

    public RoleControllerManager(Context context) {
        this(getRemoteServiceComponentName(context), context.getMainThreadHandler(), context);
    }

    private static ComponentName getRemoteServiceComponentName(Context context) {
        Intent intent = new Intent(RoleControllerService.SERVICE_INTERFACE);
        PackageManager packageManager = context.getPackageManager();
        intent.setPackage(packageManager.getPermissionControllerPackageName());
        ResolveInfo resolveInfo = packageManager.resolveService(intent, 0);
        return resolveInfo.getComponentInfo().getComponentName();
    }

    public void grantDefaultRoles(Executor executor, Consumer<Boolean> callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new GrantDefaultRolesRequest(remoteService, executor, callback));
    }

    public void onAddRoleHolder(String roleName, String packageName, @RoleManager.ManageHoldersFlags int flags, RemoteCallback callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new OnAddRoleHolderRequest(remoteService, roleName, packageName, flags, callback));
    }

    public void onRemoveRoleHolder(String roleName, String packageName, @RoleManager.ManageHoldersFlags int flags, RemoteCallback callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new OnRemoveRoleHolderRequest(remoteService, roleName, packageName, flags, callback));
    }

    public void onClearRoleHolders(String roleName, @RoleManager.ManageHoldersFlags int flags, RemoteCallback callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new OnClearRoleHoldersRequest(remoteService, roleName, flags, callback));
    }

    public void isApplicationQualifiedForRole(String roleName, String packageName, Executor executor, Consumer<Boolean> callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new IsApplicationQualifiedForRoleRequest(remoteService, roleName, packageName, executor, callback));
    }

    public void isRoleVisible(String roleName, Executor executor, Consumer<Boolean> callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new IsRoleVisibleRequest(remoteService, roleName, executor, callback));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class RemoteService extends AbstractMultiplePendingRequestsRemoteService<RemoteService, IRoleController> {
        private static final long REQUEST_TIMEOUT_MILLIS = 15000;
        private static final long UNBIND_DELAY_MILLIS = 15000;

        RemoteService(Context context, ComponentName componentName, Handler handler, int userId) {
            super(context, RoleControllerService.SERVICE_INTERFACE, componentName, userId, new AbstractRemoteService.VultureCallback() { // from class: android.app.role.-$$Lambda$RoleControllerManager$RemoteService$45dMO3SdHJhfBB_YKrC44Sznmoo
                @Override // com.android.internal.infra.AbstractRemoteService.VultureCallback
                public final void onServiceDied(Object obj) {
                    RoleControllerManager.RemoteService.lambda$new$0((RoleControllerManager.RemoteService) obj);
                }
            }, handler, 0, false, 1);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$new$0(RemoteService service) {
            String str = RoleControllerManager.LOG_TAG;
            Log.e(str, "RemoteService " + service + " died");
        }

        public Handler getHandler() {
            return this.mHandler;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService
        public IRoleController getServiceInterface(IBinder binder) {
            return IRoleController.Stub.asInterface(binder);
        }

        @Override // com.android.internal.infra.AbstractRemoteService
        protected long getTimeoutIdleBindMillis() {
            return 15000L;
        }

        @Override // com.android.internal.infra.AbstractRemoteService
        protected long getRemoteRequestMillis() {
            return 15000L;
        }

        @Override // com.android.internal.infra.AbstractRemoteService
        public void scheduleRequest(AbstractRemoteService.BasePendingRequest<RemoteService, IRoleController> pendingRequest) {
            super.scheduleRequest(pendingRequest);
        }

        @Override // com.android.internal.infra.AbstractRemoteService
        public void scheduleAsyncRequest(AbstractRemoteService.AsyncRequest<IRoleController> request) {
            super.scheduleAsyncRequest(request);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class GrantDefaultRolesRequest extends AbstractRemoteService.PendingRequest<RemoteService, IRoleController> {
        private final Consumer<Boolean> mCallback;
        private final Executor mExecutor;
        private final RemoteCallback mRemoteCallback;

        private GrantDefaultRolesRequest(RemoteService service, Executor executor, Consumer<Boolean> callback) {
            super(service);
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.app.role.-$$Lambda$RoleControllerManager$GrantDefaultRolesRequest$uMND2yv3BzXWyrtureF8K8b0f0A
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    RoleControllerManager.GrantDefaultRolesRequest.this.lambda$new$1$RoleControllerManager$GrantDefaultRolesRequest(bundle);
                }
            });
        }

        public /* synthetic */ void lambda$new$1$RoleControllerManager$GrantDefaultRolesRequest(final Bundle result) {
            this.mExecutor.execute(new Runnable() { // from class: android.app.role.-$$Lambda$RoleControllerManager$GrantDefaultRolesRequest$Qrnu382yknLH4_TvruMvYuK_N8M
                @Override // java.lang.Runnable
                public final void run() {
                    RoleControllerManager.GrantDefaultRolesRequest.this.lambda$new$0$RoleControllerManager$GrantDefaultRolesRequest(result);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$GrantDefaultRolesRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            boolean successful = result != null;
            try {
                this.mCallback.accept(Boolean.valueOf(successful));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$RoleControllerManager$GrantDefaultRolesRequest() {
            this.mCallback.accept(false);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            this.mExecutor.execute(new Runnable() { // from class: android.app.role.-$$Lambda$RoleControllerManager$GrantDefaultRolesRequest$0iOorSSTMKMxorImfJcxQ8hscBs
                @Override // java.lang.Runnable
                public final void run() {
                    RoleControllerManager.GrantDefaultRolesRequest.this.lambda$onTimeout$2$RoleControllerManager$GrantDefaultRolesRequest();
                }
            });
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).grantDefaultRoles(this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling grantDefaultRoles()", e);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.BasePendingRequest
        public void onFailed() {
            this.mRemoteCallback.sendResult(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OnAddRoleHolderRequest extends AbstractRemoteService.PendingRequest<RemoteService, IRoleController> {
        private final RemoteCallback mCallback;
        @RoleManager.ManageHoldersFlags
        private final int mFlags;
        private final String mPackageName;
        private final RemoteCallback mRemoteCallback;
        private final String mRoleName;

        private OnAddRoleHolderRequest(RemoteService service, String roleName, String packageName, @RoleManager.ManageHoldersFlags int flags, RemoteCallback callback) {
            super(service);
            this.mRoleName = roleName;
            this.mPackageName = packageName;
            this.mFlags = flags;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.app.role.-$$Lambda$RoleControllerManager$OnAddRoleHolderRequest$JT1k7eyE31b1Ili2aD3HPTU4d_Y
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    RoleControllerManager.OnAddRoleHolderRequest.this.lambda$new$0$RoleControllerManager$OnAddRoleHolderRequest(bundle);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$OnAddRoleHolderRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mCallback.sendResult(result);
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.sendResult(null);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).onAddRoleHolder(this.mRoleName, this.mPackageName, this.mFlags, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling onAddRoleHolder()", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OnRemoveRoleHolderRequest extends AbstractRemoteService.PendingRequest<RemoteService, IRoleController> {
        private final RemoteCallback mCallback;
        @RoleManager.ManageHoldersFlags
        private final int mFlags;
        private final String mPackageName;
        private final RemoteCallback mRemoteCallback;
        private final String mRoleName;

        private OnRemoveRoleHolderRequest(RemoteService service, String roleName, String packageName, @RoleManager.ManageHoldersFlags int flags, RemoteCallback callback) {
            super(service);
            this.mRoleName = roleName;
            this.mPackageName = packageName;
            this.mFlags = flags;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.app.role.-$$Lambda$RoleControllerManager$OnRemoveRoleHolderRequest$LtJIC2bE0p8jKF_FXl69Scqp5HE
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    RoleControllerManager.OnRemoveRoleHolderRequest.this.lambda$new$0$RoleControllerManager$OnRemoveRoleHolderRequest(bundle);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$OnRemoveRoleHolderRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mCallback.sendResult(result);
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.sendResult(null);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).onRemoveRoleHolder(this.mRoleName, this.mPackageName, this.mFlags, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling onRemoveRoleHolder()", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OnClearRoleHoldersRequest extends AbstractRemoteService.PendingRequest<RemoteService, IRoleController> {
        private final RemoteCallback mCallback;
        @RoleManager.ManageHoldersFlags
        private final int mFlags;
        private final RemoteCallback mRemoteCallback;
        private final String mRoleName;

        private OnClearRoleHoldersRequest(RemoteService service, String roleName, @RoleManager.ManageHoldersFlags int flags, RemoteCallback callback) {
            super(service);
            this.mRoleName = roleName;
            this.mFlags = flags;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.app.role.-$$Lambda$RoleControllerManager$OnClearRoleHoldersRequest$WFtkA3AVOOzGz5tXwMpks5Iic-o
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    RoleControllerManager.OnClearRoleHoldersRequest.this.lambda$new$0$RoleControllerManager$OnClearRoleHoldersRequest(bundle);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$OnClearRoleHoldersRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mCallback.sendResult(result);
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.sendResult(null);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).onClearRoleHolders(this.mRoleName, this.mFlags, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling onClearRoleHolders()", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class IsApplicationQualifiedForRoleRequest extends AbstractRemoteService.PendingRequest<RemoteService, IRoleController> {
        private final Consumer<Boolean> mCallback;
        private final Executor mExecutor;
        private final String mPackageName;
        private final RemoteCallback mRemoteCallback;
        private final String mRoleName;

        private IsApplicationQualifiedForRoleRequest(RemoteService service, String roleName, String packageName, Executor executor, Consumer<Boolean> callback) {
            super(service);
            this.mRoleName = roleName;
            this.mPackageName = packageName;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.app.role.-$$Lambda$RoleControllerManager$IsApplicationQualifiedForRoleRequest$YqB5KyJlcDUM5urf3ImMD1odxhI
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    RoleControllerManager.IsApplicationQualifiedForRoleRequest.this.lambda$new$1$RoleControllerManager$IsApplicationQualifiedForRoleRequest(bundle);
                }
            });
        }

        public /* synthetic */ void lambda$new$1$RoleControllerManager$IsApplicationQualifiedForRoleRequest(final Bundle result) {
            this.mExecutor.execute(new Runnable() { // from class: android.app.role.-$$Lambda$RoleControllerManager$IsApplicationQualifiedForRoleRequest$pbhRqekkSEnYlxVcT_rMcU6hV-E
                @Override // java.lang.Runnable
                public final void run() {
                    RoleControllerManager.IsApplicationQualifiedForRoleRequest.this.lambda$new$0$RoleControllerManager$IsApplicationQualifiedForRoleRequest(result);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$IsApplicationQualifiedForRoleRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            boolean qualified = result != null;
            try {
                this.mCallback.accept(Boolean.valueOf(qualified));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$RoleControllerManager$IsApplicationQualifiedForRoleRequest() {
            this.mCallback.accept(false);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            this.mExecutor.execute(new Runnable() { // from class: android.app.role.-$$Lambda$RoleControllerManager$IsApplicationQualifiedForRoleRequest$9YPce2vGDOZP97XHsgR7kBf64jQ
                @Override // java.lang.Runnable
                public final void run() {
                    RoleControllerManager.IsApplicationQualifiedForRoleRequest.this.lambda$onTimeout$2$RoleControllerManager$IsApplicationQualifiedForRoleRequest();
                }
            });
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).isApplicationQualifiedForRole(this.mRoleName, this.mPackageName, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling isApplicationQualifiedForRole()", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class IsRoleVisibleRequest extends AbstractRemoteService.PendingRequest<RemoteService, IRoleController> {
        private final Consumer<Boolean> mCallback;
        private final Executor mExecutor;
        private final RemoteCallback mRemoteCallback;
        private final String mRoleName;

        private IsRoleVisibleRequest(RemoteService service, String roleName, Executor executor, Consumer<Boolean> callback) {
            super(service);
            this.mRoleName = roleName;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.app.role.-$$Lambda$RoleControllerManager$IsRoleVisibleRequest$oEPzdmOwBqsdvIknZm3f9_oOiE8
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    RoleControllerManager.IsRoleVisibleRequest.this.lambda$new$1$RoleControllerManager$IsRoleVisibleRequest(bundle);
                }
            });
        }

        public /* synthetic */ void lambda$new$1$RoleControllerManager$IsRoleVisibleRequest(final Bundle result) {
            this.mExecutor.execute(new Runnable() { // from class: android.app.role.-$$Lambda$RoleControllerManager$IsRoleVisibleRequest$i7aWmxVK8GGR464ms-cqfIN7ou8
                @Override // java.lang.Runnable
                public final void run() {
                    RoleControllerManager.IsRoleVisibleRequest.this.lambda$new$0$RoleControllerManager$IsRoleVisibleRequest(result);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$IsRoleVisibleRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            boolean visible = result != null;
            try {
                this.mCallback.accept(Boolean.valueOf(visible));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$RoleControllerManager$IsRoleVisibleRequest() {
            this.mCallback.accept(false);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            this.mExecutor.execute(new Runnable() { // from class: android.app.role.-$$Lambda$RoleControllerManager$IsRoleVisibleRequest$mPvdI6Jc9sQbLKyjDLv3TR6mmlM
                @Override // java.lang.Runnable
                public final void run() {
                    RoleControllerManager.IsRoleVisibleRequest.this.lambda$onTimeout$2$RoleControllerManager$IsRoleVisibleRequest();
                }
            });
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).isRoleVisible(this.mRoleName, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling isRoleVisible()", e);
            }
        }
    }
}
