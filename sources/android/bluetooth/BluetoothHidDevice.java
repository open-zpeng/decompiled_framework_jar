package android.bluetooth;

import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothHidDevice;
import android.bluetooth.IBluetoothHidDeviceCallback;
import android.bluetooth.IBluetoothStateChangeCallback;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class BluetoothHidDevice implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.hiddevice.profile.action.CONNECTION_STATE_CHANGED";
    public static final byte ERROR_RSP_INVALID_PARAM = 4;
    public static final byte ERROR_RSP_INVALID_RPT_ID = 2;
    public static final byte ERROR_RSP_NOT_READY = 1;
    public static final byte ERROR_RSP_SUCCESS = 0;
    public static final byte ERROR_RSP_UNKNOWN = 14;
    public static final byte ERROR_RSP_UNSUPPORTED_REQ = 3;
    public static final byte PROTOCOL_BOOT_MODE = 0;
    public static final byte PROTOCOL_REPORT_MODE = 1;
    public static final byte REPORT_TYPE_FEATURE = 3;
    public static final byte REPORT_TYPE_INPUT = 1;
    public static final byte REPORT_TYPE_OUTPUT = 2;
    public static final byte SUBCLASS1_COMBO = -64;
    public static final byte SUBCLASS1_KEYBOARD = 64;
    public static final byte SUBCLASS1_MOUSE = Byte.MIN_VALUE;
    public static final byte SUBCLASS1_NONE = 0;
    public static final byte SUBCLASS2_CARD_READER = 6;
    public static final byte SUBCLASS2_DIGITIZER_TABLET = 5;
    public static final byte SUBCLASS2_GAMEPAD = 2;
    public static final byte SUBCLASS2_JOYSTICK = 1;
    public static final byte SUBCLASS2_REMOTE_CONTROL = 3;
    public static final byte SUBCLASS2_SENSING_DEVICE = 4;
    public static final byte SUBCLASS2_UNCATEGORIZED = 0;
    private static final String TAG = BluetoothHidDevice.class.getSimpleName();
    private Context mContext;
    private volatile IBluetoothHidDevice mService;
    private BluetoothProfile.ServiceListener mServiceListener;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub() { // from class: android.bluetooth.BluetoothHidDevice.1
        @Override // android.bluetooth.IBluetoothStateChangeCallback
        public void onBluetoothStateChange(boolean up) {
            String str = BluetoothHidDevice.TAG;
            Log.d(str, "onBluetoothStateChange: up=" + up);
            synchronized (BluetoothHidDevice.this.mConnection) {
                if (up) {
                    try {
                        if (BluetoothHidDevice.this.mService == null) {
                            Log.d(BluetoothHidDevice.TAG, "Binding HID Device service...");
                            BluetoothHidDevice.this.doBind();
                        }
                    } catch (IllegalStateException e) {
                        Log.e(BluetoothHidDevice.TAG, "onBluetoothStateChange: could not bind to HID Dev service: ", e);
                    } catch (SecurityException e2) {
                        Log.e(BluetoothHidDevice.TAG, "onBluetoothStateChange: could not bind to HID Dev service: ", e2);
                    }
                } else {
                    Log.d(BluetoothHidDevice.TAG, "Unbinding service...");
                    BluetoothHidDevice.this.doUnbind();
                }
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() { // from class: android.bluetooth.BluetoothHidDevice.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(BluetoothHidDevice.TAG, "onServiceConnected()");
            BluetoothHidDevice.this.mService = IBluetoothHidDevice.Stub.asInterface(service);
            if (BluetoothHidDevice.this.mServiceListener != null) {
                BluetoothHidDevice.this.mServiceListener.onServiceConnected(19, BluetoothHidDevice.this);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            Log.d(BluetoothHidDevice.TAG, "onServiceDisconnected()");
            BluetoothHidDevice.this.mService = null;
            if (BluetoothHidDevice.this.mServiceListener != null) {
                BluetoothHidDevice.this.mServiceListener.onServiceDisconnected(19);
            }
        }
    };
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

    /* loaded from: classes.dex */
    public static abstract class Callback {
        private static final String TAG = "BluetoothHidDevCallback";

        public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
            Log.d(TAG, "onAppStatusChanged: pluggedDevice=" + pluggedDevice + " registered=" + registered);
        }

        public void onConnectionStateChanged(BluetoothDevice device, int state) {
            Log.d(TAG, "onConnectionStateChanged: device=" + device + " state=" + state);
        }

        public void onGetReport(BluetoothDevice device, byte type, byte id, int bufferSize) {
            Log.d(TAG, "onGetReport: device=" + device + " type=" + ((int) type) + " id=" + ((int) id) + " bufferSize=" + bufferSize);
        }

        public void onSetReport(BluetoothDevice device, byte type, byte id, byte[] data) {
            Log.d(TAG, "onSetReport: device=" + device + " type=" + ((int) type) + " id=" + ((int) id));
        }

        public void onSetProtocol(BluetoothDevice device, byte protocol) {
            Log.d(TAG, "onSetProtocol: device=" + device + " protocol=" + ((int) protocol));
        }

        public void onInterruptData(BluetoothDevice device, byte reportId, byte[] data) {
            Log.d(TAG, "onInterruptData: device=" + device + " reportId=" + ((int) reportId));
        }

        public void onVirtualCableUnplug(BluetoothDevice device) {
            Log.d(TAG, "onVirtualCableUnplug: device=" + device);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CallbackWrapper extends IBluetoothHidDeviceCallback.Stub {
        private final Callback mCallback;
        private final Executor mExecutor;

        synchronized CallbackWrapper(Executor executor, Callback callback) {
            this.mExecutor = executor;
            this.mCallback = callback;
        }

        @Override // android.bluetooth.IBluetoothHidDeviceCallback
        public synchronized void onAppStatusChanged(final BluetoothDevice pluggedDevice, final boolean registered) {
            clearCallingIdentity();
            this.mExecutor.execute(new Runnable() { // from class: android.bluetooth.-$$Lambda$BluetoothHidDevice$CallbackWrapper$NFluHjT4zTfYBRXClu_2k6mPKFI
                @Override // java.lang.Runnable
                public final void run() {
                    BluetoothHidDevice.CallbackWrapper.this.mCallback.onAppStatusChanged(pluggedDevice, registered);
                }
            });
        }

        @Override // android.bluetooth.IBluetoothHidDeviceCallback
        public synchronized void onConnectionStateChanged(final BluetoothDevice device, final int state) {
            clearCallingIdentity();
            this.mExecutor.execute(new Runnable() { // from class: android.bluetooth.-$$Lambda$BluetoothHidDevice$CallbackWrapper$qtStwQVkGfOs2iJIiePWqJJpi0w
                @Override // java.lang.Runnable
                public final void run() {
                    BluetoothHidDevice.CallbackWrapper.this.mCallback.onConnectionStateChanged(device, state);
                }
            });
        }

        @Override // android.bluetooth.IBluetoothHidDeviceCallback
        public synchronized void onGetReport(final BluetoothDevice device, final byte type, final byte id, final int bufferSize) {
            clearCallingIdentity();
            this.mExecutor.execute(new Runnable() { // from class: android.bluetooth.-$$Lambda$BluetoothHidDevice$CallbackWrapper$Eyz_qG6mvTlh6a8Bp41ZoEJzQCQ
                @Override // java.lang.Runnable
                public final void run() {
                    BluetoothHidDevice.CallbackWrapper.this.mCallback.onGetReport(device, type, id, bufferSize);
                }
            });
        }

        @Override // android.bluetooth.IBluetoothHidDeviceCallback
        public synchronized void onSetReport(final BluetoothDevice device, final byte type, final byte id, final byte[] data) {
            clearCallingIdentity();
            this.mExecutor.execute(new Runnable() { // from class: android.bluetooth.-$$Lambda$BluetoothHidDevice$CallbackWrapper$3bTGVlfKj7Y0SZdifW_Ya2myDKs
                @Override // java.lang.Runnable
                public final void run() {
                    BluetoothHidDevice.CallbackWrapper.this.mCallback.onSetReport(device, type, id, data);
                }
            });
        }

        @Override // android.bluetooth.IBluetoothHidDeviceCallback
        public synchronized void onSetProtocol(final BluetoothDevice device, final byte protocol) {
            clearCallingIdentity();
            this.mExecutor.execute(new Runnable() { // from class: android.bluetooth.-$$Lambda$BluetoothHidDevice$CallbackWrapper$ypkr5GGxsAkGSBiLjIRwg-PzqCM
                @Override // java.lang.Runnable
                public final void run() {
                    BluetoothHidDevice.CallbackWrapper.this.mCallback.onSetProtocol(device, protocol);
                }
            });
        }

        @Override // android.bluetooth.IBluetoothHidDeviceCallback
        public synchronized void onInterruptData(final BluetoothDevice device, final byte reportId, final byte[] data) {
            clearCallingIdentity();
            this.mExecutor.execute(new Runnable() { // from class: android.bluetooth.-$$Lambda$BluetoothHidDevice$CallbackWrapper$xW99-tc95OmGApoKnpQ9q1TXb9k
                @Override // java.lang.Runnable
                public final void run() {
                    BluetoothHidDevice.CallbackWrapper.this.mCallback.onInterruptData(device, reportId, data);
                }
            });
        }

        @Override // android.bluetooth.IBluetoothHidDeviceCallback
        public synchronized void onVirtualCableUnplug(final BluetoothDevice device) {
            clearCallingIdentity();
            this.mExecutor.execute(new Runnable() { // from class: android.bluetooth.-$$Lambda$BluetoothHidDevice$CallbackWrapper$jiodzbAJAcleQCwlDcBjvDddELM
                @Override // java.lang.Runnable
                public final void run() {
                    BluetoothHidDevice.CallbackWrapper.this.mCallback.onVirtualCableUnplug(device);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BluetoothHidDevice(Context context, BluetoothProfile.ServiceListener listener) {
        this.mContext = context;
        this.mServiceListener = listener;
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        doBind();
    }

    synchronized boolean doBind() {
        Intent intent = new Intent(IBluetoothHidDevice.class.getName());
        ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if (comp == null || !this.mContext.bindServiceAsUser(intent, this.mConnection, 0, this.mContext.getUser())) {
            String str = TAG;
            Log.e(str, "Could not bind to Bluetooth HID Device Service with " + intent);
            return false;
        }
        Log.d(TAG, "Bound to HID Device Service");
        return true;
    }

    synchronized void doUnbind() {
        if (this.mService != null) {
            this.mService = null;
            try {
                this.mContext.unbindService(this.mConnection);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Unable to unbind HidDevService", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void close() {
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        synchronized (this.mConnection) {
            doUnbind();
        }
        this.mServiceListener = null;
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        IBluetoothHidDevice service = this.mService;
        if (service != null) {
            try {
                return service.getConnectedDevices();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
        }
        return new ArrayList();
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        IBluetoothHidDevice service = this.mService;
        if (service != null) {
            try {
                return service.getDevicesMatchingConnectionStates(states);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
        }
        return new ArrayList();
    }

    @Override // android.bluetooth.BluetoothProfile
    public int getConnectionState(BluetoothDevice device) {
        IBluetoothHidDevice service = this.mService;
        if (service != null) {
            try {
                return service.getConnectionState(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return 0;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return 0;
    }

    public boolean registerApp(BluetoothHidDeviceAppSdpSettings sdp, BluetoothHidDeviceAppQosSettings inQos, BluetoothHidDeviceAppQosSettings outQos, Executor executor, Callback callback) {
        if (sdp == null) {
            throw new IllegalArgumentException("sdp parameter cannot be null");
        }
        if (executor == null) {
            throw new IllegalArgumentException("executor parameter cannot be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("callback parameter cannot be null");
        }
        IBluetoothHidDevice service = this.mService;
        if (service != null) {
            try {
                CallbackWrapper cbw = new CallbackWrapper(executor, callback);
                boolean result = service.registerApp(sdp, inQos, outQos, cbw);
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    public boolean unregisterApp() {
        IBluetoothHidDevice service = this.mService;
        if (service != null) {
            try {
                boolean result = service.unregisterApp();
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    public boolean sendReport(BluetoothDevice device, int id, byte[] data) {
        IBluetoothHidDevice service = this.mService;
        if (service != null) {
            try {
                boolean result = service.sendReport(device, id, data);
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    public boolean replyReport(BluetoothDevice device, byte type, byte id, byte[] data) {
        IBluetoothHidDevice service = this.mService;
        if (service != null) {
            try {
                boolean result = service.replyReport(device, type, id, data);
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    public boolean reportError(BluetoothDevice device, byte error) {
        IBluetoothHidDevice service = this.mService;
        if (service != null) {
            try {
                boolean result = service.reportError(device, error);
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    public synchronized String getUserAppName() {
        IBluetoothHidDevice service = this.mService;
        if (service != null) {
            try {
                return service.getUserAppName();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return "";
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return "";
    }

    public boolean connect(BluetoothDevice device) {
        IBluetoothHidDevice service = this.mService;
        if (service != null) {
            try {
                boolean result = service.connect(device);
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }

    public boolean disconnect(BluetoothDevice device) {
        IBluetoothHidDevice service = this.mService;
        if (service != null) {
            try {
                boolean result = service.disconnect(device);
                return result;
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
                return false;
            }
        }
        Log.w(TAG, "Proxy not attached to service");
        return false;
    }
}
