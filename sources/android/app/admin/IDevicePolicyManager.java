package android.app.admin;

import android.annotation.UnsupportedAppUsage;
import android.app.IApplicationThread;
import android.app.IServiceConnection;
import android.app.admin.StartInstallingUpdateCallback;
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
import android.os.ParcelFileDescriptor;
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
    void addCrossProfileIntentFilter(ComponentName componentName, IntentFilter intentFilter, int i) throws RemoteException;

    boolean addCrossProfileWidgetProvider(ComponentName componentName, String str) throws RemoteException;

    int addOverrideApn(ComponentName componentName, ApnSetting apnSetting) throws RemoteException;

    void addPersistentPreferredActivity(ComponentName componentName, IntentFilter intentFilter, ComponentName componentName2) throws RemoteException;

    boolean approveCaCert(String str, int i, boolean z) throws RemoteException;

    boolean bindDeviceAdminServiceAsUser(ComponentName componentName, IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, IServiceConnection iServiceConnection, int i, int i2) throws RemoteException;

    boolean checkDeviceIdentifierAccess(String str, int i, int i2) throws RemoteException;

    int checkProvisioningPreCondition(String str, String str2) throws RemoteException;

    void choosePrivateKeyAlias(int i, Uri uri, String str, IBinder iBinder) throws RemoteException;

    void clearApplicationUserData(ComponentName componentName, String str, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    void clearCrossProfileIntentFilters(ComponentName componentName) throws RemoteException;

    void clearDeviceOwner(String str) throws RemoteException;

    void clearPackagePersistentPreferredActivities(ComponentName componentName, String str) throws RemoteException;

    void clearProfileOwner(ComponentName componentName) throws RemoteException;

    boolean clearResetPasswordToken(ComponentName componentName) throws RemoteException;

    void clearSystemUpdatePolicyFreezePeriodRecord() throws RemoteException;

    Intent createAdminSupportIntent(String str) throws RemoteException;

    UserHandle createAndManageUser(ComponentName componentName, String str, ComponentName componentName2, PersistableBundle persistableBundle, int i) throws RemoteException;

    void enableSystemApp(ComponentName componentName, String str, String str2) throws RemoteException;

    int enableSystemAppWithIntent(ComponentName componentName, String str, Intent intent) throws RemoteException;

    void enforceCanManageCaCerts(ComponentName componentName, String str) throws RemoteException;

    long forceNetworkLogs() throws RemoteException;

    void forceRemoveActiveAdmin(ComponentName componentName, int i) throws RemoteException;

    long forceSecurityLogs() throws RemoteException;

    void forceUpdateUserSetupComplete() throws RemoteException;

    boolean generateKeyPair(ComponentName componentName, String str, String str2, ParcelableKeyGenParameterSpec parcelableKeyGenParameterSpec, int i, KeymasterCertificateChain keymasterCertificateChain) throws RemoteException;

    String[] getAccountTypesWithManagementDisabled() throws RemoteException;

    String[] getAccountTypesWithManagementDisabledAsUser(int i) throws RemoteException;

    List<ComponentName> getActiveAdmins(int i) throws RemoteException;

    List<String> getAffiliationIds(ComponentName componentName) throws RemoteException;

    List<String> getAlwaysOnVpnLockdownWhitelist(ComponentName componentName) throws RemoteException;

    String getAlwaysOnVpnPackage(ComponentName componentName) throws RemoteException;

    Bundle getApplicationRestrictions(ComponentName componentName, String str, String str2) throws RemoteException;

    String getApplicationRestrictionsManagingPackage(ComponentName componentName) throws RemoteException;

    boolean getAutoTimeRequired() throws RemoteException;

    List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName componentName) throws RemoteException;

    boolean getBluetoothContactSharingDisabled(ComponentName componentName) throws RemoteException;

    boolean getBluetoothContactSharingDisabledForUser(int i) throws RemoteException;

    boolean getCameraDisabled(ComponentName componentName, int i) throws RemoteException;

    String getCertInstallerPackage(ComponentName componentName) throws RemoteException;

    List<String> getCrossProfileCalendarPackages(ComponentName componentName) throws RemoteException;

    List<String> getCrossProfileCalendarPackagesForUser(int i) throws RemoteException;

    boolean getCrossProfileCallerIdDisabled(ComponentName componentName) throws RemoteException;

    boolean getCrossProfileCallerIdDisabledForUser(int i) throws RemoteException;

    boolean getCrossProfileContactsSearchDisabled(ComponentName componentName) throws RemoteException;

    boolean getCrossProfileContactsSearchDisabledForUser(int i) throws RemoteException;

    List<String> getCrossProfileWidgetProviders(ComponentName componentName) throws RemoteException;

    int getCurrentFailedPasswordAttempts(int i, boolean z) throws RemoteException;

    List<String> getDelegatePackages(ComponentName componentName, String str) throws RemoteException;

    List<String> getDelegatedScopes(ComponentName componentName, String str) throws RemoteException;

    ComponentName getDeviceOwnerComponent(boolean z) throws RemoteException;

    CharSequence getDeviceOwnerLockScreenInfo() throws RemoteException;

    String getDeviceOwnerName() throws RemoteException;

    CharSequence getDeviceOwnerOrganizationName() throws RemoteException;

    int getDeviceOwnerUserId() throws RemoteException;

    List<String> getDisallowedSystemApps(ComponentName componentName, int i, String str) throws RemoteException;

    boolean getDoNotAskCredentialsOnBoot() throws RemoteException;

    CharSequence getEndUserSessionMessage(ComponentName componentName) throws RemoteException;

    boolean getForceEphemeralUsers(ComponentName componentName) throws RemoteException;

    String getGlobalPrivateDnsHost(ComponentName componentName) throws RemoteException;

    int getGlobalPrivateDnsMode(ComponentName componentName) throws RemoteException;

    ComponentName getGlobalProxyAdmin(int i) throws RemoteException;

    List<String> getKeepUninstalledPackages(ComponentName componentName, String str) throws RemoteException;

    int getKeyguardDisabledFeatures(ComponentName componentName, int i, boolean z) throws RemoteException;

    long getLastBugReportRequestTime() throws RemoteException;

    long getLastNetworkLogRetrievalTime() throws RemoteException;

    long getLastSecurityLogRetrievalTime() throws RemoteException;

    int getLockTaskFeatures(ComponentName componentName) throws RemoteException;

    String[] getLockTaskPackages(ComponentName componentName) throws RemoteException;

    CharSequence getLongSupportMessage(ComponentName componentName) throws RemoteException;

    CharSequence getLongSupportMessageForUser(ComponentName componentName, int i) throws RemoteException;

    int getMaximumFailedPasswordsForWipe(ComponentName componentName, int i, boolean z) throws RemoteException;

    long getMaximumTimeToLock(ComponentName componentName, int i, boolean z) throws RemoteException;

    List<String> getMeteredDataDisabledPackages(ComponentName componentName) throws RemoteException;

    int getOrganizationColor(ComponentName componentName) throws RemoteException;

    int getOrganizationColorForUser(int i) throws RemoteException;

    CharSequence getOrganizationName(ComponentName componentName) throws RemoteException;

    CharSequence getOrganizationNameForUser(int i) throws RemoteException;

    List<ApnSetting> getOverrideApns(ComponentName componentName) throws RemoteException;

    StringParceledListSlice getOwnerInstalledCaCerts(UserHandle userHandle) throws RemoteException;

    int getPasswordComplexity() throws RemoteException;

    long getPasswordExpiration(ComponentName componentName, int i, boolean z) throws RemoteException;

    long getPasswordExpirationTimeout(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordHistoryLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumLetters(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumLowerCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumNonLetter(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumNumeric(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumSymbols(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumUpperCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordQuality(ComponentName componentName, int i, boolean z) throws RemoteException;

    SystemUpdateInfo getPendingSystemUpdate(ComponentName componentName) throws RemoteException;

    int getPermissionGrantState(ComponentName componentName, String str, String str2, String str3) throws RemoteException;

    int getPermissionPolicy(ComponentName componentName) throws RemoteException;

    List getPermittedAccessibilityServices(ComponentName componentName) throws RemoteException;

    List getPermittedAccessibilityServicesForUser(int i) throws RemoteException;

    List<String> getPermittedCrossProfileNotificationListeners(ComponentName componentName) throws RemoteException;

    List getPermittedInputMethods(ComponentName componentName) throws RemoteException;

    List getPermittedInputMethodsForCurrentUser() throws RemoteException;

    ComponentName getProfileOwner(int i) throws RemoteException;

    ComponentName getProfileOwnerAsUser(int i) throws RemoteException;

    String getProfileOwnerName(int i) throws RemoteException;

    int getProfileWithMinimumFailedPasswordsForWipe(int i, boolean z) throws RemoteException;

    void getRemoveWarning(ComponentName componentName, RemoteCallback remoteCallback, int i) throws RemoteException;

    long getRequiredStrongAuthTimeout(ComponentName componentName, int i, boolean z) throws RemoteException;

    ComponentName getRestrictionsProvider(int i) throws RemoteException;

    boolean getScreenCaptureDisabled(ComponentName componentName, int i) throws RemoteException;

    List<UserHandle> getSecondaryUsers(ComponentName componentName) throws RemoteException;

    CharSequence getShortSupportMessage(ComponentName componentName) throws RemoteException;

    CharSequence getShortSupportMessageForUser(ComponentName componentName, int i) throws RemoteException;

    CharSequence getStartUserSessionMessage(ComponentName componentName) throws RemoteException;

    boolean getStorageEncryption(ComponentName componentName, int i) throws RemoteException;

    int getStorageEncryptionStatus(String str, int i) throws RemoteException;

    SystemUpdatePolicy getSystemUpdatePolicy() throws RemoteException;

    PersistableBundle getTransferOwnershipBundle() throws RemoteException;

    List<PersistableBundle> getTrustAgentConfiguration(ComponentName componentName, ComponentName componentName2, int i, boolean z) throws RemoteException;

    int getUserProvisioningState() throws RemoteException;

    Bundle getUserRestrictions(ComponentName componentName) throws RemoteException;

    String getWifiMacAddress(ComponentName componentName) throws RemoteException;

    void grantDeviceIdsAccessToProfileOwner(ComponentName componentName, int i) throws RemoteException;

    boolean hasDeviceOwner() throws RemoteException;

    boolean hasGrantedPolicy(ComponentName componentName, int i, int i2) throws RemoteException;

    boolean hasUserSetupCompleted() throws RemoteException;

    boolean installCaCert(ComponentName componentName, String str, byte[] bArr) throws RemoteException;

    boolean installExistingPackage(ComponentName componentName, String str, String str2) throws RemoteException;

    boolean installKeyPair(ComponentName componentName, String str, byte[] bArr, byte[] bArr2, byte[] bArr3, String str2, boolean z, boolean z2) throws RemoteException;

    void installUpdateFromFile(ComponentName componentName, ParcelFileDescriptor parcelFileDescriptor, StartInstallingUpdateCallback startInstallingUpdateCallback) throws RemoteException;

    boolean isAccessibilityServicePermittedByAdmin(ComponentName componentName, String str, int i) throws RemoteException;

    boolean isActivePasswordSufficient(int i, boolean z) throws RemoteException;

    boolean isAdminActive(ComponentName componentName, int i) throws RemoteException;

    boolean isAffiliatedUser() throws RemoteException;

    boolean isAlwaysOnVpnLockdownEnabled(ComponentName componentName) throws RemoteException;

    boolean isApplicationHidden(ComponentName componentName, String str, String str2) throws RemoteException;

    boolean isBackupServiceEnabled(ComponentName componentName) throws RemoteException;

    boolean isCaCertApproved(String str, int i) throws RemoteException;

    boolean isCallerApplicationRestrictionsManagingPackage(String str) throws RemoteException;

    boolean isCurrentInputMethodSetByOwner() throws RemoteException;

    boolean isDeviceProvisioned() throws RemoteException;

    boolean isDeviceProvisioningConfigApplied() throws RemoteException;

    boolean isEphemeralUser(ComponentName componentName) throws RemoteException;

    boolean isInputMethodPermittedByAdmin(ComponentName componentName, String str, int i) throws RemoteException;

    boolean isLockTaskPermitted(String str) throws RemoteException;

    boolean isLogoutEnabled() throws RemoteException;

    boolean isManagedKiosk() throws RemoteException;

    boolean isManagedProfile(ComponentName componentName) throws RemoteException;

    boolean isMasterVolumeMuted(ComponentName componentName) throws RemoteException;

    boolean isMeteredDataDisabledPackageForUser(ComponentName componentName, String str, int i) throws RemoteException;

    boolean isNetworkLoggingEnabled(ComponentName componentName, String str) throws RemoteException;

    boolean isNotificationListenerServicePermitted(String str, int i) throws RemoteException;

    boolean isOverrideApnEnabled(ComponentName componentName) throws RemoteException;

    boolean isPackageAllowedToAccessCalendarForUser(String str, int i) throws RemoteException;

    boolean isPackageSuspended(ComponentName componentName, String str, String str2) throws RemoteException;

    boolean isProfileActivePasswordSufficientForParent(int i) throws RemoteException;

    boolean isProvisioningAllowed(String str, String str2) throws RemoteException;

    boolean isRemovingAdmin(ComponentName componentName, int i) throws RemoteException;

    boolean isResetPasswordTokenActive(ComponentName componentName) throws RemoteException;

    boolean isSecurityLoggingEnabled(ComponentName componentName) throws RemoteException;

    boolean isSeparateProfileChallengeAllowed(int i) throws RemoteException;

    boolean isSystemOnlyUser(ComponentName componentName) throws RemoteException;

    boolean isUnattendedManagedKiosk() throws RemoteException;

    boolean isUninstallBlocked(ComponentName componentName, String str) throws RemoteException;

    boolean isUninstallInQueue(String str) throws RemoteException;

    boolean isUsingUnifiedPassword(ComponentName componentName) throws RemoteException;

    void lockNow(int i, boolean z) throws RemoteException;

    int logoutUser(ComponentName componentName) throws RemoteException;

    void notifyLockTaskModeChanged(boolean z, String str, int i) throws RemoteException;

    void notifyPendingSystemUpdate(SystemUpdateInfo systemUpdateInfo) throws RemoteException;

    @UnsupportedAppUsage
    boolean packageHasActiveAdmins(String str, int i) throws RemoteException;

    void reboot(ComponentName componentName) throws RemoteException;

    void removeActiveAdmin(ComponentName componentName, int i) throws RemoteException;

    boolean removeCrossProfileWidgetProvider(ComponentName componentName, String str) throws RemoteException;

    boolean removeKeyPair(ComponentName componentName, String str, String str2) throws RemoteException;

    boolean removeOverrideApn(ComponentName componentName, int i) throws RemoteException;

    boolean removeUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    void reportFailedBiometricAttempt(int i) throws RemoteException;

    void reportFailedPasswordAttempt(int i) throws RemoteException;

    void reportKeyguardDismissed(int i) throws RemoteException;

    void reportKeyguardSecured(int i) throws RemoteException;

    void reportPasswordChanged(int i) throws RemoteException;

    void reportSuccessfulBiometricAttempt(int i) throws RemoteException;

    void reportSuccessfulPasswordAttempt(int i) throws RemoteException;

    boolean requestBugreport(ComponentName componentName) throws RemoteException;

    boolean resetPassword(String str, int i) throws RemoteException;

    boolean resetPasswordWithToken(ComponentName componentName, String str, byte[] bArr, int i) throws RemoteException;

    List<NetworkEvent> retrieveNetworkLogs(ComponentName componentName, String str, long j) throws RemoteException;

    ParceledListSlice retrievePreRebootSecurityLogs(ComponentName componentName) throws RemoteException;

    ParceledListSlice retrieveSecurityLogs(ComponentName componentName) throws RemoteException;

    void setAccountManagementDisabled(ComponentName componentName, String str, boolean z) throws RemoteException;

    void setActiveAdmin(ComponentName componentName, boolean z, int i) throws RemoteException;

    void setActivePasswordState(PasswordMetrics passwordMetrics, int i) throws RemoteException;

    void setAffiliationIds(ComponentName componentName, List<String> list) throws RemoteException;

    boolean setAlwaysOnVpnPackage(ComponentName componentName, String str, boolean z, List<String> list) throws RemoteException;

    boolean setApplicationHidden(ComponentName componentName, String str, String str2, boolean z) throws RemoteException;

    void setApplicationRestrictions(ComponentName componentName, String str, String str2, Bundle bundle) throws RemoteException;

    boolean setApplicationRestrictionsManagingPackage(ComponentName componentName, String str) throws RemoteException;

    void setAutoTimeRequired(ComponentName componentName, boolean z) throws RemoteException;

    void setBackupServiceEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setBluetoothContactSharingDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setCameraDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setCertInstallerPackage(ComponentName componentName, String str) throws RemoteException;

    void setCrossProfileCalendarPackages(ComponentName componentName, List<String> list) throws RemoteException;

    void setCrossProfileCallerIdDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setCrossProfileContactsSearchDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setDefaultSmsApplication(ComponentName componentName, String str) throws RemoteException;

    void setDelegatedScopes(ComponentName componentName, String str, List<String> list) throws RemoteException;

    boolean setDeviceOwner(ComponentName componentName, String str, int i) throws RemoteException;

    void setDeviceOwnerLockScreenInfo(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setDeviceProvisioningConfigApplied() throws RemoteException;

    void setEndUserSessionMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setForceEphemeralUsers(ComponentName componentName, boolean z) throws RemoteException;

    int setGlobalPrivateDns(ComponentName componentName, int i, String str) throws RemoteException;

    ComponentName setGlobalProxy(ComponentName componentName, String str, String str2) throws RemoteException;

    void setGlobalSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    void setKeepUninstalledPackages(ComponentName componentName, String str, List<String> list) throws RemoteException;

    boolean setKeyPairCertificate(ComponentName componentName, String str, String str2, byte[] bArr, byte[] bArr2, boolean z) throws RemoteException;

    boolean setKeyguardDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setKeyguardDisabledFeatures(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setLockTaskFeatures(ComponentName componentName, int i) throws RemoteException;

    void setLockTaskPackages(ComponentName componentName, String[] strArr) throws RemoteException;

    void setLogoutEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setLongSupportMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setMasterVolumeMuted(ComponentName componentName, boolean z) throws RemoteException;

    void setMaximumFailedPasswordsForWipe(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setMaximumTimeToLock(ComponentName componentName, long j, boolean z) throws RemoteException;

    List<String> setMeteredDataDisabledPackages(ComponentName componentName, List<String> list) throws RemoteException;

    void setNetworkLoggingEnabled(ComponentName componentName, String str, boolean z) throws RemoteException;

    void setOrganizationColor(ComponentName componentName, int i) throws RemoteException;

    void setOrganizationColorForUser(int i, int i2) throws RemoteException;

    void setOrganizationName(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setOverrideApnsEnabled(ComponentName componentName, boolean z) throws RemoteException;

    String[] setPackagesSuspended(ComponentName componentName, String str, String[] strArr, boolean z) throws RemoteException;

    void setPasswordExpirationTimeout(ComponentName componentName, long j, boolean z) throws RemoteException;

    void setPasswordHistoryLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumLetters(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumLowerCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumNonLetter(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumNumeric(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumSymbols(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumUpperCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordQuality(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPermissionGrantState(ComponentName componentName, String str, String str2, String str3, int i, RemoteCallback remoteCallback) throws RemoteException;

    void setPermissionPolicy(ComponentName componentName, String str, int i) throws RemoteException;

    boolean setPermittedAccessibilityServices(ComponentName componentName, List list) throws RemoteException;

    boolean setPermittedCrossProfileNotificationListeners(ComponentName componentName, List<String> list) throws RemoteException;

    boolean setPermittedInputMethods(ComponentName componentName, List list) throws RemoteException;

    void setProfileEnabled(ComponentName componentName) throws RemoteException;

    void setProfileName(ComponentName componentName, String str) throws RemoteException;

    boolean setProfileOwner(ComponentName componentName, String str, int i) throws RemoteException;

    void setRecommendedGlobalProxy(ComponentName componentName, ProxyInfo proxyInfo) throws RemoteException;

    void setRequiredStrongAuthTimeout(ComponentName componentName, long j, boolean z) throws RemoteException;

    boolean setResetPasswordToken(ComponentName componentName, byte[] bArr) throws RemoteException;

    void setRestrictionsProvider(ComponentName componentName, ComponentName componentName2) throws RemoteException;

    void setScreenCaptureDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setSecureSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    void setSecurityLoggingEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setShortSupportMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setStartUserSessionMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    boolean setStatusBarDisabled(ComponentName componentName, boolean z) throws RemoteException;

    int setStorageEncryption(ComponentName componentName, boolean z) throws RemoteException;

    void setSystemSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    void setSystemUpdatePolicy(ComponentName componentName, SystemUpdatePolicy systemUpdatePolicy) throws RemoteException;

    boolean setTime(ComponentName componentName, long j) throws RemoteException;

    boolean setTimeZone(ComponentName componentName, String str) throws RemoteException;

    void setTrustAgentConfiguration(ComponentName componentName, ComponentName componentName2, PersistableBundle persistableBundle, boolean z) throws RemoteException;

    void setUninstallBlocked(ComponentName componentName, String str, String str2, boolean z) throws RemoteException;

    void setUserIcon(ComponentName componentName, Bitmap bitmap) throws RemoteException;

    void setUserProvisioningState(int i, int i2) throws RemoteException;

    void setUserRestriction(ComponentName componentName, String str, boolean z) throws RemoteException;

    void startManagedQuickContact(String str, long j, boolean z, long j2, Intent intent) throws RemoteException;

    int startUserInBackground(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    boolean startViewCalendarEventInManagedProfile(String str, long j, long j2, long j3, boolean z, int i) throws RemoteException;

    int stopUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    boolean switchUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    void transferOwnership(ComponentName componentName, ComponentName componentName2, PersistableBundle persistableBundle) throws RemoteException;

    void uninstallCaCerts(ComponentName componentName, String str, String[] strArr) throws RemoteException;

    void uninstallPackageWithActiveAdmins(String str) throws RemoteException;

    boolean updateOverrideApn(ComponentName componentName, int i, ApnSetting apnSetting) throws RemoteException;

    void wipeDataWithReason(int i, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IDevicePolicyManager {
        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordQuality(ComponentName who, int quality, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordQuality(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumLength(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumUpperCase(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumUpperCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumLowerCase(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumLowerCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumLetters(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumLetters(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumNumeric(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumNumeric(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumSymbols(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumSymbols(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumNonLetter(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumNonLetter(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordHistoryLength(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordHistoryLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordExpirationTimeout(ComponentName who, long expiration, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getPasswordExpirationTimeout(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getPasswordExpiration(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isActivePasswordSufficient(int userHandle, boolean parent) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isProfileActivePasswordSufficientForParent(int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordComplexity() throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isUsingUnifiedPassword(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getCurrentFailedPasswordAttempts(int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getProfileWithMinimumFailedPasswordsForWipe(int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setMaximumFailedPasswordsForWipe(ComponentName admin, int num, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getMaximumFailedPasswordsForWipe(ComponentName admin, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean resetPassword(String password, int flags) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setMaximumTimeToLock(ComponentName who, long timeMs, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getMaximumTimeToLock(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setRequiredStrongAuthTimeout(ComponentName who, long timeMs, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getRequiredStrongAuthTimeout(ComponentName who, int userId, boolean parent) throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void lockNow(int flags, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void wipeDataWithReason(int flags, String wipeReasonForUser) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName setGlobalProxy(ComponentName admin, String proxySpec, String exclusionList) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName getGlobalProxyAdmin(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setRecommendedGlobalProxy(ComponentName admin, ProxyInfo proxyInfo) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int setStorageEncryption(ComponentName who, boolean encrypt) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getStorageEncryption(ComponentName who, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getStorageEncryptionStatus(String callerPackage, int userHandle) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean requestBugreport(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCameraDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getCameraDisabled(ComponentName who, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setScreenCaptureDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getScreenCaptureDisabled(ComponentName who, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setKeyguardDisabledFeatures(ComponentName who, int which, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getKeyguardDisabledFeatures(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setActiveAdmin(ComponentName policyReceiver, boolean refreshing, int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isAdminActive(ComponentName policyReceiver, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<ComponentName> getActiveAdmins(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean packageHasActiveAdmins(String packageName, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void getRemoveWarning(ComponentName policyReceiver, RemoteCallback result, int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void removeActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void forceRemoveActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean hasGrantedPolicy(ComponentName policyReceiver, int usesPolicy, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setActivePasswordState(PasswordMetrics metrics, int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportPasswordChanged(int userId) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportFailedPasswordAttempt(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportSuccessfulPasswordAttempt(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportFailedBiometricAttempt(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportSuccessfulBiometricAttempt(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportKeyguardDismissed(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportKeyguardSecured(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setDeviceOwner(ComponentName who, String ownerName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName getDeviceOwnerComponent(boolean callingUserOnly) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean hasDeviceOwner() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getDeviceOwnerName() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearDeviceOwner(String packageName) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getDeviceOwnerUserId() throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setProfileOwner(ComponentName who, String ownerName, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName getProfileOwnerAsUser(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName getProfileOwner(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getProfileOwnerName(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setProfileEnabled(ComponentName who) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setProfileName(ComponentName who, String profileName) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearProfileOwner(ComponentName who) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean hasUserSetupCompleted() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean checkDeviceIdentifierAccess(String packageName, int pid, int uid) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setDeviceOwnerLockScreenInfo(ComponentName who, CharSequence deviceOwnerInfo) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getDeviceOwnerLockScreenInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String[] setPackagesSuspended(ComponentName admin, String callerPackage, String[] packageNames, boolean suspended) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isPackageSuspended(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean installCaCert(ComponentName admin, String callerPackage, byte[] certBuffer) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void uninstallCaCerts(ComponentName admin, String callerPackage, String[] aliases) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void enforceCanManageCaCerts(ComponentName admin, String callerPackage) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean approveCaCert(String alias, int userHandle, boolean approval) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isCaCertApproved(String alias, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean installKeyPair(ComponentName who, String callerPackage, byte[] privKeyBuffer, byte[] certBuffer, byte[] certChainBuffer, String alias, boolean requestAccess, boolean isUserSelectable) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean removeKeyPair(ComponentName who, String callerPackage, String alias) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean generateKeyPair(ComponentName who, String callerPackage, String algorithm, ParcelableKeyGenParameterSpec keySpec, int idAttestationFlags, KeymasterCertificateChain attestationChain) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setKeyPairCertificate(ComponentName who, String callerPackage, String alias, byte[] certBuffer, byte[] certChainBuffer, boolean isUserSelectable) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void choosePrivateKeyAlias(int uid, Uri uri, String alias, IBinder aliasCallback) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setDelegatedScopes(ComponentName who, String delegatePackage, List<String> scopes) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getDelegatedScopes(ComponentName who, String delegatePackage) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getDelegatePackages(ComponentName who, String scope) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCertInstallerPackage(ComponentName who, String installerPackage) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getCertInstallerPackage(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setAlwaysOnVpnPackage(ComponentName who, String vpnPackage, boolean lockdown, List<String> lockdownWhitelist) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getAlwaysOnVpnPackage(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isAlwaysOnVpnLockdownEnabled(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getAlwaysOnVpnLockdownWhitelist(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void addPersistentPreferredActivity(ComponentName admin, IntentFilter filter, ComponentName activity) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearPackagePersistentPreferredActivities(ComponentName admin, String packageName) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setDefaultSmsApplication(ComponentName admin, String packageName) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setApplicationRestrictions(ComponentName who, String callerPackage, String packageName, Bundle settings) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public Bundle getApplicationRestrictions(ComponentName who, String callerPackage, String packageName) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setApplicationRestrictionsManagingPackage(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getApplicationRestrictionsManagingPackage(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isCallerApplicationRestrictionsManagingPackage(String callerPackage) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setRestrictionsProvider(ComponentName who, ComponentName provider) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName getRestrictionsProvider(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setUserRestriction(ComponentName who, String key, boolean enable) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public Bundle getUserRestrictions(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void addCrossProfileIntentFilter(ComponentName admin, IntentFilter filter, int flags) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearCrossProfileIntentFilters(ComponentName admin) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setPermittedAccessibilityServices(ComponentName admin, List packageList) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List getPermittedAccessibilityServices(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List getPermittedAccessibilityServicesForUser(int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isAccessibilityServicePermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setPermittedInputMethods(ComponentName admin, List packageList) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List getPermittedInputMethods(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List getPermittedInputMethodsForCurrentUser() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isInputMethodPermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setPermittedCrossProfileNotificationListeners(ComponentName admin, List<String> packageList) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getPermittedCrossProfileNotificationListeners(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isNotificationListenerServicePermitted(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public Intent createAdminSupportIntent(String restriction) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setApplicationHidden(ComponentName admin, String callerPackage, String packageName, boolean hidden) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isApplicationHidden(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public UserHandle createAndManageUser(ComponentName who, String name, ComponentName profileOwner, PersistableBundle adminExtras, int flags) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean removeUser(ComponentName who, UserHandle userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean switchUser(ComponentName who, UserHandle userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int startUserInBackground(ComponentName who, UserHandle userHandle) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int stopUser(ComponentName who, UserHandle userHandle) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int logoutUser(ComponentName who) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<UserHandle> getSecondaryUsers(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void enableSystemApp(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int enableSystemAppWithIntent(ComponentName admin, String callerPackage, Intent intent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean installExistingPackage(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setAccountManagementDisabled(ComponentName who, String accountType, boolean disabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String[] getAccountTypesWithManagementDisabled() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String[] getAccountTypesWithManagementDisabledAsUser(int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setLockTaskPackages(ComponentName who, String[] packages) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String[] getLockTaskPackages(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isLockTaskPermitted(String pkg) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setLockTaskFeatures(ComponentName who, int flags) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getLockTaskFeatures(ComponentName who) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setGlobalSetting(ComponentName who, String setting, String value) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setSystemSetting(ComponentName who, String setting, String value) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setSecureSetting(ComponentName who, String setting, String value) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setTime(ComponentName who, long millis) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setTimeZone(ComponentName who, String timeZone) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setMasterVolumeMuted(ComponentName admin, boolean on) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isMasterVolumeMuted(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void notifyLockTaskModeChanged(boolean isEnabled, String pkg, int userId) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setUninstallBlocked(ComponentName admin, String callerPackage, String packageName, boolean uninstallBlocked) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isUninstallBlocked(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCrossProfileCallerIdDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getCrossProfileCallerIdDisabled(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getCrossProfileCallerIdDisabledForUser(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCrossProfileContactsSearchDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getCrossProfileContactsSearchDisabled(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getCrossProfileContactsSearchDisabledForUser(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void startManagedQuickContact(String lookupKey, long contactId, boolean isContactIdIgnored, long directoryId, Intent originalIntent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setBluetoothContactSharingDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getBluetoothContactSharingDisabled(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getBluetoothContactSharingDisabledForUser(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setTrustAgentConfiguration(ComponentName admin, ComponentName agent, PersistableBundle args, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<PersistableBundle> getTrustAgentConfiguration(ComponentName admin, ComponentName agent, int userId, boolean parent) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean addCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean removeCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getCrossProfileWidgetProviders(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setAutoTimeRequired(ComponentName who, boolean required) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getAutoTimeRequired() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setForceEphemeralUsers(ComponentName who, boolean forceEpehemeralUsers) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getForceEphemeralUsers(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isRemovingAdmin(ComponentName adminReceiver, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setUserIcon(ComponentName admin, Bitmap icon) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setSystemUpdatePolicy(ComponentName who, SystemUpdatePolicy policy) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public SystemUpdatePolicy getSystemUpdatePolicy() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearSystemUpdatePolicyFreezePeriodRecord() throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setKeyguardDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setStatusBarDisabled(ComponentName who, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getDoNotAskCredentialsOnBoot() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void notifyPendingSystemUpdate(SystemUpdateInfo info) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public SystemUpdateInfo getPendingSystemUpdate(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPermissionPolicy(ComponentName admin, String callerPackage, int policy) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPermissionPolicy(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission, int grantState, RemoteCallback resultReceiver) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isProvisioningAllowed(String action, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int checkProvisioningPreCondition(String action, String packageName) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setKeepUninstalledPackages(ComponentName admin, String callerPackage, List<String> packageList) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getKeepUninstalledPackages(ComponentName admin, String callerPackage) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isManagedProfile(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isSystemOnlyUser(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getWifiMacAddress(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reboot(ComponentName admin) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setShortSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getShortSupportMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setLongSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getLongSupportMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getShortSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getLongSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isSeparateProfileChallengeAllowed(int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setOrganizationColor(ComponentName admin, int color) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setOrganizationColorForUser(int color, int userId) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getOrganizationColor(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getOrganizationColorForUser(int userHandle) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setOrganizationName(ComponentName admin, CharSequence title) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getOrganizationName(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getDeviceOwnerOrganizationName() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getOrganizationNameForUser(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getUserProvisioningState() throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setUserProvisioningState(int state, int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setAffiliationIds(ComponentName admin, List<String> ids) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getAffiliationIds(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isAffiliatedUser() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setSecurityLoggingEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isSecurityLoggingEnabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ParceledListSlice retrieveSecurityLogs(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ParceledListSlice retrievePreRebootSecurityLogs(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long forceNetworkLogs() throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long forceSecurityLogs() throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isUninstallInQueue(String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void uninstallPackageWithActiveAdmins(String packageName) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isDeviceProvisioned() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isDeviceProvisioningConfigApplied() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setDeviceProvisioningConfigApplied() throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void forceUpdateUserSetupComplete() throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setBackupServiceEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isBackupServiceEnabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setNetworkLoggingEnabled(ComponentName admin, String packageName, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isNetworkLoggingEnabled(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<NetworkEvent> retrieveNetworkLogs(ComponentName admin, String packageName, long batchToken) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean bindDeviceAdminServiceAsUser(ComponentName admin, IApplicationThread caller, IBinder token, Intent service, IServiceConnection connection, int flags, int targetUserId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isEphemeralUser(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getLastSecurityLogRetrievalTime() throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getLastBugReportRequestTime() throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getLastNetworkLogRetrievalTime() throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setResetPasswordToken(ComponentName admin, byte[] token) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean clearResetPasswordToken(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isResetPasswordTokenActive(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean resetPasswordWithToken(ComponentName admin, String password, byte[] token, int flags) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isCurrentInputMethodSetByOwner() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public StringParceledListSlice getOwnerInstalledCaCerts(UserHandle user) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearApplicationUserData(ComponentName admin, String packageName, IPackageDataObserver callback) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setLogoutEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isLogoutEnabled() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getDisallowedSystemApps(ComponentName admin, int userId, String provisioningAction) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void transferOwnership(ComponentName admin, ComponentName target, PersistableBundle bundle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public PersistableBundle getTransferOwnershipBundle() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setStartUserSessionMessage(ComponentName admin, CharSequence startUserSessionMessage) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setEndUserSessionMessage(ComponentName admin, CharSequence endUserSessionMessage) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getStartUserSessionMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getEndUserSessionMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> setMeteredDataDisabledPackages(ComponentName admin, List<String> packageNames) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getMeteredDataDisabledPackages(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int addOverrideApn(ComponentName admin, ApnSetting apnSetting) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean updateOverrideApn(ComponentName admin, int apnId, ApnSetting apnSetting) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean removeOverrideApn(ComponentName admin, int apnId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<ApnSetting> getOverrideApns(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setOverrideApnsEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isOverrideApnEnabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isMeteredDataDisabledPackageForUser(ComponentName admin, String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int setGlobalPrivateDns(ComponentName admin, int mode, String privateDnsHost) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getGlobalPrivateDnsMode(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getGlobalPrivateDnsHost(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void grantDeviceIdsAccessToProfileOwner(ComponentName who, int userId) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void installUpdateFromFile(ComponentName admin, ParcelFileDescriptor updateFileDescriptor, StartInstallingUpdateCallback listener) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCrossProfileCalendarPackages(ComponentName admin, List<String> packageNames) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getCrossProfileCalendarPackages(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isPackageAllowedToAccessCalendarForUser(String packageName, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getCrossProfileCalendarPackagesForUser(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isManagedKiosk() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isUnattendedManagedKiosk() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean startViewCalendarEventInManagedProfile(String packageName, long eventId, long start, long end, boolean allDay, int flags) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IDevicePolicyManager {
        private static final String DESCRIPTOR = "android.app.admin.IDevicePolicyManager";
        static final int TRANSACTION_addCrossProfileIntentFilter = 116;
        static final int TRANSACTION_addCrossProfileWidgetProvider = 172;
        static final int TRANSACTION_addOverrideApn = 262;
        static final int TRANSACTION_addPersistentPreferredActivity = 104;
        static final int TRANSACTION_approveCaCert = 88;
        static final int TRANSACTION_bindDeviceAdminServiceAsUser = 238;
        static final int TRANSACTION_checkDeviceIdentifierAccess = 80;
        static final int TRANSACTION_checkProvisioningPreCondition = 194;
        static final int TRANSACTION_choosePrivateKeyAlias = 94;
        static final int TRANSACTION_clearApplicationUserData = 250;
        static final int TRANSACTION_clearCrossProfileIntentFilters = 117;
        static final int TRANSACTION_clearDeviceOwner = 70;
        static final int TRANSACTION_clearPackagePersistentPreferredActivities = 105;
        static final int TRANSACTION_clearProfileOwner = 78;
        static final int TRANSACTION_clearResetPasswordToken = 245;
        static final int TRANSACTION_clearSystemUpdatePolicyFreezePeriodRecord = 183;
        static final int TRANSACTION_createAdminSupportIntent = 129;
        static final int TRANSACTION_createAndManageUser = 132;
        static final int TRANSACTION_enableSystemApp = 139;
        static final int TRANSACTION_enableSystemAppWithIntent = 140;
        static final int TRANSACTION_enforceCanManageCaCerts = 87;
        static final int TRANSACTION_forceNetworkLogs = 225;
        static final int TRANSACTION_forceRemoveActiveAdmin = 56;
        static final int TRANSACTION_forceSecurityLogs = 226;
        static final int TRANSACTION_forceUpdateUserSetupComplete = 232;
        static final int TRANSACTION_generateKeyPair = 92;
        static final int TRANSACTION_getAccountTypesWithManagementDisabled = 143;
        static final int TRANSACTION_getAccountTypesWithManagementDisabledAsUser = 144;
        static final int TRANSACTION_getActiveAdmins = 52;
        static final int TRANSACTION_getAffiliationIds = 219;
        static final int TRANSACTION_getAlwaysOnVpnLockdownWhitelist = 103;
        static final int TRANSACTION_getAlwaysOnVpnPackage = 101;
        static final int TRANSACTION_getApplicationRestrictions = 108;
        static final int TRANSACTION_getApplicationRestrictionsManagingPackage = 110;
        static final int TRANSACTION_getAutoTimeRequired = 176;
        static final int TRANSACTION_getBindDeviceAdminTargetUsers = 239;
        static final int TRANSACTION_getBluetoothContactSharingDisabled = 168;
        static final int TRANSACTION_getBluetoothContactSharingDisabledForUser = 169;
        static final int TRANSACTION_getCameraDisabled = 45;
        static final int TRANSACTION_getCertInstallerPackage = 99;
        static final int TRANSACTION_getCrossProfileCalendarPackages = 275;
        static final int TRANSACTION_getCrossProfileCalendarPackagesForUser = 277;
        static final int TRANSACTION_getCrossProfileCallerIdDisabled = 161;
        static final int TRANSACTION_getCrossProfileCallerIdDisabledForUser = 162;
        static final int TRANSACTION_getCrossProfileContactsSearchDisabled = 164;
        static final int TRANSACTION_getCrossProfileContactsSearchDisabledForUser = 165;
        static final int TRANSACTION_getCrossProfileWidgetProviders = 174;
        static final int TRANSACTION_getCurrentFailedPasswordAttempts = 26;
        static final int TRANSACTION_getDelegatePackages = 97;
        static final int TRANSACTION_getDelegatedScopes = 96;
        static final int TRANSACTION_getDeviceOwnerComponent = 67;
        static final int TRANSACTION_getDeviceOwnerLockScreenInfo = 82;
        static final int TRANSACTION_getDeviceOwnerName = 69;
        static final int TRANSACTION_getDeviceOwnerOrganizationName = 214;
        static final int TRANSACTION_getDeviceOwnerUserId = 71;
        static final int TRANSACTION_getDisallowedSystemApps = 253;
        static final int TRANSACTION_getDoNotAskCredentialsOnBoot = 186;
        static final int TRANSACTION_getEndUserSessionMessage = 259;
        static final int TRANSACTION_getForceEphemeralUsers = 178;
        static final int TRANSACTION_getGlobalPrivateDnsHost = 271;
        static final int TRANSACTION_getGlobalPrivateDnsMode = 270;
        static final int TRANSACTION_getGlobalProxyAdmin = 38;
        static final int TRANSACTION_getKeepUninstalledPackages = 196;
        static final int TRANSACTION_getKeyguardDisabledFeatures = 49;
        static final int TRANSACTION_getLastBugReportRequestTime = 242;
        static final int TRANSACTION_getLastNetworkLogRetrievalTime = 243;
        static final int TRANSACTION_getLastSecurityLogRetrievalTime = 241;
        static final int TRANSACTION_getLockTaskFeatures = 149;
        static final int TRANSACTION_getLockTaskPackages = 146;
        static final int TRANSACTION_getLongSupportMessage = 204;
        static final int TRANSACTION_getLongSupportMessageForUser = 206;
        static final int TRANSACTION_getMaximumFailedPasswordsForWipe = 29;
        static final int TRANSACTION_getMaximumTimeToLock = 32;
        static final int TRANSACTION_getMeteredDataDisabledPackages = 261;
        static final int TRANSACTION_getOrganizationColor = 210;
        static final int TRANSACTION_getOrganizationColorForUser = 211;
        static final int TRANSACTION_getOrganizationName = 213;
        static final int TRANSACTION_getOrganizationNameForUser = 215;
        static final int TRANSACTION_getOverrideApns = 265;
        static final int TRANSACTION_getOwnerInstalledCaCerts = 249;
        static final int TRANSACTION_getPasswordComplexity = 24;
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
        static final int TRANSACTION_getPendingSystemUpdate = 188;
        static final int TRANSACTION_getPermissionGrantState = 192;
        static final int TRANSACTION_getPermissionPolicy = 190;
        static final int TRANSACTION_getPermittedAccessibilityServices = 119;
        static final int TRANSACTION_getPermittedAccessibilityServicesForUser = 120;
        static final int TRANSACTION_getPermittedCrossProfileNotificationListeners = 127;
        static final int TRANSACTION_getPermittedInputMethods = 123;
        static final int TRANSACTION_getPermittedInputMethodsForCurrentUser = 124;
        static final int TRANSACTION_getProfileOwner = 74;
        static final int TRANSACTION_getProfileOwnerAsUser = 73;
        static final int TRANSACTION_getProfileOwnerName = 75;
        static final int TRANSACTION_getProfileWithMinimumFailedPasswordsForWipe = 27;
        static final int TRANSACTION_getRemoveWarning = 54;
        static final int TRANSACTION_getRequiredStrongAuthTimeout = 34;
        static final int TRANSACTION_getRestrictionsProvider = 113;
        static final int TRANSACTION_getScreenCaptureDisabled = 47;
        static final int TRANSACTION_getSecondaryUsers = 138;
        static final int TRANSACTION_getShortSupportMessage = 202;
        static final int TRANSACTION_getShortSupportMessageForUser = 205;
        static final int TRANSACTION_getStartUserSessionMessage = 258;
        static final int TRANSACTION_getStorageEncryption = 41;
        static final int TRANSACTION_getStorageEncryptionStatus = 42;
        static final int TRANSACTION_getSystemUpdatePolicy = 182;
        static final int TRANSACTION_getTransferOwnershipBundle = 255;
        static final int TRANSACTION_getTrustAgentConfiguration = 171;
        static final int TRANSACTION_getUserProvisioningState = 216;
        static final int TRANSACTION_getUserRestrictions = 115;
        static final int TRANSACTION_getWifiMacAddress = 199;
        static final int TRANSACTION_grantDeviceIdsAccessToProfileOwner = 272;
        static final int TRANSACTION_hasDeviceOwner = 68;
        static final int TRANSACTION_hasGrantedPolicy = 57;
        static final int TRANSACTION_hasUserSetupCompleted = 79;
        static final int TRANSACTION_installCaCert = 85;
        static final int TRANSACTION_installExistingPackage = 141;
        static final int TRANSACTION_installKeyPair = 90;
        static final int TRANSACTION_installUpdateFromFile = 273;
        static final int TRANSACTION_isAccessibilityServicePermittedByAdmin = 121;
        static final int TRANSACTION_isActivePasswordSufficient = 22;
        static final int TRANSACTION_isAdminActive = 51;
        static final int TRANSACTION_isAffiliatedUser = 220;
        static final int TRANSACTION_isAlwaysOnVpnLockdownEnabled = 102;
        static final int TRANSACTION_isApplicationHidden = 131;
        static final int TRANSACTION_isBackupServiceEnabled = 234;
        static final int TRANSACTION_isCaCertApproved = 89;
        static final int TRANSACTION_isCallerApplicationRestrictionsManagingPackage = 111;
        static final int TRANSACTION_isCurrentInputMethodSetByOwner = 248;
        static final int TRANSACTION_isDeviceProvisioned = 229;
        static final int TRANSACTION_isDeviceProvisioningConfigApplied = 230;
        static final int TRANSACTION_isEphemeralUser = 240;
        static final int TRANSACTION_isInputMethodPermittedByAdmin = 125;
        static final int TRANSACTION_isLockTaskPermitted = 147;
        static final int TRANSACTION_isLogoutEnabled = 252;
        static final int TRANSACTION_isManagedKiosk = 278;
        static final int TRANSACTION_isManagedProfile = 197;
        static final int TRANSACTION_isMasterVolumeMuted = 156;
        static final int TRANSACTION_isMeteredDataDisabledPackageForUser = 268;
        static final int TRANSACTION_isNetworkLoggingEnabled = 236;
        static final int TRANSACTION_isNotificationListenerServicePermitted = 128;
        static final int TRANSACTION_isOverrideApnEnabled = 267;
        static final int TRANSACTION_isPackageAllowedToAccessCalendarForUser = 276;
        static final int TRANSACTION_isPackageSuspended = 84;
        static final int TRANSACTION_isProfileActivePasswordSufficientForParent = 23;
        static final int TRANSACTION_isProvisioningAllowed = 193;
        static final int TRANSACTION_isRemovingAdmin = 179;
        static final int TRANSACTION_isResetPasswordTokenActive = 246;
        static final int TRANSACTION_isSecurityLoggingEnabled = 222;
        static final int TRANSACTION_isSeparateProfileChallengeAllowed = 207;
        static final int TRANSACTION_isSystemOnlyUser = 198;
        static final int TRANSACTION_isUnattendedManagedKiosk = 279;
        static final int TRANSACTION_isUninstallBlocked = 159;
        static final int TRANSACTION_isUninstallInQueue = 227;
        static final int TRANSACTION_isUsingUnifiedPassword = 25;
        static final int TRANSACTION_lockNow = 35;
        static final int TRANSACTION_logoutUser = 137;
        static final int TRANSACTION_notifyLockTaskModeChanged = 157;
        static final int TRANSACTION_notifyPendingSystemUpdate = 187;
        static final int TRANSACTION_packageHasActiveAdmins = 53;
        static final int TRANSACTION_reboot = 200;
        static final int TRANSACTION_removeActiveAdmin = 55;
        static final int TRANSACTION_removeCrossProfileWidgetProvider = 173;
        static final int TRANSACTION_removeKeyPair = 91;
        static final int TRANSACTION_removeOverrideApn = 264;
        static final int TRANSACTION_removeUser = 133;
        static final int TRANSACTION_reportFailedBiometricAttempt = 62;
        static final int TRANSACTION_reportFailedPasswordAttempt = 60;
        static final int TRANSACTION_reportKeyguardDismissed = 64;
        static final int TRANSACTION_reportKeyguardSecured = 65;
        static final int TRANSACTION_reportPasswordChanged = 59;
        static final int TRANSACTION_reportSuccessfulBiometricAttempt = 63;
        static final int TRANSACTION_reportSuccessfulPasswordAttempt = 61;
        static final int TRANSACTION_requestBugreport = 43;
        static final int TRANSACTION_resetPassword = 30;
        static final int TRANSACTION_resetPasswordWithToken = 247;
        static final int TRANSACTION_retrieveNetworkLogs = 237;
        static final int TRANSACTION_retrievePreRebootSecurityLogs = 224;
        static final int TRANSACTION_retrieveSecurityLogs = 223;
        static final int TRANSACTION_setAccountManagementDisabled = 142;
        static final int TRANSACTION_setActiveAdmin = 50;
        static final int TRANSACTION_setActivePasswordState = 58;
        static final int TRANSACTION_setAffiliationIds = 218;
        static final int TRANSACTION_setAlwaysOnVpnPackage = 100;
        static final int TRANSACTION_setApplicationHidden = 130;
        static final int TRANSACTION_setApplicationRestrictions = 107;
        static final int TRANSACTION_setApplicationRestrictionsManagingPackage = 109;
        static final int TRANSACTION_setAutoTimeRequired = 175;
        static final int TRANSACTION_setBackupServiceEnabled = 233;
        static final int TRANSACTION_setBluetoothContactSharingDisabled = 167;
        static final int TRANSACTION_setCameraDisabled = 44;
        static final int TRANSACTION_setCertInstallerPackage = 98;
        static final int TRANSACTION_setCrossProfileCalendarPackages = 274;
        static final int TRANSACTION_setCrossProfileCallerIdDisabled = 160;
        static final int TRANSACTION_setCrossProfileContactsSearchDisabled = 163;
        static final int TRANSACTION_setDefaultSmsApplication = 106;
        static final int TRANSACTION_setDelegatedScopes = 95;
        static final int TRANSACTION_setDeviceOwner = 66;
        static final int TRANSACTION_setDeviceOwnerLockScreenInfo = 81;
        static final int TRANSACTION_setDeviceProvisioningConfigApplied = 231;
        static final int TRANSACTION_setEndUserSessionMessage = 257;
        static final int TRANSACTION_setForceEphemeralUsers = 177;
        static final int TRANSACTION_setGlobalPrivateDns = 269;
        static final int TRANSACTION_setGlobalProxy = 37;
        static final int TRANSACTION_setGlobalSetting = 150;
        static final int TRANSACTION_setKeepUninstalledPackages = 195;
        static final int TRANSACTION_setKeyPairCertificate = 93;
        static final int TRANSACTION_setKeyguardDisabled = 184;
        static final int TRANSACTION_setKeyguardDisabledFeatures = 48;
        static final int TRANSACTION_setLockTaskFeatures = 148;
        static final int TRANSACTION_setLockTaskPackages = 145;
        static final int TRANSACTION_setLogoutEnabled = 251;
        static final int TRANSACTION_setLongSupportMessage = 203;
        static final int TRANSACTION_setMasterVolumeMuted = 155;
        static final int TRANSACTION_setMaximumFailedPasswordsForWipe = 28;
        static final int TRANSACTION_setMaximumTimeToLock = 31;
        static final int TRANSACTION_setMeteredDataDisabledPackages = 260;
        static final int TRANSACTION_setNetworkLoggingEnabled = 235;
        static final int TRANSACTION_setOrganizationColor = 208;
        static final int TRANSACTION_setOrganizationColorForUser = 209;
        static final int TRANSACTION_setOrganizationName = 212;
        static final int TRANSACTION_setOverrideApnsEnabled = 266;
        static final int TRANSACTION_setPackagesSuspended = 83;
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
        static final int TRANSACTION_setPermissionGrantState = 191;
        static final int TRANSACTION_setPermissionPolicy = 189;
        static final int TRANSACTION_setPermittedAccessibilityServices = 118;
        static final int TRANSACTION_setPermittedCrossProfileNotificationListeners = 126;
        static final int TRANSACTION_setPermittedInputMethods = 122;
        static final int TRANSACTION_setProfileEnabled = 76;
        static final int TRANSACTION_setProfileName = 77;
        static final int TRANSACTION_setProfileOwner = 72;
        static final int TRANSACTION_setRecommendedGlobalProxy = 39;
        static final int TRANSACTION_setRequiredStrongAuthTimeout = 33;
        static final int TRANSACTION_setResetPasswordToken = 244;
        static final int TRANSACTION_setRestrictionsProvider = 112;
        static final int TRANSACTION_setScreenCaptureDisabled = 46;
        static final int TRANSACTION_setSecureSetting = 152;
        static final int TRANSACTION_setSecurityLoggingEnabled = 221;
        static final int TRANSACTION_setShortSupportMessage = 201;
        static final int TRANSACTION_setStartUserSessionMessage = 256;
        static final int TRANSACTION_setStatusBarDisabled = 185;
        static final int TRANSACTION_setStorageEncryption = 40;
        static final int TRANSACTION_setSystemSetting = 151;
        static final int TRANSACTION_setSystemUpdatePolicy = 181;
        static final int TRANSACTION_setTime = 153;
        static final int TRANSACTION_setTimeZone = 154;
        static final int TRANSACTION_setTrustAgentConfiguration = 170;
        static final int TRANSACTION_setUninstallBlocked = 158;
        static final int TRANSACTION_setUserIcon = 180;
        static final int TRANSACTION_setUserProvisioningState = 217;
        static final int TRANSACTION_setUserRestriction = 114;
        static final int TRANSACTION_startManagedQuickContact = 166;
        static final int TRANSACTION_startUserInBackground = 135;
        static final int TRANSACTION_startViewCalendarEventInManagedProfile = 280;
        static final int TRANSACTION_stopUser = 136;
        static final int TRANSACTION_switchUser = 134;
        static final int TRANSACTION_transferOwnership = 254;
        static final int TRANSACTION_uninstallCaCerts = 86;
        static final int TRANSACTION_uninstallPackageWithActiveAdmins = 228;
        static final int TRANSACTION_updateOverrideApn = 263;
        static final int TRANSACTION_wipeDataWithReason = 36;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

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

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setPasswordQuality";
                case 2:
                    return "getPasswordQuality";
                case 3:
                    return "setPasswordMinimumLength";
                case 4:
                    return "getPasswordMinimumLength";
                case 5:
                    return "setPasswordMinimumUpperCase";
                case 6:
                    return "getPasswordMinimumUpperCase";
                case 7:
                    return "setPasswordMinimumLowerCase";
                case 8:
                    return "getPasswordMinimumLowerCase";
                case 9:
                    return "setPasswordMinimumLetters";
                case 10:
                    return "getPasswordMinimumLetters";
                case 11:
                    return "setPasswordMinimumNumeric";
                case 12:
                    return "getPasswordMinimumNumeric";
                case 13:
                    return "setPasswordMinimumSymbols";
                case 14:
                    return "getPasswordMinimumSymbols";
                case 15:
                    return "setPasswordMinimumNonLetter";
                case 16:
                    return "getPasswordMinimumNonLetter";
                case 17:
                    return "setPasswordHistoryLength";
                case 18:
                    return "getPasswordHistoryLength";
                case 19:
                    return "setPasswordExpirationTimeout";
                case 20:
                    return "getPasswordExpirationTimeout";
                case 21:
                    return "getPasswordExpiration";
                case 22:
                    return "isActivePasswordSufficient";
                case 23:
                    return "isProfileActivePasswordSufficientForParent";
                case 24:
                    return "getPasswordComplexity";
                case 25:
                    return "isUsingUnifiedPassword";
                case 26:
                    return "getCurrentFailedPasswordAttempts";
                case 27:
                    return "getProfileWithMinimumFailedPasswordsForWipe";
                case 28:
                    return "setMaximumFailedPasswordsForWipe";
                case 29:
                    return "getMaximumFailedPasswordsForWipe";
                case 30:
                    return "resetPassword";
                case 31:
                    return "setMaximumTimeToLock";
                case 32:
                    return "getMaximumTimeToLock";
                case 33:
                    return "setRequiredStrongAuthTimeout";
                case 34:
                    return "getRequiredStrongAuthTimeout";
                case 35:
                    return "lockNow";
                case 36:
                    return "wipeDataWithReason";
                case 37:
                    return "setGlobalProxy";
                case 38:
                    return "getGlobalProxyAdmin";
                case 39:
                    return "setRecommendedGlobalProxy";
                case 40:
                    return "setStorageEncryption";
                case 41:
                    return "getStorageEncryption";
                case 42:
                    return "getStorageEncryptionStatus";
                case 43:
                    return "requestBugreport";
                case 44:
                    return "setCameraDisabled";
                case 45:
                    return "getCameraDisabled";
                case 46:
                    return "setScreenCaptureDisabled";
                case 47:
                    return "getScreenCaptureDisabled";
                case 48:
                    return "setKeyguardDisabledFeatures";
                case 49:
                    return "getKeyguardDisabledFeatures";
                case 50:
                    return "setActiveAdmin";
                case 51:
                    return "isAdminActive";
                case 52:
                    return "getActiveAdmins";
                case 53:
                    return "packageHasActiveAdmins";
                case 54:
                    return "getRemoveWarning";
                case 55:
                    return "removeActiveAdmin";
                case 56:
                    return "forceRemoveActiveAdmin";
                case 57:
                    return "hasGrantedPolicy";
                case 58:
                    return "setActivePasswordState";
                case 59:
                    return "reportPasswordChanged";
                case 60:
                    return "reportFailedPasswordAttempt";
                case 61:
                    return "reportSuccessfulPasswordAttempt";
                case 62:
                    return "reportFailedBiometricAttempt";
                case 63:
                    return "reportSuccessfulBiometricAttempt";
                case 64:
                    return "reportKeyguardDismissed";
                case 65:
                    return "reportKeyguardSecured";
                case 66:
                    return "setDeviceOwner";
                case 67:
                    return "getDeviceOwnerComponent";
                case 68:
                    return "hasDeviceOwner";
                case 69:
                    return "getDeviceOwnerName";
                case 70:
                    return "clearDeviceOwner";
                case 71:
                    return "getDeviceOwnerUserId";
                case 72:
                    return "setProfileOwner";
                case 73:
                    return "getProfileOwnerAsUser";
                case 74:
                    return "getProfileOwner";
                case 75:
                    return "getProfileOwnerName";
                case 76:
                    return "setProfileEnabled";
                case 77:
                    return "setProfileName";
                case 78:
                    return "clearProfileOwner";
                case 79:
                    return "hasUserSetupCompleted";
                case 80:
                    return "checkDeviceIdentifierAccess";
                case 81:
                    return "setDeviceOwnerLockScreenInfo";
                case 82:
                    return "getDeviceOwnerLockScreenInfo";
                case 83:
                    return "setPackagesSuspended";
                case 84:
                    return "isPackageSuspended";
                case 85:
                    return "installCaCert";
                case 86:
                    return "uninstallCaCerts";
                case 87:
                    return "enforceCanManageCaCerts";
                case 88:
                    return "approveCaCert";
                case 89:
                    return "isCaCertApproved";
                case 90:
                    return "installKeyPair";
                case 91:
                    return "removeKeyPair";
                case 92:
                    return "generateKeyPair";
                case 93:
                    return "setKeyPairCertificate";
                case 94:
                    return "choosePrivateKeyAlias";
                case 95:
                    return "setDelegatedScopes";
                case 96:
                    return "getDelegatedScopes";
                case 97:
                    return "getDelegatePackages";
                case 98:
                    return "setCertInstallerPackage";
                case 99:
                    return "getCertInstallerPackage";
                case 100:
                    return "setAlwaysOnVpnPackage";
                case 101:
                    return "getAlwaysOnVpnPackage";
                case 102:
                    return "isAlwaysOnVpnLockdownEnabled";
                case 103:
                    return "getAlwaysOnVpnLockdownWhitelist";
                case 104:
                    return "addPersistentPreferredActivity";
                case 105:
                    return "clearPackagePersistentPreferredActivities";
                case 106:
                    return "setDefaultSmsApplication";
                case 107:
                    return "setApplicationRestrictions";
                case 108:
                    return "getApplicationRestrictions";
                case 109:
                    return "setApplicationRestrictionsManagingPackage";
                case 110:
                    return "getApplicationRestrictionsManagingPackage";
                case 111:
                    return "isCallerApplicationRestrictionsManagingPackage";
                case 112:
                    return "setRestrictionsProvider";
                case 113:
                    return "getRestrictionsProvider";
                case 114:
                    return "setUserRestriction";
                case 115:
                    return "getUserRestrictions";
                case 116:
                    return "addCrossProfileIntentFilter";
                case 117:
                    return "clearCrossProfileIntentFilters";
                case 118:
                    return "setPermittedAccessibilityServices";
                case 119:
                    return "getPermittedAccessibilityServices";
                case 120:
                    return "getPermittedAccessibilityServicesForUser";
                case 121:
                    return "isAccessibilityServicePermittedByAdmin";
                case 122:
                    return "setPermittedInputMethods";
                case 123:
                    return "getPermittedInputMethods";
                case 124:
                    return "getPermittedInputMethodsForCurrentUser";
                case 125:
                    return "isInputMethodPermittedByAdmin";
                case 126:
                    return "setPermittedCrossProfileNotificationListeners";
                case 127:
                    return "getPermittedCrossProfileNotificationListeners";
                case 128:
                    return "isNotificationListenerServicePermitted";
                case 129:
                    return "createAdminSupportIntent";
                case 130:
                    return "setApplicationHidden";
                case 131:
                    return "isApplicationHidden";
                case 132:
                    return "createAndManageUser";
                case 133:
                    return "removeUser";
                case 134:
                    return "switchUser";
                case 135:
                    return "startUserInBackground";
                case 136:
                    return "stopUser";
                case 137:
                    return "logoutUser";
                case 138:
                    return "getSecondaryUsers";
                case 139:
                    return "enableSystemApp";
                case 140:
                    return "enableSystemAppWithIntent";
                case 141:
                    return "installExistingPackage";
                case 142:
                    return "setAccountManagementDisabled";
                case 143:
                    return "getAccountTypesWithManagementDisabled";
                case 144:
                    return "getAccountTypesWithManagementDisabledAsUser";
                case 145:
                    return "setLockTaskPackages";
                case 146:
                    return "getLockTaskPackages";
                case 147:
                    return "isLockTaskPermitted";
                case 148:
                    return "setLockTaskFeatures";
                case 149:
                    return "getLockTaskFeatures";
                case 150:
                    return "setGlobalSetting";
                case 151:
                    return "setSystemSetting";
                case 152:
                    return "setSecureSetting";
                case 153:
                    return "setTime";
                case 154:
                    return "setTimeZone";
                case 155:
                    return "setMasterVolumeMuted";
                case 156:
                    return "isMasterVolumeMuted";
                case 157:
                    return "notifyLockTaskModeChanged";
                case 158:
                    return "setUninstallBlocked";
                case 159:
                    return "isUninstallBlocked";
                case 160:
                    return "setCrossProfileCallerIdDisabled";
                case 161:
                    return "getCrossProfileCallerIdDisabled";
                case 162:
                    return "getCrossProfileCallerIdDisabledForUser";
                case 163:
                    return "setCrossProfileContactsSearchDisabled";
                case 164:
                    return "getCrossProfileContactsSearchDisabled";
                case 165:
                    return "getCrossProfileContactsSearchDisabledForUser";
                case 166:
                    return "startManagedQuickContact";
                case 167:
                    return "setBluetoothContactSharingDisabled";
                case 168:
                    return "getBluetoothContactSharingDisabled";
                case 169:
                    return "getBluetoothContactSharingDisabledForUser";
                case 170:
                    return "setTrustAgentConfiguration";
                case 171:
                    return "getTrustAgentConfiguration";
                case 172:
                    return "addCrossProfileWidgetProvider";
                case 173:
                    return "removeCrossProfileWidgetProvider";
                case 174:
                    return "getCrossProfileWidgetProviders";
                case 175:
                    return "setAutoTimeRequired";
                case 176:
                    return "getAutoTimeRequired";
                case 177:
                    return "setForceEphemeralUsers";
                case 178:
                    return "getForceEphemeralUsers";
                case 179:
                    return "isRemovingAdmin";
                case 180:
                    return "setUserIcon";
                case 181:
                    return "setSystemUpdatePolicy";
                case 182:
                    return "getSystemUpdatePolicy";
                case 183:
                    return "clearSystemUpdatePolicyFreezePeriodRecord";
                case 184:
                    return "setKeyguardDisabled";
                case 185:
                    return "setStatusBarDisabled";
                case 186:
                    return "getDoNotAskCredentialsOnBoot";
                case 187:
                    return "notifyPendingSystemUpdate";
                case 188:
                    return "getPendingSystemUpdate";
                case 189:
                    return "setPermissionPolicy";
                case 190:
                    return "getPermissionPolicy";
                case 191:
                    return "setPermissionGrantState";
                case 192:
                    return "getPermissionGrantState";
                case 193:
                    return "isProvisioningAllowed";
                case 194:
                    return "checkProvisioningPreCondition";
                case 195:
                    return "setKeepUninstalledPackages";
                case 196:
                    return "getKeepUninstalledPackages";
                case 197:
                    return "isManagedProfile";
                case 198:
                    return "isSystemOnlyUser";
                case 199:
                    return "getWifiMacAddress";
                case 200:
                    return "reboot";
                case 201:
                    return "setShortSupportMessage";
                case 202:
                    return "getShortSupportMessage";
                case 203:
                    return "setLongSupportMessage";
                case 204:
                    return "getLongSupportMessage";
                case 205:
                    return "getShortSupportMessageForUser";
                case 206:
                    return "getLongSupportMessageForUser";
                case 207:
                    return "isSeparateProfileChallengeAllowed";
                case 208:
                    return "setOrganizationColor";
                case 209:
                    return "setOrganizationColorForUser";
                case 210:
                    return "getOrganizationColor";
                case 211:
                    return "getOrganizationColorForUser";
                case 212:
                    return "setOrganizationName";
                case 213:
                    return "getOrganizationName";
                case 214:
                    return "getDeviceOwnerOrganizationName";
                case 215:
                    return "getOrganizationNameForUser";
                case 216:
                    return "getUserProvisioningState";
                case 217:
                    return "setUserProvisioningState";
                case 218:
                    return "setAffiliationIds";
                case 219:
                    return "getAffiliationIds";
                case 220:
                    return "isAffiliatedUser";
                case 221:
                    return "setSecurityLoggingEnabled";
                case 222:
                    return "isSecurityLoggingEnabled";
                case 223:
                    return "retrieveSecurityLogs";
                case 224:
                    return "retrievePreRebootSecurityLogs";
                case 225:
                    return "forceNetworkLogs";
                case 226:
                    return "forceSecurityLogs";
                case 227:
                    return "isUninstallInQueue";
                case 228:
                    return "uninstallPackageWithActiveAdmins";
                case 229:
                    return "isDeviceProvisioned";
                case 230:
                    return "isDeviceProvisioningConfigApplied";
                case 231:
                    return "setDeviceProvisioningConfigApplied";
                case 232:
                    return "forceUpdateUserSetupComplete";
                case 233:
                    return "setBackupServiceEnabled";
                case 234:
                    return "isBackupServiceEnabled";
                case 235:
                    return "setNetworkLoggingEnabled";
                case 236:
                    return "isNetworkLoggingEnabled";
                case 237:
                    return "retrieveNetworkLogs";
                case 238:
                    return "bindDeviceAdminServiceAsUser";
                case 239:
                    return "getBindDeviceAdminTargetUsers";
                case 240:
                    return "isEphemeralUser";
                case 241:
                    return "getLastSecurityLogRetrievalTime";
                case 242:
                    return "getLastBugReportRequestTime";
                case 243:
                    return "getLastNetworkLogRetrievalTime";
                case 244:
                    return "setResetPasswordToken";
                case 245:
                    return "clearResetPasswordToken";
                case 246:
                    return "isResetPasswordTokenActive";
                case 247:
                    return "resetPasswordWithToken";
                case 248:
                    return "isCurrentInputMethodSetByOwner";
                case 249:
                    return "getOwnerInstalledCaCerts";
                case 250:
                    return "clearApplicationUserData";
                case 251:
                    return "setLogoutEnabled";
                case 252:
                    return "isLogoutEnabled";
                case 253:
                    return "getDisallowedSystemApps";
                case 254:
                    return "transferOwnership";
                case 255:
                    return "getTransferOwnershipBundle";
                case 256:
                    return "setStartUserSessionMessage";
                case 257:
                    return "setEndUserSessionMessage";
                case 258:
                    return "getStartUserSessionMessage";
                case 259:
                    return "getEndUserSessionMessage";
                case 260:
                    return "setMeteredDataDisabledPackages";
                case 261:
                    return "getMeteredDataDisabledPackages";
                case 262:
                    return "addOverrideApn";
                case 263:
                    return "updateOverrideApn";
                case 264:
                    return "removeOverrideApn";
                case 265:
                    return "getOverrideApns";
                case 266:
                    return "setOverrideApnsEnabled";
                case 267:
                    return "isOverrideApnEnabled";
                case 268:
                    return "isMeteredDataDisabledPackageForUser";
                case 269:
                    return "setGlobalPrivateDns";
                case 270:
                    return "getGlobalPrivateDnsMode";
                case 271:
                    return "getGlobalPrivateDnsHost";
                case 272:
                    return "grantDeviceIdsAccessToProfileOwner";
                case 273:
                    return "installUpdateFromFile";
                case 274:
                    return "setCrossProfileCalendarPackages";
                case 275:
                    return "getCrossProfileCalendarPackages";
                case 276:
                    return "isPackageAllowedToAccessCalendarForUser";
                case 277:
                    return "getCrossProfileCalendarPackagesForUser";
                case 278:
                    return "isManagedKiosk";
                case 279:
                    return "isUnattendedManagedKiosk";
                case 280:
                    return "startViewCalendarEventInManagedProfile";
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
            ComponentName _arg0;
            ComponentName _arg02;
            ComponentName _arg03;
            ComponentName _arg04;
            ComponentName _arg05;
            ComponentName _arg06;
            ComponentName _arg07;
            ComponentName _arg08;
            ComponentName _arg09;
            ComponentName _arg010;
            ComponentName _arg011;
            ComponentName _arg012;
            ComponentName _arg013;
            ComponentName _arg014;
            ComponentName _arg015;
            ComponentName _arg016;
            ComponentName _arg017;
            ComponentName _arg018;
            ComponentName _arg019;
            ComponentName _arg020;
            ComponentName _arg021;
            ComponentName _arg022;
            ComponentName _arg023;
            ComponentName _arg024;
            ComponentName _arg025;
            ComponentName _arg026;
            ComponentName _arg027;
            ComponentName _arg028;
            ComponentName _arg029;
            ComponentName _arg030;
            ProxyInfo _arg1;
            ComponentName _arg031;
            ComponentName _arg032;
            ComponentName _arg033;
            ComponentName _arg034;
            ComponentName _arg035;
            ComponentName _arg036;
            ComponentName _arg037;
            ComponentName _arg038;
            ComponentName _arg039;
            ComponentName _arg040;
            ComponentName _arg041;
            ComponentName _arg042;
            RemoteCallback _arg12;
            ComponentName _arg043;
            ComponentName _arg044;
            ComponentName _arg045;
            PasswordMetrics _arg046;
            ComponentName _arg047;
            ComponentName _arg048;
            ComponentName _arg049;
            ComponentName _arg050;
            ComponentName _arg051;
            ComponentName _arg052;
            CharSequence _arg13;
            ComponentName _arg053;
            ComponentName _arg054;
            ComponentName _arg055;
            ComponentName _arg056;
            ComponentName _arg057;
            ComponentName _arg058;
            ComponentName _arg059;
            ParcelableKeyGenParameterSpec _arg3;
            Uri _arg14;
            ComponentName _arg060;
            ComponentName _arg061;
            ComponentName _arg062;
            ComponentName _arg063;
            ComponentName _arg064;
            ComponentName _arg065;
            ComponentName _arg066;
            ComponentName _arg067;
            ComponentName _arg068;
            ComponentName _arg069;
            IntentFilter _arg15;
            ComponentName _arg2;
            ComponentName _arg070;
            ComponentName _arg071;
            ComponentName _arg072;
            Bundle _arg32;
            ComponentName _arg073;
            ComponentName _arg074;
            ComponentName _arg075;
            ComponentName _arg076;
            ComponentName _arg16;
            ComponentName _arg077;
            ComponentName _arg078;
            ComponentName _arg079;
            IntentFilter _arg17;
            ComponentName _arg080;
            ComponentName _arg081;
            ComponentName _arg082;
            ComponentName _arg083;
            ComponentName _arg084;
            ComponentName _arg085;
            ComponentName _arg086;
            ComponentName _arg087;
            ComponentName _arg088;
            ComponentName _arg089;
            ComponentName _arg090;
            ComponentName _arg091;
            ComponentName _arg22;
            PersistableBundle _arg33;
            ComponentName _arg092;
            UserHandle _arg18;
            ComponentName _arg093;
            UserHandle _arg19;
            ComponentName _arg094;
            UserHandle _arg110;
            ComponentName _arg095;
            UserHandle _arg111;
            ComponentName _arg096;
            ComponentName _arg097;
            ComponentName _arg098;
            ComponentName _arg099;
            Intent _arg23;
            ComponentName _arg0100;
            ComponentName _arg0101;
            ComponentName _arg0102;
            ComponentName _arg0103;
            ComponentName _arg0104;
            ComponentName _arg0105;
            ComponentName _arg0106;
            ComponentName _arg0107;
            ComponentName _arg0108;
            ComponentName _arg0109;
            ComponentName _arg0110;
            ComponentName _arg0111;
            ComponentName _arg0112;
            ComponentName _arg0113;
            ComponentName _arg0114;
            ComponentName _arg0115;
            ComponentName _arg0116;
            ComponentName _arg0117;
            ComponentName _arg0118;
            Intent _arg4;
            ComponentName _arg0119;
            ComponentName _arg0120;
            ComponentName _arg0121;
            ComponentName _arg112;
            PersistableBundle _arg24;
            ComponentName _arg0122;
            ComponentName _arg113;
            ComponentName _arg0123;
            ComponentName _arg0124;
            ComponentName _arg0125;
            ComponentName _arg0126;
            ComponentName _arg0127;
            ComponentName _arg0128;
            ComponentName _arg0129;
            ComponentName _arg0130;
            Bitmap _arg114;
            ComponentName _arg0131;
            SystemUpdatePolicy _arg115;
            ComponentName _arg0132;
            ComponentName _arg0133;
            SystemUpdateInfo _arg0134;
            ComponentName _arg0135;
            ComponentName _arg0136;
            ComponentName _arg0137;
            ComponentName _arg0138;
            ComponentName _arg0139;
            ComponentName _arg0140;
            ComponentName _arg0141;
            ComponentName _arg0142;
            ComponentName _arg0143;
            ComponentName _arg0144;
            ComponentName _arg0145;
            CharSequence _arg116;
            ComponentName _arg0146;
            ComponentName _arg0147;
            CharSequence _arg117;
            ComponentName _arg0148;
            ComponentName _arg0149;
            ComponentName _arg0150;
            ComponentName _arg0151;
            ComponentName _arg0152;
            ComponentName _arg0153;
            CharSequence _arg118;
            ComponentName _arg0154;
            ComponentName _arg0155;
            ComponentName _arg0156;
            ComponentName _arg0157;
            ComponentName _arg0158;
            ComponentName _arg0159;
            ComponentName _arg0160;
            ComponentName _arg0161;
            ComponentName _arg0162;
            ComponentName _arg0163;
            ComponentName _arg0164;
            ComponentName _arg0165;
            ComponentName _arg0166;
            ComponentName _arg0167;
            ComponentName _arg0168;
            ComponentName _arg0169;
            ComponentName _arg0170;
            ComponentName _arg0171;
            UserHandle _arg0172;
            ComponentName _arg0173;
            ComponentName _arg0174;
            ComponentName _arg0175;
            ComponentName _arg0176;
            ComponentName _arg119;
            PersistableBundle _arg25;
            ComponentName _arg0177;
            CharSequence _arg120;
            ComponentName _arg0178;
            CharSequence _arg121;
            ComponentName _arg0179;
            ComponentName _arg0180;
            ComponentName _arg0181;
            ComponentName _arg0182;
            ComponentName _arg0183;
            ApnSetting _arg122;
            ComponentName _arg0184;
            ApnSetting _arg26;
            ComponentName _arg0185;
            ComponentName _arg0186;
            ComponentName _arg0187;
            ComponentName _arg0188;
            ComponentName _arg0189;
            ComponentName _arg0190;
            ComponentName _arg0191;
            ComponentName _arg0192;
            ComponentName _arg0193;
            ComponentName _arg0194;
            ParcelFileDescriptor _arg123;
            ComponentName _arg0195;
            ComponentName _arg0196;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    int _arg124 = data.readInt();
                    boolean _arg27 = data.readInt() != 0;
                    setPasswordQuality(_arg0, _arg124, _arg27);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    int _arg125 = data.readInt();
                    boolean _arg28 = data.readInt() != 0;
                    int _result = getPasswordQuality(_arg02, _arg125, _arg28);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    int _arg126 = data.readInt();
                    boolean _arg29 = data.readInt() != 0;
                    setPasswordMinimumLength(_arg03, _arg126, _arg29);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    int _arg127 = data.readInt();
                    boolean _arg210 = data.readInt() != 0;
                    int _result2 = getPasswordMinimumLength(_arg04, _arg127, _arg210);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    int _arg128 = data.readInt();
                    boolean _arg211 = data.readInt() != 0;
                    setPasswordMinimumUpperCase(_arg05, _arg128, _arg211);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    int _arg129 = data.readInt();
                    boolean _arg212 = data.readInt() != 0;
                    int _result3 = getPasswordMinimumUpperCase(_arg06, _arg129, _arg212);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    int _arg130 = data.readInt();
                    boolean _arg213 = data.readInt() != 0;
                    setPasswordMinimumLowerCase(_arg07, _arg130, _arg213);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    int _arg131 = data.readInt();
                    boolean _arg214 = data.readInt() != 0;
                    int _result4 = getPasswordMinimumLowerCase(_arg08, _arg131, _arg214);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg09 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg09 = null;
                    }
                    int _arg132 = data.readInt();
                    boolean _arg215 = data.readInt() != 0;
                    setPasswordMinimumLetters(_arg09, _arg132, _arg215);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg010 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg010 = null;
                    }
                    int _arg133 = data.readInt();
                    boolean _arg216 = data.readInt() != 0;
                    int _result5 = getPasswordMinimumLetters(_arg010, _arg133, _arg216);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg011 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg011 = null;
                    }
                    int _arg134 = data.readInt();
                    boolean _arg217 = data.readInt() != 0;
                    setPasswordMinimumNumeric(_arg011, _arg134, _arg217);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg012 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg012 = null;
                    }
                    int _arg135 = data.readInt();
                    boolean _arg218 = data.readInt() != 0;
                    int _result6 = getPasswordMinimumNumeric(_arg012, _arg135, _arg218);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg013 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg013 = null;
                    }
                    int _arg136 = data.readInt();
                    boolean _arg219 = data.readInt() != 0;
                    setPasswordMinimumSymbols(_arg013, _arg136, _arg219);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg014 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg014 = null;
                    }
                    int _arg137 = data.readInt();
                    boolean _arg220 = data.readInt() != 0;
                    int _result7 = getPasswordMinimumSymbols(_arg014, _arg137, _arg220);
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg015 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg015 = null;
                    }
                    int _arg138 = data.readInt();
                    boolean _arg221 = data.readInt() != 0;
                    setPasswordMinimumNonLetter(_arg015, _arg138, _arg221);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg016 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg016 = null;
                    }
                    int _arg139 = data.readInt();
                    boolean _arg222 = data.readInt() != 0;
                    int _result8 = getPasswordMinimumNonLetter(_arg016, _arg139, _arg222);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg017 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg017 = null;
                    }
                    int _arg140 = data.readInt();
                    boolean _arg223 = data.readInt() != 0;
                    setPasswordHistoryLength(_arg017, _arg140, _arg223);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg018 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg018 = null;
                    }
                    int _arg141 = data.readInt();
                    boolean _arg224 = data.readInt() != 0;
                    int _result9 = getPasswordHistoryLength(_arg018, _arg141, _arg224);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg019 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg019 = null;
                    }
                    long _arg142 = data.readLong();
                    boolean _arg225 = data.readInt() != 0;
                    setPasswordExpirationTimeout(_arg019, _arg142, _arg225);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg020 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg020 = null;
                    }
                    int _arg143 = data.readInt();
                    boolean _arg226 = data.readInt() != 0;
                    long _result10 = getPasswordExpirationTimeout(_arg020, _arg143, _arg226);
                    reply.writeNoException();
                    reply.writeLong(_result10);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg021 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg021 = null;
                    }
                    int _arg144 = data.readInt();
                    boolean _arg227 = data.readInt() != 0;
                    long _result11 = getPasswordExpiration(_arg021, _arg144, _arg227);
                    reply.writeNoException();
                    reply.writeLong(_result11);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0197 = data.readInt();
                    boolean _arg145 = data.readInt() != 0;
                    boolean isActivePasswordSufficient = isActivePasswordSufficient(_arg0197, _arg145);
                    reply.writeNoException();
                    reply.writeInt(isActivePasswordSufficient ? 1 : 0);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0198 = data.readInt();
                    boolean isProfileActivePasswordSufficientForParent = isProfileActivePasswordSufficientForParent(_arg0198);
                    reply.writeNoException();
                    reply.writeInt(isProfileActivePasswordSufficientForParent ? 1 : 0);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    int _result12 = getPasswordComplexity();
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg022 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg022 = null;
                    }
                    boolean isUsingUnifiedPassword = isUsingUnifiedPassword(_arg022);
                    reply.writeNoException();
                    reply.writeInt(isUsingUnifiedPassword ? 1 : 0);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0199 = data.readInt();
                    boolean _arg146 = data.readInt() != 0;
                    int _result13 = getCurrentFailedPasswordAttempts(_arg0199, _arg146);
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0200 = data.readInt();
                    boolean _arg147 = data.readInt() != 0;
                    int _result14 = getProfileWithMinimumFailedPasswordsForWipe(_arg0200, _arg147);
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg023 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg023 = null;
                    }
                    int _arg148 = data.readInt();
                    boolean _arg228 = data.readInt() != 0;
                    setMaximumFailedPasswordsForWipe(_arg023, _arg148, _arg228);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg024 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg024 = null;
                    }
                    int _arg149 = data.readInt();
                    boolean _arg229 = data.readInt() != 0;
                    int _result15 = getMaximumFailedPasswordsForWipe(_arg024, _arg149, _arg229);
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0201 = data.readString();
                    int _arg150 = data.readInt();
                    boolean resetPassword = resetPassword(_arg0201, _arg150);
                    reply.writeNoException();
                    reply.writeInt(resetPassword ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg025 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg025 = null;
                    }
                    long _arg151 = data.readLong();
                    boolean _arg230 = data.readInt() != 0;
                    setMaximumTimeToLock(_arg025, _arg151, _arg230);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg026 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg026 = null;
                    }
                    int _arg152 = data.readInt();
                    boolean _arg231 = data.readInt() != 0;
                    long _result16 = getMaximumTimeToLock(_arg026, _arg152, _arg231);
                    reply.writeNoException();
                    reply.writeLong(_result16);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg027 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg027 = null;
                    }
                    long _arg153 = data.readLong();
                    boolean _arg232 = data.readInt() != 0;
                    setRequiredStrongAuthTimeout(_arg027, _arg153, _arg232);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg028 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg028 = null;
                    }
                    int _arg154 = data.readInt();
                    boolean _arg233 = data.readInt() != 0;
                    long _result17 = getRequiredStrongAuthTimeout(_arg028, _arg154, _arg233);
                    reply.writeNoException();
                    reply.writeLong(_result17);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0202 = data.readInt();
                    boolean _arg155 = data.readInt() != 0;
                    lockNow(_arg0202, _arg155);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0203 = data.readInt();
                    String _arg156 = data.readString();
                    wipeDataWithReason(_arg0203, _arg156);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg029 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg029 = null;
                    }
                    String _arg157 = data.readString();
                    String _arg234 = data.readString();
                    ComponentName _result18 = setGlobalProxy(_arg029, _arg157, _arg234);
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
                    int _arg0204 = data.readInt();
                    ComponentName _result19 = getGlobalProxyAdmin(_arg0204);
                    reply.writeNoException();
                    if (_result19 != null) {
                        reply.writeInt(1);
                        _result19.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg030 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg030 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = ProxyInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    setRecommendedGlobalProxy(_arg030, _arg1);
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg031 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg031 = null;
                    }
                    boolean _arg158 = data.readInt() != 0;
                    int _result20 = setStorageEncryption(_arg031, _arg158);
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg032 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg032 = null;
                    }
                    int _arg159 = data.readInt();
                    boolean storageEncryption = getStorageEncryption(_arg032, _arg159);
                    reply.writeNoException();
                    reply.writeInt(storageEncryption ? 1 : 0);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0205 = data.readString();
                    int _arg160 = data.readInt();
                    int _result21 = getStorageEncryptionStatus(_arg0205, _arg160);
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg033 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg033 = null;
                    }
                    boolean requestBugreport = requestBugreport(_arg033);
                    reply.writeNoException();
                    reply.writeInt(requestBugreport ? 1 : 0);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg034 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg034 = null;
                    }
                    boolean _arg161 = data.readInt() != 0;
                    setCameraDisabled(_arg034, _arg161);
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg035 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg035 = null;
                    }
                    int _arg162 = data.readInt();
                    boolean cameraDisabled = getCameraDisabled(_arg035, _arg162);
                    reply.writeNoException();
                    reply.writeInt(cameraDisabled ? 1 : 0);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg036 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg036 = null;
                    }
                    boolean _arg163 = data.readInt() != 0;
                    setScreenCaptureDisabled(_arg036, _arg163);
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg037 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg037 = null;
                    }
                    int _arg164 = data.readInt();
                    boolean screenCaptureDisabled = getScreenCaptureDisabled(_arg037, _arg164);
                    reply.writeNoException();
                    reply.writeInt(screenCaptureDisabled ? 1 : 0);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg038 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg038 = null;
                    }
                    int _arg165 = data.readInt();
                    boolean _arg235 = data.readInt() != 0;
                    setKeyguardDisabledFeatures(_arg038, _arg165, _arg235);
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg039 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg039 = null;
                    }
                    int _arg166 = data.readInt();
                    boolean _arg236 = data.readInt() != 0;
                    int _result22 = getKeyguardDisabledFeatures(_arg039, _arg166, _arg236);
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg040 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg040 = null;
                    }
                    boolean _arg167 = data.readInt() != 0;
                    int _arg237 = data.readInt();
                    setActiveAdmin(_arg040, _arg167, _arg237);
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg041 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg041 = null;
                    }
                    int _arg168 = data.readInt();
                    boolean isAdminActive = isAdminActive(_arg041, _arg168);
                    reply.writeNoException();
                    reply.writeInt(isAdminActive ? 1 : 0);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0206 = data.readInt();
                    List<ComponentName> _result23 = getActiveAdmins(_arg0206);
                    reply.writeNoException();
                    reply.writeTypedList(_result23);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0207 = data.readString();
                    int _arg169 = data.readInt();
                    boolean packageHasActiveAdmins = packageHasActiveAdmins(_arg0207, _arg169);
                    reply.writeNoException();
                    reply.writeInt(packageHasActiveAdmins ? 1 : 0);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg042 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg042 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg12 = RemoteCallback.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    int _arg238 = data.readInt();
                    getRemoveWarning(_arg042, _arg12, _arg238);
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg043 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg043 = null;
                    }
                    int _arg170 = data.readInt();
                    removeActiveAdmin(_arg043, _arg170);
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg044 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg044 = null;
                    }
                    int _arg171 = data.readInt();
                    forceRemoveActiveAdmin(_arg044, _arg171);
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg045 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg045 = null;
                    }
                    int _arg172 = data.readInt();
                    int _arg239 = data.readInt();
                    boolean hasGrantedPolicy = hasGrantedPolicy(_arg045, _arg172, _arg239);
                    reply.writeNoException();
                    reply.writeInt(hasGrantedPolicy ? 1 : 0);
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg046 = PasswordMetrics.CREATOR.createFromParcel(data);
                    } else {
                        _arg046 = null;
                    }
                    int _arg173 = data.readInt();
                    setActivePasswordState(_arg046, _arg173);
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0208 = data.readInt();
                    reportPasswordChanged(_arg0208);
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0209 = data.readInt();
                    reportFailedPasswordAttempt(_arg0209);
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0210 = data.readInt();
                    reportSuccessfulPasswordAttempt(_arg0210);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0211 = data.readInt();
                    reportFailedBiometricAttempt(_arg0211);
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0212 = data.readInt();
                    reportSuccessfulBiometricAttempt(_arg0212);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0213 = data.readInt();
                    reportKeyguardDismissed(_arg0213);
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0214 = data.readInt();
                    reportKeyguardSecured(_arg0214);
                    reply.writeNoException();
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg047 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg047 = null;
                    }
                    String _arg174 = data.readString();
                    int _arg240 = data.readInt();
                    boolean deviceOwner = setDeviceOwner(_arg047, _arg174, _arg240);
                    reply.writeNoException();
                    reply.writeInt(deviceOwner ? 1 : 0);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg0215 = data.readInt() != 0;
                    ComponentName _result24 = getDeviceOwnerComponent(_arg0215);
                    reply.writeNoException();
                    if (_result24 != null) {
                        reply.writeInt(1);
                        _result24.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasDeviceOwner = hasDeviceOwner();
                    reply.writeNoException();
                    reply.writeInt(hasDeviceOwner ? 1 : 0);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    String _result25 = getDeviceOwnerName();
                    reply.writeNoException();
                    reply.writeString(_result25);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0216 = data.readString();
                    clearDeviceOwner(_arg0216);
                    reply.writeNoException();
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    int _result26 = getDeviceOwnerUserId();
                    reply.writeNoException();
                    reply.writeInt(_result26);
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg048 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg048 = null;
                    }
                    String _arg175 = data.readString();
                    int _arg241 = data.readInt();
                    boolean profileOwner = setProfileOwner(_arg048, _arg175, _arg241);
                    reply.writeNoException();
                    reply.writeInt(profileOwner ? 1 : 0);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0217 = data.readInt();
                    ComponentName _result27 = getProfileOwnerAsUser(_arg0217);
                    reply.writeNoException();
                    if (_result27 != null) {
                        reply.writeInt(1);
                        _result27.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0218 = data.readInt();
                    ComponentName _result28 = getProfileOwner(_arg0218);
                    reply.writeNoException();
                    if (_result28 != null) {
                        reply.writeInt(1);
                        _result28.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0219 = data.readInt();
                    String _result29 = getProfileOwnerName(_arg0219);
                    reply.writeNoException();
                    reply.writeString(_result29);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg049 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg049 = null;
                    }
                    setProfileEnabled(_arg049);
                    reply.writeNoException();
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg050 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg050 = null;
                    }
                    String _arg176 = data.readString();
                    setProfileName(_arg050, _arg176);
                    reply.writeNoException();
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg051 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg051 = null;
                    }
                    clearProfileOwner(_arg051);
                    reply.writeNoException();
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasUserSetupCompleted = hasUserSetupCompleted();
                    reply.writeNoException();
                    reply.writeInt(hasUserSetupCompleted ? 1 : 0);
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0220 = data.readString();
                    int _arg177 = data.readInt();
                    int _arg242 = data.readInt();
                    boolean checkDeviceIdentifierAccess = checkDeviceIdentifierAccess(_arg0220, _arg177, _arg242);
                    reply.writeNoException();
                    reply.writeInt(checkDeviceIdentifierAccess ? 1 : 0);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg052 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg052 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    setDeviceOwnerLockScreenInfo(_arg052, _arg13);
                    reply.writeNoException();
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    CharSequence _result30 = getDeviceOwnerLockScreenInfo();
                    reply.writeNoException();
                    if (_result30 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result30, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg053 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg053 = null;
                    }
                    String _arg178 = data.readString();
                    String[] _arg243 = data.createStringArray();
                    boolean _arg34 = data.readInt() != 0;
                    String[] _result31 = setPackagesSuspended(_arg053, _arg178, _arg243, _arg34);
                    reply.writeNoException();
                    reply.writeStringArray(_result31);
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg054 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg054 = null;
                    }
                    String _arg179 = data.readString();
                    String _arg244 = data.readString();
                    boolean isPackageSuspended = isPackageSuspended(_arg054, _arg179, _arg244);
                    reply.writeNoException();
                    reply.writeInt(isPackageSuspended ? 1 : 0);
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg055 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg055 = null;
                    }
                    String _arg180 = data.readString();
                    byte[] _arg245 = data.createByteArray();
                    boolean installCaCert = installCaCert(_arg055, _arg180, _arg245);
                    reply.writeNoException();
                    reply.writeInt(installCaCert ? 1 : 0);
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg056 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg056 = null;
                    }
                    String _arg181 = data.readString();
                    String[] _arg246 = data.createStringArray();
                    uninstallCaCerts(_arg056, _arg181, _arg246);
                    reply.writeNoException();
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg057 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg057 = null;
                    }
                    String _arg182 = data.readString();
                    enforceCanManageCaCerts(_arg057, _arg182);
                    reply.writeNoException();
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0221 = data.readString();
                    int _arg183 = data.readInt();
                    boolean _arg247 = data.readInt() != 0;
                    boolean approveCaCert = approveCaCert(_arg0221, _arg183, _arg247);
                    reply.writeNoException();
                    reply.writeInt(approveCaCert ? 1 : 0);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0222 = data.readString();
                    int _arg184 = data.readInt();
                    boolean isCaCertApproved = isCaCertApproved(_arg0222, _arg184);
                    reply.writeNoException();
                    reply.writeInt(isCaCertApproved ? 1 : 0);
                    return true;
                case 90:
                    return onTransact$installKeyPair$(data, reply);
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg058 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg058 = null;
                    }
                    String _arg185 = data.readString();
                    String _arg248 = data.readString();
                    boolean removeKeyPair = removeKeyPair(_arg058, _arg185, _arg248);
                    reply.writeNoException();
                    reply.writeInt(removeKeyPair ? 1 : 0);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg059 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg059 = null;
                    }
                    String _arg186 = data.readString();
                    String _arg249 = data.readString();
                    if (data.readInt() != 0) {
                        _arg3 = ParcelableKeyGenParameterSpec.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    int _arg42 = data.readInt();
                    KeymasterCertificateChain _arg5 = new KeymasterCertificateChain();
                    boolean generateKeyPair = generateKeyPair(_arg059, _arg186, _arg249, _arg3, _arg42, _arg5);
                    reply.writeNoException();
                    reply.writeInt(generateKeyPair ? 1 : 0);
                    reply.writeInt(1);
                    _arg5.writeToParcel(reply, 1);
                    return true;
                case 93:
                    return onTransact$setKeyPairCertificate$(data, reply);
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0223 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg14 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    String _arg250 = data.readString();
                    IBinder _arg35 = data.readStrongBinder();
                    choosePrivateKeyAlias(_arg0223, _arg14, _arg250, _arg35);
                    reply.writeNoException();
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg060 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg060 = null;
                    }
                    String _arg187 = data.readString();
                    List<String> _arg251 = data.createStringArrayList();
                    setDelegatedScopes(_arg060, _arg187, _arg251);
                    reply.writeNoException();
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg061 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg061 = null;
                    }
                    String _arg188 = data.readString();
                    List<String> _result32 = getDelegatedScopes(_arg061, _arg188);
                    reply.writeNoException();
                    reply.writeStringList(_result32);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg062 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg062 = null;
                    }
                    String _arg189 = data.readString();
                    List<String> _result33 = getDelegatePackages(_arg062, _arg189);
                    reply.writeNoException();
                    reply.writeStringList(_result33);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg063 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg063 = null;
                    }
                    String _arg190 = data.readString();
                    setCertInstallerPackage(_arg063, _arg190);
                    reply.writeNoException();
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg064 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg064 = null;
                    }
                    String _result34 = getCertInstallerPackage(_arg064);
                    reply.writeNoException();
                    reply.writeString(_result34);
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg065 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg065 = null;
                    }
                    String _arg191 = data.readString();
                    boolean _arg252 = data.readInt() != 0;
                    List<String> _arg36 = data.createStringArrayList();
                    boolean alwaysOnVpnPackage = setAlwaysOnVpnPackage(_arg065, _arg191, _arg252, _arg36);
                    reply.writeNoException();
                    reply.writeInt(alwaysOnVpnPackage ? 1 : 0);
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg066 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg066 = null;
                    }
                    String _result35 = getAlwaysOnVpnPackage(_arg066);
                    reply.writeNoException();
                    reply.writeString(_result35);
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg067 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg067 = null;
                    }
                    boolean isAlwaysOnVpnLockdownEnabled = isAlwaysOnVpnLockdownEnabled(_arg067);
                    reply.writeNoException();
                    reply.writeInt(isAlwaysOnVpnLockdownEnabled ? 1 : 0);
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg068 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg068 = null;
                    }
                    List<String> _result36 = getAlwaysOnVpnLockdownWhitelist(_arg068);
                    reply.writeNoException();
                    reply.writeStringList(_result36);
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg069 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg069 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg15 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    addPersistentPreferredActivity(_arg069, _arg15, _arg2);
                    reply.writeNoException();
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg070 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg070 = null;
                    }
                    String _arg192 = data.readString();
                    clearPackagePersistentPreferredActivities(_arg070, _arg192);
                    reply.writeNoException();
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg071 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg071 = null;
                    }
                    String _arg193 = data.readString();
                    setDefaultSmsApplication(_arg071, _arg193);
                    reply.writeNoException();
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg072 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg072 = null;
                    }
                    String _arg194 = data.readString();
                    String _arg253 = data.readString();
                    if (data.readInt() != 0) {
                        _arg32 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    setApplicationRestrictions(_arg072, _arg194, _arg253, _arg32);
                    reply.writeNoException();
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg073 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg073 = null;
                    }
                    String _arg195 = data.readString();
                    String _arg254 = data.readString();
                    Bundle _result37 = getApplicationRestrictions(_arg073, _arg195, _arg254);
                    reply.writeNoException();
                    if (_result37 != null) {
                        reply.writeInt(1);
                        _result37.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg074 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg074 = null;
                    }
                    String _arg196 = data.readString();
                    boolean applicationRestrictionsManagingPackage = setApplicationRestrictionsManagingPackage(_arg074, _arg196);
                    reply.writeNoException();
                    reply.writeInt(applicationRestrictionsManagingPackage ? 1 : 0);
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg075 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg075 = null;
                    }
                    String _result38 = getApplicationRestrictionsManagingPackage(_arg075);
                    reply.writeNoException();
                    reply.writeString(_result38);
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0224 = data.readString();
                    boolean isCallerApplicationRestrictionsManagingPackage = isCallerApplicationRestrictionsManagingPackage(_arg0224);
                    reply.writeNoException();
                    reply.writeInt(isCallerApplicationRestrictionsManagingPackage ? 1 : 0);
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg076 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg076 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg16 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg16 = null;
                    }
                    setRestrictionsProvider(_arg076, _arg16);
                    reply.writeNoException();
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0225 = data.readInt();
                    ComponentName _result39 = getRestrictionsProvider(_arg0225);
                    reply.writeNoException();
                    if (_result39 != null) {
                        reply.writeInt(1);
                        _result39.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg077 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg077 = null;
                    }
                    String _arg197 = data.readString();
                    boolean _arg255 = data.readInt() != 0;
                    setUserRestriction(_arg077, _arg197, _arg255);
                    reply.writeNoException();
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg078 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg078 = null;
                    }
                    Bundle _result40 = getUserRestrictions(_arg078);
                    reply.writeNoException();
                    if (_result40 != null) {
                        reply.writeInt(1);
                        _result40.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg079 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg079 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg17 = IntentFilter.CREATOR.createFromParcel(data);
                    } else {
                        _arg17 = null;
                    }
                    int _arg256 = data.readInt();
                    addCrossProfileIntentFilter(_arg079, _arg17, _arg256);
                    reply.writeNoException();
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg080 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg080 = null;
                    }
                    clearCrossProfileIntentFilters(_arg080);
                    reply.writeNoException();
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg081 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg081 = null;
                    }
                    ClassLoader cl = getClass().getClassLoader();
                    List _arg198 = data.readArrayList(cl);
                    boolean permittedAccessibilityServices = setPermittedAccessibilityServices(_arg081, _arg198);
                    reply.writeNoException();
                    reply.writeInt(permittedAccessibilityServices ? 1 : 0);
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg082 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg082 = null;
                    }
                    List _result41 = getPermittedAccessibilityServices(_arg082);
                    reply.writeNoException();
                    reply.writeList(_result41);
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0226 = data.readInt();
                    List _result42 = getPermittedAccessibilityServicesForUser(_arg0226);
                    reply.writeNoException();
                    reply.writeList(_result42);
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg083 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg083 = null;
                    }
                    String _arg199 = data.readString();
                    int _arg257 = data.readInt();
                    boolean isAccessibilityServicePermittedByAdmin = isAccessibilityServicePermittedByAdmin(_arg083, _arg199, _arg257);
                    reply.writeNoException();
                    reply.writeInt(isAccessibilityServicePermittedByAdmin ? 1 : 0);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg084 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg084 = null;
                    }
                    ClassLoader cl2 = getClass().getClassLoader();
                    List _arg1100 = data.readArrayList(cl2);
                    boolean permittedInputMethods = setPermittedInputMethods(_arg084, _arg1100);
                    reply.writeNoException();
                    reply.writeInt(permittedInputMethods ? 1 : 0);
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg085 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg085 = null;
                    }
                    List _result43 = getPermittedInputMethods(_arg085);
                    reply.writeNoException();
                    reply.writeList(_result43);
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    List _result44 = getPermittedInputMethodsForCurrentUser();
                    reply.writeNoException();
                    reply.writeList(_result44);
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg086 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg086 = null;
                    }
                    String _arg1101 = data.readString();
                    int _arg258 = data.readInt();
                    boolean isInputMethodPermittedByAdmin = isInputMethodPermittedByAdmin(_arg086, _arg1101, _arg258);
                    reply.writeNoException();
                    reply.writeInt(isInputMethodPermittedByAdmin ? 1 : 0);
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg087 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg087 = null;
                    }
                    List<String> _arg1102 = data.createStringArrayList();
                    boolean permittedCrossProfileNotificationListeners = setPermittedCrossProfileNotificationListeners(_arg087, _arg1102);
                    reply.writeNoException();
                    reply.writeInt(permittedCrossProfileNotificationListeners ? 1 : 0);
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg088 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg088 = null;
                    }
                    List<String> _result45 = getPermittedCrossProfileNotificationListeners(_arg088);
                    reply.writeNoException();
                    reply.writeStringList(_result45);
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0227 = data.readString();
                    int _arg1103 = data.readInt();
                    boolean isNotificationListenerServicePermitted = isNotificationListenerServicePermitted(_arg0227, _arg1103);
                    reply.writeNoException();
                    reply.writeInt(isNotificationListenerServicePermitted ? 1 : 0);
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0228 = data.readString();
                    Intent _result46 = createAdminSupportIntent(_arg0228);
                    reply.writeNoException();
                    if (_result46 != null) {
                        reply.writeInt(1);
                        _result46.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg089 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg089 = null;
                    }
                    String _arg1104 = data.readString();
                    String _arg259 = data.readString();
                    boolean _arg37 = data.readInt() != 0;
                    boolean applicationHidden = setApplicationHidden(_arg089, _arg1104, _arg259, _arg37);
                    reply.writeNoException();
                    reply.writeInt(applicationHidden ? 1 : 0);
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg090 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg090 = null;
                    }
                    String _arg1105 = data.readString();
                    String _arg260 = data.readString();
                    boolean isApplicationHidden = isApplicationHidden(_arg090, _arg1105, _arg260);
                    reply.writeNoException();
                    reply.writeInt(isApplicationHidden ? 1 : 0);
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg091 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg091 = null;
                    }
                    String _arg1106 = data.readString();
                    if (data.readInt() != 0) {
                        _arg22 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg33 = PersistableBundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg33 = null;
                    }
                    int _arg43 = data.readInt();
                    UserHandle _result47 = createAndManageUser(_arg091, _arg1106, _arg22, _arg33, _arg43);
                    reply.writeNoException();
                    if (_result47 != null) {
                        reply.writeInt(1);
                        _result47.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg092 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg092 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg18 = UserHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg18 = null;
                    }
                    boolean removeUser = removeUser(_arg092, _arg18);
                    reply.writeNoException();
                    reply.writeInt(removeUser ? 1 : 0);
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg093 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg093 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg19 = UserHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg19 = null;
                    }
                    boolean switchUser = switchUser(_arg093, _arg19);
                    reply.writeNoException();
                    reply.writeInt(switchUser ? 1 : 0);
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg094 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg094 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg110 = UserHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg110 = null;
                    }
                    int _result48 = startUserInBackground(_arg094, _arg110);
                    reply.writeNoException();
                    reply.writeInt(_result48);
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg095 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg095 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg111 = UserHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg111 = null;
                    }
                    int _result49 = stopUser(_arg095, _arg111);
                    reply.writeNoException();
                    reply.writeInt(_result49);
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg096 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg096 = null;
                    }
                    int _result50 = logoutUser(_arg096);
                    reply.writeNoException();
                    reply.writeInt(_result50);
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg097 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg097 = null;
                    }
                    List<UserHandle> _result51 = getSecondaryUsers(_arg097);
                    reply.writeNoException();
                    reply.writeTypedList(_result51);
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg098 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg098 = null;
                    }
                    String _arg1107 = data.readString();
                    String _arg261 = data.readString();
                    enableSystemApp(_arg098, _arg1107, _arg261);
                    reply.writeNoException();
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg099 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg099 = null;
                    }
                    String _arg1108 = data.readString();
                    if (data.readInt() != 0) {
                        _arg23 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg23 = null;
                    }
                    int _result52 = enableSystemAppWithIntent(_arg099, _arg1108, _arg23);
                    reply.writeNoException();
                    reply.writeInt(_result52);
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0100 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0100 = null;
                    }
                    String _arg1109 = data.readString();
                    String _arg262 = data.readString();
                    boolean installExistingPackage = installExistingPackage(_arg0100, _arg1109, _arg262);
                    reply.writeNoException();
                    reply.writeInt(installExistingPackage ? 1 : 0);
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0101 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0101 = null;
                    }
                    String _arg1110 = data.readString();
                    boolean _arg263 = data.readInt() != 0;
                    setAccountManagementDisabled(_arg0101, _arg1110, _arg263);
                    reply.writeNoException();
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result53 = getAccountTypesWithManagementDisabled();
                    reply.writeNoException();
                    reply.writeStringArray(_result53);
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0229 = data.readInt();
                    String[] _result54 = getAccountTypesWithManagementDisabledAsUser(_arg0229);
                    reply.writeNoException();
                    reply.writeStringArray(_result54);
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0102 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0102 = null;
                    }
                    String[] _arg1111 = data.createStringArray();
                    setLockTaskPackages(_arg0102, _arg1111);
                    reply.writeNoException();
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0103 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0103 = null;
                    }
                    String[] _result55 = getLockTaskPackages(_arg0103);
                    reply.writeNoException();
                    reply.writeStringArray(_result55);
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0230 = data.readString();
                    boolean isLockTaskPermitted = isLockTaskPermitted(_arg0230);
                    reply.writeNoException();
                    reply.writeInt(isLockTaskPermitted ? 1 : 0);
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0104 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0104 = null;
                    }
                    int _arg1112 = data.readInt();
                    setLockTaskFeatures(_arg0104, _arg1112);
                    reply.writeNoException();
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0105 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0105 = null;
                    }
                    int _result56 = getLockTaskFeatures(_arg0105);
                    reply.writeNoException();
                    reply.writeInt(_result56);
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0106 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0106 = null;
                    }
                    String _arg1113 = data.readString();
                    String _arg264 = data.readString();
                    setGlobalSetting(_arg0106, _arg1113, _arg264);
                    reply.writeNoException();
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0107 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0107 = null;
                    }
                    String _arg1114 = data.readString();
                    String _arg265 = data.readString();
                    setSystemSetting(_arg0107, _arg1114, _arg265);
                    reply.writeNoException();
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0108 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0108 = null;
                    }
                    String _arg1115 = data.readString();
                    String _arg266 = data.readString();
                    setSecureSetting(_arg0108, _arg1115, _arg266);
                    reply.writeNoException();
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0109 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0109 = null;
                    }
                    long _arg1116 = data.readLong();
                    boolean time = setTime(_arg0109, _arg1116);
                    reply.writeNoException();
                    reply.writeInt(time ? 1 : 0);
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0110 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0110 = null;
                    }
                    String _arg1117 = data.readString();
                    boolean timeZone = setTimeZone(_arg0110, _arg1117);
                    reply.writeNoException();
                    reply.writeInt(timeZone ? 1 : 0);
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0111 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0111 = null;
                    }
                    boolean _arg1118 = data.readInt() != 0;
                    setMasterVolumeMuted(_arg0111, _arg1118);
                    reply.writeNoException();
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0112 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0112 = null;
                    }
                    boolean isMasterVolumeMuted = isMasterVolumeMuted(_arg0112);
                    reply.writeNoException();
                    reply.writeInt(isMasterVolumeMuted ? 1 : 0);
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg0231 = data.readInt() != 0;
                    String _arg1119 = data.readString();
                    int _arg267 = data.readInt();
                    notifyLockTaskModeChanged(_arg0231, _arg1119, _arg267);
                    reply.writeNoException();
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0113 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0113 = null;
                    }
                    String _arg1120 = data.readString();
                    String _arg268 = data.readString();
                    boolean _arg38 = data.readInt() != 0;
                    setUninstallBlocked(_arg0113, _arg1120, _arg268, _arg38);
                    reply.writeNoException();
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0114 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0114 = null;
                    }
                    String _arg1121 = data.readString();
                    boolean isUninstallBlocked = isUninstallBlocked(_arg0114, _arg1121);
                    reply.writeNoException();
                    reply.writeInt(isUninstallBlocked ? 1 : 0);
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0115 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0115 = null;
                    }
                    boolean _arg1122 = data.readInt() != 0;
                    setCrossProfileCallerIdDisabled(_arg0115, _arg1122);
                    reply.writeNoException();
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0116 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0116 = null;
                    }
                    boolean crossProfileCallerIdDisabled = getCrossProfileCallerIdDisabled(_arg0116);
                    reply.writeNoException();
                    reply.writeInt(crossProfileCallerIdDisabled ? 1 : 0);
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0232 = data.readInt();
                    boolean crossProfileCallerIdDisabledForUser = getCrossProfileCallerIdDisabledForUser(_arg0232);
                    reply.writeNoException();
                    reply.writeInt(crossProfileCallerIdDisabledForUser ? 1 : 0);
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0117 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0117 = null;
                    }
                    boolean _arg1123 = data.readInt() != 0;
                    setCrossProfileContactsSearchDisabled(_arg0117, _arg1123);
                    reply.writeNoException();
                    return true;
                case 164:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0118 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0118 = null;
                    }
                    boolean crossProfileContactsSearchDisabled = getCrossProfileContactsSearchDisabled(_arg0118);
                    reply.writeNoException();
                    reply.writeInt(crossProfileContactsSearchDisabled ? 1 : 0);
                    return true;
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0233 = data.readInt();
                    boolean crossProfileContactsSearchDisabledForUser = getCrossProfileContactsSearchDisabledForUser(_arg0233);
                    reply.writeNoException();
                    reply.writeInt(crossProfileContactsSearchDisabledForUser ? 1 : 0);
                    return true;
                case 166:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0234 = data.readString();
                    long _arg1124 = data.readLong();
                    boolean _arg269 = data.readInt() != 0;
                    long _arg39 = data.readLong();
                    if (data.readInt() != 0) {
                        _arg4 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    startManagedQuickContact(_arg0234, _arg1124, _arg269, _arg39, _arg4);
                    reply.writeNoException();
                    return true;
                case 167:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0119 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0119 = null;
                    }
                    boolean _arg1125 = data.readInt() != 0;
                    setBluetoothContactSharingDisabled(_arg0119, _arg1125);
                    reply.writeNoException();
                    return true;
                case 168:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0120 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0120 = null;
                    }
                    boolean bluetoothContactSharingDisabled = getBluetoothContactSharingDisabled(_arg0120);
                    reply.writeNoException();
                    reply.writeInt(bluetoothContactSharingDisabled ? 1 : 0);
                    return true;
                case 169:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0235 = data.readInt();
                    boolean bluetoothContactSharingDisabledForUser = getBluetoothContactSharingDisabledForUser(_arg0235);
                    reply.writeNoException();
                    reply.writeInt(bluetoothContactSharingDisabledForUser ? 1 : 0);
                    return true;
                case 170:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0121 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0121 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg112 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg112 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg24 = PersistableBundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg24 = null;
                    }
                    boolean _arg310 = data.readInt() != 0;
                    setTrustAgentConfiguration(_arg0121, _arg112, _arg24, _arg310);
                    reply.writeNoException();
                    return true;
                case 171:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0122 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0122 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg113 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg113 = null;
                    }
                    int _arg270 = data.readInt();
                    boolean _arg311 = data.readInt() != 0;
                    List<PersistableBundle> _result57 = getTrustAgentConfiguration(_arg0122, _arg113, _arg270, _arg311);
                    reply.writeNoException();
                    reply.writeTypedList(_result57);
                    return true;
                case 172:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0123 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0123 = null;
                    }
                    String _arg1126 = data.readString();
                    boolean addCrossProfileWidgetProvider = addCrossProfileWidgetProvider(_arg0123, _arg1126);
                    reply.writeNoException();
                    reply.writeInt(addCrossProfileWidgetProvider ? 1 : 0);
                    return true;
                case 173:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0124 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0124 = null;
                    }
                    String _arg1127 = data.readString();
                    boolean removeCrossProfileWidgetProvider = removeCrossProfileWidgetProvider(_arg0124, _arg1127);
                    reply.writeNoException();
                    reply.writeInt(removeCrossProfileWidgetProvider ? 1 : 0);
                    return true;
                case 174:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0125 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0125 = null;
                    }
                    List<String> _result58 = getCrossProfileWidgetProviders(_arg0125);
                    reply.writeNoException();
                    reply.writeStringList(_result58);
                    return true;
                case 175:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0126 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0126 = null;
                    }
                    boolean _arg1128 = data.readInt() != 0;
                    setAutoTimeRequired(_arg0126, _arg1128);
                    reply.writeNoException();
                    return true;
                case 176:
                    data.enforceInterface(DESCRIPTOR);
                    boolean autoTimeRequired = getAutoTimeRequired();
                    reply.writeNoException();
                    reply.writeInt(autoTimeRequired ? 1 : 0);
                    return true;
                case 177:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0127 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0127 = null;
                    }
                    boolean _arg1129 = data.readInt() != 0;
                    setForceEphemeralUsers(_arg0127, _arg1129);
                    reply.writeNoException();
                    return true;
                case 178:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0128 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0128 = null;
                    }
                    boolean forceEphemeralUsers = getForceEphemeralUsers(_arg0128);
                    reply.writeNoException();
                    reply.writeInt(forceEphemeralUsers ? 1 : 0);
                    return true;
                case 179:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0129 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0129 = null;
                    }
                    int _arg1130 = data.readInt();
                    boolean isRemovingAdmin = isRemovingAdmin(_arg0129, _arg1130);
                    reply.writeNoException();
                    reply.writeInt(isRemovingAdmin ? 1 : 0);
                    return true;
                case 180:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0130 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0130 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg114 = Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg114 = null;
                    }
                    setUserIcon(_arg0130, _arg114);
                    reply.writeNoException();
                    return true;
                case 181:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0131 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0131 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg115 = SystemUpdatePolicy.CREATOR.createFromParcel(data);
                    } else {
                        _arg115 = null;
                    }
                    setSystemUpdatePolicy(_arg0131, _arg115);
                    reply.writeNoException();
                    return true;
                case 182:
                    data.enforceInterface(DESCRIPTOR);
                    SystemUpdatePolicy _result59 = getSystemUpdatePolicy();
                    reply.writeNoException();
                    if (_result59 != null) {
                        reply.writeInt(1);
                        _result59.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 183:
                    data.enforceInterface(DESCRIPTOR);
                    clearSystemUpdatePolicyFreezePeriodRecord();
                    reply.writeNoException();
                    return true;
                case 184:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0132 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0132 = null;
                    }
                    boolean _arg1131 = data.readInt() != 0;
                    boolean keyguardDisabled = setKeyguardDisabled(_arg0132, _arg1131);
                    reply.writeNoException();
                    reply.writeInt(keyguardDisabled ? 1 : 0);
                    return true;
                case 185:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0133 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0133 = null;
                    }
                    boolean _arg1132 = data.readInt() != 0;
                    boolean statusBarDisabled = setStatusBarDisabled(_arg0133, _arg1132);
                    reply.writeNoException();
                    reply.writeInt(statusBarDisabled ? 1 : 0);
                    return true;
                case 186:
                    data.enforceInterface(DESCRIPTOR);
                    boolean doNotAskCredentialsOnBoot = getDoNotAskCredentialsOnBoot();
                    reply.writeNoException();
                    reply.writeInt(doNotAskCredentialsOnBoot ? 1 : 0);
                    return true;
                case 187:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0134 = SystemUpdateInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg0134 = null;
                    }
                    notifyPendingSystemUpdate(_arg0134);
                    reply.writeNoException();
                    return true;
                case 188:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0135 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0135 = null;
                    }
                    SystemUpdateInfo _result60 = getPendingSystemUpdate(_arg0135);
                    reply.writeNoException();
                    if (_result60 != null) {
                        reply.writeInt(1);
                        _result60.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 189:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0136 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0136 = null;
                    }
                    String _arg1133 = data.readString();
                    int _arg271 = data.readInt();
                    setPermissionPolicy(_arg0136, _arg1133, _arg271);
                    reply.writeNoException();
                    return true;
                case 190:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0137 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0137 = null;
                    }
                    int _result61 = getPermissionPolicy(_arg0137);
                    reply.writeNoException();
                    reply.writeInt(_result61);
                    return true;
                case 191:
                    return onTransact$setPermissionGrantState$(data, reply);
                case 192:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0138 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0138 = null;
                    }
                    String _arg1134 = data.readString();
                    String _arg272 = data.readString();
                    String _arg312 = data.readString();
                    int _result62 = getPermissionGrantState(_arg0138, _arg1134, _arg272, _arg312);
                    reply.writeNoException();
                    reply.writeInt(_result62);
                    return true;
                case 193:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0236 = data.readString();
                    String _arg1135 = data.readString();
                    boolean isProvisioningAllowed = isProvisioningAllowed(_arg0236, _arg1135);
                    reply.writeNoException();
                    reply.writeInt(isProvisioningAllowed ? 1 : 0);
                    return true;
                case 194:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0237 = data.readString();
                    String _arg1136 = data.readString();
                    int _result63 = checkProvisioningPreCondition(_arg0237, _arg1136);
                    reply.writeNoException();
                    reply.writeInt(_result63);
                    return true;
                case 195:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0139 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0139 = null;
                    }
                    String _arg1137 = data.readString();
                    List<String> _arg273 = data.createStringArrayList();
                    setKeepUninstalledPackages(_arg0139, _arg1137, _arg273);
                    reply.writeNoException();
                    return true;
                case 196:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0140 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0140 = null;
                    }
                    String _arg1138 = data.readString();
                    List<String> _result64 = getKeepUninstalledPackages(_arg0140, _arg1138);
                    reply.writeNoException();
                    reply.writeStringList(_result64);
                    return true;
                case 197:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0141 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0141 = null;
                    }
                    boolean isManagedProfile = isManagedProfile(_arg0141);
                    reply.writeNoException();
                    reply.writeInt(isManagedProfile ? 1 : 0);
                    return true;
                case 198:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0142 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0142 = null;
                    }
                    boolean isSystemOnlyUser = isSystemOnlyUser(_arg0142);
                    reply.writeNoException();
                    reply.writeInt(isSystemOnlyUser ? 1 : 0);
                    return true;
                case 199:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0143 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0143 = null;
                    }
                    String _result65 = getWifiMacAddress(_arg0143);
                    reply.writeNoException();
                    reply.writeString(_result65);
                    return true;
                case 200:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0144 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0144 = null;
                    }
                    reboot(_arg0144);
                    reply.writeNoException();
                    return true;
                case 201:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0145 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0145 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg116 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg116 = null;
                    }
                    setShortSupportMessage(_arg0145, _arg116);
                    reply.writeNoException();
                    return true;
                case 202:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0146 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0146 = null;
                    }
                    CharSequence _result66 = getShortSupportMessage(_arg0146);
                    reply.writeNoException();
                    if (_result66 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result66, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 203:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0147 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0147 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg117 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg117 = null;
                    }
                    setLongSupportMessage(_arg0147, _arg117);
                    reply.writeNoException();
                    return true;
                case 204:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0148 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0148 = null;
                    }
                    CharSequence _result67 = getLongSupportMessage(_arg0148);
                    reply.writeNoException();
                    if (_result67 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result67, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 205:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0149 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0149 = null;
                    }
                    int _arg1139 = data.readInt();
                    CharSequence _result68 = getShortSupportMessageForUser(_arg0149, _arg1139);
                    reply.writeNoException();
                    if (_result68 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result68, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 206:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0150 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0150 = null;
                    }
                    int _arg1140 = data.readInt();
                    CharSequence _result69 = getLongSupportMessageForUser(_arg0150, _arg1140);
                    reply.writeNoException();
                    if (_result69 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result69, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 207:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0238 = data.readInt();
                    boolean isSeparateProfileChallengeAllowed = isSeparateProfileChallengeAllowed(_arg0238);
                    reply.writeNoException();
                    reply.writeInt(isSeparateProfileChallengeAllowed ? 1 : 0);
                    return true;
                case 208:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0151 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0151 = null;
                    }
                    int _arg1141 = data.readInt();
                    setOrganizationColor(_arg0151, _arg1141);
                    reply.writeNoException();
                    return true;
                case 209:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0239 = data.readInt();
                    int _arg1142 = data.readInt();
                    setOrganizationColorForUser(_arg0239, _arg1142);
                    reply.writeNoException();
                    return true;
                case 210:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0152 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0152 = null;
                    }
                    int _result70 = getOrganizationColor(_arg0152);
                    reply.writeNoException();
                    reply.writeInt(_result70);
                    return true;
                case 211:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0240 = data.readInt();
                    int _result71 = getOrganizationColorForUser(_arg0240);
                    reply.writeNoException();
                    reply.writeInt(_result71);
                    return true;
                case 212:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0153 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0153 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg118 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg118 = null;
                    }
                    setOrganizationName(_arg0153, _arg118);
                    reply.writeNoException();
                    return true;
                case 213:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0154 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0154 = null;
                    }
                    CharSequence _result72 = getOrganizationName(_arg0154);
                    reply.writeNoException();
                    if (_result72 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result72, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 214:
                    data.enforceInterface(DESCRIPTOR);
                    CharSequence _result73 = getDeviceOwnerOrganizationName();
                    reply.writeNoException();
                    if (_result73 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result73, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 215:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0241 = data.readInt();
                    CharSequence _result74 = getOrganizationNameForUser(_arg0241);
                    reply.writeNoException();
                    if (_result74 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result74, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 216:
                    data.enforceInterface(DESCRIPTOR);
                    int _result75 = getUserProvisioningState();
                    reply.writeNoException();
                    reply.writeInt(_result75);
                    return true;
                case 217:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0242 = data.readInt();
                    int _arg1143 = data.readInt();
                    setUserProvisioningState(_arg0242, _arg1143);
                    reply.writeNoException();
                    return true;
                case 218:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0155 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0155 = null;
                    }
                    List<String> _arg1144 = data.createStringArrayList();
                    setAffiliationIds(_arg0155, _arg1144);
                    reply.writeNoException();
                    return true;
                case 219:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0156 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0156 = null;
                    }
                    List<String> _result76 = getAffiliationIds(_arg0156);
                    reply.writeNoException();
                    reply.writeStringList(_result76);
                    return true;
                case 220:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAffiliatedUser = isAffiliatedUser();
                    reply.writeNoException();
                    reply.writeInt(isAffiliatedUser ? 1 : 0);
                    return true;
                case 221:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0157 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0157 = null;
                    }
                    boolean _arg1145 = data.readInt() != 0;
                    setSecurityLoggingEnabled(_arg0157, _arg1145);
                    reply.writeNoException();
                    return true;
                case 222:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0158 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0158 = null;
                    }
                    boolean isSecurityLoggingEnabled = isSecurityLoggingEnabled(_arg0158);
                    reply.writeNoException();
                    reply.writeInt(isSecurityLoggingEnabled ? 1 : 0);
                    return true;
                case 223:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0159 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0159 = null;
                    }
                    ParceledListSlice _result77 = retrieveSecurityLogs(_arg0159);
                    reply.writeNoException();
                    if (_result77 != null) {
                        reply.writeInt(1);
                        _result77.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 224:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0160 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0160 = null;
                    }
                    ParceledListSlice _result78 = retrievePreRebootSecurityLogs(_arg0160);
                    reply.writeNoException();
                    if (_result78 != null) {
                        reply.writeInt(1);
                        _result78.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 225:
                    data.enforceInterface(DESCRIPTOR);
                    long _result79 = forceNetworkLogs();
                    reply.writeNoException();
                    reply.writeLong(_result79);
                    return true;
                case 226:
                    data.enforceInterface(DESCRIPTOR);
                    long _result80 = forceSecurityLogs();
                    reply.writeNoException();
                    reply.writeLong(_result80);
                    return true;
                case 227:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0243 = data.readString();
                    boolean isUninstallInQueue = isUninstallInQueue(_arg0243);
                    reply.writeNoException();
                    reply.writeInt(isUninstallInQueue ? 1 : 0);
                    return true;
                case 228:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0244 = data.readString();
                    uninstallPackageWithActiveAdmins(_arg0244);
                    reply.writeNoException();
                    return true;
                case 229:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDeviceProvisioned = isDeviceProvisioned();
                    reply.writeNoException();
                    reply.writeInt(isDeviceProvisioned ? 1 : 0);
                    return true;
                case 230:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDeviceProvisioningConfigApplied = isDeviceProvisioningConfigApplied();
                    reply.writeNoException();
                    reply.writeInt(isDeviceProvisioningConfigApplied ? 1 : 0);
                    return true;
                case 231:
                    data.enforceInterface(DESCRIPTOR);
                    setDeviceProvisioningConfigApplied();
                    reply.writeNoException();
                    return true;
                case 232:
                    data.enforceInterface(DESCRIPTOR);
                    forceUpdateUserSetupComplete();
                    reply.writeNoException();
                    return true;
                case 233:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0161 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0161 = null;
                    }
                    boolean _arg1146 = data.readInt() != 0;
                    setBackupServiceEnabled(_arg0161, _arg1146);
                    reply.writeNoException();
                    return true;
                case 234:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0162 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0162 = null;
                    }
                    boolean isBackupServiceEnabled = isBackupServiceEnabled(_arg0162);
                    reply.writeNoException();
                    reply.writeInt(isBackupServiceEnabled ? 1 : 0);
                    return true;
                case 235:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0163 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0163 = null;
                    }
                    String _arg1147 = data.readString();
                    boolean _arg274 = data.readInt() != 0;
                    setNetworkLoggingEnabled(_arg0163, _arg1147, _arg274);
                    reply.writeNoException();
                    return true;
                case 236:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0164 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0164 = null;
                    }
                    String _arg1148 = data.readString();
                    boolean isNetworkLoggingEnabled = isNetworkLoggingEnabled(_arg0164, _arg1148);
                    reply.writeNoException();
                    reply.writeInt(isNetworkLoggingEnabled ? 1 : 0);
                    return true;
                case 237:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0165 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0165 = null;
                    }
                    String _arg1149 = data.readString();
                    long _arg275 = data.readLong();
                    List<NetworkEvent> _result81 = retrieveNetworkLogs(_arg0165, _arg1149, _arg275);
                    reply.writeNoException();
                    reply.writeTypedList(_result81);
                    return true;
                case 238:
                    return onTransact$bindDeviceAdminServiceAsUser$(data, reply);
                case 239:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0166 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0166 = null;
                    }
                    List<UserHandle> _result82 = getBindDeviceAdminTargetUsers(_arg0166);
                    reply.writeNoException();
                    reply.writeTypedList(_result82);
                    return true;
                case 240:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0167 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0167 = null;
                    }
                    boolean isEphemeralUser = isEphemeralUser(_arg0167);
                    reply.writeNoException();
                    reply.writeInt(isEphemeralUser ? 1 : 0);
                    return true;
                case 241:
                    data.enforceInterface(DESCRIPTOR);
                    long _result83 = getLastSecurityLogRetrievalTime();
                    reply.writeNoException();
                    reply.writeLong(_result83);
                    return true;
                case 242:
                    data.enforceInterface(DESCRIPTOR);
                    long _result84 = getLastBugReportRequestTime();
                    reply.writeNoException();
                    reply.writeLong(_result84);
                    return true;
                case 243:
                    data.enforceInterface(DESCRIPTOR);
                    long _result85 = getLastNetworkLogRetrievalTime();
                    reply.writeNoException();
                    reply.writeLong(_result85);
                    return true;
                case 244:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0168 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0168 = null;
                    }
                    byte[] _arg1150 = data.createByteArray();
                    boolean resetPasswordToken = setResetPasswordToken(_arg0168, _arg1150);
                    reply.writeNoException();
                    reply.writeInt(resetPasswordToken ? 1 : 0);
                    return true;
                case 245:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0169 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0169 = null;
                    }
                    boolean clearResetPasswordToken = clearResetPasswordToken(_arg0169);
                    reply.writeNoException();
                    reply.writeInt(clearResetPasswordToken ? 1 : 0);
                    return true;
                case 246:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0170 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0170 = null;
                    }
                    boolean isResetPasswordTokenActive = isResetPasswordTokenActive(_arg0170);
                    reply.writeNoException();
                    reply.writeInt(isResetPasswordTokenActive ? 1 : 0);
                    return true;
                case 247:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0171 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0171 = null;
                    }
                    String _arg1151 = data.readString();
                    byte[] _arg276 = data.createByteArray();
                    int _arg313 = data.readInt();
                    boolean resetPasswordWithToken = resetPasswordWithToken(_arg0171, _arg1151, _arg276, _arg313);
                    reply.writeNoException();
                    reply.writeInt(resetPasswordWithToken ? 1 : 0);
                    return true;
                case 248:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isCurrentInputMethodSetByOwner = isCurrentInputMethodSetByOwner();
                    reply.writeNoException();
                    reply.writeInt(isCurrentInputMethodSetByOwner ? 1 : 0);
                    return true;
                case 249:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0172 = UserHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg0172 = null;
                    }
                    StringParceledListSlice _result86 = getOwnerInstalledCaCerts(_arg0172);
                    reply.writeNoException();
                    if (_result86 != null) {
                        reply.writeInt(1);
                        _result86.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 250:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0173 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0173 = null;
                    }
                    String _arg1152 = data.readString();
                    IPackageDataObserver _arg277 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    clearApplicationUserData(_arg0173, _arg1152, _arg277);
                    reply.writeNoException();
                    return true;
                case 251:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0174 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0174 = null;
                    }
                    boolean _arg1153 = data.readInt() != 0;
                    setLogoutEnabled(_arg0174, _arg1153);
                    reply.writeNoException();
                    return true;
                case 252:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isLogoutEnabled = isLogoutEnabled();
                    reply.writeNoException();
                    reply.writeInt(isLogoutEnabled ? 1 : 0);
                    return true;
                case 253:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0175 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0175 = null;
                    }
                    int _arg1154 = data.readInt();
                    String _arg278 = data.readString();
                    List<String> _result87 = getDisallowedSystemApps(_arg0175, _arg1154, _arg278);
                    reply.writeNoException();
                    reply.writeStringList(_result87);
                    return true;
                case 254:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0176 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0176 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg119 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg119 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg25 = PersistableBundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg25 = null;
                    }
                    transferOwnership(_arg0176, _arg119, _arg25);
                    reply.writeNoException();
                    return true;
                case 255:
                    data.enforceInterface(DESCRIPTOR);
                    PersistableBundle _result88 = getTransferOwnershipBundle();
                    reply.writeNoException();
                    if (_result88 != null) {
                        reply.writeInt(1);
                        _result88.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 256:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0177 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0177 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg120 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg120 = null;
                    }
                    setStartUserSessionMessage(_arg0177, _arg120);
                    reply.writeNoException();
                    return true;
                case 257:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0178 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0178 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg121 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg121 = null;
                    }
                    setEndUserSessionMessage(_arg0178, _arg121);
                    reply.writeNoException();
                    return true;
                case 258:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0179 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0179 = null;
                    }
                    CharSequence _result89 = getStartUserSessionMessage(_arg0179);
                    reply.writeNoException();
                    if (_result89 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result89, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 259:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0180 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0180 = null;
                    }
                    CharSequence _result90 = getEndUserSessionMessage(_arg0180);
                    reply.writeNoException();
                    if (_result90 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result90, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 260:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0181 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0181 = null;
                    }
                    List<String> _arg1155 = data.createStringArrayList();
                    List<String> _result91 = setMeteredDataDisabledPackages(_arg0181, _arg1155);
                    reply.writeNoException();
                    reply.writeStringList(_result91);
                    return true;
                case 261:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0182 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0182 = null;
                    }
                    List<String> _result92 = getMeteredDataDisabledPackages(_arg0182);
                    reply.writeNoException();
                    reply.writeStringList(_result92);
                    return true;
                case 262:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0183 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0183 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg122 = ApnSetting.CREATOR.createFromParcel(data);
                    } else {
                        _arg122 = null;
                    }
                    int _result93 = addOverrideApn(_arg0183, _arg122);
                    reply.writeNoException();
                    reply.writeInt(_result93);
                    return true;
                case 263:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0184 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0184 = null;
                    }
                    int _arg1156 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg26 = ApnSetting.CREATOR.createFromParcel(data);
                    } else {
                        _arg26 = null;
                    }
                    boolean updateOverrideApn = updateOverrideApn(_arg0184, _arg1156, _arg26);
                    reply.writeNoException();
                    reply.writeInt(updateOverrideApn ? 1 : 0);
                    return true;
                case 264:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0185 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0185 = null;
                    }
                    int _arg1157 = data.readInt();
                    boolean removeOverrideApn = removeOverrideApn(_arg0185, _arg1157);
                    reply.writeNoException();
                    reply.writeInt(removeOverrideApn ? 1 : 0);
                    return true;
                case 265:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0186 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0186 = null;
                    }
                    List<ApnSetting> _result94 = getOverrideApns(_arg0186);
                    reply.writeNoException();
                    reply.writeTypedList(_result94);
                    return true;
                case 266:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0187 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0187 = null;
                    }
                    boolean _arg1158 = data.readInt() != 0;
                    setOverrideApnsEnabled(_arg0187, _arg1158);
                    reply.writeNoException();
                    return true;
                case 267:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0188 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0188 = null;
                    }
                    boolean isOverrideApnEnabled = isOverrideApnEnabled(_arg0188);
                    reply.writeNoException();
                    reply.writeInt(isOverrideApnEnabled ? 1 : 0);
                    return true;
                case 268:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0189 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0189 = null;
                    }
                    String _arg1159 = data.readString();
                    int _arg279 = data.readInt();
                    boolean isMeteredDataDisabledPackageForUser = isMeteredDataDisabledPackageForUser(_arg0189, _arg1159, _arg279);
                    reply.writeNoException();
                    reply.writeInt(isMeteredDataDisabledPackageForUser ? 1 : 0);
                    return true;
                case 269:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0190 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0190 = null;
                    }
                    int _arg1160 = data.readInt();
                    String _arg280 = data.readString();
                    int _result95 = setGlobalPrivateDns(_arg0190, _arg1160, _arg280);
                    reply.writeNoException();
                    reply.writeInt(_result95);
                    return true;
                case 270:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0191 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0191 = null;
                    }
                    int _result96 = getGlobalPrivateDnsMode(_arg0191);
                    reply.writeNoException();
                    reply.writeInt(_result96);
                    return true;
                case 271:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0192 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0192 = null;
                    }
                    String _result97 = getGlobalPrivateDnsHost(_arg0192);
                    reply.writeNoException();
                    reply.writeString(_result97);
                    return true;
                case 272:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0193 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0193 = null;
                    }
                    int _arg1161 = data.readInt();
                    grantDeviceIdsAccessToProfileOwner(_arg0193, _arg1161);
                    reply.writeNoException();
                    return true;
                case 273:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0194 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0194 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg123 = ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg123 = null;
                    }
                    StartInstallingUpdateCallback _arg281 = StartInstallingUpdateCallback.Stub.asInterface(data.readStrongBinder());
                    installUpdateFromFile(_arg0194, _arg123, _arg281);
                    reply.writeNoException();
                    return true;
                case 274:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0195 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0195 = null;
                    }
                    List<String> _arg1162 = data.createStringArrayList();
                    setCrossProfileCalendarPackages(_arg0195, _arg1162);
                    reply.writeNoException();
                    return true;
                case 275:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0196 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0196 = null;
                    }
                    List<String> _result98 = getCrossProfileCalendarPackages(_arg0196);
                    reply.writeNoException();
                    reply.writeStringList(_result98);
                    return true;
                case 276:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0245 = data.readString();
                    int _arg1163 = data.readInt();
                    boolean isPackageAllowedToAccessCalendarForUser = isPackageAllowedToAccessCalendarForUser(_arg0245, _arg1163);
                    reply.writeNoException();
                    reply.writeInt(isPackageAllowedToAccessCalendarForUser ? 1 : 0);
                    return true;
                case 277:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0246 = data.readInt();
                    List<String> _result99 = getCrossProfileCalendarPackagesForUser(_arg0246);
                    reply.writeNoException();
                    reply.writeStringList(_result99);
                    return true;
                case 278:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isManagedKiosk = isManagedKiosk();
                    reply.writeNoException();
                    reply.writeInt(isManagedKiosk ? 1 : 0);
                    return true;
                case 279:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isUnattendedManagedKiosk = isUnattendedManagedKiosk();
                    reply.writeNoException();
                    reply.writeInt(isUnattendedManagedKiosk ? 1 : 0);
                    return true;
                case 280:
                    return onTransact$startViewCalendarEventInManagedProfile$(data, reply);
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IDevicePolicyManager {
            public static IDevicePolicyManager sDefaultImpl;
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

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordQuality(ComponentName who, int quality, boolean parent) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordQuality(who, quality, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordQuality(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordQuality(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumLength(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumLength(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumLength(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumUpperCase(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumUpperCase(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumUpperCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumUpperCase(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumLowerCase(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumLowerCase(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumLowerCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumLowerCase(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumLetters(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumLetters(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumLetters(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumLetters(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumNumeric(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumNumeric(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumNumeric(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumNumeric(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumSymbols(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumSymbols(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumSymbols(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumSymbols(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumNonLetter(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumNonLetter(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumNonLetter(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumNonLetter(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordHistoryLength(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordHistoryLength(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordHistoryLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordHistoryLength(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordExpirationTimeout(ComponentName who, long expiration, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(expiration);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordExpirationTimeout(who, expiration, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getPasswordExpirationTimeout(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordExpirationTimeout(who, userHandle, parent);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getPasswordExpiration(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordExpiration(who, userHandle, parent);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isActivePasswordSufficient(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isActivePasswordSufficient(userHandle, parent);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isProfileActivePasswordSufficientForParent(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isProfileActivePasswordSufficientForParent(userHandle);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordComplexity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordComplexity();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isUsingUnifiedPassword(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUsingUnifiedPassword(admin);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getCurrentFailedPasswordAttempts(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentFailedPasswordAttempts(userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getProfileWithMinimumFailedPasswordsForWipe(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProfileWithMinimumFailedPasswordsForWipe(userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setMaximumFailedPasswordsForWipe(ComponentName admin, int num, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(num);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMaximumFailedPasswordsForWipe(admin, num, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getMaximumFailedPasswordsForWipe(ComponentName admin, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMaximumFailedPasswordsForWipe(admin, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean resetPassword(String password, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resetPassword(password, flags);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setMaximumTimeToLock(ComponentName who, long timeMs, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timeMs);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMaximumTimeToLock(who, timeMs, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getMaximumTimeToLock(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMaximumTimeToLock(who, userHandle, parent);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setRequiredStrongAuthTimeout(ComponentName who, long timeMs, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timeMs);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRequiredStrongAuthTimeout(who, timeMs, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getRequiredStrongAuthTimeout(ComponentName who, int userId, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRequiredStrongAuthTimeout(who, userId, parent);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void lockNow(int flags, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().lockNow(flags, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void wipeDataWithReason(int flags, String wipeReasonForUser) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeString(wipeReasonForUser);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().wipeDataWithReason(flags, wipeReasonForUser);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName setGlobalProxy(ComponentName admin, String proxySpec, String exclusionList) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setGlobalProxy(admin, proxySpec, exclusionList);
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

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName getGlobalProxyAdmin(int userHandle) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGlobalProxyAdmin(userHandle);
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

            @Override // android.app.admin.IDevicePolicyManager
            public void setRecommendedGlobalProxy(ComponentName admin, ProxyInfo proxyInfo) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRecommendedGlobalProxy(admin, proxyInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int setStorageEncryption(ComponentName who, boolean encrypt) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!encrypt) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setStorageEncryption(who, encrypt);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getStorageEncryption(ComponentName who, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStorageEncryption(who, userHandle);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getStorageEncryptionStatus(String callerPackage, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStorageEncryptionStatus(callerPackage, userHandle);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean requestBugreport(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().requestBugreport(who);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCameraDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCameraDisabled(who, disabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getCameraDisabled(ComponentName who, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCameraDisabled(who, userHandle);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setScreenCaptureDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setScreenCaptureDisabled(who, disabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getScreenCaptureDisabled(ComponentName who, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getScreenCaptureDisabled(who, userHandle);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setKeyguardDisabledFeatures(ComponentName who, int which, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(which);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setKeyguardDisabledFeatures(who, which, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getKeyguardDisabledFeatures(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getKeyguardDisabledFeatures(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setActiveAdmin(ComponentName policyReceiver, boolean refreshing, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!refreshing) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setActiveAdmin(policyReceiver, refreshing, userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isAdminActive(ComponentName policyReceiver, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAdminActive(policyReceiver, userHandle);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<ComponentName> getActiveAdmins(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActiveAdmins(userHandle);
                    }
                    _reply.readException();
                    List<ComponentName> _result = _reply.createTypedArrayList(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean packageHasActiveAdmins(String packageName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().packageHasActiveAdmins(packageName, userHandle);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void getRemoveWarning(ComponentName policyReceiver, RemoteCallback result, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getRemoveWarning(policyReceiver, result, userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void removeActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeActiveAdmin(policyReceiver, userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void forceRemoveActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceRemoveActiveAdmin(policyReceiver, userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean hasGrantedPolicy(ComponentName policyReceiver, int usesPolicy, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasGrantedPolicy(policyReceiver, usesPolicy, userHandle);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setActivePasswordState(PasswordMetrics metrics, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setActivePasswordState(metrics, userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportPasswordChanged(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportPasswordChanged(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportFailedPasswordAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportFailedPasswordAttempt(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportSuccessfulPasswordAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportSuccessfulPasswordAttempt(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportFailedBiometricAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportFailedBiometricAttempt(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportSuccessfulBiometricAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportSuccessfulBiometricAttempt(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportKeyguardDismissed(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportKeyguardDismissed(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportKeyguardSecured(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportKeyguardSecured(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setDeviceOwner(ComponentName who, String ownerName, int userId) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setDeviceOwner(who, ownerName, userId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName getDeviceOwnerComponent(boolean callingUserOnly) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callingUserOnly ? 1 : 0);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceOwnerComponent(callingUserOnly);
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

            @Override // android.app.admin.IDevicePolicyManager
            public boolean hasDeviceOwner() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasDeviceOwner();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getDeviceOwnerName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceOwnerName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void clearDeviceOwner(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearDeviceOwner(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getDeviceOwnerUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceOwnerUserId();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setProfileOwner(ComponentName who, String ownerName, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setProfileOwner(who, ownerName, userHandle);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName getProfileOwnerAsUser(int userHandle) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProfileOwnerAsUser(userHandle);
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

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName getProfileOwner(int userHandle) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProfileOwner(userHandle);
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

            @Override // android.app.admin.IDevicePolicyManager
            public String getProfileOwnerName(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProfileOwnerName(userHandle);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setProfileEnabled(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setProfileEnabled(who);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setProfileName(ComponentName who, String profileName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setProfileName(who, profileName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void clearProfileOwner(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearProfileOwner(who);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean hasUserSetupCompleted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasUserSetupCompleted();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean checkDeviceIdentifierAccess(String packageName, int pid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkDeviceIdentifierAccess(packageName, pid, uid);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setDeviceOwnerLockScreenInfo(ComponentName who, CharSequence deviceOwnerInfo) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDeviceOwnerLockScreenInfo(who, deviceOwnerInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getDeviceOwnerLockScreenInfo() throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceOwnerLockScreenInfo();
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

            @Override // android.app.admin.IDevicePolicyManager
            public String[] setPackagesSuspended(ComponentName admin, String callerPackage, String[] packageNames, boolean suspended) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeStringArray(packageNames);
                    if (!suspended) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPackagesSuspended(admin, callerPackage, packageNames, suspended);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isPackageSuspended(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageSuspended(admin, callerPackage, packageName);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean installCaCert(ComponentName admin, String callerPackage, byte[] certBuffer) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().installCaCert(admin, callerPackage, certBuffer);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void uninstallCaCerts(ComponentName admin, String callerPackage, String[] aliases) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().uninstallCaCerts(admin, callerPackage, aliases);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void enforceCanManageCaCerts(ComponentName admin, String callerPackage) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enforceCanManageCaCerts(admin, callerPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean approveCaCert(String alias, int userHandle, boolean approval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(userHandle);
                    _data.writeInt(approval ? 1 : 0);
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().approveCaCert(alias, userHandle, approval);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isCaCertApproved(String alias, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCaCertApproved(alias, userHandle);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean installKeyPair(ComponentName who, String callerPackage, byte[] privKeyBuffer, byte[] certBuffer, byte[] certChainBuffer, String alias, boolean requestAccess, boolean isUserSelectable) throws RemoteException {
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
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(callerPackage);
                    try {
                        _data.writeByteArray(privKeyBuffer);
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeByteArray(certBuffer);
                        _data.writeByteArray(certChainBuffer);
                        _data.writeString(alias);
                        _data.writeInt(requestAccess ? 1 : 0);
                        _data.writeInt(isUserSelectable ? 1 : 0);
                        boolean _status = this.mRemote.transact(90, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            boolean installKeyPair = Stub.getDefaultImpl().installKeyPair(who, callerPackage, privKeyBuffer, certBuffer, certChainBuffer, alias, requestAccess, isUserSelectable);
                            _reply.recycle();
                            _data.recycle();
                            return installKeyPair;
                        }
                        _reply.readException();
                        boolean _result = _reply.readInt() != 0;
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

            @Override // android.app.admin.IDevicePolicyManager
            public boolean removeKeyPair(ComponentName who, String callerPackage, String alias) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(91, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeKeyPair(who, callerPackage, alias);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean generateKeyPair(ComponentName who, String callerPackage, String algorithm, ParcelableKeyGenParameterSpec keySpec, int idAttestationFlags, KeymasterCertificateChain attestationChain) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(callerPackage);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(algorithm);
                    if (keySpec != null) {
                        _data.writeInt(1);
                        keySpec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(idAttestationFlags);
                    } catch (Throwable th3) {
                        th = th3;
                    }
                    try {
                        boolean _status = this.mRemote.transact(92, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            boolean generateKeyPair = Stub.getDefaultImpl().generateKeyPair(who, callerPackage, algorithm, keySpec, idAttestationFlags, attestationChain);
                            _reply.recycle();
                            _data.recycle();
                            return generateKeyPair;
                        }
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        if (_reply.readInt() != 0) {
                            try {
                                attestationChain.readFromParcel(_reply);
                            } catch (Throwable th4) {
                                th = th4;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setKeyPairCertificate(ComponentName who, String callerPackage, String alias, byte[] certBuffer, byte[] certChainBuffer, boolean isUserSelectable) throws RemoteException {
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
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(callerPackage);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(alias);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeByteArray(certBuffer);
                    try {
                        _data.writeByteArray(certChainBuffer);
                        _data.writeInt(isUserSelectable ? 1 : 0);
                    } catch (Throwable th4) {
                        th = th4;
                    }
                    try {
                        boolean _status = this.mRemote.transact(93, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            boolean keyPairCertificate = Stub.getDefaultImpl().setKeyPairCertificate(who, callerPackage, alias, certBuffer, certChainBuffer, isUserSelectable);
                            _reply.recycle();
                            _data.recycle();
                            return keyPairCertificate;
                        }
                        _reply.readException();
                        boolean _result = _reply.readInt() != 0;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void choosePrivateKeyAlias(int uid, Uri uri, String alias, IBinder aliasCallback) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(94, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().choosePrivateKeyAlias(uid, uri, alias, aliasCallback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setDelegatedScopes(ComponentName who, String delegatePackage, List<String> scopes) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(95, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDelegatedScopes(who, delegatePackage, scopes);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getDelegatedScopes(ComponentName who, String delegatePackage) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(96, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDelegatedScopes(who, delegatePackage);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getDelegatePackages(ComponentName who, String scope) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(97, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDelegatePackages(who, scope);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCertInstallerPackage(ComponentName who, String installerPackage) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(98, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCertInstallerPackage(who, installerPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getCertInstallerPackage(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(99, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCertInstallerPackage(who);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setAlwaysOnVpnPackage(ComponentName who, String vpnPackage, boolean lockdown, List<String> lockdownWhitelist) throws RemoteException {
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
                    _data.writeStringList(lockdownWhitelist);
                    boolean _status = this.mRemote.transact(100, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setAlwaysOnVpnPackage(who, vpnPackage, lockdown, lockdownWhitelist);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getAlwaysOnVpnPackage(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(101, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAlwaysOnVpnPackage(who);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isAlwaysOnVpnLockdownEnabled(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(102, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAlwaysOnVpnLockdownEnabled(who);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getAlwaysOnVpnLockdownWhitelist(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(103, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAlwaysOnVpnLockdownWhitelist(who);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void addPersistentPreferredActivity(ComponentName admin, IntentFilter filter, ComponentName activity) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(104, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addPersistentPreferredActivity(admin, filter, activity);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void clearPackagePersistentPreferredActivities(ComponentName admin, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(105, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearPackagePersistentPreferredActivities(admin, packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setDefaultSmsApplication(ComponentName admin, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(106, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDefaultSmsApplication(admin, packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setApplicationRestrictions(ComponentName who, String callerPackage, String packageName, Bundle settings) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(107, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setApplicationRestrictions(who, callerPackage, packageName, settings);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public Bundle getApplicationRestrictions(ComponentName who, String callerPackage, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(108, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getApplicationRestrictions(who, callerPackage, packageName);
                    }
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
            public boolean setApplicationRestrictionsManagingPackage(ComponentName admin, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(109, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setApplicationRestrictionsManagingPackage(admin, packageName);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getApplicationRestrictionsManagingPackage(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(110, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getApplicationRestrictionsManagingPackage(admin);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isCallerApplicationRestrictionsManagingPackage(String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    boolean _status = this.mRemote.transact(111, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCallerApplicationRestrictionsManagingPackage(callerPackage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setRestrictionsProvider(ComponentName who, ComponentName provider) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(112, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRestrictionsProvider(who, provider);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName getRestrictionsProvider(int userHandle) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(113, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRestrictionsProvider(userHandle);
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

            @Override // android.app.admin.IDevicePolicyManager
            public void setUserRestriction(ComponentName who, String key, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(key);
                    if (!enable) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(114, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserRestriction(who, key, enable);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public Bundle getUserRestrictions(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(115, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUserRestrictions(who);
                    }
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
            public void addCrossProfileIntentFilter(ComponentName admin, IntentFilter filter, int flags) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(116, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addCrossProfileIntentFilter(admin, filter, flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void clearCrossProfileIntentFilters(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(117, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearCrossProfileIntentFilters(admin);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setPermittedAccessibilityServices(ComponentName admin, List packageList) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(118, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPermittedAccessibilityServices(admin, packageList);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List getPermittedAccessibilityServices(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(119, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermittedAccessibilityServices(admin);
                    }
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
            public List getPermittedAccessibilityServicesForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(120, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermittedAccessibilityServicesForUser(userId);
                    }
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
            public boolean isAccessibilityServicePermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(121, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAccessibilityServicePermittedByAdmin(admin, packageName, userId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setPermittedInputMethods(ComponentName admin, List packageList) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(122, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPermittedInputMethods(admin, packageList);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List getPermittedInputMethods(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(123, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermittedInputMethods(admin);
                    }
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
            public List getPermittedInputMethodsForCurrentUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(124, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermittedInputMethodsForCurrentUser();
                    }
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
            public boolean isInputMethodPermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(125, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isInputMethodPermittedByAdmin(admin, packageName, userId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setPermittedCrossProfileNotificationListeners(ComponentName admin, List<String> packageList) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(126, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPermittedCrossProfileNotificationListeners(admin, packageList);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getPermittedCrossProfileNotificationListeners(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(127, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermittedCrossProfileNotificationListeners(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isNotificationListenerServicePermitted(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(128, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isNotificationListenerServicePermitted(packageName, userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public Intent createAdminSupportIntent(String restriction) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(restriction);
                    boolean _status = this.mRemote.transact(129, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().createAdminSupportIntent(restriction);
                    }
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
            public boolean setApplicationHidden(ComponentName admin, String callerPackage, String packageName, boolean hidden) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(130, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setApplicationHidden(admin, callerPackage, packageName, hidden);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isApplicationHidden(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(131, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isApplicationHidden(admin, callerPackage, packageName);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public UserHandle createAndManageUser(ComponentName who, String name, ComponentName profileOwner, PersistableBundle adminExtras, int flags) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(132, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().createAndManageUser(who, name, profileOwner, adminExtras, flags);
                    }
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
            public boolean removeUser(ComponentName who, UserHandle userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(133, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeUser(who, userHandle);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean switchUser(ComponentName who, UserHandle userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(134, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().switchUser(who, userHandle);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int startUserInBackground(ComponentName who, UserHandle userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(135, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startUserInBackground(who, userHandle);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int stopUser(ComponentName who, UserHandle userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(136, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopUser(who, userHandle);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int logoutUser(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(137, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().logoutUser(who);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<UserHandle> getSecondaryUsers(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(138, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSecondaryUsers(who);
                    }
                    _reply.readException();
                    List<UserHandle> _result = _reply.createTypedArrayList(UserHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void enableSystemApp(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(139, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enableSystemApp(admin, callerPackage, packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int enableSystemAppWithIntent(ComponentName admin, String callerPackage, Intent intent) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(140, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enableSystemAppWithIntent(admin, callerPackage, intent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean installExistingPackage(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(141, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().installExistingPackage(admin, callerPackage, packageName);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setAccountManagementDisabled(ComponentName who, String accountType, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(accountType);
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(142, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAccountManagementDisabled(who, accountType, disabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String[] getAccountTypesWithManagementDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(143, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAccountTypesWithManagementDisabled();
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String[] getAccountTypesWithManagementDisabledAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(144, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAccountTypesWithManagementDisabledAsUser(userId);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setLockTaskPackages(ComponentName who, String[] packages) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(145, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLockTaskPackages(who, packages);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String[] getLockTaskPackages(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(146, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLockTaskPackages(who);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isLockTaskPermitted(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean _status = this.mRemote.transact(147, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isLockTaskPermitted(pkg);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setLockTaskFeatures(ComponentName who, int flags) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(148, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLockTaskFeatures(who, flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getLockTaskFeatures(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(149, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLockTaskFeatures(who);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setGlobalSetting(ComponentName who, String setting, String value) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(150, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setGlobalSetting(who, setting, value);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setSystemSetting(ComponentName who, String setting, String value) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(151, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSystemSetting(who, setting, value);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setSecureSetting(ComponentName who, String setting, String value) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(152, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSecureSetting(who, setting, value);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setTime(ComponentName who, long millis) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(153, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setTime(who, millis);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setTimeZone(ComponentName who, String timeZone) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(154, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setTimeZone(who, timeZone);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setMasterVolumeMuted(ComponentName admin, boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!on) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(155, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMasterVolumeMuted(admin, on);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isMasterVolumeMuted(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(156, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMasterVolumeMuted(admin);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void notifyLockTaskModeChanged(boolean isEnabled, String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isEnabled ? 1 : 0);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(157, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyLockTaskModeChanged(isEnabled, pkg, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setUninstallBlocked(ComponentName admin, String callerPackage, String packageName, boolean uninstallBlocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    if (!uninstallBlocked) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(158, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUninstallBlocked(admin, callerPackage, packageName, uninstallBlocked);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isUninstallBlocked(ComponentName admin, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(159, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUninstallBlocked(admin, packageName);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCrossProfileCallerIdDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(160, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCrossProfileCallerIdDisabled(who, disabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getCrossProfileCallerIdDisabled(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(161, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileCallerIdDisabled(who);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getCrossProfileCallerIdDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(162, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileCallerIdDisabledForUser(userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCrossProfileContactsSearchDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(163, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCrossProfileContactsSearchDisabled(who, disabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getCrossProfileContactsSearchDisabled(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(164, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileContactsSearchDisabled(who);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getCrossProfileContactsSearchDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(165, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileContactsSearchDisabledForUser(userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void startManagedQuickContact(String lookupKey, long contactId, boolean isContactIdIgnored, long directoryId, Intent originalIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(lookupKey);
                        try {
                            _data.writeLong(contactId);
                            _data.writeInt(isContactIdIgnored ? 1 : 0);
                            _data.writeLong(directoryId);
                            if (originalIntent != null) {
                                _data.writeInt(1);
                                originalIntent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th) {
                            th = th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        boolean _status = this.mRemote.transact(166, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().startManagedQuickContact(lookupKey, contactId, isContactIdIgnored, directoryId, originalIntent);
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
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setBluetoothContactSharingDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(167, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBluetoothContactSharingDisabled(who, disabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getBluetoothContactSharingDisabled(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(168, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBluetoothContactSharingDisabled(who);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getBluetoothContactSharingDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(169, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBluetoothContactSharingDisabledForUser(userId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setTrustAgentConfiguration(ComponentName admin, ComponentName agent, PersistableBundle args, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
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
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(170, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTrustAgentConfiguration(admin, agent, args, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<PersistableBundle> getTrustAgentConfiguration(ComponentName admin, ComponentName agent, int userId, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
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
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(171, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTrustAgentConfiguration(admin, agent, userId, parent);
                    }
                    _reply.readException();
                    List<PersistableBundle> _result = _reply.createTypedArrayList(PersistableBundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean addCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(172, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addCrossProfileWidgetProvider(admin, packageName);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean removeCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(173, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeCrossProfileWidgetProvider(admin, packageName);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getCrossProfileWidgetProviders(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(174, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileWidgetProviders(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setAutoTimeRequired(ComponentName who, boolean required) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!required) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(175, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAutoTimeRequired(who, required);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getAutoTimeRequired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(176, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAutoTimeRequired();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setForceEphemeralUsers(ComponentName who, boolean forceEpehemeralUsers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!forceEpehemeralUsers) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(177, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setForceEphemeralUsers(who, forceEpehemeralUsers);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getForceEphemeralUsers(ComponentName who) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(178, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getForceEphemeralUsers(who);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isRemovingAdmin(ComponentName adminReceiver, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(179, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isRemovingAdmin(adminReceiver, userHandle);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setUserIcon(ComponentName admin, Bitmap icon) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(180, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserIcon(admin, icon);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setSystemUpdatePolicy(ComponentName who, SystemUpdatePolicy policy) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(181, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSystemUpdatePolicy(who, policy);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public SystemUpdatePolicy getSystemUpdatePolicy() throws RemoteException {
                SystemUpdatePolicy _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(182, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSystemUpdatePolicy();
                    }
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
            public void clearSystemUpdatePolicyFreezePeriodRecord() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(183, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearSystemUpdatePolicyFreezePeriodRecord();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setKeyguardDisabled(ComponentName admin, boolean disabled) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(184, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setKeyguardDisabled(admin, disabled);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setStatusBarDisabled(ComponentName who, boolean disabled) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(185, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setStatusBarDisabled(who, disabled);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getDoNotAskCredentialsOnBoot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(186, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDoNotAskCredentialsOnBoot();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void notifyPendingSystemUpdate(SystemUpdateInfo info) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(187, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyPendingSystemUpdate(info);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public SystemUpdateInfo getPendingSystemUpdate(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(188, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPendingSystemUpdate(admin);
                    }
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
            public void setPermissionPolicy(ComponentName admin, String callerPackage, int policy) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(189, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPermissionPolicy(admin, callerPackage, policy);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPermissionPolicy(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(190, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermissionPolicy(admin);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission, int grantState, RemoteCallback resultReceiver) throws RemoteException {
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
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(callerPackage);
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
                    _data.writeString(permission);
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(grantState);
                    if (resultReceiver != null) {
                        _data.writeInt(1);
                        resultReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(191, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPermissionGrantState(admin, callerPackage, packageName, permission, grantState, resultReceiver);
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(192, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermissionGrantState(admin, callerPackage, packageName, permission);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isProvisioningAllowed(String action, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(193, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isProvisioningAllowed(action, packageName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int checkProvisioningPreCondition(String action, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(194, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkProvisioningPreCondition(action, packageName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setKeepUninstalledPackages(ComponentName admin, String callerPackage, List<String> packageList) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(195, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setKeepUninstalledPackages(admin, callerPackage, packageList);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getKeepUninstalledPackages(ComponentName admin, String callerPackage) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(196, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getKeepUninstalledPackages(admin, callerPackage);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isManagedProfile(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(197, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isManagedProfile(admin);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isSystemOnlyUser(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(198, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSystemOnlyUser(admin);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getWifiMacAddress(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(199, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getWifiMacAddress(admin);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reboot(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(200, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reboot(admin);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setShortSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(201, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setShortSupportMessage(admin, message);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getShortSupportMessage(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(202, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getShortSupportMessage(admin);
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

            @Override // android.app.admin.IDevicePolicyManager
            public void setLongSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(203, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLongSupportMessage(admin, message);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getLongSupportMessage(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(204, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLongSupportMessage(admin);
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

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getShortSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(205, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getShortSupportMessageForUser(admin, userHandle);
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

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getLongSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(206, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLongSupportMessageForUser(admin, userHandle);
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

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isSeparateProfileChallengeAllowed(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(207, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSeparateProfileChallengeAllowed(userHandle);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setOrganizationColor(ComponentName admin, int color) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(208, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setOrganizationColor(admin, color);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setOrganizationColorForUser(int color, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(color);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(209, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setOrganizationColorForUser(color, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getOrganizationColor(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(210, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOrganizationColor(admin);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getOrganizationColorForUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(211, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOrganizationColorForUser(userHandle);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setOrganizationName(ComponentName admin, CharSequence title) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(212, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setOrganizationName(admin, title);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getOrganizationName(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(213, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOrganizationName(admin);
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

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getDeviceOwnerOrganizationName() throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(214, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceOwnerOrganizationName();
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

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getOrganizationNameForUser(int userHandle) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(215, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOrganizationNameForUser(userHandle);
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

            @Override // android.app.admin.IDevicePolicyManager
            public int getUserProvisioningState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(216, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUserProvisioningState();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setUserProvisioningState(int state, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(217, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserProvisioningState(state, userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setAffiliationIds(ComponentName admin, List<String> ids) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(218, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAffiliationIds(admin, ids);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getAffiliationIds(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(219, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAffiliationIds(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isAffiliatedUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(220, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAffiliatedUser();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setSecurityLoggingEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(221, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSecurityLoggingEnabled(admin, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isSecurityLoggingEnabled(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(222, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSecurityLoggingEnabled(admin);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ParceledListSlice retrieveSecurityLogs(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(223, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().retrieveSecurityLogs(admin);
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

            @Override // android.app.admin.IDevicePolicyManager
            public ParceledListSlice retrievePreRebootSecurityLogs(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(224, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().retrievePreRebootSecurityLogs(admin);
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

            @Override // android.app.admin.IDevicePolicyManager
            public long forceNetworkLogs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(225, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().forceNetworkLogs();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long forceSecurityLogs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(226, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().forceSecurityLogs();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isUninstallInQueue(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(227, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUninstallInQueue(packageName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void uninstallPackageWithActiveAdmins(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(228, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().uninstallPackageWithActiveAdmins(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isDeviceProvisioned() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(229, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDeviceProvisioned();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isDeviceProvisioningConfigApplied() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(230, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDeviceProvisioningConfigApplied();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setDeviceProvisioningConfigApplied() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(231, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDeviceProvisioningConfigApplied();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void forceUpdateUserSetupComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(232, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceUpdateUserSetupComplete();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setBackupServiceEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(233, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBackupServiceEnabled(admin, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isBackupServiceEnabled(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(234, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBackupServiceEnabled(admin);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setNetworkLoggingEnabled(ComponentName admin, String packageName, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(235, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNetworkLoggingEnabled(admin, packageName, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isNetworkLoggingEnabled(ComponentName admin, String packageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(236, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isNetworkLoggingEnabled(admin, packageName);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<NetworkEvent> retrieveNetworkLogs(ComponentName admin, String packageName, long batchToken) throws RemoteException {
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
                    _data.writeLong(batchToken);
                    boolean _status = this.mRemote.transact(237, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().retrieveNetworkLogs(admin, packageName, batchToken);
                    }
                    _reply.readException();
                    List<NetworkEvent> _result = _reply.createTypedArrayList(NetworkEvent.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean bindDeviceAdminServiceAsUser(ComponentName admin, IApplicationThread caller, IBinder token, Intent service, IServiceConnection connection, int flags, int targetUserId) throws RemoteException {
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
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(token);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(flags);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(targetUserId);
                    boolean _status = this.mRemote.transact(238, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean bindDeviceAdminServiceAsUser = Stub.getDefaultImpl().bindDeviceAdminServiceAsUser(admin, caller, token, service, connection, flags, targetUserId);
                        _reply.recycle();
                        _data.recycle();
                        return bindDeviceAdminServiceAsUser;
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
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(239, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBindDeviceAdminTargetUsers(admin);
                    }
                    _reply.readException();
                    List<UserHandle> _result = _reply.createTypedArrayList(UserHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isEphemeralUser(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(240, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isEphemeralUser(admin);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getLastSecurityLogRetrievalTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(241, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastSecurityLogRetrievalTime();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getLastBugReportRequestTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(242, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastBugReportRequestTime();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getLastNetworkLogRetrievalTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(243, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastNetworkLogRetrievalTime();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setResetPasswordToken(ComponentName admin, byte[] token) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(244, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setResetPasswordToken(admin, token);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean clearResetPasswordToken(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(245, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().clearResetPasswordToken(admin);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isResetPasswordTokenActive(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(246, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isResetPasswordTokenActive(admin);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean resetPasswordWithToken(ComponentName admin, String password, byte[] token, int flags) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(247, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resetPasswordWithToken(admin, password, token, flags);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isCurrentInputMethodSetByOwner() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(248, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCurrentInputMethodSetByOwner();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public StringParceledListSlice getOwnerInstalledCaCerts(UserHandle user) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(249, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOwnerInstalledCaCerts(user);
                    }
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
            public void clearApplicationUserData(ComponentName admin, String packageName, IPackageDataObserver callback) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(250, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearApplicationUserData(admin, packageName, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setLogoutEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(251, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLogoutEnabled(admin, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isLogoutEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(252, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isLogoutEnabled();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getDisallowedSystemApps(ComponentName admin, int userId, String provisioningAction) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(253, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDisallowedSystemApps(admin, userId, provisioningAction);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void transferOwnership(ComponentName admin, ComponentName target, PersistableBundle bundle) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(254, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().transferOwnership(admin, target, bundle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public PersistableBundle getTransferOwnershipBundle() throws RemoteException {
                PersistableBundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(255, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTransferOwnershipBundle();
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

            @Override // android.app.admin.IDevicePolicyManager
            public void setStartUserSessionMessage(ComponentName admin, CharSequence startUserSessionMessage) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(256, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setStartUserSessionMessage(admin, startUserSessionMessage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setEndUserSessionMessage(ComponentName admin, CharSequence endUserSessionMessage) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(257, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setEndUserSessionMessage(admin, endUserSessionMessage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getStartUserSessionMessage(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(258, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStartUserSessionMessage(admin);
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

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getEndUserSessionMessage(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(259, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEndUserSessionMessage(admin);
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

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> setMeteredDataDisabledPackages(ComponentName admin, List<String> packageNames) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(260, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setMeteredDataDisabledPackages(admin, packageNames);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getMeteredDataDisabledPackages(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(261, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMeteredDataDisabledPackages(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int addOverrideApn(ComponentName admin, ApnSetting apnSetting) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(262, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addOverrideApn(admin, apnSetting);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean updateOverrideApn(ComponentName admin, int apnId, ApnSetting apnSetting) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(263, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateOverrideApn(admin, apnId, apnSetting);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean removeOverrideApn(ComponentName admin, int apnId) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(264, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeOverrideApn(admin, apnId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<ApnSetting> getOverrideApns(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(265, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOverrideApns(admin);
                    }
                    _reply.readException();
                    List<ApnSetting> _result = _reply.createTypedArrayList(ApnSetting.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setOverrideApnsEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(266, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setOverrideApnsEnabled(admin, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isOverrideApnEnabled(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(267, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isOverrideApnEnabled(admin);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isMeteredDataDisabledPackageForUser(ComponentName admin, String packageName, int userId) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(268, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMeteredDataDisabledPackageForUser(admin, packageName, userId);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int setGlobalPrivateDns(ComponentName admin, int mode, String privateDnsHost) throws RemoteException {
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
                    _data.writeInt(mode);
                    _data.writeString(privateDnsHost);
                    boolean _status = this.mRemote.transact(269, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setGlobalPrivateDns(admin, mode, privateDnsHost);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getGlobalPrivateDnsMode(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(270, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGlobalPrivateDnsMode(admin);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getGlobalPrivateDnsHost(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(271, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGlobalPrivateDnsHost(admin);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void grantDeviceIdsAccessToProfileOwner(ComponentName who, int userId) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(272, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().grantDeviceIdsAccessToProfileOwner(who, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void installUpdateFromFile(ComponentName admin, ParcelFileDescriptor updateFileDescriptor, StartInstallingUpdateCallback listener) throws RemoteException {
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
                    if (updateFileDescriptor != null) {
                        _data.writeInt(1);
                        updateFileDescriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(273, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().installUpdateFromFile(admin, updateFileDescriptor, listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCrossProfileCalendarPackages(ComponentName admin, List<String> packageNames) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(274, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCrossProfileCalendarPackages(admin, packageNames);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getCrossProfileCalendarPackages(ComponentName admin) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(275, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileCalendarPackages(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isPackageAllowedToAccessCalendarForUser(String packageName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(276, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageAllowedToAccessCalendarForUser(packageName, userHandle);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getCrossProfileCalendarPackagesForUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(277, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileCalendarPackagesForUser(userHandle);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isManagedKiosk() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(278, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isManagedKiosk();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isUnattendedManagedKiosk() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(279, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUnattendedManagedKiosk();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean startViewCalendarEventInManagedProfile(String packageName, long eventId, long start, long end, boolean allDay, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(packageName);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeLong(eventId);
                    _data.writeLong(start);
                    _data.writeLong(end);
                    _data.writeInt(allDay ? 1 : 0);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(280, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean startViewCalendarEventInManagedProfile = Stub.getDefaultImpl().startViewCalendarEventInManagedProfile(packageName, eventId, start, end, allDay, flags);
                        _reply.recycle();
                        _data.recycle();
                        return startViewCalendarEventInManagedProfile;
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }
        }

        private boolean onTransact$installKeyPair$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            byte[] _arg2 = data.createByteArray();
            byte[] _arg3 = data.createByteArray();
            byte[] _arg4 = data.createByteArray();
            String _arg5 = data.readString();
            boolean _arg6 = data.readInt() != 0;
            boolean _arg7 = data.readInt() != 0;
            boolean installKeyPair = installKeyPair(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
            reply.writeNoException();
            reply.writeInt(installKeyPair ? 1 : 0);
            return true;
        }

        private boolean onTransact$setKeyPairCertificate$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            byte[] _arg3 = data.createByteArray();
            byte[] _arg4 = data.createByteArray();
            boolean _arg5 = data.readInt() != 0;
            boolean keyPairCertificate = setKeyPairCertificate(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            reply.writeInt(keyPairCertificate ? 1 : 0);
            return true;
        }

        private boolean onTransact$setPermissionGrantState$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            RemoteCallback _arg5;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            String _arg3 = data.readString();
            int _arg4 = data.readInt();
            if (data.readInt() != 0) {
                _arg5 = RemoteCallback.CREATOR.createFromParcel(data);
            } else {
                _arg5 = null;
            }
            setPermissionGrantState(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$bindDeviceAdminServiceAsUser$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            Intent _arg3;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            IApplicationThread _arg1 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            IBinder _arg2 = data.readStrongBinder();
            if (data.readInt() != 0) {
                _arg3 = Intent.CREATOR.createFromParcel(data);
            } else {
                _arg3 = null;
            }
            IServiceConnection _arg4 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
            int _arg5 = data.readInt();
            int _arg6 = data.readInt();
            boolean bindDeviceAdminServiceAsUser = bindDeviceAdminServiceAsUser(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
            reply.writeNoException();
            reply.writeInt(bindDeviceAdminServiceAsUser ? 1 : 0);
            return true;
        }

        private boolean onTransact$startViewCalendarEventInManagedProfile$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            long _arg1 = data.readLong();
            long _arg2 = data.readLong();
            long _arg3 = data.readLong();
            boolean _arg4 = data.readInt() != 0;
            int _arg5 = data.readInt();
            boolean startViewCalendarEventInManagedProfile = startViewCalendarEventInManagedProfile(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            reply.writeInt(startViewCalendarEventInManagedProfile ? 1 : 0);
            return true;
        }

        public static boolean setDefaultImpl(IDevicePolicyManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IDevicePolicyManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
