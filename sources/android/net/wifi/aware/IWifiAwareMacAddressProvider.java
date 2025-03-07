package android.net.wifi.aware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

/* loaded from: classes2.dex */
public interface IWifiAwareMacAddressProvider extends IInterface {
    void macAddress(Map map) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IWifiAwareMacAddressProvider {
        @Override // android.net.wifi.aware.IWifiAwareMacAddressProvider
        public void macAddress(Map peerIdToMacMap) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IWifiAwareMacAddressProvider {
        private static final String DESCRIPTOR = "android.net.wifi.aware.IWifiAwareMacAddressProvider";
        static final int TRANSACTION_macAddress = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiAwareMacAddressProvider asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IWifiAwareMacAddressProvider)) {
                return (IWifiAwareMacAddressProvider) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "macAddress";
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
            ClassLoader cl = getClass().getClassLoader();
            Map _arg0 = data.readHashMap(cl);
            macAddress(_arg0);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IWifiAwareMacAddressProvider {
            public static IWifiAwareMacAddressProvider sDefaultImpl;
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

            @Override // android.net.wifi.aware.IWifiAwareMacAddressProvider
            public void macAddress(Map peerIdToMacMap) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(peerIdToMacMap);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().macAddress(peerIdToMacMap);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IWifiAwareMacAddressProvider impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IWifiAwareMacAddressProvider getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
