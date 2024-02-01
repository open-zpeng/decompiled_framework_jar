package com.android.internal.backup;

import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupTransport;
import android.app.backup.RestoreDescription;
import android.app.backup.RestoreSet;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.ArrayMap;
import android.util.Log;
import com.android.org.bouncycastle.util.encoders.Base64;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import libcore.io.IoUtils;
/* loaded from: classes3.dex */
public class LocalTransport extends BackupTransport {
    private static final long CURRENT_SET_TOKEN = 1;
    private static final boolean DEBUG = false;
    private static final long FULL_BACKUP_SIZE_QUOTA = 26214400;
    private static final String FULL_DATA_DIR = "_full";
    private static final String INCREMENTAL_DIR = "_delta";
    private static final long KEY_VALUE_BACKUP_SIZE_QUOTA = 5242880;
    static final long[] POSSIBLE_SETS = {2, 3, 4, 5, 6, 7, 8, 9};
    private static final String TAG = "LocalTransport";
    private static final String TRANSPORT_DATA_MANAGEMENT_LABEL = "";
    private static final String TRANSPORT_DESTINATION_STRING = "Backing up to debug-only private cache";
    private static final String TRANSPORT_DIR_NAME = "com.android.internal.backup.LocalTransport";
    private Context mContext;
    private FileInputStream mCurFullRestoreStream;
    private byte[] mFullBackupBuffer;
    private BufferedOutputStream mFullBackupOutputStream;
    private long mFullBackupSize;
    private byte[] mFullRestoreBuffer;
    private FileOutputStream mFullRestoreSocketStream;
    private String mFullTargetPackage;
    private final LocalTransportParameters mParameters;
    private File mRestoreSetDir;
    private File mRestoreSetFullDir;
    private File mRestoreSetIncrementalDir;
    private int mRestoreType;
    private ParcelFileDescriptor mSocket;
    private FileInputStream mSocketInputStream;
    private File mDataDir = new File(Environment.getDownloadCacheDirectory(), Context.BACKUP_SERVICE);
    private File mCurrentSetDir = new File(this.mDataDir, Long.toString(1));
    private File mCurrentSetIncrementalDir = new File(this.mCurrentSetDir, INCREMENTAL_DIR);
    private File mCurrentSetFullDir = new File(this.mCurrentSetDir, FULL_DATA_DIR);
    private PackageInfo[] mRestorePackages = null;
    private int mRestorePackage = -1;

    private void makeDataDirs() {
        this.mCurrentSetDir.mkdirs();
        this.mCurrentSetFullDir.mkdir();
        this.mCurrentSetIncrementalDir.mkdir();
    }

