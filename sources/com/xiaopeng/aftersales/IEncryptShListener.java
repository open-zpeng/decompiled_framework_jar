package com.xiaopeng.aftersales;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes3.dex */
public interface IEncryptShListener extends IInterface {
    void onEncryptShResponse(int i, String str, String str2, boolean z) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IEncryptShListener {
        private static final String DESCRIPTOR = "com.xiaopeng.aftersales.IEncryptShListener";
        static final int TRANSACTION_onEncryptShResponse = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEncryptShListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IEncryptShListener)) {
                return (IEncryptShListener) iin;
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
            String _arg2 = data.readString();
            boolean _arg3 = data.readInt() != 0;
            onEncryptShResponse(_arg0, _arg1, _arg2, _arg3);
            return true;
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IEncryptShListener {
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

            @Override // com.xiaopeng.aftersales.IEncryptShListener
            public void onEncryptShResponse(int errorcode, String resultPath, String outputPath, boolean isCloudCmd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(errorcode);
                    _data.writeString(resultPath);
                    _data.writeString(outputPath);
                    _data.writeInt(isCloudCmd ? 1 : 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
