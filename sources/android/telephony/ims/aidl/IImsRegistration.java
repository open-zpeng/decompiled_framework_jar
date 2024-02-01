package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsRegistrationCallback;
/* loaded from: classes2.dex */
public interface IImsRegistration extends IInterface {
    private protected synchronized void addRegistrationCallback(IImsRegistrationCallback iImsRegistrationCallback) throws RemoteException;

    private protected synchronized int getRegistrationTechnology() throws RemoteException;

    private protected synchronized void removeRegistrationCallback(IImsRegistrationCallback iImsRegistrationCallback) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IImsRegistration {
        public protected static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsRegistration";
        public private protected static final int TRANSACTION_addRegistrationCallback = 2;
        public private protected static final int TRANSACTION_getRegistrationTechnology = 1;
        public private protected static final int TRANSACTION_removeRegistrationCallback = 3;

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized IImsRegistration asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IImsRegistration)) {
                return (IImsRegistration) iin;
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
                    int _result = getRegistrationTechnology();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IImsRegistrationCallback _arg0 = IImsRegistrationCallback.Stub.asInterface(data.readStrongBinder());
                    addRegistrationCallback(_arg0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IImsRegistrationCallback _arg02 = IImsRegistrationCallback.Stub.asInterface(data.readStrongBinder());
                    removeRegistrationCallback(_arg02);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IImsRegistration {
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

            private protected synchronized int getRegistrationTechnology() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void addRegistrationCallback(IImsRegistrationCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void removeRegistrationCallback(IImsRegistrationCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
