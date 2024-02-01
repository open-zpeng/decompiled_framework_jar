package android.os;
/* loaded from: classes2.dex */
public interface IStatsManager extends IInterface {
    synchronized void addConfiguration(long j, byte[] bArr, String str) throws RemoteException;

    synchronized byte[] getData(long j, String str) throws RemoteException;

    synchronized byte[] getMetadata(String str) throws RemoteException;

    synchronized void informAlarmForSubscriberTriggeringFired() throws RemoteException;

    synchronized void informAllUidData(int[] iArr, long[] jArr, String[] strArr) throws RemoteException;

    synchronized void informAnomalyAlarmFired() throws RemoteException;

    synchronized void informDeviceShutdown() throws RemoteException;

    synchronized void informOnePackage(String str, int i, long j) throws RemoteException;

    synchronized void informOnePackageRemoved(String str, int i) throws RemoteException;

    synchronized void informPollAlarmFired() throws RemoteException;

    synchronized void removeConfiguration(long j, String str) throws RemoteException;

    synchronized void removeDataFetchOperation(long j, String str) throws RemoteException;

    synchronized void sendAppBreadcrumbAtom(int i, int i2) throws RemoteException;

    synchronized void setBroadcastSubscriber(long j, long j2, IBinder iBinder, String str) throws RemoteException;

    synchronized void setDataFetchOperation(long j, IBinder iBinder, String str) throws RemoteException;

    synchronized void statsCompanionReady() throws RemoteException;

    synchronized void systemRunning() throws RemoteException;

    synchronized void unsetBroadcastSubscriber(long j, long j2, String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IStatsManager {
        private static final String DESCRIPTOR = "android.os.IStatsManager";
        static final int TRANSACTION_addConfiguration = 12;
        static final int TRANSACTION_getData = 10;
        static final int TRANSACTION_getMetadata = 11;
        static final int TRANSACTION_informAlarmForSubscriberTriggeringFired = 5;
        static final int TRANSACTION_informAllUidData = 7;
        static final int TRANSACTION_informAnomalyAlarmFired = 3;
        static final int TRANSACTION_informDeviceShutdown = 6;
        static final int TRANSACTION_informOnePackage = 8;
        static final int TRANSACTION_informOnePackageRemoved = 9;
        static final int TRANSACTION_informPollAlarmFired = 4;
        static final int TRANSACTION_removeConfiguration = 15;
        static final int TRANSACTION_removeDataFetchOperation = 14;
        static final int TRANSACTION_sendAppBreadcrumbAtom = 18;
        static final int TRANSACTION_setBroadcastSubscriber = 16;
        static final int TRANSACTION_setDataFetchOperation = 13;
        static final int TRANSACTION_statsCompanionReady = 2;
        static final int TRANSACTION_systemRunning = 1;
        static final int TRANSACTION_unsetBroadcastSubscriber = 17;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IStatsManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IStatsManager)) {
                return (IStatsManager) iin;
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
                    systemRunning();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    statsCompanionReady();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    informAnomalyAlarmFired();
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    informPollAlarmFired();
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    informAlarmForSubscriberTriggeringFired();
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    informDeviceShutdown();
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg0 = data.createIntArray();
                    long[] _arg1 = data.createLongArray();
                    String[] _arg2 = data.createStringArray();
                    informAllUidData(_arg0, _arg1, _arg2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    int _arg12 = data.readInt();
                    long _arg22 = data.readLong();
                    informOnePackage(_arg02, _arg12, _arg22);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    int _arg13 = data.readInt();
                    informOnePackageRemoved(_arg03, _arg13);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg04 = data.readLong();
                    String _arg14 = data.readString();
                    byte[] _result = getData(_arg04, _arg14);
                    reply.writeNoException();
                    reply.writeByteArray(_result);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    byte[] _result2 = getMetadata(_arg05);
                    reply.writeNoException();
                    reply.writeByteArray(_result2);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg06 = data.readLong();
                    byte[] _arg15 = data.createByteArray();
                    String _arg23 = data.readString();
                    addConfiguration(_arg06, _arg15, _arg23);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg07 = data.readLong();
                    IBinder _arg16 = data.readStrongBinder();
                    String _arg24 = data.readString();
                    setDataFetchOperation(_arg07, _arg16, _arg24);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg08 = data.readLong();
                    String _arg17 = data.readString();
                    removeDataFetchOperation(_arg08, _arg17);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg09 = data.readLong();
                    String _arg18 = data.readString();
                    removeConfiguration(_arg09, _arg18);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg010 = data.readLong();
                    long _arg19 = data.readLong();
                    IBinder _arg25 = data.readStrongBinder();
                    String _arg3 = data.readString();
                    setBroadcastSubscriber(_arg010, _arg19, _arg25, _arg3);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg011 = data.readLong();
                    long _arg110 = data.readLong();
                    String _arg26 = data.readString();
                    unsetBroadcastSubscriber(_arg011, _arg110, _arg26);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    int _arg111 = data.readInt();
                    sendAppBreadcrumbAtom(_arg012, _arg111);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IStatsManager {
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

            @Override // android.os.IStatsManager
            public synchronized void systemRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void statsCompanionReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void informAnomalyAlarmFired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void informPollAlarmFired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void informAlarmForSubscriberTriggeringFired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void informDeviceShutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void informAllUidData(int[] uid, long[] version, String[] app) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(uid);
                    _data.writeLongArray(version);
                    _data.writeStringArray(app);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void informOnePackage(String app, int uid, long version) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(app);
                    _data.writeInt(uid);
                    _data.writeLong(version);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void informOnePackageRemoved(String app, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(app);
                    _data.writeInt(uid);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized byte[] getData(long key, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(key);
                    _data.writeString(packageName);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized byte[] getMetadata(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void addConfiguration(long configKey, byte[] config, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(configKey);
                    _data.writeByteArray(config);
                    _data.writeString(packageName);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void setDataFetchOperation(long configKey, IBinder intentSender, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(configKey);
                    _data.writeStrongBinder(intentSender);
                    _data.writeString(packageName);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void removeDataFetchOperation(long configKey, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(configKey);
                    _data.writeString(packageName);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void removeConfiguration(long configKey, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(configKey);
                    _data.writeString(packageName);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void setBroadcastSubscriber(long configKey, long subscriberId, IBinder intentSender, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(configKey);
                    _data.writeLong(subscriberId);
                    _data.writeStrongBinder(intentSender);
                    _data.writeString(packageName);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void unsetBroadcastSubscriber(long configKey, long subscriberId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(configKey);
                    _data.writeLong(subscriberId);
                    _data.writeString(packageName);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsManager
            public synchronized void sendAppBreadcrumbAtom(int label, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(label);
                    _data.writeInt(state);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
