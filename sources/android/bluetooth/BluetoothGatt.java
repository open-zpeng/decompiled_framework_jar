package android.bluetooth;

import android.bluetooth.IBluetoothGattCallback;
import android.os.Handler;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public final class BluetoothGatt implements BluetoothProfile {
    static final int AUTHENTICATION_MITM = 2;
    static final int AUTHENTICATION_NONE = 0;
    static final int AUTHENTICATION_NO_MITM = 1;
    private static final int AUTH_RETRY_STATE_IDLE = 0;
    private static final int AUTH_RETRY_STATE_MITM = 2;
    private static final int AUTH_RETRY_STATE_NO_MITM = 1;
    public static final int CONNECTION_PRIORITY_BALANCED = 0;
    public static final int CONNECTION_PRIORITY_HIGH = 1;
    public static final int CONNECTION_PRIORITY_LOW_POWER = 2;
    private static final int CONN_STATE_CLOSED = 4;
    private static final int CONN_STATE_CONNECTED = 2;
    private static final int CONN_STATE_CONNECTING = 1;
    private static final int CONN_STATE_DISCONNECTING = 3;
    private static final int CONN_STATE_IDLE = 0;
    private static final boolean DBG = true;
    public static final int GATT_CONNECTION_CONGESTED = 143;
    public static final int GATT_FAILURE = 257;
    public static final int GATT_INSUFFICIENT_AUTHENTICATION = 5;
    public static final int GATT_INSUFFICIENT_ENCRYPTION = 15;
    public static final int GATT_INVALID_ATTRIBUTE_LENGTH = 13;
    public static final int GATT_INVALID_OFFSET = 7;
    public static final int GATT_READ_NOT_PERMITTED = 2;
    public static final int GATT_REQUEST_NOT_SUPPORTED = 6;
    public static final int GATT_SUCCESS = 0;
    public static final int GATT_WRITE_NOT_PERMITTED = 3;
    private static final String TAG = "BluetoothGatt";
    private static final boolean VDBG = false;
    public protected boolean mAutoConnect;
    public protected volatile BluetoothGattCallback mCallback;
    public protected int mClientIf;
    private BluetoothDevice mDevice;
    private Handler mHandler;
    private boolean mOpportunistic;
    private int mPhy;
    public protected IBluetoothGatt mService;
    public protected int mTransport;
    private final Object mStateLock = new Object();
    public protected Boolean mDeviceBusy = false;
    private final IBluetoothGattCallback mBluetoothGattCallback = new IBluetoothGattCallback.Stub() { // from class: android.bluetooth.BluetoothGatt.1
        @Override // android.bluetooth.IBluetoothGattCallback
        public void onClientRegistered(int status, int clientIf) {
            Log.d(BluetoothGatt.TAG, "onClientRegistered() - status=" + status + " clientIf=" + clientIf);
            BluetoothGatt.this.mClientIf = clientIf;
            if (status != 0) {
                BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                        if (callback != null) {
                            callback.onConnectionStateChange(BluetoothGatt.this, 257, 0);
                        }
                    }
                });
                synchronized (BluetoothGatt.this.mStateLock) {
                    BluetoothGatt.this.mConnState = 0;
                }
                return;
            }
            try {
                BluetoothGatt.this.mService.clientConnect(BluetoothGatt.this.mClientIf, BluetoothGatt.this.mDevice.getAddress(), !BluetoothGatt.this.mAutoConnect, BluetoothGatt.this.mTransport, BluetoothGatt.this.mOpportunistic, BluetoothGatt.this.mPhy);
            } catch (RemoteException e) {
                Log.e(BluetoothGatt.TAG, "", e);
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onPhyUpdate(String address, final int txPhy, final int rxPhy, final int status) {
            Log.d(BluetoothGatt.TAG, "onPhyUpdate() - status=" + status + " address=" + address + " txPhy=" + txPhy + " rxPhy=" + rxPhy);
            if (address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                        if (callback != null) {
                            callback.onPhyUpdate(BluetoothGatt.this, txPhy, rxPhy, status);
                        }
                    }
                });
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onPhyRead(String address, final int txPhy, final int rxPhy, final int status) {
            Log.d(BluetoothGatt.TAG, "onPhyRead() - status=" + status + " address=" + address + " txPhy=" + txPhy + " rxPhy=" + rxPhy);
            if (address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.3
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                        if (callback != null) {
                            callback.onPhyRead(BluetoothGatt.this, txPhy, rxPhy, status);
                        }
                    }
                });
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onClientConnectionState(final int status, int clientIf, boolean connected, String address) {
            Log.d(BluetoothGatt.TAG, "onClientConnectionState() - status=" + status + " clientIf=" + clientIf + " device=" + address);
            if (!address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                return;
            }
            final int profileState = connected ? 2 : 0;
            BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.4
                @Override // java.lang.Runnable
                public void run() {
                    BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                    if (callback != null) {
                        callback.onConnectionStateChange(BluetoothGatt.this, status, profileState);
                    }
                }
            });
            synchronized (BluetoothGatt.this.mStateLock) {
                try {
                    if (connected) {
                        BluetoothGatt.this.mConnState = 2;
                    } else {
                        BluetoothGatt.this.mConnState = 0;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            synchronized (BluetoothGatt.this.mDeviceBusy) {
                BluetoothGatt.this.mDeviceBusy = false;
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onSearchComplete(String address, List<BluetoothGattService> services, final int status) {
            Log.d(BluetoothGatt.TAG, "onSearchComplete() = Device=" + address + " Status=" + status);
            if (!address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                return;
            }
            for (BluetoothGattService s : services) {
                s.setDevice(BluetoothGatt.this.mDevice);
            }
            BluetoothGatt.this.mServices.addAll(services);
            for (BluetoothGattService fixedService : BluetoothGatt.this.mServices) {
                ArrayList<BluetoothGattService> includedServices = new ArrayList<>(fixedService.getIncludedServices());
                fixedService.getIncludedServices().clear();
                Iterator<BluetoothGattService> it = includedServices.iterator();
                while (it.hasNext()) {
                    BluetoothGattService brokenRef = it.next();
                    BluetoothGattService includedService = BluetoothGatt.this.getService(BluetoothGatt.this.mDevice, brokenRef.getUuid(), brokenRef.getInstanceId());
                    if (includedService != null) {
                        fixedService.addIncludedService(includedService);
                    } else {
                        Log.e(BluetoothGatt.TAG, "Broken GATT database: can't find included service.");
                    }
                }
            }
            BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.5
                @Override // java.lang.Runnable
                public void run() {
                    BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                    if (callback != null) {
                        callback.onServicesDiscovered(BluetoothGatt.this, status);
                    }
                }
            });
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onCharacteristicRead(String address, final int status, int handle, final byte[] value) {
            if (address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                synchronized (BluetoothGatt.this.mDeviceBusy) {
                    BluetoothGatt.this.mDeviceBusy = false;
                }
                if (status == 5 || status == 15) {
                    if (BluetoothGatt.this.mAuthRetryState != 2) {
                        try {
                            int authReq = BluetoothGatt.this.mAuthRetryState == 0 ? 1 : 2;
                            BluetoothGatt.this.mService.readCharacteristic(BluetoothGatt.this.mClientIf, address, handle, authReq);
                            BluetoothGatt.access$1308(BluetoothGatt.this);
                            return;
                        } catch (RemoteException e) {
                            Log.e(BluetoothGatt.TAG, "", e);
                        }
                    }
                }
                BluetoothGatt.this.mAuthRetryState = 0;
                final BluetoothGattCharacteristic characteristic = BluetoothGatt.this.getCharacteristicById(BluetoothGatt.this.mDevice, handle);
                if (characteristic != null) {
                    BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.6
                        @Override // java.lang.Runnable
                        public void run() {
                            BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                            if (callback != null) {
                                if (status == 0) {
                                    characteristic.setValue(value);
                                }
                                callback.onCharacteristicRead(BluetoothGatt.this, characteristic, status);
                            }
                        }
                    });
                } else {
                    Log.w(BluetoothGatt.TAG, "onCharacteristicRead() failed to find characteristic!");
                }
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onCharacteristicWrite(String address, final int status, int handle) {
            if (address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                synchronized (BluetoothGatt.this.mDeviceBusy) {
                    BluetoothGatt.this.mDeviceBusy = false;
                }
                final BluetoothGattCharacteristic characteristic = BluetoothGatt.this.getCharacteristicById(BluetoothGatt.this.mDevice, handle);
                if (characteristic == null) {
                    return;
                }
                if (status == 5 || status == 15) {
                    int i = 2;
                    if (BluetoothGatt.this.mAuthRetryState != 2) {
                        try {
                            if (BluetoothGatt.this.mAuthRetryState == 0) {
                                i = 1;
                            }
                            int authReq = i;
                            BluetoothGatt.this.mService.writeCharacteristic(BluetoothGatt.this.mClientIf, address, handle, characteristic.getWriteType(), authReq, characteristic.getValue());
                            BluetoothGatt.access$1308(BluetoothGatt.this);
                            return;
                        } catch (RemoteException e) {
                            Log.e(BluetoothGatt.TAG, "", e);
                        }
                    }
                }
                BluetoothGatt.this.mAuthRetryState = 0;
                BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.7
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                        if (callback != null) {
                            callback.onCharacteristicWrite(BluetoothGatt.this, characteristic, status);
                        }
                    }
                });
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onNotify(String address, int handle, final byte[] value) {
            final BluetoothGattCharacteristic characteristic;
            if (address.equals(BluetoothGatt.this.mDevice.getAddress()) && (characteristic = BluetoothGatt.this.getCharacteristicById(BluetoothGatt.this.mDevice, handle)) != null) {
                BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.8
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                        if (callback != null) {
                            characteristic.setValue(value);
                            callback.onCharacteristicChanged(BluetoothGatt.this, characteristic);
                        }
                    }
                });
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onDescriptorRead(String address, final int status, int handle, final byte[] value) {
            if (address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                synchronized (BluetoothGatt.this.mDeviceBusy) {
                    BluetoothGatt.this.mDeviceBusy = false;
                }
                final BluetoothGattDescriptor descriptor = BluetoothGatt.this.getDescriptorById(BluetoothGatt.this.mDevice, handle);
                if (descriptor == null) {
                    return;
                }
                if (status == 5 || status == 15) {
                    if (BluetoothGatt.this.mAuthRetryState != 2) {
                        try {
                            int authReq = BluetoothGatt.this.mAuthRetryState == 0 ? 1 : 2;
                            BluetoothGatt.this.mService.readDescriptor(BluetoothGatt.this.mClientIf, address, handle, authReq);
                            BluetoothGatt.access$1308(BluetoothGatt.this);
                            return;
                        } catch (RemoteException e) {
                            Log.e(BluetoothGatt.TAG, "", e);
                        }
                    }
                }
                BluetoothGatt.this.mAuthRetryState = 0;
                BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.9
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                        if (callback != null) {
                            if (status == 0) {
                                descriptor.setValue(value);
                            }
                            callback.onDescriptorRead(BluetoothGatt.this, descriptor, status);
                        }
                    }
                });
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onDescriptorWrite(String address, final int status, int handle) {
            if (address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                synchronized (BluetoothGatt.this.mDeviceBusy) {
                    BluetoothGatt.this.mDeviceBusy = false;
                }
                final BluetoothGattDescriptor descriptor = BluetoothGatt.this.getDescriptorById(BluetoothGatt.this.mDevice, handle);
                if (descriptor == null) {
                    return;
                }
                if (status == 5 || status == 15) {
                    int i = 2;
                    if (BluetoothGatt.this.mAuthRetryState != 2) {
                        try {
                            if (BluetoothGatt.this.mAuthRetryState == 0) {
                                i = 1;
                            }
                            int authReq = i;
                            BluetoothGatt.this.mService.writeDescriptor(BluetoothGatt.this.mClientIf, address, handle, authReq, descriptor.getValue());
                            BluetoothGatt.access$1308(BluetoothGatt.this);
                            return;
                        } catch (RemoteException e) {
                            Log.e(BluetoothGatt.TAG, "", e);
                        }
                    }
                }
                BluetoothGatt.this.mAuthRetryState = 0;
                BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.10
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                        if (callback != null) {
                            callback.onDescriptorWrite(BluetoothGatt.this, descriptor, status);
                        }
                    }
                });
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onExecuteWrite(String address, final int status) {
            if (address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                synchronized (BluetoothGatt.this.mDeviceBusy) {
                    BluetoothGatt.this.mDeviceBusy = false;
                }
                BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.11
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                        if (callback != null) {
                            callback.onReliableWriteCompleted(BluetoothGatt.this, status);
                        }
                    }
                });
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onReadRemoteRssi(String address, final int rssi, final int status) {
            if (address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.12
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                        if (callback != null) {
                            callback.onReadRemoteRssi(BluetoothGatt.this, rssi, status);
                        }
                    }
                });
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onConfigureMTU(String address, final int mtu, final int status) {
            Log.d(BluetoothGatt.TAG, "onConfigureMTU() - Device=" + address + " mtu=" + mtu + " status=" + status);
            if (address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.13
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                        if (callback != null) {
                            callback.onMtuChanged(BluetoothGatt.this, mtu, status);
                        }
                    }
                });
            }
        }

        @Override // android.bluetooth.IBluetoothGattCallback
        public void onConnectionUpdated(String address, final int interval, final int latency, final int timeout, final int status) {
            Log.d(BluetoothGatt.TAG, "onConnectionUpdated() - Device=" + address + " interval=" + interval + " latency=" + latency + " timeout=" + timeout + " status=" + status);
            if (address.equals(BluetoothGatt.this.mDevice.getAddress())) {
                BluetoothGatt.this.runOrQueueCallback(new Runnable() { // from class: android.bluetooth.BluetoothGatt.1.14
                    @Override // java.lang.Runnable
                    public void run() {
                        BluetoothGattCallback callback = BluetoothGatt.this.mCallback;
                        if (callback != null) {
                            callback.onConnectionUpdated(BluetoothGatt.this, interval, latency, timeout, status);
                        }
                    }
                });
            }
        }
    };
    private List<BluetoothGattService> mServices = new ArrayList();
    private int mConnState = 0;
    public protected int mAuthRetryState = 0;

    static /* synthetic */ int access$1308(BluetoothGatt x0) {
        int i = x0.mAuthRetryState;
        x0.mAuthRetryState = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BluetoothGatt(IBluetoothGatt iGatt, BluetoothDevice device, int transport, boolean opportunistic, int phy) {
        this.mService = iGatt;
        this.mDevice = device;
        this.mTransport = transport;
        this.mPhy = phy;
        this.mOpportunistic = opportunistic;
    }

    public void close() {
        Log.d(TAG, "close()");
        unregisterApp();
        this.mConnState = 4;
        this.mAuthRetryState = 0;
    }

    synchronized BluetoothGattService getService(BluetoothDevice device, UUID uuid, int instanceId) {
        for (BluetoothGattService svc : this.mServices) {
            if (svc.getDevice().equals(device) && svc.getInstanceId() == instanceId && svc.getUuid().equals(uuid)) {
                return svc;
            }
        }
        return null;
    }

    synchronized BluetoothGattCharacteristic getCharacteristicById(BluetoothDevice device, int instanceId) {
        for (BluetoothGattService svc : this.mServices) {
            for (BluetoothGattCharacteristic charac : svc.getCharacteristics()) {
                if (charac.getInstanceId() == instanceId) {
                    return charac;
                }
            }
        }
        return null;
    }

    synchronized BluetoothGattDescriptor getDescriptorById(BluetoothDevice device, int instanceId) {
        for (BluetoothGattService svc : this.mServices) {
            for (BluetoothGattCharacteristic charac : svc.getCharacteristics()) {
                for (BluetoothGattDescriptor desc : charac.getDescriptors()) {
                    if (desc.getInstanceId() == instanceId) {
                        return desc;
                    }
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void runOrQueueCallback(Runnable cb) {
        if (this.mHandler == null) {
            try {
                cb.run();
                return;
            } catch (Exception ex) {
                Log.w(TAG, "Unhandled exception in callback", ex);
                return;
            }
        }
        this.mHandler.post(cb);
    }

    private synchronized boolean registerApp(BluetoothGattCallback callback, Handler handler) {
        Log.d(TAG, "registerApp()");
        if (this.mService == null) {
            return false;
        }
        this.mCallback = callback;
        this.mHandler = handler;
        UUID uuid = UUID.randomUUID();
        Log.d(TAG, "registerApp() - UUID=" + uuid);
        try {
            this.mService.registerClient(new ParcelUuid(uuid), this.mBluetoothGattCallback);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public protected void unregisterApp() {
        Log.d(TAG, "unregisterApp() - mClientIf=" + this.mClientIf);
        if (this.mService == null || this.mClientIf == 0) {
            return;
        }
        try {
            this.mCallback = null;
            this.mService.unregisterClient(this.mClientIf);
            this.mClientIf = 0;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    public private protected boolean connect(Boolean autoConnect, BluetoothGattCallback callback, Handler handler) {
        Log.d(TAG, "connect() - device: " + this.mDevice.getAddress() + ", auto: " + autoConnect);
        synchronized (this.mStateLock) {
            if (this.mConnState != 0) {
                throw new IllegalStateException("Not idle");
            }
            this.mConnState = 1;
        }
        this.mAutoConnect = autoConnect.booleanValue();
        if (registerApp(callback, handler)) {
            return true;
        }
        synchronized (this.mStateLock) {
            this.mConnState = 0;
        }
        Log.e(TAG, "Failed to register callback");
        return false;
    }

    public void disconnect() {
        Log.d(TAG, "cancelOpen() - device: " + this.mDevice.getAddress());
        if (this.mService == null || this.mClientIf == 0) {
            return;
        }
        try {
            this.mService.clientDisconnect(this.mClientIf, this.mDevice.getAddress());
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    public boolean connect() {
        try {
            this.mService.clientConnect(this.mClientIf, this.mDevice.getAddress(), false, this.mTransport, this.mOpportunistic, this.mPhy);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public void setPreferredPhy(int txPhy, int rxPhy, int phyOptions) {
        try {
            this.mService.clientSetPreferredPhy(this.mClientIf, this.mDevice.getAddress(), txPhy, rxPhy, phyOptions);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    public void readPhy() {
        try {
            this.mService.clientReadPhy(this.mClientIf, this.mDevice.getAddress());
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public boolean discoverServices() {
        Log.d(TAG, "discoverServices() - device: " + this.mDevice.getAddress());
        if (this.mService == null || this.mClientIf == 0) {
            return false;
        }
        this.mServices.clear();
        try {
            this.mService.discoverServices(this.mClientIf, this.mDevice.getAddress());
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public synchronized boolean discoverServiceByUuid(UUID uuid) {
        Log.d(TAG, "discoverServiceByUuid() - device: " + this.mDevice.getAddress());
        if (this.mService == null || this.mClientIf == 0) {
            return false;
        }
        this.mServices.clear();
        try {
            this.mService.discoverServiceByUuid(this.mClientIf, this.mDevice.getAddress(), new ParcelUuid(uuid));
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public List<BluetoothGattService> getServices() {
        List<BluetoothGattService> result = new ArrayList<>();
        for (BluetoothGattService service : this.mServices) {
            if (service.getDevice().equals(this.mDevice)) {
                result.add(service);
            }
        }
        return result;
    }

    public BluetoothGattService getService(UUID uuid) {
        for (BluetoothGattService service : this.mServices) {
            if (service.getDevice().equals(this.mDevice) && service.getUuid().equals(uuid)) {
                return service;
            }
        }
        return null;
    }

    public boolean readCharacteristic(BluetoothGattCharacteristic characteristic) {
        BluetoothGattService service;
        BluetoothDevice device;
        if ((characteristic.getProperties() & 2) == 0 || this.mService == null || this.mClientIf == 0 || (service = characteristic.getService()) == null || (device = service.getDevice()) == null) {
            return false;
        }
        synchronized (this.mDeviceBusy) {
            if (this.mDeviceBusy.booleanValue()) {
                return false;
            }
            this.mDeviceBusy = true;
            try {
                this.mService.readCharacteristic(this.mClientIf, device.getAddress(), characteristic.getInstanceId(), 0);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                this.mDeviceBusy = false;
                return false;
            }
        }
    }

    public synchronized boolean readUsingCharacteristicUuid(UUID uuid, int startHandle, int endHandle) {
        if (this.mService == null || this.mClientIf == 0) {
            return false;
        }
        synchronized (this.mDeviceBusy) {
            if (this.mDeviceBusy.booleanValue()) {
                return false;
            }
            this.mDeviceBusy = true;
            try {
                this.mService.readUsingCharacteristicUuid(this.mClientIf, this.mDevice.getAddress(), new ParcelUuid(uuid), startHandle, endHandle, 0);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                this.mDeviceBusy = false;
                return false;
            }
        }
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        BluetoothGattService service;
        BluetoothDevice device;
        if (((characteristic.getProperties() & 8) == 0 && (characteristic.getProperties() & 4) == 0) || this.mService == null || this.mClientIf == 0 || characteristic.getValue() == null || (service = characteristic.getService()) == null || (device = service.getDevice()) == null) {
            return false;
        }
        synchronized (this.mDeviceBusy) {
            if (this.mDeviceBusy.booleanValue()) {
                return false;
            }
            this.mDeviceBusy = true;
            try {
                this.mService.writeCharacteristic(this.mClientIf, device.getAddress(), characteristic.getInstanceId(), characteristic.getWriteType(), 0, characteristic.getValue());
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                this.mDeviceBusy = false;
                return false;
            }
        }
    }

    public boolean readDescriptor(BluetoothGattDescriptor descriptor) {
        BluetoothGattCharacteristic characteristic;
        BluetoothGattService service;
        BluetoothDevice device;
        if (this.mService == null || this.mClientIf == 0 || (characteristic = descriptor.getCharacteristic()) == null || (service = characteristic.getService()) == null || (device = service.getDevice()) == null) {
            return false;
        }
        synchronized (this.mDeviceBusy) {
            if (this.mDeviceBusy.booleanValue()) {
                return false;
            }
            this.mDeviceBusy = true;
            try {
                this.mService.readDescriptor(this.mClientIf, device.getAddress(), descriptor.getInstanceId(), 0);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                this.mDeviceBusy = false;
                return false;
            }
        }
    }

    public boolean writeDescriptor(BluetoothGattDescriptor descriptor) {
        BluetoothGattCharacteristic characteristic;
        BluetoothGattService service;
        BluetoothDevice device;
        if (this.mService == null || this.mClientIf == 0 || descriptor.getValue() == null || (characteristic = descriptor.getCharacteristic()) == null || (service = characteristic.getService()) == null || (device = service.getDevice()) == null) {
            return false;
        }
        synchronized (this.mDeviceBusy) {
            if (this.mDeviceBusy.booleanValue()) {
                return false;
            }
            this.mDeviceBusy = true;
            try {
                this.mService.writeDescriptor(this.mClientIf, device.getAddress(), descriptor.getInstanceId(), 0, descriptor.getValue());
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                this.mDeviceBusy = false;
                return false;
            }
        }
    }

    public boolean beginReliableWrite() {
        if (this.mService == null || this.mClientIf == 0) {
            return false;
        }
        try {
            this.mService.beginReliableWrite(this.mClientIf, this.mDevice.getAddress());
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean executeReliableWrite() {
        if (this.mService == null || this.mClientIf == 0) {
            return false;
        }
        synchronized (this.mDeviceBusy) {
            if (this.mDeviceBusy.booleanValue()) {
                return false;
            }
            this.mDeviceBusy = true;
            try {
                this.mService.endReliableWrite(this.mClientIf, this.mDevice.getAddress(), true);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
                this.mDeviceBusy = false;
                return false;
            }
        }
    }

    public void abortReliableWrite() {
        if (this.mService == null || this.mClientIf == 0) {
            return;
        }
        try {
            this.mService.endReliableWrite(this.mClientIf, this.mDevice.getAddress(), false);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    @Deprecated
    public void abortReliableWrite(BluetoothDevice mDevice) {
        abortReliableWrite();
    }

    public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable) {
        BluetoothGattService service;
        BluetoothDevice device;
        Log.d(TAG, "setCharacteristicNotification() - uuid: " + characteristic.getUuid() + " enable: " + enable);
        if (this.mService == null || this.mClientIf == 0 || (service = characteristic.getService()) == null || (device = service.getDevice()) == null) {
            return false;
        }
        try {
            this.mService.registerForNotification(this.mClientIf, device.getAddress(), characteristic.getInstanceId(), enable);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    private protected boolean refresh() {
        Log.d(TAG, "refresh() - device: " + this.mDevice.getAddress());
        if (this.mService == null || this.mClientIf == 0) {
            return false;
        }
        try {
            this.mService.refreshDevice(this.mClientIf, this.mDevice.getAddress());
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean readRemoteRssi() {
        Log.d(TAG, "readRssi() - device: " + this.mDevice.getAddress());
        if (this.mService == null || this.mClientIf == 0) {
            return false;
        }
        try {
            this.mService.readRemoteRssi(this.mClientIf, this.mDevice.getAddress());
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean requestMtu(int mtu) {
        Log.d(TAG, "configureMTU() - device: " + this.mDevice.getAddress() + " mtu: " + mtu);
        if (this.mService == null || this.mClientIf == 0) {
            return false;
        }
        try {
            this.mService.configureMTU(this.mClientIf, this.mDevice.getAddress(), mtu);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean requestConnectionPriority(int connectionPriority) {
        if (connectionPriority < 0 || connectionPriority > 2) {
            throw new IllegalArgumentException("connectionPriority not within valid range");
        }
        Log.d(TAG, "requestConnectionPriority() - params: " + connectionPriority);
        if (this.mService == null || this.mClientIf == 0) {
            return false;
        }
        try {
            this.mService.connectionParameterUpdate(this.mClientIf, this.mDevice.getAddress(), connectionPriority);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public synchronized boolean requestLeConnectionUpdate(int minConnectionInterval, int maxConnectionInterval, int slaveLatency, int supervisionTimeout, int minConnectionEventLen, int maxConnectionEventLen) {
        Log.d(TAG, "requestLeConnectionUpdate() - min=(" + minConnectionInterval + ")" + (minConnectionInterval * 1.25d) + "msec, max=(" + maxConnectionInterval + ")" + (1.25d * maxConnectionInterval) + "msec, latency=" + slaveLatency + ", timeout=" + supervisionTimeout + "msec, min_ce=" + minConnectionEventLen + ", max_ce=" + maxConnectionEventLen);
        if (this.mService == null || this.mClientIf == 0) {
            return false;
        }
        try {
            this.mService.leConnectionUpdate(this.mClientIf, this.mDevice.getAddress(), minConnectionInterval, maxConnectionInterval, slaveLatency, supervisionTimeout, minConnectionEventLen, maxConnectionEventLen);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
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
