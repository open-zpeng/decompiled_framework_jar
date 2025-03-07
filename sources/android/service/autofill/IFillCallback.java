package android.service.autofill;

import android.os.Binder;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;

/* loaded from: classes2.dex */
public interface IFillCallback extends IInterface {
    void onCancellable(ICancellationSignal iCancellationSignal) throws RemoteException;

    void onFailure(int i, CharSequence charSequence) throws RemoteException;

    void onSuccess(FillResponse fillResponse) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IFillCallback {
        @Override // android.service.autofill.IFillCallback
        public void onCancellable(ICancellationSignal cancellation) throws RemoteException {
        }

        @Override // android.service.autofill.IFillCallback
        public void onSuccess(FillResponse response) throws RemoteException {
        }

        @Override // android.service.autofill.IFillCallback
        public void onFailure(int requestId, CharSequence message) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IFillCallback {
        private static final String DESCRIPTOR = "android.service.autofill.IFillCallback";
        static final int TRANSACTION_onCancellable = 1;
        static final int TRANSACTION_onFailure = 3;
        static final int TRANSACTION_onSuccess = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFillCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IFillCallback)) {
                return (IFillCallback) iin;
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
                        return "onFailure";
                    }
                    return null;
                }
                return "onSuccess";
            }
            return "onCancellable";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            FillResponse _arg0;
            CharSequence _arg1;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                ICancellationSignal _arg02 = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                onCancellable(_arg02);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg0 = FillResponse.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onSuccess(_arg0);
                return true;
            } else if (code != 3) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                int _arg03 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                onFailure(_arg03, _arg1);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IFillCallback {
            public static IFillCallback sDefaultImpl;
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

            @Override // android.service.autofill.IFillCallback
            public void onCancellable(ICancellationSignal cancellation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cancellation != null ? cancellation.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onCancellable(cancellation);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.autofill.IFillCallback
            public void onSuccess(FillResponse response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSuccess(response);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.autofill.IFillCallback
            public void onFailure(int requestId, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestId);
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onFailure(requestId, message);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IFillCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IFillCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
