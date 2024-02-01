package android.util;

import android.os.FileUtils;
import android.os.SystemClock;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class AtomicFile {
    private final File mBackupName;
    private final File mBaseName;
    private final String mCommitTag;
    private long mStartTime;

    public AtomicFile(File baseName) {
        this(baseName, null);
    }

    public synchronized AtomicFile(File baseName, String commitTag) {
        this.mBaseName = baseName;
        this.mBackupName = new File(baseName.getPath() + ".bak");
        this.mCommitTag = commitTag;
    }

    public File getBaseFile() {
        return this.mBaseName;
    }

    public void delete() {
        this.mBaseName.delete();
        this.mBackupName.delete();
    }

    public FileOutputStream startWrite() throws IOException {
        return startWrite(this.mCommitTag != null ? SystemClock.uptimeMillis() : 0L);
    }

    public synchronized FileOutputStream startWrite(long startTime) throws IOException {
        this.mStartTime = startTime;
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
            if (!parent.mkdirs()) {
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

    public void finishWrite(FileOutputStream str) {
        if (str != null) {
            FileUtils.sync(str);
            try {
                str.close();
                this.mBackupName.delete();
            } catch (IOException e) {
                Log.w("AtomicFile", "finishWrite: Got exception:", e);
            }
            String str2 = this.mCommitTag;
        }
    }

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

    @Deprecated
    public synchronized void truncate() throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(this.mBaseName);
            FileUtils.sync(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            throw new IOException("Couldn't append " + this.mBaseName);
        } catch (IOException e2) {
        }
    }

    @Deprecated
    public synchronized FileOutputStream openAppend() throws IOException {
        try {
            return new FileOutputStream(this.mBaseName, true);
        } catch (FileNotFoundException e) {
            throw new IOException("Couldn't append " + this.mBaseName);
        }
    }

    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mBackupName.exists()) {
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
        }
        return new FileInputStream(this.mBaseName);
    }

    public synchronized boolean exists() {
        return this.mBaseName.exists() || this.mBackupName.exists();
    }

    public synchronized long getLastModifiedTime() {
        if (this.mBackupName.exists()) {
            return this.mBackupName.lastModified();
        }
        return this.mBaseName.lastModified();
    }

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

    public synchronized void write(Consumer<FileOutputStream> writeContent) {
        FileOutputStream out = null;
        try {
            out = startWrite();
            writeContent.accept(out);
            finishWrite(out);
        } finally {
        }
    }
}
