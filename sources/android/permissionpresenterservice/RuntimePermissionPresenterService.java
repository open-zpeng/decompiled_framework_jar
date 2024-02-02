package android.permissionpresenterservice;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.permission.IRuntimePermissionPresenter;
import android.content.pm.permission.RuntimePermissionPresentationInfo;
import android.content.pm.permission.RuntimePermissionPresenter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallback;
import com.android.internal.os.SomeArgs;
import java.util.List;
@SystemApi
/* loaded from: classes2.dex */
public abstract class RuntimePermissionPresenterService extends Service {
    public static final String SERVICE_INTERFACE = "android.permissionpresenterservice.RuntimePermissionPresenterService";
    private Handler mHandler;

    public abstract List<RuntimePermissionPresentationInfo> onGetAppPermissions(String str);

    private protected abstract void onRevokeRuntimePermission(String str, String str2);

    @Override // android.content.ContextWrapper
    public final void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mHandler = new MyHandler(base.getMainLooper());
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return new IRuntimePermissionPresenter.Stub() { // from class: android.permissionpresenterservice.RuntimePermissionPresenterService.1
            @Override // android.content.pm.permission.IRuntimePermissionPresenter
            public void getAppPermissions(String packageName, RemoteCallback callback) {
                SomeArgs args = SomeArgs.obtain();
                args.arg1 = packageName;
                args.arg2 = callback;
                RuntimePermissionPresenterService.this.mHandler.obtainMessage(1, args).sendToTarget();
            }

            @Override // android.content.pm.permission.IRuntimePermissionPresenter
            public void revokeRuntimePermission(String packageName, String permissionName) {
                SomeArgs args = SomeArgs.obtain();
                args.arg1 = packageName;
                args.arg2 = permissionName;
                RuntimePermissionPresenterService.this.mHandler.obtainMessage(3, args).sendToTarget();
            }
        };
    }

    /* loaded from: classes2.dex */
    private final class MyHandler extends Handler {
        public static final int MSG_GET_APPS_USING_PERMISSIONS = 2;
        public static final int MSG_GET_APP_PERMISSIONS = 1;
        public static final int MSG_REVOKE_APP_PERMISSION = 3;

        public MyHandler(Looper looper) {
            super(looper, null, false);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i != 1) {
                if (i == 3) {
                    SomeArgs args = (SomeArgs) msg.obj;
                    String packageName = (String) args.arg1;
                    String permissionName = (String) args.arg2;
                    args.recycle();
                    RuntimePermissionPresenterService.this.onRevokeRuntimePermission(packageName, permissionName);
                    return;
                }
                return;
            }
            SomeArgs args2 = (SomeArgs) msg.obj;
            String packageName2 = (String) args2.arg1;
            RemoteCallback callback = (RemoteCallback) args2.arg2;
            args2.recycle();
            List<RuntimePermissionPresentationInfo> permissions = RuntimePermissionPresenterService.this.onGetAppPermissions(packageName2);
            if (permissions != null && !permissions.isEmpty()) {
                Bundle result = new Bundle();
                result.putParcelableList(RuntimePermissionPresenter.KEY_RESULT, permissions);
                callback.sendResult(result);
                return;
            }
            callback.sendResult(null);
        }
    }
}
