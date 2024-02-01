package com.android.internal.app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.service.notification.ZenModeConfig;
import android.util.Slog;
import com.android.internal.R;
import com.android.internal.app.AlertController;
/* loaded from: classes3.dex */
public class SuspendedAppActivity extends AlertActivity implements DialogInterface.OnClickListener {
    public static final String EXTRA_DIALOG_MESSAGE = "SuspendedAppActivity.extra.DIALOG_MESSAGE";
    public static final String EXTRA_SUSPENDED_PACKAGE = "SuspendedAppActivity.extra.SUSPENDED_PACKAGE";
    public static final String EXTRA_SUSPENDING_PACKAGE = "SuspendedAppActivity.extra.SUSPENDING_PACKAGE";
    private static final String TAG = "SuspendedAppActivity";
    private Intent mMoreDetailsIntent;
    private PackageManager mPm;
    private int mUserId;

    private CharSequence getAppLabel(String packageName) {
        try {
            return this.mPm.getApplicationInfoAsUser(packageName, 0, this.mUserId).loadLabel(this.mPm);
        } catch (PackageManager.NameNotFoundException ne) {
            Slog.e(TAG, "Package " + packageName + " not found", ne);
            return packageName;
        }
    }

    private Intent getMoreDetailsActivity(String suspendingPackage, String suspendedPackage, int userId) {
        Intent moreDetailsIntent = new Intent(Intent.ACTION_SHOW_SUSPENDED_APP_DETAILS).setPackage(suspendingPackage);
        ResolveInfo resolvedInfo = this.mPm.resolveActivityAsUser(moreDetailsIntent, 0, userId);
        if (resolvedInfo != null && resolvedInfo.activityInfo != null && Manifest.permission.SEND_SHOW_SUSPENDED_APP_DETAILS.equals(resolvedInfo.activityInfo.permission)) {
            moreDetailsIntent.putExtra(Intent.EXTRA_PACKAGE_NAME, suspendedPackage).setFlags(335544320);
            return moreDetailsIntent;
        }
        return null;
    }

    @Override // com.android.internal.app.AlertActivity, android.app.Activity
    public void onCreate(Bundle icicle) {
        CharSequence dialogMessage;
        super.onCreate(icicle);
        this.mPm = getPackageManager();
        getWindow().setType(2008);
        Intent intent = getIntent();
        this.mUserId = intent.getIntExtra(Intent.EXTRA_USER_ID, -1);
        if (this.mUserId < 0) {
            Slog.wtf(TAG, "Invalid user: " + this.mUserId);
            finish();
            return;
        }
        String suppliedMessage = intent.getStringExtra(EXTRA_DIALOG_MESSAGE);
        String suspendedPackage = intent.getStringExtra(EXTRA_SUSPENDED_PACKAGE);
        String suspendingPackage = intent.getStringExtra(EXTRA_SUSPENDING_PACKAGE);
        CharSequence suspendedAppLabel = getAppLabel(suspendedPackage);
        if (suppliedMessage == null) {
            dialogMessage = getString(R.string.app_suspended_default_message, suspendedAppLabel, getAppLabel(suspendingPackage));
        } else {
            dialogMessage = String.format(getResources().getConfiguration().getLocales().get(0), suppliedMessage, suspendedAppLabel);
        }
        AlertController.AlertParams ap = this.mAlertParams;
        ap.mTitle = getString(R.string.app_suspended_title);
        ap.mMessage = dialogMessage;
        ap.mPositiveButtonText = getString(android.R.string.ok);
        this.mMoreDetailsIntent = getMoreDetailsActivity(suspendingPackage, suspendedPackage, this.mUserId);
        if (this.mMoreDetailsIntent != null) {
            ap.mNeutralButtonText = getString(R.string.app_suspended_more_details);
        }
        ap.mNeutralButtonListener = this;
        ap.mPositiveButtonListener = this;
        setupAlert();
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        if (which == -3) {
            startActivityAsUser(this.mMoreDetailsIntent, UserHandle.of(this.mUserId));
            Slog.i(TAG, "Started more details activity");
        }
        finish();
    }

    public static Intent createSuspendedAppInterceptIntent(String suspendedPackage, String suspendingPackage, String dialogMessage, int userId) {
        return new Intent().setClassName(ZenModeConfig.SYSTEM_AUTHORITY, SuspendedAppActivity.class.getName()).putExtra(EXTRA_SUSPENDED_PACKAGE, suspendedPackage).putExtra(EXTRA_DIALOG_MESSAGE, dialogMessage).putExtra(EXTRA_SUSPENDING_PACKAGE, suspendingPackage).putExtra(Intent.EXTRA_USER_ID, userId).setFlags(276824064);
    }
}
