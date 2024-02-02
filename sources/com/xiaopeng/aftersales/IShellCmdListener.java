package com.xiaopeng.aftersales;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes3.dex */
public interface IShellCmdListener extends IInterface {
    void onShellResponse(int i, String str, boolean z) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IShellCmdListener {
        private static final String DESCRIPTOR = "com.xiaopeng.aftersales.IShellCmdListener";
        static final int TRANSACTION_onShellResponse = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IShellCmdListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IShellCmdListener)) {
                return (IShellCmdListener) iin;
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
            int _arg0 = data.readInt();
            String _arg1 = data.readString();
            boolean _arg2 = data.readInt() != 0;
            onShellResponse(_arg0, _arg1, _arg2);
            return true;
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IShellCmdListener {
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

            @Override // com.xiaopeng.aftersales.IShellCmdListener
            public void onShellResponse(int errorcode, String resultPath, boolean isCloudCmd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(errorcode);
                    _data.writeString(resultPath);
                    _data.writeInt(isCloudCmd ? 1 : 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
