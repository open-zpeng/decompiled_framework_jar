package android.service.textclassifier;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.service.textclassifier.ITextClassificationCallback;
import android.service.textclassifier.ITextLinksCallback;
import android.service.textclassifier.ITextSelectionCallback;
import android.view.textclassifier.SelectionEvent;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassificationContext;
import android.view.textclassifier.TextClassificationSessionId;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextSelection;
/* loaded from: classes2.dex */
public interface ITextClassifierService extends IInterface {
    synchronized void onClassifyText(TextClassificationSessionId textClassificationSessionId, TextClassification.Request request, ITextClassificationCallback iTextClassificationCallback) throws RemoteException;

    synchronized void onCreateTextClassificationSession(TextClassificationContext textClassificationContext, TextClassificationSessionId textClassificationSessionId) throws RemoteException;

    synchronized void onDestroyTextClassificationSession(TextClassificationSessionId textClassificationSessionId) throws RemoteException;

    synchronized void onGenerateLinks(TextClassificationSessionId textClassificationSessionId, TextLinks.Request request, ITextLinksCallback iTextLinksCallback) throws RemoteException;

    synchronized void onSelectionEvent(TextClassificationSessionId textClassificationSessionId, SelectionEvent selectionEvent) throws RemoteException;

    synchronized void onSuggestSelection(TextClassificationSessionId textClassificationSessionId, TextSelection.Request request, ITextSelectionCallback iTextSelectionCallback) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ITextClassifierService {
        private static final String DESCRIPTOR = "android.service.textclassifier.ITextClassifierService";
        static final int TRANSACTION_onClassifyText = 2;
        static final int TRANSACTION_onCreateTextClassificationSession = 5;
        static final int TRANSACTION_onDestroyTextClassificationSession = 6;
        static final int TRANSACTION_onGenerateLinks = 3;
        static final int TRANSACTION_onSelectionEvent = 4;
        static final int TRANSACTION_onSuggestSelection = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ITextClassifierService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITextClassifierService)) {
                return (ITextClassifierService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            TextClassificationSessionId _arg0;
            TextClassificationSessionId _arg02;
            TextClassificationSessionId _arg03;
            TextClassificationSessionId _arg04;
            TextClassificationContext _arg05;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = TextClassificationSessionId.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    TextSelection.Request _arg1 = data.readInt() != 0 ? TextSelection.Request.CREATOR.createFromParcel(data) : null;
                    ITextSelectionCallback _arg2 = ITextSelectionCallback.Stub.asInterface(data.readStrongBinder());
                    onSuggestSelection(_arg0, _arg1, _arg2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = TextClassificationSessionId.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    TextClassification.Request _arg12 = data.readInt() != 0 ? TextClassification.Request.CREATOR.createFromParcel(data) : null;
                    ITextClassificationCallback _arg22 = ITextClassificationCallback.Stub.asInterface(data.readStrongBinder());
                    onClassifyText(_arg02, _arg12, _arg22);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = TextClassificationSessionId.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    TextLinks.Request _arg13 = data.readInt() != 0 ? TextLinks.Request.CREATOR.createFromParcel(data) : null;
                    ITextLinksCallback _arg23 = ITextLinksCallback.Stub.asInterface(data.readStrongBinder());
                    onGenerateLinks(_arg03, _arg13, _arg23);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = TextClassificationSessionId.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    SelectionEvent _arg14 = data.readInt() != 0 ? SelectionEvent.CREATOR.createFromParcel(data) : null;
                    onSelectionEvent(_arg04, _arg14);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = TextClassificationContext.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    TextClassificationSessionId _arg15 = data.readInt() != 0 ? TextClassificationSessionId.CREATOR.createFromParcel(data) : null;
                    onCreateTextClassificationSession(_arg05, _arg15);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    TextClassificationSessionId _arg06 = data.readInt() != 0 ? TextClassificationSessionId.CREATOR.createFromParcel(data) : null;
                    onDestroyTextClassificationSession(_arg06);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ITextClassifierService {
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

            @Override // android.service.textclassifier.ITextClassifierService
            public synchronized void onSuggestSelection(TextClassificationSessionId sessionId, TextSelection.Request request, ITextSelectionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionId != null) {
                        _data.writeInt(1);
                        sessionId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.textclassifier.ITextClassifierService
            public synchronized void onClassifyText(TextClassificationSessionId sessionId, TextClassification.Request request, ITextClassificationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionId != null) {
                        _data.writeInt(1);
                        sessionId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.textclassifier.ITextClassifierService
            public synchronized void onGenerateLinks(TextClassificationSessionId sessionId, TextLinks.Request request, ITextLinksCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionId != null) {
                        _data.writeInt(1);
                        sessionId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.textclassifier.ITextClassifierService
            public synchronized void onSelectionEvent(TextClassificationSessionId sessionId, SelectionEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionId != null) {
                        _data.writeInt(1);
                        sessionId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.textclassifier.ITextClassifierService
            public synchronized void onCreateTextClassificationSession(TextClassificationContext context, TextClassificationSessionId sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (context != null) {
                        _data.writeInt(1);
                        context.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sessionId != null) {
                        _data.writeInt(1);
                        sessionId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.textclassifier.ITextClassifierService
            public synchronized void onDestroyTextClassificationSession(TextClassificationSessionId sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionId != null) {
                        _data.writeInt(1);
                        sessionId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
