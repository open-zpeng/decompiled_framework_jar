package android.net.lowpan;

import android.net.IpPrefix;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface ILowpanInterfaceListener extends IInterface {
    private protected synchronized void onConnectedChanged(boolean z) throws RemoteException;

    private protected synchronized void onEnabledChanged(boolean z) throws RemoteException;

    private protected synchronized void onLinkAddressAdded(String str) throws RemoteException;

    private protected synchronized void onLinkAddressRemoved(String str) throws RemoteException;

    private protected synchronized void onLinkNetworkAdded(IpPrefix ipPrefix) throws RemoteException;

    private protected synchronized void onLinkNetworkRemoved(IpPrefix ipPrefix) throws RemoteException;

    private protected synchronized void onLowpanIdentityChanged(LowpanIdentity lowpanIdentity) throws RemoteException;

    private protected synchronized void onReceiveFromCommissioner(byte[] bArr) throws RemoteException;

    private protected synchronized void onRoleChanged(String str) throws RemoteException;

    private protected synchronized void onStateChanged(String str) throws RemoteException;

    private protected synchronized void onUpChanged(boolean z) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ILowpanInterfaceListener {
        public protected static final String DESCRIPTOR = "android.net.lowpan.ILowpanInterfaceListener";
        public private protected static final int TRANSACTION_onConnectedChanged = 2;
        public private protected static final int TRANSACTION_onEnabledChanged = 1;
        public private protected static final int TRANSACTION_onLinkAddressAdded = 9;
        public private protected static final int TRANSACTION_onLinkAddressRemoved = 10;
        public private protected static final int TRANSACTION_onLinkNetworkAdded = 7;
        public private protected static final int TRANSACTION_onLinkNetworkRemoved = 8;
        public private protected static final int TRANSACTION_onLowpanIdentityChanged = 6;
        public private protected static final int TRANSACTION_onReceiveFromCommissioner = 11;
        public private protected static final int TRANSACTION_onRoleChanged = 4;
        public private protected static final int TRANSACTION_onStateChanged = 5;
        public private protected static final int TRANSACTION_onUpChanged = 3;

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized ILowpanInterfaceListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ILowpanInterfaceListener)) {
                return (ILowpanInterfaceListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    onEnabledChanged(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    onConnectedChanged(_arg0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    onUpChanged(_arg0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onRoleChanged(data.readString());
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onStateChanged(data.readString());
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    LowpanIdentity _arg02 = data.readInt() != 0 ? LowpanIdentity.CREATOR.createFromParcel(data) : null;
                    onLowpanIdentityChanged(_arg02);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IpPrefix _arg03 = data.readInt() != 0 ? IpPrefix.CREATOR.createFromParcel(data) : null;
                    onLinkNetworkAdded(_arg03);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    IpPrefix _arg04 = data.readInt() != 0 ? IpPrefix.CREATOR.createFromParcel(data) : null;
                    onLinkNetworkRemoved(_arg04);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    onLinkAddressAdded(data.readString());
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    onLinkAddressRemoved(data.readString());
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    onReceiveFromCommissioner(data.createByteArray());
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ILowpanInterfaceListener {
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

            private protected synchronized void onEnabledChanged(boolean value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(value ? 1 : 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onConnectedChanged(boolean value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(value ? 1 : 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onUpChanged(boolean value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(value ? 1 : 0);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onRoleChanged(String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(value);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onStateChanged(String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(value);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onLowpanIdentityChanged(LowpanIdentity value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (value != null) {
                        _data.writeInt(1);
                        value.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onLinkNetworkAdded(IpPrefix value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (value != null) {
                        _data.writeInt(1);
                        value.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onLinkNetworkRemoved(IpPrefix value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (value != null) {
                        _data.writeInt(1);
                        value.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onLinkAddressAdded(String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(value);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onLinkAddressRemoved(String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(value);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onReceiveFromCommissioner(byte[] packet) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(packet);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
