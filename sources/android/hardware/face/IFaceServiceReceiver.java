package android.hardware.face;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IFaceServiceReceiver extends IInterface {
    void onAcquired(long j, int i, int i2) throws RemoteException;

    void onAuthenticationFailed(long j) throws RemoteException;

    void onAuthenticationSucceeded(long j, Face face, int i) throws RemoteException;

    void onEnrollResult(long j, int i, int i2) throws RemoteException;

    void onEnumerated(long j, int i, int i2) throws RemoteException;

    void onError(long j, int i, int i2) throws RemoteException;

    void onFeatureGet(boolean z, int i, boolean z2) throws RemoteException;

    void onFeatureSet(boolean z, int i) throws RemoteException;

    void onRemoved(long j, int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IFaceServiceReceiver {
        @Override // android.hardware.face.IFaceServiceReceiver
        public void onEnrollResult(long deviceId, int faceId, int remaining) throws RemoteException {
        }

        @Override // android.hardware.face.IFaceServiceReceiver
        public void onAcquired(long deviceId, int acquiredInfo, int vendorCode) throws RemoteException {
        }

        @Override // android.hardware.face.IFaceServiceReceiver
        public void onAuthenticationSucceeded(long deviceId, Face face, int userId) throws RemoteException {
        }

        @Override // android.hardware.face.IFaceServiceReceiver
        public void onAuthenticationFailed(long deviceId) throws RemoteException {
        }

        @Override // android.hardware.face.IFaceServiceReceiver
        public void onError(long deviceId, int error, int vendorCode) throws RemoteException {
        }

        @Override // android.hardware.face.IFaceServiceReceiver
        public void onRemoved(long deviceId, int faceId, int remaining) throws RemoteException {
        }

        @Override // android.hardware.face.IFaceServiceReceiver
        public void onEnumerated(long deviceId, int faceId, int remaining) throws RemoteException {
        }

        @Override // android.hardware.face.IFaceServiceReceiver
        public void onFeatureSet(boolean success, int feature) throws RemoteException {
        }

        @Override // android.hardware.face.IFaceServiceReceiver
        public void onFeatureGet(boolean success, int feature, boolean value) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IFaceServiceReceiver {
        private static final String DESCRIPTOR = "android.hardware.face.IFaceServiceReceiver";
        static final int TRANSACTION_onAcquired = 2;
        static final int TRANSACTION_onAuthenticationFailed = 4;
        static final int TRANSACTION_onAuthenticationSucceeded = 3;
        static final int TRANSACTION_onEnrollResult = 1;
        static final int TRANSACTION_onEnumerated = 7;
        static final int TRANSACTION_onError = 5;
        static final int TRANSACTION_onFeatureGet = 9;
        static final int TRANSACTION_onFeatureSet = 8;
        static final int TRANSACTION_onRemoved = 6;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFaceServiceReceiver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IFaceServiceReceiver)) {
                return (IFaceServiceReceiver) iin;
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
                    return "onEnrollResult";
                case 2:
                    return "onAcquired";
                case 3:
                    return "onAuthenticationSucceeded";
                case 4:
                    return "onAuthenticationFailed";
                case 5:
                    return "onError";
                case 6:
                    return "onRemoved";
                case 7:
                    return "onEnumerated";
                case 8:
                    return "onFeatureSet";
                case 9:
                    return "onFeatureGet";
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
            Face _arg1;
            boolean _arg2;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg0 = data.readLong();
                    int _arg12 = data.readInt();
                    onEnrollResult(_arg0, _arg12, data.readInt());
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg02 = data.readLong();
                    int _arg13 = data.readInt();
                    onAcquired(_arg02, _arg13, data.readInt());
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg03 = data.readLong();
                    if (data.readInt() != 0) {
                        _arg1 = Face.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    onAuthenticationSucceeded(_arg03, _arg1, data.readInt());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg04 = data.readLong();
                    onAuthenticationFailed(_arg04);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg05 = data.readLong();
                    int _arg14 = data.readInt();
                    onError(_arg05, _arg14, data.readInt());
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg06 = data.readLong();
                    int _arg15 = data.readInt();
                    onRemoved(_arg06, _arg15, data.readInt());
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg07 = data.readLong();
                    int _arg16 = data.readInt();
                    onEnumerated(_arg07, _arg16, data.readInt());
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    int _arg17 = data.readInt();
                    onFeatureSet(_arg2, _arg17);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg08 = data.readInt() != 0;
                    int _arg18 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    onFeatureGet(_arg08, _arg18, _arg2);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IFaceServiceReceiver {
            public static IFaceServiceReceiver sDefaultImpl;
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

            @Override // android.hardware.face.IFaceServiceReceiver
            public void onEnrollResult(long deviceId, int faceId, int remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(faceId);
                    _data.writeInt(remaining);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onEnrollResult(deviceId, faceId, remaining);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IFaceServiceReceiver
            public void onAcquired(long deviceId, int acquiredInfo, int vendorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(acquiredInfo);
                    _data.writeInt(vendorCode);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAcquired(deviceId, acquiredInfo, vendorCode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IFaceServiceReceiver
            public void onAuthenticationSucceeded(long deviceId, Face face, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    if (face != null) {
                        _data.writeInt(1);
                        face.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAuthenticationSucceeded(deviceId, face, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IFaceServiceReceiver
            public void onAuthenticationFailed(long deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAuthenticationFailed(deviceId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IFaceServiceReceiver
            public void onError(long deviceId, int error, int vendorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(error);
                    _data.writeInt(vendorCode);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onError(deviceId, error, vendorCode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IFaceServiceReceiver
            public void onRemoved(long deviceId, int faceId, int remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(faceId);
                    _data.writeInt(remaining);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onRemoved(deviceId, faceId, remaining);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IFaceServiceReceiver
            public void onEnumerated(long deviceId, int faceId, int remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(faceId);
                    _data.writeInt(remaining);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onEnumerated(deviceId, faceId, remaining);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IFaceServiceReceiver
            public void onFeatureSet(boolean success, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(success ? 1 : 0);
                    _data.writeInt(feature);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onFeatureSet(success, feature);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.face.IFaceServiceReceiver
            public void onFeatureGet(boolean success, int feature, boolean value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(success ? 1 : 0);
                    _data.writeInt(feature);
                    _data.writeInt(value ? 1 : 0);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onFeatureGet(success, feature, value);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IFaceServiceReceiver impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IFaceServiceReceiver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
