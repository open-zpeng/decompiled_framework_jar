package android.hardware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ICameraServiceProxy extends IInterface {
    public static final int CAMERA_API_LEVEL_1 = 1;
    public static final int CAMERA_API_LEVEL_2 = 2;
    public static final int CAMERA_FACING_BACK = 0;
    public static final int CAMERA_FACING_EXTERNAL = 2;
    public static final int CAMERA_FACING_FRONT = 1;
    public static final int CAMERA_STATE_ACTIVE = 1;
    public static final int CAMERA_STATE_CLOSED = 3;
    public static final int CAMERA_STATE_IDLE = 2;
    public static final int CAMERA_STATE_OPEN = 0;

    void notifyCameraState(String str, int i, int i2, String str2, int i3) throws RemoteException;

    void pingForUserUpdate() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICameraServiceProxy {
        @Override // android.hardware.ICameraServiceProxy
        public void pingForUserUpdate() throws RemoteException {
        }

        @Override // android.hardware.ICameraServiceProxy
        public void notifyCameraState(String cameraId, int facing, int newCameraState, String clientName, int apiLevel) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICameraServiceProxy {
        private static final String DESCRIPTOR = "android.hardware.ICameraServiceProxy";
        static final int TRANSACTION_notifyCameraState = 2;
        static final int TRANSACTION_pingForUserUpdate = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICameraServiceProxy asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ICameraServiceProxy)) {
                return (ICameraServiceProxy) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                if (transactionCode == 2) {
                    return "notifyCameraState";
                }
                return null;
            }
            return "pingForUserUpdate";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1) {
                if (code != 2) {
                    if (code == 1598968902) {
                        reply.writeString(DESCRIPTOR);
                        return true;
                    }
                    return super.onTransact(code, data, reply, flags);
                }
                data.enforceInterface(DESCRIPTOR);
                String _arg0 = data.readString();
                int _arg1 = data.readInt();
                int _arg2 = data.readInt();
                String _arg3 = data.readString();
                int _arg4 = data.readInt();
                notifyCameraState(_arg0, _arg1, _arg2, _arg3, _arg4);
                return true;
            }
            data.enforceInterface(DESCRIPTOR);
            pingForUserUpdate();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements ICameraServiceProxy {
            public static ICameraServiceProxy sDefaultImpl;
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

            @Override // android.hardware.ICameraServiceProxy
            public void pingForUserUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().pingForUserUpdate();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.ICameraServiceProxy
            public void notifyCameraState(String cameraId, int facing, int newCameraState, String clientName, int apiLevel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(cameraId);
                    _data.writeInt(facing);
                    _data.writeInt(newCameraState);
                    _data.writeString(clientName);
                    _data.writeInt(apiLevel);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCameraState(cameraId, facing, newCameraState, clientName, apiLevel);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ICameraServiceProxy impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ICameraServiceProxy getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
