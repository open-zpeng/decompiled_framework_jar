package android.app;

import android.app.ActivityManager;
import android.app.ITaskStackListener;
import android.content.ComponentName;
import android.os.RemoteException;
/* loaded from: classes.dex */
public abstract class TaskStackListener extends ITaskStackListener.Stub {
    private protected void onTaskStackChanged() throws RemoteException {
    }

    private protected void onActivityPinned(String packageName, int userId, int taskId, int stackId) throws RemoteException {
    }

    private protected void onActivityUnpinned() throws RemoteException {
    }

    private protected void onPinnedActivityRestartAttempt(boolean clearedTask) throws RemoteException {
    }

    private protected void onPinnedStackAnimationStarted() throws RemoteException {
    }

    private protected void onPinnedStackAnimationEnded() throws RemoteException {
    }

    private protected void onActivityForcedResizable(String packageName, int taskId, int reason) throws RemoteException {
    }

    private protected void onActivityDismissingDockedStack() throws RemoteException {
    }

    private protected void onActivityLaunchOnSecondaryDisplayFailed() throws RemoteException {
    }

    @Override // android.app.ITaskStackListener
    public synchronized void onTaskCreated(int taskId, ComponentName componentName) throws RemoteException {
    }

    private protected void onTaskRemoved(int taskId) throws RemoteException {
    }

    private protected void onTaskMovedToFront(int taskId) throws RemoteException {
    }

    @Override // android.app.ITaskStackListener
    public synchronized void onTaskRemovalStarted(int taskId) throws RemoteException {
    }

    public synchronized void onTaskDescriptionChanged(int taskId, ActivityManager.TaskDescription td) throws RemoteException {
    }

    private protected void onActivityRequestedOrientationChanged(int taskId, int requestedOrientation) throws RemoteException {
    }

    private protected void onTaskProfileLocked(int taskId, int userId) throws RemoteException {
    }

    private protected void onTaskSnapshotChanged(int taskId, ActivityManager.TaskSnapshot snapshot) throws RemoteException {
    }
}
