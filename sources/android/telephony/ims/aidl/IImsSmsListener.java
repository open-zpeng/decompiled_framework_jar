package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IImsSmsListener extends IInterface {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onSendSmsResult(int i, int i2, int i3, int i4) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onSmsReceived(int i, String str, byte[] bArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onSmsStatusReportReceived(int i, int i2, String str, byte[] bArr) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IImsSmsListener {
        public protected static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsSmsListener";
        public private protected static final int TRANSACTION_onSendSmsResult = 1;
        public private protected static final int TRANSACTION_onSmsReceived = 3;
        public private protected static final int TRANSACTION_onSmsStatusReportReceived = 2;

        private protected synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized IImsSmsListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IImsSmsListener)) {
                return (IImsSmsListener) iin;
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
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    onSendSmsResult(_arg0, _arg1, _arg2, _arg3);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    int _arg12 = data.readInt();
                    String _arg22 = data.readString();
                    byte[] _arg32 = data.createByteArray();
                    onSmsStatusReportReceived(_arg02, _arg12, _arg22, _arg32);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    String _arg13 = data.readString();
                    byte[] _arg23 = data.createByteArray();
                    onSmsReceived(_arg03, _arg13, _arg23);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IImsSmsListener {
            public protected IBinder mRemote;

            public private protected synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            private protected synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            private protected synchronized void onSendSmsResult(int token, int messageRef, int status, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(messageRef);
                    _data.writeInt(status);
                    _data.writeInt(reason);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onSmsStatusReportReceived(int token, int messageRef, String format, byte[] pdu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(messageRef);
                    _data.writeString(format);
                    _data.writeByteArray(pdu);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onSmsReceived(int token, String format, byte[] pdu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeString(format);
                    _data.writeByteArray(pdu);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
