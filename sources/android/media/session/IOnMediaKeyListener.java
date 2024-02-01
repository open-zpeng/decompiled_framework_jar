package android.media.session;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.view.KeyEvent;
/* loaded from: classes.dex */
public interface IOnMediaKeyListener extends IInterface {
    synchronized void onMediaKey(KeyEvent keyEvent, ResultReceiver resultReceiver) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOnMediaKeyListener {
        private static final String DESCRIPTOR = "android.media.session.IOnMediaKeyListener";
        static final int TRANSACTION_onMediaKey = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IOnMediaKeyListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IOnMediaKeyListener)) {
                return (IOnMediaKeyListener) iin;
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
            ResultReceiver _arg1 = data.readInt() != 0 ? ResultReceiver.CREATOR.createFromParcel(data) : null;
            onMediaKey(_arg0, _arg1);
            return true;
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOnMediaKeyListener {
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

            @Override // android.media.session.IOnMediaKeyListener
            public synchronized void onMediaKey(KeyEvent event, ResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
