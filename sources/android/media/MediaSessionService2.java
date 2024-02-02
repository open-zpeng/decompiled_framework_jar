package android.media;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.update.ApiLoader;
import android.media.update.MediaSessionService2Provider;
import android.os.IBinder;
/* loaded from: classes.dex */
public abstract class MediaSessionService2 extends Service {
    public static final String SERVICE_INTERFACE = "android.media.MediaSessionService2";
    public static final String SERVICE_META_DATA = "android.media.session";
    private final MediaSessionService2Provider mProvider = createProvider();

    public abstract synchronized MediaSession2 onCreateSession(String str);

    synchronized MediaSessionService2Provider createProvider() {
        return ApiLoader.getProvider().createMediaSessionService2(this);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mProvider.onCreate_impl();
    }

    public synchronized MediaNotification onUpdateNotification() {
        return this.mProvider.onUpdateNotification_impl();
    }

    public final synchronized MediaSession2 getSession() {
        return this.mProvider.getSession_impl();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mProvider.onBind_impl(intent);
    }

    /* loaded from: classes.dex */
    public static class MediaNotification {
        private final MediaSessionService2Provider.MediaNotificationProvider mProvider;

        public synchronized MediaNotification(int notificationId, Notification notification) {
            this.mProvider = ApiLoader.getProvider().createMediaSessionService2MediaNotification(this, notificationId, notification);
        }

        public synchronized int getNotificationId() {
            return this.mProvider.getNotificationId_impl();
        }

        public synchronized Notification getNotification() {
            return this.mProvider.getNotification_impl();
        }
    }
}
