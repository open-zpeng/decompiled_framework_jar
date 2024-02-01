package android.net;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ResultReceiver;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.internal.net.VpnProfile;
/* loaded from: classes2.dex */
public interface IConnectivityManager extends IInterface {
    synchronized boolean addVpnAddress(String str, int i) throws RemoteException;

    synchronized int checkMobileProvisioning(int i) throws RemoteException;

    synchronized ParcelFileDescriptor establishVpn(VpnConfig vpnConfig) throws RemoteException;

    synchronized void factoryReset() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    LinkProperties getActiveLinkProperties() throws RemoteException;

    synchronized Network getActiveNetwork() throws RemoteException;

    synchronized Network getActiveNetworkForUid(int i, boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    NetworkInfo getActiveNetworkInfo() throws RemoteException;

    synchronized NetworkInfo getActiveNetworkInfoForUid(int i, boolean z) throws RemoteException;

    synchronized NetworkQuotaInfo getActiveNetworkQuotaInfo() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    NetworkInfo[] getAllNetworkInfo() throws RemoteException;

    private protected NetworkState[] getAllNetworkState() throws RemoteException;

    synchronized Network[] getAllNetworks() throws RemoteException;

    synchronized String getAlwaysOnVpnPackage(int i) throws RemoteException;

    synchronized String getCaptivePortalServerUrl() throws RemoteException;

    synchronized NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int i) throws RemoteException;

    synchronized ProxyInfo getGlobalProxy() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getLastTetherError(String str) throws RemoteException;

    synchronized LegacyVpnInfo getLegacyVpnInfo(int i) throws RemoteException;

    synchronized LinkProperties getLinkProperties(Network network) throws RemoteException;

    synchronized LinkProperties getLinkPropertiesForType(int i) throws RemoteException;

    synchronized String getMobileProvisioningUrl() throws RemoteException;

    synchronized int getMultipathPreference(Network network) throws RemoteException;

    synchronized NetworkCapabilities getNetworkCapabilities(Network network) throws RemoteException;

    synchronized Network getNetworkForType(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    NetworkInfo getNetworkInfo(int i) throws RemoteException;

    synchronized NetworkInfo getNetworkInfoForUid(Network network, int i, boolean z) throws RemoteException;

    synchronized byte[] getNetworkWatchlistConfigHash() throws RemoteException;

    synchronized ProxyInfo getProxyForNetwork(Network network) throws RemoteException;

    synchronized int getRestoreDefaultNetworkDelay(int i) throws RemoteException;

    synchronized String[] getTetherableBluetoothRegexs() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String[] getTetherableIfaces() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String[] getTetherableUsbRegexs() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String[] getTetherableWifiRegexs() throws RemoteException;

    synchronized String[] getTetheredDhcpRanges() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String[] getTetheredIfaces() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String[] getTetheringErroredIfaces() throws RemoteException;

    synchronized VpnConfig getVpnConfig(int i) throws RemoteException;

    synchronized boolean isActiveNetworkMetered() throws RemoteException;

    synchronized boolean isAlwaysOnVpnPackageSupported(int i, String str) throws RemoteException;

    synchronized boolean isNetworkSupported(int i) throws RemoteException;

    synchronized boolean isTetheringSupported(String str) throws RemoteException;

    synchronized NetworkRequest listenForNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, IBinder iBinder) throws RemoteException;

