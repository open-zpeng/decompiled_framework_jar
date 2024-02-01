package android.content.res;

import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.io.FileDescriptor;
import java.io.IOException;
/* loaded from: classes.dex */
public final class ApkAssets {
    @GuardedBy("this")
    private final long mNativePtr;
    @GuardedBy("this")
    private boolean mOpen = true;
    @GuardedBy("this")
    private StringBlock mStringBlock;

    private static native void nativeDestroy(long j);

    private static native String nativeGetAssetPath(long j);

    private static native long nativeGetStringBlock(long j);

    private static native boolean nativeIsUpToDate(long j);

    private static native long nativeLoad(String str, boolean z, boolean z2, boolean z3) throws IOException;

    private static native long nativeLoadFromFd(FileDescriptor fileDescriptor, String str, boolean z, boolean z2) throws IOException;

    private static native long nativeOpenXml(long j, String str) throws IOException;

    public static synchronized ApkAssets loadFromPath(String path) throws IOException {
        return new ApkAssets(path, false, false, false);
    }

    public static synchronized ApkAssets loadFromPath(String path, boolean system) throws IOException {
        return new ApkAssets(path, system, false, false);
    }

    public static synchronized ApkAssets loadFromPath(String path, boolean system, boolean forceSharedLibrary) throws IOException {
        return new ApkAssets(path, system, forceSharedLibrary, false);
    }

    public static synchronized ApkAssets loadFromFd(FileDescriptor fd, String friendlyName, boolean system, boolean forceSharedLibrary) throws IOException {
        return new ApkAssets(fd, friendlyName, system, forceSharedLibrary);
    }

    public static synchronized ApkAssets loadOverlayFromPath(String idmapPath, boolean system) throws IOException {
        return new ApkAssets(idmapPath, system, false, true);
    }

    private synchronized ApkAssets(String path, boolean system, boolean forceSharedLib, boolean overlay) throws IOException {
        Preconditions.checkNotNull(path, "path");
        this.mNativePtr = nativeLoad(path, system, forceSharedLib, overlay);
        this.mStringBlock = new StringBlock(nativeGetStringBlock(this.mNativePtr), true);
    }

    private synchronized ApkAssets(FileDescriptor fd, String friendlyName, boolean system, boolean forceSharedLib) throws IOException {
        Preconditions.checkNotNull(fd, "fd");
        Preconditions.checkNotNull(friendlyName, "friendlyName");
        this.mNativePtr = nativeLoadFromFd(fd, friendlyName, system, forceSharedLib);
        this.mStringBlock = new StringBlock(nativeGetStringBlock(this.mNativePtr), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getAssetPath() {
        String nativeGetAssetPath;
        synchronized (this) {
            nativeGetAssetPath = nativeGetAssetPath(this.mNativePtr);
        }
        return nativeGetAssetPath;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized CharSequence getStringFromPool(int idx) {
        return this.mStringBlock.get(idx);
    }

    public synchronized XmlResourceParser openXml(String fileName) throws IOException {
        XmlResourceParser parser;
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            long nativeXmlPtr = nativeOpenXml(this.mNativePtr, fileName);
            XmlBlock block = new XmlBlock(null, nativeXmlPtr);
            parser = block.newParser();
            if (parser == null) {
                throw new AssertionError("block.newParser() returned a null parser");
            }
            block.close();
        }
        return parser;
    }

    public synchronized boolean isUpToDate() {
        return nativeIsUpToDate(this.mNativePtr);
    }

    public String toString() {
        return "ApkAssets{path=" + getAssetPath() + "}";
    }

    protected void finalize() throws Throwable {
        close();
    }

    public void close() throws Throwable {
        synchronized (this) {
            if (this.mOpen) {
                this.mOpen = false;
                this.mStringBlock.close();
                nativeDestroy(this.mNativePtr);
            }
        }
    }
}
