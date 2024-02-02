package android.view.autofill;

import android.content.ComponentName;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.service.autofill.FillEventHistory;
import android.service.autofill.UserData;
import android.view.autofill.IAutoFillManagerClient;
import java.util.List;
/* loaded from: classes2.dex */
public interface IAutoFillManager extends IInterface {
    synchronized int addClient(IAutoFillManagerClient iAutoFillManagerClient, int i) throws RemoteException;

    synchronized void cancelSession(int i, int i2) throws RemoteException;

    synchronized void disableOwnedAutofillServices(int i) throws RemoteException;

    synchronized void finishSession(int i, int i2) throws RemoteException;

    synchronized ComponentName getAutofillServiceComponentName() throws RemoteException;

    synchronized String[] getAvailableFieldClassificationAlgorithms() throws RemoteException;

    synchronized String getDefaultFieldClassificationAlgorithm() throws RemoteException;

    synchronized FillEventHistory getFillEventHistory() throws RemoteException;

    synchronized UserData getUserData() throws RemoteException;

    synchronized String getUserDataId() throws RemoteException;

    synchronized boolean isFieldClassificationEnabled() throws RemoteException;

    synchronized boolean isServiceEnabled(int i, String str) throws RemoteException;

    synchronized boolean isServiceSupported(int i) throws RemoteException;

    synchronized void onPendingSaveUi(int i, IBinder iBinder) throws RemoteException;

    synchronized void removeClient(IAutoFillManagerClient iAutoFillManagerClient, int i) throws RemoteException;

    synchronized boolean restoreSession(int i, IBinder iBinder, IBinder iBinder2) throws RemoteException;

    synchronized void setAuthenticationResult(Bundle bundle, int i, int i2, int i3) throws RemoteException;

    synchronized void setAutofillFailure(int i, List<AutofillId> list, int i2) throws RemoteException;

    synchronized void setHasCallback(int i, int i2, boolean z) throws RemoteException;

    synchronized void setUserData(UserData userData) throws RemoteException;

    synchronized int startSession(IBinder iBinder, IBinder iBinder2, AutofillId autofillId, Rect rect, AutofillValue autofillValue, int i, boolean z, int i2, ComponentName componentName, boolean z2) throws RemoteException;

    synchronized int updateOrRestartSession(IBinder iBinder, IBinder iBinder2, AutofillId autofillId, Rect rect, AutofillValue autofillValue, int i, boolean z, int i2, ComponentName componentName, int i3, int i4, boolean z2) throws RemoteException;

