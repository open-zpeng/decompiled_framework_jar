package android.os;
/* loaded from: classes2.dex */
public interface IStatsCompanionService extends IInterface {
    synchronized void cancelAlarmForSubscriberTriggering() throws RemoteException;

    synchronized void cancelAnomalyAlarm() throws RemoteException;

    synchronized void cancelPullingAlarm() throws RemoteException;

    synchronized StatsLogEventWrapper[] pullData(int i) throws RemoteException;

    synchronized void sendDataBroadcast(IBinder iBinder, long j) throws RemoteException;

    synchronized void sendSubscriberBroadcast(IBinder iBinder, long j, long j2, long j3, long j4, String[] strArr, StatsDimensionsValue statsDimensionsValue) throws RemoteException;

    synchronized void setAlarmForSubscriberTriggering(long j) throws RemoteException;

    synchronized void setAnomalyAlarm(long j) throws RemoteException;

    synchronized void setPullingAlarm(long j) throws RemoteException;

    synchronized void statsdReady() throws RemoteException;

    synchronized void triggerUidSnapshot() throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IStatsCompanionService {
        private static final String DESCRIPTOR = "android.os.IStatsCompanionService";
        static final int TRANSACTION_cancelAlarmForSubscriberTriggering = 7;
        static final int TRANSACTION_cancelAnomalyAlarm = 3;
        static final int TRANSACTION_cancelPullingAlarm = 5;
        static final int TRANSACTION_pullData = 8;
        static final int TRANSACTION_sendDataBroadcast = 9;
        static final int TRANSACTION_sendSubscriberBroadcast = 10;
        static final int TRANSACTION_setAlarmForSubscriberTriggering = 6;
        static final int TRANSACTION_setAnomalyAlarm = 2;
        static final int TRANSACTION_setPullingAlarm = 4;
        static final int TRANSACTION_statsdReady = 1;
        static final int TRANSACTION_triggerUidSnapshot = 11;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IStatsCompanionService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IStatsCompanionService)) {
                return (IStatsCompanionService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        statsdReady();
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        long _arg0 = data.readLong();
                        setAnomalyAlarm(_arg0);
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        cancelAnomalyAlarm();
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        long _arg02 = data.readLong();
                        setPullingAlarm(_arg02);
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        cancelPullingAlarm();
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        long _arg03 = data.readLong();
                        setAlarmForSubscriberTriggering(_arg03);
                        return true;
                    case 7:
                        data.enforceInterface(DESCRIPTOR);
                        cancelAlarmForSubscriberTriggering();
                        return true;
                    case 8:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg04 = data.readInt();
                        StatsLogEventWrapper[] _result = pullData(_arg04);
                        reply.writeNoException();
                        reply.writeTypedArray(_result, 1);
                        return true;
                    case 9:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg05 = data.readStrongBinder();
                        long _arg1 = data.readLong();
                        sendDataBroadcast(_arg05, _arg1);
                        return true;
                    case 10:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg06 = data.readStrongBinder();
                        long _arg12 = data.readLong();
                        long _arg2 = data.readLong();
                        long _arg3 = data.readLong();
                        long _arg4 = data.readLong();
                        String[] _arg5 = data.createStringArray();
                        StatsDimensionsValue _arg6 = data.readInt() != 0 ? StatsDimensionsValue.CREATOR.createFromParcel(data) : null;
                        sendSubscriberBroadcast(_arg06, _arg12, _arg2, _arg3, _arg4, _arg5, _arg6);
                        return true;
                    case 11:
                        data.enforceInterface(DESCRIPTOR);
                        triggerUidSnapshot();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(DESCRIPTOR);
            return true;
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IStatsCompanionService {
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

            @Override // android.os.IStatsCompanionService
            public synchronized void statsdReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsCompanionService
            public synchronized void setAnomalyAlarm(long timestampMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(timestampMs);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsCompanionService
            public synchronized void cancelAnomalyAlarm() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsCompanionService
            public synchronized void setPullingAlarm(long nextPullTimeMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(nextPullTimeMs);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsCompanionService
            public synchronized void cancelPullingAlarm() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsCompanionService
            public synchronized void setAlarmForSubscriberTriggering(long timestampMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(timestampMs);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsCompanionService
            public synchronized void cancelAlarmForSubscriberTriggering() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsCompanionService
            public synchronized StatsLogEventWrapper[] pullData(int pullCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pullCode);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    StatsLogEventWrapper[] _result = (StatsLogEventWrapper[]) _reply.createTypedArray(StatsLogEventWrapper.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsCompanionService
            public synchronized void sendDataBroadcast(IBinder intentSender, long lastReportTimeNs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(intentSender);
                    _data.writeLong(lastReportTimeNs);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IStatsCompanionService
            public synchronized void sendSubscriberBroadcast(IBinder intentSender, long configUid, long configId, long subscriptionId, long subscriptionRuleId, String[] cookies, StatsDimensionsValue dimensionsValue) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(intentSender);
                    try {
                        _data.writeLong(configUid);
                        try {
                            _data.writeLong(configId);
                            try {
                                _data.writeLong(subscriptionId);
                            } catch (Throwable th2) {
                                th = th2;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(subscriptionRuleId);
                        try {
                            _data.writeStringArray(cookies);
                            if (dimensionsValue != null) {
                                _data.writeInt(1);
                                dimensionsValue.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                this.mRemote.transact(10, _data, null, 1);
                                _data.recycle();
                            } catch (Throwable th5) {
                                th = th5;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.os.IStatsCompanionService
            public synchronized void triggerUidSnapshot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
