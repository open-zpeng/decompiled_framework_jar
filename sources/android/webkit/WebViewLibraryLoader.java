package android.webkit;

import android.app.ActivityManagerInternal;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebViewFactory;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
@VisibleForTesting
/* loaded from: classes2.dex */
public class WebViewLibraryLoader {
    private static final long CHROMIUM_WEBVIEW_DEFAULT_VMSIZE_BYTES = 104857600;
    private static final String CHROMIUM_WEBVIEW_NATIVE_RELRO_32 = "/data/misc/shared_relro/libwebviewchromium32.relro";
    private static final String CHROMIUM_WEBVIEW_NATIVE_RELRO_64 = "/data/misc/shared_relro/libwebviewchromium64.relro";
    private static final boolean DEBUG = false;
    private static final String LOGTAG = WebViewLibraryLoader.class.getSimpleName();
    private static boolean sAddressSpaceReserved = false;

    static native boolean nativeCreateRelroFile(String str, String str2);

    static native int nativeLoadWithRelroFile(String str, String str2, ClassLoader classLoader);

    static native boolean nativeReserveAddressSpace(long j);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class RelroFileCreator {
        private synchronized RelroFileCreator() {
        }

        public static synchronized void main(String[] args) {
            boolean is64Bit = VMRuntime.getRuntime().is64Bit();
            try {
                if (args.length == 1 && args[0] != null) {
                    String str = WebViewLibraryLoader.LOGTAG;
                    Log.v(str, "RelroFileCreator (64bit = " + is64Bit + "), lib: " + args[0]);
                    if (!WebViewLibraryLoader.sAddressSpaceReserved) {
                        Log.e(WebViewLibraryLoader.LOGTAG, "can't create relro file; address space not reserved");
                        return;
                    }
                    boolean result = WebViewLibraryLoader.nativeCreateRelroFile(args[0], is64Bit ? WebViewLibraryLoader.CHROMIUM_WEBVIEW_NATIVE_RELRO_64 : WebViewLibraryLoader.CHROMIUM_WEBVIEW_NATIVE_RELRO_32);
                    try {
                        WebViewFactory.getUpdateServiceUnchecked().notifyRelroCreationCompleted();
                    } catch (RemoteException e) {
                        Log.e(WebViewLibraryLoader.LOGTAG, "error notifying update service", e);
                    }
                    if (!result) {
                        Log.e(WebViewLibraryLoader.LOGTAG, "failed to create relro file");
                    }
                    System.exit(0);
                    return;
                }
                String str2 = WebViewLibraryLoader.LOGTAG;
                Log.e(str2, "Invalid RelroFileCreator args: " + Arrays.toString(args));
                try {
                    WebViewFactory.getUpdateServiceUnchecked().notifyRelroCreationCompleted();
                } catch (RemoteException e2) {
                    Log.e(WebViewLibraryLoader.LOGTAG, "error notifying update service", e2);
                }
                if (0 == 0) {
                    Log.e(WebViewLibraryLoader.LOGTAG, "failed to create relro file");
                }
                System.exit(0);
            } finally {
                try {
                    WebViewFactory.getUpdateServiceUnchecked().notifyRelroCreationCompleted();
                } catch (RemoteException e3) {
                    Log.e(WebViewLibraryLoader.LOGTAG, "error notifying update service", e3);
                }
                if (0 == 0) {
                    Log.e(WebViewLibraryLoader.LOGTAG, "failed to create relro file");
                }
                System.exit(0);
            }
        }
    }

