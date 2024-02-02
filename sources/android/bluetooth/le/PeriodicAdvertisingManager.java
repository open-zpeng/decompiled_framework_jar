package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.bluetooth.le.IPeriodicAdvertisingCallback;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import java.util.IdentityHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class PeriodicAdvertisingManager {
    private static final int SKIP_MAX = 499;
    private static final int SKIP_MIN = 0;
    private static final int SYNC_STARTING = -1;
    private static final String TAG = "PeriodicAdvertisingManager";
    private static final int TIMEOUT_MAX = 16384;
    private static final int TIMEOUT_MIN = 10;
    private final IBluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Map<PeriodicAdvertisingCallback, IPeriodicAdvertisingCallback> mCallbackWrappers = new IdentityHashMap();

    public synchronized PeriodicAdvertisingManager(IBluetoothManager bluetoothManager) {
        this.mBluetoothManager = bluetoothManager;
    }

    public synchronized void registerSync(ScanResult scanResult, int skip, int timeout, PeriodicAdvertisingCallback callback) {
        registerSync(scanResult, skip, timeout, callback, null);
    }

    public synchronized void registerSync(ScanResult scanResult, int skip, int timeout, PeriodicAdvertisingCallback callback, Handler handler) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can't be null");
        }
        if (scanResult == null) {
            throw new IllegalArgumentException("scanResult can't be null");
        }
        if (scanResult.getAdvertisingSid() == 255) {
            throw new IllegalArgumentException("scanResult must contain a valid sid");
        }
        if (skip < 0 || skip > SKIP_MAX) {
            throw new IllegalArgumentException("timeout must be between 10 and 16384");
        }
        if (timeout < 10 || timeout > 16384) {
            throw new IllegalArgumentException("timeout must be between 10 and 16384");
        }
        try {
            IBluetoothGatt gatt = this.mBluetoothManager.getBluetoothGatt();
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            IPeriodicAdvertisingCallback wrapped = wrap(callback, handler);
            this.mCallbackWrappers.put(callback, wrapped);
            try {
                gatt.registerSync(scanResult, skip, timeout, wrapped);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to register sync - ", e);
            }
        } catch (RemoteException e2) {
            Log.e(TAG, "Failed to get Bluetooth gatt - ", e2);
            callback.onSyncEstablished(0, scanResult.getDevice(), scanResult.getAdvertisingSid(), skip, timeout, 2);
        }
    }

    public synchronized void unregisterSync(PeriodicAdvertisingCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can't be null");
        }
        try {
            IBluetoothGatt gatt = this.mBluetoothManager.getBluetoothGatt();
            IPeriodicAdvertisingCallback wrapper = this.mCallbackWrappers.remove(callback);
            if (wrapper == null) {
                throw new IllegalArgumentException("callback was not properly registered");
            }
            try {
                gatt.unregisterSync(wrapper);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to cancel sync creation - ", e);
            }
        } catch (RemoteException e2) {
            Log.e(TAG, "Failed to get Bluetooth gatt - ", e2);
        }
    }

    private synchronized IPeriodicAdvertisingCallback wrap(final PeriodicAdvertisingCallback callback, final Handler handler) {
        return new IPeriodicAdvertisingCallback.Stub() { // from class: android.bluetooth.le.PeriodicAdvertisingManager.1
            @Override // android.bluetooth.le.IPeriodicAdvertisingCallback
            public void onSyncEstablished(final int syncHandle, final BluetoothDevice device, final int advertisingSid, final int skip, final int timeout, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.PeriodicAdvertisingManager.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        callback.onSyncEstablished(syncHandle, device, advertisingSid, skip, timeout, status);
                        if (status != 0) {
                            PeriodicAdvertisingManager.this.mCallbackWrappers.remove(callback);
                        }
                    }
                });
            }

            @Override // android.bluetooth.le.IPeriodicAdvertisingCallback
            public void onPeriodicAdvertisingReport(final PeriodicAdvertisingReport report) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.PeriodicAdvertisingManager.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        callback.onPeriodicAdvertisingReport(report);
                    }
                });
            }

            @Override // android.bluetooth.le.IPeriodicAdvertisingCallback
            public void onSyncLost(final int syncHandle) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.PeriodicAdvertisingManager.1.3
                    @Override // java.lang.Runnable
                    public void run() {
                        callback.onSyncLost(syncHandle);
                        PeriodicAdvertisingManager.this.mCallbackWrappers.remove(callback);
                    }
                });
            }
        };
    }
}
