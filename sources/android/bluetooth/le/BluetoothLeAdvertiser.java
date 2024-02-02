package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.IAdvertisingSetCallback;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class BluetoothLeAdvertiser {
    private static final int FLAGS_FIELD_BYTES = 3;
    private static final int MANUFACTURER_SPECIFIC_DATA_LENGTH = 2;
    private static final int MAX_ADVERTISING_DATA_BYTES = 1650;
    private static final int MAX_LEGACY_ADVERTISING_DATA_BYTES = 31;
    private static final int OVERHEAD_BYTES_PER_FIELD = 2;
    private static final String TAG = "BluetoothLeAdvertiser";
    private final IBluetoothManager mBluetoothManager;
    private final Map<AdvertiseCallback, AdvertisingSetCallback> mLegacyAdvertisers = new HashMap();
    private final Map<AdvertisingSetCallback, IAdvertisingSetCallback> mCallbackWrappers = Collections.synchronizedMap(new HashMap());
    private final Map<Integer, AdvertisingSet> mAdvertisingSets = Collections.synchronizedMap(new HashMap());
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public synchronized BluetoothLeAdvertiser(IBluetoothManager bluetoothManager) {
        this.mBluetoothManager = bluetoothManager;
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseCallback callback) {
        startAdvertising(settings, advertiseData, null, callback);
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseData scanResponse, AdvertiseCallback callback) {
        synchronized (this.mLegacyAdvertisers) {
            try {
                try {
                    BluetoothLeUtils.checkAdapterStateOn(this.mBluetoothAdapter);
                    if (callback == null) {
                        throw new IllegalArgumentException("callback cannot be null");
                    }
                    boolean isConnectable = settings.isConnectable();
                    try {
                        int i = 1;
                        if (totalBytes(advertiseData, isConnectable) <= 31 && totalBytes(scanResponse, false) <= 31) {
                            if (this.mLegacyAdvertisers.containsKey(callback)) {
                                postStartFailure(callback, 3);
                                return;
                            }
                            AdvertisingSetParameters.Builder parameters = new AdvertisingSetParameters.Builder();
                            parameters.setLegacyMode(true);
                            parameters.setConnectable(isConnectable);
                            parameters.setScannable(true);
                            if (settings.getMode() == 0) {
                                parameters.setInterval(AdvertisingSetParameters.INTERVAL_HIGH);
                            } else if (settings.getMode() == 1) {
                                parameters.setInterval(400);
                            } else if (settings.getMode() == 2) {
                                parameters.setInterval(160);
                            }
                            if (settings.getTxPowerLevel() == 0) {
                                parameters.setTxPowerLevel(-21);
                            } else if (settings.getTxPowerLevel() == 1) {
                                parameters.setTxPowerLevel(-15);
                            } else if (settings.getTxPowerLevel() == 2) {
                                parameters.setTxPowerLevel(-7);
                            } else if (settings.getTxPowerLevel() == 3) {
                                parameters.setTxPowerLevel(1);
                            }
                            int duration = 0;
                            int timeoutMillis = settings.getTimeout();
                            if (timeoutMillis > 0) {
                                if (timeoutMillis >= 10) {
                                    i = timeoutMillis / 10;
                                }
                                duration = i;
                            }
                            int duration2 = duration;
                            AdvertisingSetCallback wrapped = wrapOldCallback(callback, settings);
                            this.mLegacyAdvertisers.put(callback, wrapped);
                            startAdvertisingSet(parameters.build(), advertiseData, scanResponse, null, null, duration2, 0, wrapped);
                            return;
                        }
                        postStartFailure(callback, 1);
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    synchronized AdvertisingSetCallback wrapOldCallback(final AdvertiseCallback callback, final AdvertiseSettings settings) {
        return new AdvertisingSetCallback() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.1
            @Override // android.bluetooth.le.AdvertisingSetCallback
            public void onAdvertisingSetStarted(AdvertisingSet advertisingSet, int txPower, int status) {
                if (status != 0) {
                    BluetoothLeAdvertiser.this.postStartFailure(callback, status);
                } else {
                    BluetoothLeAdvertiser.this.postStartSuccess(callback, settings);
                }
            }

            @Override // android.bluetooth.le.AdvertisingSetCallback
            public void onAdvertisingEnabled(AdvertisingSet advertisingSet, boolean enabled, int status) {
                if (enabled) {
                    Log.e(BluetoothLeAdvertiser.TAG, "Legacy advertiser should be only disabled on timeout, but was enabled!");
                } else {
                    BluetoothLeAdvertiser.this.stopAdvertising(callback);
                }
            }
        };
    }

    public void stopAdvertising(AdvertiseCallback callback) {
        synchronized (this.mLegacyAdvertisers) {
            if (callback == null) {
                throw new IllegalArgumentException("callback cannot be null");
            }
            AdvertisingSetCallback wrapper = this.mLegacyAdvertisers.get(callback);
            if (wrapper == null) {
                return;
            }
            stopAdvertisingSet(wrapper);
            this.mLegacyAdvertisers.remove(callback);
        }
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, AdvertisingSetCallback callback) {
        startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, 0, 0, callback, new Handler(Looper.getMainLooper()));
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, AdvertisingSetCallback callback, Handler handler) {
        startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, 0, 0, callback, handler);
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, int duration, int maxExtendedAdvertisingEvents, AdvertisingSetCallback callback) {
        startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, duration, maxExtendedAdvertisingEvents, callback, new Handler(Looper.getMainLooper()));
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, int duration, int maxExtendedAdvertisingEvents, AdvertisingSetCallback callback, List<BluetoothDevice> btDevices) {
        AdvertiseData advertiseData2;
        BluetoothLeUtils.checkAdapterStateOn(this.mBluetoothAdapter);
        Handler handler = new Handler(Looper.getMainLooper());
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null");
        }
        boolean isConnectable = parameters.isConnectable();
        if (parameters.isLegacy()) {
            if (totalBytes(advertiseData, isConnectable) > 31) {
                throw new IllegalArgumentException("Legacy advertising data too big");
            }
            if (totalBytes(scanResponse, false) > 31) {
                throw new IllegalArgumentException("Legacy scan response data too big");
            }
            advertiseData2 = periodicData;
        } else {
            boolean supportCodedPhy = this.mBluetoothAdapter.isLeCodedPhySupported();
            boolean support2MPhy = this.mBluetoothAdapter.isLe2MPhySupported();
            int pphy = parameters.getPrimaryPhy();
            int sphy = parameters.getSecondaryPhy();
            if (pphy == 3 && !supportCodedPhy) {
                throw new IllegalArgumentException("Unsupported primary PHY selected");
            }
            if ((sphy == 3 && !supportCodedPhy) || (sphy == 2 && !support2MPhy)) {
                throw new IllegalArgumentException("Unsupported secondary PHY selected");
            }
            int maxData = this.mBluetoothAdapter.getLeMaximumAdvertisingDataLength();
            if (totalBytes(advertiseData, isConnectable) > maxData) {
                throw new IllegalArgumentException("Advertising data too big");
            }
            if (totalBytes(scanResponse, false) > maxData) {
                throw new IllegalArgumentException("Scan response data too big");
            }
            advertiseData2 = periodicData;
            if (totalBytes(advertiseData2, false) > maxData) {
                throw new IllegalArgumentException("Periodic advertising data too big");
            }
            boolean supportPeriodic = this.mBluetoothAdapter.isLePeriodicAdvertisingSupported();
            if (periodicParameters != null && !supportPeriodic) {
                throw new IllegalArgumentException("Controller does not support LE Periodic Advertising");
            }
        }
        if (maxExtendedAdvertisingEvents < 0 || maxExtendedAdvertisingEvents > 255) {
            throw new IllegalArgumentException("maxExtendedAdvertisingEvents out of range: " + maxExtendedAdvertisingEvents);
        } else if (maxExtendedAdvertisingEvents != 0 && !this.mBluetoothAdapter.isLePeriodicAdvertisingSupported()) {
            throw new IllegalArgumentException("Can't use maxExtendedAdvertisingEvents with controller that don't support LE Extended Advertising");
        } else {
            if (duration < 0 || duration > 65535) {
                throw new IllegalArgumentException("duration out of range: " + duration);
            }
            try {
                IBluetoothGatt gatt = this.mBluetoothManager.getBluetoothGatt();
                IAdvertisingSetCallback wrapped = wrap(callback, handler);
                if (this.mCallbackWrappers.putIfAbsent(callback, wrapped) != null) {
                    throw new IllegalArgumentException("callback instance already associated with advertising");
                }
                try {
                    gatt.startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, advertiseData2, duration, maxExtendedAdvertisingEvents, wrapped, btDevices);
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to start advertising set - ", e);
                    postStartSetFailure(handler, callback, 4);
                }
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to get Bluetooth gatt - ", e2);
                postStartSetFailure(handler, callback, 4);
            }
        }
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, int duration, int maxExtendedAdvertisingEvents, AdvertisingSetCallback callback, Handler handler) {
        AdvertiseData advertiseData2;
        BluetoothLeUtils.checkAdapterStateOn(this.mBluetoothAdapter);
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null");
        }
        boolean isConnectable = parameters.isConnectable();
        if (parameters.isLegacy()) {
            if (totalBytes(advertiseData, isConnectable) > 31) {
                throw new IllegalArgumentException("Legacy advertising data too big");
            }
            if (totalBytes(scanResponse, false) > 31) {
                throw new IllegalArgumentException("Legacy scan response data too big");
            }
            advertiseData2 = periodicData;
        } else {
            boolean supportCodedPhy = this.mBluetoothAdapter.isLeCodedPhySupported();
            boolean support2MPhy = this.mBluetoothAdapter.isLe2MPhySupported();
            int pphy = parameters.getPrimaryPhy();
            int sphy = parameters.getSecondaryPhy();
            if (pphy == 3 && !supportCodedPhy) {
                throw new IllegalArgumentException("Unsupported primary PHY selected");
            }
            if ((sphy == 3 && !supportCodedPhy) || (sphy == 2 && !support2MPhy)) {
                throw new IllegalArgumentException("Unsupported secondary PHY selected");
            }
            int maxData = this.mBluetoothAdapter.getLeMaximumAdvertisingDataLength();
            if (totalBytes(advertiseData, isConnectable) > maxData) {
                throw new IllegalArgumentException("Advertising data too big");
            }
            if (totalBytes(scanResponse, false) > maxData) {
                throw new IllegalArgumentException("Scan response data too big");
            }
            advertiseData2 = periodicData;
            if (totalBytes(advertiseData2, false) > maxData) {
                throw new IllegalArgumentException("Periodic advertising data too big");
            }
            boolean supportPeriodic = this.mBluetoothAdapter.isLePeriodicAdvertisingSupported();
            if (periodicParameters != null && !supportPeriodic) {
                throw new IllegalArgumentException("Controller does not support LE Periodic Advertising");
            }
        }
        if (maxExtendedAdvertisingEvents < 0 || maxExtendedAdvertisingEvents > 255) {
            throw new IllegalArgumentException("maxExtendedAdvertisingEvents out of range: " + maxExtendedAdvertisingEvents);
        } else if (maxExtendedAdvertisingEvents != 0 && !this.mBluetoothAdapter.isLePeriodicAdvertisingSupported()) {
            throw new IllegalArgumentException("Can't use maxExtendedAdvertisingEvents with controller that don't support LE Extended Advertising");
        } else {
            if (duration < 0 || duration > 65535) {
                throw new IllegalArgumentException("duration out of range: " + duration);
            }
            try {
                IBluetoothGatt gatt = this.mBluetoothManager.getBluetoothGatt();
                IAdvertisingSetCallback wrapped = wrap(callback, handler);
                if (this.mCallbackWrappers.putIfAbsent(callback, wrapped) != null) {
                    throw new IllegalArgumentException("callback instance already associated with advertising");
                }
                try {
                    gatt.startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, advertiseData2, duration, maxExtendedAdvertisingEvents, wrapped, null);
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to start advertising set - ", e);
                    postStartSetFailure(handler, callback, 4);
                }
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to get Bluetooth gatt - ", e2);
                postStartSetFailure(handler, callback, 4);
            }
        }
    }

    public void stopAdvertisingSet(AdvertisingSetCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null");
        }
        IAdvertisingSetCallback wrapped = this.mCallbackWrappers.remove(callback);
        if (wrapped == null) {
            return;
        }
        try {
            IBluetoothGatt gatt = this.mBluetoothManager.getBluetoothGatt();
            gatt.stopAdvertisingSet(wrapped);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to stop advertising - ", e);
        }
    }

    public synchronized void cleanup() {
        this.mLegacyAdvertisers.clear();
        this.mCallbackWrappers.clear();
        this.mAdvertisingSets.clear();
    }

    private synchronized int totalBytes(AdvertiseData data, boolean isFlagsIncluded) {
        if (data == null) {
            return 0;
        }
        int size = isFlagsIncluded ? 3 : 0;
        if (data.getServiceUuids() != null) {
            int num16BitUuids = 0;
            int num32BitUuids = 0;
            int num128BitUuids = 0;
            for (ParcelUuid uuid : data.getServiceUuids()) {
                if (BluetoothUuid.is16BitUuid(uuid)) {
                    num16BitUuids++;
                } else if (BluetoothUuid.is32BitUuid(uuid)) {
                    num32BitUuids++;
                } else {
                    num128BitUuids++;
                }
            }
            if (num16BitUuids != 0) {
                size += (num16BitUuids * 2) + 2;
            }
            if (num32BitUuids != 0) {
                size += (num32BitUuids * 4) + 2;
            }
            if (num128BitUuids != 0) {
                size += (num128BitUuids * 16) + 2;
            }
        }
        for (ParcelUuid uuid2 : data.getServiceData().keySet()) {
            int uuidLen = BluetoothUuid.uuidToBytes(uuid2).length;
            size += 2 + uuidLen + byteLength(data.getServiceData().get(uuid2));
        }
        for (int i = 0; i < data.getManufacturerSpecificData().size(); i++) {
            size += 4 + byteLength(data.getManufacturerSpecificData().valueAt(i));
        }
        if (data.getIncludeTxPowerLevel()) {
            size += 3;
        }
        if (data.getIncludeDeviceName() && this.mBluetoothAdapter.getName() != null) {
            return size + 2 + this.mBluetoothAdapter.getName().length();
        }
        return size;
    }

    private synchronized int byteLength(byte[] array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }

    synchronized IAdvertisingSetCallback wrap(final AdvertisingSetCallback callback, final Handler handler) {
        return new IAdvertisingSetCallback.Stub() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2
            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onAdvertisingSetStarted(final int advertiserId, final int txPower, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (status != 0) {
                            callback.onAdvertisingSetStarted(null, 0, status);
                            BluetoothLeAdvertiser.this.mCallbackWrappers.remove(callback);
                            return;
                        }
                        AdvertisingSet advertisingSet = new AdvertisingSet(advertiserId, BluetoothLeAdvertiser.this.mBluetoothManager);
                        BluetoothLeAdvertiser.this.mAdvertisingSets.put(Integer.valueOf(advertiserId), advertisingSet);
                        callback.onAdvertisingSetStarted(advertisingSet, txPower, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onOwnAddressRead(final int advertiserId, final int addressType, final String address) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onOwnAddressRead(advertisingSet, addressType, address);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onAdvertisingSetStopped(final int advertiserId) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.3
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onAdvertisingSetStopped(advertisingSet);
                        BluetoothLeAdvertiser.this.mAdvertisingSets.remove(Integer.valueOf(advertiserId));
                        BluetoothLeAdvertiser.this.mCallbackWrappers.remove(callback);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onAdvertisingEnabled(final int advertiserId, final boolean enabled, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.4
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onAdvertisingEnabled(advertisingSet, enabled, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onAdvertisingDataSet(final int advertiserId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.5
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onAdvertisingDataSet(advertisingSet, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onScanResponseDataSet(final int advertiserId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.6
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onScanResponseDataSet(advertisingSet, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onAdvertisingParametersUpdated(final int advertiserId, final int txPower, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.7
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onAdvertisingParametersUpdated(advertisingSet, txPower, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onPeriodicAdvertisingParametersUpdated(final int advertiserId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.8
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onPeriodicAdvertisingParametersUpdated(advertisingSet, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onPeriodicAdvertisingDataSet(final int advertiserId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.9
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onPeriodicAdvertisingDataSet(advertisingSet, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onPeriodicAdvertisingEnabled(final int advertiserId, final boolean enable, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.10
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onPeriodicAdvertisingEnabled(advertisingSet, enable, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onAdvertisingWhiteListUpdated(final int advertiserId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.11
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onAdvertisingWhiteListUpdated(advertisingSet, status);
                    }
                });
            }
        };
    }

    private synchronized void postStartSetFailure(Handler handler, final AdvertisingSetCallback callback, final int error) {
        handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.3
            @Override // java.lang.Runnable
            public void run() {
                callback.onAdvertisingSetStarted(null, 0, error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void postStartFailure(final AdvertiseCallback callback, final int error) {
        this.mHandler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.4
            @Override // java.lang.Runnable
            public void run() {
                callback.onStartFailure(error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void postStartSuccess(final AdvertiseCallback callback, final AdvertiseSettings settings) {
        this.mHandler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.5
            @Override // java.lang.Runnable
            public void run() {
                callback.onStartSuccess(settings);
            }
        });
    }
}
