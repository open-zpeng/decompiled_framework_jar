package android.app.trust;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;

/* loaded from: classes.dex */
public interface ITrustListener extends IInterface {
    void onTrustChanged(boolean z, int i, int i2) throws RemoteException;

    void onTrustError(CharSequence charSequence) throws RemoteException;

    void onTrustManagedChanged(boolean z, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ITrustListener {
        @Override // android.app.trust.ITrustListener
        public void onTrustChanged(boolean enabled, int userId, int flags) throws RemoteException {
        }

        @Override // android.app.trust.ITrustListener
        public void onTrustManagedChanged(boolean managed, int userId) throws RemoteException {
        }

        @Override // android.app.trust.ITrustListener
        public void onTrustError(CharSequence message) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ITrustListener {
        private static final String DESCRIPTOR = "android.app.trust.ITrustListener";
        static final int TRANSACTION_onTrustChanged = 1;
        static final int TRANSACTION_onTrustError = 3;
        static final int TRANSACTION_onTrustManagedChanged = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITrustListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITrustListener)) {
                return (ITrustListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                if (transactionCode != 2) {
                    if (transactionCode == 3) {
                        return "onTrustError";
                    }
                    return null;
                }
                return "onTrustManagedChanged";
            }
            return "onTrustChanged";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            CharSequence _arg02;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                _arg0 = data.readInt() != 0;
                int _arg1 = data.readInt();
                int _arg2 = data.readInt();
                onTrustChanged(_arg0, _arg1, _arg2);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                _arg0 = data.readInt() != 0;
                int _arg12 = data.readInt();
                onTrustManagedChanged(_arg0, _arg12);
                return true;
            } else if (code != 3) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg02 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                onTrustError(_arg02);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements ITrustListener {
            public static ITrustListener sDefaultImpl;
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

            @Override // android.app.trust.ITrustListener
            public void onTrustChanged(boolean enabled, int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTrustChanged(enabled, userId, flags);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustListener
            public void onTrustManagedChanged(boolean managed, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(managed ? 1 : 0);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTrustManagedChanged(managed, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustListener
            public void onTrustError(CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTrustError(message);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ITrustListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ITrustListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
