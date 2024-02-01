package android.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothAvrcpController;
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
public final class BluetoothAvrcpController implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.avrcp-controller.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_PLAYER_SETTING = "android.bluetooth.avrcp-controller.profile.action.PLAYER_SETTING";
    public static final String ACTION_UIDS_EVENT = "android.bluetooth.avrcp-controller.profile.action.UIDS_EVENT";
    public static final int BTRC_FEAT_ABSOLUTE_VOLUME = 2;
    public static final int BTRC_FEAT_BROWSE = 4;
    public static final int BTRC_FEAT_COVER_ART = 8;
    public static final int BTRC_FEAT_METADATA = 1;
    public static final int BTRC_FEAT_NONE = 0;
    private static final boolean DBG = false;
    public static final String EXTRA_PLAYER_SETTING = "android.bluetooth.avrcp-controller.profile.extra.PLAYER_SETTING";
    private static final String TAG = "BluetoothAvrcpController";
    private static final boolean VDBG = false;
    private Context mContext;
    private volatile IBluetoothAvrcpController mService;
    private BluetoothProfile.ServiceListener mServiceListener;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub() { // from class: android.bluetooth.BluetoothAvrcpController.1
        @Override // android.bluetooth.IBluetoothStateChangeCallback
        public void onBluetoothStateChange(boolean up) {
            if (!up) {
                synchronized (BluetoothAvrcpController.this.mConnection) {
                    try {
                        BluetoothAvrcpController.this.mService = null;
                        BluetoothAvrcpController.this.mContext.unbindService(BluetoothAvrcpController.this.mConnection);
                    } catch (Exception re) {
                        Log.e(BluetoothAvrcpController.TAG, "", re);
                    }
                }
                return;
            }
            synchronized (BluetoothAvrcpController.this.mConnection) {
                try {
                    if (BluetoothAvrcpController.this.mService == null) {
                        BluetoothAvrcpController.this.doBind();
                    }
                } catch (Exception re2) {
                    Log.e(BluetoothAvrcpController.TAG, "", re2);
                }
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() { // from class: android.bluetooth.BluetoothAvrcpController.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            BluetoothAvrcpController.this.mService = IBluetoothAvrcpController.Stub.asInterface(Binder.allowBlocking(service));
            if (BluetoothAvrcpController.this.mServiceListener != null) {
                BluetoothAvrcpController.this.mServiceListener.onServiceConnected(12, BluetoothAvrcpController.this);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            BluetoothAvrcpController.this.mService = null;
            if (BluetoothAvrcpController.this.mServiceListener != null) {
                BluetoothAvrcpController.this.mServiceListener.onServiceDisconnected(12);
            }
        }
    };
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BluetoothAvrcpController(Context context, BluetoothProfile.ServiceListener l) {
        this.mContext = context;
        this.mServiceListener = l;
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
        Intent intent = new Intent(IBluetoothAvrcpController.class.getName());
        ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if (comp == null || !this.mContext.bindServiceAsUser(intent, this.mConnection, 0, this.mContext.getUser())) {
            Log.e(TAG, "Could not bind to Bluetooth AVRCP Controller Service with " + intent);
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void close() {
        this.mServiceListener = null;
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
    }

    public void finalize() {
        close();
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        IBluetoothAvrcpController service = this.mService;
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
        IBluetoothAvrcpController service = this.mService;
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
        IBluetoothAvrcpController service = this.mService;
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

    public synchronized BluetoothAvrcpPlayerSettings getPlayerSettings(BluetoothDevice device) {
        IBluetoothAvrcpController service = this.mService;
        if (service == null || !isEnabled()) {
            return null;
        }
        try {
            BluetoothAvrcpPlayerSettings settings = service.getPlayerSettings(device);
            return settings;
        } catch (RemoteException e) {
            Log.e(TAG, "Error talking to BT service in getMetadata() " + e);
            return null;
        }
    }

    public synchronized boolean setPlayerApplicationSetting(BluetoothAvrcpPlayerSettings plAppSetting) {
        IBluetoothAvrcpController service = this.mService;
        if (service != null && isEnabled()) {
            try {
                return service.setPlayerApplicationSetting(plAppSetting);
            } catch (RemoteException e) {
                Log.e(TAG, "Error talking to BT service in setPlayerApplicationSetting() " + e);
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    public synchronized void sendGroupNavigationCmd(BluetoothDevice device, int keyCode, int keyState) {
        Log.d(TAG, "sendGroupNavigationCmd dev = " + device + " key " + keyCode + " State = " + keyState);
        IBluetoothAvrcpController service = this.mService;
        if (service != null && isEnabled()) {
            try {
                service.sendGroupNavigationCmd(device, keyCode, keyState);
            } catch (RemoteException e) {
                Log.e(TAG, "Error talking to BT service in sendGroupNavigationCmd()", e);
            }
        } else if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
    }

    public void startFetchingAlbumArt(String mimeType, int height, int width, long maxSize) {
        if (this.mService != null && isEnabled()) {
            try {
                this.mService.startFetchingAlbumArt(mimeType, height, width, maxSize);
            } catch (RemoteException e) {
                Log.e(TAG, "Error talking to BT service in startFetchingAlbumArt() " + e);
                return;
            }
        }
        if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
    }

    private synchronized boolean isEnabled() {
        return this.mAdapter.getState() == 12;
    }

    private static synchronized boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    private static synchronized void log(String msg) {
        Log.d(TAG, msg);
    }
}
