package android.service.sms;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteCallback;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface IFinancialSmsService extends IInterface {
    void getSmsMessages(RemoteCallback remoteCallback, Bundle bundle) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IFinancialSmsService {
        @Override // android.service.sms.IFinancialSmsService
        public void getSmsMessages(RemoteCallback callback, Bundle params) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IFinancialSmsService {
        private static final String DESCRIPTOR = "android.service.sms.IFinancialSmsService";
        static final int TRANSACTION_getSmsMessages = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFinancialSmsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IFinancialSmsService)) {
                return (IFinancialSmsService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getSmsMessages";
            }
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            RemoteCallback _arg0;
            Bundle _arg1;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = RemoteCallback.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            if (data.readInt() != 0) {
                _arg1 = Bundle.CREATOR.createFromParcel(data);
            } else {
                _arg1 = null;
            }
            getSmsMessages(_arg0, _arg1);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IFinancialSmsService {
            public static IFinancialSmsService sDefaultImpl;
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

            @Override // android.service.sms.IFinancialSmsService
            public void getSmsMessages(RemoteCallback callback, Bundle params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callback != null) {
                        _data.writeInt(1);
                        callback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getSmsMessages(callback, params);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IFinancialSmsService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IFinancialSmsService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