    synchronized void updateSession(int i, AutofillId autofillId, Rect rect, AutofillValue autofillValue, int i2, int i3, int i4) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IAutoFillManager {
        private static final String DESCRIPTOR = "android.view.autofill.IAutoFillManager";
        static final int TRANSACTION_addClient = 1;
        static final int TRANSACTION_cancelSession = 10;
        static final int TRANSACTION_disableOwnedAutofillServices = 13;
        static final int TRANSACTION_finishSession = 9;
        static final int TRANSACTION_getAutofillServiceComponentName = 21;
        static final int TRANSACTION_getAvailableFieldClassificationAlgorithms = 22;
        static final int TRANSACTION_getDefaultFieldClassificationAlgorithm = 23;
        static final int TRANSACTION_getFillEventHistory = 4;
        static final int TRANSACTION_getUserData = 17;
        static final int TRANSACTION_getUserDataId = 18;
        static final int TRANSACTION_isFieldClassificationEnabled = 20;
        static final int TRANSACTION_isServiceEnabled = 15;
        static final int TRANSACTION_isServiceSupported = 14;
        static final int TRANSACTION_onPendingSaveUi = 16;
        static final int TRANSACTION_removeClient = 2;
        static final int TRANSACTION_restoreSession = 5;
        static final int TRANSACTION_setAuthenticationResult = 11;
        static final int TRANSACTION_setAutofillFailure = 8;
        static final int TRANSACTION_setHasCallback = 12;
        static final int TRANSACTION_setUserData = 19;
        static final int TRANSACTION_startSession = 3;
        static final int TRANSACTION_updateOrRestartSession = 7;
        static final int TRANSACTION_updateSession = 6;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IAutoFillManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAutoFillManager)) {
                return (IAutoFillManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            AutofillId _arg2;
            Rect _arg3;
            AutofillValue _arg4;
            AutofillId _arg1;
            Rect _arg22;
            AutofillValue _arg32;
            AutofillId _arg23;
            Rect _arg33;
            AutofillValue _arg42;
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        IAutoFillManagerClient _arg0 = IAutoFillManagerClient.Stub.asInterface(data.readStrongBinder());
                        int _arg12 = data.readInt();
                        int _result = addClient(_arg0, _arg12);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        IAutoFillManagerClient _arg02 = IAutoFillManagerClient.Stub.asInterface(data.readStrongBinder());
                        int _arg13 = data.readInt();
                        removeClient(_arg02, _arg13);
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg03 = data.readStrongBinder();
                        IBinder _arg14 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            AutofillId _arg24 = AutofillId.CREATOR.createFromParcel(data);
                            _arg2 = _arg24;
                        } else {
                            _arg2 = null;
                        }
                        if (data.readInt() != 0) {
                            Rect _arg34 = Rect.CREATOR.createFromParcel(data);
                            _arg3 = _arg34;
                        } else {
                            _arg3 = null;
                        }
                        if (data.readInt() != 0) {
                            AutofillValue _arg43 = AutofillValue.CREATOR.createFromParcel(data);
                            _arg4 = _arg43;
                        } else {
                            _arg4 = null;
                        }
                        int _arg5 = data.readInt();
                        boolean _arg6 = data.readInt() != 0;
                        int _arg7 = data.readInt();
                        ComponentName _arg8 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                        boolean _arg9 = data.readInt() != 0;
                        int _result2 = startSession(_arg03, _arg14, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        FillEventHistory _result3 = getFillEventHistory();
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg04 = data.readInt();
                        IBinder _arg15 = data.readStrongBinder();
                        IBinder _arg25 = data.readStrongBinder();
                        boolean restoreSession = restoreSession(_arg04, _arg15, _arg25);
                        reply.writeNoException();
                        reply.writeInt(restoreSession ? 1 : 0);
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg05 = data.readInt();
                        if (data.readInt() != 0) {
                            AutofillId _arg16 = AutofillId.CREATOR.createFromParcel(data);
                            _arg1 = _arg16;
                        } else {
                            _arg1 = null;
                        }
                        if (data.readInt() != 0) {
                            Rect _arg26 = Rect.CREATOR.createFromParcel(data);
                            _arg22 = _arg26;
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            AutofillValue _arg35 = AutofillValue.CREATOR.createFromParcel(data);
                            _arg32 = _arg35;
                        } else {
                            _arg32 = null;
                        }
                        int _arg44 = data.readInt();
                        int _arg52 = data.readInt();
                        int _arg62 = data.readInt();
                        updateSession(_arg05, _arg1, _arg22, _arg32, _arg44, _arg52, _arg62);
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg06 = data.readStrongBinder();
                        IBinder _arg17 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            AutofillId _arg27 = AutofillId.CREATOR.createFromParcel(data);
                            _arg23 = _arg27;
                        } else {
                            _arg23 = null;
                        }
                        if (data.readInt() != 0) {
                            Rect _arg36 = Rect.CREATOR.createFromParcel(data);
                            _arg33 = _arg36;
                        } else {
                            _arg33 = null;
                        }
                        if (data.readInt() != 0) {
                            AutofillValue _arg45 = AutofillValue.CREATOR.createFromParcel(data);
                            _arg42 = _arg45;
                        } else {
                            _arg42 = null;
                        }
                        int _arg53 = data.readInt();
                        boolean _arg63 = data.readInt() != 0;
                        int _arg72 = data.readInt();
                        ComponentName _arg82 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                        int _arg92 = data.readInt();
                        int _arg10 = data.readInt();
                        boolean _arg11 = data.readInt() != 0;
                        int _result4 = updateOrRestartSession(_arg06, _arg17, _arg23, _arg33, _arg42, _arg53, _arg63, _arg72, _arg82, _arg92, _arg10, _arg11);
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 8:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg07 = data.readInt();
                        List<AutofillId> _arg18 = data.createTypedArrayList(AutofillId.CREATOR);
                        int _arg28 = data.readInt();
                        setAutofillFailure(_arg07, _arg18, _arg28);
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg08 = data.readInt();
                        int _arg19 = data.readInt();
                        finishSession(_arg08, _arg19);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg09 = data.readInt();
                        int _arg110 = data.readInt();
                        cancelSession(_arg09, _arg110);
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(DESCRIPTOR);
                        Bundle _arg010 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                        Bundle _arg011 = _arg010;
                        int _arg111 = data.readInt();
                        int _arg29 = data.readInt();
                        int _arg37 = data.readInt();
                        setAuthenticationResult(_arg011, _arg111, _arg29, _arg37);
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg012 = data.readInt();
                        int _arg112 = data.readInt();
                        boolean _arg210 = data.readInt() != 0;
                        setHasCallback(_arg012, _arg112, _arg210);
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg013 = data.readInt();
                        disableOwnedAutofillServices(_arg013);
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg014 = data.readInt();
                        boolean isServiceSupported = isServiceSupported(_arg014);
                        reply.writeNoException();
                        reply.writeInt(isServiceSupported ? 1 : 0);
                        return true;
                    case 15:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg015 = data.readInt();
                        String _arg113 = data.readString();
                        boolean isServiceEnabled = isServiceEnabled(_arg015, _arg113);
                        reply.writeNoException();
                        reply.writeInt(isServiceEnabled ? 1 : 0);
                        return true;
                    case 16:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg016 = data.readInt();
                        IBinder _arg114 = data.readStrongBinder();
                        onPendingSaveUi(_arg016, _arg114);
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(DESCRIPTOR);
                        UserData _result5 = getUserData();
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 18:
                        data.enforceInterface(DESCRIPTOR);
                        String _result6 = getUserDataId();
                        reply.writeNoException();
                        reply.writeString(_result6);
                        return true;
                    case 19:
                        data.enforceInterface(DESCRIPTOR);
                        UserData _arg017 = data.readInt() != 0 ? UserData.CREATOR.createFromParcel(data) : null;
                        setUserData(_arg017);
                        reply.writeNoException();
                        return true;
                    case 20:
                        data.enforceInterface(DESCRIPTOR);
                        boolean isFieldClassificationEnabled = isFieldClassificationEnabled();
                        reply.writeNoException();
                        reply.writeInt(isFieldClassificationEnabled ? 1 : 0);
                        return true;
                    case 21:
                        data.enforceInterface(DESCRIPTOR);
                        ComponentName _result7 = getAutofillServiceComponentName();
                        reply.writeNoException();
                        if (_result7 != null) {
                            reply.writeInt(1);
                            _result7.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 22:
                        data.enforceInterface(DESCRIPTOR);
                        String[] _result8 = getAvailableFieldClassificationAlgorithms();
                        reply.writeNoException();
                        reply.writeStringArray(_result8);
                        return true;
                    case 23:
                        data.enforceInterface(DESCRIPTOR);
                        String _result9 = getDefaultFieldClassificationAlgorithm();
                        reply.writeNoException();
                        reply.writeString(_result9);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(DESCRIPTOR);
            return true;
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IAutoFillManager {
            private IBinder mRemote;

            public private protected Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized int addClient(IAutoFillManagerClient client, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized void removeClient(IAutoFillManagerClient client, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized int startSession(IBinder activityToken, IBinder appCallback, AutofillId autoFillId, Rect bounds, AutofillValue value, int userId, boolean hasCallback, int flags, ComponentName componentName, boolean compatMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(appCallback);
                    if (autoFillId != null) {
                        _data.writeInt(1);
                        autoFillId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bounds != null) {
                        _data.writeInt(1);
                        bounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (value != null) {
                        _data.writeInt(1);
                        value.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeInt(hasCallback ? 1 : 0);
                    _data.writeInt(flags);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(compatMode ? 1 : 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized FillEventHistory getFillEventHistory() throws RemoteException {
                FillEventHistory _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = FillEventHistory.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized boolean restoreSession(int sessionId, IBinder activityToken, IBinder appCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(appCallback);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized void updateSession(int sessionId, AutofillId id, Rect bounds, AutofillValue value, int action, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (id != null) {
                        _data.writeInt(1);
                        id.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bounds != null) {
                        _data.writeInt(1);
                        bounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (value != null) {
                        _data.writeInt(1);
                        value.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(action);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized int updateOrRestartSession(IBinder activityToken, IBinder appCallback, AutofillId autoFillId, Rect bounds, AutofillValue value, int userId, boolean hasCallback, int flags, ComponentName componentName, int sessionId, int action, boolean compatMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeStrongBinder(activityToken);
                        try {
                            _data.writeStrongBinder(appCallback);
                            if (autoFillId != null) {
                                _data.writeInt(1);
                                autoFillId.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (bounds != null) {
                                _data.writeInt(1);
                                bounds.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (value != null) {
                                _data.writeInt(1);
                                value.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th) {
                            th = th;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    _data.writeInt(userId);
                    try {
                        _data.writeInt(hasCallback ? 1 : 0);
                        try {
                            _data.writeInt(flags);
                            if (componentName != null) {
                                _data.writeInt(1);
                                componentName.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeInt(sessionId);
                            } catch (Throwable th4) {
                                th = th4;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(action);
                        try {
                            _data.writeInt(compatMode ? 1 : 0);
                            try {
                                this.mRemote.transact(7, _data, _reply, 0);
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            } catch (Throwable th7) {
                                th = th7;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th8) {
                            th = th8;
                        }
                    } catch (Throwable th9) {
                        th = th9;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th10) {
                    th = th10;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized void setAutofillFailure(int sessionId, List<AutofillId> ids, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeTypedList(ids);
                    _data.writeInt(userId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized void finishSession(int sessionId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(userId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized void cancelSession(int sessionId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(userId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized void setAuthenticationResult(Bundle data, int sessionId, int authenticationId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sessionId);
                    _data.writeInt(authenticationId);
                    _data.writeInt(userId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized void setHasCallback(int sessionId, int userId, boolean hasIt) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(userId);
                    _data.writeInt(hasIt ? 1 : 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized void disableOwnedAutofillServices(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized boolean isServiceSupported(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized boolean isServiceEnabled(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized void onPendingSaveUi(int operation, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(operation);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized UserData getUserData() throws RemoteException {
                UserData _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = UserData.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized String getUserDataId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized void setUserData(UserData userData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (userData != null) {
                        _data.writeInt(1);
                        userData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized boolean isFieldClassificationEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized ComponentName getAutofillServiceComponentName() throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized String[] getAvailableFieldClassificationAlgorithms() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.autofill.IAutoFillManager
            public synchronized String getDefaultFieldClassificationAlgorithm() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
