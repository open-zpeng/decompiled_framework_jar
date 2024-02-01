package android.hardware.fingerprint;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IFingerprintServiceLockoutResetCallback extends IInterface {
    synchronized void onLockoutReset(long j, IRemoteCallback iRemoteCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IFingerprintServiceLockoutResetCallback {
        private static final String DESCRIPTOR = "android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback";
        static final int TRANSACTION_onLockoutReset = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IFingerprintServiceLockoutResetCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IFingerprintServiceLockoutResetCallback)) {
                return (IFingerprintServiceLockoutResetCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            long _arg0 = data.readLong();
            IRemoteCallback _arg1 = IRemoteCallback.Stub.asInterface(data.readStrongBinder());
            onLockoutReset(_arg0, _arg1);
            return true;
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IFingerprintServiceLockoutResetCallback {
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

            @Override // android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback
            public synchronized void onLockoutReset(long deviceId, IRemoteCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
