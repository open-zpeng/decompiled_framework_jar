package android.util.jar;

import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.jar.StrictJarVerifier;
import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import libcore.io.IoBridge;
import libcore.io.IoUtils;
import libcore.io.Streams;
/* loaded from: classes2.dex */
public final class StrictJarFile {
    public protected boolean closed;
    public protected final FileDescriptor fd;
    public protected final CloseGuard guard;
    public protected final boolean isSigned;
    public protected final StrictJarManifest manifest;
    public protected final long nativeHandle;
    public protected final StrictJarVerifier verifier;

    public protected static native void nativeClose(long j);

    public protected static native ZipEntry nativeFindEntry(long j, String str);

    /* JADX INFO: Access modifiers changed from: public */
    public static native ZipEntry nativeNextEntry(long j);

    public protected static native long nativeOpenJarFile(String str, int i) throws IOException;

    /* JADX INFO: Access modifiers changed from: public */
    public static native long nativeStartIteration(long j, String str);

    private protected synchronized StrictJarFile(String fileName) throws IOException, SecurityException {
        this(fileName, true, true);
    }

    private protected synchronized StrictJarFile(FileDescriptor fd) throws IOException, SecurityException {
        this(fd, true, true);
    }

    private protected synchronized StrictJarFile(FileDescriptor fd, boolean verify, boolean signatureSchemeRollbackProtectionsEnforced) throws IOException, SecurityException {
        this("[fd:" + fd.getInt$() + "]", fd, verify, signatureSchemeRollbackProtectionsEnforced);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized StrictJarFile(String fileName, boolean verify, boolean signatureSchemeRollbackProtectionsEnforced) throws IOException, SecurityException {
        this(fileName, IoBridge.open(fileName, OsConstants.O_RDONLY), verify, signatureSchemeRollbackProtectionsEnforced);
    }

    public protected synchronized StrictJarFile(String name, FileDescriptor fd, boolean verify, boolean signatureSchemeRollbackProtectionsEnforced) throws IOException, SecurityException {
        this.guard = CloseGuard.get();
        this.nativeHandle = nativeOpenJarFile(name, fd.getInt$());
        this.fd = fd;
        boolean z = false;
        try {
            if (verify) {
                HashMap<String, byte[]> metaEntries = getMetaEntries();
                this.manifest = new StrictJarManifest(metaEntries.get("META-INF/MANIFEST.MF"), true);
                this.verifier = new StrictJarVerifier(name, this.manifest, metaEntries, signatureSchemeRollbackProtectionsEnforced);
                Set<String> files = this.manifest.getEntries().keySet();
                for (String file : files) {
                    if (findEntry(file) == null) {
                        throw new SecurityException("File " + file + " in manifest does not exist");
                    }
                }
                if (this.verifier.readCertificates() && this.verifier.isSignedJar()) {
                    z = true;
                }
                this.isSigned = z;
            } else {
                this.isSigned = false;
                this.manifest = null;
                this.verifier = null;
            }
            this.guard.open("close");
        } catch (IOException | SecurityException e) {
            nativeClose(this.nativeHandle);
            IoUtils.closeQuietly(fd);
            this.closed = true;
            throw e;
        }
    }

    private protected synchronized StrictJarManifest getManifest() {
        return this.manifest;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Iterator<ZipEntry> iterator() throws IOException {
        return new EntryIterator(this.nativeHandle, "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized ZipEntry findEntry(String name) {
        return nativeFindEntry(this.nativeHandle, name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Certificate[][] getCertificateChains(ZipEntry ze) {
        if (this.isSigned) {
            return this.verifier.getCertificateChains(ze.getName());
        }
        return null;
    }

    @Deprecated
    private protected synchronized Certificate[] getCertificates(ZipEntry ze) {
        if (this.isSigned) {
            Certificate[][] certChains = this.verifier.getCertificateChains(ze.getName());
            int count = 0;
            for (Certificate[] chain : certChains) {
                count += chain.length;
            }
            Certificate[] certs = new Certificate[count];
            int i = 0;
            for (Certificate[] chain2 : certChains) {
                System.arraycopy(chain2, 0, certs, i, chain2.length);
                i += chain2.length;
            }
            return certs;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized InputStream getInputStream(ZipEntry ze) {
        InputStream is = getZipInputStream(ze);
        if (this.isSigned) {
            StrictJarVerifier.VerifierEntry entry = this.verifier.initEntry(ze.getName());
            if (entry == null) {
                return is;
            }
            return new JarFileInputStream(is, ze.getSize(), entry);
        }
        return is;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void close() throws IOException {
        if (!this.closed) {
            if (this.guard != null) {
                this.guard.close();
            }
            nativeClose(this.nativeHandle);
            IoUtils.closeQuietly(this.fd);
            this.closed = true;
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (this.guard != null) {
                this.guard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    public protected synchronized InputStream getZipInputStream(ZipEntry ze) {
        if (ze.getMethod() == 0) {
            return new FDStream(this.fd, ze.getDataOffset(), ze.getDataOffset() + ze.getSize());
        }
        FDStream wrapped = new FDStream(this.fd, ze.getDataOffset(), ze.getDataOffset() + ze.getCompressedSize());
        int bufSize = Math.max(1024, (int) Math.min(ze.getSize(), 65535L));
        return new ZipInflaterInputStream(wrapped, new Inflater(true), bufSize, ze);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class EntryIterator implements Iterator<ZipEntry> {
        public protected final long iterationHandle;
        public protected ZipEntry nextEntry;

        public private protected synchronized EntryIterator(long nativeHandle, String prefix) throws IOException {
            this.iterationHandle = StrictJarFile.nativeStartIteration(nativeHandle, prefix);
        }

        @Override // java.util.Iterator
        public ZipEntry next() {
            if (this.nextEntry == null) {
                return StrictJarFile.nativeNextEntry(this.iterationHandle);
            }
            ZipEntry ze = this.nextEntry;
            this.nextEntry = null;
            return ze;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextEntry != null) {
                return true;
            }
            ZipEntry ze = StrictJarFile.nativeNextEntry(this.iterationHandle);
            if (ze == null) {
                return false;
            }
            this.nextEntry = ze;
            return true;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public protected synchronized HashMap<String, byte[]> getMetaEntries() throws IOException {
        HashMap<String, byte[]> metaEntries = new HashMap<>();
        Iterator<ZipEntry> entryIterator = new EntryIterator(this.nativeHandle, "META-INF/");
        while (entryIterator.hasNext()) {
            ZipEntry entry = entryIterator.next();
            metaEntries.put(entry.getName(), Streams.readFully(getInputStream(entry)));
        }
        return metaEntries;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class JarFileInputStream extends FilterInputStream {
        public protected long count;
        public protected boolean done;
        public protected final StrictJarVerifier.VerifierEntry entry;

        public private protected synchronized JarFileInputStream(InputStream is, long size, StrictJarVerifier.VerifierEntry e) {
            super(is);
            this.done = false;
            this.entry = e;
            this.count = size;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            if (this.done) {
                return -1;
            }
            if (this.count > 0) {
                int r = super.read();
                if (r != -1) {
                    this.entry.write(r);
                    this.count--;
                } else {
                    this.count = 0L;
                }
                if (this.count == 0) {
                    this.done = true;
                    this.entry.verify();
                }
                return r;
            }
            this.done = true;
            this.entry.verify();
            return -1;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            if (this.done) {
                return -1;
            }
            if (this.count > 0) {
                int r = super.read(buffer, byteOffset, byteCount);
                if (r != -1) {
                    int size = r;
                    if (this.count < size) {
                        size = (int) this.count;
                    }
                    this.entry.write(buffer, byteOffset, size);
                    this.count -= size;
                } else {
                    this.count = 0L;
                }
                if (this.count == 0) {
                    this.done = true;
                    this.entry.verify();
                }
                return r;
            }
            this.done = true;
            this.entry.verify();
            return -1;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int available() throws IOException {
            if (this.done) {
                return 0;
            }
            return super.available();
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public long skip(long byteCount) throws IOException {
            return Streams.skipByReading(this, byteCount);
        }
    }

    /* loaded from: classes2.dex */
    public static class ZipInflaterInputStream extends InflaterInputStream {
        public protected long bytesRead;
        public protected final ZipEntry entry;

        private protected synchronized ZipInflaterInputStream(InputStream is, Inflater inf, int bsize, ZipEntry entry) {
            super(is, inf, bsize);
            this.bytesRead = 0L;
            this.entry = entry;
        }

        @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            try {
                int i = super.read(buffer, byteOffset, byteCount);
                if (i == -1) {
                    if (this.entry.getSize() != this.bytesRead) {
                        throw new IOException("Size mismatch on inflated file: " + this.bytesRead + " vs " + this.entry.getSize());
                    }
                } else {
                    this.bytesRead += i;
                }
                return i;
            } catch (IOException e) {
                throw new IOException("Error reading data for " + this.entry.getName() + " near offset " + this.bytesRead, e);
            }
        }

        @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
        public int available() throws IOException {
            if (this.closed || super.available() == 0) {
                return 0;
            }
            return (int) (this.entry.getSize() - this.bytesRead);
        }
    }

    /* loaded from: classes2.dex */
    public static class FDStream extends InputStream {
        public protected long endOffset;
        public protected final FileDescriptor fd;
        public protected long offset;

        private protected synchronized FDStream(FileDescriptor fd, long initialOffset, long endOffset) {
            this.fd = fd;
            this.offset = initialOffset;
            this.endOffset = endOffset;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.offset < this.endOffset ? 1 : 0;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            return Streams.readSingleByte(this);
        }

        @Override // java.io.InputStream
        public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            synchronized (this.fd) {
                long length = this.endOffset - this.offset;
                if (byteCount > length) {
                    byteCount = (int) length;
                }
                try {
                    Os.lseek(this.fd, this.offset, OsConstants.SEEK_SET);
                    int count = IoBridge.read(this.fd, buffer, byteOffset, byteCount);
                    if (count > 0) {
                        this.offset += count;
                        return count;
                    }
                    return -1;
                } catch (ErrnoException e) {
                    throw new IOException(e);
                }
            }
        }

        @Override // java.io.InputStream
        public long skip(long byteCount) throws IOException {
            if (byteCount > this.endOffset - this.offset) {
                byteCount = this.endOffset - this.offset;
            }
            this.offset += byteCount;
            return byteCount;
        }
    }
}
