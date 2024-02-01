package android.app;

import android.app.IUserSwitchObserver;
import android.os.IRemoteCallback;
import android.os.RemoteException;
/* loaded from: classes.dex */
public class UserSwitchObserver extends IUserSwitchObserver.Stub {
    public synchronized void onUserSwitching(int newUserId, IRemoteCallback reply) throws RemoteException {
        if (reply != null) {
            reply.sendResult(null);
        }
    }

    @Override // android.app.IUserSwitchObserver
    public synchronized void onUserSwitchComplete(int newUserId) throws RemoteException {
    }

    @Override // android.app.IUserSwitchObserver
    public synchronized void onForegroundProfileSwitch(int newProfileId) throws RemoteException {
    }

    @Override // android.app.IUserSwitchObserver
    public synchronized void onLockedBootComplete(int newUserId) throws RemoteException {
    }
}
