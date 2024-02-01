package android.bluetooth;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothManager;
import android.bluetooth.IBluetoothManagerCallback;
import android.bluetooth.IBluetoothMetadataListener;
import android.bluetooth.IBluetoothStateChangeCallback;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.PeriodicAdvertisingManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.SynchronousResultReceiver;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/* loaded from: classes.dex */
public final class BluetoothAdapter {
    public static final String ACTION_BLE_ACL_CONNECTED = "android.bluetooth.adapter.action.BLE_ACL_CONNECTED";
    public static final String ACTION_BLE_ACL_DISCONNECTED = "android.bluetooth.adapter.action.BLE_ACL_DISCONNECTED";
    @SystemApi
    public static final String ACTION_BLE_STATE_CHANGED = "android.bluetooth.adapter.action.BLE_STATE_CHANGED";
    public static final String ACTION_BLUETOOTH_ADDRESS_CHANGED = "android.bluetooth.adapter.action.BLUETOOTH_ADDRESS_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_DISCOVERY_FINISHED = "android.bluetooth.adapter.action.DISCOVERY_FINISHED";
    public static final String ACTION_DISCOVERY_STARTED = "android.bluetooth.adapter.action.DISCOVERY_STARTED";
    public static final String ACTION_LOCAL_NAME_CHANGED = "android.bluetooth.adapter.action.LOCAL_NAME_CHANGED";
    public static final String ACTION_LOCAL_OOB_DATA = "android.bluetooth.adapter.action.LOCAL_OOB_DATA";
    @SystemApi
    public static final String ACTION_REQUEST_BLE_SCAN_ALWAYS_AVAILABLE = "android.bluetooth.adapter.action.REQUEST_BLE_SCAN_ALWAYS_AVAILABLE";
    public static final String ACTION_REQUEST_DISABLE = "android.bluetooth.adapter.action.REQUEST_DISABLE";
    public static final String ACTION_REQUEST_DISCOVERABLE = "android.bluetooth.adapter.action.REQUEST_DISCOVERABLE";
    public static final String ACTION_REQUEST_ENABLE = "android.bluetooth.adapter.action.REQUEST_ENABLE";
    public static final String ACTION_SCAN_MODE_CHANGED = "android.bluetooth.adapter.action.SCAN_MODE_CHANGED";
    public static final String ACTION_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";
    public static final String ACTION_XPENG_STATE_CHANGED = "xiaopeng.bluetooth.action.ACTION_STATE_CHANGED";
    private static final int ADDRESS_LENGTH = 17;
    public static final String BLUETOOTH_MANAGER_SERVICE = "bluetooth_manager";
    private static final boolean DBG = true;
    public static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";
    public static final int ERROR = Integer.MIN_VALUE;
    public static final String EXTRA_BLUETOOTH_ADDRESS = "android.bluetooth.adapter.extra.BLUETOOTH_ADDRESS";
    public static final String EXTRA_CONNECTION_STATE = "android.bluetooth.adapter.extra.CONNECTION_STATE";
    public static final String EXTRA_DISCOVERABLE_DURATION = "android.bluetooth.adapter.extra.DISCOVERABLE_DURATION";
    public static final String EXTRA_LOCAL_NAME = "android.bluetooth.adapter.extra.LOCAL_NAME";
    public static final String EXTRA_LOCAL_OOB_DATA = "android.bluetooth.adapter.extra.LOCAL_OOB_DATA";
    public static final String EXTRA_PREVIOUS_CONNECTION_STATE = "android.bluetooth.adapter.extra.PREVIOUS_CONNECTION_STATE";
    public static final String EXTRA_PREVIOUS_SCAN_MODE = "android.bluetooth.adapter.extra.PREVIOUS_SCAN_MODE";
    public static final String EXTRA_PREVIOUS_STATE = "android.bluetooth.adapter.extra.PREVIOUS_STATE";
    public static final String EXTRA_SCAN_MODE = "android.bluetooth.adapter.extra.SCAN_MODE";
    public static final String EXTRA_STATE = "android.bluetooth.adapter.extra.STATE";
    public static final int IO_CAPABILITY_IN = 2;
    public static final int IO_CAPABILITY_IO = 1;
    public static final int IO_CAPABILITY_KBDISP = 4;
    public static final int IO_CAPABILITY_MAX = 5;
    public static final int IO_CAPABILITY_NONE = 3;
    public static final int IO_CAPABILITY_OUT = 0;
    public static final int IO_CAPABILITY_UNKNOWN = 255;
    public static final int SCAN_MODE_CONNECTABLE = 21;
    public static final int SCAN_MODE_CONNECTABLE_DISCOVERABLE = 23;
    public static final int SCAN_MODE_NONE = 20;
    public static final int SOCKET_CHANNEL_AUTO_STATIC_NO_SDP = -2;
    public static final int STATE_BLE_ON = 15;
    public static final int STATE_BLE_TURNING_OFF = 16;
    public static final int STATE_BLE_TURNING_ON = 14;
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_DISCONNECTING = 3;
    public static final int STATE_OFF = 10;
    public static final int STATE_ON = 12;
    public static final int STATE_TURNING_OFF = 13;
    public static final int STATE_TURNING_ON = 11;
    private static final String TAG = "BluetoothAdapter";
    public static final int TYPE_DEVICE_MAX = 1;
    public static final int TYPE_SECOND_DEVICE = 1;
    private static final boolean VDBG = false;
    public static final String XPENG_EXTRA_DEVICEID = "deviceid";
    public static final String XPENG_EXTRA_PRESTATE = "prestate";
    public static final String XPENG_EXTRA_STATE = "state";
    private static BluetoothAdapter sAdapter;
    private static BluetoothLeAdvertiser sBluetoothLeAdvertiser;
    private static BluetoothLeScanner sBluetoothLeScanner;
    private static PeriodicAdvertisingManager sPeriodicAdvertisingManager;
    private Context mContext;
    private final Map<LeScanCallback, ScanCallback> mLeScanClients;
    private final IBluetoothManager mManagerService;
    @UnsupportedAppUsage
    private IBluetooth mService;
    private final IBinder mToken;
    public static final UUID LE_PSM_CHARACTERISTIC_UUID = UUID.fromString("2d410339-82b6-42aa-b34e-e2e01df8cc1a");
    private static final Map<BluetoothDevice, List<Pair<OnMetadataChangedListener, Executor>>> sMetadataListeners = new HashMap();
    private static final IBluetoothMetadataListener sBluetoothMetadataListener = new AnonymousClass1();
    private final ReentrantReadWriteLock mServiceLock = new ReentrantReadWriteLock();
    private final Object mLock = new Object();
    private final IBluetoothManagerCallback mManagerCallback = new AnonymousClass2();
    private final ArrayList<IBluetoothManagerCallback> mProxyServiceStateCallbacks = new ArrayList<>();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface AdapterState {
    }

