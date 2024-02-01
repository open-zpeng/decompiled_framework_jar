package android.app.usage;

import android.content.ComponentName;
import android.content.res.Configuration;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class UsageStatsManagerInternal {
    public abstract synchronized void addAppIdleStateChangeListener(AppIdleStateChangeListener appIdleStateChangeListener);

    public abstract synchronized void applyRestoredPayload(int i, String str, byte[] bArr);

    public abstract synchronized int getAppStandbyBucket(String str, int i, long j);

    public abstract synchronized byte[] getBackupPayload(int i, String str);

    public abstract synchronized int[] getIdleUidsForUser(int i);

    public abstract synchronized long getTimeSinceLastJobRun(String str, int i);

    public abstract synchronized boolean isAppIdle(String str, int i, int i2);

    public abstract synchronized boolean isAppIdleParoleOn();

    public abstract synchronized void onActiveAdminAdded(String str, int i);

    public abstract synchronized void onAdminDataAvailable();

    public abstract synchronized void prepareShutdown();

    public abstract synchronized List<UsageStats> queryUsageStatsForUser(int i, int i2, long j, long j2, boolean z);

    public abstract synchronized void removeAppIdleStateChangeListener(AppIdleStateChangeListener appIdleStateChangeListener);

    public abstract synchronized void reportAppJobState(String str, int i, int i2, long j);

    public abstract synchronized void reportConfigurationChange(Configuration configuration, int i);

    public abstract synchronized void reportContentProviderUsage(String str, String str2, int i);

    public abstract synchronized void reportEvent(ComponentName componentName, int i, int i2);

    public abstract synchronized void reportEvent(String str, int i, int i2);

    public abstract synchronized void reportExemptedSyncScheduled(String str, int i);

    public abstract synchronized void reportExemptedSyncStart(String str, int i);

    public abstract synchronized void reportInterruptiveNotification(String str, String str2, int i);

    public abstract synchronized void reportShortcutUsage(String str, String str2, int i);

    public abstract synchronized void setActiveAdminApps(Set<String> set, int i);

    public abstract synchronized void setLastJobRunTime(String str, int i, long j);

    /* loaded from: classes.dex */
    public static abstract class AppIdleStateChangeListener {
        public abstract synchronized void onAppIdleStateChanged(String str, int i, boolean z, int i2, int i3);

        public abstract synchronized void onParoleStateChanged(boolean z);

        public synchronized void onUserInteractionStarted(String packageName, int userId) {
        }
    }
}
