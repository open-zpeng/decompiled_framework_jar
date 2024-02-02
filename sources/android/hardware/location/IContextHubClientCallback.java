package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IContextHubClientCallback extends IInterface {
    synchronized void onHubReset() throws RemoteException;

    synchronized void onMessageFromNanoApp(NanoAppMessage nanoAppMessage) throws RemoteException;

    synchronized void onNanoAppAborted(long j, int i) throws RemoteException;

    synchronized void onNanoAppDisabled(long j) throws RemoteException;

    synchronized void onNanoAppEnabled(long j) throws RemoteException;

    synchronized void onNanoAppLoaded(long j) throws RemoteException;

    synchronized void onNanoAppUnloaded(long j) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IContextHubClientCallback {
        private static final String DESCRIPTOR = "android.hardware.location.IContextHubClientCallback";
        static final int TRANSACTION_onHubReset = 2;
        static final int TRANSACTION_onMessageFromNanoApp = 1;
        static final int TRANSACTION_onNanoAppAborted = 3;
        static final int TRANSACTION_onNanoAppDisabled = 7;
        static final int TRANSACTION_onNanoAppEnabled = 6;
        static final int TRANSACTION_onNanoAppLoaded = 4;
        static final int TRANSACTION_onNanoAppUnloaded = 5;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IContextHubClientCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IContextHubClientCallback)) {
                return (IContextHubClientCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            NanoAppMessage _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = NanoAppMessage.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onMessageFromNanoApp(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onHubReset();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg02 = data.readLong();
                    int _arg1 = data.readInt();
                    onNanoAppAborted(_arg02, _arg1);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg03 = data.readLong();
                    onNanoAppLoaded(_arg03);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg04 = data.readLong();
                    onNanoAppUnloaded(_arg04);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg05 = data.readLong();
                    onNanoAppEnabled(_arg05);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg06 = data.readLong();
                    onNanoAppDisabled(_arg06);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IContextHubClientCallback {
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

            @Override // android.hardware.location.IContextHubClientCallback
            public synchronized void onMessageFromNanoApp(NanoAppMessage message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.location.IContextHubClientCallback
            public synchronized void onHubReset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.location.IContextHubClientCallback
            public synchronized void onNanoAppAborted(long nanoAppId, int abortCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(nanoAppId);
                    _data.writeInt(abortCode);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.location.IContextHubClientCallback
            public synchronized void onNanoAppLoaded(long nanoAppId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(nanoAppId);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.location.IContextHubClientCallback
            public synchronized void onNanoAppUnloaded(long nanoAppId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(nanoAppId);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.location.IContextHubClientCallback
            public synchronized void onNanoAppEnabled(long nanoAppId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(nanoAppId);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.location.IContextHubClientCallback
            public synchronized void onNanoAppDisabled(long nanoAppId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(nanoAppId);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
