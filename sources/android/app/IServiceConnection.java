package android.app;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IServiceConnection extends IInterface {
    private protected void connected(ComponentName componentName, IBinder iBinder, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IServiceConnection {
        private static final String DESCRIPTOR = "android.app.IServiceConnection";
        static final int TRANSACTION_connected = 1;

        /* JADX INFO: Access modifiers changed from: private */
        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IServiceConnection asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IServiceConnection)) {
                return (IServiceConnection) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ComponentName _arg0;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            IBinder _arg1 = data.readStrongBinder();
            boolean _arg2 = data.readInt() != 0;
            connected(_arg0, _arg1, _arg2);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IServiceConnection {
            public protected IBinder mRemote;

            public private protected Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public synchronized void connected(ComponentName name, IBinder service, boolean dead) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(service);
                    _data.writeInt(dead ? 1 : 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
