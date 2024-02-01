package android.content;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
/* loaded from: classes.dex */
public class SyncContext {
    private static final long HEARTBEAT_SEND_INTERVAL_IN_MS = 1000;
    private long mLastHeartbeatSendTime = 0;
    private ISyncContext mSyncContext;

    /* JADX INFO: Access modifiers changed from: private */
    public SyncContext(ISyncContext syncContextInterface) {
        this.mSyncContext = syncContextInterface;
    }

    private protected void setStatusText(String message) {
        updateHeartbeat();
    }

    private synchronized void updateHeartbeat() {
        long now = SystemClock.elapsedRealtime();
        if (now < this.mLastHeartbeatSendTime + 1000) {
            return;
        }
        try {
            this.mLastHeartbeatSendTime = now;
            if (this.mSyncContext != null) {
                this.mSyncContext.sendHeartbeat();
            }
        } catch (RemoteException e) {
        }
    }

    public void onFinished(SyncResult result) {
        try {
            if (this.mSyncContext != null) {
                this.mSyncContext.onFinished(result);
            }
        } catch (RemoteException e) {
        }
    }

    public IBinder getSyncContextBinder() {
        if (this.mSyncContext == null) {
            return null;
        }
        return this.mSyncContext.asBinder();
    }
}
