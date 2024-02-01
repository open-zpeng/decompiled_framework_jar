package com.android.internal.content;

import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.os.Build;
import android.os.SELinux;
import android.os.SystemProperties;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Slog;
import dalvik.system.CloseGuard;
import dalvik.system.VMRuntime;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;
/* loaded from: classes3.dex */
public class NativeLibraryHelper {
    private static final int BITCODE_PRESENT = 1;
    public static final String CLEAR_ABI_OVERRIDE = "-";
    private static final boolean DEBUG_NATIVE = false;
    private static final boolean HAS_NATIVE_BRIDGE = !"0".equals(SystemProperties.get("ro.dalvik.vm.native.bridge", "0"));
    public static final String LIB64_DIR_NAME = "lib64";
    public static final String LIB_DIR_NAME = "lib";
    private static final String TAG = "NativeHelper";

    private static native int hasRenderscriptBitcode(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeClose(long j);

    private static native int nativeCopyNativeBinaries(long j, String str, String str2, boolean z, boolean z2, boolean z3);

    private static native int nativeFindSupportedAbi(long j, String[] strArr, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenApk(String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenApkFd(FileDescriptor fileDescriptor, String str);

    private static native long nativeSumNativeBinaries(long j, String str, boolean z);

    /* loaded from: classes3.dex */
    public static class Handle implements Closeable {
        final long[] apkHandles;
        final boolean debuggable;
        final boolean extractNativeLibs;
        private volatile boolean mClosed;
        private final CloseGuard mGuard = CloseGuard.get();
        final boolean multiArch;

        public static Handle create(File packageFile) throws IOException {
            try {
                PackageParser.PackageLite lite = PackageParser.parsePackageLite(packageFile, 0);
                return create(lite);
            } catch (PackageParser.PackageParserException e) {
                throw new IOException("Failed to parse package: " + packageFile, e);
            }
        }

        public static Handle create(PackageParser.Package pkg) throws IOException {
            return create(pkg.getAllCodePaths(), (pkg.applicationInfo.flags & Integer.MIN_VALUE) != 0, (pkg.applicationInfo.flags & 268435456) != 0, (pkg.applicationInfo.flags & 2) != 0);
        }

        public static Handle create(PackageParser.PackageLite lite) throws IOException {
            return create(lite.getAllCodePaths(), lite.multiArch, lite.extractNativeLibs, lite.debuggable);
        }

        private static Handle create(List<String> codePaths, boolean multiArch, boolean extractNativeLibs, boolean debuggable) throws IOException {
            int size = codePaths.size();
            long[] apkHandles = new long[size];
            for (int i = 0; i < size; i++) {
                String path = codePaths.get(i);
                apkHandles[i] = NativeLibraryHelper.nativeOpenApk(path);
                if (apkHandles[i] == 0) {
                    for (int j = 0; j < i; j++) {
                        NativeLibraryHelper.nativeClose(apkHandles[j]);
                    }
                    throw new IOException("Unable to open APK: " + path);
                }
            }
            return new Handle(apkHandles, multiArch, extractNativeLibs, debuggable);
        }

        public static Handle createFd(PackageParser.PackageLite lite, FileDescriptor fd) throws IOException {
            String path = lite.baseCodePath;
            long[] apkHandles = {NativeLibraryHelper.nativeOpenApkFd(fd, path)};
            if (apkHandles[0] == 0) {
                throw new IOException("Unable to open APK " + path + " from fd " + fd);
            }
            return new Handle(apkHandles, lite.multiArch, lite.extractNativeLibs, lite.debuggable);
        }

        Handle(long[] apkHandles, boolean multiArch, boolean extractNativeLibs, boolean debuggable) {
            this.apkHandles = apkHandles;
            this.multiArch = multiArch;
            this.extractNativeLibs = extractNativeLibs;
            this.debuggable = debuggable;
            this.mGuard.open("close");
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            long[] jArr;
            for (long apkHandle : this.apkHandles) {
                NativeLibraryHelper.nativeClose(apkHandle);
            }
            this.mGuard.close();
            this.mClosed = true;
        }

        protected void finalize() throws Throwable {
            if (this.mGuard != null) {
                this.mGuard.warnIfOpen();
            }
            try {
                if (!this.mClosed) {
                    close();
                }
            } finally {
                super.finalize();
            }
        }
    }

    private static long sumNativeBinaries(Handle handle, String abi) {
        long[] jArr;
        long sum = 0;
        for (long apkHandle : handle.apkHandles) {
            sum += nativeSumNativeBinaries(apkHandle, abi, handle.debuggable);
        }
        return sum;
    }

    public static int copyNativeBinaries(Handle handle, File sharedLibraryDir, String abi) {
        long[] jArr;
        for (long apkHandle : handle.apkHandles) {
            int res = nativeCopyNativeBinaries(apkHandle, sharedLibraryDir.getPath(), abi, handle.extractNativeLibs, HAS_NATIVE_BRIDGE, handle.debuggable);
            if (res != 1) {
                return res;
            }
        }
        return 1;
    }

    public static int findSupportedAbi(Handle handle, String[] supportedAbis) {
        long[] jArr;
        int finalRes = -114;
        for (long apkHandle : handle.apkHandles) {
            int res = nativeFindSupportedAbi(apkHandle, supportedAbis, handle.debuggable);
            if (res != -114) {
                if (res == -113) {
                    if (finalRes < 0) {
                        finalRes = PackageManager.INSTALL_FAILED_NO_MATCHING_ABIS;
                    }
                } else if (res >= 0) {
                    if (finalRes < 0 || res < finalRes) {
                        finalRes = res;
                    }
                } else {
                    return res;
                }
            }
        }
        return finalRes;
    }

    public static void removeNativeBinariesLI(String nativeLibraryPath) {
        if (nativeLibraryPath == null) {
            return;
        }
        removeNativeBinariesFromDirLI(new File(nativeLibraryPath), false);
    }

    public static void removeNativeBinariesFromDirLI(File nativeLibraryRoot, boolean deleteRootDir) {
        if (nativeLibraryRoot.exists()) {
            File[] files = nativeLibraryRoot.listFiles();
            if (files != null) {
                for (int nn = 0; nn < files.length; nn++) {
                    if (files[nn].isDirectory()) {
                        removeNativeBinariesFromDirLI(files[nn], true);
                    } else if (!files[nn].delete()) {
                        Slog.w(TAG, "Could not delete native binary: " + files[nn].getPath());
                    }
                }
            }
            if (deleteRootDir && !nativeLibraryRoot.delete()) {
                Slog.w(TAG, "Could not delete native binary directory: " + nativeLibraryRoot.getPath());
            }
        }
    }

    public static void createNativeLibrarySubdir(File path) throws IOException {
        if (!path.isDirectory()) {
            path.delete();
            if (!path.mkdir()) {
                throw new IOException("Cannot create " + path.getPath());
            }
            try {
                Os.chmod(path.getPath(), OsConstants.S_IRWXU | OsConstants.S_IRGRP | OsConstants.S_IXGRP | OsConstants.S_IROTH | OsConstants.S_IXOTH);
            } catch (ErrnoException e) {
                throw new IOException("Cannot chmod native library directory " + path.getPath(), e);
            }
        } else if (!SELinux.restorecon(path)) {
            throw new IOException("Cannot set SELinux context for " + path.getPath());
        }
    }

    private static long sumNativeBinariesForSupportedAbi(Handle handle, String[] abiList) {
        int abi = findSupportedAbi(handle, abiList);
        if (abi >= 0) {
            return sumNativeBinaries(handle, abiList[abi]);
        }
        return 0L;
    }

    public static int copyNativeBinariesForSupportedAbi(Handle handle, File libraryRoot, String[] abiList, boolean useIsaSubdir) throws IOException {
        File isaSubdir;
        createNativeLibrarySubdir(libraryRoot);
        int abi = findSupportedAbi(handle, abiList);
        if (abi >= 0) {
            String instructionSet = VMRuntime.getInstructionSet(abiList[abi]);
            if (useIsaSubdir) {
                isaSubdir = new File(libraryRoot, instructionSet);
                createNativeLibrarySubdir(isaSubdir);
            } else {
                isaSubdir = libraryRoot;
            }
            int copyRet = copyNativeBinaries(handle, isaSubdir, abiList[abi]);
            if (copyRet != 1) {
                return copyRet;
            }
        }
        return abi;
    }

    public static int copyNativeBinariesWithOverride(Handle handle, File libraryRoot, String abiOverride) {
        int copyRet;
        int copyRet2;
        try {
            if (handle.multiArch) {
                if (abiOverride != null && !CLEAR_ABI_OVERRIDE.equals(abiOverride)) {
                    Slog.w(TAG, "Ignoring abiOverride for multi arch application.");
                }
                if (Build.SUPPORTED_32_BIT_ABIS.length > 0 && (copyRet2 = copyNativeBinariesForSupportedAbi(handle, libraryRoot, Build.SUPPORTED_32_BIT_ABIS, true)) < 0 && copyRet2 != -114 && copyRet2 != -113) {
                    Slog.w(TAG, "Failure copying 32 bit native libraries; copyRet=" + copyRet2);
                    return copyRet2;
                } else if (Build.SUPPORTED_64_BIT_ABIS.length > 0 && (copyRet = copyNativeBinariesForSupportedAbi(handle, libraryRoot, Build.SUPPORTED_64_BIT_ABIS, true)) < 0 && copyRet != -114 && copyRet != -113) {
                    Slog.w(TAG, "Failure copying 64 bit native libraries; copyRet=" + copyRet);
                    return copyRet;
                }
            } else {
                String cpuAbiOverride = null;
                if (CLEAR_ABI_OVERRIDE.equals(abiOverride)) {
                    cpuAbiOverride = null;
                } else if (abiOverride != null) {
                    cpuAbiOverride = abiOverride;
                }
                String[] abiList = cpuAbiOverride != null ? new String[]{cpuAbiOverride} : Build.SUPPORTED_ABIS;
                if (Build.SUPPORTED_64_BIT_ABIS.length > 0 && cpuAbiOverride == null && hasRenderscriptBitcode(handle)) {
                    abiList = Build.SUPPORTED_32_BIT_ABIS;
                }
                int copyRet3 = copyNativeBinariesForSupportedAbi(handle, libraryRoot, abiList, true);
                if (copyRet3 < 0 && copyRet3 != -114) {
                    Slog.w(TAG, "Failure copying native libraries [errorCode=" + copyRet3 + "]");
                    return copyRet3;
                }
            }
            return 1;
        } catch (IOException e) {
            Slog.e(TAG, "Copying native libraries failed", e);
            return -110;
        }
    }

    public static long sumNativeBinariesWithOverride(Handle handle, String abiOverride) throws IOException {
        if (handle.multiArch) {
            if (abiOverride != null && !CLEAR_ABI_OVERRIDE.equals(abiOverride)) {
                Slog.w(TAG, "Ignoring abiOverride for multi arch application.");
            }
            long sum = Build.SUPPORTED_32_BIT_ABIS.length > 0 ? 0 + sumNativeBinariesForSupportedAbi(handle, Build.SUPPORTED_32_BIT_ABIS) : 0L;
            if (Build.SUPPORTED_64_BIT_ABIS.length > 0) {
                return sum + sumNativeBinariesForSupportedAbi(handle, Build.SUPPORTED_64_BIT_ABIS);
            }
            return sum;
        }
        String cpuAbiOverride = null;
        if (CLEAR_ABI_OVERRIDE.equals(abiOverride)) {
            cpuAbiOverride = null;
        } else if (abiOverride != null) {
            cpuAbiOverride = abiOverride;
        }
        String[] abiList = cpuAbiOverride != null ? new String[]{cpuAbiOverride} : Build.SUPPORTED_ABIS;
        if (Build.SUPPORTED_64_BIT_ABIS.length > 0 && cpuAbiOverride == null && hasRenderscriptBitcode(handle)) {
            abiList = Build.SUPPORTED_32_BIT_ABIS;
        }
        return 0 + sumNativeBinariesForSupportedAbi(handle, abiList);
    }

    public static boolean hasRenderscriptBitcode(Handle handle) throws IOException {
        long[] jArr;
        for (long apkHandle : handle.apkHandles) {
            int res = hasRenderscriptBitcode(apkHandle);
            if (res < 0) {
                throw new IOException("Error scanning APK, code: " + res);
            } else if (res == 1) {
                return true;
            }
        }
        return false;
    }
}
