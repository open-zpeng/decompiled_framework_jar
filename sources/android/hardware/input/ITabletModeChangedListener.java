package android.hardware.input;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ITabletModeChangedListener extends IInterface {
    void onTabletModeChanged(long j, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ITabletModeChangedListener {
        @Override // android.hardware.input.ITabletModeChangedListener
        public void onTabletModeChanged(long whenNanos, boolean inTabletMode) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ITabletModeChangedListener {
        private static final String DESCRIPTOR = "android.hardware.input.ITabletModeChangedListener";
        static final int TRANSACTION_onTabletModeChanged = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITabletModeChangedListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITabletModeChangedListener)) {
                return (ITabletModeChangedListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onTabletModeChanged";
            }
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            long _arg0 = data.readLong();
            boolean _arg1 = data.readInt() != 0;
            onTabletModeChanged(_arg0, _arg1);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements ITabletModeChangedListener {
            public static ITabletModeChangedListener sDefaultImpl;
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

            @Override // android.hardware.input.ITabletModeChangedListener
            public void onTabletModeChanged(long whenNanos, boolean inTabletMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(whenNanos);
                    _data.writeInt(inTabletMode ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTabletModeChanged(whenNanos, inTabletMode);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ITabletModeChangedListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ITabletModeChangedListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
