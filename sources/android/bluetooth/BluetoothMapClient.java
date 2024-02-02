package android.bluetooth;

import android.app.PendingIntent;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothMapClient;
import android.bluetooth.IBluetoothStateChangeCallback;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class BluetoothMapClient implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.mapmce.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_EXT_INSTANCE_INFORMATION = "android.bluetooth.mapmce.profile.action.ext.INSTANCE_INFORMATION";
    public static final String ACTION_EXT_MESSAGE_DELETED_STATUS_CHANGED = "android.bluetooth.mapmce.profile.action.ext.MESSAGE_DELETED_STATUS_CHANGED";
    public static final String ACTION_EXT_MESSAGE_READ_STATUS_CHANGED = "android.bluetooth.mapmce.profile.action.ext.MESSAGE_READ_STATUS_CHANGED";
    public static final String ACTION_MESSAGE_DELIVERED_SUCCESSFULLY = "android.bluetooth.mapmce.profile.action.MESSAGE_DELIVERED_SUCCESSFULLY";
    public static final String ACTION_MESSAGE_RECEIVED = "android.bluetooth.mapmce.profile.action.MESSAGE_RECEIVED";
    public static final String ACTION_MESSAGE_SENT_SUCCESSFULLY = "android.bluetooth.mapmce.profile.action.MESSAGE_SENT_SUCCESSFULLY";
    public static final int DELETED = 1;
    public static final String EXTRA_FOLDER = "android.bluetooth.mapmce.profile.extra.FOLDER";
    public static final String EXTRA_INSTANCE_ID = "android.bluetooth.mapmce.profile.extra.INSTANCE_ID";
    public static final String EXTRA_INSTANCE_NAME = "android.bluetooth.mapmce.profile.extra.INSTANCE_NAME";
    public static final String EXTRA_MESSAGE_HANDLE = "android.bluetooth.mapmce.profile.extra.MESSAGE_HANDLE";
    public static final String EXTRA_OWNER_UCI = "android.bluetooth.mapmce.profile.extra.OWNER_UCI";
    public static final String EXTRA_READ_STATUS = "android.bluetooth.mapmce.profile.extra.READ_STATUS";
    public static final String EXTRA_RECIPIENT_CONTACT_NAME = "android.bluetooth.mapmce.profile.extra.RECIPIENT_CONTACT_NAME";
    public static final String EXTRA_RECIPIENT_CONTACT_URI = "android.bluetooth.mapmce.profile.extra.RECIPIENT_CONTACT_URI";
    public static final String EXTRA_SENDER_CONTACT_NAME = "android.bluetooth.mapmce.profile.extra.SENDER_CONTACT_NAME";
    public static final String EXTRA_SENDER_CONTACT_URI = "android.bluetooth.mapmce.profile.extra.SENDER_CONTACT_URI";
    public static final String EXTRA_SUPPORTED_TYPE = "android.bluetooth.mapmce.profile.extra.SUPPORTED_TYPE";
    public static final String EXTRA_TYPE = "android.bluetooth.mapmce.profile.extra.TYPE";
    public static final int READ = 0;
    public static final int RESULT_CANCELED = 2;
    public static final int RESULT_FAILURE = 0;
    public static final int RESULT_SUCCESS = 1;
    public static final int STATE_ERROR = -1;
    private BluetoothAdapter mAdapter;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub() { // from class: android.bluetooth.BluetoothMapClient.1
        @Override // android.bluetooth.IBluetoothStateChangeCallback
        public void onBluetoothStateChange(boolean up) {
            if (BluetoothMapClient.DBG) {
                Log.d(BluetoothMapClient.TAG, "onBluetoothStateChange: up=" + up);
            }
            if (!up) {
                if (BluetoothMapClient.VDBG) {
                    Log.d(BluetoothMapClient.TAG, "Unbinding service...");
                }
                synchronized (BluetoothMapClient.this.mConnection) {
                    try {
                        BluetoothMapClient.this.mService = null;
                        BluetoothMapClient.this.mContext.unbindService(BluetoothMapClient.this.mConnection);
                    } catch (Exception re) {
                        Log.e(BluetoothMapClient.TAG, "", re);
                    }
                }
                return;
            }
            synchronized (BluetoothMapClient.this.mConnection) {
                try {
                    if (BluetoothMapClient.this.mService == null) {
                        if (BluetoothMapClient.VDBG) {
                            Log.d(BluetoothMapClient.TAG, "Binding service...");
                        }
                        BluetoothMapClient.this.doBind();
                    }
                } catch (Exception re2) {
                    Log.e(BluetoothMapClient.TAG, "", re2);
                }
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() { // from class: android.bluetooth.BluetoothMapClient.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (BluetoothMapClient.DBG) {
                Log.d(BluetoothMapClient.TAG, "Proxy object connected");
            }
            BluetoothMapClient.this.mService = IBluetoothMapClient.Stub.asInterface(service);
            if (BluetoothMapClient.this.mServiceListener != null) {
                BluetoothMapClient.this.mServiceListener.onServiceConnected(18, BluetoothMapClient.this);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            if (BluetoothMapClient.DBG) {
                Log.d(BluetoothMapClient.TAG, "Proxy object disconnected");
            }
            BluetoothMapClient.this.mService = null;
            if (BluetoothMapClient.this.mServiceListener != null) {
                BluetoothMapClient.this.mServiceListener.onServiceDisconnected(18);
            }
        }
    };
    private final Context mContext;
    private volatile IBluetoothMapClient mService;
    private BluetoothProfile.ServiceListener mServiceListener;
    private static final String TAG = "BluetoothMapClient";
    private static final boolean DBG = Log.isLoggable(TAG, 3);
    private static final boolean VDBG = Log.isLoggable(TAG, 2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BluetoothMapClient(Context context, BluetoothProfile.ServiceListener l) {
        if (DBG) {
            Log.d(TAG, "Create BluetoothMapClient proxy object");
        }
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
        Intent intent = new Intent(IBluetoothMapClient.class.getName());
        ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if (comp == null || !this.mContext.bindServiceAsUser(intent, this.mConnection, 0, this.mContext.getUser())) {
            Log.e(TAG, "Could not bind to Bluetooth MAP MCE Service with " + intent);
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

    public synchronized boolean isConnected(BluetoothDevice device) {
        if (VDBG) {
            Log.d(TAG, "isConnected(" + device + ")");
        }
        IBluetoothMapClient service = this.mService;
        if (service != null) {
            try {
                return service.isConnected(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        if (DBG) {
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
        return false;
    }

    public synchronized boolean connect(BluetoothDevice device) {
        if (DBG) {
            Log.d(TAG, "connect(" + device + ")for MAPS MCE");
        }
        IBluetoothMapClient service = this.mService;
        if (service != null) {
            try {
                return service.connect(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        if (DBG) {
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
        return false;
    }

    public synchronized boolean disconnect(BluetoothDevice device) {
        if (DBG) {
            Log.d(TAG, "disconnect(" + device + ")");
        }
        IBluetoothMapClient service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.disconnect(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        if (DBG) {
            Log.d(TAG, "getConnectedDevices()");
        }
        IBluetoothMapClient service = this.mService;
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
        if (DBG) {
            Log.d(TAG, "getDevicesMatchingStates()");
        }
        IBluetoothMapClient service = this.mService;
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
        if (DBG) {
            Log.d(TAG, "getConnectionState(" + device + ")");
        }
        IBluetoothMapClient service = this.mService;
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
        if (DBG) {
            Log.d(TAG, "setPriority(" + device + ", " + priority + ")");
        }
        IBluetoothMapClient service = this.mService;
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
        if (VDBG) {
            Log.d(TAG, "getPriority(" + device + ")");
        }
        IBluetoothMapClient service = this.mService;
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

    private protected boolean sendMessage(BluetoothDevice device, Uri[] contacts, String message, PendingIntent sentIntent, PendingIntent deliveredIntent) {
        if (DBG) {
            Log.d(TAG, "sendMessage(" + device + ", " + contacts + ", " + message);
        }
        IBluetoothMapClient service = this.mService;
        if (service == null || !isEnabled() || !isValidDevice(device)) {
            return false;
        }
        try {
            return service.sendMessage(device, contacts, message, sentIntent, deliveredIntent);
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    public synchronized boolean getUnreadMessages(BluetoothDevice device) {
        if (DBG) {
            Log.d(TAG, "getUnreadMessages(" + device + ")");
        }
        IBluetoothMapClient service = this.mService;
        if (service == null || !isEnabled() || !isValidDevice(device)) {
            return false;
        }
        try {
            return service.getUnreadMessages(device);
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    public boolean setMessageStatus(BluetoothDevice device, String handle, int status) {
        if (DBG) {
            Log.d(TAG, "setMessageStatus(" + device + ", " + handle + ", " + status + ")");
        }
        IBluetoothMapClient service = this.mService;
        if (service == null || !isEnabled() || !isValidDevice(device) || handle == null || (status != 0 && status != 1)) {
            return false;
        }
        try {
            return service.setMessageStatus(device, handle, status);
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    public boolean abort(BluetoothDevice device) {
        if (DBG) {
            Log.d(TAG, "abort(" + device + ")");
        }
        IBluetoothMapClient service = this.mService;
        if (service == null || !isEnabled() || !isValidDevice(device)) {
            return false;
        }
        try {
            return service.abort(device);
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    public boolean setActiveInstance(BluetoothDevice device, byte instance) {
        if (DBG) {
            Log.d(TAG, "setActiveInstance(" + device + ", " + ((int) instance) + ")");
        }
        IBluetoothMapClient service = this.mService;
        if (service == null || !isEnabled() || !isValidDevice(device)) {
            return false;
        }
        try {
            return service.setActiveInstance(device, instance);
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    private synchronized boolean isEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null || adapter.getState() != 12) {
            if (DBG) {
                Log.d(TAG, "Bluetooth is Not enabled");
                return false;
            }
            return false;
        }
        return true;
    }

    private static synchronized boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }
}
