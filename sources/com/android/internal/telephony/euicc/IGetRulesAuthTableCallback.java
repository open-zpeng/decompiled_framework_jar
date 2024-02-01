package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.euicc.EuiccRulesAuthTable;
/* loaded from: classes3.dex */
public interface IGetRulesAuthTableCallback extends IInterface {
    private protected synchronized void onComplete(int i, EuiccRulesAuthTable euiccRulesAuthTable) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IGetRulesAuthTableCallback {
        public protected static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IGetRulesAuthTableCallback";
        public private protected static final int TRANSACTION_onComplete = 1;

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized IGetRulesAuthTableCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IGetRulesAuthTableCallback)) {
                return (IGetRulesAuthTableCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            EuiccRulesAuthTable _arg1;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            if (data.readInt() != 0) {
                _arg1 = EuiccRulesAuthTable.CREATOR.createFromParcel(data);
            } else {
                _arg1 = null;
            }
            onComplete(_arg0, _arg1);
            return true;
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IGetRulesAuthTableCallback {
            public protected IBinder mRemote;

            public private protected synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            private protected synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            private protected synchronized void onComplete(int resultCode, EuiccRulesAuthTable rat) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(resultCode);
                    if (rat != null) {
                        _data.writeInt(1);
                        rat.writeToParcel(_data, 0);
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
