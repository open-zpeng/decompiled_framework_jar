package com.xiaopeng.app;

import android.app.Activity;
import android.app.ActivityThread;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.view.Window;
/* loaded from: classes3.dex */
public class xpActivityThreadProxy {
    private static final String TAG = "xpActivityThreadProxy";
    private boolean mResumeEventInjected = false;

    public void handleResumeActivity(ActivityThread.ActivityClientRecord r, Handler handler, String reason) {
        injectResumeEvent(r, handler);
    }

    public void performResumeActivity(ActivityThread.ActivityClientRecord r, Handler handler, boolean finalStateRequest, String reason) {
        injectResumeEvent(r, handler);
    }

    public void handlePauseActivity(ActivityThread.ActivityClientRecord r, boolean finished, boolean userLeaving, int configChanges, int resumingFlags, String reason) {
        this.mResumeEventInjected = false;
        if (r != null) {
            Activity activity = r.getActivity();
            boolean isHome = xpActivityManager.isHomeActivity(r);
            if (isHome && activity != null) {
                int type = xpActivityManager.getActivityWindowType(r);
                boolean visible = xpActivityManager.shouldVisibleWhenPaused(type, resumingFlags);
                activity.callOnPause(visible);
            }
        }
    }

    public void performPauseActivity(ActivityThread.ActivityClientRecord r, boolean finished, String reason) {
        this.mResumeEventInjected = false;
    }

    public void performPauseActivityIfNeeded(ActivityThread.ActivityClientRecord r, String reason) {
        this.mResumeEventInjected = false;
    }

    private boolean isResumeReady(ActivityThread.ActivityClientRecord r) {
        if (r == null) {
            return false;
        }
        ActivityInfo info = r.getActivityInfo();
        Window window = r.getWindow();
        Intent intent = r.getIntent();
        return (info == null || window == null || intent == null) ? false : true;
    }

    private void injectResumeEvent(ActivityThread.ActivityClientRecord r, Handler handler) {
        boolean isResumeReady = isResumeReady(r);
        if (isResumeReady && !this.mResumeEventInjected) {
            xpActivityManager.handleActivityChanged(r);
            xpActivityManager.finishMiniProgramIfNeed(r, handler);
            this.mResumeEventInjected = true;
        }
    }
}
