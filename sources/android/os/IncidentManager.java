package android.os;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.IBinder;
import android.os.IIncidentManager;
import android.util.Slog;
@SystemApi
/* loaded from: classes2.dex */
public class IncidentManager {
    private static final String TAG = "IncidentManager";
    private final Context mContext;
    private IIncidentManager mService;

    public synchronized IncidentManager(Context context) {
        this.mContext = context;
    }

    public void reportIncident(IncidentReportArgs args) {
        reportIncidentInternal(args);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class IncidentdDeathRecipient implements IBinder.DeathRecipient {
        private IncidentdDeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (this) {
                IncidentManager.this.mService = null;
            }
        }
    }

    private synchronized void reportIncidentInternal(IncidentReportArgs args) {
        try {
            IIncidentManager service = getIIncidentManagerLocked();
            if (service == null) {
                Slog.e(TAG, "reportIncident can't find incident binder service");
            } else {
                service.reportIncident(args);
            }
        } catch (RemoteException ex) {
            Slog.e(TAG, "reportIncident failed", ex);
        }
    }

    private synchronized IIncidentManager getIIncidentManagerLocked() throws RemoteException {
        if (this.mService != null) {
            return this.mService;
        }
        synchronized (this) {
            if (this.mService != null) {
                return this.mService;
            }
            this.mService = IIncidentManager.Stub.asInterface(ServiceManager.getService(Context.INCIDENT_SERVICE));
            if (this.mService != null) {
                this.mService.asBinder().linkToDeath(new IncidentdDeathRecipient(), 0);
            }
            return this.mService;
        }
    }
}
