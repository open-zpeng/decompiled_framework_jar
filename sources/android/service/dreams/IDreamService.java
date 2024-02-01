package android.service.dreams;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface IDreamService extends IInterface {
    void attach(IBinder iBinder, boolean z, IRemoteCallback iRemoteCallback) throws RemoteException;

    void detach() throws RemoteException;

    void wakeUp() throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IDreamService {
        @Override // android.service.dreams.IDreamService
        public void attach(IBinder windowToken, boolean canDoze, IRemoteCallback started) throws RemoteException {
        }

        @Override // android.service.dreams.IDreamService
        public void detach() throws RemoteException {
        }

        @Override // android.service.dreams.IDreamService
        public void wakeUp() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IDreamService {
        private static final String DESCRIPTOR = "android.service.dreams.IDreamService";
        static final int TRANSACTION_attach = 1;
        static final int TRANSACTION_detach = 2;
        static final int TRANSACTION_wakeUp = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDreamService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IDreamService)) {
                return (IDreamService) iin;
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
                    if (transactionCode == 3) {
                        return "wakeUp";
                    }
                    return null;
                }
                return "detach";
            }
            return "attach";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                IBinder _arg0 = data.readStrongBinder();
                boolean _arg1 = data.readInt() != 0;
                IRemoteCallback _arg2 = IRemoteCallback.Stub.asInterface(data.readStrongBinder());
                attach(_arg0, _arg1, _arg2);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                detach();
                return true;
            } else if (code == 3) {
                data.enforceInterface(DESCRIPTOR);
                wakeUp();
                return true;
            } else if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            } else {
                return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IDreamService {
            public static IDreamService sDefaultImpl;
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

            @Override // android.service.dreams.IDreamService
            public void attach(IBinder windowToken, boolean canDoze, IRemoteCallback started) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken);
                    _data.writeInt(canDoze ? 1 : 0);
                    _data.writeStrongBinder(started != null ? started.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().attach(windowToken, canDoze, started);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.dreams.IDreamService
            public void detach() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().detach();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.dreams.IDreamService
            public void wakeUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().wakeUp();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IDreamService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IDreamService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
