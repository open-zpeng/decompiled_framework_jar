package com.xiaopeng.biutil;

import android.app.ActivityThread;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Log;
import com.xiaopeng.util.FeatureOption;
/* loaded from: classes3.dex */
public class BiLogUploader {
    private static final String BI_UPLOADLOG_ACTION = "android.intent.action.BI_UPLOADLOG";
    private static final String BI_UPLOADLOG_EXTRA_LOG = "LogString";
    private static final String TAG = "BiLogUploader";

    public synchronized void submit(BiLog biLog) {
        if (!FeatureOption.FO_DEVICE_INTERNATIONAL_ENABLED && SystemProperties.getInt("sys.boot_completed", 0) == 1) {
            Context context = ActivityThread.currentApplication();
            Intent intent = new Intent(BI_UPLOADLOG_ACTION);
            Log.d(TAG, "intent(" + biLog.getString() + ")");
            try {
                intent.setFlags(67108864);
                intent.addFlags(4194304);
                intent.setPackage("com.xiaopeng.xuiservice");
                intent.putExtra(BI_UPLOADLOG_EXTRA_LOG, biLog.getString());
                context.sendBroadcastAsUser(intent, UserHandle.ALL);
            } catch (Exception e) {
                Log.e(TAG, "submit failed " + e);
            }
        }
    }
}