    /* loaded from: classes.dex */
    public interface BluetoothStateChangeCallback {
        void onBluetoothStateChange(boolean z);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface IoCapability {
    }

    /* loaded from: classes.dex */
    public interface LeScanCallback {
        void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr);
    }

    @SystemApi
    /* loaded from: classes.dex */
    public interface OnMetadataChangedListener {
        void onMetadataChanged(BluetoothDevice bluetoothDevice, int i, byte[] bArr);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ScanMode {
    }

    public static String nameForState(int state) {
        switch (state) {
            case 10:
                return "OFF";
            case 11:
                return "TURNING_ON";
            case 12:
                return "ON";
            case 13:
                return "TURNING_OFF";
            case 14:
                return "BLE_TURNING_ON";
            case 15:
                return "BLE_ON";
            case 16:
                return "BLE_TURNING_OFF";
            default:
                return "?!?!? (" + state + ")";
        }
    }

    /* renamed from: android.bluetooth.BluetoothAdapter$1  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass1 extends IBluetoothMetadataListener.Stub {
        AnonymousClass1() {
        }

        @Override // android.bluetooth.IBluetoothMetadataListener
        public void onMetadataChanged(final BluetoothDevice device, final int key, final byte[] value) {
            synchronized (BluetoothAdapter.sMetadataListeners) {
                if (BluetoothAdapter.sMetadataListeners.containsKey(device)) {
                    List<Pair<OnMetadataChangedListener, Executor>> list = (List) BluetoothAdapter.sMetadataListeners.get(device);
                    for (Pair<OnMetadataChangedListener, Executor> pair : list) {
                        final OnMetadataChangedListener listener = (OnMetadataChangedListener) pair.first;
                        Executor executor = (Executor) pair.second;
                        executor.execute(new Runnable() { // from class: android.bluetooth.-$$Lambda$BluetoothAdapter$1$I3p3FVKkxuogQU8eug7PAKoZKZc
                            @Override // java.lang.Runnable
                            public final void run() {
                                BluetoothAdapter.OnMetadataChangedListener.this.onMetadataChanged(device, key, value);
                            }
                        });
                    }
                }
            }
        }
    }

    public static synchronized BluetoothAdapter getDefaultAdapter() {
        BluetoothAdapter bluetoothAdapter;
        synchronized (BluetoothAdapter.class) {
            if (sAdapter == null) {
                IBinder b = ServiceManager.getService(BLUETOOTH_MANAGER_SERVICE);
                if (b != null) {
                    IBluetoothManager managerService = IBluetoothManager.Stub.asInterface(b);
                    sAdapter = new BluetoothAdapter(managerService);
                } else {
                    Log.e(TAG, "Bluetooth binder is null");
                }
            }
            bluetoothAdapter = sAdapter;
        }
        return bluetoothAdapter;
    }

    BluetoothAdapter(IBluetoothManager managerService) {
        try {
            if (managerService == null) {
                throw new IllegalArgumentException("bluetooth manager service is null");
            }
            try {
                this.mServiceLock.writeLock().lock();
                this.mService = managerService.registerAdapter(this.mManagerCallback);
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            this.mServiceLock.writeLock().unlock();
            this.mManagerService = managerService;
            this.mLeScanClients = new HashMap();
            this.mToken = new Binder();
        } catch (Throwable th) {
            this.mServiceLock.writeLock().unlock();
            throw th;
        }
    }

    public BluetoothDevice getRemoteDevice(String address) {
        return new BluetoothDevice(address);
    }

    public BluetoothDevice getRemoteDevice(byte[] address) {
        if (address == null || address.length != 6) {
            throw new IllegalArgumentException("Bluetooth address must have 6 bytes");
        }
        return new BluetoothDevice(String.format(Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X", Byte.valueOf(address[0]), Byte.valueOf(address[1]), Byte.valueOf(address[2]), Byte.valueOf(address[3]), Byte.valueOf(address[4]), Byte.valueOf(address[5])));
    }

    public BluetoothLeAdvertiser getBluetoothLeAdvertiser() {
        if (!getLeAccess()) {
            return null;
        }
        synchronized (this.mLock) {
            if (sBluetoothLeAdvertiser == null) {
                sBluetoothLeAdvertiser = new BluetoothLeAdvertiser(this.mManagerService);
            }
        }
        return sBluetoothLeAdvertiser;
    }

    public PeriodicAdvertisingManager getPeriodicAdvertisingManager() {
        if (getLeAccess() && isLePeriodicAdvertisingSupported()) {
            synchronized (this.mLock) {
                if (sPeriodicAdvertisingManager == null) {
                    sPeriodicAdvertisingManager = new PeriodicAdvertisingManager(this.mManagerService);
                }
            }
            return sPeriodicAdvertisingManager;
        }
        return null;
    }

    public BluetoothLeScanner getBluetoothLeScanner() {
        if (!getLeAccess()) {
            return null;
        }
        synchronized (this.mLock) {
            if (sBluetoothLeScanner == null) {
                sBluetoothLeScanner = new BluetoothLeScanner(this.mManagerService);
            }
        }
        return sBluetoothLeScanner;
    }

    public boolean isEnabled() {
        try {
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.isEnabled();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            this.mServiceLock.readLock().unlock();
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    @SystemApi
    public boolean isLeEnabled() {
        int state = getLeState();
        Log.d(TAG, "isLeEnabled(): " + nameForState(state));
        return state == 12 || state == 15;
    }

    @SystemApi
    public boolean disableBLE() {
        if (isBleScanAlwaysAvailable()) {
            int state = getLeState();
            if (state == 12 || state == 15) {
                String packageName = ActivityThread.currentPackageName();
                Log.d(TAG, "disableBLE(): de-registering " + packageName);
                try {
                    this.mManagerService.updateBleAppCount(this.mToken, false, packageName);
                    return true;
                } catch (RemoteException e) {
                    Log.e(TAG, "", e);
                    return true;
                }
            }
            Log.d(TAG, "disableBLE(): Already disabled");
            return false;
        }
        return false;
    }

    @SystemApi
    public boolean enableBLE() {
        if (isBleScanAlwaysAvailable()) {
            try {
                String packageName = ActivityThread.currentPackageName();
                this.mManagerService.updateBleAppCount(this.mToken, true, packageName);
                if (isLeEnabled()) {
                    Log.d(TAG, "enableBLE(): Bluetooth already enabled");
                    return true;
                }
                Log.d(TAG, "enableBLE(): Calling enable");
                return this.mManagerService.enable(packageName);
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                return false;
            }
        }
        return false;
    }

    public int getState() {
        int state = 10;
        try {
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    state = this.mService.getState();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            this.mServiceLock.readLock().unlock();
            if (state == 15 || state == 14 || state == 16) {
                return 10;
            }
            return state;
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
            throw th;
        }
    }

    @UnsupportedAppUsage
    public int getLeState() {
        int state = 10;
        try {
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    state = this.mService.getState();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            return state;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    boolean getLeAccess() {
        return getLeState() == 12 || getLeState() == 15;
    }

    public boolean enable() {
        if (isEnabled()) {
            Log.d(TAG, "enable(): BT already enabled!");
            return true;
        }
        try {
            return this.mManagerService.enable(ActivityThread.currentPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean disable() {
        try {
            return this.mManagerService.disable(ActivityThread.currentPackageName(), true);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean disableDevice(int deviceId, boolean persist) {
        if (deviceId > 1) {
            return false;
        }
        try {
            return this.mManagerService.disableDevice(deviceId, ActivityThread.currentPackageName(), persist);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean disableDevice(int deviceId) {
        return disableDevice(deviceId, true);
    }

    public boolean enableDevice(int deviceId, boolean persist) {
        if (deviceId > 1) {
            return false;
        }
        try {
            return this.mManagerService.enableDevice(deviceId, ActivityThread.currentPackageName(), persist);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean enableDevice(int deviceId) {
        return enableDevice(deviceId, true);
    }

    public boolean isDeviceEnabled(int deviceId) {
        if (deviceId > 1) {
            return false;
        }
        try {
            return this.mManagerService.isDeviceEnabled(deviceId);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean isDeviceConnected(int deviceId) {
        String addr = getConnectedDevice(deviceId);
        return !TextUtils.isEmpty(addr);
    }

    public String getConnectedDevice(int deviceId) {
        try {
            return this.mManagerService.getConnectedDevice(deviceId);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return "";
        }
    }

    public void setConnectedDevice(int deviceId, String address) {
        try {
            this.mManagerService.setConnectedDevice(deviceId, address);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    public String getModuleVersion() {
        try {
            return this.mManagerService.getModuleVersion();
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return "0-0";
        }
    }

    public boolean startDownload(String path) {
        try {
            return this.mManagerService.startDownload(path);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean startUpload() {
        try {
            return this.mManagerService.startUpload();
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public int getDownloadProgress() {
        try {
            return this.mManagerService.getDownloadProgress();
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return 0;
        }
    }

    public int getUploadProgress() {
        try {
            return this.mManagerService.getUploadProgress();
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return 0;
        }
    }

    @UnsupportedAppUsage
    public boolean disable(boolean persist) {
        try {
            return this.mManagerService.disable(ActivityThread.currentPackageName(), persist);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public String getAddress() {
        try {
            return this.mManagerService.getAddress();
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    public String getName() {
        try {
            return this.mManagerService.getName();
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    @UnsupportedAppUsage
    public boolean factoryReset() {
        try {
            try {
                this.mServiceLock.readLock().lock();
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            if (this.mService == null || !this.mService.factoryReset() || this.mManagerService == null || !this.mManagerService.onFactoryReset()) {
                Log.e(TAG, "factoryReset(): Setting persist.bluetooth.factoryreset to retry later");
                SystemProperties.set("persist.bluetooth.factoryreset", "true");
                this.mServiceLock.readLock().unlock();
                return false;
            }
            return true;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    @UnsupportedAppUsage
    public ParcelUuid[] getUuids() {
        try {
            if (getState() != 12) {
                return null;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.getUuids();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            return null;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean setName(String name) {
        try {
            if (getState() != 12) {
                return false;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.setName(name);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public BluetoothClass getBluetoothClass() {
        try {
            if (getState() != 12) {
                return null;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.getBluetoothClass();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            return null;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean setBluetoothClass(BluetoothClass bluetoothClass) {
        try {
            if (getState() != 12) {
                return false;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.setBluetoothClass(bluetoothClass);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public int getIoCapability() {
        try {
            if (getState() != 12) {
                return 255;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.getIoCapability();
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return 255;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean setIoCapability(int capability) {
        try {
            if (getState() != 12) {
                return false;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.setIoCapability(capability);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public int getLeIoCapability() {
        try {
            if (getState() != 12) {
                return 255;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.getLeIoCapability();
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return 255;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean setLeIoCapability(int capability) {
        try {
            if (getState() != 12) {
                return false;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.setLeIoCapability(capability);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public int getScanMode() {
        try {
            if (getState() != 12) {
                return 20;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.getScanMode();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            return 20;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    @UnsupportedAppUsage
    public boolean setScanMode(int mode, int duration) {
        try {
            if (getState() != 12) {
                return false;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.setScanMode(mode, duration);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    @UnsupportedAppUsage
    public boolean setScanMode(int mode) {
        if (getState() != 12) {
            return false;
        }
        return setScanMode(mode, getDiscoverableTimeout());
    }

    @UnsupportedAppUsage
    public int getDiscoverableTimeout() {
        try {
            if (getState() != 12) {
                return -1;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.getDiscoverableTimeout();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            return -1;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    @UnsupportedAppUsage
    public void setDiscoverableTimeout(int timeout) {
        if (getState() != 12) {
            return;
        }
        try {
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    this.mService.setDiscoverableTimeout(timeout);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public long getDiscoveryEndMillis() {
        try {
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.getDiscoveryEndMillis();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            this.mServiceLock.readLock().unlock();
            return -1L;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private String getOpPackageName() {
        Context context = this.mContext;
        if (context != null) {
            return context.getOpPackageName();
        }
        return ActivityThread.currentOpPackageName();
    }

    public boolean startDiscovery() {
        if (getState() != 12) {
            return false;
        }
        try {
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.startDiscovery(getOpPackageName());
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean cancelDiscovery() {
        try {
            if (getState() != 12) {
                return false;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.cancelDiscovery();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isDiscovering() {
        try {
            if (getState() != 12) {
                return false;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.isDiscovering();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isMultipleAdvertisementSupported() {
        try {
            if (getState() != 12) {
                return false;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.isMultiAdvertisementSupported();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "failed to get isMultipleAdvertisementSupported, error: ", e);
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    @SystemApi
    public boolean isBleScanAlwaysAvailable() {
        try {
            return this.mManagerService.isBleScanAlwaysAvailable();
        } catch (RemoteException e) {
            Log.e(TAG, "remote expection when calling isBleScanAlwaysAvailable", e);
            return false;
        }
    }

    public boolean isOffloadedFilteringSupported() {
        try {
            if (getLeAccess()) {
                try {
                    this.mServiceLock.readLock().lock();
                    if (this.mService != null) {
                        return this.mService.isOffloadedFilteringSupported();
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "failed to get isOffloadedFilteringSupported, error: ", e);
                }
                return false;
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isOffloadedScanBatchingSupported() {
        try {
            if (getLeAccess()) {
                try {
                    this.mServiceLock.readLock().lock();
                    if (this.mService != null) {
                        return this.mService.isOffloadedScanBatchingSupported();
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "failed to get isOffloadedScanBatchingSupported, error: ", e);
                }
                return false;
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isLe2MPhySupported() {
        try {
            if (getLeAccess()) {
                try {
                    this.mServiceLock.readLock().lock();
                    if (this.mService != null) {
                        return this.mService.isLe2MPhySupported();
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "failed to get isExtendedAdvertisingSupported, error: ", e);
                }
                return false;
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isLeCodedPhySupported() {
        try {
            if (getLeAccess()) {
                try {
                    this.mServiceLock.readLock().lock();
                    if (this.mService != null) {
                        return this.mService.isLeCodedPhySupported();
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "failed to get isLeCodedPhySupported, error: ", e);
                }
                return false;
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isLeExtendedAdvertisingSupported() {
        try {
            if (getLeAccess()) {
                try {
                    this.mServiceLock.readLock().lock();
                    if (this.mService != null) {
                        return this.mService.isLeExtendedAdvertisingSupported();
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "failed to get isLeExtendedAdvertisingSupported, error: ", e);
                }
                return false;
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isLePeriodicAdvertisingSupported() {
        try {
            if (getLeAccess()) {
                try {
                    this.mServiceLock.readLock().lock();
                    if (this.mService != null) {
                        return this.mService.isLePeriodicAdvertisingSupported();
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "failed to get isLePeriodicAdvertisingSupported, error: ", e);
                }
                return false;
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public int getLeMaximumAdvertisingDataLength() {
        try {
            if (getLeAccess()) {
                try {
                    this.mServiceLock.readLock().lock();
                    if (this.mService != null) {
                        return this.mService.getLeMaximumAdvertisingDataLength();
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "failed to get getLeMaximumAdvertisingDataLength, error: ", e);
                }
                return 0;
            }
            return 0;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    private boolean isHearingAidProfileSupported() {
        try {
            return this.mManagerService.isHearingAidProfileSupported();
        } catch (RemoteException e) {
            Log.e(TAG, "remote expection when calling isHearingAidProfileSupported", e);
            return false;
        }
    }

    public int getMaxConnectedAudioDevices() {
        try {
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.getMaxConnectedAudioDevices();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "failed to get getMaxConnectedAudioDevices, error: ", e);
            }
            this.mServiceLock.readLock().unlock();
            return 1;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isHardwareTrackingFiltersAvailable() {
        if (getLeAccess()) {
            try {
                IBluetoothGatt iGatt = this.mManagerService.getBluetoothGatt();
                if (iGatt == null) {
                    return false;
                }
                return iGatt.numHwTrackFiltersAvailable() != 0;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                return false;
            }
        }
        return false;
    }

    @Deprecated
    public BluetoothActivityEnergyInfo getControllerActivityEnergyInfo(int updateType) {
        SynchronousResultReceiver receiver = new SynchronousResultReceiver();
        requestControllerActivityEnergyInfo(receiver);
        try {
            SynchronousResultReceiver.Result result = receiver.awaitResult(1000L);
            if (result.bundle != null) {
                return (BluetoothActivityEnergyInfo) result.bundle.getParcelable("controller_activity");
            }
            return null;
        } catch (TimeoutException e) {
            Log.e(TAG, "getControllerActivityEnergyInfo timed out");
            return null;
        }
    }

    public void requestControllerActivityEnergyInfo(ResultReceiver result) {
        try {
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    this.mService.requestActivityInfo(result);
                    result = null;
                }
                this.mServiceLock.readLock().unlock();
                if (result == null) {
                    return;
                }
            } catch (RemoteException e) {
                Log.e(TAG, "getControllerActivityEnergyInfoCallback: " + e);
                this.mServiceLock.readLock().unlock();
                if (result == null) {
                    return;
                }
            }
            result.send(0, null);
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
            if (result != null) {
                result.send(0, null);
            }
            throw th;
        }
    }

    public Set<BluetoothDevice> getBondedDevices() {
        try {
            if (getState() != 12) {
                return toDeviceSet(new BluetoothDevice[0]);
            }
            try {
                this.mServiceLock.readLock().lock();
                return this.mService != null ? toDeviceSet(this.mService.getBondedDevices()) : toDeviceSet(new BluetoothDevice[0]);
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                this.mServiceLock.readLock().unlock();
                return null;
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public List<Integer> getSupportedProfiles() {
        ArrayList<Integer> supportedProfiles = new ArrayList<>();
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    long supportedProfilesBitMask = this.mService.getSupportedProfiles();
                    for (int i = 0; i <= 21; i++) {
                        if (((1 << i) & supportedProfilesBitMask) != 0) {
                            supportedProfiles.add(Integer.valueOf(i));
                        }
                    }
                } else if (isHearingAidProfileSupported()) {
                    supportedProfiles.add(21);
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getSupportedProfiles:", e);
        }
        return supportedProfiles;
    }

    @UnsupportedAppUsage
    public int getConnectionState() {
        try {
            if (getState() != 12) {
                return 0;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.getAdapterConnectionState();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "getConnectionState:", e);
            }
            return 0;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public int getProfileConnectionState(int profile) {
        try {
            if (getState() != 12) {
                return 0;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.getProfileConnectionState(profile);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "getProfileConnectionState:", e);
            }
            return 0;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public BluetoothServerSocket listenUsingRfcommOn(int channel) throws IOException {
        return listenUsingRfcommOn(channel, false, false);
    }

    @UnsupportedAppUsage
    public BluetoothServerSocket listenUsingRfcommOn(int channel, boolean mitm, boolean min16DigitPin) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, true, true, channel, mitm, min16DigitPin);
        int errno = socket.mSocket.bindListen();
        if (channel == -2) {
            socket.setChannel(socket.mSocket.getPort());
        }
        if (errno == 0) {
            return socket;
        }
        throw new IOException("Error: " + errno);
    }

    public BluetoothServerSocket listenUsingRfcommWithServiceRecord(String name, UUID uuid) throws IOException {
        return createNewRfcommSocketAndRecord(name, uuid, true, true);
    }

    public BluetoothServerSocket listenUsingInsecureRfcommWithServiceRecord(String name, UUID uuid) throws IOException {
        return createNewRfcommSocketAndRecord(name, uuid, false, false);
    }

    @UnsupportedAppUsage
    public BluetoothServerSocket listenUsingEncryptedRfcommWithServiceRecord(String name, UUID uuid) throws IOException {
        return createNewRfcommSocketAndRecord(name, uuid, false, true);
    }

    private BluetoothServerSocket createNewRfcommSocketAndRecord(String name, UUID uuid, boolean auth, boolean encrypt) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, auth, encrypt, new ParcelUuid(uuid));
        socket.setServiceName(name);
        int errno = socket.mSocket.bindListen();
        if (errno != 0) {
            throw new IOException("Error: " + errno);
        }
        return socket;
    }

    public BluetoothServerSocket listenUsingInsecureRfcommOn(int port) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, false, false, port);
        int errno = socket.mSocket.bindListen();
        if (port == -2) {
            socket.setChannel(socket.mSocket.getPort());
        }
        if (errno != 0) {
            throw new IOException("Error: " + errno);
        }
        return socket;
    }

    public BluetoothServerSocket listenUsingEncryptedRfcommOn(int port) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, false, true, port);
        int errno = socket.mSocket.bindListen();
        if (port == -2) {
            socket.setChannel(socket.mSocket.getPort());
        }
        if (errno < 0) {
            throw new IOException("Error: " + errno);
        }
        return socket;
    }

    public static BluetoothServerSocket listenUsingScoOn() throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(2, false, false, -1);
        socket.mSocket.bindListen();
        return socket;
    }

    public BluetoothServerSocket listenUsingL2capOn(int port, boolean mitm, boolean min16DigitPin) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(3, true, true, port, mitm, min16DigitPin);
        int errno = socket.mSocket.bindListen();
        if (port == -2) {
            int assignedChannel = socket.mSocket.getPort();
            Log.d(TAG, "listenUsingL2capOn: set assigned channel to " + assignedChannel);
            socket.setChannel(assignedChannel);
        }
        if (errno == 0) {
            return socket;
        }
        throw new IOException("Error: " + errno);
    }

    public BluetoothServerSocket listenUsingL2capOn(int port) throws IOException {
        return listenUsingL2capOn(port, false, false);
    }

    public BluetoothServerSocket listenUsingInsecureL2capOn(int port) throws IOException {
        Log.d(TAG, "listenUsingInsecureL2capOn: port=" + port);
        BluetoothServerSocket socket = new BluetoothServerSocket(3, false, false, port, false, false);
        int errno = socket.mSocket.bindListen();
        if (port == -2) {
            int assignedChannel = socket.mSocket.getPort();
            Log.d(TAG, "listenUsingInsecureL2capOn: set assigned channel to " + assignedChannel);
            socket.setChannel(assignedChannel);
        }
        if (errno != 0) {
            throw new IOException("Error: " + errno);
        }
        return socket;
    }

    public Pair<byte[], byte[]> readOutOfBandData() {
        return null;
    }

    public boolean readLocalOobData() {
        try {
            if (getState() != 12) {
                return false;
            }
            try {
                this.mServiceLock.readLock().lock();
                if (this.mService != null) {
                    return this.mService.readLocalOobData();
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            this.mServiceLock.readLock().unlock();
            return true;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean getProfileProxy(Context context, BluetoothProfile.ServiceListener listener, int profile) {
        if (context == null || listener == null) {
            return false;
        }
        if (profile == 1) {
            new BluetoothHeadset(context, listener);
            return true;
        } else if (profile == 2) {
            new BluetoothA2dp(context, listener);
            return true;
        } else if (profile == 11) {
            new BluetoothA2dpSink(context, listener);
            return true;
        } else if (profile == 12) {
            new BluetoothAvrcpController(context, listener);
            return true;
        } else if (profile == 4) {
            new BluetoothHidHost(context, listener);
            return true;
        } else if (profile == 5) {
            new BluetoothPan(context, listener);
            return true;
        } else if (profile == 3) {
            Log.e(TAG, "getProfileProxy(): BluetoothHealth is deprecated");
            return false;
        } else if (profile == 9) {
            new BluetoothMap(context, listener);
            return true;
        } else if (profile == 16) {
            new BluetoothHeadsetClient(context, listener);
            return true;
        } else if (profile == 10) {
            new BluetoothSap(context, listener);
            return true;
        } else if (profile == 17) {
            new BluetoothPbapClient(context, listener);
            return true;
        } else if (profile == 18) {
            new BluetoothMapClient(context, listener);
            return true;
        } else if (profile == 19) {
            new BluetoothHidDevice(context, listener);
            return true;
        } else if (profile != 21 || !isHearingAidProfileSupported()) {
            return false;
        } else {
            new BluetoothHearingAid(context, listener);
            return true;
        }
    }

    public void closeProfileProxy(int profile, BluetoothProfile proxy) {
        if (proxy == null) {
            return;
        }
        switch (profile) {
            case 1:
                BluetoothHeadset headset = (BluetoothHeadset) proxy;
                headset.close();
                return;
            case 2:
                BluetoothA2dp a2dp = (BluetoothA2dp) proxy;
                a2dp.close();
                return;
            case 3:
            case 6:
            case 13:
            case 14:
            case 15:
            case 20:
            default:
                return;
            case 4:
                BluetoothHidHost iDev = (BluetoothHidHost) proxy;
                iDev.close();
                return;
            case 5:
                BluetoothPan pan = (BluetoothPan) proxy;
                pan.close();
                return;
            case 7:
                BluetoothGatt gatt = (BluetoothGatt) proxy;
                gatt.close();
                return;
            case 8:
                BluetoothGattServer gattServer = (BluetoothGattServer) proxy;
                gattServer.close();
                return;
            case 9:
                BluetoothMap map = (BluetoothMap) proxy;
                map.close();
                return;
            case 10:
                BluetoothSap sap = (BluetoothSap) proxy;
                sap.close();
                return;
            case 11:
                BluetoothA2dpSink a2dpSink = (BluetoothA2dpSink) proxy;
                a2dpSink.close();
                return;
            case 12:
                BluetoothAvrcpController avrcp = (BluetoothAvrcpController) proxy;
                avrcp.close();
                return;
            case 16:
                BluetoothHeadsetClient headsetClient = (BluetoothHeadsetClient) proxy;
                headsetClient.close();
                return;
            case 17:
                BluetoothPbapClient pbapClient = (BluetoothPbapClient) proxy;
                pbapClient.close();
                return;
            case 18:
                BluetoothMapClient mapClient = (BluetoothMapClient) proxy;
                mapClient.close();
                return;
            case 19:
                BluetoothHidDevice hidDevice = (BluetoothHidDevice) proxy;
                hidDevice.close();
                return;
            case 21:
                BluetoothHearingAid hearingAid = (BluetoothHearingAid) proxy;
                hearingAid.close();
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.bluetooth.BluetoothAdapter$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends IBluetoothManagerCallback.Stub {
        AnonymousClass2() {
        }

        @Override // android.bluetooth.IBluetoothManagerCallback
        public void onBluetoothServiceUp(IBluetooth bluetoothService) {
            Log.d(BluetoothAdapter.TAG, "onBluetoothServiceUp: " + bluetoothService);
            BluetoothAdapter.this.mServiceLock.writeLock().lock();
            BluetoothAdapter.this.mService = bluetoothService;
            BluetoothAdapter.this.mServiceLock.writeLock().unlock();
            synchronized (BluetoothAdapter.this.mProxyServiceStateCallbacks) {
                Iterator it = BluetoothAdapter.this.mProxyServiceStateCallbacks.iterator();
                while (it.hasNext()) {
                    IBluetoothManagerCallback cb = (IBluetoothManagerCallback) it.next();
                    if (cb != null) {
                        try {
                            cb.onBluetoothServiceUp(bluetoothService);
                        } catch (Exception e) {
                            Log.e(BluetoothAdapter.TAG, "", e);
                        }
                    } else {
                        Log.d(BluetoothAdapter.TAG, "onBluetoothServiceUp: cb is null!");
                    }
                }
            }
            synchronized (BluetoothAdapter.sMetadataListeners) {
                BluetoothAdapter.sMetadataListeners.forEach(new BiConsumer() { // from class: android.bluetooth.-$$Lambda$BluetoothAdapter$2$INSd_aND-SGWhhPZUtIqya_Uxw4
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        BluetoothAdapter.AnonymousClass2.this.lambda$onBluetoothServiceUp$0$BluetoothAdapter$2((BluetoothDevice) obj, (List) obj2);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onBluetoothServiceUp$0$BluetoothAdapter$2(BluetoothDevice device, List pair) {
            try {
                BluetoothAdapter.this.mService.registerMetadataListener(BluetoothAdapter.sBluetoothMetadataListener, device);
            } catch (RemoteException e) {
                Log.e(BluetoothAdapter.TAG, "Failed to register metadata listener", e);
            }
        }

        @Override // android.bluetooth.IBluetoothManagerCallback
        public void onBluetoothServiceDown() {
            Log.d(BluetoothAdapter.TAG, "onBluetoothServiceDown: " + BluetoothAdapter.this.mService);
            try {
                BluetoothAdapter.this.mServiceLock.writeLock().lock();
                BluetoothAdapter.this.mService = null;
                if (BluetoothAdapter.this.mLeScanClients != null) {
                    BluetoothAdapter.this.mLeScanClients.clear();
                }
                if (BluetoothAdapter.sBluetoothLeAdvertiser != null) {
                    BluetoothAdapter.sBluetoothLeAdvertiser.cleanup();
                }
                if (BluetoothAdapter.sBluetoothLeScanner != null) {
                    BluetoothAdapter.sBluetoothLeScanner.cleanup();
                }
                BluetoothAdapter.this.mServiceLock.writeLock().unlock();
                synchronized (BluetoothAdapter.this.mProxyServiceStateCallbacks) {
                    Iterator it = BluetoothAdapter.this.mProxyServiceStateCallbacks.iterator();
                    while (it.hasNext()) {
                        IBluetoothManagerCallback cb = (IBluetoothManagerCallback) it.next();
                        if (cb != null) {
                            try {
                                cb.onBluetoothServiceDown();
                            } catch (Exception e) {
                                Log.e(BluetoothAdapter.TAG, "", e);
                            }
                        } else {
                            Log.d(BluetoothAdapter.TAG, "onBluetoothServiceDown: cb is null!");
                        }
                    }
                }
            } catch (Throwable th) {
                BluetoothAdapter.this.mServiceLock.writeLock().unlock();
                throw th;
            }
        }

        @Override // android.bluetooth.IBluetoothManagerCallback
        public void onBrEdrDown() {
        }
    }

    @SystemApi
    public boolean enableNoAutoConnect() {
        if (isEnabled()) {
            Log.d(TAG, "enableNoAutoConnect(): BT already enabled!");
            return true;
        }
        try {
            return this.mManagerService.enableNoAutoConnect(ActivityThread.currentPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean changeApplicationBluetoothState(boolean on, BluetoothStateChangeCallback callback) {
        return false;
    }

    /* loaded from: classes.dex */
    public class StateChangeCallbackWrapper extends IBluetoothStateChangeCallback.Stub {
        private BluetoothStateChangeCallback mCallback;

