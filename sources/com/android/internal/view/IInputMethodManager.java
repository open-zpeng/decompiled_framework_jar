package com.android.internal.view;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.text.style.SuggestionSpan;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.inputmethod.IInputContentUriToken;
import com.android.internal.view.IInputContext;
import com.android.internal.view.IInputMethodClient;
import java.util.List;
/* loaded from: classes3.dex */
public interface IInputMethodManager extends IInterface {
    private protected void addClient(IInputMethodClient iInputMethodClient, IInputContext iInputContext, int i, int i2) throws RemoteException;

    synchronized void clearLastInputMethodWindowForTransition(IBinder iBinder) throws RemoteException;

    synchronized IInputContentUriToken createInputContentUriToken(IBinder iBinder, Uri uri, String str) throws RemoteException;

    synchronized void finishInput(IInputMethodClient iInputMethodClient) throws RemoteException;

    void forceHideSoftInputMethod() throws RemoteException;

    synchronized InputMethodSubtype getCurrentInputMethodSubtype() throws RemoteException;

    synchronized List<InputMethodInfo> getEnabledInputMethodList() throws RemoteException;

    synchronized List<InputMethodSubtype> getEnabledInputMethodSubtypeList(String str, boolean z) throws RemoteException;

    synchronized List<InputMethodInfo> getInputMethodList() throws RemoteException;

    synchronized int getInputMethodWindowVisibleHeight() throws RemoteException;

    boolean getInputShown() throws RemoteException;

    synchronized InputMethodSubtype getLastInputMethodSubtype() throws RemoteException;

    synchronized List getShortcutInputMethodsAndSubtypes() throws RemoteException;

    synchronized List<InputMethodInfo> getVrInputMethodList() throws RemoteException;

    synchronized void hideMySoftInput(IBinder iBinder, int i) throws RemoteException;

    synchronized boolean hideSoftInput(IInputMethodClient iInputMethodClient, int i, ResultReceiver resultReceiver) throws RemoteException;

    synchronized boolean isInputMethodPickerShownForTest() throws RemoteException;

    synchronized boolean notifySuggestionPicked(SuggestionSpan suggestionSpan, String str, int i) throws RemoteException;

    synchronized void notifyUserAction(int i) throws RemoteException;

    synchronized void registerSuggestionSpansForNotification(SuggestionSpan[] suggestionSpanArr) throws RemoteException;

    private protected void removeClient(IInputMethodClient iInputMethodClient) throws RemoteException;

    synchronized void reportFullscreenMode(IBinder iBinder, boolean z) throws RemoteException;

    synchronized void setAdditionalInputMethodSubtypes(String str, InputMethodSubtype[] inputMethodSubtypeArr) throws RemoteException;

    synchronized boolean setCurrentInputMethodSubtype(InputMethodSubtype inputMethodSubtype) throws RemoteException;

    synchronized void setImeWindowStatus(IBinder iBinder, IBinder iBinder2, int i, int i2) throws RemoteException;

    synchronized void setInputMethod(IBinder iBinder, String str) throws RemoteException;

    synchronized void setInputMethodAndSubtype(IBinder iBinder, String str, InputMethodSubtype inputMethodSubtype) throws RemoteException;

    synchronized boolean shouldOfferSwitchingToNextInputMethod(IBinder iBinder) throws RemoteException;

    synchronized void showInputMethodAndSubtypeEnablerFromClient(IInputMethodClient iInputMethodClient, String str) throws RemoteException;

    synchronized void showInputMethodPickerFromClient(IInputMethodClient iInputMethodClient, int i) throws RemoteException;

    synchronized void showMySoftInput(IBinder iBinder, int i) throws RemoteException;

    synchronized boolean showSoftInput(IInputMethodClient iInputMethodClient, int i, ResultReceiver resultReceiver) throws RemoteException;

    synchronized InputBindResult startInputOrWindowGainedFocus(int i, IInputMethodClient iInputMethodClient, IBinder iBinder, int i2, int i3, int i4, EditorInfo editorInfo, IInputContext iInputContext, int i5, int i6) throws RemoteException;

    synchronized boolean switchToNextInputMethod(IBinder iBinder, boolean z) throws RemoteException;

    synchronized boolean switchToPreviousInputMethod(IBinder iBinder) throws RemoteException;

