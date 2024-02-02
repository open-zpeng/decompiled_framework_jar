package android.net.wifi;

import android.content.pm.ParceledListSlice;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.wifi.ISoftApCallback;
import android.net.wifi.hotspot2.IProvisioningCallback;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.WorkSource;
import java.util.List;
/* loaded from: classes2.dex */
public interface IWifiManager extends IInterface {
    synchronized void acquireMulticastLock(IBinder iBinder, String str) throws RemoteException;

    synchronized boolean acquireWifiLock(IBinder iBinder, int i, String str, WorkSource workSource) throws RemoteException;

    synchronized int addOrUpdateNetwork(WifiConfiguration wifiConfiguration, String str) throws RemoteException;

    synchronized boolean addOrUpdatePasspointConfiguration(PasspointConfiguration passpointConfiguration, String str) throws RemoteException;

    synchronized void deauthenticateNetwork(long j, boolean z) throws RemoteException;

    synchronized void disableEphemeralNetwork(String str, String str2) throws RemoteException;

    synchronized boolean disableNetwork(int i, String str) throws RemoteException;

    synchronized void disconnect(String str) throws RemoteException;

    int dppAddBootstrapQrCode(String str) throws RemoteException;

    int dppBootstrapGenerate(WifiDppConfig wifiDppConfig) throws RemoteException;

    int dppBootstrapRemove(int i) throws RemoteException;

    int dppConfiguratorAdd(String str, String str2, int i) throws RemoteException;

    String dppConfiguratorGetKey(int i) throws RemoteException;

    int dppConfiguratorRemove(int i) throws RemoteException;

    String dppGetUri(int i) throws RemoteException;

    int dppListen(String str, int i, boolean z, boolean z2) throws RemoteException;

    int dppStartAuth(WifiDppConfig wifiDppConfig) throws RemoteException;

    void dppStopListen() throws RemoteException;

    synchronized boolean enableNetwork(int i, boolean z, String str) throws RemoteException;

    synchronized void enableTdls(String str, boolean z) throws RemoteException;

    synchronized void enableTdlsWithMacAddress(String str, boolean z) throws RemoteException;

    synchronized void enableVerboseLogging(int i) throws RemoteException;

    synchronized void enableWifiConnectivityManager(boolean z) throws RemoteException;

    synchronized void factoryReset(String str) throws RemoteException;

    synchronized List<WifiConfiguration> getAllMatchingWifiConfigs(ScanResult scanResult) throws RemoteException;

    String getCapabilities(String str) throws RemoteException;

    synchronized ParceledListSlice getConfiguredNetworks() throws RemoteException;

    synchronized WifiInfo getConnectionInfo(String str) throws RemoteException;

    synchronized String getCountryCode() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    Network getCurrentNetwork() throws RemoteException;

    synchronized String getCurrentNetworkWpsNfcConfigurationToken() throws RemoteException;

    synchronized DhcpInfo getDhcpInfo() throws RemoteException;

    synchronized List<OsuProvider> getMatchingOsuProviders(ScanResult scanResult) throws RemoteException;

    synchronized WifiConfiguration getMatchingWifiConfig(ScanResult scanResult) throws RemoteException;

    synchronized List<PasspointConfiguration> getPasspointConfigurations() throws RemoteException;

    synchronized ParceledListSlice getPrivilegedConfiguredNetworks() throws RemoteException;

    synchronized List<ScanResult> getScanResults(String str) throws RemoteException;

    synchronized int getSupportedFeatures() throws RemoteException;

    synchronized int getVerboseLoggingLevel() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    WifiConfiguration getWifiApConfiguration() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getWifiApEnabledState() throws RemoteException;

    synchronized int getWifiEnabledState() throws RemoteException;

    synchronized Messenger getWifiServiceMessenger(String str) throws RemoteException;

    synchronized void initializeMulticastFiltering() throws RemoteException;

    synchronized boolean isDualBandSupported() throws RemoteException;

    synchronized boolean isMulticastEnabled() throws RemoteException;

    synchronized boolean isScanAlwaysAvailable() throws RemoteException;

    synchronized int matchProviderWithCurrentNetwork(String str) throws RemoteException;

    synchronized boolean needs5GHzToAnyApBandConversion() throws RemoteException;

    void notifyUserOfApBandConversion(String str) throws RemoteException;

