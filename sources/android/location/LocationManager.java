package android.location;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.content.Context;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.GpsMeasurementsEvent;
import android.location.GpsNavigationMessageEvent;
import android.location.GpsStatus;
import android.location.IGnssStatusListener;
import android.location.ILocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.location.ProviderProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes.dex */
public class LocationManager {
    public static final String EXTRA_GPS_ENABLED = "enabled";
    public static final String FUSED_PROVIDER = "fused";
    public static final String GPS_ENABLED_CHANGE_ACTION = "android.location.GPS_ENABLED_CHANGE";
    public static final String GPS_FIX_CHANGE_ACTION = "android.location.GPS_FIX_CHANGE";
    public static final String GPS_PROVIDER = "gps";
    public static final String HIGH_POWER_REQUEST_CHANGE_ACTION = "android.location.HIGH_POWER_REQUEST_CHANGE";
    public static final String KEY_LOCATION_CHANGED = "location";
    public static final String KEY_PROVIDER_ENABLED = "providerEnabled";
    public static final String KEY_PROXIMITY_ENTERING = "entering";
    public static final String KEY_STATUS_CHANGED = "status";
    public static final String METADATA_SETTINGS_FOOTER_STRING = "com.android.settings.location.FOOTER_STRING";
    public static final String MODE_CHANGED_ACTION = "android.location.MODE_CHANGED";
    public static final String MODE_CHANGING_ACTION = "com.android.settings.location.MODE_CHANGING";
    public static final String NETWORK_PROVIDER = "network";
    public static final String PASSIVE_PROVIDER = "passive";
    public static final String PROVIDERS_CHANGED_ACTION = "android.location.PROVIDERS_CHANGED";
    public static final String SETTINGS_FOOTER_DISPLAYED_ACTION = "com.android.settings.location.DISPLAYED_FOOTER";
    public static final String SETTINGS_FOOTER_REMOVED_ACTION = "com.android.settings.location.REMOVED_FOOTER";
    private static final String TAG = "LocationManager";
    private final BatchedLocationCallbackTransport mBatchedLocationCallbackTransport;
    private final Context mContext;
    private final GnssMeasurementCallbackTransport mGnssMeasurementCallbackTransport;
    private final GnssNavigationMessageCallbackTransport mGnssNavigationMessageCallbackTransport;
    private volatile GnssStatus mGnssStatus;
    public protected final ILocationManager mService;
    private int mTimeToFirstFix;
    private final HashMap<GpsStatus.Listener, GnssStatusListenerTransport> mGpsStatusListeners = new HashMap<>();
    private final HashMap<GpsStatus.NmeaListener, GnssStatusListenerTransport> mGpsNmeaListeners = new HashMap<>();
    private final HashMap<GnssStatus.Callback, GnssStatusListenerTransport> mGnssStatusListeners = new HashMap<>();
    private final HashMap<OnNmeaMessageListener, GnssStatusListenerTransport> mGnssNmeaListeners = new HashMap<>();
    private HashMap<LocationListener, ListenerTransport> mListeners = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ListenerTransport extends ILocationListener.Stub {
        private static final int TYPE_LOCATION_CHANGED = 1;
        private static final int TYPE_PROVIDER_DISABLED = 4;
        private static final int TYPE_PROVIDER_ENABLED = 3;
        private static final int TYPE_STATUS_CHANGED = 2;
        private LocationListener mListener;
        private final Handler mListenerHandler;

        ListenerTransport(LocationListener listener, Looper looper) {
            this.mListener = listener;
            if (looper == null) {
                this.mListenerHandler = new Handler() { // from class: android.location.LocationManager.ListenerTransport.1
                    @Override // android.os.Handler
                    public void handleMessage(Message msg) {
                        ListenerTransport.this._handleMessage(msg);
                    }
                };
            } else {
                this.mListenerHandler = new Handler(looper) { // from class: android.location.LocationManager.ListenerTransport.2
                    @Override // android.os.Handler
                    public void handleMessage(Message msg) {
                        ListenerTransport.this._handleMessage(msg);
                    }
                };
            }
        }

        public synchronized void onLocationChanged(Location location) {
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = location;
            this.mListenerHandler.sendMessage(msg);
        }

        public synchronized void onStatusChanged(String provider, int status, Bundle extras) {
            Message msg = Message.obtain();
            msg.what = 2;
            Bundle b = new Bundle();
            b.putString("provider", provider);
            b.putInt("status", status);
            if (extras != null) {
                b.putBundle("extras", extras);
            }
            msg.obj = b;
            this.mListenerHandler.sendMessage(msg);
        }

        public synchronized void onProviderEnabled(String provider) {
            Message msg = Message.obtain();
            msg.what = 3;
            msg.obj = provider;
            this.mListenerHandler.sendMessage(msg);
        }

        public synchronized void onProviderDisabled(String provider) {
            Message msg = Message.obtain();
            msg.what = 4;
            msg.obj = provider;
            this.mListenerHandler.sendMessage(msg);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void _handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Location location = new Location((Location) msg.obj);
                    this.mListener.onLocationChanged(location);
                    break;
                case 2:
                    Bundle b = (Bundle) msg.obj;
                    String provider = b.getString("provider");
                    int status = b.getInt("status");
                    Bundle extras = b.getBundle("extras");
                    this.mListener.onStatusChanged(provider, status, extras);
                    break;
                case 3:
                    this.mListener.onProviderEnabled((String) msg.obj);
                    break;
                case 4:
                    this.mListener.onProviderDisabled((String) msg.obj);
                    break;
            }
            try {
                LocationManager.this.mService.locationCallbackFinished(this);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public String[] getBackgroundThrottlingWhitelist() {
        try {
            return this.mService.getBackgroundThrottlingWhitelist();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized LocationManager(Context context, ILocationManager service) {
        this.mService = service;
        this.mContext = context;
        this.mGnssMeasurementCallbackTransport = new GnssMeasurementCallbackTransport(this.mContext, this.mService);
        this.mGnssNavigationMessageCallbackTransport = new GnssNavigationMessageCallbackTransport(this.mContext, this.mService);
        this.mBatchedLocationCallbackTransport = new BatchedLocationCallbackTransport(this.mContext, this.mService);
    }

    private synchronized LocationProvider createProvider(String name, ProviderProperties properties) {
        return new LocationProvider(name, properties);
    }

    public List<String> getAllProviders() {
        try {
            return this.mService.getAllProviders();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<String> getProviders(boolean enabledOnly) {
        try {
            return this.mService.getProviders(null, enabledOnly);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public LocationProvider getProvider(String name) {
        checkProvider(name);
        try {
            ProviderProperties properties = this.mService.getProviderProperties(name);
            if (properties == null) {
                return null;
            }
            return createProvider(name, properties);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<String> getProviders(Criteria criteria, boolean enabledOnly) {
        checkCriteria(criteria);
        try {
            return this.mService.getProviders(criteria, enabledOnly);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getBestProvider(Criteria criteria, boolean enabledOnly) {
        checkCriteria(criteria);
        try {
            return this.mService.getBestProvider(criteria, enabledOnly);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener) {
        checkProvider(provider);
        checkListener(listener);
        LocationRequest request = LocationRequest.createFromDeprecatedProvider(provider, minTime, minDistance, false);
        requestLocationUpdates(request, listener, (Looper) null, (PendingIntent) null);
    }

    public void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener, Looper looper) {
        checkProvider(provider);
        checkListener(listener);
        LocationRequest request = LocationRequest.createFromDeprecatedProvider(provider, minTime, minDistance, false);
        requestLocationUpdates(request, listener, looper, (PendingIntent) null);
    }

    public void requestLocationUpdates(long minTime, float minDistance, Criteria criteria, LocationListener listener, Looper looper) {
        checkCriteria(criteria);
        checkListener(listener);
        LocationRequest request = LocationRequest.createFromDeprecatedCriteria(criteria, minTime, minDistance, false);
        requestLocationUpdates(request, listener, looper, (PendingIntent) null);
    }

    public void requestLocationUpdates(String provider, long minTime, float minDistance, PendingIntent intent) {
        checkProvider(provider);
        checkPendingIntent(intent);
        LocationRequest request = LocationRequest.createFromDeprecatedProvider(provider, minTime, minDistance, false);
        requestLocationUpdates(request, (LocationListener) null, (Looper) null, intent);
    }

    public void requestLocationUpdates(long minTime, float minDistance, Criteria criteria, PendingIntent intent) {
        checkCriteria(criteria);
        checkPendingIntent(intent);
        LocationRequest request = LocationRequest.createFromDeprecatedCriteria(criteria, minTime, minDistance, false);
        requestLocationUpdates(request, (LocationListener) null, (Looper) null, intent);
    }

    public void requestSingleUpdate(String provider, LocationListener listener, Looper looper) {
        checkProvider(provider);
        checkListener(listener);
        LocationRequest request = LocationRequest.createFromDeprecatedProvider(provider, 0L, 0.0f, true);
        requestLocationUpdates(request, listener, looper, (PendingIntent) null);
    }

    public void requestSingleUpdate(Criteria criteria, LocationListener listener, Looper looper) {
        checkCriteria(criteria);
        checkListener(listener);
        LocationRequest request = LocationRequest.createFromDeprecatedCriteria(criteria, 0L, 0.0f, true);
        requestLocationUpdates(request, listener, looper, (PendingIntent) null);
    }

    public void requestSingleUpdate(String provider, PendingIntent intent) {
        checkProvider(provider);
        checkPendingIntent(intent);
        LocationRequest request = LocationRequest.createFromDeprecatedProvider(provider, 0L, 0.0f, true);
        requestLocationUpdates(request, (LocationListener) null, (Looper) null, intent);
    }

    public void requestSingleUpdate(Criteria criteria, PendingIntent intent) {
        checkCriteria(criteria);
        checkPendingIntent(intent);
        LocationRequest request = LocationRequest.createFromDeprecatedCriteria(criteria, 0L, 0.0f, true);
        requestLocationUpdates(request, (LocationListener) null, (Looper) null, intent);
    }

    @SystemApi
    public void requestLocationUpdates(LocationRequest request, LocationListener listener, Looper looper) {
        checkListener(listener);
        requestLocationUpdates(request, listener, looper, (PendingIntent) null);
    }

    @SystemApi
    public void requestLocationUpdates(LocationRequest request, PendingIntent intent) {
        checkPendingIntent(intent);
        requestLocationUpdates(request, (LocationListener) null, (Looper) null, intent);
    }

    public synchronized boolean injectLocation(Location newLocation) {
        try {
            return this.mService.injectLocation(newLocation);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private synchronized ListenerTransport wrapListener(LocationListener listener, Looper looper) {
        ListenerTransport transport;
        if (listener == null) {
            return null;
        }
        synchronized (this.mListeners) {
            transport = this.mListeners.get(listener);
            if (transport == null) {
                transport = new ListenerTransport(listener, looper);
            }
            this.mListeners.put(listener, transport);
        }
        return transport;
    }

    public protected void requestLocationUpdates(LocationRequest request, LocationListener listener, Looper looper, PendingIntent intent) {
        String packageName = this.mContext.getPackageName();
        ListenerTransport transport = wrapListener(listener, looper);
        try {
            this.mService.requestLocationUpdates(request, transport, intent, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removeUpdates(LocationListener listener) {
        ListenerTransport transport;
        checkListener(listener);
        String packageName = this.mContext.getPackageName();
        synchronized (this.mListeners) {
            transport = this.mListeners.remove(listener);
        }
        if (transport == null) {
            return;
        }
        try {
            this.mService.removeUpdates(transport, null, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removeUpdates(PendingIntent intent) {
        checkPendingIntent(intent);
        String packageName = this.mContext.getPackageName();
        try {
            this.mService.removeUpdates(null, intent, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addProximityAlert(double latitude, double longitude, float radius, long expiration, PendingIntent intent) {
        checkPendingIntent(intent);
        if (expiration < 0) {
            expiration = Long.MAX_VALUE;
        }
        Geofence fence = Geofence.createCircle(latitude, longitude, radius);
        LocationRequest request = new LocationRequest().setExpireIn(expiration);
        try {
            this.mService.requestGeofence(request, fence, intent, this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void addGeofence(LocationRequest request, Geofence fence, PendingIntent intent) {
        checkPendingIntent(intent);
        checkGeofence(fence);
        try {
            this.mService.requestGeofence(request, fence, intent, this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removeProximityAlert(PendingIntent intent) {
        checkPendingIntent(intent);
        String packageName = this.mContext.getPackageName();
        try {
            this.mService.removeGeofence(null, intent, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void removeGeofence(Geofence fence, PendingIntent intent) {
        checkPendingIntent(intent);
        checkGeofence(fence);
        String packageName = this.mContext.getPackageName();
        try {
            this.mService.removeGeofence(fence, intent, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void removeAllGeofences(PendingIntent intent) {
        checkPendingIntent(intent);
        String packageName = this.mContext.getPackageName();
        try {
            this.mService.removeGeofence(null, intent, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isLocationEnabled() {
        return isLocationEnabledForUser(Process.myUserHandle());
    }

    @SystemApi
    public void setLocationEnabledForUser(boolean enabled, UserHandle userHandle) {
        try {
            this.mService.setLocationEnabledForUser(enabled, userHandle.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean isLocationEnabledForUser(UserHandle userHandle) {
        try {
            return this.mService.isLocationEnabledForUser(userHandle.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isProviderEnabled(String provider) {
        return isProviderEnabledForUser(provider, Process.myUserHandle());
    }

    @SystemApi
    public boolean isProviderEnabledForUser(String provider, UserHandle userHandle) {
        checkProvider(provider);
        try {
            return this.mService.isProviderEnabledForUser(provider, userHandle.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean setProviderEnabledForUser(String provider, boolean enabled, UserHandle userHandle) {
        checkProvider(provider);
        try {
            return this.mService.setProviderEnabledForUser(provider, enabled, userHandle.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized Location getLastLocation() {
        String packageName = this.mContext.getPackageName();
        try {
            return this.mService.getLastLocation(null, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Location getLastKnownLocation(String provider) {
        checkProvider(provider);
        String packageName = this.mContext.getPackageName();
        LocationRequest request = LocationRequest.createFromDeprecatedProvider(provider, 0L, 0.0f, true);
        try {
            return this.mService.getLastLocation(request, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addTestProvider(String name, boolean requiresNetwork, boolean requiresSatellite, boolean requiresCell, boolean hasMonetaryCost, boolean supportsAltitude, boolean supportsSpeed, boolean supportsBearing, int powerRequirement, int accuracy) {
        ProviderProperties properties = new ProviderProperties(requiresNetwork, requiresSatellite, requiresCell, hasMonetaryCost, supportsAltitude, supportsSpeed, supportsBearing, powerRequirement, accuracy);
        if (name.matches(LocationProvider.BAD_CHARS_REGEX)) {
            throw new IllegalArgumentException("provider name contains illegal character: " + name);
        }
        try {
            this.mService.addTestProvider(name, properties, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removeTestProvider(String provider) {
        try {
            this.mService.removeTestProvider(provider, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTestProviderLocation(String provider, Location loc) {
        if (!loc.isComplete()) {
            IllegalArgumentException e = new IllegalArgumentException("Incomplete location object, missing timestamp or accuracy? " + loc);
            if (this.mContext.getApplicationInfo().targetSdkVersion <= 16) {
                Log.w(TAG, e);
                loc.makeComplete();
            } else {
                throw e;
            }
        }
        try {
            this.mService.setTestProviderLocation(provider, loc, this.mContext.getOpPackageName());
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        }
    }

    public void clearTestProviderLocation(String provider) {
        try {
            this.mService.clearTestProviderLocation(provider, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTestProviderEnabled(String provider, boolean enabled) {
        try {
            this.mService.setTestProviderEnabled(provider, enabled, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearTestProviderEnabled(String provider) {
        try {
            this.mService.clearTestProviderEnabled(provider, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTestProviderStatus(String provider, int status, Bundle extras, long updateTime) {
        try {
            this.mService.setTestProviderStatus(provider, status, extras, updateTime, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearTestProviderStatus(String provider) {
        try {
            this.mService.clearTestProviderStatus(provider, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class GnssStatusListenerTransport extends IGnssStatusListener.Stub {
        private static final int NMEA_RECEIVED = 1000;
        private final GnssStatus.Callback mGnssCallback;
        private final Handler mGnssHandler;
        private final OnNmeaMessageListener mGnssNmeaListener;
        private final GpsStatus.Listener mGpsListener;
        private final GpsStatus.NmeaListener mGpsNmeaListener;
        private final ArrayList<Nmea> mNmeaBuffer;

        /* loaded from: classes.dex */
        private class GnssHandler extends Handler {
            public GnssHandler(Handler handler) {
                super(handler != null ? handler.getLooper() : Looper.myLooper());
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                int i = msg.what;
                if (i != 1000) {
                    switch (i) {
                        case 1:
                            GnssStatusListenerTransport.this.mGnssCallback.onStarted();
                            return;
                        case 2:
                            GnssStatusListenerTransport.this.mGnssCallback.onStopped();
                            return;
                        case 3:
                            GnssStatusListenerTransport.this.mGnssCallback.onFirstFix(LocationManager.this.mTimeToFirstFix);
                            return;
                        case 4:
                            GnssStatusListenerTransport.this.mGnssCallback.onSatelliteStatusChanged(LocationManager.this.mGnssStatus);
                            return;
                        default:
                            return;
                    }
                }
                synchronized (GnssStatusListenerTransport.this.mNmeaBuffer) {
                    int length = GnssStatusListenerTransport.this.mNmeaBuffer.size();
                    for (int i2 = 0; i2 < length; i2++) {
                        Nmea nmea = (Nmea) GnssStatusListenerTransport.this.mNmeaBuffer.get(i2);
                        GnssStatusListenerTransport.this.mGnssNmeaListener.onNmeaMessage(nmea.mNmea, nmea.mTimestamp);
                    }
                    GnssStatusListenerTransport.this.mNmeaBuffer.clear();
                }
            }
        }

        /* loaded from: classes.dex */
        private class Nmea {
            String mNmea;
            long mTimestamp;

            Nmea(long timestamp, String nmea) {
                this.mTimestamp = timestamp;
                this.mNmea = nmea;
            }
        }

        GnssStatusListenerTransport(LocationManager locationManager, GpsStatus.Listener listener) {
            this(listener, (Handler) null);
        }

        GnssStatusListenerTransport(GpsStatus.Listener listener, Handler handler) {
            this.mGpsListener = listener;
            this.mGnssHandler = new GnssHandler(handler);
            this.mGpsNmeaListener = null;
            this.mNmeaBuffer = null;
            this.mGnssCallback = this.mGpsListener != null ? new GnssStatus.Callback() { // from class: android.location.LocationManager.GnssStatusListenerTransport.1
                @Override // android.location.GnssStatus.Callback
                public void onStarted() {
                    GnssStatusListenerTransport.this.mGpsListener.onGpsStatusChanged(1);
                }

                @Override // android.location.GnssStatus.Callback
                public void onStopped() {
                    GnssStatusListenerTransport.this.mGpsListener.onGpsStatusChanged(2);
                }

                @Override // android.location.GnssStatus.Callback
                public void onFirstFix(int ttff) {
                    GnssStatusListenerTransport.this.mGpsListener.onGpsStatusChanged(3);
                }

                @Override // android.location.GnssStatus.Callback
                public void onSatelliteStatusChanged(GnssStatus status) {
                    GnssStatusListenerTransport.this.mGpsListener.onGpsStatusChanged(4);
                }
            } : null;
            this.mGnssNmeaListener = null;
        }

        GnssStatusListenerTransport(LocationManager locationManager, GpsStatus.NmeaListener listener) {
            this(listener, (Handler) null);
        }

        GnssStatusListenerTransport(GpsStatus.NmeaListener listener, Handler handler) {
            this.mGpsListener = null;
            this.mGnssHandler = new GnssHandler(handler);
            this.mGpsNmeaListener = listener;
            this.mNmeaBuffer = new ArrayList<>();
            this.mGnssCallback = null;
            this.mGnssNmeaListener = this.mGpsNmeaListener != null ? new OnNmeaMessageListener() { // from class: android.location.LocationManager.GnssStatusListenerTransport.2
                @Override // android.location.OnNmeaMessageListener
                public void onNmeaMessage(String nmea, long timestamp) {
                    GnssStatusListenerTransport.this.mGpsNmeaListener.onNmeaReceived(timestamp, nmea);
                }
            } : null;
        }

        GnssStatusListenerTransport(LocationManager locationManager, GnssStatus.Callback callback) {
            this(callback, (Handler) null);
        }

        GnssStatusListenerTransport(GnssStatus.Callback callback, Handler handler) {
            this.mGnssCallback = callback;
            this.mGnssHandler = new GnssHandler(handler);
            this.mGnssNmeaListener = null;
            this.mNmeaBuffer = null;
            this.mGpsListener = null;
            this.mGpsNmeaListener = null;
        }

        GnssStatusListenerTransport(LocationManager locationManager, OnNmeaMessageListener listener) {
            this(listener, (Handler) null);
        }

        GnssStatusListenerTransport(OnNmeaMessageListener listener, Handler handler) {
            this.mGnssCallback = null;
            this.mGnssHandler = new GnssHandler(handler);
            this.mGnssNmeaListener = listener;
            this.mGpsListener = null;
            this.mGpsNmeaListener = null;
            this.mNmeaBuffer = new ArrayList<>();
        }

        @Override // android.location.IGnssStatusListener
        public synchronized void onGnssStarted() {
            if (this.mGnssCallback != null) {
                Message msg = Message.obtain();
                msg.what = 1;
                this.mGnssHandler.sendMessage(msg);
            }
        }

        @Override // android.location.IGnssStatusListener
        public synchronized void onGnssStopped() {
            if (this.mGnssCallback != null) {
                Message msg = Message.obtain();
                msg.what = 2;
                this.mGnssHandler.sendMessage(msg);
            }
        }

        @Override // android.location.IGnssStatusListener
        public synchronized void onFirstFix(int ttff) {
            if (this.mGnssCallback != null) {
                LocationManager.this.mTimeToFirstFix = ttff;
                Message msg = Message.obtain();
                msg.what = 3;
                this.mGnssHandler.sendMessage(msg);
            }
        }

        @Override // android.location.IGnssStatusListener
        public synchronized void onSvStatusChanged(int svCount, int[] prnWithFlags, float[] cn0s, float[] elevations, float[] azimuths, float[] carrierFreqs) {
            if (this.mGnssCallback != null) {
                LocationManager.this.mGnssStatus = new GnssStatus(svCount, prnWithFlags, cn0s, elevations, azimuths, carrierFreqs);
                Message msg = Message.obtain();
                msg.what = 4;
                this.mGnssHandler.removeMessages(4);
                this.mGnssHandler.sendMessage(msg);
            }
        }

        @Override // android.location.IGnssStatusListener
        public synchronized void onNmeaReceived(long timestamp, String nmea) {
            if (this.mGnssNmeaListener != null) {
                synchronized (this.mNmeaBuffer) {
                    this.mNmeaBuffer.add(new Nmea(timestamp, nmea));
                }
                Message msg = Message.obtain();
                msg.what = 1000;
                this.mGnssHandler.removeMessages(1000);
                this.mGnssHandler.sendMessage(msg);
            }
        }
    }

    @Deprecated
    public boolean addGpsStatusListener(GpsStatus.Listener listener) {
        if (this.mGpsStatusListeners.get(listener) != null) {
            return true;
        }
        try {
            GnssStatusListenerTransport transport = new GnssStatusListenerTransport(this, listener);
            boolean result = this.mService.registerGnssStatusCallback(transport, this.mContext.getPackageName());
            if (result) {
                this.mGpsStatusListeners.put(listener, transport);
            }
            return result;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void removeGpsStatusListener(GpsStatus.Listener listener) {
        try {
            GnssStatusListenerTransport transport = this.mGpsStatusListeners.remove(listener);
            if (transport != null) {
                this.mService.unregisterGnssStatusCallback(transport);
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean registerGnssStatusCallback(GnssStatus.Callback callback) {
        return registerGnssStatusCallback(callback, null);
    }

    public boolean registerGnssStatusCallback(GnssStatus.Callback callback, Handler handler) {
        if (this.mGnssStatusListeners.get(callback) != null) {
            return true;
        }
        try {
            GnssStatusListenerTransport transport = new GnssStatusListenerTransport(callback, handler);
            boolean result = this.mService.registerGnssStatusCallback(transport, this.mContext.getPackageName());
            if (result) {
                this.mGnssStatusListeners.put(callback, transport);
            }
            return result;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterGnssStatusCallback(GnssStatus.Callback callback) {
        try {
            GnssStatusListenerTransport transport = this.mGnssStatusListeners.remove(callback);
            if (transport != null) {
                this.mService.unregisterGnssStatusCallback(transport);
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean addNmeaListener(GpsStatus.NmeaListener listener) {
        if (this.mGpsNmeaListeners.get(listener) != null) {
            return true;
        }
        try {
            GnssStatusListenerTransport transport = new GnssStatusListenerTransport(this, listener);
            boolean result = this.mService.registerGnssStatusCallback(transport, this.mContext.getPackageName());
            if (result) {
                this.mGpsNmeaListeners.put(listener, transport);
            }
            return result;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void removeNmeaListener(GpsStatus.NmeaListener listener) {
        try {
            GnssStatusListenerTransport transport = this.mGpsNmeaListeners.remove(listener);
            if (transport != null) {
                this.mService.unregisterGnssStatusCallback(transport);
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean addNmeaListener(OnNmeaMessageListener listener) {
        return addNmeaListener(listener, null);
    }

    public boolean addNmeaListener(OnNmeaMessageListener listener, Handler handler) {
        if (this.mGpsNmeaListeners.get(listener) != null) {
            return true;
        }
        try {
            GnssStatusListenerTransport transport = new GnssStatusListenerTransport(listener, handler);
            boolean result = this.mService.registerGnssStatusCallback(transport, this.mContext.getPackageName());
            if (result) {
                this.mGnssNmeaListeners.put(listener, transport);
            }
            return result;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removeNmeaListener(OnNmeaMessageListener listener) {
        try {
            GnssStatusListenerTransport transport = this.mGnssNmeaListeners.remove(listener);
            if (transport != null) {
                this.mService.unregisterGnssStatusCallback(transport);
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    @SuppressLint({"Doclava125"})
    @Deprecated
    public boolean addGpsMeasurementListener(GpsMeasurementsEvent.Listener listener) {
        return false;
    }

    public boolean registerGnssMeasurementsCallback(GnssMeasurementsEvent.Callback callback) {
        return registerGnssMeasurementsCallback(callback, null);
    }

    public boolean registerGnssMeasurementsCallback(GnssMeasurementsEvent.Callback callback, Handler handler) {
        return this.mGnssMeasurementCallbackTransport.add(callback, handler);
    }

    @SystemApi
    @SuppressLint({"Doclava125"})
    @Deprecated
    public void removeGpsMeasurementListener(GpsMeasurementsEvent.Listener listener) {
    }

    public void unregisterGnssMeasurementsCallback(GnssMeasurementsEvent.Callback callback) {
        this.mGnssMeasurementCallbackTransport.remove(callback);
    }

    @SystemApi
    @SuppressLint({"Doclava125"})
    @Deprecated
    public boolean addGpsNavigationMessageListener(GpsNavigationMessageEvent.Listener listener) {
        return false;
    }

    @SystemApi
    @SuppressLint({"Doclava125"})
    @Deprecated
    public void removeGpsNavigationMessageListener(GpsNavigationMessageEvent.Listener listener) {
    }

    public boolean registerGnssNavigationMessageCallback(GnssNavigationMessage.Callback callback) {
        return registerGnssNavigationMessageCallback(callback, null);
    }

    public boolean registerGnssNavigationMessageCallback(GnssNavigationMessage.Callback callback, Handler handler) {
        return this.mGnssNavigationMessageCallbackTransport.add(callback, handler);
    }

    public void unregisterGnssNavigationMessageCallback(GnssNavigationMessage.Callback callback) {
        this.mGnssNavigationMessageCallbackTransport.remove(callback);
    }

    @Deprecated
    public GpsStatus getGpsStatus(GpsStatus status) {
        if (status == null) {
            status = new GpsStatus();
        }
        if (this.mGnssStatus != null) {
            status.setStatus(this.mGnssStatus, this.mTimeToFirstFix);
        }
        return status;
    }

    public int getGnssYearOfHardware() {
        try {
            return this.mService.getGnssYearOfHardware();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getGnssHardwareModelName() {
        try {
            return this.mService.getGnssHardwareModelName();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public int getGnssBatchSize() {
        try {
            return this.mService.getGnssBatchSize(this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean registerGnssBatchedLocationCallback(long periodNanos, boolean wakeOnFifoFull, BatchedLocationCallback callback, Handler handler) {
        this.mBatchedLocationCallbackTransport.add(callback, handler);
        try {
            return this.mService.startGnssBatch(periodNanos, wakeOnFifoFull, this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void flushGnssBatch() {
        try {
            this.mService.flushGnssBatch(this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean unregisterGnssBatchedLocationCallback(BatchedLocationCallback callback) {
        this.mBatchedLocationCallbackTransport.remove(callback);
        try {
            return this.mService.stopGnssBatch();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean sendExtraCommand(String provider, String command, Bundle extras) {
        try {
            return this.mService.sendExtraCommand(provider, command, extras);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sendNiResponse(int notifId, int userResponse) {
        try {
            return this.mService.sendNiResponse(notifId, userResponse);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static synchronized void checkProvider(String provider) {
        if (provider == null) {
            throw new IllegalArgumentException("invalid provider: " + provider);
        }
    }

    private static synchronized void checkCriteria(Criteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("invalid criteria: " + criteria);
        }
    }

    private static synchronized void checkListener(LocationListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("invalid listener: " + listener);
        }
    }

    private synchronized void checkPendingIntent(PendingIntent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("invalid pending intent: " + intent);
        } else if (!intent.isTargetedToPackage()) {
            IllegalArgumentException e = new IllegalArgumentException("pending intent must be targeted to package");
            if (this.mContext.getApplicationInfo().targetSdkVersion > 16) {
                throw e;
            }
            Log.w(TAG, e);
        }
    }

    private static synchronized void checkGeofence(Geofence fence) {
        if (fence == null) {
            throw new IllegalArgumentException("invalid geofence: " + fence);
        }
    }
}
