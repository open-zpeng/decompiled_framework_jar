package android.app;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.IBinder;
import android.os.IStatsManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.AndroidException;
import android.util.Slog;
@SystemApi
/* loaded from: classes.dex */
public final class StatsManager {
    public static final String ACTION_STATSD_STARTED = "android.app.action.STATSD_STARTED";
    private static final boolean DEBUG = false;
    public static final String EXTRA_STATS_BROADCAST_SUBSCRIBER_COOKIES = "android.app.extra.STATS_BROADCAST_SUBSCRIBER_COOKIES";
    public static final String EXTRA_STATS_CONFIG_KEY = "android.app.extra.STATS_CONFIG_KEY";
    public static final String EXTRA_STATS_CONFIG_UID = "android.app.extra.STATS_CONFIG_UID";
    public static final String EXTRA_STATS_DIMENSIONS_VALUE = "android.app.extra.STATS_DIMENSIONS_VALUE";
    public static final String EXTRA_STATS_SUBSCRIPTION_ID = "android.app.extra.STATS_SUBSCRIPTION_ID";
    public static final String EXTRA_STATS_SUBSCRIPTION_RULE_ID = "android.app.extra.STATS_SUBSCRIPTION_RULE_ID";
    private static final String TAG = "StatsManager";
    private final Context mContext;
    private IStatsManager mService;

    public synchronized StatsManager(Context context) {
        this.mContext = context;
    }

    public void addConfig(long configKey, byte[] config) throws StatsUnavailableException {
        synchronized (this) {
            try {
                try {
                    try {
                        IStatsManager service = getIStatsManagerLocked();
                        service.addConfiguration(configKey, config, this.mContext.getOpPackageName());
                    } catch (SecurityException e) {
                        throw new StatsUnavailableException(e.getMessage(), e);
                    }
                } catch (RemoteException e2) {
                    Slog.e(TAG, "Failed to connect to statsd when adding configuration");
                    throw new StatsUnavailableException("could not connect", e2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean addConfiguration(long configKey, byte[] config) {
        try {
            addConfig(configKey, config);
            return true;
        } catch (StatsUnavailableException | IllegalArgumentException e) {
            return false;
        }
    }

    public void removeConfig(long configKey) throws StatsUnavailableException {
        synchronized (this) {
            try {
                try {
                    try {
                        IStatsManager service = getIStatsManagerLocked();
                        service.removeConfiguration(configKey, this.mContext.getOpPackageName());
                    } catch (SecurityException e) {
                        throw new StatsUnavailableException(e.getMessage(), e);
                    }
                } catch (RemoteException e2) {
                    Slog.e(TAG, "Failed to connect to statsd when removing configuration");
                    throw new StatsUnavailableException("could not connect", e2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean removeConfiguration(long configKey) {
        try {
            removeConfig(configKey);
            return true;
        } catch (StatsUnavailableException e) {
            return false;
        }
    }

    public void setBroadcastSubscriber(PendingIntent pendingIntent, long configKey, long subscriberId) throws StatsUnavailableException {
        synchronized (this) {
            try {
                try {
                    try {
                        IStatsManager service = getIStatsManagerLocked();
                        if (pendingIntent != null) {
                            IBinder intentSender = pendingIntent.getTarget().asBinder();
                            service.setBroadcastSubscriber(configKey, subscriberId, intentSender, this.mContext.getOpPackageName());
                        } else {
                            service.unsetBroadcastSubscriber(configKey, subscriberId, this.mContext.getOpPackageName());
                        }
                    } catch (SecurityException e) {
                        throw new StatsUnavailableException(e.getMessage(), e);
                    }
                } catch (RemoteException e2) {
                    Slog.e(TAG, "Failed to connect to statsd when adding broadcast subscriber", e2);
                    throw new StatsUnavailableException("could not connect", e2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean setBroadcastSubscriber(long configKey, long subscriberId, PendingIntent pendingIntent) {
        try {
            setBroadcastSubscriber(pendingIntent, configKey, subscriberId);
            return true;
        } catch (StatsUnavailableException e) {
            return false;
        }
    }

    public void setFetchReportsOperation(PendingIntent pendingIntent, long configKey) throws StatsUnavailableException {
        synchronized (this) {
            try {
                try {
                    IStatsManager service = getIStatsManagerLocked();
                    if (pendingIntent == null) {
                        service.removeDataFetchOperation(configKey, this.mContext.getOpPackageName());
                    } else {
                        IBinder intentSender = pendingIntent.getTarget().asBinder();
                        service.setDataFetchOperation(configKey, intentSender, this.mContext.getOpPackageName());
                    }
                } catch (RemoteException e) {
                    Slog.e(TAG, "Failed to connect to statsd when registering data listener.");
                    throw new StatsUnavailableException("could not connect", e);
                } catch (SecurityException e2) {
                    throw new StatsUnavailableException(e2.getMessage(), e2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean setDataFetchOperation(long configKey, PendingIntent pendingIntent) {
        try {
            setFetchReportsOperation(pendingIntent, configKey);
            return true;
        } catch (StatsUnavailableException e) {
            return false;
        }
    }

    public byte[] getReports(long configKey) throws StatsUnavailableException {
        byte[] data;
        synchronized (this) {
            try {
                try {
                    try {
                        IStatsManager service = getIStatsManagerLocked();
                        data = service.getData(configKey, this.mContext.getOpPackageName());
                    } catch (SecurityException e) {
                        throw new StatsUnavailableException(e.getMessage(), e);
                    }
                } catch (RemoteException e2) {
                    Slog.e(TAG, "Failed to connect to statsd when getting data");
                    throw new StatsUnavailableException("could not connect", e2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return data;
    }

    public byte[] getData(long configKey) {
        try {
            return getReports(configKey);
        } catch (StatsUnavailableException e) {
            return null;
        }
    }

    public byte[] getStatsMetadata() throws StatsUnavailableException {
        byte[] metadata;
        synchronized (this) {
            try {
                try {
                    try {
                        IStatsManager service = getIStatsManagerLocked();
                        metadata = service.getMetadata(this.mContext.getOpPackageName());
                    } catch (SecurityException e) {
                        throw new StatsUnavailableException(e.getMessage(), e);
                    }
                } catch (RemoteException e2) {
                    Slog.e(TAG, "Failed to connect to statsd when getting metadata");
                    throw new StatsUnavailableException("could not connect", e2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return metadata;
    }

    public byte[] getMetadata() {
        try {
            return getStatsMetadata();
        } catch (StatsUnavailableException e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class StatsdDeathRecipient implements IBinder.DeathRecipient {
        private StatsdDeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (this) {
                StatsManager.this.mService = null;
            }
        }
    }

    private synchronized IStatsManager getIStatsManagerLocked() throws StatsUnavailableException {
        if (this.mService != null) {
            return this.mService;
        }
        this.mService = IStatsManager.Stub.asInterface(ServiceManager.getService(Context.STATS_MANAGER));
        if (this.mService == null) {
            throw new StatsUnavailableException("could not be found");
        }
        try {
            this.mService.asBinder().linkToDeath(new StatsdDeathRecipient(), 0);
            return this.mService;
        } catch (RemoteException e) {
            throw new StatsUnavailableException("could not connect when linkToDeath", e);
        }
    }

    /* loaded from: classes.dex */
    public static class StatsUnavailableException extends AndroidException {
        public StatsUnavailableException(String reason) {
            super("Failed to connect to statsd: " + reason);
        }

        public StatsUnavailableException(String reason, Throwable e) {
            super("Failed to connect to statsd: " + reason, e);
        }
    }
}
