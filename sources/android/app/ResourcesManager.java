package android.app;

import android.content.Context;
import android.content.pm.PackageParser;
import android.content.res.ApkAssets;
import android.content.res.AssetManager;
import android.content.res.CompatResources;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.ResourcesImpl;
import android.content.res.ResourcesKey;
import android.hardware.display.DisplayManagerGlobal;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.Trace;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.util.Pair;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayAdjustments;
import android.view.IWindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.IndentingPrintWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class ResourcesManager {
    private static final boolean DEBUG = false;
    private static final boolean ENABLE_APK_ASSETS_CACHE = false;
    static final String TAG = "ResourcesManager";
    private static ResourcesManager sResourcesManager;
    private CompatibilityInfo mResCompatibilityInfo;
    private static final Predicate<WeakReference<Resources>> sEmptyReferencePredicate = new Predicate() { // from class: android.app.-$$Lambda$ResourcesManager$QJ7UiVk_XS90KuXAsIjIEym1DnM
        @Override // java.util.function.Predicate
        public final boolean test(Object obj) {
            return ResourcesManager.lambda$static$0((WeakReference) obj);
        }
    };
    private static IWindowManager mWindowManager = null;
    private static Configuration mXuiConfiguration = null;
    public protected final Configuration mResConfiguration = new Configuration();
    public protected final ArrayMap<ResourcesKey, WeakReference<ResourcesImpl>> mResourceImpls = new ArrayMap<>();
    public protected final ArrayList<WeakReference<Resources>> mResourceReferences = new ArrayList<>();
    private final LruCache<ApkKey, ApkAssets> mLoadedApkAssets = null;
    private final ArrayMap<ApkKey, WeakReference<ApkAssets>> mCachedApkAssets = new ArrayMap<>();
    public protected final WeakHashMap<IBinder, ActivityResources> mActivityResourceReferences = new WeakHashMap<>();
    private final ArrayMap<Pair<Integer, DisplayAdjustments>, WeakReference<Display>> mAdjustedDisplays = new ArrayMap<>();

    private protected ResourcesManager() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$static$0(WeakReference weakRef) {
        return weakRef == null || weakRef.get() == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ApkKey {
        public final boolean overlay;
        public final String path;
        public final boolean sharedLib;

        synchronized ApkKey(String path, boolean sharedLib, boolean overlay) {
            this.path = path;
            this.sharedLib = sharedLib;
            this.overlay = overlay;
        }

        public int hashCode() {
            int result = (31 * 1) + this.path.hashCode();
            return (31 * ((31 * result) + Boolean.hashCode(this.sharedLib))) + Boolean.hashCode(this.overlay);
        }

        public boolean equals(Object obj) {
            if (obj instanceof ApkKey) {
                ApkKey other = (ApkKey) obj;
                return this.path.equals(other.path) && this.sharedLib == other.sharedLib && this.overlay == other.overlay;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ActivityResources {
        public final ArrayList<WeakReference<Resources>> activityResources;
        public final Configuration overrideConfig;

        public protected ActivityResources() {
            this.overrideConfig = new Configuration();
            this.activityResources = new ArrayList<>();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ResourcesManager getInstance() {
        ResourcesManager resourcesManager;
        IBinder b;
        synchronized (ResourcesManager.class) {
            if (sResourcesManager == null) {
                sResourcesManager = new ResourcesManager();
                if (mWindowManager == null && (b = ServiceManager.getService(Context.WINDOW_SERVICE)) != null) {
                    mWindowManager = IWindowManager.Stub.asInterface(b);
                }
            }
            resourcesManager = sResourcesManager;
        }
        return resourcesManager;
    }

    public synchronized void invalidatePath(String path) {
        int count = 0;
        int i = 0;
        while (i < this.mResourceImpls.size()) {
            ResourcesKey key = this.mResourceImpls.keyAt(i);
            if (key.isPathReferenced(path)) {
                cleanupResourceImpl(key);
                count++;
            } else {
                i++;
            }
        }
        Log.i(TAG, "Invalidated " + count + " asset managers that referenced " + path);
    }

    public synchronized Configuration getConfiguration() {
        if (mXuiConfiguration == null) {
            mXuiConfiguration = getXuiConfiguration();
        }
        if (mXuiConfiguration != null) {
            return mXuiConfiguration;
        }
        return this.mResConfiguration;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized DisplayMetrics getDisplayMetrics() {
        return getDisplayMetrics(0, DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS);
    }

    private Configuration getXuiConfiguration() {
        IBinder b;
        if (mXuiConfiguration == null) {
            try {
                if (mWindowManager == null && (b = ServiceManager.getService(Context.WINDOW_SERVICE)) != null) {
                    mWindowManager = IWindowManager.Stub.asInterface(b);
                }
                if (mWindowManager != null) {
                    mXuiConfiguration = mWindowManager.getXuiConfiguration(ActivityThread.currentPackageName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mXuiConfiguration;
    }

    @VisibleForTesting
    protected synchronized DisplayMetrics getDisplayMetrics(int displayId, DisplayAdjustments da) {
        DisplayMetrics dm = new DisplayMetrics();
        Display display = getAdjustedDisplay(displayId, da);
        if (display != null) {
            display.getMetrics(dm);
        } else {
            dm.setToDefaults();
        }
        if (mXuiConfiguration == null) {
            mXuiConfiguration = getXuiConfiguration();
        }
        if (mXuiConfiguration != null) {
            int i = mXuiConfiguration.densityDpi;
            dm.noncompatDensityDpi = i;
            dm.densityDpi = i;
            float f = mXuiConfiguration.densityDpi * 0.00625f;
            dm.noncompatDensity = f;
            dm.density = f;
            float f2 = dm.density;
            dm.noncompatScaledDensity = f2;
            dm.scaledDensity = f2;
            int i2 = mXuiConfiguration.screenWidthDp;
            dm.widthPixels = i2;
            dm.noncompatWidthPixels = i2;
            int i3 = mXuiConfiguration.screenHeightDp;
            dm.heightPixels = i3;
            dm.noncompatHeightPixels = i3;
        }
        return dm;
    }

    private static synchronized void applyNonDefaultDisplayMetricsToConfiguration(DisplayMetrics dm, Configuration config) {
        config.touchscreen = 1;
        config.densityDpi = dm.densityDpi;
        config.screenWidthDp = (int) (dm.widthPixels / dm.density);
        config.screenHeightDp = (int) (dm.heightPixels / dm.density);
        int sl = Configuration.resetScreenLayout(config.screenLayout);
        if (dm.widthPixels > dm.heightPixels) {
            config.orientation = 2;
            config.screenLayout = Configuration.reduceScreenLayout(sl, config.screenWidthDp, config.screenHeightDp);
        } else {
            config.orientation = 1;
            config.screenLayout = Configuration.reduceScreenLayout(sl, config.screenHeightDp, config.screenWidthDp);
        }
        config.smallestScreenWidthDp = config.screenWidthDp;
        config.compatScreenWidthDp = config.screenWidthDp;
        config.compatScreenHeightDp = config.screenHeightDp;
        config.compatSmallestScreenWidthDp = config.smallestScreenWidthDp;
    }

    public synchronized boolean applyCompatConfigurationLocked(int displayDensity, Configuration compatConfiguration) {
        if (this.mResCompatibilityInfo != null && !this.mResCompatibilityInfo.supportsScreen()) {
            this.mResCompatibilityInfo.applyToConfiguration(displayDensity, compatConfiguration);
            return true;
        }
        return false;
    }

    private synchronized Display getAdjustedDisplay(int displayId, DisplayAdjustments displayAdjustments) {
        Display display;
        DisplayAdjustments displayAdjustmentsCopy = displayAdjustments != null ? new DisplayAdjustments(displayAdjustments) : new DisplayAdjustments();
        Pair<Integer, DisplayAdjustments> key = Pair.create(Integer.valueOf(displayId), displayAdjustmentsCopy);
        synchronized (this) {
            WeakReference<Display> wd = this.mAdjustedDisplays.get(key);
            if (wd == null || (display = wd.get()) == null) {
                DisplayManagerGlobal dm = DisplayManagerGlobal.getInstance();
                if (dm == null) {
                    return null;
                }
                Display display2 = dm.getCompatibleDisplay(displayId, key.second);
                if (display2 != null) {
                    this.mAdjustedDisplays.put(key, new WeakReference<>(display2));
                }
                return display2;
            }
            return display;
        }
    }

    public synchronized Display getAdjustedDisplay(int displayId, Resources resources) {
        DisplayManagerGlobal dm = DisplayManagerGlobal.getInstance();
        if (dm == null) {
            return null;
        }
        return dm.getCompatibleDisplay(displayId, resources);
    }

    private synchronized void cleanupResourceImpl(ResourcesKey removedKey) {
        ResourcesImpl res = this.mResourceImpls.remove(removedKey).get();
        if (res != null) {
            res.flushLayoutCache();
        }
    }

    private static synchronized String overlayPathToIdmapPath(String path) {
        return "/data/resource-cache/" + path.substring(1).replace('/', '@') + "@idmap";
    }

    private synchronized ApkAssets loadApkAssets(String path, boolean sharedLib, boolean overlay) throws IOException {
        ApkAssets apkAssets;
        ApkKey newKey = new ApkKey(path, sharedLib, overlay);
        if (this.mLoadedApkAssets != null) {
            ApkAssets apkAssets2 = this.mLoadedApkAssets.get(newKey);
            ApkAssets apkAssets3 = apkAssets2;
            if (apkAssets3 != null) {
                return apkAssets3;
            }
        }
        WeakReference<ApkAssets> apkAssetsRef = this.mCachedApkAssets.get(newKey);
        if (apkAssetsRef != null) {
            ApkAssets apkAssets4 = apkAssetsRef.get();
            ApkAssets apkAssets5 = apkAssets4;
            if (apkAssets5 != null) {
                if (this.mLoadedApkAssets != null) {
                    this.mLoadedApkAssets.put(newKey, apkAssets5);
                }
                return apkAssets5;
            }
            this.mCachedApkAssets.remove(newKey);
        }
        if (overlay) {
            apkAssets = ApkAssets.loadOverlayFromPath(overlayPathToIdmapPath(path), false);
        } else {
            apkAssets = ApkAssets.loadFromPath(path, false, sharedLib);
        }
        if (this.mLoadedApkAssets != null) {
            this.mLoadedApkAssets.put(newKey, apkAssets);
        }
        this.mCachedApkAssets.put(newKey, new WeakReference<>(apkAssets));
        return apkAssets;
    }

    @VisibleForTesting
    public private AssetManager createAssetManager(ResourcesKey key) {
        String[] strArr;
        String[] strArr2;
        String[] strArr3;
        AssetManager.Builder builder = new AssetManager.Builder();
        if (key.mResDir != null) {
            try {
                builder.addApkAssets(loadApkAssets(key.mResDir, false, false));
            } catch (IOException e) {
                Log.e(TAG, "failed to add asset path " + key.mResDir);
                return null;
            }
        }
        if (key.mSplitResDirs != null) {
            for (String splitResDir : key.mSplitResDirs) {
                try {
                    builder.addApkAssets(loadApkAssets(splitResDir, false, false));
                } catch (IOException e2) {
                    Log.e(TAG, "failed to add split asset path " + splitResDir);
                    return null;
                }
            }
        }
        if (key.mOverlayDirs != null) {
            for (String idmapPath : key.mOverlayDirs) {
                try {
                    builder.addApkAssets(loadApkAssets(idmapPath, false, true));
                } catch (IOException e3) {
                    Log.w(TAG, "failed to add overlay path " + idmapPath);
                }
            }
        }
        if (key.mLibDirs != null) {
            for (String libDir : key.mLibDirs) {
                if (libDir.endsWith(PackageParser.APK_FILE_EXTENSION)) {
                    try {
                        builder.addApkAssets(loadApkAssets(libDir, true, false));
                    } catch (IOException e4) {
                        Log.w(TAG, "Asset path '" + libDir + "' does not exist or contains no resources.");
                    }
                }
            }
        }
        return builder.build();
    }

    private static synchronized <T> int countLiveReferences(Collection<WeakReference<T>> collection) {
        int count = 0;
        Iterator<WeakReference<T>> it = collection.iterator();
        while (it.hasNext()) {
            WeakReference<T> ref = it.next();
            T value = ref != null ? ref.get() : null;
            if (value != null) {
                count++;
            }
        }
        return count;
    }

    public synchronized void dump(String prefix, PrintWriter printWriter) {
        IndentingPrintWriter pw = new IndentingPrintWriter(printWriter, "  ");
        for (int i = 0; i < prefix.length() / 2; i++) {
            pw.increaseIndent();
        }
        pw.println("ResourcesManager:");
        pw.increaseIndent();
        if (this.mLoadedApkAssets != null) {
            pw.print("cached apks: total=");
            pw.print(this.mLoadedApkAssets.size());
            pw.print(" created=");
            pw.print(this.mLoadedApkAssets.createCount());
            pw.print(" evicted=");
            pw.print(this.mLoadedApkAssets.evictionCount());
            pw.print(" hit=");
            pw.print(this.mLoadedApkAssets.hitCount());
            pw.print(" miss=");
            pw.print(this.mLoadedApkAssets.missCount());
            pw.print(" max=");
            pw.print(this.mLoadedApkAssets.maxSize());
        } else {
            pw.print("cached apks: 0 [cache disabled]");
        }
        pw.println();
        pw.print("total apks: ");
        pw.println(countLiveReferences(this.mCachedApkAssets.values()));
        pw.print("resources: ");
        int references = countLiveReferences(this.mResourceReferences);
        for (ActivityResources activityResources : this.mActivityResourceReferences.values()) {
            references += countLiveReferences(activityResources.activityResources);
        }
        pw.println(references);
        pw.print("resource impls: ");
        pw.println(countLiveReferences(this.mResourceImpls.values()));
    }

    private synchronized Configuration generateConfig(ResourcesKey key, DisplayMetrics dm) {
        boolean isDefaultDisplay = key.mDisplayId == 0;
        boolean hasOverrideConfig = key.hasOverrideConfiguration();
        if (!isDefaultDisplay || hasOverrideConfig) {
            Configuration config = new Configuration(getConfiguration());
            if (!isDefaultDisplay) {
                applyNonDefaultDisplayMetricsToConfiguration(dm, config);
            }
            if (hasOverrideConfig) {
                config.updateFrom(key.mOverrideConfiguration);
                return config;
            }
            return config;
        }
        return getConfiguration();
    }

    private synchronized ResourcesImpl createResourcesImpl(ResourcesKey key) {
        DisplayAdjustments daj = new DisplayAdjustments(key.mOverrideConfiguration);
        daj.setCompatibilityInfo(key.mCompatInfo);
        AssetManager assets = createAssetManager(key);
        if (assets == null) {
            return null;
        }
        DisplayMetrics dm = getDisplayMetrics(key.mDisplayId, daj);
        Configuration config = generateConfig(key, dm);
        ResourcesImpl impl = new ResourcesImpl(assets, dm, config, daj);
        return impl;
    }

    private synchronized ResourcesImpl findResourcesImplForKeyLocked(ResourcesKey key) {
        WeakReference<ResourcesImpl> weakImplRef = this.mResourceImpls.get(key);
        ResourcesImpl impl = weakImplRef != null ? weakImplRef.get() : null;
        if (impl == null || !impl.getAssets().isUpToDate()) {
            return null;
        }
        return impl;
    }

    private synchronized ResourcesImpl findOrCreateResourcesImplForKeyLocked(ResourcesKey key) {
        ResourcesImpl impl = findResourcesImplForKeyLocked(key);
        if (impl == null && (impl = createResourcesImpl(key)) != null) {
            this.mResourceImpls.put(key, new WeakReference<>(impl));
        }
        return impl;
    }

    private synchronized ResourcesKey findKeyForResourceImplLocked(ResourcesImpl resourceImpl) {
        int refCount = this.mResourceImpls.size();
        int i = 0;
        while (true) {
            if (i >= refCount) {
                return null;
            }
            WeakReference<ResourcesImpl> weakImplRef = this.mResourceImpls.valueAt(i);
            ResourcesImpl impl = weakImplRef != null ? weakImplRef.get() : null;
            if (impl == null || resourceImpl != impl) {
                i++;
            } else {
                return this.mResourceImpls.keyAt(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isSameResourcesOverrideConfig(IBinder activityToken, Configuration overrideConfig) {
        ActivityResources activityResources;
        if (activityToken == null) {
            activityResources = null;
        } else {
            try {
                activityResources = this.mActivityResourceReferences.get(activityToken);
            } finally {
            }
        }
        if (activityResources == null) {
            return overrideConfig == null;
        }
        if (!Objects.equals(activityResources.overrideConfig, overrideConfig) && (overrideConfig == null || activityResources.overrideConfig == null || overrideConfig.diffPublicOnly(activityResources.overrideConfig) != 0)) {
            return r1;
        }
        r1 = true;
        return r1;
    }

    private synchronized ActivityResources getOrCreateActivityResourcesStructLocked(IBinder activityToken) {
        ActivityResources activityResources = this.mActivityResourceReferences.get(activityToken);
        if (activityResources == null) {
            ActivityResources activityResources2 = new ActivityResources();
            this.mActivityResourceReferences.put(activityToken, activityResources2);
            return activityResources2;
        }
        return activityResources;
    }

    private synchronized Resources getOrCreateResourcesForActivityLocked(IBinder activityToken, ClassLoader classLoader, ResourcesImpl impl, CompatibilityInfo compatInfo) {
        ActivityResources activityResources = getOrCreateActivityResourcesStructLocked(activityToken);
        int refCount = activityResources.activityResources.size();
        for (int i = 0; i < refCount; i++) {
            WeakReference<Resources> weakResourceRef = activityResources.activityResources.get(i);
            Resources resources = weakResourceRef.get();
            if (resources != null && Objects.equals(resources.getClassLoader(), classLoader) && resources.getImpl() == impl) {
                return resources;
            }
        }
        Resources resources2 = compatInfo.needsCompatResources() ? new CompatResources(classLoader) : new Resources(classLoader);
        resources2.setImpl(impl);
        activityResources.activityResources.add(new WeakReference<>(resources2));
        return resources2;
    }

    private synchronized Resources getOrCreateResourcesLocked(ClassLoader classLoader, ResourcesImpl impl, CompatibilityInfo compatInfo) {
        int refCount = this.mResourceReferences.size();
        for (int i = 0; i < refCount; i++) {
            WeakReference<Resources> weakResourceRef = this.mResourceReferences.get(i);
            Resources resources = weakResourceRef.get();
            if (resources != null && Objects.equals(resources.getClassLoader(), classLoader) && resources.getImpl() == impl) {
                return resources;
            }
        }
        Resources resources2 = compatInfo.needsCompatResources() ? new CompatResources(classLoader) : new Resources(classLoader);
        resources2.setImpl(impl);
        this.mResourceReferences.add(new WeakReference<>(resources2));
        return resources2;
    }

    public synchronized Resources createBaseActivityResources(IBinder activityToken, String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, Configuration overrideConfig, CompatibilityInfo compatInfo, ClassLoader classLoader) {
        try {
            Trace.traceBegin(8192L, "ResourcesManager#createBaseActivityResources");
            ResourcesKey key = new ResourcesKey(resDir, splitResDirs, overlayDirs, libDirs, displayId, overrideConfig != null ? new Configuration(overrideConfig) : null, compatInfo);
            ClassLoader classLoader2 = classLoader != null ? classLoader : ClassLoader.getSystemClassLoader();
            try {
                synchronized (this) {
                    try {
                        getOrCreateActivityResourcesStructLocked(activityToken);
                    } catch (Throwable th) {
                        th = th;
                        Trace.traceEnd(8192L);
                        throw th;
                    }
                }
                updateResourcesForActivity(activityToken, overrideConfig, displayId, false);
                Resources orCreateResources = getOrCreateResources(activityToken, key, classLoader2);
                Trace.traceEnd(8192L);
                return orCreateResources;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private synchronized Resources getOrCreateResources(IBinder activityToken, ResourcesKey key, ClassLoader classLoader) {
        Resources resources;
        try {
            if (activityToken != null) {
                ActivityResources activityResources = getOrCreateActivityResourcesStructLocked(activityToken);
                ArrayUtils.unstableRemoveIf(activityResources.activityResources, sEmptyReferencePredicate);
                if (key.hasOverrideConfiguration() && !activityResources.overrideConfig.equals(Configuration.EMPTY)) {
                    Configuration temp = new Configuration(activityResources.overrideConfig);
                    temp.updateFrom(key.mOverrideConfiguration);
                    if (getXuiConfiguration() != null) {
                        temp.updateFrom(getConfiguration());
                    }
                    key.mOverrideConfiguration.setTo(temp);
                }
                ResourcesImpl resourcesImpl = findResourcesImplForKeyLocked(key);
                if (resourcesImpl != null) {
                    return getOrCreateResourcesForActivityLocked(activityToken, classLoader, resourcesImpl, key.mCompatInfo);
                }
            } else {
                ArrayUtils.unstableRemoveIf(this.mResourceReferences, sEmptyReferencePredicate);
                ResourcesImpl resourcesImpl2 = findResourcesImplForKeyLocked(key);
                if (resourcesImpl2 != null) {
                    return getOrCreateResourcesLocked(classLoader, resourcesImpl2, key.mCompatInfo);
                }
            }
            ResourcesImpl resourcesImpl3 = createResourcesImpl(key);
            if (resourcesImpl3 == null) {
                return null;
            }
            this.mResourceImpls.put(key, new WeakReference<>(resourcesImpl3));
            if (activityToken != null) {
                resources = getOrCreateResourcesForActivityLocked(activityToken, classLoader, resourcesImpl3, key.mCompatInfo);
            } else {
                resources = getOrCreateResourcesLocked(classLoader, resourcesImpl3, key.mCompatInfo);
            }
            return resources;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized Resources getResources(IBinder activityToken, String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, Configuration overrideConfig, CompatibilityInfo compatInfo, ClassLoader classLoader) {
        try {
            Trace.traceBegin(8192L, "ResourcesManager#getResources");
            ResourcesKey key = new ResourcesKey(resDir, splitResDirs, overlayDirs, libDirs, displayId, overrideConfig != null ? new Configuration(overrideConfig) : null, compatInfo);
            try {
                Resources orCreateResources = getOrCreateResources(activityToken, key, classLoader != null ? classLoader : ClassLoader.getSystemClassLoader());
                Trace.traceEnd(8192L);
                return orCreateResources;
            } catch (Throwable th) {
                th = th;
                Trace.traceEnd(8192L);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public synchronized void updateResourcesForActivity(IBinder activityToken, Configuration overrideConfig, int displayId, boolean movedToDifferentDisplay) {
        ActivityResources activityResources;
        try {
            Trace.traceBegin(8192L, "ResourcesManager#updateResourcesForActivity");
            synchronized (this) {
                ActivityResources activityResources2 = getOrCreateActivityResourcesStructLocked(activityToken);
                if (!Objects.equals(activityResources2.overrideConfig, overrideConfig) || movedToDifferentDisplay) {
                    Configuration oldConfig = new Configuration(activityResources2.overrideConfig);
                    if (overrideConfig != null) {
                        activityResources2.overrideConfig.setTo(overrideConfig);
                    } else {
                        activityResources2.overrideConfig.unset();
                    }
                    boolean activityHasOverrideConfig = !activityResources2.overrideConfig.equals(Configuration.EMPTY);
                    int refCount = activityResources2.activityResources.size();
                    int i = 0;
                    while (i < refCount) {
                        WeakReference<Resources> weakResRef = activityResources2.activityResources.get(i);
                        Resources resources = weakResRef.get();
                        if (resources != null) {
                            ResourcesKey oldKey = findKeyForResourceImplLocked(resources.getImpl());
                            if (oldKey == null) {
                                Slog.e(TAG, "can't find ResourcesKey for resources impl=" + resources.getImpl());
                            } else {
                                Configuration rebasedOverrideConfig = new Configuration();
                                if (overrideConfig != null) {
                                    rebasedOverrideConfig.setTo(overrideConfig);
                                }
                                if (activityHasOverrideConfig && oldKey.hasOverrideConfiguration()) {
                                    Configuration overrideOverrideConfig = Configuration.generateDelta(oldConfig, oldKey.mOverrideConfiguration);
                                    rebasedOverrideConfig.updateFrom(overrideOverrideConfig);
                                }
                                activityResources = activityResources2;
                                ResourcesKey newKey = new ResourcesKey(oldKey.mResDir, oldKey.mSplitResDirs, oldKey.mOverlayDirs, oldKey.mLibDirs, displayId, rebasedOverrideConfig, oldKey.mCompatInfo);
                                ResourcesImpl resourcesImpl = findResourcesImplForKeyLocked(newKey);
                                if (resourcesImpl == null && (resourcesImpl = createResourcesImpl(newKey)) != null) {
                                    this.mResourceImpls.put(newKey, new WeakReference<>(resourcesImpl));
                                }
                                if (resourcesImpl != null && resourcesImpl != resources.getImpl()) {
                                    resources.setImpl(resourcesImpl);
                                }
                                i++;
                                activityResources2 = activityResources;
                            }
                        }
                        activityResources = activityResources2;
                        i++;
                        activityResources2 = activityResources;
                    }
                    return;
                }
                Trace.traceEnd(8192L);
            }
        } finally {
            Trace.traceEnd(8192L);
        }
    }

    public final synchronized boolean applyConfigurationToResourcesLocked(Configuration config, CompatibilityInfo compat) {
        try {
            Trace.traceBegin(8192L, "ResourcesManager#applyConfigurationToResourcesLocked");
            boolean z = false;
            if (!this.mResConfiguration.isOtherSeqNewer(config) && compat == null) {
                Trace.traceEnd(8192L);
                return false;
            }
            int changes = this.mResConfiguration.updateFrom(config);
            this.mAdjustedDisplays.clear();
            DisplayMetrics defaultDisplayMetrics = getDisplayMetrics();
            if (compat != null && (this.mResCompatibilityInfo == null || !this.mResCompatibilityInfo.equals(compat))) {
                this.mResCompatibilityInfo = compat;
                changes |= 3328;
            }
            Resources.updateSystemConfiguration(config, defaultDisplayMetrics, compat);
            ApplicationPackageManager.configurationChanged();
            Configuration tmpConfig = null;
            boolean z2 = true;
            int i = this.mResourceImpls.size() - 1;
            while (i >= 0) {
                ResourcesKey key = this.mResourceImpls.keyAt(i);
                WeakReference<ResourcesImpl> weakImplRef = this.mResourceImpls.valueAt(i);
                ResourcesImpl r = weakImplRef != null ? weakImplRef.get() : null;
                if (r != null) {
                    int displayId = key.mDisplayId;
                    boolean isDefaultDisplay = displayId == 0 ? z2 : z;
                    boolean hasOverrideConfiguration = key.hasOverrideConfiguration();
                    if (isDefaultDisplay && !hasOverrideConfiguration) {
                        r.updateConfiguration(config, defaultDisplayMetrics, compat);
                    }
                    if (tmpConfig == null) {
                        tmpConfig = new Configuration();
                    }
                    tmpConfig.setTo(config);
                    DisplayAdjustments daj = r.getDisplayAdjustments();
                    if (compat != null) {
                        daj = new DisplayAdjustments(daj);
                        daj.setCompatibilityInfo(compat);
                    }
                    DisplayMetrics dm = getDisplayMetrics(displayId, daj);
                    if (!isDefaultDisplay) {
                        applyNonDefaultDisplayMetricsToConfiguration(dm, tmpConfig);
                    }
                    if (hasOverrideConfiguration) {
                        tmpConfig.updateFrom(key.mOverrideConfiguration);
                    }
                    r.updateConfiguration(tmpConfig, dm, compat);
                } else {
                    this.mResourceImpls.removeAt(i);
                }
                i--;
                z = false;
                z2 = true;
            }
            return changes != 0;
        } finally {
            Trace.traceEnd(8192L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appendLibAssetForMainAssetPath(String assetPath, String libAsset) {
        int implCount;
        String str = libAsset;
        synchronized (this) {
            try {
                try {
                    ArrayMap<ResourcesImpl, ResourcesKey> updatedResourceKeys = new ArrayMap<>();
                    int implCount2 = this.mResourceImpls.size();
                    int i = 0;
                    int i2 = 0;
                    while (i2 < implCount2) {
                        ResourcesKey key = this.mResourceImpls.keyAt(i2);
                        WeakReference<ResourcesImpl> weakImplRef = this.mResourceImpls.valueAt(i2);
                        ResourcesImpl impl = weakImplRef != null ? weakImplRef.get() : null;
                        if (impl != null && Objects.equals(key.mResDir, assetPath) && !ArrayUtils.contains(key.mLibDirs, str)) {
                            int newLibAssetCount = 1 + (key.mLibDirs != null ? key.mLibDirs.length : i);
                            String[] newLibAssets = new String[newLibAssetCount];
                            if (key.mLibDirs != null) {
                                System.arraycopy(key.mLibDirs, i, newLibAssets, i, key.mLibDirs.length);
                            }
                            newLibAssets[newLibAssetCount - 1] = str;
                            implCount = implCount2;
                            updatedResourceKeys.put(impl, new ResourcesKey(key.mResDir, key.mSplitResDirs, key.mOverlayDirs, newLibAssets, key.mDisplayId, key.mOverrideConfiguration, key.mCompatInfo));
                            i2++;
                            implCount2 = implCount;
                            str = libAsset;
                            i = 0;
                        }
                        implCount = implCount2;
                        i2++;
                        implCount2 = implCount;
                        str = libAsset;
                        i = 0;
                    }
                    redirectResourcesToNewImplLocked(updatedResourceKeys);
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void applyNewResourceDirsLocked(String baseCodePath, String[] newResourceDirs) {
        try {
            Trace.traceBegin(8192L, "ResourcesManager#applyNewResourceDirsLocked");
            ArrayMap<ResourcesImpl, ResourcesKey> updatedResourceKeys = new ArrayMap<>();
            int implCount = this.mResourceImpls.size();
            for (int i = 0; i < implCount; i++) {
                ResourcesKey key = this.mResourceImpls.keyAt(i);
                WeakReference<ResourcesImpl> weakImplRef = this.mResourceImpls.valueAt(i);
                ResourcesImpl impl = weakImplRef != null ? weakImplRef.get() : null;
                if (impl != null) {
                    if (key.mResDir != null) {
                        try {
                            if (key.mResDir.equals(baseCodePath)) {
                            }
                        } catch (Throwable th) {
                            th = th;
                            Trace.traceEnd(8192L);
                            throw th;
                        }
                    }
                    updatedResourceKeys.put(impl, new ResourcesKey(key.mResDir, key.mSplitResDirs, newResourceDirs, key.mLibDirs, key.mDisplayId, key.mOverrideConfiguration, key.mCompatInfo));
                }
            }
            redirectResourcesToNewImplLocked(updatedResourceKeys);
            Trace.traceEnd(8192L);
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private synchronized void redirectResourcesToNewImplLocked(ArrayMap<ResourcesImpl, ResourcesKey> updatedResourceKeys) {
        ResourcesKey key;
        ResourcesKey key2;
        if (updatedResourceKeys.isEmpty()) {
            return;
        }
        int resourcesCount = this.mResourceReferences.size();
        int i = 0;
        while (true) {
            if (i < resourcesCount) {
                WeakReference<Resources> ref = this.mResourceReferences.get(i);
                Resources r = ref != null ? ref.get() : null;
                if (r != null && (key2 = updatedResourceKeys.get(r.getImpl())) != null) {
                    ResourcesImpl impl = findOrCreateResourcesImplForKeyLocked(key2);
                    if (impl == null) {
                        throw new Resources.NotFoundException("failed to redirect ResourcesImpl");
                    }
                    r.setImpl(impl);
                }
                i++;
            } else {
                for (ActivityResources activityResources : this.mActivityResourceReferences.values()) {
                    int resCount = activityResources.activityResources.size();
                    for (int i2 = 0; i2 < resCount; i2++) {
                        WeakReference<Resources> ref2 = activityResources.activityResources.get(i2);
                        Resources r2 = ref2 != null ? ref2.get() : null;
                        if (r2 != null && (key = updatedResourceKeys.get(r2.getImpl())) != null) {
                            ResourcesImpl impl2 = findOrCreateResourcesImplForKeyLocked(key);
                            if (impl2 == null) {
                                throw new Resources.NotFoundException("failed to redirect ResourcesImpl");
                            }
                            r2.setImpl(impl2);
                        }
                    }
                }
                return;
            }
        }
    }
}
