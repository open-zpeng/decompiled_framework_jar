package android.net.util;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Slog;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class MultinetworkPolicyTracker {
    private static String TAG = MultinetworkPolicyTracker.class.getSimpleName();
    private int mActiveSubId;
    private volatile boolean mAvoidBadWifi;
    private final BroadcastReceiver mBroadcastReceiver;
    private final Context mContext;
    private final Handler mHandler;
    private volatile int mMeteredMultipathPreference;
    private final Runnable mReevaluateRunnable;
    private final ContentResolver mResolver;
    private final SettingObserver mSettingObserver;
    private final List<Uri> mSettingsUris;

    public MultinetworkPolicyTracker(Context ctx, Handler handler) {
        this(ctx, handler, null);
    }

    public MultinetworkPolicyTracker(Context ctx, Handler handler, final Runnable avoidBadWifiCallback) {
        this.mAvoidBadWifi = true;
        this.mActiveSubId = -1;
        this.mContext = ctx;
        this.mHandler = handler;
        this.mReevaluateRunnable = new Runnable() { // from class: android.net.util.-$$Lambda$MultinetworkPolicyTracker$0siHK6f4lHJz8hbdHbT6G4Kp-V4
            @Override // java.lang.Runnable
            public final void run() {
                MultinetworkPolicyTracker.this.lambda$new$0$MultinetworkPolicyTracker(avoidBadWifiCallback);
            }
        };
        this.mSettingsUris = Arrays.asList(Settings.Global.getUriFor(Settings.Global.NETWORK_AVOID_BAD_WIFI), Settings.Global.getUriFor(Settings.Global.NETWORK_METERED_MULTIPATH_PREFERENCE));
        this.mResolver = this.mContext.getContentResolver();
        this.mSettingObserver = new SettingObserver();
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: android.net.util.MultinetworkPolicyTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                MultinetworkPolicyTracker.this.reevaluate();
            }
        };
        TelephonyManager.from(ctx).listen(new PhoneStateListener() { // from class: android.net.util.MultinetworkPolicyTracker.2
            @Override // android.telephony.PhoneStateListener
            public void onActiveDataSubscriptionIdChanged(int subId) {
                MultinetworkPolicyTracker.this.mActiveSubId = subId;
                MultinetworkPolicyTracker.this.reevaluate();
            }
        }, 4194304);
        updateAvoidBadWifi();
        updateMeteredMultipathPreference();
    }

    public /* synthetic */ void lambda$new$0$MultinetworkPolicyTracker(Runnable avoidBadWifiCallback) {
        if (updateAvoidBadWifi() && avoidBadWifiCallback != null) {
            avoidBadWifiCallback.run();
        }
        updateMeteredMultipathPreference();
    }

    public void start() {
        for (Uri uri : this.mSettingsUris) {
            this.mResolver.registerContentObserver(uri, false, this.mSettingObserver);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, UserHandle.ALL, intentFilter, null, null);
        reevaluate();
    }

    public void shutdown() {
        this.mResolver.unregisterContentObserver(this.mSettingObserver);
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
    }

    public boolean getAvoidBadWifi() {
        return this.mAvoidBadWifi;
    }

    public int getMeteredMultipathPreference() {
        return this.mMeteredMultipathPreference;
    }

    public boolean configRestrictsAvoidBadWifi() {
        return getResourcesForActiveSubId().getInteger(R.integer.config_networkAvoidBadWifi) == 0;
    }

    private Resources getResourcesForActiveSubId() {
        return SubscriptionManager.getResourcesForSubId(this.mContext, this.mActiveSubId);
    }

    public boolean shouldNotifyWifiUnvalidated() {
        return configRestrictsAvoidBadWifi() && getAvoidBadWifiSetting() == null;
    }

    public String getAvoidBadWifiSetting() {
        return Settings.Global.getString(this.mResolver, Settings.Global.NETWORK_AVOID_BAD_WIFI);
    }

    @VisibleForTesting
    public void reevaluate() {
        this.mHandler.post(this.mReevaluateRunnable);
    }

    public boolean updateAvoidBadWifi() {
        boolean settingAvoidBadWifi = WifiEnterpriseConfig.ENGINE_ENABLE.equals(getAvoidBadWifiSetting());
        boolean prev = this.mAvoidBadWifi;
        this.mAvoidBadWifi = settingAvoidBadWifi || !configRestrictsAvoidBadWifi();
        return this.mAvoidBadWifi != prev;
    }

    public int configMeteredMultipathPreference() {
        return this.mContext.getResources().getInteger(R.integer.config_networkMeteredMultipathPreference);
    }

    public void updateMeteredMultipathPreference() {
        String setting = Settings.Global.getString(this.mResolver, Settings.Global.NETWORK_METERED_MULTIPATH_PREFERENCE);
        try {
            this.mMeteredMultipathPreference = Integer.parseInt(setting);
        } catch (NumberFormatException e) {
            this.mMeteredMultipathPreference = configMeteredMultipathPreference();
        }
    }

    /* loaded from: classes2.dex */
    private class SettingObserver extends ContentObserver {
        public SettingObserver() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            Slog.wtf(MultinetworkPolicyTracker.TAG, "Should never be reached.");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            if (!MultinetworkPolicyTracker.this.mSettingsUris.contains(uri)) {
                String str = MultinetworkPolicyTracker.TAG;
                Slog.wtf(str, "Unexpected settings observation: " + uri);
            }
            MultinetworkPolicyTracker.this.reevaluate();
        }
    }
}
