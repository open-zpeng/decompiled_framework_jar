package android.service.oemlock;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IOemLockService extends IInterface {
    synchronized boolean isDeviceOemUnlocked() throws RemoteException;

    synchronized boolean isOemUnlockAllowed() throws RemoteException;

    synchronized boolean isOemUnlockAllowedByCarrier() throws RemoteException;

    synchronized boolean isOemUnlockAllowedByUser() throws RemoteException;

    synchronized void setOemUnlockAllowedByCarrier(boolean z, byte[] bArr) throws RemoteException;

    synchronized void setOemUnlockAllowedByUser(boolean z) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IOemLockService {
        private static final String DESCRIPTOR = "android.service.oemlock.IOemLockService";
        static final int TRANSACTION_isDeviceOemUnlocked = 6;
        static final int TRANSACTION_isOemUnlockAllowed = 5;
        static final int TRANSACTION_isOemUnlockAllowedByCarrier = 2;
        static final int TRANSACTION_isOemUnlockAllowedByUser = 4;
        static final int TRANSACTION_setOemUnlockAllowedByCarrier = 1;
        static final int TRANSACTION_setOemUnlockAllowedByUser = 3;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IOemLockService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IOemLockService)) {
                return (IOemLockService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    byte[] _arg1 = data.createByteArray();
                    setOemUnlockAllowedByCarrier(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isOemUnlockAllowedByCarrier = isOemUnlockAllowedByCarrier();
                    reply.writeNoException();
                    reply.writeInt(isOemUnlockAllowedByCarrier ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    setOemUnlockAllowedByUser(_arg0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isOemUnlockAllowedByUser = isOemUnlockAllowedByUser();
                    reply.writeNoException();
                    reply.writeInt(isOemUnlockAllowedByUser ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isOemUnlockAllowed = isOemUnlockAllowed();
                    reply.writeNoException();
                    reply.writeInt(isOemUnlockAllowed ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDeviceOemUnlocked = isDeviceOemUnlocked();
                    reply.writeNoException();
                    reply.writeInt(isDeviceOemUnlocked ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IOemLockService {
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

            @Override // android.service.oemlock.IOemLockService
            public synchronized void setOemUnlockAllowedByCarrier(boolean allowed, byte[] signature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(allowed ? 1 : 0);
                    _data.writeByteArray(signature);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.service.oemlock.IOemLockService
            public synchronized boolean isOemUnlockAllowedByCarrier() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.service.oemlock.IOemLockService
            public synchronized void setOemUnlockAllowedByUser(boolean allowed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(allowed ? 1 : 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.service.oemlock.IOemLockService
            public synchronized boolean isOemUnlockAllowedByUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.service.oemlock.IOemLockService
            public synchronized boolean isOemUnlockAllowed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.service.oemlock.IOemLockService
            public synchronized boolean isDeviceOemUnlocked() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
