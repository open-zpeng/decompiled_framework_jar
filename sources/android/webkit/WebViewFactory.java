package android.webkit;

import android.annotation.SystemApi;
import android.app.ActivityManager;
import android.app.AppGlobals;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Trace;
import android.util.AndroidRuntimeException;
import android.util.ArraySet;
import android.util.Log;
import android.webkit.IWebViewUpdateService;
import java.io.File;
import java.lang.reflect.Method;
@SystemApi
/* loaded from: classes2.dex */
public final class WebViewFactory {
    private static final String CHROMIUM_WEBVIEW_FACTORY = "com.android.webview.chromium.WebViewChromiumFactoryProviderForP";
    private static final String CHROMIUM_WEBVIEW_FACTORY_METHOD = "create";
    public static final String CHROMIUM_WEBVIEW_VMSIZE_SIZE_PROPERTY = "persist.sys.webview.vmsize";
    private static final boolean DEBUG = false;
    public static final int LIBLOAD_ADDRESS_SPACE_NOT_RESERVED = 2;
    public static final int LIBLOAD_FAILED_JNI_CALL = 7;
    public static final int LIBLOAD_FAILED_LISTING_WEBVIEW_PACKAGES = 4;
    public static final int LIBLOAD_FAILED_TO_FIND_NAMESPACE = 10;
    public static final int LIBLOAD_FAILED_TO_LOAD_LIBRARY = 6;
    public static final int LIBLOAD_FAILED_TO_OPEN_RELRO_FILE = 5;
    public static final int LIBLOAD_FAILED_WAITING_FOR_RELRO = 3;
    public static final int LIBLOAD_FAILED_WAITING_FOR_WEBVIEW_REASON_UNKNOWN = 8;
    public static final int LIBLOAD_SUCCESS = 0;
    public static final int LIBLOAD_WRONG_PACKAGE_NAME = 1;
    private static final String LOGTAG = "WebViewFactory";
    private static String sDataDirectorySuffix;
    public protected static PackageInfo sPackageInfo;
    public protected static WebViewFactoryProvider sProviderInstance;
    private static boolean sWebViewDisabled;
    private static Boolean sWebViewSupported;
    private static final Object sProviderLock = new Object();
    private static String WEBVIEW_UPDATE_SERVICE_NAME = "webviewupdate";

