package com.xiaopeng.aftersales;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes3.dex */
public interface ILogicActionListener extends IInterface {
    void uploadLogicAction(String str, String str2, String str3, String str4, String str5, String str6, String str7) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ILogicActionListener {
        private static final String DESCRIPTOR = "com.xiaopeng.aftersales.ILogicActionListener";
        static final int TRANSACTION_uploadLogicAction = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILogicActionListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ILogicActionListener)) {
                return (ILogicActionListener) iin;
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
            String _arg0 = data.readString();
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            String _arg3 = data.readString();
            String _arg4 = data.readString();
            String _arg5 = data.readString();
            String _arg6 = data.readString();
            uploadLogicAction(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
            return true;
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements ILogicActionListener {
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

            @Override // com.xiaopeng.aftersales.ILogicActionListener
            public void uploadLogicAction(String issueName, String conclusion, String startTime, String endTime, String logicactionTime, String logicactionEntry, String logictreeVer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(issueName);
                    _data.writeString(conclusion);
                    _data.writeString(startTime);
                    _data.writeString(endTime);
                    _data.writeString(logicactionTime);
                    _data.writeString(logicactionEntry);
                    _data.writeString(logictreeVer);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
