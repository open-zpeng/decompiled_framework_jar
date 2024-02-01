package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.NetworkStats;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.WorkSource;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telephony.CarrierRestrictionRules;
import android.telephony.CellInfo;
import android.telephony.ClientRequestStats;
import android.telephony.ICellInfoCallback;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.NeighboringCellInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.PhoneNumberRange;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyHistogram;
import android.telephony.UiccCardInfo;
import android.telephony.UiccSlotInfo;
import android.telephony.VisualVoicemailSmsFilterSettings;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsConfigCallback;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsRcsFeature;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.IImsRegistrationCallback;
import com.android.ims.internal.IImsServiceFeatureCallback;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.INumberVerificationCallback;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public interface ITelephony extends IInterface {
    void cacheMmTelCapabilityProvisioning(int i, int i2, int i3, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void call(String str, String str2) throws RemoteException;

    boolean canChangeDtmfToneLength(int i, String str) throws RemoteException;

    void carrierActionReportDefaultNetworkStatus(int i, boolean z) throws RemoteException;

    void carrierActionResetAll(int i) throws RemoteException;

    void carrierActionSetMeteredApnsEnabled(int i, boolean z) throws RemoteException;

    void carrierActionSetRadioEnabled(int i, boolean z) throws RemoteException;

    int checkCarrierPrivilegesForPackage(int i, String str) throws RemoteException;

    int checkCarrierPrivilegesForPackageAnyPhone(String str) throws RemoteException;

    @UnsupportedAppUsage
    void dial(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean disableDataConnectivity() throws RemoteException;

    void disableIms(int i) throws RemoteException;

    @UnsupportedAppUsage
    void disableLocationUpdates() throws RemoteException;

    void disableLocationUpdatesForSubscriber(int i) throws RemoteException;

    void disableVisualVoicemailSmsFilter(String str, int i) throws RemoteException;

    boolean doesSwitchMultiSimConfigTriggerReboot(int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean enableDataConnectivity() throws RemoteException;

    void enableIms(int i) throws RemoteException;

    @UnsupportedAppUsage
    void enableLocationUpdates() throws RemoteException;

    void enableLocationUpdatesForSubscriber(int i) throws RemoteException;

    boolean enableModemForSlot(int i, boolean z) throws RemoteException;

    void enableVideoCalling(boolean z) throws RemoteException;

    void enableVisualVoicemailSmsFilter(String str, int i, VisualVoicemailSmsFilterSettings visualVoicemailSmsFilterSettings) throws RemoteException;

    void enqueueSmsPickResult(String str, IIntegerConsumer iIntegerConsumer) throws RemoteException;

    void factoryReset(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getActivePhoneType() throws RemoteException;

    int getActivePhoneTypeForSlot(int i) throws RemoteException;

    VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int i) throws RemoteException;

    String getAidForAppType(int i, int i2) throws RemoteException;

    List<CellInfo> getAllCellInfo(String str) throws RemoteException;

    CarrierRestrictionRules getAllowedCarriers() throws RemoteException;

    int getCalculatedPreferredNetworkType(String str) throws RemoteException;

    @UnsupportedAppUsage
    int getCallState() throws RemoteException;

    int getCallStateForSlot(int i) throws RemoteException;

    int getCardIdForDefaultEuicc(int i, String str) throws RemoteException;

    int getCarrierIdFromMccMnc(int i, String str, boolean z) throws RemoteException;

    int getCarrierIdListVersion(int i) throws RemoteException;

    List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int i) throws RemoteException;

    int getCarrierPrivilegeStatus(int i) throws RemoteException;

    int getCarrierPrivilegeStatusForUid(int i, int i2) throws RemoteException;

    int getCdmaEriIconIndex(String str) throws RemoteException;

    int getCdmaEriIconIndexForSubscriber(int i, String str) throws RemoteException;

    int getCdmaEriIconMode(String str) throws RemoteException;

    int getCdmaEriIconModeForSubscriber(int i, String str) throws RemoteException;

    String getCdmaEriText(String str) throws RemoteException;

    String getCdmaEriTextForSubscriber(int i, String str) throws RemoteException;

    String getCdmaMdn(int i) throws RemoteException;

    String getCdmaMin(int i) throws RemoteException;

    String getCdmaPrlVersion(int i) throws RemoteException;

    int getCdmaRoamingMode(int i) throws RemoteException;

    Bundle getCellLocation(String str) throws RemoteException;

    CellNetworkScanResult getCellNetworkScanResults(int i, String str) throws RemoteException;

    List<String> getCertsFromCarrierPrivilegeAccessRules(int i) throws RemoteException;

    List<ClientRequestStats> getClientRequestStats(String str, int i) throws RemoteException;

    int getDataActivationState(int i, String str) throws RemoteException;

    int getDataActivity() throws RemoteException;

    int getDataActivityForSubId(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean getDataEnabled(int i) throws RemoteException;

    int getDataNetworkType(String str) throws RemoteException;

    int getDataNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    int getDataState() throws RemoteException;

    int getDataStateForSubId(int i) throws RemoteException;

    String getDefaultSmsApp(int i) throws RemoteException;

    String getDeviceId(String str) throws RemoteException;

    String getDeviceSoftwareVersionForSlot(int i, String str) throws RemoteException;

    boolean getEmergencyCallbackMode(int i) throws RemoteException;

    Map getEmergencyNumberList(String str) throws RemoteException;

    List<String> getEmergencyNumberListTestMode() throws RemoteException;

    String getEsn(int i) throws RemoteException;

    String[] getForbiddenPlmns(int i, int i2, String str) throws RemoteException;

    String getImeiForSlot(int i, String str) throws RemoteException;

    IImsConfig getImsConfig(int i, int i2) throws RemoteException;

    int getImsProvisioningInt(int i, int i2) throws RemoteException;

    boolean getImsProvisioningStatusForCapability(int i, int i2, int i3) throws RemoteException;

    String getImsProvisioningString(int i, int i2) throws RemoteException;

    int getImsRegTechnologyForMmTel(int i) throws RemoteException;

    IImsRegistration getImsRegistration(int i, int i2) throws RemoteException;

    String getImsService(int i, boolean z) throws RemoteException;

    String getLine1AlphaTagForDisplay(int i, String str) throws RemoteException;

    String getLine1NumberForDisplay(int i, String str) throws RemoteException;

    int getLteOnCdmaMode(String str) throws RemoteException;

    int getLteOnCdmaModeForSubscriber(int i, String str) throws RemoteException;

    String getManufacturerCodeForSlot(int i) throws RemoteException;

    String getMeidForSlot(int i, String str) throws RemoteException;

    String[] getMergedSubscriberIds(int i, String str) throws RemoteException;

    String[] getMergedSubscriberIdsFromGroup(int i, String str) throws RemoteException;

    IImsMmTelFeature getMmTelFeatureAndListen(int i, IImsServiceFeatureCallback iImsServiceFeatureCallback) throws RemoteException;

    String getMmsUAProfUrl(int i) throws RemoteException;

    String getMmsUserAgent(int i) throws RemoteException;

    List<NeighboringCellInfo> getNeighboringCellInfo(String str) throws RemoteException;

    String getNetworkCountryIsoForPhone(int i) throws RemoteException;

    int getNetworkSelectionMode(int i) throws RemoteException;

    int getNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    int getNumberOfModemsWithSimultaneousDataConnections(int i, String str) throws RemoteException;

    List<String> getPackagesWithCarrierPrivileges(int i) throws RemoteException;

    List<String> getPackagesWithCarrierPrivilegesForAllPhones() throws RemoteException;

    String[] getPcscfAddress(String str, String str2) throws RemoteException;

    PhoneAccountHandle getPhoneAccountHandleForSubscriptionId(int i) throws RemoteException;

    int getPreferredNetworkType(int i) throws RemoteException;

    int getRadioAccessFamily(int i, String str) throws RemoteException;

    int getRadioHalVersion() throws RemoteException;

    int getRadioPowerState(int i, String str) throws RemoteException;

    IImsRcsFeature getRcsFeatureAndListen(int i, IImsServiceFeatureCallback iImsServiceFeatureCallback) throws RemoteException;

    ServiceState getServiceStateForSubscriber(int i, String str) throws RemoteException;

    SignalStrength getSignalStrength(int i) throws RemoteException;

    String getSimLocaleForSubscriber(int i) throws RemoteException;

    int[] getSlotsMapping() throws RemoteException;

    String[] getSmsApps(int i) throws RemoteException;

    int getSubIdForPhoneAccount(PhoneAccount phoneAccount) throws RemoteException;

    int getSubscriptionCarrierId(int i) throws RemoteException;

    String getSubscriptionCarrierName(int i) throws RemoteException;

    int getSubscriptionSpecificCarrierId(int i) throws RemoteException;

    String getSubscriptionSpecificCarrierName(int i) throws RemoteException;

    List<TelephonyHistogram> getTelephonyHistograms() throws RemoteException;

    boolean getTetherApnRequiredForSubscriber(int i) throws RemoteException;

    String getTypeAllocationCodeForSlot(int i) throws RemoteException;

    List<UiccCardInfo> getUiccCardsInfo(String str) throws RemoteException;

    UiccSlotInfo[] getUiccSlotsInfo() throws RemoteException;

    String getVisualVoicemailPackageName(String str, int i) throws RemoteException;

    Bundle getVisualVoicemailSettings(String str, int i) throws RemoteException;

    VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(String str, int i) throws RemoteException;

    int getVoWiFiModeSetting(int i) throws RemoteException;

    int getVoWiFiRoamingModeSetting(int i) throws RemoteException;

    int getVoiceActivationState(int i, String str) throws RemoteException;

    int getVoiceMessageCountForSubscriber(int i, String str) throws RemoteException;

    int getVoiceNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    Uri getVoicemailRingtoneUri(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    NetworkStats getVtDataUsage(int i, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    boolean handlePinMmi(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean handlePinMmiForSubscriber(int i, String str) throws RemoteException;

    void handleUssdRequest(int i, String str, ResultReceiver resultReceiver) throws RemoteException;

    @UnsupportedAppUsage
    boolean hasIccCard() throws RemoteException;

    boolean hasIccCardUsingSlotIndex(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean iccCloseLogicalChannel(int i, int i2) throws RemoteException;

    boolean iccCloseLogicalChannelBySlot(int i, int i2) throws RemoteException;

    byte[] iccExchangeSimIO(int i, int i2, int i3, int i4, int i5, int i6, String str) throws RemoteException;

    IccOpenLogicalChannelResponse iccOpenLogicalChannel(int i, String str, String str2, int i2) throws RemoteException;

    IccOpenLogicalChannelResponse iccOpenLogicalChannelBySlot(int i, String str, String str2, int i2) throws RemoteException;

    String iccTransmitApduBasicChannel(int i, String str, int i2, int i3, int i4, int i5, int i6, String str2) throws RemoteException;

    String iccTransmitApduBasicChannelBySlot(int i, String str, int i2, int i3, int i4, int i5, int i6, String str2) throws RemoteException;

    @UnsupportedAppUsage
    String iccTransmitApduLogicalChannel(int i, int i2, int i3, int i4, int i5, int i6, int i7, String str) throws RemoteException;

    String iccTransmitApduLogicalChannelBySlot(int i, int i2, int i3, int i4, int i5, int i6, int i7, String str) throws RemoteException;

    int invokeOemRilRequestRaw(byte[] bArr, byte[] bArr2) throws RemoteException;

    boolean isAdvancedCallingSettingEnabled(int i) throws RemoteException;

    boolean isApnMetered(int i, int i2) throws RemoteException;

    boolean isAvailable(int i, int i2, int i3) throws RemoteException;

    boolean isCapable(int i, int i2, int i3) throws RemoteException;

    boolean isConcurrentVoiceAndDataAllowed(int i) throws RemoteException;

    boolean isDataAllowedInVoiceCall(int i) throws RemoteException;

    boolean isDataConnectivityPossible(int i) throws RemoteException;

    boolean isDataEnabled(int i) throws RemoteException;

    boolean isDataEnabledForApn(int i, int i2, String str) throws RemoteException;

    boolean isDataRoamingEnabled(int i) throws RemoteException;

    boolean isEmergencyNumber(String str, boolean z) throws RemoteException;

    boolean isHearingAidCompatibilitySupported() throws RemoteException;

    boolean isImsRegistered(int i) throws RemoteException;

    boolean isInEmergencySmsMode() throws RemoteException;

    boolean isManualNetworkSelectionAllowed(int i) throws RemoteException;

    boolean isMmTelCapabilityProvisionedInCache(int i, int i2, int i3) throws RemoteException;

    boolean isModemEnabledForSlot(int i, String str) throws RemoteException;

    int isMultiSimSupported(String str) throws RemoteException;

    boolean isRadioOn(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean isRadioOnForSubscriber(int i, String str) throws RemoteException;

    boolean isRttSupported(int i) throws RemoteException;

    boolean isTtyModeSupported() throws RemoteException;

    boolean isTtyOverVolteEnabled(int i) throws RemoteException;

    boolean isUserDataEnabled(int i) throws RemoteException;

    boolean isVideoCallingEnabled(String str) throws RemoteException;

    boolean isVideoTelephonyAvailable(int i) throws RemoteException;

    boolean isVoWiFiRoamingSettingEnabled(int i) throws RemoteException;

    boolean isVoWiFiSettingEnabled(int i) throws RemoteException;

    boolean isVoicemailVibrationEnabled(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    boolean isVtSettingEnabled(int i) throws RemoteException;

    boolean isWifiCallingAvailable(int i) throws RemoteException;

    boolean isWorldPhone(int i, String str) throws RemoteException;

    boolean needMobileRadioShutdown() throws RemoteException;

    boolean needsOtaServiceProvisioning() throws RemoteException;

    String nvReadItem(int i) throws RemoteException;

    boolean nvWriteCdmaPrl(byte[] bArr) throws RemoteException;

    boolean nvWriteItem(int i, String str) throws RemoteException;

    boolean rebootModem(int i) throws RemoteException;

    void refreshUiccProfile(int i) throws RemoteException;

    void registerImsProvisioningChangedCallback(int i, IImsConfigCallback iImsConfigCallback) throws RemoteException;

    void registerImsRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) throws RemoteException;

    void registerMmTelCapabilityCallback(int i, IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    void requestCellInfoUpdate(int i, ICellInfoCallback iCellInfoCallback, String str) throws RemoteException;

    void requestCellInfoUpdateWithWorkSource(int i, ICellInfoCallback iCellInfoCallback, String str, WorkSource workSource) throws RemoteException;

    void requestModemActivityInfo(ResultReceiver resultReceiver) throws RemoteException;

    int requestNetworkScan(int i, NetworkScanRequest networkScanRequest, Messenger messenger, IBinder iBinder, String str) throws RemoteException;

    void requestNumberVerification(PhoneNumberRange phoneNumberRange, long j, INumberVerificationCallback iNumberVerificationCallback, String str) throws RemoteException;

    boolean resetModemConfig(int i) throws RemoteException;

    void sendDialerSpecialCode(String str, String str2) throws RemoteException;

    String sendEnvelopeWithStatus(int i, String str) throws RemoteException;

    void sendVisualVoicemailSmsForSubscriber(String str, int i, String str2, int i2, String str3, PendingIntent pendingIntent) throws RemoteException;

    void setAdvancedCallingSettingEnabled(int i, boolean z) throws RemoteException;

    int setAllowedCarriers(CarrierRestrictionRules carrierRestrictionRules) throws RemoteException;

    void setCarrierTestOverride(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) throws RemoteException;

    boolean setCdmaRoamingMode(int i, int i2) throws RemoteException;

    boolean setCdmaSubscriptionMode(int i, int i2) throws RemoteException;

    void setCellInfoListRate(int i) throws RemoteException;

    void setDataActivationState(int i, int i2) throws RemoteException;

    boolean setDataAllowedDuringVoiceCall(int i, boolean z) throws RemoteException;

    void setDataRoamingEnabled(int i, boolean z) throws RemoteException;

    void setDefaultSmsApp(int i, String str) throws RemoteException;

    int setImsProvisioningInt(int i, int i2, int i3) throws RemoteException;

    void setImsProvisioningStatusForCapability(int i, int i2, int i3, boolean z) throws RemoteException;

    int setImsProvisioningString(int i, int i2, String str) throws RemoteException;

    void setImsRegistrationState(boolean z) throws RemoteException;

    boolean setImsService(int i, boolean z, String str) throws RemoteException;

    boolean setLine1NumberForDisplayForSubscriber(int i, String str, String str2) throws RemoteException;

    void setMultiSimCarrierRestriction(boolean z) throws RemoteException;

    void setNetworkSelectionModeAutomatic(int i) throws RemoteException;

    boolean setNetworkSelectionModeManual(int i, OperatorInfo operatorInfo, boolean z) throws RemoteException;

    boolean setOperatorBrandOverride(int i, String str) throws RemoteException;

    void setPolicyDataEnabled(boolean z, int i) throws RemoteException;

    boolean setPreferredNetworkType(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    boolean setRadio(boolean z) throws RemoteException;

    void setRadioCapability(RadioAccessFamily[] radioAccessFamilyArr) throws RemoteException;

    boolean setRadioForSubscriber(int i, boolean z) throws RemoteException;

    void setRadioIndicationUpdateMode(int i, int i2, int i3) throws RemoteException;

    boolean setRadioPower(boolean z) throws RemoteException;

    boolean setRoamingOverride(int i, List<String> list, List<String> list2, List<String> list3, List<String> list4) throws RemoteException;

    void setRttCapabilitySetting(int i, boolean z) throws RemoteException;

    void setSimPowerStateForSlot(int i, int i2) throws RemoteException;

    void setUserDataEnabled(int i, boolean z) throws RemoteException;

    void setVoWiFiModeSetting(int i, int i2) throws RemoteException;

    void setVoWiFiNonPersistent(int i, boolean z, int i2) throws RemoteException;

    void setVoWiFiRoamingModeSetting(int i, int i2) throws RemoteException;

    void setVoWiFiRoamingSettingEnabled(int i, boolean z) throws RemoteException;

    void setVoWiFiSettingEnabled(int i, boolean z) throws RemoteException;

    void setVoiceActivationState(int i, int i2) throws RemoteException;

    boolean setVoiceMailNumber(int i, String str, String str2) throws RemoteException;

    void setVoicemailRingtoneUri(String str, PhoneAccountHandle phoneAccountHandle, Uri uri) throws RemoteException;

    void setVoicemailVibrationEnabled(String str, PhoneAccountHandle phoneAccountHandle, boolean z) throws RemoteException;

    void setVtSettingEnabled(int i, boolean z) throws RemoteException;

    void shutdownMobileRadios() throws RemoteException;

    void stopNetworkScan(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    boolean supplyPin(String str) throws RemoteException;

    boolean supplyPinForSubscriber(int i, String str) throws RemoteException;

    int[] supplyPinReportResult(String str) throws RemoteException;

    int[] supplyPinReportResultForSubscriber(int i, String str) throws RemoteException;

    boolean supplyPuk(String str, String str2) throws RemoteException;

    boolean supplyPukForSubscriber(int i, String str, String str2) throws RemoteException;

    int[] supplyPukReportResult(String str, String str2) throws RemoteException;

    int[] supplyPukReportResultForSubscriber(int i, String str, String str2) throws RemoteException;

    void switchMultiSimConfig(int i) throws RemoteException;

    boolean switchSlots(int[] iArr) throws RemoteException;

    @UnsupportedAppUsage
    void toggleRadioOnOff() throws RemoteException;

    void toggleRadioOnOffForSubscriber(int i) throws RemoteException;

    void unregisterImsProvisioningChangedCallback(int i, IImsConfigCallback iImsConfigCallback) throws RemoteException;

    void unregisterImsRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) throws RemoteException;

    void unregisterMmTelCapabilityCallback(int i, IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    void updateEmergencyNumberListTestMode(int i, EmergencyNumber emergencyNumber) throws RemoteException;

    @UnsupportedAppUsage
    void updateServiceLocation() throws RemoteException;

    void updateServiceLocationForSubscriber(int i) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements ITelephony {
        @Override // com.android.internal.telephony.ITelephony
        public void dial(String number) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void call(String callingPackage, String number) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isRadioOn(String callingPackage) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isRadioOnForSubscriber(int subId, String callingPackage) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean supplyPin(String pin) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean supplyPinForSubscriber(int subId, String pin) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean supplyPuk(String puk, String pin) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean supplyPukForSubscriber(int subId, String puk, String pin) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int[] supplyPinReportResult(String pin) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int[] supplyPinReportResultForSubscriber(int subId, String pin) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int[] supplyPukReportResult(String puk, String pin) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int[] supplyPukReportResultForSubscriber(int subId, String puk, String pin) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean handlePinMmi(String dialString) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void handleUssdRequest(int subId, String ussdRequest, ResultReceiver wrappedCallback) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean handlePinMmiForSubscriber(int subId, String dialString) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void toggleRadioOnOff() throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void toggleRadioOnOffForSubscriber(int subId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setRadio(boolean turnOn) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setRadioForSubscriber(int subId, boolean turnOn) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setRadioPower(boolean turnOn) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void updateServiceLocation() throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void updateServiceLocationForSubscriber(int subId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void enableLocationUpdates() throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void enableLocationUpdatesForSubscriber(int subId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void disableLocationUpdates() throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void disableLocationUpdatesForSubscriber(int subId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean enableDataConnectivity() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean disableDataConnectivity() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isDataConnectivityPossible(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public Bundle getCellLocation(String callingPkg) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getNetworkCountryIsoForPhone(int phoneId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public List<NeighboringCellInfo> getNeighboringCellInfo(String callingPkg) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCallState() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCallStateForSlot(int slotIndex) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getDataActivity() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getDataActivityForSubId(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getDataState() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getDataStateForSubId(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getActivePhoneType() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getActivePhoneTypeForSlot(int slotIndex) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCdmaEriIconIndex(String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCdmaEriIconIndexForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCdmaEriIconMode(String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCdmaEriIconModeForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getCdmaEriText(String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getCdmaEriTextForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean needsOtaServiceProvisioning() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setVoiceMailNumber(int subId, String alphaTag, String number) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setVoiceActivationState(int subId, int activationState) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setDataActivationState(int subId, int activationState) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getVoiceActivationState(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getDataActivationState(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getVoiceMessageCountForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isConcurrentVoiceAndDataAllowed(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public Bundle getVisualVoicemailSettings(String callingPackage, int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getVisualVoicemailPackageName(String callingPackage, int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void enableVisualVoicemailSmsFilter(String callingPackage, int subId, VisualVoicemailSmsFilterSettings settings) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void disableVisualVoicemailSmsFilter(String callingPackage, int subId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(String callingPackage, int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void sendVisualVoicemailSmsForSubscriber(String callingPackage, int subId, String number, int port, String text, PendingIntent sentIntent) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void sendDialerSpecialCode(String callingPackageName, String inputCode) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getDataNetworkType(String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getDataNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getVoiceNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean hasIccCard() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean hasIccCardUsingSlotIndex(int slotIndex) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getLteOnCdmaMode(String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getLteOnCdmaModeForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public List<CellInfo> getAllCellInfo(String callingPkg) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void requestCellInfoUpdate(int subId, ICellInfoCallback cb, String callingPkg) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void requestCellInfoUpdateWithWorkSource(int subId, ICellInfoCallback cb, String callingPkg, WorkSource ws) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setCellInfoListRate(int rateInMillis) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public IccOpenLogicalChannelResponse iccOpenLogicalChannelBySlot(int slotIndex, String callingPackage, String AID, int p2) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public IccOpenLogicalChannelResponse iccOpenLogicalChannel(int subId, String callingPackage, String AID, int p2) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean iccCloseLogicalChannelBySlot(int slotIndex, int channel) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean iccCloseLogicalChannel(int subId, int channel) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String iccTransmitApduLogicalChannelBySlot(int slotIndex, int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String iccTransmitApduLogicalChannel(int subId, int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String iccTransmitApduBasicChannelBySlot(int slotIndex, String callingPackage, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String iccTransmitApduBasicChannel(int subId, String callingPackage, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public byte[] iccExchangeSimIO(int subId, int fileID, int command, int p1, int p2, int p3, String filePath) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String sendEnvelopeWithStatus(int subId, String content) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String nvReadItem(int itemID) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean nvWriteItem(int itemID, String itemValue) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean nvWriteCdmaPrl(byte[] preferredRoamingList) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean resetModemConfig(int slotIndex) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean rebootModem(int slotIndex) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCalculatedPreferredNetworkType(String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getPreferredNetworkType(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean getTetherApnRequiredForSubscriber(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void enableIms(int slotId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void disableIms(int slotId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public IImsMmTelFeature getMmTelFeatureAndListen(int slotId, IImsServiceFeatureCallback callback) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public IImsRcsFeature getRcsFeatureAndListen(int slotId, IImsServiceFeatureCallback callback) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public IImsRegistration getImsRegistration(int slotId, int feature) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public IImsConfig getImsConfig(int slotId, int feature) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setImsService(int slotId, boolean isCarrierImsService, String packageName) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getImsService(int slotId, boolean isCarrierImsService) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setNetworkSelectionModeAutomatic(int subId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public CellNetworkScanResult getCellNetworkScanResults(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int requestNetworkScan(int subId, NetworkScanRequest request, Messenger messenger, IBinder binder, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void stopNetworkScan(int subId, int scanId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setNetworkSelectionModeManual(int subId, OperatorInfo operatorInfo, boolean persisSelection) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setPreferredNetworkType(int subId, int networkType) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setUserDataEnabled(int subId, boolean enable) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean getDataEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isUserDataEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isDataEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isManualNetworkSelectionAllowed(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String[] getPcscfAddress(String apnType, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setImsRegistrationState(boolean registered) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getCdmaMdn(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getCdmaMin(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void requestNumberVerification(PhoneNumberRange range, long timeoutMillis, INumberVerificationCallback callback, String callingPackage) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCarrierPrivilegeStatus(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCarrierPrivilegeStatusForUid(int subId, int uid) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int checkCarrierPrivilegesForPackage(int subId, String pkgName) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int checkCarrierPrivilegesForPackageAnyPhone(String pkgName) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int phoneId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setLine1NumberForDisplayForSubscriber(int subId, String alphaTag, String number) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getLine1NumberForDisplay(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getLine1AlphaTagForDisplay(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String[] getMergedSubscriberIds(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String[] getMergedSubscriberIdsFromGroup(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setOperatorBrandOverride(int subId, String brand) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setRoamingOverride(int subId, List<String> gsmRoamingList, List<String> gsmNonRoamingList, List<String> cdmaRoamingList, List<String> cdmaNonRoamingList) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean needMobileRadioShutdown() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void shutdownMobileRadios() throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setRadioCapability(RadioAccessFamily[] rafs) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getRadioAccessFamily(int phoneId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void enableVideoCalling(boolean enable) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isVideoCallingEnabled(String callingPackage) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean canChangeDtmfToneLength(int subId, String callingPackage) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isWorldPhone(int subId, String callingPackage) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isTtyModeSupported() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isRttSupported(int subscriptionId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isHearingAidCompatibilitySupported() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isImsRegistered(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isWifiCallingAvailable(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isVideoTelephonyAvailable(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getImsRegTechnologyForMmTel(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getDeviceId(String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getImeiForSlot(int slotIndex, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getTypeAllocationCodeForSlot(int slotIndex) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getMeidForSlot(int slotIndex, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getManufacturerCodeForSlot(int slotIndex) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getDeviceSoftwareVersionForSlot(int slotIndex, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getSubIdForPhoneAccount(PhoneAccount phoneAccount) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public PhoneAccountHandle getPhoneAccountHandleForSubscriptionId(int subscriptionId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void factoryReset(int subId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getSimLocaleForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void requestModemActivityInfo(ResultReceiver result) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public ServiceState getServiceStateForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public Uri getVoicemailRingtoneUri(PhoneAccountHandle accountHandle) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setVoicemailRingtoneUri(String callingPackage, PhoneAccountHandle phoneAccountHandle, Uri uri) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isVoicemailVibrationEnabled(PhoneAccountHandle accountHandle) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setVoicemailVibrationEnabled(String callingPackage, PhoneAccountHandle phoneAccountHandle, boolean enabled) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public List<String> getPackagesWithCarrierPrivileges(int phoneId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public List<String> getPackagesWithCarrierPrivilegesForAllPhones() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getAidForAppType(int subId, int appType) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getEsn(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getCdmaPrlVersion(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public List<TelephonyHistogram> getTelephonyHistograms() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int setAllowedCarriers(CarrierRestrictionRules carrierRestrictionRules) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public CarrierRestrictionRules getAllowedCarriers() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getSubscriptionCarrierId(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getSubscriptionCarrierName(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getSubscriptionSpecificCarrierId(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getSubscriptionSpecificCarrierName(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCarrierIdFromMccMnc(int slotIndex, String mccmnc, boolean isSubscriptionMccMnc) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void carrierActionSetMeteredApnsEnabled(int subId, boolean visible) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void carrierActionSetRadioEnabled(int subId, boolean enabled) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void carrierActionReportDefaultNetworkStatus(int subId, boolean report) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void carrierActionResetAll(int subId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public NetworkStats getVtDataUsage(int subId, boolean perUidStats) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setPolicyDataEnabled(boolean enabled, int subId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public List<ClientRequestStats> getClientRequestStats(String callingPackage, int subid) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setSimPowerStateForSlot(int slotIndex, int state) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public String[] getForbiddenPlmns(int subId, int appType, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean getEmergencyCallbackMode(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public SignalStrength getSignalStrength(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCardIdForDefaultEuicc(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public List<UiccCardInfo> getUiccCardsInfo(String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public UiccSlotInfo[] getUiccSlotsInfo() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean switchSlots(int[] physicalSlots) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setRadioIndicationUpdateMode(int subId, int filters, int mode) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isDataRoamingEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setDataRoamingEnabled(int subId, boolean isEnabled) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCdmaRoamingMode(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setCdmaRoamingMode(int subId, int mode) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setCdmaSubscriptionMode(int subId, int mode) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setCarrierTestOverride(int subId, String mccmnc, String imsi, String iccid, String gid1, String gid2, String plmn, String spn, String carrierPrivilegeRules, String apn) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getCarrierIdListVersion(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void refreshUiccProfile(int subId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getNumberOfModemsWithSimultaneousDataConnections(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getNetworkSelectionMode(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isInEmergencySmsMode() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String[] getSmsApps(int userId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getDefaultSmsApp(int userId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setDefaultSmsApp(int userId, String packageName) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getRadioPowerState(int slotIndex, String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void registerImsRegistrationCallback(int subId, IImsRegistrationCallback c) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void unregisterImsRegistrationCallback(int subId, IImsRegistrationCallback c) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void registerMmTelCapabilityCallback(int subId, IImsCapabilityCallback c) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void unregisterMmTelCapabilityCallback(int subId, IImsCapabilityCallback c) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isCapable(int subId, int capability, int regTech) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isAvailable(int subId, int capability, int regTech) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isAdvancedCallingSettingEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setAdvancedCallingSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isVtSettingEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setVtSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isVoWiFiSettingEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setVoWiFiSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isVoWiFiRoamingSettingEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setVoWiFiRoamingSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setVoWiFiNonPersistent(int subId, boolean isCapable, int mode) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getVoWiFiModeSetting(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setVoWiFiModeSetting(int subId, int mode) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getVoWiFiRoamingModeSetting(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setVoWiFiRoamingModeSetting(int subId, int mode) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setRttCapabilitySetting(int subId, boolean isEnabled) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isTtyOverVolteEnabled(int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public Map getEmergencyNumberList(String callingPackage) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isEmergencyNumber(String number, boolean exactMatch) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public List<String> getCertsFromCarrierPrivilegeAccessRules(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void registerImsProvisioningChangedCallback(int subId, IImsConfigCallback callback) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void unregisterImsProvisioningChangedCallback(int subId, IImsConfigCallback callback) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setImsProvisioningStatusForCapability(int subId, int capability, int tech, boolean isProvisioned) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean getImsProvisioningStatusForCapability(int subId, int capability, int tech) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isMmTelCapabilityProvisionedInCache(int subId, int capability, int tech) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void cacheMmTelCapabilityProvisioning(int subId, int capability, int tech, boolean isProvisioned) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getImsProvisioningInt(int subId, int key) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getImsProvisioningString(int subId, int key) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int setImsProvisioningInt(int subId, int key, int value) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int setImsProvisioningString(int subId, int key, String value) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void updateEmergencyNumberListTestMode(int action, EmergencyNumber num) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public List<String> getEmergencyNumberListTestMode() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean enableModemForSlot(int slotIndex, boolean enable) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void setMultiSimCarrierRestriction(boolean isMultiSimCarrierRestricted) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public int isMultiSimSupported(String callingPackage) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void switchMultiSimConfig(int numOfSims) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean doesSwitchMultiSimConfigTriggerReboot(int subId, String callingPackage) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int[] getSlotsMapping() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public int getRadioHalVersion() throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isModemEnabledForSlot(int slotIndex, String callingPackage) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isDataEnabledForApn(int apnType, int subId, String callingPackage) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isApnMetered(int apnType, int subId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public void enqueueSmsPickResult(String callingPackage, IIntegerConsumer subIdResult) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getMmsUserAgent(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public String getMmsUAProfUrl(int subId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean setDataAllowedDuringVoiceCall(int subId, boolean allow) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ITelephony
        public boolean isDataAllowedInVoiceCall(int subId) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ITelephony {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ITelephony";
        static final int TRANSACTION_cacheMmTelCapabilityProvisioning = 234;
        static final int TRANSACTION_call = 2;
        static final int TRANSACTION_canChangeDtmfToneLength = 136;
        static final int TRANSACTION_carrierActionReportDefaultNetworkStatus = 176;
        static final int TRANSACTION_carrierActionResetAll = 177;
        static final int TRANSACTION_carrierActionSetMeteredApnsEnabled = 174;
        static final int TRANSACTION_carrierActionSetRadioEnabled = 175;
        static final int TRANSACTION_checkCarrierPrivilegesForPackage = 119;
        static final int TRANSACTION_checkCarrierPrivilegesForPackageAnyPhone = 120;
        static final int TRANSACTION_dial = 1;
        static final int TRANSACTION_disableDataConnectivity = 28;
        static final int TRANSACTION_disableIms = 94;
        static final int TRANSACTION_disableLocationUpdates = 25;
        static final int TRANSACTION_disableLocationUpdatesForSubscriber = 26;
        static final int TRANSACTION_disableVisualVoicemailSmsFilter = 58;
        static final int TRANSACTION_doesSwitchMultiSimConfigTriggerReboot = 245;
        static final int TRANSACTION_enableDataConnectivity = 27;
        static final int TRANSACTION_enableIms = 93;
        static final int TRANSACTION_enableLocationUpdates = 23;
        static final int TRANSACTION_enableLocationUpdatesForSubscriber = 24;
        static final int TRANSACTION_enableModemForSlot = 241;
        static final int TRANSACTION_enableVideoCalling = 134;
        static final int TRANSACTION_enableVisualVoicemailSmsFilter = 57;
        static final int TRANSACTION_enqueueSmsPickResult = 251;
        static final int TRANSACTION_factoryReset = 153;
        static final int TRANSACTION_getActivePhoneType = 39;
        static final int TRANSACTION_getActivePhoneTypeForSlot = 40;
        static final int TRANSACTION_getActiveVisualVoicemailSmsFilterSettings = 60;
        static final int TRANSACTION_getAidForAppType = 163;
        static final int TRANSACTION_getAllCellInfo = 71;
        static final int TRANSACTION_getAllowedCarriers = 168;
        static final int TRANSACTION_getCalculatedPreferredNetworkType = 90;
        static final int TRANSACTION_getCallState = 33;
        static final int TRANSACTION_getCallStateForSlot = 34;
        static final int TRANSACTION_getCardIdForDefaultEuicc = 185;
        static final int TRANSACTION_getCarrierIdFromMccMnc = 173;
        static final int TRANSACTION_getCarrierIdListVersion = 196;
        static final int TRANSACTION_getCarrierPackageNamesForIntentAndPhone = 121;
        static final int TRANSACTION_getCarrierPrivilegeStatus = 117;
        static final int TRANSACTION_getCarrierPrivilegeStatusForUid = 118;
        static final int TRANSACTION_getCdmaEriIconIndex = 41;
        static final int TRANSACTION_getCdmaEriIconIndexForSubscriber = 42;
        static final int TRANSACTION_getCdmaEriIconMode = 43;
        static final int TRANSACTION_getCdmaEriIconModeForSubscriber = 44;
        static final int TRANSACTION_getCdmaEriText = 45;
        static final int TRANSACTION_getCdmaEriTextForSubscriber = 46;
        static final int TRANSACTION_getCdmaMdn = 114;
        static final int TRANSACTION_getCdmaMin = 115;
        static final int TRANSACTION_getCdmaPrlVersion = 165;
        static final int TRANSACTION_getCdmaRoamingMode = 192;
        static final int TRANSACTION_getCellLocation = 30;
        static final int TRANSACTION_getCellNetworkScanResults = 102;
        static final int TRANSACTION_getCertsFromCarrierPrivilegeAccessRules = 228;
        static final int TRANSACTION_getClientRequestStats = 180;
        static final int TRANSACTION_getDataActivationState = 52;
        static final int TRANSACTION_getDataActivity = 35;
        static final int TRANSACTION_getDataActivityForSubId = 36;
        static final int TRANSACTION_getDataEnabled = 108;
        static final int TRANSACTION_getDataNetworkType = 64;
        static final int TRANSACTION_getDataNetworkTypeForSubscriber = 65;
        static final int TRANSACTION_getDataState = 37;
        static final int TRANSACTION_getDataStateForSubId = 38;
        static final int TRANSACTION_getDefaultSmsApp = 202;
        static final int TRANSACTION_getDeviceId = 145;
        static final int TRANSACTION_getDeviceSoftwareVersionForSlot = 150;
        static final int TRANSACTION_getEmergencyCallbackMode = 183;
        static final int TRANSACTION_getEmergencyNumberList = 226;
        static final int TRANSACTION_getEmergencyNumberListTestMode = 240;
        static final int TRANSACTION_getEsn = 164;
        static final int TRANSACTION_getForbiddenPlmns = 182;
        static final int TRANSACTION_getImeiForSlot = 146;
        static final int TRANSACTION_getImsConfig = 98;
        static final int TRANSACTION_getImsProvisioningInt = 235;
        static final int TRANSACTION_getImsProvisioningStatusForCapability = 232;
        static final int TRANSACTION_getImsProvisioningString = 236;
        static final int TRANSACTION_getImsRegTechnologyForMmTel = 144;
        static final int TRANSACTION_getImsRegistration = 97;
        static final int TRANSACTION_getImsService = 100;
        static final int TRANSACTION_getLine1AlphaTagForDisplay = 124;
        static final int TRANSACTION_getLine1NumberForDisplay = 123;
        static final int TRANSACTION_getLteOnCdmaMode = 69;
        static final int TRANSACTION_getLteOnCdmaModeForSubscriber = 70;
        static final int TRANSACTION_getManufacturerCodeForSlot = 149;
        static final int TRANSACTION_getMeidForSlot = 148;
        static final int TRANSACTION_getMergedSubscriberIds = 125;
        static final int TRANSACTION_getMergedSubscriberIdsFromGroup = 126;
        static final int TRANSACTION_getMmTelFeatureAndListen = 95;
        static final int TRANSACTION_getMmsUAProfUrl = 253;
        static final int TRANSACTION_getMmsUserAgent = 252;
        static final int TRANSACTION_getNeighboringCellInfo = 32;
        static final int TRANSACTION_getNetworkCountryIsoForPhone = 31;
        static final int TRANSACTION_getNetworkSelectionMode = 199;
        static final int TRANSACTION_getNetworkTypeForSubscriber = 63;
        static final int TRANSACTION_getNumberOfModemsWithSimultaneousDataConnections = 198;
        static final int TRANSACTION_getPackagesWithCarrierPrivileges = 161;
        static final int TRANSACTION_getPackagesWithCarrierPrivilegesForAllPhones = 162;
        static final int TRANSACTION_getPcscfAddress = 112;
        static final int TRANSACTION_getPhoneAccountHandleForSubscriptionId = 152;
        static final int TRANSACTION_getPreferredNetworkType = 91;
        static final int TRANSACTION_getRadioAccessFamily = 133;
        static final int TRANSACTION_getRadioHalVersion = 247;
        static final int TRANSACTION_getRadioPowerState = 204;
        static final int TRANSACTION_getRcsFeatureAndListen = 96;
        static final int TRANSACTION_getServiceStateForSubscriber = 156;
        static final int TRANSACTION_getSignalStrength = 184;
        static final int TRANSACTION_getSimLocaleForSubscriber = 154;
        static final int TRANSACTION_getSlotsMapping = 246;
        static final int TRANSACTION_getSmsApps = 201;
        static final int TRANSACTION_getSubIdForPhoneAccount = 151;
        static final int TRANSACTION_getSubscriptionCarrierId = 169;
        static final int TRANSACTION_getSubscriptionCarrierName = 170;
        static final int TRANSACTION_getSubscriptionSpecificCarrierId = 171;
        static final int TRANSACTION_getSubscriptionSpecificCarrierName = 172;
        static final int TRANSACTION_getTelephonyHistograms = 166;
        static final int TRANSACTION_getTetherApnRequiredForSubscriber = 92;
        static final int TRANSACTION_getTypeAllocationCodeForSlot = 147;
        static final int TRANSACTION_getUiccCardsInfo = 186;
        static final int TRANSACTION_getUiccSlotsInfo = 187;
        static final int TRANSACTION_getVisualVoicemailPackageName = 56;
        static final int TRANSACTION_getVisualVoicemailSettings = 55;
        static final int TRANSACTION_getVisualVoicemailSmsFilterSettings = 59;
        static final int TRANSACTION_getVoWiFiModeSetting = 220;
        static final int TRANSACTION_getVoWiFiRoamingModeSetting = 222;
        static final int TRANSACTION_getVoiceActivationState = 51;
        static final int TRANSACTION_getVoiceMessageCountForSubscriber = 53;
        static final int TRANSACTION_getVoiceNetworkTypeForSubscriber = 66;
        static final int TRANSACTION_getVoicemailRingtoneUri = 157;
        static final int TRANSACTION_getVtDataUsage = 178;
        static final int TRANSACTION_handlePinMmi = 13;
        static final int TRANSACTION_handlePinMmiForSubscriber = 15;
        static final int TRANSACTION_handleUssdRequest = 14;
        static final int TRANSACTION_hasIccCard = 67;
        static final int TRANSACTION_hasIccCardUsingSlotIndex = 68;
        static final int TRANSACTION_iccCloseLogicalChannel = 78;
        static final int TRANSACTION_iccCloseLogicalChannelBySlot = 77;
        static final int TRANSACTION_iccExchangeSimIO = 83;
        static final int TRANSACTION_iccOpenLogicalChannel = 76;
        static final int TRANSACTION_iccOpenLogicalChannelBySlot = 75;
        static final int TRANSACTION_iccTransmitApduBasicChannel = 82;
        static final int TRANSACTION_iccTransmitApduBasicChannelBySlot = 81;
        static final int TRANSACTION_iccTransmitApduLogicalChannel = 80;
        static final int TRANSACTION_iccTransmitApduLogicalChannelBySlot = 79;
        static final int TRANSACTION_invokeOemRilRequestRaw = 129;
        static final int TRANSACTION_isAdvancedCallingSettingEnabled = 211;
        static final int TRANSACTION_isApnMetered = 250;
        static final int TRANSACTION_isAvailable = 210;
        static final int TRANSACTION_isCapable = 209;
        static final int TRANSACTION_isConcurrentVoiceAndDataAllowed = 54;
        static final int TRANSACTION_isDataAllowedInVoiceCall = 255;
        static final int TRANSACTION_isDataConnectivityPossible = 29;
        static final int TRANSACTION_isDataEnabled = 110;
        static final int TRANSACTION_isDataEnabledForApn = 249;
        static final int TRANSACTION_isDataRoamingEnabled = 190;
        static final int TRANSACTION_isEmergencyNumber = 227;
        static final int TRANSACTION_isHearingAidCompatibilitySupported = 140;
        static final int TRANSACTION_isImsRegistered = 141;
        static final int TRANSACTION_isInEmergencySmsMode = 200;
        static final int TRANSACTION_isManualNetworkSelectionAllowed = 111;
        static final int TRANSACTION_isMmTelCapabilityProvisionedInCache = 233;
        static final int TRANSACTION_isModemEnabledForSlot = 248;
        static final int TRANSACTION_isMultiSimSupported = 243;
        static final int TRANSACTION_isRadioOn = 3;
        static final int TRANSACTION_isRadioOnForSubscriber = 4;
        static final int TRANSACTION_isRttSupported = 139;
        static final int TRANSACTION_isTtyModeSupported = 138;
        static final int TRANSACTION_isTtyOverVolteEnabled = 225;
        static final int TRANSACTION_isUserDataEnabled = 109;
        static final int TRANSACTION_isVideoCallingEnabled = 135;
        static final int TRANSACTION_isVideoTelephonyAvailable = 143;
        static final int TRANSACTION_isVoWiFiRoamingSettingEnabled = 217;
        static final int TRANSACTION_isVoWiFiSettingEnabled = 215;
        static final int TRANSACTION_isVoicemailVibrationEnabled = 159;
        static final int TRANSACTION_isVtSettingEnabled = 213;
        static final int TRANSACTION_isWifiCallingAvailable = 142;
        static final int TRANSACTION_isWorldPhone = 137;
        static final int TRANSACTION_needMobileRadioShutdown = 130;
        static final int TRANSACTION_needsOtaServiceProvisioning = 47;
        static final int TRANSACTION_nvReadItem = 85;
        static final int TRANSACTION_nvWriteCdmaPrl = 87;
        static final int TRANSACTION_nvWriteItem = 86;
        static final int TRANSACTION_rebootModem = 89;
        static final int TRANSACTION_refreshUiccProfile = 197;
        static final int TRANSACTION_registerImsProvisioningChangedCallback = 229;
        static final int TRANSACTION_registerImsRegistrationCallback = 205;
        static final int TRANSACTION_registerMmTelCapabilityCallback = 207;
        static final int TRANSACTION_requestCellInfoUpdate = 72;
        static final int TRANSACTION_requestCellInfoUpdateWithWorkSource = 73;
        static final int TRANSACTION_requestModemActivityInfo = 155;
        static final int TRANSACTION_requestNetworkScan = 103;
        static final int TRANSACTION_requestNumberVerification = 116;
        static final int TRANSACTION_resetModemConfig = 88;
        static final int TRANSACTION_sendDialerSpecialCode = 62;
        static final int TRANSACTION_sendEnvelopeWithStatus = 84;
        static final int TRANSACTION_sendVisualVoicemailSmsForSubscriber = 61;
        static final int TRANSACTION_setAdvancedCallingSettingEnabled = 212;
        static final int TRANSACTION_setAllowedCarriers = 167;
        static final int TRANSACTION_setCarrierTestOverride = 195;
        static final int TRANSACTION_setCdmaRoamingMode = 193;
        static final int TRANSACTION_setCdmaSubscriptionMode = 194;
        static final int TRANSACTION_setCellInfoListRate = 74;
        static final int TRANSACTION_setDataActivationState = 50;
        static final int TRANSACTION_setDataAllowedDuringVoiceCall = 254;
        static final int TRANSACTION_setDataRoamingEnabled = 191;
        static final int TRANSACTION_setDefaultSmsApp = 203;
        static final int TRANSACTION_setImsProvisioningInt = 237;
        static final int TRANSACTION_setImsProvisioningStatusForCapability = 231;
        static final int TRANSACTION_setImsProvisioningString = 238;
        static final int TRANSACTION_setImsRegistrationState = 113;
        static final int TRANSACTION_setImsService = 99;
        static final int TRANSACTION_setLine1NumberForDisplayForSubscriber = 122;
        static final int TRANSACTION_setMultiSimCarrierRestriction = 242;
        static final int TRANSACTION_setNetworkSelectionModeAutomatic = 101;
        static final int TRANSACTION_setNetworkSelectionModeManual = 105;
        static final int TRANSACTION_setOperatorBrandOverride = 127;
        static final int TRANSACTION_setPolicyDataEnabled = 179;
        static final int TRANSACTION_setPreferredNetworkType = 106;
        static final int TRANSACTION_setRadio = 18;
        static final int TRANSACTION_setRadioCapability = 132;
        static final int TRANSACTION_setRadioForSubscriber = 19;
        static final int TRANSACTION_setRadioIndicationUpdateMode = 189;
        static final int TRANSACTION_setRadioPower = 20;
        static final int TRANSACTION_setRoamingOverride = 128;
        static final int TRANSACTION_setRttCapabilitySetting = 224;
        static final int TRANSACTION_setSimPowerStateForSlot = 181;
        static final int TRANSACTION_setUserDataEnabled = 107;
        static final int TRANSACTION_setVoWiFiModeSetting = 221;
        static final int TRANSACTION_setVoWiFiNonPersistent = 219;
        static final int TRANSACTION_setVoWiFiRoamingModeSetting = 223;
        static final int TRANSACTION_setVoWiFiRoamingSettingEnabled = 218;
        static final int TRANSACTION_setVoWiFiSettingEnabled = 216;
        static final int TRANSACTION_setVoiceActivationState = 49;
        static final int TRANSACTION_setVoiceMailNumber = 48;
        static final int TRANSACTION_setVoicemailRingtoneUri = 158;
        static final int TRANSACTION_setVoicemailVibrationEnabled = 160;
        static final int TRANSACTION_setVtSettingEnabled = 214;
        static final int TRANSACTION_shutdownMobileRadios = 131;
        static final int TRANSACTION_stopNetworkScan = 104;
        static final int TRANSACTION_supplyPin = 5;
        static final int TRANSACTION_supplyPinForSubscriber = 6;
        static final int TRANSACTION_supplyPinReportResult = 9;
        static final int TRANSACTION_supplyPinReportResultForSubscriber = 10;
        static final int TRANSACTION_supplyPuk = 7;
        static final int TRANSACTION_supplyPukForSubscriber = 8;
        static final int TRANSACTION_supplyPukReportResult = 11;
        static final int TRANSACTION_supplyPukReportResultForSubscriber = 12;
        static final int TRANSACTION_switchMultiSimConfig = 244;
        static final int TRANSACTION_switchSlots = 188;
        static final int TRANSACTION_toggleRadioOnOff = 16;
        static final int TRANSACTION_toggleRadioOnOffForSubscriber = 17;
        static final int TRANSACTION_unregisterImsProvisioningChangedCallback = 230;
        static final int TRANSACTION_unregisterImsRegistrationCallback = 206;
        static final int TRANSACTION_unregisterMmTelCapabilityCallback = 208;
        static final int TRANSACTION_updateEmergencyNumberListTestMode = 239;
        static final int TRANSACTION_updateServiceLocation = 21;
        static final int TRANSACTION_updateServiceLocationForSubscriber = 22;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITelephony asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITelephony)) {
                return (ITelephony) iin;
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
                    return "dial";
                case 2:
                    return "call";
                case 3:
                    return "isRadioOn";
                case 4:
                    return "isRadioOnForSubscriber";
                case 5:
                    return "supplyPin";
                case 6:
                    return "supplyPinForSubscriber";
                case 7:
                    return "supplyPuk";
                case 8:
                    return "supplyPukForSubscriber";
                case 9:
                    return "supplyPinReportResult";
                case 10:
                    return "supplyPinReportResultForSubscriber";
                case 11:
                    return "supplyPukReportResult";
                case 12:
                    return "supplyPukReportResultForSubscriber";
                case 13:
                    return "handlePinMmi";
                case 14:
                    return "handleUssdRequest";
                case 15:
                    return "handlePinMmiForSubscriber";
                case 16:
                    return "toggleRadioOnOff";
                case 17:
                    return "toggleRadioOnOffForSubscriber";
                case 18:
                    return "setRadio";
                case 19:
                    return "setRadioForSubscriber";
                case 20:
                    return "setRadioPower";
                case 21:
                    return "updateServiceLocation";
                case 22:
                    return "updateServiceLocationForSubscriber";
                case 23:
                    return "enableLocationUpdates";
                case 24:
                    return "enableLocationUpdatesForSubscriber";
                case 25:
                    return "disableLocationUpdates";
                case 26:
                    return "disableLocationUpdatesForSubscriber";
                case 27:
                    return "enableDataConnectivity";
                case 28:
                    return "disableDataConnectivity";
                case 29:
                    return "isDataConnectivityPossible";
                case 30:
                    return "getCellLocation";
                case 31:
                    return "getNetworkCountryIsoForPhone";
                case 32:
                    return "getNeighboringCellInfo";
                case 33:
                    return "getCallState";
                case 34:
                    return "getCallStateForSlot";
                case 35:
                    return "getDataActivity";
                case 36:
                    return "getDataActivityForSubId";
                case 37:
                    return "getDataState";
                case 38:
                    return "getDataStateForSubId";
                case 39:
                    return "getActivePhoneType";
                case 40:
                    return "getActivePhoneTypeForSlot";
                case 41:
                    return "getCdmaEriIconIndex";
                case 42:
                    return "getCdmaEriIconIndexForSubscriber";
                case 43:
                    return "getCdmaEriIconMode";
                case 44:
                    return "getCdmaEriIconModeForSubscriber";
                case 45:
                    return "getCdmaEriText";
                case 46:
                    return "getCdmaEriTextForSubscriber";
                case 47:
                    return "needsOtaServiceProvisioning";
                case 48:
                    return "setVoiceMailNumber";
                case 49:
                    return "setVoiceActivationState";
                case 50:
                    return "setDataActivationState";
                case 51:
                    return "getVoiceActivationState";
                case 52:
                    return "getDataActivationState";
                case 53:
                    return "getVoiceMessageCountForSubscriber";
                case 54:
                    return "isConcurrentVoiceAndDataAllowed";
                case 55:
                    return "getVisualVoicemailSettings";
                case 56:
                    return "getVisualVoicemailPackageName";
                case 57:
                    return "enableVisualVoicemailSmsFilter";
                case 58:
                    return "disableVisualVoicemailSmsFilter";
                case 59:
                    return "getVisualVoicemailSmsFilterSettings";
                case 60:
                    return "getActiveVisualVoicemailSmsFilterSettings";
                case 61:
                    return "sendVisualVoicemailSmsForSubscriber";
                case 62:
                    return "sendDialerSpecialCode";
                case 63:
                    return "getNetworkTypeForSubscriber";
                case 64:
                    return "getDataNetworkType";
                case 65:
                    return "getDataNetworkTypeForSubscriber";
                case 66:
                    return "getVoiceNetworkTypeForSubscriber";
                case 67:
                    return "hasIccCard";
                case 68:
                    return "hasIccCardUsingSlotIndex";
                case 69:
                    return "getLteOnCdmaMode";
                case 70:
                    return "getLteOnCdmaModeForSubscriber";
                case 71:
                    return "getAllCellInfo";
                case 72:
                    return "requestCellInfoUpdate";
                case 73:
                    return "requestCellInfoUpdateWithWorkSource";
                case 74:
                    return "setCellInfoListRate";
                case 75:
                    return "iccOpenLogicalChannelBySlot";
                case 76:
                    return "iccOpenLogicalChannel";
                case 77:
                    return "iccCloseLogicalChannelBySlot";
                case 78:
                    return "iccCloseLogicalChannel";
                case 79:
                    return "iccTransmitApduLogicalChannelBySlot";
                case 80:
                    return "iccTransmitApduLogicalChannel";
                case 81:
                    return "iccTransmitApduBasicChannelBySlot";
                case 82:
                    return "iccTransmitApduBasicChannel";
                case 83:
                    return "iccExchangeSimIO";
                case 84:
                    return "sendEnvelopeWithStatus";
                case 85:
                    return "nvReadItem";
                case 86:
                    return "nvWriteItem";
                case 87:
                    return "nvWriteCdmaPrl";
                case 88:
                    return "resetModemConfig";
                case 89:
                    return "rebootModem";
                case 90:
                    return "getCalculatedPreferredNetworkType";
                case 91:
                    return "getPreferredNetworkType";
                case 92:
                    return "getTetherApnRequiredForSubscriber";
                case 93:
                    return "enableIms";
                case 94:
                    return "disableIms";
                case 95:
                    return "getMmTelFeatureAndListen";
                case 96:
                    return "getRcsFeatureAndListen";
                case 97:
                    return "getImsRegistration";
                case 98:
                    return "getImsConfig";
                case 99:
                    return "setImsService";
                case 100:
                    return "getImsService";
                case 101:
                    return "setNetworkSelectionModeAutomatic";
                case 102:
                    return "getCellNetworkScanResults";
                case 103:
                    return "requestNetworkScan";
                case 104:
                    return "stopNetworkScan";
                case 105:
                    return "setNetworkSelectionModeManual";
                case 106:
                    return "setPreferredNetworkType";
                case 107:
                    return "setUserDataEnabled";
                case 108:
                    return "getDataEnabled";
                case 109:
                    return "isUserDataEnabled";
                case 110:
                    return "isDataEnabled";
                case 111:
                    return "isManualNetworkSelectionAllowed";
                case 112:
                    return "getPcscfAddress";
                case 113:
                    return "setImsRegistrationState";
                case 114:
                    return "getCdmaMdn";
                case 115:
                    return "getCdmaMin";
                case 116:
                    return "requestNumberVerification";
                case 117:
                    return "getCarrierPrivilegeStatus";
                case 118:
                    return "getCarrierPrivilegeStatusForUid";
                case 119:
                    return "checkCarrierPrivilegesForPackage";
                case 120:
                    return "checkCarrierPrivilegesForPackageAnyPhone";
                case 121:
                    return "getCarrierPackageNamesForIntentAndPhone";
                case 122:
                    return "setLine1NumberForDisplayForSubscriber";
                case 123:
                    return "getLine1NumberForDisplay";
                case 124:
                    return "getLine1AlphaTagForDisplay";
                case 125:
                    return "getMergedSubscriberIds";
                case 126:
                    return "getMergedSubscriberIdsFromGroup";
                case 127:
                    return "setOperatorBrandOverride";
                case 128:
                    return "setRoamingOverride";
                case 129:
                    return "invokeOemRilRequestRaw";
                case 130:
                    return "needMobileRadioShutdown";
                case 131:
                    return "shutdownMobileRadios";
                case 132:
                    return "setRadioCapability";
                case 133:
                    return "getRadioAccessFamily";
                case 134:
                    return "enableVideoCalling";
                case 135:
                    return "isVideoCallingEnabled";
                case 136:
                    return "canChangeDtmfToneLength";
                case 137:
                    return "isWorldPhone";
                case 138:
                    return "isTtyModeSupported";
                case 139:
                    return "isRttSupported";
                case 140:
                    return "isHearingAidCompatibilitySupported";
                case 141:
                    return "isImsRegistered";
                case 142:
                    return "isWifiCallingAvailable";
                case 143:
                    return "isVideoTelephonyAvailable";
                case 144:
                    return "getImsRegTechnologyForMmTel";
                case 145:
                    return "getDeviceId";
                case 146:
                    return "getImeiForSlot";
                case 147:
                    return "getTypeAllocationCodeForSlot";
                case 148:
                    return "getMeidForSlot";
                case 149:
                    return "getManufacturerCodeForSlot";
                case 150:
                    return "getDeviceSoftwareVersionForSlot";
                case 151:
                    return "getSubIdForPhoneAccount";
                case 152:
                    return "getPhoneAccountHandleForSubscriptionId";
                case 153:
                    return "factoryReset";
                case 154:
                    return "getSimLocaleForSubscriber";
                case 155:
                    return "requestModemActivityInfo";
                case 156:
                    return "getServiceStateForSubscriber";
                case 157:
                    return "getVoicemailRingtoneUri";
                case 158:
                    return "setVoicemailRingtoneUri";
                case 159:
                    return "isVoicemailVibrationEnabled";
                case 160:
                    return "setVoicemailVibrationEnabled";
                case 161:
                    return "getPackagesWithCarrierPrivileges";
                case 162:
                    return "getPackagesWithCarrierPrivilegesForAllPhones";
                case 163:
                    return "getAidForAppType";
                case 164:
                    return "getEsn";
                case 165:
                    return "getCdmaPrlVersion";
                case 166:
                    return "getTelephonyHistograms";
                case 167:
                    return "setAllowedCarriers";
                case 168:
                    return "getAllowedCarriers";
                case 169:
                    return "getSubscriptionCarrierId";
                case 170:
                    return "getSubscriptionCarrierName";
                case 171:
                    return "getSubscriptionSpecificCarrierId";
                case 172:
                    return "getSubscriptionSpecificCarrierName";
                case 173:
                    return "getCarrierIdFromMccMnc";
                case 174:
                    return "carrierActionSetMeteredApnsEnabled";
                case 175:
                    return "carrierActionSetRadioEnabled";
                case 176:
                    return "carrierActionReportDefaultNetworkStatus";
                case 177:
                    return "carrierActionResetAll";
                case 178:
                    return "getVtDataUsage";
                case 179:
                    return "setPolicyDataEnabled";
                case 180:
                    return "getClientRequestStats";
                case 181:
                    return "setSimPowerStateForSlot";
                case 182:
                    return "getForbiddenPlmns";
                case 183:
                    return "getEmergencyCallbackMode";
                case 184:
                    return "getSignalStrength";
                case 185:
                    return "getCardIdForDefaultEuicc";
                case 186:
                    return "getUiccCardsInfo";
                case 187:
                    return "getUiccSlotsInfo";
                case 188:
                    return "switchSlots";
                case 189:
                    return "setRadioIndicationUpdateMode";
                case 190:
                    return "isDataRoamingEnabled";
                case 191:
                    return "setDataRoamingEnabled";
                case 192:
                    return "getCdmaRoamingMode";
                case 193:
                    return "setCdmaRoamingMode";
                case 194:
                    return "setCdmaSubscriptionMode";
                case 195:
                    return "setCarrierTestOverride";
                case 196:
                    return "getCarrierIdListVersion";
                case 197:
                    return "refreshUiccProfile";
                case 198:
                    return "getNumberOfModemsWithSimultaneousDataConnections";
                case 199:
                    return "getNetworkSelectionMode";
                case 200:
                    return "isInEmergencySmsMode";
                case 201:
                    return "getSmsApps";
                case 202:
                    return "getDefaultSmsApp";
                case 203:
                    return "setDefaultSmsApp";
                case 204:
                    return "getRadioPowerState";
                case 205:
                    return "registerImsRegistrationCallback";
                case 206:
                    return "unregisterImsRegistrationCallback";
                case 207:
                    return "registerMmTelCapabilityCallback";
                case 208:
                    return "unregisterMmTelCapabilityCallback";
                case 209:
                    return "isCapable";
                case 210:
                    return "isAvailable";
                case 211:
                    return "isAdvancedCallingSettingEnabled";
                case 212:
                    return "setAdvancedCallingSettingEnabled";
                case 213:
                    return "isVtSettingEnabled";
                case 214:
                    return "setVtSettingEnabled";
                case 215:
                    return "isVoWiFiSettingEnabled";
                case 216:
                    return "setVoWiFiSettingEnabled";
                case 217:
                    return "isVoWiFiRoamingSettingEnabled";
                case 218:
                    return "setVoWiFiRoamingSettingEnabled";
                case 219:
                    return "setVoWiFiNonPersistent";
                case 220:
                    return "getVoWiFiModeSetting";
                case 221:
                    return "setVoWiFiModeSetting";
                case 222:
                    return "getVoWiFiRoamingModeSetting";
                case 223:
                    return "setVoWiFiRoamingModeSetting";
                case 224:
                    return "setRttCapabilitySetting";
                case 225:
                    return "isTtyOverVolteEnabled";
                case 226:
                    return "getEmergencyNumberList";
                case 227:
                    return "isEmergencyNumber";
                case 228:
                    return "getCertsFromCarrierPrivilegeAccessRules";
                case 229:
                    return "registerImsProvisioningChangedCallback";
                case 230:
                    return "unregisterImsProvisioningChangedCallback";
                case 231:
                    return "setImsProvisioningStatusForCapability";
                case 232:
                    return "getImsProvisioningStatusForCapability";
                case 233:
                    return "isMmTelCapabilityProvisionedInCache";
                case 234:
                    return "cacheMmTelCapabilityProvisioning";
                case 235:
                    return "getImsProvisioningInt";
                case 236:
                    return "getImsProvisioningString";
                case 237:
                    return "setImsProvisioningInt";
                case 238:
                    return "setImsProvisioningString";
                case 239:
                    return "updateEmergencyNumberListTestMode";
                case 240:
                    return "getEmergencyNumberListTestMode";
                case 241:
                    return "enableModemForSlot";
                case 242:
                    return "setMultiSimCarrierRestriction";
                case 243:
                    return "isMultiSimSupported";
                case 244:
                    return "switchMultiSimConfig";
                case 245:
                    return "doesSwitchMultiSimConfigTriggerReboot";
                case 246:
                    return "getSlotsMapping";
                case 247:
                    return "getRadioHalVersion";
                case 248:
                    return "isModemEnabledForSlot";
                case 249:
                    return "isDataEnabledForApn";
                case 250:
                    return "isApnMetered";
                case 251:
                    return "enqueueSmsPickResult";
                case 252:
                    return "getMmsUserAgent";
                case 253:
                    return "getMmsUAProfUrl";
                case 254:
                    return "setDataAllowedDuringVoiceCall";
                case 255:
                    return "isDataAllowedInVoiceCall";
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
            ResultReceiver _arg2;
            boolean _arg1;
            VisualVoicemailSmsFilterSettings _arg22;
            PendingIntent _arg5;
            WorkSource _arg3;
            NetworkScanRequest _arg12;
            Messenger _arg23;
            OperatorInfo _arg13;
            PhoneNumberRange _arg0;
            Intent _arg02;
            byte[] _arg14;
            PhoneAccount _arg03;
            ResultReceiver _arg04;
            PhoneAccountHandle _arg05;
            PhoneAccountHandle _arg15;
            Uri _arg24;
            PhoneAccountHandle _arg06;
            PhoneAccountHandle _arg16;
            CarrierRestrictionRules _arg07;
            EmergencyNumber _arg17;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    dial(_arg08);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    call(_arg09, data.readString());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    boolean isRadioOn = isRadioOn(_arg010);
                    reply.writeNoException();
                    reply.writeInt(isRadioOn ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    boolean isRadioOnForSubscriber = isRadioOnForSubscriber(_arg011, data.readString());
                    reply.writeNoException();
                    reply.writeInt(isRadioOnForSubscriber ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    boolean supplyPin = supplyPin(_arg012);
                    reply.writeNoException();
                    reply.writeInt(supplyPin ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    boolean supplyPinForSubscriber = supplyPinForSubscriber(_arg013, data.readString());
                    reply.writeNoException();
                    reply.writeInt(supplyPinForSubscriber ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    boolean supplyPuk = supplyPuk(_arg014, data.readString());
                    reply.writeNoException();
                    reply.writeInt(supplyPuk ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg015 = data.readInt();
                    String _arg18 = data.readString();
                    String _arg25 = data.readString();
                    boolean supplyPukForSubscriber = supplyPukForSubscriber(_arg015, _arg18, _arg25);
                    reply.writeNoException();
                    reply.writeInt(supplyPukForSubscriber ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    int[] _result = supplyPinReportResult(_arg016);
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    int[] _result2 = supplyPinReportResultForSubscriber(_arg017, data.readString());
                    reply.writeNoException();
                    reply.writeIntArray(_result2);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    int[] _result3 = supplyPukReportResult(_arg018, data.readString());
                    reply.writeNoException();
                    reply.writeIntArray(_result3);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg019 = data.readInt();
                    String _arg19 = data.readString();
                    String _arg26 = data.readString();
                    int[] _result4 = supplyPukReportResultForSubscriber(_arg019, _arg19, _arg26);
                    reply.writeNoException();
                    reply.writeIntArray(_result4);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    boolean handlePinMmi = handlePinMmi(_arg020);
                    reply.writeNoException();
                    reply.writeInt(handlePinMmi ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg021 = data.readInt();
                    String _arg110 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = ResultReceiver.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    handleUssdRequest(_arg021, _arg110, _arg2);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    boolean handlePinMmiForSubscriber = handlePinMmiForSubscriber(_arg022, data.readString());
                    reply.writeNoException();
                    reply.writeInt(handlePinMmiForSubscriber ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    toggleRadioOnOff();
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    toggleRadioOnOffForSubscriber(_arg023);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg024 = _arg1;
                    boolean radio = setRadio(_arg024);
                    reply.writeNoException();
                    reply.writeInt(radio ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    boolean radioForSubscriber = setRadioForSubscriber(_arg025, _arg1);
                    reply.writeNoException();
                    reply.writeInt(radioForSubscriber ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg026 = _arg1;
                    boolean radioPower = setRadioPower(_arg026);
                    reply.writeNoException();
                    reply.writeInt(radioPower ? 1 : 0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    updateServiceLocation();
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg027 = data.readInt();
                    updateServiceLocationForSubscriber(_arg027);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    enableLocationUpdates();
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg028 = data.readInt();
                    enableLocationUpdatesForSubscriber(_arg028);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    disableLocationUpdates();
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg029 = data.readInt();
                    disableLocationUpdatesForSubscriber(_arg029);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    boolean enableDataConnectivity = enableDataConnectivity();
                    reply.writeNoException();
                    reply.writeInt(enableDataConnectivity ? 1 : 0);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    boolean disableDataConnectivity = disableDataConnectivity();
                    reply.writeNoException();
                    reply.writeInt(disableDataConnectivity ? 1 : 0);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg030 = data.readInt();
                    boolean isDataConnectivityPossible = isDataConnectivityPossible(_arg030);
                    reply.writeNoException();
                    reply.writeInt(isDataConnectivityPossible ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg031 = data.readString();
                    Bundle _result5 = getCellLocation(_arg031);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg032 = data.readInt();
                    String _result6 = getNetworkCountryIsoForPhone(_arg032);
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg033 = data.readString();
                    List<NeighboringCellInfo> _result7 = getNeighboringCellInfo(_arg033);
                    reply.writeNoException();
                    reply.writeTypedList(_result7);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    int _result8 = getCallState();
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg034 = data.readInt();
                    int _result9 = getCallStateForSlot(_arg034);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _result10 = getDataActivity();
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg035 = data.readInt();
                    int _result11 = getDataActivityForSubId(_arg035);
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    int _result12 = getDataState();
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    int _result13 = getDataStateForSubId(_arg036);
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    int _result14 = getActivePhoneType();
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg037 = data.readInt();
                    int _result15 = getActivePhoneTypeForSlot(_arg037);
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg038 = data.readString();
                    int _result16 = getCdmaEriIconIndex(_arg038);
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg039 = data.readInt();
                    int _result17 = getCdmaEriIconIndexForSubscriber(_arg039, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg040 = data.readString();
                    int _result18 = getCdmaEriIconMode(_arg040);
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg041 = data.readInt();
                    int _result19 = getCdmaEriIconModeForSubscriber(_arg041, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg042 = data.readString();
                    String _result20 = getCdmaEriText(_arg042);
                    reply.writeNoException();
                    reply.writeString(_result20);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg043 = data.readInt();
                    String _result21 = getCdmaEriTextForSubscriber(_arg043, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result21);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    boolean needsOtaServiceProvisioning = needsOtaServiceProvisioning();
                    reply.writeNoException();
                    reply.writeInt(needsOtaServiceProvisioning ? 1 : 0);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg044 = data.readInt();
                    String _arg111 = data.readString();
                    String _arg27 = data.readString();
                    boolean voiceMailNumber = setVoiceMailNumber(_arg044, _arg111, _arg27);
                    reply.writeNoException();
                    reply.writeInt(voiceMailNumber ? 1 : 0);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg045 = data.readInt();
                    setVoiceActivationState(_arg045, data.readInt());
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg046 = data.readInt();
                    setDataActivationState(_arg046, data.readInt());
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg047 = data.readInt();
                    int _result22 = getVoiceActivationState(_arg047, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg048 = data.readInt();
                    int _result23 = getDataActivationState(_arg048, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result23);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg049 = data.readInt();
                    int _result24 = getVoiceMessageCountForSubscriber(_arg049, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result24);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg050 = data.readInt();
                    boolean isConcurrentVoiceAndDataAllowed = isConcurrentVoiceAndDataAllowed(_arg050);
                    reply.writeNoException();
                    reply.writeInt(isConcurrentVoiceAndDataAllowed ? 1 : 0);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg051 = data.readString();
                    Bundle _result25 = getVisualVoicemailSettings(_arg051, data.readInt());
                    reply.writeNoException();
                    if (_result25 != null) {
                        reply.writeInt(1);
                        _result25.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg052 = data.readString();
                    String _result26 = getVisualVoicemailPackageName(_arg052, data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result26);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg053 = data.readString();
                    int _arg112 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg22 = VisualVoicemailSmsFilterSettings.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    enableVisualVoicemailSmsFilter(_arg053, _arg112, _arg22);
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg054 = data.readString();
                    disableVisualVoicemailSmsFilter(_arg054, data.readInt());
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg055 = data.readString();
                    VisualVoicemailSmsFilterSettings _result27 = getVisualVoicemailSmsFilterSettings(_arg055, data.readInt());
                    reply.writeNoException();
                    if (_result27 != null) {
                        reply.writeInt(1);
                        _result27.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg056 = data.readInt();
                    VisualVoicemailSmsFilterSettings _result28 = getActiveVisualVoicemailSmsFilterSettings(_arg056);
                    reply.writeNoException();
                    if (_result28 != null) {
                        reply.writeInt(1);
                        _result28.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg057 = data.readString();
                    int _arg113 = data.readInt();
                    String _arg28 = data.readString();
                    int _arg32 = data.readInt();
                    String _arg4 = data.readString();
                    if (data.readInt() != 0) {
                        _arg5 = PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    sendVisualVoicemailSmsForSubscriber(_arg057, _arg113, _arg28, _arg32, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg058 = data.readString();
                    sendDialerSpecialCode(_arg058, data.readString());
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg059 = data.readInt();
                    int _result29 = getNetworkTypeForSubscriber(_arg059, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result29);
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg060 = data.readString();
                    int _result30 = getDataNetworkType(_arg060);
                    reply.writeNoException();
                    reply.writeInt(_result30);
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg061 = data.readInt();
                    int _result31 = getDataNetworkTypeForSubscriber(_arg061, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result31);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg062 = data.readInt();
                    int _result32 = getVoiceNetworkTypeForSubscriber(_arg062, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result32);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasIccCard = hasIccCard();
                    reply.writeNoException();
                    reply.writeInt(hasIccCard ? 1 : 0);
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg063 = data.readInt();
                    boolean hasIccCardUsingSlotIndex = hasIccCardUsingSlotIndex(_arg063);
                    reply.writeNoException();
                    reply.writeInt(hasIccCardUsingSlotIndex ? 1 : 0);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg064 = data.readString();
                    int _result33 = getLteOnCdmaMode(_arg064);
                    reply.writeNoException();
                    reply.writeInt(_result33);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg065 = data.readInt();
                    int _result34 = getLteOnCdmaModeForSubscriber(_arg065, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result34);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg066 = data.readString();
                    List<CellInfo> _result35 = getAllCellInfo(_arg066);
                    reply.writeNoException();
                    reply.writeTypedList(_result35);
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg067 = data.readInt();
                    ICellInfoCallback _arg114 = ICellInfoCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg29 = data.readString();
                    requestCellInfoUpdate(_arg067, _arg114, _arg29);
                    reply.writeNoException();
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg068 = data.readInt();
                    ICellInfoCallback _arg115 = ICellInfoCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg210 = data.readString();
                    if (data.readInt() != 0) {
                        _arg3 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    requestCellInfoUpdateWithWorkSource(_arg068, _arg115, _arg210, _arg3);
                    reply.writeNoException();
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg069 = data.readInt();
                    setCellInfoListRate(_arg069);
                    reply.writeNoException();
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg070 = data.readInt();
                    String _arg116 = data.readString();
                    String _arg211 = data.readString();
                    int _arg33 = data.readInt();
                    IccOpenLogicalChannelResponse _result36 = iccOpenLogicalChannelBySlot(_arg070, _arg116, _arg211, _arg33);
                    reply.writeNoException();
                    if (_result36 != null) {
                        reply.writeInt(1);
                        _result36.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg071 = data.readInt();
                    String _arg117 = data.readString();
                    String _arg212 = data.readString();
                    int _arg34 = data.readInt();
                    IccOpenLogicalChannelResponse _result37 = iccOpenLogicalChannel(_arg071, _arg117, _arg212, _arg34);
                    reply.writeNoException();
                    if (_result37 != null) {
                        reply.writeInt(1);
                        _result37.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg072 = data.readInt();
                    boolean iccCloseLogicalChannelBySlot = iccCloseLogicalChannelBySlot(_arg072, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(iccCloseLogicalChannelBySlot ? 1 : 0);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg073 = data.readInt();
                    boolean iccCloseLogicalChannel = iccCloseLogicalChannel(_arg073, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(iccCloseLogicalChannel ? 1 : 0);
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg074 = data.readInt();
                    int _arg118 = data.readInt();
                    int _arg213 = data.readInt();
                    int _arg35 = data.readInt();
                    int _arg42 = data.readInt();
                    int _arg52 = data.readInt();
                    int _arg6 = data.readInt();
                    String _arg7 = data.readString();
                    String _result38 = iccTransmitApduLogicalChannelBySlot(_arg074, _arg118, _arg213, _arg35, _arg42, _arg52, _arg6, _arg7);
                    reply.writeNoException();
                    reply.writeString(_result38);
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg075 = data.readInt();
                    int _arg119 = data.readInt();
                    int _arg214 = data.readInt();
                    int _arg36 = data.readInt();
                    int _arg43 = data.readInt();
                    int _arg53 = data.readInt();
                    int _arg62 = data.readInt();
                    String _arg72 = data.readString();
                    String _result39 = iccTransmitApduLogicalChannel(_arg075, _arg119, _arg214, _arg36, _arg43, _arg53, _arg62, _arg72);
                    reply.writeNoException();
                    reply.writeString(_result39);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg076 = data.readInt();
                    String _arg120 = data.readString();
                    int _arg215 = data.readInt();
                    int _arg37 = data.readInt();
                    int _arg44 = data.readInt();
                    int _arg54 = data.readInt();
                    int _arg63 = data.readInt();
                    String _arg73 = data.readString();
                    String _result40 = iccTransmitApduBasicChannelBySlot(_arg076, _arg120, _arg215, _arg37, _arg44, _arg54, _arg63, _arg73);
                    reply.writeNoException();
                    reply.writeString(_result40);
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg077 = data.readInt();
                    String _arg121 = data.readString();
                    int _arg216 = data.readInt();
                    int _arg38 = data.readInt();
                    int _arg45 = data.readInt();
                    int _arg55 = data.readInt();
                    int _arg64 = data.readInt();
                    String _arg74 = data.readString();
                    String _result41 = iccTransmitApduBasicChannel(_arg077, _arg121, _arg216, _arg38, _arg45, _arg55, _arg64, _arg74);
                    reply.writeNoException();
                    reply.writeString(_result41);
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg078 = data.readInt();
                    int _arg122 = data.readInt();
                    int _arg217 = data.readInt();
                    int _arg39 = data.readInt();
                    int _arg46 = data.readInt();
                    int _arg56 = data.readInt();
                    String _arg65 = data.readString();
                    byte[] _result42 = iccExchangeSimIO(_arg078, _arg122, _arg217, _arg39, _arg46, _arg56, _arg65);
                    reply.writeNoException();
                    reply.writeByteArray(_result42);
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg079 = data.readInt();
                    String _result43 = sendEnvelopeWithStatus(_arg079, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result43);
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg080 = data.readInt();
                    String _result44 = nvReadItem(_arg080);
                    reply.writeNoException();
                    reply.writeString(_result44);
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg081 = data.readInt();
                    boolean nvWriteItem = nvWriteItem(_arg081, data.readString());
                    reply.writeNoException();
                    reply.writeInt(nvWriteItem ? 1 : 0);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg082 = data.createByteArray();
                    boolean nvWriteCdmaPrl = nvWriteCdmaPrl(_arg082);
                    reply.writeNoException();
                    reply.writeInt(nvWriteCdmaPrl ? 1 : 0);
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg083 = data.readInt();
                    boolean resetModemConfig = resetModemConfig(_arg083);
                    reply.writeNoException();
                    reply.writeInt(resetModemConfig ? 1 : 0);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg084 = data.readInt();
                    boolean rebootModem = rebootModem(_arg084);
                    reply.writeNoException();
                    reply.writeInt(rebootModem ? 1 : 0);
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg085 = data.readString();
                    int _result45 = getCalculatedPreferredNetworkType(_arg085);
                    reply.writeNoException();
                    reply.writeInt(_result45);
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg086 = data.readInt();
                    int _result46 = getPreferredNetworkType(_arg086);
                    reply.writeNoException();
                    reply.writeInt(_result46);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg087 = data.readInt();
                    boolean tetherApnRequiredForSubscriber = getTetherApnRequiredForSubscriber(_arg087);
                    reply.writeNoException();
                    reply.writeInt(tetherApnRequiredForSubscriber ? 1 : 0);
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg088 = data.readInt();
                    enableIms(_arg088);
                    reply.writeNoException();
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg089 = data.readInt();
                    disableIms(_arg089);
                    reply.writeNoException();
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg090 = data.readInt();
                    IImsMmTelFeature _result47 = getMmTelFeatureAndListen(_arg090, IImsServiceFeatureCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeStrongBinder(_result47 != null ? _result47.asBinder() : null);
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg091 = data.readInt();
                    IImsRcsFeature _result48 = getRcsFeatureAndListen(_arg091, IImsServiceFeatureCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeStrongBinder(_result48 != null ? _result48.asBinder() : null);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg092 = data.readInt();
                    IImsRegistration _result49 = getImsRegistration(_arg092, data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(_result49 != null ? _result49.asBinder() : null);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg093 = data.readInt();
                    IImsConfig _result50 = getImsConfig(_arg093, data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(_result50 != null ? _result50.asBinder() : null);
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg094 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    String _arg218 = data.readString();
                    boolean imsService = setImsService(_arg094, _arg1, _arg218);
                    reply.writeNoException();
                    reply.writeInt(imsService ? 1 : 0);
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg095 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    String _result51 = getImsService(_arg095, _arg1);
                    reply.writeNoException();
                    reply.writeString(_result51);
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg096 = data.readInt();
                    setNetworkSelectionModeAutomatic(_arg096);
                    reply.writeNoException();
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg097 = data.readInt();
                    CellNetworkScanResult _result52 = getCellNetworkScanResults(_arg097, data.readString());
                    reply.writeNoException();
                    if (_result52 != null) {
                        reply.writeInt(1);
                        _result52.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg098 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = NetworkScanRequest.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg23 = Messenger.CREATOR.createFromParcel(data);
                    } else {
                        _arg23 = null;
                    }
                    IBinder _arg310 = data.readStrongBinder();
                    String _arg47 = data.readString();
                    int _result53 = requestNetworkScan(_arg098, _arg12, _arg23, _arg310, _arg47);
                    reply.writeNoException();
                    reply.writeInt(_result53);
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg099 = data.readInt();
                    stopNetworkScan(_arg099, data.readInt());
                    reply.writeNoException();
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0100 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = OperatorInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    _arg1 = data.readInt() != 0;
                    boolean networkSelectionModeManual = setNetworkSelectionModeManual(_arg0100, _arg13, _arg1);
                    reply.writeNoException();
                    reply.writeInt(networkSelectionModeManual ? 1 : 0);
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0101 = data.readInt();
                    boolean preferredNetworkType = setPreferredNetworkType(_arg0101, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(preferredNetworkType ? 1 : 0);
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0102 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setUserDataEnabled(_arg0102, _arg1);
                    reply.writeNoException();
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0103 = data.readInt();
                    boolean dataEnabled = getDataEnabled(_arg0103);
                    reply.writeNoException();
                    reply.writeInt(dataEnabled ? 1 : 0);
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0104 = data.readInt();
                    boolean isUserDataEnabled = isUserDataEnabled(_arg0104);
                    reply.writeNoException();
                    reply.writeInt(isUserDataEnabled ? 1 : 0);
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0105 = data.readInt();
                    boolean isDataEnabled = isDataEnabled(_arg0105);
                    reply.writeNoException();
                    reply.writeInt(isDataEnabled ? 1 : 0);
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0106 = data.readInt();
                    boolean isManualNetworkSelectionAllowed = isManualNetworkSelectionAllowed(_arg0106);
                    reply.writeNoException();
                    reply.writeInt(isManualNetworkSelectionAllowed ? 1 : 0);
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0107 = data.readString();
                    String[] _result54 = getPcscfAddress(_arg0107, data.readString());
                    reply.writeNoException();
                    reply.writeStringArray(_result54);
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0108 = _arg1;
                    setImsRegistrationState(_arg0108);
                    reply.writeNoException();
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0109 = data.readInt();
                    String _result55 = getCdmaMdn(_arg0109);
                    reply.writeNoException();
                    reply.writeString(_result55);
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0110 = data.readInt();
                    String _result56 = getCdmaMin(_arg0110);
                    reply.writeNoException();
                    reply.writeString(_result56);
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = PhoneNumberRange.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    long _arg123 = data.readLong();
                    INumberVerificationCallback _arg219 = INumberVerificationCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg311 = data.readString();
                    requestNumberVerification(_arg0, _arg123, _arg219, _arg311);
                    reply.writeNoException();
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0111 = data.readInt();
                    int _result57 = getCarrierPrivilegeStatus(_arg0111);
                    reply.writeNoException();
                    reply.writeInt(_result57);
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0112 = data.readInt();
                    int _result58 = getCarrierPrivilegeStatusForUid(_arg0112, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result58);
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0113 = data.readInt();
                    int _result59 = checkCarrierPrivilegesForPackage(_arg0113, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result59);
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0114 = data.readString();
                    int _result60 = checkCarrierPrivilegesForPackageAnyPhone(_arg0114);
                    reply.writeNoException();
                    reply.writeInt(_result60);
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    List<String> _result61 = getCarrierPackageNamesForIntentAndPhone(_arg02, data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result61);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0115 = data.readInt();
                    String _arg124 = data.readString();
                    String _arg220 = data.readString();
                    boolean line1NumberForDisplayForSubscriber = setLine1NumberForDisplayForSubscriber(_arg0115, _arg124, _arg220);
                    reply.writeNoException();
                    reply.writeInt(line1NumberForDisplayForSubscriber ? 1 : 0);
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0116 = data.readInt();
                    String _result62 = getLine1NumberForDisplay(_arg0116, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result62);
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0117 = data.readInt();
                    String _result63 = getLine1AlphaTagForDisplay(_arg0117, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result63);
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0118 = data.readInt();
                    String[] _result64 = getMergedSubscriberIds(_arg0118, data.readString());
                    reply.writeNoException();
                    reply.writeStringArray(_result64);
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0119 = data.readInt();
                    String[] _result65 = getMergedSubscriberIdsFromGroup(_arg0119, data.readString());
                    reply.writeNoException();
                    reply.writeStringArray(_result65);
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0120 = data.readInt();
                    boolean operatorBrandOverride = setOperatorBrandOverride(_arg0120, data.readString());
                    reply.writeNoException();
                    reply.writeInt(operatorBrandOverride ? 1 : 0);
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0121 = data.readInt();
                    List<String> _arg125 = data.createStringArrayList();
                    List<String> _arg221 = data.createStringArrayList();
                    List<String> _arg312 = data.createStringArrayList();
                    List<String> _arg48 = data.createStringArrayList();
                    boolean roamingOverride = setRoamingOverride(_arg0121, _arg125, _arg221, _arg312, _arg48);
                    reply.writeNoException();
                    reply.writeInt(roamingOverride ? 1 : 0);
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg0122 = data.createByteArray();
                    int _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg14 = null;
                    } else {
                        _arg14 = new byte[_arg1_length];
                    }
                    int _result66 = invokeOemRilRequestRaw(_arg0122, _arg14);
                    reply.writeNoException();
                    reply.writeInt(_result66);
                    reply.writeByteArray(_arg14);
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    boolean needMobileRadioShutdown = needMobileRadioShutdown();
                    reply.writeNoException();
                    reply.writeInt(needMobileRadioShutdown ? 1 : 0);
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    shutdownMobileRadios();
                    reply.writeNoException();
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    RadioAccessFamily[] _arg0123 = (RadioAccessFamily[]) data.createTypedArray(RadioAccessFamily.CREATOR);
                    setRadioCapability(_arg0123);
                    reply.writeNoException();
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0124 = data.readInt();
                    int _result67 = getRadioAccessFamily(_arg0124, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result67);
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0125 = _arg1;
                    enableVideoCalling(_arg0125);
                    reply.writeNoException();
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0126 = data.readString();
                    boolean isVideoCallingEnabled = isVideoCallingEnabled(_arg0126);
                    reply.writeNoException();
                    reply.writeInt(isVideoCallingEnabled ? 1 : 0);
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0127 = data.readInt();
                    boolean canChangeDtmfToneLength = canChangeDtmfToneLength(_arg0127, data.readString());
                    reply.writeNoException();
                    reply.writeInt(canChangeDtmfToneLength ? 1 : 0);
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0128 = data.readInt();
                    boolean isWorldPhone = isWorldPhone(_arg0128, data.readString());
                    reply.writeNoException();
                    reply.writeInt(isWorldPhone ? 1 : 0);
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isTtyModeSupported = isTtyModeSupported();
                    reply.writeNoException();
                    reply.writeInt(isTtyModeSupported ? 1 : 0);
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0129 = data.readInt();
                    boolean isRttSupported = isRttSupported(_arg0129);
                    reply.writeNoException();
                    reply.writeInt(isRttSupported ? 1 : 0);
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHearingAidCompatibilitySupported = isHearingAidCompatibilitySupported();
                    reply.writeNoException();
                    reply.writeInt(isHearingAidCompatibilitySupported ? 1 : 0);
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0130 = data.readInt();
                    boolean isImsRegistered = isImsRegistered(_arg0130);
                    reply.writeNoException();
                    reply.writeInt(isImsRegistered ? 1 : 0);
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0131 = data.readInt();
                    boolean isWifiCallingAvailable = isWifiCallingAvailable(_arg0131);
                    reply.writeNoException();
                    reply.writeInt(isWifiCallingAvailable ? 1 : 0);
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0132 = data.readInt();
                    boolean isVideoTelephonyAvailable = isVideoTelephonyAvailable(_arg0132);
                    reply.writeNoException();
                    reply.writeInt(isVideoTelephonyAvailable ? 1 : 0);
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0133 = data.readInt();
                    int _result68 = getImsRegTechnologyForMmTel(_arg0133);
                    reply.writeNoException();
                    reply.writeInt(_result68);
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0134 = data.readString();
                    String _result69 = getDeviceId(_arg0134);
                    reply.writeNoException();
                    reply.writeString(_result69);
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0135 = data.readInt();
                    String _result70 = getImeiForSlot(_arg0135, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result70);
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0136 = data.readInt();
                    String _result71 = getTypeAllocationCodeForSlot(_arg0136);
                    reply.writeNoException();
                    reply.writeString(_result71);
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0137 = data.readInt();
                    String _result72 = getMeidForSlot(_arg0137, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result72);
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0138 = data.readInt();
                    String _result73 = getManufacturerCodeForSlot(_arg0138);
                    reply.writeNoException();
                    reply.writeString(_result73);
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0139 = data.readInt();
                    String _result74 = getDeviceSoftwareVersionForSlot(_arg0139, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result74);
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = PhoneAccount.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    int _result75 = getSubIdForPhoneAccount(_arg03);
                    reply.writeNoException();
                    reply.writeInt(_result75);
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0140 = data.readInt();
                    PhoneAccountHandle _result76 = getPhoneAccountHandleForSubscriptionId(_arg0140);
                    reply.writeNoException();
                    if (_result76 != null) {
                        reply.writeInt(1);
                        _result76.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0141 = data.readInt();
                    factoryReset(_arg0141);
                    reply.writeNoException();
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0142 = data.readInt();
                    String _result77 = getSimLocaleForSubscriber(_arg0142);
                    reply.writeNoException();
                    reply.writeString(_result77);
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = ResultReceiver.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    requestModemActivityInfo(_arg04);
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0143 = data.readInt();
                    ServiceState _result78 = getServiceStateForSubscriber(_arg0143, data.readString());
                    reply.writeNoException();
                    if (_result78 != null) {
                        reply.writeInt(1);
                        _result78.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    Uri _result79 = getVoicemailRingtoneUri(_arg05);
                    reply.writeNoException();
                    if (_result79 != null) {
                        reply.writeInt(1);
                        _result79.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0144 = data.readString();
                    if (data.readInt() != 0) {
                        _arg15 = PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg24 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg24 = null;
                    }
                    setVoicemailRingtoneUri(_arg0144, _arg15, _arg24);
                    reply.writeNoException();
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    boolean isVoicemailVibrationEnabled = isVoicemailVibrationEnabled(_arg06);
                    reply.writeNoException();
                    reply.writeInt(isVoicemailVibrationEnabled ? 1 : 0);
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0145 = data.readString();
                    if (data.readInt() != 0) {
                        _arg16 = PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg16 = null;
                    }
                    _arg1 = data.readInt() != 0;
                    setVoicemailVibrationEnabled(_arg0145, _arg16, _arg1);
                    reply.writeNoException();
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0146 = data.readInt();
                    List<String> _result80 = getPackagesWithCarrierPrivileges(_arg0146);
                    reply.writeNoException();
                    reply.writeStringList(_result80);
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result81 = getPackagesWithCarrierPrivilegesForAllPhones();
                    reply.writeNoException();
                    reply.writeStringList(_result81);
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0147 = data.readInt();
                    String _result82 = getAidForAppType(_arg0147, data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result82);
                    return true;
                case 164:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0148 = data.readInt();
                    String _result83 = getEsn(_arg0148);
                    reply.writeNoException();
                    reply.writeString(_result83);
                    return true;
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0149 = data.readInt();
                    String _result84 = getCdmaPrlVersion(_arg0149);
                    reply.writeNoException();
                    reply.writeString(_result84);
                    return true;
                case 166:
                    data.enforceInterface(DESCRIPTOR);
                    List<TelephonyHistogram> _result85 = getTelephonyHistograms();
                    reply.writeNoException();
                    reply.writeTypedList(_result85);
                    return true;
                case 167:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = CarrierRestrictionRules.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    int _result86 = setAllowedCarriers(_arg07);
                    reply.writeNoException();
                    reply.writeInt(_result86);
                    return true;
                case 168:
                    data.enforceInterface(DESCRIPTOR);
                    CarrierRestrictionRules _result87 = getAllowedCarriers();
                    reply.writeNoException();
                    if (_result87 != null) {
                        reply.writeInt(1);
                        _result87.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 169:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0150 = data.readInt();
                    int _result88 = getSubscriptionCarrierId(_arg0150);
                    reply.writeNoException();
                    reply.writeInt(_result88);
                    return true;
                case 170:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0151 = data.readInt();
                    String _result89 = getSubscriptionCarrierName(_arg0151);
                    reply.writeNoException();
                    reply.writeString(_result89);
                    return true;
                case 171:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0152 = data.readInt();
                    int _result90 = getSubscriptionSpecificCarrierId(_arg0152);
                    reply.writeNoException();
                    reply.writeInt(_result90);
                    return true;
                case 172:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0153 = data.readInt();
                    String _result91 = getSubscriptionSpecificCarrierName(_arg0153);
                    reply.writeNoException();
                    reply.writeString(_result91);
                    return true;
                case 173:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0154 = data.readInt();
                    String _arg126 = data.readString();
                    _arg1 = data.readInt() != 0;
                    int _result92 = getCarrierIdFromMccMnc(_arg0154, _arg126, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result92);
                    return true;
                case 174:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0155 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    carrierActionSetMeteredApnsEnabled(_arg0155, _arg1);
                    reply.writeNoException();
                    return true;
                case 175:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0156 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    carrierActionSetRadioEnabled(_arg0156, _arg1);
                    reply.writeNoException();
                    return true;
                case 176:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0157 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    carrierActionReportDefaultNetworkStatus(_arg0157, _arg1);
                    reply.writeNoException();
                    return true;
                case 177:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0158 = data.readInt();
                    carrierActionResetAll(_arg0158);
                    reply.writeNoException();
                    return true;
                case 178:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0159 = data.readInt();
                    NetworkStats _result93 = getVtDataUsage(_arg0159, data.readInt() != 0);
                    reply.writeNoException();
                    if (_result93 != null) {
                        reply.writeInt(1);
                        _result93.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 179:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0160 = _arg1;
                    setPolicyDataEnabled(_arg0160, data.readInt());
                    reply.writeNoException();
                    return true;
                case 180:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0161 = data.readString();
                    List<ClientRequestStats> _result94 = getClientRequestStats(_arg0161, data.readInt());
                    reply.writeNoException();
                    reply.writeTypedList(_result94);
                    return true;
                case 181:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0162 = data.readInt();
                    setSimPowerStateForSlot(_arg0162, data.readInt());
                    reply.writeNoException();
                    return true;
                case 182:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0163 = data.readInt();
                    int _arg127 = data.readInt();
                    String _arg222 = data.readString();
                    String[] _result95 = getForbiddenPlmns(_arg0163, _arg127, _arg222);
                    reply.writeNoException();
                    reply.writeStringArray(_result95);
                    return true;
                case 183:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0164 = data.readInt();
                    boolean emergencyCallbackMode = getEmergencyCallbackMode(_arg0164);
                    reply.writeNoException();
                    reply.writeInt(emergencyCallbackMode ? 1 : 0);
                    return true;
                case 184:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0165 = data.readInt();
                    SignalStrength _result96 = getSignalStrength(_arg0165);
                    reply.writeNoException();
                    if (_result96 != null) {
                        reply.writeInt(1);
                        _result96.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 185:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0166 = data.readInt();
                    int _result97 = getCardIdForDefaultEuicc(_arg0166, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result97);
                    return true;
                case 186:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0167 = data.readString();
                    List<UiccCardInfo> _result98 = getUiccCardsInfo(_arg0167);
                    reply.writeNoException();
                    reply.writeTypedList(_result98);
                    return true;
                case 187:
                    data.enforceInterface(DESCRIPTOR);
                    UiccSlotInfo[] _result99 = getUiccSlotsInfo();
                    reply.writeNoException();
                    reply.writeTypedArray(_result99, 1);
                    return true;
                case 188:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg0168 = data.createIntArray();
                    boolean switchSlots = switchSlots(_arg0168);
                    reply.writeNoException();
                    reply.writeInt(switchSlots ? 1 : 0);
                    return true;
                case 189:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0169 = data.readInt();
                    int _arg128 = data.readInt();
                    int _arg223 = data.readInt();
                    setRadioIndicationUpdateMode(_arg0169, _arg128, _arg223);
                    reply.writeNoException();
                    return true;
                case 190:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0170 = data.readInt();
                    boolean isDataRoamingEnabled = isDataRoamingEnabled(_arg0170);
                    reply.writeNoException();
                    reply.writeInt(isDataRoamingEnabled ? 1 : 0);
                    return true;
                case 191:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0171 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setDataRoamingEnabled(_arg0171, _arg1);
                    reply.writeNoException();
                    return true;
                case 192:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0172 = data.readInt();
                    int _result100 = getCdmaRoamingMode(_arg0172);
                    reply.writeNoException();
                    reply.writeInt(_result100);
                    return true;
                case 193:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0173 = data.readInt();
                    boolean cdmaRoamingMode = setCdmaRoamingMode(_arg0173, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(cdmaRoamingMode ? 1 : 0);
                    return true;
                case 194:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0174 = data.readInt();
                    boolean cdmaSubscriptionMode = setCdmaSubscriptionMode(_arg0174, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(cdmaSubscriptionMode ? 1 : 0);
                    return true;
                case 195:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0175 = data.readInt();
                    String _arg129 = data.readString();
                    String _arg224 = data.readString();
                    String _arg313 = data.readString();
                    String _arg49 = data.readString();
                    String _arg57 = data.readString();
                    String _arg66 = data.readString();
                    String _arg75 = data.readString();
                    String _arg8 = data.readString();
                    String _arg9 = data.readString();
                    setCarrierTestOverride(_arg0175, _arg129, _arg224, _arg313, _arg49, _arg57, _arg66, _arg75, _arg8, _arg9);
                    reply.writeNoException();
                    return true;
                case 196:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0176 = data.readInt();
                    int _result101 = getCarrierIdListVersion(_arg0176);
                    reply.writeNoException();
                    reply.writeInt(_result101);
                    return true;
                case 197:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0177 = data.readInt();
                    refreshUiccProfile(_arg0177);
                    reply.writeNoException();
                    return true;
                case 198:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0178 = data.readInt();
                    int _result102 = getNumberOfModemsWithSimultaneousDataConnections(_arg0178, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result102);
                    return true;
                case 199:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0179 = data.readInt();
                    int _result103 = getNetworkSelectionMode(_arg0179);
                    reply.writeNoException();
                    reply.writeInt(_result103);
                    return true;
                case 200:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isInEmergencySmsMode = isInEmergencySmsMode();
                    reply.writeNoException();
                    reply.writeInt(isInEmergencySmsMode ? 1 : 0);
                    return true;
                case 201:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0180 = data.readInt();
                    String[] _result104 = getSmsApps(_arg0180);
                    reply.writeNoException();
                    reply.writeStringArray(_result104);
                    return true;
                case 202:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0181 = data.readInt();
                    String _result105 = getDefaultSmsApp(_arg0181);
                    reply.writeNoException();
                    reply.writeString(_result105);
                    return true;
                case 203:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0182 = data.readInt();
                    setDefaultSmsApp(_arg0182, data.readString());
                    reply.writeNoException();
                    return true;
                case 204:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0183 = data.readInt();
                    int _result106 = getRadioPowerState(_arg0183, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result106);
                    return true;
                case 205:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0184 = data.readInt();
                    registerImsRegistrationCallback(_arg0184, IImsRegistrationCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 206:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0185 = data.readInt();
                    unregisterImsRegistrationCallback(_arg0185, IImsRegistrationCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 207:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0186 = data.readInt();
                    registerMmTelCapabilityCallback(_arg0186, IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 208:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0187 = data.readInt();
                    unregisterMmTelCapabilityCallback(_arg0187, IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 209:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0188 = data.readInt();
                    int _arg130 = data.readInt();
                    int _arg225 = data.readInt();
                    boolean isCapable = isCapable(_arg0188, _arg130, _arg225);
                    reply.writeNoException();
                    reply.writeInt(isCapable ? 1 : 0);
                    return true;
                case 210:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0189 = data.readInt();
                    int _arg131 = data.readInt();
                    int _arg226 = data.readInt();
                    boolean isAvailable = isAvailable(_arg0189, _arg131, _arg226);
                    reply.writeNoException();
                    reply.writeInt(isAvailable ? 1 : 0);
                    return true;
                case 211:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0190 = data.readInt();
                    boolean isAdvancedCallingSettingEnabled = isAdvancedCallingSettingEnabled(_arg0190);
                    reply.writeNoException();
                    reply.writeInt(isAdvancedCallingSettingEnabled ? 1 : 0);
                    return true;
                case 212:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0191 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setAdvancedCallingSettingEnabled(_arg0191, _arg1);
                    reply.writeNoException();
                    return true;
                case 213:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0192 = data.readInt();
                    boolean isVtSettingEnabled = isVtSettingEnabled(_arg0192);
                    reply.writeNoException();
                    reply.writeInt(isVtSettingEnabled ? 1 : 0);
                    return true;
                case 214:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0193 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setVtSettingEnabled(_arg0193, _arg1);
                    reply.writeNoException();
                    return true;
                case 215:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0194 = data.readInt();
                    boolean isVoWiFiSettingEnabled = isVoWiFiSettingEnabled(_arg0194);
                    reply.writeNoException();
                    reply.writeInt(isVoWiFiSettingEnabled ? 1 : 0);
                    return true;
                case 216:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0195 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setVoWiFiSettingEnabled(_arg0195, _arg1);
                    reply.writeNoException();
                    return true;
                case 217:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0196 = data.readInt();
                    boolean isVoWiFiRoamingSettingEnabled = isVoWiFiRoamingSettingEnabled(_arg0196);
                    reply.writeNoException();
                    reply.writeInt(isVoWiFiRoamingSettingEnabled ? 1 : 0);
                    return true;
                case 218:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0197 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setVoWiFiRoamingSettingEnabled(_arg0197, _arg1);
                    reply.writeNoException();
                    return true;
                case 219:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0198 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    int _arg227 = data.readInt();
                    setVoWiFiNonPersistent(_arg0198, _arg1, _arg227);
                    reply.writeNoException();
                    return true;
                case 220:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0199 = data.readInt();
                    int _result107 = getVoWiFiModeSetting(_arg0199);
                    reply.writeNoException();
                    reply.writeInt(_result107);
                    return true;
                case 221:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0200 = data.readInt();
                    setVoWiFiModeSetting(_arg0200, data.readInt());
                    reply.writeNoException();
                    return true;
                case 222:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0201 = data.readInt();
                    int _result108 = getVoWiFiRoamingModeSetting(_arg0201);
                    reply.writeNoException();
                    reply.writeInt(_result108);
                    return true;
                case 223:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0202 = data.readInt();
                    setVoWiFiRoamingModeSetting(_arg0202, data.readInt());
                    reply.writeNoException();
                    return true;
                case 224:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0203 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setRttCapabilitySetting(_arg0203, _arg1);
                    reply.writeNoException();
                    return true;
                case 225:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0204 = data.readInt();
                    boolean isTtyOverVolteEnabled = isTtyOverVolteEnabled(_arg0204);
                    reply.writeNoException();
                    reply.writeInt(isTtyOverVolteEnabled ? 1 : 0);
                    return true;
                case 226:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0205 = data.readString();
                    Map _result109 = getEmergencyNumberList(_arg0205);
                    reply.writeNoException();
                    reply.writeMap(_result109);
                    return true;
                case 227:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0206 = data.readString();
                    _arg1 = data.readInt() != 0;
                    boolean isEmergencyNumber = isEmergencyNumber(_arg0206, _arg1);
                    reply.writeNoException();
                    reply.writeInt(isEmergencyNumber ? 1 : 0);
                    return true;
                case 228:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0207 = data.readInt();
                    List<String> _result110 = getCertsFromCarrierPrivilegeAccessRules(_arg0207);
                    reply.writeNoException();
                    reply.writeStringList(_result110);
                    return true;
                case 229:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0208 = data.readInt();
                    registerImsProvisioningChangedCallback(_arg0208, IImsConfigCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 230:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0209 = data.readInt();
                    unregisterImsProvisioningChangedCallback(_arg0209, IImsConfigCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 231:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0210 = data.readInt();
                    int _arg132 = data.readInt();
                    int _arg228 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setImsProvisioningStatusForCapability(_arg0210, _arg132, _arg228, _arg1);
                    reply.writeNoException();
                    return true;
                case 232:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0211 = data.readInt();
                    int _arg133 = data.readInt();
                    int _arg229 = data.readInt();
                    boolean imsProvisioningStatusForCapability = getImsProvisioningStatusForCapability(_arg0211, _arg133, _arg229);
                    reply.writeNoException();
                    reply.writeInt(imsProvisioningStatusForCapability ? 1 : 0);
                    return true;
                case 233:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0212 = data.readInt();
                    int _arg134 = data.readInt();
                    int _arg230 = data.readInt();
                    boolean isMmTelCapabilityProvisionedInCache = isMmTelCapabilityProvisionedInCache(_arg0212, _arg134, _arg230);
                    reply.writeNoException();
                    reply.writeInt(isMmTelCapabilityProvisionedInCache ? 1 : 0);
                    return true;
                case 234:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0213 = data.readInt();
                    int _arg135 = data.readInt();
                    int _arg231 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    cacheMmTelCapabilityProvisioning(_arg0213, _arg135, _arg231, _arg1);
                    reply.writeNoException();
                    return true;
                case 235:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0214 = data.readInt();
                    int _result111 = getImsProvisioningInt(_arg0214, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result111);
                    return true;
                case 236:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0215 = data.readInt();
                    String _result112 = getImsProvisioningString(_arg0215, data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result112);
                    return true;
                case 237:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0216 = data.readInt();
                    int _arg136 = data.readInt();
                    int _arg232 = data.readInt();
                    int _result113 = setImsProvisioningInt(_arg0216, _arg136, _arg232);
                    reply.writeNoException();
                    reply.writeInt(_result113);
                    return true;
                case 238:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0217 = data.readInt();
                    int _arg137 = data.readInt();
                    String _arg233 = data.readString();
                    int _result114 = setImsProvisioningString(_arg0217, _arg137, _arg233);
                    reply.writeNoException();
                    reply.writeInt(_result114);
                    return true;
                case 239:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0218 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg17 = EmergencyNumber.CREATOR.createFromParcel(data);
                    } else {
                        _arg17 = null;
                    }
                    updateEmergencyNumberListTestMode(_arg0218, _arg17);
                    reply.writeNoException();
                    return true;
                case 240:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result115 = getEmergencyNumberListTestMode();
                    reply.writeNoException();
                    reply.writeStringList(_result115);
                    return true;
                case 241:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0219 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    boolean enableModemForSlot = enableModemForSlot(_arg0219, _arg1);
                    reply.writeNoException();
                    reply.writeInt(enableModemForSlot ? 1 : 0);
                    return true;
                case 242:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0220 = _arg1;
                    setMultiSimCarrierRestriction(_arg0220);
                    reply.writeNoException();
                    return true;
                case 243:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0221 = data.readString();
                    int _result116 = isMultiSimSupported(_arg0221);
                    reply.writeNoException();
                    reply.writeInt(_result116);
                    return true;
                case 244:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0222 = data.readInt();
                    switchMultiSimConfig(_arg0222);
                    reply.writeNoException();
                    return true;
                case 245:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0223 = data.readInt();
                    boolean doesSwitchMultiSimConfigTriggerReboot = doesSwitchMultiSimConfigTriggerReboot(_arg0223, data.readString());
                    reply.writeNoException();
                    reply.writeInt(doesSwitchMultiSimConfigTriggerReboot ? 1 : 0);
                    return true;
                case 246:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result117 = getSlotsMapping();
                    reply.writeNoException();
                    reply.writeIntArray(_result117);
                    return true;
                case 247:
                    data.enforceInterface(DESCRIPTOR);
                    int _result118 = getRadioHalVersion();
                    reply.writeNoException();
                    reply.writeInt(_result118);
                    return true;
                case 248:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0224 = data.readInt();
                    boolean isModemEnabledForSlot = isModemEnabledForSlot(_arg0224, data.readString());
                    reply.writeNoException();
                    reply.writeInt(isModemEnabledForSlot ? 1 : 0);
                    return true;
                case 249:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0225 = data.readInt();
                    int _arg138 = data.readInt();
                    String _arg234 = data.readString();
                    boolean isDataEnabledForApn = isDataEnabledForApn(_arg0225, _arg138, _arg234);
                    reply.writeNoException();
                    reply.writeInt(isDataEnabledForApn ? 1 : 0);
                    return true;
                case 250:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0226 = data.readInt();
                    boolean isApnMetered = isApnMetered(_arg0226, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(isApnMetered ? 1 : 0);
                    return true;
                case 251:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0227 = data.readString();
                    enqueueSmsPickResult(_arg0227, IIntegerConsumer.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 252:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0228 = data.readInt();
                    String _result119 = getMmsUserAgent(_arg0228);
                    reply.writeNoException();
                    reply.writeString(_result119);
                    return true;
                case 253:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0229 = data.readInt();
                    String _result120 = getMmsUAProfUrl(_arg0229);
                    reply.writeNoException();
                    reply.writeString(_result120);
                    return true;
                case 254:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0230 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    boolean dataAllowedDuringVoiceCall = setDataAllowedDuringVoiceCall(_arg0230, _arg1);
                    reply.writeNoException();
                    reply.writeInt(dataAllowedDuringVoiceCall ? 1 : 0);
                    return true;
                case 255:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0231 = data.readInt();
                    boolean isDataAllowedInVoiceCall = isDataAllowedInVoiceCall(_arg0231);
                    reply.writeNoException();
                    reply.writeInt(isDataAllowedInVoiceCall ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements ITelephony {
            public static ITelephony sDefaultImpl;
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

            @Override // com.android.internal.telephony.ITelephony
            public void dial(String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dial(number);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void call(String callingPackage, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(number);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().call(callingPackage, number);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isRadioOn(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isRadioOn(callingPackage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isRadioOnForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isRadioOnForSubscriber(subId, callingPackage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean supplyPin(String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pin);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supplyPin(pin);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean supplyPinForSubscriber(int subId, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pin);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supplyPinForSubscriber(subId, pin);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean supplyPuk(String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supplyPuk(puk, pin);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean supplyPukForSubscriber(int subId, String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supplyPukForSubscriber(subId, puk, pin);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int[] supplyPinReportResult(String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pin);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supplyPinReportResult(pin);
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int[] supplyPinReportResultForSubscriber(int subId, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pin);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supplyPinReportResultForSubscriber(subId, pin);
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int[] supplyPukReportResult(String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supplyPukReportResult(puk, pin);
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int[] supplyPukReportResultForSubscriber(int subId, String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supplyPukReportResultForSubscriber(subId, puk, pin);
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean handlePinMmi(String dialString) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dialString);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().handlePinMmi(dialString);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void handleUssdRequest(int subId, String ussdRequest, ResultReceiver wrappedCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(ussdRequest);
                    if (wrappedCallback != null) {
                        _data.writeInt(1);
                        wrappedCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleUssdRequest(subId, ussdRequest, wrappedCallback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean handlePinMmiForSubscriber(int subId, String dialString) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(dialString);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().handlePinMmiForSubscriber(subId, dialString);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void toggleRadioOnOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().toggleRadioOnOff();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void toggleRadioOnOffForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().toggleRadioOnOffForSubscriber(subId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setRadio(boolean turnOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(turnOn ? 1 : 0);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setRadio(turnOn);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setRadioForSubscriber(int subId, boolean turnOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(turnOn ? 1 : 0);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setRadioForSubscriber(subId, turnOn);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setRadioPower(boolean turnOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(turnOn ? 1 : 0);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setRadioPower(turnOn);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void updateServiceLocation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateServiceLocation();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void updateServiceLocationForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateServiceLocationForSubscriber(subId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void enableLocationUpdates() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enableLocationUpdates();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void enableLocationUpdatesForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enableLocationUpdatesForSubscriber(subId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void disableLocationUpdates() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disableLocationUpdates();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void disableLocationUpdatesForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disableLocationUpdatesForSubscriber(subId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean enableDataConnectivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enableDataConnectivity();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean disableDataConnectivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().disableDataConnectivity();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isDataConnectivityPossible(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDataConnectivityPossible(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public Bundle getCellLocation(String callingPkg) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCellLocation(callingPkg);
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

            @Override // com.android.internal.telephony.ITelephony
            public String getNetworkCountryIsoForPhone(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNetworkCountryIsoForPhone(phoneId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public List<NeighboringCellInfo> getNeighboringCellInfo(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNeighboringCellInfo(callingPkg);
                    }
                    _reply.readException();
                    List<NeighboringCellInfo> _result = _reply.createTypedArrayList(NeighboringCellInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCallState();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCallStateForSlot(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCallStateForSlot(slotIndex);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getDataActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDataActivity();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getDataActivityForSubId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDataActivityForSubId(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getDataState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDataState();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getDataStateForSubId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDataStateForSubId(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getActivePhoneType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActivePhoneType();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getActivePhoneTypeForSlot(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActivePhoneTypeForSlot(slotIndex);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCdmaEriIconIndex(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCdmaEriIconIndex(callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCdmaEriIconIndexForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCdmaEriIconIndexForSubscriber(subId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCdmaEriIconMode(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCdmaEriIconMode(callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCdmaEriIconModeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCdmaEriIconModeForSubscriber(subId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getCdmaEriText(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCdmaEriText(callingPackage);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getCdmaEriTextForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCdmaEriTextForSubscriber(subId, callingPackage);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean needsOtaServiceProvisioning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().needsOtaServiceProvisioning();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setVoiceMailNumber(int subId, String alphaTag, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(alphaTag);
                    _data.writeString(number);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setVoiceMailNumber(subId, alphaTag, number);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setVoiceActivationState(int subId, int activationState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(activationState);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoiceActivationState(subId, activationState);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setDataActivationState(int subId, int activationState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(activationState);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDataActivationState(subId, activationState);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getVoiceActivationState(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVoiceActivationState(subId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getDataActivationState(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDataActivationState(subId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getVoiceMessageCountForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVoiceMessageCountForSubscriber(subId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isConcurrentVoiceAndDataAllowed(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isConcurrentVoiceAndDataAllowed(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public Bundle getVisualVoicemailSettings(String callingPackage, int subId) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVisualVoicemailSettings(callingPackage, subId);
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

            @Override // com.android.internal.telephony.ITelephony
            public String getVisualVoicemailPackageName(String callingPackage, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVisualVoicemailPackageName(callingPackage, subId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void enableVisualVoicemailSmsFilter(String callingPackage, int subId, VisualVoicemailSmsFilterSettings settings) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    if (settings != null) {
                        _data.writeInt(1);
                        settings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enableVisualVoicemailSmsFilter(callingPackage, subId, settings);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void disableVisualVoicemailSmsFilter(String callingPackage, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(58, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disableVisualVoicemailSmsFilter(callingPackage, subId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(String callingPackage, int subId) throws RemoteException {
                VisualVoicemailSmsFilterSettings _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVisualVoicemailSmsFilterSettings(callingPackage, subId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VisualVoicemailSmsFilterSettings.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int subId) throws RemoteException {
                VisualVoicemailSmsFilterSettings _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActiveVisualVoicemailSmsFilterSettings(subId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VisualVoicemailSmsFilterSettings.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void sendVisualVoicemailSmsForSubscriber(String callingPackage, int subId, String number, int port, String text, PendingIntent sentIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(callingPackage);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(subId);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(number);
                    try {
                        _data.writeInt(port);
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(text);
                        if (sentIntent != null) {
                            _data.writeInt(1);
                            sentIntent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().sendVisualVoicemailSmsForSubscriber(callingPackage, subId, number, port, text, sentIntent);
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
                } catch (Throwable th6) {
                    th = th6;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void sendDialerSpecialCode(String callingPackageName, String inputCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackageName);
                    _data.writeString(inputCode);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendDialerSpecialCode(callingPackageName, inputCode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNetworkTypeForSubscriber(subId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getDataNetworkType(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDataNetworkType(callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getDataNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDataNetworkTypeForSubscriber(subId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getVoiceNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVoiceNetworkTypeForSubscriber(subId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean hasIccCard() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasIccCard();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean hasIccCardUsingSlotIndex(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasIccCardUsingSlotIndex(slotIndex);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getLteOnCdmaMode(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLteOnCdmaMode(callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getLteOnCdmaModeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLteOnCdmaModeForSubscriber(subId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public List<CellInfo> getAllCellInfo(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllCellInfo(callingPkg);
                    }
                    _reply.readException();
                    List<CellInfo> _result = _reply.createTypedArrayList(CellInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void requestCellInfoUpdate(int subId, ICellInfoCallback cb, String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeString(callingPkg);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestCellInfoUpdate(subId, cb, callingPkg);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void requestCellInfoUpdateWithWorkSource(int subId, ICellInfoCallback cb, String callingPkg, WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeString(callingPkg);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestCellInfoUpdateWithWorkSource(subId, cb, callingPkg, ws);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setCellInfoListRate(int rateInMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rateInMillis);
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCellInfoListRate(rateInMillis);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public IccOpenLogicalChannelResponse iccOpenLogicalChannelBySlot(int slotIndex, String callingPackage, String AID, int p2) throws RemoteException {
                IccOpenLogicalChannelResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    _data.writeString(AID);
                    _data.writeInt(p2);
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().iccOpenLogicalChannelBySlot(slotIndex, callingPackage, AID, p2);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = IccOpenLogicalChannelResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public IccOpenLogicalChannelResponse iccOpenLogicalChannel(int subId, String callingPackage, String AID, int p2) throws RemoteException {
                IccOpenLogicalChannelResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    _data.writeString(AID);
                    _data.writeInt(p2);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().iccOpenLogicalChannel(subId, callingPackage, AID, p2);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = IccOpenLogicalChannelResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean iccCloseLogicalChannelBySlot(int slotIndex, int channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(channel);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().iccCloseLogicalChannelBySlot(slotIndex, channel);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean iccCloseLogicalChannel(int subId, int channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(channel);
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().iccCloseLogicalChannel(subId, channel);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String iccTransmitApduLogicalChannelBySlot(int slotIndex, int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(slotIndex);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(channel);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(cla);
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(instruction);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String iccTransmitApduLogicalChannelBySlot = Stub.getDefaultImpl().iccTransmitApduLogicalChannelBySlot(slotIndex, channel, cla, instruction, p1, p2, p3, data);
                        _reply.recycle();
                        _data.recycle();
                        return iccTransmitApduLogicalChannelBySlot;
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String iccTransmitApduLogicalChannel(int subId, int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(subId);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(channel);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(cla);
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(instruction);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String iccTransmitApduLogicalChannel = Stub.getDefaultImpl().iccTransmitApduLogicalChannel(subId, channel, cla, instruction, p1, p2, p3, data);
                        _reply.recycle();
                        _data.recycle();
                        return iccTransmitApduLogicalChannel;
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String iccTransmitApduBasicChannelBySlot(int slotIndex, String callingPackage, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(slotIndex);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(callingPackage);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(cla);
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(instruction);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String iccTransmitApduBasicChannelBySlot = Stub.getDefaultImpl().iccTransmitApduBasicChannelBySlot(slotIndex, callingPackage, cla, instruction, p1, p2, p3, data);
                        _reply.recycle();
                        _data.recycle();
                        return iccTransmitApduBasicChannelBySlot;
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String iccTransmitApduBasicChannel(int subId, String callingPackage, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(subId);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(callingPackage);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(cla);
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(instruction);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String iccTransmitApduBasicChannel = Stub.getDefaultImpl().iccTransmitApduBasicChannel(subId, callingPackage, cla, instruction, p1, p2, p3, data);
                        _reply.recycle();
                        _data.recycle();
                        return iccTransmitApduBasicChannel;
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public byte[] iccExchangeSimIO(int subId, int fileID, int command, int p1, int p2, int p3, String filePath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(subId);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(fileID);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(command);
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(p1);
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(filePath);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        byte[] iccExchangeSimIO = Stub.getDefaultImpl().iccExchangeSimIO(subId, fileID, command, p1, p2, p3, filePath);
                        _reply.recycle();
                        _data.recycle();
                        return iccExchangeSimIO;
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th6) {
                    th = th6;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String sendEnvelopeWithStatus(int subId, String content) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(content);
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().sendEnvelopeWithStatus(subId, content);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String nvReadItem(int itemID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(itemID);
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().nvReadItem(itemID);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean nvWriteItem(int itemID, String itemValue) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(itemID);
                    _data.writeString(itemValue);
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().nvWriteItem(itemID, itemValue);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean nvWriteCdmaPrl(byte[] preferredRoamingList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(preferredRoamingList);
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().nvWriteCdmaPrl(preferredRoamingList);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean resetModemConfig(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resetModemConfig(slotIndex);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean rebootModem(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().rebootModem(slotIndex);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCalculatedPreferredNetworkType(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(90, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCalculatedPreferredNetworkType(callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getPreferredNetworkType(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(91, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPreferredNetworkType(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean getTetherApnRequiredForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(92, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTetherApnRequiredForSubscriber(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void enableIms(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    boolean _status = this.mRemote.transact(93, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enableIms(slotId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void disableIms(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    boolean _status = this.mRemote.transact(94, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disableIms(slotId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public IImsMmTelFeature getMmTelFeatureAndListen(int slotId, IImsServiceFeatureCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(95, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMmTelFeatureAndListen(slotId, callback);
                    }
                    _reply.readException();
                    IImsMmTelFeature _result = IImsMmTelFeature.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public IImsRcsFeature getRcsFeatureAndListen(int slotId, IImsServiceFeatureCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(96, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRcsFeatureAndListen(slotId, callback);
                    }
                    _reply.readException();
                    IImsRcsFeature _result = IImsRcsFeature.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public IImsRegistration getImsRegistration(int slotId, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(feature);
                    boolean _status = this.mRemote.transact(97, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getImsRegistration(slotId, feature);
                    }
                    _reply.readException();
                    IImsRegistration _result = IImsRegistration.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public IImsConfig getImsConfig(int slotId, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(feature);
                    boolean _status = this.mRemote.transact(98, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getImsConfig(slotId, feature);
                    }
                    _reply.readException();
                    IImsConfig _result = IImsConfig.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setImsService(int slotId, boolean isCarrierImsService, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(isCarrierImsService ? 1 : 0);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(99, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setImsService(slotId, isCarrierImsService, packageName);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getImsService(int slotId, boolean isCarrierImsService) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(isCarrierImsService ? 1 : 0);
                    boolean _status = this.mRemote.transact(100, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getImsService(slotId, isCarrierImsService);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setNetworkSelectionModeAutomatic(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(101, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNetworkSelectionModeAutomatic(subId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public CellNetworkScanResult getCellNetworkScanResults(int subId, String callingPackage) throws RemoteException {
                CellNetworkScanResult _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(102, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCellNetworkScanResults(subId, callingPackage);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = CellNetworkScanResult.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int requestNetworkScan(int subId, NetworkScanRequest request, Messenger messenger, IBinder binder, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(103, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().requestNetworkScan(subId, request, messenger, binder, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void stopNetworkScan(int subId, int scanId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(scanId);
                    boolean _status = this.mRemote.transact(104, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopNetworkScan(subId, scanId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setNetworkSelectionModeManual(int subId, OperatorInfo operatorInfo, boolean persisSelection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (operatorInfo != null) {
                        _data.writeInt(1);
                        operatorInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(persisSelection ? 1 : 0);
                    boolean _status = this.mRemote.transact(105, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setNetworkSelectionModeManual(subId, operatorInfo, persisSelection);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setPreferredNetworkType(int subId, int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(networkType);
                    boolean _status = this.mRemote.transact(106, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPreferredNetworkType(subId, networkType);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setUserDataEnabled(int subId, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(107, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserDataEnabled(subId, enable);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean getDataEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(108, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDataEnabled(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isUserDataEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(109, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUserDataEnabled(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isDataEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(110, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDataEnabled(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isManualNetworkSelectionAllowed(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(111, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isManualNetworkSelectionAllowed(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String[] getPcscfAddress(String apnType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apnType);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(112, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPcscfAddress(apnType, callingPackage);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setImsRegistrationState(boolean registered) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(registered ? 1 : 0);
                    boolean _status = this.mRemote.transact(113, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setImsRegistrationState(registered);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getCdmaMdn(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(114, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCdmaMdn(subId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getCdmaMin(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(115, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCdmaMin(subId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void requestNumberVerification(PhoneNumberRange range, long timeoutMillis, INumberVerificationCallback callback, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (range != null) {
                        _data.writeInt(1);
                        range.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timeoutMillis);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(116, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestNumberVerification(range, timeoutMillis, callback, callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCarrierPrivilegeStatus(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(117, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCarrierPrivilegeStatus(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCarrierPrivilegeStatusForUid(int subId, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(118, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCarrierPrivilegeStatusForUid(subId, uid);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int checkCarrierPrivilegesForPackage(int subId, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pkgName);
                    boolean _status = this.mRemote.transact(119, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkCarrierPrivilegesForPackage(subId, pkgName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int checkCarrierPrivilegesForPackageAnyPhone(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    boolean _status = this.mRemote.transact(120, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkCarrierPrivilegesForPackageAnyPhone(pkgName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int phoneId) throws RemoteException {
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
                    _data.writeInt(phoneId);
                    boolean _status = this.mRemote.transact(121, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCarrierPackageNamesForIntentAndPhone(intent, phoneId);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setLine1NumberForDisplayForSubscriber(int subId, String alphaTag, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(alphaTag);
                    _data.writeString(number);
                    boolean _status = this.mRemote.transact(122, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setLine1NumberForDisplayForSubscriber(subId, alphaTag, number);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getLine1NumberForDisplay(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(123, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLine1NumberForDisplay(subId, callingPackage);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getLine1AlphaTagForDisplay(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(124, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLine1AlphaTagForDisplay(subId, callingPackage);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String[] getMergedSubscriberIds(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(125, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMergedSubscriberIds(subId, callingPackage);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String[] getMergedSubscriberIdsFromGroup(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(126, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMergedSubscriberIdsFromGroup(subId, callingPackage);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setOperatorBrandOverride(int subId, String brand) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(brand);
                    boolean _status = this.mRemote.transact(127, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setOperatorBrandOverride(subId, brand);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setRoamingOverride(int subId, List<String> gsmRoamingList, List<String> gsmNonRoamingList, List<String> cdmaRoamingList, List<String> cdmaNonRoamingList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(subId);
                    try {
                        _data.writeStringList(gsmRoamingList);
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeStringList(gsmNonRoamingList);
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeStringList(cdmaRoamingList);
                        try {
                            _data.writeStringList(cdmaNonRoamingList);
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
                        boolean _status = this.mRemote.transact(128, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            boolean roamingOverride = Stub.getDefaultImpl().setRoamingOverride(subId, gsmRoamingList, gsmNonRoamingList, cdmaRoamingList, cdmaNonRoamingList);
                            _reply.recycle();
                            _data.recycle();
                            return roamingOverride;
                        }
                        _reply.readException();
                        boolean _status2 = _reply.readInt() != 0;
                        _reply.recycle();
                        _data.recycle();
                        return _status2;
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

            @Override // com.android.internal.telephony.ITelephony
            public int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(oemReq);
                    if (oemResp == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(oemResp.length);
                    }
                    boolean _status = this.mRemote.transact(129, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().invokeOemRilRequestRaw(oemReq, oemResp);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(oemResp);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean needMobileRadioShutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(130, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().needMobileRadioShutdown();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void shutdownMobileRadios() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(131, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().shutdownMobileRadios();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setRadioCapability(RadioAccessFamily[] rafs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(rafs, 0);
                    boolean _status = this.mRemote.transact(132, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRadioCapability(rafs);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getRadioAccessFamily(int phoneId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(133, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRadioAccessFamily(phoneId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void enableVideoCalling(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(134, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enableVideoCalling(enable);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isVideoCallingEnabled(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(135, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isVideoCallingEnabled(callingPackage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean canChangeDtmfToneLength(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(136, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().canChangeDtmfToneLength(subId, callingPackage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isWorldPhone(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(137, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isWorldPhone(subId, callingPackage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isTtyModeSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(138, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isTtyModeSupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isRttSupported(int subscriptionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subscriptionId);
                    boolean _status = this.mRemote.transact(139, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isRttSupported(subscriptionId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isHearingAidCompatibilitySupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(140, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isHearingAidCompatibilitySupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isImsRegistered(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(141, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isImsRegistered(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isWifiCallingAvailable(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(142, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isWifiCallingAvailable(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isVideoTelephonyAvailable(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(143, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isVideoTelephonyAvailable(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getImsRegTechnologyForMmTel(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(144, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getImsRegTechnologyForMmTel(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getDeviceId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(145, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceId(callingPackage);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getImeiForSlot(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(146, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getImeiForSlot(slotIndex, callingPackage);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getTypeAllocationCodeForSlot(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    boolean _status = this.mRemote.transact(147, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTypeAllocationCodeForSlot(slotIndex);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getMeidForSlot(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(148, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMeidForSlot(slotIndex, callingPackage);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getManufacturerCodeForSlot(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    boolean _status = this.mRemote.transact(149, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getManufacturerCodeForSlot(slotIndex);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getDeviceSoftwareVersionForSlot(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(150, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceSoftwareVersionForSlot(slotIndex, callingPackage);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getSubIdForPhoneAccount(PhoneAccount phoneAccount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccount != null) {
                        _data.writeInt(1);
                        phoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(151, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSubIdForPhoneAccount(phoneAccount);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public PhoneAccountHandle getPhoneAccountHandleForSubscriptionId(int subscriptionId) throws RemoteException {
                PhoneAccountHandle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subscriptionId);
                    boolean _status = this.mRemote.transact(152, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPhoneAccountHandleForSubscriptionId(subscriptionId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PhoneAccountHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void factoryReset(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(153, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().factoryReset(subId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getSimLocaleForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(154, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSimLocaleForSubscriber(subId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void requestModemActivityInfo(ResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(155, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestModemActivityInfo(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public ServiceState getServiceStateForSubscriber(int subId, String callingPackage) throws RemoteException {
                ServiceState _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(156, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getServiceStateForSubscriber(subId, callingPackage);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ServiceState.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public Uri getVoicemailRingtoneUri(PhoneAccountHandle accountHandle) throws RemoteException {
                Uri _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(157, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVoicemailRingtoneUri(accountHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setVoicemailRingtoneUri(String callingPackage, PhoneAccountHandle phoneAccountHandle, Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (phoneAccountHandle != null) {
                        _data.writeInt(1);
                        phoneAccountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(158, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoicemailRingtoneUri(callingPackage, phoneAccountHandle, uri);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isVoicemailVibrationEnabled(PhoneAccountHandle accountHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(159, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isVoicemailVibrationEnabled(accountHandle);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setVoicemailVibrationEnabled(String callingPackage, PhoneAccountHandle phoneAccountHandle, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 1;
                    if (phoneAccountHandle != null) {
                        _data.writeInt(1);
                        phoneAccountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(160, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoicemailVibrationEnabled(callingPackage, phoneAccountHandle, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public List<String> getPackagesWithCarrierPrivileges(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    boolean _status = this.mRemote.transact(161, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackagesWithCarrierPrivileges(phoneId);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public List<String> getPackagesWithCarrierPrivilegesForAllPhones() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(162, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPackagesWithCarrierPrivilegesForAllPhones();
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getAidForAppType(int subId, int appType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(appType);
                    boolean _status = this.mRemote.transact(163, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAidForAppType(subId, appType);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getEsn(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(164, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEsn(subId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getCdmaPrlVersion(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(165, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCdmaPrlVersion(subId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public List<TelephonyHistogram> getTelephonyHistograms() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(166, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTelephonyHistograms();
                    }
                    _reply.readException();
                    List<TelephonyHistogram> _result = _reply.createTypedArrayList(TelephonyHistogram.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int setAllowedCarriers(CarrierRestrictionRules carrierRestrictionRules) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (carrierRestrictionRules != null) {
                        _data.writeInt(1);
                        carrierRestrictionRules.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(167, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setAllowedCarriers(carrierRestrictionRules);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public CarrierRestrictionRules getAllowedCarriers() throws RemoteException {
                CarrierRestrictionRules _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(168, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllowedCarriers();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = CarrierRestrictionRules.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getSubscriptionCarrierId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(169, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSubscriptionCarrierId(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getSubscriptionCarrierName(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(170, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSubscriptionCarrierName(subId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getSubscriptionSpecificCarrierId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(171, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSubscriptionSpecificCarrierId(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getSubscriptionSpecificCarrierName(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(172, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSubscriptionSpecificCarrierName(subId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCarrierIdFromMccMnc(int slotIndex, String mccmnc, boolean isSubscriptionMccMnc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(mccmnc);
                    _data.writeInt(isSubscriptionMccMnc ? 1 : 0);
                    boolean _status = this.mRemote.transact(173, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCarrierIdFromMccMnc(slotIndex, mccmnc, isSubscriptionMccMnc);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void carrierActionSetMeteredApnsEnabled(int subId, boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(visible ? 1 : 0);
                    boolean _status = this.mRemote.transact(174, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().carrierActionSetMeteredApnsEnabled(subId, visible);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void carrierActionSetRadioEnabled(int subId, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(175, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().carrierActionSetRadioEnabled(subId, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void carrierActionReportDefaultNetworkStatus(int subId, boolean report) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(report ? 1 : 0);
                    boolean _status = this.mRemote.transact(176, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().carrierActionReportDefaultNetworkStatus(subId, report);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void carrierActionResetAll(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(177, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().carrierActionResetAll(subId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public NetworkStats getVtDataUsage(int subId, boolean perUidStats) throws RemoteException {
                NetworkStats _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(perUidStats ? 1 : 0);
                    boolean _status = this.mRemote.transact(178, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVtDataUsage(subId, perUidStats);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NetworkStats.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setPolicyDataEnabled(boolean enabled, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(179, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPolicyDataEnabled(enabled, subId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public List<ClientRequestStats> getClientRequestStats(String callingPackage, int subid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subid);
                    boolean _status = this.mRemote.transact(180, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getClientRequestStats(callingPackage, subid);
                    }
                    _reply.readException();
                    List<ClientRequestStats> _result = _reply.createTypedArrayList(ClientRequestStats.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setSimPowerStateForSlot(int slotIndex, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(181, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSimPowerStateForSlot(slotIndex, state);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String[] getForbiddenPlmns(int subId, int appType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(appType);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(182, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getForbiddenPlmns(subId, appType, callingPackage);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean getEmergencyCallbackMode(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(183, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEmergencyCallbackMode(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public SignalStrength getSignalStrength(int subId) throws RemoteException {
                SignalStrength _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(184, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSignalStrength(subId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SignalStrength.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCardIdForDefaultEuicc(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(185, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCardIdForDefaultEuicc(subId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public List<UiccCardInfo> getUiccCardsInfo(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(186, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUiccCardsInfo(callingPackage);
                    }
                    _reply.readException();
                    List<UiccCardInfo> _result = _reply.createTypedArrayList(UiccCardInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public UiccSlotInfo[] getUiccSlotsInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(187, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUiccSlotsInfo();
                    }
                    _reply.readException();
                    UiccSlotInfo[] _result = (UiccSlotInfo[]) _reply.createTypedArray(UiccSlotInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean switchSlots(int[] physicalSlots) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(physicalSlots);
                    boolean _status = this.mRemote.transact(188, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().switchSlots(physicalSlots);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setRadioIndicationUpdateMode(int subId, int filters, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(filters);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(189, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRadioIndicationUpdateMode(subId, filters, mode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isDataRoamingEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(190, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDataRoamingEnabled(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setDataRoamingEnabled(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(191, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDataRoamingEnabled(subId, isEnabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getCdmaRoamingMode(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(192, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCdmaRoamingMode(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setCdmaRoamingMode(int subId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(193, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setCdmaRoamingMode(subId, mode);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setCdmaSubscriptionMode(int subId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(194, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setCdmaSubscriptionMode(subId, mode);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setCarrierTestOverride(int subId, String mccmnc, String imsi, String iccid, String gid1, String gid2, String plmn, String spn, String carrierPrivilegeRules, String apn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(subId);
                    try {
                        _data.writeString(mccmnc);
                        _data.writeString(imsi);
                        _data.writeString(iccid);
                        _data.writeString(gid1);
                        _data.writeString(gid2);
                        _data.writeString(plmn);
                        _data.writeString(spn);
                        _data.writeString(carrierPrivilegeRules);
                        _data.writeString(apn);
                        boolean _status = this.mRemote.transact(195, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().setCarrierTestOverride(subId, mccmnc, imsi, iccid, gid1, gid2, plmn, spn, carrierPrivilegeRules, apn);
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
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

            @Override // com.android.internal.telephony.ITelephony
            public int getCarrierIdListVersion(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(196, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCarrierIdListVersion(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void refreshUiccProfile(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(197, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().refreshUiccProfile(subId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getNumberOfModemsWithSimultaneousDataConnections(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(198, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNumberOfModemsWithSimultaneousDataConnections(subId, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getNetworkSelectionMode(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(199, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNetworkSelectionMode(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isInEmergencySmsMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(200, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isInEmergencySmsMode();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String[] getSmsApps(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(201, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSmsApps(userId);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getDefaultSmsApp(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(202, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDefaultSmsApp(userId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setDefaultSmsApp(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(203, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDefaultSmsApp(userId, packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getRadioPowerState(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(204, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRadioPowerState(slotIndex, callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void registerImsRegistrationCallback(int subId, IImsRegistrationCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    boolean _status = this.mRemote.transact(205, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerImsRegistrationCallback(subId, c);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void unregisterImsRegistrationCallback(int subId, IImsRegistrationCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    boolean _status = this.mRemote.transact(206, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterImsRegistrationCallback(subId, c);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void registerMmTelCapabilityCallback(int subId, IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    boolean _status = this.mRemote.transact(207, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerMmTelCapabilityCallback(subId, c);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void unregisterMmTelCapabilityCallback(int subId, IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    boolean _status = this.mRemote.transact(208, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterMmTelCapabilityCallback(subId, c);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isCapable(int subId, int capability, int regTech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(regTech);
                    boolean _status = this.mRemote.transact(209, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCapable(subId, capability, regTech);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isAvailable(int subId, int capability, int regTech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(regTech);
                    boolean _status = this.mRemote.transact(210, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAvailable(subId, capability, regTech);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isAdvancedCallingSettingEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(211, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAdvancedCallingSettingEnabled(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setAdvancedCallingSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(212, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAdvancedCallingSettingEnabled(subId, isEnabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isVtSettingEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(213, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isVtSettingEnabled(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setVtSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(214, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVtSettingEnabled(subId, isEnabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isVoWiFiSettingEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(215, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isVoWiFiSettingEnabled(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setVoWiFiSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(216, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoWiFiSettingEnabled(subId, isEnabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isVoWiFiRoamingSettingEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(217, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isVoWiFiRoamingSettingEnabled(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setVoWiFiRoamingSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(218, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoWiFiRoamingSettingEnabled(subId, isEnabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setVoWiFiNonPersistent(int subId, boolean isCapable, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isCapable ? 1 : 0);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(219, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoWiFiNonPersistent(subId, isCapable, mode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getVoWiFiModeSetting(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(220, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVoWiFiModeSetting(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setVoWiFiModeSetting(int subId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(221, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoWiFiModeSetting(subId, mode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getVoWiFiRoamingModeSetting(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(222, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVoWiFiRoamingModeSetting(subId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setVoWiFiRoamingModeSetting(int subId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(223, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoWiFiRoamingModeSetting(subId, mode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setRttCapabilitySetting(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(224, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRttCapabilitySetting(subId, isEnabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isTtyOverVolteEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(225, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isTtyOverVolteEnabled(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public Map getEmergencyNumberList(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(226, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEmergencyNumberList(callingPackage);
                    }
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isEmergencyNumber(String number, boolean exactMatch) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    _data.writeInt(exactMatch ? 1 : 0);
                    boolean _status = this.mRemote.transact(227, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isEmergencyNumber(number, exactMatch);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public List<String> getCertsFromCarrierPrivilegeAccessRules(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(228, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCertsFromCarrierPrivilegeAccessRules(subId);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void registerImsProvisioningChangedCallback(int subId, IImsConfigCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(229, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerImsProvisioningChangedCallback(subId, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void unregisterImsProvisioningChangedCallback(int subId, IImsConfigCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(230, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterImsProvisioningChangedCallback(subId, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setImsProvisioningStatusForCapability(int subId, int capability, int tech, boolean isProvisioned) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(tech);
                    _data.writeInt(isProvisioned ? 1 : 0);
                    boolean _status = this.mRemote.transact(231, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setImsProvisioningStatusForCapability(subId, capability, tech, isProvisioned);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean getImsProvisioningStatusForCapability(int subId, int capability, int tech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(tech);
                    boolean _status = this.mRemote.transact(232, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getImsProvisioningStatusForCapability(subId, capability, tech);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isMmTelCapabilityProvisionedInCache(int subId, int capability, int tech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(tech);
                    boolean _status = this.mRemote.transact(233, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMmTelCapabilityProvisionedInCache(subId, capability, tech);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void cacheMmTelCapabilityProvisioning(int subId, int capability, int tech, boolean isProvisioned) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(tech);
                    _data.writeInt(isProvisioned ? 1 : 0);
                    boolean _status = this.mRemote.transact(234, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cacheMmTelCapabilityProvisioning(subId, capability, tech, isProvisioned);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getImsProvisioningInt(int subId, int key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(key);
                    boolean _status = this.mRemote.transact(235, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getImsProvisioningInt(subId, key);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getImsProvisioningString(int subId, int key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(key);
                    boolean _status = this.mRemote.transact(236, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getImsProvisioningString(subId, key);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int setImsProvisioningInt(int subId, int key, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(key);
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(237, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setImsProvisioningInt(subId, key, value);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int setImsProvisioningString(int subId, int key, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(key);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(238, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setImsProvisioningString(subId, key, value);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void updateEmergencyNumberListTestMode(int action, EmergencyNumber num) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(action);
                    if (num != null) {
                        _data.writeInt(1);
                        num.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(239, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateEmergencyNumberListTestMode(action, num);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public List<String> getEmergencyNumberListTestMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(240, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEmergencyNumberListTestMode();
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean enableModemForSlot(int slotIndex, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(241, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enableModemForSlot(slotIndex, enable);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void setMultiSimCarrierRestriction(boolean isMultiSimCarrierRestricted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isMultiSimCarrierRestricted ? 1 : 0);
                    boolean _status = this.mRemote.transact(242, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMultiSimCarrierRestriction(isMultiSimCarrierRestricted);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int isMultiSimSupported(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(243, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMultiSimSupported(callingPackage);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void switchMultiSimConfig(int numOfSims) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(numOfSims);
                    boolean _status = this.mRemote.transact(244, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().switchMultiSimConfig(numOfSims);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean doesSwitchMultiSimConfigTriggerReboot(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(245, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().doesSwitchMultiSimConfigTriggerReboot(subId, callingPackage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int[] getSlotsMapping() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(246, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSlotsMapping();
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public int getRadioHalVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(247, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRadioHalVersion();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isModemEnabledForSlot(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(248, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isModemEnabledForSlot(slotIndex, callingPackage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isDataEnabledForApn(int apnType, int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apnType);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(249, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDataEnabledForApn(apnType, subId, callingPackage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isApnMetered(int apnType, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apnType);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(250, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isApnMetered(apnType, subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public void enqueueSmsPickResult(String callingPackage, IIntegerConsumer subIdResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(subIdResult != null ? subIdResult.asBinder() : null);
                    boolean _status = this.mRemote.transact(251, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enqueueSmsPickResult(callingPackage, subIdResult);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getMmsUserAgent(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(252, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMmsUserAgent(subId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public String getMmsUAProfUrl(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(253, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMmsUAProfUrl(subId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean setDataAllowedDuringVoiceCall(int subId, boolean allow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(allow ? 1 : 0);
                    boolean _status = this.mRemote.transact(254, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setDataAllowedDuringVoiceCall(subId, allow);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public boolean isDataAllowedInVoiceCall(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(255, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDataAllowedInVoiceCall(subId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ITelephony impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ITelephony getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
