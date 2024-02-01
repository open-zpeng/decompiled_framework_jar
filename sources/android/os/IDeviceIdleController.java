package android.os;

import android.os.IMaintenanceActivityListener;
/* loaded from: classes2.dex */
public interface IDeviceIdleController extends IInterface {
    private protected void addPowerSaveTempWhitelistApp(String str, long j, int i, String str2) throws RemoteException;

    synchronized long addPowerSaveTempWhitelistAppForMms(String str, int i, String str2) throws RemoteException;

    synchronized long addPowerSaveTempWhitelistAppForSms(String str, int i, String str2) throws RemoteException;

    synchronized void addPowerSaveWhitelistApp(String str) throws RemoteException;

    synchronized void exitIdle(String str) throws RemoteException;

    private protected int[] getAppIdTempWhitelist() throws RemoteException;

    synchronized int[] getAppIdUserWhitelist() throws RemoteException;

    synchronized int[] getAppIdWhitelist() throws RemoteException;

    synchronized int[] getAppIdWhitelistExceptIdle() throws RemoteException;

    synchronized String[] getFullPowerWhitelist() throws RemoteException;

    private protected String[] getFullPowerWhitelistExceptIdle() throws RemoteException;

    synchronized String[] getRemovedSystemPowerWhitelistApps() throws RemoteException;

    synchronized String[] getSystemPowerWhitelist() throws RemoteException;

    synchronized String[] getSystemPowerWhitelistExceptIdle() throws RemoteException;

    synchronized String[] getUserPowerWhitelist() throws RemoteException;

    synchronized boolean isPowerSaveWhitelistApp(String str) throws RemoteException;

    synchronized boolean isPowerSaveWhitelistExceptIdleApp(String str) throws RemoteException;

    synchronized boolean registerMaintenanceActivityListener(IMaintenanceActivityListener iMaintenanceActivityListener) throws RemoteException;

    synchronized void removePowerSaveWhitelistApp(String str) throws RemoteException;

    synchronized void removeSystemPowerWhitelistApp(String str) throws RemoteException;

    synchronized void restoreSystemPowerWhitelistApp(String str) throws RemoteException;

