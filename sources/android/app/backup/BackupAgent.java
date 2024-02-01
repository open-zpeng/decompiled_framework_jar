package android.app.backup;

import android.app.IBackupAgent;
import android.app.QueuedWork;
import android.app.backup.FullBackup;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.ArraySet;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public abstract class BackupAgent extends ContextWrapper {
    private static final boolean DEBUG = false;
    public static final int FLAG_CLIENT_SIDE_ENCRYPTION_ENABLED = 1;
    public static final int FLAG_DEVICE_TO_DEVICE_TRANSFER = 2;
    public static final int FLAG_FAKE_CLIENT_SIDE_ENCRYPTION_ENABLED = Integer.MIN_VALUE;
    public static final int RESULT_ERROR = -1;
    public static final int RESULT_SUCCESS = 0;
    private static final String TAG = "BackupAgent";
    public static final int TYPE_DIRECTORY = 2;
    public static final int TYPE_EOF = 0;
    public static final int TYPE_FILE = 1;
    public static final int TYPE_SYMLINK = 3;
    private final IBinder mBinder;
    Handler mHandler;
    private UserHandle mUser;

    public abstract void onBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) throws IOException;

    public abstract void onRestore(BackupDataInput backupDataInput, int i, ParcelFileDescriptor parcelFileDescriptor) throws IOException;

    Handler getHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
        return this.mHandler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class SharedPrefsSynchronizer implements Runnable {
        public final CountDownLatch mLatch = new CountDownLatch(1);

        SharedPrefsSynchronizer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            QueuedWork.waitToFinish();
            this.mLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void waitForSharedPrefs() {
        Handler h = getHandler();
        SharedPrefsSynchronizer s = new SharedPrefsSynchronizer();
        h.postAtFrontOfQueue(s);
        try {
            s.mLatch.await();
        } catch (InterruptedException e) {
        }
    }

    public BackupAgent() {
        super(null);
        this.mHandler = null;
        this.mBinder = new BackupServiceBinder().asBinder();
    }

    public void onCreate() {
    }

    public void onCreate(UserHandle user) {
        onCreate();
        this.mUser = user;
    }

    public void onDestroy() {
    }

    public void onRestore(BackupDataInput data, long appVersionCode, ParcelFileDescriptor newState) throws IOException {
        onRestore(data, (int) appVersionCode, newState);
    }

    public void onFullBackup(FullBackupDataOutput data) throws IOException {
        String libDir;
        FullBackup.BackupScheme backupScheme = FullBackup.getBackupScheme(this);
        if (!backupScheme.isFullBackupContentEnabled()) {
            return;
        }
        try {
            Map<String, Set<FullBackup.BackupScheme.PathWithRequiredFlags>> manifestIncludeMap = backupScheme.maybeParseAndGetCanonicalIncludePaths();
            ArraySet<FullBackup.BackupScheme.PathWithRequiredFlags> manifestExcludeSet = backupScheme.maybeParseAndGetCanonicalExcludePaths();
            String packageName = getPackageName();
            ApplicationInfo appInfo = getApplicationInfo();
            Context ceContext = createCredentialProtectedStorageContext();
            String rootDir = ceContext.getDataDir().getCanonicalPath();
            String filesDir = ceContext.getFilesDir().getCanonicalPath();
            String noBackupDir = ceContext.getNoBackupFilesDir().getCanonicalPath();
            String databaseDir = ceContext.getDatabasePath("foo").getParentFile().getCanonicalPath();
            String sharedPrefsDir = ceContext.getSharedPreferencesPath("foo").getParentFile().getCanonicalPath();
            String cacheDir = ceContext.getCacheDir().getCanonicalPath();
            String codeCacheDir = ceContext.getCodeCacheDir().getCanonicalPath();
            Context deContext = createDeviceProtectedStorageContext();
            String deviceRootDir = deContext.getDataDir().getCanonicalPath();
            String deviceFilesDir = deContext.getFilesDir().getCanonicalPath();
            String deviceNoBackupDir = deContext.getNoBackupFilesDir().getCanonicalPath();
            String deviceDatabaseDir = deContext.getDatabasePath("foo").getParentFile().getCanonicalPath();
            String deviceSharedPrefsDir = deContext.getSharedPreferencesPath("foo").getParentFile().getCanonicalPath();
            String deviceCacheDir = deContext.getCacheDir().getCanonicalPath();
            String deviceCodeCacheDir = deContext.getCodeCacheDir().getCanonicalPath();
            String deviceRootDir2 = appInfo.nativeLibraryDir;
            if (deviceRootDir2 != null) {
                libDir = new File(appInfo.nativeLibraryDir).getCanonicalPath();
            } else {
                libDir = null;
            }
            ArraySet<String> traversalExcludeSet = new ArraySet<>();
            traversalExcludeSet.add(filesDir);
            traversalExcludeSet.add(noBackupDir);
            traversalExcludeSet.add(databaseDir);
            traversalExcludeSet.add(sharedPrefsDir);
            traversalExcludeSet.add(cacheDir);
            traversalExcludeSet.add(codeCacheDir);
            traversalExcludeSet.add(deviceFilesDir);
            traversalExcludeSet.add(deviceNoBackupDir);
            traversalExcludeSet.add(deviceDatabaseDir);
            traversalExcludeSet.add(deviceSharedPrefsDir);
            traversalExcludeSet.add(deviceCacheDir);
            traversalExcludeSet.add(deviceCodeCacheDir);
            if (libDir != null) {
                traversalExcludeSet.add(libDir);
            }
            applyXmlFiltersAndDoFullBackupForDomain(packageName, "r", manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
            traversalExcludeSet.add(rootDir);
            applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.DEVICE_ROOT_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
            traversalExcludeSet.add(deviceRootDir);
            traversalExcludeSet.remove(filesDir);
            applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.FILES_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
            traversalExcludeSet.add(filesDir);
            traversalExcludeSet.remove(deviceFilesDir);
            applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.DEVICE_FILES_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
            traversalExcludeSet.add(deviceFilesDir);
            traversalExcludeSet.remove(databaseDir);
            applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.DATABASE_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
            traversalExcludeSet.add(databaseDir);
            traversalExcludeSet.remove(deviceDatabaseDir);
            applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.DEVICE_DATABASE_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
            traversalExcludeSet.add(deviceDatabaseDir);
            traversalExcludeSet.remove(sharedPrefsDir);
            applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.SHAREDPREFS_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
            traversalExcludeSet.add(sharedPrefsDir);
            traversalExcludeSet.remove(deviceSharedPrefsDir);
            applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.DEVICE_SHAREDPREFS_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
            traversalExcludeSet.add(deviceSharedPrefsDir);
            if (Process.myUid() != 1000) {
                File efLocation = getExternalFilesDir(null);
                if (efLocation != null) {
                    applyXmlFiltersAndDoFullBackupForDomain(packageName, FullBackup.MANAGED_EXTERNAL_TREE_TOKEN, manifestIncludeMap, manifestExcludeSet, traversalExcludeSet, data);
                }
            }
        } catch (IOException | XmlPullParserException e) {
            if (Log.isLoggable("BackupXmlParserLogging", 2)) {
                Log.v("BackupXmlParserLogging", "Exception trying to parse fullBackupContent xml file! Aborting full backup.", e);
            }
        }
    }

    public void onQuotaExceeded(long backupDataBytes, long quotaBytes) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getBackupUserId() {
        UserHandle userHandle = this.mUser;
        return userHandle == null ? super.getUserId() : userHandle.getIdentifier();
    }

    private void applyXmlFiltersAndDoFullBackupForDomain(String packageName, String domainToken, Map<String, Set<FullBackup.BackupScheme.PathWithRequiredFlags>> includeMap, ArraySet<FullBackup.BackupScheme.PathWithRequiredFlags> filterSet, ArraySet<String> traversalExcludeSet, FullBackupDataOutput data) throws IOException {
        if (includeMap != null && includeMap.size() != 0) {
            if (includeMap.get(domainToken) != null) {
                for (FullBackup.BackupScheme.PathWithRequiredFlags includeFile : includeMap.get(domainToken)) {
                    if (areIncludeRequiredTransportFlagsSatisfied(includeFile.getRequiredFlags(), data.getTransportFlags())) {
                        fullBackupFileTree(packageName, domainToken, includeFile.getPath(), filterSet, traversalExcludeSet, data);
                    }
                }
                return;
            }
            return;
        }
        fullBackupFileTree(packageName, domainToken, FullBackup.getBackupScheme(this).tokenToDirectoryPath(domainToken), filterSet, traversalExcludeSet, data);
    }

    private boolean areIncludeRequiredTransportFlagsSatisfied(int includeFlags, int transportFlags) {
        return (transportFlags & includeFlags) == includeFlags;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0123  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void fullBackupFile(java.io.File r30, android.app.backup.FullBackupDataOutput r31) {
        /*
            Method dump skipped, instructions count: 534
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.backup.BackupAgent.fullBackupFile(java.io.File, android.app.backup.FullBackupDataOutput):void");
    }

    protected final void fullBackupFileTree(String packageName, String domain, String startingPath, ArraySet<FullBackup.BackupScheme.PathWithRequiredFlags> manifestExcludes, ArraySet<String> systemExcludes, FullBackupDataOutput output) {
        File[] contents;
        ArraySet<FullBackup.BackupScheme.PathWithRequiredFlags> arraySet = manifestExcludes;
        String domainPath = FullBackup.getBackupScheme(this).tokenToDirectoryPath(domain);
        if (domainPath == null) {
            return;
        }
        File rootFile = new File(startingPath);
        if (rootFile.exists()) {
            LinkedList<File> scanQueue = new LinkedList<>();
            scanQueue.add(rootFile);
            while (scanQueue.size() > 0) {
                File file = scanQueue.remove(0);
                try {
                    StructStat stat = Os.lstat(file.getPath());
                    if (OsConstants.S_ISREG(stat.st_mode) || OsConstants.S_ISDIR(stat.st_mode)) {
                        String filePath = file.getCanonicalPath();
                        if (arraySet != null && manifestExcludesContainFilePath(arraySet, filePath)) {
                        }
                        if (systemExcludes == null || !systemExcludes.contains(filePath)) {
                            if (OsConstants.S_ISDIR(stat.st_mode) && (contents = file.listFiles()) != null) {
                                for (File entry : contents) {
                                    scanQueue.add(0, entry);
                                }
                            }
                            FullBackup.backupToTar(packageName, domain, null, domainPath, filePath, output);
                            arraySet = manifestExcludes;
                        }
                    }
                } catch (ErrnoException e) {
                    if (Log.isLoggable("BackupXmlParserLogging", 2)) {
                        Log.v("BackupXmlParserLogging", "Error scanning file " + file + " : " + e);
                    }
                    arraySet = manifestExcludes;
                } catch (IOException e2) {
                    if (Log.isLoggable("BackupXmlParserLogging", 2)) {
                        Log.v("BackupXmlParserLogging", "Error canonicalizing path of " + file);
                    }
                    arraySet = manifestExcludes;
                }
            }
        }
    }

    private boolean manifestExcludesContainFilePath(ArraySet<FullBackup.BackupScheme.PathWithRequiredFlags> manifestExcludes, String filePath) {
        Iterator<FullBackup.BackupScheme.PathWithRequiredFlags> it = manifestExcludes.iterator();
        while (it.hasNext()) {
            FullBackup.BackupScheme.PathWithRequiredFlags exclude = it.next();
            String excludePath = exclude.getPath();
            if (excludePath != null && excludePath.equals(filePath)) {
                return true;
            }
        }
        return false;
    }

    public void onRestoreFile(ParcelFileDescriptor data, long size, File destination, int type, long mode, long mtime) throws IOException {
        boolean accept = isFileEligibleForRestore(destination);
        FullBackup.restoreFile(data, size, type, mode, mtime, accept ? destination : null);
    }

    private boolean isFileEligibleForRestore(File destination) throws IOException {
        FullBackup.BackupScheme bs = FullBackup.getBackupScheme(this);
        if (!bs.isFullBackupContentEnabled()) {
            if (Log.isLoggable("BackupXmlParserLogging", 2)) {
                Log.v("BackupXmlParserLogging", "onRestoreFile \"" + destination.getCanonicalPath() + "\" : fullBackupContent not enabled for " + getPackageName());
            }
            return false;
        }
        String destinationCanonicalPath = destination.getCanonicalPath();
        try {
            Map<String, Set<FullBackup.BackupScheme.PathWithRequiredFlags>> includes = bs.maybeParseAndGetCanonicalIncludePaths();
            ArraySet<FullBackup.BackupScheme.PathWithRequiredFlags> excludes = bs.maybeParseAndGetCanonicalExcludePaths();
            if (excludes != null && BackupUtils.isFileSpecifiedInPathList(destination, excludes)) {
                if (Log.isLoggable("BackupXmlParserLogging", 2)) {
                    Log.v("BackupXmlParserLogging", "onRestoreFile: \"" + destinationCanonicalPath + "\": listed in excludes; skipping.");
                }
                return false;
            } else if (includes != null && !includes.isEmpty()) {
                boolean explicitlyIncluded = false;
                for (Set<FullBackup.BackupScheme.PathWithRequiredFlags> domainIncludes : includes.values()) {
                    explicitlyIncluded |= BackupUtils.isFileSpecifiedInPathList(destination, domainIncludes);
                    if (explicitlyIncluded) {
                        break;
                    }
                }
                if (!explicitlyIncluded) {
                    if (Log.isLoggable("BackupXmlParserLogging", 2)) {
                        Log.v("BackupXmlParserLogging", "onRestoreFile: Trying to restore \"" + destinationCanonicalPath + "\" but it isn't specified in the included files; skipping.");
                    }
                    return false;
                }
                return true;
            } else {
                return true;
            }
        } catch (XmlPullParserException e) {
            if (Log.isLoggable("BackupXmlParserLogging", 2)) {
                Log.v("BackupXmlParserLogging", "onRestoreFile \"" + destinationCanonicalPath + "\" : Exception trying to parse fullBackupContent xml file! Aborting onRestoreFile.", e);
            }
            return false;
        }
    }

    protected void onRestoreFile(ParcelFileDescriptor data, long size, int type, String domain, String path, long mode, long mtime) throws IOException {
        long mode2;
        String basePath = FullBackup.getBackupScheme(this).tokenToDirectoryPath(domain);
        if (!domain.equals(FullBackup.MANAGED_EXTERNAL_TREE_TOKEN)) {
            mode2 = mode;
        } else {
            mode2 = -1;
        }
        if (basePath != null) {
            File outFile = new File(basePath, path);
            String outPath = outFile.getCanonicalPath();
            if (outPath.startsWith(basePath + File.separatorChar)) {
                onRestoreFile(data, size, outFile, type, mode2, mtime);
                return;
            }
        }
        FullBackup.restoreFile(data, size, type, mode2, mtime, null);
    }

    public void onRestoreFinished() {
    }

    public final IBinder onBind() {
        return this.mBinder;
    }

    public void attach(Context context) {
        attachBaseContext(context);
    }

    /* loaded from: classes.dex */
    private class BackupServiceBinder extends IBackupAgent.Stub {
        private static final String TAG = "BackupServiceBinder";

        private BackupServiceBinder() {
        }

        /* JADX WARN: Removed duplicated region for block: B:40:0x00c8  */
        @Override // android.app.IBackupAgent
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void doBackup(android.os.ParcelFileDescriptor r18, android.os.ParcelFileDescriptor r19, android.os.ParcelFileDescriptor r20, long r21, android.app.backup.IBackupCallback r23, int r24) throws android.os.RemoteException {
            /*
                r17 = this;
                r1 = r17
                r2 = r23
                java.lang.String r3 = ") threw"
                java.lang.String r4 = "onBackup ("
                java.lang.String r5 = "BackupServiceBinder"
                long r6 = android.os.Binder.clearCallingIdentity()
                android.app.backup.BackupDataOutput r0 = new android.app.backup.BackupDataOutput
                java.io.FileDescriptor r8 = r19.getFileDescriptor()
                r9 = r21
                r11 = r24
                r0.<init>(r8, r9, r11)
                r8 = r0
                r12 = -1
                android.app.backup.BackupAgent r0 = android.app.backup.BackupAgent.this     // Catch: java.lang.Throwable -> L52 java.lang.RuntimeException -> L5b java.io.IOException -> L83
                r14 = r18
                r15 = r20
                r0.onBackup(r14, r8, r15)     // Catch: java.lang.Throwable -> L4a java.lang.RuntimeException -> L4c java.io.IOException -> L4e
                r3 = 0
                android.app.backup.BackupAgent r0 = android.app.backup.BackupAgent.this
                android.app.backup.BackupAgent.access$100(r0)
                android.os.Binder.restoreCallingIdentity(r6)
                r2.operationComplete(r3)     // Catch: android.os.RemoteException -> L35
                goto L36
            L35:
                r0 = move-exception
            L36:
                int r0 = android.os.Binder.getCallingPid()
                int r5 = android.os.Process.myPid()
                if (r0 == r5) goto L49
                libcore.io.IoUtils.closeQuietly(r18)
                libcore.io.IoUtils.closeQuietly(r19)
                libcore.io.IoUtils.closeQuietly(r20)
            L49:
                return
            L4a:
                r0 = move-exception
                goto L57
            L4c:
                r0 = move-exception
                goto L60
            L4e:
                r0 = move-exception
                r16 = r8
                goto L8a
            L52:
                r0 = move-exception
                r14 = r18
                r15 = r20
            L57:
                r3 = r0
                r16 = r8
                goto Lb1
            L5b:
                r0 = move-exception
                r14 = r18
                r15 = r20
            L60:
                r16 = r8
                java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Laf
                r8.<init>()     // Catch: java.lang.Throwable -> Laf
                r8.append(r4)     // Catch: java.lang.Throwable -> Laf
                android.app.backup.BackupAgent r4 = android.app.backup.BackupAgent.this     // Catch: java.lang.Throwable -> Laf
                java.lang.Class r4 = r4.getClass()     // Catch: java.lang.Throwable -> Laf
                java.lang.String r4 = r4.getName()     // Catch: java.lang.Throwable -> Laf
                r8.append(r4)     // Catch: java.lang.Throwable -> Laf
                r8.append(r3)     // Catch: java.lang.Throwable -> Laf
                java.lang.String r3 = r8.toString()     // Catch: java.lang.Throwable -> Laf
                android.util.Log.d(r5, r3, r0)     // Catch: java.lang.Throwable -> Laf
                throw r0     // Catch: java.lang.Throwable -> Laf
            L83:
                r0 = move-exception
                r14 = r18
                r15 = r20
                r16 = r8
            L8a:
                java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Laf
                r8.<init>()     // Catch: java.lang.Throwable -> Laf
                r8.append(r4)     // Catch: java.lang.Throwable -> Laf
                android.app.backup.BackupAgent r4 = android.app.backup.BackupAgent.this     // Catch: java.lang.Throwable -> Laf
                java.lang.Class r4 = r4.getClass()     // Catch: java.lang.Throwable -> Laf
                java.lang.String r4 = r4.getName()     // Catch: java.lang.Throwable -> Laf
                r8.append(r4)     // Catch: java.lang.Throwable -> Laf
                r8.append(r3)     // Catch: java.lang.Throwable -> Laf
                java.lang.String r3 = r8.toString()     // Catch: java.lang.Throwable -> Laf
                android.util.Log.d(r5, r3, r0)     // Catch: java.lang.Throwable -> Laf
                java.lang.RuntimeException r3 = new java.lang.RuntimeException     // Catch: java.lang.Throwable -> Laf
                r3.<init>(r0)     // Catch: java.lang.Throwable -> Laf
                throw r3     // Catch: java.lang.Throwable -> Laf
            Laf:
                r0 = move-exception
                r3 = r0
            Lb1:
                android.app.backup.BackupAgent r0 = android.app.backup.BackupAgent.this
                android.app.backup.BackupAgent.access$100(r0)
                android.os.Binder.restoreCallingIdentity(r6)
                r2.operationComplete(r12)     // Catch: android.os.RemoteException -> Lbd
                goto Lbe
            Lbd:
                r0 = move-exception
            Lbe:
                int r0 = android.os.Binder.getCallingPid()
                int r4 = android.os.Process.myPid()
                if (r0 == r4) goto Ld1
                libcore.io.IoUtils.closeQuietly(r18)
                libcore.io.IoUtils.closeQuietly(r19)
                libcore.io.IoUtils.closeQuietly(r20)
            Ld1:
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.backup.BackupAgent.BackupServiceBinder.doBackup(android.os.ParcelFileDescriptor, android.os.ParcelFileDescriptor, android.os.ParcelFileDescriptor, long, android.app.backup.IBackupCallback, int):void");
        }

        /* JADX WARN: Removed duplicated region for block: B:37:0x00c8  */
        @Override // android.app.IBackupAgent
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void doRestore(android.os.ParcelFileDescriptor r17, long r18, android.os.ParcelFileDescriptor r20, int r21, android.app.backup.IBackupManager r22) throws android.os.RemoteException {
            /*
                r16 = this;
                r1 = r16
                r2 = r21
                r3 = r22
                java.lang.String r4 = ") threw"
                java.lang.String r5 = "onRestore ("
                java.lang.String r6 = "BackupServiceBinder"
                long r7 = android.os.Binder.clearCallingIdentity()
                android.app.backup.BackupAgent r0 = android.app.backup.BackupAgent.this
                android.app.backup.BackupAgent.access$100(r0)
                android.app.backup.BackupDataInput r0 = new android.app.backup.BackupDataInput
                java.io.FileDescriptor r9 = r17.getFileDescriptor()
                r0.<init>(r9)
                r9 = r0
                r10 = 0
                android.app.backup.BackupAgent r0 = android.app.backup.BackupAgent.this     // Catch: java.lang.Throwable -> L54 java.lang.RuntimeException -> L5b java.io.IOException -> L81
                r12 = r18
                r14 = r20
                r0.onRestore(r9, r12, r14)     // Catch: java.lang.Throwable -> L4e java.lang.RuntimeException -> L50 java.io.IOException -> L52
                android.app.backup.BackupAgent r0 = android.app.backup.BackupAgent.this
                r0.reloadSharedPreferences()
                android.os.Binder.restoreCallingIdentity(r7)
                android.app.backup.BackupAgent r0 = android.app.backup.BackupAgent.this     // Catch: android.os.RemoteException -> L3c
                int r0 = android.app.backup.BackupAgent.access$200(r0)     // Catch: android.os.RemoteException -> L3c
                r3.opCompleteForUser(r0, r2, r10)     // Catch: android.os.RemoteException -> L3c
                goto L3d
            L3c:
                r0 = move-exception
            L3d:
                int r0 = android.os.Binder.getCallingPid()
                int r4 = android.os.Process.myPid()
                if (r0 == r4) goto L4d
                libcore.io.IoUtils.closeQuietly(r17)
                libcore.io.IoUtils.closeQuietly(r20)
            L4d:
                return
            L4e:
                r0 = move-exception
                goto L59
            L50:
                r0 = move-exception
                goto L60
            L52:
                r0 = move-exception
                goto L86
            L54:
                r0 = move-exception
                r12 = r18
                r14 = r20
            L59:
                r4 = r0
                goto Lab
            L5b:
                r0 = move-exception
                r12 = r18
                r14 = r20
            L60:
                java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4e
                r15.<init>()     // Catch: java.lang.Throwable -> L4e
                r15.append(r5)     // Catch: java.lang.Throwable -> L4e
                android.app.backup.BackupAgent r5 = android.app.backup.BackupAgent.this     // Catch: java.lang.Throwable -> L4e
                java.lang.Class r5 = r5.getClass()     // Catch: java.lang.Throwable -> L4e
                java.lang.String r5 = r5.getName()     // Catch: java.lang.Throwable -> L4e
                r15.append(r5)     // Catch: java.lang.Throwable -> L4e
                r15.append(r4)     // Catch: java.lang.Throwable -> L4e
                java.lang.String r4 = r15.toString()     // Catch: java.lang.Throwable -> L4e
                android.util.Log.d(r6, r4, r0)     // Catch: java.lang.Throwable -> L4e
                throw r0     // Catch: java.lang.Throwable -> L4e
            L81:
                r0 = move-exception
                r12 = r18
                r14 = r20
            L86:
                java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4e
                r15.<init>()     // Catch: java.lang.Throwable -> L4e
                r15.append(r5)     // Catch: java.lang.Throwable -> L4e
                android.app.backup.BackupAgent r5 = android.app.backup.BackupAgent.this     // Catch: java.lang.Throwable -> L4e
                java.lang.Class r5 = r5.getClass()     // Catch: java.lang.Throwable -> L4e
                java.lang.String r5 = r5.getName()     // Catch: java.lang.Throwable -> L4e
                r15.append(r5)     // Catch: java.lang.Throwable -> L4e
                r15.append(r4)     // Catch: java.lang.Throwable -> L4e
                java.lang.String r4 = r15.toString()     // Catch: java.lang.Throwable -> L4e
                android.util.Log.d(r6, r4, r0)     // Catch: java.lang.Throwable -> L4e
                java.lang.RuntimeException r4 = new java.lang.RuntimeException     // Catch: java.lang.Throwable -> L4e
                r4.<init>(r0)     // Catch: java.lang.Throwable -> L4e
                throw r4     // Catch: java.lang.Throwable -> L4e
            Lab:
                android.app.backup.BackupAgent r0 = android.app.backup.BackupAgent.this
                r0.reloadSharedPreferences()
                android.os.Binder.restoreCallingIdentity(r7)
                android.app.backup.BackupAgent r0 = android.app.backup.BackupAgent.this     // Catch: android.os.RemoteException -> Lbd
                int r0 = android.app.backup.BackupAgent.access$200(r0)     // Catch: android.os.RemoteException -> Lbd
                r3.opCompleteForUser(r0, r2, r10)     // Catch: android.os.RemoteException -> Lbd
                goto Lbe
            Lbd:
                r0 = move-exception
            Lbe:
                int r0 = android.os.Binder.getCallingPid()
                int r5 = android.os.Process.myPid()
                if (r0 == r5) goto Lce
                libcore.io.IoUtils.closeQuietly(r17)
                libcore.io.IoUtils.closeQuietly(r20)
            Lce:
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.backup.BackupAgent.BackupServiceBinder.doRestore(android.os.ParcelFileDescriptor, long, android.os.ParcelFileDescriptor, int, android.app.backup.IBackupManager):void");
        }

        /* JADX WARN: Removed duplicated region for block: B:47:0x00f3  */
        @Override // android.app.IBackupAgent
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void doFullBackup(android.os.ParcelFileDescriptor r17, long r18, int r20, android.app.backup.IBackupManager r21, int r22) {
            /*
                Method dump skipped, instructions count: 247
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.backup.BackupAgent.BackupServiceBinder.doFullBackup(android.os.ParcelFileDescriptor, long, int, android.app.backup.IBackupManager, int):void");
        }

        @Override // android.app.IBackupAgent
        public void doMeasureFullBackup(long quotaBytes, int token, IBackupManager callbackBinder, int transportFlags) {
            long ident = Binder.clearCallingIdentity();
            FullBackupDataOutput measureOutput = new FullBackupDataOutput(quotaBytes, transportFlags);
            BackupAgent.this.waitForSharedPrefs();
            try {
                try {
                    BackupAgent.this.onFullBackup(measureOutput);
                    Binder.restoreCallingIdentity(ident);
                    try {
                        callbackBinder.opCompleteForUser(BackupAgent.this.getBackupUserId(), token, measureOutput.getSize());
                    } catch (RemoteException e) {
                    }
                } catch (IOException ex) {
                    Log.d(TAG, "onFullBackup[M] (" + BackupAgent.this.getClass().getName() + ") threw", ex);
                    throw new RuntimeException(ex);
                } catch (RuntimeException ex2) {
                    Log.d(TAG, "onFullBackup[M] (" + BackupAgent.this.getClass().getName() + ") threw", ex2);
                    throw ex2;
                }
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opCompleteForUser(BackupAgent.this.getBackupUserId(), token, measureOutput.getSize());
                } catch (RemoteException e2) {
                }
                throw th;
            }
        }

        @Override // android.app.IBackupAgent
        public void doRestoreFile(ParcelFileDescriptor data, long size, int type, String domain, String path, long mode, long mtime, int token, IBackupManager callbackBinder) throws RemoteException {
            long ident = Binder.clearCallingIdentity();
            try {
                try {
                    BackupAgent.this.onRestoreFile(data, size, type, domain, path, mode, mtime);
                    BackupAgent.this.waitForSharedPrefs();
                    BackupAgent.this.reloadSharedPreferences();
                    Binder.restoreCallingIdentity(ident);
                    try {
                        callbackBinder.opCompleteForUser(BackupAgent.this.getBackupUserId(), token, 0L);
                    } catch (RemoteException e) {
                    }
                    if (Binder.getCallingPid() != Process.myPid()) {
                        IoUtils.closeQuietly(data);
                    }
                } catch (IOException e2) {
                    Log.d(TAG, "onRestoreFile (" + BackupAgent.this.getClass().getName() + ") threw", e2);
                    throw new RuntimeException(e2);
                }
            } catch (Throwable th) {
                BackupAgent.this.waitForSharedPrefs();
                BackupAgent.this.reloadSharedPreferences();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opCompleteForUser(BackupAgent.this.getBackupUserId(), token, 0L);
                } catch (RemoteException e3) {
                }
                if (Binder.getCallingPid() != Process.myPid()) {
                    IoUtils.closeQuietly(data);
                }
                throw th;
            }
        }

        @Override // android.app.IBackupAgent
        public void doRestoreFinished(int token, IBackupManager callbackBinder) {
            long ident = Binder.clearCallingIdentity();
            try {
                try {
                    BackupAgent.this.onRestoreFinished();
                    BackupAgent.this.waitForSharedPrefs();
                    Binder.restoreCallingIdentity(ident);
                    try {
                        callbackBinder.opCompleteForUser(BackupAgent.this.getBackupUserId(), token, 0L);
                    } catch (RemoteException e) {
                    }
                } catch (Exception e2) {
                    Log.d(TAG, "onRestoreFinished (" + BackupAgent.this.getClass().getName() + ") threw", e2);
                    throw e2;
                }
            } catch (Throwable th) {
                BackupAgent.this.waitForSharedPrefs();
                Binder.restoreCallingIdentity(ident);
                try {
                    callbackBinder.opCompleteForUser(BackupAgent.this.getBackupUserId(), token, 0L);
                } catch (RemoteException e3) {
                }
                throw th;
            }
        }

        @Override // android.app.IBackupAgent
        public void fail(String message) {
            BackupAgent.this.getHandler().post(new FailRunnable(message));
        }

        @Override // android.app.IBackupAgent
        public void doQuotaExceeded(long backupDataBytes, long quotaBytes, IBackupCallback callbackBinder) {
            long ident = Binder.clearCallingIdentity();
            try {
                try {
                    BackupAgent.this.onQuotaExceeded(backupDataBytes, quotaBytes);
                    BackupAgent.this.waitForSharedPrefs();
                    Binder.restoreCallingIdentity(ident);
                    try {
                        callbackBinder.operationComplete(0L);
                    } catch (RemoteException e) {
                    }
                } catch (Throwable th) {
                    BackupAgent.this.waitForSharedPrefs();
                    Binder.restoreCallingIdentity(ident);
                    try {
                        callbackBinder.operationComplete(-1L);
                    } catch (RemoteException e2) {
                    }
                    throw th;
                }
            } catch (Exception e3) {
                Log.d(TAG, "onQuotaExceeded(" + BackupAgent.this.getClass().getName() + ") threw", e3);
                throw e3;
            }
        }
    }

    /* loaded from: classes.dex */
    static class FailRunnable implements Runnable {
        private String mMessage;

        FailRunnable(String message) {
            this.mMessage = message;
        }

        @Override // java.lang.Runnable
        public void run() {
            throw new IllegalStateException(this.mMessage);
        }
    }
}
