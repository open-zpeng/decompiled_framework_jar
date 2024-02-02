package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageParser;
import android.os.Bundle;
import android.util.SparseArray;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
/* loaded from: classes.dex */
public abstract class PackageManagerInternal {
    public static final int PACKAGE_BROWSER = 4;
    public static final int PACKAGE_INSTALLER = 2;
    public static final int PACKAGE_SETUP_WIZARD = 1;
    public static final int PACKAGE_SYSTEM = 0;
    public static final int PACKAGE_SYSTEM_TEXT_CLASSIFIER = 5;
    public static final int PACKAGE_VERIFIER = 3;

    /* loaded from: classes.dex */
    public interface ExternalSourcesPolicy {
        public static final int USER_BLOCKED = 1;
        public static final int USER_DEFAULT = 2;
        public static final int USER_TRUSTED = 0;

        synchronized int getPackageTrustedToInstallApps(String str, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface KnownPackage {
    }

    /* loaded from: classes.dex */
    public interface PackageListObserver {
        synchronized void onPackageAdded(String str);

        synchronized void onPackageRemoved(String str);
    }

    /* loaded from: classes.dex */
    public interface PackagesProvider {
        synchronized String[] getPackages(int i);
    }

    /* loaded from: classes.dex */
    public interface SyncAdapterPackagesProvider {
        synchronized String[] getPackages(String str, int i);
    }

    public abstract synchronized void addIsolatedUid(int i, int i2);

    public abstract synchronized boolean canAccessComponent(int i, ComponentName componentName, int i2);

    public abstract synchronized boolean canAccessInstantApps(int i, int i2);

    public abstract synchronized boolean filterAppAccess(PackageParser.Package r1, int i, int i2);

    public abstract synchronized ActivityInfo getActivityInfo(ComponentName componentName, int i, int i2, int i3);

    public abstract synchronized ApplicationInfo getApplicationInfo(String str, int i, int i2, int i3);

    public abstract synchronized ComponentName getDefaultHomeActivity(int i);

    public abstract synchronized PackageParser.Package getDisabledPackage(String str);

    public abstract synchronized ComponentName getHomeActivitiesAsUser(List<ResolveInfo> list, int i);

    public abstract synchronized String getInstantAppPackageName(int i);

    public abstract synchronized String getKnownPackageName(int i, int i2);

    public abstract synchronized String getNameForUid(int i);

    public abstract synchronized List<PackageInfo> getOverlayPackages(int i);

    public abstract synchronized PackageParser.Package getPackage(String str);

    public abstract synchronized PackageInfo getPackageInfo(String str, int i, int i2, int i3);

    public abstract synchronized PackageList getPackageList(PackageListObserver packageListObserver);

    public abstract synchronized int getPackageTargetSdkVersion(String str);

    public abstract synchronized int getPackageUid(String str, int i, int i2);

    public abstract synchronized int getPermissionFlagsTEMP(String str, String str2, int i);

    public abstract synchronized String getSetupWizardPackageName();

    public abstract synchronized String getSuspendedDialogMessage(String str, int i);

    public abstract synchronized Bundle getSuspendedPackageLauncherExtras(String str, int i);

    public abstract synchronized String getSuspendingPackage(String str, int i);

    public abstract synchronized List<String> getTargetPackageNames(int i);

    public abstract synchronized int getUidTargetSdkVersion(int i);

    public abstract synchronized void grantDefaultPermissionsToDefaultDialerApp(String str, int i);

    public abstract synchronized void grantDefaultPermissionsToDefaultSimCallManager(String str, int i);

    public abstract synchronized void grantDefaultPermissionsToDefaultSmsApp(String str, int i);

    public abstract synchronized void grantDefaultPermissionsToDefaultUseOpenWifiApp(String str, int i);

    public abstract synchronized void grantEphemeralAccess(int i, Intent intent, int i2, int i3);

    public abstract synchronized void grantRuntimePermission(String str, String str2, int i, boolean z);

    public abstract synchronized boolean hasInstantApplicationMetadata(String str, int i);

    public abstract synchronized boolean hasSignatureCapability(int i, int i2, @PackageParser.SigningDetails.CertCapabilities int i3);

    public abstract synchronized boolean isDataRestoreSafe(Signature signature, String str);

    public abstract synchronized boolean isDataRestoreSafe(byte[] bArr, String str);

    public abstract synchronized boolean isInstantApp(String str, int i);

    public abstract synchronized boolean isInstantAppInstallerComponent(ComponentName componentName);

    public abstract synchronized boolean isLegacySystemApp(PackageParser.Package r1);

    public abstract synchronized boolean isPackageDataProtected(int i, String str);

    public abstract synchronized boolean isPackageEphemeral(int i, String str);

    public abstract synchronized boolean isPackagePersistent(String str);

    public abstract synchronized boolean isPackageStateProtected(String str, int i);

    public abstract synchronized boolean isPackageSuspended(String str, int i);

    public abstract synchronized boolean isPermissionsReviewRequired(String str, int i);

    public abstract synchronized boolean isResolveActivityComponent(ComponentInfo componentInfo);

    public abstract synchronized void notifyPackageUse(String str, int i);

    public abstract synchronized void pruneInstantApps();

    public abstract synchronized List<ResolveInfo> queryIntentActivities(Intent intent, int i, int i2, int i3);

    public abstract synchronized List<ResolveInfo> queryIntentServices(Intent intent, int i, int i2, int i3);

    public abstract synchronized void removeIsolatedUid(int i);

    public abstract synchronized void removePackageListObserver(PackageListObserver packageListObserver);

    public abstract synchronized void requestInstantAppResolutionPhaseTwo(AuxiliaryResolveInfo auxiliaryResolveInfo, Intent intent, String str, String str2, Bundle bundle, int i);

    public abstract synchronized ProviderInfo resolveContentProvider(String str, int i, int i2);

    public abstract synchronized ResolveInfo resolveIntent(Intent intent, String str, int i, int i2, boolean z, int i3);

    public abstract synchronized ResolveInfo resolveService(Intent intent, String str, int i, int i2, int i3);

    public abstract synchronized void revokeRuntimePermission(String str, String str2, int i, boolean z);

    public abstract synchronized void setDeviceAndProfileOwnerPackages(int i, String str, SparseArray<String> sparseArray);

    public abstract synchronized void setDialerAppPackagesProvider(PackagesProvider packagesProvider);

    public abstract synchronized boolean setEnabledOverlayPackages(int i, String str, List<String> list);

    public abstract synchronized void setExternalSourcesPolicy(ExternalSourcesPolicy externalSourcesPolicy);

    public abstract synchronized void setKeepUninstalledPackages(List<String> list);

    public abstract synchronized void setLocationPackagesProvider(PackagesProvider packagesProvider);

    public abstract synchronized void setSimCallManagerPackagesProvider(PackagesProvider packagesProvider);

    public abstract synchronized void setSmsAppPackagesProvider(PackagesProvider packagesProvider);

    public abstract synchronized void setSyncAdapterPackagesprovider(SyncAdapterPackagesProvider syncAdapterPackagesProvider);

    public abstract synchronized void setUseOpenWifiAppPackagesProvider(PackagesProvider packagesProvider);

    public abstract synchronized void setVoiceInteractionPackagesProvider(PackagesProvider packagesProvider);

    public abstract synchronized void updatePermissionFlagsTEMP(String str, String str2, int i, int i2, int i3);

    public abstract synchronized boolean wasPackageEverLaunched(String str, int i);

    public synchronized PackageList getPackageList() {
        return getPackageList(null);
    }
}
