package com.xiaopeng.app;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.WindowManager;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.util.xpLogger;
import com.xiaopeng.view.SharedDisplayManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes3.dex */
public class ActivityInfoManager {
    public static final String ACTIVITY_FLAGS = "com.xiaopeng.metadata.activity.flags";
    public static final String ACTIVITY_LEVEL = "com.xiaopeng.metadata.activity.level";
    public static final String CATEGORY_SECONDARY_HOME = "android.intent.category.SECONDARY_HOME";
    public static final int FLAG_ACTIVITY_LAUNCH_APPLET = 512;
    public static final int FLAG_ACTIVITY_LAUNCH_AUTOMATIC = 134217728;
    public static final int FLAG_ACTIVITY_LAUNCH_DEFAULT = Integer.MIN_VALUE;
    public static final int FLAG_ACTIVITY_LAUNCH_DIALOG = 128;
    public static final int FLAG_ACTIVITY_LAUNCH_FULLSCREEN = 64;
    public static final int FLAG_ACTIVITY_LAUNCH_HOME = 536870912;
    public static final int FLAG_ACTIVITY_LAUNCH_NO_LIMITED = 1024;
    public static final int FLAG_ACTIVITY_LAUNCH_PANEL = 256;
    public static final int FLAG_ACTIVITY_LAUNCH_PHYSICAL = 32;
    public static final int FLAG_ACTIVITY_LAUNCH_SECONDARY = 268435456;
    public static final int FLAG_ACTIVITY_LAUNCH_SHARED = 1024;
    public static final int FLAG_ACTIVITY_LAUNCH_SYSTEM = 1073741824;
    public static final int FLAG_ACTIVITY_LAUNCH_TOPPING = 16;
    public static final int FLAG_ACTIVITY_TASK_OFF_HOME = 512;
    private static final String TAG = "ActivityInfoManager";
    private static final boolean LIST_ENABLED = SystemProperties.getBoolean("persist.xp.am.activity.info.list.enable", true);
    private static final ArrayList<String> sPanelList = new ArrayList<>();
    private static final ArrayList<String> sDialogList = new ArrayList<>();
    private static final ArrayList<String> sToppingList = new ArrayList<>();
    private static final ArrayList<String> sFullscreenList = new ArrayList<>();
    private static final List<String> sPhysicalFullscreenList = getPhysicalFullscreenPackages();

    public static Intent appendIntentExtra(Context context, xpActivityInfo activityInfo) {
        if (activityInfo == null || activityInfo.intent == null) {
            return null;
        }
        String pkg = "";
        String cls = "";
        if (notEmpty(activityInfo.packageName) && notEmpty(activityInfo.className)) {
            pkg = activityInfo.packageName;
            cls = activityInfo.className;
        } else {
            ComponentName component = activityInfo.intent.getComponent();
            if (component != null && notEmpty(component.getPackageName()) && notEmpty(component.getClassName())) {
                pkg = component.getPackageName();
                cls = component.getClassName();
            }
        }
        int flags = 0;
        boolean isSystem = isSystemApplication(pkg);
        boolean physicalFullscreen = isPhysicalFullscreen(pkg);
        if (isSystem) {
            int flags2 = getActivityFlags(activityInfo);
            int level = getActivityLevel(activityInfo);
            if (flags2 == 0) {
                flags2 = getActivityFlagsByList(pkg, cls);
            }
            if (flags2 == 0) {
                boolean isHome = isDefaultHome(context, activityInfo);
                boolean isSecondaryHome = isSecondaryHome(null, activityInfo);
                boolean isApplet = isAppletActivity(activityInfo);
                boolean isShared = isSharedActivity(activityInfo);
                if (isHome) {
                    flags2 |= 536870912;
                } else if (isSecondaryHome) {
                    flags2 |= 268435456;
                } else if (isApplet) {
                    flags2 |= 512;
                } else if (isShared) {
                    flags2 |= 1024;
                } else {
                    flags2 |= Integer.MIN_VALUE;
                }
            }
            if (level > 0) {
                flags2 |= 16;
            }
            flags = flags2 | 1073741824;
        } else {
            try {
                xpPackageInfo pi = AppGlobals.getPackageManager().getXpPackageInfo(pkg);
                flags = 0 | getActivityFlagsByJson(pi);
            } catch (Exception e) {
            }
        }
        boolean hasPhysicalFlags = SharedDisplayManager.isPhysicalActivity(flags);
        if (physicalFullscreen || hasPhysicalFlags) {
            flags = flags | 32 | 64;
        }
        if (shouldTaskOnHome(flags) && !hasIntentFlag(512, activityInfo.intent)) {
            activityInfo.intent.addFlags(16384);
        }
        if ((flags & 536870912) == 536870912) {
            activityInfo.intent = getOverrideHomeIntent(activityInfo.intent);
        }
        activityInfo.intent.setPrivateFlags(flags);
        xpLogger.i(TAG, "appendIntentExtra flags=0x" + Integer.toHexString(flags) + " info=" + activityInfo.toString());
        return activityInfo.intent;
    }

