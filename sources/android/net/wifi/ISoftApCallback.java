package android.net.wifi;

import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface ISoftApCallback extends IInterface {
    void onClientsUpdated(ParceledListSlice parceledListSlice) throws RemoteException;

    synchronized void onNumClientsChanged(int i) throws RemoteException;

    synchronized void onStateChanged(int i, int i2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ISoftApCallback {
        private static final String DESCRIPTOR = "android.net.wifi.ISoftApCallback";
        static final int TRANSACTION_onClientsUpdated = 3;
        static final int TRANSACTION_onNumClientsChanged = 2;
        static final int TRANSACTION_onStateChanged = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ISoftApCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISoftApCallback)) {
                return (ISoftApCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ParceledListSlice _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    int _arg1 = data.readInt();
                    onStateChanged(_arg02, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    onNumClientsChanged(_arg03);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = ParceledListSlice.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onClientsUpdated(_arg0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ISoftApCallback {
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

            @Override // android.net.wifi.ISoftApCallback
            public synchronized void onStateChanged(int state, int failureReason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeInt(failureReason);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.ISoftApCallback
            public synchronized void onNumClientsChanged(int numClients) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(numClients);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.ISoftApCallback
            public void onClientsUpdated(ParceledListSlice clients) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (clients != null) {
                        _data.writeInt(1);
                        clients.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
