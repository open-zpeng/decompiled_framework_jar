package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IHdmiMhlVendorCommandListener extends IInterface {
    void onReceived(int i, int i2, int i3, byte[] bArr) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IHdmiMhlVendorCommandListener {
        @Override // android.hardware.hdmi.IHdmiMhlVendorCommandListener
        public void onReceived(int portId, int offset, int length, byte[] data) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IHdmiMhlVendorCommandListener {
        private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiMhlVendorCommandListener";
        static final int TRANSACTION_onReceived = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IHdmiMhlVendorCommandListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IHdmiMhlVendorCommandListener)) {
                return (IHdmiMhlVendorCommandListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onReceived";
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
            int _arg0 = data.readInt();
            int _arg1 = data.readInt();
            int _arg2 = data.readInt();
            byte[] _arg3 = data.createByteArray();
            onReceived(_arg0, _arg1, _arg2, _arg3);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IHdmiMhlVendorCommandListener {
            public static IHdmiMhlVendorCommandListener sDefaultImpl;
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

            @Override // android.hardware.hdmi.IHdmiMhlVendorCommandListener
            public void onReceived(int portId, int offset, int length, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(portId);
                    _data.writeInt(offset);
                    _data.writeInt(length);
                    _data.writeByteArray(data);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onReceived(portId, offset, length, data);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IHdmiMhlVendorCommandListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IHdmiMhlVendorCommandListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
