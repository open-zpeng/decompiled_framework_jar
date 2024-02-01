package android.hardware;

import android.hardware.ICamera;
import android.hardware.ICameraClient;
import android.hardware.ICameraServiceListener;
import android.hardware.camera2.ICameraDeviceCallbacks;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.VendorTagDescriptor;
import android.hardware.camera2.params.VendorTagDescriptorCache;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface ICameraService extends IInterface {
    public static final int API_VERSION_1 = 1;
    public static final int API_VERSION_2 = 2;
    public static final int CAMERA_HAL_API_VERSION_UNSPECIFIED = -1;
    public static final int CAMERA_TYPE_ALL = 1;
    public static final int CAMERA_TYPE_BACKWARD_COMPATIBLE = 0;
    public static final int ERROR_ALREADY_EXISTS = 2;
    public static final int ERROR_CAMERA_IN_USE = 7;
    public static final int ERROR_DEPRECATED_HAL = 9;
    public static final int ERROR_DISABLED = 6;
    public static final int ERROR_DISCONNECTED = 4;
    public static final int ERROR_ILLEGAL_ARGUMENT = 3;
    public static final int ERROR_INVALID_OPERATION = 10;
    public static final int ERROR_MAX_CAMERAS_IN_USE = 8;
    public static final int ERROR_PERMISSION_DENIED = 1;
    public static final int ERROR_TIMED_OUT = 5;
    public static final int EVENT_NONE = 0;
    public static final int EVENT_USER_SWITCHED = 1;
    public static final int USE_CALLING_PID = -1;
    public static final int USE_CALLING_UID = -1;

    synchronized CameraStatus[] addListener(ICameraServiceListener iCameraServiceListener) throws RemoteException;

    synchronized ICamera connect(ICameraClient iCameraClient, int i, String str, int i2, int i3) throws RemoteException;

    synchronized ICameraDeviceUser connectDevice(ICameraDeviceCallbacks iCameraDeviceCallbacks, String str, String str2, int i) throws RemoteException;

    synchronized ICamera connectLegacy(ICameraClient iCameraClient, int i, int i2, String str, int i3) throws RemoteException;

    synchronized CameraMetadataNative getCameraCharacteristics(String str) throws RemoteException;

    synchronized CameraInfo getCameraInfo(int i) throws RemoteException;

    synchronized VendorTagDescriptorCache getCameraVendorTagCache() throws RemoteException;

    synchronized VendorTagDescriptor getCameraVendorTagDescriptor() throws RemoteException;

    synchronized String getLegacyParameters(int i) throws RemoteException;

    synchronized int getNumberOfCameras(int i) throws RemoteException;

    synchronized void notifySystemEvent(int i, int[] iArr) throws RemoteException;

    synchronized void removeListener(ICameraServiceListener iCameraServiceListener) throws RemoteException;

    synchronized void setTorchMode(String str, boolean z, IBinder iBinder) throws RemoteException;

    synchronized boolean supportsCameraApi(String str, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICameraService {
        private static final String DESCRIPTOR = "android.hardware.ICameraService";
        static final int TRANSACTION_addListener = 6;
        static final int TRANSACTION_connect = 3;
        static final int TRANSACTION_connectDevice = 4;
        static final int TRANSACTION_connectLegacy = 5;
        static final int TRANSACTION_getCameraCharacteristics = 8;
        static final int TRANSACTION_getCameraInfo = 2;
        static final int TRANSACTION_getCameraVendorTagCache = 10;
        static final int TRANSACTION_getCameraVendorTagDescriptor = 9;
        static final int TRANSACTION_getLegacyParameters = 11;
        static final int TRANSACTION_getNumberOfCameras = 1;
        static final int TRANSACTION_notifySystemEvent = 14;
        static final int TRANSACTION_removeListener = 7;
        static final int TRANSACTION_setTorchMode = 13;
        static final int TRANSACTION_supportsCameraApi = 12;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static ICameraService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ICameraService)) {
                return (ICameraService) iin;
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
                    int _result = getNumberOfCameras(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    CameraInfo _result2 = getCameraInfo(_arg02);
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
                    ICameraClient _arg03 = ICameraClient.Stub.asInterface(data.readStrongBinder());
                    int _arg1 = data.readInt();
                    String _arg2 = data.readString();
                    int _arg3 = data.readInt();
                    int _arg4 = data.readInt();
                    ICamera _result3 = connect(_arg03, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result3 != null ? _result3.asBinder() : null);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ICameraDeviceCallbacks _arg04 = ICameraDeviceCallbacks.Stub.asInterface(data.readStrongBinder());
                    String _arg12 = data.readString();
                    String _arg22 = data.readString();
                    int _arg32 = data.readInt();
                    ICameraDeviceUser _result4 = connectDevice(_arg04, _arg12, _arg22, _arg32);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result4 != null ? _result4.asBinder() : null);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    ICameraClient _arg05 = ICameraClient.Stub.asInterface(data.readStrongBinder());
                    int _arg13 = data.readInt();
                    int _arg23 = data.readInt();
                    String _arg33 = data.readString();
                    int _arg42 = data.readInt();
                    ICamera _result5 = connectLegacy(_arg05, _arg13, _arg23, _arg33, _arg42);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result5 != null ? _result5.asBinder() : null);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    ICameraServiceListener _arg06 = ICameraServiceListener.Stub.asInterface(data.readStrongBinder());
                    CameraStatus[] _result6 = addListener(_arg06);
                    reply.writeNoException();
                    reply.writeTypedArray(_result6, 1);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    ICameraServiceListener _arg07 = ICameraServiceListener.Stub.asInterface(data.readStrongBinder());
                    removeListener(_arg07);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    CameraMetadataNative _result7 = getCameraCharacteristics(_arg08);
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    VendorTagDescriptor _result8 = getCameraVendorTagDescriptor();
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    VendorTagDescriptorCache _result9 = getCameraVendorTagCache();
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    String _result10 = getLegacyParameters(_arg09);
                    reply.writeNoException();
                    reply.writeString(_result10);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    int _arg14 = data.readInt();
                    boolean supportsCameraApi = supportsCameraApi(_arg010, _arg14);
                    reply.writeNoException();
                    reply.writeInt(supportsCameraApi ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    boolean _arg15 = data.readInt() != 0;
                    IBinder _arg24 = data.readStrongBinder();
                    setTorchMode(_arg011, _arg15, _arg24);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    int[] _arg16 = data.createIntArray();
                    notifySystemEvent(_arg012, _arg16);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICameraService {
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

            @Override // android.hardware.ICameraService
            public synchronized int getNumberOfCameras(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized CameraInfo getCameraInfo(int cameraId) throws RemoteException {
                CameraInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cameraId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = CameraInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized ICamera connect(ICameraClient client, int cameraId, String opPackageName, int clientUid, int clientPid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(cameraId);
                    _data.writeString(opPackageName);
                    _data.writeInt(clientUid);
                    _data.writeInt(clientPid);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    ICamera _result = ICamera.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized ICameraDeviceUser connectDevice(ICameraDeviceCallbacks callbacks, String cameraId, String opPackageName, int clientUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callbacks != null ? callbacks.asBinder() : null);
                    _data.writeString(cameraId);
                    _data.writeString(opPackageName);
                    _data.writeInt(clientUid);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    ICameraDeviceUser _result = ICameraDeviceUser.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized ICamera connectLegacy(ICameraClient client, int cameraId, int halVersion, String opPackageName, int clientUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(cameraId);
                    _data.writeInt(halVersion);
                    _data.writeString(opPackageName);
                    _data.writeInt(clientUid);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    ICamera _result = ICamera.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized CameraStatus[] addListener(ICameraServiceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    CameraStatus[] _result = (CameraStatus[]) _reply.createTypedArray(CameraStatus.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized void removeListener(ICameraServiceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized CameraMetadataNative getCameraCharacteristics(String cameraId) throws RemoteException {
                CameraMetadataNative _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(cameraId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = CameraMetadataNative.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized VendorTagDescriptor getCameraVendorTagDescriptor() throws RemoteException {
                VendorTagDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VendorTagDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized VendorTagDescriptorCache getCameraVendorTagCache() throws RemoteException {
                VendorTagDescriptorCache _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VendorTagDescriptorCache.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized String getLegacyParameters(int cameraId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cameraId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized boolean supportsCameraApi(String cameraId, int apiVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(cameraId);
                    _data.writeInt(apiVersion);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized void setTorchMode(String cameraId, boolean enabled, IBinder clientBinder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(cameraId);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeStrongBinder(clientBinder);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraService
            public synchronized void notifySystemEvent(int eventId, int[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(eventId);
                    _data.writeIntArray(args);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
