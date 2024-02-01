package android.content.pm;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.IDexModuleRegisterCallback;
import android.content.pm.IOnPermissionsChangeListener;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageDeleteObserver2;
import android.content.pm.IPackageInstaller;
import android.content.pm.IPackageMoveObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.dex.IArtManager;
import android.content.pm.permission.SplitPermissionInfoParcelable;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.text.TextUtils;
import com.xiaopeng.app.xpAppInfo;
import com.xiaopeng.app.xpPackageInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public interface IPackageManager extends IInterface {
    boolean activitySupportsIntent(ComponentName componentName, Intent intent, String str) throws RemoteException;

    void addCrossProfileIntentFilter(IntentFilter intentFilter, String str, int i, int i2, int i3) throws RemoteException;

    void addOnPermissionsChangeListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) throws RemoteException;

    @UnsupportedAppUsage
    boolean addPermission(PermissionInfo permissionInfo) throws RemoteException;

    @UnsupportedAppUsage
    boolean addPermissionAsync(PermissionInfo permissionInfo) throws RemoteException;

    void addPersistentPreferredActivity(IntentFilter intentFilter, ComponentName componentName, int i) throws RemoteException;

    void addPreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName, int i2) throws RemoteException;

    boolean addWhitelistedRestrictedPermission(String str, String str2, int i, int i2) throws RemoteException;

    boolean canForwardTo(Intent intent, String str, int i, int i2) throws RemoteException;

    boolean canRequestPackageInstalls(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    String[] canonicalToCurrentPackageNames(String[] strArr) throws RemoteException;

    void checkPackageStartable(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    int checkPermission(String str, String str2, int i) throws RemoteException;

    @UnsupportedAppUsage
    int checkSignatures(String str, String str2) throws RemoteException;

    int checkUidPermission(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    int checkUidSignatures(int i, int i2) throws RemoteException;

    void clearApplicationProfileData(String str) throws RemoteException;

    void clearApplicationUserData(String str, IPackageDataObserver iPackageDataObserver, int i) throws RemoteException;

    void clearCrossProfileIntentFilters(int i, String str) throws RemoteException;

    void clearPackagePersistentPreferredActivities(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    void clearPackagePreferredActivities(String str) throws RemoteException;

    boolean compileLayouts(String str) throws RemoteException;

    @UnsupportedAppUsage
    String[] currentToCanonicalPackageNames(String[] strArr) throws RemoteException;

    @UnsupportedAppUsage
    void deleteApplicationCacheFiles(String str, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    void deleteApplicationCacheFilesAsUser(String str, int i, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    void deletePackageAsUser(String str, int i, IPackageDeleteObserver iPackageDeleteObserver, int i2, int i3) throws RemoteException;

    void deletePackageVersioned(VersionedPackage versionedPackage, IPackageDeleteObserver2 iPackageDeleteObserver2, int i, int i2) throws RemoteException;

    void deletePreloadsFileCache() throws RemoteException;

    void dumpProfiles(String str) throws RemoteException;

    void enterSafeMode() throws RemoteException;

    void extendVerificationTimeout(int i, int i2, long j) throws RemoteException;

    ResolveInfo findPersistentPreferredActivity(Intent intent, int i) throws RemoteException;

    void finishPackageInstall(int i, boolean z) throws RemoteException;

    void flushPackageRestrictionsAsUser(int i) throws RemoteException;

    void forceDexOpt(String str) throws RemoteException;

    void freeStorage(String str, long j, int i, IntentSender intentSender) throws RemoteException;

    void freeStorageAndNotify(String str, long j, int i, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    @UnsupportedAppUsage
    ActivityInfo getActivityInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    ParceledListSlice getAllIntentFilters(String str) throws RemoteException;

    List<String> getAllPackages() throws RemoteException;

    ParceledListSlice getAllPermissionGroups(int i) throws RemoteException;

    List<xpPackageInfo> getAllXpPackageInfos() throws RemoteException;

    @UnsupportedAppUsage
    String[] getAppOpPermissionPackages(String str) throws RemoteException;

    String getAppPredictionServicePackageName() throws RemoteException;

    @UnsupportedAppUsage
    int getApplicationEnabledSetting(String str, int i) throws RemoteException;

    boolean getApplicationHiddenSettingAsUser(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    ApplicationInfo getApplicationInfo(String str, int i, int i2) throws RemoteException;

    IArtManager getArtManager() throws RemoteException;

    String getAttentionServicePackageName() throws RemoteException;

    @UnsupportedAppUsage
    boolean getBlockUninstallForUser(String str, int i) throws RemoteException;

    ChangedPackages getChangedPackages(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    int getComponentEnabledSetting(ComponentName componentName, int i) throws RemoteException;

    ParceledListSlice getDeclaredSharedLibraries(String str, int i, int i2) throws RemoteException;

    byte[] getDefaultAppsBackup(int i) throws RemoteException;

    String getDefaultBrowserPackageName(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getFlagsForUid(int i) throws RemoteException;

    CharSequence getHarmfulAppWarning(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    ComponentName getHomeActivities(List<ResolveInfo> list) throws RemoteException;

    String getIncidentReportApproverPackageName() throws RemoteException;

    @UnsupportedAppUsage
    int getInstallLocation() throws RemoteException;

    int getInstallReason(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice getInstalledApplications(int i, int i2) throws RemoteException;

    List<ModuleInfo> getInstalledModules(int i) throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice getInstalledPackages(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String getInstallerPackageName(String str) throws RemoteException;

    String getInstantAppAndroidId(String str, int i) throws RemoteException;

    byte[] getInstantAppCookie(String str, int i) throws RemoteException;

    Bitmap getInstantAppIcon(String str, int i) throws RemoteException;

    ComponentName getInstantAppInstallerComponent() throws RemoteException;

    ComponentName getInstantAppResolverComponent() throws RemoteException;

    ComponentName getInstantAppResolverSettingsComponent() throws RemoteException;

    ParceledListSlice getInstantApps(int i) throws RemoteException;

    @UnsupportedAppUsage
    InstrumentationInfo getInstrumentationInfo(ComponentName componentName, int i) throws RemoteException;

    byte[] getIntentFilterVerificationBackup(int i) throws RemoteException;

    ParceledListSlice getIntentFilterVerifications(String str) throws RemoteException;

    int getIntentVerificationStatus(String str, int i) throws RemoteException;

    KeySet getKeySetByAlias(String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    ResolveInfo getLastChosenActivity(Intent intent, String str, int i) throws RemoteException;

    ModuleInfo getModuleInfo(String str, int i) throws RemoteException;

    int getMoveStatus(int i) throws RemoteException;

    @UnsupportedAppUsage
    String getNameForUid(int i) throws RemoteException;

    String[] getNamesForUids(int[] iArr) throws RemoteException;

    ActivityInfo getOverlayActivityInfo(ActivityInfo activityInfo) throws RemoteException;

    ApplicationInfo getOverlayApplicationInfo(ApplicationInfo applicationInfo) throws RemoteException;

    int[] getPackageGids(String str, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    PackageInfo getPackageInfo(String str, int i, int i2) throws RemoteException;

    PackageInfo getPackageInfoVersioned(VersionedPackage versionedPackage, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    IPackageInstaller getPackageInstaller() throws RemoteException;

    void getPackageSizeInfo(String str, int i, IPackageStatsObserver iPackageStatsObserver) throws RemoteException;

    @UnsupportedAppUsage
    int getPackageUid(String str, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String[] getPackagesForUid(int i) throws RemoteException;

    ParceledListSlice getPackagesHoldingPermissions(String[] strArr, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String getPermissionControllerPackageName() throws RemoteException;

    int getPermissionFlags(String str, String str2, int i) throws RemoteException;

    @UnsupportedAppUsage
    PermissionGroupInfo getPermissionGroupInfo(String str, int i) throws RemoteException;

    PermissionInfo getPermissionInfo(String str, String str2, int i) throws RemoteException;

    ParceledListSlice getPersistentApplications(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getPreferredActivities(List<IntentFilter> list, List<ComponentName> list2, String str) throws RemoteException;

    byte[] getPreferredActivityBackup(int i) throws RemoteException;

    int getPrivateFlagsForUid(int i) throws RemoteException;

    @UnsupportedAppUsage
    ProviderInfo getProviderInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    ActivityInfo getReceiverInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    int getRuntimePermissionsVersion(int i) throws RemoteException;

    @UnsupportedAppUsage
    ServiceInfo getServiceInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String getServicesSystemSharedLibraryPackageName() throws RemoteException;

    ParceledListSlice getSharedLibraries(String str, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    String getSharedSystemSharedLibraryPackageName() throws RemoteException;

    KeySet getSigningKeySet(String str) throws RemoteException;

    List<SplitPermissionInfoParcelable> getSplitPermissions() throws RemoteException;

    PersistableBundle getSuspendedPackageAppExtras(String str, int i) throws RemoteException;

    ParceledListSlice getSystemAvailableFeatures() throws RemoteException;

    String getSystemCaptionsServicePackageName() throws RemoteException;

    @UnsupportedAppUsage
    String[] getSystemSharedLibraryNames() throws RemoteException;

    String getSystemTextClassifierPackageName() throws RemoteException;

    @UnsupportedAppUsage
    int getUidForSharedUser(String str) throws RemoteException;

    String[] getUnsuspendablePackagesForUser(String[] strArr, int i) throws RemoteException;

    VerifierDeviceIdentity getVerifierDeviceIdentity() throws RemoteException;

    String getWellbeingPackageName() throws RemoteException;

    List<String> getWhitelistedRestrictedPermissions(String str, int i, int i2) throws RemoteException;

    Bitmap getXpAppIcon(String str) throws RemoteException;

    List<xpAppInfo> getXpAppPackageList(int i) throws RemoteException;

    xpPackageInfo getXpPackageInfo(String str) throws RemoteException;

    void grantDefaultPermissionsToActiveLuiApp(String str, int i) throws RemoteException;

    void grantDefaultPermissionsToEnabledCarrierApps(String[] strArr, int i) throws RemoteException;

    void grantDefaultPermissionsToEnabledImsServices(String[] strArr, int i) throws RemoteException;

    void grantDefaultPermissionsToEnabledTelephonyDataServices(String[] strArr, int i) throws RemoteException;

    @UnsupportedAppUsage
    void grantRuntimePermission(String str, String str2, int i) throws RemoteException;

    boolean hasSigningCertificate(String str, byte[] bArr, int i) throws RemoteException;

    boolean hasSystemFeature(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean hasSystemUidErrors() throws RemoteException;

    boolean hasUidSigningCertificate(int i, byte[] bArr, int i2) throws RemoteException;

    int installExistingPackageAsUser(String str, int i, int i2, int i3, List<String> list) throws RemoteException;

    boolean isDeviceUpgrading() throws RemoteException;

    boolean isFirstBoot() throws RemoteException;

    boolean isInstantApp(String str, int i) throws RemoteException;

    boolean isOnlyCoreApps() throws RemoteException;

    @UnsupportedAppUsage
    boolean isPackageAvailable(String str, int i) throws RemoteException;

    boolean isPackageDeviceAdminOnAnyUser(String str) throws RemoteException;

    boolean isPackageSignedByKeySet(String str, KeySet keySet) throws RemoteException;

    boolean isPackageSignedByKeySetExactly(String str, KeySet keySet) throws RemoteException;

    boolean isPackageStateProtected(String str, int i) throws RemoteException;

    boolean isPackageSuspendedForUser(String str, int i) throws RemoteException;

    boolean isPermissionEnforced(String str) throws RemoteException;

    boolean isPermissionRevokedByPolicy(String str, String str2, int i) throws RemoteException;

    boolean isProtectedBroadcast(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean isSafeMode() throws RemoteException;

    @UnsupportedAppUsage
    boolean isStorageLow() throws RemoteException;

    @UnsupportedAppUsage
    boolean isUidPrivileged(int i) throws RemoteException;

    void logAppProcessStartIfNeeded(String str, int i, String str2, String str3, int i2) throws RemoteException;

    int movePackage(String str, String str2) throws RemoteException;

    int movePrimaryStorage(String str) throws RemoteException;

    void notifyDexLoad(String str, List<String> list, List<String> list2, String str2) throws RemoteException;

    void notifyPackageUse(String str, int i) throws RemoteException;

    void notifyPackagesReplacedReceived(String[] strArr) throws RemoteException;

    boolean performDexOptMode(String str, boolean z, String str2, boolean z2, boolean z3, String str3) throws RemoteException;

    boolean performDexOptSecondary(String str, String str2, boolean z) throws RemoteException;

    void performFstrimIfNeeded() throws RemoteException;

    ParceledListSlice queryContentProviders(String str, int i, int i2, String str2) throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice queryInstrumentation(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice queryIntentActivities(Intent intent, String str, int i, int i2) throws RemoteException;

    ParceledListSlice queryIntentActivityOptions(ComponentName componentName, Intent[] intentArr, String[] strArr, Intent intent, String str, int i, int i2) throws RemoteException;

    ParceledListSlice queryIntentContentProviders(Intent intent, String str, int i, int i2) throws RemoteException;

    ParceledListSlice queryIntentReceivers(Intent intent, String str, int i, int i2) throws RemoteException;

    ParceledListSlice queryIntentServices(Intent intent, String str, int i, int i2) throws RemoteException;

    ParceledListSlice queryPermissionsByGroup(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    void querySyncProviders(List<String> list, List<ProviderInfo> list2) throws RemoteException;

    void reconcileSecondaryDexFiles(String str) throws RemoteException;

    void registerDexModule(String str, String str2, boolean z, IDexModuleRegisterCallback iDexModuleRegisterCallback) throws RemoteException;

    void registerMoveCallback(IPackageMoveObserver iPackageMoveObserver) throws RemoteException;

    void removeOnPermissionsChangeListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) throws RemoteException;

    @UnsupportedAppUsage
    void removePermission(String str) throws RemoteException;

    boolean removeWhitelistedRestrictedPermission(String str, String str2, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void replacePreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName, int i2) throws RemoteException;

    void resetApplicationPreferences(int i) throws RemoteException;

    void resetRuntimePermissions() throws RemoteException;

    ProviderInfo resolveContentProvider(String str, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    ResolveInfo resolveIntent(Intent intent, String str, int i, int i2) throws RemoteException;

    ResolveInfo resolveService(Intent intent, String str, int i, int i2) throws RemoteException;

    void restoreDefaultApps(byte[] bArr, int i) throws RemoteException;

    void restoreIntentFilterVerification(byte[] bArr, int i) throws RemoteException;

    void restorePreferredActivities(byte[] bArr, int i) throws RemoteException;

    void revokeDefaultPermissionsFromDisabledTelephonyDataServices(String[] strArr, int i) throws RemoteException;

    void revokeDefaultPermissionsFromLuiApps(String[] strArr, int i) throws RemoteException;

    void revokeRuntimePermission(String str, String str2, int i) throws RemoteException;

    boolean runBackgroundDexoptJob(List<String> list) throws RemoteException;

    void sendDeviceCustomizationReadyBroadcast() throws RemoteException;

    void setApplicationCategoryHint(String str, int i, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void setApplicationEnabledSetting(String str, int i, int i2, int i3, String str2) throws RemoteException;

    @UnsupportedAppUsage
    boolean setApplicationHiddenSettingAsUser(String str, boolean z, int i) throws RemoteException;

    boolean setBlockUninstallForUser(String str, boolean z, int i) throws RemoteException;

    @UnsupportedAppUsage
    void setComponentEnabledSetting(ComponentName componentName, int i, int i2, int i3) throws RemoteException;

    boolean setDefaultBrowserPackageName(String str, int i) throws RemoteException;

    String[] setDistractingPackageRestrictionsAsUser(String[] strArr, int i, int i2) throws RemoteException;

    void setHarmfulAppWarning(String str, CharSequence charSequence, int i) throws RemoteException;

    void setHomeActivity(ComponentName componentName, int i) throws RemoteException;

    boolean setInstallLocation(int i) throws RemoteException;

    @UnsupportedAppUsage
    void setInstallerPackageName(String str, String str2) throws RemoteException;

    boolean setInstantAppCookie(String str, byte[] bArr, int i) throws RemoteException;

    @UnsupportedAppUsage
    void setLastChosenActivity(Intent intent, String str, int i, IntentFilter intentFilter, int i2, ComponentName componentName) throws RemoteException;

    void setPackageNotLaunched(String str, boolean z, int i) throws RemoteException;

    @UnsupportedAppUsage
    void setPackageStoppedState(String str, boolean z, int i) throws RemoteException;

    String[] setPackagesSuspendedAsUser(String[] strArr, boolean z, PersistableBundle persistableBundle, PersistableBundle persistableBundle2, SuspendDialogInfo suspendDialogInfo, String str, int i) throws RemoteException;

    void setPermissionEnforced(String str, boolean z) throws RemoteException;

    boolean setRequiredForSystemUser(String str, boolean z) throws RemoteException;

    void setRuntimePermissionsVersion(int i, int i2) throws RemoteException;

    void setSystemAppHiddenUntilInstalled(String str, boolean z) throws RemoteException;

    boolean setSystemAppInstallState(String str, boolean z, int i) throws RemoteException;

    void setUpdateAvailable(String str, boolean z) throws RemoteException;

    void setXpPackageInfo(String str, String str2) throws RemoteException;

    boolean shouldShowRequestPermissionRationale(String str, String str2, int i) throws RemoteException;

    void systemReady() throws RemoteException;

    void unregisterMoveCallback(IPackageMoveObserver iPackageMoveObserver) throws RemoteException;

    void updateAppScreenFlag(int i) throws RemoteException;

    boolean updateIntentVerificationStatus(String str, int i, int i2) throws RemoteException;

    void updatePackagesIfNeeded() throws RemoteException;

    void updatePermissionFlags(String str, String str2, int i, int i2, boolean z, int i3) throws RemoteException;

    void updatePermissionFlagsForAllApps(int i, int i2, int i3) throws RemoteException;

    void verifyIntentFilter(int i, int i2, List<String> list) throws RemoteException;

    void verifyPendingInstall(int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IPackageManager {
        @Override // android.content.pm.IPackageManager
        public void checkPackageStartable(String packageName, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public boolean isPackageAvailable(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public PackageInfo getPackageInfoVersioned(VersionedPackage versionedPackage, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public int getPackageUid(String packageName, int flags, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public int[] getPackageGids(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String[] currentToCanonicalPackageNames(String[] names) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String[] canonicalToCurrentPackageNames(String[] names) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public PermissionInfo getPermissionInfo(String name, String packageName, int flags) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice queryPermissionsByGroup(String group, int flags) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice getAllPermissionGroups(int flags) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ActivityInfo getActivityInfo(ComponentName className, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean activitySupportsIntent(ComponentName className, Intent intent, String resolvedType) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public ActivityInfo getReceiverInfo(ComponentName className, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ServiceInfo getServiceInfo(ComponentName className, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ProviderInfo getProviderInfo(ComponentName className, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public int checkPermission(String permName, String pkgName, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public int checkUidPermission(String permName, int uid) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public boolean addPermission(PermissionInfo info) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void removePermission(String name) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void grantRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void revokeRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void resetRuntimePermissions() throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public int getPermissionFlags(String permissionName, String packageName, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public void updatePermissionFlags(String permissionName, String packageName, int flagMask, int flagValues, boolean checkAdjustPolicyFlagPermission, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void updatePermissionFlagsForAllApps(int flagMask, int flagValues, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public List<String> getWhitelistedRestrictedPermissions(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean addWhitelistedRestrictedPermission(String packageName, String permission, int whitelistFlags, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean removeWhitelistedRestrictedPermission(String packageName, String permission, int whitelistFlags, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean shouldShowRequestPermissionRationale(String permissionName, String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isProtectedBroadcast(String actionName) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public int checkSignatures(String pkg1, String pkg2) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public int checkUidSignatures(int uid1, int uid2) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public List<String> getAllPackages() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String[] getPackagesForUid(int uid) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String getNameForUid(int uid) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String[] getNamesForUids(int[] uids) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public int getUidForSharedUser(String sharedUserName) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public int getFlagsForUid(int uid) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public int getPrivateFlagsForUid(int uid) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isUidPrivileged(int uid) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public String[] getAppOpPermissionPackages(String permissionName) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ResolveInfo findPersistentPreferredActivity(Intent intent, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean canForwardTo(Intent intent, String resolvedType, int sourceUserId, int targetUserId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice queryIntentActivityOptions(ComponentName caller, Intent[] specifics, String[] specificTypes, Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice queryIntentServices(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice getInstalledPackages(int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice getPackagesHoldingPermissions(String[] permissions, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice getInstalledApplications(int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice getPersistentApplications(int flags) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ProviderInfo resolveContentProvider(String name, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public void querySyncProviders(List<String> outNames, List<ProviderInfo> outInfo) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice queryContentProviders(String processName, int uid, int flags, String metaDataKey) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public InstrumentationInfo getInstrumentationInfo(ComponentName className, int flags) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice queryInstrumentation(String targetPackage, int flags) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public void finishPackageInstall(int token, boolean didLaunch) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void setInstallerPackageName(String targetPackage, String installerPackageName) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void setApplicationCategoryHint(String packageName, int categoryHint, String callerPackageName) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void deletePackageAsUser(String packageName, int versionCode, IPackageDeleteObserver observer, int userId, int flags) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void deletePackageVersioned(VersionedPackage versionedPackage, IPackageDeleteObserver2 observer, int userId, int flags) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public String getInstallerPackageName(String packageName) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public void resetApplicationPreferences(int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public ResolveInfo getLastChosenActivity(Intent intent, String resolvedType, int flags) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public void setLastChosenActivity(Intent intent, String resolvedType, int flags, IntentFilter filter, int match, ComponentName activity) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void replacePreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void clearPackagePreferredActivities(String packageName) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public int getPreferredActivities(List<IntentFilter> outFilters, List<ComponentName> outActivities, String packageName) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public void addPersistentPreferredActivity(IntentFilter filter, ComponentName activity, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void clearPackagePersistentPreferredActivities(String packageName, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void addCrossProfileIntentFilter(IntentFilter intentFilter, String ownerPackage, int sourceUserId, int targetUserId, int flags) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void clearCrossProfileIntentFilters(int sourceUserId, String ownerPackage) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public String[] setDistractingPackageRestrictionsAsUser(String[] packageNames, int restrictionFlags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String[] setPackagesSuspendedAsUser(String[] packageNames, boolean suspended, PersistableBundle appExtras, PersistableBundle launcherExtras, SuspendDialogInfo dialogInfo, String callingPackage, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String[] getUnsuspendablePackagesForUser(String[] packageNames, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isPackageSuspendedForUser(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public PersistableBundle getSuspendedPackageAppExtras(String packageName, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public byte[] getPreferredActivityBackup(int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public void restorePreferredActivities(byte[] backup, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public byte[] getDefaultAppsBackup(int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public void restoreDefaultApps(byte[] backup, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public byte[] getIntentFilterVerificationBackup(int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public void restoreIntentFilterVerification(byte[] backup, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public ComponentName getHomeActivities(List<ResolveInfo> outHomeCandidates) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public void setHomeActivity(ComponentName className, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public int getComponentEnabledSetting(ComponentName componentName, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public int getApplicationEnabledSetting(String packageName, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public void logAppProcessStartIfNeeded(String processName, int uid, String seinfo, String apkFile, int pid) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void flushPackageRestrictionsAsUser(int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void setPackageStoppedState(String packageName, boolean stopped, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void freeStorageAndNotify(String volumeUuid, long freeStorageSize, int storageFlags, IPackageDataObserver observer) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void freeStorage(String volumeUuid, long freeStorageSize, int storageFlags, IntentSender pi) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void deleteApplicationCacheFiles(String packageName, IPackageDataObserver observer) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void deleteApplicationCacheFilesAsUser(String packageName, int userId, IPackageDataObserver observer) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void clearApplicationUserData(String packageName, IPackageDataObserver observer, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void clearApplicationProfileData(String packageName) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void getPackageSizeInfo(String packageName, int userHandle, IPackageStatsObserver observer) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public String[] getSystemSharedLibraryNames() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice getSystemAvailableFeatures() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean hasSystemFeature(String name, int version) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void enterSafeMode() throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public boolean isSafeMode() throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void systemReady() throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public boolean hasSystemUidErrors() throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void performFstrimIfNeeded() throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void updatePackagesIfNeeded() throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void notifyPackageUse(String packageName, int reason) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void notifyDexLoad(String loadingPackageName, List<String> classLoadersNames, List<String> classPaths, String loaderIsa) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void registerDexModule(String packageName, String dexModulePath, boolean isSharedModule, IDexModuleRegisterCallback callback) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public boolean performDexOptMode(String packageName, boolean checkProfiles, String targetCompilerFilter, boolean force, boolean bootComplete, String splitName) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean performDexOptSecondary(String packageName, String targetCompilerFilter, boolean force) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean compileLayouts(String packageName) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void dumpProfiles(String packageName) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void forceDexOpt(String packageName) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public boolean runBackgroundDexoptJob(List<String> packageNames) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void reconcileSecondaryDexFiles(String packageName) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public int getMoveStatus(int moveId) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public void registerMoveCallback(IPackageMoveObserver callback) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void unregisterMoveCallback(IPackageMoveObserver callback) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public int movePackage(String packageName, String volumeUuid) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public int movePrimaryStorage(String volumeUuid) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public boolean addPermissionAsync(PermissionInfo info) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean setInstallLocation(int loc) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public int getInstallLocation() throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public int installExistingPackageAsUser(String packageName, int userId, int installFlags, int installReason, List<String> whiteListedPermissions) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public void verifyPendingInstall(int id, int verificationCode) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void verifyIntentFilter(int id, int verificationCode, List<String> failedDomains) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public int getIntentVerificationStatus(String packageName, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public boolean updateIntentVerificationStatus(String packageName, int status, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice getIntentFilterVerifications(String packageName) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice getAllIntentFilters(String packageName) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean setDefaultBrowserPackageName(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public String getDefaultBrowserPackageName(int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public VerifierDeviceIdentity getVerifierDeviceIdentity() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isFirstBoot() throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isOnlyCoreApps() throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isDeviceUpgrading() throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void setPermissionEnforced(String permission, boolean enforced) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public boolean isPermissionEnforced(String permission) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isStorageLow() throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean setApplicationHiddenSettingAsUser(String packageName, boolean hidden, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean getApplicationHiddenSettingAsUser(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void setSystemAppHiddenUntilInstalled(String packageName, boolean hidden) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public boolean setSystemAppInstallState(String packageName, boolean installed, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public IPackageInstaller getPackageInstaller() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean setBlockUninstallForUser(String packageName, boolean blockUninstall, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean getBlockUninstallForUser(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public KeySet getKeySetByAlias(String packageName, String alias) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public KeySet getSigningKeySet(String packageName) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isPackageSignedByKeySet(String packageName, KeySet ks) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isPackageSignedByKeySetExactly(String packageName, KeySet ks) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void addOnPermissionsChangeListener(IOnPermissionsChangeListener listener) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void removeOnPermissionsChangeListener(IOnPermissionsChangeListener listener) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void grantDefaultPermissionsToEnabledCarrierApps(String[] packageNames, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void grantDefaultPermissionsToEnabledImsServices(String[] packageNames, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void grantDefaultPermissionsToEnabledTelephonyDataServices(String[] packageNames, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void revokeDefaultPermissionsFromDisabledTelephonyDataServices(String[] packageNames, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void grantDefaultPermissionsToActiveLuiApp(String packageName, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void revokeDefaultPermissionsFromLuiApps(String[] packageNames, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public boolean isPermissionRevokedByPolicy(String permission, String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public String getPermissionControllerPackageName() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice getInstantApps(int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public byte[] getInstantAppCookie(String packageName, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean setInstantAppCookie(String packageName, byte[] cookie, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public Bitmap getInstantAppIcon(String packageName, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isInstantApp(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean setRequiredForSystemUser(String packageName, boolean systemUserApp) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void setUpdateAvailable(String packageName, boolean updateAvaialble) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public String getServicesSystemSharedLibraryPackageName() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String getSharedSystemSharedLibraryPackageName() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ChangedPackages getChangedPackages(int sequenceNumber, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isPackageDeviceAdminOnAnyUser(String packageName) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public int getInstallReason(String packageName, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice getSharedLibraries(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ParceledListSlice getDeclaredSharedLibraries(String packageName, int flags, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean canRequestPackageInstalls(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void deletePreloadsFileCache() throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public ComponentName getInstantAppResolverComponent() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ComponentName getInstantAppResolverSettingsComponent() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ComponentName getInstantAppInstallerComponent() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String getInstantAppAndroidId(String packageName, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public IArtManager getArtManager() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public void setHarmfulAppWarning(String packageName, CharSequence warning, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public CharSequence getHarmfulAppWarning(String packageName, int userId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean hasSigningCertificate(String packageName, byte[] signingCertificate, int flags) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public boolean hasUidSigningCertificate(int uid, byte[] signingCertificate, int flags) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public String getSystemTextClassifierPackageName() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String getAttentionServicePackageName() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String getWellbeingPackageName() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String getAppPredictionServicePackageName() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String getSystemCaptionsServicePackageName() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public String getIncidentReportApproverPackageName() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public boolean isPackageStateProtected(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManager
        public void sendDeviceCustomizationReadyBroadcast() throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public List<ModuleInfo> getInstalledModules(int flags) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ModuleInfo getModuleInfo(String packageName, int flags) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public int getRuntimePermissionsVersion(int userId) throws RemoteException {
            return 0;
        }

        @Override // android.content.pm.IPackageManager
        public void setRuntimePermissionsVersion(int version, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void notifyPackagesReplacedReceived(String[] packages) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public List<SplitPermissionInfoParcelable> getSplitPermissions() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ApplicationInfo getOverlayApplicationInfo(ApplicationInfo info) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public ActivityInfo getOverlayActivityInfo(ActivityInfo info) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public xpPackageInfo getXpPackageInfo(String packageName) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public void setXpPackageInfo(String packageName, String packageInfo) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void setPackageNotLaunched(String packageName, boolean stop, int userId) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public void updateAppScreenFlag(int gearLevel) throws RemoteException {
        }

        @Override // android.content.pm.IPackageManager
        public List<xpPackageInfo> getAllXpPackageInfos() throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public List<xpAppInfo> getXpAppPackageList(int screenId) throws RemoteException {
            return null;
        }

        @Override // android.content.pm.IPackageManager
        public Bitmap getXpAppIcon(String packageName) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPackageManager {
        private static final String DESCRIPTOR = "android.content.pm.IPackageManager";
        static final int TRANSACTION_activitySupportsIntent = 15;
        static final int TRANSACTION_addCrossProfileIntentFilter = 78;
        static final int TRANSACTION_addOnPermissionsChangeListener = 162;
        static final int TRANSACTION_addPermission = 21;
        static final int TRANSACTION_addPermissionAsync = 131;
        static final int TRANSACTION_addPersistentPreferredActivity = 76;
        static final int TRANSACTION_addPreferredActivity = 72;
        static final int TRANSACTION_addWhitelistedRestrictedPermission = 30;
        static final int TRANSACTION_canForwardTo = 47;
        static final int TRANSACTION_canRequestPackageInstalls = 186;
        static final int TRANSACTION_canonicalToCurrentPackageNames = 8;
        static final int TRANSACTION_checkPackageStartable = 1;
        static final int TRANSACTION_checkPermission = 19;
        static final int TRANSACTION_checkSignatures = 34;
        static final int TRANSACTION_checkUidPermission = 20;
        static final int TRANSACTION_checkUidSignatures = 35;
        static final int TRANSACTION_clearApplicationProfileData = 105;
        static final int TRANSACTION_clearApplicationUserData = 104;
        static final int TRANSACTION_clearCrossProfileIntentFilters = 79;
        static final int TRANSACTION_clearPackagePersistentPreferredActivities = 77;
        static final int TRANSACTION_clearPackagePreferredActivities = 74;
        static final int TRANSACTION_compileLayouts = 121;
        static final int TRANSACTION_currentToCanonicalPackageNames = 7;
        static final int TRANSACTION_deleteApplicationCacheFiles = 102;
        static final int TRANSACTION_deleteApplicationCacheFilesAsUser = 103;
        static final int TRANSACTION_deletePackageAsUser = 66;
        static final int TRANSACTION_deletePackageVersioned = 67;
        static final int TRANSACTION_deletePreloadsFileCache = 187;
        static final int TRANSACTION_dumpProfiles = 122;
        static final int TRANSACTION_enterSafeMode = 110;
        static final int TRANSACTION_extendVerificationTimeout = 136;
        static final int TRANSACTION_findPersistentPreferredActivity = 46;
        static final int TRANSACTION_finishPackageInstall = 63;
        static final int TRANSACTION_flushPackageRestrictionsAsUser = 98;
        static final int TRANSACTION_forceDexOpt = 123;
        static final int TRANSACTION_freeStorage = 101;
        static final int TRANSACTION_freeStorageAndNotify = 100;
        static final int TRANSACTION_getActivityInfo = 14;
        static final int TRANSACTION_getAllIntentFilters = 141;
        static final int TRANSACTION_getAllPackages = 36;
        static final int TRANSACTION_getAllPermissionGroups = 12;
        static final int TRANSACTION_getAllXpPackageInfos = 217;
        static final int TRANSACTION_getAppOpPermissionPackages = 44;
        static final int TRANSACTION_getAppPredictionServicePackageName = 200;
        static final int TRANSACTION_getApplicationEnabledSetting = 96;
        static final int TRANSACTION_getApplicationHiddenSettingAsUser = 152;
        static final int TRANSACTION_getApplicationInfo = 13;
        static final int TRANSACTION_getArtManager = 192;
        static final int TRANSACTION_getAttentionServicePackageName = 198;
        static final int TRANSACTION_getBlockUninstallForUser = 157;
        static final int TRANSACTION_getChangedPackages = 181;
        static final int TRANSACTION_getComponentEnabledSetting = 94;
        static final int TRANSACTION_getDeclaredSharedLibraries = 185;
        static final int TRANSACTION_getDefaultAppsBackup = 87;
        static final int TRANSACTION_getDefaultBrowserPackageName = 143;
        static final int TRANSACTION_getFlagsForUid = 41;
        static final int TRANSACTION_getHarmfulAppWarning = 194;
        static final int TRANSACTION_getHomeActivities = 91;
        static final int TRANSACTION_getIncidentReportApproverPackageName = 202;
        static final int TRANSACTION_getInstallLocation = 133;
        static final int TRANSACTION_getInstallReason = 183;
        static final int TRANSACTION_getInstalledApplications = 56;
        static final int TRANSACTION_getInstalledModules = 205;
        static final int TRANSACTION_getInstalledPackages = 54;
        static final int TRANSACTION_getInstallerPackageName = 68;
        static final int TRANSACTION_getInstantAppAndroidId = 191;
        static final int TRANSACTION_getInstantAppCookie = 173;
        static final int TRANSACTION_getInstantAppIcon = 175;
        static final int TRANSACTION_getInstantAppInstallerComponent = 190;
        static final int TRANSACTION_getInstantAppResolverComponent = 188;
        static final int TRANSACTION_getInstantAppResolverSettingsComponent = 189;
        static final int TRANSACTION_getInstantApps = 172;
        static final int TRANSACTION_getInstrumentationInfo = 61;
        static final int TRANSACTION_getIntentFilterVerificationBackup = 89;
        static final int TRANSACTION_getIntentFilterVerifications = 140;
        static final int TRANSACTION_getIntentVerificationStatus = 138;
        static final int TRANSACTION_getKeySetByAlias = 158;
        static final int TRANSACTION_getLastChosenActivity = 70;
        static final int TRANSACTION_getModuleInfo = 206;
        static final int TRANSACTION_getMoveStatus = 126;
        static final int TRANSACTION_getNameForUid = 38;
        static final int TRANSACTION_getNamesForUids = 39;
        static final int TRANSACTION_getOverlayActivityInfo = 212;
        static final int TRANSACTION_getOverlayApplicationInfo = 211;
        static final int TRANSACTION_getPackageGids = 6;
        static final int TRANSACTION_getPackageInfo = 3;
        static final int TRANSACTION_getPackageInfoVersioned = 4;
        static final int TRANSACTION_getPackageInstaller = 155;
        static final int TRANSACTION_getPackageSizeInfo = 106;
        static final int TRANSACTION_getPackageUid = 5;
        static final int TRANSACTION_getPackagesForUid = 37;
        static final int TRANSACTION_getPackagesHoldingPermissions = 55;
        static final int TRANSACTION_getPermissionControllerPackageName = 171;
        static final int TRANSACTION_getPermissionFlags = 26;
        static final int TRANSACTION_getPermissionGroupInfo = 11;
        static final int TRANSACTION_getPermissionInfo = 9;
        static final int TRANSACTION_getPersistentApplications = 57;
        static final int TRANSACTION_getPreferredActivities = 75;
        static final int TRANSACTION_getPreferredActivityBackup = 85;
        static final int TRANSACTION_getPrivateFlagsForUid = 42;
        static final int TRANSACTION_getProviderInfo = 18;
        static final int TRANSACTION_getReceiverInfo = 16;
        static final int TRANSACTION_getRuntimePermissionsVersion = 207;
        static final int TRANSACTION_getServiceInfo = 17;
        static final int TRANSACTION_getServicesSystemSharedLibraryPackageName = 179;
        static final int TRANSACTION_getSharedLibraries = 184;
        static final int TRANSACTION_getSharedSystemSharedLibraryPackageName = 180;
        static final int TRANSACTION_getSigningKeySet = 159;
        static final int TRANSACTION_getSplitPermissions = 210;
        static final int TRANSACTION_getSuspendedPackageAppExtras = 84;
        static final int TRANSACTION_getSystemAvailableFeatures = 108;
        static final int TRANSACTION_getSystemCaptionsServicePackageName = 201;
        static final int TRANSACTION_getSystemSharedLibraryNames = 107;
        static final int TRANSACTION_getSystemTextClassifierPackageName = 197;
        static final int TRANSACTION_getUidForSharedUser = 40;
        static final int TRANSACTION_getUnsuspendablePackagesForUser = 82;
        static final int TRANSACTION_getVerifierDeviceIdentity = 144;
        static final int TRANSACTION_getWellbeingPackageName = 199;
        static final int TRANSACTION_getWhitelistedRestrictedPermissions = 29;
        static final int TRANSACTION_getXpAppIcon = 219;
        static final int TRANSACTION_getXpAppPackageList = 218;
        static final int TRANSACTION_getXpPackageInfo = 213;
        static final int TRANSACTION_grantDefaultPermissionsToActiveLuiApp = 168;
        static final int TRANSACTION_grantDefaultPermissionsToEnabledCarrierApps = 164;
        static final int TRANSACTION_grantDefaultPermissionsToEnabledImsServices = 165;
        static final int TRANSACTION_grantDefaultPermissionsToEnabledTelephonyDataServices = 166;
        static final int TRANSACTION_grantRuntimePermission = 23;
        static final int TRANSACTION_hasSigningCertificate = 195;
        static final int TRANSACTION_hasSystemFeature = 109;
        static final int TRANSACTION_hasSystemUidErrors = 113;
        static final int TRANSACTION_hasUidSigningCertificate = 196;
        static final int TRANSACTION_installExistingPackageAsUser = 134;
        static final int TRANSACTION_isDeviceUpgrading = 147;
        static final int TRANSACTION_isFirstBoot = 145;
        static final int TRANSACTION_isInstantApp = 176;
        static final int TRANSACTION_isOnlyCoreApps = 146;
        static final int TRANSACTION_isPackageAvailable = 2;
        static final int TRANSACTION_isPackageDeviceAdminOnAnyUser = 182;
        static final int TRANSACTION_isPackageSignedByKeySet = 160;
        static final int TRANSACTION_isPackageSignedByKeySetExactly = 161;
        static final int TRANSACTION_isPackageStateProtected = 203;
        static final int TRANSACTION_isPackageSuspendedForUser = 83;
        static final int TRANSACTION_isPermissionEnforced = 149;
        static final int TRANSACTION_isPermissionRevokedByPolicy = 170;
        static final int TRANSACTION_isProtectedBroadcast = 33;
        static final int TRANSACTION_isSafeMode = 111;
        static final int TRANSACTION_isStorageLow = 150;
        static final int TRANSACTION_isUidPrivileged = 43;
        static final int TRANSACTION_logAppProcessStartIfNeeded = 97;
        static final int TRANSACTION_movePackage = 129;
        static final int TRANSACTION_movePrimaryStorage = 130;
        static final int TRANSACTION_notifyDexLoad = 117;
        static final int TRANSACTION_notifyPackageUse = 116;
        static final int TRANSACTION_notifyPackagesReplacedReceived = 209;
        static final int TRANSACTION_performDexOptMode = 119;
        static final int TRANSACTION_performDexOptSecondary = 120;
        static final int TRANSACTION_performFstrimIfNeeded = 114;
        static final int TRANSACTION_queryContentProviders = 60;
        static final int TRANSACTION_queryInstrumentation = 62;
        static final int TRANSACTION_queryIntentActivities = 48;
        static final int TRANSACTION_queryIntentActivityOptions = 49;
        static final int TRANSACTION_queryIntentContentProviders = 53;
        static final int TRANSACTION_queryIntentReceivers = 50;
        static final int TRANSACTION_queryIntentServices = 52;
        static final int TRANSACTION_queryPermissionsByGroup = 10;
        static final int TRANSACTION_querySyncProviders = 59;
        static final int TRANSACTION_reconcileSecondaryDexFiles = 125;
        static final int TRANSACTION_registerDexModule = 118;
        static final int TRANSACTION_registerMoveCallback = 127;
        static final int TRANSACTION_removeOnPermissionsChangeListener = 163;
        static final int TRANSACTION_removePermission = 22;
        static final int TRANSACTION_removeWhitelistedRestrictedPermission = 31;
        static final int TRANSACTION_replacePreferredActivity = 73;
        static final int TRANSACTION_resetApplicationPreferences = 69;
        static final int TRANSACTION_resetRuntimePermissions = 25;
        static final int TRANSACTION_resolveContentProvider = 58;
        static final int TRANSACTION_resolveIntent = 45;
        static final int TRANSACTION_resolveService = 51;
        static final int TRANSACTION_restoreDefaultApps = 88;
        static final int TRANSACTION_restoreIntentFilterVerification = 90;
        static final int TRANSACTION_restorePreferredActivities = 86;
        static final int TRANSACTION_revokeDefaultPermissionsFromDisabledTelephonyDataServices = 167;
        static final int TRANSACTION_revokeDefaultPermissionsFromLuiApps = 169;
        static final int TRANSACTION_revokeRuntimePermission = 24;
        static final int TRANSACTION_runBackgroundDexoptJob = 124;
        static final int TRANSACTION_sendDeviceCustomizationReadyBroadcast = 204;
        static final int TRANSACTION_setApplicationCategoryHint = 65;
        static final int TRANSACTION_setApplicationEnabledSetting = 95;
        static final int TRANSACTION_setApplicationHiddenSettingAsUser = 151;
        static final int TRANSACTION_setBlockUninstallForUser = 156;
        static final int TRANSACTION_setComponentEnabledSetting = 93;
        static final int TRANSACTION_setDefaultBrowserPackageName = 142;
        static final int TRANSACTION_setDistractingPackageRestrictionsAsUser = 80;
        static final int TRANSACTION_setHarmfulAppWarning = 193;
        static final int TRANSACTION_setHomeActivity = 92;
        static final int TRANSACTION_setInstallLocation = 132;
        static final int TRANSACTION_setInstallerPackageName = 64;
        static final int TRANSACTION_setInstantAppCookie = 174;
        static final int TRANSACTION_setLastChosenActivity = 71;
        static final int TRANSACTION_setPackageNotLaunched = 215;
        static final int TRANSACTION_setPackageStoppedState = 99;
        static final int TRANSACTION_setPackagesSuspendedAsUser = 81;
        static final int TRANSACTION_setPermissionEnforced = 148;
        static final int TRANSACTION_setRequiredForSystemUser = 177;
        static final int TRANSACTION_setRuntimePermissionsVersion = 208;
        static final int TRANSACTION_setSystemAppHiddenUntilInstalled = 153;
        static final int TRANSACTION_setSystemAppInstallState = 154;
        static final int TRANSACTION_setUpdateAvailable = 178;
        static final int TRANSACTION_setXpPackageInfo = 214;
        static final int TRANSACTION_shouldShowRequestPermissionRationale = 32;
        static final int TRANSACTION_systemReady = 112;
        static final int TRANSACTION_unregisterMoveCallback = 128;
        static final int TRANSACTION_updateAppScreenFlag = 216;
        static final int TRANSACTION_updateIntentVerificationStatus = 139;
        static final int TRANSACTION_updatePackagesIfNeeded = 115;
        static final int TRANSACTION_updatePermissionFlags = 27;
        static final int TRANSACTION_updatePermissionFlagsForAllApps = 28;
        static final int TRANSACTION_verifyIntentFilter = 137;
        static final int TRANSACTION_verifyPendingInstall = 135;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPackageManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IPackageManager)) {
                return (IPackageManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "checkPackageStartable";
                case 2:
                    return "isPackageAvailable";
                case 3:
                    return "getPackageInfo";
                case 4:
                    return "getPackageInfoVersioned";
                case 5:
                    return "getPackageUid";
                case 6:
                    return "getPackageGids";
                case 7:
                    return "currentToCanonicalPackageNames";
                case 8:
                    return "canonicalToCurrentPackageNames";
                case 9:
                    return "getPermissionInfo";
                case 10:
                    return "queryPermissionsByGroup";
                case 11:
                    return "getPermissionGroupInfo";
                case 12:
                    return "getAllPermissionGroups";
                case 13:
                    return "getApplicationInfo";
                case 14:
                    return "getActivityInfo";
                case 15:
                    return "activitySupportsIntent";
                case 16:
                    return "getReceiverInfo";
                case 17:
                    return "getServiceInfo";
                case 18:
                    return "getProviderInfo";
                case 19:
                    return "checkPermission";
                case 20:
                    return "checkUidPermission";
                case 21:
                    return "addPermission";
                case 22:
                    return "removePermission";
                case 23:
                    return "grantRuntimePermission";
                case 24:
                    return "revokeRuntimePermission";
                case 25:
                    return "resetRuntimePermissions";
                case 26:
                    return "getPermissionFlags";
                case 27:
                    return "updatePermissionFlags";
                case 28:
                    return "updatePermissionFlagsForAllApps";
                case 29:
                    return "getWhitelistedRestrictedPermissions";
                case 30:
                    return "addWhitelistedRestrictedPermission";
                case 31:
                    return "removeWhitelistedRestrictedPermission";
                case 32:
                    return "shouldShowRequestPermissionRationale";
                case 33:
                    return "isProtectedBroadcast";
                case 34:
                    return "checkSignatures";
                case 35:
                    return "checkUidSignatures";
                case 36:
                    return "getAllPackages";
                case 37:
                    return "getPackagesForUid";
                case 38:
                    return "getNameForUid";
                case 39:
                    return "getNamesForUids";
                case 40:
                    return "getUidForSharedUser";
                case 41:
                    return "getFlagsForUid";
                case 42:
                    return "getPrivateFlagsForUid";
                case 43:
                    return "isUidPrivileged";
                case 44:
                    return "getAppOpPermissionPackages";
                case 45:
                    return "resolveIntent";
                case 46:
                    return "findPersistentPreferredActivity";
                case 47:
                    return "canForwardTo";
                case 48:
                    return "queryIntentActivities";
                case 49:
                    return "queryIntentActivityOptions";
                case 50:
                    return "queryIntentReceivers";
                case 51:
                    return "resolveService";
                case 52:
                    return "queryIntentServices";
                case 53:
                    return "queryIntentContentProviders";
                case 54:
                    return "getInstalledPackages";
                case 55:
                    return "getPackagesHoldingPermissions";
                case 56:
                    return "getInstalledApplications";
                case 57:
                    return "getPersistentApplications";
                case 58:
                    return "resolveContentProvider";
                case 59:
                    return "querySyncProviders";
                case 60:
                    return "queryContentProviders";
                case 61:
                    return "getInstrumentationInfo";
                case 62:
                    return "queryInstrumentation";
                case 63:
                    return "finishPackageInstall";
                case 64:
                    return "setInstallerPackageName";
                case 65:
                    return "setApplicationCategoryHint";
                case 66:
                    return "deletePackageAsUser";
                case 67:
                    return "deletePackageVersioned";
                case 68:
                    return "getInstallerPackageName";
                case 69:
                    return "resetApplicationPreferences";
                case 70:
                    return "getLastChosenActivity";
                case 71:
                    return "setLastChosenActivity";
                case 72:
                    return "addPreferredActivity";
                case 73:
                    return "replacePreferredActivity";
                case 74:
                    return "clearPackagePreferredActivities";
                case 75:
                    return "getPreferredActivities";
                case 76:
                    return "addPersistentPreferredActivity";
                case 77:
                    return "clearPackagePersistentPreferredActivities";
                case 78:
                    return "addCrossProfileIntentFilter";
                case 79:
                    return "clearCrossProfileIntentFilters";
                case 80:
                    return "setDistractingPackageRestrictionsAsUser";
                case 81:
                    return "setPackagesSuspendedAsUser";
                case 82:
                    return "getUnsuspendablePackagesForUser";
                case 83:
                    return "isPackageSuspendedForUser";
                case 84:
                    return "getSuspendedPackageAppExtras";
                case 85:
                    return "getPreferredActivityBackup";
                case 86:
                    return "restorePreferredActivities";
                case 87:
                    return "getDefaultAppsBackup";
                case 88:
                    return "restoreDefaultApps";
                case 89:
                    return "getIntentFilterVerificationBackup";
                case 90:
                    return "restoreIntentFilterVerification";
                case 91:
                    return "getHomeActivities";
                case 92:
                    return "setHomeActivity";
                case 93:
                    return "setComponentEnabledSetting";
                case 94:
                    return "getComponentEnabledSetting";
                case 95:
                    return "setApplicationEnabledSetting";
                case 96:
                    return "getApplicationEnabledSetting";
                case 97:
                    return "logAppProcessStartIfNeeded";
                case 98:
                    return "flushPackageRestrictionsAsUser";
                case 99:
                    return "setPackageStoppedState";
                case 100:
                    return "freeStorageAndNotify";
                case 101:
                    return "freeStorage";
                case 102:
                    return "deleteApplicationCacheFiles";
                case 103:
                    return "deleteApplicationCacheFilesAsUser";
                case 104:
                    return "clearApplicationUserData";
                case 105:
                    return "clearApplicationProfileData";
                case 106:
                    return "getPackageSizeInfo";
                case 107:
                    return "getSystemSharedLibraryNames";
                case 108:
                    return "getSystemAvailableFeatures";
                case 109:
                    return "hasSystemFeature";
                case 110:
                    return "enterSafeMode";
                case 111:
                    return "isSafeMode";
                case 112:
                    return "systemReady";
                case 113:
                    return "hasSystemUidErrors";
                case 114:
                    return "performFstrimIfNeeded";
                case 115:
                    return "updatePackagesIfNeeded";
                case 116:
                    return "notifyPackageUse";
                case 117:
                    return "notifyDexLoad";
                case 118:
                    return "registerDexModule";
                case 119:
                    return "performDexOptMode";
                case 120:
                    return "performDexOptSecondary";
                case 121:
                    return "compileLayouts";
                case 122:
                    return "dumpProfiles";
                case 123:
                    return "forceDexOpt";
                case 124:
                    return "runBackgroundDexoptJob";
                case 125:
                    return "reconcileSecondaryDexFiles";
                case 126:
                    return "getMoveStatus";
                case 127:
                    return "registerMoveCallback";
                case 128:
                    return "unregisterMoveCallback";
                case 129:
                    return "movePackage";
                case 130:
                    return "movePrimaryStorage";
                case 131:
                    return "addPermissionAsync";
                case 132:
                    return "setInstallLocation";
                case 133:
                    return "getInstallLocation";
                case 134:
                    return "installExistingPackageAsUser";
                case 135:
                    return "verifyPendingInstall";
                case 136:
                    return "extendVerificationTimeout";
                case 137:
                    return "verifyIntentFilter";
                case 138:
                    return "getIntentVerificationStatus";
                case 139:
                    return "updateIntentVerificationStatus";
                case 140:
                    return "getIntentFilterVerifications";
                case 141:
                    return "getAllIntentFilters";
                case 142:
                    return "setDefaultBrowserPackageName";
                case 143:
                    return "getDefaultBrowserPackageName";
                case 144:
                    return "getVerifierDeviceIdentity";
                case 145:
                    return "isFirstBoot";
                case 146:
                    return "isOnlyCoreApps";
                case 147:
                    return "isDeviceUpgrading";
                case 148:
                    return "setPermissionEnforced";
                case 149:
                    return "isPermissionEnforced";
                case 150:
                    return "isStorageLow";
                case 151:
                    return "setApplicationHiddenSettingAsUser";
                case 152:
                    return "getApplicationHiddenSettingAsUser";
                case 153:
                    return "setSystemAppHiddenUntilInstalled";
                case 154:
                    return "setSystemAppInstallState";
                case 155:
                    return "getPackageInstaller";
                case 156:
                    return "setBlockUninstallForUser";
                case 157:
                    return "getBlockUninstallForUser";
                case 158:
                    return "getKeySetByAlias";
                case 159:
                    return "getSigningKeySet";
                case 160:
                    return "isPackageSignedByKeySet";
                case 161:
                    return "isPackageSignedByKeySetExactly";
                case 162:
                    return "addOnPermissionsChangeListener";
                case 163:
                    return "removeOnPermissionsChangeListener";
                case 164:
                    return "grantDefaultPermissionsToEnabledCarrierApps";
                case 165:
                    return "grantDefaultPermissionsToEnabledImsServices";
                case 166:
                    return "grantDefaultPermissionsToEnabledTelephonyDataServices";
                case 167:
                    return "revokeDefaultPermissionsFromDisabledTelephonyDataServices";
                case 168:
                    return "grantDefaultPermissionsToActiveLuiApp";
                case 169:
                    return "revokeDefaultPermissionsFromLuiApps";
                case 170:
                    return "isPermissionRevokedByPolicy";
                case 171:
                    return "getPermissionControllerPackageName";
                case 172:
                    return "getInstantApps";
                case 173:
                    return "getInstantAppCookie";
                case 174:
                    return "setInstantAppCookie";
                case 175:
                    return "getInstantAppIcon";
                case 176:
                    return "isInstantApp";
                case 177:
                    return "setRequiredForSystemUser";
                case 178:
                    return "setUpdateAvailable";
                case 179:
                    return "getServicesSystemSharedLibraryPackageName";
                case 180:
                    return "getSharedSystemSharedLibraryPackageName";
                case 181:
                    return "getChangedPackages";
                case 182:
                    return "isPackageDeviceAdminOnAnyUser";
                case 183:
                    return "getInstallReason";
                case 184:
                    return "getSharedLibraries";
                case 185:
                    return "getDeclaredSharedLibraries";
                case 186:
                    return "canRequestPackageInstalls";
                case 187:
                    return "deletePreloadsFileCache";
                case 188:
                    return "getInstantAppResolverComponent";
                case 189:
                    return "getInstantAppResolverSettingsComponent";
                case 190:
                    return "getInstantAppInstallerComponent";
                case 191:
                    return "getInstantAppAndroidId";
                case 192:
                    return "getArtManager";
                case 193:
                    return "setHarmfulAppWarning";
                case 194:
                    return "getHarmfulAppWarning";
                case 195:
                    return "hasSigningCertificate";
                case 196:
                    return "hasUidSigningCertificate";
                case 197:
                    return "getSystemTextClassifierPackageName";
                case 198:
                    return "getAttentionServicePackageName";
                case 199:
                    return "getWellbeingPackageName";
                case 200:
                    return "getAppPredictionServicePackageName";
                case 201:
                    return "getSystemCaptionsServicePackageName";
                case 202:
                    return "getIncidentReportApproverPackageName";
                case 203:
                    return "isPackageStateProtected";
                case 204:
                    return "sendDeviceCustomizationReadyBroadcast";
                case 205:
                    return "getInstalledModules";
                case 206:
                    return "getModuleInfo";
                case 207:
                    return "getRuntimePermissionsVersion";
                case 208:
                    return "setRuntimePermissionsVersion";
                case 209:
                    return "notifyPackagesReplacedReceived";
                case 210:
                    return "getSplitPermissions";
                case 211:
                    return "getOverlayApplicationInfo";
                case 212:
                    return "getOverlayActivityInfo";
                case 213:
                    return "getXpPackageInfo";
                case 214:
                    return "setXpPackageInfo";
                case 215:
                    return "setPackageNotLaunched";
                case 216:
                    return "updateAppScreenFlag";
                case 217:
                    return "getAllXpPackageInfos";
                case 218:
                    return "getXpAppPackageList";
                case 219:
                    return "getXpAppIcon";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            VersionedPackage _arg0;
            ComponentName _arg02;
            ComponentName _arg03;
            Intent _arg1;
            ComponentName _arg04;
            ComponentName _arg05;
            ComponentName _arg06;
            PermissionInfo _arg07;
            Intent _arg08;
            Intent _arg09;
            Intent _arg010;
            Intent _arg011;
            ComponentName _arg012;
            Intent _arg3;
            Intent _arg013;
            Intent _arg014;
            Intent _arg015;
            Intent _arg016;
            ComponentName _arg017;
            VersionedPackage _arg018;
            Intent _arg019;
            Intent _arg020;
            IntentFilter _arg32;
            ComponentName _arg5;
            IntentFilter _arg021;
            ComponentName _arg33;
            IntentFilter _arg022;
            ComponentName _arg34;
            IntentFilter _arg023;
            ComponentName _arg12;
            IntentFilter _arg024;
            PersistableBundle _arg2;
            PersistableBundle _arg35;
            SuspendDialogInfo _arg4;
            ComponentName _arg025;
            ComponentName _arg026;
            ComponentName _arg027;
            IntentSender _arg36;
            PermissionInfo _arg028;
            KeySet _arg13;
            KeySet _arg14;
            CharSequence _arg15;
            ApplicationInfo _arg029;
            ActivityInfo _arg030;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg031 = data.readString();
                    int _arg16 = data.readInt();
                    checkPackageStartable(_arg031, _arg16);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    int _arg17 = data.readInt();
                    boolean isPackageAvailable = isPackageAvailable(_arg032, _arg17);
                    reply.writeNoException();
                    reply.writeInt(isPackageAvailable ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg033 = data.readString();
                    int _arg18 = data.readInt();
                    int _arg22 = data.readInt();
                    PackageInfo _result = getPackageInfo(_arg033, _arg18, _arg22);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = VersionedPackage.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    int _arg19 = data.readInt();
                    int _arg23 = data.readInt();
                    PackageInfo _result2 = getPackageInfoVersioned(_arg0, _arg19, _arg23);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg034 = data.readString();
                    int _arg110 = data.readInt();
                    int _arg24 = data.readInt();
                    int _result3 = getPackageUid(_arg034, _arg110, _arg24);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg035 = data.readString();
                    int _arg111 = data.readInt();
                    int _arg25 = data.readInt();
                    int[] _result4 = getPackageGids(_arg035, _arg111, _arg25);
                    reply.writeNoException();
                    reply.writeIntArray(_result4);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg036 = data.createStringArray();
                    String[] _result5 = currentToCanonicalPackageNames(_arg036);
                    reply.writeNoException();
                    reply.writeStringArray(_result5);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg037 = data.createStringArray();
                    String[] _result6 = canonicalToCurrentPackageNames(_arg037);
                    reply.writeNoException();
                    reply.writeStringArray(_result6);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg038 = data.readString();
                    String _arg112 = data.readString();
                    int _arg26 = data.readInt();
                    PermissionInfo _result7 = getPermissionInfo(_arg038, _arg112, _arg26);
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg039 = data.readString();
                    int _arg113 = data.readInt();
                    ParceledListSlice _result8 = queryPermissionsByGroup(_arg039, _arg113);
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg040 = data.readString();
                    int _arg114 = data.readInt();
                    PermissionGroupInfo _result9 = getPermissionGroupInfo(_arg040, _arg114);
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg041 = data.readInt();
                    ParceledListSlice _result10 = getAllPermissionGroups(_arg041);
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg042 = data.readString();
                    int _arg115 = data.readInt();
                    int _arg27 = data.readInt();
                    ApplicationInfo _result11 = getApplicationInfo(_arg042, _arg115, _arg27);
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    int _arg116 = data.readInt();
                    int _arg28 = data.readInt();
                    ActivityInfo _result12 = getActivityInfo(_arg02, _arg116, _arg28);
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    String _arg29 = data.readString();
                    boolean activitySupportsIntent = activitySupportsIntent(_arg03, _arg1, _arg29);
                    reply.writeNoException();
                    reply.writeInt(activitySupportsIntent ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    int _arg117 = data.readInt();
                    int _arg210 = data.readInt();
                    ActivityInfo _result13 = getReceiverInfo(_arg04, _arg117, _arg210);
                    reply.writeNoException();
                    if (_result13 != null) {
                        reply.writeInt(1);
                        _result13.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    int _arg118 = data.readInt();
                    int _arg211 = data.readInt();
                    ServiceInfo _result14 = getServiceInfo(_arg05, _arg118, _arg211);
                    reply.writeNoException();
                    if (_result14 != null) {
                        reply.writeInt(1);
                        _result14.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    int _arg119 = data.readInt();
                    int _arg212 = data.readInt();
                    ProviderInfo _result15 = getProviderInfo(_arg06, _arg119, _arg212);
                    reply.writeNoException();
                    if (_result15 != null) {
                        reply.writeInt(1);
                        _result15.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg043 = data.readString();
                    String _arg120 = data.readString();
                    int _arg213 = data.readInt();
                    int _result16 = checkPermission(_arg043, _arg120, _arg213);
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg044 = data.readString();
                    int _arg121 = data.readInt();
                    int _result17 = checkUidPermission(_arg044, _arg121);
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = PermissionInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    boolean addPermission = addPermission(_arg07);
                    reply.writeNoException();
                    reply.writeInt(addPermission ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg045 = data.readString();
                    removePermission(_arg045);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg046 = data.readString();
                    String _arg122 = data.readString();
                    int _arg214 = data.readInt();
                    grantRuntimePermission(_arg046, _arg122, _arg214);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg047 = data.readString();
                    String _arg123 = data.readString();
                    int _arg215 = data.readInt();
                    revokeRuntimePermission(_arg047, _arg123, _arg215);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    resetRuntimePermissions();
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg048 = data.readString();
                    String _arg124 = data.readString();
                    int _arg216 = data.readInt();
                    int _result18 = getPermissionFlags(_arg048, _arg124, _arg216);
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg049 = data.readString();
                    String _arg125 = data.readString();
                    int _arg217 = data.readInt();
                    int _arg37 = data.readInt();
                    boolean _arg42 = data.readInt() != 0;
                    int _arg52 = data.readInt();
                    updatePermissionFlags(_arg049, _arg125, _arg217, _arg37, _arg42, _arg52);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg050 = data.readInt();
                    int _arg126 = data.readInt();
                    int _arg218 = data.readInt();
                    updatePermissionFlagsForAllApps(_arg050, _arg126, _arg218);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg051 = data.readString();
                    int _arg127 = data.readInt();
                    int _arg219 = data.readInt();
                    List<String> _result19 = getWhitelistedRestrictedPermissions(_arg051, _arg127, _arg219);
                    reply.writeNoException();
                    reply.writeStringList(_result19);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg052 = data.readString();
                    String _arg128 = data.readString();
                    int _arg220 = data.readInt();
                    int _arg38 = data.readInt();
                    boolean addWhitelistedRestrictedPermission = addWhitelistedRestrictedPermission(_arg052, _arg128, _arg220, _arg38);
                    reply.writeNoException();
                    reply.writeInt(addWhitelistedRestrictedPermission ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg053 = data.readString();
                    String _arg129 = data.readString();
                    int _arg221 = data.readInt();
                    int _arg39 = data.readInt();
                    boolean removeWhitelistedRestrictedPermission = removeWhitelistedRestrictedPermission(_arg053, _arg129, _arg221, _arg39);
                    reply.writeNoException();
                    reply.writeInt(removeWhitelistedRestrictedPermission ? 1 : 0);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg054 = data.readString();
                    String _arg130 = data.readString();
                    int _arg222 = data.readInt();
                    boolean shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale(_arg054, _arg130, _arg222);
                    reply.writeNoException();
                    reply.writeInt(shouldShowRequestPermissionRationale ? 1 : 0);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg055 = data.readString();
                    boolean isProtectedBroadcast = isProtectedBroadcast(_arg055);
                    reply.writeNoException();
                    reply.writeInt(isProtectedBroadcast ? 1 : 0);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg056 = data.readString();
                    String _arg131 = data.readString();
                    int _result20 = checkSignatures(_arg056, _arg131);
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg057 = data.readInt();
                    int _arg132 = data.readInt();
                    int _result21 = checkUidSignatures(_arg057, _arg132);
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result22 = getAllPackages();
                    reply.writeNoException();
                    reply.writeStringList(_result22);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg058 = data.readInt();
                    String[] _result23 = getPackagesForUid(_arg058);
                    reply.writeNoException();
                    reply.writeStringArray(_result23);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg059 = data.readInt();
                    String _result24 = getNameForUid(_arg059);
                    reply.writeNoException();
                    reply.writeString(_result24);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg060 = data.createIntArray();
                    String[] _result25 = getNamesForUids(_arg060);
                    reply.writeNoException();
                    reply.writeStringArray(_result25);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg061 = data.readString();
                    int _result26 = getUidForSharedUser(_arg061);
                    reply.writeNoException();
                    reply.writeInt(_result26);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg062 = data.readInt();
                    int _result27 = getFlagsForUid(_arg062);
                    reply.writeNoException();
                    reply.writeInt(_result27);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg063 = data.readInt();
                    int _result28 = getPrivateFlagsForUid(_arg063);
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg064 = data.readInt();
                    boolean isUidPrivileged = isUidPrivileged(_arg064);
                    reply.writeNoException();
                    reply.writeInt(isUidPrivileged ? 1 : 0);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg065 = data.readString();
                    String[] _result29 = getAppOpPermissionPackages(_arg065);
                    reply.writeNoException();
                    reply.writeStringArray(_result29);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    String _arg133 = data.readString();
                    int _arg223 = data.readInt();
                    int _arg310 = data.readInt();
                    ResolveInfo _result30 = resolveIntent(_arg08, _arg133, _arg223, _arg310);
                    reply.writeNoException();
                    if (_result30 != null) {
                        reply.writeInt(1);
                        _result30.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg09 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg09 = null;
                    }
                    int _arg134 = data.readInt();
                    ResolveInfo _result31 = findPersistentPreferredActivity(_arg09, _arg134);
                    reply.writeNoException();
                    if (_result31 != null) {
                        reply.writeInt(1);
                        _result31.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg010 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg010 = null;
                    }
                    String _arg135 = data.readString();
                    int _arg224 = data.readInt();
                    int _arg311 = data.readInt();
                    boolean canForwardTo = canForwardTo(_arg010, _arg135, _arg224, _arg311);
                    reply.writeNoException();
                    reply.writeInt(canForwardTo ? 1 : 0);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg011 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg011 = null;
                    }
                    String _arg136 = data.readString();
                    int _arg225 = data.readInt();
                    int _arg312 = data.readInt();
                    ParceledListSlice _result32 = queryIntentActivities(_arg011, _arg136, _arg225, _arg312);
                    reply.writeNoException();
                    if (_result32 != null) {
                        reply.writeInt(1);
                        _result32.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg012 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg012 = null;
                    }
                    Intent[] _arg137 = (Intent[]) data.createTypedArray(Intent.CREATOR);
                    String[] _arg226 = data.createStringArray();
                    if (data.readInt() != 0) {
                        _arg3 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    String _arg43 = data.readString();
                    int _arg53 = data.readInt();
                    int _arg6 = data.readInt();
                    ParceledListSlice _result33 = queryIntentActivityOptions(_arg012, _arg137, _arg226, _arg3, _arg43, _arg53, _arg6);
                    reply.writeNoException();
                    if (_result33 != null) {
                        reply.writeInt(1);
                        _result33.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg013 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg013 = null;
                    }
                    String _arg138 = data.readString();
                    int _arg227 = data.readInt();
                    int _arg313 = data.readInt();
                    ParceledListSlice _result34 = queryIntentReceivers(_arg013, _arg138, _arg227, _arg313);
                    reply.writeNoException();
                    if (_result34 != null) {
                        reply.writeInt(1);
                        _result34.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg014 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg014 = null;
                    }
                    String _arg139 = data.readString();
                    int _arg228 = data.readInt();
                    int _arg314 = data.readInt();
                    ResolveInfo _result35 = resolveService(_arg014, _arg139, _arg228, _arg314);
                    reply.writeNoException();
                    if (_result35 != null) {
                        reply.writeInt(1);
                        _result35.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg015 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg015 = null;
                    }
                    String _arg140 = data.readString();
                    int _arg229 = data.readInt();
                    int _arg315 = data.readInt();
                    ParceledListSlice _result36 = queryIntentServices(_arg015, _arg140, _arg229, _arg315);
                    reply.writeNoException();
                    if (_result36 != null) {
                        reply.writeInt(1);
                        _result36.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg016 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg016 = null;
                    }
                    String _arg141 = data.readString();
                    int _arg230 = data.readInt();
                    int _arg316 = data.readInt();
                    ParceledListSlice _result37 = queryIntentContentProviders(_arg016, _arg141, _arg230, _arg316);
                    reply.writeNoException();
                    if (_result37 != null) {
                        reply.writeInt(1);
                        _result37.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg066 = data.readInt();
                    int _arg142 = data.readInt();
                    ParceledListSlice _result38 = getInstalledPackages(_arg066, _arg142);
                    reply.writeNoException();
                    if (_result38 != null) {
                        reply.writeInt(1);
                        _result38.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg067 = data.createStringArray();
                    int _arg143 = data.readInt();
                    int _arg231 = data.readInt();
                    ParceledListSlice _result39 = getPackagesHoldingPermissions(_arg067, _arg143, _arg231);
                    reply.writeNoException();
                    if (_result39 != null) {
                        reply.writeInt(1);
                        _result39.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg068 = data.readInt();
                    int _arg144 = data.readInt();
                    ParceledListSlice _result40 = getInstalledApplications(_arg068, _arg144);
                    reply.writeNoException();
                    if (_result40 != null) {
                        reply.writeInt(1);
                        _result40.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg069 = data.readInt();
                    ParceledListSlice _result41 = getPersistentApplications(_arg069);
                    reply.writeNoException();
                    if (_result41 != null) {
                        reply.writeInt(1);
                        _result41.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg070 = data.readString();
                    int _arg145 = data.readInt();
                    int _arg232 = data.readInt();
                    ProviderInfo _result42 = resolveContentProvider(_arg070, _arg145, _arg232);
                    reply.writeNoException();
                    if (_result42 != null) {
                        reply.writeInt(1);
                        _result42.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg071 = data.createStringArrayList();
                    ArrayList createTypedArrayList = data.createTypedArrayList(ProviderInfo.CREATOR);
                    querySyncProviders(_arg071, createTypedArrayList);
                    reply.writeNoException();
                    reply.writeStringList(_arg071);
                    reply.writeTypedList(createTypedArrayList);
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg072 = data.readString();
                    int _arg146 = data.readInt();
                    int _arg233 = data.readInt();
                    String _arg317 = data.readString();
                    ParceledListSlice _result43 = queryContentProviders(_arg072, _arg146, _arg233, _arg317);
                    reply.writeNoException();
                    if (_result43 != null) {
                        reply.writeInt(1);
                        _result43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg017 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg017 = null;
                    }
                    int _arg147 = data.readInt();
                    InstrumentationInfo _result44 = getInstrumentationInfo(_arg017, _arg147);
                    reply.writeNoException();
                    if (_result44 != null) {
                        reply.writeInt(1);
                        _result44.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg073 = data.readString();
                    int _arg148 = data.readInt();
                    ParceledListSlice _result45 = queryInstrumentation(_arg073, _arg148);
                    reply.writeNoException();
                    if (_result45 != null) {
                        reply.writeInt(1);
                        _result45.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg074 = data.readInt();
                    boolean _arg149 = data.readInt() != 0;
                    finishPackageInstall(_arg074, _arg149);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg075 = data.readString();
                    String _arg150 = data.readString();
                    setInstallerPackageName(_arg075, _arg150);
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg076 = data.readString();
                    int _arg151 = data.readInt();
                    String _arg234 = data.readString();
                    setApplicationCategoryHint(_arg076, _arg151, _arg234);
                    reply.writeNoException();
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg077 = data.readString();
                    int _arg152 = data.readInt();
                    IPackageDeleteObserver _arg235 = IPackageDeleteObserver.Stub.asInterface(data.readStrongBinder());
                    int _arg318 = data.readInt();
                    int _arg44 = data.readInt();
                    deletePackageAsUser(_arg077, _arg152, _arg235, _arg318, _arg44);
                    reply.writeNoException();
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg018 = VersionedPackage.CREATOR.createFromParcel(data);
                    } else {
                        _arg018 = null;
                    }
                    IPackageDeleteObserver2 _arg153 = IPackageDeleteObserver2.Stub.asInterface(data.readStrongBinder());
                    int _arg236 = data.readInt();
                    int _arg319 = data.readInt();
                    deletePackageVersioned(_arg018, _arg153, _arg236, _arg319);
                    reply.writeNoException();
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg078 = data.readString();
                    String _result46 = getInstallerPackageName(_arg078);
                    reply.writeNoException();
                    reply.writeString(_result46);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg079 = data.readInt();
                    resetApplicationPreferences(_arg079);
                    reply.writeNoException();
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg019 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg019 = null;
                    }
                    String _arg154 = data.readString();
                    int _arg237 = data.readInt();
                    ResolveInfo _result47 = getLastChosenActivity(_arg019, _arg154, _arg237);
                    reply.writeNoException();
                    if (_result47 != null) {
                        reply.writeInt(1);
                        _result47.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg020 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg020 = null;
                    }
                    String _arg155 = data.readString();
                    int _arg238 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg32 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    int _arg45 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg5 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    setLastChosenActivity(_arg020, _arg155, _arg238, _arg32, _arg45, _arg5);
                    reply.writeNoException();
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg021 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg021 = null;
                    }
                    int _arg156 = data.readInt();
                    ComponentName[] _arg239 = (ComponentName[]) data.createTypedArray(ComponentName.CREATOR);
                    if (data.readInt() != 0) {
                        _arg33 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg33 = null;
                    }
                    int _arg46 = data.readInt();
                    addPreferredActivity(_arg021, _arg156, _arg239, _arg33, _arg46);
                    reply.writeNoException();
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg022 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg022 = null;
                    }
                    int _arg157 = data.readInt();
                    ComponentName[] _arg240 = (ComponentName[]) data.createTypedArray(ComponentName.CREATOR);
                    if (data.readInt() != 0) {
                        _arg34 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg34 = null;
                    }
                    int _arg47 = data.readInt();
                    replacePreferredActivity(_arg022, _arg157, _arg240, _arg34, _arg47);
                    reply.writeNoException();
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg080 = data.readString();
                    clearPackagePreferredActivities(_arg080);
                    reply.writeNoException();
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    String _arg241 = data.readString();
                    int _result48 = getPreferredActivities(arrayList, arrayList2, _arg241);
                    reply.writeNoException();
                    reply.writeInt(_result48);
                    reply.writeTypedList(arrayList);
                    reply.writeTypedList(arrayList2);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg023 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg023 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg12 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    int _arg242 = data.readInt();
                    addPersistentPreferredActivity(_arg023, _arg12, _arg242);
                    reply.writeNoException();
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg081 = data.readString();
                    int _arg158 = data.readInt();
                    clearPackagePersistentPreferredActivities(_arg081, _arg158);
                    reply.writeNoException();
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg024 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg024 = null;
                    }
                    String _arg159 = data.readString();
                    int _arg243 = data.readInt();
                    int _arg320 = data.readInt();
                    int _arg48 = data.readInt();
                    addCrossProfileIntentFilter(_arg024, _arg159, _arg243, _arg320, _arg48);
                    reply.writeNoException();
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg082 = data.readInt();
                    String _arg160 = data.readString();
                    clearCrossProfileIntentFilters(_arg082, _arg160);
                    reply.writeNoException();
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg083 = data.createStringArray();
                    int _arg161 = data.readInt();
                    int _arg244 = data.readInt();
                    String[] _result49 = setDistractingPackageRestrictionsAsUser(_arg083, _arg161, _arg244);
                    reply.writeNoException();
                    reply.writeStringArray(_result49);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg084 = data.createStringArray();
                    boolean _arg162 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        _arg2 = PersistableBundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg35 = PersistableBundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg35 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg4 = SuspendDialogInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    String _arg54 = data.readString();
                    int _arg62 = data.readInt();
                    String[] _result50 = setPackagesSuspendedAsUser(_arg084, _arg162, _arg2, _arg35, _arg4, _arg54, _arg62);
                    reply.writeNoException();
                    reply.writeStringArray(_result50);
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg085 = data.createStringArray();
                    int _arg163 = data.readInt();
                    String[] _result51 = getUnsuspendablePackagesForUser(_arg085, _arg163);
                    reply.writeNoException();
                    reply.writeStringArray(_result51);
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg086 = data.readString();
                    int _arg164 = data.readInt();
                    boolean isPackageSuspendedForUser = isPackageSuspendedForUser(_arg086, _arg164);
                    reply.writeNoException();
                    reply.writeInt(isPackageSuspendedForUser ? 1 : 0);
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg087 = data.readString();
                    int _arg165 = data.readInt();
                    PersistableBundle _result52 = getSuspendedPackageAppExtras(_arg087, _arg165);
                    reply.writeNoException();
                    if (_result52 != null) {
                        reply.writeInt(1);
                        _result52.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg088 = data.readInt();
                    byte[] _result53 = getPreferredActivityBackup(_arg088);
                    reply.writeNoException();
                    reply.writeByteArray(_result53);
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg089 = data.createByteArray();
                    int _arg166 = data.readInt();
                    restorePreferredActivities(_arg089, _arg166);
                    reply.writeNoException();
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg090 = data.readInt();
                    byte[] _result54 = getDefaultAppsBackup(_arg090);
                    reply.writeNoException();
                    reply.writeByteArray(_result54);
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg091 = data.createByteArray();
                    int _arg167 = data.readInt();
                    restoreDefaultApps(_arg091, _arg167);
                    reply.writeNoException();
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg092 = data.readInt();
                    byte[] _result55 = getIntentFilterVerificationBackup(_arg092);
                    reply.writeNoException();
                    reply.writeByteArray(_result55);
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg093 = data.createByteArray();
                    int _arg168 = data.readInt();
                    restoreIntentFilterVerification(_arg093, _arg168);
                    reply.writeNoException();
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    ArrayList arrayList3 = new ArrayList();
                    ComponentName _result56 = getHomeActivities(arrayList3);
                    reply.writeNoException();
                    if (_result56 != null) {
                        reply.writeInt(1);
                        _result56.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    reply.writeTypedList(arrayList3);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg025 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg025 = null;
                    }
                    int _arg169 = data.readInt();
                    setHomeActivity(_arg025, _arg169);
                    reply.writeNoException();
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg026 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg026 = null;
                    }
                    int _arg170 = data.readInt();
                    int _arg245 = data.readInt();
                    int _arg321 = data.readInt();
                    setComponentEnabledSetting(_arg026, _arg170, _arg245, _arg321);
                    reply.writeNoException();
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg027 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg027 = null;
                    }
                    int _arg171 = data.readInt();
                    int _result57 = getComponentEnabledSetting(_arg027, _arg171);
                    reply.writeNoException();
                    reply.writeInt(_result57);
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg094 = data.readString();
                    int _arg172 = data.readInt();
                    int _arg246 = data.readInt();
                    int _arg322 = data.readInt();
                    String _arg49 = data.readString();
                    setApplicationEnabledSetting(_arg094, _arg172, _arg246, _arg322, _arg49);
                    reply.writeNoException();
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg095 = data.readString();
                    int _arg173 = data.readInt();
                    int _result58 = getApplicationEnabledSetting(_arg095, _arg173);
                    reply.writeNoException();
                    reply.writeInt(_result58);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg096 = data.readString();
                    int _arg174 = data.readInt();
                    String _arg247 = data.readString();
                    String _arg323 = data.readString();
                    int _arg410 = data.readInt();
                    logAppProcessStartIfNeeded(_arg096, _arg174, _arg247, _arg323, _arg410);
                    reply.writeNoException();
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg097 = data.readInt();
                    flushPackageRestrictionsAsUser(_arg097);
                    reply.writeNoException();
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg098 = data.readString();
                    boolean _arg175 = data.readInt() != 0;
                    int _arg248 = data.readInt();
                    setPackageStoppedState(_arg098, _arg175, _arg248);
                    reply.writeNoException();
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg099 = data.readString();
                    long _arg176 = data.readLong();
                    int _arg249 = data.readInt();
                    IPackageDataObserver _arg324 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    freeStorageAndNotify(_arg099, _arg176, _arg249, _arg324);
                    reply.writeNoException();
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0100 = data.readString();
                    long _arg177 = data.readLong();
                    int _arg250 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg36 = IntentSender.CREATOR.createFromParcel(data);
                    } else {
                        _arg36 = null;
                    }
                    freeStorage(_arg0100, _arg177, _arg250, _arg36);
                    reply.writeNoException();
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0101 = data.readString();
                    IPackageDataObserver _arg178 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    deleteApplicationCacheFiles(_arg0101, _arg178);
                    reply.writeNoException();
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0102 = data.readString();
                    int _arg179 = data.readInt();
                    IPackageDataObserver _arg251 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    deleteApplicationCacheFilesAsUser(_arg0102, _arg179, _arg251);
                    reply.writeNoException();
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0103 = data.readString();
                    IPackageDataObserver _arg180 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    int _arg252 = data.readInt();
                    clearApplicationUserData(_arg0103, _arg180, _arg252);
                    reply.writeNoException();
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0104 = data.readString();
                    clearApplicationProfileData(_arg0104);
                    reply.writeNoException();
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0105 = data.readString();
                    int _arg181 = data.readInt();
                    IPackageStatsObserver _arg253 = IPackageStatsObserver.Stub.asInterface(data.readStrongBinder());
                    getPackageSizeInfo(_arg0105, _arg181, _arg253);
                    reply.writeNoException();
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result59 = getSystemSharedLibraryNames();
                    reply.writeNoException();
                    reply.writeStringArray(_result59);
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    ParceledListSlice _result60 = getSystemAvailableFeatures();
                    reply.writeNoException();
                    if (_result60 != null) {
                        reply.writeInt(1);
                        _result60.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0106 = data.readString();
                    int _arg182 = data.readInt();
                    boolean hasSystemFeature = hasSystemFeature(_arg0106, _arg182);
                    reply.writeNoException();
                    reply.writeInt(hasSystemFeature ? 1 : 0);
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    enterSafeMode();
                    reply.writeNoException();
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSafeMode = isSafeMode();
                    reply.writeNoException();
                    reply.writeInt(isSafeMode ? 1 : 0);
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    systemReady();
                    reply.writeNoException();
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasSystemUidErrors = hasSystemUidErrors();
                    reply.writeNoException();
                    reply.writeInt(hasSystemUidErrors ? 1 : 0);
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    performFstrimIfNeeded();
                    reply.writeNoException();
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    updatePackagesIfNeeded();
                    reply.writeNoException();
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0107 = data.readString();
                    int _arg183 = data.readInt();
                    notifyPackageUse(_arg0107, _arg183);
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0108 = data.readString();
                    List<String> _arg184 = data.createStringArrayList();
                    List<String> _arg254 = data.createStringArrayList();
                    String _arg325 = data.readString();
                    notifyDexLoad(_arg0108, _arg184, _arg254, _arg325);
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0109 = data.readString();
                    String _arg185 = data.readString();
                    boolean _arg255 = data.readInt() != 0;
                    IDexModuleRegisterCallback _arg326 = IDexModuleRegisterCallback.Stub.asInterface(data.readStrongBinder());
                    registerDexModule(_arg0109, _arg185, _arg255, _arg326);
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0110 = data.readString();
                    boolean _arg186 = data.readInt() != 0;
                    String _arg256 = data.readString();
                    boolean _arg327 = data.readInt() != 0;
                    boolean _arg411 = data.readInt() != 0;
                    String _arg55 = data.readString();
                    boolean performDexOptMode = performDexOptMode(_arg0110, _arg186, _arg256, _arg327, _arg411, _arg55);
                    reply.writeNoException();
                    reply.writeInt(performDexOptMode ? 1 : 0);
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0111 = data.readString();
                    String _arg187 = data.readString();
                    boolean _arg257 = data.readInt() != 0;
                    boolean performDexOptSecondary = performDexOptSecondary(_arg0111, _arg187, _arg257);
                    reply.writeNoException();
                    reply.writeInt(performDexOptSecondary ? 1 : 0);
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0112 = data.readString();
                    boolean compileLayouts = compileLayouts(_arg0112);
                    reply.writeNoException();
                    reply.writeInt(compileLayouts ? 1 : 0);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0113 = data.readString();
                    dumpProfiles(_arg0113);
                    reply.writeNoException();
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0114 = data.readString();
                    forceDexOpt(_arg0114);
                    reply.writeNoException();
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg0115 = data.createStringArrayList();
                    boolean runBackgroundDexoptJob = runBackgroundDexoptJob(_arg0115);
                    reply.writeNoException();
                    reply.writeInt(runBackgroundDexoptJob ? 1 : 0);
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0116 = data.readString();
                    reconcileSecondaryDexFiles(_arg0116);
                    reply.writeNoException();
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0117 = data.readInt();
                    int _result61 = getMoveStatus(_arg0117);
                    reply.writeNoException();
                    reply.writeInt(_result61);
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    IPackageMoveObserver _arg0118 = IPackageMoveObserver.Stub.asInterface(data.readStrongBinder());
                    registerMoveCallback(_arg0118);
                    reply.writeNoException();
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    IPackageMoveObserver _arg0119 = IPackageMoveObserver.Stub.asInterface(data.readStrongBinder());
                    unregisterMoveCallback(_arg0119);
                    reply.writeNoException();
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0120 = data.readString();
                    String _arg188 = data.readString();
                    int _result62 = movePackage(_arg0120, _arg188);
                    reply.writeNoException();
                    reply.writeInt(_result62);
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0121 = data.readString();
                    int _result63 = movePrimaryStorage(_arg0121);
                    reply.writeNoException();
                    reply.writeInt(_result63);
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg028 = PermissionInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg028 = null;
                    }
                    boolean addPermissionAsync = addPermissionAsync(_arg028);
                    reply.writeNoException();
                    reply.writeInt(addPermissionAsync ? 1 : 0);
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0122 = data.readInt();
                    boolean installLocation = setInstallLocation(_arg0122);
                    reply.writeNoException();
                    reply.writeInt(installLocation ? 1 : 0);
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    int _result64 = getInstallLocation();
                    reply.writeNoException();
                    reply.writeInt(_result64);
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0123 = data.readString();
                    int _arg189 = data.readInt();
                    int _arg258 = data.readInt();
                    int _arg328 = data.readInt();
                    List<String> _arg412 = data.createStringArrayList();
                    int _result65 = installExistingPackageAsUser(_arg0123, _arg189, _arg258, _arg328, _arg412);
                    reply.writeNoException();
                    reply.writeInt(_result65);
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0124 = data.readInt();
                    int _arg190 = data.readInt();
                    verifyPendingInstall(_arg0124, _arg190);
                    reply.writeNoException();
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0125 = data.readInt();
                    int _arg191 = data.readInt();
                    long _arg259 = data.readLong();
                    extendVerificationTimeout(_arg0125, _arg191, _arg259);
                    reply.writeNoException();
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0126 = data.readInt();
                    int _arg192 = data.readInt();
                    List<String> _arg260 = data.createStringArrayList();
                    verifyIntentFilter(_arg0126, _arg192, _arg260);
                    reply.writeNoException();
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0127 = data.readString();
                    int _arg193 = data.readInt();
                    int _result66 = getIntentVerificationStatus(_arg0127, _arg193);
                    reply.writeNoException();
                    reply.writeInt(_result66);
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0128 = data.readString();
                    int _arg194 = data.readInt();
                    int _arg261 = data.readInt();
                    boolean updateIntentVerificationStatus = updateIntentVerificationStatus(_arg0128, _arg194, _arg261);
                    reply.writeNoException();
                    reply.writeInt(updateIntentVerificationStatus ? 1 : 0);
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0129 = data.readString();
                    ParceledListSlice _result67 = getIntentFilterVerifications(_arg0129);
                    reply.writeNoException();
                    if (_result67 != null) {
                        reply.writeInt(1);
                        _result67.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0130 = data.readString();
                    ParceledListSlice _result68 = getAllIntentFilters(_arg0130);
                    reply.writeNoException();
                    if (_result68 != null) {
                        reply.writeInt(1);
                        _result68.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0131 = data.readString();
                    int _arg195 = data.readInt();
                    boolean defaultBrowserPackageName = setDefaultBrowserPackageName(_arg0131, _arg195);
                    reply.writeNoException();
                    reply.writeInt(defaultBrowserPackageName ? 1 : 0);
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0132 = data.readInt();
                    String _result69 = getDefaultBrowserPackageName(_arg0132);
                    reply.writeNoException();
                    reply.writeString(_result69);
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    VerifierDeviceIdentity _result70 = getVerifierDeviceIdentity();
                    reply.writeNoException();
                    if (_result70 != null) {
                        reply.writeInt(1);
                        _result70.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isFirstBoot = isFirstBoot();
                    reply.writeNoException();
                    reply.writeInt(isFirstBoot ? 1 : 0);
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isOnlyCoreApps = isOnlyCoreApps();
                    reply.writeNoException();
                    reply.writeInt(isOnlyCoreApps ? 1 : 0);
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDeviceUpgrading = isDeviceUpgrading();
                    reply.writeNoException();
                    reply.writeInt(isDeviceUpgrading ? 1 : 0);
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0133 = data.readString();
                    boolean _arg196 = data.readInt() != 0;
                    setPermissionEnforced(_arg0133, _arg196);
                    reply.writeNoException();
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0134 = data.readString();
                    boolean isPermissionEnforced = isPermissionEnforced(_arg0134);
                    reply.writeNoException();
                    reply.writeInt(isPermissionEnforced ? 1 : 0);
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isStorageLow = isStorageLow();
                    reply.writeNoException();
                    reply.writeInt(isStorageLow ? 1 : 0);
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0135 = data.readString();
                    boolean _arg197 = data.readInt() != 0;
                    int _arg262 = data.readInt();
                    boolean applicationHiddenSettingAsUser = setApplicationHiddenSettingAsUser(_arg0135, _arg197, _arg262);
                    reply.writeNoException();
                    reply.writeInt(applicationHiddenSettingAsUser ? 1 : 0);
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0136 = data.readString();
                    int _arg198 = data.readInt();
                    boolean applicationHiddenSettingAsUser2 = getApplicationHiddenSettingAsUser(_arg0136, _arg198);
                    reply.writeNoException();
                    reply.writeInt(applicationHiddenSettingAsUser2 ? 1 : 0);
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0137 = data.readString();
                    boolean _arg199 = data.readInt() != 0;
                    setSystemAppHiddenUntilInstalled(_arg0137, _arg199);
                    reply.writeNoException();
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0138 = data.readString();
                    boolean _arg1100 = data.readInt() != 0;
                    int _arg263 = data.readInt();
                    boolean systemAppInstallState = setSystemAppInstallState(_arg0138, _arg1100, _arg263);
                    reply.writeNoException();
                    reply.writeInt(systemAppInstallState ? 1 : 0);
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    IPackageInstaller _result71 = getPackageInstaller();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result71 != null ? _result71.asBinder() : null);
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0139 = data.readString();
                    boolean _arg1101 = data.readInt() != 0;
                    int _arg264 = data.readInt();
                    boolean blockUninstallForUser = setBlockUninstallForUser(_arg0139, _arg1101, _arg264);
                    reply.writeNoException();
                    reply.writeInt(blockUninstallForUser ? 1 : 0);
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0140 = data.readString();
                    int _arg1102 = data.readInt();
                    boolean blockUninstallForUser2 = getBlockUninstallForUser(_arg0140, _arg1102);
                    reply.writeNoException();
                    reply.writeInt(blockUninstallForUser2 ? 1 : 0);
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0141 = data.readString();
                    String _arg1103 = data.readString();
                    KeySet _result72 = getKeySetByAlias(_arg0141, _arg1103);
                    reply.writeNoException();
                    if (_result72 != null) {
                        reply.writeInt(1);
                        _result72.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0142 = data.readString();
                    KeySet _result73 = getSigningKeySet(_arg0142);
                    reply.writeNoException();
                    if (_result73 != null) {
                        reply.writeInt(1);
                        _result73.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0143 = data.readString();
                    if (data.readInt() != 0) {
                        _arg13 = KeySet.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    boolean isPackageSignedByKeySet = isPackageSignedByKeySet(_arg0143, _arg13);
                    reply.writeNoException();
                    reply.writeInt(isPackageSignedByKeySet ? 1 : 0);
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0144 = data.readString();
                    if (data.readInt() != 0) {
                        _arg14 = KeySet.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    boolean isPackageSignedByKeySetExactly = isPackageSignedByKeySetExactly(_arg0144, _arg14);
                    reply.writeNoException();
                    reply.writeInt(isPackageSignedByKeySetExactly ? 1 : 0);
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    IOnPermissionsChangeListener _arg0145 = IOnPermissionsChangeListener.Stub.asInterface(data.readStrongBinder());
                    addOnPermissionsChangeListener(_arg0145);
                    reply.writeNoException();
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    IOnPermissionsChangeListener _arg0146 = IOnPermissionsChangeListener.Stub.asInterface(data.readStrongBinder());
                    removeOnPermissionsChangeListener(_arg0146);
                    reply.writeNoException();
                    return true;
                case 164:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg0147 = data.createStringArray();
                    int _arg1104 = data.readInt();
                    grantDefaultPermissionsToEnabledCarrierApps(_arg0147, _arg1104);
                    reply.writeNoException();
                    return true;
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg0148 = data.createStringArray();
                    int _arg1105 = data.readInt();
                    grantDefaultPermissionsToEnabledImsServices(_arg0148, _arg1105);
                    reply.writeNoException();
                    return true;
                case 166:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg0149 = data.createStringArray();
                    int _arg1106 = data.readInt();
                    grantDefaultPermissionsToEnabledTelephonyDataServices(_arg0149, _arg1106);
                    reply.writeNoException();
                    return true;
                case 167:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg0150 = data.createStringArray();
                    int _arg1107 = data.readInt();
                    revokeDefaultPermissionsFromDisabledTelephonyDataServices(_arg0150, _arg1107);
                    reply.writeNoException();
                    return true;
                case 168:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0151 = data.readString();
                    int _arg1108 = data.readInt();
                    grantDefaultPermissionsToActiveLuiApp(_arg0151, _arg1108);
                    reply.writeNoException();
                    return true;
                case 169:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg0152 = data.createStringArray();
                    int _arg1109 = data.readInt();
                    revokeDefaultPermissionsFromLuiApps(_arg0152, _arg1109);
                    reply.writeNoException();
                    return true;
                case 170:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0153 = data.readString();
                    String _arg1110 = data.readString();
                    int _arg265 = data.readInt();
                    boolean isPermissionRevokedByPolicy = isPermissionRevokedByPolicy(_arg0153, _arg1110, _arg265);
                    reply.writeNoException();
                    reply.writeInt(isPermissionRevokedByPolicy ? 1 : 0);
                    return true;
                case 171:
                    data.enforceInterface(DESCRIPTOR);
                    String _result74 = getPermissionControllerPackageName();
                    reply.writeNoException();
                    reply.writeString(_result74);
                    return true;
                case 172:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0154 = data.readInt();
                    ParceledListSlice _result75 = getInstantApps(_arg0154);
                    reply.writeNoException();
                    if (_result75 != null) {
                        reply.writeInt(1);
                        _result75.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 173:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0155 = data.readString();
                    int _arg1111 = data.readInt();
                    byte[] _result76 = getInstantAppCookie(_arg0155, _arg1111);
                    reply.writeNoException();
                    reply.writeByteArray(_result76);
                    return true;
                case 174:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0156 = data.readString();
                    byte[] _arg1112 = data.createByteArray();
                    int _arg266 = data.readInt();
                    boolean instantAppCookie = setInstantAppCookie(_arg0156, _arg1112, _arg266);
                    reply.writeNoException();
                    reply.writeInt(instantAppCookie ? 1 : 0);
                    return true;
                case 175:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0157 = data.readString();
                    int _arg1113 = data.readInt();
                    Bitmap _result77 = getInstantAppIcon(_arg0157, _arg1113);
                    reply.writeNoException();
                    if (_result77 != null) {
                        reply.writeInt(1);
                        _result77.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 176:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0158 = data.readString();
                    int _arg1114 = data.readInt();
                    boolean isInstantApp = isInstantApp(_arg0158, _arg1114);
                    reply.writeNoException();
                    reply.writeInt(isInstantApp ? 1 : 0);
                    return true;
                case 177:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0159 = data.readString();
                    boolean _arg1115 = data.readInt() != 0;
                    boolean requiredForSystemUser = setRequiredForSystemUser(_arg0159, _arg1115);
                    reply.writeNoException();
                    reply.writeInt(requiredForSystemUser ? 1 : 0);
                    return true;
                case 178:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0160 = data.readString();
                    boolean _arg1116 = data.readInt() != 0;
                    setUpdateAvailable(_arg0160, _arg1116);
                    reply.writeNoException();
                    return true;
                case 179:
                    data.enforceInterface(DESCRIPTOR);
                    String _result78 = getServicesSystemSharedLibraryPackageName();
                    reply.writeNoException();
                    reply.writeString(_result78);
                    return true;
                case 180:
                    data.enforceInterface(DESCRIPTOR);
                    String _result79 = getSharedSystemSharedLibraryPackageName();
                    reply.writeNoException();
                    reply.writeString(_result79);
                    return true;
                case 181:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0161 = data.readInt();
                    int _arg1117 = data.readInt();
                    ChangedPackages _result80 = getChangedPackages(_arg0161, _arg1117);
                    reply.writeNoException();
                    if (_result80 != null) {
                        reply.writeInt(1);
                        _result80.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 182:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0162 = data.readString();
                    boolean isPackageDeviceAdminOnAnyUser = isPackageDeviceAdminOnAnyUser(_arg0162);
                    reply.writeNoException();
                    reply.writeInt(isPackageDeviceAdminOnAnyUser ? 1 : 0);
                    return true;
                case 183:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0163 = data.readString();
                    int _arg1118 = data.readInt();
                    int _result81 = getInstallReason(_arg0163, _arg1118);
                    reply.writeNoException();
                    reply.writeInt(_result81);
                    return true;
                case 184:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0164 = data.readString();
                    int _arg1119 = data.readInt();
                    int _arg267 = data.readInt();
                    ParceledListSlice _result82 = getSharedLibraries(_arg0164, _arg1119, _arg267);
                    reply.writeNoException();
                    if (_result82 != null) {
                        reply.writeInt(1);
                        _result82.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 185:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0165 = data.readString();
                    int _arg1120 = data.readInt();
                    int _arg268 = data.readInt();
                    ParceledListSlice _result83 = getDeclaredSharedLibraries(_arg0165, _arg1120, _arg268);
                    reply.writeNoException();
                    if (_result83 != null) {
                        reply.writeInt(1);
                        _result83.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 186:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0166 = data.readString();
                    int _arg1121 = data.readInt();
                    boolean canRequestPackageInstalls = canRequestPackageInstalls(_arg0166, _arg1121);
                    reply.writeNoException();
                    reply.writeInt(canRequestPackageInstalls ? 1 : 0);
                    return true;
                case 187:
                    data.enforceInterface(DESCRIPTOR);
                    deletePreloadsFileCache();
                    reply.writeNoException();
                    return true;
                case 188:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _result84 = getInstantAppResolverComponent();
                    reply.writeNoException();
                    if (_result84 != null) {
                        reply.writeInt(1);
                        _result84.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 189:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _result85 = getInstantAppResolverSettingsComponent();
                    reply.writeNoException();
                    if (_result85 != null) {
                        reply.writeInt(1);
                        _result85.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 190:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _result86 = getInstantAppInstallerComponent();
                    reply.writeNoException();
                    if (_result86 != null) {
                        reply.writeInt(1);
                        _result86.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 191:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0167 = data.readString();
                    int _arg1122 = data.readInt();
                    String _result87 = getInstantAppAndroidId(_arg0167, _arg1122);
                    reply.writeNoException();
                    reply.writeString(_result87);
                    return true;
                case 192:
                    data.enforceInterface(DESCRIPTOR);
                    IArtManager _result88 = getArtManager();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result88 != null ? _result88.asBinder() : null);
                    return true;
                case 193:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0168 = data.readString();
                    if (data.readInt() != 0) {
                        _arg15 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    int _arg269 = data.readInt();
                    setHarmfulAppWarning(_arg0168, _arg15, _arg269);
                    reply.writeNoException();
                    return true;
                case 194:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0169 = data.readString();
                    int _arg1123 = data.readInt();
                    CharSequence _result89 = getHarmfulAppWarning(_arg0169, _arg1123);
                    reply.writeNoException();
                    if (_result89 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result89, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 195:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0170 = data.readString();
                    byte[] _arg1124 = data.createByteArray();
                    int _arg270 = data.readInt();
                    boolean hasSigningCertificate = hasSigningCertificate(_arg0170, _arg1124, _arg270);
                    reply.writeNoException();
                    reply.writeInt(hasSigningCertificate ? 1 : 0);
                    return true;
                case 196:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0171 = data.readInt();
                    byte[] _arg1125 = data.createByteArray();
                    int _arg271 = data.readInt();
                    boolean hasUidSigningCertificate = hasUidSigningCertificate(_arg0171, _arg1125, _arg271);
                    reply.writeNoException();
                    reply.writeInt(hasUidSigningCertificate ? 1 : 0);
                    return true;
                case 197:
                    data.enforceInterface(DESCRIPTOR);
                    String _result90 = getSystemTextClassifierPackageName();
                    reply.writeNoException();
                    reply.writeString(_result90);
                    return true;
                case 198:
                    data.enforceInterface(DESCRIPTOR);
                    String _result91 = getAttentionServicePackageName();
                    reply.writeNoException();
                    reply.writeString(_result91);
                    return true;
                case 199:
                    data.enforceInterface(DESCRIPTOR);
                    String _result92 = getWellbeingPackageName();
                    reply.writeNoException();
                    reply.writeString(_result92);
                    return true;
                case 200:
                    data.enforceInterface(DESCRIPTOR);
                    String _result93 = getAppPredictionServicePackageName();
                    reply.writeNoException();
                    reply.writeString(_result93);
                    return true;
                case 201:
                    data.enforceInterface(DESCRIPTOR);
                    String _result94 = getSystemCaptionsServicePackageName();
                    reply.writeNoException();
                    reply.writeString(_result94);
                    return true;
                case 202:
                    data.enforceInterface(DESCRIPTOR);
                    String _result95 = getIncidentReportApproverPackageName();
                    reply.writeNoException();
                    reply.writeString(_result95);
                    return true;
                case 203:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0172 = data.readString();
                    int _arg1126 = data.readInt();
                    boolean isPackageStateProtected = isPackageStateProtected(_arg0172, _arg1126);
                    reply.writeNoException();
                    reply.writeInt(isPackageStateProtected ? 1 : 0);
                    return true;
                case 204:
                    data.enforceInterface(DESCRIPTOR);
                    sendDeviceCustomizationReadyBroadcast();
                    reply.writeNoException();
                    return true;
                case 205:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0173 = data.readInt();
                    List<ModuleInfo> _result96 = getInstalledModules(_arg0173);
                    reply.writeNoException();
                    reply.writeTypedList(_result96);
                    return true;
                case 206:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0174 = data.readString();
                    int _arg1127 = data.readInt();
                    ModuleInfo _result97 = getModuleInfo(_arg0174, _arg1127);
                    reply.writeNoException();
                    if (_result97 != null) {
                        reply.writeInt(1);
                        _result97.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 207:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0175 = data.readInt();
                    int _result98 = getRuntimePermissionsVersion(_arg0175);
                    reply.writeNoException();
                    reply.writeInt(_result98);
                    return true;
                case 208:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0176 = data.readInt();
                    int _arg1128 = data.readInt();
                    setRuntimePermissionsVersion(_arg0176, _arg1128);
                    reply.writeNoException();
                    return true;
                case 209:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg0177 = data.createStringArray();
                    notifyPackagesReplacedReceived(_arg0177);
                    reply.writeNoException();
                    return true;
                case 210:
                    data.enforceInterface(DESCRIPTOR);
                    List<SplitPermissionInfoParcelable> _result99 = getSplitPermissions();
                    reply.writeNoException();
                    reply.writeTypedList(_result99);
                    return true;
                case 211:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg029 = ApplicationInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg029 = null;
                    }
                    ApplicationInfo _result100 = getOverlayApplicationInfo(_arg029);
                    reply.writeNoException();
                    if (_result100 != null) {
                        reply.writeInt(1);
                        _result100.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 212:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg030 = ActivityInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg030 = null;
                    }
                    ActivityInfo _result101 = getOverlayActivityInfo(_arg030);
                    reply.writeNoException();
                    if (_result101 != null) {
                        reply.writeInt(1);
                        _result101.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 213:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0178 = data.readString();
                    xpPackageInfo _result102 = getXpPackageInfo(_arg0178);
                    reply.writeNoException();
                    if (_result102 != null) {
                        reply.writeInt(1);
                        _result102.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 214:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0179 = data.readString();
                    String _arg1129 = data.readString();
                    setXpPackageInfo(_arg0179, _arg1129);
                    reply.writeNoException();
                    return true;
                case 215:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0180 = data.readString();
                    boolean _arg1130 = data.readInt() != 0;
                    int _arg272 = data.readInt();
                    setPackageNotLaunched(_arg0180, _arg1130, _arg272);
                    reply.writeNoException();
                    return true;
                case 216:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0181 = data.readInt();
                    updateAppScreenFlag(_arg0181);
                    reply.writeNoException();
                    return true;
                case 217:
                    data.enforceInterface(DESCRIPTOR);
                    List<xpPackageInfo> _result103 = getAllXpPackageInfos();
                    reply.writeNoException();
                    reply.writeTypedList(_result103);
                    return true;
                case 218:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0182 = data.readInt();
                    List<xpAppInfo> _result104 = getXpAppPackageList(_arg0182);
                    reply.writeNoException();
                    reply.writeTypedList(_result104);
                    return true;
                case 219:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0183 = data.readString();
                    Bitmap _result105 = getXpAppIcon(_arg0183);
                    reply.writeNoException();
                    if (_result105 != null) {
                        reply.writeInt(1);
                        _result105.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IPackageManager {
            public static IPackageManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.content.pm.IPackageManager
            public void checkPackageStartable(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().checkPackageStartable(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isPackageAvailable(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageAvailable(packageName, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
                PackageInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageInfo(packageName, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PackageInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public PackageInfo getPackageInfoVersioned(VersionedPackage versionedPackage, int flags, int userId) throws RemoteException {
                PackageInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (versionedPackage != null) {
                        _data.writeInt(1);
                        versionedPackage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageInfoVersioned(versionedPackage, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PackageInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getPackageUid(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageUid(packageName, flags, userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int[] getPackageGids(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageGids(packageName, flags, userId);
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String[] currentToCanonicalPackageNames(String[] names) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(names);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().currentToCanonicalPackageNames(names);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String[] canonicalToCurrentPackageNames(String[] names) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(names);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().canonicalToCurrentPackageNames(names);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public PermissionInfo getPermissionInfo(String name, String packageName, int flags) throws RemoteException {
                PermissionInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermissionInfo(name, packageName, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PermissionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice queryPermissionsByGroup(String group, int flags) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(group);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().queryPermissionsByGroup(group, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws RemoteException {
                PermissionGroupInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermissionGroupInfo(name, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PermissionGroupInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice getAllPermissionGroups(int flags) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllPermissionGroups(flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws RemoteException {
                ApplicationInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getApplicationInfo(packageName, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ApplicationInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ActivityInfo getActivityInfo(ComponentName className, int flags, int userId) throws RemoteException {
                ActivityInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActivityInfo(className, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ActivityInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean activitySupportsIntent(ComponentName className, Intent intent, String resolvedType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().activitySupportsIntent(className, intent, resolvedType);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ActivityInfo getReceiverInfo(ComponentName className, int flags, int userId) throws RemoteException {
                ActivityInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getReceiverInfo(className, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ActivityInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ServiceInfo getServiceInfo(ComponentName className, int flags, int userId) throws RemoteException {
                ServiceInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getServiceInfo(className, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ServiceInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ProviderInfo getProviderInfo(ComponentName className, int flags, int userId) throws RemoteException {
                ProviderInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProviderInfo(className, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ProviderInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int checkPermission(String permName, String pkgName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permName);
                    _data.writeString(pkgName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkPermission(permName, pkgName, userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int checkUidPermission(String permName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permName);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkUidPermission(permName, uid);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean addPermission(PermissionInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addPermission(info);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void removePermission(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removePermission(name);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void grantRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permissionName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().grantRuntimePermission(packageName, permissionName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void revokeRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permissionName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().revokeRuntimePermission(packageName, permissionName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void resetRuntimePermissions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resetRuntimePermissions();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getPermissionFlags(String permissionName, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permissionName);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermissionFlags(permissionName, packageName, userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void updatePermissionFlags(String permissionName, String packageName, int flagMask, int flagValues, boolean checkAdjustPolicyFlagPermission, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(permissionName);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(packageName);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(flagMask);
                    try {
                        _data.writeInt(flagValues);
                        _data.writeInt(checkAdjustPolicyFlagPermission ? 1 : 0);
                        try {
                            _data.writeInt(userId);
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().updatePermissionFlags(permissionName, packageName, flagMask, flagValues, checkAdjustPolicyFlagPermission, userId);
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.content.pm.IPackageManager
            public void updatePermissionFlagsForAllApps(int flagMask, int flagValues, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flagMask);
                    _data.writeInt(flagValues);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updatePermissionFlagsForAllApps(flagMask, flagValues, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public List<String> getWhitelistedRestrictedPermissions(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getWhitelistedRestrictedPermissions(packageName, flags, userId);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean addWhitelistedRestrictedPermission(String packageName, String permission, int whitelistFlags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    _data.writeInt(whitelistFlags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addWhitelistedRestrictedPermission(packageName, permission, whitelistFlags, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean removeWhitelistedRestrictedPermission(String packageName, String permission, int whitelistFlags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    _data.writeInt(whitelistFlags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeWhitelistedRestrictedPermission(packageName, permission, whitelistFlags, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean shouldShowRequestPermissionRationale(String permissionName, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permissionName);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().shouldShowRequestPermissionRationale(permissionName, packageName, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isProtectedBroadcast(String actionName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(actionName);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isProtectedBroadcast(actionName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int checkSignatures(String pkg1, String pkg2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg1);
                    _data.writeString(pkg2);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkSignatures(pkg1, pkg2);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int checkUidSignatures(int uid1, int uid2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid1);
                    _data.writeInt(uid2);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkUidSignatures(uid1, uid2);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public List<String> getAllPackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllPackages();
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String[] getPackagesForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackagesForUid(uid);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getNameForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNameForUid(uid);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String[] getNamesForUids(int[] uids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(uids);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNamesForUids(uids);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getUidForSharedUser(String sharedUserName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sharedUserName);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUidForSharedUser(sharedUserName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getFlagsForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFlagsForUid(uid);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getPrivateFlagsForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPrivateFlagsForUid(uid);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isUidPrivileged(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUidPrivileged(uid);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String[] getAppOpPermissionPackages(String permissionName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permissionName);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAppOpPermissionPackages(permissionName);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ResolveInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resolveIntent(intent, resolvedType, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ResolveInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ResolveInfo findPersistentPreferredActivity(Intent intent, int userId) throws RemoteException {
                ResolveInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().findPersistentPreferredActivity(intent, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ResolveInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean canForwardTo(Intent intent, String resolvedType, int sourceUserId, int targetUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(sourceUserId);
                    _data.writeInt(targetUserId);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().canForwardTo(intent, resolvedType, sourceUserId, targetUserId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().queryIntentActivities(intent, resolvedType, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice queryIntentActivityOptions(ComponentName caller, Intent[] specifics, String[] specificTypes, Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caller != null) {
                        _data.writeInt(1);
                        caller.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeTypedArray(specifics, 0);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeStringArray(specificTypes);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(resolvedType);
                        _data.writeInt(flags);
                        _data.writeInt(userId);
                        boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            ParceledListSlice queryIntentActivityOptions = Stub.getDefaultImpl().queryIntentActivityOptions(caller, specifics, specificTypes, intent, resolvedType, flags, userId);
                            _reply.recycle();
                            _data.recycle();
                            return queryIntentActivityOptions;
                        }
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                        } else {
                            _result = null;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().queryIntentReceivers(intent, resolvedType, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ResolveInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resolveService(intent, resolvedType, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ResolveInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice queryIntentServices(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().queryIntentServices(intent, resolvedType, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().queryIntentContentProviders(intent, resolvedType, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice getInstalledPackages(int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstalledPackages(flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice getPackagesHoldingPermissions(String[] permissions, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(permissions);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackagesHoldingPermissions(permissions, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice getInstalledApplications(int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstalledApplications(flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice getPersistentApplications(int flags) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPersistentApplications(flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ProviderInfo resolveContentProvider(String name, int flags, int userId) throws RemoteException {
                ProviderInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resolveContentProvider(name, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ProviderInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void querySyncProviders(List<String> outNames, List<ProviderInfo> outInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(outNames);
                    _data.writeTypedList(outInfo);
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().querySyncProviders(outNames, outInfo);
                        return;
                    }
                    _reply.readException();
                    _reply.readStringList(outNames);
                    _reply.readTypedList(outInfo, ProviderInfo.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice queryContentProviders(String processName, int uid, int flags, String metaDataKey) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    _data.writeInt(flags);
                    _data.writeString(metaDataKey);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().queryContentProviders(processName, uid, flags, metaDataKey);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public InstrumentationInfo getInstrumentationInfo(ComponentName className, int flags) throws RemoteException {
                InstrumentationInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstrumentationInfo(className, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = InstrumentationInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice queryInstrumentation(String targetPackage, int flags) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(targetPackage);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().queryInstrumentation(targetPackage, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void finishPackageInstall(int token, boolean didLaunch) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(didLaunch ? 1 : 0);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishPackageInstall(token, didLaunch);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setInstallerPackageName(String targetPackage, String installerPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(targetPackage);
                    _data.writeString(installerPackageName);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setInstallerPackageName(targetPackage, installerPackageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setApplicationCategoryHint(String packageName, int categoryHint, String callerPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(categoryHint);
                    _data.writeString(callerPackageName);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setApplicationCategoryHint(packageName, categoryHint, callerPackageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void deletePackageAsUser(String packageName, int versionCode, IPackageDeleteObserver observer, int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(versionCode);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().deletePackageAsUser(packageName, versionCode, observer, userId, flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void deletePackageVersioned(VersionedPackage versionedPackage, IPackageDeleteObserver2 observer, int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (versionedPackage != null) {
                        _data.writeInt(1);
                        versionedPackage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().deletePackageVersioned(versionedPackage, observer, userId, flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getInstallerPackageName(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstallerPackageName(packageName);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void resetApplicationPreferences(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resetApplicationPreferences(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ResolveInfo getLastChosenActivity(Intent intent, String resolvedType, int flags) throws RemoteException {
                ResolveInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastChosenActivity(intent, resolvedType, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ResolveInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setLastChosenActivity(Intent intent, String resolvedType, int flags, IntentFilter filter, int match, ComponentName activity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(resolvedType);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(flags);
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(match);
                        if (activity != null) {
                            _data.writeInt(1);
                            activity.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().setLastChosenActivity(intent, resolvedType, flags, filter, match, activity);
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.content.pm.IPackageManager
            public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(match);
                    _data.writeTypedArray(set, 0);
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addPreferredActivity(filter, match, set, activity, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void replacePreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(match);
                    _data.writeTypedArray(set, 0);
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().replacePreferredActivity(filter, match, set, activity, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void clearPackagePreferredActivities(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearPackagePreferredActivities(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getPreferredActivities(List<IntentFilter> outFilters, List<ComponentName> outActivities, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPreferredActivities(outFilters, outActivities, packageName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readTypedList(outFilters, IntentFilter.CREATOR);
                    _reply.readTypedList(outActivities, ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void addPersistentPreferredActivity(IntentFilter filter, ComponentName activity, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addPersistentPreferredActivity(filter, activity, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void clearPackagePersistentPreferredActivities(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearPackagePersistentPreferredActivities(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void addCrossProfileIntentFilter(IntentFilter intentFilter, String ownerPackage, int sourceUserId, int targetUserId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intentFilter != null) {
                        _data.writeInt(1);
                        intentFilter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(ownerPackage);
                    _data.writeInt(sourceUserId);
                    _data.writeInt(targetUserId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addCrossProfileIntentFilter(intentFilter, ownerPackage, sourceUserId, targetUserId, flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void clearCrossProfileIntentFilters(int sourceUserId, String ownerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sourceUserId);
                    _data.writeString(ownerPackage);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearCrossProfileIntentFilters(sourceUserId, ownerPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String[] setDistractingPackageRestrictionsAsUser(String[] packageNames, int restrictionFlags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(restrictionFlags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setDistractingPackageRestrictionsAsUser(packageNames, restrictionFlags, userId);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String[] setPackagesSuspendedAsUser(String[] packageNames, boolean suspended, PersistableBundle appExtras, PersistableBundle launcherExtras, SuspendDialogInfo dialogInfo, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStringArray(packageNames);
                    _data.writeInt(suspended ? 1 : 0);
                    if (appExtras != null) {
                        _data.writeInt(1);
                        appExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (launcherExtras != null) {
                        _data.writeInt(1);
                        launcherExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (dialogInfo != null) {
                        _data.writeInt(1);
                        dialogInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callingPackage);
                        _data.writeInt(userId);
                        boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            String[] packagesSuspendedAsUser = Stub.getDefaultImpl().setPackagesSuspendedAsUser(packageNames, suspended, appExtras, launcherExtras, dialogInfo, callingPackage, userId);
                            _reply.recycle();
                            _data.recycle();
                            return packagesSuspendedAsUser;
                        }
                        _reply.readException();
                        String[] _result = _reply.createStringArray();
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.content.pm.IPackageManager
            public String[] getUnsuspendablePackagesForUser(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUnsuspendablePackagesForUser(packageNames, userId);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isPackageSuspendedForUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageSuspendedForUser(packageName, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public PersistableBundle getSuspendedPackageAppExtras(String packageName, int userId) throws RemoteException {
                PersistableBundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSuspendedPackageAppExtras(packageName, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PersistableBundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public byte[] getPreferredActivityBackup(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPreferredActivityBackup(userId);
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void restorePreferredActivities(byte[] backup, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(backup);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().restorePreferredActivities(backup, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public byte[] getDefaultAppsBackup(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDefaultAppsBackup(userId);
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void restoreDefaultApps(byte[] backup, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(backup);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().restoreDefaultApps(backup, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public byte[] getIntentFilterVerificationBackup(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIntentFilterVerificationBackup(userId);
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void restoreIntentFilterVerification(byte[] backup, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(backup);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(90, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().restoreIntentFilterVerification(backup, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ComponentName getHomeActivities(List<ResolveInfo> outHomeCandidates) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(91, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getHomeActivities(outHomeCandidates);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.readTypedList(outHomeCandidates, ResolveInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setHomeActivity(ComponentName className, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(92, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setHomeActivity(className, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(newState);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(93, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setComponentEnabledSetting(componentName, newState, flags, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getComponentEnabledSetting(ComponentName componentName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(94, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getComponentEnabledSetting(componentName, userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(newState);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(95, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setApplicationEnabledSetting(packageName, newState, flags, userId, callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getApplicationEnabledSetting(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(96, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getApplicationEnabledSetting(packageName, userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void logAppProcessStartIfNeeded(String processName, int uid, String seinfo, String apkFile, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    _data.writeString(seinfo);
                    _data.writeString(apkFile);
                    _data.writeInt(pid);
                    boolean _status = this.mRemote.transact(97, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().logAppProcessStartIfNeeded(processName, uid, seinfo, apkFile, pid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void flushPackageRestrictionsAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(98, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().flushPackageRestrictionsAsUser(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setPackageStoppedState(String packageName, boolean stopped, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(stopped ? 1 : 0);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(99, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPackageStoppedState(packageName, stopped, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void freeStorageAndNotify(String volumeUuid, long freeStorageSize, int storageFlags, IPackageDataObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeLong(freeStorageSize);
                    _data.writeInt(storageFlags);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(100, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().freeStorageAndNotify(volumeUuid, freeStorageSize, storageFlags, observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void freeStorage(String volumeUuid, long freeStorageSize, int storageFlags, IntentSender pi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeLong(freeStorageSize);
                    _data.writeInt(storageFlags);
                    if (pi != null) {
                        _data.writeInt(1);
                        pi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(101, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().freeStorage(volumeUuid, freeStorageSize, storageFlags, pi);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void deleteApplicationCacheFiles(String packageName, IPackageDataObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(102, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().deleteApplicationCacheFiles(packageName, observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void deleteApplicationCacheFilesAsUser(String packageName, int userId, IPackageDataObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(103, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().deleteApplicationCacheFilesAsUser(packageName, userId, observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void clearApplicationUserData(String packageName, IPackageDataObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(104, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearApplicationUserData(packageName, observer, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void clearApplicationProfileData(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(105, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearApplicationProfileData(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void getPackageSizeInfo(String packageName, int userHandle, IPackageStatsObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(106, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getPackageSizeInfo(packageName, userHandle, observer);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String[] getSystemSharedLibraryNames() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(107, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSystemSharedLibraryNames();
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice getSystemAvailableFeatures() throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(108, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSystemAvailableFeatures();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean hasSystemFeature(String name, int version) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(version);
                    boolean _status = this.mRemote.transact(109, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasSystemFeature(name, version);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void enterSafeMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(110, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enterSafeMode();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isSafeMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(111, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSafeMode();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void systemReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(112, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().systemReady();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean hasSystemUidErrors() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(113, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasSystemUidErrors();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void performFstrimIfNeeded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(114, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().performFstrimIfNeeded();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void updatePackagesIfNeeded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(115, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updatePackagesIfNeeded();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void notifyPackageUse(String packageName, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(reason);
                    boolean _status = this.mRemote.transact(116, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyPackageUse(packageName, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void notifyDexLoad(String loadingPackageName, List<String> classLoadersNames, List<String> classPaths, String loaderIsa) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(loadingPackageName);
                    _data.writeStringList(classLoadersNames);
                    _data.writeStringList(classPaths);
                    _data.writeString(loaderIsa);
                    boolean _status = this.mRemote.transact(117, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyDexLoad(loadingPackageName, classLoadersNames, classPaths, loaderIsa);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void registerDexModule(String packageName, String dexModulePath, boolean isSharedModule, IDexModuleRegisterCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(dexModulePath);
                    _data.writeInt(isSharedModule ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(118, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerDexModule(packageName, dexModulePath, isSharedModule, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean performDexOptMode(String packageName, boolean checkProfiles, String targetCompilerFilter, boolean force, boolean bootComplete, String splitName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                        _data.writeInt(checkProfiles ? 1 : 0);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(targetCompilerFilter);
                        _data.writeInt(force ? 1 : 0);
                        _data.writeInt(bootComplete ? 1 : 0);
                        try {
                            _data.writeString(splitName);
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        boolean _status = this.mRemote.transact(119, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            boolean performDexOptMode = Stub.getDefaultImpl().performDexOptMode(packageName, checkProfiles, targetCompilerFilter, force, bootComplete, splitName);
                            _reply.recycle();
                            _data.recycle();
                            return performDexOptMode;
                        }
                        _reply.readException();
                        boolean _result = _reply.readInt() != 0;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean performDexOptSecondary(String packageName, String targetCompilerFilter, boolean force) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(targetCompilerFilter);
                    _data.writeInt(force ? 1 : 0);
                    boolean _status = this.mRemote.transact(120, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().performDexOptSecondary(packageName, targetCompilerFilter, force);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean compileLayouts(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(121, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().compileLayouts(packageName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void dumpProfiles(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(122, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dumpProfiles(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void forceDexOpt(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(123, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceDexOpt(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean runBackgroundDexoptJob(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    boolean _status = this.mRemote.transact(124, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().runBackgroundDexoptJob(packageNames);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void reconcileSecondaryDexFiles(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(125, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reconcileSecondaryDexFiles(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getMoveStatus(int moveId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(moveId);
                    boolean _status = this.mRemote.transact(126, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMoveStatus(moveId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void registerMoveCallback(IPackageMoveObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(127, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerMoveCallback(callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void unregisterMoveCallback(IPackageMoveObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(128, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterMoveCallback(callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int movePackage(String packageName, String volumeUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(volumeUuid);
                    boolean _status = this.mRemote.transact(129, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().movePackage(packageName, volumeUuid);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int movePrimaryStorage(String volumeUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    boolean _status = this.mRemote.transact(130, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().movePrimaryStorage(volumeUuid);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean addPermissionAsync(PermissionInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(131, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addPermissionAsync(info);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean setInstallLocation(int loc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(loc);
                    boolean _status = this.mRemote.transact(132, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setInstallLocation(loc);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getInstallLocation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(133, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstallLocation();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int installExistingPackageAsUser(String packageName, int userId, int installFlags, int installReason, List<String> whiteListedPermissions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(installFlags);
                    _data.writeInt(installReason);
                    _data.writeStringList(whiteListedPermissions);
                    boolean _status = this.mRemote.transact(134, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().installExistingPackageAsUser(packageName, userId, installFlags, installReason, whiteListedPermissions);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void verifyPendingInstall(int id, int verificationCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(verificationCode);
                    boolean _status = this.mRemote.transact(135, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().verifyPendingInstall(id, verificationCode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(verificationCodeAtTimeout);
                    _data.writeLong(millisecondsToDelay);
                    boolean _status = this.mRemote.transact(136, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().extendVerificationTimeout(id, verificationCodeAtTimeout, millisecondsToDelay);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void verifyIntentFilter(int id, int verificationCode, List<String> failedDomains) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(verificationCode);
                    _data.writeStringList(failedDomains);
                    boolean _status = this.mRemote.transact(137, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().verifyIntentFilter(id, verificationCode, failedDomains);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getIntentVerificationStatus(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(138, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIntentVerificationStatus(packageName, userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean updateIntentVerificationStatus(String packageName, int status, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(status);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(139, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateIntentVerificationStatus(packageName, status, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice getIntentFilterVerifications(String packageName) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(140, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIntentFilterVerifications(packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice getAllIntentFilters(String packageName) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(141, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllIntentFilters(packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean setDefaultBrowserPackageName(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(142, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setDefaultBrowserPackageName(packageName, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getDefaultBrowserPackageName(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(143, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDefaultBrowserPackageName(userId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public VerifierDeviceIdentity getVerifierDeviceIdentity() throws RemoteException {
                VerifierDeviceIdentity _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(144, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVerifierDeviceIdentity();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VerifierDeviceIdentity.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isFirstBoot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(145, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isFirstBoot();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isOnlyCoreApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(146, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isOnlyCoreApps();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isDeviceUpgrading() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(147, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDeviceUpgrading();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setPermissionEnforced(String permission, boolean enforced) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeInt(enforced ? 1 : 0);
                    boolean _status = this.mRemote.transact(148, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPermissionEnforced(permission, enforced);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isPermissionEnforced(String permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    boolean _status = this.mRemote.transact(149, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPermissionEnforced(permission);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isStorageLow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(150, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isStorageLow();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean setApplicationHiddenSettingAsUser(String packageName, boolean hidden, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(hidden ? 1 : 0);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(151, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setApplicationHiddenSettingAsUser(packageName, hidden, userId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean getApplicationHiddenSettingAsUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(152, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getApplicationHiddenSettingAsUser(packageName, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setSystemAppHiddenUntilInstalled(String packageName, boolean hidden) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(hidden ? 1 : 0);
                    boolean _status = this.mRemote.transact(153, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSystemAppHiddenUntilInstalled(packageName, hidden);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean setSystemAppInstallState(String packageName, boolean installed, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(installed ? 1 : 0);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(154, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setSystemAppInstallState(packageName, installed, userId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public IPackageInstaller getPackageInstaller() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(155, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackageInstaller();
                    }
                    _reply.readException();
                    IPackageInstaller _result = IPackageInstaller.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean setBlockUninstallForUser(String packageName, boolean blockUninstall, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(blockUninstall ? 1 : 0);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(156, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setBlockUninstallForUser(packageName, blockUninstall, userId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean getBlockUninstallForUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(157, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBlockUninstallForUser(packageName, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public KeySet getKeySetByAlias(String packageName, String alias) throws RemoteException {
                KeySet _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(alias);
                    boolean _status = this.mRemote.transact(158, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getKeySetByAlias(packageName, alias);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = KeySet.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public KeySet getSigningKeySet(String packageName) throws RemoteException {
                KeySet _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(159, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSigningKeySet(packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = KeySet.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isPackageSignedByKeySet(String packageName, KeySet ks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (ks != null) {
                        _data.writeInt(1);
                        ks.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(160, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageSignedByKeySet(packageName, ks);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isPackageSignedByKeySetExactly(String packageName, KeySet ks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (ks != null) {
                        _data.writeInt(1);
                        ks.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(161, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageSignedByKeySetExactly(packageName, ks);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void addOnPermissionsChangeListener(IOnPermissionsChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(162, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addOnPermissionsChangeListener(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void removeOnPermissionsChangeListener(IOnPermissionsChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(163, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeOnPermissionsChangeListener(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void grantDefaultPermissionsToEnabledCarrierApps(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(164, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().grantDefaultPermissionsToEnabledCarrierApps(packageNames, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void grantDefaultPermissionsToEnabledImsServices(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(165, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().grantDefaultPermissionsToEnabledImsServices(packageNames, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void grantDefaultPermissionsToEnabledTelephonyDataServices(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(166, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().grantDefaultPermissionsToEnabledTelephonyDataServices(packageNames, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void revokeDefaultPermissionsFromDisabledTelephonyDataServices(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(167, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().revokeDefaultPermissionsFromDisabledTelephonyDataServices(packageNames, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void grantDefaultPermissionsToActiveLuiApp(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(168, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().grantDefaultPermissionsToActiveLuiApp(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void revokeDefaultPermissionsFromLuiApps(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(169, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().revokeDefaultPermissionsFromLuiApps(packageNames, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isPermissionRevokedByPolicy(String permission, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(170, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPermissionRevokedByPolicy(permission, packageName, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getPermissionControllerPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(171, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermissionControllerPackageName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice getInstantApps(int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(172, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstantApps(userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public byte[] getInstantAppCookie(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(173, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstantAppCookie(packageName, userId);
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean setInstantAppCookie(String packageName, byte[] cookie, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeByteArray(cookie);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(174, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setInstantAppCookie(packageName, cookie, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public Bitmap getInstantAppIcon(String packageName, int userId) throws RemoteException {
                Bitmap _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(175, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstantAppIcon(packageName, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bitmap.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isInstantApp(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(176, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isInstantApp(packageName, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean setRequiredForSystemUser(String packageName, boolean systemUserApp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(systemUserApp ? 1 : 0);
                    boolean _status = this.mRemote.transact(177, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setRequiredForSystemUser(packageName, systemUserApp);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setUpdateAvailable(String packageName, boolean updateAvaialble) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(updateAvaialble ? 1 : 0);
                    boolean _status = this.mRemote.transact(178, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUpdateAvailable(packageName, updateAvaialble);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getServicesSystemSharedLibraryPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(179, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getServicesSystemSharedLibraryPackageName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getSharedSystemSharedLibraryPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(180, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSharedSystemSharedLibraryPackageName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ChangedPackages getChangedPackages(int sequenceNumber, int userId) throws RemoteException {
                ChangedPackages _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sequenceNumber);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(181, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getChangedPackages(sequenceNumber, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ChangedPackages.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isPackageDeviceAdminOnAnyUser(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(182, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageDeviceAdminOnAnyUser(packageName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getInstallReason(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(183, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstallReason(packageName, userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice getSharedLibraries(String packageName, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(184, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSharedLibraries(packageName, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ParceledListSlice getDeclaredSharedLibraries(String packageName, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(185, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeclaredSharedLibraries(packageName, flags, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean canRequestPackageInstalls(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(186, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().canRequestPackageInstalls(packageName, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void deletePreloadsFileCache() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(187, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().deletePreloadsFileCache();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ComponentName getInstantAppResolverComponent() throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(188, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstantAppResolverComponent();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ComponentName getInstantAppResolverSettingsComponent() throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(189, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstantAppResolverSettingsComponent();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ComponentName getInstantAppInstallerComponent() throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(190, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstantAppInstallerComponent();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getInstantAppAndroidId(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(191, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstantAppAndroidId(packageName, userId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public IArtManager getArtManager() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(192, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getArtManager();
                    }
                    _reply.readException();
                    IArtManager _result = IArtManager.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setHarmfulAppWarning(String packageName, CharSequence warning, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (warning != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(warning, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(193, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setHarmfulAppWarning(packageName, warning, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public CharSequence getHarmfulAppWarning(String packageName, int userId) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(194, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getHarmfulAppWarning(packageName, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean hasSigningCertificate(String packageName, byte[] signingCertificate, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeByteArray(signingCertificate);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(195, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasSigningCertificate(packageName, signingCertificate, flags);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean hasUidSigningCertificate(int uid, byte[] signingCertificate, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeByteArray(signingCertificate);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(196, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasUidSigningCertificate(uid, signingCertificate, flags);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getSystemTextClassifierPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(197, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSystemTextClassifierPackageName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getAttentionServicePackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(198, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAttentionServicePackageName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getWellbeingPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(199, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getWellbeingPackageName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getAppPredictionServicePackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(200, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAppPredictionServicePackageName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getSystemCaptionsServicePackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(201, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSystemCaptionsServicePackageName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public String getIncidentReportApproverPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(202, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIncidentReportApproverPackageName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public boolean isPackageStateProtected(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(203, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageStateProtected(packageName, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void sendDeviceCustomizationReadyBroadcast() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(204, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendDeviceCustomizationReadyBroadcast();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public List<ModuleInfo> getInstalledModules(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(205, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstalledModules(flags);
                    }
                    _reply.readException();
                    List<ModuleInfo> _result = _reply.createTypedArrayList(ModuleInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ModuleInfo getModuleInfo(String packageName, int flags) throws RemoteException {
                ModuleInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(206, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getModuleInfo(packageName, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ModuleInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public int getRuntimePermissionsVersion(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(207, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRuntimePermissionsVersion(userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setRuntimePermissionsVersion(int version, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(version);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(208, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRuntimePermissionsVersion(version, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void notifyPackagesReplacedReceived(String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packages);
                    boolean _status = this.mRemote.transact(209, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyPackagesReplacedReceived(packages);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public List<SplitPermissionInfoParcelable> getSplitPermissions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(210, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSplitPermissions();
                    }
                    _reply.readException();
                    List<SplitPermissionInfoParcelable> _result = _reply.createTypedArrayList(SplitPermissionInfoParcelable.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ApplicationInfo getOverlayApplicationInfo(ApplicationInfo info) throws RemoteException {
                ApplicationInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(211, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOverlayApplicationInfo(info);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ApplicationInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public ActivityInfo getOverlayActivityInfo(ActivityInfo info) throws RemoteException {
                ActivityInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(212, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOverlayActivityInfo(info);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ActivityInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public xpPackageInfo getXpPackageInfo(String packageName) throws RemoteException {
                xpPackageInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(213, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getXpPackageInfo(packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = xpPackageInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setXpPackageInfo(String packageName, String packageInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(packageInfo);
                    boolean _status = this.mRemote.transact(214, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setXpPackageInfo(packageName, packageInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void setPackageNotLaunched(String packageName, boolean stop, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(stop ? 1 : 0);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(215, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPackageNotLaunched(packageName, stop, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void updateAppScreenFlag(int gearLevel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(gearLevel);
                    boolean _status = this.mRemote.transact(216, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateAppScreenFlag(gearLevel);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public List<xpPackageInfo> getAllXpPackageInfos() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(217, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllXpPackageInfos();
                    }
                    _reply.readException();
                    List<xpPackageInfo> _result = _reply.createTypedArrayList(xpPackageInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public List<xpAppInfo> getXpAppPackageList(int screenId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(screenId);
                    boolean _status = this.mRemote.transact(218, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getXpAppPackageList(screenId);
                    }
                    _reply.readException();
                    List<xpAppInfo> _result = _reply.createTypedArrayList(xpAppInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public Bitmap getXpAppIcon(String packageName) throws RemoteException {
                Bitmap _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(219, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getXpAppIcon(packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bitmap.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IPackageManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IPackageManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
