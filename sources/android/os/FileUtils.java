package android.os;

import android.provider.DocumentsContract;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.webkit.MimeTypeMap;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.SizedInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import libcore.io.IoUtils;
import libcore.util.EmptyArray;
/* loaded from: classes2.dex */
public class FileUtils {
    private static final long COPY_CHECKPOINT_BYTES = 524288;
    private static final File[] EMPTY = new File[0];
    private static final boolean ENABLE_COPY_OPTIMIZATIONS = true;
    public static final int S_IRGRP = 32;
    public static final int S_IROTH = 4;
    public static final int S_IRUSR = 256;
    public static final int S_IRWXG = 56;
    public static final int S_IRWXO = 7;
    public static final int S_IRWXU = 448;
    public static final int S_IWGRP = 16;
    public static final int S_IWOTH = 2;
    public static final int S_IWUSR = 128;
    public static final int S_IXGRP = 8;
    public static final int S_IXOTH = 1;
    public static final int S_IXUSR = 64;
    private static final String TAG = "FileUtils";

    /* loaded from: classes2.dex */
    public interface ProgressListener {
        synchronized void onProgress(long j);
    }

    private protected FileUtils() {
    }

    /* loaded from: classes2.dex */
    private static class NoImagePreloadHolder {
        public static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("[\\w%+,./=_-]+");

        private synchronized NoImagePreloadHolder() {
        }
    }

