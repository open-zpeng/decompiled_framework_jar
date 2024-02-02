package android.database.sqlite;

import android.app.ActivityThread;
import android.app.Application;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.KeyValueListParser;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
/* loaded from: classes.dex */
public class SQLiteCompatibilityWalFlags {
    private static final String TAG = "SQLiteCompatibilityWalFlags";
    private static volatile boolean sCallingGlobalSettings;
    private static volatile boolean sCompatibilityWalSupported;
    private static volatile boolean sFlagsSet;
    private static volatile boolean sInitialized;
    private static volatile long sTruncateSize = -1;
    private static volatile String sWALSyncMode;

    @VisibleForTesting
    public static synchronized boolean areFlagsSet() {
        initIfNeeded();
        return sFlagsSet;
    }

    @VisibleForTesting
    public static synchronized boolean isCompatibilityWalSupported() {
        initIfNeeded();
        return sCompatibilityWalSupported;
    }

    @VisibleForTesting
    public static synchronized String getWALSyncMode() {
        initIfNeeded();
        return sWALSyncMode;
    }

    @VisibleForTesting
    public static long getTruncateSize() {
        initIfNeeded();
        return sTruncateSize;
    }

    private static synchronized void initIfNeeded() {
        if (sInitialized || sCallingGlobalSettings) {
            return;
        }
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        Application app = activityThread == null ? null : activityThread.getApplication();
        String flags = null;
        if (app == null) {
            Log.w(TAG, "Cannot read global setting sqlite_compatibility_wal_flags - Application state not available");
        } else {
            try {
                sCallingGlobalSettings = true;
                flags = Settings.Global.getString(app.getContentResolver(), Settings.Global.SQLITE_COMPATIBILITY_WAL_FLAGS);
            } finally {
                sCallingGlobalSettings = false;
            }
        }
        init(flags);
    }

    @VisibleForTesting
    public static synchronized void init(String flags) {
        if (TextUtils.isEmpty(flags)) {
            sInitialized = true;
            return;
        }
        KeyValueListParser parser = new KeyValueListParser(',');
        try {
            parser.setString(flags);
            sCompatibilityWalSupported = parser.getBoolean("compatibility_wal_supported", SQLiteGlobal.isCompatibilityWalSupported());
            sWALSyncMode = parser.getString("wal_syncmode", SQLiteGlobal.getWALSyncMode());
            sTruncateSize = parser.getInt("truncate_size", -1);
            Log.i(TAG, "Read compatibility WAL flags: compatibility_wal_supported=" + sCompatibilityWalSupported + ", wal_syncmode=" + sWALSyncMode);
            sFlagsSet = true;
            sInitialized = true;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Setting has invalid format: " + flags, e);
            sInitialized = true;
        }
    }

    @VisibleForTesting
    public static synchronized void reset() {
        sInitialized = false;
        sFlagsSet = false;
        sCompatibilityWalSupported = false;
        sWALSyncMode = null;
    }
}
