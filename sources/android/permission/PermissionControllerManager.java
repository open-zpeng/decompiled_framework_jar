package android.permission;

import android.Manifest;
import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.UserHandle;
import android.permission.IPermissionController;
import android.permission.PermissionControllerManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService;
import com.android.internal.infra.AbstractRemoteService;
import com.android.internal.util.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import libcore.io.IoUtils;

@SystemApi
/* loaded from: classes2.dex */
public final class PermissionControllerManager {
    public static final int COUNT_ONLY_WHEN_GRANTED = 1;
    public static final int COUNT_WHEN_SYSTEM = 2;
    public static final String KEY_RESULT = "android.permission.PermissionControllerManager.key.result";
    public static final int REASON_INSTALLER_POLICY_VIOLATION = 2;
    public static final int REASON_MALWARE = 1;
    private static final String TAG = PermissionControllerManager.class.getSimpleName();
    private static final Object sLock = new Object();
    @GuardedBy({"sLock"})
    private static ArrayMap<Pair<Integer, Thread>, RemoteService> sRemoteServices = new ArrayMap<>(1);
    private final Context mContext;
    private final RemoteService mRemoteService;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface CountPermissionAppsFlag {
    }

    /* loaded from: classes2.dex */
    public interface OnCountPermissionAppsResultCallback {
        void onCountPermissionApps(int i);
    }

    /* loaded from: classes2.dex */
    public interface OnGetAppPermissionResultCallback {
        void onGetAppPermissions(List<RuntimePermissionPresentationInfo> list);
    }

    /* loaded from: classes2.dex */
    public interface OnGetRuntimePermissionBackupCallback {
        void onGetRuntimePermissionsBackup(byte[] bArr);
    }

    /* loaded from: classes2.dex */
    public interface OnPermissionUsageResultCallback {
        void onPermissionUsageResult(List<RuntimePermissionUsageInfo> list);
    }

    /* loaded from: classes2.dex */
    public static abstract class OnRevokeRuntimePermissionsCallback {
        public abstract void onRevokeRuntimePermissions(Map<String, List<String>> map);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Reason {
    }

    public PermissionControllerManager(Context context, Handler handler) {
        synchronized (sLock) {
            Pair<Integer, Thread> key = new Pair<>(Integer.valueOf(context.getUserId()), handler.getLooper().getThread());
            RemoteService remoteService = sRemoteServices.get(key);
            if (remoteService == null) {
                Intent intent = new Intent(PermissionControllerService.SERVICE_INTERFACE);
                intent.setPackage(context.getPackageManager().getPermissionControllerPackageName());
                ResolveInfo serviceInfo = context.getPackageManager().resolveService(intent, 0);
                remoteService = new RemoteService(ActivityThread.currentApplication(), serviceInfo.getComponentInfo().getComponentName(), handler, context.getUser());
                sRemoteServices.put(key, remoteService);
            }
            this.mRemoteService = remoteService;
        }
        this.mContext = context;
    }

