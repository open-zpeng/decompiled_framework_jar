package android.bluetooth;

import android.bluetooth.IBluetoothCallback;
import android.bluetooth.IBluetoothMetadataListener;
import android.bluetooth.IBluetoothSocketManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.os.ResultReceiver;

/* loaded from: classes.dex */
public interface IBluetooth extends IInterface {
    boolean addOutOfBandBondDevice(BluetoothDevice bluetoothDevice, String str, int i, int i2) throws RemoteException;

    boolean cancelBondProcess(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean cancelDiscovery() throws RemoteException;

    boolean createBond(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    boolean createBondOutOfBand(BluetoothDevice bluetoothDevice, int i, OobData oobData) throws RemoteException;

    boolean disable() throws RemoteException;

    boolean enable() throws RemoteException;

    boolean enableNoAutoConnect() throws RemoteException;

    boolean factoryReset() throws RemoteException;

    boolean fetchRemoteUuids(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getAdapterConnectionState() throws RemoteException;

    String getAddress() throws RemoteException;

    int getBatteryLevel(BluetoothDevice bluetoothDevice) throws RemoteException;

    BluetoothClass getBluetoothClass() throws RemoteException;

    int getBondState(BluetoothDevice bluetoothDevice) throws RemoteException;

    BluetoothDevice[] getBondedDevices() throws RemoteException;

    int getConnectionState(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getDiscoverableTimeout() throws RemoteException;

    long getDiscoveryEndMillis() throws RemoteException;

    int getIoCapability() throws RemoteException;

    int getLeIoCapability() throws RemoteException;

    int getLeMaximumAdvertisingDataLength() throws RemoteException;

    void getLinkKey(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getMaxConnectedAudioDevices() throws RemoteException;

    int getMessageAccessPermission(BluetoothDevice bluetoothDevice) throws RemoteException;

    byte[] getMetadata(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    String getName() throws RemoteException;

    int getPhonebookAccessPermission(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getProfileConnectionState(int i) throws RemoteException;

    String getRemoteAlias(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getRemoteClass(BluetoothDevice bluetoothDevice) throws RemoteException;

    String getRemoteName(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getRemoteType(BluetoothDevice bluetoothDevice) throws RemoteException;

    ParcelUuid[] getRemoteUuids(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getScanMode() throws RemoteException;

    boolean getSilenceMode(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getSimAccessPermission(BluetoothDevice bluetoothDevice) throws RemoteException;

    IBluetoothSocketManager getSocketManager() throws RemoteException;

    int getState() throws RemoteException;

    long getSupportedProfiles() throws RemoteException;

    ParcelUuid[] getUuids() throws RemoteException;

    boolean isActivityAndEnergyReportingSupported() throws RemoteException;

    boolean isBondingInitiatedLocally(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean isDiscovering() throws RemoteException;

    boolean isEnabled() throws RemoteException;

    boolean isLe2MPhySupported() throws RemoteException;

    boolean isLeCodedPhySupported() throws RemoteException;

    boolean isLeExtendedAdvertisingSupported() throws RemoteException;

    boolean isLePeriodicAdvertisingSupported() throws RemoteException;

    boolean isMultiAdvertisementSupported() throws RemoteException;

    boolean isOffloadedFilteringSupported() throws RemoteException;

    boolean isOffloadedScanBatchingSupported() throws RemoteException;

    void onBrEdrDown() throws RemoteException;

    void onLeServiceUp() throws RemoteException;

    boolean readLocalOobData() throws RemoteException;

    void registerCallback(IBluetoothCallback iBluetoothCallback) throws RemoteException;

    boolean registerMetadataListener(IBluetoothMetadataListener iBluetoothMetadataListener, BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean removeBond(BluetoothDevice bluetoothDevice) throws RemoteException;

    BluetoothActivityEnergyInfo reportActivityInfo() throws RemoteException;

    void requestActivityInfo(ResultReceiver resultReceiver) throws RemoteException;

    boolean sdpSearch(BluetoothDevice bluetoothDevice, ParcelUuid parcelUuid) throws RemoteException;

    void sendConnectionStateChange(BluetoothDevice bluetoothDevice, int i, int i2, int i3) throws RemoteException;

    boolean setBluetoothClass(BluetoothClass bluetoothClass) throws RemoteException;

    boolean setDiscoverableTimeout(int i) throws RemoteException;

    boolean setIoCapability(int i) throws RemoteException;

    boolean setLeIoCapability(int i) throws RemoteException;

    boolean setMessageAccessPermission(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    boolean setMetadata(BluetoothDevice bluetoothDevice, int i, byte[] bArr) throws RemoteException;

    boolean setName(String str) throws RemoteException;

    boolean setPairingConfirmation(BluetoothDevice bluetoothDevice, boolean z) throws RemoteException;

    boolean setPasskey(BluetoothDevice bluetoothDevice, boolean z, int i, byte[] bArr) throws RemoteException;

    boolean setPhonebookAccessPermission(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    boolean setPin(BluetoothDevice bluetoothDevice, boolean z, int i, byte[] bArr) throws RemoteException;

    boolean setRemoteAlias(BluetoothDevice bluetoothDevice, String str) throws RemoteException;

    boolean setScanMode(int i, int i2) throws RemoteException;

    boolean setSilenceMode(BluetoothDevice bluetoothDevice, boolean z) throws RemoteException;

    boolean setSimAccessPermission(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    boolean startDiscovery(String str) throws RemoteException;

    void unregisterCallback(IBluetoothCallback iBluetoothCallback) throws RemoteException;

    boolean unregisterMetadataListener(BluetoothDevice bluetoothDevice) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBluetooth {
        @Override // android.bluetooth.IBluetooth
        public boolean isEnabled() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getState() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean enable() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean enableNoAutoConnect() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean disable() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public String getAddress() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetooth
        public ParcelUuid[] getUuids() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setName(String name) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public String getName() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetooth
        public BluetoothClass getBluetoothClass() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setBluetoothClass(BluetoothClass bluetoothClass) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getIoCapability() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setIoCapability(int capability) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getLeIoCapability() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setLeIoCapability(int capability) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean readLocalOobData() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getScanMode() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setScanMode(int mode, int duration) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getDiscoverableTimeout() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setDiscoverableTimeout(int timeout) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean startDiscovery(String callingPackage) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean cancelDiscovery() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean isDiscovering() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public long getDiscoveryEndMillis() throws RemoteException {
            return 0L;
        }

        @Override // android.bluetooth.IBluetooth
        public int getAdapterConnectionState() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public int getProfileConnectionState(int profile) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public BluetoothDevice[] getBondedDevices() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean createBond(BluetoothDevice device, int transport) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean createBondOutOfBand(BluetoothDevice device, int transport, OobData oobData) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean addOutOfBandBondDevice(BluetoothDevice device, String linkKey, int linkKeyType, int pinLen) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public void getLinkKey(BluetoothDevice device) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetooth
        public boolean cancelBondProcess(BluetoothDevice device) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean removeBond(BluetoothDevice device) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getBondState(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean isBondingInitiatedLocally(BluetoothDevice device) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public long getSupportedProfiles() throws RemoteException {
            return 0L;
        }

        @Override // android.bluetooth.IBluetooth
        public int getConnectionState(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public String getRemoteName(BluetoothDevice device) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetooth
        public int getRemoteType(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public String getRemoteAlias(BluetoothDevice device) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setRemoteAlias(BluetoothDevice device, String name) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getRemoteClass(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public ParcelUuid[] getRemoteUuids(BluetoothDevice device) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean fetchRemoteUuids(BluetoothDevice device) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean sdpSearch(BluetoothDevice device, ParcelUuid uuid) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getBatteryLevel(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public int getMaxConnectedAudioDevices() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setPin(BluetoothDevice device, boolean accept, int len, byte[] pinCode) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setPasskey(BluetoothDevice device, boolean accept, int len, byte[] passkey) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setPairingConfirmation(BluetoothDevice device, boolean accept) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getPhonebookAccessPermission(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setSilenceMode(BluetoothDevice device, boolean silence) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean getSilenceMode(BluetoothDevice device) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setPhonebookAccessPermission(BluetoothDevice device, int value) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getMessageAccessPermission(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setMessageAccessPermission(BluetoothDevice device, int value) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getSimAccessPermission(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setSimAccessPermission(BluetoothDevice device, int value) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public void sendConnectionStateChange(BluetoothDevice device, int profile, int state, int prevState) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetooth
        public void registerCallback(IBluetoothCallback callback) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetooth
        public void unregisterCallback(IBluetoothCallback callback) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetooth
        public IBluetoothSocketManager getSocketManager() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean factoryReset() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean isMultiAdvertisementSupported() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean isOffloadedFilteringSupported() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean isOffloadedScanBatchingSupported() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean isActivityAndEnergyReportingSupported() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean isLe2MPhySupported() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean isLeCodedPhySupported() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean isLeExtendedAdvertisingSupported() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean isLePeriodicAdvertisingSupported() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public int getLeMaximumAdvertisingDataLength() throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetooth
        public BluetoothActivityEnergyInfo reportActivityInfo() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean registerMetadataListener(IBluetoothMetadataListener listener, BluetoothDevice device) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean unregisterMetadataListener(BluetoothDevice device) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public boolean setMetadata(BluetoothDevice device, int key, byte[] value) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetooth
        public byte[] getMetadata(BluetoothDevice device, int key) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetooth
        public void requestActivityInfo(ResultReceiver result) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetooth
        public void onLeServiceUp() throws RemoteException {
        }

        @Override // android.bluetooth.IBluetooth
        public void onBrEdrDown() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBluetooth {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetooth";
        static final int TRANSACTION_addOutOfBandBondDevice = 30;
        static final int TRANSACTION_cancelBondProcess = 32;
        static final int TRANSACTION_cancelDiscovery = 22;
        static final int TRANSACTION_createBond = 28;
        static final int TRANSACTION_createBondOutOfBand = 29;
        static final int TRANSACTION_disable = 5;
        static final int TRANSACTION_enable = 3;
        static final int TRANSACTION_enableNoAutoConnect = 4;
        static final int TRANSACTION_factoryReset = 63;
        static final int TRANSACTION_fetchRemoteUuids = 44;
        static final int TRANSACTION_getAdapterConnectionState = 25;
        static final int TRANSACTION_getAddress = 6;
        static final int TRANSACTION_getBatteryLevel = 46;
        static final int TRANSACTION_getBluetoothClass = 10;
        static final int TRANSACTION_getBondState = 34;
        static final int TRANSACTION_getBondedDevices = 27;
        static final int TRANSACTION_getConnectionState = 37;
        static final int TRANSACTION_getDiscoverableTimeout = 19;
        static final int TRANSACTION_getDiscoveryEndMillis = 24;
        static final int TRANSACTION_getIoCapability = 12;
        static final int TRANSACTION_getLeIoCapability = 14;
        static final int TRANSACTION_getLeMaximumAdvertisingDataLength = 72;
        static final int TRANSACTION_getLinkKey = 31;
        static final int TRANSACTION_getMaxConnectedAudioDevices = 47;
        static final int TRANSACTION_getMessageAccessPermission = 55;
        static final int TRANSACTION_getMetadata = 77;
        static final int TRANSACTION_getName = 9;
        static final int TRANSACTION_getPhonebookAccessPermission = 51;
        static final int TRANSACTION_getProfileConnectionState = 26;
        static final int TRANSACTION_getRemoteAlias = 40;
        static final int TRANSACTION_getRemoteClass = 42;
        static final int TRANSACTION_getRemoteName = 38;
        static final int TRANSACTION_getRemoteType = 39;
        static final int TRANSACTION_getRemoteUuids = 43;
        static final int TRANSACTION_getScanMode = 17;
        static final int TRANSACTION_getSilenceMode = 53;
        static final int TRANSACTION_getSimAccessPermission = 57;
        static final int TRANSACTION_getSocketManager = 62;
        static final int TRANSACTION_getState = 2;
        static final int TRANSACTION_getSupportedProfiles = 36;
        static final int TRANSACTION_getUuids = 7;
        static final int TRANSACTION_isActivityAndEnergyReportingSupported = 67;
        static final int TRANSACTION_isBondingInitiatedLocally = 35;
        static final int TRANSACTION_isDiscovering = 23;
        static final int TRANSACTION_isEnabled = 1;
        static final int TRANSACTION_isLe2MPhySupported = 68;
        static final int TRANSACTION_isLeCodedPhySupported = 69;
        static final int TRANSACTION_isLeExtendedAdvertisingSupported = 70;
        static final int TRANSACTION_isLePeriodicAdvertisingSupported = 71;
        static final int TRANSACTION_isMultiAdvertisementSupported = 64;
        static final int TRANSACTION_isOffloadedFilteringSupported = 65;
        static final int TRANSACTION_isOffloadedScanBatchingSupported = 66;
        static final int TRANSACTION_onBrEdrDown = 80;
        static final int TRANSACTION_onLeServiceUp = 79;
        static final int TRANSACTION_readLocalOobData = 16;
        static final int TRANSACTION_registerCallback = 60;
        static final int TRANSACTION_registerMetadataListener = 74;
        static final int TRANSACTION_removeBond = 33;
        static final int TRANSACTION_reportActivityInfo = 73;
        static final int TRANSACTION_requestActivityInfo = 78;
        static final int TRANSACTION_sdpSearch = 45;
        static final int TRANSACTION_sendConnectionStateChange = 59;
        static final int TRANSACTION_setBluetoothClass = 11;
        static final int TRANSACTION_setDiscoverableTimeout = 20;
        static final int TRANSACTION_setIoCapability = 13;
        static final int TRANSACTION_setLeIoCapability = 15;
        static final int TRANSACTION_setMessageAccessPermission = 56;
        static final int TRANSACTION_setMetadata = 76;
        static final int TRANSACTION_setName = 8;
        static final int TRANSACTION_setPairingConfirmation = 50;
        static final int TRANSACTION_setPasskey = 49;
        static final int TRANSACTION_setPhonebookAccessPermission = 54;
        static final int TRANSACTION_setPin = 48;
        static final int TRANSACTION_setRemoteAlias = 41;
        static final int TRANSACTION_setScanMode = 18;
        static final int TRANSACTION_setSilenceMode = 52;
        static final int TRANSACTION_setSimAccessPermission = 58;
        static final int TRANSACTION_startDiscovery = 21;
        static final int TRANSACTION_unregisterCallback = 61;
        static final int TRANSACTION_unregisterMetadataListener = 75;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetooth asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBluetooth)) {
                return (IBluetooth) iin;
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
                    return "isEnabled";
                case 2:
                    return "getState";
                case 3:
                    return "enable";
                case 4:
                    return "enableNoAutoConnect";
                case 5:
                    return "disable";
                case 6:
                    return "getAddress";
                case 7:
                    return "getUuids";
                case 8:
                    return "setName";
                case 9:
                    return "getName";
                case 10:
                    return "getBluetoothClass";
                case 11:
                    return "setBluetoothClass";
                case 12:
                    return "getIoCapability";
                case 13:
                    return "setIoCapability";
                case 14:
                    return "getLeIoCapability";
                case 15:
                    return "setLeIoCapability";
                case 16:
                    return "readLocalOobData";
                case 17:
                    return "getScanMode";
                case 18:
                    return "setScanMode";
                case 19:
                    return "getDiscoverableTimeout";
                case 20:
                    return "setDiscoverableTimeout";
                case 21:
                    return "startDiscovery";
                case 22:
                    return "cancelDiscovery";
                case 23:
                    return "isDiscovering";
                case 24:
                    return "getDiscoveryEndMillis";
                case 25:
                    return "getAdapterConnectionState";
                case 26:
                    return "getProfileConnectionState";
                case 27:
                    return "getBondedDevices";
                case 28:
                    return "createBond";
                case 29:
                    return "createBondOutOfBand";
                case 30:
                    return "addOutOfBandBondDevice";
                case 31:
                    return "getLinkKey";
                case 32:
                    return "cancelBondProcess";
                case 33:
                    return "removeBond";
                case 34:
                    return "getBondState";
                case 35:
                    return "isBondingInitiatedLocally";
                case 36:
                    return "getSupportedProfiles";
                case 37:
                    return "getConnectionState";
                case 38:
                    return "getRemoteName";
                case 39:
                    return "getRemoteType";
                case 40:
                    return "getRemoteAlias";
                case 41:
                    return "setRemoteAlias";
                case 42:
                    return "getRemoteClass";
                case 43:
                    return "getRemoteUuids";
                case 44:
                    return "fetchRemoteUuids";
                case 45:
                    return "sdpSearch";
                case 46:
                    return "getBatteryLevel";
                case 47:
                    return "getMaxConnectedAudioDevices";
                case 48:
                    return "setPin";
                case 49:
                    return "setPasskey";
                case 50:
                    return "setPairingConfirmation";
                case 51:
                    return "getPhonebookAccessPermission";
                case 52:
                    return "setSilenceMode";
                case 53:
                    return "getSilenceMode";
                case 54:
                    return "setPhonebookAccessPermission";
                case 55:
                    return "getMessageAccessPermission";
                case 56:
                    return "setMessageAccessPermission";
                case 57:
                    return "getSimAccessPermission";
                case 58:
                    return "setSimAccessPermission";
                case 59:
                    return "sendConnectionStateChange";
                case 60:
                    return "registerCallback";
                case 61:
                    return "unregisterCallback";
                case 62:
                    return "getSocketManager";
                case 63:
                    return "factoryReset";
                case 64:
                    return "isMultiAdvertisementSupported";
                case 65:
                    return "isOffloadedFilteringSupported";
                case 66:
                    return "isOffloadedScanBatchingSupported";
                case 67:
                    return "isActivityAndEnergyReportingSupported";
                case 68:
                    return "isLe2MPhySupported";
                case 69:
                    return "isLeCodedPhySupported";
                case 70:
                    return "isLeExtendedAdvertisingSupported";
                case 71:
                    return "isLePeriodicAdvertisingSupported";
                case 72:
                    return "getLeMaximumAdvertisingDataLength";
                case 73:
                    return "reportActivityInfo";
                case 74:
                    return "registerMetadataListener";
                case 75:
                    return "unregisterMetadataListener";
                case 76:
                    return "setMetadata";
                case 77:
                    return "getMetadata";
                case 78:
                    return "requestActivityInfo";
                case 79:
                    return "onLeServiceUp";
                case 80:
                    return "onBrEdrDown";
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
            BluetoothClass _arg0;
            BluetoothDevice _arg02;
            BluetoothDevice _arg03;
            OobData _arg2;
            BluetoothDevice _arg04;
            BluetoothDevice _arg05;
            BluetoothDevice _arg06;
            BluetoothDevice _arg07;
            BluetoothDevice _arg08;
            BluetoothDevice _arg09;
            BluetoothDevice _arg010;
            BluetoothDevice _arg011;
            BluetoothDevice _arg012;
            BluetoothDevice _arg013;
            BluetoothDevice _arg014;
            BluetoothDevice _arg015;
            BluetoothDevice _arg016;
            BluetoothDevice _arg017;
            BluetoothDevice _arg018;
            ParcelUuid _arg1;
            BluetoothDevice _arg019;
            BluetoothDevice _arg020;
            boolean _arg12;
            BluetoothDevice _arg021;
            BluetoothDevice _arg022;
            BluetoothDevice _arg023;
            BluetoothDevice _arg024;
            BluetoothDevice _arg025;
            BluetoothDevice _arg026;
            BluetoothDevice _arg027;
            BluetoothDevice _arg028;
            BluetoothDevice _arg029;
            BluetoothDevice _arg030;
            BluetoothDevice _arg031;
            BluetoothDevice _arg13;
            BluetoothDevice _arg032;
            BluetoothDevice _arg033;
            BluetoothDevice _arg034;
            ResultReceiver _arg035;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isEnabled = isEnabled();
                    reply.writeNoException();
                    reply.writeInt(isEnabled ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _result = getState();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    boolean enable = enable();
                    reply.writeNoException();
                    reply.writeInt(enable ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    boolean enableNoAutoConnect = enableNoAutoConnect();
                    reply.writeNoException();
                    reply.writeInt(enableNoAutoConnect ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    boolean disable = disable();
                    reply.writeNoException();
                    reply.writeInt(disable ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _result2 = getAddress();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelUuid[] _result3 = getUuids();
                    reply.writeNoException();
                    reply.writeTypedArray(_result3, 1);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg036 = data.readString();
                    boolean name = setName(_arg036);
                    reply.writeNoException();
                    reply.writeInt(name ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _result4 = getName();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothClass _result5 = getBluetoothClass();
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = BluetoothClass.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    boolean bluetoothClass = setBluetoothClass(_arg0);
                    reply.writeNoException();
                    reply.writeInt(bluetoothClass ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _result6 = getIoCapability();
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg037 = data.readInt();
                    boolean ioCapability = setIoCapability(_arg037);
                    reply.writeNoException();
                    reply.writeInt(ioCapability ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _result7 = getLeIoCapability();
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg038 = data.readInt();
                    boolean leIoCapability = setLeIoCapability(_arg038);
                    reply.writeNoException();
                    reply.writeInt(leIoCapability ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    boolean readLocalOobData = readLocalOobData();
                    reply.writeNoException();
                    reply.writeInt(readLocalOobData ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _result8 = getScanMode();
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg039 = data.readInt();
                    boolean scanMode = setScanMode(_arg039, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(scanMode ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _result9 = getDiscoverableTimeout();
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg040 = data.readInt();
                    boolean discoverableTimeout = setDiscoverableTimeout(_arg040);
                    reply.writeNoException();
                    reply.writeInt(discoverableTimeout ? 1 : 0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg041 = data.readString();
                    boolean startDiscovery = startDiscovery(_arg041);
                    reply.writeNoException();
                    reply.writeInt(startDiscovery ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    boolean cancelDiscovery = cancelDiscovery();
                    reply.writeNoException();
                    reply.writeInt(cancelDiscovery ? 1 : 0);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDiscovering = isDiscovering();
                    reply.writeNoException();
                    reply.writeInt(isDiscovering ? 1 : 0);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    long _result10 = getDiscoveryEndMillis();
                    reply.writeNoException();
                    reply.writeLong(_result10);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _result11 = getAdapterConnectionState();
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg042 = data.readInt();
                    int _result12 = getProfileConnectionState(_arg042);
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice[] _result13 = getBondedDevices();
                    reply.writeNoException();
                    reply.writeTypedArray(_result13, 1);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    boolean createBond = createBond(_arg02, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(createBond ? 1 : 0);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    int _arg14 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = OobData.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    boolean createBondOutOfBand = createBondOutOfBand(_arg03, _arg14, _arg2);
                    reply.writeNoException();
                    reply.writeInt(createBondOutOfBand ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    String _arg15 = data.readString();
                    int _arg22 = data.readInt();
                    int _arg3 = data.readInt();
                    boolean addOutOfBandBondDevice = addOutOfBandBondDevice(_arg04, _arg15, _arg22, _arg3);
                    reply.writeNoException();
                    reply.writeInt(addOutOfBandBondDevice ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    getLinkKey(_arg05);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    boolean cancelBondProcess = cancelBondProcess(_arg06);
                    reply.writeNoException();
                    reply.writeInt(cancelBondProcess ? 1 : 0);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    boolean removeBond = removeBond(_arg07);
                    reply.writeNoException();
                    reply.writeInt(removeBond ? 1 : 0);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    int _result14 = getBondState(_arg08);
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg09 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg09 = null;
                    }
                    boolean isBondingInitiatedLocally = isBondingInitiatedLocally(_arg09);
                    reply.writeNoException();
                    reply.writeInt(isBondingInitiatedLocally ? 1 : 0);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    long _result15 = getSupportedProfiles();
                    reply.writeNoException();
                    reply.writeLong(_result15);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg010 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg010 = null;
                    }
                    int _result16 = getConnectionState(_arg010);
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg011 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg011 = null;
                    }
                    String _result17 = getRemoteName(_arg011);
                    reply.writeNoException();
                    reply.writeString(_result17);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg012 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg012 = null;
                    }
                    int _result18 = getRemoteType(_arg012);
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg013 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg013 = null;
                    }
                    String _result19 = getRemoteAlias(_arg013);
                    reply.writeNoException();
                    reply.writeString(_result19);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg014 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg014 = null;
                    }
                    boolean remoteAlias = setRemoteAlias(_arg014, data.readString());
                    reply.writeNoException();
                    reply.writeInt(remoteAlias ? 1 : 0);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg015 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg015 = null;
                    }
                    int _result20 = getRemoteClass(_arg015);
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg016 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg016 = null;
                    }
                    ParcelUuid[] _result21 = getRemoteUuids(_arg016);
                    reply.writeNoException();
                    reply.writeTypedArray(_result21, 1);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg017 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg017 = null;
                    }
                    boolean fetchRemoteUuids = fetchRemoteUuids(_arg017);
                    reply.writeNoException();
                    reply.writeInt(fetchRemoteUuids ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg018 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg018 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    boolean sdpSearch = sdpSearch(_arg018, _arg1);
                    reply.writeNoException();
                    reply.writeInt(sdpSearch ? 1 : 0);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg019 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg019 = null;
                    }
                    int _result22 = getBatteryLevel(_arg019);
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    int _result23 = getMaxConnectedAudioDevices();
                    reply.writeNoException();
                    reply.writeInt(_result23);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg020 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg020 = null;
                    }
                    _arg12 = data.readInt() != 0;
                    int _arg23 = data.readInt();
                    byte[] _arg32 = data.createByteArray();
                    boolean pin = setPin(_arg020, _arg12, _arg23, _arg32);
                    reply.writeNoException();
                    reply.writeInt(pin ? 1 : 0);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg021 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg021 = null;
                    }
                    _arg12 = data.readInt() != 0;
                    int _arg24 = data.readInt();
                    byte[] _arg33 = data.createByteArray();
                    boolean passkey = setPasskey(_arg021, _arg12, _arg24, _arg33);
                    reply.writeNoException();
                    reply.writeInt(passkey ? 1 : 0);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg022 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg022 = null;
                    }
                    _arg12 = data.readInt() != 0;
                    boolean pairingConfirmation = setPairingConfirmation(_arg022, _arg12);
                    reply.writeNoException();
                    reply.writeInt(pairingConfirmation ? 1 : 0);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg023 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg023 = null;
                    }
                    int _result24 = getPhonebookAccessPermission(_arg023);
                    reply.writeNoException();
                    reply.writeInt(_result24);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg024 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg024 = null;
                    }
                    _arg12 = data.readInt() != 0;
                    boolean silenceMode = setSilenceMode(_arg024, _arg12);
                    reply.writeNoException();
                    reply.writeInt(silenceMode ? 1 : 0);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg025 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg025 = null;
                    }
                    boolean silenceMode2 = getSilenceMode(_arg025);
                    reply.writeNoException();
                    reply.writeInt(silenceMode2 ? 1 : 0);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg026 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg026 = null;
                    }
                    boolean phonebookAccessPermission = setPhonebookAccessPermission(_arg026, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(phonebookAccessPermission ? 1 : 0);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg027 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg027 = null;
                    }
                    int _result25 = getMessageAccessPermission(_arg027);
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg028 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg028 = null;
                    }
                    boolean messageAccessPermission = setMessageAccessPermission(_arg028, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(messageAccessPermission ? 1 : 0);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg029 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg029 = null;
                    }
                    int _result26 = getSimAccessPermission(_arg029);
                    reply.writeNoException();
                    reply.writeInt(_result26);
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg030 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg030 = null;
                    }
                    boolean simAccessPermission = setSimAccessPermission(_arg030, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(simAccessPermission ? 1 : 0);
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg031 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg031 = null;
                    }
                    int _arg16 = data.readInt();
                    int _arg25 = data.readInt();
                    int _arg34 = data.readInt();
                    sendConnectionStateChange(_arg031, _arg16, _arg25, _arg34);
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    IBluetoothCallback _arg043 = IBluetoothCallback.Stub.asInterface(data.readStrongBinder());
                    registerCallback(_arg043);
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    IBluetoothCallback _arg044 = IBluetoothCallback.Stub.asInterface(data.readStrongBinder());
                    unregisterCallback(_arg044);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    IBluetoothSocketManager _result27 = getSocketManager();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result27 != null ? _result27.asBinder() : null);
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    boolean factoryReset = factoryReset();
                    reply.writeNoException();
                    reply.writeInt(factoryReset ? 1 : 0);
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isMultiAdvertisementSupported = isMultiAdvertisementSupported();
                    reply.writeNoException();
                    reply.writeInt(isMultiAdvertisementSupported ? 1 : 0);
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isOffloadedFilteringSupported = isOffloadedFilteringSupported();
                    reply.writeNoException();
                    reply.writeInt(isOffloadedFilteringSupported ? 1 : 0);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isOffloadedScanBatchingSupported = isOffloadedScanBatchingSupported();
                    reply.writeNoException();
                    reply.writeInt(isOffloadedScanBatchingSupported ? 1 : 0);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isActivityAndEnergyReportingSupported = isActivityAndEnergyReportingSupported();
                    reply.writeNoException();
                    reply.writeInt(isActivityAndEnergyReportingSupported ? 1 : 0);
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isLe2MPhySupported = isLe2MPhySupported();
                    reply.writeNoException();
                    reply.writeInt(isLe2MPhySupported ? 1 : 0);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isLeCodedPhySupported = isLeCodedPhySupported();
                    reply.writeNoException();
                    reply.writeInt(isLeCodedPhySupported ? 1 : 0);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isLeExtendedAdvertisingSupported = isLeExtendedAdvertisingSupported();
                    reply.writeNoException();
                    reply.writeInt(isLeExtendedAdvertisingSupported ? 1 : 0);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isLePeriodicAdvertisingSupported = isLePeriodicAdvertisingSupported();
                    reply.writeNoException();
                    reply.writeInt(isLePeriodicAdvertisingSupported ? 1 : 0);
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    int _result28 = getLeMaximumAdvertisingDataLength();
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothActivityEnergyInfo _result29 = reportActivityInfo();
                    reply.writeNoException();
                    if (_result29 != null) {
                        reply.writeInt(1);
                        _result29.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    IBluetoothMetadataListener _arg045 = IBluetoothMetadataListener.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg13 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    boolean registerMetadataListener = registerMetadataListener(_arg045, _arg13);
                    reply.writeNoException();
                    reply.writeInt(registerMetadataListener ? 1 : 0);
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg032 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg032 = null;
                    }
                    boolean unregisterMetadataListener = unregisterMetadataListener(_arg032);
                    reply.writeNoException();
                    reply.writeInt(unregisterMetadataListener ? 1 : 0);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg033 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg033 = null;
                    }
                    int _arg17 = data.readInt();
                    byte[] _arg26 = data.createByteArray();
                    boolean metadata = setMetadata(_arg033, _arg17, _arg26);
                    reply.writeNoException();
                    reply.writeInt(metadata ? 1 : 0);
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg034 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg034 = null;
                    }
                    byte[] _result30 = getMetadata(_arg034, data.readInt());
                    reply.writeNoException();
                    reply.writeByteArray(_result30);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg035 = ResultReceiver.CREATOR.createFromParcel(data);
                    } else {
                        _arg035 = null;
                    }
                    requestActivityInfo(_arg035);
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    onLeServiceUp();
                    reply.writeNoException();
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    onBrEdrDown();
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBluetooth {
            public static IBluetooth sDefaultImpl;
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

            @Override // android.bluetooth.IBluetooth
            public boolean isEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isEnabled();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getState();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean enable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enable();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean enableNoAutoConnect() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enableNoAutoConnect();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean disable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().disable();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public String getAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAddress();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public ParcelUuid[] getUuids() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUuids();
                    }
                    _reply.readException();
                    ParcelUuid[] _result = (ParcelUuid[]) _reply.createTypedArray(ParcelUuid.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setName(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setName(name);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public String getName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public BluetoothClass getBluetoothClass() throws RemoteException {
                BluetoothClass _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBluetoothClass();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = BluetoothClass.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setBluetoothClass(BluetoothClass bluetoothClass) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bluetoothClass != null) {
                        _data.writeInt(1);
                        bluetoothClass.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setBluetoothClass(bluetoothClass);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getIoCapability() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIoCapability();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setIoCapability(int capability) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capability);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setIoCapability(capability);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getLeIoCapability() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLeIoCapability();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setLeIoCapability(int capability) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capability);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setLeIoCapability(capability);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean readLocalOobData() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().readLocalOobData();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getScanMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getScanMode();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setScanMode(int mode, int duration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(duration);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setScanMode(mode, duration);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getDiscoverableTimeout() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDiscoverableTimeout();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setDiscoverableTimeout(int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeout);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setDiscoverableTimeout(timeout);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean startDiscovery(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startDiscovery(callingPackage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean cancelDiscovery() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().cancelDiscovery();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean isDiscovering() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDiscovering();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public long getDiscoveryEndMillis() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDiscoveryEndMillis();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getAdapterConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAdapterConnectionState();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getProfileConnectionState(int profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(profile);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProfileConnectionState(profile);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public BluetoothDevice[] getBondedDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBondedDevices();
                    }
                    _reply.readException();
                    BluetoothDevice[] _result = (BluetoothDevice[]) _reply.createTypedArray(BluetoothDevice.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean createBond(BluetoothDevice device, int transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(transport);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().createBond(device, transport);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean createBondOutOfBand(BluetoothDevice device, int transport, OobData oobData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(transport);
                    if (oobData != null) {
                        _data.writeInt(1);
                        oobData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().createBondOutOfBand(device, transport, oobData);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean addOutOfBandBondDevice(BluetoothDevice device, String linkKey, int linkKeyType, int pinLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(linkKey);
                    _data.writeInt(linkKeyType);
                    _data.writeInt(pinLen);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addOutOfBandBondDevice(device, linkKey, linkKeyType, pinLen);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public void getLinkKey(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getLinkKey(device);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean cancelBondProcess(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().cancelBondProcess(device);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean removeBond(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeBond(device);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getBondState(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBondState(device);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean isBondingInitiatedLocally(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBondingInitiatedLocally(device);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public long getSupportedProfiles() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSupportedProfiles();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getConnectionState(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getConnectionState(device);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public String getRemoteName(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRemoteName(device);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getRemoteType(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRemoteType(device);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public String getRemoteAlias(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRemoteAlias(device);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setRemoteAlias(BluetoothDevice device, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setRemoteAlias(device, name);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getRemoteClass(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRemoteClass(device);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public ParcelUuid[] getRemoteUuids(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRemoteUuids(device);
                    }
                    _reply.readException();
                    ParcelUuid[] _result = (ParcelUuid[]) _reply.createTypedArray(ParcelUuid.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean fetchRemoteUuids(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().fetchRemoteUuids(device);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean sdpSearch(BluetoothDevice device, ParcelUuid uuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().sdpSearch(device, uuid);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getBatteryLevel(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBatteryLevel(device);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getMaxConnectedAudioDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMaxConnectedAudioDevices();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setPin(BluetoothDevice device, boolean accept, int len, byte[] pinCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(accept ? 1 : 0);
                    _data.writeInt(len);
                    _data.writeByteArray(pinCode);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPin(device, accept, len, pinCode);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setPasskey(BluetoothDevice device, boolean accept, int len, byte[] passkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(accept ? 1 : 0);
                    _data.writeInt(len);
                    _data.writeByteArray(passkey);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPasskey(device, accept, len, passkey);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setPairingConfirmation(BluetoothDevice device, boolean accept) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(accept ? 1 : 0);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPairingConfirmation(device, accept);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getPhonebookAccessPermission(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPhonebookAccessPermission(device);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setSilenceMode(BluetoothDevice device, boolean silence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(silence ? 1 : 0);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setSilenceMode(device, silence);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean getSilenceMode(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSilenceMode(device);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setPhonebookAccessPermission(BluetoothDevice device, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPhonebookAccessPermission(device, value);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getMessageAccessPermission(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMessageAccessPermission(device);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setMessageAccessPermission(BluetoothDevice device, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setMessageAccessPermission(device, value);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getSimAccessPermission(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSimAccessPermission(device);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setSimAccessPermission(BluetoothDevice device, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setSimAccessPermission(device, value);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public void sendConnectionStateChange(BluetoothDevice device, int profile, int state, int prevState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(profile);
                    _data.writeInt(state);
                    _data.writeInt(prevState);
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendConnectionStateChange(device, profile, state, prevState);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public void registerCallback(IBluetoothCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerCallback(callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public void unregisterCallback(IBluetoothCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterCallback(callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public IBluetoothSocketManager getSocketManager() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSocketManager();
                    }
                    _reply.readException();
                    IBluetoothSocketManager _result = IBluetoothSocketManager.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean factoryReset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().factoryReset();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean isMultiAdvertisementSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMultiAdvertisementSupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean isOffloadedFilteringSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isOffloadedFilteringSupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean isOffloadedScanBatchingSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isOffloadedScanBatchingSupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean isActivityAndEnergyReportingSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isActivityAndEnergyReportingSupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean isLe2MPhySupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isLe2MPhySupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean isLeCodedPhySupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isLeCodedPhySupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean isLeExtendedAdvertisingSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isLeExtendedAdvertisingSupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean isLePeriodicAdvertisingSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isLePeriodicAdvertisingSupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public int getLeMaximumAdvertisingDataLength() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLeMaximumAdvertisingDataLength();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public BluetoothActivityEnergyInfo reportActivityInfo() throws RemoteException {
                BluetoothActivityEnergyInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().reportActivityInfo();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = BluetoothActivityEnergyInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean registerMetadataListener(IBluetoothMetadataListener listener, BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerMetadataListener(listener, device);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean unregisterMetadataListener(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unregisterMetadataListener(device);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public boolean setMetadata(BluetoothDevice device, int key, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(key);
                    _data.writeByteArray(value);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setMetadata(device, key, value);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public byte[] getMetadata(BluetoothDevice device, int key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(key);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMetadata(device, key);
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public void requestActivityInfo(ResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(78, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestActivityInfo(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public void onLeServiceUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onLeServiceUp();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetooth
            public void onBrEdrDown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBrEdrDown();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBluetooth impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBluetooth getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