    private static synchronized String getWebViewPreparationErrorReason(int error) {
        if (error != 8) {
            switch (error) {
                case 3:
                    return "Time out waiting for Relro files being created";
                case 4:
                    return "No WebView installed";
                default:
                    return "Unknown";
            }
        }
        return "Crashed for unknown reason";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class MissingWebViewPackageException extends Exception {
        public synchronized MissingWebViewPackageException(String message) {
            super(message);
        }

        public synchronized MissingWebViewPackageException(Exception e) {
            super(e);
        }
    }

    private static synchronized boolean isWebViewSupported() {
        if (sWebViewSupported == null) {
            sWebViewSupported = Boolean.valueOf(AppGlobals.getInitialApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_WEBVIEW));
        }
        return sWebViewSupported.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void disableWebView() {
        synchronized (sProviderLock) {
            if (sProviderInstance != null) {
                throw new IllegalStateException("Can't disable WebView: WebView already initialized");
            }
            sWebViewDisabled = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void setDataDirectorySuffix(String suffix) {
        synchronized (sProviderLock) {
            if (sProviderInstance != null) {
                throw new IllegalStateException("Can't set data directory suffix: WebView already initialized");
            }
            if (suffix.indexOf(File.separatorChar) >= 0) {
                throw new IllegalArgumentException("Suffix " + suffix + " contains a path separator");
            }
            sDataDirectorySuffix = suffix;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized String getDataDirectorySuffix() {
        String str;
        synchronized (sProviderLock) {
            str = sDataDirectorySuffix;
        }
        return str;
    }

    public static synchronized String getWebViewLibrary(ApplicationInfo ai) {
        if (ai.metaData != null) {
            return ai.metaData.getString("com.android.webview.WebViewLibrary");
        }
        return null;
    }

    public static PackageInfo getLoadedPackageInfo() {
        PackageInfo packageInfo;
        synchronized (sProviderLock) {
            packageInfo = sPackageInfo;
        }
        return packageInfo;
    }

    public static synchronized Class<WebViewFactoryProvider> getWebViewProviderClass(ClassLoader clazzLoader) throws ClassNotFoundException {
        return Class.forName(CHROMIUM_WEBVIEW_FACTORY, true, clazzLoader);
    }

    public static int loadWebViewNativeLibraryFromPackage(String packageName, ClassLoader clazzLoader) {
        if (isWebViewSupported()) {
            try {
                WebViewProviderResponse response = getUpdateService().waitForAndGetProvider();
                if (response.status != 0 && response.status != 3) {
                    return response.status;
                }
                if (response.packageInfo.packageName.equals(packageName)) {
                    PackageManager packageManager = AppGlobals.getInitialApplication().getPackageManager();
                    try {
                        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 268435584);
                        String libraryFileName = getWebViewLibrary(packageInfo.applicationInfo);
                        int loadNativeRet = WebViewLibraryLoader.loadNativeLibrary(clazzLoader, libraryFileName);
                        return loadNativeRet == 0 ? response.status : loadNativeRet;
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.e(LOGTAG, "Couldn't find package " + packageName);
                        return 1;
                    }
                }
                return 1;
            } catch (RemoteException e2) {
                Log.e(LOGTAG, "error waiting for relro creation", e2);
                return 8;
            }
        }
        return 1;
    }

    public private protected static WebViewFactoryProvider getProvider() {
        synchronized (sProviderLock) {
            if (sProviderInstance != null) {
                return sProviderInstance;
            }
            int uid = Process.myUid();
            if (uid == 0 || uid == 1000 || uid == 1001 || uid == 1027 || uid == 1002) {
                throw new UnsupportedOperationException("For security reasons, WebView is not allowed in privileged processes");
            }
            if (!isWebViewSupported()) {
                throw new UnsupportedOperationException();
            }
            if (sWebViewDisabled) {
                throw new IllegalStateException("WebView.disableWebView() was called: WebView is disabled");
            }
            Trace.traceBegin(16L, "WebViewFactory.getProvider()");
            Class<WebViewFactoryProvider> providerClass = getProviderClass();
            Method staticFactory = null;
            try {
                staticFactory = providerClass.getMethod(CHROMIUM_WEBVIEW_FACTORY_METHOD, WebViewDelegate.class);
            } catch (Exception e) {
            }
            Trace.traceBegin(16L, "WebViewFactoryProvider invocation");
            try {
                try {
                    sProviderInstance = (WebViewFactoryProvider) staticFactory.invoke(null, new WebViewDelegate());
                    WebViewFactoryProvider webViewFactoryProvider = sProviderInstance;
                    Trace.traceEnd(16L);
                    return webViewFactoryProvider;
                } catch (Exception e2) {
                    Log.e(LOGTAG, "error instantiating provider", e2);
                    throw new AndroidRuntimeException(e2);
                }
            } finally {
                Trace.traceEnd(16L);
            }
        }
    }

    private static synchronized boolean signaturesEquals(Signature[] s1, Signature[] s2) {
        if (s1 == null) {
            return s2 == null;
        } else if (s2 == null) {
            return false;
        } else {
            ArraySet<Signature> set1 = new ArraySet<>();
            for (Signature signature : s1) {
                set1.add(signature);
            }
            ArraySet<Signature> set2 = new ArraySet<>();
            for (Signature signature2 : s2) {
                set2.add(signature2);
            }
            return set1.equals(set2);
        }
    }

    private static synchronized void verifyPackageInfo(PackageInfo chosen, PackageInfo toUse) throws MissingWebViewPackageException {
        if (!chosen.packageName.equals(toUse.packageName)) {
            throw new MissingWebViewPackageException("Failed to verify WebView provider, packageName mismatch, expected: " + chosen.packageName + " actual: " + toUse.packageName);
        } else if (chosen.getLongVersionCode() > toUse.getLongVersionCode()) {
            throw new MissingWebViewPackageException("Failed to verify WebView provider, version code is lower than expected: " + chosen.getLongVersionCode() + " actual: " + toUse.getLongVersionCode());
        } else if (getWebViewLibrary(toUse.applicationInfo) == null) {
            throw new MissingWebViewPackageException("Tried to load an invalid WebView provider: " + toUse.packageName);
        } else if (!signaturesEquals(chosen.signatures, toUse.signatures)) {
            throw new MissingWebViewPackageException("Failed to verify WebView provider, signature mismatch");
        }
    }

    private static synchronized void fixupStubApplicationInfo(ApplicationInfo ai, PackageManager pm) throws MissingWebViewPackageException {
        String donorPackageName = null;
        if (ai.metaData != null) {
            donorPackageName = ai.metaData.getString("com.android.webview.WebViewDonorPackage");
        }
        if (donorPackageName != null) {
            try {
                PackageInfo donorPackage = pm.getPackageInfo(donorPackageName, 270541824);
                ApplicationInfo donorInfo = donorPackage.applicationInfo;
                ai.sourceDir = donorInfo.sourceDir;
                ai.splitSourceDirs = donorInfo.splitSourceDirs;
                ai.nativeLibraryDir = donorInfo.nativeLibraryDir;
                ai.secondaryNativeLibraryDir = donorInfo.secondaryNativeLibraryDir;
                ai.primaryCpuAbi = donorInfo.primaryCpuAbi;
                ai.secondaryCpuAbi = donorInfo.secondaryCpuAbi;
            } catch (PackageManager.NameNotFoundException e) {
                throw new MissingWebViewPackageException("Failed to find donor package: " + donorPackageName);
            }
        }
    }

    public protected static Context getWebViewContextAndSetProvider() throws MissingWebViewPackageException {
        Application initialApplication = AppGlobals.getInitialApplication();
        try {
            Trace.traceBegin(16L, "WebViewUpdateService.waitForAndGetProvider()");
            WebViewProviderResponse response = getUpdateService().waitForAndGetProvider();
            Trace.traceEnd(16L);
            if (response.status != 0 && response.status != 3) {
                throw new MissingWebViewPackageException("Failed to load WebView provider: " + getWebViewPreparationErrorReason(response.status));
            }
            Trace.traceBegin(16L, "ActivityManager.addPackageDependency()");
            ActivityManager.getService().addPackageDependency(response.packageInfo.packageName);
            Trace.traceEnd(16L);
            PackageManager pm = initialApplication.getPackageManager();
            Trace.traceBegin(16L, "PackageManager.getPackageInfo()");
            PackageInfo newPackageInfo = pm.getPackageInfo(response.packageInfo.packageName, 268444864);
            Trace.traceEnd(16L);
            verifyPackageInfo(response.packageInfo, newPackageInfo);
            ApplicationInfo ai = newPackageInfo.applicationInfo;
            fixupStubApplicationInfo(ai, pm);
            Trace.traceBegin(16L, "initialApplication.createApplicationContext");
            Context webViewContext = initialApplication.createApplicationContext(ai, 3);
            sPackageInfo = newPackageInfo;
            Trace.traceEnd(16L);
            return webViewContext;
        } catch (PackageManager.NameNotFoundException | RemoteException e) {
            throw new MissingWebViewPackageException("Failed to load WebView provider: " + e);
        }
    }

    public protected static Class<WebViewFactoryProvider> getProviderClass() {
        Application initialApplication = AppGlobals.getInitialApplication();
        try {
            Trace.traceBegin(16L, "WebViewFactory.getWebViewContextAndSetProvider()");
            Context webViewContext = getWebViewContextAndSetProvider();
            Trace.traceEnd(16L);
            Log.i(LOGTAG, "Loading " + sPackageInfo.packageName + " version " + sPackageInfo.versionName + " (code " + sPackageInfo.getLongVersionCode() + ")");
            Trace.traceBegin(16L, "WebViewFactory.getChromiumProviderClass()");
            try {
                initialApplication.getAssets().addAssetPathAsSharedLibrary(webViewContext.getApplicationInfo().sourceDir);
                ClassLoader clazzLoader = webViewContext.getClassLoader();
                Trace.traceBegin(16L, "WebViewFactory.loadNativeLibrary()");
                WebViewLibraryLoader.loadNativeLibrary(clazzLoader, getWebViewLibrary(sPackageInfo.applicationInfo));
                Trace.traceEnd(16L);
                Trace.traceBegin(16L, "Class.forName()");
                try {
                    Class<WebViewFactoryProvider> webViewProviderClass = getWebViewProviderClass(clazzLoader);
                    Trace.traceEnd(16L);
                    return webViewProviderClass;
                } finally {
                    Trace.traceEnd(16L);
                }
            } catch (ClassNotFoundException e) {
                Log.e(LOGTAG, "error loading provider", e);
                throw new AndroidRuntimeException(e);
            }
        } catch (MissingWebViewPackageException e2) {
            Log.e(LOGTAG, "Chromium WebView package does not exist", e2);
            throw new AndroidRuntimeException(e2);
        }
    }

    public static void prepareWebViewInZygote() {
        try {
            WebViewLibraryLoader.reserveAddressSpaceInZygote();
        } catch (Throwable t) {
            Log.e(LOGTAG, "error preparing native loader", t);
        }
    }

    public static synchronized int onWebViewProviderChanged(PackageInfo packageInfo) {
        int startedRelroProcesses = 0;
        ApplicationInfo originalAppInfo = new ApplicationInfo(packageInfo.applicationInfo);
        try {
            fixupStubApplicationInfo(packageInfo.applicationInfo, AppGlobals.getInitialApplication().getPackageManager());
            startedRelroProcesses = WebViewLibraryLoader.prepareNativeLibraries(packageInfo);
        } catch (Throwable t) {
            Log.e(LOGTAG, "error preparing webview native library", t);
        }
        WebViewZygote.onWebViewProviderChanged(packageInfo, originalAppInfo);
        return startedRelroProcesses;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IWebViewUpdateService getUpdateService() {
        if (isWebViewSupported()) {
            return getUpdateServiceUnchecked();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized IWebViewUpdateService getUpdateServiceUnchecked() {
        return IWebViewUpdateService.Stub.asInterface(ServiceManager.getService(WEBVIEW_UPDATE_SERVICE_NAME));
    }
}
