package android.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothPan;
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
public final class BluetoothPan implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.pan.profile.action.CONNECTION_STATE_CHANGED";
    private static final boolean DBG = true;
    public static final String EXTRA_LOCAL_ROLE = "android.bluetooth.pan.extra.LOCAL_ROLE";
    public static final int LOCAL_NAP_ROLE = 1;
    public static final int LOCAL_PANU_ROLE = 2;
    public static final int PAN_CONNECT_FAILED_ALREADY_CONNECTED = 1001;
    public static final int PAN_CONNECT_FAILED_ATTEMPT_FAILED = 1002;
    public static final int PAN_DISCONNECT_FAILED_NOT_CONNECTED = 1000;
    public static final int PAN_OPERATION_GENERIC_FAILURE = 1003;
    public static final int PAN_OPERATION_SUCCESS = 1004;
    public static final int PAN_ROLE_NONE = 0;
    public static final int REMOTE_NAP_ROLE = 1;
    public static final int REMOTE_PANU_ROLE = 2;
    private static final String TAG = "BluetoothPan";
    private static final boolean VDBG = false;
    private Context mContext;
    private volatile IBluetoothPan mPanService;
    private BluetoothProfile.ServiceListener mServiceListener;
    private final IBluetoothStateChangeCallback mStateChangeCallback = new IBluetoothStateChangeCallback.Stub() { // from class: android.bluetooth.BluetoothPan.1
        @Override // android.bluetooth.IBluetoothStateChangeCallback
        public void onBluetoothStateChange(boolean on) {
            Log.d(BluetoothPan.TAG, "onBluetoothStateChange on: " + on);
            if (on) {
                try {
                    if (BluetoothPan.this.mPanService == null) {
                        BluetoothPan.this.doBind();
                        return;
                    }
                    return;
                } catch (IllegalStateException e) {
                    Log.e(BluetoothPan.TAG, "onBluetoothStateChange: could not bind to PAN service: ", e);
                    return;
                } catch (SecurityException e2) {
                    Log.e(BluetoothPan.TAG, "onBluetoothStateChange: could not bind to PAN service: ", e2);
                    return;
                }
            }
            synchronized (BluetoothPan.this.mConnection) {
                try {
                    BluetoothPan.this.mPanService = null;
                    BluetoothPan.this.mContext.unbindService(BluetoothPan.this.mConnection);
                } catch (Exception re) {
                    Log.e(BluetoothPan.TAG, "", re);
                }
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() { // from class: android.bluetooth.BluetoothPan.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(BluetoothPan.TAG, "BluetoothPAN Proxy object connected");
            BluetoothPan.this.mPanService = IBluetoothPan.Stub.asInterface(Binder.allowBlocking(service));
            if (BluetoothPan.this.mServiceListener != null) {
                BluetoothPan.this.mServiceListener.onServiceConnected(5, BluetoothPan.this);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            Log.d(BluetoothPan.TAG, "BluetoothPAN Proxy object disconnected");
            BluetoothPan.this.mPanService = null;
            if (BluetoothPan.this.mServiceListener != null) {
                BluetoothPan.this.mServiceListener.onServiceDisconnected(5);
            }
        }
    };
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

    public private protected BluetoothPan(Context context, BluetoothProfile.ServiceListener l) {
        this.mContext = context;
        this.mServiceListener = l;
        try {
            this.mAdapter.getBluetoothManager().registerStateChangeCallback(this.mStateChangeCallback);
        } catch (RemoteException re) {
            Log.w(TAG, "Unable to register BluetoothStateChangeCallback", re);
        }
        doBind();
    }

    public private protected boolean doBind() {
        Intent intent = new Intent(IBluetoothPan.class.getName());
        ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if (comp == null || !this.mContext.bindServiceAsUser(intent, this.mConnection, 0, this.mContext.getUser())) {
            Log.e(TAG, "Could not bind to Bluetooth Pan Service with " + intent);
            return false;
        }
        return true;
    }

    public private protected void close() {
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(this.mStateChangeCallback);
            } catch (RemoteException re) {
                Log.w(TAG, "Unable to unregister BluetoothStateChangeCallback", re);
            }
        }
        synchronized (this.mConnection) {
            if (this.mPanService != null) {
                try {
                    this.mPanService = null;
                    this.mContext.unbindService(this.mConnection);
                } catch (Exception re2) {
                    Log.e(TAG, "", re2);
                }
            }
        }
        this.mServiceListener = null;
    }

    protected void finalize() {
        close();
    }

    private protected boolean connect(BluetoothDevice device) {
        log("connect(" + device + ")");
        IBluetoothPan service = this.mPanService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.connect(device);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    private protected boolean disconnect(BluetoothDevice device) {
        log("disconnect(" + device + ")");
        IBluetoothPan service = this.mPanService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.disconnect(device);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        IBluetoothPan service = this.mPanService;
        if (service != null && isEnabled()) {
            try {
                return service.getConnectedDevices();
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
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
        IBluetoothPan service = this.mPanService;
        if (service != null && isEnabled()) {
            try {
                return service.getDevicesMatchingConnectionStates(states);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
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
        IBluetoothPan service = this.mPanService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.getConnectionState(device);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return 0;
    }

    private protected void setBluetoothTethering(boolean value) {
        log("setBluetoothTethering(" + value + ")");
        IBluetoothPan service = this.mPanService;
        if (service != null && isEnabled()) {
            try {
                service.setBluetoothTethering(value);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        }
    }

    private protected boolean isTetheringOn() {
        IBluetoothPan service = this.mPanService;
        if (service != null && isEnabled()) {
            try {
                return service.isTetheringOn();
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        return false;
    }

    public protected boolean isEnabled() {
        return this.mAdapter.getState() == 12;
    }

    public protected static boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    public protected static void log(String msg) {
        Log.d(TAG, msg);
    }
}
