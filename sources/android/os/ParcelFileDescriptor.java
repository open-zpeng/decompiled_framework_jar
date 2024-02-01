package android.os;

import android.app.backup.FullBackup;
import android.os.MessageQueue;
import android.os.Parcelable;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.ByteOrder;
import libcore.io.IoUtils;
import libcore.io.Memory;
/* loaded from: classes2.dex */
public class ParcelFileDescriptor implements Parcelable, Closeable {
    public static final Parcelable.Creator<ParcelFileDescriptor> CREATOR = new Parcelable.Creator<ParcelFileDescriptor>() { // from class: android.os.ParcelFileDescriptor.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelFileDescriptor createFromParcel(Parcel in) {
            int hasCommChannel = in.readInt();
            FileDescriptor fd = in.readRawFileDescriptor();
            FileDescriptor commChannel = null;
            if (hasCommChannel != 0) {
                commChannel = in.readRawFileDescriptor();
            }
            return new ParcelFileDescriptor(fd, commChannel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelFileDescriptor[] newArray(int size) {
            return new ParcelFileDescriptor[size];
        }
    };
    private static final int MAX_STATUS = 1024;
    public static final int MODE_APPEND = 33554432;
    public static final int MODE_CREATE = 134217728;
    public static final int MODE_READ_ONLY = 268435456;
    public static final int MODE_READ_WRITE = 805306368;
    public static final int MODE_TRUNCATE = 67108864;
    @Deprecated
    public static final int MODE_WORLD_READABLE = 1;
    @Deprecated
    public static final int MODE_WORLD_WRITEABLE = 2;
    public static final int MODE_WRITE_ONLY = 536870912;
    private static final String TAG = "ParcelFileDescriptor";
    private volatile boolean mClosed;
    private FileDescriptor mCommFd;
    private final FileDescriptor mFd;
    private final CloseGuard mGuard;
    private Status mStatus;
    private byte[] mStatusBuf;
    private final ParcelFileDescriptor mWrapped;

    /* loaded from: classes2.dex */
    public interface OnCloseListener {
        void onClose(IOException iOException);
    }

    public ParcelFileDescriptor(ParcelFileDescriptor wrapped) {
        this.mGuard = CloseGuard.get();
        this.mWrapped = wrapped;
        this.mFd = null;
        this.mCommFd = null;
        this.mClosed = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ParcelFileDescriptor(FileDescriptor fd) {
        this(fd, null);
    }

    public synchronized ParcelFileDescriptor(FileDescriptor fd, FileDescriptor commChannel) {
        this.mGuard = CloseGuard.get();
        if (fd == null) {
            throw new NullPointerException("FileDescriptor must not be null");
        }
        this.mWrapped = null;
        this.mFd = fd;
        this.mCommFd = commChannel;
        this.mGuard.open("close");
    }

    public static ParcelFileDescriptor open(File file, int mode) throws FileNotFoundException {
        FileDescriptor fd = openInternal(file, mode);
        if (fd == null) {
            return null;
        }
        return new ParcelFileDescriptor(fd);
    }

    public static ParcelFileDescriptor open(File file, int mode, Handler handler, OnCloseListener listener) throws IOException {
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }
        FileDescriptor fd = openInternal(file, mode);
        if (fd == null) {
            return null;
        }
        return fromFd(fd, handler, listener);
    }

    public static synchronized ParcelFileDescriptor fromFd(FileDescriptor fd, Handler handler, final OnCloseListener listener) throws IOException {
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }
        FileDescriptor[] comm = createCommSocketPair();
        ParcelFileDescriptor pfd = new ParcelFileDescriptor(fd, comm[0]);
        final MessageQueue queue = handler.getLooper().getQueue();
        queue.addOnFileDescriptorEventListener(comm[1], 1, new MessageQueue.OnFileDescriptorEventListener() { // from class: android.os.ParcelFileDescriptor.1
            @Override // android.os.MessageQueue.OnFileDescriptorEventListener
            public int onFileDescriptorEvents(FileDescriptor fd2, int events) {
                Status status = null;
                if ((events & 1) != 0) {
                    byte[] buf = new byte[1024];
                    status = ParcelFileDescriptor.readCommStatus(fd2, buf);
                } else if ((events & 4) != 0) {
                    status = new Status(-2);
                }
                if (status != null) {
                    MessageQueue.this.removeOnFileDescriptorEventListener(fd2);
                    IoUtils.closeQuietly(fd2);
                    listener.onClose(status.asIOException());
                    return 0;
                }
                return 1;
            }
        });
        return pfd;
    }

