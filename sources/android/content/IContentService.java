package android.content;

import android.accounts.Account;
import android.content.ISyncStatusObserver;
import android.database.IContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes.dex */
public interface IContentService extends IInterface {
    synchronized void addPeriodicSync(Account account, String str, Bundle bundle, long j) throws RemoteException;

    synchronized void addStatusChangeListener(int i, ISyncStatusObserver iSyncStatusObserver) throws RemoteException;

    synchronized void cancelRequest(SyncRequest syncRequest) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void cancelSync(Account account, String str, ComponentName componentName) throws RemoteException;

    synchronized void cancelSyncAsUser(Account account, String str, ComponentName componentName, int i) throws RemoteException;

    synchronized Bundle getCache(String str, Uri uri, int i) throws RemoteException;

    synchronized List<SyncInfo> getCurrentSyncs() throws RemoteException;

    synchronized List<SyncInfo> getCurrentSyncsAsUser(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getIsSyncable(Account account, String str) throws RemoteException;

    synchronized int getIsSyncableAsUser(Account account, String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean getMasterSyncAutomatically() throws RemoteException;

    synchronized boolean getMasterSyncAutomaticallyAsUser(int i) throws RemoteException;

    synchronized List<PeriodicSync> getPeriodicSyncs(Account account, String str, ComponentName componentName) throws RemoteException;

    synchronized String[] getSyncAdapterPackagesForAuthorityAsUser(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    SyncAdapterType[] getSyncAdapterTypes() throws RemoteException;

    synchronized SyncAdapterType[] getSyncAdapterTypesAsUser(int i) throws RemoteException;

    synchronized boolean getSyncAutomatically(Account account, String str) throws RemoteException;

    synchronized boolean getSyncAutomaticallyAsUser(Account account, String str, int i) throws RemoteException;

    synchronized SyncStatusInfo getSyncStatus(Account account, String str, ComponentName componentName) throws RemoteException;

    synchronized SyncStatusInfo getSyncStatusAsUser(Account account, String str, ComponentName componentName, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isSyncActive(Account account, String str, ComponentName componentName) throws RemoteException;

    synchronized boolean isSyncPending(Account account, String str, ComponentName componentName) throws RemoteException;

    synchronized boolean isSyncPendingAsUser(Account account, String str, ComponentName componentName, int i) throws RemoteException;

    synchronized void notifyChange(Uri uri, IContentObserver iContentObserver, boolean z, int i, int i2, int i3) throws RemoteException;

    synchronized void putCache(String str, Uri uri, Bundle bundle, int i) throws RemoteException;

    synchronized void registerContentObserver(Uri uri, boolean z, IContentObserver iContentObserver, int i, int i2) throws RemoteException;

    synchronized void removePeriodicSync(Account account, String str, Bundle bundle) throws RemoteException;

    synchronized void removeStatusChangeListener(ISyncStatusObserver iSyncStatusObserver) throws RemoteException;

    synchronized void requestSync(Account account, String str, Bundle bundle) throws RemoteException;

    synchronized void resetTodayStats() throws RemoteException;

    synchronized void setIsSyncable(Account account, String str, int i) throws RemoteException;

    private protected void setMasterSyncAutomatically(boolean z) throws RemoteException;

    synchronized void setMasterSyncAutomaticallyAsUser(boolean z, int i) throws RemoteException;

    synchronized void setSyncAutomatically(Account account, String str, boolean z) throws RemoteException;

    synchronized void setSyncAutomaticallyAsUser(Account account, String str, boolean z, int i) throws RemoteException;

    synchronized void sync(SyncRequest syncRequest) throws RemoteException;

    synchronized void syncAsUser(SyncRequest syncRequest, int i) throws RemoteException;

    synchronized void unregisterContentObserver(IContentObserver iContentObserver) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IContentService {
        private static final String DESCRIPTOR = "android.content.IContentService";
        static final int TRANSACTION_addPeriodicSync = 15;
        static final int TRANSACTION_addStatusChangeListener = 34;
        static final int TRANSACTION_cancelRequest = 9;
        static final int TRANSACTION_cancelSync = 7;
        static final int TRANSACTION_cancelSyncAsUser = 8;
        static final int TRANSACTION_getCache = 37;
        static final int TRANSACTION_getCurrentSyncs = 24;
        static final int TRANSACTION_getCurrentSyncsAsUser = 25;
        static final int TRANSACTION_getIsSyncable = 17;
        static final int TRANSACTION_getIsSyncableAsUser = 18;
        static final int TRANSACTION_getMasterSyncAutomatically = 22;
        static final int TRANSACTION_getMasterSyncAutomaticallyAsUser = 23;
        static final int TRANSACTION_getPeriodicSyncs = 14;
        static final int TRANSACTION_getSyncAdapterPackagesForAuthorityAsUser = 28;
        static final int TRANSACTION_getSyncAdapterTypes = 26;
        static final int TRANSACTION_getSyncAdapterTypesAsUser = 27;
        static final int TRANSACTION_getSyncAutomatically = 10;
        static final int TRANSACTION_getSyncAutomaticallyAsUser = 11;
        static final int TRANSACTION_getSyncStatus = 30;
        static final int TRANSACTION_getSyncStatusAsUser = 31;
        static final int TRANSACTION_isSyncActive = 29;
        static final int TRANSACTION_isSyncPending = 32;
        static final int TRANSACTION_isSyncPendingAsUser = 33;
        static final int TRANSACTION_notifyChange = 3;
        static final int TRANSACTION_putCache = 36;
        static final int TRANSACTION_registerContentObserver = 2;
        static final int TRANSACTION_removePeriodicSync = 16;
        static final int TRANSACTION_removeStatusChangeListener = 35;
        static final int TRANSACTION_requestSync = 4;
        static final int TRANSACTION_resetTodayStats = 38;
        static final int TRANSACTION_setIsSyncable = 19;
        static final int TRANSACTION_setMasterSyncAutomatically = 20;
        static final int TRANSACTION_setMasterSyncAutomaticallyAsUser = 21;
        static final int TRANSACTION_setSyncAutomatically = 12;
        static final int TRANSACTION_setSyncAutomaticallyAsUser = 13;
        static final int TRANSACTION_sync = 5;
        static final int TRANSACTION_syncAsUser = 6;
        static final int TRANSACTION_unregisterContentObserver = 1;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IContentService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IContentService)) {
                return (IContentService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Uri _arg0;
            Uri _arg02;
            Account _arg03;
            Account _arg04;
            Account _arg05;
            boolean _arg06;
            Account _arg07;
            Bundle _arg2;
            Account _arg08;
            Account _arg09;
            Account _arg010;
            Account _arg011;
            Account _arg012;
            Account _arg013;
            Uri _arg1;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterContentObserver(IContentObserver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    boolean _arg12 = data.readInt() != 0;
                    IContentObserver _arg22 = IContentObserver.Stub.asInterface(data.readStrongBinder());
                    int _arg3 = data.readInt();
                    int _arg4 = data.readInt();
                    registerContentObserver(_arg0, _arg12, _arg22, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    IContentObserver _arg13 = IContentObserver.Stub.asInterface(data.readStrongBinder());
                    boolean _arg23 = data.readInt() != 0;
                    int _arg32 = data.readInt();
                    int _arg42 = data.readInt();
                    int _arg5 = data.readInt();
                    notifyChange(_arg02, _arg13, _arg23, _arg32, _arg42, _arg5);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = Account.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    String _arg14 = data.readString();
                    Bundle _arg24 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    requestSync(_arg03, _arg14, _arg24);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    sync(data.readInt() != 0 ? SyncRequest.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    SyncRequest _arg014 = data.readInt() != 0 ? SyncRequest.CREATOR.createFromParcel(data) : null;
                    int _arg15 = data.readInt();
                    syncAsUser(_arg014, _arg15);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = Account.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    String _arg16 = data.readString();
                    ComponentName _arg25 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    cancelSync(_arg04, _arg16, _arg25);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = Account.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    String _arg17 = data.readString();
                    ComponentName _arg26 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg33 = data.readInt();
                    cancelSyncAsUser(_arg05, _arg17, _arg26, _arg33);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    cancelRequest(data.readInt() != 0 ? SyncRequest.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    Account _arg015 = data.readInt() != 0 ? Account.CREATOR.createFromParcel(data) : null;
                    String _arg18 = data.readString();
                    boolean syncAutomatically = getSyncAutomatically(_arg015, _arg18);
                    reply.writeNoException();
                    reply.writeInt(syncAutomatically ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    Account _arg016 = data.readInt() != 0 ? Account.CREATOR.createFromParcel(data) : null;
                    String _arg19 = data.readString();
                    int _arg27 = data.readInt();
                    boolean syncAutomaticallyAsUser = getSyncAutomaticallyAsUser(_arg016, _arg19, _arg27);
                    reply.writeNoException();
                    reply.writeInt(syncAutomaticallyAsUser ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    Account _arg017 = data.readInt() != 0 ? Account.CREATOR.createFromParcel(data) : null;
                    String _arg110 = data.readString();
                    _arg06 = data.readInt() != 0;
                    setSyncAutomatically(_arg017, _arg110, _arg06);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    Account _arg018 = data.readInt() != 0 ? Account.CREATOR.createFromParcel(data) : null;
                    String _arg111 = data.readString();
                    _arg06 = data.readInt() != 0;
                    int _arg34 = data.readInt();
                    setSyncAutomaticallyAsUser(_arg018, _arg111, _arg06, _arg34);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = Account.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    String _arg112 = data.readString();
                    ComponentName _arg28 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List<PeriodicSync> _result = getPeriodicSyncs(_arg07, _arg112, _arg28);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    Account _arg019 = data.readInt() != 0 ? Account.CREATOR.createFromParcel(data) : null;
                    String _arg113 = data.readString();
                    if (data.readInt() != 0) {
                        Bundle _arg29 = Bundle.CREATOR.createFromParcel(data);
                        _arg2 = _arg29;
                    } else {
                        _arg2 = null;
                    }
                    long _arg35 = data.readLong();
                    addPeriodicSync(_arg019, _arg113, _arg2, _arg35);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = Account.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    String _arg114 = data.readString();
                    Bundle _arg210 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    removePeriodicSync(_arg08, _arg114, _arg210);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    Account _arg020 = data.readInt() != 0 ? Account.CREATOR.createFromParcel(data) : null;
                    String _arg115 = data.readString();
                    int _result2 = getIsSyncable(_arg020, _arg115);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    Account _arg021 = data.readInt() != 0 ? Account.CREATOR.createFromParcel(data) : null;
                    String _arg116 = data.readString();
                    int _arg211 = data.readInt();
                    int _result3 = getIsSyncableAsUser(_arg021, _arg116, _arg211);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    Account _arg022 = data.readInt() != 0 ? Account.CREATOR.createFromParcel(data) : null;
                    String _arg117 = data.readString();
                    int _arg212 = data.readInt();
                    setIsSyncable(_arg022, _arg117, _arg212);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _arg06 = data.readInt() != 0;
                    setMasterSyncAutomatically(_arg06);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _arg06 = data.readInt() != 0;
                    int _arg118 = data.readInt();
                    setMasterSyncAutomaticallyAsUser(_arg06, _arg118);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    boolean masterSyncAutomatically = getMasterSyncAutomatically();
                    reply.writeNoException();
                    reply.writeInt(masterSyncAutomatically ? 1 : 0);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    boolean masterSyncAutomaticallyAsUser = getMasterSyncAutomaticallyAsUser(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(masterSyncAutomaticallyAsUser ? 1 : 0);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    List<SyncInfo> _result4 = getCurrentSyncs();
                    reply.writeNoException();
                    reply.writeTypedList(_result4);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    List<SyncInfo> _result5 = getCurrentSyncsAsUser(data.readInt());
                    reply.writeNoException();
                    reply.writeTypedList(_result5);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    SyncAdapterType[] _result6 = getSyncAdapterTypes();
                    reply.writeNoException();
                    reply.writeTypedArray(_result6, 1);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    SyncAdapterType[] _result7 = getSyncAdapterTypesAsUser(data.readInt());
                    reply.writeNoException();
                    reply.writeTypedArray(_result7, 1);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg023 = data.readString();
                    int _arg119 = data.readInt();
                    String[] _result8 = getSyncAdapterPackagesForAuthorityAsUser(_arg023, _arg119);
                    reply.writeNoException();
                    reply.writeStringArray(_result8);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg09 = Account.CREATOR.createFromParcel(data);
                    } else {
                        _arg09 = null;
                    }
                    String _arg120 = data.readString();
                    ComponentName _arg213 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isSyncActive = isSyncActive(_arg09, _arg120, _arg213);
                    reply.writeNoException();
                    reply.writeInt(isSyncActive ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg010 = Account.CREATOR.createFromParcel(data);
                    } else {
                        _arg010 = null;
                    }
                    String _arg121 = data.readString();
                    ComponentName _arg214 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    SyncStatusInfo _result9 = getSyncStatus(_arg010, _arg121, _arg214);
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg011 = Account.CREATOR.createFromParcel(data);
                    } else {
                        _arg011 = null;
                    }
                    String _arg122 = data.readString();
                    ComponentName _arg215 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg36 = data.readInt();
                    SyncStatusInfo _result10 = getSyncStatusAsUser(_arg011, _arg122, _arg215, _arg36);
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg012 = Account.CREATOR.createFromParcel(data);
                    } else {
                        _arg012 = null;
                    }
                    String _arg123 = data.readString();
                    ComponentName _arg216 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isSyncPending = isSyncPending(_arg012, _arg123, _arg216);
                    reply.writeNoException();
                    reply.writeInt(isSyncPending ? 1 : 0);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg013 = Account.CREATOR.createFromParcel(data);
                    } else {
                        _arg013 = null;
                    }
                    String _arg124 = data.readString();
                    ComponentName _arg217 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg37 = data.readInt();
                    boolean isSyncPendingAsUser = isSyncPendingAsUser(_arg013, _arg124, _arg217, _arg37);
                    reply.writeNoException();
                    reply.writeInt(isSyncPendingAsUser ? 1 : 0);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    ISyncStatusObserver _arg125 = ISyncStatusObserver.Stub.asInterface(data.readStrongBinder());
                    addStatusChangeListener(_arg024, _arg125);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    removeStatusChangeListener(ISyncStatusObserver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg025 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    Bundle _arg218 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    int _arg38 = data.readInt();
                    putCache(_arg025, _arg1, _arg218, _arg38);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg026 = data.readString();
                    Uri _arg126 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    int _arg219 = data.readInt();
                    Bundle _result11 = getCache(_arg026, _arg126, _arg219);
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    resetTodayStats();
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IContentService {
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

            @Override // android.content.IContentService
            public synchronized void unregisterContentObserver(IContentObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer, int userHandle, int targetSdkVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(notifyForDescendants ? 1 : 0);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userHandle);
                    _data.writeInt(targetSdkVersion);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, int flags, int userHandle, int targetSdkVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(observerWantsSelfNotifications ? 1 : 0);
                    _data.writeInt(flags);
                    _data.writeInt(userHandle);
                    _data.writeInt(targetSdkVersion);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void requestSync(Account account, String authority, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void sync(SyncRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void syncAsUser(SyncRequest request, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void cancelSync(Account account, String authority, ComponentName cname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void cancelSyncAsUser(Account account, String authority, ComponentName cname, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void cancelRequest(SyncRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized boolean getSyncAutomatically(Account account, String providerName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized boolean getSyncAutomaticallyAsUser(Account account, String providerName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    _data.writeInt(userId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void setSyncAutomatically(Account account, String providerName, boolean sync) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    _data.writeInt(sync ? 1 : 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void setSyncAutomaticallyAsUser(Account account, String providerName, boolean sync, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    _data.writeInt(sync ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized List<PeriodicSync> getPeriodicSyncs(Account account, String providerName, ComponentName cname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    List<PeriodicSync> _result = _reply.createTypedArrayList(PeriodicSync.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void addPeriodicSync(Account account, String providerName, Bundle extras, long pollFrequency) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(pollFrequency);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void removePeriodicSync(Account account, String providerName, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getIsSyncable(Account account, String providerName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized int getIsSyncableAsUser(Account account, String providerName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    _data.writeInt(userId);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void setIsSyncable(Account account, String providerName, int syncable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    _data.writeInt(syncable);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setMasterSyncAutomatically(boolean flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flag ? 1 : 0);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void setMasterSyncAutomaticallyAsUser(boolean flag, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flag ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean getMasterSyncAutomatically() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized boolean getMasterSyncAutomaticallyAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized List<SyncInfo> getCurrentSyncs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    List<SyncInfo> _result = _reply.createTypedArrayList(SyncInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized List<SyncInfo> getCurrentSyncsAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    List<SyncInfo> _result = _reply.createTypedArrayList(SyncInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized SyncAdapterType[] getSyncAdapterTypes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    SyncAdapterType[] _result = (SyncAdapterType[]) _reply.createTypedArray(SyncAdapterType.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized SyncAdapterType[] getSyncAdapterTypesAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    SyncAdapterType[] _result = (SyncAdapterType[]) _reply.createTypedArray(SyncAdapterType.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized String[] getSyncAdapterPackagesForAuthorityAsUser(String authority, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(authority);
                    _data.writeInt(userId);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isSyncActive(Account account, String authority, ComponentName cname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized SyncStatusInfo getSyncStatus(Account account, String authority, ComponentName cname) throws RemoteException {
                SyncStatusInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SyncStatusInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized SyncStatusInfo getSyncStatusAsUser(Account account, String authority, ComponentName cname, int userId) throws RemoteException {
                SyncStatusInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SyncStatusInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized boolean isSyncPending(Account account, String authority, ComponentName cname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized boolean isSyncPendingAsUser(Account account, String authority, ComponentName cname, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void addStatusChangeListener(int mask, ISyncStatusObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mask);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void removeStatusChangeListener(ISyncStatusObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void putCache(String packageName, Uri key, Bundle value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (key != null) {
                        _data.writeInt(1);
                        key.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (value != null) {
                        _data.writeInt(1);
                        value.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized Bundle getCache(String packageName, Uri key, int userId) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (key != null) {
                        _data.writeInt(1);
                        key.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.IContentService
            public synchronized void resetTodayStats() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
