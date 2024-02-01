package com.xiaopeng.view;

import android.app.Activity;
import android.app.ActivityThread;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.IPackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.IWindowManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.xiaopeng.app.ActivityInfoManager;
import com.xiaopeng.app.MiniProgramManager;
import com.xiaopeng.app.xpActivityManager;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.util.xpLogger;
/* loaded from: classes3.dex */
public class xpWindowManager {
    public static final boolean CFG_NAVIGATION_INPUT_CONSUMER_ENABLED = false;
    public static final boolean CFG_REQUEST_TRANSIENT_BAR_SUPPORT = false;
    public static final boolean CFG_WINDOW_ANIMATION_ENABLED = false;
    private static final boolean DEBUG = false;
    private static final String DEVICE = SystemProperties.get("ro.product.device", "");
    private static final String[] SKIP_SYSTEMUI_VISIBILITY_PROCESS = {"com.xiaopeng.carcontrol:unity"};
    private static final String TAG = "xpWindowManager";
    public static final long TOAST_DURATION_LONG = 4000;
    public static final long TOAST_DURATION_LONGER = 6000;
    public static final long TOAST_DURATION_SHORT = 2500;

    /* loaded from: classes3.dex */
    public static final class WindowErrorInfo {
        public String className;
        public String errorType;
        public String packageName;
    }

    /* loaded from: classes3.dex */
    public static final class WindowInfo {
        public Rect dimmerBounds;
        public Rect rect;
        public int[] roundCorner;
        public Rect touchRegion;
        public int windowType = 0;
        public int parentType = 0;
    }

    public static WindowManager.LayoutParams getOverrideLayoutParams(WindowManager.LayoutParams lp, View view, IWindowManager wm) {
        if (lp != null && view != null && wm != null) {
            try {
                String packageName = view.getContext().getBasePackageName();
                int systemUiVisibility = lp.systemUiVisibility | lp.subtreeSystemUiVisibility;
                boolean fullscreen = isFullscreen(systemUiVisibility, lp.flags, lp.xpFlags);
                int i = 0;
                boolean fullscreenType = lp.type == 6;
                lp.type = getOverrideWindowType(lp.type, packageName);
                WindowManager.LayoutParams nlp = wm.getXuiLayoutParams(lp);
                Configuration config = wm.getXuiConfiguration(packageName);
                if (lp.x == -1) {
                    lp.x = nlp != null ? nlp.x : 0;
                }
                if (lp.y == -1) {
                    lp.y = nlp != null ? nlp.y : 0;
                }
                lp.x = fullscreen ? 0 : lp.x;
                if (!fullscreen) {
                    i = lp.y;
                }
                lp.y = i;
                switch (lp.width) {
                    case -1:
                        lp.width = (nlp == null || fullscreen || fullscreenType) ? lp.width : nlp.width;
                        lp.width = (config == null || fullscreen || fullscreenType) ? lp.width : config.screenWidthDp;
                        break;
                }
                switch (lp.height) {
                    case -1:
                        lp.height = (nlp == null || fullscreen || fullscreenType) ? lp.height : nlp.height;
                        lp.height = (config == null || fullscreen || fullscreenType) ? lp.height : config.screenHeightDp;
                        break;
                }
                xpLogger.log(TAG, "getOverrideLayoutParams global lp=" + lp.toString());
            } catch (Exception e) {
                xpLogger.log(TAG, "getOverrideLayoutParams global e=" + e);
            }
        }
        return lp;
    }

