package com.android.internal.telecom;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.PhoneAccountSuggestion;
import java.util.List;

/* loaded from: classes3.dex */
public interface IPhoneAccountSuggestionCallback extends IInterface {
    void suggestPhoneAccounts(String str, List<PhoneAccountSuggestion> list) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IPhoneAccountSuggestionCallback {
        @Override // com.android.internal.telecom.IPhoneAccountSuggestionCallback
        public void suggestPhoneAccounts(String number, List<PhoneAccountSuggestion> suggestions) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IPhoneAccountSuggestionCallback {
        private static final String DESCRIPTOR = "com.android.internal.telecom.IPhoneAccountSuggestionCallback";
        static final int TRANSACTION_suggestPhoneAccounts = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPhoneAccountSuggestionCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IPhoneAccountSuggestionCallback)) {
                return (IPhoneAccountSuggestionCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "suggestPhoneAccounts";
            }
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
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
            List<PhoneAccountSuggestion> _arg1 = data.createTypedArrayList(PhoneAccountSuggestion.CREATOR);
            suggestPhoneAccounts(_arg0, _arg1);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IPhoneAccountSuggestionCallback {
            public static IPhoneAccountSuggestionCallback sDefaultImpl;
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

            @Override // com.android.internal.telecom.IPhoneAccountSuggestionCallback
            public void suggestPhoneAccounts(String number, List<PhoneAccountSuggestion> suggestions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    _data.writeTypedList(suggestions);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().suggestPhoneAccounts(number, suggestions);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IPhoneAccountSuggestionCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IPhoneAccountSuggestionCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
