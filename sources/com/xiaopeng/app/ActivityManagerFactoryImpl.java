package com.xiaopeng.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import java.util.List;
/* loaded from: classes3.dex */
public class ActivityManagerFactoryImpl extends ActivityManagerFactory {
    /* JADX INFO: Access modifiers changed from: protected */
    public ActivityManagerFactoryImpl(Context context) {
        super(context);
    }

    @Override // com.xiaopeng.app.ActivityManagerFactory
    public List<String> getSpeechObserver() {
        try {
            return ActivityManager.getService().getSpeechObserver();
        } catch (Exception e) {
            return null;
        }
    }

    @Override // com.xiaopeng.app.ActivityManagerFactory
    public void notifyInstallPersistentPackage(String packageName) {
    }

    @Override // com.xiaopeng.app.ActivityManagerFactory
    public void startActivity(Context context, Intent intent, int screenId) {
        context.startActivity(intent);
    }

    @Override // com.xiaopeng.app.ActivityManagerFactory
    public void startActivity(Context context, Intent intent, Bundle options, int screenId) {
        context.startActivity(intent, options);
    }

    @Override // com.xiaopeng.app.ActivityManagerFactory
    public void startActivityAsUser(Context context, Intent intent, Bundle options, UserHandle userId, int screenId) {
        context.startActivity(intent, options);
    }

    @Override // com.xiaopeng.app.ActivityManagerFactory
    public void startActivityForResult(Activity activity, Intent intent, int requestCode, int screenId) {
    }

    @Override // com.xiaopeng.app.ActivityManagerFactory
    public void startActivityForResult(Activity activity, Intent intent, int requestCode, Bundle options, int screenId) {
    }

    @Override // com.xiaopeng.app.ActivityManagerFactory
    public void startActivityForResult(Context context, String who, Intent intent, int requestCode, Bundle options, int screenId) {
    }
}
