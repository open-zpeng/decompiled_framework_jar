package android.app;

import android.app.IActivityManager;
import android.content.Intent;
import android.os.IBinder;
@Deprecated
/* loaded from: classes.dex */
public abstract class ActivityManagerNative {
    private protected ActivityManagerNative() {
    }

    private protected static IActivityManager asInterface(IBinder obj) {
        return IActivityManager.Stub.asInterface(obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IActivityManager getDefault() {
        return ActivityManager.getService();
    }

    private protected static boolean isSystemReady() {
        return ActivityManager.isSystemReady();
    }

    private protected static void broadcastStickyIntent(Intent intent, String permission, int userId) {
        broadcastStickyIntent(intent, permission, -1, userId);
    }

    public static synchronized void broadcastStickyIntent(Intent intent, String permission, int appOp, int userId) {
        ActivityManager.broadcastStickyIntent(intent, appOp, userId);
    }

    public static synchronized void noteWakeupAlarm(PendingIntent ps, int sourceUid, String sourcePkg, String tag) {
        ActivityManager.noteWakeupAlarm(ps, null, sourceUid, sourcePkg, tag);
    }

    public static synchronized void noteAlarmStart(PendingIntent ps, int sourceUid, String tag) {
        ActivityManager.noteAlarmStart(ps, null, sourceUid, tag);
    }

    public static synchronized void noteAlarmFinish(PendingIntent ps, int sourceUid, String tag) {
        ActivityManager.noteAlarmFinish(ps, null, sourceUid, tag);
    }
}
