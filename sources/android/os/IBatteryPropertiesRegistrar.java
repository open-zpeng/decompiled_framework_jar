package android.os;

import android.os.IBatteryPropertiesListener;
/* loaded from: classes2.dex */
public interface IBatteryPropertiesRegistrar extends IInterface {
    synchronized int getProperty(int i, BatteryProperty batteryProperty) throws RemoteException;

    synchronized void registerListener(IBatteryPropertiesListener iBatteryPropertiesListener) throws RemoteException;

    synchronized void scheduleUpdate() throws RemoteException;

    synchronized void unregisterListener(IBatteryPropertiesListener iBatteryPropertiesListener) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IBatteryPropertiesRegistrar {
        private static final String DESCRIPTOR = "android.os.IBatteryPropertiesRegistrar";
        static final int TRANSACTION_getProperty = 3;
        static final int TRANSACTION_registerListener = 1;
        static final int TRANSACTION_scheduleUpdate = 4;
        static final int TRANSACTION_unregisterListener = 2;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IBatteryPropertiesRegistrar asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBatteryPropertiesRegistrar)) {
                return (IBatteryPropertiesRegistrar) iin;
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
                    IBatteryPropertiesListener _arg0 = IBatteryPropertiesListener.Stub.asInterface(data.readStrongBinder());
                    registerListener(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IBatteryPropertiesListener _arg02 = IBatteryPropertiesListener.Stub.asInterface(data.readStrongBinder());
                    unregisterListener(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    BatteryProperty _arg1 = new BatteryProperty();
                    int _result = getProperty(_arg03, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    reply.writeInt(1);
                    _arg1.writeToParcel(reply, 1);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    scheduleUpdate();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IBatteryPropertiesRegistrar {
            private IBinder mRemote;

            public private protected Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.os.IBatteryPropertiesRegistrar
            public synchronized void registerListener(IBatteryPropertiesListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IBatteryPropertiesRegistrar
            public synchronized void unregisterListener(IBatteryPropertiesListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IBatteryPropertiesRegistrar
            public synchronized int getProperty(int id, BatteryProperty prop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        prop.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IBatteryPropertiesRegistrar
            public synchronized void scheduleUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