    public LocalTransport(Context context, LocalTransportParameters parameters) {
        this.mContext = context;
        this.mParameters = parameters;
        makeDataDirs();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocalTransportParameters getParameters() {
        return this.mParameters;
    }

    @Override // android.app.backup.BackupTransport
    public String name() {
        return new ComponentName(this.mContext, getClass()).flattenToShortString();
    }

    @Override // android.app.backup.BackupTransport
    public Intent configurationIntent() {
        return null;
    }

    @Override // android.app.backup.BackupTransport
    public String currentDestinationString() {
        return TRANSPORT_DESTINATION_STRING;
    }

    @Override // android.app.backup.BackupTransport
    public Intent dataManagementIntent() {
        return null;
    }

    @Override // android.app.backup.BackupTransport
    public String dataManagementLabel() {
        return "";
    }

    @Override // android.app.backup.BackupTransport
    public String transportDirName() {
        return TRANSPORT_DIR_NAME;
    }

    @Override // android.app.backup.BackupTransport
    public int getTransportFlags() {
        int flags = super.getTransportFlags();
        if (this.mParameters.isFakeEncryptionFlag()) {
            return flags | Integer.MIN_VALUE;
        }
        return flags;
    }

    @Override // android.app.backup.BackupTransport
    public long requestBackupTime() {
        return 0L;
    }

    @Override // android.app.backup.BackupTransport
    public int initializeDevice() {
        deleteContents(this.mCurrentSetDir);
        makeDataDirs();
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class KVOperation {
        final String key;
        final byte[] value;

        KVOperation(String k, byte[] v) {
            this.key = k;
            this.value = v;
        }
    }

    @Override // android.app.backup.BackupTransport
    public int performBackup(PackageInfo packageInfo, ParcelFileDescriptor data) {
        return performBackup(packageInfo, data, 0);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Found unreachable blocks
        	at jadx.core.dex.visitors.blocks.DominatorTree.sortBlocks(DominatorTree.java:35)
        	at jadx.core.dex.visitors.blocks.DominatorTree.compute(DominatorTree.java:25)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.computeDominators(BlockProcessor.java:202)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:45)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    @Override // android.app.backup.BackupTransport
    public int performBackup(android.content.pm.PackageInfo r22, android.os.ParcelFileDescriptor r23, int r24) {
        /*
            Method dump skipped, instructions count: 381
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.backup.LocalTransport.performBackup(android.content.pm.PackageInfo, android.os.ParcelFileDescriptor, int):int");
    }

    private ArrayList<KVOperation> parseBackupStream(ParcelFileDescriptor data) throws IOException {
        ArrayList<KVOperation> changeOps = new ArrayList<>();
        BackupDataInput changeSet = new BackupDataInput(data.getFileDescriptor());
        while (changeSet.readNextHeader()) {
            String key = changeSet.getKey();
            String base64Key = new String(Base64.encode(key.getBytes()));
            int dataSize = changeSet.getDataSize();
            byte[] buf = dataSize >= 0 ? new byte[dataSize] : null;
            if (dataSize >= 0) {
                changeSet.readEntityData(buf, 0, dataSize);
            }
            changeOps.add(new KVOperation(base64Key, buf));
        }
        return changeOps;
    }

    private int parseKeySizes(File packageDir, ArrayMap<String, Integer> datastore) {
        int totalSize = 0;
        String[] elements = packageDir.list();
        if (elements != null) {
            for (String file : elements) {
                File element = new File(packageDir, file);
                int size = (int) element.length();
                totalSize += size;
                datastore.put(file, Integer.valueOf(size));
            }
        }
        return totalSize;
    }

    private void deleteContents(File dirname) {
        File[] contents = dirname.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (f.isDirectory()) {
                    deleteContents(f);
                }
                f.delete();
            }
        }
    }

    @Override // android.app.backup.BackupTransport
    public int clearBackupData(PackageInfo packageInfo) {
        File packageDir = new File(this.mCurrentSetIncrementalDir, packageInfo.packageName);
        File[] fileset = packageDir.listFiles();
        if (fileset != null) {
            for (File f : fileset) {
                f.delete();
            }
            packageDir.delete();
        }
        File packageDir2 = new File(this.mCurrentSetFullDir, packageInfo.packageName);
        File[] tarballs = packageDir2.listFiles();
        if (tarballs != null) {
            for (File f2 : tarballs) {
                f2.delete();
            }
            packageDir2.delete();
        }
        return 0;
    }

    @Override // android.app.backup.BackupTransport
    public int finishBackup() {
        return tearDownFullBackup();
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [android.os.ParcelFileDescriptor, java.io.BufferedOutputStream] */
    private int tearDownFullBackup() {
        if (this.mSocket != null) {
            try {
                if (this.mFullBackupOutputStream != null) {
                    this.mFullBackupOutputStream.flush();
                    this.mFullBackupOutputStream.close();
                }
                this.mSocketInputStream = null;
                this.mFullTargetPackage = null;
                this.mSocket.close();
                return 0;
            } catch (IOException e) {
                return -1000;
            } finally {
                this.mSocket = null;
                this.mFullBackupOutputStream = null;
            }
        }
        return 0;
    }

    private File tarballFile(String pkgName) {
        return new File(this.mCurrentSetFullDir, pkgName);
    }

    @Override // android.app.backup.BackupTransport
    public long requestFullBackupTime() {
        return 0L;
    }

    @Override // android.app.backup.BackupTransport
    public int checkFullBackupSize(long size) {
        if (size <= 0) {
            return -1002;
        }
        if (size <= FULL_BACKUP_SIZE_QUOTA) {
            return 0;
        }
        return -1005;
    }

    @Override // android.app.backup.BackupTransport
    public int performFullBackup(PackageInfo targetPackage, ParcelFileDescriptor socket) {
        if (this.mSocket != null) {
            Log.e(TAG, "Attempt to initiate full backup while one is in progress");
            return -1000;
        }
        try {
            this.mFullBackupSize = 0L;
            this.mSocket = ParcelFileDescriptor.dup(socket.getFileDescriptor());
            this.mSocketInputStream = new FileInputStream(this.mSocket.getFileDescriptor());
            this.mFullTargetPackage = targetPackage.packageName;
            this.mFullBackupBuffer = new byte[4096];
            return 0;
        } catch (IOException e) {
            Log.e(TAG, "Unable to process socket for full backup");
            return -1000;
        }
    }

    @Override // android.app.backup.BackupTransport
    public int sendBackupData(int numBytes) {
        if (this.mSocket == null) {
            Log.w(TAG, "Attempted sendBackupData before performFullBackup");
            return -1000;
        }
        this.mFullBackupSize += numBytes;
        if (this.mFullBackupSize > FULL_BACKUP_SIZE_QUOTA) {
            return -1005;
        }
        if (numBytes > this.mFullBackupBuffer.length) {
            this.mFullBackupBuffer = new byte[numBytes];
        }
        if (this.mFullBackupOutputStream == null) {
            try {
                File tarball = tarballFile(this.mFullTargetPackage);
                FileOutputStream tarstream = new FileOutputStream(tarball);
                this.mFullBackupOutputStream = new BufferedOutputStream(tarstream);
            } catch (FileNotFoundException e) {
                return -1000;
            }
        }
        int bytesLeft = numBytes;
        while (bytesLeft > 0) {
            try {
                int nRead = this.mSocketInputStream.read(this.mFullBackupBuffer, 0, bytesLeft);
                if (nRead >= 0) {
                    this.mFullBackupOutputStream.write(this.mFullBackupBuffer, 0, nRead);
                    bytesLeft -= nRead;
                } else {
                    Log.w(TAG, "Unexpected EOD; failing backup");
                    return -1000;
                }
            } catch (IOException e2) {
                Log.e(TAG, "Error handling backup data for " + this.mFullTargetPackage);
                return -1000;
            }
        }
        return 0;
    }

    @Override // android.app.backup.BackupTransport
    public void cancelFullBackup() {
        File archive = tarballFile(this.mFullTargetPackage);
        tearDownFullBackup();
        if (archive.exists()) {
            archive.delete();
        }
    }

    @Override // android.app.backup.BackupTransport
    public RestoreSet[] getAvailableRestoreSets() {
        long[] jArr;
        long[] existing = new long[POSSIBLE_SETS.length + 1];
        int i = 0;
        int num = 0;
        for (long token : POSSIBLE_SETS) {
            if (new File(this.mDataDir, Long.toString(token)).exists()) {
                existing[num] = token;
                num++;
            }
        }
        existing[num] = 1;
        RestoreSet[] available = new RestoreSet[num + 1];
        while (true) {
            int i2 = i;
            int i3 = available.length;
            if (i2 < i3) {
                available[i2] = new RestoreSet("Local disk image", "flash", existing[i2]);
                i = i2 + 1;
            } else {
                return available;
            }
        }
    }

    @Override // android.app.backup.BackupTransport
    public long getCurrentRestoreSet() {
        return 1L;
    }

    @Override // android.app.backup.BackupTransport
    public int startRestore(long token, PackageInfo[] packages) {
        this.mRestorePackages = packages;
        this.mRestorePackage = -1;
        this.mRestoreSetDir = new File(this.mDataDir, Long.toString(token));
        this.mRestoreSetIncrementalDir = new File(this.mRestoreSetDir, INCREMENTAL_DIR);
        this.mRestoreSetFullDir = new File(this.mRestoreSetDir, FULL_DATA_DIR);
        return 0;
    }

    @Override // android.app.backup.BackupTransport
    public RestoreDescription nextRestorePackage() {
        String name;
        if (this.mRestorePackages == null) {
            throw new IllegalStateException("startRestore not called");
        }
        boolean found = false;
        do {
            int i = this.mRestorePackage + 1;
            this.mRestorePackage = i;
            if (i < this.mRestorePackages.length) {
                name = this.mRestorePackages[this.mRestorePackage].packageName;
                String[] contents = new File(this.mRestoreSetIncrementalDir, name).list();
                if (contents != null && contents.length > 0) {
                    this.mRestoreType = 1;
                    found = true;
                }
                if (!found) {
                    File maybeFullData = new File(this.mRestoreSetFullDir, name);
                    if (maybeFullData.length() > 0) {
                        this.mRestoreType = 2;
                        this.mCurFullRestoreStream = null;
                        found = true;
                        continue;
                    } else {
                        continue;
                    }
                }
            } else {
                return RestoreDescription.NO_MORE_PACKAGES;
            }
        } while (!found);
        return new RestoreDescription(name, this.mRestoreType);
    }

    @Override // android.app.backup.BackupTransport
    public int getRestoreData(ParcelFileDescriptor outFd) {
        if (this.mRestorePackages == null) {
            throw new IllegalStateException("startRestore not called");
        }
        if (this.mRestorePackage < 0) {
            throw new IllegalStateException("nextRestorePackage not called");
        }
        if (this.mRestoreType != 1) {
            throw new IllegalStateException("getRestoreData(fd) for non-key/value dataset");
        }
        File packageDir = new File(this.mRestoreSetIncrementalDir, this.mRestorePackages[this.mRestorePackage].packageName);
        ArrayList<DecodedFilename> blobs = contentsByKey(packageDir);
        if (blobs == null) {
            Log.e(TAG, "No keys for package: " + packageDir);
            return -1000;
        }
        BackupDataOutput out = new BackupDataOutput(outFd.getFileDescriptor());
        try {
            Iterator<DecodedFilename> it = blobs.iterator();
            while (it.hasNext()) {
                DecodedFilename keyEntry = it.next();
                File f = keyEntry.file;
                FileInputStream in = new FileInputStream(f);
                int size = (int) f.length();
                byte[] buf = new byte[size];
                in.read(buf);
                out.writeEntityHeader(keyEntry.key, size);
                out.writeEntityData(buf, size);
                in.close();
            }
            return 0;
        } catch (IOException e) {
            Log.e(TAG, "Unable to read backup records", e);
            return -1000;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class DecodedFilename implements Comparable<DecodedFilename> {
        public File file;
        public String key;

        public DecodedFilename(File f) {
            this.file = f;
            this.key = new String(Base64.decode(f.getName()));
        }

        @Override // java.lang.Comparable
        public int compareTo(DecodedFilename other) {
            return this.key.compareTo(other.key);
        }
    }

    private ArrayList<DecodedFilename> contentsByKey(File dir) {
        File[] allFiles = dir.listFiles();
        if (allFiles == null || allFiles.length == 0) {
            return null;
        }
        ArrayList<DecodedFilename> contents = new ArrayList<>();
        for (File f : allFiles) {
            contents.add(new DecodedFilename(f));
        }
        Collections.sort(contents);
        return contents;
    }

    @Override // android.app.backup.BackupTransport
    public void finishRestore() {
        if (this.mRestoreType == 2) {
            resetFullRestoreState();
        }
        this.mRestoreType = 0;
    }

    private void resetFullRestoreState() {
        IoUtils.closeQuietly(this.mCurFullRestoreStream);
        this.mCurFullRestoreStream = null;
        this.mFullRestoreSocketStream = null;
        this.mFullRestoreBuffer = null;
    }

    @Override // android.app.backup.BackupTransport
    public int getNextFullRestoreDataChunk(ParcelFileDescriptor socket) {
        if (this.mRestoreType != 2) {
            throw new IllegalStateException("Asked for full restore data for non-stream package");
        }
        if (this.mCurFullRestoreStream == null) {
            String name = this.mRestorePackages[this.mRestorePackage].packageName;
            File dataset = new File(this.mRestoreSetFullDir, name);
            try {
                this.mCurFullRestoreStream = new FileInputStream(dataset);
                this.mFullRestoreSocketStream = new FileOutputStream(socket.getFileDescriptor());
                this.mFullRestoreBuffer = new byte[2048];
            } catch (IOException e) {
                Log.e(TAG, "Unable to read archive for " + name);
                return -1002;
            }
        }
        try {
            int nRead = this.mCurFullRestoreStream.read(this.mFullRestoreBuffer);
            if (nRead < 0) {
                return -1;
            }
            if (nRead == 0) {
                Log.w(TAG, "read() of archive file returned 0; treating as EOF");
                return -1;
            }
            this.mFullRestoreSocketStream.write(this.mFullRestoreBuffer, 0, nRead);
            return nRead;
        } catch (IOException e2) {
            return -1000;
        }
    }

    @Override // android.app.backup.BackupTransport
    public int abortFullRestore() {
        if (this.mRestoreType != 2) {
            throw new IllegalStateException("abortFullRestore() but not currently restoring");
        }
        resetFullRestoreState();
        this.mRestoreType = 0;
        return 0;
    }

    @Override // android.app.backup.BackupTransport
    public long getBackupQuota(String packageName, boolean isFullBackup) {
        return isFullBackup ? FULL_BACKUP_SIZE_QUOTA : KEY_VALUE_BACKUP_SIZE_QUOTA;
    }
}