    public static int getActivityFlagsByList(String packageName, String className) {
        int flags = 0;
        if (!LIST_ENABLED || !notEmpty(packageName) || !notEmpty(className)) {
            return 0;
        }
        ComponentName component = ComponentName.createRelative(packageName, className);
        String var = component.flattenToString();
        if (sPanelList.contains(var)) {
            flags = 0 | 256;
        }
        if (sDialogList.contains(var)) {
            flags |= 128;
        }
        if (sToppingList.contains(var)) {
            flags |= 16;
        }
        if (sFullscreenList.contains(var)) {
            return flags | 64;
        }
        return flags;
    }

    public static int getActivityFlagsByJson(xpPackageInfo pi) {
        int flags = 0;
        if (pi == null) {
            return 0;
        }
        boolean hideInfoFlow = false;
        boolean hideStatusBar = false;
        boolean hideNavigationBar = false;
        int screenFlag = pi.screenFlag;
        if ((screenFlag & 1) == 1) {
            hideStatusBar = true;
        }
        if ((screenFlag & 2) == 2) {
            hideInfoFlow = true;
        }
        if ((screenFlag & 4) == 4) {
            hideNavigationBar = true;
        }
        if (hideInfoFlow && hideStatusBar && hideNavigationBar) {
            flags = 0 | 64;
            if (pi.screenAdaption == 1) {
                flags |= 134217728;
            }
        }
        if ((screenFlag & 8) == 8) {
            return flags | 128;
        }
        return flags;
    }

