package android.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothHearingAid;
import android.bluetooth.IBluetoothStateChangeCallback;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.telephony.ims.ImsConferenceState;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/* loaded from: classes.dex */
public final class BluetoothHearingAid implements BluetoothProfile {
    private protected static final String ACTION_ACTIVE_DEVICE_CHANGED = "android.bluetooth.hearingaid.profile.action.ACTIVE_DEVICE_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_PLAYING_STATE_CHANGED = "android.bluetooth.hearingaid.profile.action.PLAYING_STATE_CHANGED";
    private static final boolean DBG = false;
    public static final long HI_SYNC_ID_INVALID = 0;
    public static final int MODE_BINAURAL = 1;
    public static final int MODE_MONAURAL = 0;
    public static final int SIDE_LEFT = 0;
    public static final int SIDE_RIGHT = 1;
    public static final int STATE_NOT_PLAYING = 11;
    public static final int STATE_PLAYING = 10;
    private static final String TAG = "BluetoothHearingAid";
    private static final boolean VDBG = false;
    private Context mContext;
    @GuardedBy("mServiceLock")
    private IBluetoothHearingAid mService;
    private BluetoothProfile.ServiceListener mServiceListener;
    private final ReentrantReadWriteLock mServiceLock = new ReentrantReadWriteLock();
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub() { // from class: android.bluetooth.BluetoothHearingAid.1
        @Override // android.bluetooth.IBluetoothStateChangeCallback
        public void onBluetoothStateChange(boolean up) {
            try {
                try {
                    if (!up) {
                        try {
                            BluetoothHearingAid.this.mServiceLock.writeLock().lock();
                            BluetoothHearingAid.this.mService = null;
                            BluetoothHearingAid.this.mContext.unbindService(BluetoothHearingAid.this.mConnection);
                        } catch (Exception re) {
                            Log.e(BluetoothHearingAid.TAG, "", re);
                        }
                        return;
                    }
                    try {
                        BluetoothHearingAid.this.mServiceLock.readLock().lock();
                        if (BluetoothHearingAid.this.mService == null) {
                            BluetoothHearingAid.this.doBind();
                        }
                    } catch (Exception re2) {
                        Log.e(BluetoothHearingAid.TAG, "", re2);
                    }
                } finally {
                    BluetoothHearingAid.this.mServiceLock.writeLock().unlock();
                }
            } finally {
                BluetoothHearingAid.this.mServiceLock.readLock().unlock();
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() { // from class: android.bluetooth.BluetoothHearingAid.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            try {
                BluetoothHearingAid.this.mServiceLock.writeLock().lock();
                BluetoothHearingAid.this.mService = IBluetoothHearingAid.Stub.asInterface(Binder.allowBlocking(service));
                BluetoothHearingAid.this.mServiceLock.writeLock().unlock();
                if (BluetoothHearingAid.this.mServiceListener != null) {
                    BluetoothHearingAid.this.mServiceListener.onServiceConnected(21, BluetoothHearingAid.this);
                }
            } catch (Throwable th) {
                BluetoothHearingAid.this.mServiceLock.writeLock().unlock();
                throw th;
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            try {
                BluetoothHearingAid.this.mServiceLock.writeLock().lock();
                BluetoothHearingAid.this.mService = null;
                BluetoothHearingAid.this.mServiceLock.writeLock().unlock();
                if (BluetoothHearingAid.this.mServiceListener != null) {
                    BluetoothHearingAid.this.mServiceListener.onServiceDisconnected(21);
                }
            } catch (Throwable th) {
                BluetoothHearingAid.this.mServiceLock.writeLock().unlock();
                throw th;
            }
        }
    };
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BluetoothHearingAid(Context context, BluetoothProfile.ServiceListener l) {
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

    synchronized void doBind() {
        Intent intent = new Intent(IBluetoothHearingAid.class.getName());
        ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if (comp == null || !this.mContext.bindServiceAsUser(intent, this.mConnection, 0, Process.myUserHandle())) {
            Log.e(TAG, "Could not bind to Bluetooth Hearing Aid Service with " + intent);
        }
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
        try {
            try {
                this.mServiceLock.writeLock().lock();
                if (this.mService != null) {
                    this.mService = null;
                    this.mContext.unbindService(this.mConnection);
                }
            } catch (Exception re) {
                Log.e(TAG, "", re);
            }
        } finally {
            this.mServiceLock.writeLock().unlock();
        }
    }

    public void finalize() {
    }

    public synchronized boolean connect(BluetoothDevice device) {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null && isEnabled() && isValidDevice(device)) {
                return this.mService.connect(device);
            }
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public synchronized boolean disconnect(BluetoothDevice device) {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null && isEnabled() && isValidDevice(device)) {
                return this.mService.disconnect(device);
            }
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService == null || !isEnabled()) {
                if (this.mService == null) {
                    Log.w(TAG, "Proxy not attached to service");
                }
                return new ArrayList();
            }
            return this.mService.getConnectedDevices();
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService == null || !isEnabled()) {
                if (this.mService == null) {
                    Log.w(TAG, "Proxy not attached to service");
                }
                return new ArrayList();
            }
            return this.mService.getDevicesMatchingConnectionStates(states);
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    @Override // android.bluetooth.BluetoothProfile
    public int getConnectionState(BluetoothDevice device) {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null && isEnabled() && isValidDevice(device)) {
                return this.mService.getConnectionState(device);
            }
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return 0;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return 0;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    private protected boolean setActiveDevice(BluetoothDevice device) {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null && isEnabled() && (device == null || isValidDevice(device))) {
                this.mService.setActiveDevice(device);
                return true;
            }
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    private protected List<BluetoothDevice> getActiveDevices() {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService == null || !isEnabled()) {
                if (this.mService == null) {
                    Log.w(TAG, "Proxy not attached to service");
                }
                return new ArrayList();
            }
            return this.mService.getActiveDevices();
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public synchronized boolean setPriority(BluetoothDevice device, int priority) {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService == null || !isEnabled() || !isValidDevice(device)) {
                if (this.mService == null) {
                    Log.w(TAG, "Proxy not attached to service");
                }
                return false;
            } else if (priority == 0 || priority == 100) {
                return this.mService.setPriority(device, priority);
            } else {
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public synchronized int getPriority(BluetoothDevice device) {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null && isEnabled() && isValidDevice(device)) {
                return this.mService.getPriority(device);
            }
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return 0;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return 0;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public static synchronized String stateToString(int state) {
        switch (state) {
            case 0:
                return ImsConferenceState.STATUS_DISCONNECTED;
            case 1:
                return "connecting";
            case 2:
                return "connected";
            case 3:
                return ImsConferenceState.STATUS_DISCONNECTING;
            default:
                switch (state) {
                    case 10:
                        return "playing";
                    case 11:
                        return "not playing";
                    default:
                        return "<unknown state " + state + ">";
                }
        }
    }

    public synchronized int getVolume() {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService == null || !isEnabled()) {
                if (this.mService == null) {
                    Log.w(TAG, "Proxy not attached to service");
                }
                return 0;
            }
            return this.mService.getVolume();
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return 0;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public synchronized void adjustVolume(int direction) {
        try {
            try {
                this.mServiceLock.readLock().lock();
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            } else if (isEnabled()) {
                this.mService.adjustVolume(direction);
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public synchronized void setVolume(int volume) {
        try {
            try {
                this.mServiceLock.readLock().lock();
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            } else if (isEnabled()) {
                this.mService.setVolume(volume);
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public synchronized long getHiSyncId(BluetoothDevice device) {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
                return 0L;
            }
            if (isEnabled() && isValidDevice(device)) {
                return this.mService.getHiSyncId(device);
            }
            return 0L;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return 0L;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public synchronized int getDeviceSide(BluetoothDevice device) {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null && isEnabled() && isValidDevice(device)) {
                return this.mService.getDeviceSide(device);
            }
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return 0;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return 0;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public synchronized int getDeviceMode(BluetoothDevice device) {
        try {
            this.mServiceLock.readLock().lock();
            if (this.mService != null && isEnabled() && isValidDevice(device)) {
                return this.mService.getDeviceMode(device);
            }
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return 0;
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return 0;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    private synchronized boolean isEnabled() {
        return this.mAdapter.getState() == 12;
    }

    private synchronized boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    private static synchronized void log(String msg) {
        Log.d(TAG, msg);
    }
}
