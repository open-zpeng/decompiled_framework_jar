package android.location;

import android.content.Context;
import android.location.IBatchedLocationCallback;
import android.location.LocalListenerHelper;
import android.os.RemoteException;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class BatchedLocationCallbackTransport extends LocalListenerHelper<BatchedLocationCallback> {
    private final IBatchedLocationCallback mCallbackTransport;
    private final ILocationManager mLocationManager;

    public synchronized BatchedLocationCallbackTransport(Context context, ILocationManager locationManager) {
        super(context, "BatchedLocationCallbackTransport");
        this.mCallbackTransport = new CallbackTransport();
        this.mLocationManager = locationManager;
    }

    @Override // android.location.LocalListenerHelper
    protected synchronized boolean registerWithServer() throws RemoteException {
        return this.mLocationManager.addGnssBatchingCallback(this.mCallbackTransport, getContext().getPackageName());
    }

    @Override // android.location.LocalListenerHelper
    protected synchronized void unregisterFromServer() throws RemoteException {
        this.mLocationManager.removeGnssBatchingCallback();
    }

    /* loaded from: classes.dex */
    private class CallbackTransport extends IBatchedLocationCallback.Stub {
        private CallbackTransport() {
        }

        @Override // android.location.IBatchedLocationCallback
        public synchronized void onLocationBatch(final List<Location> locations) {
            LocalListenerHelper.ListenerOperation<BatchedLocationCallback> operation = new LocalListenerHelper.ListenerOperation<BatchedLocationCallback>() { // from class: android.location.BatchedLocationCallbackTransport.CallbackTransport.1
                @Override // android.location.LocalListenerHelper.ListenerOperation
                public void execute(BatchedLocationCallback callback) throws RemoteException {
                    callback.onLocationBatch(locations);
                }
            };
            BatchedLocationCallbackTransport.this.foreach(operation);
        }
    }
}
