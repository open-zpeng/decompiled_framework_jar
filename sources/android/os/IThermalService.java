package android.os;

import android.os.IThermalEventListener;
import android.os.IThermalStatusListener;
import java.util.List;

/* loaded from: classes2.dex */
public interface IThermalService extends IInterface {
    List<CoolingDevice> getCurrentCoolingDevices() throws RemoteException;

    List<CoolingDevice> getCurrentCoolingDevicesWithType(int i) throws RemoteException;

    List<Temperature> getCurrentTemperatures() throws RemoteException;

    List<Temperature> getCurrentTemperaturesWithType(int i) throws RemoteException;

    int getCurrentThermalStatus() throws RemoteException;

    boolean registerThermalEventListener(IThermalEventListener iThermalEventListener) throws RemoteException;

    boolean registerThermalEventListenerWithType(IThermalEventListener iThermalEventListener, int i) throws RemoteException;

    boolean registerThermalStatusListener(IThermalStatusListener iThermalStatusListener) throws RemoteException;

    boolean unregisterThermalEventListener(IThermalEventListener iThermalEventListener) throws RemoteException;

    boolean unregisterThermalStatusListener(IThermalStatusListener iThermalStatusListener) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IThermalService {
        @Override // android.os.IThermalService
        public boolean registerThermalEventListener(IThermalEventListener listener) throws RemoteException {
            return false;
        }

        @Override // android.os.IThermalService
        public boolean registerThermalEventListenerWithType(IThermalEventListener listener, int type) throws RemoteException {
            return false;
        }

        @Override // android.os.IThermalService
        public boolean unregisterThermalEventListener(IThermalEventListener listener) throws RemoteException {
            return false;
        }

        @Override // android.os.IThermalService
        public List<Temperature> getCurrentTemperatures() throws RemoteException {
            return null;
        }

        @Override // android.os.IThermalService
        public List<Temperature> getCurrentTemperaturesWithType(int type) throws RemoteException {
            return null;
        }

        @Override // android.os.IThermalService
        public boolean registerThermalStatusListener(IThermalStatusListener listener) throws RemoteException {
            return false;
        }

        @Override // android.os.IThermalService
        public boolean unregisterThermalStatusListener(IThermalStatusListener listener) throws RemoteException {
            return false;
        }

        @Override // android.os.IThermalService
        public int getCurrentThermalStatus() throws RemoteException {
            return 0;
        }

        @Override // android.os.IThermalService
        public List<CoolingDevice> getCurrentCoolingDevices() throws RemoteException {
            return null;
        }

        @Override // android.os.IThermalService
        public List<CoolingDevice> getCurrentCoolingDevicesWithType(int type) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IThermalService {
        private static final String DESCRIPTOR = "android.os.IThermalService";
        static final int TRANSACTION_getCurrentCoolingDevices = 9;
        static final int TRANSACTION_getCurrentCoolingDevicesWithType = 10;
        static final int TRANSACTION_getCurrentTemperatures = 4;
        static final int TRANSACTION_getCurrentTemperaturesWithType = 5;
        static final int TRANSACTION_getCurrentThermalStatus = 8;
        static final int TRANSACTION_registerThermalEventListener = 1;
        static final int TRANSACTION_registerThermalEventListenerWithType = 2;
        static final int TRANSACTION_registerThermalStatusListener = 6;
        static final int TRANSACTION_unregisterThermalEventListener = 3;
        static final int TRANSACTION_unregisterThermalStatusListener = 7;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IThermalService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IThermalService)) {
                return (IThermalService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "registerThermalEventListener";
                case 2:
                    return "registerThermalEventListenerWithType";
                case 3:
                    return "unregisterThermalEventListener";
                case 4:
                    return "getCurrentTemperatures";
                case 5:
                    return "getCurrentTemperaturesWithType";
                case 6:
                    return "registerThermalStatusListener";
                case 7:
                    return "unregisterThermalStatusListener";
                case 8:
                    return "getCurrentThermalStatus";
                case 9:
                    return "getCurrentCoolingDevices";
                case 10:
                    return "getCurrentCoolingDevicesWithType";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
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
                    IThermalEventListener _arg0 = IThermalEventListener.Stub.asInterface(data.readStrongBinder());
                    boolean registerThermalEventListener = registerThermalEventListener(_arg0);
                    reply.writeNoException();
                    reply.writeInt(registerThermalEventListener ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IThermalEventListener _arg02 = IThermalEventListener.Stub.asInterface(data.readStrongBinder());
                    int _arg1 = data.readInt();
                    boolean registerThermalEventListenerWithType = registerThermalEventListenerWithType(_arg02, _arg1);
                    reply.writeNoException();
                    reply.writeInt(registerThermalEventListenerWithType ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IThermalEventListener _arg03 = IThermalEventListener.Stub.asInterface(data.readStrongBinder());
                    boolean unregisterThermalEventListener = unregisterThermalEventListener(_arg03);
                    reply.writeNoException();
                    reply.writeInt(unregisterThermalEventListener ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    List<Temperature> _result = getCurrentTemperatures();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    List<Temperature> _result2 = getCurrentTemperaturesWithType(_arg04);
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IThermalStatusListener _arg05 = IThermalStatusListener.Stub.asInterface(data.readStrongBinder());
                    boolean registerThermalStatusListener = registerThermalStatusListener(_arg05);
                    reply.writeNoException();
                    reply.writeInt(registerThermalStatusListener ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IThermalStatusListener _arg06 = IThermalStatusListener.Stub.asInterface(data.readStrongBinder());
                    boolean unregisterThermalStatusListener = unregisterThermalStatusListener(_arg06);
                    reply.writeNoException();
                    reply.writeInt(unregisterThermalStatusListener ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _result3 = getCurrentThermalStatus();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    List<CoolingDevice> _result4 = getCurrentCoolingDevices();
                    reply.writeNoException();
                    reply.writeTypedList(_result4);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    List<CoolingDevice> _result5 = getCurrentCoolingDevicesWithType(_arg07);
                    reply.writeNoException();
                    reply.writeTypedList(_result5);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IThermalService {
            public static IThermalService sDefaultImpl;
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

            @Override // android.os.IThermalService
            public boolean registerThermalEventListener(IThermalEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerThermalEventListener(listener);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IThermalService
            public boolean registerThermalEventListenerWithType(IThermalEventListener listener, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerThermalEventListenerWithType(listener, type);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IThermalService
            public boolean unregisterThermalEventListener(IThermalEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unregisterThermalEventListener(listener);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IThermalService
            public List<Temperature> getCurrentTemperatures() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentTemperatures();
                    }
                    _reply.readException();
                    List<Temperature> _result = _reply.createTypedArrayList(Temperature.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IThermalService
            public List<Temperature> getCurrentTemperaturesWithType(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentTemperaturesWithType(type);
                    }
                    _reply.readException();
                    List<Temperature> _result = _reply.createTypedArrayList(Temperature.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IThermalService
            public boolean registerThermalStatusListener(IThermalStatusListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerThermalStatusListener(listener);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IThermalService
            public boolean unregisterThermalStatusListener(IThermalStatusListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unregisterThermalStatusListener(listener);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IThermalService
            public int getCurrentThermalStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentThermalStatus();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IThermalService
            public List<CoolingDevice> getCurrentCoolingDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentCoolingDevices();
                    }
                    _reply.readException();
                    List<CoolingDevice> _result = _reply.createTypedArrayList(CoolingDevice.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IThermalService
            public List<CoolingDevice> getCurrentCoolingDevicesWithType(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentCoolingDevicesWithType(type);
                    }
                    _reply.readException();
                    List<CoolingDevice> _result = _reply.createTypedArrayList(CoolingDevice.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IThermalService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IThermalService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
