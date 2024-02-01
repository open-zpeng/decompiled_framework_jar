package android.os;
/* loaded from: classes2.dex */
public interface ISchedulingPolicyService extends IInterface {
    synchronized int requestCpusetBoost(boolean z, IBinder iBinder) throws RemoteException;

    synchronized int requestPriority(int i, int i2, int i3, boolean z) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ISchedulingPolicyService {
        private static final String DESCRIPTOR = "android.os.ISchedulingPolicyService";
        static final int TRANSACTION_requestCpusetBoost = 2;
        static final int TRANSACTION_requestPriority = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ISchedulingPolicyService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISchedulingPolicyService)) {
                return (ISchedulingPolicyService) iin;
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
                    int _arg02 = data.readInt();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    _arg0 = data.readInt() != 0;
                    int _result = requestPriority(_arg02, _arg1, _arg2, _arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    IBinder _arg12 = data.readStrongBinder();
                    int _result2 = requestCpusetBoost(_arg0, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ISchedulingPolicyService {
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

            @Override // android.os.ISchedulingPolicyService
            public synchronized int requestPriority(int pid, int tid, int prio, boolean isForApp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(tid);
                    _data.writeInt(prio);
                    _data.writeInt(isForApp ? 1 : 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISchedulingPolicyService
            public synchronized int requestCpusetBoost(boolean enable, IBinder client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeStrongBinder(client);
                    this.mRemote.transact(2, _data, _reply, 0);
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
