package android.hardware.input;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IInputDevicesChangedListener extends IInterface {
    synchronized void onInputDevicesChanged(int[] iArr) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IInputDevicesChangedListener {
        private static final String DESCRIPTOR = "android.hardware.input.IInputDevicesChangedListener";
        static final int TRANSACTION_onInputDevicesChanged = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IInputDevicesChangedListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IInputDevicesChangedListener)) {
                return (IInputDevicesChangedListener) iin;
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
            int[] _arg0 = data.createIntArray();
            onInputDevicesChanged(_arg0);
            return true;
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IInputDevicesChangedListener {
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

            @Override // android.hardware.input.IInputDevicesChangedListener
            public synchronized void onInputDevicesChanged(int[] deviceIdAndGeneration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(deviceIdAndGeneration);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
