package android.media.projection;

import android.media.projection.IMediaProjection;
import android.media.projection.IMediaProjectionWatcherCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IMediaProjectionManager extends IInterface {
    synchronized void addCallback(IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback) throws RemoteException;

    synchronized IMediaProjection createProjection(int i, String str, int i2, boolean z) throws RemoteException;

    synchronized MediaProjectionInfo getActiveProjectionInfo() throws RemoteException;

    private protected boolean hasProjectionPermission(int i, String str) throws RemoteException;

    synchronized boolean isValidMediaProjection(IMediaProjection iMediaProjection) throws RemoteException;

    synchronized void removeCallback(IMediaProjectionWatcherCallback iMediaProjectionWatcherCallback) throws RemoteException;

    synchronized void stopActiveProjection() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IMediaProjectionManager {
        private static final String DESCRIPTOR = "android.media.projection.IMediaProjectionManager";
        static final int TRANSACTION_addCallback = 6;
        static final int TRANSACTION_createProjection = 2;
        static final int TRANSACTION_getActiveProjectionInfo = 4;
        static final int TRANSACTION_hasProjectionPermission = 1;
        static final int TRANSACTION_isValidMediaProjection = 3;
        static final int TRANSACTION_removeCallback = 7;
        static final int TRANSACTION_stopActiveProjection = 5;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IMediaProjectionManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMediaProjectionManager)) {
                return (IMediaProjectionManager) iin;
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
                    String _arg1 = data.readString();
                    boolean hasProjectionPermission = hasProjectionPermission(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(hasProjectionPermission ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    String _arg12 = data.readString();
                    int _arg2 = data.readInt();
                    boolean _arg3 = data.readInt() != 0;
                    IMediaProjection _result = createProjection(_arg02, _arg12, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IMediaProjection _arg03 = IMediaProjection.Stub.asInterface(data.readStrongBinder());
                    boolean isValidMediaProjection = isValidMediaProjection(_arg03);
                    reply.writeNoException();
                    reply.writeInt(isValidMediaProjection ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    MediaProjectionInfo _result2 = getActiveProjectionInfo();
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    stopActiveProjection();
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IMediaProjectionWatcherCallback _arg04 = IMediaProjectionWatcherCallback.Stub.asInterface(data.readStrongBinder());
                    addCallback(_arg04);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IMediaProjectionWatcherCallback _arg05 = IMediaProjectionWatcherCallback.Stub.asInterface(data.readStrongBinder());
                    removeCallback(_arg05);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IMediaProjectionManager {
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

            public synchronized boolean hasProjectionPermission(int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.projection.IMediaProjectionManager
            public synchronized IMediaProjection createProjection(int uid, String packageName, int type, boolean permanentGrant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeInt(type);
                    _data.writeInt(permanentGrant ? 1 : 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    IMediaProjection _result = IMediaProjection.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.projection.IMediaProjectionManager
            public synchronized boolean isValidMediaProjection(IMediaProjection projection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(projection != null ? projection.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.projection.IMediaProjectionManager
            public synchronized MediaProjectionInfo getActiveProjectionInfo() throws RemoteException {
                MediaProjectionInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = MediaProjectionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.projection.IMediaProjectionManager
            public synchronized void stopActiveProjection() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.projection.IMediaProjectionManager
            public synchronized void addCallback(IMediaProjectionWatcherCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.projection.IMediaProjectionManager
            public synchronized void removeCallback(IMediaProjectionWatcherCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
