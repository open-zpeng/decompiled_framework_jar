package android.os;
/* loaded from: classes2.dex */
public interface IBatteryPropertiesListener extends IInterface {
    synchronized void batteryPropertiesChanged(BatteryProperties batteryProperties) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IBatteryPropertiesListener {
        private static final String DESCRIPTOR = "android.os.IBatteryPropertiesListener";
        static final int TRANSACTION_batteryPropertiesChanged = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IBatteryPropertiesListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBatteryPropertiesListener)) {
                return (IBatteryPropertiesListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            BatteryProperties _arg0;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = BatteryProperties.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            batteryPropertiesChanged(_arg0);
            return true;
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IBatteryPropertiesListener {
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

            @Override // android.os.IBatteryPropertiesListener
            public synchronized void batteryPropertiesChanged(BatteryProperties props) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (props != null) {
                        _data.writeInt(1);
                        props.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
