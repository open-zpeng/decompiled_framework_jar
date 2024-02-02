package android.net.lowpan;

import android.net.lowpan.ILowpanInterface;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface ILowpanManagerListener extends IInterface {
    private protected synchronized void onInterfaceAdded(ILowpanInterface iLowpanInterface) throws RemoteException;

    private protected synchronized void onInterfaceRemoved(ILowpanInterface iLowpanInterface) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ILowpanManagerListener {
        public protected static final String DESCRIPTOR = "android.net.lowpan.ILowpanManagerListener";
        public private protected static final int TRANSACTION_onInterfaceAdded = 1;
        public private protected static final int TRANSACTION_onInterfaceRemoved = 2;

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized ILowpanManagerListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ILowpanManagerListener)) {
                return (ILowpanManagerListener) iin;
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
                    ILowpanInterface _arg0 = ILowpanInterface.Stub.asInterface(data.readStrongBinder());
                    onInterfaceAdded(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    ILowpanInterface _arg02 = ILowpanInterface.Stub.asInterface(data.readStrongBinder());
                    onInterfaceRemoved(_arg02);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ILowpanManagerListener {
            public protected IBinder mRemote;

            public private protected synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            private protected synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            private protected synchronized void onInterfaceAdded(ILowpanInterface lowpanInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lowpanInterface != null ? lowpanInterface.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onInterfaceRemoved(ILowpanInterface lowpanInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lowpanInterface != null ? lowpanInterface.asBinder() : null);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