    synchronized void queryPasspointIcon(long j, String str) throws RemoteException;

    synchronized void reassociate(String str) throws RemoteException;

    synchronized void reconnect(String str) throws RemoteException;

    synchronized void registerSoftApCallback(IBinder iBinder, ISoftApCallback iSoftApCallback, int i) throws RemoteException;

    synchronized void releaseMulticastLock() throws RemoteException;

    synchronized boolean releaseWifiLock(IBinder iBinder) throws RemoteException;

    synchronized boolean removeNetwork(int i, String str) throws RemoteException;

    synchronized boolean removePasspointConfiguration(String str, String str2) throws RemoteException;

    synchronized WifiActivityEnergyInfo reportActivityInfo() throws RemoteException;

    synchronized void requestActivityInfo(ResultReceiver resultReceiver) throws RemoteException;

    synchronized void restoreBackupData(byte[] bArr) throws RemoteException;

    synchronized void restoreSupplicantBackupData(byte[] bArr, byte[] bArr2) throws RemoteException;

    synchronized byte[] retrieveBackupData() throws RemoteException;

    synchronized void setCountryCode(String str) throws RemoteException;

    synchronized boolean setWifiApConfiguration(WifiConfiguration wifiConfiguration, String str) throws RemoteException;

    synchronized boolean setWifiEnabled(String str, boolean z) throws RemoteException;

    synchronized int startLocalOnlyHotspot(Messenger messenger, IBinder iBinder, String str) throws RemoteException;

    synchronized boolean startScan(String str) throws RemoteException;

    synchronized boolean startSoftAp(WifiConfiguration wifiConfiguration) throws RemoteException;

    synchronized void startSubscriptionProvisioning(OsuProvider osuProvider, IProvisioningCallback iProvisioningCallback) throws RemoteException;

    synchronized void startWatchLocalOnlyHotspot(Messenger messenger, IBinder iBinder) throws RemoteException;

    synchronized void stopLocalOnlyHotspot() throws RemoteException;

    synchronized boolean stopSoftAp() throws RemoteException;

    synchronized void stopWatchLocalOnlyHotspot() throws RemoteException;

    synchronized void unregisterSoftApCallback(int i) throws RemoteException;

    synchronized void updateInterfaceIpState(String str, int i) throws RemoteException;

