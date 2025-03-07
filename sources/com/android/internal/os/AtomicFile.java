package com.android.internal.os;

import android.annotation.UnsupportedAppUsage;
import android.os.FileUtils;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes3.dex */
public final class AtomicFile {
    private final File mBackupName;
    private final File mBaseName;

    @UnsupportedAppUsage
    public AtomicFile(File baseName) {
        this.mBaseName = baseName;
        this.mBackupName = new File(baseName.getPath() + ".bak");
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public File getBaseFile() {
        return this.mBaseName;
    }

    @UnsupportedAppUsage
    public FileOutputStream startWrite() throws IOException {
        if (this.mBaseName.exists()) {
            if (!this.mBackupName.exists()) {
                if (!this.mBaseName.renameTo(this.mBackupName)) {
                    Log.w("AtomicFile", "Couldn't rename file " + this.mBaseName + " to backup file " + this.mBackupName);
                }
            } else {
                this.mBaseName.delete();
            }
        }
        try {
            FileOutputStream str = new FileOutputStream(this.mBaseName);
            return str;
        } catch (FileNotFoundException e) {
            File parent = this.mBaseName.getParentFile();
            if (!parent.mkdir()) {
                throw new IOException("Couldn't create directory " + this.mBaseName);
            }
            FileUtils.setPermissions(parent.getPath(), 505, -1, -1);
            try {
                FileOutputStream str2 = new FileOutputStream(this.mBaseName);
                return str2;
            } catch (FileNotFoundException e2) {
                throw new IOException("Couldn't create " + this.mBaseName);
            }
        }
    }

    @UnsupportedAppUsage
    public void finishWrite(FileOutputStream str) {
        if (str != null) {
            FileUtils.sync(str);
            try {
                str.close();
                this.mBackupName.delete();
            } catch (IOException e) {
                Log.w("AtomicFile", "finishWrite: Got exception:", e);
            }
        }
    }

    @UnsupportedAppUsage
    public void failWrite(FileOutputStream str) {
        if (str != null) {
            FileUtils.sync(str);
            try {
                str.close();
                this.mBaseName.delete();
                this.mBackupName.renameTo(this.mBaseName);
            } catch (IOException e) {
                Log.w("AtomicFile", "failWrite: Got exception:", e);
            }
        }
    }

    @UnsupportedAppUsage
    public FileOutputStream openAppend() throws IOException {
        try {
            return new FileOutputStream(this.mBaseName, true);
        } catch (FileNotFoundException e) {
            throw new IOException("Couldn't append " + this.mBaseName);
        }
    }

    @UnsupportedAppUsage
    public void truncate() throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(this.mBaseName);
            FileUtils.sync(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            throw new IOException("Couldn't append " + this.mBaseName);
        } catch (IOException e2) {
        }
    }

    public boolean exists() {
        return this.mBaseName.exists() || this.mBackupName.exists();
    }

    public void delete() {
        this.mBaseName.delete();
        this.mBackupName.delete();
    }

    @UnsupportedAppUsage
    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mBackupName.exists()) {
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
        }
        return new FileInputStream(this.mBaseName);
    }

    @UnsupportedAppUsage
    public byte[] readFully() throws IOException {
        FileInputStream stream = openRead();
        int pos = 0;
        try {
            byte[] data = new byte[stream.available()];
            while (true) {
                int amt = stream.read(data, pos, data.length - pos);
                if (amt <= 0) {
                    return data;
                }
                pos += amt;
                int avail = stream.available();
                if (avail > data.length - pos) {
                    byte[] newData = new byte[pos + avail];
                    System.arraycopy(data, 0, newData, 0, pos);
                    data = newData;
                }
            }
        } finally {
            stream.close();
        }
    }
}
