package android.media.midi;

import android.bluetooth.BluetoothDevice;
import android.media.midi.IMidiDeviceListener;
import android.media.midi.IMidiDeviceOpenCallback;
import android.media.midi.IMidiDeviceServer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IMidiManager extends IInterface {
    synchronized void closeDevice(IBinder iBinder, IBinder iBinder2) throws RemoteException;

    synchronized MidiDeviceStatus getDeviceStatus(MidiDeviceInfo midiDeviceInfo) throws RemoteException;

    synchronized MidiDeviceInfo[] getDevices() throws RemoteException;

    synchronized MidiDeviceInfo getServiceDeviceInfo(String str, String str2) throws RemoteException;

    synchronized void openBluetoothDevice(IBinder iBinder, BluetoothDevice bluetoothDevice, IMidiDeviceOpenCallback iMidiDeviceOpenCallback) throws RemoteException;

    synchronized void openDevice(IBinder iBinder, MidiDeviceInfo midiDeviceInfo, IMidiDeviceOpenCallback iMidiDeviceOpenCallback) throws RemoteException;

    synchronized MidiDeviceInfo registerDeviceServer(IMidiDeviceServer iMidiDeviceServer, int i, int i2, String[] strArr, String[] strArr2, Bundle bundle, int i3) throws RemoteException;

    synchronized void registerListener(IBinder iBinder, IMidiDeviceListener iMidiDeviceListener) throws RemoteException;

    synchronized void setDeviceStatus(IMidiDeviceServer iMidiDeviceServer, MidiDeviceStatus midiDeviceStatus) throws RemoteException;

    synchronized void unregisterDeviceServer(IMidiDeviceServer iMidiDeviceServer) throws RemoteException;

    synchronized void unregisterListener(IBinder iBinder, IMidiDeviceListener iMidiDeviceListener) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IMidiManager {
        private static final String DESCRIPTOR = "android.media.midi.IMidiManager";
        static final int TRANSACTION_closeDevice = 6;
        static final int TRANSACTION_getDeviceStatus = 10;
        static final int TRANSACTION_getDevices = 1;
        static final int TRANSACTION_getServiceDeviceInfo = 9;
        static final int TRANSACTION_openBluetoothDevice = 5;
        static final int TRANSACTION_openDevice = 4;
        static final int TRANSACTION_registerDeviceServer = 7;
        static final int TRANSACTION_registerListener = 2;
        static final int TRANSACTION_setDeviceStatus = 11;
        static final int TRANSACTION_unregisterDeviceServer = 8;
        static final int TRANSACTION_unregisterListener = 3;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IMidiManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMidiManager)) {
                return (IMidiManager) iin;
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
                    MidiDeviceInfo[] _result = getDevices();
                    reply.writeNoException();
                    reply.writeTypedArray(_result, 1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0 = data.readStrongBinder();
                    IMidiDeviceListener _arg1 = IMidiDeviceListener.Stub.asInterface(data.readStrongBinder());
                    registerListener(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg02 = data.readStrongBinder();
                    IMidiDeviceListener _arg12 = IMidiDeviceListener.Stub.asInterface(data.readStrongBinder());
                    unregisterListener(_arg02, _arg12);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg03 = data.readStrongBinder();
                    MidiDeviceInfo _arg13 = data.readInt() != 0 ? MidiDeviceInfo.CREATOR.createFromParcel(data) : null;
                    IMidiDeviceOpenCallback _arg2 = IMidiDeviceOpenCallback.Stub.asInterface(data.readStrongBinder());
                    openDevice(_arg03, _arg13, _arg2);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg04 = data.readStrongBinder();
                    BluetoothDevice _arg14 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    IMidiDeviceOpenCallback _arg22 = IMidiDeviceOpenCallback.Stub.asInterface(data.readStrongBinder());
                    openBluetoothDevice(_arg04, _arg14, _arg22);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg05 = data.readStrongBinder();
                    IBinder _arg15 = data.readStrongBinder();
                    closeDevice(_arg05, _arg15);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IMidiDeviceServer _arg06 = IMidiDeviceServer.Stub.asInterface(data.readStrongBinder());
                    int _arg16 = data.readInt();
                    int _arg23 = data.readInt();
                    String[] _arg3 = data.createStringArray();
                    String[] _arg4 = data.createStringArray();
                    Bundle _arg5 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    int _arg6 = data.readInt();
                    MidiDeviceInfo _result2 = registerDeviceServer(_arg06, _arg16, _arg23, _arg3, _arg4, _arg5, _arg6);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    IMidiDeviceServer _arg07 = IMidiDeviceServer.Stub.asInterface(data.readStrongBinder());
                    unregisterDeviceServer(_arg07);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    String _arg17 = data.readString();
                    MidiDeviceInfo _result3 = getServiceDeviceInfo(_arg08, _arg17);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    MidiDeviceInfo _arg09 = data.readInt() != 0 ? MidiDeviceInfo.CREATOR.createFromParcel(data) : null;
                    MidiDeviceStatus _result4 = getDeviceStatus(_arg09);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    IMidiDeviceServer _arg010 = IMidiDeviceServer.Stub.asInterface(data.readStrongBinder());
                    MidiDeviceStatus _arg18 = data.readInt() != 0 ? MidiDeviceStatus.CREATOR.createFromParcel(data) : null;
                    setDeviceStatus(_arg010, _arg18);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IMidiManager {
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

            @Override // android.media.midi.IMidiManager
            public synchronized MidiDeviceInfo[] getDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    MidiDeviceInfo[] _result = (MidiDeviceInfo[]) _reply.createTypedArray(MidiDeviceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.midi.IMidiManager
            public synchronized void registerListener(IBinder clientToken, IMidiDeviceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clientToken);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.midi.IMidiManager
            public synchronized void unregisterListener(IBinder clientToken, IMidiDeviceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clientToken);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.midi.IMidiManager
            public synchronized void openDevice(IBinder clientToken, MidiDeviceInfo device, IMidiDeviceOpenCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clientToken);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.midi.IMidiManager
            public synchronized void openBluetoothDevice(IBinder clientToken, BluetoothDevice bluetoothDevice, IMidiDeviceOpenCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clientToken);
                    if (bluetoothDevice != null) {
                        _data.writeInt(1);
                        bluetoothDevice.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.midi.IMidiManager
            public synchronized void closeDevice(IBinder clientToken, IBinder deviceToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clientToken);
                    _data.writeStrongBinder(deviceToken);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.midi.IMidiManager
            public synchronized MidiDeviceInfo registerDeviceServer(IMidiDeviceServer server, int numInputPorts, int numOutputPorts, String[] inputPortNames, String[] outputPortNames, Bundle properties, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    MidiDeviceInfo _result = null;
                    _data.writeStrongBinder(server != null ? server.asBinder() : null);
                    _data.writeInt(numInputPorts);
                    _data.writeInt(numOutputPorts);
                    _data.writeStringArray(inputPortNames);
                    _data.writeStringArray(outputPortNames);
                    if (properties != null) {
                        _data.writeInt(1);
                        properties.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(type);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = MidiDeviceInfo.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.midi.IMidiManager
            public synchronized void unregisterDeviceServer(IMidiDeviceServer server) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(server != null ? server.asBinder() : null);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.midi.IMidiManager
            public synchronized MidiDeviceInfo getServiceDeviceInfo(String packageName, String className) throws RemoteException {
                MidiDeviceInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(className);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = MidiDeviceInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.midi.IMidiManager
            public synchronized MidiDeviceStatus getDeviceStatus(MidiDeviceInfo deviceInfo) throws RemoteException {
                MidiDeviceStatus _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (deviceInfo != null) {
                        _data.writeInt(1);
                        deviceInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = MidiDeviceStatus.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.midi.IMidiManager
            public synchronized void setDeviceStatus(IMidiDeviceServer server, MidiDeviceStatus status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(server != null ? server.asBinder() : null);
                    if (status != null) {
                        _data.writeInt(1);
                        status.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
