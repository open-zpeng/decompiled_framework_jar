package android.content.pm;

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
    synchronized boolean activitySupportsIntent(ComponentName componentName, Intent intent, String str) throws RemoteException;

    synchronized void addCrossProfileIntentFilter(IntentFilter intentFilter, String str, int i, int i2, int i3) throws RemoteException;

    synchronized void addOnPermissionsChangeListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean addPermission(PermissionInfo permissionInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean addPermissionAsync(PermissionInfo permissionInfo) throws RemoteException;

    synchronized void addPersistentPreferredActivity(IntentFilter intentFilter, ComponentName componentName, int i) throws RemoteException;

    synchronized void addPreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName, int i2) throws RemoteException;

    synchronized boolean canForwardTo(Intent intent, String str, int i, int i2) throws RemoteException;

    synchronized boolean canRequestPackageInstalls(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String[] canonicalToCurrentPackageNames(String[] strArr) throws RemoteException;

    synchronized void checkPackageStartable(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int checkPermission(String str, String str2, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int checkSignatures(String str, String str2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int checkUidPermission(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int checkUidSignatures(int i, int i2) throws RemoteException;

    synchronized void clearApplicationProfileData(String str) throws RemoteException;

    synchronized void clearApplicationUserData(String str, IPackageDataObserver iPackageDataObserver, int i) throws RemoteException;

    synchronized void clearCrossProfileIntentFilters(int i, String str) throws RemoteException;

    void clearDalvikCache(String str) throws RemoteException;

    synchronized void clearPackagePersistentPreferredActivities(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void clearPackagePreferredActivities(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String[] currentToCanonicalPackageNames(String[] strArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void deleteApplicationCacheFiles(String str, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    synchronized void deleteApplicationCacheFilesAsUser(String str, int i, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    synchronized void deletePackageAsUser(String str, int i, IPackageDeleteObserver iPackageDeleteObserver, int i2, int i3) throws RemoteException;

    synchronized void deletePackageVersioned(VersionedPackage versionedPackage, IPackageDeleteObserver2 iPackageDeleteObserver2, int i, int i2) throws RemoteException;

    synchronized void deletePreloadsFileCache() throws RemoteException;

    synchronized void dumpProfiles(String str) throws RemoteException;

    private protected void enterSafeMode() throws RemoteException;

    synchronized void extendVerificationTimeout(int i, int i2, long j) throws RemoteException;

    synchronized ResolveInfo findPersistentPreferredActivity(Intent intent, int i) throws RemoteException;

    synchronized void finishPackageInstall(int i, boolean z) throws RemoteException;

    synchronized void flushPackageRestrictionsAsUser(int i) throws RemoteException;

    synchronized void forceDexOpt(String str) throws RemoteException;

    synchronized void freeStorage(String str, long j, int i, IntentSender intentSender) throws RemoteException;

    synchronized void freeStorageAndNotify(String str, long j, int i, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ActivityInfo getActivityInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    synchronized ParceledListSlice getAllIntentFilters(String str) throws RemoteException;

    synchronized List<String> getAllPackages() throws RemoteException;

    synchronized ParceledListSlice getAllPermissionGroups(int i) throws RemoteException;

    List<xpPackageInfo> getAllXpPackageInfos() throws RemoteException;

    private protected String[] getAppOpPermissionPackages(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getApplicationEnabledSetting(String str, int i) throws RemoteException;

    synchronized boolean getApplicationHiddenSettingAsUser(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ApplicationInfo getApplicationInfo(String str, int i, int i2) throws RemoteException;

    synchronized IArtManager getArtManager() throws RemoteException;

    private protected boolean getBlockUninstallForUser(String str, int i) throws RemoteException;

    synchronized ChangedPackages getChangedPackages(int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getComponentEnabledSetting(ComponentName componentName, int i) throws RemoteException;

    synchronized byte[] getDefaultAppsBackup(int i) throws RemoteException;

    synchronized String getDefaultBrowserPackageName(int i) throws RemoteException;

    private protected int getFlagsForUid(int i) throws RemoteException;

    synchronized CharSequence getHarmfulAppWarning(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ComponentName getHomeActivities(List<ResolveInfo> list) throws RemoteException;

    private protected int getInstallLocation() throws RemoteException;

    synchronized int getInstallReason(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ParceledListSlice getInstalledApplications(int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ParceledListSlice getInstalledPackages(int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String getInstallerPackageName(String str) throws RemoteException;

    synchronized String getInstantAppAndroidId(String str, int i) throws RemoteException;

    synchronized byte[] getInstantAppCookie(String str, int i) throws RemoteException;

    synchronized Bitmap getInstantAppIcon(String str, int i) throws RemoteException;

    synchronized ComponentName getInstantAppInstallerComponent() throws RemoteException;

    synchronized ComponentName getInstantAppResolverComponent() throws RemoteException;

    synchronized ComponentName getInstantAppResolverSettingsComponent() throws RemoteException;

    synchronized ParceledListSlice getInstantApps(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    InstrumentationInfo getInstrumentationInfo(ComponentName componentName, int i) throws RemoteException;

    synchronized byte[] getIntentFilterVerificationBackup(int i) throws RemoteException;

    synchronized ParceledListSlice getIntentFilterVerifications(String str) throws RemoteException;

    synchronized int getIntentVerificationStatus(String str, int i) throws RemoteException;

    synchronized KeySet getKeySetByAlias(String str, String str2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ResolveInfo getLastChosenActivity(Intent intent, String str, int i) throws RemoteException;

    synchronized int getMoveStatus(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String getNameForUid(int i) throws RemoteException;

    synchronized String[] getNamesForUids(int[] iArr) throws RemoteException;

    ActivityInfo getOverlayActivityInfo(ActivityInfo activityInfo) throws RemoteException;

    ApplicationInfo getOverlayApplicationInfo(ApplicationInfo applicationInfo) throws RemoteException;

    synchronized int[] getPackageGids(String str, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    PackageInfo getPackageInfo(String str, int i, int i2) throws RemoteException;

    synchronized PackageInfo getPackageInfoVersioned(VersionedPackage versionedPackage, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    IPackageInstaller getPackageInstaller() throws RemoteException;

    synchronized void getPackageSizeInfo(String str, int i, IPackageStatsObserver iPackageStatsObserver) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getPackageUid(String str, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String[] getPackagesForUid(int i) throws RemoteException;

    synchronized ParceledListSlice getPackagesHoldingPermissions(String[] strArr, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String getPermissionControllerPackageName() throws RemoteException;

    synchronized int getPermissionFlags(String str, String str2, int i) throws RemoteException;

    synchronized byte[] getPermissionGrantBackup(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    PermissionGroupInfo getPermissionGroupInfo(String str, int i) throws RemoteException;

    synchronized PermissionInfo getPermissionInfo(String str, String str2, int i) throws RemoteException;

    synchronized ParceledListSlice getPersistentApplications(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getPreferredActivities(List<IntentFilter> list, List<ComponentName> list2, String str) throws RemoteException;

    synchronized byte[] getPreferredActivityBackup(int i) throws RemoteException;

    synchronized int getPrivateFlagsForUid(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ProviderInfo getProviderInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ActivityInfo getReceiverInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ServiceInfo getServiceInfo(ComponentName componentName, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String getServicesSystemSharedLibraryPackageName() throws RemoteException;

    synchronized ParceledListSlice getSharedLibraries(String str, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String getSharedSystemSharedLibraryPackageName() throws RemoteException;

    synchronized KeySet getSigningKeySet(String str) throws RemoteException;

    synchronized PersistableBundle getSuspendedPackageAppExtras(String str, int i) throws RemoteException;

    synchronized ParceledListSlice getSystemAvailableFeatures() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String[] getSystemSharedLibraryNames() throws RemoteException;

    synchronized String getSystemTextClassifierPackageName() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getUidForSharedUser(String str) throws RemoteException;

    synchronized VerifierDeviceIdentity getVerifierDeviceIdentity() throws RemoteException;

    Bitmap getXpAppIcon(String str) throws RemoteException;

    List<xpAppInfo> getXpAppPackageList(int i) throws RemoteException;

    xpPackageInfo getXpPackageInfo(String str) throws RemoteException;

    synchronized void grantDefaultPermissionsToActiveLuiApp(String str, int i) throws RemoteException;

    synchronized void grantDefaultPermissionsToEnabledCarrierApps(String[] strArr, int i) throws RemoteException;

    synchronized void grantDefaultPermissionsToEnabledImsServices(String[] strArr, int i) throws RemoteException;

    synchronized void grantDefaultPermissionsToEnabledTelephonyDataServices(String[] strArr, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void grantRuntimePermission(String str, String str2, int i) throws RemoteException;

    synchronized boolean hasSigningCertificate(String str, byte[] bArr, int i) throws RemoteException;

    synchronized boolean hasSystemFeature(String str, int i) throws RemoteException;

    private protected boolean hasSystemUidErrors() throws RemoteException;

    synchronized boolean hasUidSigningCertificate(int i, byte[] bArr, int i2) throws RemoteException;

    synchronized int installExistingPackageAsUser(String str, int i, int i2, int i3) throws RemoteException;

    synchronized boolean isFirstBoot() throws RemoteException;

    synchronized boolean isInstantApp(String str, int i) throws RemoteException;

    synchronized boolean isOnlyCoreApps() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isPackageAvailable(String str, int i) throws RemoteException;

    synchronized boolean isPackageDeviceAdminOnAnyUser(String str) throws RemoteException;

    synchronized boolean isPackageSignedByKeySet(String str, KeySet keySet) throws RemoteException;

    synchronized boolean isPackageSignedByKeySetExactly(String str, KeySet keySet) throws RemoteException;

    synchronized boolean isPackageStateProtected(String str, int i) throws RemoteException;

    synchronized boolean isPackageSuspendedForUser(String str, int i) throws RemoteException;

    synchronized boolean isPermissionEnforced(String str) throws RemoteException;

    synchronized boolean isPermissionRevokedByPolicy(String str, String str2, int i) throws RemoteException;

    private protected boolean isProtectedBroadcast(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isSafeMode() throws RemoteException;

    private protected boolean isStorageLow() throws RemoteException;

    private protected boolean isUidPrivileged(int i) throws RemoteException;

    synchronized boolean isUpgrade() throws RemoteException;

    synchronized void logAppProcessStartIfNeeded(String str, int i, String str2, String str3, int i2) throws RemoteException;

    synchronized int movePackage(String str, String str2) throws RemoteException;

    synchronized int movePrimaryStorage(String str) throws RemoteException;

    synchronized PackageCleanItem nextPackageToClean(PackageCleanItem packageCleanItem) throws RemoteException;

    synchronized void notifyDexLoad(String str, List<String> list, List<String> list2, String str2) throws RemoteException;

    synchronized void notifyPackageUse(String str, int i) throws RemoteException;

    synchronized boolean performDexOptMode(String str, boolean z, String str2, boolean z2, boolean z3, String str3) throws RemoteException;

    synchronized boolean performDexOptSecondary(String str, String str2, boolean z) throws RemoteException;

    synchronized void performFstrimIfNeeded() throws RemoteException;

    synchronized ParceledListSlice queryContentProviders(String str, int i, int i2, String str2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ParceledListSlice queryInstrumentation(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ParceledListSlice queryIntentActivities(Intent intent, String str, int i, int i2) throws RemoteException;

    synchronized ParceledListSlice queryIntentActivityOptions(ComponentName componentName, Intent[] intentArr, String[] strArr, Intent intent, String str, int i, int i2) throws RemoteException;

    synchronized ParceledListSlice queryIntentContentProviders(Intent intent, String str, int i, int i2) throws RemoteException;

    synchronized ParceledListSlice queryIntentReceivers(Intent intent, String str, int i, int i2) throws RemoteException;

    synchronized ParceledListSlice queryIntentServices(Intent intent, String str, int i, int i2) throws RemoteException;

    synchronized ParceledListSlice queryPermissionsByGroup(String str, int i) throws RemoteException;

    private protected void querySyncProviders(List<String> list, List<ProviderInfo> list2) throws RemoteException;

    synchronized void reconcileSecondaryDexFiles(String str) throws RemoteException;

    synchronized void registerDexModule(String str, String str2, boolean z, IDexModuleRegisterCallback iDexModuleRegisterCallback) throws RemoteException;

    synchronized void registerMoveCallback(IPackageMoveObserver iPackageMoveObserver) throws RemoteException;

    synchronized void removeOnPermissionsChangeListener(IOnPermissionsChangeListener iOnPermissionsChangeListener) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void removePermission(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void replacePreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName, int i2) throws RemoteException;

    synchronized void resetApplicationPreferences(int i) throws RemoteException;

    synchronized void resetRuntimePermissions() throws RemoteException;

    synchronized ProviderInfo resolveContentProvider(String str, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ResolveInfo resolveIntent(Intent intent, String str, int i, int i2) throws RemoteException;

    synchronized ResolveInfo resolveService(Intent intent, String str, int i, int i2) throws RemoteException;

    synchronized void restoreDefaultApps(byte[] bArr, int i) throws RemoteException;

    synchronized void restoreIntentFilterVerification(byte[] bArr, int i) throws RemoteException;

    synchronized void restorePermissionGrants(byte[] bArr, int i) throws RemoteException;

    synchronized void restorePreferredActivities(byte[] bArr, int i) throws RemoteException;

    synchronized void revokeDefaultPermissionsFromDisabledTelephonyDataServices(String[] strArr, int i) throws RemoteException;

    synchronized void revokeDefaultPermissionsFromLuiApps(String[] strArr, int i) throws RemoteException;

    synchronized void revokeRuntimePermission(String str, String str2, int i) throws RemoteException;

    synchronized boolean runBackgroundDexoptJob(List<String> list) throws RemoteException;

    synchronized void setApplicationCategoryHint(String str, int i, String str2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setApplicationEnabledSetting(String str, int i, int i2, int i3, String str2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean setApplicationHiddenSettingAsUser(String str, boolean z, int i) throws RemoteException;

    synchronized boolean setBlockUninstallForUser(String str, boolean z, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setComponentEnabledSetting(ComponentName componentName, int i, int i2, int i3) throws RemoteException;

    synchronized boolean setDefaultBrowserPackageName(String str, int i) throws RemoteException;

    synchronized void setHarmfulAppWarning(String str, CharSequence charSequence, int i) throws RemoteException;

    synchronized void setHomeActivity(ComponentName componentName, int i) throws RemoteException;

    synchronized boolean setInstallLocation(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setInstallerPackageName(String str, String str2) throws RemoteException;

    synchronized boolean setInstantAppCookie(String str, byte[] bArr, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setLastChosenActivity(Intent intent, String str, int i, IntentFilter intentFilter, int i2, ComponentName componentName) throws RemoteException;

    void setPackageNotLaunched(String str, boolean z, int i) throws RemoteException;

    private protected void setPackageStoppedState(String str, boolean z, int i) throws RemoteException;

    synchronized String[] setPackagesSuspendedAsUser(String[] strArr, boolean z, PersistableBundle persistableBundle, PersistableBundle persistableBundle2, String str, String str2, int i) throws RemoteException;

    synchronized void setPermissionEnforced(String str, boolean z) throws RemoteException;

    synchronized boolean setRequiredForSystemUser(String str, boolean z) throws RemoteException;

    void setSystemAppHiddenUntilInstalled(String str, boolean z) throws RemoteException;

    boolean setSystemAppInstallState(String str, boolean z, int i) throws RemoteException;

    synchronized void setUpdateAvailable(String str, boolean z) throws RemoteException;

    void setXpPackageInfo(String str, String str2) throws RemoteException;

    synchronized boolean shouldShowRequestPermissionRationale(String str, String str2, int i) throws RemoteException;

    private protected void systemReady() throws RemoteException;

    synchronized void unregisterMoveCallback(IPackageMoveObserver iPackageMoveObserver) throws RemoteException;

    void updateAppScreenFlag(int i) throws RemoteException;

    synchronized boolean updateIntentVerificationStatus(String str, int i, int i2) throws RemoteException;

    synchronized void updatePackagesIfNeeded() throws RemoteException;

    synchronized void updatePermissionFlags(String str, String str2, int i, int i2, int i3) throws RemoteException;

    synchronized void updatePermissionFlagsForAllApps(int i, int i2, int i3) throws RemoteException;

    synchronized void verifyIntentFilter(int i, int i2, List<String> list) throws RemoteException;

    synchronized void verifyPendingInstall(int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPackageManager {
        private static final String DESCRIPTOR = "android.content.pm.IPackageManager";
        static final int TRANSACTION_activitySupportsIntent = 15;
        static final int TRANSACTION_addCrossProfileIntentFilter = 75;
        static final int TRANSACTION_addOnPermissionsChangeListener = 160;
        static final int TRANSACTION_addPermission = 21;
        static final int TRANSACTION_addPermissionAsync = 129;
        static final int TRANSACTION_addPersistentPreferredActivity = 73;
        static final int TRANSACTION_addPreferredActivity = 69;
        static final int TRANSACTION_canForwardTo = 44;
        static final int TRANSACTION_canRequestPackageInstalls = 183;
        static final int TRANSACTION_canonicalToCurrentPackageNames = 8;
        static final int TRANSACTION_checkPackageStartable = 1;
        static final int TRANSACTION_checkPermission = 19;
        static final int TRANSACTION_checkSignatures = 31;
        static final int TRANSACTION_checkUidPermission = 20;
        static final int TRANSACTION_checkUidSignatures = 32;
        static final int TRANSACTION_clearApplicationProfileData = 102;
        static final int TRANSACTION_clearApplicationUserData = 101;
        static final int TRANSACTION_clearCrossProfileIntentFilters = 76;
        static final int TRANSACTION_clearDalvikCache = 103;
        static final int TRANSACTION_clearPackagePersistentPreferredActivities = 74;
        static final int TRANSACTION_clearPackagePreferredActivities = 71;
        static final int TRANSACTION_currentToCanonicalPackageNames = 7;
        static final int TRANSACTION_deleteApplicationCacheFiles = 99;
        static final int TRANSACTION_deleteApplicationCacheFilesAsUser = 100;
        static final int TRANSACTION_deletePackageAsUser = 63;
        static final int TRANSACTION_deletePackageVersioned = 64;
        static final int TRANSACTION_deletePreloadsFileCache = 184;
        static final int TRANSACTION_dumpProfiles = 119;
        static final int TRANSACTION_enterSafeMode = 108;
        static final int TRANSACTION_extendVerificationTimeout = 134;
        static final int TRANSACTION_findPersistentPreferredActivity = 43;
        static final int TRANSACTION_finishPackageInstall = 60;
        static final int TRANSACTION_flushPackageRestrictionsAsUser = 95;
        static final int TRANSACTION_forceDexOpt = 120;
        static final int TRANSACTION_freeStorage = 98;
        static final int TRANSACTION_freeStorageAndNotify = 97;
        static final int TRANSACTION_getActivityInfo = 14;
        static final int TRANSACTION_getAllIntentFilters = 139;
        static final int TRANSACTION_getAllPackages = 33;
        static final int TRANSACTION_getAllPermissionGroups = 12;
        static final int TRANSACTION_getAllXpPackageInfos = 202;
        static final int TRANSACTION_getAppOpPermissionPackages = 41;
        static final int TRANSACTION_getApplicationEnabledSetting = 93;
        static final int TRANSACTION_getApplicationHiddenSettingAsUser = 150;
        public private protected static final int TRANSACTION_getApplicationInfo = 13;
        static final int TRANSACTION_getArtManager = 189;
        static final int TRANSACTION_getBlockUninstallForUser = 155;
        static final int TRANSACTION_getChangedPackages = 179;
        static final int TRANSACTION_getComponentEnabledSetting = 91;
        static final int TRANSACTION_getDefaultAppsBackup = 82;
        static final int TRANSACTION_getDefaultBrowserPackageName = 141;
        static final int TRANSACTION_getFlagsForUid = 38;
        static final int TRANSACTION_getHarmfulAppWarning = 191;
        static final int TRANSACTION_getHomeActivities = 88;
        static final int TRANSACTION_getInstallLocation = 131;
        static final int TRANSACTION_getInstallReason = 181;
        static final int TRANSACTION_getInstalledApplications = 53;
        static final int TRANSACTION_getInstalledPackages = 51;
        static final int TRANSACTION_getInstallerPackageName = 65;
        static final int TRANSACTION_getInstantAppAndroidId = 188;
        static final int TRANSACTION_getInstantAppCookie = 171;
        static final int TRANSACTION_getInstantAppIcon = 173;
        static final int TRANSACTION_getInstantAppInstallerComponent = 187;
        static final int TRANSACTION_getInstantAppResolverComponent = 185;
        static final int TRANSACTION_getInstantAppResolverSettingsComponent = 186;
        static final int TRANSACTION_getInstantApps = 170;
        static final int TRANSACTION_getInstrumentationInfo = 58;
        static final int TRANSACTION_getIntentFilterVerificationBackup = 84;
        static final int TRANSACTION_getIntentFilterVerifications = 138;
        static final int TRANSACTION_getIntentVerificationStatus = 136;
        static final int TRANSACTION_getKeySetByAlias = 156;
        static final int TRANSACTION_getLastChosenActivity = 67;
        static final int TRANSACTION_getMoveStatus = 124;
        static final int TRANSACTION_getNameForUid = 35;
        static final int TRANSACTION_getNamesForUids = 36;
        static final int TRANSACTION_getOverlayActivityInfo = 197;
        static final int TRANSACTION_getOverlayApplicationInfo = 196;
        static final int TRANSACTION_getPackageGids = 6;
        static final int TRANSACTION_getPackageInfo = 3;
        static final int TRANSACTION_getPackageInfoVersioned = 4;
        static final int TRANSACTION_getPackageInstaller = 153;
        static final int TRANSACTION_getPackageSizeInfo = 104;
        static final int TRANSACTION_getPackageUid = 5;
        static final int TRANSACTION_getPackagesForUid = 34;
        static final int TRANSACTION_getPackagesHoldingPermissions = 52;
        static final int TRANSACTION_getPermissionControllerPackageName = 169;
        static final int TRANSACTION_getPermissionFlags = 26;
        static final int TRANSACTION_getPermissionGrantBackup = 86;
        static final int TRANSACTION_getPermissionGroupInfo = 11;
        static final int TRANSACTION_getPermissionInfo = 9;
        static final int TRANSACTION_getPersistentApplications = 54;
        static final int TRANSACTION_getPreferredActivities = 72;
        static final int TRANSACTION_getPreferredActivityBackup = 80;
        static final int TRANSACTION_getPrivateFlagsForUid = 39;
        static final int TRANSACTION_getProviderInfo = 18;
        static final int TRANSACTION_getReceiverInfo = 16;
        static final int TRANSACTION_getServiceInfo = 17;
        static final int TRANSACTION_getServicesSystemSharedLibraryPackageName = 177;
        static final int TRANSACTION_getSharedLibraries = 182;
        static final int TRANSACTION_getSharedSystemSharedLibraryPackageName = 178;
        static final int TRANSACTION_getSigningKeySet = 157;
        static final int TRANSACTION_getSuspendedPackageAppExtras = 79;
        static final int TRANSACTION_getSystemAvailableFeatures = 106;
        static final int TRANSACTION_getSystemSharedLibraryNames = 105;
        static final int TRANSACTION_getSystemTextClassifierPackageName = 194;
        static final int TRANSACTION_getUidForSharedUser = 37;
        static final int TRANSACTION_getVerifierDeviceIdentity = 142;
        static final int TRANSACTION_getXpAppIcon = 204;
        static final int TRANSACTION_getXpAppPackageList = 203;
        static final int TRANSACTION_getXpPackageInfo = 198;
        static final int TRANSACTION_grantDefaultPermissionsToActiveLuiApp = 166;
        static final int TRANSACTION_grantDefaultPermissionsToEnabledCarrierApps = 162;
        static final int TRANSACTION_grantDefaultPermissionsToEnabledImsServices = 163;
        static final int TRANSACTION_grantDefaultPermissionsToEnabledTelephonyDataServices = 164;
        static final int TRANSACTION_grantRuntimePermission = 23;
        static final int TRANSACTION_hasSigningCertificate = 192;
        static final int TRANSACTION_hasSystemFeature = 107;
        static final int TRANSACTION_hasSystemUidErrors = 111;
        static final int TRANSACTION_hasUidSigningCertificate = 193;
        static final int TRANSACTION_installExistingPackageAsUser = 132;
        static final int TRANSACTION_isFirstBoot = 143;
        static final int TRANSACTION_isInstantApp = 174;
        static final int TRANSACTION_isOnlyCoreApps = 144;
        static final int TRANSACTION_isPackageAvailable = 2;
        static final int TRANSACTION_isPackageDeviceAdminOnAnyUser = 180;
        static final int TRANSACTION_isPackageSignedByKeySet = 158;
        static final int TRANSACTION_isPackageSignedByKeySetExactly = 159;
        static final int TRANSACTION_isPackageStateProtected = 195;
        static final int TRANSACTION_isPackageSuspendedForUser = 78;
        static final int TRANSACTION_isPermissionEnforced = 147;
        static final int TRANSACTION_isPermissionRevokedByPolicy = 168;
        static final int TRANSACTION_isProtectedBroadcast = 30;
        static final int TRANSACTION_isSafeMode = 109;
        static final int TRANSACTION_isStorageLow = 148;
        static final int TRANSACTION_isUidPrivileged = 40;
        static final int TRANSACTION_isUpgrade = 145;
        static final int TRANSACTION_logAppProcessStartIfNeeded = 94;
        static final int TRANSACTION_movePackage = 127;
        static final int TRANSACTION_movePrimaryStorage = 128;
        static final int TRANSACTION_nextPackageToClean = 123;
        static final int TRANSACTION_notifyDexLoad = 115;
        static final int TRANSACTION_notifyPackageUse = 114;
        static final int TRANSACTION_performDexOptMode = 117;
        static final int TRANSACTION_performDexOptSecondary = 118;
        static final int TRANSACTION_performFstrimIfNeeded = 112;
        static final int TRANSACTION_queryContentProviders = 57;
        static final int TRANSACTION_queryInstrumentation = 59;
        static final int TRANSACTION_queryIntentActivities = 45;
        static final int TRANSACTION_queryIntentActivityOptions = 46;
        static final int TRANSACTION_queryIntentContentProviders = 50;
        static final int TRANSACTION_queryIntentReceivers = 47;
        static final int TRANSACTION_queryIntentServices = 49;
        static final int TRANSACTION_queryPermissionsByGroup = 10;
        static final int TRANSACTION_querySyncProviders = 56;
        static final int TRANSACTION_reconcileSecondaryDexFiles = 122;
        static final int TRANSACTION_registerDexModule = 116;
        static final int TRANSACTION_registerMoveCallback = 125;
        static final int TRANSACTION_removeOnPermissionsChangeListener = 161;
        static final int TRANSACTION_removePermission = 22;
        static final int TRANSACTION_replacePreferredActivity = 70;
        static final int TRANSACTION_resetApplicationPreferences = 66;
        static final int TRANSACTION_resetRuntimePermissions = 25;
        static final int TRANSACTION_resolveContentProvider = 55;
        static final int TRANSACTION_resolveIntent = 42;
        static final int TRANSACTION_resolveService = 48;
        static final int TRANSACTION_restoreDefaultApps = 83;
        static final int TRANSACTION_restoreIntentFilterVerification = 85;
        static final int TRANSACTION_restorePermissionGrants = 87;
        static final int TRANSACTION_restorePreferredActivities = 81;
        static final int TRANSACTION_revokeDefaultPermissionsFromDisabledTelephonyDataServices = 165;
        static final int TRANSACTION_revokeDefaultPermissionsFromLuiApps = 167;
        static final int TRANSACTION_revokeRuntimePermission = 24;
        static final int TRANSACTION_runBackgroundDexoptJob = 121;
        static final int TRANSACTION_setApplicationCategoryHint = 62;
        static final int TRANSACTION_setApplicationEnabledSetting = 92;
        static final int TRANSACTION_setApplicationHiddenSettingAsUser = 149;
        static final int TRANSACTION_setBlockUninstallForUser = 154;
        static final int TRANSACTION_setComponentEnabledSetting = 90;
        static final int TRANSACTION_setDefaultBrowserPackageName = 140;
        static final int TRANSACTION_setHarmfulAppWarning = 190;
        static final int TRANSACTION_setHomeActivity = 89;
        static final int TRANSACTION_setInstallLocation = 130;
        static final int TRANSACTION_setInstallerPackageName = 61;
        static final int TRANSACTION_setInstantAppCookie = 172;
        static final int TRANSACTION_setLastChosenActivity = 68;
        static final int TRANSACTION_setPackageNotLaunched = 200;
        static final int TRANSACTION_setPackageStoppedState = 96;
        static final int TRANSACTION_setPackagesSuspendedAsUser = 77;
        static final int TRANSACTION_setPermissionEnforced = 146;
        static final int TRANSACTION_setRequiredForSystemUser = 175;
        static final int TRANSACTION_setSystemAppHiddenUntilInstalled = 151;
        static final int TRANSACTION_setSystemAppInstallState = 152;
        static final int TRANSACTION_setUpdateAvailable = 176;
        static final int TRANSACTION_setXpPackageInfo = 199;
        static final int TRANSACTION_shouldShowRequestPermissionRationale = 29;
        static final int TRANSACTION_systemReady = 110;
        static final int TRANSACTION_unregisterMoveCallback = 126;
        static final int TRANSACTION_updateAppScreenFlag = 201;
        static final int TRANSACTION_updateIntentVerificationStatus = 137;
        static final int TRANSACTION_updatePackagesIfNeeded = 113;
        static final int TRANSACTION_updatePermissionFlags = 27;
        static final int TRANSACTION_updatePermissionFlagsForAllApps = 28;
        static final int TRANSACTION_verifyIntentFilter = 135;
        static final int TRANSACTION_verifyPendingInstall = 133;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
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

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ComponentName _arg0;
            ComponentName _arg02;
            Intent _arg03;
            IntentFilter _arg3;
            IntentFilter _arg04;
            IntentFilter _arg05;
            IntentFilter _arg06;
            PersistableBundle _arg2;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    int _arg1 = data.readInt();
                    checkPackageStartable(_arg07, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    int _arg12 = data.readInt();
                    boolean isPackageAvailable = isPackageAvailable(_arg08, _arg12);
                    reply.writeNoException();
                    reply.writeInt(isPackageAvailable ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    int _arg13 = data.readInt();
                    int _arg22 = data.readInt();
                    PackageInfo _result = getPackageInfo(_arg09, _arg13, _arg22);
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
                    VersionedPackage _arg010 = data.readInt() != 0 ? VersionedPackage.CREATOR.createFromParcel(data) : null;
                    int _arg14 = data.readInt();
                    int _arg23 = data.readInt();
                    PackageInfo _result2 = getPackageInfoVersioned(_arg010, _arg14, _arg23);
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
                    String _arg011 = data.readString();
                    int _arg15 = data.readInt();
                    int _arg24 = data.readInt();
                    int _result3 = getPackageUid(_arg011, _arg15, _arg24);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    int _arg16 = data.readInt();
                    int _arg25 = data.readInt();
                    int[] _result4 = getPackageGids(_arg012, _arg16, _arg25);
                    reply.writeNoException();
                    reply.writeIntArray(_result4);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg013 = data.createStringArray();
                    String[] _result5 = currentToCanonicalPackageNames(_arg013);
                    reply.writeNoException();
                    reply.writeStringArray(_result5);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg014 = data.createStringArray();
                    String[] _result6 = canonicalToCurrentPackageNames(_arg014);
                    reply.writeNoException();
                    reply.writeStringArray(_result6);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    String _arg17 = data.readString();
                    int _arg26 = data.readInt();
                    PermissionInfo _result7 = getPermissionInfo(_arg015, _arg17, _arg26);
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
                    String _arg016 = data.readString();
                    int _arg18 = data.readInt();
                    ParceledListSlice _result8 = queryPermissionsByGroup(_arg016, _arg18);
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
                    String _arg017 = data.readString();
                    int _arg19 = data.readInt();
                    PermissionGroupInfo _result9 = getPermissionGroupInfo(_arg017, _arg19);
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
                    int _arg018 = data.readInt();
                    ParceledListSlice _result10 = getAllPermissionGroups(_arg018);
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
                    String _arg019 = data.readString();
                    int _arg110 = data.readInt();
                    int _arg27 = data.readInt();
                    ApplicationInfo _result11 = getApplicationInfo(_arg019, _arg110, _arg27);
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
                    ComponentName _arg020 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg111 = data.readInt();
                    int _arg28 = data.readInt();
                    ActivityInfo _result12 = getActivityInfo(_arg020, _arg111, _arg28);
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
                        _arg0 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    Intent _arg112 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg29 = data.readString();
                    boolean activitySupportsIntent = activitySupportsIntent(_arg0, _arg112, _arg29);
                    reply.writeNoException();
                    reply.writeInt(activitySupportsIntent ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg021 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg113 = data.readInt();
                    int _arg210 = data.readInt();
                    ActivityInfo _result13 = getReceiverInfo(_arg021, _arg113, _arg210);
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
                    ComponentName _arg022 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg114 = data.readInt();
                    int _arg211 = data.readInt();
                    ServiceInfo _result14 = getServiceInfo(_arg022, _arg114, _arg211);
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
                    ComponentName _arg023 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg115 = data.readInt();
                    int _arg212 = data.readInt();
                    ProviderInfo _result15 = getProviderInfo(_arg023, _arg115, _arg212);
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
                    String _arg024 = data.readString();
                    String _arg116 = data.readString();
                    int _arg213 = data.readInt();
                    int _result16 = checkPermission(_arg024, _arg116, _arg213);
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg025 = data.readString();
                    int _arg117 = data.readInt();
                    int _result17 = checkUidPermission(_arg025, _arg117);
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    PermissionInfo _arg026 = data.readInt() != 0 ? PermissionInfo.CREATOR.createFromParcel(data) : null;
                    boolean addPermission = addPermission(_arg026);
                    reply.writeNoException();
                    reply.writeInt(addPermission ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg027 = data.readString();
                    removePermission(_arg027);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg028 = data.readString();
                    String _arg118 = data.readString();
                    int _arg214 = data.readInt();
                    grantRuntimePermission(_arg028, _arg118, _arg214);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg029 = data.readString();
                    String _arg119 = data.readString();
                    int _arg215 = data.readInt();
                    revokeRuntimePermission(_arg029, _arg119, _arg215);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    resetRuntimePermissions();
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg030 = data.readString();
                    String _arg120 = data.readString();
                    int _arg216 = data.readInt();
                    int _result18 = getPermissionFlags(_arg030, _arg120, _arg216);
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg031 = data.readString();
                    String _arg121 = data.readString();
                    int _arg217 = data.readInt();
                    int _arg32 = data.readInt();
                    int _arg4 = data.readInt();
                    updatePermissionFlags(_arg031, _arg121, _arg217, _arg32, _arg4);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg032 = data.readInt();
                    int _arg122 = data.readInt();
                    int _arg218 = data.readInt();
                    updatePermissionFlagsForAllApps(_arg032, _arg122, _arg218);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg033 = data.readString();
                    String _arg123 = data.readString();
                    int _arg219 = data.readInt();
                    boolean shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale(_arg033, _arg123, _arg219);
                    reply.writeNoException();
                    reply.writeInt(shouldShowRequestPermissionRationale ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg034 = data.readString();
                    boolean isProtectedBroadcast = isProtectedBroadcast(_arg034);
                    reply.writeNoException();
                    reply.writeInt(isProtectedBroadcast ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg035 = data.readString();
                    String _arg124 = data.readString();
                    int _result19 = checkSignatures(_arg035, _arg124);
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    int _arg125 = data.readInt();
                    int _result20 = checkUidSignatures(_arg036, _arg125);
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result21 = getAllPackages();
                    reply.writeNoException();
                    reply.writeStringList(_result21);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg037 = data.readInt();
                    String[] _result22 = getPackagesForUid(_arg037);
                    reply.writeNoException();
                    reply.writeStringArray(_result22);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg038 = data.readInt();
                    String _result23 = getNameForUid(_arg038);
                    reply.writeNoException();
                    reply.writeString(_result23);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg039 = data.createIntArray();
                    String[] _result24 = getNamesForUids(_arg039);
                    reply.writeNoException();
                    reply.writeStringArray(_result24);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg040 = data.readString();
                    int _result25 = getUidForSharedUser(_arg040);
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg041 = data.readInt();
                    int _result26 = getFlagsForUid(_arg041);
                    reply.writeNoException();
                    reply.writeInt(_result26);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg042 = data.readInt();
                    int _result27 = getPrivateFlagsForUid(_arg042);
                    reply.writeNoException();
                    reply.writeInt(_result27);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg043 = data.readInt();
                    boolean isUidPrivileged = isUidPrivileged(_arg043);
                    reply.writeNoException();
                    reply.writeInt(isUidPrivileged ? 1 : 0);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg044 = data.readString();
                    String[] _result28 = getAppOpPermissionPackages(_arg044);
                    reply.writeNoException();
                    reply.writeStringArray(_result28);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg045 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg126 = data.readString();
                    int _arg220 = data.readInt();
                    int _arg33 = data.readInt();
                    ResolveInfo _result29 = resolveIntent(_arg045, _arg126, _arg220, _arg33);
                    reply.writeNoException();
                    if (_result29 != null) {
                        reply.writeInt(1);
                        _result29.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg046 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    int _arg127 = data.readInt();
                    ResolveInfo _result30 = findPersistentPreferredActivity(_arg046, _arg127);
                    reply.writeNoException();
                    if (_result30 != null) {
                        reply.writeInt(1);
                        _result30.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg047 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg128 = data.readString();
                    int _arg221 = data.readInt();
                    int _arg34 = data.readInt();
                    boolean canForwardTo = canForwardTo(_arg047, _arg128, _arg221, _arg34);
                    reply.writeNoException();
                    reply.writeInt(canForwardTo ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg048 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg129 = data.readString();
                    int _arg222 = data.readInt();
                    int _arg35 = data.readInt();
                    ParceledListSlice _result31 = queryIntentActivities(_arg048, _arg129, _arg222, _arg35);
                    reply.writeNoException();
                    if (_result31 != null) {
                        reply.writeInt(1);
                        _result31.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    Intent[] _arg130 = (Intent[]) data.createTypedArray(Intent.CREATOR);
                    String[] _arg223 = data.createStringArray();
                    Intent _arg36 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg42 = data.readString();
                    int _arg5 = data.readInt();
                    int _arg6 = data.readInt();
                    ParceledListSlice _result32 = queryIntentActivityOptions(_arg02, _arg130, _arg223, _arg36, _arg42, _arg5, _arg6);
                    reply.writeNoException();
                    if (_result32 != null) {
                        reply.writeInt(1);
                        _result32.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg049 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg131 = data.readString();
                    int _arg224 = data.readInt();
                    int _arg37 = data.readInt();
                    ParceledListSlice _result33 = queryIntentReceivers(_arg049, _arg131, _arg224, _arg37);
                    reply.writeNoException();
                    if (_result33 != null) {
                        reply.writeInt(1);
                        _result33.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg050 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg132 = data.readString();
                    int _arg225 = data.readInt();
                    int _arg38 = data.readInt();
                    ResolveInfo _result34 = resolveService(_arg050, _arg132, _arg225, _arg38);
                    reply.writeNoException();
                    if (_result34 != null) {
                        reply.writeInt(1);
                        _result34.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg051 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg133 = data.readString();
                    int _arg226 = data.readInt();
                    int _arg39 = data.readInt();
                    ParceledListSlice _result35 = queryIntentServices(_arg051, _arg133, _arg226, _arg39);
                    reply.writeNoException();
                    if (_result35 != null) {
                        reply.writeInt(1);
                        _result35.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg052 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg134 = data.readString();
                    int _arg227 = data.readInt();
                    int _arg310 = data.readInt();
                    ParceledListSlice _result36 = queryIntentContentProviders(_arg052, _arg134, _arg227, _arg310);
                    reply.writeNoException();
                    if (_result36 != null) {
                        reply.writeInt(1);
                        _result36.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg053 = data.readInt();
                    int _arg135 = data.readInt();
                    ParceledListSlice _result37 = getInstalledPackages(_arg053, _arg135);
                    reply.writeNoException();
                    if (_result37 != null) {
                        reply.writeInt(1);
                        _result37.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg054 = data.createStringArray();
                    int _arg136 = data.readInt();
                    int _arg228 = data.readInt();
                    ParceledListSlice _result38 = getPackagesHoldingPermissions(_arg054, _arg136, _arg228);
                    reply.writeNoException();
                    if (_result38 != null) {
                        reply.writeInt(1);
                        _result38.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg055 = data.readInt();
                    int _arg137 = data.readInt();
                    ParceledListSlice _result39 = getInstalledApplications(_arg055, _arg137);
                    reply.writeNoException();
                    if (_result39 != null) {
                        reply.writeInt(1);
                        _result39.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg056 = data.readInt();
                    ParceledListSlice _result40 = getPersistentApplications(_arg056);
                    reply.writeNoException();
                    if (_result40 != null) {
                        reply.writeInt(1);
                        _result40.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg057 = data.readString();
                    int _arg138 = data.readInt();
                    int _arg229 = data.readInt();
                    ProviderInfo _result41 = resolveContentProvider(_arg057, _arg138, _arg229);
                    reply.writeNoException();
                    if (_result41 != null) {
                        reply.writeInt(1);
                        _result41.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg058 = data.createStringArrayList();
                    ArrayList createTypedArrayList = data.createTypedArrayList(ProviderInfo.CREATOR);
                    querySyncProviders(_arg058, createTypedArrayList);
                    reply.writeNoException();
                    reply.writeStringList(_arg058);
                    reply.writeTypedList(createTypedArrayList);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg059 = data.readString();
                    int _arg139 = data.readInt();
                    int _arg230 = data.readInt();
                    String _arg311 = data.readString();
                    ParceledListSlice _result42 = queryContentProviders(_arg059, _arg139, _arg230, _arg311);
                    reply.writeNoException();
                    if (_result42 != null) {
                        reply.writeInt(1);
                        _result42.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg060 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg140 = data.readInt();
                    InstrumentationInfo _result43 = getInstrumentationInfo(_arg060, _arg140);
                    reply.writeNoException();
                    if (_result43 != null) {
                        reply.writeInt(1);
                        _result43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg061 = data.readString();
                    int _arg141 = data.readInt();
                    ParceledListSlice _result44 = queryInstrumentation(_arg061, _arg141);
                    reply.writeNoException();
                    if (_result44 != null) {
                        reply.writeInt(1);
                        _result44.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg062 = data.readInt();
                    boolean _arg142 = data.readInt() != 0;
                    finishPackageInstall(_arg062, _arg142);
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg063 = data.readString();
                    String _arg143 = data.readString();
                    setInstallerPackageName(_arg063, _arg143);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg064 = data.readString();
                    int _arg144 = data.readInt();
                    String _arg231 = data.readString();
                    setApplicationCategoryHint(_arg064, _arg144, _arg231);
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg065 = data.readString();
                    int _arg145 = data.readInt();
                    IPackageDeleteObserver _arg232 = IPackageDeleteObserver.Stub.asInterface(data.readStrongBinder());
                    int _arg312 = data.readInt();
                    int _arg43 = data.readInt();
                    deletePackageAsUser(_arg065, _arg145, _arg232, _arg312, _arg43);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    VersionedPackage _arg066 = data.readInt() != 0 ? VersionedPackage.CREATOR.createFromParcel(data) : null;
                    IPackageDeleteObserver2 _arg146 = IPackageDeleteObserver2.Stub.asInterface(data.readStrongBinder());
                    int _arg233 = data.readInt();
                    int _arg313 = data.readInt();
                    deletePackageVersioned(_arg066, _arg146, _arg233, _arg313);
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg067 = data.readString();
                    String _result45 = getInstallerPackageName(_arg067);
                    reply.writeNoException();
                    reply.writeString(_result45);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg068 = data.readInt();
                    resetApplicationPreferences(_arg068);
                    reply.writeNoException();
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg069 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg147 = data.readString();
                    int _arg234 = data.readInt();
                    ResolveInfo _result46 = getLastChosenActivity(_arg069, _arg147, _arg234);
                    reply.writeNoException();
                    if (_result46 != null) {
                        reply.writeInt(1);
                        _result46.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    String _arg148 = data.readString();
                    int _arg235 = data.readInt();
                    if (data.readInt() != 0) {
                        IntentFilter _arg314 = IntentFilter.CREATOR.createFromParcel(data);
                        _arg3 = _arg314;
                    } else {
                        _arg3 = null;
                    }
                    int _arg44 = data.readInt();
                    ComponentName _arg52 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    setLastChosenActivity(_arg03, _arg148, _arg235, _arg3, _arg44, _arg52);
                    reply.writeNoException();
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    int _arg149 = data.readInt();
                    ComponentName[] _arg236 = (ComponentName[]) data.createTypedArray(ComponentName.CREATOR);
                    ComponentName _arg315 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg45 = data.readInt();
                    addPreferredActivity(_arg04, _arg149, _arg236, _arg315, _arg45);
                    reply.writeNoException();
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    int _arg150 = data.readInt();
                    ComponentName[] _arg237 = (ComponentName[]) data.createTypedArray(ComponentName.CREATOR);
                    ComponentName _arg316 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg46 = data.readInt();
                    replacePreferredActivity(_arg05, _arg150, _arg237, _arg316, _arg46);
                    reply.writeNoException();
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg070 = data.readString();
                    clearPackagePreferredActivities(_arg070);
                    reply.writeNoException();
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    String _arg238 = data.readString();
                    int _result47 = getPreferredActivities(arrayList, arrayList2, _arg238);
                    reply.writeNoException();
                    reply.writeInt(_result47);
                    reply.writeTypedList(arrayList);
                    reply.writeTypedList(arrayList2);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    ComponentName _arg151 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg239 = data.readInt();
                    addPersistentPreferredActivity(_arg06, _arg151, _arg239);
                    reply.writeNoException();
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg071 = data.readString();
                    int _arg152 = data.readInt();
                    clearPackagePersistentPreferredActivities(_arg071, _arg152);
                    reply.writeNoException();
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    IntentFilter _arg072 = data.readInt() != 0 ? IntentFilter.CREATOR.createFromParcel(data) : null;
                    String _arg153 = data.readString();
                    int _arg240 = data.readInt();
                    int _arg317 = data.readInt();
                    int _arg47 = data.readInt();
                    addCrossProfileIntentFilter(_arg072, _arg153, _arg240, _arg317, _arg47);
                    reply.writeNoException();
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg073 = data.readInt();
                    String _arg154 = data.readString();
                    clearCrossProfileIntentFilters(_arg073, _arg154);
                    reply.writeNoException();
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg074 = data.createStringArray();
                    boolean _arg155 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        PersistableBundle _arg241 = PersistableBundle.CREATOR.createFromParcel(data);
                        _arg2 = _arg241;
                    } else {
                        _arg2 = null;
                    }
                    PersistableBundle _arg318 = data.readInt() != 0 ? PersistableBundle.CREATOR.createFromParcel(data) : null;
                    String _arg48 = data.readString();
                    String _arg53 = data.readString();
                    int _arg62 = data.readInt();
                    String[] _result48 = setPackagesSuspendedAsUser(_arg074, _arg155, _arg2, _arg318, _arg48, _arg53, _arg62);
                    reply.writeNoException();
                    reply.writeStringArray(_result48);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg075 = data.readString();
                    int _arg156 = data.readInt();
                    boolean isPackageSuspendedForUser = isPackageSuspendedForUser(_arg075, _arg156);
                    reply.writeNoException();
                    reply.writeInt(isPackageSuspendedForUser ? 1 : 0);
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg076 = data.readString();
                    int _arg157 = data.readInt();
                    PersistableBundle _result49 = getSuspendedPackageAppExtras(_arg076, _arg157);
                    reply.writeNoException();
                    if (_result49 != null) {
                        reply.writeInt(1);
                        _result49.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg077 = data.readInt();
                    byte[] _result50 = getPreferredActivityBackup(_arg077);
                    reply.writeNoException();
                    reply.writeByteArray(_result50);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg078 = data.createByteArray();
                    int _arg158 = data.readInt();
                    restorePreferredActivities(_arg078, _arg158);
                    reply.writeNoException();
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg079 = data.readInt();
                    byte[] _result51 = getDefaultAppsBackup(_arg079);
                    reply.writeNoException();
                    reply.writeByteArray(_result51);
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg080 = data.createByteArray();
                    int _arg159 = data.readInt();
                    restoreDefaultApps(_arg080, _arg159);
                    reply.writeNoException();
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg081 = data.readInt();
                    byte[] _result52 = getIntentFilterVerificationBackup(_arg081);
                    reply.writeNoException();
                    reply.writeByteArray(_result52);
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg082 = data.createByteArray();
                    int _arg160 = data.readInt();
                    restoreIntentFilterVerification(_arg082, _arg160);
                    reply.writeNoException();
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg083 = data.readInt();
                    byte[] _result53 = getPermissionGrantBackup(_arg083);
                    reply.writeNoException();
                    reply.writeByteArray(_result53);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg084 = data.createByteArray();
                    int _arg161 = data.readInt();
                    restorePermissionGrants(_arg084, _arg161);
                    reply.writeNoException();
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    ArrayList arrayList3 = new ArrayList();
                    ComponentName _result54 = getHomeActivities(arrayList3);
                    reply.writeNoException();
                    if (_result54 != null) {
                        reply.writeInt(1);
                        _result54.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    reply.writeTypedList(arrayList3);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg085 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg162 = data.readInt();
                    setHomeActivity(_arg085, _arg162);
                    reply.writeNoException();
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg086 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg163 = data.readInt();
                    int _arg242 = data.readInt();
                    int _arg319 = data.readInt();
                    setComponentEnabledSetting(_arg086, _arg163, _arg242, _arg319);
                    reply.writeNoException();
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg087 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg164 = data.readInt();
                    int _result55 = getComponentEnabledSetting(_arg087, _arg164);
                    reply.writeNoException();
                    reply.writeInt(_result55);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg088 = data.readString();
                    int _arg165 = data.readInt();
                    int _arg243 = data.readInt();
                    int _arg320 = data.readInt();
                    String _arg49 = data.readString();
                    setApplicationEnabledSetting(_arg088, _arg165, _arg243, _arg320, _arg49);
                    reply.writeNoException();
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg089 = data.readString();
                    int _arg166 = data.readInt();
                    int _result56 = getApplicationEnabledSetting(_arg089, _arg166);
                    reply.writeNoException();
                    reply.writeInt(_result56);
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg090 = data.readString();
                    int _arg167 = data.readInt();
                    String _arg244 = data.readString();
                    String _arg321 = data.readString();
                    int _arg410 = data.readInt();
                    logAppProcessStartIfNeeded(_arg090, _arg167, _arg244, _arg321, _arg410);
                    reply.writeNoException();
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg091 = data.readInt();
                    flushPackageRestrictionsAsUser(_arg091);
                    reply.writeNoException();
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg092 = data.readString();
                    boolean _arg168 = data.readInt() != 0;
                    int _arg245 = data.readInt();
                    setPackageStoppedState(_arg092, _arg168, _arg245);
                    reply.writeNoException();
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg093 = data.readString();
                    long _arg169 = data.readLong();
                    int _arg246 = data.readInt();
                    IPackageDataObserver _arg322 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    freeStorageAndNotify(_arg093, _arg169, _arg246, _arg322);
                    reply.writeNoException();
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg094 = data.readString();
                    long _arg170 = data.readLong();
                    int _arg247 = data.readInt();
                    IntentSender _arg323 = data.readInt() != 0 ? IntentSender.CREATOR.createFromParcel(data) : null;
                    freeStorage(_arg094, _arg170, _arg247, _arg323);
                    reply.writeNoException();
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg095 = data.readString();
                    IPackageDataObserver _arg171 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    deleteApplicationCacheFiles(_arg095, _arg171);
                    reply.writeNoException();
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg096 = data.readString();
                    int _arg172 = data.readInt();
                    IPackageDataObserver _arg248 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    deleteApplicationCacheFilesAsUser(_arg096, _arg172, _arg248);
                    reply.writeNoException();
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg097 = data.readString();
                    IPackageDataObserver _arg173 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    int _arg249 = data.readInt();
                    clearApplicationUserData(_arg097, _arg173, _arg249);
                    reply.writeNoException();
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg098 = data.readString();
                    clearApplicationProfileData(_arg098);
                    reply.writeNoException();
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg099 = data.readString();
                    clearDalvikCache(_arg099);
                    reply.writeNoException();
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0100 = data.readString();
                    int _arg174 = data.readInt();
                    IPackageStatsObserver _arg250 = IPackageStatsObserver.Stub.asInterface(data.readStrongBinder());
                    getPackageSizeInfo(_arg0100, _arg174, _arg250);
                    reply.writeNoException();
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result57 = getSystemSharedLibraryNames();
                    reply.writeNoException();
                    reply.writeStringArray(_result57);
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    ParceledListSlice _result58 = getSystemAvailableFeatures();
                    reply.writeNoException();
                    if (_result58 != null) {
                        reply.writeInt(1);
                        _result58.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0101 = data.readString();
                    int _arg175 = data.readInt();
                    boolean hasSystemFeature = hasSystemFeature(_arg0101, _arg175);
                    reply.writeNoException();
                    reply.writeInt(hasSystemFeature ? 1 : 0);
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    enterSafeMode();
                    reply.writeNoException();
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSafeMode = isSafeMode();
                    reply.writeNoException();
                    reply.writeInt(isSafeMode ? 1 : 0);
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    systemReady();
                    reply.writeNoException();
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasSystemUidErrors = hasSystemUidErrors();
                    reply.writeNoException();
                    reply.writeInt(hasSystemUidErrors ? 1 : 0);
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    performFstrimIfNeeded();
                    reply.writeNoException();
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    updatePackagesIfNeeded();
                    reply.writeNoException();
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0102 = data.readString();
                    int _arg176 = data.readInt();
                    notifyPackageUse(_arg0102, _arg176);
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0103 = data.readString();
                    List<String> _arg177 = data.createStringArrayList();
                    List<String> _arg251 = data.createStringArrayList();
                    String _arg324 = data.readString();
                    notifyDexLoad(_arg0103, _arg177, _arg251, _arg324);
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0104 = data.readString();
                    String _arg178 = data.readString();
                    boolean _arg252 = data.readInt() != 0;
                    IDexModuleRegisterCallback _arg325 = IDexModuleRegisterCallback.Stub.asInterface(data.readStrongBinder());
                    registerDexModule(_arg0104, _arg178, _arg252, _arg325);
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0105 = data.readString();
                    boolean _arg179 = data.readInt() != 0;
                    String _arg253 = data.readString();
                    boolean _arg326 = data.readInt() != 0;
                    boolean _arg411 = data.readInt() != 0;
                    String _arg54 = data.readString();
                    boolean performDexOptMode = performDexOptMode(_arg0105, _arg179, _arg253, _arg326, _arg411, _arg54);
                    reply.writeNoException();
                    reply.writeInt(performDexOptMode ? 1 : 0);
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0106 = data.readString();
                    String _arg180 = data.readString();
                    boolean _arg254 = data.readInt() != 0;
                    boolean performDexOptSecondary = performDexOptSecondary(_arg0106, _arg180, _arg254);
                    reply.writeNoException();
                    reply.writeInt(performDexOptSecondary ? 1 : 0);
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0107 = data.readString();
                    dumpProfiles(_arg0107);
                    reply.writeNoException();
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0108 = data.readString();
                    forceDexOpt(_arg0108);
                    reply.writeNoException();
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg0109 = data.createStringArrayList();
                    boolean runBackgroundDexoptJob = runBackgroundDexoptJob(_arg0109);
                    reply.writeNoException();
                    reply.writeInt(runBackgroundDexoptJob ? 1 : 0);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0110 = data.readString();
                    reconcileSecondaryDexFiles(_arg0110);
                    reply.writeNoException();
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    PackageCleanItem _arg0111 = data.readInt() != 0 ? PackageCleanItem.CREATOR.createFromParcel(data) : null;
                    PackageCleanItem _result59 = nextPackageToClean(_arg0111);
                    reply.writeNoException();
                    if (_result59 != null) {
                        reply.writeInt(1);
                        _result59.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0112 = data.readInt();
                    int _result60 = getMoveStatus(_arg0112);
                    reply.writeNoException();
                    reply.writeInt(_result60);
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    IPackageMoveObserver _arg0113 = IPackageMoveObserver.Stub.asInterface(data.readStrongBinder());
                    registerMoveCallback(_arg0113);
                    reply.writeNoException();
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    IPackageMoveObserver _arg0114 = IPackageMoveObserver.Stub.asInterface(data.readStrongBinder());
                    unregisterMoveCallback(_arg0114);
                    reply.writeNoException();
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0115 = data.readString();
                    String _arg181 = data.readString();
                    int _result61 = movePackage(_arg0115, _arg181);
                    reply.writeNoException();
                    reply.writeInt(_result61);
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0116 = data.readString();
                    int _result62 = movePrimaryStorage(_arg0116);
                    reply.writeNoException();
                    reply.writeInt(_result62);
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    PermissionInfo _arg0117 = data.readInt() != 0 ? PermissionInfo.CREATOR.createFromParcel(data) : null;
                    boolean addPermissionAsync = addPermissionAsync(_arg0117);
                    reply.writeNoException();
                    reply.writeInt(addPermissionAsync ? 1 : 0);
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0118 = data.readInt();
                    boolean installLocation = setInstallLocation(_arg0118);
                    reply.writeNoException();
                    reply.writeInt(installLocation ? 1 : 0);
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    int _result63 = getInstallLocation();
                    reply.writeNoException();
                    reply.writeInt(_result63);
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0119 = data.readString();
                    int _arg182 = data.readInt();
                    int _arg255 = data.readInt();
                    int _arg327 = data.readInt();
                    int _result64 = installExistingPackageAsUser(_arg0119, _arg182, _arg255, _arg327);
                    reply.writeNoException();
                    reply.writeInt(_result64);
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0120 = data.readInt();
                    int _arg183 = data.readInt();
                    verifyPendingInstall(_arg0120, _arg183);
                    reply.writeNoException();
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0121 = data.readInt();
                    int _arg184 = data.readInt();
                    long _arg256 = data.readLong();
                    extendVerificationTimeout(_arg0121, _arg184, _arg256);
                    reply.writeNoException();
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0122 = data.readInt();
                    int _arg185 = data.readInt();
                    List<String> _arg257 = data.createStringArrayList();
                    verifyIntentFilter(_arg0122, _arg185, _arg257);
                    reply.writeNoException();
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0123 = data.readString();
                    int _arg186 = data.readInt();
                    int _result65 = getIntentVerificationStatus(_arg0123, _arg186);
                    reply.writeNoException();
                    reply.writeInt(_result65);
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0124 = data.readString();
                    int _arg187 = data.readInt();
                    int _arg258 = data.readInt();
                    boolean updateIntentVerificationStatus = updateIntentVerificationStatus(_arg0124, _arg187, _arg258);
                    reply.writeNoException();
                    reply.writeInt(updateIntentVerificationStatus ? 1 : 0);
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0125 = data.readString();
                    ParceledListSlice _result66 = getIntentFilterVerifications(_arg0125);
                    reply.writeNoException();
                    if (_result66 != null) {
                        reply.writeInt(1);
                        _result66.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0126 = data.readString();
                    ParceledListSlice _result67 = getAllIntentFilters(_arg0126);
                    reply.writeNoException();
                    if (_result67 != null) {
                        reply.writeInt(1);
                        _result67.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0127 = data.readString();
                    int _arg188 = data.readInt();
                    boolean defaultBrowserPackageName = setDefaultBrowserPackageName(_arg0127, _arg188);
                    reply.writeNoException();
                    reply.writeInt(defaultBrowserPackageName ? 1 : 0);
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0128 = data.readInt();
                    String _result68 = getDefaultBrowserPackageName(_arg0128);
                    reply.writeNoException();
                    reply.writeString(_result68);
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    VerifierDeviceIdentity _result69 = getVerifierDeviceIdentity();
                    reply.writeNoException();
                    if (_result69 != null) {
                        reply.writeInt(1);
                        _result69.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isFirstBoot = isFirstBoot();
                    reply.writeNoException();
                    reply.writeInt(isFirstBoot ? 1 : 0);
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isOnlyCoreApps = isOnlyCoreApps();
                    reply.writeNoException();
                    reply.writeInt(isOnlyCoreApps ? 1 : 0);
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isUpgrade = isUpgrade();
                    reply.writeNoException();
                    reply.writeInt(isUpgrade ? 1 : 0);
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0129 = data.readString();
                    boolean _arg189 = data.readInt() != 0;
                    setPermissionEnforced(_arg0129, _arg189);
                    reply.writeNoException();
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0130 = data.readString();
                    boolean isPermissionEnforced = isPermissionEnforced(_arg0130);
                    reply.writeNoException();
                    reply.writeInt(isPermissionEnforced ? 1 : 0);
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isStorageLow = isStorageLow();
                    reply.writeNoException();
                    reply.writeInt(isStorageLow ? 1 : 0);
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0131 = data.readString();
                    boolean _arg190 = data.readInt() != 0;
                    int _arg259 = data.readInt();
                    boolean applicationHiddenSettingAsUser = setApplicationHiddenSettingAsUser(_arg0131, _arg190, _arg259);
                    reply.writeNoException();
                    reply.writeInt(applicationHiddenSettingAsUser ? 1 : 0);
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0132 = data.readString();
                    int _arg191 = data.readInt();
                    boolean applicationHiddenSettingAsUser2 = getApplicationHiddenSettingAsUser(_arg0132, _arg191);
                    reply.writeNoException();
                    reply.writeInt(applicationHiddenSettingAsUser2 ? 1 : 0);
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0133 = data.readString();
                    boolean _arg192 = data.readInt() != 0;
                    setSystemAppHiddenUntilInstalled(_arg0133, _arg192);
                    reply.writeNoException();
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0134 = data.readString();
                    boolean _arg193 = data.readInt() != 0;
                    int _arg260 = data.readInt();
                    boolean systemAppInstallState = setSystemAppInstallState(_arg0134, _arg193, _arg260);
                    reply.writeNoException();
                    reply.writeInt(systemAppInstallState ? 1 : 0);
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    IPackageInstaller _result70 = getPackageInstaller();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result70 != null ? _result70.asBinder() : null);
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0135 = data.readString();
                    boolean _arg194 = data.readInt() != 0;
                    int _arg261 = data.readInt();
                    boolean blockUninstallForUser = setBlockUninstallForUser(_arg0135, _arg194, _arg261);
                    reply.writeNoException();
                    reply.writeInt(blockUninstallForUser ? 1 : 0);
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0136 = data.readString();
                    int _arg195 = data.readInt();
                    boolean blockUninstallForUser2 = getBlockUninstallForUser(_arg0136, _arg195);
                    reply.writeNoException();
                    reply.writeInt(blockUninstallForUser2 ? 1 : 0);
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0137 = data.readString();
                    String _arg196 = data.readString();
                    KeySet _result71 = getKeySetByAlias(_arg0137, _arg196);
                    reply.writeNoException();
                    if (_result71 != null) {
                        reply.writeInt(1);
                        _result71.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0138 = data.readString();
                    KeySet _result72 = getSigningKeySet(_arg0138);
                    reply.writeNoException();
                    if (_result72 != null) {
                        reply.writeInt(1);
                        _result72.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0139 = data.readString();
                    KeySet _arg197 = data.readInt() != 0 ? KeySet.CREATOR.createFromParcel(data) : null;
                    boolean isPackageSignedByKeySet = isPackageSignedByKeySet(_arg0139, _arg197);
                    reply.writeNoException();
                    reply.writeInt(isPackageSignedByKeySet ? 1 : 0);
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0140 = data.readString();
                    KeySet _arg198 = data.readInt() != 0 ? KeySet.CREATOR.createFromParcel(data) : null;
                    boolean isPackageSignedByKeySetExactly = isPackageSignedByKeySetExactly(_arg0140, _arg198);
                    reply.writeNoException();
                    reply.writeInt(isPackageSignedByKeySetExactly ? 1 : 0);
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    IOnPermissionsChangeListener _arg0141 = IOnPermissionsChangeListener.Stub.asInterface(data.readStrongBinder());
                    addOnPermissionsChangeListener(_arg0141);
                    reply.writeNoException();
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    IOnPermissionsChangeListener _arg0142 = IOnPermissionsChangeListener.Stub.asInterface(data.readStrongBinder());
                    removeOnPermissionsChangeListener(_arg0142);
                    reply.writeNoException();
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg0143 = data.createStringArray();
                    int _arg199 = data.readInt();
                    grantDefaultPermissionsToEnabledCarrierApps(_arg0143, _arg199);
                    reply.writeNoException();
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg0144 = data.createStringArray();
                    int _arg1100 = data.readInt();
                    grantDefaultPermissionsToEnabledImsServices(_arg0144, _arg1100);
                    reply.writeNoException();
                    return true;
                case 164:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg0145 = data.createStringArray();
                    int _arg1101 = data.readInt();
                    grantDefaultPermissionsToEnabledTelephonyDataServices(_arg0145, _arg1101);
                    reply.writeNoException();
                    return true;
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg0146 = data.createStringArray();
                    int _arg1102 = data.readInt();
                    revokeDefaultPermissionsFromDisabledTelephonyDataServices(_arg0146, _arg1102);
                    reply.writeNoException();
                    return true;
                case 166:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0147 = data.readString();
                    int _arg1103 = data.readInt();
                    grantDefaultPermissionsToActiveLuiApp(_arg0147, _arg1103);
                    reply.writeNoException();
                    return true;
                case 167:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg0148 = data.createStringArray();
                    int _arg1104 = data.readInt();
                    revokeDefaultPermissionsFromLuiApps(_arg0148, _arg1104);
                    reply.writeNoException();
                    return true;
                case 168:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0149 = data.readString();
                    String _arg1105 = data.readString();
                    int _arg262 = data.readInt();
                    boolean isPermissionRevokedByPolicy = isPermissionRevokedByPolicy(_arg0149, _arg1105, _arg262);
                    reply.writeNoException();
                    reply.writeInt(isPermissionRevokedByPolicy ? 1 : 0);
                    return true;
                case 169:
                    data.enforceInterface(DESCRIPTOR);
                    String _result73 = getPermissionControllerPackageName();
                    reply.writeNoException();
                    reply.writeString(_result73);
                    return true;
                case 170:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0150 = data.readInt();
                    ParceledListSlice _result74 = getInstantApps(_arg0150);
                    reply.writeNoException();
                    if (_result74 != null) {
                        reply.writeInt(1);
                        _result74.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 171:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0151 = data.readString();
                    int _arg1106 = data.readInt();
                    byte[] _result75 = getInstantAppCookie(_arg0151, _arg1106);
                    reply.writeNoException();
                    reply.writeByteArray(_result75);
                    return true;
                case 172:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0152 = data.readString();
                    byte[] _arg1107 = data.createByteArray();
                    int _arg263 = data.readInt();
                    boolean instantAppCookie = setInstantAppCookie(_arg0152, _arg1107, _arg263);
                    reply.writeNoException();
                    reply.writeInt(instantAppCookie ? 1 : 0);
                    return true;
                case 173:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0153 = data.readString();
                    int _arg1108 = data.readInt();
                    Bitmap _result76 = getInstantAppIcon(_arg0153, _arg1108);
                    reply.writeNoException();
                    if (_result76 != null) {
                        reply.writeInt(1);
                        _result76.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 174:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0154 = data.readString();
                    int _arg1109 = data.readInt();
                    boolean isInstantApp = isInstantApp(_arg0154, _arg1109);
                    reply.writeNoException();
                    reply.writeInt(isInstantApp ? 1 : 0);
                    return true;
                case 175:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0155 = data.readString();
                    boolean _arg1110 = data.readInt() != 0;
                    boolean requiredForSystemUser = setRequiredForSystemUser(_arg0155, _arg1110);
                    reply.writeNoException();
                    reply.writeInt(requiredForSystemUser ? 1 : 0);
                    return true;
                case 176:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0156 = data.readString();
                    boolean _arg1111 = data.readInt() != 0;
                    setUpdateAvailable(_arg0156, _arg1111);
                    reply.writeNoException();
                    return true;
                case 177:
                    data.enforceInterface(DESCRIPTOR);
                    String _result77 = getServicesSystemSharedLibraryPackageName();
                    reply.writeNoException();
                    reply.writeString(_result77);
                    return true;
                case 178:
                    data.enforceInterface(DESCRIPTOR);
                    String _result78 = getSharedSystemSharedLibraryPackageName();
                    reply.writeNoException();
                    reply.writeString(_result78);
                    return true;
                case 179:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0157 = data.readInt();
                    int _arg1112 = data.readInt();
                    ChangedPackages _result79 = getChangedPackages(_arg0157, _arg1112);
                    reply.writeNoException();
                    if (_result79 != null) {
                        reply.writeInt(1);
                        _result79.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 180:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0158 = data.readString();
                    boolean isPackageDeviceAdminOnAnyUser = isPackageDeviceAdminOnAnyUser(_arg0158);
                    reply.writeNoException();
                    reply.writeInt(isPackageDeviceAdminOnAnyUser ? 1 : 0);
                    return true;
                case 181:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0159 = data.readString();
                    int _arg1113 = data.readInt();
                    int _result80 = getInstallReason(_arg0159, _arg1113);
                    reply.writeNoException();
                    reply.writeInt(_result80);
                    return true;
                case 182:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0160 = data.readString();
                    int _arg1114 = data.readInt();
                    int _arg264 = data.readInt();
                    ParceledListSlice _result81 = getSharedLibraries(_arg0160, _arg1114, _arg264);
                    reply.writeNoException();
                    if (_result81 != null) {
                        reply.writeInt(1);
                        _result81.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 183:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0161 = data.readString();
                    int _arg1115 = data.readInt();
                    boolean canRequestPackageInstalls = canRequestPackageInstalls(_arg0161, _arg1115);
                    reply.writeNoException();
                    reply.writeInt(canRequestPackageInstalls ? 1 : 0);
                    return true;
                case 184:
                    data.enforceInterface(DESCRIPTOR);
                    deletePreloadsFileCache();
                    reply.writeNoException();
                    return true;
                case 185:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _result82 = getInstantAppResolverComponent();
                    reply.writeNoException();
                    if (_result82 != null) {
                        reply.writeInt(1);
                        _result82.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 186:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _result83 = getInstantAppResolverSettingsComponent();
                    reply.writeNoException();
                    if (_result83 != null) {
                        reply.writeInt(1);
                        _result83.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 187:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _result84 = getInstantAppInstallerComponent();
                    reply.writeNoException();
                    if (_result84 != null) {
                        reply.writeInt(1);
                        _result84.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 188:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0162 = data.readString();
                    int _arg1116 = data.readInt();
                    String _result85 = getInstantAppAndroidId(_arg0162, _arg1116);
                    reply.writeNoException();
                    reply.writeString(_result85);
                    return true;
                case 189:
                    data.enforceInterface(DESCRIPTOR);
                    IArtManager _result86 = getArtManager();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result86 != null ? _result86.asBinder() : null);
                    return true;
                case 190:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0163 = data.readString();
                    CharSequence _arg1117 = data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null;
                    int _arg265 = data.readInt();
                    setHarmfulAppWarning(_arg0163, _arg1117, _arg265);
                    reply.writeNoException();
                    return true;
                case 191:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0164 = data.readString();
                    int _arg1118 = data.readInt();
                    CharSequence _result87 = getHarmfulAppWarning(_arg0164, _arg1118);
                    reply.writeNoException();
                    if (_result87 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result87, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 192:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0165 = data.readString();
                    byte[] _arg1119 = data.createByteArray();
                    int _arg266 = data.readInt();
                    boolean hasSigningCertificate = hasSigningCertificate(_arg0165, _arg1119, _arg266);
                    reply.writeNoException();
                    reply.writeInt(hasSigningCertificate ? 1 : 0);
                    return true;
                case 193:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0166 = data.readInt();
                    byte[] _arg1120 = data.createByteArray();
                    int _arg267 = data.readInt();
                    boolean hasUidSigningCertificate = hasUidSigningCertificate(_arg0166, _arg1120, _arg267);
                    reply.writeNoException();
                    reply.writeInt(hasUidSigningCertificate ? 1 : 0);
                    return true;
                case 194:
                    data.enforceInterface(DESCRIPTOR);
                    String _result88 = getSystemTextClassifierPackageName();
                    reply.writeNoException();
                    reply.writeString(_result88);
                    return true;
                case 195:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0167 = data.readString();
                    int _arg1121 = data.readInt();
                    boolean isPackageStateProtected = isPackageStateProtected(_arg0167, _arg1121);
                    reply.writeNoException();
                    reply.writeInt(isPackageStateProtected ? 1 : 0);
                    return true;
                case 196:
                    data.enforceInterface(DESCRIPTOR);
                    ApplicationInfo _arg0168 = data.readInt() != 0 ? ApplicationInfo.CREATOR.createFromParcel(data) : null;
                    ApplicationInfo _result89 = getOverlayApplicationInfo(_arg0168);
                    reply.writeNoException();
                    if (_result89 != null) {
                        reply.writeInt(1);
                        _result89.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 197:
                    data.enforceInterface(DESCRIPTOR);
                    ActivityInfo _arg0169 = data.readInt() != 0 ? ActivityInfo.CREATOR.createFromParcel(data) : null;
                    ActivityInfo _result90 = getOverlayActivityInfo(_arg0169);
                    reply.writeNoException();
                    if (_result90 != null) {
                        reply.writeInt(1);
                        _result90.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 198:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0170 = data.readString();
                    xpPackageInfo _result91 = getXpPackageInfo(_arg0170);
                    reply.writeNoException();
                    if (_result91 != null) {
                        reply.writeInt(1);
                        _result91.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 199:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0171 = data.readString();
                    String _arg1122 = data.readString();
                    setXpPackageInfo(_arg0171, _arg1122);
                    reply.writeNoException();
                    return true;
                case 200:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0172 = data.readString();
                    boolean _arg1123 = data.readInt() != 0;
                    int _arg268 = data.readInt();
                    setPackageNotLaunched(_arg0172, _arg1123, _arg268);
                    reply.writeNoException();
                    return true;
                case 201:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0173 = data.readInt();
                    updateAppScreenFlag(_arg0173);
                    reply.writeNoException();
                    return true;
                case 202:
                    data.enforceInterface(DESCRIPTOR);
                    List<xpPackageInfo> _result92 = getAllXpPackageInfos();
                    reply.writeNoException();
                    reply.writeTypedList(_result92);
                    return true;
                case 203:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0174 = data.readInt();
                    List<xpAppInfo> _result93 = getXpAppPackageList(_arg0174);
                    reply.writeNoException();
                    reply.writeTypedList(_result93);
                    return true;
                case 204:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0175 = data.readString();
                    Bitmap _result94 = getXpAppIcon(_arg0175);
                    reply.writeNoException();
                    if (_result94 != null) {
                        reply.writeInt(1);
                        _result94.writeToParcel(reply, 1);
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
            private IBinder mRemote;

            public private protected Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void checkPackageStartable(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isPackageAvailable(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
                PackageInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(3, _data, _reply, 0);
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
            public synchronized PackageInfo getPackageInfoVersioned(VersionedPackage versionedPackage, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(4, _data, _reply, 0);
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

            public synchronized int getPackageUid(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized int[] getPackageGids(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String[] currentToCanonicalPackageNames(String[] names) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(names);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String[] canonicalToCurrentPackageNames(String[] names) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(names);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized PermissionInfo getPermissionInfo(String name, String packageName, int flags) throws RemoteException {
                PermissionInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    this.mRemote.transact(9, _data, _reply, 0);
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
            public synchronized ParceledListSlice queryPermissionsByGroup(String group, int flags) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(group);
                    _data.writeInt(flags);
                    this.mRemote.transact(10, _data, _reply, 0);
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

            public synchronized PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws RemoteException {
                PermissionGroupInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(flags);
                    this.mRemote.transact(11, _data, _reply, 0);
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
            public synchronized ParceledListSlice getAllPermissionGroups(int flags) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    this.mRemote.transact(12, _data, _reply, 0);
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

            public synchronized ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws RemoteException {
                ApplicationInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(13, _data, _reply, 0);
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

            public synchronized ActivityInfo getActivityInfo(ComponentName className, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(14, _data, _reply, 0);
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
            public synchronized boolean activitySupportsIntent(ComponentName className, Intent intent, String resolvedType) throws RemoteException {
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
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized ActivityInfo getReceiverInfo(ComponentName className, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(16, _data, _reply, 0);
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

            public synchronized ServiceInfo getServiceInfo(ComponentName className, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(17, _data, _reply, 0);
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

            public synchronized ProviderInfo getProviderInfo(ComponentName className, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(18, _data, _reply, 0);
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

            public synchronized int checkPermission(String permName, String pkgName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permName);
                    _data.writeString(pkgName);
                    _data.writeInt(userId);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected int checkUidPermission(String permName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permName);
                    _data.writeInt(uid);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean addPermission(PermissionInfo info) throws RemoteException {
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
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void removePermission(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void grantRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permissionName);
                    _data.writeInt(userId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void revokeRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permissionName);
                    _data.writeInt(userId);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void resetRuntimePermissions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized int getPermissionFlags(String permissionName, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permissionName);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void updatePermissionFlags(String permissionName, String packageName, int flagMask, int flagValues, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permissionName);
                    _data.writeString(packageName);
                    _data.writeInt(flagMask);
                    _data.writeInt(flagValues);
                    _data.writeInt(userId);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void updatePermissionFlagsForAllApps(int flagMask, int flagValues, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flagMask);
                    _data.writeInt(flagValues);
                    _data.writeInt(userId);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean shouldShowRequestPermissionRationale(String permissionName, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permissionName);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isProtectedBroadcast(String actionName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(actionName);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int checkSignatures(String pkg1, String pkg2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg1);
                    _data.writeString(pkg2);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int checkUidSignatures(int uid1, int uid2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid1);
                    _data.writeInt(uid2);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized List<String> getAllPackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected String[] getPackagesForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getNameForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized String[] getNamesForUids(int[] uids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(uids);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getUidForSharedUser(String sharedUserName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sharedUserName);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getFlagsForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized int getPrivateFlagsForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isUidPrivileged(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected String[] getAppOpPermissionPackages(String permissionName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permissionName);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(42, _data, _reply, 0);
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
            public synchronized ResolveInfo findPersistentPreferredActivity(Intent intent, int userId) throws RemoteException {
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
                    this.mRemote.transact(43, _data, _reply, 0);
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
            public synchronized boolean canForwardTo(Intent intent, String resolvedType, int sourceUserId, int targetUserId) throws RemoteException {
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
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized ParceledListSlice queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(45, _data, _reply, 0);
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
            public synchronized ParceledListSlice queryIntentActivityOptions(ComponentName caller, Intent[] specifics, String[] specificTypes, Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
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
                    _data.writeTypedArray(specifics, 0);
                    _data.writeStringArray(specificTypes);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(46, _data, _reply, 0);
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
            public synchronized ParceledListSlice queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(47, _data, _reply, 0);
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
            public synchronized ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(48, _data, _reply, 0);
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
            public synchronized ParceledListSlice queryIntentServices(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(49, _data, _reply, 0);
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
            public synchronized ParceledListSlice queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(50, _data, _reply, 0);
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

            private protected ParceledListSlice getInstalledPackages(int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(51, _data, _reply, 0);
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
            public synchronized ParceledListSlice getPackagesHoldingPermissions(String[] permissions, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(permissions);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(52, _data, _reply, 0);
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

            public synchronized ParceledListSlice getInstalledApplications(int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(53, _data, _reply, 0);
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
            public synchronized ParceledListSlice getPersistentApplications(int flags) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    this.mRemote.transact(54, _data, _reply, 0);
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
            public synchronized ProviderInfo resolveContentProvider(String name, int flags, int userId) throws RemoteException {
                ProviderInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(55, _data, _reply, 0);
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

            public synchronized void querySyncProviders(List<String> outNames, List<ProviderInfo> outInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(outNames);
                    _data.writeTypedList(outInfo);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    _reply.readStringList(outNames);
                    _reply.readTypedList(outInfo, ProviderInfo.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized ParceledListSlice queryContentProviders(String processName, int uid, int flags, String metaDataKey) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    _data.writeInt(flags);
                    _data.writeString(metaDataKey);
                    this.mRemote.transact(57, _data, _reply, 0);
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

            public synchronized InstrumentationInfo getInstrumentationInfo(ComponentName className, int flags) throws RemoteException {
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
                    this.mRemote.transact(58, _data, _reply, 0);
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

            public synchronized ParceledListSlice queryInstrumentation(String targetPackage, int flags) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(targetPackage);
                    _data.writeInt(flags);
                    this.mRemote.transact(59, _data, _reply, 0);
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
            public synchronized void finishPackageInstall(int token, boolean didLaunch) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(didLaunch ? 1 : 0);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setInstallerPackageName(String targetPackage, String installerPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(targetPackage);
                    _data.writeString(installerPackageName);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void setApplicationCategoryHint(String packageName, int categoryHint, String callerPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(categoryHint);
                    _data.writeString(callerPackageName);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void deletePackageAsUser(String packageName, int versionCode, IPackageDeleteObserver observer, int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(versionCode);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void deletePackageVersioned(VersionedPackage versionedPackage, IPackageDeleteObserver2 observer, int userId, int flags) throws RemoteException {
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
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getInstallerPackageName(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void resetApplicationPreferences(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected ResolveInfo getLastChosenActivity(Intent intent, String resolvedType, int flags) throws RemoteException {
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
                    this.mRemote.transact(67, _data, _reply, 0);
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

            public synchronized void setLastChosenActivity(Intent intent, String resolvedType, int flags, IntentFilter filter, int match, ComponentName activity) throws RemoteException {
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
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(match);
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) throws RemoteException {
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
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void replacePreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) throws RemoteException {
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
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void clearPackagePreferredActivities(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getPreferredActivities(List<IntentFilter> outFilters, List<ComponentName> outActivities, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(72, _data, _reply, 0);
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
            public synchronized void addPersistentPreferredActivity(IntentFilter filter, ComponentName activity, int userId) throws RemoteException {
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
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void clearPackagePersistentPreferredActivities(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void addCrossProfileIntentFilter(IntentFilter intentFilter, String ownerPackage, int sourceUserId, int targetUserId, int flags) throws RemoteException {
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
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void clearCrossProfileIntentFilters(int sourceUserId, String ownerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sourceUserId);
                    _data.writeString(ownerPackage);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized String[] setPackagesSuspendedAsUser(String[] packageNames, boolean suspended, PersistableBundle appExtras, PersistableBundle launcherExtras, String dialogMessage, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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
                    _data.writeString(dialogMessage);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean isPackageSuspendedForUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized PersistableBundle getSuspendedPackageAppExtras(String packageName, int userId) throws RemoteException {
                PersistableBundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(79, _data, _reply, 0);
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
            public synchronized byte[] getPreferredActivityBackup(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void restorePreferredActivities(byte[] backup, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(backup);
                    _data.writeInt(userId);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized byte[] getDefaultAppsBackup(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void restoreDefaultApps(byte[] backup, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(backup);
                    _data.writeInt(userId);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized byte[] getIntentFilterVerificationBackup(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void restoreIntentFilterVerification(byte[] backup, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(backup);
                    _data.writeInt(userId);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized byte[] getPermissionGrantBackup(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void restorePermissionGrants(byte[] backup, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(backup);
                    _data.writeInt(userId);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized ComponentName getHomeActivities(List<ResolveInfo> outHomeCandidates) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(88, _data, _reply, 0);
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
            public synchronized void setHomeActivity(ComponentName className, int userId) throws RemoteException {
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
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) throws RemoteException {
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
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getComponentEnabledSetting(ComponentName componentName, int userId) throws RemoteException {
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
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(newState);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getApplicationEnabledSetting(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void logAppProcessStartIfNeeded(String processName, int uid, String seinfo, String apkFile, int pid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    _data.writeString(seinfo);
                    _data.writeString(apkFile);
                    _data.writeInt(pid);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void flushPackageRestrictionsAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setPackageStoppedState(String packageName, boolean stopped, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(stopped ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void freeStorageAndNotify(String volumeUuid, long freeStorageSize, int storageFlags, IPackageDataObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeLong(freeStorageSize);
                    _data.writeInt(storageFlags);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void freeStorage(String volumeUuid, long freeStorageSize, int storageFlags, IntentSender pi) throws RemoteException {
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
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void deleteApplicationCacheFiles(String packageName, IPackageDataObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void deleteApplicationCacheFilesAsUser(String packageName, int userId, IPackageDataObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void clearApplicationUserData(String packageName, IPackageDataObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void clearApplicationProfileData(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public void clearDalvikCache(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void getPackageSizeInfo(String packageName, int userHandle, IPackageStatsObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected String[] getSystemSharedLibraryNames() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized ParceledListSlice getSystemAvailableFeatures() throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(106, _data, _reply, 0);
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
            public synchronized boolean hasSystemFeature(String name, int version) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(version);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void enterSafeMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isSafeMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void systemReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean hasSystemUidErrors() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(111, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void performFstrimIfNeeded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(112, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void updatePackagesIfNeeded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void notifyPackageUse(String packageName, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(reason);
                    this.mRemote.transact(114, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void notifyDexLoad(String loadingPackageName, List<String> classLoadersNames, List<String> classPaths, String loaderIsa) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(loadingPackageName);
                    _data.writeStringList(classLoadersNames);
                    _data.writeStringList(classPaths);
                    _data.writeString(loaderIsa);
                    this.mRemote.transact(115, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void registerDexModule(String packageName, String dexModulePath, boolean isSharedModule, IDexModuleRegisterCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(dexModulePath);
                    _data.writeInt(isSharedModule ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(116, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean performDexOptMode(String packageName, boolean checkProfiles, String targetCompilerFilter, boolean force, boolean bootComplete, String splitName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(checkProfiles ? 1 : 0);
                    _data.writeString(targetCompilerFilter);
                    _data.writeInt(force ? 1 : 0);
                    _data.writeInt(bootComplete ? 1 : 0);
                    _data.writeString(splitName);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean performDexOptSecondary(String packageName, String targetCompilerFilter, boolean force) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(targetCompilerFilter);
                    _data.writeInt(force ? 1 : 0);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void dumpProfiles(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void forceDexOpt(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(120, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean runBackgroundDexoptJob(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void reconcileSecondaryDexFiles(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(122, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized PackageCleanItem nextPackageToClean(PackageCleanItem lastPackage) throws RemoteException {
                PackageCleanItem _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (lastPackage != null) {
                        _data.writeInt(1);
                        lastPackage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PackageCleanItem.CREATOR.createFromParcel(_reply);
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
            public synchronized int getMoveStatus(int moveId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(moveId);
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void registerMoveCallback(IPackageMoveObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void unregisterMoveCallback(IPackageMoveObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized int movePackage(String packageName, String volumeUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(volumeUuid);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized int movePrimaryStorage(String volumeUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean addPermissionAsync(PermissionInfo info) throws RemoteException {
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
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean setInstallLocation(int loc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(loc);
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected int getInstallLocation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized int installExistingPackageAsUser(String packageName, int userId, int installFlags, int installReason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(installFlags);
                    _data.writeInt(installReason);
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void verifyPendingInstall(int id, int verificationCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(verificationCode);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(verificationCodeAtTimeout);
                    _data.writeLong(millisecondsToDelay);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void verifyIntentFilter(int id, int verificationCode, List<String> failedDomains) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(verificationCode);
                    _data.writeStringList(failedDomains);
                    this.mRemote.transact(135, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized int getIntentVerificationStatus(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(136, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean updateIntentVerificationStatus(String packageName, int status, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(status);
                    _data.writeInt(userId);
                    this.mRemote.transact(137, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized ParceledListSlice getIntentFilterVerifications(String packageName) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(138, _data, _reply, 0);
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
            public synchronized ParceledListSlice getAllIntentFilters(String packageName) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(139, _data, _reply, 0);
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
            public synchronized boolean setDefaultBrowserPackageName(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized String getDefaultBrowserPackageName(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(141, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized VerifierDeviceIdentity getVerifierDeviceIdentity() throws RemoteException {
                VerifierDeviceIdentity _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(142, _data, _reply, 0);
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
            public synchronized boolean isFirstBoot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(143, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean isOnlyCoreApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(144, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean isUpgrade() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(145, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void setPermissionEnforced(String permission, boolean enforced) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeInt(enforced ? 1 : 0);
                    this.mRemote.transact(146, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean isPermissionEnforced(String permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    this.mRemote.transact(147, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isStorageLow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(148, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean setApplicationHiddenSettingAsUser(String packageName, boolean hidden, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(hidden ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(149, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean getApplicationHiddenSettingAsUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(150, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
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
                    this.mRemote.transact(151, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(152, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized IPackageInstaller getPackageInstaller() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(153, _data, _reply, 0);
                    _reply.readException();
                    IPackageInstaller _result = IPackageInstaller.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean setBlockUninstallForUser(String packageName, boolean blockUninstall, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(blockUninstall ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(154, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean getBlockUninstallForUser(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(155, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized KeySet getKeySetByAlias(String packageName, String alias) throws RemoteException {
                KeySet _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(alias);
                    this.mRemote.transact(156, _data, _reply, 0);
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
            public synchronized KeySet getSigningKeySet(String packageName) throws RemoteException {
                KeySet _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(157, _data, _reply, 0);
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
            public synchronized boolean isPackageSignedByKeySet(String packageName, KeySet ks) throws RemoteException {
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
                    this.mRemote.transact(158, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean isPackageSignedByKeySetExactly(String packageName, KeySet ks) throws RemoteException {
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
                    this.mRemote.transact(159, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void addOnPermissionsChangeListener(IOnPermissionsChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(160, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void removeOnPermissionsChangeListener(IOnPermissionsChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(161, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void grantDefaultPermissionsToEnabledCarrierApps(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    this.mRemote.transact(162, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void grantDefaultPermissionsToEnabledImsServices(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    this.mRemote.transact(163, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void grantDefaultPermissionsToEnabledTelephonyDataServices(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    this.mRemote.transact(164, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void revokeDefaultPermissionsFromDisabledTelephonyDataServices(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    this.mRemote.transact(165, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void grantDefaultPermissionsToActiveLuiApp(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(166, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void revokeDefaultPermissionsFromLuiApps(String[] packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    this.mRemote.transact(167, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean isPermissionRevokedByPolicy(String permission, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(168, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getPermissionControllerPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(169, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized ParceledListSlice getInstantApps(int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(170, _data, _reply, 0);
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
            public synchronized byte[] getInstantAppCookie(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(171, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean setInstantAppCookie(String packageName, byte[] cookie, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeByteArray(cookie);
                    _data.writeInt(userId);
                    this.mRemote.transact(172, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized Bitmap getInstantAppIcon(String packageName, int userId) throws RemoteException {
                Bitmap _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(173, _data, _reply, 0);
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
            public synchronized boolean isInstantApp(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(174, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean setRequiredForSystemUser(String packageName, boolean systemUserApp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(systemUserApp ? 1 : 0);
                    this.mRemote.transact(175, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void setUpdateAvailable(String packageName, boolean updateAvaialble) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(updateAvaialble ? 1 : 0);
                    this.mRemote.transact(176, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getServicesSystemSharedLibraryPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(177, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getSharedSystemSharedLibraryPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(178, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized ChangedPackages getChangedPackages(int sequenceNumber, int userId) throws RemoteException {
                ChangedPackages _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sequenceNumber);
                    _data.writeInt(userId);
                    this.mRemote.transact(179, _data, _reply, 0);
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
            public synchronized boolean isPackageDeviceAdminOnAnyUser(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(180, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized int getInstallReason(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(181, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized ParceledListSlice getSharedLibraries(String packageName, int flags, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    this.mRemote.transact(182, _data, _reply, 0);
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
            public synchronized boolean canRequestPackageInstalls(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(183, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void deletePreloadsFileCache() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(184, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized ComponentName getInstantAppResolverComponent() throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(185, _data, _reply, 0);
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
            public synchronized ComponentName getInstantAppResolverSettingsComponent() throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(186, _data, _reply, 0);
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
            public synchronized ComponentName getInstantAppInstallerComponent() throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(187, _data, _reply, 0);
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
            public synchronized String getInstantAppAndroidId(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(188, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized IArtManager getArtManager() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(189, _data, _reply, 0);
                    _reply.readException();
                    IArtManager _result = IArtManager.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized void setHarmfulAppWarning(String packageName, CharSequence warning, int userId) throws RemoteException {
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
                    this.mRemote.transact(190, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized CharSequence getHarmfulAppWarning(String packageName, int userId) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(191, _data, _reply, 0);
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
            public synchronized boolean hasSigningCertificate(String packageName, byte[] signingCertificate, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeByteArray(signingCertificate);
                    _data.writeInt(flags);
                    this.mRemote.transact(192, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean hasUidSigningCertificate(int uid, byte[] signingCertificate, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeByteArray(signingCertificate);
                    _data.writeInt(flags);
                    this.mRemote.transact(193, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized String getSystemTextClassifierPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(194, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageManager
            public synchronized boolean isPackageStateProtected(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(195, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
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
                    this.mRemote.transact(196, _data, _reply, 0);
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
                    this.mRemote.transact(197, _data, _reply, 0);
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
                    this.mRemote.transact(198, _data, _reply, 0);
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
                    this.mRemote.transact(199, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(200, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(201, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(202, _data, _reply, 0);
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
                    this.mRemote.transact(203, _data, _reply, 0);
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
                    this.mRemote.transact(204, _data, _reply, 0);
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
    }
}
