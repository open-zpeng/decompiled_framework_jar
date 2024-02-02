package android.app;

import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.xiaopeng.util.DebugOption;
import com.xiaopeng.util.xpLogger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public abstract class Service extends ContextWrapper implements ComponentCallbacks2 {
    private static final boolean DEBUG = DebugOption.DEBUG_AM_SERVICE;
    public static final int START_CONTINUATION_MASK = 15;
    public static final int START_FLAG_REDELIVERY = 1;
    public static final int START_FLAG_RETRY = 2;
    public static final int START_NOT_STICKY = 2;
    public static final int START_REDELIVER_INTENT = 3;
    public static final int START_STICKY = 1;
    public static final int START_STICKY_COMPATIBILITY = 0;
    public static final int START_TASK_REMOVED_COMPLETE = 1000;
    public static final int STOP_FOREGROUND_DETACH = 2;
    public static final int STOP_FOREGROUND_REMOVE = 1;
    private static final String TAG = "Service";
    public protected IActivityManager mActivityManager;
    public protected Application mApplication;
    public protected String mClassName;
    public protected boolean mStartCompatibility;
    public protected ActivityThread mThread;
    public protected IBinder mToken;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface StartArgFlags {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface StartResult {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface StopForegroundFlags {
    }

    public abstract IBinder onBind(Intent intent);

    public Service() {
        super(null);
        this.mThread = null;
        this.mClassName = null;
        this.mToken = null;
        this.mApplication = null;
        this.mActivityManager = null;
        this.mStartCompatibility = false;
    }

    public final Application getApplication() {
        return this.mApplication;
    }

    public void onCreate() {
        if (DEBUG) {
            xpLogger.i(TAG, "onCreate class=" + getClassName());
        }
    }

    @Deprecated
    public void onStart(Intent intent, int startId) {
        if (DEBUG) {
            xpLogger.i(TAG, "onStart class=" + getClassName());
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        onStart(intent, startId);
        if (DEBUG) {
            xpLogger.i(TAG, "onStartCommand class=" + getClassName());
        }
        return !this.mStartCompatibility ? 1 : 0;
    }

    public void onDestroy() {
        if (DEBUG) {
            xpLogger.i(TAG, "onDestroy class=" + getClassName());
        }
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
        if (DEBUG) {
            xpLogger.i(TAG, "onLowMemory class=" + getClassName());
        }
    }

    @Override // android.content.ComponentCallbacks2
    public void onTrimMemory(int level) {
        if (DEBUG) {
            xpLogger.i(TAG, "onTrimMemory class=" + getClassName());
        }
    }

    public boolean onUnbind(Intent intent) {
        return false;
    }

    public void onRebind(Intent intent) {
    }

    public void onTaskRemoved(Intent rootIntent) {
    }

    public final void stopSelf() {
        stopSelf(-1);
    }

    public final void stopSelf(int startId) {
        if (this.mActivityManager == null) {
            return;
        }
        try {
            this.mActivityManager.stopServiceToken(new ComponentName(this, this.mClassName), this.mToken, startId);
        } catch (RemoteException e) {
        }
    }

    public final boolean stopSelfResult(int startId) {
        if (this.mActivityManager == null) {
            return false;
        }
        try {
            return this.mActivityManager.stopServiceToken(new ComponentName(this, this.mClassName), this.mToken, startId);
        } catch (RemoteException e) {
            return false;
        }
    }

    @Deprecated
    private protected final void setForeground(boolean isForeground) {
        Log.w(TAG, "setForeground: ignoring old API call on " + getClass().getName());
    }

    public final void startForeground(int id, Notification notification) {
        try {
            this.mActivityManager.setServiceForeground(new ComponentName(this, this.mClassName), this.mToken, id, notification, 0);
        } catch (RemoteException e) {
        }
    }

    public final void stopForeground(boolean removeNotification) {
        stopForeground(removeNotification ? 1 : 0);
    }

    public final void stopForeground(int flags) {
        try {
            this.mActivityManager.setServiceForeground(new ComponentName(this, this.mClassName), this.mToken, 0, null, flags);
        } catch (RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.println("nothing to dump");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void attach(Context context, ActivityThread thread, String className, IBinder token, Application application, Object activityManager) {
        attachBaseContext(context);
        this.mThread = thread;
        this.mClassName = className;
        this.mToken = token;
        this.mApplication = application;
        this.mActivityManager = (IActivityManager) activityManager;
        this.mStartCompatibility = getApplicationInfo().targetSdkVersion < 5;
    }

    public final synchronized void detachAndCleanUp() {
        this.mToken = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized String getClassName() {
        return this.mClassName;
    }
}
