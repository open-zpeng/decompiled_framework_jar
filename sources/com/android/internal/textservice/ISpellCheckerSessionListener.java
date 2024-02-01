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
    synchronized void onGetSentenceSuggestions(SentenceSuggestionsInfo[] sentenceSuggestionsInfoArr) throws RemoteException;

    synchronized void onGetSuggestions(SuggestionsInfo[] suggestionsInfoArr) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ISpellCheckerSessionListener {
        private static final String DESCRIPTOR = "com.android.internal.textservice.ISpellCheckerSessionListener";
        static final int TRANSACTION_onGetSentenceSuggestions = 2;
        static final int TRANSACTION_onGetSuggestions = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ISpellCheckerSessionListener asInterface(IBinder obj) {
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

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    SuggestionsInfo[] _arg0 = (SuggestionsInfo[]) data.createTypedArray(SuggestionsInfo.CREATOR);
                    onGetSuggestions(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    SentenceSuggestionsInfo[] _arg02 = (SentenceSuggestionsInfo[]) data.createTypedArray(SentenceSuggestionsInfo.CREATOR);
                    onGetSentenceSuggestions(_arg02);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements ISpellCheckerSessionListener {
            private IBinder mRemote;

            synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.android.internal.textservice.ISpellCheckerSessionListener
            public synchronized void onGetSuggestions(SuggestionsInfo[] results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(results, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.textservice.ISpellCheckerSessionListener
            public synchronized void onGetSentenceSuggestions(SentenceSuggestionsInfo[] result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(result, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
