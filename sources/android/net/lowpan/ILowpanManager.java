package android.net.lowpan;

import android.net.lowpan.ILowpanInterface;
import android.net.lowpan.ILowpanManagerListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface ILowpanManager extends IInterface {
    private protected static final String LOWPAN_SERVICE_NAME = "lowpan";

    private protected synchronized void addInterface(ILowpanInterface iLowpanInterface) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void addListener(ILowpanManagerListener iLowpanManagerListener) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized ILowpanInterface getInterface(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String[] getInterfaceList() throws RemoteException;

    private protected synchronized void removeInterface(ILowpanInterface iLowpanInterface) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void removeListener(ILowpanManagerListener iLowpanManagerListener) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ILowpanManager {
        public protected static final String DESCRIPTOR = "android.net.lowpan.ILowpanManager";
        public private protected static final int TRANSACTION_addInterface = 5;
        public private protected static final int TRANSACTION_addListener = 3;
        public private protected static final int TRANSACTION_getInterface = 1;
        public private protected static final int TRANSACTION_getInterfaceList = 2;
        public private protected static final int TRANSACTION_removeInterface = 6;
        public private protected static final int TRANSACTION_removeListener = 4;

        private protected synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized ILowpanManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ILowpanManager)) {
                return (ILowpanManager) iin;
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
                    String _arg0 = data.readString();
                    ILowpanInterface _result = getInterface(_arg0);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result2 = getInterfaceList();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    ILowpanManagerListener _arg02 = ILowpanManagerListener.Stub.asInterface(data.readStrongBinder());
                    addListener(_arg02);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ILowpanManagerListener _arg03 = ILowpanManagerListener.Stub.asInterface(data.readStrongBinder());
                    removeListener(_arg03);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    ILowpanInterface _arg04 = ILowpanInterface.Stub.asInterface(data.readStrongBinder());
                    addInterface(_arg04);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    ILowpanInterface _arg05 = ILowpanInterface.Stub.asInterface(data.readStrongBinder());
                    removeInterface(_arg05);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ILowpanManager {
            public protected IBinder mRemote;

            public private protected synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            private protected synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            private protected synchronized ILowpanInterface getInterface(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    ILowpanInterface _result = ILowpanInterface.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized String[] getInterfaceList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void addListener(ILowpanManagerListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void removeListener(ILowpanManagerListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void addInterface(ILowpanInterface lowpan_interface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lowpan_interface != null ? lowpan_interface.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void removeInterface(ILowpanInterface lowpan_interface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lowpan_interface != null ? lowpan_interface.asBinder() : null);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
