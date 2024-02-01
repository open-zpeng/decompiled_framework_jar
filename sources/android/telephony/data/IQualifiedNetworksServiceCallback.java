package android.telephony.data;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface IQualifiedNetworksServiceCallback extends IInterface {
    void onQualifiedNetworkTypesChanged(int i, int[] iArr) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IQualifiedNetworksServiceCallback {
        @Override // android.telephony.data.IQualifiedNetworksServiceCallback
        public void onQualifiedNetworkTypesChanged(int apnTypes, int[] qualifiedNetworkTypes) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IQualifiedNetworksServiceCallback {
        private static final String DESCRIPTOR = "android.telephony.data.IQualifiedNetworksServiceCallback";
        static final int TRANSACTION_onQualifiedNetworkTypesChanged = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IQualifiedNetworksServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IQualifiedNetworksServiceCallback)) {
                return (IQualifiedNetworksServiceCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onQualifiedNetworkTypesChanged";
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
            int[] _arg1 = data.createIntArray();
            onQualifiedNetworkTypesChanged(_arg0, _arg1);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IQualifiedNetworksServiceCallback {
            public static IQualifiedNetworksServiceCallback sDefaultImpl;
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

            @Override // android.telephony.data.IQualifiedNetworksServiceCallback
            public void onQualifiedNetworkTypesChanged(int apnTypes, int[] qualifiedNetworkTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apnTypes);
                    _data.writeIntArray(qualifiedNetworkTypes);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onQualifiedNetworkTypesChanged(apnTypes, qualifiedNetworkTypes);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IQualifiedNetworksServiceCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IQualifiedNetworksServiceCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
