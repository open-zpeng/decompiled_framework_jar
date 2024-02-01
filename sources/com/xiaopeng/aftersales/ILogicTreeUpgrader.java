package com.xiaopeng.aftersales;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes3.dex */
public interface ILogicTreeUpgrader extends IInterface {
    void onUpgradeStatus(boolean z) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ILogicTreeUpgrader {
        private static final String DESCRIPTOR = "com.xiaopeng.aftersales.ILogicTreeUpgrader";
        static final int TRANSACTION_onUpgradeStatus = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILogicTreeUpgrader asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ILogicTreeUpgrader)) {
                return (ILogicTreeUpgrader) iin;
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
            boolean _arg0 = data.readInt() != 0;
            onUpgradeStatus(_arg0);
            return true;
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements ILogicTreeUpgrader {
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

            @Override // com.xiaopeng.aftersales.ILogicTreeUpgrader
            public void onUpgradeStatus(boolean status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status ? 1 : 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
