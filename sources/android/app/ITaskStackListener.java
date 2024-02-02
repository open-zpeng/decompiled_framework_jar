package android.app;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface ITaskStackListener extends IInterface {
    public static final int FORCED_RESIZEABLE_REASON_SECONDARY_DISPLAY = 2;
    public static final int FORCED_RESIZEABLE_REASON_SPLIT_SCREEN = 1;

    synchronized void onActivityDismissingDockedStack() throws RemoteException;

    synchronized void onActivityForcedResizable(String str, int i, int i2) throws RemoteException;

    synchronized void onActivityLaunchOnSecondaryDisplayFailed() throws RemoteException;

    synchronized void onActivityPinned(String str, int i, int i2, int i3) throws RemoteException;

    synchronized void onActivityRequestedOrientationChanged(int i, int i2) throws RemoteException;

    synchronized void onActivityUnpinned() throws RemoteException;

    synchronized void onPinnedActivityRestartAttempt(boolean z) throws RemoteException;

    synchronized void onPinnedStackAnimationEnded() throws RemoteException;

    synchronized void onPinnedStackAnimationStarted() throws RemoteException;

    synchronized void onTaskCreated(int i, ComponentName componentName) throws RemoteException;

    synchronized void onTaskDescriptionChanged(int i, ActivityManager.TaskDescription taskDescription) throws RemoteException;

    synchronized void onTaskMovedToFront(int i) throws RemoteException;

    synchronized void onTaskProfileLocked(int i, int i2) throws RemoteException;

    synchronized void onTaskRemovalStarted(int i) throws RemoteException;

    synchronized void onTaskRemoved(int i) throws RemoteException;

    synchronized void onTaskSnapshotChanged(int i, ActivityManager.TaskSnapshot taskSnapshot) throws RemoteException;

    synchronized void onTaskStackChanged() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ITaskStackListener {
        private static final String DESCRIPTOR = "android.app.ITaskStackListener";
        static final int TRANSACTION_onActivityDismissingDockedStack = 8;
        static final int TRANSACTION_onActivityForcedResizable = 7;
        static final int TRANSACTION_onActivityLaunchOnSecondaryDisplayFailed = 9;
        static final int TRANSACTION_onActivityPinned = 2;
        static final int TRANSACTION_onActivityRequestedOrientationChanged = 14;
        static final int TRANSACTION_onActivityUnpinned = 3;
        static final int TRANSACTION_onPinnedActivityRestartAttempt = 4;
        static final int TRANSACTION_onPinnedStackAnimationEnded = 6;
        static final int TRANSACTION_onPinnedStackAnimationStarted = 5;
        static final int TRANSACTION_onTaskCreated = 10;
        static final int TRANSACTION_onTaskDescriptionChanged = 13;
        static final int TRANSACTION_onTaskMovedToFront = 12;
        static final int TRANSACTION_onTaskProfileLocked = 16;
        static final int TRANSACTION_onTaskRemovalStarted = 15;
        static final int TRANSACTION_onTaskRemoved = 11;
        static final int TRANSACTION_onTaskSnapshotChanged = 17;
        static final int TRANSACTION_onTaskStackChanged = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ITaskStackListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITaskStackListener)) {
                return (ITaskStackListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onTaskStackChanged();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    onActivityPinned(_arg0, _arg1, _arg2, _arg3);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onActivityUnpinned();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg02 = data.readInt() != 0;
                    onPinnedActivityRestartAttempt(_arg02);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onPinnedStackAnimationStarted();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    onPinnedStackAnimationEnded();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    int _arg12 = data.readInt();
                    int _arg22 = data.readInt();
                    onActivityForcedResizable(_arg03, _arg12, _arg22);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    onActivityDismissingDockedStack();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    onActivityLaunchOnSecondaryDisplayFailed();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    ComponentName _arg13 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    onTaskCreated(_arg04, _arg13);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    onTaskRemoved(_arg05);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    onTaskMovedToFront(_arg06);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    ActivityManager.TaskDescription _arg14 = data.readInt() != 0 ? ActivityManager.TaskDescription.CREATOR.createFromParcel(data) : null;
                    onTaskDescriptionChanged(_arg07, _arg14);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    int _arg15 = data.readInt();
                    onActivityRequestedOrientationChanged(_arg08, _arg15);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    onTaskRemovalStarted(_arg09);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    int _arg16 = data.readInt();
                    onTaskProfileLocked(_arg010, _arg16);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    ActivityManager.TaskSnapshot _arg17 = data.readInt() != 0 ? ActivityManager.TaskSnapshot.CREATOR.createFromParcel(data) : null;
                    onTaskSnapshotChanged(_arg011, _arg17);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements ITaskStackListener {
            private IBinder mRemote;

            synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onTaskStackChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onActivityPinned(String packageName, int userId, int taskId, int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onActivityUnpinned() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onPinnedActivityRestartAttempt(boolean clearedTask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clearedTask ? 1 : 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onPinnedStackAnimationStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onPinnedStackAnimationEnded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onActivityForcedResizable(String packageName, int taskId, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(taskId);
                    _data.writeInt(reason);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onActivityDismissingDockedStack() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onActivityLaunchOnSecondaryDisplayFailed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onTaskCreated(int taskId, ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onTaskRemoved(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onTaskMovedToFront(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onTaskDescriptionChanged(int taskId, ActivityManager.TaskDescription td) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (td != null) {
                        _data.writeInt(1);
                        td.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onActivityRequestedOrientationChanged(int taskId, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(requestedOrientation);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onTaskRemovalStarted(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onTaskProfileLocked(int taskId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(userId);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public synchronized void onTaskSnapshotChanged(int taskId, ActivityManager.TaskSnapshot snapshot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (snapshot != null) {
                        _data.writeInt(1);
                        snapshot.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
