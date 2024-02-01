package android.view.autofill;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.autofill.IAutofillWindowPresenter;
import java.util.List;
/* loaded from: classes2.dex */
public interface IAutoFillManagerClient extends IInterface {
    synchronized void authenticate(int i, int i2, IntentSender intentSender, Intent intent) throws RemoteException;

    synchronized void autofill(int i, List<AutofillId> list, List<AutofillValue> list2) throws RemoteException;

    synchronized void dispatchUnhandledKey(int i, AutofillId autofillId, KeyEvent keyEvent) throws RemoteException;

    synchronized void notifyNoFillUi(int i, AutofillId autofillId, int i2) throws RemoteException;

    synchronized void requestHideFillUi(int i, AutofillId autofillId) throws RemoteException;

    synchronized void requestShowFillUi(int i, AutofillId autofillId, int i2, int i3, Rect rect, IAutofillWindowPresenter iAutofillWindowPresenter) throws RemoteException;

    synchronized void setSaveUiState(int i, boolean z) throws RemoteException;

    synchronized void setSessionFinished(int i) throws RemoteException;

    synchronized void setState(int i) throws RemoteException;

    synchronized void setTrackedViews(int i, AutofillId[] autofillIdArr, boolean z, boolean z2, AutofillId[] autofillIdArr2, AutofillId autofillId) throws RemoteException;

    synchronized void startIntentSender(IntentSender intentSender, Intent intent) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IAutoFillManagerClient {
        private static final String DESCRIPTOR = "android.view.autofill.IAutoFillManagerClient";
        static final int TRANSACTION_authenticate = 3;
        static final int TRANSACTION_autofill = 2;
        static final int TRANSACTION_dispatchUnhandledKey = 8;
        static final int TRANSACTION_notifyNoFillUi = 7;
        static final int TRANSACTION_requestHideFillUi = 6;
        static final int TRANSACTION_requestShowFillUi = 5;
        static final int TRANSACTION_setSaveUiState = 10;
        static final int TRANSACTION_setSessionFinished = 11;
        static final int TRANSACTION_setState = 1;
        static final int TRANSACTION_setTrackedViews = 4;
        static final int TRANSACTION_startIntentSender = 9;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IAutoFillManagerClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAutoFillManagerClient)) {
                return (IAutoFillManagerClient) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IntentSender _arg2;
            AutofillId _arg5;
            AutofillId _arg1;
            Rect _arg4;
            AutofillId _arg12;
            IntentSender _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    setState(_arg02);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    List<AutofillId> _arg13 = data.createTypedArrayList(AutofillId.CREATOR);
                    List<AutofillValue> _arg22 = data.createTypedArrayList(AutofillValue.CREATOR);
                    autofill(_arg03, _arg13, _arg22);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    int _arg14 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = IntentSender.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    Intent _arg3 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    authenticate(_arg04, _arg14, _arg2, _arg3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    AutofillId[] _arg15 = (AutofillId[]) data.createTypedArray(AutofillId.CREATOR);
                    boolean _arg23 = data.readInt() != 0;
                    boolean _arg32 = data.readInt() != 0;
                    AutofillId[] _arg42 = (AutofillId[]) data.createTypedArray(AutofillId.CREATOR);
                    if (data.readInt() != 0) {
                        AutofillId _arg52 = AutofillId.CREATOR.createFromParcel(data);
                        _arg5 = _arg52;
                    } else {
                        _arg5 = null;
                    }
                    setTrackedViews(_arg05, _arg15, _arg23, _arg32, _arg42, _arg5);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    if (data.readInt() != 0) {
                        AutofillId _arg16 = AutofillId.CREATOR.createFromParcel(data);
                        _arg1 = _arg16;
                    } else {
                        _arg1 = null;
                    }
                    int _arg24 = data.readInt();
                    int _arg33 = data.readInt();
                    if (data.readInt() != 0) {
                        Rect _arg43 = Rect.CREATOR.createFromParcel(data);
                        _arg4 = _arg43;
                    } else {
                        _arg4 = null;
                    }
                    IAutofillWindowPresenter _arg53 = IAutofillWindowPresenter.Stub.asInterface(data.readStrongBinder());
                    requestShowFillUi(_arg06, _arg1, _arg24, _arg33, _arg4, _arg53);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    AutofillId _arg17 = data.readInt() != 0 ? AutofillId.CREATOR.createFromParcel(data) : null;
                    requestHideFillUi(_arg07, _arg17);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    AutofillId _arg18 = data.readInt() != 0 ? AutofillId.CREATOR.createFromParcel(data) : null;
                    int _arg25 = data.readInt();
                    notifyNoFillUi(_arg08, _arg18, _arg25);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = AutofillId.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    KeyEvent _arg26 = data.readInt() != 0 ? KeyEvent.CREATOR.createFromParcel(data) : null;
                    dispatchUnhandledKey(_arg09, _arg12, _arg26);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = IntentSender.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    Intent _arg19 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    startIntentSender(_arg0, _arg19);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    boolean _arg110 = data.readInt() != 0;
                    setSaveUiState(_arg010, _arg110);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    setSessionFinished(_arg011);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IAutoFillManagerClient {
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

            @Override // android.view.autofill.IAutoFillManagerClient
            public synchronized void setState(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public synchronized void autofill(int sessionId, List<AutofillId> ids, List<AutofillValue> values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeTypedList(ids);
                    _data.writeTypedList(values);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public synchronized void authenticate(int sessionId, int authenticationId, IntentSender intent, Intent fillInIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(authenticationId);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fillInIntent != null) {
                        _data.writeInt(1);
                        fillInIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public synchronized void setTrackedViews(int sessionId, AutofillId[] savableIds, boolean saveOnAllViewsInvisible, boolean saveOnFinish, AutofillId[] fillableIds, AutofillId saveTriggerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeTypedArray(savableIds, 0);
                    _data.writeInt(saveOnAllViewsInvisible ? 1 : 0);
                    _data.writeInt(saveOnFinish ? 1 : 0);
                    _data.writeTypedArray(fillableIds, 0);
                    if (saveTriggerId != null) {
                        _data.writeInt(1);
                        saveTriggerId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public synchronized void requestShowFillUi(int sessionId, AutofillId id, int width, int height, Rect anchorBounds, IAutofillWindowPresenter presenter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (id != null) {
                        _data.writeInt(1);
                        id.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(width);
                    _data.writeInt(height);
                    if (anchorBounds != null) {
                        _data.writeInt(1);
                        anchorBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(presenter != null ? presenter.asBinder() : null);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public synchronized void requestHideFillUi(int sessionId, AutofillId id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (id != null) {
                        _data.writeInt(1);
                        id.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public synchronized void notifyNoFillUi(int sessionId, AutofillId id, int sessionFinishedState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (id != null) {
                        _data.writeInt(1);
                        id.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sessionFinishedState);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public synchronized void dispatchUnhandledKey(int sessionId, AutofillId id, KeyEvent keyEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (id != null) {
                        _data.writeInt(1);
                        id.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (keyEvent != null) {
                        _data.writeInt(1);
                        keyEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public synchronized void startIntentSender(IntentSender intentSender, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intentSender != null) {
                        _data.writeInt(1);
                        intentSender.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public synchronized void setSaveUiState(int sessionId, boolean shown) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(shown ? 1 : 0);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public synchronized void setSessionFinished(int newState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newState);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
