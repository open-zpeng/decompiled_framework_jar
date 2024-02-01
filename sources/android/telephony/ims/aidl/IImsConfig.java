package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsConfigCallback;
/* loaded from: classes2.dex */
public interface IImsConfig extends IInterface {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized void addImsConfigCallback(IImsConfigCallback iImsConfigCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getConfigInt(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String getConfigString(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void removeImsConfigCallback(IImsConfigCallback iImsConfigCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int setConfigInt(int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int setConfigString(int i, String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IImsConfig {
        public protected static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsConfig";
        public private protected static final int TRANSACTION_addImsConfigCallback = 1;
        public private protected static final int TRANSACTION_getConfigInt = 3;
        public private protected static final int TRANSACTION_getConfigString = 4;
        public private protected static final int TRANSACTION_removeImsConfigCallback = 2;
        public private protected static final int TRANSACTION_setConfigInt = 5;
        public private protected static final int TRANSACTION_setConfigString = 6;

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized IImsConfig asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IImsConfig)) {
                return (IImsConfig) iin;
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
                    IImsConfigCallback _arg0 = IImsConfigCallback.Stub.asInterface(data.readStrongBinder());
                    addImsConfigCallback(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IImsConfigCallback _arg02 = IImsConfigCallback.Stub.asInterface(data.readStrongBinder());
                    removeImsConfigCallback(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    int _result = getConfigInt(_arg03);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    String _result2 = getConfigString(_arg04);
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    int _arg1 = data.readInt();
                    int _result3 = setConfigInt(_arg05, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    String _arg12 = data.readString();
                    int _result4 = setConfigString(_arg06, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IImsConfig {
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

            private protected synchronized void addImsConfigCallback(IImsConfigCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void removeImsConfigCallback(IImsConfigCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized int getConfigInt(int item) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(item);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized String getConfigString(int item) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(item);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized int setConfigInt(int item, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(item);
                    _data.writeInt(value);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized int setConfigString(int item, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(item);
                    _data.writeString(value);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