    public void revokeRuntimePermissions(Map<String, List<String>> request, boolean doDryRun, int reason, Executor executor, OnRevokeRuntimePermissionsCallback callback) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(callback);
        Preconditions.checkNotNull(request);
        for (Map.Entry<String, List<String>> appRequest : request.entrySet()) {
            Preconditions.checkNotNull(appRequest.getKey());
            Preconditions.checkCollectionElementsNotNull(appRequest.getValue(), "permissions");
        }
        if (this.mContext.checkSelfPermission(Manifest.permission.REVOKE_RUNTIME_PERMISSIONS) != 0) {
            throw new SecurityException("android.permission.REVOKE_RUNTIME_PERMISSIONS required");
        }
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingRevokeRuntimePermissionRequest(remoteService, request, doDryRun, reason, this.mContext.getPackageName(), executor, callback));
    }

    public void setRuntimePermissionGrantStateByDeviceAdmin(String callerPackageName, String packageName, String permission, int grantState, Executor executor, Consumer<Boolean> callback) {
        Preconditions.checkStringNotEmpty(callerPackageName);
        Preconditions.checkStringNotEmpty(packageName);
        Preconditions.checkStringNotEmpty(permission);
        boolean z = true;
        if (grantState != 1 && grantState != 2 && grantState != 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingSetRuntimePermissionGrantStateByDeviceAdmin(remoteService, callerPackageName, packageName, permission, grantState, executor, callback));
    }

    public void getRuntimePermissionBackup(UserHandle user, Executor executor, OnGetRuntimePermissionBackupCallback callback) {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingGetRuntimePermissionBackup(remoteService, user, executor, callback));
    }

    public void restoreRuntimePermissionBackup(byte[] backup, UserHandle user) {
        Preconditions.checkNotNull(backup);
        Preconditions.checkNotNull(user);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleAsyncRequest(new PendingRestoreRuntimePermissionBackup(remoteService, backup, user));
    }

    public void restoreDelayedRuntimePermissionBackup(String packageName, UserHandle user, Executor executor, Consumer<Boolean> callback) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingRestoreDelayedRuntimePermissionBackup(remoteService, packageName, user, executor, callback));
    }

    public void getAppPermissions(String packageName, OnGetAppPermissionResultCallback callback, Handler handler) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingGetAppPermissionRequest(remoteService, packageName, callback, handler == null ? remoteService.getHandler() : handler));
    }

    public void revokeRuntimePermission(String packageName, String permissionName) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(permissionName);
        this.mRemoteService.scheduleAsyncRequest(new PendingRevokeAppPermissionRequest(packageName, permissionName));
    }

    public void countPermissionApps(List<String> permissionNames, int flags, OnCountPermissionAppsResultCallback callback, Handler handler) {
        Preconditions.checkCollectionElementsNotNull(permissionNames, "permissionNames");
        Preconditions.checkFlagsArgument(flags, 3);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingCountPermissionAppsRequest(remoteService, permissionNames, flags, callback, handler == null ? remoteService.getHandler() : handler));
    }

    public void getPermissionUsages(boolean countSystem, long numMillis, Executor executor, OnPermissionUsageResultCallback callback) {
        Preconditions.checkArgumentNonnegative(numMillis);
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingGetPermissionUsagesRequest(remoteService, countSystem, numMillis, executor, callback));
    }

    public void grantOrUpgradeDefaultRuntimePermissions(Executor executor, Consumer<Boolean> callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(remoteService, executor, callback));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class RemoteService extends AbstractMultiplePendingRequestsRemoteService<RemoteService, IPermissionController> {
        private static final long MESSAGE_TIMEOUT_MILLIS = 30000;
        private static final long UNBIND_TIMEOUT_MILLIS = 10000;

        RemoteService(Context context, ComponentName componentName, Handler handler, UserHandle user) {
            super(context, PermissionControllerService.SERVICE_INTERFACE, componentName, user.getIdentifier(), new AbstractRemoteService.VultureCallback() { // from class: android.permission.-$$Lambda$PermissionControllerManager$RemoteService$L8N-TbqIPWKu7tyiOxbu_00YKss
                @Override // com.android.internal.infra.AbstractRemoteService.VultureCallback
                public final void onServiceDied(Object obj) {
                    PermissionControllerManager.RemoteService.lambda$new$0((PermissionControllerManager.RemoteService) obj);
                }
            }, handler, 0, false, 1);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$new$0(RemoteService service) {
            String str = PermissionControllerManager.TAG;
            Log.e(str, "RemoteService " + service + " died");
        }

        Handler getHandler() {
            return this.mHandler;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService
        public IPermissionController getServiceInterface(IBinder binder) {
            return IPermissionController.Stub.asInterface(binder);
        }

        @Override // com.android.internal.infra.AbstractRemoteService
        protected long getTimeoutIdleBindMillis() {
            return 10000L;
        }

        @Override // com.android.internal.infra.AbstractRemoteService
        protected long getRemoteRequestMillis() {
            return 30000L;
        }

        @Override // com.android.internal.infra.AbstractRemoteService
        public void scheduleRequest(AbstractRemoteService.BasePendingRequest<RemoteService, IPermissionController> pendingRequest) {
            super.scheduleRequest(pendingRequest);
        }

        @Override // com.android.internal.infra.AbstractRemoteService
        public void scheduleAsyncRequest(AbstractRemoteService.AsyncRequest<IPermissionController> request) {
            super.scheduleAsyncRequest(request);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class FileReaderTask<Callback extends Consumer<byte[]>> extends AsyncTask<Void, Void, byte[]> {
        private final Callback mCallback;
        private ParcelFileDescriptor mLocalPipe;
        private ParcelFileDescriptor mRemotePipe;

        FileReaderTask(Callback callback) {
            this.mCallback = callback;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            try {
                ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
                this.mLocalPipe = pipe[0];
                this.mRemotePipe = pipe[1];
            } catch (IOException e) {
                Log.e(PermissionControllerManager.TAG, "Could not create pipe needed to get runtime permission backup", e);
            }
        }

        ParcelFileDescriptor getRemotePipe() {
            return this.mRemotePipe;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public byte[] doInBackground(Void... ignored) {
            int numRead;
            ByteArrayOutputStream combinedBuffer = new ByteArrayOutputStream();
            try {
                InputStream in = new ParcelFileDescriptor.AutoCloseInputStream(this.mLocalPipe);
                byte[] buffer = new byte[16384];
                while (!isCancelled() && (numRead = in.read(buffer)) != -1) {
                    combinedBuffer.write(buffer, 0, numRead);
                }
                in.close();
            } catch (IOException | NullPointerException e) {
                Log.e(PermissionControllerManager.TAG, "Error reading runtime permission backup", e);
                combinedBuffer.reset();
            }
            return combinedBuffer.toByteArray();
        }

        void interruptRead() {
            IoUtils.closeQuietly(this.mLocalPipe);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            onPostExecute(new byte[0]);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(byte[] backup) {
            IoUtils.closeQuietly(this.mLocalPipe);
            this.mCallback.accept(backup);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class FileWriterTask extends AsyncTask<byte[], Void, Void> {
        private static final int CHUNK_SIZE = 4096;
        private ParcelFileDescriptor mLocalPipe;
        private ParcelFileDescriptor mRemotePipe;

        private FileWriterTask() {
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            try {
                ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
                this.mRemotePipe = pipe[0];
                this.mLocalPipe = pipe[1];
            } catch (IOException e) {
                Log.e(PermissionControllerManager.TAG, "Could not create pipe needed to send runtime permission backup", e);
            }
        }

        ParcelFileDescriptor getRemotePipe() {
            return this.mRemotePipe;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(byte[]... in) {
            byte[] buffer = in[0];
            try {
                OutputStream out = new ParcelFileDescriptor.AutoCloseOutputStream(this.mLocalPipe);
                for (int offset = 0; offset < buffer.length; offset += 4096) {
                    out.write(buffer, offset, Math.min(4096, buffer.length - offset));
                }
                out.close();
                return null;
            } catch (IOException | NullPointerException e) {
                Log.e(PermissionControllerManager.TAG, "Error sending runtime permission backup", e);
                return null;
            }
        }

        void interruptWrite() {
            IoUtils.closeQuietly(this.mLocalPipe);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            onPostExecute((Void) null);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void ignored) {
            IoUtils.closeQuietly(this.mLocalPipe);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class PendingRevokeRuntimePermissionRequest extends AbstractRemoteService.PendingRequest<RemoteService, IPermissionController> {
        private final OnRevokeRuntimePermissionsCallback mCallback;
        private final String mCallingPackage;
        private final boolean mDoDryRun;
        private final Executor mExecutor;
        private final int mReason;
        private final RemoteCallback mRemoteCallback;
        private final Map<String, List<String>> mRequest;

        private PendingRevokeRuntimePermissionRequest(RemoteService service, Map<String, List<String>> request, boolean doDryRun, int reason, String callingPackage, final Executor executor, final OnRevokeRuntimePermissionsCallback callback) {
            super(service);
            this.mRequest = request;
            this.mDoDryRun = doDryRun;
            this.mReason = reason;
            this.mCallingPackage = callingPackage;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingRevokeRuntimePermissionRequest$StUWUj0fmNRuCwuUzh3M5C7e_o0
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    PermissionControllerManager.PendingRevokeRuntimePermissionRequest.this.lambda$new$1$PermissionControllerManager$PendingRevokeRuntimePermissionRequest(executor, callback, bundle);
                }
            }, null);
        }

        public /* synthetic */ void lambda$new$1$PermissionControllerManager$PendingRevokeRuntimePermissionRequest(Executor executor, final OnRevokeRuntimePermissionsCallback callback, final Bundle result) {
            executor.execute(new Runnable() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingRevokeRuntimePermissionRequest$RY69_9rYfdoaXdLj_Ux-62tZUXg
                @Override // java.lang.Runnable
                public final void run() {
                    PermissionControllerManager.PendingRevokeRuntimePermissionRequest.this.lambda$new$0$PermissionControllerManager$PendingRevokeRuntimePermissionRequest(result, callback);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingRevokeRuntimePermissionRequest(Bundle result, OnRevokeRuntimePermissionsCallback callback) {
            long token = Binder.clearCallingIdentity();
            try {
                Map<String, List<String>> revoked = new ArrayMap<>();
                try {
                    Bundle bundleizedRevoked = result.getBundle(PermissionControllerManager.KEY_RESULT);
                    for (String packageName : bundleizedRevoked.keySet()) {
                        Preconditions.checkNotNull(packageName);
                        ArrayList<String> permissions = bundleizedRevoked.getStringArrayList(packageName);
                        Preconditions.checkCollectionElementsNotNull(permissions, "permissions");
                        revoked.put(packageName, permissions);
                    }
                } catch (Exception e) {
                    Log.e(PermissionControllerManager.TAG, "Could not read result when revoking runtime permissions", e);
                }
                callback.onRevokeRuntimePermissions(revoked);
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingRevokeRuntimePermissionRequest$HQXgA6xx0k7jv6y22RQn3Fx34QQ
                    @Override // java.lang.Runnable
                    public final void run() {
                        PermissionControllerManager.PendingRevokeRuntimePermissionRequest.this.lambda$onTimeout$2$PermissionControllerManager$PendingRevokeRuntimePermissionRequest();
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$PermissionControllerManager$PendingRevokeRuntimePermissionRequest() {
            this.mCallback.onRevokeRuntimePermissions(Collections.emptyMap());
        }

        @Override // java.lang.Runnable
        public void run() {
            Bundle bundledizedRequest = new Bundle();
            for (Map.Entry<String, List<String>> appRequest : this.mRequest.entrySet()) {
                bundledizedRequest.putStringArrayList(appRequest.getKey(), new ArrayList<>(appRequest.getValue()));
            }
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).revokeRuntimePermissions(bundledizedRequest, this.mDoDryRun, this.mReason, this.mCallingPackage, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error revoking runtime permission", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class PendingGetRuntimePermissionBackup extends AbstractRemoteService.PendingRequest<RemoteService, IPermissionController> implements Consumer<byte[]> {
        private final FileReaderTask<PendingGetRuntimePermissionBackup> mBackupReader;
        private final OnGetRuntimePermissionBackupCallback mCallback;
        private final Executor mExecutor;
        private final UserHandle mUser;

        private PendingGetRuntimePermissionBackup(RemoteService service, UserHandle user, Executor executor, OnGetRuntimePermissionBackupCallback callback) {
            super(service);
            this.mUser = user;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mBackupReader = new FileReaderTask<>(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            this.mBackupReader.cancel(true);
            this.mBackupReader.interruptRead();
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mBackupReader.getStatus() != AsyncTask.Status.PENDING) {
                return;
            }
            this.mBackupReader.execute(new Void[0]);
            ParcelFileDescriptor remotePipe = this.mBackupReader.getRemotePipe();
            try {
                try {
                    ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).getRuntimePermissionBackup(this.mUser, remotePipe);
                } catch (RemoteException e) {
                    Log.e(PermissionControllerManager.TAG, "Error getting runtime permission backup", e);
                }
            } finally {
                IoUtils.closeQuietly(remotePipe);
            }
        }

        @Override // java.util.function.Consumer
        public void accept(final byte[] backup) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingGetRuntimePermissionBackup$TnLX6gxZCMF3D0czwj_XwNhPIgE
                    @Override // java.lang.Runnable
                    public final void run() {
                        PermissionControllerManager.PendingGetRuntimePermissionBackup.this.lambda$accept$0$PermissionControllerManager$PendingGetRuntimePermissionBackup(backup);
                    }
                });
                Binder.restoreCallingIdentity(token);
                finish();
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(token);
                throw th;
            }
        }

        public /* synthetic */ void lambda$accept$0$PermissionControllerManager$PendingGetRuntimePermissionBackup(byte[] backup) {
            this.mCallback.onGetRuntimePermissionsBackup(backup);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class PendingSetRuntimePermissionGrantStateByDeviceAdmin extends AbstractRemoteService.PendingRequest<RemoteService, IPermissionController> {
        private final Consumer<Boolean> mCallback;
        private final String mCallerPackageName;
        private final Executor mExecutor;
        private final int mGrantState;
        private final String mPackageName;
        private final String mPermission;
        private final RemoteCallback mRemoteCallback;

        private PendingSetRuntimePermissionGrantStateByDeviceAdmin(RemoteService service, String callerPackageName, String packageName, String permission, int grantState, final Executor executor, final Consumer<Boolean> callback) {
            super(service);
            this.mCallerPackageName = callerPackageName;
            this.mPackageName = packageName;
            this.mPermission = permission;
            this.mGrantState = grantState;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin$9CrKvc4Mj43M641VzAbk1z_vjck
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    PermissionControllerManager.PendingSetRuntimePermissionGrantStateByDeviceAdmin.this.lambda$new$1$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin(executor, callback, bundle);
                }
            }, null);
        }

        public /* synthetic */ void lambda$new$1$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin(Executor executor, final Consumer callback, final Bundle result) {
            executor.execute(new Runnable() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin$L3EtiNpasfEGf-E2sSUKhk-dYUg
                @Override // java.lang.Runnable
                public final void run() {
                    PermissionControllerManager.PendingSetRuntimePermissionGrantStateByDeviceAdmin.this.lambda$new$0$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin(callback, result);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin(Consumer callback, Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                callback.accept(Boolean.valueOf(result.getBoolean(PermissionControllerManager.KEY_RESULT, false)));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin$cgbsG1socgf6wsJmCUAPmh-jKmw
                    @Override // java.lang.Runnable
                    public final void run() {
                        PermissionControllerManager.PendingSetRuntimePermissionGrantStateByDeviceAdmin.this.lambda$onTimeout$2$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin();
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin() {
            this.mCallback.accept(false);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).setRuntimePermissionGrantStateByDeviceAdmin(this.mCallerPackageName, this.mPackageName, this.mPermission, this.mGrantState, this.mRemoteCallback);
            } catch (RemoteException e) {
                String str = PermissionControllerManager.TAG;
                Log.e(str, "Error setting permissions state for device admin " + this.mPackageName, e);
            }
        }
    }

    /* loaded from: classes2.dex */
    private static final class PendingRestoreRuntimePermissionBackup implements AbstractRemoteService.AsyncRequest<IPermissionController> {
        private final byte[] mBackup;
        private final FileWriterTask mBackupSender;
        private final UserHandle mUser;

        private PendingRestoreRuntimePermissionBackup(RemoteService service, byte[] backup, UserHandle user) {
            this.mBackup = backup;
            this.mUser = user;
            this.mBackupSender = new FileWriterTask();
        }

        @Override // com.android.internal.infra.AbstractRemoteService.AsyncRequest
        public void run(IPermissionController service) {
            if (this.mBackupSender.getStatus() != AsyncTask.Status.PENDING) {
                return;
            }
            this.mBackupSender.execute(this.mBackup);
            ParcelFileDescriptor remotePipe = this.mBackupSender.getRemotePipe();
            try {
                try {
                    service.restoreRuntimePermissionBackup(this.mUser, remotePipe);
                } catch (RemoteException e) {
                    Log.e(PermissionControllerManager.TAG, "Error sending runtime permission backup", e);
                    this.mBackupSender.cancel(false);
                    this.mBackupSender.interruptWrite();
                }
            } finally {
                IoUtils.closeQuietly(remotePipe);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class PendingRestoreDelayedRuntimePermissionBackup extends AbstractRemoteService.PendingRequest<RemoteService, IPermissionController> {
        private final Consumer<Boolean> mCallback;
        private final Executor mExecutor;
        private final String mPackageName;
        private final RemoteCallback mRemoteCallback;
        private final UserHandle mUser;

        private PendingRestoreDelayedRuntimePermissionBackup(RemoteService service, String packageName, UserHandle user, final Executor executor, final Consumer<Boolean> callback) {
            super(service);
            this.mPackageName = packageName;
            this.mUser = user;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$S_BIiPaqfMH7CNqPH_RO6xHRCeQ
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    PermissionControllerManager.PendingRestoreDelayedRuntimePermissionBackup.this.lambda$new$1$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup(executor, callback, bundle);
                }
            }, null);
        }

        public /* synthetic */ void lambda$new$1$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup(Executor executor, final Consumer callback, final Bundle result) {
            executor.execute(new Runnable() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$ZGmiW-2RcTI6YZLE1JgWr0ufJGk
                @Override // java.lang.Runnable
                public final void run() {
                    PermissionControllerManager.PendingRestoreDelayedRuntimePermissionBackup.this.lambda$new$0$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup(callback, result);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup(Consumer callback, Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                callback.accept(Boolean.valueOf(result.getBoolean(PermissionControllerManager.KEY_RESULT, false)));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$eZmglu-5wkoNFQT0fHebFoNMze8
                    @Override // java.lang.Runnable
                    public final void run() {
                        PermissionControllerManager.PendingRestoreDelayedRuntimePermissionBackup.this.lambda$onTimeout$2$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup();
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup() {
            this.mCallback.accept(true);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).restoreDelayedRuntimePermissionBackup(this.mPackageName, this.mUser, this.mRemoteCallback);
            } catch (RemoteException e) {
                String str = PermissionControllerManager.TAG;
                Log.e(str, "Error restoring delayed permissions for " + this.mPackageName, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class PendingGetAppPermissionRequest extends AbstractRemoteService.PendingRequest<RemoteService, IPermissionController> {
        private final OnGetAppPermissionResultCallback mCallback;
        private final String mPackageName;
        private final RemoteCallback mRemoteCallback;

        private PendingGetAppPermissionRequest(RemoteService service, String packageName, final OnGetAppPermissionResultCallback callback, Handler handler) {
            super(service);
            this.mPackageName = packageName;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingGetAppPermissionRequest$7R0rGbvqPEHrjxlrMX66LMgfTj4
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    PermissionControllerManager.PendingGetAppPermissionRequest.this.lambda$new$0$PermissionControllerManager$PendingGetAppPermissionRequest(callback, bundle);
                }
            }, handler);
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingGetAppPermissionRequest(OnGetAppPermissionResultCallback callback, Bundle result) {
            List<RuntimePermissionPresentationInfo> permissions = null;
            if (result != null) {
                permissions = result.getParcelableArrayList(PermissionControllerManager.KEY_RESULT);
            }
            if (permissions == null) {
                permissions = Collections.emptyList();
            }
            List<RuntimePermissionPresentationInfo> reportedPermissions = permissions;
            callback.onGetAppPermissions(reportedPermissions);
            finish();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.onGetAppPermissions(Collections.emptyList());
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).getAppPermissions(this.mPackageName, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error getting app permission", e);
            }
        }
    }

    /* loaded from: classes2.dex */
    private static final class PendingRevokeAppPermissionRequest implements AbstractRemoteService.AsyncRequest<IPermissionController> {
        private final String mPackageName;
        private final String mPermissionName;

        private PendingRevokeAppPermissionRequest(String packageName, String permissionName) {
            this.mPackageName = packageName;
            this.mPermissionName = permissionName;
        }

        @Override // com.android.internal.infra.AbstractRemoteService.AsyncRequest
        public void run(IPermissionController remoteInterface) {
            try {
                remoteInterface.revokeRuntimePermission(this.mPackageName, this.mPermissionName);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error revoking app permission", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class PendingCountPermissionAppsRequest extends AbstractRemoteService.PendingRequest<RemoteService, IPermissionController> {
        private final OnCountPermissionAppsResultCallback mCallback;
        private final int mFlags;
        private final List<String> mPermissionNames;
        private final RemoteCallback mRemoteCallback;

        private PendingCountPermissionAppsRequest(RemoteService service, List<String> permissionNames, int flags, final OnCountPermissionAppsResultCallback callback, Handler handler) {
            super(service);
            this.mPermissionNames = permissionNames;
            this.mFlags = flags;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingCountPermissionAppsRequest$5yk4p2I96nUHJ1QRErjoF1iiLLY
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    PermissionControllerManager.PendingCountPermissionAppsRequest.this.lambda$new$0$PermissionControllerManager$PendingCountPermissionAppsRequest(callback, bundle);
                }
            }, handler);
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingCountPermissionAppsRequest(OnCountPermissionAppsResultCallback callback, Bundle result) {
            int numApps;
            if (result != null) {
                numApps = result.getInt(PermissionControllerManager.KEY_RESULT);
            } else {
                numApps = 0;
            }
            callback.onCountPermissionApps(numApps);
            finish();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.onCountPermissionApps(0);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).countPermissionApps(this.mPermissionNames, this.mFlags, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error counting permission apps", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class PendingGetPermissionUsagesRequest extends AbstractRemoteService.PendingRequest<RemoteService, IPermissionController> {
        private final OnPermissionUsageResultCallback mCallback;
        private final boolean mCountSystem;
        private final long mNumMillis;
        private final RemoteCallback mRemoteCallback;

        private PendingGetPermissionUsagesRequest(RemoteService service, boolean countSystem, long numMillis, final Executor executor, final OnPermissionUsageResultCallback callback) {
            super(service);
            this.mCountSystem = countSystem;
            this.mNumMillis = numMillis;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingGetPermissionUsagesRequest$M0RAdfneqBIIFQEhfWzd068mi7g
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    PermissionControllerManager.PendingGetPermissionUsagesRequest.this.lambda$new$1$PermissionControllerManager$PendingGetPermissionUsagesRequest(executor, callback, bundle);
                }
            }, null);
        }

        public /* synthetic */ void lambda$new$1$PermissionControllerManager$PendingGetPermissionUsagesRequest(Executor executor, final OnPermissionUsageResultCallback callback, final Bundle result) {
            executor.execute(new Runnable() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingGetPermissionUsagesRequest$WBIc65bpG47GE1DYeIzY6NX7Oyw
                @Override // java.lang.Runnable
                public final void run() {
                    PermissionControllerManager.PendingGetPermissionUsagesRequest.this.lambda$new$0$PermissionControllerManager$PendingGetPermissionUsagesRequest(result, callback);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingGetPermissionUsagesRequest(Bundle result, OnPermissionUsageResultCallback callback) {
            List<RuntimePermissionUsageInfo> users;
            long token = Binder.clearCallingIdentity();
            try {
                if (result != null) {
                    users = result.getParcelableArrayList(PermissionControllerManager.KEY_RESULT);
                } else {
                    users = Collections.emptyList();
                }
                List<RuntimePermissionUsageInfo> reportedUsers = users;
                callback.onPermissionUsageResult(reportedUsers);
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.onPermissionUsageResult(Collections.emptyList());
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).getPermissionUsages(this.mCountSystem, this.mNumMillis, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error counting permission users", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class PendingGrantOrUpgradeDefaultRuntimePermissionsRequest extends AbstractRemoteService.PendingRequest<RemoteService, IPermissionController> {
        private final Consumer<Boolean> mCallback;
        private final RemoteCallback mRemoteCallback;

        private PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(RemoteService service, final Executor executor, final Consumer<Boolean> callback) {
            super(service);
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest$khE8_2qLkPzjjwzPXI9vCg1JiSo
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    PermissionControllerManager.PendingGrantOrUpgradeDefaultRuntimePermissionsRequest.this.lambda$new$1$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(executor, callback, bundle);
                }
            }, null);
        }

        public /* synthetic */ void lambda$new$1$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(Executor executor, final Consumer callback, final Bundle result) {
            executor.execute(new Runnable() { // from class: android.permission.-$$Lambda$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest$LF2T0wqhyO211uMsePvWLLBRNHc
                @Override // java.lang.Runnable
                public final void run() {
                    PermissionControllerManager.PendingGrantOrUpgradeDefaultRuntimePermissionsRequest.this.lambda$new$0$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(callback, result);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(Consumer callback, Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                callback.accept(Boolean.valueOf(result != null));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.internal.infra.AbstractRemoteService.PendingRequest
        public void onTimeout(RemoteService remoteService) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mCallback.accept(false);
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).grantOrUpgradeDefaultRuntimePermissions(this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error granting or upgrading runtime permissions", e);
            }
        }
    }
}
