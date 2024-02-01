package android.net.wifi.p2p;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface IWifiP2pManager extends IInterface {
    void checkConfigureWifiDisplayPermission() throws RemoteException;

    void close(IBinder iBinder) throws RemoteException;

    Messenger getMessenger(IBinder iBinder) throws RemoteException;

    Messenger getP2pStateMachineMessenger() throws RemoteException;

    WifiP2pConfig getP2pTetherConfiguration() throws RemoteException;

    void setMiracastMode(int i) throws RemoteException;

    boolean setP2pTetherConfiguration(WifiP2pConfig wifiP2pConfig) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IWifiP2pManager {
        @Override // android.net.wifi.p2p.IWifiP2pManager
        public Messenger getMessenger(IBinder binder) throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.p2p.IWifiP2pManager
        public Messenger getP2pStateMachineMessenger() throws RemoteException {
            return null;
        }

        @Override // android.net.wifi.p2p.IWifiP2pManager
        public void close(IBinder binder) throws RemoteException {
        }

        @Override // android.net.wifi.p2p.IWifiP2pManager
        public void setMiracastMode(int mode) throws RemoteException {
        }

        @Override // android.net.wifi.p2p.IWifiP2pManager
        public void checkConfigureWifiDisplayPermission() throws RemoteException {
        }

        @Override // android.net.wifi.p2p.IWifiP2pManager
        public boolean setP2pTetherConfiguration(WifiP2pConfig config) throws RemoteException {
            return false;
        }

        @Override // android.net.wifi.p2p.IWifiP2pManager
        public WifiP2pConfig getP2pTetherConfiguration() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IWifiP2pManager {
        private static final String DESCRIPTOR = "android.net.wifi.p2p.IWifiP2pManager";
        static final int TRANSACTION_checkConfigureWifiDisplayPermission = 5;
        static final int TRANSACTION_close = 3;
        static final int TRANSACTION_getMessenger = 1;
        static final int TRANSACTION_getP2pStateMachineMessenger = 2;
        static final int TRANSACTION_getP2pTetherConfiguration = 7;
        static final int TRANSACTION_setMiracastMode = 4;
        static final int TRANSACTION_setP2pTetherConfiguration = 6;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiP2pManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IWifiP2pManager)) {
                return (IWifiP2pManager) iin;
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
                    return "getMessenger";
                case 2:
                    return "getP2pStateMachineMessenger";
                case 3:
                    return "close";
                case 4:
                    return "setMiracastMode";
                case 5:
                    return "checkConfigureWifiDisplayPermission";
                case 6:
                    return "setP2pTetherConfiguration";
                case 7:
                    return "getP2pTetherConfiguration";
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
            WifiP2pConfig _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg02 = data.readStrongBinder();
                    Messenger _result = getMessenger(_arg02);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    Messenger _result2 = getP2pStateMachineMessenger();
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg03 = data.readStrongBinder();
                    close(_arg03);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    setMiracastMode(_arg04);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    checkConfigureWifiDisplayPermission();
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = WifiP2pConfig.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    boolean p2pTetherConfiguration = setP2pTetherConfiguration(_arg0);
                    reply.writeNoException();
                    reply.writeInt(p2pTetherConfiguration ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    WifiP2pConfig _result3 = getP2pTetherConfiguration();
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IWifiP2pManager {
            public static IWifiP2pManager sDefaultImpl;
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

            @Override // android.net.wifi.p2p.IWifiP2pManager
            public Messenger getMessenger(IBinder binder) throws RemoteException {
                Messenger _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMessenger(binder);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Messenger.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.p2p.IWifiP2pManager
            public Messenger getP2pStateMachineMessenger() throws RemoteException {
                Messenger _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getP2pStateMachineMessenger();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Messenger.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.p2p.IWifiP2pManager
            public void close(IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().close(binder);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.p2p.IWifiP2pManager
            public void setMiracastMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMiracastMode(mode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.p2p.IWifiP2pManager
            public void checkConfigureWifiDisplayPermission() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().checkConfigureWifiDisplayPermission();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.p2p.IWifiP2pManager
            public boolean setP2pTetherConfiguration(WifiP2pConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setP2pTetherConfiguration(config);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.p2p.IWifiP2pManager
            public WifiP2pConfig getP2pTetherConfiguration() throws RemoteException {
                WifiP2pConfig _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getP2pTetherConfiguration();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = WifiP2pConfig.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IWifiP2pManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IWifiP2pManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