    static synchronized void createRelroFile(boolean is64Bit, WebViewNativeLibrary nativeLib) {
        final String abi = is64Bit ? Build.SUPPORTED_64_BIT_ABIS[0] : Build.SUPPORTED_32_BIT_ABIS[0];
        Runnable crashHandler = new Runnable() { // from class: android.webkit.WebViewLibraryLoader.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    String str = WebViewLibraryLoader.LOGTAG;
                    Log.e(str, "relro file creator for " + abi + " crashed. Proceeding without");
                    WebViewFactory.getUpdateService().notifyRelroCreationCompleted();
                } catch (RemoteException e) {
                    String str2 = WebViewLibraryLoader.LOGTAG;
                    Log.e(str2, "Cannot reach WebViewUpdateService. " + e.getMessage());
                }
            }
        };
        if (nativeLib != null) {
            try {
                if (nativeLib.path != null) {
                    String name = RelroFileCreator.class.getName();
                    String[] strArr = {nativeLib.path};
                    boolean success = ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).startIsolatedProcess(name, strArr, "WebViewLoader-" + abi, abi, 1037, crashHandler);
                    if (!success) {
                        throw new Exception("Failed to start the relro file creator process");
                    }
                    return;
                }
            } catch (Throwable t) {
                String str = LOGTAG;
                Log.e(str, "error starting relro file creator for abi " + abi, t);
                crashHandler.run();
                return;
            }
        }
        throw new IllegalArgumentException("Native library paths to the WebView RelRo process must not be null!");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized int prepareNativeLibraries(PackageInfo webviewPackageInfo) throws WebViewFactory.MissingWebViewPackageException {
        WebViewNativeLibrary nativeLib32bit = getWebViewNativeLibrary(webviewPackageInfo, false);
        WebViewNativeLibrary nativeLib64bit = getWebViewNativeLibrary(webviewPackageInfo, true);
        updateWebViewZygoteVmSize(nativeLib32bit, nativeLib64bit);
        return createRelros(nativeLib32bit, nativeLib64bit);
    }

    private static synchronized int createRelros(WebViewNativeLibrary nativeLib32bit, WebViewNativeLibrary nativeLib64bit) {
        int numRelros = 0;
        if (Build.SUPPORTED_32_BIT_ABIS.length > 0) {
            if (nativeLib32bit == null) {
                Log.e(LOGTAG, "No 32-bit WebView library path, skipping relro creation.");
            } else {
                createRelroFile(false, nativeLib32bit);
                numRelros = 0 + 1;
            }
        }
        if (Build.SUPPORTED_64_BIT_ABIS.length > 0) {
            if (nativeLib64bit == null) {
                Log.e(LOGTAG, "No 64-bit WebView library path, skipping relro creation.");
                return numRelros;
            }
            createRelroFile(true, nativeLib64bit);
            return numRelros + 1;
        }
        return numRelros;
    }

    private static synchronized void updateWebViewZygoteVmSize(WebViewNativeLibrary nativeLib32bit, WebViewNativeLibrary nativeLib64bit) throws WebViewFactory.MissingWebViewPackageException {
        long newVmSize = nativeLib32bit != null ? Math.max(0L, nativeLib32bit.size) : 0L;
        if (nativeLib64bit != null) {
            newVmSize = Math.max(newVmSize, nativeLib64bit.size);
        }
        long newVmSize2 = Math.max(2 * newVmSize, (long) CHROMIUM_WEBVIEW_DEFAULT_VMSIZE_BYTES);
        String str = LOGTAG;
        Log.d(str, "Setting new address space to " + newVmSize2);
        setWebViewZygoteVmSize(newVmSize2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void reserveAddressSpaceInZygote() {
        System.loadLibrary("webviewchromium_loader");
        long addressSpaceToReserve = SystemProperties.getLong(WebViewFactory.CHROMIUM_WEBVIEW_VMSIZE_SIZE_PROPERTY, CHROMIUM_WEBVIEW_DEFAULT_VMSIZE_BYTES);
        sAddressSpaceReserved = nativeReserveAddressSpace(addressSpaceToReserve);
        if (!sAddressSpaceReserved) {
            String str = LOGTAG;
            Log.e(str, "reserving " + addressSpaceToReserve + " bytes of address space failed");
        }
    }

    public static synchronized int loadNativeLibrary(ClassLoader clazzLoader, String libraryFileName) {
        if (!sAddressSpaceReserved) {
            Log.e(LOGTAG, "can't load with relro file; address space not reserved");
            return 2;
        }
        String relroPath = VMRuntime.getRuntime().is64Bit() ? CHROMIUM_WEBVIEW_NATIVE_RELRO_64 : CHROMIUM_WEBVIEW_NATIVE_RELRO_32;
        int result = nativeLoadWithRelroFile(libraryFileName, relroPath, clazzLoader);
        if (result != 0) {
            Log.w(LOGTAG, "failed to load with relro file, proceeding without");
        }
        return result;
    }

    @VisibleForTesting
    public static synchronized WebViewNativeLibrary getWebViewNativeLibrary(PackageInfo packageInfo, boolean is64bit) throws WebViewFactory.MissingWebViewPackageException {
        ApplicationInfo ai = packageInfo.applicationInfo;
        String nativeLibFileName = WebViewFactory.getWebViewLibrary(ai);
        String dir = getWebViewNativeLibraryDirectory(ai, is64bit);
        WebViewNativeLibrary lib = findNativeLibrary(ai, nativeLibFileName, is64bit ? Build.SUPPORTED_64_BIT_ABIS : Build.SUPPORTED_32_BIT_ABIS, dir);
        return lib;
    }

    @VisibleForTesting
    public static synchronized String getWebViewNativeLibraryDirectory(ApplicationInfo ai, boolean is64bit) {
        if (is64bit == VMRuntime.is64BitAbi(ai.primaryCpuAbi)) {
            return ai.nativeLibraryDir;
        }
        if (!TextUtils.isEmpty(ai.secondaryCpuAbi)) {
            return ai.secondaryNativeLibraryDir;
        }
        return "";
    }

    private static synchronized WebViewNativeLibrary findNativeLibrary(ApplicationInfo ai, String nativeLibFileName, String[] abiList, String libDirectory) throws WebViewFactory.MissingWebViewPackageException {
        if (TextUtils.isEmpty(libDirectory)) {
            return null;
        }
        String libPath = libDirectory + "/" + nativeLibFileName;
        File f = new File(libPath);
        if (f.exists()) {
            return new WebViewNativeLibrary(libPath, f.length());
        }
        return getLoadFromApkPath(ai.sourceDir, abiList, nativeLibFileName);
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    public static class WebViewNativeLibrary {
        public final String path;
        public final long size;

        synchronized WebViewNativeLibrary(String path, long size) {
            this.path = path;
            this.size = size;
        }
    }

    private static synchronized WebViewNativeLibrary getLoadFromApkPath(String apkPath, String[] abiList, String nativeLibFileName) throws WebViewFactory.MissingWebViewPackageException {
        try {
            ZipFile z = new ZipFile(apkPath);
            for (String abi : abiList) {
                String entry = "lib/" + abi + "/" + nativeLibFileName;
                ZipEntry e = z.getEntry(entry);
                if (e != null && e.getMethod() == 0) {
                    WebViewNativeLibrary webViewNativeLibrary = new WebViewNativeLibrary(apkPath + "!/" + entry, e.getSize());
                    z.close();
                    return webViewNativeLibrary;
                }
            }
            z.close();
            return null;
        } catch (IOException e2) {
            throw new WebViewFactory.MissingWebViewPackageException(e2);
        }
    }

    private static synchronized void setWebViewZygoteVmSize(long vmSize) {
        SystemProperties.set(WebViewFactory.CHROMIUM_WEBVIEW_VMSIZE_SIZE_PROPERTY, Long.toString(vmSize));
    }
}
