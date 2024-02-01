package com.xiaopeng.input;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.MotionEvent;

/* loaded from: classes3.dex */
public interface IInputEventListener extends IInterface {
    void onKeyEvent(KeyEvent keyEvent, String str) throws RemoteException;

    void onTouchEvent(MotionEvent motionEvent, String str) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IInputEventListener {
        @Override // com.xiaopeng.input.IInputEventListener
        public void onKeyEvent(KeyEvent event, String extra) throws RemoteException {
        }

        @Override // com.xiaopeng.input.IInputEventListener
        public void onTouchEvent(MotionEvent event, String extra) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IInputEventListener {
        private static final String DESCRIPTOR = "com.xiaopeng.input.IInputEventListener";
        static final int TRANSACTION_onKeyEvent = 1;
        static final int TRANSACTION_onTouchEvent = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInputEventListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IInputEventListener)) {
                return (IInputEventListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                if (transactionCode == 2) {
                    return "onTouchEvent";
                }
                return null;
            }
            return "onKeyEvent";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            KeyEvent _arg0;
            MotionEvent _arg02;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg0 = KeyEvent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                String _arg1 = data.readString();
                onKeyEvent(_arg0, _arg1);
                reply.writeNoException();
                return true;
            } else if (code != 2) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg02 = MotionEvent.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                String _arg12 = data.readString();
                onTouchEvent(_arg02, _arg12);
                reply.writeNoException();
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IInputEventListener {
            public static IInputEventListener sDefaultImpl;
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

            @Override // com.xiaopeng.input.IInputEventListener
            public void onKeyEvent(KeyEvent event, String extra) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(extra);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onKeyEvent(event, extra);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.input.IInputEventListener
            public void onTouchEvent(MotionEvent event, String extra) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(extra);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTouchEvent(event, extra);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IInputEventListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IInputEventListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
