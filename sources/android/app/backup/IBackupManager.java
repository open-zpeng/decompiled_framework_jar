package android.app.backup;

import android.app.backup.IBackupManagerMonitor;
import android.app.backup.IBackupObserver;
import android.app.backup.IFullBackupRestoreObserver;
import android.app.backup.IRestoreSession;
import android.app.backup.ISelectBackupTransportCallback;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IBackupManager extends IInterface {
    private protected void acknowledgeFullBackupOrRestore(int i, boolean z, String str, String str2, IFullBackupRestoreObserver iFullBackupRestoreObserver) throws RemoteException;

    synchronized void adbBackup(ParcelFileDescriptor parcelFileDescriptor, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, String[] strArr) throws RemoteException;

    synchronized void adbRestore(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    synchronized void agentConnected(String str, IBinder iBinder) throws RemoteException;

    synchronized void agentDisconnected(String str) throws RemoteException;

    synchronized void backupNow() throws RemoteException;

    synchronized IRestoreSession beginRestoreSession(String str, String str2) throws RemoteException;

    synchronized void cancelBackups() throws RemoteException;

    private protected void clearBackupData(String str, String str2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void dataChanged(String str) throws RemoteException;

    synchronized String[] filterAppsEligibleForBackup(String[] strArr) throws RemoteException;

    synchronized void fullTransportBackup(String[] strArr) throws RemoteException;

    synchronized long getAvailableRestoreToken(String str) throws RemoteException;

    synchronized Intent getConfigurationIntent(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String getCurrentTransport() throws RemoteException;

    synchronized Intent getDataManagementIntent(String str) throws RemoteException;

    synchronized String getDataManagementLabel(String str) throws RemoteException;

    synchronized String getDestinationString(String str) throws RemoteException;

    synchronized String[] getTransportWhitelist() throws RemoteException;

    synchronized boolean hasBackupPassword() throws RemoteException;

    synchronized void initializeTransports(String[] strArr, IBackupObserver iBackupObserver) throws RemoteException;

    synchronized boolean isAppEligibleForBackup(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isBackupEnabled() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isBackupServiceActive(int i) throws RemoteException;

    synchronized ComponentName[] listAllTransportComponents() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String[] listAllTransports() throws RemoteException;

    synchronized void opComplete(int i, long j) throws RemoteException;

    synchronized int requestBackup(String[] strArr, IBackupObserver iBackupObserver, IBackupManagerMonitor iBackupManagerMonitor, int i) throws RemoteException;

    synchronized void restoreAtInstall(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String selectBackupTransport(String str) throws RemoteException;

    synchronized void selectBackupTransportAsync(ComponentName componentName, ISelectBackupTransportCallback iSelectBackupTransportCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setAutoRestore(boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setBackupEnabled(boolean z) throws RemoteException;

    synchronized boolean setBackupPassword(String str, String str2) throws RemoteException;

    synchronized void setBackupProvisioned(boolean z) throws RemoteException;

    synchronized void setBackupServiceActive(int i, boolean z) throws RemoteException;

    synchronized void updateTransportAttributes(ComponentName componentName, String str, Intent intent, String str2, Intent intent2, String str3) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBackupManager {
        private static final String DESCRIPTOR = "android.app.backup.IBackupManager";
        static final int TRANSACTION_acknowledgeFullBackupOrRestore = 17;
        static final int TRANSACTION_adbBackup = 14;
        static final int TRANSACTION_adbRestore = 16;
        static final int TRANSACTION_agentConnected = 4;
        static final int TRANSACTION_agentDisconnected = 5;
        static final int TRANSACTION_backupNow = 13;
        static final int TRANSACTION_beginRestoreSession = 29;
        static final int TRANSACTION_cancelBackups = 37;
        static final int TRANSACTION_clearBackupData = 2;
        static final int TRANSACTION_dataChanged = 1;
        static final int TRANSACTION_filterAppsEligibleForBackup = 35;
        static final int TRANSACTION_fullTransportBackup = 15;
        static final int TRANSACTION_getAvailableRestoreToken = 33;
        static final int TRANSACTION_getConfigurationIntent = 25;
        static final int TRANSACTION_getCurrentTransport = 19;
        static final int TRANSACTION_getDataManagementIntent = 27;
        static final int TRANSACTION_getDataManagementLabel = 28;
        static final int TRANSACTION_getDestinationString = 26;
        static final int TRANSACTION_getTransportWhitelist = 22;
        static final int TRANSACTION_hasBackupPassword = 12;
        static final int TRANSACTION_initializeTransports = 3;
        static final int TRANSACTION_isAppEligibleForBackup = 34;
        static final int TRANSACTION_isBackupEnabled = 10;
        static final int TRANSACTION_isBackupServiceActive = 32;
        static final int TRANSACTION_listAllTransportComponents = 21;
        static final int TRANSACTION_listAllTransports = 20;
        static final int TRANSACTION_opComplete = 30;
        static final int TRANSACTION_requestBackup = 36;
        static final int TRANSACTION_restoreAtInstall = 6;
        static final int TRANSACTION_selectBackupTransport = 23;
        static final int TRANSACTION_selectBackupTransportAsync = 24;
        static final int TRANSACTION_setAutoRestore = 8;
        static final int TRANSACTION_setBackupEnabled = 7;
        static final int TRANSACTION_setBackupPassword = 11;
        static final int TRANSACTION_setBackupProvisioned = 9;
        static final int TRANSACTION_setBackupServiceActive = 31;
        static final int TRANSACTION_updateTransportAttributes = 18;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IBackupManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBackupManager)) {
                return (IBackupManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg8;
            ComponentName _arg0;
            Intent _arg2;
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg02 = data.readString();
                        dataChanged(_arg02);
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg03 = data.readString();
                        String _arg1 = data.readString();
                        clearBackupData(_arg03, _arg1);
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        String[] _arg04 = data.createStringArray();
                        IBackupObserver _arg12 = IBackupObserver.Stub.asInterface(data.readStrongBinder());
                        initializeTransports(_arg04, _arg12);
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg05 = data.readString();
                        IBinder _arg13 = data.readStrongBinder();
                        agentConnected(_arg05, _arg13);
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg06 = data.readString();
                        agentDisconnected(_arg06);
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg07 = data.readString();
                        int _arg14 = data.readInt();
                        restoreAtInstall(_arg07, _arg14);
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(DESCRIPTOR);
                        _arg8 = data.readInt() != 0;
                        boolean _arg08 = _arg8;
                        setBackupEnabled(_arg08);
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(DESCRIPTOR);
                        _arg8 = data.readInt() != 0;
                        boolean _arg09 = _arg8;
                        setAutoRestore(_arg09);
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(DESCRIPTOR);
                        _arg8 = data.readInt() != 0;
                        boolean _arg010 = _arg8;
                        setBackupProvisioned(_arg010);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(DESCRIPTOR);
                        boolean isBackupEnabled = isBackupEnabled();
                        reply.writeNoException();
                        reply.writeInt(isBackupEnabled ? 1 : 0);
                        return true;
                    case 11:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg011 = data.readString();
                        String _arg15 = data.readString();
                        boolean backupPassword = setBackupPassword(_arg011, _arg15);
                        reply.writeNoException();
                        reply.writeInt(backupPassword ? 1 : 0);
                        return true;
                    case 12:
                        data.enforceInterface(DESCRIPTOR);
                        boolean hasBackupPassword = hasBackupPassword();
                        reply.writeNoException();
                        reply.writeInt(hasBackupPassword ? 1 : 0);
                        return true;
                    case 13:
                        data.enforceInterface(DESCRIPTOR);
                        backupNow();
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(DESCRIPTOR);
                        ParcelFileDescriptor _arg012 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                        boolean _arg16 = data.readInt() != 0;
                        boolean _arg22 = data.readInt() != 0;
                        boolean _arg3 = data.readInt() != 0;
                        boolean _arg4 = data.readInt() != 0;
                        boolean _arg5 = data.readInt() != 0;
                        boolean _arg6 = data.readInt() != 0;
                        boolean _arg7 = data.readInt() != 0;
                        _arg8 = data.readInt() != 0;
                        String[] _arg9 = data.createStringArray();
                        adbBackup(_arg012, _arg16, _arg22, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9);
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(DESCRIPTOR);
                        String[] _arg013 = data.createStringArray();
                        fullTransportBackup(_arg013);
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(DESCRIPTOR);
                        ParcelFileDescriptor _arg014 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                        adbRestore(_arg014);
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg015 = data.readInt();
                        boolean _arg17 = data.readInt() != 0;
                        String _arg23 = data.readString();
                        String _arg32 = data.readString();
                        IFullBackupRestoreObserver _arg42 = IFullBackupRestoreObserver.Stub.asInterface(data.readStrongBinder());
                        acknowledgeFullBackupOrRestore(_arg015, _arg17, _arg23, _arg32, _arg42);
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg0 = ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        String _arg18 = data.readString();
                        if (data.readInt() != 0) {
                            Intent _arg24 = Intent.CREATOR.createFromParcel(data);
                            _arg2 = _arg24;
                        } else {
                            _arg2 = null;
                        }
                        String _arg33 = data.readString();
                        Intent _arg43 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                        String _arg52 = data.readString();
                        updateTransportAttributes(_arg0, _arg18, _arg2, _arg33, _arg43, _arg52);
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(DESCRIPTOR);
                        String _result = getCurrentTransport();
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 20:
                        data.enforceInterface(DESCRIPTOR);
                        String[] _result2 = listAllTransports();
                        reply.writeNoException();
                        reply.writeStringArray(_result2);
                        return true;
                    case 21:
                        data.enforceInterface(DESCRIPTOR);
                        ComponentName[] _result3 = listAllTransportComponents();
                        reply.writeNoException();
                        reply.writeTypedArray(_result3, 1);
                        return true;
                    case 22:
                        data.enforceInterface(DESCRIPTOR);
                        String[] _result4 = getTransportWhitelist();
                        reply.writeNoException();
                        reply.writeStringArray(_result4);
                        return true;
                    case 23:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg016 = data.readString();
                        String _result5 = selectBackupTransport(_arg016);
                        reply.writeNoException();
                        reply.writeString(_result5);
                        return true;
                    case 24:
                        data.enforceInterface(DESCRIPTOR);
                        ComponentName _arg017 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                        ISelectBackupTransportCallback _arg19 = ISelectBackupTransportCallback.Stub.asInterface(data.readStrongBinder());
                        selectBackupTransportAsync(_arg017, _arg19);
                        reply.writeNoException();
                        return true;
                    case 25:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg018 = data.readString();
                        Intent _result6 = getConfigurationIntent(_arg018);
                        reply.writeNoException();
                        if (_result6 != null) {
                            reply.writeInt(1);
                            _result6.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 26:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg019 = data.readString();
                        String _result7 = getDestinationString(_arg019);
                        reply.writeNoException();
                        reply.writeString(_result7);
                        return true;
                    case 27:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg020 = data.readString();
                        Intent _result8 = getDataManagementIntent(_arg020);
                        reply.writeNoException();
                        if (_result8 != null) {
                            reply.writeInt(1);
                            _result8.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 28:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg021 = data.readString();
                        String _result9 = getDataManagementLabel(_arg021);
                        reply.writeNoException();
                        reply.writeString(_result9);
                        return true;
                    case 29:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg022 = data.readString();
                        String _arg110 = data.readString();
                        IRestoreSession _result10 = beginRestoreSession(_arg022, _arg110);
                        reply.writeNoException();
                        reply.writeStrongBinder(_result10 != null ? _result10.asBinder() : null);
                        return true;
                    case 30:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg023 = data.readInt();
                        long _arg111 = data.readLong();
                        opComplete(_arg023, _arg111);
                        reply.writeNoException();
                        return true;
                    case 31:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg024 = data.readInt();
                        _arg8 = data.readInt() != 0;
                        boolean _arg112 = _arg8;
                        setBackupServiceActive(_arg024, _arg112);
                        reply.writeNoException();
                        return true;
                    case 32:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg025 = data.readInt();
                        boolean isBackupServiceActive = isBackupServiceActive(_arg025);
                        reply.writeNoException();
                        reply.writeInt(isBackupServiceActive ? 1 : 0);
                        return true;
                    case 33:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg026 = data.readString();
                        long _result11 = getAvailableRestoreToken(_arg026);
                        reply.writeNoException();
                        reply.writeLong(_result11);
                        return true;
                    case 34:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg027 = data.readString();
                        boolean isAppEligibleForBackup = isAppEligibleForBackup(_arg027);
                        reply.writeNoException();
                        reply.writeInt(isAppEligibleForBackup ? 1 : 0);
                        return true;
                    case 35:
                        data.enforceInterface(DESCRIPTOR);
                        String[] _arg028 = data.createStringArray();
                        String[] _result12 = filterAppsEligibleForBackup(_arg028);
                        reply.writeNoException();
                        reply.writeStringArray(_result12);
                        return true;
                    case 36:
                        data.enforceInterface(DESCRIPTOR);
                        String[] _arg029 = data.createStringArray();
                        IBackupObserver _arg113 = IBackupObserver.Stub.asInterface(data.readStrongBinder());
                        IBackupManagerMonitor _arg25 = IBackupManagerMonitor.Stub.asInterface(data.readStrongBinder());
                        int _arg34 = data.readInt();
                        int _result13 = requestBackup(_arg029, _arg113, _arg25, _arg34);
                        reply.writeNoException();
                        reply.writeInt(_result13);
                        return true;
                    case 37:
                        data.enforceInterface(DESCRIPTOR);
                        cancelBackups();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(DESCRIPTOR);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBackupManager {
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

            public synchronized void dataChanged(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void clearBackupData(String transportName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transportName);
                    _data.writeString(packageName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void initializeTransports(String[] transportNames, IBackupObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(transportNames);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void agentConnected(String packageName, IBinder agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(agent);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void agentDisconnected(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void restoreAtInstall(String packageName, int token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(token);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setBackupEnabled(boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isEnabled ? 1 : 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setAutoRestore(boolean doAutoRestore) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(doAutoRestore ? 1 : 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void setBackupProvisioned(boolean isProvisioned) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isProvisioned ? 1 : 0);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isBackupEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized boolean setBackupPassword(String currentPw, String newPw) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(currentPw);
                    _data.writeString(newPw);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized boolean hasBackupPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void backupNow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void adbBackup(ParcelFileDescriptor fd, boolean includeApks, boolean includeObbs, boolean includeShared, boolean doWidgets, boolean allApps, boolean allIncludesSystem, boolean doCompress, boolean doKeyValue, String[] packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(includeApks ? 1 : 0);
                    _data.writeInt(includeObbs ? 1 : 0);
                    _data.writeInt(includeShared ? 1 : 0);
                    _data.writeInt(doWidgets ? 1 : 0);
                    _data.writeInt(allApps ? 1 : 0);
                    _data.writeInt(allIncludesSystem ? 1 : 0);
                    _data.writeInt(doCompress ? 1 : 0);
                    _data.writeInt(doKeyValue ? 1 : 0);
                    _data.writeStringArray(packageNames);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void fullTransportBackup(String[] packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void adbRestore(ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
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

            public synchronized void acknowledgeFullBackupOrRestore(int token, boolean allow, String curPassword, String encryptionPassword, IFullBackupRestoreObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(allow ? 1 : 0);
                    _data.writeString(curPassword);
                    _data.writeString(encryptionPassword);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void updateTransportAttributes(ComponentName transportComponent, String name, Intent configurationIntent, String currentDestinationString, Intent dataManagementIntent, String dataManagementLabel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (transportComponent != null) {
                        _data.writeInt(1);
                        transportComponent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(name);
                    if (configurationIntent != null) {
                        _data.writeInt(1);
                        configurationIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(currentDestinationString);
                    if (dataManagementIntent != null) {
                        _data.writeInt(1);
                        dataManagementIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(dataManagementLabel);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getCurrentTransport() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String[] listAllTransports() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized ComponentName[] listAllTransportComponents() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    ComponentName[] _result = (ComponentName[]) _reply.createTypedArray(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized String[] getTransportWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String selectBackupTransport(String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void selectBackupTransportAsync(ComponentName transport, ISelectBackupTransportCallback listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (transport != null) {
                        _data.writeInt(1);
                        transport.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized Intent getConfigurationIntent(String transport) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized String getDestinationString(String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized Intent getDataManagementIntent(String transport) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized String getDataManagementLabel(String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized IRestoreSession beginRestoreSession(String packageName, String transportID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(transportID);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    IRestoreSession _result = IRestoreSession.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void opComplete(int token, long result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeLong(result);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void setBackupServiceActive(int whichUser, boolean makeActive) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(whichUser);
                    _data.writeInt(makeActive ? 1 : 0);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isBackupServiceActive(int whichUser) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(whichUser);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized long getAvailableRestoreToken(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized boolean isAppEligibleForBackup(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized String[] filterAppsEligibleForBackup(String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packages);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized int requestBackup(String[] packages, IBackupObserver observer, IBackupManagerMonitor monitor, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packages);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeStrongBinder(monitor != null ? monitor.asBinder() : null);
                    _data.writeInt(flags);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public synchronized void cancelBackups() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
