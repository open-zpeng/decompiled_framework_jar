package android.os;

/* loaded from: classes2.dex */
public interface Ifm1388Service extends IInterface {
    int getData(int i, long j) throws RemoteException;

    int getVECPath(byte[] bArr) throws RemoteException;

    int getVal(int i) throws RemoteException;

    void setData(int i, long j, int i2) throws RemoteException;

    void setVal(int i) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements Ifm1388Service {
        @Override // android.os.Ifm1388Service
        public void setVal(int val) throws RemoteException {
        }

        @Override // android.os.Ifm1388Service
        public int getVal(int val) throws RemoteException {
            return 0;
        }

        @Override // android.os.Ifm1388Service
        public void setData(int action, long RegAddr, int val) throws RemoteException {
        }

        @Override // android.os.Ifm1388Service
        public int getData(int action, long RegAddr) throws RemoteException {
            return 0;
        }

        @Override // android.os.Ifm1388Service
        public int getVECPath(byte[] VECPath) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements Ifm1388Service {
        private static final String DESCRIPTOR = "android.os.Ifm1388Service";
        static final int TRANSACTION_getData = 4;
        static final int TRANSACTION_getVECPath = 5;
        static final int TRANSACTION_getVal = 2;
        static final int TRANSACTION_setData = 3;
        static final int TRANSACTION_setVal = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static Ifm1388Service asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof Ifm1388Service)) {
                return (Ifm1388Service) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                if (transactionCode != 2) {
                    if (transactionCode != 3) {
                        if (transactionCode != 4) {
                            if (transactionCode == 5) {
                                return "getVECPath";
                            }
                            return null;
                        }
                        return "getData";
                    }
                    return "setData";
                }
                return "getVal";
            }
            return "setVal";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            byte[] _arg0;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                int _arg02 = data.readInt();
                setVal(_arg02);
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                int _arg03 = data.readInt();
                int _result = getVal(_arg03);
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 3) {
                data.enforceInterface(DESCRIPTOR);
                int _arg04 = data.readInt();
                long _arg1 = data.readLong();
                int _arg2 = data.readInt();
                setData(_arg04, _arg1, _arg2);
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                data.enforceInterface(DESCRIPTOR);
                int _arg05 = data.readInt();
                long _arg12 = data.readLong();
                int _result2 = getData(_arg05, _arg12);
                reply.writeNoException();
                reply.writeInt(_result2);
                return true;
            } else if (code != 5) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                int _arg0_length = data.readInt();
                if (_arg0_length < 0) {
                    _arg0 = null;
                } else {
                    _arg0 = new byte[_arg0_length];
                }
                int _result3 = getVECPath(_arg0);
                reply.writeNoException();
                reply.writeInt(_result3);
                reply.writeByteArray(_arg0);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements Ifm1388Service {
            public static Ifm1388Service sDefaultImpl;
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

            @Override // android.os.Ifm1388Service
            public void setVal(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVal(val);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388Service
            public int getVal(int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(val);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVal(val);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388Service
            public void setData(int action, long RegAddr, int val) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(action);
                    _data.writeLong(RegAddr);
                    _data.writeInt(val);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setData(action, RegAddr, val);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388Service
            public int getData(int action, long RegAddr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(action);
                    _data.writeLong(RegAddr);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getData(action, RegAddr);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388Service
            public int getVECPath(byte[] VECPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (VECPath == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(VECPath.length);
                    }
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVECPath(VECPath);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(VECPath);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(Ifm1388Service impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static Ifm1388Service getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
