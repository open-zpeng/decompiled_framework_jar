package android.os;

/* loaded from: classes2.dex */
public interface IBatteryPropertiesRegistrar extends IInterface {
    int getProperty(int i, BatteryProperty batteryProperty) throws RemoteException;

    void scheduleUpdate() throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IBatteryPropertiesRegistrar {
        @Override // android.os.IBatteryPropertiesRegistrar
        public int getProperty(int id, BatteryProperty prop) throws RemoteException {
            return 0;
        }

        @Override // android.os.IBatteryPropertiesRegistrar
        public void scheduleUpdate() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IBatteryPropertiesRegistrar {
        private static final String DESCRIPTOR = "android.os.IBatteryPropertiesRegistrar";
        static final int TRANSACTION_getProperty = 1;
        static final int TRANSACTION_scheduleUpdate = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBatteryPropertiesRegistrar asInterface(IBinder obj) {
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

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                if (transactionCode == 2) {
                    return "scheduleUpdate";
                }
                return null;
            }
            return "getProperty";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1) {
                if (code == 2) {
                    data.enforceInterface(DESCRIPTOR);
                    scheduleUpdate();
                    return true;
                } else if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                } else {
                    return super.onTransact(code, data, reply, flags);
                }
            }
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            BatteryProperty _arg1 = new BatteryProperty();
            int _result = getProperty(_arg0, _arg1);
            reply.writeNoException();
            reply.writeInt(_result);
            reply.writeInt(1);
            _arg1.writeToParcel(reply, 1);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IBatteryPropertiesRegistrar {
            public static IBatteryPropertiesRegistrar sDefaultImpl;
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

            @Override // android.os.IBatteryPropertiesRegistrar
            public int getProperty(int id, BatteryProperty prop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProperty(id, prop);
                    }
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
            public void scheduleUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleUpdate();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBatteryPropertiesRegistrar impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBatteryPropertiesRegistrar getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
