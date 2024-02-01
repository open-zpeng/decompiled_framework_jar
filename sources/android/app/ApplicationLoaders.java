package android.app;

import android.os.Build;
import android.os.GraphicsEnvironment;
import android.os.Trace;
import android.util.ArrayMap;
import com.android.internal.os.ClassLoaderFactory;
import dalvik.system.PathClassLoader;
import java.util.Collection;
/* loaded from: classes.dex */
public class ApplicationLoaders {
    private static final ApplicationLoaders gApplicationLoaders = new ApplicationLoaders();
    public protected final ArrayMap<String, ClassLoader> mLoaders = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    public static ApplicationLoaders getDefault() {
        return gApplicationLoaders;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ClassLoader getClassLoader(String zip, int targetSdkVersion, boolean isBundled, String librarySearchPath, String libraryPermittedPath, ClassLoader parent, String classLoaderName) {
        return getClassLoader(zip, targetSdkVersion, isBundled, librarySearchPath, libraryPermittedPath, parent, zip, classLoaderName);
    }

    private synchronized ClassLoader getClassLoader(String zip, int targetSdkVersion, boolean isBundled, String librarySearchPath, String libraryPermittedPath, ClassLoader parent, String cacheKey, String classLoaderName) {
        ClassLoader baseParent = ClassLoader.getSystemClassLoader().getParent();
        synchronized (this.mLoaders) {
            ClassLoader parent2 = parent == null ? baseParent : parent;
            try {
                try {
                    if (parent2 != baseParent) {
                        Trace.traceBegin(64L, zip);
                        ClassLoader loader = ClassLoaderFactory.createClassLoader(zip, null, parent2, classLoaderName);
                        Trace.traceEnd(64L);
                        return loader;
                    }
                    try {
                        ClassLoader loader2 = this.mLoaders.get(cacheKey);
                        if (loader2 != null) {
                            return loader2;
                        }
                        Trace.traceBegin(64L, zip);
                        ClassLoader classloader = ClassLoaderFactory.createClassLoader(zip, librarySearchPath, libraryPermittedPath, parent2, targetSdkVersion, isBundled, classLoaderName);
                        Trace.traceEnd(64L);
                        Trace.traceBegin(64L, "setLayerPaths");
                        GraphicsEnvironment.getInstance().setLayerPaths(classloader, librarySearchPath, libraryPermittedPath);
                        Trace.traceEnd(64L);
                        this.mLoaders.put(cacheKey, classloader);
                        return classloader;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    public synchronized ClassLoader createAndCacheWebViewClassLoader(String packagePath, String libsPath, String cacheKey) {
        return getClassLoader(packagePath, Build.VERSION.SDK_INT, false, libsPath, null, null, cacheKey, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addPath(ClassLoader classLoader, String dexPath) {
        if (!(classLoader instanceof PathClassLoader)) {
            throw new IllegalStateException("class loader is not a PathClassLoader");
        }
        PathClassLoader baseDexClassLoader = (PathClassLoader) classLoader;
        baseDexClassLoader.addDexPath(dexPath);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addNative(ClassLoader classLoader, Collection<String> libPaths) {
        if (!(classLoader instanceof PathClassLoader)) {
            throw new IllegalStateException("class loader is not a PathClassLoader");
        }
        PathClassLoader baseDexClassLoader = (PathClassLoader) classLoader;
        baseDexClassLoader.addNativePath(libPaths);
    }
}
