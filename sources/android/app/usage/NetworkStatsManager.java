package android.app.usage;

import android.app.usage.NetworkStats;
import android.content.Context;
import android.net.DataUsageRequest;
import android.net.INetworkStatsService;
import android.net.NetworkIdentity;
import android.net.NetworkTemplate;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.DataUnit;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
/* loaded from: classes.dex */
public class NetworkStatsManager {
    public static final int CALLBACK_LIMIT_REACHED = 0;
    public static final int CALLBACK_RELEASED = 1;
    private static final boolean DBG = false;
    public static final int FLAG_AUGMENT_WITH_SUBSCRIPTION_PLAN = 4;
    public static final int FLAG_POLL_FORCE = 2;
    public static final int FLAG_POLL_ON_OPEN = 1;
    public static final long MIN_THRESHOLD_BYTES = DataUnit.MEBIBYTES.toBytes(2);
    private static final String TAG = "NetworkStatsManager";
    private final Context mContext;
    private int mFlags;
    private final INetworkStatsService mService;

    /* JADX INFO: Access modifiers changed from: private */
    public NetworkStatsManager(Context context) throws ServiceManager.ServiceNotFoundException {
        this(context, INetworkStatsService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.NETWORK_STATS_SERVICE)));
    }

    @VisibleForTesting
    public synchronized NetworkStatsManager(Context context, INetworkStatsService service) {
        this.mContext = context;
        this.mService = service;
        setPollOnOpen(true);
    }

    public synchronized void setPollOnOpen(boolean pollOnOpen) {
        if (pollOnOpen) {
            this.mFlags |= 1;
        } else {
            this.mFlags &= -2;
        }
    }

    public void setPollForce(boolean pollForce) {
        if (pollForce) {
            this.mFlags |= 2;
        } else {
            this.mFlags &= -3;
        }
    }

    public synchronized void setAugmentWithSubscriptionPlan(boolean augmentWithSubscriptionPlan) {
        if (augmentWithSubscriptionPlan) {
            this.mFlags |= 4;
        } else {
            this.mFlags &= -5;
        }
    }

    public synchronized NetworkStats.Bucket querySummaryForDevice(NetworkTemplate template, long startTime, long endTime) throws SecurityException, RemoteException {
        NetworkStats stats = new NetworkStats(this.mContext, template, this.mFlags, startTime, endTime, this.mService);
        NetworkStats.Bucket bucket = stats.getDeviceSummaryForNetwork();
        stats.close();
        return bucket;
    }

    public NetworkStats.Bucket querySummaryForDevice(int networkType, String subscriberId, long startTime, long endTime) throws SecurityException, RemoteException {
        try {
            NetworkTemplate template = createTemplate(networkType, subscriberId);
            return querySummaryForDevice(template, startTime, endTime);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public NetworkStats.Bucket querySummaryForUser(int networkType, String subscriberId, long startTime, long endTime) throws SecurityException, RemoteException {
        try {
            NetworkTemplate template = createTemplate(networkType, subscriberId);
            NetworkStats stats = new NetworkStats(this.mContext, template, this.mFlags, startTime, endTime, this.mService);
            stats.startSummaryEnumeration();
            stats.close();
            return stats.getSummaryAggregate();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public NetworkStats querySummary(int networkType, String subscriberId, long startTime, long endTime) throws SecurityException, RemoteException {
        try {
            NetworkTemplate template = createTemplate(networkType, subscriberId);
            NetworkStats result = new NetworkStats(this.mContext, template, this.mFlags, startTime, endTime, this.mService);
            result.startSummaryEnumeration();
            return result;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public NetworkStats queryDetailsForUid(int networkType, String subscriberId, long startTime, long endTime, int uid) throws SecurityException {
        return queryDetailsForUidTagState(networkType, subscriberId, startTime, endTime, uid, 0, -1);
    }

    public NetworkStats queryDetailsForUidTag(int networkType, String subscriberId, long startTime, long endTime, int uid, int tag) throws SecurityException {
        return queryDetailsForUidTagState(networkType, subscriberId, startTime, endTime, uid, tag, -1);
    }

    public NetworkStats queryDetailsForUidTagState(int networkType, String subscriberId, long startTime, long endTime, int uid, int tag, int state) throws SecurityException {
        NetworkTemplate template = createTemplate(networkType, subscriberId);
        try {
            NetworkStats result = new NetworkStats(this.mContext, template, this.mFlags, startTime, endTime, this.mService);
            result.startHistoryEnumeration(uid, tag, state);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "Error while querying stats for uid=" + uid + " tag=" + tag + " state=" + state, e);
            return null;
        }
    }

    public NetworkStats queryDetails(int networkType, String subscriberId, long startTime, long endTime) throws SecurityException, RemoteException {
        try {
            NetworkTemplate template = createTemplate(networkType, subscriberId);
            NetworkStats result = new NetworkStats(this.mContext, template, this.mFlags, startTime, endTime, this.mService);
            result.startUserUidEnumeration();
            return result;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public synchronized void registerUsageCallback(NetworkTemplate template, int networkType, long thresholdBytes, UsageCallback callback, Handler handler) {
        Looper looper;
        Preconditions.checkNotNull(callback, "UsageCallback cannot be null");
        if (handler == null) {
            looper = Looper.myLooper();
        } else {
            looper = handler.getLooper();
        }
        DataUsageRequest request = new DataUsageRequest(0, template, thresholdBytes);
        try {
            CallbackHandler callbackHandler = new CallbackHandler(looper, networkType, template.getSubscriberId(), callback);
            callback.request = this.mService.registerUsageCallback(this.mContext.getOpPackageName(), request, new Messenger(callbackHandler), new Binder());
            if (callback.request == null) {
                Log.e(TAG, "Request from callback is null; should not happen");
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerUsageCallback(int networkType, String subscriberId, long thresholdBytes, UsageCallback callback) {
        registerUsageCallback(networkType, subscriberId, thresholdBytes, callback, (Handler) null);
    }

    public void registerUsageCallback(int networkType, String subscriberId, long thresholdBytes, UsageCallback callback, Handler handler) {
        NetworkTemplate template = createTemplate(networkType, subscriberId);
        registerUsageCallback(template, networkType, thresholdBytes, callback, handler);
    }

    public void unregisterUsageCallback(UsageCallback callback) {
        if (callback == null || callback.request == null || callback.request.requestId == 0) {
            throw new IllegalArgumentException("Invalid UsageCallback");
        }
        try {
            this.mService.unregisterUsageRequest(callback.request);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int[] getTrafficStatsInfo(String packageName) {
        try {
            return this.mService.getTrafficStatsInfo(packageName);
        } catch (Exception e) {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class UsageCallback {
        private DataUsageRequest request;

        public abstract void onThresholdReached(int i, String str);
    }

    private static synchronized NetworkTemplate createTemplate(int networkType, String subscriberId) {
        if (networkType != 9) {
            switch (networkType) {
                case 0:
                    if (subscriberId == null) {
                        NetworkTemplate template = NetworkTemplate.buildTemplateMobileWildcard();
                        return template;
                    }
                    NetworkTemplate template2 = NetworkTemplate.buildTemplateMobileAll(subscriberId);
                    return template2;
                case 1:
                    NetworkTemplate template3 = NetworkTemplate.buildTemplateWifiWildcard();
                    return template3;
                default:
                    throw new IllegalArgumentException("Cannot create template for network type " + networkType + ", subscriberId '" + NetworkIdentity.scrubSubscriberId(subscriberId) + "'.");
            }
        }
        NetworkTemplate template4 = new NetworkTemplate(5, null, null, null, -1, -1, -1);
        return template4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CallbackHandler extends Handler {
        private UsageCallback mCallback;
        private final int mNetworkType;
        private final String mSubscriberId;

        synchronized CallbackHandler(Looper looper, int networkType, String subscriberId, UsageCallback callback) {
            super(looper);
            this.mNetworkType = networkType;
            this.mSubscriberId = subscriberId;
            this.mCallback = callback;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            DataUsageRequest request = (DataUsageRequest) getObject(message, DataUsageRequest.PARCELABLE_KEY);
            switch (message.what) {
                case 0:
                    if (this.mCallback != null) {
                        this.mCallback.onThresholdReached(this.mNetworkType, this.mSubscriberId);
                        return;
                    }
                    Log.e(NetworkStatsManager.TAG, "limit reached with released callback for " + request);
                    return;
                case 1:
                    this.mCallback = null;
                    return;
                default:
                    return;
            }
        }

        private static synchronized Object getObject(Message msg, String key) {
            return msg.getData().getParcelable(key);
        }
    }
}
