package android.app.admin;

import android.app.IApplicationThread;
import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ParceledListSlice;
import android.content.pm.StringParceledListSlice;
import android.graphics.Bitmap;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.UserHandle;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keystore.ParcelableKeyGenParameterSpec;
import android.telephony.data.ApnSetting;
import android.text.TextUtils;
import java.util.List;
/* loaded from: classes.dex */
public interface IDevicePolicyManager extends IInterface {
    synchronized void addCrossProfileIntentFilter(ComponentName componentName, IntentFilter intentFilter, int i) throws RemoteException;

    synchronized boolean addCrossProfileWidgetProvider(ComponentName componentName, String str) throws RemoteException;

    synchronized int addOverrideApn(ComponentName componentName, ApnSetting apnSetting) throws RemoteException;

    synchronized void addPersistentPreferredActivity(ComponentName componentName, IntentFilter intentFilter, ComponentName componentName2) throws RemoteException;

    synchronized boolean approveCaCert(String str, int i, boolean z) throws RemoteException;

    synchronized boolean bindDeviceAdminServiceAsUser(ComponentName componentName, IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, IServiceConnection iServiceConnection, int i, int i2) throws RemoteException;

    synchronized int checkProvisioningPreCondition(String str, String str2) throws RemoteException;

    synchronized void choosePrivateKeyAlias(int i, Uri uri, String str, IBinder iBinder) throws RemoteException;

    synchronized void clearApplicationUserData(ComponentName componentName, String str, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    synchronized void clearCrossProfileIntentFilters(ComponentName componentName) throws RemoteException;

    synchronized void clearDeviceOwner(String str) throws RemoteException;

    synchronized void clearPackagePersistentPreferredActivities(ComponentName componentName, String str) throws RemoteException;

    synchronized void clearProfileOwner(ComponentName componentName) throws RemoteException;

    synchronized boolean clearResetPasswordToken(ComponentName componentName) throws RemoteException;

    synchronized void clearSystemUpdatePolicyFreezePeriodRecord() throws RemoteException;

    synchronized Intent createAdminSupportIntent(String str) throws RemoteException;

    synchronized UserHandle createAndManageUser(ComponentName componentName, String str, ComponentName componentName2, PersistableBundle persistableBundle, int i) throws RemoteException;

    synchronized void enableSystemApp(ComponentName componentName, String str, String str2) throws RemoteException;

    synchronized int enableSystemAppWithIntent(ComponentName componentName, String str, Intent intent) throws RemoteException;

    synchronized void enforceCanManageCaCerts(ComponentName componentName, String str) throws RemoteException;

    synchronized void forceRemoveActiveAdmin(ComponentName componentName, int i) throws RemoteException;

    synchronized long forceSecurityLogs() throws RemoteException;

    synchronized void forceUpdateUserSetupComplete() throws RemoteException;

    synchronized boolean generateKeyPair(ComponentName componentName, String str, String str2, ParcelableKeyGenParameterSpec parcelableKeyGenParameterSpec, int i, KeymasterCertificateChain keymasterCertificateChain) throws RemoteException;

    synchronized String[] getAccountTypesWithManagementDisabled() throws RemoteException;

    synchronized String[] getAccountTypesWithManagementDisabledAsUser(int i) throws RemoteException;

    synchronized List<ComponentName> getActiveAdmins(int i) throws RemoteException;

    synchronized List<String> getAffiliationIds(ComponentName componentName) throws RemoteException;

    synchronized String getAlwaysOnVpnPackage(ComponentName componentName) throws RemoteException;

    synchronized Bundle getApplicationRestrictions(ComponentName componentName, String str, String str2) throws RemoteException;

    synchronized String getApplicationRestrictionsManagingPackage(ComponentName componentName) throws RemoteException;

    synchronized boolean getAutoTimeRequired() throws RemoteException;

    synchronized List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName componentName) throws RemoteException;

    synchronized boolean getBluetoothContactSharingDisabled(ComponentName componentName) throws RemoteException;

    synchronized boolean getBluetoothContactSharingDisabledForUser(int i) throws RemoteException;

    synchronized boolean getCameraDisabled(ComponentName componentName, int i) throws RemoteException;

    synchronized String getCertInstallerPackage(ComponentName componentName) throws RemoteException;

    synchronized boolean getCrossProfileCallerIdDisabled(ComponentName componentName) throws RemoteException;

    synchronized boolean getCrossProfileCallerIdDisabledForUser(int i) throws RemoteException;

    synchronized boolean getCrossProfileContactsSearchDisabled(ComponentName componentName) throws RemoteException;

    synchronized boolean getCrossProfileContactsSearchDisabledForUser(int i) throws RemoteException;

    synchronized List<String> getCrossProfileWidgetProviders(ComponentName componentName) throws RemoteException;

    synchronized int getCurrentFailedPasswordAttempts(int i, boolean z) throws RemoteException;

    synchronized List<String> getDelegatePackages(ComponentName componentName, String str) throws RemoteException;

    synchronized List<String> getDelegatedScopes(ComponentName componentName, String str) throws RemoteException;

    synchronized ComponentName getDeviceOwnerComponent(boolean z) throws RemoteException;

    synchronized CharSequence getDeviceOwnerLockScreenInfo() throws RemoteException;

    synchronized String getDeviceOwnerName() throws RemoteException;

    synchronized CharSequence getDeviceOwnerOrganizationName() throws RemoteException;

    synchronized int getDeviceOwnerUserId() throws RemoteException;

    synchronized List<String> getDisallowedSystemApps(ComponentName componentName, int i, String str) throws RemoteException;

    synchronized boolean getDoNotAskCredentialsOnBoot() throws RemoteException;

    synchronized CharSequence getEndUserSessionMessage(ComponentName componentName) throws RemoteException;

    synchronized boolean getForceEphemeralUsers(ComponentName componentName) throws RemoteException;

    synchronized ComponentName getGlobalProxyAdmin(int i) throws RemoteException;

    synchronized List<String> getKeepUninstalledPackages(ComponentName componentName, String str) throws RemoteException;

