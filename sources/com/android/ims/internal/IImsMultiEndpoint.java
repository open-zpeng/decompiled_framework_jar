package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.ims.internal.IImsExternalCallStateListener;

/* loaded from: classes3.dex */
public interface IImsMultiEndpoint extends IInterface {
    void requestImsExternalCallStateInfo() throws RemoteException;

    void setListener(IImsExternalCallStateListener iImsExternalCallStateListener) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IImsMultiEndpoint {
        @Override // com.android.ims.internal.IImsMultiEndpoint
        public void setListener(IImsExternalCallStateListener listener) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsMultiEndpoint
        public void requestImsExternalCallStateInfo() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IImsMultiEndpoint {
        private static final String DESCRIPTOR = "com.android.ims.internal.IImsMultiEndpoint";
        static final int TRANSACTION_requestImsExternalCallStateInfo = 2;
        static final int TRANSACTION_setListener = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsMultiEndpoint asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IImsMultiEndpoint)) {
                return (IImsMultiEndpoint) iin;
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
                    return "requestImsExternalCallStateInfo";
                }
                return null;
            }
            return "setListener";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                IImsExternalCallStateListener _arg0 = IImsExternalCallStateListener.Stub.asInterface(data.readStrongBinder());
                setListener(_arg0);
                reply.writeNoException();
                return true;
            } else if (code != 2) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                requestImsExternalCallStateInfo();
                reply.writeNoException();
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IImsMultiEndpoint {
            public static IImsMultiEndpoint sDefaultImpl;
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

            @Override // com.android.ims.internal.IImsMultiEndpoint
            public void setListener(IImsExternalCallStateListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setListener(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsMultiEndpoint
            public void requestImsExternalCallStateInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestImsExternalCallStateInfo();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IImsMultiEndpoint impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IImsMultiEndpoint getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
