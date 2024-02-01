package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IBluetoothMetadataListener extends IInterface {
    void onMetadataChanged(BluetoothDevice bluetoothDevice, int i, byte[] bArr) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBluetoothMetadataListener {
        @Override // android.bluetooth.IBluetoothMetadataListener
        public void onMetadataChanged(BluetoothDevice devices, int key, byte[] value) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBluetoothMetadataListener {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothMetadataListener";
        static final int TRANSACTION_onMetadataChanged = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothMetadataListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBluetoothMetadataListener)) {
                return (IBluetoothMetadataListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onMetadataChanged";
            }
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            BluetoothDevice _arg0;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = BluetoothDevice.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            int _arg1 = data.readInt();
            byte[] _arg2 = data.createByteArray();
            onMetadataChanged(_arg0, _arg1, _arg2);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBluetoothMetadataListener {
            public static IBluetoothMetadataListener sDefaultImpl;
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

            @Override // android.bluetooth.IBluetoothMetadataListener
            public void onMetadataChanged(BluetoothDevice devices, int key, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (devices != null) {
                        _data.writeInt(1);
                        devices.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(key);
                    _data.writeByteArray(value);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onMetadataChanged(devices, key, value);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBluetoothMetadataListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBluetoothMetadataListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
