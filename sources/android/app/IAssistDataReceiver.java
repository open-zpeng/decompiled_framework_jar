package android.app;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IAssistDataReceiver extends IInterface {
    private protected void onHandleAssistData(Bundle bundle) throws RemoteException;

    private protected void onHandleAssistScreenshot(Bitmap bitmap) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IAssistDataReceiver {
        private static final String DESCRIPTOR = "android.app.IAssistDataReceiver";
        static final int TRANSACTION_onHandleAssistData = 1;
        static final int TRANSACTION_onHandleAssistScreenshot = 2;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IAssistDataReceiver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAssistDataReceiver)) {
                return (IAssistDataReceiver) iin;
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
                    Bundle _arg0 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onHandleAssistData(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    Bitmap _arg02 = data.readInt() != 0 ? Bitmap.CREATOR.createFromParcel(data) : null;
                    onHandleAssistScreenshot(_arg02);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IAssistDataReceiver {
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

            public synchronized void onHandleAssistData(Bundle resultData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (resultData != null) {
                        _data.writeInt(1);
                        resultData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void onHandleAssistScreenshot(Bitmap screenshot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (screenshot != null) {
                        _data.writeInt(1);
                        screenshot.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
