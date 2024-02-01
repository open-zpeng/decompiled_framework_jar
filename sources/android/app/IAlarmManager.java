package android.app;

import android.app.AlarmManager;
import android.app.IAlarmListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.WorkSource;
/* loaded from: classes.dex */
public interface IAlarmManager extends IInterface {
    synchronized long currentNetworkTimeMillis() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    AlarmManager.AlarmClockInfo getNextAlarmClock(int i) throws RemoteException;

    synchronized long getNextWakeFromIdleTime() throws RemoteException;

    synchronized void remove(PendingIntent pendingIntent, IAlarmListener iAlarmListener) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void set(String str, int i, long j, long j2, long j3, int i2, PendingIntent pendingIntent, IAlarmListener iAlarmListener, String str2, WorkSource workSource, AlarmManager.AlarmClockInfo alarmClockInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean setTime(long j) throws RemoteException;

    synchronized void setTimeZone(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IAlarmManager {
        private static final String DESCRIPTOR = "android.app.IAlarmManager";
        static final int TRANSACTION_currentNetworkTimeMillis = 7;
        static final int TRANSACTION_getNextAlarmClock = 6;
        static final int TRANSACTION_getNextWakeFromIdleTime = 5;
        public private protected static final int TRANSACTION_remove = 4;
        public private protected static final int TRANSACTION_set = 1;
        static final int TRANSACTION_setTime = 2;
        static final int TRANSACTION_setTimeZone = 3;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IAlarmManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAlarmManager)) {
                return (IAlarmManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            PendingIntent _arg6;
            WorkSource _arg9;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    long _arg2 = data.readLong();
                    long _arg3 = data.readLong();
                    long _arg4 = data.readLong();
                    int _arg5 = data.readInt();
                    if (data.readInt() != 0) {
                        PendingIntent _arg62 = PendingIntent.CREATOR.createFromParcel(data);
                        _arg6 = _arg62;
                    } else {
                        _arg6 = null;
                    }
                    IAlarmListener _arg7 = IAlarmListener.Stub.asInterface(data.readStrongBinder());
                    String _arg8 = data.readString();
                    if (data.readInt() != 0) {
                        WorkSource _arg92 = WorkSource.CREATOR.createFromParcel(data);
                        _arg9 = _arg92;
                    } else {
                        _arg9 = null;
                    }
                    AlarmManager.AlarmClockInfo _arg10 = data.readInt() != 0 ? AlarmManager.AlarmClockInfo.CREATOR.createFromParcel(data) : null;
                    set(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg02 = data.readLong();
                    boolean time = setTime(_arg02);
                    reply.writeNoException();
                    reply.writeInt(time ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    setTimeZone(_arg03);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    PendingIntent _arg04 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    IAlarmListener _arg12 = IAlarmListener.Stub.asInterface(data.readStrongBinder());
                    remove(_arg04, _arg12);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    long _result = getNextWakeFromIdleTime();
                    reply.writeNoException();
                    reply.writeLong(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    AlarmManager.AlarmClockInfo _result2 = getNextAlarmClock(_arg05);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    long _result3 = currentNetworkTimeMillis();
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IAlarmManager {
            private IBinder mRemote;

            public private protected Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public synchronized void set(String callingPackage, int type, long triggerAtTime, long windowLength, long interval, int flags, PendingIntent operation, IAlarmListener listener, String listenerTag, WorkSource workSource, AlarmManager.AlarmClockInfo alarmClock) throws RemoteException {
                int i;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(callingPackage);
                    try {
                        _data.writeInt(type);
                        try {
                            _data.writeLong(triggerAtTime);
                            try {
                                _data.writeLong(windowLength);
                                try {
                                    _data.writeLong(interval);
                                    try {
                                        _data.writeInt(flags);
                                        if (operation != null) {
                                            _data.writeInt(1);
                                            operation.writeToParcel(_data, 0);
                                        } else {
                                            _data.writeInt(0);
                                        }
                                        _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                                        try {
                                            _data.writeString(listenerTag);
                                            if (workSource != null) {
                                                _data.writeInt(1);
                                                i = 0;
                                                workSource.writeToParcel(_data, 0);
                                            } else {
                                                i = 0;
                                                _data.writeInt(0);
                                            }
                                            if (alarmClock != null) {
                                                _data.writeInt(1);
                                                alarmClock.writeToParcel(_data, 0);
                                            } else {
                                                _data.writeInt(i);
                                            }
                                            this.mRemote.transact(1, _data, _reply, 0);
                                            _reply.readException();
                                            _reply.recycle();
                                            _data.recycle();
                                        } catch (Throwable th2) {
                                            th = th2;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    } catch (Throwable th3) {
                                        th = th3;
                                        _reply.recycle();
                                        _data.recycle();
                                        throw th;
                                    }
                                } catch (Throwable th4) {
                                    th = th4;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th5) {
                                th = th5;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public synchronized boolean setTime(long millis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(millis);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IAlarmManager
            public synchronized void setTimeZone(String zone) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(zone);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IAlarmManager
            public synchronized void remove(PendingIntent operation, IAlarmListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IAlarmManager
            public synchronized long getNextWakeFromIdleTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized AlarmManager.AlarmClockInfo getNextAlarmClock(int userId) throws RemoteException {
                AlarmManager.AlarmClockInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = AlarmManager.AlarmClockInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IAlarmManager
            public synchronized long currentNetworkTimeMillis() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
