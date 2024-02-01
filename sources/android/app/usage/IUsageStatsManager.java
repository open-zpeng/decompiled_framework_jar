package android.app.usage;

import android.app.PendingIntent;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IUsageStatsManager extends IInterface {
    synchronized int getAppStandbyBucket(String str, String str2, int i) throws RemoteException;

    synchronized ParceledListSlice getAppStandbyBuckets(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isAppInactive(String str, int i) throws RemoteException;

    synchronized void onCarrierPrivilegedAppsChanged() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ParceledListSlice queryConfigurationStats(int i, long j, long j2, String str) throws RemoteException;

    synchronized ParceledListSlice queryEventStats(int i, long j, long j2, String str) throws RemoteException;

    synchronized UsageEvents queryEvents(long j, long j2, String str) throws RemoteException;

    synchronized UsageEvents queryEventsForPackage(long j, long j2, String str) throws RemoteException;

    synchronized UsageEvents queryEventsForPackageForUser(long j, long j2, int i, String str, String str2) throws RemoteException;

    synchronized UsageEvents queryEventsForUser(long j, long j2, int i, String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ParceledListSlice queryUsageStats(int i, long j, long j2, String str) throws RemoteException;

    synchronized void registerAppUsageObserver(int i, String[] strArr, long j, PendingIntent pendingIntent, String str) throws RemoteException;

    synchronized void reportChooserSelection(String str, int i, String str2, String[] strArr, String str3) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setAppInactive(String str, boolean z, int i) throws RemoteException;

    synchronized void setAppStandbyBucket(String str, int i, int i2) throws RemoteException;

    synchronized void setAppStandbyBuckets(ParceledListSlice parceledListSlice, int i) throws RemoteException;

    synchronized void unregisterAppUsageObserver(int i, String str) throws RemoteException;

    synchronized void whitelistAppTemporarily(String str, long j, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IUsageStatsManager {
        private static final String DESCRIPTOR = "android.app.usage.IUsageStatsManager";
        static final int TRANSACTION_getAppStandbyBucket = 13;
        static final int TRANSACTION_getAppStandbyBuckets = 15;
        static final int TRANSACTION_isAppInactive = 9;
        static final int TRANSACTION_onCarrierPrivilegedAppsChanged = 11;
        static final int TRANSACTION_queryConfigurationStats = 2;
        static final int TRANSACTION_queryEventStats = 3;
        static final int TRANSACTION_queryEvents = 4;
        static final int TRANSACTION_queryEventsForPackage = 5;
        static final int TRANSACTION_queryEventsForPackageForUser = 7;
        static final int TRANSACTION_queryEventsForUser = 6;
        static final int TRANSACTION_queryUsageStats = 1;
        static final int TRANSACTION_registerAppUsageObserver = 17;
        static final int TRANSACTION_reportChooserSelection = 12;
        static final int TRANSACTION_setAppInactive = 8;
        static final int TRANSACTION_setAppStandbyBucket = 14;
        static final int TRANSACTION_setAppStandbyBuckets = 16;
        static final int TRANSACTION_unregisterAppUsageObserver = 18;
        static final int TRANSACTION_whitelistAppTemporarily = 10;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IUsageStatsManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IUsageStatsManager)) {
                return (IUsageStatsManager) iin;
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
                    int _arg0 = data.readInt();
                    long _arg1 = data.readLong();
                    long _arg2 = data.readLong();
                    String _arg3 = data.readString();
                    ParceledListSlice _result = queryUsageStats(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    long _arg12 = data.readLong();
                    long _arg22 = data.readLong();
                    String _arg32 = data.readString();
                    ParceledListSlice _result2 = queryConfigurationStats(_arg02, _arg12, _arg22, _arg32);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    long _arg13 = data.readLong();
                    long _arg23 = data.readLong();
                    String _arg33 = data.readString();
                    ParceledListSlice _result3 = queryEventStats(_arg03, _arg13, _arg23, _arg33);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg04 = data.readLong();
                    long _arg14 = data.readLong();
                    String _arg24 = data.readString();
                    UsageEvents _result4 = queryEvents(_arg04, _arg14, _arg24);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg05 = data.readLong();
                    long _arg15 = data.readLong();
                    String _arg25 = data.readString();
                    UsageEvents _result5 = queryEventsForPackage(_arg05, _arg15, _arg25);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg06 = data.readLong();
                    long _arg16 = data.readLong();
                    int _arg26 = data.readInt();
                    String _arg34 = data.readString();
                    UsageEvents _result6 = queryEventsForUser(_arg06, _arg16, _arg26, _arg34);
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg07 = data.readLong();
                    long _arg17 = data.readLong();
                    int _arg27 = data.readInt();
                    String _arg35 = data.readString();
                    String _arg4 = data.readString();
                    UsageEvents _result7 = queryEventsForPackageForUser(_arg07, _arg17, _arg27, _arg35, _arg4);
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    boolean _arg18 = data.readInt() != 0;
                    int _arg28 = data.readInt();
                    setAppInactive(_arg08, _arg18, _arg28);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    int _arg19 = data.readInt();
                    boolean isAppInactive = isAppInactive(_arg09, _arg19);
                    reply.writeNoException();
                    reply.writeInt(isAppInactive ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    long _arg110 = data.readLong();
                    int _arg29 = data.readInt();
                    whitelistAppTemporarily(_arg010, _arg110, _arg29);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    onCarrierPrivilegedAppsChanged();
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    int _arg111 = data.readInt();
                    String _arg210 = data.readString();
                    String[] _arg36 = data.createStringArray();
                    String _arg42 = data.readString();
                    reportChooserSelection(_arg011, _arg111, _arg210, _arg36, _arg42);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    String _arg112 = data.readString();
                    int _arg211 = data.readInt();
                    int _result8 = getAppStandbyBucket(_arg012, _arg112, _arg211);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    int _arg113 = data.readInt();
                    int _arg212 = data.readInt();
                    setAppStandbyBucket(_arg013, _arg113, _arg212);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    int _arg114 = data.readInt();
                    ParceledListSlice _result9 = getAppStandbyBuckets(_arg014, _arg114);
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    ParceledListSlice _arg015 = data.readInt() != 0 ? ParceledListSlice.CREATOR.createFromParcel(data) : null;
                    int _arg115 = data.readInt();
                    setAppStandbyBuckets(_arg015, _arg115);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    String[] _arg116 = data.createStringArray();
                    long _arg213 = data.readLong();
                    PendingIntent _arg37 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    String _arg43 = data.readString();
                    registerAppUsageObserver(_arg016, _arg116, _arg213, _arg37, _arg43);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    String _arg117 = data.readString();
                    unregisterAppUsageObserver(_arg017, _arg117);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IUsageStatsManager {
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

            public synchronized ParceledListSlice queryUsageStats(int bucketType, long beginTime, long endTime, String callingPackage) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bucketType);
                    _data.writeLong(beginTime);
                    _data.writeLong(endTime);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized ParceledListSlice queryConfigurationStats(int bucketType, long beginTime, long endTime, String callingPackage) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bucketType);
                    _data.writeLong(beginTime);
                    _data.writeLong(endTime);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized ParceledListSlice queryEventStats(int bucketType, long beginTime, long endTime, String callingPackage) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bucketType);
                    _data.writeLong(beginTime);
                    _data.writeLong(endTime);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized UsageEvents queryEvents(long beginTime, long endTime, String callingPackage) throws RemoteException {
                UsageEvents _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(beginTime);
                    _data.writeLong(endTime);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = UsageEvents.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized UsageEvents queryEventsForPackage(long beginTime, long endTime, String callingPackage) throws RemoteException {
                UsageEvents _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(beginTime);
                    _data.writeLong(endTime);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = UsageEvents.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized UsageEvents queryEventsForUser(long beginTime, long endTime, int userId, String callingPackage) throws RemoteException {
                UsageEvents _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(beginTime);
                    _data.writeLong(endTime);
                    _data.writeInt(userId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = UsageEvents.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized UsageEvents queryEventsForPackageForUser(long beginTime, long endTime, int userId, String pkg, String callingPackage) throws RemoteException {
                UsageEvents _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(beginTime);
                    _data.writeLong(endTime);
                    _data.writeInt(userId);
                    _data.writeString(pkg);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = UsageEvents.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setAppInactive(String packageName, boolean inactive, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(inactive ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isAppInactive(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized void whitelistAppTemporarily(String packageName, long duration, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeLong(duration);
                    _data.writeInt(userId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized void onCarrierPrivilegedAppsChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized void reportChooserSelection(String packageName, int userId, String contentType, String[] annotations, String action) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(contentType);
                    _data.writeStringArray(annotations);
                    _data.writeString(action);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized int getAppStandbyBucket(String packageName, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized void setAppStandbyBucket(String packageName, int bucket, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(bucket);
                    _data.writeInt(userId);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized ParceledListSlice getAppStandbyBuckets(String callingPackage, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized void setAppStandbyBuckets(ParceledListSlice appBuckets, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appBuckets != null) {
                        _data.writeInt(1);
                        appBuckets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized void registerAppUsageObserver(int observerId, String[] packages, long timeLimitMs, PendingIntent callback, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(observerId);
                    _data.writeStringArray(packages);
                    _data.writeLong(timeLimitMs);
                    if (callback != null) {
                        _data.writeInt(1);
                        callback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.usage.IUsageStatsManager
            public synchronized void unregisterAppUsageObserver(int observerId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(observerId);
                    _data.writeString(callingPackage);
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
