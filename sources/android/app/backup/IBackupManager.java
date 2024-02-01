package android.app.backup;

import android.annotation.UnsupportedAppUsage;
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
import android.os.UserHandle;
import android.text.TextUtils;

/* loaded from: classes.dex */
public interface IBackupManager extends IInterface {
    @UnsupportedAppUsage
    void acknowledgeFullBackupOrRestore(int i, boolean z, String str, String str2, IFullBackupRestoreObserver iFullBackupRestoreObserver) throws RemoteException;

    void acknowledgeFullBackupOrRestoreForUser(int i, int i2, boolean z, String str, String str2, IFullBackupRestoreObserver iFullBackupRestoreObserver) throws RemoteException;

    void adbBackup(int i, ParcelFileDescriptor parcelFileDescriptor, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, String[] strArr) throws RemoteException;

    void adbRestore(int i, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void agentConnected(String str, IBinder iBinder) throws RemoteException;

    void agentConnectedForUser(int i, String str, IBinder iBinder) throws RemoteException;

    void agentDisconnected(String str) throws RemoteException;

    void agentDisconnectedForUser(int i, String str) throws RemoteException;

    void backupNow() throws RemoteException;

    void backupNowForUser(int i) throws RemoteException;

    IRestoreSession beginRestoreSessionForUser(int i, String str, String str2) throws RemoteException;

    void cancelBackups() throws RemoteException;

    void cancelBackupsForUser(int i) throws RemoteException;

    @UnsupportedAppUsage
    void clearBackupData(String str, String str2) throws RemoteException;

    void clearBackupDataForUser(int i, String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void dataChanged(String str) throws RemoteException;

    void dataChangedForUser(int i, String str) throws RemoteException;

    String[] filterAppsEligibleForBackupForUser(int i, String[] strArr) throws RemoteException;

    void fullTransportBackupForUser(int i, String[] strArr) throws RemoteException;

    long getAvailableRestoreTokenForUser(int i, String str) throws RemoteException;

    Intent getConfigurationIntent(String str) throws RemoteException;

    Intent getConfigurationIntentForUser(int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    String getCurrentTransport() throws RemoteException;

    ComponentName getCurrentTransportComponentForUser(int i) throws RemoteException;

    String getCurrentTransportForUser(int i) throws RemoteException;

    Intent getDataManagementIntent(String str) throws RemoteException;

    Intent getDataManagementIntentForUser(int i, String str) throws RemoteException;

    CharSequence getDataManagementLabelForUser(int i, String str) throws RemoteException;

    String getDestinationString(String str) throws RemoteException;

    String getDestinationStringForUser(int i, String str) throws RemoteException;

    String[] getTransportWhitelist() throws RemoteException;

    UserHandle getUserForAncestralSerialNumber(long j) throws RemoteException;

    boolean hasBackupPassword() throws RemoteException;

    void initializeTransportsForUser(int i, String[] strArr, IBackupObserver iBackupObserver) throws RemoteException;

    boolean isAppEligibleForBackupForUser(int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean isBackupEnabled() throws RemoteException;

    boolean isBackupEnabledForUser(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean isBackupServiceActive(int i) throws RemoteException;

    ComponentName[] listAllTransportComponentsForUser(int i) throws RemoteException;

    @UnsupportedAppUsage
    String[] listAllTransports() throws RemoteException;

    String[] listAllTransportsForUser(int i) throws RemoteException;

    void opComplete(int i, long j) throws RemoteException;

    void opCompleteForUser(int i, int i2, long j) throws RemoteException;

    int requestBackup(String[] strArr, IBackupObserver iBackupObserver, IBackupManagerMonitor iBackupManagerMonitor, int i) throws RemoteException;

    int requestBackupForUser(int i, String[] strArr, IBackupObserver iBackupObserver, IBackupManagerMonitor iBackupManagerMonitor, int i2) throws RemoteException;

    void restoreAtInstall(String str, int i) throws RemoteException;

    void restoreAtInstallForUser(int i, String str, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String selectBackupTransport(String str) throws RemoteException;

    void selectBackupTransportAsyncForUser(int i, ComponentName componentName, ISelectBackupTransportCallback iSelectBackupTransportCallback) throws RemoteException;

    String selectBackupTransportForUser(int i, String str) throws RemoteException;

    void setAncestralSerialNumber(long j) throws RemoteException;

    @UnsupportedAppUsage
    void setAutoRestore(boolean z) throws RemoteException;

    void setAutoRestoreForUser(int i, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setBackupEnabled(boolean z) throws RemoteException;

    void setBackupEnabledForUser(int i, boolean z) throws RemoteException;

    boolean setBackupPassword(String str, String str2) throws RemoteException;

    void setBackupServiceActive(int i, boolean z) throws RemoteException;

    void updateTransportAttributesForUser(int i, ComponentName componentName, String str, Intent intent, String str2, Intent intent2, CharSequence charSequence) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBackupManager {
        @Override // android.app.backup.IBackupManager
        public void dataChangedForUser(int userId, String packageName) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void dataChanged(String packageName) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void clearBackupDataForUser(int userId, String transportName, String packageName) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void clearBackupData(String transportName, String packageName) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void initializeTransportsForUser(int userId, String[] transportNames, IBackupObserver observer) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void agentConnectedForUser(int userId, String packageName, IBinder agent) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void agentConnected(String packageName, IBinder agent) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void agentDisconnectedForUser(int userId, String packageName) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void agentDisconnected(String packageName) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void restoreAtInstallForUser(int userId, String packageName, int token) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void restoreAtInstall(String packageName, int token) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void setBackupEnabledForUser(int userId, boolean isEnabled) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void setBackupEnabled(boolean isEnabled) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void setAutoRestoreForUser(int userId, boolean doAutoRestore) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void setAutoRestore(boolean doAutoRestore) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public boolean isBackupEnabledForUser(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.backup.IBackupManager
        public boolean isBackupEnabled() throws RemoteException {
            return false;
        }

        @Override // android.app.backup.IBackupManager
        public boolean setBackupPassword(String currentPw, String newPw) throws RemoteException {
            return false;
        }

        @Override // android.app.backup.IBackupManager
        public boolean hasBackupPassword() throws RemoteException {
            return false;
        }

        @Override // android.app.backup.IBackupManager
        public void backupNowForUser(int userId) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void backupNow() throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void adbBackup(int userId, ParcelFileDescriptor fd, boolean includeApks, boolean includeObbs, boolean includeShared, boolean doWidgets, boolean allApps, boolean allIncludesSystem, boolean doCompress, boolean doKeyValue, String[] packageNames) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void fullTransportBackupForUser(int userId, String[] packageNames) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void adbRestore(int userId, ParcelFileDescriptor fd) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void acknowledgeFullBackupOrRestoreForUser(int userId, int token, boolean allow, String curPassword, String encryptionPassword, IFullBackupRestoreObserver observer) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void acknowledgeFullBackupOrRestore(int token, boolean allow, String curPassword, String encryptionPassword, IFullBackupRestoreObserver observer) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void updateTransportAttributesForUser(int userId, ComponentName transportComponent, String name, Intent configurationIntent, String currentDestinationString, Intent dataManagementIntent, CharSequence dataManagementLabel) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public String getCurrentTransportForUser(int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public String getCurrentTransport() throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public ComponentName getCurrentTransportComponentForUser(int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public String[] listAllTransportsForUser(int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public String[] listAllTransports() throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public ComponentName[] listAllTransportComponentsForUser(int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public String[] getTransportWhitelist() throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public String selectBackupTransportForUser(int userId, String transport) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public String selectBackupTransport(String transport) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public void selectBackupTransportAsyncForUser(int userId, ComponentName transport, ISelectBackupTransportCallback listener) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public Intent getConfigurationIntentForUser(int userId, String transport) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public Intent getConfigurationIntent(String transport) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public String getDestinationStringForUser(int userId, String transport) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public String getDestinationString(String transport) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public Intent getDataManagementIntentForUser(int userId, String transport) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public Intent getDataManagementIntent(String transport) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public CharSequence getDataManagementLabelForUser(int userId, String transport) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public IRestoreSession beginRestoreSessionForUser(int userId, String packageName, String transportID) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public void opCompleteForUser(int userId, int token, long result) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void opComplete(int token, long result) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void setBackupServiceActive(int whichUser, boolean makeActive) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public boolean isBackupServiceActive(int whichUser) throws RemoteException {
            return false;
        }

        @Override // android.app.backup.IBackupManager
        public long getAvailableRestoreTokenForUser(int userId, String packageName) throws RemoteException {
            return 0L;
        }

        @Override // android.app.backup.IBackupManager
        public boolean isAppEligibleForBackupForUser(int userId, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.backup.IBackupManager
        public String[] filterAppsEligibleForBackupForUser(int userId, String[] packages) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public int requestBackupForUser(int userId, String[] packages, IBackupObserver observer, IBackupManagerMonitor monitor, int flags) throws RemoteException {
            return 0;
        }

        @Override // android.app.backup.IBackupManager
        public int requestBackup(String[] packages, IBackupObserver observer, IBackupManagerMonitor monitor, int flags) throws RemoteException {
            return 0;
        }

        @Override // android.app.backup.IBackupManager
        public void cancelBackupsForUser(int userId) throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public void cancelBackups() throws RemoteException {
        }

        @Override // android.app.backup.IBackupManager
        public UserHandle getUserForAncestralSerialNumber(long ancestralSerialNumber) throws RemoteException {
            return null;
        }

        @Override // android.app.backup.IBackupManager
        public void setAncestralSerialNumber(long ancestralSerialNumber) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBackupManager {
        private static final String DESCRIPTOR = "android.app.backup.IBackupManager";
        static final int TRANSACTION_acknowledgeFullBackupOrRestore = 26;
        static final int TRANSACTION_acknowledgeFullBackupOrRestoreForUser = 25;
        static final int TRANSACTION_adbBackup = 22;
        static final int TRANSACTION_adbRestore = 24;
        static final int TRANSACTION_agentConnected = 7;
        static final int TRANSACTION_agentConnectedForUser = 6;
        static final int TRANSACTION_agentDisconnected = 9;
        static final int TRANSACTION_agentDisconnectedForUser = 8;
        static final int TRANSACTION_backupNow = 21;
        static final int TRANSACTION_backupNowForUser = 20;
        static final int TRANSACTION_beginRestoreSessionForUser = 45;
        static final int TRANSACTION_cancelBackups = 56;
        static final int TRANSACTION_cancelBackupsForUser = 55;
        static final int TRANSACTION_clearBackupData = 4;
        static final int TRANSACTION_clearBackupDataForUser = 3;
        static final int TRANSACTION_dataChanged = 2;
        static final int TRANSACTION_dataChangedForUser = 1;
        static final int TRANSACTION_filterAppsEligibleForBackupForUser = 52;
        static final int TRANSACTION_fullTransportBackupForUser = 23;
        static final int TRANSACTION_getAvailableRestoreTokenForUser = 50;
        static final int TRANSACTION_getConfigurationIntent = 39;
        static final int TRANSACTION_getConfigurationIntentForUser = 38;
        static final int TRANSACTION_getCurrentTransport = 29;
        static final int TRANSACTION_getCurrentTransportComponentForUser = 30;
        static final int TRANSACTION_getCurrentTransportForUser = 28;
        static final int TRANSACTION_getDataManagementIntent = 43;
        static final int TRANSACTION_getDataManagementIntentForUser = 42;
        static final int TRANSACTION_getDataManagementLabelForUser = 44;
        static final int TRANSACTION_getDestinationString = 41;
        static final int TRANSACTION_getDestinationStringForUser = 40;
        static final int TRANSACTION_getTransportWhitelist = 34;
        static final int TRANSACTION_getUserForAncestralSerialNumber = 57;
        static final int TRANSACTION_hasBackupPassword = 19;
        static final int TRANSACTION_initializeTransportsForUser = 5;
        static final int TRANSACTION_isAppEligibleForBackupForUser = 51;
        static final int TRANSACTION_isBackupEnabled = 17;
        static final int TRANSACTION_isBackupEnabledForUser = 16;
        static final int TRANSACTION_isBackupServiceActive = 49;
        static final int TRANSACTION_listAllTransportComponentsForUser = 33;
        static final int TRANSACTION_listAllTransports = 32;
        static final int TRANSACTION_listAllTransportsForUser = 31;
        static final int TRANSACTION_opComplete = 47;
        static final int TRANSACTION_opCompleteForUser = 46;
        static final int TRANSACTION_requestBackup = 54;
        static final int TRANSACTION_requestBackupForUser = 53;
        static final int TRANSACTION_restoreAtInstall = 11;
        static final int TRANSACTION_restoreAtInstallForUser = 10;
        static final int TRANSACTION_selectBackupTransport = 36;
        static final int TRANSACTION_selectBackupTransportAsyncForUser = 37;
        static final int TRANSACTION_selectBackupTransportForUser = 35;
        static final int TRANSACTION_setAncestralSerialNumber = 58;
        static final int TRANSACTION_setAutoRestore = 15;
        static final int TRANSACTION_setAutoRestoreForUser = 14;
        static final int TRANSACTION_setBackupEnabled = 13;
        static final int TRANSACTION_setBackupEnabledForUser = 12;
        static final int TRANSACTION_setBackupPassword = 18;
        static final int TRANSACTION_setBackupServiceActive = 48;
        static final int TRANSACTION_updateTransportAttributesForUser = 27;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

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

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "dataChangedForUser";
                case 2:
                    return "dataChanged";
                case 3:
                    return "clearBackupDataForUser";
                case 4:
                    return "clearBackupData";
                case 5:
                    return "initializeTransportsForUser";
                case 6:
                    return "agentConnectedForUser";
                case 7:
                    return "agentConnected";
                case 8:
                    return "agentDisconnectedForUser";
                case 9:
                    return "agentDisconnected";
                case 10:
                    return "restoreAtInstallForUser";
                case 11:
                    return "restoreAtInstall";
                case 12:
                    return "setBackupEnabledForUser";
                case 13:
                    return "setBackupEnabled";
                case 14:
                    return "setAutoRestoreForUser";
                case 15:
                    return "setAutoRestore";
                case 16:
                    return "isBackupEnabledForUser";
                case 17:
                    return "isBackupEnabled";
                case 18:
                    return "setBackupPassword";
                case 19:
                    return "hasBackupPassword";
                case 20:
                    return "backupNowForUser";
                case 21:
                    return "backupNow";
                case 22:
                    return "adbBackup";
                case 23:
                    return "fullTransportBackupForUser";
                case 24:
                    return "adbRestore";
                case 25:
                    return "acknowledgeFullBackupOrRestoreForUser";
                case 26:
                    return "acknowledgeFullBackupOrRestore";
                case 27:
                    return "updateTransportAttributesForUser";
                case 28:
                    return "getCurrentTransportForUser";
                case 29:
                    return "getCurrentTransport";
                case 30:
                    return "getCurrentTransportComponentForUser";
                case 31:
                    return "listAllTransportsForUser";
                case 32:
                    return "listAllTransports";
                case 33:
                    return "listAllTransportComponentsForUser";
                case 34:
                    return "getTransportWhitelist";
                case 35:
                    return "selectBackupTransportForUser";
                case 36:
                    return "selectBackupTransport";
                case 37:
                    return "selectBackupTransportAsyncForUser";
                case 38:
                    return "getConfigurationIntentForUser";
                case 39:
                    return "getConfigurationIntent";
                case 40:
                    return "getDestinationStringForUser";
                case 41:
                    return "getDestinationString";
                case 42:
                    return "getDataManagementIntentForUser";
                case 43:
                    return "getDataManagementIntent";
                case 44:
                    return "getDataManagementLabelForUser";
                case 45:
                    return "beginRestoreSessionForUser";
                case 46:
                    return "opCompleteForUser";
                case 47:
                    return "opComplete";
                case 48:
                    return "setBackupServiceActive";
                case 49:
                    return "isBackupServiceActive";
                case 50:
                    return "getAvailableRestoreTokenForUser";
                case 51:
                    return "isAppEligibleForBackupForUser";
                case 52:
                    return "filterAppsEligibleForBackupForUser";
                case 53:
                    return "requestBackupForUser";
                case 54:
                    return "requestBackup";
                case 55:
                    return "cancelBackupsForUser";
                case 56:
                    return "cancelBackups";
                case 57:
                    return "getUserForAncestralSerialNumber";
                case 58:
                    return "setAncestralSerialNumber";
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
            boolean _arg1;
            ParcelFileDescriptor _arg12;
            ParcelFileDescriptor _arg13;
            ComponentName _arg14;
            Intent _arg3;
            Intent _arg5;
            CharSequence _arg6;
            ComponentName _arg15;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    dataChangedForUser(_arg0, data.readString());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    dataChanged(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    String _arg16 = data.readString();
                    String _arg2 = data.readString();
                    clearBackupDataForUser(_arg03, _arg16, _arg2);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    clearBackupData(_arg04, data.readString());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    String[] _arg17 = data.createStringArray();
                    IBackupObserver _arg22 = IBackupObserver.Stub.asInterface(data.readStrongBinder());
                    initializeTransportsForUser(_arg05, _arg17, _arg22);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    String _arg18 = data.readString();
                    IBinder _arg23 = data.readStrongBinder();
                    agentConnectedForUser(_arg06, _arg18, _arg23);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    agentConnected(_arg07, data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    agentDisconnectedForUser(_arg08, data.readString());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    agentDisconnected(_arg09);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    String _arg19 = data.readString();
                    int _arg24 = data.readInt();
                    restoreAtInstallForUser(_arg010, _arg19, _arg24);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    restoreAtInstall(_arg011, data.readInt());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setBackupEnabledForUser(_arg012, _arg1);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    setBackupEnabled(_arg1);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setAutoRestoreForUser(_arg013, _arg1);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    setAutoRestore(_arg1);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    boolean isBackupEnabledForUser = isBackupEnabledForUser(_arg014);
                    reply.writeNoException();
                    reply.writeInt(isBackupEnabledForUser ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBackupEnabled = isBackupEnabled();
                    reply.writeNoException();
                    reply.writeInt(isBackupEnabled ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    boolean backupPassword = setBackupPassword(_arg015, data.readString());
                    reply.writeNoException();
                    reply.writeInt(backupPassword ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasBackupPassword = hasBackupPassword();
                    reply.writeNoException();
                    reply.writeInt(hasBackupPassword ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    backupNowForUser(_arg016);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    backupNow();
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    boolean _arg25 = data.readInt() != 0;
                    boolean _arg32 = data.readInt() != 0;
                    boolean _arg4 = data.readInt() != 0;
                    boolean _arg52 = data.readInt() != 0;
                    boolean _arg62 = data.readInt() != 0;
                    boolean _arg7 = data.readInt() != 0;
                    boolean _arg8 = data.readInt() != 0;
                    _arg1 = data.readInt() != 0;
                    boolean _arg9 = _arg1;
                    String[] _arg10 = data.createStringArray();
                    adbBackup(_arg017, _arg12, _arg25, _arg32, _arg4, _arg52, _arg62, _arg7, _arg8, _arg9, _arg10);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg018 = data.readInt();
                    fullTransportBackupForUser(_arg018, data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg019 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    adbRestore(_arg019, _arg13);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    int _arg110 = data.readInt();
                    boolean _arg26 = data.readInt() != 0;
                    String _arg33 = data.readString();
                    String _arg42 = data.readString();
                    IFullBackupRestoreObserver _arg53 = IFullBackupRestoreObserver.Stub.asInterface(data.readStrongBinder());
                    acknowledgeFullBackupOrRestoreForUser(_arg020, _arg110, _arg26, _arg33, _arg42, _arg53);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg021 = data.readInt();
                    boolean _arg111 = data.readInt() != 0;
                    String _arg27 = data.readString();
                    String _arg34 = data.readString();
                    IFullBackupRestoreObserver _arg43 = IFullBackupRestoreObserver.Stub.asInterface(data.readStrongBinder());
                    acknowledgeFullBackupOrRestore(_arg021, _arg111, _arg27, _arg34, _arg43);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg14 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    String _arg28 = data.readString();
                    if (data.readInt() != 0) {
                        _arg3 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    String _arg44 = data.readString();
                    if (data.readInt() != 0) {
                        _arg5 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg6 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    updateTransportAttributesForUser(_arg022, _arg14, _arg28, _arg3, _arg44, _arg5, _arg6);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    String _result = getCurrentTransportForUser(_arg023);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String _result2 = getCurrentTransport();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    ComponentName _result3 = getCurrentTransportComponentForUser(_arg024);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    String[] _result4 = listAllTransportsForUser(_arg025);
                    reply.writeNoException();
                    reply.writeStringArray(_result4);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result5 = listAllTransports();
                    reply.writeNoException();
                    reply.writeStringArray(_result5);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg026 = data.readInt();
                    ComponentName[] _result6 = listAllTransportComponentsForUser(_arg026);
                    reply.writeNoException();
                    reply.writeTypedArray(_result6, 1);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result7 = getTransportWhitelist();
                    reply.writeNoException();
                    reply.writeStringArray(_result7);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg027 = data.readInt();
                    String _result8 = selectBackupTransportForUser(_arg027, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result8);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg028 = data.readString();
                    String _result9 = selectBackupTransport(_arg028);
                    reply.writeNoException();
                    reply.writeString(_result9);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg029 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg15 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    ISelectBackupTransportCallback _arg29 = ISelectBackupTransportCallback.Stub.asInterface(data.readStrongBinder());
                    selectBackupTransportAsyncForUser(_arg029, _arg15, _arg29);
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg030 = data.readInt();
                    Intent _result10 = getConfigurationIntentForUser(_arg030, data.readString());
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg031 = data.readString();
                    Intent _result11 = getConfigurationIntent(_arg031);
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg032 = data.readInt();
                    String _result12 = getDestinationStringForUser(_arg032, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result12);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg033 = data.readString();
                    String _result13 = getDestinationString(_arg033);
                    reply.writeNoException();
                    reply.writeString(_result13);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg034 = data.readInt();
                    Intent _result14 = getDataManagementIntentForUser(_arg034, data.readString());
                    reply.writeNoException();
                    if (_result14 != null) {
                        reply.writeInt(1);
                        _result14.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg035 = data.readString();
                    Intent _result15 = getDataManagementIntent(_arg035);
                    reply.writeNoException();
                    if (_result15 != null) {
                        reply.writeInt(1);
                        _result15.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    CharSequence _result16 = getDataManagementLabelForUser(_arg036, data.readString());
                    reply.writeNoException();
                    if (_result16 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result16, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg037 = data.readInt();
                    String _arg112 = data.readString();
                    String _arg210 = data.readString();
                    IRestoreSession _result17 = beginRestoreSessionForUser(_arg037, _arg112, _arg210);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result17 != null ? _result17.asBinder() : null);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg038 = data.readInt();
                    int _arg113 = data.readInt();
                    long _arg211 = data.readLong();
                    opCompleteForUser(_arg038, _arg113, _arg211);
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg039 = data.readInt();
                    opComplete(_arg039, data.readLong());
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg040 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setBackupServiceActive(_arg040, _arg1);
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg041 = data.readInt();
                    boolean isBackupServiceActive = isBackupServiceActive(_arg041);
                    reply.writeNoException();
                    reply.writeInt(isBackupServiceActive ? 1 : 0);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg042 = data.readInt();
                    long _result18 = getAvailableRestoreTokenForUser(_arg042, data.readString());
                    reply.writeNoException();
                    reply.writeLong(_result18);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg043 = data.readInt();
                    boolean isAppEligibleForBackupForUser = isAppEligibleForBackupForUser(_arg043, data.readString());
                    reply.writeNoException();
                    reply.writeInt(isAppEligibleForBackupForUser ? 1 : 0);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg044 = data.readInt();
                    String[] _result19 = filterAppsEligibleForBackupForUser(_arg044, data.createStringArray());
                    reply.writeNoException();
                    reply.writeStringArray(_result19);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg045 = data.readInt();
                    String[] _arg114 = data.createStringArray();
                    IBackupObserver _arg212 = IBackupObserver.Stub.asInterface(data.readStrongBinder());
                    IBackupManagerMonitor _arg35 = IBackupManagerMonitor.Stub.asInterface(data.readStrongBinder());
                    int _arg45 = data.readInt();
                    int _result20 = requestBackupForUser(_arg045, _arg114, _arg212, _arg35, _arg45);
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg046 = data.createStringArray();
                    IBackupObserver _arg115 = IBackupObserver.Stub.asInterface(data.readStrongBinder());
                    IBackupManagerMonitor _arg213 = IBackupManagerMonitor.Stub.asInterface(data.readStrongBinder());
                    int _arg36 = data.readInt();
                    int _result21 = requestBackup(_arg046, _arg115, _arg213, _arg36);
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg047 = data.readInt();
                    cancelBackupsForUser(_arg047);
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    cancelBackups();
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg048 = data.readLong();
                    UserHandle _result22 = getUserForAncestralSerialNumber(_arg048);
                    reply.writeNoException();
                    if (_result22 != null) {
                        reply.writeInt(1);
                        _result22.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg049 = data.readLong();
                    setAncestralSerialNumber(_arg049);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBackupManager {
            public static IBackupManager sDefaultImpl;
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

            @Override // android.app.backup.IBackupManager
            public void dataChangedForUser(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dataChangedForUser(userId, packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void dataChanged(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dataChanged(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void clearBackupDataForUser(int userId, String transportName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transportName);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearBackupDataForUser(userId, transportName, packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void clearBackupData(String transportName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transportName);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearBackupData(transportName, packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void initializeTransportsForUser(int userId, String[] transportNames, IBackupObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(transportNames);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().initializeTransportsForUser(userId, transportNames, observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void agentConnectedForUser(int userId, String packageName, IBinder agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(agent);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().agentConnectedForUser(userId, packageName, agent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void agentConnected(String packageName, IBinder agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(agent);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().agentConnected(packageName, agent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void agentDisconnectedForUser(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().agentDisconnectedForUser(userId, packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void agentDisconnected(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().agentDisconnected(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void restoreAtInstallForUser(int userId, String packageName, int token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    _data.writeInt(token);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().restoreAtInstallForUser(userId, packageName, token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void restoreAtInstall(String packageName, int token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(token);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().restoreAtInstall(packageName, token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void setBackupEnabledForUser(int userId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBackupEnabledForUser(userId, isEnabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void setBackupEnabled(boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isEnabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBackupEnabled(isEnabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void setAutoRestoreForUser(int userId, boolean doAutoRestore) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(doAutoRestore ? 1 : 0);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAutoRestoreForUser(userId, doAutoRestore);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void setAutoRestore(boolean doAutoRestore) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(doAutoRestore ? 1 : 0);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAutoRestore(doAutoRestore);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public boolean isBackupEnabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBackupEnabledForUser(userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public boolean isBackupEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBackupEnabled();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public boolean setBackupPassword(String currentPw, String newPw) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(currentPw);
                    _data.writeString(newPw);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setBackupPassword(currentPw, newPw);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public boolean hasBackupPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasBackupPassword();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void backupNowForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().backupNowForUser(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void backupNow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().backupNow();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void adbBackup(int userId, ParcelFileDescriptor fd, boolean includeApks, boolean includeObbs, boolean includeShared, boolean doWidgets, boolean allApps, boolean allIncludesSystem, boolean doCompress, boolean doKeyValue, String[] packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 1;
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
                    if (!doKeyValue) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeStringArray(packageNames);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().adbBackup(userId, fd, includeApks, includeObbs, includeShared, doWidgets, allApps, allIncludesSystem, doCompress, doKeyValue, packageNames);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void fullTransportBackupForUser(int userId, String[] packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packageNames);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fullTransportBackupForUser(userId, packageNames);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void adbRestore(int userId, ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().adbRestore(userId, fd);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void acknowledgeFullBackupOrRestoreForUser(int userId, int token, boolean allow, String curPassword, String encryptionPassword, IFullBackupRestoreObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(userId);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(token);
                    _data.writeInt(allow ? 1 : 0);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(curPassword);
                    try {
                        _data.writeString(encryptionPassword);
                        _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    } catch (Throwable th4) {
                        th = th4;
                    }
                    try {
                        boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().acknowledgeFullBackupOrRestoreForUser(userId, token, allow, curPassword, encryptionPassword, observer);
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
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
            }

            @Override // android.app.backup.IBackupManager
            public void acknowledgeFullBackupOrRestore(int token, boolean allow, String curPassword, String encryptionPassword, IFullBackupRestoreObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(allow ? 1 : 0);
                    _data.writeString(curPassword);
                    _data.writeString(encryptionPassword);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().acknowledgeFullBackupOrRestore(token, allow, curPassword, encryptionPassword, observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void updateTransportAttributesForUser(int userId, ComponentName transportComponent, String name, Intent configurationIntent, String currentDestinationString, Intent dataManagementIntent, CharSequence dataManagementLabel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(userId);
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
                        if (dataManagementLabel != null) {
                            _data.writeInt(1);
                            TextUtils.writeToParcel(dataManagementLabel, _data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().updateTransportAttributesForUser(userId, transportComponent, name, configurationIntent, currentDestinationString, dataManagementIntent, dataManagementLabel);
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }

            @Override // android.app.backup.IBackupManager
            public String getCurrentTransportForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentTransportForUser(userId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public String getCurrentTransport() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentTransport();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public ComponentName getCurrentTransportComponentForUser(int userId) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentTransportComponentForUser(userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.createFromParcel(_reply);
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
            public String[] listAllTransportsForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().listAllTransportsForUser(userId);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public String[] listAllTransports() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().listAllTransports();
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public ComponentName[] listAllTransportComponentsForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().listAllTransportComponentsForUser(userId);
                    }
                    _reply.readException();
                    ComponentName[] _result = (ComponentName[]) _reply.createTypedArray(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public String[] getTransportWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTransportWhitelist();
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public String selectBackupTransportForUser(int userId, String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transport);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().selectBackupTransportForUser(userId, transport);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public String selectBackupTransport(String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().selectBackupTransport(transport);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void selectBackupTransportAsyncForUser(int userId, ComponentName transport, ISelectBackupTransportCallback listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (transport != null) {
                        _data.writeInt(1);
                        transport.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().selectBackupTransportAsyncForUser(userId, transport, listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public Intent getConfigurationIntentForUser(int userId, String transport) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transport);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getConfigurationIntentForUser(userId, transport);
                    }
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
            public Intent getConfigurationIntent(String transport) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getConfigurationIntent(transport);
                    }
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
            public String getDestinationStringForUser(int userId, String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transport);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDestinationStringForUser(userId, transport);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public String getDestinationString(String transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDestinationString(transport);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public Intent getDataManagementIntentForUser(int userId, String transport) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transport);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDataManagementIntentForUser(userId, transport);
                    }
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
            public Intent getDataManagementIntent(String transport) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(transport);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDataManagementIntent(transport);
                    }
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
            public CharSequence getDataManagementLabelForUser(int userId, String transport) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(transport);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDataManagementLabelForUser(userId, transport);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(_reply);
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
            public IRestoreSession beginRestoreSessionForUser(int userId, String packageName, String transportID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    _data.writeString(transportID);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().beginRestoreSessionForUser(userId, packageName, transportID);
                    }
                    _reply.readException();
                    IRestoreSession _result = IRestoreSession.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void opCompleteForUser(int userId, int token, long result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(token);
                    _data.writeLong(result);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().opCompleteForUser(userId, token, result);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void opComplete(int token, long result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeLong(result);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().opComplete(token, result);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void setBackupServiceActive(int whichUser, boolean makeActive) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(whichUser);
                    _data.writeInt(makeActive ? 1 : 0);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBackupServiceActive(whichUser, makeActive);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public boolean isBackupServiceActive(int whichUser) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(whichUser);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBackupServiceActive(whichUser);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public long getAvailableRestoreTokenForUser(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAvailableRestoreTokenForUser(userId, packageName);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public boolean isAppEligibleForBackupForUser(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAppEligibleForBackupForUser(userId, packageName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public String[] filterAppsEligibleForBackupForUser(int userId, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packages);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().filterAppsEligibleForBackupForUser(userId, packages);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public int requestBackupForUser(int userId, String[] packages, IBackupObserver observer, IBackupManagerMonitor monitor, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packages);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeStrongBinder(monitor != null ? monitor.asBinder() : null);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().requestBackupForUser(userId, packages, observer, monitor, flags);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public int requestBackup(String[] packages, IBackupObserver observer, IBackupManagerMonitor monitor, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packages);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeStrongBinder(monitor != null ? monitor.asBinder() : null);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().requestBackup(packages, observer, monitor, flags);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void cancelBackupsForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelBackupsForUser(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public void cancelBackups() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelBackups();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.backup.IBackupManager
            public UserHandle getUserForAncestralSerialNumber(long ancestralSerialNumber) throws RemoteException {
                UserHandle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(ancestralSerialNumber);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUserForAncestralSerialNumber(ancestralSerialNumber);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = UserHandle.CREATOR.createFromParcel(_reply);
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
            public void setAncestralSerialNumber(long ancestralSerialNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(ancestralSerialNumber);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAncestralSerialNumber(ancestralSerialNumber);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBackupManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBackupManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
