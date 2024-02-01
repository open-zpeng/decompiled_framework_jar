package android.net.wifi.rtt;

import android.net.wifi.rtt.IRttCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.WorkSource;
/* loaded from: classes2.dex */
public interface IWifiRttManager extends IInterface {
    synchronized void cancelRanging(WorkSource workSource) throws RemoteException;

    synchronized boolean isAvailable() throws RemoteException;

    synchronized void startRanging(IBinder iBinder, String str, WorkSource workSource, RangingRequest rangingRequest, IRttCallback iRttCallback) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IWifiRttManager {
        private static final String DESCRIPTOR = "android.net.wifi.rtt.IWifiRttManager";
        static final int TRANSACTION_cancelRanging = 3;
        static final int TRANSACTION_isAvailable = 1;
        static final int TRANSACTION_startRanging = 2;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IWifiRttManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IWifiRttManager)) {
                return (IWifiRttManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            WorkSource _arg2;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAvailable = isAvailable();
                    reply.writeNoException();
                    reply.writeInt(isAvailable ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0 = data.readStrongBinder();
                    String _arg1 = data.readString();
                    if (data.readInt() != 0) {
                        WorkSource _arg22 = WorkSource.CREATOR.createFromParcel(data);
                        _arg2 = _arg22;
                    } else {
                        _arg2 = null;
                    }
                    RangingRequest _arg3 = data.readInt() != 0 ? RangingRequest.CREATOR.createFromParcel(data) : null;
                    IRttCallback _arg4 = IRttCallback.Stub.asInterface(data.readStrongBinder());
                    startRanging(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    WorkSource _arg02 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                    cancelRanging(_arg02);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IWifiRttManager {
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

            @Override // android.net.wifi.rtt.IWifiRttManager
            public synchronized boolean isAvailable() throws RemoteException {
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

            @Override // android.net.wifi.rtt.IWifiRttManager
            public synchronized void startRanging(IBinder binder, String callingPackage, WorkSource workSource, RangingRequest request, IRttCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(callingPackage);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.rtt.IWifiRttManager
            public synchronized void cancelRanging(WorkSource workSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
