package android.media.update;

import android.app.Notification;
import android.content.Intent;
import android.media.MediaSession2;
import android.media.MediaSessionService2;
import android.os.IBinder;
/* loaded from: classes2.dex */
public interface MediaSessionService2Provider {

    /* loaded from: classes2.dex */
    public interface MediaNotificationProvider {
        /* JADX INFO: Access modifiers changed from: private */
        synchronized int getNotificationId_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized Notification getNotification_impl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaSession2 getSession_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized IBinder onBind_impl(Intent intent);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onCreate_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaSessionService2.MediaNotification onUpdateNotification_impl();
}
