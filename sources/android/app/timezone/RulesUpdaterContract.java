package android.app.timezone;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
/* loaded from: classes.dex */
public final class RulesUpdaterContract {
    private protected static final String ACTION_TRIGGER_RULES_UPDATE_CHECK = "com.android.intent.action.timezone.TRIGGER_RULES_UPDATE_CHECK";
    private protected static final String EXTRA_CHECK_TOKEN = "com.android.intent.extra.timezone.CHECK_TOKEN";
    private protected static final String TRIGGER_TIME_ZONE_RULES_CHECK_PERMISSION = "android.permission.TRIGGER_TIME_ZONE_RULES_CHECK";
    private protected static final String UPDATE_TIME_ZONE_RULES_PERMISSION = "android.permission.UPDATE_TIME_ZONE_RULES";

    private protected synchronized RulesUpdaterContract() {
    }

    private protected static synchronized Intent createUpdaterIntent(String updaterPackageName) {
        Intent intent = new Intent(ACTION_TRIGGER_RULES_UPDATE_CHECK);
        intent.setPackage(updaterPackageName);
        intent.setFlags(32);
        return intent;
    }

    private protected static synchronized void sendBroadcast(Context context, String updaterAppPackageName, byte[] checkTokenBytes) {
        Intent intent = createUpdaterIntent(updaterAppPackageName);
        intent.putExtra(EXTRA_CHECK_TOKEN, checkTokenBytes);
        context.sendBroadcastAsUser(intent, UserHandle.SYSTEM, "android.permission.UPDATE_TIME_ZONE_RULES");
    }
}
