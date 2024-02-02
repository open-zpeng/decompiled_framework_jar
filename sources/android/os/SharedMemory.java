package android.os;

import android.os.Parcelable;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import dalvik.system.VMRuntime;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import java.nio.DirectByteBuffer;
import java.nio.NioUtils;
import sun.misc.Cleaner;
/* loaded from: classes2.dex */
public final class SharedMemory implements Parcelable, Closeable {
    private Cleaner mCleaner;
    private final FileDescriptor mFileDescriptor;
    private final MemoryRegistration mMemoryRegistration;
    private final int mSize;
    private static final int PROT_MASK = ((OsConstants.PROT_READ | OsConstants.PROT_WRITE) | OsConstants.PROT_EXEC) | OsConstants.PROT_NONE;
    public static final Parcelable.Creator<SharedMemory> CREATOR = new Parcelable.Creator<SharedMemory>() { // from class: android.os.SharedMemory.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SharedMemory createFromParcel(Parcel source) {
            FileDescriptor descriptor = source.readRawFileDescriptor();
            return new SharedMemory(descriptor);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SharedMemory[] newArray(int size) {
            return new SharedMemory[size];
        }
    };

    private static native FileDescriptor nCreate(String str, int i) throws ErrnoException;

    private static native int nGetSize(FileDescriptor fileDescriptor);

    private static native int nSetProt(FileDescriptor fileDescriptor, int i);

    private synchronized SharedMemory(FileDescriptor fd) {
        if (fd == null) {
            throw new IllegalArgumentException("Unable to create SharedMemory from a null FileDescriptor");
        }
        if (!fd.valid()) {
            throw new IllegalArgumentException("Unable to create SharedMemory from closed FileDescriptor");
        }
        this.mFileDescriptor = fd;
        this.mSize = nGetSize(this.mFileDescriptor);
        if (this.mSize <= 0) {
            throw new IllegalArgumentException("FileDescriptor is not a valid ashmem fd");
        }
        this.mMemoryRegistration = new MemoryRegistration(this.mSize);
        this.mCleaner = Cleaner.create(this.mFileDescriptor, new Closer(this.mFileDescriptor, this.mMemoryRegistration));
    }

    public static SharedMemory create(String name, int size) throws ErrnoException {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than zero");
        }
        return new SharedMemory(nCreate(name, size));
    }

    private synchronized void checkOpen() {
        if (!this.mFileDescriptor.valid()) {
            throw new IllegalStateException("SharedMemory is closed");
        }
    }

    private static synchronized void validateProt(int prot) {
        if (((~PROT_MASK) & prot) != 0) {
            throw new IllegalArgumentException("Invalid prot value");
        }
    }

    public boolean setProtect(int prot) {
        checkOpen();
        validateProt(prot);
        int errno = nSetProt(this.mFileDescriptor, prot);
        return errno == 0;
    }

    public synchronized FileDescriptor getFileDescriptor() {
        return this.mFileDescriptor;
    }

    private protected int getFd() {
        return this.mFileDescriptor.getInt$();
    }

    public int getSize() {
        checkOpen();
        return this.mSize;
    }

    public ByteBuffer mapReadWrite() throws ErrnoException {
        return map(OsConstants.PROT_READ | OsConstants.PROT_WRITE, 0, this.mSize);
    }

    public ByteBuffer mapReadOnly() throws ErrnoException {
        return map(OsConstants.PROT_READ, 0, this.mSize);
    }

    public ByteBuffer map(int prot, int offset, int length) throws ErrnoException {
        checkOpen();
        validateProt(prot);
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must be >= 0");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be > 0");
        }
        if (offset + length > this.mSize) {
            throw new IllegalArgumentException("offset + length must not exceed getSize()");
        }
        long address = Os.mmap(0L, length, prot, OsConstants.MAP_SHARED, this.mFileDescriptor, offset);
        boolean readOnly = (prot & OsConstants.PROT_WRITE) == 0;
        Runnable unmapper = new Unmapper(address, length, this.mMemoryRegistration.acquire());
        return new DirectByteBuffer(length, address, this.mFileDescriptor, unmapper, readOnly);
    }

    public static void unmap(ByteBuffer buffer) {
        if (buffer instanceof DirectByteBuffer) {
            NioUtils.freeDirectBuffer(buffer);
            return;
        }
        throw new IllegalArgumentException("ByteBuffer wasn't created by #map(int, int, int); can't unmap");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.mCleaner != null) {
            this.mCleaner.clean();
            this.mCleaner = null;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 1;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        checkOpen();
        dest.writeFileDescriptor(this.mFileDescriptor);
    }

    /* loaded from: classes2.dex */
    private static final class Closer implements Runnable {
        private FileDescriptor mFd;
        private MemoryRegistration mMemoryReference;

        private synchronized Closer(FileDescriptor fd, MemoryRegistration memoryReference) {
            this.mFd = fd;
            this.mMemoryReference = memoryReference;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Os.close(this.mFd);
            } catch (ErrnoException e) {
            }
            this.mMemoryReference.release();
            this.mMemoryReference = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class Unmapper implements Runnable {
        private long mAddress;
        private MemoryRegistration mMemoryReference;
        private int mSize;

        private synchronized Unmapper(long address, int size, MemoryRegistration memoryReference) {
            this.mAddress = address;
            this.mSize = size;
            this.mMemoryReference = memoryReference;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Os.munmap(this.mAddress, this.mSize);
            } catch (ErrnoException e) {
            }
            this.mMemoryReference.release();
            this.mMemoryReference = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class MemoryRegistration {
        private int mReferenceCount;
        private int mSize;

        private synchronized MemoryRegistration(int size) {
            this.mSize = size;
            this.mReferenceCount = 1;
            VMRuntime.getRuntime().registerNativeAllocation(this.mSize);
        }

        public synchronized MemoryRegistration acquire() {
            this.mReferenceCount++;
            return this;
        }

        public synchronized void release() {
            this.mReferenceCount--;
            if (this.mReferenceCount == 0) {
                VMRuntime.getRuntime().registerNativeFree(this.mSize);
            }
        }
    }
}
