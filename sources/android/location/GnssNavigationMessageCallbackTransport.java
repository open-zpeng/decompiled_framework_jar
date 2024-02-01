package android.location;

import android.content.Context;
import android.location.GnssNavigationMessage;
import android.location.IGnssNavigationMessageListener;
import android.location.LocalListenerHelper;
import android.os.RemoteException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class GnssNavigationMessageCallbackTransport extends LocalListenerHelper<GnssNavigationMessage.Callback> {
    private final IGnssNavigationMessageListener mListenerTransport;
    private final ILocationManager mLocationManager;

    public GnssNavigationMessageCallbackTransport(Context context, ILocationManager locationManager) {
        super(context, "GnssNavigationMessageCallbackTransport");
        this.mListenerTransport = new ListenerTransport();
        this.mLocationManager = locationManager;
    }

    @Override // android.location.LocalListenerHelper
    protected boolean registerWithServer() throws RemoteException {
        return this.mLocationManager.addGnssNavigationMessageListener(this.mListenerTransport, getContext().getPackageName());
    }

    @Override // android.location.LocalListenerHelper
    protected void unregisterFromServer() throws RemoteException {
        this.mLocationManager.removeGnssNavigationMessageListener(this.mListenerTransport);
    }

    /* loaded from: classes.dex */
    private class ListenerTransport extends IGnssNavigationMessageListener.Stub {
        private ListenerTransport() {
        }

        @Override // android.location.IGnssNavigationMessageListener
        public void onGnssNavigationMessageReceived(final GnssNavigationMessage event) {
            LocalListenerHelper.ListenerOperation<GnssNavigationMessage.Callback> operation = new LocalListenerHelper.ListenerOperation<GnssNavigationMessage.Callback>() { // from class: android.location.GnssNavigationMessageCallbackTransport.ListenerTransport.1
                @Override // android.location.LocalListenerHelper.ListenerOperation
                public void execute(GnssNavigationMessage.Callback callback) throws RemoteException {
                    callback.onGnssNavigationMessageReceived(event);
                }
            };
            GnssNavigationMessageCallbackTransport.this.foreach(operation);
        }

        @Override // android.location.IGnssNavigationMessageListener
        public void onStatusChanged(final int status) {
            LocalListenerHelper.ListenerOperation<GnssNavigationMessage.Callback> operation = new LocalListenerHelper.ListenerOperation<GnssNavigationMessage.Callback>() { // from class: android.location.GnssNavigationMessageCallbackTransport.ListenerTransport.2
                @Override // android.location.LocalListenerHelper.ListenerOperation
                public void execute(GnssNavigationMessage.Callback callback) throws RemoteException {
                    callback.onStatusChanged(status);
                }
            };
            GnssNavigationMessageCallbackTransport.this.foreach(operation);
        }
    }
}
