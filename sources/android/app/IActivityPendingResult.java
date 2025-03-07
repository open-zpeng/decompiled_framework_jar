package android.app;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IActivityPendingResult extends IInterface {
    boolean sendResult(int i, String str, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IActivityPendingResult {
        @Override // android.app.IActivityPendingResult
        public boolean sendResult(int code, String data, Bundle ex) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IActivityPendingResult {
        private static final String DESCRIPTOR = "android.app.IActivityPendingResult";
        static final int TRANSACTION_sendResult = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IActivityPendingResult asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IActivityPendingResult)) {
                return (IActivityPendingResult) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "sendResult";
            }
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Bundle _arg2;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            String _arg1 = data.readString();
            if (data.readInt() != 0) {
                _arg2 = Bundle.CREATOR.createFromParcel(data);
            } else {
                _arg2 = null;
            }
            boolean sendResult = sendResult(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(sendResult ? 1 : 0);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IActivityPendingResult {
            public static IActivityPendingResult sDefaultImpl;
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

            @Override // android.app.IActivityPendingResult
            public boolean sendResult(int code, String data, Bundle ex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeString(data);
                    if (ex != null) {
                        _data.writeInt(1);
                        ex.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().sendResult(code, data, ex);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IActivityPendingResult impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IActivityPendingResult getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
