package android.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothMap;
import android.bluetooth.IBluetoothStateChangeCallback;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class BluetoothMap implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.map.profile.action.CONNECTION_STATE_CHANGED";
    private static final boolean DBG = true;
    public static final int RESULT_CANCELED = 2;
    public static final int RESULT_FAILURE = 0;
    public static final int RESULT_SUCCESS = 1;
    public static final int STATE_ERROR = -1;
    private static final String TAG = "BluetoothMap";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub() { // from class: android.bluetooth.BluetoothMap.1
        @Override // android.bluetooth.IBluetoothStateChangeCallback
        public void onBluetoothStateChange(boolean up) {
            Log.d(BluetoothMap.TAG, "onBluetoothStateChange: up=" + up);
            if (!up) {
                synchronized (BluetoothMap.this.mConnection) {
                    try {
                        BluetoothMap.this.mService = null;
                        BluetoothMap.this.mContext.unbindService(BluetoothMap.this.mConnection);
                    } catch (Exception re) {
                        Log.e(BluetoothMap.TAG, "", re);
                    }
                }
                return;
            }
            synchronized (BluetoothMap.this.mConnection) {
                try {
                    if (BluetoothMap.this.mService == null) {
                        BluetoothMap.this.doBind();
                    }
                } catch (Exception re2) {
                    Log.e(BluetoothMap.TAG, "", re2);
                }
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() { // from class: android.bluetooth.BluetoothMap.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            BluetoothMap.log("Proxy object connected");
            BluetoothMap.this.mService = IBluetoothMap.Stub.asInterface(Binder.allowBlocking(service));
            if (BluetoothMap.this.mServiceListener != null) {
                BluetoothMap.this.mServiceListener.onServiceConnected(9, BluetoothMap.this);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            BluetoothMap.log("Proxy object disconnected");
            BluetoothMap.this.mService = null;
            if (BluetoothMap.this.mServiceListener != null) {
                BluetoothMap.this.mServiceListener.onServiceDisconnected(9);
            }
        }
    };
    private final Context mContext;
    private volatile IBluetoothMap mService;
    private BluetoothProfile.ServiceListener mServiceListener;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BluetoothMap(Context context, BluetoothProfile.ServiceListener l) {
        Log.d(TAG, "Create BluetoothMap proxy object");
        this.mContext = context;
        this.mServiceListener = l;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
        }
        doBind();
    }

    synchronized boolean doBind() {
        Intent intent = new Intent(IBluetoothMap.class.getName());
        ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if (comp == null || !this.mContext.bindServiceAsUser(intent, this.mConnection, 0, this.mContext.getUser())) {
            Log.e(TAG, "Could not bind to Bluetooth MAP Service with " + intent);
            return false;
        }
        return true;
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    public synchronized void close() {
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
        synchronized (this.mConnection) {
            if (this.mService != null) {
                try {
                    this.mService = null;
                    this.mContext.unbindService(this.mConnection);
                } catch (Exception re) {
                    Log.e(TAG, "", re);
                }
            }
        }
        this.mServiceListener = null;
    }

    public synchronized int getState() {
        IBluetoothMap service = this.mService;
        if (service != null) {
            try {
                return service.getState();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return -1;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        log(Log.getStackTraceString(new Throwable()));
        return -1;
    }

    public synchronized BluetoothDevice getClient() {
        IBluetoothMap service = this.mService;
        if (service != null) {
            try {
                return service.getClient();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return null;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        log(Log.getStackTraceString(new Throwable()));
        return null;
    }

    public synchronized boolean isConnected(BluetoothDevice device) {
        IBluetoothMap service = this.mService;
        if (service != null) {
            try {
                return service.isConnected(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        log(Log.getStackTraceString(new Throwable()));
        return false;
    }

    public synchronized boolean connect(BluetoothDevice device) {
        log("connect(" + device + ")not supported for MAPS");
        return false;
    }

    private protected boolean disconnect(BluetoothDevice device) {
        log("disconnect(" + device + ")");
        IBluetoothMap service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.disconnect(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    public static synchronized boolean doesClassMatchSink(BluetoothClass btClass) {
        int deviceClass = btClass.getDeviceClass();
        if (deviceClass == 256 || deviceClass == 260 || deviceClass == 264 || deviceClass == 268) {
            return true;
        }
        return false;
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        log("getConnectedDevices()");
        IBluetoothMap service = this.mService;
        if (service != null && isEnabled()) {
            try {
                return service.getConnectedDevices();
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return new ArrayList();
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return new ArrayList();
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        log("getDevicesMatchingStates()");
        IBluetoothMap service = this.mService;
        if (service != null && isEnabled()) {
            try {
                return service.getDevicesMatchingConnectionStates(states);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return new ArrayList();
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return new ArrayList();
    }

    @Override // android.bluetooth.BluetoothProfile
    public int getConnectionState(BluetoothDevice device) {
        log("getConnectionState(" + device + ")");
        IBluetoothMap service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.getConnectionState(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return 0;
    }

    public synchronized boolean setPriority(BluetoothDevice device, int priority) {
        log("setPriority(" + device + ", " + priority + ")");
        IBluetoothMap service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            if (priority != 0 && priority != 100) {
                return false;
            }
            try {
                return service.setPriority(device, priority);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    public synchronized int getPriority(BluetoothDevice device) {
        IBluetoothMap service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.getPriority(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void log(String msg) {
        Log.d(TAG, msg);
    }

    private synchronized boolean isEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null || adapter.getState() != 12) {
            log("Bluetooth is Not enabled");
            return false;
        }
        return true;
    }

    private static synchronized boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }
}
