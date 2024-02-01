package android.net.metrics;

import android.net.ConnectivityMetricsEvent;
import android.net.IIpConnectivityMetrics;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.BitUtils;
/* loaded from: classes2.dex */
public class IpConnectivityLog {
    private static final boolean DBG = false;
    public static final String SERVICE_NAME = "connmetrics";
    private static final String TAG = IpConnectivityLog.class.getSimpleName();
    private IIpConnectivityMetrics mService;

    private protected IpConnectivityLog() {
    }

    @VisibleForTesting
    public synchronized IpConnectivityLog(IIpConnectivityMetrics service) {
        this.mService = service;
    }

    private synchronized boolean checkLoggerService() {
        if (this.mService != null) {
            return true;
        }
        IIpConnectivityMetrics service = IIpConnectivityMetrics.Stub.asInterface(ServiceManager.getService(SERVICE_NAME));
        if (service == null) {
            return false;
        }
        this.mService = service;
        return true;
    }

    public synchronized boolean log(ConnectivityMetricsEvent ev) {
        if (checkLoggerService()) {
            if (ev.timestamp == 0) {
                ev.timestamp = System.currentTimeMillis();
            }
            try {
                int left = this.mService.logEvent(ev);
                return left >= 0;
            } catch (RemoteException e) {
                Log.e(TAG, "Error logging event", e);
                return false;
            }
        }
        return false;
    }

    public synchronized boolean log(long timestamp, Parcelable data) {
        ConnectivityMetricsEvent ev = makeEv(data);
        ev.timestamp = timestamp;
        return log(ev);
    }

    private protected boolean log(String ifname, Parcelable data) {
        ConnectivityMetricsEvent ev = makeEv(data);
        ev.ifname = ifname;
        return log(ev);
    }

    public synchronized boolean log(int netid, int[] transports, Parcelable data) {
        ConnectivityMetricsEvent ev = makeEv(data);
        ev.netId = netid;
        ev.transports = BitUtils.packBits(transports);
        return log(ev);
    }

    private protected boolean log(Parcelable data) {
        return log(makeEv(data));
    }

    private static synchronized ConnectivityMetricsEvent makeEv(Parcelable data) {
        ConnectivityMetricsEvent ev = new ConnectivityMetricsEvent();
        ev.data = data;
        return ev;
    }
}
