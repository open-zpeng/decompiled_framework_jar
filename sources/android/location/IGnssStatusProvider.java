package android.location;

import android.location.IGnssStatusListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IGnssStatusProvider extends IInterface {
    synchronized void registerGnssStatusCallback(IGnssStatusListener iGnssStatusListener) throws RemoteException;

    synchronized void unregisterGnssStatusCallback(IGnssStatusListener iGnssStatusListener) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IGnssStatusProvider {
        private static final String DESCRIPTOR = "android.location.IGnssStatusProvider";
        static final int TRANSACTION_registerGnssStatusCallback = 1;
        static final int TRANSACTION_unregisterGnssStatusCallback = 2;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IGnssStatusProvider asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IGnssStatusProvider)) {
                return (IGnssStatusProvider) iin;
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
                    IGnssStatusListener _arg0 = IGnssStatusListener.Stub.asInterface(data.readStrongBinder());
                    registerGnssStatusCallback(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IGnssStatusListener _arg02 = IGnssStatusListener.Stub.asInterface(data.readStrongBinder());
                    unregisterGnssStatusCallback(_arg02);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IGnssStatusProvider {
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

            @Override // android.location.IGnssStatusProvider
            public synchronized void registerGnssStatusCallback(IGnssStatusListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.IGnssStatusProvider
            public synchronized void unregisterGnssStatusCallback(IGnssStatusListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