    synchronized int getKeyguardDisabledFeatures(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized long getLastBugReportRequestTime() throws RemoteException;

    synchronized long getLastNetworkLogRetrievalTime() throws RemoteException;

    synchronized long getLastSecurityLogRetrievalTime() throws RemoteException;

    synchronized int getLockTaskFeatures(ComponentName componentName) throws RemoteException;

    synchronized String[] getLockTaskPackages(ComponentName componentName) throws RemoteException;

    synchronized CharSequence getLongSupportMessage(ComponentName componentName) throws RemoteException;

    synchronized CharSequence getLongSupportMessageForUser(ComponentName componentName, int i) throws RemoteException;

    synchronized ComponentName getMandatoryBackupTransport() throws RemoteException;

    synchronized int getMaximumFailedPasswordsForWipe(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized long getMaximumTimeToLock(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized List<String> getMeteredDataDisabledPackages(ComponentName componentName) throws RemoteException;

    synchronized int getOrganizationColor(ComponentName componentName) throws RemoteException;

    synchronized int getOrganizationColorForUser(int i) throws RemoteException;

    synchronized CharSequence getOrganizationName(ComponentName componentName) throws RemoteException;

    synchronized CharSequence getOrganizationNameForUser(int i) throws RemoteException;

    synchronized List<ApnSetting> getOverrideApns(ComponentName componentName) throws RemoteException;

    synchronized StringParceledListSlice getOwnerInstalledCaCerts(UserHandle userHandle) throws RemoteException;

    synchronized long getPasswordExpiration(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized long getPasswordExpirationTimeout(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized int getPasswordHistoryLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized int getPasswordMinimumLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized int getPasswordMinimumLetters(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized int getPasswordMinimumLowerCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized int getPasswordMinimumNonLetter(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized int getPasswordMinimumNumeric(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized int getPasswordMinimumSymbols(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized int getPasswordMinimumUpperCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized int getPasswordQuality(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized SystemUpdateInfo getPendingSystemUpdate(ComponentName componentName) throws RemoteException;

    synchronized int getPermissionGrantState(ComponentName componentName, String str, String str2, String str3) throws RemoteException;

    synchronized int getPermissionPolicy(ComponentName componentName) throws RemoteException;

    synchronized List getPermittedAccessibilityServices(ComponentName componentName) throws RemoteException;

    synchronized List getPermittedAccessibilityServicesForUser(int i) throws RemoteException;

    synchronized List<String> getPermittedCrossProfileNotificationListeners(ComponentName componentName) throws RemoteException;

    synchronized List getPermittedInputMethods(ComponentName componentName) throws RemoteException;

    synchronized List getPermittedInputMethodsForCurrentUser() throws RemoteException;

    synchronized ComponentName getProfileOwner(int i) throws RemoteException;

    synchronized String getProfileOwnerName(int i) throws RemoteException;

    synchronized int getProfileWithMinimumFailedPasswordsForWipe(int i, boolean z) throws RemoteException;

    synchronized void getRemoveWarning(ComponentName componentName, RemoteCallback remoteCallback, int i) throws RemoteException;

    synchronized long getRequiredStrongAuthTimeout(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized ComponentName getRestrictionsProvider(int i) throws RemoteException;

    synchronized boolean getScreenCaptureDisabled(ComponentName componentName, int i) throws RemoteException;

    synchronized List<UserHandle> getSecondaryUsers(ComponentName componentName) throws RemoteException;

    synchronized CharSequence getShortSupportMessage(ComponentName componentName) throws RemoteException;

    synchronized CharSequence getShortSupportMessageForUser(ComponentName componentName, int i) throws RemoteException;

    synchronized CharSequence getStartUserSessionMessage(ComponentName componentName) throws RemoteException;

    synchronized boolean getStorageEncryption(ComponentName componentName, int i) throws RemoteException;

    synchronized int getStorageEncryptionStatus(String str, int i) throws RemoteException;

    synchronized SystemUpdatePolicy getSystemUpdatePolicy() throws RemoteException;

    synchronized PersistableBundle getTransferOwnershipBundle() throws RemoteException;

    synchronized List<PersistableBundle> getTrustAgentConfiguration(ComponentName componentName, ComponentName componentName2, int i, boolean z) throws RemoteException;

    synchronized int getUserProvisioningState() throws RemoteException;

    synchronized Bundle getUserRestrictions(ComponentName componentName) throws RemoteException;

    synchronized String getWifiMacAddress(ComponentName componentName) throws RemoteException;

    synchronized boolean hasDeviceOwner() throws RemoteException;

    synchronized boolean hasGrantedPolicy(ComponentName componentName, int i, int i2) throws RemoteException;

    synchronized boolean hasUserSetupCompleted() throws RemoteException;

    synchronized boolean installCaCert(ComponentName componentName, String str, byte[] bArr) throws RemoteException;

    synchronized boolean installExistingPackage(ComponentName componentName, String str, String str2) throws RemoteException;

    synchronized boolean installKeyPair(ComponentName componentName, String str, byte[] bArr, byte[] bArr2, byte[] bArr3, String str2, boolean z, boolean z2) throws RemoteException;

    synchronized boolean isAccessibilityServicePermittedByAdmin(ComponentName componentName, String str, int i) throws RemoteException;

    synchronized boolean isActivePasswordSufficient(int i, boolean z) throws RemoteException;

    synchronized boolean isAdminActive(ComponentName componentName, int i) throws RemoteException;

    synchronized boolean isAffiliatedUser() throws RemoteException;

    synchronized boolean isApplicationHidden(ComponentName componentName, String str, String str2) throws RemoteException;

    synchronized boolean isBackupServiceEnabled(ComponentName componentName) throws RemoteException;

    synchronized boolean isCaCertApproved(String str, int i) throws RemoteException;

    synchronized boolean isCallerApplicationRestrictionsManagingPackage(String str) throws RemoteException;

    synchronized boolean isCurrentInputMethodSetByOwner() throws RemoteException;

    synchronized boolean isDeviceProvisioned() throws RemoteException;

    synchronized boolean isDeviceProvisioningConfigApplied() throws RemoteException;

    synchronized boolean isEphemeralUser(ComponentName componentName) throws RemoteException;

    synchronized boolean isInputMethodPermittedByAdmin(ComponentName componentName, String str, int i) throws RemoteException;

    synchronized boolean isLockTaskPermitted(String str) throws RemoteException;

    synchronized boolean isLogoutEnabled() throws RemoteException;

    synchronized boolean isManagedProfile(ComponentName componentName) throws RemoteException;

    synchronized boolean isMasterVolumeMuted(ComponentName componentName) throws RemoteException;

    synchronized boolean isMeteredDataDisabledPackageForUser(ComponentName componentName, String str, int i) throws RemoteException;

    synchronized boolean isNetworkLoggingEnabled(ComponentName componentName) throws RemoteException;

    synchronized boolean isNotificationListenerServicePermitted(String str, int i) throws RemoteException;

    synchronized boolean isOverrideApnEnabled(ComponentName componentName) throws RemoteException;

    synchronized boolean isPackageSuspended(ComponentName componentName, String str, String str2) throws RemoteException;

    synchronized boolean isProfileActivePasswordSufficientForParent(int i) throws RemoteException;

    synchronized boolean isProvisioningAllowed(String str, String str2) throws RemoteException;

    synchronized boolean isRemovingAdmin(ComponentName componentName, int i) throws RemoteException;

    synchronized boolean isResetPasswordTokenActive(ComponentName componentName) throws RemoteException;

    synchronized boolean isSecurityLoggingEnabled(ComponentName componentName) throws RemoteException;

    synchronized boolean isSeparateProfileChallengeAllowed(int i) throws RemoteException;

    synchronized boolean isSystemOnlyUser(ComponentName componentName) throws RemoteException;

    synchronized boolean isUninstallBlocked(ComponentName componentName, String str) throws RemoteException;

    synchronized boolean isUninstallInQueue(String str) throws RemoteException;

    synchronized boolean isUsingUnifiedPassword(ComponentName componentName) throws RemoteException;

    synchronized void lockNow(int i, boolean z) throws RemoteException;

    synchronized int logoutUser(ComponentName componentName) throws RemoteException;

    synchronized void notifyLockTaskModeChanged(boolean z, String str, int i) throws RemoteException;

    synchronized void notifyPendingSystemUpdate(SystemUpdateInfo systemUpdateInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean packageHasActiveAdmins(String str, int i) throws RemoteException;

    synchronized void reboot(ComponentName componentName) throws RemoteException;

    synchronized void removeActiveAdmin(ComponentName componentName, int i) throws RemoteException;

    synchronized boolean removeCrossProfileWidgetProvider(ComponentName componentName, String str) throws RemoteException;

    synchronized boolean removeKeyPair(ComponentName componentName, String str, String str2) throws RemoteException;

    synchronized boolean removeOverrideApn(ComponentName componentName, int i) throws RemoteException;

    synchronized boolean removeUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    synchronized void reportFailedFingerprintAttempt(int i) throws RemoteException;

    synchronized void reportFailedPasswordAttempt(int i) throws RemoteException;

    synchronized void reportKeyguardDismissed(int i) throws RemoteException;

    synchronized void reportKeyguardSecured(int i) throws RemoteException;

    synchronized void reportPasswordChanged(int i) throws RemoteException;

    synchronized void reportSuccessfulFingerprintAttempt(int i) throws RemoteException;

    synchronized void reportSuccessfulPasswordAttempt(int i) throws RemoteException;

    synchronized boolean requestBugreport(ComponentName componentName) throws RemoteException;

    synchronized boolean resetPassword(String str, int i) throws RemoteException;

    synchronized boolean resetPasswordWithToken(ComponentName componentName, String str, byte[] bArr, int i) throws RemoteException;

    synchronized List<NetworkEvent> retrieveNetworkLogs(ComponentName componentName, long j) throws RemoteException;

    synchronized ParceledListSlice retrievePreRebootSecurityLogs(ComponentName componentName) throws RemoteException;

    synchronized ParceledListSlice retrieveSecurityLogs(ComponentName componentName) throws RemoteException;

    synchronized void setAccountManagementDisabled(ComponentName componentName, String str, boolean z) throws RemoteException;

    synchronized void setActiveAdmin(ComponentName componentName, boolean z, int i) throws RemoteException;

    synchronized void setActivePasswordState(PasswordMetrics passwordMetrics, int i) throws RemoteException;

    synchronized void setAffiliationIds(ComponentName componentName, List<String> list) throws RemoteException;

    synchronized boolean setAlwaysOnVpnPackage(ComponentName componentName, String str, boolean z) throws RemoteException;

    synchronized boolean setApplicationHidden(ComponentName componentName, String str, String str2, boolean z) throws RemoteException;

    synchronized void setApplicationRestrictions(ComponentName componentName, String str, String str2, Bundle bundle) throws RemoteException;

    synchronized boolean setApplicationRestrictionsManagingPackage(ComponentName componentName, String str) throws RemoteException;

    synchronized void setAutoTimeRequired(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setBackupServiceEnabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setBluetoothContactSharingDisabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setCameraDisabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setCertInstallerPackage(ComponentName componentName, String str) throws RemoteException;

    synchronized void setCrossProfileCallerIdDisabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setCrossProfileContactsSearchDisabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setDefaultSmsApplication(ComponentName componentName, String str) throws RemoteException;

    synchronized void setDelegatedScopes(ComponentName componentName, String str, List<String> list) throws RemoteException;

    synchronized boolean setDeviceOwner(ComponentName componentName, String str, int i) throws RemoteException;

    synchronized void setDeviceOwnerLockScreenInfo(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    synchronized void setDeviceProvisioningConfigApplied() throws RemoteException;

    synchronized void setEndUserSessionMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    synchronized void setForceEphemeralUsers(ComponentName componentName, boolean z) throws RemoteException;

    synchronized ComponentName setGlobalProxy(ComponentName componentName, String str, String str2) throws RemoteException;

    synchronized void setGlobalSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    synchronized void setKeepUninstalledPackages(ComponentName componentName, String str, List<String> list) throws RemoteException;

    synchronized boolean setKeyPairCertificate(ComponentName componentName, String str, String str2, byte[] bArr, byte[] bArr2, boolean z) throws RemoteException;

    synchronized boolean setKeyguardDisabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setKeyguardDisabledFeatures(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized void setLockTaskFeatures(ComponentName componentName, int i) throws RemoteException;

    synchronized void setLockTaskPackages(ComponentName componentName, String[] strArr) throws RemoteException;

    synchronized void setLogoutEnabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setLongSupportMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    synchronized boolean setMandatoryBackupTransport(ComponentName componentName, ComponentName componentName2) throws RemoteException;

    synchronized void setMasterVolumeMuted(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setMaximumFailedPasswordsForWipe(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized void setMaximumTimeToLock(ComponentName componentName, long j, boolean z) throws RemoteException;

    synchronized List<String> setMeteredDataDisabledPackages(ComponentName componentName, List<String> list) throws RemoteException;

    synchronized void setNetworkLoggingEnabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setOrganizationColor(ComponentName componentName, int i) throws RemoteException;

    synchronized void setOrganizationColorForUser(int i, int i2) throws RemoteException;

    synchronized void setOrganizationName(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    synchronized void setOverrideApnsEnabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized String[] setPackagesSuspended(ComponentName componentName, String str, String[] strArr, boolean z) throws RemoteException;

    synchronized void setPasswordExpirationTimeout(ComponentName componentName, long j, boolean z) throws RemoteException;

    synchronized void setPasswordHistoryLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized void setPasswordMinimumLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized void setPasswordMinimumLetters(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized void setPasswordMinimumLowerCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized void setPasswordMinimumNonLetter(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized void setPasswordMinimumNumeric(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized void setPasswordMinimumSymbols(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized void setPasswordMinimumUpperCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized void setPasswordQuality(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized boolean setPermissionGrantState(ComponentName componentName, String str, String str2, String str3, int i) throws RemoteException;

    synchronized void setPermissionPolicy(ComponentName componentName, String str, int i) throws RemoteException;

    synchronized boolean setPermittedAccessibilityServices(ComponentName componentName, List list) throws RemoteException;

    synchronized boolean setPermittedCrossProfileNotificationListeners(ComponentName componentName, List<String> list) throws RemoteException;

    synchronized boolean setPermittedInputMethods(ComponentName componentName, List list) throws RemoteException;

    synchronized void setProfileEnabled(ComponentName componentName) throws RemoteException;

    synchronized void setProfileName(ComponentName componentName, String str) throws RemoteException;

    synchronized boolean setProfileOwner(ComponentName componentName, String str, int i) throws RemoteException;

    synchronized void setRecommendedGlobalProxy(ComponentName componentName, ProxyInfo proxyInfo) throws RemoteException;

    synchronized void setRequiredStrongAuthTimeout(ComponentName componentName, long j, boolean z) throws RemoteException;

    synchronized boolean setResetPasswordToken(ComponentName componentName, byte[] bArr) throws RemoteException;

    synchronized void setRestrictionsProvider(ComponentName componentName, ComponentName componentName2) throws RemoteException;

    synchronized void setScreenCaptureDisabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setSecureSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    synchronized void setSecurityLoggingEnabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setShortSupportMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    synchronized void setStartUserSessionMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    synchronized boolean setStatusBarDisabled(ComponentName componentName, boolean z) throws RemoteException;

    synchronized int setStorageEncryption(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setSystemSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    synchronized void setSystemUpdatePolicy(ComponentName componentName, SystemUpdatePolicy systemUpdatePolicy) throws RemoteException;

    synchronized boolean setTime(ComponentName componentName, long j) throws RemoteException;

    synchronized boolean setTimeZone(ComponentName componentName, String str) throws RemoteException;

    synchronized void setTrustAgentConfiguration(ComponentName componentName, ComponentName componentName2, PersistableBundle persistableBundle, boolean z) throws RemoteException;

    synchronized void setUninstallBlocked(ComponentName componentName, String str, String str2, boolean z) throws RemoteException;

    synchronized void setUserIcon(ComponentName componentName, Bitmap bitmap) throws RemoteException;

    synchronized void setUserProvisioningState(int i, int i2) throws RemoteException;

    synchronized void setUserRestriction(ComponentName componentName, String str, boolean z) throws RemoteException;

    synchronized void startManagedQuickContact(String str, long j, boolean z, long j2, Intent intent) throws RemoteException;

    synchronized int startUserInBackground(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    synchronized int stopUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    synchronized boolean switchUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    synchronized void transferOwnership(ComponentName componentName, ComponentName componentName2, PersistableBundle persistableBundle) throws RemoteException;

    synchronized void uninstallCaCerts(ComponentName componentName, String str, String[] strArr) throws RemoteException;

    synchronized void uninstallPackageWithActiveAdmins(String str) throws RemoteException;

    synchronized boolean updateOverrideApn(ComponentName componentName, int i, ApnSetting apnSetting) throws RemoteException;

    synchronized void wipeDataWithReason(int i, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IDevicePolicyManager {
        private static final String DESCRIPTOR = "android.app.admin.IDevicePolicyManager";
        static final int TRANSACTION_addCrossProfileIntentFilter = 111;
        static final int TRANSACTION_addCrossProfileWidgetProvider = 167;
        static final int TRANSACTION_addOverrideApn = 258;
        static final int TRANSACTION_addPersistentPreferredActivity = 99;
        static final int TRANSACTION_approveCaCert = 85;
        static final int TRANSACTION_bindDeviceAdminServiceAsUser = 234;
        static final int TRANSACTION_checkProvisioningPreCondition = 189;
        static final int TRANSACTION_choosePrivateKeyAlias = 91;
        static final int TRANSACTION_clearApplicationUserData = 246;
        static final int TRANSACTION_clearCrossProfileIntentFilters = 112;
        static final int TRANSACTION_clearDeviceOwner = 69;
        static final int TRANSACTION_clearPackagePersistentPreferredActivities = 100;
        static final int TRANSACTION_clearProfileOwner = 76;
        static final int TRANSACTION_clearResetPasswordToken = 241;
        static final int TRANSACTION_clearSystemUpdatePolicyFreezePeriodRecord = 178;
        static final int TRANSACTION_createAdminSupportIntent = 124;
        static final int TRANSACTION_createAndManageUser = 127;
        static final int TRANSACTION_enableSystemApp = 134;
        static final int TRANSACTION_enableSystemAppWithIntent = 135;
        static final int TRANSACTION_enforceCanManageCaCerts = 84;
        static final int TRANSACTION_forceRemoveActiveAdmin = 55;
        static final int TRANSACTION_forceSecurityLogs = 220;
        static final int TRANSACTION_forceUpdateUserSetupComplete = 226;
        static final int TRANSACTION_generateKeyPair = 89;
        static final int TRANSACTION_getAccountTypesWithManagementDisabled = 138;
        static final int TRANSACTION_getAccountTypesWithManagementDisabledAsUser = 139;
        static final int TRANSACTION_getActiveAdmins = 51;
        static final int TRANSACTION_getAffiliationIds = 214;
        static final int TRANSACTION_getAlwaysOnVpnPackage = 98;
        static final int TRANSACTION_getApplicationRestrictions = 103;
        static final int TRANSACTION_getApplicationRestrictionsManagingPackage = 105;
        static final int TRANSACTION_getAutoTimeRequired = 171;
        static final int TRANSACTION_getBindDeviceAdminTargetUsers = 235;
        static final int TRANSACTION_getBluetoothContactSharingDisabled = 163;
        static final int TRANSACTION_getBluetoothContactSharingDisabledForUser = 164;
        static final int TRANSACTION_getCameraDisabled = 44;
        static final int TRANSACTION_getCertInstallerPackage = 96;
        static final int TRANSACTION_getCrossProfileCallerIdDisabled = 156;
        static final int TRANSACTION_getCrossProfileCallerIdDisabledForUser = 157;
        static final int TRANSACTION_getCrossProfileContactsSearchDisabled = 159;
        static final int TRANSACTION_getCrossProfileContactsSearchDisabledForUser = 160;
        static final int TRANSACTION_getCrossProfileWidgetProviders = 169;
        static final int TRANSACTION_getCurrentFailedPasswordAttempts = 25;
        static final int TRANSACTION_getDelegatePackages = 94;
        static final int TRANSACTION_getDelegatedScopes = 93;
        static final int TRANSACTION_getDeviceOwnerComponent = 66;
        static final int TRANSACTION_getDeviceOwnerLockScreenInfo = 79;
        static final int TRANSACTION_getDeviceOwnerName = 68;
        static final int TRANSACTION_getDeviceOwnerOrganizationName = 209;
        static final int TRANSACTION_getDeviceOwnerUserId = 70;
        static final int TRANSACTION_getDisallowedSystemApps = 249;
        static final int TRANSACTION_getDoNotAskCredentialsOnBoot = 181;
        static final int TRANSACTION_getEndUserSessionMessage = 255;
        static final int TRANSACTION_getForceEphemeralUsers = 173;
        static final int TRANSACTION_getGlobalProxyAdmin = 37;
        static final int TRANSACTION_getKeepUninstalledPackages = 191;
        static final int TRANSACTION_getKeyguardDisabledFeatures = 48;
        static final int TRANSACTION_getLastBugReportRequestTime = 238;
        static final int TRANSACTION_getLastNetworkLogRetrievalTime = 239;
        static final int TRANSACTION_getLastSecurityLogRetrievalTime = 237;
        static final int TRANSACTION_getLockTaskFeatures = 144;
        static final int TRANSACTION_getLockTaskPackages = 141;
        static final int TRANSACTION_getLongSupportMessage = 199;
        static final int TRANSACTION_getLongSupportMessageForUser = 201;
        static final int TRANSACTION_getMandatoryBackupTransport = 230;
        static final int TRANSACTION_getMaximumFailedPasswordsForWipe = 28;
        static final int TRANSACTION_getMaximumTimeToLock = 31;
        static final int TRANSACTION_getMeteredDataDisabledPackages = 257;
        static final int TRANSACTION_getOrganizationColor = 205;
        static final int TRANSACTION_getOrganizationColorForUser = 206;
        static final int TRANSACTION_getOrganizationName = 208;
        static final int TRANSACTION_getOrganizationNameForUser = 210;
        static final int TRANSACTION_getOverrideApns = 261;
        static final int TRANSACTION_getOwnerInstalledCaCerts = 245;
        static final int TRANSACTION_getPasswordExpiration = 21;
        static final int TRANSACTION_getPasswordExpirationTimeout = 20;
        static final int TRANSACTION_getPasswordHistoryLength = 18;
        static final int TRANSACTION_getPasswordMinimumLength = 4;
        static final int TRANSACTION_getPasswordMinimumLetters = 10;
        static final int TRANSACTION_getPasswordMinimumLowerCase = 8;
        static final int TRANSACTION_getPasswordMinimumNonLetter = 16;
        static final int TRANSACTION_getPasswordMinimumNumeric = 12;
        static final int TRANSACTION_getPasswordMinimumSymbols = 14;
        static final int TRANSACTION_getPasswordMinimumUpperCase = 6;
        static final int TRANSACTION_getPasswordQuality = 2;
        static final int TRANSACTION_getPendingSystemUpdate = 183;
        static final int TRANSACTION_getPermissionGrantState = 187;
        static final int TRANSACTION_getPermissionPolicy = 185;
        static final int TRANSACTION_getPermittedAccessibilityServices = 114;
        static final int TRANSACTION_getPermittedAccessibilityServicesForUser = 115;
        static final int TRANSACTION_getPermittedCrossProfileNotificationListeners = 122;
        static final int TRANSACTION_getPermittedInputMethods = 118;
        static final int TRANSACTION_getPermittedInputMethodsForCurrentUser = 119;
        static final int TRANSACTION_getProfileOwner = 72;
        static final int TRANSACTION_getProfileOwnerName = 73;
        static final int TRANSACTION_getProfileWithMinimumFailedPasswordsForWipe = 26;
        static final int TRANSACTION_getRemoveWarning = 53;
        static final int TRANSACTION_getRequiredStrongAuthTimeout = 33;
        static final int TRANSACTION_getRestrictionsProvider = 108;
        static final int TRANSACTION_getScreenCaptureDisabled = 46;
        static final int TRANSACTION_getSecondaryUsers = 133;
        static final int TRANSACTION_getShortSupportMessage = 197;
        static final int TRANSACTION_getShortSupportMessageForUser = 200;
        static final int TRANSACTION_getStartUserSessionMessage = 254;
        static final int TRANSACTION_getStorageEncryption = 40;
        static final int TRANSACTION_getStorageEncryptionStatus = 41;
        static final int TRANSACTION_getSystemUpdatePolicy = 177;
        static final int TRANSACTION_getTransferOwnershipBundle = 251;
        static final int TRANSACTION_getTrustAgentConfiguration = 166;
        static final int TRANSACTION_getUserProvisioningState = 211;
        static final int TRANSACTION_getUserRestrictions = 110;
        static final int TRANSACTION_getWifiMacAddress = 194;
        static final int TRANSACTION_hasDeviceOwner = 67;
        static final int TRANSACTION_hasGrantedPolicy = 56;
        static final int TRANSACTION_hasUserSetupCompleted = 77;
        static final int TRANSACTION_installCaCert = 82;
        static final int TRANSACTION_installExistingPackage = 136;
        static final int TRANSACTION_installKeyPair = 87;
        static final int TRANSACTION_isAccessibilityServicePermittedByAdmin = 116;
        static final int TRANSACTION_isActivePasswordSufficient = 22;
        static final int TRANSACTION_isAdminActive = 50;
        static final int TRANSACTION_isAffiliatedUser = 215;
        static final int TRANSACTION_isApplicationHidden = 126;
        static final int TRANSACTION_isBackupServiceEnabled = 228;
        static final int TRANSACTION_isCaCertApproved = 86;
        static final int TRANSACTION_isCallerApplicationRestrictionsManagingPackage = 106;
        static final int TRANSACTION_isCurrentInputMethodSetByOwner = 244;
        static final int TRANSACTION_isDeviceProvisioned = 223;
        static final int TRANSACTION_isDeviceProvisioningConfigApplied = 224;
        static final int TRANSACTION_isEphemeralUser = 236;
        static final int TRANSACTION_isInputMethodPermittedByAdmin = 120;
        static final int TRANSACTION_isLockTaskPermitted = 142;
        static final int TRANSACTION_isLogoutEnabled = 248;
        static final int TRANSACTION_isManagedProfile = 192;
        static final int TRANSACTION_isMasterVolumeMuted = 151;
        static final int TRANSACTION_isMeteredDataDisabledPackageForUser = 264;
        static final int TRANSACTION_isNetworkLoggingEnabled = 232;
        static final int TRANSACTION_isNotificationListenerServicePermitted = 123;
        static final int TRANSACTION_isOverrideApnEnabled = 263;
        static final int TRANSACTION_isPackageSuspended = 81;
        static final int TRANSACTION_isProfileActivePasswordSufficientForParent = 23;
        static final int TRANSACTION_isProvisioningAllowed = 188;
        static final int TRANSACTION_isRemovingAdmin = 174;
        static final int TRANSACTION_isResetPasswordTokenActive = 242;
        static final int TRANSACTION_isSecurityLoggingEnabled = 217;
        static final int TRANSACTION_isSeparateProfileChallengeAllowed = 202;
        static final int TRANSACTION_isSystemOnlyUser = 193;
        static final int TRANSACTION_isUninstallBlocked = 154;
        static final int TRANSACTION_isUninstallInQueue = 221;
        static final int TRANSACTION_isUsingUnifiedPassword = 24;
        static final int TRANSACTION_lockNow = 34;
        static final int TRANSACTION_logoutUser = 132;
        static final int TRANSACTION_notifyLockTaskModeChanged = 152;
        static final int TRANSACTION_notifyPendingSystemUpdate = 182;
        public private protected static final int TRANSACTION_packageHasActiveAdmins = 52;
        static final int TRANSACTION_reboot = 195;
        public private protected static final int TRANSACTION_removeActiveAdmin = 54;
        static final int TRANSACTION_removeCrossProfileWidgetProvider = 168;
        static final int TRANSACTION_removeKeyPair = 88;
        static final int TRANSACTION_removeOverrideApn = 260;
        static final int TRANSACTION_removeUser = 128;
        static final int TRANSACTION_reportFailedFingerprintAttempt = 61;
        static final int TRANSACTION_reportFailedPasswordAttempt = 59;
        static final int TRANSACTION_reportKeyguardDismissed = 63;
        static final int TRANSACTION_reportKeyguardSecured = 64;
        static final int TRANSACTION_reportPasswordChanged = 58;
        static final int TRANSACTION_reportSuccessfulFingerprintAttempt = 62;
        static final int TRANSACTION_reportSuccessfulPasswordAttempt = 60;
        static final int TRANSACTION_requestBugreport = 42;
        static final int TRANSACTION_resetPassword = 29;
        static final int TRANSACTION_resetPasswordWithToken = 243;
        static final int TRANSACTION_retrieveNetworkLogs = 233;
        static final int TRANSACTION_retrievePreRebootSecurityLogs = 219;
        static final int TRANSACTION_retrieveSecurityLogs = 218;
        static final int TRANSACTION_setAccountManagementDisabled = 137;
        static final int TRANSACTION_setActiveAdmin = 49;
        static final int TRANSACTION_setActivePasswordState = 57;
        static final int TRANSACTION_setAffiliationIds = 213;
        static final int TRANSACTION_setAlwaysOnVpnPackage = 97;
        static final int TRANSACTION_setApplicationHidden = 125;
        static final int TRANSACTION_setApplicationRestrictions = 102;
        static final int TRANSACTION_setApplicationRestrictionsManagingPackage = 104;
        static final int TRANSACTION_setAutoTimeRequired = 170;
        static final int TRANSACTION_setBackupServiceEnabled = 227;
        static final int TRANSACTION_setBluetoothContactSharingDisabled = 162;
        static final int TRANSACTION_setCameraDisabled = 43;
        static final int TRANSACTION_setCertInstallerPackage = 95;
        static final int TRANSACTION_setCrossProfileCallerIdDisabled = 155;
        static final int TRANSACTION_setCrossProfileContactsSearchDisabled = 158;
        static final int TRANSACTION_setDefaultSmsApplication = 101;
        static final int TRANSACTION_setDelegatedScopes = 92;
        static final int TRANSACTION_setDeviceOwner = 65;
        static final int TRANSACTION_setDeviceOwnerLockScreenInfo = 78;
        static final int TRANSACTION_setDeviceProvisioningConfigApplied = 225;
        static final int TRANSACTION_setEndUserSessionMessage = 253;
        static final int TRANSACTION_setForceEphemeralUsers = 172;
        static final int TRANSACTION_setGlobalProxy = 36;
        static final int TRANSACTION_setGlobalSetting = 145;
        static final int TRANSACTION_setKeepUninstalledPackages = 190;
        static final int TRANSACTION_setKeyPairCertificate = 90;
        static final int TRANSACTION_setKeyguardDisabled = 179;
        static final int TRANSACTION_setKeyguardDisabledFeatures = 47;
        static final int TRANSACTION_setLockTaskFeatures = 143;
        static final int TRANSACTION_setLockTaskPackages = 140;
        static final int TRANSACTION_setLogoutEnabled = 247;
        static final int TRANSACTION_setLongSupportMessage = 198;
        static final int TRANSACTION_setMandatoryBackupTransport = 229;
        static final int TRANSACTION_setMasterVolumeMuted = 150;
        static final int TRANSACTION_setMaximumFailedPasswordsForWipe = 27;
        static final int TRANSACTION_setMaximumTimeToLock = 30;
        static final int TRANSACTION_setMeteredDataDisabledPackages = 256;
        static final int TRANSACTION_setNetworkLoggingEnabled = 231;
        static final int TRANSACTION_setOrganizationColor = 203;
        static final int TRANSACTION_setOrganizationColorForUser = 204;
        static final int TRANSACTION_setOrganizationName = 207;
        static final int TRANSACTION_setOverrideApnsEnabled = 262;
        static final int TRANSACTION_setPackagesSuspended = 80;
        static final int TRANSACTION_setPasswordExpirationTimeout = 19;
        static final int TRANSACTION_setPasswordHistoryLength = 17;
        static final int TRANSACTION_setPasswordMinimumLength = 3;
        static final int TRANSACTION_setPasswordMinimumLetters = 9;
        static final int TRANSACTION_setPasswordMinimumLowerCase = 7;
        static final int TRANSACTION_setPasswordMinimumNonLetter = 15;
        static final int TRANSACTION_setPasswordMinimumNumeric = 11;
        static final int TRANSACTION_setPasswordMinimumSymbols = 13;
        static final int TRANSACTION_setPasswordMinimumUpperCase = 5;
        static final int TRANSACTION_setPasswordQuality = 1;
        static final int TRANSACTION_setPermissionGrantState = 186;
        static final int TRANSACTION_setPermissionPolicy = 184;
        static final int TRANSACTION_setPermittedAccessibilityServices = 113;
        static final int TRANSACTION_setPermittedCrossProfileNotificationListeners = 121;
        static final int TRANSACTION_setPermittedInputMethods = 117;
        static final int TRANSACTION_setProfileEnabled = 74;
        static final int TRANSACTION_setProfileName = 75;
        static final int TRANSACTION_setProfileOwner = 71;
        static final int TRANSACTION_setRecommendedGlobalProxy = 38;
        static final int TRANSACTION_setRequiredStrongAuthTimeout = 32;
        static final int TRANSACTION_setResetPasswordToken = 240;
        static final int TRANSACTION_setRestrictionsProvider = 107;
        static final int TRANSACTION_setScreenCaptureDisabled = 45;
        static final int TRANSACTION_setSecureSetting = 147;
        static final int TRANSACTION_setSecurityLoggingEnabled = 216;
        static final int TRANSACTION_setShortSupportMessage = 196;
        static final int TRANSACTION_setStartUserSessionMessage = 252;
        static final int TRANSACTION_setStatusBarDisabled = 180;
        static final int TRANSACTION_setStorageEncryption = 39;
        static final int TRANSACTION_setSystemSetting = 146;
        static final int TRANSACTION_setSystemUpdatePolicy = 176;
        static final int TRANSACTION_setTime = 148;
        static final int TRANSACTION_setTimeZone = 149;
        static final int TRANSACTION_setTrustAgentConfiguration = 165;
        static final int TRANSACTION_setUninstallBlocked = 153;
        static final int TRANSACTION_setUserIcon = 175;
        static final int TRANSACTION_setUserProvisioningState = 212;
        static final int TRANSACTION_setUserRestriction = 109;
        static final int TRANSACTION_startManagedQuickContact = 161;
        static final int TRANSACTION_startUserInBackground = 130;
        static final int TRANSACTION_stopUser = 131;
        static final int TRANSACTION_switchUser = 129;
        static final int TRANSACTION_transferOwnership = 250;
        static final int TRANSACTION_uninstallCaCerts = 83;
        static final int TRANSACTION_uninstallPackageWithActiveAdmins = 222;
        static final int TRANSACTION_updateOverrideApn = 259;
        static final int TRANSACTION_wipeDataWithReason = 35;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IDevicePolicyManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IDevicePolicyManager)) {
                return (IDevicePolicyManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg5;
            ComponentName _arg0;
            ComponentName _arg02;
            ComponentName _arg03;
            ComponentName _arg04;
            ComponentName _arg05;
            IntentFilter _arg1;
            ComponentName _arg06;
            ComponentName _arg07;
            ComponentName _arg08;
            ComponentName _arg09;
            ComponentName _arg2;
            ComponentName _arg010;
            ComponentName _arg011;
            ComponentName _arg012;
            ComponentName _arg013;
            ComponentName _arg014;
            ComponentName _arg015;
            ComponentName _arg12;
            ComponentName _arg016;
            ComponentName _arg017;
            ComponentName _arg018;
            ComponentName _arg019;
            ComponentName _arg020;
            ComponentName _arg021;
            ComponentName _arg022;
            ComponentName _arg023;
            ComponentName _arg024;
            ComponentName _arg13;
            ComponentName _arg025;
            ComponentName _arg026;
            ComponentName _arg027;
            ComponentName _arg028;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg029 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg14 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg22 = _arg5;
                    setPasswordQuality(_arg029, _arg14, _arg22);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg030 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg15 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg23 = _arg5;
                    int _result = getPasswordQuality(_arg030, _arg15, _arg23);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg031 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg16 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg24 = _arg5;
                    setPasswordMinimumLength(_arg031, _arg16, _arg24);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg032 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg17 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg25 = _arg5;
                    int _result2 = getPasswordMinimumLength(_arg032, _arg17, _arg25);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg033 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg18 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg26 = _arg5;
                    setPasswordMinimumUpperCase(_arg033, _arg18, _arg26);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg034 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg19 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg27 = _arg5;
                    int _result3 = getPasswordMinimumUpperCase(_arg034, _arg19, _arg27);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg035 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg110 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg28 = _arg5;
                    setPasswordMinimumLowerCase(_arg035, _arg110, _arg28);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg036 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg111 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg29 = _arg5;
                    int _result4 = getPasswordMinimumLowerCase(_arg036, _arg111, _arg29);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg037 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg112 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg210 = _arg5;
                    setPasswordMinimumLetters(_arg037, _arg112, _arg210);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg038 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg113 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg211 = _arg5;
                    int _result5 = getPasswordMinimumLetters(_arg038, _arg113, _arg211);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg039 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg114 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg212 = _arg5;
                    setPasswordMinimumNumeric(_arg039, _arg114, _arg212);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg040 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg115 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg213 = _arg5;
                    int _result6 = getPasswordMinimumNumeric(_arg040, _arg115, _arg213);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg041 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg116 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg214 = _arg5;
                    setPasswordMinimumSymbols(_arg041, _arg116, _arg214);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg042 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg117 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg215 = _arg5;
                    int _result7 = getPasswordMinimumSymbols(_arg042, _arg117, _arg215);
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg043 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg118 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg216 = _arg5;
                    setPasswordMinimumNonLetter(_arg043, _arg118, _arg216);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg044 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg119 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg217 = _arg5;
                    int _result8 = getPasswordMinimumNonLetter(_arg044, _arg119, _arg217);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg045 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg120 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg218 = _arg5;
                    setPasswordHistoryLength(_arg045, _arg120, _arg218);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg046 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg121 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg219 = _arg5;
                    int _result9 = getPasswordHistoryLength(_arg046, _arg121, _arg219);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg047 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    long _arg122 = data.readLong();
                    _arg5 = data.readInt() != 0;
                    boolean _arg220 = _arg5;
                    setPasswordExpirationTimeout(_arg047, _arg122, _arg220);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg048 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg123 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg221 = _arg5;
                    long _result10 = getPasswordExpirationTimeout(_arg048, _arg123, _arg221);
                    reply.writeNoException();
                    reply.writeLong(_result10);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg049 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg124 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg222 = _arg5;
                    long _result11 = getPasswordExpiration(_arg049, _arg124, _arg222);
                    reply.writeNoException();
                    reply.writeLong(_result11);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg050 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg125 = _arg5;
                    boolean isActivePasswordSufficient = isActivePasswordSufficient(_arg050, _arg125);
                    reply.writeNoException();
                    reply.writeInt(isActivePasswordSufficient ? 1 : 0);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg051 = data.readInt();
                    boolean isProfileActivePasswordSufficientForParent = isProfileActivePasswordSufficientForParent(_arg051);
                    reply.writeNoException();
                    reply.writeInt(isProfileActivePasswordSufficientForParent ? 1 : 0);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg052 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isUsingUnifiedPassword = isUsingUnifiedPassword(_arg052);
                    reply.writeNoException();
                    reply.writeInt(isUsingUnifiedPassword ? 1 : 0);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg053 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg126 = _arg5;
                    int _result12 = getCurrentFailedPasswordAttempts(_arg053, _arg126);
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg054 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg127 = _arg5;
                    int _result13 = getProfileWithMinimumFailedPasswordsForWipe(_arg054, _arg127);
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg055 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg128 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg223 = _arg5;
                    setMaximumFailedPasswordsForWipe(_arg055, _arg128, _arg223);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg056 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg129 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg224 = _arg5;
                    int _result14 = getMaximumFailedPasswordsForWipe(_arg056, _arg129, _arg224);
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg057 = data.readString();
                    int _arg130 = data.readInt();
                    boolean resetPassword = resetPassword(_arg057, _arg130);
                    reply.writeNoException();
                    reply.writeInt(resetPassword ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg058 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    long _arg131 = data.readLong();
                    _arg5 = data.readInt() != 0;
                    boolean _arg225 = _arg5;
                    setMaximumTimeToLock(_arg058, _arg131, _arg225);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg059 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg132 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg226 = _arg5;
                    long _result15 = getMaximumTimeToLock(_arg059, _arg132, _arg226);
                    reply.writeNoException();
                    reply.writeLong(_result15);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg060 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    long _arg133 = data.readLong();
                    _arg5 = data.readInt() != 0;
                    boolean _arg227 = _arg5;
                    setRequiredStrongAuthTimeout(_arg060, _arg133, _arg227);
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg061 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg134 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg228 = _arg5;
                    long _result16 = getRequiredStrongAuthTimeout(_arg061, _arg134, _arg228);
                    reply.writeNoException();
                    reply.writeLong(_result16);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg062 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg135 = _arg5;
                    lockNow(_arg062, _arg135);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg063 = data.readInt();
                    String _arg136 = data.readString();
                    wipeDataWithReason(_arg063, _arg136);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg064 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg137 = data.readString();
                    String _arg229 = data.readString();
                    ComponentName _result17 = setGlobalProxy(_arg064, _arg137, _arg229);
                    reply.writeNoException();
                    if (_result17 != null) {
                        reply.writeInt(1);
                        _result17.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg065 = data.readInt();
                    ComponentName _result18 = getGlobalProxyAdmin(_arg065);
                    reply.writeNoException();
                    if (_result18 != null) {
                        reply.writeInt(1);
                        _result18.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    ProxyInfo _arg138 = data.readInt() != 0 ? ProxyInfo.CREATOR.createFromParcel(data) : null;
                    setRecommendedGlobalProxy(_arg0, _arg138);
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg066 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg139 = _arg5;
                    int _result19 = setStorageEncryption(_arg066, _arg139);
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg067 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg140 = data.readInt();
                    boolean storageEncryption = getStorageEncryption(_arg067, _arg140);
                    reply.writeNoException();
                    reply.writeInt(storageEncryption ? 1 : 0);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg068 = data.readString();
                    int _arg141 = data.readInt();
                    int _result20 = getStorageEncryptionStatus(_arg068, _arg141);
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg069 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean requestBugreport = requestBugreport(_arg069);
                    reply.writeNoException();
                    reply.writeInt(requestBugreport ? 1 : 0);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg070 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg142 = _arg5;
                    setCameraDisabled(_arg070, _arg142);
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg071 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg143 = data.readInt();
                    boolean cameraDisabled = getCameraDisabled(_arg071, _arg143);
                    reply.writeNoException();
                    reply.writeInt(cameraDisabled ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg072 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg144 = _arg5;
                    setScreenCaptureDisabled(_arg072, _arg144);
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg073 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg145 = data.readInt();
                    boolean screenCaptureDisabled = getScreenCaptureDisabled(_arg073, _arg145);
                    reply.writeNoException();
                    reply.writeInt(screenCaptureDisabled ? 1 : 0);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg074 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg146 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg230 = _arg5;
                    setKeyguardDisabledFeatures(_arg074, _arg146, _arg230);
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg075 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg147 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg231 = _arg5;
                    int _result21 = getKeyguardDisabledFeatures(_arg075, _arg147, _arg231);
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg076 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg148 = _arg5;
                    int _arg232 = data.readInt();
                    setActiveAdmin(_arg076, _arg148, _arg232);
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg077 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg149 = data.readInt();
                    boolean isAdminActive = isAdminActive(_arg077, _arg149);
                    reply.writeNoException();
                    reply.writeInt(isAdminActive ? 1 : 0);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg078 = data.readInt();
                    List<ComponentName> _result22 = getActiveAdmins(_arg078);
                    reply.writeNoException();
                    reply.writeTypedList(_result22);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg079 = data.readString();
                    int _arg150 = data.readInt();
                    boolean packageHasActiveAdmins = packageHasActiveAdmins(_arg079, _arg150);
                    reply.writeNoException();
                    reply.writeInt(packageHasActiveAdmins ? 1 : 0);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    RemoteCallback _arg151 = data.readInt() != 0 ? RemoteCallback.CREATOR.createFromParcel(data) : null;
                    int _arg233 = data.readInt();
                    getRemoveWarning(_arg02, _arg151, _arg233);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg080 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg152 = data.readInt();
                    removeActiveAdmin(_arg080, _arg152);
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg081 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg153 = data.readInt();
                    forceRemoveActiveAdmin(_arg081, _arg153);
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg082 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg154 = data.readInt();
                    int _arg234 = data.readInt();
                    boolean hasGrantedPolicy = hasGrantedPolicy(_arg082, _arg154, _arg234);
                    reply.writeNoException();
                    reply.writeInt(hasGrantedPolicy ? 1 : 0);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    PasswordMetrics _arg083 = data.readInt() != 0 ? PasswordMetrics.CREATOR.createFromParcel(data) : null;
                    int _arg155 = data.readInt();
                    setActivePasswordState(_arg083, _arg155);
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg084 = data.readInt();
                    reportPasswordChanged(_arg084);
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg085 = data.readInt();
                    reportFailedPasswordAttempt(_arg085);
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg086 = data.readInt();
                    reportSuccessfulPasswordAttempt(_arg086);
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg087 = data.readInt();
                    reportFailedFingerprintAttempt(_arg087);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg088 = data.readInt();
                    reportSuccessfulFingerprintAttempt(_arg088);
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg089 = data.readInt();
                    reportKeyguardDismissed(_arg089);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg090 = data.readInt();
                    reportKeyguardSecured(_arg090);
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg091 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg156 = data.readString();
                    int _arg235 = data.readInt();
                    boolean deviceOwner = setDeviceOwner(_arg091, _arg156, _arg235);
                    reply.writeNoException();
                    reply.writeInt(deviceOwner ? 1 : 0);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg092 = data.readInt() != 0;
                    ComponentName _result23 = getDeviceOwnerComponent(_arg092);
                    reply.writeNoException();
                    if (_result23 != null) {
                        reply.writeInt(1);
                        _result23.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasDeviceOwner = hasDeviceOwner();
                    reply.writeNoException();
                    reply.writeInt(hasDeviceOwner ? 1 : 0);
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    String _result24 = getDeviceOwnerName();
                    reply.writeNoException();
                    reply.writeString(_result24);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg093 = data.readString();
                    clearDeviceOwner(_arg093);
                    reply.writeNoException();
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    int _result25 = getDeviceOwnerUserId();
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg094 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg157 = data.readString();
                    int _arg236 = data.readInt();
                    boolean profileOwner = setProfileOwner(_arg094, _arg157, _arg236);
                    reply.writeNoException();
                    reply.writeInt(profileOwner ? 1 : 0);
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg095 = data.readInt();
                    ComponentName _result26 = getProfileOwner(_arg095);
                    reply.writeNoException();
                    if (_result26 != null) {
                        reply.writeInt(1);
                        _result26.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg096 = data.readInt();
                    String _result27 = getProfileOwnerName(_arg096);
                    reply.writeNoException();
                    reply.writeString(_result27);
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg097 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    setProfileEnabled(_arg097);
                    reply.writeNoException();
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg098 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg158 = data.readString();
                    setProfileName(_arg098, _arg158);
                    reply.writeNoException();
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg099 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    clearProfileOwner(_arg099);
                    reply.writeNoException();
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasUserSetupCompleted = hasUserSetupCompleted();
                    reply.writeNoException();
                    reply.writeInt(hasUserSetupCompleted ? 1 : 0);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    CharSequence _arg159 = data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null;
                    setDeviceOwnerLockScreenInfo(_arg03, _arg159);
                    reply.writeNoException();
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    CharSequence _result28 = getDeviceOwnerLockScreenInfo();
                    reply.writeNoException();
                    if (_result28 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result28, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0100 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg160 = data.readString();
                    String[] _arg237 = data.createStringArray();
                    _arg5 = data.readInt() != 0;
                    boolean _arg3 = _arg5;
                    String[] _result29 = setPackagesSuspended(_arg0100, _arg160, _arg237, _arg3);
                    reply.writeNoException();
                    reply.writeStringArray(_result29);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0101 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg161 = data.readString();
                    String _arg238 = data.readString();
                    boolean isPackageSuspended = isPackageSuspended(_arg0101, _arg161, _arg238);
                    reply.writeNoException();
                    reply.writeInt(isPackageSuspended ? 1 : 0);
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0102 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg162 = data.readString();
                    byte[] _arg239 = data.createByteArray();
                    boolean installCaCert = installCaCert(_arg0102, _arg162, _arg239);
                    reply.writeNoException();
                    reply.writeInt(installCaCert ? 1 : 0);
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0103 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg163 = data.readString();
                    String[] _arg240 = data.createStringArray();
                    uninstallCaCerts(_arg0103, _arg163, _arg240);
                    reply.writeNoException();
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0104 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg164 = data.readString();
                    enforceCanManageCaCerts(_arg0104, _arg164);
                    reply.writeNoException();
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0105 = data.readString();
                    int _arg165 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg241 = _arg5;
                    boolean approveCaCert = approveCaCert(_arg0105, _arg165, _arg241);
                    reply.writeNoException();
                    reply.writeInt(approveCaCert ? 1 : 0);
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0106 = data.readString();
                    int _arg166 = data.readInt();
                    boolean isCaCertApproved = isCaCertApproved(_arg0106, _arg166);
                    reply.writeNoException();
                    reply.writeInt(isCaCertApproved ? 1 : 0);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0107 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg167 = data.readString();
                    byte[] _arg242 = data.createByteArray();
                    byte[] _arg32 = data.createByteArray();
                    byte[] _arg4 = data.createByteArray();
                    String _arg52 = data.readString();
                    boolean _arg6 = data.readInt() != 0;
                    boolean _arg7 = data.readInt() != 0;
                    boolean installKeyPair = installKeyPair(_arg0107, _arg167, _arg242, _arg32, _arg4, _arg52, _arg6, _arg7);
                    reply.writeNoException();
                    reply.writeInt(installKeyPair ? 1 : 0);
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0108 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg168 = data.readString();
                    String _arg243 = data.readString();
                    boolean removeKeyPair = removeKeyPair(_arg0108, _arg168, _arg243);
                    reply.writeNoException();
                    reply.writeInt(removeKeyPair ? 1 : 0);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    String _arg169 = data.readString();
                    String _arg244 = data.readString();
                    ParcelableKeyGenParameterSpec _arg33 = data.readInt() != 0 ? ParcelableKeyGenParameterSpec.CREATOR.createFromParcel(data) : null;
                    int _arg42 = data.readInt();
                    KeymasterCertificateChain _arg53 = new KeymasterCertificateChain();
                    boolean generateKeyPair = generateKeyPair(_arg04, _arg169, _arg244, _arg33, _arg42, _arg53);
                    reply.writeNoException();
                    reply.writeInt(generateKeyPair ? 1 : 0);
                    reply.writeInt(1);
                    _arg53.writeToParcel(reply, 1);
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0109 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg170 = data.readString();
                    String _arg245 = data.readString();
                    byte[] _arg34 = data.createByteArray();
                    byte[] _arg43 = data.createByteArray();
                    _arg5 = data.readInt() != 0;
                    boolean keyPairCertificate = setKeyPairCertificate(_arg0109, _arg170, _arg245, _arg34, _arg43, _arg5);
                    reply.writeNoException();
                    reply.writeInt(keyPairCertificate ? 1 : 0);
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0110 = data.readInt();
                    Uri _arg171 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    String _arg246 = data.readString();
                    IBinder _arg35 = data.readStrongBinder();
                    choosePrivateKeyAlias(_arg0110, _arg171, _arg246, _arg35);
                    reply.writeNoException();
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0111 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg172 = data.readString();
                    List<String> _arg247 = data.createStringArrayList();
                    setDelegatedScopes(_arg0111, _arg172, _arg247);
                    reply.writeNoException();
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0112 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg173 = data.readString();
                    List<String> _result30 = getDelegatedScopes(_arg0112, _arg173);
                    reply.writeNoException();
                    reply.writeStringList(_result30);
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0113 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg174 = data.readString();
                    List<String> _result31 = getDelegatePackages(_arg0113, _arg174);
                    reply.writeNoException();
                    reply.writeStringList(_result31);
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0114 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg175 = data.readString();
                    setCertInstallerPackage(_arg0114, _arg175);
                    reply.writeNoException();
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0115 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _result32 = getCertInstallerPackage(_arg0115);
                    reply.writeNoException();
                    reply.writeString(_result32);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0116 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg176 = data.readString();
                    _arg5 = data.readInt() != 0;
                    boolean _arg248 = _arg5;
                    boolean alwaysOnVpnPackage = setAlwaysOnVpnPackage(_arg0116, _arg176, _arg248);
                    reply.writeNoException();
                    reply.writeInt(alwaysOnVpnPackage ? 1 : 0);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0117 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _result33 = getAlwaysOnVpnPackage(_arg0117);
                    reply.writeNoException();
                    reply.writeString(_result33);
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    ComponentName _arg249 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    addPersistentPreferredActivity(_arg05, _arg1, _arg249);
                    reply.writeNoException();
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0118 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg177 = data.readString();
                    clearPackagePersistentPreferredActivities(_arg0118, _arg177);
                    reply.writeNoException();
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0119 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg178 = data.readString();
                    setDefaultSmsApplication(_arg0119, _arg178);
                    reply.writeNoException();
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    String _arg179 = data.readString();
                    String _arg250 = data.readString();
                    Bundle _arg36 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    setApplicationRestrictions(_arg06, _arg179, _arg250, _arg36);
                    reply.writeNoException();
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0120 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg180 = data.readString();
                    String _arg251 = data.readString();
                    Bundle _result34 = getApplicationRestrictions(_arg0120, _arg180, _arg251);
                    reply.writeNoException();
                    if (_result34 != null) {
                        reply.writeInt(1);
                        _result34.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0121 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg181 = data.readString();
                    boolean applicationRestrictionsManagingPackage = setApplicationRestrictionsManagingPackage(_arg0121, _arg181);
                    reply.writeNoException();
                    reply.writeInt(applicationRestrictionsManagingPackage ? 1 : 0);
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0122 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _result35 = getApplicationRestrictionsManagingPackage(_arg0122);
                    reply.writeNoException();
                    reply.writeString(_result35);
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0123 = data.readString();
                    boolean isCallerApplicationRestrictionsManagingPackage = isCallerApplicationRestrictionsManagingPackage(_arg0123);
                    reply.writeNoException();
                    reply.writeInt(isCallerApplicationRestrictionsManagingPackage ? 1 : 0);
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    ComponentName _arg182 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    setRestrictionsProvider(_arg07, _arg182);
                    reply.writeNoException();
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0124 = data.readInt();
                    ComponentName _result36 = getRestrictionsProvider(_arg0124);
                    reply.writeNoException();
                    if (_result36 != null) {
                        reply.writeInt(1);
                        _result36.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0125 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg183 = data.readString();
                    _arg5 = data.readInt() != 0;
                    boolean _arg252 = _arg5;
                    setUserRestriction(_arg0125, _arg183, _arg252);
                    reply.writeNoException();
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0126 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    Bundle _result37 = getUserRestrictions(_arg0126);
                    reply.writeNoException();
                    if (_result37 != null) {
                        reply.writeInt(1);
                        _result37.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    IntentFilter _arg184 = data.readInt() != 0 ? IntentFilter.CREATOR.createFromParcel(data) : null;
                    int _arg253 = data.readInt();
                    addCrossProfileIntentFilter(_arg08, _arg184, _arg253);
                    reply.writeNoException();
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0127 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    clearCrossProfileIntentFilters(_arg0127);
                    reply.writeNoException();
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0128 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    ClassLoader cl = getClass().getClassLoader();
                    List _arg185 = data.readArrayList(cl);
                    boolean permittedAccessibilityServices = setPermittedAccessibilityServices(_arg0128, _arg185);
                    reply.writeNoException();
                    reply.writeInt(permittedAccessibilityServices ? 1 : 0);
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0129 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List _result38 = getPermittedAccessibilityServices(_arg0129);
                    reply.writeNoException();
                    reply.writeList(_result38);
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0130 = data.readInt();
                    List _result39 = getPermittedAccessibilityServicesForUser(_arg0130);
                    reply.writeNoException();
                    reply.writeList(_result39);
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0131 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg186 = data.readString();
                    int _arg254 = data.readInt();
                    boolean isAccessibilityServicePermittedByAdmin = isAccessibilityServicePermittedByAdmin(_arg0131, _arg186, _arg254);
                    reply.writeNoException();
                    reply.writeInt(isAccessibilityServicePermittedByAdmin ? 1 : 0);
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0132 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    ClassLoader cl2 = getClass().getClassLoader();
                    List _arg187 = data.readArrayList(cl2);
                    boolean permittedInputMethods = setPermittedInputMethods(_arg0132, _arg187);
                    reply.writeNoException();
                    reply.writeInt(permittedInputMethods ? 1 : 0);
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0133 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List _result40 = getPermittedInputMethods(_arg0133);
                    reply.writeNoException();
                    reply.writeList(_result40);
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    List _result41 = getPermittedInputMethodsForCurrentUser();
                    reply.writeNoException();
                    reply.writeList(_result41);
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0134 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg188 = data.readString();
                    int _arg255 = data.readInt();
                    boolean isInputMethodPermittedByAdmin = isInputMethodPermittedByAdmin(_arg0134, _arg188, _arg255);
                    reply.writeNoException();
                    reply.writeInt(isInputMethodPermittedByAdmin ? 1 : 0);
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0135 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List<String> _arg189 = data.createStringArrayList();
                    boolean permittedCrossProfileNotificationListeners = setPermittedCrossProfileNotificationListeners(_arg0135, _arg189);
                    reply.writeNoException();
                    reply.writeInt(permittedCrossProfileNotificationListeners ? 1 : 0);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0136 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List<String> _result42 = getPermittedCrossProfileNotificationListeners(_arg0136);
                    reply.writeNoException();
                    reply.writeStringList(_result42);
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0137 = data.readString();
                    int _arg190 = data.readInt();
                    boolean isNotificationListenerServicePermitted = isNotificationListenerServicePermitted(_arg0137, _arg190);
                    reply.writeNoException();
                    reply.writeInt(isNotificationListenerServicePermitted ? 1 : 0);
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0138 = data.readString();
                    Intent _result43 = createAdminSupportIntent(_arg0138);
                    reply.writeNoException();
                    if (_result43 != null) {
                        reply.writeInt(1);
                        _result43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0139 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg191 = data.readString();
                    String _arg256 = data.readString();
                    _arg5 = data.readInt() != 0;
                    boolean _arg37 = _arg5;
                    boolean applicationHidden = setApplicationHidden(_arg0139, _arg191, _arg256, _arg37);
                    reply.writeNoException();
                    reply.writeInt(applicationHidden ? 1 : 0);
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0140 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg192 = data.readString();
                    String _arg257 = data.readString();
                    boolean isApplicationHidden = isApplicationHidden(_arg0140, _arg192, _arg257);
                    reply.writeNoException();
                    reply.writeInt(isApplicationHidden ? 1 : 0);
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg09 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg09 = null;
                    }
                    String _arg193 = data.readString();
                    if (data.readInt() != 0) {
                        ComponentName _arg258 = ComponentName.CREATOR.createFromParcel(data);
                        _arg2 = _arg258;
                    } else {
                        _arg2 = null;
                    }
                    PersistableBundle _arg38 = data.readInt() != 0 ? PersistableBundle.CREATOR.createFromParcel(data) : null;
                    int _arg44 = data.readInt();
                    UserHandle _result44 = createAndManageUser(_arg09, _arg193, _arg2, _arg38, _arg44);
                    reply.writeNoException();
                    if (_result44 != null) {
                        reply.writeInt(1);
                        _result44.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg010 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg010 = null;
                    }
                    UserHandle _arg194 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    boolean removeUser = removeUser(_arg010, _arg194);
                    reply.writeNoException();
                    reply.writeInt(removeUser ? 1 : 0);
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg011 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg011 = null;
                    }
                    UserHandle _arg195 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    boolean switchUser = switchUser(_arg011, _arg195);
                    reply.writeNoException();
                    reply.writeInt(switchUser ? 1 : 0);
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg012 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg012 = null;
                    }
                    UserHandle _arg196 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    int _result45 = startUserInBackground(_arg012, _arg196);
                    reply.writeNoException();
                    reply.writeInt(_result45);
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg013 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg013 = null;
                    }
                    UserHandle _arg197 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    int _result46 = stopUser(_arg013, _arg197);
                    reply.writeNoException();
                    reply.writeInt(_result46);
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0141 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _result47 = logoutUser(_arg0141);
                    reply.writeNoException();
                    reply.writeInt(_result47);
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0142 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List<UserHandle> _result48 = getSecondaryUsers(_arg0142);
                    reply.writeNoException();
                    reply.writeTypedList(_result48);
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0143 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg198 = data.readString();
                    String _arg259 = data.readString();
                    enableSystemApp(_arg0143, _arg198, _arg259);
                    reply.writeNoException();
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg014 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg014 = null;
                    }
                    String _arg199 = data.readString();
                    Intent _arg260 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    int _result49 = enableSystemAppWithIntent(_arg014, _arg199, _arg260);
                    reply.writeNoException();
                    reply.writeInt(_result49);
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0144 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1100 = data.readString();
                    String _arg261 = data.readString();
                    boolean installExistingPackage = installExistingPackage(_arg0144, _arg1100, _arg261);
                    reply.writeNoException();
                    reply.writeInt(installExistingPackage ? 1 : 0);
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0145 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1101 = data.readString();
                    _arg5 = data.readInt() != 0;
                    boolean _arg262 = _arg5;
                    setAccountManagementDisabled(_arg0145, _arg1101, _arg262);
                    reply.writeNoException();
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result50 = getAccountTypesWithManagementDisabled();
                    reply.writeNoException();
                    reply.writeStringArray(_result50);
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0146 = data.readInt();
                    String[] _result51 = getAccountTypesWithManagementDisabledAsUser(_arg0146);
                    reply.writeNoException();
                    reply.writeStringArray(_result51);
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0147 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String[] _arg1102 = data.createStringArray();
                    setLockTaskPackages(_arg0147, _arg1102);
                    reply.writeNoException();
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0148 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String[] _result52 = getLockTaskPackages(_arg0148);
                    reply.writeNoException();
                    reply.writeStringArray(_result52);
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0149 = data.readString();
                    boolean isLockTaskPermitted = isLockTaskPermitted(_arg0149);
                    reply.writeNoException();
                    reply.writeInt(isLockTaskPermitted ? 1 : 0);
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0150 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1103 = data.readInt();
                    setLockTaskFeatures(_arg0150, _arg1103);
                    reply.writeNoException();
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0151 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _result53 = getLockTaskFeatures(_arg0151);
                    reply.writeNoException();
                    reply.writeInt(_result53);
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0152 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1104 = data.readString();
                    String _arg263 = data.readString();
                    setGlobalSetting(_arg0152, _arg1104, _arg263);
                    reply.writeNoException();
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0153 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1105 = data.readString();
                    String _arg264 = data.readString();
                    setSystemSetting(_arg0153, _arg1105, _arg264);
                    reply.writeNoException();
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0154 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1106 = data.readString();
                    String _arg265 = data.readString();
                    setSecureSetting(_arg0154, _arg1106, _arg265);
                    reply.writeNoException();
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0155 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    long _arg1107 = data.readLong();
                    boolean time = setTime(_arg0155, _arg1107);
                    reply.writeNoException();
                    reply.writeInt(time ? 1 : 0);
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0156 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1108 = data.readString();
                    boolean timeZone = setTimeZone(_arg0156, _arg1108);
                    reply.writeNoException();
                    reply.writeInt(timeZone ? 1 : 0);
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0157 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1109 = _arg5;
                    setMasterVolumeMuted(_arg0157, _arg1109);
                    reply.writeNoException();
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0158 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isMasterVolumeMuted = isMasterVolumeMuted(_arg0158);
                    reply.writeNoException();
                    reply.writeInt(isMasterVolumeMuted ? 1 : 0);
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    _arg5 = data.readInt() != 0;
                    boolean _arg0159 = _arg5;
                    String _arg1110 = data.readString();
                    int _arg266 = data.readInt();
                    notifyLockTaskModeChanged(_arg0159, _arg1110, _arg266);
                    reply.writeNoException();
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0160 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1111 = data.readString();
                    String _arg267 = data.readString();
                    _arg5 = data.readInt() != 0;
                    boolean _arg39 = _arg5;
                    setUninstallBlocked(_arg0160, _arg1111, _arg267, _arg39);
                    reply.writeNoException();
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0161 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1112 = data.readString();
                    boolean isUninstallBlocked = isUninstallBlocked(_arg0161, _arg1112);
                    reply.writeNoException();
                    reply.writeInt(isUninstallBlocked ? 1 : 0);
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0162 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1113 = _arg5;
                    setCrossProfileCallerIdDisabled(_arg0162, _arg1113);
                    reply.writeNoException();
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0163 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean crossProfileCallerIdDisabled = getCrossProfileCallerIdDisabled(_arg0163);
                    reply.writeNoException();
                    reply.writeInt(crossProfileCallerIdDisabled ? 1 : 0);
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0164 = data.readInt();
                    boolean crossProfileCallerIdDisabledForUser = getCrossProfileCallerIdDisabledForUser(_arg0164);
                    reply.writeNoException();
                    reply.writeInt(crossProfileCallerIdDisabledForUser ? 1 : 0);
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0165 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1114 = _arg5;
                    setCrossProfileContactsSearchDisabled(_arg0165, _arg1114);
                    reply.writeNoException();
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0166 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean crossProfileContactsSearchDisabled = getCrossProfileContactsSearchDisabled(_arg0166);
                    reply.writeNoException();
                    reply.writeInt(crossProfileContactsSearchDisabled ? 1 : 0);
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0167 = data.readInt();
                    boolean crossProfileContactsSearchDisabledForUser = getCrossProfileContactsSearchDisabledForUser(_arg0167);
                    reply.writeNoException();
                    reply.writeInt(crossProfileContactsSearchDisabledForUser ? 1 : 0);
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0168 = data.readString();
                    long _arg1115 = data.readLong();
                    boolean _arg268 = data.readInt() != 0;
                    long _arg310 = data.readLong();
                    Intent _arg45 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    startManagedQuickContact(_arg0168, _arg1115, _arg268, _arg310, _arg45);
                    reply.writeNoException();
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0169 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1116 = _arg5;
                    setBluetoothContactSharingDisabled(_arg0169, _arg1116);
                    reply.writeNoException();
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0170 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean bluetoothContactSharingDisabled = getBluetoothContactSharingDisabled(_arg0170);
                    reply.writeNoException();
                    reply.writeInt(bluetoothContactSharingDisabled ? 1 : 0);
                    return true;
                case 164:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0171 = data.readInt();
                    boolean bluetoothContactSharingDisabledForUser = getBluetoothContactSharingDisabledForUser(_arg0171);
                    reply.writeNoException();
                    reply.writeInt(bluetoothContactSharingDisabledForUser ? 1 : 0);
                    return true;
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg015 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg015 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg12 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    PersistableBundle _arg269 = data.readInt() != 0 ? PersistableBundle.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg311 = _arg5;
                    setTrustAgentConfiguration(_arg015, _arg12, _arg269, _arg311);
                    reply.writeNoException();
                    return true;
                case 166:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg016 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg016 = null;
                    }
                    ComponentName _arg1117 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg270 = data.readInt();
                    _arg5 = data.readInt() != 0;
                    boolean _arg312 = _arg5;
                    List<PersistableBundle> _result54 = getTrustAgentConfiguration(_arg016, _arg1117, _arg270, _arg312);
                    reply.writeNoException();
                    reply.writeTypedList(_result54);
                    return true;
                case 167:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0172 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1118 = data.readString();
                    boolean addCrossProfileWidgetProvider = addCrossProfileWidgetProvider(_arg0172, _arg1118);
                    reply.writeNoException();
                    reply.writeInt(addCrossProfileWidgetProvider ? 1 : 0);
                    return true;
                case 168:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0173 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1119 = data.readString();
                    boolean removeCrossProfileWidgetProvider = removeCrossProfileWidgetProvider(_arg0173, _arg1119);
                    reply.writeNoException();
                    reply.writeInt(removeCrossProfileWidgetProvider ? 1 : 0);
                    return true;
                case 169:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0174 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List<String> _result55 = getCrossProfileWidgetProviders(_arg0174);
                    reply.writeNoException();
                    reply.writeStringList(_result55);
                    return true;
                case 170:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0175 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1120 = _arg5;
                    setAutoTimeRequired(_arg0175, _arg1120);
                    reply.writeNoException();
                    return true;
                case 171:
                    data.enforceInterface(DESCRIPTOR);
                    boolean autoTimeRequired = getAutoTimeRequired();
                    reply.writeNoException();
                    reply.writeInt(autoTimeRequired ? 1 : 0);
                    return true;
                case 172:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0176 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1121 = _arg5;
                    setForceEphemeralUsers(_arg0176, _arg1121);
                    reply.writeNoException();
                    return true;
                case 173:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0177 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean forceEphemeralUsers = getForceEphemeralUsers(_arg0177);
                    reply.writeNoException();
                    reply.writeInt(forceEphemeralUsers ? 1 : 0);
                    return true;
                case 174:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0178 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1122 = data.readInt();
                    boolean isRemovingAdmin = isRemovingAdmin(_arg0178, _arg1122);
                    reply.writeNoException();
                    reply.writeInt(isRemovingAdmin ? 1 : 0);
                    return true;
                case 175:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg017 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg017 = null;
                    }
                    Bitmap _arg1123 = data.readInt() != 0 ? Bitmap.CREATOR.createFromParcel(data) : null;
                    setUserIcon(_arg017, _arg1123);
                    reply.writeNoException();
                    return true;
                case 176:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg018 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg018 = null;
                    }
                    SystemUpdatePolicy _arg1124 = data.readInt() != 0 ? SystemUpdatePolicy.CREATOR.createFromParcel(data) : null;
                    setSystemUpdatePolicy(_arg018, _arg1124);
                    reply.writeNoException();
                    return true;
                case 177:
                    data.enforceInterface(DESCRIPTOR);
                    SystemUpdatePolicy _result56 = getSystemUpdatePolicy();
                    reply.writeNoException();
                    if (_result56 != null) {
                        reply.writeInt(1);
                        _result56.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 178:
                    data.enforceInterface(DESCRIPTOR);
                    clearSystemUpdatePolicyFreezePeriodRecord();
                    reply.writeNoException();
                    return true;
                case 179:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0179 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1125 = _arg5;
                    boolean keyguardDisabled = setKeyguardDisabled(_arg0179, _arg1125);
                    reply.writeNoException();
                    reply.writeInt(keyguardDisabled ? 1 : 0);
                    return true;
                case 180:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0180 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1126 = _arg5;
                    boolean statusBarDisabled = setStatusBarDisabled(_arg0180, _arg1126);
                    reply.writeNoException();
                    reply.writeInt(statusBarDisabled ? 1 : 0);
                    return true;
                case 181:
                    data.enforceInterface(DESCRIPTOR);
                    boolean doNotAskCredentialsOnBoot = getDoNotAskCredentialsOnBoot();
                    reply.writeNoException();
                    reply.writeInt(doNotAskCredentialsOnBoot ? 1 : 0);
                    return true;
                case 182:
                    data.enforceInterface(DESCRIPTOR);
                    SystemUpdateInfo _arg0181 = data.readInt() != 0 ? SystemUpdateInfo.CREATOR.createFromParcel(data) : null;
                    notifyPendingSystemUpdate(_arg0181);
                    reply.writeNoException();
                    return true;
                case 183:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0182 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    SystemUpdateInfo _result57 = getPendingSystemUpdate(_arg0182);
                    reply.writeNoException();
                    if (_result57 != null) {
                        reply.writeInt(1);
                        _result57.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 184:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0183 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1127 = data.readString();
                    int _arg271 = data.readInt();
                    setPermissionPolicy(_arg0183, _arg1127, _arg271);
                    reply.writeNoException();
                    return true;
                case 185:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0184 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _result58 = getPermissionPolicy(_arg0184);
                    reply.writeNoException();
                    reply.writeInt(_result58);
                    return true;
                case 186:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0185 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1128 = data.readString();
                    String _arg272 = data.readString();
                    String _arg313 = data.readString();
                    int _arg46 = data.readInt();
                    boolean permissionGrantState = setPermissionGrantState(_arg0185, _arg1128, _arg272, _arg313, _arg46);
                    reply.writeNoException();
                    reply.writeInt(permissionGrantState ? 1 : 0);
                    return true;
                case 187:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0186 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1129 = data.readString();
                    String _arg273 = data.readString();
                    String _arg314 = data.readString();
                    int _result59 = getPermissionGrantState(_arg0186, _arg1129, _arg273, _arg314);
                    reply.writeNoException();
                    reply.writeInt(_result59);
                    return true;
                case 188:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0187 = data.readString();
                    String _arg1130 = data.readString();
                    boolean isProvisioningAllowed = isProvisioningAllowed(_arg0187, _arg1130);
                    reply.writeNoException();
                    reply.writeInt(isProvisioningAllowed ? 1 : 0);
                    return true;
                case 189:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0188 = data.readString();
                    String _arg1131 = data.readString();
                    int _result60 = checkProvisioningPreCondition(_arg0188, _arg1131);
                    reply.writeNoException();
                    reply.writeInt(_result60);
                    return true;
                case 190:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0189 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1132 = data.readString();
                    List<String> _arg274 = data.createStringArrayList();
                    setKeepUninstalledPackages(_arg0189, _arg1132, _arg274);
                    reply.writeNoException();
                    return true;
                case 191:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0190 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1133 = data.readString();
                    List<String> _result61 = getKeepUninstalledPackages(_arg0190, _arg1133);
                    reply.writeNoException();
                    reply.writeStringList(_result61);
                    return true;
                case 192:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0191 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isManagedProfile = isManagedProfile(_arg0191);
                    reply.writeNoException();
                    reply.writeInt(isManagedProfile ? 1 : 0);
                    return true;
                case 193:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0192 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isSystemOnlyUser = isSystemOnlyUser(_arg0192);
                    reply.writeNoException();
                    reply.writeInt(isSystemOnlyUser ? 1 : 0);
                    return true;
                case 194:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0193 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _result62 = getWifiMacAddress(_arg0193);
                    reply.writeNoException();
                    reply.writeString(_result62);
                    return true;
                case 195:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0194 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    reboot(_arg0194);
                    reply.writeNoException();
                    return true;
                case 196:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg019 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg019 = null;
                    }
                    CharSequence _arg1134 = data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null;
                    setShortSupportMessage(_arg019, _arg1134);
                    reply.writeNoException();
                    return true;
                case 197:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0195 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    CharSequence _result63 = getShortSupportMessage(_arg0195);
                    reply.writeNoException();
                    if (_result63 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result63, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 198:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg020 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg020 = null;
                    }
                    CharSequence _arg1135 = data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null;
                    setLongSupportMessage(_arg020, _arg1135);
                    reply.writeNoException();
                    return true;
                case 199:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0196 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    CharSequence _result64 = getLongSupportMessage(_arg0196);
                    reply.writeNoException();
                    if (_result64 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result64, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 200:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0197 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1136 = data.readInt();
                    CharSequence _result65 = getShortSupportMessageForUser(_arg0197, _arg1136);
                    reply.writeNoException();
                    if (_result65 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result65, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 201:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0198 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1137 = data.readInt();
                    CharSequence _result66 = getLongSupportMessageForUser(_arg0198, _arg1137);
                    reply.writeNoException();
                    if (_result66 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result66, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 202:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0199 = data.readInt();
                    boolean isSeparateProfileChallengeAllowed = isSeparateProfileChallengeAllowed(_arg0199);
                    reply.writeNoException();
                    reply.writeInt(isSeparateProfileChallengeAllowed ? 1 : 0);
                    return true;
                case 203:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0200 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1138 = data.readInt();
                    setOrganizationColor(_arg0200, _arg1138);
                    reply.writeNoException();
                    return true;
                case 204:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0201 = data.readInt();
                    int _arg1139 = data.readInt();
                    setOrganizationColorForUser(_arg0201, _arg1139);
                    reply.writeNoException();
                    return true;
                case 205:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0202 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _result67 = getOrganizationColor(_arg0202);
                    reply.writeNoException();
                    reply.writeInt(_result67);
                    return true;
                case 206:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0203 = data.readInt();
                    int _result68 = getOrganizationColorForUser(_arg0203);
                    reply.writeNoException();
                    reply.writeInt(_result68);
                    return true;
                case 207:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg021 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg021 = null;
                    }
                    CharSequence _arg1140 = data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null;
                    setOrganizationName(_arg021, _arg1140);
                    reply.writeNoException();
                    return true;
                case 208:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0204 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    CharSequence _result69 = getOrganizationName(_arg0204);
                    reply.writeNoException();
                    if (_result69 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result69, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 209:
                    data.enforceInterface(DESCRIPTOR);
                    CharSequence _result70 = getDeviceOwnerOrganizationName();
                    reply.writeNoException();
                    if (_result70 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result70, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 210:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0205 = data.readInt();
                    CharSequence _result71 = getOrganizationNameForUser(_arg0205);
                    reply.writeNoException();
                    if (_result71 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result71, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 211:
                    data.enforceInterface(DESCRIPTOR);
                    int _result72 = getUserProvisioningState();
                    reply.writeNoException();
                    reply.writeInt(_result72);
                    return true;
                case 212:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0206 = data.readInt();
                    int _arg1141 = data.readInt();
                    setUserProvisioningState(_arg0206, _arg1141);
                    reply.writeNoException();
                    return true;
                case 213:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0207 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List<String> _arg1142 = data.createStringArrayList();
                    setAffiliationIds(_arg0207, _arg1142);
                    reply.writeNoException();
                    return true;
                case 214:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0208 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List<String> _result73 = getAffiliationIds(_arg0208);
                    reply.writeNoException();
                    reply.writeStringList(_result73);
                    return true;
                case 215:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAffiliatedUser = isAffiliatedUser();
                    reply.writeNoException();
                    reply.writeInt(isAffiliatedUser ? 1 : 0);
                    return true;
                case 216:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0209 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1143 = _arg5;
                    setSecurityLoggingEnabled(_arg0209, _arg1143);
                    reply.writeNoException();
                    return true;
                case 217:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0210 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isSecurityLoggingEnabled = isSecurityLoggingEnabled(_arg0210);
                    reply.writeNoException();
                    reply.writeInt(isSecurityLoggingEnabled ? 1 : 0);
                    return true;
                case 218:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0211 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    ParceledListSlice _result74 = retrieveSecurityLogs(_arg0211);
                    reply.writeNoException();
                    if (_result74 != null) {
                        reply.writeInt(1);
                        _result74.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 219:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0212 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    ParceledListSlice _result75 = retrievePreRebootSecurityLogs(_arg0212);
                    reply.writeNoException();
                    if (_result75 != null) {
                        reply.writeInt(1);
                        _result75.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 220:
                    data.enforceInterface(DESCRIPTOR);
                    long _result76 = forceSecurityLogs();
                    reply.writeNoException();
                    reply.writeLong(_result76);
                    return true;
                case 221:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0213 = data.readString();
                    boolean isUninstallInQueue = isUninstallInQueue(_arg0213);
                    reply.writeNoException();
                    reply.writeInt(isUninstallInQueue ? 1 : 0);
                    return true;
                case 222:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0214 = data.readString();
                    uninstallPackageWithActiveAdmins(_arg0214);
                    reply.writeNoException();
                    return true;
                case 223:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDeviceProvisioned = isDeviceProvisioned();
                    reply.writeNoException();
                    reply.writeInt(isDeviceProvisioned ? 1 : 0);
                    return true;
                case 224:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDeviceProvisioningConfigApplied = isDeviceProvisioningConfigApplied();
                    reply.writeNoException();
                    reply.writeInt(isDeviceProvisioningConfigApplied ? 1 : 0);
                    return true;
                case 225:
                    data.enforceInterface(DESCRIPTOR);
                    setDeviceProvisioningConfigApplied();
                    reply.writeNoException();
                    return true;
                case 226:
                    data.enforceInterface(DESCRIPTOR);
                    forceUpdateUserSetupComplete();
                    reply.writeNoException();
                    return true;
                case 227:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0215 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1144 = _arg5;
                    setBackupServiceEnabled(_arg0215, _arg1144);
                    reply.writeNoException();
                    return true;
                case 228:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0216 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isBackupServiceEnabled = isBackupServiceEnabled(_arg0216);
                    reply.writeNoException();
                    reply.writeInt(isBackupServiceEnabled ? 1 : 0);
                    return true;
                case 229:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg022 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg022 = null;
                    }
                    ComponentName _arg1145 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean mandatoryBackupTransport = setMandatoryBackupTransport(_arg022, _arg1145);
                    reply.writeNoException();
                    reply.writeInt(mandatoryBackupTransport ? 1 : 0);
                    return true;
                case 230:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _result77 = getMandatoryBackupTransport();
                    reply.writeNoException();
                    if (_result77 != null) {
                        reply.writeInt(1);
                        _result77.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 231:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0217 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1146 = _arg5;
                    setNetworkLoggingEnabled(_arg0217, _arg1146);
                    reply.writeNoException();
                    return true;
                case 232:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0218 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isNetworkLoggingEnabled = isNetworkLoggingEnabled(_arg0218);
                    reply.writeNoException();
                    reply.writeInt(isNetworkLoggingEnabled ? 1 : 0);
                    return true;
                case 233:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0219 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    long _arg1147 = data.readLong();
                    List<NetworkEvent> _result78 = retrieveNetworkLogs(_arg0219, _arg1147);
                    reply.writeNoException();
                    reply.writeTypedList(_result78);
                    return true;
                case 234:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg023 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg023 = null;
                    }
                    IApplicationThread _arg1148 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    IBinder _arg275 = data.readStrongBinder();
                    Intent _arg315 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    IServiceConnection _arg47 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
                    int _arg54 = data.readInt();
                    int _arg62 = data.readInt();
                    boolean bindDeviceAdminServiceAsUser = bindDeviceAdminServiceAsUser(_arg023, _arg1148, _arg275, _arg315, _arg47, _arg54, _arg62);
                    reply.writeNoException();
                    reply.writeInt(bindDeviceAdminServiceAsUser ? 1 : 0);
                    return true;
                case 235:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0220 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List<UserHandle> _result79 = getBindDeviceAdminTargetUsers(_arg0220);
                    reply.writeNoException();
                    reply.writeTypedList(_result79);
                    return true;
                case 236:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0221 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isEphemeralUser = isEphemeralUser(_arg0221);
                    reply.writeNoException();
                    reply.writeInt(isEphemeralUser ? 1 : 0);
                    return true;
                case 237:
                    data.enforceInterface(DESCRIPTOR);
                    long _result80 = getLastSecurityLogRetrievalTime();
                    reply.writeNoException();
                    reply.writeLong(_result80);
                    return true;
                case 238:
                    data.enforceInterface(DESCRIPTOR);
                    long _result81 = getLastBugReportRequestTime();
                    reply.writeNoException();
                    reply.writeLong(_result81);
                    return true;
                case 239:
                    data.enforceInterface(DESCRIPTOR);
                    long _result82 = getLastNetworkLogRetrievalTime();
                    reply.writeNoException();
                    reply.writeLong(_result82);
                    return true;
                case 240:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0222 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    byte[] _arg1149 = data.createByteArray();
                    boolean resetPasswordToken = setResetPasswordToken(_arg0222, _arg1149);
                    reply.writeNoException();
                    reply.writeInt(resetPasswordToken ? 1 : 0);
                    return true;
                case 241:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0223 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean clearResetPasswordToken = clearResetPasswordToken(_arg0223);
                    reply.writeNoException();
                    reply.writeInt(clearResetPasswordToken ? 1 : 0);
                    return true;
                case 242:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0224 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isResetPasswordTokenActive = isResetPasswordTokenActive(_arg0224);
                    reply.writeNoException();
                    reply.writeInt(isResetPasswordTokenActive ? 1 : 0);
                    return true;
                case 243:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0225 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1150 = data.readString();
                    byte[] _arg276 = data.createByteArray();
                    int _arg316 = data.readInt();
                    boolean resetPasswordWithToken = resetPasswordWithToken(_arg0225, _arg1150, _arg276, _arg316);
                    reply.writeNoException();
                    reply.writeInt(resetPasswordWithToken ? 1 : 0);
                    return true;
                case 244:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isCurrentInputMethodSetByOwner = isCurrentInputMethodSetByOwner();
                    reply.writeNoException();
                    reply.writeInt(isCurrentInputMethodSetByOwner ? 1 : 0);
                    return true;
                case 245:
                    data.enforceInterface(DESCRIPTOR);
                    UserHandle _arg0226 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    StringParceledListSlice _result83 = getOwnerInstalledCaCerts(_arg0226);
                    reply.writeNoException();
                    if (_result83 != null) {
                        reply.writeInt(1);
                        _result83.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 246:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0227 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1151 = data.readString();
                    IPackageDataObserver _arg277 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    clearApplicationUserData(_arg0227, _arg1151, _arg277);
                    reply.writeNoException();
                    return true;
                case 247:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0228 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1152 = _arg5;
                    setLogoutEnabled(_arg0228, _arg1152);
                    reply.writeNoException();
                    return true;
                case 248:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isLogoutEnabled = isLogoutEnabled();
                    reply.writeNoException();
                    reply.writeInt(isLogoutEnabled ? 1 : 0);
                    return true;
                case 249:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0229 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1153 = data.readInt();
                    String _arg278 = data.readString();
                    List<String> _result84 = getDisallowedSystemApps(_arg0229, _arg1153, _arg278);
                    reply.writeNoException();
                    reply.writeStringList(_result84);
                    return true;
                case 250:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg024 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg024 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    PersistableBundle _arg279 = data.readInt() != 0 ? PersistableBundle.CREATOR.createFromParcel(data) : null;
                    transferOwnership(_arg024, _arg13, _arg279);
                    reply.writeNoException();
                    return true;
                case 251:
                    data.enforceInterface(DESCRIPTOR);
                    PersistableBundle _result85 = getTransferOwnershipBundle();
                    reply.writeNoException();
                    if (_result85 != null) {
                        reply.writeInt(1);
                        _result85.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 252:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg025 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg025 = null;
                    }
                    CharSequence _arg1154 = data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null;
                    setStartUserSessionMessage(_arg025, _arg1154);
                    reply.writeNoException();
                    return true;
                case 253:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg026 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg026 = null;
                    }
                    CharSequence _arg1155 = data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null;
                    setEndUserSessionMessage(_arg026, _arg1155);
                    reply.writeNoException();
                    return true;
                case 254:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0230 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    CharSequence _result86 = getStartUserSessionMessage(_arg0230);
                    reply.writeNoException();
                    if (_result86 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result86, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 255:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0231 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    CharSequence _result87 = getEndUserSessionMessage(_arg0231);
                    reply.writeNoException();
                    if (_result87 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result87, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 256:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0232 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List<String> _arg1156 = data.createStringArrayList();
                    List<String> _result88 = setMeteredDataDisabledPackages(_arg0232, _arg1156);
                    reply.writeNoException();
                    reply.writeStringList(_result88);
                    return true;
                case 257:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0233 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List<String> _result89 = getMeteredDataDisabledPackages(_arg0233);
                    reply.writeNoException();
                    reply.writeStringList(_result89);
                    return true;
                case 258:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg027 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg027 = null;
                    }
                    ApnSetting _arg1157 = data.readInt() != 0 ? ApnSetting.CREATOR.createFromParcel(data) : null;
                    int _result90 = addOverrideApn(_arg027, _arg1157);
                    reply.writeNoException();
                    reply.writeInt(_result90);
                    return true;
                case 259:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg028 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg028 = null;
                    }
                    int _arg1158 = data.readInt();
                    ApnSetting _arg280 = data.readInt() != 0 ? ApnSetting.CREATOR.createFromParcel(data) : null;
                    boolean updateOverrideApn = updateOverrideApn(_arg028, _arg1158, _arg280);
                    reply.writeNoException();
                    reply.writeInt(updateOverrideApn ? 1 : 0);
                    return true;
                case 260:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0234 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg1159 = data.readInt();
                    boolean removeOverrideApn = removeOverrideApn(_arg0234, _arg1159);
                    reply.writeNoException();
                    reply.writeInt(removeOverrideApn ? 1 : 0);
                    return true;
                case 261:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0235 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    List<ApnSetting> _result91 = getOverrideApns(_arg0235);
                    reply.writeNoException();
                    reply.writeTypedList(_result91);
                    return true;
                case 262:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0236 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg5 = data.readInt() != 0;
                    boolean _arg1160 = _arg5;
                    setOverrideApnsEnabled(_arg0236, _arg1160);
                    reply.writeNoException();
                    return true;
                case 263:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0237 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isOverrideApnEnabled = isOverrideApnEnabled(_arg0237);
                    reply.writeNoException();
                    reply.writeInt(isOverrideApnEnabled ? 1 : 0);
                    return true;
                case 264:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg0238 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg1161 = data.readString();
                    int _arg281 = data.readInt();
                    boolean isMeteredDataDisabledPackageForUser = isMeteredDataDisabledPackageForUser(_arg0238, _arg1161, _arg281);
                    reply.writeNoException();
                    reply.writeInt(isMeteredDataDisabledPackageForUser ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IDevicePolicyManager {
            private IBinder mRemote;

            synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setPasswordQuality(ComponentName who, int quality, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(quality);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getPasswordQuality(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setPasswordMinimumLength(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getPasswordMinimumLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setPasswordMinimumUpperCase(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getPasswordMinimumUpperCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setPasswordMinimumLowerCase(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getPasswordMinimumLowerCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setPasswordMinimumLetters(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getPasswordMinimumLetters(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setPasswordMinimumNumeric(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getPasswordMinimumNumeric(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setPasswordMinimumSymbols(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getPasswordMinimumSymbols(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setPasswordMinimumNonLetter(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getPasswordMinimumNonLetter(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setPasswordHistoryLength(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getPasswordHistoryLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setPasswordExpirationTimeout(ComponentName who, long expiration, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(expiration);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized long getPasswordExpirationTimeout(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized long getPasswordExpiration(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isActivePasswordSufficient(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isProfileActivePasswordSufficientForParent(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isUsingUnifiedPassword(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getCurrentFailedPasswordAttempts(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getProfileWithMinimumFailedPasswordsForWipe(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setMaximumFailedPasswordsForWipe(ComponentName admin, int num, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(num);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getMaximumFailedPasswordsForWipe(ComponentName admin, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean resetPassword(String password, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    _data.writeInt(flags);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setMaximumTimeToLock(ComponentName who, long timeMs, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timeMs);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized long getMaximumTimeToLock(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setRequiredStrongAuthTimeout(ComponentName who, long timeMs, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timeMs);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized long getRequiredStrongAuthTimeout(ComponentName who, int userId, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void lockNow(int flags, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void wipeDataWithReason(int flags, String wipeReasonForUser) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeString(wipeReasonForUser);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized ComponentName setGlobalProxy(ComponentName admin, String proxySpec, String exclusionList) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(proxySpec);
                    _data.writeString(exclusionList);
                    this.mRemote.transact(36, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized ComponentName getGlobalProxyAdmin(int userHandle) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(37, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setRecommendedGlobalProxy(ComponentName admin, ProxyInfo proxyInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (proxyInfo != null) {
                        _data.writeInt(1);
                        proxyInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int setStorageEncryption(ComponentName who, boolean encrypt) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(encrypt ? 1 : 0);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getStorageEncryption(ComponentName who, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getStorageEncryptionStatus(String callerPackage, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean requestBugreport(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setCameraDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getCameraDisabled(ComponentName who, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setScreenCaptureDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getScreenCaptureDisabled(ComponentName who, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setKeyguardDisabledFeatures(ComponentName who, int which, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(which);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getKeyguardDisabledFeatures(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setActiveAdmin(ComponentName policyReceiver, boolean refreshing, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(refreshing ? 1 : 0);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isAdminActive(ComponentName policyReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<ComponentName> getActiveAdmins(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    List<ComponentName> _result = _reply.createTypedArrayList(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean packageHasActiveAdmins(String packageName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void getRemoveWarning(ComponentName policyReceiver, RemoteCallback result, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void removeActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void forceRemoveActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean hasGrantedPolicy(ComponentName policyReceiver, int usesPolicy, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(usesPolicy);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setActivePasswordState(PasswordMetrics metrics, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (metrics != null) {
                        _data.writeInt(1);
                        metrics.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void reportPasswordChanged(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void reportFailedPasswordAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void reportSuccessfulPasswordAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void reportFailedFingerprintAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void reportSuccessfulFingerprintAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void reportKeyguardDismissed(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void reportKeyguardSecured(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setDeviceOwner(ComponentName who, String ownerName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(ownerName);
                    _data.writeInt(userId);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized ComponentName getDeviceOwnerComponent(boolean callingUserOnly) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callingUserOnly ? 1 : 0);
                    this.mRemote.transact(66, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean hasDeviceOwner() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized String getDeviceOwnerName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void clearDeviceOwner(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getDeviceOwnerUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setProfileOwner(ComponentName who, String ownerName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(ownerName);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized ComponentName getProfileOwner(int userHandle) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(72, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized String getProfileOwnerName(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setProfileEnabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setProfileName(ComponentName who, String profileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(profileName);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void clearProfileOwner(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean hasUserSetupCompleted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setDeviceOwnerLockScreenInfo(ComponentName who, CharSequence deviceOwnerInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deviceOwnerInfo != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(deviceOwnerInfo, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized CharSequence getDeviceOwnerLockScreenInfo() throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(79, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized String[] setPackagesSuspended(ComponentName admin, String callerPackage, String[] packageNames, boolean suspended) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(suspended ? 1 : 0);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isPackageSuspended(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean installCaCert(ComponentName admin, String callerPackage, byte[] certBuffer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeByteArray(certBuffer);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void uninstallCaCerts(ComponentName admin, String callerPackage, String[] aliases) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeStringArray(aliases);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void enforceCanManageCaCerts(ComponentName admin, String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean approveCaCert(String alias, int userHandle, boolean approval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(userHandle);
                    _data.writeInt(approval ? 1 : 0);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isCaCertApproved(String alias, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean installKeyPair(ComponentName who, String callerPackage, byte[] privKeyBuffer, byte[] certBuffer, byte[] certChainBuffer, String alias, boolean requestAccess, boolean isUserSelectable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeByteArray(privKeyBuffer);
                    _data.writeByteArray(certBuffer);
                    _data.writeByteArray(certChainBuffer);
                    _data.writeString(alias);
                    _data.writeInt(requestAccess ? 1 : 0);
                    _data.writeInt(isUserSelectable ? 1 : 0);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean removeKeyPair(ComponentName who, String callerPackage, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(alias);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean generateKeyPair(ComponentName who, String callerPackage, String algorithm, ParcelableKeyGenParameterSpec keySpec, int idAttestationFlags, KeymasterCertificateChain attestationChain) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(algorithm);
                    if (keySpec != null) {
                        _data.writeInt(1);
                        keySpec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(idAttestationFlags);
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    if (_reply.readInt() != 0) {
                        attestationChain.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setKeyPairCertificate(ComponentName who, String callerPackage, String alias, byte[] certBuffer, byte[] certChainBuffer, boolean isUserSelectable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(alias);
                    _data.writeByteArray(certBuffer);
                    _data.writeByteArray(certChainBuffer);
                    _data.writeInt(isUserSelectable ? 1 : 0);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void choosePrivateKeyAlias(int uid, Uri uri, String alias, IBinder aliasCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(alias);
                    _data.writeStrongBinder(aliasCallback);
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setDelegatedScopes(ComponentName who, String delegatePackage, List<String> scopes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(delegatePackage);
                    _data.writeStringList(scopes);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<String> getDelegatedScopes(ComponentName who, String delegatePackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(delegatePackage);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<String> getDelegatePackages(ComponentName who, String scope) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(scope);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setCertInstallerPackage(ComponentName who, String installerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(installerPackage);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized String getCertInstallerPackage(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setAlwaysOnVpnPackage(ComponentName who, String vpnPackage, boolean lockdown) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(vpnPackage);
                    _data.writeInt(lockdown ? 1 : 0);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized String getAlwaysOnVpnPackage(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void addPersistentPreferredActivity(ComponentName admin, IntentFilter filter, ComponentName activity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
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
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void clearPackagePersistentPreferredActivities(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setDefaultSmsApplication(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setApplicationRestrictions(ComponentName who, String callerPackage, String packageName, Bundle settings) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    if (settings != null) {
                        _data.writeInt(1);
                        settings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized Bundle getApplicationRestrictions(ComponentName who, String callerPackage, String packageName) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setApplicationRestrictionsManagingPackage(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized String getApplicationRestrictionsManagingPackage(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isCallerApplicationRestrictionsManagingPackage(String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setRestrictionsProvider(ComponentName who, ComponentName provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized ComponentName getRestrictionsProvider(int userHandle) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(108, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setUserRestriction(ComponentName who, String key, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(key);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized Bundle getUserRestrictions(ComponentName who) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void addCrossProfileIntentFilter(ComponentName admin, IntentFilter filter, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    this.mRemote.transact(111, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void clearCrossProfileIntentFilters(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(112, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setPermittedAccessibilityServices(ComponentName admin, List packageList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeList(packageList);
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List getPermittedAccessibilityServices(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(114, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List getPermittedAccessibilityServicesForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isAccessibilityServicePermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setPermittedInputMethods(ComponentName admin, List packageList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeList(packageList);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List getPermittedInputMethods(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List getPermittedInputMethodsForCurrentUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isInputMethodPermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(120, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setPermittedCrossProfileNotificationListeners(ComponentName admin, List<String> packageList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(packageList);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<String> getPermittedCrossProfileNotificationListeners(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(122, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isNotificationListenerServicePermitted(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized Intent createAdminSupportIntent(String restriction) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(restriction);
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setApplicationHidden(ComponentName admin, String callerPackage, String packageName, boolean hidden) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    _data.writeInt(hidden ? 1 : 0);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isApplicationHidden(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized UserHandle createAndManageUser(ComponentName who, String name, ComponentName profileOwner, PersistableBundle adminExtras, int flags) throws RemoteException {
                UserHandle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(name);
                    if (profileOwner != null) {
                        _data.writeInt(1);
                        profileOwner.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (adminExtras != null) {
                        _data.writeInt(1);
                        adminExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = UserHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean removeUser(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean switchUser(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int startUserInBackground(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int stopUser(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int logoutUser(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<UserHandle> getSecondaryUsers(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                    List<UserHandle> _result = _reply.createTypedArrayList(UserHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void enableSystemApp(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int enableSystemAppWithIntent(ComponentName admin, String callerPackage, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(135, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean installExistingPackage(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    this.mRemote.transact(136, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setAccountManagementDisabled(ComponentName who, String accountType, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(accountType);
                    _data.writeInt(disabled ? 1 : 0);
                    this.mRemote.transact(137, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized String[] getAccountTypesWithManagementDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(138, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized String[] getAccountTypesWithManagementDisabledAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(139, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setLockTaskPackages(ComponentName who, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(packages);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized String[] getLockTaskPackages(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(141, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isLockTaskPermitted(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(142, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setLockTaskFeatures(ComponentName who, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    this.mRemote.transact(143, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getLockTaskFeatures(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(144, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setGlobalSetting(ComponentName who, String setting, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(setting);
                    _data.writeString(value);
                    this.mRemote.transact(145, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setSystemSetting(ComponentName who, String setting, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(setting);
                    _data.writeString(value);
                    this.mRemote.transact(146, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setSecureSetting(ComponentName who, String setting, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(setting);
                    _data.writeString(value);
                    this.mRemote.transact(147, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setTime(ComponentName who, long millis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(millis);
                    this.mRemote.transact(148, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setTimeZone(ComponentName who, String timeZone) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(timeZone);
                    this.mRemote.transact(149, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setMasterVolumeMuted(ComponentName admin, boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(on ? 1 : 0);
                    this.mRemote.transact(150, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isMasterVolumeMuted(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(151, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void notifyLockTaskModeChanged(boolean isEnabled, String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isEnabled ? 1 : 0);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    this.mRemote.transact(152, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setUninstallBlocked(ComponentName admin, String callerPackage, String packageName, boolean uninstallBlocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    _data.writeInt(uninstallBlocked ? 1 : 0);
                    this.mRemote.transact(153, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isUninstallBlocked(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(154, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setCrossProfileCallerIdDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    this.mRemote.transact(155, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getCrossProfileCallerIdDisabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(156, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getCrossProfileCallerIdDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(157, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setCrossProfileContactsSearchDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    this.mRemote.transact(158, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getCrossProfileContactsSearchDisabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getCrossProfileContactsSearchDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(160, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void startManagedQuickContact(String lookupKey, long contactId, boolean isContactIdIgnored, long directoryId, Intent originalIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(lookupKey);
                    _data.writeLong(contactId);
                    _data.writeInt(isContactIdIgnored ? 1 : 0);
                    _data.writeLong(directoryId);
                    if (originalIntent != null) {
                        _data.writeInt(1);
                        originalIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(161, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setBluetoothContactSharingDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    this.mRemote.transact(162, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getBluetoothContactSharingDisabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(163, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getBluetoothContactSharingDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(164, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setTrustAgentConfiguration(ComponentName admin, ComponentName agent, PersistableBundle args, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (agent != null) {
                        _data.writeInt(1);
                        agent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(165, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<PersistableBundle> getTrustAgentConfiguration(ComponentName admin, ComponentName agent, int userId, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (agent != null) {
                        _data.writeInt(1);
                        agent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeInt(parent ? 1 : 0);
                    this.mRemote.transact(166, _data, _reply, 0);
                    _reply.readException();
                    List<PersistableBundle> _result = _reply.createTypedArrayList(PersistableBundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean addCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(167, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean removeCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(168, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<String> getCrossProfileWidgetProviders(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(169, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setAutoTimeRequired(ComponentName who, boolean required) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(required ? 1 : 0);
                    this.mRemote.transact(170, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getAutoTimeRequired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(171, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setForceEphemeralUsers(ComponentName who, boolean forceEpehemeralUsers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(forceEpehemeralUsers ? 1 : 0);
                    this.mRemote.transact(172, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getForceEphemeralUsers(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(173, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isRemovingAdmin(ComponentName adminReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (adminReceiver != null) {
                        _data.writeInt(1);
                        adminReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    this.mRemote.transact(174, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setUserIcon(ComponentName admin, Bitmap icon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (icon != null) {
                        _data.writeInt(1);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(175, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setSystemUpdatePolicy(ComponentName who, SystemUpdatePolicy policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (policy != null) {
                        _data.writeInt(1);
                        policy.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(176, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized SystemUpdatePolicy getSystemUpdatePolicy() throws RemoteException {
                SystemUpdatePolicy _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(177, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SystemUpdatePolicy.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void clearSystemUpdatePolicyFreezePeriodRecord() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(178, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setKeyguardDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    this.mRemote.transact(179, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setStatusBarDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    this.mRemote.transact(180, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean getDoNotAskCredentialsOnBoot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(181, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void notifyPendingSystemUpdate(SystemUpdateInfo info) throws RemoteException {
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
                    this.mRemote.transact(182, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized SystemUpdateInfo getPendingSystemUpdate(ComponentName admin) throws RemoteException {
                SystemUpdateInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(183, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SystemUpdateInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setPermissionPolicy(ComponentName admin, String callerPackage, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeInt(policy);
                    this.mRemote.transact(184, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getPermissionPolicy(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(185, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission, int grantState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    _data.writeInt(grantState);
                    this.mRemote.transact(186, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    this.mRemote.transact(187, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isProvisioningAllowed(String action, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(packageName);
                    this.mRemote.transact(188, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int checkProvisioningPreCondition(String action, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(packageName);
                    this.mRemote.transact(189, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setKeepUninstalledPackages(ComponentName admin, String callerPackage, List<String> packageList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeStringList(packageList);
                    this.mRemote.transact(190, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<String> getKeepUninstalledPackages(ComponentName admin, String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    this.mRemote.transact(191, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isManagedProfile(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(192, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isSystemOnlyUser(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(193, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized String getWifiMacAddress(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(194, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void reboot(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(195, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setShortSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(196, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized CharSequence getShortSupportMessage(ComponentName admin) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(197, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setLongSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(198, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized CharSequence getLongSupportMessage(ComponentName admin) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(199, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized CharSequence getShortSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    this.mRemote.transact(200, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized CharSequence getLongSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    this.mRemote.transact(201, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isSeparateProfileChallengeAllowed(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(202, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setOrganizationColor(ComponentName admin, int color) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(color);
                    this.mRemote.transact(203, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setOrganizationColorForUser(int color, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(color);
                    _data.writeInt(userId);
                    this.mRemote.transact(204, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getOrganizationColor(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(205, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getOrganizationColorForUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(206, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setOrganizationName(ComponentName admin, CharSequence title) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (title != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(title, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(207, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized CharSequence getOrganizationName(ComponentName admin) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(208, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized CharSequence getDeviceOwnerOrganizationName() throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(209, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized CharSequence getOrganizationNameForUser(int userHandle) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(210, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int getUserProvisioningState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(211, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setUserProvisioningState(int state, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(212, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setAffiliationIds(ComponentName admin, List<String> ids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(ids);
                    this.mRemote.transact(213, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<String> getAffiliationIds(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(214, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isAffiliatedUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(215, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setSecurityLoggingEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(216, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isSecurityLoggingEnabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(217, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized ParceledListSlice retrieveSecurityLogs(ComponentName admin) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(218, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized ParceledListSlice retrievePreRebootSecurityLogs(ComponentName admin) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(219, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized long forceSecurityLogs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(220, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isUninstallInQueue(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(221, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void uninstallPackageWithActiveAdmins(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(222, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isDeviceProvisioned() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(223, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isDeviceProvisioningConfigApplied() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(224, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setDeviceProvisioningConfigApplied() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(225, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void forceUpdateUserSetupComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(226, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setBackupServiceEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(227, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isBackupServiceEnabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(228, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setMandatoryBackupTransport(ComponentName admin, ComponentName backupTransportComponent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (backupTransportComponent != null) {
                        _data.writeInt(1);
                        backupTransportComponent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(229, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized ComponentName getMandatoryBackupTransport() throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(230, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setNetworkLoggingEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(231, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isNetworkLoggingEnabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(232, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<NetworkEvent> retrieveNetworkLogs(ComponentName admin, long batchToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(batchToken);
                    this.mRemote.transact(233, _data, _reply, 0);
                    _reply.readException();
                    List<NetworkEvent> _result = _reply.createTypedArrayList(NetworkEvent.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean bindDeviceAdminServiceAsUser(ComponentName admin, IApplicationThread caller, IBinder token, Intent service, IServiceConnection connection, int flags, int targetUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeStrongBinder(token);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeInt(targetUserId);
                    this.mRemote.transact(234, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(235, _data, _reply, 0);
                    _reply.readException();
                    List<UserHandle> _result = _reply.createTypedArrayList(UserHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isEphemeralUser(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(236, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized long getLastSecurityLogRetrievalTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(237, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized long getLastBugReportRequestTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(238, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized long getLastNetworkLogRetrievalTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(239, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean setResetPasswordToken(ComponentName admin, byte[] token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(token);
                    this.mRemote.transact(240, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean clearResetPasswordToken(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(241, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isResetPasswordTokenActive(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(242, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean resetPasswordWithToken(ComponentName admin, String password, byte[] token, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(password);
                    _data.writeByteArray(token);
                    _data.writeInt(flags);
                    this.mRemote.transact(243, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isCurrentInputMethodSetByOwner() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(244, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized StringParceledListSlice getOwnerInstalledCaCerts(UserHandle user) throws RemoteException {
                StringParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(245, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StringParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void clearApplicationUserData(ComponentName admin, String packageName, IPackageDataObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(246, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setLogoutEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(247, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isLogoutEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(248, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<String> getDisallowedSystemApps(ComponentName admin, int userId, String provisioningAction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeString(provisioningAction);
                    this.mRemote.transact(249, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void transferOwnership(ComponentName admin, ComponentName target, PersistableBundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (target != null) {
                        _data.writeInt(1);
                        target.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(250, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized PersistableBundle getTransferOwnershipBundle() throws RemoteException {
                PersistableBundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(251, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setStartUserSessionMessage(ComponentName admin, CharSequence startUserSessionMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (startUserSessionMessage != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(startUserSessionMessage, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(252, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setEndUserSessionMessage(ComponentName admin, CharSequence endUserSessionMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (endUserSessionMessage != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(endUserSessionMessage, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(253, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized CharSequence getStartUserSessionMessage(ComponentName admin) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(254, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized CharSequence getEndUserSessionMessage(ComponentName admin) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(255, _data, _reply, 0);
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

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<String> setMeteredDataDisabledPackages(ComponentName admin, List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(packageNames);
                    this.mRemote.transact(256, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<String> getMeteredDataDisabledPackages(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(257, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized int addOverrideApn(ComponentName admin, ApnSetting apnSetting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (apnSetting != null) {
                        _data.writeInt(1);
                        apnSetting.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(258, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean updateOverrideApn(ComponentName admin, int apnId, ApnSetting apnSetting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(apnId);
                    if (apnSetting != null) {
                        _data.writeInt(1);
                        apnSetting.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(259, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean removeOverrideApn(ComponentName admin, int apnId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(apnId);
                    this.mRemote.transact(260, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized List<ApnSetting> getOverrideApns(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(261, _data, _reply, 0);
                    _reply.readException();
                    List<ApnSetting> _result = _reply.createTypedArrayList(ApnSetting.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized void setOverrideApnsEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(262, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isOverrideApnEnabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(263, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public synchronized boolean isMeteredDataDisabledPackageForUser(ComponentName admin, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(264, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
