package android.content.pm;

import android.content.pm.PackageParser;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;
@VisibleForTesting
/* loaded from: classes.dex */
public abstract class PackageSharedLibraryUpdater {
    public abstract synchronized void updatePackage(PackageParser.Package r1);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void removeLibrary(PackageParser.Package pkg, String libraryName) {
        pkg.usesLibraries = ArrayUtils.remove(pkg.usesLibraries, libraryName);
        pkg.usesOptionalLibraries = ArrayUtils.remove(pkg.usesOptionalLibraries, libraryName);
    }

    static synchronized <T> ArrayList<T> prefix(ArrayList<T> cur, T val) {
        if (cur == null) {
            cur = new ArrayList<>();
        }
        cur.add(0, val);
        return cur;
    }

    private static synchronized boolean isLibraryPresent(ArrayList<String> usesLibraries, ArrayList<String> usesOptionalLibraries, String apacheHttpLegacy) {
        return ArrayUtils.contains(usesLibraries, apacheHttpLegacy) || ArrayUtils.contains(usesOptionalLibraries, apacheHttpLegacy);
    }

    static synchronized boolean apkTargetsApiLevelLessThanOrEqualToOMR1(PackageParser.Package pkg) {
        int targetSdkVersion = pkg.applicationInfo.targetSdkVersion;
        return targetSdkVersion < 28;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void prefixImplicitDependency(PackageParser.Package pkg, String existingLibrary, String implicitDependency) {
        ArrayList<String> usesLibraries = pkg.usesLibraries;
        ArrayList<String> usesOptionalLibraries = pkg.usesOptionalLibraries;
        if (!isLibraryPresent(usesLibraries, usesOptionalLibraries, implicitDependency)) {
            if (ArrayUtils.contains(usesLibraries, existingLibrary)) {
                prefix(usesLibraries, implicitDependency);
            } else if (ArrayUtils.contains(usesOptionalLibraries, existingLibrary)) {
                prefix(usesOptionalLibraries, implicitDependency);
            }
            pkg.usesLibraries = usesLibraries;
            pkg.usesOptionalLibraries = usesOptionalLibraries;
        }
    }

    synchronized void prefixRequiredLibrary(PackageParser.Package pkg, String libraryName) {
        ArrayList<String> usesLibraries = pkg.usesLibraries;
        ArrayList<String> usesOptionalLibraries = pkg.usesOptionalLibraries;
        boolean alreadyPresent = isLibraryPresent(usesLibraries, usesOptionalLibraries, libraryName);
        if (!alreadyPresent) {
            pkg.usesLibraries = prefix(usesLibraries, libraryName);
        }
    }
}
