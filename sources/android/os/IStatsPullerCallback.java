package android.os;

/* loaded from: classes2.dex */
public interface IStatsPullerCallback extends IInterface {
    StatsLogEventWrapper[] pullData(int i, long j, long j2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IStatsPullerCallback {
        @Override // android.os.IStatsPullerCallback
        public StatsLogEventWrapper[] pullData(int atomTag, long elapsedNanos, long wallClocknanos) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IStatsPullerCallback {
        private static final String DESCRIPTOR = "android.os.IStatsPullerCallback";
        static final int TRANSACTION_pullData = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStatsPullerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IStatsPullerCallback)) {
                return (IStatsPullerCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "pullData";
            }
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            long _arg1 = data.readLong();
            long _arg2 = data.readLong();
            StatsLogEventWrapper[] _result = pullData(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeTypedArray(_result, 1);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IStatsPullerCallback {
            public static IStatsPullerCallback sDefaultImpl;
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

            @Override // android.os.IStatsPullerCallback
            public StatsLogEventWrapper[] pullData(int atomTag, long elapsedNanos, long wallClocknanos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(atomTag);
                    _data.writeLong(elapsedNanos);
                    _data.writeLong(wallClocknanos);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().pullData(atomTag, elapsedNanos, wallClocknanos);
                    }
                    _reply.readException();
                    StatsLogEventWrapper[] _result = (StatsLogEventWrapper[]) _reply.createTypedArray(StatsLogEventWrapper.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IStatsPullerCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IStatsPullerCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