    synchronized void updateStatusIcon(IBinder iBinder, String str, int i) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IInputMethodManager {
        private static final String DESCRIPTOR = "com.android.internal.view.IInputMethodManager";
        static final int TRANSACTION_addClient = 7;
        static final int TRANSACTION_clearLastInputMethodWindowForTransition = 31;
        static final int TRANSACTION_createInputContentUriToken = 32;
        static final int TRANSACTION_finishInput = 9;
        static final int TRANSACTION_forceHideSoftInputMethod = 36;
        static final int TRANSACTION_getCurrentInputMethodSubtype = 24;
        static final int TRANSACTION_getEnabledInputMethodList = 3;
        static final int TRANSACTION_getEnabledInputMethodSubtypeList = 4;
        static final int TRANSACTION_getInputMethodList = 1;
        static final int TRANSACTION_getInputMethodWindowVisibleHeight = 30;
        static final int TRANSACTION_getInputShown = 35;
        static final int TRANSACTION_getLastInputMethodSubtype = 5;
        static final int TRANSACTION_getShortcutInputMethodsAndSubtypes = 6;
        static final int TRANSACTION_getVrInputMethodList = 2;
        static final int TRANSACTION_hideMySoftInput = 18;
        static final int TRANSACTION_hideSoftInput = 11;
        static final int TRANSACTION_isInputMethodPickerShownForTest = 15;
        static final int TRANSACTION_notifySuggestionPicked = 23;
        static final int TRANSACTION_notifyUserAction = 34;
        static final int TRANSACTION_registerSuggestionSpansForNotification = 22;
        static final int TRANSACTION_removeClient = 8;
        static final int TRANSACTION_reportFullscreenMode = 33;
        static final int TRANSACTION_setAdditionalInputMethodSubtypes = 29;
        static final int TRANSACTION_setCurrentInputMethodSubtype = 25;
        static final int TRANSACTION_setImeWindowStatus = 21;
        static final int TRANSACTION_setInputMethod = 16;
        static final int TRANSACTION_setInputMethodAndSubtype = 17;
        static final int TRANSACTION_shouldOfferSwitchingToNextInputMethod = 28;
        static final int TRANSACTION_showInputMethodAndSubtypeEnablerFromClient = 14;
        static final int TRANSACTION_showInputMethodPickerFromClient = 13;
        static final int TRANSACTION_showMySoftInput = 19;
        static final int TRANSACTION_showSoftInput = 10;
        static final int TRANSACTION_startInputOrWindowGainedFocus = 12;
        static final int TRANSACTION_switchToNextInputMethod = 27;
        static final int TRANSACTION_switchToPreviousInputMethod = 26;
        static final int TRANSACTION_updateStatusIcon = 20;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IInputMethodManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IInputMethodManager)) {
                return (IInputMethodManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Uri _arg1;
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        List<InputMethodInfo> _result = getInputMethodList();
                        reply.writeNoException();
                        reply.writeTypedList(_result);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        List<InputMethodInfo> _result2 = getVrInputMethodList();
                        reply.writeNoException();
                        reply.writeTypedList(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        List<InputMethodInfo> _result3 = getEnabledInputMethodList();
                        reply.writeNoException();
                        reply.writeTypedList(_result3);
                        return true;
                    case 4:
                        boolean _arg12 = false;
                        data.enforceInterface(DESCRIPTOR);
                        String _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = true;
                        }
                        List<InputMethodSubtype> _result4 = getEnabledInputMethodSubtypeList(_arg0, _arg12);
                        reply.writeNoException();
                        reply.writeTypedList(_result4);
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        InputMethodSubtype _result5 = getLastInputMethodSubtype();
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        List _result6 = getShortcutInputMethodsAndSubtypes();
                        reply.writeNoException();
                        reply.writeList(_result6);
                        return true;
                    case 7:
                        data.enforceInterface(DESCRIPTOR);
                        IInputMethodClient _arg02 = IInputMethodClient.Stub.asInterface(data.readStrongBinder());
                        IInputContext _arg13 = IInputContext.Stub.asInterface(data.readStrongBinder());
                        int _arg2 = data.readInt();
                        int _arg3 = data.readInt();
                        addClient(_arg02, _arg13, _arg2, _arg3);
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(DESCRIPTOR);
                        IInputMethodClient _arg03 = IInputMethodClient.Stub.asInterface(data.readStrongBinder());
                        removeClient(_arg03);
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(DESCRIPTOR);
                        IInputMethodClient _arg04 = IInputMethodClient.Stub.asInterface(data.readStrongBinder());
                        finishInput(_arg04);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(DESCRIPTOR);
                        IInputMethodClient _arg05 = IInputMethodClient.Stub.asInterface(data.readStrongBinder());
                        int _arg14 = data.readInt();
                        ResultReceiver _arg22 = data.readInt() != 0 ? ResultReceiver.CREATOR.createFromParcel(data) : null;
                        boolean showSoftInput = showSoftInput(_arg05, _arg14, _arg22);
                        reply.writeNoException();
                        reply.writeInt(showSoftInput ? 1 : 0);
                        return true;
                    case 11:
                        data.enforceInterface(DESCRIPTOR);
                        IInputMethodClient _arg06 = IInputMethodClient.Stub.asInterface(data.readStrongBinder());
                        int _arg15 = data.readInt();
                        ResultReceiver _arg23 = data.readInt() != 0 ? ResultReceiver.CREATOR.createFromParcel(data) : null;
                        boolean hideSoftInput = hideSoftInput(_arg06, _arg15, _arg23);
                        reply.writeNoException();
                        reply.writeInt(hideSoftInput ? 1 : 0);
                        return true;
                    case 12:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg07 = data.readInt();
                        IInputMethodClient _arg16 = IInputMethodClient.Stub.asInterface(data.readStrongBinder());
                        IBinder _arg24 = data.readStrongBinder();
                        int _arg32 = data.readInt();
                        int _arg4 = data.readInt();
                        int _arg5 = data.readInt();
                        EditorInfo _arg6 = data.readInt() != 0 ? EditorInfo.CREATOR.createFromParcel(data) : null;
                        IInputContext _arg7 = IInputContext.Stub.asInterface(data.readStrongBinder());
                        int _arg8 = data.readInt();
                        int _arg9 = data.readInt();
                        InputBindResult _result7 = startInputOrWindowGainedFocus(_arg07, _arg16, _arg24, _arg32, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9);
                        reply.writeNoException();
                        if (_result7 != null) {
                            reply.writeInt(1);
                            _result7.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 13:
                        data.enforceInterface(DESCRIPTOR);
                        IInputMethodClient _arg08 = IInputMethodClient.Stub.asInterface(data.readStrongBinder());
                        int _arg17 = data.readInt();
                        showInputMethodPickerFromClient(_arg08, _arg17);
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(DESCRIPTOR);
                        IInputMethodClient _arg09 = IInputMethodClient.Stub.asInterface(data.readStrongBinder());
                        String _arg18 = data.readString();
                        showInputMethodAndSubtypeEnablerFromClient(_arg09, _arg18);
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(DESCRIPTOR);
                        boolean isInputMethodPickerShownForTest = isInputMethodPickerShownForTest();
                        reply.writeNoException();
                        reply.writeInt(isInputMethodPickerShownForTest ? 1 : 0);
                        return true;
                    case 16:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg010 = data.readStrongBinder();
                        String _arg19 = data.readString();
                        setInputMethod(_arg010, _arg19);
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg011 = data.readStrongBinder();
                        String _arg110 = data.readString();
                        InputMethodSubtype _arg25 = data.readInt() != 0 ? InputMethodSubtype.CREATOR.createFromParcel(data) : null;
                        setInputMethodAndSubtype(_arg011, _arg110, _arg25);
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg012 = data.readStrongBinder();
                        int _arg111 = data.readInt();
                        hideMySoftInput(_arg012, _arg111);
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg013 = data.readStrongBinder();
                        int _arg112 = data.readInt();
                        showMySoftInput(_arg013, _arg112);
                        reply.writeNoException();
                        return true;
                    case 20:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg014 = data.readStrongBinder();
                        String _arg113 = data.readString();
                        int _arg26 = data.readInt();
                        updateStatusIcon(_arg014, _arg113, _arg26);
                        reply.writeNoException();
                        return true;
                    case 21:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg015 = data.readStrongBinder();
                        IBinder _arg114 = data.readStrongBinder();
                        int _arg27 = data.readInt();
                        int _arg33 = data.readInt();
                        setImeWindowStatus(_arg015, _arg114, _arg27, _arg33);
                        reply.writeNoException();
                        return true;
                    case 22:
                        data.enforceInterface(DESCRIPTOR);
                        SuggestionSpan[] _arg016 = (SuggestionSpan[]) data.createTypedArray(SuggestionSpan.CREATOR);
                        registerSuggestionSpansForNotification(_arg016);
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(DESCRIPTOR);
                        SuggestionSpan _arg017 = data.readInt() != 0 ? SuggestionSpan.CREATOR.createFromParcel(data) : null;
                        String _arg115 = data.readString();
                        int _arg28 = data.readInt();
                        boolean notifySuggestionPicked = notifySuggestionPicked(_arg017, _arg115, _arg28);
                        reply.writeNoException();
                        reply.writeInt(notifySuggestionPicked ? 1 : 0);
                        return true;
                    case 24:
                        data.enforceInterface(DESCRIPTOR);
                        InputMethodSubtype _result8 = getCurrentInputMethodSubtype();
                        reply.writeNoException();
                        if (_result8 != null) {
                            reply.writeInt(1);
                            _result8.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 25:
                        data.enforceInterface(DESCRIPTOR);
                        InputMethodSubtype _arg018 = data.readInt() != 0 ? InputMethodSubtype.CREATOR.createFromParcel(data) : null;
                        boolean currentInputMethodSubtype = setCurrentInputMethodSubtype(_arg018);
                        reply.writeNoException();
                        reply.writeInt(currentInputMethodSubtype ? 1 : 0);
                        return true;
                    case 26:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg019 = data.readStrongBinder();
                        boolean switchToPreviousInputMethod = switchToPreviousInputMethod(_arg019);
                        reply.writeNoException();
                        reply.writeInt(switchToPreviousInputMethod ? 1 : 0);
                        return true;
                    case 27:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg020 = data.readStrongBinder();
                        boolean _arg116 = data.readInt() != 0;
                        boolean switchToNextInputMethod = switchToNextInputMethod(_arg020, _arg116);
                        reply.writeNoException();
                        reply.writeInt(switchToNextInputMethod ? 1 : 0);
                        return true;
                    case 28:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg021 = data.readStrongBinder();
                        boolean shouldOfferSwitchingToNextInputMethod = shouldOfferSwitchingToNextInputMethod(_arg021);
                        reply.writeNoException();
                        reply.writeInt(shouldOfferSwitchingToNextInputMethod ? 1 : 0);
                        return true;
                    case 29:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg022 = data.readString();
                        InputMethodSubtype[] _arg117 = (InputMethodSubtype[]) data.createTypedArray(InputMethodSubtype.CREATOR);
                        setAdditionalInputMethodSubtypes(_arg022, _arg117);
                        reply.writeNoException();
                        return true;
                    case 30:
                        data.enforceInterface(DESCRIPTOR);
                        int _result9 = getInputMethodWindowVisibleHeight();
                        reply.writeNoException();
                        reply.writeInt(_result9);
                        return true;
                    case 31:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg023 = data.readStrongBinder();
                        clearLastInputMethodWindowForTransition(_arg023);
                        reply.writeNoException();
                        return true;
                    case 32:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg024 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg1 = Uri.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        String _arg29 = data.readString();
                        IInputContentUriToken _result10 = createInputContentUriToken(_arg024, _arg1, _arg29);
                        reply.writeNoException();
                        reply.writeStrongBinder(_result10 != null ? _result10.asBinder() : null);
                        return true;
                    case 33:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg025 = data.readStrongBinder();
                        boolean _arg118 = data.readInt() != 0;
                        reportFullscreenMode(_arg025, _arg118);
                        reply.writeNoException();
                        return true;
                    case 34:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg026 = data.readInt();
                        notifyUserAction(_arg026);
                        return true;
                    case 35:
                        data.enforceInterface(DESCRIPTOR);
                        boolean inputShown = getInputShown();
                        reply.writeNoException();
                        reply.writeInt(inputShown ? 1 : 0);
                        return true;
                    case 36:
                        data.enforceInterface(DESCRIPTOR);
                        forceHideSoftInputMethod();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(DESCRIPTOR);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IInputMethodManager {
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

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized List<InputMethodInfo> getInputMethodList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<InputMethodInfo> _result = _reply.createTypedArrayList(InputMethodInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized List<InputMethodInfo> getVrInputMethodList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<InputMethodInfo> _result = _reply.createTypedArrayList(InputMethodInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected List<InputMethodInfo> getEnabledInputMethodList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    List<InputMethodInfo> _result = _reply.createTypedArrayList(InputMethodInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized List<InputMethodSubtype> getEnabledInputMethodSubtypeList(String imiId, boolean allowsImplicitlySelectedSubtypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(imiId);
                    _data.writeInt(allowsImplicitlySelectedSubtypes ? 1 : 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    List<InputMethodSubtype> _result = _reply.createTypedArrayList(InputMethodSubtype.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized InputMethodSubtype getLastInputMethodSubtype() throws RemoteException {
                InputMethodSubtype _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = InputMethodSubtype.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized List getShortcutInputMethodsAndSubtypes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void addClient(IInputMethodClient client, IInputContext inputContext, int uid, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeStrongBinder(inputContext != null ? inputContext.asBinder() : null);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void removeClient(IInputMethodClient client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void finishInput(IInputMethodClient client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized boolean showSoftInput(IInputMethodClient client, int flags, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(flags);
                    if (resultReceiver != null) {
                        _data.writeInt(1);
                        resultReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized boolean hideSoftInput(IInputMethodClient client, int flags, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(flags);
                    if (resultReceiver != null) {
                        _data.writeInt(1);
                        resultReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized InputBindResult startInputOrWindowGainedFocus(int startInputReason, IInputMethodClient client, IBinder windowToken, int controlFlags, int softInputMode, int windowFlags, EditorInfo attribute, IInputContext inputContext, int missingMethodFlags, int unverifiedTargetSdkVersion) throws RemoteException {
                InputBindResult _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(startInputReason);
                        _data.writeStrongBinder(client != null ? client.asBinder() : null);
                        try {
                            _data.writeStrongBinder(windowToken);
                            try {
                                _data.writeInt(controlFlags);
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
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(softInputMode);
                        try {
                            _data.writeInt(windowFlags);
                            if (attribute != null) {
                                _data.writeInt(1);
                                attribute.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeStrongBinder(inputContext != null ? inputContext.asBinder() : null);
                            try {
                                _data.writeInt(missingMethodFlags);
                                try {
                                    _data.writeInt(unverifiedTargetSdkVersion);
                                } catch (Throwable th4) {
                                    th = th4;
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
                    } catch (Throwable th7) {
                        th = th7;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                }
                try {
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        InputBindResult _result2 = InputBindResult.CREATOR.createFromParcel(_reply);
                        _result = _result2;
                    } else {
                        _result = null;
                    }
                    InputBindResult _result3 = _result;
                    _reply.recycle();
                    _data.recycle();
                    return _result3;
                } catch (Throwable th9) {
                    th = th9;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void showInputMethodPickerFromClient(IInputMethodClient client, int auxiliarySubtypeMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(auxiliarySubtypeMode);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void showInputMethodAndSubtypeEnablerFromClient(IInputMethodClient client, String topId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeString(topId);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized boolean isInputMethodPickerShownForTest() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void setInputMethod(IBinder token, String id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(id);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void setInputMethodAndSubtype(IBinder token, String id, InputMethodSubtype subtype) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(id);
                    if (subtype != null) {
                        _data.writeInt(1);
                        subtype.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void hideMySoftInput(IBinder token, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(flags);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void showMySoftInput(IBinder token, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(flags);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void updateStatusIcon(IBinder token, String packageName, int iconId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(packageName);
                    _data.writeInt(iconId);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void setImeWindowStatus(IBinder token, IBinder startInputToken, int vis, int backDisposition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeStrongBinder(startInputToken);
                    _data.writeInt(vis);
                    _data.writeInt(backDisposition);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void registerSuggestionSpansForNotification(SuggestionSpan[] spans) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(spans, 0);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized boolean notifySuggestionPicked(SuggestionSpan span, String originalString, int index) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (span != null) {
                        _data.writeInt(1);
                        span.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(originalString);
                    _data.writeInt(index);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized InputMethodSubtype getCurrentInputMethodSubtype() throws RemoteException {
                InputMethodSubtype _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = InputMethodSubtype.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized boolean setCurrentInputMethodSubtype(InputMethodSubtype subtype) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (subtype != null) {
                        _data.writeInt(1);
                        subtype.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized boolean switchToPreviousInputMethod(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized boolean switchToNextInputMethod(IBinder token, boolean onlyCurrentIme) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(onlyCurrentIme ? 1 : 0);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized boolean shouldOfferSwitchingToNextInputMethod(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void setAdditionalInputMethodSubtypes(String id, InputMethodSubtype[] subtypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    _data.writeTypedArray(subtypes, 0);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized int getInputMethodWindowVisibleHeight() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void clearLastInputMethodWindowForTransition(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized IInputContentUriToken createInputContentUriToken(IBinder token, Uri contentUri, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (contentUri != null) {
                        _data.writeInt(1);
                        contentUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    IInputContentUriToken _result = IInputContentUriToken.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void reportFullscreenMode(IBinder token, boolean fullscreen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(fullscreen ? 1 : 0);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public synchronized void notifyUserAction(int sequenceNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sequenceNumber);
                    this.mRemote.transact(34, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public boolean getInputShown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethodManager
            public void forceHideSoftInputMethod() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
