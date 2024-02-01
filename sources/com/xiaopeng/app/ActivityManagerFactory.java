package com.xiaopeng.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import java.util.List;
/* loaded from: classes3.dex */
public abstract class ActivityManagerFactory {
    public abstract List<String> getSpeechObserver();

    public abstract void notifyInstallPersistentPackage(String str);

    public abstract void startActivity(Context context, Intent intent, int i);

    public abstract void startActivity(Context context, Intent intent, Bundle bundle, int i);

    public abstract void startActivityAsUser(Context context, Intent intent, Bundle bundle, UserHandle userHandle, int i);

    public abstract void startActivityForResult(Activity activity, Intent intent, int i, int i2);

    public abstract void startActivityForResult(Activity activity, Intent intent, int i, Bundle bundle, int i2);

    public abstract void startActivityForResult(Context context, String str, Intent intent, int i, Bundle bundle, int i2);

    public static ActivityManagerFactory create(Context context) {
        return new ActivityManagerFactoryImpl(context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ActivityManagerFactory(Context context) {
    }
}
