package com.android.internal.os;

import android.os.Trace;
import android.provider.SettingsStringUtil;
import dalvik.system.DelegateLastClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
/* loaded from: classes3.dex */
public class ClassLoaderFactory {
    private static final String PATH_CLASS_LOADER_NAME = PathClassLoader.class.getName();
    private static final String DEX_CLASS_LOADER_NAME = DexClassLoader.class.getName();
    private static final String DELEGATE_LAST_CLASS_LOADER_NAME = DelegateLastClassLoader.class.getName();

    private static native String createClassloaderNamespace(ClassLoader classLoader, int i, String str, String str2, boolean z, boolean z2);

    private ClassLoaderFactory() {
    }

    public static boolean isValidClassLoaderName(String name) {
        return name != null && (isPathClassLoaderName(name) || isDelegateLastClassLoaderName(name));
    }

    public static boolean isPathClassLoaderName(String name) {
        return name == null || PATH_CLASS_LOADER_NAME.equals(name) || DEX_CLASS_LOADER_NAME.equals(name);
    }

    public static boolean isDelegateLastClassLoaderName(String name) {
        return DELEGATE_LAST_CLASS_LOADER_NAME.equals(name);
    }

    public static ClassLoader createClassLoader(String dexPath, String librarySearchPath, ClassLoader parent, String classloaderName) {
        if (isPathClassLoaderName(classloaderName)) {
            return new PathClassLoader(dexPath, librarySearchPath, parent);
        }
        if (isDelegateLastClassLoaderName(classloaderName)) {
            return new DelegateLastClassLoader(dexPath, librarySearchPath, parent);
        }
        throw new AssertionError("Invalid classLoaderName: " + classloaderName);
    }

    public static ClassLoader createClassLoader(String dexPath, String librarySearchPath, String libraryPermittedPath, ClassLoader parent, int targetSdkVersion, boolean isNamespaceShared, String classloaderName) {
        ClassLoader classLoader = createClassLoader(dexPath, librarySearchPath, parent, classloaderName);
        boolean isForVendor = false;
        String[] split = dexPath.split(SettingsStringUtil.DELIMITER);
        int length = split.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String path = split[i];
            if (!path.startsWith("/vendor/")) {
                i++;
            } else {
                isForVendor = true;
                break;
            }
        }
        Trace.traceBegin(64L, "createClassloaderNamespace");
        String errorMessage = createClassloaderNamespace(classLoader, targetSdkVersion, librarySearchPath, libraryPermittedPath, isNamespaceShared, isForVendor);
        Trace.traceEnd(64L);
        if (errorMessage != null) {
            throw new UnsatisfiedLinkError("Unable to create namespace for the classloader " + classLoader + ": " + errorMessage);
        }
        return classLoader;
    }
}
