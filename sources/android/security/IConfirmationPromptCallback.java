package android.security;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface IConfirmationPromptCallback extends IInterface {
    void onConfirmationPromptCompleted(int i, byte[] bArr) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IConfirmationPromptCallback {
        @Override // android.security.IConfirmationPromptCallback
        public void onConfirmationPromptCompleted(int result, byte[] dataThatWasConfirmed) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IConfirmationPromptCallback {
        private static final String DESCRIPTOR = "android.security.IConfirmationPromptCallback";
        static final int TRANSACTION_onConfirmationPromptCompleted = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IConfirmationPromptCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IConfirmationPromptCallback)) {
                return (IConfirmationPromptCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onConfirmationPromptCompleted";
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
            byte[] _arg1 = data.createByteArray();
            onConfirmationPromptCompleted(_arg0, _arg1);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IConfirmationPromptCallback {
            public static IConfirmationPromptCallback sDefaultImpl;
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

            @Override // android.security.IConfirmationPromptCallback
            public void onConfirmationPromptCompleted(int result, byte[] dataThatWasConfirmed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeByteArray(dataThatWasConfirmed);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onConfirmationPromptCompleted(result, dataThatWasConfirmed);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IConfirmationPromptCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IConfirmationPromptCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
