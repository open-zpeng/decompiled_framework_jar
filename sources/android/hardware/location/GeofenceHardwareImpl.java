package android.hardware.location;

import android.Manifest;
import android.content.Context;
import android.location.IFusedGeofenceHardware;
import android.location.IGpsGeofenceHardware;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.SettingsStringUtil;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class GeofenceHardwareImpl {
    private static final int ADD_GEOFENCE_CALLBACK = 2;
    private static final int CALLBACK_ADD = 2;
    private static final int CALLBACK_REMOVE = 3;
    private static final int CAPABILITY_GNSS = 1;
    private static final int FIRST_VERSION_WITH_CAPABILITIES = 2;
    private static final int GEOFENCE_CALLBACK_BINDER_DIED = 6;
    private static final int GEOFENCE_STATUS = 1;
    private static final int GEOFENCE_TRANSITION_CALLBACK = 1;
    private static final int LOCATION_HAS_ACCURACY = 16;
    private static final int LOCATION_HAS_ALTITUDE = 2;
    private static final int LOCATION_HAS_BEARING = 8;
    private static final int LOCATION_HAS_LAT_LONG = 1;
    private static final int LOCATION_HAS_SPEED = 4;
    private static final int LOCATION_INVALID = 0;
    private static final int MONITOR_CALLBACK_BINDER_DIED = 4;
    private static final int PAUSE_GEOFENCE_CALLBACK = 4;
    private static final int REAPER_GEOFENCE_ADDED = 1;
    private static final int REAPER_MONITOR_CALLBACK_ADDED = 2;
    private static final int REAPER_REMOVED = 3;
    private static final int REMOVE_GEOFENCE_CALLBACK = 3;
    private static final int RESOLUTION_LEVEL_COARSE = 2;
    private static final int RESOLUTION_LEVEL_FINE = 3;
    private static final int RESOLUTION_LEVEL_NONE = 1;
    private static final int RESUME_GEOFENCE_CALLBACK = 5;
    private static GeofenceHardwareImpl sInstance;
    private int mCapabilities;
    private final Context mContext;
    private IFusedGeofenceHardware mFusedService;
    private IGpsGeofenceHardware mGpsService;
    private PowerManager.WakeLock mWakeLock;
    private static final String TAG = "GeofenceHardwareImpl";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private final SparseArray<IGeofenceHardwareCallback> mGeofences = new SparseArray<>();
    private final ArrayList<IGeofenceHardwareMonitorCallback>[] mCallbacks = new ArrayList[2];
    private final ArrayList<Reaper> mReapers = new ArrayList<>();
    private int mVersion = 1;
    private int[] mSupportedMonitorTypes = new int[2];
    private Handler mGeofenceHandler = new Handler() { // from class: android.hardware.location.GeofenceHardwareImpl.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            IGeofenceHardwareCallback callback;
            IGeofenceHardwareCallback callback2;
            IGeofenceHardwareCallback callback3;
            IGeofenceHardwareCallback callback4;
            IGeofenceHardwareCallback callback5;
            switch (msg.what) {
                case 1:
                    GeofenceTransition geofenceTransition = (GeofenceTransition) msg.obj;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceTransition.mGeofenceId);
                        if (GeofenceHardwareImpl.DEBUG) {
                            Log.d(GeofenceHardwareImpl.TAG, "GeofenceTransistionCallback: GPS : GeofenceId: " + geofenceTransition.mGeofenceId + " Transition: " + geofenceTransition.mTransition + " Location: " + geofenceTransition.mLocation + SettingsStringUtil.DELIMITER + GeofenceHardwareImpl.this.mGeofences);
                        }
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceTransition(geofenceTransition.mGeofenceId, geofenceTransition.mTransition, geofenceTransition.mLocation, geofenceTransition.mTimestamp, geofenceTransition.mMonitoringType);
                        } catch (RemoteException e) {
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 2:
                    int geofenceId = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback2 = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId);
                    }
                    if (callback2 != null) {
                        try {
                            callback2.onGeofenceAdd(geofenceId, msg.arg2);
                        } catch (RemoteException e2) {
                            Log.i(GeofenceHardwareImpl.TAG, "Remote Exception:" + e2);
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 3:
                    int geofenceId2 = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback3 = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId2);
                    }
                    if (callback3 != null) {
                        try {
                            callback3.onGeofenceRemove(geofenceId2, msg.arg2);
                        } catch (RemoteException e3) {
                        }
                        IBinder callbackBinder = callback3.asBinder();
                        boolean callbackInUse = false;
                        synchronized (GeofenceHardwareImpl.this.mGeofences) {
                            GeofenceHardwareImpl.this.mGeofences.remove(geofenceId2);
                            int i = 0;
                            while (true) {
                                if (i < GeofenceHardwareImpl.this.mGeofences.size()) {
                                    if (((IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.valueAt(i)).asBinder() != callbackBinder) {
                                        i++;
                                    } else {
                                        callbackInUse = true;
                                    }
                                }
                            }
                        }
                        if (!callbackInUse) {
                            Iterator<Reaper> iterator = GeofenceHardwareImpl.this.mReapers.iterator();
                            while (iterator.hasNext()) {
                                Reaper reaper = iterator.next();
                                if (reaper.mCallback != null && reaper.mCallback.asBinder() == callbackBinder) {
                                    iterator.remove();
                                    reaper.unlinkToDeath();
                                    if (GeofenceHardwareImpl.DEBUG) {
                                        Log.d(GeofenceHardwareImpl.TAG, String.format("Removed reaper %s because binder %s is no longer needed.", reaper, callbackBinder));
                                    }
                                }
                            }
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 4:
                    int geofenceId3 = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback4 = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId3);
                    }
                    if (callback4 != null) {
                        try {
                            callback4.onGeofencePause(geofenceId3, msg.arg2);
                        } catch (RemoteException e4) {
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 5:
                    int geofenceId4 = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback5 = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId4);
                    }
                    if (callback5 != null) {
                        try {
                            callback5.onGeofenceResume(geofenceId4, msg.arg2);
                        } catch (RemoteException e5) {
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 6:
                    IGeofenceHardwareCallback callback6 = (IGeofenceHardwareCallback) msg.obj;
                    if (GeofenceHardwareImpl.DEBUG) {
                        Log.d(GeofenceHardwareImpl.TAG, "Geofence callback reaped:" + callback6);
                    }
                    int monitoringType = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        for (int i2 = 0; i2 < GeofenceHardwareImpl.this.mGeofences.size(); i2++) {
                            if (((IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.valueAt(i2)).equals(callback6)) {
                                int geofenceId5 = GeofenceHardwareImpl.this.mGeofences.keyAt(i2);
                                GeofenceHardwareImpl.this.removeGeofence(GeofenceHardwareImpl.this.mGeofences.keyAt(i2), monitoringType);
                                GeofenceHardwareImpl.this.mGeofences.remove(geofenceId5);
                            }
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private Handler mCallbacksHandler = new Handler() { // from class: android.hardware.location.GeofenceHardwareImpl.2
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                GeofenceHardwareMonitorEvent event = (GeofenceHardwareMonitorEvent) msg.obj;
                ArrayList<IGeofenceHardwareMonitorCallback> callbackList = GeofenceHardwareImpl.this.mCallbacks[event.getMonitoringType()];
                if (callbackList != null) {
                    if (GeofenceHardwareImpl.DEBUG) {
                        Log.d(GeofenceHardwareImpl.TAG, "MonitoringSystemChangeCallback: " + event);
                    }
                    Iterator<IGeofenceHardwareMonitorCallback> it = callbackList.iterator();
                    while (it.hasNext()) {
                        IGeofenceHardwareMonitorCallback c = it.next();
                        try {
                            c.onMonitoringSystemChange(event);
                        } catch (RemoteException e) {
                            Log.d(GeofenceHardwareImpl.TAG, "Error reporting onMonitoringSystemChange.", e);
                        }
                    }
                }
                GeofenceHardwareImpl.this.releaseWakeLock();
            } else if (i == 2) {
                int monitoringType = msg.arg1;
                IGeofenceHardwareMonitorCallback callback = (IGeofenceHardwareMonitorCallback) msg.obj;
                ArrayList<IGeofenceHardwareMonitorCallback> callbackList2 = GeofenceHardwareImpl.this.mCallbacks[monitoringType];
                if (callbackList2 == null) {
                    callbackList2 = new ArrayList<>();
                    GeofenceHardwareImpl.this.mCallbacks[monitoringType] = callbackList2;
                }
                if (!callbackList2.contains(callback)) {
                    callbackList2.add(callback);
                }
            } else if (i == 3) {
                int monitoringType2 = msg.arg1;
                IGeofenceHardwareMonitorCallback callback2 = (IGeofenceHardwareMonitorCallback) msg.obj;
                ArrayList<IGeofenceHardwareMonitorCallback> callbackList3 = GeofenceHardwareImpl.this.mCallbacks[monitoringType2];
                if (callbackList3 != null) {
                    callbackList3.remove(callback2);
                }
            } else if (i == 4) {
                IGeofenceHardwareMonitorCallback callback3 = (IGeofenceHardwareMonitorCallback) msg.obj;
                if (GeofenceHardwareImpl.DEBUG) {
                    Log.d(GeofenceHardwareImpl.TAG, "Monitor callback reaped:" + callback3);
                }
                ArrayList<IGeofenceHardwareMonitorCallback> callbackList4 = GeofenceHardwareImpl.this.mCallbacks[msg.arg1];
                if (callbackList4 != null && callbackList4.contains(callback3)) {
                    callbackList4.remove(callback3);
                }
            }
        }
    };
    private Handler mReaperHandler = new Handler() { // from class: android.hardware.location.GeofenceHardwareImpl.3
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1) {
                IGeofenceHardwareCallback callback = (IGeofenceHardwareCallback) msg.obj;
                int monitoringType = msg.arg1;
                Reaper r = new Reaper(callback, monitoringType);
                if (!GeofenceHardwareImpl.this.mReapers.contains(r)) {
                    GeofenceHardwareImpl.this.mReapers.add(r);
                    IBinder b = callback.asBinder();
                    try {
                        b.linkToDeath(r, 0);
                    } catch (RemoteException e) {
                    }
                }
            } else if (i != 2) {
                if (i == 3) {
                    GeofenceHardwareImpl.this.mReapers.remove((Reaper) msg.obj);
                }
            } else {
                IGeofenceHardwareMonitorCallback monitorCallback = (IGeofenceHardwareMonitorCallback) msg.obj;
                int monitoringType2 = msg.arg1;
                Reaper r2 = new Reaper(monitorCallback, monitoringType2);
                if (!GeofenceHardwareImpl.this.mReapers.contains(r2)) {
                    GeofenceHardwareImpl.this.mReapers.add(r2);
                    IBinder b2 = monitorCallback.asBinder();
                    try {
                        b2.linkToDeath(r2, 0);
                    } catch (RemoteException e2) {
                    }
                }
            }
        }
    };

    public static synchronized GeofenceHardwareImpl getInstance(Context context) {
        GeofenceHardwareImpl geofenceHardwareImpl;
        synchronized (GeofenceHardwareImpl.class) {
            if (sInstance == null) {
                sInstance = new GeofenceHardwareImpl(context);
            }
            geofenceHardwareImpl = sInstance;
        }
        return geofenceHardwareImpl;
    }

    private GeofenceHardwareImpl(Context context) {
        this.mContext = context;
        setMonitorAvailability(0, 2);
        setMonitorAvailability(1, 2);
    }

    private void acquireWakeLock() {
        if (this.mWakeLock == null) {
            PowerManager powerManager = (PowerManager) this.mContext.getSystemService(Context.POWER_SERVICE);
            this.mWakeLock = powerManager.newWakeLock(1, TAG);
        }
        this.mWakeLock.acquire();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseWakeLock() {
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
    }

    private void updateGpsHardwareAvailability() {
        boolean gpsSupported;
        try {
            gpsSupported = this.mGpsService.isHardwareGeofenceSupported();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote Exception calling LocationManagerService");
            gpsSupported = false;
        }
        if (gpsSupported) {
            setMonitorAvailability(0, 0);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0014 A[Catch: RemoteException -> 0x0025, TRY_LEAVE, TryCatch #0 {RemoteException -> 0x0025, blocks: (B:3:0x0002, B:5:0x0007, B:10:0x0010, B:12:0x0014), top: B:24:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0030  */
    /* JADX WARN: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void updateFusedHardwareAvailability() {
        /*
            r5 = this;
            r0 = 0
            r1 = 1
            int r2 = r5.mVersion     // Catch: android.os.RemoteException -> L25
            r3 = 2
            if (r2 < r3) goto Lf
            int r2 = r5.mCapabilities     // Catch: android.os.RemoteException -> L25
            r2 = r2 & r1
            if (r2 == 0) goto Ld
            goto Lf
        Ld:
            r2 = r0
            goto L10
        Lf:
            r2 = r1
        L10:
            android.location.IFusedGeofenceHardware r3 = r5.mFusedService     // Catch: android.os.RemoteException -> L25
            if (r3 == 0) goto L22
            android.location.IFusedGeofenceHardware r3 = r5.mFusedService     // Catch: android.os.RemoteException -> L25
            boolean r3 = r3.isSupported()     // Catch: android.os.RemoteException -> L25
            if (r3 == 0) goto L20
            if (r2 == 0) goto L20
            r3 = r1
            goto L23
        L20:
            r3 = r0
            goto L23
        L22:
            r3 = r0
        L23:
            r2 = r3
            goto L2e
        L25:
            r2 = move-exception
            java.lang.String r3 = "GeofenceHardwareImpl"
            java.lang.String r4 = "RemoteException calling LocationManagerService"
            android.util.Log.e(r3, r4)
            r2 = 0
        L2e:
            if (r2 == 0) goto L33
            r5.setMonitorAvailability(r1, r0)
        L33:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.location.GeofenceHardwareImpl.updateFusedHardwareAvailability():void");
    }

    public void setGpsHardwareGeofence(IGpsGeofenceHardware service) {
        if (this.mGpsService == null) {
            this.mGpsService = service;
            updateGpsHardwareAvailability();
        } else if (service != null) {
            Log.e(TAG, "Error: GpsService being set again.");
        } else {
            this.mGpsService = null;
            Log.w(TAG, "GPS Geofence Hardware service seems to have crashed");
        }
    }

    public void onCapabilities(int capabilities) {
        this.mCapabilities = capabilities;
        updateFusedHardwareAvailability();
    }

    public void setVersion(int version) {
        this.mVersion = version;
        updateFusedHardwareAvailability();
    }

    public void setFusedGeofenceHardware(IFusedGeofenceHardware service) {
        if (this.mFusedService == null) {
            this.mFusedService = service;
            updateFusedHardwareAvailability();
        } else if (service != null) {
            Log.e(TAG, "Error: FusedService being set again");
        } else {
            this.mFusedService = null;
            Log.w(TAG, "Fused Geofence Hardware service seems to have crashed");
        }
    }

    public int[] getMonitoringTypes() {
        boolean gpsSupported;
        boolean fusedSupported;
        synchronized (this.mSupportedMonitorTypes) {
            gpsSupported = this.mSupportedMonitorTypes[0] != 2;
            fusedSupported = this.mSupportedMonitorTypes[1] != 2;
        }
        return gpsSupported ? fusedSupported ? new int[]{0, 1} : new int[]{0} : fusedSupported ? new int[]{1} : new int[0];
    }

    public int getStatusOfMonitoringType(int monitoringType) {
        int i;
        synchronized (this.mSupportedMonitorTypes) {
            if (monitoringType >= this.mSupportedMonitorTypes.length || monitoringType < 0) {
                throw new IllegalArgumentException("Unknown monitoring type");
            }
            i = this.mSupportedMonitorTypes[monitoringType];
        }
        return i;
    }

    public int getCapabilitiesForMonitoringType(int monitoringType) {
        if (this.mSupportedMonitorTypes[monitoringType] != 0) {
            return 0;
        }
        if (monitoringType != 0) {
            if (monitoringType == 1) {
                if (this.mVersion < 2) {
                    return 1;
                }
                return this.mCapabilities;
            }
            return 0;
        }
        return 1;
    }

    public boolean addCircularFence(int monitoringType, GeofenceHardwareRequestParcelable request, IGeofenceHardwareCallback callback) {
        boolean result;
        int geofenceId = request.getId();
        if (DEBUG) {
            String message = String.format("addCircularFence: monitoringType=%d, %s", Integer.valueOf(monitoringType), request);
            Log.d(TAG, message);
        }
        synchronized (this.mGeofences) {
            this.mGeofences.put(geofenceId, callback);
        }
        if (monitoringType == 0) {
            IGpsGeofenceHardware iGpsGeofenceHardware = this.mGpsService;
            if (iGpsGeofenceHardware == null) {
                return false;
            }
            try {
                boolean result2 = iGpsGeofenceHardware.addCircularHardwareGeofence(request.getId(), request.getLatitude(), request.getLongitude(), request.getRadius(), request.getLastTransition(), request.getMonitorTransitions(), request.getNotificationResponsiveness(), request.getUnknownTimer());
                result = result2;
            } catch (RemoteException e) {
                Log.e(TAG, "AddGeofence: Remote Exception calling LocationManagerService");
                result = false;
            }
        } else if (monitoringType == 1) {
            IFusedGeofenceHardware iFusedGeofenceHardware = this.mFusedService;
            if (iFusedGeofenceHardware == null) {
                return false;
            }
            try {
                iFusedGeofenceHardware.addGeofences(new GeofenceHardwareRequestParcelable[]{request});
                result = true;
            } catch (RemoteException e2) {
                Log.e(TAG, "AddGeofence: RemoteException calling LocationManagerService");
                result = false;
            }
        } else {
            result = false;
        }
        if (result) {
            Message m = this.mReaperHandler.obtainMessage(1, callback);
            m.arg1 = monitoringType;
            this.mReaperHandler.sendMessage(m);
        } else {
            synchronized (this.mGeofences) {
                this.mGeofences.remove(geofenceId);
            }
        }
        if (DEBUG) {
            Log.d(TAG, "addCircularFence: Result is: " + result);
        }
        return result;
    }

    public boolean removeGeofence(int geofenceId, int monitoringType) {
        boolean result;
        if (DEBUG) {
            Log.d(TAG, "Remove Geofence: GeofenceId: " + geofenceId);
        }
        synchronized (this.mGeofences) {
            if (this.mGeofences.get(geofenceId) == null) {
                throw new IllegalArgumentException("Geofence " + geofenceId + " not registered.");
            }
        }
        if (monitoringType == 0) {
            IGpsGeofenceHardware iGpsGeofenceHardware = this.mGpsService;
            if (iGpsGeofenceHardware == null) {
                return false;
            }
            try {
                result = iGpsGeofenceHardware.removeHardwareGeofence(geofenceId);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoveGeofence: Remote Exception calling LocationManagerService");
                result = false;
            }
        } else if (monitoringType == 1) {
            IFusedGeofenceHardware iFusedGeofenceHardware = this.mFusedService;
            if (iFusedGeofenceHardware == null) {
                return false;
            }
            try {
                iFusedGeofenceHardware.removeGeofences(new int[]{geofenceId});
                result = true;
            } catch (RemoteException e2) {
                Log.e(TAG, "RemoveGeofence: RemoteException calling LocationManagerService");
                result = false;
            }
        } else {
            result = false;
        }
        if (DEBUG) {
            Log.d(TAG, "removeGeofence: Result is: " + result);
        }
        return result;
    }

    public boolean pauseGeofence(int geofenceId, int monitoringType) {
        boolean result;
        if (DEBUG) {
            Log.d(TAG, "Pause Geofence: GeofenceId: " + geofenceId);
        }
        synchronized (this.mGeofences) {
            if (this.mGeofences.get(geofenceId) == null) {
                throw new IllegalArgumentException("Geofence " + geofenceId + " not registered.");
            }
        }
        if (monitoringType == 0) {
            IGpsGeofenceHardware iGpsGeofenceHardware = this.mGpsService;
            if (iGpsGeofenceHardware == null) {
                return false;
            }
            try {
                result = iGpsGeofenceHardware.pauseHardwareGeofence(geofenceId);
            } catch (RemoteException e) {
                Log.e(TAG, "PauseGeofence: Remote Exception calling LocationManagerService");
                result = false;
            }
        } else if (monitoringType == 1) {
            IFusedGeofenceHardware iFusedGeofenceHardware = this.mFusedService;
            if (iFusedGeofenceHardware == null) {
                return false;
            }
            try {
                iFusedGeofenceHardware.pauseMonitoringGeofence(geofenceId);
                result = true;
            } catch (RemoteException e2) {
                Log.e(TAG, "PauseGeofence: RemoteException calling LocationManagerService");
                result = false;
            }
        } else {
            result = false;
        }
        if (DEBUG) {
            Log.d(TAG, "pauseGeofence: Result is: " + result);
        }
        return result;
    }

    public boolean resumeGeofence(int geofenceId, int monitoringType, int monitorTransition) {
        boolean result;
        if (DEBUG) {
            Log.d(TAG, "Resume Geofence: GeofenceId: " + geofenceId);
        }
        synchronized (this.mGeofences) {
            if (this.mGeofences.get(geofenceId) == null) {
                throw new IllegalArgumentException("Geofence " + geofenceId + " not registered.");
            }
        }
        if (monitoringType == 0) {
            IGpsGeofenceHardware iGpsGeofenceHardware = this.mGpsService;
            if (iGpsGeofenceHardware == null) {
                return false;
            }
            try {
                result = iGpsGeofenceHardware.resumeHardwareGeofence(geofenceId, monitorTransition);
            } catch (RemoteException e) {
                Log.e(TAG, "ResumeGeofence: Remote Exception calling LocationManagerService");
                result = false;
            }
        } else if (monitoringType == 1) {
            IFusedGeofenceHardware iFusedGeofenceHardware = this.mFusedService;
            if (iFusedGeofenceHardware == null) {
                return false;
            }
            try {
                iFusedGeofenceHardware.resumeMonitoringGeofence(geofenceId, monitorTransition);
                result = true;
            } catch (RemoteException e2) {
                Log.e(TAG, "ResumeGeofence: RemoteException calling LocationManagerService");
                result = false;
            }
        } else {
            result = false;
        }
        if (DEBUG) {
            Log.d(TAG, "resumeGeofence: Result is: " + result);
        }
        return result;
    }

    public boolean registerForMonitorStateChangeCallback(int monitoringType, IGeofenceHardwareMonitorCallback callback) {
        Message reaperMessage = this.mReaperHandler.obtainMessage(2, callback);
        reaperMessage.arg1 = monitoringType;
        this.mReaperHandler.sendMessage(reaperMessage);
        Message m = this.mCallbacksHandler.obtainMessage(2, callback);
        m.arg1 = monitoringType;
        this.mCallbacksHandler.sendMessage(m);
        return true;
    }

    public boolean unregisterForMonitorStateChangeCallback(int monitoringType, IGeofenceHardwareMonitorCallback callback) {
        Message m = this.mCallbacksHandler.obtainMessage(3, callback);
        m.arg1 = monitoringType;
        this.mCallbacksHandler.sendMessage(m);
        return true;
    }

    public void reportGeofenceTransition(int geofenceId, Location location, int transition, long transitionTimestamp, int monitoringType, int sourcesUsed) {
        if (location == null) {
            Log.e(TAG, String.format("Invalid Geofence Transition: location=null", new Object[0]));
            return;
        }
        if (DEBUG) {
            Log.d(TAG, "GeofenceTransition| " + location + ", transition:" + transition + ", transitionTimestamp:" + transitionTimestamp + ", monitoringType:" + monitoringType + ", sourcesUsed:" + sourcesUsed);
        }
        GeofenceTransition geofenceTransition = new GeofenceTransition(geofenceId, transition, transitionTimestamp, location, monitoringType, sourcesUsed);
        acquireWakeLock();
        Message message = this.mGeofenceHandler.obtainMessage(1, geofenceTransition);
        message.sendToTarget();
    }

    public void reportGeofenceMonitorStatus(int monitoringType, int monitoringStatus, Location location, int source) {
        setMonitorAvailability(monitoringType, monitoringStatus);
        acquireWakeLock();
        GeofenceHardwareMonitorEvent event = new GeofenceHardwareMonitorEvent(monitoringType, monitoringStatus, source, location);
        Message message = this.mCallbacksHandler.obtainMessage(1, event);
        message.sendToTarget();
    }

    private void reportGeofenceOperationStatus(int operation, int geofenceId, int operationStatus) {
        acquireWakeLock();
        Message message = this.mGeofenceHandler.obtainMessage(operation);
        message.arg1 = geofenceId;
        message.arg2 = operationStatus;
        message.sendToTarget();
    }

    public void reportGeofenceAddStatus(int geofenceId, int status) {
        if (DEBUG) {
            Log.d(TAG, "AddCallback| id:" + geofenceId + ", status:" + status);
        }
        reportGeofenceOperationStatus(2, geofenceId, status);
    }

    public void reportGeofenceRemoveStatus(int geofenceId, int status) {
        if (DEBUG) {
            Log.d(TAG, "RemoveCallback| id:" + geofenceId + ", status:" + status);
        }
        reportGeofenceOperationStatus(3, geofenceId, status);
    }

    public void reportGeofencePauseStatus(int geofenceId, int status) {
        if (DEBUG) {
            Log.d(TAG, "PauseCallbac| id:" + geofenceId + ", status" + status);
        }
        reportGeofenceOperationStatus(4, geofenceId, status);
    }

    public void reportGeofenceResumeStatus(int geofenceId, int status) {
        if (DEBUG) {
            Log.d(TAG, "ResumeCallback| id:" + geofenceId + ", status:" + status);
        }
        reportGeofenceOperationStatus(5, geofenceId, status);
    }

    /* loaded from: classes.dex */
    private class GeofenceTransition {
        private int mGeofenceId;
        private Location mLocation;
        private int mMonitoringType;
        private int mSourcesUsed;
        private long mTimestamp;
        private int mTransition;

        GeofenceTransition(int geofenceId, int transition, long timestamp, Location location, int monitoringType, int sourcesUsed) {
            this.mGeofenceId = geofenceId;
            this.mTransition = transition;
            this.mTimestamp = timestamp;
            this.mLocation = location;
            this.mMonitoringType = monitoringType;
            this.mSourcesUsed = sourcesUsed;
        }
    }

    private void setMonitorAvailability(int monitor, int val) {
        synchronized (this.mSupportedMonitorTypes) {
            this.mSupportedMonitorTypes[monitor] = val;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMonitoringResolutionLevel(int monitoringType) {
        return (monitoringType == 0 || monitoringType == 1) ? 3 : 1;
    }

    /* loaded from: classes.dex */
    class Reaper implements IBinder.DeathRecipient {
        private IGeofenceHardwareCallback mCallback;
        private IGeofenceHardwareMonitorCallback mMonitorCallback;
        private int mMonitoringType;

        Reaper(IGeofenceHardwareCallback c, int monitoringType) {
            this.mCallback = c;
            this.mMonitoringType = monitoringType;
        }

        Reaper(IGeofenceHardwareMonitorCallback c, int monitoringType) {
            this.mMonitorCallback = c;
            this.mMonitoringType = monitoringType;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (this.mCallback != null) {
                Message m = GeofenceHardwareImpl.this.mGeofenceHandler.obtainMessage(6, this.mCallback);
                m.arg1 = this.mMonitoringType;
                GeofenceHardwareImpl.this.mGeofenceHandler.sendMessage(m);
            } else if (this.mMonitorCallback != null) {
                Message m2 = GeofenceHardwareImpl.this.mCallbacksHandler.obtainMessage(4, this.mMonitorCallback);
                m2.arg1 = this.mMonitoringType;
                GeofenceHardwareImpl.this.mCallbacksHandler.sendMessage(m2);
            }
            Message reaperMessage = GeofenceHardwareImpl.this.mReaperHandler.obtainMessage(3, this);
            GeofenceHardwareImpl.this.mReaperHandler.sendMessage(reaperMessage);
        }

        public int hashCode() {
            int i = 17 * 31;
            IGeofenceHardwareCallback iGeofenceHardwareCallback = this.mCallback;
            int result = i + (iGeofenceHardwareCallback != null ? iGeofenceHardwareCallback.asBinder().hashCode() : 0);
            int result2 = result * 31;
            IGeofenceHardwareMonitorCallback iGeofenceHardwareMonitorCallback = this.mMonitorCallback;
            return ((result2 + (iGeofenceHardwareMonitorCallback != null ? iGeofenceHardwareMonitorCallback.asBinder().hashCode() : 0)) * 31) + this.mMonitoringType;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            Reaper rhs = (Reaper) obj;
            if (!binderEquals(rhs.mCallback, this.mCallback) || !binderEquals(rhs.mMonitorCallback, this.mMonitorCallback) || rhs.mMonitoringType != this.mMonitoringType) {
                return false;
            }
            return true;
        }

        private boolean binderEquals(IInterface left, IInterface right) {
            return left == null ? right == null : right != null && left.asBinder() == right.asBinder();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean unlinkToDeath() {
            IGeofenceHardwareMonitorCallback iGeofenceHardwareMonitorCallback = this.mMonitorCallback;
            if (iGeofenceHardwareMonitorCallback != null) {
                return iGeofenceHardwareMonitorCallback.asBinder().unlinkToDeath(this, 0);
            }
            IGeofenceHardwareCallback iGeofenceHardwareCallback = this.mCallback;
            if (iGeofenceHardwareCallback != null) {
                return iGeofenceHardwareCallback.asBinder().unlinkToDeath(this, 0);
            }
            return true;
        }

        private boolean callbackEquals(IGeofenceHardwareCallback cb) {
            IGeofenceHardwareCallback iGeofenceHardwareCallback = this.mCallback;
            return iGeofenceHardwareCallback != null && iGeofenceHardwareCallback.asBinder() == cb.asBinder();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAllowedResolutionLevel(int pid, int uid) {
        if (this.mContext.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, pid, uid) == 0) {
            return 3;
        }
        if (this.mContext.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, pid, uid) == 0) {
            return 2;
        }
        return 1;
    }
}
