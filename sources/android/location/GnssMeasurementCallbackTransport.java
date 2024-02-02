package android.location;

import android.content.Context;
import android.location.GnssMeasurementsEvent;
import android.location.IGnssMeasurementsListener;
import android.location.LocalListenerHelper;
import android.os.RemoteException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class GnssMeasurementCallbackTransport extends LocalListenerHelper<GnssMeasurementsEvent.Callback> {
    private final IGnssMeasurementsListener mListenerTransport;
    private final ILocationManager mLocationManager;

    public synchronized GnssMeasurementCallbackTransport(Context context, ILocationManager locationManager) {
        super(context, "GnssMeasurementListenerTransport");
        this.mListenerTransport = new ListenerTransport();
        this.mLocationManager = locationManager;
    }

    @Override // android.location.LocalListenerHelper
    protected synchronized boolean registerWithServer() throws RemoteException {
        return this.mLocationManager.addGnssMeasurementsListener(this.mListenerTransport, getContext().getPackageName());
    }

    @Override // android.location.LocalListenerHelper
    protected synchronized void unregisterFromServer() throws RemoteException {
        this.mLocationManager.removeGnssMeasurementsListener(this.mListenerTransport);
    }

    /* loaded from: classes.dex */
    private class ListenerTransport extends IGnssMeasurementsListener.Stub {
        private ListenerTransport() {
        }

        @Override // android.location.IGnssMeasurementsListener
        public synchronized void onGnssMeasurementsReceived(final GnssMeasurementsEvent event) {
            LocalListenerHelper.ListenerOperation<GnssMeasurementsEvent.Callback> operation = new LocalListenerHelper.ListenerOperation<GnssMeasurementsEvent.Callback>() { // from class: android.location.GnssMeasurementCallbackTransport.ListenerTransport.1
                @Override // android.location.LocalListenerHelper.ListenerOperation
                public void execute(GnssMeasurementsEvent.Callback callback) throws RemoteException {
                    callback.onGnssMeasurementsReceived(event);
                }
            };
            GnssMeasurementCallbackTransport.this.foreach(operation);
        }

        @Override // android.location.IGnssMeasurementsListener
        public synchronized void onStatusChanged(final int status) {
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
