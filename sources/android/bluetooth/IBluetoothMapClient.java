package android.bluetooth;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes.dex */
public interface IBluetoothMapClient extends IInterface {
    boolean abort(BluetoothDevice bluetoothDevice) throws RemoteException;

    synchronized boolean connect(BluetoothDevice bluetoothDevice) throws RemoteException;

    synchronized boolean disconnect(BluetoothDevice bluetoothDevice) throws RemoteException;

    synchronized List<BluetoothDevice> getConnectedDevices() throws RemoteException;

    synchronized int getConnectionState(BluetoothDevice bluetoothDevice) throws RemoteException;

    synchronized List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] iArr) throws RemoteException;

    synchronized int getPriority(BluetoothDevice bluetoothDevice) throws RemoteException;

    synchronized boolean getUnreadMessages(BluetoothDevice bluetoothDevice) throws RemoteException;

    synchronized boolean isConnected(BluetoothDevice bluetoothDevice) throws RemoteException;

    synchronized boolean sendMessage(BluetoothDevice bluetoothDevice, Uri[] uriArr, String str, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    boolean setActiveInstance(BluetoothDevice bluetoothDevice, byte b) throws RemoteException;

    boolean setMessageStatus(BluetoothDevice bluetoothDevice, String str, int i) throws RemoteException;

    synchronized boolean setPriority(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBluetoothMapClient {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothMapClient";
        static final int TRANSACTION_abort = 12;
        static final int TRANSACTION_connect = 1;
        static final int TRANSACTION_disconnect = 2;
        static final int TRANSACTION_getConnectedDevices = 4;
        static final int TRANSACTION_getConnectionState = 6;
        static final int TRANSACTION_getDevicesMatchingConnectionStates = 5;
        static final int TRANSACTION_getPriority = 8;
        static final int TRANSACTION_getUnreadMessages = 10;
        static final int TRANSACTION_isConnected = 3;
        static final int TRANSACTION_sendMessage = 9;
        static final int TRANSACTION_setActiveInstance = 13;
        static final int TRANSACTION_setMessageStatus = 11;
        static final int TRANSACTION_setPriority = 7;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IBluetoothMapClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBluetoothMapClient)) {
                return (IBluetoothMapClient) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            BluetoothDevice _arg0;
            PendingIntent _arg3;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg02 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    boolean connect = connect(_arg02);
                    reply.writeNoException();
                    reply.writeInt(connect ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg03 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    boolean disconnect = disconnect(_arg03);
                    reply.writeNoException();
                    reply.writeInt(disconnect ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg04 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    boolean isConnected = isConnected(_arg04);
                    reply.writeNoException();
                    reply.writeInt(isConnected ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    List<BluetoothDevice> _result = getConnectedDevices();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg05 = data.createIntArray();
                    List<BluetoothDevice> _result2 = getDevicesMatchingConnectionStates(_arg05);
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg06 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    int _result3 = getConnectionState(_arg06);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg07 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    int _arg1 = data.readInt();
                    boolean priority = setPriority(_arg07, _arg1);
                    reply.writeNoException();
                    reply.writeInt(priority ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg08 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    int _result4 = getPriority(_arg08);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        BluetoothDevice _arg09 = BluetoothDevice.CREATOR.createFromParcel(data);
                        _arg0 = _arg09;
                    } else {
                        _arg0 = null;
                    }
                    Uri[] _arg12 = (Uri[]) data.createTypedArray(Uri.CREATOR);
                    String _arg2 = data.readString();
                    if (data.readInt() != 0) {
                        PendingIntent _arg32 = PendingIntent.CREATOR.createFromParcel(data);
                        _arg3 = _arg32;
                    } else {
                        _arg3 = null;
                    }
                    PendingIntent _arg4 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    boolean sendMessage = sendMessage(_arg0, _arg12, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeInt(sendMessage ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg010 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    boolean unreadMessages = getUnreadMessages(_arg010);
                    reply.writeNoException();
                    reply.writeInt(unreadMessages ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg011 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    String _arg13 = data.readString();
                    int _arg22 = data.readInt();
                    boolean messageStatus = setMessageStatus(_arg011, _arg13, _arg22);
                    reply.writeNoException();
                    reply.writeInt(messageStatus ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg012 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    boolean abort = abort(_arg012);
                    reply.writeNoException();
                    reply.writeInt(abort ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg013 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    byte _arg14 = data.readByte();
                    boolean activeInstance = setActiveInstance(_arg013, _arg14);
                    reply.writeNoException();
                    reply.writeInt(activeInstance ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IBluetoothMapClient {
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

            @Override // android.bluetooth.IBluetoothMapClient
            public synchronized boolean connect(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public synchronized boolean disconnect(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public synchronized boolean isConnected(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public synchronized List<BluetoothDevice> getConnectedDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    List<BluetoothDevice> _result = _reply.createTypedArrayList(BluetoothDevice.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public synchronized List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(states);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    List<BluetoothDevice> _result = _reply.createTypedArrayList(BluetoothDevice.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public synchronized int getConnectionState(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public synchronized boolean setPriority(BluetoothDevice device, int priority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(priority);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public synchronized int getPriority(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public synchronized boolean sendMessage(BluetoothDevice device, Uri[] contacts, String message, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedArray(contacts, 0);
                    _data.writeString(message);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public synchronized boolean getUnreadMessages(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public boolean setMessageStatus(BluetoothDevice device, String handle, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(handle);
                    _data.writeInt(status);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public boolean abort(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothMapClient
            public boolean setActiveInstance(BluetoothDevice device, byte instance) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByte(instance);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
