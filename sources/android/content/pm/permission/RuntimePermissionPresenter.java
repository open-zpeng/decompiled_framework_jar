package android.content.pm.permission;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.permission.IRuntimePermissionPresenter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.permissionpresenterservice.RuntimePermissionPresenterService;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.SomeArgs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public final class RuntimePermissionPresenter {
    public static final String KEY_RESULT = "android.content.pm.permission.RuntimePermissionPresenter.key.result";
    private static final String TAG = "RuntimePermPresenter";
    @GuardedBy("sLock")
    private static RuntimePermissionPresenter sInstance;
    private static final Object sLock = new Object();
    private final RemoteService mRemoteService;

    /* loaded from: classes.dex */
    public static abstract class OnResultCallback {
        public synchronized void onGetAppPermissions(List<RuntimePermissionPresentationInfo> permissions) {
        }
    }

    public static synchronized RuntimePermissionPresenter getInstance(Context context) {
        RuntimePermissionPresenter runtimePermissionPresenter;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new RuntimePermissionPresenter(context.getApplicationContext());
            }
            runtimePermissionPresenter = sInstance;
        }
        return runtimePermissionPresenter;
    }

    private synchronized RuntimePermissionPresenter(Context context) {
        this.mRemoteService = new RemoteService(context);
    }

    public synchronized void getAppPermissions(String packageName, OnResultCallback callback, Handler handler) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = packageName;
        args.arg2 = callback;
        args.arg3 = handler;
        Message message = this.mRemoteService.obtainMessage(1, args);
        this.mRemoteService.processMessage(message);
    }

    public synchronized void revokeRuntimePermission(String packageName, String permissionName) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = packageName;
        args.arg2 = permissionName;
        Message message = this.mRemoteService.obtainMessage(4, args);
        this.mRemoteService.processMessage(message);
    }

    /* loaded from: classes.dex */
    private static final class RemoteService extends Handler implements ServiceConnection {
        public static final int MSG_GET_APPS_USING_PERMISSIONS = 2;
        public static final int MSG_GET_APP_PERMISSIONS = 1;
        public static final int MSG_REVOKE_APP_PERMISSIONS = 4;
        public static final int MSG_UNBIND = 3;
        private static final long UNBIND_TIMEOUT_MILLIS = 10000;
        @GuardedBy("mLock")
        private boolean mBound;
        private final Context mContext;
        private final Object mLock;
        @GuardedBy("mLock")
        private final List<Message> mPendingWork;
        @GuardedBy("mLock")
        private IRuntimePermissionPresenter mRemoteInstance;

        public synchronized RemoteService(Context context) {
            super(context.getMainLooper(), null, false);
            this.mLock = new Object();
            this.mPendingWork = new ArrayList();
            this.mContext = context;
        }

        public synchronized void processMessage(Message message) {
            synchronized (this.mLock) {
                if (!this.mBound) {
                    Intent intent = new Intent(RuntimePermissionPresenterService.SERVICE_INTERFACE);
                    intent.setPackage(this.mContext.getPackageManager().getPermissionControllerPackageName());
                    this.mBound = this.mContext.bindService(intent, this, 1);
                }
                this.mPendingWork.add(message);
                scheduleNextMessageIfNeededLocked();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (this.mLock) {
                this.mRemoteInstance = IRuntimePermissionPresenter.Stub.asInterface(service);
                scheduleNextMessageIfNeededLocked();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            synchronized (this.mLock) {
                this.mRemoteInstance = null;
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            IRuntimePermissionPresenter remoteInstance;
            IRuntimePermissionPresenter remoteInstance2;
            int i = msg.what;
            if (i == 1) {
                SomeArgs args = (SomeArgs) msg.obj;
                String packageName = (String) args.arg1;
                final OnResultCallback callback = (OnResultCallback) args.arg2;
                final Handler handler = (Handler) args.arg3;
                args.recycle();
                synchronized (this.mLock) {
                    remoteInstance = this.mRemoteInstance;
                }
                if (remoteInstance == null) {
                    return;
                }
                try {
                    remoteInstance.getAppPermissions(packageName, new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.content.pm.permission.RuntimePermissionPresenter.RemoteService.1
                        @Override // android.os.RemoteCallback.OnResultListener
                        public void onResult(Bundle result) {
                            List<RuntimePermissionPresentationInfo> permissions = null;
                            if (result != null) {
                                permissions = result.getParcelableArrayList(RuntimePermissionPresenter.KEY_RESULT);
                            }
                            if (permissions == null) {
                                permissions = Collections.emptyList();
                            }
                            final List<RuntimePermissionPresentationInfo> reportedPermissions = permissions;
                            if (handler != null) {
                                handler.post(new Runnable() { // from class: android.content.pm.permission.RuntimePermissionPresenter.RemoteService.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        callback.onGetAppPermissions(reportedPermissions);
                                    }
                                });
                            } else {
                                callback.onGetAppPermissions(reportedPermissions);
                            }
                        }
                    }, this));
                } catch (RemoteException re) {
                    Log.e(RuntimePermissionPresenter.TAG, "Error getting app permissions", re);
                }
                scheduleUnbind();
            } else {
                switch (i) {
                    case 3:
                        synchronized (this.mLock) {
                            if (this.mBound) {
                                this.mContext.unbindService(this);
                                this.mBound = false;
                            }
                            this.mRemoteInstance = null;
                        }
                        break;
                    case 4:
                        SomeArgs args2 = (SomeArgs) msg.obj;
                        String packageName2 = (String) args2.arg1;
                        String permissionName = (String) args2.arg2;
                        args2.recycle();
                        synchronized (this.mLock) {
                            remoteInstance2 = this.mRemoteInstance;
                        }
                        if (remoteInstance2 == null) {
                            return;
                        }
                        try {
                            remoteInstance2.revokeRuntimePermission(packageName2, permissionName);
                            break;
                        } catch (RemoteException re2) {
                            Log.e(RuntimePermissionPresenter.TAG, "Error getting app permissions", re2);
                            break;
                        }
                }
            }
            synchronized (this.mLock) {
                scheduleNextMessageIfNeededLocked();
            }
        }

        @GuardedBy("mLock")
        private synchronized void scheduleNextMessageIfNeededLocked() {
            if (this.mBound && this.mRemoteInstance != null && !this.mPendingWork.isEmpty()) {
                Message nextMessage = this.mPendingWork.remove(0);
                sendMessage(nextMessage);
            }
        }

        private synchronized void scheduleUnbind() {
            removeMessages(3);
            sendEmptyMessageDelayed(3, 10000L);
        }
    }
}
