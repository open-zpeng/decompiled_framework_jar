package android.net.wifi;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.wifi.ISoftApCallback;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.IProvisioningCallback;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.ProvisioningCallback;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.WorkSource;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AsyncChannel;
import com.android.internal.util.Protocol;
import com.android.server.net.NetworkPinner;
import dalvik.system.CloseGuard;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
/* loaded from: classes2.dex */
public class WifiManager {
    public static final String ACTION_PASSPOINT_DEAUTH_IMMINENT = "android.net.wifi.action.PASSPOINT_DEAUTH_IMMINENT";
    public static final String ACTION_PASSPOINT_ICON = "android.net.wifi.action.PASSPOINT_ICON";
    public static final String ACTION_PASSPOINT_OSU_PROVIDERS_LIST = "android.net.wifi.action.PASSPOINT_OSU_PROVIDERS_LIST";
    public static final String ACTION_PASSPOINT_SUBSCRIPTION_REMEDIATION = "android.net.wifi.action.PASSPOINT_SUBSCRIPTION_REMEDIATION";
    public static final String ACTION_PICK_WIFI_NETWORK = "android.net.wifi.PICK_WIFI_NETWORK";
    public static final String ACTION_REQUEST_DISABLE = "android.net.wifi.action.REQUEST_DISABLE";
    public static final String ACTION_REQUEST_ENABLE = "android.net.wifi.action.REQUEST_ENABLE";
    public static final String ACTION_REQUEST_SCAN_ALWAYS_AVAILABLE = "android.net.wifi.action.REQUEST_SCAN_ALWAYS_AVAILABLE";
    private static final int BASE = 151552;
    @Deprecated
    public static final String BATCHED_SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.BATCHED_RESULTS";
    public static final int BUSY = 2;
    public static final int CANCEL_WPS = 151566;
    public static final int CANCEL_WPS_FAILED = 151567;
    public static final int CANCEL_WPS_SUCCEDED = 151568;
    @SystemApi
    public static final int CHANGE_REASON_ADDED = 0;
    @SystemApi
    public static final int CHANGE_REASON_CONFIG_CHANGE = 2;
    @SystemApi
    public static final int CHANGE_REASON_REMOVED = 1;
    @SystemApi
    public static final String CONFIGURED_NETWORKS_CHANGED_ACTION = "android.net.wifi.CONFIGURED_NETWORKS_CHANGE";
    public static final int CONNECT_NETWORK = 151553;
    public static final int CONNECT_NETWORK_FAILED = 151554;
    public static final int CONNECT_NETWORK_SUCCEEDED = 151555;
    public static final int DATA_ACTIVITY_IN = 1;
    public static final int DATA_ACTIVITY_INOUT = 3;
    public static final int DATA_ACTIVITY_NONE = 0;
    public static final int DATA_ACTIVITY_NOTIFICATION = 1;
    public static final int DATA_ACTIVITY_OUT = 2;
    public static final boolean DEFAULT_POOR_NETWORK_AVOIDANCE_ENABLED = false;
    public static final int DISABLE_NETWORK = 151569;
    public static final int DISABLE_NETWORK_FAILED = 151570;
    public static final int DISABLE_NETWORK_SUCCEEDED = 151571;
    public static final String DPP_EVENT_ACTION = "com.qualcomm.qti.net.wifi.DPP_EVENT";
    public static final int ERROR = 0;
    @Deprecated
    public static final int ERROR_AUTHENTICATING = 1;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_EAP_FAILURE = 3;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_NONE = 0;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_TIMEOUT = 1;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_WRONG_PSWD = 2;
    public static final String EXTRA_ANQP_ELEMENT_DATA = "android.net.wifi.extra.ANQP_ELEMENT_DATA";
    @Deprecated
    public static final String EXTRA_BSSID = "bssid";
    public static final String EXTRA_BSSID_LONG = "android.net.wifi.extra.BSSID_LONG";
    @SystemApi
    public static final String EXTRA_CHANGE_REASON = "changeReason";
    public static final String EXTRA_DELAY = "android.net.wifi.extra.DELAY";
    public static final String EXTRA_DPP_EVENT_DATA = "dppEventData";
    public static final String EXTRA_DPP_EVENT_TYPE = "dppEventType";
    public static final String EXTRA_ESS = "android.net.wifi.extra.ESS";
    public static final String EXTRA_FILENAME = "android.net.wifi.extra.FILENAME";
    public static final String EXTRA_ICON = "android.net.wifi.extra.ICON";
    public static final String EXTRA_LINK_PROPERTIES = "linkProperties";
    @SystemApi
    public static final String EXTRA_MULTIPLE_NETWORKS_CHANGED = "multipleChanges";
    public static final String EXTRA_NETWORK_CAPABILITIES = "networkCapabilities";
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_NEW_RSSI = "newRssi";
    @Deprecated
    public static final String EXTRA_NEW_STATE = "newState";
    @SystemApi
    public static final String EXTRA_PREVIOUS_WIFI_AP_STATE = "previous_wifi_state";
    public static final String EXTRA_PREVIOUS_WIFI_STATE = "previous_wifi_state";
    public static final String EXTRA_RESULTS_UPDATED = "resultsUpdated";
    public static final String EXTRA_SCAN_AVAILABLE = "scan_enabled";
    public static final String EXTRA_SUBSCRIPTION_REMEDIATION_METHOD = "android.net.wifi.extra.SUBSCRIPTION_REMEDIATION_METHOD";
    @Deprecated
    public static final String EXTRA_SUPPLICANT_CONNECTED = "connected";
    @Deprecated
    public static final String EXTRA_SUPPLICANT_ERROR = "supplicantError";
    @Deprecated
    public static final String EXTRA_SUPPLICANT_ERROR_REASON = "supplicantErrorReason";
    public static final String EXTRA_URL = "android.net.wifi.extra.URL";
    public static final String EXTRA_WIFI_AP_FAILURE_REASON = "wifi_ap_error_code";
    public static final String EXTRA_WIFI_AP_INTERFACE_NAME = "wifi_ap_interface_name";
    public static final String EXTRA_WIFI_AP_MODE = "wifi_ap_mode";
    @SystemApi
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
    @SystemApi
    public static final String EXTRA_WIFI_CONFIGURATION = "wifiConfiguration";
    @SystemApi
    public static final String EXTRA_WIFI_CREDENTIAL_EVENT_TYPE = "et";
    @SystemApi
    public static final String EXTRA_WIFI_CREDENTIAL_SSID = "ssid";
    @Deprecated
    public static final String EXTRA_WIFI_INFO = "wifiInfo";
    public static final String EXTRA_WIFI_STATE = "wifi_state";
    public static final int FORGET_NETWORK = 151556;
    public static final int FORGET_NETWORK_FAILED = 151557;
    public static final int FORGET_NETWORK_SUCCEEDED = 151558;
    public static final int HOTSPOT_FAILED = 2;
    public static final int HOTSPOT_OBSERVER_REGISTERED = 3;
    public static final int HOTSPOT_STARTED = 0;
    public static final int HOTSPOT_STOPPED = 1;
    public static final int IFACE_IP_MODE_CONFIGURATION_ERROR = 0;
    public static final int IFACE_IP_MODE_LOCAL_ONLY = 2;
    public static final int IFACE_IP_MODE_TETHERED = 1;
    public static final int IFACE_IP_MODE_UNSPECIFIED = -1;
    public static final int INVALID_ARGS = 8;
    private static final int INVALID_KEY = 0;
    public static final int IN_PROGRESS = 1;
    private protected static final String LINK_CONFIGURATION_CHANGED_ACTION = "android.net.wifi.LINK_CONFIGURATION_CHANGED";
    private static final int MAX_ACTIVE_LOCKS = 50;
    public protected static final int MAX_RSSI = -55;
    public protected static final int MIN_RSSI = -100;
    public static final String NETWORK_IDS_CHANGED_ACTION = "android.net.wifi.NETWORK_IDS_CHANGED";
    public static final String NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE";
    public static final int NOT_AUTHORIZED = 9;
    public static final String RSSI_CHANGED_ACTION = "android.net.wifi.RSSI_CHANGED";
    private protected static final int RSSI_LEVELS = 5;
    public static final int RSSI_PKTCNT_FETCH = 151572;
    public static final int RSSI_PKTCNT_FETCH_FAILED = 151574;
    public static final int RSSI_PKTCNT_FETCH_SUCCEEDED = 151573;
    public static final int SAP_START_FAILURE_GENERAL = 0;
    public static final int SAP_START_FAILURE_NO_CHANNEL = 1;
    public static final int SAVE_NETWORK = 151559;
    public static final int SAVE_NETWORK_FAILED = 151560;
    public static final int SAVE_NETWORK_SUCCEEDED = 151561;
    public static final String SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.SCAN_RESULTS";
    public static final int START_WPS = 151562;
    public static final int START_WPS_SUCCEEDED = 151563;
    @Deprecated
    public static final String SUPPLICANT_CONNECTION_CHANGE_ACTION = "android.net.wifi.supplicant.CONNECTION_CHANGE";
    @Deprecated
    public static final String SUPPLICANT_STATE_CHANGED_ACTION = "android.net.wifi.supplicant.STATE_CHANGE";
    private static final String TAG = "WifiManager";
    @SystemApi
    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    @SystemApi
    public static final int WIFI_AP_STATE_DISABLED = 11;
    @SystemApi
    public static final int WIFI_AP_STATE_DISABLING = 10;
    @SystemApi
    public static final int WIFI_AP_STATE_ENABLED = 13;
    @SystemApi
    public static final int WIFI_AP_STATE_ENABLING = 12;
    @SystemApi
    public static final int WIFI_AP_STATE_FAILED = 14;
    @SystemApi
    public static final String WIFI_CREDENTIAL_CHANGED_ACTION = "android.net.wifi.WIFI_CREDENTIAL_CHANGED";
    @SystemApi
    public static final int WIFI_CREDENTIAL_FORGOT = 1;
    @SystemApi
    public static final int WIFI_CREDENTIAL_SAVED = 0;
    public static final int WIFI_FEATURE_ADDITIONAL_STA = 2048;
    public static final int WIFI_FEATURE_AP_STA = 32768;
    public static final int WIFI_FEATURE_AWARE = 64;
    public static final int WIFI_FEATURE_BATCH_SCAN = 512;
    public static final int WIFI_FEATURE_CONFIG_NDO = 2097152;
    public static final int WIFI_FEATURE_CONTROL_ROAMING = 8388608;
    public static final int WIFI_FEATURE_D2AP_RTT = 256;
    public static final int WIFI_FEATURE_D2D_RTT = 128;
    public static final int WIFI_FEATURE_EPR = 16384;
    public static final int WIFI_FEATURE_HAL_EPNO = 262144;
    public static final int WIFI_FEATURE_IE_WHITELIST = 16777216;
    public static final int WIFI_FEATURE_INFRA = 1;
    public static final int WIFI_FEATURE_INFRA_5G = 2;
    public static final int WIFI_FEATURE_LINK_LAYER_STATS = 65536;
    public static final int WIFI_FEATURE_LOGGER = 131072;
    public static final int WIFI_FEATURE_MKEEP_ALIVE = 1048576;
    public static final int WIFI_FEATURE_MOBILE_HOTSPOT = 16;
    public static final int WIFI_FEATURE_P2P = 8;
    public static final int WIFI_FEATURE_PASSPOINT = 4;
    public static final int WIFI_FEATURE_PNO = 1024;
    public static final int WIFI_FEATURE_RSSI_MONITOR = 524288;
    public static final int WIFI_FEATURE_SCANNER = 32;
    public static final int WIFI_FEATURE_SCAN_RAND = 33554432;
    public static final int WIFI_FEATURE_TDLS = 4096;
    public static final int WIFI_FEATURE_TDLS_OFFCHANNEL = 8192;
    public static final int WIFI_FEATURE_TRANSMIT_POWER = 4194304;
    public static final int WIFI_FEATURE_TX_POWER_LIMIT = 67108864;
    private protected static final int WIFI_FREQUENCY_BAND_2GHZ = 2;
    private protected static final int WIFI_FREQUENCY_BAND_5GHZ = 1;
    private protected static final int WIFI_FREQUENCY_BAND_AUTO = 0;
    public static final int WIFI_MODE_FULL = 1;
    public static final int WIFI_MODE_FULL_HIGH_PERF = 3;
    public static final int WIFI_MODE_NO_LOCKS_HELD = 0;
    public static final int WIFI_MODE_SCAN_ONLY = 2;
    public static final String WIFI_SCAN_AVAILABLE = "wifi_scan_available";
    public static final String WIFI_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    public static final int WIFI_STATE_DISABLED = 1;
    public static final int WIFI_STATE_DISABLING = 0;
    public static final int WIFI_STATE_ENABLED = 3;
    public static final int WIFI_STATE_ENABLING = 2;
    public static final int WIFI_STATE_UNKNOWN = 4;
    public static final int WPS_AUTH_FAILURE = 6;
    public static final int WPS_COMPLETED = 151565;
    public static final int WPS_FAILED = 151564;
    public static final int WPS_OVERLAP_ERROR = 3;
    public static final int WPS_TIMED_OUT = 7;
    public static final int WPS_TKIP_ONLY_PROHIBITED = 5;
    public static final int WPS_WEP_PROHIBITED = 4;
    private static final Object sServiceHandlerDispatchLock = new Object();
    public protected int mActiveLockCount;
    private AsyncChannel mAsyncChannel;
    private CountDownLatch mConnected;
    private Context mContext;
    @GuardedBy("mLock")
    private LocalOnlyHotspotCallbackProxy mLOHSCallbackProxy;
    @GuardedBy("mLock")
    private LocalOnlyHotspotObserverProxy mLOHSObserverProxy;
    private int mListenerKey = 1;
    private final SparseArray mListenerMap = new SparseArray();
    private final Object mListenerMapLock = new Object();
    private final Object mLock = new Object();
    private Looper mLooper;
    public private protected IWifiManager mService;
    private final int mTargetSdkVersion;

