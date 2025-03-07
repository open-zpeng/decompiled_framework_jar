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
import com.android.internal.os.IResultReceiver;
import java.util.List;

/* loaded from: classes3.dex */
public interface IAutoFillManagerClient extends IInterface {
    void authenticate(int i, int i2, IntentSender intentSender, Intent intent) throws RemoteException;

    void autofill(int i, List<AutofillId> list, List<AutofillValue> list2) throws RemoteException;

    void dispatchUnhandledKey(int i, AutofillId autofillId, KeyEvent keyEvent) throws RemoteException;

    void getAugmentedAutofillClient(IResultReceiver iResultReceiver) throws RemoteException;

    void notifyNoFillUi(int i, AutofillId autofillId, int i2) throws RemoteException;

    void requestHideFillUi(int i, AutofillId autofillId) throws RemoteException;

    void requestShowFillUi(int i, AutofillId autofillId, int i2, int i3, Rect rect, IAutofillWindowPresenter iAutofillWindowPresenter) throws RemoteException;

    void setSaveUiState(int i, boolean z) throws RemoteException;

    void setSessionFinished(int i, List<AutofillId> list) throws RemoteException;

    void setState(int i) throws RemoteException;

    void setTrackedViews(int i, AutofillId[] autofillIdArr, boolean z, boolean z2, AutofillId[] autofillIdArr2, AutofillId autofillId) throws RemoteException;

