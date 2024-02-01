package android.app;

import android.content.Intent;
import android.content.pm.IPackageInstallObserver2;
import android.os.Bundle;
/* loaded from: classes.dex */
public class PackageInstallObserver {
    private final IPackageInstallObserver2.Stub mBinder = new IPackageInstallObserver2.Stub() { // from class: android.app.PackageInstallObserver.1
        public void onUserActionRequired(Intent intent) {
            PackageInstallObserver.this.onUserActionRequired(intent);
        }

        public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) {
            PackageInstallObserver.this.onPackageInstalled(basePackageName, returnCode, msg, extras);
        }
    };

    private protected PackageInstallObserver() {
    }

    public synchronized IPackageInstallObserver2 getBinder() {
        return this.mBinder;
    }

    public synchronized void onUserActionRequired(Intent intent) {
    }

    private protected void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) {
    }
}
