package android.companion;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ICompanionDeviceDiscoveryServiceCallback extends IInterface {
    void onDeviceSelected(String str, int i, String str2) throws RemoteException;

    void onDeviceSelectionCancel() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICompanionDeviceDiscoveryServiceCallback {
        @Override // android.companion.ICompanionDeviceDiscoveryServiceCallback
        public void onDeviceSelected(String packageName, int userId, String deviceAddress) throws RemoteException {
        }

        @Override // android.companion.ICompanionDeviceDiscoveryServiceCallback
        public void onDeviceSelectionCancel() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICompanionDeviceDiscoveryServiceCallback {
        private static final String DESCRIPTOR = "android.companion.ICompanionDeviceDiscoveryServiceCallback";
        static final int TRANSACTION_onDeviceSelected = 1;
        static final int TRANSACTION_onDeviceSelectionCancel = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICompanionDeviceDiscoveryServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ICompanionDeviceDiscoveryServiceCallback)) {
                return (ICompanionDeviceDiscoveryServiceCallback) iin;
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
                    return "onDeviceSelectionCancel";
                }
                return null;
            }
            return "onDeviceSelected";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                String _arg0 = data.readString();
                int _arg1 = data.readInt();
                String _arg2 = data.readString();
                onDeviceSelected(_arg0, _arg1, _arg2);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                onDeviceSelectionCancel();
                return true;
            } else if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            } else {
                return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements ICompanionDeviceDiscoveryServiceCallback {
            public static ICompanionDeviceDiscoveryServiceCallback sDefaultImpl;
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

            @Override // android.companion.ICompanionDeviceDiscoveryServiceCallback
            public void onDeviceSelected(String packageName, int userId, String deviceAddress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(deviceAddress);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onDeviceSelected(packageName, userId, deviceAddress);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.companion.ICompanionDeviceDiscoveryServiceCallback
            public void onDeviceSelectionCancel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onDeviceSelectionCancel();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ICompanionDeviceDiscoveryServiceCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ICompanionDeviceDiscoveryServiceCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
