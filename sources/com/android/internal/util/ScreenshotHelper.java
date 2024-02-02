package com.android.internal.util;

import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.view.InputDevice;
/* loaded from: classes3.dex */
public class ScreenshotHelper {
    private static final String SYSUI_PACKAGE = "com.android.systemui";
    private static final String SYSUI_SCREENSHOT_ERROR_RECEIVER = "com.android.systemui.screenshot.ScreenshotServiceErrorReceiver";
    private static final String SYSUI_SCREENSHOT_SERVICE = "com.android.systemui.screenshot.TakeScreenshotService";
    private static final String TAG = "ScreenshotHelper";
    private final Context mContext;
    private final int SCREENSHOT_TIMEOUT_MS = 10000;
    private final Object mScreenshotLock = new Object();
    private ServiceConnection mScreenshotConnection = null;

    public ScreenshotHelper(Context context) {
        this.mContext = context;
    }

    public void takeScreenshot(final int screenshotType, final boolean hasStatus, final boolean hasNav, final Handler handler) {
        synchronized (this.mScreenshotLock) {
            try {
                try {
                    if (this.mScreenshotConnection != null) {
                        return;
                    }
                    ComponentName serviceComponent = new ComponentName(SYSUI_PACKAGE, SYSUI_SCREENSHOT_SERVICE);
                    Intent serviceIntent = new Intent();
                    final Runnable mScreenshotTimeout = new Runnable() { // from class: com.android.internal.util.ScreenshotHelper.1
                        @Override // java.lang.Runnable
                        public void run() {
                            synchronized (ScreenshotHelper.this.mScreenshotLock) {
                                if (ScreenshotHelper.this.mScreenshotConnection != null) {
                                    ScreenshotHelper.this.mContext.unbindService(ScreenshotHelper.this.mScreenshotConnection);
                                    ScreenshotHelper.this.mScreenshotConnection = null;
                                    ScreenshotHelper.this.notifyScreenshotError();
                                }
                            }
                        }
                    };
                    serviceIntent.setComponent(serviceComponent);
                    ServiceConnection conn = new ServiceConnection() { // from class: com.android.internal.util.ScreenshotHelper.2
                        @Override // android.content.ServiceConnection
                        public void onServiceConnected(ComponentName name, IBinder service) {
                            synchronized (ScreenshotHelper.this.mScreenshotLock) {
                                if (ScreenshotHelper.this.mScreenshotConnection != this) {
                                    return;
                                }
                                Messenger messenger = new Messenger(service);
                                Message msg = Message.obtain((Handler) null, screenshotType);
                                Handler h = new Handler(handler.getLooper()) { // from class: com.android.internal.util.ScreenshotHelper.2.1
                                    @Override // android.os.Handler
                                    public void handleMessage(Message msg2) {
                                        synchronized (ScreenshotHelper.this.mScreenshotLock) {
                                            if (ScreenshotHelper.this.mScreenshotConnection == this) {
                                                ScreenshotHelper.this.mContext.unbindService(ScreenshotHelper.this.mScreenshotConnection);
                                                ScreenshotHelper.this.mScreenshotConnection = null;
                                                handler.removeCallbacks(mScreenshotTimeout);
                                            }
                                        }
                                    }
                                };
                                msg.replyTo = new Messenger(h);
                                msg.arg1 = hasStatus ? 1 : 0;
                                msg.arg2 = hasNav ? 1 : 0;
                                try {
                                    messenger.send(msg);
                                } catch (RemoteException e) {
                                    Log.e(ScreenshotHelper.TAG, "Couldn't take screenshot: " + e);
                                }
                            }
                        }

                        @Override // android.content.ServiceConnection
                        public void onServiceDisconnected(ComponentName name) {
                            synchronized (ScreenshotHelper.this.mScreenshotLock) {
                                if (ScreenshotHelper.this.mScreenshotConnection != null) {
                                    ScreenshotHelper.this.mContext.unbindService(ScreenshotHelper.this.mScreenshotConnection);
                                    ScreenshotHelper.this.mScreenshotConnection = null;
                                    handler.removeCallbacks(mScreenshotTimeout);
                                    ScreenshotHelper.this.notifyScreenshotError();
                                }
                            }
                        }
                    };
                    if (this.mContext.bindServiceAsUser(serviceIntent, conn, InputDevice.SOURCE_HDMI, UserHandle.CURRENT)) {
                        this.mScreenshotConnection = conn;
                        handler.postDelayed(mScreenshotTimeout, JobInfo.MIN_BACKOFF_MILLIS);
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyScreenshotError() {
        ComponentName errorComponent = new ComponentName(SYSUI_PACKAGE, SYSUI_SCREENSHOT_ERROR_RECEIVER);
        Intent errorIntent = new Intent(Intent.ACTION_USER_PRESENT);
        errorIntent.setComponent(errorComponent);
        errorIntent.addFlags(335544320);
        this.mContext.sendBroadcastAsUser(errorIntent, UserHandle.CURRENT);
    }
}
