package android.service.carrier;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface ICarrierMessagingCallback extends IInterface {
    synchronized void onDownloadMmsComplete(int i) throws RemoteException;

    synchronized void onFilterComplete(int i) throws RemoteException;

    synchronized void onSendMmsComplete(int i, byte[] bArr) throws RemoteException;

    synchronized void onSendMultipartSmsComplete(int i, int[] iArr) throws RemoteException;

    synchronized void onSendSmsComplete(int i, int i2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ICarrierMessagingCallback {
        private static final String DESCRIPTOR = "android.service.carrier.ICarrierMessagingCallback";
        static final int TRANSACTION_onDownloadMmsComplete = 5;
        static final int TRANSACTION_onFilterComplete = 1;
        static final int TRANSACTION_onSendMmsComplete = 4;
        static final int TRANSACTION_onSendMultipartSmsComplete = 3;
        static final int TRANSACTION_onSendSmsComplete = 2;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ICarrierMessagingCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ICarrierMessagingCallback)) {
                return (ICarrierMessagingCallback) iin;
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
                    int _arg0 = data.readInt();
                    onFilterComplete(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    int _arg1 = data.readInt();
                    onSendSmsComplete(_arg02, _arg1);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    int[] _arg12 = data.createIntArray();
                    onSendMultipartSmsComplete(_arg03, _arg12);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    byte[] _arg13 = data.createByteArray();
                    onSendMmsComplete(_arg04, _arg13);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    onDownloadMmsComplete(_arg05);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ICarrierMessagingCallback {
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

            @Override // android.service.carrier.ICarrierMessagingCallback
            public synchronized void onFilterComplete(int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.carrier.ICarrierMessagingCallback
            public synchronized void onSendSmsComplete(int result, int messageRef) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeInt(messageRef);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.carrier.ICarrierMessagingCallback
            public synchronized void onSendMultipartSmsComplete(int result, int[] messageRefs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeIntArray(messageRefs);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.carrier.ICarrierMessagingCallback
            public synchronized void onSendMmsComplete(int result, byte[] sendConfPdu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeByteArray(sendConfPdu);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.carrier.ICarrierMessagingCallback
            public synchronized void onDownloadMmsComplete(int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
