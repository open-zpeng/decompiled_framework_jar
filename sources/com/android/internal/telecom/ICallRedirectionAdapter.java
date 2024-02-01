package com.android.internal.telecom;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.PhoneAccountHandle;

/* loaded from: classes3.dex */
public interface ICallRedirectionAdapter extends IInterface {
    void cancelCall() throws RemoteException;

    void placeCallUnmodified() throws RemoteException;

    void redirectCall(Uri uri, PhoneAccountHandle phoneAccountHandle, boolean z) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements ICallRedirectionAdapter {
        @Override // com.android.internal.telecom.ICallRedirectionAdapter
        public void cancelCall() throws RemoteException {
        }

        @Override // com.android.internal.telecom.ICallRedirectionAdapter
        public void placeCallUnmodified() throws RemoteException {
        }

        @Override // com.android.internal.telecom.ICallRedirectionAdapter
        public void redirectCall(Uri handle, PhoneAccountHandle targetPhoneAccount, boolean confirmFirst) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ICallRedirectionAdapter {
        private static final String DESCRIPTOR = "com.android.internal.telecom.ICallRedirectionAdapter";
        static final int TRANSACTION_cancelCall = 1;
        static final int TRANSACTION_placeCallUnmodified = 2;
        static final int TRANSACTION_redirectCall = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICallRedirectionAdapter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ICallRedirectionAdapter)) {
                return (ICallRedirectionAdapter) iin;
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
                        return "redirectCall";
                    }
                    return null;
                }
                return "placeCallUnmodified";
            }
            return "cancelCall";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Uri _arg0;
            PhoneAccountHandle _arg1;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                cancelCall();
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                placeCallUnmodified();
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
                    _arg0 = Uri.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                if (data.readInt() != 0) {
                    _arg1 = PhoneAccountHandle.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                boolean _arg2 = data.readInt() != 0;
                redirectCall(_arg0, _arg1, _arg2);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements ICallRedirectionAdapter {
            public static ICallRedirectionAdapter sDefaultImpl;
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

            @Override // com.android.internal.telecom.ICallRedirectionAdapter
            public void cancelCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelCall();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ICallRedirectionAdapter
            public void placeCallUnmodified() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().placeCallUnmodified();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ICallRedirectionAdapter
            public void redirectCall(Uri handle, PhoneAccountHandle targetPhoneAccount, boolean confirmFirst) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (handle != null) {
                        _data.writeInt(1);
                        handle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (targetPhoneAccount != null) {
                        _data.writeInt(1);
                        targetPhoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(confirmFirst ? 1 : 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().redirectCall(handle, targetPhoneAccount, confirmFirst);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ICallRedirectionAdapter impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ICallRedirectionAdapter getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
