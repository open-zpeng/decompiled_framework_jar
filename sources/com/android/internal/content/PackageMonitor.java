package com.android.internal.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.Preconditions;
import java.util.HashSet;
/* loaded from: classes3.dex */
public abstract class PackageMonitor extends BroadcastReceiver {
    public static final int PACKAGE_PERMANENT_CHANGE = 3;
    public static final int PACKAGE_TEMPORARY_CHANGE = 2;
    public static final int PACKAGE_UNCHANGED = 0;
    public static final int PACKAGE_UPDATING = 1;
    String[] mAppearingPackages;
    int mChangeType;
    String[] mDisappearingPackages;
    String[] mModifiedComponents;
    String[] mModifiedPackages;
    Context mRegisteredContext;
    Handler mRegisteredHandler;
    boolean mSomePackagesChanged;
    static final IntentFilter sPackageFilt = new IntentFilter();
    static final IntentFilter sNonDataFilt = new IntentFilter();
    static final IntentFilter sExternalFilt = new IntentFilter();
    final HashSet<String> mUpdatingPackages = new HashSet<>();
    int mChangeUserId = UserInfo.NO_PROFILE_GROUP_ID;
    String[] mTempArray = new String[1];

    static {
        sPackageFilt.addAction(Intent.ACTION_PACKAGE_ADDED);
        sPackageFilt.addAction(Intent.ACTION_PACKAGE_REMOVED);
        sPackageFilt.addAction(Intent.ACTION_PACKAGE_CHANGED);
        sPackageFilt.addAction(Intent.ACTION_QUERY_PACKAGE_RESTART);
        sPackageFilt.addAction(Intent.ACTION_PACKAGE_RESTARTED);
        sPackageFilt.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
        sPackageFilt.addDataScheme("package");
        sNonDataFilt.addAction(Intent.ACTION_UID_REMOVED);
        sNonDataFilt.addAction(Intent.ACTION_USER_STOPPED);
        sNonDataFilt.addAction(Intent.ACTION_PACKAGES_SUSPENDED);
        sNonDataFilt.addAction(Intent.ACTION_PACKAGES_UNSUSPENDED);
        sExternalFilt.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sExternalFilt.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void register(Context context, Looper thread, boolean externalStorage) {
        register(context, thread, (UserHandle) null, externalStorage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void register(Context context, Looper thread, UserHandle user, boolean externalStorage) {
        register(context, user, externalStorage, thread == null ? BackgroundThread.getHandler() : new Handler(thread));
    }

    public synchronized void register(Context context, UserHandle user, boolean externalStorage, Handler handler) {
        if (this.mRegisteredContext != null) {
            throw new IllegalStateException("Already registered");
        }
        this.mRegisteredContext = context;
        this.mRegisteredHandler = (Handler) Preconditions.checkNotNull(handler);
        if (user != null) {
            context.registerReceiverAsUser(this, user, sPackageFilt, null, this.mRegisteredHandler);
            context.registerReceiverAsUser(this, user, sNonDataFilt, null, this.mRegisteredHandler);
            if (externalStorage) {
                context.registerReceiverAsUser(this, user, sExternalFilt, null, this.mRegisteredHandler);
                return;
            }
            return;
        }
        context.registerReceiver(this, sPackageFilt, null, this.mRegisteredHandler);
        context.registerReceiver(this, sNonDataFilt, null, this.mRegisteredHandler);
        if (externalStorage) {
            context.registerReceiver(this, sExternalFilt, null, this.mRegisteredHandler);
        }
    }

    public synchronized Handler getRegisteredHandler() {
        return this.mRegisteredHandler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregister() {
        if (this.mRegisteredContext == null) {
            throw new IllegalStateException("Not registered");
        }
        this.mRegisteredContext.unregisterReceiver(this);
        this.mRegisteredContext = null;
    }

    synchronized boolean isPackageUpdating(String packageName) {
        boolean contains;
        synchronized (this.mUpdatingPackages) {
            contains = this.mUpdatingPackages.contains(packageName);
        }
        return contains;
    }

    public synchronized void onBeginPackageChanges() {
    }

    public synchronized void onPackageAdded(String packageName, int uid) {
    }

    private protected void onPackageRemoved(String packageName, int uid) {
    }

    public synchronized void onPackageRemovedAllUsers(String packageName, int uid) {
    }

    public synchronized void onPackageUpdateStarted(String packageName, int uid) {
    }

    public synchronized void onPackageUpdateFinished(String packageName, int uid) {
    }

    private protected boolean onPackageChanged(String packageName, int uid, String[] components) {
        if (components != null) {
            for (String name : components) {
                if (packageName.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized boolean onHandleForceStop(Intent intent, String[] packages, int uid, boolean doit) {
        return false;
    }

    public synchronized void onHandleUserStop(Intent intent, int userHandle) {
    }

    public synchronized void onUidRemoved(int uid) {
    }

    public synchronized void onPackagesAvailable(String[] packages) {
    }

    public synchronized void onPackagesUnavailable(String[] packages) {
    }

    public synchronized void onPackagesSuspended(String[] packages) {
    }

    public synchronized void onPackagesSuspended(String[] packages, Bundle launcherExtras) {
        onPackagesSuspended(packages);
    }

    public synchronized void onPackagesUnsuspended(String[] packages) {
    }

    public synchronized void onPackageDisappeared(String packageName, int reason) {
    }

    public synchronized void onPackageAppeared(String packageName, int reason) {
    }

    public synchronized void onPackageModified(String packageName) {
    }

    public synchronized boolean didSomePackagesChange() {
        return this.mSomePackagesChanged;
    }

    public synchronized int isPackageAppearing(String packageName) {
        if (this.mAppearingPackages != null) {
            for (int i = this.mAppearingPackages.length - 1; i >= 0; i--) {
                if (packageName.equals(this.mAppearingPackages[i])) {
                    return this.mChangeType;
                }
            }
            return 0;
        }
        return 0;
    }

    public synchronized boolean anyPackagesAppearing() {
        return this.mAppearingPackages != null;
    }

    private protected int isPackageDisappearing(String packageName) {
        if (this.mDisappearingPackages != null) {
            for (int i = this.mDisappearingPackages.length - 1; i >= 0; i--) {
                if (packageName.equals(this.mDisappearingPackages[i])) {
                    return this.mChangeType;
                }
            }
            return 0;
        }
        return 0;
    }

    public synchronized boolean anyPackagesDisappearing() {
        return this.mDisappearingPackages != null;
    }

    public synchronized boolean isReplacing() {
        return this.mChangeType == 1;
    }

    private protected boolean isPackageModified(String packageName) {
        if (this.mModifiedPackages != null) {
            for (int i = this.mModifiedPackages.length - 1; i >= 0; i--) {
                if (packageName.equals(this.mModifiedPackages[i])) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public synchronized boolean isComponentModified(String className) {
        if (className == null || this.mModifiedComponents == null) {
            return false;
        }
        for (int i = this.mModifiedComponents.length - 1; i >= 0; i--) {
            if (className.equals(this.mModifiedComponents[i])) {
                return true;
            }
        }
        return false;
    }

    public synchronized void onSomePackagesChanged() {
    }

    public synchronized void onFinishPackageChanges() {
    }

    public synchronized void onPackageDataCleared(String packageName, int uid) {
    }

    public synchronized int getChangingUserId() {
        return this.mChangeUserId;
    }

    synchronized String getPackageName(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            String pkg = uri.getSchemeSpecificPart();
            return pkg;
        }
        return null;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        this.mChangeUserId = intent.getIntExtra(Intent.EXTRA_USER_HANDLE, UserInfo.NO_PROFILE_GROUP_ID);
        if (this.mChangeUserId == -10000) {
            Slog.w("PackageMonitor", "Intent broadcast does not contain user handle: " + intent);
            return;
        }
        onBeginPackageChanges();
        this.mAppearingPackages = null;
        this.mDisappearingPackages = null;
        int i = 0;
        this.mSomePackagesChanged = false;
        this.mModifiedComponents = null;
        String action = intent.getAction();
        if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
            String pkg = getPackageName(intent);
            int uid = intent.getIntExtra(Intent.EXTRA_UID, 0);
            this.mSomePackagesChanged = true;
            if (pkg != null) {
                this.mAppearingPackages = this.mTempArray;
                this.mTempArray[0] = pkg;
                if (intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {
                    this.mModifiedPackages = this.mTempArray;
                    this.mChangeType = 1;
                    onPackageUpdateFinished(pkg, uid);
                    onPackageModified(pkg);
                } else {
                    this.mChangeType = 3;
                    onPackageAdded(pkg, uid);
                }
                onPackageAppeared(pkg, this.mChangeType);
                if (this.mChangeType == 1) {
                    synchronized (this.mUpdatingPackages) {
                        this.mUpdatingPackages.remove(pkg);
                    }
                }
            }
        } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
            String pkg2 = getPackageName(intent);
            int uid2 = intent.getIntExtra(Intent.EXTRA_UID, 0);
            if (pkg2 != null) {
                this.mDisappearingPackages = this.mTempArray;
                this.mTempArray[0] = pkg2;
                if (intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {
                    this.mChangeType = 1;
                    synchronized (this.mUpdatingPackages) {
                    }
                    onPackageUpdateStarted(pkg2, uid2);
                } else {
                    this.mChangeType = 3;
                    this.mSomePackagesChanged = true;
                    onPackageRemoved(pkg2, uid2);
                    if (intent.getBooleanExtra(Intent.EXTRA_REMOVED_FOR_ALL_USERS, false)) {
                        onPackageRemovedAllUsers(pkg2, uid2);
                    }
                }
                onPackageDisappeared(pkg2, this.mChangeType);
            }
        } else if (Intent.ACTION_PACKAGE_CHANGED.equals(action)) {
            String pkg3 = getPackageName(intent);
            int uid3 = intent.getIntExtra(Intent.EXTRA_UID, 0);
            this.mModifiedComponents = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST);
            if (pkg3 != null) {
                this.mModifiedPackages = this.mTempArray;
                this.mTempArray[0] = pkg3;
                this.mChangeType = 3;
                if (onPackageChanged(pkg3, uid3, this.mModifiedComponents)) {
                    this.mSomePackagesChanged = true;
                }
                onPackageModified(pkg3);
            }
        } else if (Intent.ACTION_PACKAGE_DATA_CLEARED.equals(action)) {
            String pkg4 = getPackageName(intent);
            int uid4 = intent.getIntExtra(Intent.EXTRA_UID, 0);
            if (pkg4 != null) {
                onPackageDataCleared(pkg4, uid4);
            }
        } else {
            if (Intent.ACTION_QUERY_PACKAGE_RESTART.equals(action)) {
                this.mDisappearingPackages = intent.getStringArrayExtra(Intent.EXTRA_PACKAGES);
                this.mChangeType = 2;
                boolean canRestart = onHandleForceStop(intent, this.mDisappearingPackages, intent.getIntExtra(Intent.EXTRA_UID, 0), false);
                if (canRestart) {
                    setResultCode(-1);
                }
            } else if (Intent.ACTION_PACKAGE_RESTARTED.equals(action)) {
                this.mDisappearingPackages = new String[]{getPackageName(intent)};
                this.mChangeType = 2;
                onHandleForceStop(intent, this.mDisappearingPackages, intent.getIntExtra(Intent.EXTRA_UID, 0), true);
            } else if (Intent.ACTION_UID_REMOVED.equals(action)) {
                onUidRemoved(intent.getIntExtra(Intent.EXTRA_UID, 0));
            } else if (Intent.ACTION_USER_STOPPED.equals(action)) {
                if (intent.hasExtra(Intent.EXTRA_USER_HANDLE)) {
                    onHandleUserStop(intent, intent.getIntExtra(Intent.EXTRA_USER_HANDLE, 0));
                }
            } else if (Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE.equals(action)) {
                String[] pkgList = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
                this.mAppearingPackages = pkgList;
                this.mChangeType = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false) ? 1 : 2;
                this.mSomePackagesChanged = true;
                if (pkgList != null) {
                    onPackagesAvailable(pkgList);
                    while (i < pkgList.length) {
                        onPackageAppeared(pkgList[i], this.mChangeType);
                        i++;
                    }
                }
            } else if (Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE.equals(action)) {
                String[] pkgList2 = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
                this.mDisappearingPackages = pkgList2;
                this.mChangeType = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false) ? 1 : 2;
                this.mSomePackagesChanged = true;
                if (pkgList2 != null) {
                    onPackagesUnavailable(pkgList2);
                    while (i < pkgList2.length) {
                        onPackageDisappeared(pkgList2[i], this.mChangeType);
                        i++;
                    }
                }
            } else if (Intent.ACTION_PACKAGES_SUSPENDED.equals(action)) {
                String[] pkgList3 = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
                Bundle launcherExtras = intent.getBundleExtra(Intent.EXTRA_LAUNCHER_EXTRAS);
                this.mSomePackagesChanged = true;
                onPackagesSuspended(pkgList3, launcherExtras);
            } else if (Intent.ACTION_PACKAGES_UNSUSPENDED.equals(action)) {
                String[] pkgList4 = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_PACKAGE_LIST);
                this.mSomePackagesChanged = true;
                onPackagesUnsuspended(pkgList4);
            }
        }
        if (this.mSomePackagesChanged) {
            onSomePackagesChanged();
        }
        onFinishPackageChanges();
        this.mChangeUserId = UserInfo.NO_PROFILE_GROUP_ID;
    }
}