    synchronized void pendingListenForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent) throws RemoteException;

    synchronized NetworkRequest pendingRequestForNetwork(NetworkCapabilities networkCapabilities, PendingIntent pendingIntent) throws RemoteException;

    synchronized boolean prepareVpn(String str, String str2, int i) throws RemoteException;

    synchronized int registerNetworkAgent(Messenger messenger, NetworkInfo networkInfo, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int i, NetworkMisc networkMisc) throws RemoteException;

    synchronized void registerNetworkFactory(Messenger messenger, String str) throws RemoteException;

    synchronized void releaseNetworkRequest(NetworkRequest networkRequest) throws RemoteException;

    synchronized void releasePendingNetworkRequest(PendingIntent pendingIntent) throws RemoteException;

    synchronized boolean removeVpnAddress(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void reportInetCondition(int i, int i2) throws RemoteException;

    synchronized void reportNetworkConnectivity(Network network, boolean z) throws RemoteException;

    synchronized boolean requestBandwidthUpdate(Network network) throws RemoteException;

    synchronized NetworkRequest requestNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, int i, IBinder iBinder, int i2) throws RemoteException;

    synchronized boolean requestRouteToHostAddress(int i, byte[] bArr) throws RemoteException;

    synchronized void setAcceptUnvalidated(Network network, boolean z, boolean z2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setAirplaneMode(boolean z) throws RemoteException;

    synchronized boolean setAlwaysOnVpnPackage(int i, String str, boolean z) throws RemoteException;

    synchronized void setAvoidUnvalidated(Network network) throws RemoteException;

    synchronized void setGlobalProxy(ProxyInfo proxyInfo) throws RemoteException;

    synchronized void setProvisioningNotificationVisible(boolean z, int i, String str) throws RemoteException;

    synchronized boolean setUnderlyingNetworksForVpn(Network[] networkArr) throws RemoteException;

    synchronized int setUsbTethering(boolean z, String str) throws RemoteException;

    synchronized void setVpnPackageAuthorization(String str, int i, boolean z) throws RemoteException;

    synchronized void startCaptivePortalApp(Network network) throws RemoteException;

    private protected void startLegacyVpn(VpnProfile vpnProfile) throws RemoteException;

    synchronized void startNattKeepalive(Network network, int i, Messenger messenger, IBinder iBinder, String str, int i2, String str2) throws RemoteException;

    synchronized void startTethering(int i, ResultReceiver resultReceiver, boolean z, String str) throws RemoteException;

    synchronized void stopKeepalive(Network network, int i) throws RemoteException;

    synchronized void stopTethering(int i, String str) throws RemoteException;

    synchronized int tether(String str, String str2) throws RemoteException;

    synchronized void unregisterNetworkFactory(Messenger messenger) throws RemoteException;

    synchronized int untether(String str, String str2) throws RemoteException;

    synchronized boolean updateLockdownVpn() throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IConnectivityManager {
        private static final String DESCRIPTOR = "android.net.IConnectivityManager";
        static final int TRANSACTION_addVpnAddress = 68;
        static final int TRANSACTION_checkMobileProvisioning = 49;
        static final int TRANSACTION_establishVpn = 41;
        static final int TRANSACTION_factoryReset = 71;
        static final int TRANSACTION_getActiveLinkProperties = 12;
        static final int TRANSACTION_getActiveNetwork = 1;
        static final int TRANSACTION_getActiveNetworkForUid = 2;
        static final int TRANSACTION_getActiveNetworkInfo = 3;
        static final int TRANSACTION_getActiveNetworkInfoForUid = 4;
        static final int TRANSACTION_getActiveNetworkQuotaInfo = 17;
        static final int TRANSACTION_getAllNetworkInfo = 7;
        static final int TRANSACTION_getAllNetworkState = 16;
        static final int TRANSACTION_getAllNetworks = 9;
        static final int TRANSACTION_getAlwaysOnVpnPackage = 48;
        static final int TRANSACTION_getCaptivePortalServerUrl = 74;
        static final int TRANSACTION_getDefaultNetworkCapabilitiesForUser = 10;
        static final int TRANSACTION_getGlobalProxy = 36;
        static final int TRANSACTION_getLastTetherError = 22;
        static final int TRANSACTION_getLegacyVpnInfo = 44;
        static final int TRANSACTION_getLinkProperties = 14;
        static final int TRANSACTION_getLinkPropertiesForType = 13;
        static final int TRANSACTION_getMobileProvisioningUrl = 50;
        static final int TRANSACTION_getMultipathPreference = 66;
        static final int TRANSACTION_getNetworkCapabilities = 15;
        static final int TRANSACTION_getNetworkForType = 8;
        static final int TRANSACTION_getNetworkInfo = 5;
        static final int TRANSACTION_getNetworkInfoForUid = 6;
        static final int TRANSACTION_getNetworkWatchlistConfigHash = 75;
        static final int TRANSACTION_getProxyForNetwork = 38;
        static final int TRANSACTION_getRestoreDefaultNetworkDelay = 67;
        static final int TRANSACTION_getTetherableBluetoothRegexs = 32;
        static final int TRANSACTION_getTetherableIfaces = 26;
        static final int TRANSACTION_getTetherableUsbRegexs = 30;
        static final int TRANSACTION_getTetherableWifiRegexs = 31;
        static final int TRANSACTION_getTetheredDhcpRanges = 29;
        static final int TRANSACTION_getTetheredIfaces = 27;
        static final int TRANSACTION_getTetheringErroredIfaces = 28;
        static final int TRANSACTION_getVpnConfig = 42;
        static final int TRANSACTION_isActiveNetworkMetered = 18;
        static final int TRANSACTION_isAlwaysOnVpnPackageSupported = 46;
        static final int TRANSACTION_isNetworkSupported = 11;
        static final int TRANSACTION_isTetheringSupported = 23;
        static final int TRANSACTION_listenForNetwork = 60;
        static final int TRANSACTION_pendingListenForNetwork = 61;
        static final int TRANSACTION_pendingRequestForNetwork = 58;
        static final int TRANSACTION_prepareVpn = 39;
        static final int TRANSACTION_registerNetworkAgent = 56;
        static final int TRANSACTION_registerNetworkFactory = 53;
        static final int TRANSACTION_releaseNetworkRequest = 62;
        static final int TRANSACTION_releasePendingNetworkRequest = 59;
        static final int TRANSACTION_removeVpnAddress = 69;
        static final int TRANSACTION_reportInetCondition = 34;
        static final int TRANSACTION_reportNetworkConnectivity = 35;
        static final int TRANSACTION_requestBandwidthUpdate = 54;
        static final int TRANSACTION_requestNetwork = 57;
        static final int TRANSACTION_requestRouteToHostAddress = 19;
        static final int TRANSACTION_setAcceptUnvalidated = 63;
        static final int TRANSACTION_setAirplaneMode = 52;
        static final int TRANSACTION_setAlwaysOnVpnPackage = 47;
        static final int TRANSACTION_setAvoidUnvalidated = 64;
        static final int TRANSACTION_setGlobalProxy = 37;
        static final int TRANSACTION_setProvisioningNotificationVisible = 51;
        static final int TRANSACTION_setUnderlyingNetworksForVpn = 70;
        static final int TRANSACTION_setUsbTethering = 33;
        static final int TRANSACTION_setVpnPackageAuthorization = 40;
        static final int TRANSACTION_startCaptivePortalApp = 65;
        static final int TRANSACTION_startLegacyVpn = 43;
        static final int TRANSACTION_startNattKeepalive = 72;
        static final int TRANSACTION_startTethering = 24;
        static final int TRANSACTION_stopKeepalive = 73;
        static final int TRANSACTION_stopTethering = 25;
        static final int TRANSACTION_tether = 20;
        static final int TRANSACTION_unregisterNetworkFactory = 55;
        static final int TRANSACTION_untether = 21;
        static final int TRANSACTION_updateLockdownVpn = 45;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IConnectivityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IConnectivityManager)) {
                return (IConnectivityManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Messenger _arg0;
            NetworkInfo _arg1;
            LinkProperties _arg2;
            NetworkCapabilities _arg3;
            NetworkCapabilities _arg02;
            NetworkCapabilities _arg03;
            NetworkCapabilities _arg04;
            NetworkCapabilities _arg05;
            Network _arg06;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    Network _result = getActiveNetwork();
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    boolean _arg12 = data.readInt() != 0;
                    Network _result2 = getActiveNetworkForUid(_arg07, _arg12);
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
                    NetworkInfo _result3 = getActiveNetworkInfo();
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    boolean _arg13 = data.readInt() != 0;
                    NetworkInfo _result4 = getActiveNetworkInfoForUid(_arg08, _arg13);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    NetworkInfo _result5 = getNetworkInfo(_arg09);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    Network _arg010 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    int _arg14 = data.readInt();
                    boolean _arg22 = data.readInt() != 0;
                    NetworkInfo _result6 = getNetworkInfoForUid(_arg010, _arg14, _arg22);
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    NetworkInfo[] _result7 = getAllNetworkInfo();
                    reply.writeNoException();
                    reply.writeTypedArray(_result7, 1);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    Network _result8 = getNetworkForType(_arg011);
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    Network[] _result9 = getAllNetworks();
                    reply.writeNoException();
                    reply.writeTypedArray(_result9, 1);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    NetworkCapabilities[] _result10 = getDefaultNetworkCapabilitiesForUser(_arg012);
                    reply.writeNoException();
                    reply.writeTypedArray(_result10, 1);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    boolean isNetworkSupported = isNetworkSupported(_arg013);
                    reply.writeNoException();
                    reply.writeInt(isNetworkSupported ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    LinkProperties _result11 = getActiveLinkProperties();
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    LinkProperties _result12 = getLinkPropertiesForType(_arg014);
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    Network _arg015 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    LinkProperties _result13 = getLinkProperties(_arg015);
                    reply.writeNoException();
                    if (_result13 != null) {
                        reply.writeInt(1);
                        _result13.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    Network _arg016 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    NetworkCapabilities _result14 = getNetworkCapabilities(_arg016);
                    reply.writeNoException();
                    if (_result14 != null) {
                        reply.writeInt(1);
                        _result14.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    NetworkState[] _result15 = getAllNetworkState();
                    reply.writeNoException();
                    reply.writeTypedArray(_result15, 1);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    NetworkQuotaInfo _result16 = getActiveNetworkQuotaInfo();
                    reply.writeNoException();
                    if (_result16 != null) {
                        reply.writeInt(1);
                        _result16.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isActiveNetworkMetered = isActiveNetworkMetered();
                    reply.writeNoException();
                    reply.writeInt(isActiveNetworkMetered ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    byte[] _arg15 = data.createByteArray();
                    boolean requestRouteToHostAddress = requestRouteToHostAddress(_arg017, _arg15);
                    reply.writeNoException();
                    reply.writeInt(requestRouteToHostAddress ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    String _arg16 = data.readString();
                    int _result17 = tether(_arg018, _arg16);
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    String _arg17 = data.readString();
                    int _result18 = untether(_arg019, _arg17);
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    int _result19 = getLastTetherError(_arg020);
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    boolean isTetheringSupported = isTetheringSupported(_arg021);
                    reply.writeNoException();
                    reply.writeInt(isTetheringSupported ? 1 : 0);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    ResultReceiver _arg18 = data.readInt() != 0 ? ResultReceiver.CREATOR.createFromParcel(data) : null;
                    boolean _arg23 = data.readInt() != 0;
                    String _arg32 = data.readString();
                    startTethering(_arg022, _arg18, _arg23, _arg32);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    String _arg19 = data.readString();
                    stopTethering(_arg023, _arg19);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result20 = getTetherableIfaces();
                    reply.writeNoException();
                    reply.writeStringArray(_result20);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result21 = getTetheredIfaces();
                    reply.writeNoException();
                    reply.writeStringArray(_result21);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result22 = getTetheringErroredIfaces();
                    reply.writeNoException();
                    reply.writeStringArray(_result22);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result23 = getTetheredDhcpRanges();
                    reply.writeNoException();
                    reply.writeStringArray(_result23);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result24 = getTetherableUsbRegexs();
                    reply.writeNoException();
                    reply.writeStringArray(_result24);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result25 = getTetherableWifiRegexs();
                    reply.writeNoException();
                    reply.writeStringArray(_result25);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result26 = getTetherableBluetoothRegexs();
                    reply.writeNoException();
                    reply.writeStringArray(_result26);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg024 = data.readInt() != 0;
                    String _arg110 = data.readString();
                    int _result27 = setUsbTethering(_arg024, _arg110);
                    reply.writeNoException();
                    reply.writeInt(_result27);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    int _arg111 = data.readInt();
                    reportInetCondition(_arg025, _arg111);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    Network _arg026 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    boolean _arg112 = data.readInt() != 0;
                    reportNetworkConnectivity(_arg026, _arg112);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    ProxyInfo _result28 = getGlobalProxy();
                    reply.writeNoException();
                    if (_result28 != null) {
                        reply.writeInt(1);
                        _result28.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    ProxyInfo _arg027 = data.readInt() != 0 ? ProxyInfo.CREATOR.createFromParcel(data) : null;
                    setGlobalProxy(_arg027);
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    Network _arg028 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    ProxyInfo _result29 = getProxyForNetwork(_arg028);
                    reply.writeNoException();
                    if (_result29 != null) {
                        reply.writeInt(1);
                        _result29.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg029 = data.readString();
                    String _arg113 = data.readString();
                    int _arg24 = data.readInt();
                    boolean prepareVpn = prepareVpn(_arg029, _arg113, _arg24);
                    reply.writeNoException();
                    reply.writeInt(prepareVpn ? 1 : 0);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg030 = data.readString();
                    int _arg114 = data.readInt();
                    boolean _arg25 = data.readInt() != 0;
                    setVpnPackageAuthorization(_arg030, _arg114, _arg25);
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    VpnConfig _arg031 = data.readInt() != 0 ? VpnConfig.CREATOR.createFromParcel(data) : null;
                    ParcelFileDescriptor _result30 = establishVpn(_arg031);
                    reply.writeNoException();
                    if (_result30 != null) {
                        reply.writeInt(1);
                        _result30.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg032 = data.readInt();
                    VpnConfig _result31 = getVpnConfig(_arg032);
                    reply.writeNoException();
                    if (_result31 != null) {
                        reply.writeInt(1);
                        _result31.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    VpnProfile _arg033 = data.readInt() != 0 ? VpnProfile.CREATOR.createFromParcel(data) : null;
                    startLegacyVpn(_arg033);
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg034 = data.readInt();
                    LegacyVpnInfo _result32 = getLegacyVpnInfo(_arg034);
                    reply.writeNoException();
                    if (_result32 != null) {
                        reply.writeInt(1);
                        _result32.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    boolean updateLockdownVpn = updateLockdownVpn();
                    reply.writeNoException();
                    reply.writeInt(updateLockdownVpn ? 1 : 0);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg035 = data.readInt();
                    String _arg115 = data.readString();
                    boolean isAlwaysOnVpnPackageSupported = isAlwaysOnVpnPackageSupported(_arg035, _arg115);
                    reply.writeNoException();
                    reply.writeInt(isAlwaysOnVpnPackageSupported ? 1 : 0);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    String _arg116 = data.readString();
                    boolean _arg26 = data.readInt() != 0;
                    boolean alwaysOnVpnPackage = setAlwaysOnVpnPackage(_arg036, _arg116, _arg26);
                    reply.writeNoException();
                    reply.writeInt(alwaysOnVpnPackage ? 1 : 0);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg037 = data.readInt();
                    String _result33 = getAlwaysOnVpnPackage(_arg037);
                    reply.writeNoException();
                    reply.writeString(_result33);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg038 = data.readInt();
                    int _result34 = checkMobileProvisioning(_arg038);
                    reply.writeNoException();
                    reply.writeInt(_result34);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    String _result35 = getMobileProvisioningUrl();
                    reply.writeNoException();
                    reply.writeString(_result35);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg039 = data.readInt() != 0;
                    int _arg117 = data.readInt();
                    String _arg27 = data.readString();
                    setProvisioningNotificationVisible(_arg039, _arg117, _arg27);
                    reply.writeNoException();
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg040 = data.readInt() != 0;
                    setAirplaneMode(_arg040);
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    Messenger _arg041 = data.readInt() != 0 ? Messenger.CREATOR.createFromParcel(data) : null;
                    String _arg118 = data.readString();
                    registerNetworkFactory(_arg041, _arg118);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    Network _arg042 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    boolean requestBandwidthUpdate = requestBandwidthUpdate(_arg042);
                    reply.writeNoException();
                    reply.writeInt(requestBandwidthUpdate ? 1 : 0);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    Messenger _arg043 = data.readInt() != 0 ? Messenger.CREATOR.createFromParcel(data) : null;
                    unregisterNetworkFactory(_arg043);
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Messenger.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = NetworkInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = LinkProperties.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg3 = NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    int _arg4 = data.readInt();
                    NetworkMisc _arg5 = data.readInt() != 0 ? NetworkMisc.CREATOR.createFromParcel(data) : null;
                    int _result36 = registerNetworkAgent(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    reply.writeInt(_result36);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    Messenger _arg119 = data.readInt() != 0 ? Messenger.CREATOR.createFromParcel(data) : null;
                    int _arg28 = data.readInt();
                    IBinder _arg33 = data.readStrongBinder();
                    int _arg42 = data.readInt();
                    NetworkRequest _result37 = requestNetwork(_arg02, _arg119, _arg28, _arg33, _arg42);
                    reply.writeNoException();
                    if (_result37 != null) {
                        reply.writeInt(1);
                        _result37.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    PendingIntent _arg120 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    NetworkRequest _result38 = pendingRequestForNetwork(_arg03, _arg120);
                    reply.writeNoException();
                    if (_result38 != null) {
                        reply.writeInt(1);
                        _result38.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    PendingIntent _arg044 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    releasePendingNetworkRequest(_arg044);
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    Messenger _arg121 = data.readInt() != 0 ? Messenger.CREATOR.createFromParcel(data) : null;
                    IBinder _arg29 = data.readStrongBinder();
                    NetworkRequest _result39 = listenForNetwork(_arg04, _arg121, _arg29);
                    reply.writeNoException();
                    if (_result39 != null) {
                        reply.writeInt(1);
                        _result39.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    PendingIntent _arg122 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    pendingListenForNetwork(_arg05, _arg122);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    NetworkRequest _arg045 = data.readInt() != 0 ? NetworkRequest.CREATOR.createFromParcel(data) : null;
                    releaseNetworkRequest(_arg045);
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    Network _arg046 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    boolean _arg123 = data.readInt() != 0;
                    boolean _arg210 = data.readInt() != 0;
                    setAcceptUnvalidated(_arg046, _arg123, _arg210);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    Network _arg047 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    setAvoidUnvalidated(_arg047);
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    Network _arg048 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    startCaptivePortalApp(_arg048);
                    reply.writeNoException();
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    Network _arg049 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    int _result40 = getMultipathPreference(_arg049);
                    reply.writeNoException();
                    reply.writeInt(_result40);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg050 = data.readInt();
                    int _result41 = getRestoreDefaultNetworkDelay(_arg050);
                    reply.writeNoException();
                    reply.writeInt(_result41);
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg051 = data.readString();
                    int _arg124 = data.readInt();
                    boolean addVpnAddress = addVpnAddress(_arg051, _arg124);
                    reply.writeNoException();
                    reply.writeInt(addVpnAddress ? 1 : 0);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg052 = data.readString();
                    int _arg125 = data.readInt();
                    boolean removeVpnAddress = removeVpnAddress(_arg052, _arg125);
                    reply.writeNoException();
                    reply.writeInt(removeVpnAddress ? 1 : 0);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    Network[] _arg053 = (Network[]) data.createTypedArray(Network.CREATOR);
                    boolean underlyingNetworksForVpn = setUnderlyingNetworksForVpn(_arg053);
                    reply.writeNoException();
                    reply.writeInt(underlyingNetworksForVpn ? 1 : 0);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    factoryReset();
                    reply.writeNoException();
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = Network.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    int _arg126 = data.readInt();
                    Messenger _arg211 = data.readInt() != 0 ? Messenger.CREATOR.createFromParcel(data) : null;
                    IBinder _arg34 = data.readStrongBinder();
                    String _arg43 = data.readString();
                    int _arg52 = data.readInt();
                    String _arg6 = data.readString();
                    startNattKeepalive(_arg06, _arg126, _arg211, _arg34, _arg43, _arg52, _arg6);
                    reply.writeNoException();
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    Network _arg054 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    int _arg127 = data.readInt();
                    stopKeepalive(_arg054, _arg127);
                    reply.writeNoException();
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    String _result42 = getCaptivePortalServerUrl();
                    reply.writeNoException();
                    reply.writeString(_result42);
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _result43 = getNetworkWatchlistConfigHash();
                    reply.writeNoException();
                    reply.writeByteArray(_result43);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IConnectivityManager {
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

            @Override // android.net.IConnectivityManager
            public synchronized Network getActiveNetwork() throws RemoteException {
                Network _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
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

            @Override // android.net.IConnectivityManager
            public synchronized Network getActiveNetworkForUid(int uid, boolean ignoreBlocked) throws RemoteException {
                Network _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(ignoreBlocked ? 1 : 0);
                    this.mRemote.transact(2, _data, _reply, 0);
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

            private protected NetworkInfo getActiveNetworkInfo() throws RemoteException {
                NetworkInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NetworkInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked) throws RemoteException {
                NetworkInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(ignoreBlocked ? 1 : 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NetworkInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized NetworkInfo getNetworkInfo(int networkType) throws RemoteException {
                NetworkInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NetworkInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized NetworkInfo getNetworkInfoForUid(Network network, int uid, boolean ignoreBlocked) throws RemoteException {
                NetworkInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    _data.writeInt(ignoreBlocked ? 1 : 0);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NetworkInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected NetworkInfo[] getAllNetworkInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    NetworkInfo[] _result = (NetworkInfo[]) _reply.createTypedArray(NetworkInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized Network getNetworkForType(int networkType) throws RemoteException {
                Network _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    this.mRemote.transact(8, _data, _reply, 0);
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

            private protected Network[] getAllNetworks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    Network[] _result = (Network[]) _reply.createTypedArray(Network.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    NetworkCapabilities[] _result = (NetworkCapabilities[]) _reply.createTypedArray(NetworkCapabilities.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean isNetworkSupported(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected LinkProperties getActiveLinkProperties() throws RemoteException {
                LinkProperties _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = LinkProperties.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized LinkProperties getLinkPropertiesForType(int networkType) throws RemoteException {
                LinkProperties _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = LinkProperties.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized LinkProperties getLinkProperties(Network network) throws RemoteException {
                LinkProperties _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = LinkProperties.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized NetworkCapabilities getNetworkCapabilities(Network network) throws RemoteException {
                NetworkCapabilities _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NetworkCapabilities.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized NetworkState[] getAllNetworkState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    NetworkState[] _result = (NetworkState[]) _reply.createTypedArray(NetworkState.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized NetworkQuotaInfo getActiveNetworkQuotaInfo() throws RemoteException {
                NetworkQuotaInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NetworkQuotaInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean isActiveNetworkMetered() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean requestRouteToHostAddress(int networkType, byte[] hostAddress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeByteArray(hostAddress);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized int tether(String iface, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(callerPkg);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized int untether(String iface, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeString(callerPkg);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getLastTetherError(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean isTetheringSupported(String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void startTethering(int type, ResultReceiver receiver, boolean showProvisioningUi, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    if (receiver != null) {
                        _data.writeInt(1);
                        receiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(showProvisioningUi ? 1 : 0);
                    _data.writeString(callerPkg);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void stopTethering(int type, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callerPkg);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected String[] getTetherableIfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected String[] getTetheredIfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String[] getTetheringErroredIfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized String[] getTetheredDhcpRanges() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected String[] getTetherableUsbRegexs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String[] getTetherableWifiRegexs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized String[] getTetherableBluetoothRegexs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized int setUsbTethering(boolean enable, String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeString(callerPkg);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void reportInetCondition(int networkType, int percentage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeInt(percentage);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void reportNetworkConnectivity(Network network, boolean hasConnectivity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(hasConnectivity ? 1 : 0);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized ProxyInfo getGlobalProxy() throws RemoteException {
                ProxyInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ProxyInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void setGlobalProxy(ProxyInfo p) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (p != null) {
                        _data.writeInt(1);
                        p.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized ProxyInfo getProxyForNetwork(Network nework) throws RemoteException {
                ProxyInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (nework != null) {
                        _data.writeInt(1);
                        nework.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ProxyInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean prepareVpn(String oldPackage, String newPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(oldPackage);
                    _data.writeString(newPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void setVpnPackageAuthorization(String packageName, int userId, boolean authorized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(authorized ? 1 : 0);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized ParcelFileDescriptor establishVpn(VpnConfig config) throws RemoteException {
                ParcelFileDescriptor _result;
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
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized VpnConfig getVpnConfig(int userId) throws RemoteException {
                VpnConfig _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VpnConfig.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void startLegacyVpn(VpnProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized LegacyVpnInfo getLegacyVpnInfo(int userId) throws RemoteException {
                LegacyVpnInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = LegacyVpnInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean updateLockdownVpn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean isAlwaysOnVpnPackageSupported(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean setAlwaysOnVpnPackage(int userId, String packageName, boolean lockdown) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    _data.writeInt(lockdown ? 1 : 0);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized String getAlwaysOnVpnPackage(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized int checkMobileProvisioning(int suggestedTimeOutMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(suggestedTimeOutMs);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized String getMobileProvisioningUrl() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void setProvisioningNotificationVisible(boolean visible, int networkType, String action) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visible ? 1 : 0);
                    _data.writeInt(networkType);
                    _data.writeString(action);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setAirplaneMode(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void registerNetworkFactory(Messenger messenger, String name) throws RemoteException {
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
                    _data.writeString(name);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean requestBandwidthUpdate(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void unregisterNetworkFactory(Messenger messenger) throws RemoteException {
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
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized int registerNetworkAgent(Messenger messenger, NetworkInfo ni, LinkProperties lp, NetworkCapabilities nc, int score, NetworkMisc misc) throws RemoteException {
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
                    if (ni != null) {
                        _data.writeInt(1);
                        ni.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (lp != null) {
                        _data.writeInt(1);
                        lp.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (nc != null) {
                        _data.writeInt(1);
                        nc.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(score);
                    if (misc != null) {
                        _data.writeInt(1);
                        misc.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized NetworkRequest requestNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, int timeoutSec, IBinder binder, int legacy) throws RemoteException {
                NetworkRequest _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(timeoutSec);
                    _data.writeStrongBinder(binder);
                    _data.writeInt(legacy);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NetworkRequest.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized NetworkRequest pendingRequestForNetwork(NetworkCapabilities networkCapabilities, PendingIntent operation) throws RemoteException {
                NetworkRequest _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NetworkRequest.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void releasePendingNetworkRequest(PendingIntent operation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized NetworkRequest listenForNetwork(NetworkCapabilities networkCapabilities, Messenger messenger, IBinder binder) throws RemoteException {
                NetworkRequest _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
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
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NetworkRequest.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void pendingListenForNetwork(NetworkCapabilities networkCapabilities, PendingIntent operation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void releaseNetworkRequest(NetworkRequest networkRequest) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (networkRequest != null) {
                        _data.writeInt(1);
                        networkRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void setAcceptUnvalidated(Network network, boolean accept, boolean always) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(accept ? 1 : 0);
                    _data.writeInt(always ? 1 : 0);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void setAvoidUnvalidated(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void startCaptivePortalApp(Network network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized int getMultipathPreference(Network Network) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (Network != null) {
                        _data.writeInt(1);
                        Network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized int getRestoreDefaultNetworkDelay(int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean addVpnAddress(String address, int prefixLength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(prefixLength);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean removeVpnAddress(String address, int prefixLength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(prefixLength);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized boolean setUnderlyingNetworksForVpn(Network[] networks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(networks, 0);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void factoryReset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void startNattKeepalive(Network network, int intervalSeconds, Messenger messenger, IBinder binder, String srcAddr, int srcPort, String dstAddr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(intervalSeconds);
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    _data.writeString(srcAddr);
                    _data.writeInt(srcPort);
                    _data.writeString(dstAddr);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized void stopKeepalive(Network network, int slot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (network != null) {
                        _data.writeInt(1);
                        network.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(slot);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized String getCaptivePortalServerUrl() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IConnectivityManager
            public synchronized byte[] getNetworkWatchlistConfigHash() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
