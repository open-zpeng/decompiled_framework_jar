package android.content.pm.dex;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ISnapshotRuntimeProfileCallback extends IInterface {
    void onError(int i) throws RemoteException;

    void onSuccess(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ISnapshotRuntimeProfileCallback {
        @Override // android.content.pm.dex.ISnapshotRuntimeProfileCallback
        public void onSuccess(ParcelFileDescriptor profileReadFd) throws RemoteException {
        }

        @Override // android.content.pm.dex.ISnapshotRuntimeProfileCallback
        public void onError(int errCode) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISnapshotRuntimeProfileCallback {
        private static final String DESCRIPTOR = "android.content.pm.dex.ISnapshotRuntimeProfileCallback";
        static final int TRANSACTION_onError = 2;
        static final int TRANSACTION_onSuccess = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISnapshotRuntimeProfileCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISnapshotRuntimeProfileCallback)) {
                return (ISnapshotRuntimeProfileCallback) iin;
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
                    return "onError";
                }
                return null;
            }
            return "onSuccess";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ParcelFileDescriptor _arg0;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg0 = ParcelFileDescriptor.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onSuccess(_arg0);
                return true;
            } else if (code != 2) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                int _arg02 = data.readInt();
                onError(_arg02);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements ISnapshotRuntimeProfileCallback {
            public static ISnapshotRuntimeProfileCallback sDefaultImpl;
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

            @Override // android.content.pm.dex.ISnapshotRuntimeProfileCallback
            public void onSuccess(ParcelFileDescriptor profileReadFd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profileReadFd != null) {
                        _data.writeInt(1);
                        profileReadFd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSuccess(profileReadFd);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.dex.ISnapshotRuntimeProfileCallback
            public void onError(int errCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(errCode);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onError(errCode);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISnapshotRuntimeProfileCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ISnapshotRuntimeProfileCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