    synchronized void unregisterMaintenanceActivityListener(IMaintenanceActivityListener iMaintenanceActivityListener) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IDeviceIdleController {
        private static final String DESCRIPTOR = "android.os.IDeviceIdleController";
        static final int TRANSACTION_addPowerSaveTempWhitelistApp = 17;
        static final int TRANSACTION_addPowerSaveTempWhitelistAppForMms = 18;
        static final int TRANSACTION_addPowerSaveTempWhitelistAppForSms = 19;
        static final int TRANSACTION_addPowerSaveWhitelistApp = 1;
        static final int TRANSACTION_exitIdle = 20;
        static final int TRANSACTION_getAppIdTempWhitelist = 14;
        static final int TRANSACTION_getAppIdUserWhitelist = 13;
        static final int TRANSACTION_getAppIdWhitelist = 12;
        static final int TRANSACTION_getAppIdWhitelistExceptIdle = 11;
        static final int TRANSACTION_getFullPowerWhitelist = 10;
        static final int TRANSACTION_getFullPowerWhitelistExceptIdle = 9;
        static final int TRANSACTION_getRemovedSystemPowerWhitelistApps = 5;
        static final int TRANSACTION_getSystemPowerWhitelist = 7;
        static final int TRANSACTION_getSystemPowerWhitelistExceptIdle = 6;
        static final int TRANSACTION_getUserPowerWhitelist = 8;
        static final int TRANSACTION_isPowerSaveWhitelistApp = 16;
        static final int TRANSACTION_isPowerSaveWhitelistExceptIdleApp = 15;
        static final int TRANSACTION_registerMaintenanceActivityListener = 21;
        static final int TRANSACTION_removePowerSaveWhitelistApp = 2;
        static final int TRANSACTION_removeSystemPowerWhitelistApp = 3;
        static final int TRANSACTION_restoreSystemPowerWhitelistApp = 4;
        static final int TRANSACTION_unregisterMaintenanceActivityListener = 22;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IDeviceIdleController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IDeviceIdleController)) {
                return (IDeviceIdleController) iin;
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
                    String _arg0 = data.readString();
                    addPowerSaveWhitelistApp(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    removePowerSaveWhitelistApp(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    removeSystemPowerWhitelistApp(_arg03);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    restoreSystemPowerWhitelistApp(_arg04);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result = getRemovedSystemPowerWhitelistApps();
                    reply.writeNoException();
                    reply.writeStringArray(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result2 = getSystemPowerWhitelistExceptIdle();
                    reply.writeNoException();
                    reply.writeStringArray(_result2);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result3 = getSystemPowerWhitelist();
                    reply.writeNoException();
                    reply.writeStringArray(_result3);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result4 = getUserPowerWhitelist();
                    reply.writeNoException();
                    reply.writeStringArray(_result4);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result5 = getFullPowerWhitelistExceptIdle();
                    reply.writeNoException();
                    reply.writeStringArray(_result5);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result6 = getFullPowerWhitelist();
                    reply.writeNoException();
                    reply.writeStringArray(_result6);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result7 = getAppIdWhitelistExceptIdle();
                    reply.writeNoException();
                    reply.writeIntArray(_result7);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result8 = getAppIdWhitelist();
                    reply.writeNoException();
                    reply.writeIntArray(_result8);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result9 = getAppIdUserWhitelist();
                    reply.writeNoException();
                    reply.writeIntArray(_result9);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result10 = getAppIdTempWhitelist();
                    reply.writeNoException();
                    reply.writeIntArray(_result10);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    boolean isPowerSaveWhitelistExceptIdleApp = isPowerSaveWhitelistExceptIdleApp(_arg05);
                    reply.writeNoException();
                    reply.writeInt(isPowerSaveWhitelistExceptIdleApp ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    boolean isPowerSaveWhitelistApp = isPowerSaveWhitelistApp(_arg06);
                    reply.writeNoException();
                    reply.writeInt(isPowerSaveWhitelistApp ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    long _arg1 = data.readLong();
                    int _arg2 = data.readInt();
                    String _arg3 = data.readString();
                    addPowerSaveTempWhitelistApp(_arg07, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    int _arg12 = data.readInt();
                    String _arg22 = data.readString();
                    long _result11 = addPowerSaveTempWhitelistAppForMms(_arg08, _arg12, _arg22);
                    reply.writeNoException();
                    reply.writeLong(_result11);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    int _arg13 = data.readInt();
                    String _arg23 = data.readString();
                    long _result12 = addPowerSaveTempWhitelistAppForSms(_arg09, _arg13, _arg23);
                    reply.writeNoException();
                    reply.writeLong(_result12);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    exitIdle(_arg010);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    IMaintenanceActivityListener _arg011 = IMaintenanceActivityListener.Stub.asInterface(data.readStrongBinder());
                    boolean registerMaintenanceActivityListener = registerMaintenanceActivityListener(_arg011);
                    reply.writeNoException();
                    reply.writeInt(registerMaintenanceActivityListener ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    IMaintenanceActivityListener _arg012 = IMaintenanceActivityListener.Stub.asInterface(data.readStrongBinder());
                    unregisterMaintenanceActivityListener(_arg012);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IDeviceIdleController {
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

            @Override // android.os.IDeviceIdleController
            public synchronized void addPowerSaveWhitelistApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized void removePowerSaveWhitelistApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized void removeSystemPowerWhitelistApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized void restoreSystemPowerWhitelistApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized String[] getRemovedSystemPowerWhitelistApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized String[] getSystemPowerWhitelistExceptIdle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized String[] getSystemPowerWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized String[] getUserPowerWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String[] getFullPowerWhitelistExceptIdle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized String[] getFullPowerWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized int[] getAppIdWhitelistExceptIdle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized int[] getAppIdWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized int[] getAppIdUserWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int[] getAppIdTempWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized boolean isPowerSaveWhitelistExceptIdleApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized boolean isPowerSaveWhitelistApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void addPowerSaveTempWhitelistApp(String name, long duration, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeLong(duration);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized long addPowerSaveTempWhitelistAppForMms(String name, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized long addPowerSaveTempWhitelistAppForSms(String name, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized void exitIdle(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized boolean registerMaintenanceActivityListener(IMaintenanceActivityListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDeviceIdleController
            public synchronized void unregisterMaintenanceActivityListener(IMaintenanceActivityListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