    private protected static int setPermissions(File path, int mode, int uid, int gid) {
        return setPermissions(path.getAbsolutePath(), mode, uid, gid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int setPermissions(String path, int mode, int uid, int gid) {
        try {
            Os.chmod(path, mode);
            if (uid >= 0 || gid >= 0) {
                try {
                    Os.chown(path, uid, gid);
                    return 0;
                } catch (ErrnoException e) {
                    Slog.w(TAG, "Failed to chown(" + path + "): " + e);
                    return e.errno;
                }
            }
            return 0;
        } catch (ErrnoException e2) {
            Slog.w(TAG, "Failed to chmod(" + path + "): " + e2);
            return e2.errno;
        }
    }

    private protected static int setPermissions(FileDescriptor fd, int mode, int uid, int gid) {
        try {
            Os.fchmod(fd, mode);
            if (uid >= 0 || gid >= 0) {
                try {
                    Os.fchown(fd, uid, gid);
                    return 0;
                } catch (ErrnoException e) {
                    Slog.w(TAG, "Failed to fchown(): " + e);
                    return e.errno;
                }
            }
            return 0;
        } catch (ErrnoException e2) {
            Slog.w(TAG, "Failed to fchmod(): " + e2);
            return e2.errno;
        }
    }

    public static synchronized void copyPermissions(File from, File to) throws IOException {
        try {
            StructStat stat = Os.stat(from.getAbsolutePath());
            Os.chmod(to.getAbsolutePath(), stat.st_mode);
            Os.chown(to.getAbsolutePath(), stat.st_uid, stat.st_gid);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public static synchronized int getUid(String path) {
        try {
            return Os.stat(path).st_uid;
        } catch (ErrnoException e) {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean sync(FileOutputStream stream) {
        if (stream != null) {
            try {
                stream.getFD().sync();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    private protected static boolean copyFile(File srcFile, File destFile) {
        try {
            copyFileOrThrow(srcFile, destFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Deprecated
    public static synchronized void copyFileOrThrow(File srcFile, File destFile) throws IOException {
        InputStream in = new FileInputStream(srcFile);
        try {
            copyToFileOrThrow(in, destFile);
            $closeResource(null, in);
        } finally {
        }
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    @Deprecated
    private protected static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            copyToFileOrThrow(inputStream, destFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Deprecated
    public static synchronized void copyToFileOrThrow(InputStream in, File destFile) throws IOException {
        if (destFile.exists()) {
            destFile.delete();
        }
        FileOutputStream out = new FileOutputStream(destFile);
        try {
            copy(in, out);
            try {
                Os.fsync(out.getFD());
            } catch (ErrnoException e) {
                throw e.rethrowAsIOException();
            }
        } finally {
            $closeResource(null, out);
        }
    }

    public static synchronized long copy(File from, File to) throws IOException {
        return copy(from, to, (ProgressListener) null, (CancellationSignal) null);
    }

    public static synchronized long copy(File from, File to, ProgressListener listener, CancellationSignal signal) throws IOException {
        FileInputStream in = new FileInputStream(from);
        try {
            FileOutputStream out = new FileOutputStream(to);
            long copy = copy(in, out, listener, signal);
            $closeResource(null, out);
            $closeResource(null, in);
            return copy;
        } finally {
        }
    }

    public static synchronized long copy(InputStream in, OutputStream out) throws IOException {
        return copy(in, out, (ProgressListener) null, (CancellationSignal) null);
    }

    public static synchronized long copy(InputStream in, OutputStream out, ProgressListener listener, CancellationSignal signal) throws IOException {
        if ((in instanceof FileInputStream) && (out instanceof FileOutputStream)) {
            return copy(((FileInputStream) in).getFD(), ((FileOutputStream) out).getFD(), listener, signal);
        }
        return copyInternalUserspace(in, out, listener, signal);
    }

    public static synchronized long copy(FileDescriptor in, FileDescriptor out) throws IOException {
        return copy(in, out, (ProgressListener) null, (CancellationSignal) null);
    }

    public static synchronized long copy(FileDescriptor in, FileDescriptor out, ProgressListener listener, CancellationSignal signal) throws IOException {
        return copy(in, out, listener, signal, Long.MAX_VALUE);
    }

    public static synchronized long copy(FileDescriptor in, FileDescriptor out, ProgressListener listener, CancellationSignal signal, long count) throws IOException {
        try {
            StructStat st_in = Os.fstat(in);
            StructStat st_out = Os.fstat(out);
            if (OsConstants.S_ISREG(st_in.st_mode) && OsConstants.S_ISREG(st_out.st_mode)) {
                return copyInternalSendfile(in, out, listener, signal, count);
            }
            if (OsConstants.S_ISFIFO(st_in.st_mode) || OsConstants.S_ISFIFO(st_out.st_mode)) {
                return copyInternalSplice(in, out, listener, signal, count);
            }
            return copyInternalUserspace(in, out, listener, signal, count);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    @VisibleForTesting
    public static synchronized long copyInternalSplice(FileDescriptor in, FileDescriptor out, ProgressListener listener, CancellationSignal signal, long count) throws ErrnoException {
        long count2 = count;
        long progress = 0;
        long checkpoint = 0;
        while (true) {
            long t = Os.splice(in, null, out, null, Math.min(count2, 524288L), OsConstants.SPLICE_F_MOVE | OsConstants.SPLICE_F_MORE);
            if (t == 0) {
                break;
            }
            progress += t;
            checkpoint += t;
            count2 -= t;
            if (checkpoint >= 524288) {
                if (signal != null) {
                    signal.throwIfCanceled();
                }
                if (listener != null) {
                    listener.onProgress(progress);
                }
                checkpoint = 0;
            }
        }
        if (listener != null) {
            listener.onProgress(progress);
        }
        return progress;
    }

    @VisibleForTesting
    public static synchronized long copyInternalSendfile(FileDescriptor in, FileDescriptor out, ProgressListener listener, CancellationSignal signal, long count) throws ErrnoException {
        long count2 = count;
        long progress = 0;
        long checkpoint = 0;
        while (true) {
            long t = Os.sendfile(out, in, null, Math.min(count2, 524288L));
            if (t == 0) {
                break;
            }
            progress += t;
            checkpoint += t;
            count2 -= t;
            if (checkpoint >= 524288) {
                if (signal != null) {
                    signal.throwIfCanceled();
                }
                if (listener != null) {
                    listener.onProgress(progress);
                }
                checkpoint = 0;
            }
        }
        if (listener != null) {
            listener.onProgress(progress);
        }
        return progress;
    }

    @VisibleForTesting
    public static synchronized long copyInternalUserspace(FileDescriptor in, FileDescriptor out, ProgressListener listener, CancellationSignal signal, long count) throws IOException {
        if (count != Long.MAX_VALUE) {
            return copyInternalUserspace(new SizedInputStream(new FileInputStream(in), count), new FileOutputStream(out), listener, signal);
        }
        return copyInternalUserspace(new FileInputStream(in), new FileOutputStream(out), listener, signal);
    }

    @VisibleForTesting
    public static synchronized long copyInternalUserspace(InputStream in, OutputStream out, ProgressListener listener, CancellationSignal signal) throws IOException {
        long progress = 0;
        long checkpoint = 0;
        byte[] buffer = new byte[8192];
        while (true) {
            int t = in.read(buffer);
            if (t == -1) {
                break;
            }
            out.write(buffer, 0, t);
            progress += t;
            checkpoint += t;
            if (checkpoint >= 524288) {
                if (signal != null) {
                    signal.throwIfCanceled();
                }
                if (listener != null) {
                    listener.onProgress(progress);
                }
                checkpoint = 0;
            }
        }
        if (listener != null) {
            listener.onProgress(progress);
        }
        return progress;
    }

    private protected static boolean isFilenameSafe(File file) {
        return NoImagePreloadHolder.SAFE_FILENAME_PATTERN.matcher(file.getPath()).matches();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String readTextFile(File file, int max, String ellipsis) throws IOException {
        int len;
        int len2;
        InputStream input = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(input);
        try {
            long size = file.length();
            if (max <= 0 && (size <= 0 || max != 0)) {
                if (max >= 0) {
                    ByteArrayOutputStream contents = new ByteArrayOutputStream();
                    byte[] data = new byte[1024];
                    do {
                        len = bis.read(data);
                        if (len > 0) {
                            contents.write(data, 0, len);
                        }
                    } while (len == data.length);
                    return contents.toString();
                }
                boolean rolled = false;
                byte[] last = null;
                byte[] data2 = null;
                do {
                    if (last != null) {
                        rolled = true;
                    }
                    byte[] tmp = last;
                    last = data2;
                    data2 = tmp;
                    if (data2 == null) {
                        data2 = new byte[-max];
                    }
                    len2 = bis.read(data2);
                } while (len2 == data2.length);
                if (last != null || len2 > 0) {
                    if (last == null) {
                        return new String(data2, 0, len2);
                    }
                    if (len2 > 0) {
                        rolled = true;
                        System.arraycopy(last, len2, last, 0, last.length - len2);
                        System.arraycopy(data2, 0, last, last.length - len2, len2);
                    }
                    if (ellipsis != null && rolled) {
                        return ellipsis + new String(last);
                    }
                    return new String(last);
                }
                return "";
            }
            if (size > 0 && (max == 0 || size < max)) {
                max = (int) size;
            }
            byte[] data3 = new byte[max + 1];
            int length = bis.read(data3);
            if (length <= 0) {
                return "";
            }
            if (length <= max) {
                return new String(data3, 0, length);
            }
            if (ellipsis == null) {
                return new String(data3, 0, max);
            }
            return new String(data3, 0, max) + ellipsis;
        } finally {
            bis.close();
            input.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void stringToFile(File file, String string) throws IOException {
        stringToFile(file.getAbsolutePath(), string);
    }

    public static synchronized void bytesToFile(String filename, byte[] content) throws IOException {
        if (filename.startsWith("/proc/")) {
            int oldMask = StrictMode.allowThreadDiskWritesMask();
            try {
                FileOutputStream fos = new FileOutputStream(filename);
                fos.write(content);
                $closeResource(null, fos);
                return;
            } finally {
                StrictMode.setThreadPolicyMask(oldMask);
            }
        }
        FileOutputStream fos2 = new FileOutputStream(filename);
        try {
            fos2.write(content);
            $closeResource(null, fos2);
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                $closeResource(th, fos2);
                throw th2;
            }
        }
    }

    private protected static void stringToFile(String filename, String string) throws IOException {
        bytesToFile(filename, string.getBytes(StandardCharsets.UTF_8));
    }

    private protected static long checksumCrc32(File file) throws FileNotFoundException, IOException {
        CRC32 checkSummer = new CRC32();
        CheckedInputStream cis = null;
        try {
            cis = new CheckedInputStream(new FileInputStream(file), checkSummer);
            byte[] buf = new byte[128];
            while (cis.read(buf) >= 0) {
            }
            long value = checkSummer.getValue();
            try {
                cis.close();
            } catch (IOException e) {
            }
            return value;
        } catch (Throwable th) {
            if (cis != null) {
                try {
                    cis.close();
                } catch (IOException e2) {
                }
            }
            throw th;
        }
    }

    private protected static boolean deleteOlderFiles(File dir, int minCount, long minAgeMs) {
        if (minCount < 0 || minAgeMs < 0) {
            throw new IllegalArgumentException("Constraints must be positive or 0");
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return false;
        }
        Arrays.sort(files, new Comparator<File>() { // from class: android.os.FileUtils.1
            @Override // java.util.Comparator
            public int compare(File lhs, File rhs) {
                return Long.compare(rhs.lastModified(), lhs.lastModified());
            }
        });
        boolean deleted = false;
        for (int i = minCount; i < files.length; i++) {
            File file = files[i];
            long age = System.currentTimeMillis() - file.lastModified();
            if (age > minAgeMs && file.delete()) {
                Log.d(TAG, "Deleted old file " + file);
                deleted = true;
            }
        }
        return deleted;
    }

    public static synchronized boolean contains(File[] dirs, File file) {
        for (File dir : dirs) {
            if (contains(dir, file)) {
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean contains(File dir, File file) {
        if (dir == null || file == null) {
            return false;
        }
        return contains(dir.getAbsolutePath(), file.getAbsolutePath());
    }

    public static synchronized boolean contains(String dirPath, String filePath) {
        if (dirPath.equals(filePath)) {
            return true;
        }
        if (!dirPath.endsWith("/")) {
            dirPath = dirPath + "/";
        }
        return filePath.startsWith(dirPath);
    }

    public static synchronized boolean deleteContentsAndDir(File dir) {
        if (deleteContents(dir)) {
            return dir.delete();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean deleteContents(File dir) {
        File[] files = dir.listFiles();
        boolean success = true;
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    success &= deleteContents(file);
                }
                if (!file.delete()) {
                    Log.w(TAG, "Failed to delete " + file);
                    success = false;
                }
            }
        }
        return success;
    }

    private static synchronized boolean isValidExtFilenameChar(char c) {
        if (c == 0 || c == '/') {
            return false;
        }
        return true;
    }

    public static synchronized boolean isValidExtFilename(String name) {
        return name != null && name.equals(buildValidExtFilename(name));
    }

    public static synchronized String buildValidExtFilename(String name) {
        if (TextUtils.isEmpty(name) || ".".equals(name) || "..".equals(name)) {
            return "(invalid)";
        }
        StringBuilder res = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (isValidExtFilenameChar(c)) {
                res.append(c);
            } else {
                res.append('_');
            }
        }
        trimFilename(res, 255);
        return res.toString();
    }

    private static synchronized boolean isValidFatFilenameChar(char c) {
        if ((c < 0 || c > 31) && c != '\"' && c != '*' && c != '/' && c != ':' && c != '<' && c != '\\' && c != '|' && c != 127) {
            switch (c) {
                case '>':
                case '?':
                    break;
                default:
                    return true;
            }
        }
        return false;
    }

    public static synchronized boolean isValidFatFilename(String name) {
        return name != null && name.equals(buildValidFatFilename(name));
    }

    public static synchronized String buildValidFatFilename(String name) {
        if (TextUtils.isEmpty(name) || ".".equals(name) || "..".equals(name)) {
            return "(invalid)";
        }
        StringBuilder res = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (isValidFatFilenameChar(c)) {
                res.append(c);
            } else {
                res.append('_');
            }
        }
        trimFilename(res, 255);
        return res.toString();
    }

    @VisibleForTesting
    public static synchronized String trimFilename(String str, int maxBytes) {
        StringBuilder res = new StringBuilder(str);
        trimFilename(res, maxBytes);
        return res.toString();
    }

    private static synchronized void trimFilename(StringBuilder res, int maxBytes) {
        byte[] raw = res.toString().getBytes(StandardCharsets.UTF_8);
        if (raw.length > maxBytes) {
            int maxBytes2 = maxBytes - 3;
            while (raw.length > maxBytes2) {
                res.deleteCharAt(res.length() / 2);
                raw = res.toString().getBytes(StandardCharsets.UTF_8);
            }
            res.insert(res.length() / 2, "...");
        }
    }

    public static synchronized String rewriteAfterRename(File beforeDir, File afterDir, String path) {
        File result;
        if (path == null || (result = rewriteAfterRename(beforeDir, afterDir, new File(path))) == null) {
            return null;
        }
        return result.getAbsolutePath();
    }

    public static synchronized String[] rewriteAfterRename(File beforeDir, File afterDir, String[] paths) {
        if (paths == null) {
            return null;
        }
        String[] result = new String[paths.length];
        for (int i = 0; i < paths.length; i++) {
            result[i] = rewriteAfterRename(beforeDir, afterDir, paths[i]);
        }
        return result;
    }

    public static synchronized File rewriteAfterRename(File beforeDir, File afterDir, File file) {
        if (file == null || beforeDir == null || afterDir == null || !contains(beforeDir, file)) {
            return null;
        }
        String splice = file.getAbsolutePath().substring(beforeDir.getAbsolutePath().length());
        return new File(afterDir, splice);
    }

    private static synchronized File buildUniqueFileWithExtension(File parent, String name, String ext) throws FileNotFoundException {
        File file = buildFile(parent, name, ext);
        int n = 0;
        while (file.exists()) {
            int n2 = n + 1;
            if (n >= 32) {
                throw new FileNotFoundException("Failed to create unique file");
            }
            file = buildFile(parent, name + " (" + n2 + ")", ext);
            n = n2;
        }
        return file;
    }

    public static synchronized File buildUniqueFile(File parent, String mimeType, String displayName) throws FileNotFoundException {
        String[] parts = splitFileName(mimeType, displayName);
        return buildUniqueFileWithExtension(parent, parts[0], parts[1]);
    }

    public static synchronized File buildUniqueFile(File parent, String displayName) throws FileNotFoundException {
        String name;
        String ext;
        int lastDot = displayName.lastIndexOf(46);
        if (lastDot >= 0) {
            name = displayName.substring(0, lastDot);
            ext = displayName.substring(lastDot + 1);
        } else {
            name = displayName;
            ext = null;
        }
        return buildUniqueFileWithExtension(parent, name, ext);
    }

    public static synchronized String[] splitFileName(String mimeType, String displayName) {
        String name;
        String ext;
        String mimeTypeFromExt;
        String name2;
        String ext2;
        if (DocumentsContract.Document.MIME_TYPE_DIR.equals(mimeType)) {
            ext2 = null;
            name2 = displayName;
        } else {
            int lastDot = displayName.lastIndexOf(46);
            if (lastDot >= 0) {
                name = displayName.substring(0, lastDot);
                ext = displayName.substring(lastDot + 1);
                mimeTypeFromExt = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());
            } else {
                name = displayName;
                ext = null;
                mimeTypeFromExt = null;
            }
            String str = ext;
            name2 = name;
            ext2 = str;
            if (mimeTypeFromExt == null) {
                mimeTypeFromExt = "application/octet-stream";
            }
            String extFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
            if (!Objects.equals(mimeType, mimeTypeFromExt) && !Objects.equals(ext2, extFromMimeType)) {
                name2 = displayName;
                ext2 = extFromMimeType;
            }
        }
        if (ext2 == null) {
            ext2 = "";
        }
        return new String[]{name2, ext2};
    }

    private static synchronized File buildFile(File parent, String name, String ext) {
        if (TextUtils.isEmpty(ext)) {
            return new File(parent, name);
        }
        return new File(parent, name + "." + ext);
    }

    public static synchronized String[] listOrEmpty(File dir) {
        if (dir == null) {
            return EmptyArray.STRING;
        }
        String[] res = dir.list();
        if (res != null) {
            return res;
        }
        return EmptyArray.STRING;
    }

    public static synchronized File[] listFilesOrEmpty(File dir) {
        if (dir == null) {
            return EMPTY;
        }
        File[] res = dir.listFiles();
        if (res != null) {
            return res;
        }
        return EMPTY;
    }

    public static synchronized File[] listFilesOrEmpty(File dir, FilenameFilter filter) {
        if (dir == null) {
            return EMPTY;
        }
        File[] res = dir.listFiles(filter);
        if (res != null) {
            return res;
        }
        return EMPTY;
    }

    public static synchronized File newFileOrNull(String path) {
        if (path != null) {
            return new File(path);
        }
        return null;
    }

    public static synchronized File createDir(File baseDir, String name) {
        File dir = new File(baseDir, name);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                return dir;
            }
            return null;
        } else if (dir.mkdir()) {
            return dir;
        } else {
            return null;
        }
    }

    public static synchronized long roundStorageSize(long size) {
        long val = 1;
        long pow = 1;
        while (val * pow < size) {
            val <<= 1;
            if (val > 512) {
                val = 1;
                pow *= 1000;
            }
        }
        return val * pow;
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    public static class MemoryPipe extends Thread implements AutoCloseable {
        private final byte[] data;
        private final FileDescriptor[] pipe;
        private final boolean sink;

        private synchronized MemoryPipe(byte[] data, boolean sink) throws IOException {
            try {
                this.pipe = Os.pipe();
                this.data = data;
                this.sink = sink;
            } catch (ErrnoException e) {
                throw e.rethrowAsIOException();
            }
        }

        private synchronized MemoryPipe startInternal() {
            super.start();
            return this;
        }

        public static synchronized MemoryPipe createSource(byte[] data) throws IOException {
            return new MemoryPipe(data, false).startInternal();
        }

        public static synchronized MemoryPipe createSink(byte[] data) throws IOException {
            return new MemoryPipe(data, true).startInternal();
        }

        public synchronized FileDescriptor getFD() {
            return this.sink ? this.pipe[1] : this.pipe[0];
        }

        public synchronized FileDescriptor getInternalFD() {
            return this.sink ? this.pipe[0] : this.pipe[1];
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x002a, code lost:
            if (r6.sink != false) goto L22;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x0042, code lost:
            if (r6.sink == false) goto L19;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0044, code lost:
            android.os.SystemClock.sleep(java.util.concurrent.TimeUnit.SECONDS.toMillis(1));
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x004d, code lost:
            libcore.io.IoUtils.closeQuietly(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x0051, code lost:
            return;
         */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                r6 = this;
                java.io.FileDescriptor r0 = r6.getInternalFD()
                r1 = 0
            L5:
                r2 = 1
                byte[] r4 = r6.data     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3f
                int r4 = r4.length     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3f
                if (r1 >= r4) goto L28
                boolean r4 = r6.sink     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3f
                if (r4 == 0) goto L1c
                byte[] r4 = r6.data     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3f
                byte[] r5 = r6.data     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3f
                int r5 = r5.length     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3f
                int r5 = r5 - r1
                int r4 = android.system.Os.read(r0, r4, r1, r5)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3f
                int r1 = r1 + r4
                goto L5
            L1c:
                byte[] r4 = r6.data     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3f
                byte[] r5 = r6.data     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3f
                int r5 = r5.length     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3f
                int r5 = r5 - r1
                int r4 = android.system.Os.write(r0, r4, r1, r5)     // Catch: java.lang.Throwable -> L2d java.lang.Throwable -> L3f
                int r1 = r1 + r4
                goto L5
            L28:
                boolean r1 = r6.sink
                if (r1 == 0) goto L4d
                goto L44
            L2d:
                r1 = move-exception
                boolean r4 = r6.sink
                if (r4 == 0) goto L3b
                java.util.concurrent.TimeUnit r4 = java.util.concurrent.TimeUnit.SECONDS
                long r2 = r4.toMillis(r2)
                android.os.SystemClock.sleep(r2)
            L3b:
                libcore.io.IoUtils.closeQuietly(r0)
                throw r1
            L3f:
                r1 = move-exception
                boolean r1 = r6.sink
                if (r1 == 0) goto L4d
            L44:
                java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.SECONDS
                long r1 = r1.toMillis(r2)
                android.os.SystemClock.sleep(r1)
            L4d:
                libcore.io.IoUtils.closeQuietly(r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: android.os.FileUtils.MemoryPipe.run():void");
        }

        @Override // java.lang.AutoCloseable
        public void close() throws Exception {
            IoUtils.closeQuietly(getFD());
        }
    }
}
