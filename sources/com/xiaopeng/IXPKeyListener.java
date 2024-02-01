package com.xiaopeng;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.KeyEvent;
/* loaded from: classes3.dex */
public interface IXPKeyListener extends IInterface {
    int notify(KeyEvent keyEvent, String str) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IXPKeyListener {
        private static final String DESCRIPTOR = "com.xiaopeng.IXPKeyListener";
        static final int TRANSACTION_notify = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IXPKeyListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IXPKeyListener)) {
                return (IXPKeyListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            KeyEvent _arg0;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = KeyEvent.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            int _result = notify(_arg0, _arg1);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IXPKeyListener {
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

            @Override // com.xiaopeng.IXPKeyListener
            public int notify(KeyEvent event, String extra) throws RemoteException {
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
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
