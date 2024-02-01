package com.android.internal.util;

import android.app.job.JobInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.view.InputDevice;
import java.util.function.Consumer;

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
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.util.ScreenshotHelper.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            synchronized (ScreenshotHelper.this.mScreenshotLock) {
                if (Intent.ACTION_USER_SWITCHED.equals(intent.getAction())) {
                    ScreenshotHelper.this.resetConnection();
                }
            }
        }
    };

    public ScreenshotHelper(Context context) {
        this.mContext = context;
        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_SWITCHED);
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
    }

    public void takeScreenshot(int screenshotType, boolean hasStatus, boolean hasNav, Handler handler, Consumer<Uri> completionConsumer) {
        takeScreenshot(screenshotType, hasStatus, hasNav, JobInfo.MIN_BACKOFF_MILLIS, handler, completionConsumer);
    }

    public void takeScreenshot(final int screenshotType, final boolean hasStatus, final boolean hasNav, long timeoutMs, final Handler handler, final Consumer<Uri> completionConsumer) {
        synchronized (this.mScreenshotLock) {
            try {
                try {
                    if (this.mScreenshotConnection != null) {
                        return;
                    }
                    ComponentName serviceComponent = new ComponentName(SYSUI_PACKAGE, SYSUI_SCREENSHOT_SERVICE);
                    Intent serviceIntent = new Intent();
                    try {
                        final Runnable mScreenshotTimeout = new Runnable() { // from class: com.android.internal.util.-$$Lambda$ScreenshotHelper$r9rp933Jt-vm9lU4BZ-gj9VY0YE
                            @Override // java.lang.Runnable
                            public final void run() {
                                ScreenshotHelper.this.lambda$takeScreenshot$0$ScreenshotHelper(completionConsumer);
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
                                                    ScreenshotHelper.this.resetConnection();
                                                    handler.removeCallbacks(mScreenshotTimeout);
                                                }
                                            }
                                            if (completionConsumer != null) {
                                                completionConsumer.accept((Uri) msg2.obj);
                                            }
                                        }
                                    };
                                    msg.replyTo = new Messenger(h);
                                    int i = 1;
                                    msg.arg1 = hasStatus ? 1 : 0;
                                    if (!hasNav) {
                                        i = 0;
                                    }
                                    msg.arg2 = i;
                                    try {
                                        messenger.send(msg);
                                    } catch (RemoteException e) {
                                        Log.e(ScreenshotHelper.TAG, "Couldn't take screenshot: " + e);
                                        if (completionConsumer != null) {
                                            completionConsumer.accept(null);
                                        }
                                    }
                                }
                            }

                            @Override // android.content.ServiceConnection
                            public void onServiceDisconnected(ComponentName name) {
                                synchronized (ScreenshotHelper.this.mScreenshotLock) {
                                    if (ScreenshotHelper.this.mScreenshotConnection != null) {
                                        ScreenshotHelper.this.resetConnection();
                                        handler.removeCallbacks(mScreenshotTimeout);
                                        ScreenshotHelper.this.notifyScreenshotError();
                                    }
                                }
                            }
                        };
                        if (this.mContext.bindServiceAsUser(serviceIntent, conn, InputDevice.SOURCE_HDMI, UserHandle.CURRENT)) {
                            this.mScreenshotConnection = conn;
                            handler.postDelayed(mScreenshotTimeout, timeoutMs);
                        }
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    public /* synthetic */ void lambda$takeScreenshot$0$ScreenshotHelper(Consumer completionConsumer) {
        synchronized (this.mScreenshotLock) {
            if (this.mScreenshotConnection != null) {
                Log.e(TAG, "Timed out before getting screenshot capture response");
                resetConnection();
                notifyScreenshotError();
            }
            if (completionConsumer != null) {
                completionConsumer.accept(null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetConnection() {
        ServiceConnection serviceConnection = this.mScreenshotConnection;
        if (serviceConnection != null) {
            this.mContext.unbindService(serviceConnection);
            this.mScreenshotConnection = null;
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
