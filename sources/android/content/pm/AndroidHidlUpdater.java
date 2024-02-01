package android.content.pm;

import android.content.pm.PackageParser;
import com.android.internal.annotations.VisibleForTesting;

@VisibleForTesting
/* loaded from: classes.dex */
public class AndroidHidlUpdater extends PackageSharedLibraryUpdater {
    @Override // android.content.pm.PackageSharedLibraryUpdater
    public void updatePackage(PackageParser.Package pkg) {
        ApplicationInfo info = pkg.applicationInfo;
        boolean isSystem = true;
        boolean isLegacy = info.targetSdkVersion <= 28;
        if (!info.isSystemApp() && !info.isUpdatedSystemApp()) {
            isSystem = false;
        }
        if (isLegacy && isSystem) {
            prefixRequiredLibrary(pkg, "android.hidl.base-V1.0-java");
            prefixRequiredLibrary(pkg, "android.hidl.manager-V1.0-java");
            return;
        }
        removeLibrary(pkg, "android.hidl.base-V1.0-java");
        removeLibrary(pkg, "android.hidl.manager-V1.0-java");
    }
}
