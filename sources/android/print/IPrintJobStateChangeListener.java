package android.print;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface IPrintJobStateChangeListener extends IInterface {
    void onPrintJobStateChanged(PrintJobId printJobId) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IPrintJobStateChangeListener {
        @Override // android.print.IPrintJobStateChangeListener
        public void onPrintJobStateChanged(PrintJobId printJobId) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IPrintJobStateChangeListener {
        private static final String DESCRIPTOR = "android.print.IPrintJobStateChangeListener";
        static final int TRANSACTION_onPrintJobStateChanged = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPrintJobStateChangeListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IPrintJobStateChangeListener)) {
                return (IPrintJobStateChangeListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onPrintJobStateChanged";
            }
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            PrintJobId _arg0;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = PrintJobId.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            onPrintJobStateChanged(_arg0);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IPrintJobStateChangeListener {
            public static IPrintJobStateChangeListener sDefaultImpl;
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

            @Override // android.print.IPrintJobStateChangeListener
            public void onPrintJobStateChanged(PrintJobId printJobId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJobId != null) {
                        _data.writeInt(1);
                        printJobId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onPrintJobStateChanged(printJobId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IPrintJobStateChangeListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IPrintJobStateChangeListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