    public static final Intent getOverrideHomeIntent(Intent intent) {
        if (intent != null) {
            try {
                Set<String> categories = intent.getCategories();
                if (categories != null && categories.size() > 0) {
                    for (String c : categories) {
                        intent.removeCategory(c);
                    }
                }
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setType(null);
                intent.setData(null);
                return intent;
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static final boolean hasIntentFlag(int flag, Intent intent) {
        return intent != null && (intent.getFlags() & flag) == flag;
    }

    public static boolean isHomeIntent(Intent intent) {
        return HomeInfo.isHomeIntent(intent);
    }

    public static boolean isHomeActivity(WindowManager.LayoutParams lp) {
        if (lp == null) {
            return false;
        }
        if (lp.type == 5) {
            return true;
        }
        int intentFlags = lp.intentFlags;
        return (intentFlags & 536870912) == 536870912;
    }

    public static boolean isDefaultHome(Context context, xpActivityInfo ai) {
        return HomeInfo.isHome(context, ai != null ? ai.packageName : "", ai != null ? ai.className : "", true);
    }

    public static boolean isHome(Context context, xpActivityInfo ai) {
        return HomeInfo.isHome(context, ai != null ? ai.packageName : "", ai != null ? ai.className : "", false);
    }

    public static boolean hasHomeFlags(int privateFlags) {
        boolean isHome = (privateFlags & 536870912) == 536870912;
        boolean isSecondaryHome = (privateFlags & 268435456) == 268435456;
        return isHome || isSecondaryHome;
    }

    public static boolean isSecondaryHome(Context context, xpActivityInfo activityInfo) {
        if (activityInfo == null) {
            return false;
        }
        String packageName = activityInfo.packageName;
        String className = activityInfo.className;
        Intent intent = activityInfo.intent;
        if (intent != null) {
            if (!Intent.ACTION_MAIN.equals(intent.getAction()) || !intent.hasCategory("android.intent.category.SECONDARY_HOME") || intent.getCategories().size() != 1 || intent.getData() != null || intent.getType() != null) {
                return false;
            }
            return true;
        } else if (!notEmpty(packageName) || !notEmpty(className)) {
            return false;
        } else {
            String secondaryComponent = FeatureOption.FO_SECONDARY_HOME_COMPONENT;
            ComponentName component = notEmpty(secondaryComponent) ? ComponentName.unflattenFromString(secondaryComponent) : null;
            if (component == null || !equals(packageName, component.getPackageName()) || !equals(className, component.getClassName())) {
                return false;
            }
            return true;
        }
    }

    public static boolean isToppingActivity(xpActivityInfo activityInfo) {
        return getActivityLevel(activityInfo) > 0;
    }

    public static boolean isSharedActivity(xpActivityInfo activityInfo) {
        return SharedDisplayManager.isSharedApplication(activityInfo);
    }

    public static boolean isAppletActivity(xpActivityInfo activityInfo) {
        return MiniProgramManager.isMiniProgram(activityInfo);
    }

    public static boolean isFreeformActivity(xpActivityInfo activityInfo) {
        return isSharedActivity(activityInfo) || isAppletActivity(activityInfo);
    }

    public static boolean shouldTaskOnHome(int flags) {
        if ((flags & 256) == 256 || (flags & 128) == 128 || (flags & 512) == 512 || (flags & 1024) == 1024) {
            return false;
        }
        if ((flags & 1073741824) == 1073741824) {
            return ((flags & 64) == 64 || (flags & 16) == 16) ? false : true;
        }
        return true;
    }

    public static boolean isSystemApplication(String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            if (packageName.startsWith("android") || packageName.startsWith("com.xpeng") || packageName.startsWith("com.xiaopeng") || MiniProgramManager.isMiniProgram(xpActivityInfo.create(packageName, null))) {
                return true;
            }
            try {
                PackageInfo pi = AppGlobals.getPackageManager().getPackageInfo(packageName, 0, UserHandle.myUserId());
                if (!isSystemApplication(pi)) {
                    if (!isSystemUpdateApplication(pi)) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    public static boolean isSystemApplication(PackageInfo info) {
        return (info == null || (info.applicationInfo.flags & 1) == 0) ? false : true;
    }

    public static boolean isSystemUpdateApplication(PackageInfo info) {
        return (info == null || (info.applicationInfo.flags & 128) == 0) ? false : true;
    }

    public static boolean isUserApplication(PackageInfo pInfo) {
        return (isSystemApplication(pInfo) || isSystemUpdateApplication(pInfo)) ? false : true;
    }

    private static boolean isPhysicalFullscreen(String packageName) {
        if (sPhysicalFullscreenList != null && !TextUtils.isEmpty(packageName)) {
            return sPhysicalFullscreenList.contains(packageName);
        }
        return false;
    }

    private static List<String> getPhysicalFullscreenPackages() {
        String[] packages;
        try {
            String content = SystemProperties.get("persist.xp.am.ai.physical_fullscreen_packages", "");
            if (!TextUtils.isEmpty(content) && (packages = content.split(";")) != null) {
                return Arrays.asList(packages);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static int getActivityFlags(Intent intent) {
        if (intent != null) {
            return intent.getPrivateFlags();
        }
        return 0;
    }

    public static int getActivityFlags(xpActivityInfo activityInfo) {
        if (activityInfo == null) {
            return 0;
        }
        int flags = getActivityFlags(activityInfo.intent);
        return flags == 0 ? getIntMetadata(ACTIVITY_FLAGS, activityInfo.packageName, activityInfo.className) : flags;
    }

    public static int getActivityLevel(xpActivityInfo activityInfo) {
        if (activityInfo != null) {
            if (LIST_ENABLED) {
                ComponentName component = null;
                if (notEmpty(activityInfo.packageName) && notEmpty(activityInfo.className)) {
                    component = ComponentName.createRelative(activityInfo.packageName, activityInfo.className);
                }
                if (activityInfo.intent != null && activityInfo.intent.getComponent() != null) {
                    component = activityInfo.intent.getComponent();
                }
                String var = component.flattenToString();
                if (notEmpty(var) && sToppingList.contains(var)) {
                    return 1;
                }
            }
            return getIntMetadata(ACTIVITY_LEVEL, activityInfo.packageName, activityInfo.className);
        }
        return 0;
    }

    private static int getIntMetadata(String key, String packageName, String className) {
        ComponentName component;
        ActivityInfo ai;
        try {
            if (notEmpty(key) && notEmpty(packageName) && notEmpty(className) && (component = ComponentName.createRelative(packageName, className)) != null && (ai = AppGlobals.getPackageManager().getActivityInfo(component, 128, UserHandle.myUserId())) != null && ai.applicationInfo != null && ai.metaData != null) {
                return ai.metaData.getInt(key, 0);
            }
        } catch (Exception e) {
        }
        return 0;
    }

    private static boolean equals(String a, String b) {
        return TextUtils.equals(a, b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean notEmpty(String var) {
        return !TextUtils.isEmpty(var);
    }

    /* loaded from: classes3.dex */
    public static final class HomeInfo {
        public ComponentName component;
        public int priority;
        public boolean setupWizard;

        public static boolean isHomeIntent(Intent intent) {
            return intent != null && Intent.ACTION_MAIN.equals(intent.getAction()) && intent.hasCategory(Intent.CATEGORY_HOME) && intent.getCategories().size() == 1 && intent.getData() == null && intent.getType() == null;
        }

        public static boolean isHome(Context context, String packageName, String className, boolean defaultOnly) {
            ComponentName c;
            if (ActivityInfoManager.notEmpty(packageName) && ActivityInfoManager.notEmpty(className)) {
                try {
                    if (defaultOnly) {
                        ComponentName c2 = findDefaultHome(context);
                        return c2 != null && packageName.equals(c2.getPackageName()) && className.equals(c2.getClassName());
                    }
                    ArrayList<HomeInfo> list = findHome(context);
                    if (list != null && !list.isEmpty()) {
                        Iterator<HomeInfo> it = list.iterator();
                        while (it.hasNext()) {
                            HomeInfo info = it.next();
                            if (info != null && (c = info.component) != null && c != null && packageName.equals(c.getPackageName()) && className.equals(c.getClassName())) {
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
            return false;
        }

        public static ArrayList<HomeInfo> findHome(Context context) {
            try {
                if (!UserManager.get(context).isUserUnlocked()) {
                    return null;
                }
                ArrayList<HomeInfo> list = new ArrayList<>();
                PackageManager pm = context.getPackageManager();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                List<ResolveInfo> resolveInfo = pm.queryIntentActivities(intent, 131168);
                Iterator<ResolveInfo> it = resolveInfo.iterator();
                while (it.hasNext()) {
                    ResolveInfo ri = it.next();
                    boolean z = true;
                    boolean enabled = (ri == null || ri.activityInfo == null || !ri.activityInfo.enabled) ? false : true;
                    if (enabled) {
                        HomeInfo info = new HomeInfo();
                        info.component = ri.activityInfo.getComponentName();
                        info.priority = ri.priority;
                        if (ri.filter == null || !ri.filter.hasCategory(Intent.CATEGORY_SETUP_WIZARD)) {
                            z = false;
                        }
                        info.setupWizard = z;
                        list.add(info);
                    }
                }
                return list;
            } catch (Exception e) {
                return null;
            }
        }

        public static ComponentName findDefaultHome(Context context) {
            int i;
            try {
                ArrayList<HomeInfo> list = findHome(context);
                if (list != null && !list.isEmpty()) {
                    int priority = -1000;
                    ComponentName component = null;
                    Iterator<HomeInfo> it = list.iterator();
                    while (it.hasNext()) {
                        HomeInfo info = it.next();
                        if (info != null && !info.setupWizard && (i = info.priority) > priority) {
                            priority = i;
                            component = info.component;
                        }
                    }
                    return component;
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
