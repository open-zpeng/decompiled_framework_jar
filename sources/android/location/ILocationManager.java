package android.location;

import android.app.PendingIntent;
import android.location.IBatchedLocationCallback;
import android.location.IGnssMeasurementsListener;
import android.location.IGnssNavigationMessageListener;
import android.location.IGnssStatusListener;
import android.location.ILocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.location.ProviderProperties;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public interface ILocationManager extends IInterface {
    synchronized boolean addGnssBatchingCallback(IBatchedLocationCallback iBatchedLocationCallback, String str) throws RemoteException;

    synchronized boolean addGnssMeasurementsListener(IGnssMeasurementsListener iGnssMeasurementsListener, String str) throws RemoteException;

    synchronized boolean addGnssNavigationMessageListener(IGnssNavigationMessageListener iGnssNavigationMessageListener, String str) throws RemoteException;

    synchronized void addTestProvider(String str, ProviderProperties providerProperties, String str2) throws RemoteException;

    synchronized void clearTestProviderEnabled(String str, String str2) throws RemoteException;

    synchronized void clearTestProviderLocation(String str, String str2) throws RemoteException;

    synchronized void clearTestProviderStatus(String str, String str2) throws RemoteException;

    synchronized void flushGnssBatch(String str) throws RemoteException;

    synchronized boolean geocoderIsPresent() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    List<String> getAllProviders() throws RemoteException;

    synchronized String[] getBackgroundThrottlingWhitelist() throws RemoteException;

    synchronized String getBestProvider(Criteria criteria, boolean z) throws RemoteException;

    synchronized String getFromLocation(double d, double d2, int i, GeocoderParams geocoderParams, List<Address> list) throws RemoteException;

    synchronized String getFromLocationName(String str, double d, double d2, double d3, double d4, int i, GeocoderParams geocoderParams, List<Address> list) throws RemoteException;

    synchronized int getGnssBatchSize(String str) throws RemoteException;

    synchronized String getGnssHardwareModelName() throws RemoteException;

    synchronized int getGnssYearOfHardware() throws RemoteException;

    synchronized Location getLastLocation(LocationRequest locationRequest, String str) throws RemoteException;

    private protected String getNetworkProviderPackage() throws RemoteException;

    synchronized ProviderProperties getProviderProperties(String str) throws RemoteException;

    synchronized List<String> getProviders(Criteria criteria, boolean z) throws RemoteException;

    synchronized boolean injectLocation(Location location) throws RemoteException;

    synchronized boolean isLocationEnabledForUser(int i) throws RemoteException;

    synchronized boolean isProviderEnabledForUser(String str, int i) throws RemoteException;

    synchronized void locationCallbackFinished(ILocationListener iLocationListener) throws RemoteException;

    synchronized boolean providerMeetsCriteria(String str, Criteria criteria) throws RemoteException;

    synchronized boolean registerGnssStatusCallback(IGnssStatusListener iGnssStatusListener, String str) throws RemoteException;

    synchronized void removeGeofence(Geofence geofence, PendingIntent pendingIntent, String str) throws RemoteException;

    synchronized void removeGnssBatchingCallback() throws RemoteException;

    synchronized void removeGnssMeasurementsListener(IGnssMeasurementsListener iGnssMeasurementsListener) throws RemoteException;

    synchronized void removeGnssNavigationMessageListener(IGnssNavigationMessageListener iGnssNavigationMessageListener) throws RemoteException;

    synchronized void removeTestProvider(String str, String str2) throws RemoteException;

    synchronized void removeUpdates(ILocationListener iLocationListener, PendingIntent pendingIntent, String str) throws RemoteException;

    private protected void reportLocation(Location location, boolean z) throws RemoteException;

    synchronized void reportLocationBatch(List<Location> list) throws RemoteException;

    synchronized void requestGeofence(LocationRequest locationRequest, Geofence geofence, PendingIntent pendingIntent, String str) throws RemoteException;

    synchronized void requestLocationUpdates(LocationRequest locationRequest, ILocationListener iLocationListener, PendingIntent pendingIntent, String str) throws RemoteException;

    synchronized boolean sendExtraCommand(String str, String str2, Bundle bundle) throws RemoteException;

    synchronized boolean sendNiResponse(int i, int i2) throws RemoteException;

    synchronized void setLocationEnabledForUser(boolean z, int i) throws RemoteException;

    synchronized boolean setProviderEnabledForUser(String str, boolean z, int i) throws RemoteException;

    synchronized void setTestProviderEnabled(String str, boolean z, String str2) throws RemoteException;

    synchronized void setTestProviderLocation(String str, Location location, String str2) throws RemoteException;

    synchronized void setTestProviderStatus(String str, int i, Bundle bundle, long j, String str2) throws RemoteException;

    synchronized boolean startGnssBatch(long j, boolean z, String str) throws RemoteException;

    synchronized boolean stopGnssBatch() throws RemoteException;

    synchronized void unregisterGnssStatusCallback(IGnssStatusListener iGnssStatusListener) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ILocationManager {
        private static final String DESCRIPTOR = "android.location.ILocationManager";
        static final int TRANSACTION_addGnssBatchingCallback = 19;
        static final int TRANSACTION_addGnssMeasurementsListener = 12;
        static final int TRANSACTION_addGnssNavigationMessageListener = 14;
        static final int TRANSACTION_addTestProvider = 35;
        static final int TRANSACTION_clearTestProviderEnabled = 40;
        static final int TRANSACTION_clearTestProviderLocation = 38;
        static final int TRANSACTION_clearTestProviderStatus = 42;
        static final int TRANSACTION_flushGnssBatch = 22;
        static final int TRANSACTION_geocoderIsPresent = 8;
        public private protected static final int TRANSACTION_getAllProviders = 25;
        static final int TRANSACTION_getBackgroundThrottlingWhitelist = 47;
        static final int TRANSACTION_getBestProvider = 27;
        static final int TRANSACTION_getFromLocation = 9;
        static final int TRANSACTION_getFromLocationName = 10;
        static final int TRANSACTION_getGnssBatchSize = 18;
        static final int TRANSACTION_getGnssHardwareModelName = 17;
        static final int TRANSACTION_getGnssYearOfHardware = 16;
        static final int TRANSACTION_getLastLocation = 5;
        static final int TRANSACTION_getNetworkProviderPackage = 30;
        static final int TRANSACTION_getProviderProperties = 29;
        static final int TRANSACTION_getProviders = 26;
        static final int TRANSACTION_injectLocation = 24;
        static final int TRANSACTION_isLocationEnabledForUser = 33;
        static final int TRANSACTION_isProviderEnabledForUser = 31;
        static final int TRANSACTION_locationCallbackFinished = 46;
        static final int TRANSACTION_providerMeetsCriteria = 28;
        static final int TRANSACTION_registerGnssStatusCallback = 6;
        static final int TRANSACTION_removeGeofence = 4;
        static final int TRANSACTION_removeGnssBatchingCallback = 20;
        static final int TRANSACTION_removeGnssMeasurementsListener = 13;
        static final int TRANSACTION_removeGnssNavigationMessageListener = 15;
        static final int TRANSACTION_removeTestProvider = 36;
        static final int TRANSACTION_removeUpdates = 2;
        static final int TRANSACTION_reportLocation = 44;
        static final int TRANSACTION_reportLocationBatch = 45;
        static final int TRANSACTION_requestGeofence = 3;
        static final int TRANSACTION_requestLocationUpdates = 1;
        static final int TRANSACTION_sendExtraCommand = 43;
        static final int TRANSACTION_sendNiResponse = 11;
        static final int TRANSACTION_setLocationEnabledForUser = 34;
        static final int TRANSACTION_setProviderEnabledForUser = 32;
        static final int TRANSACTION_setTestProviderEnabled = 39;
        static final int TRANSACTION_setTestProviderLocation = 37;
        static final int TRANSACTION_setTestProviderStatus = 41;
        static final int TRANSACTION_startGnssBatch = 21;
        static final int TRANSACTION_stopGnssBatch = 23;
        static final int TRANSACTION_unregisterGnssStatusCallback = 7;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static ILocationManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ILocationManager)) {
                return (ILocationManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            LocationRequest _arg0;
            LocationRequest _arg02;
            Geofence _arg1;
            Geofence _arg03;
            GeocoderParams _arg3;
            GeocoderParams _arg6;
            boolean _arg12;
            Bundle _arg2;
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg0 = LocationRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        ILocationListener _arg13 = ILocationListener.Stub.asInterface(data.readStrongBinder());
                        PendingIntent _arg22 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                        String _arg32 = data.readString();
                        requestLocationUpdates(_arg0, _arg13, _arg22, _arg32);
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        ILocationListener _arg04 = ILocationListener.Stub.asInterface(data.readStrongBinder());
                        PendingIntent _arg14 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                        String _arg23 = data.readString();
                        removeUpdates(_arg04, _arg14, _arg23);
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg02 = LocationRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = Geofence.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        PendingIntent _arg24 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                        String _arg33 = data.readString();
                        requestGeofence(_arg02, _arg1, _arg24, _arg33);
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg03 = Geofence.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        PendingIntent _arg15 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                        String _arg25 = data.readString();
                        removeGeofence(_arg03, _arg15, _arg25);
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        LocationRequest _arg05 = data.readInt() != 0 ? LocationRequest.CREATOR.createFromParcel(data) : null;
                        Location _result = getLastLocation(_arg05, data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        IGnssStatusListener _arg06 = IGnssStatusListener.Stub.asInterface(data.readStrongBinder());
                        boolean registerGnssStatusCallback = registerGnssStatusCallback(_arg06, data.readString());
                        reply.writeNoException();
                        reply.writeInt(registerGnssStatusCallback ? 1 : 0);
                        return true;
                    case 7:
                        data.enforceInterface(DESCRIPTOR);
                        IGnssStatusListener _arg07 = IGnssStatusListener.Stub.asInterface(data.readStrongBinder());
                        unregisterGnssStatusCallback(_arg07);
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(DESCRIPTOR);
                        boolean geocoderIsPresent = geocoderIsPresent();
                        reply.writeNoException();
                        reply.writeInt(geocoderIsPresent ? 1 : 0);
                        return true;
                    case 9:
                        data.enforceInterface(DESCRIPTOR);
                        double _arg08 = data.readDouble();
                        double _arg16 = data.readDouble();
                        int _arg26 = data.readInt();
                        if (data.readInt() != 0) {
                            GeocoderParams _arg34 = GeocoderParams.CREATOR.createFromParcel(data);
                            _arg3 = _arg34;
                        } else {
                            _arg3 = null;
                        }
                        ArrayList arrayList = new ArrayList();
                        String _result2 = getFromLocation(_arg08, _arg16, _arg26, _arg3, arrayList);
                        reply.writeNoException();
                        reply.writeString(_result2);
                        reply.writeTypedList(arrayList);
                        return true;
                    case 10:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg09 = data.readString();
                        double _arg17 = data.readDouble();
                        double _arg27 = data.readDouble();
                        double _arg35 = data.readDouble();
                        double _arg4 = data.readDouble();
                        int _arg5 = data.readInt();
                        if (data.readInt() != 0) {
                            GeocoderParams _arg62 = GeocoderParams.CREATOR.createFromParcel(data);
                            _arg6 = _arg62;
                        } else {
                            _arg6 = null;
                        }
                        ArrayList arrayList2 = new ArrayList();
                        String _result3 = getFromLocationName(_arg09, _arg17, _arg27, _arg35, _arg4, _arg5, _arg6, arrayList2);
                        reply.writeNoException();
                        reply.writeString(_result3);
                        reply.writeTypedList(arrayList2);
                        return true;
                    case 11:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg010 = data.readInt();
                        boolean sendNiResponse = sendNiResponse(_arg010, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(sendNiResponse ? 1 : 0);
                        return true;
                    case 12:
                        data.enforceInterface(DESCRIPTOR);
                        IGnssMeasurementsListener _arg011 = IGnssMeasurementsListener.Stub.asInterface(data.readStrongBinder());
                        boolean addGnssMeasurementsListener = addGnssMeasurementsListener(_arg011, data.readString());
                        reply.writeNoException();
                        reply.writeInt(addGnssMeasurementsListener ? 1 : 0);
                        return true;
                    case 13:
                        data.enforceInterface(DESCRIPTOR);
                        IGnssMeasurementsListener _arg012 = IGnssMeasurementsListener.Stub.asInterface(data.readStrongBinder());
                        removeGnssMeasurementsListener(_arg012);
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(DESCRIPTOR);
                        IGnssNavigationMessageListener _arg013 = IGnssNavigationMessageListener.Stub.asInterface(data.readStrongBinder());
                        boolean addGnssNavigationMessageListener = addGnssNavigationMessageListener(_arg013, data.readString());
                        reply.writeNoException();
                        reply.writeInt(addGnssNavigationMessageListener ? 1 : 0);
                        return true;
                    case 15:
                        data.enforceInterface(DESCRIPTOR);
                        IGnssNavigationMessageListener _arg014 = IGnssNavigationMessageListener.Stub.asInterface(data.readStrongBinder());
                        removeGnssNavigationMessageListener(_arg014);
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(DESCRIPTOR);
                        int _result4 = getGnssYearOfHardware();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 17:
                        data.enforceInterface(DESCRIPTOR);
                        String _result5 = getGnssHardwareModelName();
                        reply.writeNoException();
                        reply.writeString(_result5);
                        return true;
                    case 18:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg015 = data.readString();
                        int _result6 = getGnssBatchSize(_arg015);
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 19:
                        data.enforceInterface(DESCRIPTOR);
                        IBatchedLocationCallback _arg016 = IBatchedLocationCallback.Stub.asInterface(data.readStrongBinder());
                        boolean addGnssBatchingCallback = addGnssBatchingCallback(_arg016, data.readString());
                        reply.writeNoException();
                        reply.writeInt(addGnssBatchingCallback ? 1 : 0);
                        return true;
                    case 20:
                        data.enforceInterface(DESCRIPTOR);
                        removeGnssBatchingCallback();
                        reply.writeNoException();
                        return true;
                    case 21:
                        data.enforceInterface(DESCRIPTOR);
                        long _arg017 = data.readLong();
                        _arg12 = data.readInt() != 0;
                        String _arg28 = data.readString();
                        boolean startGnssBatch = startGnssBatch(_arg017, _arg12, _arg28);
                        reply.writeNoException();
                        reply.writeInt(startGnssBatch ? 1 : 0);
                        return true;
                    case 22:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg018 = data.readString();
                        flushGnssBatch(_arg018);
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(DESCRIPTOR);
                        boolean stopGnssBatch = stopGnssBatch();
                        reply.writeNoException();
                        reply.writeInt(stopGnssBatch ? 1 : 0);
                        return true;
                    case 24:
                        data.enforceInterface(DESCRIPTOR);
                        Location _arg019 = data.readInt() != 0 ? Location.CREATOR.createFromParcel(data) : null;
                        boolean injectLocation = injectLocation(_arg019);
                        reply.writeNoException();
                        reply.writeInt(injectLocation ? 1 : 0);
                        return true;
                    case 25:
                        data.enforceInterface(DESCRIPTOR);
                        List<String> _result7 = getAllProviders();
                        reply.writeNoException();
                        reply.writeStringList(_result7);
                        return true;
                    case 26:
                        data.enforceInterface(DESCRIPTOR);
                        Criteria _arg020 = data.readInt() != 0 ? Criteria.CREATOR.createFromParcel(data) : null;
                        _arg12 = data.readInt() != 0;
                        List<String> _result8 = getProviders(_arg020, _arg12);
                        reply.writeNoException();
                        reply.writeStringList(_result8);
                        return true;
                    case 27:
                        data.enforceInterface(DESCRIPTOR);
                        Criteria _arg021 = data.readInt() != 0 ? Criteria.CREATOR.createFromParcel(data) : null;
                        _arg12 = data.readInt() != 0;
                        String _result9 = getBestProvider(_arg021, _arg12);
                        reply.writeNoException();
                        reply.writeString(_result9);
                        return true;
                    case 28:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg022 = data.readString();
                        boolean providerMeetsCriteria = providerMeetsCriteria(_arg022, data.readInt() != 0 ? Criteria.CREATOR.createFromParcel(data) : null);
                        reply.writeNoException();
                        reply.writeInt(providerMeetsCriteria ? 1 : 0);
                        return true;
                    case 29:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg023 = data.readString();
                        ProviderProperties _result10 = getProviderProperties(_arg023);
                        reply.writeNoException();
                        if (_result10 != null) {
                            reply.writeInt(1);
                            _result10.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 30:
                        data.enforceInterface(DESCRIPTOR);
                        String _result11 = getNetworkProviderPackage();
                        reply.writeNoException();
                        reply.writeString(_result11);
                        return true;
                    case 31:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg024 = data.readString();
                        boolean isProviderEnabledForUser = isProviderEnabledForUser(_arg024, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(isProviderEnabledForUser ? 1 : 0);
                        return true;
                    case 32:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg025 = data.readString();
                        _arg12 = data.readInt() != 0;
                        int _arg29 = data.readInt();
                        boolean providerEnabledForUser = setProviderEnabledForUser(_arg025, _arg12, _arg29);
                        reply.writeNoException();
                        reply.writeInt(providerEnabledForUser ? 1 : 0);
                        return true;
                    case 33:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg026 = data.readInt();
                        boolean isLocationEnabledForUser = isLocationEnabledForUser(_arg026);
                        reply.writeNoException();
                        reply.writeInt(isLocationEnabledForUser ? 1 : 0);
                        return true;
                    case 34:
                        data.enforceInterface(DESCRIPTOR);
                        _arg12 = data.readInt() != 0;
                        setLocationEnabledForUser(_arg12, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 35:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg027 = data.readString();
                        ProviderProperties _arg18 = data.readInt() != 0 ? ProviderProperties.CREATOR.createFromParcel(data) : null;
                        String _arg210 = data.readString();
                        addTestProvider(_arg027, _arg18, _arg210);
                        reply.writeNoException();
                        return true;
                    case 36:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg028 = data.readString();
                        removeTestProvider(_arg028, data.readString());
                        reply.writeNoException();
                        return true;
                    case 37:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg029 = data.readString();
                        Location _arg19 = data.readInt() != 0 ? Location.CREATOR.createFromParcel(data) : null;
                        String _arg211 = data.readString();
                        setTestProviderLocation(_arg029, _arg19, _arg211);
                        reply.writeNoException();
                        return true;
                    case 38:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg030 = data.readString();
                        clearTestProviderLocation(_arg030, data.readString());
                        reply.writeNoException();
                        return true;
                    case 39:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg031 = data.readString();
                        _arg12 = data.readInt() != 0;
                        String _arg212 = data.readString();
                        setTestProviderEnabled(_arg031, _arg12, _arg212);
                        reply.writeNoException();
                        return true;
                    case 40:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg032 = data.readString();
                        clearTestProviderEnabled(_arg032, data.readString());
                        reply.writeNoException();
                        return true;
                    case 41:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg033 = data.readString();
                        int _arg110 = data.readInt();
                        if (data.readInt() != 0) {
                            Bundle _arg213 = Bundle.CREATOR.createFromParcel(data);
                            _arg2 = _arg213;
                        } else {
                            _arg2 = null;
                        }
                        long _arg36 = data.readLong();
                        String _arg42 = data.readString();
                        setTestProviderStatus(_arg033, _arg110, _arg2, _arg36, _arg42);
                        reply.writeNoException();
                        return true;
                    case 42:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg034 = data.readString();
                        clearTestProviderStatus(_arg034, data.readString());
                        reply.writeNoException();
                        return true;
                    case 43:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg035 = data.readString();
                        String _arg111 = data.readString();
                        Bundle _arg214 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                        boolean sendExtraCommand = sendExtraCommand(_arg035, _arg111, _arg214);
                        reply.writeNoException();
                        reply.writeInt(sendExtraCommand ? 1 : 0);
                        if (_arg214 != null) {
                            reply.writeInt(1);
                            _arg214.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 44:
                        data.enforceInterface(DESCRIPTOR);
                        Location _arg036 = data.readInt() != 0 ? Location.CREATOR.createFromParcel(data) : null;
                        _arg12 = data.readInt() != 0;
                        reportLocation(_arg036, _arg12);
                        reply.writeNoException();
                        return true;
                    case 45:
                        data.enforceInterface(DESCRIPTOR);
                        List<Location> _arg037 = data.createTypedArrayList(Location.CREATOR);
                        reportLocationBatch(_arg037);
                        reply.writeNoException();
                        return true;
                    case 46:
                        data.enforceInterface(DESCRIPTOR);
                        ILocationListener _arg038 = ILocationListener.Stub.asInterface(data.readStrongBinder());
                        locationCallbackFinished(_arg038);
                        reply.writeNoException();
                        return true;
                    case 47:
                        data.enforceInterface(DESCRIPTOR);
                        String[] _result12 = getBackgroundThrottlingWhitelist();
                        reply.writeNoException();
                        reply.writeStringArray(_result12);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(DESCRIPTOR);
            return true;
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ILocationManager {
            private IBinder mRemote;

            public private protected Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.location.ILocationManager
            public synchronized void requestLocationUpdates(LocationRequest request, ILocationListener listener, PendingIntent intent, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void removeUpdates(ILocationListener listener, PendingIntent intent, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void requestGeofence(LocationRequest request, Geofence geofence, PendingIntent intent, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (geofence != null) {
                        _data.writeInt(1);
                        geofence.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void removeGeofence(Geofence fence, PendingIntent intent, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fence != null) {
                        _data.writeInt(1);
                        fence.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized Location getLastLocation(LocationRequest request, String packageName) throws RemoteException {
                Location _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Location.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean registerGnssStatusCallback(IGnssStatusListener callback, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(packageName);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void unregisterGnssStatusCallback(IGnssStatusListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean geocoderIsPresent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized String getFromLocation(double latitude, double longitude, int maxResults, GeocoderParams params, List<Address> addrs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeDouble(latitude);
                    _data.writeDouble(longitude);
                    _data.writeInt(maxResults);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    _reply.readTypedList(addrs, Address.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized String getFromLocationName(String locationName, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude, int maxResults, GeocoderParams params, List<Address> addrs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(locationName);
                        try {
                            _data.writeDouble(lowerLeftLatitude);
                        } catch (Throwable th) {
                            th = th;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    _data.writeDouble(lowerLeftLongitude);
                    try {
                        _data.writeDouble(upperRightLatitude);
                        try {
                            _data.writeDouble(upperRightLongitude);
                            try {
                                _data.writeInt(maxResults);
                                if (params != null) {
                                    _data.writeInt(1);
                                    params.writeToParcel(_data, 0);
                                } else {
                                    _data.writeInt(0);
                                }
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    try {
                        _reply.readTypedList(addrs, Address.CREATOR);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th8) {
                        th = th8;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th9) {
                    th = th9;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean sendNiResponse(int notifId, int userResponse) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(notifId);
                    _data.writeInt(userResponse);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean addGnssMeasurementsListener(IGnssMeasurementsListener listener, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeString(packageName);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void removeGnssMeasurementsListener(IGnssMeasurementsListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean addGnssNavigationMessageListener(IGnssNavigationMessageListener listener, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeString(packageName);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void removeGnssNavigationMessageListener(IGnssNavigationMessageListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized int getGnssYearOfHardware() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized String getGnssHardwareModelName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized int getGnssBatchSize(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean addGnssBatchingCallback(IBatchedLocationCallback callback, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(packageName);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void removeGnssBatchingCallback() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean startGnssBatch(long periodNanos, boolean wakeOnFifoFull, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(periodNanos);
                    _data.writeInt(wakeOnFifoFull ? 1 : 0);
                    _data.writeString(packageName);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void flushGnssBatch(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean stopGnssBatch() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean injectLocation(Location location) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (location != null) {
                        _data.writeInt(1);
                        location.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized List<String> getAllProviders() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized List<String> getProviders(Criteria criteria, boolean enabledOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (criteria != null) {
                        _data.writeInt(1);
                        criteria.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabledOnly ? 1 : 0);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized String getBestProvider(Criteria criteria, boolean enabledOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (criteria != null) {
                        _data.writeInt(1);
                        criteria.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabledOnly ? 1 : 0);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean providerMeetsCriteria(String provider, Criteria criteria) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    if (criteria != null) {
                        _data.writeInt(1);
                        criteria.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized ProviderProperties getProviderProperties(String provider) throws RemoteException {
                ProviderProperties _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ProviderProperties.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getNetworkProviderPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean isProviderEnabledForUser(String provider, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeInt(userId);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean setProviderEnabledForUser(String provider, boolean enabled, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean isLocationEnabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void setLocationEnabledForUser(boolean enabled, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void addTestProvider(String name, ProviderProperties properties, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    if (properties != null) {
                        _data.writeInt(1);
                        properties.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(opPackageName);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void removeTestProvider(String provider, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void setTestProviderLocation(String provider, Location loc, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    if (loc != null) {
                        _data.writeInt(1);
                        loc.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(opPackageName);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void clearTestProviderLocation(String provider, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void setTestProviderEnabled(String provider, boolean enabled, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void clearTestProviderEnabled(String provider, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void setTestProviderStatus(String provider, int status, Bundle extras, long updateTime, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeInt(status);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(updateTime);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void clearTestProviderStatus(String provider, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized boolean sendExtraCommand(String provider, String command, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeString(command);
                    boolean _result = true;
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    if (_reply.readInt() != 0) {
                        extras.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void reportLocation(Location location, boolean passive) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (location != null) {
                        _data.writeInt(1);
                        location.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(passive ? 1 : 0);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void reportLocationBatch(List<Location> locations) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(locations);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized void locationCallbackFinished(ILocationListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.ILocationManager
            public synchronized String[] getBackgroundThrottlingWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
