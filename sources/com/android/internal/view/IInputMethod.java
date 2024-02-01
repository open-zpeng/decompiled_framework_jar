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
import com.android.internal.view.IInputContext;
import com.android.internal.view.IInputMethodSession;
import com.android.internal.view.IInputSessionCallback;
/* loaded from: classes3.dex */
public interface IInputMethod extends IInterface {
    private protected void attachToken(IBinder iBinder) throws RemoteException;

    private protected void bindInput(InputBinding inputBinding) throws RemoteException;

    synchronized void changeInputMethodSubtype(InputMethodSubtype inputMethodSubtype) throws RemoteException;

    synchronized void createSession(InputChannel inputChannel, IInputSessionCallback iInputSessionCallback) throws RemoteException;

    private protected void hideSoftInput(int i, ResultReceiver resultReceiver) throws RemoteException;

    synchronized void revokeSession(IInputMethodSession iInputMethodSession) throws RemoteException;

    private protected void setSessionEnabled(IInputMethodSession iInputMethodSession, boolean z) throws RemoteException;

    private protected void showSoftInput(int i, ResultReceiver resultReceiver) throws RemoteException;

    synchronized void startInput(IBinder iBinder, IInputContext iInputContext, int i, EditorInfo editorInfo, boolean z) throws RemoteException;

    private protected void unbindInput() throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IInputMethod {
        private static final String DESCRIPTOR = "com.android.internal.view.IInputMethod";
        static final int TRANSACTION_attachToken = 1;
        static final int TRANSACTION_bindInput = 2;
        static final int TRANSACTION_changeInputMethodSubtype = 10;
        static final int TRANSACTION_createSession = 5;
        static final int TRANSACTION_hideSoftInput = 9;
        static final int TRANSACTION_revokeSession = 7;
        static final int TRANSACTION_setSessionEnabled = 6;
        static final int TRANSACTION_showSoftInput = 8;
        static final int TRANSACTION_startInput = 4;
        static final int TRANSACTION_unbindInput = 3;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        private protected static IInputMethod asInterface(IBinder obj) {
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

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0 = data.readStrongBinder();
                    attachToken(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    InputBinding _arg02 = data.readInt() != 0 ? InputBinding.CREATOR.createFromParcel(data) : null;
                    bindInput(_arg02);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    unbindInput();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg03 = data.readStrongBinder();
                    IInputContext _arg1 = IInputContext.Stub.asInterface(data.readStrongBinder());
                    int _arg2 = data.readInt();
                    EditorInfo _arg3 = data.readInt() != 0 ? EditorInfo.CREATOR.createFromParcel(data) : null;
                    boolean _arg4 = data.readInt() != 0;
                    startInput(_arg03, _arg1, _arg2, _arg3, _arg4);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    InputChannel _arg04 = data.readInt() != 0 ? InputChannel.CREATOR.createFromParcel(data) : null;
                    InputChannel _arg05 = _arg04;
                    IInputSessionCallback _arg12 = IInputSessionCallback.Stub.asInterface(data.readStrongBinder());
                    createSession(_arg05, _arg12);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IInputMethodSession _arg06 = IInputMethodSession.Stub.asInterface(data.readStrongBinder());
                    boolean _arg13 = data.readInt() != 0;
                    setSessionEnabled(_arg06, _arg13);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IInputMethodSession _arg07 = IInputMethodSession.Stub.asInterface(data.readStrongBinder());
                    revokeSession(_arg07);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    ResultReceiver _arg14 = data.readInt() != 0 ? ResultReceiver.CREATOR.createFromParcel(data) : null;
                    showSoftInput(_arg08, _arg14);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    ResultReceiver _arg15 = data.readInt() != 0 ? ResultReceiver.CREATOR.createFromParcel(data) : null;
                    hideSoftInput(_arg09, _arg15);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    InputMethodSubtype _arg010 = data.readInt() != 0 ? InputMethodSubtype.CREATOR.createFromParcel(data) : null;
                    changeInputMethodSubtype(_arg010);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IInputMethod {
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

            public synchronized void attachToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void bindInput(InputBinding binding) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (binding != null) {
                        _data.writeInt(1);
                        binding.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void unbindInput() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public synchronized void startInput(IBinder startInputToken, IInputContext inputContext, int missingMethods, EditorInfo attribute, boolean restarting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(startInputToken);
                    _data.writeStrongBinder(inputContext != null ? inputContext.asBinder() : null);
                    _data.writeInt(missingMethods);
                    if (attribute != null) {
                        _data.writeInt(1);
                        attribute.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(restarting ? 1 : 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public synchronized void createSession(InputChannel channel, IInputSessionCallback callback) throws RemoteException {
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
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void setSessionEnabled(IInputMethodSession session, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public synchronized void revokeSession(IInputMethodSession session) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void showSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
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
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void hideSoftInput(int flags, ResultReceiver resultReceiver) throws RemoteException {
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
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.view.IInputMethod
            public synchronized void changeInputMethodSubtype(InputMethodSubtype subtype) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (subtype != null) {
                        _data.writeInt(1);
                        subtype.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
