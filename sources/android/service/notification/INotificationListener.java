package android.service.notification;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.IStatusBarNotificationHolder;
/* loaded from: classes2.dex */
public interface INotificationListener extends IInterface {
    synchronized void onInterruptionFilterChanged(int i) throws RemoteException;

    synchronized void onListenerConnected(NotificationRankingUpdate notificationRankingUpdate) throws RemoteException;

    synchronized void onListenerHintsChanged(int i) throws RemoteException;

    synchronized void onNotificationChannelGroupModification(String str, UserHandle userHandle, NotificationChannelGroup notificationChannelGroup, int i) throws RemoteException;

    synchronized void onNotificationChannelModification(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) throws RemoteException;

    synchronized void onNotificationEnqueued(IStatusBarNotificationHolder iStatusBarNotificationHolder) throws RemoteException;

    synchronized void onNotificationPosted(IStatusBarNotificationHolder iStatusBarNotificationHolder, NotificationRankingUpdate notificationRankingUpdate) throws RemoteException;

    synchronized void onNotificationRankingUpdate(NotificationRankingUpdate notificationRankingUpdate) throws RemoteException;

    synchronized void onNotificationRemoved(IStatusBarNotificationHolder iStatusBarNotificationHolder, NotificationRankingUpdate notificationRankingUpdate, NotificationStats notificationStats, int i) throws RemoteException;

    synchronized void onNotificationSnoozedUntilContext(IStatusBarNotificationHolder iStatusBarNotificationHolder, String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements INotificationListener {
        private static final String DESCRIPTOR = "android.service.notification.INotificationListener";
        static final int TRANSACTION_onInterruptionFilterChanged = 6;
        static final int TRANSACTION_onListenerConnected = 1;
        static final int TRANSACTION_onListenerHintsChanged = 5;
        static final int TRANSACTION_onNotificationChannelGroupModification = 8;
        static final int TRANSACTION_onNotificationChannelModification = 7;
        static final int TRANSACTION_onNotificationEnqueued = 9;
        static final int TRANSACTION_onNotificationPosted = 2;
        static final int TRANSACTION_onNotificationRankingUpdate = 4;
        static final int TRANSACTION_onNotificationRemoved = 3;
        static final int TRANSACTION_onNotificationSnoozedUntilContext = 10;

        /* JADX INFO: Access modifiers changed from: private */
        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized INotificationListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof INotificationListener)) {
                return (INotificationListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            NotificationRankingUpdate _arg1;
            UserHandle _arg12;
            UserHandle _arg13;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    NotificationRankingUpdate _arg0 = data.readInt() != 0 ? NotificationRankingUpdate.CREATOR.createFromParcel(data) : null;
                    onListenerConnected(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IStatusBarNotificationHolder _arg02 = IStatusBarNotificationHolder.Stub.asInterface(data.readStrongBinder());
                    NotificationRankingUpdate _arg14 = data.readInt() != 0 ? NotificationRankingUpdate.CREATOR.createFromParcel(data) : null;
                    onNotificationPosted(_arg02, _arg14);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IStatusBarNotificationHolder _arg03 = IStatusBarNotificationHolder.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg1 = NotificationRankingUpdate.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    NotificationStats _arg2 = data.readInt() != 0 ? NotificationStats.CREATOR.createFromParcel(data) : null;
                    int _arg3 = data.readInt();
                    onNotificationRemoved(_arg03, _arg1, _arg2, _arg3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    NotificationRankingUpdate _arg04 = data.readInt() != 0 ? NotificationRankingUpdate.CREATOR.createFromParcel(data) : null;
                    onNotificationRankingUpdate(_arg04);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    onListenerHintsChanged(_arg05);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    onInterruptionFilterChanged(_arg06);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = UserHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    NotificationChannel _arg22 = data.readInt() != 0 ? NotificationChannel.CREATOR.createFromParcel(data) : null;
                    int _arg32 = data.readInt();
                    onNotificationChannelModification(_arg07, _arg12, _arg22, _arg32);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    if (data.readInt() != 0) {
                        _arg13 = UserHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    NotificationChannelGroup _arg23 = data.readInt() != 0 ? NotificationChannelGroup.CREATOR.createFromParcel(data) : null;
                    int _arg33 = data.readInt();
                    onNotificationChannelGroupModification(_arg08, _arg13, _arg23, _arg33);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    IStatusBarNotificationHolder _arg09 = IStatusBarNotificationHolder.Stub.asInterface(data.readStrongBinder());
                    onNotificationEnqueued(_arg09);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IStatusBarNotificationHolder _arg010 = IStatusBarNotificationHolder.Stub.asInterface(data.readStrongBinder());
                    String _arg15 = data.readString();
                    onNotificationSnoozedUntilContext(_arg010, _arg15);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements INotificationListener {
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

            @Override // android.service.notification.INotificationListener
            public synchronized void onListenerConnected(NotificationRankingUpdate update) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (update != null) {
                        _data.writeInt(1);
                        update.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.notification.INotificationListener
            public synchronized void onNotificationPosted(IStatusBarNotificationHolder notificationHolder, NotificationRankingUpdate update) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(notificationHolder != null ? notificationHolder.asBinder() : null);
                    if (update != null) {
                        _data.writeInt(1);
                        update.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.notification.INotificationListener
            public synchronized void onNotificationRemoved(IStatusBarNotificationHolder notificationHolder, NotificationRankingUpdate update, NotificationStats stats, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(notificationHolder != null ? notificationHolder.asBinder() : null);
                    if (update != null) {
                        _data.writeInt(1);
                        update.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (stats != null) {
                        _data.writeInt(1);
                        stats.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(reason);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.notification.INotificationListener
            public synchronized void onNotificationRankingUpdate(NotificationRankingUpdate update) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (update != null) {
                        _data.writeInt(1);
                        update.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.notification.INotificationListener
            public synchronized void onListenerHintsChanged(int hints) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hints);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.notification.INotificationListener
            public synchronized void onInterruptionFilterChanged(int interruptionFilter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(interruptionFilter);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.notification.INotificationListener
            public synchronized void onNotificationChannelModification(String pkgName, UserHandle user, NotificationChannel channel, int modificationType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (channel != null) {
                        _data.writeInt(1);
                        channel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(modificationType);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.notification.INotificationListener
            public synchronized void onNotificationChannelGroupModification(String pkgName, UserHandle user, NotificationChannelGroup group, int modificationType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (group != null) {
                        _data.writeInt(1);
                        group.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(modificationType);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.notification.INotificationListener
            public synchronized void onNotificationEnqueued(IStatusBarNotificationHolder notificationHolder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(notificationHolder != null ? notificationHolder.asBinder() : null);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.notification.INotificationListener
            public synchronized void onNotificationSnoozedUntilContext(IStatusBarNotificationHolder notificationHolder, String snoozeCriterionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(notificationHolder != null ? notificationHolder.asBinder() : null);
                    _data.writeString(snoozeCriterionId);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
