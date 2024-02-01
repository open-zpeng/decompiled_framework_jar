package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IUserSwitchObserver extends IInterface {
    synchronized void onForegroundProfileSwitch(int i) throws RemoteException;

    synchronized void onLockedBootComplete(int i) throws RemoteException;

    synchronized void onUserSwitchComplete(int i) throws RemoteException;

    synchronized void onUserSwitching(int i, IRemoteCallback iRemoteCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IUserSwitchObserver {
        private static final String DESCRIPTOR = "android.app.IUserSwitchObserver";
        static final int TRANSACTION_onForegroundProfileSwitch = 3;
        static final int TRANSACTION_onLockedBootComplete = 4;
        static final int TRANSACTION_onUserSwitchComplete = 2;
        static final int TRANSACTION_onUserSwitching = 1;

        /* JADX INFO: Access modifiers changed from: private */
        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IUserSwitchObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IUserSwitchObserver)) {
                return (IUserSwitchObserver) iin;
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
                    int _arg0 = data.readInt();
                    IRemoteCallback _arg1 = IRemoteCallback.Stub.asInterface(data.readStrongBinder());
                    onUserSwitching(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    onUserSwitchComplete(_arg02);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    onForegroundProfileSwitch(_arg03);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    onLockedBootComplete(_arg04);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IUserSwitchObserver {
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

            @Override // android.app.IUserSwitchObserver
            public synchronized void onUserSwitching(int newUserId, IRemoteCallback reply) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newUserId);
                    _data.writeStrongBinder(reply != null ? reply.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IUserSwitchObserver
            public synchronized void onUserSwitchComplete(int newUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newUserId);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IUserSwitchObserver
            public synchronized void onForegroundProfileSwitch(int newProfileId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newProfileId);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IUserSwitchObserver
            public synchronized void onLockedBootComplete(int newUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newUserId);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
