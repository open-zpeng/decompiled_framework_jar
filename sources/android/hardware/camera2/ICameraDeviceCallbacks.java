package android.hardware.camera2;

import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CaptureResultExtras;
import android.hardware.camera2.impl.PhysicalCaptureResultInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface ICameraDeviceCallbacks extends IInterface {
    public static final int ERROR_CAMERA_BUFFER = 5;
    public static final int ERROR_CAMERA_DEVICE = 1;
    public static final int ERROR_CAMERA_DISABLED = 6;
    public static final int ERROR_CAMERA_DISCONNECTED = 0;
    public static final int ERROR_CAMERA_INVALID_ERROR = -1;
    public static final int ERROR_CAMERA_REQUEST = 3;
    public static final int ERROR_CAMERA_RESULT = 4;
    public static final int ERROR_CAMERA_SERVICE = 2;

    synchronized void onCaptureStarted(CaptureResultExtras captureResultExtras, long j) throws RemoteException;

    synchronized void onDeviceError(int i, CaptureResultExtras captureResultExtras) throws RemoteException;

    synchronized void onDeviceIdle() throws RemoteException;

    synchronized void onPrepared(int i) throws RemoteException;

    synchronized void onRepeatingRequestError(long j, int i) throws RemoteException;

    synchronized void onRequestQueueEmpty() throws RemoteException;

    synchronized void onResultReceived(CameraMetadataNative cameraMetadataNative, CaptureResultExtras captureResultExtras, PhysicalCaptureResultInfo[] physicalCaptureResultInfoArr) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICameraDeviceCallbacks {
        private static final String DESCRIPTOR = "android.hardware.camera2.ICameraDeviceCallbacks";
        static final int TRANSACTION_onCaptureStarted = 3;
        static final int TRANSACTION_onDeviceError = 1;
        static final int TRANSACTION_onDeviceIdle = 2;
        static final int TRANSACTION_onPrepared = 5;
        static final int TRANSACTION_onRepeatingRequestError = 6;
        static final int TRANSACTION_onRequestQueueEmpty = 7;
        static final int TRANSACTION_onResultReceived = 4;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ICameraDeviceCallbacks asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ICameraDeviceCallbacks)) {
                return (ICameraDeviceCallbacks) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            CaptureResultExtras _arg1;
            CameraMetadataNative _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    _arg1 = data.readInt() != 0 ? CaptureResultExtras.CREATOR.createFromParcel(data) : null;
                    onDeviceError(_arg02, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onDeviceIdle();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0 ? CaptureResultExtras.CREATOR.createFromParcel(data) : null;
                    onCaptureStarted(_arg1, data.readLong());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = CameraMetadataNative.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _arg1 = data.readInt() != 0 ? CaptureResultExtras.CREATOR.createFromParcel(data) : null;
                    PhysicalCaptureResultInfo[] _arg2 = (PhysicalCaptureResultInfo[]) data.createTypedArray(PhysicalCaptureResultInfo.CREATOR);
                    onResultReceived(_arg0, _arg1, _arg2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    onPrepared(_arg03);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg04 = data.readLong();
                    onRepeatingRequestError(_arg04, data.readInt());
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    onRequestQueueEmpty();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICameraDeviceCallbacks {
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

            @Override // android.hardware.camera2.ICameraDeviceCallbacks
            public synchronized void onDeviceError(int errorCode, CaptureResultExtras resultExtras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(errorCode);
                    if (resultExtras != null) {
                        _data.writeInt(1);
                        resultExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.camera2.ICameraDeviceCallbacks
            public synchronized void onDeviceIdle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.camera2.ICameraDeviceCallbacks
            public synchronized void onCaptureStarted(CaptureResultExtras resultExtras, long timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (resultExtras != null) {
                        _data.writeInt(1);
                        resultExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timestamp);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.camera2.ICameraDeviceCallbacks
            public synchronized void onResultReceived(CameraMetadataNative result, CaptureResultExtras resultExtras, PhysicalCaptureResultInfo[] physicalCaptureResultInfos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (resultExtras != null) {
                        _data.writeInt(1);
                        resultExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedArray(physicalCaptureResultInfos, 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.camera2.ICameraDeviceCallbacks
            public synchronized void onPrepared(int streamId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamId);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.camera2.ICameraDeviceCallbacks
            public synchronized void onRepeatingRequestError(long lastFrameNumber, int repeatingRequestId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(lastFrameNumber);
                    _data.writeInt(repeatingRequestId);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.camera2.ICameraDeviceCallbacks
            public synchronized void onRequestQueueEmpty() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