    synchronized void updateWifiLockWorkSource(IBinder iBinder, WorkSource workSource) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IWifiManager {
        private static final String DESCRIPTOR = "android.net.wifi.IWifiManager";
        static final int TRANSACTION_acquireMulticastLock = 38;
        static final int TRANSACTION_acquireWifiLock = 33;
        static final int TRANSACTION_addOrUpdateNetwork = 9;
        static final int TRANSACTION_addOrUpdatePasspointConfiguration = 10;
        static final int TRANSACTION_deauthenticateNetwork = 15;
        static final int TRANSACTION_disableEphemeralNetwork = 58;
        static final int TRANSACTION_disableNetwork = 18;
        static final int TRANSACTION_disconnect = 21;
        static final int TRANSACTION_dppAddBootstrapQrCode = 68;
        static final int TRANSACTION_dppBootstrapGenerate = 69;
        static final int TRANSACTION_dppBootstrapRemove = 71;
        static final int TRANSACTION_dppConfiguratorAdd = 74;
        static final int TRANSACTION_dppConfiguratorGetKey = 77;
        static final int TRANSACTION_dppConfiguratorRemove = 75;
        static final int TRANSACTION_dppGetUri = 70;
        static final int TRANSACTION_dppListen = 72;
        static final int TRANSACTION_dppStartAuth = 76;
        static final int TRANSACTION_dppStopListen = 73;
        static final int TRANSACTION_enableNetwork = 17;
        static final int TRANSACTION_enableTdls = 52;
        static final int TRANSACTION_enableTdlsWithMacAddress = 53;
        static final int TRANSACTION_enableVerboseLogging = 55;
        static final int TRANSACTION_enableWifiConnectivityManager = 57;
        static final int TRANSACTION_factoryReset = 59;
        static final int TRANSACTION_getAllMatchingWifiConfigs = 7;
        static final int TRANSACTION_getCapabilities = 67;
        static final int TRANSACTION_getConfiguredNetworks = 4;
        static final int TRANSACTION_getConnectionInfo = 24;
        static final int TRANSACTION_getCountryCode = 28;
        static final int TRANSACTION_getCurrentNetwork = 60;
        static final int TRANSACTION_getCurrentNetworkWpsNfcConfigurationToken = 54;
        static final int TRANSACTION_getDhcpInfo = 31;
        static final int TRANSACTION_getMatchingOsuProviders = 8;
        static final int TRANSACTION_getMatchingWifiConfig = 6;
        static final int TRANSACTION_getPasspointConfigurations = 12;
        static final int TRANSACTION_getPrivilegedConfiguredNetworks = 5;
        public private protected static final int TRANSACTION_getScanResults = 20;
        static final int TRANSACTION_getSupportedFeatures = 1;
        static final int TRANSACTION_getVerboseLoggingLevel = 56;
        static final int TRANSACTION_getWifiApConfiguration = 48;
        static final int TRANSACTION_getWifiApEnabledState = 47;
        static final int TRANSACTION_getWifiEnabledState = 26;
        static final int TRANSACTION_getWifiServiceMessenger = 51;
        static final int TRANSACTION_initializeMulticastFiltering = 36;
        static final int TRANSACTION_isDualBandSupported = 29;
        static final int TRANSACTION_isMulticastEnabled = 37;
        static final int TRANSACTION_isScanAlwaysAvailable = 32;
        static final int TRANSACTION_matchProviderWithCurrentNetwork = 14;
        static final int TRANSACTION_needs5GHzToAnyApBandConversion = 30;
        static final int TRANSACTION_notifyUserOfApBandConversion = 50;
        static final int TRANSACTION_queryPasspointIcon = 13;
        static final int TRANSACTION_reassociate = 23;
        static final int TRANSACTION_reconnect = 22;
        static final int TRANSACTION_registerSoftApCallback = 65;
        static final int TRANSACTION_releaseMulticastLock = 39;
        static final int TRANSACTION_releaseWifiLock = 35;
        static final int TRANSACTION_removeNetwork = 16;
        static final int TRANSACTION_removePasspointConfiguration = 11;
        static final int TRANSACTION_reportActivityInfo = 2;
        static final int TRANSACTION_requestActivityInfo = 3;
        static final int TRANSACTION_restoreBackupData = 62;
        static final int TRANSACTION_restoreSupplicantBackupData = 63;
        static final int TRANSACTION_retrieveBackupData = 61;
        static final int TRANSACTION_setCountryCode = 27;
        static final int TRANSACTION_setWifiApConfiguration = 49;
        static final int TRANSACTION_setWifiEnabled = 25;
        static final int TRANSACTION_startLocalOnlyHotspot = 43;
        static final int TRANSACTION_startScan = 19;
        static final int TRANSACTION_startSoftAp = 41;
        static final int TRANSACTION_startSubscriptionProvisioning = 64;
        static final int TRANSACTION_startWatchLocalOnlyHotspot = 45;
        static final int TRANSACTION_stopLocalOnlyHotspot = 44;
        static final int TRANSACTION_stopSoftAp = 42;
        static final int TRANSACTION_stopWatchLocalOnlyHotspot = 46;
        static final int TRANSACTION_unregisterSoftApCallback = 66;
        static final int TRANSACTION_updateInterfaceIpState = 40;
        static final int TRANSACTION_updateWifiLockWorkSource = 34;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IWifiManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IWifiManager)) {
                return (IWifiManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg3;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _result = getSupportedFeatures();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    WifiActivityEnergyInfo _result2 = reportActivityInfo();
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    ResultReceiver _arg0 = data.readInt() != 0 ? ResultReceiver.CREATOR.createFromParcel(data) : null;
                    requestActivityInfo(_arg0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ParceledListSlice _result3 = getConfiguredNetworks();
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    ParceledListSlice _result4 = getPrivilegedConfiguredNetworks();
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    ScanResult _arg02 = data.readInt() != 0 ? ScanResult.CREATOR.createFromParcel(data) : null;
                    WifiConfiguration _result5 = getMatchingWifiConfig(_arg02);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    ScanResult _arg03 = data.readInt() != 0 ? ScanResult.CREATOR.createFromParcel(data) : null;
                    List<WifiConfiguration> _result6 = getAllMatchingWifiConfigs(_arg03);
                    reply.writeNoException();
                    reply.writeTypedList(_result6);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    ScanResult _arg04 = data.readInt() != 0 ? ScanResult.CREATOR.createFromParcel(data) : null;
                    List<OsuProvider> _result7 = getMatchingOsuProviders(_arg04);
                    reply.writeNoException();
                    reply.writeTypedList(_result7);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    WifiConfiguration _arg05 = data.readInt() != 0 ? WifiConfiguration.CREATOR.createFromParcel(data) : null;
                    String _arg1 = data.readString();
                    int _result8 = addOrUpdateNetwork(_arg05, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    PasspointConfiguration _arg06 = data.readInt() != 0 ? PasspointConfiguration.CREATOR.createFromParcel(data) : null;
                    String _arg12 = data.readString();
                    boolean addOrUpdatePasspointConfiguration = addOrUpdatePasspointConfiguration(_arg06, _arg12);
                    reply.writeNoException();
                    reply.writeInt(addOrUpdatePasspointConfiguration ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    String _arg13 = data.readString();
                    boolean removePasspointConfiguration = removePasspointConfiguration(_arg07, _arg13);
                    reply.writeNoException();
                    reply.writeInt(removePasspointConfiguration ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    List<PasspointConfiguration> _result9 = getPasspointConfigurations();
                    reply.writeNoException();
                    reply.writeTypedList(_result9);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg08 = data.readLong();
                    String _arg14 = data.readString();
                    queryPasspointIcon(_arg08, _arg14);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    int _result10 = matchProviderWithCurrentNetwork(_arg09);
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg010 = data.readLong();
                    _arg3 = data.readInt() != 0;
                    boolean _arg15 = _arg3;
                    deauthenticateNetwork(_arg010, _arg15);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    String _arg16 = data.readString();
                    boolean removeNetwork = removeNetwork(_arg011, _arg16);
                    reply.writeNoException();
                    reply.writeInt(removeNetwork ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    _arg3 = data.readInt() != 0;
                    String _arg2 = data.readString();
                    boolean enableNetwork = enableNetwork(_arg012, _arg3, _arg2);
                    reply.writeNoException();
                    reply.writeInt(enableNetwork ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    String _arg17 = data.readString();
                    boolean disableNetwork = disableNetwork(_arg013, _arg17);
                    reply.writeNoException();
                    reply.writeInt(disableNetwork ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    boolean startScan = startScan(_arg014);
                    reply.writeNoException();
                    reply.writeInt(startScan ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    List<ScanResult> _result11 = getScanResults(_arg015);
                    reply.writeNoException();
                    reply.writeTypedList(_result11);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    disconnect(_arg016);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    reconnect(_arg017);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    reassociate(_arg018);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    WifiInfo _result12 = getConnectionInfo(_arg019);
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    _arg3 = data.readInt() != 0;
                    boolean wifiEnabled = setWifiEnabled(_arg020, _arg3);
                    reply.writeNoException();
                    reply.writeInt(wifiEnabled ? 1 : 0);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _result13 = getWifiEnabledState();
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    setCountryCode(_arg021);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String _result14 = getCountryCode();
                    reply.writeNoException();
                    reply.writeString(_result14);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDualBandSupported = isDualBandSupported();
                    reply.writeNoException();
                    reply.writeInt(isDualBandSupported ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    boolean needs5GHzToAnyApBandConversion = needs5GHzToAnyApBandConversion();
                    reply.writeNoException();
                    reply.writeInt(needs5GHzToAnyApBandConversion ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    DhcpInfo _result15 = getDhcpInfo();
                    reply.writeNoException();
                    if (_result15 != null) {
                        reply.writeInt(1);
                        _result15.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isScanAlwaysAvailable = isScanAlwaysAvailable();
                    reply.writeNoException();
                    reply.writeInt(isScanAlwaysAvailable ? 1 : 0);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg022 = data.readStrongBinder();
                    int _arg18 = data.readInt();
                    String _arg22 = data.readString();
                    boolean acquireWifiLock = acquireWifiLock(_arg022, _arg18, _arg22, data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeInt(acquireWifiLock ? 1 : 0);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg023 = data.readStrongBinder();
                    WorkSource _arg19 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                    updateWifiLockWorkSource(_arg023, _arg19);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg024 = data.readStrongBinder();
                    boolean releaseWifiLock = releaseWifiLock(_arg024);
                    reply.writeNoException();
                    reply.writeInt(releaseWifiLock ? 1 : 0);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    initializeMulticastFiltering();
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isMulticastEnabled = isMulticastEnabled();
                    reply.writeNoException();
                    reply.writeInt(isMulticastEnabled ? 1 : 0);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg025 = data.readStrongBinder();
                    String _arg110 = data.readString();
                    acquireMulticastLock(_arg025, _arg110);
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    releaseMulticastLock();
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg026 = data.readString();
                    int _arg111 = data.readInt();
                    updateInterfaceIpState(_arg026, _arg111);
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    WifiConfiguration _arg027 = data.readInt() != 0 ? WifiConfiguration.CREATOR.createFromParcel(data) : null;
                    boolean startSoftAp = startSoftAp(_arg027);
                    reply.writeNoException();
                    reply.writeInt(startSoftAp ? 1 : 0);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    boolean stopSoftAp = stopSoftAp();
                    reply.writeNoException();
                    reply.writeInt(stopSoftAp ? 1 : 0);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    Messenger _arg028 = data.readInt() != 0 ? Messenger.CREATOR.createFromParcel(data) : null;
                    IBinder _arg112 = data.readStrongBinder();
                    String _arg23 = data.readString();
                    int _result16 = startLocalOnlyHotspot(_arg028, _arg112, _arg23);
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    stopLocalOnlyHotspot();
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    Messenger _arg029 = data.readInt() != 0 ? Messenger.CREATOR.createFromParcel(data) : null;
                    IBinder _arg113 = data.readStrongBinder();
                    startWatchLocalOnlyHotspot(_arg029, _arg113);
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    stopWatchLocalOnlyHotspot();
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    int _result17 = getWifiApEnabledState();
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    WifiConfiguration _result18 = getWifiApConfiguration();
                    reply.writeNoException();
                    if (_result18 != null) {
                        reply.writeInt(1);
                        _result18.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    WifiConfiguration _arg030 = data.readInt() != 0 ? WifiConfiguration.CREATOR.createFromParcel(data) : null;
                    String _arg114 = data.readString();
                    boolean wifiApConfiguration = setWifiApConfiguration(_arg030, _arg114);
                    reply.writeNoException();
                    reply.writeInt(wifiApConfiguration ? 1 : 0);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg031 = data.readString();
                    notifyUserOfApBandConversion(_arg031);
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    Messenger _result19 = getWifiServiceMessenger(_arg032);
                    reply.writeNoException();
                    if (_result19 != null) {
                        reply.writeInt(1);
                        _result19.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg033 = data.readString();
                    _arg3 = data.readInt() != 0;
                    enableTdls(_arg033, _arg3);
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg034 = data.readString();
                    _arg3 = data.readInt() != 0;
                    enableTdlsWithMacAddress(_arg034, _arg3);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    String _result20 = getCurrentNetworkWpsNfcConfigurationToken();
                    reply.writeNoException();
                    reply.writeString(_result20);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg035 = data.readInt();
                    enableVerboseLogging(_arg035);
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    int _result21 = getVerboseLoggingLevel();
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    _arg3 = data.readInt() != 0;
                    boolean _arg036 = _arg3;
                    enableWifiConnectivityManager(_arg036);
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg037 = data.readString();
                    String _arg115 = data.readString();
                    disableEphemeralNetwork(_arg037, _arg115);
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg038 = data.readString();
                    factoryReset(_arg038);
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    Network _result22 = getCurrentNetwork();
                    reply.writeNoException();
                    if (_result22 != null) {
                        reply.writeInt(1);
                        _result22.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _result23 = retrieveBackupData();
                    reply.writeNoException();
                    reply.writeByteArray(_result23);
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg039 = data.createByteArray();
                    restoreBackupData(_arg039);
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg040 = data.createByteArray();
                    byte[] _arg116 = data.createByteArray();
                    restoreSupplicantBackupData(_arg040, _arg116);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    OsuProvider _arg041 = data.readInt() != 0 ? OsuProvider.CREATOR.createFromParcel(data) : null;
                    IProvisioningCallback _arg117 = IProvisioningCallback.Stub.asInterface(data.readStrongBinder());
                    startSubscriptionProvisioning(_arg041, _arg117);
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg042 = data.readStrongBinder();
                    ISoftApCallback _arg118 = ISoftApCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg24 = data.readInt();
                    registerSoftApCallback(_arg042, _arg118, _arg24);
                    reply.writeNoException();
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg043 = data.readInt();
                    unregisterSoftApCallback(_arg043);
                    reply.writeNoException();
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg044 = data.readString();
                    String _result24 = getCapabilities(_arg044);
                    reply.writeNoException();
                    reply.writeString(_result24);
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg045 = data.readString();
                    int _result25 = dppAddBootstrapQrCode(_arg045);
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    WifiDppConfig _arg046 = data.readInt() != 0 ? WifiDppConfig.CREATOR.createFromParcel(data) : null;
                    int _result26 = dppBootstrapGenerate(_arg046);
                    reply.writeNoException();
                    reply.writeInt(_result26);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg047 = data.readInt();
                    String _result27 = dppGetUri(_arg047);
                    reply.writeNoException();
                    reply.writeString(_result27);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg048 = data.readInt();
                    int _result28 = dppBootstrapRemove(_arg048);
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg049 = data.readString();
                    int _arg119 = data.readInt();
                    boolean _arg25 = data.readInt() != 0;
                    _arg3 = data.readInt() != 0;
                    int _result29 = dppListen(_arg049, _arg119, _arg25, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result29);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    dppStopListen();
                    reply.writeNoException();
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg050 = data.readString();
                    String _arg120 = data.readString();
                    int _arg26 = data.readInt();
                    int _result30 = dppConfiguratorAdd(_arg050, _arg120, _arg26);
                    reply.writeNoException();
                    reply.writeInt(_result30);
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg051 = data.readInt();
                    int _result31 = dppConfiguratorRemove(_arg051);
                    reply.writeNoException();
                    reply.writeInt(_result31);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    WifiDppConfig _arg052 = data.readInt() != 0 ? WifiDppConfig.CREATOR.createFromParcel(data) : null;
                    int _result32 = dppStartAuth(_arg052);
                    reply.writeNoException();
                    reply.writeInt(_result32);
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg053 = data.readInt();
                    String _result33 = dppConfiguratorGetKey(_arg053);
                    reply.writeNoException();
                    reply.writeString(_result33);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IWifiManager {
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

            @Override // android.net.wifi.IWifiManager
            public synchronized int getSupportedFeatures() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized WifiActivityEnergyInfo reportActivityInfo() throws RemoteException {
                WifiActivityEnergyInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = WifiActivityEnergyInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void requestActivityInfo(ResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized ParceledListSlice getConfiguredNetworks() throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
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

            @Override // android.net.wifi.IWifiManager
            public synchronized ParceledListSlice getPrivilegedConfiguredNetworks() throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
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

            @Override // android.net.wifi.IWifiManager
            public synchronized WifiConfiguration getMatchingWifiConfig(ScanResult scanResult) throws RemoteException {
                WifiConfiguration _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (scanResult != null) {
                        _data.writeInt(1);
                        scanResult.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = WifiConfiguration.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized List<WifiConfiguration> getAllMatchingWifiConfigs(ScanResult scanResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (scanResult != null) {
                        _data.writeInt(1);
                        scanResult.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    List<WifiConfiguration> _result = _reply.createTypedArrayList(WifiConfiguration.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized List<OsuProvider> getMatchingOsuProviders(ScanResult scanResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (scanResult != null) {
                        _data.writeInt(1);
                        scanResult.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    List<OsuProvider> _result = _reply.createTypedArrayList(OsuProvider.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized int addOrUpdateNetwork(WifiConfiguration config, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean addOrUpdatePasspointConfiguration(PasspointConfiguration config, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean removePasspointConfiguration(String fqdn, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fqdn);
                    _data.writeString(packageName);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized List<PasspointConfiguration> getPasspointConfigurations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    List<PasspointConfiguration> _result = _reply.createTypedArrayList(PasspointConfiguration.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void queryPasspointIcon(long bssid, String fileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(bssid);
                    _data.writeString(fileName);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized int matchProviderWithCurrentNetwork(String fqdn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fqdn);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void deauthenticateNetwork(long holdoff, boolean ess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(holdoff);
                    _data.writeInt(ess ? 1 : 0);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean removeNetwork(int netId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeString(packageName);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean enableNetwork(int netId, boolean disableOthers, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeInt(disableOthers ? 1 : 0);
                    _data.writeString(packageName);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean disableNetwork(int netId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeString(packageName);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean startScan(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized List<ScanResult> getScanResults(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    List<ScanResult> _result = _reply.createTypedArrayList(ScanResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void disconnect(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void reconnect(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void reassociate(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized WifiInfo getConnectionInfo(String callingPackage) throws RemoteException {
                WifiInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = WifiInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean setWifiEnabled(String packageName, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized int getWifiEnabledState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void setCountryCode(String country) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(country);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized String getCountryCode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean isDualBandSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean needs5GHzToAnyApBandConversion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized DhcpInfo getDhcpInfo() throws RemoteException {
                DhcpInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = DhcpInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean isScanAlwaysAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean acquireWifiLock(IBinder lock, int lockType, String tag, WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    _data.writeInt(lockType);
                    _data.writeString(tag);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void updateWifiLockWorkSource(IBinder lock, WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean releaseWifiLock(IBinder lock) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(lock);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void initializeMulticastFiltering() throws RemoteException {
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

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean isMulticastEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void acquireMulticastLock(IBinder binder, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(tag);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void releaseMulticastLock() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void updateInterfaceIpState(String ifaceName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ifaceName);
                    _data.writeInt(mode);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean startSoftAp(WifiConfiguration wifiConfig) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (wifiConfig != null) {
                        _data.writeInt(1);
                        wifiConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean stopSoftAp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized int startLocalOnlyHotspot(Messenger messenger, IBinder binder, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    _data.writeString(packageName);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void stopLocalOnlyHotspot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void startWatchLocalOnlyHotspot(Messenger messenger, IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void stopWatchLocalOnlyHotspot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getWifiApEnabledState() throws RemoteException {
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

            public synchronized WifiConfiguration getWifiApConfiguration() throws RemoteException {
                WifiConfiguration _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = WifiConfiguration.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized boolean setWifiApConfiguration(WifiConfiguration wifiConfig, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (wifiConfig != null) {
                        _data.writeInt(1);
                        wifiConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public void notifyUserOfApBandConversion(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized Messenger getWifiServiceMessenger(String packageName) throws RemoteException {
                Messenger _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Messenger.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void enableTdls(String remoteIPAddress, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(remoteIPAddress);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void enableTdlsWithMacAddress(String remoteMacAddress, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(remoteMacAddress);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized String getCurrentNetworkWpsNfcConfigurationToken() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void enableVerboseLogging(int verbose) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(verbose);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized int getVerboseLoggingLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void enableWifiConnectivityManager(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void disableEphemeralNetwork(String SSID, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(SSID);
                    _data.writeString(packageName);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void factoryReset(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized Network getCurrentNetwork() throws RemoteException {
                Network _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Network.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized byte[] retrieveBackupData() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void restoreBackupData(byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(data);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void restoreSupplicantBackupData(byte[] supplicantData, byte[] ipConfigData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(supplicantData);
                    _data.writeByteArray(ipConfigData);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void startSubscriptionProvisioning(OsuProvider provider, IProvisioningCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void registerSoftApCallback(IBinder binder, ISoftApCallback callback, int callbackIdentifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(callbackIdentifier);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public synchronized void unregisterSoftApCallback(int callbackIdentifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callbackIdentifier);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public String getCapabilities(String capaType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(capaType);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public int dppAddBootstrapQrCode(String uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uri);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public int dppBootstrapGenerate(WifiDppConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public String dppGetUri(int bootstrap_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bootstrap_id);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public int dppBootstrapRemove(int bootstrap_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bootstrap_id);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public int dppListen(String frequency, int dpp_role, boolean qr_mutual, boolean netrole_ap) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(frequency);
                    _data.writeInt(dpp_role);
                    _data.writeInt(qr_mutual ? 1 : 0);
                    _data.writeInt(netrole_ap ? 1 : 0);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public void dppStopListen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public int dppConfiguratorAdd(String curve, String key, int expiry) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(curve);
                    _data.writeString(key);
                    _data.writeInt(expiry);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public int dppConfiguratorRemove(int config_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(config_id);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public int dppStartAuth(WifiDppConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.wifi.IWifiManager
            public String dppConfiguratorGetKey(int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
