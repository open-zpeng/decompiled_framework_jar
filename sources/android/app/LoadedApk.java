package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.IServiceConnection;
import android.app.LoadedApk;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.dex.ArtManager;
import android.content.pm.split.SplitDependencyLoader;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.view.DisplayAdjustments;
import com.android.internal.util.ArrayUtils;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public final class LoadedApk {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final boolean DEBUG = false;
    private static final String PROPERTY_NAME_APPEND_NATIVE = "pi.append_native_lib_paths";
    static final String TAG = "LoadedApk";
    @UnsupportedAppUsage
    private final ActivityThread mActivityThread;
    private AppComponentFactory mAppComponentFactory;
    @UnsupportedAppUsage
    private String mAppDir;
    @UnsupportedAppUsage
    private Application mApplication;
    @UnsupportedAppUsage
    private ApplicationInfo mApplicationInfo;
    @UnsupportedAppUsage
    private final ClassLoader mBaseClassLoader;
    @UnsupportedAppUsage
    private ClassLoader mClassLoader;
    private File mCredentialProtectedDataDirFile;
    @UnsupportedAppUsage
    private String mDataDir;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private File mDataDirFile;
    private ClassLoader mDefaultClassLoader;
    private File mDeviceProtectedDataDirFile;
    @UnsupportedAppUsage
    private final DisplayAdjustments mDisplayAdjustments;
    private final boolean mIncludeCode;
    @UnsupportedAppUsage
    private String mLibDir;
    private String[] mOverlayDirs;
    @UnsupportedAppUsage
    final String mPackageName;
    @UnsupportedAppUsage
    private final ArrayMap<Context, ArrayMap<BroadcastReceiver, ReceiverDispatcher>> mReceivers;
    private final boolean mRegisterPackage;
    @UnsupportedAppUsage
    private String mResDir;
    @UnsupportedAppUsage
    Resources mResources;
    private final boolean mSecurityViolation;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final ArrayMap<Context, ArrayMap<ServiceConnection, ServiceDispatcher>> mServices;
    private String[] mSplitAppDirs;
    private String[] mSplitClassLoaderNames;
    private SplitDependencyLoaderImpl mSplitLoader;
    private String[] mSplitNames;
    @UnsupportedAppUsage
    private String[] mSplitResDirs;
    private final ArrayMap<Context, ArrayMap<ServiceConnection, ServiceDispatcher>> mUnboundServices;
    private final ArrayMap<Context, ArrayMap<BroadcastReceiver, ReceiverDispatcher>> mUnregisteredReceivers;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Application getApplication() {
        return this.mApplication;
    }

    public LoadedApk(ActivityThread activityThread, ApplicationInfo aInfo, CompatibilityInfo compatInfo, ClassLoader baseLoader, boolean securityViolation, boolean includeCode, boolean registerPackage) {
        this.mDisplayAdjustments = new DisplayAdjustments();
        this.mReceivers = new ArrayMap<>();
        this.mUnregisteredReceivers = new ArrayMap<>();
        this.mServices = new ArrayMap<>();
        this.mUnboundServices = new ArrayMap<>();
        this.mActivityThread = activityThread;
        setApplicationInfo(aInfo);
        this.mPackageName = aInfo.packageName;
        this.mBaseClassLoader = baseLoader;
        this.mSecurityViolation = securityViolation;
        this.mIncludeCode = includeCode;
        this.mRegisterPackage = registerPackage;
        this.mDisplayAdjustments.setCompatibilityInfo(compatInfo);
        this.mAppComponentFactory = createAppFactory(this.mApplicationInfo, this.mBaseClassLoader);
    }

    private static ApplicationInfo adjustNativeLibraryPaths(ApplicationInfo info) {
        if (info.primaryCpuAbi != null && info.secondaryCpuAbi != null) {
            String runtimeIsa = VMRuntime.getRuntime().vmInstructionSet();
            String secondaryIsa = VMRuntime.getInstructionSet(info.secondaryCpuAbi);
            String secondaryDexCodeIsa = SystemProperties.get("ro.dalvik.vm.isa." + secondaryIsa);
            if (runtimeIsa.equals(secondaryDexCodeIsa.isEmpty() ? secondaryIsa : secondaryDexCodeIsa)) {
                ApplicationInfo modified = new ApplicationInfo(info);
                modified.nativeLibraryDir = modified.secondaryNativeLibraryDir;
                modified.primaryCpuAbi = modified.secondaryCpuAbi;
                return modified;
            }
        }
        return info;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LoadedApk(ActivityThread activityThread) {
        this.mDisplayAdjustments = new DisplayAdjustments();
        this.mReceivers = new ArrayMap<>();
        this.mUnregisteredReceivers = new ArrayMap<>();
        this.mServices = new ArrayMap<>();
        this.mUnboundServices = new ArrayMap<>();
        this.mActivityThread = activityThread;
        this.mApplicationInfo = new ApplicationInfo();
        this.mApplicationInfo.packageName = "android";
        this.mPackageName = "android";
        this.mAppDir = null;
        this.mResDir = null;
        this.mSplitAppDirs = null;
        this.mSplitResDirs = null;
        this.mSplitClassLoaderNames = null;
        this.mOverlayDirs = null;
        this.mDataDir = null;
        this.mDataDirFile = null;
        this.mDeviceProtectedDataDirFile = null;
        this.mCredentialProtectedDataDirFile = null;
        this.mLibDir = null;
        this.mBaseClassLoader = null;
        this.mSecurityViolation = false;
        this.mIncludeCode = true;
        this.mRegisterPackage = false;
        this.mResources = Resources.getSystem();
        this.mDefaultClassLoader = ClassLoader.getSystemClassLoader();
        this.mAppComponentFactory = createAppFactory(this.mApplicationInfo, this.mDefaultClassLoader);
        this.mClassLoader = this.mAppComponentFactory.instantiateClassLoader(this.mDefaultClassLoader, new ApplicationInfo(this.mApplicationInfo));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void installSystemApplicationInfo(ApplicationInfo info, ClassLoader classLoader) {
        this.mApplicationInfo = info;
        this.mDefaultClassLoader = classLoader;
        this.mAppComponentFactory = createAppFactory(info, this.mDefaultClassLoader);
        this.mClassLoader = this.mAppComponentFactory.instantiateClassLoader(this.mDefaultClassLoader, new ApplicationInfo(this.mApplicationInfo));
    }

    private AppComponentFactory createAppFactory(ApplicationInfo appInfo, ClassLoader cl) {
        if (appInfo.appComponentFactory != null && cl != null) {
            try {
                return (AppComponentFactory) cl.loadClass(appInfo.appComponentFactory).newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                Slog.e(TAG, "Unable to instantiate appComponentFactory", e);
            }
        }
        return AppComponentFactory.DEFAULT;
    }

    public AppComponentFactory getAppFactory() {
        return this.mAppComponentFactory;
    }

    @UnsupportedAppUsage
    public String getPackageName() {
        return this.mPackageName;
    }

    @UnsupportedAppUsage
    public ApplicationInfo getApplicationInfo() {
        return this.mApplicationInfo;
    }

    public int getTargetSdkVersion() {
        return this.mApplicationInfo.targetSdkVersion;
    }

    public boolean isSecurityViolation() {
        return this.mSecurityViolation;
    }

    @UnsupportedAppUsage
    public CompatibilityInfo getCompatibilityInfo() {
        return this.mDisplayAdjustments.getCompatibilityInfo();
    }

    public void setCompatibilityInfo(CompatibilityInfo compatInfo) {
        this.mDisplayAdjustments.setCompatibilityInfo(compatInfo);
    }

    private static String[] getLibrariesFor(String packageName) {
        try {
            ApplicationInfo ai = ActivityThread.getPackageManager().getApplicationInfo(packageName, 1024, UserHandle.myUserId());
            if (ai == null) {
                return null;
            }
            return ai.sharedLibraryFiles;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void updateApplicationInfo(ApplicationInfo aInfo, List<String> oldPaths) {
        setApplicationInfo(aInfo);
        List<String> newPaths = new ArrayList<>();
        makePaths(this.mActivityThread, aInfo, newPaths);
        List<String> addedPaths = new ArrayList<>(newPaths.size());
        if (oldPaths != null) {
            for (String path : newPaths) {
                String apkName = path.substring(path.lastIndexOf(File.separator));
                boolean match = false;
                Iterator<String> it = oldPaths.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    String oldPath = it.next();
                    String oldApkName = oldPath.substring(oldPath.lastIndexOf(File.separator));
                    if (apkName.equals(oldApkName)) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    addedPaths.add(path);
                }
            }
        } else {
            addedPaths.addAll(newPaths);
        }
        synchronized (this) {
            createOrUpdateClassLoaderLocked(addedPaths);
            if (this.mResources != null) {
                try {
                    String[] splitPaths = getSplitPaths(null);
                    this.mResources = ResourcesManager.getInstance().getResources(null, this.mResDir, splitPaths, this.mOverlayDirs, this.mApplicationInfo.sharedLibraryFiles, 0, null, getCompatibilityInfo(), getClassLoader());
                } catch (PackageManager.NameNotFoundException e) {
                    throw new AssertionError("null split not found");
                }
            }
        }
        this.mAppComponentFactory = createAppFactory(aInfo, this.mDefaultClassLoader);
    }

    private void setApplicationInfo(ApplicationInfo aInfo) {
        int myUid = Process.myUid();
        ApplicationInfo aInfo2 = adjustNativeLibraryPaths(aInfo);
        this.mApplicationInfo = aInfo2;
        this.mAppDir = aInfo2.sourceDir;
        this.mResDir = aInfo2.uid == myUid ? aInfo2.sourceDir : aInfo2.publicSourceDir;
        this.mOverlayDirs = aInfo2.resourceDirs;
        this.mDataDir = aInfo2.dataDir;
        this.mLibDir = aInfo2.nativeLibraryDir;
        this.mDataDirFile = FileUtils.newFileOrNull(aInfo2.dataDir);
        this.mDeviceProtectedDataDirFile = FileUtils.newFileOrNull(aInfo2.deviceProtectedDataDir);
        this.mCredentialProtectedDataDirFile = FileUtils.newFileOrNull(aInfo2.credentialProtectedDataDir);
        this.mSplitNames = aInfo2.splitNames;
        this.mSplitAppDirs = aInfo2.splitSourceDirs;
        this.mSplitResDirs = aInfo2.uid == myUid ? aInfo2.splitSourceDirs : aInfo2.splitPublicSourceDirs;
        this.mSplitClassLoaderNames = aInfo2.splitClassLoaderNames;
        if (aInfo2.requestsIsolatedSplitLoading() && !ArrayUtils.isEmpty(this.mSplitNames)) {
            this.mSplitLoader = new SplitDependencyLoaderImpl(aInfo2.splitDependencies);
        }
    }

    public static void makePaths(ActivityThread activityThread, ApplicationInfo aInfo, List<String> outZipPaths) {
        makePaths(activityThread, false, aInfo, outZipPaths, null);
    }

    private static void appendSharedLibrariesLibPathsIfNeeded(List<SharedLibraryInfo> sharedLibraries, ApplicationInfo aInfo, Set<String> outSeenPaths, List<String> outLibPaths) {
        if (sharedLibraries == null) {
            return;
        }
        for (SharedLibraryInfo lib : sharedLibraries) {
            List<String> paths = lib.getAllCodePaths();
            outSeenPaths.addAll(paths);
            for (String path : paths) {
                appendApkLibPathIfNeeded(path, aInfo, outLibPaths);
            }
            appendSharedLibrariesLibPathsIfNeeded(lib.getDependencies(), aInfo, outSeenPaths, outLibPaths);
        }
    }

    public static void makePaths(ActivityThread activityThread, boolean isBundledApp, ApplicationInfo aInfo, List<String> outZipPaths, List<String> outLibPaths) {
        String[] strArr;
        String appDir = aInfo.sourceDir;
        String libDir = aInfo.nativeLibraryDir;
        outZipPaths.clear();
        outZipPaths.add(appDir);
        if (aInfo.splitSourceDirs != null && !aInfo.requestsIsolatedSplitLoading()) {
            Collections.addAll(outZipPaths, aInfo.splitSourceDirs);
        }
        if (outLibPaths != null) {
            outLibPaths.clear();
        }
        String[] instrumentationLibs = null;
        if (activityThread != null) {
            String instrumentationPackageName = activityThread.mInstrumentationPackageName;
            String instrumentationAppDir = activityThread.mInstrumentationAppDir;
            String[] instrumentationSplitAppDirs = activityThread.mInstrumentationSplitAppDirs;
            String instrumentationLibDir = activityThread.mInstrumentationLibDir;
            String instrumentedAppDir = activityThread.mInstrumentedAppDir;
            String[] instrumentedSplitAppDirs = activityThread.mInstrumentedSplitAppDirs;
            String instrumentedLibDir = activityThread.mInstrumentedLibDir;
            if (appDir.equals(instrumentationAppDir) || appDir.equals(instrumentedAppDir)) {
                outZipPaths.clear();
                outZipPaths.add(instrumentationAppDir);
                if (!aInfo.requestsIsolatedSplitLoading()) {
                    if (instrumentationSplitAppDirs != null) {
                        Collections.addAll(outZipPaths, instrumentationSplitAppDirs);
                    }
                    if (!instrumentationAppDir.equals(instrumentedAppDir)) {
                        outZipPaths.add(instrumentedAppDir);
                        if (instrumentedSplitAppDirs != null) {
                            Collections.addAll(outZipPaths, instrumentedSplitAppDirs);
                        }
                    }
                }
                if (outLibPaths != null) {
                    outLibPaths.add(instrumentationLibDir);
                    if (!instrumentationLibDir.equals(instrumentedLibDir)) {
                        outLibPaths.add(instrumentedLibDir);
                    }
                }
                if (!instrumentedAppDir.equals(instrumentationAppDir)) {
                    instrumentationLibs = getLibrariesFor(instrumentationPackageName);
                }
            }
        }
        if (outLibPaths != null) {
            if (outLibPaths.isEmpty()) {
                outLibPaths.add(libDir);
            }
            if (aInfo.primaryCpuAbi != null) {
                if (aInfo.targetSdkVersion < 24) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("/system/fake-libs");
                    sb.append(VMRuntime.is64BitAbi(aInfo.primaryCpuAbi) ? "64" : "");
                    outLibPaths.add(sb.toString());
                }
                for (String apk : outZipPaths) {
                    outLibPaths.add(apk + "!/lib/" + aInfo.primaryCpuAbi);
                }
            }
            if (isBundledApp) {
                outLibPaths.add(System.getProperty("java.library.path"));
            }
        }
        Set<String> outSeenPaths = new LinkedHashSet<>();
        appendSharedLibrariesLibPathsIfNeeded(aInfo.sharedLibraryInfos, aInfo, outSeenPaths, outLibPaths);
        if (aInfo.sharedLibraryFiles != null) {
            int index = 0;
            for (String lib : aInfo.sharedLibraryFiles) {
                if (!outSeenPaths.contains(lib) && !outZipPaths.contains(lib)) {
                    outZipPaths.add(index, lib);
                    index++;
                    appendApkLibPathIfNeeded(lib, aInfo, outLibPaths);
                }
            }
        }
        if (instrumentationLibs != null) {
            for (String lib2 : instrumentationLibs) {
                if (!outZipPaths.contains(lib2)) {
                    outZipPaths.add(0, lib2);
                    appendApkLibPathIfNeeded(lib2, aInfo, outLibPaths);
                }
            }
        }
    }

    private static void appendApkLibPathIfNeeded(String path, ApplicationInfo applicationInfo, List<String> outLibPaths) {
        if (outLibPaths != null && applicationInfo.primaryCpuAbi != null && path.endsWith(PackageParser.APK_FILE_EXTENSION) && applicationInfo.targetSdkVersion >= 26) {
            outLibPaths.add(path + "!/lib/" + applicationInfo.primaryCpuAbi);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SplitDependencyLoaderImpl extends SplitDependencyLoader<PackageManager.NameNotFoundException> {
        private final ClassLoader[] mCachedClassLoaders;
        private final String[][] mCachedResourcePaths;

        SplitDependencyLoaderImpl(SparseArray<int[]> dependencies) {
            super(dependencies);
            this.mCachedResourcePaths = new String[LoadedApk.this.mSplitNames.length + 1];
            this.mCachedClassLoaders = new ClassLoader[LoadedApk.this.mSplitNames.length + 1];
        }

        @Override // android.content.pm.split.SplitDependencyLoader
        protected boolean isSplitCached(int splitIdx) {
            return this.mCachedClassLoaders[splitIdx] != null;
        }

        @Override // android.content.pm.split.SplitDependencyLoader
        protected void constructSplit(int splitIdx, int[] configSplitIndices, int parentSplitIdx) throws PackageManager.NameNotFoundException {
            ArrayList<String> splitPaths = new ArrayList<>();
            if (splitIdx == 0) {
                LoadedApk.this.createOrUpdateClassLoaderLocked(null);
                this.mCachedClassLoaders[0] = LoadedApk.this.mClassLoader;
                for (int configSplitIdx : configSplitIndices) {
                    splitPaths.add(LoadedApk.this.mSplitResDirs[configSplitIdx - 1]);
                }
                this.mCachedResourcePaths[0] = (String[]) splitPaths.toArray(new String[splitPaths.size()]);
                return;
            }
            ClassLoader[] classLoaderArr = this.mCachedClassLoaders;
            ClassLoader parent = classLoaderArr[parentSplitIdx];
            classLoaderArr[splitIdx] = ApplicationLoaders.getDefault().getClassLoader(LoadedApk.this.mSplitAppDirs[splitIdx - 1], LoadedApk.this.getTargetSdkVersion(), false, null, null, parent, LoadedApk.this.mSplitClassLoaderNames[splitIdx - 1]);
            Collections.addAll(splitPaths, this.mCachedResourcePaths[parentSplitIdx]);
            splitPaths.add(LoadedApk.this.mSplitResDirs[splitIdx - 1]);
            for (int configSplitIdx2 : configSplitIndices) {
                splitPaths.add(LoadedApk.this.mSplitResDirs[configSplitIdx2 - 1]);
            }
            this.mCachedResourcePaths[splitIdx] = (String[]) splitPaths.toArray(new String[splitPaths.size()]);
        }

        private int ensureSplitLoaded(String splitName) throws PackageManager.NameNotFoundException {
            int idx = 0;
            if (splitName != null) {
                int idx2 = Arrays.binarySearch(LoadedApk.this.mSplitNames, splitName);
                if (idx2 < 0) {
                    throw new PackageManager.NameNotFoundException("Split name '" + splitName + "' is not installed");
                }
                idx = idx2 + 1;
            }
            loadDependenciesForSplit(idx);
            return idx;
        }

        ClassLoader getClassLoaderForSplit(String splitName) throws PackageManager.NameNotFoundException {
            return this.mCachedClassLoaders[ensureSplitLoaded(splitName)];
        }

        String[] getSplitPathsForSplit(String splitName) throws PackageManager.NameNotFoundException {
            return this.mCachedResourcePaths[ensureSplitLoaded(splitName)];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClassLoader getSplitClassLoader(String splitName) throws PackageManager.NameNotFoundException {
        SplitDependencyLoaderImpl splitDependencyLoaderImpl = this.mSplitLoader;
        if (splitDependencyLoaderImpl == null) {
            return this.mClassLoader;
        }
        return splitDependencyLoaderImpl.getClassLoaderForSplit(splitName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String[] getSplitPaths(String splitName) throws PackageManager.NameNotFoundException {
        SplitDependencyLoaderImpl splitDependencyLoaderImpl = this.mSplitLoader;
        if (splitDependencyLoaderImpl == null) {
            return this.mSplitResDirs;
        }
        return splitDependencyLoaderImpl.getSplitPathsForSplit(splitName);
    }

    ClassLoader createSharedLibraryLoader(SharedLibraryInfo sharedLibrary, boolean isBundledApp, String librarySearchPath, String libraryPermittedPath) {
        List<String> paths = sharedLibrary.getAllCodePaths();
        List<ClassLoader> sharedLibraries = createSharedLibrariesLoaders(sharedLibrary.getDependencies(), isBundledApp, librarySearchPath, libraryPermittedPath);
        String jars = paths.size() == 1 ? paths.get(0) : TextUtils.join(File.pathSeparator, paths);
        return ApplicationLoaders.getDefault().getSharedLibraryClassLoaderWithSharedLibraries(jars, this.mApplicationInfo.targetSdkVersion, isBundledApp, librarySearchPath, libraryPermittedPath, null, null, sharedLibraries);
    }

    private List<ClassLoader> createSharedLibrariesLoaders(List<SharedLibraryInfo> sharedLibraries, boolean isBundledApp, String librarySearchPath, String libraryPermittedPath) {
        if (sharedLibraries == null) {
            return null;
        }
        List<ClassLoader> loaders = new ArrayList<>();
        for (SharedLibraryInfo info : sharedLibraries) {
            loaders.add(createSharedLibraryLoader(info, isBundledApp, librarySearchPath, libraryPermittedPath));
        }
        return loaders;
    }

    private StrictMode.ThreadPolicy allowThreadDiskReads() {
        if (this.mActivityThread == null) {
            return null;
        }
        return StrictMode.allowThreadDiskReads();
    }

    private void setThreadPolicy(StrictMode.ThreadPolicy policy) {
        if (this.mActivityThread != null && policy != null) {
            StrictMode.setThreadPolicy(policy);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Finally extract failed */
    public void createOrUpdateClassLoaderLocked(List<String> addedPaths) {
        boolean isBundledApp;
        String libraryPermittedPath;
        boolean needToSetupJitProfiles;
        if (this.mPackageName.equals("android")) {
            if (this.mClassLoader != null) {
                return;
            }
            ClassLoader classLoader = this.mBaseClassLoader;
            if (classLoader == null) {
                this.mDefaultClassLoader = ClassLoader.getSystemClassLoader();
            } else {
                this.mDefaultClassLoader = classLoader;
            }
            this.mAppComponentFactory = createAppFactory(this.mApplicationInfo, this.mDefaultClassLoader);
            this.mClassLoader = this.mAppComponentFactory.instantiateClassLoader(this.mDefaultClassLoader, new ApplicationInfo(this.mApplicationInfo));
            return;
        }
        if (this.mActivityThread != null && !Objects.equals(this.mPackageName, ActivityThread.currentPackageName()) && this.mIncludeCode) {
            try {
                ActivityThread.getPackageManager().notifyPackageUse(this.mPackageName, 6);
            } catch (RemoteException re) {
                throw re.rethrowFromSystemServer();
            }
        }
        if (this.mRegisterPackage) {
            try {
                ActivityManager.getService().addPackageDependency(this.mPackageName);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        List<String> zipPaths = new ArrayList<>(10);
        List<String> libPaths = new ArrayList<>(10);
        boolean isBundledApp2 = this.mApplicationInfo.isSystemApp() && !this.mApplicationInfo.isUpdatedSystemApp();
        String defaultSearchPaths = System.getProperty("java.library.path");
        boolean treatVendorApkAsUnbundled = !defaultSearchPaths.contains("/vendor/lib");
        if (this.mApplicationInfo.getCodePath() != null && this.mApplicationInfo.isVendor() && treatVendorApkAsUnbundled) {
            isBundledApp = false;
        } else {
            isBundledApp = isBundledApp2;
        }
        makePaths(this.mActivityThread, isBundledApp, this.mApplicationInfo, zipPaths, libPaths);
        String libraryPermittedPath2 = this.mDataDir;
        if (!isBundledApp) {
            libraryPermittedPath = libraryPermittedPath2;
        } else {
            libraryPermittedPath = (libraryPermittedPath2 + File.pathSeparator + Paths.get(getAppDir(), new String[0]).getParent().toString()) + File.pathSeparator + defaultSearchPaths;
        }
        String librarySearchPath = TextUtils.join(File.pathSeparator, libPaths);
        if (!this.mIncludeCode) {
            if (this.mDefaultClassLoader == null) {
                StrictMode.ThreadPolicy oldPolicy = allowThreadDiskReads();
                this.mDefaultClassLoader = ApplicationLoaders.getDefault().getClassLoader("", this.mApplicationInfo.targetSdkVersion, isBundledApp, librarySearchPath, libraryPermittedPath, this.mBaseClassLoader, null);
                setThreadPolicy(oldPolicy);
                this.mAppComponentFactory = AppComponentFactory.DEFAULT;
            }
            if (this.mClassLoader == null) {
                this.mClassLoader = this.mAppComponentFactory.instantiateClassLoader(this.mDefaultClassLoader, new ApplicationInfo(this.mApplicationInfo));
                return;
            }
            return;
        }
        boolean isBundledApp3 = isBundledApp;
        String zip = zipPaths.size() == 1 ? zipPaths.get(0) : TextUtils.join(File.pathSeparator, zipPaths);
        if (this.mDefaultClassLoader != null) {
            needToSetupJitProfiles = false;
        } else {
            StrictMode.ThreadPolicy oldPolicy2 = allowThreadDiskReads();
            List<ClassLoader> sharedLibraries = createSharedLibrariesLoaders(this.mApplicationInfo.sharedLibraryInfos, isBundledApp3, librarySearchPath, libraryPermittedPath);
            this.mDefaultClassLoader = ApplicationLoaders.getDefault().getClassLoaderWithSharedLibraries(zip, this.mApplicationInfo.targetSdkVersion, isBundledApp3, librarySearchPath, libraryPermittedPath, this.mBaseClassLoader, this.mApplicationInfo.classLoaderName, sharedLibraries);
            this.mAppComponentFactory = createAppFactory(this.mApplicationInfo, this.mDefaultClassLoader);
            setThreadPolicy(oldPolicy2);
            needToSetupJitProfiles = true;
        }
        boolean needToSetupJitProfiles2 = libPaths.isEmpty();
        if (!needToSetupJitProfiles2 && SystemProperties.getBoolean(PROPERTY_NAME_APPEND_NATIVE, true)) {
            StrictMode.ThreadPolicy oldPolicy3 = allowThreadDiskReads();
            try {
                ApplicationLoaders.getDefault().addNative(this.mDefaultClassLoader, libPaths);
                setThreadPolicy(oldPolicy3);
            } catch (Throwable th) {
                setThreadPolicy(oldPolicy3);
                throw th;
            }
        }
        List<String> extraLibPaths = new ArrayList<>(4);
        String abiSuffix = VMRuntime.getRuntime().is64Bit() ? "64" : "";
        if (!defaultSearchPaths.contains("/apex/com.android.runtime/lib")) {
            extraLibPaths.add("/apex/com.android.runtime/lib" + abiSuffix);
        }
        if (!defaultSearchPaths.contains("/vendor/lib")) {
            extraLibPaths.add("/vendor/lib" + abiSuffix);
        }
        if (!defaultSearchPaths.contains("/odm/lib")) {
            extraLibPaths.add("/odm/lib" + abiSuffix);
        }
        if (!defaultSearchPaths.contains("/product/lib")) {
            extraLibPaths.add("/product/lib" + abiSuffix);
        }
        if (!extraLibPaths.isEmpty()) {
            StrictMode.ThreadPolicy oldPolicy4 = allowThreadDiskReads();
            try {
                ApplicationLoaders.getDefault().addNative(this.mDefaultClassLoader, extraLibPaths);
                setThreadPolicy(oldPolicy4);
            } catch (Throwable th2) {
                setThreadPolicy(oldPolicy4);
                throw th2;
            }
        }
        if (addedPaths != null && addedPaths.size() > 0) {
            String add = TextUtils.join(File.pathSeparator, addedPaths);
            ApplicationLoaders.getDefault().addPath(this.mDefaultClassLoader, add);
            needToSetupJitProfiles = true;
        }
        if (needToSetupJitProfiles && !ActivityThread.isSystem() && this.mActivityThread != null) {
            setupJitProfileSupport();
        }
        if (this.mClassLoader == null) {
            this.mClassLoader = this.mAppComponentFactory.instantiateClassLoader(this.mDefaultClassLoader, new ApplicationInfo(this.mApplicationInfo));
        }
    }

    @UnsupportedAppUsage
    public ClassLoader getClassLoader() {
        ClassLoader classLoader;
        synchronized (this) {
            if (this.mClassLoader == null) {
                createOrUpdateClassLoaderLocked(null);
            }
            classLoader = this.mClassLoader;
        }
        return classLoader;
    }

    private void setupJitProfileSupport() {
        if (!SystemProperties.getBoolean("dalvik.vm.usejitprofiles", false)) {
            return;
        }
        BaseDexClassLoader.setReporter(DexLoadReporter.getInstance());
        if (this.mApplicationInfo.uid != Process.myUid()) {
            return;
        }
        List<String> codePaths = new ArrayList<>();
        if ((this.mApplicationInfo.flags & 4) != 0) {
            codePaths.add(this.mApplicationInfo.sourceDir);
        }
        if (this.mApplicationInfo.splitSourceDirs != null) {
            Collections.addAll(codePaths, this.mApplicationInfo.splitSourceDirs);
        }
        if (codePaths.isEmpty()) {
            return;
        }
        int i = codePaths.size() - 1;
        while (i >= 0) {
            String splitName = i == 0 ? null : this.mApplicationInfo.splitNames[i - 1];
            String profileFile = ArtManager.getCurrentProfilePath(this.mPackageName, UserHandle.myUserId(), splitName);
            VMRuntime.registerAppInfo(profileFile, new String[]{codePaths.get(i)});
            i--;
        }
        DexLoadReporter.getInstance().registerAppDataDir(this.mPackageName, this.mDataDir);
    }

    private void initializeJavaContextClassLoader() {
        ClassLoader contextClassLoader;
        IPackageManager pm = ActivityThread.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(this.mPackageName, 268435456, UserHandle.myUserId());
            if (pi == null) {
                throw new IllegalStateException("Unable to get package info for " + this.mPackageName + "; is package not installed?");
            }
            boolean sharable = true;
            boolean sharedUserIdSet = pi.sharedUserId != null;
            boolean processNameNotDefault = (pi.applicationInfo == null || this.mPackageName.equals(pi.applicationInfo.processName)) ? false : true;
            if (!sharedUserIdSet && !processNameNotDefault) {
                sharable = false;
            }
            if (sharable) {
                contextClassLoader = new WarningContextClassLoader();
            } else {
                contextClassLoader = this.mClassLoader;
            }
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class WarningContextClassLoader extends ClassLoader {
        private static boolean warned = false;

        private WarningContextClassLoader() {
        }

        private void warn(String methodName) {
            if (warned) {
                return;
            }
            warned = true;
            Thread.currentThread().setContextClassLoader(getParent());
            Slog.w(ActivityThread.TAG, "ClassLoader." + methodName + ": The class loader returned by Thread.getContextClassLoader() may fail for processes that host multiple applications. You should explicitly specify a context class loader. For example: Thread.setContextClassLoader(getClass().getClassLoader());");
        }

        @Override // java.lang.ClassLoader
        public URL getResource(String resName) {
            warn("getResource");
            return getParent().getResource(resName);
        }

        @Override // java.lang.ClassLoader
        public Enumeration<URL> getResources(String resName) throws IOException {
            warn("getResources");
            return getParent().getResources(resName);
        }

        @Override // java.lang.ClassLoader
        public InputStream getResourceAsStream(String resName) {
            warn("getResourceAsStream");
            return getParent().getResourceAsStream(resName);
        }

        @Override // java.lang.ClassLoader
        public Class<?> loadClass(String className) throws ClassNotFoundException {
            warn("loadClass");
            return getParent().loadClass(className);
        }

        @Override // java.lang.ClassLoader
        public void setClassAssertionStatus(String cname, boolean enable) {
            warn("setClassAssertionStatus");
            getParent().setClassAssertionStatus(cname, enable);
        }

        @Override // java.lang.ClassLoader
        public void setPackageAssertionStatus(String pname, boolean enable) {
            warn("setPackageAssertionStatus");
            getParent().setPackageAssertionStatus(pname, enable);
        }

        @Override // java.lang.ClassLoader
        public void setDefaultAssertionStatus(boolean enable) {
            warn("setDefaultAssertionStatus");
            getParent().setDefaultAssertionStatus(enable);
        }

        @Override // java.lang.ClassLoader
        public void clearAssertionStatus() {
            warn("clearAssertionStatus");
            getParent().clearAssertionStatus();
        }
    }

    @UnsupportedAppUsage
    public String getAppDir() {
        return this.mAppDir;
    }

    public String getLibDir() {
        return this.mLibDir;
    }

    @UnsupportedAppUsage
    public String getResDir() {
        return this.mResDir;
    }

    public String[] getSplitAppDirs() {
        return this.mSplitAppDirs;
    }

    @UnsupportedAppUsage
    public String[] getSplitResDirs() {
        return this.mSplitResDirs;
    }

    @UnsupportedAppUsage
    public String[] getOverlayDirs() {
        return this.mOverlayDirs;
    }

    public String getDataDir() {
        return this.mDataDir;
    }

    @UnsupportedAppUsage
    public File getDataDirFile() {
        return this.mDataDirFile;
    }

    public File getDeviceProtectedDataDirFile() {
        return this.mDeviceProtectedDataDirFile;
    }

    public File getCredentialProtectedDataDirFile() {
        return this.mCredentialProtectedDataDirFile;
    }

    @UnsupportedAppUsage
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    @UnsupportedAppUsage
    public Resources getResources() {
        if (this.mResources == null) {
            try {
                String[] splitPaths = getSplitPaths(null);
                this.mResources = ResourcesManager.getInstance().getResources(null, this.mResDir, splitPaths, this.mOverlayDirs, this.mApplicationInfo.sharedLibraryFiles, 0, null, getCompatibilityInfo(), getClassLoader());
            } catch (PackageManager.NameNotFoundException e) {
                throw new AssertionError("null split not found");
            }
        }
        return this.mResources;
    }

    @UnsupportedAppUsage
    public Application makeApplication(boolean forceDefaultAppClass, Instrumentation instrumentation) {
        Application application = this.mApplication;
        if (application != null) {
            return application;
        }
        Trace.traceBegin(64L, "makeApplication");
        Application app = null;
        String appClass = this.mApplicationInfo.className;
        appClass = (forceDefaultAppClass || appClass == null) ? "android.app.Application" : "android.app.Application";
        try {
            ClassLoader cl = getClassLoader();
            if (!this.mPackageName.equals("android")) {
                Trace.traceBegin(64L, "initializeJavaContextClassLoader");
                initializeJavaContextClassLoader();
                Trace.traceEnd(64L);
            }
            ContextImpl appContext = ContextImpl.createAppContext(this.mActivityThread, this);
            app = this.mActivityThread.mInstrumentation.newApplication(cl, appClass, appContext);
            appContext.setOuterContext(app);
        } catch (Exception e) {
            if (!this.mActivityThread.mInstrumentation.onException(app, e)) {
                Trace.traceEnd(64L);
                throw new RuntimeException("Unable to instantiate application " + appClass + ": " + e.toString(), e);
            }
        }
        this.mActivityThread.mAllApplications.add(app);
        this.mApplication = app;
        if (instrumentation != null) {
            try {
                instrumentation.callApplicationOnCreate(app);
            } catch (Exception e2) {
                if (!instrumentation.onException(app, e2)) {
                    Trace.traceEnd(64L);
                    throw new RuntimeException("Unable to create application " + app.getClass().getName() + ": " + e2.toString(), e2);
                }
            }
        }
        SparseArray<String> packageIdentifiers = getAssets().getAssignedPackageIdentifiers();
        int N = packageIdentifiers.size();
        for (int i = 0; i < N; i++) {
            int id = packageIdentifiers.keyAt(i);
            if (id != 1 && id != 127) {
                rewriteRValues(getClassLoader(), packageIdentifiers.valueAt(i), id);
            }
        }
        Trace.traceEnd(64L);
        return app;
    }

    @UnsupportedAppUsage
    private void rewriteRValues(ClassLoader cl, String packageName, int id) {
        Throwable cause;
        try {
            Class<?> rClazz = cl.loadClass(packageName + ".R");
            try {
                Method callback = rClazz.getMethod("onResourcesLoaded", Integer.TYPE);
                try {
                    callback.invoke(null, Integer.valueOf(id));
                } catch (IllegalAccessException e) {
                    cause = e;
                    throw new RuntimeException("Failed to rewrite resource references for " + packageName, cause);
                } catch (InvocationTargetException e2) {
                    cause = e2.getCause();
                    throw new RuntimeException("Failed to rewrite resource references for " + packageName, cause);
                }
            } catch (NoSuchMethodException e3) {
            }
        } catch (ClassNotFoundException e4) {
            Log.i(TAG, "No resource references to update in package " + packageName);
        }
    }

    public void removeContextRegistrations(Context context, String who, String what) {
        int i;
        boolean reportRegistrationLeaks = StrictMode.vmRegistrationLeaksEnabled();
        synchronized (this.mReceivers) {
            ArrayMap<BroadcastReceiver, ReceiverDispatcher> rmap = this.mReceivers.remove(context);
            if (rmap != null) {
                for (int i2 = 0; i2 < rmap.size(); i2++) {
                    ReceiverDispatcher rd = rmap.valueAt(i2);
                    IntentReceiverLeaked leak = new IntentReceiverLeaked(what + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + who + " has leaked IntentReceiver " + rd.getIntentReceiver() + " that was originally registered here. Are you missing a call to unregisterReceiver()?");
                    leak.setStackTrace(rd.getLocation().getStackTrace());
                    Slog.e(ActivityThread.TAG, leak.getMessage(), leak);
                    if (reportRegistrationLeaks) {
                        StrictMode.onIntentReceiverLeaked(leak);
                    }
                    try {
                        ActivityManager.getService().unregisterReceiver(rd.getIIntentReceiver());
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            }
            this.mUnregisteredReceivers.remove(context);
        }
        synchronized (this.mServices) {
            ArrayMap<ServiceConnection, ServiceDispatcher> smap = this.mServices.remove(context);
            if (smap != null) {
                for (i = 0; i < smap.size(); i++) {
                    ServiceDispatcher sd = smap.valueAt(i);
                    ServiceConnectionLeaked leak2 = new ServiceConnectionLeaked(what + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + who + " has leaked ServiceConnection " + sd.getServiceConnection() + " that was originally bound here");
                    leak2.setStackTrace(sd.getLocation().getStackTrace());
                    Slog.e(ActivityThread.TAG, leak2.getMessage(), leak2);
                    if (reportRegistrationLeaks) {
                        StrictMode.onServiceConnectionLeaked(leak2);
                    }
                    try {
                        ActivityManager.getService().unbindService(sd.getIServiceConnection());
                        sd.doForget();
                    } catch (RemoteException e2) {
                        throw e2.rethrowFromSystemServer();
                    }
                }
            }
            this.mUnboundServices.remove(context);
        }
    }

    public IIntentReceiver getReceiverDispatcher(BroadcastReceiver r, Context context, Handler handler, Instrumentation instrumentation, boolean registered) {
        IIntentReceiver iIntentReceiver;
        synchronized (this.mReceivers) {
            ReceiverDispatcher rd = null;
            ArrayMap<BroadcastReceiver, ReceiverDispatcher> map = null;
            if (registered) {
                try {
                    map = this.mReceivers.get(context);
                    if (map != null) {
                        rd = map.get(r);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (rd == null) {
                rd = new ReceiverDispatcher(r, context, handler, instrumentation, registered);
                if (registered) {
                    if (map == null) {
                        map = new ArrayMap<>();
                        this.mReceivers.put(context, map);
                    }
                    map.put(r, rd);
                }
            } else {
                rd.validate(context, handler);
            }
            rd.mForgotten = false;
            iIntentReceiver = rd.getIIntentReceiver();
        }
        return iIntentReceiver;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x00ad, code lost:
        throw new java.lang.IllegalStateException("Unbinding Receiver " + r10 + " from Context that is no longer in use: " + r9);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.content.IIntentReceiver forgetReceiverDispatcher(android.content.Context r9, android.content.BroadcastReceiver r10) {
        /*
            r8 = this;
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk$ReceiverDispatcher>> r0 = r8.mReceivers
            monitor-enter(r0)
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk$ReceiverDispatcher>> r1 = r8.mReceivers     // Catch: java.lang.Throwable -> Lc5
            java.lang.Object r1 = r1.get(r9)     // Catch: java.lang.Throwable -> Lc5
            android.util.ArrayMap r1 = (android.util.ArrayMap) r1     // Catch: java.lang.Throwable -> Lc5
            r2 = 0
            if (r1 == 0) goto L59
            java.lang.Object r3 = r1.get(r10)     // Catch: java.lang.Throwable -> Lc5
            android.app.LoadedApk$ReceiverDispatcher r3 = (android.app.LoadedApk.ReceiverDispatcher) r3     // Catch: java.lang.Throwable -> Lc5
            r2 = r3
            if (r2 == 0) goto L59
            r1.remove(r10)     // Catch: java.lang.Throwable -> Lc5
            int r3 = r1.size()     // Catch: java.lang.Throwable -> Lc5
            if (r3 != 0) goto L25
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk$ReceiverDispatcher>> r3 = r8.mReceivers     // Catch: java.lang.Throwable -> Lc5
            r3.remove(r9)     // Catch: java.lang.Throwable -> Lc5
        L25:
            boolean r3 = r10.getDebugUnregister()     // Catch: java.lang.Throwable -> Lc5
            if (r3 == 0) goto L50
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk$ReceiverDispatcher>> r3 = r8.mUnregisteredReceivers     // Catch: java.lang.Throwable -> Lc5
            java.lang.Object r3 = r3.get(r9)     // Catch: java.lang.Throwable -> Lc5
            android.util.ArrayMap r3 = (android.util.ArrayMap) r3     // Catch: java.lang.Throwable -> Lc5
            if (r3 != 0) goto L40
            android.util.ArrayMap r4 = new android.util.ArrayMap     // Catch: java.lang.Throwable -> Lc5
            r4.<init>()     // Catch: java.lang.Throwable -> Lc5
            r3 = r4
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk$ReceiverDispatcher>> r4 = r8.mUnregisteredReceivers     // Catch: java.lang.Throwable -> Lc5
            r4.put(r9, r3)     // Catch: java.lang.Throwable -> Lc5
        L40:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> Lc5
            java.lang.String r5 = "Originally unregistered here:"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> Lc5
            r4.fillInStackTrace()     // Catch: java.lang.Throwable -> Lc5
            r2.setUnregisterLocation(r4)     // Catch: java.lang.Throwable -> Lc5
            r3.put(r10, r2)     // Catch: java.lang.Throwable -> Lc5
        L50:
            r3 = 1
            r2.mForgotten = r3     // Catch: java.lang.Throwable -> Lc5
            android.content.IIntentReceiver r3 = r2.getIIntentReceiver()     // Catch: java.lang.Throwable -> Lc5
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc5
            return r3
        L59:
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.BroadcastReceiver, android.app.LoadedApk$ReceiverDispatcher>> r3 = r8.mUnregisteredReceivers     // Catch: java.lang.Throwable -> Lc5
            java.lang.Object r3 = r3.get(r9)     // Catch: java.lang.Throwable -> Lc5
            android.util.ArrayMap r3 = (android.util.ArrayMap) r3     // Catch: java.lang.Throwable -> Lc5
            if (r3 == 0) goto L8d
            java.lang.Object r4 = r3.get(r10)     // Catch: java.lang.Throwable -> Lc5
            android.app.LoadedApk$ReceiverDispatcher r4 = (android.app.LoadedApk.ReceiverDispatcher) r4     // Catch: java.lang.Throwable -> Lc5
            r2 = r4
            if (r2 != 0) goto L6d
            goto L8d
        L6d:
            java.lang.RuntimeException r4 = r2.getUnregisterLocation()     // Catch: java.lang.Throwable -> Lc5
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> Lc5
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc5
            r6.<init>()     // Catch: java.lang.Throwable -> Lc5
            java.lang.String r7 = "Unregistering Receiver "
            r6.append(r7)     // Catch: java.lang.Throwable -> Lc5
            r6.append(r10)     // Catch: java.lang.Throwable -> Lc5
            java.lang.String r7 = " that was already unregistered"
            r6.append(r7)     // Catch: java.lang.Throwable -> Lc5
            java.lang.String r6 = r6.toString()     // Catch: java.lang.Throwable -> Lc5
            r5.<init>(r6, r4)     // Catch: java.lang.Throwable -> Lc5
            throw r5     // Catch: java.lang.Throwable -> Lc5
        L8d:
            if (r9 != 0) goto Lae
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> Lc5
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc5
            r5.<init>()     // Catch: java.lang.Throwable -> Lc5
            java.lang.String r6 = "Unbinding Receiver "
            r5.append(r6)     // Catch: java.lang.Throwable -> Lc5
            r5.append(r10)     // Catch: java.lang.Throwable -> Lc5
            java.lang.String r6 = " from Context that is no longer in use: "
            r5.append(r6)     // Catch: java.lang.Throwable -> Lc5
            r5.append(r9)     // Catch: java.lang.Throwable -> Lc5
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> Lc5
            r4.<init>(r5)     // Catch: java.lang.Throwable -> Lc5
            throw r4     // Catch: java.lang.Throwable -> Lc5
        Lae:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> Lc5
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc5
            r5.<init>()     // Catch: java.lang.Throwable -> Lc5
            java.lang.String r6 = "Receiver not registered: "
            r5.append(r6)     // Catch: java.lang.Throwable -> Lc5
            r5.append(r10)     // Catch: java.lang.Throwable -> Lc5
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> Lc5
            r4.<init>(r5)     // Catch: java.lang.Throwable -> Lc5
            throw r4     // Catch: java.lang.Throwable -> Lc5
        Lc5:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc5
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.LoadedApk.forgetReceiverDispatcher(android.content.Context, android.content.BroadcastReceiver):android.content.IIntentReceiver");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ReceiverDispatcher {
        final Handler mActivityThread;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        final Context mContext;
        boolean mForgotten;
        final IIntentReceiver.Stub mIIntentReceiver;
        final Instrumentation mInstrumentation;
        final IntentReceiverLeaked mLocation;
        @UnsupportedAppUsage
        final BroadcastReceiver mReceiver;
        final boolean mRegistered;
        RuntimeException mUnregisterLocation;

        /* loaded from: classes.dex */
        static final class InnerReceiver extends IIntentReceiver.Stub {
            final WeakReference<ReceiverDispatcher> mDispatcher;
            final ReceiverDispatcher mStrongRef;

            InnerReceiver(ReceiverDispatcher rd, boolean strong) {
                this.mDispatcher = new WeakReference<>(rd);
                this.mStrongRef = strong ? rd : null;
            }

            @Override // android.content.IIntentReceiver
            public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
                ReceiverDispatcher rd;
                if (intent == null) {
                    Log.wtf(LoadedApk.TAG, "Null intent received");
                    rd = null;
                } else {
                    rd = this.mDispatcher.get();
                }
                if (ActivityThread.DEBUG_BROADCAST) {
                    int seq = intent.getIntExtra("seq", -1);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Receiving broadcast ");
                    sb.append(intent.getAction());
                    sb.append(" seq=");
                    sb.append(seq);
                    sb.append(" to ");
                    sb.append(rd != null ? rd.mReceiver : null);
                    Slog.i(ActivityThread.TAG, sb.toString());
                }
                if (rd != null) {
                    rd.performReceive(intent, resultCode, data, extras, ordered, sticky, sendingUser);
                    return;
                }
                if (ActivityThread.DEBUG_BROADCAST) {
                    Slog.i(ActivityThread.TAG, "Finishing broadcast to unregistered receiver");
                }
                IActivityManager mgr = ActivityManager.getService();
                if (extras != null) {
                    try {
                        extras.setAllowFds(false);
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
                mgr.finishReceiver(this, resultCode, data, extras, false, intent.getFlags());
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public final class Args extends BroadcastReceiver.PendingResult {
            private Intent mCurIntent;
            private boolean mDispatched;
            private final boolean mOrdered;
            private boolean mRunCalled;

            public Args(Intent intent, int resultCode, String resultData, Bundle resultExtras, boolean ordered, boolean sticky, int sendingUser) {
                super(resultCode, resultData, resultExtras, ReceiverDispatcher.this.mRegistered ? 1 : 2, ordered, sticky, ReceiverDispatcher.this.mIIntentReceiver.asBinder(), sendingUser, intent.getFlags());
                this.mCurIntent = intent;
                this.mOrdered = ordered;
            }

            public final Runnable getRunnable() {
                return new Runnable() { // from class: android.app.-$$Lambda$LoadedApk$ReceiverDispatcher$Args$_BumDX2UKsnxLVrE6UJsJZkotuA
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoadedApk.ReceiverDispatcher.Args.this.lambda$getRunnable$0$LoadedApk$ReceiverDispatcher$Args();
                    }
                };
            }

            public /* synthetic */ void lambda$getRunnable$0$LoadedApk$ReceiverDispatcher$Args() {
                BroadcastReceiver receiver = ReceiverDispatcher.this.mReceiver;
                boolean ordered = this.mOrdered;
                if (ActivityThread.DEBUG_BROADCAST) {
                    int seq = this.mCurIntent.getIntExtra("seq", -1);
                    Slog.i(ActivityThread.TAG, "Dispatching broadcast " + this.mCurIntent.getAction() + " seq=" + seq + " to " + ReceiverDispatcher.this.mReceiver);
                    StringBuilder sb = new StringBuilder();
                    sb.append("  mRegistered=");
                    sb.append(ReceiverDispatcher.this.mRegistered);
                    sb.append(" mOrderedHint=");
                    sb.append(ordered);
                    Slog.i(ActivityThread.TAG, sb.toString());
                }
                IActivityManager mgr = ActivityManager.getService();
                Intent intent = this.mCurIntent;
                if (intent == null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Null intent being dispatched, mDispatched=");
                    sb2.append(this.mDispatched);
                    sb2.append(this.mRunCalled ? ", run() has already been called" : "");
                    Log.wtf(LoadedApk.TAG, sb2.toString());
                }
                this.mCurIntent = null;
                this.mDispatched = true;
                this.mRunCalled = true;
                if (receiver == null || intent == null || ReceiverDispatcher.this.mForgotten) {
                    if (ReceiverDispatcher.this.mRegistered && ordered) {
                        if (ActivityThread.DEBUG_BROADCAST) {
                            Slog.i(ActivityThread.TAG, "Finishing null broadcast to " + ReceiverDispatcher.this.mReceiver);
                        }
                        sendFinished(mgr);
                        return;
                    }
                    return;
                }
                Trace.traceBegin(64L, "broadcastReceiveReg");
                try {
                    ClassLoader cl = ReceiverDispatcher.this.mReceiver.getClass().getClassLoader();
                    intent.setExtrasClassLoader(cl);
                    intent.prepareToEnterProcess();
                    setExtrasClassLoader(cl);
                    receiver.setPendingResult(this);
                    receiver.onReceive(ReceiverDispatcher.this.mContext, intent);
                } catch (Exception e) {
                    if (ReceiverDispatcher.this.mRegistered && ordered) {
                        if (ActivityThread.DEBUG_BROADCAST) {
                            Slog.i(ActivityThread.TAG, "Finishing failed broadcast to " + ReceiverDispatcher.this.mReceiver);
                        }
                        sendFinished(mgr);
                    }
                    if (ReceiverDispatcher.this.mInstrumentation == null || !ReceiverDispatcher.this.mInstrumentation.onException(ReceiverDispatcher.this.mReceiver, e)) {
                        Trace.traceEnd(64L);
                        throw new RuntimeException("Error receiving broadcast " + intent + " in " + ReceiverDispatcher.this.mReceiver, e);
                    }
                }
                if (receiver.getPendingResult() != null) {
                    finish();
                }
                Trace.traceEnd(64L);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ReceiverDispatcher(BroadcastReceiver receiver, Context context, Handler activityThread, Instrumentation instrumentation, boolean registered) {
            if (activityThread == null) {
                throw new NullPointerException("Handler must not be null");
            }
            this.mIIntentReceiver = new InnerReceiver(this, !registered);
            this.mReceiver = receiver;
            this.mContext = context;
            this.mActivityThread = activityThread;
            this.mInstrumentation = instrumentation;
            this.mRegistered = registered;
            this.mLocation = new IntentReceiverLeaked(null);
            this.mLocation.fillInStackTrace();
        }

        void validate(Context context, Handler activityThread) {
            if (this.mContext != context) {
                throw new IllegalStateException("Receiver " + this.mReceiver + " registered with differing Context (was " + this.mContext + " now " + context + ")");
            } else if (this.mActivityThread != activityThread) {
                throw new IllegalStateException("Receiver " + this.mReceiver + " registered with differing handler (was " + this.mActivityThread + " now " + activityThread + ")");
            }
        }

        IntentReceiverLeaked getLocation() {
            return this.mLocation;
        }

        @UnsupportedAppUsage
        BroadcastReceiver getIntentReceiver() {
            return this.mReceiver;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @UnsupportedAppUsage
        public IIntentReceiver getIIntentReceiver() {
            return this.mIIntentReceiver;
        }

        void setUnregisterLocation(RuntimeException ex) {
            this.mUnregisterLocation = ex;
        }

        RuntimeException getUnregisterLocation() {
            return this.mUnregisterLocation;
        }

        public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
            Args args = new Args(intent, resultCode, data, extras, ordered, sticky, sendingUser);
            if (intent == null) {
                Log.wtf(LoadedApk.TAG, "Null intent received");
            } else if (ActivityThread.DEBUG_BROADCAST) {
                int seq = intent.getIntExtra("seq", -1);
                Slog.i(ActivityThread.TAG, "Enqueueing broadcast " + intent.getAction() + " seq=" + seq + " to " + this.mReceiver);
            }
            if ((intent == null || !this.mActivityThread.post(args.getRunnable())) && this.mRegistered && ordered) {
                IActivityManager mgr = ActivityManager.getService();
                if (ActivityThread.DEBUG_BROADCAST) {
                    Slog.i(ActivityThread.TAG, "Finishing sync broadcast to " + this.mReceiver);
                }
                args.sendFinished(mgr);
            }
        }
    }

    @UnsupportedAppUsage
    public final IServiceConnection getServiceDispatcher(ServiceConnection c, Context context, Handler handler, int flags) {
        return getServiceDispatcherCommon(c, context, handler, null, flags);
    }

    public final IServiceConnection getServiceDispatcher(ServiceConnection c, Context context, Executor executor, int flags) {
        return getServiceDispatcherCommon(c, context, null, executor, flags);
    }

    private IServiceConnection getServiceDispatcherCommon(ServiceConnection c, Context context, Handler handler, Executor executor, int flags) {
        IServiceConnection iServiceConnection;
        synchronized (this.mServices) {
            ServiceDispatcher sd = null;
            ArrayMap<ServiceConnection, ServiceDispatcher> map = this.mServices.get(context);
            if (map != null) {
                sd = map.get(c);
            }
            if (sd == null) {
                if (executor != null) {
                    sd = new ServiceDispatcher(c, context, executor, flags);
                } else {
                    sd = new ServiceDispatcher(c, context, handler, flags);
                }
                if (map == null) {
                    map = new ArrayMap<>();
                    this.mServices.put(context, map);
                }
                map.put(c, sd);
            } else {
                sd.validate(context, handler, executor);
            }
            iServiceConnection = sd.getIServiceConnection();
        }
        return iServiceConnection;
    }

    @UnsupportedAppUsage
    public IServiceConnection lookupServiceDispatcher(ServiceConnection c, Context context) {
        IServiceConnection iServiceConnection;
        synchronized (this.mServices) {
            ServiceDispatcher sd = null;
            ArrayMap<ServiceConnection, ServiceDispatcher> map = this.mServices.get(context);
            if (map != null) {
                sd = map.get(c);
            }
            iServiceConnection = sd != null ? sd.getIServiceConnection() : null;
        }
        return iServiceConnection;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x00af, code lost:
        throw new java.lang.IllegalStateException("Unbinding Service " + r10 + " from Context that is no longer in use: " + r9);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final android.app.IServiceConnection forgetServiceDispatcher(android.content.Context r9, android.content.ServiceConnection r10) {
        /*
            r8 = this;
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk$ServiceDispatcher>> r0 = r8.mServices
            monitor-enter(r0)
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk$ServiceDispatcher>> r1 = r8.mServices     // Catch: java.lang.Throwable -> Lc7
            java.lang.Object r1 = r1.get(r9)     // Catch: java.lang.Throwable -> Lc7
            android.util.ArrayMap r1 = (android.util.ArrayMap) r1     // Catch: java.lang.Throwable -> Lc7
            r2 = 0
            if (r1 == 0) goto L5b
            java.lang.Object r3 = r1.get(r10)     // Catch: java.lang.Throwable -> Lc7
            android.app.LoadedApk$ServiceDispatcher r3 = (android.app.LoadedApk.ServiceDispatcher) r3     // Catch: java.lang.Throwable -> Lc7
            r2 = r3
            if (r2 == 0) goto L5b
            r1.remove(r10)     // Catch: java.lang.Throwable -> Lc7
            r2.doForget()     // Catch: java.lang.Throwable -> Lc7
            int r3 = r1.size()     // Catch: java.lang.Throwable -> Lc7
            if (r3 != 0) goto L28
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk$ServiceDispatcher>> r3 = r8.mServices     // Catch: java.lang.Throwable -> Lc7
            r3.remove(r9)     // Catch: java.lang.Throwable -> Lc7
        L28:
            int r3 = r2.getFlags()     // Catch: java.lang.Throwable -> Lc7
            r3 = r3 & 2
            if (r3 == 0) goto L55
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk$ServiceDispatcher>> r3 = r8.mUnboundServices     // Catch: java.lang.Throwable -> Lc7
            java.lang.Object r3 = r3.get(r9)     // Catch: java.lang.Throwable -> Lc7
            android.util.ArrayMap r3 = (android.util.ArrayMap) r3     // Catch: java.lang.Throwable -> Lc7
            if (r3 != 0) goto L45
            android.util.ArrayMap r4 = new android.util.ArrayMap     // Catch: java.lang.Throwable -> Lc7
            r4.<init>()     // Catch: java.lang.Throwable -> Lc7
            r3 = r4
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk$ServiceDispatcher>> r4 = r8.mUnboundServices     // Catch: java.lang.Throwable -> Lc7
            r4.put(r9, r3)     // Catch: java.lang.Throwable -> Lc7
        L45:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> Lc7
            java.lang.String r5 = "Originally unbound here:"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> Lc7
            r4.fillInStackTrace()     // Catch: java.lang.Throwable -> Lc7
            r2.setUnbindLocation(r4)     // Catch: java.lang.Throwable -> Lc7
            r3.put(r10, r2)     // Catch: java.lang.Throwable -> Lc7
        L55:
            android.app.IServiceConnection r3 = r2.getIServiceConnection()     // Catch: java.lang.Throwable -> Lc7
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc7
            return r3
        L5b:
            android.util.ArrayMap<android.content.Context, android.util.ArrayMap<android.content.ServiceConnection, android.app.LoadedApk$ServiceDispatcher>> r3 = r8.mUnboundServices     // Catch: java.lang.Throwable -> Lc7
            java.lang.Object r3 = r3.get(r9)     // Catch: java.lang.Throwable -> Lc7
            android.util.ArrayMap r3 = (android.util.ArrayMap) r3     // Catch: java.lang.Throwable -> Lc7
            if (r3 == 0) goto L8f
            java.lang.Object r4 = r3.get(r10)     // Catch: java.lang.Throwable -> Lc7
            android.app.LoadedApk$ServiceDispatcher r4 = (android.app.LoadedApk.ServiceDispatcher) r4     // Catch: java.lang.Throwable -> Lc7
            r2 = r4
            if (r2 != 0) goto L6f
            goto L8f
        L6f:
            java.lang.RuntimeException r4 = r2.getUnbindLocation()     // Catch: java.lang.Throwable -> Lc7
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> Lc7
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc7
            r6.<init>()     // Catch: java.lang.Throwable -> Lc7
            java.lang.String r7 = "Unbinding Service "
            r6.append(r7)     // Catch: java.lang.Throwable -> Lc7
            r6.append(r10)     // Catch: java.lang.Throwable -> Lc7
            java.lang.String r7 = " that was already unbound"
            r6.append(r7)     // Catch: java.lang.Throwable -> Lc7
            java.lang.String r6 = r6.toString()     // Catch: java.lang.Throwable -> Lc7
            r5.<init>(r6, r4)     // Catch: java.lang.Throwable -> Lc7
            throw r5     // Catch: java.lang.Throwable -> Lc7
        L8f:
            if (r9 != 0) goto Lb0
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> Lc7
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc7
            r5.<init>()     // Catch: java.lang.Throwable -> Lc7
            java.lang.String r6 = "Unbinding Service "
            r5.append(r6)     // Catch: java.lang.Throwable -> Lc7
            r5.append(r10)     // Catch: java.lang.Throwable -> Lc7
            java.lang.String r6 = " from Context that is no longer in use: "
            r5.append(r6)     // Catch: java.lang.Throwable -> Lc7
            r5.append(r9)     // Catch: java.lang.Throwable -> Lc7
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> Lc7
            r4.<init>(r5)     // Catch: java.lang.Throwable -> Lc7
            throw r4     // Catch: java.lang.Throwable -> Lc7
        Lb0:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> Lc7
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc7
            r5.<init>()     // Catch: java.lang.Throwable -> Lc7
            java.lang.String r6 = "Service not registered: "
            r5.append(r6)     // Catch: java.lang.Throwable -> Lc7
            r5.append(r10)     // Catch: java.lang.Throwable -> Lc7
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> Lc7
            r4.<init>(r5)     // Catch: java.lang.Throwable -> Lc7
            throw r4     // Catch: java.lang.Throwable -> Lc7
        Lc7:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc7
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.LoadedApk.forgetServiceDispatcher(android.content.Context, android.content.ServiceConnection):android.app.IServiceConnection");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ServiceDispatcher {
        private final ArrayMap<ComponentName, ConnectionInfo> mActiveConnections;
        private final Executor mActivityExecutor;
        private final Handler mActivityThread;
        @UnsupportedAppUsage
        private final ServiceConnection mConnection;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        private final Context mContext;
        private final int mFlags;
        private boolean mForgotten;
        private final InnerConnection mIServiceConnection;
        private final ServiceConnectionLeaked mLocation;
        private RuntimeException mUnbindLocation;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class ConnectionInfo {
            IBinder binder;
            IBinder.DeathRecipient deathMonitor;

            private ConnectionInfo() {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class InnerConnection extends IServiceConnection.Stub {
            @UnsupportedAppUsage
            final WeakReference<ServiceDispatcher> mDispatcher;

            InnerConnection(ServiceDispatcher sd) {
                this.mDispatcher = new WeakReference<>(sd);
            }

            @Override // android.app.IServiceConnection
            public void connected(ComponentName name, IBinder service, boolean dead) throws RemoteException {
                ServiceDispatcher sd = this.mDispatcher.get();
                if (sd != null) {
                    sd.connected(name, service, dead);
                }
            }
        }

        @UnsupportedAppUsage
        ServiceDispatcher(ServiceConnection conn, Context context, Handler activityThread, int flags) {
            this.mActiveConnections = new ArrayMap<>();
            this.mIServiceConnection = new InnerConnection(this);
            this.mConnection = conn;
            this.mContext = context;
            this.mActivityThread = activityThread;
            this.mActivityExecutor = null;
            this.mLocation = new ServiceConnectionLeaked(null);
            this.mLocation.fillInStackTrace();
            this.mFlags = flags;
        }

        ServiceDispatcher(ServiceConnection conn, Context context, Executor activityExecutor, int flags) {
            this.mActiveConnections = new ArrayMap<>();
            this.mIServiceConnection = new InnerConnection(this);
            this.mConnection = conn;
            this.mContext = context;
            this.mActivityThread = null;
            this.mActivityExecutor = activityExecutor;
            this.mLocation = new ServiceConnectionLeaked(null);
            this.mLocation.fillInStackTrace();
            this.mFlags = flags;
        }

        void validate(Context context, Handler activityThread, Executor activityExecutor) {
            if (this.mContext != context) {
                throw new RuntimeException("ServiceConnection " + this.mConnection + " registered with differing Context (was " + this.mContext + " now " + context + ")");
            } else if (this.mActivityThread != activityThread) {
                throw new RuntimeException("ServiceConnection " + this.mConnection + " registered with differing handler (was " + this.mActivityThread + " now " + activityThread + ")");
            } else if (this.mActivityExecutor != activityExecutor) {
                throw new RuntimeException("ServiceConnection " + this.mConnection + " registered with differing executor (was " + this.mActivityExecutor + " now " + activityExecutor + ")");
            }
        }

        void doForget() {
            synchronized (this) {
                for (int i = 0; i < this.mActiveConnections.size(); i++) {
                    ConnectionInfo ci = this.mActiveConnections.valueAt(i);
                    ci.binder.unlinkToDeath(ci.deathMonitor, 0);
                }
                this.mActiveConnections.clear();
                this.mForgotten = true;
            }
        }

        ServiceConnectionLeaked getLocation() {
            return this.mLocation;
        }

        ServiceConnection getServiceConnection() {
            return this.mConnection;
        }

        @UnsupportedAppUsage
        IServiceConnection getIServiceConnection() {
            return this.mIServiceConnection;
        }

        int getFlags() {
            return this.mFlags;
        }

        void setUnbindLocation(RuntimeException ex) {
            this.mUnbindLocation = ex;
        }

        RuntimeException getUnbindLocation() {
            return this.mUnbindLocation;
        }

        public void connected(ComponentName name, IBinder service, boolean dead) {
            Executor executor = this.mActivityExecutor;
            if (executor != null) {
                executor.execute(new RunConnection(name, service, 0, dead));
                return;
            }
            Handler handler = this.mActivityThread;
            if (handler != null) {
                handler.post(new RunConnection(name, service, 0, dead));
            } else {
                doConnected(name, service, dead);
            }
        }

        public void death(ComponentName name, IBinder service) {
            Executor executor = this.mActivityExecutor;
            if (executor != null) {
                executor.execute(new RunConnection(name, service, 1, false));
                return;
            }
            Handler handler = this.mActivityThread;
            if (handler != null) {
                handler.post(new RunConnection(name, service, 1, false));
            } else {
                doDeath(name, service);
            }
        }

        public void doConnected(ComponentName name, IBinder service, boolean dead) {
            synchronized (this) {
                if (this.mForgotten) {
                    return;
                }
                ConnectionInfo old = this.mActiveConnections.get(name);
                if (old == null || old.binder != service) {
                    if (service != null) {
                        ConnectionInfo info = new ConnectionInfo();
                        info.binder = service;
                        info.deathMonitor = new DeathMonitor(name, service);
                        try {
                            service.linkToDeath(info.deathMonitor, 0);
                            this.mActiveConnections.put(name, info);
                        } catch (RemoteException e) {
                            this.mActiveConnections.remove(name);
                            return;
                        }
                    } else {
                        this.mActiveConnections.remove(name);
                    }
                    if (old != null) {
                        old.binder.unlinkToDeath(old.deathMonitor, 0);
                    }
                    if (old != null) {
                        this.mConnection.onServiceDisconnected(name);
                    }
                    if (dead) {
                        this.mConnection.onBindingDied(name);
                    }
                    if (service != null) {
                        this.mConnection.onServiceConnected(name, service);
                    } else {
                        this.mConnection.onNullBinding(name);
                    }
                }
            }
        }

        public void doDeath(ComponentName name, IBinder service) {
            synchronized (this) {
                ConnectionInfo old = this.mActiveConnections.get(name);
                if (old != null && old.binder == service) {
                    this.mActiveConnections.remove(name);
                    old.binder.unlinkToDeath(old.deathMonitor, 0);
                    this.mConnection.onServiceDisconnected(name);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public final class RunConnection implements Runnable {
            final int mCommand;
            final boolean mDead;
            final ComponentName mName;
            final IBinder mService;

            RunConnection(ComponentName name, IBinder service, int command, boolean dead) {
                this.mName = name;
                this.mService = service;
                this.mCommand = command;
                this.mDead = dead;
            }

            @Override // java.lang.Runnable
            public void run() {
                int i = this.mCommand;
                if (i == 0) {
                    ServiceDispatcher.this.doConnected(this.mName, this.mService, this.mDead);
                } else if (i == 1) {
                    ServiceDispatcher.this.doDeath(this.mName, this.mService);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public final class DeathMonitor implements IBinder.DeathRecipient {
            final ComponentName mName;
            final IBinder mService;

            DeathMonitor(ComponentName name, IBinder service) {
                this.mName = name;
                this.mService = service;
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                ServiceDispatcher.this.death(this.mName, this.mService);
            }
        }
    }
}
