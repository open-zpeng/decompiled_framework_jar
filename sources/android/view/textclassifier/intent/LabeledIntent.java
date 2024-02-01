package android.view.textclassifier.intent;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.textclassifier.ExtrasUtils;
import android.view.textclassifier.Log;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassifier;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: classes3.dex */
public final class LabeledIntent {
    public static final int DEFAULT_REQUEST_CODE = 0;
    private static final TitleChooser DEFAULT_TITLE_CHOOSER = new TitleChooser() { // from class: android.view.textclassifier.intent.-$$Lambda$LabeledIntent$LaL7EfxShgNu4lrdo3mv85g49Jg
        @Override // android.view.textclassifier.intent.LabeledIntent.TitleChooser
        public final CharSequence chooseTitle(LabeledIntent labeledIntent, ResolveInfo resolveInfo) {
            return LabeledIntent.lambda$static$0(labeledIntent, resolveInfo);
        }
    };
    private static final String TAG = "LabeledIntent";
    public final String description;
    public final String descriptionWithAppName;
    public final Intent intent;
    public final int requestCode;
    public final String titleWithEntity;
    public final String titleWithoutEntity;

    /* loaded from: classes3.dex */
    public interface TitleChooser {
        CharSequence chooseTitle(LabeledIntent labeledIntent, ResolveInfo resolveInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ CharSequence lambda$static$0(LabeledIntent labeledIntent, ResolveInfo resolveInfo) {
        if (!TextUtils.isEmpty(labeledIntent.titleWithEntity)) {
            return labeledIntent.titleWithEntity;
        }
        return labeledIntent.titleWithoutEntity;
    }

    public LabeledIntent(String titleWithoutEntity, String titleWithEntity, String description, String descriptionWithAppName, Intent intent, int requestCode) {
        if (TextUtils.isEmpty(titleWithEntity) && TextUtils.isEmpty(titleWithoutEntity)) {
            throw new IllegalArgumentException("titleWithEntity and titleWithoutEntity should not be both null");
        }
        this.titleWithoutEntity = titleWithoutEntity;
        this.titleWithEntity = titleWithEntity;
        this.description = (String) Preconditions.checkNotNull(description);
        this.descriptionWithAppName = descriptionWithAppName;
        this.intent = (Intent) Preconditions.checkNotNull(intent);
        this.requestCode = requestCode;
    }

    public Result resolve(Context context, TitleChooser titleChooser, Bundle textLanguagesBundle) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(this.intent, 0);
        if (resolveInfo == null || resolveInfo.activityInfo == null) {
            Log.w(TAG, "resolveInfo or activityInfo is null");
            return null;
        }
        String packageName = resolveInfo.activityInfo.packageName;
        String className = resolveInfo.activityInfo.name;
        if (packageName == null || className == null) {
            Log.w(TAG, "packageName or className is null");
            return null;
        }
        Intent resolvedIntent = new Intent(this.intent);
        resolvedIntent.putExtra(TextClassifier.EXTRA_FROM_TEXT_CLASSIFIER, getFromTextClassifierExtra(textLanguagesBundle));
        boolean shouldShowIcon = false;
        Icon icon = null;
        if (!"android".equals(packageName)) {
            resolvedIntent.setComponent(new ComponentName(packageName, className));
            if (resolveInfo.activityInfo.getIconResource() != 0) {
                icon = Icon.createWithResource(packageName, resolveInfo.activityInfo.getIconResource());
                shouldShowIcon = true;
            }
        }
        if (icon == null) {
            icon = Icon.createWithResource("android", (int) R.drawable.ic_more_items);
        }
        PendingIntent pendingIntent = TextClassification.createPendingIntent(context, resolvedIntent, this.requestCode);
        CharSequence title = (titleChooser == null ? DEFAULT_TITLE_CHOOSER : titleChooser).chooseTitle(this, resolveInfo);
        if (TextUtils.isEmpty(title)) {
            Log.w(TAG, "Custom titleChooser return null, fallback to the default titleChooser");
            title = DEFAULT_TITLE_CHOOSER.chooseTitle(this, resolveInfo);
        }
        RemoteAction action = new RemoteAction(icon, title, resolveDescription(resolveInfo, pm), pendingIntent);
        action.setShouldShowIcon(shouldShowIcon);
        return new Result(resolvedIntent, action);
    }

    private String resolveDescription(ResolveInfo resolveInfo, PackageManager packageManager) {
        if (!TextUtils.isEmpty(this.descriptionWithAppName)) {
            String applicationName = getApplicationName(resolveInfo, packageManager);
            if (!TextUtils.isEmpty(applicationName)) {
                return String.format(this.descriptionWithAppName, applicationName);
            }
        }
        return this.description;
    }

    private String getApplicationName(ResolveInfo resolveInfo, PackageManager packageManager) {
        if (resolveInfo.activityInfo == null || "android".equals(resolveInfo.activityInfo.packageName) || resolveInfo.activityInfo.applicationInfo == null) {
            return null;
        }
        return (String) packageManager.getApplicationLabel(resolveInfo.activityInfo.applicationInfo);
    }

    private Bundle getFromTextClassifierExtra(Bundle textLanguagesBundle) {
        if (textLanguagesBundle != null) {
            Bundle bundle = new Bundle();
            ExtrasUtils.putTextLanguagesExtra(bundle, textLanguagesBundle);
            return bundle;
        }
        return Bundle.EMPTY;
    }

    /* loaded from: classes3.dex */
    public static final class Result {
        public final RemoteAction remoteAction;
        public final Intent resolvedIntent;

        public Result(Intent resolvedIntent, RemoteAction remoteAction) {
            this.resolvedIntent = (Intent) Preconditions.checkNotNull(resolvedIntent);
            this.remoteAction = (RemoteAction) Preconditions.checkNotNull(remoteAction);
        }
    }
}
