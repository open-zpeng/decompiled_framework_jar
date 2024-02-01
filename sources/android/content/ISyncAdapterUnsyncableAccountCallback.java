package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ISyncAdapterUnsyncableAccountCallback extends IInterface {
    void onUnsyncableAccountDone(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ISyncAdapterUnsyncableAccountCallback {
        @Override // android.content.ISyncAdapterUnsyncableAccountCallback
        public void onUnsyncableAccountDone(boolean isReady) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISyncAdapterUnsyncableAccountCallback {
        private static final String DESCRIPTOR = "android.content.ISyncAdapterUnsyncableAccountCallback";
        static final int TRANSACTION_onUnsyncableAccountDone = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISyncAdapterUnsyncableAccountCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISyncAdapterUnsyncableAccountCallback)) {
                return (ISyncAdapterUnsyncableAccountCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onUnsyncableAccountDone";
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
            boolean _arg0 = data.readInt() != 0;
            onUnsyncableAccountDone(_arg0);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements ISyncAdapterUnsyncableAccountCallback {
            public static ISyncAdapterUnsyncableAccountCallback sDefaultImpl;
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

            @Override // android.content.ISyncAdapterUnsyncableAccountCallback
            public void onUnsyncableAccountDone(boolean isReady) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isReady ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onUnsyncableAccountDone(isReady);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISyncAdapterUnsyncableAccountCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ISyncAdapterUnsyncableAccountCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