    private static synchronized FileDescriptor openInternal(File file, int mode) throws FileNotFoundException {
        if ((mode & 805306368) == 0) {
            throw new IllegalArgumentException("Must specify MODE_READ_ONLY, MODE_WRITE_ONLY, or MODE_READ_WRITE");
        }
        int flags = 0;
        int i = mode & 805306368;
        if (i == 0 || i == 268435456) {
            flags = OsConstants.O_RDONLY;
        } else if (i != 536870912) {
            if (i == 805306368) {
                flags = OsConstants.O_RDWR;
            }
        } else {
            flags = OsConstants.O_WRONLY;
        }
        if ((134217728 & mode) != 0) {
            flags |= OsConstants.O_CREAT;
        }
        if ((67108864 & mode) != 0) {
            flags |= OsConstants.O_TRUNC;
        }
        if ((33554432 & mode) != 0) {
            flags |= OsConstants.O_APPEND;
        }
        int realMode = OsConstants.S_IRWXU | OsConstants.S_IRWXG;
        if ((mode & 1) != 0) {
            realMode |= OsConstants.S_IROTH;
        }
        if ((mode & 2) != 0) {
            realMode |= OsConstants.S_IWOTH;
        }
        String path = file.getPath();
        try {
            return Os.open(path, flags, realMode);
        } catch (ErrnoException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    public static ParcelFileDescriptor dup(FileDescriptor orig) throws IOException {
        try {
            FileDescriptor fd = Os.dup(orig);
            return new ParcelFileDescriptor(fd);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public ParcelFileDescriptor dup() throws IOException {
        if (this.mWrapped != null) {
            return this.mWrapped.dup();
        }
        return dup(getFileDescriptor());
    }

    public static ParcelFileDescriptor fromFd(int fd) throws IOException {
        FileDescriptor original = new FileDescriptor();
        original.setInt$(fd);
        try {
            FileDescriptor dup = Os.dup(original);
            return new ParcelFileDescriptor(dup);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public static ParcelFileDescriptor adoptFd(int fd) {
        FileDescriptor fdesc = new FileDescriptor();
        fdesc.setInt$(fd);
        return new ParcelFileDescriptor(fdesc);
    }

    public static ParcelFileDescriptor fromSocket(Socket socket) {
        FileDescriptor fd = socket.getFileDescriptor$();
        if (fd != null) {
            return new ParcelFileDescriptor(fd);
        }
        return null;
    }

    public static ParcelFileDescriptor fromDatagramSocket(DatagramSocket datagramSocket) {
        FileDescriptor fd = datagramSocket.getFileDescriptor$();
        if (fd != null) {
            return new ParcelFileDescriptor(fd);
        }
        return null;
    }

    public static ParcelFileDescriptor[] createPipe() throws IOException {
        try {
            FileDescriptor[] fds = Os.pipe();
            return new ParcelFileDescriptor[]{new ParcelFileDescriptor(fds[0]), new ParcelFileDescriptor(fds[1])};
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public static ParcelFileDescriptor[] createReliablePipe() throws IOException {
        try {
            FileDescriptor[] comm = createCommSocketPair();
            FileDescriptor[] fds = Os.pipe();
            return new ParcelFileDescriptor[]{new ParcelFileDescriptor(fds[0], comm[0]), new ParcelFileDescriptor(fds[1], comm[1])};
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public static ParcelFileDescriptor[] createSocketPair() throws IOException {
        return createSocketPair(OsConstants.SOCK_STREAM);
    }

    public static synchronized ParcelFileDescriptor[] createSocketPair(int type) throws IOException {
        try {
            FileDescriptor fd0 = new FileDescriptor();
            FileDescriptor fd1 = new FileDescriptor();
            Os.socketpair(OsConstants.AF_UNIX, type, 0, fd0, fd1);
            return new ParcelFileDescriptor[]{new ParcelFileDescriptor(fd0), new ParcelFileDescriptor(fd1)};
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public static ParcelFileDescriptor[] createReliableSocketPair() throws IOException {
        return createReliableSocketPair(OsConstants.SOCK_STREAM);
    }

    public static synchronized ParcelFileDescriptor[] createReliableSocketPair(int type) throws IOException {
        try {
            FileDescriptor[] comm = createCommSocketPair();
            FileDescriptor fd0 = new FileDescriptor();
            FileDescriptor fd1 = new FileDescriptor();
            Os.socketpair(OsConstants.AF_UNIX, type, 0, fd0, fd1);
            return new ParcelFileDescriptor[]{new ParcelFileDescriptor(fd0, comm[0]), new ParcelFileDescriptor(fd1, comm[1])};
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    private static synchronized FileDescriptor[] createCommSocketPair() throws IOException {
        try {
            FileDescriptor comm1 = new FileDescriptor();
            FileDescriptor comm2 = new FileDescriptor();
            Os.socketpair(OsConstants.AF_UNIX, OsConstants.SOCK_SEQPACKET, 0, comm1, comm2);
            IoUtils.setBlocking(comm1, false);
            IoUtils.setBlocking(comm2, false);
            return new FileDescriptor[]{comm1, comm2};
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    private static byte[] intToByteArray(int intData) {
        byte[] byteArray = {(byte) (intData & 255), (byte) ((intData >> 8) & 255), (byte) ((intData >> 16) & 255), (byte) ((intData >> 24) & 255)};
        return byteArray;
    }

    public static ParcelFileDescriptor fromData(byte[] data1, byte[] data2, String name) throws IOException {
        int data1Length = data1 == null ? 0 : data1.length;
        int data2Length = data2 == null ? 0 : data2.length;
        if (data1Length == 0 && data2Length == 0) {
            return null;
        }
        byte[] data1LengthArray = intToByteArray(data1Length);
        int data1LengthArrayLength = data1LengthArray.length;
        int length = data1LengthArrayLength + data1Length + data2Length;
        MemoryFile file = new MemoryFile(name, length);
        file.writeBytes(data1LengthArray, 0, 0, data1LengthArrayLength);
        if (data1 != null) {
            file.writeBytes(data1, 0, data1LengthArrayLength, data1Length);
        }
        if (data2 != null) {
            file.writeBytes(data2, 0, data1LengthArrayLength + data1Length, data2Length);
        }
        try {
            file.deactivate();
            FileDescriptor fd = file.getFileDescriptor();
            return fd != null ? dup(fd) : null;
        } finally {
            file.close();
        }
    }

    @Deprecated
    private protected static ParcelFileDescriptor fromData(byte[] data, String name) throws IOException {
        if (data == null) {
            return null;
        }
        MemoryFile file = new MemoryFile(name, data.length);
        try {
            if (data.length > 0) {
                file.writeBytes(data, 0, 0, data.length);
            }
            file.deactivate();
            FileDescriptor fd = file.getFileDescriptor();
            return fd != null ? dup(fd) : null;
        } finally {
            file.close();
        }
    }

    public static int parseMode(String mode) {
        if (FullBackup.ROOT_TREE_TOKEN.equals(mode)) {
            return 268435456;
        }
        if ("w".equals(mode) || "wt".equals(mode)) {
            return 738197504;
        }
        if ("wa".equals(mode)) {
            return 704643072;
        }
        if ("rw".equals(mode)) {
            return 939524096;
        }
        if ("rwt".equals(mode)) {
            return 1006632960;
        }
        throw new IllegalArgumentException("Bad mode '" + mode + "'");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File getFile(FileDescriptor fd) throws IOException {
        try {
            String path = Os.readlink("/proc/self/fd/" + fd.getInt$());
            if (OsConstants.S_ISREG(Os.stat(path).st_mode)) {
                return new File(path);
            }
            throw new IOException("Not a regular file: " + path);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public FileDescriptor getFileDescriptor() {
        if (this.mWrapped != null) {
            return this.mWrapped.getFileDescriptor();
        }
        return this.mFd;
    }

    public long getStatSize() {
        if (this.mWrapped != null) {
            return this.mWrapped.getStatSize();
        }
        try {
            StructStat st = Os.fstat(this.mFd);
            if (!OsConstants.S_ISREG(st.st_mode) && !OsConstants.S_ISLNK(st.st_mode)) {
                return -1L;
            }
            return st.st_size;
        } catch (ErrnoException e) {
            Log.w(TAG, "fstat() failed: " + e);
            return -1L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long seekTo(long pos) throws IOException {
        if (this.mWrapped != null) {
            return this.mWrapped.seekTo(pos);
        }
        try {
            return Os.lseek(this.mFd, pos, OsConstants.SEEK_SET);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public int getFd() {
        if (this.mWrapped != null) {
            return this.mWrapped.getFd();
        }
        if (this.mClosed) {
            throw new IllegalStateException("Already closed");
        }
        return this.mFd.getInt$();
    }

    public int detachFd() {
        if (this.mWrapped != null) {
            return this.mWrapped.detachFd();
        }
        if (this.mClosed) {
            throw new IllegalStateException("Already closed");
        }
        int fd = getFd();
        this.mFd.setInt$(-1);
        writeCommStatusAndClose(2, null);
        this.mClosed = true;
        this.mGuard.close();
        releaseResources();
        return fd;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.mWrapped != null) {
            try {
                this.mWrapped.close();
                return;
            } finally {
                releaseResources();
            }
        }
        closeWithStatus(0, null);
    }

    public void closeWithError(String msg) throws IOException {
        if (this.mWrapped != null) {
            try {
                this.mWrapped.closeWithError(msg);
            } finally {
                releaseResources();
            }
        } else if (msg == null) {
            throw new IllegalArgumentException("Message must not be null");
        } else {
            closeWithStatus(1, msg);
        }
    }

    private synchronized void closeWithStatus(int status, String msg) {
        if (this.mClosed) {
            return;
        }
        this.mClosed = true;
        if (this.mGuard != null) {
            this.mGuard.close();
        }
        writeCommStatusAndClose(status, msg);
        IoUtils.closeQuietly(this.mFd);
        releaseResources();
    }

    public synchronized void releaseResources() {
    }

    private synchronized byte[] getOrCreateStatusBuffer() {
        if (this.mStatusBuf == null) {
            this.mStatusBuf = new byte[1024];
        }
        return this.mStatusBuf;
    }

    private synchronized void writeCommStatusAndClose(int status, String msg) {
        if (this.mCommFd == null) {
            if (msg != null) {
                Log.w(TAG, "Unable to inform peer: " + msg);
                return;
            }
            return;
        }
        if (status == 2) {
            Log.w(TAG, "Peer expected signal when closed; unable to deliver after detach");
        }
        if (status == -1) {
            return;
        }
        try {
            this.mStatus = readCommStatus(this.mCommFd, getOrCreateStatusBuffer());
            if (this.mStatus != null) {
                return;
            }
            try {
                try {
                    byte[] buf = getOrCreateStatusBuffer();
                    Memory.pokeInt(buf, 0, status, ByteOrder.BIG_ENDIAN);
                    int writePtr = 0 + 4;
                    if (msg != null) {
                        byte[] rawMsg = msg.getBytes();
                        int len = Math.min(rawMsg.length, buf.length - writePtr);
                        System.arraycopy(rawMsg, 0, buf, writePtr, len);
                        writePtr += len;
                    }
                    Os.write(this.mCommFd, buf, 0, writePtr);
                } catch (ErrnoException e) {
                    Log.w(TAG, "Failed to report status: " + e);
                }
            } catch (InterruptedIOException e2) {
                Log.w(TAG, "Failed to report status: " + e2);
            }
        } finally {
            IoUtils.closeQuietly(this.mCommFd);
            this.mCommFd = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized Status readCommStatus(FileDescriptor comm, byte[] buf) {
        try {
            int n = Os.read(comm, buf, 0, buf.length);
            if (n == 0) {
                return new Status(-2);
            }
            int status = Memory.peekInt(buf, 0, ByteOrder.BIG_ENDIAN);
            if (status == 1) {
                String msg = new String(buf, 4, n - 4);
                return new Status(status, msg);
            }
            return new Status(status);
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.EAGAIN) {
                return null;
            }
            Log.d(TAG, "Failed to read status; assuming dead: " + e);
            return new Status(-2);
        } catch (InterruptedIOException e2) {
            Log.d(TAG, "Failed to read status; assuming dead: " + e2);
            return new Status(-2);
        }
    }

    public boolean canDetectErrors() {
        if (this.mWrapped != null) {
            return this.mWrapped.canDetectErrors();
        }
        return this.mCommFd != null;
    }

    public void checkError() throws IOException {
        if (this.mWrapped != null) {
            this.mWrapped.checkError();
            return;
        }
        if (this.mStatus == null) {
            if (this.mCommFd == null) {
                Log.w(TAG, "Peer didn't provide a comm channel; unable to check for errors");
                return;
            }
            this.mStatus = readCommStatus(this.mCommFd, getOrCreateStatusBuffer());
        }
        if (this.mStatus == null || this.mStatus.status == 0) {
            return;
        }
        throw this.mStatus.asIOException();
    }

    /* loaded from: classes2.dex */
    public static class AutoCloseInputStream extends FileInputStream {
        private final ParcelFileDescriptor mPfd;

        public AutoCloseInputStream(ParcelFileDescriptor pfd) {
            super(pfd.getFileDescriptor());
            this.mPfd = pfd;
        }

        @Override // java.io.FileInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            try {
                this.mPfd.close();
            } finally {
                super.close();
            }
        }

        @Override // java.io.FileInputStream, java.io.InputStream
        public int read() throws IOException {
            int result = super.read();
            if (result == -1 && this.mPfd.canDetectErrors()) {
                this.mPfd.checkError();
            }
            return result;
        }

        @Override // java.io.FileInputStream, java.io.InputStream
        public int read(byte[] b) throws IOException {
            int result = super.read(b);
            if (result == -1 && this.mPfd.canDetectErrors()) {
                this.mPfd.checkError();
            }
            return result;
        }

        @Override // java.io.FileInputStream, java.io.InputStream
        public int read(byte[] b, int off, int len) throws IOException {
            int result = super.read(b, off, len);
            if (result == -1 && this.mPfd.canDetectErrors()) {
                this.mPfd.checkError();
            }
            return result;
        }
    }

    /* loaded from: classes2.dex */
    public static class AutoCloseOutputStream extends FileOutputStream {
        private final ParcelFileDescriptor mPfd;

        public AutoCloseOutputStream(ParcelFileDescriptor pfd) {
            super(pfd.getFileDescriptor());
            this.mPfd = pfd;
        }

        @Override // java.io.FileOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            try {
                this.mPfd.close();
            } finally {
                super.close();
            }
        }
    }

    public String toString() {
        if (this.mWrapped != null) {
            return this.mWrapped.toString();
        }
        return "{ParcelFileDescriptor: " + this.mFd + "}";
    }

    protected void finalize() throws Throwable {
        if (this.mWrapped != null) {
            releaseResources();
        }
        if (this.mGuard != null) {
            this.mGuard.warnIfOpen();
        }
        try {
            if (!this.mClosed) {
                closeWithStatus(3, null);
            }
        } finally {
            super.finalize();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        if (this.mWrapped != null) {
            return this.mWrapped.describeContents();
        }
        return 1;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (this.mWrapped != null) {
            try {
                this.mWrapped.writeToParcel(out, flags);
                return;
            } finally {
                releaseResources();
            }
        }
        if (this.mCommFd != null) {
            out.writeInt(1);
            out.writeFileDescriptor(this.mFd);
            out.writeFileDescriptor(this.mCommFd);
        } else {
            out.writeInt(0);
            out.writeFileDescriptor(this.mFd);
        }
        if ((flags & 1) != 0 && !this.mClosed) {
            closeWithStatus(-1, null);
        }
    }

    /* loaded from: classes2.dex */
    public static class FileDescriptorDetachedException extends IOException {
        public protected static final long serialVersionUID = 955542466045L;

        public FileDescriptorDetachedException() {
            super("Remote side is detached");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class Status {
        public static final int DEAD = -2;
        public static final int DETACHED = 2;
        public static final int ERROR = 1;
        public static final int LEAKED = 3;
        public static final int OK = 0;
        public static final int SILENCE = -1;
        public final String msg;
        public final int status;

        public synchronized Status(int status) {
            this(status, null);
        }

        public synchronized Status(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public synchronized IOException asIOException() {
            int i = this.status;
            if (i == -2) {
                return new IOException("Remote side is dead");
            }
            switch (i) {
                case 0:
                    return null;
                case 1:
                    return new IOException("Remote error: " + this.msg);
                case 2:
                    return new FileDescriptorDetachedException();
                case 3:
                    return new IOException("Remote side was leaked");
                default:
                    return new IOException("Unknown status: " + this.status);
            }
        }

        public String toString() {
            return "{" + this.status + ": " + this.msg + "}";
        }
    }
}