        StateChangeCallbackWrapper(BluetoothStateChangeCallback callback) {
            this.mCallback = callback;
        }

        @Override // android.bluetooth.IBluetoothStateChangeCallback
        public void onBluetoothStateChange(boolean on) {
            this.mCallback.onBluetoothStateChange(on);
        }
    }

    private Set<BluetoothDevice> toDeviceSet(BluetoothDevice[] devices) {
        Set<BluetoothDevice> deviceSet = new HashSet<>(Arrays.asList(devices));
        return Collections.unmodifiableSet(deviceSet);
    }

    protected void finalize() throws Throwable {
        try {
            try {
                this.mManagerService.unregisterAdapter(this.mManagerCallback);
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
        } finally {
            super.finalize();
        }
    }

    public static boolean checkBluetoothAddress(String address) {
        if (address == null || address.length() != 17) {
            return false;
        }
        for (int i = 0; i < 17; i++) {
            char c = address.charAt(i);
            int i2 = i % 3;
            if (i2 == 0 || i2 == 1) {
                if ((c < '0' || c > '9') && (c < 'A' || c > 'F')) {
                    return false;
                }
            } else if (i2 == 2 && c != ':') {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public IBluetoothManager getBluetoothManager() {
        return this.mManagerService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public IBluetooth getBluetoothService(IBluetoothManagerCallback cb) {
        synchronized (this.mProxyServiceStateCallbacks) {
            if (cb == null) {
                Log.w(TAG, "getBluetoothService() called with no BluetoothManagerCallback");
            } else if (!this.mProxyServiceStateCallbacks.contains(cb)) {
                this.mProxyServiceStateCallbacks.add(cb);
            }
        }
        return this.mService;
    }

    void removeServiceStateCallback(IBluetoothManagerCallback cb) {
        synchronized (this.mProxyServiceStateCallbacks) {
            this.mProxyServiceStateCallbacks.remove(cb);
        }
    }

    @Deprecated
    public boolean startLeScan(LeScanCallback callback) {
        return startLeScan(null, callback);
    }

    @Deprecated
    public boolean startLeScan(final UUID[] serviceUuids, final LeScanCallback callback) {
        Log.d(TAG, "startLeScan(): " + Arrays.toString(serviceUuids));
        if (callback == null) {
            Log.e(TAG, "startLeScan: null callback");
            return false;
        }
        BluetoothLeScanner scanner = getBluetoothLeScanner();
        if (scanner == null) {
            Log.e(TAG, "startLeScan: cannot get BluetoothLeScanner");
            return false;
        }
        synchronized (this.mLeScanClients) {
            if (this.mLeScanClients.containsKey(callback)) {
                Log.e(TAG, "LE Scan has already started");
                return false;
            }
            try {
                IBluetoothGatt iGatt = this.mManagerService.getBluetoothGatt();
                if (iGatt == null) {
                    return false;
                }
                ScanCallback scanCallback = new ScanCallback() { // from class: android.bluetooth.BluetoothAdapter.3
                    @Override // android.bluetooth.le.ScanCallback
                    public void onScanResult(int callbackType, ScanResult result) {
                        UUID[] uuidArr;
                        if (callbackType != 1) {
                            Log.e(BluetoothAdapter.TAG, "LE Scan has already started");
                            return;
                        }
                        ScanRecord scanRecord = result.getScanRecord();
                        if (scanRecord == null) {
                            return;
                        }
                        if (serviceUuids != null) {
                            List<ParcelUuid> uuids = new ArrayList<>();
                            for (UUID uuid : serviceUuids) {
                                uuids.add(new ParcelUuid(uuid));
                            }
                            List<ParcelUuid> scanServiceUuids = scanRecord.getServiceUuids();
                            if (scanServiceUuids == null || !scanServiceUuids.containsAll(uuids)) {
                                Log.d(BluetoothAdapter.TAG, "uuids does not match");
                                return;
                            }
                        }
                        callback.onLeScan(result.getDevice(), result.getRssi(), scanRecord.getBytes());
                    }
                };
                ScanSettings settings = new ScanSettings.Builder().setCallbackType(1).setScanMode(2).build();
                List<ScanFilter> filters = new ArrayList<>();
                if (serviceUuids != null && serviceUuids.length > 0) {
                    ScanFilter filter = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(serviceUuids[0])).build();
                    filters.add(filter);
                }
                scanner.startScan(filters, settings, scanCallback);
                this.mLeScanClients.put(callback, scanCallback);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                return false;
            }
        }
    }

    @Deprecated
    public void stopLeScan(LeScanCallback callback) {
        Log.d(TAG, "stopLeScan()");
        BluetoothLeScanner scanner = getBluetoothLeScanner();
        if (scanner == null) {
            return;
        }
        synchronized (this.mLeScanClients) {
            ScanCallback scanCallback = this.mLeScanClients.remove(callback);
            if (scanCallback == null) {
                Log.d(TAG, "scan not started yet");
            } else {
                scanner.stopScan(scanCallback);
            }
        }
    }

    public BluetoothServerSocket listenUsingL2capChannel() throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(4, true, true, -2, false, false);
        int errno = socket.mSocket.bindListen();
        if (errno != 0) {
            throw new IOException("Error: " + errno);
        }
        int assignedPsm = socket.mSocket.getPort();
        if (assignedPsm == 0) {
            throw new IOException("Error: Unable to assign PSM value");
        }
        Log.d(TAG, "listenUsingL2capChannel: set assigned PSM to " + assignedPsm);
        socket.setChannel(assignedPsm);
        return socket;
    }

    public BluetoothServerSocket listenUsingL2capCoc(int transport) throws IOException {
        Log.e(TAG, "listenUsingL2capCoc: PLEASE USE THE OFFICIAL API, listenUsingL2capChannel");
        return listenUsingL2capChannel();
    }

    public BluetoothServerSocket listenUsingInsecureL2capChannel() throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(4, false, false, -2, false, false);
        int errno = socket.mSocket.bindListen();
        if (errno != 0) {
            throw new IOException("Error: " + errno);
        }
        int assignedPsm = socket.mSocket.getPort();
        if (assignedPsm == 0) {
            throw new IOException("Error: Unable to assign PSM value");
        }
        Log.d(TAG, "listenUsingInsecureL2capChannel: set assigned PSM to " + assignedPsm);
        socket.setChannel(assignedPsm);
        return socket;
    }

    public BluetoothServerSocket listenUsingInsecureL2capCoc(int transport) throws IOException {
        Log.e(TAG, "listenUsingInsecureL2capCoc: PLEASE USE THE OFFICIAL API, listenUsingInsecureL2capChannel");
        return listenUsingInsecureL2capChannel();
    }

    @SystemApi
    public boolean addOnMetadataChangedListener(BluetoothDevice device, Executor executor, final OnMetadataChangedListener listener) {
        boolean ret;
        Map<BluetoothDevice, List<Pair<OnMetadataChangedListener, Executor>>> map;
        Log.d(TAG, "addOnMetadataChangedListener()");
        IBluetooth service = this.mService;
        if (service == null) {
            Log.e(TAG, "Bluetooth is not enabled. Cannot register metadata listener");
            return false;
        } else if (listener == null) {
            throw new NullPointerException("listener is null");
        } else {
            if (device == null) {
                throw new NullPointerException("device is null");
            }
            if (executor == null) {
                throw new NullPointerException("executor is null");
            }
            synchronized (sMetadataListeners) {
                List<Pair<OnMetadataChangedListener, Executor>> listenerList = sMetadataListeners.get(device);
                if (listenerList == null) {
                    listenerList = new ArrayList();
                    sMetadataListeners.put(device, listenerList);
                } else if (listenerList.stream().anyMatch(new Predicate() { // from class: android.bluetooth.-$$Lambda$BluetoothAdapter$3qDRCAtPJRu3UbUEFsHnCxkioak
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean equals;
                        equals = ((BluetoothAdapter.OnMetadataChangedListener) ((Pair) obj).first).equals(BluetoothAdapter.OnMetadataChangedListener.this);
                        return equals;
                    }
                })) {
                    throw new IllegalArgumentException("listener was already regestered for the device");
                }
                Pair<OnMetadataChangedListener, Executor> listenerPair = new Pair<>(listener, executor);
                listenerList.add(listenerPair);
                ret = false;
                try {
                    ret = service.registerMetadataListener(sBluetoothMetadataListener, device);
                } catch (RemoteException e) {
                    Log.e(TAG, "registerMetadataListener fail", e);
                    if (0 == 0) {
                        listenerList.remove(listenerPair);
                        if (listenerList.isEmpty()) {
                            map = sMetadataListeners;
                        }
                    }
                }
                if (!ret) {
                    listenerList.remove(listenerPair);
                    if (listenerList.isEmpty()) {
                        map = sMetadataListeners;
                        map.remove(device);
                    }
                }
            }
            return ret;
        }
    }

    @SystemApi
    public boolean removeOnMetadataChangedListener(BluetoothDevice device, final OnMetadataChangedListener listener) {
        Log.d(TAG, "removeOnMetadataChangedListener()");
        if (device == null) {
            throw new NullPointerException("device is null");
        }
        if (listener == null) {
            throw new NullPointerException("listener is null");
        }
        synchronized (sMetadataListeners) {
            if (!sMetadataListeners.containsKey(device)) {
                throw new IllegalArgumentException("device was not registered");
            }
            sMetadataListeners.get(device).removeIf(new Predicate() { // from class: android.bluetooth.-$$Lambda$BluetoothAdapter$dhiyWTssvWZcLytIeq61ARYDHrc
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean equals;
                    equals = ((BluetoothAdapter.OnMetadataChangedListener) ((Pair) obj).first).equals(BluetoothAdapter.OnMetadataChangedListener.this);
                    return equals;
                }
            });
            if (sMetadataListeners.get(device).isEmpty()) {
                sMetadataListeners.remove(device);
                IBluetooth service = this.mService;
                if (service == null) {
                    return true;
                }
                try {
                    return service.unregisterMetadataListener(device);
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterMetadataListener fail", e);
                    return false;
                }
            }
            return true;
        }
    }
}
