package android.service.carrier;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface ICarrierMessagingCallback extends IInterface {
    void onDownloadMmsComplete(int i) throws RemoteException;

    void onFilterComplete(int i) throws RemoteException;

    void onSendMmsComplete(int i, byte[] bArr) throws RemoteException;

    void onSendMultipartSmsComplete(int i, int[] iArr) throws RemoteException;

    void onSendSmsComplete(int i, int i2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements ICarrierMessagingCallback {
        @Override // android.service.carrier.ICarrierMessagingCallback
        public void onFilterComplete(int result) throws RemoteException {
        }

        @Override // android.service.carrier.ICarrierMessagingCallback
        public void onSendSmsComplete(int result, int messageRef) throws RemoteException {
        }

        @Override // android.service.carrier.ICarrierMessagingCallback
        public void onSendMultipartSmsComplete(int result, int[] messageRefs) throws RemoteException {
        }

        @Override // android.service.carrier.ICarrierMessagingCallback
        public void onSendMmsComplete(int result, byte[] sendConfPdu) throws RemoteException {
        }

        @Override // android.service.carrier.ICarrierMessagingCallback
        public void onDownloadMmsComplete(int result) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ICarrierMessagingCallback {
        private static final String DESCRIPTOR = "android.service.carrier.ICarrierMessagingCallback";
        static final int TRANSACTION_onDownloadMmsComplete = 5;
        static final int TRANSACTION_onFilterComplete = 1;
        static final int TRANSACTION_onSendMmsComplete = 4;
        static final int TRANSACTION_onSendMultipartSmsComplete = 3;
        static final int TRANSACTION_onSendSmsComplete = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICarrierMessagingCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ICarrierMessagingCallback)) {
                return (ICarrierMessagingCallback) iin;
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
                                return "onDownloadMmsComplete";
                            }
                            return null;
                        }
                        return "onSendMmsComplete";
                    }
                    return "onSendMultipartSmsComplete";
                }
                return "onSendSmsComplete";
            }
            return "onFilterComplete";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                int _arg0 = data.readInt();
                onFilterComplete(_arg0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                int _arg02 = data.readInt();
                int _arg1 = data.readInt();
                onSendSmsComplete(_arg02, _arg1);
                return true;
            } else if (code == 3) {
                data.enforceInterface(DESCRIPTOR);
                int _arg03 = data.readInt();
                int[] _arg12 = data.createIntArray();
                onSendMultipartSmsComplete(_arg03, _arg12);
                return true;
            } else if (code == 4) {
                data.enforceInterface(DESCRIPTOR);
                int _arg04 = data.readInt();
                byte[] _arg13 = data.createByteArray();
                onSendMmsComplete(_arg04, _arg13);
                return true;
            } else if (code != 5) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                int _arg05 = data.readInt();
                onDownloadMmsComplete(_arg05);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements ICarrierMessagingCallback {
            public static ICarrierMessagingCallback sDefaultImpl;
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

            @Override // android.service.carrier.ICarrierMessagingCallback
            public void onFilterComplete(int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onFilterComplete(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.carrier.ICarrierMessagingCallback
            public void onSendSmsComplete(int result, int messageRef) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeInt(messageRef);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSendSmsComplete(result, messageRef);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.carrier.ICarrierMessagingCallback
            public void onSendMultipartSmsComplete(int result, int[] messageRefs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeIntArray(messageRefs);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSendMultipartSmsComplete(result, messageRefs);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.carrier.ICarrierMessagingCallback
            public void onSendMmsComplete(int result, byte[] sendConfPdu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeByteArray(sendConfPdu);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSendMmsComplete(result, sendConfPdu);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.carrier.ICarrierMessagingCallback
            public void onDownloadMmsComplete(int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onDownloadMmsComplete(result);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ICarrierMessagingCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ICarrierMessagingCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
