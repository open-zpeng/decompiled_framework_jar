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

    void onActivityDismissingDockedStack() throws RemoteException;

    void onActivityForcedResizable(String str, int i, int i2) throws RemoteException;

    void onActivityLaunchOnSecondaryDisplayFailed(ActivityManager.RunningTaskInfo runningTaskInfo, int i) throws RemoteException;

    void onActivityLaunchOnSecondaryDisplayRerouted(ActivityManager.RunningTaskInfo runningTaskInfo, int i) throws RemoteException;

    void onActivityPinned(String str, int i, int i2, int i3) throws RemoteException;

    void onActivityRequestedOrientationChanged(int i, int i2) throws RemoteException;

    void onActivityUnpinned() throws RemoteException;

    void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException;

    void onPinnedActivityRestartAttempt(boolean z) throws RemoteException;

    void onPinnedStackAnimationEnded() throws RemoteException;

    void onPinnedStackAnimationStarted() throws RemoteException;

    void onSingleTaskDisplayDrawn(int i) throws RemoteException;

    void onSingleTaskDisplayEmpty(int i) throws RemoteException;

    void onSizeCompatModeActivityChanged(int i, IBinder iBinder) throws RemoteException;

    void onTaskCreated(int i, ComponentName componentName) throws RemoteException;

    void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException;

    void onTaskDisplayChanged(int i, int i2) throws RemoteException;

    void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException;

    void onTaskProfileLocked(int i, int i2) throws RemoteException;

    void onTaskRemovalStarted(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException;

    void onTaskRemoved(int i) throws RemoteException;

    void onTaskSnapshotChanged(int i, ActivityManager.TaskSnapshot taskSnapshot) throws RemoteException;

    void onTaskStackChanged() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ITaskStackListener {
        @Override // android.app.ITaskStackListener
        public void onTaskStackChanged() throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onActivityPinned(String packageName, int userId, int taskId, int stackId) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onActivityUnpinned() throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onPinnedActivityRestartAttempt(boolean clearedTask) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onPinnedStackAnimationStarted() throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onPinnedStackAnimationEnded() throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onActivityForcedResizable(String packageName, int taskId, int reason) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onActivityDismissingDockedStack() throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onActivityLaunchOnSecondaryDisplayFailed(ActivityManager.RunningTaskInfo taskInfo, int requestedDisplayId) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onActivityLaunchOnSecondaryDisplayRerouted(ActivityManager.RunningTaskInfo taskInfo, int requestedDisplayId) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onTaskCreated(int taskId, ComponentName componentName) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onTaskRemoved(int taskId) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onTaskMovedToFront(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onActivityRequestedOrientationChanged(int taskId, int requestedOrientation) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onTaskRemovalStarted(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onTaskProfileLocked(int taskId, int userId) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onTaskSnapshotChanged(int taskId, ActivityManager.TaskSnapshot snapshot) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onSizeCompatModeActivityChanged(int displayId, IBinder activityToken) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onSingleTaskDisplayDrawn(int displayId) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onSingleTaskDisplayEmpty(int displayId) throws RemoteException {
        }

        @Override // android.app.ITaskStackListener
        public void onTaskDisplayChanged(int taskId, int newDisplayId) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ITaskStackListener {
        private static final String DESCRIPTOR = "android.app.ITaskStackListener";
        static final int TRANSACTION_onActivityDismissingDockedStack = 8;
        static final int TRANSACTION_onActivityForcedResizable = 7;
        static final int TRANSACTION_onActivityLaunchOnSecondaryDisplayFailed = 9;
        static final int TRANSACTION_onActivityLaunchOnSecondaryDisplayRerouted = 10;
        static final int TRANSACTION_onActivityPinned = 2;
        static final int TRANSACTION_onActivityRequestedOrientationChanged = 15;
        static final int TRANSACTION_onActivityUnpinned = 3;
        static final int TRANSACTION_onBackPressedOnTaskRoot = 20;
        static final int TRANSACTION_onPinnedActivityRestartAttempt = 4;
        static final int TRANSACTION_onPinnedStackAnimationEnded = 6;
        static final int TRANSACTION_onPinnedStackAnimationStarted = 5;
        static final int TRANSACTION_onSingleTaskDisplayDrawn = 21;
        static final int TRANSACTION_onSingleTaskDisplayEmpty = 22;
        static final int TRANSACTION_onSizeCompatModeActivityChanged = 19;
        static final int TRANSACTION_onTaskCreated = 11;
        static final int TRANSACTION_onTaskDescriptionChanged = 14;
        static final int TRANSACTION_onTaskDisplayChanged = 23;
        static final int TRANSACTION_onTaskMovedToFront = 13;
        static final int TRANSACTION_onTaskProfileLocked = 17;
        static final int TRANSACTION_onTaskRemovalStarted = 16;
        static final int TRANSACTION_onTaskRemoved = 12;
        static final int TRANSACTION_onTaskSnapshotChanged = 18;
        static final int TRANSACTION_onTaskStackChanged = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITaskStackListener asInterface(IBinder obj) {
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

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onTaskStackChanged";
                case 2:
                    return "onActivityPinned";
                case 3:
                    return "onActivityUnpinned";
                case 4:
                    return "onPinnedActivityRestartAttempt";
                case 5:
                    return "onPinnedStackAnimationStarted";
                case 6:
                    return "onPinnedStackAnimationEnded";
                case 7:
                    return "onActivityForcedResizable";
                case 8:
                    return "onActivityDismissingDockedStack";
                case 9:
                    return "onActivityLaunchOnSecondaryDisplayFailed";
                case 10:
                    return "onActivityLaunchOnSecondaryDisplayRerouted";
                case 11:
                    return "onTaskCreated";
                case 12:
                    return "onTaskRemoved";
                case 13:
                    return "onTaskMovedToFront";
                case 14:
                    return "onTaskDescriptionChanged";
                case 15:
                    return "onActivityRequestedOrientationChanged";
                case 16:
                    return "onTaskRemovalStarted";
                case 17:
                    return "onTaskProfileLocked";
                case 18:
                    return "onTaskSnapshotChanged";
                case 19:
                    return "onSizeCompatModeActivityChanged";
                case 20:
                    return "onBackPressedOnTaskRoot";
                case 21:
                    return "onSingleTaskDisplayDrawn";
                case 22:
                    return "onSingleTaskDisplayEmpty";
                case 23:
                    return "onTaskDisplayChanged";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ActivityManager.RunningTaskInfo _arg0;
            ActivityManager.RunningTaskInfo _arg02;
            ComponentName _arg1;
            ActivityManager.RunningTaskInfo _arg03;
            ActivityManager.RunningTaskInfo _arg04;
            ActivityManager.RunningTaskInfo _arg05;
            ActivityManager.TaskSnapshot _arg12;
            ActivityManager.RunningTaskInfo _arg06;
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
                    String _arg07 = data.readString();
                    int _arg13 = data.readInt();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    onActivityPinned(_arg07, _arg13, _arg2, _arg3);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onActivityUnpinned();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg08 = data.readInt() != 0;
                    onPinnedActivityRestartAttempt(_arg08);
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
                    String _arg09 = data.readString();
                    int _arg14 = data.readInt();
                    int _arg22 = data.readInt();
                    onActivityForcedResizable(_arg09, _arg14, _arg22);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    onActivityDismissingDockedStack();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = ActivityManager.RunningTaskInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    int _arg15 = data.readInt();
                    onActivityLaunchOnSecondaryDisplayFailed(_arg0, _arg15);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = ActivityManager.RunningTaskInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    int _arg16 = data.readInt();
                    onActivityLaunchOnSecondaryDisplayRerouted(_arg02, _arg16);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    onTaskCreated(_arg010, _arg1);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    onTaskRemoved(_arg011);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = ActivityManager.RunningTaskInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    onTaskMovedToFront(_arg03);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = ActivityManager.RunningTaskInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    onTaskDescriptionChanged(_arg04);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    int _arg17 = data.readInt();
                    onActivityRequestedOrientationChanged(_arg012, _arg17);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = ActivityManager.RunningTaskInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    onTaskRemovalStarted(_arg05);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    int _arg18 = data.readInt();
                    onTaskProfileLocked(_arg013, _arg18);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = ActivityManager.TaskSnapshot.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    onTaskSnapshotChanged(_arg014, _arg12);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg015 = data.readInt();
                    IBinder _arg19 = data.readStrongBinder();
                    onSizeCompatModeActivityChanged(_arg015, _arg19);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = ActivityManager.RunningTaskInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    onBackPressedOnTaskRoot(_arg06);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    onSingleTaskDisplayDrawn(_arg016);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    onSingleTaskDisplayEmpty(_arg017);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg018 = data.readInt();
                    int _arg110 = data.readInt();
                    onTaskDisplayChanged(_arg018, _arg110);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements ITaskStackListener {
            public static ITaskStackListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.ITaskStackListener
            public void onTaskStackChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTaskStackChanged();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onActivityPinned(String packageName, int userId, int taskId, int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityPinned(packageName, userId, taskId, stackId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onActivityUnpinned() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityUnpinned();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onPinnedActivityRestartAttempt(boolean clearedTask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clearedTask ? 1 : 0);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onPinnedActivityRestartAttempt(clearedTask);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onPinnedStackAnimationStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onPinnedStackAnimationStarted();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onPinnedStackAnimationEnded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onPinnedStackAnimationEnded();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onActivityForcedResizable(String packageName, int taskId, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(taskId);
                    _data.writeInt(reason);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityForcedResizable(packageName, taskId, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onActivityDismissingDockedStack() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityDismissingDockedStack();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onActivityLaunchOnSecondaryDisplayFailed(ActivityManager.RunningTaskInfo taskInfo, int requestedDisplayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(requestedDisplayId);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityLaunchOnSecondaryDisplayFailed(taskInfo, requestedDisplayId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onActivityLaunchOnSecondaryDisplayRerouted(ActivityManager.RunningTaskInfo taskInfo, int requestedDisplayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(requestedDisplayId);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityLaunchOnSecondaryDisplayRerouted(taskInfo, requestedDisplayId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onTaskCreated(int taskId, ComponentName componentName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTaskCreated(taskId, componentName);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onTaskRemoved(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTaskRemoved(taskId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onTaskMovedToFront(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTaskMovedToFront(taskInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTaskDescriptionChanged(taskInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onActivityRequestedOrientationChanged(int taskId, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(requestedOrientation);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityRequestedOrientationChanged(taskId, requestedOrientation);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onTaskRemovalStarted(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTaskRemovalStarted(taskInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onTaskProfileLocked(int taskId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTaskProfileLocked(taskId, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onTaskSnapshotChanged(int taskId, ActivityManager.TaskSnapshot snapshot) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTaskSnapshotChanged(taskId, snapshot);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onSizeCompatModeActivityChanged(int displayId, IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeStrongBinder(activityToken);
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSizeCompatModeActivityChanged(displayId, activityToken);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBackPressedOnTaskRoot(taskInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onSingleTaskDisplayDrawn(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean _status = this.mRemote.transact(21, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSingleTaskDisplayDrawn(displayId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onSingleTaskDisplayEmpty(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean _status = this.mRemote.transact(22, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSingleTaskDisplayEmpty(displayId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITaskStackListener
            public void onTaskDisplayChanged(int taskId, int newDisplayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(newDisplayId);
                    boolean _status = this.mRemote.transact(23, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTaskDisplayChanged(taskId, newDisplayId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ITaskStackListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ITaskStackListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
