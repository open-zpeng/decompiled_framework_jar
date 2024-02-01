package android.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
/* loaded from: classes.dex */
public class LauncherActivityInfo {
    private static final String TAG = "LauncherActivityInfo";
    public protected ActivityInfo mActivityInfo;
    private ComponentName mComponentName;
    private final PackageManager mPm;
    private UserHandle mUser;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized LauncherActivityInfo(Context context, ActivityInfo info, UserHandle user) {
        this(context);
        this.mActivityInfo = info;
        this.mComponentName = new ComponentName(info.packageName, info.name);
        this.mUser = user;
    }

    synchronized LauncherActivityInfo(Context context) {
        this.mPm = context.getPackageManager();
    }

    public ComponentName getComponentName() {
        return this.mComponentName;
    }

    public UserHandle getUser() {
        return this.mUser;
    }

    public CharSequence getLabel() {
        return this.mActivityInfo.loadLabel(this.mPm);
    }

    public Drawable getIcon(int density) {
        Drawable overlayDrawable = getApplicationInfo().loadIcon(this.mPm);
        if (overlayDrawable != null) {
            return overlayDrawable;
        }
        int iconRes = this.mActivityInfo.getIconResource();
        Drawable icon = null;
        if (density != 0 && iconRes != 0) {
            try {
                Resources resources = this.mPm.getResourcesForApplication(this.mActivityInfo.applicationInfo);
                icon = resources.getDrawableForDensity(iconRes, density);
            } catch (PackageManager.NameNotFoundException | Resources.NotFoundException e) {
            }
        }
        if (icon == null) {
            return this.mActivityInfo.loadIcon(this.mPm);
        }
        return icon;
    }

    public synchronized int getApplicationFlags() {
        return this.mActivityInfo.applicationInfo.flags;
    }

    public ApplicationInfo getApplicationInfo() {
        return this.mActivityInfo.applicationInfo;
    }

    public long getFirstInstallTime() {
        try {
            return this.mPm.getPackageInfo(this.mActivityInfo.packageName, 8192).firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            return 0L;
        }
    }

    public String getName() {
        return this.mActivityInfo.name;
    }

    public Drawable getBadgedIcon(int density) {
        Drawable originalIcon = getIcon(density);
        return this.mPm.getUserBadgedIcon(originalIcon, this.mUser);
    }
}
