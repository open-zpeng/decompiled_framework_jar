package android.telephony.mbms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface IStreamingServiceCallback extends IInterface {
    void onBroadcastSignalStrengthUpdated(int i) throws RemoteException;

    void onError(int i, String str) throws RemoteException;

    void onMediaDescriptionUpdated() throws RemoteException;

    void onStreamMethodUpdated(int i) throws RemoteException;

    void onStreamStateUpdated(int i, int i2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IStreamingServiceCallback {
        @Override // android.telephony.mbms.IStreamingServiceCallback
        public void onError(int errorCode, String message) throws RemoteException {
        }

        @Override // android.telephony.mbms.IStreamingServiceCallback
        public void onStreamStateUpdated(int state, int reason) throws RemoteException {
        }

        @Override // android.telephony.mbms.IStreamingServiceCallback
        public void onMediaDescriptionUpdated() throws RemoteException {
        }

        @Override // android.telephony.mbms.IStreamingServiceCallback
        public void onBroadcastSignalStrengthUpdated(int signalStrength) throws RemoteException {
        }

        @Override // android.telephony.mbms.IStreamingServiceCallback
        public void onStreamMethodUpdated(int methodType) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IStreamingServiceCallback {
        private static final String DESCRIPTOR = "android.telephony.mbms.IStreamingServiceCallback";
        static final int TRANSACTION_onBroadcastSignalStrengthUpdated = 4;
        static final int TRANSACTION_onError = 1;
        static final int TRANSACTION_onMediaDescriptionUpdated = 3;
        static final int TRANSACTION_onStreamMethodUpdated = 5;
        static final int TRANSACTION_onStreamStateUpdated = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStreamingServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IStreamingServiceCallback)) {
                return (IStreamingServiceCallback) iin;
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
                                return "onStreamMethodUpdated";
                            }
                            return null;
                        }
                        return "onBroadcastSignalStrengthUpdated";
                    }
                    return "onMediaDescriptionUpdated";
                }
                return "onStreamStateUpdated";
            }
            return "onError";
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
                String _arg1 = data.readString();
                onError(_arg0, _arg1);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                int _arg02 = data.readInt();
                int _arg12 = data.readInt();
                onStreamStateUpdated(_arg02, _arg12);
                return true;
            } else if (code == 3) {
                data.enforceInterface(DESCRIPTOR);
                onMediaDescriptionUpdated();
                return true;
            } else if (code == 4) {
                data.enforceInterface(DESCRIPTOR);
                int _arg03 = data.readInt();
                onBroadcastSignalStrengthUpdated(_arg03);
                return true;
            } else if (code != 5) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                int _arg04 = data.readInt();
                onStreamMethodUpdated(_arg04);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IStreamingServiceCallback {
            public static IStreamingServiceCallback sDefaultImpl;
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

            @Override // android.telephony.mbms.IStreamingServiceCallback
            public void onError(int errorCode, String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(errorCode);
                    _data.writeString(message);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onError(errorCode, message);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.IStreamingServiceCallback
            public void onStreamStateUpdated(int state, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeInt(reason);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStreamStateUpdated(state, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.IStreamingServiceCallback
            public void onMediaDescriptionUpdated() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onMediaDescriptionUpdated();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.IStreamingServiceCallback
            public void onBroadcastSignalStrengthUpdated(int signalStrength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(signalStrength);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBroadcastSignalStrengthUpdated(signalStrength);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.IStreamingServiceCallback
            public void onStreamMethodUpdated(int methodType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(methodType);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStreamMethodUpdated(methodType);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IStreamingServiceCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IStreamingServiceCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