    public static WindowManager.LayoutParams getOverrideLayoutParams(ActivityThread.ActivityClientRecord r) {
        if (r != null) {
            Intent intent = r.getIntent();
            Window window = r.getWindow();
            if (intent != null && window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                int activityFlags = ActivityInfoManager.getActivityFlags(intent);
                if (lp != null) {
                    lp.type = 1;
                    boolean fullscreen = (activityFlags & 64) == 64;
                    boolean isHome = (activityFlags & 536870912) == 536870912;
                    boolean isPanel = (activityFlags & 256) == 256;
                    boolean isFreeform = (activityFlags & 512) == 512;
                    boolean isAutomatic = (activityFlags & 134217728) == 134217728;
                    boolean isSecondary = (activityFlags & 268435456) == 268435456;
                    if (isPanel) {
                        lp.type = 6;
                    } else if (fullscreen) {
                        lp.type = 6;
                        lp.flags |= 1024;
                        lp.systemUiVisibility |= 4;
                        lp.systemUiVisibility |= 1024;
                        lp.systemUiVisibility |= 2;
                        lp.systemUiVisibility = 512 | lp.systemUiVisibility;
                    } else if (isHome) {
                        lp.type = 5;
                    } else if (isFreeform) {
                        lp.type = 12;
                    } else if (isSecondary) {
                        lp.type = 8;
                    }
                    if (isAutomatic) {
                        lp.type = 1;
                        lp.xpFlags |= 8;
                    } else {
                        lp.xpFlags &= -9;
                    }
                    if (fullscreen) {
                        lp.xpFlags |= 4;
                    } else {
                        lp.xpFlags &= -5;
                    }
                    if ((activityFlags & 1073741824) != 1073741824) {
                        lp.flags &= -513;
                    }
                    if (lp != null) {
                        lp.copyFrom(getOverrideLayoutParams(lp));
                    }
                    return lp;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public static WindowManager.LayoutParams getOverrideLayoutParams(WindowManager.LayoutParams lp, Dialog dialog) {
        WindowManager.LayoutParams olp;
        if (lp != null && dialog != null) {
            Activity owner = getDialogOwnerActivity(dialog);
            boolean isApplicationAlert = isApplicationAlertWindowType(lp);
            boolean miniProgram = MiniProgramManager.isMiniProgramDialog(owner);
            if (isApplicationAlert && owner != null && (olp = owner.getWindow().getAttributes()) != null) {
                lp.parentType = olp.type;
                lp.flags = olp.flags;
                lp.systemUiVisibility = olp.systemUiVisibility;
                lp.subtreeSystemUiVisibility = olp.subtreeSystemUiVisibility;
                int i = olp.type;
                if (i != 6 && i == 12) {
                    miniProgram = true;
                }
            }
            if (miniProgram) {
                lp.type = 12;
                lp.xpFlags |= 1;
                lp.parentType = 12;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("getOverrideLayoutParams lp.type=");
            sb.append(lp.type);
            sb.append(" lp.parentType=");
            sb.append(lp.parentType);
            sb.append(" owner=");
            sb.append(owner != null ? owner.getComponentName() : "");
            xpLogger.log(TAG, sb.toString());
        }
        return lp;
    }

    public static WindowManager.LayoutParams getOverrideLayoutParams(WindowManager.LayoutParams lp, PopupWindow popup) {
        if (lp != null && popup != null) {
            try {
                View content = popup.getContentView();
                String packageName = content != null ? content.getContext().getBasePackageName() : "";
                boolean systemApplication = ActivityInfoManager.isSystemApplication(packageName);
                if (!systemApplication) {
                    IBinder b = ServiceManager.getService("package");
                    IPackageManager pm = IPackageManager.Stub.asInterface(b);
                    xpPackageInfo info = pm.getXpPackageInfo(packageName);
                    boolean isFullscreen = xpPackageInfo.isFullscreen(info);
                    if (isFullscreen) {
                        lp.flags = getWindowFlags(lp.flags, true, true, true, true, false);
                        lp.systemUiVisibility = getSystemUiVisibility(lp.systemUiVisibility, true, true, true, true, false);
                        lp.subtreeSystemUiVisibility = lp.systemUiVisibility;
                    }
                }
            } catch (Exception e) {
            }
        }
        return lp;
    }

    public static WindowManager.LayoutParams getOverrideLayoutParams(WindowManager.LayoutParams lp) {
        if (lp != null) {
            StringBuffer buffer = new StringBuffer();
            int type = lp.type;
            int flags = lp.flags;
            int systemUiVisibility = lp.systemUiVisibility;
            boolean isAutoscreen = isAutoscreen(lp.xpFlags);
            boolean isFullscreen = isFullscreen(lp.subtreeSystemUiVisibility | systemUiVisibility, lp.flags, lp.xpFlags);
            boolean immersive = false;
            boolean fullscreen = false;
            boolean hideNavigation = false;
            boolean layoutFullscreen = false;
            buffer.append("getOverrideLayoutParams");
            buffer.append(" type=" + type);
            buffer.append(" flags=" + Integer.toHexString(flags));
            buffer.append(" systemUiVisibility=" + Integer.toHexString(systemUiVisibility));
            if (type == 6) {
                if (isFullscreen) {
                    immersive = true;
                    fullscreen = true;
                    hideNavigation = true;
                    layoutFullscreen = true;
                }
                boolean hasFullscreenFlag = hasFullscreenFlag(systemUiVisibility, flags);
                boolean hasHideNavigationFlag = hasHideNavigationFlag(systemUiVisibility, flags);
                if (hasFullscreenFlag) {
                    fullscreen = true;
                    layoutFullscreen = true;
                }
                if (hasHideNavigationFlag) {
                    hideNavigation = true;
                }
                if (hasFullscreenFlag && hasHideNavigationFlag) {
                    immersive = true;
                }
            } else {
                if (type != 9) {
                    if (type != 12) {
                        if (type != 1000) {
                            if (type != 2042) {
                                switch (type) {
                                }
                            }
                        } else if (isFullscreen) {
                            immersive = true;
                            fullscreen = true;
                            hideNavigation = true;
                            layoutFullscreen = true;
                        }
                    }
                    if (isAutoscreen) {
                        fullscreen = isFullscreen;
                        hideNavigation = isFullscreen;
                        layoutFullscreen = isFullscreen;
                    } else {
                        fullscreen = false;
                        hideNavigation = false;
                        layoutFullscreen = false;
                    }
                }
                boolean hasFullscreenFlag2 = hasFullscreenFlag(systemUiVisibility, flags);
                boolean hasHideNavigationFlag2 = hasHideNavigationFlag(systemUiVisibility, flags);
                if (hasFullscreenFlag2) {
                    fullscreen = true;
                    layoutFullscreen = true;
                }
                if (hasHideNavigationFlag2) {
                    hideNavigation = true;
                }
                if (hasFullscreenFlag2 && hasHideNavigationFlag2) {
                    immersive = true;
                }
            }
            boolean immersive2 = immersive;
            boolean fullscreen2 = fullscreen;
            boolean hideNavigation2 = hideNavigation;
            boolean layoutFullscreen2 = layoutFullscreen;
            lp.flags = getWindowFlags(flags, immersive2, fullscreen2, layoutFullscreen2, hideNavigation2, false);
            lp.systemUiVisibility = getSystemUiVisibility(systemUiVisibility, immersive2, fullscreen2, layoutFullscreen2, hideNavigation2, false);
            lp.subtreeSystemUiVisibility = lp.systemUiVisibility;
        }
        return lp;
    }

    public static int getOverrideSubtreeSystemUiVisibility(WindowManager.LayoutParams lp, int systemUiVisibility) {
        if (lp == null) {
            return systemUiVisibility;
        }
        int type = lp.type;
        if (type != 12) {
            return systemUiVisibility;
        }
        boolean currentFullscreen = isFullscreen(lp.systemUiVisibility | systemUiVisibility, lp.flags, lp.xpFlags);
        if (!currentFullscreen) {
            return systemUiVisibility;
        }
        int subtreeSystemUiVisibility = getSystemUiVisibility(systemUiVisibility, false, false, false, false, false);
        return subtreeSystemUiVisibility;
    }

    public static int getOverrideWindowType(int windowType, String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            boolean systemApplication = xpActivityManager.isSystemApplication(packageName);
            if (systemApplication) {
                return windowType;
            }
            if (windowType == 12) {
                return 7;
            }
        }
        return windowType;
    }

    public static int[] getOverrideRoundCorner(int[] roundCorner, WindowManager.LayoutParams lp) {
        if (lp != null && roundCorner != null && roundCorner.length == 3 && lp.type == 12) {
            boolean isDialog = (lp.xpFlags & 1) == 1;
            if (isDialog) {
                roundCorner[0] = 0;
                roundCorner[1] = 0;
                roundCorner[2] = 0;
            }
        }
        return roundCorner;
    }

    public static boolean isAutoscreen(int xpFlags) {
        return (xpFlags & 8) == 8;
    }

    public static boolean isFullscreen(int systemUiVisibility, int flags) {
        boolean fullscreen = hasFullscreenFlag(systemUiVisibility, flags);
        boolean hideNavigationBar = hasHideNavigationFlag(systemUiVisibility, flags);
        return fullscreen && hideNavigationBar;
    }

    public static boolean isFullscreen(int systemUiVisibility, int flags, int xpFlags) {
        boolean fullscreen = hasFullscreenFlag(systemUiVisibility, flags);
        boolean hideNavigationBar = hasHideNavigationFlag(systemUiVisibility, flags);
        boolean xpFullscreen = (xpFlags & 4) == 4;
        boolean xpAutoscreen = (xpFlags & 8) == 8;
        if (xpAutoscreen) {
            return xpFullscreen;
        }
        return (fullscreen && hideNavigationBar) || xpFullscreen;
    }

    public static boolean hasFullscreenFlag(int systemUiVisibility, int flags) {
        if ((systemUiVisibility & 4) == 4 || (flags & 1024) == 1024) {
            return true;
        }
        return false;
    }

    public static boolean hasHideNavigationFlag(int systemUiVisibility, int flags) {
        if ((systemUiVisibility & 2) == 2 || (systemUiVisibility & 512) == 512) {
            return true;
        }
        return false;
    }

    public static int getSystemUiVisibility(int systemUiVisibility, boolean immersive, boolean fullscreen, boolean layoutFullscreen, boolean hideNavigation, boolean translucentSystemUI) {
        int systemUiVisibility2;
        int systemUiVisibility3;
        int systemUiVisibility4;
        int systemUiVisibility5;
        if (immersive) {
            systemUiVisibility2 = systemUiVisibility | 4096;
        } else {
            systemUiVisibility2 = systemUiVisibility & (-2049) & (-4097);
        }
        if (fullscreen) {
            systemUiVisibility3 = systemUiVisibility2 | 4;
        } else {
            systemUiVisibility3 = systemUiVisibility2 & (-5);
        }
        if (layoutFullscreen) {
            systemUiVisibility4 = systemUiVisibility3 | 1024;
        } else {
            systemUiVisibility4 = systemUiVisibility3 & (-1025);
        }
        if (hideNavigation) {
            systemUiVisibility5 = systemUiVisibility4 | 2 | 512;
        } else {
            systemUiVisibility5 = systemUiVisibility4 & (-3) & (-513);
        }
        if (translucentSystemUI) {
            return systemUiVisibility5 | 32776;
        }
        return systemUiVisibility5 & (-32777);
    }

    public static int getWindowFlags(int flags, boolean immersive, boolean fullscreen, boolean layoutFullscreen, boolean hideNavigation, boolean translucentSystemUI) {
        int flags2;
        if (fullscreen) {
            flags2 = (flags | 1024) & (-2049);
        } else {
            flags2 = flags & (-1025) & (-2049);
        }
        if (translucentSystemUI) {
            return flags2 | 134217728;
        }
        return flags2 & (-134217729);
    }

    public static boolean isDesktopHome() {
        return FeatureOption.FO_HOME_DESKTOP_SUPPORT;
    }

    public static boolean keepActivityShown(ActivityThread.ActivityClientRecord r, boolean show) {
        if (isDesktopHome() && r != null && r.getIntent().hasCategory(Intent.CATEGORY_HOME)) {
            return true;
        }
        return show;
    }

    public static boolean skipSystemUiVisibility(String processName) {
        String[] strArr;
        if (!TextUtils.isEmpty(processName)) {
            for (String val : SKIP_SYSTEMUI_VISIBILITY_PROCESS) {
                if (processName.equals(val)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean packagesLunchFullscreen(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            IBinder b = ServiceManager.getService("package");
            IPackageManager pm = IPackageManager.Stub.asInterface(b);
            xpPackageInfo info = pm.getXpPackageInfo(packageName);
            boolean fullscreen = xpPackageInfo.isFullscreen(info);
            return fullscreen;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isComponentValid(ComponentName component) {
        return (component == null || TextUtils.isEmpty(component.getPackageName()) || TextUtils.isEmpty(component.getClassName())) ? false : true;
    }

    public static Rect getRealDisplayRect(Context context) {
        if (context != null) {
            try {
                Rect rect = new Rect();
                DisplayMetrics metrics = new DisplayMetrics();
                context.getDisplay().getRealMetrics(metrics);
                rect.set(0, 0, metrics.widthPixels, metrics.heightPixels);
                return rect;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static Rect getRealFullscreenRect() {
        Rect rect = new Rect();
        rect.setEmpty();
        try {
            IWindowManager wm = getWindowManager();
            if (wm != null) {
                Rect out = wm.getRealDisplayRect();
                rect.set(out);
            }
        } catch (Exception e) {
        }
        return rect;
    }

    public static Rect getOverrideToastWindowFrame(Context context, WindowManager.LayoutParams lp) {
        if (context != null && lp != null && lp.type == 2005) {
            String packageName = !TextUtils.isEmpty(lp.packageName) ? lp.packageName : context.getBasePackageName();
            boolean miniProgram = MiniProgramManager.isMiniProgramPackage(packageName);
            if (!miniProgram) {
                return getRealFullscreenRect();
            }
            return null;
        }
        return null;
    }

    public static WindowInfo getWindowInfo(WindowManager.LayoutParams windowLp) {
        return getWindowInfo(windowLp, null);
    }

    public static WindowInfo getWindowInfo(WindowManager.LayoutParams windowLp, WindowManager.LayoutParams parentLp) {
        int parentUiVisibility;
        Rect rect;
        Rect fullscreenRect;
        try {
            IBinder binder = ServiceManager.getService(Context.WINDOW_SERVICE);
            IWindowManager wm = binder != null ? IWindowManager.Stub.asInterface(binder) : null;
            if (wm != null && windowLp != null) {
                try {
                    WindowInfo info = new WindowInfo();
                    int systemUiVisibility = windowLp.systemUiVisibility | windowLp.subtreeSystemUiVisibility;
                    if (parentLp != null) {
                        try {
                            parentUiVisibility = parentLp.systemUiVisibility | parentLp.subtreeSystemUiVisibility;
                        } catch (Exception e) {
                            return null;
                        }
                    } else {
                        parentUiVisibility = 0;
                    }
                    info.windowType = windowLp.type;
                    info.parentType = parentLp != null ? parentLp.type : windowLp.parentType;
                    boolean windowFullscreen = isFullscreen(systemUiVisibility, windowLp.flags, parentLp != null ? parentLp.xpFlags : 0);
                    boolean parentFullscreen = isFullscreen(parentUiVisibility, parentLp != null ? parentLp.flags : 0, parentLp != null ? parentLp.xpFlags : 0);
                    boolean application = isApplicationWindowType(windowLp);
                    boolean systemAlert = isSystemAlertWindowType(windowLp);
                    boolean applicationAlert = isApplicationAlertWindowType(windowLp);
                    Rect rect2 = wm.getXuiRectByType(info.windowType);
                    if (application) {
                        info.roundCorner = wm.getXuiRoundCorner(2);
                        info.rect = rect2;
                        info.dimmerBounds = windowFullscreen ? null : rect2;
                    } else if (systemAlert) {
                        info.roundCorner = wm.getXuiRoundCorner(2);
                        info.rect = wm.getXuiRectByType(2008);
                        info.dimmerBounds = null;
                    } else if (applicationAlert) {
                        int targetWindow = info.parentType != 0 ? info.parentType : info.windowType;
                        info.roundCorner = wm.getXuiRoundCorner(targetWindow);
                        info.rect = wm.getXuiRectByType(targetWindow);
                        if (!windowFullscreen && !parentFullscreen) {
                            rect = info.rect;
                            info.dimmerBounds = rect;
                        }
                        rect = null;
                        info.dimmerBounds = rect;
                    } else {
                        info.roundCorner = null;
                        info.rect = rect2;
                        info.dimmerBounds = windowFullscreen ? null : rect2;
                    }
                    int i = info.windowType;
                    try {
                        if (i != 9) {
                            if (i != 12) {
                                switch (i) {
                                    case 5:
                                        Rect app = wm.getXuiRectByType(2);
                                        if (app != null && rect2 != null) {
                                            int left = app.left;
                                            int top = app.top;
                                            int right = app.right;
                                            int bottom = app.bottom;
                                            if (!rect2.equals(app)) {
                                                bottom = rect2.bottom;
                                            }
                                            info.touchRegion = new Rect(left, top, right, bottom);
                                            return info;
                                        }
                                        break;
                                    case 6:
                                        info.touchRegion = rect2;
                                        if (rect2 != null && windowFullscreen) {
                                            info.touchRegion.set(0, 0, rect2.right, rect2.bottom);
                                        }
                                }
                            } else {
                                info.touchRegion = rect2;
                            }
                        } else if (!windowFullscreen) {
                            if (info.parentType == 5) {
                                WindowInfo homeInfo = getWindowInfo(new WindowManager.LayoutParams(5));
                                if (homeInfo != null) {
                                    info.touchRegion = homeInfo.touchRegion;
                                }
                            } else {
                                info.touchRegion = wm.getXuiRectByType(2);
                            }
                        }
                        if (windowFullscreen && (fullscreenRect = getRealFullscreenRect()) != null && !fullscreenRect.isEmpty()) {
                            info.touchRegion = fullscreenRect;
                        }
                        return info;
                    } catch (Exception e2) {
                        return null;
                    }
                } catch (Exception e3) {
                    return null;
                }
            }
            return null;
        } catch (Exception e4) {
            return null;
        }
    }

    public static boolean isAlertWindowType(WindowManager.LayoutParams lp) {
        return isSystemAlertWindowType(lp) || isApplicationAlertWindowType(lp);
    }

    public static boolean isApplicationWindowType(int windowType) {
        if (windowType >= 1 && windowType <= 99) {
            return true;
        }
        return false;
    }

    public static boolean isApplicationWindowType(WindowManager.LayoutParams lp) {
        if (lp != null) {
            int i = lp.type;
            if (i != 12) {
                switch (i) {
                }
            }
            boolean isDialog = (lp.xpFlags & 1) == 1;
            return !isDialog;
        }
        return false;
    }

    public static boolean isSystemAlertWindowType(int windowType) {
        switch (windowType) {
            case 2002:
            case 2003:
            case 2006:
            case 2007:
            case 2008:
            case WindowManager.LayoutParams.TYPE_SYSTEM_ERROR /* 2010 */:
            case WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY /* 2038 */:
            case 2047:
            case 2048:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSystemAlertWindowType(WindowManager.LayoutParams lp) {
        if (lp != null) {
            return isSystemAlertWindowType(lp.type);
        }
        return false;
    }

    public static boolean isApplicationAlertWindowType(WindowManager.LayoutParams lp) {
        if (lp != null) {
            int i = lp.type;
            if (i == 9) {
                return true;
            }
            if (i != 12) {
                switch (i) {
                }
            }
            boolean isDialog = (lp.xpFlags & 1) == 1;
            if (isDialog) {
                return true;
            }
        }
        return false;
    }

    public static Activity getDialogOwnerActivity(Dialog dialog) {
        if (dialog != null) {
            try {
                Context context = dialog.getContext();
                Activity activity = dialog.getOwnerActivity();
                while (activity == null && context != null) {
                    if (context instanceof Activity) {
                        activity = (Activity) context;
                    } else if (context instanceof ContextWrapper) {
                        context = ((ContextWrapper) context).getBaseContext();
                    } else {
                        context = null;
                    }
                }
                return activity;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static int getDialogOwnerActivityType(Dialog dialog) {
        Activity activity = getDialogOwnerActivity(dialog);
        if (activity == null) {
            return 0;
        }
        int windowType = activity.getWindow().getAttributes().type;
        return windowType;
    }

    public static long getToastDurationMillis(int duration) {
        switch (duration) {
            case 0:
                return TOAST_DURATION_SHORT;
            case 1:
                return TOAST_DURATION_LONG;
            case 2:
                return TOAST_DURATION_LONGER;
            default:
                return TOAST_DURATION_SHORT;
        }
    }

    public static IWindowManager getWindowManager() {
        IBinder b = ServiceManager.getService(Context.WINDOW_SERVICE);
        return IWindowManager.Stub.asInterface(b);
    }

    public static void triggerWindowErrorEvent(Context context, Object object) {
        if (object != null && context != null) {
            try {
                WindowErrorInfo info = new WindowErrorInfo();
                if (object instanceof Dialog) {
                    Dialog dialog = (Dialog) object;
                    info.errorType = "dialog";
                    info.className = dialog != null ? dialog.getClass().toString() : "";
                    info.packageName = dialog != null ? dialog.getContext().getBasePackageName() : "";
                } else if (object instanceof ActivityInfo) {
                    ActivityInfo ai = (ActivityInfo) object;
                    ComponentName component = ai != null ? ai.getComponentName() : null;
                    info.errorType = Context.ACTIVITY_SERVICE;
                    info.className = component != null ? component.getClassName() : "";
                    info.packageName = component != null ? component.getPackageName() : "";
                }
                if (!TextUtils.isEmpty(info.packageName) && !TextUtils.isEmpty(info.className)) {
                    sendWindowErrorBroadcast(context, info);
                }
            } catch (Exception e) {
            }
        }
    }

    public static void sendWindowErrorBroadcast(Context context, WindowErrorInfo info) {
        try {
            Intent intent = new Intent("com.xiaopeng.intent.action.XUI_WINDOW_ERROR");
            intent.addFlags(1344274432);
            if (info != null && !TextUtils.isEmpty(info.packageName)) {
                intent.setPackage(info.packageName);
                intent.putExtra("android.intent.extra.errorType", info.errorType);
                intent.putExtra("android.intent.extra.className", info.className);
                intent.putExtra("android.intent.extra.packageName", info.packageName);
                xpLogger.i(TAG, "sendWindowErrorBroadcast errorType=" + info.errorType + " className=" + info.className + " packageName=" + info.packageName);
            }
            context.sendBroadcastAsUser(intent, UserHandle.ALL);
        } catch (Exception e) {
        }
    }
}
