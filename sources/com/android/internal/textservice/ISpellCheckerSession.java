package com.android.internal.textservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.textservice.TextInfo;
/* loaded from: classes3.dex */
public interface ISpellCheckerSession extends IInterface {
    synchronized void onCancel() throws RemoteException;

    synchronized void onClose() throws RemoteException;

    synchronized void onGetSentenceSuggestionsMultiple(TextInfo[] textInfoArr, int i) throws RemoteException;

    synchronized void onGetSuggestionsMultiple(TextInfo[] textInfoArr, int i, boolean z) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ISpellCheckerSession {
        private static final String DESCRIPTOR = "com.android.internal.textservice.ISpellCheckerSession";
        static final int TRANSACTION_onCancel = 3;
        static final int TRANSACTION_onClose = 4;
        static final int TRANSACTION_onGetSentenceSuggestionsMultiple = 2;
        static final int TRANSACTION_onGetSuggestionsMultiple = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ISpellCheckerSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISpellCheckerSession)) {
                return (ISpellCheckerSession) iin;
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
                    TextInfo[] _arg0 = (TextInfo[]) data.createTypedArray(TextInfo.CREATOR);
                    int _arg1 = data.readInt();
                    boolean _arg2 = data.readInt() != 0;
                    onGetSuggestionsMultiple(_arg0, _arg1, _arg2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    TextInfo[] _arg02 = (TextInfo[]) data.createTypedArray(TextInfo.CREATOR);
                    int _arg12 = data.readInt();
                    onGetSentenceSuggestionsMultiple(_arg02, _arg12);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onCancel();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onClose();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements ISpellCheckerSession {
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

            @Override // com.android.internal.textservice.ISpellCheckerSession
            public synchronized void onGetSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit, boolean multipleWords) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(textInfos, 0);
                    _data.writeInt(suggestionsLimit);
                    _data.writeInt(multipleWords ? 1 : 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.textservice.ISpellCheckerSession
            public synchronized void onGetSentenceSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(textInfos, 0);
                    _data.writeInt(suggestionsLimit);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.textservice.ISpellCheckerSession
            public synchronized void onCancel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.textservice.ISpellCheckerSession
            public synchronized void onClose() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
