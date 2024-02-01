package com.android.internal.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.view.InputChannel;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.inputmethod.IInputMethodPrivilegedOperations;
import com.android.internal.view.IInputContext;
import com.android.internal.view.IInputMethodSession;
import com.android.internal.view.IInputSessionCallback;

/* loaded from: classes3.dex */
public interface IInputMethod extends IInterface {
    void bindInput(InputBinding inputBinding) throws RemoteException;

    void changeInputMethodSubtype(InputMethodSubtype inputMethodSubtype) throws RemoteException;

    void createSession(InputChannel inputChannel, IInputSessionCallback iInputSessionCallback) throws RemoteException;

    void hideSoftInput(int i, ResultReceiver resultReceiver) throws RemoteException;

    void initializeInternal(IBinder iBinder, int i, IInputMethodPrivilegedOperations iInputMethodPrivilegedOperations) throws RemoteException;

    void revokeSession(IInputMethodSession iInputMethodSession) throws RemoteException;

    void setSessionEnabled(IInputMethodSession iInputMethodSession, boolean z) throws RemoteException;

    void showSoftInput(int i, ResultReceiver resultReceiver) throws RemoteException;

    void startInput(IBinder iBinder, IInputContext iInputContext, int i, EditorInfo editorInfo, boolean z, boolean z2) throws RemoteException;

    void unbindInput() throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IInputMethod {
        @Override // com.android.internal.view.IInputMethod
        public void initializeInternal(IBinder token, int displayId, IInputMethodPrivilegedOperations privOps) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputMethod
        public void bindInput(InputBinding binding) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputMethod
        public void unbindInput() throws RemoteException {
        }

        @Override // com.android.internal.view.IInputMethod
        public void startInput(IBinder startInputToken, IInputContext inputContext, int missingMethods, EditorInfo attribute, boolean restarting, boolean preRenderImeViews) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputMethod
        public void createSession(InputChannel channel, IInputSessionCallback callback) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputMethod
        public void setSessionEnabled(IInputMethodSession session, boolean enabled) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputMethod
        public void revokeSession(IInputMethodSession session) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputMethod
        public void showSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputMethod
        public void hideSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
        }

        @Override // com.android.internal.view.IInputMethod
        public void changeInputMethodSubtype(InputMethodSubtype subtype) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IInputMethod {
        private static final String DESCRIPTOR = "com.android.internal.view.IInputMethod";
        static final int TRANSACTION_bindInput = 2;
        static final int TRANSACTION_changeInputMethodSubtype = 10;
        static final int TRANSACTION_createSession = 5;
        static final int TRANSACTION_hideSoftInput = 9;
        static final int TRANSACTION_initializeInternal = 1;
        static final int TRANSACTION_revokeSession = 7;
        static final int TRANSACTION_setSessionEnabled = 6;
        static final int TRANSACTION_showSoftInput = 8;
        static final int TRANSACTION_startInput = 4;
        static final int TRANSACTION_unbindInput = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInputMethod asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IInputMethod)) {
                return (IInputMethod) iin;
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
                    return "initializeInternal";
                case 2:
                    return "bindInput";
                case 3:
                    return "unbindInput";
                case 4:
                    return "startInput";
                case 5:
                    return "createSession";
                case 6:
                    return "setSessionEnabled";
                case 7:
                    return "revokeSession";
                case 8:
                    return "showSoftInput";
                case 9:
                    return "hideSoftInput";
                case 10:
                    return "changeInputMethodSubtype";
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
            InputBinding _arg0;
            EditorInfo _arg3;
            InputChannel _arg02;
            ResultReceiver _arg1;
            ResultReceiver _arg12;
            InputMethodSubtype _arg03;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg04 = data.readStrongBinder();
                    int _arg13 = data.readInt();
                    IInputMethodPrivilegedOperations _arg2 = IInputMethodPrivilegedOperations.Stub.asInterface(data.readStrongBinder());
                    initializeInternal(_arg04, _arg13, _arg2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = InputBinding.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    bindInput(_arg0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    unbindInput();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg05 = data.readStrongBinder();
                    IInputContext _arg14 = IInputContext.Stub.asInterface(data.readStrongBinder());
                    int _arg22 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = EditorInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    boolean _arg4 = data.readInt() != 0;
                    boolean _arg5 = data.readInt() != 0;
                    startInput(_arg05, _arg14, _arg22, _arg3, _arg4, _arg5);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = InputChannel.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    IInputSessionCallback _arg15 = IInputSessionCallback.Stub.asInterface(data.readStrongBinder());
                    createSession(_arg02, _arg15);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IInputMethodSession _arg06 = IInputMethodSession.Stub.asInterface(data.readStrongBinder());
                    boolean _arg16 = data.readInt() != 0;
                    setSessionEnabled(_arg06, _arg16);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IInputMethodSession _arg07 = IInputMethodSession.Stub.asInterface(data.readStrongBinder());
                    revokeSession(_arg07);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = ResultReceiver.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    showSoftInput(_arg08, _arg1);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = ResultReceiver.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    hideSoftInput(_arg09, _arg12);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = InputMethodSubtype.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    changeInputMethodSubtype(_arg03);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IInputMethod {
            public static IInputMethod sDefaultImpl;
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

            @Override // com.android.internal.view.IInputMethod
            public void initializeInternal(IBinder token, int displayId, IInputMethodPrivilegedOperations privOps) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(displayId);
                    _data.writeStrongBinder(privOps != null ? privOps.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().initializeInternal(token, displayId, privOps);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public void bindInput(InputBinding binding) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (binding != null) {
                        _data.writeInt(1);
                        binding.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().bindInput(binding);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public void unbindInput() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unbindInput();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public void startInput(IBinder startInputToken, IInputContext inputContext, int missingMethods, EditorInfo attribute, boolean restarting, boolean preRenderImeViews) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(startInputToken);
                    _data.writeStrongBinder(inputContext != null ? inputContext.asBinder() : null);
                    try {
                        _data.writeInt(missingMethods);
                        if (attribute != null) {
                            _data.writeInt(1);
                            attribute.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(restarting ? 1 : 0);
                        _data.writeInt(preRenderImeViews ? 1 : 0);
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _data.recycle();
                    throw th;
                }
                try {
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startInput(startInputToken, inputContext, missingMethods, attribute, restarting, preRenderImeViews);
                        _data.recycle();
                        return;
                    }
                    _data.recycle();
                } catch (Throwable th4) {
                    th = th4;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public void createSession(InputChannel channel, IInputSessionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (channel != null) {
                        _data.writeInt(1);
                        channel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().createSession(channel, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public void setSessionEnabled(IInputMethodSession session, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSessionEnabled(session, enabled);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public void revokeSession(IInputMethodSession session) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().revokeSession(session);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public void showSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    if (resultReceiver != null) {
                        _data.writeInt(1);
                        resultReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().showSoftInput(flags, resultReceiver);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public void hideSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    if (resultReceiver != null) {
                        _data.writeInt(1);
                        resultReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().hideSoftInput(flags, resultReceiver);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public void changeInputMethodSubtype(InputMethodSubtype subtype) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (subtype != null) {
                        _data.writeInt(1);
                        subtype.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().changeInputMethodSubtype(subtype);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IInputMethod impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IInputMethod getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
