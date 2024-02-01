package android.service.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IGetEuiccProfileInfoListCallback extends IInterface {
    /* JADX INFO: Access modifiers changed from: private */
    void onComplete(GetEuiccProfileInfoListResult getEuiccProfileInfoListResult) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IGetEuiccProfileInfoListCallback {
        private static final String DESCRIPTOR = "android.service.euicc.IGetEuiccProfileInfoListCallback";
        static final int TRANSACTION_onComplete = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IGetEuiccProfileInfoListCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IGetEuiccProfileInfoListCallback)) {
                return (IGetEuiccProfileInfoListCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            GetEuiccProfileInfoListResult _arg0;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = GetEuiccProfileInfoListResult.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            onComplete(_arg0);
            return true;
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IGetEuiccProfileInfoListCallback {
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

            public synchronized void onComplete(GetEuiccProfileInfoListResult result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
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
