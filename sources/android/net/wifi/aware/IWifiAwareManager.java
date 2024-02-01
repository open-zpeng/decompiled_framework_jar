package android.net.wifi.aware;

import android.net.wifi.aware.IWifiAwareDiscoverySessionCallback;
import android.net.wifi.aware.IWifiAwareEventCallback;
import android.net.wifi.aware.IWifiAwareMacAddressProvider;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes2.dex */
public interface IWifiAwareManager extends IInterface {
    synchronized void connect(IBinder iBinder, String str, IWifiAwareEventCallback iWifiAwareEventCallback, ConfigRequest configRequest, boolean z) throws RemoteException;

    synchronized void disconnect(int i, IBinder iBinder) throws RemoteException;

    synchronized Characteristics getCharacteristics() throws RemoteException;

    synchronized boolean isUsageEnabled() throws RemoteException;

    synchronized void publish(String str, int i, PublishConfig publishConfig, IWifiAwareDiscoverySessionCallback iWifiAwareDiscoverySessionCallback) throws RemoteException;

    synchronized void requestMacAddresses(int i, List list, IWifiAwareMacAddressProvider iWifiAwareMacAddressProvider) throws RemoteException;

    synchronized void sendMessage(int i, int i2, int i3, byte[] bArr, int i4, int i5) throws RemoteException;

    synchronized void subscribe(String str, int i, SubscribeConfig subscribeConfig, IWifiAwareDiscoverySessionCallback iWifiAwareDiscoverySessionCallback) throws RemoteException;

    synchronized void terminateSession(int i, int i2) throws RemoteException;

    synchronized void updatePublish(int i, int i2, PublishConfig publishConfig) throws RemoteException;

    synchronized void updateSubscribe(int i, int i2, SubscribeConfig subscribeConfig) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IWifiAwareManager {
        private static final String DESCRIPTOR = "android.net.wifi.aware.IWifiAwareManager";
        static final int TRANSACTION_connect = 3;
        static final int TRANSACTION_disconnect = 4;
        static final int TRANSACTION_getCharacteristics = 2;
        static final int TRANSACTION_isUsageEnabled = 1;
        static final int TRANSACTION_publish = 5;
        static final int TRANSACTION_requestMacAddresses = 11;
        static final int TRANSACTION_sendMessage = 9;
        static final int TRANSACTION_subscribe = 6;
        static final int TRANSACTION_terminateSession = 10;
        static final int TRANSACTION_updatePublish = 7;
        static final int TRANSACTION_updateSubscribe = 8;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IWifiAwareManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IWifiAwareManager)) {
                return (IWifiAwareManager) iin;
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
                    boolean isUsageEnabled = isUsageEnabled();
                    reply.writeNoException();
                    reply.writeInt(isUsageEnabled ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    Characteristics _result = getCharacteristics();
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0 = data.readStrongBinder();
                    String _arg1 = data.readString();
                    IWifiAwareEventCallback _arg2 = IWifiAwareEventCallback.Stub.asInterface(data.readStrongBinder());
                    ConfigRequest _arg3 = data.readInt() != 0 ? ConfigRequest.CREATOR.createFromParcel(data) : null;
                    boolean _arg4 = data.readInt() != 0;
                    connect(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    IBinder _arg12 = data.readStrongBinder();
                    disconnect(_arg02, _arg12);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    int _arg13 = data.readInt();
                    PublishConfig _arg22 = data.readInt() != 0 ? PublishConfig.CREATOR.createFromParcel(data) : null;
                    IWifiAwareDiscoverySessionCallback _arg32 = IWifiAwareDiscoverySessionCallback.Stub.asInterface(data.readStrongBinder());
                    publish(_arg03, _arg13, _arg22, _arg32);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    int _arg14 = data.readInt();
                    SubscribeConfig _arg23 = data.readInt() != 0 ? SubscribeConfig.CREATOR.createFromParcel(data) : null;
                    IWifiAwareDiscoverySessionCallback _arg33 = IWifiAwareDiscoverySessionCallback.Stub.asInterface(data.readStrongBinder());
                    subscribe(_arg04, _arg14, _arg23, _arg33);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    int _arg15 = data.readInt();
                    PublishConfig _arg24 = data.readInt() != 0 ? PublishConfig.CREATOR.createFromParcel(data) : null;
                    updatePublish(_arg05, _arg15, _arg24);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    int _arg16 = data.readInt();
                    SubscribeConfig _arg25 = data.readInt() != 0 ? SubscribeConfig.CREATOR.createFromParcel(data) : null;
                    updateSubscribe(_arg06, _arg16, _arg25);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    int _arg17 = data.readInt();
                    int _arg26 = data.readInt();
                    byte[] _arg34 = data.createByteArray();
                    int _arg42 = data.readInt();
                    int _arg5 = data.readInt();
                    sendMessage(_arg07, _arg17, _arg26, _arg34, _arg42, _arg5);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    int _arg18 = data.readInt();
                    terminateSession(_arg08, _arg18);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    ClassLoader cl = getClass().getClassLoader();
                    List _arg19 = data.readArrayList(cl);
                    IWifiAwareMacAddressProvider _arg27 = IWifiAwareMacAddressProvider.Stub.asInterface(data.readStrongBinder());
                    requestMacAddresses(_arg09, _arg19, _arg27);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IWifiAwareManager {
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

            @Override // android.net.wifi.aware.IWifiAwareManager
            public synchronized boolean isUsageEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareManager
            public synchronized Characteristics getCharacteristics() throws RemoteException {
                Characteristics _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Characteristics.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareManager
            public synchronized void connect(IBinder binder, String callingPackage, IWifiAwareEventCallback callback, ConfigRequest configRequest, boolean notifyOnIdentityChanged) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (configRequest != null) {
                        _data.writeInt(1);
                        configRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(notifyOnIdentityChanged ? 1 : 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareManager
            public synchronized void disconnect(int clientId, IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeStrongBinder(binder);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareManager
            public synchronized void publish(String callingPackage, int clientId, PublishConfig publishConfig, IWifiAwareDiscoverySessionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(clientId);
                    if (publishConfig != null) {
                        _data.writeInt(1);
                        publishConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareManager
            public synchronized void subscribe(String callingPackage, int clientId, SubscribeConfig subscribeConfig, IWifiAwareDiscoverySessionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(clientId);
                    if (subscribeConfig != null) {
                        _data.writeInt(1);
                        subscribeConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareManager
            public synchronized void updatePublish(int clientId, int discoverySessionId, PublishConfig publishConfig) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(discoverySessionId);
                    if (publishConfig != null) {
                        _data.writeInt(1);
                        publishConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareManager
            public synchronized void updateSubscribe(int clientId, int discoverySessionId, SubscribeConfig subscribeConfig) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(discoverySessionId);
                    if (subscribeConfig != null) {
                        _data.writeInt(1);
                        subscribeConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareManager
            public synchronized void sendMessage(int clientId, int discoverySessionId, int peerId, byte[] message, int messageId, int retryCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(discoverySessionId);
                    _data.writeInt(peerId);
                    _data.writeByteArray(message);
                    _data.writeInt(messageId);
                    _data.writeInt(retryCount);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareManager
            public synchronized void terminateSession(int clientId, int discoverySessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(discoverySessionId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.aware.IWifiAwareManager
            public synchronized void requestMacAddresses(int uid, List peerIds, IWifiAwareMacAddressProvider callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeList(peerIds);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
