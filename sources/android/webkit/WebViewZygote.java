package android.webkit;

import android.app.LoadedApk;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ChildZygoteProcess;
import android.os.Process;
import android.os.ZygoteProcess;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class WebViewZygote {
    private static final String LOGTAG = "WebViewZygote";
    private static final Object sLock = new Object();
    @GuardedBy("sLock")
    private static boolean sMultiprocessEnabled = false;
    @GuardedBy("sLock")
    private static PackageInfo sPackage;
    @GuardedBy("sLock")
    private static ApplicationInfo sPackageOriginalAppInfo;
    @GuardedBy("sLock")
    private static ChildZygoteProcess sZygote;

    public static synchronized ZygoteProcess getProcess() {
        synchronized (sLock) {
            if (sZygote != null) {
                return sZygote;
            }
            connectToZygoteIfNeededLocked();
            return sZygote;
        }
    }

    public static synchronized String getPackageName() {
        String str;
        synchronized (sLock) {
            str = sPackage.packageName;
        }
        return str;
    }

    public static synchronized boolean isMultiprocessEnabled() {
        boolean z;
        synchronized (sLock) {
            z = sMultiprocessEnabled && sPackage != null;
        }
        return z;
    }

    public static synchronized void setMultiprocessEnabled(boolean enabled) {
        synchronized (sLock) {
            sMultiprocessEnabled = enabled;
            if (enabled) {
                AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() { // from class: android.webkit.-$$Lambda$xYTrYQCPf1HcdlWzDof3mq93ihs
                    @Override // java.lang.Runnable
                    public final void run() {
                        WebViewZygote.getProcess();
                    }
                });
            } else {
                stopZygoteLocked();
            }
        }
    }

    public static synchronized void onWebViewProviderChanged(PackageInfo packageInfo, ApplicationInfo originalAppInfo) {
        synchronized (sLock) {
            sPackage = packageInfo;
            sPackageOriginalAppInfo = originalAppInfo;
            if (sMultiprocessEnabled) {
                stopZygoteLocked();
            }
        }
    }

    @GuardedBy("sLock")
    private static synchronized void stopZygoteLocked() {
        if (sZygote != null) {
            sZygote.close();
            Process.killProcess(sZygote.getPid());
            sZygote = null;
        }
    }

    @GuardedBy("sLock")
    private static synchronized void connectToZygoteIfNeededLocked() {
        if (sZygote != null) {
            return;
        }
        if (sPackage == null) {
            Log.e(LOGTAG, "Cannot connect to zygote, no package specified");
            return;
        }
        try {
            sZygote = Process.zygoteProcess.startChildZygote("com.android.internal.os.WebViewZygoteInit", "webview_zygote", 1053, 1053, null, 0, "webview_zygote", sPackage.applicationInfo.primaryCpuAbi, null);
            List<String> zipPaths = new ArrayList<>(10);
            List<String> libPaths = new ArrayList<>(10);
            LoadedApk.makePaths(null, false, sPackage.applicationInfo, zipPaths, libPaths);
            String librarySearchPath = TextUtils.join(File.pathSeparator, libPaths);
            String zip = zipPaths.size() == 1 ? zipPaths.get(0) : TextUtils.join(File.pathSeparator, zipPaths);
            String libFileName = WebViewFactory.getWebViewLibrary(sPackage.applicationInfo);
            LoadedApk.makePaths(null, false, sPackageOriginalAppInfo, zipPaths, null);
            String cacheKey = zipPaths.size() == 1 ? zipPaths.get(0) : TextUtils.join(File.pathSeparator, zipPaths);
            ZygoteProcess.waitForConnectionToZygote(sZygote.getPrimarySocketAddress());
            Log.d(LOGTAG, "Preloading package " + zip + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + librarySearchPath);
            sZygote.preloadPackageForAbi(zip, librarySearchPath, libFileName, cacheKey, Build.SUPPORTED_ABIS[0]);
        } catch (Exception e) {
            Log.e(LOGTAG, "Error connecting to webview zygote", e);
            stopZygoteLocked();
        }
    }
}
