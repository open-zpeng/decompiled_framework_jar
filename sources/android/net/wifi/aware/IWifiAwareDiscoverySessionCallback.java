package android.net.wifi.aware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IWifiAwareDiscoverySessionCallback extends IInterface {
    synchronized void onMatch(int i, byte[] bArr, byte[] bArr2) throws RemoteException;

    synchronized void onMatchWithDistance(int i, byte[] bArr, byte[] bArr2, int i2) throws RemoteException;

    synchronized void onMessageReceived(int i, byte[] bArr) throws RemoteException;

    synchronized void onMessageSendFail(int i, int i2) throws RemoteException;

    synchronized void onMessageSendSuccess(int i) throws RemoteException;

    synchronized void onSessionConfigFail(int i) throws RemoteException;

    synchronized void onSessionConfigSuccess() throws RemoteException;

    synchronized void onSessionStarted(int i) throws RemoteException;

    synchronized void onSessionTerminated(int i) throws RemoteException;

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

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IWifiAwareDiscoverySessionCallback asInterface(IBinder obj) {
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

        /* loaded from: classes2.dex */
        private static class Proxy implements IWifiAwareDiscoverySessionCallback {
            private IBinder mRemote;

            synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public synchronized void onSessionStarted(int discoverySessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(discoverySessionId);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public synchronized void onSessionConfigSuccess() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public synchronized void onSessionConfigFail(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public synchronized void onSessionTerminated(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public synchronized void onMatch(int peerId, byte[] serviceSpecificInfo, byte[] matchFilter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(peerId);
                    _data.writeByteArray(serviceSpecificInfo);
                    _data.writeByteArray(matchFilter);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public synchronized void onMatchWithDistance(int peerId, byte[] serviceSpecificInfo, byte[] matchFilter, int distanceMm) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(peerId);
                    _data.writeByteArray(serviceSpecificInfo);
                    _data.writeByteArray(matchFilter);
                    _data.writeInt(distanceMm);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public synchronized void onMessageSendSuccess(int messageId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public synchronized void onMessageSendFail(int messageId, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(messageId);
                    _data.writeInt(reason);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
            public synchronized void onMessageReceived(int peerId, byte[] message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(peerId);
                    _data.writeByteArray(message);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
