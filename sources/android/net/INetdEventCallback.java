package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface INetdEventCallback extends IInterface {
    public static final int CALLBACK_CALLER_CONNECTIVITY_SERVICE = 0;
    public static final int CALLBACK_CALLER_DEVICE_POLICY = 1;
    public static final int CALLBACK_CALLER_NETWORK_WATCHLIST = 2;

    synchronized void onConnectEvent(String str, int i, long j, int i2) throws RemoteException;

    synchronized void onDnsEvent(String str, String[] strArr, int i, long j, int i2) throws RemoteException;

    synchronized void onPrivateDnsValidationEvent(int i, String str, String str2, boolean z) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements INetdEventCallback {
        private static final String DESCRIPTOR = "android.net.INetdEventCallback";
        static final int TRANSACTION_onConnectEvent = 3;
        static final int TRANSACTION_onDnsEvent = 1;
        static final int TRANSACTION_onPrivateDnsValidationEvent = 2;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized INetdEventCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof INetdEventCallback)) {
                return (INetdEventCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg0 = data.readString();
                        String[] _arg1 = data.createStringArray();
                        int _arg2 = data.readInt();
                        long _arg3 = data.readLong();
                        int _arg4 = data.readInt();
                        onDnsEvent(_arg0, _arg1, _arg2, _arg3, _arg4);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg02 = data.readInt();
                        String _arg12 = data.readString();
                        String _arg22 = data.readString();
                        boolean _arg32 = data.readInt() != 0;
                        onPrivateDnsValidationEvent(_arg02, _arg12, _arg22, _arg32);
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg03 = data.readString();
                        int _arg13 = data.readInt();
                        long _arg23 = data.readLong();
                        int _arg33 = data.readInt();
                        onConnectEvent(_arg03, _arg13, _arg23, _arg33);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(DESCRIPTOR);
            return true;
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements INetdEventCallback {
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

            @Override // android.net.INetdEventCallback
            public synchronized void onDnsEvent(String hostname, String[] ipAddresses, int ipAddressesCount, long timestamp, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(hostname);
                    _data.writeStringArray(ipAddresses);
                    _data.writeInt(ipAddressesCount);
                    _data.writeLong(timestamp);
                    _data.writeInt(uid);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdEventCallback
            public synchronized void onPrivateDnsValidationEvent(int netId, String ipAddress, String hostname, boolean validated) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeString(ipAddress);
                    _data.writeString(hostname);
                    _data.writeInt(validated ? 1 : 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdEventCallback
            public synchronized void onConnectEvent(String ipAddr, int port, long timestamp, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ipAddr);
                    _data.writeInt(port);
                    _data.writeLong(timestamp);
                    _data.writeInt(uid);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
