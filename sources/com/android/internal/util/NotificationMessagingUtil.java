package com.android.internal.util;

import android.app.Notification;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import java.util.Objects;

/* loaded from: classes3.dex */
public class NotificationMessagingUtil {
    private static final String DEFAULT_SMS_APP_SETTING = "sms_default_application";
    private final Context mContext;
    private ArrayMap<Integer, String> mDefaultSmsApp = new ArrayMap<>();
    private final ContentObserver mSmsContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.android.internal.util.NotificationMessagingUtil.1
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri, int userId) {
            if (Settings.Secure.getUriFor("sms_default_application").equals(uri)) {
                NotificationMessagingUtil.this.cacheDefaultSmsApp(userId);
            }
        }
    };

    public NotificationMessagingUtil(Context context) {
        this.mContext = context;
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("sms_default_application"), false, this.mSmsContentObserver);
    }

    public boolean isImportantMessaging(StatusBarNotification sbn, int importance) {
        if (importance < 2) {
            return false;
        }
        return hasMessagingStyle(sbn) || (isCategoryMessage(sbn) && isDefaultMessagingApp(sbn));
    }

    public boolean isMessaging(StatusBarNotification sbn) {
        return hasMessagingStyle(sbn) || isDefaultMessagingApp(sbn) || isCategoryMessage(sbn);
    }

    private boolean isDefaultMessagingApp(StatusBarNotification sbn) {
        int userId = sbn.getUserId();
        if (userId == -10000 || userId == -1) {
            return false;
        }
        if (this.mDefaultSmsApp.get(Integer.valueOf(userId)) == null) {
            cacheDefaultSmsApp(userId);
        }
        return Objects.equals(this.mDefaultSmsApp.get(Integer.valueOf(userId)), sbn.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cacheDefaultSmsApp(int userId) {
        this.mDefaultSmsApp.put(Integer.valueOf(userId), Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "sms_default_application", userId));
    }

    private boolean hasMessagingStyle(StatusBarNotification sbn) {
        Class<? extends Notification.Style> style = sbn.getNotification().getNotificationStyle();
        return Notification.MessagingStyle.class.equals(style);
    }

    private boolean isCategoryMessage(StatusBarNotification sbn) {
        return Notification.CATEGORY_MESSAGE.equals(sbn.getNotification().category);
    }
}
