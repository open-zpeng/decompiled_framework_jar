package android.app.backup;

import android.annotation.SystemApi;
import android.app.backup.IBackupManagerMonitor;
import android.app.backup.IRestoreObserver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
@SystemApi
/* loaded from: classes.dex */
public class RestoreSession {
    static final String TAG = "RestoreSession";
    IRestoreSession mBinder;
    final Context mContext;
    RestoreObserverWrapper mObserver = null;

    public int getAvailableRestoreSets(RestoreObserver observer, BackupManagerMonitor monitor) {
        BackupManagerMonitorWrapper monitorWrapper;
        RestoreObserverWrapper obsWrapper = new RestoreObserverWrapper(this.mContext, observer);
        if (monitor == null) {
            monitorWrapper = null;
        } else {
            monitorWrapper = new BackupManagerMonitorWrapper(monitor);
        }
        try {
            int err = this.mBinder.getAvailableRestoreSets(obsWrapper, monitorWrapper);
            return err;
        } catch (RemoteException e) {
            Log.d(TAG, "Can't contact server to get available sets");
            return -1;
        }
    }

    public int getAvailableRestoreSets(RestoreObserver observer) {
        return getAvailableRestoreSets(observer, null);
    }

    public int restoreAll(long token, RestoreObserver observer, BackupManagerMonitor monitor) {
        BackupManagerMonitorWrapper monitorWrapper;
        if (this.mObserver != null) {
            Log.d(TAG, "restoreAll() called during active restore");
            return -1;
        }
        this.mObserver = new RestoreObserverWrapper(this.mContext, observer);
        if (monitor == null) {
            monitorWrapper = null;
        } else {
            monitorWrapper = new BackupManagerMonitorWrapper(monitor);
        }
        try {
            int err = this.mBinder.restoreAll(token, this.mObserver, monitorWrapper);
            return err;
        } catch (RemoteException e) {
            Log.d(TAG, "Can't contact server to restore");
            return -1;
        }
    }

    public int restoreAll(long token, RestoreObserver observer) {
        return restoreAll(token, observer, null);
    }

    public synchronized int restoreSome(long token, RestoreObserver observer, BackupManagerMonitor monitor, String[] packages) {
        if (this.mObserver != null) {
            Log.d(TAG, "restoreAll() called during active restore");
            return -1;
        }
        this.mObserver = new RestoreObserverWrapper(this.mContext, observer);
        BackupManagerMonitorWrapper monitorWrapper = monitor == null ? null : new BackupManagerMonitorWrapper(monitor);
        try {
            int err = this.mBinder.restoreSome(token, this.mObserver, monitorWrapper, packages);
            return err;
        } catch (RemoteException e) {
            Log.d(TAG, "Can't contact server to restore packages");
            return -1;
        }
    }

    public synchronized int restoreSome(long token, RestoreObserver observer, String[] packages) {
        return restoreSome(token, observer, null, packages);
    }

    public int restorePackage(String packageName, RestoreObserver observer, BackupManagerMonitor monitor) {
        BackupManagerMonitorWrapper monitorWrapper;
        if (this.mObserver != null) {
            Log.d(TAG, "restorePackage() called during active restore");
            return -1;
        }
        this.mObserver = new RestoreObserverWrapper(this.mContext, observer);
        if (monitor == null) {
            monitorWrapper = null;
        } else {
            monitorWrapper = new BackupManagerMonitorWrapper(monitor);
        }
        try {
            int err = this.mBinder.restorePackage(packageName, this.mObserver, monitorWrapper);
            return err;
        } catch (RemoteException e) {
            Log.d(TAG, "Can't contact server to restore package");
            return -1;
        }
    }

    public int restorePackage(String packageName, RestoreObserver observer) {
        return restorePackage(packageName, observer, null);
    }

    public void endRestoreSession() {
        try {
            try {
                this.mBinder.endRestoreSession();
            } catch (RemoteException e) {
                Log.d(TAG, "Can't contact server to get available sets");
            }
        } finally {
            this.mBinder = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized RestoreSession(Context context, IRestoreSession binder) {
        this.mContext = context;
        this.mBinder = binder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RestoreObserverWrapper extends IRestoreObserver.Stub {
        static final int MSG_RESTORE_FINISHED = 3;
        static final int MSG_RESTORE_SETS_AVAILABLE = 4;
        static final int MSG_RESTORE_STARTING = 1;
        static final int MSG_UPDATE = 2;
        final RestoreObserver mAppObserver;
        final Handler mHandler;

        RestoreObserverWrapper(Context context, RestoreObserver appObserver) {
            this.mHandler = new Handler(context.getMainLooper()) { // from class: android.app.backup.RestoreSession.RestoreObserverWrapper.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            RestoreObserverWrapper.this.mAppObserver.restoreStarting(msg.arg1);
                            return;
                        case 2:
                            RestoreObserverWrapper.this.mAppObserver.onUpdate(msg.arg1, (String) msg.obj);
                            return;
                        case 3:
                            RestoreObserverWrapper.this.mAppObserver.restoreFinished(msg.arg1);
                            return;
                        case 4:
                            RestoreObserverWrapper.this.mAppObserver.restoreSetsAvailable((RestoreSet[]) msg.obj);
                            return;
                        default:
                            return;
                    }
                }
            };
            this.mAppObserver = appObserver;
        }

        @Override // android.app.backup.IRestoreObserver
        public synchronized void restoreSetsAvailable(RestoreSet[] result) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(4, result));
        }

        @Override // android.app.backup.IRestoreObserver
        public synchronized void restoreStarting(int numPackages) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, numPackages, 0));
        }

        @Override // android.app.backup.IRestoreObserver
        public synchronized void onUpdate(int nowBeingRestored, String currentPackage) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(2, nowBeingRestored, 0, currentPackage));
        }

        @Override // android.app.backup.IRestoreObserver
        public synchronized void restoreFinished(int error) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, error, 0));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class BackupManagerMonitorWrapper extends IBackupManagerMonitor.Stub {
        final BackupManagerMonitor mMonitor;

        BackupManagerMonitorWrapper(BackupManagerMonitor monitor) {
            this.mMonitor = monitor;
        }

        @Override // android.app.backup.IBackupManagerMonitor
        public synchronized void onEvent(Bundle event) throws RemoteException {
            this.mMonitor.onEvent(event);
        }
    }
}
