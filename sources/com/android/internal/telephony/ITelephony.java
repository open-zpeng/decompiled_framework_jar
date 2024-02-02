package com.android.internal.telephony;

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
import android.service.carrier.CarrierIdentifier;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telephony.CellInfo;
import android.telephony.ClientRequestStats;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.NeighboringCellInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyHistogram;
import android.telephony.UiccSlotInfo;
import android.telephony.VisualVoicemailSmsFilterSettings;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsRcsFeature;
import android.telephony.ims.aidl.IImsRegistration;
import com.android.ims.internal.IImsServiceFeatureCallback;
import java.util.List;
/* loaded from: classes3.dex */
public interface ITelephony extends IInterface {
    /* JADX INFO: Access modifiers changed from: private */
    void answerRingingCall() throws RemoteException;

    synchronized void answerRingingCallForSubscriber(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void call(String str, String str2) throws RemoteException;

    synchronized boolean canChangeDtmfToneLength() throws RemoteException;

    synchronized void carrierActionReportDefaultNetworkStatus(int i, boolean z) throws RemoteException;

    void carrierActionResetAll(int i) throws RemoteException;

    synchronized void carrierActionSetMeteredApnsEnabled(int i, boolean z) throws RemoteException;

    synchronized void carrierActionSetRadioEnabled(int i, boolean z) throws RemoteException;

    synchronized int checkCarrierPrivilegesForPackage(String str) throws RemoteException;

    synchronized int checkCarrierPrivilegesForPackageAnyPhone(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void dial(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean disableDataConnectivity() throws RemoteException;

    synchronized void disableIms(int i) throws RemoteException;

    private protected void disableLocationUpdates() throws RemoteException;

    synchronized void disableLocationUpdatesForSubscriber(int i) throws RemoteException;

    synchronized void disableVisualVoicemailSmsFilter(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean enableDataConnectivity() throws RemoteException;

    synchronized void enableIms(int i) throws RemoteException;

    private protected void enableLocationUpdates() throws RemoteException;

    synchronized void enableLocationUpdatesForSubscriber(int i) throws RemoteException;

    synchronized void enableVideoCalling(boolean z) throws RemoteException;

    synchronized void enableVisualVoicemailSmsFilter(String str, int i, VisualVoicemailSmsFilterSettings visualVoicemailSmsFilterSettings) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean endCall() throws RemoteException;

    private protected boolean endCallForSubscriber(int i) throws RemoteException;

    synchronized void factoryReset(int i) throws RemoteException;

    private protected int getActivePhoneType() throws RemoteException;

    synchronized int getActivePhoneTypeForSlot(int i) throws RemoteException;

    synchronized VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int i) throws RemoteException;

    synchronized String getAidForAppType(int i, int i2) throws RemoteException;

    synchronized List<CellInfo> getAllCellInfo(String str) throws RemoteException;

    synchronized List<CarrierIdentifier> getAllowedCarriers(int i) throws RemoteException;

    synchronized int getCalculatedPreferredNetworkType(String str) throws RemoteException;

    private protected int getCallState() throws RemoteException;

    synchronized int getCallStateForSlot(int i) throws RemoteException;

    synchronized int getCarrierIdListVersion(int i) throws RemoteException;

    synchronized List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int i) throws RemoteException;

    synchronized int getCarrierPrivilegeStatus(int i) throws RemoteException;

    synchronized int getCarrierPrivilegeStatusForUid(int i, int i2) throws RemoteException;

    synchronized int getCdmaEriIconIndex(String str) throws RemoteException;

    synchronized int getCdmaEriIconIndexForSubscriber(int i, String str) throws RemoteException;

    synchronized int getCdmaEriIconMode(String str) throws RemoteException;

    synchronized int getCdmaEriIconModeForSubscriber(int i, String str) throws RemoteException;

    synchronized String getCdmaEriText(String str) throws RemoteException;

    synchronized String getCdmaEriTextForSubscriber(int i, String str) throws RemoteException;

    synchronized String getCdmaMdn(int i) throws RemoteException;

    synchronized String getCdmaMin(int i) throws RemoteException;

    synchronized String getCdmaPrlVersion(int i) throws RemoteException;

    synchronized Bundle getCellLocation(String str) throws RemoteException;

    synchronized CellNetworkScanResult getCellNetworkScanResults(int i) throws RemoteException;

    synchronized List<ClientRequestStats> getClientRequestStats(String str, int i) throws RemoteException;

    synchronized int getDataActivationState(int i, String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getDataActivity() throws RemoteException;

    private protected boolean getDataEnabled(int i) throws RemoteException;

    synchronized int getDataNetworkType(String str) throws RemoteException;

    synchronized int getDataNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getDataState() throws RemoteException;

    synchronized int getDefaultSim() throws RemoteException;

    synchronized String getDeviceId(String str) throws RemoteException;

    synchronized String getDeviceSoftwareVersionForSlot(int i, String str) throws RemoteException;

    synchronized boolean getEmergencyCallbackMode(int i) throws RemoteException;

    synchronized String getEsn(int i) throws RemoteException;

    synchronized String[] getForbiddenPlmns(int i, int i2, String str) throws RemoteException;

    synchronized String getImeiForSlot(int i, String str) throws RemoteException;

    synchronized IImsConfig getImsConfig(int i, int i2) throws RemoteException;

    synchronized int getImsRegTechnologyForMmTel(int i) throws RemoteException;

    synchronized IImsRegistration getImsRegistration(int i, int i2) throws RemoteException;

    synchronized String getImsService(int i, boolean z) throws RemoteException;

    synchronized String getLine1AlphaTagForDisplay(int i, String str) throws RemoteException;

    synchronized String getLine1NumberForDisplay(int i, String str) throws RemoteException;

    synchronized String getLocaleFromDefaultSim() throws RemoteException;

    synchronized int getLteOnCdmaMode(String str) throws RemoteException;

    synchronized int getLteOnCdmaModeForSubscriber(int i, String str) throws RemoteException;

    synchronized String getMeidForSlot(int i, String str) throws RemoteException;

    synchronized String[] getMergedSubscriberIds(String str) throws RemoteException;

    synchronized IImsMmTelFeature getMmTelFeatureAndListen(int i, IImsServiceFeatureCallback iImsServiceFeatureCallback) throws RemoteException;

    synchronized List<NeighboringCellInfo> getNeighboringCellInfo(String str) throws RemoteException;

    synchronized String getNetworkCountryIsoForPhone(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getNetworkType() throws RemoteException;

    synchronized int getNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    synchronized List<String> getPackagesWithCarrierPrivileges() throws RemoteException;

    synchronized String[] getPcscfAddress(String str, String str2) throws RemoteException;

    synchronized int getPreferredNetworkType(int i) throws RemoteException;

    synchronized int getRadioAccessFamily(int i, String str) throws RemoteException;

    synchronized IImsRcsFeature getRcsFeatureAndListen(int i, IImsServiceFeatureCallback iImsServiceFeatureCallback) throws RemoteException;

    synchronized ServiceState getServiceStateForSubscriber(int i, String str) throws RemoteException;

    synchronized SignalStrength getSignalStrength(int i) throws RemoteException;

    synchronized int getSubIdForPhoneAccount(PhoneAccount phoneAccount) throws RemoteException;

    synchronized int getSubscriptionCarrierId(int i) throws RemoteException;

    synchronized String getSubscriptionCarrierName(int i) throws RemoteException;

    synchronized List<TelephonyHistogram> getTelephonyHistograms() throws RemoteException;

    synchronized int getTetherApnRequired() throws RemoteException;

    synchronized UiccSlotInfo[] getUiccSlotsInfo() throws RemoteException;

    synchronized String getVisualVoicemailPackageName(String str, int i) throws RemoteException;

    synchronized Bundle getVisualVoicemailSettings(String str, int i) throws RemoteException;

    synchronized VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(String str, int i) throws RemoteException;

    synchronized int getVoiceActivationState(int i, String str) throws RemoteException;

    private protected int getVoiceMessageCount() throws RemoteException;

    synchronized int getVoiceMessageCountForSubscriber(int i) throws RemoteException;

    synchronized int getVoiceNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    synchronized Uri getVoicemailRingtoneUri(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    synchronized NetworkStats getVtDataUsage(int i, boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean handlePinMmi(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean handlePinMmiForSubscriber(int i, String str) throws RemoteException;

    synchronized void handleUssdRequest(int i, String str, ResultReceiver resultReceiver) throws RemoteException;

    private protected boolean hasIccCard() throws RemoteException;

    synchronized boolean hasIccCardUsingSlotIndex(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean iccCloseLogicalChannel(int i, int i2) throws RemoteException;

    synchronized byte[] iccExchangeSimIO(int i, int i2, int i3, int i4, int i5, int i6, String str) throws RemoteException;

    synchronized IccOpenLogicalChannelResponse iccOpenLogicalChannel(int i, String str, String str2, int i2) throws RemoteException;

    synchronized String iccTransmitApduBasicChannel(int i, String str, int i2, int i3, int i4, int i5, int i6, String str2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String iccTransmitApduLogicalChannel(int i, int i2, int i3, int i4, int i5, int i6, int i7, String str) throws RemoteException;

    synchronized int invokeOemRilRequestRaw(byte[] bArr, byte[] bArr2) throws RemoteException;

    synchronized boolean isConcurrentVoiceAndDataAllowed(int i) throws RemoteException;

    synchronized boolean isDataConnectivityPossible(int i) throws RemoteException;

    synchronized boolean isDataEnabled(int i) throws RemoteException;

    synchronized boolean isHearingAidCompatibilitySupported() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isIdle(String str) throws RemoteException;

    private protected boolean isIdleForSubscriber(int i, String str) throws RemoteException;

    synchronized boolean isImsRegistered(int i) throws RemoteException;

    synchronized boolean isOffhook(String str) throws RemoteException;

    synchronized boolean isOffhookForSubscriber(int i, String str) throws RemoteException;

    synchronized boolean isRadioOn(String str) throws RemoteException;

    private protected boolean isRadioOnForSubscriber(int i, String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isRinging(String str) throws RemoteException;

    synchronized boolean isRingingForSubscriber(int i, String str) throws RemoteException;

    synchronized boolean isTtyModeSupported() throws RemoteException;

    synchronized boolean isUserDataEnabled(int i) throws RemoteException;

    synchronized boolean isVideoCallingEnabled(String str) throws RemoteException;

    synchronized boolean isVideoTelephonyAvailable(int i) throws RemoteException;

    synchronized boolean isVoicemailVibrationEnabled(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    synchronized boolean isVolteAvailable(int i) throws RemoteException;

    synchronized boolean isWifiCallingAvailable(int i) throws RemoteException;

    synchronized boolean isWorldPhone() throws RemoteException;

    synchronized boolean needMobileRadioShutdown() throws RemoteException;

    synchronized boolean needsOtaServiceProvisioning() throws RemoteException;

    synchronized String nvReadItem(int i) throws RemoteException;

    synchronized boolean nvResetConfig(int i) throws RemoteException;

    synchronized boolean nvWriteCdmaPrl(byte[] bArr) throws RemoteException;

    synchronized boolean nvWriteItem(int i, String str) throws RemoteException;

    synchronized void refreshUiccProfile(int i) throws RemoteException;

    synchronized void requestModemActivityInfo(ResultReceiver resultReceiver) throws RemoteException;

    synchronized int requestNetworkScan(int i, NetworkScanRequest networkScanRequest, Messenger messenger, IBinder iBinder) throws RemoteException;

    synchronized void sendDialerSpecialCode(String str, String str2) throws RemoteException;

    synchronized String sendEnvelopeWithStatus(int i, String str) throws RemoteException;

    synchronized void sendVisualVoicemailSmsForSubscriber(String str, int i, String str2, int i2, String str3, PendingIntent pendingIntent) throws RemoteException;

    synchronized int setAllowedCarriers(int i, List<CarrierIdentifier> list) throws RemoteException;

    synchronized void setCarrierTestOverride(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7) throws RemoteException;

    synchronized void setCellInfoListRate(int i) throws RemoteException;

    synchronized void setDataActivationState(int i, int i2) throws RemoteException;

    synchronized void setImsRegistrationState(boolean z) throws RemoteException;

    synchronized boolean setImsService(int i, boolean z, String str) throws RemoteException;

    synchronized boolean setLine1NumberForDisplayForSubscriber(int i, String str, String str2) throws RemoteException;

    synchronized void setNetworkSelectionModeAutomatic(int i) throws RemoteException;

    synchronized boolean setNetworkSelectionModeManual(int i, String str, boolean z) throws RemoteException;

    synchronized boolean setOperatorBrandOverride(int i, String str) throws RemoteException;

    synchronized void setPolicyDataEnabled(boolean z, int i) throws RemoteException;

    synchronized boolean setPreferredNetworkType(int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean setRadio(boolean z) throws RemoteException;

    synchronized void setRadioCapability(RadioAccessFamily[] radioAccessFamilyArr) throws RemoteException;

    synchronized boolean setRadioForSubscriber(int i, boolean z) throws RemoteException;

    synchronized void setRadioIndicationUpdateMode(int i, int i2, int i3) throws RemoteException;

    synchronized boolean setRadioPower(boolean z) throws RemoteException;

    synchronized boolean setRoamingOverride(int i, List<String> list, List<String> list2, List<String> list3, List<String> list4) throws RemoteException;

    synchronized void setSimPowerStateForSlot(int i, int i2) throws RemoteException;

    synchronized void setUserDataEnabled(int i, boolean z) throws RemoteException;

    synchronized void setVoiceActivationState(int i, int i2) throws RemoteException;

    synchronized boolean setVoiceMailNumber(int i, String str, String str2) throws RemoteException;

    synchronized void setVoicemailRingtoneUri(String str, PhoneAccountHandle phoneAccountHandle, Uri uri) throws RemoteException;

    synchronized void setVoicemailVibrationEnabled(String str, PhoneAccountHandle phoneAccountHandle, boolean z) throws RemoteException;

    synchronized void shutdownMobileRadios() throws RemoteException;

    private protected void silenceRinger() throws RemoteException;

    synchronized void stopNetworkScan(int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean supplyPin(String str) throws RemoteException;

    synchronized boolean supplyPinForSubscriber(int i, String str) throws RemoteException;

    synchronized int[] supplyPinReportResult(String str) throws RemoteException;

    synchronized int[] supplyPinReportResultForSubscriber(int i, String str) throws RemoteException;

    synchronized boolean supplyPuk(String str, String str2) throws RemoteException;

    synchronized boolean supplyPukForSubscriber(int i, String str, String str2) throws RemoteException;

    synchronized int[] supplyPukReportResult(String str, String str2) throws RemoteException;

    synchronized int[] supplyPukReportResultForSubscriber(int i, String str, String str2) throws RemoteException;

    synchronized boolean switchSlots(int[] iArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void toggleRadioOnOff() throws RemoteException;

    synchronized void toggleRadioOnOffForSubscriber(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void updateServiceLocation() throws RemoteException;

    synchronized void updateServiceLocationForSubscriber(int i) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ITelephony {
        public protected static final String DESCRIPTOR = "com.android.internal.telephony.ITelephony";
        public private protected static final int TRANSACTION_answerRingingCall = 5;
        static final int TRANSACTION_answerRingingCallForSubscriber = 6;
        public private protected static final int TRANSACTION_call = 2;
        static final int TRANSACTION_canChangeDtmfToneLength = 138;
        static final int TRANSACTION_carrierActionReportDefaultNetworkStatus = 171;
        static final int TRANSACTION_carrierActionResetAll = 172;
        static final int TRANSACTION_carrierActionSetMeteredApnsEnabled = 169;
        static final int TRANSACTION_carrierActionSetRadioEnabled = 170;
        static final int TRANSACTION_checkCarrierPrivilegesForPackage = 122;
        static final int TRANSACTION_checkCarrierPrivilegesForPackageAnyPhone = 123;
        public private protected static final int TRANSACTION_dial = 1;
        static final int TRANSACTION_disableDataConnectivity = 39;
        static final int TRANSACTION_disableIms = 99;
        static final int TRANSACTION_disableLocationUpdates = 36;
        static final int TRANSACTION_disableLocationUpdatesForSubscriber = 37;
        static final int TRANSACTION_disableVisualVoicemailSmsFilter = 68;
        static final int TRANSACTION_enableDataConnectivity = 38;
        static final int TRANSACTION_enableIms = 98;
        static final int TRANSACTION_enableLocationUpdates = 34;
        static final int TRANSACTION_enableLocationUpdatesForSubscriber = 35;
        static final int TRANSACTION_enableVideoCalling = 136;
        static final int TRANSACTION_enableVisualVoicemailSmsFilter = 67;
        public private protected static final int TRANSACTION_endCall = 3;
        static final int TRANSACTION_endCallForSubscriber = 4;
        static final int TRANSACTION_factoryReset = 152;
        static final int TRANSACTION_getActivePhoneType = 48;
        static final int TRANSACTION_getActivePhoneTypeForSlot = 49;
        static final int TRANSACTION_getActiveVisualVoicemailSmsFilterSettings = 70;
        static final int TRANSACTION_getAidForAppType = 161;
        static final int TRANSACTION_getAllCellInfo = 82;
        static final int TRANSACTION_getAllowedCarriers = 166;
        static final int TRANSACTION_getCalculatedPreferredNetworkType = 95;
        static final int TRANSACTION_getCallState = 44;
        static final int TRANSACTION_getCallStateForSlot = 45;
        static final int TRANSACTION_getCarrierIdListVersion = 184;
        static final int TRANSACTION_getCarrierPackageNamesForIntentAndPhone = 124;
        static final int TRANSACTION_getCarrierPrivilegeStatus = 120;
        static final int TRANSACTION_getCarrierPrivilegeStatusForUid = 121;
        static final int TRANSACTION_getCdmaEriIconIndex = 50;
        static final int TRANSACTION_getCdmaEriIconIndexForSubscriber = 51;
        static final int TRANSACTION_getCdmaEriIconMode = 52;
        static final int TRANSACTION_getCdmaEriIconModeForSubscriber = 53;
        static final int TRANSACTION_getCdmaEriText = 54;
        static final int TRANSACTION_getCdmaEriTextForSubscriber = 55;
        static final int TRANSACTION_getCdmaMdn = 118;
        static final int TRANSACTION_getCdmaMin = 119;
        static final int TRANSACTION_getCdmaPrlVersion = 163;
        static final int TRANSACTION_getCellLocation = 41;
        static final int TRANSACTION_getCellNetworkScanResults = 107;
        static final int TRANSACTION_getClientRequestStats = 175;
        static final int TRANSACTION_getDataActivationState = 61;
        static final int TRANSACTION_getDataActivity = 46;
        static final int TRANSACTION_getDataEnabled = 113;
        static final int TRANSACTION_getDataNetworkType = 75;
        static final int TRANSACTION_getDataNetworkTypeForSubscriber = 76;
        static final int TRANSACTION_getDataState = 47;
        static final int TRANSACTION_getDefaultSim = 84;
        public private protected static final int TRANSACTION_getDeviceId = 147;
        static final int TRANSACTION_getDeviceSoftwareVersionForSlot = 150;
        static final int TRANSACTION_getEmergencyCallbackMode = 178;
        static final int TRANSACTION_getEsn = 162;
        static final int TRANSACTION_getForbiddenPlmns = 177;
        static final int TRANSACTION_getImeiForSlot = 148;
        static final int TRANSACTION_getImsConfig = 103;
        static final int TRANSACTION_getImsRegTechnologyForMmTel = 146;
        static final int TRANSACTION_getImsRegistration = 102;
        static final int TRANSACTION_getImsService = 105;
        static final int TRANSACTION_getLine1AlphaTagForDisplay = 127;
        static final int TRANSACTION_getLine1NumberForDisplay = 126;
        static final int TRANSACTION_getLocaleFromDefaultSim = 153;
        static final int TRANSACTION_getLteOnCdmaMode = 80;
        static final int TRANSACTION_getLteOnCdmaModeForSubscriber = 81;
        static final int TRANSACTION_getMeidForSlot = 149;
        static final int TRANSACTION_getMergedSubscriberIds = 128;
        static final int TRANSACTION_getMmTelFeatureAndListen = 100;
        static final int TRANSACTION_getNeighboringCellInfo = 43;
        static final int TRANSACTION_getNetworkCountryIsoForPhone = 42;
        static final int TRANSACTION_getNetworkType = 73;
        static final int TRANSACTION_getNetworkTypeForSubscriber = 74;
        static final int TRANSACTION_getPackagesWithCarrierPrivileges = 160;
        static final int TRANSACTION_getPcscfAddress = 116;
        static final int TRANSACTION_getPreferredNetworkType = 96;
        static final int TRANSACTION_getRadioAccessFamily = 135;
        static final int TRANSACTION_getRcsFeatureAndListen = 101;
        static final int TRANSACTION_getServiceStateForSubscriber = 155;
        static final int TRANSACTION_getSignalStrength = 179;
        static final int TRANSACTION_getSubIdForPhoneAccount = 151;
        static final int TRANSACTION_getSubscriptionCarrierId = 167;
        static final int TRANSACTION_getSubscriptionCarrierName = 168;
        static final int TRANSACTION_getTelephonyHistograms = 164;
        static final int TRANSACTION_getTetherApnRequired = 97;
        static final int TRANSACTION_getUiccSlotsInfo = 180;
        static final int TRANSACTION_getVisualVoicemailPackageName = 66;
        static final int TRANSACTION_getVisualVoicemailSettings = 65;
        static final int TRANSACTION_getVisualVoicemailSmsFilterSettings = 69;
        static final int TRANSACTION_getVoiceActivationState = 60;
        static final int TRANSACTION_getVoiceMessageCount = 62;
        static final int TRANSACTION_getVoiceMessageCountForSubscriber = 63;
        static final int TRANSACTION_getVoiceNetworkTypeForSubscriber = 77;
        static final int TRANSACTION_getVoicemailRingtoneUri = 156;
        static final int TRANSACTION_getVtDataUsage = 173;
        static final int TRANSACTION_handlePinMmi = 24;
        static final int TRANSACTION_handlePinMmiForSubscriber = 26;
        static final int TRANSACTION_handleUssdRequest = 25;
        static final int TRANSACTION_hasIccCard = 78;
        static final int TRANSACTION_hasIccCardUsingSlotIndex = 79;
        static final int TRANSACTION_iccCloseLogicalChannel = 86;
        static final int TRANSACTION_iccExchangeSimIO = 89;
        static final int TRANSACTION_iccOpenLogicalChannel = 85;
        static final int TRANSACTION_iccTransmitApduBasicChannel = 88;
        static final int TRANSACTION_iccTransmitApduLogicalChannel = 87;
        static final int TRANSACTION_invokeOemRilRequestRaw = 131;
        static final int TRANSACTION_isConcurrentVoiceAndDataAllowed = 64;
        static final int TRANSACTION_isDataConnectivityPossible = 40;
        static final int TRANSACTION_isDataEnabled = 115;
        static final int TRANSACTION_isHearingAidCompatibilitySupported = 141;
        static final int TRANSACTION_isIdle = 12;
        static final int TRANSACTION_isIdleForSubscriber = 13;
        static final int TRANSACTION_isImsRegistered = 142;
        static final int TRANSACTION_isOffhook = 8;
        static final int TRANSACTION_isOffhookForSubscriber = 9;
        static final int TRANSACTION_isRadioOn = 14;
        static final int TRANSACTION_isRadioOnForSubscriber = 15;
        static final int TRANSACTION_isRinging = 11;
        static final int TRANSACTION_isRingingForSubscriber = 10;
        static final int TRANSACTION_isTtyModeSupported = 140;
        static final int TRANSACTION_isUserDataEnabled = 114;
        static final int TRANSACTION_isVideoCallingEnabled = 137;
        static final int TRANSACTION_isVideoTelephonyAvailable = 145;
        static final int TRANSACTION_isVoicemailVibrationEnabled = 158;
        static final int TRANSACTION_isVolteAvailable = 144;
        static final int TRANSACTION_isWifiCallingAvailable = 143;
        static final int TRANSACTION_isWorldPhone = 139;
        static final int TRANSACTION_needMobileRadioShutdown = 132;
        static final int TRANSACTION_needsOtaServiceProvisioning = 56;
        static final int TRANSACTION_nvReadItem = 91;
        static final int TRANSACTION_nvResetConfig = 94;
        static final int TRANSACTION_nvWriteCdmaPrl = 93;
        static final int TRANSACTION_nvWriteItem = 92;
        static final int TRANSACTION_refreshUiccProfile = 185;
        static final int TRANSACTION_requestModemActivityInfo = 154;
        static final int TRANSACTION_requestNetworkScan = 108;
        static final int TRANSACTION_sendDialerSpecialCode = 72;
        static final int TRANSACTION_sendEnvelopeWithStatus = 90;
        static final int TRANSACTION_sendVisualVoicemailSmsForSubscriber = 71;
        static final int TRANSACTION_setAllowedCarriers = 165;
        static final int TRANSACTION_setCarrierTestOverride = 183;
        static final int TRANSACTION_setCellInfoListRate = 83;
        static final int TRANSACTION_setDataActivationState = 59;
        static final int TRANSACTION_setImsRegistrationState = 117;
        static final int TRANSACTION_setImsService = 104;
        static final int TRANSACTION_setLine1NumberForDisplayForSubscriber = 125;
        static final int TRANSACTION_setNetworkSelectionModeAutomatic = 106;
        static final int TRANSACTION_setNetworkSelectionModeManual = 110;
        static final int TRANSACTION_setOperatorBrandOverride = 129;
        static final int TRANSACTION_setPolicyDataEnabled = 174;
        static final int TRANSACTION_setPreferredNetworkType = 111;
        static final int TRANSACTION_setRadio = 29;
        static final int TRANSACTION_setRadioCapability = 134;
        static final int TRANSACTION_setRadioForSubscriber = 30;
        static final int TRANSACTION_setRadioIndicationUpdateMode = 182;
        static final int TRANSACTION_setRadioPower = 31;
        static final int TRANSACTION_setRoamingOverride = 130;
        static final int TRANSACTION_setSimPowerStateForSlot = 176;
        static final int TRANSACTION_setUserDataEnabled = 112;
        static final int TRANSACTION_setVoiceActivationState = 58;
        static final int TRANSACTION_setVoiceMailNumber = 57;
        static final int TRANSACTION_setVoicemailRingtoneUri = 157;
        static final int TRANSACTION_setVoicemailVibrationEnabled = 159;
        static final int TRANSACTION_shutdownMobileRadios = 133;
        static final int TRANSACTION_silenceRinger = 7;
        static final int TRANSACTION_stopNetworkScan = 109;
        static final int TRANSACTION_supplyPin = 16;
        static final int TRANSACTION_supplyPinForSubscriber = 17;
        static final int TRANSACTION_supplyPinReportResult = 20;
        static final int TRANSACTION_supplyPinReportResultForSubscriber = 21;
        static final int TRANSACTION_supplyPuk = 18;
        static final int TRANSACTION_supplyPukForSubscriber = 19;
        static final int TRANSACTION_supplyPukReportResult = 22;
        static final int TRANSACTION_supplyPukReportResultForSubscriber = 23;
        static final int TRANSACTION_switchSlots = 181;
        static final int TRANSACTION_toggleRadioOnOff = 27;
        static final int TRANSACTION_toggleRadioOnOffForSubscriber = 28;
        static final int TRANSACTION_updateServiceLocation = 32;
        static final int TRANSACTION_updateServiceLocationForSubscriber = 33;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
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

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg1;
            NetworkScanRequest _arg12;
            byte[] _arg13;
            PhoneAccountHandle _arg14;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    dial(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    call(_arg02, data.readString());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    boolean endCall = endCall();
                    reply.writeNoException();
                    reply.writeInt(endCall ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    boolean endCallForSubscriber = endCallForSubscriber(_arg03);
                    reply.writeNoException();
                    reply.writeInt(endCallForSubscriber ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    answerRingingCall();
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    answerRingingCallForSubscriber(_arg04);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    silenceRinger();
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    boolean isOffhook = isOffhook(_arg05);
                    reply.writeNoException();
                    reply.writeInt(isOffhook ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    boolean isOffhookForSubscriber = isOffhookForSubscriber(_arg06, data.readString());
                    reply.writeNoException();
                    reply.writeInt(isOffhookForSubscriber ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    boolean isRingingForSubscriber = isRingingForSubscriber(_arg07, data.readString());
                    reply.writeNoException();
                    reply.writeInt(isRingingForSubscriber ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    boolean isRinging = isRinging(_arg08);
                    reply.writeNoException();
                    reply.writeInt(isRinging ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    boolean isIdle = isIdle(_arg09);
                    reply.writeNoException();
                    reply.writeInt(isIdle ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    boolean isIdleForSubscriber = isIdleForSubscriber(_arg010, data.readString());
                    reply.writeNoException();
                    reply.writeInt(isIdleForSubscriber ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    boolean isRadioOn = isRadioOn(_arg011);
                    reply.writeNoException();
                    reply.writeInt(isRadioOn ? 1 : 0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    boolean isRadioOnForSubscriber = isRadioOnForSubscriber(_arg012, data.readString());
                    reply.writeNoException();
                    reply.writeInt(isRadioOnForSubscriber ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    boolean supplyPin = supplyPin(_arg013);
                    reply.writeNoException();
                    reply.writeInt(supplyPin ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    boolean supplyPinForSubscriber = supplyPinForSubscriber(_arg014, data.readString());
                    reply.writeNoException();
                    reply.writeInt(supplyPinForSubscriber ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    boolean supplyPuk = supplyPuk(_arg015, data.readString());
                    reply.writeNoException();
                    reply.writeInt(supplyPuk ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    String _arg15 = data.readString();
                    String _arg2 = data.readString();
                    boolean supplyPukForSubscriber = supplyPukForSubscriber(_arg016, _arg15, _arg2);
                    reply.writeNoException();
                    reply.writeInt(supplyPukForSubscriber ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    int[] _result = supplyPinReportResult(_arg017);
                    reply.writeNoException();
                    reply.writeIntArray(_result);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg018 = data.readInt();
                    int[] _result2 = supplyPinReportResultForSubscriber(_arg018, data.readString());
                    reply.writeNoException();
                    reply.writeIntArray(_result2);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    int[] _result3 = supplyPukReportResult(_arg019, data.readString());
                    reply.writeNoException();
                    reply.writeIntArray(_result3);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    String _arg16 = data.readString();
                    String _arg22 = data.readString();
                    int[] _result4 = supplyPukReportResultForSubscriber(_arg020, _arg16, _arg22);
                    reply.writeNoException();
                    reply.writeIntArray(_result4);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    boolean handlePinMmi = handlePinMmi(_arg021);
                    reply.writeNoException();
                    reply.writeInt(handlePinMmi ? 1 : 0);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    String _arg17 = data.readString();
                    ResultReceiver _arg23 = data.readInt() != 0 ? ResultReceiver.CREATOR.createFromParcel(data) : null;
                    handleUssdRequest(_arg022, _arg17, _arg23);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    boolean handlePinMmiForSubscriber = handlePinMmiForSubscriber(_arg023, data.readString());
                    reply.writeNoException();
                    reply.writeInt(handlePinMmiForSubscriber ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    toggleRadioOnOff();
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    toggleRadioOnOffForSubscriber(_arg024);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg025 = _arg1;
                    boolean radio = setRadio(_arg025);
                    reply.writeNoException();
                    reply.writeInt(radio ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg026 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    boolean radioForSubscriber = setRadioForSubscriber(_arg026, _arg1);
                    reply.writeNoException();
                    reply.writeInt(radioForSubscriber ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg027 = _arg1;
                    boolean radioPower = setRadioPower(_arg027);
                    reply.writeNoException();
                    reply.writeInt(radioPower ? 1 : 0);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    updateServiceLocation();
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg028 = data.readInt();
                    updateServiceLocationForSubscriber(_arg028);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    enableLocationUpdates();
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg029 = data.readInt();
                    enableLocationUpdatesForSubscriber(_arg029);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    disableLocationUpdates();
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg030 = data.readInt();
                    disableLocationUpdatesForSubscriber(_arg030);
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    boolean enableDataConnectivity = enableDataConnectivity();
                    reply.writeNoException();
                    reply.writeInt(enableDataConnectivity ? 1 : 0);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    boolean disableDataConnectivity = disableDataConnectivity();
                    reply.writeNoException();
                    reply.writeInt(disableDataConnectivity ? 1 : 0);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg031 = data.readInt();
                    boolean isDataConnectivityPossible = isDataConnectivityPossible(_arg031);
                    reply.writeNoException();
                    reply.writeInt(isDataConnectivityPossible ? 1 : 0);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    Bundle _result5 = getCellLocation(_arg032);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg033 = data.readInt();
                    String _result6 = getNetworkCountryIsoForPhone(_arg033);
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg034 = data.readString();
                    List<NeighboringCellInfo> _result7 = getNeighboringCellInfo(_arg034);
                    reply.writeNoException();
                    reply.writeTypedList(_result7);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    int _result8 = getCallState();
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg035 = data.readInt();
                    int _result9 = getCallStateForSlot(_arg035);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    int _result10 = getDataActivity();
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    int _result11 = getDataState();
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    int _result12 = getActivePhoneType();
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    int _result13 = getActivePhoneTypeForSlot(_arg036);
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg037 = data.readString();
                    int _result14 = getCdmaEriIconIndex(_arg037);
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg038 = data.readInt();
                    int _result15 = getCdmaEriIconIndexForSubscriber(_arg038, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg039 = data.readString();
                    int _result16 = getCdmaEriIconMode(_arg039);
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg040 = data.readInt();
                    int _result17 = getCdmaEriIconModeForSubscriber(_arg040, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg041 = data.readString();
                    String _result18 = getCdmaEriText(_arg041);
                    reply.writeNoException();
                    reply.writeString(_result18);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg042 = data.readInt();
                    String _result19 = getCdmaEriTextForSubscriber(_arg042, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result19);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    boolean needsOtaServiceProvisioning = needsOtaServiceProvisioning();
                    reply.writeNoException();
                    reply.writeInt(needsOtaServiceProvisioning ? 1 : 0);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg043 = data.readInt();
                    String _arg18 = data.readString();
                    String _arg24 = data.readString();
                    boolean voiceMailNumber = setVoiceMailNumber(_arg043, _arg18, _arg24);
                    reply.writeNoException();
                    reply.writeInt(voiceMailNumber ? 1 : 0);
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg044 = data.readInt();
                    setVoiceActivationState(_arg044, data.readInt());
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg045 = data.readInt();
                    setDataActivationState(_arg045, data.readInt());
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg046 = data.readInt();
                    int _result20 = getVoiceActivationState(_arg046, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg047 = data.readInt();
                    int _result21 = getDataActivationState(_arg047, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    int _result22 = getVoiceMessageCount();
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg048 = data.readInt();
                    int _result23 = getVoiceMessageCountForSubscriber(_arg048);
                    reply.writeNoException();
                    reply.writeInt(_result23);
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg049 = data.readInt();
                    boolean isConcurrentVoiceAndDataAllowed = isConcurrentVoiceAndDataAllowed(_arg049);
                    reply.writeNoException();
                    reply.writeInt(isConcurrentVoiceAndDataAllowed ? 1 : 0);
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg050 = data.readString();
                    Bundle _result24 = getVisualVoicemailSettings(_arg050, data.readInt());
                    reply.writeNoException();
                    if (_result24 != null) {
                        reply.writeInt(1);
                        _result24.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg051 = data.readString();
                    String _result25 = getVisualVoicemailPackageName(_arg051, data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result25);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg052 = data.readString();
                    int _arg19 = data.readInt();
                    VisualVoicemailSmsFilterSettings _arg25 = data.readInt() != 0 ? VisualVoicemailSmsFilterSettings.CREATOR.createFromParcel(data) : null;
                    enableVisualVoicemailSmsFilter(_arg052, _arg19, _arg25);
                    reply.writeNoException();
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg053 = data.readString();
                    disableVisualVoicemailSmsFilter(_arg053, data.readInt());
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg054 = data.readString();
                    VisualVoicemailSmsFilterSettings _result26 = getVisualVoicemailSmsFilterSettings(_arg054, data.readInt());
                    reply.writeNoException();
                    if (_result26 != null) {
                        reply.writeInt(1);
                        _result26.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg055 = data.readInt();
                    VisualVoicemailSmsFilterSettings _result27 = getActiveVisualVoicemailSmsFilterSettings(_arg055);
                    reply.writeNoException();
                    if (_result27 != null) {
                        reply.writeInt(1);
                        _result27.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg056 = data.readString();
                    int _arg110 = data.readInt();
                    String _arg26 = data.readString();
                    int _arg3 = data.readInt();
                    String _arg4 = data.readString();
                    PendingIntent _arg5 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    sendVisualVoicemailSmsForSubscriber(_arg056, _arg110, _arg26, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg057 = data.readString();
                    sendDialerSpecialCode(_arg057, data.readString());
                    reply.writeNoException();
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    int _result28 = getNetworkType();
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg058 = data.readInt();
                    int _result29 = getNetworkTypeForSubscriber(_arg058, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result29);
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg059 = data.readString();
                    int _result30 = getDataNetworkType(_arg059);
                    reply.writeNoException();
                    reply.writeInt(_result30);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg060 = data.readInt();
                    int _result31 = getDataNetworkTypeForSubscriber(_arg060, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result31);
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg061 = data.readInt();
                    int _result32 = getVoiceNetworkTypeForSubscriber(_arg061, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result32);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasIccCard = hasIccCard();
                    reply.writeNoException();
                    reply.writeInt(hasIccCard ? 1 : 0);
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg062 = data.readInt();
                    boolean hasIccCardUsingSlotIndex = hasIccCardUsingSlotIndex(_arg062);
                    reply.writeNoException();
                    reply.writeInt(hasIccCardUsingSlotIndex ? 1 : 0);
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg063 = data.readString();
                    int _result33 = getLteOnCdmaMode(_arg063);
                    reply.writeNoException();
                    reply.writeInt(_result33);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg064 = data.readInt();
                    int _result34 = getLteOnCdmaModeForSubscriber(_arg064, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result34);
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg065 = data.readString();
                    List<CellInfo> _result35 = getAllCellInfo(_arg065);
                    reply.writeNoException();
                    reply.writeTypedList(_result35);
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg066 = data.readInt();
                    setCellInfoListRate(_arg066);
                    reply.writeNoException();
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    int _result36 = getDefaultSim();
                    reply.writeNoException();
                    reply.writeInt(_result36);
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg067 = data.readInt();
                    String _arg111 = data.readString();
                    String _arg27 = data.readString();
                    int _arg32 = data.readInt();
                    IccOpenLogicalChannelResponse _result37 = iccOpenLogicalChannel(_arg067, _arg111, _arg27, _arg32);
                    reply.writeNoException();
                    if (_result37 != null) {
                        reply.writeInt(1);
                        _result37.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg068 = data.readInt();
                    boolean iccCloseLogicalChannel = iccCloseLogicalChannel(_arg068, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(iccCloseLogicalChannel ? 1 : 0);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg069 = data.readInt();
                    int _arg112 = data.readInt();
                    int _arg28 = data.readInt();
                    int _arg33 = data.readInt();
                    int _arg42 = data.readInt();
                    int _arg52 = data.readInt();
                    int _arg6 = data.readInt();
                    String _arg7 = data.readString();
                    String _result38 = iccTransmitApduLogicalChannel(_arg069, _arg112, _arg28, _arg33, _arg42, _arg52, _arg6, _arg7);
                    reply.writeNoException();
                    reply.writeString(_result38);
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg070 = data.readInt();
                    String _arg113 = data.readString();
                    int _arg29 = data.readInt();
                    int _arg34 = data.readInt();
                    int _arg43 = data.readInt();
                    int _arg53 = data.readInt();
                    int _arg62 = data.readInt();
                    String _arg72 = data.readString();
                    String _result39 = iccTransmitApduBasicChannel(_arg070, _arg113, _arg29, _arg34, _arg43, _arg53, _arg62, _arg72);
                    reply.writeNoException();
                    reply.writeString(_result39);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg071 = data.readInt();
                    int _arg114 = data.readInt();
                    int _arg210 = data.readInt();
                    int _arg35 = data.readInt();
                    int _arg44 = data.readInt();
                    int _arg54 = data.readInt();
                    String _arg63 = data.readString();
                    byte[] _result40 = iccExchangeSimIO(_arg071, _arg114, _arg210, _arg35, _arg44, _arg54, _arg63);
                    reply.writeNoException();
                    reply.writeByteArray(_result40);
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg072 = data.readInt();
                    String _result41 = sendEnvelopeWithStatus(_arg072, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result41);
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg073 = data.readInt();
                    String _result42 = nvReadItem(_arg073);
                    reply.writeNoException();
                    reply.writeString(_result42);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg074 = data.readInt();
                    boolean nvWriteItem = nvWriteItem(_arg074, data.readString());
                    reply.writeNoException();
                    reply.writeInt(nvWriteItem ? 1 : 0);
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg075 = data.createByteArray();
                    boolean nvWriteCdmaPrl = nvWriteCdmaPrl(_arg075);
                    reply.writeNoException();
                    reply.writeInt(nvWriteCdmaPrl ? 1 : 0);
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg076 = data.readInt();
                    boolean nvResetConfig = nvResetConfig(_arg076);
                    reply.writeNoException();
                    reply.writeInt(nvResetConfig ? 1 : 0);
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg077 = data.readString();
                    int _result43 = getCalculatedPreferredNetworkType(_arg077);
                    reply.writeNoException();
                    reply.writeInt(_result43);
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg078 = data.readInt();
                    int _result44 = getPreferredNetworkType(_arg078);
                    reply.writeNoException();
                    reply.writeInt(_result44);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    int _result45 = getTetherApnRequired();
                    reply.writeNoException();
                    reply.writeInt(_result45);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg079 = data.readInt();
                    enableIms(_arg079);
                    reply.writeNoException();
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg080 = data.readInt();
                    disableIms(_arg080);
                    reply.writeNoException();
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg081 = data.readInt();
                    IImsMmTelFeature _result46 = getMmTelFeatureAndListen(_arg081, IImsServiceFeatureCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeStrongBinder(_result46 != null ? _result46.asBinder() : null);
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg082 = data.readInt();
                    IImsRcsFeature _result47 = getRcsFeatureAndListen(_arg082, IImsServiceFeatureCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeStrongBinder(_result47 != null ? _result47.asBinder() : null);
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg083 = data.readInt();
                    IImsRegistration _result48 = getImsRegistration(_arg083, data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(_result48 != null ? _result48.asBinder() : null);
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg084 = data.readInt();
                    IImsConfig _result49 = getImsConfig(_arg084, data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(_result49 != null ? _result49.asBinder() : null);
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg085 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    String _arg211 = data.readString();
                    boolean imsService = setImsService(_arg085, _arg1, _arg211);
                    reply.writeNoException();
                    reply.writeInt(imsService ? 1 : 0);
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg086 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    String _result50 = getImsService(_arg086, _arg1);
                    reply.writeNoException();
                    reply.writeString(_result50);
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg087 = data.readInt();
                    setNetworkSelectionModeAutomatic(_arg087);
                    reply.writeNoException();
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg088 = data.readInt();
                    CellNetworkScanResult _result51 = getCellNetworkScanResults(_arg088);
                    reply.writeNoException();
                    if (_result51 != null) {
                        reply.writeInt(1);
                        _result51.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg089 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = NetworkScanRequest.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    Messenger _arg212 = data.readInt() != 0 ? Messenger.CREATOR.createFromParcel(data) : null;
                    IBinder _arg36 = data.readStrongBinder();
                    int _result52 = requestNetworkScan(_arg089, _arg12, _arg212, _arg36);
                    reply.writeNoException();
                    reply.writeInt(_result52);
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg090 = data.readInt();
                    stopNetworkScan(_arg090, data.readInt());
                    reply.writeNoException();
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg091 = data.readInt();
                    String _arg115 = data.readString();
                    _arg1 = data.readInt() != 0;
                    boolean networkSelectionModeManual = setNetworkSelectionModeManual(_arg091, _arg115, _arg1);
                    reply.writeNoException();
                    reply.writeInt(networkSelectionModeManual ? 1 : 0);
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg092 = data.readInt();
                    boolean preferredNetworkType = setPreferredNetworkType(_arg092, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(preferredNetworkType ? 1 : 0);
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg093 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setUserDataEnabled(_arg093, _arg1);
                    reply.writeNoException();
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg094 = data.readInt();
                    boolean dataEnabled = getDataEnabled(_arg094);
                    reply.writeNoException();
                    reply.writeInt(dataEnabled ? 1 : 0);
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg095 = data.readInt();
                    boolean isUserDataEnabled = isUserDataEnabled(_arg095);
                    reply.writeNoException();
                    reply.writeInt(isUserDataEnabled ? 1 : 0);
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg096 = data.readInt();
                    boolean isDataEnabled = isDataEnabled(_arg096);
                    reply.writeNoException();
                    reply.writeInt(isDataEnabled ? 1 : 0);
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg097 = data.readString();
                    String[] _result53 = getPcscfAddress(_arg097, data.readString());
                    reply.writeNoException();
                    reply.writeStringArray(_result53);
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg098 = _arg1;
                    setImsRegistrationState(_arg098);
                    reply.writeNoException();
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg099 = data.readInt();
                    String _result54 = getCdmaMdn(_arg099);
                    reply.writeNoException();
                    reply.writeString(_result54);
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0100 = data.readInt();
                    String _result55 = getCdmaMin(_arg0100);
                    reply.writeNoException();
                    reply.writeString(_result55);
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0101 = data.readInt();
                    int _result56 = getCarrierPrivilegeStatus(_arg0101);
                    reply.writeNoException();
                    reply.writeInt(_result56);
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0102 = data.readInt();
                    int _result57 = getCarrierPrivilegeStatusForUid(_arg0102, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result57);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0103 = data.readString();
                    int _result58 = checkCarrierPrivilegesForPackage(_arg0103);
                    reply.writeNoException();
                    reply.writeInt(_result58);
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0104 = data.readString();
                    int _result59 = checkCarrierPrivilegesForPackageAnyPhone(_arg0104);
                    reply.writeNoException();
                    reply.writeInt(_result59);
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _arg0105 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    List<String> _result60 = getCarrierPackageNamesForIntentAndPhone(_arg0105, data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result60);
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0106 = data.readInt();
                    String _arg116 = data.readString();
                    String _arg213 = data.readString();
                    boolean line1NumberForDisplayForSubscriber = setLine1NumberForDisplayForSubscriber(_arg0106, _arg116, _arg213);
                    reply.writeNoException();
                    reply.writeInt(line1NumberForDisplayForSubscriber ? 1 : 0);
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0107 = data.readInt();
                    String _result61 = getLine1NumberForDisplay(_arg0107, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result61);
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0108 = data.readInt();
                    String _result62 = getLine1AlphaTagForDisplay(_arg0108, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result62);
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0109 = data.readString();
                    String[] _result63 = getMergedSubscriberIds(_arg0109);
                    reply.writeNoException();
                    reply.writeStringArray(_result63);
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0110 = data.readInt();
                    boolean operatorBrandOverride = setOperatorBrandOverride(_arg0110, data.readString());
                    reply.writeNoException();
                    reply.writeInt(operatorBrandOverride ? 1 : 0);
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0111 = data.readInt();
                    List<String> _arg117 = data.createStringArrayList();
                    List<String> _arg214 = data.createStringArrayList();
                    List<String> _arg37 = data.createStringArrayList();
                    List<String> _arg45 = data.createStringArrayList();
                    boolean roamingOverride = setRoamingOverride(_arg0111, _arg117, _arg214, _arg37, _arg45);
                    reply.writeNoException();
                    reply.writeInt(roamingOverride ? 1 : 0);
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg0112 = data.createByteArray();
                    int _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg13 = null;
                    } else {
                        _arg13 = new byte[_arg1_length];
                    }
                    int _result64 = invokeOemRilRequestRaw(_arg0112, _arg13);
                    reply.writeNoException();
                    reply.writeInt(_result64);
                    reply.writeByteArray(_arg13);
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    boolean needMobileRadioShutdown = needMobileRadioShutdown();
                    reply.writeNoException();
                    reply.writeInt(needMobileRadioShutdown ? 1 : 0);
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    shutdownMobileRadios();
                    reply.writeNoException();
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    RadioAccessFamily[] _arg0113 = (RadioAccessFamily[]) data.createTypedArray(RadioAccessFamily.CREATOR);
                    setRadioCapability(_arg0113);
                    reply.writeNoException();
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0114 = data.readInt();
                    int _result65 = getRadioAccessFamily(_arg0114, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result65);
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0115 = _arg1;
                    enableVideoCalling(_arg0115);
                    reply.writeNoException();
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0116 = data.readString();
                    boolean isVideoCallingEnabled = isVideoCallingEnabled(_arg0116);
                    reply.writeNoException();
                    reply.writeInt(isVideoCallingEnabled ? 1 : 0);
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    boolean canChangeDtmfToneLength = canChangeDtmfToneLength();
                    reply.writeNoException();
                    reply.writeInt(canChangeDtmfToneLength ? 1 : 0);
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isWorldPhone = isWorldPhone();
                    reply.writeNoException();
                    reply.writeInt(isWorldPhone ? 1 : 0);
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isTtyModeSupported = isTtyModeSupported();
                    reply.writeNoException();
                    reply.writeInt(isTtyModeSupported ? 1 : 0);
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHearingAidCompatibilitySupported = isHearingAidCompatibilitySupported();
                    reply.writeNoException();
                    reply.writeInt(isHearingAidCompatibilitySupported ? 1 : 0);
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0117 = data.readInt();
                    boolean isImsRegistered = isImsRegistered(_arg0117);
                    reply.writeNoException();
                    reply.writeInt(isImsRegistered ? 1 : 0);
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0118 = data.readInt();
                    boolean isWifiCallingAvailable = isWifiCallingAvailable(_arg0118);
                    reply.writeNoException();
                    reply.writeInt(isWifiCallingAvailable ? 1 : 0);
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0119 = data.readInt();
                    boolean isVolteAvailable = isVolteAvailable(_arg0119);
                    reply.writeNoException();
                    reply.writeInt(isVolteAvailable ? 1 : 0);
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0120 = data.readInt();
                    boolean isVideoTelephonyAvailable = isVideoTelephonyAvailable(_arg0120);
                    reply.writeNoException();
                    reply.writeInt(isVideoTelephonyAvailable ? 1 : 0);
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0121 = data.readInt();
                    int _result66 = getImsRegTechnologyForMmTel(_arg0121);
                    reply.writeNoException();
                    reply.writeInt(_result66);
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0122 = data.readString();
                    String _result67 = getDeviceId(_arg0122);
                    reply.writeNoException();
                    reply.writeString(_result67);
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0123 = data.readInt();
                    String _result68 = getImeiForSlot(_arg0123, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result68);
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0124 = data.readInt();
                    String _result69 = getMeidForSlot(_arg0124, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result69);
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0125 = data.readInt();
                    String _result70 = getDeviceSoftwareVersionForSlot(_arg0125, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result70);
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccount _arg0126 = data.readInt() != 0 ? PhoneAccount.CREATOR.createFromParcel(data) : null;
                    int _result71 = getSubIdForPhoneAccount(_arg0126);
                    reply.writeNoException();
                    reply.writeInt(_result71);
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0127 = data.readInt();
                    factoryReset(_arg0127);
                    reply.writeNoException();
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    String _result72 = getLocaleFromDefaultSim();
                    reply.writeNoException();
                    reply.writeString(_result72);
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    ResultReceiver _arg0128 = data.readInt() != 0 ? ResultReceiver.CREATOR.createFromParcel(data) : null;
                    requestModemActivityInfo(_arg0128);
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0129 = data.readInt();
                    ServiceState _result73 = getServiceStateForSubscriber(_arg0129, data.readString());
                    reply.writeNoException();
                    if (_result73 != null) {
                        reply.writeInt(1);
                        _result73.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg0130 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    Uri _result74 = getVoicemailRingtoneUri(_arg0130);
                    reply.writeNoException();
                    if (_result74 != null) {
                        reply.writeInt(1);
                        _result74.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0131 = data.readString();
                    if (data.readInt() != 0) {
                        _arg14 = PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    Uri _arg215 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    setVoicemailRingtoneUri(_arg0131, _arg14, _arg215);
                    reply.writeNoException();
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg0132 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    boolean isVoicemailVibrationEnabled = isVoicemailVibrationEnabled(_arg0132);
                    reply.writeNoException();
                    reply.writeInt(isVoicemailVibrationEnabled ? 1 : 0);
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0133 = data.readString();
                    PhoneAccountHandle _arg118 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    _arg1 = data.readInt() != 0;
                    setVoicemailVibrationEnabled(_arg0133, _arg118, _arg1);
                    reply.writeNoException();
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result75 = getPackagesWithCarrierPrivileges();
                    reply.writeNoException();
                    reply.writeStringList(_result75);
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0134 = data.readInt();
                    String _result76 = getAidForAppType(_arg0134, data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result76);
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0135 = data.readInt();
                    String _result77 = getEsn(_arg0135);
                    reply.writeNoException();
                    reply.writeString(_result77);
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0136 = data.readInt();
                    String _result78 = getCdmaPrlVersion(_arg0136);
                    reply.writeNoException();
                    reply.writeString(_result78);
                    return true;
                case 164:
                    data.enforceInterface(DESCRIPTOR);
                    List<TelephonyHistogram> _result79 = getTelephonyHistograms();
                    reply.writeNoException();
                    reply.writeTypedList(_result79);
                    return true;
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0137 = data.readInt();
                    int _result80 = setAllowedCarriers(_arg0137, data.createTypedArrayList(CarrierIdentifier.CREATOR));
                    reply.writeNoException();
                    reply.writeInt(_result80);
                    return true;
                case 166:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0138 = data.readInt();
                    List<CarrierIdentifier> _result81 = getAllowedCarriers(_arg0138);
                    reply.writeNoException();
                    reply.writeTypedList(_result81);
                    return true;
                case 167:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0139 = data.readInt();
                    int _result82 = getSubscriptionCarrierId(_arg0139);
                    reply.writeNoException();
                    reply.writeInt(_result82);
                    return true;
                case 168:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0140 = data.readInt();
                    String _result83 = getSubscriptionCarrierName(_arg0140);
                    reply.writeNoException();
                    reply.writeString(_result83);
                    return true;
                case 169:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0141 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    carrierActionSetMeteredApnsEnabled(_arg0141, _arg1);
                    reply.writeNoException();
                    return true;
                case 170:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0142 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    carrierActionSetRadioEnabled(_arg0142, _arg1);
                    reply.writeNoException();
                    return true;
                case 171:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0143 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    carrierActionReportDefaultNetworkStatus(_arg0143, _arg1);
                    reply.writeNoException();
                    return true;
                case 172:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0144 = data.readInt();
                    carrierActionResetAll(_arg0144);
                    reply.writeNoException();
                    return true;
                case 173:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0145 = data.readInt();
                    boolean _arg119 = data.readInt() != 0;
                    NetworkStats _result84 = getVtDataUsage(_arg0145, _arg119);
                    reply.writeNoException();
                    if (_result84 != null) {
                        reply.writeInt(1);
                        _result84.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 174:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg0146 = _arg1;
                    setPolicyDataEnabled(_arg0146, data.readInt());
                    reply.writeNoException();
                    return true;
                case 175:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0147 = data.readString();
                    List<ClientRequestStats> _result85 = getClientRequestStats(_arg0147, data.readInt());
                    reply.writeNoException();
                    reply.writeTypedList(_result85);
                    return true;
                case 176:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0148 = data.readInt();
                    setSimPowerStateForSlot(_arg0148, data.readInt());
                    reply.writeNoException();
                    return true;
                case 177:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0149 = data.readInt();
                    int _arg120 = data.readInt();
                    String _arg216 = data.readString();
                    String[] _result86 = getForbiddenPlmns(_arg0149, _arg120, _arg216);
                    reply.writeNoException();
                    reply.writeStringArray(_result86);
                    return true;
                case 178:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0150 = data.readInt();
                    boolean emergencyCallbackMode = getEmergencyCallbackMode(_arg0150);
                    reply.writeNoException();
                    reply.writeInt(emergencyCallbackMode ? 1 : 0);
                    return true;
                case 179:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0151 = data.readInt();
                    SignalStrength _result87 = getSignalStrength(_arg0151);
                    reply.writeNoException();
                    if (_result87 != null) {
                        reply.writeInt(1);
                        _result87.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 180:
                    data.enforceInterface(DESCRIPTOR);
                    UiccSlotInfo[] _result88 = getUiccSlotsInfo();
                    reply.writeNoException();
                    reply.writeTypedArray(_result88, 1);
                    return true;
                case 181:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg0152 = data.createIntArray();
                    boolean switchSlots = switchSlots(_arg0152);
                    reply.writeNoException();
                    reply.writeInt(switchSlots ? 1 : 0);
                    return true;
                case 182:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0153 = data.readInt();
                    int _arg121 = data.readInt();
                    int _arg217 = data.readInt();
                    setRadioIndicationUpdateMode(_arg0153, _arg121, _arg217);
                    reply.writeNoException();
                    return true;
                case 183:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0154 = data.readInt();
                    String _arg122 = data.readString();
                    String _arg218 = data.readString();
                    String _arg38 = data.readString();
                    String _arg46 = data.readString();
                    String _arg55 = data.readString();
                    String _arg64 = data.readString();
                    String _arg73 = data.readString();
                    setCarrierTestOverride(_arg0154, _arg122, _arg218, _arg38, _arg46, _arg55, _arg64, _arg73);
                    reply.writeNoException();
                    return true;
                case 184:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0155 = data.readInt();
                    int _result89 = getCarrierIdListVersion(_arg0155);
                    reply.writeNoException();
                    reply.writeInt(_result89);
                    return true;
                case 185:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0156 = data.readInt();
                    refreshUiccProfile(_arg0156);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements ITelephony {
            public protected IBinder mRemote;

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

            public synchronized void dial(String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void call(String callingPackage, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(number);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected boolean endCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected boolean endCallForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void answerRingingCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void answerRingingCallForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void silenceRinger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isOffhook(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isOffhookForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isRingingForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isRinging(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isIdle(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isIdleForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected boolean isRadioOn(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean isRadioOnForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean supplyPin(String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pin);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean supplyPinForSubscriber(int subId, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pin);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean supplyPuk(String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean supplyPukForSubscriber(int subId, String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int[] supplyPinReportResult(String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pin);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int[] supplyPinReportResultForSubscriber(int subId, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pin);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int[] supplyPukReportResult(String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int[] supplyPukReportResultForSubscriber(int subId, String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean handlePinMmi(String dialString) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dialString);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void handleUssdRequest(int subId, String ussdRequest, ResultReceiver wrappedCallback) throws RemoteException {
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
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean handlePinMmiForSubscriber(int subId, String dialString) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(dialString);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void toggleRadioOnOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void toggleRadioOnOffForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean setRadio(boolean turnOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(turnOn ? 1 : 0);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean setRadioForSubscriber(int subId, boolean turnOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(turnOn ? 1 : 0);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean setRadioPower(boolean turnOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(turnOn ? 1 : 0);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void updateServiceLocation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void updateServiceLocationForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void enableLocationUpdates() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void enableLocationUpdatesForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void disableLocationUpdates() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void disableLocationUpdatesForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean enableDataConnectivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean disableDataConnectivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isDataConnectivityPossible(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized Bundle getCellLocation(String callingPkg) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(41, _data, _reply, 0);
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
            public synchronized String getNetworkCountryIsoForPhone(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized List<NeighboringCellInfo> getNeighboringCellInfo(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    List<NeighboringCellInfo> _result = _reply.createTypedArrayList(NeighboringCellInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getCallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getCallStateForSlot(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getDataActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getDataState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getActivePhoneType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getActivePhoneTypeForSlot(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getCdmaEriIconIndex(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getCdmaEriIconIndexForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getCdmaEriIconMode(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getCdmaEriIconModeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getCdmaEriText(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getCdmaEriTextForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean needsOtaServiceProvisioning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean setVoiceMailNumber(int subId, String alphaTag, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(alphaTag);
                    _data.writeString(number);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void setVoiceActivationState(int subId, int activationState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(activationState);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void setDataActivationState(int subId, int activationState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(activationState);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getVoiceActivationState(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getDataActivationState(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getVoiceMessageCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getVoiceMessageCountForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isConcurrentVoiceAndDataAllowed(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized Bundle getVisualVoicemailSettings(String callingPackage, int subId) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    this.mRemote.transact(65, _data, _reply, 0);
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
            public synchronized String getVisualVoicemailPackageName(String callingPackage, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void enableVisualVoicemailSmsFilter(String callingPackage, int subId, VisualVoicemailSmsFilterSettings settings) throws RemoteException {
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
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void disableVisualVoicemailSmsFilter(String callingPackage, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    this.mRemote.transact(68, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(String callingPackage, int subId) throws RemoteException {
                VisualVoicemailSmsFilterSettings _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    this.mRemote.transact(69, _data, _reply, 0);
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
            public synchronized VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int subId) throws RemoteException {
                VisualVoicemailSmsFilterSettings _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(70, _data, _reply, 0);
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
            public synchronized void sendVisualVoicemailSmsForSubscriber(String callingPackage, int subId, String number, int port, String text, PendingIntent sentIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    _data.writeString(number);
                    _data.writeInt(port);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void sendDialerSpecialCode(String callingPackageName, String inputCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackageName);
                    _data.writeString(inputCode);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getNetworkType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getDataNetworkType(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getDataNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getVoiceNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean hasIccCard() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean hasIccCardUsingSlotIndex(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getLteOnCdmaMode(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getLteOnCdmaModeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized List<CellInfo> getAllCellInfo(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                    List<CellInfo> _result = _reply.createTypedArrayList(CellInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void setCellInfoListRate(int rateInMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rateInMillis);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getDefaultSim() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized IccOpenLogicalChannelResponse iccOpenLogicalChannel(int subId, String callingPackage, String AID, int p2) throws RemoteException {
                IccOpenLogicalChannelResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    _data.writeString(AID);
                    _data.writeInt(p2);
                    this.mRemote.transact(85, _data, _reply, 0);
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

            public synchronized boolean iccCloseLogicalChannel(int subId, int channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(channel);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String iccTransmitApduLogicalChannel(int subId, int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(channel);
                    _data.writeInt(cla);
                    _data.writeInt(instruction);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(data);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String iccTransmitApduBasicChannel(int subId, String callingPackage, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    _data.writeInt(cla);
                    _data.writeInt(instruction);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(data);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized byte[] iccExchangeSimIO(int subId, int fileID, int command, int p1, int p2, int p3, String filePath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(fileID);
                    _data.writeInt(command);
                    _data.writeInt(p1);
                    _data.writeInt(p2);
                    _data.writeInt(p3);
                    _data.writeString(filePath);
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String sendEnvelopeWithStatus(int subId, String content) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(content);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String nvReadItem(int itemID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(itemID);
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean nvWriteItem(int itemID, String itemValue) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(itemID);
                    _data.writeString(itemValue);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean nvWriteCdmaPrl(byte[] preferredRoamingList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(preferredRoamingList);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean nvResetConfig(int resetType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(resetType);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getCalculatedPreferredNetworkType(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getPreferredNetworkType(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getTetherApnRequired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void enableIms(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void disableIms(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized IImsMmTelFeature getMmTelFeatureAndListen(int slotId, IImsServiceFeatureCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                    IImsMmTelFeature _result = IImsMmTelFeature.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized IImsRcsFeature getRcsFeatureAndListen(int slotId, IImsServiceFeatureCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                    IImsRcsFeature _result = IImsRcsFeature.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized IImsRegistration getImsRegistration(int slotId, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(feature);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                    IImsRegistration _result = IImsRegistration.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized IImsConfig getImsConfig(int slotId, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(feature);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                    IImsConfig _result = IImsConfig.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean setImsService(int slotId, boolean isCarrierImsService, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(isCarrierImsService ? 1 : 0);
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

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getImsService(int slotId, boolean isCarrierImsService) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(isCarrierImsService ? 1 : 0);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void setNetworkSelectionModeAutomatic(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized CellNetworkScanResult getCellNetworkScanResults(int subId) throws RemoteException {
                CellNetworkScanResult _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(107, _data, _reply, 0);
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
            public synchronized int requestNetworkScan(int subId, NetworkScanRequest request, Messenger messenger, IBinder binder) throws RemoteException {
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
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void stopNetworkScan(int subId, int scanId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(scanId);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean setNetworkSelectionModeManual(int subId, String operatorNumeric, boolean persistSelection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(operatorNumeric);
                    _data.writeInt(persistSelection ? 1 : 0);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean setPreferredNetworkType(int subId, int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(networkType);
                    this.mRemote.transact(111, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void setUserDataEnabled(int subId, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(112, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean getDataEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isUserDataEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(114, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isDataEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String[] getPcscfAddress(String apnType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apnType);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void setImsRegistrationState(boolean registered) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(registered ? 1 : 0);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getCdmaMdn(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getCdmaMin(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getCarrierPrivilegeStatus(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(120, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getCarrierPrivilegeStatusForUid(int subId, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(uid);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int checkCarrierPrivilegesForPackage(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(122, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int checkCarrierPrivilegesForPackageAnyPhone(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int phoneId) throws RemoteException {
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
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean setLine1NumberForDisplayForSubscriber(int subId, String alphaTag, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(alphaTag);
                    _data.writeString(number);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getLine1NumberForDisplay(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getLine1AlphaTagForDisplay(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String[] getMergedSubscriberIds(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean setOperatorBrandOverride(int subId, String brand) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(brand);
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean setRoamingOverride(int subId, List<String> gsmRoamingList, List<String> gsmNonRoamingList, List<String> cdmaRoamingList, List<String> cdmaNonRoamingList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStringList(gsmRoamingList);
                    _data.writeStringList(gsmNonRoamingList);
                    _data.writeStringList(cdmaRoamingList);
                    _data.writeStringList(cdmaNonRoamingList);
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) throws RemoteException {
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
                    this.mRemote.transact(131, _data, _reply, 0);
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
            public synchronized boolean needMobileRadioShutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void shutdownMobileRadios() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void setRadioCapability(RadioAccessFamily[] rafs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(rafs, 0);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getRadioAccessFamily(int phoneId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(135, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void enableVideoCalling(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(136, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isVideoCallingEnabled(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(137, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean canChangeDtmfToneLength() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(138, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isWorldPhone() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(139, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isTtyModeSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isHearingAidCompatibilitySupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(141, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isImsRegistered(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(142, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isWifiCallingAvailable(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(143, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isVolteAvailable(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(144, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isVideoTelephonyAvailable(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(145, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getImsRegTechnologyForMmTel(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(146, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected String getDeviceId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(147, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getImeiForSlot(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(148, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getMeidForSlot(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(149, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getDeviceSoftwareVersionForSlot(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(150, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getSubIdForPhoneAccount(PhoneAccount phoneAccount) throws RemoteException {
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
                    this.mRemote.transact(151, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void factoryReset(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(152, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getLocaleFromDefaultSim() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(153, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void requestModemActivityInfo(ResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(154, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized ServiceState getServiceStateForSubscriber(int subId, String callingPackage) throws RemoteException {
                ServiceState _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(155, _data, _reply, 0);
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
            public synchronized Uri getVoicemailRingtoneUri(PhoneAccountHandle accountHandle) throws RemoteException {
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
                    this.mRemote.transact(156, _data, _reply, 0);
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
            public synchronized void setVoicemailRingtoneUri(String callingPackage, PhoneAccountHandle phoneAccountHandle, Uri uri) throws RemoteException {
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
                    this.mRemote.transact(157, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean isVoicemailVibrationEnabled(PhoneAccountHandle accountHandle) throws RemoteException {
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
                    this.mRemote.transact(158, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void setVoicemailVibrationEnabled(String callingPackage, PhoneAccountHandle phoneAccountHandle, boolean enabled) throws RemoteException {
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
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(159, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized List<String> getPackagesWithCarrierPrivileges() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(160, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getAidForAppType(int subId, int appType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(appType);
                    this.mRemote.transact(161, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getEsn(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(162, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getCdmaPrlVersion(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(163, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized List<TelephonyHistogram> getTelephonyHistograms() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(164, _data, _reply, 0);
                    _reply.readException();
                    List<TelephonyHistogram> _result = _reply.createTypedArrayList(TelephonyHistogram.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int setAllowedCarriers(int slotIndex, List<CarrierIdentifier> carriers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeTypedList(carriers);
                    this.mRemote.transact(165, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized List<CarrierIdentifier> getAllowedCarriers(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    this.mRemote.transact(166, _data, _reply, 0);
                    _reply.readException();
                    List<CarrierIdentifier> _result = _reply.createTypedArrayList(CarrierIdentifier.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getSubscriptionCarrierId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(167, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String getSubscriptionCarrierName(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(168, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void carrierActionSetMeteredApnsEnabled(int subId, boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(visible ? 1 : 0);
                    this.mRemote.transact(169, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void carrierActionSetRadioEnabled(int subId, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(170, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void carrierActionReportDefaultNetworkStatus(int subId, boolean report) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(report ? 1 : 0);
                    this.mRemote.transact(171, _data, _reply, 0);
                    _reply.readException();
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
                    this.mRemote.transact(172, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized NetworkStats getVtDataUsage(int subId, boolean perUidStats) throws RemoteException {
                NetworkStats _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(perUidStats ? 1 : 0);
                    this.mRemote.transact(173, _data, _reply, 0);
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
            public synchronized void setPolicyDataEnabled(boolean enabled, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeInt(subId);
                    this.mRemote.transact(174, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized List<ClientRequestStats> getClientRequestStats(String callingPackage, int subid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subid);
                    this.mRemote.transact(175, _data, _reply, 0);
                    _reply.readException();
                    List<ClientRequestStats> _result = _reply.createTypedArrayList(ClientRequestStats.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void setSimPowerStateForSlot(int slotIndex, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(state);
                    this.mRemote.transact(176, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized String[] getForbiddenPlmns(int subId, int appType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(appType);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(177, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean getEmergencyCallbackMode(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(178, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized SignalStrength getSignalStrength(int subId) throws RemoteException {
                SignalStrength _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(179, _data, _reply, 0);
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
            public synchronized UiccSlotInfo[] getUiccSlotsInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(180, _data, _reply, 0);
                    _reply.readException();
                    UiccSlotInfo[] _result = (UiccSlotInfo[]) _reply.createTypedArray(UiccSlotInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized boolean switchSlots(int[] physicalSlots) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(physicalSlots);
                    this.mRemote.transact(181, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void setRadioIndicationUpdateMode(int subId, int filters, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(filters);
                    _data.writeInt(mode);
                    this.mRemote.transact(182, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void setCarrierTestOverride(int subId, String mccmnc, String imsi, String iccid, String gid1, String gid2, String plmn, String spn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(mccmnc);
                    _data.writeString(imsi);
                    _data.writeString(iccid);
                    _data.writeString(gid1);
                    _data.writeString(gid2);
                    _data.writeString(plmn);
                    _data.writeString(spn);
                    this.mRemote.transact(183, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized int getCarrierIdListVersion(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(184, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephony
            public synchronized void refreshUiccProfile(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(185, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
