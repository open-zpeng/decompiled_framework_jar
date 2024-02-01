package com.xiaopeng.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;

/* loaded from: classes3.dex */
public class ActivityManagerFactory {
    private ActivityManager mActivityManager;

    public static ActivityManagerFactory create(Context context) {
        return new ActivityManagerFactory(context);
    }

    private ActivityManagerFactory(Context context) {
        this.mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public static void startActivity(Context context, Intent intent, int screenId) {
        startActivity(context, intent, null, screenId);
    }

    public static void startActivity(Context context, Intent intent, Bundle options, int screenId) {
        if (context == null || intent == null) {
            return;
        }
        intent.setScreenId(screenId);
        context.startActivity(intent, options);
    }

    public static void startActivityAsUser(Context context, Intent intent, Bundle options, UserHandle userId, int screenId) {
        if (context == null || intent == null) {
            return;
        }
        intent.setScreenId(screenId);
        context.startActivityAsUser(intent, options, userId);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode, int sharedId) {
        startActivityForResult(activity, intent, requestCode, null, sharedId);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode, Bundle options, int screenId) {
        if (activity == null || intent == null) {
            return;
        }
        intent.setScreenId(screenId);
        activity.startActivityForResult(intent, requestCode, options);
    }

    public static void startActivityForResult(Context context, String who, Intent intent, int requestCode, Bundle options, int screenId) {
        if (context == null || intent == null) {
            return;
        }
        intent.setScreenId(screenId);
        context.startActivityForResult(who, intent, requestCode, options);
    }
}
