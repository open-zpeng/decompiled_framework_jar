package android.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface INetworkServiceCallback extends IInterface {
    void onNetworkStateChanged() throws RemoteException;

    void onRequestNetworkRegistrationInfoComplete(int i, NetworkRegistrationInfo networkRegistrationInfo) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements INetworkServiceCallback {
        @Override // android.telephony.INetworkServiceCallback
        public void onRequestNetworkRegistrationInfoComplete(int result, NetworkRegistrationInfo state) throws RemoteException {
        }

        @Override // android.telephony.INetworkServiceCallback
        public void onNetworkStateChanged() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements INetworkServiceCallback {
        private static final String DESCRIPTOR = "android.telephony.INetworkServiceCallback";
        static final int TRANSACTION_onNetworkStateChanged = 2;
        static final int TRANSACTION_onRequestNetworkRegistrationInfoComplete = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INetworkServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof INetworkServiceCallback)) {
                return (INetworkServiceCallback) iin;
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
                    return "onNetworkStateChanged";
                }
                return null;
            }
            return "onRequestNetworkRegistrationInfoComplete";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            NetworkRegistrationInfo _arg1;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                int _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = NetworkRegistrationInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                onRequestNetworkRegistrationInfoComplete(_arg0, _arg1);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                onNetworkStateChanged();
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
        public static class Proxy implements INetworkServiceCallback {
            public static INetworkServiceCallback sDefaultImpl;
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

            @Override // android.telephony.INetworkServiceCallback
            public void onRequestNetworkRegistrationInfoComplete(int result, NetworkRegistrationInfo state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onRequestNetworkRegistrationInfoComplete(result, state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.telephony.INetworkServiceCallback
            public void onNetworkStateChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onNetworkStateChanged();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(INetworkServiceCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static INetworkServiceCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
