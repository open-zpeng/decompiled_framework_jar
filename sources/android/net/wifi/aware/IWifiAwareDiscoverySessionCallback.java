package android.net.wifi.aware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface IWifiAwareDiscoverySessionCallback extends IInterface {
    void onMatch(int i, byte[] bArr, byte[] bArr2) throws RemoteException;

    void onMatchWithDistance(int i, byte[] bArr, byte[] bArr2, int i2) throws RemoteException;

    void onMessageReceived(int i, byte[] bArr) throws RemoteException;

    void onMessageSendFail(int i, int i2) throws RemoteException;

    void onMessageSendSuccess(int i) throws RemoteException;

    void onSessionConfigFail(int i) throws RemoteException;

    void onSessionConfigSuccess() throws RemoteException;

    void onSessionStarted(int i) throws RemoteException;

    void onSessionTerminated(int i) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IWifiAwareDiscoverySessionCallback {
        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public void onSessionStarted(int discoverySessionId) throws RemoteException {
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public void onSessionConfigSuccess() throws RemoteException {
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public void onSessionConfigFail(int reason) throws RemoteException {
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public void onSessionTerminated(int reason) throws RemoteException {
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public void onMatch(int peerId, byte[] serviceSpecificInfo, byte[] matchFilter) throws RemoteException {
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public void onMatchWithDistance(int peerId, byte[] serviceSpecificInfo, byte[] matchFilter, int distanceMm) throws RemoteException {
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public void onMessageSendSuccess(int messageId) throws RemoteException {
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public void onMessageSendFail(int messageId, int reason) throws RemoteException {
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public void onMessageReceived(int peerId, byte[] message) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IWifiAwareDiscoverySessionCallback {
        private static final String DESCRIPTOR = "android.net.wifi.aware.IWifiAwareDiscoverySessionCallback";
        static final int TRANSACTION_onMatch = 5;
        static final int TRANSACTION_onMatchWithDistance = 6;
        static final int TRANSACTION_onMessageReceived = 9;
        static final int TRANSACTION_onMessageSendFail = 8;
        static final int TRANSACTION_onMessageSendSuccess = 7;
        static final int TRANSACTION_onSessionConfigFail = 3;
        static final int TRANSACTION_onSessionConfigSuccess = 2;
        static final int TRANSACTION_onSessionStarted = 1;
        static final int TRANSACTION_onSessionTerminated = 4;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiAwareDiscoverySessionCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IWifiAwareDiscoverySessionCallback)) {
                return (IWifiAwareDiscoverySessionCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onSessionStarted";
                case 2:
                    return "onSessionConfigSuccess";
                case 3:
                    return "onSessionConfigFail";
                case 4:
                    return "onSessionTerminated";
                case 5:
                    return "onMatch";
                case 6:
                    return "onMatchWithDistance";
                case 7:
                    return "onMessageSendSuccess";
                case 8:
                    return "onMessageSendFail";
                case 9:
                    return "onMessageReceived";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    onSessionStarted(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onSessionConfigSuccess();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    onSessionConfigFail(_arg02);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    onSessionTerminated(_arg03);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    byte[] _arg1 = data.createByteArray();
                    byte[] _arg2 = data.createByteArray();
                    onMatch(_arg04, _arg1, _arg2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    byte[] _arg12 = data.createByteArray();
                    byte[] _arg22 = data.createByteArray();
                    int _arg3 = data.readInt();
                    onMatchWithDistance(_arg05, _arg12, _arg22, _arg3);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    onMessageSendSuccess(_arg06);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    int _arg13 = data.readInt();
                    onMessageSendFail(_arg07, _arg13);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    byte[] _arg14 = data.createByteArray();
                    onMessageReceived(_arg08, _arg14);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IWifiAwareDiscoverySessionCallback {
            public static IWifiAwareDiscoverySessionCallback sDefaultImpl;
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

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public void onSessionStarted(int discoverySessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(discoverySessionId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSessionStarted(discoverySessionId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public void onSessionConfigSuccess() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSessionConfigSuccess();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public void onSessionConfigFail(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSessionConfigFail(reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public void onSessionTerminated(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSessionTerminated(reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public void onMatch(int peerId, byte[] serviceSpecificInfo, byte[] matchFilter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(peerId);
                    _data.writeByteArray(serviceSpecificInfo);
                    _data.writeByteArray(matchFilter);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onMatch(peerId, serviceSpecificInfo, matchFilter);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public void onMatchWithDistance(int peerId, byte[] serviceSpecificInfo, byte[] matchFilter, int distanceMm) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(peerId);
                    _data.writeByteArray(serviceSpecificInfo);
                    _data.writeByteArray(matchFilter);
                    _data.writeInt(distanceMm);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onMatchWithDistance(peerId, serviceSpecificInfo, matchFilter, distanceMm);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public void onMessageSendSuccess(int messageId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onMessageSendSuccess(messageId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public void onMessageSendFail(int messageId, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(reason);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onMessageSendFail(messageId, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public void onMessageReceived(int peerId, byte[] message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(peerId);
                    _data.writeByteArray(message);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onMessageReceived(peerId, message);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IWifiAwareDiscoverySessionCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IWifiAwareDiscoverySessionCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
