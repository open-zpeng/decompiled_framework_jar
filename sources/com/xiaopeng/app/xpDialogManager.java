package com.xiaopeng.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import com.xiaopeng.view.xpWindowManager;
import java.util.List;
/* loaded from: classes3.dex */
public class xpDialogManager {
    private static final String TAG = "xpDialogManager";

    public static boolean isDialogValid(int type, String packageName) {
        boolean isValid = true;
        StringBuffer buffer = new StringBuffer("");
        buffer.append("isDialogValid");
        if (type == 2010 || type == 2047) {
            isValid = true;
        } else {
            try {
                List<ActivityManager.RunningTaskInfo> list = xpActivityManager.getActivityManager().getTasks(1);
                if (list != null && list.size() > 0) {
                    ComponentName component = list.get(0).topActivity;
                    String top = component.getPackageName();
                    boolean isToppingActivity = ActivityInfoManager.isToppingActivity(xpActivityInfo.create(component));
                    buffer.append(" top=" + top);
                    buffer.append(" packageName=" + packageName);
                    buffer.append(" isToppingActivity=" + isToppingActivity);
                    if (!TextUtils.isEmpty(top)) {
                        if (top.equals(packageName)) {
                            isValid = true;
                        }
                    }
                    if (isToppingActivity) {
                        isValid = false;
                    }
                }
            } catch (Exception e) {
            }
        }
        buffer.append(" isValid=" + isValid);
        return isValid;
    }

    public static boolean shouldDismissByPolicy(int type) {
        return isSystemDialog(type);
    }

    public static boolean waitActivityReadyIfNeed(final Dialog dialog) {
        Activity activity;
        View decor;
        if (dialog == null || dialog.getWindow() == null) {
            return false;
        }
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        int type = lp != null ? lp.type : 0;
        if (type != 12 || (activity = xpWindowManager.getDialogOwnerActivity(dialog)) == null) {
            return false;
        }
        try {
            if (activity.getWindow() == null || (decor = activity.getWindow().getDecorView()) == null || decor.isAttachedToWindow()) {
                return false;
            }
            new Handler().postDelayed(new Runnable() { // from class: com.xiaopeng.app.xpDialogManager.1
                @Override // java.lang.Runnable
                public void run() {
                    Dialog.this.show();
                }
            }, 50L);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void closeSystemDialogs() {
        try {
            xpActivityManager.getActivityManager().closeSystemDialogs(TAG);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static boolean isSystemDialog(int type) {
        if (type == 2038 || type == 2048) {
            return true;
        }
        switch (type) {
            case 2002:
            case 2003:
                return true;
            default:
                switch (type) {
                    case 2006:
                    case 2007:
                    case 2008:
                    case WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG /* 2009 */:
                        return true;
                    default:
                        return false;
                }
        }
    }

    public static xpDialogInfo getDialogRecorder() {
        return null;
    }

    public static void setDialogRecorder(xpDialogInfo info) {
        if (info == null) {
            return;
        }
        try {
            xpActivityManager.getActivityManager().setDialogRecorder(info);
        } catch (Exception e) {
        }
    }
}