    @SystemApi
    /* loaded from: classes2.dex */
    public interface ActionListener {
        void onFailure(int i);

        void onSuccess();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface SapStartFailure {
    }

    /* loaded from: classes2.dex */
    public interface TxPacketCountListener {
        synchronized void onFailure(int i);

        synchronized void onSuccess(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface WifiApState {
    }

    /* loaded from: classes2.dex */
    public static abstract class WpsCallback {
        public abstract void onFailed(int i);

        public abstract void onStarted(String str);

        public abstract void onSucceeded();
    }

    static /* synthetic */ int access$708(WifiManager x0) {
        int i = x0.mActiveLockCount;
        x0.mActiveLockCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$710(WifiManager x0) {
        int i = x0.mActiveLockCount;
        x0.mActiveLockCount = i - 1;
        return i;
    }

    public synchronized WifiManager(Context context, IWifiManager service, Looper looper) {
        this.mContext = context;
        this.mService = service;
        this.mLooper = looper;
        this.mTargetSdkVersion = context.getApplicationInfo().targetSdkVersion;
    }

    public List<WifiConfiguration> getConfiguredNetworks() {
        try {
            ParceledListSlice<WifiConfiguration> parceledList = this.mService.getConfiguredNetworks();
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public List<WifiConfiguration> getPrivilegedConfiguredNetworks() {
        try {
            ParceledListSlice<WifiConfiguration> parceledList = this.mService.getPrivilegedConfiguredNetworks();
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected WifiConfiguration getMatchingWifiConfig(ScanResult scanResult) {
        try {
            return this.mService.getMatchingWifiConfig(scanResult);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized List<WifiConfiguration> getAllMatchingWifiConfigs(ScanResult scanResult) {
        try {
            return this.mService.getAllMatchingWifiConfigs(scanResult);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized List<OsuProvider> getMatchingOsuProviders(ScanResult scanResult) {
        try {
            return this.mService.getMatchingOsuProviders(scanResult);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int addNetwork(WifiConfiguration config) {
        if (config == null) {
            return -1;
        }
        config.networkId = -1;
        return addOrUpdateNetwork(config);
    }

    public int updateNetwork(WifiConfiguration config) {
        if (config == null || config.networkId < 0) {
            return -1;
        }
        return addOrUpdateNetwork(config);
    }

    private synchronized int addOrUpdateNetwork(WifiConfiguration config) {
        try {
            return this.mService.addOrUpdateNetwork(config, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addOrUpdatePasspointConfiguration(PasspointConfiguration config) {
        try {
            if (!this.mService.addOrUpdatePasspointConfiguration(config, this.mContext.getOpPackageName())) {
                throw new IllegalArgumentException();
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removePasspointConfiguration(String fqdn) {
        try {
            if (!this.mService.removePasspointConfiguration(fqdn, this.mContext.getOpPackageName())) {
                throw new IllegalArgumentException();
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<PasspointConfiguration> getPasspointConfigurations() {
        try {
            return this.mService.getPasspointConfigurations();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void queryPasspointIcon(long bssid, String fileName) {
        try {
            this.mService.queryPasspointIcon(bssid, fileName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized int matchProviderWithCurrentNetwork(String fqdn) {
        try {
            return this.mService.matchProviderWithCurrentNetwork(fqdn);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void deauthenticateNetwork(long holdoff, boolean ess) {
        try {
            this.mService.deauthenticateNetwork(holdoff, ess);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean removeNetwork(int netId) {
        try {
            return this.mService.removeNetwork(netId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean enableNetwork(int netId, boolean attemptConnect) {
        boolean pin = attemptConnect && this.mTargetSdkVersion < 21;
        if (pin) {
            NetworkRequest request = new NetworkRequest.Builder().clearCapabilities().addCapability(15).addTransportType(1).build();
            NetworkPinner.pin(this.mContext, request);
        }
        try {
            boolean success = this.mService.enableNetwork(netId, attemptConnect, this.mContext.getOpPackageName());
            if (pin && !success) {
                NetworkPinner.unpin();
            }
            return success;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean disableNetwork(int netId) {
        try {
            return this.mService.disableNetwork(netId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean disconnect() {
        try {
            this.mService.disconnect(this.mContext.getOpPackageName());
            return true;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean reconnect() {
        try {
            this.mService.reconnect(this.mContext.getOpPackageName());
            return true;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean reassociate() {
        try {
            this.mService.reassociate(this.mContext.getOpPackageName());
            return true;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean pingSupplicant() {
        return isWifiEnabled();
    }

    private synchronized int getSupportedFeatures() {
        try {
            return this.mService.getSupportedFeatures();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private synchronized boolean isFeatureSupported(int feature) {
        return (getSupportedFeatures() & feature) == feature;
    }

    public boolean is5GHzBandSupported() {
        return isFeatureSupported(2);
    }

    public synchronized boolean isPasspointSupported() {
        return isFeatureSupported(4);
    }

    public boolean isP2pSupported() {
        return isFeatureSupported(8);
    }

    @SystemApi
    public boolean isPortableHotspotSupported() {
        return isFeatureSupported(16);
    }

    @SystemApi
    public boolean isWifiScannerSupported() {
        return isFeatureSupported(32);
    }

    public synchronized boolean isWifiAwareSupported() {
        return isFeatureSupported(64);
    }

    @SystemApi
    public boolean isDeviceToDeviceRttSupported() {
        return isFeatureSupported(128);
    }

    @SystemApi
    public boolean isDeviceToApRttSupported() {
        return isFeatureSupported(256);
    }

    public boolean isPreferredNetworkOffloadSupported() {
        return isFeatureSupported(1024);
    }

    public synchronized boolean isAdditionalStaSupported() {
        return isFeatureSupported(2048);
    }

    public boolean isTdlsSupported() {
        return isFeatureSupported(4096);
    }

    public synchronized boolean isOffChannelTdlsSupported() {
        return isFeatureSupported(8192);
    }

    public boolean isEnhancedPowerReportingSupported() {
        return isFeatureSupported(65536);
    }

    public synchronized WifiActivityEnergyInfo getControllerActivityEnergyInfo(int updateType) {
        WifiActivityEnergyInfo reportActivityInfo;
        if (this.mService == null) {
            return null;
        }
        try {
            synchronized (this) {
                reportActivityInfo = this.mService.reportActivityInfo();
            }
            return reportActivityInfo;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean startScan() {
        return startScan(null);
    }

    @SystemApi
    public boolean startScan(WorkSource workSource) {
        try {
            String packageName = this.mContext.getOpPackageName();
            return this.mService.startScan(packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized String getCurrentNetworkWpsNfcConfigurationToken() {
        return null;
    }

    public WifiInfo getConnectionInfo() {
        try {
            return this.mService.getConnectionInfo(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ScanResult> getScanResults() {
        try {
            return this.mService.getScanResults(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isScanAlwaysAvailable() {
        try {
            return this.mService.isScanAlwaysAvailable();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean saveConfiguration() {
        return true;
    }

    public synchronized void setCountryCode(String country) {
        try {
            this.mService.setCountryCode(country);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected String getCountryCode() {
        try {
            String country = this.mService.getCountryCode();
            return country;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected boolean isDualBandSupported() {
        try {
            return this.mService.isDualBandSupported();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized boolean isDualModeSupported() {
        try {
            return this.mService.needs5GHzToAnyApBandConversion();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public DhcpInfo getDhcpInfo() {
        try {
            return this.mService.getDhcpInfo();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean setWifiEnabled(boolean enabled) {
        try {
            return this.mService.setWifiEnabled(this.mContext.getOpPackageName(), enabled);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getWifiState() {
        try {
            return this.mService.getWifiEnabledState();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isWifiEnabled() {
        return getWifiState() == 3;
    }

    public synchronized void getTxPacketCount(TxPacketCountListener listener) {
        getChannel().sendMessage(RSSI_PKTCNT_FETCH, 0, putListener(listener));
    }

    public static int calculateSignalLevel(int rssi, int numLevels) {
        if (rssi <= -100) {
            return 0;
        }
        if (rssi >= -55) {
            return numLevels - 1;
        }
        float outputRange = numLevels - 1;
        return (int) (((rssi + 100) * outputRange) / 45.0f);
    }

    public static int compareSignalLevel(int rssiA, int rssiB) {
        return rssiA - rssiB;
    }

    public synchronized void updateInterfaceIpState(String ifaceName, int mode) {
        try {
            this.mService.updateInterfaceIpState(ifaceName, mode);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized boolean startSoftAp(WifiConfiguration wifiConfig) {
        try {
            return this.mService.startSoftAp(wifiConfig);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized boolean stopSoftAp() {
        try {
            return this.mService.stopSoftAp();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void startLocalOnlyHotspot(LocalOnlyHotspotCallback callback, Handler handler) {
        synchronized (this.mLock) {
            try {
                Looper looper = handler == null ? this.mContext.getMainLooper() : handler.getLooper();
                LocalOnlyHotspotCallbackProxy proxy = new LocalOnlyHotspotCallbackProxy(this, looper, callback);
                try {
                    String packageName = this.mContext.getOpPackageName();
                    int returnCode = this.mService.startLocalOnlyHotspot(proxy.getMessenger(), new Binder(), packageName);
                    if (returnCode != 0) {
                        proxy.notifyFailed(returnCode);
                    } else {
                        this.mLOHSCallbackProxy = proxy;
                    }
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private protected void cancelLocalOnlyHotspotRequest() {
        synchronized (this.mLock) {
            stopLocalOnlyHotspot();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void stopLocalOnlyHotspot() {
        synchronized (this.mLock) {
            if (this.mLOHSCallbackProxy == null) {
                return;
            }
            this.mLOHSCallbackProxy = null;
            try {
                this.mService.stopLocalOnlyHotspot();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public synchronized void watchLocalOnlyHotspot(LocalOnlyHotspotObserver observer, Handler handler) {
        synchronized (this.mLock) {
            try {
                Looper looper = handler == null ? this.mContext.getMainLooper() : handler.getLooper();
                this.mLOHSObserverProxy = new LocalOnlyHotspotObserverProxy(this, looper, observer);
                try {
                    this.mService.startWatchLocalOnlyHotspot(this.mLOHSObserverProxy.getMessenger(), new Binder());
                    this.mLOHSObserverProxy.registered();
                } catch (RemoteException e) {
                    this.mLOHSObserverProxy = null;
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public synchronized void unregisterLocalOnlyHotspotObserver() {
        synchronized (this.mLock) {
            if (this.mLOHSObserverProxy == null) {
                return;
            }
            this.mLOHSObserverProxy = null;
            try {
                this.mService.stopWatchLocalOnlyHotspot();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @SystemApi
    public int getWifiApState() {
        try {
            return this.mService.getWifiApEnabledState();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean isWifiApEnabled() {
        return getWifiApState() == 13;
    }

    @SystemApi
    public WifiConfiguration getWifiApConfiguration() {
        try {
            return this.mService.getWifiApConfiguration();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean setWifiApConfiguration(WifiConfiguration wifiConfig) {
        try {
            return this.mService.setWifiApConfiguration(wifiConfig, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void notifyUserOfApBandConversion() {
        Log.d(TAG, "apBand was converted, notify the user");
        try {
            this.mService.notifyUserOfApBandConversion(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTdlsEnabled(InetAddress remoteIPAddress, boolean enable) {
        try {
            this.mService.enableTdls(remoteIPAddress.getHostAddress(), enable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTdlsEnabledWithMacAddress(String remoteMacAddress, boolean enable) {
        try {
            this.mService.enableTdlsWithMacAddress(remoteMacAddress, enable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* loaded from: classes2.dex */
    public interface SoftApCallback {
        synchronized void onNumClientsChanged(int i);

        synchronized void onStateChanged(int i, int i2);

        default void onClientsUpdated(List<WifiClient> clients) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class SoftApCallbackProxy extends ISoftApCallback.Stub {
        private final SoftApCallback mCallback;
        private final Handler mHandler;

        synchronized SoftApCallbackProxy(Looper looper, SoftApCallback callback) {
            this.mHandler = new Handler(looper);
            this.mCallback = callback;
        }

        @Override // android.net.wifi.ISoftApCallback
        public synchronized void onStateChanged(final int state, final int failureReason) throws RemoteException {
            Log.v(WifiManager.TAG, "SoftApCallbackProxy: onStateChanged: state=" + state + ", failureReason=" + failureReason);
            this.mHandler.post(new Runnable() { // from class: android.net.wifi.-$$Lambda$WifiManager$SoftApCallbackProxy$vmSW5veUpC52oRINBy419US5snk
                @Override // java.lang.Runnable
                public final void run() {
                    WifiManager.SoftApCallbackProxy.this.mCallback.onStateChanged(state, failureReason);
                }
            });
        }

        @Override // android.net.wifi.ISoftApCallback
        public synchronized void onNumClientsChanged(final int numClients) throws RemoteException {
            Log.v(WifiManager.TAG, "SoftApCallbackProxy: onNumClientsChanged: numClients=" + numClients);
            this.mHandler.post(new Runnable() { // from class: android.net.wifi.-$$Lambda$WifiManager$SoftApCallbackProxy$f44R8L0UcqgnIaD5lXMmeuRHCWI
                @Override // java.lang.Runnable
                public final void run() {
                    WifiManager.SoftApCallbackProxy.this.mCallback.onNumClientsChanged(numClients);
                }
            });
        }

        @Override // android.net.wifi.ISoftApCallback
        public void onClientsUpdated(final ParceledListSlice clients) throws RemoteException {
            Log.v(WifiManager.TAG, "SoftApCallbackProxy: onClientsUpdated: Clients=" + clients);
            this.mHandler.post(new Runnable() { // from class: android.net.wifi.-$$Lambda$WifiManager$SoftApCallbackProxy$GRG3AHOLp7Fd-hucrODwK2CdL_k
                @Override // java.lang.Runnable
                public final void run() {
                    WifiManager.SoftApCallbackProxy.lambda$onClientsUpdated$2(WifiManager.SoftApCallbackProxy.this, clients);
                }
            });
        }

        public static /* synthetic */ void lambda$onClientsUpdated$2(SoftApCallbackProxy softApCallbackProxy, ParceledListSlice clients) {
            if (clients == null) {
                softApCallbackProxy.mCallback.onClientsUpdated(Collections.emptyList());
            } else {
                softApCallbackProxy.mCallback.onClientsUpdated(clients.getList());
            }
        }
    }

    public synchronized void registerSoftApCallback(SoftApCallback callback, Handler handler) {
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null");
        }
        Log.v(TAG, "registerSoftApCallback: callback=" + callback + ", handler=" + handler);
        Looper looper = handler == null ? this.mContext.getMainLooper() : handler.getLooper();
        Binder binder = new Binder();
        try {
            this.mService.registerSoftApCallback(binder, new SoftApCallbackProxy(looper, callback), callback.hashCode());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void unregisterSoftApCallback(SoftApCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null");
        }
        Log.v(TAG, "unregisterSoftApCallback: callback=" + callback);
        try {
            this.mService.unregisterSoftApCallback(callback.hashCode());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* loaded from: classes2.dex */
    public class LocalOnlyHotspotReservation implements AutoCloseable {
        private final CloseGuard mCloseGuard = CloseGuard.get();
        private final WifiConfiguration mConfig;

        @VisibleForTesting
        public LocalOnlyHotspotReservation(WifiConfiguration config) {
            this.mConfig = config;
            this.mCloseGuard.open("close");
        }

        public WifiConfiguration getWifiConfiguration() {
            return this.mConfig;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            try {
                WifiManager.this.stopLocalOnlyHotspot();
                this.mCloseGuard.close();
            } catch (Exception e) {
                Log.e(WifiManager.TAG, "Failed to stop Local Only Hotspot.");
            }
        }

        protected void finalize() throws Throwable {
            try {
                if (this.mCloseGuard != null) {
                    this.mCloseGuard.warnIfOpen();
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class LocalOnlyHotspotCallback {
        public static final int ERROR_GENERIC = 2;
        public static final int ERROR_INCOMPATIBLE_MODE = 3;
        public static final int ERROR_NO_CHANNEL = 1;
        public static final int ERROR_TETHERING_DISALLOWED = 4;
        public static final int REQUEST_REGISTERED = 0;

        public void onStarted(LocalOnlyHotspotReservation reservation) {
        }

        public void onStopped() {
        }

        public void onFailed(int reason) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class LocalOnlyHotspotCallbackProxy {
        private final Handler mHandler;
        private final Looper mLooper;
        private final Messenger mMessenger;
        private final WeakReference<WifiManager> mWifiManager;

        synchronized LocalOnlyHotspotCallbackProxy(WifiManager manager, Looper looper, final LocalOnlyHotspotCallback callback) {
            this.mWifiManager = new WeakReference<>(manager);
            this.mLooper = looper;
            this.mHandler = new Handler(looper) { // from class: android.net.wifi.WifiManager.LocalOnlyHotspotCallbackProxy.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    Log.d(WifiManager.TAG, "LocalOnlyHotspotCallbackProxy: handle message what: " + msg.what + " msg: " + msg);
                    WifiManager manager2 = (WifiManager) LocalOnlyHotspotCallbackProxy.this.mWifiManager.get();
                    if (manager2 == null) {
                        Log.w(WifiManager.TAG, "LocalOnlyHotspotCallbackProxy: handle message post GC");
                        return;
                    }
                    switch (msg.what) {
                        case 0:
                            WifiConfiguration config = (WifiConfiguration) msg.obj;
                            if (config == null) {
                                Log.e(WifiManager.TAG, "LocalOnlyHotspotCallbackProxy: config cannot be null.");
                                callback.onFailed(2);
                                return;
                            }
                            LocalOnlyHotspotCallback localOnlyHotspotCallback = callback;
                            Objects.requireNonNull(manager2);
                            localOnlyHotspotCallback.onStarted(new LocalOnlyHotspotReservation(config));
                            return;
                        case 1:
                            Log.w(WifiManager.TAG, "LocalOnlyHotspotCallbackProxy: hotspot stopped");
                            callback.onStopped();
                            return;
                        case 2:
                            int reasonCode = msg.arg1;
                            Log.w(WifiManager.TAG, "LocalOnlyHotspotCallbackProxy: failed to start.  reason: " + reasonCode);
                            callback.onFailed(reasonCode);
                            Log.w(WifiManager.TAG, "done with the callback...");
                            return;
                        default:
                            Log.e(WifiManager.TAG, "LocalOnlyHotspotCallbackProxy unhandled message.  type: " + msg.what);
                            return;
                    }
                }
            };
            this.mMessenger = new Messenger(this.mHandler);
        }

        public synchronized Messenger getMessenger() {
            return this.mMessenger;
        }

        public synchronized void notifyFailed(int reason) throws RemoteException {
            Message msg = Message.obtain();
            msg.what = 2;
            msg.arg1 = reason;
            this.mMessenger.send(msg);
        }
    }

    /* loaded from: classes2.dex */
    public class LocalOnlyHotspotSubscription implements AutoCloseable {
        private final CloseGuard mCloseGuard = CloseGuard.get();

        @VisibleForTesting
        public LocalOnlyHotspotSubscription() {
            this.mCloseGuard.open("close");
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            try {
                WifiManager.this.unregisterLocalOnlyHotspotObserver();
                this.mCloseGuard.close();
            } catch (Exception e) {
                Log.e(WifiManager.TAG, "Failed to unregister LocalOnlyHotspotObserver.");
            }
        }

        protected void finalize() throws Throwable {
            try {
                if (this.mCloseGuard != null) {
                    this.mCloseGuard.warnIfOpen();
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class LocalOnlyHotspotObserver {
        public synchronized void onRegistered(LocalOnlyHotspotSubscription subscription) {
        }

        public synchronized void onStarted(WifiConfiguration config) {
        }

        public synchronized void onStopped() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class LocalOnlyHotspotObserverProxy {
        private final Handler mHandler;
        private final Looper mLooper;
        private final Messenger mMessenger;
        private final WeakReference<WifiManager> mWifiManager;

        synchronized LocalOnlyHotspotObserverProxy(WifiManager manager, Looper looper, final LocalOnlyHotspotObserver observer) {
            this.mWifiManager = new WeakReference<>(manager);
            this.mLooper = looper;
            this.mHandler = new Handler(looper) { // from class: android.net.wifi.WifiManager.LocalOnlyHotspotObserverProxy.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    Log.d(WifiManager.TAG, "LocalOnlyHotspotObserverProxy: handle message what: " + msg.what + " msg: " + msg);
                    WifiManager manager2 = (WifiManager) LocalOnlyHotspotObserverProxy.this.mWifiManager.get();
                    if (manager2 == null) {
                        Log.w(WifiManager.TAG, "LocalOnlyHotspotObserverProxy: handle message post GC");
                        return;
                    }
                    int i = msg.what;
                    if (i == 3) {
                        LocalOnlyHotspotObserver localOnlyHotspotObserver = observer;
                        Objects.requireNonNull(manager2);
                        localOnlyHotspotObserver.onRegistered(new LocalOnlyHotspotSubscription());
                        return;
                    }
                    switch (i) {
                        case 0:
                            WifiConfiguration config = (WifiConfiguration) msg.obj;
                            if (config == null) {
                                Log.e(WifiManager.TAG, "LocalOnlyHotspotObserverProxy: config cannot be null.");
                                return;
                            } else {
                                observer.onStarted(config);
                                return;
                            }
                        case 1:
                            observer.onStopped();
                            return;
                        default:
                            Log.e(WifiManager.TAG, "LocalOnlyHotspotObserverProxy unhandled message.  type: " + msg.what);
                            return;
                    }
                }
            };
            this.mMessenger = new Messenger(this.mHandler);
        }

        public synchronized Messenger getMessenger() {
            return this.mMessenger;
        }

        public synchronized void registered() throws RemoteException {
            Message msg = Message.obtain();
            msg.what = 3;
            this.mMessenger.send(msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            synchronized (WifiManager.sServiceHandlerDispatchLock) {
                dispatchMessageToListeners(message);
            }
        }

        private synchronized void dispatchMessageToListeners(Message message) {
            Object listener = WifiManager.this.removeListener(message.arg2);
            switch (message.what) {
                case Protocol.BASE_SYSTEM_ASYNC_CHANNEL /* 69632 */:
                    if (message.arg1 == 0) {
                        WifiManager.this.mAsyncChannel.sendMessage(69633);
                    } else {
                        Log.e(WifiManager.TAG, "Failed to set up channel connection");
                        WifiManager.this.mAsyncChannel = null;
                    }
                    WifiManager.this.mConnected.countDown();
                    return;
                case AsyncChannel.CMD_CHANNEL_FULLY_CONNECTED /* 69634 */:
                default:
                    return;
                case AsyncChannel.CMD_CHANNEL_DISCONNECTED /* 69636 */:
                    Log.e(WifiManager.TAG, "Channel connection lost");
                    WifiManager.this.mAsyncChannel = null;
                    getLooper().quit();
                    return;
                case WifiManager.CONNECT_NETWORK_FAILED /* 151554 */:
                case WifiManager.FORGET_NETWORK_FAILED /* 151557 */:
                case WifiManager.SAVE_NETWORK_FAILED /* 151560 */:
                case WifiManager.DISABLE_NETWORK_FAILED /* 151570 */:
                    if (listener != null) {
                        ((ActionListener) listener).onFailure(message.arg1);
                        return;
                    }
                    return;
                case WifiManager.CONNECT_NETWORK_SUCCEEDED /* 151555 */:
                case WifiManager.FORGET_NETWORK_SUCCEEDED /* 151558 */:
                case WifiManager.SAVE_NETWORK_SUCCEEDED /* 151561 */:
                case WifiManager.DISABLE_NETWORK_SUCCEEDED /* 151571 */:
                    if (listener != null) {
                        ((ActionListener) listener).onSuccess();
                        return;
                    }
                    return;
                case WifiManager.RSSI_PKTCNT_FETCH_SUCCEEDED /* 151573 */:
                    if (listener != null) {
                        RssiPacketCountInfo info = (RssiPacketCountInfo) message.obj;
                        if (info != null) {
                            ((TxPacketCountListener) listener).onSuccess(info.txgood + info.txbad);
                            return;
                        } else {
                            ((TxPacketCountListener) listener).onFailure(0);
                            return;
                        }
                    }
                    return;
                case WifiManager.RSSI_PKTCNT_FETCH_FAILED /* 151574 */:
                    if (listener != null) {
                        ((TxPacketCountListener) listener).onFailure(message.arg1);
                        return;
                    }
                    return;
            }
        }
    }

    private synchronized int putListener(Object listener) {
        int key;
        if (listener == null) {
            return 0;
        }
        synchronized (this.mListenerMapLock) {
            do {
                key = this.mListenerKey;
                this.mListenerKey = key + 1;
            } while (key == 0);
            this.mListenerMap.put(key, listener);
        }
        return key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Object removeListener(int key) {
        Object listener;
        if (key == 0) {
            return null;
        }
        synchronized (this.mListenerMapLock) {
            listener = this.mListenerMap.get(key);
            this.mListenerMap.remove(key);
        }
        return listener;
    }

    private synchronized AsyncChannel getChannel() {
        if (this.mAsyncChannel == null) {
            Messenger messenger = getWifiServiceMessenger();
            if (messenger == null) {
                throw new IllegalStateException("getWifiServiceMessenger() returned null!  This is invalid.");
            }
            this.mAsyncChannel = new AsyncChannel();
            this.mConnected = new CountDownLatch(1);
            Handler handler = new ServiceHandler(this.mLooper);
            this.mAsyncChannel.connect(this.mContext, handler, messenger);
            try {
                this.mConnected.await();
            } catch (InterruptedException e) {
                Log.e(TAG, "interrupted wait at init");
            }
        }
        return this.mAsyncChannel;
    }

    @SystemApi
    public void connect(WifiConfiguration config, ActionListener listener) {
        if (config == null) {
            throw new IllegalArgumentException("config cannot be null");
        }
        getChannel().sendMessage(CONNECT_NETWORK, -1, putListener(listener), config);
    }

    private protected void connect(int networkId, ActionListener listener) {
        if (networkId < 0) {
            throw new IllegalArgumentException("Network id cannot be negative");
        }
        getChannel().sendMessage(CONNECT_NETWORK, networkId, putListener(listener));
    }

    private protected void save(WifiConfiguration config, ActionListener listener) {
        if (config == null) {
            throw new IllegalArgumentException("config cannot be null");
        }
        getChannel().sendMessage(SAVE_NETWORK, 0, putListener(listener), config);
    }

    private protected void forget(int netId, ActionListener listener) {
        if (netId < 0) {
            throw new IllegalArgumentException("Network id cannot be negative");
        }
        getChannel().sendMessage(FORGET_NETWORK, netId, putListener(listener));
    }

    private protected void disable(int netId, ActionListener listener) {
        if (netId < 0) {
            throw new IllegalArgumentException("Network id cannot be negative");
        }
        getChannel().sendMessage(DISABLE_NETWORK, netId, putListener(listener));
    }

    public synchronized void disableEphemeralNetwork(String SSID) {
        if (SSID == null) {
            throw new IllegalArgumentException("SSID cannot be null");
        }
        try {
            this.mService.disableEphemeralNetwork(SSID, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void startWps(WpsInfo config, WpsCallback listener) {
        if (listener != null) {
            listener.onFailed(0);
        }
    }

    public void cancelWps(WpsCallback listener) {
        if (listener != null) {
            listener.onFailed(0);
        }
    }

    private protected Messenger getWifiServiceMessenger() {
        try {
            return this.mService.getWifiServiceMessenger(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* loaded from: classes2.dex */
    public class WifiLock {
        private final IBinder mBinder;
        private boolean mHeld;
        int mLockType;
        private int mRefCount;
        private boolean mRefCounted;
        private String mTag;
        private WorkSource mWorkSource;

        private WifiLock(int lockType, String tag) {
            this.mTag = tag;
            this.mLockType = lockType;
            this.mBinder = new Binder();
            this.mRefCount = 0;
            this.mRefCounted = true;
            this.mHeld = false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0012, code lost:
            if (r7.mHeld == false) goto L9;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void acquire() {
            /*
                r7 = this;
                android.os.IBinder r0 = r7.mBinder
                monitor-enter(r0)
                boolean r1 = r7.mRefCounted     // Catch: java.lang.Throwable -> L56
                r2 = 1
                if (r1 == 0) goto L10
                int r1 = r7.mRefCount     // Catch: java.lang.Throwable -> L56
                int r1 = r1 + r2
                r7.mRefCount = r1     // Catch: java.lang.Throwable -> L56
                if (r1 != r2) goto L54
                goto L14
            L10:
                boolean r1 = r7.mHeld     // Catch: java.lang.Throwable -> L56
                if (r1 != 0) goto L54
            L14:
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch: android.os.RemoteException -> L4e java.lang.Throwable -> L56
                android.net.wifi.IWifiManager r1 = r1.mService     // Catch: android.os.RemoteException -> L4e java.lang.Throwable -> L56
                android.os.IBinder r3 = r7.mBinder     // Catch: android.os.RemoteException -> L4e java.lang.Throwable -> L56
                int r4 = r7.mLockType     // Catch: android.os.RemoteException -> L4e java.lang.Throwable -> L56
                java.lang.String r5 = r7.mTag     // Catch: android.os.RemoteException -> L4e java.lang.Throwable -> L56
                android.os.WorkSource r6 = r7.mWorkSource     // Catch: android.os.RemoteException -> L4e java.lang.Throwable -> L56
                r1.acquireWifiLock(r3, r4, r5, r6)     // Catch: android.os.RemoteException -> L4e java.lang.Throwable -> L56
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch: android.os.RemoteException -> L4e java.lang.Throwable -> L56
                monitor-enter(r1)     // Catch: android.os.RemoteException -> L4e java.lang.Throwable -> L56
                android.net.wifi.WifiManager r3 = android.net.wifi.WifiManager.this     // Catch: java.lang.Throwable -> L4b
                int r3 = android.net.wifi.WifiManager.access$700(r3)     // Catch: java.lang.Throwable -> L4b
                r4 = 50
                if (r3 >= r4) goto L3a
                android.net.wifi.WifiManager r3 = android.net.wifi.WifiManager.this     // Catch: java.lang.Throwable -> L4b
                android.net.wifi.WifiManager.access$708(r3)     // Catch: java.lang.Throwable -> L4b
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L4b
                r7.mHeld = r2     // Catch: java.lang.Throwable -> L56
                goto L54
            L3a:
                android.net.wifi.WifiManager r2 = android.net.wifi.WifiManager.this     // Catch: java.lang.Throwable -> L4b
                android.net.wifi.IWifiManager r2 = r2.mService     // Catch: java.lang.Throwable -> L4b
                android.os.IBinder r3 = r7.mBinder     // Catch: java.lang.Throwable -> L4b
                r2.releaseWifiLock(r3)     // Catch: java.lang.Throwable -> L4b
                java.lang.UnsupportedOperationException r2 = new java.lang.UnsupportedOperationException     // Catch: java.lang.Throwable -> L4b
                java.lang.String r3 = "Exceeded maximum number of wifi locks"
                r2.<init>(r3)     // Catch: java.lang.Throwable -> L4b
                throw r2     // Catch: java.lang.Throwable -> L4b
            L4b:
                r2 = move-exception
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L4b
                throw r2     // Catch: android.os.RemoteException -> L4e java.lang.Throwable -> L56
            L4e:
                r1 = move-exception
                java.lang.RuntimeException r2 = r1.rethrowFromSystemServer()     // Catch: java.lang.Throwable -> L56
                throw r2     // Catch: java.lang.Throwable -> L56
            L54:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L56
                return
            L56:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L56
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.WifiLock.acquire():void");
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0012, code lost:
            if (r4.mHeld != false) goto L9;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void release() {
            /*
                r4 = this;
                android.os.IBinder r0 = r4.mBinder
                monitor-enter(r0)
                boolean r1 = r4.mRefCounted     // Catch: java.lang.Throwable -> L53
                if (r1 == 0) goto L10
                int r1 = r4.mRefCount     // Catch: java.lang.Throwable -> L53
                int r1 = r1 + (-1)
                r4.mRefCount = r1     // Catch: java.lang.Throwable -> L53
                if (r1 != 0) goto L34
                goto L14
            L10:
                boolean r1 = r4.mHeld     // Catch: java.lang.Throwable -> L53
                if (r1 == 0) goto L34
            L14:
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch: android.os.RemoteException -> L2e java.lang.Throwable -> L53
                android.net.wifi.IWifiManager r1 = r1.mService     // Catch: android.os.RemoteException -> L2e java.lang.Throwable -> L53
                android.os.IBinder r2 = r4.mBinder     // Catch: android.os.RemoteException -> L2e java.lang.Throwable -> L53
                r1.releaseWifiLock(r2)     // Catch: android.os.RemoteException -> L2e java.lang.Throwable -> L53
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch: android.os.RemoteException -> L2e java.lang.Throwable -> L53
                monitor-enter(r1)     // Catch: android.os.RemoteException -> L2e java.lang.Throwable -> L53
                android.net.wifi.WifiManager r2 = android.net.wifi.WifiManager.this     // Catch: java.lang.Throwable -> L2b
                android.net.wifi.WifiManager.access$710(r2)     // Catch: java.lang.Throwable -> L2b
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L2b
                r1 = 0
                r4.mHeld = r1     // Catch: java.lang.Throwable -> L53
                goto L34
            L2b:
                r2 = move-exception
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L2b
                throw r2     // Catch: android.os.RemoteException -> L2e java.lang.Throwable -> L53
            L2e:
                r1 = move-exception
                java.lang.RuntimeException r2 = r1.rethrowFromSystemServer()     // Catch: java.lang.Throwable -> L53
                throw r2     // Catch: java.lang.Throwable -> L53
            L34:
                int r1 = r4.mRefCount     // Catch: java.lang.Throwable -> L53
                if (r1 < 0) goto L3a
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L53
                return
            L3a:
                java.lang.RuntimeException r1 = new java.lang.RuntimeException     // Catch: java.lang.Throwable -> L53
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L53
                r2.<init>()     // Catch: java.lang.Throwable -> L53
                java.lang.String r3 = "WifiLock under-locked "
                r2.append(r3)     // Catch: java.lang.Throwable -> L53
                java.lang.String r3 = r4.mTag     // Catch: java.lang.Throwable -> L53
                r2.append(r3)     // Catch: java.lang.Throwable -> L53
                java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L53
                r1.<init>(r2)     // Catch: java.lang.Throwable -> L53
                throw r1     // Catch: java.lang.Throwable -> L53
            L53:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L53
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.WifiLock.release():void");
        }

        public void setReferenceCounted(boolean refCounted) {
            this.mRefCounted = refCounted;
        }

        public boolean isHeld() {
            boolean z;
            synchronized (this.mBinder) {
                z = this.mHeld;
            }
            return z;
        }

        public void setWorkSource(WorkSource ws) {
            synchronized (this.mBinder) {
                if (ws != null) {
                    try {
                        if (ws.isEmpty()) {
                            ws = null;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                boolean changed = true;
                if (ws == null) {
                    this.mWorkSource = null;
                } else {
                    ws.clearNames();
                    boolean z = true;
                    if (this.mWorkSource == null) {
                        if (this.mWorkSource == null) {
                            z = false;
                        }
                        changed = z;
                        this.mWorkSource = new WorkSource(ws);
                    } else {
                        changed = !this.mWorkSource.equals(ws);
                        if (changed) {
                            this.mWorkSource.set(ws);
                        }
                    }
                }
                if (changed && this.mHeld) {
                    try {
                        WifiManager.this.mService.updateWifiLockWorkSource(this.mBinder, this.mWorkSource);
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            }
        }

        public String toString() {
            String s3;
            String str;
            synchronized (this.mBinder) {
                String s1 = Integer.toHexString(System.identityHashCode(this));
                String s2 = this.mHeld ? "held; " : "";
                if (this.mRefCounted) {
                    s3 = "refcounted: refcount = " + this.mRefCount;
                } else {
                    s3 = "not refcounted";
                }
                str = "WifiLock{ " + s1 + "; " + s2 + s3 + " }";
            }
            return str;
        }

        protected void finalize() throws Throwable {
            super.finalize();
            synchronized (this.mBinder) {
                if (this.mHeld) {
                    try {
                        WifiManager.this.mService.releaseWifiLock(this.mBinder);
                        synchronized (WifiManager.this) {
                            WifiManager.access$710(WifiManager.this);
                        }
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            }
        }
    }

    public WifiLock createWifiLock(int lockType, String tag) {
        return new WifiLock(lockType, tag);
    }

    public WifiLock createWifiLock(String tag) {
        return new WifiLock(1, tag);
    }

    public MulticastLock createMulticastLock(String tag) {
        return new MulticastLock(tag);
    }

    /* loaded from: classes2.dex */
    public class MulticastLock {
        private final IBinder mBinder;
        private boolean mHeld;
        private int mRefCount;
        private boolean mRefCounted;
        private String mTag;

        private MulticastLock(String tag) {
            this.mTag = tag;
            this.mBinder = new Binder();
            this.mRefCount = 0;
            this.mRefCounted = true;
            this.mHeld = false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0012, code lost:
            if (r5.mHeld == false) goto L9;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void acquire() {
            /*
                r5 = this;
                android.os.IBinder r0 = r5.mBinder
                monitor-enter(r0)
                boolean r1 = r5.mRefCounted     // Catch: java.lang.Throwable -> L50
                r2 = 1
                if (r1 == 0) goto L10
                int r1 = r5.mRefCount     // Catch: java.lang.Throwable -> L50
                int r1 = r1 + r2
                r5.mRefCount = r1     // Catch: java.lang.Throwable -> L50
                if (r1 != r2) goto L4e
                goto L14
            L10:
                boolean r1 = r5.mHeld     // Catch: java.lang.Throwable -> L50
                if (r1 != 0) goto L4e
            L14:
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L50
                android.net.wifi.IWifiManager r1 = r1.mService     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L50
                android.os.IBinder r3 = r5.mBinder     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L50
                java.lang.String r4 = r5.mTag     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L50
                r1.acquireMulticastLock(r3, r4)     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L50
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L50
                monitor-enter(r1)     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L50
                android.net.wifi.WifiManager r3 = android.net.wifi.WifiManager.this     // Catch: java.lang.Throwable -> L45
                int r3 = android.net.wifi.WifiManager.access$700(r3)     // Catch: java.lang.Throwable -> L45
                r4 = 50
                if (r3 >= r4) goto L36
                android.net.wifi.WifiManager r3 = android.net.wifi.WifiManager.this     // Catch: java.lang.Throwable -> L45
                android.net.wifi.WifiManager.access$708(r3)     // Catch: java.lang.Throwable -> L45
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L45
                r5.mHeld = r2     // Catch: java.lang.Throwable -> L50
                goto L4e
            L36:
                android.net.wifi.WifiManager r2 = android.net.wifi.WifiManager.this     // Catch: java.lang.Throwable -> L45
                android.net.wifi.IWifiManager r2 = r2.mService     // Catch: java.lang.Throwable -> L45
                r2.releaseMulticastLock()     // Catch: java.lang.Throwable -> L45
                java.lang.UnsupportedOperationException r2 = new java.lang.UnsupportedOperationException     // Catch: java.lang.Throwable -> L45
                java.lang.String r3 = "Exceeded maximum number of wifi locks"
                r2.<init>(r3)     // Catch: java.lang.Throwable -> L45
                throw r2     // Catch: java.lang.Throwable -> L45
            L45:
                r2 = move-exception
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L45
                throw r2     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L50
            L48:
                r1 = move-exception
                java.lang.RuntimeException r2 = r1.rethrowFromSystemServer()     // Catch: java.lang.Throwable -> L50
                throw r2     // Catch: java.lang.Throwable -> L50
            L4e:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L50
                return
            L50:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L50
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.MulticastLock.acquire():void");
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0012, code lost:
            if (r4.mHeld != false) goto L9;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void release() {
            /*
                r4 = this;
                android.os.IBinder r0 = r4.mBinder
                monitor-enter(r0)
                boolean r1 = r4.mRefCounted     // Catch: java.lang.Throwable -> L51
                if (r1 == 0) goto L10
                int r1 = r4.mRefCount     // Catch: java.lang.Throwable -> L51
                int r1 = r1 + (-1)
                r4.mRefCount = r1     // Catch: java.lang.Throwable -> L51
                if (r1 != 0) goto L32
                goto L14
            L10:
                boolean r1 = r4.mHeld     // Catch: java.lang.Throwable -> L51
                if (r1 == 0) goto L32
            L14:
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch: android.os.RemoteException -> L2c java.lang.Throwable -> L51
                android.net.wifi.IWifiManager r1 = r1.mService     // Catch: android.os.RemoteException -> L2c java.lang.Throwable -> L51
                r1.releaseMulticastLock()     // Catch: android.os.RemoteException -> L2c java.lang.Throwable -> L51
                android.net.wifi.WifiManager r1 = android.net.wifi.WifiManager.this     // Catch: android.os.RemoteException -> L2c java.lang.Throwable -> L51
                monitor-enter(r1)     // Catch: android.os.RemoteException -> L2c java.lang.Throwable -> L51
                android.net.wifi.WifiManager r2 = android.net.wifi.WifiManager.this     // Catch: java.lang.Throwable -> L29
                android.net.wifi.WifiManager.access$710(r2)     // Catch: java.lang.Throwable -> L29
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L29
                r1 = 0
                r4.mHeld = r1     // Catch: java.lang.Throwable -> L51
                goto L32
            L29:
                r2 = move-exception
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L29
                throw r2     // Catch: android.os.RemoteException -> L2c java.lang.Throwable -> L51
            L2c:
                r1 = move-exception
                java.lang.RuntimeException r2 = r1.rethrowFromSystemServer()     // Catch: java.lang.Throwable -> L51
                throw r2     // Catch: java.lang.Throwable -> L51
            L32:
                int r1 = r4.mRefCount     // Catch: java.lang.Throwable -> L51
                if (r1 < 0) goto L38
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L51
                return
            L38:
                java.lang.RuntimeException r1 = new java.lang.RuntimeException     // Catch: java.lang.Throwable -> L51
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L51
                r2.<init>()     // Catch: java.lang.Throwable -> L51
                java.lang.String r3 = "MulticastLock under-locked "
                r2.append(r3)     // Catch: java.lang.Throwable -> L51
                java.lang.String r3 = r4.mTag     // Catch: java.lang.Throwable -> L51
                r2.append(r3)     // Catch: java.lang.Throwable -> L51
                java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L51
                r1.<init>(r2)     // Catch: java.lang.Throwable -> L51
                throw r1     // Catch: java.lang.Throwable -> L51
            L51:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L51
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.MulticastLock.release():void");
        }

        public void setReferenceCounted(boolean refCounted) {
            this.mRefCounted = refCounted;
        }

        public boolean isHeld() {
            boolean z;
            synchronized (this.mBinder) {
                z = this.mHeld;
            }
            return z;
        }

        public String toString() {
            String s3;
            String str;
            synchronized (this.mBinder) {
                String s1 = Integer.toHexString(System.identityHashCode(this));
                String s2 = this.mHeld ? "held; " : "";
                if (this.mRefCounted) {
                    s3 = "refcounted: refcount = " + this.mRefCount;
                } else {
                    s3 = "not refcounted";
                }
                str = "MulticastLock{ " + s1 + "; " + s2 + s3 + " }";
            }
            return str;
        }

        protected void finalize() throws Throwable {
            super.finalize();
            setReferenceCounted(false);
            release();
        }
    }

    public synchronized boolean isMulticastEnabled() {
        try {
            return this.mService.isMulticastEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected boolean initializeMulticastFiltering() {
        try {
            this.mService.initializeMulticastFiltering();
            return true;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mAsyncChannel != null) {
                this.mAsyncChannel.disconnect();
            }
        } finally {
            super.finalize();
        }
    }

    private protected void enableVerboseLogging(int verbose) {
        try {
            this.mService.enableVerboseLogging(verbose);
        } catch (Exception e) {
            Log.e(TAG, "enableVerboseLogging " + e.toString());
        }
    }

    private protected int getVerboseLoggingLevel() {
        try {
            return this.mService.getVerboseLoggingLevel();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void factoryReset() {
        try {
            this.mService.factoryReset(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected Network getCurrentNetwork() {
        try {
            return this.mService.getCurrentNetwork();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized boolean setEnableAutoJoinWhenAssociated(boolean enabled) {
        return false;
    }

    public synchronized boolean getEnableAutoJoinWhenAssociated() {
        return false;
    }

    public synchronized void enableWifiConnectivityManager(boolean enabled) {
        try {
            this.mService.enableWifiConnectivityManager(enabled);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized byte[] retrieveBackupData() {
        try {
            return this.mService.retrieveBackupData();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void restoreBackupData(byte[] data) {
        try {
            this.mService.restoreBackupData(data);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public synchronized void restoreSupplicantBackupData(byte[] supplicantData, byte[] ipConfigData) {
        try {
            this.mService.restoreSupplicantBackupData(supplicantData, ipConfigData);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void startSubscriptionProvisioning(OsuProvider provider, ProvisioningCallback callback, Handler handler) {
        Looper looper = handler == null ? Looper.getMainLooper() : handler.getLooper();
        try {
            this.mService.startSubscriptionProvisioning(provider, new ProvisioningCallbackProxy(looper, callback));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class ProvisioningCallbackProxy extends IProvisioningCallback.Stub {
        private final ProvisioningCallback mCallback;
        private final Handler mHandler;

        synchronized ProvisioningCallbackProxy(Looper looper, ProvisioningCallback callback) {
            this.mHandler = new Handler(looper);
            this.mCallback = callback;
        }

        @Override // android.net.wifi.hotspot2.IProvisioningCallback
        public synchronized void onProvisioningStatus(final int status) {
            this.mHandler.post(new Runnable() { // from class: android.net.wifi.-$$Lambda$WifiManager$ProvisioningCallbackProxy$0_NXiwyrbrT_579x-6QMO0y3rzc
                @Override // java.lang.Runnable
                public final void run() {
                    WifiManager.ProvisioningCallbackProxy.this.mCallback.onProvisioningStatus(status);
                }
            });
        }

        @Override // android.net.wifi.hotspot2.IProvisioningCallback
        public synchronized void onProvisioningFailure(final int status) {
            this.mHandler.post(new Runnable() { // from class: android.net.wifi.-$$Lambda$WifiManager$ProvisioningCallbackProxy$rgPeSRj_1qriYZtaCu57EZHtc_Q
                @Override // java.lang.Runnable
                public final void run() {
                    WifiManager.ProvisioningCallbackProxy.this.mCallback.onProvisioningFailure(status);
                }
            });
        }
    }

    public String getCapabilities(String capaType) {
        try {
            return this.mService.getCapabilities(capaType);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppAddBootstrapQrCode(String uri) {
        try {
            return this.mService.dppAddBootstrapQrCode(uri);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppBootstrapGenerate(WifiDppConfig config) {
        try {
            return this.mService.dppBootstrapGenerate(config);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String dppGetUri(int bootstrap_id) {
        try {
            return this.mService.dppGetUri(bootstrap_id);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppBootstrapRemove(int bootstrap_id) {
        try {
            return this.mService.dppBootstrapRemove(bootstrap_id);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppListen(String frequency, int dpp_role, boolean qr_mutual, boolean netrole_ap) {
        try {
            return this.mService.dppListen(frequency, dpp_role, qr_mutual, netrole_ap);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void dppStopListen() {
        try {
            this.mService.dppStopListen();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppConfiguratorAdd(String curve, String key, int expiry) {
        try {
            return this.mService.dppConfiguratorAdd(curve, key, expiry);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppConfiguratorRemove(int config_id) {
        try {
            return this.mService.dppConfiguratorRemove(config_id);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppStartAuth(WifiDppConfig config) {
        try {
            return this.mService.dppStartAuth(config);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String dppConfiguratorGetKey(int id) {
        try {
            return this.mService.dppConfiguratorGetKey(id);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
