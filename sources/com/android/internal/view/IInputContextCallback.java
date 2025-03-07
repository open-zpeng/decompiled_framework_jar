package com.android.internal.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.inputmethod.ExtractedText;

/* loaded from: classes3.dex */
public interface IInputContextCallback extends IInterface {
    void setCommitContentResult(boolean z, int i) throws RemoteException;

    void setCursorCapsMode(int i, int i2) throws RemoteException;

    void setExtractedText(ExtractedText extractedText, int i) throws RemoteException;

    void setRequestUpdateCursorAnchorInfoResult(boolean z, int i) throws RemoteException;

    void setSelectedText(CharSequence charSequence, int i) throws RemoteException;

    void setTextAfterCursor(CharSequence charSequence, int i) throws RemoteException;

    void setTextBeforeCursor(CharSequence charSequence, int i) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IInputContextCallback {
        @Override // com.android.internal.view.IInputContextCallback
        public void setTextBeforeCursor(CharSequence textBeforeCursor, int seq) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputContextCallback
        public void setTextAfterCursor(CharSequence textAfterCursor, int seq) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputContextCallback
        public void setCursorCapsMode(int capsMode, int seq) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputContextCallback
        public void setExtractedText(ExtractedText extractedText, int seq) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputContextCallback
        public void setSelectedText(CharSequence selectedText, int seq) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputContextCallback
        public void setRequestUpdateCursorAnchorInfoResult(boolean result, int seq) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputContextCallback
        public void setCommitContentResult(boolean result, int seq) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IInputContextCallback {
        private static final String DESCRIPTOR = "com.android.internal.view.IInputContextCallback";
        static final int TRANSACTION_setCommitContentResult = 7;
        static final int TRANSACTION_setCursorCapsMode = 3;
        static final int TRANSACTION_setExtractedText = 4;
        static final int TRANSACTION_setRequestUpdateCursorAnchorInfoResult = 6;
        static final int TRANSACTION_setSelectedText = 5;
        static final int TRANSACTION_setTextAfterCursor = 2;
        static final int TRANSACTION_setTextBeforeCursor = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInputContextCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IInputContextCallback)) {
                return (IInputContextCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setTextBeforeCursor";
                case 2:
                    return "setTextAfterCursor";
                case 3:
                    return "setCursorCapsMode";
                case 4:
                    return "setExtractedText";
                case 5:
                    return "setSelectedText";
                case 6:
                    return "setRequestUpdateCursorAnchorInfoResult";
                case 7:
                    return "setCommitContentResult";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            CharSequence _arg0;
            CharSequence _arg02;
            ExtractedText _arg03;
            CharSequence _arg04;
            boolean _arg05;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    int _arg1 = data.readInt();
                    setTextBeforeCursor(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    int _arg12 = data.readInt();
                    setTextAfterCursor(_arg02, _arg12);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    int _arg13 = data.readInt();
                    setCursorCapsMode(_arg06, _arg13);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = ExtractedText.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    int _arg14 = data.readInt();
                    setExtractedText(_arg03, _arg14);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    int _arg15 = data.readInt();
                    setSelectedText(_arg04, _arg15);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _arg05 = data.readInt() != 0;
                    int _arg16 = data.readInt();
                    setRequestUpdateCursorAnchorInfoResult(_arg05, _arg16);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg05 = data.readInt() != 0;
                    int _arg17 = data.readInt();
                    setCommitContentResult(_arg05, _arg17);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IInputContextCallback {
            public static IInputContextCallback sDefaultImpl;
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

            @Override // com.android.internal.view.IInputContextCallback
            public void setTextBeforeCursor(CharSequence textBeforeCursor, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (textBeforeCursor != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(textBeforeCursor, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(seq);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTextBeforeCursor(textBeforeCursor, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputContextCallback
            public void setTextAfterCursor(CharSequence textAfterCursor, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (textAfterCursor != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(textAfterCursor, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(seq);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTextAfterCursor(textAfterCursor, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputContextCallback
            public void setCursorCapsMode(int capsMode, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capsMode);
                    _data.writeInt(seq);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCursorCapsMode(capsMode, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputContextCallback
            public void setExtractedText(ExtractedText extractedText, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (extractedText != null) {
                        _data.writeInt(1);
                        extractedText.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(seq);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setExtractedText(extractedText, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputContextCallback
            public void setSelectedText(CharSequence selectedText, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (selectedText != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(selectedText, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(seq);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSelectedText(selectedText, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputContextCallback
            public void setRequestUpdateCursorAnchorInfoResult(boolean result, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result ? 1 : 0);
                    _data.writeInt(seq);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRequestUpdateCursorAnchorInfoResult(result, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputContextCallback
            public void setCommitContentResult(boolean result, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result ? 1 : 0);
                    _data.writeInt(seq);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCommitContentResult(result, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IInputContextCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IInputContextCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
