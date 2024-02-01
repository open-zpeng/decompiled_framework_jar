package com.xiaopeng.app;

import android.content.pm.IPackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
/* loaded from: classes3.dex */
public class xpPackageManager {
    private static xpPackageManager sPackageManager = null;
    private final IPackageManager mPM = getPackageManager();

    private xpPackageManager() {
    }

    public static xpPackageManager get() {
        if (sPackageManager == null) {
            synchronized (xpPackageManager.class) {
                if (sPackageManager == null) {
                    sPackageManager = new xpPackageManager();
                }
            }
        }
        return sPackageManager;
    }

    public static xpPackageInfo getOverridePackageInfo(String packageName) {
        IPackageManager pm = getPackageManager();
        if (pm != null && !TextUtils.isEmpty(packageName)) {
            try {
                return pm.getXpPackageInfo(packageName);
            } catch (RemoteException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static Bitmap getOverrideAppIcon(String pkgName) {
        IPackageManager pm = getPackageManager();
        if (pm != null && !TextUtils.isEmpty(pkgName)) {
            try {
                return pm.getXpAppIcon(pkgName);
            } catch (RemoteException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static IPackageManager getPackageManager() {
        IBinder b = ServiceManager.getService("package");
        return IPackageManager.Stub.asInterface(b);
    }
}
