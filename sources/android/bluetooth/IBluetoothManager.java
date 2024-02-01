package android.bluetooth;

import android.bluetooth.IBluetooth;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManagerCallback;
import android.bluetooth.IBluetoothProfileServiceConnection;
import android.bluetooth.IBluetoothStateChangeCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IBluetoothManager extends IInterface {
    boolean bindBluetoothProfileService(int i, IBluetoothProfileServiceConnection iBluetoothProfileServiceConnection) throws RemoteException;

    boolean disable(String str, boolean z) throws RemoteException;

    boolean disableDevice(int i, String str, boolean z) throws RemoteException;

    boolean enable(String str) throws RemoteException;

    boolean enableDevice(int i, String str, boolean z) throws RemoteException;

    boolean enableNoAutoConnect(String str) throws RemoteException;

    String getAddress() throws RemoteException;

    IBluetoothGatt getBluetoothGatt() throws RemoteException;

    String getConnectedDevice(int i) throws RemoteException;

    int getDownloadProgress() throws RemoteException;

    String getModuleVersion() throws RemoteException;

    String getName() throws RemoteException;

    int getState() throws RemoteException;

    int getUploadProgress() throws RemoteException;

    boolean isBleAppPresent() throws RemoteException;

    boolean isBleScanAlwaysAvailable() throws RemoteException;

    boolean isDeviceEnabled(int i) throws RemoteException;

    boolean isEnabled() throws RemoteException;

    boolean isHearingAidProfileSupported() throws RemoteException;

    boolean onFactoryReset() throws RemoteException;

    IBluetooth registerAdapter(IBluetoothManagerCallback iBluetoothManagerCallback) throws RemoteException;

    void registerStateChangeCallback(IBluetoothStateChangeCallback iBluetoothStateChangeCallback) throws RemoteException;

    void setConnectedDevice(int i, String str) throws RemoteException;

    boolean startDownload(String str) throws RemoteException;

    boolean startUpload() throws RemoteException;

    void unbindBluetoothProfileService(int i, IBluetoothProfileServiceConnection iBluetoothProfileServiceConnection) throws RemoteException;

    void unregisterAdapter(IBluetoothManagerCallback iBluetoothManagerCallback) throws RemoteException;

    void unregisterStateChangeCallback(IBluetoothStateChangeCallback iBluetoothStateChangeCallback) throws RemoteException;

    int updateBleAppCount(IBinder iBinder, boolean z, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBluetoothManager {
        @Override // android.bluetooth.IBluetoothManager
        public IBluetooth registerAdapter(IBluetoothManagerCallback callback) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothManager
        public void unregisterAdapter(IBluetoothManagerCallback callback) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothManager
        public void registerStateChangeCallback(IBluetoothStateChangeCallback callback) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothManager
        public void unregisterStateChangeCallback(IBluetoothStateChangeCallback callback) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean isEnabled() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean enable(String packageName) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean enableNoAutoConnect(String packageName) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean disable(String packageName, boolean persist) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public int getState() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetoothManager
        public IBluetoothGatt getBluetoothGatt() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean bindBluetoothProfileService(int profile, IBluetoothProfileServiceConnection proxy) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public void unbindBluetoothProfileService(int profile, IBluetoothProfileServiceConnection proxy) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothManager
        public String getAddress() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothManager
        public String getName() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean onFactoryReset() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean isBleScanAlwaysAvailable() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public int updateBleAppCount(IBinder b, boolean enable, String packageName) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean isBleAppPresent() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean isHearingAidProfileSupported() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean enableDevice(int deviceId, String packageName, boolean persist) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean disableDevice(int deviceId, String packageName, boolean persist) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean isDeviceEnabled(int deviceId) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public String getConnectedDevice(int deviceId) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothManager
        public void setConnectedDevice(int deviceId, String address) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothManager
        public String getModuleVersion() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean startDownload(String path) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public boolean startUpload() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothManager
        public int getUploadProgress() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetoothManager
        public int getDownloadProgress() throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBluetoothManager {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothManager";
        static final int TRANSACTION_bindBluetoothProfileService = 11;
        static final int TRANSACTION_disable = 8;
        static final int TRANSACTION_disableDevice = 21;
        static final int TRANSACTION_enable = 6;
        static final int TRANSACTION_enableDevice = 20;
        static final int TRANSACTION_enableNoAutoConnect = 7;
        static final int TRANSACTION_getAddress = 13;
        static final int TRANSACTION_getBluetoothGatt = 10;
        static final int TRANSACTION_getConnectedDevice = 23;
        static final int TRANSACTION_getDownloadProgress = 29;
        static final int TRANSACTION_getModuleVersion = 25;
        static final int TRANSACTION_getName = 14;
        static final int TRANSACTION_getState = 9;
        static final int TRANSACTION_getUploadProgress = 28;
        static final int TRANSACTION_isBleAppPresent = 18;
        static final int TRANSACTION_isBleScanAlwaysAvailable = 16;
        static final int TRANSACTION_isDeviceEnabled = 22;
        static final int TRANSACTION_isEnabled = 5;
        static final int TRANSACTION_isHearingAidProfileSupported = 19;
        static final int TRANSACTION_onFactoryReset = 15;
        static final int TRANSACTION_registerAdapter = 1;
        static final int TRANSACTION_registerStateChangeCallback = 3;
        static final int TRANSACTION_setConnectedDevice = 24;
        static final int TRANSACTION_startDownload = 26;
        static final int TRANSACTION_startUpload = 27;
        static final int TRANSACTION_unbindBluetoothProfileService = 12;
        static final int TRANSACTION_unregisterAdapter = 2;
        static final int TRANSACTION_unregisterStateChangeCallback = 4;
        static final int TRANSACTION_updateBleAppCount = 17;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBluetoothManager)) {
                return (IBluetoothManager) iin;
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
                    return "registerAdapter";
                case 2:
                    return "unregisterAdapter";
                case 3:
                    return "registerStateChangeCallback";
                case 4:
                    return "unregisterStateChangeCallback";
                case 5:
                    return "isEnabled";
                case 6:
                    return "enable";
                case 7:
                    return "enableNoAutoConnect";
                case 8:
                    return "disable";
                case 9:
                    return "getState";
                case 10:
                    return "getBluetoothGatt";
                case 11:
                    return "bindBluetoothProfileService";
                case 12:
                    return "unbindBluetoothProfileService";
                case 13:
                    return "getAddress";
                case 14:
                    return "getName";
                case 15:
                    return "onFactoryReset";
                case 16:
                    return "isBleScanAlwaysAvailable";
                case 17:
                    return "updateBleAppCount";
                case 18:
                    return "isBleAppPresent";
                case 19:
                    return "isHearingAidProfileSupported";
                case 20:
                    return "enableDevice";
                case 21:
                    return "disableDevice";
                case 22:
                    return "isDeviceEnabled";
                case 23:
                    return "getConnectedDevice";
                case 24:
                    return "setConnectedDevice";
                case 25:
                    return "getModuleVersion";
                case 26:
                    return "startDownload";
                case 27:
                    return "startUpload";
                case 28:
                    return "getUploadProgress";
                case 29:
                    return "getDownloadProgress";
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
            boolean _arg2;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IBluetoothManagerCallback _arg0 = IBluetoothManagerCallback.Stub.asInterface(data.readStrongBinder());
                    IBluetooth _result = registerAdapter(_arg0);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IBluetoothManagerCallback _arg02 = IBluetoothManagerCallback.Stub.asInterface(data.readStrongBinder());
                    unregisterAdapter(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IBluetoothStateChangeCallback _arg03 = IBluetoothStateChangeCallback.Stub.asInterface(data.readStrongBinder());
                    registerStateChangeCallback(_arg03);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IBluetoothStateChangeCallback _arg04 = IBluetoothStateChangeCallback.Stub.asInterface(data.readStrongBinder());
                    unregisterStateChangeCallback(_arg04);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isEnabled = isEnabled();
                    reply.writeNoException();
                    reply.writeInt(isEnabled ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    boolean enable = enable(_arg05);
                    reply.writeNoException();
                    reply.writeInt(enable ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    boolean enableNoAutoConnect = enableNoAutoConnect(_arg06);
                    reply.writeNoException();
                    reply.writeInt(enableNoAutoConnect ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    _arg2 = data.readInt() != 0;
                    boolean disable = disable(_arg07, _arg2);
                    reply.writeNoException();
                    reply.writeInt(disable ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = getState();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IBluetoothGatt _result3 = getBluetoothGatt();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result3 != null ? _result3.asBinder() : null);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    IBluetoothProfileServiceConnection _arg1 = IBluetoothProfileServiceConnection.Stub.asInterface(data.readStrongBinder());
                    boolean bindBluetoothProfileService = bindBluetoothProfileService(_arg08, _arg1);
                    reply.writeNoException();
                    reply.writeInt(bindBluetoothProfileService ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    IBluetoothProfileServiceConnection _arg12 = IBluetoothProfileServiceConnection.Stub.asInterface(data.readStrongBinder());
                    unbindBluetoothProfileService(_arg09, _arg12);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _result4 = getAddress();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _result5 = getName();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    boolean onFactoryReset = onFactoryReset();
                    reply.writeNoException();
                    reply.writeInt(onFactoryReset ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBleScanAlwaysAvailable = isBleScanAlwaysAvailable();
                    reply.writeNoException();
                    reply.writeInt(isBleScanAlwaysAvailable ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg010 = data.readStrongBinder();
                    _arg2 = data.readInt() != 0;
                    int _result6 = updateBleAppCount(_arg010, _arg2, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBleAppPresent = isBleAppPresent();
                    reply.writeNoException();
                    reply.writeInt(isBleAppPresent ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHearingAidProfileSupported = isHearingAidProfileSupported();
                    reply.writeNoException();
                    reply.writeInt(isHearingAidProfileSupported ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    String _arg13 = data.readString();
                    _arg2 = data.readInt() != 0;
                    boolean enableDevice = enableDevice(_arg011, _arg13, _arg2);
                    reply.writeNoException();
                    reply.writeInt(enableDevice ? 1 : 0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    String _arg14 = data.readString();
                    _arg2 = data.readInt() != 0;
                    boolean disableDevice = disableDevice(_arg012, _arg14, _arg2);
                    reply.writeNoException();
                    reply.writeInt(disableDevice ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    boolean isDeviceEnabled = isDeviceEnabled(_arg013);
                    reply.writeNoException();
                    reply.writeInt(isDeviceEnabled ? 1 : 0);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    String _result7 = getConnectedDevice(_arg014);
                    reply.writeNoException();
                    reply.writeString(_result7);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg015 = data.readInt();
                    String _arg15 = data.readString();
                    setConnectedDevice(_arg015, _arg15);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _result8 = getModuleVersion();
                    reply.writeNoException();
                    reply.writeString(_result8);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    boolean startDownload = startDownload(_arg016);
                    reply.writeNoException();
                    reply.writeInt(startDownload ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    boolean startUpload = startUpload();
                    reply.writeNoException();
                    reply.writeInt(startUpload ? 1 : 0);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _result9 = getUploadProgress();
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    int _result10 = getDownloadProgress();
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBluetoothManager {
            public static IBluetoothManager sDefaultImpl;
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

            @Override // android.bluetooth.IBluetoothManager
            public IBluetooth registerAdapter(IBluetoothManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerAdapter(callback);
                    }
                    _reply.readException();
                    IBluetooth _result = IBluetooth.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public void unregisterAdapter(IBluetoothManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterAdapter(callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public void registerStateChangeCallback(IBluetoothStateChangeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerStateChangeCallback(callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public void unregisterStateChangeCallback(IBluetoothStateChangeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterStateChangeCallback(callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean isEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isEnabled();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean enable(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enable(packageName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean enableNoAutoConnect(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enableNoAutoConnect(packageName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean disable(String packageName, boolean persist) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(persist ? 1 : 0);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().disable(packageName, persist);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public int getState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getState();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public IBluetoothGatt getBluetoothGatt() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBluetoothGatt();
                    }
                    _reply.readException();
                    IBluetoothGatt _result = IBluetoothGatt.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean bindBluetoothProfileService(int profile, IBluetoothProfileServiceConnection proxy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(profile);
                    _data.writeStrongBinder(proxy != null ? proxy.asBinder() : null);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().bindBluetoothProfileService(profile, proxy);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public void unbindBluetoothProfileService(int profile, IBluetoothProfileServiceConnection proxy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(profile);
                    _data.writeStrongBinder(proxy != null ? proxy.asBinder() : null);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unbindBluetoothProfileService(profile, proxy);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public String getAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAddress();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public String getName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean onFactoryReset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().onFactoryReset();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean isBleScanAlwaysAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBleScanAlwaysAvailable();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public int updateBleAppCount(IBinder b, boolean enable, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(b);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateBleAppCount(b, enable, packageName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean isBleAppPresent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBleAppPresent();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean isHearingAidProfileSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isHearingAidProfileSupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean enableDevice(int deviceId, String packageName, boolean persist) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeString(packageName);
                    _data.writeInt(persist ? 1 : 0);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enableDevice(deviceId, packageName, persist);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean disableDevice(int deviceId, String packageName, boolean persist) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeString(packageName);
                    _data.writeInt(persist ? 1 : 0);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().disableDevice(deviceId, packageName, persist);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean isDeviceEnabled(int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDeviceEnabled(deviceId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public String getConnectedDevice(int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getConnectedDevice(deviceId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public void setConnectedDevice(int deviceId, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeString(address);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setConnectedDevice(deviceId, address);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public String getModuleVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getModuleVersion();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean startDownload(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startDownload(path);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public boolean startUpload() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startUpload();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public int getUploadProgress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUploadProgress();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothManager
            public int getDownloadProgress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDownloadProgress();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBluetoothManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBluetoothManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
