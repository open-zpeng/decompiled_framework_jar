package android.bluetooth;

import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class BluetoothManager {
    private static final boolean DBG = false;
    private static final String TAG = "BluetoothManager";
    private final BluetoothAdapter mAdapter;

    public BluetoothManager(Context context) {
        Context context2 = context.getApplicationContext();
        if (context2 == null) {
            throw new IllegalArgumentException("context not associated with any application (using a mock context?)");
        }
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothAdapter bluetoothAdapter = this.mAdapter;
        if (bluetoothAdapter != null) {
            bluetoothAdapter.setContext(context2);
        }
    }

    public BluetoothAdapter getAdapter() {
        return this.mAdapter;
    }

    public int getConnectionState(BluetoothDevice device, int profile) {
        List<BluetoothDevice> connectedDevices = getConnectedDevices(profile);
        for (BluetoothDevice connectedDevice : connectedDevices) {
            if (device.equals(connectedDevice)) {
                return 2;
            }
        }
        return 0;
    }

    public List<BluetoothDevice> getConnectedDevices(int profile) {
        if (profile != 7 && profile != 8) {
            throw new IllegalArgumentException("Profile not supported: " + profile);
        }
        List<BluetoothDevice> connectedDevices = new ArrayList<>();
        try {
            IBluetoothManager managerService = this.mAdapter.getBluetoothManager();
            IBluetoothGatt iGatt = managerService.getBluetoothGatt();
            return iGatt == null ? connectedDevices : iGatt.getDevicesMatchingConnectionStates(new int[]{2});
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return connectedDevices;
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int profile, int[] states) {
        if (profile != 7 && profile != 8) {
            throw new IllegalArgumentException("Profile not supported: " + profile);
        }
        List<BluetoothDevice> devices = new ArrayList<>();
        try {
            IBluetoothManager managerService = this.mAdapter.getBluetoothManager();
            IBluetoothGatt iGatt = managerService.getBluetoothGatt();
            return iGatt == null ? devices : iGatt.getDevicesMatchingConnectionStates(states);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return devices;
        }
    }

    public BluetoothGattServer openGattServer(Context context, BluetoothGattServerCallback callback) {
        return openGattServer(context, callback, 0);
    }

    public BluetoothGattServer openGattServer(Context context, BluetoothGattServerCallback callback, int transport) {
        if (context == null || callback == null) {
            throw new IllegalArgumentException("null parameter: " + context + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + callback);
        }
        try {
            IBluetoothManager managerService = this.mAdapter.getBluetoothManager();
            IBluetoothGatt iGatt = managerService.getBluetoothGatt();
            if (iGatt == null) {
                Log.e(TAG, "Fail to get GATT Server connection");
                return null;
            }
            BluetoothGattServer mGattServer = new BluetoothGattServer(iGatt, transport);
            Boolean regStatus = Boolean.valueOf(mGattServer.registerCallback(callback));
            if (regStatus.booleanValue()) {
                return mGattServer;
            }
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }
}
