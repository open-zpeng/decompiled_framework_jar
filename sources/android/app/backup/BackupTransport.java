package android.app.backup;

import android.annotation.SystemApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.android.internal.backup.IBackupTransport;
@SystemApi
/* loaded from: classes.dex */
public class BackupTransport {
    public static final int AGENT_ERROR = -1003;
    public static final int AGENT_UNKNOWN = -1004;
    public static final String EXTRA_TRANSPORT_REGISTRATION = "android.app.backup.extra.TRANSPORT_REGISTRATION";
    public static final int FLAG_INCREMENTAL = 2;
    public static final int FLAG_NON_INCREMENTAL = 4;
    public static final int FLAG_USER_INITIATED = 1;
    public static final int NO_MORE_DATA = -1;
    public static final int TRANSPORT_ERROR = -1000;
    public static final int TRANSPORT_NON_INCREMENTAL_BACKUP_REQUIRED = -1006;
    public static final int TRANSPORT_NOT_INITIALIZED = -1001;
    public static final int TRANSPORT_OK = 0;
    public static final int TRANSPORT_PACKAGE_REJECTED = -1002;
    public static final int TRANSPORT_QUOTA_EXCEEDED = -1005;
    IBackupTransport mBinderImpl = new TransportImpl();

    public IBinder getBinder() {
        return this.mBinderImpl.asBinder();
    }

    public String name() {
        throw new UnsupportedOperationException("Transport name() not implemented");
    }

    public Intent configurationIntent() {
        return null;
    }

    public String currentDestinationString() {
        throw new UnsupportedOperationException("Transport currentDestinationString() not implemented");
    }

    public Intent dataManagementIntent() {
        return null;
    }

    public String dataManagementLabel() {
        throw new UnsupportedOperationException("Transport dataManagementLabel() not implemented");
    }

    public String transportDirName() {
        throw new UnsupportedOperationException("Transport transportDirName() not implemented");
    }

    public int initializeDevice() {
        return -1000;
    }

    public int clearBackupData(PackageInfo packageInfo) {
        return -1000;
    }

    public int finishBackup() {
        return -1000;
    }

    public long requestBackupTime() {
        return 0L;
    }

    public int performBackup(PackageInfo packageInfo, ParcelFileDescriptor inFd, int flags) {
        return performBackup(packageInfo, inFd);
    }

    public int performBackup(PackageInfo packageInfo, ParcelFileDescriptor inFd) {
        return -1000;
    }

    public RestoreSet[] getAvailableRestoreSets() {
        return null;
    }

    public long getCurrentRestoreSet() {
        return 0L;
    }

    public int startRestore(long token, PackageInfo[] packages) {
        return -1000;
    }

    public RestoreDescription nextRestorePackage() {
        return null;
    }

    public int getRestoreData(ParcelFileDescriptor outFd) {
        return -1000;
    }

    public void finishRestore() {
        throw new UnsupportedOperationException("Transport finishRestore() not implemented");
    }

    public long requestFullBackupTime() {
        return 0L;
    }

    public int performFullBackup(PackageInfo targetPackage, ParcelFileDescriptor socket, int flags) {
        return performFullBackup(targetPackage, socket);
    }

    public int performFullBackup(PackageInfo targetPackage, ParcelFileDescriptor socket) {
        return -1002;
    }

    public int checkFullBackupSize(long size) {
        return 0;
    }

    public int sendBackupData(int numBytes) {
        return -1000;
    }

    public void cancelFullBackup() {
        throw new UnsupportedOperationException("Transport cancelFullBackup() not implemented");
    }

    public boolean isAppEligibleForBackup(PackageInfo targetPackage, boolean isFullBackup) {
        return true;
    }

    public long getBackupQuota(String packageName, boolean isFullBackup) {
        return Long.MAX_VALUE;
    }

    public int getNextFullRestoreDataChunk(ParcelFileDescriptor socket) {
        return 0;
    }

    public int abortFullRestore() {
        return 0;
    }

    public int getTransportFlags() {
        return 0;
    }

    /* loaded from: classes.dex */
    class TransportImpl extends IBackupTransport.Stub {
        TransportImpl() {
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized String name() throws RemoteException {
            return BackupTransport.this.name();
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized Intent configurationIntent() throws RemoteException {
            return BackupTransport.this.configurationIntent();
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized String currentDestinationString() throws RemoteException {
            return BackupTransport.this.currentDestinationString();
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized Intent dataManagementIntent() {
            return BackupTransport.this.dataManagementIntent();
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized String dataManagementLabel() {
            return BackupTransport.this.dataManagementLabel();
        }

        public synchronized String transportDirName() throws RemoteException {
            return BackupTransport.this.transportDirName();
        }

        public synchronized long requestBackupTime() throws RemoteException {
            return BackupTransport.this.requestBackupTime();
        }

        public synchronized int initializeDevice() throws RemoteException {
            return BackupTransport.this.initializeDevice();
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized int performBackup(PackageInfo packageInfo, ParcelFileDescriptor inFd, int flags) throws RemoteException {
            return BackupTransport.this.performBackup(packageInfo, inFd, flags);
        }

        public synchronized int clearBackupData(PackageInfo packageInfo) throws RemoteException {
            return BackupTransport.this.clearBackupData(packageInfo);
        }

        public synchronized int finishBackup() throws RemoteException {
            return BackupTransport.this.finishBackup();
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized RestoreSet[] getAvailableRestoreSets() throws RemoteException {
            return BackupTransport.this.getAvailableRestoreSets();
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized long getCurrentRestoreSet() throws RemoteException {
            return BackupTransport.this.getCurrentRestoreSet();
        }

        public synchronized int startRestore(long token, PackageInfo[] packages) throws RemoteException {
            return BackupTransport.this.startRestore(token, packages);
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized RestoreDescription nextRestorePackage() throws RemoteException {
            return BackupTransport.this.nextRestorePackage();
        }

        public synchronized int getRestoreData(ParcelFileDescriptor outFd) throws RemoteException {
            return BackupTransport.this.getRestoreData(outFd);
        }

        public synchronized void finishRestore() throws RemoteException {
            BackupTransport.this.finishRestore();
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized long requestFullBackupTime() throws RemoteException {
            return BackupTransport.this.requestFullBackupTime();
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized int performFullBackup(PackageInfo targetPackage, ParcelFileDescriptor socket, int flags) throws RemoteException {
            return BackupTransport.this.performFullBackup(targetPackage, socket, flags);
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized int checkFullBackupSize(long size) {
            return BackupTransport.this.checkFullBackupSize(size);
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized int sendBackupData(int numBytes) throws RemoteException {
            return BackupTransport.this.sendBackupData(numBytes);
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized void cancelFullBackup() throws RemoteException {
            BackupTransport.this.cancelFullBackup();
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized boolean isAppEligibleForBackup(PackageInfo targetPackage, boolean isFullBackup) throws RemoteException {
            return BackupTransport.this.isAppEligibleForBackup(targetPackage, isFullBackup);
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized long getBackupQuota(String packageName, boolean isFullBackup) {
            return BackupTransport.this.getBackupQuota(packageName, isFullBackup);
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized int getTransportFlags() {
            return BackupTransport.this.getTransportFlags();
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized int getNextFullRestoreDataChunk(ParcelFileDescriptor socket) {
            return BackupTransport.this.getNextFullRestoreDataChunk(socket);
        }

        @Override // com.android.internal.backup.IBackupTransport
        public synchronized int abortFullRestore() {
            return BackupTransport.this.abortFullRestore();
        }
    }
}
