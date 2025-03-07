package com.android.internal.textservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SuggestionsInfo;

/* loaded from: classes3.dex */
public interface ISpellCheckerSessionListener extends IInterface {
    void onGetSentenceSuggestions(SentenceSuggestionsInfo[] sentenceSuggestionsInfoArr) throws RemoteException;

    void onGetSuggestions(SuggestionsInfo[] suggestionsInfoArr) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements ISpellCheckerSessionListener {
        @Override // com.android.internal.textservice.ISpellCheckerSessionListener
        public void onGetSuggestions(SuggestionsInfo[] results) throws RemoteException {
        }

        @Override // com.android.internal.textservice.ISpellCheckerSessionListener
        public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] result) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ISpellCheckerSessionListener {
        private static final String DESCRIPTOR = "com.android.internal.textservice.ISpellCheckerSessionListener";
        static final int TRANSACTION_onGetSentenceSuggestions = 2;
        static final int TRANSACTION_onGetSuggestions = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISpellCheckerSessionListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISpellCheckerSessionListener)) {
                return (ISpellCheckerSessionListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                if (transactionCode == 2) {
                    return "onGetSentenceSuggestions";
                }
                return null;
            }
            return "onGetSuggestions";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                SuggestionsInfo[] _arg0 = (SuggestionsInfo[]) data.createTypedArray(SuggestionsInfo.CREATOR);
                onGetSuggestions(_arg0);
                return true;
            } else if (code != 2) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                SentenceSuggestionsInfo[] _arg02 = (SentenceSuggestionsInfo[]) data.createTypedArray(SentenceSuggestionsInfo.CREATOR);
                onGetSentenceSuggestions(_arg02);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements ISpellCheckerSessionListener {
            public static ISpellCheckerSessionListener sDefaultImpl;
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

            @Override // com.android.internal.textservice.ISpellCheckerSessionListener
            public void onGetSuggestions(SuggestionsInfo[] results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(results, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onGetSuggestions(results);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.textservice.ISpellCheckerSessionListener
            public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(result, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onGetSentenceSuggestions(result);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISpellCheckerSessionListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ISpellCheckerSessionListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
