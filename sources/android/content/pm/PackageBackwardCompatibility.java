package android.content.pm;

import android.content.pm.PackageBackwardCompatibility;
import android.content.pm.PackageParser;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
@VisibleForTesting
/* loaded from: classes.dex */
public class PackageBackwardCompatibility extends PackageSharedLibraryUpdater {
    private static final PackageBackwardCompatibility INSTANCE;
    private static final String TAG = PackageBackwardCompatibility.class.getSimpleName();
    private final boolean mBootClassPathContainsATB;
    private final boolean mBootClassPathContainsOAHL;
    private final PackageSharedLibraryUpdater[] mPackageUpdaters;

    static {
        List<PackageSharedLibraryUpdater> packageUpdaters = new ArrayList<>();
        boolean bootClassPathContainsOAHL = !addOptionalUpdater(packageUpdaters, "android.content.pm.OrgApacheHttpLegacyUpdater", new Supplier() { // from class: android.content.pm.-$$Lambda$FMztmpMwSp3D3ge8Zxr31di8ZBg
            @Override // java.util.function.Supplier
            public final Object get() {
                return new PackageBackwardCompatibility.RemoveUnnecessaryOrgApacheHttpLegacyLibrary();
            }
        });
        packageUpdaters.add(new AndroidTestRunnerSplitUpdater());
        boolean bootClassPathContainsATB = !addOptionalUpdater(packageUpdaters, "android.content.pm.AndroidTestBaseUpdater", new Supplier() { // from class: android.content.pm.-$$Lambda$jpya2qgMDDEok2GAoKRDqPM5lIE
            @Override // java.util.function.Supplier
            public final Object get() {
                return new PackageBackwardCompatibility.RemoveUnnecessaryAndroidTestBaseLibrary();
            }
        });
        PackageSharedLibraryUpdater[] updaterArray = (PackageSharedLibraryUpdater[]) packageUpdaters.toArray(new PackageSharedLibraryUpdater[0]);
        INSTANCE = new PackageBackwardCompatibility(bootClassPathContainsOAHL, bootClassPathContainsATB, updaterArray);
    }

    private static synchronized boolean addOptionalUpdater(List<PackageSharedLibraryUpdater> packageUpdaters, String className, Supplier<PackageSharedLibraryUpdater> defaultUpdater) {
        Class cls;
        PackageSharedLibraryUpdater updater;
        try {
            cls = PackageBackwardCompatibility.class.getClassLoader().loadClass(className).asSubclass(PackageSharedLibraryUpdater.class);
            String str = TAG;
            Log.i(str, "Loaded " + className);
        } catch (ClassNotFoundException e) {
            String str2 = TAG;
            Log.i(str2, "Could not find " + className + ", ignoring");
            cls = null;
        }
        boolean usedOptional = false;
        if (cls == null) {
            updater = defaultUpdater.get();
        } else {
            try {
                updater = (PackageSharedLibraryUpdater) cls.getConstructor(new Class[0]).newInstance(new Object[0]);
                usedOptional = true;
            } catch (ReflectiveOperationException e2) {
                throw new IllegalStateException("Could not create instance of " + className, e2);
            }
        }
        packageUpdaters.add(updater);
        return usedOptional;
    }

    @VisibleForTesting
    public static synchronized PackageSharedLibraryUpdater getInstance() {
        return INSTANCE;
    }

    public synchronized PackageBackwardCompatibility(boolean bootClassPathContainsOAHL, boolean bootClassPathContainsATB, PackageSharedLibraryUpdater[] packageUpdaters) {
        this.mBootClassPathContainsOAHL = bootClassPathContainsOAHL;
        this.mBootClassPathContainsATB = bootClassPathContainsATB;
        this.mPackageUpdaters = packageUpdaters;
    }

    @VisibleForTesting
    public static synchronized void modifySharedLibraries(PackageParser.Package pkg) {
        INSTANCE.updatePackage(pkg);
    }

    @Override // android.content.pm.PackageSharedLibraryUpdater
    public synchronized void updatePackage(PackageParser.Package pkg) {
        PackageSharedLibraryUpdater[] packageSharedLibraryUpdaterArr;
        for (PackageSharedLibraryUpdater packageUpdater : this.mPackageUpdaters) {
            packageUpdater.updatePackage(pkg);
        }
    }

    @VisibleForTesting
    public static synchronized boolean bootClassPathContainsOAHL() {
        return INSTANCE.mBootClassPathContainsOAHL;
    }

    @VisibleForTesting
    public static synchronized boolean bootClassPathContainsATB() {
        return INSTANCE.mBootClassPathContainsATB;
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class AndroidTestRunnerSplitUpdater extends PackageSharedLibraryUpdater {
        @Override // android.content.pm.PackageSharedLibraryUpdater
        public synchronized void updatePackage(PackageParser.Package pkg) {
            prefixImplicitDependency(pkg, "android.test.runner", "android.test.mock");
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class RemoveUnnecessaryOrgApacheHttpLegacyLibrary extends PackageSharedLibraryUpdater {
        @Override // android.content.pm.PackageSharedLibraryUpdater
        public synchronized void updatePackage(PackageParser.Package pkg) {
            removeLibrary(pkg, "org.apache.http.legacy");
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class RemoveUnnecessaryAndroidTestBaseLibrary extends PackageSharedLibraryUpdater {
        @Override // android.content.pm.PackageSharedLibraryUpdater
        public synchronized void updatePackage(PackageParser.Package pkg) {
            removeLibrary(pkg, "android.test.base");
        }
    }
}