    void startIntentSender(IntentSender intentSender, Intent intent) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IAutoFillManagerClient {
        @Override // android.view.autofill.IAutoFillManagerClient
        public void setState(int flags) throws RemoteException {
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public void autofill(int sessionId, List<AutofillId> ids, List<AutofillValue> values) throws RemoteException {
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public void authenticate(int sessionId, int authenticationId, IntentSender intent, Intent fillInIntent) throws RemoteException {
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public void setTrackedViews(int sessionId, AutofillId[] savableIds, boolean saveOnAllViewsInvisible, boolean saveOnFinish, AutofillId[] fillableIds, AutofillId saveTriggerId) throws RemoteException {
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public void requestShowFillUi(int sessionId, AutofillId id, int width, int height, Rect anchorBounds, IAutofillWindowPresenter presenter) throws RemoteException {
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public void requestHideFillUi(int sessionId, AutofillId id) throws RemoteException {
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public void notifyNoFillUi(int sessionId, AutofillId id, int sessionFinishedState) throws RemoteException {
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public void dispatchUnhandledKey(int sessionId, AutofillId id, KeyEvent keyEvent) throws RemoteException {
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public void startIntentSender(IntentSender intentSender, Intent intent) throws RemoteException {
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public void setSaveUiState(int sessionId, boolean shown) throws RemoteException {
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public void setSessionFinished(int newState, List<AutofillId> autofillableIds) throws RemoteException {
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public void getAugmentedAutofillClient(IResultReceiver result) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IAutoFillManagerClient {
        private static final String DESCRIPTOR = "android.view.autofill.IAutoFillManagerClient";
        static final int TRANSACTION_authenticate = 3;
        static final int TRANSACTION_autofill = 2;
        static final int TRANSACTION_dispatchUnhandledKey = 8;
        static final int TRANSACTION_getAugmentedAutofillClient = 12;
        static final int TRANSACTION_notifyNoFillUi = 7;
        static final int TRANSACTION_requestHideFillUi = 6;
        static final int TRANSACTION_requestShowFillUi = 5;
        static final int TRANSACTION_setSaveUiState = 10;
        static final int TRANSACTION_setSessionFinished = 11;
        static final int TRANSACTION_setState = 1;
        static final int TRANSACTION_setTrackedViews = 4;
        static final int TRANSACTION_startIntentSender = 9;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAutoFillManagerClient asInterface(IBinder obj) {
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

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setState";
                case 2:
                    return "autofill";
                case 3:
                    return "authenticate";
                case 4:
                    return "setTrackedViews";
                case 5:
                    return "requestShowFillUi";
                case 6:
                    return "requestHideFillUi";
                case 7:
                    return "notifyNoFillUi";
                case 8:
                    return "dispatchUnhandledKey";
                case 9:
                    return "startIntentSender";
                case 10:
                    return "setSaveUiState";
                case 11:
                    return "setSessionFinished";
                case 12:
                    return "getAugmentedAutofillClient";
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
            IntentSender _arg2;
            Intent _arg3;
            AutofillId _arg5;
            AutofillId _arg1;
            Rect _arg4;
            AutofillId _arg12;
            AutofillId _arg13;
            AutofillId _arg14;
            KeyEvent _arg22;
            IntentSender _arg0;
            Intent _arg15;
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
                    List<AutofillId> _arg16 = data.createTypedArrayList(AutofillId.CREATOR);
                    List<AutofillValue> _arg23 = data.createTypedArrayList(AutofillValue.CREATOR);
                    autofill(_arg03, _arg16, _arg23);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    int _arg17 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = IntentSender.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg3 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    authenticate(_arg04, _arg17, _arg2, _arg3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    AutofillId[] _arg18 = (AutofillId[]) data.createTypedArray(AutofillId.CREATOR);
                    boolean _arg24 = data.readInt() != 0;
                    boolean _arg32 = data.readInt() != 0;
                    AutofillId[] _arg42 = (AutofillId[]) data.createTypedArray(AutofillId.CREATOR);
                    if (data.readInt() != 0) {
                        _arg5 = AutofillId.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    setTrackedViews(_arg05, _arg18, _arg24, _arg32, _arg42, _arg5);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = AutofillId.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    int _arg25 = data.readInt();
                    int _arg33 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg4 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    IAutofillWindowPresenter _arg52 = IAutofillWindowPresenter.Stub.asInterface(data.readStrongBinder());
                    requestShowFillUi(_arg06, _arg1, _arg25, _arg33, _arg4, _arg52);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = AutofillId.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    requestHideFillUi(_arg07, _arg12);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = AutofillId.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    int _arg26 = data.readInt();
                    notifyNoFillUi(_arg08, _arg13, _arg26);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg14 = AutofillId.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg22 = KeyEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    dispatchUnhandledKey(_arg09, _arg14, _arg22);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = IntentSender.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg15 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    startIntentSender(_arg0, _arg15);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    boolean _arg19 = data.readInt() != 0;
                    setSaveUiState(_arg010, _arg19);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    List<AutofillId> _arg110 = data.createTypedArrayList(AutofillId.CREATOR);
                    setSessionFinished(_arg011, _arg110);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    IResultReceiver _arg012 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                    getAugmentedAutofillClient(_arg012);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IAutoFillManagerClient {
            public static IAutoFillManagerClient sDefaultImpl;
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

            @Override // android.view.autofill.IAutoFillManagerClient
            public void setState(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setState(flags);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public void autofill(int sessionId, List<AutofillId> ids, List<AutofillValue> values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeTypedList(ids);
                    _data.writeTypedList(values);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().autofill(sessionId, ids, values);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public void authenticate(int sessionId, int authenticationId, IntentSender intent, Intent fillInIntent) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().authenticate(sessionId, authenticationId, intent, fillInIntent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public void setTrackedViews(int sessionId, AutofillId[] savableIds, boolean saveOnAllViewsInvisible, boolean saveOnFinish, AutofillId[] fillableIds, AutofillId saveTriggerId) throws RemoteException {
                int i;
                int i2;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(sessionId);
                } catch (Throwable th2) {
                    th = th2;
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeTypedArray(savableIds, 0);
                    if (!saveOnAllViewsInvisible) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (!saveOnFinish) {
                        i2 = 0;
                    } else {
                        i2 = 1;
                    }
                    _data.writeInt(i2);
                    try {
                        _data.writeTypedArray(fillableIds, 0);
                        if (saveTriggerId != null) {
                            _data.writeInt(1);
                            saveTriggerId.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _data.recycle();
                    throw th;
                }
                try {
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTrackedViews(sessionId, savableIds, saveOnAllViewsInvisible, saveOnFinish, fillableIds, saveTriggerId);
                        _data.recycle();
                        return;
                    }
                    _data.recycle();
                } catch (Throwable th5) {
                    th = th5;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public void requestShowFillUi(int sessionId, AutofillId id, int width, int height, Rect anchorBounds, IAutofillWindowPresenter presenter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(sessionId);
                    if (id != null) {
                        _data.writeInt(1);
                        id.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(width);
                    try {
                        _data.writeInt(height);
                        if (anchorBounds != null) {
                            _data.writeInt(1);
                            anchorBounds.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeStrongBinder(presenter != null ? presenter.asBinder() : null);
                        try {
                            boolean _status = this.mRemote.transact(5, _data, null, 1);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                Stub.getDefaultImpl().requestShowFillUi(sessionId, id, width, height, anchorBounds, presenter);
                                _data.recycle();
                                return;
                            }
                            _data.recycle();
                        } catch (Throwable th3) {
                            th = th3;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public void requestHideFillUi(int sessionId, AutofillId id) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestHideFillUi(sessionId, id);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public void notifyNoFillUi(int sessionId, AutofillId id, int sessionFinishedState) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyNoFillUi(sessionId, id, sessionFinishedState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public void dispatchUnhandledKey(int sessionId, AutofillId id, KeyEvent keyEvent) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchUnhandledKey(sessionId, id, keyEvent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public void startIntentSender(IntentSender intentSender, Intent intent) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startIntentSender(intentSender, intent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public void setSaveUiState(int sessionId, boolean shown) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(shown ? 1 : 0);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSaveUiState(sessionId, shown);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public void setSessionFinished(int newState, List<AutofillId> autofillableIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newState);
                    _data.writeTypedList(autofillableIds);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSessionFinished(newState, autofillableIds);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManagerClient
            public void getAugmentedAutofillClient(IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getAugmentedAutofillClient(result);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IAutoFillManagerClient impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IAutoFillManagerClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
