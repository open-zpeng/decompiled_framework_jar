package android.companion;

import android.companion.ICompanionDeviceDiscoveryServiceCallback;
import android.companion.IFindDeviceCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface ICompanionDeviceDiscoveryService extends IInterface {
    synchronized void startDiscovery(AssociationRequest associationRequest, String str, IFindDeviceCallback iFindDeviceCallback, ICompanionDeviceDiscoveryServiceCallback iCompanionDeviceDiscoveryServiceCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICompanionDeviceDiscoveryService {
        private static final String DESCRIPTOR = "android.companion.ICompanionDeviceDiscoveryService";
        static final int TRANSACTION_startDiscovery = 1;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ICompanionDeviceDiscoveryService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ICompanionDeviceDiscoveryService)) {
                return (ICompanionDeviceDiscoveryService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            AssociationRequest _arg0;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = AssociationRequest.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            IFindDeviceCallback _arg2 = IFindDeviceCallback.Stub.asInterface(data.readStrongBinder());
            ICompanionDeviceDiscoveryServiceCallback _arg3 = ICompanionDeviceDiscoveryServiceCallback.Stub.asInterface(data.readStrongBinder());
            startDiscovery(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICompanionDeviceDiscoveryService {
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

            @Override // android.companion.ICompanionDeviceDiscoveryService
            public synchronized void startDiscovery(AssociationRequest request, String callingPackage, IFindDeviceCallback findCallback, ICompanionDeviceDiscoveryServiceCallback serviceCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(findCallback != null ? findCallback.asBinder() : null);
                    _data.writeStrongBinder(serviceCallback != null ? serviceCallback.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
