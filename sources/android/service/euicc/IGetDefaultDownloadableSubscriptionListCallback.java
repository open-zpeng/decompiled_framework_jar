package android.service.euicc;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface IGetDefaultDownloadableSubscriptionListCallback extends IInterface {
    @UnsupportedAppUsage
    void onComplete(GetDefaultDownloadableSubscriptionListResult getDefaultDownloadableSubscriptionListResult) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IGetDefaultDownloadableSubscriptionListCallback {
        @Override // android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback
        public void onComplete(GetDefaultDownloadableSubscriptionListResult result) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IGetDefaultDownloadableSubscriptionListCallback {
        private static final String DESCRIPTOR = "android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback";
        static final int TRANSACTION_onComplete = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGetDefaultDownloadableSubscriptionListCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IGetDefaultDownloadableSubscriptionListCallback)) {
                return (IGetDefaultDownloadableSubscriptionListCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onComplete";
            }
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            GetDefaultDownloadableSubscriptionListResult _arg0;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = GetDefaultDownloadableSubscriptionListResult.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            onComplete(_arg0);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IGetDefaultDownloadableSubscriptionListCallback {
            public static IGetDefaultDownloadableSubscriptionListCallback sDefaultImpl;
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

            @Override // android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback
            public void onComplete(GetDefaultDownloadableSubscriptionListResult result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onComplete(result);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IGetDefaultDownloadableSubscriptionListCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IGetDefaultDownloadableSubscriptionListCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
