package android.bluetooth;

import android.app.job.JobInfo;
import android.bluetooth.IBluetoothGattServerCallback;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public final class BluetoothGattServer implements BluetoothProfile {
    private static final int CALLBACK_REG_TIMEOUT = 10000;
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothGattServer";
    private static final boolean VDBG = false;
    private BluetoothGattService mPendingService;
    private IBluetoothGatt mService;
    private int mTransport;
    private Object mServerIfLock = new Object();
    private final IBluetoothGattServerCallback mBluetoothGattServerCallback = new IBluetoothGattServerCallback.Stub() { // from class: android.bluetooth.BluetoothGattServer.1
        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onServerRegistered(int status, int serverIf) {
            Log.d(BluetoothGattServer.TAG, "onServerRegistered() - status=" + status + " serverIf=" + serverIf);
            synchronized (BluetoothGattServer.this.mServerIfLock) {
                if (BluetoothGattServer.this.mCallback != null) {
                    BluetoothGattServer.this.mServerIf = serverIf;
                    BluetoothGattServer.this.mServerIfLock.notify();
                } else {
                    Log.e(BluetoothGattServer.TAG, "onServerRegistered: mCallback is null");
                }
            }
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onServerConnectionState(int status, int serverIf, boolean connected, String address) {
            Log.d(BluetoothGattServer.TAG, "onServerConnectionState() - status=" + status + " serverIf=" + serverIf + " device=" + address);
            try {
                BluetoothGattServer.this.mCallback.onConnectionStateChange(BluetoothGattServer.this.mAdapter.getRemoteDevice(address), status, connected ? 2 : 0);
            } catch (Exception ex) {
                Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
            }
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onServiceAdded(int status, BluetoothGattService service) {
            Log.d(BluetoothGattServer.TAG, "onServiceAdded() - handle=" + service.getInstanceId() + " uuid=" + service.getUuid() + " status=" + status);
            if (BluetoothGattServer.this.mPendingService != null) {
                BluetoothGattService tmp = BluetoothGattServer.this.mPendingService;
                BluetoothGattServer.this.mPendingService = null;
                tmp.setInstanceId(service.getInstanceId());
                List<BluetoothGattCharacteristic> temp_chars = tmp.getCharacteristics();
                List<BluetoothGattCharacteristic> svc_chars = service.getCharacteristics();
                for (int i = 0; i < svc_chars.size(); i++) {
                    BluetoothGattCharacteristic temp_char = temp_chars.get(i);
                    BluetoothGattCharacteristic svc_char = svc_chars.get(i);
                    temp_char.setInstanceId(svc_char.getInstanceId());
                    List<BluetoothGattDescriptor> temp_descs = temp_char.getDescriptors();
                    List<BluetoothGattDescriptor> svc_descs = svc_char.getDescriptors();
                    for (int j = 0; j < svc_descs.size(); j++) {
                        temp_descs.get(j).setInstanceId(svc_descs.get(j).getInstanceId());
                    }
                }
                BluetoothGattServer.this.mServices.add(tmp);
                try {
                    BluetoothGattServer.this.mCallback.onServiceAdded(status, tmp);
                } catch (Exception ex) {
                    Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                }
            }
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onCharacteristicReadRequest(String address, int transId, int offset, boolean isLong, int handle) {
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattCharacteristic characteristic = BluetoothGattServer.this.getCharacteristicByHandle(handle);
            if (characteristic != null) {
                try {
                    BluetoothGattServer.this.mCallback.onCharacteristicReadRequest(device, transId, offset, characteristic);
                    return;
                } catch (Exception ex) {
                    Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                    return;
                }
            }
            Log.w(BluetoothGattServer.TAG, "onCharacteristicReadRequest() no char for handle " + handle);
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onDescriptorReadRequest(String address, int transId, int offset, boolean isLong, int handle) {
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattDescriptor descriptor = BluetoothGattServer.this.getDescriptorByHandle(handle);
            if (descriptor != null) {
                try {
                    BluetoothGattServer.this.mCallback.onDescriptorReadRequest(device, transId, offset, descriptor);
                    return;
                } catch (Exception ex) {
                    Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                    return;
                }
            }
            Log.w(BluetoothGattServer.TAG, "onDescriptorReadRequest() no desc for handle " + handle);
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onCharacteristicWriteRequest(String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int handle, byte[] value) {
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattCharacteristic characteristic = BluetoothGattServer.this.getCharacteristicByHandle(handle);
            if (characteristic != null) {
                try {
                    BluetoothGattServer.this.mCallback.onCharacteristicWriteRequest(device, transId, characteristic, isPrep, needRsp, offset, value);
                    return;
                } catch (Exception ex) {
                    Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                    return;
                }
            }
            Log.w(BluetoothGattServer.TAG, "onCharacteristicWriteRequest() no char for handle " + handle);
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onDescriptorWriteRequest(String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int handle, byte[] value) {
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattDescriptor descriptor = BluetoothGattServer.this.getDescriptorByHandle(handle);
            if (descriptor != null) {
                try {
                    BluetoothGattServer.this.mCallback.onDescriptorWriteRequest(device, transId, descriptor, isPrep, needRsp, offset, value);
                    return;
                } catch (Exception ex) {
                    Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                    return;
                }
            }
            Log.w(BluetoothGattServer.TAG, "onDescriptorWriteRequest() no desc for handle " + handle);
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onExecuteWrite(String address, int transId, boolean execWrite) {
            Log.d(BluetoothGattServer.TAG, "onExecuteWrite() - device=" + address + ", transId=" + transId + "execWrite=" + execWrite);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device == null) {
                return;
            }
            try {
                BluetoothGattServer.this.mCallback.onExecuteWrite(device, transId, execWrite);
            } catch (Exception ex) {
                Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
            }
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onNotificationSent(String address, int status) {
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device == null) {
                return;
            }
            try {
                BluetoothGattServer.this.mCallback.onNotificationSent(device, status);
            } catch (Exception ex) {
                Log.w(BluetoothGattServer.TAG, "Unhandled exception: " + ex);
            }
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onMtuChanged(String address, int mtu) {
            Log.d(BluetoothGattServer.TAG, "onMtuChanged() - device=" + address + ", mtu=" + mtu);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device == null) {
                return;
            }
            try {
                BluetoothGattServer.this.mCallback.onMtuChanged(device, mtu);
            } catch (Exception ex) {
                Log.w(BluetoothGattServer.TAG, "Unhandled exception: " + ex);
            }
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onPhyUpdate(String address, int txPhy, int rxPhy, int status) {
            Log.d(BluetoothGattServer.TAG, "onPhyUpdate() - device=" + address + ", txPHy=" + txPhy + ", rxPHy=" + rxPhy);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device == null) {
                return;
            }
            try {
                BluetoothGattServer.this.mCallback.onPhyUpdate(device, txPhy, rxPhy, status);
            } catch (Exception ex) {
                Log.w(BluetoothGattServer.TAG, "Unhandled exception: " + ex);
            }
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onPhyRead(String address, int txPhy, int rxPhy, int status) {
            Log.d(BluetoothGattServer.TAG, "onPhyUpdate() - device=" + address + ", txPHy=" + txPhy + ", rxPHy=" + rxPhy);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device == null) {
                return;
            }
            try {
                BluetoothGattServer.this.mCallback.onPhyRead(device, txPhy, rxPhy, status);
            } catch (Exception ex) {
                Log.w(BluetoothGattServer.TAG, "Unhandled exception: " + ex);
            }
        }

        @Override // android.bluetooth.IBluetoothGattServerCallback
        public void onConnectionUpdated(String address, int interval, int latency, int timeout, int status) {
            Log.d(BluetoothGattServer.TAG, "onConnectionUpdated() - Device=" + address + " interval=" + interval + " latency=" + latency + " timeout=" + timeout + " status=" + status);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device == null) {
                return;
            }
            try {
                BluetoothGattServer.this.mCallback.onConnectionUpdated(device, interval, latency, timeout, status);
            } catch (Exception ex) {
                Log.w(BluetoothGattServer.TAG, "Unhandled exception: " + ex);
            }
        }
    };
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothGattServerCallback mCallback = null;
    private int mServerIf = 0;
    private List<BluetoothGattService> mServices = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BluetoothGattServer(IBluetoothGatt iGatt, int transport) {
        this.mService = iGatt;
        this.mTransport = transport;
    }

    synchronized BluetoothGattCharacteristic getCharacteristicByHandle(int handle) {
        for (BluetoothGattService svc : this.mServices) {
            for (BluetoothGattCharacteristic charac : svc.getCharacteristics()) {
                if (charac.getInstanceId() == handle) {
                    return charac;
                }
            }
        }
        return null;
    }

    synchronized BluetoothGattDescriptor getDescriptorByHandle(int handle) {
        for (BluetoothGattService svc : this.mServices) {
            for (BluetoothGattCharacteristic charac : svc.getCharacteristics()) {
                for (BluetoothGattDescriptor desc : charac.getDescriptors()) {
                    if (desc.getInstanceId() == handle) {
                        return desc;
                    }
                }
            }
        }
        return null;
    }

    public void close() {
        Log.d(TAG, "close()");
        unregisterCallback();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean registerCallback(BluetoothGattServerCallback callback) {
        Log.d(TAG, "registerCallback()");
        if (this.mService == null) {
            Log.e(TAG, "GATT service not available");
            return false;
        }
        UUID uuid = UUID.randomUUID();
        Log.d(TAG, "registerCallback() - UUID=" + uuid);
        synchronized (this.mServerIfLock) {
            if (this.mCallback != null) {
                Log.e(TAG, "App can register callback only once");
                return false;
            }
            this.mCallback = callback;
            try {
                this.mService.registerServer(new ParcelUuid(uuid), this.mBluetoothGattServerCallback);
                try {
                    this.mServerIfLock.wait(JobInfo.MIN_BACKOFF_MILLIS);
                } catch (InterruptedException e) {
                    Log.e(TAG, "" + e);
                    this.mCallback = null;
                }
                if (this.mServerIf == 0) {
                    this.mCallback = null;
                    return false;
                }
                return true;
            } catch (RemoteException e2) {
                Log.e(TAG, "", e2);
                this.mCallback = null;
                return false;
            }
        }
    }

    private synchronized void unregisterCallback() {
        Log.d(TAG, "unregisterCallback() - mServerIf=" + this.mServerIf);
        if (this.mService == null || this.mServerIf == 0) {
            return;
        }
        try {
            this.mCallback = null;
            this.mService.unregisterServer(this.mServerIf);
            this.mServerIf = 0;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    synchronized BluetoothGattService getService(UUID uuid, int instanceId, int type) {
        for (BluetoothGattService svc : this.mServices) {
            if (svc.getType() == type && svc.getInstanceId() == instanceId && svc.getUuid().equals(uuid)) {
                return svc;
            }
        }
        return null;
    }

    public boolean connect(BluetoothDevice device, boolean autoConnect) {
        Log.d(TAG, "connect() - device: " + device.getAddress() + ", auto: " + autoConnect);
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        try {
            this.mService.serverConnect(this.mServerIf, device.getAddress(), !autoConnect, this.mTransport);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public void cancelConnection(BluetoothDevice device) {
        Log.d(TAG, "cancelConnection() - device: " + device.getAddress());
        if (this.mService == null || this.mServerIf == 0) {
            return;
        }
        try {
            this.mService.serverDisconnect(this.mServerIf, device.getAddress());
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    public void setPreferredPhy(BluetoothDevice device, int txPhy, int rxPhy, int phyOptions) {
        try {
            this.mService.serverSetPreferredPhy(this.mServerIf, device.getAddress(), txPhy, rxPhy, phyOptions);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    public void readPhy(BluetoothDevice device) {
        try {
            this.mService.serverReadPhy(this.mServerIf, device.getAddress());
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    public boolean sendResponse(BluetoothDevice device, int requestId, int status, int offset, byte[] value) {
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        try {
            this.mService.sendResponse(this.mServerIf, device.getAddress(), requestId, status, offset, value);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean notifyCharacteristicChanged(BluetoothDevice device, BluetoothGattCharacteristic characteristic, boolean confirm) {
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        BluetoothGattService service = characteristic.getService();
        if (service == null) {
            return false;
        }
        if (characteristic.getValue() == null) {
            throw new IllegalArgumentException("Chracteristic value is empty. Use BluetoothGattCharacteristic#setvalue to update");
        }
        try {
            this.mService.sendNotification(this.mServerIf, device.getAddress(), characteristic.getInstanceId(), confirm, characteristic.getValue());
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean addService(BluetoothGattService service) {
        Log.d(TAG, "addService() - service: " + service.getUuid());
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        this.mPendingService = service;
        try {
            this.mService.addService(this.mServerIf, service);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean removeService(BluetoothGattService service) {
        BluetoothGattService intService;
        Log.d(TAG, "removeService() - service: " + service.getUuid());
        if (this.mService == null || this.mServerIf == 0 || (intService = getService(service.getUuid(), service.getInstanceId(), service.getType())) == null) {
            return false;
        }
        try {
            this.mService.removeService(this.mServerIf, service.getInstanceId());
            this.mServices.remove(intService);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public void clearServices() {
        Log.d(TAG, "clearServices()");
        if (this.mService == null || this.mServerIf == 0) {
            return;
        }
        try {
            this.mService.clearServices(this.mServerIf);
            this.mServices.clear();
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    public List<BluetoothGattService> getServices() {
        return this.mServices;
    }

    public BluetoothGattService getService(UUID uuid) {
        for (BluetoothGattService service : this.mServices) {
            if (service.getUuid().equals(uuid)) {
                return service;
            }
        }
        return null;
    }

    @Override // android.bluetooth.BluetoothProfile
    public int getConnectionState(BluetoothDevice device) {
        throw new UnsupportedOperationException("Use BluetoothManager#getConnectionState instead.");
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        throw new UnsupportedOperationException("Use BluetoothManager#getConnectedDevices instead.");
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        throw new UnsupportedOperationException("Use BluetoothManager#getDevicesMatchingConnectionStates instead.");
    }
}
