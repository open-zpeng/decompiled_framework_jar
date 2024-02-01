package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
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

    public BluetoothLeAdvertiser(IBluetoothManager bluetoothManager) {
        this.mBluetoothManager = bluetoothManager;
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseCallback callback) {
        startAdvertising(settings, advertiseData, null, callback);
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseData scanResponse, AdvertiseCallback callback) {
        int duration;
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
                                parameters.setInterval(1600);
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
                            int timeoutMillis = settings.getTimeout();
                            if (timeoutMillis <= 0) {
                                duration = 0;
                            } else {
                                if (timeoutMillis >= 10) {
                                    i = timeoutMillis / 10;
                                }
                                int duration2 = i;
                                duration = duration2;
                            }
                            AdvertisingSetCallback wrapped = wrapOldCallback(callback, settings);
                            this.mLegacyAdvertisers.put(callback, wrapped);
                            startAdvertisingSet(parameters.build(), advertiseData, scanResponse, null, null, duration, 0, wrapped);
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

    AdvertisingSetCallback wrapOldCallback(final AdvertiseCallback callback, final AdvertiseSettings settings) {
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
            try {
                if (callback == null) {
                    throw new IllegalArgumentException("callback cannot be null");
                }
                AdvertisingSetCallback wrapper = this.mLegacyAdvertisers.get(callback);
                if (wrapper == null) {
                    return;
                }
                stopAdvertisingSet(wrapper);
                this.mLegacyAdvertisers.remove(callback);
            } catch (Throwable th) {
                throw th;
            }
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

    /* JADX WARN: Code restructure failed: missing block: B:44:0x00b5, code lost:
        if (r25 > 255) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00b7, code lost:
        if (r25 == 0) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00bf, code lost:
        if (r18.mBluetoothAdapter.isLePeriodicAdvertisingSupported() == false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00c9, code lost:
        throw new java.lang.IllegalArgumentException("Can't use maxExtendedAdvertisingEvents with controller that don't support LE Extended Advertising");
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00ca, code lost:
        if (r24 < 0) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00cf, code lost:
        if (r24 > 65535) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00d2, code lost:
        r2 = r18.mBluetoothManager.getBluetoothGatt();
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00d9, code lost:
        r5 = wrap(r26, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00e3, code lost:
        if (r18.mCallbackWrappers.putIfAbsent(r26, r5) != null) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00ff, code lost:
        r2.startAdvertisingSet(r19, r20, r21, r22, r23, r24, r25, r5, r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0103, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0104, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0105, code lost:
        android.util.Log.e(android.bluetooth.le.BluetoothLeAdvertiser.TAG, "Failed to start advertising set - ", r0);
        postStartSetFailure(r0, r26, 4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0110, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0118, code lost:
        throw new java.lang.IllegalArgumentException("callback instance already associated with advertising");
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0119, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x011a, code lost:
        android.util.Log.e(android.bluetooth.le.BluetoothLeAdvertiser.TAG, "Failed to get Bluetooth gatt - ", r0);
        postStartSetFailure(r0, r26, 4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0128, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0143, code lost:
        throw new java.lang.IllegalArgumentException("duration out of range: " + r24);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void startAdvertisingSet(android.bluetooth.le.AdvertisingSetParameters r19, android.bluetooth.le.AdvertiseData r20, android.bluetooth.le.AdvertiseData r21, android.bluetooth.le.PeriodicAdvertisingParameters r22, android.bluetooth.le.AdvertiseData r23, int r24, int r25, android.bluetooth.le.AdvertisingSetCallback r26, java.util.List<android.bluetooth.BluetoothDevice> r27) {
        /*
            Method dump skipped, instructions count: 391
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.le.BluetoothLeAdvertiser.startAdvertisingSet(android.bluetooth.le.AdvertisingSetParameters, android.bluetooth.le.AdvertiseData, android.bluetooth.le.AdvertiseData, android.bluetooth.le.PeriodicAdvertisingParameters, android.bluetooth.le.AdvertiseData, int, int, android.bluetooth.le.AdvertisingSetCallback, java.util.List):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x00ad, code lost:
        if (r27 > 255) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00af, code lost:
        if (r27 == 0) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00b7, code lost:
        if (r20.mBluetoothAdapter.isLePeriodicAdvertisingSupported() == false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00c1, code lost:
        throw new java.lang.IllegalArgumentException("Can't use maxExtendedAdvertisingEvents with controller that don't support LE Extended Advertising");
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00c2, code lost:
        if (r26 < 0) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00c7, code lost:
        if (r26 > 65535) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00ca, code lost:
        r0 = r20.mBluetoothManager.getBluetoothGatt();
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00d3, code lost:
        if (r0 != null) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00d5, code lost:
        android.util.Log.e(android.bluetooth.le.BluetoothLeAdvertiser.TAG, "Bluetooth GATT is null");
        postStartSetFailure(r29, r28, 4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00dd, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00de, code lost:
        r5 = wrap(r28, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00e8, code lost:
        if (r20.mCallbackWrappers.putIfAbsent(r28, r5) != null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0106, code lost:
        r0.startAdvertisingSet(r21, r22, r23, r24, r25, r26, r27, r5, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x010a, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x010b, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x010c, code lost:
        android.util.Log.e(android.bluetooth.le.BluetoothLeAdvertiser.TAG, "Failed to start advertising set - ", r0);
        postStartSetFailure(r29, r28, 4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0119, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0121, code lost:
        throw new java.lang.IllegalArgumentException("callback instance already associated with advertising");
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0122, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0123, code lost:
        android.util.Log.e(android.bluetooth.le.BluetoothLeAdvertiser.TAG, "Failed to get Bluetooth GATT - ", r0);
        postStartSetFailure(r29, r28, 4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0131, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x014c, code lost:
        throw new java.lang.IllegalArgumentException("duration out of range: " + r26);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void startAdvertisingSet(android.bluetooth.le.AdvertisingSetParameters r21, android.bluetooth.le.AdvertiseData r22, android.bluetooth.le.AdvertiseData r23, android.bluetooth.le.PeriodicAdvertisingParameters r24, android.bluetooth.le.AdvertiseData r25, int r26, int r27, android.bluetooth.le.AdvertisingSetCallback r28, android.os.Handler r29) {
        /*
            Method dump skipped, instructions count: 400
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.le.BluetoothLeAdvertiser.startAdvertisingSet(android.bluetooth.le.AdvertisingSetParameters, android.bluetooth.le.AdvertiseData, android.bluetooth.le.AdvertiseData, android.bluetooth.le.PeriodicAdvertisingParameters, android.bluetooth.le.AdvertiseData, int, int, android.bluetooth.le.AdvertisingSetCallback, android.os.Handler):void");
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

    public void cleanup() {
        this.mLegacyAdvertisers.clear();
        this.mCallbackWrappers.clear();
        this.mAdvertisingSets.clear();
    }

    private int totalBytes(AdvertiseData data, boolean isFlagsIncluded) {
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
            size += uuidLen + 2 + byteLength(data.getServiceData().get(uuid2));
        }
        for (int i = 0; i < data.getManufacturerSpecificData().size(); i++) {
            size += byteLength(data.getManufacturerSpecificData().valueAt(i)) + 4;
        }
        if (data.getIncludeTxPowerLevel()) {
            size += 3;
        }
        if (data.getIncludeDeviceName() && this.mBluetoothAdapter.getName() != null) {
            return size + this.mBluetoothAdapter.getName().length() + 2;
        }
        return size;
    }

    private int byteLength(byte[] array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }

    IAdvertisingSetCallback wrap(final AdvertisingSetCallback callback, final Handler handler) {
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

    private void postStartSetFailure(Handler handler, final AdvertisingSetCallback callback, final int error) {
        handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.3
            @Override // java.lang.Runnable
            public void run() {
                callback.onAdvertisingSetStarted(null, 0, error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postStartFailure(final AdvertiseCallback callback, final int error) {
        this.mHandler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.4
            @Override // java.lang.Runnable
            public void run() {
                callback.onStartFailure(error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postStartSuccess(final AdvertiseCallback callback, final AdvertiseSettings settings) {
        this.mHandler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.5
            @Override // java.lang.Runnable
            public void run() {
                callback.onStartSuccess(settings);
            }
        });
    }
}
