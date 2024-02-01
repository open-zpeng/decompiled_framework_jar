package android.location;

import android.content.Context;
import android.location.GnssMeasurementsEvent;
import android.location.IGnssMeasurementsListener;
import android.location.LocalListenerHelper;
import android.os.RemoteException;
import com.android.internal.util.Preconditions;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class GnssMeasurementCallbackTransport extends LocalListenerHelper<GnssMeasurementsEvent.Callback> {
    private static final String TAG = "GnssMeasCbTransport";
    private final IGnssMeasurementsListener mListenerTransport;
    private final ILocationManager mLocationManager;

    public GnssMeasurementCallbackTransport(Context context, ILocationManager locationManager) {
        super(context, TAG);
        this.mListenerTransport = new ListenerTransport();
        this.mLocationManager = locationManager;
    }

    @Override // android.location.LocalListenerHelper
    protected boolean registerWithServer() throws RemoteException {
        return this.mLocationManager.addGnssMeasurementsListener(this.mListenerTransport, getContext().getPackageName());
    }

    @Override // android.location.LocalListenerHelper
    protected void unregisterFromServer() throws RemoteException {
        this.mLocationManager.removeGnssMeasurementsListener(this.mListenerTransport);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void injectGnssMeasurementCorrections(GnssMeasurementCorrections measurementCorrections) throws RemoteException {
        Preconditions.checkNotNull(measurementCorrections);
        this.mLocationManager.injectGnssMeasurementCorrections(measurementCorrections, getContext().getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getGnssCapabilities() throws RemoteException {
        return this.mLocationManager.getGnssCapabilities(getContext().getPackageName());
    }

    /* loaded from: classes.dex */
    private class ListenerTransport extends IGnssMeasurementsListener.Stub {
        private ListenerTransport() {
        }

        @Override // android.location.IGnssMeasurementsListener
        public void onGnssMeasurementsReceived(final GnssMeasurementsEvent event) {
            LocalListenerHelper.ListenerOperation<GnssMeasurementsEvent.Callback> operation = new LocalListenerHelper.ListenerOperation<GnssMeasurementsEvent.Callback>() { // from class: android.location.GnssMeasurementCallbackTransport.ListenerTransport.1
                @Override // android.location.LocalListenerHelper.ListenerOperation
                public void execute(GnssMeasurementsEvent.Callback callback) throws RemoteException {
                    callback.onGnssMeasurementsReceived(event);
                }
            };
            GnssMeasurementCallbackTransport.this.foreach(operation);
        }

        @Override // android.location.IGnssMeasurementsListener
        public void onStatusChanged(final int status) {
            LocalListenerHelper.ListenerOperation<GnssMeasurementsEvent.Callback> operation = new LocalListenerHelper.ListenerOperation<GnssMeasurementsEvent.Callback>() { // from class: android.location.GnssMeasurementCallbackTransport.ListenerTransport.2
                @Override // android.location.LocalListenerHelper.ListenerOperation
                public void execute(GnssMeasurementsEvent.Callback callback) throws RemoteException {
                    callback.onStatusChanged(status);
                }
            };
            GnssMeasurementCallbackTransport.this.foreach(operation);
        }
    }
}
