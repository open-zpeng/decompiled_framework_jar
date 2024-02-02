package android.net.wifi.aware;

import android.util.Log;
/* loaded from: classes2.dex */
public class SubscribeDiscoverySession extends DiscoverySession {
    private static final String TAG = "SubscribeDiscSession";

    public synchronized SubscribeDiscoverySession(WifiAwareManager manager, int clientId, int sessionId) {
        super(manager, clientId, sessionId);
    }

    public void updateSubscribe(SubscribeConfig subscribeConfig) {
        if (this.mTerminated) {
            Log.w(TAG, "updateSubscribe: called on terminated session");
            return;
        }
        WifiAwareManager mgr = this.mMgr.get();
        if (mgr == null) {
            Log.w(TAG, "updateSubscribe: called post GC on WifiAwareManager");
        } else {
            mgr.updateSubscribe(this.mClientId, this.mSessionId, subscribeConfig);
        }
    }
}
