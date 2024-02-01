package android.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothPbapClient;
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
public final class BluetoothPbapClient implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.pbapclient.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_PHONEBOOK_DOWNLOAD_STATE_CHANGED = "android.bluetooth.pbapclient.profile.action.PHONEBOOK_DOWNLOAD_STATE_CHANGED";
    public static final String CCH_PATH = "telecom/cch.vcf";
    private static final boolean DBG = false;
    public static final int DOWNLOAD_COMPLETED = 1;
    public static final int DOWNLOAD_FAILED = 2;
    public static final int DOWNLOAD_IN_PROGRESS = 0;
    public static final String EXTRA_DOWNLOAD_RESULT = "android.bluetooth.pbapclient.profile.extra.DOWNLOAD_RESULT";
    public static final String EXTRA_DOWNLOAD_STATE = "android.bluetooth.pbapclient.profile.extra.DOWNLOAD_STATE";
    public static final String EXTRA_PHONEBOOK_PATH = "android.bluetooth.pbapclient.profile.extra.PHONEBOOK_PATH";
    public static final String ICH_PATH = "telecom/ich.vcf";
    public static final String MCH_PATH = "telecom/mch.vcf";
    public static final String OCH_PATH = "telecom/och.vcf";
    private static final long PBAP_FILTER_ADR = 32;
    private static final long PBAP_FILTER_EMAIL = 256;
    private static final long PBAP_FILTER_FN = 2;
    private static final long PBAP_FILTER_N = 4;
    private static final long PBAP_FILTER_NICKNAME = 8388608;
    private static final long PBAP_FILTER_PHOTO = 8;
    private static final long PBAP_FILTER_TEL = 128;
    private static final long PBAP_FILTER_VERSION = 1;
    public static final String PB_PATH = "telecom/pb.vcf";
    public static final int RESULT_CANCELED = 2;
    public static final int RESULT_FAILURE = 0;
    public static final int RESULT_INVALID_PARAMETER = 3;
    public static final int RESULT_SUCCESS = 1;
    public static final String SIM1_CCH_PATH = "SIM1/telecom/cch.vcf";
    public static final String SIM1_ICH_PATH = "SIM1/telecom/ich.vcf";
    public static final String SIM1_MCH_PATH = "SIM1/telecom/mch.vcf";
    public static final String SIM1_OCH_PATH = "SIM1/telecom/och.vcf";
    public static final String SIM1_PB_PATH = "SIM1/telecom/pb.vcf";
    public static final int STATE_ERROR = -1;
    private static final String TAG = "BluetoothPbapClient";
    private static final boolean VDBG = false;
    private final Context mContext;
    private volatile IBluetoothPbapClient mService;
    private BluetoothProfile.ServiceListener mServiceListener;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub() { // from class: android.bluetooth.BluetoothPbapClient.1
        @Override // android.bluetooth.IBluetoothStateChangeCallback
        public void onBluetoothStateChange(boolean up) {
            if (!up) {
                synchronized (BluetoothPbapClient.this.mConnection) {
                    try {
                        BluetoothPbapClient.this.mService = null;
                        BluetoothPbapClient.this.mContext.unbindService(BluetoothPbapClient.this.mConnection);
                    } catch (Exception re) {
                        Log.e(BluetoothPbapClient.TAG, "", re);
                    }
                }
                return;
            }
            synchronized (BluetoothPbapClient.this.mConnection) {
                try {
                    if (BluetoothPbapClient.this.mService == null) {
                        BluetoothPbapClient.this.doBind();
                    }
                } catch (Exception re2) {
                    Log.e(BluetoothPbapClient.TAG, "", re2);
                }
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() { // from class: android.bluetooth.BluetoothPbapClient.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            BluetoothPbapClient.this.mService = IBluetoothPbapClient.Stub.asInterface(Binder.allowBlocking(service));
            if (BluetoothPbapClient.this.mServiceListener != null) {
                BluetoothPbapClient.this.mServiceListener.onServiceConnected(17, BluetoothPbapClient.this);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            BluetoothPbapClient.this.mService = null;
            if (BluetoothPbapClient.this.mServiceListener != null) {
                BluetoothPbapClient.this.mServiceListener.onServiceDisconnected(17);
            }
        }
    };
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BluetoothPbapClient(Context context, BluetoothProfile.ServiceListener l) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean doBind() {
        Intent intent = new Intent(IBluetoothPbapClient.class.getName());
        ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if (comp == null || !this.mContext.bindServiceAsUser(intent, this.mConnection, 0, this.mContext.getUser())) {
            Log.e(TAG, "Could not bind to Bluetooth PBAP Client Service with " + intent);
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

    public synchronized boolean connect(BluetoothDevice device) {
        IBluetoothPbapClient service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.connect(device);
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

    public synchronized boolean disconnect(BluetoothDevice device) {
        IBluetoothPbapClient service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                service.disconnect(device);
                return true;
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

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        IBluetoothPbapClient service = this.mService;
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
        IBluetoothPbapClient service = this.mService;
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
        IBluetoothPbapClient service = this.mService;
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

    private static synchronized void log(String msg) {
        Log.d(TAG, msg);
    }

    private synchronized boolean isEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.getState() == 12) {
            return true;
        }
        log("Bluetooth is Not enabled");
        return false;
    }

    private static synchronized boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    public synchronized boolean setPriority(BluetoothDevice device, int priority) {
        IBluetoothPbapClient service = this.mService;
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
        IBluetoothPbapClient service = this.mService;
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

    public boolean pullPhonebook(BluetoothDevice device, String pbName, long filter, int listStartOffset, int maxListCount) {
        IBluetoothPbapClient service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.pullPhonebook(device, pbName, filter, listStartOffset, maxListCount);
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
}
