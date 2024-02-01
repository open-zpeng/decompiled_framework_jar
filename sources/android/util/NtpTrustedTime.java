package android.util;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.SntpClient;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.internal.R;

/* loaded from: classes2.dex */
public class NtpTrustedTime implements TrustedTime {
    private static final boolean LOGD = false;
    private static final String TAG = "NtpTrustedTime";
    private static Context sContext;
    private static NtpTrustedTime sSingleton;
    private ConnectivityManager mCM;
    private long mCachedNtpCertainty;
    private long mCachedNtpElapsedRealtime;
    private long mCachedNtpTime;
    private boolean mHasCache;
    private final String[] mNtpServices;
    private String mServer;
    private final long mTimeout;
    private int mNtpServiceCount = 0;
    private final Object mNtpTrustedTimeLock = new Object();

    private NtpTrustedTime(String[] server, long timeout) {
        this.mNtpServices = server;
        this.mTimeout = timeout;
    }

    @UnsupportedAppUsage
    public static NtpTrustedTime getInstance(Context context) {
        if (sSingleton == null) {
            synchronized (NtpTrustedTime.class) {
                if (sSingleton == null) {
                    Resources res = context.getResources();
                    ContentResolver resolver = context.getContentResolver();
                    long defaultTimeout = res.getInteger(R.integer.config_ntpTimeout);
                    long timeout = Settings.Global.getLong(resolver, Settings.Global.NTP_TIMEOUT, defaultTimeout);
                    String[] services = res.getStringArray(R.array.ntp_services);
                    sSingleton = new NtpTrustedTime(services, timeout);
                    sContext = context;
                }
            }
        }
        return sSingleton;
    }

    @Override // android.util.TrustedTime
    @UnsupportedAppUsage
    public boolean forceRefresh() {
        synchronized (this) {
            if (this.mCM == null) {
                this.mCM = (ConnectivityManager) sContext.getSystemService(ConnectivityManager.class);
            }
        }
        ConnectivityManager connectivityManager = this.mCM;
        Network network = connectivityManager == null ? null : connectivityManager.getActiveNetwork();
        return forceRefresh(network);
    }

    public boolean forceRefresh(Network network) {
        if (this.mNtpServices == null) {
            return false;
        }
        synchronized (this) {
            if (this.mCM == null) {
                this.mCM = (ConnectivityManager) sContext.getSystemService(ConnectivityManager.class);
            }
        }
        ConnectivityManager connectivityManager = this.mCM;
        NetworkInfo ni = connectivityManager == null ? null : connectivityManager.getNetworkInfo(network);
        if (ni == null || !ni.isConnected()) {
            return false;
        }
        SntpClient client = new SntpClient();
        synchronized (this.mNtpTrustedTimeLock) {
            if (this.mNtpServiceCount == this.mNtpServices.length) {
                this.mNtpServiceCount = 0;
            }
            this.mServer = this.mNtpServices[this.mNtpServiceCount];
            Log.d(TAG, "mServer = " + this.mServer);
            this.mNtpServiceCount = this.mNtpServiceCount + 1;
        }
        if (!TextUtils.isEmpty(this.mServer) && client.requestTime(this.mServer, (int) this.mTimeout, network)) {
            this.mHasCache = true;
            this.mCachedNtpTime = client.getNtpTime();
            this.mCachedNtpElapsedRealtime = client.getNtpTimeReference();
            EventLog.writeEvent((int) android.os.EventLogTags.TIME_NTP_TRUSTED, this.mServer, Long.valueOf(this.mCachedNtpTime), Long.valueOf(this.mCachedNtpElapsedRealtime));
            this.mCachedNtpCertainty = client.getRoundTripTime() / 2;
            return true;
        }
        return false;
    }

    @Override // android.util.TrustedTime
    @UnsupportedAppUsage
    public boolean hasCache() {
        return this.mHasCache;
    }

    @Override // android.util.TrustedTime
    public long getCacheAge() {
        if (this.mHasCache) {
            return SystemClock.elapsedRealtime() - this.mCachedNtpElapsedRealtime;
        }
        return Long.MAX_VALUE;
    }

    @Override // android.util.TrustedTime
    public long getCacheCertainty() {
        if (this.mHasCache) {
            return this.mCachedNtpCertainty;
        }
        return Long.MAX_VALUE;
    }

    @Override // android.util.TrustedTime
    @UnsupportedAppUsage
    public long currentTimeMillis() {
        if (!this.mHasCache) {
            throw new IllegalStateException("Missing authoritative time source");
        }
        return this.mCachedNtpTime + getCacheAge();
    }

    @UnsupportedAppUsage
    public long getCachedNtpTime() {
        return this.mCachedNtpTime;
    }

    @UnsupportedAppUsage
    public long getCachedNtpTimeReference() {
        return this.mCachedNtpElapsedRealtime;
    }
}
