package android.car.hardware.eps;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IEpsEventListener extends IInterface {
    void onEpsSteeringAngleEvent(float f) throws RemoteException;

    void onEpsSteeringAngleSpeedEvent(float f) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IEpsEventListener {
        private static final String DESCRIPTOR = "android.car.hardware.eps.IEpsEventListener";
        static final int TRANSACTION_onEpsSteeringAngleEvent = 1;
        static final int TRANSACTION_onEpsSteeringAngleSpeedEvent = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEpsEventListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IEpsEventListener)) {
                return (IEpsEventListener) iin;
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
                    float _arg0 = data.readFloat();
                    onEpsSteeringAngleEvent(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    float _arg02 = data.readFloat();
                    onEpsSteeringAngleSpeedEvent(_arg02);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IEpsEventListener {
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

            @Override // android.car.hardware.eps.IEpsEventListener
            public void onEpsSteeringAngleEvent(float angle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(angle);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.car.hardware.eps.IEpsEventListener
            public void onEpsSteeringAngleSpeedEvent(float angleSpeed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(angleSpeed);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
